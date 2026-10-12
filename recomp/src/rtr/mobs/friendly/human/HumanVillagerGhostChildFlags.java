package rtr.mobs.friendly.human;

import org.newdawn.slick.Color;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.map.MapData;
import rtr.mobs.MobBase;
import rtr.mobs.MobFlagsBase;

public class HumanVillagerGhostChildFlags extends MobFlagsBase {
   public HumanVillagerGhostChildFlags() {
      this.colorLayersArrays[0] = new Color[]{new Color(-14487826), new Color(-14492195), new Color(-14496564), new Color(-14500933), new Color(-14505302)};
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
      this.colorLayersArrays[2] = new Color[]{new Color(-9539112)};
      this.colorLayersArrays[3] = new Color[]{new Color(-13684387)};
      this.mobSoundSets.put(MobBase.MobGender.NONE, SoundModule.SoundSet.GHOST);
      this.idleSoundRate = 2000;
      this.blockMapGroup = MapData.BlockMapGroup.GHOSTS;
      this.aggressiveness = 0;
      this.levelCapLow = 1;
      this.levelCapHigh = 2;
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
      this.ghostBaseType = MobBase.MobType.HUMAN_VILLAGER_CHILD;
      this.ghostNegativeBaseType = MobBase.MobType.ZOMBIE_CHILD;
      this.ghostBehaviorChance = 100;
      this.ghostFadeInChance = 5000;
      this.ghostFadeOutChance = 50;
      this.ghostFullMoonFadeChance = 50;
      this.ghostEclipseFadeChance = 10;
      this.ghostBloodMoonFadeChance = 10;
      this.ghostLeaveChance = 5000;
      this.ignoreThreatenedTiles = true;
      this.invulnerable = true;
      this.maximumAlpha = 0.4F;
      this.configPerkAdjustments();
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.GHOST_APPEARANCE_CHANCE);
   }
}
