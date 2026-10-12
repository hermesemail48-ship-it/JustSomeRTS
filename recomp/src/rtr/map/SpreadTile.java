package rtr.map;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.goal.GoalModule;
import rtr.particles.ParticleModule;
import rtr.states.StateBase;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;
import rtr.weather.WeatherModule;

public class SpreadTile {
   private MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   private ParticleModule particle = (ParticleModule)StateBase.getModule(ModuleBase.ModuleType.PARTICLE);
   private WeatherModule weather = (WeatherModule)StateBase.getModule(ModuleBase.ModuleType.WEATHER);
   private GoalModule goal = (GoalModule)StateBase.getModule(ModuleBase.ModuleType.GOAL);
   private MapTilesLoader.TileSet tileType;
   private int spreadX;
   private int spreadY;
   private int tileLayer;
   private int spreadChance;
   private int motivatedAmount;
   private boolean onlyIfMotivated;
   private int trySpreadTick = Utilities.randomInt(1000);
   private int trySpread = 1000;
   private boolean ignoreWeather;

   public SpreadTile(OrderedPair coordinates, MapTilesLoader.TileSet type, int layer, int spread, boolean onlyMotivated, boolean ignoreWeather) {
      this.spreadX = coordinates.getX();
      this.spreadY = coordinates.getY();
      this.tileType = type;
      this.tileLayer = layer;
      this.spreadChance = spread;
      this.onlyIfMotivated = onlyMotivated;
      this.ignoreWeather = ignoreWeather;
   }

