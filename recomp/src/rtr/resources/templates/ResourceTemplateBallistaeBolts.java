package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateBallistaeBolts extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.ballistaeBolts.single");
      this.namePlural = Text.getText("resource.ballistaeBolts.plural");
      this.description = Text.getText("resource.ballistaeBolts.description");
      this.baseResources.add(ResourceModule.ResourceType.WOOD);
      this.trashResource = ResourceModule.ResourceType.WOODY_TRASH;
      this.resourceSlot = ResourceModule.ResourceSlot.NONE;
      this.resourceCategory = ResourceModule.ResourceCategory.TOWER_AMMO;
      this.baseValue = 10;
      this.baseDurability = 2000;
   }
}
