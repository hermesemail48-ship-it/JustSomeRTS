package rtr.mobs.animals;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import rtr.mobs.MobBase;
import rtr.mobs.MobFactory;
import rtr.utilities.Utilities;

public class WildClucker extends MobBase {
   public WildClucker() throws SlickException {
   }

   @Override
   public void configNew(int id, float x, float y, boolean findReachMap) throws SlickException {
      this.configNew(id, x, y, null, findReachMap);
   }

   @Override
   public void configNew(int id, float x, float y, String message, boolean findReachMap) throws SlickException {
      super.configNew(id, x, y);
      this.gender = Utilities.randomInt(2) == 0 ? MobBase.MobGender.MALE : MobBase.MobGender.FEMALE;
      this.fullName = MobBase.MobType.WILD_CLUCKER.getName();
      this.mobType = MobBase.MobType.WILD_CLUCKER;
      this.mobGroup = MobBase.MobGroup.ANIMAL;
      this.sprite = this.mob.createMobSpriteSheet(this.mobType, this.gender);
      this.eggMax = 100;
      this.mobFlags = MobFactory.getMobFlagsInstance(this.mobType);
      Color[][] colorLayersArrays = this.mobFlags.getColorLayersArray();
      int randomColor = Utilities.randomInt(colorLayersArrays[0].length);
      this.colorLayers[0] = colorLayersArrays[0][randomColor];
      randomColor = Utilities.randomInt(colorLayersArrays[1].length);
      this.colorLayers[1] = colorLayersArrays[1][randomColor];
      randomColor = Utilities.randomInt(colorLayersArrays[2].length);
      this.colorLayers[2] = colorLayersArrays[2][randomColor];
      randomColor = Utilities.randomInt(colorLayersArrays[3].length);
      this.colorLayers[3] = colorLayersArrays[3][randomColor];
      randomColor = Utilities.randomInt(colorLayersArrays[4].length);
      this.colorLayers[4] = colorLayersArrays[4][randomColor];
      this.agingRateTick = Utilities.randomInt(this.mobFlags.getAgingRate() / 2);
      this.configureNewFinalize(message, findReachMap, false);
   }
}
