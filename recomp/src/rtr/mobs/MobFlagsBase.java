package rtr.mobs;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import org.newdawn.slick.Color;
import rtr.ModuleBase;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.TimeModule;
import rtr.map.CollisionModule;
import rtr.map.MapData;
import rtr.missiles.MissileModule;
import rtr.particles.ParticleModule;
import rtr.resources.ResourceModule;
import rtr.states.StateBase;

public class MobFlagsBase {
   protected PerkModule perk = (PerkModule)StateBase.getModule(ModuleBase.ModuleType.PERK);
   protected TimeModule time = (TimeModule)StateBase.getModule(ModuleBase.ModuleType.TIME);
   protected Color[][] colorLayersArrays = new Color[7][];
   protected HashMap<MobBase.MobGender, SoundModule.SoundSet> mobSoundSets = new HashMap<>();
   protected int idleSoundRate;
   protected MapData.BlockMapGroup blockMapGroup;
   protected int aggressiveness;
   protected int levelCapLow;
   protected int levelCapHigh;
   protected int influenceMaintainCost;
   protected float statVelocityBase;
   protected float statVelocityMultiplier;
   protected int experienceMultiplier;
   protected int statsPerLevel;
   protected int statStrengthChance;
   protected int statDexterityChance;
   protected double carryMaxBonusMultiplier = 1.0;
   protected boolean allowInventory;
   protected boolean allowInventoryRandomSpawnItems;
   protected boolean allowInventoryEquipBuildings;
   protected boolean allowInventoryEquipRestrictionOverride;
   protected int inventoryRandomSpawnItemsChance;
   protected int inventorySearchChance;
   protected int inventoryNearbyResourcesOnGroundDistance;
   protected boolean canInventorySearchVillage;
   protected boolean canInventoryEquipHead;
   protected boolean canInventoryEquipChest;
   protected boolean canInventoryEquipTool;
   protected boolean canInventoryEquipMeleeWeapon;
   protected boolean canInventoryEquipShield;
   protected boolean canInventoryEquipRecovery;
   protected boolean canInventoryEquipWater;
   protected boolean canInventoryEquipFood;
   protected boolean canInventoryEquipRangedWeapon;
   protected boolean canInventoryEquipRangedWeaponAmmo;
   protected HashMap<ResourceModule.ResourceType, Integer> dropsList = new HashMap<>();
   protected int dropsChance;
   protected int dropsAttempts;
   protected boolean dropsAllowSlaughter;
   protected boolean canMonsterSpawn;
   protected int monsterSpawnDayDefaultMinimum;
   protected int monsterSpawnDayDefaultMaximum;
   protected boolean canAnimalSpawn;
   protected int animalSpawnDayDefaultMinimum;
   protected int animalSpawnDayDefaultMaximum;
   protected int animalSpawnAmountMinimum;
   protected int animalSpawnAmountMaximum;
   protected int animalSpawnAmountWeight;
   protected boolean allowCourierBehavior;
   protected int courierBehaviorChance;
   protected int courierDepartRegionChance;
   protected TimeModule.TimeOfDay courierDepartRegionTimeOfDay;
   protected int courierIdleWithResourcesDelay;
   protected boolean allowMigrateDepartBehavior;
   protected int migrateDepartRegionChance;
   protected TimeModule.TimeOfDay migrateDepartTimeOfDay;
   protected MobBase.MobType migrantDepartArrivalType;
   protected boolean allowMigrationPreperationBehavior;
   protected int migrationPreperationChance;
   protected TimeModule.TimeOfDay migrationPreperationTimeOfDay;
   protected MobBase.MobType migrationPreperationMobType;
   protected int migrateDepartHungerPercent;
   protected int migrationPreperationThirstPercent;
   protected int migrationPreperationAgePercent;
   protected boolean allowAging;
   protected int agingStartAgeMinimum;
   protected int agingStartAgeMaximum;
   protected int agingGrowUpAge;
   protected MobBase.MobType agingGrowUpType;
   protected int agingElderAge;
   protected MobBase.MobType agingElderType;
   protected int agingMaximumAge;
   protected int agingRate;
   protected boolean allowHealthRegen;
   protected int healthRegenRate;
   protected int agingConstitutionDecayRate;
   protected boolean allowHealthRepair;
   protected int healthRepairChance;
   protected int healthRepairCriticalChance;
   protected double healthRepairPercentage;
   protected double healthRepairCriticalPercentage;
   protected boolean allowThermal;
   protected boolean canThermalFreeze;
   protected boolean canThermalFreezeCritical;
   protected boolean canThermalOverheat;
   protected boolean canThermalOverheatCritical;
   protected int thermalBaseline;
   protected int thermalFreeze;
   protected int thermalFreezeCritical;
   protected int thermalOverheat;
   protected int thermalOverheatCritical;
   protected int thermalIncreaseTempChance;
   protected int thermalAdjustRate;
   protected boolean allowWander;
   protected int wanderChance;
   protected boolean wanderPreferVillage;
   protected boolean wanderPreferCorruption;
   protected boolean canWanderAimlessly;
   protected int wanderAimlesslyWeight;
   protected int wanderAimlesslyDistance;
   protected boolean canWanderObjects;
   protected int wanderObjectWeight;
   protected boolean canWanderHome;
   protected int wanderHomeWeight;
   protected boolean canWanderMate;
   protected int wanderMateWeight;
   protected boolean canWanderOwner;
   protected int wanderOwnerWeight;
   protected boolean canWanderSpawn;
   protected int wanderSpawnWeight;
   protected int wanderSpawnDistance;
   protected boolean canWanderGhost;
   protected int wanderGhostWeight;
   protected int wanderGhostDistance;
   protected boolean allowConfused;
   protected int confusedChance;
   protected int confusedWanderChance;
   protected int confusedBreakAwayChance;
   protected int confusedWanderDistance;
   protected boolean allowPanic;
   protected int panicChance;
   protected int panicBreakAwayChance;
   protected int panicDistance;
   protected boolean allowPanicHomeSearch;
   protected int panicHomeSearchDisance;
   protected int panicRunHomeChance;
   protected int panicHideInHomeDuration;
   protected boolean allowHome;
   protected boolean canEnterHome;
   protected int homeVisitChance;
   protected int homeFindChance;
   protected int homeLeaveWeight;
   protected int inHomeSleepWeight;
   protected int inHomeEatWeight;
   protected boolean allowSleep;
   protected boolean canSleepOutside;
   protected boolean canSleepOutsidePreferHome;
   protected boolean canSleepHome;
   protected int sleepHomeReturnLevel;
   protected int sleepHomeEnergyLevel;
   protected int sleepDelay;
   protected int sleepHomeBreakawayLevel;
   protected int sleepHomeBreakawayChance;
   protected int sleepOutsideEnergyLevel;
   protected int sleepOutsideChance;
   protected int sleepOutsideBreakawayLevel;
   protected int sleepOutsideBreakawayChance;
   protected int energyDecayRate;
   protected boolean allowIncapacitated;
   protected int incapacitatedBreakawayChance;
   protected boolean allowHunger;
   protected int hungerFindFoodChance;
   protected int hungerStarvationFoodSearchLevel;
   protected int hungerHungryFoodSearchLevel;
   protected int hungerSnackSearchLevel;
   protected int hungerHungryFoodSearchDistance;
   protected int hungerHarvestTime;
   protected int hungerEatTime;
   protected int hungerInHomeEatLevel;
   protected int hungerEatBreakawayLevel;
   protected int hungerEatBreakawayChance;
   protected int hungerDecayRate;
   protected int hungerStarvationRate;
   protected boolean allowThirst;
   protected int thirstFindWaterChance;
   protected int thirstDehydrationWaterSearchLevel;
   protected int thirstThirstyWaterSearchLevel;
   protected int thirstSnackSearchLevel;
   protected int thirstOverheatSearchLevel;
   protected int thirstThirstyWaterSearchDistance;
   protected int thirstHarvestTime;
   protected int thirstDrinkTime;
   protected int thirstInHomeDrinkLevel;
   protected int thirstDrinkBreakawayLevel;
   protected int thirstDrinkBreakawayChance;
   protected int thirstDecayRate;
   protected int thirstDehydrationRate;
   protected boolean allowFaith;
   protected int faithDecayRate;
   protected int faithInfluenceMinimum;
   protected int faithInfluenceMaximum;
   protected int faithInfluenceFixed;
   protected int faithLowLevel;
   protected boolean allowSocial;
   protected boolean canSocialMate;
   protected int socialMateChance;
   protected boolean canSocialBreed;
   protected int socialBreedChance;
   protected MobBase.MobType socialMateChildType;
   protected boolean canBeBless;
   protected MobBase.MobType socialMateChildBlessedType;
   protected boolean canSocialBeOwner;
   protected boolean canSocialHaveOwner;
   protected int socialOwnerChance;
   protected boolean canSocialTrade;
   protected int socialTradeChance;
   protected int socialInHomeCoitusChance;
   protected int socialCoitusDelay;
   protected int socialPregnantDelay;
   protected int socialCoitusChance;
   protected int socialCoitusEnergyUse;
   protected int socialCoitusHungerUse;
   protected int socialPregnantChance;
   protected int socialPregnantMaximum;
   protected int socialGestationDelay;
   protected int socialChildBirthChildDeathChance;
   protected int socialTwinsChance;
   protected int socialTripletsChance;
   protected int socialBirthEnergyDecrease;
   protected int socialBirthHungerDecrease;
   protected int socialChatChance;
   protected boolean socialChatPrefersVillage;
   protected boolean socialChatShareWisdom;
   protected int socialChatShareWisdomChance;
   protected boolean socialChatShareFaith;
   protected int socialChatShareFaithChance;
   protected int socialChatBreakawayChance;
   protected int socialChatSearchRadius;
   protected int socialChatInterruptChance;
   protected int socialRate;
   protected int socialSadnessLevel;
   protected int socialLonelyLevel;
   protected int socialBoredLevel;
   protected boolean allowTrade;
   protected int tradeChance;
   protected boolean allowWork;
   protected int workChance;
   protected int workEnergyCutoff;
   protected int workNearbyResourcesOnGroundDistance;
   protected int workFindExtraResourcesDistance;
   protected boolean allowCorruptionWork;
   protected int corruptionWorkChance;
   protected int corruptionWorkTime;
   protected int corruptionWorkHarvestDistance;
   protected int corruptionWorkNearbyResourcesOnGroundDistance;
   protected int corruptionWorkFindExtraResourcesDistance;
   protected int corruptionWorkNewBuildingDistance;
   protected boolean allowCombat;
   protected boolean allowCombatMelee;
   protected CollisionModule.CollisionHeight combatMeleeLineOfSight;
   protected boolean allowCombatRanged;
   protected MissileModule.MissileType combatRangedMissileType;
   protected int combatRangedRange;
   protected CollisionModule.CollisionHeight combatRangedLineOfSight;
   protected MobBase.DamageType combatDamageType;
   protected int combatSearchDistance;
   protected int combatAggroChance;
   protected int combatFleeSearchDistance;
   protected int combatFleeRandomDistance;
   protected int combatAttackBreakawayChance;
   protected int combatFleeBreakawayChance;
   protected boolean combatPrefersVillage;
   protected boolean combatCanAttackMobs;
   protected boolean combatCanAttackBuildings;
   protected int combatMeleeAttackBuildingRepositionChance;
   protected EnumMap<MobBase.DamageType, Integer> damageResistances = new EnumMap<>(MobBase.DamageType.class);
   protected EnumMap<MobBase.DamageType, Integer> damageVulnerabilities = new EnumMap<>(MobBase.DamageType.class);
   protected boolean allowOnFire;
   protected boolean onFireCanPanic;
   protected int onFirePanicChance;
   protected int onFirePanicDistance;
   protected int onFireAmountMaximum;
   protected boolean allowFrozen;
   protected int frozenAmountMaximum;
   protected boolean allowPoisoned;
   protected int poisonedAmountMaximum;
   protected boolean allowDysentery;
   protected int dysenteryAmountMaximum;
   protected boolean allowBlight;
   protected MobBase.MobType blightSpawnType;
   protected int blightAmountMaximum;
   protected boolean allowGhostBehavior;
   protected MobBase.MobType ghostBaseType;
   protected MobBase.MobType ghostNegativeBaseType;
   protected int ghostBehaviorChance;
   protected int ghostFadeInChance;
   protected int ghostFadeOutChance;
   protected int ghostFullMoonFadeChance;
   protected int ghostEclipseFadeChance;
   protected int ghostBloodMoonFadeChance;
   protected int ghostLeaveChance;
   protected boolean allowGolemBehavior;
   protected int golemBehaviorChance;
   protected boolean golemRequiresCharge;
   protected int golemRequiresChargeAmountMax;
   protected boolean allowHostileBehavior;
   protected int hostileBehaviorAttackChance;
   protected int hostileBehaviorBreakawayChance;
   protected int hostileBehaviorLeaveChance;
   protected boolean allowHostileBehaviorPack;
   protected int hostileBehaviorPackChance;
   protected int hostileBehaviorPackDistance;
   protected int hostileBehaviorPackMaximum;
   protected boolean allowNomadBehavior;
   protected int nomadBehaviorChance;
   protected MobBase.MobType nomadBehaviorMobType;
   protected int nomadBehaviorPackDistance;
   protected boolean allowProvisionerBehavior;
   protected int provisionerWanderBehaviorChance;
   protected int provisionerLeaveVillageChance;
   protected boolean allowLaborerBehavior;
   protected int laborerWanderBehaviorChance;
   protected int laborerLeaveVillageChance;
   protected boolean allowAnimalWanderBehavior;
   protected int animalWanderBehaviorChance;
   protected boolean animalWanderBehaviorPreferGroups;
   protected int animalWanderBehaviorPreferGroupsMaxiumumSize;
   protected ArrayList<ResourceModule.ResourceType> animalWanderBehaviorPreferredResources = new ArrayList<>();
   protected boolean allowWildDoggoWanderIn;
   protected int wildDoggoWanderInChanceDawn;
   protected int wildDoggoWanderInChanceMorning;
   protected int wildDoggoWanderInChanceMidday;
   protected int wildDoggoWanderInChanceEvening;
   protected int wildDoggoWanderInChanceDusk;
   protected int wildDoggoWanderInChanceNight;
   protected int wildDoggoWanderInPackDistance;
   protected boolean allowSpawnSearch;
   protected int spawnSearchChance;
   protected int spawnSearchDistance;
   protected boolean spawnSearchDieIfFail;
   protected boolean allowSpawnRepair;
   protected int spawnRepairChance;
   protected int spawnRepairTime;
   protected boolean allowGodExperience;
   protected boolean ignoreThreatenedTiles;
   protected boolean threatenTiles;
   protected boolean allowForcedPathfinding;
   protected boolean allowBoostedLevel;
   protected boolean allowGhost;
   protected MobBase.MobType ghostSpawnType;
   protected boolean diesSlowlyOffCorruption;
   protected int diesSlowlyPercentage;
   protected int diesSlowlyRate;
   protected byte lightRadius;
   protected byte lightIntensity;
   protected boolean invulnerable;
   protected float maximumAlpha = 1.0F;
   protected ParticleModule.ParticleSet bloodType;
   protected boolean movementTrail;
   protected int movementTrailChance;
   protected boolean allowDeathSplit;
   protected MobBase.MobType deathSplitType;
   protected int deathSplitChance;
   protected int deathSplitAmount;
   protected MobBase.MobType domesticateType;
   protected boolean allowProduceEggs;

