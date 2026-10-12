package rtr.mobs.jobs;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.resources.ResourceModule;

public class MobJobProvisioner extends MobJobBase {
   public MobJobProvisioner() {
      this.mobJobName = Text.getText("mobJob.provisioner.single");
      this.mobJobNamePlural = Text.getText("mobJob.provisioner.plural");
      this.description = Text.getText("mobJob.provisioner.description");
      this.baseColor = new Color(-16733645);
      this.accentColor = new Color(-5592406);
      this.canWorkerSortPriority.add(ResourceModule.ResourceType.GOLD_COIN_SACK);
      this.canWorkerStore.add(ResourceModule.ResourceType.GOLD_COIN_SACK);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.GOLD_COIN_SACK);

      ResourceModule.ResourceType[] var4;
      for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
         this.canWorkerTrade.add(t);
      }
   }
}
