package rtr.mobs.golems;

import org.newdawn.slick.Color;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.map.MapData;
import rtr.mobs.MobBase;
import rtr.mobs.MobFlagsBase;
import rtr.particles.ParticleModule;

public class LaborGolemFlags extends MobFlagsBase {
   public LaborGolemFlags() {
      this.colorLayersArrays[0] = new Color[]{new Color(-12298923)};
      this.colorLayersArrays[1] = new Color[]{new Color(-13421773)};
      this.colorLayersArrays[2] = new Color[]{new Color(-16768427)};
      this.colorLayersArrays[3] = new Color[]{new Color(-16768478)};
      this.mobSoundSets.put(MobBase.MobGender.NONE, SoundModule.SoundSet.GOLEM);
      this.idleSoundRate = 2000;
      this.blockMapGroup = MapData.BlockMapGroup.STANDARD_NO_CORRUPTION;
      this.aggressiveness = 0;
      this.levelCapLow = 1;
      this.levelCapHigh = 2;
      this.influenceMaintainCost = 300;
      this.statVelocityBase = 0.4F;
      this.statVelocityMultiplier = 6.0E-4F;
      this.experienceMultiplier = 200;
      this.statsPerLevel = 35;
      this.statStrengthChance = 20;
      this.statDexterityChance = 10;
      this.allowAging = true;
      this.agingRate = this.time.getFullDayLength();
      this.allowWander = true;
      this.wanderChance = 500;
      this.canWanderAimlessly = true;
      this.wanderAimlesslyWeight = 10;
      this.wanderAimlesslyDistance = 8;
      this.canWanderSpawn = true;
      this.wanderSpawnWeight = 50;
      this.wanderSpawnDistance = 4;
      this.allowWork = true;
      this.workChance = 100;
      this.workEnergyCutoff = 10;
      this.workNearbyResourcesOnGroundDistance = 16;
      this.workFindExtraResourcesDistance = 3;
      this.allowCombat = true;
      this.combatDamageType = MobBase.DamageType.MAGIC_REGULAR;
      this.combatSearchDistance = 4;
      this.combatAggroChance = 5000;
      this.combatFleeSearchDistance = 32;
      this.combatFleeRandomDistance = 24;
      this.combatAttackBreakawayChance = 1500;
      this.combatFleeBreakawayChance = 300;
      this.combatCanAttackMobs = true;
      this.combatCanAttackBuildings = true;
      this.combatMeleeAttackBuildingRepositionChance = 10;
      this.allowGodExperience = true;
      this.ignoreThreatenedTiles = true;
      this.lightRadius = 4;
      this.lightIntensity = 12;
      this.maximumAlpha = 1.0F;
      this.bloodType = ParticleModule.ParticleSet.MAGIC_BLUE;
      this.configPerkAdjustments();
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.LABOR_GOLEM_SPELL_COST);
   }
}
