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

public class IceRoggoGolemFlags extends MobFlagsBase {
   public IceRoggoGolemFlags() {
      this.colorLayersArrays[0] = new Color[]{
         new Color(-3355444), new Color(-2236963), new Color(-1118482), new Color(-1), new Color(-10066330), new Color(-8947849), new Color(-7829368)
      };
      this.colorLayersArrays[1] = Utilities.returnColorArrayDarker(this.colorLayersArrays[0], 0.3F);
      this.colorLayersArrays[2] = this.colorLayersArrays[0];
      this.colorLayersArrays[3] = this.colorLayersArrays[0];
      this.colorLayersArrays[4] = Utilities.returnColorArrayDarker(this.colorLayersArrays[0], 0.4F);
      this.colorLayersArrays[5] = new Color[]{new Color(-16733526)};
      this.mobSoundSets.put(MobBase.MobGender.NONE, SoundModule.SoundSet.GOLEM);
      this.idleSoundRate = 2000;
      this.blockMapGroup = MapData.BlockMapGroup.STANDARD_NO_CORRUPTION;
      this.aggressiveness = 3000;
      this.levelCapLow = 2;
      this.levelCapHigh = 3;
      this.statVelocityBase = 0.4F;
      this.statVelocityMultiplier = 6.0E-4F;
      this.experienceMultiplier = 200;
      this.statsPerLevel = 35;
      this.statStrengthChance = 5;
      this.statDexterityChance = 20;
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
      this.combatDamageType = MobBase.DamageType.ICE;
      this.allowCombatRanged = true;
      this.combatRangedMissileType = MissileModule.MissileType.ICE_GOLEM;
      this.combatRangedRange = 8;
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
