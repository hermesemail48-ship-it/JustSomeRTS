package rtr.objects.objectflags;

import rtr.utilities.JavaCompat;
import java.util.ArrayList;
import java.util.EnumMap;
import rtr.ModuleBase;
import rtr.PerkModule;
import rtr.map.MapModule;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.gates.GateBase;
import rtr.objects.towerhead.TowerBase;
import rtr.resources.ResourceModule;
import rtr.states.StateBase;
import rtr.utilities.OrderedPair;

public class ObjectFlags {
   protected PerkModule perk = (PerkModule)StateBase.getModule(ModuleBase.ModuleType.PERK);
   protected MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   protected String name;
   protected String baseName;
   protected String description = "";
   protected int width;
   protected int height;
   protected MobBase.MobGroup mobGroup;
   protected MapTilesLoader.TileSet baseType;
   protected ObjectBase.ObjectSubType subType;
   protected MapTilesLoader.TileSet currentType;
   protected boolean canNotSelect;
   protected boolean canNotBeAttacked;
   protected boolean invulnerable;
   protected boolean allowDismantle;
   protected boolean allowPause;
   protected ArrayList<MapTilesLoader.TileSet[]> requiredSupportBuildings = new ArrayList<>();
   protected ArrayList<MapTilesLoader.TileSet[]> recommendedSupportBuildings = new ArrayList<>();
   protected ArrayList<ResourceModule.ResourceType> canBuildingBeBuilt = new ArrayList<>();
   protected MobJobBase.MobJobType workerJobType = MobJobBase.MobJobType.NONE;
   protected int workersProvided;
   protected double globalWorkSpeedReduction;
   protected EnumMap<ResourceModule.ResourceType, Double> harvestingSpeedReduction = new EnumMap<>(ResourceModule.ResourceType.class);
   protected EnumMap<ResourceModule.ResourceType, Double> refiningSpeedReduction = new EnumMap<>(ResourceModule.ResourceType.class);
   protected double processingTrashSpeedReduction;
   protected double buildingSpeedReduction;
   protected double buildingRoadSpeedReduction;
   protected double dismantlingSpeedReduction;
   protected double clearingSpeedReduction;
   protected double repairingSpeedReduction;
   protected double repairingRoadSpeedReduction;
   protected double farmingSpeedReduction;
   protected double medicSpeedReduction;
   protected double prayingSpeedReduction;
   protected double capturingSoulsSpeedReduction;
   protected double tendAnimalsSpeedReduction;
   protected EnumMap<ResourceModule.ResourceType, ObjectBase.ResourceStorePriority> buildingStorePriority = new EnumMap<>(ResourceModule.ResourceType.class);
   protected ArrayList<ResourceModule.ResourceType> canBuildingStore = new ArrayList<>();
   protected EnumMap<ResourceModule.ResourceType, Integer> resourceStorageMax = new EnumMap<>(ResourceModule.ResourceType.class);
   protected boolean hasFillArt;
   protected int baseRange;
   protected int baseDesirability;
   protected OrderedPair towerCoordinates;
   protected TowerBase.TowerType towerType;
   protected TowerBase.TowerUpgradeStage towerUpgradeStage;
   protected ArrayList<ResourceModule.ResourceType> canBuildingFire = new ArrayList<>();
   protected int ammoMax;
   protected OrderedPair gateCoordinates;
   protected GateBase.GateType gateType;
   protected EnumMap<ResourceModule.ResourceType, Integer> resourceBaseValues = new EnumMap<>(ResourceModule.ResourceType.class);
   protected boolean canUpgrade;
   protected MapTilesLoader.TileSet nextUpgradeType;
   protected MapTilesLoader.TileSet nextUpgradeType2;
   protected MapTilesLoader.TileSet nextUpgradeType3;
   protected int occupantsMax;
   protected MobJobBase.MobJobType occupantsJob;
   protected ArrayList<MobBase.MobType> occupantsTypes = new ArrayList<>();
   protected int occupantsSleepBonusPercentOff;
   protected int occupantsHealthRegenBonusTicks;
   protected int occupantsFaithBonus;
   protected boolean isLightningRod;
   protected int lightningRodRange;
   protected OrderedPair lightningRodCoordinates;
   protected int lightningRodDissipationRate;
   protected boolean isCullisGate;
   protected boolean canInstability;
   protected int instabilityAmountMax;
   protected boolean isLootBox;
   protected boolean isCombobulator;
   protected int combobulatorMax;
   protected int combobulatorBonusLevel;
   protected int combobulatorEnergyRequired;
   protected int combobulatorEnergyRate;
   protected int combobulatorEnergyLossPerGolem;
   protected int combobulatorEnergyLossPerGolemRate;
   protected int combobulatorResourceDistance;
   protected ResourceModule.ResourceType combobulatorResourceType;
   protected boolean combobulatorEasterEggMode;
   protected ArrayList<MobBase.MobType> combobulatorSpawnTypes = new ArrayList<>();
   protected boolean canEnergyRegenerateFree;
   protected boolean canCollectEssence;
   protected boolean canAcceptEssence;
   protected boolean canDeliverEssence;
   protected boolean canDeliverEssenceToCollectors;
   protected boolean canReleaseEssenceToGod;
   protected OrderedPair essenceReturnCoordinates;
   protected OrderedPair essenceReturnCoordinatesPixelOffset;
   protected int energyMax;
   protected int energyRegenerationRate;
   protected int energyConsumeRate;
   protected boolean isFountain;
   protected boolean canCatchRain;
   protected int rainCatchRate;
   protected boolean canGenerateWater;
   protected int waterGenerationRate;
   protected boolean canPurifyWater;
   protected int purifyWaterRate;
   protected boolean isBurner;
   protected int burnerRate;
   protected int burnerEnergyDissipationRate;
   protected boolean isProcessor;
   protected boolean trashCan;
   protected int buildingsProvided;
   protected int ancillariesProvided;
   protected int catjeetProvisionersProvided;
   protected int migrationsPerDayProvided;
   protected int influenceMaintainCost;
   protected boolean destroyIfIgnored;
   protected int destroyIfIgnoredDelay;
   protected boolean allowDamageMonsterSpawns;
   protected int damageMonsterSpawnsDelay;

