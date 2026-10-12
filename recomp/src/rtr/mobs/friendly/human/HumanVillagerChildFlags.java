package rtr.mobs.friendly.human;

import org.newdawn.slick.Color;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.map.CollisionModule;
import rtr.map.MapData;
import rtr.mobs.MobBase;
import rtr.mobs.MobFlagsBase;
import rtr.particles.ParticleModule;
import rtr.resources.ResourceModule;

public class HumanVillagerChildFlags extends MobFlagsBase {
   public HumanVillagerChildFlags() {
      this.colorLayersArrays[0] = new Color[]{new Color(-1728397), new Color(-4559799), new Color(-9420487)};
      this.colorLayersArrays[1] = new Color[]{
         new Color(-16185338),
         new Color(-13884885),
         new Color(-9346214),
         new Color(-4741474),
         new Color(-2702142),
         new Color(-3489871),
         new Color(-2305862),
         new Color(-1651032),
         new Color(-1718104),
         new Color(-2179943),
         new Color(-4679816),
         new Color(-5936314),
         new Color(-4894151),
         new Color(-7517629),
         new Color(-7252675),
         new Color(-11322062),
         new Color(-12898268),
         new Color(-11188168),
         new Color(-11648193),
         new Color(-11516860),
         new Color(-9810366),
         new Color(-5798550),
         new Color(-6850207)
      };
      this.colorLayersArrays[2] = new Color[]{new Color(-9539112)};
      this.colorLayersArrays[3] = new Color[]{new Color(-13684387)};
      this.mobSoundSets.put(MobBase.MobGender.MALE, SoundModule.SoundSet.VILLAGER_MALE);
      this.mobSoundSets.put(MobBase.MobGender.FEMALE, SoundModule.SoundSet.VILLAGER_FEMALE);
      this.idleSoundRate = 2000;
      this.blockMapGroup = MapData.BlockMapGroup.STANDARD;
      this.aggressiveness = -100;
      this.levelCapLow = 1;
      this.levelCapHigh = 2;
      this.statVelocityBase = 0.8F;
      this.statVelocityMultiplier = 0.0014F;
      this.experienceMultiplier = 100;
      this.statsPerLevel = 25;
      this.statStrengthChance = 10;
      this.statDexterityChance = 10;
      this.allowInventory = true;
      this.allowInventoryEquipBuildings = true;
      this.inventorySearchChance = 100;
      this.inventoryNearbyResourcesOnGroundDistance = 8;
      this.canInventorySearchVillage = true;
      this.canInventoryEquipRecovery = true;
      this.canInventoryEquipWater = true;
      this.canInventoryEquipFood = true;
      this.dropsList.put(ResourceModule.ResourceType.RAW_MEAT, 1);
      this.dropsChance = 2;
      this.dropsAttempts = 1;
      this.allowAging = true;
      this.agingGrowUpAge = 3;
      this.agingGrowUpType = MobBase.MobType.HUMAN_VILLAGER;
      this.agingMaximumAge = 40;
      this.agingRate = this.time.getFullDayLength();
      this.agingConstitutionDecayRate = 50000;
      this.allowHealthRegen = true;
      this.healthRegenRate = 350;
      this.allowHealthRepair = true;
      this.healthRepairChance = 100;
      this.healthRepairCriticalChance = 5;
      this.healthRepairPercentage = 0.6;
      this.healthRepairCriticalPercentage = 0.2;
      this.allowThermal = true;
      this.canThermalFreeze = true;
      this.canThermalFreezeCritical = true;
      this.canThermalOverheat = true;
      this.canThermalOverheatCritical = true;
      this.thermalBaseline = 50;
      this.thermalFreeze = 20;
      this.thermalFreezeCritical = 10;
      this.thermalOverheat = 80;
      this.thermalOverheatCritical = 90;
      this.thermalIncreaseTempChance = 300;
      this.thermalAdjustRate = 1500;
      this.allowWander = true;
      this.wanderChance = 100;
      this.wanderPreferVillage = true;
      this.canWanderAimlessly = true;
      this.wanderAimlesslyWeight = 10;
      this.wanderAimlesslyDistance = 12;
      this.canWanderObjects = true;
      this.wanderObjectWeight = 50;
      this.canWanderHome = true;
      this.wanderHomeWeight = 1000;
      this.allowConfused = true;
      this.confusedChance = 7000;
      this.confusedWanderChance = 1000;
      this.confusedBreakAwayChance = 1000;
      this.confusedWanderDistance = 6;
      this.allowPanic = true;
      this.panicChance = 50;
      this.panicBreakAwayChance = 1500;
      this.panicDistance = 10;
      this.allowPanicHomeSearch = true;
      this.panicHomeSearchDisance = 16;
      this.panicRunHomeChance = 4;
      this.panicHideInHomeDuration = 3500;
      this.allowHome = true;
      this.canEnterHome = true;
      this.homeVisitChance = 1500;
      this.homeFindChance = 10;
      this.homeLeaveWeight = 1500;
      this.inHomeSleepWeight = 100;
      this.inHomeEatWeight = 100;
      this.allowSleep = true;
      this.canSleepOutside = true;
      this.canSleepHome = true;
      this.sleepHomeReturnLevel = 20;
      this.sleepHomeEnergyLevel = 50;
      this.sleepDelay = 50;
      this.sleepHomeBreakawayLevel = 80;
      this.sleepHomeBreakawayChance = 5;
      this.sleepOutsideEnergyLevel = 5;
      this.sleepOutsideChance = 100;
      this.sleepOutsideBreakawayLevel = 40;
      this.sleepOutsideBreakawayChance = 10;
      this.energyDecayRate = 1000;
      this.allowIncapacitated = true;
      this.incapacitatedBreakawayChance = 8000;
      this.allowHunger = true;
      this.hungerFindFoodChance = 20;
      this.hungerStarvationFoodSearchLevel = 10;
      this.hungerHungryFoodSearchLevel = 30;
      this.hungerSnackSearchLevel = 70;
      this.hungerHungryFoodSearchDistance = 16;
      this.hungerHarvestTime = 1000;
      this.hungerEatTime = 60;
      this.hungerInHomeEatLevel = 50;
      this.hungerEatBreakawayLevel = 75;
      this.hungerEatBreakawayChance = 3;
      this.hungerDecayRate = 1500;
      this.hungerStarvationRate = 500;
      this.allowThirst = true;
      this.thirstFindWaterChance = 20;
      this.thirstDehydrationWaterSearchLevel = 10;
      this.thirstThirstyWaterSearchLevel = 20;
      this.thirstSnackSearchLevel = 50;
      this.thirstOverheatSearchLevel = 90;
      this.thirstThirstyWaterSearchDistance = 16;
      this.thirstHarvestTime = 200;
      this.thirstDrinkTime = 30;
      this.thirstInHomeDrinkLevel = 50;
      this.thirstDrinkBreakawayLevel = 75;
      this.thirstDrinkBreakawayChance = 300;
      this.thirstDecayRate = 2000;
      this.thirstDehydrationRate = 500;
      this.allowFaith = true;
      this.faithDecayRate = 10000;
      this.faithInfluenceMinimum = 5;
      this.faithInfluenceMaximum = 20;
      this.faithLowLevel = 5;
      this.allowSocial = true;
      this.canSocialTrade = true;
      this.canSocialBeOwner = true;
      this.socialTradeChance = 3;
      this.socialChatChance = 300;
      this.socialChatPrefersVillage = true;
      this.socialChatBreakawayChance = 2000;
      this.socialChatSearchRadius = 8;
      this.socialChatInterruptChance = 5;
      this.socialRate = 500;
      this.socialSadnessLevel = 10;
      this.socialLonelyLevel = 40;
      this.socialBoredLevel = 70;
      this.allowTrade = true;
      this.tradeChance = 80;
      this.allowCombat = true;
      this.allowCombatMelee = true;
      this.combatMeleeLineOfSight = CollisionModule.CollisionHeight.HIGH;
      this.combatDamageType = MobBase.DamageType.CRUSHING;
      this.combatSearchDistance = 6;
      this.combatAggroChance = 220;
      this.combatFleeSearchDistance = 32;
      this.combatFleeRandomDistance = 24;
      this.combatAttackBreakawayChance = 300;
      this.combatFleeBreakawayChance = 300;
      this.combatPrefersVillage = true;
      this.combatCanAttackMobs = true;
      this.allowOnFire = true;
      this.onFireCanPanic = true;
      this.onFirePanicChance = 50;
      this.onFirePanicDistance = 8;
      this.onFireAmountMaximum = 5000;
      this.allowFrozen = true;
      this.frozenAmountMaximum = 5000;
      this.allowPoisoned = true;
      this.poisonedAmountMaximum = 15000;
      this.allowDysentery = true;
      this.dysenteryAmountMaximum = 15000;
      this.allowBlight = true;
      this.blightSpawnType = MobBase.MobType.ZOMBIE_CHILD;
      this.blightAmountMaximum = 30000;
      this.allowGodExperience = true;
      this.ignoreThreatenedTiles = true;
      this.allowGhost = true;
      this.ghostSpawnType = MobBase.MobType.HUMAN_VILLAGER_GHOST_CHILD;
      this.maximumAlpha = 1.0F;
      this.bloodType = ParticleModule.ParticleSet.BLOOD_RED;
      this.configPerkAdjustments();
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.VILLAGER_COLD_RESISTANCE);
      this.perkAdjustment(PerkModule.PerkType.VILLAGER_HEAT_RESISTANCE);
      this.perkAdjustment(PerkModule.PerkType.VILLAGER_LIFESPANS);
   }
}
