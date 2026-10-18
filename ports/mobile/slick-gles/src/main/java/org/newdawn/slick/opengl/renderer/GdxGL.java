package org.newdawn.slick.opengl.renderer;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.NumberUtils;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import org.newdawn.slick.opengl.TextureImpl;

/**
 * Emulates the slice of OpenGL 1.x fixed-function pipeline that Slick2D uses, on top of GLES 2.0 via libGDX.
 *
 * <p>Vertices from glBegin/glVertex are transformed by the model-view matrix on the CPU and batched; the batch is
 * flushed whenever any state that affects rasterisation changes (texture, blend, colour mask, scissor, clip planes,
 * projection, primitive class). Clip planes are evaluated per-fragment, and GL_COLOR_SUM (secondary colour, used
 * by Slick's Image.drawFlash) is a per-vertex attribute.
 *
 * <p>Coordinates handed to glScissor/glReadPixels/glCopyTexImage2D are in logical pixels of the current render
 * target and are multiplied by {@link #targetScale} (the back-buffer can be larger than the game's logical screen).
 */
public class GdxGL implements SGL {
   private static final int FLOATS_PER_VERTEX = 6;
   private static final int MAX_VERTICES = 6 * 8192;
   private static final int GL_TEXTURE_2D = 3553;
   private static final int GL_BLEND = 3042;
   private static final int GL_SCISSOR_TEST = 3089;
   private static final int GL_CLIP_PLANE0 = 12288;
   private static final int GL_COLOR_SUM = 33880;
   private static final int GL_MODELVIEW_MATRIX = 2982;
   private static final int GL_PROJECTION_MATRIX = 2983;
   private static final int GL_COLOR_CLEAR_VALUE = 3106;
   private static final int GL_MAX_TEXTURE_SIZE = 3379;
   private static final int GL_CLAMP = 10496;
   private static final int GL_CLAMP_TO_EDGE = 33071;
   private static final int GL_MIRROR_CLAMP = 34627;
   private static final int GL_RGBA8 = 32856;
   private static final int GL_RGB8 = 32849;
   private static final int GL_RGBA = 6408;
   private static final int GL_RGB = 6407;
   private static final int GL_ALPHA = 6406;
   private static final int GL_LUMINANCE = 6409;
   private static final int GL_LUMINANCE_ALPHA = 6410;
   private static final int GL_BGRA = 32993;
   private static final int GL_PROGRAM_POINT_SIZE = 0x8642;

   private static final int BATCH_TRIANGLES = GL20.GL_TRIANGLES;
   private static final int BATCH_LINES = GL20.GL_LINES;
   private static final int BATCH_POINTS = GL20.GL_POINTS;

   private static final String VERTEX_SHADER = "attribute vec4 a_position;\n"
      + "attribute vec4 a_color;\n"
      + "attribute vec2 a_texCoord0;\n"
      + "attribute vec4 a_secondary;\n"
      + "uniform mat4 u_proj;\n"
      + "uniform float u_pointSize;\n"
      + "varying vec4 v_color;\n"
      + "varying vec2 v_uv;\n"
      + "varying vec4 v_sec;\n"
      + "varying vec2 v_eye;\n"
      + "void main() {\n"
      + "  v_color = a_color;\n"
      + "  v_uv = a_texCoord0;\n"
      + "  v_sec = a_secondary;\n"
      + "  v_eye = a_position.xy;\n"
      + "  gl_PointSize = u_pointSize;\n"
      + "  gl_Position = u_proj * vec4(a_position.xy, 0.0, 1.0);\n"
      + "}\n";
   // highp: texture coordinates into large sheets (and atlas pages) need more than fp16 precision.
   private static final String FRAGMENT_SHADER = "#ifdef GL_ES\n"
      + "#ifdef GL_FRAGMENT_PRECISION_HIGH\n"
      + "precision highp float;\n"
      + "#else\n"
      + "precision mediump float;\n"
      + "#endif\n"
      + "#endif\n"
      + "uniform sampler2D u_texture;\n"
      + "uniform vec4 u_plane0;\n"
      + "uniform vec4 u_plane1;\n"
      + "uniform vec4 u_plane2;\n"
      + "uniform vec4 u_plane3;\n"
      + "uniform vec4 u_planeOn;\n"
      + "varying vec4 v_color;\n"
      + "varying vec2 v_uv;\n"
      + "varying vec4 v_sec;\n"
      + "varying vec2 v_eye;\n"
      + "void main() {\n"
      + "  vec4 p = vec4(v_eye, 0.0, 1.0);\n"
      + "  if (u_planeOn.x > 0.5 && dot(u_plane0, p) < 0.0) discard;\n"
      + "  if (u_planeOn.y > 0.5 && dot(u_plane1, p) < 0.0) discard;\n"
      + "  if (u_planeOn.z > 0.5 && dot(u_plane2, p) < 0.0) discard;\n"
      + "  if (u_planeOn.w > 0.5 && dot(u_plane3, p) < 0.0) discard;\n"
      // u < -1.5 marks a vertex drawn with GL_TEXTURE_2D off, so shapes and sprites can share one batch.
      + "  vec4 c = (v_uv.x < -1.5 ? vec4(1.0) : texture2D(u_texture, v_uv)) * v_color;\n"
      + "  c.rgb = min(c.rgb + v_sec.rgb * v_sec.a, vec3(1.0));\n"
      + "  gl_FragColor = c;\n"
      + "}\n";

   private static final float UNTEXTURED_U = -2.0F;
   private static final boolean TEX_STATS = Boolean.getBoolean("rtr.texStats");
   private final Map<Integer, int[]> switchCounts = new HashMap<Integer, int[]>();
   private long texStatsAt;

