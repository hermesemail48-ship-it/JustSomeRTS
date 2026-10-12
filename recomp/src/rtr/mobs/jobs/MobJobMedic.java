package rtr.mobs.jobs;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.resources.ResourceModule;

public class MobJobMedic extends MobJobBase {
   public MobJobMedic() {
      this.mobJobName = Text.getText("mobJob.medic.single");
      this.mobJobNamePlural = Text.getText("mobJob.medic.plural");
      this.description = Text.getText("mobJob.medic.description");
      this.baseColor = new Color(-65536);
      this.accentColor = new Color(-1);
      this.refineSpeedBase = 400;
      this.medicSpeedBase = 100;
      this.preferredTools.add(ResourceModule.ResourceType.MEDKIT);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.BANDAGE);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.SILK);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.WOOD);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.MEDKIT);
      this.canWorkerRefine.add(ResourceModule.ResourceType.BANDAGE);
      this.canWorkerRefine.add(ResourceModule.ResourceType.MEDKIT);
      this.canWorkerStore.add(ResourceModule.ResourceType.SILK);
      this.canWorkerStore.add(ResourceModule.ResourceType.WOOD);
      this.canWorkerStore.add(ResourceModule.ResourceType.BANDAGE);
      this.canWorkerStore.add(ResourceModule.ResourceType.MEDKIT);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.WOODY_TRASH);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.TRASHY_TRASH);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.SILK);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.WOOD);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.BANDAGE);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.MEDKIT);
      this.isMedic = true;
   }
}
