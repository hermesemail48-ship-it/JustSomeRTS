package rtr.mobs.essence;

import java.util.ArrayList;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.objects.ObjectBase;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.utilities.Utilities;

public class EssenceModule extends ModuleBase {
   private ArrayList<EssenceBase> essenceArray = new ArrayList<>();

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      this.requiresSync = true;
      this.initLoad();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
      this.essenceArray.clear();
   }

   @Override
   protected void newMainMenu() throws SlickException {
   }

   @Override
   protected void newPlay() throws SlickException {
   }

   @Override
   protected void loadPlay() throws SlickException {
      this.loadRegionalSaveData();
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
      YMLDataMap dataMap = this.save.getActiveRegionalSave().getAndUnloadModuleSaveData(this.moduleType);

      for (int count = 0; dataMap.containsKey("essence" + count); count++) {
         this.loadEssence(dataMap.getSubMap("essence" + count));
      }
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
   }

   @Override
   public void initLoadSync() {
      this.synced = true;

      for (int e = 0; e < this.essenceArray.size(); e++) {
         this.essenceArray.get(e).loadSync();
      }
   }

   @Override
   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("version", Game.getVersion());

      for (int i = 0; i < this.essenceArray.size(); i++) {
         dataMap.put("essence" + i, this.essenceArray.get(i).getRegionalSaveData());
      }

      return dataMap;
   }

   public void update() throws SlickException {
      for (int e = 0; e < this.essenceArray.size(); e++) {
         this.essenceArray.get(e).update();
      }
   }

   public void render(Graphics g) throws SlickException {
      g.glBeginQuads();

      for (int e = 0; e < this.essenceArray.size(); e++) {
         this.essenceArray.get(e).render(g);
      }

      g.glEnd();
   }

   public void removeEssence(EssenceBase e) {
      this.essenceArray.remove(e);
   }

   public void convertObjectEnergyToEssence(float x, float y, int energyAmount, boolean disableCooldown) throws SlickException {
      int randomDivisor = Utilities.randomInt(8) + 1;

      while (energyAmount % randomDivisor != 0) {
         randomDivisor = Utilities.randomInt(8) + 1;
      }

      int energyPer = energyAmount / randomDivisor;
      int amount = randomDivisor;

      for (int a = 0; a < amount; a++) {
         this.essenceArray.add(new EssenceBase(x, y, energyPer, disableCooldown));
      }
   }

   public void convertObjectEnergyToEssence(float x, float y, int energyAmount, ObjectBase target) throws SlickException {
      int randomDivisor = Utilities.randomInt(8) + 1;

      while (energyAmount % randomDivisor != 0) {
         randomDivisor = Utilities.randomInt(8) + 1;
      }

      int energyPer = energyAmount / randomDivisor;
      int amount = randomDivisor;

      for (int a = 0; a < amount; a++) {
         this.essenceArray.add(new EssenceBase(x, y, energyPer, target));
      }
   }

   public void newEssence(float x, float y, int energyPer, int amount) {
      for (int a = 0; a < amount; a++) {
         this.essenceArray.add(new EssenceBase(x, y, energyPer, false));
      }

      this.stats.increaseEssenceGeneratedThisPeriod(amount * energyPer);
   }

   public void loadEssence(YMLDataMap dataMap) {
      this.essenceArray.add(new EssenceBase(dataMap));
   }

   public ArrayList<EssenceBase> getEssenceArray() {
      return this.essenceArray;
   }
}
