package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateSilk extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.silk.single");
      this.namePlural = Text.getText("resource.silk.plural");
      this.description = Text.getText("resource.silk.description");
      this.trashResource = ResourceModule.ResourceType.TRASHY_TRASH;
      this.resourceSlot = ResourceModule.ResourceSlot.NONE;
      this.resourceCategory = ResourceModule.ResourceCategory.BYPRODUCT;
      this.baseValue = 20;
      this.baseDurability = 1500;
   }
}
