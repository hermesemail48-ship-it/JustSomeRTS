package rtr.help.tips;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;

public class TipNoTowers extends TipBase {
   public TipNoTowers() {
      this.tipName = "tipNoTowers";
      this.tipHeader = Text.getText("helpTip.tip.noTowersHeader");
      this.tipBody = Text.getText("helpTip.tip.noTowers");
      this.finalize();
   }

   @Override
   protected void findTipConditions() throws SlickException {
      if (this.time.getDay() >= 3) {
         ArrayList<ObjectBase> objectList = this.object.getObjectsArrayList();

         for (ObjectBase o : objectList) {
            if (o.getTower() != null) {
               this.clearAttachmentData();
               this.foundTipCondition = false;
               return;
            }
         }

         Text.setVariableText("day", this.time.getDay());
         this.tipBody = Text.getText("helpTip.tip.noTowers");
         ObjectBase ox = this.object.getFirstBaseTypeFound(MapTilesLoader.TileSet.CASTLE_1);
         if (ox != null && ox.getObjectFlags().getSubType() == ObjectBase.ObjectSubType.BUILT) {
            this.attachedObject = ox;
            this.foundTipCondition = true;
            this.sound.playSound(SoundModule.SoundType.TIP);
            return;
         }

         ox = this.object.getFirstBaseTypeFound(MapTilesLoader.TileSet.ANCILLARY);
         if (ox != null && ox.getObjectFlags().getSubType() == ObjectBase.ObjectSubType.BUILT) {
            this.attachedObject = ox;
            this.foundTipCondition = true;
            this.sound.playSound(SoundModule.SoundType.TIP);
            return;
         }

         for (ObjectBase o2 : objectList) {
            if (o2.getObjectFlags().getMobGroup() == MobBase.MobGroup.VILLAGER
               && o2.getObjectFlags().getBaseType().getTileSetTemplate().getType() != MapTilesLoader.TileSetType.WALL) {
               this.attachedObject = o2;
               this.foundTipCondition = true;
               this.sound.playSound(SoundModule.SoundType.TIP);
               return;
            }
         }
      }

      this.clearAttachmentData();
      this.foundTipCondition = false;
   }

   @Override
   protected void verifyTipConditions() throws SlickException {
      if (this.attachedObject != null && this.object.objectExists(this.attachedObject)) {
         for (ObjectBase o : this.object.getObjectsArrayList()) {
            if (o.getTower() != null) {
               this.clearAttachmentData();
               this.foundTipCondition = false;
               this.findTipConditions();
               return;
            }
         }
      }

      this.clearAttachmentData();
      this.foundTipCondition = false;
      this.findTipConditions();
   }
}