   /** Debug (-Drtr.texStats): which textures end batches most, every 5 s. */
   private void dumpTexStats() {
      long now = System.currentTimeMillis();
      if (now - this.texStatsAt < 5000L) {
         return;
      }

      this.texStatsAt = now;
      java.util.ArrayList<Map.Entry<Integer, int[]>> list = new java.util.ArrayList<Map.Entry<Integer, int[]>>(this.switchCounts.entrySet());
      java.util.Collections.sort(list, new java.util.Comparator<Map.Entry<Integer, int[]>>() {
         @Override
         public int compare(Map.Entry<Integer, int[]> a, Map.Entry<Integer, int[]> b) {
            return b.getValue()[0] - a.getValue()[0];
         }
      });
      StringBuilder sb = new StringBuilder("[tex] batch-ending textures per 5 s:");

      for (int i = 0; i < Math.min(14, list.size()); i++) {
         int[] size = this.textureSizes.get(list.get(i).getKey());
         sb.append(' ').append(list.get(i).getKey()).append(size == null ? "" : "(" + size[0] + "x" + size[1] + ")").append('=').append(list.get(i).getValue()[0]);
      }

      System.out.println(sb);
      this.switchCounts.clear();
   }
   /** Whether the pending batch has textured vertices (only those care which texture is bound). */
   private boolean pendingTextured;
   /** Last state actually sent to GL by flush(); invalid after anything else may have touched GL state. */
   private boolean glStateValid;
   private boolean appliedBlend;
   private int appliedSrc;
   private int appliedDst;
   private final boolean[] appliedMask = new boolean[4];
   private boolean appliedScissor;
   private final int[] appliedScissorBox = new int[4];
   private final float[] appliedProj = new float[16];
   private final float[][] appliedPlanes = new float[4][4];
   private final boolean[] appliedPlaneOn = new boolean[4];
   private float appliedPointSize = -1.0F;

   /** Forget what flush() last sent to GL (call after code outside this class may have changed GL state). */
   public void invalidateGlState() {
      this.glStateValid = false;
   }

   private ShaderProgram shader;
   private Mesh mesh;
   private final float[] vertices = new float[MAX_VERTICES * FLOATS_PER_VERTEX];
   private int vertexCount;
   private int batchType = BATCH_TRIANGLES;
   private int whiteTexture;
   private boolean initialised;

   private final Matrix4 modelView = new Matrix4();
   private final Matrix4 projection = new Matrix4();
   private final ArrayDeque<Matrix4> stack = new ArrayDeque<Matrix4>();
   private final Matrix4 tmp = new Matrix4();

   private float r = 1.0F;
   private float g = 1.0F;
   private float b = 1.0F;
   private float a = 1.0F;
   private float alphaScale = 1.0F;
   private float packedColor = packColor(1.0F, 1.0F, 1.0F, 1.0F);
   private float u;
   private float v;
   private float packedSecondary = 0.0F;
   private int secR;
   private int secG;
   private int secB;
   private boolean colorSum;

   private boolean texture2D = true;
   private int boundTexture;
   private boolean blend = true;
   private int srcBlend = GL20.GL_SRC_ALPHA;
   private int dstBlend = GL20.GL_ONE_MINUS_SRC_ALPHA;
   private final boolean[] colorMask = new boolean[]{true, true, true, true};
   private boolean scissor;
   private final int[] scissorBox = new int[4];
   private final float[][] planes = new float[4][4];
   private final boolean[] planeOn = new boolean[4];
   private float lineWidth = 1.0F;
   private float pointSize = 1.0F;
   private final float[] clearColor = new float[4];

   private int primitive = -1;
   private int primCount;
   private final float[] primX = new float[4];
   private final float[] primY = new float[4];
   private final float[] primU = new float[4];
   private final float[] primV = new float[4];
   private final float[] primC = new float[4];
   private final float[] primS = new float[4];
   private float firstX;
   private float firstY;
   private float firstU;
   private float firstV;
   private float firstC;
   private float firstS;

   private float targetScale = 1.0F;
   private final Map<Integer, int[]> textureSizes = new HashMap<Integer, int[]>();
   private int readbackFbo;

   private static float packColor(float r, float g, float b, float a) {
      int ir = (int)(255.0F * clamp(r));
      int ig = (int)(255.0F * clamp(g));
      int ib = (int)(255.0F * clamp(b));
      int ia = (int)(255.0F * clamp(a));
      return NumberUtils.intToFloatColor(ia << 24 | ib << 16 | ig << 8 | ir);
   }

   private static float clamp(float f) {
      return f < 0.0F ? 0.0F : (f > 1.0F ? 1.0F : f);
   }

