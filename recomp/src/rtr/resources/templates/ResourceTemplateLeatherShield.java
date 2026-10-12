package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateLeatherShield extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.leatherShield.single");
      this.namePlural = Text.getText("resource.leatherShield.plural");
      this.description = Text.getText("resource.leatherShield.description");
      this.baseResources.add(ResourceModule.ResourceType.RAW_HIDE);
      this.trashResource = ResourceModule.ResourceType.TRASHY_TRASH;
      this.resourceSlot = ResourceModule.ResourceSlot.SHIELD;
      this.resourceCategory = ResourceModule.ResourceCategory.ARMOR;
      this.baseValue = 40;
      this.usesMax = 200;
      this.deflectChance = 8;
      this.baseDurability = 2000;
   }
}
