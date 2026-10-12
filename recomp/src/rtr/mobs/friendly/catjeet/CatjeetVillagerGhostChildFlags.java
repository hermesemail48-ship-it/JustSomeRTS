package rtr.mobs.friendly.catjeet;

import org.newdawn.slick.Color;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.map.MapData;
import rtr.mobs.MobBase;
import rtr.mobs.MobFlagsBase;
import rtr.utilities.Utilities;

public class CatjeetVillagerGhostChildFlags extends MobFlagsBase {
   public CatjeetVillagerGhostChildFlags() {
      this.colorLayersArrays[0] = new Color[]{new Color(-14487826), new Color(-14492195), new Color(-14496564), new Color(-14500933), new Color(-14505302)};
      this.colorLayersArrays[1] = this.colorLayersArrays[0];
      this.colorLayersArrays[2] = new Color[]{new Color(-16512890)};
      this.colorLayersArrays[3] = new Color[]{new Color(-16748426)};
      this.colorLayersArrays[5] = this.colorLayersArrays[0];
      this.colorLayersArrays[6] = Utilities.returnColorArrayDarker(this.colorLayersArrays[0], 0.2F);
      this.mobSoundSets.put(MobBase.MobGender.NONE, SoundModule.SoundSet.GHOST);
      this.idleSoundRate = 2000;
      this.blockMapGroup = MapData.BlockMapGroup.GHOSTS;
      this.aggressiveness = 0;
      this.levelCapLow = 1;
      this.levelCapHigh = 2;
      this.statVelocityBase = 0.2F;
      this.statVelocityMultiplier = 2.0E-4F;
      this.experienceMultiplier = 100;
      this.statsPerLevel = 25;
      this.statStrengthChance = 10;
      this.statDexterityChance = 5;
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
      this.ghostBaseType = MobBase.MobType.CATJEET_VILLAGER_CHILD;
      this.ghostNegativeBaseType = MobBase.MobType.ZOMBIE_CATJEET_CHILD;
      this.ghostBehaviorChance = 100;
      this.ghostFadeInChance = 5000;
      this.ghostFadeOutChance = 50;
      this.ghostFullMoonFadeChance = 50;
      this.ghostEclipseFadeChance = 10;
      this.ghostBloodMoonFadeChance = 10;
      this.ghostLeaveChance = 5000;
      this.allowGodExperience = true;
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
