package rtr.help.tips;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.utilities.OrderedPair;

public class TipWallAttacked extends TipBase {
   public TipWallAttacked() {
      this.tipName = "tipWallAttacked";
      this.tipHeader = Text.getText("helpTip.tip.wallAttackedHeader");
      this.finalize();
   }

   @Override
   protected void findTipConditions() throws SlickException {
      for (MobBase m : this.mob.getMobsByGroup(MobBase.MobGroup.MONSTER, false)) {
         ArrayList<OrderedPair> mobPath = m.getActiveNode().getPathCoordinatesList();
         if (mobPath != null) {
            for (OrderedPair o : mobPath) {
               ObjectBase objectCheck = this.object.getBuildingAtLocation(o);
               if (objectCheck != null
                  && objectCheck.getObjectFlags().getSubType() == ObjectBase.ObjectSubType.BUILT
                  && objectCheck.getObjectFlags().getBaseType().getTileSetTemplate().getType() == MapTilesLoader.TileSetType.WALL) {
                  Text.setVariableText("buildingName", objectCheck.getObjectFlags().getName());
                  this.tipBody = Text.getText("helpTip.tip.wallAttacked");
                  this.attachedObject = objectCheck;
                  this.foundTipCondition = true;
                  this.sound.playSound(SoundModule.SoundType.TIP);
                  return;
               }
            }
         }
      }

      this.clearAttachmentData();
      this.foundTipCondition = false;
   }

   @Override
   protected void verifyTipConditions() throws SlickException {
      if (this.attachedObject != null
         && this.object.objectExists(this.attachedObject)
         && this.attachedObject.getObjectFlags().getSubType() == ObjectBase.ObjectSubType.BUILT
         && this.attachedObject.getObjectFlags().getBaseType().getTileSetTemplate().getType() == MapTilesLoader.TileSetType.WALL) {
         for (MobBase m : this.mob.getMobsByGroup(MobBase.MobGroup.MONSTER, false)) {
            ArrayList<OrderedPair> mobPath = m.getActiveNode().getPathCoordinatesList();
            if (mobPath != null) {
               for (OrderedPair o : mobPath) {
                  ObjectBase objectCheck = this.object.getBuildingAtLocation(o);
                  if (objectCheck == this.attachedObject) {
                     return;
                  }
               }
            }
         }
      }

      this.clearAttachmentData();
      this.foundTipCondition = false;
      this.findTipConditions();
   }
}
