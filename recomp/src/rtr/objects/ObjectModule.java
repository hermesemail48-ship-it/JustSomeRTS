package rtr.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import rtr.ModuleBase;
import rtr.PerkModule;
import rtr.console.Console;
import rtr.goal.GoalModule;
import rtr.map.MapData;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.gates.GateBase;
import rtr.objects.gates.spritesheet.GateSprite;
import rtr.objects.gates.spritesheet.GateSpriteLoader;
import rtr.objects.objectflags.ObjectFlagFactory;
import rtr.objects.objectflags.ObjectFlags;
import rtr.objects.rangebuilder.RangeBuilder;
import rtr.objects.towerhead.TowerBase;
import rtr.objects.towerhead.spritesheet.TowerSprite;
import rtr.objects.towerhead.spritesheet.TowerSpriteLoader;
import rtr.objects.towerhead.towerheadflags.TowerHeadFlagFactory;
import rtr.resources.ResourceModule;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class ObjectModule extends ModuleBase {
   private static int UPDATE_WORK_REDUCTION_AMOUNTS_RATE = 200;
   private RangeBuilder orb;
   private TowerSpriteLoader towerSpriteLoader;
   private GateSpriteLoader gateSpriteLoader;
   private HashMap<Integer, ObjectBase> objectList = new HashMap<>();
   private ArrayList<ObjectBase> objectArrayList = new ArrayList<>();
   private ArrayList<ObjectBase> sortGroupUpdateList = new ArrayList<>();
   private HashMap<MobBase.MobGroup, ArrayList<ObjectBase>> objectsByGroup = new HashMap<>();
   private HashMap<MobJobBase.MobJobType, ArrayList<ObjectBase>> objectsByJob = new HashMap<>();
   private HashMap<ObjectModule.ObjectSortGroup, ArrayList<ObjectBase>> objectsBySortGroup = new HashMap<>();
   private ArrayList<ObjectBase> objectsToRemove = new ArrayList<>();
   private ArrayList<ObjectBase> objectMasterBuildingPriorityList = new ArrayList<>();
   private ArrayList<ArrayList<ObjectBase>> objectGroupedPriorityList = new ArrayList<>();
   private EnumMap<MapTilesLoader.TileSet, ObjectModule.SortOrder> objectGroupedSortOrder = new EnumMap<>(MapTilesLoader.TileSet.class);
   private int updateWorkReductionAmountsTick = 0;
   private double globalWorkSpeedReduction;
   private double[] harvestingSpeedReduction = new double[ResourceModule.ResourceType.values().length];
   private double[] refiningSpeedReduction = new double[ResourceModule.ResourceType.values().length];
   private double processingTrashSpeedReduction;
   private double buildingSpeedReduction;
   private double buildingRoadSpeedReduction;
   private double dismantlingSpeedReduction;
   private double clearingSpeedReduction;
   private double repairingSpeedReduction;
   private double repairingRoadSpeedReduction;
   private double farmingSpeedReduction;
   private double medicSpeedReduction;
   private double prayingSpeedReduction;
   private double capturingSoulsSpeedReduction;
   private double tendAnimalsSpeedReduction;
   private ObjectBase[][] objectMap;
   private int activeBuildings;
   private int maximumBuildings = 4;
   private int activeAncillaries;
   private int maximumAncillaries;
   private int maximumCatjeetProvisioners;
   private int maximumMigrationsPerDay;
   private int objectLastID;
   private ObjectFlagFactory objectFlagFactory;
   private TowerHeadFlagFactory towerHeadFlagFactory;

   public ObjectFlagFactory getObjectFlagFactory() {
      return this.objectFlagFactory;
   }

   public TowerHeadFlagFactory getTowerHeadFlagFactory() {
      return this.towerHeadFlagFactory;
   }

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);

      MobBase.MobGroup[] var6;
      for (MobBase.MobGroup g : MobBase.MobGroup.values()) {
         this.objectsByGroup.put(g, new ArrayList<>());
      }

      for (MobJobBase.MobJobType j : MobJobBase.MobJobType.values()) {
         this.objectsByJob.put(j, new ArrayList<>());
      }

      for (ObjectModule.ObjectSortGroup g : ObjectModule.ObjectSortGroup.values()) {
         this.objectsBySortGroup.put(g, new ArrayList<>());
      }

      if (this.objectMap == null) {
         this.objectMap = new ObjectBase[256][256];
      }

      this.orb = new RangeBuilder();
      if (this.towerSpriteLoader == null) {
         this.towerSpriteLoader = new TowerSpriteLoader();
      }

      if (this.gateSpriteLoader == null) {
         this.gateSpriteLoader = new GateSpriteLoader();
      }

      this.objectFlagFactory = new ObjectFlagFactory();
      this.towerHeadFlagFactory = new TowerHeadFlagFactory();
      this.requiresSync = true;
      this.initLoad();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
      this.objectList.clear();
      this.objectArrayList.clear();

      MobBase.MobGroup[] var4;
      for (MobBase.MobGroup g : MobBase.MobGroup.values()) {
         this.objectsByGroup.put(g, new ArrayList<>());
      }

      for (MobJobBase.MobJobType j : MobJobBase.MobJobType.values()) {
         this.objectsByJob.put(j, new ArrayList<>());
      }

      for (ObjectModule.ObjectSortGroup g : ObjectModule.ObjectSortGroup.values()) {
         this.objectsBySortGroup.put(g, new ArrayList<>());
      }

      this.objectsToRemove.clear();
      this.objectMasterBuildingPriorityList.clear();
      this.objectGroupedPriorityList.clear();
      this.globalWorkSpeedReduction = 0.0;
      this.harvestingSpeedReduction = new double[ResourceModule.ResourceType.values().length];
      this.refiningSpeedReduction = new double[ResourceModule.ResourceType.values().length];
      this.processingTrashSpeedReduction = 0.0;
      this.buildingSpeedReduction = 0.0;
      this.buildingRoadSpeedReduction = 0.0;
      this.dismantlingSpeedReduction = 0.0;
      this.clearingSpeedReduction = 0.0;
      this.repairingSpeedReduction = 0.0;
      this.repairingRoadSpeedReduction = 0.0;
      this.farmingSpeedReduction = 0.0;
      this.medicSpeedReduction = 0.0;
      this.prayingSpeedReduction = 0.0;
      this.capturingSoulsSpeedReduction = 0.0;
      this.tendAnimalsSpeedReduction = 0.0;
      this.objectMap = new ObjectBase[256][256];
      this.activeBuildings = 0;
      this.maximumBuildings = 4;
      this.activeAncillaries = 0;
      this.maximumAncillaries = 0;
      this.maximumCatjeetProvisioners = 0;
      this.maximumMigrationsPerDay = 0;
      this.objectLastID = 0;
   }

   @Override
   protected void newMainMenu() throws SlickException {
      this.loadMapEditor();
   }

   @Override
   protected void newPlay() throws SlickException {
      this.loadMapEditor();
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
      YMLDataMap dataMap = this.save.getActiveRegionalSave().getAndUnloadModuleSaveData(this.moduleType);
      this.objectLastID = Integer.parseInt(dataMap.getOrDefault("objectLastID", 0));
      int id = 0;

      while (dataMap.containsKey("object" + id)) {
         YMLDataMap objectMap = dataMap.getSubMap("object" + id);
         id++;
         String currentTypeString = objectMap.getOrDefault("currentType", "null");
         if (currentTypeString.contains("BARRACKS")) {
            Console.saveConverterOut(this, "InDev 33f", "InDev 34 Unstable 1", "Converting old Barracks building into a ranger lodge.");
            currentTypeString = currentTypeString.replace("BARRACKS", "RANGER_LODGE");
         }

         MapTilesLoader.TileSet currentType = null;

         MapTilesLoader.TileSet[] var9;
         for (MapTilesLoader.TileSet t : MapTilesLoader.TileSet.values()) {
            if (t.toString().equals(currentTypeString)) {
               currentType = t;
               break;
            }
         }

         String subTypeString = objectMap.getOrDefault("subType", "null");
         ObjectBase.ObjectSubType subType = null;

         ObjectBase.ObjectSubType[] var11;
         for (ObjectBase.ObjectSubType tx : ObjectBase.ObjectSubType.values()) {
            if (tx.toString().equals(subTypeString)) {
               subType = tx;
               break;
            }
         }

         this.addObject(
            Integer.parseInt(objectMap.getOrDefault("objectX", 0)),
            Integer.parseInt(objectMap.getOrDefault("objectY", 0)),
            Integer.parseInt(objectMap.getOrDefault("objectID", 0)),
            currentType,
            subType
         );
      }
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
      this.objectLastID = Integer.parseInt(dataMap.getOrDefault("objectLastID", 0));

      for (int count = 0; dataMap.containsKey("object" + count); count++) {
         YMLDataMap thisMap = dataMap.getSubMap("object" + count);
         thisMap.put("version", dataMap.getOrDefault("version", "noVersion"));
         this.addObject(thisMap);
      }

      this.activeBuildings = Integer.parseInt(dataMap.getOrDefault("activeBuildings", "0"));
      this.maximumBuildings = Integer.parseInt(dataMap.getOrDefault("maximumBuildings", "4"));
      this.activeAncillaries = Integer.parseInt(dataMap.getOrDefault("activeAncillaries", "0"));
      this.maximumAncillaries = Integer.parseInt(dataMap.getOrDefault("maximumAncillaries", "0"));
      this.maximumCatjeetProvisioners = Integer.parseInt(dataMap.getOrDefault("maximumCatjeetProvisioners", "0"));
      this.maximumMigrationsPerDay = Integer.parseInt(dataMap.getOrDefault("maximumMigrationsPerDay", "0"));
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
   }

   @Override
   public void initLoadSync() throws SlickException {
      this.synced = true;

      for (ObjectBase o : this.objectArrayList) {
         o.loadSync();
      }

      this.orb.updateFullRangeMap();
      HashMap<MapTilesLoader.TileSet, HashMap<ObjectBase.ObjectSubType, ObjectFlags>> objectInstances = this.objectFlagFactory.getObjectFlagsList();

      for (MapTilesLoader.TileSet t : objectInstances.keySet()) {
         if (t.getTileSetTemplate().getType() == MapTilesLoader.TileSetType.WALL) {
            this.objectGroupedSortOrder.put(t, ObjectModule.SortOrder.OUTER);
         } else {
            this.objectGroupedSortOrder.put(t, ObjectModule.SortOrder.FIRST);
         }
      }

      this.updateWorkReductionAmounts();

      for (ObjectBase o : this.objectArrayList) {
         int tileX = o.getObjectX();
         int tileY = o.getObjectY();
         int objectWidth = o.getObjectFlags().getWidth();
         int objectHeight = o.getObjectFlags().getHeight();

         for (int w = 0; w < objectWidth; w++) {
            for (int h = 0; h < objectHeight; h++) {
               this.objectMap[w + tileX][h + tileY] = o;
            }
         }
      }

      if (!this.goal.getAllGoals().get(GoalModule.GoalType.YOU_ALREADY_LOST).isCompleted()) {
         ObjectBase camp = this.getFirstBaseTypeFound(MapTilesLoader.TileSet.CASTLE_1);
         if (camp != null && camp.getCurrentPhase() == MapTilesLoader.TileSetObjectPhase.BASE) {
            this.goal.incrementGoal(GoalModule.GoalType.YOU_ALREADY_LOST);
         }
      }

      this.sortPriorityList();
   }

   @Override
   public YMLDataMap getRegionalSaveData() throws SlickException {
      this.updateObjectArrays();
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("version", Game.getVersion());
      int count = 0;

      for (ObjectBase o : this.objectArrayList) {
         dataMap.put("object" + count, o.getRegionalSaveData());
         count++;
      }

      dataMap.put("objectLastID", this.objectLastID);
      dataMap.put("activeBuildings", this.activeBuildings);
      dataMap.put("maximumBuildings", this.maximumBuildings);
      dataMap.put("activeAncillaries", this.activeAncillaries);
      dataMap.put("maximumAncillaries", this.maximumAncillaries);
      dataMap.put("maximumCatjeetProvisioners", this.maximumCatjeetProvisioners);
      dataMap.put("maximumMigrationsPerDay", this.maximumMigrationsPerDay);
      return dataMap;
   }

   @Override
   public YMLDataMap getEditorSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("version", Game.getVersion());
      int count = 0;
      if (this.objectArrayList.size() == 0) {
         dataMap.put("objectCount", 0);
      } else {
         for (ObjectBase o : this.objectArrayList) {
            dataMap.put("object" + count, o.getEditorProperties());
            count++;
         }

         dataMap.put("objectCount", count);
      }

      dataMap.put("objectLastID", this.objectLastID);
      return dataMap;
   }

   public void update(boolean showIcons) throws SlickException {
      this.sortPriorityList();

      for (ObjectBase o : this.sortGroupUpdateList) {
         this.updateSortGroups(o);
      }

      this.sortGroupUpdateList.clear();
      this.updateObjectArrays();
      this.activeBuildings = 0;
      this.maximumBuildings = 4;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.BUILDING_SLOTS) > 0.0) {
         this.maximumBuildings = (int)(this.maximumBuildings + this.perk.getPerkTypeTotal(PerkModule.PerkType.BUILDING_SLOTS));
      }

      this.activeAncillaries = 0;
      this.maximumAncillaries = 0;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.ANCILLARY_SLOTS) > 0.0) {
         this.maximumAncillaries = (int)(this.maximumAncillaries + this.perk.getPerkTypeTotal(PerkModule.PerkType.ANCILLARY_SLOTS));
      }

      this.maximumCatjeetProvisioners = 0;
      this.maximumMigrationsPerDay = 0;

      for (ObjectBase o : this.objectArrayList) {
         o.update(showIcons);
         ObjectFlags objectFlags = o.getObjectFlags();
         if (o.isInRange()
            && objectFlags.getMobGroup() != MobBase.MobGroup.MONSTER
            && (objectFlags.getMobGroup() != MobBase.MobGroup.NONE || objectFlags.getSubType() == ObjectBase.ObjectSubType.ABANDONED)) {
            this.maximumBuildings = this.maximumBuildings + objectFlags.getBuildingsProvided();
            this.maximumAncillaries = this.maximumAncillaries + objectFlags.getAncillariesProvided();
            this.maximumCatjeetProvisioners = this.maximumCatjeetProvisioners + objectFlags.getCatjeetProvisionersProvided();
            this.maximumMigrationsPerDay = this.maximumMigrationsPerDay + objectFlags.getMigrationsPerDayProvided();
            if (objectFlags.getBaseType().getTileSetTemplate().getType() != MapTilesLoader.TileSetType.WALL
               && (
                  objectFlags.getSubType() == ObjectBase.ObjectSubType.DISMANTLE
                     || objectFlags.getSubType() == ObjectBase.ObjectSubType.BUILT
                     || objectFlags.getSubType() == ObjectBase.ObjectSubType.CONSTRUCTION
                     || objectFlags.getSubType() == ObjectBase.ObjectSubType.ABANDONED && !o.isPaused()
               )) {
               if (objectFlags.getBaseType() == MapTilesLoader.TileSet.ANCILLARY) {
                  this.activeAncillaries++;
               } else {
                  this.activeBuildings++;
               }
            }
         }
      }

      this.orb.update();
      this.updateWorkReductionAmountsTick++;
      if (this.updateWorkReductionAmountsTick > UPDATE_WORK_REDUCTION_AMOUNTS_RATE) {
         this.updateWorkReductionAmounts();
         this.updateWorkReductionAmountsTick = 0;
      }
   }

   private void updateObjectArrays() throws SlickException {
      for (ObjectBase o : this.objectsToRemove) {
         this.removeObject(o);
      }

      this.objectsToRemove.clear();
   }

   private void updateWorkReductionAmounts() {
      this.globalWorkSpeedReduction = 0.0;

      ResourceModule.ResourceType[] t;
      for (ResourceModule.ResourceType tx : t = ResourceModule.ResourceType.values()) {
         this.harvestingSpeedReduction[tx.ordinal()] = 0.0;
         this.refiningSpeedReduction[tx.ordinal()] = 0.0;
      }

      this.processingTrashSpeedReduction = 0.0;
      this.buildingSpeedReduction = 0.0;
      this.buildingRoadSpeedReduction = 0.0;
      this.dismantlingSpeedReduction = 0.0;
      this.clearingSpeedReduction = 0.0;
      this.repairingSpeedReduction = 0.0;
      this.repairingRoadSpeedReduction = 0.0;
      this.farmingSpeedReduction = 0.0;
      this.medicSpeedReduction = 0.0;
      this.prayingSpeedReduction = 0.0;
      this.capturingSoulsSpeedReduction = 0.0;
      this.tendAnimalsSpeedReduction = 0.0;

      for (ObjectBase o : this.objectArrayList) {
         if (o.isInRange()) {
            ObjectFlags objectFlags = o.getObjectFlags();
            this.globalWorkSpeedReduction = this.globalWorkSpeedReduction + objectFlags.getGlobalWorkSpeedReduction();

            ResourceModule.ResourceType[] var7;
            for (ResourceModule.ResourceType tx : ResourceModule.ResourceType.values()) {
               this.harvestingSpeedReduction[tx.ordinal()] += objectFlags.getHarvestingSpeedReduction(tx);
               this.refiningSpeedReduction[tx.ordinal()] += objectFlags.getRefiningSpeedReduction(tx);
            }

            this.processingTrashSpeedReduction = this.processingTrashSpeedReduction + objectFlags.getProcessingTrashSpeedReduction();
            this.buildingSpeedReduction = this.buildingSpeedReduction + objectFlags.getBuildingSpeedReduction();
            this.buildingRoadSpeedReduction = this.buildingRoadSpeedReduction + objectFlags.getBuildingRoadSpeedReduction();
            this.dismantlingSpeedReduction = this.dismantlingSpeedReduction + objectFlags.getDismantlingSpeedReduction();
            this.clearingSpeedReduction = this.clearingSpeedReduction + objectFlags.getClearingSpeedReduction();
            this.repairingSpeedReduction = this.repairingSpeedReduction + objectFlags.getRepairingSpeedReduction();
            this.repairingRoadSpeedReduction = this.repairingRoadSpeedReduction + objectFlags.getRepairingRoadSpeedReduction();
            this.farmingSpeedReduction = this.farmingSpeedReduction + objectFlags.getFarmingSpeedReduction();
            this.medicSpeedReduction = this.medicSpeedReduction + objectFlags.getMedicSpeedReduction();
            this.prayingSpeedReduction = this.prayingSpeedReduction + objectFlags.getPrayingSpeedReduction();
            this.capturingSoulsSpeedReduction = this.capturingSoulsSpeedReduction + objectFlags.getCapturingSoulsSpeedReduction();
            this.tendAnimalsSpeedReduction = this.tendAnimalsSpeedReduction + objectFlags.getTendAnimalsSpeedReduction();
         }
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.GLOBAL_WORK_SPEED) > 0.0) {
         this.globalWorkSpeedReduction = this.globalWorkSpeedReduction + this.perk.getPerkTypeTotal(PerkModule.PerkType.GLOBAL_WORK_SPEED) * 100.0;
      }

      for (ResourceModule.ResourceType tx : t = ResourceModule.ResourceType.values()) {
         if ((
               tx == ResourceModule.ResourceType.WOOD
                  || tx == ResourceModule.ResourceType.ROCK
                  || tx == ResourceModule.ResourceType.CRYSTAL
                  || tx == ResourceModule.ResourceType.RAW_VEGETABLE
            )
            && this.perk.getPerkTypeTotal(PerkModule.PerkType.HARVESTING_SPEED) > 0.0) {
            this.harvestingSpeedReduction[tx.ordinal()] += this.perk.getPerkTypeTotal(PerkModule.PerkType.HARVESTING_SPEED) * 100.0;
         }

         if (tx.getTemplate().getBaseResources().size() > 0 && this.perk.getPerkTypeTotal(PerkModule.PerkType.REFINING_SPEED) > 0.0) {
            this.refiningSpeedReduction[tx.ordinal()] += this.perk.getPerkTypeTotal(PerkModule.PerkType.REFINING_SPEED) * 100.0;
         }
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.BUILDING_SPEED) > 0.0) {
         this.buildingSpeedReduction = this.buildingSpeedReduction + this.perk.getPerkTypeTotal(PerkModule.PerkType.BUILDING_SPEED) * 100.0;
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.ROAD_CONSTRUCTION_SPEED) > 0.0) {
         this.buildingRoadSpeedReduction = this.buildingRoadSpeedReduction + this.perk.getPerkTypeTotal(PerkModule.PerkType.ROAD_CONSTRUCTION_SPEED) * 100.0;
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.DISMANTLING_SPEED) > 0.0) {
         this.dismantlingSpeedReduction = this.dismantlingSpeedReduction + this.perk.getPerkTypeTotal(PerkModule.PerkType.DISMANTLING_SPEED) * 100.0;
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.CLEARING_SPEED) > 0.0) {
         this.clearingSpeedReduction = this.clearingSpeedReduction + this.perk.getPerkTypeTotal(PerkModule.PerkType.CLEARING_SPEED) * 100.0;
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.REPAIRING_SPEED) > 0.0) {
         this.repairingSpeedReduction = this.repairingSpeedReduction + this.perk.getPerkTypeTotal(PerkModule.PerkType.REPAIRING_SPEED) * 100.0;
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.MAINTAINING_ROAD_SPEED) > 0.0) {
         this.repairingRoadSpeedReduction = this.repairingRoadSpeedReduction + this.perk.getPerkTypeTotal(PerkModule.PerkType.MAINTAINING_ROAD_SPEED) * 100.0;
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.FARMING_SPEED) > 0.0) {
         this.farmingSpeedReduction = this.farmingSpeedReduction + this.perk.getPerkTypeTotal(PerkModule.PerkType.FARMING_SPEED) * 100.0;
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.MEDIC_WORK_SPEED) > 0.0) {
         this.medicSpeedReduction = this.medicSpeedReduction + this.perk.getPerkTypeTotal(PerkModule.PerkType.MEDIC_WORK_SPEED) * 100.0;
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.PRAYING_SPEED) > 0.0) {
         this.prayingSpeedReduction = this.prayingSpeedReduction + this.perk.getPerkTypeTotal(PerkModule.PerkType.PRAYING_SPEED) * 100.0;
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.CAPTURING_SOULS_SPEED) > 0.0) {
         this.capturingSoulsSpeedReduction = this.capturingSoulsSpeedReduction + this.perk.getPerkTypeTotal(PerkModule.PerkType.CAPTURING_SOULS_SPEED) * 100.0;
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.TEND_ANIMALS_SPEED) > 0.0) {
         this.tendAnimalsSpeedReduction = this.tendAnimalsSpeedReduction + this.perk.getPerkTypeTotal(PerkModule.PerkType.TEND_ANIMALS_SPEED) * 100.0;
      }

      for (ResourceModule.ResourceType tx : t = ResourceModule.ResourceType.values()) {
         if (this.harvestingSpeedReduction[tx.ordinal()] > 90.0) {
            this.harvestingSpeedReduction[tx.ordinal()] = 90.0;
         }

         if (this.refiningSpeedReduction[tx.ordinal()] > 90.0) {
            this.refiningSpeedReduction[tx.ordinal()] = 90.0;
         }
      }

      if (this.buildingSpeedReduction > 90.0) {
         this.buildingSpeedReduction = 90.0;
      }

      if (this.buildingRoadSpeedReduction > 90.0) {
         this.buildingRoadSpeedReduction = 90.0;
      }

      if (this.dismantlingSpeedReduction > 90.0) {
         this.dismantlingSpeedReduction = 90.0;
      }

      if (this.clearingSpeedReduction > 90.0) {
         this.clearingSpeedReduction = 90.0;
      }

      if (this.repairingSpeedReduction > 90.0) {
         this.repairingSpeedReduction = 90.0;
      }

      if (this.repairingRoadSpeedReduction > 90.0) {
         this.repairingRoadSpeedReduction = 90.0;
      }

      if (this.farmingSpeedReduction > 90.0) {
         this.farmingSpeedReduction = 90.0;
      }

      if (this.medicSpeedReduction > 90.0) {
         this.medicSpeedReduction = 90.0;
      }

      if (this.prayingSpeedReduction > 90.0) {
         this.prayingSpeedReduction = 90.0;
      }

      if (this.capturingSoulsSpeedReduction > 90.0) {
         this.capturingSoulsSpeedReduction = 90.0;
      }

      if (this.tendAnimalsSpeedReduction > 90.0) {
         this.tendAnimalsSpeedReduction = 90.0;
      }
   }

   public void renderTowers() throws SlickException {
      this.towerSpriteLoader.getBaseSpriteSheet().startUse();

      for (ObjectBase o : this.objectArrayList) {
         o.renderTower();
      }

      this.towerSpriteLoader.getBaseSpriteSheet().endUse();
   }

   public void renderGates() throws SlickException {
      this.gateSpriteLoader.getBaseSpriteSheet().startUse();

      for (ObjectBase o : this.objectArrayList) {
         o.renderGate();
      }

      this.gateSpriteLoader.getBaseSpriteSheet().endUse();
   }

   public void renderTowersMapWriter() throws SlickException {
      for (ObjectBase o : this.objectArrayList) {
         o.renderTowerMapWriter();
      }
   }

   public void renderGatesMapWriter() throws SlickException {
      for (ObjectBase o : this.objectArrayList) {
         o.renderGateMapWriter();
      }
   }

   public void renderOverlay(Graphics g, boolean showIcons, boolean debug) throws SlickException {
      for (ObjectBase o : this.objectArrayList) {
         o.renderOverlay(g, showIcons, debug);
      }
   }

   public double getGlobalWorkSpeedReduction() {
      return this.globalWorkSpeedReduction;
   }

   public double getHarvestingSpeedReduction(ResourceModule.ResourceType t) {
      return this.harvestingSpeedReduction[t.ordinal()];
   }

   public double getRefiningSpeedReduction(ResourceModule.ResourceType t) {
      return this.refiningSpeedReduction[t.ordinal()];
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

   private void sortPriorityList() {
      ObjectBase castle = null;

      label130:
      for (ObjectBase o : this.objectArrayList) {
         ObjectFlags objectFlags = o.getObjectFlags();
         if (o.isInRange() && objectFlags.getMobGroup() != MobBase.MobGroup.MONSTER) {
            if (objectFlags.getBaseType() == MapTilesLoader.TileSet.CASTLE_1) {
               castle = o;
            }

            for (ArrayList<ObjectBase> a : this.objectGroupedPriorityList) {
               ObjectFlags groupObjectFlags = a.get(0).getObjectFlags();
               if (groupObjectFlags.getBaseType() == objectFlags.getBaseType() && a.contains(o)) {
                  continue label130;
               }
            }

            if (objectFlags.getBaseType().getTileSetTemplate().getType() == MapTilesLoader.TileSetType.WALL) {
               boolean foundWall = false;

               for (ArrayList<ObjectBase> ax : this.objectGroupedPriorityList) {
                  ObjectFlags groupObjectFlags = ax.get(0).getObjectFlags();
                  if (groupObjectFlags.getBaseType() == objectFlags.getBaseType()) {
                     foundWall = true;
                     ax.add(o);
                     break;
                  }
               }

               if (!foundWall) {
                  ArrayList<ObjectBase> newList = new ArrayList<>();
                  newList.add(o);
                  this.objectGroupedPriorityList.add(newList);
               }
            } else {
               ArrayList<ObjectBase> newList = new ArrayList<>();
               newList.add(o);
               this.objectGroupedPriorityList.add(newList);
            }
         }
      }

      for (int axx = this.objectGroupedPriorityList.size() - 1; axx >= 0; axx--) {
         for (int l = this.objectGroupedPriorityList.get(axx).size() - 1; l >= 0; l--) {
            if (!this.objectGroupedPriorityList.get(axx).get(l).isInRange()) {
               this.objectGroupedPriorityList.get(axx).remove(l);
            }
         }

         if (this.objectGroupedPriorityList.get(axx).size() == 0) {
            this.objectGroupedPriorityList.remove(axx);
         }
      }

      for (ArrayList<ObjectBase> axx : this.objectGroupedPriorityList) {
         MapTilesLoader.TileSet wallBaseType = axx.get(0).getObjectFlags().getBaseType();
         if (wallBaseType.getTileSetTemplate().getType() == MapTilesLoader.TileSetType.WALL) {
            axx.clear();

            for (ObjectBase ox : this.objectArrayList) {
               if (ox.isInRange() && ox.getObjectFlags().getBaseType() == wallBaseType) {
                  axx.add(ox);
               }
            }

            if (this.objectGroupedSortOrder.get(wallBaseType) != ObjectModule.SortOrder.LAST && castle != null) {
               if (this.objectGroupedSortOrder.get(wallBaseType) == ObjectModule.SortOrder.INNER) {
                  OrderedPair center = castle.getCenterCoordinates();
                  this.sortPriorityListSortWalls(center.getX(), center.getY(), axx, false);
               } else if (this.objectGroupedSortOrder.get(wallBaseType) == ObjectModule.SortOrder.OUTER) {
                  OrderedPair center = castle.getCenterCoordinates();
                  this.sortPriorityListSortWalls(center.getX(), center.getY(), axx, true);
               }
            } else {
               Collections.reverse(axx);
            }
         }
      }

      this.objectMasterBuildingPriorityList.clear();

      for (ArrayList<ObjectBase> axxx : this.objectGroupedPriorityList) {
         this.objectMasterBuildingPriorityList.addAll(axxx);
      }
   }

   private void sortPriorityListSortWalls(final int tileX, final int tileY, ArrayList<ObjectBase> list, final boolean outer) {
      Collections.sort(list, new Comparator<ObjectBase>() {
         public int compare(ObjectBase m1, ObjectBase m2) {
            int dx1 = m1.getObjectX() - tileX;
            int dy1 = m1.getObjectY() - tileY;
            int hValue1 = dx1 * dx1 + dy1 * dy1;
            int dx2 = m2.getObjectX() - tileX;
            int dy2 = m2.getObjectY() - tileY;
            int hValue2 = dx2 * dx2 + dy2 * dy2;
            if (m1 == m2) {
               return 0;
            } else {
               return outer ? hValue2 - hValue1 : hValue1 - hValue2;
            }
         }
      });
   }

   public ObjectBase addObject(int tileX, int tileY, MapTilesLoader.TileSet baseType, ObjectBase.ObjectSubType subType) throws SlickException {
      int next = this.getNextObjectID();
      return this.addObject(tileX, tileY, next, baseType, subType);
   }

   public ObjectBase addObject(int tileX, int tileY, int objectID, MapTilesLoader.TileSet baseType, ObjectBase.ObjectSubType subType) throws SlickException {
      ObjectBase newObject = new ObjectBase(tileX, tileY, objectID, baseType, subType);
      int width = newObject.getObjectFlags().getWidth();
      int height = newObject.getObjectFlags().getHeight();

      for (int w = 0; w < width; w++) {
         for (int h = 0; h < height; h++) {
            this.objectMap[w + tileX][h + tileY] = newObject;
         }
      }

      this.objectList.put(objectID, newObject);
      this.objectArrayList.add(newObject);
      this.addToSortGroups(newObject);
      return newObject;
   }

   public void addObject(YMLDataMap dataMap) throws SlickException {
      ObjectBase newObject = new ObjectBase(dataMap);
      this.objectList.put(newObject.getID(), newObject);
      this.objectArrayList.add(newObject);
      this.addToSortGroups(newObject);
   }

   private void updateSortGroups(ObjectBase o) {
      this.removeFromSortGroups(o);
      this.addToSortGroups(o);
   }

   private void addToSortGroups(ObjectBase o) {
      ObjectFlags objectFlags = o.getObjectFlags();
      this.objectsByGroup.get(objectFlags.getMobGroup()).add(o);
      if (objectFlags.getOccupantsJob() != null && objectFlags.getOccupantsJob() != MobJobBase.MobJobType.NONE) {
         this.objectsByJob.get(objectFlags.getOccupantsJob()).add(o);
      }

      if (objectFlags.canAcceptEssence()) {
         this.objectsBySortGroup.get(ObjectModule.ObjectSortGroup.ACCEPTS_ESSENCE).add(o);
      }

      if (objectFlags.canCollectEssence()) {
         this.objectsBySortGroup.get(ObjectModule.ObjectSortGroup.COLLECTS_ESSENCE).add(o);
      }

      if (objectFlags.canDeliverEssence()) {
         this.objectsBySortGroup.get(ObjectModule.ObjectSortGroup.DELIVERS_ESSENCE).add(o);
      }

      if (o.getTower() != null) {
         this.objectsBySortGroup.get(ObjectModule.ObjectSortGroup.TOWERS).add(o);
      }

      if (objectFlags.isLightningRod()) {
         this.objectsBySortGroup.get(ObjectModule.ObjectSortGroup.LIGHTNING_RODS).add(o);
      }

      if (objectFlags.getOccupantsMax() > 0) {
         this.objectsBySortGroup.get(ObjectModule.ObjectSortGroup.PROVIDES_OCCUPANTS_SLOTS).add(o);
      }

      if (objectFlags.getWorkersProvided() > 0) {
         this.objectsBySortGroup.get(ObjectModule.ObjectSortGroup.PROVIDES_JOB_SLOTS).add(o);
      }

      if (objectFlags.canBuildingStore(ResourceModule.ResourceType.WATER_BOTTLE)
         || objectFlags.canBuildingStore(ResourceModule.ResourceType.DIRTY_WATER_BUCKET)
         || objectFlags.canBuildingStore(ResourceModule.ResourceType.WATER_BUCKET)) {
         this.objectsBySortGroup.get(ObjectModule.ObjectSortGroup.WATER_SOURCES).add(o);
      }

      if (objectFlags.getCatjeetProvisionersProvided() > 0) {
         this.objectsBySortGroup.get(ObjectModule.ObjectSortGroup.PROVIDES_PROVISIONERS).add(o);
      }
   }

   private void removeFromSortGroups(ObjectBase o) {
      this.objectsByGroup.get(o.getObjectFlags().getMobGroup()).remove(o);
      if (o.getObjectFlags().getOccupantsJob() != null) {
         this.objectsByJob.get(o.getObjectFlags().getOccupantsJob()).remove(o);
      }

      ObjectModule.ObjectSortGroup[] var5;
      for (ObjectModule.ObjectSortGroup g : ObjectModule.ObjectSortGroup.values()) {
         this.objectsBySortGroup.get(g).remove(o);
      }
   }

   public void flagObjectForDestructionAndRemoval(ObjectBase o) throws SlickException {
      if (!this.objectsToRemove.contains(o)) {
         this.objectsToRemove.add(o);
      }

      o.setDestroyed();
   }

   private void removeObject(ObjectBase o) throws SlickException {
      int arrayID = o.getID();
      this.map.removeObjectFromMap(o);
      if (this.mob.isInitialized()) {
         o.removeAllMobs();
      }

      if (this.essence.isInitialized()) {
         o.discharge();
      }

      int objectWidth = this.objectList.get(arrayID).getObjectFlags().getWidth();
      int objectHeight = this.objectList.get(arrayID).getObjectFlags().getHeight();
      int objectX = this.objectList.get(arrayID).getObjectX();
      int objectY = this.objectList.get(arrayID).getObjectY();

      for (int w = 0; w < objectWidth; w++) {
         for (int h = 0; h < objectHeight; h++) {
            this.objectMap[w + objectX][h + objectY] = null;
         }
      }

      if (this.resource != null) {
         this.resource.deleteFarmland(this.objectList.get(arrayID).getLocalFarmland());
      }

      for (int i = 0; i < this.objectList.get(arrayID).getLocalFarmland().size(); i++) {
         this.objectList.get(arrayID).getLocalFarmland().get(i).removeCrop();
      }

      this.objectList.get(arrayID).getLocalFarmland().clear();
      this.objectList.get(arrayID).removeAllMobs();
      this.objectList.remove(arrayID);
      this.objectArrayList.remove(o);
      this.removeFromSortGroups(o);
      this.objectMasterBuildingPriorityList.remove(o);
      ArrayList<ObjectBase> groupedListCheck = null;

      for (ArrayList<ObjectBase> a : this.objectGroupedPriorityList) {
         if (a.contains(o)) {
            groupedListCheck = a;
            a.remove(o);
            break;
         }
      }

      if (groupedListCheck != null && groupedListCheck.size() == 0) {
         this.objectGroupedPriorityList.remove(groupedListCheck);
      }
   }

   public ObjectBase getObjectAt(Shape mouse) {
      for (ObjectBase o : this.objectArrayList) {
         if (o.intersects(mouse)) {
            return o;
         }
      }

      return null;
   }

   public boolean isObject(int tileX, int tileY) {
      return this.objectMap[tileX][tileY] != null;
   }

   public Rectangle getObjectRectangle(int x) {
      return this.objectList.get(x).getIntersectBox();
   }

   public HashMap<Integer, ObjectBase> getObjects() {
      return this.objectList;
   }

   public ArrayList<ObjectBase> getObjectsArrayList() {
      return this.objectArrayList;
   }

   public boolean objectExists(ObjectBase o) {
      return o != null && this.objectList.get(o.getID()) != null && !o.isDestroyed();
   }

   public ArrayList<ObjectBase> getObjectMasterBuildingPriorityList() {
      return this.objectMasterBuildingPriorityList;
   }

   public ArrayList<ArrayList<ObjectBase>> getObjectGroupedPriorityList() {
      return this.objectGroupedPriorityList;
   }

   public void increasePriority(ObjectBase o) {
      int i = -1;

      for (ArrayList<ObjectBase> a : this.objectGroupedPriorityList) {
         if (a.contains(o)) {
            i = this.objectGroupedPriorityList.indexOf(a);
            break;
         }
      }

      if (i > 0) {
         Collections.swap(this.objectGroupedPriorityList, i, i - 1);
      }
   }

   public void decreasePriority(ObjectBase o) {
      int i = -1;

      for (ArrayList<ObjectBase> a : this.objectGroupedPriorityList) {
         if (a.contains(o)) {
            i = this.objectGroupedPriorityList.indexOf(a);
            break;
         }
      }

      if (i < this.objectGroupedPriorityList.size() - 1) {
         Collections.swap(this.objectGroupedPriorityList, i, i + 1);
      }
   }

   public void setPriorityTop(ObjectBase o) {
      ArrayList<ObjectBase> moveList = null;

      for (ArrayList<ObjectBase> a : this.objectGroupedPriorityList) {
         if (a.contains(o)) {
            moveList = a;
            break;
         }
      }

      this.objectGroupedPriorityList.remove(moveList);
      this.objectGroupedPriorityList.add(0, moveList);
   }

   public void setPriorityBottom(ObjectBase o) {
      ArrayList<ObjectBase> moveList = null;

      for (ArrayList<ObjectBase> a : this.objectGroupedPriorityList) {
         if (a.contains(o)) {
            moveList = a;
            break;
         }
      }

      this.objectGroupedPriorityList.remove(moveList);
      this.objectGroupedPriorityList.add(moveList);
   }

   public ObjectModule.SortOrder getGroupSortOrder(ObjectBase o) {
      return this.objectGroupedSortOrder.get(o.getObjectFlags().getBaseType());
   }

   public void toggleGroupSortOrder(ObjectBase o) {
      MapTilesLoader.TileSet baseType = o.getObjectFlags().getBaseType();
      if (this.objectGroupedSortOrder.get(baseType).ordinal() == ObjectModule.SortOrder.values().length - 1) {
         this.objectGroupedSortOrder.put(baseType, ObjectModule.SortOrder.values()[0]);
      } else {
         this.objectGroupedSortOrder.put(baseType, ObjectModule.SortOrder.values()[this.objectGroupedSortOrder.get(baseType).ordinal() + 1]);
      }
   }

   public int objectCount(MapTilesLoader.TileSet object, ObjectBase.ObjectSubType subType) {
      int count = 0;

      for (ObjectBase o : this.objectArrayList) {
         MapTilesLoader.TileSet thisBaseType = o.getObjectFlags().getBaseType();
         ObjectBase.ObjectSubType thisSubType = o.getObjectFlags().getSubType();
         if (thisBaseType == object && thisSubType == subType) {
            count++;
         }
      }

      return count;
   }

   public int objectCount(MapTilesLoader.TileSet object, boolean inRangeOnly) {
      int count = 0;

      for (ObjectBase o : this.objectArrayList) {
         if (o.getObjectFlags().getBaseType() == object && o.isInRange()) {
            count++;
         }
      }

      return count;
   }

   public int objectCount(MapTilesLoader.TileSet object) {
      int count = 0;

      for (ObjectBase o : this.objectArrayList) {
         if (o.getObjectFlags().getBaseType() == object) {
            count++;
         }
      }

      return count;
   }

   public ObjectBase objectFindFirst(MapTilesLoader.TileSet object) {
      for (int o : this.objectList.keySet()) {
         ObjectBase objectCheck = this.objectList.get(o);
         if (objectCheck.getObjectFlags().getBaseType() == object) {
            return objectCheck;
         }
      }

      return null;
   }

   public ObjectBase getBuildingAtLocation(int tileX, int tileY) {
      return this.objectMap[tileX][tileY];
   }

   public ObjectBase getBuildingAtLocation(OrderedPair o) {
      return this.getBuildingAtLocation(o.getX(), o.getY());
   }

   public ObjectBase getWallAtLocation(int tileX, int tileY) {
      for (ObjectBase o : this.objectArrayList) {
         if (o.getObjectFlags().getBaseType().getTileSetTemplate().getType() == MapTilesLoader.TileSetType.WALL
            && o.getObjectX() == tileX
            && o.getObjectY() == tileY) {
            return o;
         }
      }

      return null;
   }

   public int[][] getRangeMap(RangeBuilder.RangeMapLayer layer) {
      return this.orb.getRangeMap(layer);
   }

   public boolean isInRange(int tileX, int tileY) {
      return this.orb.getRangeMap(RangeBuilder.RangeMapLayer.RANGE_AND_DESIRABILITY)[tileX][tileY] >= 1;
   }

   public ArrayList<ObjectBase> getObjectsByGroup(MobBase.MobGroup g) {
      ArrayList<ObjectBase> list = new ArrayList<>();
      if (this.objectsByGroup.get(g) != null) {
         list.addAll(this.objectsByGroup.get(g));
      }

      return list;
   }

   public ArrayList<ObjectBase> getObjectsByJob(MobJobBase.MobJobType j) {
      ArrayList<ObjectBase> list = new ArrayList<>();
      if (this.objectsByJob.get(j) != null) {
         list.addAll(this.objectsByJob.get(j));
      }

      return list;
   }

   public ArrayList<ObjectBase> getObjectsBySortGroup(ObjectModule.ObjectSortGroup g) {
      ArrayList<ObjectBase> list = new ArrayList<>();
      if (this.objectsBySortGroup.get(g) != null) {
         list.addAll(this.objectsBySortGroup.get(g));
      }

      return list;
   }

   public ObjectBase getFirstBaseTypeFound(MapTilesLoader.TileSet t) {
      for (ObjectBase o : this.objectArrayList) {
         if (o.inRange && o.getObjectFlags().getBaseType() == t) {
            return o;
         }
      }

      return null;
   }

   public ObjectBase getFirstBaseTypeFound(MapTilesLoader.TileSet t, boolean ignoreRange) {
      for (ObjectBase o : this.objectArrayList) {
         if (o.getObjectFlags().getBaseType() == t) {
            return o;
         }
      }

      return null;
   }

   public int getNextObjectID() {
      return this.objectLastID++;
   }

   public boolean canBuildNewBuilding() {
      return this.activeBuildings < this.maximumBuildings;
   }

   public int getActiveBuildings() {
      return this.activeBuildings;
   }

   public int getMaximumBuildings() {
      return this.maximumBuildings;
   }

   public boolean canBuildNewAncillary() {
      return this.activeAncillaries < this.maximumAncillaries;
   }

   public int getActiveAncillaries() {
      return this.activeAncillaries;
   }

   public int getMaximumAncillaries() {
      return this.maximumAncillaries;
   }

   public int getMaximumCatjeetProvisioners() {
      return this.maximumCatjeetProvisioners;
   }

   public int getMaximumMigrationsPerDay() {
      return this.maximumMigrationsPerDay;
   }

   public ArrayList<ObjectBase> getObjectsNearby(MobBase mob, int distance) {
      int tileX = mob.getTileX();
      int tileY = mob.getTileY();
      ArrayList<ObjectBase> list = new ArrayList<>();

      for (OrderedPair c : Utilities.getAllCoordinatesWithin(tileX, tileY, distance)) {
         if (this.isObject(c.getX(), c.getY())) {
            ObjectBase o = this.objectMap[c.getX()][c.getY()];
            if (mob.canReach(o) && !list.contains(o)) {
               list.add(o);
            }
         }
      }

      return list;
   }

   public ArrayList<ObjectBase> getObjectsNearby(int tileX, int tileY, int distance) {
      return this.getObjectsNearby(tileX, tileY, distance, null, null);
   }

   public ArrayList<ObjectBase> getObjectsNearby(int tileX, int tileY, int distance, MobBase.MobGroup mobGroup) {
      return this.getObjectsNearby(tileX, tileY, distance, mobGroup, null);
   }

   public ArrayList<ObjectBase> getObjectsNearby(int tileX, int tileY, int distance, ArrayList<MobBase.MobGroup> mobGroups) {
      return this.getObjectsNearby(tileX, tileY, distance, null, mobGroups);
   }

   public ArrayList<ObjectBase> getObjectsNearby(int tileX, int tileY, int distance, MobBase.MobGroup mobGroup, ArrayList<MobBase.MobGroup> mobGroups) {
      ArrayList<ObjectBase> list = new ArrayList<>();

      for (OrderedPair c : Utilities.getAllCoordinatesWithin(tileX, tileY, distance)) {
         if (this.isObject(c.getX(), c.getY())) {
            ObjectBase o = this.objectMap[c.getX()][c.getY()];
            if ((mobGroups == null || mobGroups.contains(o.getObjectFlags().getMobGroup()))
               && (mobGroup == null || o.getObjectFlags().getMobGroup() == mobGroup)
               && !list.contains(o)) {
               list.add(o);
            }
         }
      }

      return list;
   }

   public ArrayList<ObjectBase> cullObjectsByDistance(int tileX, int tileY, int distance, ArrayList<ObjectBase> list, MapData.BlockMapGroup g) throws SlickException {
      ArrayList<ObjectBase> listOut = new ArrayList<>();

      for (OrderedPair c : this.mob.getPathfinder().getCoordinatesInRange(tileX, tileY, distance, false, g)) {
         ObjectBase o = this.objectMap[c.getX()][c.getY()];
         if (o != null && !listOut.contains(o) && list.contains(o)) {
            listOut.add(o);
         }
      }

      return listOut;
   }

   public TowerSprite createTowerSpriteSheet(TowerBase.TowerType towerType) throws SlickException {
      return this.towerSpriteLoader.createNewTowerSprite(towerType);
   }

   public GateSprite createGateSpriteSheet(GateBase.GateType gateType) throws SlickException {
      return this.gateSpriteLoader.createNewGateSprite(gateType);
   }

   public RangeBuilder getObjectRangeBuilder() {
      return this.orb;
   }

   public void addSortGroupObjectsUpdateList(ObjectBase o) {
      this.sortGroupUpdateList.add(o);
   }

   public boolean villageHasGolemParkingSpots() {
      for (ObjectBase o : this.objectArrayList) {
         if (o.isInRange() && o.getCourierGolemParking().size() > 0) {
            return true;
         }
      }

      return false;
   }

   public static enum ObjectSortGroup {
      DELIVERS_ESSENCE,
      ACCEPTS_ESSENCE,
      COLLECTS_ESSENCE,
      LIGHTNING_RODS,
      TOWERS,
      PROVIDES_OCCUPANTS_SLOTS,
      PROVIDES_JOB_SLOTS,
      WATER_SOURCES,
      PROVIDES_PROVISIONERS;
   }

   public static enum SortOrder {
      FIRST("First Placed"),
      LAST("Last Placed"),
      INNER("Inner"),
      OUTER("Outer");

      private final String text;

      private SortOrder(String t) {
         this.text = t;
      }

      public String getText() {
         return this.text;
      }
   }
}
