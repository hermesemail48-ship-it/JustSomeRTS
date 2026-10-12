package rtr.mobs.jobs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.console.Console;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.ObjectModule;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.utilities.Assignment;
import rtr.utilities.Utilities;

public class MobJobModule extends ModuleBase {
   private EnumMap<MobJobBase.MobJobType, Integer> desiredWorkers = new EnumMap<>(MobJobBase.MobJobType.class);
   private EnumMap<MobJobBase.MobJobType, Integer> currentWorkers = new EnumMap<>(MobJobBase.MobJobType.class);
   private EnumMap<MobJobBase.MobJobType, Integer> maximumWorkers = new EnumMap<>(MobJobBase.MobJobType.class);
   private EnumMap<MobJobBase.MobJobType, Color> customBaseColor = new EnumMap<>(MobJobBase.MobJobType.class);
   private EnumMap<MobJobBase.MobJobType, Color> customAccentColor = new EnumMap<>(MobJobBase.MobJobType.class);
   private int totalCurrentWorkers;
   private int totalCurrentWorkersPlusEnroute;
   private int totalDesiredWorkers;
   private ArrayList<MobJobBase.MobJobType> jobDemand = new ArrayList<>();
   private int unassignedVillagers = 0;

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      this.initLoad();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
      this.desiredWorkers.clear();
      this.currentWorkers.clear();
      this.maximumWorkers.clear();
      this.customBaseColor.clear();
      this.customAccentColor.clear();
   }

   @Override
   protected void newMainMenu() throws SlickException {
   }

   @Override
   protected void newPlay() throws SlickException {
      MobJobBase.MobJobType[] var4;
      for (MobJobBase.MobJobType t : MobJobBase.MobJobType.values()) {
         this.desiredWorkers.put(t, 0);
         this.currentWorkers.put(t, 0);
         this.maximumWorkers.put(t, 0);
         this.customBaseColor.put(t, null);
         this.customAccentColor.put(t, null);
      }

      for (MobBase m : this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false)) {
         this.currentWorkers.put(m.getMobJobType(), this.currentWorkers.get(m.getMobJobType()) + 1);
      }

      this.calculateTotalWorkers();
      this.calculateJobDemand();
   }

   @Override
   protected void loadPlay() throws SlickException {
      this.loadRegionalSaveData();
   }

   @Override
   protected void newMapEditor() throws SlickException {
   }

   @Override
   protected void loadMapEditor() throws SlickException {
   }

   @Override
   protected void newWorldMap() throws SlickException {
   }

   @Override
   protected void loadWorldMap() throws SlickException {
   }

   @Override
   protected void loadRegionalSaveData() throws SlickException {
      MobJobBase.MobJobType[] var4;
      for (MobJobBase.MobJobType t : MobJobBase.MobJobType.values()) {
         this.desiredWorkers.put(t, 0);
         this.currentWorkers.put(t, 0);
         this.maximumWorkers.put(t, 0);
         this.customBaseColor.put(t, null);
         this.customAccentColor.put(t, null);
      }

      YMLDataMap dataMap = this.save.getActiveRegionalSave().getAndUnloadModuleSaveData(this.moduleType);

      MobJobBase.MobJobType[] var5;
      for (MobJobBase.MobJobType t : MobJobBase.MobJobType.values()) {
         if (dataMap.containsMap(t.toString())) {
            YMLDataMap thisMap = dataMap.getSubMap(t.toString());
            if (thisMap != null) {
               this.desiredWorkers.put(t, Integer.parseInt(thisMap.getOrDefault("desiredWorkers", 0)));
               this.currentWorkers.put(t, Integer.parseInt(thisMap.getOrDefault("currentWorkers", 0)));
               this.maximumWorkers.put(t, Integer.parseInt(thisMap.getOrDefault("maximumWorkers", 0)));
               if (thisMap.containsKey("customBaseColor")) {
                  this.customBaseColor.put(t, thisMap.unpackColor("customBaseColor"));
               }

               if (thisMap.containsKey("customAccentColor")) {
                  this.customAccentColor.put(t, thisMap.unpackColor("customAccentColor"));
               }
            }
         }
      }

      if (dataMap.containsMap("GUARD")) {
         Console.saveConverterOut(this, "InDev 33f", "Release 1b", "Old GUARD data found, converting to RANGER.");
         YMLDataMap thisMap = dataMap.getSubMap("GUARD");
         if (thisMap != null) {
            this.desiredWorkers.put(MobJobBase.MobJobType.RANGER, Integer.parseInt(thisMap.getOrDefault("desiredWorkers", 0)));
            this.currentWorkers.put(MobJobBase.MobJobType.RANGER, Integer.parseInt(thisMap.getOrDefault("currentWorkers", 0)));
            this.maximumWorkers.put(MobJobBase.MobJobType.RANGER, Integer.parseInt(thisMap.getOrDefault("maximumWorkers", 0)));
         }
      }
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
   }

   @Override
   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("version", Game.getVersion());

      MobJobBase.MobJobType[] var5;
      for (MobJobBase.MobJobType t : MobJobBase.MobJobType.values()) {
         Map<String, Object> thisMap = new HashMap<>();
         boolean useData = false;
         if (this.desiredWorkers.get(t) != 0) {
            thisMap.put("desiredWorkers", this.desiredWorkers.get(t));
            useData = true;
         }

         if (this.currentWorkers.get(t) != 0) {
            thisMap.put("currentWorkers", this.currentWorkers.get(t));
            useData = true;
         }

         if (this.maximumWorkers.get(t) != 0) {
            thisMap.put("maximumWorkers", this.maximumWorkers.get(t));
            useData = true;
         }

         if (this.customBaseColor.get(t) != null) {
            thisMap.put("customBaseColor", dataMap.packColor(this.customBaseColor.get(t)));
            useData = true;
         }

         if (this.customAccentColor.get(t) != null) {
            thisMap.put("customAccentColor", dataMap.packColor(this.customAccentColor.get(t)));
            useData = true;
         }

         if (useData) {
            dataMap.put(t.toString(), thisMap);
         }
      }

      return dataMap;
   }

   public void update() throws SlickException {
      ArrayList<ObjectBase> objects = this.object.getObjectsBySortGroup(ObjectModule.ObjectSortGroup.PROVIDES_JOB_SLOTS);

      MobJobBase.MobJobType[] var5;
      for (MobJobBase.MobJobType t : MobJobBase.MobJobType.values()) {
         this.maximumWorkers.put(t, 0);
      }

      for (ObjectBase o : objects) {
         ObjectFlags objectFlags = o.getObjectFlags();
         if (o.isInRange() && objectFlags.getWorkerJobType() != MobJobBase.MobJobType.NONE) {
            this.maximumWorkers.put(objectFlags.getWorkerJobType(), this.maximumWorkers.get(objectFlags.getWorkerJobType()) + objectFlags.getWorkersProvided());
         }
      }

      for (MobJobBase.MobJobType t : MobJobBase.MobJobType.values()) {
         if (this.desiredWorkers.get(t) > this.maximumWorkers.get(t)) {
            this.desiredWorkers.put(t, this.maximumWorkers.get(t));
         }
      }

      this.unassignedVillagers = 0;

      for (MobBase m : this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false)) {
         if (m.getMobFlags().getAllowWork()
            && (
               m.getMobJobType() == MobJobBase.MobJobType.NONE && m.getAssignment().getAssignmentType() != Assignment.AssignmentType.GET_JOB
                  || m.getAssignment().getAssignmentType() == Assignment.AssignmentType.QUIT_JOB
            )) {
            this.unassignedVillagers++;
         }
      }

      this.calculateTotalWorkers();
      this.calculateJobDemand();
   }

   public void calculateTotalWorkers() {
      ArrayList<MobBase> villagers = this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false);
      int totalCurrent = 0;
      int totalDesired = 0;

      MobJobBase.MobJobType[] var7;
      for (MobJobBase.MobJobType t : MobJobBase.MobJobType.values()) {
         if (!t.getJob().isNotCounted()) {
            totalCurrent += this.currentWorkers.get(t);
            totalDesired += this.desiredWorkers.get(t);
         }
      }

      this.totalCurrentWorkers = totalCurrent;
      this.totalDesiredWorkers = totalDesired;
      int hiring = 0;
      int quitting = 0;

      for (MobBase m : villagers) {
         if (m.getAssignment().getAssignmentType() == Assignment.AssignmentType.GET_JOB) {
            hiring++;
         } else if (m.getAssignment().getAssignmentType() == Assignment.AssignmentType.QUIT_JOB) {
            quitting++;
         }
      }

      totalCurrent += hiring - quitting;
      this.totalCurrentWorkersPlusEnroute = totalCurrent;
   }

   public int getTotalCurrentWorkers() {
      return this.totalCurrentWorkers;
   }

   public int getTotalCurrentWorkers(boolean includeEnroute) {
      return this.totalCurrentWorkersPlusEnroute;
   }

   public int getTotalDesiredWorkers() {
      return this.totalDesiredWorkers;
   }

   public void calculateJobDemand() {
      this.jobDemand.clear();

      MobJobBase.MobJobType[] var4;
      for (MobJobBase.MobJobType t : MobJobBase.MobJobType.values()) {
         if (this.desiredWorkers.get(t) > 0 && this.getCurrentWorkers(t, true) < this.desiredWorkers.get(t)) {
            this.jobDemand.add(t);
         }
      }

      Collections.sort(this.jobDemand, new Comparator<MobJobBase.MobJobType>() {
         public int compare(MobJobBase.MobJobType m1, MobJobBase.MobJobType m2) {
            return m1 == m2 ? 0 : (int)MobJobModule.this.getJobFillPercentage(m1) - (int)MobJobModule.this.getJobFillPercentage(m2);
         }
      });
   }

   private float getJobFillPercentage(MobJobBase.MobJobType t) {
      float percent = 0.0F;
      int currentWorkers = this.getCurrentWorkers(t, true);
      if (currentWorkers < this.desiredWorkers.get(t)) {
         percent = currentWorkers * 100.0F / this.desiredWorkers.get(t).intValue();
      }

      return percent;
   }

   public int getDesiredWorkers(MobJobBase.MobJobType t) {
      return this.desiredWorkers.get(t);
   }

   public int getCurrentWorkers(MobJobBase.MobJobType t) {
      return this.currentWorkers.get(t);
   }

   public int getCurrentWorkers(MobJobBase.MobJobType t, boolean includeEnroute) {
      ArrayList<MobBase> villagers = this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false);
      int hiring = 0;
      int quitting = 0;

      for (MobBase m : villagers) {
         if (m.getMobJobType() == t || m.getAssignment().getMobJobType() == t) {
            if (m.getAssignment().getAssignmentType() == Assignment.AssignmentType.GET_JOB) {
               hiring++;
            }

            if (m.getAssignment().getAssignmentType() == Assignment.AssignmentType.QUIT_JOB) {
               quitting++;
            }
         }
      }

      return this.currentWorkers.get(t) + hiring - quitting;
   }

   public int getMaximumWorkers(MobJobBase.MobJobType t) {
      return this.maximumWorkers.get(t);
   }

   public int getTotalMaximumWorkers() {
      int totalMaximum = 0;

      MobJobBase.MobJobType[] var5;
      for (MobJobBase.MobJobType t : MobJobBase.MobJobType.values()) {
         if (!t.getJob().isNotCounted()) {
            totalMaximum += this.maximumWorkers.get(t);
         }
      }

      return totalMaximum;
   }

   public void increaseDesiredWorkers(MobJobBase.MobJobType t, int i) {
      this.desiredWorkers.put(t, this.desiredWorkers.get(t) + i);
      if (this.desiredWorkers.get(t) > this.maximumWorkers.get(t)) {
         this.desiredWorkers.put(t, this.maximumWorkers.get(t));
      }
   }

   public void decreaseDesiredWorkers(MobJobBase.MobJobType t, int i) {
      this.desiredWorkers.put(t, this.desiredWorkers.get(t) - i);
      if (this.desiredWorkers.get(t) < 0) {
         this.desiredWorkers.put(t, 0);
      }
   }

   public void increaseCurrentWorkers(MobJobBase.MobJobType t, int i) {
      this.currentWorkers.put(t, this.currentWorkers.get(t) + i);
   }

   public void decreaseCurrentWorkers(MobJobBase.MobJobType t, int i) {
      this.currentWorkers.put(t, this.currentWorkers.get(t) - i);
   }

   public MobJobBase.MobJobType getHighestDemandJobType() {
      return this.jobDemand.size() == 0 ? MobJobBase.MobJobType.NONE : this.jobDemand.get(0);
   }

   public MobJobBase.MobJobType getLowestDemandJobType() {
      return this.jobDemand.size() == 0 ? MobJobBase.MobJobType.NONE : this.jobDemand.get(this.jobDemand.size() - 1);
   }

   public int getUnassignedVillagers() {
      return this.unassignedVillagers;
   }

   public int getCalculatedWorkTime(MobBase m) {
      Assignment.AssignmentType t = m.getAssignment().getAssignmentType();
      ResourceModule.ResourceType r = m.getAssignment().getPickupResourceType();
      MobJobBase j = m.getMobJobType().getJob();
      int speedBase = 10000;
      if (t == Assignment.AssignmentType.HARVESTING || t == Assignment.AssignmentType.TENDING_FARM) {
         speedBase = j.getHarvestSpeedBase();
      } else if (t == Assignment.AssignmentType.BUILDING || t == Assignment.AssignmentType.BUILDING_ROAD) {
         speedBase = j.getBuildingSpeedBase();
      } else if (t == Assignment.AssignmentType.REFINE_RESOURCE || t == Assignment.AssignmentType.REFINE_WATER) {
         speedBase = j.getRefineSpeedBase();
      } else if (t == Assignment.AssignmentType.PROCESS_RESOURCE || t == Assignment.AssignmentType.MAKE_TRASHY_CUBE) {
         speedBase = j.getProcessingTrashSpeedBase();
      } else if (t == Assignment.AssignmentType.DISMANTLING) {
         speedBase = j.getDismantlingSpeedBase();
      } else if (t == Assignment.AssignmentType.CLEARING) {
         speedBase = j.getClearingSpeedBase();
      } else if (t == Assignment.AssignmentType.DIGGING) {
         speedBase = j.getDiggingSpeedBase();
      } else if (t == Assignment.AssignmentType.REPAIRING || t == Assignment.AssignmentType.REPAIRING_ROAD) {
         speedBase = j.getRepairSpeedBase();
      } else if (t == Assignment.AssignmentType.MEDIC_WORK) {
         speedBase = j.getMedicSpeedBase();
      } else if (t == Assignment.AssignmentType.PRAYING) {
         speedBase = j.getPrayingSpeedBase();
      } else if (t == Assignment.AssignmentType.CAPTURING_SOUL) {
         speedBase = j.getCapturingSoulSpeedBase();
      } else if (t == Assignment.AssignmentType.TERMINATE_ANIMAL_PREGNANCY) {
         speedBase = j.getTendAnimalsBase();
      }

      double percentReduction = this.object.getGlobalWorkSpeedReduction();
      if (t == Assignment.AssignmentType.HARVESTING) {
         percentReduction += this.object.getHarvestingSpeedReduction(r);
      } else if (t == Assignment.AssignmentType.REFINE_RESOURCE || t == Assignment.AssignmentType.REFINE_WATER) {
         percentReduction += this.object.getRefiningSpeedReduction(r);
      } else if (t == Assignment.AssignmentType.PROCESS_RESOURCE || t == Assignment.AssignmentType.MAKE_TRASHY_CUBE) {
         percentReduction += this.object.getProcessingTrashSpeedReduction();
      } else if (t == Assignment.AssignmentType.BUILDING) {
         percentReduction += this.object.getBuildingSpeedReduction();
      } else if (t == Assignment.AssignmentType.BUILDING_ROAD) {
         percentReduction += this.object.getBuildingRoadSpeedReduction();
      } else if (t == Assignment.AssignmentType.DISMANTLING) {
         percentReduction += this.object.getDismantlingSpeedReduction();
      } else if (t == Assignment.AssignmentType.CLEARING) {
         percentReduction += this.object.getClearingSpeedReduction();
      } else if (t == Assignment.AssignmentType.REPAIRING) {
         percentReduction += this.object.getRepairingSpeedReduction();
      } else if (t == Assignment.AssignmentType.REPAIRING_ROAD) {
         percentReduction += this.object.getRepairingRoadSpeedReduction();
      } else if (t == Assignment.AssignmentType.TENDING_FARM) {
         percentReduction += this.object.getFarmingSpeedReduction();
      } else if (t == Assignment.AssignmentType.MEDIC_WORK) {
         percentReduction += this.object.getMedicSpeedReduction();
      } else if (t == Assignment.AssignmentType.PRAYING) {
         percentReduction += this.object.getPrayingSpeedReduction();
      } else if (t == Assignment.AssignmentType.CAPTURING_SOUL) {
         percentReduction += this.object.getCapturingSoulsSpeedReduction();
      } else if (t == Assignment.AssignmentType.TERMINATE_ANIMAL_PREGNANCY) {
         percentReduction += this.object.getTendAnimalsSpeedReduction();
      }

      percentReduction += m.getWorkSpeedReductionBase();
      percentReduction -= 100.0F - this.weather.getMovementSpeedReductionTotal() * 100.0F;
      if (m.isPregnant()) {
         percentReduction -= 20.0;
      }

      if (this.hasPreferredTools(m)) {
         percentReduction += 50.0;
      }

      if (percentReduction > 90.0) {
         percentReduction = 90.0;
      } else if (percentReduction < -90.0) {
         percentReduction = -90.0;
      }

      return (int)(speedBase - speedBase * (percentReduction / 100.0));
   }

   private boolean hasPreferredTools(MobBase m) {
      if (m.getInventoryToolSlot() == null) {
         return false;
      } else {
         ArrayList<ResourceModule.ResourceType> preferredTools = m.getMobJobType().getJob().getPreferredTools();
         return preferredTools.contains(m.getInventoryToolSlot().getType());
      }
   }

   public void setCustomBaseColor(MobJobBase.MobJobType j, Color c) {
      this.customBaseColor.put(j, c);
      this.mob.refreshMobJobColors(j);
   }

   public void setCustomAccentColor(MobJobBase.MobJobType j, Color c) {
      this.customAccentColor.put(j, c);
      this.mob.refreshMobJobColors(j);
   }

   public void resetCustomBaseColor(MobJobBase.MobJobType j) {
      this.customBaseColor.put(j, null);
      this.mob.refreshMobJobColors(j);
   }

   public void resetCustomAccentColor(MobJobBase.MobJobType j) {
      this.customAccentColor.put(j, null);
      this.mob.refreshMobJobColors(j);
   }

   public void adjustBaseColor(MobJobBase.MobJobType j, int red, int green, int blue, int alpha) {
      Color originalColor = this.getCustomBaseColor(j);
      Color newColor = new Color(originalColor);
      int finalColorRed = Utilities.setAndKeepWithinBounds(0, 255, newColor.getRed() + red);
      int finalColorGreen = Utilities.setAndKeepWithinBounds(0, 255, newColor.getGreen() + green);
      int finalColorBlue = Utilities.setAndKeepWithinBounds(0, 255, newColor.getBlue() + blue);
      int finalColorAlpha = Utilities.setAndKeepWithinBounds(0, 255, newColor.getAlpha() + alpha);
      newColor.setColor(finalColorRed, finalColorGreen, finalColorBlue, finalColorAlpha);
      this.customBaseColor.put(j, newColor);
      this.mob.refreshMobJobColors(j);
   }

   public void adjustAccentColor(MobJobBase.MobJobType j, int red, int green, int blue, int alpha) {
      Color originalColor = this.getCustomAccentColor(j);
      Color newColor = new Color(originalColor);
      int finalColorRed = Utilities.setAndKeepWithinBounds(0, 255, newColor.getRed() + red);
      int finalColorGreen = Utilities.setAndKeepWithinBounds(0, 255, newColor.getGreen() + green);
      int finalColorBlue = Utilities.setAndKeepWithinBounds(0, 255, newColor.getBlue() + blue);
      int finalColorAlpha = Utilities.setAndKeepWithinBounds(0, 255, newColor.getAlpha() + alpha);
      newColor.setColor(finalColorRed, finalColorGreen, finalColorBlue, finalColorAlpha);
      this.customAccentColor.put(j, newColor);
      this.mob.refreshMobJobColors(j);
   }

   public Color getCustomBaseColor(MobJobBase.MobJobType j) {
      if (this.customBaseColor.get(j) != null) {
         return this.customBaseColor.get(j);
      } else {
         return j.getJob().getBaseColor() != null ? j.getJob().getBaseColor() : new Color(-1);
      }
   }

   public Color getCustomAccentColor(MobJobBase.MobJobType j) {
      if (this.customAccentColor.get(j) != null) {
         return this.customAccentColor.get(j);
      } else {
         return j.getJob().getAccentColor() != null ? j.getJob().getAccentColor() : new Color(-1);
      }
   }
}
