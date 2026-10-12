package rtr.resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import rtr.ImageLoader;
import rtr.ModuleBase;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.TimeModule;
import rtr.console.Console;
import rtr.font.FontModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.map.DepthModule;
import rtr.map.MapData;
import rtr.map.MapModule;
import rtr.mobs.MobBase;
import rtr.mobs.MobFactory;
import rtr.mobs.MobModule;
import rtr.objects.ObjectBase;
import rtr.objects.ObjectModule;
import rtr.particles.ParticleModule;
import rtr.save.YMLDataMap;
import rtr.spawning.CorruptionModule;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.system.gamemodetemplates.GameModeTemplateBase;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;
import rtr.weather.WeatherModule;

public class ResourceBase {
   private MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   private ResourceModule resource = (ResourceModule)StateBase.getModule(ModuleBase.ModuleType.RESOURCE);
   private ParticleModule particle = (ParticleModule)StateBase.getModule(ModuleBase.ModuleType.PARTICLE);
   private MobModule mob = (MobModule)StateBase.getModule(ModuleBase.ModuleType.MOB);
   private ObjectModule object = (ObjectModule)StateBase.getModule(ModuleBase.ModuleType.OBJECT);
   private FontModule font = (FontModule)StateBase.getModule(ModuleBase.ModuleType.FONT);
   private DepthModule depth = (DepthModule)StateBase.getModule(ModuleBase.ModuleType.DEPTH);
   private SoundModule sound = (SoundModule)StateBase.getModule(ModuleBase.ModuleType.SOUND);
   private PerkModule perk = (PerkModule)StateBase.getModule(ModuleBase.ModuleType.PERK);
   private WeatherModule weather = (WeatherModule)StateBase.getModule(ModuleBase.ModuleType.WEATHER);
   private TimeModule time = (TimeModule)StateBase.getModule(ModuleBase.ModuleType.TIME);
   private CorruptionModule corruption = (CorruptionModule)StateBase.getModule(ModuleBase.ModuleType.CORRUPTION);
   private GoalModule goal = (GoalModule)StateBase.getModule(ModuleBase.ModuleType.GOAL);
   private int resourceID = -1;
   private float resourceX;
   private float resourceY;
   private float resourceXPickupOffset;
   private float resourceYPickupOffset;
   private int tileX;
   private int tileY;
   private ResourceModule.ResourceType type;
   private Image sprite;
   private int spriteNumber;
   private ResourceModule.ResourceSpriteSet spriteSet;
   private ResourceModule.ResourceColorSet color = ResourceModule.ResourceColorSet.DEFAULT;
   private int resourceDurability;
   private int resourceDurabilityMax;
   private int uses = 1;
   private int usesMax = 1;
   private MobBase carrier;
   private int carrierID = -1;
   private MobBase assigned;
   private int assignedID = -1;
   private ObjectBase building;
   private int buildingID = -1;
   private MobBase captured;
   private int capturedID = -1;
   private MobBase boundGhost;
   private int boundGhostID = -1;
   private boolean onGround;
   private boolean inBuilding;
   private boolean carried;
   private boolean inInventory;
   private boolean grabbed;
   private boolean dropping;
   protected int grabbedDropTick;
   protected Image resourceArrow;
   private float resourceArrowBounce = 0.0F;
   private float resourceArrowBounceSpeed = 0.0F;

   public int getResourceDurability() {
      return this.resourceDurability;
   }

   public int getResourceDurabilityMax() {
      return this.resourceDurabilityMax;
   }

   public void increaseDurability(int i) {
      this.resourceDurability += i;
      if (this.resourceDurability > this.resourceDurabilityMax) {
         this.resourceDurability = this.resourceDurabilityMax;
      }
   }

   public Image getResourceArrow() {
      return this.resourceArrow;
   }

   public ResourceBase(int iD, ResourceModule.ResourceType t, ResourceModule.ResourceColorSet rC) {
      this.setupResource(iD, t, rC);
   }

   public ResourceBase(int iD, int tX, int tY, ResourceModule.ResourceType t, ResourceModule.ResourceColorSet rC) throws SlickException {
      this.setupResource(iD, t, rC);
      this.placeOnGround(tX, tY);
   }

   public ResourceBase(int iD, ObjectBase b, ResourceModule.ResourceType t, ResourceModule.ResourceColorSet rC) {
      this.setupResource(iD, t, rC);
      this.placeInBuilding(b);
   }

   public ResourceBase(int iD, MobBase m, ResourceModule.ResourceType t, ResourceModule.ResourceColorSet rC) {
      this.setupResource(iD, t, rC);
      this.placeCarried(m);
   }

   public ResourceBase(YMLDataMap dataMap) throws SlickException {
      this(dataMap, false);
   }

