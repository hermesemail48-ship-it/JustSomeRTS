package rtr.compat.lwjgl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public final class BufferUtils {
   public static ByteBuffer createByteBuffer(int size) {
      return ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
   }

   public static ShortBuffer createShortBuffer(int size) {
      return createByteBuffer(size << 1).asShortBuffer();
   }

   public static IntBuffer createIntBuffer(int size) {
      return createByteBuffer(size << 2).asIntBuffer();
   }

   public static FloatBuffer createFloatBuffer(int size) {
      return createByteBuffer(size << 2).asFloatBuffer();
   }

   public static DoubleBuffer createDoubleBuffer(int size) {
      return createByteBuffer(size << 3).asDoubleBuffer();
   }
}
