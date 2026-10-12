package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateEerieVessel extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.eerieVessel.single");
      this.namePlural = Text.getText("resource.eerieVessel.plural");
      this.description = Text.getText("resource.eerieVessel.description");
      this.resourceSlot = ResourceModule.ResourceSlot.NONE;
      this.resourceCategory = ResourceModule.ResourceCategory.MISCELLANEOUS;
      this.canTrade = true;
      this.baseValue = 5000;
      this.dontAddMisc = true;
      this.baseDurability = 10000;
   }
}