   public ResourceBase(YMLDataMap dataMap, boolean readOnly) throws SlickException {
      this.resourceID = Integer.parseInt(dataMap.getOrDefault("resourceID", 0));
      this.resourceX = Float.parseFloat(dataMap.getOrDefault("resourceX", 0.0F));
      this.resourceY = Float.parseFloat(dataMap.getOrDefault("resourceY", 0.0F));
      this.resourceXPickupOffset = Float.parseFloat(dataMap.getOrDefault("resourceXPickupOffset", 0.0F));
      this.resourceYPickupOffset = Float.parseFloat(dataMap.getOrDefault("resourceYPickupOffset", 0.0F));
      this.tileX = Integer.parseInt(dataMap.getOrDefault("tileX", 0));
      this.tileY = Integer.parseInt(dataMap.getOrDefault("tileY", 0));
      String typeString = dataMap.getOrDefault("type", "null");
      if (typeString.equals("RAW_FOOD")) {
         Console.saveConverterOut(this, "InDev 33f", "InDev 34 Unstable 2", "Converting old RAW_FOOD into RAW_VEGETABLE.");
         typeString = "RAW_VEGETABLE";
      }

      if (typeString.equals("WOOD_SHIELD")) {
         Console.saveConverterOut(this, "InDev 33f", "InDev 34 Unstable 2", "Converting old WOOD_SHIELD into LEATHER_SHIELD.");
         typeString = "LEATHER_SHIELD";
      }

      ResourceModule.ResourceType[] var7;
      for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
         if (t.toString().equals(typeString)) {
            this.type = t;
            break;
         }
      }

      this.spriteNumber = Integer.parseInt(dataMap.getOrDefault("spriteNumber", 0));
      this.spriteSet = this.resource.getResourceSpriteSet(this.type, this.color);
      String colorString = dataMap.getOrDefault("color", "null");

      ResourceModule.ResourceColorSet[] var8;
      for (ResourceModule.ResourceColorSet cS : ResourceModule.ResourceColorSet.values()) {
         if (cS.toString().equals(colorString)) {
            this.color = cS;
            break;
         }
      }

