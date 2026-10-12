package rtr.help.tips;

import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.system.Game;

public class TipGameSpeed extends TipBase {
   public TipGameSpeed() {
      this.tipName = "tipGameSpeed";
      this.tipHeader = Text.getText("helpTip.tip.gameSpeedHeader");
      this.tipBody = Text.getText("helpTip.tip.gameSpeed");
      this.finalize();
   }

   @Override
   protected void findTipConditions() throws SlickException {
      if (Game.getCS().getGameSpeed() > 1) {
         this.forcePopup = true;
         this.foundTipCondition = true;
      } else {
         this.clearAttachmentData();
         this.foundTipCondition = false;
      }
   }
}