   public Color[][] getColorLayersArray() {
      return this.colorLayersArrays;
   }

   public SoundModule.SoundSet getMobSoundSet(MobBase.MobGender g) {
      return this.mobSoundSets.containsKey(g) ? this.mobSoundSets.get(g) : this.mobSoundSets.get(MobBase.MobGender.NONE);
   }

   public int getIdleSoundRate() {
      return this.idleSoundRate;
   }

   public MapData.BlockMapGroup getBlockMapGroup() {
      return this.blockMapGroup;
   }

   public int getAggressiveness() {
      return this.aggressiveness;
   }

   public int getLevelCapLow() {
      return this.levelCapLow;
   }

   public int getLevelCapHigh() {
      return this.levelCapHigh;
   }

   public int getInfluenceMaintainCost() {
      return this.influenceMaintainCost;
   }

   public float getStatVelocityBase() {
      return this.statVelocityBase;
   }

   public float getStatVelocityMultiplier() {
      return this.statVelocityMultiplier;
   }

   public int getExperienceMultiplier() {
      return this.experienceMultiplier;
   }

   public int getStatsPerLevel() {
      return this.statsPerLevel;
   }

   public int getStatStrengthChance() {
      return this.statStrengthChance;
   }

   public int getStatDexterityChance() {
      return this.statDexterityChance;
   }

