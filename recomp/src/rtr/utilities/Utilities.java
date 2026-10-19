package rtr.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.CharSequenceInputStream;
import org.apache.commons.net.ftp.FTPClient;
import org.newdawn.slick.Color;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import rtr.console.Console;
import rtr.system.Game;
import rtr.system.threadpool.RTRThreadPool;

public class Utilities {
   private static Random random = new Random();
   private static long startTime;
   private static long endTime;
   private static boolean sendingProfile;
   private static boolean sendingProfileFailed;
   private static int sendingProfileAttempts;

   public static void startTimer() {
      startTime = System.currentTimeMillis();
   }

   public static boolean endTimer(String message, int cullBelow) {
      endTime = System.currentTimeMillis();
      if (endTime - startTime > cullBelow) {
         Console.out(message + (endTime - startTime), true);
         return true;
      } else {
         return false;
      }
   }

   public static void endTimer(String message) {
      endTimer(message, 0);
   }

   public static void endTimer() {
      endTimer("Time: ", 0);
   }

   public static void startTimerNano() {
      startTime = System.nanoTime();
   }

   public static boolean endTimerNano(String message, int cullBelow) {
      endTime = System.nanoTime();
      if (endTime - startTime > cullBelow) {
         Console.out(message + (endTime - startTime), true);
         return true;
      } else {
         return false;
      }
   }

   public static void endTimerNano(String message) {
      endTimerNano(message, 0);
   }

   public static void endTimerNano() {
      endTimerNano("Time: ", 0);
   }

   public static int randomInt(int i) {
      if (i < 1) {
         System.out.println("Random " + i + " is too low, set to 1.");
         i = 1;
      }

      return random.nextInt(i);
   }

   public static int randomInt(int x, int y) {
      if (x >= y) {
         System.out.println("Random values flipped! Fixing..");
         int y1 = x;
         x = y;
         y = y1;
      }

      int difference = Math.abs(x - y);
      if (difference < 1) {
         System.out.println("Random " + difference + " is too low, set to 1.");
         difference = 1;
      }

      int randomInt = random.nextInt(difference + 1);
      return randomInt + x;
   }

   public static float randomFloat() {
      return ThreadLocalRandom.current().nextFloat();
   }

   public static boolean isOutOfBoundsOfUsableMap(int x, int y) {
      return x < 1 || y < 1 || x >= 255 || y >= 255;
   }

   public static boolean isOutOfBoundsOfUsableMapPixels(int x, int y) {
      return x < 1 || y < 1 || x >= 4095 || y >= 4095;
   }

   public static boolean isOutOfBounds(int width, int height, int x, int y) {
      return x < 0 || y < 0 || x >= width || y >= height;
   }

   public static boolean coordinatesWithin(int x, int y, int x2, int y2, int distance) {
      return Math.abs(x - x2) <= distance && Math.abs(y - y2) <= distance;
   }

   public static ArrayList<OrderedPair> getAllCoordinatesWithin(int centerX, int centerY, int distanceX, int distanceY) {
      ArrayList<OrderedPair> list = new ArrayList<>();
      int x = distanceX;
      int y = distanceY;
      int endX = distanceX + 1;
      int endY = distanceY + 1;

      for (int x2 = 0; x2 < endX; x2++) {
         for (int y2 = 0; y2 < endY; y2++) {
            int dx = x - x2;
            int dy = y - y2;
            int hValue = (int)Math.sqrt(dx * dx + dy * dy);
            if (hValue < distanceX || hValue < distanceY) {
               int shiftX = centerX - distanceX;
               int shiftY = centerY - distanceY;
               OrderedPair o = null;
               OrderedPair o2 = null;
               OrderedPair o3 = null;
               OrderedPair o4 = null;
               o = OrderedPair.getOrderedPair(shiftX + x2, shiftY + y2);
               if (x2 != endX - 1) {
                  o2 = OrderedPair.getOrderedPair(shiftX + x2 * -1 + distanceX * 2, shiftY + y2);
               }

               if (y2 != endY - 1) {
                  o3 = OrderedPair.getOrderedPair(shiftX + x2, shiftY + y2 * -1 + distanceY * 2);
               }

               if (x2 != endX - 1 && y2 != endY - 1) {
                  o4 = OrderedPair.getOrderedPair(shiftX + x2 * -1 + distanceX * 2, shiftY + y2 * -1 + distanceY * 2);
               }

               if (o != null) {
                  list.add(o);
               }

               if (o2 != null) {
                  list.add(o2);
               }

               if (o3 != null) {
                  list.add(o3);
               }

               if (o4 != null) {
                  list.add(o4);
               }
            }
         }
      }

      return list;
   }

