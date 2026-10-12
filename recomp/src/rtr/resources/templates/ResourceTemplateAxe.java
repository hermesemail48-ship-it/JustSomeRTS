package rtr.resources.templates;

import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.resources.ResourceModule;

public class ResourceTemplateAxe extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.axe.single");
      this.namePlural = Text.getText("resource.axe.plural");
      this.description = Text.getText("resource.axe.description");
      this.baseResources.add(ResourceModule.ResourceType.BOARD);
      this.baseResources.add(ResourceModule.ResourceType.IRON_INGOT);
      this.trashResource = ResourceModule.ResourceType.TRASHY_TRASH;
      this.resourceSlot = ResourceModule.ResourceSlot.TOOL;
      this.resourceCategory = ResourceModule.ResourceCategory.TOOLS;
      this.itemLevel = 2;
      this.baseValue = 100;
      this.usesMax = 300;
      this.damagePercent = 20;
      this.damageType = MobBase.DamageType.SLASHING;
      this.baseDurability = 2000;
   }
}
