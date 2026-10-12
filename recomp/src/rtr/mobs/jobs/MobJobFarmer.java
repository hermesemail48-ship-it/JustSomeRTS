package rtr.mobs.jobs;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.resources.ResourceModule;

public class MobJobFarmer extends MobJobBase {
   public MobJobFarmer() {
      this.mobJobName = Text.getText("mobJob.farmer.single");
      this.mobJobNamePlural = Text.getText("mobJob.farmer.plural");
      this.description = Text.getText("mobJob.farmer.description");
      this.baseColor = new Color(-16755456);
      this.accentColor = new Color(-6711040);
      this.harvestSpeedBase = 300;
      this.tendAnimalsBase = 500;
      this.preferredTools.add(ResourceModule.ResourceType.HOE);
      this.preferredTools.add(ResourceModule.ResourceType.SHOVEL);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.RAW_VEGETABLE);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.DIRTY_WATER_BUCKET);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.RAW_MEAT);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.COOKED_MEAT);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.EGG);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.BOILED_EGG);
      this.canWorkerHarvest.add(ResourceModule.ResourceType.RAW_VEGETABLE);
      this.canWorkerHarvest.add(ResourceModule.ResourceType.DIRTY_WATER_BUCKET);
      this.canWorkerHarvestWorksiteOnly.add(ResourceModule.ResourceType.DIRTY_WATER_BUCKET);
      this.canWorkerPlant.add(ResourceModule.ResourceType.RAW_VEGETABLE);
      this.canWorkerStore.add(ResourceModule.ResourceType.RAW_VEGETABLE);
      this.canWorkerStore.add(ResourceModule.ResourceType.DIRTY_WATER_BUCKET);
      this.canWorkerStore.add(ResourceModule.ResourceType.RAW_MEAT);
      this.canWorkerStore.add(ResourceModule.ResourceType.COOKED_MEAT);
      this.canWorkerStore.add(ResourceModule.ResourceType.EGG);
      this.canWorkerStore.add(ResourceModule.ResourceType.BOILED_EGG);
      this.canWorkerStoreWorksiteOnly.add(ResourceModule.ResourceType.DIRTY_WATER_BUCKET);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.ORGANICY_TRASH);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.TRASHY_TRASH);
      this.canTendAnimals = true;
   }
}
