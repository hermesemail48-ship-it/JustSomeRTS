package rtr.help.tips;

import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.resources.ResourceModule;

public class TipTrash extends TipBase {
   public TipTrash() {
      this.tipName = "tipTrash";
      this.tipHeader = Text.getText("helpTip.tip.trashHeader");
      this.tipBody = Text.getText("helpTip.tip.trash");
      this.finalize();
   }

   @Override
   protected void findTipConditions() throws SlickException {
      int trashCount = 0;

      ResourceModule.ResourceType[] var5;
      for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
         if (t.getTemplate().getResourceCategory() == ResourceModule.ResourceCategory.TRASH) {
            trashCount += this.resource.getResourceGround(t, true);
            if (trashCount >= 128) {
               break;
            }
         }
      }

      if (trashCount >= 128) {
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

         for (ObjectBase o2 : this.object.getObjectsArrayList()) {
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
         int trashCount = 0;

         ResourceModule.ResourceType[] var5;
         for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
            if (t.getTemplate().getResourceCategory() == ResourceModule.ResourceCategory.TRASH) {
               trashCount += this.resource.getResourceGround(t, true);
               if (trashCount >= 128) {
                  break;
               }
            }
         }

         if (trashCount >= 128) {
            return;
         }
      }

      this.clearAttachmentData();
      this.foundTipCondition = false;
      this.findTipConditions();
   }
}
