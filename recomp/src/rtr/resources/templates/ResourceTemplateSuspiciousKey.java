package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateSuspiciousKey extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.suspiciousKey.single");
      this.namePlural = Text.getText("resource.suspiciousKey.plural");
      this.description = Text.getText("resource.suspiciousKey.description");
      this.resourceSlot = ResourceModule.ResourceSlot.NONE;
      this.trashResource = ResourceModule.ResourceType.SUSPICICIOUSY_TRASH;
      this.resourceCategory = ResourceModule.ResourceCategory.MISCELLANEOUS;
      this.baseValue = 150;
      this.dontAddMisc = true;
      this.usesMax = 1;
      this.baseDurability = -1;
   }
}
