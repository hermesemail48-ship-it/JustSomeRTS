package rtr.help.tips;

import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.mobs.MobBase;

public class TipVillagerFreezing extends TipBase {
   public TipVillagerFreezing() {
      this.tipName = "tipVillagerFreezing";
      this.tipHeader = Text.getText("helpTip.tip.villagerFreezingHeader");
      this.finalize();
   }

   @Override
   protected void findTipConditions() throws SlickException {
      for (MobBase m : this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false)) {
         if (m.getBodyTemp() <= m.getMobFlags().getThermalFreezeCritical()) {
            this.attachedMob = m;
            Text.setVariableText("mobName", m.getFormattedName(true));
            if (m.getAssignedHome() == -1) {
               this.tipBody = Text.getText("helpTip.tip.villagerFreezingNoHome");
            } else if (m.getGender() == MobBase.MobGender.MALE) {
               this.tipBody = Text.getText("helpTip.tip.villagerFreezingMale");
            } else {
               this.tipBody = Text.getText("helpTip.tip.villagerFreezingFemale");
            }

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
      if (this.attachedMob == null
         || !this.mob.getMobHashMap().containsKey(this.attachedMob.getID())
         || this.attachedMob.isDead()
         || this.attachedMob.getBodyTemp() > this.attachedMob.getMobFlags().getThermalFreezeCritical()) {
         this.clearAttachmentData();
         this.foundTipCondition = false;
         this.findTipConditions();
      }
   }
}
