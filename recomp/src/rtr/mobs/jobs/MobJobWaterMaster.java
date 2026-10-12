package rtr.mobs.jobs;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.resources.ResourceModule;

public class MobJobWaterMaster extends MobJobBase {
   public MobJobWaterMaster() {
      this.mobJobName = Text.getText("mobJob.waterMaster.single");
      this.mobJobNamePlural = Text.getText("mobJob.waterMaster.plural");
      this.description = Text.getText("mobJob.waterMaster.description");
      this.baseColor = new Color(-12713797);
      this.accentColor = new Color(-9011266);
      this.harvestSpeedBase = 400;
      this.refineSpeedBase = 150;
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.WATER_BUCKET);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.DIRTY_WATER_BUCKET);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.WATER_BOTTLE);
      this.canWorkerHarvest.add(ResourceModule.ResourceType.DIRTY_WATER_BUCKET);
      this.canWorkerStore.add(ResourceModule.ResourceType.WATER_BUCKET);
      this.canWorkerStore.add(ResourceModule.ResourceType.DIRTY_WATER_BUCKET);
      this.canWorkerStore.add(ResourceModule.ResourceType.WATER_BOTTLE);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.TRASHY_TRASH);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.WATER_BUCKET);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.DIRTY_WATER_BUCKET);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.WATER_BOTTLE);
      this.canWorkerRedistributeOnlyFull.add(ResourceModule.ResourceType.WATER_BUCKET);
      this.canWorkerRedistributeOnlyFull.add(ResourceModule.ResourceType.DIRTY_WATER_BUCKET);
   }
}
