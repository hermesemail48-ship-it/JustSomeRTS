package rtr.mobs.ai;

import java.util.ArrayList;
import java.util.HashMap;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.map.MapModule;
import rtr.map.WorkSelectionModule;
import rtr.mobs.MobBase;
import rtr.mobs.MobModule;
import rtr.objects.ObjectBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.road.RoadModule;
import rtr.states.StateBase;
import rtr.utilities.OrderedPair;

public class AIUtilities {
   public static ArrayList<ObjectBase> createLowestPriorityStorageListHashMap(
      MobBase thisMob, ResourceModule.ResourceType validResourceType, ArrayList<ObjectBase> objectList
   ) {
      ArrayList<ResourceModule.ResourceType> validResourceTypes = new ArrayList<>();
      validResourceTypes.add(validResourceType);
      return createLowestPriorityStorageListHashMap(thisMob, validResourceTypes, objectList).get(validResourceType);
   }

   public static HashMap<ResourceModule.ResourceType, ArrayList<ObjectBase>> createLowestPriorityStorageListHashMap(
      MobBase thisMob, ArrayList<ResourceModule.ResourceType> validResourceTypes, ArrayList<ObjectBase> objectList
   ) {
      HashMap<ResourceModule.ResourceType, ArrayList<ObjectBase>> validStoresByType = new HashMap<>();

      for (ResourceModule.ResourceType t : validResourceTypes) {
         validStoresByType.put(t, new ArrayList<>());

         for (ObjectBase o : objectList) {
            if (thisMob.canReach(o, true)
               && !o.isPaused()
               && o.isResourceAvailable(t)
               && (
                  !o.getObjectFlags().canBuildingBeBuilt(t)
                     || o.getResourceCount(t) > o.getObjectFlags().getResourceValueBaseCount(t) - o.getResourceValueCount(t)
               )) {
               validStoresByType.get(t).add(o);
            }
         }
      }

      HashMap<ResourceModule.ResourceType, ArrayList<ObjectBase>> validStoresByTypeLowestPriority = new HashMap<>();

      for (ResourceModule.ResourceType t : validResourceTypes) {
         validStoresByTypeLowestPriority.put(t, new ArrayList<>());
         ArrayList<ObjectBase> validStores = validStoresByType.get(t);
         ObjectBase.ResourceStorePriority lowestPriority = ObjectBase.ResourceStorePriority.VERY_HIGH;

         for (ObjectBase ox : validStores) {
            ObjectBase.ResourceStorePriority p = ox.getObjectFlags().getBuildingStorePriority().get(t);
            if (p != null && p.ordinal() > lowestPriority.ordinal()) {
               lowestPriority = p;
            }
         }

         ArrayList<ObjectBase> lowestPriorityOut = new ArrayList<>();

         for (ObjectBase oxx : validStores) {
            ObjectBase.ResourceStorePriority p = oxx.getObjectFlags().getBuildingStorePriority().get(t);
            if (p == lowestPriority) {
               lowestPriorityOut.add(oxx);
            }
         }

         validStoresByTypeLowestPriority.put(t, lowestPriorityOut);
      }

      return validStoresByTypeLowestPriority;
   }

   public static ArrayList<ResourceBase> createAvailabeResourceList(
      MobBase thisMob, ResourceModule.ResourceType resourceType, boolean allowResourcesInVillage, int nearbyDistance
   ) throws SlickException {
      ArrayList<ResourceModule.ResourceType> validResourceTypes = new ArrayList<>();
      validResourceTypes.add(resourceType);
      return createAvailabeResourceList(thisMob, validResourceTypes, allowResourcesInVillage, nearbyDistance).get(resourceType);
   }

