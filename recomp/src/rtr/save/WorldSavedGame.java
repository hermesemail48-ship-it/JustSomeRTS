package rtr.save;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.input.CharSequenceInputStream;
import org.newdawn.slick.SlickException;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import rtr.ModuleBase;
import rtr.console.Console;
import rtr.font.Text;
import rtr.resources.ResourceModule;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.system.gamemodetemplates.GameModeTemplateBase;
import rtr.system.threadpool.RTRThreadPool;

public class WorldSavedGame {
   private GameModeTemplateBase.GameMode gameMode;
   private String baseSaveFolder;
   private int highestSaveNumber = 0;
   private HashMap<ModuleBase.ModuleType, Map<String, Object>> preparedModuleSaveData = new HashMap<>();
   private HashMap<ModuleBase.ModuleType, YMLDataMap> loadedModuleSaveData = new HashMap<>();
   private boolean newGame;
   private boolean saveRunning;

   public WorldSavedGame(GameModeTemplateBase.GameMode gM) throws SlickException {
      this(gM, false, -1);
   }

   public WorldSavedGame(GameModeTemplateBase.GameMode gM, boolean nG) throws SlickException {
      this(gM, nG, -1);
   }

   public WorldSavedGame(GameModeTemplateBase.GameMode gM, int saveNumber) throws SlickException {
      this(gM, false, -1);
   }

   public WorldSavedGame(GameModeTemplateBase.GameMode gM, boolean nG, int saveNumber) throws SlickException {
      this.gameMode = gM;
      this.newGame = nG;
      this.baseSaveFolder = "profiles/profile" + Game.getActiveProfile() + "/saves/worldMap/worldSaveData";
      if (saveNumber == -1) {
         ArrayList<File> savesFound = SavedGamesHandler.getSortedListOfSaves(this.baseSaveFolder, this.gameMode);
         if (savesFound.size() > 0) {
            this.highestSaveNumber = Integer.parseInt(savesFound.get(0).getName().replaceAll("[^0-9]", ""));
         }
      } else {
         this.highestSaveNumber = saveNumber;
      }

      if (this.newGame) {
         System.out.println("World Map provisioner stock missing, regenerating.");
         ResourceModule resource = (ResourceModule)StateBase.getModule(ModuleBase.ModuleType.RESOURCE);
         resource.createInitialProvisionerStock();
      }
   }

   public void fullSaveSingleThreaded() throws SlickException {
      long startTotalTime = System.currentTimeMillis();
      if (this.gameMode != GameModeTemplateBase.GameMode.MAP_EDITOR) {
         while (this.saveRunning || RTRThreadPool.getPathRequests().size() > 0) {
            try {
               Thread.sleep(10L);
            } catch (InterruptedException var7) {
               var7.printStackTrace();
            }
         }
      }

      ModuleBase.ModuleType[] var6;
      for (ModuleBase.ModuleType mT : ModuleBase.ModuleType.values()) {
         this.gatherModuleData(mT);
      }

      this.writeDataToDisk(true);
      long endTotalTime = System.currentTimeMillis();
      Text.setVariableText("time", endTotalTime - startTotalTime + "ms");
      Console.out(Text.getText("console.panel.save.world.full"), false);
   }

   public void runAutosave() throws SlickException {
      while (this.saveRunning || RTRThreadPool.getPathRequests().size() > 0) {
         try {
            Thread.sleep(10L);
         } catch (InterruptedException var7) {
            var7.printStackTrace();
         }
      }

      long startTotalTime = System.currentTimeMillis();

      ModuleBase.ModuleType[] var6;
      for (ModuleBase.ModuleType mT : ModuleBase.ModuleType.values()) {
         this.gatherModuleData(mT);
      }

      Runnable r = new Runnable() {
         @Override
         public void run() {
            WorldSavedGame.this.writeDataToDisk(false);
         }
      };
      new Thread(r).start();
      long endTotalTime = System.currentTimeMillis();
      Text.setVariableText("time", endTotalTime - startTotalTime + "ms");
      Console.out(Text.getText("console.panel.save.world.auto.dataGather"), false);
   }

   private void writeDataToDisk(boolean writeMinimap) {
      long startTotalTimeThread = System.currentTimeMillis();
      this.saveRunning = true;

      try {
         ZipOutputStream zipOut = null;
         File folderCheck = new File(this.baseSaveFolder);
         if (!folderCheck.exists()) {
            folderCheck.mkdirs();
         }

         zipOut = new ZipOutputStream(new FileOutputStream(this.baseSaveFolder + "/" + this.gameMode.getFolderName() + "-NEW.zip"));

         for (ModuleBase.ModuleType mT : this.preparedModuleSaveData.keySet()) {
            Map<String, Object> dataMap = this.preparedModuleSaveData.get(mT);
            if (dataMap != null) {
               String parsedFileName = mT.getSystemName().replaceAll(" ", "");
               char[] c = parsedFileName.toCharArray();
               c[0] = Character.toLowerCase(c[0]);
               parsedFileName = new String(c);
               DumperOptions options = new DumperOptions();
               options.setPrettyFlow(true);
               options.setDefaultFlowStyle(FlowStyle.BLOCK);
               Yaml yaml = new Yaml(options);
               StringWriter writer = new StringWriter();
               yaml.dump(dataMap, writer);
               CharSequenceInputStream writerInputStream = new CharSequenceInputStream(writer.toString(), StandardCharsets.UTF_8);
               zipOut.putNextEntry(new ZipEntry(parsedFileName + ".yml"));
               byte[] b = new byte[1024];

               int count;
               while ((count = writerInputStream.read(b)) > 0) {
                  zipOut.write(b, 0, count);
               }

               writerInputStream.close();
            }
         }

         this.preparedModuleSaveData.clear();
         zipOut.flush();
         zipOut.close();
         this.runSaveCleanupPlay();
      } catch (IOException var17) {
         this.saveRunning = false;
         var17.printStackTrace();
      }

      long endTotalTimeThread = System.currentTimeMillis();
      Text.setVariableText("time", endTotalTimeThread - startTotalTimeThread + "ms");
      Text.setVariableText("saveNumber", this.highestSaveNumber);
      Console.out(Text.getText("console.panel.save.world.auto.dataWritten"), false);
      this.saveRunning = false;
   }

