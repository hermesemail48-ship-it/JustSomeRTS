package rtr.resources.templates;

import java.util.ArrayList;
import rtr.map.CollisionModule;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.resources.ResourceModule;

public class ResourceTemplateBase {
   protected boolean loaded;
   protected String name;
   protected String namePlural;
   protected String description;
   protected ArrayList<ResourceModule.ResourceType> baseResources = new ArrayList<>();
   protected ResourceModule.ResourceType trashResource;
   protected ResourceModule.ResourceType trashProcessResource;
   protected boolean alwaysReturnResource;
   protected ResourceModule.ResourceSlot resourceSlot;
   protected ResourceModule.ResourceCategory resourceCategory;
   protected int itemLevel = 1;
   protected boolean canTrade = true;
   protected int baseValue = 1;
   protected boolean hideFromResourceBar;
   protected boolean dontAddMisc;
   protected int usesMax = 1;
   protected int useTime;
   protected boolean canMergeUses;
   protected int foodPerUse;
   protected int waterPerUse;
   protected int trashPerUse;
   protected int healthPercentPerUse;
   protected int rangedWeaponRange;
   protected MissileModule.MissileType rangedWeaponMissileType;
   protected CollisionModule.CollisionHeight rangedWeaponLineOfSight;
   protected ResourceModule.ResourceType rangedWeaponRequiredAmmo;
   protected int damagePercent;
   protected MobBase.DamageType damageType;
   protected int combatSpeedPercent;
   protected int armorPercent;
   protected int deflectChance;
   protected int baseDurability;
   protected boolean destroyWhenDropped;

   public void load() {
      this.loaded = true;
   }

   public boolean isLoaded() {
      return this.loaded;
   }

   public String getName() {
      return this.name;
   }

   public String getNamePlural() {
      return this.namePlural;
   }

   public String getDescription() {
      return this.description;
   }

   public ArrayList<ResourceModule.ResourceType> getBaseResources() {
      return this.baseResources;
   }

   public ResourceModule.ResourceType getTrashResource() {
      return this.trashResource;
   }

   public ResourceModule.ResourceType getTrashProcessResource() {
      return this.trashProcessResource;
   }

   public boolean alwaysReturnResource() {
      return this.alwaysReturnResource;
   }

   public ResourceModule.ResourceSlot getResourceSlot() {
      return this.resourceSlot;
   }

   public ResourceModule.ResourceCategory getResourceCategory() {
      return this.resourceCategory;
   }

   public int getItemLevel() {
      return this.itemLevel;
   }

   public boolean canTrade() {
      return this.canTrade;
   }

   public int getBaseValue() {
      return this.baseValue;
   }

   public boolean canHideFromResourceBar() {
      return this.hideFromResourceBar;
   }

   public boolean dontAddMisc() {
      return this.dontAddMisc;
   }

   public boolean canEquip() {
      return this.resourceSlot != ResourceModule.ResourceSlot.NONE;
   }

   public int getUsesMax() {
      return this.usesMax;
   }

   public int getUseTime() {
      return this.useTime;
   }

   public boolean canMergeUses() {
      return this.canMergeUses;
   }

   public int getFoodPerUse() {
      return this.foodPerUse;
   }

   public int getWaterPerUse() {
      return this.waterPerUse;
   }

   public int getTrashPerUse() {
      return this.trashPerUse;
   }

   public int getHealthPercentPerUse() {
      return this.healthPercentPerUse;
   }

   public int getRangedWeaponRange() {
      return this.rangedWeaponRange;
   }

   public MissileModule.MissileType getRangedWeaponMissileType() {
      return this.rangedWeaponMissileType;
   }

   public CollisionModule.CollisionHeight getRangedWeaponLineOfSight() {
      return this.rangedWeaponLineOfSight;
   }

   public ResourceModule.ResourceType getRangedWeaponRequiredAmmo() {
      return this.rangedWeaponRequiredAmmo;
   }

   public int getDamagePercent() {
      return this.damagePercent;
   }

   public MobBase.DamageType getDamageType() {
      return this.damageType;
   }

   public int getCombatSpeedPercent() {
      return this.combatSpeedPercent;
   }

   public int getArmorPercent() {
      return this.armorPercent;
   }

   public int getDeflectChance() {
      return this.deflectChance;
   }

   public int getBaseDurability() {
      return this.baseDurability;
   }

   public boolean isDestroyedWhenDropped() {
      return this.destroyWhenDropped;
   }
}
