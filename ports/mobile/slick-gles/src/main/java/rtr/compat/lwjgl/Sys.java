package rtr.compat.lwjgl;

import com.badlogic.gdx.Gdx;

public final class Sys {
   public static long getTimerResolution() {
      return 1000L;
   }

   public static long getTime() {
      return System.nanoTime() / 1000000L;
   }

   public static String getClipboard() {
      return Gdx.app == null || Gdx.app.getClipboard() == null ? null : Gdx.app.getClipboard().getContents();
   }

   public static String getVersion() {
      return "2.9.3-gdx";
   }

   public static void alert(String title, String message) {
      System.err.println(title + ": " + message);
   }

   public static boolean openURL(String url) {
      return Gdx.net != null && Gdx.net.openURI(url);
   }
}
