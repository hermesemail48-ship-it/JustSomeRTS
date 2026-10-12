package rtr.mobs.essence;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.SoundModule;
import rtr.StatsModule;
import rtr.goal.GoalModule;
import rtr.influence.InfluenceModule;
import rtr.map.MapModule;
import rtr.map.MapTilesLoader;
import rtr.objects.ObjectBase;
import rtr.objects.ObjectModule;
import rtr.particles.ParticleModule;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.utilities.Utilities;

public class EssenceBase {
   private EssenceModule essence = (EssenceModule)StateBase.getModule(ModuleBase.ModuleType.ESSENCE);
   private ObjectModule object = (ObjectModule)StateBase.getModule(ModuleBase.ModuleType.OBJECT);
   private ParticleModule particles = (ParticleModule)StateBase.getModule(ModuleBase.ModuleType.PARTICLE);
   private InfluenceModule influence = (InfluenceModule)StateBase.getModule(ModuleBase.ModuleType.INFLUENCE);
   private SoundModule sound = (SoundModule)StateBase.getModule(ModuleBase.ModuleType.SOUND);
   private MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   private StatsModule stats = (StatsModule)StateBase.getModule(ModuleBase.ModuleType.STATS);
   private GoalModule goal = (GoalModule)StateBase.getModule(ModuleBase.ModuleType.GOAL);
   private static int DECAY_RATE = 5000;
   private float essenceX;
   private float essenceY;
   private float velX;
   private float velY;
   private float velMod;
   private int energyValue = 1;
   private int decayRateTick;
   private ObjectBase target;
   private int targetID = -1;
   private float targetX;
   private float targetY;
   private int restPeriod;
   private int restPeriodTick;
   private Color essenceDotColor = new Color(new Color(0, 255, 0, 150));
   private Color essenceDotColorBeingDelivered = new Color(new Color(0, 255, 255, 150));
   private Color essenceDotColorShadow = new Color(new Color(0, 100, 0, 100));
   private Color essenceDotColorBeingDeliveredShadow = new Color(new Color(0, 100, 100, 100));
   private boolean beingDelivered;
   private ParticleModule.ParticleSet beingDeliveredColors = ParticleModule.ParticleSet.FIRE_BLUE;
   private ParticleModule.ParticleSet idleColors = ParticleModule.ParticleSet.FIRE_GREEN;
   private ParticleModule.ParticleSet toHandColors = ParticleModule.ParticleSet.FIRE_PURPLE;
   private ParticleModule.ParticleSet beingDeliveredFromAltar = ParticleModule.ParticleSet.FIRE_YELLOW;

   public EssenceBase(float x, float y, int e, boolean disableCooldown) {
      this.essenceX = x;
      this.essenceY = y;
      this.velX = (Utilities.randomFloat() - Utilities.randomFloat()) / 5.0F;
      this.velY = (Utilities.randomFloat() - Utilities.randomFloat()) / 5.0F;
      this.energyValue = e;
      this.checkTarget();
      if (disableCooldown) {
         this.restPeriod = 1;
      } else {
         this.restPeriod = Utilities.randomInt(50) + 50;
      }
   }

   public EssenceBase(float x, float y, int e, ObjectBase t) {
      this.beingDelivered = true;
      this.target = t;
      this.targetX = this.target.getEssenceReturnXOnMapExactPixel();
      this.targetY = this.target.getEssenceReturnYOnMapExactPixel();
      this.essenceX = x;
      this.essenceY = y;
      this.velX = Utilities.randomFloat() - Utilities.randomFloat();
      this.velY = Utilities.randomFloat() - Utilities.randomFloat();
      this.velMod = 0.1F;
      this.energyValue = e;
      this.target.increaseEnergyInRoute(this.energyValue);
      this.restPeriod = 25;
   }

