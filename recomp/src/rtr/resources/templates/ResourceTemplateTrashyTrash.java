package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateTrashyTrash extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.trashyTrash.single");
      this.namePlural = Text.getText("resource.trashyTrash.plural");
      this.description = Text.getText("resource.trashyTrash.description");
      this.alwaysReturnResource = true;
      this.resourceSlot = ResourceModule.ResourceSlot.NONE;
      this.resourceCategory = ResourceModule.ResourceCategory.TRASH;
      this.canTrade = false;
      this.baseValue = 0;
      this.dontAddMisc = true;
      this.usesMax = 50;
      this.canMergeUses = true;
      this.trashPerUse = 1;
      this.baseDurability = -1;
   }
}
