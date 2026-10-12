package rtr.help.tips;

import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.objects.ObjectBase;

public class TipWorkBuildings extends TipBase {
   public TipWorkBuildings() {
      this.tipName = "tipWorkBuildings";
      this.tipHeader = Text.getText("helpTip.tip.workBuildingsHeader");
      this.finalize();
   }

   @Override
   protected void findTipConditions() throws SlickException {
      for (ObjectBase o : this.object.getObjectsArrayList()) {
         if (o.getObjectFlags().getBaseType() != MapTilesLoader.TileSet.CASTLE_1 && o.isInRange() && o.getObjectFlags().getWorkersProvided() > 0) {
            Text.setVariableText("buildingName", o.getFormattedName());
            Text.setVariableText("amount", o.getObjectFlags().getWorkersProvided());
            Text.setVariableText("mobJobName", o.getObjectFlags().getWorkerJobType().getJob().getMobJobName());
            this.tipBody = Text.getText("helpTip.tip.workBuildings");
            this.attachedObject = o;
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
      if (this.attachedObject == null
         || !this.object.objectExists(this.attachedObject)
         || this.attachedObject.getObjectFlags().getBaseType() == MapTilesLoader.TileSet.CASTLE_1
         || !this.attachedObject.isInRange()
         || this.attachedObject.getObjectFlags().getWorkersProvided() <= 0) {
         this.clearAttachmentData();
         this.foundTipCondition = false;
         this.findTipConditions();
      }
   }
}
