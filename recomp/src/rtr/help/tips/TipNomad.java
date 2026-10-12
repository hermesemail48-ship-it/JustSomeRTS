package rtr.help.tips;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.mobs.MobBase;

public class TipNomad extends TipBase {
   public TipNomad() {
      this.tipName = "tipNomad";
      this.tipHeader = Text.getText("helpTip.tip.nomadHeader");
      this.tipBody = Text.getText("helpTip.tip.nomad");
      this.finalize();
   }

   @Override
   protected void findTipConditions() throws SlickException {
      ArrayList<MobBase> mobList = this.mob.getMobsByGroup(MobBase.MobGroup.NOMAD, false);
      if (mobList.size() > 0) {
         this.attachedMob = mobList.get(0);
         this.foundTipCondition = true;
         this.sound.playSound(SoundModule.SoundType.TIP);
      } else {
         this.clearAttachmentData();
         this.foundTipCondition = false;
      }
   }

   @Override
   protected void verifyTipConditions() throws SlickException {
      if (this.attachedMob == null
         || !this.mob.getMobHashMap().containsKey(this.attachedMob.getID())
         || this.attachedMob.getMobGroup() != MobBase.MobGroup.NOMAD) {
         this.clearAttachmentData();
         this.foundTipCondition = false;
         this.findTipConditions();
      }
   }
}