   public String getName() {
      return this.name;
   }

   public String getBaseName() {
      return this.baseName;
   }

   public String getDescription() {
      return this.description;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public MobBase.MobGroup getMobGroup() {
      return this.mobGroup;
   }

   public MapTilesLoader.TileSet getBaseType() {
      return this.baseType;
   }

   public ObjectBase.ObjectSubType getSubType() {
      return this.subType;
   }

   public MapTilesLoader.TileSet getCurrentType() {
      return this.currentType;
   }

   public boolean canNotSelect() {
      return this.canNotSelect;
   }

   public boolean canNotBeAttacked() {
      return this.canNotBeAttacked;
   }

   public boolean isInvulnerable() {
      return this.invulnerable;
   }

   public boolean allowDismantle() {
      return this.allowDismantle;
   }

   public boolean allowPause() {
      return this.allowPause;
   }

   public ArrayList<MapTilesLoader.TileSet[]> getRequiredSupportBuildings() {
      return this.requiredSupportBuildings;
   }

   public ArrayList<MapTilesLoader.TileSet[]> getRecommendedSupportBuildings() {
      return this.recommendedSupportBuildings;
   }

   public ArrayList<ResourceModule.ResourceType> getCanBuildingBeBuilt() {
      return this.canBuildingBeBuilt;
   }

   public boolean canBuildingBeBuilt() {
      return this.canWorkWith(this.canBuildingBeBuilt);
   }

   public boolean canBuildingBeBuilt(ResourceModule.ResourceType type) {
      return this.canWorkWith(this.canBuildingBeBuilt, type);
   }

   public MobJobBase.MobJobType getWorkerJobType() {
      return this.workerJobType;
   }

   public int getWorkersProvided() {
      return this.workersProvided;
   }

   public double getGlobalWorkSpeedReduction() {
      return this.globalWorkSpeedReduction;
   }

   public double getHarvestingSpeedReduction(ResourceModule.ResourceType t) {
      return JavaCompat.getOrDefault(this.harvestingSpeedReduction, t, 0.0);
   }

   public EnumMap<ResourceModule.ResourceType, Double> getHarvestingSpeedReduction() {
      return this.harvestingSpeedReduction;
   }

   public double getRefiningSpeedReduction(ResourceModule.ResourceType t) {
      return JavaCompat.getOrDefault(this.refiningSpeedReduction, t, 0.0);
   }

   public EnumMap<ResourceModule.ResourceType, Double> getRefiningSpeedReduction() {
      return this.refiningSpeedReduction;
   }

   public double getProcessingTrashSpeedReduction() {
      return this.processingTrashSpeedReduction;
   }

   public double getBuildingSpeedReduction() {
      return this.buildingSpeedReduction;
   }

   public double getBuildingRoadSpeedReduction() {
      return this.buildingRoadSpeedReduction;
   }

   public double getDismantlingSpeedReduction() {
      return this.dismantlingSpeedReduction;
   }

   public double getClearingSpeedReduction() {
      return this.clearingSpeedReduction;
   }

   public double getRepairingSpeedReduction() {
      return this.repairingSpeedReduction;
   }

   public double getRepairingRoadSpeedReduction() {
      return this.repairingRoadSpeedReduction;
   }

   public double getFarmingSpeedReduction() {
      return this.farmingSpeedReduction;
   }

   public double getMedicSpeedReduction() {
      return this.medicSpeedReduction;
   }

   public double getPrayingSpeedReduction() {
      return this.prayingSpeedReduction;
   }

   public double getCapturingSoulsSpeedReduction() {
      return this.capturingSoulsSpeedReduction;
   }

   public double getTendAnimalsSpeedReduction() {
      return this.tendAnimalsSpeedReduction;
   }

   public EnumMap<ResourceModule.ResourceType, ObjectBase.ResourceStorePriority> getBuildingStorePriority() {
      return this.buildingStorePriority;
   }

   public ArrayList<ResourceModule.ResourceType> getCanBuildingStore() {
      return this.canBuildingStore;
   }

   public boolean canBuildingStore() {
      return this.canWorkWith(this.canBuildingStore);
   }

   public boolean canBuildingStore(ResourceModule.ResourceType type) {
      return this.canWorkWith(this.canBuildingStore, type);
   }

   public EnumMap<ResourceModule.ResourceType, Integer> getResourceStorageMax() {
      return this.resourceStorageMax;
   }

   public boolean hasFillArt() {
      return this.hasFillArt;
   }

   public int getBaseRange() {
      return this.baseRange;
   }

   public int getBaseDesirability() {
      return this.baseDesirability;
   }

   public OrderedPair getTowerCoordinates() {
      return this.towerCoordinates;
   }

   public int getTowerXOnBuilding() {
      return this.towerCoordinates.getX();
   }

   public int getTowerYOnBuilding() {
      return this.towerCoordinates.getY();
   }

   public TowerBase.TowerType getTowerType() {
      return this.towerType;
   }

   public TowerBase.TowerUpgradeStage getTowerUpgradeStage() {
      return this.towerUpgradeStage;
   }

   public ArrayList<ResourceModule.ResourceType> getCanBuildingFire() {
      return this.canBuildingFire;
   }

   public boolean canBuildingFire() {
      return this.canWorkWith(this.canBuildingFire);
   }

   public boolean canBuildingFire(ResourceModule.ResourceType type) {
      return this.canWorkWith(this.canBuildingFire, type);
   }

   public int getAmmoMax() {
      return this.ammoMax;
   }

   public OrderedPair getGateCoordinates() {
      return this.gateCoordinates;
   }

   public int getGateXOnBuilding() {
      return this.gateCoordinates.getX();
   }

   public int getGateYOnBuilding() {
      return this.gateCoordinates.getY();
   }

   public GateBase.GateType getGateType() {
      return this.gateType;
   }

   public EnumMap<ResourceModule.ResourceType, Integer> getResourceBaseValues() {
      return this.resourceBaseValues;
   }

   public int getResourceValueBaseCount(ResourceModule.ResourceType type) {
      return this.resourceBaseValues.get(type);
   }

   public int getTotalValueBase() {
      int totalResources = 0;

      ResourceModule.ResourceType[] var5;
      for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
         totalResources += this.getResourceValueBaseCount(type);
      }

      return totalResources;
   }

