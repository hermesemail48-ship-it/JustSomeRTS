package rtr.mobs.monsters;

import org.newdawn.slick.Color;
import rtr.SoundModule;
import rtr.map.CollisionModule;
import rtr.map.MapData;
import rtr.mobs.MobBase;
import rtr.mobs.MobFlagsBase;
import rtr.particles.ParticleModule;

public class SmallSlimeFlags extends MobFlagsBase {
   public SmallSlimeFlags() {
      this.colorLayersArrays[0] = new Color[]{
         new Color(-16738048),
         new Color(-16724992),
         new Color(-16711936),
         new Color(-12281600),
         new Color(-10040320),
         new Color(-7799040),
         new Color(-16737980),
         new Color(-16724890),
         new Color(-16711800)
      };
      this.mobSoundSets.put(MobBase.MobGender.NONE, SoundModule.SoundSet.SLIME);
      this.idleSoundRate = 2000;
      this.blockMapGroup = MapData.BlockMapGroup.MONSTERS;
      this.aggressiveness = 100;
      this.levelCapLow = 1;
      this.levelCapHigh = 12;
      this.statVelocityBase = 0.2F;
      this.statVelocityMultiplier = 2.0E-5F;
      this.experienceMultiplier = 50;
      this.statsPerLevel = 15;
      this.statStrengthChance = 10;
      this.statDexterityChance = 10;
      this.allowInventory = true;
      this.allowInventoryRandomSpawnItems = true;
      this.allowInventoryEquipRestrictionOverride = true;
      this.inventoryRandomSpawnItemsChance = 300;
      this.inventorySearchChance = 250;
      this.inventoryNearbyResourcesOnGroundDistance = 8;
      this.canInventoryEquipRecovery = true;
      this.canInventoryEquipWater = true;
      this.canInventoryEquipFood = true;
      this.canMonsterSpawn = true;
      this.monsterSpawnDayDefaultMinimum = 2;
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
      this.thermalFreezeCritical = 5;
      this.thermalOverheat = 80;
      this.thermalOverheatCritical = 95;
      this.thermalIncreaseTempChance = 400;
      this.thermalAdjustRate = 2000;
      this.allowHealthRegen = true;
      this.healthRegenRate = 600;
      this.allowWander = true;
      this.wanderChance = 750;
      this.wanderPreferCorruption = true;
      this.canWanderAimlessly = true;
      this.wanderAimlesslyWeight = 10;
      this.wanderAimlesslyDistance = 8;
      this.canWanderObjects = true;
      this.wanderObjectWeight = 150;
      this.allowCombat = true;
      this.allowCombatMelee = true;
      this.combatMeleeLineOfSight = CollisionModule.CollisionHeight.HIGH;
      this.combatDamageType = MobBase.DamageType.POISON;
      this.combatSearchDistance = 4;
      this.combatAggroChance = 300;
      this.combatFleeSearchDistance = 32;
      this.combatFleeRandomDistance = 24;
      this.combatAttackBreakawayChance = 750;
      this.combatFleeBreakawayChance = 300;
      this.combatCanAttackMobs = true;
      this.combatCanAttackBuildings = true;
      this.combatMeleeAttackBuildingRepositionChance = 10;
      this.damageResistances.put(MobBase.DamageType.BLIGHT, 80);
      this.damageResistances.put(MobBase.DamageType.PIERCING, 50);
      this.damageResistances.put(MobBase.DamageType.SLASHING, 50);
      this.damageResistances.put(MobBase.DamageType.POISON, 80);
      this.damageVulnerabilities.put(MobBase.DamageType.ELECTRIC, 50);
      this.damageVulnerabilities.put(MobBase.DamageType.MAGIC_ELECTRIC, 80);
      this.damageVulnerabilities.put(MobBase.DamageType.ICE, 30);
      this.damageVulnerabilities.put(MobBase.DamageType.MAGIC_ICE, 30);
      this.damageVulnerabilities.put(MobBase.DamageType.FIRE, 30);
      this.damageVulnerabilities.put(MobBase.DamageType.MAGIC_FIRE, 30);
      this.damageVulnerabilities.put(MobBase.DamageType.MAGIC_REGULAR, 50);
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
      this.maximumAlpha = 0.9F;
      this.bloodType = ParticleModule.ParticleSet.BLOOD_GREEN;
      this.movementTrail = true;
      this.movementTrailChance = 25;
      this.configPerkAdjustments();
   }
}
