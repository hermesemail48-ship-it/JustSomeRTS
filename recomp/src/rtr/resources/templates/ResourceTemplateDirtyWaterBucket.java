package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateDirtyWaterBucket extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.dirtyWaterBucket.single");
      this.namePlural = Text.getText("resource.dirtyWaterBucket.plural");
      this.description = Text.getText("resource.dirtyWaterBucket.description");
      this.usesMax = 40;
      this.canMergeUses = true;
      this.resourceSlot = ResourceModule.ResourceSlot.NONE;
      this.resourceCategory = ResourceModule.ResourceCategory.FOOD_AND_WATER;
      this.canTrade = false;
      this.baseValue = 1;
      this.dontAddMisc = true;
      this.baseDurability = 2000;
   }
}