   /**
    * Relative offsets (x, y pairs) of every tile getAllCoordinatesWithin returns for a distance, in its original order.
    * Callers ask for the same few distances thousands of times, so the shape is computed once per distance.
    */
   private static final java.util.concurrent.ConcurrentHashMap<Integer, int[]> WITHIN_OFFSETS = new java.util.concurrent.ConcurrentHashMap<>();

   public static int[] getOffsetsWithin(int distance) {
      int[] cached = WITHIN_OFFSETS.get(distance);
      if (cached != null) {
         return cached;
      }

      int x = distance;
      int y = distance;
      int endX = distance + 1;
      int endY = distance + 1;
      int[] out = new int[Math.max(8, (distance * 2 + 1) * (distance * 2 + 1) * 2)];
      int n = 0;

      for (int x2 = 0; x2 < endX; x2++) {
         for (int y2 = 0; y2 < endY; y2++) {
            int dx = x - x2;
            int dy = y - y2;
            int hValue = (int)Math.sqrt(dx * dx + dy * dy);
            if (hValue < distance) {
               out[n++] = x2 - distance;
               out[n++] = y2 - distance;
               if (x2 != endX - 1) {
                  out[n++] = -x2 + distance;
                  out[n++] = y2 - distance;
               }

               if (y2 != endY - 1) {
                  out[n++] = x2 - distance;
                  out[n++] = -y2 + distance;
               }

               if (x2 != endX - 1 && y2 != endY - 1) {
                  out[n++] = -x2 + distance;
                  out[n++] = -y2 + distance;
               }
            }
         }
      }

      int[] offsets = java.util.Arrays.copyOf(out, n);
      WITHIN_OFFSETS.put(distance, offsets);
      return offsets;
   }

   public static ArrayList<OrderedPair> getAllCoordinatesWithin(int centerX, int centerY, int distance) {
      int[] offsets = getOffsetsWithin(distance);
      ArrayList<OrderedPair> list = new ArrayList<>(offsets.length / 2);

      for (int i = 0; i < offsets.length; i += 2) {
         OrderedPair o = OrderedPair.getOrderedPair(centerX + offsets[i], centerY + offsets[i + 1]);
         if (o != null) {
            list.add(o);
         }
      }

      return list;
   }

   public static byte[][] createRangeMap(int d) {
      byte[][] out = new byte[d * 2][d * 2];
      int x = d;
      int y = d;
      int endX = out.length / 2 + 1;
      int endY = out[0].length / 2 + 1;

      for (int x2 = 0; x2 < endX; x2++) {
         for (int y2 = 0; y2 < endY; y2++) {
            int dx = x - x2;
            int dy = y - y2;
            int hValue = (int)Math.sqrt(dx * dx + dy * dy);
            if (hValue < d) {
               out[x2][y2] = 1;
               if (x2 != endX - 1) {
                  out[x2 * -1 + d * 2][y2] = 1;
               }

               if (y2 != endY - 1) {
                  out[x2][y2 * -1 + d * 2] = 1;
               }

               if (x2 != endX - 1 && y2 != endY - 1) {
                  out[x2 * -1 + d * 2][y2 * -1 + d * 2] = 1;
               }
            }
         }
      }

      return out;
   }

   public static float calculateAngle(float x1, float y1, float x2, float y2) {
      float xDiff = x2 - x1;
      float yDiff = y2 - y1;
      return (float)Math.atan2(yDiff, xDiff);
   }

