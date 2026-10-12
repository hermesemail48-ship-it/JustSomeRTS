package rtr.help.tips;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;

public class TipBuildingPriority extends TipBase {
   public TipBuildingPriority() {
      this.tipName = "tipBuildingPriority";
      this.tipHeader = Text.getText("helpTip.tip.buildingPriorityHeader");
      this.tipBody = Text.getText("helpTip.tip.buildingPriority");
      this.finalize();
   }

   @Override
   protected void findTipConditions() throws SlickException {
      int buildingCount = 0;
      ArrayList<ObjectBase> objectList = this.object.getObjectsArrayList();

      for (ObjectBase o2 : objectList) {
         if (o2.isInRange()
            && o2.getObjectFlags().getMobGroup() == MobBase.MobGroup.VILLAGER
            && o2.getObjectFlags().getBaseType().getTileSetTemplate().getType() != MapTilesLoader.TileSetType.WALL) {
            if (++buildingCount >= 15) {
               break;
            }
         }
      }

      if (buildingCount >= 15) {
         ObjectBase o = this.object.getFirstBaseTypeFound(MapTilesLoader.TileSet.CASTLE_1);
         if (o != null && o.getObjectFlags().getSubType() == ObjectBase.ObjectSubType.BUILT) {
            this.attachedObject = o;
            this.foundTipCondition = true;
            this.sound.playSound(SoundModule.SoundType.TIP);
            return;
         }

         o = this.object.getFirstBaseTypeFound(MapTilesLoader.TileSet.ANCILLARY);
         if (o != null && o.getObjectFlags().getSubType() == ObjectBase.ObjectSubType.BUILT) {
            this.attachedObject = o;
            this.foundTipCondition = true;
            this.sound.playSound(SoundModule.SoundType.TIP);
            return;
         }

         for (ObjectBase o2x : objectList) {
            if (o2x.getObjectFlags().getMobGroup() == MobBase.MobGroup.VILLAGER
               && o2x.getObjectFlags().getBaseType().getTileSetTemplate().getType() != MapTilesLoader.TileSetType.WALL) {
               this.attachedObject = o2x;
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
         int buildingCount = 0;

         for (ObjectBase o2 : this.object.getObjectsArrayList()) {
            if (o2.isInRange()
               && o2.getObjectFlags().getMobGroup() == MobBase.MobGroup.VILLAGER
               && o2.getObjectFlags().getBaseType().getTileSetTemplate().getType() != MapTilesLoader.TileSetType.WALL) {
               if (++buildingCount >= 15) {
                  break;
               }
            }
         }

         if (buildingCount >= 15) {
            return;
         }
      }

      this.clearAttachmentData();
      this.foundTipCondition = false;
      this.findTipConditions();
   }
}
