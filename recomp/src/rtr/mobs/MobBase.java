package rtr.mobs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
import rtr.TimeModule;
import rtr.console.Console;
import rtr.font.FontModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.lighting.Light;
import rtr.lighting.LightingModule;
import rtr.map.CollisionModule;
import rtr.map.DepthModule;
import rtr.map.MapData;
import rtr.map.MapModule;
import rtr.mobs.ai.AIAnimalWanderBehavior;
import rtr.mobs.ai.AIAttemptBreed;
import rtr.mobs.ai.AIAttemptWork;
import rtr.mobs.ai.AIBase;
import rtr.mobs.ai.AIBeingSlaughtered;
import rtr.mobs.ai.AIBirth;
import rtr.mobs.ai.AIBuildWork;
import rtr.mobs.ai.AIBuyFromProvisioner;
import rtr.mobs.ai.AIBuyLaborer;
import rtr.mobs.ai.AICaptureAnimals;
import rtr.mobs.ai.AICaptureAnimalsInHand;
import rtr.mobs.ai.AICaptureSoul;
import rtr.mobs.ai.AICaptureSoulInHand;
import rtr.mobs.ai.AIChat;
import rtr.mobs.ai.AIClearResourcesFromRoadWork;
import rtr.mobs.ai.AIClearTopographyFromRoadWork;
import rtr.mobs.ai.AIClearTopographyWork;
import rtr.mobs.ai.AICombatBuildings;
import rtr.mobs.ai.AICombatMobs;
import rtr.mobs.ai.AIConfused;
import rtr.mobs.ai.AIConstructionHarvestWork;
import rtr.mobs.ai.AICourierArriveBehavior;
import rtr.mobs.ai.AICourierDepartBehavior;
import rtr.mobs.ai.AICourierParkBehavior;
import rtr.mobs.ai.AICriticalRepairWork;
import rtr.mobs.ai.AIDeath;
import rtr.mobs.ai.AIDeliverConstructionResource;
import rtr.mobs.ai.AIDeliverConstructionResourceInHand;
import rtr.mobs.ai.AIDeliverCourierResource;
import rtr.mobs.ai.AIDeliverCourierResourceInHand;
import rtr.mobs.ai.AIDeliverRoadConstructionResource;
import rtr.mobs.ai.AIDeliverRoadConstructionResourceInHand;
import rtr.mobs.ai.AIDigWork;
import rtr.mobs.ai.AIDismantleWork;
import rtr.mobs.ai.AIEquipResource;
import rtr.mobs.ai.AIFindBetterHome;
import rtr.mobs.ai.AIFindFood;
import rtr.mobs.ai.AIFindFoodCheck;
import rtr.mobs.ai.AIFindFoodHarvest;
import rtr.mobs.ai.AIFindHome;
import rtr.mobs.ai.AIFindJob;
import rtr.mobs.ai.AIFindSpawn;
import rtr.mobs.ai.AIFindWaterCheck;
import rtr.mobs.ai.AIFindWaterFountain;
import rtr.mobs.ai.AIFindWaterHarvest;
import rtr.mobs.ai.AIFrozen;
import rtr.mobs.ai.AIGhostBehavior;
import rtr.mobs.ai.AIGoHome;
import rtr.mobs.ai.AIGolemGuardBehavior;
import rtr.mobs.ai.AIGolemHealBehavior;
import rtr.mobs.ai.AIGolemPatrolBehavior;
import rtr.mobs.ai.AIGrabbed;
import rtr.mobs.ai.AIGrowth;
import rtr.mobs.ai.AIHarvestFarmWork;
import rtr.mobs.ai.AIHarvestWaterWork;
import rtr.mobs.ai.AIHarvestWork;
import rtr.mobs.ai.AIHealthRepair;
import rtr.mobs.ai.AIInHome;
import rtr.mobs.ai.AIInHomeHiding;
import rtr.mobs.ai.AIIncapacitated;
import rtr.mobs.ai.AILaborerLeave;
import rtr.mobs.ai.AILaborerPost;
import rtr.mobs.ai.AIMakeTrashyCube;
import rtr.mobs.ai.AIMateCoitus;
import rtr.mobs.ai.AIMateGoCoitus;
import rtr.mobs.ai.AIMateGoCoitusOther;
import rtr.mobs.ai.AIMedicWork;
import rtr.mobs.ai.AIMigrantDepartBehavior;
import rtr.mobs.ai.AIMigrationPreperationBehavior;
import rtr.mobs.ai.AINomadBehavior;
import rtr.mobs.ai.AINomadBehaviorOther;
import rtr.mobs.ai.AIOnFirePanic;
import rtr.mobs.ai.AIPanic;
import rtr.mobs.ai.AIPatrolFrontLine;
import rtr.mobs.ai.AIPatrolVillage;
import rtr.mobs.ai.AIPlaceVessel;
import rtr.mobs.ai.AIPlaceVesselInHand;
import rtr.mobs.ai.AIPlantResource;
import rtr.mobs.ai.AIPlantResourceInHand;
import rtr.mobs.ai.AIPray;
import rtr.mobs.ai.AIProcessTrash;
import rtr.mobs.ai.AIProvisionerLeave;
import rtr.mobs.ai.AIProvisionerPost;
import rtr.mobs.ai.AIQuitJob;
import rtr.mobs.ai.AIRangerSummoned;
import rtr.mobs.ai.AIRedistributeResources;
import rtr.mobs.ai.AIRefineResource;
import rtr.mobs.ai.AIRefineWaterBottle;
import rtr.mobs.ai.AIRemoveRoadWork;
import rtr.mobs.ai.AIRepairRoadWork;
import rtr.mobs.ai.AIRepairWork;
import rtr.mobs.ai.AIRoadWork;
import rtr.mobs.ai.AISellResource;
import rtr.mobs.ai.AISellResourceInHand;
import rtr.mobs.ai.AISlaughterAnimal;
import rtr.mobs.ai.AISlaughterAnimalInHand;
import rtr.mobs.ai.AISleepOutside;
import rtr.mobs.ai.AISpawnRepair;
import rtr.mobs.ai.AIStart;
import rtr.mobs.ai.AIStoreResource;
import rtr.mobs.ai.AIStoreResourceInHand;
import rtr.mobs.ai.AIStoreTrashInTrashCan;
import rtr.mobs.ai.AIStoreTrashInTrashCanInHand;
import rtr.mobs.ai.AIStuck;
import rtr.mobs.ai.AITendFarmsWork;
import rtr.mobs.ai.AITerminateAnimalPregnancy;
import rtr.mobs.ai.AITrade;
import rtr.mobs.ai.AIVisitMate;
import rtr.mobs.ai.AIWander;
import rtr.mobs.ai.AIWildDoggoDomesticate;
import rtr.mobs.ai.AIWildDoggoWanderIn;
import rtr.mobs.ai.AIWildDoggoWanderInOther;
import rtr.mobs.ai.corruption.AIAttemptCorruptionWork;
import rtr.mobs.ai.corruption.AICorruptionBuildWork;
import rtr.mobs.ai.corruption.AICorruptionBuildingPlacement;
import rtr.mobs.ai.corruption.AICorruptionClearTopographyToBuildWork;
import rtr.mobs.ai.corruption.AICorruptionClearTopographyWork;
import rtr.mobs.ai.corruption.AICorruptionDeliverResourceInHand;
import rtr.mobs.ai.corruption.AICorruptionDeliverResourceOnGround;
import rtr.mobs.ai.corruption.AICorruptionDeliverRoadResourceInHand;
import rtr.mobs.ai.corruption.AICorruptionDeliverRoadResourceOnGround;
import rtr.mobs.ai.corruption.AICorruptionHarvestWork;
import rtr.mobs.ai.corruption.AICorruptionRoadWork;
import rtr.mobs.ai.corruption.AIHostileBehaviorAttack;
import rtr.mobs.ai.corruption.AIHostileBehaviorAttackOther;
import rtr.mobs.ai.corruption.AIHostileBehaviorLeave;
import rtr.mobs.essence.EssenceModule;
import rtr.mobs.jobs.MobJobBase;
import rtr.mobs.jobs.MobJobModule;
import rtr.mobs.spritesheet.MobSprite;
import rtr.objects.ObjectBase;
import rtr.objects.ObjectModule;
import rtr.particles.ParticleModule;
import rtr.particles.projectiles.ProjectileModule;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.road.RoadModule;
import rtr.save.SaveConversionUtilities;
import rtr.save.SaveModule;
import rtr.save.YMLDataMap;
import rtr.spawning.CorruptionModule;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.system.ScaleControl;
import rtr.system.gamemodetemplates.GameModeTemplateBase;
import rtr.utilities.Assignment;
import rtr.utilities.FastMath;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;
import rtr.weather.WeatherModule;

public class MobBase {
   protected MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   protected ParticleModule particle = (ParticleModule)StateBase.getModule(ModuleBase.ModuleType.PARTICLE);
   protected ProjectileModule projectile = (ProjectileModule)StateBase.getModule(ModuleBase.ModuleType.PROJECTILE);
   protected CollisionModule collision = (CollisionModule)StateBase.getModule(ModuleBase.ModuleType.COLLISION);
   protected DepthModule depth = (DepthModule)StateBase.getModule(ModuleBase.ModuleType.DEPTH);
   protected ResourceModule resource = (ResourceModule)StateBase.getModule(ModuleBase.ModuleType.RESOURCE);
   protected ObjectModule object = (ObjectModule)StateBase.getModule(ModuleBase.ModuleType.OBJECT);
   protected MobModule mob = (MobModule)StateBase.getModule(ModuleBase.ModuleType.MOB);
   protected MobJobModule mobJob = (MobJobModule)StateBase.getModule(ModuleBase.ModuleType.MOB_JOB);
   protected SoundModule sound = (SoundModule)StateBase.getModule(ModuleBase.ModuleType.SOUND);
   protected FontModule font = (FontModule)StateBase.getModule(ModuleBase.ModuleType.FONT);
   protected TimeModule time = (TimeModule)StateBase.getModule(ModuleBase.ModuleType.TIME);
   protected EssenceModule essence = (EssenceModule)StateBase.getModule(ModuleBase.ModuleType.ESSENCE);
   protected WeatherModule weather = (WeatherModule)StateBase.getModule(ModuleBase.ModuleType.WEATHER);
   protected LightingModule lighting = (LightingModule)StateBase.getModule(ModuleBase.ModuleType.LIGHTING);
   protected RoadModule road = (RoadModule)StateBase.getModule(ModuleBase.ModuleType.ROAD);
   protected CorruptionModule corruption = (CorruptionModule)StateBase.getModule(ModuleBase.ModuleType.CORRUPTION);
   protected SaveModule save = (SaveModule)StateBase.getModule(ModuleBase.ModuleType.SAVE);
   protected GodModule god = (GodModule)StateBase.getModule(ModuleBase.ModuleType.GOD);
   protected PerkModule perk = (PerkModule)StateBase.getModule(ModuleBase.ModuleType.PERK);
   protected GoalModule goal = (GoalModule)StateBase.getModule(ModuleBase.ModuleType.GOAL);
   protected String currentTask = "";
   protected AIBase activeNode;
   public ArrayList<AIBase> aiNodes = new ArrayList<>();
   private String activeNodeName;
   private YMLDataMap activeNodeData;
   protected AIStart aiStart;
   protected AIGrabbed aiGrabbed;
   protected AIChat aiChat;
   protected AICourierParkBehavior aiCourierParkBehavior;
   protected AICourierArriveBehavior aiCourierArriveBehavior;
   protected AICourierDepartBehavior aiCourierDepartBehavior;
   protected AIMigrationPreperationBehavior aiMigrationPreperationBehavior;
   protected AIMigrantDepartBehavior aiMigrantLeave;
   protected AITrade aiTrade;
   protected AIBirth aiBirth;
   protected AIGrowth aiGrowth;
   protected AIWander aiWander;
   protected AIConfused aiConfused;
   protected AIPanic aiPanic;
   protected AIDeath aiDeath;
   protected AIStuck aiStuck;
   protected AIFindHome aiFindHome;
   protected AIFindSpawn aiFindSpawn;
   protected AIFindBetterHome aiFindBetterHome;
   protected AIGoHome aiGoHome;
   protected AIEquipResource aiEquipResource;
   protected AIHealthRepair aiHealthRepair;
   protected AIFindFoodCheck aiFindFoodCheck;
   protected AIFindFood aiFindFood;
   protected AIFindFoodHarvest aiFindFoodHarvest;
   protected AIFindWaterCheck aiFindWaterCheck;
   protected AIFindWaterFountain aiFindWaterFountain;
   protected AIFindWaterHarvest aiFindWaterHarvest;
   protected AIInHome aiInHome;
   protected AIInHomeHiding aiInHomeHiding;
   protected AISleepOutside aiSleepOutside;
   protected AIIncapacitated aiIncapacitated;
   protected AICombatMobs aiCombatMobs;
   protected AICombatBuildings aiCombatBuildings;
   protected AIHostileBehaviorAttack aiHostileBehaviorAttack;
   protected AIHostileBehaviorAttackOther aiHostileBehaviorAttackOther;
   protected AIHostileBehaviorLeave aiHostileBehaviorLeave;
   protected AINomadBehavior aiNomadBehavior;
   protected AINomadBehaviorOther aiNomadBehaviorOther;
   protected AILaborerPost aiLaborerPost;
   protected AILaborerLeave aiLaborerLeave;
   protected AIProvisionerPost aiProvisionerPost;
   protected AIProvisionerLeave aiProvisionerLeave;
   protected AIBuyLaborer aiBuyLaborer;
   protected AIBuyFromProvisioner aiBuyFromProvisioner;
   protected AISellResourceInHand aiSellResourceInHand;
   protected AISellResource aiSellResource;
   protected AIAnimalWanderBehavior aiAnimalWanderBehavior;
   protected AIWildDoggoWanderIn aiWildDoggoWanderIn;
   protected AIWildDoggoWanderInOther aiWildDoggoWanderInOther;
   protected AIWildDoggoDomesticate aiWildDoggoDomesticate;
   protected AISpawnRepair aiSpawnRepair;
   protected AIGhostBehavior aiGhostBehavior;
   protected AIGolemHealBehavior aiGolemHealBehavior;
   protected AIGolemGuardBehavior aiGolemGuardBehavior;
   protected AIGolemPatrolBehavior aiGolemPatrolBehavior;
   protected AIMedicWork aiMedicWork;
   protected AIPatrolVillage aiPatrolVillage;
   protected AIPatrolFrontLine aiPatrolFrontLine;
   protected AICaptureSoul aiCaptureSoul;
   protected AICaptureSoulInHand aiCaptureSoulInHand;
   protected AIPlaceVessel aiPlaceVessel;
   protected AIPlaceVesselInHand aiPlaceVesselInHand;
   protected AIPray aiPray;
   protected AITerminateAnimalPregnancy aiTerminateAnimalPregnancy;
   protected AICaptureAnimals aiCaptureAnimals;
   protected AICaptureAnimalsInHand aiCaptureAnimalsInHand;
   protected AISlaughterAnimal aiSlaughterAnimal;
   protected AISlaughterAnimalInHand aiSlaughterAnimalInHand;
   protected AIBeingSlaughtered aiBeingSlaughtered;
   protected AIRangerSummoned aiRangerSummoned;
   protected AIOnFirePanic aiOnFirePanic;
   protected AIFrozen aiFrozen;
   protected AIVisitMate aiVisitMate;
   protected AIAttemptBreed aiAttemptBreed;
   protected AIAttemptCorruptionWork aiAttemptCorruptionWork;
   protected AICorruptionBuildingPlacement aiCorruptionBuildingPlacement;
   protected AICorruptionBuildWork aiCorruptionBuildWork;
   protected AICorruptionDeliverResourceInHand aiCorruptionDeliverResourceInHand;
   protected AICorruptionDeliverResourceOnGround aiCorruptionDeliverResourceOnGround;
   protected AICorruptionDeliverRoadResourceInHand aiCorruptionDeliverRoadResourceInHand;
   protected AICorruptionDeliverRoadResourceOnGround aiCorruptionDeliverRoadResourceOnGround;
   protected AICorruptionHarvestWork aiCorruptionHarvestWork;
   protected AICorruptionClearTopographyWork aiCorruptionClearTopographyWork;
   protected AICorruptionClearTopographyToBuildWork aiCorruptionClearTopographyToBuildWork;
   protected AICorruptionRoadWork aiCorruptionRoadWork;
   protected AIAttemptWork aiAttemptWork;
   protected AIFindJob aiFindJob;
   protected AIQuitJob aiQuitJob;
   protected AICriticalRepairWork aiCriticalRepairWork;
   protected AIRepairWork aiRepairWork;
   protected AIRepairRoadWork aiRepairRoadWork;
   protected AIDismantleWork aiDismantleWork;
   protected AIBuildWork aiBuildWork;
   protected AIClearTopographyWork aiClearTopographyWork;
   protected AIClearTopographyFromRoadWork aiClearTopographyFromRoadWork;
   protected AIClearResourcesFromRoadWork aiClearResourcesFromRoadWork;
   protected AIRemoveRoadWork aiRemoveRoadWork;
   protected AIDigWork aiDigWork;
   protected AIRoadWork aiRoadWork;
   protected AIConstructionHarvestWork aiConstructionHarvestWork;
   protected AIHarvestWork aiHarvestWork;
   protected AIHarvestFarmWork aiHarvestFarmWork;
   protected AIHarvestWaterWork aiHarvestWaterWork;
   protected AITendFarmsWork aiTendFarmsWork;
   protected AIPlantResourceInHand aiPlantResourceInHand;
   protected AIPlantResource aiPlantResource;
   protected AIDeliverConstructionResourceInHand aiDeliverConstructionResourceInHand;
   protected AIDeliverConstructionResource aiDeliverConstructionResource;
   protected AIDeliverCourierResourceInHand aiDeliverCourierResourceInHand;
   protected AIDeliverCourierResource aiDeliverCourierResource;
   protected AIDeliverRoadConstructionResourceInHand aiDeliverRoadConstructionResourceInHand;
   protected AIDeliverRoadConstructionResource aiDeliverRoadConstructionResource;
   protected AIStoreResourceInHand aiStoreResourceInHand;
   protected AIStoreResource aiAIStoreResource;
   protected AIStoreTrashInTrashCanInHand aiStoreTrashInTrashCanInHand;
   protected AIStoreTrashInTrashCan aiAIStoreTrashInTrashCan;
   protected AIMakeTrashyCube aiAIMakeTrashyCube;
   protected AIRedistributeResources aiRedistributeResources;
   protected AIRefineResource aiRefineResource;
   protected AIProcessTrash aiProcessTrash;
   protected AIRefineWaterBottle aiRefineWaterBottle;
   protected AIMateCoitus aiMateCoitus;
   protected AIMateGoCoitus aiMateGoCoitus;
   protected AIMateGoCoitusOther aiMateGoCoitusOther;
   protected int mobID;
   protected MobSprite sprite;
   protected Image mobRing = ImageLoader.getImage("res/mobs/mobRing.png");
   protected Image dropArrow = ImageLoader.getImage("res/resourceArrow.png");
   private float dropArrowBounce = 0.0F;
   private float dropArrowBounceSpeed = 0.0F;
   protected float ringAlpha;
   protected Image needsStarving = ImageLoader.getImage("res/GUI/icon/iconStarving.png");
   protected Image needsWater = ImageLoader.getImage("res/GUI/icon/iconWater.png");
   protected Image needsFaithLow = ImageLoader.getImage("res/GUI/icon/iconFaithLow.png");
   protected Image needsEnergy = ImageLoader.getImage("res/GUI/icon/iconEnergy.png");
   protected Image needsHomeless = ImageLoader.getImage("res/GUI/icon/iconHomeless.png");
   protected Image needsTempLow = ImageLoader.getImage("res/GUI/icon/iconTempLow.png");
   protected Image needsTempHigh = ImageLoader.getImage("res/GUI/icon/iconTempHigh.png");
   protected Image[] needsBubbleCenter = new Image[]{
      ImageLoader.getImage("res/GUI/icon/needsBubble/needsBubbleCenter" + Utilities.randomInt(1, 6) + ".png"),
      ImageLoader.getImage("res/GUI/icon/needsBubble/needsBubbleCenter" + Utilities.randomInt(1, 6) + ".png"),
      ImageLoader.getImage("res/GUI/icon/needsBubble/needsBubbleCenter" + Utilities.randomInt(1, 6) + ".png"),
      ImageLoader.getImage("res/GUI/icon/needsBubble/needsBubbleCenter" + Utilities.randomInt(1, 6) + ".png"),
      ImageLoader.getImage("res/GUI/icon/needsBubble/needsBubbleCenter" + Utilities.randomInt(1, 6) + ".png"),
      ImageLoader.getImage("res/GUI/icon/needsBubble/needsBubbleCenter" + Utilities.randomInt(1, 6) + ".png"),
      ImageLoader.getImage("res/GUI/icon/needsBubble/needsBubbleCenter" + Utilities.randomInt(1, 6) + ".png"),
      ImageLoader.getImage("res/GUI/icon/needsBubble/needsBubbleCenter" + Utilities.randomInt(1, 6) + ".png"),
      ImageLoader.getImage("res/GUI/icon/needsBubble/needsBubbleCenter" + Utilities.randomInt(1, 6) + ".png"),
      ImageLoader.getImage("res/GUI/icon/needsBubble/needsBubbleCenter" + Utilities.randomInt(1, 6) + ".png")
   };
   protected Image needsBubbleDots = ImageLoader.getImage("res/GUI/icon/needsBubble/needsBubbleDots" + Utilities.randomInt(1, 6) + ".png");
   protected Image needsBubbleLeft = ImageLoader.getImage("res/GUI/icon/needsBubble/needsBubbleLeft" + Utilities.randomInt(1, 3) + ".png");
   protected Image needsBubbleRight = ImageLoader.getImage("res/GUI/icon/needsBubble/needsBubbleRight" + Utilities.randomInt(1, 3) + ".png");
   protected MobReachMap mobReachMap;
   protected boolean debug;
   protected MobFlagsBase mobFlags;
   protected float mobX;
   protected float mobY;
   protected int tileX;
   protected int tileY;
   protected MobBase.MobPosition position = MobBase.MobPosition.NORTH;
   protected Rectangle mobBox;
   protected Rectangle mobBoxGUI;
   protected float alpha = 0.0F;
   protected boolean fadeIn = true;
   protected boolean fadeOut = false;
   protected MobBase mother;
   protected int motherID = -1;
   protected MobBase father;
   protected int fatherID = -1;
   protected MobBase.MobGender gender = MobBase.MobGender.NONE;
   protected String firstName;
   protected String lastName;
   protected String fullName;
   protected MobBase.MobType mobType;
   protected MobBase.MobGroup mobGroup;
   protected ArrayList<MobBase.MobGroup> hostileMobGroups = new ArrayList<>();
   protected ArrayList<MobBase.MobGroup> friendlyMobGroups = new ArrayList<>();
   protected MobJobBase.MobJobType mobJobType = MobJobBase.MobJobType.NONE;
   protected Color[] colorLayers = new Color[7];
   protected int level;
   protected int age;
   protected int lifeExpectancy;
   protected int experience;
   protected int strength;
   protected int dexterity;
   protected int hitPoints;
   protected int hitPointsMax;
   protected int hunger;
   protected int hungerMax;
   protected int thirst;
   protected int thirstMax;
   protected int faith;
   protected int faithMax;
   protected ArrayList<String> killedNameList = new ArrayList<>();
   protected int kills;
   protected int trash;
   protected int trashMax;
   protected int egg;
   protected int eggMax;
   protected int energy;
   protected int energyMax;
   protected int social;
   protected int socialMax;
   protected int bodyTemp;
   protected int bodyTempMax;
   protected float baseVelocity;
   protected float currentVelocity;
   protected float angle;
   protected ArrayList<ResourceBase> carriedResourcesList = new ArrayList<>();
   protected int[] carriedResourcesListIDs;
   protected ResourceBase inventoryHeadSlot;
   protected int inventoryHeadSlotID = -1;
   protected ResourceBase inventoryChestSlot;
   protected int inventoryChestSlotID = -1;
   protected ResourceBase inventoryToolSlot;
   protected int inventoryToolSlotID = -1;
   protected ResourceBase inventoryMeleeWeaponSlot;
   protected int inventoryMeleeWeaponSlotID = -1;
   protected ResourceBase inventoryShieldSlot;
   protected int inventoryShieldSlotID = -1;
   protected ResourceBase inventoryRecoverySlot;
   protected int inventoryRecoverySlotID = -1;
   protected ResourceBase inventoryWaterSlot;
   protected int inventoryWaterSlotID = -1;
   protected ResourceBase inventoryFoodSlot;
   protected int inventoryFoodSlotID = -1;
   protected ResourceBase inventoryRangedWeaponSlot;
   protected int inventoryRangedWeaponSlotID = -1;
   protected ResourceBase inventoryRangedWeaponAmmoSlot;
   protected int inventoryRangedWeaponAmmoSlotID = -1;
   protected boolean dropsBlocked;
   protected Assignment assignment = new Assignment(this);
   protected int courierIdleWithResourcesDelayTick;
   protected int agingRateTick;
   protected int agingConstitutionDecayRateTick;
   protected int healthRegenRateTick;
   protected int healthRepairTimeTick;
   protected int thermalAdjustRateTick;
   protected OrderedPair confusedTarget;
   protected int panicHideInHomeDurationTick;
   protected int assignedHome = -1;
   protected boolean insideHouse;
   protected int sleepDelayTick;
   protected int energyDecayRateTick;
   protected int hungerHarvestTimeTick;
   protected int hungerEatTimeTick;
   protected int hungerDecayRateTick;
   protected int hungerStarvationRateTick;
   protected int thirstHarvestTimeTick;
   protected int thirstDrinkTimeTick;
   protected int thirstDecayRateTick;
   protected int thirstDehydrationRateTick;
   protected int faithDecayRateTick;
   protected boolean socialBlessed;
   protected boolean socialChildBlessed;
   protected int socialCoitusDelayTick;
   protected int socialPregnantDelayTick;
   protected int socialPregnantCount;
   protected int socialGestationDelayTick;
   protected int socialRateTick;
   protected MobBase socialChildFather;
   protected int socialChildFatherID = -1;
   protected MobBase mate;
   protected int mateID = -1;
   protected MobBase owner;
   protected int ownerID = -1;
   protected boolean pregnant;
   protected MobBase tradeTarget;
   protected int tradeTargetID = -1;
   protected int workDelayTick;
   protected int workSpeedReductionBase;
   protected int workFindExtraResourcesLastX;
   protected int workFindExtraResourcesLastY;
   protected boolean dead;
   protected boolean decaying;
   protected int decayTick;
   protected int combatDelayTick;
   protected boolean onFire;
   protected int onFireAmount;
   protected boolean frozen;
   protected int frozenAmount;
   protected boolean poisoned;
   protected int poisonedAmount;
   protected boolean dysentery;
   protected int dysenteryAmount;
   protected boolean blighted;
   protected int blightAmount;
   protected int golemRequiresChargeAmount;
   protected int assignedSpawn = -1;
   protected boolean idle;
   protected boolean grabbed;
   protected int grabbedDropTick;
   protected boolean captured;
   protected ResourceBase capturedResource;
   protected int capturedResourceID = -1;
   protected boolean boundGhost;
   protected MobBase boundGhostBody;
   protected int boundGhostBodyID = -1;
   protected ResourceBase boundGhostResource;
   protected int boundGhostResourceID = -1;
   protected boolean boosted;
   protected int diesSlowlyRateTick;
   protected Light light;
   protected int bleedingAmount;
   protected boolean noSelect;
   protected MobBase.DamageType lastDamageType;
   protected MobBase.DamageCause lastDamageCause;
   protected MobBase lastDamageMob;
   protected int lastDamageMobID = -1;
   protected ObjectBase lastDamageObject;
   protected int lastDamageObjectID = -1;

   public String getCurrentTask() {
      return this.currentTask;
   }

   public void setCurrentTask(String s) {
      this.currentTask = s;
   }

   public AIBase getActiveNode() {
      return this.activeNode;
   }

   public void interruptNode(AIBase a) throws SlickException {
      this.activeNode.interruptNode(a);
   }

   public AIStart getAIStart() {
      return this.aiStart;
   }

   public AIGrabbed getAIGrabbed() {
      return this.aiGrabbed;
   }

   public AIChat getAIChat() {
      return this.aiChat;
   }

   public AICourierParkBehavior getAICourierParkBehavior() {
      return this.aiCourierParkBehavior;
   }

   public AICourierArriveBehavior getAICourierArriveBehavior() {
      return this.aiCourierArriveBehavior;
   }

   public AICourierDepartBehavior getAICourierDepartBehavior() {
      return this.aiCourierDepartBehavior;
   }

   public AIMigrationPreperationBehavior getAIMigrationProperationBehavior() {
      return this.aiMigrationPreperationBehavior;
   }

   public AIMigrantDepartBehavior getAIMigrantLeave() {
      return this.aiMigrantLeave;
   }

   public AITrade getAITrade() {
      return this.aiTrade;
   }

   public AIBirth getAIBirth() {
      return this.aiBirth;
   }

   public AIGrowth getAIGrowth() {
      return this.aiGrowth;
   }

   public AIWander getAIWander() {
      return this.aiWander;
   }

   public AIConfused getAIConfused() {
      return this.aiConfused;
   }

   public AIPanic getAIPanic() {
      return this.aiPanic;
   }

   public AIDeath getAIDeath() {
      return this.aiDeath;
   }

   public AIStuck getAIStuck() {
      return this.aiStuck;
   }

   public AIFindHome getAIFindHome() {
      return this.aiFindHome;
   }

   public AIFindSpawn getAIFindSpawn() {
      return this.aiFindSpawn;
   }

   public AIFindBetterHome getAIFindBetterHome() {
      return this.aiFindBetterHome;
   }

   public AIGoHome getAIGoHome() {
      return this.aiGoHome;
   }

   public AIEquipResource getAIEquipResource() {
      return this.aiEquipResource;
   }

   public AIHealthRepair getAIHealthRepair() {
      return this.aiHealthRepair;
   }

   public AIFindFoodCheck getAIFindFoodCheck() {
      return this.aiFindFoodCheck;
   }

   public AIFindFood getAIFindFood() {
      return this.aiFindFood;
   }

   public AIFindFoodHarvest getAIFindFoodHarvest() {
      return this.aiFindFoodHarvest;
   }

   public AIFindWaterCheck getAIFindWaterCheck() {
      return this.aiFindWaterCheck;
   }

   public AIFindWaterFountain getAIFindWaterFountain() {
      return this.aiFindWaterFountain;
   }

   public AIFindWaterHarvest getAIFindWaterHarvest() {
      return this.aiFindWaterHarvest;
   }

   public AIInHome getAIInHome() {
      return this.aiInHome;
   }

   public AIInHomeHiding getAIInHomeHiding() {
      return this.aiInHomeHiding;
   }

   public AISleepOutside getAISleepOutside() {
      return this.aiSleepOutside;
   }

   public AIIncapacitated getAIIncapacitated() {
      return this.aiIncapacitated;
   }

   public AICombatMobs getAICombatMobs() {
      return this.aiCombatMobs;
   }

   public AICombatBuildings getAICombatBuildings() {
      return this.aiCombatBuildings;
   }

   public AIHostileBehaviorAttack getAIHostileBehaviorAttack() {
      return this.aiHostileBehaviorAttack;
   }

   public AIHostileBehaviorAttackOther getAIHostileBehaviorAttackOther() {
      return this.aiHostileBehaviorAttackOther;
   }

   public AIHostileBehaviorLeave getAIHostileBehaviorLeave() {
      return this.aiHostileBehaviorLeave;
   }

   public AINomadBehavior getAINomadBehavior() {
      return this.aiNomadBehavior;
   }

   public AINomadBehaviorOther getAINomadBehaviorOther() {
      return this.aiNomadBehaviorOther;
   }

   public AILaborerPost getAILaborerPost() {
      return this.aiLaborerPost;
   }

   public AILaborerLeave getAILaborerLeave() {
      return this.aiLaborerLeave;
   }

   public AIProvisionerPost getAIProvisionerPost() {
      return this.aiProvisionerPost;
   }

   public AIProvisionerLeave getAIProvisionerLeave() {
      return this.aiProvisionerLeave;
   }

   public AIBuyLaborer getAIBuyLaborer() {
      return this.aiBuyLaborer;
   }

   public AIBuyFromProvisioner getAIBuyFromProvisioner() {
      return this.aiBuyFromProvisioner;
   }

   public AISellResourceInHand getAISellResourceInHand() {
      return this.aiSellResourceInHand;
   }

   public AISellResource getAISellResource() {
      return this.aiSellResource;
   }

   public AIAnimalWanderBehavior getAIAnimalWanderBehavior() {
      return this.aiAnimalWanderBehavior;
   }

   public AIWildDoggoWanderIn getAIWildDoggoWanderIn() {
      return this.aiWildDoggoWanderIn;
   }

   public AIWildDoggoWanderInOther getAIWildDoggoWanderInOther() {
      return this.aiWildDoggoWanderInOther;
   }

   public AIWildDoggoDomesticate getAIWildDoggoDomesticate() {
      return this.aiWildDoggoDomesticate;
   }

   public AISpawnRepair getAISpawnRepair() {
      return this.aiSpawnRepair;
   }

   public AIGhostBehavior getAIGhostBehavior() {
      return this.aiGhostBehavior;
   }

   public AIGolemHealBehavior getAIGolemHealBehavior() {
      return this.aiGolemHealBehavior;
   }

   public AIGolemGuardBehavior getAIGolemGuardBehavior() {
      return this.aiGolemGuardBehavior;
   }

   public AIGolemPatrolBehavior getAIGolemPatrolBehavior() {
      return this.aiGolemPatrolBehavior;
   }

   public AIMedicWork getAIMedicWork() {
      return this.aiMedicWork;
   }

   public AIPatrolVillage getAIPatrolVillage() {
      return this.aiPatrolVillage;
   }

   public AIPatrolFrontLine getAIPatrolFrontLine() {
      return this.aiPatrolFrontLine;
   }

   public AICaptureSoul getAICaptureSoul() {
      return this.aiCaptureSoul;
   }

   public AICaptureSoulInHand getAICaptureSoulInHand() {
      return this.aiCaptureSoulInHand;
   }

   public AIPlaceVessel getAIPlaceVessel() {
      return this.aiPlaceVessel;
   }

   public AIPlaceVesselInHand getAIPlaceVesselInHand() {
      return this.aiPlaceVesselInHand;
   }

   public AIPray getAIPray() {
      return this.aiPray;
   }

   public AITerminateAnimalPregnancy getAITerminateAnimalPregnancy() {
      return this.aiTerminateAnimalPregnancy;
   }

   public AICaptureAnimals getAICaptureAnimals() {
      return this.aiCaptureAnimals;
   }

   public AICaptureAnimalsInHand getAICaptureAnimalsInHand() {
      return this.aiCaptureAnimalsInHand;
   }

   public AISlaughterAnimal getAISlaughterAnimal() {
      return this.aiSlaughterAnimal;
   }

   public AISlaughterAnimalInHand getAISlaughterAnimalInHand() {
      return this.aiSlaughterAnimalInHand;
   }

   public AIBeingSlaughtered getAIBeingSlaughtered() {
      return this.aiBeingSlaughtered;
   }

   public AIRangerSummoned getAIRangerSummoned() {
      return this.aiRangerSummoned;
   }

   public AIOnFirePanic getAIOnFirePanic() {
      return this.aiOnFirePanic;
   }

   public AIFrozen getAIFrozen() {
      return this.aiFrozen;
   }

   public AIVisitMate getAIVisitMate() {
      return this.aiVisitMate;
   }

   public AIAttemptBreed getAIAttemptBreed() {
      return this.aiAttemptBreed;
   }

   public AIAttemptCorruptionWork getAIAttemptCorruptionWork() {
      return this.aiAttemptCorruptionWork;
   }

   public AICorruptionBuildingPlacement getAICorruptionBuildingPlacement() {
      return this.aiCorruptionBuildingPlacement;
   }

   public AICorruptionBuildWork getAICorruptionBuildWork() {
      return this.aiCorruptionBuildWork;
   }

   public AICorruptionDeliverResourceInHand getAICorruptionDeliverResourceInHand() {
      return this.aiCorruptionDeliverResourceInHand;
   }

   public AICorruptionDeliverResourceOnGround getAICorruptionDeliverResourceOnGround() {
      return this.aiCorruptionDeliverResourceOnGround;
   }

   public AICorruptionDeliverRoadResourceInHand getAICorruptionDeliverRoadResourceInHand() {
      return this.aiCorruptionDeliverRoadResourceInHand;
   }

   public AICorruptionDeliverRoadResourceOnGround getAICorruptionDeliverRoadResourceOnGround() {
      return this.aiCorruptionDeliverRoadResourceOnGround;
   }

   public AICorruptionHarvestWork getAICorruptionHarvestWork() {
      return this.aiCorruptionHarvestWork;
   }

   public AICorruptionClearTopographyWork getAICorruptionClearTopographyWork() {
      return this.aiCorruptionClearTopographyWork;
   }

