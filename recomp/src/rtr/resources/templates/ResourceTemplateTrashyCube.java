package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateTrashyCube extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.trashyCube.single");
      this.namePlural = Text.getText("resource.trashyCube.plural");
      this.description = Text.getText("resource.trashyCube.description");
      this.baseResources.add(ResourceModule.ResourceType.TRASHY_TRASH);
      this.trashResource = ResourceModule.ResourceType.TRASHY_CUBE;
      this.alwaysReturnResource = true;
      this.resourceSlot = ResourceModule.ResourceSlot.NONE;
      this.resourceCategory = ResourceModule.ResourceCategory.TRASH;
      this.canTrade = false;
      this.baseValue = 0;
      this.dontAddMisc = true;
      this.usesMax = 200;
      this.canMergeUses = true;
      this.baseDurability = -1;
   }
}
