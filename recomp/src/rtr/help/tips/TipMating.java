package rtr.help.tips;

import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.mobs.MobBase;

public class TipMating extends TipBase {
   public TipMating() {
      this.tipName = "tipMating";
      this.tipHeader = Text.getText("helpTip.tip.matingHeader");
      this.finalize();
   }

   @Override
   protected void findTipConditions() throws SlickException {
      if (this.time.getDay() >= 3) {
         for (MobBase m : this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false)) {
            if (m.getMate() != null) {
               Text.setVariableText("mobName", m.getFormattedName(true));
               Text.setVariableText("mobMateName", m.getMate().getFormattedName(true));
               if (m.getGender() != m.getMate().getGender()) {
                  this.tipBody = Text.getText("helpTip.tip.matingDifferentGender");
               } else {
                  this.tipBody = Text.getText("helpTip.tip.matingSameGender");
               }

               this.attachedMob = m;
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
      if (this.attachedMob == null
         || !this.mob.getMobHashMap().containsKey(this.attachedMob.getID())
         || this.time.getDay() < 3
         || this.attachedMob.getMate() == null) {
         this.clearAttachmentData();
         this.foundTipCondition = false;
         this.findTipConditions();
      }
   }
}