   public AICorruptionClearTopographyToBuildWork getAICorruptionClearTopographyToBuildWork() {
      return this.aiCorruptionClearTopographyToBuildWork;
   }

   public AICorruptionRoadWork getAICorruptionRoadWork() {
      return this.aiCorruptionRoadWork;
   }

   public AIAttemptWork getAIAttemptWork() {
      return this.aiAttemptWork;
   }

   public AIFindJob getAIFindJob() {
      return this.aiFindJob;
   }

   public AIQuitJob getAIQuitJob() {
      return this.aiQuitJob;
   }

   public AICriticalRepairWork getAICriticalRepairWork() {
      return this.aiCriticalRepairWork;
   }

   public AIRepairWork getAIRepairWork() {
      return this.aiRepairWork;
   }

   public AIRepairRoadWork getAIRepairRoadWork() {
      return this.aiRepairRoadWork;
   }

   public AIDismantleWork getAIDismantleWork() {
      return this.aiDismantleWork;
   }

   public AIBuildWork getAIBuildWork() {
      return this.aiBuildWork;
   }

   public AIClearTopographyWork getAIClearTopographyWork() {
      return this.aiClearTopographyWork;
   }

   public AIClearTopographyFromRoadWork getAIClearTopographyFromRoadWork() {
      return this.aiClearTopographyFromRoadWork;
   }

   public AIClearResourcesFromRoadWork getAIClearResourcesFromRoadWork() {
      return this.aiClearResourcesFromRoadWork;
   }

   public AIRemoveRoadWork getAIRemoveRoadWork() {
      return this.aiRemoveRoadWork;
   }

   public AIDigWork getAIDigWork() {
      return this.aiDigWork;
   }

   public AIRoadWork getAIRoadWork() {
      return this.aiRoadWork;
   }

   public AIConstructionHarvestWork getAIConstructionHarvestWork() {
      return this.aiConstructionHarvestWork;
   }

   public AIHarvestWork getAIHarvestWork() {
      return this.aiHarvestWork;
   }

   public AIHarvestFarmWork getAIHarvestFarmWork() {
      return this.aiHarvestFarmWork;
   }

   public AIHarvestWaterWork getAIHarvestWaterWork() {
      return this.aiHarvestWaterWork;
   }

   public AITendFarmsWork getAITendFarmsWork() {
      return this.aiTendFarmsWork;
   }

   public AIPlantResourceInHand getAIPlantResourceInHand() {
      return this.aiPlantResourceInHand;
   }

   public AIPlantResource getAIPlantResource() {
      return this.aiPlantResource;
   }

   public AIDeliverConstructionResourceInHand getAIDeliverConstructionResourceInHand() {
      return this.aiDeliverConstructionResourceInHand;
   }

   public AIDeliverConstructionResource getAIDeliverConstructionResource() {
      return this.aiDeliverConstructionResource;
   }

   public AIDeliverCourierResourceInHand getAIDeliverCourierResourceInHand() {
      return this.aiDeliverCourierResourceInHand;
   }

   public AIDeliverCourierResource getAIDeliverCourierResource() {
      return this.aiDeliverCourierResource;
   }

   public AIDeliverRoadConstructionResourceInHand getAIDeliverRoadConstructionResourceInHand() {
      return this.aiDeliverRoadConstructionResourceInHand;
   }

   public AIDeliverRoadConstructionResource getAIDeliverRoadConstructionResource() {
      return this.aiDeliverRoadConstructionResource;
   }

   public AIStoreResourceInHand getAIStoreResourceInHand() {
      return this.aiStoreResourceInHand;
   }

   public AIStoreResource getAIStoreResource() {
      return this.aiAIStoreResource;
   }

   public AIStoreTrashInTrashCanInHand getAIStoreTrashInTrashCanInHand() {
      return this.aiStoreTrashInTrashCanInHand;
   }

   public AIStoreTrashInTrashCan getAIStoreTrashInTrashCan() {
      return this.aiAIStoreTrashInTrashCan;
   }

   public AIMakeTrashyCube getAIMakeTrashyCube() {
      return this.aiAIMakeTrashyCube;
   }

   public AIRedistributeResources getAIRedistributeResources() {
      return this.aiRedistributeResources;
   }

   public AIRefineResource getAIRefineResource() {
      return this.aiRefineResource;
   }

   public AIProcessTrash getAIProcessTrash() {
      return this.aiProcessTrash;
   }

   public AIRefineWaterBottle getAIRefineWaterBottle() {
      return this.aiRefineWaterBottle;
   }

   public AIMateCoitus getAIMateCoitus() {
      return this.aiMateCoitus;
   }

   public AIMateGoCoitus getAIMateGoCoitus() {
      return this.aiMateGoCoitus;
   }

   public AIMateGoCoitusOther getAIMateGoCoitusOther() {
      return this.aiMateGoCoitusOther;
   }

   private void initAI() {
      this.aiStart = new AIStart(this);
      this.aiNodes.add(this.aiStart);
      this.aiGrabbed = new AIGrabbed(this);
      this.aiNodes.add(this.aiGrabbed);
      this.aiChat = new AIChat(this);
      this.aiNodes.add(this.aiChat);
      this.aiCourierParkBehavior = new AICourierParkBehavior(this);
      this.aiNodes.add(this.aiCourierParkBehavior);
      this.aiCourierArriveBehavior = new AICourierArriveBehavior(this);
      this.aiNodes.add(this.aiCourierArriveBehavior);
      this.aiCourierDepartBehavior = new AICourierDepartBehavior(this);
      this.aiNodes.add(this.aiCourierDepartBehavior);
      this.aiMigrationPreperationBehavior = new AIMigrationPreperationBehavior(this);
      this.aiNodes.add(this.aiMigrationPreperationBehavior);
      this.aiMigrantLeave = new AIMigrantDepartBehavior(this);
      this.aiNodes.add(this.aiMigrantLeave);
      this.aiTrade = new AITrade(this);
      this.aiNodes.add(this.aiTrade);
      this.aiBirth = new AIBirth(this);
      this.aiNodes.add(this.aiBirth);
      this.aiGrowth = new AIGrowth(this);
      this.aiNodes.add(this.aiGrowth);
      this.aiWander = new AIWander(this);
      this.aiNodes.add(this.aiWander);
      this.aiConfused = new AIConfused(this);
      this.aiNodes.add(this.aiConfused);
      this.aiPanic = new AIPanic(this);
      this.aiNodes.add(this.aiPanic);
      this.aiDeath = new AIDeath(this);
      this.aiNodes.add(this.aiDeath);
      this.aiStuck = new AIStuck(this);
      this.aiNodes.add(this.aiStuck);
      this.aiFindHome = new AIFindHome(this);
      this.aiNodes.add(this.aiFindHome);
      this.aiFindSpawn = new AIFindSpawn(this);
      this.aiNodes.add(this.aiFindSpawn);
      this.aiFindBetterHome = new AIFindBetterHome(this);
      this.aiNodes.add(this.aiFindBetterHome);
      this.aiGoHome = new AIGoHome(this);
      this.aiNodes.add(this.aiGoHome);
      this.aiEquipResource = new AIEquipResource(this);
      this.aiNodes.add(this.aiEquipResource);
      this.aiHealthRepair = new AIHealthRepair(this);
      this.aiNodes.add(this.aiHealthRepair);
      this.aiFindFoodCheck = new AIFindFoodCheck(this);
      this.aiNodes.add(this.aiFindFoodCheck);
      this.aiFindFood = new AIFindFood(this);
      this.aiNodes.add(this.aiFindFood);
      this.aiFindFoodHarvest = new AIFindFoodHarvest(this);
      this.aiNodes.add(this.aiFindFoodHarvest);
      this.aiFindWaterCheck = new AIFindWaterCheck(this);
      this.aiNodes.add(this.aiFindWaterCheck);
      this.aiFindWaterFountain = new AIFindWaterFountain(this);
      this.aiNodes.add(this.aiFindWaterFountain);
      this.aiFindWaterHarvest = new AIFindWaterHarvest(this);
      this.aiNodes.add(this.aiFindWaterHarvest);
      this.aiInHome = new AIInHome(this);
      this.aiNodes.add(this.aiInHome);
      this.aiInHomeHiding = new AIInHomeHiding(this);
      this.aiNodes.add(this.aiInHomeHiding);
      this.aiSleepOutside = new AISleepOutside(this);
      this.aiNodes.add(this.aiSleepOutside);
      this.aiIncapacitated = new AIIncapacitated(this);
      this.aiNodes.add(this.aiIncapacitated);
      this.aiCombatMobs = new AICombatMobs(this);
      this.aiNodes.add(this.aiCombatMobs);
      this.aiCombatBuildings = new AICombatBuildings(this);
      this.aiNodes.add(this.aiCombatBuildings);
      this.aiHostileBehaviorAttack = new AIHostileBehaviorAttack(this);
      this.aiNodes.add(this.aiHostileBehaviorAttack);
      this.aiHostileBehaviorAttackOther = new AIHostileBehaviorAttackOther(this);
      this.aiNodes.add(this.aiHostileBehaviorAttackOther);
      this.aiHostileBehaviorLeave = new AIHostileBehaviorLeave(this);
      this.aiNodes.add(this.aiHostileBehaviorLeave);
      this.aiNomadBehavior = new AINomadBehavior(this);
      this.aiNodes.add(this.aiNomadBehavior);
      this.aiNomadBehaviorOther = new AINomadBehaviorOther(this);
      this.aiNodes.add(this.aiNomadBehaviorOther);
      this.aiLaborerPost = new AILaborerPost(this);
      this.aiNodes.add(this.aiLaborerPost);
      this.aiLaborerLeave = new AILaborerLeave(this);
      this.aiNodes.add(this.aiLaborerLeave);
      this.aiProvisionerPost = new AIProvisionerPost(this);
      this.aiNodes.add(this.aiProvisionerPost);
      this.aiProvisionerLeave = new AIProvisionerLeave(this);
      this.aiNodes.add(this.aiProvisionerLeave);
      this.aiBuyLaborer = new AIBuyLaborer(this);
      this.aiNodes.add(this.aiBuyLaborer);
      this.aiBuyFromProvisioner = new AIBuyFromProvisioner(this);
      this.aiNodes.add(this.aiBuyFromProvisioner);
      this.aiSellResourceInHand = new AISellResourceInHand(this);
      this.aiNodes.add(this.aiSellResourceInHand);
      this.aiSellResource = new AISellResource(this);
      this.aiNodes.add(this.aiSellResource);
      this.aiAnimalWanderBehavior = new AIAnimalWanderBehavior(this);
      this.aiNodes.add(this.aiAnimalWanderBehavior);
      this.aiWildDoggoWanderIn = new AIWildDoggoWanderIn(this);
      this.aiNodes.add(this.aiWildDoggoWanderIn);
      this.aiWildDoggoWanderInOther = new AIWildDoggoWanderInOther(this);
      this.aiNodes.add(this.aiWildDoggoWanderInOther);
      this.aiWildDoggoDomesticate = new AIWildDoggoDomesticate(this);
      this.aiNodes.add(this.aiWildDoggoDomesticate);
      this.aiSpawnRepair = new AISpawnRepair(this);
      this.aiNodes.add(this.aiSpawnRepair);
      this.aiGhostBehavior = new AIGhostBehavior(this);
      this.aiNodes.add(this.aiGhostBehavior);
      this.aiGolemHealBehavior = new AIGolemHealBehavior(this);
      this.aiNodes.add(this.aiGolemHealBehavior);
      this.aiGolemGuardBehavior = new AIGolemGuardBehavior(this);
      this.aiNodes.add(this.aiGolemGuardBehavior);
      this.aiGolemPatrolBehavior = new AIGolemPatrolBehavior(this);
      this.aiNodes.add(this.aiGolemPatrolBehavior);
      this.aiMedicWork = new AIMedicWork(this);
      this.aiNodes.add(this.aiMedicWork);
      this.aiPatrolVillage = new AIPatrolVillage(this);
      this.aiNodes.add(this.aiPatrolVillage);
      this.aiPatrolFrontLine = new AIPatrolFrontLine(this);
      this.aiNodes.add(this.aiPatrolFrontLine);
      this.aiCaptureSoul = new AICaptureSoul(this);
      this.aiNodes.add(this.aiCaptureSoul);
      this.aiCaptureSoulInHand = new AICaptureSoulInHand(this);
      this.aiNodes.add(this.aiCaptureSoulInHand);
      this.aiPlaceVessel = new AIPlaceVessel(this);
      this.aiNodes.add(this.aiPlaceVessel);
      this.aiPlaceVesselInHand = new AIPlaceVesselInHand(this);
      this.aiNodes.add(this.aiPlaceVesselInHand);
      this.aiPray = new AIPray(this);
      this.aiNodes.add(this.aiPray);
      this.aiTerminateAnimalPregnancy = new AITerminateAnimalPregnancy(this);
      this.aiNodes.add(this.aiTerminateAnimalPregnancy);
      this.aiCaptureAnimals = new AICaptureAnimals(this);
      this.aiNodes.add(this.aiCaptureAnimals);
      this.aiCaptureAnimalsInHand = new AICaptureAnimalsInHand(this);
      this.aiNodes.add(this.aiCaptureAnimalsInHand);
      this.aiSlaughterAnimal = new AISlaughterAnimal(this);
      this.aiNodes.add(this.aiSlaughterAnimal);
      this.aiSlaughterAnimalInHand = new AISlaughterAnimalInHand(this);
      this.aiNodes.add(this.aiSlaughterAnimalInHand);
      this.aiBeingSlaughtered = new AIBeingSlaughtered(this);
      this.aiNodes.add(this.aiBeingSlaughtered);
      this.aiRangerSummoned = new AIRangerSummoned(this);
      this.aiNodes.add(this.aiRangerSummoned);
      this.aiOnFirePanic = new AIOnFirePanic(this);
      this.aiNodes.add(this.aiOnFirePanic);
      this.aiFrozen = new AIFrozen(this);
      this.aiNodes.add(this.aiFrozen);
      this.aiVisitMate = new AIVisitMate(this);
      this.aiNodes.add(this.aiVisitMate);
      this.aiAttemptBreed = new AIAttemptBreed(this);
      this.aiNodes.add(this.aiAttemptBreed);
      this.aiAttemptCorruptionWork = new AIAttemptCorruptionWork(this);
      this.aiNodes.add(this.aiAttemptCorruptionWork);
      this.aiCorruptionBuildingPlacement = new AICorruptionBuildingPlacement(this);
      this.aiNodes.add(this.aiCorruptionBuildingPlacement);
      this.aiCorruptionBuildWork = new AICorruptionBuildWork(this);
      this.aiNodes.add(this.aiCorruptionBuildWork);
      this.aiCorruptionDeliverResourceInHand = new AICorruptionDeliverResourceInHand(this);
      this.aiNodes.add(this.aiCorruptionDeliverResourceInHand);
      this.aiCorruptionDeliverResourceOnGround = new AICorruptionDeliverResourceOnGround(this);
      this.aiNodes.add(this.aiCorruptionDeliverResourceOnGround);
      this.aiCorruptionDeliverRoadResourceInHand = new AICorruptionDeliverRoadResourceInHand(this);
      this.aiNodes.add(this.aiCorruptionDeliverRoadResourceInHand);
      this.aiCorruptionDeliverRoadResourceOnGround = new AICorruptionDeliverRoadResourceOnGround(this);
      this.aiNodes.add(this.aiCorruptionDeliverRoadResourceOnGround);
      this.aiCorruptionHarvestWork = new AICorruptionHarvestWork(this);
      this.aiNodes.add(this.aiCorruptionHarvestWork);
      this.aiCorruptionClearTopographyWork = new AICorruptionClearTopographyWork(this);
      this.aiNodes.add(this.aiCorruptionClearTopographyWork);
      this.aiCorruptionClearTopographyToBuildWork = new AICorruptionClearTopographyToBuildWork(this);
      this.aiNodes.add(this.aiCorruptionClearTopographyToBuildWork);
      this.aiCorruptionRoadWork = new AICorruptionRoadWork(this);
      this.aiNodes.add(this.aiCorruptionRoadWork);
      this.aiAttemptWork = new AIAttemptWork(this);
      this.aiNodes.add(this.aiAttemptWork);
      this.aiFindJob = new AIFindJob(this);
      this.aiNodes.add(this.aiFindJob);
      this.aiQuitJob = new AIQuitJob(this);
      this.aiNodes.add(this.aiQuitJob);
      this.aiCriticalRepairWork = new AICriticalRepairWork(this);
      this.aiNodes.add(this.aiCriticalRepairWork);
      this.aiRepairWork = new AIRepairWork(this);
      this.aiNodes.add(this.aiRepairWork);
      this.aiRepairRoadWork = new AIRepairRoadWork(this);
      this.aiNodes.add(this.aiRepairRoadWork);
      this.aiDismantleWork = new AIDismantleWork(this);
      this.aiNodes.add(this.aiDismantleWork);
      this.aiBuildWork = new AIBuildWork(this);
      this.aiNodes.add(this.aiBuildWork);
      this.aiClearTopographyWork = new AIClearTopographyWork(this);
      this.aiNodes.add(this.aiClearTopographyWork);
      this.aiClearTopographyFromRoadWork = new AIClearTopographyFromRoadWork(this);
      this.aiNodes.add(this.aiClearTopographyFromRoadWork);
      this.aiClearResourcesFromRoadWork = new AIClearResourcesFromRoadWork(this);
      this.aiNodes.add(this.aiClearResourcesFromRoadWork);
      this.aiRemoveRoadWork = new AIRemoveRoadWork(this);
      this.aiNodes.add(this.aiRemoveRoadWork);
      this.aiDigWork = new AIDigWork(this);
      this.aiNodes.add(this.aiDigWork);
      this.aiRoadWork = new AIRoadWork(this);
      this.aiNodes.add(this.aiRoadWork);
      this.aiConstructionHarvestWork = new AIConstructionHarvestWork(this);
      this.aiNodes.add(this.aiConstructionHarvestWork);
      this.aiHarvestWork = new AIHarvestWork(this);
      this.aiNodes.add(this.aiHarvestWork);
      this.aiHarvestFarmWork = new AIHarvestFarmWork(this);
      this.aiNodes.add(this.aiHarvestFarmWork);
      this.aiHarvestWaterWork = new AIHarvestWaterWork(this);
      this.aiNodes.add(this.aiHarvestWaterWork);
      this.aiTendFarmsWork = new AITendFarmsWork(this);
      this.aiNodes.add(this.aiTendFarmsWork);
      this.aiPlantResourceInHand = new AIPlantResourceInHand(this);
      this.aiNodes.add(this.aiPlantResourceInHand);
      this.aiPlantResource = new AIPlantResource(this);
      this.aiNodes.add(this.aiPlantResource);
      this.aiDeliverConstructionResourceInHand = new AIDeliverConstructionResourceInHand(this);
      this.aiNodes.add(this.aiDeliverConstructionResourceInHand);
      this.aiDeliverConstructionResource = new AIDeliverConstructionResource(this);
      this.aiNodes.add(this.aiDeliverConstructionResource);
      this.aiDeliverCourierResourceInHand = new AIDeliverCourierResourceInHand(this);
      this.aiNodes.add(this.aiDeliverCourierResourceInHand);
      this.aiDeliverCourierResource = new AIDeliverCourierResource(this);
      this.aiNodes.add(this.aiDeliverCourierResource);
      this.aiDeliverRoadConstructionResourceInHand = new AIDeliverRoadConstructionResourceInHand(this);
      this.aiNodes.add(this.aiDeliverRoadConstructionResourceInHand);
      this.aiDeliverRoadConstructionResource = new AIDeliverRoadConstructionResource(this);
      this.aiNodes.add(this.aiDeliverRoadConstructionResource);
      this.aiStoreResourceInHand = new AIStoreResourceInHand(this);
      this.aiNodes.add(this.aiStoreResourceInHand);
      this.aiAIStoreResource = new AIStoreResource(this);
      this.aiNodes.add(this.aiAIStoreResource);
      this.aiStoreTrashInTrashCanInHand = new AIStoreTrashInTrashCanInHand(this);
      this.aiNodes.add(this.aiStoreTrashInTrashCanInHand);
      this.aiAIStoreTrashInTrashCan = new AIStoreTrashInTrashCan(this);
      this.aiNodes.add(this.aiAIStoreTrashInTrashCan);
      this.aiAIMakeTrashyCube = new AIMakeTrashyCube(this);
      this.aiNodes.add(this.aiAIMakeTrashyCube);
      this.aiRedistributeResources = new AIRedistributeResources(this);
      this.aiNodes.add(this.aiRedistributeResources);
      this.aiRefineResource = new AIRefineResource(this);
      this.aiNodes.add(this.aiRefineResource);
      this.aiProcessTrash = new AIProcessTrash(this);
      this.aiNodes.add(this.aiProcessTrash);
      this.aiRefineWaterBottle = new AIRefineWaterBottle(this);
      this.aiNodes.add(this.aiRefineWaterBottle);
      this.aiMateCoitus = new AIMateCoitus(this);
      this.aiNodes.add(this.aiMateCoitus);
      this.aiMateGoCoitus = new AIMateGoCoitus(this);
      this.aiNodes.add(this.aiMateGoCoitus);
      this.aiMateGoCoitusOther = new AIMateGoCoitusOther(this);
      this.aiNodes.add(this.aiMateGoCoitusOther);
      this.activeNode = this.aiStart;
   }

   public int getID() {
      return this.mobID;
   }

   public MobSprite getSprite() {
      return this.sprite;
   }

   public Image getMobRing() {
      return this.mobRing;
   }

   public Image getDropArrow() {
      return this.dropArrow;
   }

   public MobFlagsBase getMobFlags() {
      return this.mobFlags;
   }

   public float getMobX() {
      return this.mobX;
   }

   public float getMobY() {
      return this.mobY;
   }

   public void setMobX(float x) {
      if (x > 4064.0F) {
         this.mobX = 4064.0F;
      } else if (x <= 1.0F) {
         this.mobX = 16.0F;
      } else {
         this.mobX = x;
      }
   }

   public void setMobY(float y) {
      if (y > 4064.0F) {
         this.mobY = 4064.0F;
      } else if (y <= 1.0F) {
         this.mobY = 16.0F;
      } else {
         this.mobY = y;
      }
   }

   public int getTileX() {
      return this.tileX;
   }

   public int getTileY() {
      return this.tileY;
   }

   public void tileUpdate() {
      if ((int)this.mobX % 16 <= 2) {
         this.tileX = (int)this.mobX / 16;
      }

      if ((int)(this.mobY + 8.0F) % 16 <= 2) {
         this.tileY = (int)(this.mobY + 8.0F) / 16;
      }
   }

   public void tileUpdateForced() {
      this.tileX = (int)this.mobX / 16;
      this.tileY = (int)(this.mobY + 8.0F) / 16;
   }

   public float getAlpha() {
      return this.alpha;
   }

   public MobBase getMother() {
      return this.mother;
   }

   public int getMotherID() {
      return this.motherID;
   }

   public MobBase getFather() {
      return this.father;
   }

   public int getFatherID() {
      return this.fatherID;
   }

   public MobBase.MobGender getGender() {
      return this.gender;
   }

   public String getFirstName() {
      return this.firstName;
   }

   public String getLastName() {
      return this.lastName;
   }

   public String getFullName() {
      return this.fullName;
   }

   public MobBase.MobType getMobType() {
      return this.mobType;
   }

   public MobBase.MobGroup getMobGroup() {
      return this.mobGroup;
   }

   public void setMobGroup(MobBase.MobGroup g) {
      this.mobGroup = g;
   }

   public ArrayList<MobBase.MobGroup> getHostileMobGroups() {
      return this.hostileMobGroups;
   }

   public ArrayList<MobBase.MobGroup> getFriendlyMobGroups() {
      return this.friendlyMobGroups;
   }

   public MobJobBase.MobJobType getMobJobType() {
      return this.mobJobType;
   }

   public void setMobJobType(MobJobBase.MobJobType j) {
      this.setMobJobType(j, true);
   }

   public void setMobJobType(MobJobBase.MobJobType j, boolean changeColors) {
      this.mobJob.decreaseCurrentWorkers(this.mobJobType, 1);
      this.mobJobType = j;
      this.mobJob.increaseCurrentWorkers(this.mobJobType, 1);
      if (changeColors) {
         this.refreshMobJobColors();
      }
   }

   public void refreshMobJobColors() {
      if (this.mobJobType == MobJobBase.MobJobType.NONE) {
         this.colorLayers[2] = this.mobFlags.getColorLayersArray()[2][0];
         this.colorLayers[3] = this.mobFlags.getColorLayersArray()[3][0];
      } else {
         this.colorLayers[2] = this.mobJob.getCustomBaseColor(this.mobJobType);
         this.colorLayers[3] = this.mobJob.getCustomAccentColor(this.mobJobType);
      }
   }

   public Color[] getColorLayers() {
      return this.colorLayers;
   }

   protected void calculateStats() {
      this.calculateHitPointsMax();
      this.calculateHungerMax();
      this.calculateThirstMax();
      this.calculateFaithMax();
      this.calculateEnergyMax();
      this.calculateSocialMax();
      this.calculateBodyTempMax();
      this.calculateBaseVelocity();
      this.calculateWorkSpeedReductionBase();
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int i) throws SlickException {
      this.strength = 0;
      this.dexterity = 0;

      for (int x = 0; x < 3; x++) {
         this.levelUp(false);
      }

      this.level = 0;

      for (int x = 0; x < i; x++) {
         this.levelUp(false);
      }

      this.calculateStats();
      this.hitPoints = this.hitPointsMax;
   }

   public void increaseLevel(int i) throws SlickException {
      this.increaseLevel(i, true);
   }

   public void increaseLevel(int i, boolean showIcon) throws SlickException {
      this.level += i;
      if (showIcon) {
         this.font.newFloater("iconLevel", "$GRE1" + this.level, this.mobX + 4.0F, this.mobY);
      }
   }

   public void decreaseLevel(int i) {
      this.level -= i;
      if (this.level < 1) {
         this.level = 1;
      }
   }

   public void setMonsterSpawnLevel() throws SlickException {
      int levelCapLowAdjusted = this.time.getDay() / 2;
      if (levelCapLowAdjusted < this.mobFlags.getLevelCapLow()) {
         levelCapLowAdjusted = this.mobFlags.getLevelCapLow();
      }

      if (levelCapLowAdjusted >= this.mobFlags.getLevelCapHigh()) {
         levelCapLowAdjusted = this.mobFlags.getLevelCapHigh() - 1;
      }

      int levelCapHighAdjusted = this.mobFlags.getLevelCapHigh();
      if (levelCapHighAdjusted > this.time.getDay()) {
      }

      levelCapHighAdjusted = this.time.getDay();
      if (levelCapHighAdjusted > this.mobFlags.getLevelCapHigh()) {
         levelCapHighAdjusted = this.mobFlags.getLevelCapHigh();
      }

      if (levelCapLowAdjusted >= levelCapHighAdjusted) {
         levelCapHighAdjusted = levelCapLowAdjusted + 1;
      }

      int levelOut = Utilities.randomInt(levelCapLowAdjusted, levelCapHighAdjusted);
      int boostChance = 250;
      if (Game.getCS().getGameMode() != GameModeTemplateBase.GameMode.MAIN_MENU) {
         int corruptedTiles = this.corruption.getCorruptionTilesArray().size();
         int corruptedDesiredAmountStart = this.corruption.getCorruptionDesiredAmountStart();
         double corruptedBoost = this.corruption.getCorruptionThreatenedPercentBoost();
         int spawnLevelBonus = 0;
         if (corruptedTiles < corruptedDesiredAmountStart) {
            spawnLevelBonus = (int)(spawnLevelBonus + corruptedBoost / 6.0);
            boostChance -= 100;
         } else if (corruptedBoost >= 1.0) {
            spawnLevelBonus = (int)(spawnLevelBonus + corruptedBoost / 12.0);
            boostChance -= 50;
         }

         levelOut += spawnLevelBonus;
      }

      if (this.mobFlags.getAllowBoostedLevel()) {
         boostChance -= this.time.getDay();
         boostChance = (int)(boostChance * this.save.getActiveRegionalSave().getGameMode().getTemplate().getBoostedMonsterChanceMultiplier());
         if (boostChance < 1) {
            boostChance = 1;
         }

         if (Utilities.randomInt(boostChance) == 0) {
            this.boosted = true;
            levelOut *= 2;

            for (int i = 0; i < this.colorLayers.length; i++) {
               if (this.colorLayers[i] != null) {
                  this.colorLayers[i] = this.colorLayers[i].brighter(2.0F);
               }
            }
         }
      }

      this.setLevel(levelOut);
   }

   public int getAge() {
      return this.age;
   }

   public void setAge(int i) throws SlickException {
      this.age = i;
   }

   public void increaseAge(int i) throws SlickException {
      this.age += i;
   }

   public void decreaseAge(int i) {
      this.age -= i;
      if (this.age < 1) {
         this.age = 1;
      }
   }

   public int getLifeExpectancy() {
      return this.lifeExpectancy;
   }

   public void setLifeExpectancy(int i) {
      this.lifeExpectancy = i;
      if (this.mobGroup == MobBase.MobGroup.VILLAGER && this.perk.getPerkTypeTotal(PerkModule.PerkType.LIFE_EXPECTANCY) > 0.0) {
         this.lifeExpectancy = (int)(this.lifeExpectancy + this.lifeExpectancy * this.perk.getPerkTypeTotal(PerkModule.PerkType.LIFE_EXPECTANCY));
      }
   }

   public void increaseLifeExpectancy(int i) throws SlickException {
      this.lifeExpectancy += i;
   }

   public void decreaseLifeExpectancy(int i) throws SlickException {
      this.lifeExpectancy -= i;
      if (this.lifeExpectancy < 1) {
         this.lifeExpectancy = 1;
      }
   }

   public int getExperience() {
      return this.experience;
   }

   public void setExperience(int i) {
      this.experience = i;
   }

   public void increaseExperience(int i) throws SlickException {
      this.experience += i;
   }

   public void decreaseExperience(int i) throws SlickException {
      this.experience -= i;
      if (this.experience < 0) {
         this.experience = 0;
      }
   }

   public int getStrength() {
      return this.strength;
   }

   public void setStrength(int i) {
      this.strength = i;
      this.calculateStats();
   }

   public void increaseStrength(int i) throws SlickException {
      this.strength += i;
      this.calculateStats();
   }

   public void decreaseStrength(int i) throws SlickException {
      this.strength -= i;
      if (this.strength < 1) {
         this.strength = 1;
      }

      this.calculateStats();
   }

   public int getDexterity() {
      return this.dexterity;
   }

   public void setDexterity(int i) {
      this.dexterity = i;
      this.calculateStats();
   }

   public void increaseDexterity(int i) throws SlickException {
      this.dexterity += i;
      this.calculateStats();
   }

   public void decreaseDexterity(int i) throws SlickException {
      this.dexterity -= i;
      if (this.dexterity < 1) {
         this.dexterity = 1;
      }

      this.calculateStats();
   }

   public int getHitPoints() {
      return this.hitPoints;
   }

   public void setHitPoints(int i) {
      this.hitPoints = i;
   }

   public void increaseHitPoints(int i) throws SlickException {
      if (this.hitPoints < this.hitPointsMax) {
         this.hitPoints += i;
         if (this.hitPoints > this.hitPointsMax) {
            this.hitPoints = this.hitPointsMax;
         }
      }
   }

   public void decreaseHitPoints(int i, MobBase.DamageType type, MobBase.DamageCause cause) throws SlickException {
      this.decreaseHitPoints(i, null, null, type, cause);
   }

   public void decreaseHitPoints(int i, MobBase m, ObjectBase o, MobBase.DamageType type, MobBase.DamageCause cause) throws SlickException {
      this.lastDamageType = type;
      this.lastDamageCause = cause;
      this.lastDamageMob = m;
      this.lastDamageObject = o;
      if (this.hitPoints > 0) {
         this.hitPoints -= i;
         if (this.hitPoints < 0) {
            this.hitPoints = 0;
         }
      }
   }

   public int getHitPointsMax() {
      return this.hitPointsMax;
   }

   public void calculateHitPointsMax() {
      this.hitPointsMax = this.strength * 3;
      if (this.mobGroup == MobBase.MobGroup.VILLAGER && this.perk.getPerkTypeTotal(PerkModule.PerkType.MAXIMUM_HEALTH) > 0.0) {
         this.hitPointsMax = (int)(this.hitPointsMax + this.hitPointsMax * this.perk.getPerkTypeTotal(PerkModule.PerkType.MAXIMUM_HEALTH));
      }
   }

   public int getHunger() {
      return this.hunger;
   }

   public void setHunger(int i) {
      this.hunger = i;
   }

   public void increaseHunger(int i) {
      if (this.hunger < this.hungerMax) {
         this.hunger += i;
         if (this.hunger > this.hungerMax) {
            this.hunger = this.hungerMax;
         }
      }
   }

   public void decreaseHunger(int i) {
      if (this.hunger > 0) {
         this.hunger -= i;
         if (this.hunger < 0) {
            this.hunger = 0;
         }
      }
   }

   public int getHungerMax() {
      return this.hungerMax;
   }

   public void calculateHungerMax() {
      this.hungerMax = 100;
      if (this.mobGroup == MobBase.MobGroup.VILLAGER && this.perk.getPerkTypeTotal(PerkModule.PerkType.MAXIMUM_HUNGER) > 0.0) {
         this.hungerMax = (int)(this.hungerMax + this.hungerMax * this.perk.getPerkTypeTotal(PerkModule.PerkType.MAXIMUM_HUNGER));
      }
   }

   public int getThirst() {
      return this.thirst;
   }

   public void setThirst(int i) {
      this.thirst = i;
   }

   public void increaseThirst(int i) {
      if (this.thirst < this.thirstMax) {
         this.thirst += i;
         if (this.thirst > this.thirstMax) {
            this.thirst = this.thirstMax;
         }
      }
   }

   public void decreaseThirst(int i) {
      if (this.thirst > 0) {
         this.thirst -= i;
         if (this.thirst < 0) {
            this.thirst = 0;
         }
      }
   }

   public int getThirstMax() {
      return this.thirstMax;
   }

   public void calculateThirstMax() {
      this.thirstMax = 100;
      if (this.mobGroup == MobBase.MobGroup.VILLAGER && this.perk.getPerkTypeTotal(PerkModule.PerkType.MAXIMUM_THIRST) > 0.0) {
         this.thirstMax = (int)(this.thirstMax + this.thirstMax * this.perk.getPerkTypeTotal(PerkModule.PerkType.MAXIMUM_THIRST));
      }
   }

   public int getFaith() {
      return this.faith;
   }

   public void setFaith(int i) {
      this.faith = i;
   }

   public void increaseFaith(int i, boolean showIcon) throws SlickException {
      if (this.faith < this.faithMax) {
         this.faith += i;
         if (this.faith > this.faithMax) {
            this.faith = this.faithMax;
         }

         if (showIcon) {
            this.font.newFloater("iconFaithUp", this.mobX + 4.0F, this.mobY);
         }
      }
   }

   public void decreaseFaith(int i, boolean showIcon) throws SlickException {
      if (this.faith > 0) {
         this.faith -= i;
         if (this.faith < 0) {
            this.faith = 0;
         }

         if (showIcon) {
            this.font.newFloater("iconFaithDown", this.mobX + 4.0F, this.mobY);
         }
      }
   }

   public int getFaithMax() {
      return this.faithMax;
   }

