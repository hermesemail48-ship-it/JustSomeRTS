package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateGoldIngot extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.goldIngot.single");
      this.namePlural = Text.getText("resource.goldIngot.plural");
      this.description = Text.getText("resource.goldIngot.description");
      this.baseResources.add(ResourceModule.ResourceType.GOLD_ORE);
      this.trashResource = ResourceModule.ResourceType.TRASHY_TRASH;
      this.resourceSlot = ResourceModule.ResourceSlot.NONE;
      this.resourceCategory = ResourceModule.ResourceCategory.REFINED;
      this.canTrade = false;
      this.baseValue = 240;
      this.baseDurability = 6000;
   }
}
