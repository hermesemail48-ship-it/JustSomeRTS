package rtr.help.tips;

import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.TimeModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;

public class TipSeasonWinter extends TipBase {
   public TipSeasonWinter() {
      this.tipName = "tipSeasonWinter";
      this.tipHeader = Text.getText("helpTip.tip.seasonWinterHeader");
      this.tipBody = Text.getText("helpTip.tip.seasonWinter");
      this.finalize();
   }

   @Override
   protected void findTipConditions() throws SlickException {
      if (this.time.getSeason() == TimeModule.Season.WINTER) {
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
      if (this.attachedObject == null || !this.object.objectExists(this.attachedObject) || this.time.getSeason() != TimeModule.Season.WINTER) {
         this.clearAttachmentData();
         this.foundTipCondition = false;
         this.findTipConditions();
      }
   }
}
