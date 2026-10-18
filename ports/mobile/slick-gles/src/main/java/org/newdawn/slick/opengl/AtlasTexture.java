package org.newdawn.slick.opengl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import rtr.compat.lwjgl.BufferUtils;

/**
 * One image packed into a {@link TextureAtlas} page. getWidth()/getHeight() are its size relative to the page and
 * {@link #getAtlasU()}/{@link #getAtlasV()} its origin, so Image's texture-space maths works unchanged once its
 * offsets start at the origin. Pixel data (for Image.getColor) is the image's own, not the page's.
 */
public final class AtlasTexture extends TextureImpl {
   private final int regionX;
   private final int regionY;
   private final float u;
   private final float v;

   AtlasTexture(String ref, int pageId, int x, int y, int width, int height, int pageSize) {
      super(ref, 3553, pageId);
      // The base constructor marks itself as bound; this region was never bound.
      TextureImpl.unbind();
      this.regionX = x;
      this.regionY = y;
      this.u = (float)x / pageSize;
      this.v = (float)y / pageSize;
      this.setTextureWidth(pageSize);
      this.setTextureHeight(pageSize);
      this.setWidth(width);
      this.setHeight(height);
      this.setAlpha(true);
   }

   public float getAtlasU() {
      return this.u;
   }

   public float getAtlasV() {
      return this.v;
   }

   public int getRegionX() {
      return this.regionX;
   }

   public int getRegionY() {
      return this.regionY;
   }

   /** The page is shared: only forget this image. */
   @Override
   public void release() {
      uncache(this);
      TextureAtlas.release(this);
      if (this.getCacheName() != null) {
         InternalTextureLoader.get().clear(this.getCacheName());
      }
   }

   /** Pages are always nearest-filtered; changing the filter would affect every image on the page. */
   @Override
   public void setTextureFilter(int textureFilter) {
   }

   /** RGBA rows of this image only (row length = image width). */
   @Override
   public byte[] getTextureData() {
      byte[] cached = cached(this);
      if (cached != null) {
         return cached;
      }

      // Evicted from the pixel cache: read the page back and cut this image out of it.
      int ps = this.getTextureWidth();
      ByteBuffer buffer = BufferUtils.createByteBuffer(4 * ps * ps);
      TextureImpl.unbind();
      this.bind();
      GL.glGetTexImage(3553, 0, 6408, 5121, buffer);
      int w = this.getImageWidth();
      int h = this.getImageHeight();
      byte[] data = new byte[w * h * 4];

      for (int row = 0; row < h; row++) {
         ((Buffer)buffer).position(((this.regionY + row) * ps + this.regionX) * 4);
         buffer.get(data, row * w * 4, w * 4);
      }

      cachePixels(this, data);
      return data;
   }
}
