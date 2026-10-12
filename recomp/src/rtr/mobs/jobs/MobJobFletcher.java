package rtr.mobs.jobs;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.resources.ResourceModule;

public class MobJobFletcher extends MobJobBase {
   public MobJobFletcher() {
      this.mobJobName = Text.getText("mobJob.fletcher.single");
      this.mobJobNamePlural = Text.getText("mobJob.fletcher.plural");
      this.description = Text.getText("mobJob.fletcher.description");
      this.baseColor = new Color(-10804993);
      this.accentColor = new Color(-16733696);
      this.refineSpeedBase = 600;
      this.preferredTools.add(ResourceModule.ResourceType.AXE);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.WOOD);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.FEATHER);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.SILK);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.BALLISTAE_BOLTS);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.BOW);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.QUIVER);
      this.canWorkerRefine.add(ResourceModule.ResourceType.BALLISTAE_BOLTS);
      this.canWorkerRefine.add(ResourceModule.ResourceType.BOW);
      this.canWorkerRefine.add(ResourceModule.ResourceType.QUIVER);
      this.canWorkerStore.add(ResourceModule.ResourceType.WOOD);
      this.canWorkerStore.add(ResourceModule.ResourceType.FEATHER);
      this.canWorkerStore.add(ResourceModule.ResourceType.SILK);
      this.canWorkerStore.add(ResourceModule.ResourceType.BALLISTAE_BOLTS);
      this.canWorkerStore.add(ResourceModule.ResourceType.BOW);
      this.canWorkerStore.add(ResourceModule.ResourceType.QUIVER);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.WOODY_TRASH);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.TRASHY_TRASH);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.WOOD);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.FEATHER);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.SILK);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.BALLISTAE_BOLTS);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.BOW);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.QUIVER);
      this.canHalfRefineWork = true;
   }
}
