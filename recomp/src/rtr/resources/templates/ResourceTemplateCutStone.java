package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateCutStone extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.cutStone.single");
      this.namePlural = Text.getText("resource.cutStone.plural");
      this.description = Text.getText("resource.cutStone.description");
      this.baseResources.add(ResourceModule.ResourceType.ROCK);
      this.trashResource = ResourceModule.ResourceType.ROCKY_TRASH;
      this.resourceSlot = ResourceModule.ResourceSlot.NONE;
      this.resourceCategory = ResourceModule.ResourceCategory.REFINED;
      this.baseValue = 10;
      this.baseDurability = 4000;
   }
}
