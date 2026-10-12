package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.ObjectModule;
import rtr.objects.towerhead.TowerBase;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIGolemHealBehavior extends AIBase {
   private static final int FIND_RECOMBOBULATOR = 0;
   private static final int PATHFIND_TO_RECOMBOBULATOR = 1;
   private static AIBase resetBranch;
   private static AIBase exitBranch;

   public AIGolemHealBehavior(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_GOLEM_HEAL_BEHAVIOR;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowGolemBehavior() && this.object.isInRange(this.thisMob.getTileX(), this.thisMob.getTileY());
   }

   @Override
   public boolean tryRun() {
      return this.allowRun() && Utilities.randomInt(this.thisMobFlags.getGolemBehaviorChance()) == 0;
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         exitBranch = this.thisMob.getAIGolemGuardBehavior();
         if (this.thisMob.getHitPoints() > this.thisMob.getHitPointsMax() * 0.5F) {
            this.switchNode(this.thisMob.getAIGolemGuardBehavior());
         } else {
            switch (this.aiStep) {
               case 0:
                  this.findRecombobulator();
                  break;
               case 1:
                  this.pathfindToRecombobulator();
            }
         }
      }
   }

   private void findRecombobulator() throws SlickException {
      ArrayList<ObjectBase> possibleRecombobulators = new ArrayList<>();

      for (ObjectBase o : this.object.getObjectsBySortGroup(ObjectModule.ObjectSortGroup.TOWERS)) {
         if (this.thisMob.canReach(o)
            && o.getTower() != null
            && o.getTower().getTowerFlags().getTowerType() == TowerBase.TowerType.RECOMBOBULATOR
            && o.getEnergy() >= o.getTower().getTowerFlags().getFireEnergyCost()) {
            possibleRecombobulators.add(o);
         }
      }

      if (possibleRecombobulators.size() > 0) {
         this.thisMob.setCurrentTask(Text.getText("aiTask.golemHealBehavior.findRecombobulator"));
         OrderedPair ox = this.pathFinder
            .searchForObject(this.thisMob.getTileX(), this.thisMob.getTileY(), possibleRecombobulators, false, true, this.thisMobFlags.getBlockMapGroup());
         if (ox == null) {
            this.switchNode(exitBranch);
            return;
         }

         ObjectBase closestCombobulator = this.object.getBuildingAtLocation(ox.getX(), ox.getY());
         Assignment assignment = this.thisMob.getAssignment();
         assignment.reset();
         assignment.setPrimaryObject(closestCombobulator);
         assignment.setPrimaryCoordinates(closestCombobulator.getRandomCoordinates());
         assignment.setAssignmentType(Assignment.AssignmentType.FINDING_HOME);
         assignment.finalizeAssignment();
         this.requestPath(assignment.getPrimaryCoordinates());
         this.aiStep = 1;
      } else {
         this.switchNode(exitBranch);
      }
   }

   private void pathfindToRecombobulator() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.golemHealBehavior.pathfindToRecombobulator"));
      this.runPath();
      ObjectBase currentObject = this.thisMob.getAssignment().getPrimaryObject();
      if (currentObject == null || currentObject.isDestroyed()) {
         this.aiStep = 0;
      } else if (this.pathFailed) {
         this.aiStep = 0;
      } else if (this.pathCompleted) {
         this.switchNode(resetBranch);
      }
   }
}
