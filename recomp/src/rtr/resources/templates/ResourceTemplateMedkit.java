package rtr.resources.templates;

import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.resources.ResourceModule;

public class ResourceTemplateMedkit extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.medkit.single");
      this.namePlural = Text.getText("resource.medkit.plural");
      this.description = Text.getText("resource.medkit.description");
      this.baseResources.add(ResourceModule.ResourceType.SILK);
      this.baseResources.add(ResourceModule.ResourceType.WOOD);
      this.trashResource = ResourceModule.ResourceType.TRASHY_TRASH;
      this.resourceSlot = ResourceModule.ResourceSlot.TOOL;
      this.resourceCategory = ResourceModule.ResourceCategory.TOOLS;
      this.itemLevel = 2;
      this.baseValue = 50;
      this.usesMax = 30;
      this.damageType = MobBase.DamageType.CRUSHING;
      this.healthPercentPerUse = 50;
      this.baseDurability = 2000;
   }
}
