package rtr.save;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.imageout.ImageOut;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import rtr.ImageLoader;
import rtr.ModuleBase;
import rtr.console.Console;
import rtr.font.Text;
import rtr.map.MapModule;
import rtr.map.MiniMapModule;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.system.gamemodetemplates.GameModeTemplateBase;
import rtr.system.threadpool.RTRThreadPool;

public class RegionalSavedGame {
   private GameModeTemplateBase.GameMode gameMode;
   private StateBase.GameType gameType;
   private String mapFileName;
   private String mapPackFolderName;
   private String mapEditorFolder;
   private String baseSaveFolder;
   private int highestSaveNumber = 0;
   private HashMap<ModuleBase.ModuleType, Map<String, Object>> preparedModuleSaveData = new HashMap<>();
   private int[][][] preparedMapData;
   private HashMap<ModuleBase.ModuleType, YMLDataMap> loadedModuleSaveData = new HashMap<>();
   private int[][][] loadedMapData;
   private Image miniMapImagePreview;
   private boolean newGame;
   private boolean saveRunning;

   public RegionalSavedGame(GameModeTemplateBase.GameMode gM, StateBase.GameType gT, String mPFN, String mFN, boolean nG) throws SlickException {
      this(gM, gT, mPFN, mFN, nG, -1);
   }

   public RegionalSavedGame(GameModeTemplateBase.GameMode gM, StateBase.GameType gT, String mPFN, String mFN) throws SlickException {
      this(gM, gT, mPFN, mFN, false, -1);
   }

   public RegionalSavedGame(GameModeTemplateBase.GameMode gM, StateBase.GameType gT, String mPFN, String mFN, int saveNumber) throws SlickException {
      this(gM, gT, mPFN, mFN, false, saveNumber);
   }

