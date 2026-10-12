package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateRawMeat extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.rawMeat.single");
      this.namePlural = Text.getText("resource.rawMeat.plural");
      this.description = Text.getText("resource.rawMeat.description");
      this.resourceSlot = ResourceModule.ResourceSlot.FOOD;
      this.trashResource = ResourceModule.ResourceType.ORGANICY_TRASH;
      this.resourceCategory = ResourceModule.ResourceCategory.FOOD_AND_WATER;
      this.itemLevel = 3;
      this.baseValue = 20;
      this.usesMax = 50;
      this.foodPerUse = 1;
      this.baseDurability = 1500;
   }
}