   public double getCarryMaxBonusMultiplier() {
      return this.carryMaxBonusMultiplier;
   }

   public boolean getAllowInventory() {
      return this.allowInventory;
   }

   public boolean getAllowInventoryRandomSpawnItems() {
      return this.allowInventoryRandomSpawnItems;
   }

   public boolean getAllowInventoryEquipBuildings() {
      return this.allowInventoryEquipBuildings;
   }

   public boolean getAllowInventoryEquipRestrictionOverride() {
      return this.allowInventoryEquipRestrictionOverride;
   }

   public int getInventoryRandomSpawnItemsChance() {
      return this.inventoryRandomSpawnItemsChance;
   }

   public int getInventorySearchChance() {
      return this.inventorySearchChance;
   }

   public int getInventoryNearbyResourcesOnGroundDistance() {
      return this.inventoryNearbyResourcesOnGroundDistance;
   }

   public boolean getCanInventorySearchVillage() {
      return this.canInventorySearchVillage;
   }

   public boolean getCanInventoryEquipHead() {
      return this.canInventoryEquipHead;
   }

   public boolean getCanInventoryEquipChest() {
      return this.canInventoryEquipChest;
   }

   public boolean getCanInventoryEquipTool() {
      return this.canInventoryEquipTool;
   }

   public boolean getCanInventoryEquipMeleeWeapon() {
      return this.canInventoryEquipMeleeWeapon;
   }

   public boolean getCanInventoryEquipShield() {
      return this.canInventoryEquipShield;
   }

   public boolean getCanInventoryEquipRecovery() {
      return this.canInventoryEquipRecovery;
   }

   public boolean getCanInventoryEquipWater() {
      return this.canInventoryEquipWater;
   }

   public boolean getCanInventoryEquipFood() {
      return this.canInventoryEquipFood;
   }

