package rtr.save;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.SettingsParser;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.system.gamemodetemplates.GameModeTemplateBase;

public class SaveModule extends ModuleBase {
   public static final int SAVE_BACKUPS = 8;
   private int saveTick;
   private boolean autosaving;
   private RegionalSavedGame activeRegionalSave;
   private WorldSavedGame activeWorldSave;

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      this.initLoad();
      this.initialized = true;
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
      this.loadWorldSaveData();
   }

   @Override
   protected void loadWorldMap() throws SlickException {
      this.loadWorldSaveData();
   }

   @Override
   protected void loadRegionalSaveData() throws SlickException {
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
      GameModeTemplateBase.GameMode gM = null;
      if (gM == null && Game.getCS().getGameMode() == GameModeTemplateBase.GameMode.WORLD_MAP) {
         gM = SettingsParser.getLastGameMode();
      } else {
         gM = this.activeRegionalSave.getGameMode();
      }

      if (gM != null) {
         if (!SavedGamesHandler.worldSaveExist(gM)) {
            SavedGamesHandler.createNewWorldSave(gM);
         }

         this.activeWorldSave = SavedGamesHandler.getWorldSave(gM);
      }
   }

   public void update() throws SlickException {
      if (this.autosaving) {
         this.activeRegionalSave.runAutosave();
         if (this.activeRegionalSave.getGameType() == StateBase.GameType.WORLD_MAP) {
            this.activeWorldSave.runAutosave();
         }

         this.autosaving = false;
      } else {
         this.saveTick++;
         int saveRate = 32000;
         if (SettingsParser.getSaveFrequency() == SettingsParser.AutosaveFrequency.FULL) {
            saveRate = 32000;
         } else if (SettingsParser.getSaveFrequency() == SettingsParser.AutosaveFrequency.REDUCED) {
            saveRate = 64000;
         } else if (SettingsParser.getSaveFrequency() == SettingsParser.AutosaveFrequency.MINIMAL) {
            saveRate = 128000;
         }

         if (this.map.isMapStarted() && this.saveTick > saveRate) {
            this.autosaving = true;
            this.saveTick = 0;
         }
      }
   }

   public void saveFullGame() throws SlickException {
      if (this.map.isMapStarted() || this.activeRegionalSave.getGameMode() == GameModeTemplateBase.GameMode.MAP_EDITOR) {
         this.activeRegionalSave.fullSaveSingleThreaded();
         if (this.activeRegionalSave.getGameType() == StateBase.GameType.WORLD_MAP) {
            this.activeWorldSave.fullSaveSingleThreaded();
         }
      }
   }

   public RegionalSavedGame getActiveRegionalSave() {
      return this.activeRegionalSave;
   }

   public WorldSavedGame getActiveWorldSave() {
      return this.activeWorldSave;
   }

   public void setActiveSave(RegionalSavedGame s) throws SlickException {
      this.activeRegionalSave = s;
      if (this.activeRegionalSave.getGameType() == StateBase.GameType.WORLD_MAP) {
         this.activeWorldSave = SavedGamesHandler.getWorldSave(s.getGameMode());
      }
   }

   public boolean isAutosaving() {
      return this.autosaving;
   }

   public void resetWorldMap(GameModeTemplateBase.GameMode gM) throws SlickException {
      System.gc();

      try {
         Thread.sleep(100L);
      } catch (InterruptedException var9) {
         var9.printStackTrace();
      }

      try {
         int profileSlot = Game.getActiveProfile();
         File folder = new File("profiles/profile" + profileSlot + "/saves/worldMap/WorldMaps");
         if (folder.exists()) {
            Iterator fileIntertor = FileUtils.iterateFiles(folder, null, true);

            while (fileIntertor.hasNext()) {
               File thisFile = (File)fileIntertor.next();
               if (thisFile.getName().contains(gM.getFolderName() + "-")
                  && thisFile.getName().contains(".zip")
                  && !thisFile.getName().replaceAll("[^0-9]", "").equals("")) {
                  FileUtils.forceDelete(thisFile);
               }
            }

            System.gc();

            try {
               Thread.sleep(100L);
            } catch (InterruptedException var8) {
               var8.printStackTrace();
            }

            Iterator folderIntertor = FileUtils.iterateFilesAndDirs(folder, TrueFileFilter.INSTANCE, DirectoryFileFilter.INSTANCE);

            while (folderIntertor.hasNext()) {
               File thisFolder = (File)folderIntertor.next();
               if (thisFolder.isDirectory() && thisFolder.listFiles().length == 0) {
                  FileUtils.forceDelete(thisFolder);
               }
            }
         }

         if (gM == GameModeTemplateBase.GameMode.CUSTOM) {
            try {
               folder = new File("profiles/profile" + profileSlot + "/customConfig.gameMode");
               if (folder.exists()) {
                  FileUtils.forceDelete(folder);
               }
            } catch (IOException var7) {
               var7.printStackTrace();
            }
         }
      } catch (IOException var10) {
         var10.printStackTrace();
      }

      ModuleBase.ModuleType[] var16;
      for (ModuleBase.ModuleType mT : ModuleBase.ModuleType.values()) {
         StateBase.getModule(mT).resetWorldSaveData();
      }

      this.activeWorldSave.fullSaveSingleThreaded();
      SavedGamesHandler.loadData();
   }
}
