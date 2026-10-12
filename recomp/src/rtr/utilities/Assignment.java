package rtr.utilities;

import java.util.ArrayList;
import rtr.ModuleBase;
import rtr.console.Console;
import rtr.mobs.MobBase;
import rtr.mobs.MobModule;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.ObjectModule;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;

public class Assignment {
   private MobModule mob = (MobModule)StateBase.getModule(ModuleBase.ModuleType.MOB);
   private ObjectModule object = (ObjectModule)StateBase.getModule(ModuleBase.ModuleType.OBJECT);
   private ResourceModule resource = (ResourceModule)StateBase.getModule(ModuleBase.ModuleType.RESOURCE);
   private MobBase thisMob;
   private OrderedPair primaryCoordinates;
   private OrderedPair secondaryCoordinates;
   private ObjectBase primaryObject;
   private int primaryObjectID = -1;
   private String primaryObjectName = "";
   private ObjectBase secondaryObject;
   private int secondaryObjectID = -1;
   private String secondaryObjectName = "";
   private ObjectBase deliveryObject;
   private int deliveryObjectID = -1;
   private String deliveryObjectName = "";
   private MobBase primaryMob;
   private int primaryMobID = -1;
   private String primaryMobName = "";
   private MobBase secondaryMob;
   private int secondaryMobID = -1;
   private String secondaryMobName = "";
   private String regionName = "";
   private String regionFileName = "";
   private ArrayList<ResourceBase> pickupResources = new ArrayList<>();
   private int[] pickupResourceIDs;
   private ResourceModule.ResourceType pickupResourceType;
   private int pickupResourceAmount;
   private MobJobBase.MobJobType primaryMobJobType;
   private Assignment.AssignmentType assignmentType;

   public void setPrimaryCoordinates(OrderedPair o) {
      this.primaryCoordinates = o;
   }

   public void setPrimaryCoordinates(int x, int y) {
      this.primaryCoordinates = OrderedPair.getOrderedPair(x, y);
   }

   public OrderedPair getPrimaryCoordinates() {
      return this.primaryCoordinates;
   }

   public int getPrimaryX() {
      return this.primaryCoordinates.getX();
   }

   public int getPrimaryY() {
      return this.primaryCoordinates.getY();
   }

   public void setSecondaryCoordinates(OrderedPair o) {
      this.secondaryCoordinates = o;
   }

   public void setSecondaryCoordinates(int x, int y) {
      this.secondaryCoordinates = OrderedPair.getOrderedPair(x, y);
   }

   public OrderedPair getSecondaryCoordinates() {
      return this.secondaryCoordinates;
   }

   public int getSecondaryX() {
      return this.secondaryCoordinates.getX();
   }

   public int getSecondaryY() {
      return this.secondaryCoordinates.getY();
   }

   public void setPrimaryObject(ObjectBase o) {
      this.primaryObject = o;
      this.primaryObjectName = o.getFormattedName();
   }

   public void resetPrimaryObject() {
      this.primaryObject = null;
      this.primaryObjectName = "";
   }

   public ObjectBase getPrimaryObject() {
      return this.primaryObject;
   }

   public String getPrimaryObjectName() {
      return this.primaryObjectName;
   }

   public void setSecondaryObject(ObjectBase o) {
      this.secondaryObject = o;
      this.secondaryObjectName = o.getFormattedName();
   }

   public void resetSecondaryObject() {
      this.secondaryObject = null;
      this.secondaryObjectName = null;
   }

   public ObjectBase getSecondaryObject() {
      return this.secondaryObject;
   }

   public String getSecondaryObjectName() {
      return this.secondaryObjectName;
   }

   public void setDeliveryObject(ObjectBase o) {
      this.deliveryObject = o;
      this.deliveryObjectName = o.getFormattedName();
   }

   public void resetDeliveryObject() {
      this.deliveryObject = null;
      this.deliveryObjectName = "";
   }

   public ObjectBase getDeliveryObject() {
      return this.deliveryObject;
   }

   public String getDeliveryObjectName() {
      return this.deliveryObjectName;
   }

   public void setPrimaryMob(MobBase m) {
      this.primaryMob = m;
      this.primaryMobName = m.getFormattedName(true);
   }

   public MobBase getPrimaryMob() {
      return this.primaryMob;
   }

   public String getPrimaryMobName() {
      return this.primaryMobName;
   }

