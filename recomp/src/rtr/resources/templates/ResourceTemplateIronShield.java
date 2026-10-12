package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateIronShield extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.ironShield.single");
      this.namePlural = Text.getText("resource.ironShield.plural");
      this.description = Text.getText("resource.ironShield.description");
      this.baseResources.add(ResourceModule.ResourceType.RAW_HIDE);
      this.baseResources.add(ResourceModule.ResourceType.IRON_INGOT);
      this.trashResource = ResourceModule.ResourceType.TRASHY_TRASH;
      this.resourceSlot = ResourceModule.ResourceSlot.SHIELD;
      this.resourceCategory = ResourceModule.ResourceCategory.ARMOR;
      this.itemLevel = 2;
      this.baseValue = 120;
      this.usesMax = 400;
      this.deflectChance = 4;
      this.baseDurability = 2000;
   }
}
