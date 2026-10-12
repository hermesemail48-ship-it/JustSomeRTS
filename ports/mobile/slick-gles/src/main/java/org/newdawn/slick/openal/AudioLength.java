package org.newdawn.slick.openal;

import java.io.IOException;
import java.io.RandomAccessFile;

/** Reads a clip's duration from WAV/Ogg Vorbis headers, so Sound.playing() can be answered without OpenAL. */
public final class AudioLength {
   private AudioLength() {
   }

   /** Duration in seconds, or -1 if it cannot be determined. */
   public static float seconds(String path) {
      try {
         RandomAccessFile f = new RandomAccessFile(path, "r");

         float var3;
         try {
            String lower = path.toLowerCase();
            if (lower.endsWith(".wav")) {
               return wav(f);
            }

            if (!lower.endsWith(".ogg")) {
               return -1.0F;
            }

            var3 = ogg(f);
         } finally {
            f.close();
         }

         return var3;
      } catch (IOException var8) {
         return -1.0F;
      }
   }

   private static int le32(byte[] b, int o) {
      return b[o] & 0xFF | (b[o + 1] & 0xFF) << 8 | (b[o + 2] & 0xFF) << 16 | (b[o + 3] & 0xFF) << 24;
   }

   private static float wav(RandomAccessFile f) throws IOException {
      byte[] head = new byte[12];
      f.readFully(head);
      int byteRate = 0;
      byte[] chunk = new byte[8];

      while (f.getFilePointer() + 8 <= f.length()) {
         f.readFully(chunk);
         int size = le32(chunk, 4);
         String id = new String(chunk, 0, 4, "US-ASCII");
         if (id.equals("fmt ")) {
            byte[] fmt = new byte[Math.min(size, 16)];
            f.readFully(fmt);
            byteRate = le32(fmt, 8);
            f.seek(f.getFilePointer() + (size - fmt.length) + (size & 1));
         } else {
            if (id.equals("data")) {
               return byteRate > 0 ? (float)(size & 0xFFFFFFFFL) / byteRate : -1.0F;
            }

            f.seek(f.getFilePointer() + size + (size & 1));
         }
      }

      return -1.0F;
   }

   private static float ogg(RandomAccessFile f) throws IOException {
      byte[] first = new byte[Math.min(4096, (int)f.length())];
      f.readFully(first);
      int rate = 0;

      for (int i = 0; i + 16 < first.length; i++) {
         if (first[i] == 1 && first[i + 1] == 118 && first[i + 2] == 111 && first[i + 3] == 114 && first[i + 4] == 98 && first[i + 5] == 105 && first[i + 6] == 115) {
            rate = le32(first, i + 12);
            break;
         }
      }

      if (rate <= 0) {
         return -1.0F;
      }

      int tailLen = (int)Math.min(65536L, f.length());
      byte[] tail = new byte[tailLen];
      f.seek(f.length() - tailLen);
      f.readFully(tail);

      for (int i = tailLen - 14; i >= 0; i--) {
         if (tail[i] == 79 && tail[i + 1] == 103 && tail[i + 2] == 103 && tail[i + 3] == 83) {
            long granule = (long)le32(tail, i + 6) & 0xFFFFFFFFL | (long)le32(tail, i + 10) << 32;
            return granule > 0L ? (float)granule / rate : -1.0F;
         }
      }

      return -1.0F;
   }
}
