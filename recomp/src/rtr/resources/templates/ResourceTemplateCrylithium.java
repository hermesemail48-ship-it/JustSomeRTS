package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateCrylithium extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.crylithium.single");
      this.namePlural = Text.getText("resource.crylithium.plural");
      this.description = Text.getText("resource.crylithium.description");
      this.baseResources.add(ResourceModule.ResourceType.CRYSTAL);
      this.trashResource = ResourceModule.ResourceType.CRYSTALLY_TRASH;
      this.resourceSlot = ResourceModule.ResourceSlot.NONE;
      this.resourceCategory = ResourceModule.ResourceCategory.REFINED;
      this.baseValue = 20;
      this.baseDurability = 4000;
   }
}
