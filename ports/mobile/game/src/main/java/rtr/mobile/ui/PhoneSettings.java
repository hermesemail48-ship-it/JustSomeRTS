package rtr.mobile.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import rtr.SettingsParser;

/**
 * Phone-only options (kept in the platform's preferences store) plus shortcuts to the game's own quality settings.
 * Read every frame by {@link rtr.mobile.RtrGdxGame}.
 */
public final class PhoneSettings {
   /** Frame rate while touching the screen. */
   public static int fps = 30;
   /** Lower the frame rate when nobody is touching the screen. */
   public static boolean batterySaver = true;
   /** Scene buffer resolution relative to the logical 720p screen. */
   public static float resolution = 1.0F;
   public static final float[] RESOLUTIONS = new float[]{1.0F, 0.8F, 0.65F};
   public static final String[] RESOLUTION_NAMES = new String[]{"Sharp", "Balanced", "Battery"};
   private static Preferences prefs;

   private PhoneSettings() {
   }

   public static void load() {
      try {
         prefs = Gdx.app.getPreferences("rtr-phone");
         fps = prefs.getInteger("fps", 30) >= 60 ? 60 : 30;
         batterySaver = prefs.getBoolean("batterySaver", true);
         resolution = prefs.getFloat("resolution", 1.0F);
      } catch (RuntimeException e) {
         System.err.println("Phone settings: " + e);
      }
   }

   public static void save() {
      if (prefs != null) {
         prefs.putInteger("fps", fps);
         prefs.putBoolean("batterySaver", batterySaver);
         prefs.putFloat("resolution", resolution);
         prefs.flush();
      }
   }

   public static boolean clouds() {
      return SettingsParser.getClouds();
   }

   public static void setClouds(boolean on) {
      SettingsParser.setBaseValue("clouds", Boolean.toString(on));
   }

   public static boolean shadows() {
      return SettingsParser.getShadows();
   }

   public static void setShadows(boolean on) {
      SettingsParser.setBaseValue("shadows", Boolean.toString(on));
   }

   /** 0 minimal, 1 reduced, 2 full. */
   public static int particles() {
      String v = SettingsParser.getBaseValue("particleAmount");
      return v == null ? 2 : Integer.parseInt(v);
   }

   public static void setParticles(int amount) {
      SettingsParser.setBaseValue("particleAmount", Integer.toString(amount));
   }
}
