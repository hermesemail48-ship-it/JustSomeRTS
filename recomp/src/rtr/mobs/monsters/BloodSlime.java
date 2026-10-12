package rtr.mobs.monsters;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import rtr.mobs.MobBase;
import rtr.mobs.MobFactory;
import rtr.utilities.Utilities;

public class BloodSlime extends MobBase {
   public BloodSlime() throws SlickException {
   }

   @Override
   public void configNew(int id, float x, float y, boolean findReachMap) throws SlickException {
      this.configNew(id, x, y, null, findReachMap);
   }

   @Override
   public void configNew(int id, float x, float y, String message, boolean findReachMap) throws SlickException {
      super.configNew(id, x, y);
      this.gender = MobBase.MobGender.NONE;
      this.fullName = MobBase.MobType.BLOOD_SLIME.getName();
      this.mobType = MobBase.MobType.BLOOD_SLIME;
      this.mobGroup = MobBase.MobGroup.MONSTER;
      this.hostileMobGroups.add(MobBase.MobGroup.VILLAGER);
      this.hostileMobGroups.add(MobBase.MobGroup.DOMESTICATED_ANIMAL);
      this.hostileMobGroups.add(MobBase.MobGroup.GOLEM);
      this.hostileMobGroups.add(MobBase.MobGroup.CHARMED_MONSTER);
      this.hostileMobGroups.add(MobBase.MobGroup.NOMAD);
      this.hostileMobGroups.add(MobBase.MobGroup.MIGRANT);
      this.friendlyMobGroups.add(MobBase.MobGroup.MONSTER);
      this.sprite = this.mob.createMobSpriteSheet(this.mobType, this.gender);
      this.mobFlags = MobFactory.getMobFlagsInstance(this.mobType);
      Color[][] colorLayersArrays = this.mobFlags.getColorLayersArray();
      int randomColor = Utilities.randomInt(colorLayersArrays[0].length);
      this.colorLayers[0] = colorLayersArrays[0][randomColor];
      this.configureNewFinalize(message, findReachMap, false);
   }
}
