package rtr.map;

import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.goal.GoalModule;
import rtr.map.ai.MapAIModule;
import rtr.particles.ParticleModule;
import rtr.states.StateBase;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;
import rtr.weather.WeatherModule;

public class RegrowTile {
   private MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   private MapAIModule mapAI = (MapAIModule)StateBase.getModule(ModuleBase.ModuleType.MAP_AI);
   private ParticleModule particle = (ParticleModule)StateBase.getModule(ModuleBase.ModuleType.PARTICLE);
   private WeatherModule weather = (WeatherModule)StateBase.getModule(ModuleBase.ModuleType.WEATHER);
   private GoalModule goal = (GoalModule)StateBase.getModule(ModuleBase.ModuleType.GOAL);
   private MapTilesLoader.TileSet tileType;
   private int regrowX;
   private int regrowY;
   private int regrowChance;
   private int motivatedAmount;
   private boolean onlyIfMotivated;
   private int tryRegrowTick;
   private int tryRegrow = 1000;
   private boolean ignoreWeather;

   public RegrowTile(OrderedPair coordinates, MapTilesLoader.TileSet type, int regrow, boolean onlyMotivated, boolean ignoreWeather) {
      this.regrowX = coordinates.getX();
      this.regrowY = coordinates.getY();
      this.tileType = type;
      this.regrowChance = regrow;
      this.onlyIfMotivated = onlyMotivated;
      this.ignoreWeather = ignoreWeather;
   }

   public void update() throws SlickException {
      this.update(1);
   }

   /** Advances this tile by several ticks at once (MapAIModule updates a slice of the tiles each tick). */
   public void update(int steps) throws SlickException {
      if (!this.onlyIfMotivated || this.motivatedAmount > 0) {
         for (int step = 0; step < steps && this.motivatedAmount > 0; step++) {
            if (this.motivatedAmount > 4000) {
               if (Utilities.randomInt(5) == 0) {
                  this.particle
                     .newParticleGroup(
                        Utilities.randomInt(16) + this.regrowX * 16,
                        Utilities.randomInt(16) + this.regrowY * 16,
                        ParticleModule.ParticleGroup.MAGIC_FIRE,
                        ParticleModule.ParticleSet.FIRE_GREEN
                     );
                  this.particle
                     .newParticleGroup(
                        Utilities.randomInt(16) + this.regrowX * 16,
                        Utilities.randomInt(16) + this.regrowY * 16,
                        ParticleModule.ParticleGroup.MAGIC_FIRE,
                        ParticleModule.ParticleSet.FIRE_BLUE
                     );
               }
            } else if (Utilities.randomInt(5) == 0) {
               this.particle
                  .newParticleGroup(
                     Utilities.randomInt(16) + this.regrowX * 16,
                     Utilities.randomInt(16) + this.regrowY * 16,
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

         this.tryRegrowTick += steps;
         if (this.tryRegrowTick >= this.tryRegrow) {
            this.tryRegrowTick = 0;
            int calculatedRegrowChance = this.regrowChance;
            if (!this.ignoreWeather) {
               if (this.weather.isRaining()) {
                  calculatedRegrowChance /= 2;
               }

               if (this.weather.getCurrentTemp() <= this.weather.getFreezePoint()) {
                  calculatedRegrowChance += calculatedRegrowChance * 100;
               } else if (this.weather.getCurrentTemp() <= this.weather.getColdPoint()) {
                  calculatedRegrowChance += calculatedRegrowChance * 30;
               } else if (this.weather.getCurrentTemp() >= this.weather.getFryPoint()) {
                  calculatedRegrowChance += calculatedRegrowChance * 10;
               }
            }

            if (this.motivatedAmount > 0) {
               if (this.motivatedAmount > 4000) {
                  calculatedRegrowChance /= 6;
               } else {
                  calculatedRegrowChance /= 4;
               }
            }

            if (Utilities.randomInt(calculatedRegrowChance) == 0) {
               this.map.regrowResourceTerrain(this.regrowX, this.regrowY, this.tileType);
               if (this.isMotivated()) {
                  this.goal.incrementGoal(GoalModule.GoalType.WILD_GROWTH);
               }

               this.mapAI.removeRegrowTile(this);
            }
         }
      }
   }

   public MapTilesLoader.TileSet getTileType() {
      return this.tileType;
   }

   public int getRegrowX() {
      return this.regrowX;
   }

   public int getRegrowY() {
      return this.regrowY;
   }

   public void motivate(int amount) {
      this.motivatedAmount = this.motivatedAmount + Utilities.randomInt(250) + amount;
   }

   public boolean tryRegrow() throws SlickException {
      int calculatedRegrowChance = 3;
      if (!this.ignoreWeather) {
         if (this.weather.isRaining()) {
            calculatedRegrowChance -= 2;
         }

         if (this.weather.getCurrentTemp() <= this.weather.getFreezePoint()) {
            calculatedRegrowChance += 10;
         } else if (this.weather.getCurrentTemp() <= this.weather.getColdPoint()) {
            calculatedRegrowChance += 5;
         } else if (this.weather.getCurrentTemp() >= this.weather.getFryPoint()) {
            calculatedRegrowChance += 3;
         }
      }

      if (Utilities.randomInt(calculatedRegrowChance) == 0) {
         this.map.regrowResourceTerrain(this.regrowX, this.regrowY, this.tileType);
         if (this.isMotivated()) {
            this.goal.incrementGoal(GoalModule.GoalType.WILD_GROWTH);
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean isMotivated() {
      return this.motivatedAmount > 0;
   }
}
