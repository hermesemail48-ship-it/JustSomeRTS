package rtr.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import rtr.GodModule;
import rtr.ImageLoader;
import rtr.ModuleBase;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.StatsModule;
import rtr.TimeModule;
import rtr.console.Console;
import rtr.font.FontModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.influence.InfluenceModule;
import rtr.influence.SpellBase;
import rtr.lighting.LightingModule;
import rtr.map.CollisionModule;
import rtr.map.MapData;
import rtr.map.MapModule;
import rtr.map.MapTilesLoader;
import rtr.map.ai.MapAIModule;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.mobs.MobModule;
import rtr.mobs.essence.EssenceModule;
import rtr.mobs.jobs.MobJobBase;
import rtr.mobs.jobs.MobJobModule;
import rtr.objects.gates.GateBase;
import rtr.objects.gates.StoneGateNS;
import rtr.objects.gates.StoneGateWE;
import rtr.objects.gates.WoodGateNS;
import rtr.objects.gates.WoodGateWE;
import rtr.objects.objectflags.ObjectFlags;
import rtr.objects.towerhead.TowerAttract;
import rtr.objects.towerhead.TowerBallista;
import rtr.objects.towerhead.TowerBanish;
import rtr.objects.towerhead.TowerBase;
import rtr.objects.towerhead.TowerBow;
import rtr.objects.towerhead.TowerBullet;
import rtr.objects.towerhead.TowerCorruptedFireBow;
import rtr.objects.towerhead.TowerCorruptedFirePhantomDart;
import rtr.objects.towerhead.TowerCorruptedIceBow;
import rtr.objects.towerhead.TowerCorruptedIcePhantomDart;
import rtr.objects.towerhead.TowerCorruptedLightningBow;
import rtr.objects.towerhead.TowerCorruptedLightningPhantomDart;
import rtr.objects.towerhead.TowerCorruptedPoisonBow;
import rtr.objects.towerhead.TowerCorruptedPoisonPhantomDart;
import rtr.objects.towerhead.TowerElementalBolt;
import rtr.objects.towerhead.TowerGodBow;
import rtr.objects.towerhead.TowerMotivator;
import rtr.objects.towerhead.TowerPhantomDart;
import rtr.objects.towerhead.TowerRecombobulator;
import rtr.objects.towerhead.TowerSling;
import rtr.objects.towerhead.TowerSpray;
import rtr.objects.towerhead.TowerStatic;
import rtr.particles.ParticleModule;
import rtr.particles.projectiles.ProjectileModule;
import rtr.resources.FarmlandBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.save.SaveModule;
import rtr.save.YMLDataMap;
import rtr.spawning.CorruptionModule;
import rtr.spawning.SpawnModule;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.system.ScaleControl;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;
import rtr.weather.WeatherModule;
import rtr.weather.disasters.DisasterBase;

public class ObjectBase {
   protected MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   protected SpawnModule spawn = (SpawnModule)StateBase.getModule(ModuleBase.ModuleType.SPAWN);
   protected MapAIModule mapAI = (MapAIModule)StateBase.getModule(ModuleBase.ModuleType.MAP_AI);
   protected MobModule mob = (MobModule)StateBase.getModule(ModuleBase.ModuleType.MOB);
   protected MobJobModule mobJob = (MobJobModule)StateBase.getModule(ModuleBase.ModuleType.MOB_JOB);
   protected MissileModule missile = (MissileModule)StateBase.getModule(ModuleBase.ModuleType.MISSILE);
   protected EssenceModule essence = (EssenceModule)StateBase.getModule(ModuleBase.ModuleType.ESSENCE);
   protected ResourceModule resource = (ResourceModule)StateBase.getModule(ModuleBase.ModuleType.RESOURCE);
   protected ObjectModule object = (ObjectModule)StateBase.getModule(ModuleBase.ModuleType.OBJECT);
   protected FontModule font = (FontModule)StateBase.getModule(ModuleBase.ModuleType.FONT);
   protected SoundModule sound = (SoundModule)StateBase.getModule(ModuleBase.ModuleType.SOUND);
   protected ParticleModule particle = (ParticleModule)StateBase.getModule(ModuleBase.ModuleType.PARTICLE);
   protected ProjectileModule projectile = (ProjectileModule)StateBase.getModule(ModuleBase.ModuleType.PROJECTILE);
   protected WeatherModule weather = (WeatherModule)StateBase.getModule(ModuleBase.ModuleType.WEATHER);
   protected LightingModule lighting = (LightingModule)StateBase.getModule(ModuleBase.ModuleType.LIGHTING);
   protected InfluenceModule influence = (InfluenceModule)StateBase.getModule(ModuleBase.ModuleType.INFLUENCE);
   protected TimeModule time = (TimeModule)StateBase.getModule(ModuleBase.ModuleType.TIME);
   protected SaveModule save = (SaveModule)StateBase.getModule(ModuleBase.ModuleType.SAVE);
   protected CollisionModule collision = (CollisionModule)StateBase.getModule(ModuleBase.ModuleType.COLLISION);
   protected StatsModule stats = (StatsModule)StateBase.getModule(ModuleBase.ModuleType.STATS);
   protected CorruptionModule corruption = (CorruptionModule)StateBase.getModule(ModuleBase.ModuleType.CORRUPTION);
   protected GodModule god = (GodModule)StateBase.getModule(ModuleBase.ModuleType.GOD);
   protected GoalModule goal = (GoalModule)StateBase.getModule(ModuleBase.ModuleType.GOAL);
   protected PerkModule perk = (PerkModule)StateBase.getModule(ModuleBase.ModuleType.PERK);
   protected ObjectFlags objectFlags;
   protected Image reclaimIcon;
   protected Image pauseIcon;
   protected Image dismantleIcon;
   protected Image dismantleIconSmall;
   private EnumMap<ResourceModule.ResourceType, Image> resourceValueIcons = new EnumMap<>(ResourceModule.ResourceType.class);
   protected Rectangle intersectBox;
   protected ArrayList<OrderedPair> smokeDamageArray = new ArrayList<>();
   protected ArrayList<OrderedPair> fireDamageArray = new ArrayList<>();
   protected int objectID = -1;
   protected MapTilesLoader.TileSetObjectPhase currentPhase;
   protected int objectX;
   protected int objectY;
   protected int hitPoints;
   protected int hitPointsMax;
   protected ArrayList<String> killedNameList = new ArrayList<>();
   protected int kills;
   protected ArrayList<OrderedPair> interactPoints = new ArrayList<>();
   protected OrderedPair functionalCoordinates;
   protected boolean destroyed;
   protected boolean beDismantled;
   protected boolean paused;
   protected int totalValuesOffset;
   protected EnumMap<ResourceModule.ResourceType, ArrayList<ResourceBase>> resources = new EnumMap<>(ResourceModule.ResourceType.class);
   protected EnumMap<ResourceModule.ResourceType, int[]> resourcesIDs = new EnumMap<>(ResourceModule.ResourceType.class);
   protected boolean inRange;
   protected TowerBase tower;
   protected int ammo;
   protected GateBase gate;
   private ArrayList<OrderedPair> courierGolemParking = new ArrayList<>();
   private ArrayList<OrderedPair> prayPoints = new ArrayList<>();
   private ArrayList<OrderedPair> vesselPoints = new ArrayList<>();
   protected int vesselRegenerationRateTick;
   private ArrayList<FarmlandBase> localFarmland = new ArrayList<>();
   protected int[] localFarmlandIDs;
   protected EnumMap<ResourceModule.ResourceType, Integer> resourceValues = new EnumMap<>(ResourceModule.ResourceType.class);
   protected EnumMap<ResourceModule.ResourceType, ArrayList<ResourceModule.ResourceColorSet>> resourceValuesColors = new EnumMap<>(
      ResourceModule.ResourceType.class
   );
   protected ArrayList<MobBase> occupants = new ArrayList<>();
   protected int[] occupantsIDs;
   protected int lightningRodDissipationRateTick;
   protected int instabilityAmount;
   protected boolean instabilityEarthquakeTriggered;
   protected boolean lootBoxActive;
   protected ResourceModule.ResourceCategory lootBoxCategory;
   protected int lootBoxCountTick;
   protected int lootBoxClicks;
   protected int lootBoxCooldown;
   protected ArrayList<MobBase> combobulatorSpawns = new ArrayList<>();
   protected int[] combobulatorSpawnsIDs;
   protected int combobulatorEnergyRequiredTick;
   protected int combobulatorEnergyRateTick;
   protected int combobulatorEnergyLossPerGolemRateTick;
   protected int essenceMouseDelayTick;
   protected int energy;
   protected int energyInRoute;
   protected int energyRegenerationRateTick;
   protected int energyConsumeRateTick;
   protected int rainCatchRateTick;
   protected int waterGenerationRateTick;
   protected int purifyWaterRateTick;
   protected int burnerRateTick;
   protected int burnerEnergyDissipationRateTick;
   protected int destroyIfIgnoredDelayTick;
   protected int damageMonsterSpawnsDelayTick;

   public ObjectFlags getObjectFlags() {
      return this.objectFlags;
   }

   public Rectangle getIntersectBox() {
      return this.intersectBox;
   }

   public int getID() {
      return this.objectID;
   }

   public MapTilesLoader.TileSetObjectPhase getCurrentPhase() {
      return this.currentPhase;
   }

   public int getObjectX() {
      return this.objectX;
   }

   public int getObjectY() {
      return this.objectY;
   }

   public int getHitPoints() {
      return this.hitPoints;
   }

   public int getHitPointsMax() {
      return this.hitPointsMax;
   }

   public ArrayList<String> getKilledNameList() {
      return this.killedNameList;
   }

   public int getKills() {
      return this.kills;
   }

   public void setKills(int i) {
      this.kills = i;
   }

   public void increaseKills(int i, String killedName) {
      this.kills += i;
      if (this.killedNameList.size() >= 5) {
         this.killedNameList.remove(0);
      }

      this.killedNameList.add(killedName);
   }

   public void decreaseKills(int i) {
      if (this.kills > 0) {
         this.kills -= i;
         if (this.kills < 0) {
            this.kills = 0;
         }
      }
   }

   public ArrayList<OrderedPair> getInteractPoints() {
      return this.interactPoints;
   }

   public boolean hasInteractPoints() {
      return this.interactPoints.size() > 0;
   }

   public OrderedPair getFunctionalCoordinates() {
      return this.functionalCoordinates;
   }

   public int getFunctionalXOnMap() {
      return this.functionalCoordinates.getX();
   }

   public int getFunctionalYOnMap() {
      return this.functionalCoordinates.getY();
   }

   public boolean isDestroyed() {
      return this.destroyed;
   }

   public void setDestroyed() {
      this.destroyed = true;
   }

   public boolean canBeDismantle() {
      return this.beDismantled;
   }

   public boolean isPaused() {
      return this.paused;
   }

   public void togglePaused() {
      this.paused = !this.paused;
   }

   public int getTotalValuesOffset() {
      return this.totalValuesOffset;
   }

   private boolean underConstructionResourceFlagOverride(ResourceModule.ResourceType resourceType) {
      return this.objectFlags.getSubType() == ObjectBase.ObjectSubType.CONSTRUCTION && this.objectFlags.canBuildingBeBuilt(resourceType);
   }

   public TowerBase getTower() {
      return this.tower;
   }

   public int getAmmo() {
      return this.ammo;
   }

   public void increaseAmmo(int amount) throws SlickException {
      this.ammo += amount;
      if (this.ammo > this.objectFlags.getAmmoMax()) {
         this.ammo = this.objectFlags.getAmmoMax();
      }

      this.updateObject();
   }

   public void decreaseAmmo(int amount) throws SlickException {
      this.ammo -= amount;
      if (this.ammo < 0) {
         this.ammo = 0;
      }

      this.updateObject();
   }

   public GateBase getGate() {
      return this.gate;
   }

   public ArrayList<OrderedPair> getCourierGolemParking() {
      return this.courierGolemParking;
   }

   public ArrayList<OrderedPair> getPrayPoints() {
      return this.prayPoints;
   }

   public ArrayList<OrderedPair> getVesselPoints() {
      return this.vesselPoints;
   }

   public int getVesselRegenerationRateTick() {
      return this.vesselRegenerationRateTick;
   }

   public ArrayList<FarmlandBase> getLocalFarmland() {
      return this.localFarmland;
   }

   public int getResourceValueCount(ResourceModule.ResourceType type) {
      return this.resourceValues.get(type);
   }

   public int getTotalValue() {
      int totalResources = 0;

      ResourceModule.ResourceType[] var5;
      for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
         totalResources += this.getResourceValueCount(type);
      }