   public boolean getCanInventoryEquipRangedWeapon() {
      return this.canInventoryEquipRangedWeapon;
   }

   public boolean getCanInventoryEquipRangedWeaponAmmo() {
      return this.canInventoryEquipRangedWeaponAmmo;
   }

   public HashMap<ResourceModule.ResourceType, Integer> getDropsList() {
      return this.dropsList;
   }

   public int getDropsChance() {
      return this.dropsChance;
   }

   public int getDropsAttempts() {
      return this.dropsAttempts;
   }

   public boolean canDropsAllowSlaughter() {
      return this.dropsAllowSlaughter;
   }

   public boolean canMonsterSpawn() {
      return this.canMonsterSpawn;
   }

   public int getMonsterSpawnDayDefaultMinimum() {
      return this.monsterSpawnDayDefaultMinimum;
   }

   public int getMonsterSpawnDayDefaultMaximum() {
      return this.monsterSpawnDayDefaultMaximum;
   }

   public boolean canAnimalSpawn() {
      return this.canAnimalSpawn;
   }

   public int getAnimalSpawnDayDefaultMinimum() {
      return this.animalSpawnDayDefaultMinimum;
   }

   public int getAnimalSpawnDayDefaultMaximum() {
      return this.animalSpawnDayDefaultMaximum;
   }

   public int getAnimalSpawnAmountMinimum() {
      return this.animalSpawnAmountMinimum;
   }

   public int getAnimalSpawnAmountMaximum() {
      return this.animalSpawnAmountMaximum;
   }

   public int getAnimalSpawnAmountWeight() {
      return this.animalSpawnAmountWeight;
   }

   public boolean getAllowCourierBehavior() {
      return this.allowCourierBehavior;
   }

   public int getCourierBehaviorChance() {
      return this.courierBehaviorChance;
   }

   public int getCourierDepartRegionChance() {
      return this.courierDepartRegionChance;
   }

   public TimeModule.TimeOfDay getCourierDepartRegionTimeOfDay() {
      return this.courierDepartRegionTimeOfDay;
   }

   public int getCourierIdleWithResourcesDelay() {
      return this.courierIdleWithResourcesDelay;
   }

   public boolean getAllowMigrateDepartBehavior() {
      return this.allowMigrateDepartBehavior;
   }

   public int getMigrateDepartRegionChance() {
      return this.migrateDepartRegionChance;
   }

   public TimeModule.TimeOfDay getMigrateDepartTimeOfDay() {
      return this.migrateDepartTimeOfDay;
   }

   public MobBase.MobType getMigrantDepartArrivalType() {
      return this.migrantDepartArrivalType;
   }

   public boolean getAllowMigrationPreperationBehavior() {
      return this.allowMigrationPreperationBehavior;
   }

   public int getMigrationPreperationChance() {
      return this.migrationPreperationChance;
   }

   public TimeModule.TimeOfDay getMigrationPreperationTimeOfDay() {
      return this.migrationPreperationTimeOfDay;
   }

   public MobBase.MobType getMigrationPreperationMobType() {
      return this.migrationPreperationMobType;
   }

   public int getMigrationPreperationHungerPercent() {
      return this.migrateDepartHungerPercent;
   }

   public int getMigrationPreperationThirstPercent() {
      return this.migrationPreperationThirstPercent;
   }

   public int getMigrationPreperationAgePercent() {
      return this.migrationPreperationAgePercent;
   }

   public boolean getAllowAging() {
      return this.allowAging;
   }

   public int getAgingStartAgeMinimum() {
      return this.agingStartAgeMinimum;
   }

   public int getAgingStartAgeMaximum() {
      return this.agingStartAgeMaximum;
   }

   public int getAgingGrowUpAge() {
      return this.agingGrowUpAge;
   }

   public MobBase.MobType getAgingGrowUpType() {
      return this.agingGrowUpType;
   }

   public int getAgingElderAge() {
      return this.agingElderAge;
   }

   public MobBase.MobType getAgingElderType() {
      return this.agingElderType;
   }

   public int getAgingMaximumAge() {
      return this.agingMaximumAge;
   }

   public int getAgingRate() {
      return this.agingRate;
   }

   public boolean getAllowHealthRegen() {
      return this.allowHealthRegen;
   }

   public int getHealthRegenRate() {
      return this.healthRegenRate;
   }

   public int getAgingConstitutionDecayRate() {
      return this.agingConstitutionDecayRate;
   }

   public boolean getAllowHealthRepair() {
      return this.allowHealthRepair;
   }

   public int getHealthRepairChance() {
      return this.healthRepairChance;
   }

   public int getHealthRepairCriticalChance() {
      return this.healthRepairCriticalChance;
   }

   public double getHealthRepairPercentage() {
      return this.healthRepairPercentage;
   }

   public double getHealthRepairCriticalPercentage() {
      return this.healthRepairCriticalPercentage;
   }

   public boolean getAllowThermal() {
      return this.allowThermal;
   }

   public boolean getCanThermalFreeze() {
      return this.canThermalFreeze;
   }

   public boolean getCanThermalFreezeCritical() {
      return this.canThermalFreezeCritical;
   }

   public boolean getCanThermalOverheat() {
      return this.canThermalOverheat;
   }

   public boolean getCanThermalOverheatCritical() {
      return this.canThermalOverheatCritical;
   }

   public int getThermalBaseline() {
      return this.thermalBaseline;
   }

   public int getThermalFreeze() {
      return this.thermalFreeze;
   }

   public int getThermalFreezeCritical() {
      return this.thermalFreezeCritical;
   }

   public int getThermalOverheat() {
      return this.thermalOverheat;
   }

   public int getThermalOverheatCritical() {
      return this.thermalOverheatCritical;
   }

   public int getThermalIncreaseTempChance() {
      return this.thermalIncreaseTempChance;
   }

   public int getThermalAdjustRate() {
      return this.thermalAdjustRate;
   }

   public boolean getAllowWander() {
      return this.allowWander;
   }

   public int getWanderChance() {
      return this.wanderChance;
   }

   public boolean getWanderPreferVillage() {
      return this.wanderPreferVillage;
   }

   public boolean getWanderPreferCorruption() {
      return this.wanderPreferCorruption;
   }

   public boolean canWanderAimlessly() {
      return this.canWanderAimlessly;
   }

