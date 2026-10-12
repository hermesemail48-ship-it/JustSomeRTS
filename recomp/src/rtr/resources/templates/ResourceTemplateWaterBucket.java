package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateWaterBucket extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.waterBucket.single");
      this.namePlural = Text.getText("resource.waterBucket.plural");
      this.description = Text.getText("resource.waterBucket.description");
      this.resourceSlot = ResourceModule.ResourceSlot.NONE;
      this.resourceCategory = ResourceModule.ResourceCategory.FOOD_AND_WATER;
      this.canTrade = false;
      this.baseValue = 1;
      this.dontAddMisc = true;
      this.usesMax = 40;
      this.canMergeUses = true;
      this.waterPerUse = 1;
      this.baseDurability = 2000;
   }
}
