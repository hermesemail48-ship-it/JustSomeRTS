package rtr.utilities.devtools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class LineCounter implements Runnable {
   private static int sum = 0;

   public static void countLines() {
      LineCounter lineCounter = new LineCounter();
      lineCounter.runThread();
   }

   private void runThread() {
      Thread t = new Thread(this);
      t.start();
   }

   @Override
   public void run() {
      File testsFolder = new File("C:/Java Projects/SixtyGig Workspace/Rise to Ruins/src/rtr");
      if (!testsFolder.isDirectory()) {
         System.out.println("Not a directory: " + testsFolder);
      } else {
         sum = 0;

         File[] var5;
         for (File file : var5 = testsFolder.listFiles()) {
            sum = sum + count(file);
         }

         System.out.println(sum);
      }
   }

   // $VF: Could not inline inconsistent finally blocks
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private static int count(File file) {
      if (file.isDirectory()) {
         System.out.println("Entering " + file.getName());
         int sum = 0;

         File[] var19;
         for (File subFiles : var19 = file.listFiles()) {
            sum += count(subFiles);
         }

         return sum;
      } else {
         if (file.getName().endsWith(".java")) {
            System.out.println("Counting " + file.getName());

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
               int sum = 0;

               String s;
               while ((s = reader.readLine()) != null) {
                  if (!s.trim().isEmpty()) {
                     sum++;
                  }
               }

               return sum;
            } catch (Exception var14) {
               var14.printStackTrace();
            }
         }

         return 0;
      }
   }

   public static int getLinesOfCode() {
      return sum;
   }
}
