package rtr.help.tips;

import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.system.Game;
import rtr.system.gamemodetemplates.GameModeTemplateBase;

public class TipSandbox extends TipBase {
   public TipSandbox() {
      this.tipName = "tipSandbox";
      this.tipHeader = Text.getText("helpTip.tip.sandboxModeHeader");
      this.tipBody = Text.getText("helpTip.tip.sandboxMode");
      this.finalize();
   }

   @Override
   protected void findTipConditions() throws SlickException {
      if (Game.getCS().getGameMode() == GameModeTemplateBase.GameMode.SANDBOX) {
         this.forcePopup = true;
         this.foundTipCondition = true;
      } else {
         this.clearAttachmentData();
         this.foundTipCondition = false;
      }
   }
}
