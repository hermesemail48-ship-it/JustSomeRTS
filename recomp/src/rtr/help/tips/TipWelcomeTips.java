package rtr.help.tips;

import rtr.font.Text;

public class TipWelcomeTips extends TipBase {
   public TipWelcomeTips() {
      this.tipName = "tipWelcomeTips";
      this.tipHeader = Text.getText("helpTip.tip.welcomeTipsHeader");
      this.tipBody = Text.getText("helpTip.tip.welcomeTips");
      this.forcePopup = true;
      this.finalize();
   }
}
