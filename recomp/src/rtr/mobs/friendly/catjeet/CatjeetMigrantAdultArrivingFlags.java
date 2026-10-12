package rtr.mobs.friendly.catjeet;

import org.newdawn.slick.Color;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.map.CollisionModule;
import rtr.map.MapData;
import rtr.mobs.MobBase;
import rtr.mobs.MobFlagsBase;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class CatjeetMigrantAdultArrivingFlags extends MobFlagsBase {
   public CatjeetMigrantAdultArrivingFlags() {
      this.colorLayersArrays[0] = new Color[]{
         new Color(-15724528),
         new Color(-12969207),
         new Color(-10528435),
         new Color(-7310758),
         new Color(-3489122),
         new Color(-3487553),
         new Color(-6725583),
         new Color(-13295341)
      };
      this.colorLayersArrays[1] = Utilities.returnColorArrayBrighter(this.colorLayersArrays[0], 1.0F);
      this.colorLayersArrays[2] = new Color[]{new Color(-12881034)};
      this.colorLayersArrays[3] = new Color[]{new Color(-12368506)};
      this.colorLayersArrays[4] = Utilities.returnColorArrayDarker(this.colorLayersArrays[0], 0.5F);
      this.colorLayersArrays[6] = Utilities.returnColorArrayDarker(this.colorLayersArrays[0], 0.2F);
      this.mobSoundSets.put(MobBase.MobGender.MALE, SoundModule.SoundSet.VILLAGER_MALE);
      this.mobSoundSets.put(MobBase.MobGender.FEMALE, SoundModule.SoundSet.VILLAGER_FEMALE);
      this.idleSoundRate = 2000;
      this.blockMapGroup = MapData.BlockMapGroup.STANDARD_NO_CORRUPTION;
      this.aggressiveness = 0;
      this.levelCapLow = 4;
      this.levelCapHigh = 6;
      this.statVelocityBase = 0.65F;
      this.statVelocityMultiplier = 0.0015F;
      this.experienceMultiplier = 120;
      this.statsPerLevel = 25;
      this.statStrengthChance = 10;
      this.statDexterityChance = 5;
      this.allowInventory = true;
      this.inventorySearchChance = 100;
      this.inventoryNearbyResourcesOnGroundDistance = 8;
      this.canInventoryEquipHead = true;
      this.canInventoryEquipChest = true;
      this.canInventoryEquipTool = true;
      this.canInventoryEquipMeleeWeapon = true;
      this.canInventoryEquipShield = true;
      this.canInventoryEquipRecovery = true;
      this.canInventoryEquipWater = true;
      this.canInventoryEquipFood = true;
      this.canInventoryEquipRangedWeapon = true;
      this.canInventoryEquipRangedWeaponAmmo = true;
      this.allowAging = true;
      this.agingStartAgeMinimum = 3;
      this.agingStartAgeMaximum = 6;
      this.agingElderAge = 30;
      this.agingElderType = MobBase.MobType.CATJEET_NOMAD_ELDER;
      this.agingMaximumAge = 35;
      this.agingRate = this.time.getFullDayLength();
      this.agingConstitutionDecayRate = 50000;
      this.allowHealthRegen = true;
      this.healthRegenRate = 500;
      this.allowHealthRepair = true;
      this.healthRepairChance = 100;
      this.healthRepairCriticalChance = 5;
      this.healthRepairPercentage = 0.4;
      this.healthRepairCriticalPercentage = 0.1;
      this.allowThermal = true;
      this.canThermalFreeze = true;
      this.canThermalFreezeCritical = true;
      this.canThermalOverheat = true;
      this.canThermalOverheatCritical = true;
      this.thermalBaseline = 50;
      this.thermalFreeze = 20;
      this.thermalFreezeCritical = 5;
      this.thermalOverheat = 80;
      this.thermalOverheatCritical = 95;
      this.thermalIncreaseTempChance = 400;
      this.thermalAdjustRate = 2000;
      this.allowWander = true;
      this.wanderChance = 500;
      this.canWanderAimlessly = true;
      this.wanderAimlesslyWeight = 10;
      this.wanderAimlesslyDistance = 8;
      this.allowConfused = true;
      this.confusedChance = 14000;
      this.confusedWanderChance = 1500;
      this.confusedBreakAwayChance = 1000;
      this.confusedWanderDistance = 6;
      this.allowPanic = true;
      this.panicChance = 100;
      this.panicBreakAwayChance = 500;
      this.panicDistance = 6;
      this.allowSleep = true;
      this.canSleepOutside = true;
      this.sleepDelay = 50;
      this.sleepOutsideEnergyLevel = 5;
      this.sleepOutsideChance = 100;
      this.sleepOutsideBreakawayLevel = 40;
      this.sleepOutsideBreakawayChance = 10;
      this.energyDecayRate = 1500;
      this.allowIncapacitated = true;
      this.incapacitatedBreakawayChance = 10000;
      this.allowHunger = true;
      this.hungerFindFoodChance = 30;
      this.hungerStarvationFoodSearchLevel = 10;
      this.hungerHungryFoodSearchLevel = 20;
      this.hungerSnackSearchLevel = 50;
      this.hungerHungryFoodSearchDistance = 16;
      this.hungerHarvestTime = 1000;
      this.hungerEatTime = 60;
      this.hungerInHomeEatLevel = 50;
      this.hungerEatBreakawayLevel = 75;
      this.hungerEatBreakawayChance = 300;
      this.hungerDecayRate = 2000;
      this.hungerStarvationRate = 500;
      this.allowThirst = true;
      this.thirstFindWaterChance = 30;
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
      this.thirstDecayRate = 2500;
      this.thirstDehydrationRate = 500;
      this.allowCombat = true;
      this.allowCombatMelee = true;
      this.combatMeleeLineOfSight = CollisionModule.CollisionHeight.HIGH;
      this.combatDamageType = MobBase.DamageType.CRUSHING;
      this.combatSearchDistance = 6;
      this.combatAggroChance = 250;
      this.combatFleeSearchDistance = 32;
      this.combatFleeRandomDistance = 24;
      this.combatAttackBreakawayChance = 300;
      this.combatFleeBreakawayChance = 300;
      this.combatCanAttackMobs = true;
      this.combatCanAttackBuildings = true;
      this.combatMeleeAttackBuildingRepositionChance = 10;
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
      this.blightSpawnType = MobBase.MobType.ZOMBIE_CATJEET;
      this.blightAmountMaximum = 30000;
      this.allowNomadBehavior = true;
      this.nomadBehaviorChance = 500;
      this.nomadBehaviorMobType = MobBase.MobType.CATJEET_VILLAGER;
      this.nomadBehaviorPackDistance = 24;
      this.allowGodExperience = true;
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
