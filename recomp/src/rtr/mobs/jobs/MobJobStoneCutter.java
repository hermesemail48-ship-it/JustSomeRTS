package rtr.mobs.jobs;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.resources.ResourceModule;

public class MobJobStoneCutter extends MobJobBase {
   public MobJobStoneCutter() {
      this.mobJobName = Text.getText("mobJob.stoneCutter.single");
      this.mobJobNamePlural = Text.getText("mobJob.stoneCutter.plural");
      this.description = Text.getText("mobJob.stoneCutter.description");
      this.baseColor = new Color(-6750208);
      this.accentColor = new Color(-12571905);
      this.refineSpeedBase = 1000;
      this.buildingSpeedBase = 300;
      this.preferredTools.add(ResourceModule.ResourceType.PICK_AXE);
      this.preferredTools.add(ResourceModule.ResourceType.HAMMER);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.ROCK);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.CUT_STONE);
      this.canWorkerRefine.add(ResourceModule.ResourceType.CUT_STONE);
      this.canWorkerStore.add(ResourceModule.ResourceType.ROCK);
      this.canWorkerStore.add(ResourceModule.ResourceType.CUT_STONE);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.ROCKY_TRASH);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.TRASHY_TRASH);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.ROCK);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.CUT_STONE);
   }
}
