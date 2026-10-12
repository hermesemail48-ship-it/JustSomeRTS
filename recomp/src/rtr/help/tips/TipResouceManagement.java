package rtr.help.tips;

import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.objects.ObjectBase;
import rtr.resources.ResourceModule;

public class TipResouceManagement extends TipBase {
   public TipResouceManagement() {
      this.tipName = "tipResouceManagement";
      this.tipHeader = Text.getText("helpTip.tip.resouceManagementHeader");
      this.finalize();
   }

   @Override
   protected void findTipConditions() throws SlickException {
      for (ObjectBase o : this.object.getObjectsArrayList()) {
         if (o.isInRange() && o.getObjectFlags().getWorkerJobType() != null && o.getObjectFlags().getWorkerJobType().getJob().getCanWorkerRefine().size() > 0) {
            boolean notInfinity = false;

            for (ResourceModule.ResourceType t : o.getObjectFlags().getWorkerJobType().getJob().getCanWorkerRefine()) {
               if (this.resource.getResourceMaintainAmount(t) != ResourceModule.RESOURCE_INFINITE_AMOUNT
                  || this.resource.getResourceMaintainAmount(t) != ResourceModule.RESOURCE_STORAGE_AMOUNT) {
                  notInfinity = true;
                  break;
               }
            }

            if (notInfinity) {
               Text.setVariableText("buildingName", o.getFormattedName());
               Text.setVariableText("resourceName", o.getObjectFlags().getWorkerJobType().getJob().getCanWorkerRefine().get(0).getTemplate().getNamePlural());
               Text.setVariableText("mobJobName", o.getObjectFlags().getWorkerJobType().getJob().getMobJobNamePlural());
               this.tipBody = Text.getText("helpTip.tip.resouceManagement");
               this.attachedObject = o;
               this.foundTipCondition = true;
               this.sound.playSound(SoundModule.SoundType.TIP);
               return;
            }
         }
      }

      this.clearAttachmentData();
      this.foundTipCondition = false;
   }

   @Override
   protected void verifyTipConditions() throws SlickException {
      if (this.attachedObject != null
         && this.object.objectExists(this.attachedObject)
         && this.attachedObject.isInRange()
         && this.attachedObject.getObjectFlags().getWorkerJobType() != null
         && this.attachedObject.getObjectFlags().getWorkerJobType().getJob().getCanWorkerRefine().size() > 0) {
         for (ResourceModule.ResourceType t : this.attachedObject.getObjectFlags().getWorkerJobType().getJob().getCanWorkerRefine()) {
            if (this.resource.getResourceMaintainAmount(t) != ResourceModule.RESOURCE_INFINITE_AMOUNT
               || this.resource.getResourceMaintainAmount(t) != ResourceModule.RESOURCE_STORAGE_AMOUNT) {
               return;
            }
         }
      }

      this.clearAttachmentData();
      this.foundTipCondition = false;
      this.findTipConditions();
   }
}
