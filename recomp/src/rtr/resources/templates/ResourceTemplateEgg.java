package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateEgg extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.egg.single");
      this.namePlural = Text.getText("resource.egg.plural");
      this.description = Text.getText("resource.egg.description");
      this.resourceSlot = ResourceModule.ResourceSlot.FOOD;
      this.trashResource = ResourceModule.ResourceType.ORGANICY_TRASH;
      this.resourceCategory = ResourceModule.ResourceCategory.FOOD_AND_WATER;
      this.itemLevel = 2;
      this.baseValue = 10;
      this.usesMax = 40;
      this.foodPerUse = 1;
      this.baseDurability = 1500;
   }
}