   public RegionalSavedGame(GameModeTemplateBase.GameMode gM, StateBase.GameType gT, String mPFN, String mFN, boolean nG, int saveNumber) throws SlickException {
      this.gameMode = gM;
      this.gameType = gT;
      this.mapPackFolderName = mPFN;
      this.mapFileName = mFN;
      this.newGame = nG;
      this.baseSaveFolder = "profiles/profile"
         + Game.getActiveProfile()
         + "/saves/"
         + this.gameType.getText()
         + "/"
         + this.mapPackFolderName
         + "/"
         + this.mapFileName;
      this.mapEditorFolder = "maps/" + this.mapPackFolderName + "/" + this.mapFileName;
      if (saveNumber == -1) {
         ArrayList<File> savesFound = SavedGamesHandler.getSortedListOfSaves(this.baseSaveFolder, this.gameMode);
         if (savesFound.size() > 0) {
            this.highestSaveNumber = Integer.parseInt(savesFound.get(0).getName().replaceAll("[^0-9]", ""));
         }
      } else {
         this.highestSaveNumber = saveNumber;
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

      this.gatherMapData();
      this.writeDataToDisk(true);
      long endTotalTime = System.currentTimeMillis();
      Text.setVariableText("time", endTotalTime - startTotalTime + "ms");
      Console.out(Text.getText("console.panel.save.region.full"), false);
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

      this.gatherMapData();
      Runnable r = new Runnable() {
         @Override
         public void run() {
            RegionalSavedGame.this.writeDataToDisk(false);
         }
      };
      new Thread(r).start();
      long endTotalTime = System.currentTimeMillis();
      Text.setVariableText("time", endTotalTime - startTotalTime + "ms");
      Console.out(Text.getText("console.panel.save.region.auto.dataGather"), false);
   }

   private void writeDataToDisk(boolean writeMinimap) {
      long startTotalTimeThread = System.currentTimeMillis();
      this.saveRunning = true;

      try {
         ZipOutputStream zipOut = null;
         if (this.gameMode != GameModeTemplateBase.GameMode.MAP_EDITOR) {
            File folderCheck = new File(this.baseSaveFolder);
            if (!folderCheck.exists()) {
               folderCheck.mkdirs();
            }

            zipOut = new ZipOutputStream(new FileOutputStream(this.baseSaveFolder + "/" + this.gameMode.getFolderName() + "-NEW.zip"));
         } else {
            zipOut = new ZipOutputStream(new FileOutputStream(this.mapEditorFolder + "-NEW.zip"));
         }

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
         if (writeMinimap) {
            long startTime = System.currentTimeMillis();
            MiniMapModule miniMap = (MiniMapModule)StateBase.getModule(ModuleBase.ModuleType.MINI_MAP);
            zipOut.putNextEntry(new ZipEntry("minimap.bmp"));
            ImageOut.write(miniMap.getMiniMapFull(), "BMP", zipOut);
            long endTime = System.currentTimeMillis();
            Console.out("Saved Map Preview, took " + (endTime - startTime) + "ms", true);
         }

         long startTime = System.currentTimeMillis();

         try {
            zipOut.putNextEntry(new ZipEntry("map.array"));
            ObjectOutputStream mapDataOut = new ObjectOutputStream(zipOut);
            mapDataOut.writeObject(this.preparedMapData);
            mapDataOut.flush();
            mapDataOut.close();
         } catch (IOException var16) {
            Console.out("There was an error saving the map.", true);
         }

         long endTime = System.currentTimeMillis();
         Console.out("Saved Map Data, took " + (endTime - startTime) + "ms", true);
         zipOut.flush();
         zipOut.close();
         if (this.gameMode == GameModeTemplateBase.GameMode.MAP_EDITOR) {
            this.runSaveCleanupMapEditor();
         } else {
            this.runSaveCleanupPlay();
         }
      } catch (SlickException | IOException var17) {
         this.saveRunning = false;
         var17.printStackTrace();
      }

      long endTotalTimeThread = System.currentTimeMillis();
      Text.setVariableText("time", endTotalTimeThread - startTotalTimeThread + "ms");
      Text.setVariableText("saveNumber", this.highestSaveNumber);
      Console.out(Text.getText("console.panel.save.region.auto.dataWritten"), false);
      this.saveRunning = false;
   }

   private void gatherModuleData(ModuleBase.ModuleType mT) throws SlickException {
      long startTime = System.currentTimeMillis();
      ModuleBase m = StateBase.getModule(mT);
      Map<String, Object> dataMap = null;
      YMLDataMap var8;
      if (this.gameMode == GameModeTemplateBase.GameMode.MAP_EDITOR) {
         var8 = m.getEditorSaveData();
      } else {
         var8 = m.getRegionalSaveData();
      }

      if (var8 != null) {
         this.preparedModuleSaveData.put(mT, var8);
      }

      long endTime = System.currentTimeMillis();
      if (var8 != null) {
         Console.out("Saved " + mT.getSystemName() + " module regional data, took " + (endTime - startTime) + "ms", true);
      } else {
         Console.out("No regional data inside " + mT.getSystemName() + " module to save, took " + (endTime - startTime) + "ms", true);
      }
   }

   private void gatherMapData() throws SlickException {
      MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
      this.preparedMapData = new int[256][256][12];
      int[][][] originalMapData = map.getMap();

      for (int x = 0; x < this.preparedMapData.length; x++) {
         for (int y = 0; y < this.preparedMapData[x].length; y++) {
            for (int l = 0; l < this.preparedMapData[x][y].length; l++) {
               this.preparedMapData[x][y][l] = originalMapData[x][y][l];
            }
         }
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

   protected void runSaveCleanupMapEditor() {
      long startTime = System.currentTimeMillis();
      File oldFile = new File(this.mapEditorFolder + ".zip");
      if (oldFile.exists()) {
         while (!oldFile.renameTo(new File(this.mapEditorFolder + "-BAK.zip"))) {
            System.out.println("Failed to create backup. Running garbage collection and retrying.");
            System.gc();

            try {
               Thread.sleep(100L);
            } catch (InterruptedException var7) {
               var7.printStackTrace();
            }
         }
      }

      File newFile = new File(this.mapEditorFolder + "-NEW.zip");
      if (newFile.exists() && newFile.renameTo(new File(this.mapEditorFolder + ".zip"))) {
         File deleteFile = new File(this.mapEditorFolder + "-BAK.zip");
         if (deleteFile.exists()) {
            deleteFile.delete();
         }
      }

      long endTime = System.currentTimeMillis();
      Console.out("Save Clean Up, took " + (endTime - startTime) + "ms", true);
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

   public StateBase.GameType getGameType() {
      return this.gameType;
   }

   public String getMapFileName() {
      return this.mapFileName;
   }

   public String getMapPackFolderName() {
      return this.mapPackFolderName;
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

   public YMLDataMap getAndUnloadModuleSaveData(ModuleBase.ModuleType mT) {
      if (!this.loadedModuleSaveData.containsKey(mT)) {
         String rawSaveData = this.getRawModuleSavaData(mT);
         if (rawSaveData != null) {
            YMLDataMap rawDataMap = new Yaml().loadAs(rawSaveData, YMLDataMap.class);
            rawDataMap.setName(mT.getSystemName());
            this.loadedModuleSaveData.put(mT, rawDataMap);
         }
      }

      YMLDataMap mapOut = this.loadedModuleSaveData.get(mT);
      this.loadedModuleSaveData.remove(mT);
      return mapOut;
   }

   private String getRawModuleSavaData(ModuleBase.ModuleType mT) {
      String rawDataOut = null;

      try {
         File zipFile = null;
         if (!this.newGame && this.gameMode != GameModeTemplateBase.GameMode.MAP_EDITOR) {
            zipFile = new File(this.baseSaveFolder + "/" + this.gameMode.getFolderName() + "-" + this.highestSaveNumber + ".zip");
         } else {
            zipFile = new File(this.mapEditorFolder + ".zip");
         }

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
         var11.printStackTrace();
         if (!this.newGame && this.gameMode != GameModeTemplateBase.GameMode.MAP_EDITOR) {
            Console.out("Failed to create " + mT.getSystemName() + " data for: " + this.baseSaveFolder + "!", true);
         } else {
            Console.out("Failed to create " + mT.getSystemName() + " data for: " + this.mapEditorFolder + "!", true);
         }
      }

      return rawDataOut;
   }

   public int[][][] getMapData() {
      if (this.loadedMapData == null) {
         try {
            File zipFile = null;
            if (!this.newGame && this.gameMode != GameModeTemplateBase.GameMode.MAP_EDITOR) {
               zipFile = new File(this.baseSaveFolder + "/" + this.gameMode.getFolderName() + "-" + this.highestSaveNumber + ".zip");
            } else {
               zipFile = new File(this.mapEditorFolder + ".zip");
            }

            ZipFile zip = new ZipFile(zipFile);
            ZipEntry zipEntry = zip.getEntry("map.array");
            InputStream is = zip.getInputStream(zipEntry);
            ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(is));

            try {
               this.loadedMapData = (int[][][])in.readObject();
               in.close();
            } catch (ClassNotFoundException var7) {
               Console.out("There was an error loading the map array for: " + this.mapFileName + "!", true);
               in.close();
            }

            zip.close();
         } catch (Exception var8) {
            Console.out("There was an error loading the map array for: " + this.mapFileName + "!", true);
         }
      }

      return this.loadedMapData;
   }

   public Image getMiniMapImagePreview() throws SlickException {
      if (this.miniMapImagePreview == null) {
         File zipFile = null;
         if (!this.newGame && this.gameMode != GameModeTemplateBase.GameMode.MAP_EDITOR) {
            zipFile = new File(this.baseSaveFolder + "/" + this.gameMode.getFolderName() + "-" + this.highestSaveNumber + ".zip");
         } else {
            zipFile = new File(this.mapEditorFolder + ".zip");
         }

         try {
            ZipFile zip = new ZipFile(zipFile);
            ZipEntry zipEntry = zip.getEntry("minimap.bmp");
            if (zipEntry == null) {
               zipFile = new File(this.mapEditorFolder + ".zip");
               zip.close();
               zip = new ZipFile(zipFile);
               zipEntry = zip.getEntry("minimap.bmp");
               if (zipEntry == null) {
                  this.miniMapImagePreview = ImageLoader.getImage("res/ungeneratedMap.bmp");
                  zip.close();
                  return this.miniMapImagePreview;
               }
            }

            InputStream minimapInputStream = zip.getInputStream(zipEntry);
            this.miniMapImagePreview = new Image(minimapInputStream, minimapInputStream.toString(), false);
            zip.close();
            minimapInputStream.close();
         } catch (IOException var5) {
            var5.printStackTrace();
         }
      }

      return this.miniMapImagePreview;
   }

   public boolean isNewGame() {
      return this.newGame;
   }
}