   public void resetPrimaryMob() {
      this.primaryMob = null;
      this.primaryMobName = "";
   }

   public void setSecondaryMob(MobBase m) {
      this.secondaryMob = m;
      this.secondaryMobName = m.getFormattedName(true);
   }

   public MobBase getSecondaryMob() {
      return this.secondaryMob;
   }

   public String getSecondaryMobName() {
      return this.secondaryMobName;
   }

   public void resetSecondaryMob() {
      this.secondaryMob = null;
      this.secondaryMobName = "";
   }

   public void setRegionName(String s) {
      this.regionName = s;
   }

   public String getRegionName() {
      return this.regionName;
   }

   public void resetRegionName() {
      this.regionName = "";
   }

   public void setRegionFileName(String s) {
      this.regionFileName = s;
   }

   public String getRegionFileName() {
      return this.regionFileName;
   }

   public void resetRegionFileName() {
      this.regionFileName = "";
   }

   public void addPickupResources(ResourceBase r) {
      this.pickupResources.add(r);
      r.assign(this.thisMob);
   }

   public void removePickupResources(ResourceBase r) {
      this.pickupResources.remove(r);
      r.unassign();
   }

   public ResourceBase removeAndGetFirstPickupResources() {
      ResourceBase r = this.pickupResources.get(0);
      this.pickupResources.remove(0);
      r.unassign();
      return r;
   }

   public void resetPickupResources() {
      for (ResourceBase r : this.pickupResources) {
         r.unassign();
      }

      this.pickupResources.clear();
   }

   public boolean containsPickupResources(ResourceBase r) {
      return this.pickupResources.contains(r);
   }

   public ResourceBase getFirstPickupResources() {
      return this.pickupResources.get(0);
   }

   public ArrayList<ResourceBase> getPickupResources() {
      return this.pickupResources;
   }

   public void setPickupResourceType(ResourceModule.ResourceType t) {
      this.pickupResourceType = t;
   }

   public ResourceModule.ResourceType getPickupResourceType() {
      return this.pickupResourceType;
   }

   public void setPickupResourceAmount(int i) {
      this.pickupResourceAmount = i;
   }

   public int getPickupResourceAmount() {
      return this.pickupResourceAmount;
   }

   public void setMobJobType(MobJobBase.MobJobType t) {
      this.primaryMobJobType = t;
   }

   public MobJobBase.MobJobType getMobJobType() {
      return this.primaryMobJobType;
   }

   public void setAssignmentType(Assignment.AssignmentType a) {
      this.assignmentType = a;
   }

   public Assignment.AssignmentType getAssignmentType() {
      return this.assignmentType;
   }

   public Assignment(MobBase m) {
      this.thisMob = m;
   }

   public void load(YMLDataMap dataMap) {
      if (dataMap.containsKey("primaryCoordinates")) {
         this.primaryCoordinates = dataMap.unpackCoordinates("primaryCoordinates");
      }

      if (dataMap.containsKey("secondaryCoordinates")) {
         this.secondaryCoordinates = dataMap.unpackCoordinates("secondaryCoordinates");
      }

      this.primaryObjectID = Integer.parseInt(dataMap.getOrDefault("primaryObjectID", -1));
      this.primaryObjectName = dataMap.getOrDefault("primaryObjectName", "");
      this.secondaryObjectID = Integer.parseInt(dataMap.getOrDefault("secondaryObjectID", -1));
      this.secondaryObjectName = dataMap.getOrDefault("secondaryObjectName", "");
      this.deliveryObjectID = Integer.parseInt(dataMap.getOrDefault("deliveryObjectID", -1));
      this.deliveryObjectName = dataMap.getOrDefault("deliveryObjectName", "");
      this.primaryMobID = Integer.parseInt(dataMap.getOrDefault("primaryMobID", -1));
      this.primaryMobName = dataMap.getOrDefault("primaryMobName", "");
      this.secondaryMobID = Integer.parseInt(dataMap.getOrDefault("secondaryMobID", -1));
      this.secondaryMobName = dataMap.getOrDefault("secondaryMobName", "");
      this.regionName = dataMap.getOrDefault("regionName", "");
      this.regionFileName = dataMap.getOrDefault("regionFileName", "");
      if (dataMap.containsKey("pickupResourceIDs")) {
         this.pickupResourceIDs = dataMap.unpackObjectArrayIDs("pickupResourceIDs");
      }

      String pickupResourceTypeString = dataMap.getOrDefault("pickupResourceType", "null");
      if (pickupResourceTypeString.equals("RAW_FOOD")) {
         Console.saveConverterOut(this, "InDev 33f", "InDev 34 Unstable 2", "Converting RAW_FOOD to RAW_VEGETABLE");
         pickupResourceTypeString = "RAW_VEGETABLE";
      }

      if (pickupResourceTypeString.equals("WOOD_SHIELD")) {
         Console.saveConverterOut(this, "InDev 33f", "InDev 34 Unstable 2", "Converting WOOD_SHIELD into LEATHER_SHIELD.");
         pickupResourceTypeString = "LEATHER_SHIELD";
      }

      ResourceModule.ResourceType[] var6;
      for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
         if (t.toString().equals(pickupResourceTypeString)) {
            this.pickupResourceType = t;
            break;
         }
      }

