package rtr.objects.towerhead;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.missiles.MissileBase;
import rtr.objects.ObjectBase;
import rtr.objects.ObjectModule;
import rtr.resources.ResourceBase;
import rtr.save.YMLDataMap;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class TowerAttract extends TowerBase {
   public TowerAttract(int x, int y, ObjectBase t, TowerBase.TowerUpgradeStage stage) throws SlickException {
      super(x, y, t);
      this.towerFlags = this.object.getTowerHeadFlagFactory().getTowerFlags(TowerBase.TowerType.ATTRACT, stage);
   }

   public TowerAttract(YMLDataMap dataMap, ObjectBase t) throws SlickException {
      super(dataMap, t);
   }

   @Override
   public void update() throws SlickException {
      super.update();
      if (this.tower.isPaused()) {
         this.setPositionFace();
      } else {
         boolean canFireMain = this.canFireMainAmmo();
         boolean canFireAlt = this.canFireAltAmmo();
         if (canFireMain || canFireAlt) {
            if (this.reloaded || !canFireMain && !canFireAlt) {
               this.reloadRateTick = 0;
            } else {
               this.reloadRateTick++;
            }

            if (this.reloadRateTick > this.towerFlags.getReloadRate()) {
               this.reloaded = true;
               this.reloading = false;
            }

            if (this.reloaded) {
               this.fireRateTick++;
               if (!this.verifyTarget() && Utilities.randomInt(this.towerFlags.getFindTargetRate()) == 0
                  || Utilities.randomInt(this.towerFlags.getChangeTargetRate()) == 0) {
                  this.findTarget();
               }

               if (this.verifyTarget()) {
                  if (this.fireRateTick > this.towerFlags.getFireRate()) {
                     if (!canFireMain && !canFireAlt) {
                        this.resetTarget();
                        return;
                     }

                     this.missile
                        .newMissile(this.towerCenterX, this.towerCenterY, this.targetCoordinateX, this.targetCoordinateY, this.towerFlags.getMissileType());
                     this.tower.decreaseEnergy(this.towerFlags.getFireEnergyCost());
                     this.sound.playSound(SoundModule.SoundType.MAGIC_ATTRACT_FIRE, this.towerCenterX, this.towerCenterY);
                     this.reloadRateTick = 0;
                     this.fireRateTick = 0;
                     this.reloaded = false;
                  }
               } else {
                  this.resetTarget();
                  this.runRandomRotation();
               }
            }
         }
      }
   }

   @Override
   protected void findTargetCoordinate() {
      if (this.resource.getResourcesNearby(this.towerX, this.towerY, 3).size() <= 12) {
         ArrayList<OrderedPair> nearbyCoordinates = Utilities.getAllCoordinatesWithin(this.towerX, this.towerY, this.towerFlags.getDistance());

         for (ObjectBase o : this.object.getObjectsBySortGroup(ObjectModule.ObjectSortGroup.TOWERS)) {
            if (o.getTower() != null && o.getTower().towerFlags.getTowerType() == TowerBase.TowerType.ATTRACT) {
               int dx = this.towerX - o.getTower().getX();
               int dy = this.towerY - o.getTower().getY();
               int hValue = (int)Math.sqrt(dx * dx + dy * dy);
               if (hValue < this.towerFlags.getDistance() * 2) {
                  ArrayList<OrderedPair> tooCloseCoordinates = Utilities.getAllCoordinatesWithin(o.getTower().getX(), o.getTower().getY(), 3);
                  nearbyCoordinates.removeAll(tooCloseCoordinates);
               }
            }
         }

         ArrayList<OrderedPair> validResourceCoordinates = new ArrayList<>();
         byte[][] resourceMap = this.resource.getResourcesOnGroundByteMap();

         label59:
         for (OrderedPair tile : nearbyCoordinates) {
            if (resourceMap[tile.getX()][tile.getY()] != 0) {
               ResourceBase r = this.resource.getResourceOnGround(tile.getX(), tile.getY());
               if (r.getType().getTemplate().canEquip()) {
                  int dx = this.towerX - tile.getX();
                  int dy = this.towerY - tile.getY();
                  int hValue = (int)Math.sqrt(dx * dx + dy * dy);
                  if (hValue < this.towerFlags.getDistance()) {
                     for (MissileBase m : this.missile.getMissileArray()) {
                        if (m.isActive() && m.getTargetCoordinatesX() == tile.getX() && m.getTargetCoordinatesY() == tile.getY()) {
                           continue label59;
                        }
                     }

                     validResourceCoordinates.add(tile);
                  }
               }
            }
         }

         if (validResourceCoordinates.size() > 0) {
            OrderedPair randomTile = validResourceCoordinates.get(Utilities.randomInt(validResourceCoordinates.size()));
            this.targetCoordinateX = randomTile.getX();
            this.targetCoordinateY = randomTile.getY();
         }
      }
   }

   @Override
   protected boolean verifyTargetCoordinate() throws SlickException {
      byte[][] resourceMap = this.resource.getResourcesOnGroundByteMap();
      return resourceMap[this.targetCoordinateX][this.targetCoordinateY] != 0;
   }
}
