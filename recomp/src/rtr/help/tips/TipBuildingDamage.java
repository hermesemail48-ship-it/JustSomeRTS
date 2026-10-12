package rtr.help.tips;

import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;

public class TipBuildingDamage extends TipBase {
   public TipBuildingDamage() {
      this.tipName = "tipBuildingDamage";
      this.tipHeader = Text.getText("helpTip.tip.buildingDamageHeader");
      this.finalize();
   }

   @Override
   protected void findTipConditions() throws SlickException {
      for (ObjectBase o : this.object.getObjectsArrayList()) {
         if (o.isInRange() && o.getHitPoints() < o.getHitPointsMax() * 0.8 && o.getObjectFlags().getMobGroup() == MobBase.MobGroup.VILLAGER) {
            Text.setVariableText("buildingName", o.getFormattedName());
            this.tipBody = Text.getText("helpTip.tip.buildingDamage");
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
         || !(this.attachedObject.getHitPoints() < this.attachedObject.getHitPointsMax() * 0.8)
         || this.attachedObject.getObjectFlags().getMobGroup() != MobBase.MobGroup.VILLAGER) {
         this.clearAttachmentData();
         this.foundTipCondition = false;
         this.findTipConditions();
      }
   }
}
