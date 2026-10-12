package rtr.resources;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.PerkModule;
import rtr.font.FontModule;
import rtr.font.Text;
import rtr.map.MapModule;
import rtr.particles.ParticleModule;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.system.ScaleControl;
import rtr.utilities.Utilities;
import rtr.weather.WeatherModule;

public class FarmlandBase {
   private static int BASE_GROWTH_RATE = 70000;
   private ResourceModule resource = (ResourceModule)StateBase.getModule(ModuleBase.ModuleType.RESOURCE);
   private MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   private WeatherModule weather = (WeatherModule)StateBase.getModule(ModuleBase.ModuleType.WEATHER);
   private ParticleModule particle = (ParticleModule)StateBase.getModule(ModuleBase.ModuleType.PARTICLE);
   private FontModule font = (FontModule)StateBase.getModule(ModuleBase.ModuleType.FONT);
   private PerkModule perk = (PerkModule)StateBase.getModule(ModuleBase.ModuleType.PERK);
   private int farmlandID;
   private float farmlandX;
   private float farmlandY;
   private int tileX;
   private int tileY;
   private ResourceBase cropType;
   private int cropTypeID = -1;
   private Image sprite;
   private int spriteNumber;
   private ResourceModule.ResourceSpriteSet baseSpriteSet;
   private ResourceModule.ResourceSpriteSet spriteSet;
   private boolean isPlanted = false;
   private boolean isGrown = false;
   private int careUpdateRate = 1500;
   private int careUpdateRateTick;
   private int waterAmount = 0;
   private int waterAmountMax = 100;
   private int tendedAmount = 0;
   private int tendedAmountMax = 100;
   private int growthTick = 0;
   private byte stage = 0;
   private int motivatedAmount = 0;

   public FarmlandBase(int id, int x, int y) {
      this.farmlandID = id;
      this.tileX = x;
      this.tileY = y;
      this.farmlandX = x * 16 + Utilities.randomInt(-1, 1);
      this.farmlandY = y * 16 + Utilities.randomInt(-1, 1);
   }

   public FarmlandBase(YMLDataMap dataMap) {
      this.farmlandID = Integer.parseInt(dataMap.getOrDefault("farmlandID", 0));
      this.farmlandX = Float.parseFloat(dataMap.getOrDefault("farmlandX", 0.0F));
      this.farmlandY = Float.parseFloat(dataMap.getOrDefault("farmlandY", 0.0F));
      this.tileX = Integer.parseInt(dataMap.getOrDefault("tileX", 0));
      this.tileY = Integer.parseInt(dataMap.getOrDefault("tileY", 0));
      this.cropTypeID = Integer.parseInt(dataMap.getOrDefault("cropTypeID", 0));
      this.spriteNumber = Integer.parseInt(dataMap.getOrDefault("spriteNumber", 0));
      String baseSpriteSetString = dataMap.getOrDefault("baseSpriteSet", "null");
      if (!baseSpriteSetString.equals("null")) {
         ResourceModule.ResourceSpriteSet[] var6;
         for (ResourceModule.ResourceSpriteSet rSS : ResourceModule.ResourceSpriteSet.values()) {
            if (rSS.toString().equals(baseSpriteSetString)) {
               this.baseSpriteSet = rSS;
               break;
            }
         }
      }

      String spriteSetString = dataMap.getOrDefault("spriteSet", "null");
      if (!spriteSetString.equals("null")) {
         ResourceModule.ResourceSpriteSet[] var7;
         for (ResourceModule.ResourceSpriteSet rSSx : ResourceModule.ResourceSpriteSet.values()) {
            if (rSSx.toString().equals(spriteSetString)) {
               this.spriteSet = rSSx;
               break;
            }
         }
      }

      this.isPlanted = Boolean.parseBoolean(dataMap.getOrDefault("isPlanted", false));
      this.isGrown = Boolean.parseBoolean(dataMap.getOrDefault("isGrown", false));
      this.careUpdateRate = Integer.parseInt(dataMap.getOrDefault("careUpdateRate", 0));
      this.careUpdateRateTick = Integer.parseInt(dataMap.getOrDefault("careUpdateRateTick", 0));
      this.waterAmount = Integer.parseInt(dataMap.getOrDefault("waterAmount", 0));
      this.waterAmountMax = Integer.parseInt(dataMap.getOrDefault("waterAmountMax", 0));
      this.tendedAmount = Integer.parseInt(dataMap.getOrDefault("tendedAmount", 0));
      this.tendedAmountMax = Integer.parseInt(dataMap.getOrDefault("tendedAmountMax", 0));
      this.growthTick = Integer.parseInt(dataMap.getOrDefault("growthTick", 0));
      this.stage = Byte.parseByte(dataMap.getOrDefault("stage", 0));
      this.motivatedAmount = Integer.parseInt(dataMap.getOrDefault("motivatedAmount", 0));
      if (this.spriteSet != null) {
         this.sprite = this.resource.getResourceSprite(this.spriteSet, this.spriteNumber);
      }
   }