   public void calculateFaithMax() {
      this.faithMax = 100;
      if (this.mobGroup == MobBase.MobGroup.VILLAGER && this.perk.getPerkTypeTotal(PerkModule.PerkType.MAXIMUM_FAITH) > 0.0) {
         this.faithMax = (int)(this.faithMax + this.faithMax * this.perk.getPerkTypeTotal(PerkModule.PerkType.MAXIMUM_FAITH));
      }
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

   public int getTrash() {
      return this.trash;
   }

   public void setTrash(int i) {
      this.trash = i;
   }

   public void increaseTrash(int i) {
      if (this.trash < this.trashMax) {
         this.trash += i;
         if (this.trash > this.trashMax) {
            this.trash = this.trashMax;
         }
      }
   }

   public void decreaseTrash(int i) {
      if (this.trash > 0) {
         this.trash -= i;
         if (this.trash < 0) {
            this.trash = 0;
         }
      }
   }

   public int getTrashMax() {
      return this.trashMax;
   }

   public int getEgg() {
      return this.egg;
   }

   public void setEgg(int i) {
      this.egg = i;
   }

   public void increaseEgg(int i) {
      if (this.egg < this.eggMax) {
         this.egg += i;
         if (this.egg > this.eggMax) {
            this.egg = this.eggMax;
         }
      }
   }

   public void decreaseEgg(int i) {
      if (this.egg > 0) {
         this.egg -= i;
         if (this.egg < 0) {
            this.egg = 0;
         }
      }
   }

   public int getEggMax() {
      return this.eggMax;
   }

   public int getEnergy() {
      return this.energy;
   }

   public void setEnergy(int i) {
      this.energy = i;
   }

   public void increaseEnergy(int i) {
      if (this.energy < this.energyMax) {
         this.energy += i;
         if (this.energy > this.energyMax) {
            this.energy = this.energyMax;
         }
      }
   }

   public void decreaseEnergy(int i) {
      if (this.energy > 0) {
         this.energy -= i;
         if (this.energy < 0) {
            this.energy = 0;
         }
      }
   }

   public int getEnergyMax() {
      return this.energyMax;
   }

   public void calculateEnergyMax() {
      this.energyMax = 100;
      if (this.mobGroup == MobBase.MobGroup.VILLAGER && this.perk.getPerkTypeTotal(PerkModule.PerkType.MAXIMUM_ENERGY) > 0.0) {
         this.energyMax = (int)(this.energyMax + this.energyMax * this.perk.getPerkTypeTotal(PerkModule.PerkType.MAXIMUM_ENERGY));
      }
   }

   public int getSocial() {
      return this.social;
   }

   public void setSocial(int i) {
      this.social = i;
   }

   public void increaseSocial(int i) {
      if (this.social < this.socialMax) {
         this.social += i;
         if (this.social > this.socialMax) {
            this.social = this.socialMax;
         }
      }
   }

   public void decreaseSocial(int i) {
      if (this.social > 0) {
         this.social -= i;
         if (this.social < 0) {
            this.social = 0;
         }
      }
   }

   public int getSocialMax() {
      return this.socialMax;
   }

   public void calculateSocialMax() {
      this.socialMax = 100;
      if (this.mobGroup == MobBase.MobGroup.VILLAGER && this.perk.getPerkTypeTotal(PerkModule.PerkType.MAXIMUM_SOCIAL) > 0.0) {
         this.socialMax = (int)(this.socialMax + this.socialMax * this.perk.getPerkTypeTotal(PerkModule.PerkType.MAXIMUM_SOCIAL));
      }
   }

   public int getBodyTemp() {
      return this.bodyTemp;
   }

   public void setBodyTemp(int i) {
      this.bodyTemp = i;
   }

   public void increaseBodyTemp(int i) {
      if (this.bodyTemp < this.bodyTempMax) {
         this.bodyTemp += i;
         if (this.bodyTemp > this.bodyTempMax) {
            this.bodyTemp = this.bodyTempMax;
         }
      }
   }

   public void decreaseBodyTemp(int i) {
      if (this.bodyTemp > 0) {
         this.bodyTemp -= i;
         if (this.bodyTemp < 0) {
            this.bodyTemp = 0;
         }
      }
   }

   public int getBodyTempMax() {
      return this.bodyTempMax;
   }

   public void calculateBodyTempMax() {
      this.bodyTempMax = 100;
   }

   public float getBaseVelocity() {
      return this.baseVelocity;
   }

   public void calculateBaseVelocity() {
      if (this.boosted) {
         this.baseVelocity = this.mobFlags.getStatVelocityBase() + this.dexterity * (this.mobFlags.getStatVelocityMultiplier() * 2.0F);
      } else {
         this.baseVelocity = this.mobFlags.getStatVelocityBase() + this.dexterity * this.mobFlags.getStatVelocityMultiplier();
      }

      if (this.mobGroup == MobBase.MobGroup.VILLAGER && this.perk.getPerkTypeTotal(PerkModule.PerkType.MOVEMENT_SPEED) > 0.0) {
         this.baseVelocity = (float)(this.baseVelocity + this.baseVelocity * this.perk.getPerkTypeTotal(PerkModule.PerkType.MOVEMENT_SPEED));
      }

      if (this.baseVelocity > 1.0F) {
         this.baseVelocity = 1.0F;
      }
   }

   public float getCurrentVelocity() {
      return this.currentVelocity;
   }

   public float getAngle() {
      return this.angle;
   }

   public ArrayList<ResourceBase> getCarriedResourcesList() {
      return this.carriedResourcesList;
   }

   public boolean hasCarriedResources() {
      return this.carriedResourcesList.size() > 0;
   }

   public ResourceModule.ResourceType getCarriedResourcesListType() {
      return this.carriedResourcesList.size() > 0 ? this.carriedResourcesList.get(0).getType() : null;
   }

   public void addCarriedResource(ResourceBase r) {
      this.addCarriedResource(r, false);
   }

   public void addCarriedResource(ResourceBase r, boolean forcePosition) {
      this.resource.setResourceCarried(this, r);
      r.assign(this);
      this.carriedResourcesList.add(0, r);
      if (forcePosition) {
         r.forcePositionMob();
      }
   }

   public ResourceBase removeFirstCarriedResource() {
      this.carriedResourcesList.get(0).unassign();
      return this.carriedResourcesList.remove(0);
   }

   public boolean canCarryMore() {
      return this.carriedResourcesList.size() < this.getCarryMax();
   }

   public int getCarryMax() {
      int carryMax = this.strength / 50 + 2;
      if (this.mobGroup == MobBase.MobGroup.VILLAGER && this.perk.getPerkTypeTotal(PerkModule.PerkType.MAXIMUM_CARRIED_RESOURCES) > 0.0) {
         int bonus = (int)this.perk.getPerkTypeTotal(PerkModule.PerkType.MAXIMUM_CARRIED_RESOURCES);
         carryMax += bonus;
         if (carryMax < 2 + bonus) {
            carryMax = 2 + bonus;
         }

         if (carryMax > 8 + bonus) {
            carryMax = 8 + bonus;
         }
      } else if (this.mobType == MobBase.MobType.LABOR_GOLEM && this.perk.getPerkTypeTotal(PerkModule.PerkType.LABOR_GOLEM_MAXIMUM_CARRIED_RESOURCES) > 0.0) {
         int bonusx = (int)this.perk.getPerkTypeTotal(PerkModule.PerkType.LABOR_GOLEM_MAXIMUM_CARRIED_RESOURCES);
         carryMax += bonusx;
         if (carryMax < 2 + bonusx) {
            carryMax = 2 + bonusx;
         }

         if (carryMax > 8 + bonusx) {
            carryMax = 8 + bonusx;
         }
      } else {
         if (carryMax < 2) {
            carryMax = 2;
         }

         if (carryMax > 8) {
            carryMax = 8;
         }
      }

      return (int)(carryMax * this.mobFlags.getCarryMaxBonusMultiplier());
   }

   public ResourceBase getInventoryHeadSlot() {
      return this.inventoryHeadSlot;
   }

   protected void setInventoryHeadSlot(ResourceBase r) {
      if (this.inventoryHeadSlot != null && r != null) {
         System.out.println("Villager incorrectly assigned a new item to it's head while already using one. " + this.activeNode.getAIName());
      }

      this.inventoryHeadSlot = r;
   }

   public int getInventoryHeadSlotID() {
      return this.inventoryHeadSlotID;
   }

   public ResourceBase getInventoryChestSlot() {
      return this.inventoryChestSlot;
   }

   protected void setInventoryChestSlot(ResourceBase r) {
      if (this.inventoryChestSlot != null && r != null) {
         System.out.println("Villager incorrectly assigned a new item to it's chest while already using one. " + this.activeNode.getAIName());
      }

      this.inventoryChestSlot = r;
   }

   public int getInventoryChestSlotID() {
      return this.inventoryChestSlotID;
   }

   public ResourceBase getInventoryToolSlot() {
      return this.inventoryToolSlot;
   }

   protected void setInventoryToolSlot(ResourceBase r) {
      if (this.inventoryToolSlot != null && r != null) {
         System.out.println("Villager incorrectly assigned a new item to it's tool while already using one. " + this.activeNode.getAIName());
      }

      this.inventoryToolSlot = r;
   }

   public int getInventoryToolSlotID() {
      return this.inventoryToolSlotID;
   }

   public ResourceBase getInventoryMeleeWeaponSlot() {
      return this.inventoryMeleeWeaponSlot;
   }

   protected void setInventoryMeleeWeaponSlot(ResourceBase r) {
      if (this.inventoryMeleeWeaponSlot != null && r != null) {
         System.out.println("Villager incorrectly assigned a new item to it's weapon while already using one. " + this.activeNode.getAIName());
      }

      this.inventoryMeleeWeaponSlot = r;
   }

   public int getInventoryMeleeWeaponSlotID() {
      return this.inventoryMeleeWeaponSlotID;
   }

   public ResourceBase getInventoryShieldSlot() {
      return this.inventoryShieldSlot;
   }

   protected void setInventoryShieldSlot(ResourceBase r) {
      if (this.inventoryShieldSlot != null && r != null) {
         System.out.println("Villager incorrectly assigned a new item to it's weapon while already using one. " + this.activeNode.getAIName());
      }

      this.inventoryShieldSlot = r;
   }

   public int getInventoryShieldSlotID() {
      return this.inventoryShieldSlotID;
   }

   public ResourceBase getInventoryRecoverySlot() {
      return this.inventoryRecoverySlot;
   }

   protected void setInventoryRecoverySlot(ResourceBase r) {
      if (this.inventoryRecoverySlot != null && r != null) {
         System.out.println("Villager incorrectly assigned a new item to it's item one while already using one. " + this.activeNode.getAIName());
      }

      this.inventoryRecoverySlot = r;
   }

   public int getInventoryRecoverySlotID() {
      return this.inventoryRecoverySlotID;
   }

   public ResourceBase getInventoryWaterSlot() {
      return this.inventoryWaterSlot;
   }

   protected void setInventoryWaterSlot(ResourceBase r) {
      if (this.inventoryWaterSlot != null && r != null) {
         System.out.println("Villager incorrectly assigned a new item to it's item two while already using one. " + this.activeNode.getAIName());
      }

      this.inventoryWaterSlot = r;
   }

   public int getInventoryWaterSlotID() {
      return this.inventoryWaterSlotID;
   }

   public ResourceBase getInventoryFoodSlot() {
      return this.inventoryFoodSlot;
   }

   protected void setInventoryFoodSlot(ResourceBase r) {
      if (this.inventoryFoodSlot != null && r != null) {
         System.out.println("Villager incorrectly assigned a new item to it's item two while already using one. " + this.activeNode.getAIName());
      }

      this.inventoryFoodSlot = r;
   }

   public int getInventoryFoodSlotID() {
      return this.inventoryFoodSlotID;
   }

   public ResourceBase getInventoryRangedWeaponSlot() {
      return this.inventoryRangedWeaponSlot;
   }

   protected void setInventoryRangedWeaponSlot(ResourceBase r) {
      if (this.inventoryRangedWeaponSlot != null && r != null) {
         System.out.println("Villager incorrectly assigned a new item to it's ranged weapon while already using one. " + this.activeNode.getAIName());
      }

      this.inventoryRangedWeaponSlot = r;
   }

   public int getInventoryRangedWeaponSlotID() {
      return this.inventoryRangedWeaponSlotID;
   }

   public ResourceBase getInventoryRangedWeaponAmmoSlot() {
      return this.inventoryRangedWeaponAmmoSlot;
   }

   protected void setInventoryRangedWeaponAmmoSlot(ResourceBase r) {
      if (this.inventoryRangedWeaponAmmoSlot != null && r != null) {
         System.out.println("Villager incorrectly assigned a new item to it's ranged weapon ammo while already using one. " + this.activeNode.getAIName());
      }

      this.inventoryRangedWeaponAmmoSlot = r;
   }

   public int getInventoryRangedWeaponAmmoSlotID() {
      return this.inventoryRangedWeaponAmmoSlotID;
   }

   public boolean isDropsBlocked() {
      return this.dropsBlocked;
   }

   public void setDropsBlocked(boolean b) {
      this.dropsBlocked = b;
   }

   public Assignment getAssignment() {
      return this.assignment;
   }

   public int getCourierIdleWithResourcesDelayTick() {
      return this.courierIdleWithResourcesDelayTick;
   }

   public void runCourierIdleWithResourcesDelayTick(int i) {
      this.courierIdleWithResourcesDelayTick += i;
   }

   public void resetCourierIdleWithResourcesDelayTick() {
      this.courierIdleWithResourcesDelayTick = 0;
   }

   public int getAgingRateTick() {
      return this.agingRateTick;
   }

   public void runAgingRateTick(int i) {
      this.agingRateTick += i;
   }

   public void resetAgingRateTick() {
      this.agingRateTick = 0;
   }

   public int getAgingConstitutionDecayRateTick() {
      return this.agingConstitutionDecayRateTick;
   }

   public void runAgingConstitutionDecayRateTick(int i) {
      this.agingConstitutionDecayRateTick += i;
   }

   public void resetAgingConstitutionDecayRateTick() {
      this.agingConstitutionDecayRateTick = 0;
   }

   public int getHealthRegenRateTick() {
      return this.healthRegenRateTick;
   }

   public void runHealthRegenRateTick(int i) {
      this.healthRegenRateTick += i;
   }

   public void resetHealthRegenRateTick() {
      this.healthRegenRateTick = 0;
   }

   public int getHealthRepairTimeTick() {
      return this.healthRepairTimeTick;
   }

   public void runHealthRepairTimeTick(int i) {
      this.healthRepairTimeTick += i;
   }

   public void resetHealthRepairTimeTick() {
      this.healthRepairTimeTick = 0;
   }

   public int getThermalAdjustRateTick() {
      return this.thermalAdjustRateTick;
   }

   public void runThermalAdjustRateTick(int i) {
      this.thermalAdjustRateTick += i;
   }

   public void resetThermalAdjustRateTick() {
      this.thermalAdjustRateTick = 0;
   }

   public OrderedPair getConfusedTarget() {
      return this.confusedTarget;
   }

   public void setConfusedTarget(OrderedPair o) {
      this.confusedTarget = o;
   }

   public int getPanicHideInHomeDurationTick() {
      return this.panicHideInHomeDurationTick;
   }

   public void runPanicHideInHomeDurationTick(int i) {
      this.panicHideInHomeDurationTick += i;
   }

   public void resetPanicHideInHomeDurationTick() {
      this.panicHideInHomeDurationTick = 0;
   }

   public int getAssignedHome() {
      return this.assignedHome;
   }

   public void setAssignedHome(int i) {
      this.assignedHome = i;
   }

   public boolean isInsideBuilding() {
      return this.insideHouse;
   }

   public void setInsideHouse(boolean b) {
      this.insideHouse = b;
   }

   public int getSleepDelayTick() {
      return this.sleepDelayTick;
   }

   public void runSleepDelayTick(int i) {
      this.sleepDelayTick += i;
   }

   public void resetSleepDelayTick() {
      this.sleepDelayTick = 0;
   }

   public int getEnergyDecayRateTick() {
      return this.energyDecayRateTick;
   }

   public void runEnergyDecayRateTick(int i) {
      this.energyDecayRateTick += i;
   }

   public void resetEnergyDecayRateTick() {
      this.energyDecayRateTick = 0;
   }

   public int getHungerHarvestTimeTick() {
      return this.hungerHarvestTimeTick;
   }

   public void runHungerHarvestTimeTick(int i) {
      this.hungerHarvestTimeTick += i;
   }

   public void resetHungerHarvestTimeTick() {
      this.hungerHarvestTimeTick = 0;
   }

   public int getHungerEatTimeTick() {
      return this.hungerEatTimeTick;
   }

   public void runHungerEatTimeTick(int i) {
      this.hungerEatTimeTick += i;
   }

   public void resetHungerEatTimeTick() {
      this.hungerEatTimeTick = 0;
   }

   public int getHungerDecayRateTick() {
      return this.hungerDecayRateTick;
   }

   public void runHungerDecayRateTick(int i) {
      this.hungerDecayRateTick += i;
   }

   public void resetHungerDecayRateTick() {
      this.hungerDecayRateTick = 0;
   }

   public int getHungerStarvationRateTick() {
      return this.hungerStarvationRateTick;
   }

   public void runHungerStarvationRateTick(int i) {
      this.hungerStarvationRateTick += i;
   }

   public void resetHungerStarvationRateTick() {
      this.hungerStarvationRateTick = 0;
   }

   public int getThirstHarvestTimeTick() {
      return this.thirstHarvestTimeTick;
   }

   public void runThirstHarvestTimeTick(int i) {
      this.thirstHarvestTimeTick += i;
   }

   public void resetThirstHarvestTimeTick() {
      this.thirstHarvestTimeTick = 0;
   }

   public int getThirstDrinkTimeTick() {
      return this.thirstDrinkTimeTick;
   }

   public void runThirstDrinkTimeTick(int i) {
      this.thirstDrinkTimeTick += i;
   }

   public void resetThirstDrinkTimeTick() {
      this.thirstDrinkTimeTick = 0;
   }

   public int getThirstDecayRateTick() {
      return this.thirstDecayRateTick;
   }

   public void runThirstDecayRateTick(int i) {
      this.thirstDecayRateTick += i;
   }

   public void resetThirstDecayRateTick() {
      this.thirstDecayRateTick = 0;
   }

   public int getThirstDehydrationRateTick() {
      return this.thirstDehydrationRateTick;
   }

   public void runThirstDehydrationRateTick(int i) {
      this.thirstDehydrationRateTick += i;
   }

   public void resetThirstDehydrationRateTick() {
      this.thirstDehydrationRateTick = 0;
   }

   public int getFaithDecayRateTick() {
      return this.faithDecayRateTick;
   }

   public void runFaithDecayRateTick(int i) {
      this.faithDecayRateTick += i;
   }

   public void resetFaithDecayRateTick() {
      this.faithDecayRateTick = 0;
   }

   public boolean isSocialBlessed() {
      return this.socialBlessed;
   }

   public void setSocialBlessed(boolean b) {
      this.socialBlessed = b;
   }

   public boolean isSocialChildBlessed() {
      return this.socialChildBlessed;
   }

   public void setSocialChildBlessed(boolean b) {
      this.socialChildBlessed = b;
   }

   public int getSocialCoitusDelayTick() {
      return this.socialCoitusDelayTick;
   }

   public void runSocialCoitusDelayTick(int i) {
      this.socialCoitusDelayTick += i;
   }

   public void resetSocialCoitusDelayTick() {
      this.socialCoitusDelayTick = 0;
   }

   public int getSocialPregnantDelayTick() {
      return this.socialPregnantDelayTick;
   }

   public void runSocialPregnantDelayTick(int i) {
      this.socialPregnantDelayTick += i;
   }

   public void resetSocialPregnantDelayTick() {
      this.socialPregnantDelayTick = 0;
   }

   public int getSocialPregnantCount() {
      return this.socialPregnantCount;
   }

   public void setSocialPregnantCount(int i) {
      this.socialPregnantCount = i;
   }

   public void increaseSocialPregnantCount(int i) throws SlickException {
      this.socialPregnantCount += i;
   }

   public void decreaseSocialPregnantCount(int i) throws SlickException {
      this.socialPregnantCount -= i;
      if (this.socialPregnantCount < 0) {
         this.socialPregnantCount = 0;
      }
   }

   public int getSocialGestationDelayTick() {
      return this.socialGestationDelayTick;
   }

   public void runSocialGestationDelayTick(int i) {
      this.socialGestationDelayTick += i;
   }

   public void resetSocialGestationDelayTick() {
      this.socialGestationDelayTick = 0;
   }

   public int getSocialRateTick() {
      return this.socialRateTick;
   }

   public void runSocialRateTick(int i) {
      this.socialRateTick += i;
   }

   public void resetSocialRateTick() {
      this.socialRateTick = 0;
   }

   public MobBase getSocialChildFather() {
      return this.socialChildFather;
   }

   public void setSocialChildFather(MobBase f) {
      this.socialChildFather = f;
   }

   public int getSocialChildFatherID() {
      return this.socialChildFatherID;
   }

   public MobBase getMate() {
      return this.mate;
   }

   public void setMate(MobBase m) throws SlickException {
      this.mate = m;
      if (m != null) {
         this.font.newFloater("iconMate", this.mobX + 4.0F, this.mobY);
         if (m.getID() < this.mobID) {
            m.setLastName(this.lastName);
         } else {
            this.setLastName(m.getLastName());
         }
      }
   }

   public int getMateID() {
      return this.mateID;
   }

   public MobBase getOwner() {
      return this.owner;
   }

   public void setOwner(MobBase m) throws SlickException {
      this.owner = m;
      if (m != null) {
         this.font.newFloater("iconMate", this.mobX + 4.0F, this.mobY);
      }
   }

   public int getOwnerID() {
      return this.ownerID;
   }

   public boolean isPregnant() {
      return this.pregnant;
   }

   public void setPregnant(boolean b) {
      this.pregnant = b;
   }

   public MobBase getTradeTarget() {
      return this.tradeTarget;
   }

   public void setTradeTarget(MobBase m) {
      this.tradeTarget = m;
   }

   public int getTradeTargetID() {
      return this.tradeTargetID;
   }

   public int getWorkDelayTick() {
      return this.workDelayTick;
   }

   public void runWorkDelayTick(int i) {
      this.workDelayTick += i;
   }

   public void resetWorkDelayTick() {
      this.workDelayTick = 0;
   }

   public int getWorkSpeedReductionBase() {
      return this.workSpeedReductionBase;
   }

   public void calculateWorkSpeedReductionBase() {
      this.workSpeedReductionBase = (int)((this.strength + this.dexterity) * 0.05F);
      if (this.workSpeedReductionBase > 50) {
         this.workSpeedReductionBase = 50;
      }
   }

   public int getWorkFindExtraResourcesLastX() {
      return this.workFindExtraResourcesLastX;
   }

   public void setWorkFindExtraResourcesLastX(int i) {
      this.workFindExtraResourcesLastX = i;
   }

   public int getWorkFindExtraResourcesLastY() {
      return this.workFindExtraResourcesLastY;
   }

   public void setWorkFindExtraResourcesLastY(int i) {
      this.workFindExtraResourcesLastY = i;
   }

   public boolean isDead() {
      return this.dead;
   }

   public void setDead(boolean b) {
      this.dead = b;
   }

   public boolean isDecaying() {
      return this.decaying;
   }

   public void setDecaying(boolean b) {
      this.decaying = b;
   }

   public int getDecayTick() {
      return this.decayTick;
   }

   public void runDecayTick(int i) {
      this.decayTick += i;
   }

   public void resetDecayTick() {
      this.decayTick = 0;
   }

   public int getCombatDelayTick() {
      return this.combatDelayTick;
   }

   public void runCombatDelayTick(int i) {
      this.combatDelayTick += i;
   }

   public void resetCombatDelayTick() {
      this.combatDelayTick = 0;
   }

   public boolean isOnFire() {
      return this.onFire;
   }

   public void setOnFire(boolean b) {
      this.onFire = b;
   }

   public int getOnFireAmount() {
      return this.onFireAmount;
   }

   public void increaseOnFireAmount(int i) {
      if (this.mobFlags.getAllowOnFire()) {
         if (this.onFireAmount < this.mobFlags.getOnFireAmountMaximum()) {
            this.onFireAmount += i;
         }

         if (this.onFireAmount > this.mobFlags.getOnFireAmountMaximum()) {
            this.onFireAmount = this.mobFlags.getOnFireAmountMaximum();
         }
      }
   }

   public void resetOnFireAmount() {
      this.onFireAmount = 0;
   }

   public boolean isFrozen() {
      return this.frozen;
   }

   public void setFrozen(boolean b) {
      this.frozen = b;
   }

   public int getFrozenAmount() {
      return this.frozenAmount;
   }

   public void increaseFrozenAmount(int i) {
      if (this.mobFlags.getAllowFrozen()) {
         if (this.frozenAmount < this.mobFlags.getFrozenAmountMaximum()) {
            this.frozenAmount += i;
         }

         if (this.frozenAmount > this.mobFlags.getFrozenAmountMaximum()) {
            this.frozenAmount = this.mobFlags.getFrozenAmountMaximum();
         }
      }
   }

   public void resetFrozenAmount() {
      this.frozenAmount = 0;
   }

   public boolean isPoisoned() {
      return this.poisoned;
   }

   public void setPoisoned(boolean b) {
      this.poisoned = b;
   }

   public int getPoisonedAmount() {
      return this.poisonedAmount;
   }

   public void increasePoisonedAmount(int i) {
      if (this.mobFlags.getAllowPoisoned()) {
         if (this.poisonedAmount < this.mobFlags.getPoisonedAmountMaximum()) {
            this.poisonedAmount += i;
         }

         if (this.poisonedAmount > this.mobFlags.getPoisonedAmountMaximum()) {
            this.poisonedAmount = this.mobFlags.getPoisonedAmountMaximum();
         }
      }
   }

   public void resetPoisonedAmount() {
      this.poisonedAmount = 0;
   }

   public boolean isDysentery() {
      return this.dysentery;
   }

   public void setDysentery(boolean b) {
      this.dysentery = b;
   }

   public int getDysenteryAmount() {
      return this.dysenteryAmount;
   }

   public void increaseDysenteryAmount(int i) {
      if (this.mobFlags.getAllowDysentery()) {
         if (this.dysenteryAmount < this.mobFlags.getDysenteryAmountMaximum()) {
            this.dysenteryAmount += i;
         }

         if (this.dysenteryAmount > this.mobFlags.getDysenteryAmountMaximum()) {
            this.dysenteryAmount = this.mobFlags.getDysenteryAmountMaximum();
         }
      }
   }

   public void resetDysenteryAmount() {
      this.dysenteryAmount = 0;
   }

   public boolean isBlighted() {
      return this.blighted;
   }

   public void setBlighted(boolean b) {
      this.blighted = b;
   }

   public int getBlightAmount() {
      return this.blightAmount;
   }

   public void increaseBlightAmount(int i) {
      if (this.mobFlags.getAllowBlighted()) {
         if (this.blightAmount < this.mobFlags.getBlightedAmountMaximum()) {
            this.blightAmount += i;
         }

         if (this.blightAmount > this.mobFlags.getBlightedAmountMaximum()) {
            this.blightAmount = this.mobFlags.getBlightedAmountMaximum();
         }
      }
   }

   public void resetBlightAmount() {
      this.blightAmount = 0;
   }

   public int getGolemRequiresChargeAmount() {
      return this.golemRequiresChargeAmount;
   }

   public void rechargeGolemRequiresChargeAmount() {
      this.golemRequiresChargeAmount = this.mobFlags.getGolemRequiresChargeAmountMax();
   }

   public void increaseGolemRequiresChargeAmount(int i) {
      if (this.golemRequiresChargeAmount < this.mobFlags.getGolemRequiresChargeAmountMax()) {
         this.golemRequiresChargeAmount += i;
         if (this.golemRequiresChargeAmount > this.mobFlags.getGolemRequiresChargeAmountMax()) {
            this.golemRequiresChargeAmount = this.mobFlags.getGolemRequiresChargeAmountMax();
         }
      }
   }

   public void decreaseGolemRequiresChargeAmount(int i) {
      if (this.golemRequiresChargeAmount > 0) {
         this.golemRequiresChargeAmount -= i;
         if (this.golemRequiresChargeAmount < 0) {
            this.golemRequiresChargeAmount = 0;
         }
      }
   }

   public int getAssignedSpawn() {
      return this.assignedSpawn;
   }

   public void setAssignedSpawn(int i) {
      this.assignedSpawn = i;
   }

   public boolean isIdle() {
      return this.idle;
   }

   public void setIdle(boolean b) {
      this.idle = b;
   }

   public boolean isGrabbed() {
      return this.grabbed;
   }

   public void setGrabbed(boolean b) {
      this.grabbed = b;
   }

   public int getGrabbedDropTick() {
      return this.grabbedDropTick;
   }

   public void runGrabbedDropTick(int i) {
      this.grabbedDropTick += i;
   }

   public void resetGrabbedDropTick() {
      this.grabbedDropTick = 0;
   }

   public boolean isCaptured() {
      return this.captured;
   }

   public void setCaptured(ResourceBase r, boolean b) throws SlickException {
      this.capturedResource = r;
      this.captured = b;
      this.setPosition(Utilities.randomInt(2) == 0 ? MobBase.MobPosition.WEST : MobBase.MobPosition.EAST);
      this.interruptNode(this.getAIStart());
      switch (Utilities.randomInt(3)) {
         case 0:
            this.setPositionFace();
         case 1:
            this.setPositionWalk();
         case 2:
            this.setPositionFlee();
      }
   }

   public void uncapture() {
      this.capturedResource = null;
      this.captured = false;
   }

   public ResourceBase getCapturedResource() {
      return this.capturedResource;
   }

   public int getCapturedResourceID() {
      return this.capturedResourceID;
   }

   public boolean isBoundGhost() {
      return this.boundGhost;
   }

   public void setBoundGhost(ResourceBase r) throws SlickException {
      this.setBoundGhost(null, r);
   }

   public void setBoundGhost(MobBase m) throws SlickException {
      this.setBoundGhost(m, null);
   }

   private void setBoundGhost(MobBase m, ResourceBase r) throws SlickException {
      this.boundGhostBody = m;
      this.boundGhostResource = r;
      this.boundGhost = true;
   }

   public void unboundGhost() {
      this.boundGhostBody = null;
      this.boundGhostResource = null;
      this.boundGhost = false;
   }

   public MobBase getBoundGhostBody() {
      return this.boundGhostBody;
   }

   public int getBoundGhostBodyID() {
      return this.boundGhostBodyID;
   }

   public ResourceBase getBoundGhostResource() {
      return this.boundGhostResource;
   }

   public int getBoundGhostResourceID() {
      return this.boundGhostResourceID;
   }

   public boolean isBoosted() {
      return this.boosted;
   }

   public int getDiesSlowlyRateTick() {
      return this.diesSlowlyRateTick;
   }

   public void runDiesSlowlyRateTick(int i) {
      this.diesSlowlyRateTick += i;
   }

   public void resetDiesSlowlyRateTick() {
      this.diesSlowlyRateTick = 0;
   }

   public Light getLight() {
      return this.light;
   }

   public int getBleedingAmount() {
      return this.bleedingAmount;
   }

   public boolean getNoSelect() {
      return this.noSelect;
   }

   public void setNoSelect(boolean b) {
      this.noSelect = b;
   }

   public MobBase.DamageType getLastDamageType() {
      return this.lastDamageType;
   }

   public MobBase.DamageCause getLastDamageCause() {
      return this.lastDamageCause;
   }

   public MobBase getLastDamageMob() {
      return this.lastDamageMob;
   }

   public int getLastDamageMobID() {
      return this.lastDamageMobID;
   }

   public ObjectBase getLastDamageObject() {
      return this.lastDamageObject;
   }

   public int getLastDamageObjectID() {
      return this.lastDamageObjectID;
   }

   public MobBase() throws SlickException {
   }

   public void configNew(int id, float x, float y, boolean findReachMap) throws SlickException {
   }

   public void configNew(int id, float x, float y, String message, boolean findReachMap) throws SlickException {
   }

   public void configNew(int id, float x, float y) throws SlickException {
      this.mobID = id;
      this.mobX = x;
      this.mobY = y;
      this.tileUpdateForced();
      this.mobBox = new Rectangle(0.0F, 0.0F, 12.0F, 12.0F);
      this.mobBoxGUI = new Rectangle(0.0F, 0.0F, 8.0F, 12.0F);

      for (int c = 0; c < this.colorLayers.length; c++) {
         this.colorLayers[c] = new Color(-1);
      }

      this.mobReachMap = new MobReachMap(this);
   }

   protected void configureNewFinalize(String message, boolean findReachMap, boolean adjustLevelWithDay) throws SlickException {
      if (this.mobFlags.getLightRadius() > 0 && this.mobFlags.getLightIntensity() > 0) {
         this.light = new Light(this.tileX, this.tileY, this.mobFlags.getLightRadius(), this.mobFlags.getLightIntensity(), false);
      } else if (this.boosted) {
         this.light = new Light(this.tileX, this.tileY, (byte)2, (byte)5, false);
      }

      if (adjustLevelWithDay) {
         this.setMonsterSpawnLevel();
      } else {
         int levelOut = Utilities.randomInt(this.mobFlags.getLevelCapLow(), this.mobFlags.getLevelCapHigh());
         this.setLevel(levelOut);
      }

      int ageOut = 0;
      if (this.mobFlags.getAgingStartAgeMinimum() == this.mobFlags.getAgingStartAgeMaximum()) {
         ageOut = this.mobFlags.getAgingStartAgeMinimum();
      } else {
         ageOut = Utilities.randomInt(this.mobFlags.getAgingStartAgeMinimum(), this.mobFlags.getAgingStartAgeMaximum());
      }

      this.setAge(ageOut);
      this.setLifeExpectancy(this.mobFlags.getAgingMaximumAge());
      if (this.mobFlags.isGolemRequiresCharge()) {
         this.rechargeGolemRequiresChargeAmount();
      }

      if (this.mobFlags.getAllowSleep()) {
         this.energy = this.energyMax - Utilities.randomInt(20);
      }

      if (this.mobFlags.getAllowSocial()) {
         this.social = this.socialMax - Utilities.randomInt(20);
      }

      if (this.mobFlags.getAllowThermal()) {
         this.bodyTemp = this.mobFlags.getThermalBaseline() + Utilities.randomInt(-5, 5);
      }

      if (this.mobFlags.getAllowHunger()) {
         this.hunger = this.hungerMax - Utilities.randomInt(20);
      }

      if (this.mobFlags.getAllowThirst()) {
         this.thirst = this.thirstMax - Utilities.randomInt(20);
      }

      if (this.mobFlags.getAllowFaith()) {
         this.faith = this.faithMax / 5;
      }

      if (this.mobFlags.getAllowInventoryRandomSpawnItems()) {
         this.spawnRandomItems();
      }

      if (message != null) {
         Console.newBanner(this.getFormattedName(false), message);
      }

      if (findReachMap) {
         this.mobReachMap.findReachMap();
      }

      this.initAI();
   }

   public void convertInto(int id, MobBase self) throws SlickException {
      this.convertInto(id, self, null);
   }

   public void convertInto(int id, MobBase self, String message) throws SlickException {
      this.configNew(id, self.getMobX(), self.getMobY(), true);
      this.tileX = self.getTileX();
      this.tileY = self.getTileY();
      this.position = self.getPosition();
      if (self.getGender() != MobBase.MobGender.NONE) {
         this.gender = self.getGender();
      }

      this.sprite = this.mob.createMobSpriteSheet(this.mobType, this.gender);
      if (self.getFirstName() != null) {
         this.firstName = self.getFirstName();
         this.lastName = self.getLastName();
         this.fullName = this.firstName + " " + this.lastName;
      } else if (this.mobGroup == MobBase.MobGroup.DOMESTICATED_ANIMAL) {
         this.generateFirstName(MobModule.MobNameType.DOGGO);
      } else if (this.mobGroup == MobBase.MobGroup.VILLAGER) {
         this.generateFullName(MobModule.MobNameType.STANDARD);
      }

      this.level = self.getLevel();
      this.age = self.getAge();
      this.strength = self.getStrength();
      this.dexterity = self.getDexterity();
      if (this.mobFlags.getAllowAging() && self.getLifeExpectancy() > 0) {
         this.lifeExpectancy = self.getLifeExpectancy();
         this.agingConstitutionDecayRateTick = self.getAgingConstitutionDecayRateTick();
      }

      if (self.getMate() != null) {
         this.setMate(self.getMate());
         self.getMate().setMate(this);
         self.setMate(null);
      }

      if (self.getOwner() != null) {
         this.setOwner(self.getOwner());
         self.getOwner().setOwner(this);
         self.setOwner(null);
      }

      if (self.getAssignedHome() != -1) {
         this.assignHome(self.getAssignedHome());
      }

      Color[][] colorLayersArrays = this.mobFlags.getColorLayersArray();
      Color[][] selfColorLayersArrays = self.getMobFlags().getColorLayersArray();

      label122:
      for (int i = 0; i < colorLayersArrays.length; i++) {
         if (selfColorLayersArrays[i] != null && colorLayersArrays[i] != null) {
            Color[] var10;
            for (Color c1 : var10 = colorLayersArrays[i]) {
               Color[] var14;
               for (Color c2 : var14 = selfColorLayersArrays[i]) {
                  if (c1.equals(c2)) {
                     this.colorLayers[i] = self.getColorLayers()[i];
                     continue label122;
                  }
               }
            }
         }
      }

      this.calculateStats();
      if (!self.isDead()) {
         this.hitPoints = self.getHitPoints();
      } else {
         this.hitPoints = this.hitPointsMax;
      }

      if (this.mobFlags.getAllowSleep()) {
         if (self.getEnergy() > this.energyMax * 0.2F) {
            this.energy = self.getEnergy();
         } else {
            this.energy = (int)(this.energyMax * 0.2F);
         }
      }

      if (this.mobFlags.getAllowSocial()) {
         if (self.getSocial() > this.socialMax * 0.2F) {
            this.social = self.getSocial();
         } else {
            this.social = (int)(this.socialMax * 0.2F);
         }
      }

      if (this.mobFlags.getAllowThermal()) {
         this.bodyTemp = self.getBodyTemp();
      }

      if (this.mobFlags.getAllowHunger()) {
         if (self.getHunger() > this.hungerMax * 0.2F) {
            this.hunger = self.getHunger();
         } else {
            this.hunger = (int)(this.hungerMax * 0.2F);
         }
      }

      if (this.mobFlags.getAllowThirst()) {
         if (self.getThirst() > this.thirstMax * 0.2F) {
            this.thirst = self.getThirst();
         } else {
            this.thirst = (int)(this.thirstMax * 0.2F);
         }
      }

      if (this.mobFlags.getAllowFaith()) {
         if (self.getFaith() > this.faithMax * 0.2F) {
            this.faith = self.getFaith();
         } else {
            this.faith = (int)(this.faithMax * 0.2F);
         }
      }

      this.killedNameList = self.getKilledNameList();
      this.kills = self.getKills();
      if (this.mobFlags.getAllowGhostBehavior()) {
         this.fadeOut();
      }

      if (message != null) {
         Console.newBanner(this.getFormattedName(false), message);
      }
   }

   public void configNewFromParents(int id, MobBase mother, MobBase father) throws SlickException {
      this.configNewFromParents(id, mother, father, false);
   }

   public void configNewFromParents(int id, MobBase mother, MobBase father, boolean displayMessage) throws SlickException {
      this.configNew(id, mother.getMobX(), mother.getMobY(), true);
      this.mother = mother;
      this.father = father;
      this.tileX = mother.getTileX();
      this.tileY = mother.getTileY();
      this.position = mother.getPosition();
      this.gender = Utilities.randomInt(2) == 0 ? MobBase.MobGender.MALE : MobBase.MobGender.FEMALE;
      this.generateChildFullName(MobModule.MobNameType.STANDARD);
      if (mother.getLevel() + father.getLevel() > 5) {
         this.level = (mother.getLevel() + father.getLevel()) / 4;
         this.strength = (mother.getStrength() + father.getStrength()) / 4;
         this.dexterity = (mother.getDexterity() + father.getDexterity()) / 4;
         this.hitPoints = this.hitPointsMax;
      } else {
         this.setLevel(1);
      }

      if (!mother.isSocialChildBlessed()) {
         int randomSkin = Utilities.randomInt(2);
         switch (randomSkin) {
            case 0:
               this.colorLayers[0] = mother.getColorLayers()[0];
               break;
            case 1:
               this.colorLayers[0] = father.getColorLayers()[0];
         }

         int randomHair = Utilities.randomInt(2);
         switch (randomHair) {
            case 0:
               this.colorLayers[1] = mother.getColorLayers()[1];
               break;
            case 1:
               this.colorLayers[1] = father.getColorLayers()[1];
         }
      }

      this.calculateStats();
      this.hitPoints = this.hitPointsMax;
      if (this.mobFlags.isGolemRequiresCharge()) {
         this.rechargeGolemRequiresChargeAmount();
      }

      if (this.mobFlags.getAllowSleep()) {
         this.energy = this.energyMax - Utilities.randomInt(20);
      }

      if (this.mobFlags.getAllowSocial()) {
         this.social = this.socialMax - Utilities.randomInt(20);
      }

      if (this.mobFlags.getAllowThermal()) {
         this.bodyTemp = this.mobFlags.getThermalBaseline() + Utilities.randomInt(-5, 5);
      }

      if (this.mobFlags.getAllowHunger()) {
         this.hunger = this.hungerMax - Utilities.randomInt(20);
      }

      if (this.mobFlags.getAllowThirst()) {
         this.thirst = this.thirstMax - Utilities.randomInt(20);
      }

      if (this.mobFlags.getAllowFaith()) {
         this.faith = (mother.getFaith() + father.getFaith()) / 4;
      }

      if (displayMessage) {
         Text.setVariableText("mobType", this.getMobType().getName());
         Console.newBanner(this.getFormattedName(true), Text.getText("console.banner.mob.born.subtitle"));
      }
   }

   public void loadMob(YMLDataMap dataMap) throws SlickException {
      this.mobID = Integer.parseInt(dataMap.getOrDefault("mobID", 0));
      this.activeNodeName = dataMap.getOrDefault("activeNodeName", "null");
      if (this.activeNodeName.contains("GUARD") && !this.activeNodeName.contains("GOLEM")) {
         Console.saveConverterOut(this, "InDev 33f", "InDev 34 Unstable 1", "Converting GUARD AI branch to RANGER.");
         this.activeNodeName = this.activeNodeName.replace("GUARD", "RANGER");
      }

      if (dataMap.containsMap("activeNodeData")) {
         this.activeNodeData = dataMap.getSubMap("activeNodeData");
      }

      this.mobX = Float.parseFloat(dataMap.getOrDefault("mobX", 0.0F));
      this.mobY = Float.parseFloat(dataMap.getOrDefault("mobY", 0.0F));
      this.tileX = Integer.parseInt(dataMap.getOrDefault("tileX", 0));
      this.tileY = Integer.parseInt(dataMap.getOrDefault("tileY", 0));
      String positionString = dataMap.getOrDefault("position", MobBase.MobPosition.NORTH.toString());

      MobBase.MobPosition[] mobJobTypeString;
      for (MobBase.MobPosition mP : mobJobTypeString = MobBase.MobPosition.values()) {
         if (mP.toString().equals(positionString)) {
            this.position = mP;
            break;
         }
      }

      this.alpha = Float.parseFloat(dataMap.getOrDefault("alpha", 0.0F));
      this.fadeIn = Boolean.parseBoolean(dataMap.getOrDefault("fadeIn", 0));
      this.fadeOut = Boolean.parseBoolean(dataMap.getOrDefault("fadeOut", 0));
      this.motherID = Integer.parseInt(dataMap.getOrDefault("motherID", -1));
      this.fatherID = Integer.parseInt(dataMap.getOrDefault("fatherID", -1));
      String genderString = dataMap.getOrDefault("gender", MobBase.MobGender.NONE.toString());

      MobBase.MobGender[] lastDamageTypeString;
      for (MobBase.MobGender mG : lastDamageTypeString = MobBase.MobGender.values()) {
         if (mG.toString().equals(genderString)) {
            this.gender = mG;
            break;
         }
      }

      if (!dataMap.getOrDefault("firstName", "null").equals("null")) {
         this.firstName = dataMap.get("firstName").toString();
      }

      if (!dataMap.getOrDefault("lastName", "null").equals("null")) {
         this.lastName = dataMap.get("lastName").toString();
      }

      if (!dataMap.getOrDefault("fullName", "null").equals("null")) {
         this.fullName = dataMap.get("fullName").toString();
      }

      String mobTypeString = dataMap.getOrDefault("mobType", MobBase.MobType.ZOMBIE.toString());
      mobTypeString = SaveConversionUtilities.mobTypeConversion(this, mobTypeString);

      MobBase.MobType[] lastDamageCauseString;
      for (MobBase.MobType mT : lastDamageCauseString = MobBase.MobType.values()) {
         if (mT.toString().equals(mobTypeString)) {
            this.mobType = mT;
            break;
         }
      }

      this.mobFlags = MobFactory.getMobInstance(this.mobType).getMobFlags();
      String mobGroupString = dataMap.getOrDefault("mobGroup", MobBase.MobGroup.VILLAGER.toString());

      MobBase.MobGroup[] dC;
      for (MobBase.MobGroup mGx : dC = MobBase.MobGroup.values()) {
         if (mGx.toString().equals(mobGroupString)) {
            this.mobGroup = mGx;
            break;
         }
      }

      if (dataMap.containsKey("hostileMobGroups")) {
         this.hostileMobGroups = dataMap.unpackMobGroupArray("hostileMobGroups");
      }

      if (dataMap.containsKey("friendlyMobGroups")) {
         this.friendlyMobGroups = dataMap.unpackMobGroupArray("friendlyMobGroups");
      }

      String mobJobTypeStringx = dataMap.getOrDefault("mobJobType", MobJobBase.MobJobType.NONE.toString());
      if (mobJobTypeStringx.contains("GUARD")) {
         Console.saveConverterOut(this, "InDev 33f", "InDev 34 Unstable 1", "Converting GUARD MobJob branch to RANGER.");
         mobJobTypeStringx = mobJobTypeStringx.replace("GUARD", "RANGER");
      }

      MobJobBase.MobJobType[] var10;
      for (MobJobBase.MobJobType mJ : MobJobBase.MobJobType.values()) {
         if (mJ.toString().equals(mobJobTypeStringx)) {
            this.mobJobType = mJ;
            break;
         }
      }

      for (int i = 0; i < this.colorLayers.length; i++) {
         if (dataMap.containsKey("colorLayers" + i)) {
            this.colorLayers[i] = dataMap.unpackColor("colorLayers" + i);
         } else {
            this.colorLayers[i] = new Color(-1);
         }
      }

      this.level = Integer.parseInt(dataMap.getOrDefault("level", 1));
      this.age = Integer.parseInt(dataMap.getOrDefault("age", 1));
      this.lifeExpectancy = Integer.parseInt(dataMap.getOrDefault("lifeExpectancy", 0));
      this.experience = Integer.parseInt(dataMap.getOrDefault("experience", 0));
      this.strength = Integer.parseInt(dataMap.getOrDefault("strength", 1));
      this.dexterity = Integer.parseInt(dataMap.getOrDefault("dexterity", 1));
      this.hitPoints = Integer.parseInt(dataMap.getOrDefault("hitPoints", 0));
      this.hitPointsMax = Integer.parseInt(dataMap.getOrDefault("hitPointsMax", 0));
      this.hunger = Integer.parseInt(dataMap.getOrDefault("hunger", 0));
      this.hungerMax = Integer.parseInt(dataMap.getOrDefault("hungerMax", 0));
      this.thirst = Integer.parseInt(dataMap.getOrDefault("thirst", 0));
      this.thirstMax = Integer.parseInt(dataMap.getOrDefault("thirstMax", 0));
      this.faith = Integer.parseInt(dataMap.getOrDefault("faith", 0));
      this.faithMax = Integer.parseInt(dataMap.getOrDefault("faithMax", 0));
      if (this.mobFlags.getAllowFaith() && this.faith == 0 && this.faithMax == 0) {
         Console.saveConverterOut(this, "Update 1c", "Update 2 Unstable 1", "Giving " + this.getFullName() + " the " + this.mobType.getName() + " faith stats.");
         this.calculateFaithMax();
         this.faith = 75;
      }

      if (dataMap.containsKey("killedNameList")) {
         this.killedNameList = dataMap.unpackStringArray("killedNameList");
      }

      this.kills = Integer.parseInt(dataMap.getOrDefault("kills", 0));
      this.trash = Integer.parseInt(dataMap.getOrDefault("trash", 0));
      this.trashMax = Integer.parseInt(dataMap.getOrDefault("trashMax", 0));
      this.egg = Integer.parseInt(dataMap.getOrDefault("egg", 0));
      this.eggMax = Integer.parseInt(dataMap.getOrDefault("eggMax", 0));
      this.energy = Integer.parseInt(dataMap.getOrDefault("energy", 0));
      this.energyMax = Integer.parseInt(dataMap.getOrDefault("energyMax", 0));
      this.social = Integer.parseInt(dataMap.getOrDefault("social", 0));
      this.socialMax = Integer.parseInt(dataMap.getOrDefault("socialMax", 0));
      this.bodyTemp = Integer.parseInt(dataMap.getOrDefault("bodyTemp", 0));
      this.bodyTempMax = Integer.parseInt(dataMap.getOrDefault("bodyTempMax", 0));
      this.baseVelocity = Float.parseFloat(dataMap.getOrDefault("baseVelocity", 0.0F));
      this.currentVelocity = Float.parseFloat(dataMap.getOrDefault("currentVelocity", 0.0F));
      this.angle = Float.parseFloat(dataMap.getOrDefault("angle", 0.0F));
      if (dataMap.containsKey("carriedResourcesListIDs")) {
         this.carriedResourcesListIDs = dataMap.unpackObjectArrayIDs("carriedResourcesListIDs");
      }

      this.inventoryHeadSlotID = Integer.parseInt(dataMap.getOrDefault("inventoryHeadSlotID", -1));
      this.inventoryChestSlotID = Integer.parseInt(dataMap.getOrDefault("inventoryChestSlotID", -1));
      this.inventoryToolSlotID = Integer.parseInt(dataMap.getOrDefault("inventoryToolSlotID", -1));
      this.inventoryMeleeWeaponSlotID = Integer.parseInt(dataMap.getOrDefault("inventoryMeleeWeaponSlotID", -1));
      this.inventoryShieldSlotID = Integer.parseInt(dataMap.getOrDefault("inventoryShieldSlotID", -1));
      this.inventoryRecoverySlotID = Integer.parseInt(dataMap.getOrDefault("inventoryRecoverySlotID", -1));
      this.inventoryWaterSlotID = Integer.parseInt(dataMap.getOrDefault("inventoryWaterSlotID", -1));
      this.inventoryFoodSlotID = Integer.parseInt(dataMap.getOrDefault("inventoryFoodSlotID", -1));
      this.inventoryRangedWeaponSlotID = Integer.parseInt(dataMap.getOrDefault("inventoryRangedWeaponSlotID", -1));
      this.inventoryRangedWeaponAmmoSlotID = Integer.parseInt(dataMap.getOrDefault("inventoryRangedWeaponAmmoSlotID", -1));
      this.dropsBlocked = Boolean.parseBoolean(dataMap.getOrDefault("dropsBlocked", "false"));
      this.assignment.load(dataMap.getSubMap("assignment"));
      this.courierIdleWithResourcesDelayTick = Integer.parseInt(dataMap.getOrDefault("courierIdleWithResourcesDelayTick", 0));
      this.agingRateTick = Integer.parseInt(dataMap.getOrDefault("agingRateTick", 0));
      this.agingConstitutionDecayRateTick = Integer.parseInt(dataMap.getOrDefault("agingConstitutionDecayRateTick", 0));
      this.healthRegenRateTick = Integer.parseInt(dataMap.getOrDefault("healthRegenRateTick", 0));
      this.healthRepairTimeTick = Integer.parseInt(dataMap.getOrDefault("healthRepairTimeTick", 0));
      this.thermalAdjustRateTick = Integer.parseInt(dataMap.getOrDefault("thermalAdjustRateTick", 0));
      if (dataMap.containsKey("confusedTarget")) {
         this.confusedTarget = dataMap.unpackCoordinates("confusedTarget");
      }

      this.assignedHome = Integer.parseInt(dataMap.getOrDefault("assignedHome", -1));
      this.insideHouse = Boolean.parseBoolean(dataMap.getOrDefault("insideHouse", false));
      this.sleepDelayTick = Integer.parseInt(dataMap.getOrDefault("sleepDelayTick", 0));
      this.energyDecayRateTick = Integer.parseInt(dataMap.getOrDefault("energyDecayRateTick", 0));
      this.hungerHarvestTimeTick = Integer.parseInt(dataMap.getOrDefault("hungerHarvestTimeTick", 0));
      this.hungerEatTimeTick = Integer.parseInt(dataMap.getOrDefault("hungerEatTimeTick", 0));
      this.hungerDecayRateTick = Integer.parseInt(dataMap.getOrDefault("hungerDecayRateTick", 0));
      this.hungerStarvationRateTick = Integer.parseInt(dataMap.getOrDefault("hungerStarvationRateTick", 0));
      this.thirstHarvestTimeTick = Integer.parseInt(dataMap.getOrDefault("thirstHarvestTimeTick", 0));
      this.thirstDrinkTimeTick = Integer.parseInt(dataMap.getOrDefault("thirstDrinkTimeTick", 0));
      this.thirstDecayRateTick = Integer.parseInt(dataMap.getOrDefault("thirstDecayRateTick", 0));
      this.thirstDehydrationRateTick = Integer.parseInt(dataMap.getOrDefault("thirstDehydrationRateTick", 0));
      this.faithDecayRateTick = Integer.parseInt(dataMap.getOrDefault("faithDecayRateTick", 0));
      this.socialBlessed = Boolean.parseBoolean(dataMap.getOrDefault("socialBlessed", false));
      this.socialChildBlessed = Boolean.parseBoolean(dataMap.getOrDefault("socialChildBlessed", false));
      this.socialCoitusDelayTick = Integer.parseInt(dataMap.getOrDefault("socialCoitusDelayTick", 0));
      this.socialPregnantDelayTick = Integer.parseInt(dataMap.getOrDefault("socialPregnantDelayTick", 0));
      this.socialPregnantCount = Integer.parseInt(dataMap.getOrDefault("socialPregnantCount", 0));
      this.socialGestationDelayTick = Integer.parseInt(dataMap.getOrDefault("socialGestationDelayTick", 0));
      this.socialRateTick = Integer.parseInt(dataMap.getOrDefault("socialRateTick", 0));
      this.socialChildFatherID = Integer.parseInt(dataMap.getOrDefault("socialChildFatherID", -1));
      this.mateID = Integer.parseInt(dataMap.getOrDefault("mateID", -1));
      this.ownerID = Integer.parseInt(dataMap.getOrDefault("ownerID", -1));
      this.pregnant = Boolean.parseBoolean(dataMap.getOrDefault("pregnant", false));
      this.tradeTargetID = Integer.parseInt(dataMap.getOrDefault("tradeTargetID", -1));
      this.workDelayTick = Integer.parseInt(dataMap.getOrDefault("workDelayTick", 0));
      this.workSpeedReductionBase = Integer.parseInt(dataMap.getOrDefault("workSpeedReductionBase", 0));
      this.workFindExtraResourcesLastX = Integer.parseInt(dataMap.getOrDefault("workFindExtraResourcesLastX", 0));
      this.workFindExtraResourcesLastY = Integer.parseInt(dataMap.getOrDefault("workFindExtraResourcesLastY", 0));
      this.dead = Boolean.parseBoolean(dataMap.getOrDefault("dead", false));
      this.decaying = Boolean.parseBoolean(dataMap.getOrDefault("decaying", false));
      this.decayTick = Integer.parseInt(dataMap.getOrDefault("decayTick", 0));
      this.combatDelayTick = Integer.parseInt(dataMap.getOrDefault("combatDelayTick", 0));
      this.onFire = Boolean.parseBoolean(dataMap.getOrDefault("onFire", false));
      this.onFireAmount = Integer.parseInt(dataMap.getOrDefault("onFireAmount", 0));
      this.frozen = Boolean.parseBoolean(dataMap.getOrDefault("frozen", false));
      this.frozenAmount = Integer.parseInt(dataMap.getOrDefault("frozenAmount", 0));
      this.poisoned = Boolean.parseBoolean(dataMap.getOrDefault("poisoned", false));
      this.poisonedAmount = Integer.parseInt(dataMap.getOrDefault("poisonedAmount", 0));
      this.dysentery = Boolean.parseBoolean(dataMap.getOrDefault("dysentery", false));
      this.dysenteryAmount = Integer.parseInt(dataMap.getOrDefault("dysenteryAmount", 0));
      this.blighted = Boolean.parseBoolean(dataMap.getOrDefault("blighted", false));
      this.blightAmount = Integer.parseInt(dataMap.getOrDefault("blightAmount", 0));
      this.golemRequiresChargeAmount = Integer.parseInt(dataMap.getOrDefault("golemRequiresChargeAmount", 0));
      this.assignedSpawn = Integer.parseInt(dataMap.getOrDefault("assignedSpawn", -1));
      this.idle = Boolean.parseBoolean(dataMap.getOrDefault("idle", false));
      this.grabbed = Boolean.parseBoolean(dataMap.getOrDefault("grabbed", false));
      this.grabbedDropTick = Integer.parseInt(dataMap.getOrDefault("grabbedDropTick", 0));
      this.captured = Boolean.parseBoolean(dataMap.getOrDefault("captured", false));
      this.capturedResourceID = Integer.parseInt(dataMap.getOrDefault("capturedResourceID", -1));
      this.boundGhost = Boolean.parseBoolean(dataMap.getOrDefault("boundGhost", false));
      this.boundGhostBodyID = Integer.parseInt(dataMap.getOrDefault("boundGhostBodyID", -1));
      this.boundGhostResourceID = Integer.parseInt(dataMap.getOrDefault("boundGhostResourceID", -1));
      this.boosted = Boolean.parseBoolean(dataMap.getOrDefault("boosted", false));
      this.diesSlowlyRateTick = Integer.parseInt(dataMap.getOrDefault("diesSlowlyRateTick", 0));
      if (this.lighting.isInitialized()) {
         if (this.mobFlags.getLightRadius() > 0 && this.mobFlags.getLightIntensity() > 0) {
            this.light = new Light(this.tileX, this.tileY, this.mobFlags.getLightRadius(), this.mobFlags.getLightIntensity(), false);
         } else if (this.boosted) {
            this.light = new Light(this.tileX, this.tileY, (byte)2, (byte)5, false);
         }
      }

      this.bleedingAmount = Integer.parseInt(dataMap.getOrDefault("bleedingAmount", 0));
      this.noSelect = Boolean.parseBoolean(dataMap.getOrDefault("noSelect", false));
      String lastDamageTypeStringx = dataMap.getOrDefault("lastDamageType", MobBase.DamageType.REGULAR.toString());

      MobBase.DamageType[] var11;
      for (MobBase.DamageType dT : MobBase.DamageType.values()) {
         if (dT.toString().equals(lastDamageTypeStringx)) {
            this.lastDamageType = dT;
            break;
         }
      }

      String lastDamageCauseStringx = dataMap.getOrDefault("lastDamageCause", MobBase.DamageCause.NONE.toString());

      MobBase.DamageCause[] var12;
      for (MobBase.DamageCause dCx : MobBase.DamageCause.values()) {
         if (dCx.toString().equals(lastDamageCauseStringx)) {
            this.lastDamageCause = dCx;
            break;
         }
      }

      this.lastDamageMobID = Integer.parseInt(dataMap.getOrDefault("lastDamageMobID", -1));
      this.lastDamageObjectID = Integer.parseInt(dataMap.getOrDefault("lastDamageObjectID", -1));
      this.mobBox = new Rectangle(0.0F, 0.0F, 12.0F, 12.0F);
      this.mobBoxGUI = new Rectangle(0.0F, 0.0F, 8.0F, 12.0F);
      this.sprite = this.mob.createMobSpriteSheet(this.mobType, this.gender);
      this.mobReachMap = new MobReachMap(this);
      this.initAI();
   }

   public void renderUnderlay() throws SlickException {
      if (this.alpha > 0.0F && !this.isDead() && this.map.isInView(this.mobX, this.mobY, 10)) {
         int ringAdjust = 0;
         if (this.grabbed) {
            ringAdjust = 20;
         } else if (this.grabbedDropTick > 0) {
            ringAdjust = 20 - this.grabbedDropTick * 2;
         }

         float mapX = this.map.getMapX();
         float mapY = this.map.getMapY();
         if (this.mobGroup == MobBase.MobGroup.MONSTER) {
            this.mobRing
               .drawEmbedded(
                  this.mobX + mapX,
                  this.mobY + mapY + 6.0F + ringAdjust,
                  this.mobX + mapX + this.mobRing.getWidth(),
                  this.mobY + mapY + this.mobRing.getHeight() + 6.0F + ringAdjust,
                  0.0F,
                  0.0F,
                  this.mobRing.getWidth(),
                  this.mobRing.getHeight(),
                  new Color(255.0F, 0.0F, 0.0F, this.ringAlpha)
               );
         } else if (this.mobGroup == MobBase.MobGroup.VILLAGER) {
            this.mobRing
               .drawEmbedded(
                  this.mobX + mapX,
                  this.mobY + mapY + 6.0F + ringAdjust,
                  this.mobX + mapX + this.mobRing.getWidth(),
                  this.mobY + mapY + this.mobRing.getHeight() + 6.0F + ringAdjust,
                  0.0F,
                  0.0F,
                  this.mobRing.getWidth(),
                  this.mobRing.getHeight(),
                  new Color(0.0F, 255.0F, 0.0F, this.ringAlpha)
               );
         } else if (this.mobGroup == MobBase.MobGroup.GOLEM
            || this.mobGroup == MobBase.MobGroup.ANIMAL
            || this.mobGroup == MobBase.MobGroup.DOMESTICATED_ANIMAL) {
            this.mobRing
               .drawEmbedded(
                  this.mobX + mapX,
                  this.mobY + mapY + 6.0F + ringAdjust,
                  this.mobX + mapX + this.mobRing.getWidth(),
                  this.mobY + mapY + this.mobRing.getHeight() + 6.0F + ringAdjust,
                  0.0F,
                  0.0F,
                  this.mobRing.getWidth(),
                  this.mobRing.getHeight(),
                  new Color(255.0F, 255.0F, 0.0F, this.ringAlpha)
               );
         } else if (this.mobGroup == MobBase.MobGroup.CHARMED_MONSTER) {
            this.mobRing
               .drawEmbedded(
                  this.mobX + mapX,
                  this.mobY + mapY + 6.0F + ringAdjust,
                  this.mobX + mapX + this.mobRing.getWidth(),
                  this.mobY + mapY + this.mobRing.getHeight() + 6.0F + ringAdjust,
                  0.0F,
                  0.0F,
                  this.mobRing.getWidth(),
                  this.mobRing.getHeight(),
                  new Color(255.0F, 150.0F, 0.0F, this.ringAlpha)
               );
         } else {
            this.mobRing
               .drawEmbedded(
                  this.mobX + mapX,
                  this.mobY + mapY + 6.0F + ringAdjust,
                  this.mobX + mapX + this.mobRing.getWidth(),
                  this.mobY + mapY + this.mobRing.getHeight() + 6.0F + ringAdjust,
                  0.0F,
                  0.0F,
                  this.mobRing.getWidth(),
                  this.mobRing.getHeight(),
                  new Color(255.0F, 255.0F, 255.0F, this.ringAlpha)
               );
         }
      }
   }

   public void render(Graphics g, boolean paused) throws SlickException {
      float mapX = this.map.getMapX();
      float mapY = this.map.getMapY();
      int depthAmount = 0;
      if (!this.grabbed && this.grabbedDropTick <= 0) {
         depthAmount = this.depth.checkParticleDepth((int)this.mobX + 8, (int)this.mobY + 16);
      }

      if (this.alpha > 0.0F && this.map.isInView(this.mobX, this.mobY, 10)) {
         boolean isCold = false;
         boolean isHot = false;
         if (this.mobFlags.getAllowThermal()) {
            if (this.mobFlags.getCanThermalFreeze() && this.bodyTemp <= this.mobFlags.getThermalFreeze()
               || this.mobFlags.getCanThermalFreezeCritical() && this.bodyTemp <= this.mobFlags.getThermalFreezeCritical()) {
               isCold = true;
            }

            if (this.mobFlags.getCanThermalOverheat() && this.bodyTemp >= this.mobFlags.getThermalOverheat()
               || this.mobFlags.getCanThermalOverheatCritical() && this.bodyTemp >= this.mobFlags.getThermalOverheatCritical()) {
               isHot = true;
            }
         }

         this.sprite
            .render(
               this.mobX + mapX,
               this.mobY + mapY,
               this.grabbed,
               depthAmount,
               this.colorLayers,
               this.alpha,
               1.0F,
               isCold,
               isHot,
               this.poisoned,
               this.dysentery,
               this.blighted,
               this.captured
            );
         if (!paused) {
            this.renderParticles(depthAmount);
         }
      }
   }

   public void renderMapWriter() throws SlickException {
      int depthAmount = 0;
      if (!this.grabbed && this.grabbedDropTick <= 0) {
         depthAmount = this.depth.checkParticleDepth((int)this.mobX + 8, (int)this.mobY + 16);
      }

      if (this.alpha > 0.0F) {
         boolean isCold = false;
         boolean isHot = false;
         if (this.mobFlags.getAllowThermal()) {
            if (this.mobFlags.getCanThermalFreeze() && this.bodyTemp <= this.mobFlags.getThermalFreeze()
               || this.mobFlags.getCanThermalFreezeCritical() && this.bodyTemp <= this.mobFlags.getThermalFreezeCritical()) {
               isCold = true;
            }

            if (this.mobFlags.getCanThermalOverheat() && this.bodyTemp >= this.mobFlags.getThermalOverheat()
               || this.mobFlags.getCanThermalOverheatCritical() && this.bodyTemp >= this.mobFlags.getThermalOverheatCritical()) {
               isHot = true;
            }
         }

         this.sprite
            .renderMapWriter(
               this.mobX,
               this.mobY,
               this.grabbed,
               depthAmount,
               this.colorLayers,
               this.alpha,
               isCold,
               isHot,
               this.poisoned,
               this.dysentery,
               this.blighted,
               this.captured
            );
      }
   }

   public void renderOverlay(Graphics g, boolean debug) throws SlickException {
      float mapX = this.map.getMapX();
      float mapY = this.map.getMapY();
      if (this.map.isInView(this.mobX, this.mobY, 10)) {
         if (this.hitPoints < this.hitPointsMax && this.hitPoints > 0) {
            int hpLeft = (int)Math.ceil((float)this.hitPoints / this.hitPointsMax * 16.0F);
            g.setColor(new Color(0, 0, 0, 50));
            g.fillRect(this.mobX + mapX - 1.0F, this.mobY + mapY - 5.0F, 18.0F, 3.0F);
            g.setColor(Color.black);
            g.fillRect(this.mobX + mapX, this.mobY + mapY - 4.0F, 16.0F, 1.0F);
            if (hpLeft > 12.0) {
               g.setColor(Color.green);
            } else if (hpLeft > 8.0) {
               g.setColor(Color.yellow);
            } else if (hpLeft > 4.0) {
               g.setColor(Color.orange);
            } else {
               g.setColor(Color.red);
            }

            g.fillRect(this.mobX + mapX, this.mobY + mapY - 4.0F, hpLeft, 1.0F);
         }

         if (this.workDelayTick > 0 && this.mobGroup != MobBase.MobGroup.MONSTER) {
            int mobWorkTime = this.mobJob.getCalculatedWorkTime(this);
            if (this.mobFlags.getAllowSpawnRepair() && this.getActiveNode() == this.aiSpawnRepair) {
               mobWorkTime = this.mobFlags.getSpawnRepairTime();
            } else {
               mobWorkTime = this.mobJob.getCalculatedWorkTime(this);
            }

            if (this.workDelayTick < mobWorkTime) {
               int hpLeft = (int)Math.ceil((float)this.workDelayTick / mobWorkTime * 16.0F);
               g.setColor(new Color(0, 0, 0, 50));
               g.fillRect(this.mobX + mapX - 1.0F, this.mobY + mapY - 5.0F, 18.0F, 3.0F);
               g.setColor(Color.black);
               g.fillRect(this.mobX + mapX, this.mobY + mapY - 4.0F, 16.0F, 1.0F);
               g.setColor(Color.cyan);
               g.fillRect(this.mobX + mapX, this.mobY + mapY - 4.0F, hpLeft, 1.0F);
            }
         }

         this.renderNeedsBubble();
         if (this.grabbed) {
            int ringAdjust = 20 - this.grabbedDropTick * 2;
            float targetTileX = this.getCenterPixelX(true) + mapX - 1.5F;
            float targetTileY = this.getCenterPixelY(true) + mapY + 2.0F + ringAdjust;
            this.particle
               .newParticleGroup(
                  targetTileX - mapX + Utilities.randomInt(-2, 2),
                  targetTileY - mapY + this.dropArrowBounce + Utilities.randomInt(-2, 2),
                  ParticleModule.ParticleGroup.MAGIC_FIRE,
                  ParticleModule.ParticleSet.FIRE_BLUE
               );
            Color arrowColor = Color.white;
            if (this.mobGroup == MobBase.MobGroup.MONSTER) {
               arrowColor = new Color(255, 0, 0);
            } else if (this.mobGroup == MobBase.MobGroup.VILLAGER) {
               arrowColor = new Color(0, 255, 0);
            } else if (this.mobGroup == MobBase.MobGroup.GOLEM
               || this.mobGroup == MobBase.MobGroup.ANIMAL
               || this.mobGroup == MobBase.MobGroup.DOMESTICATED_ANIMAL) {
               arrowColor = new Color(255, 255, 0);
            } else if (this.mobGroup == MobBase.MobGroup.CHARMED_MONSTER) {
               arrowColor = new Color(255, 150, 0);
            } else {
               arrowColor = new Color(255, 255, 255);
            }

            this.dropArrow
               .draw(
                  targetTileX,
                  targetTileY + this.dropArrowBounce,
                  targetTileX + this.dropArrow.getWidth(),
                  targetTileY + this.dropArrow.getHeight() + this.dropArrowBounce,
                  0.0F,
                  0.0F,
                  this.dropArrow.getWidth(),
                  this.dropArrow.getHeight(),
                  arrowColor
               );
         }

         if (debug) {
            g.resetTransform();
            g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
            g.setColor(Color.white);
            g.draw(this.mobBox);
            g.resetTransform();
            g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
         }
      }
   }

   public void renderSprite(Graphics g, float x, float y, boolean debug) throws SlickException {
      this.renderSprite(g, x, y, 1.0F, debug);
   }

   public void renderSprite(Graphics g, float x, float y, float scaleAmount, boolean debug) throws SlickException {
      this.mobBoxGUI.setBounds(x + 4.0F * scaleAmount, y + 1.0F * scaleAmount, 8.0F * scaleAmount, 12.0F * scaleAmount);
      boolean isCold = false;
      boolean isHot = false;
      if (this.mobFlags.getAllowThermal()) {
         if (this.mobFlags.getCanThermalFreeze() && this.bodyTemp <= this.mobFlags.getThermalFreeze()
            || this.mobFlags.getCanThermalFreezeCritical() && this.bodyTemp <= this.mobFlags.getThermalFreezeCritical()) {
            isCold = true;
         }

         if (this.mobFlags.getCanThermalOverheat() && this.bodyTemp >= this.mobFlags.getThermalOverheat()
            || this.mobFlags.getCanThermalOverheatCritical() && this.bodyTemp >= this.mobFlags.getThermalOverheatCritical()) {
            isHot = true;
         }
      }

      this.sprite
         .render(
            x,
            y,
            this.grabbed,
            0,
            this.colorLayers,
            this.mobFlags.getMaximumAlpha(),
            scaleAmount,
            isCold,
            isHot,
            this.poisoned,
            this.dysentery,
            this.blighted,
            this.captured
         );
      this.renderSpriteParticles(x, y);
      if (debug) {
         g.draw(this.mobBoxGUI);
      }
   }

   protected void renderParticles(int depthAmount) throws SlickException {
      if (this.alpha > 0.0F) {
         if (depthAmount > 0 && Utilities.randomInt(8) == 0) {
            this.particle
               .newParticle(
                  this.getCenterPixelX(true) + Utilities.randomInt(-2, 2),
                  this.getBottomPixelY(true),
                  ParticleModule.ParticleType.SPARKLE_SPECK,
                  ParticleModule.ParticleSet.LIGHT_GRAY
               );
         }

         if (this.mobFlags.getAllowThermal()) {
            if ((
                  this.mobFlags.getCanThermalFreeze() && this.bodyTemp <= this.mobFlags.getThermalFreeze()
                     || this.mobFlags.getCanThermalFreezeCritical() && this.bodyTemp <= this.mobFlags.getThermalFreezeCritical()
               )
               && Utilities.randomInt(20) == 0) {
               this.particle
                  .newParticle(
                     this.getCenterPixelX(true) + Utilities.randomInt(-2, 2),
                     this.getCenterPixelY(true) + Utilities.randomInt(-6, 6),
                     ParticleModule.ParticleType.SMOKE_SPECK,
                     ParticleModule.ParticleSet.MAGIC_BLUE
                  );
            }

            if ((
                  this.mobFlags.getCanThermalOverheat() && this.bodyTemp >= this.mobFlags.getThermalOverheat()
                     || this.mobFlags.getCanThermalOverheatCritical() && this.bodyTemp >= this.mobFlags.getThermalOverheatCritical()
               )
               && Utilities.randomInt(10) == 0) {
               this.particle
                  .newParticle(
                     this.getCenterPixelX(true) + Utilities.randomInt(-2, 2),
                     this.getCenterPixelY(true) + Utilities.randomInt(-6, 6),
                     ParticleModule.ParticleType.SMOKE_SPECK,
                     ParticleModule.ParticleSet.LIGHT_GRAY
                  );
            }
         }
      }
   }

   protected void renderSpriteParticles(float x, float y) throws SlickException {
   }

   public void renderReachMap() throws SlickException {
      this.mobReachMap.render();
   }

   protected void renderNeedsBubble() throws SlickException {
      if (!this.dead) {
         float mapX = this.map.getMapX();
         float mapY = this.map.getMapY();
         int bubbleLength = 0;
         if (this.mobFlags.getAllowHunger() && this.hunger < this.mobFlags.getHungerHungryFoodSearchLevel()) {
            bubbleLength++;
         }

         if (this.mobFlags.getAllowThirst() && this.thirst < this.mobFlags.getThirstThirstyWaterSearchLevel()) {
            bubbleLength++;
         }

         if (this.mobFlags.getAllowFaith() && this.faith < this.mobFlags.getFaithLowLevel()) {
            bubbleLength++;
         }

         if (this.mobFlags.getAllowHome() && this.assignedHome == -1) {
            bubbleLength++;
         }

         if (this.mobFlags.getAllowSleep()
            && (
               this.mobFlags.getAllowHome() && this.energy < this.mobFlags.getSleepHomeReturnLevel()
                  || this.energy < this.mobFlags.getSleepOutsideEnergyLevel()
            )) {
            bubbleLength++;
         }

         if (this.mobFlags.getAllowThermal()) {
            if (this.mobFlags.getCanThermalFreezeCritical() && this.getBodyTemp() <= this.mobFlags.getThermalFreeze()) {
               bubbleLength++;
            }

            if (this.mobFlags.getCanThermalOverheatCritical() && this.getBodyTemp() >= this.mobFlags.getThermalOverheat()) {
               bubbleLength++;
            }
         }

         int spacing = 14;
         int alignX = 10;
         if (bubbleLength > 0) {
            float alpha = 0.7F;
            this.needsBubbleLeft.setAlpha(alpha);
            this.needsBubbleLeft.draw(mapX + this.mobX + alignX, mapY + this.mobY - 30.0F);
            this.needsBubbleDots.setAlpha(alpha);
            this.needsBubbleDots.draw(mapX + this.mobX + alignX - 2.0F, mapY + this.mobY - 12.0F);
            alignX += 6;

            for (int i = 0; i < bubbleLength; i++) {
               this.needsBubbleCenter[i].setAlpha(alpha);
               this.needsBubbleCenter[i].draw(mapX + this.mobX + alignX, mapY + this.mobY - 30.0F);
               alignX += spacing;
            }

            this.needsBubbleRight.setAlpha(alpha);
            this.needsBubbleRight.draw(mapX + this.mobX + alignX, mapY + this.mobY - 30.0F);
         }

         alignX = 14;
         if (this.mobFlags.getAllowHunger() && this.hunger < this.mobFlags.getHungerHungryFoodSearchLevel()) {
            this.needsStarving.draw(mapX + this.mobX + alignX, mapY + this.mobY - 27.0F);
            alignX += spacing;
         }

         if (this.mobFlags.getAllowThirst() && this.thirst < this.mobFlags.getThirstThirstyWaterSearchLevel()) {
            this.needsWater.draw(mapX + this.mobX + alignX, mapY + this.mobY - 27.0F);
            alignX += spacing;
         }

         if (this.mobFlags.getAllowFaith() && this.faith < this.mobFlags.getFaithLowLevel()) {
            this.needsFaithLow.draw(mapX + this.mobX + alignX, mapY + this.mobY - 27.0F);
            alignX += spacing;
         }

         if (this.mobFlags.getAllowHome() && this.assignedHome == -1) {
            this.needsHomeless.draw(mapX + this.mobX + alignX, mapY + this.mobY - 27.0F);
            alignX += spacing;
         }

         if (this.mobFlags.getAllowSleep()
            && (
               this.mobFlags.getAllowHome() && this.energy < this.mobFlags.getSleepHomeReturnLevel()
                  || this.energy < this.mobFlags.getSleepOutsideEnergyLevel()
            )) {
            this.needsEnergy.draw(mapX + this.mobX + alignX, mapY + this.mobY - 27.0F);
            alignX += spacing;
         }

         if (this.mobFlags.getAllowThermal()) {
            if (this.mobFlags.getCanThermalFreezeCritical() && this.getBodyTemp() <= this.mobFlags.getThermalFreeze()) {
               this.needsTempLow.draw(mapX + this.mobX + alignX, mapY + this.mobY - 27.0F);
               alignX += spacing;
            }

            if (this.mobFlags.getCanThermalOverheatCritical() && this.getBodyTemp() >= this.mobFlags.getThermalOverheat()) {
               this.needsTempHigh.draw(mapX + this.mobX + alignX, mapY + this.mobY - 27.0F);
               alignX += spacing;
            }
         }
      }
   }

   public void update() throws SlickException {
      float mapX = this.map.getMapX();
      float mapY = this.map.getMapY();
      this.mobBox
         .setBounds(
            (this.mobX + mapX) * ScaleControl.getScalingDifference(),
            (this.mobY + mapY) * ScaleControl.getScalingDifference(),
            16.0F * ScaleControl.getScalingDifference(),
            16.0F * ScaleControl.getScalingDifference()
         );
      if (!this.frozen) {
         this.sprite.update((int)((this.currentVelocity - this.baseVelocity) * 5.0F) * -1);
      }

      if (this.grabbed) {
         this.dropArrowBounceSpeed += 0.025F;
         this.dropArrowBounce = this.dropArrowBounce + this.dropArrowBounceSpeed;
         if (this.dropArrowBounce >= 0.0F) {
            this.dropArrowBounceSpeed = -0.5F;
         }
      }

      this.tileUpdate();
      this.updateAlpha();
      this.updateStatistics();
      this.updateOnFire();
      this.updateFrozen();
      this.updatePoisoned();
      this.updateDysentery();
      this.updateBlighted();
      this.updateBoosted();
      this.updateMakeEgg();
      if (this.mobFlags.isGolemRequiresCharge()) {
         this.decreaseGolemRequiresChargeAmount(1);
         if (this.golemRequiresChargeAmount <= 0 && Utilities.randomInt(500) == 0) {
            this.decreaseHitPoints((int)(this.hitPointsMax * 0.05F), MobBase.DamageType.REGULAR, MobBase.DamageCause.NONE);
         }
      }

      if (this.mobFlags.getDiesSlowlyOffCorruption() && !this.corruption.isTileCorrupted(this.tileX, this.tileY)) {
         this.runDiesSlowlyRateTick(1);
         int diesSlowlyRateModified = this.mobFlags.getDiesSlowlyRate();
         if (this.diesSlowlyRateTick > diesSlowlyRateModified) {
            this.decreaseHitPoints(
               (int)(this.hitPointsMax * (this.mobFlags.getDiesSlowlyPercentage() / 100.0F)), MobBase.DamageType.REGULAR, MobBase.DamageCause.NONE
            );
            this.resetDiesSlowlyRateTick();
         }
      }

      if (this.light != null) {
         this.light.setLightX(this.tileX);
         this.light.setLightY(this.tileY);
      }

      if (!this.dead && this.mobFlags.getBloodType() != null && this.bleedingAmount > 0) {
         this.bleedingAmount--;
         if (Utilities.randomInt(20) == 0) {
            this.particle
               .newParticleGroup(
                  this.getCenterPixelX(true), this.getBottomPixelY(true) - 2, ParticleModule.ParticleGroup.BLOOD_SPLATTERS, this.mobFlags.getBloodType()
               );
         }
      }

      this.currentVelocity = 0.0F;
      if (this.map.getMapData().getBlockMap(this.mobFlags.getBlockMapGroup())[this.tileX][this.tileY] == 0 && !this.canReach(this.tileX, this.tileY)) {
         this.removeReachMap();
         this.buildNewReachMap();
      }

      this.updateSpecial();
      if (!this.dead && this.alpha > 0.0F) {
         SoundModule.SoundType idleSound = this.mobFlags.getMobSoundSet(this.gender).getSoundIdle();
         if (idleSound != null && Utilities.randomInt(this.mobFlags.getIdleSoundRate()) == 0) {
            int count = this.mob.getMobsNearby(this.tileX, this.tileY, 4, this.mobType).size() / 2 - 1;
            if (count <= 0 || Utilities.randomInt(count) == 0) {
               this.sound.playSound(idleSound, this.mobX, this.mobY);
            }
         }
      }

      if (this.capturedResource != null) {
         this.mobX = this.capturedResource.getResourceX();
         this.mobY = this.capturedResource.getResourceY() - this.sprite.getCarriedResourceOffsetY().intValue();
      } else {
         long aiStart = System.currentTimeMillis();
         Object currentNode = this.activeNode.getAIName();
         int currentNodeStep = this.activeNode.getAIStep();
         this.activeNode.execute();
         long aiTime = System.currentTimeMillis() - aiStart;
         if (aiTime > 50L) {
            // The message is only built when it is logged (it used to be built for every mob every tick).
            rtr.console.Console.out(this.mobType + " " + this.mobJobType.name() + " AI " + currentNode + " STEP " + currentNodeStep + " time to execute was high: " + aiTime, true);
         }
      }
   }

   private void updateMakeEgg() throws SlickException {
      if (this.eggMax != 0) {
         if (this.mobFlags.allowProduceEggs() && this.egg >= this.eggMax) {
            OrderedPair egg = this.resource.findOpenSpace(this.tileX, this.tileY, MapData.BlockMapGroup.STANDARD);
            if (egg != null) {
               this.resource.createResourceOnGround(ResourceModule.ResourceType.EGG, ResourceModule.ResourceColorSet.DEFAULT, egg.getX(), egg.getY());
            }

            this.setEgg(0);
            this.awardXP(Utilities.randomInt(5) + 5);
            if (this.mobGroup == MobBase.MobGroup.DOMESTICATED_ANIMAL && this.mobFlags.getAllowGodExperience()) {
               this.god.increaseGodXP(0.01, GodModule.GodXPType.EGGS_LAID);
               this.goal.incrementGoal(GoalModule.GoalType.EGGCELLENT);
            }
         }
      }
   }

   protected void updateSpecial() throws SlickException {
   }

   public void updateRingAlpha(boolean active) {
      if (active && this.alpha > 0.0F) {
         if (this.ringAlpha < 1.0F) {
            this.ringAlpha += 0.1F;
         }
      } else if (this.ringAlpha > 0.0F) {
         this.ringAlpha -= 0.1F;
      }
   }

   public void setActiveNode(AIBase a) {
      this.activeNode = a;
   }

   private void updateAlpha() {
      if (this.fadeIn) {
         this.alpha += 0.05F;
         if (this.alpha >= this.mobFlags.getMaximumAlpha()) {
            this.alpha = this.mobFlags.getMaximumAlpha();
            this.fadeIn = false;
         }
      }

      if (this.fadeOut) {
         this.alpha -= 0.05F;
         if (this.alpha <= 0.0F) {
            this.alpha = 0.0F;
            this.fadeOut = false;
         }
      }
   }

   protected void updateStatistics() throws SlickException {
      if (this.dead) {
         this.decayTick++;
      } else {
         ObjectBase currentBuilding = null;
         if (this.insideHouse) {
            currentBuilding = this.object.getBuildingAtLocation(this.tileX, this.tileY);
         }

         if (this.mobFlags.getAllowHealthRegen()
            && (
               !this.mobFlags.getDiesSlowlyOffCorruption()
                  || this.mobFlags.getDiesSlowlyOffCorruption() && this.corruption.isTileCorrupted(this.tileX, this.tileY)
            )) {
            if (this.insideHouse && currentBuilding != null) {
               this.healthRegenRateTick = this.healthRegenRateTick + 1 + currentBuilding.getObjectFlags().getOccupantsHealthRegenBonusTicks();
            } else {
               this.healthRegenRateTick++;
            }

            if (this.mobFlags.getAllowHunger() && this.hunger <= 0) {
               this.healthRegenRateTick--;
            }

            if (this.mobFlags.getAllowThirst() && this.thirst <= 0) {
               this.healthRegenRateTick--;
            }

            if (this.mobFlags.getAllowOnFire() && this.onFire) {
               this.healthRegenRateTick--;
            }

            if (this.mobFlags.getAllowFrozen() && this.frozen) {
               this.healthRegenRateTick--;
            }

            if (this.mobFlags.getAllowPoisoned() && this.poisoned) {
               this.healthRegenRateTick--;
            }

            if (this.mobFlags.getAllowDysentery() && this.dysentery) {
               this.healthRegenRateTick--;
            }

            if (this.mobFlags.getAllowBlighted() && this.blighted) {
               this.healthRegenRateTick--;
            }

            if (this.mobFlags.getCanThermalFreezeCritical() && this.bodyTemp <= this.mobFlags.getThermalFreezeCritical()) {
               this.healthRegenRateTick--;
            }

            if (this.mobFlags.getCanThermalOverheatCritical() && this.bodyTemp >= this.mobFlags.getThermalOverheatCritical()) {
               this.healthRegenRateTick--;
            }

            if (this.healthRegenRateTick < 0) {
               this.healthRegenRateTick = 0;
            }
         }

         if (this.mobFlags.getAllowSleep()) {
            this.energyDecayRateTick++;
            if (this.pregnant) {
               this.energyDecayRateTick++;
            }

            if (this.carriedResourcesList.size() > 0) {
               this.energyDecayRateTick++;
            }
         }

         if (this.mobFlags.getAllowHunger()) {
            if (this.hunger <= 0) {
               this.hungerStarvationRateTick++;
            }

            this.hungerDecayRateTick++;
            if (this.pregnant) {
               this.hungerDecayRateTick++;
            }

            if (this.carriedResourcesList.size() > 0) {
               this.hungerDecayRateTick++;
            }
         }

         if (this.mobFlags.getAllowThirst() && !this.weather.isRaining()) {
            if (this.thirst <= 0) {
               this.thirstDehydrationRateTick++;
            }

            if (this.mobFlags.getAllowThermal()) {
               if (this.mobFlags.getCanThermalOverheat() && this.bodyTemp >= this.mobFlags.getThermalOverheat()) {
                  this.thirstDecayRateTick++;
               } else if (this.mobFlags.getCanThermalOverheatCritical() && this.bodyTemp >= this.mobFlags.getThermalOverheatCritical()) {
                  this.thirstDecayRateTick += 2;
               }
            }

            this.thirstDecayRateTick++;
            if (this.pregnant) {
               this.thirstDecayRateTick++;
            }

            if (this.carriedResourcesList.size() > 0) {
               this.thirstDecayRateTick++;
            }
         }

         if (this.mobFlags.getAllowFaith()) {
            this.faithDecayRateTick++;
         }

         if (this.mobFlags.getAllowSocial()) {
            this.socialRateTick--;
            if (this.insideHouse && currentBuilding != null) {
               for (MobBase m : this.mob.getMobsByAssignment(Assignment.AssignmentType.IN_HOME, false)) {
                  if (m != this && m.getAssignment().getPrimaryObject() == currentBuilding) {
                     this.socialRateTick += 10;
                     break;
                  }
               }
            }
         }

         if (this.mobFlags.getAllowAging() && this.mobFlags.getAgingMaximumAge() > 0) {
            if (this.hitPoints < this.hitPointsMax * 0.75F) {
               this.agingConstitutionDecayRateTick++;
            }

            if (this.mobFlags.getAllowSleep() && this.mobFlags.getCanSleepOutside() && this.energy < this.mobFlags.getSleepOutsideEnergyLevel()) {
               this.agingConstitutionDecayRateTick++;
            }

            if (this.mobFlags.getAllowHunger() && this.hunger < this.mobFlags.getHungerStarvationFoodSearchLevel()) {
               this.agingConstitutionDecayRateTick++;
            }

            if (this.mobFlags.getAllowThirst() && this.thirst < this.mobFlags.getThirstDehydrationWaterSearchLevel()) {
               this.agingConstitutionDecayRateTick++;
            }

            if (this.mobFlags.getAllowHome() && this.assignedHome == -1) {
               this.agingConstitutionDecayRateTick++;
            }

            if (this.mobFlags.getAllowThermal()) {
               if (this.mobFlags.getCanThermalFreezeCritical() && this.bodyTemp <= this.mobFlags.getThermalFreezeCritical()) {
                  this.agingConstitutionDecayRateTick++;
               }

               if (this.mobFlags.getCanThermalOverheatCritical() && this.bodyTemp >= this.mobFlags.getThermalOverheatCritical()) {
                  this.agingConstitutionDecayRateTick++;
               }
            }
         }

         if (this.pregnant) {
            this.socialGestationDelayTick++;
         }

         if (this.mobFlags.getAllowSocial()) {
            if (this.socialCoitusDelayTick < this.mobFlags.getSocialCoitusDelay()) {
               this.socialCoitusDelayTick++;
            }

            if (this.socialPregnantDelayTick < this.mobFlags.getSocialPregnantDelay()) {
               this.socialPregnantDelayTick++;
            }
         }

         if (this.mobFlags.getAllowThermal()) {
            if (this.insideHouse) {
               this.thermalAdjustRateTick += 10;
            } else {
               this.thermalAdjustRateTick++;
            }

            if (this.carriedResourcesList.size() > 0 && Utilities.randomInt(this.mobFlags.getThermalIncreaseTempChance()) == 0) {
               this.increaseBodyTemp(1);
            }
         }

         if (this.mobFlags.getAllowAging()) {
            this.agingRateTick++;
         }

         if (this.mobFlags.getAllowHealthRegen()) {
            int modifiedHealthRegenRate = this.mobFlags.getHealthRegenRate();
            if (this.mobGroup == MobBase.MobGroup.VILLAGER) {
               modifiedHealthRegenRate = (int)(
                  modifiedHealthRegenRate - modifiedHealthRegenRate * this.perk.getPerkTypeTotal(PerkModule.PerkType.HEALTH_RECOVERY_SPEED)
               );
            }

            if (this.healthRegenRateTick > modifiedHealthRegenRate) {
               this.increaseHitPoints(1 + (int)(this.hitPointsMax * 0.05));
               this.healthRegenRateTick = 0;
            }
         }

         if (this.mobFlags.getAllowSleep()) {
            int modifiedEnergyDecayRate = this.mobFlags.getEnergyDecayRate();
            if (this.mobGroup == MobBase.MobGroup.VILLAGER) {
               modifiedEnergyDecayRate = (int)(
                  modifiedEnergyDecayRate + modifiedEnergyDecayRate * this.perk.getPerkTypeTotal(PerkModule.PerkType.ENERGY_DECAY_RATE)
               );
            }

            if (this.energyDecayRateTick > modifiedEnergyDecayRate) {
               this.decreaseEnergy(1);
               this.energyDecayRateTick = 0;
            }
         }

         if (this.mobFlags.getAllowHunger()) {
            int modifiedHungerDecayRate = this.mobFlags.getHungerDecayRate();
            if (this.mobGroup == MobBase.MobGroup.VILLAGER) {
               modifiedHungerDecayRate = (int)(
                  modifiedHungerDecayRate + modifiedHungerDecayRate * this.perk.getPerkTypeTotal(PerkModule.PerkType.HUNGER_DECAY_RATE)
               );
            }

            if (this.hungerDecayRateTick > modifiedHungerDecayRate) {
               this.decreaseHunger(1);
               this.hungerDecayRateTick = 0;
            }
         }

         if (this.mobFlags.getAllowHunger() && this.hunger <= 0 && this.hungerStarvationRateTick > this.mobFlags.getHungerStarvationRate()) {
            this.decreaseHitPoints(1 + (int)(this.hitPointsMax * 0.05), MobBase.DamageType.NUTRIENTS, MobBase.DamageCause.STARVATION);
            this.hungerStarvationRateTick = 0;
         }

         if (this.mobFlags.getAllowThirst()) {
            int modifiedThirstDecayRate = this.mobFlags.getThirstDecayRate();
            if (this.mobGroup == MobBase.MobGroup.VILLAGER) {
               modifiedThirstDecayRate = (int)(
                  modifiedThirstDecayRate + modifiedThirstDecayRate * this.perk.getPerkTypeTotal(PerkModule.PerkType.THIRST_DECAY_RATE)
               );
            }

            if (this.thirstDecayRateTick > modifiedThirstDecayRate) {
               this.decreaseThirst(1);
               this.thirstDecayRateTick = 0;
            }
         }

         if (this.mobFlags.getAllowThirst() && this.thirst <= 0 && this.thirstDehydrationRateTick > this.mobFlags.getThirstDehydrationRate()) {
            this.decreaseHitPoints(1 + (int)(this.hitPointsMax * 0.05), MobBase.DamageType.NUTRIENTS, MobBase.DamageCause.DEHYDRATION);
            this.thirstDehydrationRateTick = 0;
         }

         if (this.mobFlags.getAllowFaith()) {
            int modifiedFaithDecayRate = this.mobFlags.getFaithDecayRate();
            if (this.mobGroup == MobBase.MobGroup.VILLAGER) {
               modifiedFaithDecayRate = (int)(
                  modifiedFaithDecayRate + modifiedFaithDecayRate * this.perk.getPerkTypeTotal(PerkModule.PerkType.FAITH_DECAY_RATE)
               );
            }

            if (this.faithDecayRateTick > modifiedFaithDecayRate) {
               this.decreaseFaith(1, false);
               this.faithDecayRateTick = 0;
            }
         }

         if (this.mobFlags.getAllowSocial()) {
            if (this.socialRateTick > this.mobFlags.getSocialRate()) {
               this.increaseSocial(1);
               this.socialRateTick = 0;
            } else if (this.socialRateTick < 0) {
               this.decreaseSocial(1);
               this.socialRateTick = this.mobFlags.getSocialRate();
            }
         }

         if (this.mobFlags.getAllowAging() && this.mobFlags.getAgingMaximumAge() > 0) {
            if (this.agingConstitutionDecayRateTick > this.mobFlags.getAgingConstitutionDecayRate()) {
               this.decreaseLifeExpectancy(1);
               this.agingConstitutionDecayRateTick = 0;
            }

            if (this.age >= this.lifeExpectancy) {
               this.decreaseHitPoints(1 + (int)(this.hitPointsMax * 0.001), MobBase.DamageType.REGULAR, MobBase.DamageCause.NATURAL_CAUSES);
            }
         }

         if (this.mobFlags.getAllowThermal() && this.thermalAdjustRateTick > this.mobFlags.getThermalAdjustRate()) {
            if (this.insideHouse) {
               if (this.mobFlags.getThermalBaseline() > this.bodyTemp) {
                  this.increaseBodyTemp(1);
               } else if (this.mobFlags.getThermalBaseline() < this.bodyTemp) {
                  this.decreaseBodyTemp(1);
               }
            } else {
               if (this.mobFlags.getCanThermalFreezeCritical() && this.bodyTemp <= this.mobFlags.getThermalFreezeCritical()) {
                  this.decreaseHitPoints(2 + (int)(this.hitPointsMax * 0.05), MobBase.DamageType.ICE, MobBase.DamageCause.COLD);
                  if (this.hitPoints <= this.hitPointsMax * 0.2 && Utilities.randomInt(20) == 0) {
                     this.increaseFrozenAmount(1000);
                  }
               }

               if (this.mobFlags.getCanThermalOverheatCritical() && this.bodyTemp >= this.mobFlags.getThermalOverheatCritical()) {
                  this.decreaseHitPoints(2 + (int)(this.hitPointsMax * 0.05), MobBase.DamageType.FIRE, MobBase.DamageCause.HEAT);
                  if (this.hitPoints <= this.hitPointsMax * 0.2 && Utilities.randomInt(20) == 0) {
                     this.increaseOnFireAmount(1000);
                  }
               }

               int tempDifferance = Math.abs((int)this.weather.getCurrentTemp() - this.bodyTemp);
               if (this.weather.getCurrentTemp() > this.bodyTemp) {
                  if (tempDifferance >= 20) {
                     this.increaseBodyTemp(3);
                  } else if (tempDifferance >= 10) {
                     this.increaseBodyTemp(2);
                  } else {
                     this.increaseBodyTemp(1);
                  }
               } else if (this.weather.getCurrentTemp() < this.bodyTemp) {
                  if (tempDifferance >= 20) {
                     this.decreaseBodyTemp(3);
                  } else if (tempDifferance >= 10) {
                     this.decreaseBodyTemp(2);
                  } else {
                     this.decreaseBodyTemp(1);
                  }
               }

               if (this.bodyTemp > this.weather.getCurrentTemp() / 2.0) {
                  int tileID = this.map.getTileId(this.tileX, this.tileY, 6);
                  if (this.weather.isRaining()) {
                     this.decreaseBodyTemp(1);
                  }

                  if (this.map.getMapTileLoader().isTileWater(tileID)
                     && !this.map.isTileFrozen(this.tileX, this.tileY)
                     && this.resource.getResourceValues()[this.tileX][this.tileY] > 0) {
                     this.decreaseBodyTemp(2);
                  }
               }
            }

            this.thermalAdjustRateTick = 0;
         }

         if (this.mobFlags.getAllowAging() && this.agingRateTick >= this.mobFlags.getAgingRate()) {
            this.increaseAge(1);
            this.agingRateTick = 0;
         }
      }
   }

   public void doWork(int thermal, int sleep, int hunger, int thirst, int faith) throws SlickException {
      if (thermal > 0 && this.mobFlags.getAllowThermal() && Utilities.randomInt(this.mobFlags.getThermalIncreaseTempChance()) == 0) {
         this.increaseBodyTemp(thermal);
      }

      if (sleep > 0 && this.mobFlags.getAllowSleep()) {
         this.runEnergyDecayRateTick(sleep);
      }

      if (hunger > 0 && this.mobFlags.getAllowHunger()) {
         this.runHungerDecayRateTick(hunger);
      }

      if (thirst > 0 && this.mobFlags.getAllowThirst()) {
         this.runThirstDecayRateTick(thirst);
      }

      if (faith > 0 && this.mobFlags.getAllowFaith()) {
         this.runFaithDecayRateTick(faith);
      }
   }

   public MobBase.MobPosition getPosition() {
      return this.position;
   }

   public void setPosition(MobBase.MobPosition p) {
      this.position = p;
   }

   protected void setAnimation(MobSprite.MobAnimationFrames a) {
      this.sprite.setPosition(a);
   }

   public void setPositionFace() {
      if (this.carriedResourcesList.size() > 0) {
         switch (this.getPosition()) {
            case NORTH:
               this.setAnimation(MobSprite.MobAnimationFrames.HOLD_NORTH);
               break;
            case EAST:
               this.setAnimation(MobSprite.MobAnimationFrames.HOLD_EAST);
               break;
            case SOUTH:
               this.setAnimation(MobSprite.MobAnimationFrames.HOLD_SOUTH);
               break;
            case WEST:
               this.setAnimation(MobSprite.MobAnimationFrames.HOLD_WEST);
         }
      } else {
         switch (this.getPosition()) {
            case NORTH:
               this.setAnimation(MobSprite.MobAnimationFrames.FACE_NORTH);
               break;
            case EAST:
               this.setAnimation(MobSprite.MobAnimationFrames.FACE_EAST);
               break;
            case SOUTH:
               this.setAnimation(MobSprite.MobAnimationFrames.FACE_SOUTH);
               break;
            case WEST:
               this.setAnimation(MobSprite.MobAnimationFrames.FACE_WEST);
         }
      }
   }

   public void setPositionEat() {
      switch (this.getPosition()) {
         case NORTH:
            this.setAnimation(MobSprite.MobAnimationFrames.EAT_NORTH);
            break;
         case EAST:
            this.setAnimation(MobSprite.MobAnimationFrames.EAT_EAST);
            break;
         case SOUTH:
            this.setAnimation(MobSprite.MobAnimationFrames.EAT_SOUTH);
            break;
         case WEST:
            this.setAnimation(MobSprite.MobAnimationFrames.EAT_WEST);
      }
   }

   public void setPositionDrink() {
      switch (this.getPosition()) {
         case NORTH:
            this.setAnimation(MobSprite.MobAnimationFrames.DRINK_NORTH);
            break;
         case EAST:
            this.setAnimation(MobSprite.MobAnimationFrames.DRINK_EAST);
            break;
         case SOUTH:
            this.setAnimation(MobSprite.MobAnimationFrames.DRINK_SOUTH);
            break;
         case WEST:
            this.setAnimation(MobSprite.MobAnimationFrames.DRINK_WEST);
      }
   }

   public void setPositionDrinkBottle() {
      switch (this.getPosition()) {
         case NORTH:
            this.setAnimation(MobSprite.MobAnimationFrames.DRINK_BOTTLE_NORTH);
            break;
         case EAST:
            this.setAnimation(MobSprite.MobAnimationFrames.DRINK_BOTTLE_EAST);
            break;
         case SOUTH:
            this.setAnimation(MobSprite.MobAnimationFrames.DRINK_BOTTLE_SOUTH);
            break;
         case WEST:
            this.setAnimation(MobSprite.MobAnimationFrames.DRINK_BOTTLE_WEST);
      }
   }

   public void setPositionWalk() {
      if (this.carriedResourcesList.size() > 0) {
         switch (this.getPosition()) {
            case NORTH:
               this.setAnimation(MobSprite.MobAnimationFrames.CARRY_NORTH);
               break;
            case EAST:
               this.setAnimation(MobSprite.MobAnimationFrames.CARRY_EAST);
               break;
            case SOUTH:
               this.setAnimation(MobSprite.MobAnimationFrames.CARRY_SOUTH);
               break;
            case WEST:
               this.setAnimation(MobSprite.MobAnimationFrames.CARRY_WEST);
         }
      } else {
         switch (this.getPosition()) {
            case NORTH:
               this.setAnimation(MobSprite.MobAnimationFrames.WALK_NORTH);
               break;
            case EAST:
               this.setAnimation(MobSprite.MobAnimationFrames.WALK_EAST);
               break;
            case SOUTH:
               this.setAnimation(MobSprite.MobAnimationFrames.WALK_SOUTH);
               break;
            case WEST:
               this.setAnimation(MobSprite.MobAnimationFrames.WALK_WEST);
         }
      }
   }

   public void setPositionAttack() {
      switch (this.getPosition()) {
         case NORTH:
            this.setAnimation(MobSprite.MobAnimationFrames.ATTACK_NORTH);
            break;
         case EAST:
            this.setAnimation(MobSprite.MobAnimationFrames.ATTACK_EAST);
            break;
         case SOUTH:
            this.setAnimation(MobSprite.MobAnimationFrames.ATTACK_SOUTH);
            break;
         case WEST:
            this.setAnimation(MobSprite.MobAnimationFrames.ATTACK_WEST);
      }
   }

   public void setPositionSleep() {
      switch (this.getPosition()) {
         case NORTH:
            this.setAnimation(MobSprite.MobAnimationFrames.SLEEP_NORTH);
            break;
         case EAST:
            this.setAnimation(MobSprite.MobAnimationFrames.SLEEP_EAST);
            break;
         case SOUTH:
            this.setAnimation(MobSprite.MobAnimationFrames.SLEEP_SOUTH);
            break;
         case WEST:
            this.setAnimation(MobSprite.MobAnimationFrames.SLEEP_WEST);
      }
   }

   public void setPositionInteract(boolean useTool, boolean ground) {
      if (useTool) {
         if (ground) {
            switch (this.getPosition()) {
               case NORTH:
                  this.setAnimation(MobSprite.MobAnimationFrames.INTERACT_GROUND_TOOL_NORTH);
                  break;
               case EAST:
                  this.setAnimation(MobSprite.MobAnimationFrames.INTERACT_GROUND_TOOL_EAST);
                  break;
               case SOUTH:
                  this.setAnimation(MobSprite.MobAnimationFrames.INTERACT_GROUND_TOOL_SOUTH);
                  break;
               case WEST:
                  this.setAnimation(MobSprite.MobAnimationFrames.INTERACT_GROUND_TOOL_WEST);
            }
         } else {
            switch (this.getPosition()) {
               case NORTH:
                  this.setAnimation(MobSprite.MobAnimationFrames.INTERACT_TOOL_NORTH);
                  break;
               case EAST:
                  this.setAnimation(MobSprite.MobAnimationFrames.INTERACT_TOOL_EAST);
                  break;
               case SOUTH:
                  this.setAnimation(MobSprite.MobAnimationFrames.INTERACT_TOOL_SOUTH);
                  break;
               case WEST:
                  this.setAnimation(MobSprite.MobAnimationFrames.INTERACT_TOOL_WEST);
            }
         }
      } else if (ground) {
         switch (this.getPosition()) {
            case NORTH:
               this.setAnimation(MobSprite.MobAnimationFrames.INTERACT_GROUND_NORTH);
               break;
            case EAST:
               this.setAnimation(MobSprite.MobAnimationFrames.INTERACT_GROUND_EAST);
               break;
            case SOUTH:
               this.setAnimation(MobSprite.MobAnimationFrames.INTERACT_GROUND_SOUTH);
               break;
            case WEST:
               this.setAnimation(MobSprite.MobAnimationFrames.INTERACT_GROUND_WEST);
         }
      } else {
         switch (this.getPosition()) {
            case NORTH:
               this.setAnimation(MobSprite.MobAnimationFrames.INTERACT_NORTH);
               break;
            case EAST:
               this.setAnimation(MobSprite.MobAnimationFrames.INTERACT_EAST);
               break;
            case SOUTH:
               this.setAnimation(MobSprite.MobAnimationFrames.INTERACT_SOUTH);
               break;
            case WEST:
               this.setAnimation(MobSprite.MobAnimationFrames.INTERACT_WEST);
         }
      }
   }

   public void setPositionFlee() {
      switch (this.getPosition()) {
         case NORTH:
            this.setAnimation(MobSprite.MobAnimationFrames.FLEE_NORTH);
            break;
         case EAST:
            this.setAnimation(MobSprite.MobAnimationFrames.FLEE_EAST);
            break;
         case SOUTH:
            this.setAnimation(MobSprite.MobAnimationFrames.FLEE_SOUTH);
            break;
         case WEST:
            this.setAnimation(MobSprite.MobAnimationFrames.FLEE_WEST);
      }
   }

   public void setPositionDeath() {
      switch (this.getPosition()) {
         case NORTH:
            this.setAnimation(MobSprite.MobAnimationFrames.DEATH_NORTH);
            break;
         case EAST:
            this.setAnimation(MobSprite.MobAnimationFrames.DEATH_EAST);
            break;
         case SOUTH:
            this.setAnimation(MobSprite.MobAnimationFrames.DEATH_SOUTH);
            break;
         case WEST:
            this.setAnimation(MobSprite.MobAnimationFrames.DEATH_WEST);
      }
   }

   public void setPositionDecay() {
      switch (this.getPosition()) {
         case NORTH:
            this.setAnimation(MobSprite.MobAnimationFrames.DECAY_NORTH);
            break;
         case EAST:
            this.setAnimation(MobSprite.MobAnimationFrames.DECAY_EAST);
            break;
         case SOUTH:
            this.setAnimation(MobSprite.MobAnimationFrames.DECAY_SOUTH);
            break;
         case WEST:
            this.setAnimation(MobSprite.MobAnimationFrames.DECAY_WEST);
      }
   }

   public void setPositionHit() {
      switch (this.getPosition()) {
         case NORTH:
            this.sprite.forcePlayAnimation(MobSprite.MobAnimationFrames.HIT_NORTH);
            break;
         case EAST:
            this.sprite.forcePlayAnimation(MobSprite.MobAnimationFrames.HIT_EAST);
            break;
         case SOUTH:
            this.sprite.forcePlayAnimation(MobSprite.MobAnimationFrames.HIT_SOUTH);
            break;
         case WEST:
            this.sprite.forcePlayAnimation(MobSprite.MobAnimationFrames.HIT_WEST);
      }
   }

   public void setPositionBowIdle() {
      switch (this.getPosition()) {
         case NORTH:
            this.setAnimation(MobSprite.MobAnimationFrames.BOW_IDLE_NORTH);
            break;
         case EAST:
            this.setAnimation(MobSprite.MobAnimationFrames.BOW_IDLE_EAST);
            break;
         case SOUTH:
            this.setAnimation(MobSprite.MobAnimationFrames.BOW_IDLE_SOUTH);
            break;
         case WEST:
            this.setAnimation(MobSprite.MobAnimationFrames.BOW_IDLE_WEST);
      }
   }

   public void setPositionBowFire() {
      switch (this.getPosition()) {
         case NORTH:
            this.setAnimation(MobSprite.MobAnimationFrames.BOW_FIRE_NORTH);
            break;
         case EAST:
            this.setAnimation(MobSprite.MobAnimationFrames.BOW_FIRE_EAST);
            break;
         case SOUTH:
            this.setAnimation(MobSprite.MobAnimationFrames.BOW_FIRE_SOUTH);
            break;
         case WEST:
            this.setAnimation(MobSprite.MobAnimationFrames.BOW_FIRE_WEST);
      }
   }

   public void setPositionPray() {
      switch (this.getPosition()) {
         case NORTH:
            this.setAnimation(MobSprite.MobAnimationFrames.PRAY_NORTH);
            break;
         case EAST:
            this.setAnimation(MobSprite.MobAnimationFrames.PRAY_EAST);
            break;
         case SOUTH:
            this.setAnimation(MobSprite.MobAnimationFrames.PRAY_SOUTH);
            break;
         case WEST:
            this.setAnimation(MobSprite.MobAnimationFrames.PRAY_WEST);
      }
   }

   public void setPositionPrayFinishing() {
      switch (this.getPosition()) {
         case NORTH:
            this.setAnimation(MobSprite.MobAnimationFrames.PRAY_FINISHING_NORTH);
            break;
         case EAST:
            this.setAnimation(MobSprite.MobAnimationFrames.PRAY_FINISHING_EAST);
            break;
         case SOUTH:
            this.setAnimation(MobSprite.MobAnimationFrames.PRAY_FINISHING_SOUTH);
            break;
         case WEST:
            this.setAnimation(MobSprite.MobAnimationFrames.PRAY_FINISHING_WEST);
      }
   }

   public void setPositionPickup() {
      switch (this.getPosition()) {
         case NORTH:
            this.setAnimation(MobSprite.MobAnimationFrames.PICKUP_NORTH);
            break;
         case EAST:
            this.setAnimation(MobSprite.MobAnimationFrames.PICKUP_EAST);
            break;
         case SOUTH:
            this.setAnimation(MobSprite.MobAnimationFrames.PICKUP_SOUTH);
            break;
         case WEST:
            this.setAnimation(MobSprite.MobAnimationFrames.PICKUP_WEST);
      }
   }

   public void setPositionLift() {
      if (this.carriedResourcesList.size() > 0) {
         switch (this.getPosition()) {
            case NORTH:
               this.setAnimation(MobSprite.MobAnimationFrames.LIFT_HAUL_NORTH);
               break;
            case EAST:
               this.setAnimation(MobSprite.MobAnimationFrames.LIFT_HAUL_EAST);
               break;
            case SOUTH:
               this.setAnimation(MobSprite.MobAnimationFrames.LIFT_HAUL_SOUTH);
               break;
            case WEST:
               this.setAnimation(MobSprite.MobAnimationFrames.LIFT_HAUL_WEST);
         }
      } else {
         switch (this.getPosition()) {
            case NORTH:
               this.setAnimation(MobSprite.MobAnimationFrames.LIFT_EQUIP_NORTH);
               break;
            case EAST:
               this.setAnimation(MobSprite.MobAnimationFrames.LIFT_EQUIP_EAST);
               break;
            case SOUTH:
               this.setAnimation(MobSprite.MobAnimationFrames.LIFT_EQUIP_SOUTH);
               break;
            case WEST:
               this.setAnimation(MobSprite.MobAnimationFrames.LIFT_EQUIP_WEST);
         }
      }
   }

   public void setPositionDrop() {
      switch (this.getPosition()) {
         case NORTH:
            this.setAnimation(MobSprite.MobAnimationFrames.DROP_NORTH);
            break;
         case EAST:
            this.setAnimation(MobSprite.MobAnimationFrames.DROP_EAST);
            break;
         case SOUTH:
            this.setAnimation(MobSprite.MobAnimationFrames.DROP_SOUTH);
            break;
         case WEST:
            this.setAnimation(MobSprite.MobAnimationFrames.DROP_WEST);
      }
   }

   private void updateOnFire() throws SlickException {
      if (this.onFireAmount > 0) {
         if (!this.dead && this.alpha > 0.0F) {
            SoundModule.SoundType getHit = this.mobFlags.getMobSoundSet(this.gender).getSoundGetHit();
            if (getHit != null && Utilities.randomInt(100) == 0) {
               this.sound.playSound(getHit, this.mobX, this.mobY);
            }
         }

         this.onFire = true;
         if (this.weather.isRaining()) {
            this.onFireAmount -= 3;
         } else {
            this.onFireAmount--;
         }

         int tileID = this.map.getTileId(this.tileX, this.tileY, 6);
         if (this.map.getMapTileLoader().isTileWater(tileID)
            && !this.map.isTileFrozen(this.tileX, this.tileY)
            && this.resource.getResourceValues()[this.tileX][this.tileY] > 0) {
            this.onFireAmount -= 5;
         }

         if (this.onFireAmount > 50) {
            this.particle
               .newParticle(
                  this.getCenterPixelX(true) + Utilities.randomInt(-2, 2),
                  this.getCenterPixelY(true) + Utilities.randomInt(-6, 6),
                  ParticleModule.ParticleType.FIRE_SPECK,
                  ParticleModule.ParticleSet.FIRE_RED
               );
            if (Utilities.randomInt(3) == 0) {
               this.particle
                  .newParticle(
                     this.getCenterPixelX(true) + Utilities.randomInt(-2, 2),
                     this.getCenterPixelY(true) + Utilities.randomInt(-6, 6),
                     ParticleModule.ParticleType.SMOKE_SPECK,
                     ParticleModule.ParticleSet.GRAY
                  );
            }
         }

         if (Utilities.randomInt(50) == 0) {
            if (this.mobFlags.getAllowThermal()) {
               this.increaseBodyTemp(2);
            }

            int decreaseBy = (int)(this.hitPointsMax * 0.02);
            if (decreaseBy < 1) {
               decreaseBy = 1;
            }

            this.decreaseHitPoints(decreaseBy, MobBase.DamageType.FIRE, MobBase.DamageCause.ON_FIRE);
         }
      } else {
         this.onFire = false;
         this.onFireAmount = 0;
      }
   }

   private void updateFrozen() throws SlickException {
      if (this.frozenAmount > 0) {
         this.frozen = true;
         if (this.weather.isRaining()) {
            this.frozenAmount -= 3;
         } else {
            this.frozenAmount--;
         }

         if (Utilities.randomInt(20) == 0) {
            this.particle
               .newParticle(
                  this.getCenterPixelX(true) + Utilities.randomInt(-2, 2),
                  this.getCenterPixelY(true) + Utilities.randomInt(-6, 6),
                  ParticleModule.ParticleType.SMOKE_SPECK,
                  ParticleModule.ParticleSet.FIRE_BLUE
               );
         }

         if (Utilities.randomInt(150) == 0) {
            if (this.mobFlags.getAllowThermal()) {
               this.decreaseBodyTemp(2);
            }

            int decreaseBy = (int)(this.hitPointsMax * 0.02);
            if (decreaseBy < 1) {
               decreaseBy = 1;
            }

            this.decreaseHitPoints(decreaseBy, MobBase.DamageType.ICE, MobBase.DamageCause.FROZEN);
         }
      } else {
         this.frozen = false;
         this.frozenAmount = 0;
      }
   }

   private void updatePoisoned() throws SlickException {
      if (this.poisonedAmount > 0) {
         this.poisoned = true;
         this.poisonedAmount--;
         if (Utilities.randomInt(20) == 0) {
            this.particle
               .newParticle(
                  this.getCenterPixelX(true) + Utilities.randomInt(-2, 2),
                  this.getCenterPixelY(true) + Utilities.randomInt(-6, 6),
                  ParticleModule.ParticleType.SMOKE_SPECK,
                  ParticleModule.ParticleSet.FIRE_GREEN
               );
         }

         if (Utilities.randomInt(750) == 0) {
            int decreaseBy = (int)(this.hitPointsMax * 0.02);
            if (decreaseBy < 1) {
               decreaseBy = 1;
            }

            this.decreaseHitPoints(decreaseBy, MobBase.DamageType.FROM_POISON, MobBase.DamageCause.FROM_POISON);
         }
      } else {
         this.poisoned = false;
         this.poisonedAmount = 0;
      }
   }

   private void updateDysentery() throws SlickException {
      if (this.dysenteryAmount > 0) {
         this.dysentery = true;
         this.dysenteryAmount--;
         if (Utilities.randomInt(1050) == 0) {
            int decreaseBy = (int)(this.hitPointsMax * 0.02);
            if (decreaseBy < 1) {
               decreaseBy = 1;
            }

            this.decreaseHitPoints(decreaseBy, MobBase.DamageType.FROM_DYSENTERY, MobBase.DamageCause.FROM_DYSENTERY);
         }
      } else {
         this.dysentery = false;
         this.dysenteryAmount = 0;
      }
   }

   private void updateBlighted() throws SlickException {
      if (this.blightAmount > 0) {
         this.blighted = true;
         this.blightAmount--;
         if (Utilities.randomInt(10) == 0) {
            this.particle
               .newParticle(
                  this.getCenterPixelX(true) + Utilities.randomInt(-2, 2),
                  this.getCenterPixelY(true) + Utilities.randomInt(-6, 6),
                  ParticleModule.ParticleType.SMOKE_SPECK,
                  ParticleModule.ParticleSet.BLACK
               );
         }

         if (Utilities.randomInt(2400) == 0) {
            int decreaseBy = (int)(this.hitPointsMax * 0.02);
            if (decreaseBy < 1) {
               decreaseBy = 1;
            }

            this.decreaseHitPoints(decreaseBy, MobBase.DamageType.FROM_BLIGHT, MobBase.DamageCause.FROM_BLIGHT);
         }

         if (this.blightAmount > 500 && Utilities.randomInt(200) == 0) {
            for (MobBase m : this.mob.getMobsNearby(this.tileX, this.tileY, 2)) {
               if (!m.isBlighted() && Utilities.randomInt(3) == 0) {
                  m.increaseBlightAmount((int)(this.getBlightAmount() * 0.5F));
               }
            }
         }
      } else {
         this.blighted = false;
         this.blightAmount = 0;
      }
   }

   private void updateBoosted() throws SlickException {
      if (this.boosted && Utilities.randomInt(2) == 0) {
         this.particle
            .newParticle(
               this.getCenterPixelX(true) + Utilities.randomInt(-2, 2),
               this.getCenterPixelY(true) + Utilities.randomInt(-6, 6),
               ParticleModule.ParticleType.MAGIC_SMOKE_SPECK,
               ParticleModule.ParticleSet.MAGIC_BLUE
            );
      }
   }

   public void takeDamage(int i, float x, float y, MobBase.DamageType type, MobBase.DamageCause cause) throws SlickException {
      this.takeDamage(i, x, y, null, null, type, cause, false);
   }

   public void takeDamage(int i, float x, float y, MobBase m, MobBase.DamageType type, MobBase.DamageCause cause, boolean blockFightOrFlight) throws SlickException {
      this.takeDamage(i, x, y, m, null, type, cause, blockFightOrFlight);
   }

   public void takeDamage(int i, float x, float y, ObjectBase o, MobBase.DamageType type, MobBase.DamageCause cause) throws SlickException {
      this.takeDamage(i, x, y, null, o, type, cause, false);
   }

   public void takeDamage(int i, float x, float y, MobBase m, MobBase.DamageType type, MobBase.DamageCause cause) throws SlickException {
      this.takeDamage(i, x, y, m, null, type, cause, false);
   }

   public void takeDamage(int d, float x, float y, MobBase m, ObjectBase o, MobBase.DamageType type, MobBase.DamageCause cause) throws SlickException {
      this.takeDamage(d, x, y, m, o, type, cause, false);
   }

   public void takeDamage(int d, float x, float y, MobBase m, ObjectBase o, MobBase.DamageType type, MobBase.DamageCause cause, boolean blockFightOrFlight) throws SlickException {
      if (!this.dead && !this.mobFlags.isInvulnerable()) {
         SoundModule.SoundType getHit = this.mobFlags.getMobSoundSet(this.gender).getSoundGetHit();
         if (getHit != null && Utilities.randomInt(3) == 0) {
            this.sound.playSound(getHit, this.mobX, this.mobY);
         }

         float damageOut = d;
         if (this.inventoryShieldSlot != null) {
            int deflectChance = this.inventoryShieldSlot.getType().getTemplate().getDeflectChance();
            if (deflectChance > 0 && Utilities.randomInt(deflectChance) == 0) {
               this.inventoryShieldSlot.decreaseUses(d);
               return;
            }
         }

         double armorReductionMultiplier = 0.0;
         if (this.inventoryHeadSlot != null) {
            armorReductionMultiplier += this.inventoryHeadSlot.getType().getTemplate().getArmorPercent();
            this.inventoryHeadSlot.decreaseUses(d);
         }

         if (this.inventoryChestSlot != null) {
            armorReductionMultiplier += this.inventoryChestSlot.getType().getTemplate().getArmorPercent();
            this.inventoryChestSlot.decreaseUses(d);
         }

         if (this.mobJobType != null) {
            armorReductionMultiplier += this.mobJobType.getJob().getBonusDefensePercent();
         }

         if (this.mobGroup == MobBase.MobGroup.VILLAGER && this.perk.getPerkTypeTotal(PerkModule.PerkType.DAMAGE_RESISTANCE) > 0.0) {
            armorReductionMultiplier += armorReductionMultiplier * this.perk.getPerkTypeTotal(PerkModule.PerkType.DAMAGE_RESISTANCE);
         }

         if (armorReductionMultiplier > 95.0) {
            armorReductionMultiplier = 95.0;
         }

         damageOut = (float)(damageOut - damageOut * (armorReductionMultiplier / 100.0));
         if (this.mobFlags.getDamageResistances().containsKey(type)) {
            damageOut = (float)(damageOut - damageOut * (this.mobFlags.getDamageResistances().get(type).intValue() / 100.0));
         }

         if (this.mobFlags.getDamageVulnerabilities().containsKey(type)) {
            damageOut = (float)(damageOut + damageOut * (this.mobFlags.getDamageVulnerabilities().get(type).intValue() / 100.0));
         }

         if ((int)damageOut <= 0) {
            damageOut = 1.0F;
         }

         if (this.mobFlags.getAllowOnFire() && (type == MobBase.DamageType.FIRE || type == MobBase.DamageType.MAGIC_FIRE)) {
            this.increaseOnFireAmount((int)damageOut * 15);
         }

         if (this.mobFlags.getAllowPoisoned() && type == MobBase.DamageType.POISON) {
            this.increasePoisonedAmount((int)damageOut * 300);
         }

         if (this.mobFlags.getAllowDysentery() && type == MobBase.DamageType.DYSENTERY) {
            this.increaseDysenteryAmount((int)damageOut * 300);
         }

         if (this.mobFlags.getAllowBlighted() && type == MobBase.DamageType.BLIGHT) {
            this.increaseBlightAmount((int)damageOut * 300);
         }

         if (this.mobFlags.getAllowAging() && this.mobFlags.getAgingMaximumAge() > 0) {
            this.runAgingConstitutionDecayRateTick((int)damageOut * 100);
         }

         if (this.mobFlags.getAllowThermal()) {
            if (type == MobBase.DamageType.FIRE || type == MobBase.DamageType.MAGIC_FIRE) {
               this.increaseBodyTemp((int)damageOut / 2);
            } else if (type == MobBase.DamageType.ICE || type == MobBase.DamageType.MAGIC_ICE) {
               this.decreaseBodyTemp((int)damageOut / 2);
            }
         }

         this.decreaseHitPoints((int)damageOut, m, o, type, cause);
         if (this.hitPoints > 0 && !this.onFire) {
            this.setPositionHit();
         }

         if (this.mobFlags.getBloodType() != null) {
            int bloodAmount = 0;
            int hpLeft = 10 - (int)Math.ceil((float)this.hitPoints / this.hitPointsMax * 10.0F);
            if (hpLeft < 8) {
               bloodAmount = (int)damageOut / 2 * (hpLeft / 2) + 1;
               this.bleedingAmount += hpLeft * 100;
            }

            double xDiff = this.getCenterPixelX(true) - x;
            double yDiff = this.getCenterPixelY(true) - y;
            float angle = (float)Math.toDegrees(Math.atan2(yDiff, xDiff));

            for (int p = 0; p < bloodAmount; p++) {
               this.particle
                  .newParticle(
                     this.getCenterPixelX(true), this.getCenterPixelY(true), angle, ParticleModule.ParticleType.BLOOD_SPECK, this.mobFlags.getBloodType()
                  );
            }
         }

         if (!blockFightOrFlight && this.mobFlags.getAllowCombat() && this.hitPoints > 0) {
            this.interuptAndAttack(m, o);
         }
      }
   }

   public void interuptAndAttack(MobBase m, ObjectBase o) throws SlickException {
      if (this.mobFlags.combatCanAttackMobs() && m != null && m.getMobGroup() != this.mobGroup && this.assignment.getPrimaryMob() == null) {
         this.activeNode.switchNode(this.aiCombatMobs);
         this.assignment.setPrimaryMob(m);
         this.assignment.setPrimaryCoordinates(OrderedPair.getOrderedPair(m.getTileX(), m.getTileY()));
         this.assignment.setAssignmentType(Assignment.AssignmentType.COMBAT);
         this.assignment.finalizeAssignment();
      } else if (this.mobFlags.combatCanAttackBuildings()
         && o != null
         && o.getObjectFlags().getMobGroup() != this.mobGroup
         && this.assignment.getPrimaryObject() == null
         && this.assignment.getPrimaryMob() == null) {
         this.activeNode.switchNode(this.aiCombatBuildings);
         this.assignment.setPrimaryObject(o);
         this.assignment.setPrimaryCoordinates(o.getRandomCoordinates());
         ArrayList<OrderedPair> allCoordinates = o.getAllCoordinates();
         this.assignment.setSecondaryCoordinates(allCoordinates.get(Utilities.randomInt(allCoordinates.size())));
         this.assignment.setAssignmentType(Assignment.AssignmentType.COMBAT);
         this.assignment.finalizeAssignment();
      }
   }

   public boolean canRangedAttack() {
      if (this.mobFlags.getAllowCombatRanged()) {
         return true;
      } else {
         ResourceBase rangedWeapon = this.getInventoryRangedWeaponSlot();
         if (rangedWeapon != null && rangedWeapon.getType().getTemplate().getRangedWeaponRequiredAmmo() == null) {
            return true;
         } else {
            ResourceBase ammo = this.getInventoryRangedWeaponAmmoSlot();
            return rangedWeapon != null && ammo != null && rangedWeapon.getType().getTemplate().getRangedWeaponRequiredAmmo() == ammo.getType();
         }
      }
   }

   public void prepareForMigrationModule() throws SlickException {
      if (this.mate != null) {
         this.mate.setMate(null);
         this.setMate(null);
      }

      if (this.owner != null) {
         this.owner.setOwner(null);
         this.setOwner(null);
      }

      if (this.insideHouse) {
         this.leaveBuilding();
      }

      for (MobBase m : this.mob.getMobArray()) {
         if (m.getAssignment().getPrimaryMob() == this || m.getAssignment().getSecondaryMob() == this) {
            m.interruptNode(m.getAIStart());
         }
      }

      if (this.mobFlags.getAllowWork()) {
         this.setMobJobType(MobFactory.getMobInstance(this.mobType).getMobJobType());
      }

      this.unassignFromBuildings();
   }

   public void unassignFromAll(boolean changeColors) throws SlickException {
      if (this.mate != null) {
         this.mate.setMate(null);
         this.setMate(null);
      }

      if (this.owner != null) {
         this.owner.setOwner(null);
         this.setOwner(null);
      }

      this.unboundGhost();
      this.unequipAndDropAll();
      if (this.insideHouse) {
         this.leaveBuilding();
      }

      for (MobBase m : this.mob.getMobArray()) {
         if (m.getAssignment().getPrimaryMob() == this || m.getAssignment().getSecondaryMob() == this) {
            m.interruptNode(m.getAIStart());
         }
      }

      if (this.mobFlags.getAllowWork()) {
         this.setMobJobType(MobFactory.getMobInstance(this.mobType).getMobJobType(), changeColors);
      }

      this.unassignFromBuildings();
   }

   public void unassignFromBuildings() {
      if (this.assignedHome != -1) {
         if (this.object.getObjects().get(this.assignedHome) != null) {
            this.object.getObjects().get(this.assignedHome).getOccupants().remove(this);
         }

         this.assignedHome = -1;
      }

      if (this.assignedSpawn != -1) {
         if (this.object.getObjects().get(this.assignedSpawn) != null) {
            this.object.getObjects().get(this.assignedSpawn).getCombobulatorSpawns().remove(this);
         }

         this.assignedSpawn = -1;
      }
   }

   public void generateDrops() throws SlickException {
      HashMap<ResourceModule.ResourceType, Integer> dropsList = this.mobFlags.getDropsList();
      if (dropsList.size() > 0) {
         ArrayList<ResourceModule.ResourceType> dropWeightList = new ArrayList<>();

         for (ResourceModule.ResourceType t : dropsList.keySet()) {
            int amount = dropsList.get(t);

            for (int x = 0; x < amount; x++) {
               dropWeightList.add(t);
            }
         }

         for (int x = 0; x < this.mobFlags.getDropsChance(); x++) {
            if (Utilities.randomInt(this.mobFlags.getDropsChance()) == 0) {
               ResourceModule.ResourceType typeOut = dropWeightList.get(Utilities.randomInt(dropWeightList.size()));
               if (!this.resource.isResourcesOnGround(this.tileX, this.tileY)) {
                  this.resource.createResourceOnGround(typeOut, ResourceModule.ResourceColorSet.DEFAULT, this.tileX, this.tileY);
               } else {
                  OrderedPair resourceCoordinates = this.resource.findOpenSpace(this.tileX, this.tileY, MapData.BlockMapGroup.STANDARD);
                  this.resource
                     .createResourceOnGround(typeOut, ResourceModule.ResourceColorSet.DEFAULT, resourceCoordinates.getX(), resourceCoordinates.getY());
               }
            }
         }
      }
   }

   protected void assignHome(int newHome) {
      if (this.assignedHome != -1) {
         this.object.getObjects().get(this.assignedHome).getOccupants().remove(this);
      }

      this.assignedHome = newHome;
      this.object.getObjects().get(newHome).getOccupants().add(this);
   }

   protected void assignSpawn(int newSpawn) {
      if (this.assignedSpawn != -1) {
         this.object.getObjects().get(this.assignedSpawn).getCombobulatorSpawns().remove(this);
      }

      this.assignedSpawn = newSpawn;
      this.object.getObjects().get(this.assignedSpawn).getCombobulatorSpawns().add(this);
   }

   public void consumeResource(ResourceBase r) throws SlickException {
      this.consumeResource(r, 1);
   }

   public void consumeResource(ResourceBase r, int i) throws SlickException {
      if (i > r.getUses()) {
         i = r.getUses();
      }

      r.decreaseUses(i);
      this.increaseHunger(r.getType().getTemplate().getFoodPerUse() * i);
      this.drinkWater(r.getType().getTemplate().getWaterPerUse() * i);
      this.increaseTrash(r.getType().getTemplate().getTrashPerUse() * i);
      if (this.mobFlags.allowProduceEggs() && r.getType().getTemplate().getFoodPerUse() > 0) {
         this.increaseEgg(r.getType().getTemplate().getFoodPerUse() * 4);
      }
   }

   public void drinkWater(int i) {
      this.increaseThirst(i);
      if (this.mobFlags.getAllowThermal()) {
         if (this.mobFlags.getCanThermalOverheatCritical() && this.bodyTemp >= this.mobFlags.getThermalOverheatCritical()) {
            this.decreaseBodyTemp(i * 3);
         } else if (this.mobFlags.getCanThermalOverheat() && this.bodyTemp >= this.mobFlags.getThermalOverheat()) {
            this.decreaseBodyTemp(i * 2);
         } else if (this.bodyTemp > this.bodyTempMax / 2) {
            this.decreaseBodyTemp(i);
         }
      }
   }

   protected void calculatedCurrentVelocity() {
      this.currentVelocity = this.baseVelocity;
      float mCost = this.map.getMapData().getMovementCost(this.mobFlags.getBlockMapGroup(), this.tileX, this.tileY);
      mCost = (1000.0F - mCost) / 1000.0F;
      this.currentVelocity = this.currentVelocity + this.currentVelocity * mCost;
      if (this.carriedResourcesList.size() > 0) {
         this.currentVelocity *= 0.8F;
      }

      if (this.pregnant) {
         this.currentVelocity *= 0.8F;
      }

      if (this.poisoned || this.blighted || this.dysentery) {
         this.currentVelocity *= 0.6F;
      }

      this.currentVelocity = this.currentVelocity * this.weather.getMovementSpeedReductionTotal();
      if (this.mobFlags.getAllowThermal()) {
         if (this.mobFlags.getCanThermalFreezeCritical() && this.bodyTemp <= this.mobFlags.getThermalFreezeCritical()) {
            this.currentVelocity *= 0.8F;
         } else if (this.mobFlags.getCanThermalOverheatCritical() && this.bodyTemp >= this.mobFlags.getThermalOverheatCritical()) {
            this.currentVelocity *= 0.8F;
         } else if (this.mobFlags.getCanThermalFreeze() && this.bodyTemp <= this.mobFlags.getThermalFreeze()) {
            this.currentVelocity *= 0.9F;
         } else if (this.mobFlags.getCanThermalOverheat() && this.bodyTemp >= this.mobFlags.getThermalOverheat()) {
            this.currentVelocity *= 0.9F;
         }
      }

      if (this.assignment.getAssignmentType() == Assignment.AssignmentType.COMBAT) {
         this.currentVelocity *= 1.2F;
      }

      if (this.mobType == MobBase.MobType.HOLY_GOLEM && this.perk.getPerkTypeTotal(PerkModule.PerkType.HOLY_GOLEM_SPEED) > 0.0) {
         this.currentVelocity = (float)(this.currentVelocity + this.currentVelocity * this.perk.getPerkTypeTotal(PerkModule.PerkType.HOLY_GOLEM_SPEED));
      }

      if (this.currentVelocity < 0.15F) {
         this.currentVelocity = 0.15F;
      }

      if (this.currentVelocity > 3.0F) {
         this.currentVelocity = 3.0F;
      }
   }

   public void moveMob(int goalX, int goalY) throws SlickException {
      if (this.tileX == goalX && this.tileY > goalY) {
         this.angle = -1.5707964F;
      } else if (this.tileX == goalX && this.tileY < goalY) {
         this.angle = 1.5707964F;
      } else if (this.tileX > goalX && this.tileY == goalY) {
         this.angle = 3.1415927F;
      } else if (this.tileX < goalX && this.tileY == goalY) {
         this.angle = 0.0F;
      } else if (this.tileX < goalX && this.tileY < goalY) {
         this.angle = 0.7853982F;
      } else if (this.tileX < goalX && this.tileY > goalY) {
         this.angle = -0.7853982F;
      } else if (this.tileX > goalX && this.tileY < goalY) {
         this.angle = 2.3561945F;
      } else if (this.tileX > goalX && this.tileY > goalY) {
         this.angle = -2.3561945F;
      }

      this.calculatedCurrentVelocity();
      if (this.tileX == goalX && this.tileY > goalY) {
         this.position = MobBase.MobPosition.NORTH;
      } else if ((this.tileX >= goalX || this.tileY <= goalY) && (this.tileX >= goalX || this.tileY != goalY) && (this.tileX >= goalX || this.tileY >= goalY)) {
         if (this.tileX == goalX && this.tileY < goalY) {
            this.position = MobBase.MobPosition.SOUTH;
         } else if (this.tileX > goalX && this.tileY < goalY || this.tileX > goalX && this.tileY == goalY || this.tileX > goalX && this.tileY > goalY) {
            this.position = MobBase.MobPosition.WEST;
         }
      } else {
         this.position = MobBase.MobPosition.EAST;
      }

      if (this.mobFlags.getMovementTrail()) {
         this.movementTrail();
      }

      this.mobX = this.mobX + FastMath.cos(this.angle) * this.currentVelocity;
      this.mobY = this.mobY + FastMath.sin(this.angle) * this.currentVelocity;
      this.road.walkOnRoads(this.tileX, this.tileY);
   }

   protected void movementTrail() {
      if (Utilities.randomInt(this.mobFlags.getMovementTrailChance()) == 0) {
         this.particle
            .newParticleGroup(
               this.getCenterPixelX(true) + Utilities.randomInt(-1, 1),
               this.getBottomPixelY(true) + Utilities.randomInt(-1, 1),
               ParticleModule.ParticleGroup.BLOOD_POOL,
               this.mobFlags.getBloodType()
            );
      }
   }

   public String getFormattedName(boolean usePrefix) {
      String nameOut = this.fullName;
      String colorOut = "$WHI0";
      if (this.gender == MobBase.MobGender.MALE) {
         colorOut = "$BLU1";
      } else if (this.gender == MobBase.MobGender.FEMALE) {
         colorOut = "$PIN1";
      }

      String resetName = Text.getVariableText("mobName");
      Text.setVariableText("mobName", nameOut);
      if (this.mobGroup == MobBase.MobGroup.MONSTER) {
         colorOut = "$RED1";
         if (usePrefix) {
            nameOut = Text.getText("mobName.article");
         }
      } else if (this.mobGroup == MobBase.MobGroup.GOLEM) {
         colorOut = "$YEL1";
         if (usePrefix) {
            nameOut = Text.getText("mobName.article");
         }
      } else if (this.mobGroup == MobBase.MobGroup.CHARMED_MONSTER) {
         colorOut = "$ORA1";
         if (usePrefix) {
            nameOut = Text.getText("mobName.article");
         }
      }

      Text.setVariableText("mobName", resetName);
      return colorOut + nameOut;
   }

   public void setFirstName(String name) {
      this.firstName = name;
      this.fullName = this.firstName + " " + this.lastName;
   }

   public void setLastName(String name) {
      this.lastName = name;
      this.fullName = this.firstName + " " + this.lastName;
   }

   protected void generateFirstName(MobModule.MobNameType nameType) {
      String[] fullNameUnparsed = null;
      List<String> twitchFullNames = this.mob.getMobTwitchData().getFullNamesList();
      if (Utilities.randomInt(2) == 0 && twitchFullNames.size() > 0) {
         fullNameUnparsed = twitchFullNames.get(Utilities.randomInt(twitchFullNames.size())).split(" ", 2);
         this.firstName = fullNameUnparsed[0];
         this.fullName = this.firstName;
         this.mob.getMobTwitchData().addUseList(this.fullName);
      } else {
         if (this.gender == MobBase.MobGender.MALE || this.gender == MobBase.MobGender.FEMALE) {
            fullNameUnparsed = this.mob.getRandomName(nameType, this.gender).split(" ", 2);
         } else if (Utilities.randomInt(2) == 0) {
            fullNameUnparsed = this.mob.getRandomName(nameType, MobBase.MobGender.MALE).split(" ", 2);
         } else {
            fullNameUnparsed = this.mob.getRandomName(nameType, MobBase.MobGender.FEMALE).split(" ", 2);
         }

         List<String> twitchFirstNames = this.mob.getMobTwitchData().getNamesList();
         if (Utilities.randomInt(2) == 0 && twitchFirstNames.size() > 0) {
            String nameOut = twitchFirstNames.get(Utilities.randomInt(twitchFirstNames.size()));
            this.firstName = nameOut;
            this.mob.getMobTwitchData().addUseList(nameOut);
         } else {
            this.firstName = fullNameUnparsed[0];
         }

         this.fullName = this.firstName;
      }

      this.lastName = "";
   }

   protected void generateFullName(MobModule.MobNameType nameType) {
      String[] fullNameUnparsed = null;
      List<String> twitchFullNames = this.mob.getMobTwitchData().getFullNamesList();
      if (Utilities.randomInt(2) == 0 && twitchFullNames.size() > 0) {
         fullNameUnparsed = twitchFullNames.get(Utilities.randomInt(twitchFullNames.size())).split(" ", 2);
         this.firstName = fullNameUnparsed[0];
         this.lastName = fullNameUnparsed[1];
         this.fullName = this.firstName + " " + this.lastName;
         this.mob.getMobTwitchData().addUseList(this.fullName);
      } else {
         if (this.gender == MobBase.MobGender.MALE || this.gender == MobBase.MobGender.FEMALE) {
            fullNameUnparsed = this.mob.getRandomName(nameType, this.gender).split(" ", 2);
         } else if (Utilities.randomInt(2) == 0) {
            fullNameUnparsed = this.mob.getRandomName(nameType, MobBase.MobGender.MALE).split(" ", 2);
         } else {
            fullNameUnparsed = this.mob.getRandomName(nameType, MobBase.MobGender.FEMALE).split(" ", 2);
         }

         List<String> twitchFirstNames = this.mob.getMobTwitchData().getNamesList();
         if (Utilities.randomInt(2) == 0 && twitchFirstNames.size() > 0) {
            String nameOut = twitchFirstNames.get(Utilities.randomInt(twitchFirstNames.size()));
            this.firstName = nameOut;
            this.mob.getMobTwitchData().addUseList(nameOut);
         } else {
            this.firstName = fullNameUnparsed[0];
         }

         this.lastName = fullNameUnparsed[1];
         this.fullName = this.firstName + " " + this.lastName;
      }
   }

   protected void generateChildFullName(MobModule.MobNameType nameType) {
      String[] fullNameUnparsed = null;
      if (this.gender == MobBase.MobGender.MALE || this.gender == MobBase.MobGender.FEMALE) {
         fullNameUnparsed = this.mob.getRandomName(nameType, this.gender).split(" ", 2);
      } else if (Utilities.randomInt(2) == 0) {
         fullNameUnparsed = this.mob.getRandomName(nameType, MobBase.MobGender.MALE).split(" ", 2);
      } else {
         fullNameUnparsed = this.mob.getRandomName(nameType, MobBase.MobGender.FEMALE).split(" ", 2);
      }

      this.firstName = fullNameUnparsed[0];
      this.lastName = this.mother.getLastName();
      this.fullName = this.firstName + " " + this.lastName;
   }

   public int combatGetCalculatedRandomMeleeDamage() {
      double damageMultiplier = 0.0;
      if (this.inventoryMeleeWeaponSlot != null) {
         damageMultiplier = this.inventoryMeleeWeaponSlot.getType().getTemplate().getDamagePercent();
      } else if (this.inventoryToolSlot != null) {
         damageMultiplier = this.inventoryToolSlot.getType().getTemplate().getDamagePercent();
      }

      damageMultiplier += this.mobJobType.getJob().getBonusDamagePercent();
      if (this.mobGroup == MobBase.MobGroup.VILLAGER && this.perk.getPerkTypeTotal(PerkModule.PerkType.MELEE_DAMAGE) > 0.0) {
         damageMultiplier += this.perk.getPerkTypeTotal(PerkModule.PerkType.MELEE_DAMAGE) * 100.0;
      }

      int randomStatsCalc = this.strength / 10 + this.dexterity / 20;
      if (randomStatsCalc <= 0) {
         randomStatsCalc = 1;
      }

      float randomDamage = Utilities.randomInt(randomStatsCalc) + 1;
      randomDamage = (float)(randomDamage + randomDamage * (damageMultiplier / 100.0));
      return (int)randomDamage;
   }

   public int combatGetCalculatedRandomRangedDamage() {
      double damageMultiplier = 0.0;
      if (this.inventoryRangedWeaponSlot != null) {
         if (this.inventoryRangedWeaponSlot.getType().getTemplate().getRangedWeaponRequiredAmmo() == null) {
            damageMultiplier = this.inventoryRangedWeaponSlot.getType().getTemplate().getDamagePercent();
         } else if (this.inventoryRangedWeaponSlot.getType().getTemplate().getRangedWeaponRequiredAmmo() != null
            && this.inventoryRangedWeaponSlot.getType().getTemplate().getRangedWeaponRequiredAmmo() == this.inventoryRangedWeaponAmmoSlot.getType()) {
            damageMultiplier = this.inventoryRangedWeaponSlot.getType().getTemplate().getDamagePercent();
         }
      }

      damageMultiplier += this.mobJobType.getJob().getBonusDamagePercent();
      if (this.mobGroup == MobBase.MobGroup.VILLAGER && this.perk.getPerkTypeTotal(PerkModule.PerkType.RANGED_DAMAGE) > 0.0) {
         damageMultiplier += this.perk.getPerkTypeTotal(PerkModule.PerkType.RANGED_DAMAGE) * 100.0;
      }

      int randomStatsCalc = this.strength / 20 + this.dexterity / 10;
      if (randomStatsCalc <= 0) {
         randomStatsCalc = 1;
      }

      float randomDamage = Utilities.randomInt(randomStatsCalc) + 1;
      randomDamage = (float)(randomDamage + randomDamage * (damageMultiplier / 100.0));
      return (int)randomDamage;
   }

   public int combatGetCalculatedAttackSpeed(boolean ranged) {
      float speedMultiplier = 0.0F;
      ResourceBase weapon = null;
      if (ranged) {
         weapon = this.inventoryRangedWeaponSlot;
      } else {
         weapon = this.inventoryMeleeWeaponSlot;
      }

      if (weapon != null) {
         speedMultiplier = weapon.getType().getTemplate().getCombatSpeedPercent();
      } else if (!ranged && this.inventoryToolSlot != null) {
         speedMultiplier = this.inventoryToolSlot.getType().getTemplate().getCombatSpeedPercent();
      }

      float attackSpeedBase = 100 - (this.strength / 30 + this.dexterity / 20);
      if (attackSpeedBase < 20.0F) {
         attackSpeedBase = 20.0F;
      }

      attackSpeedBase += attackSpeedBase * (speedMultiplier / 100.0F);
      return (int)attackSpeedBase;
   }

   public void awardXP(int XP) throws SlickException {
      if (this.level != 200) {
         this.increaseExperience(XP);

         for (int i = 0; i < XP / 2; i++) {
            this.particle
               .newParticle(
                  this.getCenterPixelX(true) + Utilities.randomInt(-4, 4),
                  this.getCenterPixelY(true) + Utilities.randomInt(12),
                  ParticleModule.ParticleType.MAGIC_SMOKE_SPECK,
                  ParticleModule.ParticleSet.MAGIC_GREEN
               );
            this.particle
               .newParticle(
                  this.getCenterPixelX(true) + Utilities.randomInt(-4, 4),
                  this.getCenterPixelY(true) + Utilities.randomInt(12),
                  ParticleModule.ParticleType.MAGIC_SMOKE_SPECK,
                  ParticleModule.ParticleSet.MAGIC_BLUE
               );
         }

         if (this.experience >= this.mobFlags.getExperienceMultiplier() * this.level) {
            this.decreaseExperience(this.mobFlags.getExperienceMultiplier() * this.level);
            this.levelUp(true);
            this.projectile.newExpandingFireRing(this.getCenterPixelX(true), this.getBottomPixelY(true), 24, 0.2F, ParticleModule.ParticleSet.MAGIC_GREEN);
            this.lighting.addShortTermLight(this.tileX, this.tileY, (byte)8, (byte)4, 128);
         }
      }
   }

   public void levelUp(boolean notifyPlayer) throws SlickException {
      int hitPointsMaxOld = this.hitPointsMax;
      this.increaseLevel(1, notifyPlayer);

      for (int x = 0; x < this.mobFlags.getStatsPerLevel(); x++) {
         boolean gainedStat = false;

         while (!gainedStat) {
            if (Utilities.randomInt(this.mobFlags.getStatStrengthChance()) == 0) {
               this.increaseStrength(1);
               gainedStat = true;
            } else if (Utilities.randomInt(this.mobFlags.getStatDexterityChance()) == 0) {
               this.increaseDexterity(1);
               gainedStat = true;
            }
         }
      }

      int hitPointsMaxDifference = this.hitPointsMax - hitPointsMaxOld;
      if (hitPointsMaxDifference > 0) {
         this.increaseHitPoints(hitPointsMaxDifference);
      }

      if (this.mobGroup == MobBase.MobGroup.VILLAGER && notifyPlayer) {
         this.sound.playSound(SoundModule.SoundType.VILLAGER_LEVEL_UP, this.mobX, this.mobY);
      }

      if (this.mobID != -1 && this.god.isInitialized() && this.mobFlags.getAllowGodExperience()) {
         this.god.increaseGodXP(0.25, GodModule.GodXPType.VILLAGER_LEVELED_UP);
      }
   }

   public byte[][] getMobReachMap() {
      return this.mobReachMap.getReachMap();
   }

   public boolean canReach(MobBase mobCheck, boolean checkRangeMap) {
      return this.mobReachMap.canReach(mobCheck, checkRangeMap);
   }

   public boolean canReach(ObjectBase objectCheck, boolean checkRangeMap) {
      return this.mobReachMap.canReach(objectCheck, checkRangeMap);
   }

   public boolean canReach(int tileX, int tileY, boolean checkRangeMap) {
      return this.mobReachMap.canReach(tileX, tileY, checkRangeMap);
   }

   public boolean canReach(OrderedPair o, boolean checkRangeMap) {
      return this.mobReachMap.canReach(o.getX(), o.getY(), checkRangeMap);
   }

   public boolean canReach(MobBase mobCheck) {
      return this.mobReachMap.canReach(mobCheck, false);
   }

   public boolean canReach(ResourceBase resourceCheck) {
      return this.mobReachMap.canReach(resourceCheck, false);
   }

   public boolean canReach(ObjectBase objectCheck) {
      return this.mobReachMap.canReach(objectCheck, false);
   }

   public boolean canReach(int tileX, int tileY) {
      return this.mobReachMap.canReach(tileX, tileY, false);
   }

   public boolean canReach(OrderedPair o) {
      return this.mobReachMap.canReach(o.getX(), o.getY(), false);
   }

   public void shareReachMap(byte[][] map) {
      this.mobReachMap.shareReachMap(map);
   }

   public void removeReachMap() {
      this.mobReachMap.removeReachMap();
   }

   public void buildNewReachMap() {
      this.mobReachMap.buildNewReachMap();
   }

   public void findReachMap() {
      this.mobReachMap.findReachMap();
   }

   public boolean intersects(Shape mouse) {
      return mouse.intersects(this.mobBox);
   }

   public boolean intersectsGUI(Shape mouse) {
      return mouse.intersects(this.mobBoxGUI);
   }

   public void unassignHomeBuilding() throws SlickException {
      this.assignedHome = -1;
   }

   public void unassignSpawnBuilding() throws SlickException {
      this.assignedSpawn = -1;
   }

   public void loadSync() throws SlickException {
      for (AIBase a : this.aiNodes) {
         if (a.getAIName().toString().equals(this.activeNodeName)) {
            this.activeNode = a;
            break;
         }
      }

      if (this.activeNode != null) {
         if (this.activeNodeData != null) {
            this.activeNode.loadRegionalProperties(this.activeNodeData);
         }

         this.activeNode.loadSync();
      }

      this.activeNodeName = null;
      this.activeNodeData = null;
      if (this.motherID != -1) {
         this.mother = this.mob.getMobHashMap().get(this.motherID);
      }

      if (this.fatherID != -1) {
         this.father = this.mob.getMobHashMap().get(this.fatherID);
      }

      if (this.socialChildFatherID != -1) {
         this.socialChildFather = this.mob.getMobHashMap().get(this.socialChildFatherID);
      }

      if (this.mateID != -1) {
         this.mate = this.mob.getMobHashMap().get(this.mateID);
      }

      if (this.ownerID != -1) {
         this.owner = this.mob.getMobHashMap().get(this.ownerID);
      }

      if (this.tradeTargetID != -1) {
         this.tradeTarget = this.mob.getMobHashMap().get(this.tradeTargetID);
      }

      this.assignment.loadSync();
      if (this.carriedResourcesListIDs != null) {
         for (int i : this.carriedResourcesListIDs) {
            this.addCarriedResource(this.resource.getResource(i));
         }
      }

      if (this.inventoryHeadSlotID != -1) {
         this.inventoryHeadSlot = this.resource.getResource(this.inventoryHeadSlotID);
      }

      if (this.inventoryChestSlotID != -1) {
         this.inventoryChestSlot = this.resource.getResource(this.inventoryChestSlotID);
      }

      if (this.inventoryToolSlotID != -1) {
         this.inventoryToolSlot = this.resource.getResource(this.inventoryToolSlotID);
      }

      if (this.inventoryMeleeWeaponSlotID != -1) {
         this.inventoryMeleeWeaponSlot = this.resource.getResource(this.inventoryMeleeWeaponSlotID);
      }

      if (this.inventoryShieldSlotID != -1) {
         this.inventoryShieldSlot = this.resource.getResource(this.inventoryShieldSlotID);
      }

      if (this.inventoryRecoverySlotID != -1) {
         this.inventoryRecoverySlot = this.resource.getResource(this.inventoryRecoverySlotID);
      }

      if (this.inventoryWaterSlotID != -1) {
         this.inventoryWaterSlot = this.resource.getResource(this.inventoryWaterSlotID);
      }

      if (this.inventoryFoodSlotID != -1) {
         this.inventoryFoodSlot = this.resource.getResource(this.inventoryFoodSlotID);
      }

      if (this.inventoryRangedWeaponSlotID != -1) {
         this.inventoryRangedWeaponSlot = this.resource.getResource(this.inventoryRangedWeaponSlotID);
      }

      if (this.inventoryRangedWeaponAmmoSlotID != -1) {
         this.inventoryRangedWeaponAmmoSlot = this.resource.getResource(this.inventoryRangedWeaponAmmoSlotID);
      }

      if (this.capturedResourceID != -1) {
         this.capturedResource = this.resource.getResource(this.capturedResourceID);
      }

      if (this.boundGhostBodyID != -1) {
         this.boundGhostBody = this.mob.getMobHashMap().get(this.boundGhostBodyID);
      }

      if (this.boundGhostResourceID != -1) {
         this.boundGhostResource = this.resource.getResource(this.boundGhostResourceID);
      }

      this.mobReachMap.buildNewReachMap();
   }

   public void loadSyncMigration(HashMap<Integer, MobBase> mobMap, HashMap<Integer, ResourceBase> resourceMap) throws SlickException {
      if (this.motherID != -1) {
         this.mother = mobMap.get(this.motherID);
      }

      if (this.fatherID != -1) {
         this.father = mobMap.get(this.fatherID);
      }

      if (this.socialChildFatherID != -1) {
         this.socialChildFather = mobMap.get(this.socialChildFatherID);
      }

      if (this.mateID != -1) {
         this.mate = mobMap.get(this.mateID);
      }

      if (this.ownerID != -1) {
         this.owner = mobMap.get(this.ownerID);
      }

      if (this.tradeTargetID != -1) {
         this.tradeTarget = mobMap.get(this.tradeTargetID);
      }

      if (this.carriedResourcesListIDs != null) {
         for (int i : this.carriedResourcesListIDs) {
            resourceMap.get(i).assign(this);
            this.carriedResourcesList.add(resourceMap.get(i));
         }
      }

      if (this.inventoryHeadSlotID != -1) {
         this.inventoryHeadSlot = resourceMap.get(this.inventoryHeadSlotID);
      }

      if (this.inventoryChestSlotID != -1) {
         this.inventoryChestSlot = resourceMap.get(this.inventoryChestSlotID);
      }

      if (this.inventoryToolSlotID != -1) {
         this.inventoryToolSlot = resourceMap.get(this.inventoryToolSlotID);
      }

      if (this.inventoryMeleeWeaponSlotID != -1) {
         this.inventoryMeleeWeaponSlot = resourceMap.get(this.inventoryMeleeWeaponSlotID);
      }

      if (this.inventoryShieldSlotID != -1) {
         this.inventoryShieldSlot = resourceMap.get(this.inventoryShieldSlotID);
      }

      if (this.inventoryRecoverySlotID != -1) {
         this.inventoryRecoverySlot = resourceMap.get(this.inventoryRecoverySlotID);
      }

      if (this.inventoryWaterSlotID != -1) {
         this.inventoryWaterSlot = resourceMap.get(this.inventoryWaterSlotID);
      }

      if (this.inventoryFoodSlotID != -1) {
         this.inventoryFoodSlot = resourceMap.get(this.inventoryFoodSlotID);
      }

      if (this.inventoryRangedWeaponSlotID != -1) {
         this.inventoryRangedWeaponSlot = resourceMap.get(this.inventoryRangedWeaponSlotID);
      }

      if (this.inventoryRangedWeaponAmmoSlotID != -1) {
         this.inventoryRangedWeaponAmmoSlot = resourceMap.get(this.inventoryRangedWeaponAmmoSlotID);
      }

      if (this.capturedResourceID != -1) {
         this.capturedResource = this.resource.getResource(this.capturedResourceID);
      }

      if (this.boundGhostBodyID != -1) {
         this.boundGhostBody = this.mob.getMobHashMap().get(this.boundGhostBodyID);
      }

      if (this.boundGhostResourceID != -1) {
         this.boundGhostResource = this.resource.getResource(this.boundGhostResourceID);
      }
   }

   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("mobID", this.mobID);
      if (this.activeNode != null) {
         dataMap.put("activeNodeName", this.activeNode.getAIName().toString());
         dataMap.put("activeNodeData", this.activeNode.getRegionalSaveData());
      }

      if (this.mobX != 0.0) {
         dataMap.put("mobX", this.mobX);
      }

      if (this.mobY != 0.0) {
         dataMap.put("mobY", this.mobY);
      }

      if (this.tileX != 0) {
         dataMap.put("tileX", this.tileX);
      }

      if (this.tileY != 0) {
         dataMap.put("tileY", this.tileY);
      }

      if (this.position != MobBase.MobPosition.NORTH) {
         dataMap.put("position", this.position.toString());
      }

      if (this.alpha != 0.0) {
         dataMap.put("alpha", this.alpha);
      }

      if (this.fadeIn) {
         dataMap.put("fadeIn", this.fadeIn);
      }

      if (this.fadeOut) {
         dataMap.put("fadeOut", this.fadeOut);
      }

      if (this.mother != null) {
         dataMap.put("motherID", this.mother.getID());
      }

      if (this.father != null) {
         dataMap.put("fatherID", this.father.getID());
      }

      if (this.gender != MobBase.MobGender.NONE) {
         dataMap.put("gender", this.gender.toString());
      }

      if (this.firstName != null) {
         dataMap.put("firstName", this.firstName);
      }

      if (this.lastName != null) {
         dataMap.put("lastName", this.lastName);
      }

      if (this.fullName != null) {
         dataMap.put("fullName", this.fullName);
      }

      if (this.mobType != null) {
         dataMap.put("mobType", this.mobType.toString());
      }

      if (this.mobGroup != null) {
         dataMap.put("mobGroup", this.mobGroup.toString());
      }

      if (this.hostileMobGroups.size() > 0) {
         dataMap.put("hostileMobGroups", dataMap.packEnumArray(this.hostileMobGroups));
      }

      if (this.friendlyMobGroups.size() > 0) {
         dataMap.put("friendlyMobGroups", dataMap.packEnumArray(this.friendlyMobGroups));
      }

      if (this.mobJobType != MobJobBase.MobJobType.NONE) {
         dataMap.put("mobJobType", this.mobJobType.toString());
      }

      for (int i = 0; i < this.colorLayers.length; i++) {
         if (!this.colorLayers[i].equals(Color.white)) {
            dataMap.put("colorLayers" + i, dataMap.packColor(this.colorLayers[i]));
         }
      }

      if (this.level != 0) {
         dataMap.put("level", this.level);
      }

      if (this.age != 0) {
         dataMap.put("age", this.age);
      }

      if (this.lifeExpectancy != 0) {
         dataMap.put("lifeExpectancy", this.lifeExpectancy);
      }

      if (this.experience != 0) {
         dataMap.put("experience", this.experience);
      }

      if (this.strength != 0) {
         dataMap.put("strength", this.strength);
      }

      if (this.dexterity != 0) {
         dataMap.put("dexterity", this.dexterity);
      }

      if (this.hitPoints != 0) {
         dataMap.put("hitPoints", this.hitPoints);
      }

      if (this.hitPointsMax != 0) {
         dataMap.put("hitPointsMax", this.hitPointsMax);
      }

      if (this.hunger != 0) {
         dataMap.put("hunger", this.hunger);
      }

      if (this.hungerMax != 0) {
         dataMap.put("hungerMax", this.hungerMax);
      }

      if (this.thirst != 0) {
         dataMap.put("thirst", this.thirst);
      }

      if (this.thirstMax != 0) {
         dataMap.put("thirstMax", this.thirstMax);
      }

      if (this.faith != 0) {
         dataMap.put("faith", this.faith);
      }

      if (this.faithMax != 0) {
         dataMap.put("faithMax", this.faithMax);
      }

      if (this.killedNameList.size() > 0) {
         dataMap.put("killedNameList", dataMap.packEnumArray(this.killedNameList));
      }

      if (this.kills != 0) {
         dataMap.put("kills", this.kills);
      }

      if (this.trash != 0) {
         dataMap.put("trash", this.trash);
      }

      if (this.trashMax != 0) {
         dataMap.put("trashMax", this.trashMax);
      }

      if (this.egg != 0) {
         dataMap.put("egg", this.egg);
      }

      if (this.eggMax != 0) {
         dataMap.put("eggMax", this.eggMax);
      }

      if (this.energy != 0) {
         dataMap.put("energy", this.energy);
      }

      if (this.energyMax != 0) {
         dataMap.put("energyMax", this.energyMax);
      }

      if (this.social != 0) {
         dataMap.put("social", this.social);
      }

      if (this.socialMax != 0) {
         dataMap.put("socialMax", this.socialMax);
      }

      if (this.bodyTemp != 0) {
         dataMap.put("bodyTemp", this.bodyTemp);
      }

      if (this.bodyTempMax != 0) {
         dataMap.put("bodyTempMax", this.bodyTempMax);
      }

      if (this.baseVelocity != 0.0F) {
         dataMap.put("baseVelocity", this.baseVelocity);
      }

      if (this.currentVelocity != 0.0F) {
         dataMap.put("currentVelocity", this.currentVelocity);
      }

      if (this.angle != 0.0) {
         dataMap.put("angle", this.angle);
      }

      if (this.carriedResourcesList.size() > 0) {
         dataMap.put("carriedResourcesListIDs", dataMap.packObjectArrayIDs(this.carriedResourcesList));
      }

      if (this.inventoryHeadSlot != null) {
         dataMap.put("inventoryHeadSlotID", this.inventoryHeadSlot.getID());
      }

      if (this.inventoryChestSlot != null) {
         dataMap.put("inventoryChestSlotID", this.inventoryChestSlot.getID());
      }

      if (this.inventoryToolSlot != null) {
         dataMap.put("inventoryToolSlotID", this.inventoryToolSlot.getID());
      }

      if (this.inventoryMeleeWeaponSlot != null) {
         dataMap.put("inventoryMeleeWeaponSlotID", this.inventoryMeleeWeaponSlot.getID());
      }

      if (this.inventoryShieldSlot != null) {
         dataMap.put("inventoryShieldSlotID", this.inventoryShieldSlot.getID());
      }

      if (this.inventoryRecoverySlot != null) {
         dataMap.put("inventoryRecoverySlotID", this.inventoryRecoverySlot.getID());
      }

      if (this.inventoryWaterSlot != null) {
         dataMap.put("inventoryWaterSlotID", this.inventoryWaterSlot.getID());
      }

      if (this.inventoryFoodSlot != null) {
         dataMap.put("inventoryFoodSlotID", this.inventoryFoodSlot.getID());
      }

      if (this.inventoryRangedWeaponSlot != null) {
         dataMap.put("inventoryRangedWeaponSlotID", this.inventoryRangedWeaponSlot.getID());
      }

      if (this.inventoryRangedWeaponAmmoSlot != null) {
         dataMap.put("inventoryRangedWeaponAmmoSlotID", this.inventoryRangedWeaponAmmoSlot.getID());
      }

      if (this.dropsBlocked) {
         dataMap.put("dropsBlocked", this.dropsBlocked);
      }

      if (this.assignment != null) {
         dataMap.put("assignment", this.assignment.getRegionalSaveData());
      }

      if (this.courierIdleWithResourcesDelayTick != 0) {
         dataMap.put("courierIdleWithResourcesDelayTick", this.courierIdleWithResourcesDelayTick);
      }

      if (this.agingRateTick != 0) {
         dataMap.put("agingRateTick", this.agingRateTick);
      }

      if (this.agingConstitutionDecayRateTick != 0) {
         dataMap.put("agingConstitutionDecayRateTick", this.agingConstitutionDecayRateTick);
      }

      if (this.healthRegenRateTick != 0) {
         dataMap.put("healthRegenRateTick", this.healthRegenRateTick);
      }

      if (this.healthRepairTimeTick != 0) {
         dataMap.put("healthRepairTimeTick", this.healthRepairTimeTick);
      }

      if (this.thermalAdjustRateTick != 0) {
         dataMap.put("thermalAdjustRateTick", this.thermalAdjustRateTick);
      }

      if (this.confusedTarget != null) {
         dataMap.put("confusedTarget", dataMap.packCoordinates(this.confusedTarget));
      }

      if (this.assignedHome != -1) {
         dataMap.put("assignedHome", this.assignedHome);
      }

      if (this.insideHouse) {
         dataMap.put("insideHouse", this.insideHouse);
      }

      if (this.sleepDelayTick != 0) {
         dataMap.put("sleepDelayTick", this.sleepDelayTick);
      }

      if (this.energyDecayRateTick != 0) {
         dataMap.put("energyDecayRateTick", this.energyDecayRateTick);
      }

      if (this.hungerHarvestTimeTick != 0) {
         dataMap.put("hungerHarvestTimeTick", this.hungerHarvestTimeTick);
      }

      if (this.hungerEatTimeTick != 0) {
         dataMap.put("hungerEatTimeTick", this.hungerEatTimeTick);
      }

      if (this.hungerDecayRateTick != 0) {
         dataMap.put("hungerDecayRateTick", this.hungerDecayRateTick);
      }

      if (this.hungerStarvationRateTick != 0) {
         dataMap.put("hungerStarvationRateTick", this.hungerStarvationRateTick);
      }

      if (this.thirstHarvestTimeTick != 0) {
         dataMap.put("thirstHarvestTimeTick", this.thirstHarvestTimeTick);
      }

      if (this.thirstDrinkTimeTick != 0) {
         dataMap.put("thirstDrinkTimeTick", this.thirstDrinkTimeTick);
      }

      if (this.thirstDecayRateTick != 0) {
         dataMap.put("thirstDecayRateTick", this.thirstDecayRateTick);
      }

      if (this.thirstDehydrationRateTick != 0) {
         dataMap.put("thirstDehydrationRateTick", this.thirstDehydrationRateTick);
      }

      if (this.faithDecayRateTick != 0) {
         dataMap.put("faithDecayRateTick", this.faithDecayRateTick);
      }

      if (this.socialBlessed) {
         dataMap.put("socialBlessed", this.socialBlessed);
      }

      if (this.socialChildBlessed) {
         dataMap.put("socialChildBlessed", this.socialChildBlessed);
      }

      if (this.socialCoitusDelayTick != 0) {
         dataMap.put("socialCoitusDelayTick", this.socialCoitusDelayTick);
      }

      if (this.socialPregnantDelayTick != 0) {
         dataMap.put("socialPregnantDelayTick", this.socialPregnantDelayTick);
      }

      if (this.socialPregnantCount != 0) {
         dataMap.put("socialPregnantCount", this.socialPregnantCount);
      }

      if (this.socialGestationDelayTick != 0) {
         dataMap.put("socialGestationDelayTick", this.socialGestationDelayTick);
      }

      if (this.socialRateTick != 0) {
         dataMap.put("socialRateTick", this.socialRateTick);
      }

      if (this.socialChildFather != null) {
         dataMap.put("socialChildFatherID", this.socialChildFather.getID());
      }

      if (this.mate != null) {
         dataMap.put("mateID", this.mate.getID());
      }

      if (this.owner != null) {
         dataMap.put("ownerID", this.owner.getID());
      }

      if (this.pregnant) {
         dataMap.put("pregnant", this.pregnant);
      }

      if (this.tradeTarget != null) {
         dataMap.put("tradeTargetID", this.tradeTarget.getID());
      }

      if (this.workDelayTick != 0) {
         dataMap.put("workDelayTick", this.workDelayTick);
      }

      if (this.workSpeedReductionBase != 0) {
         dataMap.put("workSpeedReductionBase", this.workSpeedReductionBase);
      }

      if (this.workFindExtraResourcesLastX != 0) {
         dataMap.put("workFindExtraResourcesLastX", this.workFindExtraResourcesLastX);
      }

      if (this.workFindExtraResourcesLastY != 0) {
         dataMap.put("workFindExtraResourcesLastY", this.workFindExtraResourcesLastY);
      }

      if (this.dead) {
         dataMap.put("dead", this.dead);
      }

      if (this.decaying) {
         dataMap.put("decaying", this.decaying);
      }

      if (this.decayTick != 0) {
         dataMap.put("decayTick", this.decayTick);
      }

      if (this.combatDelayTick != 0) {
         dataMap.put("combatDelayTick", this.combatDelayTick);
      }

      if (this.onFire) {
         dataMap.put("onFire", this.onFire);
      }

      if (this.onFireAmount != 0) {
         dataMap.put("onFireAmount", this.onFireAmount);
      }

      if (this.frozen) {
         dataMap.put("frozen", this.frozen);
      }

      if (this.frozenAmount != 0) {
         dataMap.put("frozenAmount", this.frozenAmount);
      }

      if (this.poisoned) {
         dataMap.put("poisoned", this.poisoned);
      }

      if (this.poisonedAmount != 0) {
         dataMap.put("poisonedAmount", this.poisonedAmount);
      }

      if (this.dysentery) {
         dataMap.put("dysentery", this.dysentery);
      }

      if (this.dysenteryAmount != 0) {
         dataMap.put("dysenteryAmount", this.dysenteryAmount);
      }

      if (this.blighted) {
         dataMap.put("blighted", this.blighted);
      }

      if (this.blightAmount != 0) {
         dataMap.put("blightAmount", this.blightAmount);
      }

      if (this.golemRequiresChargeAmount != 0) {
         dataMap.put("golemRequiresChargeAmount", this.golemRequiresChargeAmount);
      }

      if (this.assignedSpawn != -1) {
         dataMap.put("assignedSpawn", this.assignedSpawn);
      }

      if (this.idle) {
         dataMap.put("idle", this.idle);
      }

      if (this.grabbed) {
         dataMap.put("grabbed", this.grabbed);
      }

      if (this.grabbedDropTick != 0) {
         dataMap.put("grabbedDropTick", this.grabbedDropTick);
      }

      if (this.captured) {
         dataMap.put("captured", this.captured);
      }

      if (this.capturedResource != null) {
         dataMap.put("capturedResourceID", this.capturedResource.getID());
      }

      if (this.boundGhost) {
         dataMap.put("boundGhost", this.boundGhost);
      }

      if (this.boundGhostBody != null) {
         dataMap.put("boundGhostBodyID", this.boundGhostBody.getID());
      }

      if (this.boundGhostResource != null) {
         dataMap.put("boundGhostResourceID", this.boundGhostResource.getID());
      }

      if (this.boosted) {
         dataMap.put("boosted", this.boosted);
      }

      if (this.diesSlowlyRateTick != 0) {
         dataMap.put("diesSlowlyRateTick", this.diesSlowlyRateTick);
      }

      if (this.bleedingAmount != 0) {
         dataMap.put("bleedingAmount", this.bleedingAmount);
      }

      if (this.noSelect) {
         dataMap.put("noSelect", this.noSelect);
      }

      if (this.lastDamageType != null) {
         dataMap.put("lastDamageType", this.lastDamageType.toString());
      }

      if (this.lastDamageCause != null) {
         dataMap.put("lastDamageCause", this.lastDamageCause.toString());
      }

      if (this.lastDamageMob != null) {
         dataMap.put("lastDamageMobID", this.lastDamageMob.getID());
      }

      if (this.lastDamageObject != null) {
         dataMap.put("lastDamageObjectID", this.lastDamageObject.getID());
      }

      return dataMap;
   }

