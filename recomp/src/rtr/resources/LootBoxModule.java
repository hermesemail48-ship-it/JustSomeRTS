package rtr.resources;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.SoundModule;
import rtr.goal.GoalModule;
import rtr.gui.states.BrushLoader;
import rtr.lighting.Light;
import rtr.map.MapData;
import rtr.map.MapModule;
import rtr.map.MapTilesLoader;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.system.gamemodetemplates.GameModeTemplateBase;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class LootBoxModule extends ModuleBase {
   private byte[][] lootBoxLocations;
   private Light[][] lootBoxLights;
   private ArrayList<OrderedPair> lootBoxLocationsArray = new ArrayList<>();
   private ArrayList<OrderedPair> lootBoxLocationsArrayRemove = new ArrayList<>();
   private int maximumBoxes;
   private int boxUncoverChance;
   private int keyChance;

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      this.lootBoxLocations = new byte[256][256];
      this.lootBoxLights = new Light[256][256];
      this.initLoad();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
      this.lootBoxLocations = new byte[256][256];
      this.lootBoxLights = new Light[256][256];
      this.lootBoxLocationsArray.clear();
      this.lootBoxLocationsArrayRemove.clear();
   }

   @Override
   protected void newMainMenu() throws SlickException {
   }

   @Override
   protected void newPlay() throws SlickException {
      int randomStartBoxes = Utilities.randomInt(6);

      for (int x = 0; x < randomStartBoxes; x++) {
         this.createRandomLootbox();
      }

      GameModeTemplateBase template = this.save.getActiveRegionalSave().getGameMode().getTemplate();
      this.maximumBoxes = template.getMaximumLootBoxes();
      this.boxUncoverChance = template.getLootBoxUncoverChance();
      this.keyChance = template.getLootBoxKeyChance();
   }

   @Override
   protected void loadPlay() throws SlickException {
      this.loadRegionalSaveData();
      GameModeTemplateBase template = this.save.getActiveRegionalSave().getGameMode().getTemplate();
      this.maximumBoxes = template.getMaximumLootBoxes();
      this.boxUncoverChance = template.getLootBoxUncoverChance();
      this.keyChance = template.getLootBoxKeyChance();
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
      if (dataMap.containsKey("lootBoxLocationsArray")) {
         this.lootBoxLocationsArray = dataMap.unpackCoordinatesList("lootBoxLocationsArray");

         for (OrderedPair o : this.lootBoxLocationsArray) {
            this.lootBoxLocations[o.getX()][o.getY()] = 1;
            this.lootBoxLights[o.getX()][o.getY()] = new Light(o.getX(), o.getY(), (byte)2, (byte)10, false);
         }
      }
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
   }

   @Override
   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("version", Game.getVersion());
      if (this.lootBoxLocationsArray.size() > 0) {
         dataMap.put("lootBoxLocationsArray", dataMap.packCoordinatesList(this.lootBoxLocationsArray));
      }

      return dataMap;
   }

   public void update(int mouseX, int mouseY) throws SlickException {
      for (OrderedPair o : this.lootBoxLocationsArray) {
         int posX = o.getX() * 16 + Utilities.randomInt(16);
         int posY = o.getY() * 16 + Utilities.randomInt(16);
         if (this.map.isInView(posX, posY, 10)) {
            if (Utilities.randomInt(3) == 0) {
               this.particle
                  .newParticleGroup(
                     posX, posY, ParticleModule.ParticleGroup.SPARKLES, ParticleModule.ParticleSet.MAGIC_PURPLE, ParticleModule.ParticleSet.MAGIC_PURPLE
                  );
            }

            if (Utilities.randomInt(10) == 0) {
               this.particle.newParticle(posX, posY, ParticleModule.ParticleType.FLASH_SPECK, ParticleModule.ParticleSet.MAGIC_PURPLE);
            }

            this.particle.newParticle(posX, posY, ParticleModule.ParticleType.FIRE_SPECK, ParticleModule.ParticleSet.FIRE_BLUE);
            posX = o.getX() * 16 + 8 + Utilities.randomInt(-1, 1);
            posY = o.getY() * 16 + 8 + Utilities.randomInt(-1, 1);
            if (Utilities.randomInt(20) == 0) {
               for (int x = 0; x < 2; x++) {
                  this.projectile.newEnergyRing(posX, posY, 8, ParticleModule.ParticleSet.DARK_GRAY, ParticleModule.ParticleSet.MAGIC_BLUE);
               }
            }
         }

         if (Utilities.randomInt(250) == 0) {
            this.mob.tryConfuseInAreaIfIdle(o.getX(), o.getY(), 4, true, 2);
            this.mob.influenceFaithInArea(o.getX(), o.getY(), 4, 1, true);
         }
      }

      this.lootBoxLocationsArrayRemove.clear();

      for (OrderedPair o : this.lootBoxLocationsArray) {
         if (Utilities.coordinatesWithin(mouseX / 16, mouseY / 16, o.getX(), o.getY(), 12)) {
            int posXx = o.getX() * 16 + 8 + Utilities.randomInt(-1, 1);
            int posYx = o.getY() * 16 + 8 + Utilities.randomInt(-1, 1);
            if (Utilities.coordinatesWithin(mouseX / 16, mouseY / 16, o.getX(), o.getY(), 6)) {
               for (int x = 0; x < 5; x++) {
                  if (Utilities.randomInt(3) == 0 && Utilities.coordinatesWithin(mouseX / 16, mouseY / 16, o.getX(), o.getY(), 6 - x)) {
                     this.particle
                        .newParticleGroup(
                           mouseX + Utilities.randomInt(-1, 1),
                           mouseY + Utilities.randomInt(-1, 1),
                           (int)Math.toDegrees(Utilities.calculateAngle(mouseX, mouseY, posXx, posYx)),
                           ParticleModule.ParticleGroup.MAGIC_SMOKE,
                           ParticleModule.ParticleSet.FIRE_PURPLE,
                           ParticleModule.ParticleSet.FIRE_PURPLE
                        );
                  }
               }
            } else if (Utilities.randomInt(3) == 0) {
               this.particle
                  .newParticleGroup(
                     mouseX + Utilities.randomInt(-1, 1),
                     mouseY + Utilities.randomInt(-1, 1),
                     (int)Math.toDegrees(Utilities.calculateAngle(mouseX, mouseY, posXx, posYx)),
                     ParticleModule.ParticleGroup.MAGIC_SMOKE,
                     ParticleModule.ParticleSet.FIRE_RED,
                     ParticleModule.ParticleSet.FIRE_RED
                  );
            }
         }

         if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[o.getX()][o.getY()] > 0
            || this.object.getBuildingAtLocation(o) != null
            || this.corruption.isTileCorrupted(o.getX(), o.getY())
            || Utilities.randomInt(1000000) == 0) {
            this.lootBoxLocationsArrayRemove.add(o);
         }
      }

      for (OrderedPair o : this.lootBoxLocationsArrayRemove) {
         this.lootBoxLocations[o.getX()][o.getY()] = 0;
         this.lootBoxLights[o.getX()][o.getY()] = null;
         this.lootBoxLocationsArray.remove(o);
      }

      if (this.maximumBoxes != 0 && this.lootBoxLocationsArray.size() < this.maximumBoxes && Utilities.randomInt(150000) == 0) {
         this.createRandomLootbox();
      }
   }

   private void createRandomLootbox() {
      int attempt = 0;
      int randomX = Utilities.randomInt(256);
      int randomY = Utilities.randomInt(256);

      while (
         this.map.getTileId(randomX, randomY, 6) > 0
            || this.map.getTileId(randomX, randomY, 9) > 0
            || this.lootBoxLocations[randomX][randomY] > 0
            || this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[randomX][randomY] > 0
            || this.object.getBuildingAtLocation(randomX, randomY) != null
            || this.corruption.isTileCorrupted(randomX, randomY)
      ) {
         randomX = Utilities.randomInt(256);
         randomY = Utilities.randomInt(256);
         if (++attempt > 1000) {
            break;
         }
      }

      this.lootBoxLocations[randomX][randomY] = 1;
      this.lootBoxLights[randomX][randomY] = new Light(randomX, randomY, (byte)2, (byte)10, false);
      this.lootBoxLocationsArray.add(OrderedPair.getOrderedPair(randomX, randomY));
   }

   public void attemptLootBoxUncover(int tileX, int tileY) {
      if (this.boxUncoverChance != 0
         && this.lootBoxLocations[tileX][tileY] == 0
         && !this.corruption.isTileCorrupted(tileX, tileY)
         && Utilities.randomInt(this.boxUncoverChance) == 0) {
         this.lootBoxLocations[tileX][tileY] = 1;
         this.lootBoxLights[tileX][tileY] = new Light(tileX, tileY, (byte)2, (byte)10, false);
         this.lootBoxLocationsArray.add(OrderedPair.getOrderedPair(tileX, tileY));
      }
   }

   public void pokeLootBoxHidingSpot(int tileX, int tileY) throws SlickException {
      if (!Utilities.isOutOfBoundsOfUsableMap(tileX, tileY)) {
         if (this.lootBoxLocations[tileX][tileY] == 1) {
            int posX = tileX * 16 + 8 + Utilities.randomInt(-2, 2);
            int posY = tileY * 16 + 8 + Utilities.randomInt(-2, 2);
            if (Utilities.randomInt(6) == 0) {
               this.projectile.newExpandingFireRing(posX, posY, 128, 1.0F, ParticleModule.ParticleSet.MAGIC_GREEN);

               for (int x = 0; x < 50; x++) {
                  this.projectile
                     .newGroundStatic(posX + Utilities.randomInt(-16, 16), posY + Utilities.randomInt(-16, 16), ParticleModule.ParticleSet.MAGIC_BLUE);
               }

               if (this.keyChance == 0 || Utilities.randomInt(this.keyChance) != 0) {
                  this.map.erase(tileX, tileY, MapModule.EraseMode.TOPOGRAPHY, 1, BrushLoader.BrushType.SQUARE);
                  this.map.placeObject(tileX, tileY, MapTilesLoader.TileSet.LOOT_BOX, ObjectBase.ObjectSubType.BUILT, true, true);
                  this.lighting.addShortTermLight(tileX, tileY, (byte)16, (byte)16, 64);
               } else if (this.resource.isResourcesOnGround(tileX, tileY)) {
                  OrderedPair o = this.resource.findOpenSpace(tileX, tileY, MapData.BlockMapGroup.STANDARD);
                  this.resource
                     .createResourceOnGround(ResourceModule.ResourceType.SUSPICICIOUS_KEY, ResourceModule.ResourceColorSet.DEFAULT, o.getX(), o.getY());
               } else {
                  this.resource.createResourceOnGround(ResourceModule.ResourceType.SUSPICICIOUS_KEY, ResourceModule.ResourceColorSet.DEFAULT, tileX, tileY);
               }

               this.goal.incrementGoal(GoalModule.GoalType.BURIED_TREASURE);
               this.mob.tryConfuseInArea(tileX, tileY, 12, true, 2);
               this.mob.tryPanicInArea(tileX, tileY, 8, true, 2);
               this.mob.influenceFaithInArea(tileX, tileY, 16, 2, 8, true);
               this.lootBoxLocations[tileX][tileY] = 0;
               this.lootBoxLights[tileX][tileY] = null;
               this.lootBoxLocationsArray.remove(OrderedPair.getOrderedPair(tileX, tileY));

               for (int x = 0; x < 5; x++) {
                  this.font.newFloater("iconConfused", posX + Utilities.randomInt(-8, 8), posY + Utilities.randomInt(-8, 8) - 10);
               }

               for (int x = 0; x < 5; x++) {
                  this.font.newFloater("iconExcite", posX + Utilities.randomInt(-8, 8), posY + Utilities.randomInt(-8, 8) - 10);
               }

               this.sound.playSound(SoundModule.SoundType.HOLY_SOFT, posX, posY);
            } else {
               for (int x = 0; x < 5; x++) {
                  this.projectile.newExpandingFireRing(posX, posY, 24, 3.0F, ParticleModule.ParticleSet.MAGIC_BLUE);
               }

               if (Utilities.randomInt(4) == 0) {
                  this.font.newFloater("iconExcite", posX, posY - 10);
               } else {
                  this.font.newFloater("iconConfused", posX, posY - 10);
               }

               this.mob.tryConfuseInAreaIfIdle(tileX, tileY, 4, true, 2);
               this.mob.influenceFaithInArea(tileX, tileY, 4, 2, 8, true);
               this.sound.playSound(SoundModule.SoundType.MAGIC_CLICK, posX, posY);
            }
         }
      }
   }

   public Light[][] getLootBoxLights() {
      return this.lootBoxLights;
   }

   public ArrayList<OrderedPair> getLootBoxLocationsArray() {
      return this.lootBoxLocationsArray;
   }
}
