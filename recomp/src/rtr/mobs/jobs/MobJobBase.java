package rtr.mobs.jobs;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import rtr.resources.ResourceModule;

public class MobJobBase {
   private static final int DEFAULT_WORK_SPEED = 2000;
   protected String mobJobName;
   protected String mobJobNamePlural;
   protected String description;
   protected Color baseColor;
   protected Color accentColor;
   protected int bonusDefensePercent;
   protected int bonusDamagePercent;
   protected boolean notCounted;
   protected boolean canRepair;
   protected boolean canRepairRoad;
   protected boolean canDismantle;
   protected boolean canClearTopography;
   protected boolean canClearRoads;
   protected boolean canDigHoles;
   protected boolean canProcessTrash;
   protected boolean canMakeTrashyCubes;
   protected boolean canPray;
   protected int harvestSpeedBase = 2000;
   protected int refineSpeedBase = 2000;
   protected int clearingSpeedBase = 2000;
   protected int diggingSpeedBase = 2000;
   protected int processingTrashSpeedBase = 2000;
   protected int buildingSpeedBase = 2000;
   protected int dismantlingSpeedBase = 2000;
   protected int repairSpeedBase = 2000;
   protected int medicSpeedBase = 2000;
   protected int prayingSpeedBase = 2000;
   protected int capturingSoulSpeedBase = 2000;
   protected int tendAnimalsBase = 2000;
   protected boolean isGuard;
   protected boolean canCaptureAnimals;
   protected boolean canCaptureSouls;
   protected boolean isMedic;
   protected boolean canTendAnimals;
   protected boolean canHalfRefineWork;
   protected ArrayList<ResourceModule.ResourceType> preferredTools = new ArrayList<>();
   protected ArrayList<ResourceModule.ResourceType> canWorkerSortEvenly = new ArrayList<>();
   protected ArrayList<ResourceModule.ResourceType> canWorkerSortClosest = new ArrayList<>();
   protected ArrayList<ResourceModule.ResourceType> canWorkerSortPriority = new ArrayList<>();
   protected ArrayList<ResourceModule.ResourceType> canWorkerDeliverConstruction = new ArrayList<>();
   protected ArrayList<ResourceModule.ResourceType> canWorkerDeliverRoadConstruction = new ArrayList<>();
   protected ArrayList<ResourceModule.ResourceType> canWorkerDeliverCourier = new ArrayList<>();
   protected ArrayList<ResourceModule.ResourceType> canWorkerDeliverCourierOnlyFull = new ArrayList<>();
   protected ArrayList<ResourceModule.ResourceType> canWorkerConstructionHarvest = new ArrayList<>();
   protected ArrayList<ResourceModule.ResourceType> canWorkerHarvest = new ArrayList<>();
   protected ArrayList<ResourceModule.ResourceType> canWorkerHarvestWorksiteOnly = new ArrayList<>();
   protected ArrayList<ResourceModule.ResourceType> canWorkerRefine = new ArrayList<>();
   protected ArrayList<ResourceModule.ResourceType> canWorkerSlaughter = new ArrayList<>();
   protected ArrayList<ResourceModule.ResourceType> canWorkerPlant = new ArrayList<>();
   protected ArrayList<ResourceModule.ResourceType> canWorkerStore = new ArrayList<>();
   protected ArrayList<ResourceModule.ResourceType> canWorkerStoreWorksiteOnly = new ArrayList<>();
   protected ArrayList<ResourceModule.ResourceType> canWorkerStoreTrashCanOnly = new ArrayList<>();
   protected ArrayList<ResourceModule.ResourceType> canWorkerRedistribute = new ArrayList<>();
   protected ArrayList<ResourceModule.ResourceType> canWorkerRedistributeOnlyFull = new ArrayList<>();
   protected ArrayList<ResourceModule.ResourceType> canWorkerBuild = new ArrayList<>();
   protected ArrayList<ResourceModule.ResourceType> canWorkerBuildRoad = new ArrayList<>();
   protected ArrayList<ResourceModule.ResourceType> canWorkerTrade = new ArrayList<>();

   public boolean canWorkerUseTools() {
      return this.canWorkWith(this.preferredTools);
   }

   public boolean canWorkerSortEvenly() {
      return this.canWorkWith(this.canWorkerSortEvenly);
   }

   public boolean canWorkerSortClosest() {
      return this.canWorkWith(this.canWorkerSortClosest);
   }

   public boolean canWorkerSortPriority() {
      return this.canWorkWith(this.canWorkerSortPriority);
   }

   public boolean canWorkerDeliverConstruction() {
      return this.canWorkWith(this.canWorkerDeliverConstruction);
   }

