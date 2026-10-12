package rtr.mobs.jobs;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.resources.ResourceModule;

public class MobJobBottler extends MobJobBase {
   public MobJobBottler() {
      this.mobJobName = Text.getText("mobJob.bottler.single");
      this.mobJobNamePlural = Text.getText("mobJob.bottler.plural");
      this.description = Text.getText("mobJob.bottler.description");
      this.baseColor = new Color(-8995159);
      this.accentColor = new Color(-9011266);
      this.refineSpeedBase = 100;
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.WATER_BOTTLE);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.WATER_BUCKET);
      this.canWorkerRefine.add(ResourceModule.ResourceType.WATER_BOTTLE);
      this.canWorkerStore.add(ResourceModule.ResourceType.WATER_BOTTLE);
      this.canWorkerStore.add(ResourceModule.ResourceType.WATER_BUCKET);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.TRASHY_TRASH);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.WATER_BUCKET);
      this.canWorkerRedistributeOnlyFull.add(ResourceModule.ResourceType.WATER_BUCKET);
   }
}
