package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateRation extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.ration.single");
      this.namePlural = Text.getText("resource.ration.plural");
      this.description = Text.getText("resource.ration.description");
      this.baseResources.add(ResourceModule.ResourceType.COOKED_MEAT);
      this.baseResources.add(ResourceModule.ResourceType.RAW_VEGETABLE);
      this.trashResource = ResourceModule.ResourceType.ORGANICY_TRASH;
      this.resourceSlot = ResourceModule.ResourceSlot.FOOD;
      this.resourceCategory = ResourceModule.ResourceCategory.FOOD_AND_WATER;
      this.itemLevel = 5;
      this.baseValue = 60;
      this.usesMax = 120;
      this.foodPerUse = 1;
      this.baseDurability = 1500;
   }
}
