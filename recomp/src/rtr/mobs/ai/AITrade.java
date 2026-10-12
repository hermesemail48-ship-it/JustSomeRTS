package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.Utilities;

public class AITrade extends AIBase {
   private static final int FIND_TRADE_PARTNER = 0;
   private static final int PATHFIND_TO_TRADE_PARTNER = 1;
   private static final int TRADE_EQUIPMENT = 2;
   private static AIBase resetBranch;

   public AITrade(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_TRADE;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowWander() && this.thisMobFlags.getAllowTrade();
   }

   @Override
   public boolean tryRun() {
      return this.allowRun() && Utilities.randomInt(this.thisMobFlags.getTradeChance()) == 0;
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         if (this.runDropResource()) {
            switch (this.aiStep) {
               case 0:
                  this.findTradePartner();
                  break;
               case 1:
                  this.pathfindToTradePartner();
                  break;
               case 2:
                  this.tradeEquipment();
            }
         }
      }
   }

   private void findTradePartner() throws SlickException {
      ArrayList<ResourceModule.ResourceType> preferredTools = this.thisMob.getMobJobType().getJob().getPreferredTools();
      boolean hasPerferredHelm = this.isPreferred(preferredTools, this.thisMob.getInventoryHeadSlot());
      boolean hasPerferredChest = this.isPreferred(preferredTools, this.thisMob.getInventoryChestSlot());
      boolean hasPerferredMeleeWeapon = this.isPreferred(preferredTools, this.thisMob.getInventoryMeleeWeaponSlot());
      boolean hasPerferredTool = this.isPreferred(preferredTools, this.thisMob.getInventoryToolSlot());
      boolean hasPerferredShield = this.isPreferred(preferredTools, this.thisMob.getInventoryShieldSlot());
      boolean hasPerferredRecovery = this.isPreferred(preferredTools, this.thisMob.getInventoryRecoverySlot());
      boolean hasPerferredWater = this.isPreferred(preferredTools, this.thisMob.getInventoryWaterSlot());
      boolean hasPerferredFood = this.isPreferred(preferredTools, this.thisMob.getInventoryFoodSlot());
      boolean hasPerferredRangedWeapon = this.isPreferred(preferredTools, this.thisMob.getInventoryRangedWeaponSlot());
      boolean hasPerferredRangedWeaponAmmo = hasPerferredRangedWeapon && this.isPreferred(preferredTools, this.thisMob.getInventoryRangedWeaponAmmoSlot());
      boolean needsFood = this.needsFood(this.thisMob);
      boolean needsWater = this.needsWater(this.thisMob);
      ArrayList<MobBase> mobsWithHelmet = new ArrayList<>();
      ArrayList<MobBase> mobsWithChest = new ArrayList<>();
      ArrayList<MobBase> mobsWithMeleeWeapon = new ArrayList<>();
      ArrayList<MobBase> mobsWithTool = new ArrayList<>();
      ArrayList<MobBase> mobsWithShield = new ArrayList<>();
      ArrayList<MobBase> mobsWithRecovery = new ArrayList<>();
      ArrayList<MobBase> mobsWithWater = new ArrayList<>();
      ArrayList<MobBase> mobsWithFood = new ArrayList<>();
      ArrayList<MobBase> mobsWithRangedWeapon = new ArrayList<>();
      ArrayList<MobBase> mobsWithRangedWeaponAmmo = new ArrayList<>();

      for (MobBase m : this.mob.getMobsByGroup(this.thisMob.getMobGroup(), false)) {
         if (m != this.thisMob && this.thisMob.canReach(m) && m.isAvailable()) {
            ArrayList<ResourceModule.ResourceType> checkPerferredTools = m.getMobJobType().getJob().getPreferredTools();
            if (!hasPerferredHelm
               && m.getInventoryHeadSlot() != null
               && !checkPerferredTools.contains(m.getInventoryHeadSlot().getType())
               && preferredTools.contains(m.getInventoryHeadSlot().getType())) {
               mobsWithHelmet.add(m);
            }

            if (!hasPerferredChest
               && m.getInventoryChestSlot() != null
               && !checkPerferredTools.contains(m.getInventoryChestSlot().getType())
               && preferredTools.contains(m.getInventoryChestSlot().getType())) {
               mobsWithChest.add(m);
            }

            if (!hasPerferredMeleeWeapon
               && m.getInventoryMeleeWeaponSlot() != null
               && !checkPerferredTools.contains(m.getInventoryMeleeWeaponSlot().getType())
               && preferredTools.contains(m.getInventoryMeleeWeaponSlot().getType())) {
               mobsWithMeleeWeapon.add(m);
            }

            if (!hasPerferredTool
               && m.getInventoryToolSlot() != null
               && !checkPerferredTools.contains(m.getInventoryToolSlot().getType())
               && preferredTools.contains(m.getInventoryToolSlot().getType())) {
               mobsWithTool.add(m);
            }

            if (!hasPerferredShield
               && m.getInventoryShieldSlot() != null
               && !checkPerferredTools.contains(m.getInventoryShieldSlot().getType())
               && preferredTools.contains(m.getInventoryShieldSlot().getType())) {
               mobsWithShield.add(m);
            }

            if (!hasPerferredRecovery
               && m.getInventoryRecoverySlot() != null
               && !checkPerferredTools.contains(m.getInventoryRecoverySlot().getType())
               && preferredTools.contains(m.getInventoryRecoverySlot().getType())) {
               mobsWithRecovery.add(m);
            }

            if (m.getInventoryWaterSlot() != null && (!m.getMobFlags().getAllowThirst() || m.getThirst() > m.getMobFlags().getThirstThirstyWaterSearchLevel())) {
               if (!hasPerferredWater
                  && !checkPerferredTools.contains(m.getInventoryWaterSlot().getType())
                  && preferredTools.contains(m.getInventoryWaterSlot().getType())) {
                  mobsWithWater.add(m);
               } else if (needsWater && m.getInventoryWaterSlot() != null) {
                  mobsWithWater.add(m);
               }
            }

            if (m.getInventoryFoodSlot() != null && (!m.getMobFlags().getAllowHunger() || m.getHunger() > m.getMobFlags().getHungerHungryFoodSearchLevel())) {
               if (!hasPerferredFood
                  && !checkPerferredTools.contains(m.getInventoryFoodSlot().getType())
                  && preferredTools.contains(m.getInventoryFoodSlot().getType())) {
                  mobsWithFood.add(m);
               } else if (needsFood && m.getInventoryFoodSlot() != null) {
                  mobsWithFood.add(m);
               }
            }

            if (!hasPerferredRangedWeapon
               && m.getInventoryRangedWeaponSlot() != null
               && !checkPerferredTools.contains(m.getInventoryRangedWeaponSlot().getType())
               && preferredTools.contains(m.getInventoryRangedWeaponSlot().getType())) {
               mobsWithRangedWeapon.add(m);
            }

            if (!hasPerferredRangedWeaponAmmo
               && m.getInventoryRangedWeaponAmmoSlot() != null
               && this.thisMob.getInventoryRangedWeaponSlot() != null
               && this.thisMob.getInventoryRangedWeaponSlot().getType().getTemplate().getRangedWeaponRequiredAmmo()
                  == m.getInventoryRangedWeaponAmmoSlot().getType()
               && !checkPerferredTools.contains(m.getInventoryRangedWeaponAmmoSlot().getType())
               && preferredTools.contains(m.getInventoryRangedWeaponAmmoSlot().getType())) {
               mobsWithRangedWeaponAmmo.add(m);
            }
         }
      }

      ArrayList<MobBase> mobsByAssignment = this.mob.getMobsByAssignment(Assignment.AssignmentType.TRADING, false);
      boolean foundWork = false;
      if (!hasPerferredHelm && this.checkForEquipment(mobsWithHelmet, mobsByAssignment, ResourceModule.ResourceSlot.HEAD)) {
         foundWork = true;
      } else if (!hasPerferredChest && this.checkForEquipment(mobsWithChest, mobsByAssignment, ResourceModule.ResourceSlot.CHEST)) {
         foundWork = true;
      } else if (!hasPerferredMeleeWeapon && this.checkForEquipment(mobsWithMeleeWeapon, mobsByAssignment, ResourceModule.ResourceSlot.MELEE_WEAPON)) {
         foundWork = true;
      } else if (!hasPerferredTool && this.checkForEquipment(mobsWithTool, mobsByAssignment, ResourceModule.ResourceSlot.TOOL)) {
         foundWork = true;
      } else if (!hasPerferredShield && this.checkForEquipment(mobsWithShield, mobsByAssignment, ResourceModule.ResourceSlot.SHIELD)) {
         foundWork = true;
      } else if (!hasPerferredRecovery && this.checkForEquipment(mobsWithRecovery, mobsByAssignment, ResourceModule.ResourceSlot.RECOVERY)) {
         foundWork = true;
      } else if ((!hasPerferredWater || needsWater) && this.checkForEquipment(mobsWithWater, mobsByAssignment, ResourceModule.ResourceSlot.WATER)) {
         foundWork = true;
      } else if ((!hasPerferredFood || needsFood) && this.checkForEquipment(mobsWithFood, mobsByAssignment, ResourceModule.ResourceSlot.FOOD)) {
         foundWork = true;
      } else if (!hasPerferredRangedWeapon && this.checkForEquipment(mobsWithRangedWeapon, mobsByAssignment, ResourceModule.ResourceSlot.RANGED_WEAPON)) {
         foundWork = true;
      } else if (!hasPerferredRangedWeaponAmmo
         && this.checkForEquipment(mobsWithRangedWeaponAmmo, mobsByAssignment, ResourceModule.ResourceSlot.RANGED_WEAPON_AMMO)) {
         foundWork = true;
      }

      if (foundWork) {
         this.requestPath(this.thisMob.getTradeTarget().getTileX(), this.thisMob.getTradeTarget().getTileY());
         this.aiStep = 1;
      } else {
         this.switchNode(resetBranch);
      }
   }

   private void pathfindToTradePartner() throws SlickException {
      Text.setVariableText("resourceName", this.thisMob.getAssignment().getPickupResourceType().getTemplate().getName());
      Text.setVariableText("mobName", this.thisMob.getTradeTarget().getFormattedName(true));
      this.thisMob.setCurrentTask(Text.getText("aiTask.trade.pathfindToTradePartner"));
      this.followMob(this.thisMob.getTradeTarget());
      if (!this.pathFailed && this.thisMob.getTradeTarget() != null) {
         if (this.pathCompleted) {
            this.aiStep = 2;
         }
      } else {
         this.aiStep = 0;
         this.thisMob.setTradeTarget(null);
      }
   }

   private void tradeEquipment() throws SlickException {
      MobBase tradeMob = this.thisMob.getTradeTarget();
      Assignment assignment = this.thisMob.getAssignment();
      ResourceBase r = tradeMob.unequipResource(assignment.getPickupResourceType().getTemplate().getResourceSlot());
      if (r != null) {
         ResourceBase r2 = this.thisMob.unequipResource(assignment.getPickupResourceType().getTemplate().getResourceSlot());
         if (r2 != null) {
            tradeMob.equipResource(r2, r2.getType().getTemplate().getResourceSlot());
         }

         this.thisMob.equipResource(r, r.getType().getTemplate().getResourceSlot());
         this.thisMob.awardXP(Utilities.randomInt(5) + 5);
         tradeMob.awardXP(Utilities.randomInt(5) + 5);
         if (this.thisMobFlags.getAllowGodExperience()) {
            this.god.increaseGodXP(0.01, GodModule.GodXPType.TRADING_EQUIPMENT);
         }

         this.getFloater(r.getType().getTemplate().getResourceSlot());
      }

      this.thisMob.getTradeTarget().interruptNode(this.thisMob.getTradeTarget().getAIStart());
      this.thisMob.setTradeTarget(null);
      assignment.reset();
      this.aiStep = 0;
   }

   private boolean needsFood(MobBase m) {
      return !this.thisMob.hasFoodResource()
         && m.getMobFlags().getAllowHunger()
         && m.getHunger() <= m.getMobFlags().getHungerHungryFoodSearchLevel()
         && m.getMobFlags().getCanInventoryEquipFood()
         && m.getInventoryFoodSlot() == null;
   }

   private boolean needsWater(MobBase m) {
      return !m.hasWaterResource()
         && m.getMobFlags().getAllowThirst()
         && m.getThirst() <= m.getMobFlags().getThirstThirstyWaterSearchLevel()
         && m.getMobFlags().getCanInventoryEquipWater()
         && m.getInventoryWaterSlot() == null;
   }

   private boolean isPreferred(ArrayList<ResourceModule.ResourceType> preferredTools, ResourceBase r) {
      return r != null && preferredTools.contains(r.getType());
   }

   private boolean checkForEquipment(ArrayList<MobBase> mobsList, ArrayList<MobBase> assignmentList, ResourceModule.ResourceSlot slot) {
      if (mobsList.size() > 0) {
         label25:
         for (MobBase m : this.mob.sortMobsByDistance(this.thisMob.getTileX(), this.thisMob.getTileY(), mobsList)) {
            for (MobBase m2 : assignmentList) {
               if (m2.getAssignment().getPrimaryMob() == m && m2.getAssignment().getPickupResourceType() == m.getResourceInInventory(slot).getType()) {
                  continue label25;
               }
            }

            this.thisMob.setTradeTarget(m);
            Assignment assignment = this.thisMob.getAssignment();
            assignment.reset();
            assignment.setPrimaryMob(m);
            assignment.addPickupResources(m.getResourceInInventory(slot));
            assignment.setPickupResourceType(m.getResourceInInventory(slot).getType());
            assignment.setAssignmentType(Assignment.AssignmentType.TRADING);
            assignment.finalizeAssignment();
            return true;
         }
      }

      return false;
   }

   private void getFloater(ResourceModule.ResourceSlot s) throws SlickException {
      switch (s) {
         case HEAD:
            this.font.newFloater("iconHeadSlot", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
            break;
         case CHEST:
            this.font.newFloater("iconChestSlot", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
            break;
         case MELEE_WEAPON:
            this.font.newFloater("iconMeleeWeaponSlot", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
            break;
         case SHIELD:
            this.font.newFloater("iconShieldSlot", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
            break;
         case TOOL:
            this.font.newFloater("iconToolSlot", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
            break;
         case RECOVERY:
            this.font.newFloater("iconRecoverySlot", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
            break;
         case WATER:
            this.font.newFloater("iconWaterSlot", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
            break;
         case FOOD:
            this.font.newFloater("iconFoodSlot", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
            break;
         case RANGED_WEAPON:
            this.font.newFloater("iconRangedWeaponSlot", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
            break;
         case RANGED_WEAPON_AMMO:
            this.font.newFloater("iconRangedWeaponAmmoSlot", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
      }
   }
}
