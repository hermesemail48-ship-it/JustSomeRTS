package rtr.help.tips;

import org.newdawn.slick.SlickException;
import rtr.SettingsParser;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.utilities.Utilities;

public class TipSpeedControls extends TipBase {
   public TipSpeedControls() {
      this.tipName = "tipSpeedControls2";
      this.tipHeader = Text.getText("helpTip.tip.speedControlsHeader");
      this.finalize();
   }

   @Override
   protected void findTipConditions() throws SlickException {
      if (this.time.getDay() >= 2 && Utilities.randomInt(100000) == 0) {
         ObjectBase o = this.object.getFirstBaseTypeFound(MapTilesLoader.TileSet.CASTLE_1);
         if (o != null && o.getObjectFlags().getSubType() == ObjectBase.ObjectSubType.BUILT) {
            this.attachedObject = o;
            this.foundTipCondition = true;
            Text.setVariableText("shortcutKey", SettingsParser.getKeyPauseName());
            this.tipBody = Text.getText("helpTip.tip.speedControls");
            this.sound.playSound(SoundModule.SoundType.TIP);
            return;
         }

         o = this.object.getFirstBaseTypeFound(MapTilesLoader.TileSet.ANCILLARY);
         if (o != null && o.getObjectFlags().getSubType() == ObjectBase.ObjectSubType.BUILT) {
            this.attachedObject = o;
            this.foundTipCondition = true;
            Text.setVariableText("shortcutKey", SettingsParser.getKeyPauseName());
            this.tipBody = Text.getText("helpTip.tip.speedControls");
            this.sound.playSound(SoundModule.SoundType.TIP);
            return;
         }

         for (ObjectBase o2 : this.object.getObjectsArrayList()) {
            if (o2.getObjectFlags().getMobGroup() == MobBase.MobGroup.VILLAGER
               && o2.getObjectFlags().getBaseType().getTileSetTemplate().getType() != MapTilesLoader.TileSetType.WALL) {
               this.attachedObject = o2;
               this.foundTipCondition = true;
               Text.setVariableText("shortcutKey", SettingsParser.getKeyPauseName());
               this.tipBody = Text.getText("helpTip.tip.speedControls");
               return;
            }
         }
      }

      this.clearAttachmentData();
      this.foundTipCondition = false;
   }

   @Override
   protected void verifyTipConditions() throws SlickException {
      if (this.attachedObject == null || !this.object.objectExists(this.attachedObject)) {
         this.clearAttachmentData();
         this.foundTipCondition = false;
         this.findTipConditions();
      }
   }
}