   public void update() throws SlickException {
      if (this.isPlanted) {
         this.careUpdateRateTick++;
         if (this.careUpdateRateTick > this.careUpdateRate) {
            if (this.weather.isRaining()) {
               this.waterAmount += 10;
               this.tendedAmount -= 2;
            } else {
               this.waterAmount--;
               if (this.weather.getCurrentTemp() <= this.weather.getFreezePoint() || this.weather.getCurrentTemp() >= this.weather.getFryPoint()) {
                  this.waterAmount -= 2;
               } else if (this.weather.getCurrentTemp() <= this.weather.getColdPoint() || this.weather.getCurrentTemp() >= this.weather.getHotPoint()) {
                  this.waterAmount--;
               }
            }

            if (this.waterAmount < 0) {
               this.waterAmount = 0;
            }

            if (this.waterAmount > this.waterAmountMax) {
               this.waterAmount = this.waterAmountMax;
            }

            this.tendedAmount--;
            if (this.tendedAmount < 0) {
               this.tendedAmount = 0;
            }

            this.careUpdateRateTick = 0;
         }

         if (this.motivatedAmount > 0) {
            if (this.motivatedAmount > 4000) {
               this.growthTick += 20;
               if (Utilities.randomInt(10) == 0) {
                  this.particle
                     .newParticle(
                        Utilities.randomInt(16) + this.tileX * 16,
                        Utilities.randomInt(16) + this.tileY * 16,
                        ParticleModule.ParticleType.FIRE_SPECK,
                        ParticleModule.ParticleSet.FIRE_GREEN
                     );
                  this.particle
                     .newParticle(
                        Utilities.randomInt(16) + this.tileX * 16,
                        Utilities.randomInt(16) + this.tileY * 16,
                        ParticleModule.ParticleType.FIRE_SPECK,
                        ParticleModule.ParticleSet.FIRE_BLUE
                     );
               }
            } else {
               this.growthTick += 15;
               if (Utilities.randomInt(10) == 0) {
                  this.particle
                     .newParticle(
                        Utilities.randomInt(16) + this.tileX * 16,
                        Utilities.randomInt(16) + this.tileY * 16,
                        ParticleModule.ParticleType.FIRE_SPECK,
                        ParticleModule.ParticleSet.FIRE_GREEN
                     );
               }
            }

            if (this.motivatedAmount > 5000) {
               this.motivatedAmount = 5000;
            } else {
               this.motivatedAmount--;
            }
         }

         if (this.tendedAmount > 0) {
            this.growthTick++;
         }

         if (this.waterAmount > 0) {
            this.growthTick++;
         }

         if (this.weather.getCurrentTemp() <= this.weather.getFreezePoint()) {
            this.growthTick -= 2;
         }

         if (this.weather.getCurrentTemp() >= this.weather.getFryPoint()) {
            this.growthTick--;
         }

         if (this.weather.getCurrentTemp() < this.weather.getHotPoint() && this.weather.getCurrentTemp() > this.weather.getColdPoint()) {
            if (this.growthTick < 0) {
               this.growthTick = 0;
            }

            this.growthTick += 2;
         } else if (this.weather.getCurrentTemp() < this.weather.getFryPoint() && this.weather.getCurrentTemp() > this.weather.getFreezePoint()) {
            if (this.growthTick < 0) {
               this.growthTick = 0;
            }

            this.growthTick++;
         }

         if (this.waterAmount > this.waterAmountMax / 3 && Utilities.randomInt(10) == 0) {
            float particleX = this.farmlandX + Utilities.randomInt(-2, 2) + 8.0F;
            float particleY = this.farmlandY + Utilities.randomInt(-2, 2) + 8.0F;
            this.particle.newParticle(particleX, particleY, ParticleModule.ParticleType.WATER_POOL_FAST, ParticleModule.ParticleSet.WATER);
            if (Utilities.randomInt(10) == 0) {
               this.particle.newParticle(particleX, particleY, ParticleModule.ParticleType.WATER_POOL, ParticleModule.ParticleSet.WATER);
            }
         }

         if (this.tendedAmount <= 0 && Utilities.randomInt(80) == 0) {
            float particleX = this.farmlandX + Utilities.randomInt(-2, 2) + 8.0F;
            float particleY = this.farmlandY + Utilities.randomInt(-2, 2) + 8.0F;
            this.particle.newParticle(particleX, particleY, ParticleModule.ParticleType.SMOKE_SPECK, ParticleModule.ParticleSet.BROWN);
         }

         if (this.growthTick < -100) {
            if (this.weather.getCurrentTemp() <= this.weather.getColdPoint() && Utilities.randomInt(30) == 0) {
               float particleX = this.farmlandX + Utilities.randomInt(-2, 2) + 8.0F;
               float particleY = this.farmlandY + Utilities.randomInt(-2, 2) + 8.0F;
               this.particle.newParticle(particleX, particleY, ParticleModule.ParticleType.SMOKE_SPECK, ParticleModule.ParticleSet.MAGIC_BLUE);
               if (Utilities.randomInt(20) == 0) {
                  this.particle.newParticle(particleX, particleY, ParticleModule.ParticleType.MAGIC_FLOAT_SPECK, ParticleModule.ParticleSet.MAGIC_BLUE);
               }
            }

            if (this.weather.getCurrentTemp() >= this.weather.getHotPoint() && Utilities.randomInt(30) == 0) {
               float particleX = this.farmlandX + Utilities.randomInt(-2, 2) + 8.0F;
               float particleY = this.farmlandY + Utilities.randomInt(-2, 2) + 8.0F;
               this.particle.newParticle(particleX, particleY, ParticleModule.ParticleType.SMOKE_SPECK, ParticleModule.ParticleSet.BLACK);
               if (Utilities.randomInt(20) == 0) {
                  this.particle.newParticle(particleX, particleY, ParticleModule.ParticleType.MAGIC_FLOAT_SPECK, ParticleModule.ParticleSet.BLACK);
               }
            }
         }

         if (this.growthTick < BASE_GROWTH_RATE / 3 * -1) {
            this.growthTick = BASE_GROWTH_RATE / 3 * -1;
         }

         if (this.growthTick < 0) {
            switch (this.stage) {
               case 1:
                  this.spriteSet = this.baseSpriteSet.getCropOne();
                  this.spriteNumber = Utilities.randomInt(this.resource.getResourceImages().length);
                  this.sprite = this.resource.getResourceSprite(this.spriteSet, this.spriteNumber);
                  this.stage--;
                  this.growthTick = BASE_GROWTH_RATE / 6 - Utilities.randomInt(BASE_GROWTH_RATE / 12);
                  break;
               case 2:
                  this.spriteSet = this.baseSpriteSet.getCropTwo();
                  this.spriteNumber = Utilities.randomInt(this.resource.getResourceImages().length);
                  this.sprite = this.resource.getResourceSprite(this.spriteSet, this.spriteNumber);
                  this.stage--;
                  this.isGrown = false;
                  this.growthTick = BASE_GROWTH_RATE / 6 - Utilities.randomInt(BASE_GROWTH_RATE / 12);
            }
         }

         int modifiedGrowthRate = BASE_GROWTH_RATE;
         if (this.perk.getPerkTypeTotal(PerkModule.PerkType.FARM_GROWTH_SPEED) > 0.0) {
            modifiedGrowthRate = (int)(modifiedGrowthRate - BASE_GROWTH_RATE * this.perk.getPerkTypeTotal(PerkModule.PerkType.FARM_GROWTH_SPEED));
         }

         if (this.growthTick > modifiedGrowthRate) {
            switch (this.stage) {
               case 0:
                  this.spriteSet = this.baseSpriteSet.getCropTwo();
                  this.spriteNumber = Utilities.randomInt(this.resource.getResourceImages().length);
                  this.sprite = this.resource.getResourceSprite(this.spriteSet, this.spriteNumber);
                  this.stage++;
                  break;
               case 1:
                  this.spriteSet = this.baseSpriteSet.getCropThree();
                  this.spriteNumber = Utilities.randomInt(this.resource.getResourceImages().length);
                  this.sprite = this.resource.getResourceSprite(this.spriteSet, this.spriteNumber);
                  this.stage++;
                  this.isGrown = true;
            }

            this.growthTick = Utilities.randomInt(BASE_GROWTH_RATE / 12);
         }
      }
   }

