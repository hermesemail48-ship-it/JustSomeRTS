package org.newdawn.slick;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.newdawn.slick.opengl.GLUtils;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;
import org.newdawn.slick.util.BufferedImageUtil;

public class TrueTypeFont implements Font {
   private static final SGL GL = Renderer.get();
   private TrueTypeFont.IntObject[] charArray = new TrueTypeFont.IntObject[256];
   private Map customChars = new HashMap();
   private boolean antiAlias;
   private int fontSize = 0;
   private int fontHeight = 0;
   private Texture fontTexture;
   private int textureWidth = 512;
   private int textureHeight = 512;
   private java.awt.Font font;
   private FontMetrics fontMetrics;

   public TrueTypeFont(java.awt.Font font, boolean antiAlias, char[] additionalChars) {
      GLUtils.checkGLContext();
      this.font = font;
      this.fontSize = font.getSize();
      this.antiAlias = antiAlias;
      this.createSet(additionalChars);
   }

   public TrueTypeFont(java.awt.Font font, boolean antiAlias) {
      this(font, antiAlias, null);
   }

   private BufferedImage getFontImage(char ch) {
      BufferedImage tempfontImage = new BufferedImage(1, 1, 2);
      Graphics2D g = (Graphics2D)tempfontImage.getGraphics();
      if (this.antiAlias) {
         g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      }

      g.setFont(this.font);
      this.fontMetrics = g.getFontMetrics();
      int charwidth = this.fontMetrics.charWidth(ch);
      if (charwidth <= 0) {
         charwidth = 1;
      }

      int charheight = this.fontMetrics.getHeight();
      if (charheight <= 0) {
         charheight = this.fontSize;
      }

      BufferedImage fontImage = new BufferedImage(charwidth, charheight, 2);
      Graphics2D gt = (Graphics2D)fontImage.getGraphics();
      if (this.antiAlias) {
         gt.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      }

      gt.setFont(this.font);
      gt.setColor(java.awt.Color.WHITE);
      int charx = 0;
      int chary = 0;
      gt.drawString(String.valueOf(ch), charx, chary + this.fontMetrics.getAscent());
      return fontImage;
   }

   private void createSet(char[] customCharsArray) {
      if (customCharsArray != null && customCharsArray.length > 0) {
         this.textureWidth *= 16;
      }

      try {
         BufferedImage imgTemp = new BufferedImage(this.textureWidth, this.textureHeight, 2);
         Graphics2D g = (Graphics2D)imgTemp.getGraphics();
         g.setColor(new java.awt.Color(255, 255, 255, 0));
         g.fillRect(0, 0, this.textureWidth, this.textureHeight);
         int rowHeight = 0;
         int positionX = 0;
         int positionY = 0;
         int customCharsLength = customCharsArray != null ? customCharsArray.length : 0;

         for (int i = 0; i < 256 + customCharsLength; i++) {
            char ch = i < 256 ? (char)i : customCharsArray[i - 256];
            BufferedImage fontImage = this.getFontImage(ch);
            TrueTypeFont.IntObject newIntObject = new TrueTypeFont.IntObject();
            newIntObject.width = fontImage.getWidth();
            newIntObject.height = fontImage.getHeight();
            if (positionX + newIntObject.width >= this.textureWidth) {
               positionX = 0;
               positionY += rowHeight;
               rowHeight = 0;
            }

            newIntObject.storedX = positionX;
            newIntObject.storedY = positionY;
            if (newIntObject.height > this.fontHeight) {
               this.fontHeight = newIntObject.height;
            }

            if (newIntObject.height > rowHeight) {
               rowHeight = newIntObject.height;
            }

            g.drawImage(fontImage, positionX, positionY, null);
            positionX += newIntObject.width;
            if (i < 256) {
               this.charArray[i] = newIntObject;
            } else {
               this.customChars.put(new Character(ch), newIntObject);
            }

            Object var13 = null;
         }

         this.fontTexture = BufferedImageUtil.getTexture(this.font.toString(), imgTemp);
      } catch (IOException var12) {
         System.err.println("Failed to create font.");
         var12.printStackTrace();
      }
   }

   private void drawQuad(float drawX, float drawY, float drawX2, float drawY2, float srcX, float srcY, float srcX2, float srcY2) {
      float DrawWidth = drawX2 - drawX;
      float DrawHeight = drawY2 - drawY;
      float TextureSrcX = srcX / this.textureWidth;
      float TextureSrcY = srcY / this.textureHeight;
      float SrcWidth = srcX2 - srcX;
      float SrcHeight = srcY2 - srcY;
      float RenderWidth = SrcWidth / this.textureWidth;
      float RenderHeight = SrcHeight / this.textureHeight;
      GL.glTexCoord2f(TextureSrcX, TextureSrcY);
      GL.glVertex2f(drawX, drawY);
      GL.glTexCoord2f(TextureSrcX, TextureSrcY + RenderHeight);
      GL.glVertex2f(drawX, drawY + DrawHeight);
      GL.glTexCoord2f(TextureSrcX + RenderWidth, TextureSrcY + RenderHeight);
      GL.glVertex2f(drawX + DrawWidth, drawY + DrawHeight);
      GL.glTexCoord2f(TextureSrcX + RenderWidth, TextureSrcY);
      GL.glVertex2f(drawX + DrawWidth, drawY);
   }

   @Override
   public int getWidth(String whatchars) {
      int totalwidth = 0;
      TrueTypeFont.IntObject intObject = null;
      int currentChar = 0;

      for (int i = 0; i < whatchars.length(); i++) {
         int var7 = whatchars.charAt(i);
         if (var7 < 256) {
            intObject = this.charArray[var7];
         } else {
            intObject = (TrueTypeFont.IntObject)this.customChars.get(new Character((char)var7));
         }

         if (intObject != null) {
            totalwidth += intObject.width;
         }
      }

      return totalwidth;
   }

   public int getHeight() {
      return this.fontHeight;
   }

   @Override
   public int getHeight(String HeightString) {
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
      this.fontTexture.bind();
      TrueTypeFont.IntObject intObject = null;
      GL.glBegin(7);
      int totalwidth = 0;

      for (int i = 0; i < whatchars.length(); i++) {
         int charCurrent = whatchars.charAt(i);
         if (charCurrent < 256) {
            intObject = this.charArray[charCurrent];
         } else {
            intObject = (TrueTypeFont.IntObject)this.customChars.get(new Character((char)charCurrent));
         }

         if (intObject != null) {
            if (i >= startIndex || i <= endIndex) {
               this.drawQuad(
                  x + totalwidth,
                  y,
                  x + totalwidth + intObject.width,
                  y + intObject.height,
                  intObject.storedX,
                  intObject.storedY,
                  intObject.storedX + intObject.width,
                  intObject.storedY + intObject.height
               );
            }

            totalwidth += intObject.width;
         }
      }

      GL.glEnd();
   }

   @Override
   public void drawString(float x, float y, String whatchars) {
      this.drawString(x, y, whatchars, Color.white);
   }

   private class IntObject {
      public int width;
      public int height;
      public int storedX;
      public int storedY;

      private IntObject() {
      }
   }
}
