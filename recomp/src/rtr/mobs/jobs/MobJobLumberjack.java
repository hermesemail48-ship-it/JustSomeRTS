package rtr.mobs.jobs;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.resources.ResourceModule;

public class MobJobLumberjack extends MobJobBase {
   public MobJobLumberjack() {
      this.mobJobName = Text.getText("mobJob.lumberjack.single");
      this.mobJobNamePlural = Text.getText("mobJob.lumberjack.plural");
      this.description = Text.getText("mobJob.lumberjack.description");
      this.baseColor = new Color(-10805248);
      this.accentColor = new Color(-16733696);
      this.harvestSpeedBase = 300;
      this.preferredTools.add(ResourceModule.ResourceType.AXE);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.WOOD);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.SILK);
      this.canWorkerHarvest.add(ResourceModule.ResourceType.WOOD);
      this.canWorkerStore.add(ResourceModule.ResourceType.WOOD);
      this.canWorkerStore.add(ResourceModule.ResourceType.SILK);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.WOODY_TRASH);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.TRASHY_TRASH);
   }
}
