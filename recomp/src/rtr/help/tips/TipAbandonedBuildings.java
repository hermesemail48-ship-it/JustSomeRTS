package rtr.help.tips;

import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.objects.ObjectBase;

public class TipAbandonedBuildings extends TipBase {
   public TipAbandonedBuildings() {
      this.tipName = "tipAbandonedBuildings";
      this.tipHeader = Text.getText("helpTip.tip.abandonedBuildingsHeader");
      this.finalize();
   }

   @Override
   protected void findTipConditions() throws SlickException {
      for (ObjectBase o : this.object.getObjectsArrayList()) {
         if (o.isInRange() && o.getObjectFlags().getSubType() == ObjectBase.ObjectSubType.ABANDONED) {
            Text.setVariableText("buildingName", o.getFormattedName());
            this.tipBody = Text.getText("helpTip.tip.abandonedBuildings");
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
         || !this.attachedObject.isInRange()
         || this.attachedObject.getObjectFlags().getSubType() != ObjectBase.ObjectSubType.ABANDONED) {
         this.clearAttachmentData();
         this.foundTipCondition = false;
         this.findTipConditions();
      }
   }
}
