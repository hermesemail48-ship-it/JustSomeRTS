package rtr.mobs.jobs;

import rtr.resources.ResourceModule;

public class MobJobDoggo extends MobJobBase {
   public MobJobDoggo() {
      this.notCounted = true;
      this.canPray = true;
      this.harvestSpeedBase = 800;

      ResourceModule.ResourceType[] var4;
      for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
         this.canWorkerSortEvenly.add(t);
      }

      this.canWorkerHarvest.add(ResourceModule.ResourceType.RAW_VEGETABLE);

      for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
         this.canWorkerStore.add(t);
      }
   }
}