   public boolean canWorkerDeliverRoadConstruction() {
      return this.canWorkWith(this.canWorkerDeliverRoadConstruction);
   }

   public boolean canWorkerDeliverCourier() {
      return this.canWorkWith(this.canWorkerDeliverCourier);
   }

   public boolean canWorkerDeliverCourierOnlyFull() {
      return this.canWorkWith(this.canWorkerDeliverCourierOnlyFull);
   }

   public boolean canWorkerConstructionHarvest() {
      return this.canWorkWith(this.canWorkerConstructionHarvest);
   }

   public boolean canWorkerHarvest() {
      return this.canWorkWith(this.canWorkerHarvest);
   }

   public boolean canWorkerHarvestWorksiteOnly() {
      return this.canWorkWith(this.canWorkerHarvestWorksiteOnly);
   }

   public boolean canWorkerRefine() {
      return this.canWorkWith(this.canWorkerRefine);
   }

   public boolean canWorkerSlaughter() {
      return this.canWorkWith(this.canWorkerSlaughter);
   }

   public boolean canWorkerPlant() {
      return this.canWorkWith(this.canWorkerPlant);
   }

   public boolean canWorkerStore() {
      return this.canWorkWith(this.canWorkerStore);
   }

   public boolean canWorkerStoreWorksiteOnly() {
      return this.canWorkWith(this.canWorkerStoreWorksiteOnly);
   }

   public boolean canWorkerStoreTrashCanOnly() {
      return this.canWorkWith(this.canWorkerStoreTrashCanOnly);
   }

   public boolean canWorkerRedistribute() {
      return this.canWorkWith(this.canWorkerRedistribute);
   }

   public boolean canWorkerRedistributeOnlyFull() {
      return this.canWorkWith(this.canWorkerRedistributeOnlyFull);
   }

   public boolean canWorkerBuild() {
      return this.canWorkWith(this.canWorkerBuild);
   }

   public boolean canWorkerBuildRoad() {
      return this.canWorkWith(this.canWorkerBuildRoad);
   }

   public boolean canWorkerTrade() {
      return this.canWorkWith(this.canWorkerTrade);
   }

   public ArrayList<ResourceModule.ResourceType> getPreferredTools() {
      return this.preferredTools;
   }

   public ArrayList<ResourceModule.ResourceType> getCanWorkerSortEvenly() {
      return this.canWorkerSortEvenly;
   }

   public ArrayList<ResourceModule.ResourceType> getCanWorkerSortClosest() {
      return this.canWorkerSortClosest;
   }

   public ArrayList<ResourceModule.ResourceType> getCanWorkerSortPriority() {
      return this.canWorkerSortPriority;
   }

   public ArrayList<ResourceModule.ResourceType> getCanWorkerDeliverConstruction() {
      return this.canWorkerDeliverConstruction;
   }

   public ArrayList<ResourceModule.ResourceType> getCanWorkerDeliverRoadConstruction() {
      return this.canWorkerDeliverRoadConstruction;
   }

   public ArrayList<ResourceModule.ResourceType> getCanWorkerDeliverCourier() {
      return this.canWorkerDeliverCourier;
   }

   public ArrayList<ResourceModule.ResourceType> getCanWorkerDeliverCourierOnlyFull() {
      return this.canWorkerDeliverCourierOnlyFull;
   }

   public ArrayList<ResourceModule.ResourceType> getCanWorkerConstructionHarvest() {
      return this.canWorkerConstructionHarvest;
   }

   public ArrayList<ResourceModule.ResourceType> getCanWorkerHarvest() {
      return this.canWorkerHarvest;
   }

   public ArrayList<ResourceModule.ResourceType> getCanWorkerHarvestWorksiteOnly() {
      return this.canWorkerHarvestWorksiteOnly;
   }

   public ArrayList<ResourceModule.ResourceType> getCanWorkerRefine() {
      return this.canWorkerRefine;
   }

   public ArrayList<ResourceModule.ResourceType> getCanWorkerSlaughter() {
      return this.canWorkerSlaughter;
   }

   public ArrayList<ResourceModule.ResourceType> getCanWorkerPlant() {
      return this.canWorkerPlant;
   }

   public ArrayList<ResourceModule.ResourceType> getCanWorkerStore() {
      return this.canWorkerStore;
   }

   public ArrayList<ResourceModule.ResourceType> getCanWorkerStoreWorksiteOnly() {
      return this.canWorkerStoreWorksiteOnly;
   }

   public ArrayList<ResourceModule.ResourceType> getCanWorkerStoreTrashCanOnly() {
      return this.canWorkerStoreTrashCanOnly;
   }

