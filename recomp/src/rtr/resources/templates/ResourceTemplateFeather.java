package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateFeather extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.feather.single");
      this.namePlural = Text.getText("resource.feather.plural");
      this.description = Text.getText("resource.feather.description");
      this.trashResource = ResourceModule.ResourceType.TRASHY_TRASH;
      this.resourceSlot = ResourceModule.ResourceSlot.NONE;
      this.resourceCategory = ResourceModule.ResourceCategory.BYPRODUCT;
      this.baseValue = 10;
      this.baseDurability = 4000;
   }
}
