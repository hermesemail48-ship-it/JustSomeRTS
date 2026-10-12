package rtr.help.tips;

import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.mobs.MobBase;

public class TipVillagerSleepy extends TipBase {
   public TipVillagerSleepy() {
      this.tipName = "tipVillagerSleepy";
      this.tipHeader = Text.getText("helpTip.tip.villagerSleepyHeader");
      this.finalize();
   }

   @Override
   protected void findTipConditions() throws SlickException {
      for (MobBase m : this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false)) {
         if (m.getEnergy() <= m.getMobFlags().getSleepHomeReturnLevel()) {
            this.attachedMob = m;
            Text.setVariableText("mobName", m.getFormattedName(true));
            if (m.getAssignedHome() == -1) {
               this.tipBody = Text.getText("helpTip.tip.villagerSleepyNoHome");
            } else {
               this.tipBody = Text.getText("helpTip.tip.villagerSleepy");
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
         || this.attachedMob.getEnergy() > this.attachedMob.getMobFlags().getSleepHomeReturnLevel()) {
         this.clearAttachmentData();
         this.foundTipCondition = false;
         this.findTipConditions();
      }
   }
}