   public ArrayList<ResourceModule.ResourceType> getCanWorkerRedistribute() {
      return this.canWorkerRedistribute;
   }

   public ArrayList<ResourceModule.ResourceType> getCanWorkerRedistributeOnlyFull() {
      return this.canWorkerRedistributeOnlyFull;
   }

   public ArrayList<ResourceModule.ResourceType> getCanWorkerBuild() {
      return this.canWorkerBuild;
   }

   public ArrayList<ResourceModule.ResourceType> getCanWorkerBuildRoad() {
      return this.canWorkerBuildRoad;
   }

   public ArrayList<ResourceModule.ResourceType> getCanWorkerTrade() {
      return this.canWorkerTrade;
   }

   public boolean canWorkerUseTool(ResourceModule.ResourceType type) {
      return this.canWorkWith(this.preferredTools, type);
   }

   public boolean canWorkerSortEvenly(ResourceModule.ResourceType type) {
      return this.canWorkWith(this.canWorkerSortEvenly, type);
   }

   public boolean canWorkerSortClosest(ResourceModule.ResourceType type) {
      return this.canWorkWith(this.canWorkerSortClosest, type);
   }

   public boolean canWorkerSortPriority(ResourceModule.ResourceType type) {
      return this.canWorkWith(this.canWorkerSortPriority, type);
   }

   public boolean canWorkerDeliverConstruction(ResourceModule.ResourceType type) {
      return this.canWorkWith(this.canWorkerDeliverConstruction, type);
   }

   public boolean canWorkerDeliverRoadConstruction(ResourceModule.ResourceType type) {
      return this.canWorkWith(this.canWorkerDeliverRoadConstruction, type);
   }

   public boolean canWorkerDeliverCourier(ResourceModule.ResourceType type) {
      return this.canWorkWith(this.canWorkerDeliverCourier, type);
   }

   public boolean canWorkerDeliverCourierOnlyFull(ResourceModule.ResourceType type) {
      return this.canWorkWith(this.canWorkerDeliverCourierOnlyFull, type);
   }

   public boolean canWorkerConstructionHarvest(ResourceModule.ResourceType type) {
      return this.canWorkWith(this.canWorkerConstructionHarvest, type);
   }

   public boolean canWorkerHarvest(ResourceModule.ResourceType type) {
      return this.canWorkWith(this.canWorkerHarvest, type);
   }

   public boolean canWorkerHarvestWorksiteOnly(ResourceModule.ResourceType type) {
      return this.canWorkWith(this.canWorkerHarvestWorksiteOnly, type);
   }

   public boolean canWorkerRefine(ResourceModule.ResourceType type) {
      return this.canWorkWith(this.canWorkerRefine, type);
   }

   public boolean canWorkerSlaughter(ResourceModule.ResourceType type) {
      return this.canWorkWith(this.canWorkerSlaughter, type);
   }

   public boolean canWorkerPlant(ResourceModule.ResourceType type) {
      return this.canWorkWith(this.canWorkerPlant, type);
   }

   public boolean canWorkerStore(ResourceModule.ResourceType type) {
      return this.canWorkWith(this.canWorkerStore, type);
   }

   public boolean canWorkerStoreWorksiteOnly(ResourceModule.ResourceType type) {
      return this.canWorkWith(this.canWorkerStoreWorksiteOnly, type);
   }

   public boolean canWorkerStoreTrashCanOnly(ResourceModule.ResourceType type) {
      return this.canWorkWith(this.canWorkerStoreTrashCanOnly, type);
   }

   public boolean canWorkerRedistribute(ResourceModule.ResourceType type) {
      return this.canWorkWith(this.canWorkerRedistribute, type);
   }

   public boolean canWorkerRedistributeOnlyFull(ResourceModule.ResourceType type) {
      return this.canWorkWith(this.canWorkerRedistributeOnlyFull, type);
   }

   public boolean canWorkerBuild(ResourceModule.ResourceType type) {
      return this.canWorkWith(this.canWorkerBuild, type);
   }

   public boolean canWorkerBuildRoad(ResourceModule.ResourceType type) {
      return this.canWorkWith(this.canWorkerBuildRoad, type);
   }

   public boolean canWorkerTrade(ResourceModule.ResourceType type) {
      return this.canWorkWith(this.canWorkerTrade, type);
   }

   private boolean canWorkWith(ArrayList<ResourceModule.ResourceType> resourceArray) {
      return resourceArray.size() > 0;
   }

   private boolean canWorkWith(ArrayList<ResourceModule.ResourceType> resourceArray, ResourceModule.ResourceType type) {
      return resourceArray.contains(type);
   }

