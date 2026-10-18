package org.newdawn.slick.opengl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;
import org.newdawn.slick.util.ResourceLoader;
import rtr.compat.lwjgl.BufferUtils;

/** Slick's texture cache/loader with decoding moved to {@link PixmapImageData}. Behaviour otherwise follows Slick. */
public class InternalTextureLoader {
   protected static SGL GL = Renderer.get();
   private static final InternalTextureLoader loader = new InternalTextureLoader();
   private final HashMap<String, Object> texturesLinear = new HashMap<String, Object>();
   private final HashMap<String, Object> texturesNearest = new HashMap<String, Object>();
   private int dstPixelFormat = 6408;
   private boolean holdTextureData;
   /** Upload decoded images at their exact size instead of padding to powers of two. */
   public static boolean npotTextures = true;

   public static InternalTextureLoader get() {
      return loader;
   }

   public void setHoldTextureData(boolean holdTextureData) {
      this.holdTextureData = holdTextureData;
   }

   public void setDeferredLoading(boolean deferred) {
   }

   public boolean isDeferredLoading() {
      return false;
   }

   public void clear(String name) {
      this.texturesLinear.remove(name);
      this.texturesNearest.remove(name);
   }

   public void clear() {
      this.texturesLinear.clear();
      this.texturesNearest.clear();
   }

   public void set16BitMode() {
   }

   public static int createTextureID() {
      IntBuffer tmp = BufferUtils.createIntBuffer(1);
      GL.glGenTextures(tmp);
      return tmp.get(0);
   }

   public Texture getTexture(File source, boolean flipped, int filter) throws IOException {
      return this.getTexture(source, flipped, filter, null);
   }

   public Texture getTexture(File source, boolean flipped, int filter, int[] transparent) throws IOException {
      return this.getTexture(new FileInputStream(source), source.getAbsolutePath(), flipped, filter, transparent);
   }

   public Texture getTexture(String resourceName, boolean flipped, int filter) throws IOException {
      return this.getTexture(resourceName, flipped, filter, null);
   }

   public Texture getTexture(String resourceName, boolean flipped, int filter, int[] transparent) throws IOException {
      InputStream in = ResourceLoader.getResourceAsStream(resourceName);
      try {
         return this.getTexture(in, resourceName, flipped, filter, transparent);
      } finally {
         in.close();
      }
   }

   public Texture getTexture(InputStream in, String resourceName, boolean flipped, int filter) throws IOException {
      return this.getTexture(in, resourceName, flipped, filter, null);
   }

   public TextureImpl getTexture(InputStream in, String resourceName, boolean flipped, int filter, int[] transparent) throws IOException {
      HashMap<String, Object> hash = filter == 9728 ? this.texturesNearest : this.texturesLinear;
      String resName = resourceName;
      if (transparent != null) {
         resName = resourceName + ":" + transparent[0] + ":" + transparent[1] + ":" + transparent[2];
      }

      resName = resName + ":" + flipped;
      AtlasTexture packed = TextureAtlas.get(resName);
      if (packed != null) {
         return packed;
      }

      Object cached = hash.get(resName);
      if (cached instanceof TextureImpl) {
         return (TextureImpl)cached;
      }

      if (cached instanceof SoftReference) {
         TextureImpl tex = (TextureImpl)((SoftReference<?>)cached).get();
         if (tex != null) {
            return tex;
         }

         hash.remove(resName);
      }

      PixmapImageData imageData = new PixmapImageData();
      ByteBuffer textureBuffer = imageData.loadImage(new BufferedInputStream(in), flipped, transparent);
      if (imageData.getTexWidth() == imageData.getWidth()
         && imageData.getTexHeight() == imageData.getHeight()
         && TextureAtlas.eligible(resourceName, imageData.getWidth(), imageData.getHeight(), imageData.getDepth() == 32, transparent)) {
         AtlasTexture region = TextureAtlas.pack("" + resourceName, resName, imageData.getWidth(), imageData.getHeight(), textureBuffer);
         if (region != null) {
            TextureImpl.cachePixels(region, imageData.getBytes());
            return region;
         }
      }

      TextureImpl tex = this.upload("" + resourceName, imageData, textureBuffer, filter, filter);
      tex.setCacheName(resName);
      TextureImpl.cachePixels(tex, imageData.getBytes());
      hash.put(resName, this.holdTextureData ? tex : new SoftReference<TextureImpl>(tex));
      return tex;
   }

   public Texture createTexture(int width, int height) throws IOException {
      return this.createTexture(width, height, 9728);
   }

   public Texture createTexture(int width, int height, int filter) throws IOException {
      return this.getTexture(new EmptyImageData(width, height), filter);
   }

   public Texture getTexture(ImageData dataSource, int filter) throws IOException {
      return this.upload("generated:" + dataSource, dataSource, dataSource.getImageBufferData(), filter, filter);
   }

   private TextureImpl upload(String ref, ImageData data, ByteBuffer pixels, int minFilter, int magFilter) throws IOException {
      int target = 3553;
      int textureID = createTextureID();
      TextureImpl texture = new TextureImpl(ref, target, textureID);
      GL.glBindTexture(target, textureID);
      int width = data.getWidth();
      int height = data.getHeight();
      boolean hasAlpha = data.getDepth() == 32;
      texture.setTextureWidth(data.getTexWidth());
      texture.setTextureHeight(data.getTexHeight());
      IntBuffer temp = BufferUtils.createIntBuffer(16);
      GL.glGetInteger(3379, temp);
      int max = temp.get(0);
      if (texture.getTextureWidth() > max || texture.getTextureHeight() > max) {
         throw new IOException("Attempt to allocate a texture too big for the current hardware: " + ref);
      }

      int srcPixelFormat = hasAlpha ? 6408 : 6407;
      int componentCount = hasAlpha ? 4 : 3;
      texture.setWidth(width);
      texture.setHeight(height);
      texture.setAlpha(hasAlpha);
      if (this.holdTextureData) {
         texture.setTextureData(srcPixelFormat, componentCount, minFilter, magFilter, pixels);
      }

      GL.glTexParameteri(target, 10241, minFilter);
      GL.glTexParameteri(target, 10240, magFilter);
      GL.glTexParameteri(target, 10242, 33071);
      GL.glTexParameteri(target, 10243, 33071);
      GL.glTexImage2D(target, 0, this.dstPixelFormat, data.getTexWidth(), data.getTexHeight(), 0, srcPixelFormat, 5121, pixels);
      return texture;
   }

   public static int get2Fold(int fold) {
      int ret = 2;

      while (ret < fold) {
         ret *= 2;
      }

      return ret;
   }

   public static IntBuffer createIntBuffer(int size) {
      return BufferUtils.createIntBuffer(size);
   }

   public void reload() {
   }

   public int reload(TextureImpl texture, int srcPixelFormat, int componentCount, int minFilter, int magFilter, ByteBuffer textureBuffer) {
      int target = 3553;
      int textureID = createTextureID();
      GL.glBindTexture(target, textureID);
      GL.glTexParameteri(target, 10241, minFilter);
      GL.glTexParameteri(target, 10240, magFilter);
      GL.glTexImage2D(
         target, 0, this.dstPixelFormat, texture.getTextureWidth(), texture.getTextureHeight(), 0, srcPixelFormat, 5121, textureBuffer
      );
      return textureID;
   }
}
