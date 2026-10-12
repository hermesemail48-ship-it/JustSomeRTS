package rtr.weather;

import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.SoundModule;
import rtr.goal.GoalModule;
import rtr.lighting.LightingModule;
import rtr.map.MapData;
import rtr.map.MapModule;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.MobModule;
import rtr.objects.ObjectBase;
import rtr.objects.ObjectModule;
import rtr.particles.ParticleModule;
import rtr.particles.projectiles.ProjectileModule;
import rtr.resources.ResourceModule;
import rtr.states.StateBase;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class Meteor {
   private MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   private SoundModule sound = (SoundModule)StateBase.getModule(ModuleBase.ModuleType.SOUND);
   private WeatherModule weather = (WeatherModule)StateBase.getModule(ModuleBase.ModuleType.WEATHER);
   private ResourceModule resource = (ResourceModule)StateBase.getModule(ModuleBase.ModuleType.RESOURCE);
   private ParticleModule particle = (ParticleModule)StateBase.getModule(ModuleBase.ModuleType.PARTICLE);
   private ProjectileModule projectile = (ProjectileModule)StateBase.getModule(ModuleBase.ModuleType.PROJECTILE);
   private MobModule mob = (MobModule)StateBase.getModule(ModuleBase.ModuleType.MOB);
   private ObjectModule object = (ObjectModule)StateBase.getModule(ModuleBase.ModuleType.OBJECT);
   private LightingModule lighting = (LightingModule)StateBase.getModule(ModuleBase.ModuleType.LIGHTING);
   private GoalModule goal = (GoalModule)StateBase.getModule(ModuleBase.ModuleType.GOAL);
   protected int targetX;
   protected int targetY;
   protected int tileX;
   protected int tileY;
   protected float blastX;
   protected float blastY;
   protected float velocity;
   protected float angle;
   private boolean godTriggered;
   protected int meteorStep;

   public Meteor(int x, int y, boolean gT) {
      this.blastX = x + Utilities.randomInt(-200, 200);
      this.blastY = y - 500 + Utilities.randomInt(50);
      this.targetX = x;
      this.targetY = y;
      this.tileX = x / 16;
      this.tileY = y / 16;
      this.godTriggered = gT;
      this.velocity = 5.0F + Utilities.randomFloat() * 2.0F;
      this.angle = Utilities.calculateProjectileAngle(this.blastX, this.blastY, this.targetX, this.targetY, this.velocity, 0.0F, 0.0F, 0.0F);
   }

   public void update() throws SlickException {
      switch (this.meteorStep) {
         case 0:
            this.sound.playSound(SoundModule.SoundType.METEOR_FIRE, this.targetX, this.targetY);
            this.meteorStep++;
            break;
         case 1:
            for (int x = 0; x < 16; x++) {
               this.particle
                  .newParticleGroup(
                     this.blastX + Utilities.randomInt(-2, 2),
                     this.blastY + Utilities.randomInt(-2, 2),
                     ParticleModule.ParticleGroup.FIRE,
                     ParticleModule.ParticleSet.FIRE_RED
                  );
               this.particle
                  .newParticleGroup(
                     this.blastX + Utilities.randomInt(-2, 2),
                     this.blastY + Utilities.randomInt(-2, 2),
                     ParticleModule.ParticleGroup.SMOKE_BELLOW,
                     ParticleModule.ParticleSet.BLACK,
                     ParticleModule.ParticleSet.GRAY
                  );
            }

            this.blastX = this.blastX + (float)Math.cos(this.angle) * this.velocity;
            this.blastY = this.blastY + (float)Math.sin(this.angle) * this.velocity;
            if (this.blastY > this.targetY) {
               this.meteorStep++;
            }

            if (Utilities.randomInt(2) == 0) {
               this.lighting.addShortTermLight((int)this.blastX / 16, (int)this.blastY / 16, (byte)4, (byte)12, 4);
            }
            break;
         case 2:
            this.blastY = this.targetY;

            for (int x = 0; x < 50; x++) {
               this.projectile.newDestruction(this.targetX, this.targetY);
            }

            for (int x = 0; x < 4; x++) {
               this.projectile.newExpandingFireRing(this.targetX, this.targetY, 64, 6.0F, ParticleModule.ParticleSet.FIRE_RED);
            }

            int mobHitOffset = 0;

            for (MobBase m : this.mob.getMobsNearby(this.tileX, this.tileY, 4)) {
               if (m.getTileX() == this.tileX && m.getTileY() == this.tileY) {
                  m.takeDamage(150 + Utilities.randomInt(50), this.targetX, this.targetY, MobBase.DamageType.FIRE, MobBase.DamageCause.METEOR);
               } else {
                  m.takeDamage(30 + Utilities.randomInt(15), this.targetX, this.targetY, MobBase.DamageType.FIRE, MobBase.DamageCause.METEOR);
               }

               if (this.godTriggered && m.getMobGroup() == MobBase.MobGroup.VILLAGER) {
                  this.goal.incrementGoal(GoalModule.GoalType.BAD_AIM);
               }

               if (m.getMobFlags().getAllowFaith()) {
                  m.decreaseFaith(Utilities.randomInt(10) + 1, true);
               }

               if (m.getMobGroup() == MobBase.MobGroup.MONSTER) {
                  mobHitOffset += 2;
               } else {
                  mobHitOffset -= 2;
               }
            }

            for (ObjectBase o : this.object.getObjectsNearby(this.tileX, this.tileY, 4)) {
               if (o.isBuildingAtLocation(this.tileX, this.tileY)) {
                  o.takeDamage(25 + Utilities.randomInt(25), MobBase.DamageType.FIRE, MobBase.DamageCause.METEOR);
               } else {
                  o.takeDamage(15 + Utilities.randomInt(15), MobBase.DamageType.FIRE, MobBase.DamageCause.METEOR);
               }

               if (o.getObjectFlags().getMobGroup() == MobBase.MobGroup.MONSTER) {
                  mobHitOffset += 2;
               } else {
                  mobHitOffset -= 2;
               }
            }

            this.sound.playSound(SoundModule.SoundType.METEOR_HIT, this.blastX, this.blastY);
            this.mob.tryConfuseInArea(this.tileX, this.tileY, 8, this.godTriggered, 1);
            this.mob.tryPanicInArea(this.tileX, this.tileY, 6, this.godTriggered, 1);
            mobHitOffset = Utilities.setAndKeepWithinBounds(-5, 5, mobHitOffset);
            if (mobHitOffset > 0) {
               this.mob.influenceFaithInArea(this.tileX, this.tileY, 6, 1, mobHitOffset, true);
            } else if (mobHitOffset < 0) {
               this.mob.influenceFaithInArea(this.tileX, this.tileY, 6, 1, mobHitOffset * -1, false);
            }

            for (int x = 0; x < 25; x++) {
               int randomX = this.tileX + Utilities.randomInt(-2, 2);
               int randomY = this.tileY + Utilities.randomInt(-2, 2);
               if (!Utilities.isOutOfBoundsOfUsableMap(randomX, randomY)) {
                  this.resource.damageResource(randomX, randomY);
               }
            }

            this.map.initSreenShake(50 + Utilities.randomInt(25));
            this.lighting.addShortTermLight(this.tileX, this.tileY, (byte)15, (byte)5, 20);
            this.lighting.addShortTermLight(this.tileX, this.tileY, (byte)5, (byte)8, 500);
            if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[this.tileX][this.tileY] == 0) {
               if (Utilities.randomInt(50) == 0) {
                  this.mob.newMob(this.tileX, this.tileY, MobBase.MobType.FIRE_ELEMENTAL, true);
               } else if (Utilities.randomInt(10) != 0) {
                  OrderedPair o = OrderedPair.getOrderedPair(this.tileX, this.tileY);
                  if (this.resource.getResourcesOnGroundByteMap()[this.tileX][this.tileY] != 0) {
                     o = this.resource.findOpenSpace(this.tileX, this.tileY, MapData.BlockMapGroup.STANDARD);
                  }

                  if (Utilities.randomInt(2) == 0) {
                     this.resource.createResourceOnGround(ResourceModule.ResourceType.IRON_ORE, ResourceModule.ResourceColorSet.DEFAULT, o.getX(), o.getY());
                  } else {
                     this.resource.createResourceOnGround(ResourceModule.ResourceType.ROCK, ResourceModule.ResourceColorSet.BLACK, o.getX(), o.getY());
                  }
               } else if (Utilities.randomInt(2) == 0 && this.map.canDigAtCoord(this.tileX, this.tileY)) {
                  for (int xx = -1; xx <= 1; xx++) {
                     for (int y = -1; y <= 1; y++) {
                        if (Utilities.randomInt(10) != 0 && this.map.canDigAtCoord(this.tileX + xx, this.tileY + y) || xx == 0 && y == 0) {
                           this.map.regrowResourceTerrain(this.tileX + xx, this.tileY + y, MapTilesLoader.TileSet.WATER_DRY);
                        }
                     }
                  }
               } else {
                  this.map.regrowResourceTerrain(this.tileX, this.tileY, MapTilesLoader.TileSet.ROCK_BLACK);
               }
            }

            this.weather.addDisasterTemp(0.1 + Utilities.randomFloat() / 4.0F);
            this.weather.removeMeteor(this);
      }
   }
}