   public void tendCrop() {
      this.tendedAmount = this.tendedAmountMax;
      if (this.growthTick < 0) {
         this.growthTick = 0;
      }
   }

   public void waterCrop(int i) {
      this.waterAmount += i;
      if (this.waterAmount > this.waterAmountMax) {
         this.waterAmount = this.waterAmountMax;
      }

      if (this.growthTick < 0) {
         this.growthTick = 0;
      }
   }

   public int getTendedAmount() {
      return this.tendedAmount;
   }

   public int getTendedAmountMax() {
      return this.tendedAmountMax;
   }

   public int getWaterAmount() {
      return this.waterAmount;
   }

   public int getWaterAmountMax() {
      return this.waterAmountMax;
   }

   public void setCropType(ResourceBase r) {
      this.isPlanted = true;
      this.cropType = r;
      if (this.cropType.getType() == ResourceModule.ResourceType.RAW_VEGETABLE) {
         this.baseSpriteSet = this.resource.getResourceSpriteSet(r.getType(), r.getColor());
         this.spriteSet = this.baseSpriteSet.getCropOne();
         this.spriteNumber = Utilities.randomInt(this.resource.getResourceImages().length);
         this.sprite = this.resource.getResourceSprite(this.spriteSet, this.spriteNumber);
      }
   }

