package org.newdawn.slick.imageout;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import rtr.compat.RtrPaths;

/** Slick's ImageOut without ImageIO: PNG via libGDX, BMP (24-bit, as ImageIO wrote it) via a small encoder. */
public class ImageOut {
   public static final String TGA = "tga";
   public static final String PNG = "png";
   public static final String JPG = "jpg";
   public static final String BMP = "bmp";
   public static final String GIF = "gif";
   public static final String[] getSupportedFormats() {
      return new String[]{PNG, BMP};
   }

   public static void write(Image image, String format, OutputStream out) throws SlickException {
      write(image, format, out, true);
   }

   public static void write(Image image, String format, OutputStream out, boolean writeAlpha) throws SlickException {
      try {
         if (format.equalsIgnoreCase(BMP)) {
            writeBmp(image, out);
         } else {
            Pixmap p = toPixmap(image, writeAlpha);

            try {
               PixmapIO.PNG png = new PixmapIO.PNG();
               png.setFlipY(false);
               png.write(out, p);
               png.dispose();
            } finally {
               p.dispose();
            }
         }
      } catch (IOException var8) {
         throw new SlickException("Unable to write image", var8);
      }
   }

   public static void write(Image image, String dest) throws SlickException {
      write(image, dest, true);
   }

   public static void write(Image image, String dest, boolean writeAlpha) throws SlickException {
      int ext = dest.lastIndexOf(46);
      if (ext < 0) {
         throw new SlickException("Unable to determine format from: " + dest);
      }

      write(image, dest.substring(ext + 1), dest, writeAlpha);
   }

   public static void write(Image image, String format, String dest) throws SlickException {
      write(image, format, dest, true);
   }

   public static void write(Image image, String format, String dest, boolean writeAlpha) throws SlickException {
      File file = new File(RtrPaths.resolve(dest));
      if (file.getParentFile() != null) {
         file.getParentFile().mkdirs();
      }

      try {
         OutputStream out = new BufferedOutputStream(new FileOutputStream(file));

         try {
            write(image, format, out, writeAlpha);
         } finally {
            out.close();
         }
      } catch (IOException var10) {
         throw new SlickException("Unable to write to the destination: " + dest, var10);
      }
   }

   private static Pixmap toPixmap(Image image, boolean writeAlpha) {
      int w = image.getWidth();
      int h = image.getHeight();
      Pixmap p = new Pixmap(w, h, Pixmap.Format.RGBA8888);
      p.setBlending(Pixmap.Blending.None);

      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            Color c = image.getColor(x, y);
            int a = writeAlpha ? c.getAlpha() : 255;
            p.drawPixel(x, y, c.getRed() << 24 | c.getGreen() << 16 | c.getBlue() << 8 | a);
         }
      }

      return p;
   }

   private static void writeBmp(Image image, OutputStream out) throws IOException {
      int w = image.getWidth();
      int h = image.getHeight();
      int rowSize = (w * 3 + 3) & -4;
      int dataSize = rowSize * h;
      byte[] header = new byte[54];
      header[0] = 66;
      header[1] = 77;
      putInt(header, 2, 54 + dataSize);
      putInt(header, 10, 54);
      putInt(header, 14, 40);
      putInt(header, 18, w);
      putInt(header, 22, h);
      header[26] = 1;
      header[28] = 24;
      putInt(header, 34, dataSize);
      out.write(header);
      byte[] row = new byte[rowSize];

      for (int y = h - 1; y >= 0; y--) {
         for (int x = 0; x < w; x++) {
            Color c = image.getColor(x, y);
            row[x * 3] = (byte)c.getBlue();
            row[x * 3 + 1] = (byte)c.getGreen();
            row[x * 3 + 2] = (byte)c.getRed();
         }

         out.write(row);
      }

      out.flush();
   }

   private static void putInt(byte[] b, int o, int v) {
      b[o] = (byte)v;
      b[o + 1] = (byte)(v >> 8);
      b[o + 2] = (byte)(v >> 16);
      b[o + 3] = (byte)(v >> 24);
   }
}
