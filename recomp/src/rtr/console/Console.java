package rtr.console;

import java.util.ArrayList;
import org.newdawn.slick.Graphics;
import rtr.SettingsParser;
import rtr.system.Game;

public class Console {
   private static ArrayList<ConsoleEntry> consoleDisplay = new ArrayList<>();
   private static ArrayList<ConsoleBanner> consoleBannerStorage = new ArrayList<>();
   private static int consoleMaxLength = 11;
   private static ConsoleBanner[] consoleSlots = new ConsoleBanner[6];

   public static void update() {
      for (int i = 0; i < consoleDisplay.size(); i++) {
         consoleDisplay.get(i).update();
      }

      if (consoleDisplay.size() > consoleMaxLength + 1) {
         consoleDisplay.remove(0);
      }

      if (consoleBannerStorage.size() > 0) {
         for (int c = 0; c < consoleSlots.length; c++) {
            if (consoleSlots[c] == null) {
               consoleSlots[c] = consoleBannerStorage.get(0);
               consoleBannerStorage.remove(0);
               if (consoleBannerStorage.size() == 0) {
                  break;
               }
            }
         }
      }

      for (int cx = 0; cx < consoleSlots.length; cx++) {
         ConsoleBanner b = consoleSlots[cx];
         if (b != null) {
            b.update(consoleBannerStorage.size());
            if (b.getDecayTick() > 20) {
               consoleSlots[cx] = null;
            }
         }
      }
   }

   public static void renderConsole(Graphics g, int x, int y, int length) {
      for (int i = length; i > 0; i--) {
         if (consoleDisplay.size() > i) {
            consoleDisplay.get(consoleDisplay.size() - i).render(x + 6, y - i * 12 - 3);
         }
      }
   }

   public static void renderBanner(Graphics g) {
      int offsetY = 0;

      for (int c = 0; c < consoleSlots.length; c++) {
         ConsoleBanner b = consoleSlots[c];
         if (b != null) {
            b.render(offsetY, g);
         }

         offsetY += 50;
      }
   }

   public static void out(int m, boolean v) {
      out(Integer.toString(m), v);
   }

   public static void out(double m, boolean v) {
      out(Double.toString(m), v);
   }

   public static void out(long m, boolean v) {
      out(Long.toString(m), v);
   }

   public static void out(String text, boolean verbose) {
      StringBuilder sb = new StringBuilder(text);

      for (int t = sb.length() - 1; t >= 0; t--) {
         if (sb.charAt(t) == '$') {
            sb.delete(t, t + 5);
         }
      }

      System.out.println(sb.toString());
      if (!verbose || SettingsParser.getVerboseConsole()) {
         for (ConsoleEntry e : consoleDisplay) {
            e.decreaseAlpha(20.0F);
         }

         consoleDisplay.add(new ConsoleEntry(text));
      }
   }

   public static void newBanner(String header, String subtitle) {
      out(header + " " + subtitle, true);

      for (int c = 0; c < consoleSlots.length; c++) {
         if (consoleSlots[c] == null) {
            consoleSlots[c] = new ConsoleBanner(header, subtitle);
            return;
         }
      }

      consoleBannerStorage.add(new ConsoleBanner(header, subtitle));
   }

   public static void saveConverterOut(Object classFile, String oldVersion, String newVersion, String text) {
      System.out.println("(" + classFile.getClass().getSimpleName() + ") (" + oldVersion + " -> " + newVersion + ") - " + text);
      ArrayList<String> gameVersions = Game.getValidGameVersions();
      if (!gameVersions.contains(oldVersion)) {
         System.out.println("CAUTION: " + oldVersion + " save data is no longer officially supported by the save system.");
      }
   }
}
