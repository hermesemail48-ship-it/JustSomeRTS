package rtr.help.tips;

import rtr.font.Text;

public class TipWorldMapWelcome extends TipBase {
   public TipWorldMapWelcome() {
      this.tipName = "tipWorldMapWelcome";
      this.tipHeader = Text.getText("helpTip.tip.worldMapWelcomeHeader");
      this.tipBody = Text.getText("helpTip.tip.worldMapWelcome");
      this.worldMapTip = true;
      this.finalize();
   }
}
