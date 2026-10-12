package rtr.weather;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.SoundModule;
import rtr.goal.GoalModule;
import rtr.gui.states.BrushLoader;
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
import rtr.road.RoadModule;
import rtr.states.StateBase;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class Comet {
   private MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   private SoundModule sound = (SoundModule)StateBase.getModule(ModuleBase.ModuleType.SOUND);
   private WeatherModule weather = (WeatherModule)StateBase.getModule(ModuleBase.ModuleType.WEATHER);
   private RoadModule road = (RoadModule)StateBase.getModule(ModuleBase.ModuleType.ROAD);
   private ParticleModule particle = (ParticleModule)StateBase.getModule(ModuleBase.ModuleType.PARTICLE);
   private ProjectileModule projectile = (ProjectileModule)StateBase.getModule(ModuleBase.ModuleType.PROJECTILE);
   private MobModule mob = (MobModule)StateBase.getModule(ModuleBase.ModuleType.MOB);
   private ObjectModule object = (ObjectModule)StateBase.getModule(ModuleBase.ModuleType.OBJECT);
   private LightingModule lighting = (LightingModule)StateBase.getModule(ModuleBase.ModuleType.LIGHTING);
   private GoalModule goal = (GoalModule)StateBase.getModule(ModuleBase.ModuleType.GOAL);
   protected ArrayList<OrderedPair> tilesInRangeInner = new ArrayList<>();
   protected ArrayList<OrderedPair> tilesInRangeOuter = new ArrayList<>();
   protected int targetX;
   protected int targetY;
   protected int tileX;
   protected int tileY;
   protected float blastX;
   protected float blastY;
   protected float velocity;
   protected float angle;
   private boolean godTriggered;
   protected int cometStep;
   protected int soundTick;

   public Comet(int x, int y, boolean gT) {
      this.blastX = x + Utilities.randomInt(-200, 200);
      this.blastY = y - 1500;
      this.targetX = x;
      this.targetY = y;
      this.tileX = x / 16;
      this.tileY = y / 16;
      this.godTriggered = gT;
      this.velocity = 2.0F + Utilities.randomFloat() * 2.0F;
      this.angle = Utilities.calculateProjectileAngle(this.blastX, this.blastY, this.targetX, this.targetY, this.velocity, 0.0F, 0.0F, 0.0F);
   }

   public void update() throws SlickException {
      switch (this.cometStep) {
         case 0:
            for (int x = 0; x < 32; x++) {
               float angle2 = (float)Math.toRadians(Utilities.randomInt(3600) / 10.0);
               float projectileX = (float)(Math.cos(angle2) * Utilities.randomInt(12) + 1.0 + this.blastX);
               float projectileY = (float)(Math.sin(angle2) * Utilities.randomInt(12) + 1.0 + this.blastY);
               this.particle.newParticleGroup(projectileX, projectileY, ParticleModule.ParticleGroup.MAGIC_FIRE, ParticleModule.ParticleSet.FIRE_BLUE);
            }

            for (int x = 0; x < 24; x++) {
               float angle2 = (float)Math.toRadians(Utilities.randomInt(3600) / 10.0);
               float projectileX = (float)(Math.cos(angle2) * Utilities.randomInt(8) + 1.0 + this.blastX);
               float projectileY = (float)(Math.sin(angle2) * Utilities.randomInt(8) + 1.0 + this.blastY);
               this.particle.newParticleGroup(projectileX, projectileY, ParticleModule.ParticleGroup.MAGIC_FIRE, ParticleModule.ParticleSet.FIRE_BLUE);
            }

            for (int x = 0; x < 16; x++) {
               float angle2 = (float)Math.toRadians(Utilities.randomInt(3600) / 10.0);
               float projectileX = (float)(Math.cos(angle2) * Utilities.randomInt(3) + 1.0 + this.blastX);
               float projectileY = (float)(Math.sin(angle2) * Utilities.randomInt(3) + 1.0 + this.blastY);
               this.particle.newParticleGroup(projectileX, projectileY, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_RED);
            }

            for (int x = 0; x < 16; x++) {
               float angle2 = (float)Math.toRadians(Utilities.randomInt(3600) / 10.0);
               float projectileX = (float)(Math.cos(angle2) * Utilities.randomInt(3) + 1.0 + this.blastX);
               float projectileY = (float)(Math.sin(angle2) * Utilities.randomInt(3) + 1.0 + this.blastY);
               this.particle
                  .newParticleGroup(projectileX, projectileY, ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS, ParticleModule.ParticleSet.FIRE_BLUE);
            }

            for (int x = 0; x < 4; x++) {
               float angle2 = (float)Math.toRadians(Utilities.randomInt(3600) / 10.0);
               float projectileX = (float)(Math.cos(angle2) * Utilities.randomInt(3) + 1.0 + this.blastX);
               float projectileY = (float)(Math.sin(angle2) * Utilities.randomInt(3) + 1.0 + this.blastY);
               this.particle
                  .newParticleGroup(
                     projectileX,
                     projectileY,
                     ParticleModule.ParticleGroup.SMOKE_BELLOW,
                     ParticleModule.ParticleSet.MAGIC_BLUE,
                     ParticleModule.ParticleSet.FIRE_BLUE
                  );
            }

            this.particle.newParticleGroup(this.targetX, this.targetY, ParticleModule.ParticleGroup.MAGIC_FIRE, ParticleModule.ParticleSet.FIRE_BLUE);
            if ((int)(this.blastY % 10.0F) == 0) {
               this.projectile.newExpandingEnergyRing(this.targetX, this.targetY, 20, this.velocity, ParticleModule.ParticleSet.FIRE_YELLOW);
            }

            this.blastX = this.blastX + (float)Math.cos(this.angle) * this.velocity;
            this.blastY = this.blastY + (float)Math.sin(this.angle) * this.velocity;
            if (this.blastY > this.targetY) {
               this.cometStep++;
            }

            this.soundTick++;
            if (this.soundTick % 30 == 0) {
               this.sound.playSound(SoundModule.SoundType.METEOR_FIRE, this.blastX, this.blastY);
               this.sound.playSound(SoundModule.SoundType.HEAVY_WIND, this.blastX, this.blastY);
               this.sound.playSound(SoundModule.SoundType.MAGIC_HEAL, this.blastX, this.blastY);
            }

            if (this.soundTick % 10 == 0) {
               this.sound.playSound(SoundModule.SoundType.MAGIC_FIRE, this.blastX, this.blastY);
            }

            if (Utilities.randomInt(4) == 0) {
               this.lighting.addShortTermLight((int)this.blastX / 16, (int)this.blastY / 16, (byte)12, (byte)12, 8);
            }
            break;
         case 1:
            this.blastY = this.targetY;

            for (int x = 0; x < 4; x++) {
               this.projectile.newExpandingFireRing(this.targetX, this.targetY, 512, 8.0F, ParticleModule.ParticleSet.MAGIC_BLUE);
            }

            for (int x = 0; x < 4; x++) {
               this.projectile.newExpandingFireRing(this.targetX, this.targetY, 384, 6.0F, ParticleModule.ParticleSet.FIRE_BLUE);
            }

            for (int x = 0; x < 4; x++) {
               this.projectile.newExpandingFireRing(this.targetX, this.targetY, 512, 5.0F, ParticleModule.ParticleSet.FIRE_BLUE);
            }

            int mobHitOffset = 0;
            ArrayList<MobBase> mobsInRangeNear = this.mob.getMobsNearby(this.tileX, this.tileY, 6);

            for (MobBase m : mobsInRangeNear) {
               m.takeDamage(500 + Utilities.randomInt(250), this.targetX, this.targetY, MobBase.DamageType.MAGIC_ICE, MobBase.DamageCause.COMET);
               if (m.getMobFlags().getAllowFaith()) {
                  m.decreaseFaith(Utilities.randomInt(10) + 1, true);
               }

               if (this.godTriggered && m.getMobGroup() == MobBase.MobGroup.VILLAGER) {
                  this.goal.incrementGoal(GoalModule.GoalType.BAD_AIM);
               }

               if (m.getMobGroup() == MobBase.MobGroup.MONSTER) {
                  mobHitOffset += 3;
               } else {
                  mobHitOffset -= 3;
               }
            }

            ArrayList<MobBase> mobsInRangeFar = this.mob.getMobsNearby(this.tileX, this.tileY, 12);
            mobsInRangeFar.removeAll(mobsInRangeNear);

            for (MobBase m : mobsInRangeFar) {
               m.takeDamage(150 + Utilities.randomInt(150), this.targetX, this.targetY, MobBase.DamageType.MAGIC_ICE, MobBase.DamageCause.COMET);
               if (m.getMobFlags().getAllowFaith()) {
                  m.decreaseFaith(Utilities.randomInt(10) + 1, true);
               }

               if (this.godTriggered && m.getMobGroup() == MobBase.MobGroup.VILLAGER) {
                  this.goal.incrementGoal(GoalModule.GoalType.BAD_AIM);
               }

               if (m.getMobGroup() == MobBase.MobGroup.MONSTER) {
                  mobHitOffset += 2;
               } else {
                  mobHitOffset -= 2;
               }
            }

            ArrayList<ObjectBase> objectsInRangeNear = this.object.getObjectsNearby(this.tileX, this.tileY, 6);

            for (ObjectBase o : objectsInRangeNear) {
               o.takeDamage(
                  (int)(o.getHitPointsMax() * 0.8 + Utilities.randomInt((int)(o.getHitPointsMax() * 0.4) + 1)),
                  MobBase.DamageType.MAGIC_ICE,
                  MobBase.DamageCause.COMET
               );
               if (this.godTriggered && o.getObjectFlags().getBaseType() == MapTilesLoader.TileSet.CASTLE_1) {
                  this.goal.incrementGoal(GoalModule.GoalType.MIDGAR);
               }

               if (o.getObjectFlags().getMobGroup() == MobBase.MobGroup.MONSTER) {
                  mobHitOffset += 2;
               } else {
                  mobHitOffset -= 2;
               }
            }

            ArrayList<ObjectBase> objectsInRangeFar = this.object.getObjectsNearby(this.tileX, this.tileY, 12);
            objectsInRangeFar.removeAll(objectsInRangeNear);

            for (ObjectBase o : objectsInRangeFar) {
               o.takeDamage(
                  (int)(o.getHitPointsMax() * 0.25 + Utilities.randomInt((int)(o.getHitPointsMax() * 0.25) + 1)),
                  MobBase.DamageType.MAGIC_ICE,
                  MobBase.DamageCause.COMET
               );
               if (this.godTriggered && o.getObjectFlags().getBaseType() == MapTilesLoader.TileSet.CASTLE_1) {
                  this.goal.incrementGoal(GoalModule.GoalType.MIDGAR);
               }

               if (o.getObjectFlags().getMobGroup() == MobBase.MobGroup.MONSTER) {
                  mobHitOffset += 2;
               } else {
                  mobHitOffset -= 2;
               }
            }

            this.sound.playSound(SoundModule.SoundType.METEOR_HIT, this.blastX, this.blastY);
            this.sound.playSound(SoundModule.SoundType.EARTHQUAKE, this.blastX, this.blastY);
            this.sound.playSound(SoundModule.SoundType.EARTHQUAKE_ROCKS, this.blastX, this.blastY);
            this.sound.playSound(SoundModule.SoundType.EARTHQUAKE_BOOM, this.blastX, this.blastY);
            this.sound.playSound(SoundModule.SoundType.EARTHQUAKE_BOOM, this.blastX, this.blastY);
            this.sound.playSound(SoundModule.SoundType.EARTHQUAKE_BOOM, this.blastX, this.blastY);
            this.sound.playSound(SoundModule.SoundType.HEAVY_WIND, this.blastX, this.blastY);
            this.sound.playSound(SoundModule.SoundType.HEAVY_WIND, this.blastX, this.blastY);
            this.sound.playSound(SoundModule.SoundType.MAGIC_FIRE, this.blastX, this.blastY);
            this.mob.tryConfuseInArea(this.tileX, this.tileY, 24, this.godTriggered, 1);
            this.mob.tryPanicInArea(this.tileX, this.tileY, 24, this.godTriggered, 1);
            mobHitOffset = Utilities.setAndKeepWithinBounds(-5, 5, mobHitOffset);
            if (mobHitOffset > 0) {
               this.mob.influenceFaithInArea(this.tileX, this.tileY, 24, 1, mobHitOffset, true);
            } else if (mobHitOffset < 0) {
               this.mob.influenceFaithInArea(this.tileX, this.tileY, 24, 1, mobHitOffset * -1, false);
            }

            this.map.initSreenShake(150 + Utilities.randomInt(25));
            this.lighting.addShortTermLight(this.tileX, this.tileY, (byte)15, (byte)5, 20);
            this.lighting.addShortTermLight(this.tileX, this.tileY, (byte)5, (byte)8, 500);
            this.cometStep++;
            break;
         case 2:
            this.map.drawToMap(this.tileX, this.tileY, MapTilesLoader.TileSet.WATER_PARTIALLY_DRY, 8, BrushLoader.BrushType.ROUND, true);

            for (OrderedPair oxxx : Utilities.getAllCoordinatesWithin(this.tileX, this.tileY, 6)) {
               this.road.decreaseRoadDurability(oxxx.getX(), oxxx.getY(), 2000);
            }

            for (OrderedPair oxxx : Utilities.getAllCoordinatesWithin(this.tileX, this.tileY, 24)) {
               this.road.decreaseRoadDurability(oxxx.getX(), oxxx.getY(), Utilities.randomInt(2000));
            }

            this.lighting.addShortTermLight(this.tileX, this.tileY, (byte)12, (byte)12, 200);
            this.cometStep++;
            break;
         case 3:
            for (OrderedPair oxx : Utilities.getAllCoordinatesWithin(this.tileX, this.tileY, 6)) {
               if (Utilities.randomInt(8) != 0 && this.map.canDigAtCoord(oxx.getX(), oxx.getY())) {
                  this.map.regrowResourceTerrain(oxx.getX(), oxx.getY(), MapTilesLoader.TileSet.WATER_PARTIALLY_DRY);
               }
            }

            this.cometStep++;
            break;
         case 4:
            this.tilesInRangeInner = Utilities.getAllCoordinatesWithin(this.tileX, this.tileY, 10);

            for (OrderedPair ox : Utilities.getAllCoordinatesWithin(this.tileX, this.tileY, 10)) {
               if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[ox.getX()][ox.getY()] > 0 || !this.map.canDigAtCoord(ox.getX(), ox.getY())
                  )
                {
                  this.tilesInRangeInner.remove(ox);
               }
            }

            this.cometStep++;
            break;
         case 5:
            this.tilesInRangeOuter = Utilities.getAllCoordinatesWithin(this.tileX, this.tileY, 18);

            for (OrderedPair o : Utilities.getAllCoordinatesWithin(this.tileX, this.tileY, 18)) {
               if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[o.getX()][o.getY()] > 0 || !this.map.canDigAtCoord(o.getX(), o.getY())) {
                  this.tilesInRangeOuter.remove(o);
               }
            }

            this.weather.removeDisasterTemp(20 + Utilities.randomInt(10));
            this.cometStep++;
            break;
         case 6:
            for (int x = 0; x < 2; x++) {
               if (this.tilesInRangeInner.size() > 0) {
                  OrderedPair o = this.tilesInRangeInner.get(Utilities.randomInt(this.tilesInRangeInner.size()));
                  if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[o.getX()][o.getY()] == 0 && this.map.canDigAtCoord(o.getX(), o.getY())) {
                     this.lighting.addShortTermLight(o.getX(), o.getY(), (byte)5, (byte)4, 128);
                     this.map
                        .drawToMap(
                           o.getX(),
                           o.getY(),
                           Utilities.randomInt(4) == 0 ? MapTilesLoader.TileSet.CRYSTAL_PURPLE_COLLECTED : MapTilesLoader.TileSet.CRYSTAL_PURPLE,
                           1,
                           BrushLoader.BrushType.ROUND
                        );
                     this.mob.tryConfuseInArea(o.getX(), o.getY(), 4, true, 2);
                     this.mob.tryPanicInArea(o.getX(), o.getY(), 2, true, 2);
                     this.mob.influenceFaithInArea(o.getX(), o.getY(), 4, 1, false);
                     this.projectile.newGroundStatic(o.getX() * 16, o.getY() * 16, ParticleModule.ParticleSet.FIRE_PURPLE);
                  }

                  this.tilesInRangeInner.remove(o);
               }

               if (this.tilesInRangeOuter.size() > 0) {
                  OrderedPair o = this.tilesInRangeOuter.get(Utilities.randomInt(this.tilesInRangeOuter.size()));
                  if (Utilities.randomInt(4) == 0
                     && this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[o.getX()][o.getY()] == 0
                     && this.map.canDigAtCoord(o.getX(), o.getY())) {
                     this.lighting.addShortTermLight(o.getX(), o.getY(), (byte)5, (byte)4, 128);
                     this.map
                        .drawToMap(
                           o.getX(),
                           o.getY(),
                           Utilities.randomInt(2) == 0 ? MapTilesLoader.TileSet.CRYSTAL_PURPLE_COLLECTED : MapTilesLoader.TileSet.CRYSTAL_PURPLE,
                           1,
                           BrushLoader.BrushType.ROUND
                        );
                     this.mob.tryConfuseInArea(o.getX(), o.getY(), 4, true, 2);
                     this.mob.tryPanicInArea(o.getX(), o.getY(), 2, true, 2);
                     this.mob.influenceFaithInArea(o.getX(), o.getY(), 4, 1, false);
                     this.projectile.newGroundStatic(o.getX() * 16, o.getY() * 16, ParticleModule.ParticleSet.MAGIC_PURPLE);
                  }

                  this.tilesInRangeOuter.remove(o);
               }

               if (this.tilesInRangeInner.size() == 0 && this.tilesInRangeOuter.size() == 0) {
                  this.weather.removeComet(this);
                  break;
               }
            }
      }
   }
}
