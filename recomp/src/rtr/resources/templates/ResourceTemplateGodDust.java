package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateGodDust extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.godDust.single");
      this.namePlural = Text.getText("resource.godDust.plural");
      this.description = Text.getText("resource.godDust.description");
      this.alwaysReturnResource = true;
      this.resourceSlot = ResourceModule.ResourceSlot.NONE;
      this.resourceCategory = ResourceModule.ResourceCategory.MISCELLANEOUS;
      this.canTrade = true;
      this.baseValue = 100;
      this.dontAddMisc = true;
      this.baseDurability = 2500;
   }
}
