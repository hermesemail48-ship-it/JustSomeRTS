package rtr.resources.templates;

import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.resources.ResourceModule;

public class ResourceTemplateWoodSword extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.woodSword.single");
      this.namePlural = Text.getText("resource.woodSword.plural");
      this.description = Text.getText("resource.woodSword.description");
      this.baseResources.add(ResourceModule.ResourceType.BOARD);
      this.trashResource = ResourceModule.ResourceType.WOODY_TRASH;
      this.resourceSlot = ResourceModule.ResourceSlot.MELEE_WEAPON;
      this.resourceCategory = ResourceModule.ResourceCategory.WEAPONS;
      this.baseValue = 20;
      this.usesMax = 300;
      this.damagePercent = 25;
      this.damageType = MobBase.DamageType.SLASHING;
      this.combatSpeedPercent = 20;
      this.baseDurability = 2000;
   }
}