   public int getWanderAimlesslyWeight() {
      return this.wanderAimlesslyWeight;
   }

   public int getWanderAimlesslyDistance() {
      return this.wanderAimlesslyDistance;
   }

   public boolean canWanderObjects() {
      return this.canWanderObjects;
   }

   public int getWanderObjectWeight() {
      return this.wanderObjectWeight;
   }

   public boolean canWanderHome() {
      return this.canWanderHome;
   }

   public int getWanderHomeWeight() {
      return this.wanderHomeWeight;
   }

   public boolean canWanderMate() {
      return this.canWanderMate;
   }

   public int getWanderMateWeight() {
      return this.wanderMateWeight;
   }

   public boolean canWanderOwner() {
      return this.canWanderOwner;
   }

   public int getWanderOwnerWeight() {
      return this.wanderOwnerWeight;
   }

   public boolean canWanderSpawn() {
      return this.canWanderSpawn;
   }

   public int getWanderSpawnWeight() {
      return this.wanderSpawnWeight;
   }

   public int getWanderSpawnDistance() {
      return this.wanderSpawnDistance;
   }

   public boolean canWanderGhost() {
      return this.canWanderGhost;
   }

   public int getWanderGhostWeight() {
      return this.wanderGhostWeight;
   }

   public int getWanderGhostDistance() {
      return this.wanderGhostDistance;
   }

   public boolean getAllowConfused() {
      return this.allowConfused;
   }

   public int getConfusedChance() {
      return this.confusedChance;
   }

   public int getConfusedWanderChance() {
      return this.confusedWanderChance;
   }

   public int getConfusedBreakAwayChance() {
      return this.confusedBreakAwayChance;
   }

   public int getConfusedWanderDistance() {
      return this.confusedWanderDistance;
   }

   public boolean getAllowPanic() {
      return this.allowPanic;
   }

   public int getPanicChance() {
      return this.panicChance;
   }

   public int getPanicBreakAwayChance() {
      return this.panicBreakAwayChance;
   }

   public int getPanicDistance() {
      return this.panicDistance;
   }

   public boolean getAllowPanicHomeSearch() {
      return this.allowPanicHomeSearch;
   }

   public int getPanicHomeSearchDisance() {
      return this.panicHomeSearchDisance;
   }

   public int getPanicRunHomeChance() {
      return this.panicRunHomeChance;
   }

   public int getPanicHideInHomeDuration() {
      return this.panicHideInHomeDuration;
   }

   public boolean getAllowHome() {
      return this.allowHome;
   }

   public boolean getCanEnterHome() {
      return this.canEnterHome;
   }

   public int getHomeVisitChance() {
      return this.homeVisitChance;
   }

   public int getHomeFindChance() {
      return this.homeFindChance;
   }

   public int getHomeLeaveWeight() {
      return this.homeLeaveWeight;
   }

   public int getInHomeSleepWeight() {
      return this.inHomeSleepWeight;
   }

   public int getInHomeEatWeight() {
      return this.inHomeEatWeight;
   }

   public boolean getAllowSleep() {
      return this.allowSleep;
   }

   public boolean getCanSleepOutside() {
      return this.canSleepOutside;
   }

   public boolean getCanSleepOutsidePreferHome() {
      return this.canSleepOutsidePreferHome;
   }

   public boolean getCanSleepHome() {
      return this.canSleepHome;
   }

   public int getSleepHomeReturnLevel() {
      return this.sleepHomeReturnLevel;
   }

   public int getSleepHomeEnergyLevel() {
      return this.sleepHomeEnergyLevel;
   }

   public int getSleepDelay() {
      return this.sleepDelay;
   }

   public int getSleepHomeBreakawayLevel() {
      return this.sleepHomeBreakawayLevel;
   }

   public int getSleepHomeBreakawayChance() {
      return this.sleepHomeBreakawayChance;
   }

   public int getSleepOutsideEnergyLevel() {
      return this.sleepOutsideEnergyLevel;
   }

   public int getSleepOutsideChance() {
      return this.sleepOutsideChance;
   }

   public int getSleepOutsideBreakawayLevel() {
      return this.sleepOutsideBreakawayLevel;
   }

   public int getSleepOutsideBreakawayChance() {
      return this.sleepOutsideBreakawayChance;
   }

   public int getEnergyDecayRate() {
      return this.energyDecayRate;
   }

   public boolean getAllowIncapacitated() {
      return this.allowIncapacitated;
   }

   public int getIncapacitatedBreakawayChance() {
      return this.incapacitatedBreakawayChance;
   }

   public boolean getAllowHunger() {
      return this.allowHunger;
   }

   public int getHungerFindFoodChance() {
      return this.hungerFindFoodChance;
   }

   public int getHungerStarvationFoodSearchLevel() {
      return this.hungerStarvationFoodSearchLevel;
   }

   public int getHungerHungryFoodSearchLevel() {
      return this.hungerHungryFoodSearchLevel;
   }

   public int getHungerSnackSearchLevel() {
      return this.hungerSnackSearchLevel;
   }

   public int getHungerHungryFoodSearchDistance() {
      return this.hungerHungryFoodSearchDistance;
   }

   public int getHungerHarvestTime() {
      return this.hungerHarvestTime;
   }

   public int getHungerEatTime() {
      return this.hungerEatTime;
   }

   public int getHungerInHomeEatLevel() {
      return this.hungerInHomeEatLevel;
   }

   public int getHungerEatBreakawayLevel() {
      return this.hungerEatBreakawayLevel;
   }

   public int getHungerEatBreakawayChance() {
      return this.hungerEatBreakawayChance;
   }

   public int getHungerDecayRate() {
      return this.hungerDecayRate;
   }

   public int getHungerStarvationRate() {
      return this.hungerStarvationRate;
   }

   public boolean getAllowThirst() {
      return this.allowThirst;
   }

   public int getThirstFindWaterChance() {
      return this.thirstFindWaterChance;
   }

   public int getThirstDehydrationWaterSearchLevel() {
      return this.thirstDehydrationWaterSearchLevel;
   }

   public int getThirstThirstyWaterSearchLevel() {
      return this.thirstThirstyWaterSearchLevel;
   }

   public int getThirstSnackSearchLevel() {
      return this.thirstSnackSearchLevel;
   }

