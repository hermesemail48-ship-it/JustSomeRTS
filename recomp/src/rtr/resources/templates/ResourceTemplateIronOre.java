package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateIronOre extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.ironOre.single");
      this.namePlural = Text.getText("resource.ironOre.plural");
      this.description = Text.getText("resource.ironOre.description");
      this.trashResource = ResourceModule.ResourceType.TRASHY_TRASH;
      this.resourceSlot = ResourceModule.ResourceSlot.NONE;
      this.resourceCategory = ResourceModule.ResourceCategory.RAW;
      this.baseValue = 20;
      this.baseDurability = 4000;
   }
}
