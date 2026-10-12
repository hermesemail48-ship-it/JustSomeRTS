package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateWood extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.wood.single");
      this.namePlural = Text.getText("resource.wood.plural");
      this.description = Text.getText("resource.wood.description");
      this.resourceSlot = ResourceModule.ResourceSlot.NONE;
      this.trashResource = ResourceModule.ResourceType.WOODY_TRASH;
      this.resourceCategory = ResourceModule.ResourceCategory.RAW;
      this.baseValue = 5;
      this.usesMax = 1;
      this.baseDurability = 2000;
   }
}
