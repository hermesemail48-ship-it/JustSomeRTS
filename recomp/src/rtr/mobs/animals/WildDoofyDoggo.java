package rtr.mobs.animals;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import rtr.mobs.MobBase;
import rtr.mobs.MobFactory;
import rtr.utilities.Utilities;

public class WildDoofyDoggo extends MobBase {
   public WildDoofyDoggo() throws SlickException {
   }

   @Override
   public void configNew(int id, float x, float y, boolean findReachMap) throws SlickException {
      this.configNew(id, x, y, null, findReachMap);
   }

   @Override
   public void configNew(int id, float x, float y, String message, boolean findReachMap) throws SlickException {
      super.configNew(id, x, y);
      this.gender = MobBase.MobGender.MALE;
      this.firstName = "Moon";
      this.lastName = "Moon";
      this.fullName = "Moon Moon";
      this.mobType = MobBase.MobType.WILD_DOOFY_DOGGO;
      this.mobGroup = MobBase.MobGroup.ANIMAL;
      this.friendlyMobGroups.add(MobBase.MobGroup.VILLAGER);
      this.sprite = this.mob.createMobSpriteSheet(this.mobType, this.gender);
      this.mobFlags = MobFactory.getMobFlagsInstance(this.mobType);
      Color[][] colorLayersArrays = this.mobFlags.getColorLayersArray();
      this.colorLayers[0] = colorLayersArrays[0][0];
      this.colorLayers[1] = colorLayersArrays[1][0];
      this.colorLayers[2] = colorLayersArrays[2][0];
      this.colorLayers[3] = colorLayersArrays[3][0];
      this.colorLayers[4] = colorLayersArrays[4][0];
      this.colorLayers[5] = colorLayersArrays[5][0];
      this.agingRateTick = Utilities.randomInt(this.mobFlags.getAgingRate() / 2);
      this.configureNewFinalize(message, findReachMap, false);
   }
}
