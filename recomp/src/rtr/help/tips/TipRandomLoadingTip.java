package rtr.help.tips;

import java.util.ArrayList;
import rtr.font.Text;
import rtr.utilities.Utilities;

public class TipRandomLoadingTip extends TipBase {
   protected ArrayList<String> randomLoadingMessages = new ArrayList<>();
   protected String randomLoadingMessage;

   public TipRandomLoadingTip() {
      int position = 0;

      while (!Text.getText("helpTip.loadingTip." + position).contains("helpTip.loadingTip")) {
         String line = Text.getText("helpTip.loadingTip." + position);
         position++;
         this.randomLoadingMessages.add(line);
      }

      this.randomLoadingMessage = this.randomLoadingMessages.get(Utilities.randomInt(this.randomLoadingMessages.size()));
      this.tipName = "tipRandomLoadingTip";
      this.tipHeader = Text.getText("helpTip.loadingTip.welcomeBack");
      this.tipBody = Text.getText(this.randomLoadingMessage);
      this.forcePopup = true;
      this.doNotSave = true;
      this.finalize();
   }
}
