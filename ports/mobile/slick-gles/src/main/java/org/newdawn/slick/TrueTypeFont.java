package org.newdawn.slick;

import com.badlogic.gdx.graphics.Pixmap;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import org.newdawn.slick.opengl.ImageData;
import org.newdawn.slick.opengl.InternalTextureLoader;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;
import rtr.compat.font.GdxFont;
import rtr.compat.lwjgl.BufferUtils;

/**
 * Rise to Ruins' TrueTypeFont (fixed cells: advance width x line height, all glyphs in one atlas), rasterised with
 * FreeType instead of AWT. The atlas grows in height as needed and is split into pages above the GPU size limit.
 */
public class TrueTypeFont implements Font {
   private static final SGL GL = Renderer.get();
   private final TrueTypeFont.Glyph[] charArray = new TrueTypeFont.Glyph[256];
   private final Map<Character, TrueTypeFont.Glyph> customChars = new HashMap<Character, TrueTypeFont.Glyph>();
   private final boolean antiAlias;
   private int fontHeight;
   private Texture[] pages;
   private int pageWidth;
   private int pageHeight;

   public TrueTypeFont(GdxFont font, boolean antiAlias, char[] additionalChars) {
      this.antiAlias = antiAlias;
      this.createSet(font, additionalChars);
   }

   public TrueTypeFont(GdxFont font, boolean antiAlias) {
      this(font, antiAlias, null);
   }

   private void createSet(GdxFont font, char[] custom) {
      int customLength = custom != null ? custom.length : 0;
      int count = 256 + customLength;
      this.fontHeight = Math.max(1, font.getLineHeight());
      int maxSize = Math.min(4096, Image.getMaxSingleImageSize());
      this.pageWidth = Math.min(maxSize, customLength > 0 ? 2048 : 512);
      this.pageHeight = maxSize;
      TrueTypeFont.Glyph[] glyphs = new TrueTypeFont.Glyph[count];
      int page = 0;
      int x = 0;
      int y = 0;
      int usedHeight = 0;

      for (int i = 0; i < count; i++) {
         char ch = i < 256 ? (char)i : custom[i - 256];
         TrueTypeFont.Glyph g = new TrueTypeFont.Glyph();
         g.ch = ch;
         g.width = Math.max(1, font.charWidth(ch));
         g.height = this.fontHeight;
         if (x + g.width >= this.pageWidth) {
            x = 0;
            y += this.fontHeight;
         }

         if (y + this.fontHeight > this.pageHeight) {
            page++;
            x = 0;
            y = 0;
         }

         g.page = page;
         g.storedX = x;
         g.storedY = y;
         x += g.width;
         if (page == 0) {
            usedHeight = Math.max(usedHeight, y + this.fontHeight);
         }

         glyphs[i] = g;
         if (i < 256) {
            this.charArray[i] = g;
         } else {
            this.customChars.put(ch, g);
         }
      }

      if (page == 0) {
         this.pageHeight = InternalTextureLoader.get2Fold(usedHeight);
      }

      Pixmap[] atlases = new Pixmap[page + 1];

      for (int p = 0; p <= page; p++) {
         atlases[p] = new Pixmap(this.pageWidth, this.pageHeight, Pixmap.Format.RGBA8888);
         atlases[p].setBlending(Pixmap.Blending.None);
         atlases[p].setColor(0x00000000);
         atlases[p].fill();
      }

      for (TrueTypeFont.Glyph g : glyphs) {
         Pixmap cell = font.renderCell(g.ch, g.width, this.antiAlias);
         atlases[g.page].drawPixmap(cell, g.storedX, g.storedY);
         cell.dispose();
      }

      this.pages = new Texture[page + 1];

      for (int p = 0; p <= page; p++) {
         try {
            this.pages[p] = InternalTextureLoader.get().getTexture(new TrueTypeFont.PixmapData(atlases[p]), 9729);
         } catch (IOException var14) {
            throw new RuntimeException("Failed to create font texture", var14);
         } finally {
            atlases[p].dispose();
         }
      }
   }

   private TrueTypeFont.Glyph glyph(int c) {
      return c < 256 ? this.charArray[c] : this.customChars.get((char)c);
   }

   private void drawQuad(float drawX, float drawY, float drawX2, float drawY2, float srcX, float srcY, float srcX2, float srcY2) {
      float tx = srcX / this.pageWidth;
      float ty = srcY / this.pageHeight;
      float tw = (srcX2 - srcX) / this.pageWidth;
      float th = (srcY2 - srcY) / this.pageHeight;
      GL.glTexCoord2f(tx, ty);
      GL.glVertex2f(drawX, drawY);
      GL.glTexCoord2f(tx, ty + th);
      GL.glVertex2f(drawX, drawY2);
      GL.glTexCoord2f(tx + tw, ty + th);
      GL.glVertex2f(drawX2, drawY2);
      GL.glTexCoord2f(tx + tw, ty);
      GL.glVertex2f(drawX2, drawY);
   }

   @Override
   public int getWidth(String whatchars) {
      int total = 0;

      for (int i = 0; i < whatchars.length(); i++) {
         TrueTypeFont.Glyph g = this.glyph(whatchars.charAt(i));
         if (g != null) {
            total += g.width;
         }
      }

      return total;
   }

   public int getHeight() {
      return this.fontHeight;
   }

   @Override
   public int getHeight(String heightString) {
      return this.fontHeight;
   }

   @Override
   public int getLineHeight() {
      return this.fontHeight;
   }

   @Override
   public void drawString(float x, float y, String whatchars, Color color) {
      this.drawString(x, y, whatchars, color, 0, whatchars.length() - 1);
   }

   @Override
   public void drawString(float x, float y, String whatchars, Color color, int startIndex, int endIndex) {
      color.bind();

      for (int p = 0; p < this.pages.length; p++) {
         this.pages[p].bind();
         GL.glBegin(7);
         int total = 0;

         for (int i = 0; i < whatchars.length(); i++) {
            TrueTypeFont.Glyph g = this.glyph(whatchars.charAt(i));
            if (g != null) {
               if (g.page == p) {
                  this.drawQuad(x + total, y, x + total + g.width, y + g.height, g.storedX, g.storedY, g.storedX + g.width, g.storedY + g.height);
               }

               total += g.width;
            }
         }

         GL.glEnd();
      }
   }

   @Override
   public void drawString(float x, float y, String whatchars) {
      this.drawString(x, y, whatchars, Color.white);
   }

   private static class Glyph {
      char ch;
      int width;
      int height;
      int storedX;
      int storedY;
      int page;
   }

   /** Exposes an RGBA Pixmap as Slick ImageData (dimensions are already powers of two). */
   private static class PixmapData implements ImageData {
      private final int width;
      private final int height;
      private final ByteBuffer data;

      PixmapData(Pixmap p) {
         this.width = p.getWidth();
         this.height = p.getHeight();
         ByteBuffer src = p.getPixels();
         this.data = BufferUtils.createByteBuffer(src.remaining());
         this.data.put(src);
         this.data.flip();
         src.position(0);
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
         return InternalTextureLoader.get2Fold(this.width);
      }

      @Override
      public int getTexHeight() {
         return InternalTextureLoader.get2Fold(this.height);
      }

      @Override
      public ByteBuffer getImageBufferData() {
         return this.data;
      }
   }
}
