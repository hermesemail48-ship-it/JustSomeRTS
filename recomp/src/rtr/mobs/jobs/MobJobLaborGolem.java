package rtr.mobs.jobs;

import rtr.resources.ResourceModule;

public class MobJobLaborGolem extends MobJobBase {
   public MobJobLaborGolem() {
      this.notCounted = true;
      this.harvestSpeedBase = 500;

      ResourceModule.ResourceType[] var4;
      for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
         if (t != ResourceModule.ResourceType.CRYSTALLY_TRASH
            && t != ResourceModule.ResourceType.ORGANICY_TRASH
            && t != ResourceModule.ResourceType.ROCKY_TRASH
            && t != ResourceModule.ResourceType.SUSPICICIOUSY_TRASH
            && t != ResourceModule.ResourceType.WOODY_TRASH
            && t != ResourceModule.ResourceType.TRASHY_TRASH) {
            this.canWorkerSortEvenly.add(t);
         }
      }

      this.canWorkerHarvest.add(ResourceModule.ResourceType.WOOD);
      this.canWorkerHarvest.add(ResourceModule.ResourceType.ROCK);

      for (ResourceModule.ResourceType tx : ResourceModule.ResourceType.values()) {
         if (tx != ResourceModule.ResourceType.CRYSTALLY_TRASH
            && tx != ResourceModule.ResourceType.ORGANICY_TRASH
            && tx != ResourceModule.ResourceType.ROCKY_TRASH
            && tx != ResourceModule.ResourceType.SUSPICICIOUSY_TRASH
            && tx != ResourceModule.ResourceType.WOODY_TRASH
            && tx != ResourceModule.ResourceType.TRASHY_TRASH) {
            this.canWorkerDeliverConstruction.add(tx);
            this.canWorkerDeliverCourier.add(tx);
            this.canWorkerStore.add(tx);
         }
      }

      this.canWorkerDeliverRoadConstruction.add(ResourceModule.ResourceType.WOOD);
      this.canWorkerDeliverRoadConstruction.add(ResourceModule.ResourceType.BOARD);
      this.canWorkerDeliverRoadConstruction.add(ResourceModule.ResourceType.ROCK);
      this.canWorkerDeliverRoadConstruction.add(ResourceModule.ResourceType.CUT_STONE);
      this.canWorkerDeliverCourierOnlyFull.add(ResourceModule.ResourceType.WATER_BUCKET);
      this.canWorkerDeliverCourierOnlyFull.add(ResourceModule.ResourceType.DIRTY_WATER_BUCKET);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.CRYSTALLY_TRASH);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.ORGANICY_TRASH);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.ROCKY_TRASH);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.WOODY_TRASH);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.TRASHY_TRASH);
   }
}
