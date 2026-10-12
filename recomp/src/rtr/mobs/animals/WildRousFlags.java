package rtr.mobs.animals;

import org.newdawn.slick.Color;
import rtr.SoundModule;
import rtr.map.CollisionModule;
import rtr.map.MapData;
import rtr.mobs.MobBase;
import rtr.mobs.MobFlagsBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Utilities;

public class WildRousFlags extends MobFlagsBase {
   public WildRousFlags() {
      this.colorLayersArrays[0] = new Color[]{
         new Color(-11708573),
         new Color(-131589),
         new Color(-10469325),
         new Color(-2171948),
         new Color(-1658250),
         new Color(-11518406),
         new Color(-12369344),
         new Color(-4023690),
         new Color(-7314369),
         new Color(-1450557)
      };
      this.colorLayersArrays[1] = Utilities.returnColorArrayBrighter(this.colorLayersArrays[0], 0.5F);
      this.colorLayersArrays[2] = new Color[]{new Color(-2048870), new Color(-5280427), new Color(-2772595), new Color(-2638941), new Color(-5865634)};
      this.colorLayersArrays[3] = Utilities.returnColorArrayBrighter(this.colorLayersArrays[0], 0.2F);
      this.colorLayersArrays[4] = new Color[]{new Color(-15658735)};
      this.mobSoundSets.put(MobBase.MobGender.NONE, SoundModule.SoundSet.ROUS);
      this.idleSoundRate = 4000;
      this.blockMapGroup = MapData.BlockMapGroup.STANDARD_NO_CORRUPTION;
      this.aggressiveness = 0;
      this.levelCapLow = 2;
      this.levelCapHigh = 4;
      this.statVelocityBase = 0.9F;
      this.statVelocityMultiplier = 0.0015F;
      this.experienceMultiplier = 100;
      this.statsPerLevel = 35;
      this.statStrengthChance = 3;
      this.statDexterityChance = 30;
      this.allowInventory = true;
      this.inventorySearchChance = 100;
      this.inventoryNearbyResourcesOnGroundDistance = 8;
      this.canInventoryEquipWater = true;
      this.canInventoryEquipFood = true;
      this.dropsList.put(ResourceModule.ResourceType.RAW_MEAT, 1);
      this.dropsChance = 2;
      this.dropsAttempts = 16;
      this.canAnimalSpawn = true;
      this.animalSpawnDayDefaultMinimum = -1;
      this.animalSpawnDayDefaultMaximum = -1;
      this.animalSpawnAmountMinimum = 1;
      this.animalSpawnAmountMaximum = 2;
      this.animalSpawnAmountWeight = 40;
      this.allowAging = true;
      this.agingStartAgeMinimum = 1;
      this.agingStartAgeMaximum = 2;
      this.agingMaximumAge = 20;
      this.agingRate = this.time.getFullDayLength();
      this.agingConstitutionDecayRate = 50000;
      this.allowHealthRegen = true;
      this.healthRegenRate = 1200;
      this.allowThermal = true;
      this.canThermalFreeze = true;
      this.canThermalOverheat = true;
      this.thermalBaseline = 50;
      this.thermalFreeze = 20;
      this.thermalOverheat = 80;
      this.thermalIncreaseTempChance = 400;
      this.thermalAdjustRate = 2000;
      this.allowWander = true;
      this.wanderChance = 200;
      this.canWanderAimlessly = true;
      this.wanderAimlesslyWeight = 50;
      this.wanderAimlesslyDistance = 12;
      this.allowConfused = true;
      this.confusedChance = 14000;
      this.confusedWanderChance = 500;
      this.confusedBreakAwayChance = 1000;
      this.confusedWanderDistance = 4;
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
      this.hungerStarvationFoodSearchLevel = 30;
      this.hungerHungryFoodSearchLevel = 40;
      this.hungerSnackSearchLevel = 50;
      this.hungerHungryFoodSearchDistance = 24;
      this.hungerHarvestTime = 1000;
      this.hungerEatTime = 60;
      this.hungerInHomeEatLevel = 50;
      this.hungerEatBreakawayLevel = 75;
      this.hungerEatBreakawayChance = 300;
      this.hungerDecayRate = 3000;
      this.hungerStarvationRate = 500;
      this.allowThirst = true;
      this.thirstFindWaterChance = 30;
      this.thirstDehydrationWaterSearchLevel = 30;
      this.thirstThirstyWaterSearchLevel = 40;
      this.thirstSnackSearchLevel = 50;
      this.thirstOverheatSearchLevel = 90;
      this.thirstThirstyWaterSearchDistance = 16;
      this.thirstHarvestTime = 200;
      this.thirstDrinkTime = 30;
      this.thirstInHomeDrinkLevel = 50;
      this.thirstDrinkBreakawayLevel = 75;
      this.thirstDrinkBreakawayChance = 300;
      this.thirstDecayRate = 3500;
      this.thirstDehydrationRate = 500;
      this.allowSocial = true;
      this.socialChatChance = 300;
      this.socialChatBreakawayChance = 500;
      this.socialChatSearchRadius = 8;
      this.socialChatInterruptChance = 50;
      this.socialRate = 500;
      this.socialSadnessLevel = 10;
      this.socialLonelyLevel = 40;
      this.socialBoredLevel = 70;
      this.allowCombat = true;
      this.allowCombatMelee = true;
      this.combatMeleeLineOfSight = CollisionModule.CollisionHeight.HIGH;
      this.combatDamageType = MobBase.DamageType.CRUSHING;
      this.combatSearchDistance = 10;
      this.combatAggroChance = 50;
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
      this.allowBlight = true;
      this.blightSpawnType = MobBase.MobType.ZOMBIE_ROUS;
      this.blightAmountMaximum = 30000;
      this.allowAnimalWanderBehavior = true;
      this.animalWanderBehaviorChance = 400;
      this.animalWanderBehaviorPreferGroups = true;
      this.animalWanderBehaviorPreferGroupsMaxiumumSize = 8;
      this.animalWanderBehaviorPreferredResources.add(ResourceModule.ResourceType.WOOD);
      this.ignoreThreatenedTiles = true;
      this.domesticateType = MobBase.MobType.ROUS;
      this.maximumAlpha = 1.0F;
      this.configPerkAdjustments();
   }
}
