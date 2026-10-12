package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateBoiledEgg extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.boiledEgg.single");
      this.namePlural = Text.getText("resource.boiledEgg.plural");
      this.description = Text.getText("resource.boiledEgg.description");
      this.baseResources.add(ResourceModule.ResourceType.EGG);
      this.resourceSlot = ResourceModule.ResourceSlot.FOOD;
      this.trashResource = ResourceModule.ResourceType.ORGANICY_TRASH;
      this.resourceCategory = ResourceModule.ResourceCategory.FOOD_AND_WATER;
      this.itemLevel = 3;
      this.baseValue = 20;
      this.usesMax = 60;
      this.foodPerUse = 1;
      this.baseDurability = 1500;
   }
}