   public void leaveBuilding() {
      this.fadeIn();
      this.insideHouse = false;
   }

   public void enterBuilding() {
      this.fadeOut();
      this.insideHouse = true;
   }

   public boolean isAvailable() {
      return !this.insideHouse && !this.grabbed && !this.dead && !this.captured;
   }

   public void fadeIn() {
      this.fadeIn = true;
      this.fadeOut = false;
   }

   public void fadeOut() {
      this.fadeIn = false;
      this.fadeOut = true;
   }

   public void move(OrderedPair o) {
      this.mobX = o.getX() * 16;
      this.mobY = o.getY() * 16;
      this.tileUpdateForced();
      if (this.getMobReachMap() == null || !this.canReach(o.getX(), o.getY())) {
         this.removeReachMap();
         this.buildNewReachMap();
      }

      if (this.hasCarriedResources()) {
         for (ResourceBase r : this.carriedResourcesList) {
            r.forcePositionMob();
         }
      }
   }

   public int getCenterPixelX(boolean useMap) {
      return useMap ? (int)(this.mobX + this.sprite.getSpriteWidth() / 2) : this.sprite.getSpriteWidth() / 2;
   }

   public int getCenterPixelY(boolean useMap) {
      return useMap ? (int)(this.mobY + this.sprite.getSpriteHeight() / 2) : this.sprite.getSpriteHeight() / 2;
   }

