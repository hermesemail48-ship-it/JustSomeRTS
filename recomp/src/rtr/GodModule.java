package rtr;

import java.util.EnumMap;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.utilities.Utilities;

public class GodModule extends ModuleBase {
   private double godXP;
   private EnumMap<GodModule.GodXPType, Double> godXPByType = new EnumMap<>(GodModule.GodXPType.class);
   private double godXPWorldMapCount;
   public int updateDelay;
   private static final int PASSIVE_UPDATE_CHECK_RATE = 5000;
   private int passiveUpdateCheckTick;

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);

      GodModule.GodXPType[] var6;
      for (GodModule.GodXPType g : GodModule.GodXPType.values()) {
         this.godXPByType.put(g, 0.0);
      }

      this.initLoad();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
      this.godXP = 0.0;
      this.godXPWorldMapCount = 0.0;
      this.updateDelay = 0;
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
      this.godXP = Double.parseDouble(dataMap.getOrDefault("godXP", 0.0));

      GodModule.GodXPType[] var5;
      for (GodModule.GodXPType g : GodModule.GodXPType.values()) {
         this.godXPByType.put(g, Double.parseDouble(dataMap.getOrDefault(g.toString(), 0.0)));
      }

      this.godXPWorldMapCount = Double.parseDouble(dataMap.getOrDefault("godXPWorldMapCount", 0.0));
   }

   @Override
   public YMLDataMap getWorldMapSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("version", Game.getVersion());
      dataMap.put("godXP", this.godXP);

      GodModule.GodXPType[] var5;
      for (GodModule.GodXPType g : GodModule.GodXPType.values()) {
         dataMap.put(g.toString(), this.godXPByType.get(g));
      }

      dataMap.put("godXPWorldMapCount", this.godXPWorldMapCount);
      return dataMap;
   }

   public void update() throws SlickException {
      this.passiveUpdateCheckTick++;
      if (this.passiveUpdateCheckTick > 5000) {
         double totalVillagers = this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false).size();
         this.increaseGodXP(totalVillagers * 0.001, GodModule.GodXPType.SURVIVING_VILLAGERS);
         double totalBuildings = this.object.getObjectMasterBuildingPriorityList().size();
         this.increaseGodXP(totalBuildings * 0.001, GodModule.GodXPType.VILLAGE_SIZE);
         this.passiveUpdateCheckTick = 0;
      }
   }

   public void updateWorldMap() throws SlickException {
      if (this.chest.getChestXP() <= 10) {
         if (this.updateDelay < 100) {
            this.updateDelay++;
         } else {
            if (Math.abs(this.godXPWorldMapCount - this.godXP) > 2.0 && Utilities.randomInt(3) == 0) {
               this.sound.playSound(SoundModule.SoundType.ESSENCE_SUCK, Utilities.randomInt(512), Utilities.randomInt(512));
            }

            if (this.godXPWorldMapCount + 10.0 < this.godXP) {
               this.godXPWorldMapCount += 10.0;
            }

            if (this.godXPWorldMapCount - 10.0 > this.godXP) {
               this.godXPWorldMapCount -= 10.0;
            }

            if (this.godXPWorldMapCount + 5.0 < this.godXP) {
               this.godXPWorldMapCount += 5.0;
            }

            if (this.godXPWorldMapCount - 5.0 > this.godXP) {
               this.godXPWorldMapCount -= 5.0;
            }

            if (this.godXPWorldMapCount + 1.0 < this.godXP) {
               this.godXPWorldMapCount++;
            }

            if (this.godXPWorldMapCount - 1.0 > this.godXP) {
               this.godXPWorldMapCount--;
            }

            if (this.godXPWorldMapCount + 0.5 < this.godXP) {
               this.godXPWorldMapCount += 0.5;
            }

            if (this.godXPWorldMapCount - 0.5 > this.godXP) {
               this.godXPWorldMapCount -= 0.5;
            }

            if (this.godXPWorldMapCount + 0.1 < this.godXP) {
               this.godXPWorldMapCount += 0.1;
            }

            if (this.godXPWorldMapCount - 0.1 > this.godXP) {
               this.godXPWorldMapCount -= 0.1;
            }

            if (this.godXPWorldMapCount + 0.01 < this.godXP) {
               this.godXPWorldMapCount += 0.01;
            }

            if (this.godXPWorldMapCount + 0.01 > this.godXP) {
               this.godXPWorldMapCount -= 0.01;
            }

            if (Math.abs(this.godXPWorldMapCount - this.godXP) < 0.01) {
               this.godXPWorldMapCount = this.godXP;
            }

            EnumMap<GodModule.GodXPType, Double> godXPByType = this.god.getGodXPByType();

            for (GodModule.GodXPType g : godXPByType.keySet()) {
               if (godXPByType.get(g) > 0.0) {
                  if (godXPByType.get(g) > 10.0) {
                     godXPByType.put(g, godXPByType.get(g) - 10.0);
                  }

                  if (godXPByType.get(g) > 5.0) {
                     godXPByType.put(g, godXPByType.get(g) - 5.0);
                  }

                  if (godXPByType.get(g) > 1.0) {
                     godXPByType.put(g, godXPByType.get(g) - 1.0);
                  }

                  if (godXPByType.get(g) > 0.5) {
                     godXPByType.put(g, godXPByType.get(g) - 0.5);
                  }

                  if (godXPByType.get(g) > 0.1) {
                     godXPByType.put(g, godXPByType.get(g) - 0.1);
                  }

                  godXPByType.put(g, godXPByType.get(g) - 0.01);
                  break;
               }

               godXPByType.put(g, 0.0);
            }
         }
      }
   }

   public void increaseGodXP(double d, GodModule.GodXPType g) {
      this.godXP += d;
      this.godXPByType.put(g, this.godXPByType.get(g) + d);
   }

   public void decreaseGodXP(double d) {
      this.godXP -= d;
      if (this.godXP < 0.0) {
         this.godXP = 0.0;
      }
   }

   public double getGodXP() {
      return this.godXP;
   }

   public EnumMap<GodModule.GodXPType, Double> getGodXPByType() {
      return this.godXPByType;
   }

   public double getGodXPWorldMapCount() {
      return this.godXPWorldMapCount;
   }

   public static enum GodXPType {
      NOMADS_JOINED_VILLAGE(Text.getText("godXP.nomadsJoinedVillage")),
      BUILDINGS_UPGRADED(Text.getText("godXP.buildingsUpgraded")),
      BUILDINGS_CONSTRUCTED(Text.getText("godXP.buildingsConstructed")),
      BUILDINGS_DISMANTLED(Text.getText("godXP.buildingsDismantled")),
      WALLS_DISMANTLED(Text.getText("godXP.wallsDismantled")),
      WALLS_CONSTRUCTED(Text.getText("godXP.wallsConstructed")),
      VILLAGER_LEVELED_UP(Text.getText("godXP.villagerLeveledUp")),
      CHILD_BIRTH(Text.getText("godXP.childBirth")),
      BUILDING(Text.getText("godXP.building")),
      BUYING_RESOURCES(Text.getText("godXP.buyingResources")),
      HIRING_LABORERS(Text.getText("godXP.hiringLaborers")),
      ELDERS_SHARING_WISDOM(Text.getText("godXP.eldersSharingWisdom")),
      CLEARING_ROADS(Text.getText("godXP.clearingRoads")),
      COMBAT(Text.getText("godXP.combat")),
      CLEARING_TOPOGRAPHY(Text.getText("godXP.clearingTopography")),
      HARVESTING_RESOURCES(Text.getText("godXP.harvestingResources")),
      FARMING(Text.getText("godXP.farming")),
      TENDING_FARMS(Text.getText("godXP.tendingFarms")),
      COITUS(Text.getText("godXP.coitus")),
      REPAIRING_BUILDINGS(Text.getText("godXP.repairingBuildings")),
      REPAIRING_ROADS(Text.getText("godXP.repairingRoads")),
      BUILDING_ROADS(Text.getText("godXP.buildingRoads")),
      DELIVERING_RESOURCES_TO_CONSTRUCTION(Text.getText("godXP.deliveringResourcesToConstructionSites")),
      DELIVERING_RESOURCES_TO_COURIER(Text.getText("godXP.deliveringResourcesToCouriers")),
      DELIVERING_RESOURCES_TO_ROAD(Text.getText("godXP.deliveringResourcesToRoad")),
      DIGGING_HOLES(Text.getText("godXP.diggingHoles")),
      DISMANTLING_BUILDING(Text.getText("godXP.dismantlingBuildings")),
      EQUIPING_RESOURCES(Text.getText("godXP.equippingResources")),
      FINDING_NEW_HOME(Text.getText("godXP.findingNewHome")),
      GROWING_UP(Text.getText("godXP.growingUp")),
      PATROLLING(Text.getText("godXP.patrolling")),
      GUARD_DUTY(Text.getText("godXP.guardDuty")),
      HEALING_VILLAGERS(Text.getText("godXP.healingVillagers")),
      CLEARING_OUT_RESOURCES(Text.getText("godXP.clearingOutResources")),
      MIGRANTS_DEPARTING(Text.getText("godXP.migrantsDeparting")),
      COURIERS_DEPARTING(Text.getText("godXP.couriersDeparting")),
      REDISTRIBUTING_RESOURCES(Text.getText("godXP.redistributingResources")),
      REFINING_RESOURCES(Text.getText("godXP.refiningResources")),
      PROCESSING_RESOURCES(Text.getText("godXP.processingResources")),
      SELLING_RESOURCES(Text.getText("godXP.sellingResources")),
      STORING_RESOURCES(Text.getText("godXP.storingResources")),
      VISITING_MATE(Text.getText("godXP.visitingMate")),
      DOMESTICATION(Text.getText("godXP.domestication")),
      DESTROYING_ROADS(Text.getText("godXP.destroyingRoads")),
      PLANTING_CROPS(Text.getText("godXP.plantingCrops")),
      MIGRANTS_PREPARING(Text.getText("godXP.migrantsPreparing")),
      TRADING_EQUIPMENT(Text.getText("godXP.tradingEquipment")),
      VILLAGERS_IMPREGNATED(Text.getText("godXP.villagersImpregnated")),
      BLESSED_VILLAGERS_IMPREGNATED(Text.getText("godXP.blessedVillagersImpregnated")),
      LIVED_FULL_LIFE(Text.getText("godXP.livedFullLife")),
      HUNTING(Text.getText("godXP.hunting")),
      SLAUGHTERING_ANIMALS(Text.getText("godXP.slaughteringAnimals")),
      PRAYING(Text.getText("godXP.praying")),
      CAPTURING_SOULS(Text.getText("godXP.capturingSouls")),
      TERMINATING_ANIMAL_PREGNANCIES(Text.getText("godXP.terminatingAnimalPregnancies")),
      CASTING_SPELLS(Text.getText("godXP.castingSpells")),
      GRABBING_CREATURES(Text.getText("godXP.grabbingCreatures")),
      GRABBING_RESOURCES(Text.getText("godXP.grabbingResources")),
      SURVIVING_VILLAGERS(Text.getText("godXP.survivingVillagers")),
      VILLAGE_SIZE(Text.getText("godXP.villageSize")),
      MONSTERS_KILLED(Text.getText("godXP.monstersKilled")),
      UNLOCKED_GOAL(Text.getText("godXP.unlockedGoal")),
      OPENED_LOOT_BOXES(Text.getText("godXP.openedLootBoxes")),
      THROW_RESOURCES_IN_CULLIS_GATE(Text.getText("godXP.throwResourcesInCullisGate")),
      THROW_CREATURES_IN_CULLIS_GATE(Text.getText("godXP.throwCreaturesInCullisGate")),
      BREEDING(Text.getText("godXP.breeding")),
      ANIMAL_IMPREGNATED(Text.getText("godXP.animalImpregnated")),
      EGGS_LAID(Text.getText("godXP.eggsLaid"));

      private String name;

      private GodXPType(String n) {
         this.name = n;
      }

      public String getName() {
         return this.name;
      }
   }
}
