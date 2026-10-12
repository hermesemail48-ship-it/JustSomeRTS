package rtr.help.tips;

import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.mobs.MobBase;

public class TipElders extends TipBase {
   public TipElders() {
      this.tipName = "tipElders";
      this.tipHeader = Text.getText("helpTip.tip.eldersHeader");
      this.finalize();
   }

   @Override
   protected void findTipConditions() throws SlickException {
      for (MobBase m : this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false)) {
         if (m.getMobFlags().getAllowAging() && m.getMobFlags().getSocialChatShareWisdom()) {
            Text.setVariableText("mobName", m.getFormattedName(false));
            this.tipBody = Text.getText("helpTip.tip.elders");
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
         || !this.attachedMob.getMobFlags().getSocialChatShareWisdom()) {
         this.clearAttachmentData();
         this.foundTipCondition = false;
         this.findTipConditions();
      }
   }
}