   public int getTopPixelY(boolean useMap) {
      return useMap ? (int)this.mobY : 0;
   }

   public int getBottomPixelY(boolean useMap) {
      return useMap ? (int)(this.mobY + this.sprite.getSpriteHeight()) : this.sprite.getSpriteHeight();
   }

   public int getLeftPixelX(boolean useMap) {
      return useMap ? (int)this.mobX : 0;
   }

   public int getRightPixelX(boolean useMap) {
      return useMap ? (int)(this.mobX + this.sprite.getSpriteWidth()) : this.sprite.getSpriteWidth();
   }

   public void poke() throws SlickException {
      if (this.dead) {
         this.runDecayTick(20000);
         this.decay();
         if (this.getDecayTick() >= 200000) {
            this.sound.playSound(SoundModule.SoundType.POP, this.getCenterPixelX(true), this.getCenterPixelY(true));

            for (int x = 0; x < 5; x++) {
               this.projectile
                  .newSmallBlast(
                     this.getCenterPixelX(true) + Utilities.randomInt(-4, 4),
                     this.getCenterPixelY(true) + 8 + Utilities.randomInt(-1, 1),
                     ParticleModule.ParticleSet.MAGIC_BLUE
                  );

               for (int y = 0; y < 5; y++) {
                  this.particle
                     .newParticle(
                        this.getCenterPixelX(true) + Utilities.randomInt(-4, 4),
                        this.getCenterPixelY(true) + 8 + Utilities.randomInt(-1, 1),
                        ParticleModule.ParticleType.SMOKE_SPECK,
                        ParticleModule.ParticleSet.MAGIC_BLUE
                     );
               }
            }
         }

         for (int x = 0; x < 10; x++) {
            this.particle
               .newParticle(
                  this.getCenterPixelX(true) + Utilities.randomInt(-4, 4),
                  this.getCenterPixelY(true) + 8 + Utilities.randomInt(-1, 1),
                  ParticleModule.ParticleType.FIRE_SPECK,
                  ParticleModule.ParticleSet.MAGIC_BLUE
               );
         }

         for (int x = 0; x < 4; x++) {
            this.particle
               .newParticle(
                  this.getCenterPixelX(true) + Utilities.randomInt(-4, 4),
                  this.getCenterPixelY(true) + 8 + Utilities.randomInt(-1, 1),
                  ParticleModule.ParticleType.SMOKE_SPECK,
                  ParticleModule.ParticleSet.MAGIC_BLUE
               );
         }
      }
   }

