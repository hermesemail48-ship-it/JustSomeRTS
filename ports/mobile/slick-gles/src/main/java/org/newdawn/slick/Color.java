package org.newdawn.slick;

import java.io.Serializable;
import java.nio.FloatBuffer;
import org.newdawn.slick.opengl.renderer.Renderer;

public class Color implements Serializable {
   private static final long serialVersionUID = 1393939L;
   public static final Color transparent = new Color(0.0F, 0.0F, 0.0F, 0.0F);
   public static final Color white = new Color(1.0F, 1.0F, 1.0F, 1.0F);
   public static final Color yellow = new Color(1.0F, 1.0F, 0.0F, 1.0F);
   public static final Color red = new Color(1.0F, 0.0F, 0.0F, 1.0F);
   public static final Color blue = new Color(0.0F, 0.0F, 1.0F, 1.0F);
   public static final Color green = new Color(0.0F, 1.0F, 0.0F, 1.0F);
   public static final Color black = new Color(0.0F, 0.0F, 0.0F, 1.0F);
   public static final Color gray = new Color(0.5F, 0.5F, 0.5F, 1.0F);
   public static final Color cyan = new Color(0.0F, 1.0F, 1.0F, 1.0F);
   public static final Color darkGray = new Color(0.3F, 0.3F, 0.3F, 1.0F);
   public static final Color lightGray = new Color(0.7F, 0.7F, 0.7F, 1.0F);
   public static final Color pink = new Color(255, 175, 175, 255);
   public static final Color orange = new Color(255, 200, 0, 255);
   public static final Color magenta = new Color(255, 0, 255, 255);
   public float r;
   public float g;
   public float b;
   public float a = 1.0F;

   public Color(Color color) {
      this.r = color.r;
      this.g = color.g;
      this.b = color.b;
      this.a = color.a;
   }

   public Color(FloatBuffer buffer) {
      this.r = buffer.get();
      this.g = buffer.get();
      this.b = buffer.get();
      this.a = buffer.get();
   }

   public Color(float r, float g, float b) {
      this.r = r;
      this.g = g;
      this.b = b;
      this.a = 1.0F;
   }

   public Color(float r, float g, float b, float a) {
      this.r = Math.min(r, 1.0F);
      this.g = Math.min(g, 1.0F);
      this.b = Math.min(b, 1.0F);
      this.a = Math.min(a, 1.0F);
   }

   public Color(int r, int g, int b) {
      this.r = r / 255.0F;
      this.g = g / 255.0F;
      this.b = b / 255.0F;
      this.a = 1.0F;
   }

   public Color(int r, int g, int b, int a) {
      this.r = r / 255.0F;
      this.g = g / 255.0F;
      this.b = b / 255.0F;
      this.a = a / 255.0F;
   }

   public Color(int value) {
      int r = (value & 0xFF0000) >> 16;
      int g = (value & 0xFF00) >> 8;
      int b = value & 0xFF;
      int a = (value & 0xFF000000) >> 24;
      if (a < 0) {
         a += 256;
      }

      if (a == 0) {
         a = 255;
      }

      this.r = r / 255.0F;
      this.g = g / 255.0F;
      this.b = b / 255.0F;
      this.a = a / 255.0F;
   }

   public static Color decode(String nm) {
      return new Color(Integer.decode(nm));
   }

   public void setColor(Color color) {
      this.r = color.r;
      this.g = color.g;
      this.b = color.b;
      this.a = color.a;
   }

   public void setColor(FloatBuffer buffer) {
      this.r = buffer.get();
      this.g = buffer.get();
      this.b = buffer.get();
      this.a = buffer.get();
   }

   public void setColor(float r, float g, float b) {
      this.r = r;
      this.g = g;
      this.b = b;
      this.a = 1.0F;
   }

   public void setColor(float r, float g, float b, float a) {
      this.r = Math.min(r, 1.0F);
      this.g = Math.min(g, 1.0F);
      this.b = Math.min(b, 1.0F);
      this.a = Math.min(a, 1.0F);
   }

   public void setColor(int r, int g, int b) {
      this.r = r / 255.0F;
      this.g = g / 255.0F;
      this.b = b / 255.0F;
      this.a = 1.0F;
   }

   public void setColor(int r, int g, int b, int a) {
      this.r = r / 255.0F;
      this.g = g / 255.0F;
      this.b = b / 255.0F;
      this.a = a / 255.0F;
   }