   public static float calculateProjectileAngle(
      float x1, float y1, float x2, float y2, float missileSpeed, float missileFriction, float targetAngle, float targetVelocity
   ) {
      double cos = Math.cos(targetAngle) * targetVelocity;
      double sin = Math.sin(targetAngle) * targetVelocity;
      double dx = x2 - x1;
      double dy = y2 - y1;
      double hValue = Math.sqrt(dx * dx + dy * dy) / missileSpeed;

      for (int x = 1; x < hValue; x++) {
         hValue += missileFriction;
      }

      x2 = (float)(x2 + cos * hValue);
      y2 = (float)(y2 + sin * hValue);
      double xDiff = x2 - x1;
      double yDiff = y2 - y1;
      return (float)Math.atan2(yDiff, xDiff);
   }

   public static boolean isSendingProfile() {
      return sendingProfile;
   }

   public static boolean isSendingProfileFailed() {
      return sendingProfileFailed;
   }

   public static int getSendingProfileAttempts() {
      return sendingProfileAttempts;
   }

   public static void sendProfile(int number, String name, String category, String comments, String username) throws IOException {
      ForkJoinPool thread = RTRThreadPool.getThreadPool(8);
      thread.submit(() -> {
         sendingProfile = true;
         sendingProfileFailed = false;
         sendingProfileAttempts = 0;
         System.out.println("Packaging profile " + number);

         try {
            FileUtils.copyDirectory(new File("profiles/profile" + number + "/"), new File("profiles/profileOut/profile0/"));
            FileUtils.copyDirectory(new File("logs/"), new File("profiles/profileOut/logs/"));
         } catch (Exception var92) {
            sendingProfile = false;
            sendingProfileFailed = true;
            var92.printStackTrace();
         }

         Properties report = new Properties();
         report.setProperty("Number", "" + number);
         report.setProperty("Name", name);
         report.setProperty("Category", category);
         report.setProperty("Comments", comments);
         report.setProperty("Username", username);

         try {
            FileOutputStream out = new FileOutputStream("profiles/profileOut/report.txt");
            report.store(out, "Report");
            out.flush();
            out.close();
         } catch (IOException var91) {
            System.out.println("Failed to save report file!");
         }

         String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());

         try (
            FileOutputStream fos = new FileOutputStream(new File("profiles/" + name + "_" + category + "_" + timeStamp + ".zip"));
            ZipOutputStream zos = new ZipOutputStream(fos);
         ) {
            zipFolder(new File("profiles/profileOut/"), "", zos);
         } catch (IOException var100) {
            sendingProfile = false;
            sendingProfileFailed = true;
            return;
         }

         System.out.println("Uploading profile " + number);
         FTPClient client = new FTPClient();
         String fileName = name + "_" + category + "_" + timeStamp + ".zip";

         try (FileInputStream fis = new FileInputStream(new File("profiles/" + fileName))) {
            client.connect("ftp.rayvolution.net");
            client.login("rtrpublic@risetoruins.com", "rtr123");
            client.setFileType(2);

            for (boolean didStore = false; !didStore; sendingProfileAttempts++) {
               if (sendingProfileAttempts > 2) {
                  sendingProfileFailed = true;
                  break;
               }

               System.out.println("Upload Attempt: " + (sendingProfileAttempts + 1));
               didStore = client.storeFile(fileName, fis);
            }

            client.logout();
         } catch (IOException var97) {
            sendingProfile = false;
            sendingProfileFailed = true;
            var97.printStackTrace();
         } finally {
            try {
               sendingProfile = false;
               client.disconnect();
            } catch (IOException var89) {
               sendingProfile = false;
               var89.printStackTrace();
            }
         }

         if (sendingProfileFailed) {
            System.out.println("Upload of profile " + number + " failed.");
         } else {
            System.out.println("Upload of profile " + number + " completed.");
         }

         try {
            FileUtils.deleteDirectory(new File("profiles/profileOut/"));
            File zip = new File("profiles/" + name + "_" + category + "_" + timeStamp + ".zip");
            if (!zip.delete()) {
               throw new IOException("Could not delete " + zip);
            }
         } catch (Exception var90) {
            sendingProfile = false;
            var90.printStackTrace();
         }

         sendingProfile = false;
      });
   }

   public static boolean isStreamLive() {
      try {
         URL url = new URL("http://risetoruins.com/streamCheck.php");
         URLConnection conn = url.openConnection();
         BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
         String inputLine = br.readLine();
         br.close();
         return inputLine.equals("ONLINE");
      } catch (IOException var4) {
         var4.printStackTrace();
         return false;
      }
   }

   public static double getPercentOf(double number, double percentOff) {
      percentOff /= 100.0;
      return number * percentOff;
   }

   public static double getPercentage(double current, double maximum) {
      return getPercentage(current, maximum, 100.0);
   }

   public static double getPercentage(double current, double maximum, double width) {
      return Math.ceil(current / maximum * width);
   }

   public static Color[] returnColorArrayBrighter(Color[] in, float f) {
      Color[] out = new Color[in.length];

      for (int c = 0; c < in.length; c++) {
         out[c] = in[c].brighter(f);
      }

      return out;
   }

   public static Color[] returnColorArrayDarker(Color[] in, float f) {
      Color[] out = new Color[in.length];

      for (int c = 0; c < in.length; c++) {
         out[c] = in[c].darker(f);
      }

      return out;
   }

   public static void runFullMapConverter() {
      try {
         File[] listOfMaps = new File("maps/UserMaps/").listFiles();

         for (int m = 0; m < listOfMaps.length; m++) {
            if (listOfMaps[m].isDirectory()) {
               System.out.println("Starting conversion process of map " + listOfMaps[m]);
               ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(listOfMaps[m] + ".zip"));
               Properties objectProperties = new Properties();
               FileInputStream objectPropertiesFile = new FileInputStream(listOfMaps[m] + "/" + listOfMaps[m].getName() + ".objectData");
               objectProperties.load(objectPropertiesFile);
               objectPropertiesFile.close();
               Map<String, Object> objectDataMap = new HashMap<>();
               objectDataMap.put("version", Game.getVersion());
               int count = 0;

               for (Object s : objectProperties.keySet()) {
                  if (!s.toString().replaceAll("[^0-9]+", "").equals("")) {
                     String[] stringIn = objectProperties.getProperty(s.toString()).split(",");
                     Map<String, Object> singleObjectDataMap = new HashMap<>();
                     singleObjectDataMap.put("objectID", count);
                     singleObjectDataMap.put("currentType", stringIn[0]);
                     singleObjectDataMap.put("subType", stringIn[1]);
                     singleObjectDataMap.put("width", stringIn[2]);
                     singleObjectDataMap.put("height", stringIn[3]);
                     singleObjectDataMap.put("objectX", stringIn[4]);
                     singleObjectDataMap.put("objectY", stringIn[5]);
                     objectDataMap.put("object" + count, singleObjectDataMap);
                     count++;
                  }
               }

               objectDataMap.put("objectLastID", count + 1);
               DumperOptions options = new DumperOptions();
               options.setPrettyFlow(true);
               options.setDefaultFlowStyle(FlowStyle.BLOCK);
               Yaml yaml = new Yaml(options);
               StringWriter writer = new StringWriter();
               yaml.dump(objectDataMap, writer);
               CharSequenceInputStream writerInputStream = new CharSequenceInputStream(writer.toString(), StandardCharsets.UTF_8);
               zipOut.putNextEntry(new ZipEntry("object.yml"));
               byte[] b = new byte[1024];

               int writeCount;
               while ((writeCount = writerInputStream.read(b)) > 0) {
                  zipOut.write(b, 0, writeCount);
               }

               writerInputStream.close();
               Properties mapProperties = new Properties();
               FileInputStream mapPropertiesFile = new FileInputStream(listOfMaps[m] + "/" + listOfMaps[m].getName() + ".mapData");
               mapProperties.load(mapPropertiesFile);
               mapPropertiesFile.close();
               Map<String, Object> mapDataMap = new HashMap<>();
               mapDataMap.put("version", Game.getVersion());
               mapDataMap.put("mapName", mapProperties.get("mapName"));
               mapDataMap.put("mapFileName", mapProperties.get("mapFileName"));
               mapDataMap.put("mapAuthor", mapProperties.get("mapAuthor"));
               mapDataMap.put("baseTerrain", mapProperties.get("baseTerrain"));
               mapDataMap.put("mapX", mapProperties.get("mapX"));
               mapDataMap.put("mapY", mapProperties.get("mapY"));
               Map<String, Object> connectedRegion = new HashMap<>();

               for (int x = 0; x < 8; x++) {
                  connectedRegion.put(String.valueOf(x), mapProperties.get("connectedRegion" + x));
               }

               mapDataMap.put("connectedRegion", connectedRegion);
               mapDataMap.put("worldMapX", mapProperties.get("worldMapX"));
               mapDataMap.put("worldMapY", mapProperties.get("worldMapY"));
               options = new DumperOptions();
               options.setPrettyFlow(true);
               options.setDefaultFlowStyle(FlowStyle.BLOCK);
               yaml = new Yaml(options);
               writer = new StringWriter();
               yaml.dump(mapDataMap, writer);
               writerInputStream = new CharSequenceInputStream(writer.toString(), StandardCharsets.UTF_8);
               zipOut.putNextEntry(new ZipEntry("map.yml"));
               b = new byte[1024];
               int var31 = 0;

               while ((var31 = writerInputStream.read(b)) > 0) {
                  zipOut.write(b, 0, var31);
               }

               writerInputStream.close();
               FileInputStream mapInputStream = new FileInputStream(listOfMaps[m] + "/" + listOfMaps[m].getName() + ".rpcmap");
               zipOut.putNextEntry(new ZipEntry("map.array"));
               b = new byte[1024];
               int var33 = 0;

               while ((var33 = mapInputStream.read(b)) > 0) {
                  zipOut.write(b, 0, var33);
               }

               writerInputStream.close();
               mapInputStream.close();
               FileInputStream miniMapInputStream = new FileInputStream(listOfMaps[m] + "/" + listOfMaps[m].getName() + ".bmp");
               zipOut.putNextEntry(new ZipEntry("minimap.bmp"));
               b = new byte[1024];
               int var35 = 0;

               while ((var35 = miniMapInputStream.read(b)) > 0) {
                  zipOut.write(b, 0, var35);
               }

               writerInputStream.close();
               miniMapInputStream.close();
               zipOut.flush();
               zipOut.close();
            }
         }
      } catch (Exception var19) {
         var19.printStackTrace();
      }
   }

   public static int setAndKeepWithinBounds(int low, int high, int value) {
      if (value > high) {
         value = high;
      }

      if (value < low) {
         value = low;
      }

      return value;
   }

   /** Zips a folder like the Files.walkFileTree visitor this replaced (directory entries end with "/"). */
   private static void zipFolder(File dir, String prefix, ZipOutputStream zos) throws IOException {
      zos.putNextEntry(new ZipEntry(prefix.isEmpty() ? "/" : prefix + "/"));
      zos.closeEntry();
      File[] children = dir.listFiles();
      if (children == null) {
         throw new IOException("Not a directory: " + dir);
      }

      for (File f : children) {
         String name = prefix.isEmpty() ? f.getName() : prefix + "/" + f.getName();
         if (f.isDirectory()) {
            zipFolder(f, name, zos);
         } else {
            zos.putNextEntry(new ZipEntry(name));
            FileInputStream in = new FileInputStream(f);

            try {
               byte[] buf = new byte[8192];

               int n;
               while ((n = in.read(buf)) > 0) {
                  zos.write(buf, 0, n);
               }
            } finally {
               in.close();
            }

            zos.closeEntry();
         }
      }
   }
}