   public void decay() throws SlickException {
      if (this.dead) {
         this.sound.playSound(SoundModule.SoundType.DECAY, this.getCenterPixelX(true), this.getCenterPixelY(true));
         int randomX = this.getCenterPixelX(true) + Utilities.randomInt(-4, 4);
         int randomY = this.getCenterPixelY(true) + 8 + Utilities.randomInt(-1, 1);
         int randomEssence = Utilities.randomInt(3);
         int randomEssenceAmount = Utilities.randomInt(3);
         this.getSprite().nextFrame();
         if (randomEssence != 0 && randomEssenceAmount != 0) {
            this.essence.newEssence(randomX, randomY, randomEssence, randomEssenceAmount);
         }

         for (int x = 0; x < 20; x++) {
            this.particle
               .newParticle(
                  this.getCenterPixelX(true) + Utilities.randomInt(-4, 4),
                  this.getCenterPixelY(true) + 8 + Utilities.randomInt(-1, 1),
                  ParticleModule.ParticleType.FIRE_SPECK,
                  ParticleModule.ParticleSet.BLACK
               );
         }

         for (int x = 0; x < 20; x++) {
            this.particle
               .newParticle(
                  this.getCenterPixelX(true) + Utilities.randomInt(-4, 4),
                  this.getCenterPixelY(true) + 8 + Utilities.randomInt(-1, 1),
                  ParticleModule.ParticleType.SMOKE_SPECK,
                  ParticleModule.ParticleSet.BLACK
               );
         }
      }
   }

