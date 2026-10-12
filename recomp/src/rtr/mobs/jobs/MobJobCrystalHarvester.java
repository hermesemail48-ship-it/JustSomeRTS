package rtr.mobs.jobs;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.resources.ResourceModule;

public class MobJobCrystalHarvester extends MobJobBase {
   public MobJobCrystalHarvester() {
      this.mobJobName = Text.getText("mobJob.crystalHarvester.single");
      this.mobJobNamePlural = Text.getText("mobJob.crystalHarvester.plural");
      this.description = Text.getText("mobJob.crystalHarvester.description");
      this.baseColor = new Color(-3390464);
      this.accentColor = new Color(-2237150);
      this.harvestSpeedBase = 300;
      this.preferredTools.add(ResourceModule.ResourceType.PICK_AXE);
      this.preferredTools.add(ResourceModule.ResourceType.HAMMER);
      this.preferredTools.add(ResourceModule.ResourceType.SHOVEL);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.CRYSTAL);
      this.canWorkerHarvest.add(ResourceModule.ResourceType.CRYSTAL);
      this.canWorkerStore.add(ResourceModule.ResourceType.CRYSTAL);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.CRYSTALLY_TRASH);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.TRASHY_TRASH);
   }
}
