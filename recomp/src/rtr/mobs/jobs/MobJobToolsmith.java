package rtr.mobs.jobs;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.resources.ResourceModule;

public class MobJobToolsmith extends MobJobBase {
   public MobJobToolsmith() {
      this.mobJobName = Text.getText("mobJob.toolsmith.single");
      this.mobJobNamePlural = Text.getText("mobJob.toolsmith.plural");
      this.description = Text.getText("mobJob.toolsmith.description");
      this.baseColor = new Color(-30208);
      this.accentColor = new Color(-11534189);
      this.refineSpeedBase = 1000;
      this.preferredTools.add(ResourceModule.ResourceType.HAMMER);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.IRON_INGOT);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.BOARD);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.AXE);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.HAMMER);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.HOE);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.PICK_AXE);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.SHOVEL);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.IRON_SWORD);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.WOOD_SWORD);
      this.canWorkerRefine.add(ResourceModule.ResourceType.AXE);
      this.canWorkerRefine.add(ResourceModule.ResourceType.HAMMER);
      this.canWorkerRefine.add(ResourceModule.ResourceType.HOE);
      this.canWorkerRefine.add(ResourceModule.ResourceType.PICK_AXE);
      this.canWorkerRefine.add(ResourceModule.ResourceType.SHOVEL);
      this.canWorkerRefine.add(ResourceModule.ResourceType.IRON_SWORD);
      this.canWorkerRefine.add(ResourceModule.ResourceType.WOOD_SWORD);
      this.canWorkerStore.add(ResourceModule.ResourceType.IRON_INGOT);
      this.canWorkerStore.add(ResourceModule.ResourceType.BOARD);
      this.canWorkerStore.add(ResourceModule.ResourceType.AXE);
      this.canWorkerStore.add(ResourceModule.ResourceType.HAMMER);
      this.canWorkerStore.add(ResourceModule.ResourceType.HOE);
      this.canWorkerStore.add(ResourceModule.ResourceType.PICK_AXE);
      this.canWorkerStore.add(ResourceModule.ResourceType.SHOVEL);
      this.canWorkerStore.add(ResourceModule.ResourceType.IRON_SWORD);
      this.canWorkerStore.add(ResourceModule.ResourceType.WOOD_SWORD);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.TRASHY_TRASH);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.IRON_INGOT);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.BOARD);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.AXE);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.HAMMER);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.HOE);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.PICK_AXE);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.SHOVEL);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.IRON_SWORD);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.WOOD_SWORD);
   }
}
