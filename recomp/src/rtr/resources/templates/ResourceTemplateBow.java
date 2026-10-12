package rtr.resources.templates;

import rtr.font.Text;
import rtr.map.CollisionModule;
import rtr.missiles.MissileModule;
import rtr.resources.ResourceModule;

public class ResourceTemplateBow extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.bow.single");
      this.namePlural = Text.getText("resource.bow.plural");
      this.description = Text.getText("resource.bow.description");
      this.baseResources.add(ResourceModule.ResourceType.SILK);
      this.baseResources.add(ResourceModule.ResourceType.WOOD);
      this.trashResource = ResourceModule.ResourceType.WOODY_TRASH;
      this.resourceSlot = ResourceModule.ResourceSlot.RANGED_WEAPON;
      this.resourceCategory = ResourceModule.ResourceCategory.WEAPONS;
      this.baseValue = 50;
      this.usesMax = 200;
      this.rangedWeaponRange = 8;
      this.rangedWeaponMissileType = MissileModule.MissileType.ITEM_BOW_ARROW;
      this.rangedWeaponLineOfSight = CollisionModule.CollisionHeight.HIGH;
      this.rangedWeaponRequiredAmmo = ResourceModule.ResourceType.QUIVER;
      this.damagePercent = 100;
      this.combatSpeedPercent = 15;
      this.baseDurability = 2000;
   }
}
