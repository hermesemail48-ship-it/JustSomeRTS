package org.newdawn.slick;

import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.newdawn.slick.opengl.EmptyImageData;
import org.newdawn.slick.opengl.ImageData;
import org.newdawn.slick.opengl.InternalTextureLoader;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.pbuffer.GraphicsFactory;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;
import org.newdawn.slick.util.FastTrig;
import org.newdawn.slick.util.Log;

public class Image implements Renderable {
   public static final int TOP_LEFT = 0;
   public static final int TOP_RIGHT = 1;
   public static final int BOTTOM_RIGHT = 2;
   public static final int BOTTOM_LEFT = 3;
   protected static SGL GL = Renderer.get();
   protected static Texture inUse;
   public static final int FILTER_LINEAR = 9729;
   public static final int FILTER_NEAREST = 9728;
   protected Texture texture;
   protected int width;
   protected int height;
   protected float textureWidth;
   protected float textureHeight;
   protected float textureOffsetX;
   protected float textureOffsetY;
   protected float angle;
   protected float alpha = 1.0F;
   protected String ref;
   protected boolean inited = false;
   protected byte[] pixelData;
   protected boolean destroyed;
   protected float centerX;
   protected float centerY;
   protected String name;
   protected Color[] corners;
   private int filter = 9729;
   private boolean flipped;
   private Color transparent;

   public static final int getMaxSingleImageSize() {
      IntBuffer buffer = BufferUtils.createIntBuffer(16);
      GL.glGetInteger(3379, buffer);
      return buffer.get(0);
   }

   public static Image createOffscreenImage(int width, int height, int filter) throws SlickException {
      Image i = new Image();
      i.width = width;
      i.height = height;
      i.filter = filter;
      i.inited = true;
      i.getGraphics();
      return i;
   }

   public static Image createOffscreenImage(int width, int height) throws SlickException {
      return createOffscreenImage(width, height, 9729);
   }

   protected Image(Image other) {
      this.width = other.getWidth();
      this.height = other.getHeight();
      this.texture = other.texture;
      this.textureWidth = other.textureWidth;
      this.textureHeight = other.textureHeight;
      this.ref = other.ref;
      this.textureOffsetX = other.textureOffsetX;
      this.textureOffsetY = other.textureOffsetY;
      this.centerX = this.width / 2.0F;
      this.centerY = this.height / 2.0F;
      this.inited = true;
   }

   protected Image() {
   }

   public Image(Texture texture) {
      this.texture = texture;
      this.ref = texture.toString();
      this.clampTexture();
   }

   public Image(String ref) throws SlickException {
      this(ref, false);
   }

   public Image(String ref, Color trans) throws SlickException {
      this(ref, false, 9729, trans);
   }

   public Image(String ref, boolean flipped) throws SlickException {
      this(ref, flipped, 9729);
   }

   public Image(String ref, boolean flipped, int filter) throws SlickException {
      this(ref, flipped, filter, null);
   }

   public Image(String ref, boolean flipped, int f, Color transparent) throws SlickException {
      this.filter = f;
      this.transparent = transparent;
      this.flipped = flipped;

      try {
         this.ref = ref;
         int[] trans = null;
         if (transparent != null) {
            trans = new int[]{(int)(transparent.r * 255.0F), (int)(transparent.g * 255.0F), (int)(transparent.b * 255.0F)};
         }

         this.texture = InternalTextureLoader.get().getTexture(ref, flipped, this.filter, trans);
      } catch (IOException var6) {
         Log.error(var6);
         throw new SlickException("Failed to load image from: " + ref, var6);
      }
   }

   public void setFilter(int f) {
      this.filter = f;
      this.texture.bind();
      GL.glTexParameteri(3553, 10241, this.filter);
      GL.glTexParameteri(3553, 10240, this.filter);
   }

   public Image(int width, int height) throws SlickException {
      this(width, height, 9728);
   }

   public Image(int width, int height, int f) throws SlickException {
      this(new EmptyImageData(width, height), f);
   }

