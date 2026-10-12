package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateBoard extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.board.single");
      this.namePlural = Text.getText("resource.board.plural");
      this.description = Text.getText("resource.board.description");
      this.baseResources.add(ResourceModule.ResourceType.WOOD);
      this.trashResource = ResourceModule.ResourceType.WOODY_TRASH;
      this.resourceSlot = ResourceModule.ResourceSlot.NONE;
      this.resourceCategory = ResourceModule.ResourceCategory.REFINED;
      this.baseValue = 10;
      this.baseDurability = 2000;
   }
}
