package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateCrystallyTrash extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.crystallyTrash.single");
      this.namePlural = Text.getText("resource.crystallyTrash.plural");
      this.description = Text.getText("resource.crystallyTrash.description");
      this.trashResource = ResourceModule.ResourceType.TRASHY_TRASH;
      this.trashProcessResource = ResourceModule.ResourceType.CRYSTAL;
      this.alwaysReturnResource = true;
      this.resourceSlot = ResourceModule.ResourceSlot.NONE;
      this.resourceCategory = ResourceModule.ResourceCategory.TRASH;
      this.canTrade = false;
      this.baseValue = 0;
      this.usesMax = 100;
      this.canMergeUses = true;
      this.trashPerUse = 1;
      this.baseDurability = 6000;
   }
}
