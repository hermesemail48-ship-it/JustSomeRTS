package rtr.weather.disasters;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.console.Console;
import rtr.font.Text;
import rtr.map.MapData;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.resources.ResourceModule;
import rtr.save.YMLDataMap;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class DisasterEarthquake extends DisasterBase {
   private ArrayList<OrderedPair> culledCoordinates = new ArrayList<>();
   private ArrayList<OrderedPair> rockCoordinates = new ArrayList<>();
   private MapTilesLoader.TileSet rockType;

   public DisasterEarthquake(int tileX, int tileY, boolean gT) {
      this.disasterType = DisasterBase.DisasterType.EARTHQUAKE;
      this.godTriggered = gT;
      this.velocityMultiplier = -0.1F;
      this.duration = 1500;
      this.centerX = tileX;
      this.centerY = tileY;
      this.targetX = this.centerX * 16 + 8;
      this.targetY = this.centerY * 16 + 8;
   }

   public DisasterEarthquake(boolean gT) throws SlickException {
      this.disasterType = DisasterBase.DisasterType.EARTHQUAKE;
      this.godTriggered = gT;
      this.velocityMultiplier = -0.1F;
      this.duration = 1500;
      if (Utilities.randomInt(25) == 0) {
         Console.newBanner(
            Text.getText("console.banner.weather.disaster.earthquake.header"), Text.getText("console.banner.weather.disaster.earthquake.subtitleEasterEgg")
         );
      } else {
         Console.newBanner(
            Text.getText("console.banner.weather.disaster.earthquake.header"), Text.getText("console.banner.weather.disaster.earthquake.subtitle")
         );
      }

      this.sound.playSound(SoundModule.SoundType.DISASTER_START);

      for (int x = 0; x < 20; x++) {
         this.centerX = Utilities.randomInt(256);
         this.centerY = Utilities.randomInt(256);
         if (this.object.isInRange(this.centerX, this.centerY)) {
            break;
         }
      }

      this.targetX = this.centerX * 16 + 8;
      this.targetY = this.centerY * 16 + 8;
   }

   public DisasterEarthquake(YMLDataMap dataMap) {
      this.loadDisaster(dataMap);
   }

   @Override
   public void update() throws SlickException {
      int distance = 32;
      switch (this.disasterStep) {
         case 0:
            for (OrderedPair tile : Utilities.getAllCoordinatesWithin(this.centerX, this.centerY, distance * 2)) {
               int dx = this.centerX - tile.getX();
               int dy = this.centerY - tile.getY();
               int hValue = (int)Math.sqrt(dx * dx + dy * dy);
               if (hValue < distance) {
                  this.culledCoordinates.add(tile);
               }
            }

            for (OrderedPair tilex : this.culledCoordinates) {
               int tileID = this.map.getTileId(tilex.getX(), tilex.getY(), 6);
               if (tileID == 0) {
                  tileID = this.map.getTileId(tilex.getX(), tilex.getY(), 9);
               }

               if (tileID != 0) {
                  int tileGID = this.map.getMapTileLoader().getTileSetGID(tileID);
                  if (this.rockType == null) {
                     MapTilesLoader.TileSet rockNearby = this.map.getMapTileLoader().getTileSetByTileID(tileGID);
                     ResourceModule.ResourceType resourceType = this.map.getMapTileLoader().getTileResourceType(tileGID);
                     if (resourceType == ResourceModule.ResourceType.ROCK) {
                        this.rockType = rockNearby;
                     }
                  }

                  MapTilesLoader.TileSet regrowTile = this.map.getMapTileLoader().canTileRegrow(tileID);
                  if (regrowTile != null) {
                     ResourceModule.ResourceType resourceType = this.map
                        .getMapTileLoader()
                        .getTileResourceType(this.map.getMapTileLoader().getTileSetGID(regrowTile));
                     if (resourceType == ResourceModule.ResourceType.ROCK) {
                        if (this.rockType == null) {
                           this.rockType = regrowTile;
                        }

                        this.rockCoordinates.add(tilex);
                     }
                  }
               }
            }

            if (this.rockType == null) {
               label182:
               for (int x = 0; x < 256; x++) {
                  for (int y = 0; y < 256; y++) {
                     int tileIDx = this.map.getTileId(x, y, 6);
                     int tileGIDx = this.map.getMapTileLoader().getTileSetGID(tileIDx);
                     MapTilesLoader.TileSet rockNearby = this.map.getMapTileLoader().getTileSetByTileID(tileGIDx);
                     ResourceModule.ResourceType resourceType = this.map.getMapTileLoader().getTileResourceType(tileGIDx);
                     if (resourceType == ResourceModule.ResourceType.ROCK) {
                        this.rockType = rockNearby;
                        break label182;
                     }

                     tileIDx = this.map.getTileId(x, y, 9);
                     tileGIDx = this.map.getMapTileLoader().getTileSetGID(tileIDx);
                     rockNearby = this.map.getMapTileLoader().getTileSetByTileID(tileGIDx);
                     resourceType = this.map.getMapTileLoader().getTileResourceType(tileGIDx);
                     if (resourceType == ResourceModule.ResourceType.ROCK) {
                        this.rockType = rockNearby;
                        break label182;
                     }
                  }
               }
            }

            if (this.rockType == null) {
               this.rockType = MapTilesLoader.TileSet.ROCK_GRAY;
            }

            this.disasterStep++;
            break;
         case 1:
            this.durationTick++;
            if ((this.durationTick % 100 == 0 || Utilities.randomInt(100) == 0) && Utilities.randomInt(2) == 0) {
               this.sound.playSound(SoundModule.SoundType.EARTHQUAKE_BOOM);
            }

            if (this.durationTick > 250 && this.durationTick % 50 == 0) {
               for (OrderedPair tilex : this.culledCoordinates) {
                  if (Utilities.randomInt(2) == 0) {
                     int randomX = tilex.getX() * 16 + Utilities.randomInt(16);
                     int randomY = tilex.getY() * 16 + Utilities.randomInt(16);
                     this.particle.newParticle(randomX, randomY, ParticleModule.ParticleType.SMOKE_SPECK, ParticleModule.ParticleSet.DARK_GRAY);
                  }

                  if (Utilities.randomInt(100) == 0) {
                     int randomX = tilex.getX() * 16 + Utilities.randomInt(16);
                     int randomY = tilex.getY() * 16 + Utilities.randomInt(16);

                     for (int i = 0; i < 5; i++) {
                        this.particle.newParticle(randomX, randomY, ParticleModule.ParticleType.SMOKE_SPECK, ParticleModule.ParticleSet.DARK_GRAY);
                     }
                  }
               }
            }

            if (this.durationTick > 250) {
               if (this.durationTick <= 500) {
                  for (int i = 0; i < this.durationTick / 32; i++) {
                     this.projectile.newEnergyRing(this.targetX, this.targetY, (this.durationTick - 250) * 2, ParticleModule.ParticleSet.GRAY);
                  }

                  for (int i = 0; i < this.durationTick / 128; i++) {
                     this.projectile.newEnergyRing(this.targetX, this.targetY, (this.durationTick - 250) * 2, ParticleModule.ParticleSet.FIRE_BLUE);
                  }
               }

               if (this.durationTick % 200 == 0 && this.durationTick <= 1200) {
                  this.projectile.newExpandingFireRing(this.targetX, this.targetY, (distance + 2) * 16, 3.0F, ParticleModule.ParticleSet.GRAY);
                  this.projectile.newExpandingFireRing(this.targetX, this.targetY, (distance + 2) * 16, 1.0F, ParticleModule.ParticleSet.GRAY);
               }

               if (Utilities.randomInt(5) == 0) {
                  this.map.initSreenShake(1.0F);
               }

               if (this.durationTick % 10 == 0) {
                  this.mob.tryPanicInArea(this.centerX, this.centerY, distance, this.godTriggered, 2);
               }

               if (this.durationTick == 200 || this.durationTick % 10 == 0 && Utilities.randomInt(3) == 0) {
                  this.sound.playSound(SoundModule.SoundType.EARTHQUAKE, this.targetX, this.targetY);
                  this.sound.playSound(SoundModule.SoundType.EARTHQUAKE_BOOM, this.targetX, this.targetY);
               }

               if (Utilities.randomInt(100) == 0) {
                  this.sound.playSound(SoundModule.SoundType.EARTHQUAKE_BOOM, this.targetX, this.targetY);
               }

               if (Utilities.randomInt(15) == 0) {
                  this.sound.playSound(SoundModule.SoundType.EARTHQUAKE_ROCKS, this.targetX, this.targetY);
               }

               if (Utilities.randomInt(20) == 0) {
                  for (MobBase m : this.mob.getMobsNearby(this.centerX, this.centerY, distance)) {
                     if (Utilities.randomInt(5) == 0) {
                        int dx = this.centerX - m.getTileX();
                        int dy = this.centerY - m.getTileY();
                        int hValue = (int)Math.sqrt(dx * dx + dy * dy);
                        int damageOut = 30 + hValue;
                        if (damageOut > 200) {
                           damageOut = 200;
                        }

                        m.takeDamage(
                           m.getHitPointsMax() / damageOut + 1, this.targetX, this.targetY, MobBase.DamageType.CRUSHING, MobBase.DamageCause.EARTHQUAKE
                        );
                     }
                  }
               }

               if (Utilities.randomInt(20) == 0) {
                  for (ObjectBase o : this.object.getObjectsNearby(this.centerX, this.centerY, distance)) {
                     if (!o.canNotBeAttacked() && Utilities.randomInt(5) == 0) {
                        int dx = this.centerX - o.getObjectX();
                        int dy = this.centerY - o.getObjectY();
                        int hValue = (int)Math.sqrt(dx * dx + dy * dy);
                        int damageOut = 30 + hValue;
                        if (damageOut > 200) {
                           damageOut = 200;
                        }

                        o.takeDamage(o.getHitPointsMax() / damageOut + 1, MobBase.DamageType.CRUSHING, MobBase.DamageCause.EARTHQUAKE);
                     }
                  }
               }

               if (Utilities.randomInt(20) == 0) {
                  for (OrderedPair tilex : this.culledCoordinates) {
                     int dx = this.centerX - tilex.getX();
                     int dy = this.centerY - tilex.getY();
                     int hValue = (int)Math.sqrt(dx * dx + dy * dy);
                     if (Utilities.randomInt(750) == 0 && !this.rockCoordinates.contains(tilex)) {
                        int damageOut = 20 - hValue;
                        if (damageOut <= 0) {
                           damageOut = 1;
                        }

                        for (int i = 0; i < damageOut; i++) {
                           this.resource.damageResource(tilex.getX(), tilex.getY());
                        }
                     }

                     hValue *= hValue * distance * 8;
                     hValue /= 8;
                     if (this.rockCoordinates.contains(tilex)) {
                        hValue /= 16;
                     }

                     if (Utilities.randomInt(hValue + 1) == 0) {
                        int tileIDxx = this.map.getTileId(tilex.getX(), tilex.getY(), 6);
                        if (tileIDxx == 0) {
                           this.map.getTileId(tilex.getX(), tilex.getY(), 9);
                        }

                        if (tileIDxx != 0) {
                           MapTilesLoader.TileSet regrowTile = this.map.getMapTileLoader().canTileRegrow(tileIDxx);
                           if (regrowTile != null) {
                              ResourceModule.ResourceType resourceTypex = this.map
                                 .getMapTileLoader()
                                 .getTileResourceType(this.map.getMapTileLoader().getTileSetGID(regrowTile));
                              if (resourceTypex == ResourceModule.ResourceType.ROCK) {
                                 this.map.regrowResourceTerrain(tilex.getX(), tilex.getY(), regrowTile);
                              }
                           }
                        } else if (Utilities.randomInt(2) == 0) {
                           if (Utilities.randomInt(6) == 0 && this.map.canDigAtCoord(tilex.getX(), tilex.getY())) {
                              for (int x = -1; x <= 1; x++) {
                                 for (int y = -1; y <= 1; y++) {
                                    if (Utilities.randomInt(10) != 0 && this.map.canDigAtCoord(tilex.getX() + x, tilex.getY() + y) || x == 0 && y == 0) {
                                       this.map.regrowResourceTerrain(tilex.getX() + x, tilex.getY() + y, MapTilesLoader.TileSet.WATER_DRY);
                                    }
                                 }
                              }
                           } else {
                              this.map.regrowResourceTerrain(tilex.getX(), tilex.getY(), this.rockType);
                           }
                        } else if (!this.resource.isResourcesOnGround(tilex.getX(), tilex.getY())
                           && this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[tilex.getX()][tilex.getY()] == 0) {
                           ResourceModule.ResourceType t = this.map
                              .getMapTileLoader()
                              .getTileResourceType(this.map.getMapTileLoader().getTileSetGID(this.rockType));
                           ResourceModule.ResourceColorSet c = this.map
                              .getMapTileLoader()
                              .getTileResourceColorSet(this.map.getMapTileLoader().getTileSetGID(this.rockType));
                           this.resource.createResourceOnGround(t, c, tilex.getX(), tilex.getY());
                        }
                     }
                  }
               }
            }

            if (this.durationTick > this.duration) {
               this.weather.removeDisaster(this);
            }
      }
   }

   @Override
   protected void loadDisaster(YMLDataMap dataMap) {
      super.loadDisaster(dataMap);
      if (dataMap.containsKey("culledCoordinates")) {
         this.culledCoordinates = dataMap.unpackCoordinatesList("culledCoordinates");
      }

      if (dataMap.containsKey("rockCoordinates")) {
         this.rockCoordinates = dataMap.unpackCoordinatesList("rockCoordinates");
      }

      String rockTypeString = dataMap.getOrDefault("rockType", MapTilesLoader.TileSet.ROCK_GRAY.toString());

      MapTilesLoader.TileSet[] var6;
      for (MapTilesLoader.TileSet t : MapTilesLoader.TileSet.values()) {
         if (t.toString().equals(rockTypeString)) {
            this.rockType = t;
            break;
         }
      }
   }

   @Override
   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = super.getRegionalSaveData();
      if (this.culledCoordinates.size() > 0) {
         dataMap.put("culledCoordinates", dataMap.packCoordinatesList(this.culledCoordinates));
      }

      if (this.rockCoordinates.size() > 0) {
         dataMap.put("rockCoordinates", dataMap.packCoordinatesList(this.rockCoordinates));
      }

      if (this.rockType != null) {
         dataMap.put("rockType", this.rockType.toString());
      }

      return dataMap;
   }
}