   public boolean canUpgrade() {
      return this.canUpgrade;
   }

   public MapTilesLoader.TileSet getNextUpgradeType() {
      return this.nextUpgradeType;
   }

   public MapTilesLoader.TileSet getNextUpgradeType2() {
      return this.nextUpgradeType2;
   }

   public MapTilesLoader.TileSet getNextUpgradeType3() {
      return this.nextUpgradeType3;
   }

   public int getOccupantsMax() {
      return this.occupantsMax;
   }

   public MobJobBase.MobJobType getOccupantsJob() {
      return this.occupantsJob;
   }

   public ArrayList<MobBase.MobType> getOccupantsTypes() {
      return this.occupantsTypes;
   }

   public int getOccupantsSleepBonusPercentOff() {
      return this.occupantsSleepBonusPercentOff;
   }

   public int getOccupantsHealthRegenBonusTicks() {
      return this.occupantsHealthRegenBonusTicks;
   }

   public int getOccupantsFaithBonus() {
      return this.occupantsFaithBonus;
   }

   public boolean isLightningRod() {
      return this.isLightningRod;
   }

   public int getLightningRodRange() {
      return this.lightningRodRange;
   }

   public OrderedPair getLightningRodCoordinates() {
      return this.lightningRodCoordinates;
   }