   public String getMobJobName() {
      return this.mobJobName;
   }

   public String getMobJobNamePlural() {
      return this.mobJobNamePlural;
   }

   public String getDescription() {
      return this.description;
   }

   public Color getBaseColor() {
      return this.baseColor;
   }

   public Color getAccentColor() {
      return this.accentColor;
   }

   public int getBonusDefensePercent() {
      return this.bonusDefensePercent;
   }

   public int getBonusDamagePercent() {
      return this.bonusDamagePercent;
   }

   public boolean isNotCounted() {
      return this.notCounted;
   }

   public boolean canRepair() {
      return this.canRepair;
   }

   public boolean canRepairRoad() {
      return this.canRepairRoad;
   }

   public boolean canDismantle() {
      return this.canDismantle;
   }

   public boolean canClearTopography() {
      return this.canClearTopography;
   }

   public boolean canClearRoads() {
      return this.canClearRoads;
   }

   public boolean canDigHoles() {
      return this.canDigHoles;
   }

   public boolean canProcessTrash() {
      return this.canProcessTrash;
   }

   public boolean canMakeTrashyCubes() {
      return this.canMakeTrashyCubes;
   }

   public boolean canPray() {
      return this.canPray;
   }

   public int getHarvestSpeedBase() {
      return this.harvestSpeedBase;
   }

   public int getRefineSpeedBase() {
      return this.refineSpeedBase;
   }

   public int getClearingSpeedBase() {
      return this.clearingSpeedBase;
   }

   public int getDiggingSpeedBase() {
      return this.diggingSpeedBase;
   }

   public int getProcessingTrashSpeedBase() {
      return this.processingTrashSpeedBase;
   }

   public int getBuildingSpeedBase() {
      return this.buildingSpeedBase;
   }

   public int getDismantlingSpeedBase() {
      return this.dismantlingSpeedBase;
   }

   public int getRepairSpeedBase() {
      return this.repairSpeedBase;
   }

   public int getMedicSpeedBase() {
      return this.medicSpeedBase;
   }

   public int getPrayingSpeedBase() {
      return this.prayingSpeedBase;
   }

   public int getCapturingSoulSpeedBase() {
      return this.capturingSoulSpeedBase;
   }

   public int getTendAnimalsBase() {
      return this.tendAnimalsBase;
   }

   public boolean isGuard() {
      return this.isGuard;
   }

   public boolean canCaptureAnimals() {
      return this.canCaptureAnimals;
   }

   public boolean canCaptureSouls() {
      return this.canCaptureSouls;
   }

   public boolean isMedic() {
      return this.isMedic;
   }

   public boolean canTendAnimals() {
      return this.canTendAnimals;
   }

   public boolean canHalfRefineWork() {
      return this.canHalfRefineWork;
   }

   public static enum MobJobType {
      ARMORSMITH(new MobJobArmorsmith()),
      BOTTLER(new MobJobBottler()),
      BUILDER(new MobJobBuilder()),
      CARPENTER(new MobJobCarpenter()),
      COOK(new MobJobCook()),
      COURIER_SUPPLIER(new MobJobCourierSupplier()),
      CRYSILLER(new MobJobCrystiller()),
      CRYSTAL_HARVESTER(new MobJobCrystalHarvester()),
      CUBE_E_GOLEM(new MobJobCubeEGolem()),
      DOGGO(new MobJobDoggo()),
      FARMER(new MobJobFarmer()),
      FLETCHER(new MobJobFletcher()),
      LABOR_GOLEM(new MobJobLaborGolem()),
      LUMBERJACK(new MobJobLumberjack()),
      MAINTAINER(new MobJobMaintainer()),
      MEDIC(new MobJobMedic()),
      MINER(new MobJobMiner()),
      NONE(new MobJobNone()),
      OCCULTIST(new MobJobOccultist()),
      ORGANIZER(new MobJobOrganizer()),
      PROVISIONER(new MobJobProvisioner()),
      RANGER(new MobJobRanger()),
      SMELTER(new MobJobSmelter()),
      STONE_CUTTER(new MobJobStoneCutter()),
      TOOLSMITH(new MobJobToolsmith()),
      TRASHER(new MobJobTrasher()),
      TUMBLER(new MobJobTumbler()),
      WATER_MASTER(new MobJobWaterMaster()),
      WAY_MAKER(new MobJobWayMaker());

      private final MobJobBase jobBase;

      private MobJobType(MobJobBase j) {
         this.jobBase = j;
      }

      public MobJobBase getJob() {
         return this.jobBase;
      }
   }
}
