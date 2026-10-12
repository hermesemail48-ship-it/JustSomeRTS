package rtr.help.tips;

import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.objects.ObjectBase;
import rtr.states.StateBase;

public class TipHarvestingResources extends TipBase {
   public TipHarvestingResources() {
      this.tipName = "tipHarvestingResources";
      this.tipHeader = Text.getText("helpTip.tip.harvestingResourcesHeader");
      this.tipBody = Text.getText("helpTip.tip.harvestingResources");
      this.finalize();
   }

   @Override
   protected void findTipConditions() throws SlickException {
      ObjectBase o = this.object.getFirstBaseTypeFound(MapTilesLoader.TileSet.CASTLE_1);
      if (o != null && o.getObjectFlags().getSubType() == ObjectBase.ObjectSubType.BUILT) {
         this.attachedObject = o;
         this.foundTipCondition = true;
         this.sound.playSound(SoundModule.SoundType.TIP);
      } else if (this.save.getActiveRegionalSave().getGameType() == StateBase.GameType.SKIRMISH) {
         this.forcePopup = true;
         this.foundTipCondition = true;
         this.sound.playSound(SoundModule.SoundType.TIP);
      } else {
         this.clearAttachmentData();
         this.foundTipCondition = false;
      }
   }

   @Override
   protected void verifyTipConditions() throws SlickException {
      if (this.save.getActiveRegionalSave().getGameType() == StateBase.GameType.SKIRMISH || !this.object.objectExists(this.attachedObject)) {
         this.clearAttachmentData();
         this.foundTipCondition = false;
         this.findTipConditions();
      }
   }
}
