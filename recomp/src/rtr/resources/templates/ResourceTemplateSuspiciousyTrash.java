package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateSuspiciousyTrash extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.suspiciousyTrash.single");
      this.namePlural = Text.getText("resource.suspiciousyTrash.plural");
      this.description = Text.getText("resource.suspiciousyTrash.description");
      this.trashResource = ResourceModule.ResourceType.TRASHY_TRASH;
      this.trashProcessResource = ResourceModule.ResourceType.SUSPICICIOUS_KEY;
      this.alwaysReturnResource = true;
      this.resourceSlot = ResourceModule.ResourceSlot.NONE;
      this.resourceCategory = ResourceModule.ResourceCategory.TRASH;
      this.canTrade = false;
      this.baseValue = 0;
      this.dontAddMisc = true;
      this.usesMax = 200;
      this.canMergeUses = true;
      this.trashPerUse = 1;
      this.baseDurability = 6000;
   }
}
