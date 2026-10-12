package rtr.mobs.jobs;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.resources.ResourceModule;

public class MobJobOrganizer extends MobJobBase {
   public MobJobOrganizer() {
      this.mobJobName = Text.getText("mobJob.organizer.single");
      this.mobJobNamePlural = Text.getText("mobJob.organizer.plural");
      this.description = Text.getText("mobJob.organizer.description");
      this.baseColor = new Color(-5618518);
      this.accentColor = new Color(-8947849);

      ResourceModule.ResourceType[] var4;
      for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
         this.canWorkerSortEvenly.add(t);
         if (t != ResourceModule.ResourceType.CRYSTALLY_TRASH
            && t != ResourceModule.ResourceType.ORGANICY_TRASH
            && t != ResourceModule.ResourceType.ROCKY_TRASH
            && t != ResourceModule.ResourceType.SUSPICICIOUSY_TRASH
            && t != ResourceModule.ResourceType.WOODY_TRASH
            && t != ResourceModule.ResourceType.TRASHY_TRASH) {
            this.canWorkerDeliverConstruction.add(t);
            this.canWorkerDeliverCourier.add(t);
            this.canWorkerStore.add(t);
            this.canWorkerRedistribute.add(t);
         }
      }

      this.canWorkerDeliverRoadConstruction.add(ResourceModule.ResourceType.WOOD);
      this.canWorkerDeliverRoadConstruction.add(ResourceModule.ResourceType.BOARD);
      this.canWorkerDeliverRoadConstruction.add(ResourceModule.ResourceType.ROCK);
      this.canWorkerDeliverRoadConstruction.add(ResourceModule.ResourceType.CUT_STONE);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.CRYSTALLY_TRASH);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.ORGANICY_TRASH);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.ROCKY_TRASH);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.WOODY_TRASH);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.TRASHY_TRASH);
      this.canWorkerRedistributeOnlyFull.add(ResourceModule.ResourceType.WATER_BUCKET);
      this.canWorkerRedistributeOnlyFull.add(ResourceModule.ResourceType.DIRTY_WATER_BUCKET);
   }
}