   public int getLightningRodXOnBuilding() {
      return this.lightningRodCoordinates.getX();
   }

   public int getLightningRodYOnBuilding() {
      return this.lightningRodCoordinates.getY();
   }

   public int getLightningRodDissipationRate() {
      return this.lightningRodDissipationRate;
   }

   public boolean isCullisGate() {
      return this.isCullisGate;
   }

   public boolean canInstability() {
      return this.canInstability;
   }

   public int getInstabilityAmountMax() {
      return this.instabilityAmountMax;
   }

   public boolean isLootBox() {
      return this.isLootBox;
   }

   public boolean isCombobulator() {
      return this.isCombobulator;
   }

   public int getCombobulatorMax() {
      return this.combobulatorMax;
   }

   public int getCombobulatorBonusLevel() {
      return this.combobulatorBonusLevel;
   }

   public int getCombobulatorEnergyRequired() {
      return this.combobulatorEnergyRequired;
   }

   public int getCombobulatorEnergyRate() {
      return this.combobulatorEnergyRate;
   }

   public int getCombobulatorEnergyLossPerGolem() {
      return this.combobulatorEnergyLossPerGolem;
   }

   public int getCombobulatorEnergyLossPerGolemRate() {
      return this.combobulatorEnergyLossPerGolemRate;
   }

   public int getCombobulatorResourceDistance() {
      return this.combobulatorResourceDistance;
   }

   public ResourceModule.ResourceType getCombobulatorResourceType() {
      return this.combobulatorResourceType;
   }

   public ArrayList<MobBase.MobType> getCombobulatorSpawnTypes() {
      return this.combobulatorSpawnTypes;
   }

   public boolean canEnergyRegenerateFree() {
      return this.canEnergyRegenerateFree;
   }

   public boolean canCollectEssence() {
      return this.canCollectEssence;
   }

   public boolean canAcceptEssence() {
      return this.canAcceptEssence;
   }

   public boolean canDeliverEssence() {
      return this.canDeliverEssence;
   }

   public boolean canDeliverEssenceToCollectors() {
      return this.canDeliverEssenceToCollectors;
   }

   public boolean canReleaseEssenceToGod() {
      return this.canReleaseEssenceToGod;
   }

   public OrderedPair getEssenceReturnCoordinates() {
      return this.essenceReturnCoordinates;
   }

   public int getEssenceReturnXOnBuilding() {
      return this.essenceReturnCoordinates.getX();
   }

   public int getEssenceReturnYOnBuilding() {
      return this.essenceReturnCoordinates.getY();
   }

   public OrderedPair getEssenceReturnCoordinatesPixelOffset() {
      return this.essenceReturnCoordinatesPixelOffset;
   }

   public int getEssenceReturnXPixelOffset() {
      return this.essenceReturnCoordinatesPixelOffset.getX();
   }

   public int getEssenceReturnYPixelOffset() {
      return this.essenceReturnCoordinatesPixelOffset.getY();
   }

   public int getEnergyMax() {
      return this.energyMax;
   }

   public int getEnergyRegenerationRate() {
      return this.energyRegenerationRate;
   }

   public int getEnergyConsumeRate() {
      return this.energyConsumeRate;
   }

   public boolean isFountain() {
      return this.isFountain;
   }

   public boolean canCatchRain() {
      return this.canCatchRain;
   }

   public int getRainCatchRate() {
      return this.rainCatchRate;
   }

