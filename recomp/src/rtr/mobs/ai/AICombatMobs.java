package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.map.CollisionModule;
import rtr.map.MapTilesLoader;
import rtr.missiles.MissileBase;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.rangebuilder.RangeBuilder;
import rtr.resources.ResourceBase;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AICombatMobs extends AIBase {
   private static final int FIND_TARGET = 0;
   private static final int FLEE_TARGET = 1;
   private static final int PATHFIND_TO_TARGET = 2;
   private static final int ATTACK_TARGET = 3;
   private static AIBase resetBranch;
   private static AIBase inHomeBranch;

   public AICombatMobs(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_COMBAT_MOBS;
      this.interruptStuck = true;
      this.interruptDeath = true;
      this.interruptOnFire = true;
      this.interruptFrozen = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowCombat() && this.thisMobFlags.combatCanAttackMobs();
   }

   @Override
   public boolean tryRun() throws SlickException {
      int combatChance = this.thisMobFlags.getCombatAggroChance();
      if (this.thisMob.getMobJobType().getJob().isGuard()) {
         combatChance /= 4;
      }

      return this.allowRun() && Utilities.randomInt(combatChance) == 0 && this.getMobsInRange();
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         inHomeBranch = this.thisMob.getAIInHomeHiding();
         if (this.thisMob.hasCarriedResources()) {
            this.runDropResource();
         } else {
            switch (this.aiStep) {
               case 0:
                  this.findTarget();
                  break;
               case 1:
                  this.fleeTarget();
                  break;
               case 2:
                  this.pathfindToTarget();
                  break;
               case 3:
                  this.attackTarget();
            }
         }
      }
   }

   private void findTarget() throws SlickException {
      if (this.thisMob.getAssignment().getPrimaryMob() == null && !this.getMobsInRange()) {
         this.switchNode(resetBranch);
      } else {
         if (this.willFlee()) {
            this.aiStep = 1;
         } else {
            OrderedPair o = null;
            if (this.thisMob.canRangedAttack()) {
               o = this.getBestRangedCoordinates();
            } else {
               o = OrderedPair.getOrderedPair(this.thisMob.getAssignment().getPrimaryMob().getTileX(), this.thisMob.getAssignment().getPrimaryMob().getTileY());
            }

            if (o == null) {
               this.switchNode(resetBranch);
               return;
            }

            if (o == OrderedPair.getOrderedPair(this.thisMob.getTileX(), this.thisMob.getTileY())) {
               this.aiStep = 3;
            } else {
               this.requestPath(o);
               this.aiStep = 2;
            }
         }

         if (!this.reattack) {
            this.font.newFloater("iconExcite", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
            this.reattack = true;
         }
      }
   }

   private void fleeTarget() throws SlickException {
      Text.setVariableText("mobName", this.thisMob.getAssignment().getPrimaryMob().getFormattedName(true));
      this.thisMob.setCurrentTask(Text.getText("aiTask.combatMobs.fleeTarget"));
      this.runFleePath();
      if (this.pathFailed
         || this.pathCompleted
         || this.thisMob.getAssignment().getPrimaryMob() == null
         || !this.thisMob.getAssignment().getPrimaryMob().isAvailable()
         || Utilities.randomInt(this.thisMobFlags.getCombatFleeBreakawayChance()) == 0) {
         ObjectBase home = this.object.getObjects().get(this.thisMob.getAssignedHome());
         if (this.thisMobFlags.getAllowPanicHomeSearch()
            && this.thisMobFlags.getCanEnterHome()
            && home != null
            && this.object.getBuildingAtLocation(this.thisMob.getTileX(), this.thisMob.getTileY()) == home
            && this.thisMob.getTileX() == this.coordinates.getX()
            && this.thisMob.getTileY() == this.coordinates.getY()) {
            this.thisMob.enterBuilding();
            this.switchNode(inHomeBranch);
         } else {
            this.aiStep = 0;
         }

         this.thisMob.getAssignment().reset();
      }
   }

   private void pathfindToTarget() throws SlickException {
      Text.setVariableText("mobName", this.thisMob.getAssignment().getPrimaryMob().getFormattedName(true));
      this.thisMob.setCurrentTask(Text.getText("aiTask.combatMobs.pathfindToTarget"));
      if (!this.thisMob.canRangedAttack() && this.thisMob.canReach(this.thisMob.getAssignment().getPrimaryMob())) {
         this.followMob(this.thisMob.getAssignment().getPrimaryMob());
      } else {
         this.runPath();
      }

      if (this.pathFailed
         || this.thisMob.getAssignment().getPrimaryMob() == null
         || !this.thisMob.getAssignment().getPrimaryMob().isAvailable()
         || Utilities.randomInt(this.thisMobFlags.getCombatAttackBreakawayChance()) == 0) {
         this.aiStep = 0;
         this.thisMob.getAssignment().reset();
      } else if (this.pathCompleted) {
         this.aiStep = 3;
      }
   }

   private void attackTarget() throws SlickException {
      if (this.thisMob.getAssignment().getPrimaryMob() != null && this.thisMob.getAssignment().getPrimaryMob().isAvailable()) {
         if (this.thisMob.canRangedAttack()
            && !Utilities.coordinatesWithin(
               this.thisMob.getAssignment().getPrimaryMob().getTileX(),
               this.thisMob.getAssignment().getPrimaryMob().getTileY(),
               this.thisMob.getTileX(),
               this.thisMob.getTileY(),
               1
            )) {
            this.attackMobRanged();
         } else {
            this.attackMobMelee();
         }
      } else {
         this.switchNode(resetBranch);
         this.reattack = false;
      }
   }

   private void attackMobMelee() throws SlickException {
      if (Utilities.randomInt(this.thisMobFlags.getCombatAttackBreakawayChance()) == 0) {
         this.aiStep = 0;
         this.thisMob.getAssignment().reset();
      } else {
         int tileX = this.thisMob.getTileX();
         int tileY = this.thisMob.getTileY();
         float mobX = this.thisMob.getMobX();
         float mobY = this.thisMob.getMobY();
         if (Utilities.coordinatesWithin(
            this.thisMob.getAssignment().getPrimaryMob().getTileX(), this.thisMob.getAssignment().getPrimaryMob().getTileY(), tileX, tileY, 1
         )) {
            float directionX = Math.abs(this.thisMob.getAssignment().getPrimaryMob().getMobX() - mobX);
            float directionY = Math.abs(this.thisMob.getAssignment().getPrimaryMob().getMobY() - mobY);
            if (this.thisMob.getInventoryToolSlot() == null && this.thisMob.getInventoryMeleeWeaponSlot() == null) {
               this.thisMob.setPositionAttack();
            } else {
               this.thisMob.setPositionInteract(true, false);
            }

            this.thisMob.runCombatDelayTick(1);
            this.thisMob.doWork(2, 2, 1, 1, 0);
            if (directionX > directionY) {
               if (mobX > this.thisMob.getAssignment().getPrimaryMob().getMobX()) {
                  this.thisMob.setPosition(MobBase.MobPosition.WEST);
               } else {
                  this.thisMob.setPosition(MobBase.MobPosition.EAST);
               }
            } else if (mobY > this.thisMob.getAssignment().getPrimaryMob().getMobY()) {
               this.thisMob.setPosition(MobBase.MobPosition.NORTH);
            } else {
               this.thisMob.setPosition(MobBase.MobPosition.SOUTH);
            }

            if (this.thisMob.getCombatDelayTick() > this.thisMob.combatGetCalculatedAttackSpeed(false)) {
               this.thisMob.resetCombatDelayTick();
               if (this.thisMob.getInventoryMeleeWeaponSlot() != null) {
                  this.thisMob
                     .getAssignment()
                     .getPrimaryMob()
                     .takeDamage(
                        this.thisMob.combatGetCalculatedRandomMeleeDamage(),
                        (float)this.thisMob.getCenterPixelX(true),
                        (float)this.thisMob.getCenterPixelY(true),
                        this.thisMob,
                        this.thisMob.getInventoryMeleeWeaponSlot().getType().getTemplate().getDamageType(),
                        MobBase.DamageCause.COMBAT
                     );
                  this.thisMob.getInventoryMeleeWeaponSlot().decreaseUses(1);
               } else if (this.thisMob.getInventoryToolSlot() != null) {
                  this.thisMob
                     .getAssignment()
                     .getPrimaryMob()
                     .takeDamage(
                        this.thisMob.combatGetCalculatedRandomMeleeDamage(),
                        (float)this.thisMob.getCenterPixelX(true),
                        (float)this.thisMob.getCenterPixelY(true),
                        this.thisMob,
                        this.thisMob.getInventoryToolSlot().getType().getTemplate().getDamageType(),
                        MobBase.DamageCause.COMBAT
                     );
                  this.thisMob.getInventoryToolSlot().decreaseUses(2);
               } else {
                  this.thisMob
                     .getAssignment()
                     .getPrimaryMob()
                     .takeDamage(
                        this.thisMob.combatGetCalculatedRandomMeleeDamage(),
                        (float)this.thisMob.getCenterPixelX(true),
                        (float)this.thisMob.getCenterPixelY(true),
                        this.thisMob,
                        this.thisMobFlags.getCombatDamageType(),
                        MobBase.DamageCause.COMBAT
                     );
               }

               if (Utilities.randomInt(2) == 0) {
                  if (this.thisMob.getInventoryMeleeWeaponSlot() != null) {
                     this.sound.playSound(SoundModule.SoundType.HIT_WEAPON_GENERIC, this.thisMob.getMobX(), this.thisMob.getMobY());
                  } else if (this.thisMob.getInventoryToolSlot() != null) {
                     this.sound.playSound(SoundModule.SoundType.HIT_TOOL_GENERIC, this.thisMob.getMobX(), this.thisMob.getMobY());
                  } else {
                     SoundModule.SoundType hit = this.thisMobFlags.getMobSoundSet(this.thisMob.getGender()).getSoundHit();
                     if (hit != null) {
                        this.sound.playSound(hit, this.thisMob.getMobX(), this.thisMob.getMobY());
                     }
                  }
               }

               if (Utilities.randomInt(10) == 0) {
                  SoundModule.SoundType hit = this.thisMobFlags.getMobSoundSet(this.thisMob.getGender()).getSoundHitSweetener();
                  if (hit != null) {
                     this.sound.playSound(hit, this.thisMob.getMobX(), this.thisMob.getMobY());
                  }
               }

               if (Utilities.randomInt(5) == 0) {
                  SoundModule.SoundType hit = this.thisMobFlags.getMobSoundSet(this.thisMob.getGender()).getSoundHitSweetener2();
                  if (hit != null) {
                     this.sound.playSound(hit, this.thisMob.getMobX(), this.thisMob.getMobY());
                  }
               }

               this.thisMob.awardXP(Utilities.randomInt(3) + 3);
               if (this.thisMobFlags.getAllowGodExperience()) {
                  this.god.increaseGodXP(0.01, GodModule.GodXPType.COMBAT);
               }

               return;
            }
         } else if (Utilities.coordinatesWithin(
            this.thisMob.getAssignment().getPrimaryMob().getTileX(), this.thisMob.getAssignment().getPrimaryMob().getTileY(), tileX, tileY, 2
         )) {
            this.requestPath(this.thisMob.getAssignment().getPrimaryMob().getTileX(), this.thisMob.getAssignment().getPrimaryMob().getTileY());
            this.aiStep = 2;
         } else {
            this.aiStep = 0;
         }
      }
   }

   private void attackMobRanged() throws SlickException {
      if (Utilities.randomInt(this.thisMobFlags.getCombatAttackBreakawayChance()) == 0) {
         this.aiStep = 0;
         this.thisMob.getAssignment().reset();
      } else {
         float mobX = this.thisMob.getMobX();
         float mobY = this.thisMob.getMobY();
         boolean usingWeapon = false;
         ResourceBase weapon = this.thisMob.getInventoryRangedWeaponSlot();
         if (weapon != null && weapon.getType().getTemplate().getRangedWeaponRequiredAmmo() == null) {
            usingWeapon = true;
         }

         ResourceBase ammo = this.thisMob.getInventoryRangedWeaponAmmoSlot();
         if (weapon != null && ammo != null && weapon.getType().getTemplate().getRangedWeaponRequiredAmmo() == ammo.getType()) {
            usingWeapon = true;
         }

         float directionX = Math.abs(this.thisMob.getAssignment().getPrimaryMob().getMobX() - mobX);
         float directionY = Math.abs(this.thisMob.getAssignment().getPrimaryMob().getMobY() - mobY);
         int attackSpeed = this.thisMob.combatGetCalculatedAttackSpeed(true);
         if (Math.abs(this.thisMob.getCombatDelayTick() - attackSpeed) < 60) {
            if (this.thisMob.getInventoryToolSlot() == null && this.thisMob.getInventoryRangedWeaponSlot() == null) {
               this.thisMob.setPositionAttack();
            } else {
               this.thisMob.setPositionBowFire();
            }
         } else if (this.thisMob.getInventoryToolSlot() == null && this.thisMob.getInventoryRangedWeaponSlot() == null) {
            this.thisMob.setPositionFace();
         } else {
            this.thisMob.setPositionBowIdle();
         }

         this.thisMob.runCombatDelayTick(1);
         this.thisMob.doWork(2, 2, 1, 1, 0);
         if (directionX > directionY) {
            if (mobX > this.thisMob.getAssignment().getPrimaryMob().getMobX()) {
               this.thisMob.setPosition(MobBase.MobPosition.WEST);
            } else {
               this.thisMob.setPosition(MobBase.MobPosition.EAST);
            }
         } else if (mobY > this.thisMob.getAssignment().getPrimaryMob().getMobY()) {
            this.thisMob.setPosition(MobBase.MobPosition.NORTH);
         } else {
            this.thisMob.setPosition(MobBase.MobPosition.SOUTH);
         }

         if (this.thisMob.getCombatDelayTick() > attackSpeed) {
            this.thisMob.resetCombatDelayTick();
            this.thisMob.getSprite().restart();
            if (this.verifyRangedTarget(this.thisMob.getTileX(), this.thisMob.getTileY(), usingWeapon)) {
               MissileBase newMissile = null;
               if (usingWeapon) {
                  newMissile = this.missile
                     .newMissile(
                        this.thisMob.getCenterPixelX(true),
                        this.thisMob.getCenterPixelY(true),
                        this.thisMob,
                        this.thisMob.getAssignment().getPrimaryMob(),
                        this.thisMob.getInventoryRangedWeaponSlot().getType().getTemplate().getRangedWeaponMissileType()
                     );
               } else {
                  newMissile = this.missile
                     .newMissile(
                        this.thisMob.getCenterPixelX(true),
                        this.thisMob.getCenterPixelY(true),
                        this.thisMob,
                        this.thisMob.getAssignment().getPrimaryMob(),
                        this.thisMobFlags.getCombatRangedMissileType()
                     );
               }

               newMissile.overrideDamage(this.thisMob.combatGetCalculatedRandomRangedDamage());
               if (usingWeapon) {
                  weapon.decreaseUses(1);
                  if (ammo != null && this.thisMob.getInventoryRangedWeaponAmmoSlot().getType() == ammo.getType()) {
                     this.thisMob.getInventoryRangedWeaponAmmoSlot().decreaseUses(1);
                  }
               }

               this.thisMob.awardXP(Utilities.randomInt(3) + 3);
               if (this.thisMobFlags.getAllowGodExperience()) {
                  this.god.increaseGodXP(0.01, GodModule.GodXPType.COMBAT);
               }
            } else {
               OrderedPair o = this.getBestRangedCoordinates();
               if (o != null) {
                  this.requestPath(o);
                  this.aiStep = 2;
               } else {
                  this.aiStep = 0;
               }
            }
         }
      }
   }

   private boolean getMobsInRange() throws SlickException {
      return this.thisMob.canRangedAttack() ? this.getMobsInRangeRanged() : this.getMobsInRangeMelee();
   }

   private boolean getMobsInRangeRanged() throws SlickException {
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      int searchDistance = this.thisMobFlags.getCombatSearchDistance();
      boolean usingWeapon = false;
      ResourceBase weapon = this.thisMob.getInventoryRangedWeaponSlot();
      if (weapon != null && weapon.getType().getTemplate().getRangedWeaponRequiredAmmo() == null) {
         usingWeapon = true;
      }

      ResourceBase ammo = this.thisMob.getInventoryRangedWeaponAmmoSlot();
      if (weapon != null && ammo != null && weapon.getType().getTemplate().getRangedWeaponRequiredAmmo() == ammo.getType()) {
         usingWeapon = true;
      }

      CollisionModule.CollisionHeight lineOfSightHeight = this.thisMobFlags.getCombatRangedLineOfSight();
      if (usingWeapon) {
         lineOfSightHeight = this.thisMob.getInventoryRangedWeaponSlot().getType().getTemplate().getRangedWeaponLineOfSight();
      }

      ArrayList<MobBase> enemyList = new ArrayList<>();

      for (MobBase.MobGroup g : this.thisMob.getHostileMobGroups()) {
         enemyList.addAll(this.mob.getMobsNearby(tileX, tileY, searchDistance, g));
      }

      if (enemyList.size() > 0) {
         for (int x = enemyList.size() - 1; x >= 0; x--) {
            MobBase targetMob = enemyList.get(x);
            if (!targetMob.isAvailable()
               || this.collision
                  .checkParticleCollisionBetween(
                     this.thisMob.getCenterPixelX(true),
                     this.thisMob.getCenterPixelY(true),
                     targetMob.getCenterPixelX(true),
                     targetMob.getCenterPixelY(true),
                     lineOfSightHeight
                  )) {
               enemyList.remove(targetMob);
            }
         }
      }

      if (enemyList.size() <= 0) {
         return false;
      } else {
         ArrayList<MobBase> mobsInWalkingDistance = new ArrayList<>();
         if (enemyList.size() > 0) {
            mobsInWalkingDistance.addAll(enemyList);
            mobsInWalkingDistance = this.mob.cullMobsByDistance(tileX, tileY, searchDistance * 2, mobsInWalkingDistance, this.thisMobFlags.getBlockMapGroup());

            for (int xx = enemyList.size() - 1; xx >= 0; xx--) {
               MobBase targetMob = enemyList.get(xx);
               if (!mobsInWalkingDistance.contains(targetMob)
                  && this.collision
                     .checkParticleCollisionBetween(
                        this.thisMob.getCenterPixelX(true),
                        this.thisMob.getCenterPixelY(true),
                        targetMob.getCenterPixelX(true),
                        targetMob.getCenterPixelY(true),
                        lineOfSightHeight
                     )) {
                  enemyList.remove(targetMob);
               }
            }
         }

         if (enemyList.size() <= 0) {
            return false;
         } else {
            ArrayList<MobBase> enemyListIdeal = new ArrayList<>();
            enemyListIdeal.addAll(enemyList);
            ArrayList<MobBase> friendlyList = new ArrayList<>();

            for (MobBase.MobGroup g : this.thisMob.getFriendlyMobGroups()) {
               friendlyList.addAll(this.mob.getMobsNearby(tileX, tileY, this.thisMobFlags.getCombatSearchDistance(), g));
            }

            for (int i = friendlyList.size() - 1; i >= 0; i--) {
               MobBase f = friendlyList.get(i);
               if (f.getAssignment().getPrimaryMob() == null) {
                  friendlyList.remove(i);
               }
            }

            for (int ix = enemyList.size() - 1; ix >= 0; ix--) {
               MobBase m = enemyList.get(ix);
               int attackers = 0;

               for (MobBase f : friendlyList) {
                  if (f.getAssignment().getPrimaryMob() == m) {
                     enemyListIdeal.remove(m);
                     attackers++;
                  }

                  if (attackers > 5) {
                     enemyList.remove(m);
                     break;
                  }
               }
            }

            ArrayList<MobBase> finalEnemy = null;
            if (enemyListIdeal.size() > 0) {
               finalEnemy = enemyListIdeal;
            } else {
               finalEnemy = enemyList;
            }

            if (finalEnemy.size() > 0) {
               Assignment assignment = this.thisMob.getAssignment();
               OrderedPair mobSearch = this.pathFinder.searchForMob(tileX, tileY, finalEnemy, false, true, this.thisMobFlags.getBlockMapGroup());
               if (mobSearch != null) {
                  assignment.setPrimaryMob(this.pathFinder.mobFetch(finalEnemy, mobSearch.getX(), mobSearch.getY()));
                  if (!this.reattack && this.thisMob.getMobGroup() == MobBase.MobGroup.VILLAGER) {
                     this.callRangers();
                  }
               } else {
                  this.mob.sortMobsByDistance(tileX, tileY, finalEnemy);
                  assignment.setPrimaryMob(finalEnemy.get(0));
               }

               assignment.setAssignmentType(Assignment.AssignmentType.COMBAT);
               assignment.finalizeAssignment();
               return true;
            } else {
               return false;
            }
         }
      }
   }

   private OrderedPair getBestRangedCoordinates() throws SlickException {
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      boolean usingWeapon = false;
      ResourceBase weapon = this.thisMob.getInventoryRangedWeaponSlot();
      if (weapon != null && weapon.getType().getTemplate().getRangedWeaponRequiredAmmo() == null) {
         usingWeapon = true;
      }

      ResourceBase ammo = this.thisMob.getInventoryRangedWeaponAmmoSlot();
      if (weapon != null && ammo != null && weapon.getType().getTemplate().getRangedWeaponRequiredAmmo() == ammo.getType()) {
         usingWeapon = true;
      }

      int attackRange = this.thisMobFlags.getCombatRangedRange();
      if (usingWeapon) {
         attackRange = this.thisMob.getInventoryRangedWeaponSlot().getType().getTemplate().getRangedWeaponRange();
      }

      MobBase target = this.thisMob.getAssignment().getPrimaryMob();
      ArrayList<OrderedPair> coordinatesInRangeOfTarget = Utilities.getAllCoordinatesWithin(target.getTileX(), target.getTileY(), attackRange);
      ArrayList<OrderedPair> nearbyCoordinatesInWalkingDistanceOfMob = this.pathFinder
         .getCoordinatesInRange(tileX, tileY, attackRange, true, this.thisMobFlags.getBlockMapGroup());
      nearbyCoordinatesInWalkingDistanceOfMob.retainAll(coordinatesInRangeOfTarget);
      ArrayList<OrderedPair> allCoordinatesInWalkingDistanceOfMob = new ArrayList<>();
      ArrayList<OrderedPair> finalCoordinatesOut = null;
      if (nearbyCoordinatesInWalkingDistanceOfMob.size() == 0) {
         for (OrderedPair o : coordinatesInRangeOfTarget) {
            if (this.thisMob.canReach(o)) {
               allCoordinatesInWalkingDistanceOfMob.add(o);
            }
         }

         if (allCoordinatesInWalkingDistanceOfMob.size() == 0) {
            return null;
         }

         finalCoordinatesOut = allCoordinatesInWalkingDistanceOfMob;
      } else {
         finalCoordinatesOut = nearbyCoordinatesInWalkingDistanceOfMob;
      }

      CollisionModule.CollisionHeight lineOfSightHeight = this.thisMobFlags.getCombatRangedLineOfSight();
      if (usingWeapon) {
         lineOfSightHeight = this.thisMob.getInventoryRangedWeaponSlot().getType().getTemplate().getRangedWeaponLineOfSight();
      }

      ArrayList<OrderedPair> validCoordinates = new ArrayList<>();

      for (OrderedPair ox : finalCoordinatesOut) {
         if (!this.collision
            .checkParticleCollisionBetween(
               ox.getX() * 16 + 8, ox.getY() * 16 + 8, target.getCenterPixelX(true), target.getCenterPixelY(true), lineOfSightHeight
            )) {
            validCoordinates.add(ox);
         }
      }

      return validCoordinates.contains(OrderedPair.getOrderedPair(tileX, tileY))
         ? OrderedPair.getOrderedPair(tileX, tileY)
         : this.pathFinder.searchFor(tileX, tileY, validCoordinates, false, true, this.thisMobFlags.getBlockMapGroup());
   }

   private boolean getMobsInRangeMelee() throws SlickException {
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      int searchDistance = this.thisMobFlags.getCombatSearchDistance();
      ArrayList<MobBase> enemyList = new ArrayList<>();

      for (MobBase.MobGroup g : this.thisMob.getHostileMobGroups()) {
         enemyList.addAll(this.mob.getMobsNearby(tileX, tileY, searchDistance, g));
      }

      if (enemyList.size() > 0) {
         for (int x = enemyList.size() - 1; x >= 0; x--) {
            MobBase targetMob = enemyList.get(x);
            if (!targetMob.isAvailable() || !this.thisMob.canReach(targetMob)) {
               enemyList.remove(targetMob);
            }
         }
      }

      if (enemyList.size() <= 0) {
         return false;
      } else {
         enemyList = this.mob.cullMobsByDistance(tileX, tileY, searchDistance * 2, enemyList, this.thisMobFlags.getBlockMapGroup());
         if (enemyList.size() <= 0) {
            return false;
         } else {
            ArrayList<MobBase> enemyListIdeal = new ArrayList<>();
            enemyListIdeal.addAll(enemyList);
            ArrayList<MobBase> friendlyList = new ArrayList<>();

            for (MobBase.MobGroup g : this.thisMob.getFriendlyMobGroups()) {
               friendlyList.addAll(this.mob.getMobsNearby(tileX, tileY, this.thisMobFlags.getCombatSearchDistance(), g));
            }

            for (int i = friendlyList.size() - 1; i >= 0; i--) {
               MobBase f = friendlyList.get(i);
               if (f.getAssignment().getPrimaryMob() == null) {
                  friendlyList.remove(i);
               }
            }

            for (int ix = enemyList.size() - 1; ix >= 0; ix--) {
               MobBase m = enemyList.get(ix);
               int attackers = 0;

               for (MobBase f : friendlyList) {
                  if (f.getAssignment().getPrimaryMob() == m) {
                     enemyListIdeal.remove(m);
                     attackers++;
                  }

                  if (attackers > 5) {
                     enemyList.remove(m);
                     break;
                  }
               }
            }

            ArrayList<MobBase> finalEnemy = null;
            if (enemyListIdeal.size() > 0) {
               finalEnemy = enemyListIdeal;
            } else {
               finalEnemy = enemyList;
            }

            if (finalEnemy.size() > 0) {
               OrderedPair mobSearch = this.pathFinder.searchForMob(tileX, tileY, finalEnemy, false, true, this.thisMobFlags.getBlockMapGroup());
               if (mobSearch != null) {
                  Assignment assignment = this.thisMob.getAssignment();
                  assignment.setPrimaryMob(this.pathFinder.mobFetch(finalEnemy, mobSearch.getX(), mobSearch.getY()));
                  assignment.setAssignmentType(Assignment.AssignmentType.COMBAT);
                  assignment.finalizeAssignment();
                  if (!this.reattack && this.thisMob.getMobGroup() == MobBase.MobGroup.VILLAGER) {
                     this.callRangers();
                  }

                  return true;
               }
            }

            return false;
         }
      }
   }

   private boolean verifyRangedTarget(int tileX, int tileY, boolean usingWeapon) throws SlickException {
      MobBase targetMob = this.thisMob.getAssignment().getPrimaryMob();
      int attackRange = this.thisMobFlags.getCombatRangedRange();
      CollisionModule.CollisionHeight lineOfSightHeight = this.thisMobFlags.getCombatRangedLineOfSight();
      if (usingWeapon) {
         attackRange = this.thisMob.getInventoryRangedWeaponSlot().getType().getTemplate().getRangedWeaponRange();
         lineOfSightHeight = this.thisMob.getInventoryRangedWeaponSlot().getType().getTemplate().getRangedWeaponLineOfSight();
      }

      if (!targetMob.isAvailable()) {
         return false;
      } else if (this.collision
         .checkParticleCollisionBetween(tileX * 16 + 8, tileY * 16 + 8, targetMob.getCenterPixelX(true), targetMob.getCenterPixelY(true), lineOfSightHeight)) {
         return false;
      } else {
         ArrayList<OrderedPair> coordinatesInRangeOfTarget = Utilities.getAllCoordinatesWithin(targetMob.getTileX(), targetMob.getTileY(), attackRange);
         if (!coordinatesInRangeOfTarget.contains(OrderedPair.getOrderedPair(tileX, tileY))) {
            return false;
         } else {
            ArrayList<MobBase> mobList = new ArrayList<>();

            for (MobBase.MobGroup g : this.thisMob.getHostileMobGroups()) {
               mobList.addAll(this.mob.getMobsNearby(tileX, tileY, attackRange, g));
            }

            return mobList.contains(targetMob);
         }
      }
   }

   private void callRangers() throws SlickException {
      ArrayList<MobBase> allRangers = new ArrayList<>();
      allRangers.addAll(this.mob.getMobsByJob(MobJobBase.MobJobType.RANGER, false));
      ArrayList<MobBase> validRangers = new ArrayList<>();
      int rangerMax = 5;
      int count = 0;

      for (MobBase m : allRangers) {
         if (m.isAvailable()
            && m.getActiveNode() != m.getAICombatMobs()
            && m.getActiveNode() != m.getAICombatBuildings()
            && m.canReach(this.thisMob)
            && m.getHitPoints() > m.getHitPointsMax() * 0.5F
            && !m.isCompromised()
            && this.object.isInRange(m.getTileX(), m.getTileY())) {
            validRangers.add(m);
         }

         if (m.getAssignment().getSecondaryMob() == this.thisMob.getAssignment().getPrimaryMob()) {
            count++;
         }
      }

      if (validRangers.size() < rangerMax) {
         for (MobBase m : allRangers) {
            if (m.isAvailable() && m.getActiveNode() != m.getAICombatMobs() && m.getActiveNode() != m.getAICombatBuildings() && m.canReach(this.thisMob)) {
               validRangers.add(m);
            }

            if (m.getAssignment().getSecondaryMob() == this.thisMob.getAssignment().getPrimaryMob()) {
               count++;
            }
         }
      }

      if (count < rangerMax) {
         if (validRangers.size() > 0) {
            validRangers = this.mob.sortMobsByDistance(this.thisMob.getTileX(), this.thisMob.getTileY(), validRangers);

            for (int x = 0; x < validRangers.size() && x < rangerMax - count; x++) {
               validRangers.get(x).interruptNode(validRangers.get(x).getAIRangerSummoned());
               validRangers.get(x).getAssignment().setPrimaryMob(this.thisMob);
               validRangers.get(x).getAssignment().setSecondaryMob(this.thisMob.getAssignment().getPrimaryMob());
               validRangers.get(x).getAssignment().setAssignmentType(Assignment.AssignmentType.COMBAT);
               validRangers.get(x).getAssignment().finalizeAssignment();
            }
         }
      }
   }

   private boolean willFlee() throws SlickException {
      if (Utilities.randomInt(this.combatFleeChance()) != 0) {
         return false;
      } else {
         if (this.thisMobFlags.getCombatPrefersVillage()) {
            if (this.thisMobFlags.getAllowPanicHomeSearch() && this.thisMobFlags.getAllowHome() && this.thisMob.getAssignedHome() != -1) {
               ObjectBase home = this.object.getObjects().get(this.thisMob.getAssignedHome());
               if (home != null && this.thisMob.canReach(home)) {
                  this.coordinates = home.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
                  this.requestPath(this.coordinates);
                  return true;
               }
            }

            ObjectBase o = this.object.getFirstBaseTypeFound(MapTilesLoader.TileSet.CASTLE_1);
            ArrayList<ObjectBase> objectsNear = this.object.getObjectsNearby(this.thisMob, this.thisMobFlags.getCombatFleeSearchDistance());
            if (o != null && this.thisMob.canReach(o, true) && !objectsNear.contains(o)) {
               this.coordinates = o.getRandomCoordinates(this.thisMob, true, true);
               this.requestPath(this.coordinates);
               return true;
            }
         }

         this.coordinates = this.combatRandomFleeCoordinates();
         this.requestPath(this.coordinates);
         return true;
      }
   }

   private OrderedPair combatRandomFleeCoordinates() throws SlickException {
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      byte[][] blockMap = this.map.getMapData().getBlockMap(this.thisMobFlags.getBlockMapGroup());
      int[][] rangeMap = this.object.getRangeMap(RangeBuilder.RangeMapLayer.RANGE_AND_DESIRABILITY);
      if (blockMap[tileX][tileY] > 0) {
         return OrderedPair.getOrderedPair(tileX, tileY);
      } else {
         int radius = this.thisMobFlags.getCombatFleeRandomDistance();
         int villageCheckCount = 0;
         int randomX = tileX + Utilities.randomInt(radius * -1, radius);
         int randomY = tileY + Utilities.randomInt(radius * -1, radius);
         randomX = randomX > 255 ? 255 : (randomX < 0 ? 0 : randomX);

         for (randomY = randomY > 255 ? 255 : (randomY < 0 ? 0 : randomY);
            blockMap[randomX][randomY] > 0
               || villageCheckCount < 1000 && this.thisMobFlags.getCombatPrefersVillage() && rangeMap[randomX][randomY] > 0
               || !this.thisMob.canReach(randomX, randomY);
            villageCheckCount++
         ) {
            randomX = tileX + Utilities.randomInt(radius * -1, radius);
            randomY = tileY + Utilities.randomInt(radius * -1, radius);
            randomX = randomX > 255 ? 255 : (randomX < 0 ? 0 : randomX);
            randomY = randomY > 255 ? 255 : (randomY < 0 ? 0 : randomY);
         }

         return OrderedPair.getOrderedPair(randomX, randomY);
      }
   }

   private int combatFleeChance() throws SlickException {
      int friend = 0;
      int foe = 0;
      ArrayList<MobBase> mobList = this.mob.getMobsNearby(this.thisMob.getTileX(), this.thisMob.getTileY(), this.thisMobFlags.getCombatSearchDistance());

      for (int e = 0; e < mobList.size(); e++) {
         MobBase mobCheck = mobList.get(e);
         if (this.thisMob.canReach(mobCheck.getTileX(), mobCheck.getTileY()) && mobCheck.getID() != this.thisMob.getID() && mobCheck.isAvailable()) {
            if (this.thisMob.getFriendlyMobGroups().contains(mobCheck.getMobGroup())) {
               friend++;
            } else if (this.thisMob.getHostileMobGroups().contains(mobCheck.getMobGroup())) {
               foe++;
            }
         }
      }

      int thisChance = friend * 6 + (this.thisMob.getStrength() + this.thisMob.getDexterity()) / 10;
      if (this.thisMob.getHostileMobGroups().contains(MobBase.MobGroup.MONSTER)) {
         for (ObjectBase o : this.object.getObjectsNearby(this.thisMob.getTileX(), this.thisMob.getTileY(), 24, MobBase.MobGroup.MONSTER)) {
            if (o.getTower() != null && o.getTower().getTargetMob() == this.thisMob) {
               thisChance = (int)(thisChance * 0.25F);
            }
         }

         if (this.corruption.isTileCorrupted(this.thisMob.getTileX(), this.thisMob.getTileY())) {
            thisChance = (int)(thisChance * 0.15F);
         }
      }

      if (this.thisMob.getMobJobType().getJob().isGuard()) {
         thisChance = (int)(thisChance * 3.0F);
      }

      if (!this.thisMob.getMobJobType().getJob().isGuard()
         && this.thisMobFlags.getCombatPrefersVillage()
         && !this.object.isInRange(this.thisMob.getTileX(), this.thisMob.getTileY())) {
         thisChance = (int)(thisChance * 0.3F);
      }

      int hitPoints = this.thisMob.getHitPoints();
      int hitPointsMax = this.thisMob.getHitPointsMax();
      if (this.thisMob.isCompromised()) {
         thisChance = (int)(thisChance * 0.25F);
      }

      if (hitPoints < hitPointsMax / 10) {
         thisChance = (int)(thisChance * 0.015F);
      } else if (hitPoints < hitPointsMax / 8) {
         thisChance = (int)(thisChance * 0.025F);
      } else if (hitPoints < hitPointsMax / 4) {
         thisChance = (int)(thisChance * 0.1F);
      } else if (hitPoints < hitPointsMax / 2) {
         thisChance = (int)(thisChance * 0.2F);
      }

      MobBase combatTarget = this.thisMob.getAssignment().getPrimaryMob();
      int thatChance = foe * 6 + (combatTarget.getStrength() + combatTarget.getDexterity()) / 10;
      if (combatTarget.getHitPoints() < combatTarget.getHitPointsMax() / 8) {
         thatChance = (int)(thatChance * 0.1F);
      } else if (combatTarget.getHitPoints() < combatTarget.getHitPointsMax() / 4) {
         thatChance = (int)(thatChance * 0.3F);
      } else if (combatTarget.getHitPoints() < combatTarget.getHitPointsMax() / 2) {
         thatChance = (int)(thatChance * 0.5F);
      }

      int sum = thisChance - thatChance + this.thisMobFlags.getAggressiveness();
      int chance = 0;
      if (sum <= -25) {
         chance = 1;
      } else if (sum <= 1) {
         chance = 1;
      } else {
         chance = sum;
      }

      return chance;
   }
}
