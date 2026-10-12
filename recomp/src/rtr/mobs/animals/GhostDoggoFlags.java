package rtr.mobs.animals;

import org.newdawn.slick.Color;
import rtr.SoundModule;
import rtr.map.MapData;
import rtr.mobs.MobBase;
import rtr.mobs.MobFlagsBase;
import rtr.utilities.Utilities;

public class GhostDoggoFlags extends MobFlagsBase {
   public GhostDoggoFlags() {
      this.colorLayersArrays[0] = new Color[]{new Color(-14487826), new Color(-14492195), new Color(-14496564), new Color(-14500933), new Color(-14505302)};
      this.colorLayersArrays[1] = Utilities.returnColorArrayBrighter(this.colorLayersArrays[0], 0.5F);
      this.colorLayersArrays[2] = Utilities.returnColorArrayBrighter(this.colorLayersArrays[0], 0.2F);
      this.colorLayersArrays[3] = Utilities.returnColorArrayBrighter(this.colorLayersArrays[0], 0.2F);
      this.colorLayersArrays[4] = Utilities.returnColorArrayDarker(this.colorLayersArrays[0], 0.2F);
      this.colorLayersArrays[5] = new Color[]{new Color(-15658735)};
      this.mobSoundSets.put(MobBase.MobGender.NONE, SoundModule.SoundSet.GHOST_DOGGO);
      this.idleSoundRate = 4000;
      this.blockMapGroup = MapData.BlockMapGroup.GHOSTS;
      this.aggressiveness = 0;
      this.levelCapLow = 1;
      this.levelCapHigh = 5;
      this.statVelocityBase = 0.2F;
      this.statVelocityMultiplier = 2.0E-4F;
      this.experienceMultiplier = 150;
      this.statsPerLevel = 25;
      this.statStrengthChance = 10;
      this.statDexterityChance = 10;
      this.allowWander = true;
      this.wanderChance = 300;
      this.wanderPreferVillage = true;
      this.canWanderAimlessly = true;
      this.wanderAimlesslyWeight = 50;
      this.wanderAimlesslyDistance = 8;
      this.canWanderGhost = true;
      this.wanderGhostWeight = 10;
      this.wanderGhostDistance = 6;
      this.allowGhostBehavior = true;
      this.ghostBaseType = MobBase.MobType.DOGGO;
      this.ghostNegativeBaseType = MobBase.MobType.ZOMBIE_DOGGO;
      this.ghostBehaviorChance = 100;
      this.ghostFadeInChance = 250;
      this.ghostFadeOutChance = 50;
      this.ghostFullMoonFadeChance = 50;
      this.ghostEclipseFadeChance = 10;
      this.ghostBloodMoonFadeChance = 10;
      this.ghostLeaveChance = 15000;
      this.ignoreThreatenedTiles = true;
      this.invulnerable = true;
      this.maximumAlpha = 0.4F;
      this.configPerkAdjustments();
   }
}
