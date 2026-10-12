package rtr.help.tips;

import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.mobs.MobBase;

public class TipGhosts extends TipBase {
   public TipGhosts() {
      this.tipName = "tipGhosts";
      this.tipHeader = Text.getText("helpTip.tip.ghostsHeader");
      this.tipBody = Text.getText("helpTip.tip.ghosts");
      this.finalize();
   }

   @Override
   protected void findTipConditions() throws SlickException {
      for (MobBase m : this.mob.getMobsByGroup(MobBase.MobGroup.GHOST, false)) {
         if (m.getAlpha() > 0.0F) {
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
      if (this.attachedMob == null || !this.mob.getMobHashMap().containsKey(this.attachedMob.getID()) || !(this.attachedMob.getAlpha() > 0.0F)) {
         this.clearAttachmentData();
         this.foundTipCondition = false;
         this.findTipConditions();
      }
   }
}