      this.resourceDurability = Integer.parseInt(dataMap.getOrDefault("resourceDurability", 0));
      this.resourceDurabilityMax = this.type.getTemplate().getBaseDurability();
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.RESOURCE_DECAY_RATE) > 0.0) {
         this.resourceDurabilityMax = (int)(
            this.resourceDurabilityMax + this.resourceDurabilityMax * this.perk.getPerkTypeTotal(PerkModule.PerkType.RESOURCE_DECAY_RATE)
         );
      }

      this.uses = Integer.parseInt(dataMap.getOrDefault("uses", 1));
      this.usesMax = this.type.getTemplate().getUsesMax();
      if (this.type.getTemplate().getResourceCategory() == ResourceModule.ResourceCategory.TOOLS
         && this.perk.getPerkTypeTotal(PerkModule.PerkType.TOOL_DURABILITY) > 0.0) {
         this.usesMax = (int)(this.usesMax + this.usesMax * this.perk.getPerkTypeTotal(PerkModule.PerkType.TOOL_DURABILITY));
      }

      if (this.type.getTemplate().getResourceCategory() == ResourceModule.ResourceCategory.FOOD_AND_WATER
         && this.perk.getPerkTypeTotal(PerkModule.PerkType.FOOD_AND_WATER_VALUE) > 0.0) {
         this.usesMax = (int)(this.usesMax + this.usesMax * this.perk.getPerkTypeTotal(PerkModule.PerkType.FOOD_AND_WATER_VALUE));
      }

      this.carrierID = Integer.parseInt(dataMap.getOrDefault("carrierID", -1));
      this.assignedID = Integer.parseInt(dataMap.getOrDefault("assignedID", -1));
      this.buildingID = Integer.parseInt(dataMap.getOrDefault("buildingID", -1));
      this.capturedID = Integer.parseInt(dataMap.getOrDefault("capturedID", -1));
      this.boundGhostID = Integer.parseInt(dataMap.getOrDefault("boundGhostID", -1));
      this.onGround = Boolean.parseBoolean(dataMap.getOrDefault("onGround", false));
      this.inBuilding = Boolean.parseBoolean(dataMap.getOrDefault("inBuilding", false));
      this.carried = Boolean.parseBoolean(dataMap.getOrDefault("carried", false));
      this.inInventory = Boolean.parseBoolean(dataMap.getOrDefault("inInventory", false));
      this.grabbed = Boolean.parseBoolean(dataMap.getOrDefault("grabbed", false));
      this.dropping = Boolean.parseBoolean(dataMap.getOrDefault("dropping", false));
      this.grabbedDropTick = Integer.parseInt(dataMap.getOrDefault("grabbedDropTick", 0));
      if (this.onGround && !readOnly) {
         this.resource.setResourcesOnGround(this.tileX, this.tileY, this);
      }

      this.sprite = this.resource.getResourceSprite(this.spriteSet, this.spriteNumber);

      try {
         this.resourceArrow = ImageLoader.getImage("res/resourceArrow.png");
      } catch (SlickException var9) {
         var9.printStackTrace();
      }
   }

   private void setupResource(int iD, ResourceModule.ResourceType t, ResourceModule.ResourceColorSet rC) {
      this.resourceID = iD;
      this.color = rC;

      try {
         this.resourceArrow = ImageLoader.getImage("res/resourceArrow.png");
      } catch (SlickException var5) {
         var5.printStackTrace();
      }

      this.spriteSet = this.resource.getResourceSpriteSet(t, this.color);
      this.spriteNumber = Utilities.randomInt(this.resource.getResourceImages().length);
      this.sprite = this.resource.getResourceSprite(this.spriteSet, this.spriteNumber);
      this.type = t;
      this.resourceDurabilityMax = this.type.getTemplate().getBaseDurability();
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.RESOURCE_DECAY_RATE) > 0.0) {
         this.resourceDurabilityMax = (int)(
            this.resourceDurabilityMax + this.resourceDurabilityMax * this.perk.getPerkTypeTotal(PerkModule.PerkType.RESOURCE_DECAY_RATE)
         );
      }

      this.resourceDurability = this.resourceDurabilityMax;
      this.usesMax = this.type.getTemplate().getUsesMax();
      if (this.type.getTemplate().getResourceCategory() == ResourceModule.ResourceCategory.TOOLS
         && this.perk.getPerkTypeTotal(PerkModule.PerkType.TOOL_DURABILITY) > 0.0) {
         this.usesMax = (int)(this.usesMax + this.usesMax * this.perk.getPerkTypeTotal(PerkModule.PerkType.TOOL_DURABILITY));
      }

      if (this.type.getTemplate().getResourceCategory() == ResourceModule.ResourceCategory.FOOD_AND_WATER
         && this.perk.getPerkTypeTotal(PerkModule.PerkType.FOOD_AND_WATER_VALUE) > 0.0) {
         this.usesMax = (int)(this.usesMax + this.usesMax * this.perk.getPerkTypeTotal(PerkModule.PerkType.FOOD_AND_WATER_VALUE));
      }

      this.uses = this.usesMax;
   }

   public void render() {
      if (this.captured == null && (this.onGround || this.carried || this.grabbed || this.dropping)) {
         int depthAmount = 0;
         if (this.carried) {
            depthAmount = this.depth.checkParticleDepth((int)this.carrier.getMobX() + 8, (int)this.carrier.getMobY() + 16);

            for (ResourceBase r : this.carrier.getCarriedResourcesList()) {
               if (r == this) {
                  break;
               }

               depthAmount -= 4;
            }
         }

         if (this.map.isInView(this.resourceX, this.resourceY, 10)) {
            float mapX = this.map.getMapX();
            float mapY = this.map.getMapY();
            if (this.isResourceNearDecay()) {
               this.sprite
                  .drawEmbedded(
                     this.resourceX + mapX,
                     this.resourceY + mapY + depthAmount,
                     this.resourceX + mapX + 16.0F,
                     this.resourceY + mapY + depthAmount + 16.0F,
                     0.0F,
                     0.0F,
                     16.0F,
                     16.0F,
                     new Color(150, 150, 150, 255)
                  );
            } else {
               this.sprite
                  .drawEmbedded(
                     this.resourceX + mapX,
                     this.resourceY + mapY + depthAmount,
                     this.resourceX + mapX + 16.0F,
                     this.resourceY + mapY + depthAmount + 16.0F,
                     0.0F,
                     0.0F,
                     16.0F,
                     16.0F,
                     Color.white
                  );
            }
         }
      }
   }

   public void renderOverlay() throws SlickException {
      if (this.map.isInView(this.resourceX, this.resourceY, 10)) {
         float mapX = this.map.getMapX();
         float mapY = this.map.getMapY();
         if (this.isAssigned() && this.onGround) {
            int colorAdjustment = 255;
            int pathStep = this.assigned.getActiveNode().getPathStep();
            if (pathStep > 30) {
               pathStep = 30;
            }

            pathStep = (int)(Math.round(pathStep / 5.0) * 5L);
            colorAdjustment -= pathStep * 5;
            if (colorAdjustment < 0) {
               colorAdjustment = 0;
            }

            this.resourceArrow
               .drawEmbedded(
                  this.resourceX + mapX + 6.5F,
                  this.resourceY + mapY + 2.0F + this.resourceArrowBounce,
                  this.resourceX + mapX + this.resourceArrow.getWidth() + 6.5F,
                  this.resourceY + mapY + this.resourceArrow.getHeight() + 2.0F + this.resourceArrowBounce,
                  0.0F,
                  0.0F,
                  this.resourceArrow.getWidth(),
                  this.resourceArrow.getHeight(),
                  new Color(255 - colorAdjustment, colorAdjustment, 255 - colorAdjustment, 255)
               );
         } else if (this.grabbed) {
            float targetTileX = this.getCenterPixelX(true) / 16 * 16 + mapX - 1.5F + 8.0F;
            float targetTileY = (this.getCenterPixelY(true) + 34) / 16 * 16 + mapY - 4.0F + 8.0F;
            this.particle
               .newParticleGroup(
                  targetTileX - mapX + Utilities.randomInt(-2, 2),
                  targetTileY - mapY + this.resourceArrowBounce + Utilities.randomInt(-2, 2),
                  ParticleModule.ParticleGroup.MAGIC_FIRE,
                  ParticleModule.ParticleSet.FIRE_BLUE
               );
            this.resourceArrow
               .drawEmbedded(
                  targetTileX,
                  targetTileY + this.resourceArrowBounce,
                  targetTileX + this.resourceArrow.getWidth(),
                  targetTileY + this.resourceArrow.getHeight() + this.resourceArrowBounce,
                  0.0F,
                  0.0F,
                  this.resourceArrow.getWidth(),
                  this.resourceArrow.getHeight(),
                  Color.cyan
               );
         }
      }
   }

   public void renderMapWriter() {
      if (this.captured == null && (this.onGround || this.carried || this.dropping)) {
         int depthAmount = 0;
         if (this.carried) {
            depthAmount = this.depth.checkParticleDepth((int)this.carrier.getMobX() + 8, (int)this.carrier.getMobY() + 16);
         }

         if (this.isResourceNearDecay()) {
            this.sprite
               .drawEmbedded(
                  this.resourceX,
                  this.resourceY + depthAmount,
                  this.resourceX + 16.0F,
                  this.resourceY + depthAmount + 16.0F,
                  0.0F,
                  0.0F,
                  16.0F,
                  16.0F,
                  new Color(150, 150, 150, 255)
               );
         } else {
            this.sprite
               .drawEmbedded(
                  this.resourceX,
                  this.resourceY + depthAmount,
                  this.resourceX + 16.0F,
                  this.resourceY + depthAmount + 16.0F,
                  0.0F,
                  0.0F,
                  16.0F,
                  16.0F,
                  Color.white
               );
         }
      }
   }

   public void renderSprite(int x, int y, boolean showUses) {
      this.sprite.draw(x, y);
      if (showUses && this.type.getTemplate().getUsesMax() > 1) {
         this.font.drawString(x + 1, y + 9, String.valueOf(this.uses), Text.FontType.BODY, 0);
      }
   }

   public void update() throws SlickException {
      if (this.onGround && this.isAssigned() || this.grabbed) {
         this.resourceArrowBounceSpeed += 0.025F;
         this.resourceArrowBounce = this.resourceArrowBounce + this.resourceArrowBounceSpeed;
         if (this.resourceArrowBounce >= 0.0F) {
            this.resourceArrowBounceSpeed = -0.5F;
         }
      }

      this.tileUpdate();
      this.checkPosition();
      if (this.onGround
         && this.type.getTemplate().getResourceCategory() == ResourceModule.ResourceCategory.TRASH
         && Game.getCS().getGameMode() != GameModeTemplateBase.GameMode.PEACEFUL
         && !this.corruption.isTileCorrupted(this.tileX, this.tileY)) {
         int chance = 50000;
         if (this.weather.isRaining()) {
            chance -= 5000;
         }

         if (this.time.getTimeOfDay() == TimeModule.TimeOfDay.DUSK
            || this.time.getTimeOfDay() == TimeModule.TimeOfDay.NIGHT
            || this.time.getTimeOfDay() == TimeModule.TimeOfDay.DAWN) {
            chance -= 5000;
         }

         if (Utilities.randomInt(chance) == 0) {
            int amountNearby = 0;

            for (int x = -1; x <= 1; x++) {
               for (int y = -1; y <= 1; y++) {
                  if (x != 0 || y != 0) {
                     ResourceBase r = this.resource.getResourceOnGround(this.tileX + x, this.tileY + y);
                     if (r != null && r.getType().getTemplate().getResourceCategory() == ResourceModule.ResourceCategory.TRASH) {
                        amountNearby++;
                     }
                  }
               }
            }

            if (amountNearby >= 2 && Utilities.randomInt(100 - amountNearby * 10) == 0) {
               if (amountNearby >= 4 && Utilities.randomInt(3) == 0) {
                  this.mob.newMob(this.tileX, this.tileY, MobBase.MobType.TRASHY_SLIME, true);
               } else {
                  this.mob.newMob(this.tileX, this.tileY, MobBase.MobType.SMALL_TRASHY_SLIME, true);
               }

               this.goal.incrementGoal(GoalModule.GoalType.WHATS_THAT_SMELL);
            }
         }
      }

      if (this.carried) {
         if (this.resourceYPickupOffset > this.carrier.getSprite().getCarriedResourceOffsetY().intValue()) {
            this.resourceYPickupOffset = (float)(this.resourceYPickupOffset - 0.4);
         }

         if (this.resourceYPickupOffset < this.carrier.getSprite().getCarriedResourceOffsetY().intValue()) {
            this.resourceYPickupOffset = this.carrier.getSprite().getCarriedResourceOffsetY().intValue();
         }

         float targetX = this.carrier.getMobX() + this.carrier.getSprite().getCarriedResourceOffsetX().intValue();
         float targetY = this.carrier.getMobY() + this.resourceYPickupOffset;
         float distanceX = Math.abs(this.resourceX - targetX);
         float distanceY = Math.abs(this.resourceY - targetY);
         float speedX = this.carrier.getCurrentVelocity() + distanceX / 10.0F;
         float speedY = this.carrier.getCurrentVelocity() + distanceY / 10.0F;
         if (this.resourceX < targetX) {
            this.resourceX += speedX;
         }

         if (this.resourceX > targetX) {
            this.resourceX -= speedX;
         }

         if (this.resourceY < targetY) {
            this.resourceY += speedY;
         }

         if (this.resourceY > targetY) {
            this.resourceY -= speedY;
         }

         if (distanceX <= 1.0F) {
            this.resourceX = targetX;
         }

         if (distanceY <= 1.0F) {
            this.resourceY = targetY;
         }
      } else {
         this.resourceXPickupOffset = 0.0F;
         this.resourceYPickupOffset = 0.0F;
      }

      if (this.inInventory) {
         this.resourceX = this.carrier.getMobX();
         this.resourceY = this.carrier.getMobY();
      } else if (this.grabbed) {
         this.resourceX = Game.getCS().getMouseOnMapX() - 7;
         this.resourceY = Game.getCS().getMouseOnMapY() - 9;
         if (this.resourceX > 4064.0F) {
            this.resourceX = 4064.0F;
         } else if (this.resourceX <= 1.0F) {
            this.resourceX = 16.0F;
         }

         if (this.resourceY > 4064.0F) {
            this.resourceY = 4064.0F;
         } else if (this.resourceY <= 1.0F) {
            this.resourceY = 16.0F;
         }
      } else if (this.dropping) {
         int randomX = 0;
         int randomY = 0;
         this.resourceY += 2.0F;
         if (this.resourceY > 4064.0F) {
            this.resourceY = 4064.0F;
         } else if (this.resourceY <= 1.0F) {
            this.resourceY = 16.0F;
         }

         for (int x = 0; x < 2; x++) {
            randomX = (int)(this.resourceX + Utilities.randomInt(16));
            randomY = (int)(this.resourceY + Utilities.randomInt(16) + 8.0F);
            this.particle.newParticle(randomX, randomY, ParticleModule.ParticleType.SMOKE_SPECK, ParticleModule.ParticleSet.GRAY);
         }

         this.grabbedDropTick++;
         if (this.grabbedDropTick > 16) {
            this.tileUpdateForced();
            ObjectBase objectCheck = this.object.getBuildingAtLocation(this.tileX, this.tileY);
            if (objectCheck != null
               && objectCheck.getObjectFlags().isCullisGate()
               && Math.abs(this.tileX - objectCheck.getFunctionalXOnMap()) <= 1
               && Math.abs(this.tileY - objectCheck.getFunctionalYOnMap()) <= 1) {
               objectCheck.sacrificeToCullisGate(this);
            } else {
               for (int x = 0; x < 200; x++) {
                  randomX = (int)(this.resourceX + Utilities.randomInt(16));
                  randomY = (int)(this.resourceY + this.sprite.getHeight());
                  this.particle
                     .newParticleGroup(randomX, randomY, Utilities.randomInt(360), ParticleModule.ParticleGroup.DUST, ParticleModule.ParticleSet.BLACK);
               }

               this.grabbedDropTick = 0;
               ObjectBase building = this.object.getBuildingAtLocation(this.tileX, this.tileY);
               if (building == null || building.getAcceptStorageResourceCount(this.type, -1) <= 0 && building.getAcceptBuildResourceCount(this.type, -1) <= 0) {
                  if (this.resource.isResourcesOnGround(this.tileX, this.tileY)) {
                     OrderedPair o = this.resource.findOpenSpace(this.tileX, this.tileY, MapData.BlockMapGroup.STANDARD);
                     this.resource.setResourcesOnGround(o.getX(), o.getY(), this);
                  } else {
                     this.resource.setResourcesOnGround(this.tileX, this.tileY, this);
                  }
               } else {
                  this.resource.setResourceInBuilding(building, this);
                  building.increaseStores(this);
                  this.goal.incrementGoal(GoalModule.GoalType.FINE_ILL_DO_IT_MYSELF);
               }

               for (MobBase m : this.mob.getMobsAt(this.tileX, this.tileY)) {
                  if (m.getMobFlags().getAllowIncapacitated() && m.isAvailable()) {
                     m.interruptNode(m.getAIIncapacitated());
                  }
               }
            }
         }
      }

      if ((this.onGround || this.carried) && this.type == ResourceModule.ResourceType.EERIE_VESSEL && this.boundGhost == null) {
         ArrayList<MobBase.MobType> validGhosts = new ArrayList<>();

         MobBase.MobType[] var31;
         for (MobBase.MobType mT : MobBase.MobType.values()) {
            if (MobFactory.getMobInstance(mT).getMobFlags().getGhostBaseType() != null) {
               validGhosts.add(mT);
            }
         }

         Collections.shuffle(validGhosts);

         try {
            MobBase mx = this.mob.newMob(this.tileX, this.tileY, validGhosts.get(0), true);
            this.setBoundGhost(mx);
            mx.setBoundGhost(this);
         } catch (SlickException var8) {
            var8.printStackTrace();
         }
      }
   }

   public void moveResource(OrderedPair o) throws SlickException {
      this.moveResource(o.getX(), o.getY());
   }

   public void moveResource(int x, int y) throws SlickException {
      this.resource.moveResourcesOnGround(this.tileX, this.tileY, x, y, this);
   }

   public void checkPosition() throws SlickException {
      if (this.onGround && this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[this.tileX][this.tileY] > 0) {
         OrderedPair o = this.resource.findOpenSpace(this.tileX, this.tileY, MapData.BlockMapGroup.STANDARD);
         this.moveResource(o.getX(), o.getY());
      }
   }

   public float getResourceX() {
      return this.resourceX;
   }

   public float getResourceY() {
      return this.resourceY;
   }

   public int getTileX() {
      return this.tileX;
   }

   public int getTileY() {
      return this.tileY;
   }

   protected void tileUpdate() {
      if ((int)this.resourceX % 16 < 4) {
         this.tileX = (int)this.resourceX / 16;
      }

      if ((int)this.resourceY % 16 < 4) {
         this.tileY = (int)this.resourceY / 16;
      }
   }

   public void tileUpdateForced() {
      this.tileX = this.getCenterPixelX(true) / 16;
      this.tileY = this.getCenterPixelY(true) / 16;
   }

   public ResourceModule.ResourceType getType() {
      return this.type;
   }

   public ResourceModule.ResourceColorSet getColor() {
      return this.color;
   }

   public Image getSprite() {
      return this.sprite;
   }

   public boolean isOnGround() {
      return this.onGround;
   }

   public boolean isInBuilding() {
      return this.inBuilding;
   }

   public boolean isCarried() {
      return this.carried;
   }

   public boolean isInInventory() {
      return this.inInventory;
   }

   public boolean isGrabbed() {
      return this.grabbed;
   }

   public boolean isDropping() {
      return this.dropping;
   }

   public void assign(MobBase m) {
      this.assigned = m;
   }

   public void unassign() {
      this.assigned = null;
   }

   public MobBase getAssigned() {
      return this.assigned;
   }

   public boolean isAssigned() {
      return this.assigned != null;
   }

   public void capture(MobBase m) throws SlickException {
      m.setCaptured(this, true);
      this.captured = m;
   }

   public void uncapture() {
      if (this.captured != null) {
         this.captured.uncapture();
         this.captured = null;
      }
   }

   public MobBase getCaptured() {
      return this.captured;
   }

   public boolean hasCaptured() {
      return this.captured != null;
   }

   public void setBoundGhost(MobBase m) throws SlickException {
      this.boundGhost = m;
   }

   public void unboundGhost() {
      this.boundGhost = null;
   }

   public MobBase getBoundGhost() {
      return this.boundGhost;
   }

   public boolean hasBoundGhost() {
      return this.boundGhost != null;
   }

   public void placeOnGround(int x, int y) throws SlickException {
      this.resetFlags();
      this.onGround = true;
      this.resourceX = x * 16 + Utilities.randomInt(-1, 1);
      this.resourceY = y * 16 + Utilities.randomInt(-3, 3);
      this.tileX = x;
      this.tileY = y;
      this.sound.playSound(SoundModule.SoundType.DROP_RESOURCE, this.resourceX, this.resourceY);
      if (this.type.getTemplate().isDestroyedWhenDropped()) {
         this.resource.deleteResource(this);
      }
   }

   public void placeInBuilding(ObjectBase b) {
      this.resetFlags();
      this.inBuilding = true;
      this.building = b;
   }

   public void forcePositionMob() {
      this.forcePositionMob(this.carrier);
   }

   public void forcePositionMob(MobBase m) {
      if (m != null) {
         this.resourceX = m.getMobX() + m.getSprite().getCarriedResourceOffsetX().intValue();
         this.resourceY = m.getMobY() + m.getSprite().getCarriedResourceOffsetY().intValue();
         this.resourceXPickupOffset = m.getSprite().getCarriedResourceOffsetX().intValue();
         this.resourceYPickupOffset = m.getSprite().getCarriedResourceOffsetY().intValue();
         this.tileX = m.getTileX();
         this.tileY = m.getTileY();
      }
   }

   public void placeCarried(MobBase m) {
      this.resetFlags();
      this.carried = true;
      this.carrier = m;
      this.tileX = m.getTileX();
      this.tileY = m.getTileY();
   }

   public void placeInInventory(MobBase m) {
      this.resetFlags();
      this.inInventory = true;
      this.carrier = m;
      this.resourceX = m.getMobX();
      this.resourceY = m.getMobY() - 16.0F + 2.0F;
      this.tileX = m.getTileX();
      this.tileY = m.getTileY();
   }

   public void grab() {
      this.resetFlags();
      this.grabbed = true;
   }

   public void drop() {
      this.resetFlags();
      this.dropping = true;
   }

   public void plantInCrop() {
      this.resetFlags();
   }

   public void damageResource(int i) throws SlickException {
      if (this.onGround
         && this.type.getTemplate().getResourceCategory() == ResourceModule.ResourceCategory.TRASH
         && this.corruption.isTileCorrupted(this.tileX, this.tileY)
         && Utilities.randomInt(200) == 0) {
         this.resource.deleteResource(this);
      }

      if (this.resourceDurabilityMax != -1) {
         if (this.onGround && this.corruption.isTileCorrupted(this.tileX, this.tileY)) {
            this.resourceDurability -= i;
         }

         this.resourceDurability -= i;
         if (this.resourceDurability <= 0) {
            this.resource.deleteResource(this);
            if (this.type.getTemplate().getTrashResource() != null) {
               this.resource.createResourceOnGround(this.type.getTemplate().getTrashResource(), this.color, this.tileX, this.tileY);
               this.goal.incrementGoal(GoalModule.GoalType.HOARDER);
            }

            for (int x = 0; x < 10; x++) {
               this.particle
                  .newParticle(
                     this.getCenterPixelX(true) + Utilities.randomInt(-3, 3),
                     this.getCenterPixelY(true) + Utilities.randomInt(-3, 3),
                     ParticleModule.ParticleType.SMOKE_SPECK,
                     ParticleModule.ParticleSet.GRAY
                  );
            }
         } else if (this.isResourceNearDecay()) {
            for (int x = 0; x < 3; x++) {
               this.particle
                  .newParticle(
                     this.getCenterPixelX(true) + Utilities.randomInt(-3, 3),
                     this.getCenterPixelY(true) + Utilities.randomInt(-3, 3),
                     ParticleModule.ParticleType.SMOKE_SPECK,
                     ParticleModule.ParticleSet.GRAY
                  );
            }
         }
      }
   }

   public boolean isResourceNearDecay() {
      return this.resourceDurabilityMax != -1 && this.resourceDurability < this.resourceDurabilityMax * 0.1;
   }

   public void increaseUses(int i) {
      this.uses += i;
      if (this.uses > this.usesMax) {
         this.uses = this.usesMax;
      }
   }

   public void decreaseUses(int i) throws SlickException {
      this.uses -= i;
      if (this.uses <= 0) {
         this.resource.deleteResource(this);

         for (int x = 0; x < 10; x++) {
            this.particle
               .newParticle(
                  this.getCenterPixelX(true) + Utilities.randomInt(-3, 3),
                  this.getCenterPixelY(true) + Utilities.randomInt(-3, 3),
                  ParticleModule.ParticleType.SMOKE_SPECK,
                  ParticleModule.ParticleSet.GRAY
               );
         }
      } else if (this.uses < this.usesMax * 0.1) {
         for (int x = 0; x < 3; x++) {
            this.particle
               .newParticle(
                  this.getCenterPixelX(true) + Utilities.randomInt(-3, 3),
                  this.getCenterPixelY(true) + Utilities.randomInt(-3, 3),
                  ParticleModule.ParticleType.SMOKE_SPECK,
                  ParticleModule.ParticleSet.GRAY
               );
         }
      }
   }

   public void setUses(int i) {
      this.uses = i;
      if (this.uses > this.usesMax) {
         this.uses = this.usesMax;
      }
   }

   public int getUses() {
      return this.uses;
   }

   public int getUsesMax() {
      return this.usesMax;
   }

   public void resetFlags() {
      this.onGround = false;
      this.inBuilding = false;
      this.carried = false;
      this.inInventory = false;
      this.grabbed = false;
      this.dropping = false;
      this.carrier = null;
      this.building = null;
   }

   public int getID() {
      return this.resourceID;
   }

   public void setID(int i) {
      this.resourceID = i;
   }

   public MobBase getCarrier() {
      return this.carrier;
   }

   public ObjectBase getBuilding() {
      return this.building;
   }

   public void loadSync() {
      if (this.carrierID != -1) {
         MobBase c = this.mob.getMobHashMap().get(this.carrierID);
         if (this.carried) {
            this.resource.setResourceCarried(c, this);
         } else if (this.inInventory) {
            this.resource.setResourceInInventory(c, this);
         }
      }

      if (this.assignedID != -1) {
         MobBase a = this.mob.getMobHashMap().get(this.assignedID);
         this.assigned = a;
      }

      if (this.buildingID != -1) {
         ObjectBase b = this.object.getObjects().get(this.buildingID);
         this.resource.setResourceInBuilding(b, this);
      }

      if (this.capturedID != -1) {
         this.captured = this.mob.getMobHashMap().get(this.capturedID);
      }

      if (this.boundGhostID != -1) {
         this.boundGhost = this.mob.getMobHashMap().get(this.boundGhostID);
      }
   }

   public Map<String, Object> getSkirmishProvisionerRegionalProperties(int rID) {
      Map<String, Object> dataMap = this.getRegionalSaveData();
      dataMap.put("resourceID", rID);
      return dataMap;
   }

   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      if (this.resourceID != -1) {
         dataMap.put("resourceID", this.resourceID);
      }

      if (this.resourceX != 0.0F) {
         dataMap.put("resourceX", this.resourceX);
      }

      if (this.resourceY != 0.0F) {
         dataMap.put("resourceY", this.resourceY);
      }

      if (this.resourceXPickupOffset != 0.0F) {
         dataMap.put("resourceXPickupOffset", this.resourceXPickupOffset);
      }

      if (this.resourceYPickupOffset != 0.0F) {
         dataMap.put("resourceYPickupOffset", this.resourceYPickupOffset);
      }

      if (this.tileX != 0) {
         dataMap.put("tileX", this.tileX);
      }

      if (this.tileY != 0) {
         dataMap.put("tileY", this.tileY);
      }

      if (this.type != null) {
         dataMap.put("type", this.type.toString());
      }

      if (this.spriteNumber != 0) {
         dataMap.put("spriteNumber", this.spriteNumber);
      }

      if (this.color != ResourceModule.ResourceColorSet.DEFAULT) {
         dataMap.put("color", this.color.toString());
      }

      if (this.resourceDurability != 0) {
         dataMap.put("resourceDurability", this.resourceDurability);
      }

      if (this.uses != 1) {
         dataMap.put("uses", this.uses);
      }

      if (this.carrier != null) {
         dataMap.put("carrierID", this.carrier.getID());
      }

      if (this.assigned != null) {
         dataMap.put("assignedID", this.assigned.getID());
      }

      if (this.building != null) {
         dataMap.put("buildingID", this.building.getID());
      }

      if (this.captured != null) {
         dataMap.put("capturedID", this.captured.getID());
      }

      if (this.boundGhost != null) {
         dataMap.put("boundGhostID", this.boundGhost.getID());
      }

      if (this.onGround) {
         dataMap.put("onGround", this.onGround);
      }

      if (this.inBuilding) {
         dataMap.put("inBuilding", this.inBuilding);
      }

      if (this.carried) {
         dataMap.put("carried", this.carried);
      }

      if (this.inInventory) {
         dataMap.put("inInventory", this.inInventory);
      }

      if (this.grabbed) {
         dataMap.put("grabbed", this.grabbed);
      }

      if (this.dropping) {
         dataMap.put("dropping", this.dropping);
      }

      if (this.grabbedDropTick != 0) {
         dataMap.put("grabbedDropTick", this.grabbedDropTick);
      }

      return dataMap;
   }

   public int getCenterPixelX(boolean useMap) {
      return useMap ? (int)(this.resourceX + this.sprite.getWidth() / 2) : this.sprite.getWidth() / 2;
   }

   public int getCenterPixelY(boolean useMap) {
      return useMap ? (int)(this.resourceY + this.sprite.getHeight() / 2) : this.sprite.getHeight() / 2;
   }

   public int getTopPixelY(boolean useMap) {
      return useMap ? (int)this.resourceY : 0;
   }

   public int getBottomPixelY(boolean useMap) {
      return useMap ? (int)(this.resourceY + this.sprite.getHeight()) : this.sprite.getHeight();
   }

   public int getLeftPixelX(boolean useMap) {
      return useMap ? (int)this.resourceX : 0;
   }

   public int getRightPixelX(boolean useMap) {
      return useMap ? (int)(this.resourceX + this.sprite.getWidth()) : this.sprite.getWidth();
   }
}
