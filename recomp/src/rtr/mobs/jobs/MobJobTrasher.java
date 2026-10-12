package rtr.mobs.jobs;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.resources.ResourceModule;

public class MobJobTrasher extends MobJobBase {
   public MobJobTrasher() {
      this.mobJobName = Text.getText("mobJob.trasher.single");
      this.mobJobNamePlural = Text.getText("mobJob.trasher.plural");
      this.description = Text.getText("mobJob.trasher.description");
      this.baseColor = new Color(-15650014);
      this.accentColor = new Color(-8947849);
      this.canProcessTrash = true;
      this.processingTrashSpeedBase = 500;
      this.preferredTools.add(ResourceModule.ResourceType.SHOVEL);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.CRYSTALLY_TRASH);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.ORGANICY_TRASH);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.ROCKY_TRASH);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.TRASHY_CUBE);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.TRASHY_TRASH);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.WOODY_TRASH);
      this.canWorkerStore.add(ResourceModule.ResourceType.CRYSTALLY_TRASH);
      this.canWorkerStore.add(ResourceModule.ResourceType.ORGANICY_TRASH);
      this.canWorkerStore.add(ResourceModule.ResourceType.ROCKY_TRASH);
      this.canWorkerStore.add(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH);
      this.canWorkerStore.add(ResourceModule.ResourceType.TRASHY_CUBE);
      this.canWorkerStore.add(ResourceModule.ResourceType.TRASHY_TRASH);
      this.canWorkerStore.add(ResourceModule.ResourceType.WOODY_TRASH);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.CRYSTALLY_TRASH);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.ORGANICY_TRASH);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.ROCKY_TRASH);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.TRASHY_CUBE);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.TRASHY_TRASH);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.WOODY_TRASH);
   }
}