   public void setColor(int value) {
      int r = (value & 0xFF0000) >> 16;
      int g = (value & 0xFF00) >> 8;
      int b = value & 0xFF;
      int a = (value & 0xFF000000) >> 24;
      if (a < 0) {
         a += 256;
      }

      if (a == 0) {
         a = 255;
      }

      this.r = r / 255.0F;
      this.g = g / 255.0F;
      this.b = b / 255.0F;
      this.a = a / 255.0F;
   }

   public void bind() {
      Renderer.get().glColor4f(this.r, this.g, this.b, this.a);
   }

   @Override
   public int hashCode() {
      return (int)(this.r + this.g + this.b + this.a) * 255;
   }

   @Override
   public boolean equals(Object other) {
      if (other instanceof Color) {
         Color o = (Color)other;
         return o.r == this.r && o.g == this.g && o.b == this.b && o.a == this.a;
      } else {
         return false;
      }
   }

   @Override
   public String toString() {
      return "Color (" + this.r + "," + this.g + "," + this.b + "," + this.a + ")";
   }

   public Color darker() {
      return this.darker(0.5F);
   }

   public Color darker(float scale) {
      scale = 1.0F - scale;
      return new Color(this.r * scale, this.g * scale, this.b * scale, this.a);
   }

   public Color brighter() {
      return this.brighter(0.2F);
   }

   public int getRed() {
      return (int)(this.r * 255.0F);
   }

   public int getGreen() {
      return (int)(this.g * 255.0F);
   }

   public int getBlue() {
      return (int)(this.b * 255.0F);
   }

   public int getAlpha() {
      return (int)(this.a * 255.0F);
   }

   public int getRedByte() {
      return (int)(this.r * 255.0F);
   }

   public int getGreenByte() {
      return (int)(this.g * 255.0F);
   }

   public int getBlueByte() {
      return (int)(this.b * 255.0F);
   }

   public int getAlphaByte() {
      return (int)(this.a * 255.0F);
   }

   public Color brighter(float scale) {
      return new Color(this.r * ++scale, this.g * scale, this.b * scale, this.a);
   }

   public Color multiply(Color c) {
      return new Color(this.r * c.r, this.g * c.g, this.b * c.b, this.a * c.a);
   }

   public void add(Color c) {
      this.r = this.r + c.r;
      this.g = this.g + c.g;
      this.b = this.b + c.b;
      this.a = this.a + c.a;
   }

   public void scale(float value) {
      this.r *= value;
      this.g *= value;
      this.b *= value;
      this.a *= value;
   }

   public Color addToCopy(Color c) {
      Color copy = new Color(this.r, this.g, this.b, this.a);
      copy.r = copy.r + c.r;
      copy.g = copy.g + c.g;
      copy.b = copy.b + c.b;
      copy.a = copy.a + c.a;
      return copy;
   }

   public Color scaleCopy(float value) {
      Color copy = new Color(this.r, this.g, this.b, this.a);
      copy.r *= value;
      copy.g *= value;
      copy.b *= value;
      copy.a *= value;
      return copy;
   }

   public Color blendColors(Color colorOne, Color colorTwo, float percentOfOne, float minPercent, float maxPercent) {
      minPercent /= 100.0F;
      maxPercent /= 100.0F;
      Color colorMergeOne = new Color(
         (colorOne.r * minPercent + (colorOne.r * maxPercent - colorOne.r * minPercent) * percentOfOne) / 100.0F,
         (colorOne.g * minPercent + (colorOne.g * maxPercent - colorOne.g * minPercent) * percentOfOne) / 100.0F,
         (colorOne.b * minPercent + (colorOne.b * maxPercent - colorOne.b * minPercent) * percentOfOne) / 100.0F
      );
      percentOfOne = 100.0F - percentOfOne;
      Color colorMergeTwo = new Color(
         (colorTwo.r * minPercent + (colorTwo.r * maxPercent - colorTwo.r * minPercent) * percentOfOne) / 100.0F,
         (colorTwo.g * minPercent + (colorTwo.g * maxPercent - colorTwo.g * minPercent) * percentOfOne) / 100.0F,
         (colorTwo.b * minPercent + (colorTwo.b * maxPercent - colorTwo.b * minPercent) * percentOfOne) / 100.0F
      );
      return new Color(colorMergeOne.r + colorMergeTwo.r, colorMergeOne.g + colorMergeTwo.g, colorMergeOne.b + colorMergeTwo.b);
   }
}