   public int getThirstOverheatSearchLevel() {
      return this.thirstOverheatSearchLevel;
   }

   public int getThirstThirstyWaterSearchDistance() {
      return this.thirstThirstyWaterSearchDistance;
   }

   public int getThirstHarvestTime() {
      return this.thirstHarvestTime;
   }

   public int getThirstDrinkTime() {
      return this.thirstDrinkTime;
   }

   public int getThirstInHomeDrinkLevel() {
      return this.thirstInHomeDrinkLevel;
   }

   public int getThirstDrinkBreakawayLevel() {
      return this.thirstDrinkBreakawayLevel;
   }

   public int getThirstDrinkBreakawayChance() {
      return this.thirstDrinkBreakawayChance;
   }

   public int getThirstDecayRate() {
      return this.thirstDecayRate;
   }

   public int getThirstDehydrationRate() {
      return this.thirstDehydrationRate;
   }

   public boolean getAllowFaith() {
      return this.allowFaith;
   }

   public int getFaithDecayRate() {
      return this.faithDecayRate;
   }

   public int getFaithInfluenceMinimum() {
      return this.faithInfluenceMinimum;
   }

   public int getFaithInfluenceMaximum() {
      return this.faithInfluenceMaximum;
   }

   public int getFaithInfluenceFixed() {
      return this.faithInfluenceFixed;
   }

   public int getFaithLowLevel() {
      return this.faithLowLevel;
   }

   public boolean getAllowSocial() {
      return this.allowSocial;
   }

   public boolean canSocialMate() {
      return this.canSocialMate;
   }

   public int getSocialMateChance() {
      return this.socialMateChance;
   }

   public boolean canSocialBreed() {
      return this.canSocialBreed;
   }

   public int getSocialBreedChance() {
      return this.socialBreedChance;
   }

   public MobBase.MobType getSocialMateChildType() {
      return this.socialMateChildType;
   }

   public boolean canBeBless() {
      return this.canBeBless;
   }

   public MobBase.MobType getSocialMateChildBlessedType() {
      return this.socialMateChildBlessedType;
   }

   public boolean canSocialBeOwner() {
      return this.canSocialBeOwner;
   }

   public boolean canSocialHaveOwner() {
      return this.canSocialHaveOwner;
   }

   public int getSocialOwnerChance() {
      return this.socialOwnerChance;
   }

   public boolean canSocialTrade() {
      return this.canSocialTrade;
   }

   public int getSocialTradeChance() {
      return this.socialTradeChance;
   }

   public int getSocialInHomeCoitusChance() {
      return this.socialInHomeCoitusChance;
   }

   public int getSocialCoitusDelay() {
      return this.socialCoitusDelay;
   }

   public int getSocialPregnantDelay() {
      return this.socialPregnantDelay;
   }

   public int getSocialCoitusChance() {
      return this.socialCoitusChance;
   }

   public int getSocialCoitusEnergyUse() {
      return this.socialCoitusEnergyUse;
   }

   public int getSocialCoitusHungerUse() {
      return this.socialCoitusHungerUse;
   }

   public int getSocialPregnantChance() {
      return this.socialPregnantChance;
   }

   public int getSocialPregnantMaximum() {
      return this.socialPregnantMaximum;
   }

   public int getSocialGestationDelay() {
      return this.socialGestationDelay;
   }

   public int getSocialChildBirthChildDeathChance() {
      return this.socialChildBirthChildDeathChance;
   }

   public int getSocialTwinsChance() {
      return this.socialTwinsChance;
   }

   public int getSocialTripletsChance() {
      return this.socialTripletsChance;
   }

   public int getSocialBirthEnergyDecrease() {
      return this.socialBirthEnergyDecrease;
   }

   public int getSocialBirthHungerDecrease() {
      return this.socialBirthHungerDecrease;
   }

   public int getSocialChatChance() {
      return this.socialChatChance;
   }

   public boolean getSocialChatPrefersVillage() {
      return this.socialChatPrefersVillage;
   }

   public boolean getSocialChatShareWisdom() {
      return this.socialChatShareWisdom;
   }

   public int getSocialChatShareWisdomChance() {
      return this.socialChatShareWisdomChance;
   }

   public boolean getSocialChatShareFaith() {
      return this.socialChatShareFaith;
   }

   public int getSocialChatShareFaithChance() {
      return this.socialChatShareFaithChance;
   }

   public int getSocialChatBreakawayChance() {
      return this.socialChatBreakawayChance;
   }

   public int getSocialChatSearchRadius() {
      return this.socialChatSearchRadius;
   }

   public int getSocialChatInterruptChance() {
      return this.socialChatInterruptChance;
   }

   public int getSocialRate() {
      return this.socialRate;
   }

   public int getSocialSadnessLevel() {
      return this.socialSadnessLevel;
   }

   public int getSocialLonelyLevel() {
      return this.socialLonelyLevel;
   }

   public int getSocialBoredLevel() {
      return this.socialBoredLevel;
   }

   public boolean getAllowTrade() {
      return this.allowTrade;
   }

   public int getTradeChance() {
      return this.tradeChance;
   }

   public boolean getAllowWork() {
      return this.allowWork;
   }

   public int getWorkChance() {
      return this.workChance;
   }

   public int getWorkEnergyCutoff() {
      return this.workEnergyCutoff;
   }

   public int getWorkNearbyResourcesOnGroundDistance() {
      return this.workNearbyResourcesOnGroundDistance;
   }

   public int getWorkFindExtraResourcesDistance() {
      return this.workFindExtraResourcesDistance;
   }

   public boolean getAllowCorruptionWork() {
      return this.allowCorruptionWork;
   }

   public int getCorruptionWorkChance() {
      return this.corruptionWorkChance;
   }

   public int getCorruptionWorkTime() {
      return this.corruptionWorkTime;
   }

   public int getCorruptionWorkHarvestDistance() {
      return this.corruptionWorkHarvestDistance;
   }

   public int getCorruptionWorkNearbyResourcesOnGroundDistance() {
      return this.corruptionWorkNearbyResourcesOnGroundDistance;
   }

   public int getCorruptionWorkFindExtraResourcesDistance() {
      return this.corruptionWorkFindExtraResourcesDistance;
   }

   public int getCorruptionWorkNewBuildingDistance() {
      return this.corruptionWorkNewBuildingDistance;
   }

