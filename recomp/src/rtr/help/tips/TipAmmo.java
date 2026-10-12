package rtr.help.tips;

import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;

public class TipAmmo extends TipBase {
   public TipAmmo() {
      this.tipName = "tipAmmo";
      this.tipHeader = Text.getText("helpTip.tip.ammoHeader");
      this.finalize();
   }

   @Override
   protected void findTipConditions() throws SlickException {
      for (ObjectBase o : this.object.getObjectsArrayList()) {
         if (o.getObjectFlags().getMobGroup() == MobBase.MobGroup.VILLAGER && o.getObjectFlags().getAmmoMax() > 0) {
            Text.setVariableText("buildingName", o.getFormattedName());
            this.tipBody = Text.getText("helpTip.tip.ammo");
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
         || this.attachedObject.getObjectFlags().getAmmoMax() <= 0) {
         this.clearAttachmentData();
         this.foundTipCondition = false;
         this.findTipConditions();
      }
   }
}
