package rtr.help.tips;

import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.mobs.MobBase;

public class TipLifeExpectancy extends TipBase {
   public TipLifeExpectancy() {
      this.tipName = "tipLifeExpectancy";
      this.tipHeader = Text.getText("helpTip.tip.lifeExpectancyHeader");
      this.tipBody = Text.getText("helpTip.tip.lifeExpectancy");
      this.finalize();
   }

   @Override
   protected void findTipConditions() throws SlickException {
      for (MobBase m : this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false)) {
         if (m.getMobFlags().getAllowAging()
            && m.getMobFlags().getAgingStartAgeMaximum() > 0
            && m.getLifeExpectancy() < m.getMobFlags().getAgingStartAgeMaximum() * 0.8F) {
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
      if (this.attachedMob == null
         || !this.mob.getMobHashMap().containsKey(this.attachedMob.getID())
         || !this.attachedMob.getMobFlags().getAllowAging()
         || this.attachedMob.getMobFlags().getAgingStartAgeMaximum() <= 0
         || !(this.attachedMob.getLifeExpectancy() < this.attachedMob.getMobFlags().getAgingStartAgeMaximum() * 0.8F)) {
         this.clearAttachmentData();
         this.foundTipCondition = false;
         this.findTipConditions();
      }
   }
}
