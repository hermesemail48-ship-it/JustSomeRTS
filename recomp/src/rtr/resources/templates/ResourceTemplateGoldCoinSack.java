package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateGoldCoinSack extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.goldCoinSack.single");
      this.namePlural = Text.getText("resource.goldCoinSack.plural");
      this.description = Text.getText("resource.goldCoinSack.description");
      this.baseResources.add(ResourceModule.ResourceType.GOLD_INGOT);
      this.trashResource = ResourceModule.ResourceType.TRASHY_TRASH;
      this.resourceSlot = ResourceModule.ResourceSlot.NONE;
      this.resourceCategory = ResourceModule.ResourceCategory.REFINED;
      this.canTrade = false;
      this.baseValue = 250;
      this.usesMax = 250;
      this.canMergeUses = true;
      this.baseDurability = 6000;
   }
}
