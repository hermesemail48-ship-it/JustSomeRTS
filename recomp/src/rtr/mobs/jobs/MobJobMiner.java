package rtr.mobs.jobs;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.resources.ResourceModule;

public class MobJobMiner extends MobJobBase {
   public MobJobMiner() {
      this.mobJobName = Text.getText("mobJob.miner.single");
      this.mobJobNamePlural = Text.getText("mobJob.miner.plural");
      this.description = Text.getText("mobJob.miner.description");
      this.baseColor = new Color(-6750208);
      this.accentColor = new Color(-12572160);
      this.harvestSpeedBase = 300;
      this.preferredTools.add(ResourceModule.ResourceType.PICK_AXE);
      this.preferredTools.add(ResourceModule.ResourceType.SHOVEL);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.ROCK);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.IRON_ORE);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.GOLD_ORE);
      this.canWorkerHarvest.add(ResourceModule.ResourceType.ROCK);
      this.canWorkerStore.add(ResourceModule.ResourceType.ROCK);
      this.canWorkerStore.add(ResourceModule.ResourceType.IRON_ORE);
      this.canWorkerStore.add(ResourceModule.ResourceType.GOLD_ORE);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.ROCKY_TRASH);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.TRASHY_TRASH);
   }
}