   public EssenceBase(YMLDataMap dataMap) {
      this.essenceX = Float.parseFloat(dataMap.getOrDefault("essenceX", 0.0F));
      this.essenceY = Float.parseFloat(dataMap.getOrDefault("essenceY", 0.0F));
      this.velX = Float.parseFloat(dataMap.getOrDefault("velX", 0.0F));
      this.velY = Float.parseFloat(dataMap.getOrDefault("velY", 0.0F));
      this.velMod = Float.parseFloat(dataMap.getOrDefault("velMod", 0.0F));
      this.energyValue = Integer.parseInt(dataMap.getOrDefault("energyValue", 1));
      this.decayRateTick = Integer.parseInt(dataMap.getOrDefault("decayRateTick", 0));
      this.targetID = Integer.parseInt(dataMap.getOrDefault("targetID", -1));
      this.targetX = Float.parseFloat(dataMap.getOrDefault("targetX", 0.0F));
      this.targetY = Float.parseFloat(dataMap.getOrDefault("targetY", 0.0F));
      this.restPeriod = Integer.parseInt(dataMap.getOrDefault("restPeriod", 1));
      this.restPeriodTick = Integer.parseInt(dataMap.getOrDefault("restPeriodTick", 0));
      this.beingDelivered = Boolean.parseBoolean(dataMap.getOrDefault("beingDelivered", false));
   }

