package rtr.help.tips;

import rtr.font.Text;

public class TipWelcomeCamp extends TipBase {
   public TipWelcomeCamp() {
      this.tipName = "tipWelcomeCamp";
      this.tipHeader = Text.getText("helpTip.tip.welcomeCampHeader");
      this.tipBody = Text.getText("helpTip.tip.welcomeCamp");
      this.forcePopup = true;
      this.finalize();
   }
}
