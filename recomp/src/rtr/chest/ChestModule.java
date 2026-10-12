package rtr.chest;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ModuleBase;
import rtr.SoundModule;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.system.Game;

public class ChestModule extends ModuleBase {
   public static final byte CHEST_DAY_MULTIPLIER = 2;
   public static final byte MAXIMUM_CHESTS = 8;
   private int chestCounter;
   private int chestXP;
   public int updateDelay;
   private Chest[] chestArray;

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      if (this.chestArray == null) {
         this.chestArray = new Chest[8];
      }

      this.initLoad();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
      this.chestCounter = 0;
      this.chestXP = 0;
      this.updateDelay = 0;
      this.chestArray = new Chest[8];
   }

   @Override
   protected void newMainMenu() throws SlickException {
   }

   @Override
   protected void newPlay() throws SlickException {
      if (this.save.getActiveRegionalSave().getGameType() == StateBase.GameType.WORLD_MAP && !this.save.getActiveWorldSave().isNewGame()) {
         this.loadWorldSaveData();
      }
   }

   @Override
   protected void loadPlay() throws SlickException {
      if (this.save.getActiveRegionalSave().getGameType() == StateBase.GameType.WORLD_MAP && !this.save.getActiveWorldSave().isNewGame()) {
         this.loadWorldSaveData();
      }
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
      this.loadWorldSaveData();
   }

   @Override
   protected void loadRegionalSaveData() throws SlickException {
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
      YMLDataMap dataMap = this.save.getActiveWorldSave().getModuleSaveData(this.moduleType);
      this.chestCounter = Integer.parseInt(dataMap.getOrDefault("chestCounter", "0"));
      this.chestXP = Integer.parseInt(dataMap.getOrDefault("chestXP", "0"));

      for (int c = 0; c < this.chestArray.length; c++) {
         if (dataMap.containsMap("chest" + c)) {
            this.chestArray[c] = new Chest(dataMap.getSubMap("chest" + c));
         }
      }
   }

   @Override
   public YMLDataMap getWorldMapSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("version", Game.getVersion());
      dataMap.put("chestCounter", this.chestCounter);
      dataMap.put("chestXP", this.chestXP);

      for (int c = 0; c < this.chestArray.length; c++) {
         if (this.chestArray[c] != null) {
            dataMap.put("chest" + c, this.chestArray[c].getWorldMapProperties());
         }
      }

      return dataMap;
   }

   public void update() throws SlickException {
      if (this.map.isMapStarted() && !this.isChestsFull()) {
         this.chestXP++;
      }
   }

   public boolean isChestsFull() {
      for (int c = 0; c < this.chestArray.length; c++) {
         if (this.chestArray[c] == null) {
            return false;
         }
      }

      return true;
   }

   public void updateWorldMap() throws SlickException {
      if (this.updateDelay < 150) {
         this.updateDelay++;
      } else {
         if (this.chestXP > 100000) {
            this.chestCounter += 3000;
            this.chestXP -= 3000;
         } else if (this.chestXP > 50000) {
            this.chestCounter += 2000;
            this.chestXP -= 2000;
         } else if (this.chestXP > 25000) {
            this.chestCounter += 1000;
            this.chestXP -= 1000;
         } else if (this.chestXP > 10000) {
            this.chestCounter += 500;
            this.chestXP -= 500;
         } else if (this.chestXP > 5000) {
            this.chestCounter += 500;
            this.chestXP -= 500;
         } else if (this.chestXP > 300) {
            this.chestCounter += 300;
            this.chestXP -= 300;
         } else {
            this.chestCounter = this.chestCounter + this.chestXP;
            this.chestXP = 0;
         }

         if (this.chestXP == 0) {
            this.sound.stopChestRoll();
         } else {
            this.sound.playChestRoll();
         }

         if (this.chestCounter >= this.time.getFullDayLength() * 2) {
            this.grantChest(1);
            this.chestCounter = 0;
         }
      }

      if (this.isChestsFull()) {
         this.chestXP = 0;
         this.chestCounter = 0;
      }
   }

   public int getChestXP() {
      return this.chestXP;
   }

   public int getNextChestCounter() {
      return this.chestCounter;
   }

   public void grantChest(int baseLevel) throws SlickException {
      for (int i = 0; i < this.chestArray.length; i++) {
         if (this.chestArray[i] == null) {
            Chest c = new Chest(baseLevel);
            this.chestArray[i] = c;
            c.makeAppear();
            this.sound.playSound(SoundModule.SoundType.BELL);
            this.save.getActiveWorldSave().fullSaveSingleThreaded();
            break;
         }
      }
   }

   public void removeChest(Chest selectedChest) throws SlickException {
      for (int i = 0; i < this.chestArray.length; i++) {
         if (this.chestArray[i] == selectedChest) {
            this.chestArray[i] = null;
            this.save.getActiveWorldSave().fullSaveSingleThreaded();
            break;
         }
      }
   }

   public Chest[] getChestArray() {
      return this.chestArray;
   }

   public Chest getIntersectedChest(Rectangle mouse) {
      for (Chest c : this.chestArray) {
         if (c != null && c.intersects(mouse)) {
            return c;
         }
      }

      return null;
   }
}
