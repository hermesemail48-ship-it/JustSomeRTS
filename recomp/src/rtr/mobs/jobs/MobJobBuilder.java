package rtr.mobs.jobs;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.resources.ResourceModule;

public class MobJobBuilder extends MobJobBase {
   public MobJobBuilder() {
      this.mobJobName = Text.getText("mobJob.builder.single");
      this.mobJobNamePlural = Text.getText("mobJob.builder.plural");
      this.description = Text.getText("mobJob.builder.description");
      this.baseColor = new Color(-16737895);
      this.accentColor = new Color(-15658701);
      this.canRepair = true;
      this.canDismantle = true;
      this.canClearTopography = true;
      this.harvestSpeedBase = 750;
      this.clearingSpeedBase = 200;
      this.buildingSpeedBase = 300;
      this.dismantlingSpeedBase = 200;
      this.repairSpeedBase = 200;
      this.preferredTools.add(ResourceModule.ResourceType.HAMMER);
      this.canWorkerSortPriority.add(ResourceModule.ResourceType.WOOD);
      this.canWorkerSortPriority.add(ResourceModule.ResourceType.BOARD);
      this.canWorkerSortPriority.add(ResourceModule.ResourceType.ROCK);
      this.canWorkerSortPriority.add(ResourceModule.ResourceType.CUT_STONE);
      this.canWorkerSortPriority.add(ResourceModule.ResourceType.CRYSTAL);
      this.canWorkerSortPriority.add(ResourceModule.ResourceType.CRYLITHIUM);
      this.canWorkerSortPriority.add(ResourceModule.ResourceType.IRON_INGOT);
      this.canWorkerDeliverConstruction.add(ResourceModule.ResourceType.WOOD);
      this.canWorkerDeliverConstruction.add(ResourceModule.ResourceType.BOARD);
      this.canWorkerDeliverConstruction.add(ResourceModule.ResourceType.ROCK);
      this.canWorkerDeliverConstruction.add(ResourceModule.ResourceType.CUT_STONE);
      this.canWorkerDeliverConstruction.add(ResourceModule.ResourceType.CRYSTAL);
      this.canWorkerDeliverConstruction.add(ResourceModule.ResourceType.CRYLITHIUM);
      this.canWorkerDeliverConstruction.add(ResourceModule.ResourceType.IRON_INGOT);
      this.canWorkerConstructionHarvest.add(ResourceModule.ResourceType.WOOD);
      this.canWorkerConstructionHarvest.add(ResourceModule.ResourceType.ROCK);
      this.canWorkerConstructionHarvest.add(ResourceModule.ResourceType.CRYSTAL);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.CRYSTALLY_TRASH);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.ROCKY_TRASH);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.WOODY_TRASH);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.TRASHY_TRASH);
      this.canWorkerBuild.add(ResourceModule.ResourceType.WOOD);
      this.canWorkerBuild.add(ResourceModule.ResourceType.BOARD);
      this.canWorkerBuild.add(ResourceModule.ResourceType.ROCK);
      this.canWorkerBuild.add(ResourceModule.ResourceType.CUT_STONE);
      this.canWorkerBuild.add(ResourceModule.ResourceType.CRYSTAL);
      this.canWorkerBuild.add(ResourceModule.ResourceType.CRYLITHIUM);
      this.canWorkerBuild.add(ResourceModule.ResourceType.IRON_INGOT);
   }
}