   public ResourceBase harvest() {
      this.stage = 0;
      this.isGrown = false;
      this.spriteSet = this.baseSpriteSet.getCropOne();
      this.spriteNumber = Utilities.randomInt(this.resource.getResourceImages().length);
      this.sprite = this.resource.getResourceSprite(this.spriteSet, this.spriteNumber);
      if (this.tendedAmount > 0) {
         this.tendedAmount /= 3;
      }

      this.motivatedAmount = 0;
      return this.resource.createResourceUnassigned(this.cropType.getType(), this.cropType.getColor());
   }

   public void removeCrop() {
   }

   public boolean isPlanted() {
      return this.isPlanted;
   }

   public boolean isGrown() {
      return this.isGrown;
   }

   public int getX() {
      return this.tileX;
   }

   public int getY() {
      return this.tileY;
   }

   public void render() {
      if (this.map.isInView(this.farmlandX, this.farmlandY, 10)) {
         float drawX = this.farmlandX + this.map.getMapX();
         float drawY = this.farmlandY + this.map.getMapY();
         if (this.sprite != null) {
            if (this.growthTick < 0) {
               this.sprite.drawEmbedded(drawX, drawY, drawX + 16.0F, drawY + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, new Color(100, 100, 100, 255));
            } else if (this.tendedAmount == 0) {
               this.sprite.drawEmbedded(drawX, drawY, drawX + 16.0F, drawY + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, new Color(150, 150, 150, 255));
            } else {
               this.sprite.drawEmbedded(drawX, drawY, drawX + 16.0F, drawY + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, Color.white);
            }
         }
      }
   }