   private void ensureInit() {
      if (!this.initialised) {
         this.initialised = true;
         ShaderProgram.pedantic = false;
         this.shader = new ShaderProgram(VERTEX_SHADER, FRAGMENT_SHADER);
         if (!this.shader.isCompiled()) {
            throw new IllegalStateException("Slick GLES shader failed: " + this.shader.getLog());
         }

         this.mesh = new Mesh(
            false,
            MAX_VERTICES,
            0,
            new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_position"),
            new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, GL20.GL_UNSIGNED_BYTE, true, "a_color"),
            new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_texCoord0"),
            new VertexAttribute(VertexAttributes.Usage.Generic, 4, GL20.GL_UNSIGNED_BYTE, true, "a_secondary")
         );
         IntBuffer ids = BufferUtils.newIntBuffer(1);
         Gdx.gl.glGenTextures(1, ids);
         this.whiteTexture = ids.get(0);
         Gdx.gl.glBindTexture(GL20.GL_TEXTURE_2D, this.whiteTexture);
         ByteBuffer white = BufferUtils.newByteBuffer(4);
         white.put((byte)-1).put((byte)-1).put((byte)-1).put((byte)-1).flip();
         Gdx.gl.glTexImage2D(GL20.GL_TEXTURE_2D, 0, GL20.GL_RGBA, 1, 1, 0, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE, white);
         Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MIN_FILTER, GL20.GL_NEAREST);
         Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MAG_FILTER, GL20.GL_NEAREST);
         if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            Gdx.gl.glEnable(GL_PROGRAM_POINT_SIZE);
         }
      }
   }

   // ---------------------------------------------------------------- render targets

   /** Everything glPushAttrib/glPushMatrix would have saved around an off-screen render. */
   public static final class State {
      final Matrix4 modelView = new Matrix4();
      final Matrix4 projection = new Matrix4();
      final ArrayDeque<Matrix4> stack = new ArrayDeque<Matrix4>();
      float r;
      float g;
      float b;
      float a;
      boolean texture2D;
      int boundTexture;
      boolean blend;
      int srcBlend;
      int dstBlend;
      final boolean[] colorMask = new boolean[4];
      boolean scissor;
      final int[] scissorBox = new int[4];
      final float[][] planes = new float[4][4];
      final boolean[] planeOn = new boolean[4];
      float lineWidth;
      float pointSize;
      final float[] clearColor = new float[4];
      float targetScale;
      final int[] viewport = new int[4];
      int framebuffer;
   }

   public State saveState() {
      this.flush();
      this.glStateValid = false;
      State s = new State();
      s.modelView.set(this.modelView);
      s.projection.set(this.projection);

      for (Matrix4 m : this.stack) {
         s.stack.addLast(new Matrix4(m));
      }

      s.r = this.r;
      s.g = this.g;
      s.b = this.b;
      s.a = this.a;
      s.texture2D = this.texture2D;
      s.boundTexture = this.boundTexture;
      s.blend = this.blend;
      s.srcBlend = this.srcBlend;
      s.dstBlend = this.dstBlend;
      System.arraycopy(this.colorMask, 0, s.colorMask, 0, 4);
      s.scissor = this.scissor;
      System.arraycopy(this.scissorBox, 0, s.scissorBox, 0, 4);

      for (int i = 0; i < 4; i++) {
         System.arraycopy(this.planes[i], 0, s.planes[i], 0, 4);
      }

      System.arraycopy(this.planeOn, 0, s.planeOn, 0, 4);
      s.lineWidth = this.lineWidth;
      s.pointSize = this.pointSize;
      System.arraycopy(this.clearColor, 0, s.clearColor, 0, 4);
      s.targetScale = this.targetScale;
      IntBuffer buf = BufferUtils.newIntBuffer(16);
      Gdx.gl.glGetIntegerv(GL20.GL_VIEWPORT, buf);

      for (int i = 0; i < 4; i++) {
         s.viewport[i] = buf.get(i);
      }

      buf.clear();
      Gdx.gl.glGetIntegerv(GL20.GL_FRAMEBUFFER_BINDING, buf);
      s.framebuffer = buf.get(0);
      return s;
   }

   public void restoreState(State s) {
      this.flush();
      this.glStateValid = false;
      this.modelView.set(s.modelView);
      this.projection.set(s.projection);
      this.stack.clear();
      this.stack.addAll(s.stack);
      this.glColor4f(s.r, s.g, s.b, s.a);
      this.texture2D = s.texture2D;
      this.boundTexture = s.boundTexture;
      Gdx.gl.glBindTexture(GL20.GL_TEXTURE_2D, s.boundTexture);
      this.blend = s.blend;
      this.srcBlend = s.srcBlend;
      this.dstBlend = s.dstBlend;
      System.arraycopy(s.colorMask, 0, this.colorMask, 0, 4);
      this.scissor = s.scissor;
      System.arraycopy(s.scissorBox, 0, this.scissorBox, 0, 4);

      for (int i = 0; i < 4; i++) {
         System.arraycopy(s.planes[i], 0, this.planes[i], 0, 4);
      }

      System.arraycopy(s.planeOn, 0, this.planeOn, 0, 4);
      this.lineWidth = s.lineWidth;
      this.pointSize = s.pointSize;
      System.arraycopy(s.clearColor, 0, this.clearColor, 0, 4);
      this.targetScale = s.targetScale;
      TextureImpl.unbind();
      Gdx.gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, s.framebuffer);
      Gdx.gl.glViewport(s.viewport[0], s.viewport[1], s.viewport[2], s.viewport[3]);
   }

   /** Resets state for drawing into a fresh off-screen target of the given pixel size (Slick's FBOGraphics.initGL). */
   public void beginOffscreen(int framebuffer, int width, int height) {
      this.flush();
      this.glStateValid = false;
      Gdx.gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, framebuffer);
      Gdx.gl.glViewport(0, 0, width, height);
      this.targetScale = 1.0F;
      this.texture2D = true;
      this.blend = true;
      this.srcBlend = GL20.GL_SRC_ALPHA;
      this.dstBlend = GL20.GL_ONE_MINUS_SRC_ALPHA;
      this.colorMask[0] = this.colorMask[1] = this.colorMask[2] = this.colorMask[3] = true;
      this.scissor = false;
      this.planeOn[0] = this.planeOn[1] = this.planeOn[2] = this.planeOn[3] = false;
      this.clearColor[0] = this.clearColor[1] = this.clearColor[2] = this.clearColor[3] = 0.0F;
      this.modelView.idt();
      this.stack.clear();
      this.projection.setToOrtho(0.0F, width, 0.0F, height, 1.0F, -1.0F);
      TextureImpl.unbind();
   }

   /** Scale from logical pixels of the current render target to real pixels. */
   public void setTargetScale(float scale) {
      this.flush();
      this.targetScale = scale;
   }

   public float getTargetScale() {
      return this.targetScale;
   }

   public void setProjectionOrtho(float left, float right, float bottom, float top) {
      this.flush();
      this.projection.setToOrtho(left, right, bottom, top, 1.0F, -1.0F);
   }

   public Matrix4 getProjection() {
      return this.projection;
   }

   // ---------------------------------------------------------------- batching

   @Override
   public void flush() {
      if (this.vertexCount == 0) {
         return;
      }

      this.ensureInit();
      GL20 gl = Gdx.gl;
      // Only send what changed since the last draw: on iOS every GL call crosses the Java/native bridge.
      boolean full = !this.glStateValid;
      if (full || this.blend != this.appliedBlend) {
         if (this.blend) {
            gl.glEnable(GL20.GL_BLEND);
         } else {
            gl.glDisable(GL20.GL_BLEND);
         }

         this.appliedBlend = this.blend;
      }

      if (this.blend && (full || this.srcBlend != this.appliedSrc || this.dstBlend != this.appliedDst)) {
         gl.glBlendFunc(this.srcBlend, this.dstBlend);
         this.appliedSrc = this.srcBlend;
         this.appliedDst = this.dstBlend;
      }

      boolean[] cm = this.colorMask;
      boolean[] am = this.appliedMask;
      if (full || cm[0] != am[0] || cm[1] != am[1] || cm[2] != am[2] || cm[3] != am[3]) {
         gl.glColorMask(cm[0], cm[1], cm[2], cm[3]);
         System.arraycopy(cm, 0, am, 0, 4);
      }

      this.applyScissor(full);
      if (full) {
         gl.glActiveTexture(GL20.GL_TEXTURE0);
      }

      // The real binding may have been changed by texture uploads, so always bind.
      gl.glBindTexture(GL20.GL_TEXTURE_2D, this.boundTexture != 0 ? this.boundTexture : this.whiteTexture);
      if (this.batchType == BATCH_LINES) {
         gl.glLineWidth(Math.max(1.0F, this.lineWidth * this.targetScale));
      }

      if (full) {
         this.shader.bind();
         this.shader.setUniformi("u_texture", 0);
      }

      float[] pv = this.projection.val;
      if (full || !java.util.Arrays.equals(pv, this.appliedProj)) {
         this.shader.setUniformMatrix("u_proj", this.projection);
         System.arraycopy(pv, 0, this.appliedProj, 0, 16);
      }

      float ps = Math.max(1.0F, this.pointSize * this.targetScale);
      if (full || ps != this.appliedPointSize) {
         this.shader.setUniformf("u_pointSize", ps);
         this.appliedPointSize = ps;
      }

      boolean planesChanged = full;

      for (int i = 0; i < 4; i++) {
         if (this.planeOn[i] != this.appliedPlaneOn[i]) {
            planesChanged = true;
         }

         if (this.planeOn[i] && (full || !java.util.Arrays.equals(this.planes[i], this.appliedPlanes[i]))) {
            this.shader.setUniformf(PLANE_UNIFORMS[i], this.planes[i][0], this.planes[i][1], this.planes[i][2], this.planes[i][3]);
            System.arraycopy(this.planes[i], 0, this.appliedPlanes[i], 0, 4);
         }
      }

      if (planesChanged) {
         this.shader.setUniformf(
            "u_planeOn", this.planeOn[0] ? 1.0F : 0.0F, this.planeOn[1] ? 1.0F : 0.0F, this.planeOn[2] ? 1.0F : 0.0F, this.planeOn[3] ? 1.0F : 0.0F
         );
         System.arraycopy(this.planeOn, 0, this.appliedPlaneOn, 0, 4);
      }

      this.glStateValid = true;
      this.pendingTextured = false;
      rtr.compat.PerfStats.drawCalls++;
      rtr.compat.PerfStats.vertices += this.vertexCount;
      this.mesh.setVertices(this.vertices, 0, this.vertexCount * FLOATS_PER_VERTEX);
      this.mesh.render(this.shader, this.batchType, 0, this.vertexCount);
      this.vertexCount = 0;
   }

   private static final String[] PLANE_UNIFORMS = new String[]{"u_plane0", "u_plane1", "u_plane2", "u_plane3"};

   private void applyScissor(boolean full) {
      if (full || this.scissor != this.appliedScissor) {
         if (this.scissor) {
            Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
         } else {
            Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
         }

         this.appliedScissor = this.scissor;
         full = true;
      }

      if (this.scissor) {
         float s = this.targetScale;
         int x = Math.round(this.scissorBox[0] * s);
         int y = Math.round(this.scissorBox[1] * s);
         int w = Math.round(this.scissorBox[2] * s);
         int h = Math.round(this.scissorBox[3] * s);
         int[] a = this.appliedScissorBox;
         if (full || a[0] != x || a[1] != y || a[2] != w || a[3] != h) {
            Gdx.gl.glScissor(x, y, w, h);
            a[0] = x;
            a[1] = y;
            a[2] = w;
            a[3] = h;
         }
      }
   }

   private void ensureBatch(int type, int needed) {
      if (this.batchType != type) {
         this.flush();
         this.batchType = type;
      }

      if (this.vertexCount + needed > MAX_VERTICES) {
         this.flush();
      }
   }

   private void put(float x, float y, float c, float tu, float tv, float s) {
      int i = this.vertexCount * FLOATS_PER_VERTEX;
      this.vertices[i] = x;
      this.vertices[i + 1] = y;
      this.vertices[i + 2] = c;
      if (this.texture2D) {
         this.vertices[i + 3] = tu;
         this.pendingTextured = true;
      } else {
         this.vertices[i + 3] = UNTEXTURED_U;
      }

      this.vertices[i + 4] = tv;
      this.vertices[i + 5] = s;
      this.vertexCount++;
   }

   private void putPrim(int i) {
      this.put(this.primX[i], this.primY[i], this.primC[i], this.primU[i], this.primV[i], this.primS[i]);
   }

   private void putFirst() {
      this.put(this.firstX, this.firstY, this.firstC, this.firstU, this.firstV, this.firstS);
   }

   // ---------------------------------------------------------------- immediate mode

   @Override
   public void glBegin(int mode) {
      this.primitive = mode;
      this.primCount = 0;
   }

   @Override
   public void glEnd() {
      if (this.primitive == 2 && this.primCount > 1) {
         this.ensureBatch(BATCH_LINES, 2);
         this.putPrim(0);
         this.putFirst();
      }

      this.primitive = -1;
      this.primCount = 0;
   }

   @Override
   public void glVertex2f(float x, float y) {
      this.glVertex3f(x, y, 0.0F);
   }

   @Override
   public void glVertex3f(float x, float y, float z) {
      if (this.primitive < 0) {
         return;
      }

      float[] m = this.modelView.val;
      float ex = m[0] * x + m[4] * y + m[12];
      float ey = m[1] * x + m[5] * y + m[13];
      float c = this.packedColor;
      float s = this.colorSum ? this.packedSecondary : 0.0F;
      switch (this.primitive) {
         case 0:
            this.ensureBatch(BATCH_POINTS, 1);
            this.put(ex, ey, c, this.u, this.v, s);
            break;
         case 1:
            this.store(this.primCount & 1, ex, ey, c, s);
            if ((++this.primCount & 1) == 0) {
               this.ensureBatch(BATCH_LINES, 2);
               this.putPrim(0);
               this.putPrim(1);
            }
            break;
         case 2:
         case 3:
            if (this.primCount == 0) {
               this.storeFirst(ex, ey, c, s);
               this.store(0, ex, ey, c, s);
            } else {
               this.ensureBatch(BATCH_LINES, 2);
               this.putPrim(0);
               this.store(0, ex, ey, c, s);
               this.putPrim(0);
            }

            this.primCount++;
            break;
         case 4:
            this.store(this.primCount % 3, ex, ey, c, s);
            if (++this.primCount % 3 == 0) {
               this.ensureBatch(BATCH_TRIANGLES, 3);
               this.putPrim(0);
               this.putPrim(1);
               this.putPrim(2);
            }
            break;
         case 5:
         case 8:
            if (this.primCount < 2) {
               this.store(this.primCount, ex, ey, c, s);
            } else {
               this.ensureBatch(BATCH_TRIANGLES, 3);
               this.putPrim(0);
               this.putPrim(1);
               this.store(2, ex, ey, c, s);
               this.putPrim(2);
               this.copyPrim(1, 0);
               this.copyPrim(2, 1);
            }

            this.primCount++;
            break;
         case 6:
         case 9:
            if (this.primCount == 0) {
               this.storeFirst(ex, ey, c, s);
            } else if (this.primCount == 1) {
               this.store(0, ex, ey, c, s);
            } else {
               this.ensureBatch(BATCH_TRIANGLES, 3);
               this.putFirst();
               this.putPrim(0);
               this.store(0, ex, ey, c, s);
               this.putPrim(0);
            }

            this.primCount++;
            break;
         case 7:
            this.store(this.primCount & 3, ex, ey, c, s);
            if ((++this.primCount & 3) == 0) {
               this.ensureBatch(BATCH_TRIANGLES, 6);
               this.putPrim(0);
               this.putPrim(1);
               this.putPrim(2);
               this.putPrim(0);
               this.putPrim(2);
               this.putPrim(3);
            }
      }
   }

   private void store(int i, float x, float y, float c, float s) {
      this.primX[i] = x;
      this.primY[i] = y;
      this.primC[i] = c;
      this.primU[i] = this.u;
      this.primV[i] = this.v;
      this.primS[i] = s;
   }

   private void storeFirst(float x, float y, float c, float s) {
      this.firstX = x;
      this.firstY = y;
      this.firstC = c;
      this.firstU = this.u;
      this.firstV = this.v;
      this.firstS = s;
   }

   private void copyPrim(int from, int to) {
      this.primX[to] = this.primX[from];
      this.primY[to] = this.primY[from];
      this.primC[to] = this.primC[from];
      this.primU[to] = this.primU[from];
      this.primV[to] = this.primV[from];
      this.primS[to] = this.primS[from];
   }

   @Override
   public void glTexCoord2f(float tu, float tv) {
      this.u = tu;
      this.v = tv;
   }

   @Override
   public void glColor4f(float red, float green, float blue, float alpha) {
      this.r = red;
      this.g = green;
      this.b = blue;
      this.a = alpha;
      this.packedColor = packColor(red, green, blue, alpha * this.alphaScale);
   }

   @Override
   public float[] getCurrentColor() {
      return new float[]{this.r, this.g, this.b, this.a};
   }

   @Override
   public void setGlobalAlphaScale(float scale) {
      this.alphaScale = scale;
      this.glColor4f(this.r, this.g, this.b, this.a);
   }

   @Override
   public void glSecondaryColor3ubEXT(byte red, byte green, byte blue) {
      this.secR = red & 255;
      this.secG = green & 255;
      this.secB = blue & 255;
      this.packedSecondary = NumberUtils.intToFloatColor(255 << 24 | this.secB << 16 | this.secG << 8 | this.secR);
   }

   @Override
   public boolean canSecondaryColor() {
      return true;
   }

   @Override
   public boolean canTextureMirrorClamp() {
      return false;
   }

   // ---------------------------------------------------------------- matrices

   @Override
   public void glLoadIdentity() {
      this.modelView.idt();
   }

   @Override
   public void glTranslatef(float x, float y, float z) {
      this.modelView.translate(x, y, z);
   }

   @Override
   public void glScalef(float x, float y, float z) {
      this.modelView.scale(x, y, z);
   }

   @Override
   public void glRotatef(float angle, float x, float y, float z) {
      this.modelView.rotate(x, y, z, angle);
   }

   @Override
   public void glPushMatrix() {
      this.stack.push(new Matrix4(this.modelView));
   }

   @Override
   public void glPopMatrix() {
      if (!this.stack.isEmpty()) {
         this.modelView.set(this.stack.pop());
      }
   }

   @Override
   public void glLoadMatrix(FloatBuffer buffer) {
      for (int i = 0; i < 16; i++) {
         this.tmp.val[i] = buffer.get(i);
      }

      this.modelView.set(this.tmp);
   }

   public Matrix4 getModelView() {
      return this.modelView;
   }

   // ---------------------------------------------------------------- state

   @Override
   public void initDisplay(int width, int height) {
      this.ensureInit();
      this.captureDefaultFramebuffer();
      this.flush();
      this.texture2D = true;
      this.blend = true;
      this.srcBlend = GL20.GL_SRC_ALPHA;
      this.dstBlend = GL20.GL_ONE_MINUS_SRC_ALPHA;
      this.clearColor[0] = this.clearColor[1] = this.clearColor[2] = this.clearColor[3] = 0.0F;
      Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
      Gdx.gl.glDisable(GL20.GL_CULL_FACE);
      this.bindMainTarget();
      this.modelView.idt();
   }

   // ---------------------------------------------------------------- main screen / scene buffer

   private boolean sceneBufferEnabled;
   private int sceneFbo;
   private int sceneTexture;
   private int sceneWidth;
   private int sceneHeight;
   private int defaultFramebuffer;

   /**
    * When enabled, the game draws into an off-screen buffer at its logical resolution (Display size) and
    * {@link #endScene()} scales it to the screen in one pass. On phones the logical size is ~720p while the panel is
    * 1170p+, so this cuts the pixels shaded by the game's many full-screen layers (lighting, clouds, fog) ~2-3x.
    */
   public void setSceneBufferEnabled(boolean enabled) {
      this.sceneBufferEnabled = enabled;
   }

   private float sceneResolution = 1.0F;

   /**
    * Scene buffer size relative to the logical screen (1 = one texel per logical pixel). Below 1 the game draws fewer
    * pixels and is scaled up more, trading sharpness for GPU time and battery.
    */
   public void setSceneResolution(float scale) {
      this.sceneResolution = Math.max(0.5F, Math.min(1.0F, scale));
   }

   public float getSceneResolution() {
      return this.sceneResolution;
   }

   public boolean isSceneBufferEnabled() {
      return this.sceneBufferEnabled;
   }

   /** Called at the start of every frame: remembers the platform's framebuffer and binds the game's target. */
   public void beginScene() {
      this.ensureInit();
      if (TEX_STATS) {
         this.dumpTexStats();
      }

      // The platform and the phone overlay drew with their own GL state since the last frame.
      this.glStateValid = false;
      this.captureDefaultFramebuffer();
      this.bindMainTarget();
   }

   /**
    * Records the platform's own framebuffer (0 on desktop/Android, GLKView's on iOS, rebound before every frame).
    * Never mistakes the scene buffer for it, which would make the final blit sample the texture it renders into.
    */
   private void captureDefaultFramebuffer() {
      IntBuffer buf = BufferUtils.newIntBuffer(16);
      Gdx.gl.glGetIntegerv(GL20.GL_FRAMEBUFFER_BINDING, buf);
      int bound = buf.get(0);
      if (this.sceneFbo == 0 || bound != this.sceneFbo) {
         this.defaultFramebuffer = bound;
      }
   }

   private void bindMainTarget() {
      this.flush();
      int lw = rtr.compat.lwjgl.opengl.Display.getWidth();
      int lh = rtr.compat.lwjgl.opengl.Display.getHeight();
      if (this.sceneBufferEnabled) {
         int sw = Math.max(1, Math.round(lw * this.sceneResolution));
         int sh = Math.max(1, Math.round(lh * this.sceneResolution));
         if (this.sceneFbo == 0 || this.sceneWidth != sw || this.sceneHeight != sh) {
            this.createSceneBuffer(sw, sh);
         }

         Gdx.gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, this.sceneFbo);
         Gdx.gl.glViewport(0, 0, sw, sh);
         this.targetScale = (float)sw / lw;
      } else {
         Gdx.gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, this.defaultFramebuffer);
         Gdx.gl.glViewport(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
         this.targetScale = rtr.compat.lwjgl.opengl.Display.getRenderScale();
      }
   }

   private void createSceneBuffer(int w, int h) {
      this.glStateValid = false;
      if (this.sceneFbo != 0) {
         Gdx.gl.glDeleteFramebuffer(this.sceneFbo);
         Gdx.gl.glDeleteTexture(this.sceneTexture);
      }

      this.sceneWidth = w;
      this.sceneHeight = h;
      this.sceneTexture = Gdx.gl.glGenTexture();
      Gdx.gl.glBindTexture(GL20.GL_TEXTURE_2D, this.sceneTexture);
      Gdx.gl.glTexImage2D(GL20.GL_TEXTURE_2D, 0, GL20.GL_RGBA, w, h, 0, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE, null);
      Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MIN_FILTER, GL20.GL_LINEAR);
      Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MAG_FILTER, GL20.GL_LINEAR);
      Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_S, GL20.GL_CLAMP_TO_EDGE);
      Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_T, GL20.GL_CLAMP_TO_EDGE);
      Gdx.gl.glBindTexture(GL20.GL_TEXTURE_2D, this.boundTexture);
      this.sceneFbo = Gdx.gl.glGenFramebuffer();
      Gdx.gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, this.sceneFbo);
      Gdx.gl.glFramebufferTexture2D(GL20.GL_FRAMEBUFFER, GL20.GL_COLOR_ATTACHMENT0, GL20.GL_TEXTURE_2D, this.sceneTexture, 0);
      int status = Gdx.gl.glCheckFramebufferStatus(GL20.GL_FRAMEBUFFER);
      if (status != GL20.GL_FRAMEBUFFER_COMPLETE) {
         System.err.println("Scene buffer incomplete (0x" + Integer.toHexString(status) + "), drawing directly to the screen");
         this.sceneBufferEnabled = false;
         Gdx.gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, this.defaultFramebuffer);
      }
   }

   /** Debug: writes the scene buffer's current contents to a PNG. */
   public void dumpSceneBuffer(String path) {
      if (this.sceneFbo == 0) {
         return;
      }

      this.flush();
      IntBuffer prev = BufferUtils.newIntBuffer(16);
      Gdx.gl.glGetIntegerv(GL20.GL_FRAMEBUFFER_BINDING, prev);
      Gdx.gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, this.sceneFbo);
      com.badlogic.gdx.graphics.Pixmap p = com.badlogic.gdx.graphics.Pixmap.createFromFrameBuffer(0, 0, this.sceneWidth, this.sceneHeight);
      com.badlogic.gdx.graphics.PixmapIO.writePNG(Gdx.files.absolute(path), p, -1, true);
      p.dispose();
      Gdx.gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, prev.get(0));
      System.out.println("scene " + this.sceneWidth + "x" + this.sceneHeight + " fbo=" + this.sceneFbo + " default=" + this.defaultFramebuffer);
   }

   /** Called at the end of every frame: scales the scene buffer onto the screen (no-op when disabled). */
   public void endScene() {
      this.flush();
      this.glStateValid = false;
      if (!this.sceneBufferEnabled || this.sceneFbo == 0) {
         return;
      }

      Gdx.gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, this.defaultFramebuffer);
      Gdx.gl.glViewport(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
      Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
      Gdx.gl.glDisable(GL20.GL_BLEND);
      Gdx.gl.glColorMask(true, true, true, true);
      Matrix4 savedProj = new Matrix4(this.projection);
      float[] planesOn = new float[]{0, 0, 0, 0};
      this.shader.bind();
      this.shader.setUniformMatrix("u_proj", this.tmp.setToOrtho(0.0F, 1.0F, 0.0F, 1.0F, 1.0F, -1.0F));
      this.shader.setUniformi("u_texture", 0);
      this.shader.setUniformf("u_planeOn", planesOn[0], planesOn[1], planesOn[2], planesOn[3]);
      Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
      Gdx.gl.glBindTexture(GL20.GL_TEXTURE_2D, this.sceneTexture);
      float white = packColor(1.0F, 1.0F, 1.0F, 1.0F);
      int i = 0;
      float[] v = this.vertices;
      float[][] quad = new float[][]{{0, 0}, {1, 0}, {1, 1}, {0, 0}, {1, 1}, {0, 1}};

      for (float[] q : quad) {
         v[i++] = q[0];
         v[i++] = q[1];
         v[i++] = white;
         v[i++] = q[0];
         v[i++] = q[1];
         v[i++] = 0.0F;
      }

      this.mesh.setVertices(v, 0, i);
      this.mesh.render(this.shader, GL20.GL_TRIANGLES, 0, 6);
      this.projection.set(savedProj);
      Gdx.gl.glBindTexture(GL20.GL_TEXTURE_2D, this.boundTexture);
      org.newdawn.slick.opengl.TextureImpl.unbind();
   }

   @Override
   public void enterOrtho(int width, int height) {
      this.mainWidth = width;
      this.mainHeight = height;
      this.setProjectionOrtho(0.0F, width, height + this.viewOffsetY, this.viewOffsetY);
   }

   private int mainWidth;
   private int mainHeight;
   private float viewOffsetY;

   /**
    * Scrolls the main screen up by {@code offset} logical pixels (keeps a focused text field above the on-screen
    * keyboard). Call only while the main screen, not an off-screen image, is the render target.
    */
   public void setViewOffsetY(float offset) {
      if (offset != this.viewOffsetY) {
         this.viewOffsetY = offset;
         if (this.mainWidth > 0) {
            this.enterOrtho(this.mainWidth, this.mainHeight);
         }
      }
   }

   public float getViewOffsetY() {
      return this.viewOffsetY;
   }

   /** Model-view transform of a point's y, i.e. where it lands on the logical screen (before the view offset). */
   public float screenY(float x, float y) {
      float[] m = this.modelView.val;
      return m[1] * x + m[5] * y + m[13];
   }

   @Override
   public void glEnable(int cap) {
      this.setCap(cap, true);
   }

   @Override
   public void glDisable(int cap) {
      this.setCap(cap, false);
   }

   private void setCap(int cap, boolean on) {
      switch (cap) {
         case GL_BLEND:
            if (this.blend != on) {
               this.flush();
               this.blend = on;
            }
            break;
         case GL_TEXTURE_2D:
            // No flush: untextured vertices are marked per vertex (see UNTEXTURED_U).
            this.texture2D = on;
            break;
         case GL_SCISSOR_TEST:
            if (this.scissor != on) {
               this.flush();
               this.scissor = on;
            }
            break;
         case GL_COLOR_SUM:
            this.colorSum = on;
            break;
         default:
            if (cap >= GL_CLIP_PLANE0 && cap < GL_CLIP_PLANE0 + 4 && this.planeOn[cap - GL_CLIP_PLANE0] != on) {
               this.flush();
               this.planeOn[cap - GL_CLIP_PLANE0] = on;
            }
      }
   }

   @Override
   public void glBlendFunc(int src, int dst) {
      if (src != this.srcBlend || dst != this.dstBlend) {
         this.flush();
         this.srcBlend = src;
         this.dstBlend = dst;
      }
   }

   @Override
   public void glColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
      if (red != this.colorMask[0] || green != this.colorMask[1] || blue != this.colorMask[2] || alpha != this.colorMask[3]) {
         this.flush();
         this.colorMask[0] = red;
         this.colorMask[1] = green;
         this.colorMask[2] = blue;
         this.colorMask[3] = alpha;
      }
   }

   @Override
   public void glScissor(int x, int y, int width, int height) {
      if (x != this.scissorBox[0] || y != this.scissorBox[1] || width != this.scissorBox[2] || height != this.scissorBox[3]) {
         this.flush();
         this.scissorBox[0] = x;
         this.scissorBox[1] = y;
         this.scissorBox[2] = width;
         this.scissorBox[3] = height;
      }
   }

   @Override
   public void glClipPlane(int plane, DoubleBuffer equation) {
      int i = plane - GL_CLIP_PLANE0;
      if (i >= 0 && i < 4) {
         this.tmp.set(this.modelView).inv();
         float[] m = this.tmp.val;
         float pa = (float)equation.get(0);
         float pb = (float)equation.get(1);
         float pc = (float)equation.get(2);
         float pd = (float)equation.get(3);
         float p0 = pa * m[0] + pb * m[1] + pc * m[2] + pd * m[3];
         float p1 = pa * m[4] + pb * m[5] + pc * m[6] + pd * m[7];
         float p2 = pa * m[8] + pb * m[9] + pc * m[10] + pd * m[11];
         float p3 = pa * m[12] + pb * m[13] + pc * m[14] + pd * m[15];
         float[] cur = this.planes[i];
         if (cur[0] != p0 || cur[1] != p1 || cur[2] != p2 || cur[3] != p3) {
            // Only a changed plane ends the batch (the game re-sets the same world clip for every widget).
            this.flush();
            cur[0] = p0;
            cur[1] = p1;
            cur[2] = p2;
            cur[3] = p3;
         }
      }
   }

   @Override
   public void glLineWidth(float width) {
      if (width != this.lineWidth) {
         if (this.batchType == BATCH_LINES) {
            this.flush();
         }

         this.lineWidth = width;
      }
   }

   @Override
   public void glPointSize(float size) {
      if (size != this.pointSize) {
         if (this.batchType == BATCH_POINTS) {
            this.flush();
         }

         this.pointSize = size;
      }
   }

   @Override
   public void glClearColor(float red, float green, float blue, float alpha) {
      this.clearColor[0] = red;
      this.clearColor[1] = green;
      this.clearColor[2] = blue;
      this.clearColor[3] = alpha;
   }

   @Override
   public void glClear(int mask) {
      this.flush();
      this.glStateValid = false;
      Gdx.gl.glColorMask(this.colorMask[0], this.colorMask[1], this.colorMask[2], this.colorMask[3]);
      this.applyScissor(true);
      Gdx.gl.glClearColor(this.clearColor[0], this.clearColor[1], this.clearColor[2], this.clearColor[3]);
      Gdx.gl.glClear(mask & GL20.GL_COLOR_BUFFER_BIT);
   }

   @Override
   public void glTexEnvi(int target, int mode, int value) {
   }

   @Override
   public void glDepthMask(boolean mask) {
   }

   @Override
   public void glClearDepth(float value) {
   }

   @Override
   public void glDepthFunc(int func) {
   }

   @Override
   public void glGetError() {
   }

   @Override
   public void glGetInteger(int id, IntBuffer ret) {
      if (id == GL_MAX_TEXTURE_SIZE) {
         Gdx.gl.glGetIntegerv(GL20.GL_MAX_TEXTURE_SIZE, ret);
      } else {
         Gdx.gl.glGetIntegerv(id, ret);
      }
   }

   @Override
   public void glGetFloat(int id, FloatBuffer ret) {
      switch (id) {
         case GL_MODELVIEW_MATRIX:
            for (int i = 0; i < 16; i++) {
               ret.put(i, this.modelView.val[i]);
            }
            break;
         case GL_PROJECTION_MATRIX:
            for (int i = 0; i < 16; i++) {
               ret.put(i, this.projection.val[i]);
            }
            break;
         case GL_COLOR_CLEAR_VALUE:
            for (int i = 0; i < 4; i++) {
               ret.put(i, this.clearColor[i]);
            }
            break;
         default:
            Gdx.gl.glGetFloatv(id, ret);
      }
   }

   // ---------------------------------------------------------------- display lists (unused by Rise to Ruins)

   @Override
   public int glGenLists(int range) {
      throw new UnsupportedOperationException("Display lists are not supported on GLES");
   }

   @Override
   public void glNewList(int id, int mode) {
      throw new UnsupportedOperationException("Display lists are not supported on GLES");
   }

   @Override
   public void glEndList() {
   }

   @Override
   public void glCallList(int id) {
   }

   @Override
   public void glDeleteLists(int list, int count) {
   }

   // ---------------------------------------------------------------- textures

   @Override
   public void glBindTexture(int target, int id) {
      if (id != this.boundTexture) {
         if (this.pendingTextured) {
            if (TEX_STATS) {
               int[] c = this.switchCounts.get(this.boundTexture);
               if (c == null) {
                  this.switchCounts.put(this.boundTexture, c = new int[1]);
               }

               c[0]++;
            }

            this.flush();
            rtr.compat.PerfStats.textureSwitches++;
         }

         this.boundTexture = id;
      }

      Gdx.gl.glBindTexture(GL20.GL_TEXTURE_2D, id);
   }

   public int getBoundTexture() {
      return this.boundTexture;
   }

   @Override
   public void glGenTextures(IntBuffer ids) {
      Gdx.gl.glGenTextures(ids.remaining(), ids);
   }

   @Override
   public void glDeleteTextures(IntBuffer ids) {
      this.flush();

      for (int i = ids.position(); i < ids.limit(); i++) {
         int id = ids.get(i);
         this.textureSizes.remove(id);
         if (id == this.boundTexture) {
            this.boundTexture = 0;
         }
      }

      Gdx.gl.glDeleteTextures(ids.remaining(), ids);
   }

   @Override
   public void glTexParameteri(int target, int param, int value) {
      this.flush();
      if (value == GL_CLAMP || value == GL_MIRROR_CLAMP) {
         value = GL_CLAMP_TO_EDGE;
      }

      if (param == GL20.GL_TEXTURE_MIN_FILTER
         || param == GL20.GL_TEXTURE_MAG_FILTER
         || param == GL20.GL_TEXTURE_WRAP_S
         || param == GL20.GL_TEXTURE_WRAP_T) {
         Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, param, value);
      }
   }

   private static int baseFormat(int format) {
      switch (format) {
         case GL_RGBA8:
         case GL_BGRA:
         case 4:
            return GL_RGBA;
         case GL_RGB8:
         case 3:
            return GL_RGB;
         case 32828:
            return GL_ALPHA;
         case 1:
            return GL_LUMINANCE;
         case 2:
            return GL_LUMINANCE_ALPHA;
         default:
            return format;
      }
   }

   @Override
   public void glTexImage2D(int target, int level, int internalFormat, int width, int height, int border, int format, int type, ByteBuffer pixels) {
      this.flush();
      int f = baseFormat(format);
      Gdx.gl.glTexImage2D(GL20.GL_TEXTURE_2D, level, f, width, height, 0, f, type, pixels);
      if (level == 0) {
         this.textureSizes.put(this.boundTexture, new int[]{width, height, f});
      }
   }

   @Override
   public void glTexSubImage2D(int target, int level, int x, int y, int width, int height, int format, int type, ByteBuffer pixels) {
      this.flush();
      Gdx.gl.glTexSubImage2D(GL20.GL_TEXTURE_2D, level, x, y, width, height, baseFormat(format), type, pixels);
   }

   @Override
   public void glCopyTexImage2D(int target, int level, int internalFormat, int x, int y, int width, int height, int border) {
      this.flush();
      float s = this.targetScale;
      int f = baseFormat(internalFormat);
      Gdx.gl.glCopyTexImage2D(GL20.GL_TEXTURE_2D, level, f, Math.round(x * s), Math.round(y * s), width, height, 0);
      this.textureSizes.put(this.boundTexture, new int[]{width, height, f});
   }

   /** GLES has no glGetTexImage: attach the bound texture to a scratch FBO and read it back (RGBA rows, bottom-up). */
   @Override
   public void glGetTexImage(int target, int level, int format, int type, ByteBuffer pixels) {
      this.flush();
      int[] size = this.textureSizes.get(this.boundTexture);
      if (size == null) {
         throw new IllegalStateException("Unknown size for texture " + this.boundTexture);
      }

      IntBuffer prev = BufferUtils.newIntBuffer(16);
      Gdx.gl.glGetIntegerv(GL20.GL_FRAMEBUFFER_BINDING, prev);
      if (this.readbackFbo == 0) {
         this.readbackFbo = Gdx.gl.glGenFramebuffer();
      }

      Gdx.gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, this.readbackFbo);
      Gdx.gl.glFramebufferTexture2D(GL20.GL_FRAMEBUFFER, GL20.GL_COLOR_ATTACHMENT0, GL20.GL_TEXTURE_2D, this.boundTexture, 0);
      Gdx.gl.glPixelStorei(GL20.GL_PACK_ALIGNMENT, 1);
      int f = baseFormat(format);
      if (f == GL_RGBA || f == GL_RGB && size[2] == GL_RGB) {
         Gdx.gl.glReadPixels(0, 0, size[0], size[1], f == GL_RGB ? GL_RGB : GL_RGBA, GL20.GL_UNSIGNED_BYTE, pixels);
      } else {
         ByteBuffer rgba = BufferUtils.newByteBuffer(size[0] * size[1] * 4);
         Gdx.gl.glReadPixels(0, 0, size[0], size[1], GL_RGBA, GL20.GL_UNSIGNED_BYTE, rgba);

         for (int i = 0; i < size[0] * size[1]; i++) {
            pixels.put(rgba.get(i * 4)).put(rgba.get(i * 4 + 1)).put(rgba.get(i * 4 + 2));
         }

         pixels.flip();
      }

      Gdx.gl.glFramebufferTexture2D(GL20.GL_FRAMEBUFFER, GL20.GL_COLOR_ATTACHMENT0, GL20.GL_TEXTURE_2D, 0, 0);
      Gdx.gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, prev.get(0));
   }

   @Override
   public void glReadPixels(int x, int y, int width, int height, int format, int type, ByteBuffer pixels) {
      this.flush();
      float s = this.targetScale;
      Gdx.gl.glReadPixels(Math.round(x * s), Math.round(y * s), width, height, baseFormat(format), type, pixels);
   }
}
