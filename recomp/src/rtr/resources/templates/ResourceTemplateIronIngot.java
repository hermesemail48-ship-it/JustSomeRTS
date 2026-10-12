package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateIronIngot extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.ironIngot.single");
      this.namePlural = Text.getText("resource.ironIngot.plural");
      this.description = Text.getText("resource.ironIngot.description");
      this.baseResources.add(ResourceModule.ResourceType.IRON_ORE);
      this.trashResource = ResourceModule.ResourceType.TRASHY_TRASH;
      this.resourceSlot = ResourceModule.ResourceSlot.NONE;
      this.resourceCategory = ResourceModule.ResourceCategory.REFINED;
      this.baseValue = 40;
      this.baseDurability = 4000;
   }
}
