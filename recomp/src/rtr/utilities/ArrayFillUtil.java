package rtr.utilities;

public class ArrayFillUtil {
   public static void fill(byte[] a, byte i) {
      for (int x = 0; x < a.length; x++) {
         a[x] = i;
      }
   }

   public static void fill(byte[][] a, byte i) {
      for (int x = 0; x < a.length; x++) {
         for (int y = 0; y < a[x].length; y++) {
            a[x][y] = i;
         }
      }
   }

   public static void fill(byte[][][] a, byte i) {
      for (int x = 0; x < a.length; x++) {
         for (int y = 0; y < a[x].length; y++) {
            for (int z = 0; z < a[x][y].length; z++) {
               a[x][y][z] = i;
            }
         }
      }
   }

   public static void fill(int[] a, int i) {
      for (int x = 0; x < a.length; x++) {
         a[x] = i;
      }
   }

   public static void fill(int[][] a, int i) {
      for (int x = 0; x < a.length; x++) {
         for (int y = 0; y < a[x].length; y++) {
            a[x][y] = i;
         }
      }
   }

   public static void fill(int[][][] a, int i) {
      for (int x = 0; x < a.length; x++) {
         for (int y = 0; y < a[x].length; y++) {
            for (int z = 0; z < a[x][y].length; z++) {
               a[x][y][z] = i;
            }
         }
      }
   }

   public static void fill(float[] a, float i) {
      for (int x = 0; x < a.length; x++) {
         a[x] = i;
      }
   }

   public static void fill(float[][] a, float i) {
      for (int x = 0; x < a.length; x++) {
         for (int y = 0; y < a[x].length; y++) {
            a[x][y] = i;
         }
      }
   }

   public static void fill(float[][][] a, float i) {
      for (int x = 0; x < a.length; x++) {
         for (int y = 0; y < a[x].length; y++) {
            for (int z = 0; z < a[x][y].length; z++) {
               a[x][y][z] = i;
            }
         }
      }
   }
}