   public void update() throws SlickException {
      this.decayRateTick++;
      if (this.decayRateTick > DECAY_RATE) {
         if (this.target != null) {
            this.target.decreaseEnergyInRoute(this.energyValue);
         }

         this.essence.removeEssence(this);
         this.stats.increaseEssenceDecayedThisPeriod(this.energyValue);
      } else {
         this.checkTarget();
         int mouseX = Game.getCS().getMouseOnMapX();
         int mouseY = Game.getCS().getMouseOnMapY();
         boolean toHand = false;
         if (this.map.isInView(this.essenceX, this.essenceY, 10)) {
            if (!this.beingDelivered
               && this.restPeriodTick >= this.restPeriod
               && !this.beingDelivered
               && this.influence.getInfluenceAmount() < this.influence.getInfluenceAmountUsableMax()
               && Math.abs(mouseX - this.essenceX) < 128.0F
               && Math.abs(mouseY - this.essenceY) < 128.0F) {
               toHand = true;
            }

            int randomX = 0;
            int randomY = 0;
            int loopChance = 3 - this.energyValue / 5;
            if (loopChance <= 0) {
               loopChance = 1;
            }

            ParticleModule.ParticleSet outputColor = this.idleColors;
            if (this.beingDelivered) {
               if (this.target.getObjectFlags().canCollectEssence()) {
                  outputColor = this.beingDeliveredFromAltar;
               } else {
                  outputColor = this.beingDeliveredColors;
               }
            } else if (toHand) {
               outputColor = this.toHandColors;
            } else {
               outputColor = this.idleColors;
            }

            if (Utilities.randomInt(loopChance) == 0) {
               this.particles.newParticleGroup(this.essenceX + randomX, this.essenceY + randomY, ParticleModule.ParticleGroup.MAGIC_FIRE, outputColor);
            }

            if (Utilities.randomInt(loopChance) == 0 && (Math.abs(this.velX) > 0.25 || Math.abs(this.velY) > 0.25)) {
               this.particles.newParticle(this.essenceX, this.essenceY, ParticleModule.ParticleType.FLASH_FAST, outputColor);
            }
         }

         if (this.restPeriodTick < this.restPeriod) {
            this.restPeriodTick++;
         }

         if (toHand) {
            if ((int)this.essenceX >= mouseX - 32
               && (int)this.essenceX <= mouseX + 32
               && (int)this.essenceY >= mouseY - 32
               && (int)this.essenceY <= mouseY + 32) {
               this.velMod += 0.001F;
            } else {
               this.velMod += 1.0E-5F;
            }

            if (this.essenceX > mouseX) {
               this.velX = this.velX - (this.velMod + 1.0E-5F);
            }

            if (this.essenceX < mouseX) {
               this.velX = this.velX + (this.velMod + 1.0E-5F);
            }

            if (this.essenceY > mouseY) {
               this.velY = this.velY - (this.velMod + 1.0E-5F);
            }

            if (this.essenceY < mouseY) {
               this.velY = this.velY + (this.velMod + 1.0E-5F);
            }

            if (this.velX > 1.5F) {
               this.velX = 1.5F;
            }

            if (this.velX < -1.5F) {
               this.velX = -1.5F;
            }

            if (this.velY > 1.5F) {
               this.velY = 1.5F;
            }

            if (this.velY < -1.5F) {
               this.velY = -1.5F;
            }

            if ((int)this.essenceX >= mouseX - 6 && (int)this.essenceX <= mouseX + 6 && (int)this.essenceY >= mouseY - 6 && (int)this.essenceY <= mouseY + 6) {
               if (this.essenceX > mouseX) {
                  this.essenceX -= 0.2F;
               }

               if (this.essenceX < mouseX) {
                  this.essenceX += 0.2F;
               }

               if (this.essenceY > mouseY) {
                  this.essenceY -= 0.2F;
               }

               if (this.essenceY < mouseY) {
                  this.essenceY += 0.2F;
               }
            } else {
               this.essenceX = this.essenceX + this.velX;
               this.essenceY = this.essenceY + this.velY;
            }

            if (Math.abs(mouseX - this.essenceX) < 3.0F && Math.abs(mouseY - this.essenceY) < 3.0F) {
               if (this.target != null) {
                  this.target.decreaseEnergyInRoute(this.energyValue);
               }

               this.influence.increaseInfluenceAmount(this.energyValue * 2);
               this.goal.incrementGoal(GoalModule.GoalType.MANA_MILLIONAIRE, this.energyValue * 2);
               this.essence.removeEssence(this);
               this.sound.playSound(SoundModule.SoundType.ESSENCE_SUCK, this.essenceX, this.essenceY);

               for (int i = 0; i < this.energyValue * 4; i++) {
                  this.particles
                     .newParticle(
                        this.essenceX + Utilities.randomInt(-3, 3),
                        this.essenceY + Utilities.randomInt(-3, 3),
                        ParticleModule.ParticleType.SMOKE_SPECK,
                        ParticleModule.ParticleSet.FIRE_GREEN
                     );
               }
            }
         } else {
            if (this.target == null) {
               if (this.velX > 1.0E-4F && this.velX < 1.0E-4F) {
                  this.velX = 0.0F;
               }

               if (this.velY > 1.0E-4F && this.velY < 1.0E-4F) {
                  this.velY = 0.0F;
               }

               if (this.velX > 1.0F) {
                  this.velX -= 0.1F;
               }

               if (this.velX < -1.0F) {
                  this.velX += 0.1F;
               }

               if (this.velY > 1.0F) {
                  this.velY -= 0.1F;
               }

               if (this.velY < -1.0F) {
                  this.velY += 0.1F;
               }

               if (this.velX > 0.0F) {
                  this.velX -= 0.001F;
               }

               if (this.velX < 0.0F) {
                  this.velX += 0.001F;
               }

               if (this.velY > 0.0F) {
                  this.velY -= 0.001F;
               }

               if (this.velY < 0.0F) {
                  this.velY += 0.001F;
               }

               this.essenceX = this.essenceX + this.velX;
               this.essenceY = this.essenceY + this.velY;
               if (Utilities.randomInt(50) == 0) {
                  int random = Utilities.randomInt(3);
                  switch (random) {
                     case 0:
                        this.velX = this.velX + Utilities.randomFloat() / 10.0F;
                        break;
                     case 1:
                        this.velX = this.velX + Utilities.randomFloat() / 10.0F * -1.0F;
                  }

                  random = Utilities.randomInt(3);
                  switch (random) {
                     case 0:
                        this.velY = this.velY + Utilities.randomFloat() / 10.0F;
                        break;
                     case 1:
                        this.velY = this.velY + Utilities.randomFloat() / 10.0F * -1.0F;
                  }
               }
            } else if (this.restPeriodTick >= this.restPeriod) {
               if ((int)this.essenceX >= this.targetX - 64.0F
                  && (int)this.essenceX <= this.targetX + 64.0F
                  && (int)this.essenceY >= this.targetY - 64.0F
                  && (int)this.essenceY <= this.targetY + 64.0F) {
                  this.velMod += 0.001F;
               } else {
                  this.velMod += 1.0E-5F;
               }

               if (this.essenceX > this.targetX) {
                  this.velX = this.velX - (this.velMod + 1.0E-5F);
               }

               if (this.essenceX < this.targetX) {
                  this.velX = this.velX + (this.velMod + 1.0E-5F);
               }

               if (this.essenceY > this.targetY) {
                  this.velY = this.velY - (this.velMod + 1.0E-5F);
               }

               if (this.essenceY < this.targetY) {
                  this.velY = this.velY + (this.velMod + 1.0E-5F);
               }

               if (this.velX > 1.5F) {
                  this.velX = 1.5F;
               }

               if (this.velX < -1.5F) {
                  this.velX = -1.5F;
               }

               if (this.velY > 1.5F) {
                  this.velY = 1.5F;
               }

               if (this.velY < -1.5F) {
                  this.velY = -1.5F;
               }

               if ((int)this.essenceX >= this.targetX - 4.0F
                  && (int)this.essenceX <= this.targetX + 4.0F
                  && (int)this.essenceY >= this.targetY - 4.0F
                  && (int)this.essenceY <= this.targetY + 4.0F) {
                  if (this.essenceX > this.targetX) {
                     this.essenceX -= 0.2F;
                  }

                  if (this.essenceX < this.targetX) {
                     this.essenceX += 0.2F;
                  }

                  if (this.essenceY > this.targetY) {
                     this.essenceY -= 0.2F;
                  }

                  if (this.essenceY < this.targetY) {
                     this.essenceY += 0.2F;
                  }
               } else {
                  this.essenceX = this.essenceX + this.velX;
                  this.essenceY = this.essenceY + this.velY;
               }

               if ((int)this.essenceX >= this.targetX - 1.0F
                  && (int)this.essenceX <= this.targetX + 1.0F
                  && (int)this.essenceY >= this.targetY - 1.0F
                  && (int)this.essenceY <= this.targetY + 1.0F) {
                  this.target.increaseEnergy(this.energyValue);
                  this.target.decreaseEnergyInRoute(this.energyValue);
                  MapTilesLoader.TileSet targetType = this.target.getObjectFlags().getBaseType();
                  if (targetType == MapTilesLoader.TileSet.ESSENCE_COLLECTOR
                     || targetType == MapTilesLoader.TileSet.ESSENCE_ALTAR
                     || targetType == MapTilesLoader.TileSet.RELIQUARY) {
                     this.goal.incrementGoal(GoalModule.GoalType.POWER_OVERWHELMING, this.energyValue);
                  }

                  this.essence.removeEssence(this);
               }
            }
         }
      }
   }

