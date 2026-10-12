package rtr.mobs.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.TimeModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AICaptureAnimals extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_ANIMALS = 1;
   private static final int SEARCH_NEARBY = 2;
   private static final int CHASE_ANIMAL = 3;
   private static final int GRAB_ANIMAL = 4;
   private static final int LIFT_ANIMAL = 5;
   private static AIBase resetBranch;
   private static AIBase failBranch;
   private static AIBase nextBranch;

   public AICaptureAnimals(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_CAPTURE_ANIMALS;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowWork() && this.thisMob.getMobJobType() != MobJobBase.MobJobType.NONE;
   }

   @Override
   public boolean tryRun() {
      return this.allowRun();
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         failBranch = this.thisMob.getAIDismantleWork();
         nextBranch = this.thisMob.getAICaptureAnimalsInHand();
         if (!this.thisMob.getMobJobType().getJob().canCaptureAnimals()) {
            this.switchNode(failBranch);
         } else if (this.time.getTimeOfDay() != TimeModule.TimeOfDay.MORNING
            && this.time.getTimeOfDay() != TimeModule.TimeOfDay.MIDDAY
            && this.time.getTimeOfDay() != TimeModule.TimeOfDay.EVENING) {
            this.switchNode(failBranch);
         } else if (this.time.isEclipse()) {
            this.switchNode(failBranch);
         } else if (this.aiStep <= 1 && this.thisMobFlags.getAllowThirst() && this.thisMob.getThirst() < this.thisMobFlags.getThirstThirstyWaterSearchLevel()) {
            this.switchNode(failBranch);
         } else if (this.aiStep <= 1 && this.thisMobFlags.getAllowHunger() && this.thisMob.getHunger() < this.thisMobFlags.getHungerHungryFoodSearchLevel()) {
            this.switchNode(failBranch);
         } else {
            switch (this.aiStep) {
               case 0:
                  this.findWork();
                  return;
               case 1:
                  this.pathfindToAnimals();
                  return;
               case 2:
                  this.searchNearby();
                  return;
               case 3:
                  this.chaseAnimal();
                  return;
               case 4:
                  this.grabAnimal();
                  return;
               case 5:
                  this.liftAnimal();
                  return;
            }
         }
      }
   }

   private void findWork() throws SlickException {
      HashMap<ResourceModule.ResourceType, ArrayList<OrderedPair>> waypoints = this.waypoint.getAnimalResourceWaypointsMap();
      if (waypoints.size() == 0) {
         this.switchNode(failBranch);
      } else {
         ArrayList<ObjectBase> objects = this.object.getObjectMasterBuildingPriorityList();
         HashMap<OrderedPair, ArrayList<ObjectBase>> validWaypoints = new HashMap<>();

         for (ArrayList<OrderedPair> list : waypoints.values()) {
            for (OrderedPair o : list) {
               if (this.thisMob.canReach(o)) {
                  ArrayList<MobBase> mobsNearby = this.mob.getMobsNearby(o.getX(), o.getY(), 24, MobBase.MobGroup.ANIMAL);
                  if (mobsNearby.size() != 0) {
                     int totalHunting = 0;
                     ArrayList<MobBase> allHunters = this.mob.getMobsByAssignment(Assignment.AssignmentType.CAPTURING_ANIMAL, false);

                     for (MobBase m : allHunters) {
                        if (m != this.thisMob && m.getAssignment().getSecondaryCoordinates() == o) {
                           totalHunting++;
                        }
                     }

                     if (totalHunting < mobsNearby.size()) {
                        ArrayList<MobBase.MobType> mobDomesticatedTypes = new ArrayList<>();

                        for (MobBase mx : mobsNearby) {
                           MobBase.MobType domesticatedType = mx.getMobFlags().getDomesticateType();
                           if (domesticatedType != null && !mobDomesticatedTypes.contains(domesticatedType)) {
                              mobDomesticatedTypes.add(domesticatedType);
                           }
                        }

                        for (ObjectBase objectCheck : objects) {
                           if (this.canObjectStoreAnyAnimals(objectCheck, allHunters) && this.thisMob.canReach(objectCheck)) {
                              ObjectFlags objectFlags = objectCheck.getObjectFlags();
                              ArrayList<MobBase.MobType> validTypes = new ArrayList<>();

                              for (MobBase.MobType t : objectFlags.getOccupantsTypes()) {
                                 if (mobDomesticatedTypes.contains(t)) {
                                    validTypes.add(t);
                                 }
                              }

                              if (validTypes.size() > 0) {
                                 if (validWaypoints.get(o) == null) {
                                    validWaypoints.put(o, new ArrayList<>());
                                 }

                                 validWaypoints.get(o).add(objectCheck);
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         if (validWaypoints.size() <= 0) {
            this.switchNode(resetBranch);
         } else {
            ArrayList<OrderedPair> validWaypointsAsList = new ArrayList<>();
            validWaypointsAsList.addAll(validWaypoints.keySet());
            OrderedPair closestWaypoint = this.pathFinder
               .searchFor(this.thisMob.getTileX(), this.thisMob.getTileY(), validWaypointsAsList, false, true, this.thisMobFlags.getBlockMapGroup());
            if (closestWaypoint != null) {
               ObjectBase lowestAnimalPen = validWaypoints.get(closestWaypoint).get(0);

               for (ObjectBase ox : validWaypoints.get(closestWaypoint)) {
                  if (ox.getOccupants().size() < lowestAnimalPen.getOccupants().size()) {
                     lowestAnimalPen = ox;
                  }

                  if (lowestAnimalPen.getOccupants().size() == 0) {
                     break;
                  }
               }

               ArrayList<OrderedPair> nearbyCoordinates = this.pathFinder
                  .getCoordinatesInRange(closestWaypoint.getX(), closestWaypoint.getY(), 16, false, this.thisMobFlags.getBlockMapGroup());
               OrderedPair coordinatesOut = nearbyCoordinates.get(Utilities.randomInt(nearbyCoordinates.size()));
               Assignment assignment = this.thisMob.getAssignment();
               assignment.reset();
               assignment.setPrimaryCoordinates(coordinatesOut);
               assignment.setSecondaryCoordinates(closestWaypoint);
               assignment.setDeliveryObject(lowestAnimalPen);
               assignment.setAssignmentType(Assignment.AssignmentType.CAPTURING_ANIMAL);
               assignment.finalizeAssignment();
               this.requestPath(coordinatesOut);
               this.aiStep = 1;
            } else {
               this.switchNode(resetBranch);
            }
         }
      }
   }

   private void pathfindToAnimals() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.captureAnimals.huntingAnimals"));
      this.runPath();
      if (this.pathFailed) {
         this.aiStep = 0;
      } else if (this.pathCompleted) {
         this.thisMob.awardXP(Utilities.randomInt(2) + 2);
         if (this.thisMobFlags.getAllowGodExperience()) {
            this.god.increaseGodXP(0.03, GodModule.GodXPType.HUNTING);
         }

         this.aiStep = 2;
      }
   }

   private void searchNearby() throws SlickException {
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      ArrayList<MobBase> mobsNearby = this.mob.getMobsNearby(tileX, tileY, 32, MobBase.MobGroup.ANIMAL);
      if (mobsNearby.size() == 0) {
         this.thisMob.getAssignment().reset();
         this.aiStep = 0;
      } else {
         mobsNearby = this.mob.cullMobsByDistance(tileX, tileY, 32, mobsNearby, this.thisMobFlags.getBlockMapGroup());
         if (mobsNearby.size() == 0) {
            this.thisMob.getAssignment().reset();
            this.aiStep = 0;
         } else {
            ArrayList<MobBase> allHunters = this.mob.getMobsByAssignment(Assignment.AssignmentType.CAPTURING_ANIMAL, false);
            ArrayList<MobBase> mobsToRemove = new ArrayList<>();

            for (MobBase m : mobsNearby) {
               for (MobBase h : allHunters) {
                  if (h.getAssignment().getPrimaryMob() == m) {
                     mobsToRemove.add(m);
                  }
               }
            }

            mobsNearby.removeAll(mobsToRemove);
            if (mobsNearby.size() == 0) {
               this.thisMob.getAssignment().reset();
               this.aiStep = 0;
            } else {
               ArrayList<MobBase.MobType> mobDomesticatedTypes = new ArrayList<>();

               for (MobBase m : mobsNearby) {
                  MobBase.MobType domesticatedType = m.getMobFlags().getDomesticateType();
                  if (domesticatedType != null && !mobDomesticatedTypes.contains(domesticatedType)) {
                     mobDomesticatedTypes.add(domesticatedType);
                  }
               }

               ObjectBase deliveryObject = this.thisMob.getAssignment().getDeliveryObject();
               if (!this.thisMob.canReach(deliveryObject)) {
                  this.thisMob.getAssignment().reset();
                  this.aiStep = 0;
               } else if (!this.object.objectExists(deliveryObject)) {
                  this.thisMob.getAssignment().reset();
                  this.aiStep = 0;
               } else {
                  ObjectFlags deliveryObjectFlags = deliveryObject.getObjectFlags();
                  ArrayList<MobBase.MobType> validTypes = new ArrayList<>();

                  for (MobBase.MobType t : deliveryObjectFlags.getOccupantsTypes()) {
                     if (mobDomesticatedTypes.contains(t)) {
                        validTypes.add(t);
                     }
                  }

                  if (validTypes.size() <= 0) {
                     this.thisMob.getAssignment().reset();
                     this.aiStep = 0;
                  } else {
                     Collections.shuffle(validTypes);
                     Collections.shuffle(mobsNearby);

                     for (MobBase.MobType tx : validTypes) {
                        for (MobBase mx : mobsNearby) {
                           if (mx.getMobFlags().getDomesticateType() == tx) {
                              this.thisMob.getAssignment().setPrimaryMob(mx);
                              this.thisMob.getAssignment().setAssignmentType(Assignment.AssignmentType.CAPTURING_ANIMAL);
                              this.requestPath(mx.getTileX(), mx.getTileY());
                              this.aiStep = 3;
                              return;
                           }
                        }
                     }

                     this.switchNode(resetBranch);
                  }
               }
            }
         }
      }
   }

   private void chaseAnimal() throws SlickException {
      Text.setVariableText("mobName", this.thisMob.getAssignment().getPrimaryMob().getFormattedName(true));
      this.thisMob.setCurrentTask(Text.getText("aiTask.captureAnimals.chaseAnimal"));
      this.followMob(this.thisMob.getAssignment().getPrimaryMob());
      if (this.pathFailed) {
         this.thisMob.getAssignment().reset();
         this.aiStep = 0;
      } else {
         if (this.pathCompleted) {
            this.aiStep = 4;
         }
      }
   }

   private void grabAnimal() throws SlickException {
      MobBase m = this.thisMob.getAssignment().getPrimaryMob();
      ObjectBase o = this.thisMob.getAssignment().getDeliveryObject();
      if (o != null
         && this.thisMob.canReach(o, true)
         && Math.abs(this.thisMob.getTileX() - m.getTileX()) <= 1
         && Math.abs(this.thisMob.getTileY() - m.getTileY()) <= 1) {
         this.thisMob.setPositionPickup();
         if (this.thisMob.getSprite().isStopped()) {
            ResourceBase capturedAnimalResource = this.resource.createResourceUnassigned(ResourceModule.ResourceType.CAPTURED_ANIMAL);
            capturedAnimalResource.capture(m);
            this.thisMob.addCarriedResource(capturedAnimalResource, true);
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionLift();
            this.aiStep = 5;
         }
      } else {
         this.thisMob.getSprite().restart();
         this.thisMob.setPositionFace();
         this.aiStep = 0;
      }
   }

   private void liftAnimal() throws SlickException {
      this.thisMob.setPositionLift();
      if (this.thisMob.getSprite().isStopped()) {
         ObjectBase o = this.thisMob.getAssignment().getDeliveryObject();
         MobBase m = this.thisMob.getAssignment().getPrimaryMob();
         if (o != null && this.thisMob.canReach(o, true)) {
            this.switchNode(nextBranch);
            Assignment assignment = this.thisMob.getAssignment();
            assignment.setDeliveryObject(o);
            assignment.setPrimaryMob(m);
            assignment.setAssignmentType(Assignment.AssignmentType.CAPTURING_ANIMAL);
            assignment.finalizeAssignment();
         } else {
            this.switchNode(nextBranch);
         }
      }
   }

   private boolean canObjectStoreAnyAnimals(ObjectBase o, ArrayList<MobBase> allHunters) {
      ObjectFlags objectFlags = o.getObjectFlags();
      if (objectFlags.getOccupantsMax() == 0) {
         return false;
      } else if (!this.thisMob.canReach(o, true)) {
         return false;
      } else {
         int availableSpace = objectFlags.getOccupantsMax() - o.getOccupants().size();
         if (availableSpace <= 0) {
            return false;
         } else {
            int totalEnroute = 0;

            for (MobBase m : allHunters) {
               if (m != this.thisMob && m.getAssignment().getDeliveryObject() == o) {
                  totalEnroute++;
               }
            }

            availableSpace -= totalEnroute;
            return availableSpace > 0;
         }
      }
   }
}
