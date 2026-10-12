package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateGhostDust extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.ghostDust.single");
      this.namePlural = Text.getText("resource.ghostDust.plural");
      this.description = Text.getText("resource.ghostDust.description");
      this.alwaysReturnResource = true;
      this.resourceSlot = ResourceModule.ResourceSlot.NONE;
      this.resourceCategory = ResourceModule.ResourceCategory.MISCELLANEOUS;
      this.canTrade = true;
      this.baseValue = 200;
      this.dontAddMisc = true;
      this.baseDurability = 2500;
   }
}
