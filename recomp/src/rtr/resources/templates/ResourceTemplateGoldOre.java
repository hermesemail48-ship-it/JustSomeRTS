package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateGoldOre extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.goldOre.single");
      this.namePlural = Text.getText("resource.goldOre.plural");
      this.description = Text.getText("resource.goldOre.description");
      this.trashResource = ResourceModule.ResourceType.TRASHY_TRASH;
      this.resourceSlot = ResourceModule.ResourceSlot.NONE;
      this.resourceCategory = ResourceModule.ResourceCategory.RAW;
      this.canTrade = false;
      this.baseValue = 120;
      this.baseDurability = 4000;
   }
}
