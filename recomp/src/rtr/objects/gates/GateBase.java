package rtr.objects.gates;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.SoundModule;
import rtr.map.CollisionModule;
import rtr.map.MapModule;
import rtr.map.ai.MapAIModule;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.mobs.MobModule;
import rtr.objects.ObjectBase;
import rtr.objects.ObjectModule;
import rtr.objects.gates.spritesheet.GateSprite;
import rtr.particles.ParticleModule;
import rtr.particles.projectiles.ProjectileModule;
import rtr.resources.ResourceModule;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.utilities.OrderedPair;

public class GateBase {
   protected MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   protected MapAIModule mapAI = (MapAIModule)StateBase.getModule(ModuleBase.ModuleType.MAP_AI);
   protected MissileModule missile = (MissileModule)StateBase.getModule(ModuleBase.ModuleType.MISSILE);
   protected MobModule mob = (MobModule)StateBase.getModule(ModuleBase.ModuleType.MOB);
   protected ObjectModule object = (ObjectModule)StateBase.getModule(ModuleBase.ModuleType.OBJECT);
   protected ProjectileModule projectile = (ProjectileModule)StateBase.getModule(ModuleBase.ModuleType.PROJECTILE);
   protected ParticleModule particle = (ParticleModule)StateBase.getModule(ModuleBase.ModuleType.PARTICLE);
   protected CollisionModule collision = (CollisionModule)StateBase.getModule(ModuleBase.ModuleType.COLLISION);
   protected ResourceModule resource = (ResourceModule)StateBase.getModule(ModuleBase.ModuleType.RESOURCE);
   protected SoundModule sound = (SoundModule)StateBase.getModule(ModuleBase.ModuleType.SOUND);
   protected GateBase.GateType gateType;
   protected int gateX;
   protected int gateY;
   protected int gateCenterX;
   protected int gateCenterY;
   protected boolean open;
   protected boolean opening;
   protected boolean closed;
   protected boolean closing;
   protected MobBase.MobGroup mobGroup;
   protected ObjectBase gate;
   protected GateSprite sprite;

   public GateBase(int x, int y, ObjectBase g) throws SlickException {
      this.gateX = x;
      this.gateY = y;
      this.gateCenterX = x * 16 + 8;
      this.gateCenterY = y * 16 + 8;
      this.closed = true;
      this.gate = g;
   }

   public GateBase(YMLDataMap dataMap, ObjectBase g) throws SlickException {
      String gateTypeString = dataMap.getOrDefault("gateType", "null");

      GateBase.GateType[] var7;
      for (GateBase.GateType type : GateBase.GateType.values()) {
         if (type.toString().equals(gateTypeString)) {
            this.gateType = type;
            break;
         }
      }

      this.gateX = Integer.parseInt(dataMap.getOrDefault("gateX", 0));
      this.gateY = Integer.parseInt(dataMap.getOrDefault("gateY", 0));
      this.gateCenterX = Integer.parseInt(dataMap.getOrDefault("gateCenterX", 0));
      this.gateCenterY = Integer.parseInt(dataMap.getOrDefault("gateCenterY", 0));
      String mobGroupString = dataMap.getOrDefault("mobGroup", "null");

      MobBase.MobGroup[] var8;
      for (MobBase.MobGroup typex : MobBase.MobGroup.values()) {
         if (typex.toString().equals(mobGroupString)) {
            this.mobGroup = typex;
            break;
         }
      }

      this.open = Boolean.parseBoolean(dataMap.getOrDefault("open", false));
      this.opening = Boolean.parseBoolean(dataMap.getOrDefault("opening", false));
      this.closed = Boolean.parseBoolean(dataMap.getOrDefault("closed", false));
      this.closing = Boolean.parseBoolean(dataMap.getOrDefault("closing", false));
      this.gate = g;
      this.sprite = this.object.createGateSpriteSheet(this.gateType);
      if (this.opening || this.open) {
         this.setPositionOpen();
      }

      if (this.closing || this.closed) {
         this.setPositionClose();
      }
   }

   public void render() throws SlickException {
      if (this.sprite != null) {
         float mapX = this.map.getMapX();
         float mapY = this.map.getMapY();
         if (this.map.isInView(this.gateX * 16, this.gateY * 16, 10)) {
            this.sprite.render(this.gateX * 16 + mapX, this.gateY * 16 + mapY, true);
         }
      }
   }

   public void renderMapWriter() throws SlickException {
      if (this.sprite != null) {
         this.sprite.render(this.gateX * 16, this.gateY * 16, false);
      }
   }