   public static HashMap<ResourceModule.ResourceType, ArrayList<ResourceBase>> createAvailabeResourceList(
      MobBase thisMob, ArrayList<ResourceModule.ResourceType> validResourceTypes, boolean allowResourcesInVillage, int nearbyDistance
   ) throws SlickException {
      ResourceModule resource = (ResourceModule)StateBase.getModule(ModuleBase.ModuleType.RESOURCE);
      RoadModule road = (RoadModule)StateBase.getModule(ModuleBase.ModuleType.ROAD);
      MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
      ArrayList<ResourceBase> resourceListFinal = null;
      ArrayList<ResourceBase> resourcesOnGroundNearby = resource.getResourcesNearby(thisMob.getTileX(), thisMob.getTileY(), nearbyDistance);
      resourcesOnGroundNearby = resource.cullResourcesByDistance(
         thisMob.getTileX(), thisMob.getTileY(), nearbyDistance, resourcesOnGroundNearby, thisMob.getMobFlags().getBlockMapGroup()
      );
      if (allowResourcesInVillage) {
         ArrayList<ResourceBase> resourcesOnGround = resource.getResourcesOnGroundInVillage();

         for (ResourceBase r : resourcesOnGroundNearby) {
            if (!resourcesOnGround.contains(r)) {
               resourcesOnGround.add(r);
            }
         }

         resourceListFinal = resourcesOnGround;
      } else {
         resourceListFinal = resourcesOnGroundNearby;
      }

      HashMap<ResourceModule.ResourceType, ArrayList<ResourceBase>> validResourcesOnGroundByType = new HashMap<>();

      for (ResourceModule.ResourceType t : validResourceTypes) {
         validResourcesOnGroundByType.put(t, new ArrayList<>());
      }

      for (ResourceBase rx : resourceListFinal) {
         if (thisMob.canReach(rx)
            && !rx.isAssigned()
            && !road.isResourceNeededForRoad(rx)
            && (!thisMob.getMobFlags().canIgnoreThreatenedTiles() || !map.getMapThreatMap().isResourceThreatened(rx))
            && validResourceTypes.contains(rx.getType())) {
            validResourcesOnGroundByType.get(rx.getType()).add(rx);
         }
      }

      return validResourcesOnGroundByType;
   }

   public static ArrayList<OrderedPair> createAvailabeResourceCoordinatesList(
      MobBase thisMob, ArrayList<ObjectBase> objectList, ArrayList<ResourceBase> resourceList
   ) {
      ArrayList<OrderedPair> coordinatesList = new ArrayList<>();
      if (objectList != null) {
         for (ObjectBase o : objectList) {
            coordinatesList.add(o.getBestRandomInteractCoordinates(thisMob, true, true, true));
         }
      }

      if (resourceList != null) {
         for (ResourceBase r : resourceList) {
            coordinatesList.add(OrderedPair.getOrderedPair(r.getTileX(), r.getTileY()));
         }
      }

      return coordinatesList;
   }

   public static int getTotalNeededRoadResourcesCount(MobBase thisMob) {
      MobModule mob = (MobModule)StateBase.getModule(ModuleBase.ModuleType.MOB);
      RoadModule road = (RoadModule)StateBase.getModule(ModuleBase.ModuleType.ROAD);
      WorkSelectionModule workSelection = (WorkSelectionModule)StateBase.getModule(ModuleBase.ModuleType.WORK_SELECTION);
      int enRouteCount = 0;
      int thisTypeCount = 0;
      ArrayList<MobBase> mobList = mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false);
      ArrayList<OrderedPair> roadSelectionList = new ArrayList<>();
      roadSelectionList.addAll(workSelection.getRoadSelectionMapArray().keySet());
      ResourceModule.ResourceType resourceType = thisMob.getAssignment().getPickupResourceType();
      if (resourceType == null && thisMob.hasCarriedResources()) {
         resourceType = thisMob.getCarriedResourcesListType();
      }

      for (MobBase m : mobList) {
         if (m.getAssignment() != null) {
            ArrayList<ResourceBase> pickupResources = m.getAssignment().getPickupResources();
            if ((pickupResources.size() != 0 || m.hasCarriedResources())
               && m.getMobJobType().getJob().canWorkerDeliverRoadConstruction(resourceType)
               && roadSelectionList.contains(m.getAssignment().getSecondaryCoordinates())) {
               if (pickupResources.size() > 0 && pickupResources.get(0).getType() == resourceType) {
                  enRouteCount += pickupResources.size();
               }

               if (m.hasCarriedResources() && m.getCarriedResourcesListType() == resourceType) {
                  enRouteCount += m.getCarriedResourcesList().size();
               }
            }
         }
      }

      for (OrderedPair o : roadSelectionList) {
         if (road.getNextRequiredResource(o.getX(), o.getY()) == resourceType) {
            thisTypeCount++;
         }
      }

      return thisTypeCount - enRouteCount;
   }
}
