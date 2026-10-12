package rtr.help.tips;

import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.mobs.MobBase;

public class TipVillagerOverheating extends TipBase {
   public TipVillagerOverheating() {
      this.tipName = "tipVillagerOverheating";
      this.tipHeader = Text.getText("helpTip.tip.villagerOverheatingHeader");
      this.finalize();
   }

   @Override
   protected void findTipConditions() throws SlickException {
      for (MobBase m : this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false)) {
         if (m.getBodyTemp() >= m.getMobFlags().getThermalOverheatCritical()) {
            this.attachedMob = m;
            Text.setVariableText("mobName", m.getFormattedName(true));
            if (m.getAssignedHome() == -1) {
               this.tipBody = Text.getText("helpTip.tip.villagerOverheatingNoHome");
            } else if (m.getGender() == MobBase.MobGender.MALE) {
               this.tipBody = Text.getText("helpTip.tip.villagerOverheatingMale");
            } else {
               this.tipBody = Text.getText("helpTip.tip.villagerOverheatingFemale");
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
         || this.attachedMob.getBodyTemp() < this.attachedMob.getMobFlags().getThermalOverheatCritical()) {
         this.clearAttachmentData();
         this.foundTipCondition = false;
         this.findTipConditions();
      }
   }
}