   public void update() throws SlickException {
      if (this.sprite != null) {
         this.sprite.update();
      }

      boolean foundFriendly = this.findFriendly();
      if (this.opening && this.sprite.isStopped()) {
         this.opening = false;
         this.open = true;
         this.closed = false;
         this.closing = false;
      }

      if (this.closing && this.sprite.isStopped()) {
         this.opening = false;
         this.open = false;
         this.closed = true;
         this.closing = false;
      }

      if (foundFriendly && !this.opening && !this.open) {
         this.setPositionOpen();
         this.sound.playSound(SoundModule.SoundType.OPEN_GATE, this.gateX * 16, this.gateY * 16);
         this.opening = true;
         this.open = false;
         this.closed = false;
         this.closing = false;
      }

      if (!foundFriendly && !this.closing && !this.closed) {
         this.setPositionClose();
         this.sound.playSound(SoundModule.SoundType.CLOSE_GATE, this.gateX * 16, this.gateY * 16);
         this.opening = false;
         this.open = false;
         this.closed = false;
         this.closing = true;
      }
   }

   protected boolean findFriendly() throws SlickException {
      ArrayList<MobBase> friendlyNearby = this.mob.getMobsNearby(this.gateX, this.gateY, 2, this.mobGroup);
      if (this.mobGroup == MobBase.MobGroup.VILLAGER) {
         friendlyNearby.addAll(this.mob.getMobsNearby(this.gateX, this.gateY, 2, MobBase.MobGroup.GOLEM));
         friendlyNearby.addAll(this.mob.getMobsNearby(this.gateX, this.gateY, 2, MobBase.MobGroup.NOMAD));
         friendlyNearby.addAll(this.mob.getMobsNearby(this.gateX, this.gateY, 2, MobBase.MobGroup.MIGRANT));
         friendlyNearby.addAll(this.mob.getMobsNearby(this.gateX, this.gateY, 2, MobBase.MobGroup.ANIMAL));
         friendlyNearby.addAll(this.mob.getMobsNearby(this.gateX, this.gateY, 2, MobBase.MobGroup.DOMESTICATED_ANIMAL));
         friendlyNearby.addAll(this.mob.getMobsNearby(this.gateX, this.gateY, 2, MobBase.MobGroup.PROVISIONER));
      }

      if (friendlyNearby.size() <= 0) {
         return false;
      } else {
         for (MobBase m : friendlyNearby) {
            if (m.getTileX() == this.gateX && m.getTileY() == this.gateY) {
               return true;
            }

            ArrayList<OrderedPair> path = m.getActiveNode().getPathCoordinatesList();
            if (path != null) {
               for (int x = 0; x < path.size(); x++) {
                  OrderedPair o = path.get(x);
                  if (o.getX() == this.gateX && o.getY() == this.gateY) {
                     return true;
                  }
               }
            }
         }

         return false;
      }
   }

   public boolean isOpen() {
      return this.open;
   }

   public boolean isClosed() {
      return this.closed || this.closing;
   }

   protected void setAnimation(GateSprite.GateAnimationFrames a) {
      this.sprite.setPosition(a);
   }

   public void setPositionOpen() {
      if (this.sprite != null) {
         this.setAnimation(GateSprite.GateAnimationFrames.OPEN);
      }
   }

   public void setPositionClose() {
      if (this.sprite != null) {
         this.setAnimation(GateSprite.GateAnimationFrames.CLOSE);
      }
   }

   public int getX() {
      return this.gateX;
   }

   public int getY() {
      return this.gateY;
   }

   public int getCenterPixelX() {
      return this.gateCenterX;
   }

   public int getCenterPixelY() {
      return this.gateCenterY;
   }

   public GateBase.GateType getGateType() {
      return this.gateType;
   }

   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("version", Game.getVersion());
      dataMap.put("gateType", this.gateType != null ? this.gateType.toString() : "null");
      dataMap.put("gateX", this.gateX);
      dataMap.put("gateY", this.gateY);
      dataMap.put("gateCenterX", this.gateCenterX);
      dataMap.put("gateCenterY", this.gateCenterY);
      dataMap.put("mobGroup", this.mobGroup != null ? this.mobGroup.toString() : "null");
      dataMap.put("open", this.open);
      dataMap.put("opening", this.opening);
      dataMap.put("closed", this.closed);
      dataMap.put("closing", this.closing);
      return dataMap;
   }

   public static enum GateType {
      WOOD_GATE_NS,
      WOOD_GATE_WE,
      STONE_GATE_NS,
      STONE_GATE_WE;
   }
}
