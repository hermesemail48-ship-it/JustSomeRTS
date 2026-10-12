package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateLeatherHelmet extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.leatherHelmet.single");
      this.namePlural = Text.getText("resource.leatherHelmet.plural");
      this.description = Text.getText("resource.leatherHelmet.description");
      this.baseResources.add(ResourceModule.ResourceType.RAW_HIDE);
      this.trashResource = ResourceModule.ResourceType.WOODY_TRASH;
      this.resourceSlot = ResourceModule.ResourceSlot.HEAD;
      this.resourceCategory = ResourceModule.ResourceCategory.ARMOR;
      this.baseValue = 40;
      this.usesMax = 250;
      this.armorPercent = 5;
      this.baseDurability = 2000;
   }
}