      return totalResources;
   }

   public ArrayList<MobBase> getOccupants() {
      return this.occupants;
   }

   public int getLightningRodXOnMap() {
      return this.objectX + this.objectFlags.getLightningRodXOnBuilding();
   }

   public int getLightningRodYOnMap() {
      return this.objectY + this.objectFlags.getLightningRodYOnBuilding();
   }

   public int getLightningRodDissipationRateTick() {
      return this.lightningRodDissipationRateTick;
   }

   public int getInstabilityAmount() {
      return this.instabilityAmount;
   }

   public boolean isInstabilityEarthquakeTriggered() {
      return this.instabilityEarthquakeTriggered;
   }

   public ResourceModule.ResourceCategory getLootBoxCategory() {
      return this.lootBoxCategory;
   }

   public int getLootBoxCountTick() {
      return this.lootBoxCountTick;
   }

   public int getLootBoxClicks() {
      return this.lootBoxClicks;
   }

   public int getLootBoxCooldown() {
      return this.lootBoxCooldown;
   }

   public ArrayList<MobBase> getCombobulatorSpawns() {
      return this.combobulatorSpawns;
   }

   public int getCombobulatorEnergyRequiredTick() {
      return this.combobulatorEnergyRequiredTick;
   }

   public int getCombobulatorEnergyRateTick() {
      return this.combobulatorEnergyRateTick;
   }

   public int getCombobulatorEnergyLossPerGolemRateTick() {
      return this.combobulatorEnergyLossPerGolemRateTick;
   }

   public int getEssenceMouseDelayTick() {
      return this.essenceMouseDelayTick;
   }

   public int getEnergy() {
      return this.energy;
   }

   public void increaseEnergy(int amount) throws SlickException {
      this.energy += amount;
      if (this.energy > this.objectFlags.getEnergyMax()) {
         this.energy = this.objectFlags.getEnergyMax();
      }

      this.updateObject();
   }

   public void decreaseEnergy(int amount) throws SlickException {
      this.energy -= amount;
      if (this.energy < 0) {
         this.energy = 0;
      }

      this.updateObject();
   }

   public int getEnergyInRoute() {
      return this.energyInRoute;
   }

   public void increaseEnergyInRoute(int amount) {
      this.energyInRoute += amount;
   }

   public void decreaseEnergyInRoute(int amount) {
      this.energyInRoute -= amount;
   }

   public int getEnergyRegenerationRateTick() {
      return this.energyRegenerationRateTick;
   }

   public int getEnergyConsumeRateTick() {
      return this.energyConsumeRateTick;
   }

   public int getEssenceReturnXOnMap() {
      return this.objectX + this.objectFlags.getEssenceReturnXOnBuilding();
   }

   public int getEssenceReturnYOnMap() {
      return this.objectY + this.objectFlags.getEssenceReturnYOnBuilding();
   }

   public int getEssenceReturnXOnMapExactPixel() {
      return this.getEssenceReturnXOnMap() * 16 + this.objectFlags.getEssenceReturnXPixelOffset();
   }

   public int getEssenceReturnYOnMapExactPixel() {
      return this.getEssenceReturnYOnMap() * 16 + this.objectFlags.getEssenceReturnYPixelOffset();
   }

   public int getRainCatchRateTick() {
      return this.rainCatchRateTick;
   }

   public int getWaterGenerationRateTick() {
      return this.waterGenerationRateTick;
   }

   public int getPurifyWaterRateTick() {
      return this.purifyWaterRateTick;
   }

   public int getBurnerRateTick() {
      return this.burnerRateTick;
   }

   public int getBurnerEnergyDissipationRateTick() {
      return this.burnerEnergyDissipationRateTick;
   }

   public int getDestroyIfIgnoredDelayTick() {
      return this.destroyIfIgnoredDelayTick;
   }

   public int getDamageMonsterSpawnsDelayTick() {
      return this.damageMonsterSpawnsDelayTick;
   }

   public ObjectBase(int tileX, int tileY, int id, MapTilesLoader.TileSet uT, ObjectBase.ObjectSubType sT) throws SlickException {
      this.objectID = id;
      this.objectX = tileX;
      this.objectY = tileY;
      this.objectFlags = this.object.getObjectFlagFactory().getObjectFlags(uT, sT);
      this.intersectBox = new Rectangle(0.0F, 0.0F, this.objectFlags.getWidth() * 16, this.objectFlags.getHeight() * 16);
      this.reclaimIcon = ImageLoader.getImage("res/GUI/reclaim.png");
      this.pauseIcon = ImageLoader.getImage("res/GUI/pause.png");
      this.dismantleIcon = ImageLoader.getImage("res/GUI/dismantle.png");
      this.dismantleIconSmall = ImageLoader.getImage("res/GUI/dismantleSmall.png");

      ResourceModule.ResourceType[] var9;
      for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
         this.resourceValueIcons
            .put(type, this.resource.getResourceSprite(this.resource.getResourceSpriteSet(type, ResourceModule.ResourceColorSet.DEFAULT), 0));
         this.resources.put(type, new ArrayList<>());
         this.resourceValues.put(type, 0);
         this.resourceValuesColors.put(type, new ArrayList<>());
      }

      this.initObject();
      this.updateObjectMapData();
   }

   public ObjectBase(YMLDataMap dataMap) throws SlickException {
      ResourceModule.ResourceType[] var5;
      for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
         this.resourceValueIcons
            .put(type, this.resource.getResourceSprite(this.resource.getResourceSpriteSet(type, ResourceModule.ResourceColorSet.DEFAULT), 0));
         this.resources.put(type, new ArrayList<>());
         this.resourceValues.put(type, 0);
         this.resourceValuesColors.put(type, new ArrayList<>());
      }

      this.loadObject(dataMap);
      this.updateObjectMapData();
      this.intersectBox = new Rectangle(0.0F, 0.0F, this.objectFlags.getWidth() * 16, this.objectFlags.getHeight() * 16);
      this.reclaimIcon = ImageLoader.getImage("res/GUI/reclaim.png");
      this.pauseIcon = ImageLoader.getImage("res/GUI/pause.png");
      this.dismantleIcon = ImageLoader.getImage("res/GUI/dismantle.png");
      this.dismantleIconSmall = ImageLoader.getImage("res/GUI/dismantleSmall.png");
   }

   private void loadObject(YMLDataMap dataMap) throws SlickException {
      String currentTypeString = dataMap.getOrDefault("currentType", "null");
      if (currentTypeString.contains("BARRACKS")) {
         Console.saveConverterOut(this, "InDev 33f", "InDev 34 Unstable 1", "Converting old Barracks building into a ranger lodge.");
         currentTypeString = currentTypeString.replace("BARRACKS", "RANGER_LODGE");
      }

      boolean forceUpdate = false;
      if (!dataMap.getOrDefault("version", "noVersion").contains("Update")) {
         Console.saveConverterOut(this, "Release 1", "Update 1 Unstable 1", "Starting one-time conversion of all curtain walls to crylithium curtain walls.");
         if (currentTypeString.equals("CURTAIN_WALL")) {
            Console.saveConverterOut(this, "Release 1", "Update 1 Unstable 1", "One time converting old Curtain Wall to Crylithium Curtain Wall.");
            currentTypeString = currentTypeString.replace("CURTAIN_WALL", "CRYLITHIUM_CURTAIN_WALL");
            forceUpdate = true;
         }
      }

      MapTilesLoader.TileSet currentType = null;

      MapTilesLoader.TileSet[] lootBoxCategoryString;
      for (MapTilesLoader.TileSet t : lootBoxCategoryString = MapTilesLoader.TileSet.values()) {
         if (t.toString().equals(currentTypeString)) {
            currentType = t;
            break;
         }
      }

      String subTypeString = dataMap.getOrDefault("subType", "null");
      ObjectBase.ObjectSubType subType = null;

      ObjectBase.ObjectSubType[] totalValueBaseModified;
      for (ObjectBase.ObjectSubType s : totalValueBaseModified = ObjectBase.ObjectSubType.values()) {
         if (s.toString().equals(subTypeString)) {
            subType = s;
            break;
         }
      }

      this.objectFlags = this.object.getObjectFlagFactory().getObjectFlags(currentType, subType);
      if (dataMap.containsKey("fireDamageArray")) {
         this.smokeDamageArray = dataMap.unpackCoordinatesList("fireDamageArray", true);
      }

      if (dataMap.containsKey("fireDamageArray")) {
         this.fireDamageArray = dataMap.unpackCoordinatesList("fireDamageArray", true);
      }

      this.objectID = Integer.parseInt(dataMap.getOrDefault("objectID", 0));
      String currentPhaseString = dataMap.getOrDefault("currentPhase", "null");

      MapTilesLoader.TileSetObjectPhase[] var11;
      for (MapTilesLoader.TileSetObjectPhase cP : MapTilesLoader.TileSetObjectPhase.values()) {
         if (cP.toString().equals(currentPhaseString)) {
            this.currentPhase = cP;
            break;
         }
      }

      this.objectX = Integer.parseInt(dataMap.getOrDefault("objectX", 0));
      this.objectY = Integer.parseInt(dataMap.getOrDefault("objectY", 0));
      this.hitPoints = Integer.parseInt(dataMap.getOrDefault("hitPoints", 1));
      this.hitPointsMax = Integer.parseInt(dataMap.getOrDefault("hitPointsMax", 1));
      if (dataMap.containsKey("killedNameList")) {
         this.killedNameList = dataMap.unpackStringArray("killedNameList");
      }

      this.kills = Integer.parseInt(dataMap.getOrDefault("kills", 0));
      this.destroyed = Boolean.parseBoolean(dataMap.getOrDefault("destroyed", false));
      if (dataMap.containsKey("canDismantle")) {
         Console.saveConverterOut(this, "InDev 33f", "InDev 34 Unstable 1", "Loading old canDismantle glad in lue of beDismantled");
         this.beDismantled = Boolean.parseBoolean(dataMap.getOrDefault("canDismantle", false));
      } else {
         this.beDismantled = Boolean.parseBoolean(dataMap.getOrDefault("beDismantled", false));
      }

      this.paused = Boolean.parseBoolean(dataMap.getOrDefault("paused", false));
      this.totalValuesOffset = Integer.parseInt(dataMap.getOrDefault("totalValuesOffset", 0));
      if (dataMap.containsMap("resourcesIDs")) {
         YMLDataMap resourcesIDsMap = dataMap.getSubMap("resourcesIDs");

         ResourceModule.ResourceType[] i;
         for (ResourceModule.ResourceType type : i = ResourceModule.ResourceType.values()) {
            if (resourcesIDsMap.containsKey(type.toString())) {
               this.resourcesIDs.put(type, resourcesIDsMap.unpackObjectArrayIDs(type.toString()));
            }
         }

         if (resourcesIDsMap.containsKey("RAW_FOOD")) {
            Console.saveConverterOut(this, "InDev 33f", "InDev 34b", "Converting RAW_FOOD to RAW_VEGETABLE");
            this.resourcesIDs.put(ResourceModule.ResourceType.RAW_VEGETABLE, resourcesIDsMap.unpackObjectArrayIDs("RAW_FOOD"));
         }

         if (resourcesIDsMap.containsKey("WOOD_SHIELD")) {
            Console.saveConverterOut(this, "InDev 33f", "InDev 34b", "Converting WOOD_SHIELD to LEATHER_SHIELD");
            this.resourcesIDs.put(ResourceModule.ResourceType.LEATHER_SHIELD, resourcesIDsMap.unpackObjectArrayIDs("WOOD_SHIELD"));
         }
      }

      this.inRange = Boolean.parseBoolean(dataMap.getOrDefault("inRange", false));
      if (dataMap.containsMap("tower")) {
         this.createTower(dataMap.getSubMap("tower"));
      }

      this.ammo = Integer.parseInt(dataMap.getOrDefault("ammo", 0));
      if (dataMap.containsMap("gate")) {
         this.createGate(dataMap.getSubMap("gate"));
      }

      this.vesselRegenerationRateTick = Integer.parseInt(dataMap.getOrDefault("vesselRegenerationRateTick", 0));
      if (dataMap.containsKey("localFarmlandIDs")) {
         this.localFarmlandIDs = dataMap.unpackObjectArrayIDs("localFarmlandIDs");
      }

      if (dataMap.containsMap("resourceValues")) {
         YMLDataMap resourceValuesMap = dataMap.getSubMap("resourceValues");

         ResourceModule.ResourceType[] var43;
         for (ResourceModule.ResourceType typex : ResourceModule.ResourceType.values()) {
            if (resourceValuesMap.containsKey(typex.toString())) {
               this.resourceValues.put(typex, Integer.parseInt(resourceValuesMap.getOrDefault(typex.toString(), 0)));
            }
         }
      }

      if (dataMap.containsMap("resourceValuesColors")) {
         YMLDataMap resourceValuesColorsMap = dataMap.getSubMap("resourceValuesColors");

         ResourceModule.ResourceType[] var44;
         for (ResourceModule.ResourceType typexx : ResourceModule.ResourceType.values()) {
            if (resourceValuesColorsMap.containsKey(typexx.toString())) {
               this.resourceValuesColors.put(typexx, resourceValuesColorsMap.unpackResourceColorSetArray(typexx.toString()));
            }
         }
      }

      for (ResourceModule.ResourceType typexxx : ResourceModule.ResourceType.values()) {
         if (this.resourceValues.get(typexxx) > this.resourceValuesColors.get(typexxx).size()) {
            Console.saveConverterOut(
               this,
               "InDev 33f",
               "InDev 34 Unstable 1",
               "Old resourceValuesColors data detected for " + typexxx.toString() + ", backfilling with default data."
            );
            this.resourceValuesColors.get(typexxx).clear();

            for (int ix = 0; ix < this.resourceValues.get(typexxx); ix++) {
               this.resourceValuesColors.get(typexxx).add(ResourceModule.ResourceColorSet.DEFAULT);
            }
         }
      }

      if (dataMap.containsKey("occupantsIDs")) {
         this.occupantsIDs = dataMap.unpackObjectArrayIDs("occupantsIDs");
      }

      this.lightningRodDissipationRateTick = Integer.parseInt(dataMap.getOrDefault("lightningRodDissipationRateTick", 0));
      this.instabilityAmount = Integer.parseInt(dataMap.getOrDefault("instabilityAmount", 0));
      this.instabilityEarthquakeTriggered = Boolean.parseBoolean(dataMap.getOrDefault("instabilityEarthquakeTriggered", false));
      this.lootBoxActive = Boolean.parseBoolean(dataMap.getOrDefault("lootBoxActive", false));
      String lootBoxCategoryStringx = dataMap.getOrDefault("lootBoxCategory", ResourceModule.ResourceCategory.RAW.toString());

      ResourceModule.ResourceCategory[] var46;
      for (ResourceModule.ResourceCategory c : ResourceModule.ResourceCategory.values()) {
         if (c.toString().equals(lootBoxCategoryStringx)) {
            this.lootBoxCategory = c;
            break;
         }
      }

      this.lootBoxCountTick = Integer.parseInt(dataMap.getOrDefault("lootBoxCountTick", 0));
      this.lootBoxClicks = Integer.parseInt(dataMap.getOrDefault("lootBoxClicks", 0));
      this.lootBoxCooldown = Integer.parseInt(dataMap.getOrDefault("lootBoxCooldown", 0));
      if (dataMap.containsKey("combobulatorSpawnsIDs")) {
         this.combobulatorSpawnsIDs = dataMap.unpackObjectArrayIDs("combobulatorSpawnsIDs");
      }

      this.combobulatorEnergyRequiredTick = Integer.parseInt(dataMap.getOrDefault("combobulatorEnergyRequiredTick", 0));
      this.combobulatorEnergyRateTick = Integer.parseInt(dataMap.getOrDefault("combobulatorEnergyRateTick", 0));
      this.combobulatorEnergyLossPerGolemRateTick = Integer.parseInt(dataMap.getOrDefault("combobulatorEnergyLossPerGolemRateTick", 0));
      this.essenceMouseDelayTick = Integer.parseInt(dataMap.getOrDefault("essenceMouseDelayTick", 0));
      this.energy = Integer.parseInt(dataMap.getOrDefault("energy", 0));
      this.energyInRoute = Integer.parseInt(dataMap.getOrDefault("energyInRoute", 0));
      this.energyRegenerationRateTick = Integer.parseInt(dataMap.getOrDefault("energyRegenerationRateTick", 0));
      this.energyConsumeRateTick = Integer.parseInt(dataMap.getOrDefault("energyConsumeRateTick", 0));
      this.rainCatchRateTick = Integer.parseInt(dataMap.getOrDefault("rainCatchRateTick", 0));
      this.waterGenerationRateTick = Integer.parseInt(dataMap.getOrDefault("waterGenerationRateTick", 0));
      this.purifyWaterRateTick = Integer.parseInt(dataMap.getOrDefault("purifyWaterRateTick", 0));
      this.burnerRateTick = Integer.parseInt(dataMap.getOrDefault("burnerRateTick", 0));
      this.burnerEnergyDissipationRateTick = Integer.parseInt(dataMap.getOrDefault("burnerEnergyDissipationRateTick", 0));
      this.destroyIfIgnoredDelayTick = Integer.parseInt(dataMap.getOrDefault("destroyIfIgnoredDelayTick", 0));
      this.damageMonsterSpawnsDelayTick = Integer.parseInt(dataMap.getOrDefault("damageMonsterSpawnsDelayTick", 0));
      if (subType != ObjectBase.ObjectSubType.BUILT) {
         int totalValueModified = this.getTotalValue() - this.totalValuesOffset;
         int totalValueBaseModifiedx = this.objectFlags.getTotalValueBase() - this.totalValuesOffset;
         if (totalValueModified >= totalValueBaseModifiedx) {
            forceUpdate = true;
         }
      }

      if (forceUpdate) {
         this.updateObject();
         this.map.updateObject(this.objectX, this.objectY, currentType, this.currentPhase);
         this.updateObjectMapData();
      }
   }

   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      MapTilesLoader.TileSet currentType = this.objectFlags.getCurrentType();
      if (currentType != null) {
         dataMap.put("currentType", currentType.toString());
      }

      ObjectBase.ObjectSubType subType = this.objectFlags.getSubType();
      if (subType != null) {
         dataMap.put("subType", subType.toString());
      }

      if (this.smokeDamageArray.size() > 0) {
         dataMap.put("smokeDamageArray", dataMap.packCoordinatesList(this.smokeDamageArray));
      }

      if (this.fireDamageArray.size() > 0) {
         dataMap.put("fireDamageArray", dataMap.packCoordinatesList(this.fireDamageArray));
      }

      if (this.objectID != -1) {
         dataMap.put("objectID", this.objectID);
      }

      if (this.currentPhase != null) {
         dataMap.put("currentPhase", this.currentPhase.toString());
      }

      if (this.objectX != 0) {
         dataMap.put("objectX", this.objectX);
      }

      if (this.objectY != 0) {
         dataMap.put("objectY", this.objectY);
      }

      if (this.hitPoints != 0) {
         dataMap.put("hitPoints", this.hitPoints);
      }

      if (this.hitPointsMax != 0) {
         dataMap.put("hitPointsMax", this.hitPointsMax);
      }

      if (this.killedNameList.size() > 0) {
         dataMap.put("killedNameList", dataMap.packEnumArray(this.killedNameList));
      }

      if (this.kills != 0) {
         dataMap.put("kills", this.kills);
      }

      if (this.destroyed) {
         dataMap.put("destroyed", this.destroyed);
      }

      if (this.beDismantled) {
         dataMap.put("beDismantled", this.beDismantled);
      }

      if (this.paused) {
         dataMap.put("paused", this.paused);
      }

      if (this.totalValuesOffset != 0) {
         dataMap.put("totalValuesOffset", this.totalValuesOffset);
      }

      YMLDataMap resourcesIDsMap = new YMLDataMap();

      for (ResourceModule.ResourceType type : this.resources.keySet()) {
         if (this.resources.get(type).size() > 0) {
            resourcesIDsMap.put(type.toString(), dataMap.packObjectArrayIDs(this.resources.get(type)));
         }
      }

      if (resourcesIDsMap.size() > 0) {
         dataMap.put("resourcesIDs", resourcesIDsMap);
      }

      if (this.inRange) {
         dataMap.put("inRange", this.inRange);
      }

      if (this.tower != null) {
         dataMap.put("tower", this.tower.getRegionalSaveData());
      }

      if (this.ammo != 0) {
         dataMap.put("ammo", this.ammo);
      }

      if (this.gate != null) {
         dataMap.put("gate", this.gate.getRegionalSaveData());
      }

      dataMap.put("vesselRegenerationRateTick", this.vesselRegenerationRateTick);
      if (this.localFarmland.size() > 0) {
         dataMap.put("localFarmlandIDs", dataMap.packObjectArrayIDs(this.localFarmland));
      }

      YMLDataMap resourceValuesMap = new YMLDataMap();

      for (ResourceModule.ResourceType typex : this.resourceValues.keySet()) {
         if (this.resourceValues.get(typex) > 0) {
            resourceValuesMap.put(typex.toString(), this.resourceValues.get(typex));
         }
      }

      if (resourceValuesMap.size() > 0) {
         dataMap.put("resourceValues", resourceValuesMap);
      }

      YMLDataMap resourceValuesColorsMap = new YMLDataMap();

      for (ResourceModule.ResourceType typexx : this.resourceValuesColors.keySet()) {
         if (this.resourceValuesColors.get(typexx).size() > 0) {
            resourceValuesColorsMap.put(typexx.toString(), resourceValuesColorsMap.packResourceColorSetArray(this.resourceValuesColors.get(typexx)));
         }
      }

      if (resourceValuesColorsMap.size() > 0) {
         dataMap.put("resourceValuesColors", resourceValuesColorsMap);
      }

      if (this.occupants.size() > 0) {
         dataMap.put("occupantsIDs", dataMap.packObjectArrayIDs(this.occupants));
      }

      if (this.lightningRodDissipationRateTick != 0) {
         dataMap.put("lightningRodDissipationRateTick", this.lightningRodDissipationRateTick);
      }

      if (this.instabilityAmount != 0) {
         dataMap.put("instabilityAmount", this.instabilityAmount);
      }

      if (this.instabilityEarthquakeTriggered) {
         dataMap.put("instabilityEarthquakeTriggered", this.instabilityEarthquakeTriggered);
      }

      if (this.lootBoxActive) {
         dataMap.put("lootBoxActive", this.lootBoxActive);
      }

      if (this.lootBoxCategory != null) {
         dataMap.put("lootBoxCategory", this.lootBoxCategory.toString());
      }

      if (this.lootBoxCountTick != 0) {
         dataMap.put("lootBoxCountTick", this.lootBoxCountTick);
      }

      if (this.lootBoxClicks != 0) {
         dataMap.put("lootBoxClicks", this.lootBoxClicks);
      }

      if (this.lootBoxCooldown != 0) {
         dataMap.put("lootBoxCooldown", this.lootBoxCooldown);
      }

      if (this.combobulatorSpawns.size() > 0) {
         dataMap.put("combobulatorSpawnsIDs", dataMap.packObjectArrayIDs(this.combobulatorSpawns));
      }

      if (this.combobulatorEnergyRequiredTick != 0) {
         dataMap.put("combobulatorEnergyRequiredTick", this.combobulatorEnergyRequiredTick);
      }

      if (this.combobulatorEnergyRateTick != 0) {
         dataMap.put("combobulatorEnergyRateTick", this.combobulatorEnergyRateTick);
      }

      if (this.combobulatorEnergyLossPerGolemRateTick != 0) {
         dataMap.put("combobulatorEnergyLossPerGolemRateTick", this.combobulatorEnergyLossPerGolemRateTick);
      }

      if (this.essenceMouseDelayTick != 0) {
         dataMap.put("essenceMouseDelayTick", this.essenceMouseDelayTick);
      }

      if (this.energy != 0) {
         dataMap.put("energy", this.energy);
      }

      if (this.energyInRoute != 0) {
         dataMap.put("energyInRoute", this.energyInRoute);
      }

      if (this.energyRegenerationRateTick != 0) {
         dataMap.put("energyRegenerationRateTick", this.energyRegenerationRateTick);
      }

      if (this.energyConsumeRateTick != 0) {
         dataMap.put("energyConsumeRateTick", this.energyConsumeRateTick);
      }

      if (this.rainCatchRateTick != 0) {
         dataMap.put("rainCatchRateTick", this.rainCatchRateTick);
      }

      if (this.waterGenerationRateTick != 0) {
         dataMap.put("waterGenerationRateTick", this.waterGenerationRateTick);
      }

      if (this.purifyWaterRateTick != 0) {
         dataMap.put("purifyWaterRateTick", this.purifyWaterRateTick);
      }

      if (this.burnerRateTick != 0) {
         dataMap.put("burnerRateTick", this.burnerRateTick);
      }

      if (this.burnerEnergyDissipationRateTick != 0) {
         dataMap.put("burnerEnergyDissipationRateTick", this.burnerEnergyDissipationRateTick);
      }

      if (this.destroyIfIgnoredDelayTick != 0) {
         dataMap.put("destroyIfIgnoredDelayTick", this.destroyIfIgnoredDelayTick);
      }

      if (this.damageMonsterSpawnsDelayTick != 0) {
         dataMap.put("damageMonsterSpawnsDelayTick", this.damageMonsterSpawnsDelayTick);
      }

      return dataMap;
   }

   public Map<String, Object> getEditorProperties() {
      YMLDataMap dataMap = new YMLDataMap();
      MapTilesLoader.TileSet currentType = this.objectFlags.getCurrentType();
      dataMap.put("currentType", currentType != null ? currentType.toString() : "null");
      ObjectBase.ObjectSubType subType = this.objectFlags.getSubType();
      dataMap.put("subType", subType != null ? subType.toString() : "null");
      dataMap.put("objectID", this.objectID);
      dataMap.put("objectX", this.objectX);
      dataMap.put("objectY", this.objectY);
      return dataMap;
   }

   protected void initObject() throws SlickException {
      if (!this.destroyed) {
         ObjectBase.ObjectSubType subType = this.objectFlags.getSubType();
         if (subType == ObjectBase.ObjectSubType.CONSTRUCTION) {
            this.initConstruction();
         } else if (subType == ObjectBase.ObjectSubType.ABANDONED) {
            this.initAbandoned();
         } else if (subType == ObjectBase.ObjectSubType.BUILT) {
            this.initBuilt();
         } else if (subType == ObjectBase.ObjectSubType.DISMANTLE) {
            this.initDismantle();
         }

         this.calculateHitPointsMax(true);
      }
   }

   protected void initAbandoned() throws SlickException {
      this.objectFlags = this.object.getObjectFlagFactory().getObjectFlags(this.objectFlags.getBaseType(), ObjectBase.ObjectSubType.ABANDONED);
      this.map.updateObject(this.objectX, this.objectY, this.objectFlags.getCurrentType(), MapTilesLoader.TileSetObjectPhase.ABANDONED);
      this.updateObjectMapData();
      this.paused = true;
      if (this.resource.isInitialized()) {
         EnumMap<ResourceModule.ResourceType, Integer> resourceBaseValues = this.objectFlags.getResourceBaseValues();

         ResourceModule.ResourceType[] var5;
         for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
            if (resourceBaseValues.get(type) > 0) {
               for (int i = 0; i < resourceBaseValues.get(type) / 2; i++) {
                  this.increaseValue(this.resource.createResourceUnassigned(type));
               }
            }
         }
      }

      this.object.addSortGroupObjectsUpdateList(this);
   }

   protected void initConstruction() throws SlickException {
      this.objectFlags = this.object.getObjectFlagFactory().getObjectFlags(this.objectFlags.getCurrentType(), ObjectBase.ObjectSubType.CONSTRUCTION);
      this.currentPhase = MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_OUTLINE;
      this.totalValuesOffset = this.getTotalValue();
      this.map.updateObject(this.objectX, this.objectY, this.objectFlags.getCurrentType(), this.currentPhase);
      this.updateObjectMapData();
      this.updateConstruction();
      this.object.addSortGroupObjectsUpdateList(this);
   }

   protected void initBuilt() throws SlickException {
      this.objectFlags = this.object.getObjectFlagFactory().getObjectFlags(this.objectFlags.getCurrentType(), ObjectBase.ObjectSubType.BUILT);
      this.currentPhase = MapTilesLoader.TileSetObjectPhase.BASE;
      this.map.updateObject(this.objectX, this.objectY, this.objectFlags.getCurrentType(), this.currentPhase);
      this.updateObjectMapData();
      if (this.resource.isInitialized()) {
         ResourceModule.ResourceType[] var4;
         for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
            EnumMap<ResourceModule.ResourceType, Integer> resourceBaseValues = this.objectFlags.getResourceBaseValues();
            if (resourceBaseValues.get(type) > 0 && this.resourceValues.get(type) <= 0) {
               for (int i = 0; i < resourceBaseValues.get(type); i++) {
                  this.increaseValue(this.resource.createResourceUnassigned(type));
               }
            }
         }
      }

      this.map
         .eraseArea(
            this.objectX, this.objectY, this.objectX + this.objectFlags.getWidth(), this.objectY + this.objectFlags.getHeight(), MapModule.EraseMode.TOPOGRAPHY
         );
      this.createTower();
      this.createGate();
      if (this.objectFlags.getMobGroup() == MobBase.MobGroup.VILLAGER) {
         MapTilesLoader.TileSet baseType = this.objectFlags.getBaseType();
         if (baseType.getTileSetTemplate().getType() != MapTilesLoader.TileSetType.WALL) {
            Text.setVariableText("buildingName", this.getFormattedName());
            Console.out(Text.getText("console.panel.object.completed"), false);
            MapTilesLoader.TileSet currentType = this.objectFlags.getCurrentType();
            this.object.addSortGroupObjectsUpdateList(this);
            if (baseType != currentType) {
               this.god.increaseGodXP(5.0, GodModule.GodXPType.BUILDINGS_UPGRADED);
               this.goal.incrementGoal(GoalModule.GoalType.THE_COST_OF_PROGRESS);
               this.stats.increaseBuildingsUpgradedThisPeriod();
               if (this.getTower() != null && this.objectFlags.getNextUpgradeType() == null) {
                  this.goal.incrementGoal(GoalModule.GoalType.RETROFIT);
               }
            } else if (baseType != MapTilesLoader.TileSet.GOD_TOWER && baseType != MapTilesLoader.TileSet.GOD_WALL) {
               this.god.increaseGodXP(5.0, GodModule.GodXPType.BUILDINGS_CONSTRUCTED);
               this.goal.incrementGoal(GoalModule.GoalType.BUILD_ALL_THE_THINGS);
               if (this.getTower() != null) {
                  this.goal.incrementGoal(GoalModule.GoalType.TOWER_DEFENSE);
               }
            }

            if (currentType == MapTilesLoader.TileSet.AMMO_STORAGE
               || currentType == MapTilesLoader.TileSet.CRYSTAL_STORAGE
               || currentType == MapTilesLoader.TileSet.EQUIPMENT_STORAGE
               || currentType == MapTilesLoader.TileSet.FOOD_STORAGE
               || currentType == MapTilesLoader.TileSet.GOLD_STORAGE
               || currentType == MapTilesLoader.TileSet.MINERAL_STORAGE
               || currentType == MapTilesLoader.TileSet.MISCELLANEOUS_STORAGE
               || currentType == MapTilesLoader.TileSet.ROCK_STORAGE
               || currentType == MapTilesLoader.TileSet.WOOD_STORAGE) {
               this.goal.incrementGoal(GoalModule.GoalType.PRODIGIOUS_PLANNER);
            }

            if (currentType == MapTilesLoader.TileSet.CASTLE_1) {
               this.goal.incrementGoal(GoalModule.GoalType.YOU_ALREADY_LOST);
            }

            if (currentType == MapTilesLoader.TileSet.CASTLE_15) {
               this.goal.incrementGoal(GoalModule.GoalType.GOD_KING);
            }

            if (currentType == MapTilesLoader.TileSet.WELL) {
               this.goal.incrementGoal(GoalModule.GoalType.WELL_WELL_WELL);
            }

            if (currentType == MapTilesLoader.TileSet.HOUSING_UPGRADE_1_QUALITY) {
               this.goal.incrementGoal(GoalModule.GoalType.GODS_VILLAGE);
            }

            if (currentType == MapTilesLoader.TileSet.HOUSING_UPGRADE_1_OCCUPANCY) {
               this.goal.incrementGoal(GoalModule.GoalType.SARDINES);
            }

            if (currentType == MapTilesLoader.TileSet.HOUSING_UPGRADE_4_QUALITY) {
               this.goal.incrementGoal(GoalModule.GoalType.FIRST_WORLD_PROBLEMS);
            }

            if (currentType == MapTilesLoader.TileSet.HOUSING_UPGRADE_4_OCCUPANCY) {
               this.goal.incrementGoal(GoalModule.GoalType.ACROPOLIS);
            }

            if (currentType == MapTilesLoader.TileSet.DOGGO_HOUSE) {
               this.goal.incrementGoal(GoalModule.GoalType.THE_DOGGO_HOUSE);
            }

            if (currentType == MapTilesLoader.TileSet.DOGGO_HOUSE_UPGRADE_2) {
               this.goal.incrementGoal(GoalModule.GoalType.THE_BIG_DOGGO_HOUSE);
            }

            if (currentType == MapTilesLoader.TileSet.BALLISTA_TOWER_UPGRADE_3_ICE) {
               this.goal.incrementGoal(GoalModule.GoalType.LET_IT_SNOW_ARROWS);
            }
         } else {
            MapTilesLoader.TileSet currentTypex = this.objectFlags.getCurrentType();
            if (currentTypex == MapTilesLoader.TileSet.CURTAIN_WALL) {
               this.goal.incrementGoal(GoalModule.GoalType.I_LIKE_BIG_BUTTRESSES);
            }

            this.god.increaseGodXP(0.5, GodModule.GodXPType.WALLS_CONSTRUCTED);
         }
      }
   }

   protected void initDismantle() throws SlickException {
      this.objectFlags = this.object.getObjectFlagFactory().getObjectFlags(this.objectFlags.getCurrentType(), ObjectBase.ObjectSubType.DISMANTLE);
      this.removeAllMobs();
      this.tower = null;
      this.gate = null;
      this.resource.deleteFarmland(this.localFarmland);
      this.localFarmland.clear();
      this.courierGolemParking.clear();
      this.prayPoints.clear();
      this.vesselPoints.clear();
      this.beDismantled = true;
      this.object.addSortGroupObjectsUpdateList(this);
   }

   public void startDismantle() throws SlickException {
      if (this.getTotalValue() == 0 && this.getTotalResourcesCount() == 0) {
         this.object.flagObjectForDestructionAndRemoval(this);
      } else {
         this.removeAllMobs();
         this.initReset();
         this.initDismantle();
      }
   }

   public void toggleUpgrade(MapTilesLoader.TileSet next) throws SlickException {
      this.object.setPriorityBottom(this);
      this.objectFlags = this.object.getObjectFlagFactory().getObjectFlags(next, ObjectBase.ObjectSubType.CONSTRUCTION);
      this.initReset();
      this.initConstruction();
   }

   protected void updateObject() throws SlickException {
      if (!this.destroyed) {
         ObjectBase.ObjectSubType subType = this.objectFlags.getSubType();
         if (subType == ObjectBase.ObjectSubType.CONSTRUCTION) {
            this.updateConstruction();
         } else if (subType == ObjectBase.ObjectSubType.ABANDONED) {
            this.updateAbandoned();
         } else if (subType == ObjectBase.ObjectSubType.BUILT) {
            this.updateBuilt();
         } else if (subType == ObjectBase.ObjectSubType.DISMANTLE) {
            this.updateDismantle();
         }

         this.calculateHitPointsMax(true);
      }
   }

   protected void updateAbandoned() throws SlickException {
      MapTilesLoader.TileSet currentType = this.objectFlags.getCurrentType();
      if (this.getTotalValue() == this.objectFlags.getTotalValueBase() * 0.6 && this.currentPhase != MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_4) {
         this.currentPhase = MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_4;
         this.map.updateObject(this.objectX, this.objectY, currentType, this.currentPhase);
         this.updateObjectMapData();
      } else if (this.getTotalValue() == this.objectFlags.getTotalValueBase() * 0.8
         && this.currentPhase != MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_5) {
         this.currentPhase = MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_5;
         this.map.updateObject(this.objectX, this.objectY, currentType, this.currentPhase);
         this.updateObjectMapData();
      } else if (this.getTotalValue() == this.objectFlags.getTotalValueBase()) {
         this.objectFlags = this.object.getObjectFlagFactory().getObjectFlags(currentType, ObjectBase.ObjectSubType.BUILT);
         this.initReset();
         this.initBuilt();
      }
   }

   protected void updateConstruction() throws SlickException {
      MapTilesLoader.TileSet currentType = this.objectFlags.getCurrentType();
      int totalValueModified = this.getTotalValue() - this.totalValuesOffset;
      int totalValueBaseModified = this.objectFlags.getTotalValueBase() - this.totalValuesOffset;
      if (this.objectFlags.getBaseType().getTileSetTemplate().getType() == MapTilesLoader.TileSetType.WALL) {
         if (totalValueModified == totalValueBaseModified) {
            this.objectFlags = this.object.getObjectFlagFactory().getObjectFlags(currentType, ObjectBase.ObjectSubType.BUILT);
            this.initReset();
            this.initBuilt();
         } else if (this.getTotalResourcesCount() > 0 && this.currentPhase != MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_1) {
            this.currentPhase = MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_1;
            this.map.updateObject(this.objectX, this.objectY, currentType, this.currentPhase);
         }
      } else if (totalValueModified != 0
         && totalValueModified < (int)(totalValueBaseModified * 0.2)
         && this.currentPhase != MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_1) {
         this.currentPhase = MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_1;
         this.map.updateObject(this.objectX, this.objectY, currentType, this.currentPhase);
         this.updateObjectMapData();
      } else if (totalValueModified == (int)(totalValueBaseModified * 0.2) && this.currentPhase != MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_2) {
         this.currentPhase = MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_2;
         this.map.updateObject(this.objectX, this.objectY, currentType, this.currentPhase);
         this.updateObjectMapData();
      } else if (totalValueModified == (int)(totalValueBaseModified * 0.4) && this.currentPhase != MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_3) {
         this.currentPhase = MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_3;
         this.map.updateObject(this.objectX, this.objectY, currentType, this.currentPhase);
         this.updateObjectMapData();
      } else if (totalValueModified == (int)(totalValueBaseModified * 0.6) && this.currentPhase != MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_4) {
         this.currentPhase = MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_4;
         this.map.updateObject(this.objectX, this.objectY, currentType, this.currentPhase);
         this.updateObjectMapData();
      } else if (totalValueModified == (int)(totalValueBaseModified * 0.8) && this.currentPhase != MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_5) {
         this.currentPhase = MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_5;
         this.map.updateObject(this.objectX, this.objectY, currentType, this.currentPhase);
         this.updateObjectMapData();
      } else if (totalValueModified >= totalValueBaseModified) {
         this.initReset();
         this.initBuilt();
      }
   }

   protected void updateBuilt() throws SlickException {
      if (this.objectFlags.hasFillArt()) {
         int baseMath = 0;
         int currentEnergy = 0;
         int currentWater = 0;
         int resourceMax = 0;

         ResourceModule.ResourceType[] var8;
         for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
            resourceMax += this.objectFlags.getResourceStorageMax().get(type);
         }

         if (resourceMax > 0) {
            baseMath += 100;
         }

         if (this.objectFlags.getEnergyMax() > 0) {
            currentEnergy = (int)Math.ceil((float)this.energy / this.objectFlags.getEnergyMax() * 100.0F);
            baseMath += 100;
         }

         int currentResources = (int)Math.ceil((float)this.getTotalResourcesCount() / resourceMax * 100.0F);
         int finalTotal = currentEnergy + currentWater + currentResources;
         MapTilesLoader.TileSet currentType = this.objectFlags.getCurrentType();
         if (finalTotal >= 0 && finalTotal <= 1 && this.currentPhase != MapTilesLoader.TileSetObjectPhase.BASE) {
            this.currentPhase = MapTilesLoader.TileSetObjectPhase.BASE;
            this.map.updateObject(this.objectX, this.objectY, currentType, this.currentPhase);
            this.updateObjectMapData();
         } else if (finalTotal > 1 && finalTotal <= (int)(baseMath * 0.3) && this.currentPhase != MapTilesLoader.TileSetObjectPhase.FILL_PHASE_1) {
            this.currentPhase = MapTilesLoader.TileSetObjectPhase.FILL_PHASE_1;
            this.map.updateObject(this.objectX, this.objectY, currentType, this.currentPhase);
            this.updateObjectMapData();
         } else if (finalTotal > (int)(baseMath * 0.3)
            && finalTotal <= (int)(baseMath * 0.6)
            && this.currentPhase != MapTilesLoader.TileSetObjectPhase.FILL_PHASE_2) {
            this.currentPhase = MapTilesLoader.TileSetObjectPhase.FILL_PHASE_2;
            this.map.updateObject(this.objectX, this.objectY, currentType, this.currentPhase);
            this.updateObjectMapData();
         } else if (finalTotal > (int)(baseMath * 0.6)
            && finalTotal <= (int)(baseMath * 0.9)
            && this.currentPhase != MapTilesLoader.TileSetObjectPhase.FILL_PHASE_3) {
            this.currentPhase = MapTilesLoader.TileSetObjectPhase.FILL_PHASE_3;
            this.map.updateObject(this.objectX, this.objectY, currentType, this.currentPhase);
            this.updateObjectMapData();
         } else if (finalTotal > (int)(baseMath * 0.9) && this.currentPhase != MapTilesLoader.TileSetObjectPhase.FILL_PHASE_4) {
            this.currentPhase = MapTilesLoader.TileSetObjectPhase.FILL_PHASE_4;
            this.map.updateObject(this.objectX, this.objectY, currentType, this.currentPhase);
            this.updateObjectMapData();
         }
      }
   }

   protected void updateDismantle() throws SlickException {
      MapTilesLoader.TileSet currentType = this.objectFlags.getCurrentType();
      if (this.objectFlags.getBaseType().getTileSetTemplate().getType() == MapTilesLoader.TileSetType.WALL) {
         if (this.getTotalValue() == 0 && this.getTotalResourcesCount() == 0) {
            this.object.flagObjectForDestructionAndRemoval(this);
            this.discharge();
            Text.setVariableText("buildingName", this.getFormattedName());
            Console.out(Text.getText("console.panel.object.dismantled"), false);
            this.god.increaseGodXP(0.1, GodModule.GodXPType.WALLS_DISMANTLED);
            this.stats.increaseBuildingsDismantledThisPeriod();
         } else if (this.currentPhase != MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_1) {
            this.currentPhase = MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_1;
            this.map.updateObject(this.objectX, this.objectY, currentType, this.currentPhase);
            this.updateObjectMapData();
         }
      } else if (this.getTotalValue() == 0 && this.getTotalResourcesCount() == 0) {
         this.object.flagObjectForDestructionAndRemoval(this);
         this.discharge();
         Text.setVariableText("buildingName", this.getFormattedName());
         Console.out(Text.getText("console.panel.object.dismantled"), false);
         this.god.increaseGodXP(1.0, GodModule.GodXPType.BUILDINGS_DISMANTLED);
         this.stats.increaseBuildingsDismantledThisPeriod();
      } else if (this.getTotalValue() == 0 && this.currentPhase != MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_1) {
         this.currentPhase = MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_1;
         this.map.updateObject(this.objectX, this.objectY, currentType, this.currentPhase);
         this.updateObjectMapData();
      } else if (this.getTotalValue() == (int)(this.objectFlags.getTotalValueBase() * 0.2)
         && this.currentPhase != MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_2) {
         this.currentPhase = MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_2;
         this.map.updateObject(this.objectX, this.objectY, currentType, this.currentPhase);
         this.updateObjectMapData();
      } else if (this.getTotalValue() == (int)(this.objectFlags.getTotalValueBase() * 0.4)
         && this.currentPhase != MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_3) {
         this.currentPhase = MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_3;
         this.map.updateObject(this.objectX, this.objectY, currentType, this.currentPhase);
         this.updateObjectMapData();
      } else if (this.getTotalValue() == (int)(this.objectFlags.getTotalValueBase() * 0.6)
         && this.currentPhase != MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_4) {
         this.currentPhase = MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_4;
         this.map.updateObject(this.objectX, this.objectY, currentType, this.currentPhase);
         this.updateObjectMapData();
      } else if (this.getTotalValue() == (int)(this.objectFlags.getTotalValueBase() * 0.8)
         && this.currentPhase != MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_5) {
         this.currentPhase = MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_5;
         this.map.updateObject(this.objectX, this.objectY, currentType, this.currentPhase);
         this.updateObjectMapData();
      }
   }

   protected void initReset() throws SlickException {
      this.instabilityAmount = 0;
      this.lightningRodDissipationRateTick = 0;
      this.essenceMouseDelayTick = 0;
      this.energyRegenerationRateTick = 0;
      this.energyConsumeRateTick = 0;
      this.burnerRateTick = 0;
      this.burnerEnergyDissipationRateTick = 0;
      this.rainCatchRateTick = 0;
      this.waterGenerationRateTick = 0;
      this.combobulatorEnergyRateTick = 0;
      this.combobulatorEnergyLossPerGolemRateTick = 0;
      this.damageMonsterSpawnsDelayTick = 0;
      this.destroyIfIgnoredDelayTick = 0;
      this.beDismantled = false;
   }

   protected void createTower(YMLDataMap dataMap) throws SlickException {
      String towerTypeString = dataMap.getOrDefault("towerType", "null");
      TowerBase.TowerType t = null;

      TowerBase.TowerType[] var7;
      for (TowerBase.TowerType type : TowerBase.TowerType.values()) {
         if (type.toString().equals(towerTypeString)) {
            t = type;
            break;
         }
      }

      if (t == TowerBase.TowerType.BALLISTA) {
         this.tower = new TowerBallista(dataMap, this);
      } else if (t == TowerBase.TowerType.BOW) {
         this.tower = new TowerBow(dataMap, this);
      } else if (t == TowerBase.TowerType.BULLET) {
         this.tower = new TowerBullet(dataMap, this);
      } else if (t == TowerBase.TowerType.ELEMENTAL_BOLT) {
         this.tower = new TowerElementalBolt(dataMap, this);
      } else if (t == TowerBase.TowerType.PHANTOM_DART) {
         this.tower = new TowerPhantomDart(dataMap, this);
      } else if (t == TowerBase.TowerType.MOTIVATOR) {
         this.tower = new TowerMotivator(dataMap, this);
      } else if (t == TowerBase.TowerType.SLING) {
         this.tower = new TowerSling(dataMap, this);
      } else if (t == TowerBase.TowerType.SPRAY) {
         this.tower = new TowerSpray(dataMap, this);
      } else if (t == TowerBase.TowerType.ATTRACT) {
         this.tower = new TowerAttract(dataMap, this);
      } else if (t == TowerBase.TowerType.STATIC) {
         this.tower = new TowerStatic(dataMap, this);
      } else if (t == TowerBase.TowerType.BANISH) {
         this.tower = new TowerBanish(dataMap, this);
      } else if (t == TowerBase.TowerType.RECOMBOBULATOR) {
         this.tower = new TowerRecombobulator(dataMap, this);
      } else if (t == TowerBase.TowerType.GOD_BOW) {
         this.tower = new TowerGodBow(dataMap, this);
      } else if (t == TowerBase.TowerType.CORRUPTED_FIRE_BOW) {
         this.tower = new TowerCorruptedFireBow(dataMap, this);
      } else if (t == TowerBase.TowerType.CORRUPTED_ICE_BOW) {
         this.tower = new TowerCorruptedIceBow(dataMap, this);
      } else if (t == TowerBase.TowerType.CORRUPTED_LIGHTNING_BOW) {
         this.tower = new TowerCorruptedLightningBow(dataMap, this);
      } else if (t == TowerBase.TowerType.CORRUPTED_POISON_BOW) {
         this.tower = new TowerCorruptedPoisonBow(dataMap, this);
      } else if (t == TowerBase.TowerType.CORRUPTED_FIRE_PHANTOM_DART) {
         this.tower = new TowerCorruptedFirePhantomDart(dataMap, this);
      } else if (t == TowerBase.TowerType.CORRUPTED_ICE_PHANTOM_DART) {
         this.tower = new TowerCorruptedIcePhantomDart(dataMap, this);
      } else if (t == TowerBase.TowerType.CORRUPTED_LIGHTNING_PHANTOM_DART) {
         this.tower = new TowerCorruptedLightningPhantomDart(dataMap, this);
      } else if (t == TowerBase.TowerType.CORRUPTED_POISON_PHANTOM_DART) {
         this.tower = new TowerCorruptedPoisonPhantomDart(dataMap, this);
      } else {
         System.out.println("ERROR LOADING TOWER TYPE");
      }
   }

   protected void createTower() throws SlickException {
      TowerBase.TowerType towerType = this.objectFlags.getTowerType();
      if (towerType != null) {
         TowerBase.TowerUpgradeStage upgradeStage = this.objectFlags.getTowerUpgradeStage();
         int towerX = this.objectX + this.objectFlags.getTowerXOnBuilding();
         int towerY = this.objectY + this.objectFlags.getTowerYOnBuilding();
         if (towerType == TowerBase.TowerType.BOW) {
            this.tower = new TowerBow(towerX, towerY, this, upgradeStage);
         } else if (towerType == TowerBase.TowerType.BALLISTA) {
            this.tower = new TowerBallista(towerX, towerY, this, upgradeStage);
         } else if (towerType == TowerBase.TowerType.BULLET) {
            this.tower = new TowerBullet(towerX, towerY, this, upgradeStage);
         } else if (towerType == TowerBase.TowerType.ELEMENTAL_BOLT) {
            this.tower = new TowerElementalBolt(towerX, towerY, this, upgradeStage);
         } else if (towerType == TowerBase.TowerType.PHANTOM_DART) {
            this.tower = new TowerPhantomDart(towerX, towerY, this, upgradeStage);
         } else if (towerType == TowerBase.TowerType.MOTIVATOR) {
            this.tower = new TowerMotivator(towerX, towerY, this);
         } else if (towerType == TowerBase.TowerType.SLING) {
            this.tower = new TowerSling(towerX, towerY, this, upgradeStage);
         } else if (towerType == TowerBase.TowerType.SPRAY) {
            this.tower = new TowerSpray(towerX, towerY, this, upgradeStage);
         } else if (towerType == TowerBase.TowerType.ATTRACT) {
            this.tower = new TowerAttract(towerX, towerY, this, upgradeStage);
         } else if (towerType == TowerBase.TowerType.STATIC) {
            this.tower = new TowerStatic(towerX, towerY, this, upgradeStage);
         } else if (towerType == TowerBase.TowerType.BANISH) {
            this.tower = new TowerBanish(towerX, towerY, this, upgradeStage);
         } else if (towerType == TowerBase.TowerType.RECOMBOBULATOR) {
            this.tower = new TowerRecombobulator(towerX, towerY, this, upgradeStage);
         } else if (towerType == TowerBase.TowerType.GOD_BOW) {
            this.tower = new TowerGodBow(towerX, towerY, this);
         } else if (towerType == TowerBase.TowerType.CORRUPTED_FIRE_BOW) {
            this.tower = new TowerCorruptedFireBow(towerX, towerY, this);
         } else if (towerType == TowerBase.TowerType.CORRUPTED_ICE_BOW) {
            this.tower = new TowerCorruptedIceBow(towerX, towerY, this);
         } else if (towerType == TowerBase.TowerType.CORRUPTED_LIGHTNING_BOW) {
            this.tower = new TowerCorruptedLightningBow(towerX, towerY, this);
         } else if (towerType == TowerBase.TowerType.CORRUPTED_POISON_BOW) {
            this.tower = new TowerCorruptedPoisonBow(towerX, towerY, this);
         } else if (towerType == TowerBase.TowerType.CORRUPTED_FIRE_PHANTOM_DART) {
            this.tower = new TowerCorruptedFirePhantomDart(towerX, towerY, this);
         } else if (towerType == TowerBase.TowerType.CORRUPTED_ICE_PHANTOM_DART) {
            this.tower = new TowerCorruptedIcePhantomDart(towerX, towerY, this);
         } else if (towerType == TowerBase.TowerType.CORRUPTED_LIGHTNING_PHANTOM_DART) {
            this.tower = new TowerCorruptedLightningPhantomDart(towerX, towerY, this);
         } else if (towerType == TowerBase.TowerType.CORRUPTED_POISON_PHANTOM_DART) {
            this.tower = new TowerCorruptedPoisonPhantomDart(towerX, towerY, this);
         }
      }
   }

   protected void createGate(YMLDataMap dataMap) throws SlickException {
      String gateTypeString = dataMap.getOrDefault("gateType", "null");
      GateBase.GateType t = null;

      GateBase.GateType[] var7;
      for (GateBase.GateType type : GateBase.GateType.values()) {
         if (type.toString().equals(gateTypeString)) {
            t = type;
            break;
         }
      }

      if (t == GateBase.GateType.WOOD_GATE_NS) {
         this.gate = new WoodGateNS(dataMap, this);
      } else if (t == GateBase.GateType.WOOD_GATE_WE) {
         this.gate = new WoodGateWE(dataMap, this);
      } else if (t == GateBase.GateType.STONE_GATE_NS) {
         this.gate = new StoneGateNS(dataMap, this);
      } else if (t == GateBase.GateType.STONE_GATE_WE) {
         this.gate = new StoneGateWE(dataMap, this);
      }
   }

   protected void createGate() throws SlickException {
      GateBase.GateType gateType = this.objectFlags.getGateType();
      if (gateType != null) {
         int gateX = this.objectX + this.objectFlags.getGateXOnBuilding();
         int gateY = this.objectY + this.objectFlags.getGateYOnBuilding();
         if (gateType == GateBase.GateType.WOOD_GATE_NS) {
            this.gate = new WoodGateNS(gateX, gateY, this);
         } else if (gateType == GateBase.GateType.WOOD_GATE_WE) {
            this.gate = new WoodGateWE(gateX, gateY, this);
         } else if (gateType == GateBase.GateType.STONE_GATE_NS) {
            this.gate = new StoneGateNS(gateX, gateY, this);
         } else if (gateType == GateBase.GateType.STONE_GATE_WE) {
            this.gate = new StoneGateWE(gateX, gateY, this);
         }
      }
   }

   protected void updateObjectMapData() {
      int width = this.objectFlags.getWidth();
      int height = this.objectFlags.getHeight();

      for (int x = 0; x < width; x++) {
         for (int y = 0; y < height; y++) {
            for (int l = 0; l < 12; l++) {
               int tileID = this.map.getTileId(this.objectX + x, this.objectY + y, l);
               if (this.map.getMapTileLoader().isTileCrop(tileID)) {
                  FarmlandBase newFarmland = this.resource.createFarmland(this.objectX + x, this.objectY + y);
                  if (newFarmland != null) {
                     this.localFarmland.add(newFarmland);
                  }
               }

               if (this.map.getMapTileLoader().isTileCourierGolemParking(tileID)) {
                  OrderedPair golemParking = OrderedPair.getOrderedPair(this.objectX + x, this.objectY + y);
                  if (!this.courierGolemParking.contains(golemParking)) {
                     this.courierGolemParking.add(golemParking);
                  }
               }

               if (this.map.getMapTileLoader().isTilePrayPoint(tileID)) {
                  OrderedPair prayPoint = OrderedPair.getOrderedPair(this.objectX + x, this.objectY + y);
                  if (!this.prayPoints.contains(prayPoint)) {
                     this.prayPoints.add(prayPoint);
                  }
               }

               if (this.map.getMapTileLoader().isTileVesselPoint(tileID)) {
                  OrderedPair vesselPoint = OrderedPair.getOrderedPair(this.objectX + x, this.objectY + y);
                  if (!this.vesselPoints.contains(vesselPoint)) {
                     this.vesselPoints.add(vesselPoint);
                  }
               }

               if (this.map.getMapTileLoader().isTileInteractPoint(tileID)) {
                  OrderedPair interactPoint = OrderedPair.getOrderedPair(this.objectX + x, this.objectY + y);
                  if (!this.interactPoints.contains(interactPoint)) {
                     this.interactPoints.add(interactPoint);
                  }
               }

               if (this.map.getMapTileLoader().isTileFunctionalPoint(tileID)) {
                  this.functionalCoordinates = OrderedPair.getOrderedPair(this.objectX + x, this.objectY + y);
               }
            }
         }
      }
   }

   protected void renderTower() throws SlickException {
      if (this.tower != null) {
         this.tower.render();
      }
   }

   protected void renderTowerMapWriter() throws SlickException {
      if (this.tower != null) {
         this.tower.renderMapWriter();
      }
   }

   protected void renderGate() throws SlickException {
      if (this.gate != null) {
         this.gate.render();
      }
   }

   protected void renderGateMapWriter() throws SlickException {
      if (this.gate != null) {
         this.gate.renderMapWriter();
      }
   }

   protected void renderOverlay(Graphics g, boolean showIcons, boolean debug) throws SlickException {
      float mapX = this.map.getMapX();
      float mapY = this.map.getMapY();
      int width = this.objectFlags.getWidth();
      if (this.map.isInView(this.objectX * 16, this.objectY * 16, 100)) {
         if (this.hitPoints < this.hitPointsMax && this.hitPoints > 0) {
            int hpLeft = (int)Math.ceil((float)this.hitPoints / this.hitPointsMax * width * 16.0F);
            g.setColor(new Color(0, 0, 0, 50));
            g.fillRect(this.objectX * 16 + mapX - 1.0F, this.objectY * 16 + mapY - 4.0F, width * 16 + 2, 3.0F);
            g.setColor(Color.black);
            g.fillRect(this.objectX * 16 + mapX, this.objectY * 16 + mapY - 3.0F, width * 16, 1.0F);
            if (this.hitPoints > this.hitPointsMax * 0.6) {
               g.setColor(Color.green);
            } else if (this.hitPoints > this.hitPointsMax * 0.4) {
               g.setColor(Color.yellow);
            } else if (this.hitPoints > this.hitPointsMax * 0.2) {
               g.setColor(Color.orange);
            } else {
               g.setColor(Color.red);
            }

            g.fillRect(this.objectX * 16 + mapX, this.objectY * 16 + mapY - 3.0F, hpLeft, 1.0F);
         }

         if (showIcons) {
            this.renderOverlayIcons(g);
         }

         g.resetTransform();
         g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
         if (this.objectFlags.getBaseType().getTileSetTemplate().getType() != MapTilesLoader.TileSetType.WALL) {
            this.renderOverlayMessages();
         }

         if (debug) {
            g.setColor(Color.orange);
            g.draw(this.intersectBox);
         }

         g.resetTransform();
         g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
      }
   }

   private void renderOverlayMessages() throws SlickException {
      float posX = this.objectX * 16 + this.objectFlags.getWidth() * 16 / 2;
      float posY = this.objectY * 16 + 16;
      float drawX = ScaleControl.getTransformedPositionX(posX);
      float drawY = ScaleControl.getTransformedPositionY(posY);
      int alignY = 0;
      int fontLineHeight = 0;
      int fontSize = 0;
      byte var22;
      byte var23;
      if (ScaleControl.getWorldScale() <= 1.0F) {
         var23 = 1;
         var22 = 20;
      } else if (ScaleControl.getWorldScale() >= 3.0F) {
         var23 = 3;
         var22 = 40;
      } else {
         var23 = 2;
         var22 = 30;
      }

      ArrayList<MapTilesLoader.TileSet> allTypesInVillage = new ArrayList<>();
      if (this.objectFlags.getSubType() == ObjectBase.ObjectSubType.BUILT && this.objectFlags.getRequiredSupportBuildings().size() > 0) {
         for (ObjectBase o : this.object.getObjectsArrayList()) {
            ObjectBase.ObjectSubType thisSubType = o.getObjectFlags().getSubType();
            MapTilesLoader.TileSet thisBaseType = o.getObjectFlags().getBaseType();
            if (o.isInRange()
               && (thisSubType != ObjectBase.ObjectSubType.ABANDONED || thisSubType != ObjectBase.ObjectSubType.DISMANTLE)
               && !allTypesInVillage.contains(thisBaseType)) {
               allTypesInVillage.add(thisBaseType);
            }
         }

         for (MapTilesLoader.TileSet[] a : this.objectFlags.getRequiredSupportBuildings()) {
            String out = "";
            boolean foundOne = false;

            for (MapTilesLoader.TileSet t : a) {
               if (allTypesInVillage.contains(t)) {
                  foundOne = true;
                  break;
               }
            }

            if (!foundOne) {
               for (MapTilesLoader.TileSet tx : a) {
                  if (!allTypesInVillage.contains(tx)) {
                     Text.setVariableText("buildingName", this.object.getObjectFlagFactory().getObjectFlags(tx, ObjectBase.ObjectSubType.BUILT).getBaseName());
                     if (out.equals("")) {
                        out = Text.getText("objectOverlay.requiredBuilding");
                     } else {
                        out = out + Text.getText("objectOverlay.requiredBuildingAdditional");
                     }
                  }
               }

               this.font.drawString(drawX, drawY + alignY, out, Text.FontType.BODY, var23, true);
               alignY += var22 * 2;
            }
         }
      }

      if (this.localFarmland.size() > 0) {
         boolean freezePoint = false;
         if (this.weather.getCurrentTemp() <= this.weather.getFreezePoint()) {
            this.font.drawString(drawX, drawY + alignY, Text.getText("objectOverlay.farms.tooCold"), Text.FontType.BODY, var23, true);
            freezePoint = true;
            alignY += var22;
         }

         if (!freezePoint) {
            int tended = 0;
            int watered = 0;
            int dying = 0;
            int planted = 0;

            for (FarmlandBase f : this.localFarmland) {
               if (f.isPlanted()) {
                  planted++;
                  if (f.isTended()) {
                     tended++;
                  }

                  if (f.isWatered()) {
                     watered++;
                  }

                  if (f.getGrowthTick() < 0) {
                     dying++;
                  }
               }
            }

            if (dying > 0) {
               this.font.drawString(drawX, drawY + alignY, Text.getText("objectOverlay.farms.cropsDying"), Text.FontType.BODY, var23, true);
               alignY += var22;
            }

            if (tended < planted / 3) {
               this.font.drawString(drawX, drawY + alignY, Text.getText("objectOverlay.farms.poorlyTended"), Text.FontType.BODY, var23, true);
               alignY += var22;
            }

            if (watered < planted / 3) {
               this.font.drawString(drawX, drawY + alignY, Text.getText("objectOverlay.farms.poorlyWatered"), Text.FontType.BODY, var23, true);
               alignY += var22;
            }
         }
      }

      if (!this.objectFlags.canDeliverEssenceToCollectors()
         && !this.objectFlags.isCombobulator()
         && !this.objectFlags.isLightningRod()
         && this.inRange
         && this.objectFlags.getEnergyMax() > 0
         && this.energy < this.objectFlags.getEnergyMax() * 0.25F) {
         this.font.drawString(drawX, drawY + alignY, Text.getText("objectOverlay.lowEnergy"), Text.FontType.BODY, var23, true);
         alignY += var22;
      }

      MobJobBase.MobJobType workerJobType = this.objectFlags.getWorkerJobType();
      if (workerJobType != MobJobBase.MobJobType.NONE) {
         if (this.mobJob.getCurrentWorkers(workerJobType, true) <= 0) {
            this.font.drawString(drawX, drawY + alignY, Text.getText("objectOverlay.noWorkers"), Text.FontType.BODY, var23, true);
            alignY += var22;
         }

         if (this.mobJob.getCurrentWorkers(workerJobType, true) < this.mobJob.getDesiredWorkers(workerJobType) * 0.75F) {
            this.font.drawString(drawX, drawY + alignY, Text.getText("objectOverlay.understaffed"), Text.FontType.BODY, var23, true);
            alignY += var22;
         }

         if (workerJobType.getJob().getCanWorkerHarvest().size() > 0 || workerJobType.getJob().getCanWorkerRefine().size() > 0) {
            boolean foundWork = false;
            boolean canWorkHere = false;

            for (ResourceModule.ResourceType txx : workerJobType.getJob().getCanWorkerHarvest()) {
               if (this.objectFlags.getCanBuildingStore().contains(txx)) {
                  canWorkHere = true;
                  if (this.resource.getResourceMakeAmount(txx) > 0 || this.resource.getResourceMaintainAmount(txx) > 0) {
                     foundWork = true;
                     break;
                  }
               }
            }

            if (!foundWork) {
               for (ResourceModule.ResourceType txxx : workerJobType.getJob().getCanWorkerRefine()) {
                  if (this.objectFlags.getCanBuildingStore().contains(txxx)) {
                     canWorkHere = true;
                     if (this.resource.getResourceMakeAmount(txxx) > 0 || this.resource.getResourceMaintainAmount(txxx) > 0) {
                        foundWork = true;
                        break;
                     }
                  }
               }
            }

            if (canWorkHere && !foundWork) {
               this.font.drawString(drawX, drawY + alignY, Text.getText("objectOverlay.noProduction"), Text.FontType.BODY, var23, true);
               alignY += var22;
            }
         }
      }

      if (this.objectFlags.isCombobulator()) {
         ArrayList<MobBase> nearByMobs = this.mob
            .getDeadMobsNearby(this.getFunctionalXOnMap(), this.getFunctionalYOnMap(), this.objectFlags.getCombobulatorResourceDistance());
         boolean hasMobs = false;
         boolean hasBuildings = false;
         boolean hasResources = false;

         for (MobBase m : nearByMobs) {
            if (this.objectFlags.getCombobulatorSpawnTypes().contains(m.getMobType())) {
               hasMobs = true;
               break;
            }
         }

         if (!hasMobs) {
            for (ResourceBase r : this.resource
               .getResourcesNearby(this.getFunctionalXOnMap(), this.getFunctionalYOnMap(), this.objectFlags.getCombobulatorResourceDistance())) {
               if (r.getType() == this.objectFlags.getCombobulatorResourceType()) {
                  hasResources = true;
                  break;
               }
            }

            if (!hasResources) {
               int combobulatorResourceDistance = this.objectFlags.getCombobulatorResourceDistance();

               label174:
               for (int x = -combobulatorResourceDistance; x < combobulatorResourceDistance; x++) {
                  for (int y = -combobulatorResourceDistance; y < combobulatorResourceDistance; y++) {
                     if (!Utilities.isOutOfBoundsOfUsableMap(x + this.getFunctionalXOnMap(), y + this.getFunctionalYOnMap())) {
                        int dx = this.getFunctionalXOnMap() - (x + this.getFunctionalXOnMap());
                        int dy = this.getFunctionalYOnMap() - (y + this.getFunctionalYOnMap());
                        int hValue = (int)Math.sqrt(dx * dx + dy * dy);
                        if (hValue < combobulatorResourceDistance
                           && this.resource.getResourceType()[x + this.getFunctionalXOnMap()][y + this.getFunctionalYOnMap()]
                              == this.objectFlags.getCombobulatorResourceType()) {
                           hasResources = true;
                           break label174;
                        }
                     }
                  }
               }
            }
         }

         if (!hasMobs && !hasResources) {
            for (ObjectBase ox : this.object
               .getObjectsNearby(this.getFunctionalXOnMap(), this.getFunctionalYOnMap(), this.objectFlags.getCombobulatorResourceDistance())) {
               if (ox.isResourceAvailable(this.objectFlags.getCombobulatorResourceType())) {
                  hasBuildings = true;
                  break;
               }
            }
         }

         if (!hasResources && !hasMobs && !hasBuildings) {
            Text.setVariableText("resourceName", this.objectFlags.getCombobulatorResourceType().getTemplate().getNamePlural());
            this.font.drawString(drawX, drawY + alignY, Text.getText("objectOverlay.noSpawningResources"), Text.FontType.BODY, var23, true);
            alignY += var22;
         }
      }
   }

   private void renderOverlayIcons(Graphics g) {
      float mapX = this.map.getMapX();
      float mapY = this.map.getMapY();
      if (this.map.isInView(this.objectX * 16, this.objectY * 16, 100)) {
         ObjectBase.ObjectSubType subType = this.objectFlags.getSubType();
         int width = this.objectFlags.getWidth();
         int height = this.objectFlags.getHeight();
         if (this.paused) {
            if (subType == ObjectBase.ObjectSubType.ABANDONED) {
               this.reclaimIcon.draw(this.objectX * 16 + mapX + 4.0F, this.objectY * 16 + mapY + 4.0F);
            } else {
               this.pauseIcon.draw(this.objectX * 16 + mapX + 4.0F, this.objectY * 16 + mapY + 4.0F);
            }
         }

         if (subType == ObjectBase.ObjectSubType.DISMANTLE) {
            if (this.paused) {
               if (width == 1 && height == 1) {
                  this.dismantleIconSmall.draw(this.objectX * 16 + mapX + 36.0F, this.objectY * 16 + mapY + 4.0F);
               } else {
                  this.dismantleIcon.draw(this.objectX * 16 + mapX + 36.0F, this.objectY * 16 + mapY + 4.0F);
               }
            } else if (width == 1 && height == 1) {
               this.dismantleIconSmall.draw(this.objectX * 16 + mapX + 4.0F, this.objectY * 16 + mapY + 4.0F);
            } else {
               this.dismantleIcon.draw(this.objectX * 16 + mapX + 4.0F, this.objectY * 16 + mapY + 4.0F);
            }
         }

         if (this.objectFlags.getMobGroup() != MobBase.MobGroup.MONSTER
            && (
               subType == ObjectBase.ObjectSubType.CONSTRUCTION
                  || subType == ObjectBase.ObjectSubType.DISMANTLE
                  || subType == ObjectBase.ObjectSubType.ABANDONED && !this.paused
            )) {
            float drawX = this.objectX * 16 + width * 16 / 2 + mapX;
            float drawY = this.objectY * 16 + height * 16 / 2 + mapY;
            float transformX = ScaleControl.getTransformedPosition(drawX);
            float transformY = ScaleControl.getTransformedPosition(drawY);
            g.resetTransform();
            g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
            MapTilesLoader.TileSetType thisTileSetType = this.objectFlags.getBaseType().getTileSetTemplate().getType();
            if (thisTileSetType != MapTilesLoader.TileSetType.WALL) {
               this.objectConstructionRequirements(transformX, transformY, g);
            }

            int percent = 0;
            String color = "$GRE1";
            if (subType == ObjectBase.ObjectSubType.DISMANTLE) {
               percent = 100 - (int)((double)this.getTotalValue() / this.objectFlags.getTotalValueBase() * 100.0);
               color = "$RED1";
            } else {
               percent = (int)(
                  ((double)this.getTotalValue() - this.totalValuesOffset) / ((double)this.objectFlags.getTotalValueBase() - this.totalValuesOffset) * 100.0
               );
            }

            if (width <= 2 || height <= 2) {
               this.font.drawString(transformX, transformY - 20.0F, color + percent + "%", Text.FontType.HEADER, 2, true);
            } else if (width > 5 && height > 5) {
               this.font.drawString(transformX, transformY - 32.0F, color + percent + "%", Text.FontType.HEADER, 5, true);
            } else {
               this.font.drawString(transformX, transformY - 24.0F, color + percent + "%", Text.FontType.HEADER, 3, true);
            }

            g.resetTransform();
            g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
         }
      }
   }

   public void objectConstructionRequirements(float drawX, float drawY, Graphics g) {
      int alignX = 0;
      int alignY = 0;
      int startX = 0;

      ResourceModule.ResourceType[] var10;
      for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
         if (this.getResourceValueCount(type) + this.getResourceCount(type) < this.objectFlags.getResourceValueBaseCount(type)) {
            startX -= 12;
         }
      }

      g.setColor(new Color(0, 0, 0, 150));

      for (ResourceModule.ResourceType typex : ResourceModule.ResourceType.values()) {
         if (this.getResourceValueCount(typex) + this.getResourceCount(typex) < this.objectFlags.getResourceValueBaseCount(typex)) {
            g.fillRect(drawX + startX + alignX + 4.0F, drawY + 4.0F, 24.0F, 20.0F);
            this.resourceValueIcons.get(typex).draw(drawX + startX + alignX + 8.0F, drawY);
            if (this.resource.getResourceTotal(typex, true) == 0) {
               this.font.drawString(drawX + startX + alignX + 11.0F, drawY + 2.0F, "$RED1X", Text.FontType.HEADER, 0, true);
               this.font
                  .drawString(
                     drawX + startX + alignX + 16.0F,
                     drawY + 14.0F,
                     "$RED1" + (this.objectFlags.getResourceValueBaseCount(typex) - (this.getResourceValueCount(typex) + this.getResourceCount(typex))),
                     Text.FontType.BODY,
                     2,
                     true
                  );
               Text.setVariableText("resourceName", typex.getTemplate().getName());
               this.font.drawString(drawX, drawY + alignY + 32.0F, Text.getText("objectOverlay.noResources"), Text.FontType.BODY, 2, true);
               alignY += 16;
            } else {
               this.font
                  .drawString(
                     drawX + startX + alignX + 16.0F,
                     drawY + 14.0F,
                     "$GRE1" + (this.objectFlags.getResourceValueBaseCount(typex) - (this.getResourceValueCount(typex) + this.getResourceCount(typex))),
                     Text.FontType.BODY,
                     2,
                     true
                  );
            }

            alignX += 24;
         }
      }
   }

   private void updateOverlayIcons() {
      int size = 16;
      int offset = 16;
      ObjectBase.ObjectSubType subType = this.objectFlags.getSubType();
      if (this.objectFlags.getWidth() == 1 && this.objectFlags.getHeight() == 1 && subType != ObjectBase.ObjectSubType.ABANDONED) {
         size = 8;
         offset = 12;
      }

      if (this.map.isInView(this.objectX * 16, this.objectY * 16, 100)) {
         if (this.paused) {
            if (subType == ObjectBase.ObjectSubType.ABANDONED) {
               if (Utilities.randomInt(offset) == 0) {
                  if (this.inRange) {
                     this.projectile.newEnergyRing(this.objectX * 16 + offset, this.objectY * 16 + offset, size, ParticleModule.ParticleSet.MAGIC_GREEN);
                     this.particle
                        .newParticleGroup(
                           this.objectX * 16 + offset,
                           this.objectY * 16 + offset,
                           ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS,
                           ParticleModule.ParticleSet.MAGIC_GREEN
                        );
                  } else {
                     this.projectile.newEnergyRing(this.objectX * 16 + offset, this.objectY * 16 + offset, size, ParticleModule.ParticleSet.MAGIC_RED);
                  }
               }
            } else if (Utilities.randomInt(offset) == 0) {
               this.projectile.newEnergyRing(this.objectX * 16 + offset, this.objectY * 16 + offset, size, ParticleModule.ParticleSet.MAGIC_BLUE);
            }
         }

         if (subType == ObjectBase.ObjectSubType.DISMANTLE) {
            if (this.paused) {
               if (Utilities.randomInt(offset) == 0) {
                  this.projectile.newEnergyRing(this.objectX * 16 + offset + 32, this.objectY * 16 + offset, size, ParticleModule.ParticleSet.MAGIC_RED);
               }
            } else if (Utilities.randomInt(offset) == 0) {
               this.projectile.newEnergyRing(this.objectX * 16 + offset, this.objectY * 16 + offset, size, ParticleModule.ParticleSet.MAGIC_RED);
            }
         }
      }
   }

   private void calculateHitPointsMax(boolean recalcHitPoints) {
      int totalResourceValue = this.getTotalValue();
      if (recalcHitPoints) {
         int hitPointsMaxOld = this.hitPointsMax;
         this.hitPointsMax = 0;

         for (int x = 0; x < totalResourceValue; x++) {
            int applied = 150 - x * 5;
            if (applied < 5) {
               applied = 5;
            } else if (applied > 100) {
               applied = 100;
            }

            this.hitPointsMax += applied;
         }

         if (this.objectFlags.getMobGroup() == MobBase.MobGroup.VILLAGER && this.perk.getPerkTypeTotal(PerkModule.PerkType.BUILDING_HEALTH) > 0.0) {
            this.hitPointsMax = (int)(this.hitPointsMax + this.hitPointsMax * this.perk.getPerkTypeTotal(PerkModule.PerkType.BUILDING_HEALTH));
         }

         if (this.hitPointsMax < hitPointsMaxOld) {
            if (this.hitPoints > this.hitPointsMax) {
               this.hitPoints = this.hitPointsMax;
            }
         } else {
            this.hitPoints = this.hitPoints + (this.hitPointsMax - hitPointsMaxOld);
         }
      } else {
         this.hitPointsMax = 0;

         for (int x = 0; x < totalResourceValue; x++) {
            int applied = 150 - x * 5;
            if (applied < 5) {
               applied = 5;
            } else if (applied > 100) {
               applied = 100;
            }

            this.hitPointsMax += applied;
         }

         if (this.objectFlags.getMobGroup() == MobBase.MobGroup.VILLAGER && this.perk.getPerkTypeTotal(PerkModule.PerkType.BUILDING_HEALTH) > 0.0) {
            this.hitPointsMax = (int)(this.hitPointsMax + this.hitPointsMax * this.perk.getPerkTypeTotal(PerkModule.PerkType.BUILDING_HEALTH));
         }
      }
   }

   public void update(boolean showIcons) throws SlickException {
      this.intersectBox
         .setBounds(
            (this.map.getMapX() + this.objectX * 16) * ScaleControl.getScalingDifference(),
            (this.map.getMapY() + this.objectY * 16) * ScaleControl.getScalingDifference(),
            this.objectFlags.getWidth() * 16 * ScaleControl.getScalingDifference(),
            this.objectFlags.getHeight() * 16 * ScaleControl.getScalingDifference()
         );
      if (!this.objectFlags.isInvulnerable()
         && this.objectFlags.getMobGroup() != MobBase.MobGroup.MONSTER
         && this.isOnCorruption()
         && Utilities.randomInt(25) == 0) {
         this.takeDamage(Utilities.randomInt(10) + 1, MobBase.DamageType.FIRE, MobBase.DamageCause.CORRUPTION);
      }

      if (!this.paused && this.tower != null) {
         this.tower.update();
      }

      if (this.gate != null) {
         this.gate.update();
      }

      if (this.objectFlags.isLightningRod()) {
         this.lightningRodEnergyDissipate();
      }

      if (this.objectFlags.canEnergyRegenerateFree()) {
         this.energyRegenerate();
      }

      if (this.objectFlags.isBurner()) {
         this.burnTrash();
      }

      if (this.objectFlags.canBuildingStore()) {
         this.mergeResources();
      }

      if (this.objectFlags.canPurifyWater()) {
         this.purifyWater();
      }

      if (this.objectFlags.canCatchRain()) {
         this.catchRain();
      }

      if (this.objectFlags.canGenerateWater()) {
         this.generateWater();
      }

      if (this.vesselPoints.size() > 0) {
         this.vesselRegenBehavior();
      }

      if (this.objectFlags.canDeliverEssence() || this.objectFlags.canDeliverEssenceToCollectors()) {
         this.deliverEssence();
      }

      if (this.objectFlags.canReleaseEssenceToGod()) {
         this.releaseEssenceToGod();
      }

      if (this.objectFlags.canInstability()) {
         this.instability();
      }

      if (this.objectFlags.isCullisGate()) {
         this.cullisGateParticles();
      }

      if (this.objectFlags.isLootBox()) {
         this.lootBoxBehavior();
      }

      if (this.objectFlags.isCombobulator() && this.mob.isInitialized()) {
         this.updateCombobulator();
      }

      if (this.objectFlags.allowDamageMonsterSpawns() && this.damageMonsterSpawnsDelayTick < this.objectFlags.getDamageMonsterSpawnsDelay()) {
         this.damageMonsterSpawnsDelayTick++;
      }

      if (this.objectFlags.canDestroyIfIgnored()) {
         this.destroyIfIgnoredUpdate();
      }

      if (showIcons) {
         this.updateOverlayIcons();
      }

      this.onFireUpdate();
   }

   private void updateCombobulator() throws SlickException {
      if (!this.paused) {
         if (this.energy > 0 && this.combobulatorEnergyRequiredTick < this.objectFlags.getCombobulatorEnergyRequired()) {
            this.combobulatorEnergyRateTick++;
            if (this.combobulatorEnergyRateTick > this.objectFlags.getCombobulatorEnergyRate()) {
               this.decreaseEnergy(1);
               this.combobulatorEnergyRequiredTick++;
               this.combobulatorEnergyRateTick = 0;
            }
         } else {
            this.combobulatorEnergyRateTick = 0;
         }

         if (this.combobulatorSpawns.size() > 0) {
            this.combobulatorEnergyLossPerGolemRateTick++;
            if (this.combobulatorEnergyLossPerGolemRateTick >= this.objectFlags.getCombobulatorEnergyLossPerGolemRate()) {
               ArrayList<MobBase> allGolemsShuffled = new ArrayList<>();
               allGolemsShuffled.addAll(this.combobulatorSpawns);

               for (MobBase m : allGolemsShuffled) {
                  if (this.energy >= this.objectFlags.getCombobulatorEnergyLossPerGolem()) {
                     this.decreaseEnergy(this.objectFlags.getCombobulatorEnergyLossPerGolem());
                     m.rechargeGolemRequiresChargeAmount();
                  }
               }

               this.combobulatorEnergyLossPerGolemRateTick = 0;
            }
         } else {
            this.combobulatorEnergyLossPerGolemRateTick = 0;
         }

         if (this.combobulatorEnergyRequiredTick > 0) {
            int currentCharge = (int)Math.ceil((float)this.combobulatorEnergyRequiredTick / this.objectFlags.getCombobulatorEnergyRequired() * 50.0F);
            int energyX = 0;
            int energyY = 0;
            if (this.objectFlags.getEssenceReturnCoordinates() != null) {
               energyX = this.getEssenceReturnXOnMapExactPixel();
               energyY = this.getEssenceReturnYOnMapExactPixel();
            } else {
               OrderedPair center = this.getCenterCoordinates();
               energyX = MapModule.getCenterPixelOfCoordinateX(center.getX());
               energyY = MapModule.getCenterPixelOfCoordinateY(center.getY());
            }

            if (this.map.isInView(energyX, energyY, 100)) {
               int chance = 60 - currentCharge;
               if (chance < 50 && Utilities.randomInt(chance) == 0) {
                  if (chance < 15) {
                     this.projectile.newExpandingEnergyTrail(energyX, energyY, ParticleModule.ParticleSet.FIRE_YELLOW);
                  } else {
                     this.projectile.newExpandingEnergyTrail(energyX, energyY, ParticleModule.ParticleSet.FIRE_BLUE);
                  }
               }
            }
         }

         if (this.combobulatorSpawns.size() < this.objectFlags.getCombobulatorMax()
            && this.combobulatorEnergyRequiredTick >= this.objectFlags.getCombobulatorEnergyRequired()) {
            ArrayList<MobBase.MobType> combobulatorSpawnTypes = this.objectFlags.getCombobulatorSpawnTypes();
            ArrayList<OrderedPair> validCoordinates = new ArrayList<>();
            ArrayList<MobBase> nearByMobs = null;
            ArrayList<ObjectBase> nearByBuildings = null;
            boolean useMobs = false;
            boolean useBuilding = false;
            nearByMobs = this.mob.getDeadMobsNearby(this.getFunctionalXOnMap(), this.getFunctionalYOnMap(), this.objectFlags.getCombobulatorResourceDistance());

            for (MobBase mx : nearByMobs) {
               if (combobulatorSpawnTypes.contains(mx.getMobType())) {
                  useMobs = true;
                  break;
               }
            }

            if (!useMobs) {
               nearByBuildings = this.object
                  .getObjectsNearby(this.getFunctionalXOnMap(), this.getFunctionalYOnMap(), this.objectFlags.getCombobulatorResourceDistance());

               for (ObjectBase o : nearByBuildings) {
                  if (o.isResourceAvailable(this.objectFlags.getCombobulatorResourceType())) {
                     useBuilding = true;
                     break;
                  }
               }
            }

            if (!useMobs && !useBuilding) {
               for (ResourceBase r : this.resource
                  .getResourcesNearby(this.getFunctionalXOnMap(), this.getFunctionalYOnMap(), this.objectFlags.getCombobulatorResourceDistance())) {
                  if (r.getType() == this.objectFlags.getCombobulatorResourceType()) {
                     validCoordinates.add(OrderedPair.getOrderedPair(r.getTileX(), r.getTileY()));
                  }
               }

               if (validCoordinates.size() == 0) {
                  for (OrderedPair ox : Utilities.getAllCoordinatesWithin(
                     this.getFunctionalXOnMap(), this.getFunctionalYOnMap(), this.objectFlags.getCombobulatorResourceDistance()
                  )) {
                     if (this.resource.getResourceType()[ox.getX()][ox.getY()] == this.objectFlags.getCombobulatorResourceType()) {
                        validCoordinates.add(ox);
                     }
                  }

                  if (validCoordinates.size() == 0) {
                     return;
                  }
               }
            }

            int interactXCenter = this.getFunctionalXOnMap() * 16 + 8;
            int interactYCenter = this.getFunctionalYOnMap() * 16 + 8;
            int resourceXCenter = 0;
            int resourceYCenter = 0;
            if (useMobs) {
               Collections.shuffle(nearByMobs);

               for (MobBase mxx : nearByMobs) {
                  if (combobulatorSpawnTypes.contains(mxx.getMobType())) {
                     resourceXCenter = mxx.getCenterPixelX(true);
                     resourceYCenter = mxx.getCenterPixelY(true);
                     this.mob.deleteMob(mxx);
                     break;
                  }
               }
            } else if (useBuilding) {
               Collections.shuffle(nearByBuildings);

               for (ObjectBase oxx : nearByBuildings) {
                  if (oxx.isResourceAvailable(this.objectFlags.getCombobulatorResourceType())) {
                     OrderedPair random = oxx.getRandomCoordinates();
                     resourceXCenter = random.getX() * 16 + 8;
                     resourceYCenter = random.getY() * 16 + 8;
                     oxx.consumeStores(oxx.getAvailableResource(this.objectFlags.getCombobulatorResourceType()));
                     break;
                  }
               }
            } else {
               OrderedPair oxxx = validCoordinates.get(Utilities.randomInt(validCoordinates.size()));
               ResourceBase rx = this.resource.getResourceOnGround(oxxx.getX(), oxxx.getY());
               if (rx != null) {
                  this.resource.deleteResource(rx);
               } else {
                  this.resource.damageResource(oxxx.getX(), oxxx.getY());
               }

               resourceXCenter = oxxx.getX() * 16 + 8;
               resourceYCenter = oxxx.getY() * 16 + 8;
            }

            this.projectile.newEnergyBeam(interactXCenter, interactYCenter, resourceXCenter, resourceYCenter);
            if (this.map.isInView(this.getCenterPixelX(), this.getCenterPixelY(), 128)) {
               for (int x = 0; x < 10; x++) {
                  this.projectile.newEnergyRing(interactXCenter, interactYCenter, 16, ParticleModule.ParticleSet.FIRE_BLUE);
               }

               for (int x = 0; x < 100; x++) {
                  this.particle
                     .newParticle(
                        resourceXCenter + Utilities.randomInt(-8, 8),
                        resourceYCenter + Utilities.randomInt(-8, 8),
                        ParticleModule.ParticleType.SMOKE_SPECK,
                        ParticleModule.ParticleSet.FIRE_GREEN
                     );
               }
            }

            OrderedPair oxxx = this.getBestFunctionalCoordinates(true);
            MobBase.MobType mobTypeOut = combobulatorSpawnTypes.get(Utilities.randomInt(combobulatorSpawnTypes.size()));
            if (mobTypeOut == MobBase.MobType.CUBE_E_GOLEM) {
               this.goal.incrementGoal(GoalModule.GoalType.WHAT_IS_MY_PURPOSE);
            }

            MobBase newMob = this.mob.newMob(oxxx.getX(), oxxx.getY(), mobTypeOut, true);
            newMob.setAssignedSpawn(this.objectID);
            this.combobulatorSpawns.add(newMob);
            if (this.objectFlags.getCombobulatorBonusLevel() > 0) {
               newMob.setLevel(newMob.getLevel() + this.objectFlags.getCombobulatorBonusLevel());
            }

            this.combobulatorEnergyRequiredTick = 0;
         }
      }
   }

   private void destroyIfIgnoredUpdate() throws SlickException {
      if (this.objectFlags.getSubType() == ObjectBase.ObjectSubType.CONSTRUCTION && this.hitPointsMax == 0) {
         if (this.destroyIfIgnoredDelayTick >= this.objectFlags.getDestroyIfIgnoredDelay()) {
            this.destroyObject(false);
            return;
         }

         this.destroyIfIgnoredDelayTick++;
      }
   }

   private void cullisGateParticles() {
      int centerX = this.getFunctionalXOnMap() * 16 + 8;
      int centerY = this.getFunctionalYOnMap() * 16 + 8;
      if (this.map.isInView(centerX, centerY, 128)
         && Utilities.randomInt(20) == 0
         && this.save.getActiveRegionalSave().getGameType() != StateBase.GameType.SKIRMISH) {
         if (this.time.getTimeOfDay() != TimeModule.TimeOfDay.DAWN
            && this.time.getTimeOfDay() != TimeModule.TimeOfDay.MORNING
            && this.time.getTimeOfDay() != TimeModule.TimeOfDay.MIDDAY) {
            this.projectile.newMagicTrail(centerX, centerY, ParticleModule.ParticleSet.FIRE_GREEN);
         } else {
            this.projectile.newMagicTrail(centerX, centerY, ParticleModule.ParticleSet.FIRE_BLUE);
         }
      }
   }

   private void lootBoxBehavior() throws SlickException {
      if (this.lootBoxCooldown < 50) {
         this.lootBoxCooldown++;
      }

      ArrayList<ResourceBase> keys = this.resources.get(ResourceModule.ResourceType.SUSPICICIOUS_KEY);
      if (!this.lootBoxActive && Utilities.randomInt(50) == 0) {
         this.mob.tryConfuseInAreaIfIdle(this.getCenterCoordinateX(), this.getCenterCoordinateY(), 4, true, 2);
      }

      if (keys.size() > 0 && !this.lootBoxActive) {
         int x = this.objectX * 16 + 8;
         int y = this.objectY * 16 + 8;
         this.resource.deleteResource(keys.get(0));
         this.lootBoxActive = true;
         this.lootBoxCategory = ResourceModule.ResourceCategory.values()[Utilities.randomInt(ResourceModule.ResourceCategory.values().length)];

         while (
            this.lootBoxCategory == ResourceModule.ResourceCategory.MISCELLANEOUS
               || this.lootBoxCategory == ResourceModule.ResourceCategory.TRASH && Utilities.randomInt(3) != 0
         ) {
            this.lootBoxCategory = ResourceModule.ResourceCategory.values()[Utilities.randomInt(ResourceModule.ResourceCategory.values().length)];
         }

         this.sound.playSound(SoundModule.SoundType.LOOT_BOX_OPEN, x, y);
         if (this.lootBoxCategory == ResourceModule.ResourceCategory.TRASH) {
            this.goal.incrementGoal(GoalModule.GoalType.I_WANT_MY_KEY_BACK);
         }

         this.goal.incrementGoal(GoalModule.GoalType.ELECTRONIC_SHARKS);
      }

      if (this.lootBoxActive) {
         int x = MapModule.getCenterPixelOfCoordinateX(this.objectX);
         int y = MapModule.getCenterPixelOfCoordinateY(this.objectY);
         this.lootBoxCountTick++;
         if (this.lootBoxCountTick == 100) {
            this.sound.playSound(SoundModule.SoundType.HOLY_LOUD, x, y);
            this.currentPhase = MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_5;
            this.map.updateObject(this.objectX, this.objectY, this.objectFlags.getCurrentType(), this.currentPhase);
            this.updateObjectMapData();
            this.lighting.addShortTermLight(this.objectX, this.objectY, (byte)24, (byte)16, 128);
            this.mob.influenceFaithInArea(this.objectX, this.objectY, 16, 2, 8, true);
            this.god.increaseGodXP(5.0, GodModule.GodXPType.OPENED_LOOT_BOXES);
         }

         if (this.lootBoxCountTick >= 150) {
            ResourceModule.ResourceType typeOut = ResourceModule.ResourceType.values()[Utilities.randomInt(ResourceModule.ResourceType.values().length)];

            while (typeOut.getTemplate().getResourceCategory() != this.lootBoxCategory) {
               typeOut = ResourceModule.ResourceType.values()[Utilities.randomInt(ResourceModule.ResourceType.values().length)];
            }

            if (this.lootBoxCountTick % 5 == 0) {
               int randomX = this.objectX + Utilities.randomInt(-4, 4);
               int randomY = this.objectY + Utilities.randomInt(-4, 4);
               OrderedPair o = this.resource.findOpenSpace(randomX, randomY, MapData.BlockMapGroup.STANDARD);
               this.resource.createResourceOnGround(typeOut, ResourceModule.ResourceColorSet.DEFAULT, o.getX(), o.getY());
               this.mob.tryPanicInArea(o.getX(), o.getY(), 2, true, 2);
               this.mob.influenceFaithInArea(o.getX(), o.getY(), 2, 1, true);
               x = o.getX() * 16 + 8;
               y = o.getY() * 16 + 8;
               if (this.map.isInView(this.getCenterPixelX(), this.getCenterPixelY(), 128)) {
                  this.projectile.newGroundStatic(x + Utilities.randomInt(-3, 3), y + Utilities.randomInt(-3, 3), ParticleModule.ParticleSet.FIRE_BLUE);
                  this.projectile
                     .newExpandingFireRing(x + Utilities.randomInt(-3, 3), y + Utilities.randomInt(-3, 3), 16, 1.0F, ParticleModule.ParticleSet.FIRE_BLUE);

                  for (int i2 = 0; i2 < 10; i2++) {
                     this.particle
                        .newParticle(
                           x + Utilities.randomInt(-8, 8),
                           y + Utilities.randomInt(-8, 8),
                           ParticleModule.ParticleType.SMOKE_SPECK,
                           ParticleModule.ParticleSet.FIRE_BLUE
                        );
                  }
               }

               if (this.lootBoxCountTick % 25 == 0) {
                  this.mob.tryConfuseInAreaIfIdle(o.getX(), o.getY(), 8, true, 2);
                  this.mob.influenceFaithInArea(o.getX(), o.getY(), 6, 1, true);
               }
            }

            int lootBoxShutoffChance = 100;
            int lootBoxMaxResourcesTick = 500;
            if (this.perk.getPerkTypeTotal(PerkModule.PerkType.MORE_LOOT_IN_LOOT_BOXES) > 0.0) {
               lootBoxShutoffChance = (int)(
                  lootBoxShutoffChance + lootBoxShutoffChance * this.perk.getPerkTypeTotal(PerkModule.PerkType.MORE_LOOT_IN_LOOT_BOXES)
               );
               lootBoxMaxResourcesTick = (int)(
                  lootBoxMaxResourcesTick + lootBoxMaxResourcesTick * this.perk.getPerkTypeTotal(PerkModule.PerkType.MORE_LOOT_IN_LOOT_BOXES)
               );
            }

            if (this.lootBoxCountTick > 300 && (Utilities.randomInt(lootBoxShutoffChance) == 0 || this.lootBoxCountTick >= lootBoxMaxResourcesTick)) {
               this.sound.playSound(SoundModule.SoundType.HEAVY_WIND, x, y);
               this.weather.newLightning(this.objectX, this.objectY, 512, true, ParticleModule.ParticleSet.MAGIC_BLUE);
               x = this.objectX * 16 + 8;
               y = this.objectY * 16 + 8;
               if (this.map.isInView(this.getCenterPixelX(), this.getCenterPixelY(), 128)) {
                  this.projectile
                     .newExpandingFireRing(x + Utilities.randomInt(-3, 3), y + Utilities.randomInt(-3, 3), 256, 4.0F, ParticleModule.ParticleSet.FIRE_BLUE);
               }

               this.mob.influenceFaithInArea(this.objectX, this.objectY, 48, 5, 15, true);
               this.destroyObject(false);
            }
         }
      }
   }

   public void pokeLootBox() throws SlickException {
      if (this.lootBoxCooldown >= 50 && !this.lootBoxActive) {
         int x = this.getCenterPixelX();
         int y = this.getCenterPixelY();
         if (this.lootBoxClicks <= 2) {
            this.projectile
               .newExpandingFireRing(x + Utilities.randomInt(-3, 3), y + Utilities.randomInt(-3, 3), 32, 1.0F, ParticleModule.ParticleSet.FIRE_BLUE);
            this.sound.playSound(SoundModule.SoundType.LOOT_BOX_LOCKED, x, y);
            this.font.newFloater(Text.getText("object.building.lootBox.clicked"), x, y);
            this.lootBoxClicks++;
         } else {
            OrderedPair center = this.getCenterCoordinates();
            ArrayList<OrderedPair> randomCoordinates = Utilities.getAllCoordinatesWithin(center.getX(), center.getY(), 16);
            ObjectBase camp = this.object.getFirstBaseTypeFound(MapTilesLoader.TileSet.CASTLE_1);
            OrderedPair lootBoxCenter = this.getCenterCoordinates();
            if (camp != null && !this.object.isInRange(lootBoxCenter.getX(), lootBoxCenter.getY())) {
               OrderedPair campCenter = camp.getCenterCoordinates();
               OrderedPair.sortPairsDistance(campCenter.getX(), campCenter.getY(), randomCoordinates);
            } else {
               Collections.shuffle(randomCoordinates);
            }

            for (OrderedPair o : randomCoordinates) {
               if (Utilities.randomInt(64) == 1 && this.map.canPlaceObject(o.getX(), o.getY(), this.objectFlags.getBaseType(), true)) {
                  this.projectile
                     .newExpandingFireRing(x + Utilities.randomInt(-3, 3), y + Utilities.randomInt(-3, 3), 256, 4.0F, ParticleModule.ParticleSet.FIRE_BLUE);
                  this.projectile.newGroundStatic(x + Utilities.randomInt(-3, 3), y + Utilities.randomInt(-3, 3), ParticleModule.ParticleSet.FIRE_BLUE);
                  this.projectile.newEnergyBeam(x, y, MapModule.getCenterPixelOfCoordinateX(o.getX()), MapModule.getCenterPixelOfCoordinateY(o.getY()));
                  this.map.placeObject(o.getX(), o.getY(), this.objectFlags.getBaseType(), this.objectFlags.getSubType(), true, true);
                  this.sound.playSound(SoundModule.SoundType.GRAB, x, y);
                  this.sound.playSound(SoundModule.SoundType.MAGIC_ZAP, x, y);
                  this.destroyObject(false);
                  this.goal.incrementGoal(GoalModule.GoalType.STOP_POKING_ME);
                  this.mob.tryConfuseInAreaIfIdle(o.getX(), o.getY(), 6, true, 2);
                  this.mob.influenceFaithInArea(o.getX(), o.getY(), 4, 1, true);
                  this.mob.tryConfuseInAreaIfIdle(center.getX(), center.getY(), 6, true, 2);
                  this.mob.influenceFaithInArea(center.getX(), center.getY(), 4, 1, false);
                  break;
               }
            }
         }
      }
   }

   private void vesselRegenBehavior() throws SlickException {
      if (!this.paused && this.isInRange()) {
         this.vesselRegenerationRateTick++;
         if (this.vesselRegenerationRateTick > 10) {
            this.vesselRegenerationRateTick = 0;
            if (this.energy > 0) {
               for (OrderedPair o : this.vesselPoints) {
                  ResourceBase r = this.resource.getResourceOnGround(o);
                  if (r != null && r.getResourceDurability() < r.getResourceDurabilityMax() - 50) {
                     r.increaseDurability(50);
                     this.decreaseEnergy(1);

                     for (int i = 0; i < 100; i++) {
                        this.particle
                           .newParticleGroup(
                              r.getCenterPixelX(true),
                              r.getCenterPixelY(true),
                              ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS,
                              ParticleModule.ParticleSet.FIRE_PURPLE
                           );
                     }
                  }
               }
            }
         }
      }
   }

   private void instability() throws SlickException {
      if (this.instabilityAmount != 0) {
         this.instabilityAmount--;
         int centerTileX = this.functionalCoordinates.getX();
         int centerTileY = this.functionalCoordinates.getY();
         int centerX = centerTileX * 16 + 8;
         int centerY = centerTileY * 16 + 8;
         int instabilityMax = this.objectFlags.getInstabilityAmountMax();
         if (this.map.isInView(this.getCenterPixelX(), this.getCenterPixelY(), 128)) {
            if (this.instabilityAmount >= instabilityMax * 0.1 && Utilities.randomInt(2) == 0) {
               this.particle.newParticle(centerX, centerY, ParticleModule.ParticleType.MAGIC_FLOAT_SPECK, ParticleModule.ParticleSet.FIRE_YELLOW);
            }

            if (this.instabilityAmount >= instabilityMax * 0.3) {
               if (Utilities.randomInt(20) == 0) {
                  this.projectile.newMagicTrail(centerX, centerY, ParticleModule.ParticleSet.FIRE_YELLOW);
               }

               if (Utilities.randomInt(10) == 0) {
                  this.projectile.newExpandingEnergyTrail(centerX, centerY, ParticleModule.ParticleSet.FIRE_YELLOW);
               }

               if (Utilities.randomInt(2) == 0) {
                  this.particle.newParticle(centerX, centerY, ParticleModule.ParticleType.MAGIC_FLOAT_SPECK, ParticleModule.ParticleSet.FIRE_YELLOW);
               }
            }

            if (this.instabilityAmount >= instabilityMax * 0.5) {
               if (Utilities.randomInt(20) == 0) {
                  this.projectile.newMagicTrail(centerX, centerY, ParticleModule.ParticleSet.FIRE_YELLOW);
               }

               if (Utilities.randomInt(2) == 0) {
                  this.particle.newParticle(centerX, centerY, ParticleModule.ParticleType.MAGIC_FLOAT_SPECK, ParticleModule.ParticleSet.FIRE_YELLOW);
               }
            }
         }

         if (this.instabilityAmount >= instabilityMax * 0.5 && Utilities.randomInt(150) == 0) {
            this.weather.newLightning(centerTileX, centerTileY, 512, true, ParticleModule.ParticleSet.MAGIC_BLUE);
         }

         if (this.instabilityAmount >= instabilityMax * 0.95) {
            if (Utilities.randomInt(100) == 0) {
               this.weather.newLightning(centerTileX, centerTileY, 512, true, ParticleModule.ParticleSet.MAGIC_BLUE);
            }

            if (this.map.isInView(this.getCenterPixelX(), this.getCenterPixelY(), 128)) {
               this.projectile.newExpandingEnergyTrail(centerX, centerY, ParticleModule.ParticleSet.FIRE_YELLOW);
            }
         } else if (this.instabilityAmount >= instabilityMax * 0.9) {
            if (Utilities.randomInt(100) == 0) {
               this.weather.newLightning(centerTileX, centerTileY, 512, true, ParticleModule.ParticleSet.MAGIC_BLUE);
            }

            if (this.map.isInView(this.getCenterPixelX(), this.getCenterPixelY(), 128)) {
               this.projectile.newExpandingEnergyTrail(centerX, centerY, ParticleModule.ParticleSet.FIRE_YELLOW);
            }
         }

         if (this.objectFlags.isCullisGate() && this.instabilityAmount >= instabilityMax * 0.7) {
            int lightningGenerationRate = 750;
            if (this.instabilityAmount >= instabilityMax * 0.8) {
               lightningGenerationRate -= 300;
            }

            if (this.instabilityAmount >= instabilityMax * 0.9) {
               lightningGenerationRate -= 100;
            }

            if (Utilities.randomInt(lightningGenerationRate) == 0) {
               if (Utilities.randomInt(2) == 0) {
                  this.weather
                     .newLightning(
                        centerTileX + Utilities.randomInt(-8, 8), centerTileY + Utilities.randomInt(-8, 8), 512, true, ParticleModule.ParticleSet.MAGIC_BLUE
                     );
               }

               this.weather
                  .newLightning(
                     centerTileX + Utilities.randomInt(-4, 4), centerTileY + Utilities.randomInt(-4, 4), 512, true, ParticleModule.ParticleSet.MAGIC_BLUE
                  );
            }
         }

         if (this.hitPoints <= this.hitPointsMax * 0.1 && !this.instabilityEarthquakeTriggered) {
            this.instabilityEarthquakeTriggered = true;
            this.influence.forceCastSpell(centerX, centerY, SpellBase.SpellType.EARTHQUAKE);
            this.goal.incrementGoal(GoalModule.GoalType.EXPLOSIVE_SIDE_EFFECTS);
         }
      }
   }

   private void onFireUpdate() throws SlickException {
      if (Utilities.randomInt(150) == 0 || this.weather.isRaining() && Utilities.randomInt(25) == 0) {
         if (this.smokeDamageArray.size() > 0) {
            this.smokeDamageArray.remove(Utilities.randomInt(this.smokeDamageArray.size()));
         }

         if (this.fireDamageArray.size() > 0) {
            this.fireDamageArray.remove(Utilities.randomInt(this.fireDamageArray.size()));
         }
      }

      if (this.map.isInView(this.getCenterPixelX(), this.getCenterPixelY(), 128)) {
         for (OrderedPair o : this.smokeDamageArray) {
            if (Utilities.randomInt(50) == 0) {
               this.particle
                  .newParticle(
                     this.objectX * 16 + o.getX() + Utilities.randomInt(-2, 2),
                     this.objectY * 16 + o.getY() + Utilities.randomInt(-2, 2),
                     ParticleModule.ParticleType.SMOKE_SPECK,
                     ParticleModule.ParticleSet.DARK_GRAY
                  );
            }
         }

         for (OrderedPair ox : this.fireDamageArray) {
            this.particle
               .newParticle(
                  this.objectX * 16 + ox.getX() + Utilities.randomInt(-2, 2),
                  this.objectY * 16 + ox.getY() + Utilities.randomInt(-2, 2),
                  ParticleModule.ParticleType.FIRE_SPECK,
                  ParticleModule.ParticleSet.FIRE_RED
               );
            if (Utilities.randomInt(50) == 0) {
               this.particle
                  .newParticle(
                     this.objectX * 16 + ox.getX() + Utilities.randomInt(-2, 2),
                     this.objectY * 16 + ox.getY() + Utilities.randomInt(-2, 2),
                     ParticleModule.ParticleType.SMOKE_SPECK,
                     ParticleModule.ParticleSet.DARK_GRAY
                  );
            }
         }
      }

      for (int x = 0; x < this.fireDamageArray.size(); x++) {
         if (Utilities.randomInt(500) == 0) {
            this.decreaseHitPoints((int)(this.hitPointsMax * 0.01F));
         }

         if (this.hitPoints <= 0) {
            this.destroyObject(true);
            break;
         }
      }
   }

   private void lightningRodEnergyDissipate() throws SlickException {
      if (!this.paused) {
         if (this.energy > 0) {
            this.lightningRodDissipationRateTick++;
            if (this.lightningRodDissipationRateTick > this.objectFlags.getLightningRodDissipationRate()) {
               int randomOut = Utilities.randomInt(10) + 1;

               while (randomOut > this.energy) {
                  randomOut = Utilities.randomInt(10) + 1;
               }

               this.lightningRodDissipationRateTick = 0;
               this.decreaseEnergy(randomOut);
               this.essence.newEssence(this.getLightningRodXOnMap() * 16 + 8, this.getLightningRodYOnMap() * 16 + 8, randomOut, 1);
            }
         }
      }
   }

   private void energyRegenerate() throws SlickException {
      if (!this.paused) {
         if (this.energy < this.objectFlags.getEnergyMax() && this.objectFlags.canEnergyRegenerateFree()) {
            this.energyRegenerationRateTick++;
         }

         if (this.energyRegenerationRateTick > this.objectFlags.getEnergyRegenerationRate()) {
            this.increaseEnergy(1);
            this.energyRegenerationRateTick = 0;
         }
      }
   }

   private void burnTrash() throws SlickException {
      if (!this.paused) {
         ResourceModule.ResourceType resourceToBurn = null;
         if (this.isResourceAvailable(ResourceModule.ResourceType.TRASHY_CUBE)) {
            resourceToBurn = ResourceModule.ResourceType.TRASHY_CUBE;
         } else if (this.isResourceAvailable(ResourceModule.ResourceType.TRASHY_TRASH)) {
            resourceToBurn = ResourceModule.ResourceType.TRASHY_TRASH;
         } else {
            ResourceModule.ResourceType[] var5;
            for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
               if (t.getTemplate().getResourceCategory() == ResourceModule.ResourceCategory.TRASH && this.isResourceAvailable(t)) {
                  resourceToBurn = t;
                  break;
               }
            }
         }

         if (resourceToBurn != null) {
            this.burnerRateTick++;
            this.burnerEnergyDissipationRateTick = 0;
            if (this.burnerRateTick > this.objectFlags.getBurnerRate()) {
               ResourceBase trash = this.getAvailableResource(resourceToBurn);
               int rate = 5;
               if (trash.getUses() < 5) {
                  rate = trash.getUses();
               }

               if (this.energy < this.objectFlags.getEnergyMax()) {
                  this.increaseEnergy(rate);
               } else {
                  this.essence.newEssence(this.getEssenceReturnXOnMapExactPixel(), this.getEssenceReturnYOnMapExactPixel(), rate, 1);
               }

               trash.decreaseUses(rate);
               if (trash.getUses() <= 0) {
                  this.goal.incrementGoal(GoalModule.GoalType.LITERAL_DUMPSTER_FIRE);
               }

               this.burnerRateTick = 0;
            }
         } else {
            this.energyRegenerationRateTick = 0;
            this.burnerEnergyDissipationRateTick++;
            if (this.burnerEnergyDissipationRateTick > this.objectFlags.getBurnerEnergyDissipationRate()) {
               this.decreaseEnergy(1);
               this.burnerEnergyDissipationRateTick = 0;
            }
         }
      }
   }

   private void mergeResources() throws SlickException {
      ResourceModule.ResourceType[] var4;
      for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
         if (type.getTemplate().canMergeUses() && this.objectFlags.canBuildingStore(type)) {
            int max = type.getTemplate().getUsesMax();
            ArrayList<ResourceBase> mergeList = this.resources.get(type);
            ArrayList<ResourceBase> notFull = new ArrayList<>();
            int usesTotal = 0;

            for (ResourceBase r : mergeList) {
               if (!r.isAssigned() && r.getUses() < max) {
                  notFull.add(r);
                  usesTotal += r.getUses();
               }
            }

            if (notFull.size() > 1) {
               for (ResourceBase rx : notFull) {
                  if (usesTotal > max) {
                     rx.setUses(max);
                     usesTotal -= max;
                  } else if (usesTotal > 0) {
                     rx.setUses(usesTotal);
                     usesTotal = 0;
                  } else {
                     this.resource.deleteResource(rx);
                  }
               }
            }
         }
      }
   }

   private void purifyWater() throws SlickException {
      if (!this.paused) {
         if (this.resources.get(ResourceModule.ResourceType.DIRTY_WATER_BUCKET).size() > 0
            && this.getAcceptStorageResourceCount(ResourceModule.ResourceType.WATER_BUCKET, -1) > 0) {
            this.purifyWaterRateTick++;
            if (this.purifyWaterRateTick > this.objectFlags.getPurifyWaterRate()) {
               this.decreaseDirtyWater(1);
               this.increaseWater(1);
               this.goal.incrementGoal(GoalModule.GoalType.NESTLED_PURE_LIFE_WATER);
               this.purifyWaterRateTick = 0;
               if (Utilities.randomInt(2000) == 0) {
                  OrderedPair o = this.getBestRandomInteractCoordinates(true);
                  if (this.resource.isResourcesOnGround(o.getX(), o.getY())) {
                     o = this.resource.findOpenSpace(o.getX(), o.getY(), MapData.BlockMapGroup.STANDARD);
                  }

                  if (o != null) {
                     this.resource
                        .createResourceOnGround(ResourceModule.ResourceType.TRASHY_TRASH, ResourceModule.ResourceColorSet.DEFAULT, o.getX(), o.getY());
                  }
               }
            }
         }
      }
   }

   private void catchRain() throws SlickException {
      if (!this.paused) {
         if (this.weather.isRaining()) {
            this.rainCatchRateTick++;
            if (this.rainCatchRateTick > this.objectFlags.getRainCatchRate()) {
               this.increaseWater(1);
               if (this.objectFlags.getBaseType() == MapTilesLoader.TileSet.RAIN_CATCHER) {
                  this.goal.incrementGoal(GoalModule.GoalType.CATCH_IT_ALL);
               }

               this.rainCatchRateTick = 0;
            }

            if (this.objectFlags.getBaseType() == MapTilesLoader.TileSet.RAIN_CATCHER && this.map.isInView(this.getCenterPixelX(), this.getCenterPixelY(), 128)
               )
             {
               for (int x = 0; x < 2; x++) {
                  int startX = (this.objectX + 1) * 16 + 6;
                  int startY = (this.objectY + 1) * 16 + 6;
                  startX += Utilities.randomInt(20);
                  startY += Utilities.randomInt(20);
                  this.particle.newParticle(startX, startY, ParticleModule.ParticleType.WATER_POOL_FAST, ParticleModule.ParticleSet.WATER);
               }
            }
         }
      }
   }

   private void generateWater() throws SlickException {
      if (!this.paused) {
         this.waterGenerationRateTick++;
         if (this.waterGenerationRateTick > this.objectFlags.getWaterGenerationRate()) {
            this.increaseWater(1);
            this.waterGenerationRateTick = 0;
         }
      }
   }

   public int getTotalWater() {
      int total = 0;

      for (ResourceBase r : this.resources.get(ResourceModule.ResourceType.WATER_BUCKET)) {
         total += r.getUses();
      }

      return total;
   }

   public int getTotalDirtyWater() {
      int total = 0;

      for (ResourceBase r : this.resources.get(ResourceModule.ResourceType.DIRTY_WATER_BUCKET)) {
         total += r.getUses();
      }

      return total;
   }

   public int getTotalGoldCoins() {
      int total = 0;

      for (ResourceBase r : this.resources.get(ResourceModule.ResourceType.GOLD_COIN_SACK)) {
         total += r.getUses();
      }

      return total;
   }

   public void removeGoldCoins(int i) throws SlickException {
      int totalAmountLeft = i;
      ArrayList<ResourceBase> searchCoins = new ArrayList<>();
      searchCoins.addAll(this.resources.get(ResourceModule.ResourceType.GOLD_COIN_SACK));
      boolean done = false;

      for (ResourceBase r : searchCoins) {
         if (!r.isAssigned()) {
            if (r.getUses() >= totalAmountLeft) {
               r.decreaseUses(totalAmountLeft);
               done = true;
               break;
            }

            totalAmountLeft -= r.getUses();
            this.resource.deleteResource(r);
         }
      }

      if (!done) {
         searchCoins.clear();
         searchCoins.addAll(this.resources.get(ResourceModule.ResourceType.GOLD_COIN_SACK));

         for (ResourceBase rx : searchCoins) {
            if (!rx.isAssigned()) {
               if (rx.getUses() >= totalAmountLeft) {
                  rx.decreaseUses(totalAmountLeft);
                  break;
               }

               totalAmountLeft -= rx.getUses();
               this.resource.deleteResource(rx);
            }
         }
      }
   }

   private void deliverEssence() throws SlickException {
      if (!this.paused && this.isInRange()) {
         if (this.energy > 0) {
            ArrayList<ObjectBase> objectList = null;
            if (this.objectFlags.canDeliverEssenceToCollectors()) {
               objectList = this.object.getObjectsBySortGroup(ObjectModule.ObjectSortGroup.COLLECTS_ESSENCE);
            } else if (this.objectFlags.canCollectEssence()) {
               objectList = this.object.getObjectsBySortGroup(ObjectModule.ObjectSortGroup.ACCEPTS_ESSENCE);
            }

            for (int i = objectList.size() - 1; i >= 0; i--) {
               ObjectBase o = objectList.get(i);
               if (o.paused || o.getEnergy() + o.getEnergyInRoute() >= o.getObjectFlags().getEnergyMax()) {
                  objectList.remove(o);
               }
            }

            if (objectList.size() > 0) {
               ObjectBase target = objectList.get(Utilities.randomInt(objectList.size()));
               int randomEnergy = Utilities.randomInt(10) + 1;

               while (randomEnergy > this.energy || target.getEnergy() + target.getEnergyInRoute() + randomEnergy > target.getObjectFlags().getEnergyMax()) {
                  randomEnergy = Utilities.randomInt(10) + 1;
               }

               this.essence
                  .convertObjectEnergyToEssence(this.getEssenceReturnXOnMapExactPixel(), this.getEssenceReturnYOnMapExactPixel(), randomEnergy, target);
               this.decreaseEnergy(randomEnergy);
            }
         }
      }
   }

   private void releaseEssenceToGod() throws SlickException {
      if (!this.paused) {
         if (this.energy > 0
            && this.influence.getInfluenceAmount() < this.influence.getInfluenceAmountMax() - this.influence.getInfluenceMaintainingAmount()
            && Utilities.randomInt(10) == 0) {
            int mouseX = Game.getCS().getMouseTileX();
            int mouseY = Game.getCS().getMouseTileY();
            if (Utilities.coordinatesWithin(this.getEssenceReturnXOnMap(), this.getEssenceReturnYOnMap(), mouseX, mouseY, 4)) {
               this.essenceMouseDelayTick++;
               if (this.essenceMouseDelayTick > 5) {
                  int randomEnergy = Utilities.randomInt(3) + 1;

                  while (randomEnergy > this.energy) {
                     randomEnergy = Utilities.randomInt(3) + 1;
                  }

                  this.essence
                     .convertObjectEnergyToEssence(this.getEssenceReturnXOnMapExactPixel(), this.getEssenceReturnYOnMapExactPixel(), randomEnergy, true);
                  this.decreaseEnergy(randomEnergy);
               }
            } else {
               this.essenceMouseDelayTick = 0;
            }
         }
      }
   }

   public void increaseStores(ResourceBase base) throws SlickException {
      ResourceModule.ResourceType type = base.getType();
      this.resources.get(type).add(base);
      this.font
         .newFloater(
            this.resource.getResourceSpriteSet(type, ResourceModule.ResourceColorSet.DEFAULT).getIconName(),
            "+1",
            (this.objectX + this.objectFlags.getWidth() / 2) * 16,
            (this.objectY + this.objectFlags.getHeight() / 2) * 16
         );
      this.sound.playSound(SoundModule.SoundType.STORE_ITEM, this.getCenterCoordinateX() * 16, this.getCenterCoordinateY() * 16);
      this.updateObject();
   }

   private void increaseValue(ResourceBase base) throws SlickException {
      ResourceModule.ResourceType type = base.getType();
      this.resourceValues.put(type, this.resourceValues.get(type) + 1);
      this.resourceValuesColors.get(type).add(base.getColor());
      this.resource.deleteResource(base);
      this.increaseHitPoints(50);
   }

   public ResourceBase takeStores(ResourceModule.ResourceType type) throws SlickException {
      ResourceBase returnedResource = null;

      for (ResourceBase r : this.resources.get(type)) {
         if (!r.isAssigned()) {
            returnedResource = r;
            break;
         }
      }

      if (returnedResource != null) {
         this.resources.get(type).remove(returnedResource);
         this.font
            .newFloater(
               this.resource.getResourceSpriteSet(type, ResourceModule.ResourceColorSet.DEFAULT).getIconName(),
               "-1",
               (this.objectX + this.objectFlags.getWidth() / 2) * 16,
               (this.objectY + this.objectFlags.getHeight() / 2) * 16
            );
         this.sound.playSound(SoundModule.SoundType.STORE_ITEM, this.getCenterCoordinateX() * 16, this.getCenterCoordinateY() * 16);
         this.updateObject();
         return returnedResource;
      } else {
         return null;
      }
   }

   public ResourceBase takeStores(ResourceBase r) throws SlickException {
      if (this.resources.get(r.getType()).contains(r)) {
         this.resources.get(r.getType()).remove(r);
         this.font
            .newFloater(
               this.resource.getResourceSpriteSet(r.getType(), ResourceModule.ResourceColorSet.DEFAULT).getIconName(),
               "-1",
               (this.objectX + this.objectFlags.getWidth() / 2) * 16,
               (this.objectY + this.objectFlags.getHeight() / 2) * 16
            );
         this.sound.playSound(SoundModule.SoundType.STORE_ITEM, this.getCenterCoordinateX() * 16, this.getCenterCoordinateY() * 16);
         this.updateObject();
         return r;
      } else {
         return null;
      }
   }

   public void consumeStores(ResourceBase r) throws SlickException {
      this.resource.deleteResource(r);
      this.resources.get(r.getType()).remove(r);
      this.font
         .newFloater(
            this.resource.getResourceSpriteSet(r.getType(), ResourceModule.ResourceColorSet.DEFAULT).getIconName(),
            "-1",
            (this.objectX + this.objectFlags.getWidth() / 2) * 16,
            (this.objectY + this.objectFlags.getHeight() / 2) * 16
         );
      this.sound.playSound(SoundModule.SoundType.STORE_ITEM, this.getCenterCoordinateX() * 16, this.getCenterCoordinateY() * 16);
      this.updateObject();
   }

   public void consumeValue(ResourceModule.ResourceType type) throws SlickException {
      this.resourceValues.put(type, this.resourceValues.get(type) - 1);
      this.font
         .newFloater(
            this.resource.getResourceSpriteSet(type, ResourceModule.ResourceColorSet.DEFAULT).getIconName(),
            "-1",
            (this.objectX + this.objectFlags.getWidth() / 2) * 16,
            (this.objectY + this.objectFlags.getHeight() / 2) * 16
         );
      this.sound.playSound(SoundModule.SoundType.STORE_ITEM, this.getCenterCoordinateX() * 16, this.getCenterCoordinateY() * 16);
      this.updateObject();
   }

   protected boolean intersects(Shape mouse) {
      return mouse.intersects(this.intersectBox);
   }

   public void removeAllMobs() throws SlickException {
      for (int i = 0; i < this.occupants.size(); i++) {
         if (this.occupants.get(i).isInsideBuilding()
            && this.object.getBuildingAtLocation(this.occupants.get(i).getTileX(), this.occupants.get(i).getTileY()) == this) {
            this.occupants.get(i).leaveBuilding();
         }

         this.occupants.get(i).unassignHomeBuilding();
      }

      this.occupants.clear();

      for (int i = 0; i < this.combobulatorSpawns.size(); i++) {
         if (this.combobulatorSpawns.get(i).isInsideBuilding()
            && this.object.getBuildingAtLocation(this.combobulatorSpawns.get(i).getTileX(), this.combobulatorSpawns.get(i).getTileY()) == this) {
            this.combobulatorSpawns.get(i).leaveBuilding();
         }

         this.combobulatorSpawns.get(i).unassignSpawnBuilding();
      }

      this.combobulatorSpawns.clear();
   }

   public void increaseCullisGateInstability(int i) throws SlickException {
      this.instabilityAmount += i;
      if (this.instabilityAmount > this.objectFlags.getInstabilityAmountMax()) {
         this.instabilityAmount = this.objectFlags.getInstabilityAmountMax();
      }
   }

   public OrderedPair getBestRandomInteractCoordinates(MobBase thisMob, boolean checkReachMap, boolean checkRangeMap, boolean allowRandom) {
      if (this.interactPoints.size() > 0) {
         ArrayList<OrderedPair> interactPointsShuffled = new ArrayList<>();
         interactPointsShuffled.addAll(this.interactPoints);
         Collections.shuffle(interactPointsShuffled);

         for (OrderedPair o : interactPointsShuffled) {
            if (thisMob.canReach(o)) {
               return o;
            }
         }
      }

      return allowRandom ? this.getRandomCoordinates(thisMob, checkReachMap, checkRangeMap) : OrderedPair.getOrderedPair(this.objectX, this.objectY);
   }

   public OrderedPair getBestRandomInteractCoordinates(boolean allowRandom) {
      if (this.interactPoints.size() > 0) {
         return this.interactPoints.get(Utilities.randomInt(this.interactPoints.size()));
      } else {
         return allowRandom ? this.getRandomCoordinates() : OrderedPair.getOrderedPair(this.objectX, this.objectY);
      }
   }

   public OrderedPair getBestFunctionalCoordinates(MobBase thisMob, boolean checkReachMap, boolean checkRangeMap, boolean allowRandom) {
      if (this.functionalCoordinates != null && thisMob.canReach(this.getFunctionalXOnMap(), this.getFunctionalYOnMap())) {
         return OrderedPair.getOrderedPair(this.getFunctionalXOnMap(), this.getFunctionalYOnMap());
      } else {
         return allowRandom ? this.getRandomCoordinates(thisMob, checkReachMap, checkRangeMap) : OrderedPair.getOrderedPair(this.objectX, this.objectY);
      }
   }

   public OrderedPair getBestFunctionalCoordinates(boolean allowRandom) {
      if (this.functionalCoordinates != null) {
         return OrderedPair.getOrderedPair(this.getFunctionalXOnMap(), this.getFunctionalYOnMap());
      } else {
         return allowRandom ? this.getRandomCoordinates() : OrderedPair.getOrderedPair(this.objectX, this.objectY);
      }
   }

   public String getFormattedName() {
      return "$YEL1" + this.objectFlags.getName();
   }

   public void increaseHitPoints(int amount) {
      this.hitPoints += amount;
      if (this.hitPoints > this.hitPointsMax) {
         this.hitPoints = this.hitPointsMax;
      }
   }

   public void decreaseHitPoints(int amount) {
      this.hitPoints -= amount;
      if (this.hitPoints < 0) {
         this.hitPoints = 0;
      }
   }

   public void takeDamage(int amount, MobBase.DamageType type, MobBase.DamageCause cause) throws SlickException {
      this.takeDamage(amount, null, null, type, cause);
   }

   public void takeDamage(int amount, MobBase m, MobBase.DamageType type, MobBase.DamageCause cause) throws SlickException {
      this.takeDamage(amount, m, null, type, cause);
   }

   public void takeDamage(int amount, ObjectBase o, MobBase.DamageType type, MobBase.DamageCause cause) throws SlickException {
      this.takeDamage(amount, null, o, type, cause);
   }

   public void takeDamage(int amount, MobBase m, ObjectBase o, MobBase.DamageType type, MobBase.DamageCause cause) throws SlickException {
      if (!this.isDestroyed() && !this.objectFlags.isInvulnerable() && this.hitPointsMax != 0) {
         int height = this.objectFlags.getHeight();
         int width = this.objectFlags.getWidth();
         if (this.objectFlags.allowDamageMonsterSpawns()
            && this.damageMonsterSpawnsDelayTick >= this.objectFlags.getDamageMonsterSpawnsDelay()
            && (m != null || o != null)) {
            ArrayList<MobBase.MobType> validTypes = this.spawn.getAllMonsterSpawnTypes();
            int monsterAmount = height * width / 3;
            if (monsterAmount < 1) {
               monsterAmount = 1;
            }

            for (int x = 0; x < monsterAmount; x++) {
               OrderedPair o1 = this.getRandomCoordinates();
               MobBase spawned = this.mob.newMob(o1.getX(), o1.getY(), validTypes.get(Utilities.randomInt(validTypes.size())), true);
               spawned.interuptAndAttack(m, o);
            }

            this.damageMonsterSpawnsDelayTick = 0;
         }

         this.decreaseHitPoints(amount);
         if (this.hitPoints <= 0) {
            String objectName = this.getFormattedName();
            if (objectName.length() > 24) {
               objectName = objectName.substring(0, 23);
            }

            if (m != null) {
               m.increaseKills(1, objectName);
            }

            if (o != null) {
               o.increaseKills(1, objectName);
            }

            if (this.objectFlags.getMobGroup() == MobBase.MobGroup.MONSTER) {
               this.goal.incrementGoal(GoalModule.GoalType.FIGHT_THE_CORRUPTION);
            }

            MapTilesLoader.TileSet baseType = this.objectFlags.getBaseType();
            if (this.objectFlags.getMobGroup() == MobBase.MobGroup.VILLAGER
               && baseType.getTileSetTemplate().getType() == MapTilesLoader.TileSetType.WALL
               && baseType != MapTilesLoader.TileSet.GOD_WALL) {
               this.goal.incrementGoal(GoalModule.GoalType.CRUMBLING_DEFENSES);
            }

            if (baseType == MapTilesLoader.TileSet.CULLIS_GATE && this.instabilityAmount > 0) {
               this.goal.incrementGoal(GoalModule.GoalType.EXPLOSIVE_SIDE_EFFECTS);
            }

            this.destroyObject(true);
         } else {
            if (type == MobBase.DamageType.FIRE || type == MobBase.DamageType.MAGIC_FIRE) {
               for (int i = 0; i < amount / 4 + 1 && this.smokeDamageArray.size() < 30; i++) {
                  if (Utilities.randomInt(2) == 0) {
                     int randomX = Utilities.randomInt(width * 16);
                     int randomY = Utilities.randomInt(height * 16);
                     this.smokeDamageArray.add(new OrderedPair(randomX, randomY));
                  }
               }

               for (int ix = 0; ix < amount / 9 + 1 && this.fireDamageArray.size() < 20; ix++) {
                  if (Utilities.randomInt(2) == 0) {
                     int randomX = Utilities.randomInt(width * 16);
                     int randomY = Utilities.randomInt(height * 16);
                     this.fireDamageArray.add(new OrderedPair(randomX, randomY));
                  }
               }
            }
         }
      }
   }

   public void destroyObject(boolean createDebris) throws SlickException {
      if (!this.destroyed) {
         this.object.flagObjectForDestructionAndRemoval(this);
         this.removeAllMobs();
         this.discharge();
         this.resource.deleteFarmland(this.localFarmland);
         this.localFarmland.clear();
         this.courierGolemParking.clear();
         this.prayPoints.clear();
         this.vesselPoints.clear();
         int height = this.objectFlags.getHeight();
         int width = this.objectFlags.getWidth();
         if (this.objectFlags.getMobGroup() == MobBase.MobGroup.VILLAGER) {
            int shakeAmount = (height + width) / 2;
            if (shakeAmount > 5) {
               shakeAmount = 5;
            }

            if (shakeAmount <= 2) {
               shakeAmount = 0;
            }

            if (shakeAmount > 0) {
               this.map.initSreenShake(shakeAmount);
            }
         }

         if (createDebris) {
            int smokeAmount = width * height;

            for (int x = 0; x < smokeAmount; x++) {
               int randomX = Utilities.randomInt(width * 16);
               int randomY = Utilities.randomInt(height * 16);
               this.projectile.newDestruction(this.objectX * 16 + randomX, this.objectY * 16 + randomY);
            }

            for (int x = 0; x < smokeAmount * 40; x++) {
               int randomX = Utilities.randomInt(width * 16);
               int randomY = Utilities.randomInt(height * 16);
               this.particle
                  .newParticleGroup(
                     this.objectX * 16 + randomX,
                     this.objectY * 16 + randomY,
                     ParticleModule.ParticleGroup.SMOKE_BELLOW,
                     ParticleModule.ParticleSet.BLACK,
                     ParticleModule.ParticleSet.WHITE
                  );
            }

            this.projectile
               .newExpandingFireRing(this.getCenterPixelX(), this.getCenterPixelY(), width * height * 16, 4.0F, ParticleModule.ParticleSet.FIRE_RED);
         }

         ResourceModule.ResourceType[] var19;
         for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
            if (this.resources.get(type).size() > 0) {
               for (int x = this.resources.get(type).size() - 1; x >= 0; x--) {
                  ResourceBase r = this.resources.get(type).get(x);
                  r.unassign();
                  OrderedPair o = this.getRandomCoordinates(true);
                  OrderedPair o2 = this.resource.findOpenSpace(o.getX(), o.getY(), MapData.BlockMapGroup.STANDARD);
                  if (Utilities.randomInt(2) == 0 || r.getType().getTemplate().alwaysReturnResource()) {
                     this.resource.setResourcesOnGround(o2.getX(), o2.getY(), r);
                  } else if (r.getType().getTemplate().getTrashResource() != null) {
                     this.resource.deleteResource(r);
                     this.resource.createResourceOnGround(r.getType().getTemplate().getTrashResource(), r.getColor(), o2.getX(), o2.getY());
                  } else {
                     this.resource.deleteResource(r);
                  }
               }
            }

            if (createDebris) {
               int diminishingReturns = 5;
               if (this.resourceValues.get(type) > 0) {
                  for (int xx = 0; xx < this.resourceValues.get(type); xx++) {
                     if (Utilities.randomInt(diminishingReturns) == 0) {
                        diminishingReturns++;
                        OrderedPair o = this.getRandomCoordinates(true);
                        OrderedPair o2 = this.resource.findOpenSpace(o.getX(), o.getY(), MapData.BlockMapGroup.STANDARD);
                        if (Utilities.randomInt(5) != 0 && !type.getTemplate().alwaysReturnResource()) {
                           this.resource
                              .createResourceOnGround(
                                 type.getTemplate().getTrashResource(), this.resourceValuesColors.get(type).remove(0), o2.getX(), o2.getY()
                              );
                        } else {
                           this.resource.createResourceOnGround(type, this.resourceValuesColors.get(type).remove(0), o2.getX(), o2.getY());
                        }
                     }
                  }
               }
            }
         }

         if (createDebris) {
            Text.setVariableText("buildingName", this.getFormattedName());
            Console.out(Text.getText("console.panel.object.destroyed"), false);
            this.stats.increaseBuildingsDestroyedThisPeriod();
         }
      }
   }

   public void repair(int amount) throws SlickException {
      this.increaseHitPoints(amount);
      if (this.fireDamageArray.size() > 0) {
         this.fireDamageArray.remove(Utilities.randomInt(this.fireDamageArray.size()));
      }

      if (this.smokeDamageArray.size() > 0) {
         this.smokeDamageArray.remove(Utilities.randomInt(this.smokeDamageArray.size()));
      }

      if (this.hitPoints >= this.hitPointsMax) {
         this.fireDamageArray.clear();
         this.smokeDamageArray.clear();
         if (this.objectFlags.getMobGroup() == MobBase.MobGroup.VILLAGER) {
            Text.setVariableText("buildingName", this.getFormattedName());
            Console.out(Text.getText("console.panel.object.repaired"), false);
         }
      }
   }

   public void putOutFire() throws SlickException {
      this.fireDamageArray.clear();
      this.smokeDamageArray.clear();
   }

   public ArrayList<OrderedPair> getSmokeDamageArray() {
      return this.smokeDamageArray;
   }

   public ArrayList<OrderedPair> getFireDamageArray() {
      return this.fireDamageArray;
   }

   public void increaseDirtyWater(int amount) throws SlickException {
      boolean foundResource = false;

      for (ResourceBase r : this.resources.get(ResourceModule.ResourceType.DIRTY_WATER_BUCKET)) {
         if (r.getUses() < r.getType().getTemplate().getUsesMax()) {
            r.increaseUses(amount);
            foundResource = true;
            break;
         }
      }

      if (!foundResource && this.getAcceptStorageResourceCount(ResourceModule.ResourceType.DIRTY_WATER_BUCKET, -1) > 0) {
         ResourceBase refinedResource = this.resource.createRefinedResource(ResourceModule.ResourceType.DIRTY_WATER_BUCKET, false, -1);
         refinedResource.placeInBuilding(this);
         refinedResource.setUses(1);
         this.increaseStores(refinedResource);
      }

      this.updateObject();
   }

   public void decreaseDirtyWater(int amount) throws SlickException {
      while (amount > 0 && this.resources.get(ResourceModule.ResourceType.DIRTY_WATER_BUCKET).size() > 0) {
         ResourceBase bucket = this.resources
            .get(ResourceModule.ResourceType.DIRTY_WATER_BUCKET)
            .get(this.resources.get(ResourceModule.ResourceType.DIRTY_WATER_BUCKET).size() - 1);
         if (bucket.getUses() >= amount) {
            bucket.decreaseUses(amount);
            amount = 0;
         } else {
            int uses = bucket.getUses();
            bucket.decreaseUses(amount);
            amount -= uses;
         }

         this.updateObject();
      }
   }

   public void increaseWater(int amount) throws SlickException {
      boolean foundResource = false;

      for (ResourceBase r : this.resources.get(ResourceModule.ResourceType.WATER_BUCKET)) {
         if (r.getUses() < r.getType().getTemplate().getUsesMax()) {
            r.increaseUses(amount);
            foundResource = true;
            break;
         }
      }

      if (!foundResource && this.getAcceptStorageResourceCount(ResourceModule.ResourceType.WATER_BUCKET, -1) > 0) {
         ResourceBase refinedResource = this.resource.createRefinedResource(ResourceModule.ResourceType.WATER_BUCKET, false, -1);
         refinedResource.placeInBuilding(this);
         refinedResource.setUses(1);
         this.increaseStores(refinedResource);
      }

      this.updateObject();
   }

   public void decreaseWater(int amount) throws SlickException {
      while (amount > 0 && this.resources.get(ResourceModule.ResourceType.WATER_BUCKET).size() > 0) {
         ResourceBase bucket = this.resources
            .get(ResourceModule.ResourceType.WATER_BUCKET)
            .get(this.resources.get(ResourceModule.ResourceType.WATER_BUCKET).size() - 1);
         if (bucket.getUses() >= amount) {
            bucket.decreaseUses(amount);
            amount = 0;
         } else {
            int uses = bucket.getUses();
            bucket.decreaseUses(amount);
            amount -= uses;
         }

         this.updateObject();
      }
   }

   public ResourceBase build(ResourceBase r, boolean allowTrash, int trashChance) throws SlickException {
      if (this.resources.get(r.getType()).contains(r)) {
         this.resourceValues.put(r.getType(), this.resourceValues.get(r.getType()) + 1);
         this.resourceValuesColors.get(r.getType()).add(r.getColor());
         this.resource.deleteResource(r);
         this.font
            .newFloater("iconWorkers", "+1", (this.objectX + this.objectFlags.getWidth() / 2) * 16, (this.objectY + this.objectFlags.getHeight() / 2) * 16);
         this.updateObject();
         if (allowTrash
            && Utilities.randomInt(trashChance) == 0
            && r.getType().getTemplate().getTrashResource() != null
            && !r.getType().getTemplate().alwaysReturnResource()) {
            return this.resource.createResourceUnassigned(r.getType().getTemplate().getTrashResource(), r.getColor());
         }
      } else {
         System.out.println("Worker tried to build a building with resources that it did not have.");
      }

      return null;
   }

   public ResourceBase dismantle(ResourceModule.ResourceType type, boolean allowTrash, int trashChance) throws SlickException {
      this.resourceValues.put(type, this.resourceValues.get(type) - 1);
      ResourceBase returnedResource;
      if (allowTrash && Utilities.randomInt(trashChance) == 0 && type.getTemplate().getTrashResource() != null && !type.getTemplate().alwaysReturnResource()) {
         returnedResource = this.resource.createResourceUnassigned(type.getTemplate().getTrashResource(), this.resourceValuesColors.get(type).remove(0));
      } else {
         returnedResource = this.resource.createResourceUnassigned(type, this.resourceValuesColors.get(type).remove(0));
      }

      this.font.newFloater("iconWorkers", (this.objectX + this.objectFlags.getWidth() / 2) * 16, (this.objectY + this.objectFlags.getHeight() / 2) * 16);
      this.updateObject();
      return returnedResource;
   }

   public int getResourceCount(ResourceModule.ResourceType type, boolean includeEnroute) {
      if (!includeEnroute) {
         return this.getResourceCount(type);
      } else {
         int enrouteCount = 0;

         for (MobBase m : this.mob.getMobArray()) {
            if (m.getAssignment() != null
               && (m.getAssignment().getPrimaryObject() == this || m.getAssignment().getSecondaryObject() == this)
               && (
                  m.getAssignment().getAssignmentType() == Assignment.AssignmentType.DELIVERY
                     || m.getAssignment().getAssignmentType() == Assignment.AssignmentType.PICK_UP_RESOURCE_FROM_GROUND
               )
               && m.getAssignment().getPickupResources().size() > 0
               && m.getAssignment().getFirstPickupResources().getType() == type) {
               enrouteCount += m.getAssignment().getPickupResources().size();
            }
         }

         return this.resources.get(type).size() + enrouteCount;
      }
   }

   public int getResourceCount(ResourceModule.ResourceType type) {
      return this.resources.get(type).size();
   }

   public int getTotalResourcesCount() {
      int totalResources = 0;

      ResourceModule.ResourceType[] var5;
      for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
         totalResources += this.getResourceCount(type);
      }

      return totalResources;
   }

   public ArrayList<ResourceBase> getStorageResources() {
      ArrayList<ResourceBase> allResources = new ArrayList<>();

      for (ArrayList<ResourceBase> t : this.resources.values()) {
         allResources.addAll(t);
      }

      return allResources;
   }

   public ArrayList<ResourceBase> getStorageResources(ResourceModule.ResourceType t) {
      return this.resources.get(t);
   }

   public int getAcceptBuildResourceCount(ResourceModule.ResourceType resourceType, int mobID) {
      if (this.objectFlags.canWorkWith(this.objectFlags.getCanBuildingBeBuilt(), resourceType)
         && this.getResourceValueCount(resourceType) + this.getResourceCount(resourceType) < this.objectFlags.getResourceValueBaseCount(resourceType)) {
         int enroute = this.getDropoffEnroute(resourceType, mobID);
         return this.objectFlags.getResourceValueBaseCount(resourceType)
            - (this.getResourceValueCount(resourceType) + this.getResourceCount(resourceType) + enroute);
      } else {
         return 0;
      }
   }

   public int getAcceptStorageResourceCount(ResourceModule.ResourceType resourceType, int mobID) {
      if (this.objectFlags.canWorkWith(this.objectFlags.getCanBuildingStore(), resourceType)
         && this.getResourceCount(resourceType) < this.objectFlags.getResourceStorageMax().get(resourceType)) {
         int enroute = this.getDropoffEnroute(resourceType, mobID);
         int total = this.getResourceCount(resourceType) + enroute;
         return this.objectFlags.getResourceStorageMax().get(resourceType) - total;
      } else {
         return 0;
      }
   }

   private int getDropoffEnroute(ResourceModule.ResourceType resourceType, int mobID) {
      int delivererCount = 0;

      for (MobBase m : this.mob.getMobsWithDeliveryAssignment(false)) {
         if (m.getAssignment() != null && m.getID() != mobID) {
            ArrayList<ResourceBase> pickupResources = m.getAssignment().getPickupResources();
            if (pickupResources.size() != 0 || m.hasCarriedResources()) {
               ObjectBase deliveryBuilding = m.getAssignment().getDeliveryObject();
               if (deliveryBuilding == this) {
                  Assignment.AssignmentType assignmentType = m.getAssignment().getAssignmentType();
                  if (assignmentType == Assignment.AssignmentType.DELIVERY || assignmentType == Assignment.AssignmentType.PICK_UP_RESOURCE_FROM_GROUND) {
                     if (pickupResources.size() > 0 && (resourceType == null || pickupResources.get(0).getType() == resourceType)) {
                        delivererCount += pickupResources.size();
                     }

                     if (m.hasCarriedResources() && (resourceType == null || m.getCarriedResourcesListType() == resourceType)) {
                        delivererCount += m.getCarriedResourcesList().size();
                     }
                  }

                  ResourceModule.ResourceType assignmentResourceType = m.getAssignment().getPickupResourceType();
                  if (assignmentType == Assignment.AssignmentType.REFINE_RESOURCE && assignmentResourceType == resourceType) {
                     delivererCount++;
                  }
               }
            }
         }
      }

      return delivererCount;
   }

   public boolean canAcceptRefineOrProcessResource(ResourceModule.ResourceType resourceType, int mobID) {
      int refineCount = 0;
      ArrayList<MobBase> mobArray = this.mob.getMobsByAssignment(Assignment.AssignmentType.REFINE_RESOURCE, false);
      mobArray.addAll(this.mob.getMobsByAssignment(Assignment.AssignmentType.PROCESS_RESOURCE, false));
      mobArray.addAll(this.mob.getMobsByAssignment(Assignment.AssignmentType.DELIVERY, false));
      mobArray.addAll(this.mob.getMobsByAssignment(Assignment.AssignmentType.PICK_UP_RESOURCE_FROM_GROUND, false));

      for (MobBase m : mobArray) {
         if (m.getAssignment() != null && m.getID() != mobID) {
            Assignment assignment = m.getAssignment();
            ObjectBase primaryBuilding = assignment.getPrimaryObject();
            ObjectBase secondaryBuilding = assignment.getSecondaryObject();
            Assignment.AssignmentType assignmentType = assignment.getAssignmentType();
            ResourceModule.ResourceType assignmentResourceType = assignment.getPickupResourceType();
            if (primaryBuilding == this || secondaryBuilding == this) {
               if (assignmentType == Assignment.AssignmentType.DELIVERY || assignmentType == Assignment.AssignmentType.PICK_UP_RESOURCE_FROM_GROUND) {
                  ArrayList<ResourceBase> pickupResources = m.getAssignment().getPickupResources();
                  if (pickupResources.size() > 0 && pickupResources.get(0).getType() == resourceType) {
                     refineCount += pickupResources.size();
                  }

                  if (m.hasCarriedResources() && m.getCarriedResourcesListType() == resourceType) {
                     refineCount += m.getCarriedResourcesList().size();
                  }
               }

               if ((assignmentType == Assignment.AssignmentType.REFINE_RESOURCE || assignmentType == Assignment.AssignmentType.PROCESS_RESOURCE)
                  && assignmentResourceType == resourceType) {
                  refineCount++;
               }
            }
         }
      }

      int total = this.getResourceCount(resourceType) + refineCount;
      return total < this.objectFlags.getResourceStorageMax().get(resourceType);
   }

   public boolean isResourceAvailable(ResourceModule.ResourceType resourceType) {
      if (this.getResourceCount(resourceType) <= 0) {
         return false;
      } else {
         for (ResourceBase r : this.resources.get(resourceType)) {
            if (!r.isAssigned()) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean isFullResourceAvailable(ResourceModule.ResourceType resourceType) {
      if (this.getResourceCount(resourceType) <= 0) {
         return false;
      } else {
         for (ResourceBase r : this.resources.get(resourceType)) {
            if (!r.isAssigned() && r.getUses() == r.getUsesMax()) {
               return true;
            }
         }

         return false;
      }
   }

   public ResourceBase getAvailableResource(ResourceModule.ResourceType type) {
      for (ResourceBase r : this.resources.get(type)) {
         if (!r.isAssigned()) {
            return r;
         }
      }

      System.out.println("The game tried to pickup a " + type.getTemplate().getName() + " from " + this.objectFlags.getName() + " and none was available.");
      return null;
   }

   public int getAvailableResourceCount(ResourceModule.ResourceType type, boolean countFullOnly) {
      int count = 0;

      for (ResourceBase r : this.resources.get(type)) {
         if (!r.isAssigned() && (!countFullOnly || r.getUses() == r.getUsesMax())) {
            count++;
         }
      }

      return count;
   }

   public void setInRange(boolean b) {
      this.inRange = b;
   }

   public boolean isInRange() {
      return this.inRange;
   }

   public boolean isOnCorruption() {
      OrderedPair o = this.getCenterCoordinates();
      return this.corruption.isTileCorrupted(o.getX(), o.getY());
   }

   public boolean isBuildingAtLocation(int tileX, int tileY) {
      return tileX >= this.objectX
         && tileX < this.objectX + this.objectFlags.getWidth()
         && tileY >= this.objectY
         && tileY < this.objectY + this.objectFlags.getHeight();
   }

   public boolean canNotBeAttacked() {
      return this.destroyed || this.objectFlags.canNotBeAttacked() || this.objectFlags.isInvulnerable() || this.hitPointsMax == 0;
   }

   public void discharge() throws SlickException {
      while (this.energy > 0) {
         int randomEnergy = Utilities.randomInt(10) + 1;

         while (randomEnergy > this.energy) {
            randomEnergy = Utilities.randomInt(10) + 1;
         }

         if (this.objectFlags.getEssenceReturnCoordinates() != null) {
            this.essence.convertObjectEnergyToEssence(this.getEssenceReturnXOnMapExactPixel(), this.getEssenceReturnYOnMapExactPixel(), randomEnergy, false);
         } else {
            this.essence.convertObjectEnergyToEssence(this.getCenterCoordinateX(), this.getCenterCoordinateY(), randomEnergy, false);
         }

         this.energy -= randomEnergy * 2;
      }

      this.energy = 0;
   }

   public OrderedPair getRandomCoordinates() {
      return this.getRandomCoordinates(false);
   }

   public OrderedPair getRandomCoordinates(boolean ignoreBlockMap) {
      int height = this.objectFlags.getHeight();
      int width = this.objectFlags.getWidth();
      int buildX = this.objectX + Utilities.randomInt(width + 2) - 1;
      int buildY = this.objectY + Utilities.randomInt(height + 2) - 1;

      for (byte[][] blockMap = this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD);
         blockMap[buildX][buildY] > 0 && !ignoreBlockMap;
         buildY = this.objectY + Utilities.randomInt(height + 2) - 1
      ) {
         buildX = this.objectX + Utilities.randomInt(width + 2) - 1;
      }

      return OrderedPair.getOrderedPair(buildX, buildY);
   }

   public ArrayList<OrderedPair> getAllCoordinates() {
      ArrayList<OrderedPair> coordinates = new ArrayList<>();
      int height = this.objectFlags.getHeight();
      int width = this.objectFlags.getWidth();
      if (height > 1 && width > 1) {
         for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
               coordinates.add(OrderedPair.getOrderedPair(this.objectX + x, this.objectY + y));
            }
         }
      } else {
         coordinates.add(OrderedPair.getOrderedPair(this.objectX, this.objectY));
      }

      return coordinates;
   }

   public OrderedPair getRandomCoordinates(MobBase mob, boolean checkReachMap, boolean checkRangeMap) {
      ArrayList<OrderedPair> preferedCoordinates = new ArrayList<>();
      boolean checkPrefered = true;
      byte[][] blockMap = this.map.getMapData().getBlockMap(mob.getMobFlags().getBlockMapGroup());
      int height = this.objectFlags.getHeight();
      int width = this.objectFlags.getWidth();

      while (preferedCoordinates.size() == 0) {
         if (checkPrefered) {
            if (height > 1 && width > 1) {
               for (int x = 1; x < width - 1; x++) {
                  for (int y = 1; y < height - 1; y++) {
                     if (blockMap[this.objectX + x][this.objectY + y] == 0
                        && (
                           blockMap[this.objectX + x + 1][this.objectY + y] > 0
                              || blockMap[this.objectX + x - 1][this.objectY + y] > 0
                              || blockMap[this.objectX + x][this.objectY + y + 1] > 0
                              || blockMap[this.objectX + x][this.objectY + y - 1] > 0
                        )
                        && (!checkReachMap || mob.canReach(this.objectX + x, this.objectY + y))
                        && (!checkRangeMap || this.object.isInRange(this.objectX + x, this.objectY + y))) {
                        preferedCoordinates.add(OrderedPair.getOrderedPair(this.objectX + x, this.objectY + y));
                     }
                  }
               }
            }

            if (preferedCoordinates.size() == 0) {
               for (int x = 0; x < width; x++) {
                  for (int yx = 0; yx < height; yx++) {
                     if (blockMap[this.objectX + x][this.objectY + yx] == 0
                        && (
                           blockMap[this.objectX + x + 1][this.objectY + yx] > 0 && x != width - 1
                              || blockMap[this.objectX + x - 1][this.objectY + yx] > 0 && x != 0
                              || blockMap[this.objectX + x][this.objectY + yx + 1] > 0 && yx != height - 1
                              || blockMap[this.objectX + x][this.objectY + yx - 1] > 0 && yx != 0
                        )
                        && (!checkReachMap || mob.canReach(this.objectX + x, this.objectY + yx))
                        && (!checkRangeMap || this.object.isInRange(this.objectX + x, this.objectY + yx))) {
                        preferedCoordinates.add(OrderedPair.getOrderedPair(this.objectX + x, this.objectY + yx));
                     }
                  }
               }
            }
         } else {
            for (int x = 0; x < width; x++) {
               for (int yxx = 0; yxx < height; yxx++) {
                  if (blockMap[this.objectX + x][this.objectY + yxx] == 0
                     && (!checkReachMap || mob.canReach(this.objectX + x, this.objectY + yxx))
                     && (!checkRangeMap || this.object.isInRange(this.objectX + x, this.objectY + yxx))) {
                     preferedCoordinates.add(OrderedPair.getOrderedPair(this.objectX + x, this.objectY + yxx));
                  }
               }
            }

            if (preferedCoordinates.size() == 0) {
               for (int x = -1; x < width + 1; x++) {
                  for (int yxxx = -1; yxxx < height + 1; yxxx++) {
                     if (blockMap[this.objectX + x][this.objectY + yxxx] == 0
                        && (!checkReachMap || mob.canReach(this.objectX + x, this.objectY + yxxx))
                        && (!checkRangeMap || this.object.isInRange(this.objectX + x, this.objectY + yxxx))) {
                        preferedCoordinates.add(OrderedPair.getOrderedPair(this.objectX + x, this.objectY + yxxx));
                     }
                  }
               }
            }
         }

         if (preferedCoordinates.size() == 0) {
            if (!checkPrefered) {
               return null;
            }

            checkPrefered = false;
         }
      }

      return preferedCoordinates.get(Utilities.randomInt(preferedCoordinates.size()));
   }

   public OrderedPair getTopLeftCoordinate() {
      return OrderedPair.getOrderedPair(this.objectX, this.objectY);
   }

   public OrderedPair getTopRightCoordinate() {
      return OrderedPair.getOrderedPair(this.objectX + this.objectFlags.getWidth() - 1, this.objectY);
   }

   public OrderedPair getBottomLeftCoordinate() {
      return OrderedPair.getOrderedPair(this.objectX, this.objectY + this.objectFlags.getHeight() - 1);
   }

   public OrderedPair getBottomRightCoordinate() {
      return OrderedPair.getOrderedPair(this.objectX + this.objectFlags.getWidth() - 1, this.objectY + this.objectFlags.getHeight() - 1);
   }

   public int getCenterCoordinateX() {
      return this.objectX + this.objectFlags.getWidth() / 2;
   }

   public int getCenterCoordinateY() {
      return this.objectY + this.objectFlags.getHeight() / 2;
   }

   public OrderedPair getCenterCoordinates() {
      return OrderedPair.getOrderedPair(this.getCenterCoordinateX(), this.getCenterCoordinateY());
   }

   public int getCenterPixelX() {
      return (this.objectX + this.objectFlags.getWidth() / 2) * 16 + 8;
   }

   public int getCenterPixelY() {
      return (this.objectY + this.objectFlags.getHeight() / 2) * 16 + 8;
   }

   public void loadSync() {
      if (this.occupantsIDs != null) {
         for (int e = 0; e < this.occupantsIDs.length; e++) {
            this.occupants.add(this.mob.getMobHashMap().get(this.occupantsIDs[e]));
         }
      }

      if (this.combobulatorSpawnsIDs != null) {
         for (int e = 0; e < this.combobulatorSpawnsIDs.length; e++) {
            this.combobulatorSpawns.add(this.mob.getMobHashMap().get(this.combobulatorSpawnsIDs[e]));
         }
      }

      if (this.localFarmlandIDs != null) {
         for (int f = 0; f < this.localFarmlandIDs.length; f++) {
            this.localFarmland.add(this.resource.getFarmland(this.localFarmlandIDs[f]));
         }
      }

      ResourceModule.ResourceType[] var4;
      for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
         if (this.resourcesIDs.get(type) != null) {
            for (int r = 0; r < ((int[])this.resourcesIDs.get(type)).length; r++) {
               this.resources.get(type).add(this.resource.getResource(this.resourcesIDs.get(type)[r]));
            }
         }
      }

      if (this.tower != null) {
         this.tower.loadSync();
      }
   }

   public void sacrificeToCullisGate(MobBase m) throws SlickException {
      this.sacrificeToCullisGate(null, m);
   }

   public void sacrificeToCullisGate(ResourceBase r) throws SlickException {
      this.sacrificeToCullisGate(r, null);
   }

   private void sacrificeToCullisGate(ResourceBase r, MobBase m) throws SlickException {
      int posX = 0;
      int posY = 0;
      int essenceAmount = 0;
      int instability = 0;
      if (r != null) {
         essenceAmount = 40;
         posX = r.getCenterPixelX(true);
         posY = r.getCenterPixelY(true);
         instability = 251 + Utilities.randomInt(250);
         this.resource.deleteResource(r);
         this.god.increaseGodXP(0.25, GodModule.GodXPType.THROW_RESOURCES_IN_CULLIS_GATE);
         this.mob.tryPanicInArea(this.getFunctionalXOnMap(), this.getFunctionalYOnMap(), 8, true, 1);
         this.mob.influenceFaithInArea(this.getFunctionalXOnMap(), this.getFunctionalYOnMap(), 8, 1, true);
         this.sound.playSound(SoundModule.SoundType.STORE_ITEM, posX, posY);
      } else if (m != null) {
         essenceAmount = (m.getLevel() * m.getMobFlags().getExperienceMultiplier() + m.getExperience()) / 25;
         essenceAmount *= 4;
         posX = m.getCenterPixelX(true);
         posY = m.getCenterPixelY(true);
         instability = m.getLevel() * (251 + Utilities.randomInt(250));
         if (instability > 2000) {
            instability = 2000;
         }

         m.unassignFromAll(false);
         m.interruptNode(m.getAIStart());
         this.mob.deleteMob(m);
         this.god.increaseGodXP(1.0, GodModule.GodXPType.THROW_CREATURES_IN_CULLIS_GATE);
         if (m.getMobGroup() == MobBase.MobGroup.MONSTER) {
            this.mob.influenceFaithInArea(this.getFunctionalXOnMap(), this.getFunctionalYOnMap(), 16, 2, 4, true);
         }

         if (m.getMobFlags().getMobSoundSet(m.getGender()) == SoundModule.SoundSet.VILLAGER_MALE && Utilities.randomInt(20) == 1) {
            this.sound.playSound(SoundModule.SoundType.WILHELM, posX, posY);
         } else {
            SoundModule.SoundType deathSound = m.getMobFlags().getMobSoundSet(m.getGender()).getSoundDeath();
            if (deathSound != null) {
               this.sound.playSound(deathSound, posX, posY);
            }
         }

         if (m.getMobGroup() == MobBase.MobGroup.VILLAGER) {
            this.sound.playSound(SoundModule.SoundType.VILLAGER_DEATH);
            Text.setVariableText("mobName", m.getFormattedName(true));
            if (m.getGender() == MobBase.MobGender.MALE) {
               Console.newBanner(
                  Text.getText("console.banner.object.sacrificeToCullisGate.header.male"), Text.getText("console.banner.object.sacrificeToCullisGate.subtitle")
               );
            } else if (m.getGender() == MobBase.MobGender.FEMALE) {
               Console.newBanner(
                  Text.getText("console.banner.object.sacrificeToCullisGate.header.female"),
                  Text.getText("console.banner.object.sacrificeToCullisGate.subtitle")
               );
            } else {
               Console.newBanner(
                  Text.getText("console.banner.object.sacrificeToCullisGate.header.none"), Text.getText("console.banner.object.sacrificeToCullisGate.subtitle")
               );
            }

            this.mob.influenceFaithInArea(this.getFunctionalXOnMap(), this.getFunctionalYOnMap(), 16, 5, 10, false);
            this.goal.incrementGoal(GoalModule.GoalType.TYRANT);
            String mobTypeString = m.getMobType().getName().toLowerCase();
            if (mobTypeString.contains("nephilim")) {
               this.goal.incrementGoal(GoalModule.GoalType.A_WORTHY_SACRIFICE);
            }

            if (mobTypeString.contains("child")) {
               this.goal.incrementGoal(GoalModule.GoalType.OMG);
            }
         }

         if (m.getMobType() == MobBase.MobType.DOOFY_DOGGO || m.getMobType() == MobBase.MobType.WILD_DOOFY_DOGGO) {
            this.goal.incrementGoal(GoalModule.GoalType.WAR_CRIMES);
         }

         if (m.getMobGroup() == MobBase.MobGroup.DOMESTICATED_ANIMAL) {
            if (m.getMobType() == MobBase.MobType.DOGGO) {
               this.mob.influenceFaithInArea(this.getFunctionalXOnMap(), this.getFunctionalYOnMap(), 16, 10, 20, false);
               Text.setVariableText("mobName", m.getFormattedName(true));
               Console.newBanner(
                  Text.getText("console.banner.object.sacrificeToCullisGate.header.doggo"),
                  Text.getText("console.banner.object.sacrificeToCullisGate.subtitle.doggo")
               );
            } else if (m.getMobType() != MobBase.MobType.DOOFY_DOGGO && m.getMobType() != MobBase.MobType.WILD_DOOFY_DOGGO) {
               this.mob.influenceFaithInArea(this.getFunctionalXOnMap(), this.getFunctionalYOnMap(), 16, 2, 3, false);
            } else {
               this.mob.influenceFaithInArea(this.getFunctionalXOnMap(), this.getFunctionalYOnMap(), 16, 15, 25, false);
               this.weather.newDisaster(DisasterBase.DisasterType.EARTHQUAKE, this.getFunctionalXOnMap(), this.getFunctionalYOnMap(), true);
               this.weather.newDisaster(DisasterBase.DisasterType.ELECTRICAL_STORM, this.getFunctionalXOnMap(), this.getFunctionalYOnMap(), true);
               this.weather.newDisaster(DisasterBase.DisasterType.METEOR_SHOWER, this.getFunctionalXOnMap(), this.getFunctionalYOnMap(), true);
               Text.setVariableText("mobName", m.getFormattedName(true));
               Console.newBanner(
                  Text.getText("console.banner.object.sacrificeToCullisGate.header.doofyDoggo"),
                  Text.getText("console.banner.object.sacrificeToCullisGate.subtitle.doofyDoggo")
               );
            }
         }

         this.mob.tryPanicInArea(this.getFunctionalXOnMap(), this.getFunctionalYOnMap(), 16, true, 1);
      }

      this.sound.playSound(SoundModule.SoundType.DISSOLVE, posX, posY);

      while (essenceAmount > 0) {
         int randomEnergy = Utilities.randomInt(16) + 1;

         while (randomEnergy > essenceAmount) {
            randomEnergy = Utilities.randomInt(16) + 1;
         }

         this.essence.newEssence(posX, posY, randomEnergy, 1);
         essenceAmount -= randomEnergy;
      }

      this.projectile.newExpandingFireRing(posX, posY, 64, 2.0F, ParticleModule.ParticleSet.MAGIC_RED);
      this.projectile.newExpandingFireRing(posX, posY, 64, 4.0F, ParticleModule.ParticleSet.MAGIC_RED);

      for (int x = 0; x < 20; x++) {
         this.projectile.newExpandingEnergyTrail(posX, posY, ParticleModule.ParticleSet.FIRE_RED);
      }

      this.increaseCullisGateInstability(instability);
   }

   public static enum ObjectSubType {
      CONSTRUCTION,
      ABANDONED,
      BUILT,
      DISMANTLE;
   }

   public static enum ResourceStorePriority {
      VERY_HIGH,
      HIGH,
      MEDIUM,
      LOW,
      VERY_LOW;
   }
}
