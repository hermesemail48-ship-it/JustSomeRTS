package rtr.weather;

import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.SoundModule;
import rtr.goal.GoalModule;
import rtr.lighting.LightingModule;
import rtr.map.MapModule;
import rtr.mobs.MobBase;
import rtr.mobs.MobModule;
import rtr.objects.ObjectBase;
import rtr.objects.ObjectModule;
import rtr.particles.ParticleModule;
import rtr.particles.projectiles.ProjectileModule;
import rtr.resources.ResourceModule;
import rtr.states.StateBase;
import rtr.utilities.Utilities;

public class Lightning {
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
   private int originX;
   private int originY;
   private int lightningX;
   private int lightningY;
   private int length;
   private int lengthCurrent;
   private boolean godTriggered;
   private boolean hitRod;
   private ParticleModule.ParticleSet particleSet;

   public Lightning(int x, int y, int l, boolean gT, boolean hR, ParticleModule.ParticleSet s) throws SlickException {
      this.lightningX = x;
      this.lightningY = y;
      this.originX = this.lightningX;
      this.originY = this.lightningY;
      this.length = l;
      this.godTriggered = gT;
      this.hitRod = hR;
      this.particleSet = s;
   }

   public void update() throws SlickException {
      int direction = Utilities.randomInt(2);
      int randomCycles = Utilities.randomInt(75) + 25;

      for (int x = 0; x < randomCycles; x++) {
         this.particle.newParticle(this.lightningX, this.lightningY, ParticleModule.ParticleType.FLASH_SPECK, this.particleSet, this.particleSet);
         if (Utilities.randomInt(5) == 0) {
            this.particle
               .newParticle(
                  this.lightningX + Utilities.randomInt(-1, 1),
                  this.lightningY + Utilities.randomInt(-1, 1),
                  ParticleModule.ParticleType.FLASH_SPECK,
                  ParticleModule.ParticleSet.WHITE,
                  this.particleSet
               );
         }

         if (Utilities.randomInt(10) == 0) {
            this.particle
               .newParticleGroup(
                  this.lightningX, this.lightningY, ParticleModule.ParticleGroup.SMOKE_BELLOW, ParticleModule.ParticleSet.WHITE, this.particleSet
               );
         }

         this.lightningY--;
         this.lengthCurrent++;
         if (Utilities.randomInt(2) == 0) {
            if (direction == 0) {
               this.lightningX++;
            } else {
               this.lightningX--;
            }
         }

         if (Utilities.randomInt(20) == 0) {
            direction = Utilities.randomInt(2);
         }
      }

      if (this.lengthCurrent >= this.length) {
         this.strike();
         this.weather.removeLightning(this);
      }
   }

   private void strike() throws SlickException {
      int tileX = 0;
      int tileY = 0;
      int strikeX = 0;
      int strikeY = 0;
      strikeX = this.originX;
      strikeY = this.originY;
      tileX = strikeX / 16;
      tileY = strikeY / 16;
      if (tileX > 0 && tileX < 256 && tileY > 0 && tileY < 256) {
         boolean hitBuilding = false;
         int mobHitOffset = 0;

         for (ObjectBase o : this.object.getObjectsNearby(tileX, tileY, 2)) {
            o.takeDamage(25 + Utilities.randomInt(25), MobBase.DamageType.ELECTRIC, MobBase.DamageCause.LIGHTNING);
            if (o.getObjectFlags().getEnergyMax() > 0) {
               o.increaseEnergy(10 + Utilities.randomInt(25));
            }

            if (o.getObjectFlags().getMobGroup() == MobBase.MobGroup.MONSTER) {
               mobHitOffset += 3;
            } else {
               mobHitOffset -= 3;
            }

            hitBuilding = true;
         }

         if (!hitBuilding) {
            for (MobBase m : this.mob.getMobsNearby(tileX, tileY, 2)) {
               m.takeDamage(
                  (int)(m.getHitPointsMax() * (0.05F + Utilities.randomFloat() * 0.5F)),
                  strikeX,
                  strikeY,
                  MobBase.DamageType.ELECTRIC,
                  MobBase.DamageCause.LIGHTNING
               );
               if (m.getMobFlags().getAllowFaith()) {
                  m.decreaseFaith(Utilities.randomInt(10) + 1, true);
               }

               if (this.godTriggered && m.getMobGroup() == MobBase.MobGroup.VILLAGER && m.getHitPoints() <= 0) {
                  this.goal.incrementGoal(GoalModule.GoalType.GRANDMASTER_SPARKLES);
               }

               if (m.getMobGroup() == MobBase.MobGroup.MONSTER) {
                  mobHitOffset += 2;
               } else {
                  mobHitOffset -= 2;
               }
            }
         }

         if (this.resource.isInitialized() && !Utilities.isOutOfBoundsOfUsableMap(tileX, tileY)) {
            this.resource.damageResource(tileX, tileY);
         }

         for (int x = 0; x < 3; x++) {
            this.projectile.newDestruction(strikeX, strikeY);
         }

         for (int x = 0; x < 5; x++) {
            this.projectile.newGroundStatic(strikeX, strikeY, this.particleSet);
         }

         this.lighting.addShortTermLight(tileX, tileY, (byte)24, (byte)10, 40);
         this.mob.tryConfuseInArea(tileX, tileY, 8, this.godTriggered, 1);
         this.mob.tryPanicInArea(tileX, tileY, 6, this.godTriggered, 1);
         if (this.hitRod) {
            if (Utilities.randomInt(2) == 0) {
               this.mob.influenceFaithInArea(tileX, tileY, 4, 1, true);
            }
         } else {
            mobHitOffset = Utilities.setAndKeepWithinBounds(-5, 5, mobHitOffset);
            if (mobHitOffset > 0) {
               this.mob.influenceFaithInArea(tileX, tileY, 6, 1, mobHitOffset, true);
            } else if (mobHitOffset < 0) {
               this.mob.influenceFaithInArea(tileX, tileY, 6, 1, mobHitOffset * -1, false);
            }
         }

         this.sound.playSound(SoundModule.SoundType.THUNDER);
      }
   }
}
