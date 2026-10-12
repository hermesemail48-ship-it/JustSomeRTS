package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateStoneBalls extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.stoneBalls.single");
      this.namePlural = Text.getText("resource.stoneBalls.plural");
      this.description = Text.getText("resource.stoneBalls.description");
      this.baseResources.add(ResourceModule.ResourceType.ROCK);
      this.trashResource = ResourceModule.ResourceType.ROCKY_TRASH;
      this.resourceSlot = ResourceModule.ResourceSlot.NONE;
      this.resourceCategory = ResourceModule.ResourceCategory.TOWER_AMMO;
      this.baseValue = 10;
      this.baseDurability = 2000;
   }
}
