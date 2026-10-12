package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateCapturedAnimal extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.capturedAnimal.single");
      this.namePlural = Text.getText("resource.capturedAnimal.plural");
      this.description = Text.getText("resource.capturedAnimal.description");
      this.resourceSlot = ResourceModule.ResourceSlot.NONE;
      this.resourceCategory = ResourceModule.ResourceCategory.MISCELLANEOUS;
      this.canTrade = false;
      this.baseValue = 0;
      this.hideFromResourceBar = true;
      this.dontAddMisc = true;
      this.usesMax = 1;
      this.baseDurability = -1;
      this.destroyWhenDropped = true;
   }
}
