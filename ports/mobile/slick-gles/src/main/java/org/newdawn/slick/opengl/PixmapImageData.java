package org.newdawn.slick.opengl;

import com.badlogic.gdx.graphics.Pixmap;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import rtr.compat.lwjgl.BufferUtils;

/**
 * Decodes PNG/JPG/BMP with libGDX's native Pixmap and lays the pixels out the way Slick's own decoders did:
 * RGBA, padded to power-of-two texture dimensions, optional vertical flip, colour-key transparency and edging.
 */
public class PixmapImageData implements LoadableImageData {
   private int width;
   private int height;
   private int texWidth;
   private int texHeight;
   private boolean edging = true;
   private ByteBuffer data;

   @Override
   public void configureEdging(boolean edging) {
      this.edging = edging;
   }

   @Override
   public ByteBuffer loadImage(InputStream in) throws IOException {
      return this.loadImage(in, false, null);
   }

   @Override
   public ByteBuffer loadImage(InputStream in, boolean flipped, int[] transparent) throws IOException {
      return this.loadImage(in, flipped, false, transparent);
   }

   @Override
   public ByteBuffer loadImage(InputStream in, boolean flipped, boolean forceAlpha, int[] transparent) throws IOException {
      byte[] bytes = readAll(in);
      Pixmap pixmap;
      try {
         pixmap = new Pixmap(bytes, 0, bytes.length);
      } catch (RuntimeException e) {
         throw new IOException("Unable to decode image", e);
      }

      try {
         return this.load(pixmap, flipped, transparent);
      } finally {
         pixmap.dispose();
      }
   }

   ByteBuffer load(Pixmap source, boolean flipped, int[] transparent) {
      Pixmap pixmap = source;
      if (source.getFormat() != Pixmap.Format.RGBA8888) {
         pixmap = new Pixmap(source.getWidth(), source.getHeight(), Pixmap.Format.RGBA8888);
         pixmap.setBlending(Pixmap.Blending.None);
         pixmap.drawPixmap(source, 0, 0);
      }

      try {
         this.width = pixmap.getWidth();
         this.height = pixmap.getHeight();
         this.texWidth = InternalTextureLoader.get2Fold(this.width);
         this.texHeight = InternalTextureLoader.get2Fold(this.height);
         ByteBuffer src = pixmap.getPixels();
         byte[] out = new byte[this.texWidth * this.texHeight * 4];
         int rowBytes = this.width * 4;
         byte[] row = new byte[rowBytes];

         for (int y = 0; y < this.height; y++) {
            src.position(y * rowBytes);
            src.get(row, 0, rowBytes);
            int dy = flipped ? this.height - 1 - y : y;
            System.arraycopy(row, 0, out, dy * this.texWidth * 4, rowBytes);
         }

         src.position(0);
         if (transparent != null) {
            for (int y = 0; y < this.height; y++) {
               for (int x = 0; x < this.width; x++) {
                  int i = (y * this.texWidth + x) * 4;
                  if ((out[i] & 255) == transparent[0] && (out[i + 1] & 255) == transparent[1] && (out[i + 2] & 255) == transparent[2]) {
                     out[i + 3] = 0;
                  }
               }
            }
         }

         if (this.edging) {
            if (this.texWidth > this.width) {
               for (int y = 0; y < this.height; y++) {
                  System.arraycopy(out, (y * this.texWidth + this.width - 1) * 4, out, (y * this.texWidth + this.width) * 4, 4);
               }
            }

            if (this.texHeight > this.height) {
               System.arraycopy(out, (this.height - 1) * this.texWidth * 4, out, this.height * this.texWidth * 4, this.texWidth * 4);
            }
         }

         this.data = BufferUtils.createByteBuffer(out.length);
         this.data.put(out);
         this.data.flip();
         return this.data;
      } finally {
         if (pixmap != source) {
            pixmap.dispose();
         }
      }
   }

   private static byte[] readAll(InputStream in) throws IOException {
      ByteArrayOutputStream bos = new ByteArrayOutputStream(64 * 1024);
      byte[] buf = new byte[64 * 1024];

      int n;
      while ((n = in.read(buf)) > 0) {
         bos.write(buf, 0, n);
      }

      return bos.toByteArray();
   }

   @Override
   public int getDepth() {
      return 32;
   }

   @Override
   public int getWidth() {
      return this.width;
   }

   @Override
   public int getHeight() {
      return this.height;
   }

   @Override
   public int getTexWidth() {
      return this.texWidth;
   }

   @Override
   public int getTexHeight() {
      return this.texHeight;
   }

   @Override
   public ByteBuffer getImageBufferData() {
      return this.data;
   }
}
