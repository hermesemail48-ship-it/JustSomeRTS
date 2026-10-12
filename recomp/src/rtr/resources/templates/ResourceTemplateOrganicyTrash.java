package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateOrganicyTrash extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.organicyTrash.single");
      this.namePlural = Text.getText("resource.organicyTrash.plural");
      this.description = Text.getText("resource.organicyTrash.description");
      this.trashResource = ResourceModule.ResourceType.TRASHY_TRASH;
      this.trashProcessResource = ResourceModule.ResourceType.RAW_VEGETABLE;
      this.alwaysReturnResource = true;
      this.resourceSlot = ResourceModule.ResourceSlot.NONE;
      this.resourceCategory = ResourceModule.ResourceCategory.TRASH;
      this.canTrade = false;
      this.baseValue = 0;
      this.dontAddMisc = true;
      this.usesMax = 100;
      this.canMergeUses = true;
      this.trashPerUse = 1;
      this.baseDurability = 6000;
   }
}