   public Image(InputStream in, String ref, boolean flipped) throws SlickException {
      this(in, ref, flipped, 9729);
   }

   public Image(InputStream in, String ref, boolean flipped, int filter) throws SlickException {
      this.load(in, ref, flipped, filter, null);
   }

   Image(ImageBuffer buffer) {
      this(buffer, 9729);
      TextureImpl.bindNone();
   }

   Image(ImageBuffer buffer, int filter) {
      this((ImageData)buffer, filter);
      TextureImpl.bindNone();
   }

   public Image(ImageData data) {
      this(data, 9729);
   }

   public Image(ImageData data, int f) {
      try {
         this.filter = f;
         this.texture = InternalTextureLoader.get().getTexture(data, this.filter);
         this.ref = this.texture.toString();
      } catch (IOException var4) {
         Log.error(var4);
      }
   }

   public int getFilter() {
      return this.filter;
   }

   public String getResourceReference() {
      return this.ref;
   }

   public void setImageColor(float r, float g, float b, float a) {
      this.setColor(0, r, g, b, a);
      this.setColor(1, r, g, b, a);
      this.setColor(3, r, g, b, a);
      this.setColor(2, r, g, b, a);
   }

   public void setImageColor(float r, float g, float b) {
      this.setColor(0, r, g, b);
      this.setColor(1, r, g, b);
      this.setColor(3, r, g, b);
      this.setColor(2, r, g, b);
   }

   public void setColor(int corner, float r, float g, float b, float a) {
      if (this.corners == null) {
         this.corners = new Color[]{
            new Color(1.0F, 1.0F, 1.0F, 1.0F), new Color(1.0F, 1.0F, 1.0F, 1.0F), new Color(1.0F, 1.0F, 1.0F, 1.0F), new Color(1.0F, 1.0F, 1.0F, 1.0F)
         };
      }

      this.corners[corner].r = r;
      this.corners[corner].g = g;
      this.corners[corner].b = b;
      this.corners[corner].a = a;
   }

   public void setColor(int corner, float r, float g, float b) {
      if (this.corners == null) {
         this.corners = new Color[]{
            new Color(1.0F, 1.0F, 1.0F, 1.0F), new Color(1.0F, 1.0F, 1.0F, 1.0F), new Color(1.0F, 1.0F, 1.0F, 1.0F), new Color(1.0F, 1.0F, 1.0F, 1.0F)
         };
      }

      this.corners[corner].r = r;
      this.corners[corner].g = g;
      this.corners[corner].b = b;
   }

