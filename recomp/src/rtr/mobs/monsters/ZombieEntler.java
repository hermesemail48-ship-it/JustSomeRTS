package rtr.mobs.monsters;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import rtr.mobs.MobBase;
import rtr.mobs.MobFactory;
import rtr.utilities.Utilities;

public class ZombieEntler extends MobBase {
   public ZombieEntler() throws SlickException {
   }

   @Override
   public void configNew(int id, float x, float y, boolean findReachMap) throws SlickException {
      this.configNew(id, x, y, null, findReachMap);
   }

   @Override
   public void configNew(int id, float x, float y, String message, boolean findReachMap) throws SlickException {
      super.configNew(id, x, y);
      this.gender = Utilities.randomInt(2) == 0 ? MobBase.MobGender.MALE : MobBase.MobGender.FEMALE;
      this.fullName = MobBase.MobType.ZOMBIE_ENTLER.getName();
      this.mobType = MobBase.MobType.ZOMBIE_ENTLER;
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
      if (Utilities.randomInt(5) == 0) {
         randomColor = Utilities.randomInt(colorLayersArrays[1].length);
      }

      this.colorLayers[1] = colorLayersArrays[1][randomColor];
      this.colorLayers[2] = colorLayersArrays[2][0];
      if (Utilities.randomInt(5) == 0) {
         randomColor = Utilities.randomInt(colorLayersArrays[3].length);
      }

      this.colorLayers[3] = colorLayersArrays[3][randomColor];
      if (Utilities.randomInt(5) == 0) {
         randomColor = Utilities.randomInt(colorLayersArrays[4].length);
      }

      this.colorLayers[4] = colorLayersArrays[4][randomColor];
      this.colorLayers[5] = colorLayersArrays[5][0];
      this.configureNewFinalize(message, findReachMap, true);
   }
}
