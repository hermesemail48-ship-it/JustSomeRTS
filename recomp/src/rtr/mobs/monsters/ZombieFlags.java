package rtr.mobs.monsters;

import org.newdawn.slick.Color;
import rtr.SoundModule;
import rtr.map.CollisionModule;
import rtr.map.MapData;
import rtr.mobs.MobBase;
import rtr.mobs.MobFlagsBase;
import rtr.particles.ParticleModule;

public class ZombieFlags extends MobFlagsBase {
   public ZombieFlags() {
      this.colorLayersArrays[0] = new Color[]{new Color(-16736256), new Color(-16749568), new Color(-16760576)};
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
      this.colorLayersArrays[2] = new Color[]{new Color(-7993340)};
      this.colorLayersArrays[3] = new Color[]{new Color(-9015296)};
      this.colorLayersArrays[4] = new Color[]{new Color(-16777216)};
      this.mobSoundSets.put(MobBase.MobGender.NONE, SoundModule.SoundSet.ZOMBIE);
      this.idleSoundRate = 2000;
      this.blockMapGroup = MapData.BlockMapGroup.MONSTERS;
      this.aggressiveness = 1000;
      this.levelCapLow = 1;
      this.levelCapHigh = 30;
      this.statVelocityBase = 0.4F;
      this.statVelocityMultiplier = 6.0E-4F;
      this.experienceMultiplier = 50;
      this.statsPerLevel = 25;
      this.statStrengthChance = 15;
      this.statDexterityChance = 10;
      this.allowInventory = true;
      this.allowInventoryRandomSpawnItems = true;
      this.allowInventoryEquipRestrictionOverride = true;
      this.inventoryRandomSpawnItemsChance = 250;
      this.inventorySearchChance = 250;
      this.inventoryNearbyResourcesOnGroundDistance = 8;
      this.canInventoryEquipHead = true;
      this.canInventoryEquipChest = true;
      this.canInventoryEquipTool = true;
      this.canInventoryEquipMeleeWeapon = true;
      this.canInventoryEquipShield = true;
      this.canInventoryEquipRecovery = true;
      this.canInventoryEquipWater = true;
      this.canInventoryEquipFood = true;
      this.canMonsterSpawn = true;
      this.monsterSpawnDayDefaultMinimum = 3;
      this.monsterSpawnDayDefaultMaximum = -1;
      this.allowAging = true;
      this.agingRate = this.time.getFullDayLength();
      this.allowThermal = true;
      this.canThermalFreeze = true;
      this.canThermalFreezeCritical = false;
      this.canThermalOverheat = true;
      this.canThermalOverheatCritical = false;
      this.thermalBaseline = 50;
      this.thermalFreeze = 20;
      this.thermalFreezeCritical = 0;
      this.thermalOverheat = 80;
      this.thermalOverheatCritical = 100;
      this.thermalIncreaseTempChance = 400;
      this.thermalAdjustRate = 2000;
      this.allowWander = true;
      this.wanderChance = 300;
      this.wanderPreferCorruption = true;
      this.canWanderAimlessly = true;
      this.wanderAimlesslyWeight = 10;
      this.wanderAimlesslyDistance = 8;
      this.canWanderObjects = true;
      this.wanderObjectWeight = 150;
      this.allowCombat = true;
      this.allowCombatMelee = true;
      this.combatMeleeLineOfSight = CollisionModule.CollisionHeight.HIGH;
      this.combatDamageType = MobBase.DamageType.BLIGHT;
      this.combatSearchDistance = 6;
      this.combatAggroChance = 250;
      this.combatFleeSearchDistance = 32;
      this.combatFleeRandomDistance = 24;
      this.combatAttackBreakawayChance = 1500;
      this.combatFleeBreakawayChance = 300;
      this.combatCanAttackMobs = true;
      this.combatCanAttackBuildings = true;
      this.combatMeleeAttackBuildingRepositionChance = 4;
      this.damageResistances.put(MobBase.DamageType.BLIGHT, 80);
      this.damageResistances.put(MobBase.DamageType.POISON, 80);
      this.damageVulnerabilities.put(MobBase.DamageType.FIRE, 50);
      this.damageVulnerabilities.put(MobBase.DamageType.MAGIC_FIRE, 50);
      this.allowOnFire = true;
      this.onFireAmountMaximum = 5000;
      this.allowFrozen = true;
      this.frozenAmountMaximum = 5000;
      this.allowHostileBehavior = true;
      this.hostileBehaviorAttackChance = 100;
      this.hostileBehaviorBreakawayChance = 50;
      this.golemRequiresChargeAmountMax = 2500;
      this.hostileBehaviorLeaveChance = 1000;
      this.allowHostileBehaviorPack = true;
      this.hostileBehaviorPackChance = 15;
      this.hostileBehaviorPackDistance = 6;
      this.hostileBehaviorPackMaximum = 12;
      this.threatenTiles = true;
      this.allowForcedPathfinding = true;
      this.allowBoostedLevel = true;
      this.maximumAlpha = 1.0F;
      this.bloodType = ParticleModule.ParticleSet.BLOOD_RED;
      this.configPerkAdjustments();
   }
}
