package rtr.utilities;

public class FastMath {
   private static final float RAD = 0.017453292F;
   private static final int SIN_BITS = 12;
   private static final int SIN_MASK = 4095;
   private static final int SIN_COUNT = 4096;
   private static final float radFull = 6.2831855F;
   private static final float degFull = 360.0F;
   private static final float radToIndex = 651.8986F;
   private static final float degToIndex = 11.377778F;
   private static final float[] sin = new float[4096];
   private static final float[] cos = new float[4096];

   static {
      for (int i = 0; i < 4096; i++) {
         sin[i] = (float)Math.sin((i + 0.5F) / 4096.0F * 6.2831855F);
         cos[i] = (float)Math.cos((i + 0.5F) / 4096.0F * 6.2831855F);
      }
   }

   public static final float sin(float rad) {
      return sin[(int)(rad * 651.8986F) & 4095];
   }

   public static final float cos(float rad) {
      return cos[(int)(rad * 651.8986F) & 4095];
   }

   public static final float sinDeg(float deg) {
      return sin[(int)(deg * 11.377778F) & 4095];
   }

   public static final float cosDeg(float deg) {
      return cos[(int)(deg * 11.377778F) & 4095];
   }

   public static final float sinDegStrict(float deg) {
      return (float)Math.sin(deg * 0.017453292F);
   }

   public static final float cosDegStrict(float deg) {
      return (float)Math.cos(deg * 0.017453292F);
   }
}
