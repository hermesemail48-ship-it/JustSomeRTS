package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateHealingPotion extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.healingPotion.single");
      this.namePlural = Text.getText("resource.healingPotion.plural");
      this.description = Text.getText("resource.healingPotion.description");
      this.trashResource = ResourceModule.ResourceType.TRASHY_TRASH;
      this.resourceSlot = ResourceModule.ResourceSlot.RECOVERY;
      this.resourceCategory = ResourceModule.ResourceCategory.RECOVERY;
      this.baseValue = 50;
      this.useTime = 5;
      this.healthPercentPerUse = 50;
      this.baseDurability = 2000;
   }
}
