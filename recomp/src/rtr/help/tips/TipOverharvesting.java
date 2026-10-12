package rtr.help.tips;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Utilities;

public class TipOverharvesting extends TipBase {
   public TipOverharvesting() {
      this.tipName = "tipOverharvesting";
      this.tipHeader = Text.getText("helpTip.tip.overharvestingHeader");
      this.tipBody = Text.getText("helpTip.tip.overharvesting");
      this.finalize();
   }

   @Override
   protected void findTipConditions() throws SlickException {
      ArrayList<ResourceBase> resources = this.resource.getResourcesOnGroundInVillage();
      int resourcesOnGroundCount = resources.size();

      for (ResourceBase r : resources) {
         if (r.getType().getTemplate().getResourceCategory() == ResourceModule.ResourceCategory.TRASH) {
            resourcesOnGroundCount--;
         }
      }

      if (resourcesOnGroundCount < 256) {
         this.clearAttachmentData();
         this.foundTipCondition = false;
      } else {
         ResourceBase rx = this.resource.getResourcesOnGroundInVillage().get(Utilities.randomInt(this.resource.getResourcesOnGroundInVillage().size()));

         for (int searchRange = 64; searchRange != 1 && this.resource.getResourcesNearby(rx.getTileX(), rx.getTileY(), 24).size() < searchRange; searchRange--) {
            rx = this.resource.getResourcesOnGroundInVillage().get(Utilities.randomInt(this.resource.getResourcesOnGroundInVillage().size()));
         }

         this.attachedResource = rx;
         this.foundTipCondition = true;
         this.sound.playSound(SoundModule.SoundType.TIP);
      }
   }

   @Override
   protected void verifyTipConditions() throws SlickException {
      if (this.attachedResource != null && this.attachedResource.isOnGround()) {
         ArrayList<ResourceBase> resources = this.resource.getResourcesOnGroundInVillage();
         int resourcesOnGroundCount = resources.size();

         for (ResourceBase r : resources) {
            if (r.getType().getTemplate().getResourceCategory() == ResourceModule.ResourceCategory.TRASH) {
               resourcesOnGroundCount--;
            }
         }

         if (resourcesOnGroundCount >= 256) {
            return;
         }
      }

      this.clearAttachmentData();
      this.foundTipCondition = false;
      this.findTipConditions();
   }
}
