package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateCrystal extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.crystal.single");
      this.namePlural = Text.getText("resource.crystal.plural");
      this.description = Text.getText("resource.crystal.description");
      this.trashResource = ResourceModule.ResourceType.CRYSTALLY_TRASH;
      this.resourceSlot = ResourceModule.ResourceSlot.NONE;
      this.resourceCategory = ResourceModule.ResourceCategory.RAW;
      this.baseValue = 10;
      this.baseDurability = 4000;
   }
}
