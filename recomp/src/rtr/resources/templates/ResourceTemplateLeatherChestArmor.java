package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateLeatherChestArmor extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.leatherChest.single");
      this.namePlural = Text.getText("resource.leatherChest.plural");
      this.description = Text.getText("resource.leatherChest.description");
      this.baseResources.add(ResourceModule.ResourceType.RAW_HIDE);
      this.trashResource = ResourceModule.ResourceType.WOODY_TRASH;
      this.resourceSlot = ResourceModule.ResourceSlot.CHEST;
      this.resourceCategory = ResourceModule.ResourceCategory.ARMOR;
      this.baseValue = 40;
      this.usesMax = 250;
      this.armorPercent = 10;
      this.baseDurability = 2000;
   }
}
