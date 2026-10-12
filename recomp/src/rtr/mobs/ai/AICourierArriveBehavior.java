package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;

public class AICourierArriveBehavior extends AIBase {
   private static final int FIND_DROP_OFF_COORDINATES = 0;
   private static final int PATHFIND_TO_DROP_OFF_COORDINATES = 1;
   private static final int DROP_OFF_RESOURCES = 2;
   private static AIBase nextBranch;
   private static AIBase resetBranch;

   public AICourierArriveBehavior(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_COURIER_ARRIVE_BEHAVIOR;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowCourierBehavior() && this.thisMob.hasCarriedResources();
   }

   @Override
   public boolean tryRun() {
      return this.allowRun();
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         nextBranch = this.thisMob.getAIFindSpawn();
         resetBranch = this.thisMob.getAIStart();
         switch (this.aiStep) {
            case 0:
               this.findArrivalCoordinates();
               break;
            case 1:
               this.pathfindToDropOffCoordinates();
               break;
            case 2:
               this.dropOffResources();
         }
      }
   }

   private void findArrivalCoordinates() throws SlickException {
      this.thisMob.fadeIn();
      ArrayList<ObjectBase> objectList = this.object.getObjectsArrayList();
      ArrayList<ObjectBase> validObjects = new ArrayList<>();

      for (ObjectBase o : objectList) {
         if (!o.isPaused()
            && this.thisMob.canReach(o, true)
            && o.getObjectFlags().canBuildingStore(this.thisMob.getCarriedResourcesListType())
            && o.getObjectFlags().getBaseType() != MapTilesLoader.TileSet.CASTLE_1) {
            validObjects.add(o);
         }
      }

      if (validObjects.size() <= 0) {
         ObjectBase castle = this.object.getFirstBaseTypeFound(MapTilesLoader.TileSet.CASTLE_1);
         if (castle != null && this.thisMob.canReach(castle)) {
            validObjects.add(castle);
         }
      }

      OrderedPair leaveCoordinates = null;
      if (validObjects.size() > 0) {
         leaveCoordinates = this.pathFinder
            .searchForObject(this.thisMob.getTileX(), this.thisMob.getTileY(), validObjects, false, true, this.thisMobFlags.getBlockMapGroup());
      }

      if (leaveCoordinates == null) {
         this.switchNode(resetBranch);
      } else {
         Assignment assignment = this.thisMob.getAssignment();
         String mapName = assignment.getRegionName();
         String mapFileName = assignment.getRegionFileName();
         assignment.reset();
         assignment.setPrimaryCoordinates(leaveCoordinates);
         assignment.setRegionName(mapName);
         assignment.setRegionFileName(mapFileName);
         assignment.setAssignmentType(Assignment.AssignmentType.COURIER_ARRIVING);
         assignment.finalizeAssignment();
         this.requestPath(leaveCoordinates);
         this.aiStep = 1;
      }
   }

   private void pathfindToDropOffCoordinates() throws SlickException {
      if (this.thisMob.getAssignment().getRegionName().equals("")) {
         this.thisMob.setCurrentTask(Text.getText("aiTask.courierArriveBehavior.pathfindToDropOffCoordinatesNoRegion"));
      } else {
         Text.setVariableText("regionName", this.thisMob.getAssignment().getRegionName());
         this.thisMob.setCurrentTask(Text.getText("aiTask.courierArriveBehavior.pathfindToDropOffCoordinates"));
      }

      this.runPath();
      if (this.pathFailed) {
         this.aiStep = 0;
      } else {
         if (this.pathCompleted) {
            this.aiStep = 2;
         }
      }
   }

   private void dropOffResources() throws SlickException {
      if (!this.runDropResource()) {
         if (this.thisMob.getAssignment().getRegionName().equals("")) {
            this.thisMob.setCurrentTask(Text.getText("aiTask.courierArriveBehavior.dropOffResourcesNoRegion"));
         } else {
            Text.setVariableText("regionName", this.thisMob.getAssignment().getRegionName());
            this.thisMob.setCurrentTask(Text.getText("aiTask.courierArriveBehavior.dropOffResources"));
         }
      } else {
         this.switchNode(nextBranch);
      }
   }
}