   public boolean getAllowCombat() {
      return this.allowCombat;
   }

   public boolean getAllowCombatMelee() {
      return this.allowCombatMelee;
   }

   public CollisionModule.CollisionHeight getCombatMeleeLineOfSight() {
      return this.combatMeleeLineOfSight;
   }

   public boolean getAllowCombatRanged() {
      return this.allowCombatRanged;
   }

   public MissileModule.MissileType getCombatRangedMissileType() {
      return this.combatRangedMissileType;
   }

   public int getCombatRangedRange() {
      return this.combatRangedRange;
   }

   public CollisionModule.CollisionHeight getCombatRangedLineOfSight() {
      return this.combatRangedLineOfSight;
   }

   public MobBase.DamageType getCombatDamageType() {
      return this.combatDamageType;
   }

   public int getCombatSearchDistance() {
      return this.combatSearchDistance;
   }

   public int getCombatAggroChance() {
      return this.combatAggroChance;
   }

   public int getCombatFleeSearchDistance() {
      return this.combatFleeSearchDistance;
   }

   public int getCombatFleeRandomDistance() {
      return this.combatFleeRandomDistance;
   }

   public int getCombatAttackBreakawayChance() {
      return this.combatAttackBreakawayChance;
   }

   public int getCombatFleeBreakawayChance() {
      return this.combatFleeBreakawayChance;
   }

   public boolean getCombatPrefersVillage() {
      return this.combatPrefersVillage;
   }

   public boolean combatCanAttackMobs() {
      return this.combatCanAttackMobs;
   }

   public boolean combatCanAttackBuildings() {
      return this.combatCanAttackBuildings;
   }

   public int getCombatMeleeAttackBuildingRepositionChance() {
      return this.combatMeleeAttackBuildingRepositionChance;
   }

   public EnumMap<MobBase.DamageType, Integer> getDamageResistances() {
      return this.damageResistances;
   }

   public EnumMap<MobBase.DamageType, Integer> getDamageVulnerabilities() {
      return this.damageVulnerabilities;
   }

   public boolean getAllowOnFire() {
      return this.allowOnFire;
   }

   public boolean getOnFireCanPanic() {
      return this.onFireCanPanic;
   }

   public int getOnFirePanicChance() {
      return this.onFirePanicChance;
   }

   public int getOnFirePanicDistance() {
      return this.onFirePanicDistance;
   }

   public int getOnFireAmountMaximum() {
      return this.onFireAmountMaximum;
   }

   public boolean getAllowFrozen() {
      return this.allowFrozen;
   }

   public int getFrozenAmountMaximum() {
      return this.frozenAmountMaximum;
   }

   public boolean getAllowPoisoned() {
      return this.allowPoisoned;
   }

   public int getPoisonedAmountMaximum() {
      return this.poisonedAmountMaximum;
   }

   public boolean getAllowDysentery() {
      return this.allowDysentery;
   }

   public int getDysenteryAmountMaximum() {
      return this.dysenteryAmountMaximum;
   }

   public boolean getAllowBlighted() {
      return this.allowBlight;
   }

   public MobBase.MobType getBlightSpawnType() {
      return this.blightSpawnType;
   }

   public int getBlightedAmountMaximum() {
      return this.blightAmountMaximum;
   }

   public boolean getAllowGhostBehavior() {
      return this.allowGhostBehavior;
   }

   public MobBase.MobType getGhostBaseType() {
      return this.ghostBaseType;
   }

   public MobBase.MobType getGhostNegativeBaseType() {
      return this.ghostNegativeBaseType;
   }

   public int getGhostBehaviorChance() {
      return this.ghostBehaviorChance;
   }

   public int getGhostFadeInChance() {
      return this.ghostFadeInChance;
   }

   public int getGhostFadeOutChance() {
      return this.ghostFadeOutChance;
   }

   public int getGhostFullMoonFadeChance() {
      return this.ghostFullMoonFadeChance;
   }

   public int getGhostEclipseFadeChance() {
      return this.ghostEclipseFadeChance;
   }

   public int getGhostBloodMoonFadeChance() {
      return this.ghostBloodMoonFadeChance;
   }

   public int getGhostLeaveChance() {
      return this.ghostLeaveChance;
   }

   public boolean getAllowGolemBehavior() {
      return this.allowGolemBehavior;
   }

   public int getGolemBehaviorChance() {
      return this.golemBehaviorChance;
   }

   public boolean isGolemRequiresCharge() {
      return this.golemRequiresCharge;
   }

   public int getGolemRequiresChargeAmountMax() {
      return this.golemRequiresChargeAmountMax;
   }

   public boolean getAllowHostileBehavior() {
      return this.allowHostileBehavior;
   }

   public int getHostileBehaviorAttackChance() {
      return this.hostileBehaviorAttackChance;
   }

   public int getHostileBehaviorBreakawayChance() {
      return this.hostileBehaviorBreakawayChance;
   }

   public int getHostileBehaviorLeaveChance() {
      return this.hostileBehaviorLeaveChance;
   }

   public boolean getAllowHostileBehaviorPack() {
      return this.allowHostileBehaviorPack;
   }

   public int getHostileBehaviorPackChance() {
      return this.hostileBehaviorPackChance;
   }

   public int getHostileBehaviorPackDistance() {
      return this.hostileBehaviorPackDistance;
   }

   public int getHostileBehaviorPackMaximum() {
      return this.hostileBehaviorPackMaximum;
   }

   public boolean getAllowNomadBehavior() {
      return this.allowNomadBehavior;
   }

   public int getNomadBehaviorChance() {
      return this.nomadBehaviorChance;
   }

   public MobBase.MobType getNomadBehaviorMobType() {
      return this.nomadBehaviorMobType;
   }

   public int getNomadBehaviorPackDistance() {
      return this.nomadBehaviorPackDistance;
   }

   public boolean getAllowProvisionerBehavior() {
      return this.allowProvisionerBehavior;
   }

   public int getProvisionerWanderBehaviorChance() {
      return this.provisionerWanderBehaviorChance;
   }

   public int getProvisionerLeaveVillageChance() {
      return this.provisionerLeaveVillageChance;
   }

   public boolean getAllowLaborerBehavior() {
      return this.allowLaborerBehavior;
   }

   public int getLaborerWanderBehaviorChance() {
      return this.laborerWanderBehaviorChance;
   }

