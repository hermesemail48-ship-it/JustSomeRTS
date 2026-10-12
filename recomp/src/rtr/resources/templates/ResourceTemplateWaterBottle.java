package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateWaterBottle extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.waterBottle.single");
      this.namePlural = Text.getText("resource.waterBottle.plural");
      this.description = Text.getText("resource.waterBottle.description");
      this.resourceSlot = ResourceModule.ResourceSlot.WATER;
      this.trashResource = ResourceModule.ResourceType.TRASHY_TRASH;
      this.resourceCategory = ResourceModule.ResourceCategory.RECOVERY;
      this.baseValue = 1;
      this.dontAddMisc = true;
      this.usesMax = 30;
      this.canMergeUses = true;
      this.waterPerUse = 1;
      this.baseDurability = 1500;
   }
}
