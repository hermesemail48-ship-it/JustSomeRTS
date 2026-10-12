package rtr.help.tips;

import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.mobs.MobBase;

public class TipPregnant extends TipBase {
   public TipPregnant() {
      this.tipName = "tipPregnant";
      this.tipHeader = Text.getText("helpTip.tip.pregnantHeader");
      this.finalize();
   }

   @Override
   protected void findTipConditions() throws SlickException {
      for (MobBase m : this.mob.getMobsByGroup(MobBase.MobGroup.GHOST, false)) {
         if (m.isPregnant()) {
            Text.setVariableText("mobName", m.getFormattedName(true));
            this.tipBody = Text.getText("helpTip.tip.pregnant");
            this.attachedMob = m;
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
      if (this.attachedMob == null || !this.mob.getMobHashMap().containsKey(this.attachedMob.getID()) || !this.attachedMob.isPregnant()) {
         this.clearAttachmentData();
         this.foundTipCondition = false;
         this.findTipConditions();
      }
   }
}
