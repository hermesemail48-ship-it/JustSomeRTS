package rtr.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import r2d.image.SpriteSheet;
import rtr.ImageLoader;
import rtr.ModuleBase;
import rtr.SoundModule;
import rtr.console.Console;
import rtr.font.Text;
import rtr.gui.states.BrushLoader;
import rtr.map.MapData;
import rtr.map.MapModule;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.particles.ParticleModule;
import rtr.resources.templates.ResourceTemplateAxe;
import rtr.resources.templates.ResourceTemplateBallistaeBolts;
import rtr.resources.templates.ResourceTemplateBandage;
import rtr.resources.templates.ResourceTemplateBase;
import rtr.resources.templates.ResourceTemplateBoard;
import rtr.resources.templates.ResourceTemplateBoiledEgg;
import rtr.resources.templates.ResourceTemplateBow;
import rtr.resources.templates.ResourceTemplateCapturedAnimal;
import rtr.resources.templates.ResourceTemplateCookedMeat;
import rtr.resources.templates.ResourceTemplateCrylithium;
import rtr.resources.templates.ResourceTemplateCrystal;
import rtr.resources.templates.ResourceTemplateCrystallyTrash;
import rtr.resources.templates.ResourceTemplateCutStone;
import rtr.resources.templates.ResourceTemplateDirtyWaterBucket;
import rtr.resources.templates.ResourceTemplateEerieVessel;
import rtr.resources.templates.ResourceTemplateEgg;
import rtr.resources.templates.ResourceTemplateEmptyEerieVessel;
import rtr.resources.templates.ResourceTemplateFeather;
import rtr.resources.templates.ResourceTemplateGhostDust;
import rtr.resources.templates.ResourceTemplateGodDust;
import rtr.resources.templates.ResourceTemplateGoldCoinSack;
import rtr.resources.templates.ResourceTemplateGoldIngot;
import rtr.resources.templates.ResourceTemplateGoldOre;
import rtr.resources.templates.ResourceTemplateHammer;
import rtr.resources.templates.ResourceTemplateHealingPotion;
import rtr.resources.templates.ResourceTemplateHoe;
import rtr.resources.templates.ResourceTemplateIronChestArmor;
import rtr.resources.templates.ResourceTemplateIronHelmet;
import rtr.resources.templates.ResourceTemplateIronIngot;
import rtr.resources.templates.ResourceTemplateIronOre;
import rtr.resources.templates.ResourceTemplateIronShield;
import rtr.resources.templates.ResourceTemplateIronSword;
import rtr.resources.templates.ResourceTemplateLeatherChestArmor;
import rtr.resources.templates.ResourceTemplateLeatherHelmet;
import rtr.resources.templates.ResourceTemplateLeatherShield;
import rtr.resources.templates.ResourceTemplateMedkit;
import rtr.resources.templates.ResourceTemplateOrganicyTrash;
import rtr.resources.templates.ResourceTemplatePickAxe;
import rtr.resources.templates.ResourceTemplateQuiver;
import rtr.resources.templates.ResourceTemplateRation;
import rtr.resources.templates.ResourceTemplateRawHide;
import rtr.resources.templates.ResourceTemplateRawMeat;
import rtr.resources.templates.ResourceTemplateRawVegetable;
import rtr.resources.templates.ResourceTemplateRock;
import rtr.resources.templates.ResourceTemplateRockyTrash;
import rtr.resources.templates.ResourceTemplateShovel;
import rtr.resources.templates.ResourceTemplateSilk;
import rtr.resources.templates.ResourceTemplateStoneBalls;
import rtr.resources.templates.ResourceTemplateSuspiciousKey;
import rtr.resources.templates.ResourceTemplateSuspiciousyTrash;
import rtr.resources.templates.ResourceTemplateTrashyCube;
import rtr.resources.templates.ResourceTemplateTrashyTrash;
import rtr.resources.templates.ResourceTemplateWaterBottle;
import rtr.resources.templates.ResourceTemplateWaterBucket;
import rtr.resources.templates.ResourceTemplateWood;
import rtr.resources.templates.ResourceTemplateWoodSword;
import rtr.resources.templates.ResourceTemplateWoodyTrash;
import rtr.road.RoadModule;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.utilities.ArrayFillUtil;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class ResourceModule extends ModuleBase {
   private int mapHeight;
   private int mapWidth;
   private int mapLayers;
   private byte[][] resourceValuesMap;
   private ResourceModule.ResourceType[][] resourceTypesMap;
   private ArrayList<ResourceBase> resourcesProvisioner = new ArrayList<>();
   private HashMap<ResourceModule.ResourceType, ArrayList<ResourceBase>> resourcesProvisionerByType;
   private HashMap<ResourceModule.ResourceType, Boolean> resourcesBarListMain;
   private HashMap<ResourceModule.ResourceBarType, Boolean> resourcesBarListRight;
   private ArrayList<ResourceBase> resourcesMaster = new ArrayList<>();
   private ArrayList<ResourceBase> resourcesInAir = new ArrayList<>();
   private ArrayList<ResourceBase> resourcesInBuilding = new ArrayList<>();
   private ArrayList<ResourceBase> resourcesOnGround = new ArrayList<>();
   private ArrayList<FarmlandBase> farmlandArray = new ArrayList<>();
   private EnumMap<ResourceModule.ResourceType, Integer> resourceGatherRate = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, ArrayList<Integer>> resourceGatherHistory = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, ArrayList<Integer>> resourceGatherRateHistory = new EnumMap<>(ResourceModule.ResourceType.class);
   private int resourceGatherSpacingTick = 0;
   private int resourceGatherSpacing = 250;
   private byte[][] resourcesOnGroundByteMap;
   private ResourceBase[][] resourcesOnGroundMap;
   private SpriteSheet resourceSheet;
   private Image[][] resourceImages;
   private int[] resourceStores = new int[RESOURCE_TYPES.length];
   private int[] resourceStoresSpace = new int[RESOURCE_TYPES.length];
   /** values() copies the array on every call; this is called per object per tick. */
   private static final ResourceModule.ResourceType[] RESOURCE_TYPES = ResourceModule.ResourceType.values();
   private static final int RESOURCE_TYPE_COUNT = RESOURCE_TYPES.length;
   private EnumMap<MapTilesLoader.TileSet, int[]> resourceStoresSpaceByType = new EnumMap<>(MapTilesLoader.TileSet.class);
   private int[] resourceGround = new int[RESOURCE_TYPES.length];
   private int[] resourceGroundInRange = new int[RESOURCE_TYPES.length];
   private int[] resourceCarried = new int[RESOURCE_TYPES.length];
   private int[] resourceEquipped = new int[RESOURCE_TYPES.length];
   private int increaseWaterRate = 1000;
   private int increaseWaterRateTick;
   private WaterBalancer waterBalancer;
   private int totalAvailableGoldCoins;
   private int maximumAvailableGoldCoins;
   private int totalAvailableWater;
   private int maximumAvailableWater;
   private int totalAvailableFood;
   private int maximumAvailableFood;
   private int totalAvailableDirtyWater;
   private int maximumAvailableDirtyWater;
   private int totalAvailableEnergy;
   private int maximumAvailableEnergy;
   public static int RESOURCE_STORAGE_AMOUNT = 501;
   public static int RESOURCE_INFINITE_AMOUNT = 502;
   private int[] resourceMakeAmounts = new int[RESOURCE_TYPES.length];
   private int[] resourceMaintainAmounts = new int[RESOURCE_TYPES.length];
   private int resourceLastID;
   private int farmlandLastID;
   public EnumMap<ResourceModule.ResourceType, EnumMap<ResourceModule.ResourceColorSet, ResourceModule.ResourceSpriteSet>> resourceColorMap;
   private static int slotNumber = 0;
   public EnumMap<ResourceModule.ResourceType, ArrayList<ResourceModule.ResourceType>> resourceRares;
   public EnumMap<ResourceModule.ResourceType, Integer> resourceRaresWeight;
   private ArrayList<OrderedPair> sweepList = new ArrayList<>();
   private ArrayList<OrderedPair> setList = new ArrayList<>();

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);

      ResourceModule.ResourceType[] var6;
      for (ResourceModule.ResourceType t : RESOURCE_TYPES) {
         this.resourceGatherRate.put(t, 0);
         this.resourceGatherHistory.put(t, new ArrayList<>());
         this.resourceGatherRateHistory.put(t, new ArrayList<>());
      }

      if (this.resourceSheet == null) {
         this.resourceSheet = new SpriteSheet(ImageLoader.getImage("res/resourceSheet.png"), 16, 16);
         this.resourceImages = new Image[this.resourceSheet.getHorizontalCount()][this.resourceSheet.getVerticalCount()];

         for (int x = 0; x < this.resourceSheet.getHorizontalCount(); x++) {
            for (int y = 0; y < this.resourceSheet.getVerticalCount(); y++) {
               this.resourceImages[x][y] = this.resourceSheet.getSprite(x, y);
            }
         }
      }

      this.loadResourceColorMap();
      this.loadResourceRares();
      this.mapHeight = 256;
      this.mapWidth = 256;
      this.mapLayers = 12;
      if (this.resourcesProvisionerByType == null) {
         this.resourcesProvisionerByType = new HashMap<>();

         for (ResourceModule.ResourceType t : RESOURCE_TYPES) {
            this.resourcesProvisionerByType.put(t, new ArrayList<>());
         }
      }

      if (this.resourcesBarListMain == null) {
         this.resourcesBarListMain = new HashMap<>();

         for (ResourceModule.ResourceType t : RESOURCE_TYPES) {
            this.resourcesBarListMain.put(t, false);
         }

         this.resourcesBarListMain.put(ResourceModule.ResourceType.WOOD, true);
         this.resourcesBarListMain.put(ResourceModule.ResourceType.BOARD, true);
         this.resourcesBarListMain.put(ResourceModule.ResourceType.ROCK, true);
         this.resourcesBarListMain.put(ResourceModule.ResourceType.CUT_STONE, true);
         this.resourcesBarListMain.put(ResourceModule.ResourceType.CRYSTAL, true);
         this.resourcesBarListMain.put(ResourceModule.ResourceType.CRYLITHIUM, true);
         this.resourcesBarListMain.put(ResourceModule.ResourceType.RAW_VEGETABLE, true);
         this.resourcesBarListMain.put(ResourceModule.ResourceType.RATION, true);
         this.resourcesBarListMain.put(ResourceModule.ResourceType.RAW_MEAT, true);
         this.resourcesBarListMain.put(ResourceModule.ResourceType.COOKED_MEAT, true);
      }

      if (this.resourcesBarListRight == null) {
         this.resourcesBarListRight = new HashMap<>();

         for (ResourceModule.ResourceBarType t : ResourceModule.ResourceBarType.values()) {
            this.resourcesBarListRight.put(t, false);
         }

         this.resourcesBarListRight.put(ResourceModule.ResourceBarType.GOLD, true);
         this.resourcesBarListRight.put(ResourceModule.ResourceBarType.WATER, true);
         this.resourcesBarListRight.put(ResourceModule.ResourceBarType.ENERGY, true);
         this.resourcesBarListRight.put(ResourceModule.ResourceBarType.FAITH, true);
      }

      if (this.resourceValuesMap == null) {
         this.resourceValuesMap = new byte[this.mapWidth][this.mapHeight];
      }

      if (this.resourceTypesMap == null) {
         this.resourceTypesMap = new ResourceModule.ResourceType[this.mapWidth][this.mapHeight];
      }

      if (this.resourcesOnGroundByteMap == null) {
         this.resourcesOnGroundByteMap = new byte[this.mapWidth][this.mapHeight];
      }

      if (this.resourcesOnGroundMap == null) {
         this.resourcesOnGroundMap = new ResourceBase[this.mapWidth][this.mapHeight];
      }

      if (this.waterBalancer == null) {
         this.waterBalancer = new WaterBalancer();
      }

      for (ResourceModule.ResourceType t : RESOURCE_TYPES) {
         if (t.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RAW
            && t.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER) {
            this.resourceMakeAmounts[t.ordinal()] = 0;
            this.resourceMaintainAmounts[t.ordinal()] = 0;
         } else {
            this.resourceMakeAmounts[t.ordinal()] = 0;
            this.resourceMaintainAmounts[t.ordinal()] = RESOURCE_STORAGE_AMOUNT;
         }
      }

      this.requiresSync = true;
      this.initLoad();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
      this.mapHeight = 0;
      this.mapWidth = 0;
      this.mapLayers = 0;

      ResourceModule.ResourceType[] var4;
      for (ResourceModule.ResourceType t : RESOURCE_TYPES) {
         this.resourcesBarListMain.put(t, false);
      }

      this.resourcesBarListMain.put(ResourceModule.ResourceType.WOOD, true);
      this.resourcesBarListMain.put(ResourceModule.ResourceType.BOARD, true);
      this.resourcesBarListMain.put(ResourceModule.ResourceType.ROCK, true);
      this.resourcesBarListMain.put(ResourceModule.ResourceType.CUT_STONE, true);
      this.resourcesBarListMain.put(ResourceModule.ResourceType.CRYSTAL, true);
      this.resourcesBarListMain.put(ResourceModule.ResourceType.CRYLITHIUM, true);
      this.resourcesBarListMain.put(ResourceModule.ResourceType.RAW_VEGETABLE, true);
      this.resourcesBarListMain.put(ResourceModule.ResourceType.RATION, true);
      this.resourcesBarListMain.put(ResourceModule.ResourceType.RAW_MEAT, true);
      this.resourcesBarListMain.put(ResourceModule.ResourceType.COOKED_MEAT, true);

      for (ResourceModule.ResourceBarType t : ResourceModule.ResourceBarType.values()) {
         this.resourcesBarListRight.put(t, false);
      }

      this.resourcesBarListRight.put(ResourceModule.ResourceBarType.GOLD, true);
      this.resourcesBarListRight.put(ResourceModule.ResourceBarType.WATER, true);
      this.resourcesBarListRight.put(ResourceModule.ResourceBarType.ENERGY, true);
      this.resourcesBarListRight.put(ResourceModule.ResourceBarType.FAITH, true);
      ArrayFillUtil.fill(this.resourceValuesMap, (byte)0);

      for (int x = 0; x < this.resourceTypesMap.length; x++) {
         for (int y = 0; y < this.resourceTypesMap[x].length; y++) {
            this.resourceTypesMap[x][y] = null;
         }
      }

      this.resourcesProvisioner.clear();

      for (ResourceModule.ResourceType t : RESOURCE_TYPES) {
         this.resourcesProvisionerByType.get(t).clear();
      }

      this.resourcesMaster.clear();
      this.resourcesInAir.clear();
      this.resourcesInBuilding.clear();
      this.resourcesOnGround.clear();
      this.farmlandArray.clear();

      for (ResourceModule.ResourceType t : RESOURCE_TYPES) {
         this.resourceGatherRate.put(t, 0);
         this.resourceGatherHistory.get(t).clear();
         this.resourceGatherRateHistory.get(t).clear();
      }

      ArrayFillUtil.fill(this.resourcesOnGroundByteMap, (byte)0);

      for (int x = 0; x < this.resourcesOnGroundMap.length; x++) {
         for (int y = 0; y < this.resourcesOnGroundMap[x].length; y++) {
            this.resourcesOnGroundMap[x][y] = null;
         }
      }

      this.resourceStores = new int[RESOURCE_TYPES.length];
      this.resourceStoresSpace = new int[RESOURCE_TYPES.length];
      HashMap<MapTilesLoader.TileSet, HashMap<ObjectBase.ObjectSubType, ObjectFlags>> flagList = this.object.getObjectFlagFactory().getObjectFlagsList();

      // Reuse each building type's counter array (zeroed) instead of allocating a new one per type every tick.
      for (HashMap<ObjectBase.ObjectSubType, ObjectFlags> o : flagList.values()) {
         MapTilesLoader.TileSet baseType = o.get(ObjectBase.ObjectSubType.BUILT).getBaseType();
         int[] counts = this.resourceStoresSpaceByType.get(baseType);
         if (counts != null && counts.length == RESOURCE_TYPE_COUNT) {
            java.util.Arrays.fill(counts, 0);
         } else {
            this.resourceStoresSpaceByType.put(baseType, new int[RESOURCE_TYPE_COUNT]);
         }
      }

      this.resourceGround = new int[RESOURCE_TYPES.length];
      this.resourceGroundInRange = new int[RESOURCE_TYPES.length];
      this.resourceCarried = new int[RESOURCE_TYPES.length];
      this.resourceEquipped = new int[RESOURCE_TYPES.length];

      ResourceModule.ResourceType[] var5;
      for (ResourceModule.ResourceType t : RESOURCE_TYPES) {
         if (t.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RAW
            && t.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
            && t != ResourceModule.ResourceType.WATER_BUCKET
            && t != ResourceModule.ResourceType.DIRTY_WATER_BUCKET) {
            this.resourceMakeAmounts[t.ordinal()] = 0;
            this.resourceMaintainAmounts[t.ordinal()] = 0;
         } else {
            this.resourceMakeAmounts[t.ordinal()] = 0;
            this.resourceMaintainAmounts[t.ordinal()] = RESOURCE_STORAGE_AMOUNT;
         }
      }

      this.increaseWaterRate = 1000;
      this.increaseWaterRateTick = 0;
      this.totalAvailableGoldCoins = 0;
      this.maximumAvailableGoldCoins = 0;
      this.totalAvailableWater = 0;
      this.maximumAvailableWater = 0;
      this.totalAvailableFood = 0;
      this.maximumAvailableFood = 0;
      this.totalAvailableDirtyWater = 0;
      this.maximumAvailableDirtyWater = 0;
      this.totalAvailableEnergy = 0;
      this.maximumAvailableEnergy = 0;
      this.resourceLastID = 0;
      this.farmlandLastID = 0;
   }

   @Override
   protected void newMainMenu() throws SlickException {
      this.buildResourceMap();
   }

   @Override
   protected void newPlay() throws SlickException {
      this.buildResourceMap();
      if (this.save.getActiveRegionalSave().getGameType() == StateBase.GameType.SKIRMISH) {
         this.createInitialProvisionerStock();
      }

      if (this.save.getActiveRegionalSave().getGameType() == StateBase.GameType.WORLD_MAP && !this.save.getActiveWorldSave().isNewGame()) {
         this.loadWorldSaveData();
      }
   }

   @Override
   protected void loadPlay() throws SlickException {
      this.loadRegionalSaveData();
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
      YMLDataMap dataMap = this.save.getActiveRegionalSave().getAndUnloadModuleSaveData(this.moduleType);
      this.resourceLastID = Integer.parseInt(dataMap.getOrDefault("resourceLastID", 0));

      for (int resourceCount = 0; dataMap.containsKey("resource" + resourceCount); resourceCount++) {
         YMLDataMap thisMap = dataMap.getSubMap("resource" + resourceCount);
         ResourceBase newResource = new ResourceBase(thisMap);
         this.resourcesMaster.add(newResource);
      }

      this.farmlandLastID = Integer.parseInt(dataMap.getOrDefault("farmlandLastID", 0));

      for (int farmlandCount = 0; dataMap.containsKey("farmland" + farmlandCount); farmlandCount++) {
         YMLDataMap thisMap = dataMap.getSubMap("farmland" + farmlandCount);
         FarmlandBase newFarmland = new FarmlandBase(thisMap);
         this.farmlandArray.add(newFarmland);
      }

      if (dataMap.containsMap("resourcesBarList")) {
         Console.saveConverterOut(this, "Update 1c", "Update 2 Unstable 2", "Converting old resourcesBarList.");
         YMLDataMap resourcesBarListMainDataMap = dataMap.getSubMap("resourcesBarList");

         ResourceModule.ResourceType[] newResource;
         for (ResourceModule.ResourceType t : newResource = RESOURCE_TYPES) {
            this.resourcesBarListMain.put(t, Boolean.parseBoolean(resourcesBarListMainDataMap.getOrDefault(t.toString(), false)));
         }
      }

      if (dataMap.containsMap("resourcesBarListMain")) {
         YMLDataMap resourcesBarListMainDataMap = dataMap.getSubMap("resourcesBarListMain");

         ResourceModule.ResourceType[] var40;
         for (ResourceModule.ResourceType t : RESOURCE_TYPES) {
            this.resourcesBarListMain.put(t, Boolean.parseBoolean(resourcesBarListMainDataMap.getOrDefault(t.toString(), false)));
         }
      }

      if (dataMap.containsMap("resourcesBarListRight")) {
         YMLDataMap resourcesBarListRightDataMap = dataMap.getSubMap("resourcesBarListRight");

         ResourceModule.ResourceBarType[] var41;
         for (ResourceModule.ResourceBarType t : ResourceModule.ResourceBarType.values()) {
            this.resourcesBarListRight.put(t, Boolean.parseBoolean(resourcesBarListRightDataMap.getOrDefault(t.toString(), false)));
         }
      }

      if (dataMap.containsMap("resourceValuesMap")) {
         Console.saveConverterOut(this, "InDev 33f", "InDev 34 Unstable 1", "Converting old resourceValuesMap.");
         YMLDataMap resourceValuesMapDataMap = dataMap.getSubMap("resourceValuesMap");

         for (int count = 0; resourceValuesMapDataMap.containsKey(Integer.toString(count)); count++) {
            YMLDataMap thisMap = resourceValuesMapDataMap.getSubMap(Integer.toString(count));
            OrderedPair o = resourceValuesMapDataMap.unpackCoordinates(Integer.toString(count));
            int x = o.getX();
            int y = o.getY();
            byte value = Byte.parseByte(thisMap.getOrDefault("value", 0));
            this.resourceValuesMap[x][y] = value;

            for (int l = 0; l < this.mapLayers; l++) {
               int tileID = this.map.getTileId(x, y, l);
               if (tileID != 0) {
                  ResourceModule.ResourceType resourceType = this.map.getMapTileLoader().getTileResourceType(tileID);
                  if (resourceType != null) {
                     this.resourceTypesMap[x][y] = resourceType;
                     break;
                  }
               }
            }
         }
      } else if (dataMap.containsKey("resourceValuesMap")) {
         dataMap.unpackCoordinatesListWithValueToArray("resourceValuesMap", this.resourceValuesMap);

         for (int x = 0; x < this.resourceValuesMap.length; x++) {
            for (int y = 0; y < this.resourceValuesMap[x].length; y++) {
               for (int lx = 0; lx < this.mapLayers; lx++) {
                  int tileID = this.map.getTileId(x, y, lx);
                  if (tileID != 0) {
                     ResourceModule.ResourceType resourceType = this.map.getMapTileLoader().getTileResourceType(tileID);
                     if (resourceType != null) {
                        this.resourceTypesMap[x][y] = resourceType;
                        break;
                     }
                  }
               }
            }
         }
      }

      this.increaseWaterRate = Integer.parseInt(dataMap.getOrDefault("increaseWaterRate", 1000));
      this.increaseWaterRateTick = Integer.parseInt(dataMap.getOrDefault("increaseWaterRateTick", 0));
      YMLDataMap makeAmountsMap = dataMap.getSubMap("resourceMakeAmounts");
      YMLDataMap maintainAmountsMap = dataMap.getSubMap("resourceMaintainAmounts");

      ResourceModule.ResourceType[] var46;
      for (ResourceModule.ResourceType t : RESOURCE_TYPES) {
         this.resourceMakeAmounts[t.ordinal()] = Integer.parseInt(makeAmountsMap.getOrDefault(t.toString(), "0"));
         this.resourceMaintainAmounts[t.ordinal()] = Integer.parseInt(maintainAmountsMap.getOrDefault(t.toString(), "0"));
      }

      if (makeAmountsMap.containsKey("RAW_FOOD")) {
         Console.saveConverterOut(this, "InDev 33f", "InDev 34b", "Converting RAW_FOOD to RAW_VEGETABLE");
         this.resourceMakeAmounts[ResourceModule.ResourceType.RAW_VEGETABLE.ordinal()] = Integer.parseInt(makeAmountsMap.getOrDefault("RAW_FOOD", "0"));
         this.resourceMaintainAmounts[ResourceModule.ResourceType.RAW_VEGETABLE.ordinal()] = Integer.parseInt(maintainAmountsMap.getOrDefault("RAW_FOOD", "0"));
      }

      if (makeAmountsMap.containsKey("WOOD_SHIELD")) {
         Console.saveConverterOut(this, "InDev 33f", "InDev 34b", "Converting WOOD_SHIELD to LEATHER_SHIELD");
         this.resourceMakeAmounts[ResourceModule.ResourceType.LEATHER_SHIELD.ordinal()] = Integer.parseInt(makeAmountsMap.getOrDefault("WOOD_SHIELD", "0"));
         this.resourceMaintainAmounts[ResourceModule.ResourceType.LEATHER_SHIELD.ordinal()] = Integer.parseInt(
            maintainAmountsMap.getOrDefault("WOOD_SHIELD", "0")
         );
      }

      if (this.save.getActiveRegionalSave().getGameType() == StateBase.GameType.SKIRMISH) {
         for (int resourceProvisionerCount = 0; dataMap.containsKey("resourceProvisioner" + resourceProvisionerCount); resourceProvisionerCount++) {
            YMLDataMap thisMap = dataMap.getSubMap("resourceProvisioner" + resourceProvisionerCount);
            ResourceBase newResource = new ResourceBase(thisMap);
            this.resourcesProvisioner.add(newResource);
            this.resourcesProvisionerByType.get(newResource.getType()).add(newResource);
         }
      }
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
      YMLDataMap dataMap = this.save.getActiveWorldSave().getModuleSaveData(this.moduleType);

      for (int count = 0; dataMap.containsKey("resource" + count); count++) {
         ResourceBase newResource = new ResourceBase(dataMap.getSubMap("resource" + count));
         this.resourcesProvisioner.add(newResource);
         this.resourcesProvisionerByType.get(newResource.getType()).add(newResource);
      }
   }

   @Override
   public void initLoadSync() {
      this.synced = true;

      for (int x = 0; x < this.resourcesMaster.size(); x++) {
         this.resourcesMaster.get(x).loadSync();
      }

      for (int x = 0; x < this.farmlandArray.size(); x++) {
         this.farmlandArray.get(x).loadSync();
      }

      HashMap<MapTilesLoader.TileSet, HashMap<ObjectBase.ObjectSubType, ObjectFlags>> flagList = this.object.getObjectFlagFactory().getObjectFlagsList();

      // Reuse each building type's counter array (zeroed) instead of allocating a new one per type every tick.
      for (HashMap<ObjectBase.ObjectSubType, ObjectFlags> o : flagList.values()) {
         MapTilesLoader.TileSet baseType = o.get(ObjectBase.ObjectSubType.BUILT).getBaseType();
         int[] counts = this.resourceStoresSpaceByType.get(baseType);
         if (counts != null && counts.length == RESOURCE_TYPE_COUNT) {
            java.util.Arrays.fill(counts, 0);
         } else {
            this.resourceStoresSpaceByType.put(baseType, new int[RESOURCE_TYPE_COUNT]);
         }
      }

      this.updateResourceCount();
   }

   @Override
   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("version", Game.getVersion());
      if (this.resourcesMaster.size() == 0) {
         dataMap.put("resourceLastID", 0);
      } else {
         int count = 0;

         for (ResourceBase r : this.resourcesMaster) {
            dataMap.put("resource" + count, r.getRegionalSaveData());
            count++;
         }

         dataMap.put("resourceLastID", this.resourceLastID);
      }

      if (this.farmlandArray.size() == 0) {
         dataMap.put("farmlandLastID", 0);
      } else {
         int count = 0;

         for (FarmlandBase f : this.farmlandArray) {
            dataMap.put("farmland" + count, f.getRegionalSaveData());
            count++;
         }

         dataMap.put("farmlandLastID", this.farmlandLastID);
      }

      Map<String, Object> resourcesBarListMainDataMap = new HashMap<>();

      ResourceModule.ResourceType[] makeAmountsMap;
      for (ResourceModule.ResourceType t : makeAmountsMap = RESOURCE_TYPES) {
         resourcesBarListMainDataMap.put(t.toString(), this.resourcesBarListMain.get(t));
      }

      dataMap.put("resourcesBarListMain", resourcesBarListMainDataMap);
      Map<String, Object> resourcesBarListRightDataMap = new HashMap<>();

      ResourceModule.ResourceBarType[] maintainAmountsMap;
      for (ResourceModule.ResourceBarType t : maintainAmountsMap = ResourceModule.ResourceBarType.values()) {
         resourcesBarListRightDataMap.put(t.toString(), this.resourcesBarListRight.get(t));
      }

      dataMap.put("resourcesBarListRight", resourcesBarListRightDataMap);
      ArrayList<OrderedPair> coordinatesList = new ArrayList<>();
      ArrayList<Integer> valuesList = new ArrayList<>();

      for (int x = 0; x < this.resourceValuesMap.length; x++) {
         for (int y = 0; y < this.resourceValuesMap[x].length; y++) {
            if (this.resourceValuesMap[x][y] != 0) {
               coordinatesList.add(OrderedPair.getOrderedPair(x, y));
               valuesList.add(Integer.valueOf(this.resourceValuesMap[x][y]));
            }
         }
      }

      if (coordinatesList.size() > 0) {
         String resourceValuesMapString = dataMap.packCoordinatesListWithValue(coordinatesList, valuesList);
         dataMap.put("resourceValuesMap", resourceValuesMapString);
      }

      dataMap.put("increaseWaterRate", this.increaseWaterRate);
      dataMap.put("increaseWaterRateTick", this.increaseWaterRateTick);
      YMLDataMap makeAmountsMapx = new YMLDataMap();
      YMLDataMap maintainAmountsMapx = new YMLDataMap();

      ResourceModule.ResourceType[] var11;
      for (ResourceModule.ResourceType t : RESOURCE_TYPES) {
         makeAmountsMapx.put(t.toString(), this.resourceMakeAmounts[t.ordinal()]);
         maintainAmountsMapx.put(t.toString(), this.resourceMaintainAmounts[t.ordinal()]);
      }

      dataMap.put("resourceMakeAmounts", makeAmountsMapx);
      dataMap.put("resourceMaintainAmounts", maintainAmountsMapx);
      if (this.save.getActiveRegionalSave().getGameType() == StateBase.GameType.SKIRMISH && this.resourcesProvisioner.size() != 0) {
         int resourceProvisionerCount = 0;

         for (ResourceBase r : this.resourcesProvisioner) {
            dataMap.put("resourceProvisioner" + resourceProvisionerCount, r.getSkirmishProvisionerRegionalProperties(resourceProvisionerCount));
            resourceProvisionerCount++;
         }
      }

      return dataMap;
   }

   @Override
   public YMLDataMap getWorldMapSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("version", Game.getVersion());
      if (this.resourcesProvisioner.size() != 0) {
         int count = 0;

         for (ResourceBase r : this.resourcesProvisioner) {
            dataMap.put("resource" + count, r.getRegionalSaveData());
            count++;
         }
      }

      return dataMap;
   }

   @Override
   public void resetWorldSaveData() throws SlickException {
      this.resetModule();
      this.resource.createInitialProvisionerStock();
   }

   private void loadResourceColorMap() {
      this.resourceColorMap = new EnumMap<>(ResourceModule.ResourceType.class);

      ResourceModule.ResourceType[] var4;
      for (ResourceModule.ResourceType t : RESOURCE_TYPES) {
         this.resourceColorMap.put(t, new EnumMap<>(ResourceModule.ResourceColorSet.class));
      }

      this.resourceColorMap.get(ResourceModule.ResourceType.WOOD).put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.WOOD_BROWN);
      this.resourceColorMap.get(ResourceModule.ResourceType.WOOD).put(ResourceModule.ResourceColorSet.BROWN, ResourceModule.ResourceSpriteSet.WOOD_BROWN);
      this.resourceColorMap.get(ResourceModule.ResourceType.WOOD).put(ResourceModule.ResourceColorSet.RED, ResourceModule.ResourceSpriteSet.WOOD_RED);
      this.resourceColorMap.get(ResourceModule.ResourceType.WOOD).put(ResourceModule.ResourceColorSet.GRAY, ResourceModule.ResourceSpriteSet.WOOD_GRAY);
      this.resourceColorMap.get(ResourceModule.ResourceType.WOOD).put(ResourceModule.ResourceColorSet.GREEN, ResourceModule.ResourceSpriteSet.WOOD_GREEN);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.WOOD)
         .put(ResourceModule.ResourceColorSet.DARK_BROWN, ResourceModule.ResourceSpriteSet.WOOD_DARK_BROWN);
      this.resourceColorMap.get(ResourceModule.ResourceType.WOOD).put(ResourceModule.ResourceColorSet.BROWN, ResourceModule.ResourceSpriteSet.WOOD_BROWN);
      this.resourceColorMap.get(ResourceModule.ResourceType.WOOD).put(ResourceModule.ResourceColorSet.BLUE, ResourceModule.ResourceSpriteSet.WOOD_BLUE);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.WOOD)
         .put(ResourceModule.ResourceColorSet.LIGHT_GRAY, ResourceModule.ResourceSpriteSet.WOOD_LIGHT_GRAY);
      this.resourceColorMap.get(ResourceModule.ResourceType.BOARD).put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.BOARD_BROWN);
      this.resourceColorMap.get(ResourceModule.ResourceType.BOARD).put(ResourceModule.ResourceColorSet.BROWN, ResourceModule.ResourceSpriteSet.BOARD_BROWN);
      this.resourceColorMap.get(ResourceModule.ResourceType.BOARD).put(ResourceModule.ResourceColorSet.RED, ResourceModule.ResourceSpriteSet.BOARD_RED);
      this.resourceColorMap.get(ResourceModule.ResourceType.BOARD).put(ResourceModule.ResourceColorSet.GRAY, ResourceModule.ResourceSpriteSet.BOARD_GRAY);
      this.resourceColorMap.get(ResourceModule.ResourceType.BOARD).put(ResourceModule.ResourceColorSet.GREEN, ResourceModule.ResourceSpriteSet.BOARD_GREEN);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.BOARD)
         .put(ResourceModule.ResourceColorSet.DARK_BROWN, ResourceModule.ResourceSpriteSet.BOARD_DARK_BROWN);
      this.resourceColorMap.get(ResourceModule.ResourceType.BOARD).put(ResourceModule.ResourceColorSet.BROWN, ResourceModule.ResourceSpriteSet.BOARD_BROWN);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.BOARD)
         .put(ResourceModule.ResourceColorSet.LIGHT_GRAY, ResourceModule.ResourceSpriteSet.BOARD_LIGHT_GRAY);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.WOODY_TRASH)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.WOODY_TRASH_BROWN);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.WOODY_TRASH)
         .put(ResourceModule.ResourceColorSet.BROWN, ResourceModule.ResourceSpriteSet.WOODY_TRASH_BROWN);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.WOODY_TRASH)
         .put(ResourceModule.ResourceColorSet.RED, ResourceModule.ResourceSpriteSet.WOODY_TRASH_RED);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.WOODY_TRASH)
         .put(ResourceModule.ResourceColorSet.GRAY, ResourceModule.ResourceSpriteSet.WOODY_TRASH_GRAY);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.WOODY_TRASH)
         .put(ResourceModule.ResourceColorSet.GREEN, ResourceModule.ResourceSpriteSet.WOODY_TRASH_GREEN);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.WOODY_TRASH)
         .put(ResourceModule.ResourceColorSet.DARK_BROWN, ResourceModule.ResourceSpriteSet.WOODY_TRASH_DARK_BROWN);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.WOODY_TRASH)
         .put(ResourceModule.ResourceColorSet.BROWN, ResourceModule.ResourceSpriteSet.WOODY_TRASH_BROWN);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.WOODY_TRASH)
         .put(ResourceModule.ResourceColorSet.LIGHT_GRAY, ResourceModule.ResourceSpriteSet.WOODY_TRASH_LIGHT_GRAY);
      this.resourceColorMap.get(ResourceModule.ResourceType.ROCK).put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.ROCK_GRAY);
      this.resourceColorMap.get(ResourceModule.ResourceType.ROCK).put(ResourceModule.ResourceColorSet.WHITE, ResourceModule.ResourceSpriteSet.ROCK_WHITE);
      this.resourceColorMap.get(ResourceModule.ResourceType.ROCK).put(ResourceModule.ResourceColorSet.GRAY, ResourceModule.ResourceSpriteSet.ROCK_GRAY);
      this.resourceColorMap.get(ResourceModule.ResourceType.ROCK).put(ResourceModule.ResourceColorSet.BLACK, ResourceModule.ResourceSpriteSet.ROCK_BLACK);
      this.resourceColorMap.get(ResourceModule.ResourceType.ROCK).put(ResourceModule.ResourceColorSet.BROWN, ResourceModule.ResourceSpriteSet.ROCK_BROWN);
      this.resourceColorMap.get(ResourceModule.ResourceType.ROCK).put(ResourceModule.ResourceColorSet.RED, ResourceModule.ResourceSpriteSet.ROCK_RED);
      this.resourceColorMap.get(ResourceModule.ResourceType.ROCK).put(ResourceModule.ResourceColorSet.TAN, ResourceModule.ResourceSpriteSet.ROCK_TAN);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.CUT_STONE)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.STONE_GRAY);
      this.resourceColorMap.get(ResourceModule.ResourceType.CUT_STONE).put(ResourceModule.ResourceColorSet.WHITE, ResourceModule.ResourceSpriteSet.STONE_WHITE);
      this.resourceColorMap.get(ResourceModule.ResourceType.CUT_STONE).put(ResourceModule.ResourceColorSet.GRAY, ResourceModule.ResourceSpriteSet.STONE_GRAY);
      this.resourceColorMap.get(ResourceModule.ResourceType.CUT_STONE).put(ResourceModule.ResourceColorSet.BLACK, ResourceModule.ResourceSpriteSet.STONE_BLACK);
      this.resourceColorMap.get(ResourceModule.ResourceType.CUT_STONE).put(ResourceModule.ResourceColorSet.BROWN, ResourceModule.ResourceSpriteSet.STONE_BROWN);
      this.resourceColorMap.get(ResourceModule.ResourceType.CUT_STONE).put(ResourceModule.ResourceColorSet.RED, ResourceModule.ResourceSpriteSet.STONE_RED);
      this.resourceColorMap.get(ResourceModule.ResourceType.CUT_STONE).put(ResourceModule.ResourceColorSet.TAN, ResourceModule.ResourceSpriteSet.STONE_TAN);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.ROCKY_TRASH)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.ROCKY_TRASH_GRAY);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.ROCKY_TRASH)
         .put(ResourceModule.ResourceColorSet.WHITE, ResourceModule.ResourceSpriteSet.ROCKY_TRASH_WHITE);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.ROCKY_TRASH)
         .put(ResourceModule.ResourceColorSet.GRAY, ResourceModule.ResourceSpriteSet.ROCKY_TRASH_GRAY);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.ROCKY_TRASH)
         .put(ResourceModule.ResourceColorSet.BLACK, ResourceModule.ResourceSpriteSet.ROCKY_TRASH_BLACK);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.ROCKY_TRASH)
         .put(ResourceModule.ResourceColorSet.BROWN, ResourceModule.ResourceSpriteSet.ROCKY_TRASH_BROWN);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.ROCKY_TRASH)
         .put(ResourceModule.ResourceColorSet.RED, ResourceModule.ResourceSpriteSet.ROCKY_TRASH_RED);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.ROCKY_TRASH)
         .put(ResourceModule.ResourceColorSet.TAN, ResourceModule.ResourceSpriteSet.ROCKY_TRASH_TAN);
      this.resourceColorMap.get(ResourceModule.ResourceType.IRON_ORE).put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.IRON_ORE);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.IRON_INGOT)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.IRON_INGOT);
      this.resourceColorMap.get(ResourceModule.ResourceType.GOLD_ORE).put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.GOLD_ORE);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.GOLD_INGOT)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.GOLD_INGOT);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.GOLD_COIN_SACK)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.GOLD_SACK);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.CRYSTAL)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.CRYSTAL_BLUE);
      this.resourceColorMap.get(ResourceModule.ResourceType.CRYSTAL).put(ResourceModule.ResourceColorSet.BLUE, ResourceModule.ResourceSpriteSet.CRYSTAL_BLUE);
      this.resourceColorMap.get(ResourceModule.ResourceType.CRYSTAL).put(ResourceModule.ResourceColorSet.RED, ResourceModule.ResourceSpriteSet.CRYSTAL_RED);
      this.resourceColorMap.get(ResourceModule.ResourceType.CRYSTAL).put(ResourceModule.ResourceColorSet.GREEN, ResourceModule.ResourceSpriteSet.CRYSTAL_GREEN);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.CRYSTAL)
         .put(ResourceModule.ResourceColorSet.PURPLE, ResourceModule.ResourceSpriteSet.CRYSTAL_PURPLE);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.CRYSTALLY_TRASH)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.CRYSTALLY_TRASH_RED);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.CRYSTALLY_TRASH)
         .put(ResourceModule.ResourceColorSet.RED, ResourceModule.ResourceSpriteSet.CRYSTALLY_TRASH_RED);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.CRYSTALLY_TRASH)
         .put(ResourceModule.ResourceColorSet.GREEN, ResourceModule.ResourceSpriteSet.CRYSTALLY_TRASH_GREEN);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.CRYSTALLY_TRASH)
         .put(ResourceModule.ResourceColorSet.BLUE, ResourceModule.ResourceSpriteSet.CRYSTALLY_TRASH_BLUE);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.CRYSTALLY_TRASH)
         .put(ResourceModule.ResourceColorSet.PURPLE, ResourceModule.ResourceSpriteSet.CRYSTALLY_TRASH_PURPLE);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.CRYLITHIUM)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.CRYLITHIUM_BLUE);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.CRYLITHIUM)
         .put(ResourceModule.ResourceColorSet.BLUE, ResourceModule.ResourceSpriteSet.CRYLITHIUM_BLUE);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.CRYLITHIUM)
         .put(ResourceModule.ResourceColorSet.RED, ResourceModule.ResourceSpriteSet.CRYLITHIUM_RED);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.CRYLITHIUM)
         .put(ResourceModule.ResourceColorSet.GREEN, ResourceModule.ResourceSpriteSet.CRYLITHIUM_GREEN);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.CRYLITHIUM)
         .put(ResourceModule.ResourceColorSet.PURPLE, ResourceModule.ResourceSpriteSet.CRYLITHIUM_PURPLE);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.RAW_VEGETABLE)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.CARROT_YELLOW);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.RAW_VEGETABLE)
         .put(ResourceModule.ResourceColorSet.YELLOW, ResourceModule.ResourceSpriteSet.CARROT_YELLOW);
      this.resourceColorMap.get(ResourceModule.ResourceType.RAW_VEGETABLE).put(ResourceModule.ResourceColorSet.BROWN, ResourceModule.ResourceSpriteSet.POTATO);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.RAW_VEGETABLE)
         .put(ResourceModule.ResourceColorSet.WHITE, ResourceModule.ResourceSpriteSet.POTATO_HOLY);
      this.resourceColorMap.get(ResourceModule.ResourceType.RAW_VEGETABLE).put(ResourceModule.ResourceColorSet.PURPLE, ResourceModule.ResourceSpriteSet.TURNIP);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.RAW_VEGETABLE)
         .put(ResourceModule.ResourceColorSet.GREEN, ResourceModule.ResourceSpriteSet.CACTUS_GREEN);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.RAW_VEGETABLE)
         .put(ResourceModule.ResourceColorSet.RED, ResourceModule.ResourceSpriteSet.MELONS_YELLOW);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.RAW_VEGETABLE)
         .put(ResourceModule.ResourceColorSet.DARK_BROWN, ResourceModule.ResourceSpriteSet.MUSHROOMS);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.ORGANICY_TRASH)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.ORGANICY_TRASH);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.BALLISTAE_BOLTS)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.ARROW_BUNDLE);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.STONE_BALLS)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.STONE_BALLS);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.IRON_SWORD)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.IRON_SWORD);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.WOOD_SWORD)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.WOOD_SWORD);
      this.resourceColorMap.get(ResourceModule.ResourceType.PICK_AXE).put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.PICK_AXE);
      this.resourceColorMap.get(ResourceModule.ResourceType.AXE).put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.AXE);
      this.resourceColorMap.get(ResourceModule.ResourceType.HOE).put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.HOE);
      this.resourceColorMap.get(ResourceModule.ResourceType.HAMMER).put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.HAMMER);
      this.resourceColorMap.get(ResourceModule.ResourceType.SHOVEL).put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.SHOVEL);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.LEATHER_HELMET)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.LEATHER_HELMET);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.LEATHER_CHEST_ARMOR);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.IRON_HELMET)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.IRON_HELMET);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.IRON_CHEST_ARMOR)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.IRON_CHEST_ARMOR);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.LEATHER_SHIELD)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.WOOD_SHIELD);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.IRON_SHIELD)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.IRON_SHIELD);
      this.resourceColorMap.get(ResourceModule.ResourceType.BOW).put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.BOW);
      this.resourceColorMap.get(ResourceModule.ResourceType.QUIVER).put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.QUIVER);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.HEALING_POTION)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.HEALING_POTION);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.WATER_BUCKET)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.WATER_BUCKET);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.DIRTY_WATER_BUCKET)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.DIRTY_WATER_BUCKET);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.WATER_BOTTLE)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.WATER_BOTTLE);
      this.resourceColorMap.get(ResourceModule.ResourceType.SILK).put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.SILK);
      this.resourceColorMap.get(ResourceModule.ResourceType.BANDAGE).put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.BANDAGE);
      this.resourceColorMap.get(ResourceModule.ResourceType.MEDKIT).put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.MEDKIT);
      this.resourceColorMap.get(ResourceModule.ResourceType.RATION).put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.RATION);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.SUSPICICIOUS_KEY)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.SUSPICICIOUS_KEY);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.SUSPICICIOUSY_TRASH);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.TRASHY_TRASH)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.TRASHY_TRASH);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.TRASHY_CUBE)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.TRASHY_CUBE);
      this.resourceColorMap.get(ResourceModule.ResourceType.RAW_MEAT).put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.RAW_MEAT);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.COOKED_MEAT)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.COOKED_MEAT);
      this.resourceColorMap.get(ResourceModule.ResourceType.FEATHER).put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.FEATHER);
      this.resourceColorMap.get(ResourceModule.ResourceType.RAW_HIDE).put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.RAW_HIDE);
      this.resourceColorMap.get(ResourceModule.ResourceType.EGG).put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.EGG);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.BOILED_EGG)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.BOILED_EGG);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.CAPTURED_ANIMAL)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.CAPTURED_ANIMAL);
      this.resourceColorMap.get(ResourceModule.ResourceType.GOD_DUST).put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.GOD_DUST);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.GHOST_DUST)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.GHOST_DUST);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.EMPTY_EERIE_VESSEL)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.EMPTY_EERIE_VESSEL);
      this.resourceColorMap
         .get(ResourceModule.ResourceType.EERIE_VESSEL)
         .put(ResourceModule.ResourceColorSet.DEFAULT, ResourceModule.ResourceSpriteSet.EERIE_VESSEL);
   }

   private void loadResourceRares() {
      this.resourceRares = new EnumMap<>(ResourceModule.ResourceType.class);
      this.resourceRaresWeight = new EnumMap<>(ResourceModule.ResourceType.class);

      ResourceModule.ResourceType[] var4;
      for (ResourceModule.ResourceType t : RESOURCE_TYPES) {
         this.resourceRares.put(t, new ArrayList<>());
         this.resourceRaresWeight.put(t, 0);
      }

      this.resourceRares.get(ResourceModule.ResourceType.ROCK).add(ResourceModule.ResourceType.IRON_ORE);
      this.resourceRaresWeight.put(ResourceModule.ResourceType.IRON_ORE, 10);
      this.resourceRares.get(ResourceModule.ResourceType.ROCK).add(ResourceModule.ResourceType.GOLD_ORE);
      this.resourceRaresWeight.put(ResourceModule.ResourceType.GOLD_ORE, 100);
      this.resourceRares.get(ResourceModule.ResourceType.WOOD).add(ResourceModule.ResourceType.SILK);
      this.resourceRaresWeight.put(ResourceModule.ResourceType.SILK, 50);
   }

   public void createInitialProvisionerStock() {
      Console.out("Creating new provisioner stock.", true);
      EnumMap<ResourceModule.ResourceType, Integer> allowedTypes = new EnumMap<>(ResourceModule.ResourceType.class);
      this.resourcesProvisioner.clear();

      ResourceModule.ResourceType[] var5;
      for (ResourceModule.ResourceType t : RESOURCE_TYPES) {
         this.resourcesProvisionerByType.get(t).clear();
      }

      for (ResourceModule.ResourceType t : RESOURCE_TYPES) {
         allowedTypes.put(t, 50);
      }

      allowedTypes.put(ResourceModule.ResourceType.WOOD, 10);
      allowedTypes.put(ResourceModule.ResourceType.ROCK, 10);
      allowedTypes.put(ResourceModule.ResourceType.BOARD, 10);
      allowedTypes.put(ResourceModule.ResourceType.CUT_STONE, 10);
      allowedTypes.put(ResourceModule.ResourceType.SUSPICICIOUS_KEY, 300);

      for (int x = 0; x < 5000; x++) {
         boolean foundType = false;

         while (!foundType) {
            ResourceModule.ResourceType type = RESOURCE_TYPES[Utilities.randomInt(RESOURCE_TYPES.length)];
            if (allowedTypes.containsKey(type) && Utilities.randomInt(allowedTypes.get(type)) == 0) {
               foundType = true;
               this.resource.createResourceProvisioner(type);
            }
         }
      }
   }

   public void buildResourceMap() throws SlickException {
      if (this.resourceValuesMap == null) {
         this.resourceValuesMap = new byte[this.mapWidth][this.mapHeight];
      }

      if (this.resourceTypesMap == null) {
         this.resourceTypesMap = new ResourceModule.ResourceType[this.mapWidth][this.mapHeight];
      }

      for (int x = 0; x < this.mapWidth; x++) {
         for (int y = 0; y < this.mapHeight; y++) {
            for (int l = 0; l < this.mapLayers; l++) {
               if (this.map.getTileId(x, y, l) != 0) {
                  int tileID = this.map.getTileId(x, y, l);
                  ResourceModule.ResourceType type = this.map.getMapTileLoader().getTileResourceType(tileID);
                  byte amount = this.map.getMapTileLoader().getTileResourceAmount(tileID);
                  byte amountMax = this.map.getMapTileLoader().getTileResourceAmountMax(tileID);
                  if (type != null) {
                     this.addResourcesToTile(x, y, type, amount, amountMax);
                  }
               }
            }
         }
      }

      this.waterBalancer.rebalanceAllWater(this.resourceValuesMap);
   }

   private void addResourcesToTile(int x, int y, ResourceModule.ResourceType type, byte amount, byte amountMax) {
      if (amount > 0) {
         this.resourceValuesMap[x][y] = (byte)(this.resourceValuesMap[x][y] + Utilities.randomInt(amount / 2) + amount);
      }

      if (this.resourceValuesMap[x][y] > amountMax) {
         this.resourceValuesMap[x][y] = amountMax;
      }

      this.resourceTypesMap[x][y] = type;
   }

   public ResourceBase harvestResource(int tileX, int tileY, int trashChance) throws SlickException {
      ResourceBase resourceOut = null;

      for (int l = 0; l < this.mapLayers; l++) {
         int tileID = this.map.getTileId(tileX, tileY, l);
         if (tileID != 0) {
            ResourceModule.ResourceType resourceType = this.map.getMapTileLoader().getTileResourceType(tileID);
            if (resourceType != null) {
               ResourceModule.ResourceType resourceTypeGenerated = this.map.getMapTileLoader().getTileResourceType(tileID);
               if (this.resourceRares.get(resourceType).size() > 0) {
                  for (ResourceModule.ResourceType t : this.resourceRares.get(resourceType)) {
                     if (Utilities.randomInt(this.resourceRaresWeight.get(t)) == 0) {
                        resourceTypeGenerated = t;
                        break;
                     }
                  }
               } else {
                  resourceTypeGenerated = resourceType;
               }

               ResourceModule.ResourceColorSet color = this.map.getMapTileLoader().getTileResourceColorSet(tileID);
               if (trashChance <= 0
                  || Utilities.randomInt(trashChance) != 0
                  || resourceTypeGenerated.getTemplate().getTrashResource() == null
                  || resourceTypeGenerated.getTemplate().alwaysReturnResource()) {
                  resourceOut = this.createResourceUnassigned(resourceTypeGenerated, color);
               } else if (Utilities.randomInt(50) == 0) {
                  resourceOut = this.createResourceUnassigned(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH, color);
               } else {
                  resourceOut = this.createResourceUnassigned(resourceTypeGenerated.getTemplate().getTrashResource(), color);
               }

               int essenceAmount = this.map.getMapTileLoader().getTileEssenceAmount(tileID);
               if (essenceAmount > 0) {
                  this.essence.newEssence(tileX * 16 + 8, tileY * 16 + 8, Utilities.randomInt(essenceAmount) + 1, 1);
               }
               break;
            }
         }
      }

      if (resourceOut != null) {
         this.font.newFloater("-1", tileX * 16 + 4, tileY * 16 + 4);
         if (this.resourceValuesMap[tileX][tileY] > 0) {
            this.resourceValuesMap[tileX][tileY]--;
            if (this.resourceValuesMap[tileX][tileY] <= 0 && this.resourceTypesMap[tileX][tileY] != ResourceModule.ResourceType.DIRTY_WATER_BUCKET) {
               this.map.consumeResourceTerrain(tileX, tileY);
               this.lootBox.attemptLootBoxUncover(tileX, tileY);
               this.resourceTypesMap[tileX][tileY] = null;
            }
         }

         if (resourceOut.getType() == ResourceModule.ResourceType.DIRTY_WATER_BUCKET) {
            this.waterBalancer.rebalanceWaterAt(this.resourceValuesMap, tileX, tileY);
         }
      }

      return resourceOut;
   }

   public ResourceBase grabResource(int x, int y) throws SlickException {
      ResourceBase itemOut = null;
      boolean foundWater = false;

      for (int l = 0; l < this.mapLayers; l++) {
         int tileID = this.map.getTileId(x, y, l);
         ResourceModule.ResourceType resource = this.map.getMapTileLoader().getTileResourceType(tileID);
         ResourceModule.ResourceColorSet color = this.map.getMapTileLoader().getTileResourceColorSet(tileID);
         if (tileID > 0 && resource != null) {
            itemOut = this.createResourceUnassigned(resource, color);
            int essenceAmount = this.map.getMapTileLoader().getTileEssenceAmount(tileID);
            if (essenceAmount > 0) {
               this.essence.newEssence(x * 16 + 8, y * 16 + 8, Utilities.randomInt(essenceAmount) + 1, 1);
            }
            break;
         }
      }

      this.font.newFloater("-1", x * 16 + 4, y * 16 + 4);
      if (this.resourceValuesMap[x][y] > 0) {
         this.resourceValuesMap[x][y]--;
         if (this.resourceValuesMap[x][y] <= 0 && this.resourceTypesMap[x][y] != ResourceModule.ResourceType.DIRTY_WATER_BUCKET) {
            this.map.consumeResourceTerrain(x, y);
            this.resourceTypesMap[x][y] = null;
         } else if (this.resourceTypesMap[x][y] == ResourceModule.ResourceType.DIRTY_WATER_BUCKET) {
            foundWater = true;
         }
      }

      if (foundWater) {
         this.waterBalancer.flagWaterRebalance(x, y);
      }

      return itemOut;
   }

   public void damageResource(int x, int y) throws SlickException {
      if (this.resourceTypesMap[x][y] != ResourceModule.ResourceType.DIRTY_WATER_BUCKET) {
         if (this.resourceValuesMap[x][y] > 0) {
            for (int l = 0; l < this.mapLayers; l++) {
               int tileID = this.map.getTileId(x, y, l);
               if (this.map.getMapTileLoader().getTileEssenceAmount(tileID) > 0) {
                  int essenceAmount = this.map.getMapTileLoader().getTileEssenceAmount(tileID);
                  if (essenceAmount > 0) {
                     this.essence.newEssence(x * 16 + 8, y * 16 + 8, Utilities.randomInt(essenceAmount) + 1, 1);
                  }
                  break;
               }
            }

            this.resourceValuesMap[x][y]--;
            if (this.resourceValuesMap[x][y] <= 0) {
               this.map.consumeResourceTerrain(x, y);
               this.resourceTypesMap[x][y] = null;
               return;
            }
         }

         if (this.resourceValuesMap[x][y] <= 0) {
            boolean canErase = false;

            for (int lx = 7; lx >= 6; lx--) {
               int tileID = this.map.getTileId(x, y, lx);
               if (tileID != 0) {
                  canErase = true;
               }

               if (this.map.getMapTileLoader().isTileInvulnerable(tileID)) {
                  canErase = false;
                  break;
               }
            }

            if (canErase) {
               this.map.erase(x, y, MapModule.EraseMode.TOPOGRAPHY, 1, BrushLoader.BrushType.ROUND);
            }

            if (!canErase) {
               for (int lx = 10; lx >= 9; lx--) {
                  int tileIDx = this.map.getTileId(x, y, lx);
                  if (tileIDx != 0) {
                     canErase = true;
                  }

                  if (this.map.getMapTileLoader().isTileInvulnerable(tileIDx)) {
                     canErase = false;
                     break;
                  }
               }

               if (canErase) {
                  this.map.erase(x, y, MapModule.EraseMode.TOPOGRAPHY, 1, BrushLoader.BrushType.ROUND);
               }
            }
         }
      }
   }

   public ResourceBase harvestCrop(int x, int y) throws SlickException {
      for (int i = 0; i < this.farmlandArray.size(); i++) {
         if (this.farmlandArray.get(i).getX() == x && this.farmlandArray.get(i).getY() == y) {
            return this.farmlandArray.get(i).harvest();
         }
      }

      this.font.newFloater("-1", x * 16 + 4, y * 16 + 4);
      return null;
   }

   public void addResourceToMasterList(ResourceBase r) {
      r.setID(this.getNextResourceID());
      this.resourcesMaster.add(r);
   }

   public void createResourceOnGround(ResourceModule.ResourceType type, ResourceModule.ResourceColorSet color, int tileX, int tileY) throws SlickException {
      ResourceBase newResource = new ResourceBase(this.getNextResourceID(), tileX, tileY, type, color);
      this.resourcesMaster.add(newResource);
      this.resourcesOnGround.add(newResource);
      this.setGroundMap(tileX, tileY, newResource);
   }

   public ResourceBase createResourceInBuilding(ResourceModule.ResourceType type, ResourceModule.ResourceColorSet color, ObjectBase carrier) {
      ResourceBase newResource = new ResourceBase(this.getNextResourceID(), carrier, type, color);
      this.resourcesMaster.add(newResource);
      this.resourcesInBuilding.add(newResource);
      return newResource;
   }

   public ResourceBase createResourceUnassigned(ResourceModule.ResourceType type) {
      ResourceBase newResource = new ResourceBase(this.getNextResourceID(), type, ResourceModule.ResourceColorSet.DEFAULT);
      this.resourcesMaster.add(newResource);
      return newResource;
   }

   public void createResourceProvisioner(ResourceModule.ResourceType type) {
      ResourceBase newResource = new ResourceBase(0, type, ResourceModule.ResourceColorSet.DEFAULT);
      this.resourcesProvisioner.add(newResource);
      this.resourcesProvisionerByType.get(newResource.getType()).add(newResource);
   }

   public ResourceBase createResourceUnassigned(ResourceModule.ResourceType type, ResourceModule.ResourceColorSet color) {
      ResourceBase newResource = new ResourceBase(this.getNextResourceID(), type, color);
      this.resourcesMaster.add(newResource);
      return newResource;
   }

   public ResourceBase createRefinedResource(ResourceModule.ResourceType type, boolean allowTrash, int trashChance) {
      return allowTrash && Utilities.randomInt(trashChance) == 0 && type.getTemplate().getTrashResource() != null && !type.getTemplate().alwaysReturnResource()
         ? this.createResourceUnassigned(type.getTemplate().getTrashResource(), ResourceModule.ResourceColorSet.DEFAULT)
         : this.createResourceUnassigned(type, ResourceModule.ResourceColorSet.DEFAULT);
   }

   public ResourceBase createRefinedResource(ResourceBase baseResource, ResourceModule.ResourceType type, boolean allowTrash, int trashChance) {
      return allowTrash
            && Utilities.randomInt(trashChance) == 0
            && baseResource.getType().getTemplate().getTrashResource() != null
            && !baseResource.getType().getTemplate().alwaysReturnResource()
         ? this.createResourceUnassigned(baseResource.getType().getTemplate().getTrashResource(), baseResource.getColor())
         : this.createResourceUnassigned(type, baseResource.getColor());
   }

   public void update() throws SlickException {
      this.updateResourceCount();

      for (int x = 0; x < this.resourcesMaster.size(); x++) {
         this.resourcesMaster.get(x).update();
      }

      this.updateResourceDecay();

      for (FarmlandBase f : this.farmlandArray) {
         f.update();
      }

      if (this.weather.isRaining()) {
         this.increaseWaterRateTick++;
         if (this.increaseWaterRateTick > this.increaseWaterRate) {
            this.waterBalancer.increaseAllWater(this.resourceValuesMap, 1);
            this.increaseWaterRateTick = 0;
         }
      } else {
         this.increaseWaterRateTick = 0;
      }

      this.waterBalancer.update(this.resourceValuesMap);
   }

   private void updateResourceDecay() throws SlickException {
      for (int x = 0; x < this.resourcesOnGround.size(); x++) {
         if (Utilities.randomInt(200) == 0) {
            int damageAmount = 0;
            ResourceBase r = this.resourcesOnGround.get(x);
            if (this.weather.isRaining() || this.weather.isSnowing()) {
               damageAmount++;
            }

            if (this.weather.getCurrentTemp() >= this.weather.getFryPoint()) {
               damageAmount += 6;
            } else if (this.weather.getCurrentTemp() >= this.weather.getHotPoint()) {
               damageAmount += 4;
            } else {
               if (this.weather.getCurrentTemp() <= this.weather.getFreezePoint()) {
                  continue;
               }

               damageAmount += 2;
            }

            r.damageResource(damageAmount);
         }
      }
   }

   public void renderResourcesOnGround(Graphics g, boolean debug) throws SlickException {
      this.resourceSheet.startUse();

      for (int x = 0; x < this.farmlandArray.size(); x++) {
         this.farmlandArray.get(x).render();
      }

      for (int x = 0; x < this.resourcesOnGround.size(); x++) {
         this.resourcesOnGround.get(x).render();
      }

      this.resourceSheet.endUse();
      if (debug) {
         for (int x = 0; x < this.farmlandArray.size(); x++) {
            this.farmlandArray.get(x).renderDebug(g);
         }
      }
   }

   public void renderResourcesOnGroundArrows() throws SlickException {
      if (this.resourcesOnGround.size() > 0) {
         this.resourcesOnGround.get(0).getResourceArrow().startUse();

         for (int x = 0; x < this.resourcesOnGround.size(); x++) {
            this.resourcesOnGround.get(x).renderOverlay();
         }

         for (int x = 0; x < this.resourcesInAir.size(); x++) {
            this.resourcesInAir.get(x).renderOverlay();
         }

         this.resourcesOnGround.get(0).getResourceArrow().endUse();
      }
   }

   public void renderResourcesOnGroundMapWriter(Graphics g) throws SlickException {
      this.resourceSheet.startUse();

      for (int x = 0; x < this.farmlandArray.size(); x++) {
         this.farmlandArray.get(x).renderMapWriter();
      }

      for (int x = 0; x < this.resourcesOnGround.size(); x++) {
         this.resourcesOnGround.get(x).renderMapWriter();
      }

      this.resourceSheet.endUse();
   }

   public void renderResourcesInAir() throws SlickException {
      Collections.sort(this.resourcesInAir, new Comparator<ResourceBase>() {
         public int compare(ResourceBase e1, ResourceBase e2) {
            return e1 == e2 ? 0 : (int)e1.getResourceY() - (int)e2.getResourceY();
         }
      });
      this.resourceSheet.startUse();

      for (ResourceBase r : this.resourcesInAir) {
         r.render();
      }

      this.resourceSheet.endUse();
   }

   public void renderResourcesInAirMapWriter() throws SlickException {
      Collections.sort(this.resourcesInAir, new Comparator<ResourceBase>() {
         public int compare(ResourceBase e1, ResourceBase e2) {
            return e1 == e2 ? 0 : (int)e1.getResourceY() - (int)e2.getResourceY();
         }
      });
      this.resourceSheet.startUse();

      for (ResourceBase r : this.resourcesInAir) {
         r.renderMapWriter();
      }

      this.resourceSheet.endUse();
   }

   public byte[][] getResourceValues() {
      return this.resourceValuesMap;
   }

   public ResourceModule.ResourceType[][] getResourceType() {
      return this.resourceTypesMap;
   }

   public void setResourcesOnGround(int tileX, int tileY, ResourceBase r) throws SlickException {
      this.resourcesInAir.remove(r);
      this.resourcesInBuilding.remove(r);
      this.resourcesOnGround.remove(r);
      this.resourcesOnGround.add(r);
      this.setGroundMap(tileX, tileY, r);
      r.placeOnGround(tileX, tileY);
   }

   public void moveResourcesOnGround(int tileX, int tileY, int x, int y, ResourceBase r) throws SlickException {
      this.resetGroundMap(tileX, tileY);
      this.setGroundMap(x, y, r);
      r.placeOnGround(x, y);
   }

   public void setResourceInBuilding(ObjectBase building, ResourceBase r) {
      this.resourcesInAir.remove(r);
      this.resourcesInBuilding.remove(r);
      this.resourcesInBuilding.add(r);
      this.resourcesOnGround.remove(r);
      if (r.isOnGround()) {
         this.resetGroundMap(r.getTileX(), r.getTileY());
      }

      r.placeInBuilding(building);
   }

   public void setResourceCarried(MobBase mob, ResourceBase r) {
      this.resourcesInAir.remove(r);
      this.resourcesInAir.add(r);
      this.resourcesInBuilding.remove(r);
      this.resourcesOnGround.remove(r);
      if (r.isOnGround()) {
         this.resetGroundMap(r.getTileX(), r.getTileY());
      }

      r.placeCarried(mob);
   }

   public void setResourceInInventory(MobBase mob, ResourceBase r) {
      this.resourcesInAir.remove(r);
      this.resourcesInBuilding.remove(r);
      this.resourcesOnGround.remove(r);
      if (r.isOnGround()) {
         this.resetGroundMap(r.getTileX(), r.getTileY());
      }

      r.placeInInventory(mob);
   }

   public void setResourceGrabbed(ResourceBase r) {
      this.resourcesInAir.remove(r);
      this.resourcesInAir.add(r);
      this.resourcesInBuilding.remove(r);
      this.resourcesOnGround.remove(r);
      if (r.isOnGround()) {
         this.resetGroundMap(r.getTileX(), r.getTileY());
      }

      r.grab();
   }

   public void setResourceAsCrop(ResourceBase r) {
      this.resourcesInAir.remove(r);
      this.resourcesInBuilding.remove(r);
      this.resourcesOnGround.remove(r);
      if (r.isOnGround()) {
         this.resetGroundMap(r.getTileX(), r.getTileY());
      }

      r.plantInCrop();
   }

   public void transferResourceToProvisioner(ResourceBase r) throws SlickException {
      this.deleteResource(r);
      this.resourcesProvisioner.add(r);
      this.resourcesProvisionerByType.get(r.getType()).add(r);
   }

   public void tranferResourceFromProvisionerToMob(MobBase m, ResourceModule.ResourceType t) throws SlickException {
      ResourceBase r = this.resourcesProvisionerByType.get(t).get(0);
      this.resourcesProvisioner.remove(r);
      this.resourcesProvisionerByType.get(r.getType()).remove(r);
      this.addResourceToMasterList(r);
      m.addCarriedResource(r, true);
   }

   public int getProvisionerCount() {
      return this.resourcesProvisioner.size();
   }

   public int getProvisionerCount(ResourceModule.ResourceType type) {
      return this.resourcesProvisionerByType.get(type).size();
   }

   public void prepareForMigrationModule(ResourceBase r) throws SlickException {
      this.resourcesMaster.remove(r);
      this.resourcesInAir.remove(r);
      this.resourcesInBuilding.remove(r);
      this.resourcesOnGround.remove(r);
   }

   public void deleteResource(ResourceBase r) throws SlickException {
      this.resourcesMaster.remove(r);
      this.resourcesInAir.remove(r);
      this.resourcesInBuilding.remove(r);
      this.resourcesOnGround.remove(r);
      if (r.getCarrier() != null) {
         r.getCarrier().unequipResource(r);
      }

      if (r.getBuilding() != null) {
         r.getBuilding().takeStores(r);
      }

      if (r.isOnGround()) {
         this.resetGroundMap(r.getTileX(), r.getTileY());
         r.resetFlags();
      }

      if (r.getCaptured() != null) {
         r.uncapture();
      }

      if (r.getBoundGhost() != null) {
         MobBase ghost = r.getBoundGhost();
         ghost.unassignFromAll(false);
         this.mob.deleteMob(ghost);
         this.sound.playSound(SoundModule.SoundType.POP, r.getCenterPixelX(true), r.getCenterPixelY(true));

         for (int x = 0; x < 5; x++) {
            this.projectile
               .newSmallBlast(
                  r.getCenterPixelX(true) + Utilities.randomInt(-4, 4),
                  r.getCenterPixelY(true) + 8 + Utilities.randomInt(-1, 1),
                  ParticleModule.ParticleSet.MAGIC_BLUE
               );

            for (int y = 0; y < 5; y++) {
               this.particle
                  .newParticle(
                     r.getCenterPixelX(true) + Utilities.randomInt(-4, 4),
                     r.getCenterPixelY(true) + 8 + Utilities.randomInt(-1, 1),
                     ParticleModule.ParticleType.SMOKE_SPECK,
                     ParticleModule.ParticleSet.MAGIC_BLUE
                  );
            }
         }
      }
   }

   public ResourceBase getResourceOnGround(int tileX, int tileY) {
      return this.resourcesOnGroundMap[tileX][tileY];
   }

   public ResourceBase getResourceOnGround(OrderedPair o) {
      return this.resourcesOnGroundMap[o.getX()][o.getY()];
   }

   private void resetGroundMap(int x, int y) {
      this.resourcesOnGroundByteMap[x][y] = 0;
      this.resourcesOnGroundMap[x][y] = null;
   }

   private void setGroundMap(int x, int y, ResourceBase r) {
      this.resourcesOnGroundByteMap[x][y] = 1;
      this.resourcesOnGroundMap[x][y] = r;
   }

   private void updateResourceCount() {
      ResourceModule.ResourceType[] objectFlags;
      for (ResourceModule.ResourceType type : objectFlags = RESOURCE_TYPES) {
         this.resourceStores[type.ordinal()] = 0;
         this.resourceStoresSpace[type.ordinal()] = 0;
         this.resourceGround[type.ordinal()] = 0;
         this.resourceGroundInRange[type.ordinal()] = 0;
         this.resourceCarried[type.ordinal()] = 0;
         this.resourceEquipped[type.ordinal()] = 0;
      }

      HashMap<MapTilesLoader.TileSet, HashMap<ObjectBase.ObjectSubType, ObjectFlags>> flagList = this.object.getObjectFlagFactory().getObjectFlagsList();

      // Reuse each building type's counter array (zeroed) instead of allocating a new one per type every tick.
      for (HashMap<ObjectBase.ObjectSubType, ObjectFlags> o : flagList.values()) {
         MapTilesLoader.TileSet baseType = o.get(ObjectBase.ObjectSubType.BUILT).getBaseType();
         int[] counts = this.resourceStoresSpaceByType.get(baseType);
         if (counts != null && counts.length == RESOURCE_TYPE_COUNT) {
            java.util.Arrays.fill(counts, 0);
         } else {
            this.resourceStoresSpaceByType.put(baseType, new int[RESOURCE_TYPE_COUNT]);
         }
      }

      this.totalAvailableGoldCoins = 0;
      this.maximumAvailableGoldCoins = 0;
      this.totalAvailableWater = 0;
      this.maximumAvailableWater = 0;
      this.totalAvailableFood = 0;
      this.maximumAvailableFood = 0;
      this.totalAvailableDirtyWater = 0;
      this.maximumAvailableDirtyWater = 0;
      this.totalAvailableEnergy = 0;
      this.maximumAvailableEnergy = 0;

      for (int i = 0; i < this.resourcesMaster.size(); i++) {
         ResourceBase r = this.resourcesMaster.get(i);
         boolean countTotals = false;
         if (!r.isInBuilding() || r.getBuilding() != null && !r.getBuilding().isDestroyed()) {
            if (r.isInBuilding() && r.getBuilding().getObjectFlags().getMobGroup() == MobBase.MobGroup.VILLAGER) {
               this.resourceStores[r.getType().ordinal()]++;
               countTotals = true;
            } else if (!r.isCarried()
               || r.getCarrier().getMobGroup() != MobBase.MobGroup.VILLAGER
                  && r.getCarrier().getMobGroup() != MobBase.MobGroup.DOMESTICATED_ANIMAL
                  && r.getCarrier().getMobGroup() != MobBase.MobGroup.GOLEM) {
               if (!r.isInInventory()
                  || r.getCarrier().getMobGroup() != MobBase.MobGroup.VILLAGER
                     && r.getCarrier().getMobGroup() != MobBase.MobGroup.DOMESTICATED_ANIMAL
                     && r.getCarrier().getMobGroup() != MobBase.MobGroup.GOLEM) {
                  if (r.isOnGround()) {
                     this.resourceGround[r.getType().ordinal()]++;
                     if (this.object.isInRange(r.getTileX(), r.getTileY())) {
                        this.resourceGroundInRange[r.getType().ordinal()]++;
                        countTotals = true;
                     }
                  }
               } else {
                  this.resourceEquipped[r.getType().ordinal()]++;
                  countTotals = true;
               }
            } else {
               this.resourceCarried[r.getType().ordinal()]++;
               countTotals = true;
            }

            if (countTotals) {
               ResourceModule.ResourceType type = r.getType();
               if (type == ResourceModule.ResourceType.GOLD_COIN_SACK) {
                  this.totalAvailableGoldCoins = this.totalAvailableGoldCoins + r.getUses();
               }

               if (type.getTemplate().getWaterPerUse() > 0) {
                  this.totalAvailableWater = this.totalAvailableWater + r.getUses() * r.getType().getTemplate().getWaterPerUse();
               }

               if (type.getTemplate().getFoodPerUse() > 0) {
                  this.totalAvailableFood = this.totalAvailableFood + r.getUses() * r.getType().getTemplate().getFoodPerUse();
               }

               if (type == ResourceModule.ResourceType.DIRTY_WATER_BUCKET) {
                  this.totalAvailableDirtyWater = this.totalAvailableDirtyWater + r.getUses();
               }
            }
         } else {
            System.out.println("Bad resource found: " + r.getType() + " " + r.getTileX() + "," + r.getTileY());

            try {
               this.resource.deleteResource(r);
            } catch (SlickException var11) {
               var11.printStackTrace();
            }
         }
      }

      for (ObjectBase o : this.object.getObjectMasterBuildingPriorityList()) {
         if (o.isInRange()) {
            ObjectFlags objectFlagsx = o.getObjectFlags();

            for (ResourceModule.ResourceType typex : objectFlagsx.getCanBuildingStore()) {
               int maximum = objectFlagsx.getResourceStorageMax().get(typex);
               this.resourceStoresSpace[typex.ordinal()] += maximum - o.getResourceCount(typex);
               this.resourceStoresSpaceByType.get(objectFlagsx.getBaseType())[typex.ordinal()] += maximum - o.getResourceCount(typex);
            }

            this.totalAvailableEnergy = this.totalAvailableEnergy + o.getEnergy();
            this.maximumAvailableEnergy = this.maximumAvailableEnergy + objectFlagsx.getEnergyMax();
            this.maximumAvailableGoldCoins = this.maximumAvailableGoldCoins
               + objectFlagsx.getResourceStorageMax().get(ResourceModule.ResourceType.GOLD_COIN_SACK)
                  * ResourceModule.ResourceType.GOLD_COIN_SACK.getTemplate().getUsesMax();

            ResourceModule.ResourceType[] total;
            for (ResourceModule.ResourceType t : total = RESOURCE_TYPES) {
               if (t.getTemplate().getWaterPerUse() > 0) {
                  this.maximumAvailableWater = this.maximumAvailableWater
                     + objectFlagsx.getResourceStorageMax().get(t) * t.getTemplate().getUsesMax() * t.getTemplate().getWaterPerUse();
               }

               if (t.getTemplate().getFoodPerUse() > 0) {
                  this.maximumAvailableFood = this.maximumAvailableFood
                     + objectFlagsx.getResourceStorageMax().get(t) * t.getTemplate().getUsesMax() * t.getTemplate().getFoodPerUse();
               }
            }

            this.maximumAvailableDirtyWater = this.maximumAvailableDirtyWater
               + objectFlagsx.getResourceStorageMax().get(ResourceModule.ResourceType.DIRTY_WATER_BUCKET)
                  * ResourceModule.ResourceType.DIRTY_WATER_BUCKET.getTemplate().getUsesMax();
         }
      }

      this.resourceGatherSpacingTick++;
      if (this.resourceGatherSpacingTick >= this.resourceGatherSpacing) {
         ResourceModule.ResourceType[] var26;
         for (ResourceModule.ResourceType typex : RESOURCE_TYPES) {
            int currentTotalValue = this.resource.getResourceTotal(typex, true);
            int newRate = 0;
            if (this.resourceGatherHistory.get(typex).size() > 0) {
               int previousTotal = this.resourceGatherHistory.get(typex).get(this.resourceGatherHistory.get(typex).size() - 1);
               newRate = currentTotalValue - previousTotal;
            }

            this.resourceGatherHistory.get(typex).add(currentTotalValue);
            this.resourceGatherRateHistory.get(typex).add(newRate);
            if (this.resourceGatherRateHistory.get(typex).size() > 8) {
               this.resourceGatherRateHistory.get(typex).remove(0);
            }

            if (this.resourceGatherHistory.get(typex).size() > 8) {
               this.resourceGatherHistory.get(typex).remove(0);
            }

            int total = 0;

            for (Integer ix : this.resourceGatherRateHistory.get(typex)) {
               total += ix;
            }

            this.resourceGatherRate.put(typex, total);
         }

         this.resourceGatherSpacingTick = 0;
      }
   }

   public ArrayList<ResourceBase> getResources() {
      return this.resourcesMaster;
   }

   public ArrayList<ResourceBase> getProvisionerResources() {
      return this.resourcesProvisioner;
   }

   public Image[][] getResourceImages() {
      return this.resourceImages;
   }

   public int getResourceStores(ResourceModule.ResourceType type) {
      return this.resourceStores[type.ordinal()];
   }

   public int getResourceStoresSpace(ResourceModule.ResourceType type) {
      return this.resourceStoresSpace[type.ordinal()];
   }

   public int getResourceStoresSpace(ResourceModule.ResourceType type, MapTilesLoader.TileSet t) {
      return this.resourceStoresSpaceByType.get(t)[type.ordinal()];
   }

   public int getResourceGround(ResourceModule.ResourceType type, boolean inRangeOnly) {
      return inRangeOnly ? this.resourceGroundInRange[type.ordinal()] : this.resourceGround[type.ordinal()];
   }

   public int getResourceCarried(ResourceModule.ResourceType type) {
      return this.resourceCarried[type.ordinal()];
   }

   public int getResourceEquipped(ResourceModule.ResourceType type) {
      return this.resourceEquipped[type.ordinal()];
   }

   public int getResourceTotal(ResourceModule.ResourceType type, boolean inRangeOnly) {
      return inRangeOnly
         ? this.resourceStores[type.ordinal()]
            + this.resourceGroundInRange[type.ordinal()]
            + this.resourceCarried[type.ordinal()]
            + this.resourceEquipped[type.ordinal()]
         : this.resourceStores[type.ordinal()]
            + this.resourceGround[type.ordinal()]
            + this.resourceCarried[type.ordinal()]
            + this.resourceEquipped[type.ordinal()];
   }

   public boolean isResourcesOnGround(int tileX, int tileY) {
      return this.resourcesOnGroundByteMap[tileX][tileY] > 0;
   }

   public ResourceModule.ResourceType getResourceTypeOnGround(int tileX, int tileY) {
      return this.resourcesOnGroundMap[tileX][tileY] == null ? null : this.resourcesOnGroundMap[tileX][tileY].getType();
   }

   public ArrayList<ResourceBase> getResourcesMaster() {
      return this.resourcesMaster;
   }

   public ArrayList<ResourceBase> getResourcesInAir() {
      return this.resourcesInAir;
   }

   public ArrayList<ResourceBase> getResourcesOnGround() {
      return this.resourcesOnGround;
   }

   public ArrayList<ResourceBase> getResourcesOnGroundInVillage() {
      ArrayList<ResourceBase> list = new ArrayList<>();

      for (int i = 0; i < this.resourcesOnGround.size(); i++) {
         ResourceBase r = this.resourcesOnGround.get(i);
         if (this.object.isInRange(r.getTileX(), r.getTileY())) {
            list.add(r);
         }
      }

      return list;
   }

   public ArrayList<ResourceBase> getResourcesOnGroundInCorruption() {
      ArrayList<ResourceBase> list = new ArrayList<>();

      for (int i = 0; i < this.resourcesOnGround.size(); i++) {
         ResourceBase r = this.resourcesOnGround.get(i);
         if (this.corruption.isTileCorrupted(r.getTileX(), r.getTileY())) {
            list.add(r);
         }
      }

      return list;
   }

   public int getResourceRate(ResourceModule.ResourceType type) {
      return this.resourceGatherRate.get(type);
   }

   public byte[][] getResourcesOnGroundByteMap() {
      return this.resourcesOnGroundByteMap;
   }

   public ResourceBase getResource(int resourceID) {
      for (int i = 0; i < this.resourcesMaster.size(); i++) {
         if (this.resourcesMaster.get(i).getID() == resourceID) {
            return this.resourcesMaster.get(i);
         }
      }

      return null;
   }

   public ResourceModule.ResourceSpriteSet getResourceSpriteSet(ResourceModule.ResourceType type, ResourceModule.ResourceColorSet color) {
      return this.resourceColorMap.get(type).get(color) == null
         ? this.resourceColorMap.get(type).get(ResourceModule.ResourceColorSet.DEFAULT)
         : this.resourceColorMap.get(type).get(color);
   }

   public Image getResourceSprite(ResourceModule.ResourceSpriteSet spriteSet, int spriteNumber) {
      return this.resourceImages[spriteNumber][spriteSet.getID()];
   }

   public ResourceModule.ResourceType getRandomResourceType() {
      int randomDelivery = Utilities.randomInt(RESOURCE_TYPES.length);
      return RESOURCE_TYPES[randomDelivery];
   }

   public ResourceModule.ResourceType getCrop(ResourceBase r) {
      return r.getType() == ResourceModule.ResourceType.RAW_VEGETABLE ? ResourceModule.ResourceType.RAW_VEGETABLE : null;
   }

   public boolean canPlant(ResourceBase r) {
      return r.getType() == ResourceModule.ResourceType.RAW_VEGETABLE;
   }

   public FarmlandBase createFarmland(int x, int y) {
      for (int i = 0; i < this.farmlandArray.size(); i++) {
         if (this.farmlandArray.get(i).getX() == x && this.farmlandArray.get(i).getY() == y) {
            return null;
         }
      }

      FarmlandBase newFarmland = new FarmlandBase(this.getNextFarmlandID(), x, y);
      this.farmlandArray.add(newFarmland);
      return newFarmland;
   }

   public void deleteFarmland(ArrayList<FarmlandBase> localFarmland) {
      for (int i = 0; i < localFarmland.size(); i++) {
         this.farmlandArray.remove(localFarmland.get(i));
      }
   }

   public void plantCrop(Assignment plant, ResourceBase r) {
      for (int i = 0; i < this.farmlandArray.size(); i++) {
         if (!this.farmlandArray.get(i).isPlanted()
            && this.farmlandArray.get(i).getX() == plant.getSecondaryX()
            && this.farmlandArray.get(i).getY() == plant.getSecondaryY()) {
            this.farmlandArray.get(i).setCropType(r);
            this.setResourceAsCrop(r);
         }
      }
   }

   public boolean isCropGrown(Assignment plant) {
      for (int i = 0; i < this.farmlandArray.size(); i++) {
         if (this.farmlandArray.get(i).getX() == plant.getSecondaryX() && this.farmlandArray.get(i).getY() == plant.getSecondaryY()) {
            return this.farmlandArray.get(i).isGrown();
         }
      }

      return false;
   }

   public boolean isCrop(Assignment plant) {
      return this.isCrop(plant.getSecondaryX(), plant.getSecondaryY());
   }

   public boolean isCrop(int x, int y) {
      for (int i = 0; i < this.farmlandArray.size(); i++) {
         if (this.farmlandArray.get(i).getX() == x && this.farmlandArray.get(i).getY() == y) {
            return true;
         }
      }

      return false;
   }

   public ArrayList<FarmlandBase> getFarmlandArray() {
      return this.farmlandArray;
   }

   public FarmlandBase getFarmland(Assignment plant) {
      return this.getFarmland(plant.getSecondaryX(), plant.getSecondaryY());
   }

   public FarmlandBase getFarmland(int x, int y) {
      for (int i = 0; i < this.farmlandArray.size(); i++) {
         if (this.farmlandArray.get(i).getX() == x && this.farmlandArray.get(i).getY() == y) {
            return this.farmlandArray.get(i);
         }
      }

      return null;
   }

   public FarmlandBase getFarmland(int f) {
      for (int i = 0; i < this.farmlandArray.size(); i++) {
         if (this.farmlandArray.get(i).getID() == f) {
            return this.farmlandArray.get(i);
         }
      }

      return null;
   }

   public int getNextResourceID() {
      return this.resourceLastID++;
   }

   public int getNextFarmlandID() {
      return this.farmlandLastID++;
   }

   public void updateResourceMap(int x, int y) throws SlickException {
      boolean foundWater = false;
      boolean foundResource = false;

      for (int l = 0; l < this.mapLayers; l++) {
         if (this.map.getTileId(x, y, l) != 0) {
            int tileID = this.map.getTileId(x, y, l);
            ResourceModule.ResourceType type = this.map.getMapTileLoader().getTileResourceType(tileID);
            byte amount = this.map.getMapTileLoader().getTileResourceAmount(tileID);
            byte amountMax = this.map.getMapTileLoader().getTileResourceAmountMax(tileID);
            if (type != null) {
               if (this.resourceValuesMap[x][y] <= 0 || this.resourceTypesMap[x][y] != type) {
                  if (this.resourceTypesMap[x][y] != type) {
                     this.resourceValuesMap[x][y] = 0;
                  }

                  this.addResourcesToTile(x, y, type, amount, amountMax);
               }

               if (type == ResourceModule.ResourceType.DIRTY_WATER_BUCKET) {
                  foundWater = true;
               }

               foundResource = true;
            }
         }
      }

      if (foundWater) {
         this.waterBalancer.flagWaterRebalance(x, y);
      }

      if (!foundResource) {
         this.resourceValuesMap[x][y] = 0;
         this.resourceTypesMap[x][y] = null;
      }
   }

   public OrderedPair findOpenSpace(int tileX, int tileY, MapData.BlockMapGroup g) throws SlickException {
      return this.findOpenSpace(tileX, tileY, g, false);
   }

   public OrderedPair findOpenSpace(int tileX, int tileY, MapData.BlockMapGroup g, boolean noRoads) throws SlickException {
      this.sweepList.clear();
      this.setList.clear();
      if (tileX >= 254) {
         tileX = 254;
      }

      if (tileX <= 2) {
         tileX = 2;
      }

      if (tileY >= 254) {
         tileY = 254;
      }

      if (tileY <= 2) {
         tileY = 2;
      }

      byte[][] blockMap = this.map.getMapData().getBlockMap(g);
      byte[][] tileCheck = new byte[256][256];

      for (int i = 0; i < blockMap.length; i++) {
         tileCheck[i] = Arrays.copyOf(blockMap[i], blockMap[i].length);
      }

      boolean foundSpot = false;
      this.sweepList.add(OrderedPair.getOrderedPair(tileX, tileY));
      tileCheck[tileX][tileY] = 2;
      OrderedPair coordinates = null;

      while (!foundSpot) {
         label95:
         for (int x = 0; x < this.sweepList.size(); x++) {
            int thisX = this.sweepList.get(x).getX();
            int thisY = this.sweepList.get(x).getY();
            if (thisX < 255 && thisX > 0 && thisY < 255 && thisY > 0) {
               ArrayList<OrderedPair> theseNodes = new ArrayList<>();
               theseNodes.add(OrderedPair.getOrderedPair(thisX + 1, thisY));
               theseNodes.add(OrderedPair.getOrderedPair(thisX - 1, thisY));
               theseNodes.add(OrderedPair.getOrderedPair(thisX, thisY + 1));
               theseNodes.add(OrderedPair.getOrderedPair(thisX, thisY - 1));
               Collections.shuffle(theseNodes);

               for (OrderedPair o : theseNodes) {
                  if (tileCheck[o.getX()][o.getY()] == 0) {
                     this.setList.add(OrderedPair.getOrderedPair(o.getX(), o.getY()));
                     tileCheck[o.getX()][o.getY()] = 2;
                  }
               }

               for (OrderedPair ox : theseNodes) {
                  if (noRoads
                     && this.road.getRoadType(ox.getX(), ox.getY()) == RoadModule.RoadType.NONE
                     && this.resourcesOnGroundByteMap[ox.getX()][ox.getY()] == 0
                     && blockMap[ox.getX()][ox.getY()] == 0) {
                     coordinates = OrderedPair.getOrderedPair(ox.getX(), ox.getY());
                     break label95;
                  }

                  if (!noRoads && this.resourcesOnGroundByteMap[ox.getX()][ox.getY()] == 0 && blockMap[ox.getX()][ox.getY()] == 0) {
                     coordinates = OrderedPair.getOrderedPair(ox.getX(), ox.getY());
                     break label95;
                  }
               }
            }
         }

         if (coordinates != null) {
            return coordinates;
         }

         this.sweepList.clear();
         if (this.setList.size() <= 0 && !foundSpot) {
            tileCheck = new byte[256][256];
            this.sweepList.add(OrderedPair.getOrderedPair(tileX, tileY));
            tileCheck[tileX][tileY] = 2;
         }

         this.sweepList.addAll(this.setList);
         Collections.shuffle(this.sweepList);
         this.setList.clear();
      }

      this.sweepList.clear();
      this.setList.clear();
      return null;
   }

   public ArrayList<ResourceBase> getResourcesNearby(int x, int y, int distance) {
      ArrayList<ResourceBase> list = new ArrayList<>();

      for (int i = 0; i < this.resourcesOnGround.size(); i++) {
         ResourceBase r = this.resourcesOnGround.get(i);
         int dx = x - r.getTileX();
         int dy = y - r.getTileY();
         int hValue = (int)Math.sqrt(dx * dx + dy * dy);
         if (hValue < distance) {
            list.add(r);
         }
      }

      return list;
   }

   public ArrayList<ResourceBase> cullResourcesByDistance(int tileX, int tileY, int distance, ArrayList<ResourceBase> list, MapData.BlockMapGroup g) throws SlickException {
      ArrayList<ResourceBase> listOut = new ArrayList<>();
      ArrayList<OrderedPair> coordinates = this.mob.getPathfinder().getCoordinatesInRange(tileX, tileY, distance, false, g);

      for (ResourceBase r : list) {
         if (coordinates.contains(OrderedPair.getOrderedPair(r.getTileX(), r.getTileY()))) {
            listOut.add(r);
         }
      }

      return listOut;
   }

   public ArrayList<ResourceModule.ResourceType> getResourceRares(ResourceModule.ResourceType t) {
      return this.resourceRares.get(t);
   }

   public int getTotalAvailableGoldCoins() {
      return this.totalAvailableGoldCoins;
   }

   public int getMaximumAvailableGoldCoins() {
      return this.maximumAvailableGoldCoins;
   }

   public int getTotalAvailableWater() {
      return this.totalAvailableWater;
   }

   public int getMaximumAvailableWater() {
      return this.maximumAvailableWater;
   }

   public int getTotalAvailableFood() {
      return this.totalAvailableFood;
   }

   public int getMaximumAvailableFood() {
      return this.maximumAvailableFood;
   }

   public int getTotalAvailableDirtyWater() {
      return this.totalAvailableDirtyWater;
   }

   public int getMaximumAvailableDirtyWater() {
      return this.maximumAvailableDirtyWater;
   }

   public int getTotalAvailableEnergy() {
      return this.totalAvailableEnergy;
   }

   public int getMaximumAvailableEnergy() {
      return this.maximumAvailableEnergy;
   }

   public void deleteMobInventory(MobBase mobBase) throws SlickException {
      ResourceModule.ResourceSlot[] var5;
      for (ResourceModule.ResourceSlot s : ResourceModule.ResourceSlot.values()) {
         if (mobBase.getResourceInInventory(s) != null) {
            this.deleteResource(mobBase.getResourceInInventory(s));
         }
      }
   }

   public int getResourceMakeAmount(ResourceModule.ResourceType t) {
      return this.resourceMakeAmounts[t.ordinal()];
   }

   public void setResourceMakeAmount(ResourceModule.ResourceType t, int amount) {
      this.resourceMakeAmounts[t.ordinal()] = amount;
   }

   public void increaseResourceMakeAmount(ResourceModule.ResourceType t, int amount) {
      if (this.resourceMakeAmounts[t.ordinal()] != RESOURCE_INFINITE_AMOUNT) {
         if (this.resourceMakeAmounts[t.ordinal()] == RESOURCE_STORAGE_AMOUNT) {
            this.resourceMakeAmounts[t.ordinal()] = RESOURCE_INFINITE_AMOUNT;
         } else {
            this.resourceMakeAmounts[t.ordinal()] += amount;
            if (this.resourceMakeAmounts[t.ordinal()] > RESOURCE_STORAGE_AMOUNT) {
               this.resourceMakeAmounts[t.ordinal()] = RESOURCE_STORAGE_AMOUNT;
            }
         }
      }
   }

   public void decreaseResourceMakeAmount(ResourceModule.ResourceType t, int amount) {
      if (this.resourceMakeAmounts[t.ordinal()] == RESOURCE_INFINITE_AMOUNT) {
         this.resourceMakeAmounts[t.ordinal()] = RESOURCE_STORAGE_AMOUNT;
      } else {
         this.resourceMakeAmounts[t.ordinal()] -= amount;
         if (this.resourceMakeAmounts[t.ordinal()] < 0) {
            this.resourceMakeAmounts[t.ordinal()] = 0;
         }
      }
   }

   public int getResourceMaintainAmount(ResourceModule.ResourceType t) {
      return this.resourceMaintainAmounts[t.ordinal()];
   }

   public void setResourceMaintainAmount(ResourceModule.ResourceType t, int amount) {
      this.resourceMaintainAmounts[t.ordinal()] = amount;
   }

   public void increaseResourceMaintainAmount(ResourceModule.ResourceType t, int amount) {
      if (this.resourceMaintainAmounts[t.ordinal()] != RESOURCE_INFINITE_AMOUNT) {
         if (this.resourceMaintainAmounts[t.ordinal()] == RESOURCE_STORAGE_AMOUNT) {
            this.resourceMaintainAmounts[t.ordinal()] = RESOURCE_INFINITE_AMOUNT;
         } else {
            this.resourceMaintainAmounts[t.ordinal()] += amount;
            if (this.resourceMaintainAmounts[t.ordinal()] > RESOURCE_STORAGE_AMOUNT) {
               this.resourceMaintainAmounts[t.ordinal()] = RESOURCE_STORAGE_AMOUNT;
            }
         }
      }
   }

   public void decreaseResourceMaintainAmount(ResourceModule.ResourceType t, int amount) {
      if (this.resourceMaintainAmounts[t.ordinal()] == RESOURCE_INFINITE_AMOUNT) {
         this.resourceMaintainAmounts[t.ordinal()] = RESOURCE_STORAGE_AMOUNT;
      } else {
         this.resourceMaintainAmounts[t.ordinal()] -= amount;
         if (this.resourceMaintainAmounts[t.ordinal()] < 0) {
            this.resourceMaintainAmounts[t.ordinal()] = 0;
         }
      }
   }

   public boolean isOnResourceBarMain(ResourceModule.ResourceType t) {
      return this.resourcesBarListMain.get(t);
   }

   public boolean setOnResourceBarMain(ResourceModule.ResourceType t, boolean b) {
      return this.resourcesBarListMain.put(t, b);
   }

   public boolean isOnResourceBarRight(ResourceModule.ResourceBarType t) {
      return this.resourcesBarListRight.get(t);
   }

   public boolean setOnResourceBarRight(ResourceModule.ResourceBarType t, boolean b) {
      return this.resourcesBarListRight.put(t, b);
   }

   public static enum ResourceBarType {
      GOLD,
      FOOD,
      WATER,
      DIRTY_WATER,
      ENERGY,
      FAITH;
   }

   public static enum ResourceCategory {
      ARMOR(Text.getText("resourceCategory.armor")),
      BYPRODUCT(Text.getText("resourceCategory.byproduct")),
      FOOD_AND_WATER(Text.getText("resourceCategory.foodAndWater")),
      MISCELLANEOUS(Text.getText("resourceCategory.miscellaneous")),
      RAW(Text.getText("resourceCategory.raw")),
      RECOVERY(Text.getText("resourceCategory.recovery")),
      REFINED(Text.getText("resourceCategory.refined")),
      TOOLS(Text.getText("resourceCategory.tools")),
      TOWER_AMMO(Text.getText("resourceCategory.towerAmmo")),
      TRASH(Text.getText("resourceCategory.trash")),
      WEAPONS(Text.getText("resourceCategory.weapons"));

      private String name;

      private ResourceCategory(String name) {
         this.name = name;
      }

      public String getText() {
         return this.name;
      }
   }

   public static enum ResourceColorSet {
      BROWN,
      GRAY,
      LIGHT_GRAY,
      GREEN,
      DARK_BROWN,
      WHITE,
      BLACK,
      TAN,
      BLUE,
      RED,
      YELLOW,
      PURPLE,
      DEFAULT;
   }

   public static enum ResourceSlot {
      HEAD,
      CHEST,
      MELEE_WEAPON,
      SHIELD,
      TOOL,
      RECOVERY,
      WATER,
      FOOD,
      RANGED_WEAPON,
      RANGED_WEAPON_AMMO,
      NONE;
   }

   public static enum ResourceSpriteSet {
      WOOD_BROWN(ResourceModule.slotNumber++, "iconWoodBrown"),
      BOARD_BROWN(ResourceModule.slotNumber++, "iconBoardBrown"),
      WOODY_TRASH_BROWN(ResourceModule.slotNumber++, "iconTrashyWoodBrown"),
      WOOD_RED(ResourceModule.slotNumber++, "iconWoodBrown"),
      BOARD_RED(ResourceModule.slotNumber++, "iconBoardBrown"),
      WOODY_TRASH_RED(ResourceModule.slotNumber++, "iconTrashyWoodBrown"),
      WOOD_GRAY(ResourceModule.slotNumber++, "iconWoodBrown"),
      BOARD_GRAY(ResourceModule.slotNumber++, "iconBoardBrown"),
      WOODY_TRASH_GRAY(ResourceModule.slotNumber++, "iconTrashyWoodBrown"),
      WOOD_GREEN(ResourceModule.slotNumber++, "iconWoodBrown"),
      BOARD_GREEN(ResourceModule.slotNumber++, "iconBoardBrown"),
      WOODY_TRASH_GREEN(ResourceModule.slotNumber++, "iconTrashyWoodBrown"),
      WOOD_DARK_BROWN(ResourceModule.slotNumber++, "iconWoodBrown"),
      BOARD_DARK_BROWN(ResourceModule.slotNumber++, "iconBoardBrown"),
      WOODY_TRASH_DARK_BROWN(ResourceModule.slotNumber++, "iconTrashyWoodBrown"),
      WOOD_BLUE(ResourceModule.slotNumber++, "iconWoodBrown"),
      BOARD_BLUE(ResourceModule.slotNumber++, "iconBoardBrown"),
      WOODY_TRASH_BLUE(ResourceModule.slotNumber++, "iconTrashyWoodBrown"),
      WOOD_LIGHT_GRAY(ResourceModule.slotNumber++, "iconWoodBrown"),
      BOARD_LIGHT_GRAY(ResourceModule.slotNumber++, "iconBoardBrown"),
      WOODY_TRASH_LIGHT_GRAY(ResourceModule.slotNumber++, "iconTrashyWoodBrown"),
      ROCK_WHITE(ResourceModule.slotNumber++, "iconRockGray"),
      STONE_WHITE(ResourceModule.slotNumber++, "iconStoneGray"),
      ROCKY_TRASH_WHITE(ResourceModule.slotNumber++, "iconRockGray"),
      ROCK_GRAY(ResourceModule.slotNumber++, "iconRockGray"),
      STONE_GRAY(ResourceModule.slotNumber++, "iconStoneGray"),
      ROCKY_TRASH_GRAY(ResourceModule.slotNumber++, "iconRockGray"),
      ROCK_BLACK(ResourceModule.slotNumber++, "iconRockGray"),
      STONE_BLACK(ResourceModule.slotNumber++, "iconStoneGray"),
      ROCKY_TRASH_BLACK(ResourceModule.slotNumber++, "iconRockGray"),
      ROCK_BROWN(ResourceModule.slotNumber++, "iconRockGray"),
      STONE_BROWN(ResourceModule.slotNumber++, "iconStoneGray"),
      ROCKY_TRASH_BROWN(ResourceModule.slotNumber++, "iconRockGray"),
      ROCK_RED(ResourceModule.slotNumber++, "iconRockGray"),
      STONE_RED(ResourceModule.slotNumber++, "iconStoneGray"),
      ROCKY_TRASH_RED(ResourceModule.slotNumber++, "iconRockGray"),
      ROCK_TAN(ResourceModule.slotNumber++, "iconRockGray"),
      STONE_TAN(ResourceModule.slotNumber++, "iconStoneGray"),
      ROCKY_TRASH_TAN(ResourceModule.slotNumber++, "iconRockGray"),
      IRON_ORE(ResourceModule.slotNumber++, "iconIronOre"),
      IRON_INGOT(ResourceModule.slotNumber++, "iconIronIngot"),
      GOLD_ORE(ResourceModule.slotNumber++, "iconGoldOre"),
      GOLD_INGOT(ResourceModule.slotNumber++, "iconGoldIngot"),
      GOLD_SACK(ResourceModule.slotNumber++, "iconGoldCoinSack"),
      CRYSTAL_RED(ResourceModule.slotNumber++, "iconCrystalRed"),
      CRYLITHIUM_RED(ResourceModule.slotNumber++, "iconCrylithiumRed"),
      CRYSTALLY_TRASH_RED(ResourceModule.slotNumber++, "iconCrystallyTrashRed"),
      CRYSTAL_GREEN(ResourceModule.slotNumber++, "iconCrystalGreen"),
      CRYLITHIUM_GREEN(ResourceModule.slotNumber++, "iconCrylithiumGreen"),
      CRYSTALLY_TRASH_GREEN(ResourceModule.slotNumber++, "iconCrystallyTrashGreen"),
      CRYSTAL_BLUE(ResourceModule.slotNumber++, "iconCrystalBlue"),
      CRYLITHIUM_BLUE(ResourceModule.slotNumber++, "iconCrylithiumBlue"),
      CRYSTALLY_TRASH_BLUE(ResourceModule.slotNumber++, "iconCrystallyTrashBlue"),
      CRYSTAL_PURPLE(ResourceModule.slotNumber++, "iconCrystalPurple"),
      CRYLITHIUM_PURPLE(ResourceModule.slotNumber++, "iconCrylithiumPurple"),
      CRYSTALLY_TRASH_PURPLE(ResourceModule.slotNumber++, "iconCrystallyTrashPurple"),
      CARROT_CROP_YELLOW_ONE(ResourceModule.slotNumber++, "iconCarrotYellow"),
      CARROT_CROP_YELLOW_TWO(ResourceModule.slotNumber++, "iconCarrotYellow"),
      CARROT_CROP_YELLOW_THREE(ResourceModule.slotNumber++, "iconCarrotYellow"),
      CARROT_YELLOW(ResourceModule.slotNumber++, CARROT_CROP_YELLOW_ONE, CARROT_CROP_YELLOW_TWO, CARROT_CROP_YELLOW_THREE, "iconCarrotYellow"),
      POTATO_CROP_ONE(ResourceModule.slotNumber++, "iconCarrotYellow"),
      POTATO_CROP_TWO(ResourceModule.slotNumber++, "iconCarrotYellow"),
      POTATO_CROP_THREE(ResourceModule.slotNumber++, "iconCarrotYellow"),
      POTATO(ResourceModule.slotNumber++, POTATO_CROP_ONE, POTATO_CROP_TWO, POTATO_CROP_THREE, "iconCarrotYellow"),
      POTATO_HOLY_CROP_ONE(ResourceModule.slotNumber++, "iconCarrotYellow"),
      POTATO_HOLY_CROP_TWO(ResourceModule.slotNumber++, "iconCarrotYellow"),
      POTATO_HOLY_CROP_THREE(ResourceModule.slotNumber++, "iconCarrotYellow"),
      POTATO_HOLY(ResourceModule.slotNumber++, POTATO_HOLY_CROP_ONE, POTATO_HOLY_CROP_TWO, POTATO_HOLY_CROP_THREE, "iconCarrotYellow"),
      TURNIP_CROP_ONE(ResourceModule.slotNumber++, "iconCarrotYellow"),
      TURNIP_CROP_TWO(ResourceModule.slotNumber++, "iconCarrotYellow"),
      TURNIP_CROP_THREE(ResourceModule.slotNumber++, "iconCarrotYellow"),
      TURNIP(ResourceModule.slotNumber++, TURNIP_CROP_ONE, TURNIP_CROP_TWO, TURNIP_CROP_THREE, "iconCarrotYellow"),
      CACTUS_CROP_GREEN_ONE(ResourceModule.slotNumber++, "iconCarrotYellow"),
      CACTUS_CROP_GREEN_TWO(ResourceModule.slotNumber++, "iconCarrotYellow"),
      CACTUS_CROP_GREEN_THREE(ResourceModule.slotNumber++, "iconCarrotYellow"),
      CACTUS_GREEN(ResourceModule.slotNumber++, CACTUS_CROP_GREEN_ONE, CACTUS_CROP_GREEN_TWO, CACTUS_CROP_GREEN_THREE, "iconCarrotYellow"),
      MELONS_CROP_YELLOW_ONE(ResourceModule.slotNumber++, "iconCarrotYellow"),
      MELONS_CROP_YELLOW_TWO(ResourceModule.slotNumber++, "iconCarrotYellow"),
      MELONS_CROP_YELLOW_THREE(ResourceModule.slotNumber++, "iconCarrotYellow"),
      MELONS_YELLOW(ResourceModule.slotNumber++, MELONS_CROP_YELLOW_ONE, MELONS_CROP_YELLOW_TWO, MELONS_CROP_YELLOW_THREE, "iconCarrotYellow"),
      MUSHROOMS_CROP_ONE(ResourceModule.slotNumber++, "iconCarrotYellow"),
      MUSHROOMS_CROP_TWO(ResourceModule.slotNumber++, "iconCarrotYellow"),
      MUSHROOMS_CROP_THREE(ResourceModule.slotNumber++, "iconCarrotYellow"),
      MUSHROOMS(ResourceModule.slotNumber++, MUSHROOMS_CROP_ONE, MUSHROOMS_CROP_TWO, MUSHROOMS_CROP_THREE, "iconCarrotYellow"),
      ORGANICY_TRASH(ResourceModule.slotNumber++, "iconOrganicyTrash"),
      ARROW_BUNDLE(ResourceModule.slotNumber++, "iconArrowBundle"),
      STONE_BALLS(ResourceModule.slotNumber++, "iconStoneBalls"),
      IRON_SWORD(ResourceModule.slotNumber++, "iconIronSword"),
      WOOD_SWORD(ResourceModule.slotNumber++, "iconWoodSword"),
      PICK_AXE(ResourceModule.slotNumber++, "iconPickAxe"),
      AXE(ResourceModule.slotNumber++, "iconAxe"),
      HOE(ResourceModule.slotNumber++, "iconHoe"),
      HAMMER(ResourceModule.slotNumber++, "iconHammer"),
      SHOVEL(ResourceModule.slotNumber++, "iconShovel"),
      LEATHER_HELMET(ResourceModule.slotNumber++, "iconHelmet"),
      LEATHER_CHEST_ARMOR(ResourceModule.slotNumber++, "iconChestArmor"),
      IRON_HELMET(ResourceModule.slotNumber++, "iconHelmet"),
      IRON_CHEST_ARMOR(ResourceModule.slotNumber++, "iconChestArmor"),
      WOOD_SHIELD(ResourceModule.slotNumber++, "iconWoodShield"),
      IRON_SHIELD(ResourceModule.slotNumber++, "iconIronShield"),
      BOW(ResourceModule.slotNumber++, "iconBow"),
      QUIVER(ResourceModule.slotNumber++, "iconQuiver"),
      HEALING_POTION(ResourceModule.slotNumber++, "iconHealingPotion"),
      YELLOW_POTION_UNUSED(ResourceModule.slotNumber++, "null"),
      BLUE_POTION_UNUSED(ResourceModule.slotNumber++, "null"),
      WATER_BUCKET(ResourceModule.slotNumber++, "iconWaterBucket"),
      DIRTY_WATER_BUCKET(ResourceModule.slotNumber++, "iconDirtyWaterBucket"),
      WATER_BOTTLE(ResourceModule.slotNumber++, "iconWaterBottle"),
      BANDAGE(ResourceModule.slotNumber++, "iconHealingPotion"),
      MEDKIT(ResourceModule.slotNumber++, "iconMedkit"),
      RATION(ResourceModule.slotNumber++, "iconHealingPotion"),
      SILK(ResourceModule.slotNumber++, "iconSilk"),
      JAR_UNUSED(ResourceModule.slotNumber++, "null"),
      SUSPICICIOUS_KEY(ResourceModule.slotNumber++, "iconSuspiciousKey"),
      SUSPICICIOUSY_TRASH(ResourceModule.slotNumber++, "iconSuspiciousyTrash"),
      TRASHY_TRASH(ResourceModule.slotNumber++, "iconTrashyTrash"),
      TRASHY_CUBE(ResourceModule.slotNumber++, "iconTrashyCube"),
      RAW_MEAT(ResourceModule.slotNumber++, "iconRawMeat"),
      COOKED_MEAT(ResourceModule.slotNumber++, "iconCookedMeat"),
      FEATHER(ResourceModule.slotNumber++, "iconFeather"),
      RAW_HIDE(ResourceModule.slotNumber++, "iconRawHide"),
      EGG(ResourceModule.slotNumber++, "iconEgg"),
      BOILED_EGG(ResourceModule.slotNumber++, "iconBoiledEgg"),
      CAPTURED_ANIMAL(ResourceModule.slotNumber++, "iconCapturedAnimal"),
      GOD_DUST(ResourceModule.slotNumber++, "iconGodDust"),
      GHOST_DUST(ResourceModule.slotNumber++, "iconGhostDust"),
      EMPTY_EERIE_VESSEL(ResourceModule.slotNumber++, "iconEmptyEerieVessel"),
      EERIE_VESSEL(ResourceModule.slotNumber++, "iconEerieVessel");

      private int spriteID;
      private String iconName;
      private ResourceModule.ResourceSpriteSet cropOne = null;
      private ResourceModule.ResourceSpriteSet cropTwo = null;
      private ResourceModule.ResourceSpriteSet cropThree = null;

      
      private ResourceSpriteSet(int spriteID, String iconName) {
         this.spriteID = spriteID;
         this.iconName = iconName;
      }

      private ResourceSpriteSet(
         int spriteID,
         ResourceModule.ResourceSpriteSet cropOne,
         ResourceModule.ResourceSpriteSet cropTwo,
         ResourceModule.ResourceSpriteSet cropThree,
         String iconName
      ) {
         this.spriteID = spriteID;
         this.cropOne = cropOne;
         this.cropTwo = cropTwo;
         this.cropThree = cropThree;
         this.iconName = iconName;
      }

      public String getIconName() {
         return this.iconName;
      }

      public ResourceModule.ResourceSpriteSet getCropOne() {
         return this.cropOne;
      }

      public ResourceModule.ResourceSpriteSet getCropTwo() {
         return this.cropTwo;
      }

      public ResourceModule.ResourceSpriteSet getCropThree() {
         return this.cropThree;
      }

      public int getID() {
         return this.spriteID;
      }
   }

   public static enum ResourceType {
      TRASHY_TRASH(new ResourceTemplateTrashyTrash()),
      TRASHY_CUBE(new ResourceTemplateTrashyCube()),
      WOODY_TRASH(new ResourceTemplateWoodyTrash()),
      WOOD(new ResourceTemplateWood()),
      BOARD(new ResourceTemplateBoard()),
      ROCKY_TRASH(new ResourceTemplateRockyTrash()),
      ROCK(new ResourceTemplateRock()),
      CUT_STONE(new ResourceTemplateCutStone()),
      IRON_ORE(new ResourceTemplateIronOre()),
      IRON_INGOT(new ResourceTemplateIronIngot()),
      GOLD_ORE(new ResourceTemplateGoldOre()),
      GOLD_INGOT(new ResourceTemplateGoldIngot()),
      GOLD_COIN_SACK(new ResourceTemplateGoldCoinSack()),
      CRYSTALLY_TRASH(new ResourceTemplateCrystallyTrash()),
      CRYSTAL(new ResourceTemplateCrystal()),
      CRYLITHIUM(new ResourceTemplateCrylithium()),
      ORGANICY_TRASH(new ResourceTemplateOrganicyTrash()),
      RAW_VEGETABLE(new ResourceTemplateRawVegetable()),
      RATION(new ResourceTemplateRation()),
      BALLISTAE_BOLTS(new ResourceTemplateBallistaeBolts()),
      STONE_BALLS(new ResourceTemplateStoneBalls()),
      IRON_SWORD(new ResourceTemplateIronSword()),
      WOOD_SWORD(new ResourceTemplateWoodSword()),
      PICK_AXE(new ResourceTemplatePickAxe()),
      AXE(new ResourceTemplateAxe()),
      HOE(new ResourceTemplateHoe()),
      HAMMER(new ResourceTemplateHammer()),
      SHOVEL(new ResourceTemplateShovel()),
      LEATHER_HELMET(new ResourceTemplateLeatherHelmet()),
      LEATHER_CHEST_ARMOR(new ResourceTemplateLeatherChestArmor()),
      IRON_HELMET(new ResourceTemplateIronHelmet()),
      IRON_CHEST_ARMOR(new ResourceTemplateIronChestArmor()),
      LEATHER_SHIELD(new ResourceTemplateLeatherShield()),
      IRON_SHIELD(new ResourceTemplateIronShield()),
      QUIVER(new ResourceTemplateQuiver()),
      BOW(new ResourceTemplateBow()),
      SILK(new ResourceTemplateSilk()),
      BANDAGE(new ResourceTemplateBandage()),
      MEDKIT(new ResourceTemplateMedkit()),
      HEALING_POTION(new ResourceTemplateHealingPotion()),
      DIRTY_WATER_BUCKET(new ResourceTemplateDirtyWaterBucket()),
      WATER_BUCKET(new ResourceTemplateWaterBucket()),
      WATER_BOTTLE(new ResourceTemplateWaterBottle()),
      SUSPICICIOUSY_TRASH(new ResourceTemplateSuspiciousyTrash()),
      SUSPICICIOUS_KEY(new ResourceTemplateSuspiciousKey()),
      RAW_MEAT(new ResourceTemplateRawMeat()),
      COOKED_MEAT(new ResourceTemplateCookedMeat()),
      FEATHER(new ResourceTemplateFeather()),
      RAW_HIDE(new ResourceTemplateRawHide()),
      EGG(new ResourceTemplateEgg()),
      BOILED_EGG(new ResourceTemplateBoiledEgg()),
      CAPTURED_ANIMAL(new ResourceTemplateCapturedAnimal()),
      GOD_DUST(new ResourceTemplateGodDust()),
      GHOST_DUST(new ResourceTemplateGhostDust()),
      EMPTY_EERIE_VESSEL(new ResourceTemplateEmptyEerieVessel()),
      EERIE_VESSEL(new ResourceTemplateEerieVessel());

      private final ResourceTemplateBase template;

      private ResourceType(ResourceTemplateBase template) {
         this.template = template;
      }

      public ResourceTemplateBase getTemplate() {
         if (!this.template.isLoaded()) {
            this.template.load();
         }

         return this.template;
      }
   }
}
