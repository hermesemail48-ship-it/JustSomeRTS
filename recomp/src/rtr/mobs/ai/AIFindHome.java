package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.ObjectModule;
import rtr.objects.objectflags.ObjectFlags;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIFindHome extends AIBase {
   private static final int SEARCH_FOR_HOME = 0;
   private static final int VISIT_NEW_HOME = 1;
   private static AIBase resetBranch;

   public AIFindHome(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_FIND_HOME;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      if (this.thisMobFlags.getAllowHome()) {
         if (this.thisMob.getAssignedHome() == -1) {
            return true;
         } else {
            ObjectBase home = this.object.getObjects().get(this.thisMob.getAssignedHome());
            ObjectFlags objectFlags = home.getObjectFlags();
            if (objectFlags.getOccupantsJob() != null && objectFlags.getOccupantsJob() != this.thisMob.getMobJobType()) {
               return true;
            } else {
               return objectFlags.getOccupantsTypes().size() > 0 && !objectFlags.getOccupantsTypes().contains(this.thisMob.getMobType())
                  ? true
                  : this.betterHousingExists();
            }
         }
      } else {
         return false;
      }
   }

   private boolean betterHousingExists() {
      ArrayList<ObjectBase> objectList = this.object.getObjectsBySortGroup(ObjectModule.ObjectSortGroup.PROVIDES_OCCUPANTS_SLOTS);
      ArrayList<MobBase> lookingForHome = this.mob.getMobsByAssignment(Assignment.AssignmentType.FINDING_HOME, false);
      ObjectBase home = this.object.getObjects().get(this.thisMob.getAssignedHome());

      for (ObjectBase o : objectList) {
         ObjectFlags objectFlags = o.getObjectFlags();
         if (o.getOccupants().size() < objectFlags.getOccupantsMax()
            && this.thisMob.canReach(o)
            && (objectFlags.getOccupantsJob() == null || objectFlags.getOccupantsJob() == this.thisMob.getMobJobType())
            && (objectFlags.getOccupantsTypes().size() <= 0 || objectFlags.getOccupantsTypes().contains(this.thisMob.getMobType()))) {
            int enroute = 0;

            for (MobBase m : lookingForHome) {
               if (m.getAssignment().getPrimaryObject() == o) {
                  enroute++;
               }
            }

            if (o.getOccupants().size() + enroute < objectFlags.getOccupantsMax()) {
               if (home.getObjectFlags().getOccupantsJob() == null
                  && objectFlags.getOccupantsJob() != null
                  && objectFlags.getOccupantsJob() == this.thisMob.getMobJobType()) {
                  return true;
               }

               if (home.getObjectFlags().getOccupantsTypes().size() == 0
                  && objectFlags.getOccupantsTypes().size() > 0
                  && objectFlags.getOccupantsTypes().contains(this.thisMob.getMobType())) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   @Override
   public boolean tryRun() {
      return this.allowRun() && Utilities.randomInt(this.thisMobFlags.getHomeFindChance()) == 0;
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         if (this.runDropResource()) {
            switch (this.aiStep) {
               case 0:
                  this.searchForHome();
                  break;
               case 1:
                  this.visitNewHome();
            }
         }
      }
   }

   private void searchForHome() throws SlickException {
      ArrayList<ObjectBase> availableHomes = new ArrayList<>();
      ArrayList<ObjectBase> idealHomes = new ArrayList<>();
      ArrayList<ObjectBase> objectList = this.object.getObjectsBySortGroup(ObjectModule.ObjectSortGroup.PROVIDES_OCCUPANTS_SLOTS);
      ArrayList<MobBase> lookingForHome = this.mob.getMobsByAssignment(Assignment.AssignmentType.FINDING_HOME, false);

      for (ObjectBase o : objectList) {
         ObjectFlags objectFlags = o.getObjectFlags();
         if (o.getOccupants().size() < objectFlags.getOccupantsMax()
            && this.thisMob.canReach(o)
            && (objectFlags.getOccupantsJob() == null || objectFlags.getOccupantsJob() == this.thisMob.getMobJobType())
            && (objectFlags.getOccupantsTypes().size() <= 0 || objectFlags.getOccupantsTypes().contains(this.thisMob.getMobType()))) {
            int enroute = 0;

            for (MobBase m : lookingForHome) {
               if (m.getAssignment().getPrimaryObject() == o) {
                  enroute++;
               }
            }

            if (o.getOccupants().size() + enroute < objectFlags.getOccupantsMax()) {
               if (objectFlags.getOccupantsJob() != this.thisMob.getMobJobType() && !objectFlags.getOccupantsTypes().contains(this.thisMob.getMobType())) {
                  availableHomes.add(o);
               } else {
                  idealHomes.add(o);
               }
            }
         }
      }

      ArrayList<ObjectBase> possibleHomes = null;
      if (idealHomes.size() > 0) {
         possibleHomes = idealHomes;
      } else {
         possibleHomes = availableHomes;
      }

      if (possibleHomes != null && possibleHomes.size() > 0) {
         this.thisMob.setCurrentTask(Text.getText("aiTask.findHome.searchForHome"));
         ObjectBase attemptHome = possibleHomes.get(Utilities.randomInt(possibleHomes.size()));
         byte[][] blockMap = this.map.getMapData().getBlockMap(this.thisMobFlags.getBlockMapGroup());
         OrderedPair ox = attemptHome.getBestRandomInteractCoordinates(true);
         Assignment assignment = this.thisMob.getAssignment();
         assignment.reset();
         assignment.setPrimaryObject(attemptHome);
         assignment.setPrimaryCoordinates(ox);
         assignment.setAssignmentType(Assignment.AssignmentType.FINDING_HOME);
         assignment.finalizeAssignment();
         this.requestPath(assignment.getPrimaryCoordinates());
         this.aiStep = 1;
      } else {
         this.switchNode(resetBranch);
      }
   }

   private void visitNewHome() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.findHome.visitNewHome"));
      ObjectBase attemptHome = this.thisMob.getAssignment().getPrimaryObject();
      if (attemptHome != null && !attemptHome.isDestroyed()) {
         if (attemptHome.getOccupants().size() >= attemptHome.getObjectFlags().getOccupantsMax()) {
            this.aiStep = 0;
         } else {
            this.runPath();
            if (this.pathFailed) {
               this.switchNode(resetBranch);
            } else {
               if (this.pathCompleted) {
                  if (attemptHome.getOccupants().size() < attemptHome.getObjectFlags().getOccupantsMax()) {
                     if (this.object.getObjects().get(this.thisMob.getAssignedHome()) != null) {
                        this.object.getObjects().get(this.thisMob.getAssignedHome()).getOccupants().remove(this.thisMob);
                     }

                     this.thisMob.awardXP(Utilities.randomInt(20) + 20);
                     if (this.thisMobFlags.getAllowGodExperience()) {
                        this.god.increaseGodXP(0.03, GodModule.GodXPType.FINDING_NEW_HOME);
                     }

                     this.thisMob.setAssignedHome(attemptHome.getID());
                     this.object.getObjects().get(attemptHome.getID()).getOccupants().add(this.thisMob);
                     this.font.newFloater("iconHome", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
                  }

                  this.switchNode(resetBranch);
               }
            }
         }
      } else {
         this.aiStep = 0;
      }
   }
}