   public void update() throws SlickException {
      if (!this.onlyIfMotivated || this.motivatedAmount > 0) {
         if (this.motivatedAmount > 0) {
            if (this.motivatedAmount > 4000) {
               if (Utilities.randomInt(5) == 0) {
                  this.particle
                     .newParticleGroup(
                        Utilities.randomInt(16) + this.spreadX * 16,
                        Utilities.randomInt(16) + this.spreadY * 16,
                        ParticleModule.ParticleGroup.MAGIC_FIRE,
                        ParticleModule.ParticleSet.FIRE_GREEN
                     );
                  this.particle
                     .newParticleGroup(
                        Utilities.randomInt(16) + this.spreadX * 16,
                        Utilities.randomInt(16) + this.spreadY * 16,
                        ParticleModule.ParticleGroup.MAGIC_FIRE,
                        ParticleModule.ParticleSet.FIRE_BLUE
                     );
               }
            } else if (Utilities.randomInt(5) == 0) {
               this.particle
                  .newParticleGroup(
                     Utilities.randomInt(16) + this.spreadX * 16,
                     Utilities.randomInt(16) + this.spreadY * 16,
                     ParticleModule.ParticleGroup.MAGIC_FIRE,
                     ParticleModule.ParticleSet.FIRE_GREEN
                  );
            }

            if (this.motivatedAmount > 5000) {
               this.motivatedAmount = 5000;
            } else {
               this.motivatedAmount--;
            }
         }

         this.trySpreadTick++;
         if (this.trySpreadTick >= this.trySpread) {
            this.trySpreadTick = 0;
            int calculatedSpreadChance = this.spreadChance;
            if (!this.ignoreWeather) {
               if (this.weather.isRaining()) {
                  calculatedSpreadChance /= 2;
               }

               if (this.weather.getCurrentTemp() <= this.weather.getFreezePoint()) {
                  calculatedSpreadChance += calculatedSpreadChance * 100;
               } else if (this.weather.getCurrentTemp() <= this.weather.getColdPoint()) {
                  calculatedSpreadChance += calculatedSpreadChance * 30;
               } else if (this.weather.getCurrentTemp() >= this.weather.getFryPoint()) {
                  calculatedSpreadChance += calculatedSpreadChance * 10;
               }
            }

            if (this.motivatedAmount > 0) {
               if (this.motivatedAmount > 4000) {
                  calculatedSpreadChance /= 6;
               } else {
                  calculatedSpreadChance /= 4;
               }
            }

            if (Utilities.randomInt(calculatedSpreadChance) == 0) {
               ArrayList<OrderedPair> spreadCoords = new ArrayList<>();

               for (int x = -1; x < 2; x++) {
                  for (int y = -1; y < 2; y++) {
                     int tileID = this.map.getTileId(this.spreadX + x, this.spreadY + y, this.tileLayer);
                     if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[this.spreadX + x][this.spreadY + y] == 0
                        && (tileID == 0 || this.map.getMapTileLoader().getTileSetByTileID(tileID) == this.tileType.getCollected())) {
                        spreadCoords.add(OrderedPair.getOrderedPair(this.spreadX + x, this.spreadY + y));
                     }
                  }
               }

               if (spreadCoords.size() > 0 && spreadCoords.size() < 9) {
                  OrderedPair spread = spreadCoords.get(Utilities.randomInt(spreadCoords.size()));
                  this.map.spreadResourceTerrain(spread.getX(), spread.getY(), this.tileType, false);
                  if (this.isMotivated()) {
                     this.goal.incrementGoal(GoalModule.GoalType.WILD_GROWTH);
                  }
               } else {
                  for (int x = -1; x < 2; x++) {
                     for (int yx = -1; yx < 2; yx++) {
                        int tileID = this.map.getTileId(this.spreadX + x, this.spreadY + yx, this.tileLayer);
                        if (this.map.getMapTileLoader().getTileSetByTileID(tileID) != this.tileType) {
                           return;
                        }
                     }
                  }

                  boolean canSpreadAccent = false;

                  label130:
                  for (int x = -1; x < 2; x++) {
                     for (int yxx = -1; yxx < 2; yxx++) {
                        int tileID = this.map.getTileId(this.spreadX + x, this.spreadY + yxx, this.tileLayer + 1);
                        if (this.map.getMapTileLoader().getTileSetByTileID(tileID) != this.tileType.getAccent()) {
                           canSpreadAccent = true;
                           break label130;
                        }
                     }
                  }

                  if (!canSpreadAccent) {
                     return;
                  }

                  spreadCoords.clear();
                  int chance = 5;

                  for (int x = -1; x < 2; x++) {
                     for (int yxxx = -1; yxxx < 2; yxxx++) {
                        int tileID = this.map.getTileId(this.spreadX + x, this.spreadY + yxxx, this.tileLayer + 1);
                        if (this.map.getMapTileLoader().getTileSetByTileID(tileID) == this.tileType.getAccent()) {
                           chance--;
                        } else {
                           spreadCoords.add(OrderedPair.getOrderedPair(this.spreadX + x, this.spreadY + yxxx));
                        }
                     }
                  }

                  if (chance <= 0) {
                     chance = 1;
                  }

                  if (Utilities.randomInt(chance) == 0 && spreadCoords.size() > 0) {
                     OrderedPair spread = spreadCoords.get(Utilities.randomInt(spreadCoords.size()));
                     this.map.spreadResourceTerrain(spread.getX(), spread.getY(), this.tileType, true);
                     if (this.isMotivated()) {
                        this.goal.incrementGoal(GoalModule.GoalType.WILD_GROWTH);
                     }
                  }
               }
            }
         }
      }
   }

   public int getSpreadX() {
      return this.spreadX;
   }

   public int getSpreadY() {
      return this.spreadY;
   }

   public MapTilesLoader.TileSet getTileType() {
      return this.tileType;
   }

   public void motivate() {
      this.motivatedAmount = this.motivatedAmount + Utilities.randomInt(250) + 250;
   }

   public boolean trySpread() throws SlickException {
      int calculatedSpreadChance = 3;
      boolean spreaded = false;
      if (this.weather.isRaining()) {
         calculatedSpreadChance -= 2;
      }

      if (this.weather.getCurrentTemp() <= this.weather.getFreezePoint()) {
         calculatedSpreadChance += 10;
      } else if (this.weather.getCurrentTemp() <= this.weather.getColdPoint()) {
         calculatedSpreadChance += 5;
      } else if (this.weather.getCurrentTemp() >= this.weather.getFryPoint()) {
         calculatedSpreadChance += 3;
      }

      if (Utilities.randomInt(calculatedSpreadChance) == 0) {
         ArrayList<OrderedPair> spreadCoords = new ArrayList<>();

         for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
               int tileID = this.map.getTileId(this.spreadX + x, this.spreadY + y, this.tileLayer);
               if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[this.spreadX + x][this.spreadY + y] == 0
                  && (tileID == 0 || this.map.getMapTileLoader().getTileSetByTileID(tileID) == this.tileType.getCollected())) {
                  spreadCoords.add(OrderedPair.getOrderedPair(this.spreadX + x, this.spreadY + y));
               }
            }
         }

         if (spreadCoords.size() > 0 && spreadCoords.size() < 9) {
            OrderedPair spread = spreadCoords.get(Utilities.randomInt(spreadCoords.size()));
            this.map.spreadResourceTerrain(spread.getX(), spread.getY(), this.tileType, false);
            spreaded = true;
         } else if (Utilities.randomInt(2) == 0) {
            for (int x = -1; x < 2; x++) {
               for (int yx = -1; yx < 2; yx++) {
                  int tileID = this.map.getTileId(this.spreadX + x, this.spreadY + yx, this.tileLayer);
                  if (this.map.getMapTileLoader().getTileSetByTileID(tileID) != this.tileType) {
                     return false;
                  }
               }
            }

            boolean canSpreadAccent = false;

            label96:
            for (int x = -1; x < 2; x++) {
               for (int yxx = -1; yxx < 2; yxx++) {
                  int tileID = this.map.getTileId(this.spreadX + x, this.spreadY + yxx, this.tileLayer + 1);
                  if (this.map.getMapTileLoader().getTileSetByTileID(tileID) != this.tileType.getAccent()) {
                     canSpreadAccent = true;
                     break label96;
                  }
               }
            }

            if (!canSpreadAccent) {
               return false;
            }

            spreadCoords.clear();
            int chance = 50;

            for (int x = -1; x < 2; x++) {
               for (int yxxx = -1; yxxx < 2; yxxx++) {
                  int tileID = this.map.getTileId(this.spreadX + x, this.spreadY + yxxx, this.tileLayer + 1);
                  if (this.map.getMapTileLoader().getTileSetByTileID(tileID) == this.tileType.getAccent()) {
                     chance -= 10;
                  } else {
                     spreadCoords.add(OrderedPair.getOrderedPair(this.spreadX + x, this.spreadY + yxxx));
                  }
               }
            }

            if (chance <= 0) {
               chance = 1;
            }

            if (Utilities.randomInt(chance) == 0 && spreadCoords.size() > 0) {
               OrderedPair spread = spreadCoords.get(Utilities.randomInt(spreadCoords.size()));
               this.map.spreadResourceTerrain(spread.getX(), spread.getY(), this.tileType, true);
               spreaded = true;
            }
         }
      }

      return spreaded;
   }

   public boolean isMotivated() {
      return this.motivatedAmount > 0;
   }
}
