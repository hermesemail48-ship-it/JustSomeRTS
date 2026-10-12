package rtr.mobs.jobs;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.resources.ResourceModule;

public class MobJobCrystiller extends MobJobBase {
   public MobJobCrystiller() {
      this.mobJobName = Text.getText("mobJob.crystiller.single");
      this.mobJobNamePlural = Text.getText("mobJob.crystiller.plural");
      this.description = Text.getText("mobJob.crystiller.description");
      this.baseColor = new Color(-3390464);
      this.accentColor = new Color(-2236929);
      this.refineSpeedBase = 1000;
      this.buildingSpeedBase = 300;
      this.preferredTools.add(ResourceModule.ResourceType.PICK_AXE);
      this.preferredTools.add(ResourceModule.ResourceType.HAMMER);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.CRYLITHIUM);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.CRYSTAL);
      this.canWorkerRefine.add(ResourceModule.ResourceType.CRYLITHIUM);
      this.canWorkerStore.add(ResourceModule.ResourceType.CRYSTAL);
      this.canWorkerStore.add(ResourceModule.ResourceType.CRYLITHIUM);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.CRYSTALLY_TRASH);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.TRASHY_TRASH);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.CRYSTAL);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.CRYLITHIUM);
   }
}