   public boolean canGenerateWater() {
      return this.canGenerateWater;
   }

   public int getWaterGenerationRate() {
      return this.waterGenerationRate;
   }

   public boolean canPurifyWater() {
      return this.canPurifyWater;
   }

   public int getPurifyWaterRate() {
      return this.purifyWaterRate;
   }

   public boolean isBurner() {
      return this.isBurner;
   }

   public int getBurnerRate() {
      return this.burnerRate;
   }

   public int getBurnerEnergyDissipationRate() {
      return this.burnerEnergyDissipationRate;
   }

   public boolean isProcessor() {
      return this.isProcessor;
   }

   public boolean isTrashCan() {
      return this.trashCan;
   }

   public int getBuildingsProvided() {
      return this.buildingsProvided;
   }

   public int getAncillariesProvided() {
      return this.ancillariesProvided;
   }

   public int getCatjeetProvisionersProvided() {
      return this.catjeetProvisionersProvided;
   }

   public int getMigrationsPerDayProvided() {
      return this.migrationsPerDayProvided;
   }

   public int getInfluenceMaintainCost() {
      return this.influenceMaintainCost;
   }

   public boolean canDestroyIfIgnored() {
      return this.destroyIfIgnored;
   }

   public int getDestroyIfIgnoredDelay() {
      return this.destroyIfIgnoredDelay;
   }

   public boolean allowDamageMonsterSpawns() {
      return this.allowDamageMonsterSpawns;
   }

   public int getDamageMonsterSpawnsDelay() {
      return this.damageMonsterSpawnsDelay;
   }

   public ObjectFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      this.currentType = currentType;
      this.subType = subType;

