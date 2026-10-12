package rtr.mobs.jobs;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.resources.ResourceModule;

public class MobJobCook extends MobJobBase {
   public MobJobCook() {
      this.mobJobName = Text.getText("mobJob.cook.single");
      this.mobJobNamePlural = Text.getText("mobJob.cook.plural");
      this.description = Text.getText("mobJob.cook.description");
      this.baseColor = new Color(-2236963);
      this.accentColor = new Color(-6710887);
      this.refineSpeedBase = 1000;
      this.buildingSpeedBase = 300;
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.RATION);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.COOKED_MEAT);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.BOILED_EGG);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.RAW_VEGETABLE);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.RAW_MEAT);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.EGG);
      this.canWorkerRefine.add(ResourceModule.ResourceType.RATION);
      this.canWorkerRefine.add(ResourceModule.ResourceType.COOKED_MEAT);
      this.canWorkerRefine.add(ResourceModule.ResourceType.BOILED_EGG);
      this.canWorkerSlaughter.add(ResourceModule.ResourceType.FEATHER);
      this.canWorkerSlaughter.add(ResourceModule.ResourceType.RAW_HIDE);
      this.canWorkerSlaughter.add(ResourceModule.ResourceType.RAW_MEAT);
      this.canWorkerStore.add(ResourceModule.ResourceType.RAW_VEGETABLE);
      this.canWorkerStore.add(ResourceModule.ResourceType.RATION);
      this.canWorkerStore.add(ResourceModule.ResourceType.RAW_MEAT);
      this.canWorkerStore.add(ResourceModule.ResourceType.COOKED_MEAT);
      this.canWorkerStore.add(ResourceModule.ResourceType.EGG);
      this.canWorkerStore.add(ResourceModule.ResourceType.BOILED_EGG);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.ORGANICY_TRASH);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.TRASHY_TRASH);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.RAW_VEGETABLE);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.RATION);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.RAW_MEAT);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.COOKED_MEAT);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.EGG);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.BOILED_EGG);
   }
}
