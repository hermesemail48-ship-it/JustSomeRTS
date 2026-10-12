package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateWoodyTrash extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.woodyTrash.single");
      this.namePlural = Text.getText("resource.woodyTrash.plural");
      this.description = Text.getText("resource.woodyTrash.description");
      this.trashResource = ResourceModule.ResourceType.TRASHY_TRASH;
      this.trashProcessResource = ResourceModule.ResourceType.WOOD;
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
