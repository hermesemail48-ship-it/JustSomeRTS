package rtr.mobs.golems;

import org.newdawn.slick.Color;
import rtr.SoundModule;
import rtr.TimeModule;
import rtr.map.MapData;
import rtr.mobs.MobBase;
import rtr.mobs.MobFlagsBase;
import rtr.particles.ParticleModule;

public class CourierGolemFlags extends MobFlagsBase {
   public CourierGolemFlags() {
      this.colorLayersArrays[0] = new Color[]{
         new Color(-16716050), new Color(-16711681), new Color(-1179410), new Color(-65281), new Color(-1118720), new Color(-256)
      };
      this.colorLayersArrays[1] = new Color[]{new Color(-3355444), new Color(-2236963), new Color(-1118482)};
      this.colorLayersArrays[2] = new Color[]{new Color(-2236963), new Color(-1118482), new Color(-1)};
      this.colorLayersArrays[3] = new Color[]{new Color(-7012352), new Color(-5439488), new Color(-3735552)};
      this.colorLayersArrays[4] = new Color[]{new Color(-16755371)};
      this.mobSoundSets.put(MobBase.MobGender.NONE, SoundModule.SoundSet.GOLEM);
      this.idleSoundRate = 2000;
      this.blockMapGroup = MapData.BlockMapGroup.STANDARD_NO_CORRUPTION;
      this.aggressiveness = 3000;
      this.levelCapLow = 1;
      this.levelCapHigh = 2;
      this.statVelocityBase = 0.4F;
      this.statVelocityMultiplier = 6.0E-4F;
      this.experienceMultiplier = 200;
      this.statsPerLevel = 35;
      this.statStrengthChance = 10;
      this.statDexterityChance = 20;
      this.carryMaxBonusMultiplier = 2.5;
      this.allowWander = true;
      this.wanderChance = 10000;
      this.canWanderAimlessly = true;
      this.wanderAimlesslyWeight = 10;
      this.wanderAimlesslyDistance = 4;
      this.canWanderSpawn = true;
      this.wanderSpawnWeight = 50;
      this.wanderSpawnDistance = 6;
      this.allowCourierBehavior = true;
      this.courierBehaviorChance = 500;
      this.courierDepartRegionChance = 500;
      this.courierDepartRegionTimeOfDay = TimeModule.TimeOfDay.MIDDAY;
      this.courierIdleWithResourcesDelay = 6000;
      this.allowAging = true;
      this.agingRate = this.time.getFullDayLength();
      this.allowSpawnSearch = true;
      this.spawnSearchChance = 2000;
      this.spawnSearchDistance = -1;
      this.spawnSearchDieIfFail = true;
      this.allowGodExperience = true;
      this.ignoreThreatenedTiles = true;
      this.maximumAlpha = 1.0F;
      this.bloodType = ParticleModule.ParticleSet.DARK_GRAY;
      this.configPerkAdjustments();
   }
}