   public void clampTexture() {
      if (GL.canTextureMirrorClamp()) {
         GL.glTexParameteri(3553, 10242, 34627);
         GL.glTexParameteri(3553, 10243, 34627);
      } else {
         GL.glTexParameteri(3553, 10242, 10496);
         GL.glTexParameteri(3553, 10243, 10496);
      }
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public Graphics getGraphics() throws SlickException {
      return GraphicsFactory.getGraphicsForImage(this);
   }

   private void load(InputStream in, String ref, boolean flipped, int f, Color transparent) throws SlickException {
      this.filter = f;

      try {
         this.ref = ref;
         int[] trans = null;
         if (transparent != null) {
            trans = new int[]{(int)(transparent.r * 255.0F), (int)(transparent.g * 255.0F), (int)(transparent.b * 255.0F)};
         }

         this.texture = InternalTextureLoader.get().getTexture(in, ref, flipped, this.filter, trans);
      } catch (IOException var7) {
         Log.error(var7);
         throw new SlickException("Failed to load image from: " + ref, var7);
      }
   }

   public void bind() {
      this.texture.bind();
   }

   protected void reinit() {
      this.inited = false;
      this.init();
   }

   protected final void init() {
      if (!this.inited) {
         this.inited = true;
         if (this.texture != null) {
            this.width = this.texture.getImageWidth();
            this.height = this.texture.getImageHeight();
            this.textureOffsetX = 0.0F;
            this.textureOffsetY = 0.0F;
            this.textureWidth = this.texture.getWidth();
            this.textureHeight = this.texture.getHeight();
         }

         this.initImpl();
         this.centerX = this.width / 2.0F;
         this.centerY = this.height / 2.0F;
      }
   }

   protected void initImpl() {
   }

   public void draw() {
      this.draw(0.0F, 0.0F);
   }

   public void drawCentered(float x, float y) {
      this.draw(x - this.getWidth() / 2.0F, y - this.getHeight() / 2.0F);
   }

   public void drawCentered(float x, float y, Color c) {
      this.draw(x - this.getWidth() / 2.0F, y - this.getHeight() / 2.0F, c);
   }

   @Override
   public void draw(float x, float y) {
      this.init();
      this.draw(x, y, this.width, this.height);
   }

   public void draw(float x, float y, Color filter) {
      this.init();
      this.draw(x, y, this.width, this.height, filter);
   }

   public void drawEmbedded(float x, float y, float width, float height, float rotation) {
      this.drawEmbedded(x, y, width, height, rotation, null);
   }

   public void drawEmbedded(float x, float y, float width, float height, float rotation, Color filter) {
      if (rotation == 0.0F) {
         if (filter != null) {
            filter.bind();
         }

         this.drawEmbedded(x, y, width, height);
      } else {
         this.init();
         if (filter != null) {
            filter.bind();
         }

         float scaleX = width / this.width;
         float scaleY = height / this.height;
         float cx = this.getCenterOfRotationX() * scaleX;
         float cy = this.getCenterOfRotationY() * scaleY;
         float p1x = -cx;
         float p1y = -cy;
         float p2x = width - cx;
         float p2y = -cy;
         float p3x = width - cx;
         float p3y = height - cy;
         float p4x = -cx;
         float p4y = height - cy;
         double rad = Math.toRadians(rotation);
         float cos = (float)FastTrig.cos(rad);
         float sin = (float)FastTrig.sin(rad);
         float tx = this.getTextureOffsetX();
         float ty = this.getTextureOffsetY();
         float tw = this.getTextureWidth();
         float th = this.getTextureHeight();
         float x1 = cos * p1x - sin * p1y + cx;
         float y1 = sin * p1x + cos * p1y + cy;
         float x2 = cos * p4x - sin * p4y + cx;
         float y2 = sin * p4x + cos * p4y + cy;
         float x3 = cos * p3x - sin * p3y + cx;
         float y3 = sin * p3x + cos * p3y + cy;
         float x4 = cos * p2x - sin * p2y + cx;
         float y4 = sin * p2x + cos * p2y + cy;
         if (this.corners == null) {
            GL.glTexCoord2f(tx, ty);
            GL.glVertex3f(x + x1, y + y1, 0.0F);
            GL.glTexCoord2f(tx, ty + th);
            GL.glVertex3f(x + x2, y + y2, 0.0F);
            GL.glTexCoord2f(tx + tw, ty + th);
            GL.glVertex3f(x + x3, y + y3, 0.0F);
            GL.glTexCoord2f(tx + tw, ty);
            GL.glVertex3f(x + x4, y + y4, 0.0F);
         } else {
            this.corners[0].bind();
            GL.glTexCoord2f(tx, ty);
            GL.glVertex3f(x + x1, y + y1, 0.0F);
            this.corners[3].bind();
            GL.glTexCoord2f(tx, ty + th);
            GL.glVertex3f(x + x2, y + y2, 0.0F);
            this.corners[2].bind();
            GL.glTexCoord2f(tx + tw, ty + th);
            GL.glVertex3f(x + x3, y + y3, 0.0F);
            this.corners[1].bind();
            GL.glTexCoord2f(tx + tw, ty);
            GL.glVertex3f(x + x4, y + y4, 0.0F);
         }
      }
   }

   public void drawEmbedded(float x, float y) {
      this.drawEmbedded(x, y, this.getWidth(), this.getHeight());
   }

   public void drawEmbedded(float x, float y, float width, float height) {
      this.init();
      if (this.corners == null) {
         GL.glTexCoord2f(this.textureOffsetX, this.textureOffsetY);
         GL.glVertex3f(x, y, 0.0F);
         GL.glTexCoord2f(this.textureOffsetX, this.textureOffsetY + this.textureHeight);
         GL.glVertex3f(x, y + height, 0.0F);
         GL.glTexCoord2f(this.textureOffsetX + this.textureWidth, this.textureOffsetY + this.textureHeight);
         GL.glVertex3f(x + width, y + height, 0.0F);
         GL.glTexCoord2f(this.textureOffsetX + this.textureWidth, this.textureOffsetY);
         GL.glVertex3f(x + width, y, 0.0F);
      } else {
         this.corners[0].bind();
         GL.glTexCoord2f(this.textureOffsetX, this.textureOffsetY);
         GL.glVertex3f(x, y, 0.0F);
         this.corners[3].bind();
         GL.glTexCoord2f(this.textureOffsetX, this.textureOffsetY + this.textureHeight);
         GL.glVertex3f(x, y + height, 0.0F);
         this.corners[2].bind();
         GL.glTexCoord2f(this.textureOffsetX + this.textureWidth, this.textureOffsetY + this.textureHeight);
         GL.glVertex3f(x + width, y + height, 0.0F);
         this.corners[1].bind();
         GL.glTexCoord2f(this.textureOffsetX + this.textureWidth, this.textureOffsetY);
         GL.glVertex3f(x + width, y, 0.0F);
      }
   }

   public void drawEmbedded(float x, float y, float x2, float y2, float srcx, float srcy, float srcx2, float srcy2) {
      this.drawEmbedded(x, y, x2, y2, srcx, srcy, srcx2, srcy2, null);
   }

   public void drawEmbedded(float x, float y, float x2, float y2, float srcx, float srcy, float srcx2, float srcy2, Color filter) {
      this.init();
      if (filter != null) {
         filter.bind();
      }

      float mywidth = x2 - x;
      float myheight = y2 - y;
      float texwidth = srcx2 - srcx;
      float texheight = srcy2 - srcy;
      float newTextureOffsetX = srcx / this.width * this.textureWidth + this.textureOffsetX;
      float newTextureOffsetY = srcy / this.height * this.textureHeight + this.textureOffsetY;
      float newTextureWidth = texwidth / this.width * this.textureWidth;
      float newTextureHeight = texheight / this.height * this.textureHeight;
      GL.glTexCoord2f(newTextureOffsetX, newTextureOffsetY);
      GL.glVertex3f(x, y, 0.0F);
      GL.glTexCoord2f(newTextureOffsetX, newTextureOffsetY + newTextureHeight);
      GL.glVertex3f(x, y + myheight, 0.0F);
      GL.glTexCoord2f(newTextureOffsetX + newTextureWidth, newTextureOffsetY + newTextureHeight);
      GL.glVertex3f(x + mywidth, y + myheight, 0.0F);
      GL.glTexCoord2f(newTextureOffsetX + newTextureWidth, newTextureOffsetY);
      GL.glVertex3f(x + mywidth, y, 0.0F);
   }

   public float getTextureOffsetX() {
      this.init();
      return this.textureOffsetX;
   }

   public float getTextureOffsetY() {
      this.init();
      return this.textureOffsetY;
   }

   public float getTextureWidth() {
      this.init();
      return this.textureWidth;
   }

   public float getTextureHeight() {
      this.init();
      return this.textureHeight;
   }

   public void draw(float x, float y, float scale) {
      this.init();
      this.draw(x, y, this.width * scale, this.height * scale, Color.white);
   }

   public void draw(float x, float y, float scale, Color filter) {
      this.init();
      this.draw(x, y, this.width * scale, this.height * scale, filter);
   }

   public void draw(float x, float y, float width, float height) {
      this.init();
      this.draw(x, y, width, height, Color.white);
   }

   public void drawSheared(float x, float y, float hshear, float vshear) {
      this.drawSheared(x, y, hshear, vshear, Color.white);
   }

   public void drawSheared(float x, float y, float hshear, float vshear, Color filter) {
      this.init();
      if (this.alpha != 1.0F) {
         if (filter == null) {
            filter = Color.white;
         }

         filter = new Color(filter);
         filter.a = filter.a * this.alpha;
      }

      if (filter != null) {
         filter.bind();
      }

      this.texture.bind();
      GL.glTranslatef(x, y, 0.0F);
      if (this.angle != 0.0F) {
         GL.glTranslatef(this.centerX, this.centerY, 0.0F);
         GL.glRotatef(this.angle, 0.0F, 0.0F, 1.0F);
         GL.glTranslatef(-this.centerX, -this.centerY, 0.0F);
      }

      GL.glBegin(7);
      GL.glTexCoord2f(this.textureOffsetX, this.textureOffsetY);
      GL.glVertex3f(0.0F, 0.0F, 0.0F);
      GL.glTexCoord2f(this.textureOffsetX, this.textureOffsetY + this.textureHeight);
      GL.glVertex3f(hshear, this.height, 0.0F);
      GL.glTexCoord2f(this.textureOffsetX + this.textureWidth, this.textureOffsetY + this.textureHeight);
      GL.glVertex3f(this.width + hshear, this.height + vshear, 0.0F);
      GL.glTexCoord2f(this.textureOffsetX + this.textureWidth, this.textureOffsetY);
      GL.glVertex3f(this.width, vshear, 0.0F);
      GL.glEnd();
      if (this.angle != 0.0F) {
         GL.glTranslatef(this.centerX, this.centerY, 0.0F);
         GL.glRotatef(-this.angle, 0.0F, 0.0F, 1.0F);
         GL.glTranslatef(-this.centerX, -this.centerY, 0.0F);
      }

      GL.glTranslatef(-x, -y, 0.0F);
   }

   public void draw(float x, float y, float width, float height, Color filter) {
      this.init();
      if (this.alpha != 1.0F) {
         if (filter == null) {
            filter = Color.white;
         }

         filter = new Color(filter);
         filter.a = filter.a * this.alpha;
      }

      if (filter != null) {
         filter.bind();
      }

      float centerX = this.centerX * (width / this.getWidth());
      float centerY = this.centerY * (height / this.getHeight());
      this.texture.bind();
      GL.glTranslatef(x, y, 0.0F);
      if (this.angle != 0.0F) {
         GL.glTranslatef(centerX, centerY, 0.0F);
         GL.glRotatef(this.angle, 0.0F, 0.0F, 1.0F);
         GL.glTranslatef(-centerX, -centerY, 0.0F);
      }

      GL.glBegin(7);
      this.drawEmbedded(0.0F, 0.0F, width, height);
      GL.glEnd();
      if (this.angle != 0.0F) {
         GL.glTranslatef(centerX, centerY, 0.0F);
         GL.glRotatef(-this.angle, 0.0F, 0.0F, 1.0F);
         GL.glTranslatef(-centerX, -centerY, 0.0F);
      }

      GL.glTranslatef(-x, -y, 0.0F);
   }

   public void drawFlash(float x, float y, float width, float height) {
      this.drawFlash(x, y, width, height, Color.white);
   }

   public void setCenterOfRotation(float x, float y) {
      this.init();
      this.centerX = x;
      this.centerY = y;
   }

   public float getCenterOfRotationX() {
      this.init();
      return this.centerX;
   }

   public float getCenterOfRotationY() {
      this.init();
      return this.centerY;
   }

   public void drawFlash(float x, float y, float width, float height, Color col) {
      this.init();
      col.bind();
      this.texture.bind();
      float centerX = this.centerX * (width / this.getWidth());
      float centerY = this.centerY * (height / this.getHeight());
      if (GL.canSecondaryColor()) {
         GL.glEnable(33880);
         GL.glSecondaryColor3ubEXT((byte)(col.r * 255.0F), (byte)(col.g * 255.0F), (byte)(col.b * 255.0F));
      }

      GL.glTexEnvi(8960, 8704, 8448);
      GL.glTranslatef(x, y, 0.0F);
      if (this.angle != 0.0F) {
         GL.glTranslatef(centerX, centerY, 0.0F);
         GL.glRotatef(this.angle, 0.0F, 0.0F, 1.0F);
         GL.glTranslatef(-centerX, -centerY, 0.0F);
      }

      GL.glBegin(7);
      this.drawEmbedded(0.0F, 0.0F, width, height);
      GL.glEnd();
      if (this.angle != 0.0F) {
         GL.glTranslatef(centerX, centerY, 0.0F);
         GL.glRotatef(-this.angle, 0.0F, 0.0F, 1.0F);
         GL.glTranslatef(-centerX, -centerY, 0.0F);
      }

      GL.glTranslatef(-x, -y, 0.0F);
      if (GL.canSecondaryColor()) {
         GL.glDisable(33880);
      }
   }

   public void drawFlash(float x, float y) {
      this.drawFlash(x, y, this.getWidth(), this.getHeight());
   }

   public void setRotation(float angle) {
      this.angle = angle % 360.0F;
   }

   public float getRotation() {
      return this.angle;
   }

   public float getAlpha() {
      return this.alpha;
   }

   public void setAlpha(float alpha) {
      this.alpha = alpha;
   }

   public void rotate(float angle) {
      this.angle += angle;
      this.angle %= 360.0F;
   }

   public void draw(float x, float y, float srcx, float srcy, float srcx2, float srcy2) {
      this.draw(x, y, x + this.width, y + this.height, srcx, srcy, srcx2, srcy2);
   }

   public void draw(float x, float y, float x2, float y2, float srcx, float srcy, float srcx2, float srcy2) {
      this.draw(x, y, x2, y2, srcx, srcy, srcx2, srcy2, Color.white);
   }

   public void draw(float x, float y, float x2, float y2, float srcx, float srcy, float srcx2, float srcy2, Color filter) {
      this.init();
      if (this.alpha != 1.0F) {
         if (filter == null) {
            filter = Color.white;
         }

         filter = new Color(filter);
         filter.a = filter.a * this.alpha;
      }

      filter.bind();
      this.texture.bind();
      float centerX = this.centerX * ((x2 - x) / this.getWidth());
      float centerY = this.centerY * ((y2 - y) / this.getHeight());
      GL.glTranslatef(x, y, 0.0F);
      if (this.angle != 0.0F) {
         GL.glTranslatef(centerX, centerY, 0.0F);
         GL.glRotatef(this.angle, 0.0F, 0.0F, 1.0F);
         GL.glTranslatef(-centerX, -centerY, 0.0F);
      }

      GL.glBegin(7);
      this.drawEmbedded(0.0F, 0.0F, x2 - x, y2 - y, srcx, srcy, srcx2, srcy2);
      GL.glEnd();
      if (this.angle != 0.0F) {
         GL.glTranslatef(centerX, centerY, 0.0F);
         GL.glRotatef(-this.angle, 0.0F, 0.0F, 1.0F);
         GL.glTranslatef(-centerX, -centerY, 0.0F);
      }

      GL.glTranslatef(-x, -y, 0.0F);
   }

   public void drawWarped(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
      Color.white.bind();
      this.texture.bind();
      GL.glTranslatef(x1, y1, 0.0F);
      if (this.angle != 0.0F) {
         GL.glTranslatef(this.centerX, this.centerY, 0.0F);
         GL.glRotatef(this.angle, 0.0F, 0.0F, 1.0F);
         GL.glTranslatef(-this.centerX, -this.centerY, 0.0F);
      }

      GL.glBegin(7);
      this.init();
      GL.glTexCoord2f(this.textureOffsetX, this.textureOffsetY);
      GL.glVertex3f(0.0F, 0.0F, 0.0F);
      GL.glTexCoord2f(this.textureOffsetX, this.textureOffsetY + this.textureHeight);
      GL.glVertex3f(x2 - x1, y2 - y1, 0.0F);
      GL.glTexCoord2f(this.textureOffsetX + this.textureWidth, this.textureOffsetY + this.textureHeight);
      GL.glVertex3f(x3 - x1, y3 - y1, 0.0F);
      GL.glTexCoord2f(this.textureOffsetX + this.textureWidth, this.textureOffsetY);
      GL.glVertex3f(x4 - x1, y4 - y1, 0.0F);
      GL.glEnd();
      if (this.angle != 0.0F) {
         GL.glTranslatef(this.centerX, this.centerY, 0.0F);
         GL.glRotatef(-this.angle, 0.0F, 0.0F, 1.0F);
         GL.glTranslatef(-this.centerX, -this.centerY, 0.0F);
      }

      GL.glTranslatef(-x1, -y1, 0.0F);
   }

   public int getWidth() {
      this.init();
      return this.width;
   }

   public int getHeight() {
      this.init();
      return this.height;
   }

   public Image copy() {
      this.init();
      return this.getScaledCopy(this.width, this.height);
   }

   public Image getSubImage(int x, int y, int width, int height) {
      this.init();
      float newTextureOffsetX = (float)x / this.width * this.textureWidth + this.textureOffsetX;
      float newTextureOffsetY = (float)y / this.height * this.textureHeight + this.textureOffsetY;
      float newTextureWidth = (float)width / this.width * this.textureWidth;
      float newTextureHeight = (float)height / this.height * this.textureHeight;
      Image sub = new Image();
      sub.inited = true;
      sub.filter = this.filter;
      sub.texture = this.texture;
      sub.ref = this.ref;
      sub.textureOffsetX = newTextureOffsetX;
      sub.textureOffsetY = newTextureOffsetY;
      sub.textureWidth = newTextureWidth;
      sub.textureHeight = newTextureHeight;
      sub.width = width;
      sub.height = height;
      sub.centerX = width / 2.0F;
      sub.centerY = height / 2.0F;
      return sub;
   }

   public Image getScaledCopy(float scale) {
      this.init();
      return this.getScaledCopy((int)(this.width * scale), (int)(this.height * scale));
   }

   public Image getScaledCopy(int width, int height) {
      this.init();
      Image image = new Image();
      image.inited = true;
      image.filter = this.filter;
      image.texture = this.texture;
      image.ref = this.ref;
      image.textureOffsetX = this.textureOffsetX;
      image.textureOffsetY = this.textureOffsetY;
      image.textureWidth = this.textureWidth;
      image.textureHeight = this.textureHeight;
      image.width = width;
      image.height = height;
      image.centerX = this.centerX * ((float)width / this.width);
      image.centerY = this.centerY * ((float)height / this.height);
      return image;
   }

   public void ensureInverted() {
      this.init();
      if (this.textureHeight > 0.0F) {
         this.textureOffsetY = this.textureOffsetY + this.textureHeight;
         this.textureHeight = -this.textureHeight;
      }
   }

   public Image getFlippedCopy(boolean flipHorizontal, boolean flipVertical) {
      this.init();
      Image image = this.copy();
      if (flipHorizontal) {
         image.textureOffsetX = this.textureOffsetX + this.textureWidth;
         image.textureWidth = -this.textureWidth;
      }

      if (flipVertical) {
         image.textureOffsetY = this.textureOffsetY + this.textureHeight;
         image.textureHeight = -this.textureHeight;
      }

      return image;
   }

   public void endUse() {
      if (inUse != this.texture) {
         throw new RuntimeException("The sprite sheet is not currently in use");
      } else {
         inUse = null;
         GL.glEnd();
      }
   }

   public void startUse() {
      if (inUse != null) {
         throw new RuntimeException("Attempt to start use of a sprite sheet before ending use with another - see endUse()");
      } else {
         inUse = this.texture;
         this.init();
         Color.white.bind();
         this.texture.bind();
         GL.glBegin(7);
      }
   }

   @Override
   public String toString() {
      this.init();
      return "[Image "
         + this.ref
         + " "
         + this.width
         + "x"
         + this.height
         + "  "
         + this.textureOffsetX
         + ","
         + this.textureOffsetY
         + ","
         + this.textureWidth
         + ","
         + this.textureHeight
         + "]";
   }

   public Texture getTexture() {
      return this.texture;
   }

   public void setTexture(Texture texture) {
      if (texture != this.texture) {
         this.destroyed = false;
      }

      this.texture = texture;
      this.reinit();
   }

   private int translate(byte b) {
      return b < 0 ? 256 + b : b;
   }

   public Color getColor(int x, int y) {
      if (this.pixelData == null) {
         this.pixelData = this.texture.getTextureData();
      }

      int xo = (int)(this.textureOffsetX * this.texture.getTextureWidth());
      int yo = (int)(this.textureOffsetY * this.texture.getTextureHeight());
      if (this.textureWidth < 0.0F) {
         x = xo - x;
      } else {
         x = xo + x;
      }

      if (this.textureHeight < 0.0F) {
         y = yo - y;
      } else {
         y = yo + y;
      }

      int offset = x + y * this.texture.getTextureWidth();
      offset *= this.texture.hasAlpha() ? 4 : 3;
      return this.texture.hasAlpha()
         ? new Color(
            this.translate(this.pixelData[offset]),
            this.translate(this.pixelData[offset + 1]),
            this.translate(this.pixelData[offset + 2]),
            this.translate(this.pixelData[offset + 3])
         )
         : new Color(this.translate(this.pixelData[offset]), this.translate(this.pixelData[offset + 1]), this.translate(this.pixelData[offset + 2]));
   }

   public boolean isDestroyed() {
      return this.destroyed;
   }

   public void destroy() throws SlickException {
      if (!this.isDestroyed()) {
         this.flushPixelData();
         this.destroyed = true;
         this.texture.release();
         GraphicsFactory.releaseGraphicsForImage(this);
      }
   }

   public void flushPixelData() {
      this.pixelData = null;
   }

   public void drawEmbedded(float x, float y, float width, float height, byte transform) {
      boolean rotate = (transform & 1) > 0;
      boolean flipY = (transform & 2) > 0 ^ rotate;
      boolean flipX = (transform & 4) > 0 ^ rotate;
      if (flipX) {
         x += width;
         width *= -1.0F;
      }

      if (flipY) {
         y += height;
         height *= -1.0F;
      }

      if (!rotate) {
         this.drawEmbedded(x, y, width, height);
      } else {
         this.init();
         if (this.corners == null) {
            GL.glTexCoord2f(this.textureOffsetX + this.textureWidth, this.textureOffsetY + this.textureHeight);
            GL.glVertex3f(x, y, 0.0F);
            GL.glTexCoord2f(this.textureOffsetX, this.textureOffsetY + this.textureHeight);
            GL.glVertex3f(x, y + height, 0.0F);
            GL.glTexCoord2f(this.textureOffsetX, this.textureOffsetY);
            GL.glVertex3f(x + width, y + height, 0.0F);
            GL.glTexCoord2f(this.textureOffsetX + this.textureWidth, this.textureOffsetY);
            GL.glVertex3f(x + width, y, 0.0F);
         } else {
            this.corners[0].bind();
            GL.glTexCoord2f(this.textureOffsetX + this.textureWidth, this.textureOffsetY + this.textureHeight);
            GL.glVertex3f(x, y, 0.0F);
            this.corners[3].bind();
            GL.glTexCoord2f(this.textureOffsetX, this.textureOffsetY + this.textureHeight);
            GL.glVertex3f(x, y + height, 0.0F);
            this.corners[2].bind();
            GL.glTexCoord2f(this.textureOffsetX, this.textureOffsetY);
            GL.glVertex3f(x + width, y + height, 0.0F);
            this.corners[1].bind();
            GL.glTexCoord2f(this.textureOffsetX + this.textureWidth, this.textureOffsetY);
            GL.glVertex3f(x + width, y, 0.0F);
         }
      }
   }
}
