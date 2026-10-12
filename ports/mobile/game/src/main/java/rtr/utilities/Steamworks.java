package rtr.utilities;

import java.util.ArrayList;

/** Mobile replacement for the Steamworks wrapper: same API, Steam is never available. */
public class Steamworks {
   public static boolean isRunning() {
      return false;
   }

   public static void initializeSteam() {
      System.out.println("SteamAPI: not available on this platform.");
   }

   public static void runCallbacks() {
   }

   public static void shutdownApi() {
   }

   public static void shutdownSteam() {
   }

   public static void grantAchievement(String achievementName, boolean silent) {
   }

   public static void clearAchievement(String achievementName, boolean silent) {
   }

   public static ArrayList<String> getAndRefreshLoadedLanguages() {
      return new ArrayList<>();
   }
}
