package rtr.help.tips;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.resources.ResourceBase;
import rtr.utilities.Utilities;

public class TipDecayingResources extends TipBase {
   public TipDecayingResources() {
      this.tipName = "tipDecayingResources";
      this.tipHeader = Text.getText("helpTip.tip.decayingResourcesHeader");
      this.tipBody = Text.getText("helpTip.tip.decayingResources");
      this.finalize();
   }

   @Override
   protected void findTipConditions() throws SlickException {
      ArrayList<ResourceBase> resources = this.resource.getResourcesOnGroundInVillage();
      ArrayList<ResourceBase> decayingResources = new ArrayList<>();
      int resourcesDecayingCount = 0;

      for (ResourceBase r : resources) {
         if (r.isResourceNearDecay()) {
            resourcesDecayingCount++;
            decayingResources.add(r);
            if (resourcesDecayingCount >= 32) {
               break;
            }
         }
      }

      if (resourcesDecayingCount < 32) {
         this.clearAttachmentData();
         this.foundTipCondition = false;
      } else {
         ResourceBase rx = decayingResources.get(Utilities.randomInt(decayingResources.size()));

         for (int searchRange = 16; searchRange != 1 && this.resource.getResourcesNearby(rx.getTileX(), rx.getTileY(), 24).size() < searchRange; searchRange--) {
            rx = decayingResources.get(Utilities.randomInt(decayingResources.size()));
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
         int resourcesDecayingCount = 0;

         for (ResourceBase r : resources) {
            if (r.isResourceNearDecay()) {
               if (++resourcesDecayingCount >= 32) {
                  break;
               }
            }
         }

         if (resourcesDecayingCount >= 32) {
            return;
         }
      }

      this.clearAttachmentData();
      this.foundTipCondition = false;
      this.findTipConditions();
   }
}
