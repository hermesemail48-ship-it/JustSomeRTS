package rtr.help.tips;

import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.mobs.MobBase;

public class TipVillagerDehydration extends TipBase {
   public TipVillagerDehydration() {
      this.tipName = "tipVillagerDehydration";
      this.tipHeader = Text.getText("helpTip.tip.villagerDehydrationHeader");
      this.finalize();
   }

   @Override
   protected void findTipConditions() throws SlickException {
      for (MobBase m : this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false)) {
         if (m.getThirst() <= m.getMobFlags().getThirstDehydrationWaterSearchLevel()) {
            this.attachedMob = m;
            Text.setVariableText("mobName", m.getFormattedName(true));
            if (m.getGender() == MobBase.MobGender.MALE) {
               this.tipBody = Text.getText("helpTip.tip.villagerDehydrationMale");
            } else {
               this.tipBody = Text.getText("helpTip.tip.villagerDehydrationFemale");
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
         || this.attachedMob.getThirst() > this.attachedMob.getMobFlags().getThirstDehydrationWaterSearchLevel()) {
         this.clearAttachmentData();
         this.foundTipCondition = false;
         this.findTipConditions();
      }
   }
}
