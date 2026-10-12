package rtr.mobs.jobs;

import rtr.resources.ResourceModule;

public class MobJobCubeEGolem extends MobJobBase {
   public MobJobCubeEGolem() {
      this.notCounted = true;
      this.canDismantle = true;
      this.canMakeTrashyCubes = true;
      this.buildingSpeedBase = 300;
      this.dismantlingSpeedBase = 200;
      this.processingTrashSpeedBase = 300;
      this.canWorkerSortPriority.add(ResourceModule.ResourceType.TRASHY_CUBE);
      this.canWorkerStore.add(ResourceModule.ResourceType.TRASHY_CUBE);
      this.canWorkerDeliverConstruction.add(ResourceModule.ResourceType.TRASHY_CUBE);
      this.canWorkerBuild.add(ResourceModule.ResourceType.TRASHY_CUBE);
   }
}
