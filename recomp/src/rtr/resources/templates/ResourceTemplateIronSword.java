package rtr.resources.templates;

import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.resources.ResourceModule;

public class ResourceTemplateIronSword extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.ironSword.single");
      this.namePlural = Text.getText("resource.ironSword.plural");
      this.description = Text.getText("resource.ironSword.description");
      this.baseResources.add(ResourceModule.ResourceType.IRON_INGOT);
      this.trashResource = ResourceModule.ResourceType.TRASHY_TRASH;
      this.resourceSlot = ResourceModule.ResourceSlot.MELEE_WEAPON;
      this.resourceCategory = ResourceModule.ResourceCategory.WEAPONS;
      this.itemLevel = 2;
      this.baseValue = 80;
      this.usesMax = 600;
      this.damagePercent = 50;
      this.damageType = MobBase.DamageType.SLASHING;
      this.combatSpeedPercent = 20;
      this.baseDurability = 2000;
   }
}
