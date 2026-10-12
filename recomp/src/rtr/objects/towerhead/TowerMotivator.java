package rtr.objects.towerhead;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.map.MapTilesLoader;
import rtr.map.RegrowTile;
import rtr.objects.ObjectBase;
import rtr.resources.ResourceModule;
import rtr.save.YMLDataMap;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class TowerMotivator extends TowerBase {
   public TowerMotivator(int x, int y, ObjectBase t) throws SlickException {
      super(x, y, t);
      this.towerFlags = this.object.getTowerHeadFlagFactory().getTowerFlags(TowerBase.TowerType.MOTIVATOR, TowerBase.TowerUpgradeStage.BASE);
   }

   public TowerMotivator(YMLDataMap dataMap, ObjectBase t) throws SlickException {
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
                     this.firing = true;
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
      ArrayList<OrderedPair> nearbyCoordinates = Utilities.getAllCoordinatesWithin(this.towerX, this.towerY, this.towerFlags.getDistance());
      ArrayList<OrderedPair> validMotivatedTileCoordinates = new ArrayList<>();
      ArrayList<OrderedPair> validUnmotivatedTileCoordinates = new ArrayList<>();

      for (OrderedPair tile : nearbyCoordinates) {
         RegrowTile regrowTile = this.mapAI.getRegrowMap()[tile.getX()][tile.getY()];
         if (regrowTile != null) {
            MapTilesLoader.TileSet tileType = regrowTile.getTileType();
            int tileID = this.map.getMapTileLoader().getTileSetGID(tileType);
            ResourceModule.ResourceType tileResourceType = this.map.getMapTileLoader().getTileResourceType(tileID);
            if (tileResourceType == ResourceModule.ResourceType.CRYSTAL) {
               int dx = this.towerX - tile.getX();
               int dy = this.towerY - tile.getY();
               int hValue = (int)Math.sqrt(dx * dx + dy * dy);
               if (hValue < this.towerFlags.getDistance()) {
                  if (regrowTile.isMotivated()) {
                     validMotivatedTileCoordinates.add(tile);
                  } else {
                     validUnmotivatedTileCoordinates.add(tile);
                  }
               }
            }
         }
      }

      if (validUnmotivatedTileCoordinates.size() > 0) {
         OrderedPair randomTile = validUnmotivatedTileCoordinates.get(Utilities.randomInt(validUnmotivatedTileCoordinates.size()));
         this.targetCoordinateX = randomTile.getX();
         this.targetCoordinateY = randomTile.getY();
      } else if (validMotivatedTileCoordinates.size() > 0) {
         OrderedPair randomTile = validMotivatedTileCoordinates.get(Utilities.randomInt(validMotivatedTileCoordinates.size()));
         this.targetCoordinateX = randomTile.getX();
         this.targetCoordinateY = randomTile.getY();
      }
   }

   @Override
   protected boolean verifyTargetCoordinate() throws SlickException {
      return this.mapAI.getRegrowMap()[this.targetCoordinateX][this.targetCoordinateY] != null
         || this.mapAI.getSpreadMap()[this.targetCoordinateX][this.targetCoordinateY] != null;
   }
}