   public void equipResource(ResourceBase r, ResourceModule.ResourceSlot s) throws SlickException {
      if (s == ResourceModule.ResourceSlot.HEAD) {
         this.resource.setResourceInInventory(this, r);
         r.assign(this);
         if (this.getInventoryHeadSlot() != null) {
            ResourceBase dropped = this.unequipResource(ResourceModule.ResourceSlot.HEAD);
            OrderedPair o = this.resource.findOpenSpace(this.tileX, this.tileY, this.mobFlags.getBlockMapGroup());
            this.resource.setResourcesOnGround(o.getX(), o.getY(), dropped);
         }

         this.setInventoryHeadSlot(r);
      } else if (s == ResourceModule.ResourceSlot.CHEST) {
         this.resource.setResourceInInventory(this, r);
         r.assign(this);
         if (this.getInventoryChestSlot() != null) {
            ResourceBase dropped = this.unequipResource(ResourceModule.ResourceSlot.CHEST);
            OrderedPair o = this.resource.findOpenSpace(this.tileX, this.tileY, this.mobFlags.getBlockMapGroup());
            this.resource.setResourcesOnGround(o.getX(), o.getY(), dropped);
         }

         this.setInventoryChestSlot(r);
      } else if (s == ResourceModule.ResourceSlot.TOOL) {
         this.resource.setResourceInInventory(this, r);
         r.assign(this);
         if (this.getInventoryToolSlot() != null) {
            ResourceBase dropped = this.unequipResource(ResourceModule.ResourceSlot.TOOL);
            OrderedPair o = this.resource.findOpenSpace(this.tileX, this.tileY, this.mobFlags.getBlockMapGroup());
            this.resource.setResourcesOnGround(o.getX(), o.getY(), dropped);
         }

         this.setInventoryToolSlot(r);
      } else if (s == ResourceModule.ResourceSlot.MELEE_WEAPON) {
         this.resource.setResourceInInventory(this, r);
         r.assign(this);
         if (this.getInventoryMeleeWeaponSlot() != null) {
            ResourceBase dropped = this.unequipResource(ResourceModule.ResourceSlot.MELEE_WEAPON);
            OrderedPair o = this.resource.findOpenSpace(this.tileX, this.tileY, this.mobFlags.getBlockMapGroup());
            this.resource.setResourcesOnGround(o.getX(), o.getY(), dropped);
         }

         this.setInventoryMeleeWeaponSlot(r);
      } else if (s == ResourceModule.ResourceSlot.SHIELD) {
         this.resource.setResourceInInventory(this, r);
         r.assign(this);
         if (this.getInventoryShieldSlot() != null) {
            ResourceBase dropped = this.unequipResource(ResourceModule.ResourceSlot.SHIELD);
            OrderedPair o = this.resource.findOpenSpace(this.tileX, this.tileY, this.mobFlags.getBlockMapGroup());
            this.resource.setResourcesOnGround(o.getX(), o.getY(), dropped);
         }

         this.setInventoryShieldSlot(r);
      } else if (s == ResourceModule.ResourceSlot.RECOVERY) {
         this.resource.setResourceInInventory(this, r);
         r.assign(this);
         if (this.getInventoryRecoverySlot() != null) {
            ResourceBase dropped = this.unequipResource(ResourceModule.ResourceSlot.RECOVERY);
            OrderedPair o = this.resource.findOpenSpace(this.tileX, this.tileY, this.mobFlags.getBlockMapGroup());
            this.resource.setResourcesOnGround(o.getX(), o.getY(), dropped);
         }

         this.setInventoryRecoverySlot(r);
      } else if (s == ResourceModule.ResourceSlot.WATER) {
         this.resource.setResourceInInventory(this, r);
         r.assign(this);
         if (this.getInventoryWaterSlot() != null) {
            ResourceBase dropped = this.unequipResource(ResourceModule.ResourceSlot.WATER);
            OrderedPair o = this.resource.findOpenSpace(this.tileX, this.tileY, this.mobFlags.getBlockMapGroup());
            this.resource.setResourcesOnGround(o.getX(), o.getY(), dropped);
         }

         this.setInventoryWaterSlot(r);
      } else if (s == ResourceModule.ResourceSlot.FOOD) {
         this.resource.setResourceInInventory(this, r);
         r.assign(this);
         if (this.getInventoryFoodSlot() != null) {
            ResourceBase dropped = this.unequipResource(ResourceModule.ResourceSlot.FOOD);
            OrderedPair o = this.resource.findOpenSpace(this.tileX, this.tileY, this.mobFlags.getBlockMapGroup());
            this.resource.setResourcesOnGround(o.getX(), o.getY(), dropped);
         }

         this.setInventoryFoodSlot(r);
      } else if (s == ResourceModule.ResourceSlot.RANGED_WEAPON) {
         this.resource.setResourceInInventory(this, r);
         r.assign(this);
         if (this.getInventoryRangedWeaponSlot() != null) {
            ResourceBase dropped = this.unequipResource(ResourceModule.ResourceSlot.RANGED_WEAPON);
            OrderedPair o = this.resource.findOpenSpace(this.tileX, this.tileY, this.mobFlags.getBlockMapGroup());
            this.resource.setResourcesOnGround(o.getX(), o.getY(), dropped);
         }

         this.setInventoryRangedWeaponSlot(r);
      } else if (s == ResourceModule.ResourceSlot.RANGED_WEAPON_AMMO) {
         this.resource.setResourceInInventory(this, r);
         r.assign(this);
         if (this.getInventoryRangedWeaponAmmoSlot() != null) {
            ResourceBase dropped = this.unequipResource(ResourceModule.ResourceSlot.RANGED_WEAPON_AMMO);
            OrderedPair o = this.resource.findOpenSpace(this.tileX, this.tileY, this.mobFlags.getBlockMapGroup());
            this.resource.setResourcesOnGround(o.getX(), o.getY(), dropped);
         }

         this.setInventoryRangedWeaponAmmoSlot(r);
      }
   }

   public ResourceBase unequipResource(ResourceModule.ResourceSlot s) {
      ResourceBase removedResource = null;
      if (s == ResourceModule.ResourceSlot.HEAD) {
         removedResource = this.getInventoryHeadSlot();
         this.setInventoryHeadSlot(null);
      } else if (s == ResourceModule.ResourceSlot.CHEST) {
         removedResource = this.getInventoryChestSlot();
         this.setInventoryChestSlot(null);
      } else if (s == ResourceModule.ResourceSlot.TOOL) {
         removedResource = this.getInventoryToolSlot();
         this.setInventoryToolSlot(null);
      } else if (s == ResourceModule.ResourceSlot.MELEE_WEAPON) {
         removedResource = this.getInventoryMeleeWeaponSlot();
         this.setInventoryMeleeWeaponSlot(null);
      } else if (s == ResourceModule.ResourceSlot.SHIELD) {
         removedResource = this.getInventoryShieldSlot();
         this.setInventoryShieldSlot(null);
      } else if (s == ResourceModule.ResourceSlot.RECOVERY) {
         removedResource = this.getInventoryRecoverySlot();
         this.setInventoryRecoverySlot(null);
      } else if (s == ResourceModule.ResourceSlot.WATER) {
         removedResource = this.getInventoryWaterSlot();
         this.setInventoryWaterSlot(null);
      } else if (s == ResourceModule.ResourceSlot.FOOD) {
         removedResource = this.getInventoryFoodSlot();
         this.setInventoryFoodSlot(null);
      } else if (s == ResourceModule.ResourceSlot.RANGED_WEAPON) {
         removedResource = this.getInventoryRangedWeaponSlot();
         this.setInventoryRangedWeaponSlot(null);
      } else if (s == ResourceModule.ResourceSlot.RANGED_WEAPON_AMMO) {
         removedResource = this.getInventoryRangedWeaponAmmoSlot();
         this.setInventoryRangedWeaponAmmoSlot(null);
      }

      if (removedResource != null) {
         removedResource.unassign();
      }

      return removedResource;
   }

