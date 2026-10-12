package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateCookedMeat extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.cookedMeat.single");
      this.namePlural = Text.getText("resource.cookedMeat.plural");
      this.description = Text.getText("resource.cookedMeat.description");
      this.baseResources.add(ResourceModule.ResourceType.RAW_MEAT);
      this.resourceSlot = ResourceModule.ResourceSlot.FOOD;
      this.trashResource = ResourceModule.ResourceType.ORGANICY_TRASH;
      this.resourceCategory = ResourceModule.ResourceCategory.FOOD_AND_WATER;
      this.itemLevel = 4;
      this.baseValue = 40;
      this.usesMax = 80;
      this.foodPerUse = 1;
      this.baseDurability = 1500;
   }
}
