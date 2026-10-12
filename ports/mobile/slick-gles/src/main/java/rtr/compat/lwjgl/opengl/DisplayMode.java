package rtr.compat.lwjgl.opengl;

public final class DisplayMode {
   private final int width;
   private final int height;
   private final int bpp;
   private final int freq;

   public DisplayMode(int width, int height) {
      this(width, height, 32, 60);
   }

   public DisplayMode(int width, int height, int bpp, int freq) {
      this.width = width;
      this.height = height;
      this.bpp = bpp;
      this.freq = freq;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public int getBitsPerPixel() {
      return this.bpp;
   }

   public int getFrequency() {
      return this.freq;
   }

   public boolean isFullscreenCapable() {
      return true;
   }

   @Override
   public String toString() {
      return this.width + " x " + this.height + " x " + this.bpp + " @" + this.freq + "Hz";
   }
}
