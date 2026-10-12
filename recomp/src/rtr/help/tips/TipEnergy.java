package rtr.help.tips;

import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;

public class TipEnergy extends TipBase {
   public TipEnergy() {
      this.tipName = "tipEnergy";
      this.tipHeader = Text.getText("helpTip.tip.energyHeader");
      this.finalize();
   }

   @Override
   protected void findTipConditions() throws SlickException {
      for (ObjectBase o : this.object.getObjectsArrayList()) {
         if (o.getObjectFlags().getMobGroup() == MobBase.MobGroup.VILLAGER
            && o.getObjectFlags().getEnergyMax() > 0
            && !o.getObjectFlags().canCollectEssence()
            && o.getPrayPoints().size() <= 0) {
            Text.setVariableText("buildingName", o.getFormattedName());
            this.tipBody = Text.getText("helpTip.tip.energy");
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
         || this.attachedObject.getObjectFlags().getEnergyMax() <= 0
         || this.attachedObject.getObjectFlags().canCollectEssence()
         || this.attachedObject.getObjectFlags().isBurner()) {
         this.clearAttachmentData();
         this.foundTipCondition = false;
         this.findTipConditions();
      }
   }
}