      this.pickupResourceAmount = Integer.parseInt(dataMap.getOrDefault("pickupResourceAmount", 0));
      String primaryMobJobTypeString = dataMap.getOrDefault("primaryMobJobType", "null");

      MobJobBase.MobJobType[] var7;
      for (MobJobBase.MobJobType tx : MobJobBase.MobJobType.values()) {
         if (tx.toString().equals(primaryMobJobTypeString)) {
            this.primaryMobJobType = tx;
            break;
         }
      }

      String assignmentTypeString = dataMap.getOrDefault("assignmentType", "null");
      if (assignmentTypeString.contains("GUARD")) {
         Console.saveConverterOut(this, "InDev 33f", "InDev 34 Unstable 2", "Converting Guard assignment type to Ranger");
         assignmentTypeString = assignmentTypeString.replace("GUARD", "RANGER");
      }

      Assignment.AssignmentType[] var8;
      for (Assignment.AssignmentType aT : Assignment.AssignmentType.values()) {
         if (aT.toString().equals(assignmentTypeString)) {
            this.assignmentType = aT;
            break;
         }
      }
   }

   public void reset() {
      this.primaryCoordinates = null;
      this.secondaryCoordinates = null;
      this.primaryObject = null;
      this.primaryObjectName = "";
      this.secondaryObject = null;
      this.secondaryObjectName = "";
      this.deliveryObject = null;
      this.deliveryObjectName = "";
      this.primaryMob = null;
      this.primaryMobName = "";
      this.secondaryMob = null;
      this.secondaryMobName = "";
      this.regionName = "";
      this.regionFileName = "";

      for (ResourceBase r : this.pickupResources) {
         if (!this.thisMob.getCarriedResourcesList().contains(r)) {
            r.unassign();
         }
      }

      this.pickupResources.clear();
      this.pickupResourceType = null;
      this.pickupResourceAmount = 0;
      this.primaryMobJobType = null;
      this.mob.removeWorkAssigned(this.thisMob);
      this.assignmentType = null;
   }

   public void loadSync() {
      if (this.primaryObjectID != -1) {
         ObjectBase o = this.object.getObjects().get(this.primaryObjectID);
         this.primaryObject = o;
      }

      if (this.secondaryObjectID != -1) {
         ObjectBase o = this.object.getObjects().get(this.secondaryObjectID);
         this.secondaryObject = o;
      }

      if (this.deliveryObjectID != -1) {
         ObjectBase o = this.object.getObjects().get(this.deliveryObjectID);
         this.deliveryObject = o;
      }

      if (this.primaryMobID != -1) {
         MobBase m = this.mob.getMobHashMap().get(this.primaryMobID);
         this.primaryMob = m;
      }

      if (this.secondaryMobID != -1) {
         MobBase m = this.mob.getMobHashMap().get(this.secondaryMobID);
         this.secondaryMob = m;
      }

      if (this.pickupResourceIDs != null) {
         for (int i : this.pickupResourceIDs) {
            ResourceBase r = this.resource.getResource(i);
            if (r != null) {
               this.pickupResources.add(r);
            }
         }

         this.pickupResourceIDs = null;
      }
   }

   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      if (this.primaryCoordinates != null) {
         dataMap.put("primaryCoordinates", dataMap.packCoordinates(this.primaryCoordinates));
      }

      if (this.secondaryCoordinates != null) {
         dataMap.put("secondaryCoordinates", dataMap.packCoordinates(this.secondaryCoordinates));
      }

      if (this.primaryObject != null) {
         dataMap.put("primaryObjectID", Integer.toString(this.primaryObject.getID()));
      }

      if (!this.primaryObjectName.equals("")) {
         dataMap.put("primaryObjectName", this.primaryObjectName);
      }

      if (this.secondaryObject != null) {
         dataMap.put("secondaryObjectID", Integer.toString(this.secondaryObject.getID()));
      }

      if (!this.secondaryObjectName.equals("")) {
         dataMap.put("secondaryObjectName", this.secondaryObjectName);
      }

      if (this.deliveryObject != null) {
         dataMap.put("deliveryObjectID", Integer.toString(this.deliveryObject.getID()));
      }

      if (!this.deliveryObjectName.equals("")) {
         dataMap.put("deliveryObjectName", this.deliveryObjectName);
      }

      if (this.primaryMob != null) {
         dataMap.put("primaryMobID", Integer.toString(this.primaryMob.getID()));
      }

      if (!this.primaryMobName.equals("")) {
         dataMap.put("primaryMobName", this.primaryMobName);
      }

      if (this.secondaryMob != null) {
         dataMap.put("secondaryMobID", Integer.toString(this.secondaryMob.getID()));
      }

      if (!this.secondaryMobName.equals("")) {
         dataMap.put("secondaryMobName", this.secondaryMobName);
      }

      if (!this.regionName.equals("")) {
         dataMap.put("regionName", this.regionName);
      }

      if (!this.regionFileName.equals("")) {
         dataMap.put("regionFileName", this.regionFileName);
      }

      if (this.pickupResources.size() != 0) {
         dataMap.put("pickupResourceIDs", dataMap.packObjectArrayIDs(this.pickupResources));
      }

      if (this.pickupResourceType != null) {
         dataMap.put("pickupResourceType", this.pickupResourceType.toString());
      }

      if (this.pickupResourceAmount != 0) {
         dataMap.put("pickupResourceAmount", this.pickupResourceAmount);
      }

      if (this.primaryMobJobType != null) {
         dataMap.put("primaryMobJobType", this.primaryMobJobType.toString());
      }

      if (this.assignmentType != null) {
         dataMap.put("assignmentType", this.assignmentType.toString());
      }

      return dataMap;
   }

   public void finalizeAssignment() {
      this.mob.addWorkAssigned(this.thisMob);
   }

   public static enum AssignmentType {
      HARVESTING,
      CLEARING,
      DELIVERY,
      BUILDING,
      REPAIRING,
      REFINE_RESOURCE,
      REFINE_WATER,
      ATTACK_BUILDING,
      PLANTING,
      ENTITY,
      PICK_UP_RESOURCE_FROM_GROUND,
      PICK_UP_RESOURCE_FROM_BUILDING,
      DISMANTLING,
      BUILDING_ROAD,
      REPAIRING_ROAD,
      FINDING_HOME,
      FINDING_SPAWN,
      GET_JOB,
      QUIT_JOB,
      EQUIP_RESOURCE,
      DRINKING_FROM_FROUNTAIN,
      TENDING_FARM,
      DIGGING,
      PATROL,
      RANGER_POSTED,
      MEDIC_WORK,
      TRADING,
      COMBAT,
      CLEAROUT_RESOURCE_FROM_BUILDING,
      CATJEET_SELLING_WARES,
      SELL_RESOURCE,
      BUY_RESOURCE,
      CATJEET_LEAVING_REGION,
      CATJEET_SELLING_LABOR,
      BUY_LABOR,
      IN_HOME,
      PRAYING,
      CORRUPTION_HARVESTING,
      SEEKING_MEDIC,
      MIGRATION_LEAVING_REGION,
      VILLAGER_BECOMING_MIGRANTS,
      COURIER_PARKED,
      COURIER_SUPPLIER_DELIVERING,
      COURIER_DEPARTING,
      COURIER_ARRIVING,
      PROCESS_RESOURCE,
      MAKE_TRASHY_CUBE,
      CAPTURING_ANIMAL,
      SLAUGHTERING_ANIMAL,
      BREEDING,
      TERMINATE_ANIMAL_PREGNANCY,
      CAPTURING_SOUL,
      PLACING_VESSEL;
   }
}