   public ResourceBase unequipResource(ResourceBase r) {
      ResourceBase removedResource = null;
      if (r == this.getInventoryHeadSlot()) {
         removedResource = this.getInventoryHeadSlot();
         this.setInventoryHeadSlot(null);
      } else if (r == this.getInventoryChestSlot()) {
         removedResource = this.getInventoryChestSlot();
         this.setInventoryChestSlot(null);
      } else if (r == this.getInventoryToolSlot()) {
         removedResource = this.getInventoryToolSlot();
         this.setInventoryToolSlot(null);
      } else if (r == this.getInventoryMeleeWeaponSlot()) {
         removedResource = this.getInventoryMeleeWeaponSlot();
         this.setInventoryMeleeWeaponSlot(null);
      } else if (r == this.getInventoryShieldSlot()) {
         removedResource = this.getInventoryShieldSlot();
         this.setInventoryShieldSlot(null);
      } else if (r == this.getInventoryRecoverySlot()) {
         removedResource = this.getInventoryRecoverySlot();
         this.setInventoryRecoverySlot(null);
      } else if (r == this.getInventoryWaterSlot()) {
         removedResource = this.getInventoryWaterSlot();
         this.setInventoryWaterSlot(null);
      } else if (r == this.getInventoryFoodSlot()) {
         removedResource = this.getInventoryFoodSlot();
         this.setInventoryFoodSlot(null);
      } else if (r == this.getInventoryRangedWeaponSlot()) {
         removedResource = this.getInventoryRangedWeaponSlot();
         this.setInventoryRangedWeaponSlot(null);
      } else if (r == this.getInventoryRangedWeaponAmmoSlot()) {
         removedResource = this.getInventoryRangedWeaponAmmoSlot();
         this.setInventoryRangedWeaponAmmoSlot(null);
      }

      if (removedResource != null) {
         removedResource.unassign();
      }

      return removedResource;
   }

   public ResourceBase getResourceInInventory(ResourceModule.ResourceSlot s) {
      if (s == ResourceModule.ResourceSlot.HEAD) {
         return this.getInventoryHeadSlot();
      } else if (s == ResourceModule.ResourceSlot.CHEST) {
         return this.getInventoryChestSlot();
      } else if (s == ResourceModule.ResourceSlot.TOOL) {
         return this.getInventoryToolSlot();
      } else if (s == ResourceModule.ResourceSlot.MELEE_WEAPON) {
         return this.getInventoryMeleeWeaponSlot();
      } else if (s == ResourceModule.ResourceSlot.SHIELD) {
         return this.getInventoryShieldSlot();
      } else if (s == ResourceModule.ResourceSlot.RECOVERY) {
         return this.getInventoryRecoverySlot();
      } else if (s == ResourceModule.ResourceSlot.WATER) {
         return this.getInventoryWaterSlot();
      } else if (s == ResourceModule.ResourceSlot.FOOD) {
         return this.getInventoryFoodSlot();
      } else if (s == ResourceModule.ResourceSlot.RANGED_WEAPON) {
         return this.getInventoryRangedWeaponSlot();
      } else {
         return s == ResourceModule.ResourceSlot.RANGED_WEAPON_AMMO ? this.getInventoryRangedWeaponAmmoSlot() : null;
      }
   }

   private void unequipAndDropAll() throws SlickException {
      this.forceDropAllCarried();
      this.forceDropAllEquipped();
   }

   public void forceDropAllCarried() throws SlickException {
      if (this.hasCarriedResources()) {
         for (ResourceBase r : this.getCarriedResourcesList()) {
            OrderedPair openSpace = this.resource.findOpenSpace(this.tileX, this.tileY, this.mobFlags.getBlockMapGroup());
            this.resource.setResourcesOnGround(openSpace.getX(), openSpace.getY(), r);
            if (r.isAssigned()) {
               r.unassign();
            }
         }

         this.getCarriedResourcesList().clear();
      }
   }

   public void forceDropAllEquipped() throws SlickException {
      ResourceModule.ResourceSlot[] var6;
      for (ResourceModule.ResourceSlot s : ResourceModule.ResourceSlot.values()) {
         ResourceBase drop = this.unequipResource(s);
         if (drop != null) {
            OrderedPair openSpace = this.resource.findOpenSpace(this.tileX, this.tileY, this.mobFlags.getBlockMapGroup());
            this.resource.setResourcesOnGround(openSpace.getX(), openSpace.getY(), drop);
            if (drop.isAssigned()) {
               drop.unassign();
            }
         }
      }
   }

   public void forceDeleteAllEquipped() throws SlickException {
      ResourceModule.ResourceSlot[] var5;
      for (ResourceModule.ResourceSlot s : ResourceModule.ResourceSlot.values()) {
         ResourceBase drop = this.unequipResource(s);
         if (drop != null) {
            this.resource.deleteResource(drop);
         }
      }
   }

   protected void spawnRandomItems() throws SlickException {
      ArrayList<ResourceModule.ResourceType> validResourceTypes = new ArrayList<>();

      ResourceModule.ResourceType[] var5;
      for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
         if (this.mobFlags.getCanInventoryEquipHead() && t.getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.HEAD) {
            validResourceTypes.add(t);
         } else if (this.mobFlags.getCanInventoryEquipChest() && t.getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.CHEST) {
            validResourceTypes.add(t);
         } else if (this.mobFlags.getCanInventoryEquipTool() && t.getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.TOOL) {
            validResourceTypes.add(t);
         } else if (this.mobFlags.getCanInventoryEquipMeleeWeapon() && t.getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.MELEE_WEAPON) {
            validResourceTypes.add(t);
         } else if (this.mobFlags.getCanInventoryEquipShield() && t.getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.SHIELD) {
            validResourceTypes.add(t);
         } else if (this.mobFlags.getCanInventoryEquipRecovery() && t.getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.RECOVERY) {
            validResourceTypes.add(t);
         } else if (this.mobFlags.getCanInventoryEquipWater() && t.getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.WATER) {
            validResourceTypes.add(t);
         } else if (this.mobFlags.getCanInventoryEquipFood() && t.getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.FOOD) {
            validResourceTypes.add(t);
         } else if (this.mobFlags.getCanInventoryEquipRangedWeapon() && t.getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.RANGED_WEAPON) {
            validResourceTypes.add(t);
         } else if (this.mobFlags.getCanInventoryEquipRangedWeaponAmmo() && t.getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.RANGED_WEAPON_AMMO
            )
          {
            validResourceTypes.add(t);
         }
      }

      if (this.mobFlags.getCanInventoryEquipHead() && Utilities.randomInt(this.mobFlags.getInventoryRandomSpawnItemsChance()) == 0) {
         Collections.shuffle(validResourceTypes);

         for (ResourceModule.ResourceType tx : validResourceTypes) {
            if (tx.getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.HEAD) {
               ResourceBase r = this.resource.createResourceUnassigned(tx);
               this.equipResource(r, r.getType().getTemplate().getResourceSlot());
               break;
            }
         }
      }

      if (this.mobFlags.getCanInventoryEquipChest() && Utilities.randomInt(this.mobFlags.getInventoryRandomSpawnItemsChance()) == 0) {
         Collections.shuffle(validResourceTypes);

         for (ResourceModule.ResourceType txx : validResourceTypes) {
            if (txx.getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.CHEST) {
               ResourceBase r = this.resource.createResourceUnassigned(txx);
               this.equipResource(r, r.getType().getTemplate().getResourceSlot());
               break;
            }
         }
      }

      if (this.mobFlags.getCanInventoryEquipTool() && Utilities.randomInt(this.mobFlags.getInventoryRandomSpawnItemsChance()) == 0) {
         Collections.shuffle(validResourceTypes);

         for (ResourceModule.ResourceType txxx : validResourceTypes) {
            if (txxx.getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.TOOL) {
               ResourceBase r = this.resource.createResourceUnassigned(txxx);
               this.equipResource(r, r.getType().getTemplate().getResourceSlot());
               break;
            }
         }
      }

      if (this.mobFlags.getCanInventoryEquipMeleeWeapon() && Utilities.randomInt(this.mobFlags.getInventoryRandomSpawnItemsChance()) == 0) {
         Collections.shuffle(validResourceTypes);

         for (ResourceModule.ResourceType txxxx : validResourceTypes) {
            if (txxxx.getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.MELEE_WEAPON) {
               ResourceBase r = this.resource.createResourceUnassigned(txxxx);
               this.equipResource(r, r.getType().getTemplate().getResourceSlot());
               break;
            }
         }
      }

      if (this.mobFlags.getCanInventoryEquipShield() && Utilities.randomInt(this.mobFlags.getInventoryRandomSpawnItemsChance()) == 0) {
         Collections.shuffle(validResourceTypes);

         for (ResourceModule.ResourceType txxxxx : validResourceTypes) {
            if (txxxxx.getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.SHIELD) {
               ResourceBase r = this.resource.createResourceUnassigned(txxxxx);
               this.equipResource(r, r.getType().getTemplate().getResourceSlot());
               break;
            }
         }
      }

      if (this.mobFlags.getCanInventoryEquipRecovery() && Utilities.randomInt(this.mobFlags.getInventoryRandomSpawnItemsChance()) == 0) {
         Collections.shuffle(validResourceTypes);

         for (ResourceModule.ResourceType txxxxxx : validResourceTypes) {
            if (txxxxxx.getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.RECOVERY) {
               ResourceBase r = this.resource.createResourceUnassigned(txxxxxx);
               this.equipResource(r, r.getType().getTemplate().getResourceSlot());
               break;
            }
         }
      }

      if (this.mobFlags.getCanInventoryEquipWater() && Utilities.randomInt(this.mobFlags.getInventoryRandomSpawnItemsChance()) == 0) {
         Collections.shuffle(validResourceTypes);

         for (ResourceModule.ResourceType txxxxxxx : validResourceTypes) {
            if (txxxxxxx.getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.WATER) {
               ResourceBase r = this.resource.createResourceUnassigned(txxxxxxx);
               this.equipResource(r, r.getType().getTemplate().getResourceSlot());
               break;
            }
         }
      }

      if (this.mobFlags.getCanInventoryEquipFood() && Utilities.randomInt(this.mobFlags.getInventoryRandomSpawnItemsChance()) == 0) {
         Collections.shuffle(validResourceTypes);

         for (ResourceModule.ResourceType txxxxxxxx : validResourceTypes) {
            if (txxxxxxxx.getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.FOOD) {
               ResourceBase r = this.resource.createResourceUnassigned(txxxxxxxx);
               this.equipResource(r, r.getType().getTemplate().getResourceSlot());
               break;
            }
         }
      }

      if (this.mobFlags.getCanInventoryEquipRangedWeapon() && Utilities.randomInt(this.mobFlags.getInventoryRandomSpawnItemsChance()) == 0) {
         Collections.shuffle(validResourceTypes);

         for (ResourceModule.ResourceType txxxxxxxxx : validResourceTypes) {
            if (txxxxxxxxx.getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.RANGED_WEAPON) {
               ResourceBase r = this.resource.createResourceUnassigned(txxxxxxxxx);
               this.equipResource(r, r.getType().getTemplate().getResourceSlot());
               break;
            }
         }
      }

      if (this.mobFlags.getCanInventoryEquipRangedWeaponAmmo() && Utilities.randomInt(this.mobFlags.getInventoryRandomSpawnItemsChance()) == 0) {
         Collections.shuffle(validResourceTypes);

         for (ResourceModule.ResourceType txxxxxxxxxx : validResourceTypes) {
            if (txxxxxxxxxx.getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.RANGED_WEAPON_AMMO) {
               ResourceBase r = this.resource.createResourceUnassigned(txxxxxxxxxx);
               this.equipResource(r, r.getType().getTemplate().getResourceSlot());
               break;
            }
         }
      }
   }

   public boolean hasFoodResource() {
      if (this.getInventoryFoodSlot() != null && this.getInventoryFoodSlot().getType().getTemplate().getFoodPerUse() > 0) {
         return true;
      } else {
         for (ResourceBase r : this.carriedResourcesList) {
            if (r.getType().getTemplate().getFoodPerUse() > 0) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean hasWaterResource() {
      if (this.getInventoryWaterSlot() != null && this.getInventoryWaterSlot().getType().getTemplate().getWaterPerUse() > 0) {
         return true;
      } else {
         for (ResourceBase r : this.carriedResourcesList) {
            if (r.getType().getTemplate().getWaterPerUse() > 0) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean hasRecoveryResource() {
      if (this.getInventoryRecoverySlot() != null && this.getInventoryRecoverySlot().getType().getTemplate().getHealthPercentPerUse() > 0) {
         return true;
      } else if (this.getInventoryToolSlot() != null && this.getInventoryToolSlot().getType().getTemplate().getHealthPercentPerUse() > 0) {
         return true;
      } else {
         for (ResourceBase r : this.carriedResourcesList) {
            if (r.getType().getTemplate().getHealthPercentPerUse() > 0) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean isCompromised() {
      return this.poisoned || this.dysentery || this.blighted || this.onFire || this.frozen;
   }

   public boolean isDesiredItem(ResourceModule.ResourceType r) {
      if (this.mobFlags.getCanInventoryEquipHead()
         && this.getInventoryHeadSlot() == null
         && r.getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.HEAD) {
         return true;
      } else if (this.mobFlags.getCanInventoryEquipChest()
         && this.getInventoryChestSlot() == null
         && r.getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.CHEST) {
         return true;
      } else {
         if (this.mobFlags.getCanInventoryEquipTool() && r.getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.TOOL) {
            if (this.getInventoryToolSlot() == null && this.mobFlags.getAllowInventoryEquipRestrictionOverride()) {
               return true;
            }

            ArrayList<ResourceModule.ResourceType> preferredTools = this.getMobJobType().getJob().getPreferredTools();
            if ((this.getInventoryToolSlot() == null || !preferredTools.contains(this.getInventoryToolSlot().getType())) && preferredTools.contains(r)) {
               return true;
            }
         }

         if (this.mobFlags.getCanInventoryEquipMeleeWeapon()
            && this.getInventoryMeleeWeaponSlot() == null
            && r.getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.MELEE_WEAPON) {
            return true;
         } else if (this.mobFlags.getCanInventoryEquipShield()
            && this.getInventoryShieldSlot() == null
            && r.getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.SHIELD) {
            return true;
         } else if (this.mobFlags.getCanInventoryEquipRecovery()
            && this.getInventoryRecoverySlot() == null
            && r.getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.RECOVERY) {
            return true;
         } else if (this.mobFlags.getCanInventoryEquipWater()
            && this.getInventoryWaterSlot() == null
            && r.getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.WATER) {
            return true;
         } else if (this.mobFlags.getCanInventoryEquipFood()
            && this.getInventoryFoodSlot() == null
            && r.getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.FOOD) {
            return true;
         } else {
            return this.mobFlags.getCanInventoryEquipRangedWeapon()
                  && this.getInventoryRangedWeaponSlot() == null
                  && r.getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.RANGED_WEAPON
               ? true
               : this.mobFlags.getCanInventoryEquipRangedWeaponAmmo()
                  && this.getInventoryRangedWeaponAmmoSlot() == null
                  && r.getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.RANGED_WEAPON_AMMO
                  && this.mobFlags.getCanInventoryEquipRangedWeapon()
                  && this.getInventoryRangedWeaponSlot() != null
                  && this.getInventoryRangedWeaponSlot().getType().getTemplate().getRangedWeaponRequiredAmmo() == r;
         }
      }
   }

   public void setID(int i) {
      this.mobID = i;
   }

   public static enum DamageCause {
      NONE(Text.getText("damageCauseName.regular")),
      FIRE(Text.getText("damageCauseName.fire")),
      ON_FIRE(Text.getText("damageCauseName.onFire")),
      ICE(Text.getText("damageCauseName.ice")),
      COLD(Text.getText("damageCauseName.cold")),
      FROZEN(Text.getText("damageCauseName.frozen")),
      POISON(Text.getText("damageCauseName.poison")),
      FROM_POISON(Text.getText("damageCauseName.poisoned")),
      DYSENTERY(Text.getText("damageCauseName.dysentery")),
      FROM_DYSENTERY(Text.getText("damageCauseName.dysentery")),
      DEHYDRATION(Text.getText("damageCauseName.dehydration")),
      STARVATION(Text.getText("damageCauseName.starving")),
      HEAT(Text.getText("damageCauseName.heat")),
      LIGHTNING(Text.getText("damageCauseName.lightning")),
      CRUSHING(Text.getText("damageCauseName.crushing")),
      PIERCING(Text.getText("damageCauseName.piercing")),
      MAGIC(Text.getText("damageCauseName.magic")),
      BLIGHT(Text.getText("damageCauseName.blight")),
      FROM_BLIGHT(Text.getText("damageCauseName.blighted")),
      COMBAT(Text.getText("damageCauseName.combat")),
      DROPPED(Text.getText("damageCauseName.dropped")),
      TOWER(Text.getText("damageCauseName.tower")),
      GOD(Text.getText("damageCauseName.god")),
      EARTHQUAKE(Text.getText("damageCauseName.earthquake")),
      HAIL(Text.getText("damageCauseName.hail")),
      METEOR(Text.getText("damageCauseName.meteor")),
      RAIN(Text.getText("damageCauseName.rain")),
      WATER(Text.getText("damageCauseName.water")),
      NATURAL_CAUSES(Text.getText("damageCauseName.naturalCauses")),
      CHILD_BIRTH(Text.getText("damageCauseName.childBirth")),
      BEING_SLAUGHTERED(Text.getText("damageCauseName.beingSlaughtered")),
      CORRUPTION(Text.getText("damageCauseName.corruption")),
      COMET(Text.getText("damageCauseName.comet")),
      TERMINATING_PREGNANCY(Text.getText("damageCauseName.terminatingPregnancy"));

      private final String damageCauseText;

      private DamageCause(String damageCauseText) {
         this.damageCauseText = damageCauseText;
      }

      public String getText() {
         return this.damageCauseText;
      }
   }

   public static enum DamageType {
      REGULAR(Text.getText("damageTypeName.regular")),
      CRUSHING(Text.getText("damageTypeName.crushing")),
      PIERCING(Text.getText("damageTypeName.piercing")),
      SLASHING(Text.getText("damageTypeName.slashing")),
      FIRE(Text.getText("damageTypeName.fire")),
      ICE(Text.getText("damageTypeName.ice")),
      ELECTRIC(Text.getText("damageTypeName.electric")),
      POISON(Text.getText("damageTypeName.poison")),
      FROM_POISON(Text.getText("damageTypeName.poisoned")),
      DYSENTERY(Text.getText("damageTypeName.dysentery")),
      FROM_DYSENTERY(Text.getText("damageTypeName.dysentery")),
      BLIGHT(Text.getText("damageTypeName.blight")),
      FROM_BLIGHT(Text.getText("damageTypeName.blighted")),
      NUTRIENTS(Text.getText("damageTypeName.nutrients")),
      MAGIC_REGULAR(Text.getText("damageTypeName.magic")),
      MAGIC_FIRE(Text.getText("damageTypeName.magicFire")),
      MAGIC_ELECTRIC(Text.getText("damageTypeName.magicElectric")),
      MAGIC_ICE(Text.getText("damageTypeName.magicIce")),
      WATER(Text.getText("damageTypeName.water"));

      private final String damageTypeText;

      private DamageType(String damageTypeText) {
         this.damageTypeText = damageTypeText;
      }

      public String getText() {
         return this.damageTypeText;
      }
   }

   public static enum MobGender {
      MALE("$CYA1"),
      FEMALE("$PIN1"),
      NONE("$WHI0");

      private final String mobGenderColor;

      private MobGender(String mobGenderColor) {
         this.mobGenderColor = mobGenderColor;
      }

      public String getGenderColor() {
         return this.mobGenderColor;
      }
   }

   public static enum MobGroup {
      VILLAGER,
      GHOST,
      GOLEM,
      MONSTER,
      CHARMED_MONSTER,
      ANIMAL,
      DOMESTICATED_ANIMAL,
      NOMAD,
      MIGRANT,
      PROVISIONER,
      NONE;
   }

   public static enum MobPosition {
      NORTH,
      EAST,
      SOUTH,
      WEST;
   }

   public static enum MobType {
      HUMAN_VILLAGER(Text.getText("mobName.humanVillager.single"), Text.getText("mobName.humanVillager.plural")),
      HUMAN_VILLAGER_CHILD(Text.getText("mobName.humanVillagerChild.single"), Text.getText("mobName.humanVillagerChild.plural")),
      HUMAN_VILLAGER_ELDER(Text.getText("mobName.humanVillagerElder.single"), Text.getText("mobName.humanVillagerElder.plural")),
      HUMAN_VILLAGER_NEPHILIM(Text.getText("mobName.humanVillagerNephilim.single"), Text.getText("mobName.humanVillagerNephilim.plural")),
      HUMAN_VILLAGER_CHILD_NEPHILIM(Text.getText("mobName.humanVillagerChildNephilim.single"), Text.getText("mobName.humanVillagerChildNephilim.plural")),
      HUMAN_VILLAGER_ELDER_NEPHILIM(Text.getText("mobName.humanVillagerElderNephilim.single"), Text.getText("mobName.humanVillagerElderNephilim.plural")),
      HUMAN_NOMAD(Text.getText("mobName.humanNomad.single"), Text.getText("mobName.humanNomad.plural")),
      HUMAN_NOMAD_CHILD(Text.getText("mobName.humanNomadChild.single"), Text.getText("mobName.humanNomadChild.plural")),
      HUMAN_NOMAD_ELDER(Text.getText("mobName.humanNomadElder.single"), Text.getText("mobName.humanNomadElder.plural")),
      HUMAN_NOMAD_NEPHILIM(Text.getText("mobName.humanNomadNephilim.single"), Text.getText("mobName.humanNomadNephilim.plural")),
      HUMAN_NOMAD_CHILD_NEPHILIM(Text.getText("mobName.humanNomadChildNephilim.single"), Text.getText("mobName.humanNomadChildNephilim.plural")),
      HUMAN_NOMAD_ELDER_NEPHILIM(Text.getText("mobName.humanNomadElderNephilim.single"), Text.getText("mobName.humanNomadElderNephilim.plural")),
      HUMAN_MIGRANT_DEPARTING(Text.getText("mobName.humanMigrantDeparting.single"), Text.getText("mobName.humanMigrantDeparting.plural")),
      HUMAN_MIGRANT_ARRIVING(Text.getText("mobName.humanMigrantArriving.single"), Text.getText("mobName.humanMigrantArriving.plural")),
      HUMAN_MIGRANT_NEPHILIM_DEPARTING(
         Text.getText("mobName.humanMigrantNephilimDeparting.single"), Text.getText("mobName.humanMigrantNephilimDeparting.plural")
      ),
      HUMAN_MIGRANT_NEPHILIM_ARRIVING(Text.getText("mobName.humanMigrantNephilimArriving.single"), Text.getText("mobName.humanMigrantNephilimArriving.plural")),
      HUMAN_VILLAGER_GHOST(Text.getText("mobName.humanVillagerGhost.single"), Text.getText("mobName.humanVillagerGhost.plural")),
      HUMAN_VILLAGER_GHOST_CHILD(Text.getText("mobName.humanVillagerGhostChild.single"), Text.getText("mobName.humanVillagerGhostChild.plural")),
      HUMAN_VILLAGER_GHOST_NEPHILIM(Text.getText("mobName.humanVillagerGhostNephilim.single"), Text.getText("mobName.humanVillagerGhostNephilim.plural")),
      HUMAN_VILLAGER_GHOST_CHILD_NEPHILIM(
         Text.getText("mobName.humanVillagerGhostChildNephilim.single"), Text.getText("mobName.humanVillagerGhostChildNephilim.plural")
      ),
      CATJEET_VILLAGER(Text.getText("mobName.catjeetVillager.single"), Text.getText("mobName.catjeetVillager.plural")),
      CATJEET_VILLAGER_CHILD(Text.getText("mobName.catjeetVillagerChild.single"), Text.getText("mobName.catjeetVillagerChild.plural")),
      CATJEET_VILLAGER_ELDER(Text.getText("mobName.catjeetVillagerElder.single"), Text.getText("mobName.catjeetVillagerElder.plural")),
      CATJEET_VILLAGER_NEPHILIM(Text.getText("mobName.catjeetVillagerNephilim.single"), Text.getText("mobName.catjeetVillagerNephilim.plural")),
      CATJEET_VILLAGER_CHILD_NEPHILIM(Text.getText("mobName.catjeetVillagerChildNephilim.single"), Text.getText("mobName.catjeetVillagerChildNephilim.plural")),
      CATJEET_VILLAGER_ELDER_NEPHILIM(Text.getText("mobName.catjeetVillagerElderNephilim.single"), Text.getText("mobName.catjeetVillagerElderNephilim.plural")),
      CATJEET_NOMAD(Text.getText("mobName.catjeetNomad.single"), Text.getText("mobName.catjeetNomad.plural")),
      CATJEET_NOMAD_CHILD(Text.getText("mobName.catjeetNomadChild.single"), Text.getText("mobName.catjeetNomadChild.plural")),
      CATJEET_NOMAD_ELDER(Text.getText("mobName.catjeetNomadElder.single"), Text.getText("mobName.catjeetNomadElder.plural")),
      CATJEET_NOMAD_NEPHILIM(Text.getText("mobName.catjeetNomadNephilim.single"), Text.getText("mobName.catjeetNomadNephilim.plural")),
      CATJEET_NOMAD_CHILD_NEPHILIM(Text.getText("mobName.catjeetNomadChildNephilim.single"), Text.getText("mobName.catjeetNomadChildNephilim.plural")),
      CATJEET_NOMAD_ELDER_NEPHILIM(Text.getText("mobName.catjeetNomadElderNephilim.single"), Text.getText("mobName.catjeetNomadElderNephilim.plural")),
      CATJEET_MIGRANT_DEPARTING(Text.getText("mobName.catjeetMigrantDeparting.single"), Text.getText("mobName.catjeetMigrantDeparting.plural")),
      CATJEET_MIGRANT_ARRIVING(Text.getText("mobName.catjeetMigrantArriving.single"), Text.getText("mobName.catjeetMigrantArriving.plural")),
      CATJEET_MIGRANT_NEPHILIM_DEPARTING(
         Text.getText("mobName.catjeetMigrantNephilimDeparting.single"), Text.getText("mobName.catjeetMigrantNephilimDeparting.plural")
      ),
      CATJEET_MIGRANT_NEPHILIM_ARRIVING(
         Text.getText("mobName.catjeetMigrantNephilimArriving.single"), Text.getText("mobName.catjeetMigrantNephilimArriving.plural")
      ),
      CATJEET_GHOST(Text.getText("mobName.catjeetGhost.single"), Text.getText("mobName.catjeetGhost.plural")),
      CATJEET_GHOST_CHILD(Text.getText("mobName.catjeetGhostChild.single"), Text.getText("mobName.catjeetGhostChild.plural")),
      CATJEET_GHOST_NEPHILIM(Text.getText("mobName.catjeetGhostNephilim.single"), Text.getText("mobName.catjeetGhostNephilim.plural")),
      CATJEET_GHOST_CHILD_NEPHILIM(Text.getText("mobName.catjeetGhostChildNephilim.single"), Text.getText("mobName.catjeetGhostChildNephilim.plural")),
      CATJEET_PROVISIONER(Text.getText("mobName.catjeetProvisioner.single"), Text.getText("mobName.catjeetProvisioner.plural")),
      CATJEET_LABORER(Text.getText("mobName.catjeetLaborer.single"), Text.getText("mobName.catjeetLaborer.plural")),
      SMALL_SLIME(Text.getText("mobName.smallSlime.single"), Text.getText("mobName.smallSlime.plural")),
      SLIME(Text.getText("mobName.slime.single"), Text.getText("mobName.slime.plural")),
      SMALL_TRASHY_SLIME(Text.getText("mobName.smallTrashySlime.single"), Text.getText("mobName.smallTrashySlime.plural")),
      TRASHY_SLIME(Text.getText("mobName.trashySlime.single"), Text.getText("mobName.trashySlime.plural")),
      SMALL_BLOOD_SLIME(Text.getText("mobName.smallBloodSlime.single"), Text.getText("mobName.smallBloodSlime.plural")),
      BLOOD_SLIME(Text.getText("mobName.bloodSlime.single"), Text.getText("mobName.bloodSlime.plural")),
      HEADLESS(Text.getText("mobName.headless.single"), Text.getText("mobName.headless.plural")),
      DRONE(Text.getText("mobName.drone.single"), Text.getText("mobName.drone.plural")),
      ZOMBIE(Text.getText("mobName.zombie.single"), Text.getText("mobName.zombie.plural")),
      ZOMBIE_CHILD(Text.getText("mobName.zombieChild.single"), Text.getText("mobName.zombieChild.plural")),
      ZOMBIE_DOGGO(Text.getText("mobName.zombieDoggo.single"), Text.getText("mobName.zombieDoggo.plural")),
      ZOMBIE_DOOFY_DOGGO(Text.getText("mobName.zombieDoofyDoggo.single"), Text.getText("mobName.zombieDoofyDoggo.plural")),
      ZOMBIE_CATJEET(Text.getText("mobName.zombieCatjeet.single"), Text.getText("mobName.zombieCatjeet.plural")),
      ZOMBIE_CATJEET_CHILD(Text.getText("mobName.zombieCatjeetChild.single"), Text.getText("mobName.zombieCatjeetChild.plural")),
      SKELETON(Text.getText("mobName.skeleton.single"), Text.getText("mobName.skeleton.plural")),
      FIRE_ELEMENTAL(Text.getText("mobName.fireElemental.single"), Text.getText("mobName.fireElemental.plural")),
      SPECTER(Text.getText("mobName.specter.single"), Text.getText("mobName.specter.plural")),
      SPECTER_CHILD(Text.getText("mobName.childSpecter.single"), Text.getText("mobName.childSpecter.plural")),
      DOGGO(Text.getText("mobName.doggo.single"), Text.getText("mobName.doggo.plural")),
      DOOFY_DOGGO(Text.getText("mobName.doofyDoggo.single"), Text.getText("mobName.doofyDoggo.plural")),
      WILD_DOGGO(Text.getText("mobName.wildDoggo.single"), Text.getText("mobName.wildDoggo.plural")),
      WILD_DOOFY_DOGGO(Text.getText("mobName.wildDoofyDoggo.single"), Text.getText("mobName.wildDoofyDoggo.plural")),
      GHOST_DOGGO(Text.getText("mobName.ghostDoggo.single"), Text.getText("mobName.ghostDoggo.plural")),
      GHOST_DOOFY_DOGGO(Text.getText("mobName.ghostDoofyDoggo.single"), Text.getText("mobName.ghostDoofyDoggo.plural")),
      STONE_GOLEM(Text.getText("mobName.stoneGolem.single"), Text.getText("mobName.stoneGolem.plural")),
      ROGGO_GOLEM(Text.getText("mobName.roggoGolem.single"), Text.getText("mobName.roggoGolem.plural")),
      WOOD_GOLEM(Text.getText("mobName.woodGolem.single"), Text.getText("mobName.woodGolem.plural")),
      LOGGO_GOLEM(Text.getText("mobName.loggoGolem.single"), Text.getText("mobName.loggoGolem.plural")),
      CRYSTAL_GOLEM(Text.getText("mobName.crystalGolem.single"), Text.getText("mobName.crystalGolem.plural")),
      CRYGGO_GOLEM(Text.getText("mobName.cryggoGolem.single"), Text.getText("mobName.cryggoGolem.plural")),
      HOLY_GOLEM(Text.getText("mobName.holyGolem.single"), Text.getText("mobName.holyGolem.plural")),
      GODDO_GOLEM(Text.getText("mobName.goddoGolem.single"), Text.getText("mobName.goddoGolem.plural")),
      COURIER_GOLEM(Text.getText("mobName.courierGolem.single"), Text.getText("mobName.courierGolem.plural")),
      LABOR_GOLEM(Text.getText("mobName.laborGolem.single"), Text.getText("mobName.laborGolem.plural")),
      CUBE_E_GOLEM(Text.getText("mobName.cubeEGolem.single"), Text.getText("mobName.cubeEGolem.plural")),
      POISON_WOOD_GOLEM(Text.getText("mobName.poisonWoodGolem.single"), Text.getText("mobName.poisonWoodGolem.plural")),
      POISON_LOGGO_GOLEM(Text.getText("mobName.poisonLoggoGolem.single"), Text.getText("mobName.poisonLoggoGolem.plural")),
      FIRE_STONE_GOLEM(Text.getText("mobName.fireStoneGolem.single"), Text.getText("mobName.fireStoneGolem.plural")),
      FIRE_ROGGO_GOLEM(Text.getText("mobName.fireRoggoGolem.single"), Text.getText("mobName.fireRoggoGolem.plural")),
      FIRE_CRYSTAL_GOLEM(Text.getText("mobName.fireCrystalGolem.single"), Text.getText("mobName.fireCrystalGolem.plural")),
      FIRE_CRYGGO_GOLEM(Text.getText("mobName.fireCryggoGolem.single"), Text.getText("mobName.fireCryggoGolem.plural")),
      ICE_STONE_GOLEM(Text.getText("mobName.iceStoneGolem.single"), Text.getText("mobName.iceStoneGolem.plural")),
      ICE_ROGGO_GOLEM(Text.getText("mobName.iceRoggoGolem.single"), Text.getText("mobName.iceRoggoGolem.plural")),
      ICE_WOOD_GOLEM(Text.getText("mobName.iceWoodGolem.single"), Text.getText("mobName.iceWoodGolem.plural")),
      ICE_LOGGO_GOLEM(Text.getText("mobName.iceLoggoGolem.single"), Text.getText("mobName.iceLoggoGolem.plural")),
      ICE_CRYSTAL_GOLEM(Text.getText("mobName.iceCrystalGolem.single"), Text.getText("mobName.iceCrystalGolem.plural")),
      ICE_CRYGGO_GOLEM(Text.getText("mobName.iceCryggoGolem.single"), Text.getText("mobName.iceCryggoGolem.plural")),
      LIGHTNING_STONE_GOLEM(Text.getText("mobName.lightningStoneGolem.single"), Text.getText("mobName.lightningStoneGolem.plural")),
      LIGHTNING_ROGGO_GOLEM(Text.getText("mobName.lightningRoggoGolem.single"), Text.getText("mobName.lightningRoggoGolem.plural")),
      LIGHTNING_WOOD_GOLEM(Text.getText("mobName.lightningWoodGolem.single"), Text.getText("mobName.lightningWoodGolem.plural")),
      LIGHTNING_LOGGO_GOLEM(Text.getText("mobName.lightningLoggoGolem.single"), Text.getText("mobName.lightningLoggoGolem.plural")),
      LIGHTNING_CRYSTAL_GOLEM(Text.getText("mobName.lightningCrystalGolem.single"), Text.getText("mobName.lightningCrystalGolem.plural")),
      LIGHTNING_CRYGGO_GOLEM(Text.getText("mobName.lightningCryggoGolem.single"), Text.getText("mobName.lightningCryggoGolem.plural")),
      WILD_CLUCKER(Text.getText("mobName.wildClucker.single"), Text.getText("mobName.wildClucker.plural")),
      ZOMBIE_CLUCKER(Text.getText("mobName.zombieClucker.single"), Text.getText("mobName.zombieClucker.plural")),
      CLUCKER(Text.getText("mobName.clucker.single"), Text.getText("mobName.clucker.plural")),
      WILD_BEEFALO(Text.getText("mobName.wildBeefalo.single"), Text.getText("mobName.wildBeefalo.plural")),
      ZOMBIE_BEEFALO(Text.getText("mobName.zombieBeefalo.single"), Text.getText("mobName.zombieBeefalo.plural")),
      BEEFALO(Text.getText("mobName.beefalo.single"), Text.getText("mobName.beefalo.plural")),
      WILD_ENTLER(Text.getText("mobName.wildEntler.single"), Text.getText("mobName.wildEntler.plural")),
      ZOMBIE_ENTLER(Text.getText("mobName.zombieEntler.single"), Text.getText("mobName.zombieEntler.plural")),
      ENTLER(Text.getText("mobName.entler.single"), Text.getText("mobName.entler.plural")),
      WILD_ROUS(Text.getText("mobName.wildRous.single"), Text.getText("mobName.wildRous.plural")),
      ZOMBIE_ROUS(Text.getText("mobName.zombieRous.single"), Text.getText("mobName.zombieRous.plural")),
      ROUS(Text.getText("mobName.rous.single"), Text.getText("mobName.rous.plural"));

      private final String mobTypeName;
      private final String mobTypeNamePlural;

      private MobType(String mobTypeName, String mobTypeNamePlural) {
         this.mobTypeName = mobTypeName;
         this.mobTypeNamePlural = mobTypeNamePlural;
      }

      private MobType(String mobTypeName) {
         this.mobTypeName = mobTypeName;
         this.mobTypeNamePlural = mobTypeName;
      }

      public String getName() {
         return this.mobTypeName;
      }

      public String getNamePlural() {
         return this.mobTypeNamePlural;
      }
   }
}
