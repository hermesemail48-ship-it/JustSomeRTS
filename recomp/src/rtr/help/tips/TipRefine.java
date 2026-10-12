package rtr.help.tips;

import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class TipRefine extends TipBase {
   public TipRefine() {
      this.tipName = "tipRefine";
      this.tipHeader = Text.getText("helpTip.tip.refineHeader");
      this.finalize();
   }

   @Override
   protected void findTipConditions() throws SlickException {
      for (ObjectBase o : this.object.getObjectsArrayList()) {
         ObjectFlags objectFlags = o.getObjectFlags();
         if (objectFlags.getMobGroup() == MobBase.MobGroup.VILLAGER
            && (
               objectFlags.canBuildingBeBuilt(ResourceModule.ResourceType.BOARD)
                  || objectFlags.canBuildingBeBuilt(ResourceModule.ResourceType.CUT_STONE)
                  || objectFlags.canBuildingBeBuilt(ResourceModule.ResourceType.CRYLITHIUM)
                  || objectFlags.canBuildingBeBuilt(ResourceModule.ResourceType.IRON_INGOT)
            )) {
            Text.setVariableText("buildingName", o.getFormattedName());
            if (o.getObjectFlags().getSubType() == ObjectBase.ObjectSubType.BUILT) {
               this.tipBody = Text.getText("helpTip.tip.refineNew");
            } else {
               this.tipBody = Text.getText("helpTip.tip.refineReclaim");
            }

            this.attachedObject = o;
            this.foundTipCondition = true;
            this.sound.playSound(SoundModule.SoundType.TIP);
            return;
         }
      }

      this.clearAttachmentData();
      this.foundTipCondition = false;
   }

   @Override
   protected void verifyTipConditions() throws SlickException {
      if (this.attachedObject == null
         || !this.object.objectExists(this.attachedObject)
         || this.attachedObject.getObjectFlags().getMobGroup() != MobBase.MobGroup.VILLAGER
         || !this.attachedObject.getObjectFlags().canBuildingBeBuilt(ResourceModule.ResourceType.BOARD)
            && !this.attachedObject.getObjectFlags().canBuildingBeBuilt(ResourceModule.ResourceType.CUT_STONE)
            && !this.attachedObject.getObjectFlags().canBuildingBeBuilt(ResourceModule.ResourceType.CRYLITHIUM)
            && !this.attachedObject.getObjectFlags().canBuildingBeBuilt(ResourceModule.ResourceType.IRON_INGOT)) {
         this.clearAttachmentData();
         this.foundTipCondition = false;
         this.findTipConditions();
      }
   }
}