   public void render(Graphics g) throws SlickException {
      if (this.map.isInView(this.essenceX, this.essenceY, 10)) {
         float mapX = this.map.getMapX();
         float mapY = this.map.getMapY();
         g.setColor(this.beingDelivered ? this.essenceDotColorBeingDeliveredShadow : this.essenceDotColorShadow);
         g.fillRectEmbedded((int)(mapX + this.essenceX - 1.0F), (int)(mapY + this.essenceY), 3.0F, 1.0F);
         g.fillRectEmbedded((int)(mapX + this.essenceX), (int)(mapY + this.essenceY - 1.0F), 1.0F, 3.0F);
         g.setColor(this.beingDelivered ? this.essenceDotColorBeingDelivered : this.essenceDotColor);
         g.fillRectEmbedded((int)(mapX + this.essenceX), (int)(mapY + this.essenceY), 1.0F, 1.0F);
      }
   }

   private void checkTarget() {
      if (!this.beingDelivered && this.target == null && Utilities.randomInt(100) == 0) {
         ArrayList<ObjectBase> objectList = this.object.getObjectsBySortGroup(ObjectModule.ObjectSortGroup.DELIVERS_ESSENCE);

         for (int i = objectList.size() - 1; i >= 0; i--) {
            ObjectBase o = objectList.get(i);
            if (o.isPaused() || o.getEnergy() + o.getEnergyInRoute() >= o.getObjectFlags().getEnergyMax()) {
               objectList.remove(o);
            }
         }

         if (objectList.size() > 0) {
            ArrayList<ObjectBase> essenceCollectorsSorted = new ArrayList<>();

            for (ObjectBase o : objectList) {
               if (essenceCollectorsSorted.size() == 0) {
                  essenceCollectorsSorted.add(o);
               } else {
                  int dx = (int)(o.getEssenceReturnXOnMap() - this.essenceX / 16.0F);
                  int dy = (int)(o.getEssenceReturnYOnMap() - this.essenceY / 16.0F);
                  int hValue = (int)Math.sqrt(dx * dx + dy * dy);
                  int ix = 0;

                  while (true) {
                     if (ix < essenceCollectorsSorted.size()) {
                        ObjectBase o2 = essenceCollectorsSorted.get(ix);
                        int dx2 = (int)(o2.getEssenceReturnXOnMap() - this.essenceX / 16.0F);
                        int dy2 = (int)(o2.getEssenceReturnYOnMap() - this.essenceY / 16.0F);
                        int hValue2 = (int)Math.sqrt(dx2 * dx2 + dy2 * dy2);
                        if (hValue >= hValue2) {
                           ix++;
                           continue;
                        }

                        essenceCollectorsSorted.add(ix, o);
                     }

                     essenceCollectorsSorted.add(o);
                     break;
                  }
               }
            }

            ObjectBase randomTarget = null;

            for (int ix = 0; ix < essenceCollectorsSorted.size(); ix++) {
               if (Utilities.randomInt(3) == 0) {
                  randomTarget = essenceCollectorsSorted.get(ix);
                  break;
               }
            }

            if (randomTarget == null) {
               randomTarget = essenceCollectorsSorted.get(Utilities.randomInt(essenceCollectorsSorted.size()));
            }

            this.target = randomTarget;
            this.target.increaseEnergyInRoute(this.energyValue);
            this.targetX = this.target.getEssenceReturnXOnMapExactPixel();
            this.targetY = this.target.getEssenceReturnYOnMapExactPixel();
         }
      }

      if (this.target != null) {
         if (this.target.getEnergy() >= this.target.getObjectFlags().getEnergyMax()
            || !this.target.getObjectFlags().canCollectEssence() && !this.target.getObjectFlags().canAcceptEssence()
            || this.target.isPaused()
            || !this.object.objectExists(this.target)) {
            this.target.decreaseEnergyInRoute(this.energyValue);
            this.target = null;
            this.beingDelivered = false;
         }
      }
   }