      ResourceModule.ResourceType[] var6;
      for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
         this.resourceBaseValues.put(type, 0);
         this.resourceStorageMax.put(type, 0);
      }

      this.configBasics();
      switch (subType) {
         case CONSTRUCTION:
            this.configConstruction();

            for (ResourceModule.ResourceType t : this.resourceBaseValues.keySet()) {
               if (this.resourceBaseValues.get(t) > 0) {
                  this.canBuildingBeBuilt.add(t);
               }
            }
            break;
         case ABANDONED:
            this.configAbandoned();

            for (ResourceModule.ResourceType tx : this.resourceBaseValues.keySet()) {
               if (this.resourceBaseValues.get(tx) > 0) {
                  this.canBuildingBeBuilt.add(tx);
               }
            }
            break;
         case BUILT:
            this.configBuilt();
            break;
         case DISMANTLE:
            this.configDismantle();
      }

      this.configPerkAdjustments();
   }

   protected void configBasics() {
   }

   protected void configConstruction() {
   }

   protected void configBuilt() {
   }

   protected void configAbandoned() {
      this.mobGroup = MobBase.MobGroup.NONE;
   }

   protected void configDismantle() {
      this.mobGroup = MobBase.MobGroup.NONE;
   }

   protected void configPerkAdjustments() {
   }

   protected void perkAdjustment(PerkModule.PerkType perkType) {
      if (!(this.perk.getPerkTypeTotal(perkType) <= 0.0)) {
         if (perkType == PerkModule.PerkType.EFFICIENT_CONSTRUCTION) {
            for (ResourceModule.ResourceType t : this.resourceBaseValues.keySet()) {
               if (this.resourceBaseValues.get(t) > 0) {
                  this.resourceBaseValues
                     .put(
                        t, (int)(this.resourceBaseValues.get(t).intValue() - this.resourceBaseValues.get(t).intValue() * this.perk.getPerkTypeTotal(perkType))
                     );
                  if (this.resourceBaseValues.get(t) == 0) {
                     this.resourceBaseValues.put(t, 1);
                  }
               }
            }
         }

         if (perkType == PerkModule.PerkType.IMPROVED_STORAGE) {
            for (ResourceModule.ResourceType r : this.resourceStorageMax.keySet()) {
               this.resourceStorageMax
                  .put(r, this.resourceStorageMax.get(r) + (int)(this.resourceStorageMax.get(r).intValue() * this.perk.getPerkTypeTotal(perkType)));
            }
         }

         if (perkType == PerkModule.PerkType.WATER_STORAGE) {
            for (ResourceModule.ResourceType r : this.resourceStorageMax.keySet()) {
               this.resourceStorageMax
                  .put(r, this.resourceStorageMax.get(r) + (int)(this.resourceStorageMax.get(r).intValue() * this.perk.getPerkTypeTotal(perkType)));
            }
         }

         if (perkType == PerkModule.PerkType.CRYSTAL_GOLEM_LEVEL
            || perkType == PerkModule.PerkType.STONE_GOLEM_LEVEL
            || perkType == PerkModule.PerkType.WOOD_GOLEM_LEVEL) {
            this.combobulatorBonusLevel = (int)(this.combobulatorBonusLevel + this.perk.getPerkTypeTotal(perkType));
         }

         if ((
               perkType == PerkModule.PerkType.CRYSTAL_GOLEM_CAPACITY
                  || perkType == PerkModule.PerkType.STONE_GOLEM_CAPACITY
                  || perkType == PerkModule.PerkType.WOOD_GOLEM_CAPACITY
            )
            && this.combobulatorMax > 0) {
            this.combobulatorMax = (int)(this.combobulatorMax + this.perk.getPerkTypeTotal(perkType));
         }

         if ((
               perkType == PerkModule.PerkType.CRYSTAL_GOLEM_CHARGE_TIME
                  || perkType == PerkModule.PerkType.STONE_GOLEM_CHARGE_TIME
                  || perkType == PerkModule.PerkType.WOOD_GOLEM_CHARGE_TIME
            )
            && this.combobulatorEnergyRate > 0) {
            this.combobulatorEnergyRate = (int)(this.combobulatorEnergyRate - this.combobulatorEnergyRate * this.perk.getPerkTypeTotal(perkType));
         }

         if (perkType == PerkModule.PerkType.EFFICIENT_HOUSING && this.occupantsMax > 0) {
            this.occupantsMax = (int)(this.occupantsMax + this.perk.getPerkTypeTotal(perkType));
         }

         if (perkType == PerkModule.PerkType.TOWER_STORAGE) {
            for (ResourceModule.ResourceType r : this.resourceStorageMax.keySet()) {
               this.resourceStorageMax
                  .put(r, this.resourceStorageMax.get(r) + (int)(this.resourceStorageMax.get(r).intValue() * this.perk.getPerkTypeTotal(perkType)));
            }

            if (this.energyMax > 0) {
               this.energyMax = (int)(this.energyMax + this.energyMax * this.perk.getPerkTypeTotal(perkType));
            }
         }

         if ((perkType == PerkModule.PerkType.GOD_TOWER_SPELL_COST || perkType == PerkModule.PerkType.GOD_WALL_SPELL_COST) && this.influenceMaintainCost > 0) {
            this.influenceMaintainCost = (int)(this.influenceMaintainCost - this.influenceMaintainCost * this.perk.getPerkTypeTotal(perkType));
         }

         if (perkType == PerkModule.PerkType.MARKET_CATJEET_CAPACITY) {
            this.catjeetProvisionersProvided = (int)(this.catjeetProvisionersProvided + this.perk.getPerkTypeTotal(perkType));
         }

         if (perkType == PerkModule.PerkType.ESSENCE_COLLECTOR_CAPACITY) {
            this.energyMax = (int)(this.energyMax + this.energyMax * this.perk.getPerkTypeTotal(perkType));
         }

         if (perkType == PerkModule.PerkType.EFFICIENT_TRASH_BURNING) {
            this.burnerRate = (int)(this.burnerRate - this.burnerRate * this.perk.getPerkTypeTotal(perkType));
         }

         if (perkType == PerkModule.PerkType.INCREASED_CULLIS_GATE_STABILITY) {
            this.instabilityAmountMax = (int)(this.instabilityAmountMax + this.instabilityAmountMax * this.perk.getPerkTypeTotal(perkType));
         }
      }
   }

   public boolean canWorkWith(ArrayList<ResourceModule.ResourceType> resourceArray) {
      return resourceArray.size() > 0;
   }

   public boolean canWorkWith(ArrayList<ResourceModule.ResourceType> resourceArray, ResourceModule.ResourceType type) {
      return resourceArray.contains(type);
   }

   public boolean hasAnyStorage() {
      for (ResourceModule.ResourceType t : this.resourceStorageMax.keySet()) {
         if (this.resourceStorageMax.get(t) > 0) {
            return true;
         }
      }

      return false;
   }
}
