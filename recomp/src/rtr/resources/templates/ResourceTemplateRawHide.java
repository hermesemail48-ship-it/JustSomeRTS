package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateRawHide extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.rawHide.single");
      this.namePlural = Text.getText("resource.rawHide.plural");
      this.description = Text.getText("resource.rawHide.description");
      this.trashResource = ResourceModule.ResourceType.TRASHY_TRASH;
      this.resourceSlot = ResourceModule.ResourceSlot.NONE;
      this.resourceCategory = ResourceModule.ResourceCategory.BYPRODUCT;
      this.baseValue = 20;
      this.baseDurability = 4000;
   }
}
