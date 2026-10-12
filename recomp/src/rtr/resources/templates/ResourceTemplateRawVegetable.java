package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateRawVegetable extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.rawVegetable.single");
      this.namePlural = Text.getText("resource.rawVegetable.plural");
      this.description = Text.getText("resource.rawVegetable.description");
      this.resourceSlot = ResourceModule.ResourceSlot.FOOD;
      this.trashResource = ResourceModule.ResourceType.ORGANICY_TRASH;
      this.resourceCategory = ResourceModule.ResourceCategory.FOOD_AND_WATER;
      this.itemLevel = 1;
      this.baseValue = 10;
      this.usesMax = 25;
      this.foodPerUse = 1;
      this.baseDurability = 1500;
   }
}
