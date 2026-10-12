package rtr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.newdawn.slick.SlickException;
import rtr.console.Console;
import rtr.save.SavedGamesHandler;
import rtr.states.StateBase;
import rtr.system.Game;

public class ProfileModule extends ModuleBase {
   private static Properties profileProperties = new Properties();
   public static int currentSlot = -1;

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      this.initLoad();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
   }

   @Override
   protected void newMainMenu() throws SlickException {
   }

   @Override
   protected void newPlay() throws SlickException {
   }

   @Override
   protected void loadPlay() throws SlickException {
   }

   @Override
   protected void newMapEditor() throws SlickException {
   }

   @Override
   protected void loadMapEditor() throws SlickException {
   }

   @Override
   protected void newWorldMap() throws SlickException {
   }

   @Override
   protected void loadWorldMap() throws SlickException {
   }

   @Override
   protected void loadRegionalSaveData() throws SlickException {
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
   }

   public void createAndLoadProfile(int profileSlot, String profileName, String twitchChannel, boolean tipsEnabled) throws SlickException {
      currentSlot = profileSlot;

      try {
         File profile = new File("profiles/profile" + profileSlot + "/");
         profile.mkdirs();
         File profileSettings = new File("profiles/profile" + profileSlot + "/profile.properties");
         FileOutputStream out = new FileOutputStream(profileSettings);
         profileProperties.setProperty("profileName", profileName);
         profileProperties.setProperty("hideReview", "false");
         profileProperties.setProperty("daysSurvived", "0");
         profileProperties.setProperty("worldMapVersion", Game.getVersion());
         profileProperties.store(out, "settings");
         out.flush();
         out.close();
      } catch (IOException var8) {
         Console.out("ProfileModule failed to save new profile properties file!", false);
      }

      SettingsParser.createProfileSettings(profileSlot, twitchChannel, tipsEnabled);
      SettingsParser.setLastProfile(profileSlot);
   }

   public void saveProfile() {
      try {
         File profile = new File("profiles/profile" + currentSlot + "/");
         profile.mkdirs();
         File profileSettings = new File("profiles/profile" + currentSlot + "/profile.properties");
         FileOutputStream out = new FileOutputStream(profileSettings);
         profileProperties.store(out, "settings");
         out.flush();
         out.close();
      } catch (IOException var4) {
         Console.out("ProfileModule failed to save new profile properties file!", false);
      }
   }

   public void deleteProfile(int profileSlot) {
      System.gc();
      File f = new File("profiles/profile" + profileSlot);

      try {
         FileUtils.forceDelete(f);
      } catch (IOException var4) {
         var4.printStackTrace();
      }
   }

   private boolean profileExists(int profileSlot) throws SlickException {
      File profileSettings = new File("profiles/profile" + profileSlot + "/profile.properties");
      return profileSettings.exists();
   }

   public boolean profileValid(int profileSlot) throws SlickException {
      if (this.profileExists(profileSlot)) {
         Properties profileCheck = new Properties();

         try {
            File profileSettings = new File("profiles/profile" + profileSlot + "/profile.properties");
            FileInputStream in = new FileInputStream(profileSettings);
            profileCheck.load(in);
            in.close();
         } catch (IOException var5) {
            Console.out("Profile valid check failed.", false);
            return false;
         }

         return !profileCheck.getProperty("profileName", "null").equals("null") && Game.isValidGameVersion(profileCheck.getProperty("worldMapVersion", "null"));
      } else {
         return false;
      }
   }

   public void loadProfile(int profileSlot) throws SlickException {
      currentSlot = profileSlot;
      profileProperties.clear();

      try {
         File profileSettings = new File("profiles/profile" + profileSlot + "/profile.properties");
         FileInputStream in = new FileInputStream(profileSettings);
         profileProperties.load(in);
         in.close();
      } catch (IOException var4) {
         Console.out("ProfileModule failed to load profile properties file!", false);
      }

      SettingsParser.loadProfileSettings(profileSlot);
      SettingsParser.setLastProfile(profileSlot);
      SavedGamesHandler.loadData();
   }

   public String getProfileName() {
      return profileProperties.getProperty("profileName", "null");
   }

   public int getDaysSurvived() {
      return Integer.parseInt(profileProperties.getProperty("daysSurvived", "0"));
   }

   public void addDaySurvived() {
      int daysSurvived = Integer.parseInt(profileProperties.getProperty("daysSurvived", "0")) + 1;
      profileProperties.setProperty("daysSurvived", Integer.toString(daysSurvived));
      this.saveProfile();
   }

   public boolean hideReview() {
      return Boolean.parseBoolean(profileProperties.getProperty("hideReview", "false"));
   }

   public void setHideReview() {
      profileProperties.setProperty("hideReview", "true");
      this.saveProfile();
   }

   public static int getCurrentProfileSlot() {
      return currentSlot;
   }

   public void resetWorldMapVersion() {
      profileProperties.setProperty("worldMapVersion", Game.getVersion());
      this.saveProfile();
   }

   public String getWorldMapVersion() {
      return profileProperties.getProperty("worldMapVersion", "NULL");
   }
}
