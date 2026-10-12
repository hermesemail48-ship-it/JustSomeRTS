package rtr.mobs.golems;

import org.newdawn.slick.Color;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.map.MapData;
import rtr.mobs.MobBase;
import rtr.mobs.MobFlagsBase;
import rtr.particles.ParticleModule;

public class HolyGolemFlags extends MobFlagsBase {
   public HolyGolemFlags() {
      this.colorLayersArrays[0] = new Color[]{new Color(-1)};
      this.colorLayersArrays[1] = new Color[]{new Color(-1)};
      this.colorLayersArrays[2] = new Color[]{new Color(-8947849)};
      this.colorLayersArrays[3] = new Color[]{new Color(-16777216)};
      this.mobSoundSets.put(MobBase.MobGender.NONE, SoundModule.SoundSet.GOLEM);
      this.idleSoundRate = 2000;
      this.blockMapGroup = MapData.BlockMapGroup.STANDARD_NO_CORRUPTION;
      this.aggressiveness = 3000;
      this.levelCapLow = 6;
      this.levelCapHigh = 8;
      this.influenceMaintainCost = 500;
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
      this.allowCombat = true;
      this.combatDamageType = MobBase.DamageType.MAGIC_REGULAR;
      this.combatSearchDistance = 16;
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
      this.ignoreThreatenedTiles = true;
      this.lightRadius = 4;
      this.lightIntensity = 12;
      this.maximumAlpha = 0.6F;
      this.bloodType = ParticleModule.ParticleSet.MAGIC_BLUE;
      this.configPerkAdjustments();
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.HOLY_GOLEM_SPELL_COST);
   }
}
