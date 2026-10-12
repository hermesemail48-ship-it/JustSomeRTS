package rtr.mobs.jobs;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.resources.ResourceModule;

public class MobJobCourierSupplier extends MobJobBase {
   public MobJobCourierSupplier() {
      this.mobJobName = Text.getText("mobJob.courierSupplier.single");
      this.mobJobNamePlural = Text.getText("mobJob.courierSupplier.plural");
      this.description = Text.getText("mobJob.courierSupplier.description");
      this.baseColor = new Color(-1595358);
      this.accentColor = new Color(-14454272);

      ResourceModule.ResourceType[] var4;
      for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
         this.canWorkerDeliverCourier.add(t);
      }

      this.canWorkerDeliverCourierOnlyFull.add(ResourceModule.ResourceType.WATER_BUCKET);
      this.canWorkerDeliverCourierOnlyFull.add(ResourceModule.ResourceType.DIRTY_WATER_BUCKET);
   }
}
