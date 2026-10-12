package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.font.Text;
import rtr.map.CollisionModule;
import rtr.map.MapTilesLoader;
import rtr.missiles.MissileBase;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.rangebuilder.RangeBuilder;
import rtr.particles.ParticleModule;
import rtr.resources.ResourceBase;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AICombatBuildings extends AIBase {
   private static final int FIND_TARGET = 0;
   private static final int FLEE_TARGET = 1;
   private static final int PATHFIND_TO_TARGET = 2;
   private static final int ATTACK_TARGET = 3;
   private static AIBase resetBranch;
   private static AIBase inHomeBranch;

   public AICombatBuildings(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_COMBAT_BUILDINGS;
      this.interruptStuck = true;
      this.interruptDeath = true;
      this.interruptOnFire = true;
      this.interruptFrozen = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowCombat() && this.thisMobFlags.combatCanAttackBuildings();
   }

   @Override
   public boolean tryRun() throws SlickException {
      return this.allowRun() && Utilities.randomInt(this.thisMobFlags.getCombatAggroChance()) == 0 && this.getBuildingsInRange();
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
      if (this.thisMob.getAssignment().getPrimaryObject() == null && !this.getBuildingsInRange()) {
         this.switchNode(resetBranch);
      } else {
         if (this.willFlee()) {
            this.aiStep = 1;
         } else {
            OrderedPair o = null;
            if (this.thisMob.canRangedAttack()) {
               o = this.getBestRangedCoordinates();
            } else {
               o = this.thisMob.getAssignment().getPrimaryObject().getRandomCoordinates(this.thisMob, true, false);
               if (o == null) {
                  o = this.thisMob.getAssignment().getPrimaryObject().getRandomCoordinates(this.thisMob, false, false);
               }
            }

            if (o == null) {
               this.switchNode(resetBranch);
               return;
            }

            if (o != null && o == OrderedPair.getOrderedPair(this.thisMob.getTileX(), this.thisMob.getTileY())) {
               this.thisMob.getAssignment().setPrimaryCoordinates(o);
               this.aiStep = 3;
            } else {
               this.requestPath(o);
               this.thisMob.getAssignment().setPrimaryCoordinates(o);
               this.aiStep = 2;
            }
         }
      }
   }

   private void fleeTarget() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObject().getFormattedName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.combatBuildings.fleeTarget"));
      this.runFleePath();
      if (this.pathFailed
         || this.pathCompleted
         || this.thisMob.getAssignment().getPrimaryObject() == null
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
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObject().getFormattedName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.combatBuildings.pathfindToTarget"));
      this.runPath();
      if (this.pathCompleted) {
         this.aiStep = 3;
      } else if (this.pathFailed
         || !this.object.objectExists(this.thisMob.getAssignment().getPrimaryObject())
         || Utilities.randomInt(this.thisMobFlags.getCombatAttackBreakawayChance()) == 0) {
         this.aiStep = 0;
         this.thisMob.getAssignment().reset();
      }
   }

   private void attackTarget() throws SlickException {
      if (this.thisMob.canRangedAttack()) {
         this.attackBuildingRanged();
      } else {
         this.attackBuildingMelee();
      }
   }

   private void attackBuildingMelee() throws SlickException {
      ObjectBase building = this.object.getObjects().get(this.thisMob.getAssignment().getPrimaryObject().getID());
      if (building == null) {
         this.aiStep = 0;
         this.thisMob.getAssignment().reset();
      } else {
         int tileX = this.thisMob.getTileX();
         int tileY = this.thisMob.getTileY();
         OrderedPair o = this.thisMob.getAssignment().getPrimaryCoordinates();
         if (!Utilities.coordinatesWithin(o.getX(), o.getY(), tileX, tileY, 1)) {
            OrderedPair randomCoordinates = building.getRandomCoordinates(this.thisMob, true, false);
            if (randomCoordinates == null) {
               randomCoordinates = building.getRandomCoordinates(this.thisMob, false, false);
            }

            this.requestPath(randomCoordinates);
            this.thisMob.getAssignment().setPrimaryCoordinates(randomCoordinates);
            this.aiStep = 2;
         } else {
            if (this.thisMob.getInventoryToolSlot() == null && this.thisMob.getInventoryMeleeWeaponSlot() == null) {
               this.thisMob.setPositionAttack();
            } else {
               this.thisMob.setPositionInteract(true, false);
            }

            byte[][] blockMap = this.map.getMapData().getBlockMap(this.thisMobFlags.getBlockMapGroup());
            if (blockMap[tileX + 1][tileY] > 0 && building.isBuildingAtLocation(tileX + 1, tileY)) {
               this.thisMob.setPosition(MobBase.MobPosition.EAST);
            } else if (blockMap[tileX - 1][tileY] > 0 && building.isBuildingAtLocation(tileX - 1, tileY)) {
               this.thisMob.setPosition(MobBase.MobPosition.WEST);
            } else if (blockMap[tileX][tileY + 1] > 0 && building.isBuildingAtLocation(tileX, tileY + 1)) {
               this.thisMob.setPosition(MobBase.MobPosition.SOUTH);
            } else if (blockMap[tileX][tileY - 1] > 0 && building.isBuildingAtLocation(tileX, tileY - 1)) {
               this.thisMob.setPosition(MobBase.MobPosition.NORTH);
            }

            this.thisMob.runCombatDelayTick(1);
            this.thisMob.doWork(2, 2, 1, 1, 0);
            if (this.thisMob.getCombatDelayTick() > this.thisMob.combatGetCalculatedAttackSpeed(false)) {
               this.thisMob.resetCombatDelayTick();
               if (this.thisMob.getInventoryMeleeWeaponSlot() != null) {
                  building.takeDamage(
                     this.thisMob.combatGetCalculatedRandomMeleeDamage(),
                     this.thisMob,
                     this.thisMob.getInventoryMeleeWeaponSlot().getType().getTemplate().getDamageType(),
                     MobBase.DamageCause.COMBAT
                  );
                  this.thisMob.getInventoryMeleeWeaponSlot().decreaseUses(1);
               } else if (this.thisMob.getInventoryToolSlot() != null) {
                  building.takeDamage(
                     this.thisMob.combatGetCalculatedRandomMeleeDamage(),
                     this.thisMob,
                     this.thisMob.getInventoryToolSlot().getType().getTemplate().getDamageType(),
                     MobBase.DamageCause.COMBAT
                  );
                  this.thisMob.getInventoryToolSlot().decreaseUses(2);
               } else {
                  building.takeDamage(
                     this.thisMob.combatGetCalculatedRandomMeleeDamage(), this.thisMob, this.thisMobFlags.getCombatDamageType(), MobBase.DamageCause.COMBAT
                  );
               }

               this.thisMob.awardXP(Utilities.randomInt(3) + 3);
               if (this.thisMobFlags.getAllowGodExperience()) {
                  this.god.increaseGodXP(0.01, GodModule.GodXPType.COMBAT);
               }

               int attackX = tileX;
               int attackY = tileY;
               if (blockMap[tileX + 1][tileY] > 0 && building.isBuildingAtLocation(tileX + 1, tileY)) {
                  attackX = tileX + 1;
               } else if (blockMap[tileX - 1][tileY] > 0 && building.isBuildingAtLocation(tileX - 1, tileY)) {
                  attackX = tileX - 1;
               } else if (blockMap[tileX][tileY + 1] > 0 && building.isBuildingAtLocation(tileX, tileY + 1)) {
                  attackY = tileY + 1;
               } else if (blockMap[tileX][tileY - 1] > 0 && building.isBuildingAtLocation(tileX, tileY - 1)) {
                  attackY = tileY - 1;
               }

               double xDiff = this.thisMob.getCenterPixelX(true) - attackX * 16 + 8;
               double yDiff = this.thisMob.getCenterPixelY(true) - attackY * 16 + 8;
               float angle = (float)Math.toDegrees(Math.atan2(yDiff, xDiff));
               int randomParticles = Utilities.randomInt(2) + 1;

               for (int x = 0; x < randomParticles; x++) {
                  this.particle
                     .newParticleGroup(
                        this.thisMob.getCenterPixelX(true),
                        this.thisMob.getCenterPixelY(true),
                        angle,
                        ParticleModule.ParticleGroup.DUST,
                        ParticleModule.ParticleSet.BLACK
                     );
                  this.particle
                     .newParticleGroup(
                        this.thisMob.getCenterPixelX(true),
                        this.thisMob.getCenterPixelY(true),
                        ParticleModule.ParticleGroup.SMOKE_BELLOW,
                        ParticleModule.ParticleSet.GRAY
                     );
               }

               if (Utilities.randomInt(this.thisMobFlags.getCombatMeleeAttackBuildingRepositionChance()) == 0) {
                  OrderedPair randomCoordinates = building.getRandomCoordinates(this.thisMob, true, false);
                  if (randomCoordinates == null) {
                     randomCoordinates = building.getRandomCoordinates(this.thisMob, false, false);
                  }

                  this.requestPath(randomCoordinates);
                  this.thisMob.getAssignment().setPrimaryCoordinates(randomCoordinates);
                  this.aiStep = 2;
                  return;
               }
            }
         }
      }
   }

   private void attackBuildingRanged() throws SlickException {
      if (this.thisMob.getAssignment().getPrimaryObject() == null) {
         this.switchNode(resetBranch);
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

         float directionX = Math.abs(this.thisMob.getAssignment().getSecondaryCoordinates().getX() * 16 - mobX);
         float directionY = Math.abs(this.thisMob.getAssignment().getSecondaryCoordinates().getY() * 16 - mobY);
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
            if (mobX > this.thisMob.getAssignment().getSecondaryCoordinates().getX() * 16) {
               this.thisMob.setPosition(MobBase.MobPosition.WEST);
            } else {
               this.thisMob.setPosition(MobBase.MobPosition.EAST);
            }
         } else if (mobY > this.thisMob.getAssignment().getSecondaryCoordinates().getY() * 16) {
            this.thisMob.setPosition(MobBase.MobPosition.NORTH);
         } else {
            this.thisMob.setPosition(MobBase.MobPosition.SOUTH);
         }

         if (this.thisMob.getCombatDelayTick() > attackSpeed) {
            this.thisMob.resetCombatDelayTick();
            if (this.verifyRangedTarget(usingWeapon)) {
               MissileBase newMissile = null;
               Assignment assignment = this.thisMob.getAssignment();
               ObjectBase target = assignment.getPrimaryObject();
               OrderedPair coordinates = assignment.getSecondaryCoordinates();
               if (usingWeapon) {
                  newMissile = this.missile
                     .newMissile(
                        this.thisMob.getCenterPixelX(true),
                        this.thisMob.getCenterPixelY(true),
                        this.thisMob,
                        target,
                        coordinates.getX(),
                        coordinates.getY(),
                        this.thisMob.getInventoryRangedWeaponSlot().getType().getTemplate().getRangedWeaponMissileType()
                     );
               } else {
                  newMissile = this.missile
                     .newMissile(
                        this.thisMob.getCenterPixelX(true),
                        this.thisMob.getCenterPixelY(true),
                        this.thisMob,
                        target,
                        coordinates.getX(),
                        coordinates.getY(),
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

               if (!this.thisMob.canRangedAttack()) {
                  assignment.reset();
                  assignment.setPrimaryObject(target);
                  assignment.setPrimaryCoordinates(target.getRandomCoordinates());
                  assignment.setAssignmentType(Assignment.AssignmentType.COMBAT);
                  assignment.finalizeAssignment();
                  this.aiStep = 0;
               }
            } else {
               OrderedPair o = this.getBestRangedCoordinates();
               if (o != null) {
                  this.requestPath(o);
                  this.thisMob.getAssignment().setPrimaryCoordinates(o);
                  this.aiStep = 2;
               } else {
                  this.aiStep = 0;
                  this.thisMob.getAssignment().reset();
               }
            }
         }
      }
   }

   private boolean getBuildingsInRange() throws SlickException {
      return this.thisMob.canRangedAttack() ? this.getBuildingsInRangeRanged() : this.getBuildingsInRangeMelee();
   }

   private boolean getBuildingsInRangeRanged() throws SlickException {
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

      ArrayList<ObjectBase> enemyList = this.object.getObjectsNearby(tileX, tileY, searchDistance);
      ArrayList<OrderedPair> allMobCoordinates = Utilities.getAllCoordinatesWithin(tileX, tileY, searchDistance);

      for (int i = enemyList.size() - 1; i >= 0; i--) {
         ObjectBase objectCheck = enemyList.get(i);
         if (this.thisMob.getHostileMobGroups().contains(objectCheck.getObjectFlags().getMobGroup()) && !objectCheck.canNotBeAttacked()) {
            ArrayList<OrderedPair> allBuildingCoordinates = objectCheck.getAllCoordinates();
            allBuildingCoordinates.retainAll(allMobCoordinates);
            boolean foundTarget = false;

            for (OrderedPair o : allBuildingCoordinates) {
               if (!this.collision
                  .checkParticleCollisionBetween(
                     this.thisMob.getCenterPixelX(true),
                     this.thisMob.getCenterPixelY(true),
                     o.getX() * 16 + 8,
                     o.getY() * 16 + 8,
                     objectCheck,
                     lineOfSightHeight
                  )) {
                  foundTarget = true;
                  break;
               }
            }

            if (!foundTarget) {
               enemyList.remove(objectCheck);
            }
         } else {
            enemyList.remove(objectCheck);
         }
      }

      if (enemyList.size() <= 0) {
         return false;
      } else {
         OrderedPair buildingSearch = this.pathFinder.searchForObject(tileX, tileY, enemyList, false, true, this.thisMobFlags.getBlockMapGroup());
         Assignment assignment = this.thisMob.getAssignment();
         if (buildingSearch != null) {
            assignment.setPrimaryObject(this.object.getBuildingAtLocation(buildingSearch));
         } else {
            assignment.setPrimaryObject(enemyList.get(Utilities.randomInt(enemyList.size())));
         }

         ArrayList<OrderedPair> allBuildingCoordinates = assignment.getPrimaryObject().getAllCoordinates();
         ArrayList<OrderedPair> validBuildingCoordinates = new ArrayList<>();
         allBuildingCoordinates.retainAll(allMobCoordinates);

         for (OrderedPair ox : allBuildingCoordinates) {
            if (!this.collision
               .checkParticleCollisionBetween(
                  this.thisMob.getCenterPixelX(true),
                  this.thisMob.getCenterPixelY(true),
                  ox.getX() * 16 + 8,
                  ox.getY() * 16 + 8,
                  assignment.getPrimaryObject(),
                  lineOfSightHeight
               )) {
               validBuildingCoordinates.add(ox);
            }
         }

         assignment.setSecondaryCoordinates(validBuildingCoordinates.get(Utilities.randomInt(validBuildingCoordinates.size())));
         assignment.setAssignmentType(Assignment.AssignmentType.COMBAT);
         assignment.finalizeAssignment();
         return true;
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

      Assignment assignment = this.thisMob.getAssignment();
      ArrayList<OrderedPair> coordinatesInRangeOfTarget = Utilities.getAllCoordinatesWithin(
         assignment.getSecondaryCoordinates().getX(), assignment.getSecondaryCoordinates().getY(), attackRange
      );
      ArrayList<OrderedPair> coordinatesInWalkingDistanceOfMob = this.pathFinder
         .getCoordinatesInRange(tileX, tileY, attackRange, true, this.thisMobFlags.getBlockMapGroup());
      coordinatesInWalkingDistanceOfMob.retainAll(coordinatesInRangeOfTarget);
      if (coordinatesInWalkingDistanceOfMob.size() == 0) {
         return null;
      } else {
         CollisionModule.CollisionHeight lineOfSightHeight = this.thisMobFlags.getCombatRangedLineOfSight();
         if (usingWeapon) {
            lineOfSightHeight = this.thisMob.getInventoryRangedWeaponSlot().getType().getTemplate().getRangedWeaponLineOfSight();
         }

         ArrayList<OrderedPair> validCoordinates = new ArrayList<>();

         for (OrderedPair o : coordinatesInWalkingDistanceOfMob) {
            if (!this.collision
               .checkParticleCollisionBetween(
                  o.getX() * 16 + 8,
                  o.getY() * 16 + 8,
                  assignment.getSecondaryCoordinates().getX() * 16 + 8,
                  assignment.getSecondaryCoordinates().getY() * 16 + 8,
                  assignment.getPrimaryObject(),
                  lineOfSightHeight
               )) {
               validCoordinates.add(o);
            }
         }

         return validCoordinates.contains(OrderedPair.getOrderedPair(tileX, tileY))
            ? OrderedPair.getOrderedPair(tileX, tileY)
            : this.pathFinder.searchFor(tileX, tileY, validCoordinates, false, true, this.thisMobFlags.getBlockMapGroup());
      }
   }

   private boolean getBuildingsInRangeMelee() throws SlickException {
      ArrayList<ObjectBase> objectList = this.object.getObjectsNearby(this.thisMob, this.thisMobFlags.getCombatSearchDistance());

      for (int i = objectList.size() - 1; i >= 0; i--) {
         ObjectBase objectCheck = objectList.get(i);
         if (!this.thisMob.canReach(objectCheck)
            || !this.thisMob.getHostileMobGroups().contains(objectCheck.getObjectFlags().getMobGroup())
            || objectCheck.canNotBeAttacked()) {
            objectList.remove(objectCheck);
         }
      }

      if (objectList.size() > 0) {
         objectList = this.object
            .cullObjectsByDistance(
               this.thisMob.getTileX(),
               this.thisMob.getTileY(),
               this.thisMobFlags.getCombatSearchDistance() * 2,
               objectList,
               this.thisMobFlags.getBlockMapGroup()
            );
      }

      if (objectList.size() > 0) {
         OrderedPair objectSearch = this.pathFinder
            .searchForObject(this.thisMob.getTileX(), this.thisMob.getTileY(), objectList, false, true, this.thisMobFlags.getBlockMapGroup());
         if (objectSearch == null) {
            objectSearch = OrderedPair.getOrderedPair(objectList.get(0).getObjectX(), objectList.get(0).getObjectY());
         }

         if (objectSearch != null) {
            ObjectBase building = this.object.getBuildingAtLocation(objectSearch.getX(), objectSearch.getY());
            Assignment assignment = this.thisMob.getAssignment();
            assignment.setPrimaryObject(building);
            assignment.setAssignmentType(Assignment.AssignmentType.COMBAT);
            assignment.finalizeAssignment();
            return true;
         }
      }

      return false;
   }

   private boolean verifyRangedTarget(boolean usingWeapon) throws SlickException {
      ObjectBase targetBuilding = this.object.getObjects().get(this.thisMob.getAssignment().getPrimaryObject().getID());
      if (targetBuilding == null) {
         return false;
      } else {
         int tileX = this.thisMob.getTileX();
         int tileY = this.thisMob.getTileY();
         int attackRange = this.thisMobFlags.getCombatRangedRange();
         CollisionModule.CollisionHeight lineOfSightHeight = this.thisMobFlags.getCombatRangedLineOfSight();
         if (usingWeapon) {
            attackRange = this.thisMob.getInventoryRangedWeaponSlot().getType().getTemplate().getRangedWeaponRange();
            lineOfSightHeight = this.thisMob.getInventoryRangedWeaponSlot().getType().getTemplate().getRangedWeaponLineOfSight();
         }

         Assignment assignment = this.thisMob.getAssignment();
         OrderedPair targetCoordinates = assignment.getSecondaryCoordinates();
         if (this.collision
            .checkParticleCollisionBetween(
               this.thisMob.getCenterPixelX(true),
               this.thisMob.getCenterPixelY(true),
               targetCoordinates.getX() * 16 + 8,
               targetCoordinates.getY() * 16 + 8,
               assignment.getPrimaryObject(),
               lineOfSightHeight
            )) {
            return false;
         } else {
            ArrayList<ObjectBase> enemyList = this.object.getObjectsNearby(tileX, tileY, attackRange);
            if (!enemyList.contains(assignment.getPrimaryObject())) {
               return false;
            } else {
               ArrayList<OrderedPair> coordinatesInRangeOfTarget = Utilities.getAllCoordinatesWithin(
                  targetCoordinates.getX(), targetCoordinates.getY(), attackRange
               );
               return coordinatesInRangeOfTarget.contains(OrderedPair.getOrderedPair(tileX, tileY));
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

      ObjectBase combatTarget = this.thisMob.getAssignment().getPrimaryObject();
      int thatChance = foe * 6;
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