   public ObjectBase getTarget() {
      return this.target;
   }

   public int getEnergyValue() {
      return this.energyValue;
   }

   public float getEssenceX() {
      return this.essenceX;
   }

   public float getEssenceY() {
      return this.essenceY;
   }

   public void loadSync() {
      if (this.targetID != -1) {
         this.target = this.object.getObjects().get(this.targetID);
      }
   }

   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      if (this.essenceX != 0.0) {
         dataMap.put("essenceX", this.essenceX);
      }

      if (this.essenceY != 0.0) {
         dataMap.put("essenceY", this.essenceY);
      }

      if (this.velX != 0.0) {
         dataMap.put("velX", this.velX);
      }

      if (this.velY != 0.0) {
         dataMap.put("velY", this.velY);
      }

      if (this.velMod != 0.0) {
         dataMap.put("velMod", this.velMod);
      }

      if (this.energyValue != 1) {
         dataMap.put("energyValue", this.energyValue);
      }

      if (this.decayRateTick != 0) {
         dataMap.put("decayRateTick", this.decayRateTick);
      }

      if (this.target != null) {
         dataMap.put("targetID", this.target.getID());
      }

      if (this.targetX != 0.0) {
         dataMap.put("targetX", this.targetX);
      }

      if (this.targetY != 0.0) {
         dataMap.put("targetY", this.targetY);
      }

      if (this.restPeriod != 0) {
         dataMap.put("restPeriod", this.restPeriod);
      }

      if (this.restPeriodTick != 0) {
         dataMap.put("restPeriodTick", this.restPeriodTick);
      }

      if (this.beingDelivered) {
         dataMap.put("beingDelivered", this.beingDelivered);
      }

      return dataMap;
   }

   public boolean isIdle() {
      return this.target == null && !this.beingDelivered;
   }
}