   private void gatherModuleData(ModuleBase.ModuleType mT) throws SlickException {
      long startTime = System.currentTimeMillis();
      ModuleBase m = StateBase.getModule(mT);
      Map<String, Object> dataMap = m.getWorldMapSaveData();
      if (dataMap != null) {
         this.preparedModuleSaveData.put(mT, dataMap);
      }

      long endTime = System.currentTimeMillis();
      if (dataMap != null) {
         Console.out("Saved " + mT.getSystemName() + " module world data, took " + (endTime - startTime) + "ms", true);
      } else {
         Console.out("No world data inside " + mT.getSystemName() + " module to save, took " + (endTime - startTime) + "ms", true);
      }
   }

   protected void runSaveCleanupPlay() {
      long startTime = System.currentTimeMillis();
      File newFile = new File(this.baseSaveFolder + "/" + this.gameMode.getFolderName() + "-NEW.zip");
      if (newFile.exists()
         && !newFile.renameTo(new File(this.baseSaveFolder + "/" + this.gameMode.getFolderName() + "-" + (this.highestSaveNumber + 1) + ".zip"))) {
         System.out.println("Failed to rename save. Running garbage collection and retrying.");
         System.gc();

         try {
            Thread.sleep(100L);
         } catch (InterruptedException var8) {
            var8.printStackTrace();
         }

         while (!newFile.renameTo(new File(this.baseSaveFolder + "/" + this.gameMode.getFolderName() + "-" + (this.highestSaveNumber + 1) + ".zip"))) {
            this.highestSaveNumber++;
            System.out.println("Failed to rename save again. Incrementing save slot number to " + this.highestSaveNumber + " and retrying.");
            System.gc();

            try {
               Thread.sleep(20L);
            } catch (InterruptedException var7) {
               var7.printStackTrace();
            }
         }
      }

      ArrayList<File> savesFound = SavedGamesHandler.getSortedListOfSaves(this.baseSaveFolder, this.gameMode);
      if (savesFound.size() != 0) {
         this.highestSaveNumber = Integer.parseInt(savesFound.get(0).getName().replaceAll("[^0-9]", ""));
         if (savesFound.size() > 8) {
            for (int x = 8; x < savesFound.size(); x++) {
               savesFound.get(x).delete();
            }
         }

         long endTime = System.currentTimeMillis();
         Console.out("Save Clean Up, took " + (endTime - startTime) + "ms", true);
      }
   }

   public void deleteSaveData() throws SlickException {
      ArrayList<File> savesFound = SavedGamesHandler.getSortedListOfSaves(this.baseSaveFolder, this.gameMode);

      for (int x = 0; x < savesFound.size(); x++) {
         savesFound.get(x).delete();
      }
   }

   public GameModeTemplateBase.GameMode getGameMode() {
      return this.gameMode;
   }

   public String getBaseSaveFolder() {
      return this.baseSaveFolder;
   }

   public YMLDataMap getModuleSaveData(ModuleBase.ModuleType mT) {
      if (!this.loadedModuleSaveData.containsKey(mT)) {
         String rawSaveData = this.getRawModuleSavaData(mT);
         if (rawSaveData != null) {
            YMLDataMap rawDataMap = new Yaml().loadAs(rawSaveData, YMLDataMap.class);
            rawDataMap.setName(mT.getSystemName());
            this.loadedModuleSaveData.put(mT, rawDataMap);
         }
      }

      return this.loadedModuleSaveData.get(mT);
   }

   private String getRawModuleSavaData(ModuleBase.ModuleType mT) {
      String rawDataOut = null;

      try {
         File zipFile = new File(this.baseSaveFolder + "/" + this.gameMode.getFolderName() + "-" + this.highestSaveNumber + ".zip");
         ZipFile zip = new ZipFile(zipFile);
         ZipEntry zipEntry = zip.getEntry(mT.getSystemName() + ".yml");
         if (zipEntry == null) {
            zip.close();
            return null;
         }

         InputStream is = zip.getInputStream(zipEntry);
         InputStreamReader in = new InputStreamReader(is, Charset.forName("UTF-8"));
         char[] buffer = new char[4096];
         StringBuilder sb = new StringBuilder();

         int len;
         while ((len = in.read(buffer)) > 0) {
            sb.append(buffer, 0, len);
         }

         rawDataOut = sb.toString();
         is.close();
         zip.close();
      } catch (Exception var11) {
         Console.out("Failed to create " + mT.getSystemName() + " data for: " + this.baseSaveFolder + "!", true);
         var11.printStackTrace();
      }

      return rawDataOut;
   }

   public boolean isNewGame() {
      return this.newGame;
   }
}
