package rtr.mobs.golems;

import org.newdawn.slick.Color;
import rtr.SoundModule;
import rtr.map.MapData;
import rtr.mobs.MobBase;
import rtr.mobs.MobFlagsBase;
import rtr.particles.ParticleModule;

public class CubeEGolemFlags extends MobFlagsBase {
   public CubeEGolemFlags() {
      this.colorLayersArrays[0] = new Color[]{new Color(-10197906)};
      this.colorLayersArrays[1] = new Color[]{new Color(-15460584)};
      this.colorLayersArrays[2] = new Color[]{new Color(-997326)};
      this.colorLayersArrays[3] = new Color[]{new Color(-2268050)};
      this.colorLayersArrays[4] = new Color[]{new Color(-15593183)};
      this.mobSoundSets.put(MobBase.MobGender.NONE, SoundModule.SoundSet.GOLEM);
      this.idleSoundRate = 2000;
      this.blockMapGroup = MapData.BlockMapGroup.STANDARD_NO_CORRUPTION;
      this.aggressiveness = 0;
      this.levelCapLow = 1;
      this.levelCapHigh = 2;
      this.statVelocityBase = 0.4F;
      this.statVelocityMultiplier = 6.0E-4F;
      this.experienceMultiplier = 200;
      this.statsPerLevel = 35;
      this.statStrengthChance = 10;
      this.statDexterityChance = 20;
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
      this.allowSpawnSearch = true;
      this.spawnSearchChance = 1000;
      this.spawnSearchDistance = 16;
      this.allowSpawnRepair = true;
      this.spawnRepairChance = 1000;
      this.spawnRepairTime = 200;
      this.spawnSearchDieIfFail = true;
      this.allowGodExperience = true;
      this.ignoreThreatenedTiles = true;
      this.maximumAlpha = 1.0F;
      this.bloodType = ParticleModule.ParticleSet.BLACK;
      this.configPerkAdjustments();
   }
}
