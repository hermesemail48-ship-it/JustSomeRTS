package rtr.mobs.monsters;

import org.newdawn.slick.Color;
import rtr.SoundModule;
import rtr.map.CollisionModule;
import rtr.map.MapData;
import rtr.mobs.MobBase;
import rtr.mobs.MobFlagsBase;
import rtr.particles.ParticleModule;

public class DroneFlags extends MobFlagsBase {
   public DroneFlags() {
      this.colorLayersArrays[0] = new Color[]{new Color(-6250336), new Color(-9671572), new Color(-12500671)};
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
      this.colorLayersArrays[2] = new Color[]{new Color(-9015296)};
      this.colorLayersArrays[3] = this.colorLayersArrays[0];
      this.colorLayersArrays[4] = new Color[]{new Color(-16777216)};
      this.mobSoundSets.put(MobBase.MobGender.NONE, SoundModule.SoundSet.GENERIC);
      this.idleSoundRate = 2000;
      this.blockMapGroup = MapData.BlockMapGroup.MONSTERS;
      this.aggressiveness = 50;
      this.levelCapLow = 1;
      this.levelCapHigh = 5;
      this.statVelocityBase = 0.45F;
      this.statVelocityMultiplier = 0.0015F;
      this.experienceMultiplier = 50;
      this.statsPerLevel = 15;
      this.statStrengthChance = 10;
      this.statDexterityChance = 20;
      this.allowInventory = true;
      this.allowInventoryRandomSpawnItems = true;
      this.allowInventoryEquipRestrictionOverride = true;
      this.inventoryRandomSpawnItemsChance = 150;
      this.inventorySearchChance = 250;
      this.inventoryNearbyResourcesOnGroundDistance = 8;
      this.canInventoryEquipChest = true;
      this.canInventoryEquipTool = true;
      this.canInventoryEquipMeleeWeapon = true;
      this.canInventoryEquipShield = true;
      this.canInventoryEquipRecovery = true;
      this.canInventoryEquipWater = true;
      this.canInventoryEquipFood = true;
      this.monsterSpawnDayDefaultMinimum = -1;
      this.monsterSpawnDayDefaultMaximum = -1;
      this.allowAging = true;
      this.agingRate = this.time.getFullDayLength();
      this.allowHealthRegen = true;
      this.healthRegenRate = 2000;
      this.allowWander = true;
      this.wanderChance = 400;
      this.wanderPreferCorruption = true;
      this.canWanderAimlessly = true;
      this.wanderAimlesslyWeight = 10;
      this.wanderAimlesslyDistance = 16;
      this.canWanderObjects = true;
      this.wanderObjectWeight = 150;
      this.allowCorruptionWork = true;
      this.corruptionWorkChance = 200;
      this.corruptionWorkTime = 500;
      this.corruptionWorkHarvestDistance = 16;
      this.corruptionWorkNearbyResourcesOnGroundDistance = 16;
      this.corruptionWorkFindExtraResourcesDistance = 6;
      this.corruptionWorkNewBuildingDistance = 8;
      this.allowCombat = true;
      this.allowCombatMelee = true;
      this.combatMeleeLineOfSight = CollisionModule.CollisionHeight.HIGH;
      this.combatDamageType = MobBase.DamageType.CRUSHING;
      this.combatSearchDistance = 6;
      this.combatAggroChance = 300;
      this.combatFleeSearchDistance = 32;
      this.combatFleeRandomDistance = 24;
      this.combatAttackBreakawayChance = 1500;
      this.combatFleeBreakawayChance = 300;
      this.combatCanAttackMobs = true;
      this.combatCanAttackBuildings = true;
      this.combatMeleeAttackBuildingRepositionChance = 4;
      this.damageVulnerabilities.put(MobBase.DamageType.FIRE, 20);
      this.damageVulnerabilities.put(MobBase.DamageType.MAGIC_FIRE, 20);
      this.allowOnFire = true;
      this.onFireAmountMaximum = 5000;
      this.allowFrozen = true;
      this.frozenAmountMaximum = 5000;
      this.allowPoisoned = true;
      this.poisonedAmountMaximum = 15000;
      this.threatenTiles = true;
      this.allowForcedPathfinding = true;
      this.diesSlowlyOffCorruption = true;
      this.diesSlowlyPercentage = 5;
      this.diesSlowlyRate = 500;
      this.maximumAlpha = 1.0F;
      this.bloodType = ParticleModule.ParticleSet.MAGIC_YELLOW;
      this.configPerkAdjustments();
   }
}
