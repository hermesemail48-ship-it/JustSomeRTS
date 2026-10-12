package rtr.mobs.jobs;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.resources.ResourceModule;

public class MobJobSmelter extends MobJobBase {
   public MobJobSmelter() {
      this.mobJobName = Text.getText("mobJob.smelter.single");
      this.mobJobNamePlural = Text.getText("mobJob.smelter.plural");
      this.description = Text.getText("mobJob.smelter.description");
      this.baseColor = new Color(-13297664);
      this.accentColor = new Color(-13421773);
      this.refineSpeedBase = 1000;
      this.preferredTools.add(ResourceModule.ResourceType.HAMMER);
      this.preferredTools.add(ResourceModule.ResourceType.PICK_AXE);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.IRON_ORE);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.IRON_INGOT);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.GOLD_ORE);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.GOLD_INGOT);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.GOLD_COIN_SACK);
      this.canWorkerRefine.add(ResourceModule.ResourceType.IRON_INGOT);
      this.canWorkerRefine.add(ResourceModule.ResourceType.GOLD_INGOT);
      this.canWorkerRefine.add(ResourceModule.ResourceType.GOLD_COIN_SACK);
      this.canWorkerStore.add(ResourceModule.ResourceType.IRON_ORE);
      this.canWorkerStore.add(ResourceModule.ResourceType.IRON_INGOT);
      this.canWorkerStore.add(ResourceModule.ResourceType.GOLD_ORE);
      this.canWorkerStore.add(ResourceModule.ResourceType.GOLD_INGOT);
      this.canWorkerStore.add(ResourceModule.ResourceType.GOLD_COIN_SACK);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.ROCKY_TRASH);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.TRASHY_TRASH);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.IRON_ORE);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.IRON_INGOT);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.GOLD_ORE);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.GOLD_INGOT);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.GOLD_COIN_SACK);
   }
}
