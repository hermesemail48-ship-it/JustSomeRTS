package rtr.mobs.jobs;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.resources.ResourceModule;

public class MobJobTumbler extends MobJobBase {
   public MobJobTumbler() {
      this.mobJobName = Text.getText("mobJob.tumbler.single");
      this.mobJobNamePlural = Text.getText("mobJob.tumbler.plural");
      this.description = Text.getText("mobJob.tumbler.description");
      this.baseColor = new Color(-6750055);
      this.accentColor = new Color(-12571905);
      this.refineSpeedBase = 300;
      this.preferredTools.add(ResourceModule.ResourceType.PICK_AXE);
      this.preferredTools.add(ResourceModule.ResourceType.HAMMER);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.ROCK);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.STONE_BALLS);
      this.canWorkerRefine.add(ResourceModule.ResourceType.STONE_BALLS);
      this.canWorkerStore.add(ResourceModule.ResourceType.ROCK);
      this.canWorkerStore.add(ResourceModule.ResourceType.STONE_BALLS);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.ROCKY_TRASH);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.TRASHY_TRASH);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.ROCK);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.STONE_BALLS);
      this.canHalfRefineWork = true;
   }
}