   public void renderMapWriter() {
      float drawX = this.farmlandX;
      float drawY = this.farmlandY;
      if (this.sprite != null) {
         if (this.growthTick < 0) {
            this.sprite.drawEmbedded(drawX, drawY, drawX + 16.0F, drawY + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, new Color(100, 100, 100, 255));
         } else if (this.tendedAmount == 0) {
            this.sprite.drawEmbedded(drawX, drawY, drawX + 16.0F, drawY + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, new Color(150, 150, 150, 255));
         } else {
            this.sprite.drawEmbedded(drawX, drawY, drawX + 16.0F, drawY + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, Color.white);
         }
      }
   }

   public void renderDebug(Graphics g) {
      if (this.map.isInView(this.farmlandX, this.farmlandY, 10)) {
         g.resetTransform();
         g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
         float drawX = ScaleControl.getTransformedPositionX(this.farmlandX);
         float drawY = ScaleControl.getTransformedPositionY(this.farmlandY);
         this.font.drawString(drawX, drawY, this.waterAmount + " / " + this.tendedAmount, Text.FontType.BODY, 0);
         this.font.drawString(drawX, drawY + 10.0F, String.valueOf(this.growthTick), Text.FontType.BODY, 0);
         g.resetTransform();
         g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
      }
   }

   public float getFarmlandX() {
      return this.farmlandX;
   }

   public float getFarmlandY() {
      return this.farmlandY;
   }

   public int getTileX() {
      return this.tileX;
   }

   public int getTileY() {
      return this.tileY;
   }

   public int getGrowthTick() {
      return this.growthTick;
   }

   public int getID() {
      return this.farmlandID;
   }

   public void loadSync() {
      if (this.cropTypeID != -1) {
         this.cropType = this.resource.getResource(this.cropTypeID);
      }
   }

   public boolean isTended() {
      return this.tendedAmount > 0;
   }

   public boolean isWatered() {
      return this.waterAmount > 0;
   }

   public void damageCrops() {
      if (this.isPlanted()) {
         if (this.isTended()) {
            this.tendedAmount -= 20;
            if (this.tendedAmount < 0) {
               this.tendedAmount = 0;
            }
         } else {
            this.growthTick -= 3000;
         }
      }
   }

   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("farmlandID", this.farmlandID);
      dataMap.put("farmlandX", this.farmlandX);
      dataMap.put("farmlandY", this.farmlandY);
      dataMap.put("tileX", this.tileX);
      dataMap.put("tileY", this.tileY);
      dataMap.put("cropTypeID", this.cropType != null ? this.cropType.getID() : "-1");
      dataMap.put("spriteNumber", this.spriteNumber);
      dataMap.put("baseSpriteSet", this.baseSpriteSet != null ? this.baseSpriteSet.toString() : "null");
      dataMap.put("spriteSet", this.spriteSet != null ? this.spriteSet.toString() : "null");
      dataMap.put("isPlanted", this.isPlanted);
      dataMap.put("isGrown", this.isGrown);
      dataMap.put("careUpdateRate", this.careUpdateRate);
      dataMap.put("careUpdateRateTick", this.careUpdateRateTick);
      dataMap.put("waterAmount", this.waterAmount);
      dataMap.put("waterAmountMax", this.waterAmountMax);
      dataMap.put("tendedAmount", this.tendedAmount);
      dataMap.put("tendedAmountMax", this.tendedAmountMax);
      dataMap.put("growthTick", this.growthTick);
      dataMap.put("stage", this.stage);
      dataMap.put("motivatedAmount", this.motivatedAmount);
      return dataMap;
   }

   public void motivate() {
      if (this.isPlanted && !this.isGrown) {
         this.motivatedAmount = this.motivatedAmount + Utilities.randomInt(250) + 250;
      }
   }
}
