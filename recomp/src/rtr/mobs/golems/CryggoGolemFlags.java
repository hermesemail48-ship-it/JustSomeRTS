package rtr.mobs.golems;

import org.newdawn.slick.Color;
import rtr.SoundModule;
import rtr.map.CollisionModule;
import rtr.map.MapData;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.mobs.MobFlagsBase;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class CryggoGolemFlags extends MobFlagsBase {
   public CryggoGolemFlags() {
      this.colorLayersArrays[0] = new Color[]{
         new Color(-211885),
         new Color(-146317),
         new Color(-6728647),
         new Color(-10279908),
         new Color(-197548),
         new Color(-6234084),
         new Color(-12806088),
         new Color(-13868004),
         new Color(-5442596),
         new Color(-15287622),
         new Color(-13017447),
         new Color(-14924957),
         new Color(-329473),
         new Color(-5929473),
         new Color(-9023567),
         new Color(-11651719)
      };
      this.colorLayersArrays[1] = Utilities.returnColorArrayBrighter(this.colorLayersArrays[0], 0.7F);
      this.colorLayersArrays[2] = Utilities.returnColorArrayBrighter(this.colorLayersArrays[0], 0.4F);
      this.colorLayersArrays[3] = Utilities.returnColorArrayBrighter(this.colorLayersArrays[0], 0.4F);
      this.colorLayersArrays[4] = this.colorLayersArrays[0];
      this.colorLayersArrays[5] = new Color[]{new Color(-16777216)};
      this.mobSoundSets.put(MobBase.MobGender.NONE, SoundModule.SoundSet.GOLEM);
      this.idleSoundRate = 2000;
      this.blockMapGroup = MapData.BlockMapGroup.STANDARD_NO_CORRUPTION;
      this.aggressiveness = 3000;
      this.levelCapLow = 1;
      this.levelCapHigh = 2;
      this.statVelocityBase = 0.7F;
      this.statVelocityMultiplier = 6.0E-4F;
      this.experienceMultiplier = 200;
      this.statsPerLevel = 35;
      this.statStrengthChance = 20;
      this.statDexterityChance = 5;
      this.allowAging = true;
      this.agingRate = this.time.getFullDayLength();
      this.allowWander = true;
      this.wanderChance = 1000;
      this.canWanderAimlessly = true;
      this.wanderAimlesslyWeight = 10;
      this.wanderAimlesslyDistance = 8;
      this.canWanderSpawn = true;
      this.wanderSpawnWeight = 50;
      this.wanderSpawnDistance = 4;
      this.allowCombat = true;
      this.combatDamageType = MobBase.DamageType.MAGIC_REGULAR;
      this.allowCombatRanged = true;
      this.combatRangedMissileType = MissileModule.MissileType.CRYSTAL_GOLEM;
      this.combatRangedRange = 12;
      this.combatRangedLineOfSight = CollisionModule.CollisionHeight.HIGH;
      this.combatSearchDistance = 12;
      this.combatAggroChance = 250;
      this.combatFleeSearchDistance = 32;
      this.combatFleeRandomDistance = 24;
      this.combatAttackBreakawayChance = 1500;
      this.combatFleeBreakawayChance = 300;
      this.combatCanAttackMobs = true;
      this.combatCanAttackBuildings = true;
      this.combatMeleeAttackBuildingRepositionChance = 10;
      this.allowGolemBehavior = true;
      this.golemBehaviorChance = 100;
      this.golemRequiresCharge = true;
      this.golemRequiresChargeAmountMax = 10000;
      this.allowSpawnSearch = true;
      this.spawnSearchChance = 1000;
      this.spawnSearchDistance = 16;
      this.allowSpawnRepair = true;
      this.spawnRepairChance = 1000;
      this.spawnRepairTime = 200;
      this.spawnSearchDieIfFail = true;
      this.ignoreThreatenedTiles = true;
      this.lightRadius = 4;
      this.lightIntensity = 4;
      this.maximumAlpha = 1.0F;
      this.bloodType = ParticleModule.ParticleSet.DARK_GRAY;
      this.configPerkAdjustments();
   }
}
