package rtr.mobs.ai;

import java.util.ArrayList;
import java.util.Collections;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;

public class AINomadBehaviorOther extends AIBase {
   private static final int FIND_VILLAGE = 0;
   private static final int PATHFIND_TO_VILLAGE = 1;
   private static final int JOIN_VILLAGE = 2;
   private static AIBase resetBranch;

   public AINomadBehaviorOther(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_NOMAD_BEHAVIOR_OTHER;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowNomadBehavior();
   }

   @Override
   public boolean tryRun() {
      return this.allowRun() && this.object.isInRange(this.thisMob.getTileX(), this.thisMob.getTileY());
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         switch (this.aiStep) {
            case 0:
               this.findVillage();
               break;
            case 1:
               this.pathfindToVillage();
               break;
            case 2:
               this.joinVillage();
         }
      }
   }

   private void findVillage() throws SlickException {
      ObjectBase building = this.object.getFirstBaseTypeFound(MapTilesLoader.TileSet.CASTLE_1);
      if (building == null) {
         ArrayList<ObjectBase> otherBuildings = new ArrayList<>();
         otherBuildings.addAll(this.object.getObjectMasterBuildingPriorityList());
         Collections.shuffle(otherBuildings);

         for (ObjectBase o : otherBuildings) {
            if (o.getObjectFlags().getBaseType().getTileSetTemplate().getType() != MapTilesLoader.TileSetType.WALL && this.thisMob.canReach(o)) {
               building = o;
               break;
            }
         }
      }

      if (building != null && this.thisMob.canReach(building)) {
         this.coordinates = building.getRandomCoordinates(this.thisMob, true, true);
         this.requestPath(this.coordinates);
         this.aiStep = 1;
      } else {
         this.switchNode(resetBranch);
      }
   }

   private void pathfindToVillage() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.nomadBehaviorOther.pathfindToVillage"));
      this.runPath();
      if (this.pathCompleted) {
         this.aiStep = 2;
      } else if (this.pathFailed) {
         this.switchNode(resetBranch);
      }
   }

   private void joinVillage() throws SlickException {
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      ObjectBase building = this.object.getBuildingAtLocation(tileX, tileY);
      if (building != null && building.getObjectFlags().getMobGroup() == MobBase.MobGroup.VILLAGER) {
         this.sound.playSound(SoundModule.SoundType.VILLAGER_BIRTH);
         this.mob.spawnMob(this.thisMob, this.thisMobFlags.getNomadBehaviorMobType(), Text.getText("console.banner.mob.nomadJoined.subtitle"), true);
         this.god.increaseGodXP(2.0, GodModule.GodXPType.NOMADS_JOINED_VILLAGE);
         this.stats.increaseNomadsJoinedThisPeriod(this.thisMobFlags.getNomadBehaviorMobType());
         String thisMobTypeString = this.thisMob.getMobType().toString();
         if (thisMobTypeString.contains("NOMAD")) {
            this.stats.increaseNomadsJoinedThisPeriod(this.thisMobFlags.getNomadBehaviorMobType());
            this.goal.incrementGoal(GoalModule.GoalType.IMMIGRANT_SONG);
            if (thisMobTypeString.contains("CATJEET")) {
               this.goal.incrementGoal(GoalModule.GoalType.STRAYS);
            }
         }
      } else {
         this.aiStep = 0;
      }
   }
}
