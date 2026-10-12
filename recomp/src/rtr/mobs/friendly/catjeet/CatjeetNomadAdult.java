package rtr.mobs.friendly.catjeet;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import rtr.mobs.MobBase;
import rtr.mobs.MobFactory;
import rtr.mobs.MobModule;
import rtr.utilities.Utilities;

public class CatjeetNomadAdult extends MobBase {
   public CatjeetNomadAdult() throws SlickException {
   }

   @Override
   public void configNew(int id, float x, float y, boolean findReachMap) throws SlickException {
      this.configNew(id, x, y, null, findReachMap);
   }

   @Override
   public void configNew(int id, float x, float y, String message, boolean findReachMap) throws SlickException {
      super.configNew(id, x, y);
      this.gender = Utilities.randomInt(2) == 0 ? MobBase.MobGender.MALE : MobBase.MobGender.FEMALE;
      this.generateFullName(MobModule.MobNameType.STANDARD);
      this.mobType = MobBase.MobType.CATJEET_NOMAD;
      this.mobGroup = MobBase.MobGroup.NOMAD;
      this.hostileMobGroups.add(MobBase.MobGroup.MONSTER);
      this.friendlyMobGroups.add(MobBase.MobGroup.VILLAGER);
      this.friendlyMobGroups.add(MobBase.MobGroup.GOLEM);
      this.friendlyMobGroups.add(MobBase.MobGroup.CHARMED_MONSTER);
      this.friendlyMobGroups.add(MobBase.MobGroup.NOMAD);
      this.friendlyMobGroups.add(MobBase.MobGroup.MIGRANT);
      this.friendlyMobGroups.add(MobBase.MobGroup.PROVISIONER);
      this.sprite = this.mob.createMobSpriteSheet(this.mobType, this.gender);
      this.mobFlags = MobFactory.getMobFlagsInstance(this.mobType);
      Color[][] colorLayersArrays = this.mobFlags.getColorLayersArray();
      int randomColor = Utilities.randomInt(colorLayersArrays[0].length);
      this.colorLayers[0] = colorLayersArrays[0][randomColor];
      if (Utilities.randomInt(5) == 0) {
         randomColor = Utilities.randomInt(colorLayersArrays[1].length);
      }

      this.colorLayers[1] = colorLayersArrays[1][randomColor];
      this.colorLayers[2] = colorLayersArrays[2][0];
      this.colorLayers[3] = colorLayersArrays[3][0];
      if (Utilities.randomInt(5) == 0) {
         randomColor = Utilities.randomInt(colorLayersArrays[4].length);
      }

      this.colorLayers[4] = colorLayersArrays[4][randomColor];
      if (Utilities.randomInt(5) == 0) {
         randomColor = Utilities.randomInt(colorLayersArrays[6].length);
      }

      this.colorLayers[6] = colorLayersArrays[6][randomColor];
      this.agingRateTick = Utilities.randomInt(this.mobFlags.getAgingRate() / 2);
      this.configureNewFinalize(message, findReachMap, false);
   }
}
