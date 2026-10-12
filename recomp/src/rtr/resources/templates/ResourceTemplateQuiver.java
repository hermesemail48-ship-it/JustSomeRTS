package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateQuiver extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.quiver.single");
      this.namePlural = Text.getText("resource.quiver.plural");
      this.description = Text.getText("resource.quiver.description");
      this.baseResources.add(ResourceModule.ResourceType.WOOD);
      this.baseResources.add(ResourceModule.ResourceType.FEATHER);
      this.trashResource = ResourceModule.ResourceType.WOODY_TRASH;
      this.resourceSlot = ResourceModule.ResourceSlot.RANGED_WEAPON_AMMO;
      this.resourceCategory = ResourceModule.ResourceCategory.WEAPONS;
      this.baseValue = 30;
      this.usesMax = 120;
      this.baseDurability = 2000;
   }
}