   public int getLaborerLeaveVillageChance() {
      return this.laborerLeaveVillageChance;
   }

   public boolean getAllowAnimalWanderBehavior() {
      return this.allowAnimalWanderBehavior;
   }

   public int getAnimalWanderBehaviorChance() {
      return this.animalWanderBehaviorChance;
   }

   public boolean isAnimalWanderBehaviorPreferGroups() {
      return this.animalWanderBehaviorPreferGroups;
   }

   public int getAnimalWanderBehaviorPreferGroupsMaxiumumSize() {
      return this.animalWanderBehaviorPreferGroupsMaxiumumSize;
   }

   public ArrayList<ResourceModule.ResourceType> getAnimalWanderBehaviorPreferredResources() {
      return this.animalWanderBehaviorPreferredResources;
   }

   public boolean getAllowWildDoggoWanderIn() {
      return this.allowWildDoggoWanderIn;
   }

   public int getWildDoggoWanderInChanceDawn() {
      return this.wildDoggoWanderInChanceDawn;
   }

   public int getWildDoggoWanderInChanceMorning() {
      return this.wildDoggoWanderInChanceMorning;
   }

   public int getWildDoggoWanderInChanceMidday() {
      return this.wildDoggoWanderInChanceMidday;
   }

   public int getWildDoggoWanderInChanceEvening() {
      return this.wildDoggoWanderInChanceEvening;
   }

   public int getWildDoggoWanderInChanceDusk() {
      return this.wildDoggoWanderInChanceDusk;
   }

   public int getWildDoggoWanderInChanceNight() {
      return this.wildDoggoWanderInChanceNight;
   }

   public int getWildDoggoWanderInPackDistance() {
      return this.wildDoggoWanderInPackDistance;
   }

   public boolean getAllowSpawnSearch() {
      return this.allowSpawnSearch;
   }

   public int getSpawnSearchChance() {
      return this.spawnSearchChance;
   }

   public int getSpawnSearchDistance() {
      return this.spawnSearchDistance;
   }

   public boolean getSpawnSearchDieIfFail() {
      return this.spawnSearchDieIfFail;
   }

   public boolean getAllowSpawnRepair() {
      return this.allowSpawnRepair;
   }

   public int getSpawnRepairChance() {
      return this.spawnRepairChance;
   }

   public int getSpawnRepairTime() {
      return this.spawnRepairTime;
   }

   public boolean getAllowGodExperience() {
      return this.allowGodExperience;
   }

   public boolean canIgnoreThreatenedTiles() {
      return this.ignoreThreatenedTiles;
   }

   public boolean canThreatenTiles() {
      return this.threatenTiles;
   }

   public boolean getAllowForcedPathfinding() {
      return this.allowForcedPathfinding;
   }

   public boolean getAllowBoostedLevel() {
      return this.allowBoostedLevel;
   }

   public boolean getAllowGhost() {
      return this.allowGhost;
   }

   public MobBase.MobType getGhostSpawnType() {
      return this.ghostSpawnType;
   }

   public boolean getDiesSlowlyOffCorruption() {
      return this.diesSlowlyOffCorruption;
   }

   public int getDiesSlowlyPercentage() {
      return this.diesSlowlyPercentage;
   }

   public int getDiesSlowlyRate() {
      return this.diesSlowlyRate;
   }

   public byte getLightRadius() {
      return this.lightRadius;
   }

   public byte getLightIntensity() {
      return this.lightIntensity;
   }

   public boolean isInvulnerable() {
      return this.invulnerable;
   }

   public float getMaximumAlpha() {
      return this.maximumAlpha;
   }

   public ParticleModule.ParticleSet getBloodType() {
      return this.bloodType;
   }

   public boolean getMovementTrail() {
      return this.movementTrail;
   }

   public int getMovementTrailChance() {
      return this.movementTrailChance;
   }

   public boolean getAllowDeathSplit() {
      return this.allowDeathSplit;
   }

   public MobBase.MobType getDeathSplitType() {
      return this.deathSplitType;
   }

   public int getDeathSplitChance() {
      return this.deathSplitChance;
   }

   public int getDeathSplitAmount() {
      return this.deathSplitAmount;
   }

   public MobBase.MobType getDomesticateType() {
      return this.domesticateType;
   }

   public boolean allowProduceEggs() {
      return this.allowProduceEggs;
   }

   protected void configPerkAdjustments() {
   }

   protected void perkAdjustment(PerkModule.PerkType perkType) {
      if (!(this.perk.getPerkTypeTotal(perkType) <= 0.0)) {
         if (perkType == PerkModule.PerkType.GHOST_APPEARANCE_CHANCE) {
            this.ghostFadeInChance = (int)(this.ghostFadeInChance - this.ghostFadeInChance * this.perk.getPerkTypeTotal(perkType));
         }

         if (perkType == PerkModule.PerkType.VILLAGER_COLD_RESISTANCE) {
            this.thermalFreeze = (int)(this.thermalFreeze - this.perk.getPerkTypeTotal(perkType));
         }

         if (perkType == PerkModule.PerkType.VILLAGER_HEAT_RESISTANCE) {
            this.thermalOverheat = (int)(this.thermalOverheat + this.perk.getPerkTypeTotal(perkType));
         }

         if (perkType == PerkModule.PerkType.VILLAGER_LIFESPANS) {
            this.agingMaximumAge = (int)(this.agingMaximumAge + this.agingMaximumAge * this.perk.getPerkTypeTotal(perkType));
         }

         if (perkType == PerkModule.PerkType.WISER_ELDERS) {
            this.socialChatShareWisdomChance = (int)(this.socialChatShareWisdomChance - this.socialChatShareWisdomChance * this.perk.getPerkTypeTotal(perkType));
            this.socialChatShareFaithChance = (int)(this.socialChatShareFaithChance - this.socialChatShareFaithChance * this.perk.getPerkTypeTotal(perkType));
         }

         if (perkType == PerkModule.PerkType.HOLY_GOLEM_SPELL_COST || perkType == PerkModule.PerkType.LABOR_GOLEM_SPELL_COST) {
            this.influenceMaintainCost = (int)(this.influenceMaintainCost - this.influenceMaintainCost * this.perk.getPerkTypeTotal(perkType));
         }
      }
   }
}
