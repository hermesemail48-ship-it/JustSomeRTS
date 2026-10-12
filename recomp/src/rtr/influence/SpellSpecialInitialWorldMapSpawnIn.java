package rtr.influence;

import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.map.MapData;
import rtr.mobs.MobBase;
import rtr.mobs.MobFlagsBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.particles.ParticleModule;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class SpellSpecialInitialWorldMapSpawnIn extends SpellBase {
   private final int MAX_VILLAGERS = 20;
   private final int MAX_RESOURCES = 64;
   private final int CATJEET_MODE_CHANCE = 50;
   private final int DOGGO_BOMB_MODE_CHANCE = 200;
   private int villagerCount;
   private int resourceCount;
   private boolean catjeetMode;
   private boolean doggoBombMode;

   @Override
   public void config(int x, int y, boolean freeCast) {
      this.type = SpellBase.SpellType.SPECIAL_INITIAL_WORLD_MAP_SPAWN_IN;
      this.name = Text.getText("spell.initialSpawnIn.name");
      this.description = Text.getText("spell.initialSpawnIn.description");
      this.castCost = freeCast ? 0 : 0;
      this.targetX = x;
      this.targetY = y;
      this.tileX = x / 16;
      this.tileY = y / 16;
      this.radius = 12;
   }

   @Override
   public void update() throws SlickException {
      this.durationTick++;
      this.particle
         .newParticleGroup(
            this.targetX + Utilities.randomInt(this.radius * 16 * -1, this.radius * 16),
            this.targetY + Utilities.randomInt(this.radius * 16 * -1, this.radius * 16),
            ParticleModule.ParticleGroup.FIRE,
            ParticleModule.ParticleSet.FIRE_GREEN
         );
      this.particle
         .newParticleGroup(
            this.targetX + Utilities.randomInt(this.radius * 16 * -1, this.radius * 16),
            this.targetY + Utilities.randomInt(this.radius * 16 * -1, this.radius * 16),
            ParticleModule.ParticleGroup.FLOATING_MAGIC_SQUARES,
            ParticleModule.ParticleSet.MAGIC_GREEN
         );
      switch (this.spellStep) {
         case 0:
            this.catjeetMode = Utilities.randomInt(50) == 0;
            this.doggoBombMode = Utilities.randomInt(200) == 0;
            this.lighting.addShortTermLight(this.tileX, this.tileY, (byte)this.radius, (byte)4, this.duration);
            this.spellStep++;
            break;
         case 1:
            if (this.durationTick > 50) {
               if (this.durationTick % 25 == 0 && this.villagerCount < 20) {
                  this.dropCreature(this.catjeetMode ? MobBase.MobType.CATJEET_VILLAGER : MobBase.MobType.HUMAN_VILLAGER, false);
                  if (this.doggoBombMode) {
                     this.dropCreature(MobBase.MobType.DOGGO, true);
                  }
               }

               if (this.durationTick % 5 == 0 && this.resourceCount < 64) {
                  this.dropResource();
               }

               this.influence.decreaseInfluenceAmount(this.influence.getInfluenceAmountMax());
               if (this.villagerCount >= 20 && this.resourceCount >= 32) {
                  this.influence.increaseInfluenceAmount(this.influence.getInfluenceAmountMax());
                  this.influence.removeSpell(this);
               }
            }
      }
   }

   private void dropCreature(MobBase.MobType type, boolean skipCount) throws SlickException {
      if (!skipCount) {
         this.villagerCount++;
      }

      int spawnX = this.tileX + Utilities.randomInt(this.radius * -1, this.radius);

      int spawnY;
      for (spawnY = this.tileY + Utilities.randomInt(this.radius * -1, this.radius);
         this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[spawnX][spawnY] != 0;
         spawnY = this.tileY + Utilities.randomInt(this.radius * -1, this.radius)
      ) {
         spawnX = this.tileX + Utilities.randomInt(this.radius * -1, this.radius);
      }

      MobBase selectedTarget = this.mob.newMob(spawnX, spawnY, type, true);
      if (Utilities.randomInt(3) == 0) {
         selectedTarget.interruptNode(selectedTarget.getAIConfused());
      }

      MobFlagsBase mobFlags = selectedTarget.getMobFlags();
      if (mobFlags.getAllowFaith()) {
         selectedTarget.setFaith((int)(selectedTarget.getFaithMax() * 0.75));
      }

      if (mobFlags.getAllowHunger()) {
         selectedTarget.setHunger(selectedTarget.getHungerMax());
      }

      if (mobFlags.getAllowThirst()) {
         selectedTarget.setThirst(selectedTarget.getThirstMax());
      }

      ResourceBase r = this.resource.createResourceUnassigned(ResourceModule.ResourceType.WATER_BOTTLE);
      selectedTarget.equipResource(r, r.getType().getTemplate().getResourceSlot());
      r = this.resource.createResourceUnassigned(ResourceModule.ResourceType.RATION);
      selectedTarget.equipResource(r, r.getType().getTemplate().getResourceSlot());
      this.beamParticles(selectedTarget.getCenterPixelX(true), selectedTarget.getBottomPixelY(true) - 3);
      this.mobJob.increaseDesiredWorkers(MobJobBase.MobJobType.BUILDER, 1);
   }

   private void dropResource() throws SlickException {
      this.resourceCount++;
      int spawnX = this.tileX + Utilities.randomInt(this.radius * -1, this.radius);

      int spawnY;
      for (spawnY = this.tileY + Utilities.randomInt(this.radius * -1, this.radius);
         this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[spawnX][spawnY] != 0 || this.resource.getResourceOnGround(spawnX, spawnY) != null;
         spawnY = this.tileY + Utilities.randomInt(this.radius * -1, this.radius)
      ) {
         spawnX = this.tileX + Utilities.randomInt(this.radius * -1, this.radius);
      }

      ResourceModule.ResourceType type = null;
      switch (Utilities.randomInt(6)) {
         case 0:
         case 1:
            type = ResourceModule.ResourceType.WOOD;
            break;
         case 2:
         case 3:
            type = ResourceModule.ResourceType.ROCK;
            break;
         case 4:
            type = ResourceModule.ResourceType.WATER_BOTTLE;
            break;
         case 5:
            type = ResourceModule.ResourceType.COOKED_MEAT;
      }

      this.resource.createResourceOnGround(type, ResourceModule.ResourceColorSet.DEFAULT, spawnX, spawnY);
      OrderedPair o = OrderedPair.getOrderedPair(spawnX, spawnY);
      this.beamParticles(o.getX() * 16 + 8, o.getY() * 16 + 16 - 3);
   }

   private void beamParticles(int particleX, int particleY) throws SlickException {
      this.sound.playSound(SoundModule.SoundType.MAGIC_RECALL, particleX, particleY);
      this.projectile.newEnergyBeam(particleX, particleY, particleX + Utilities.randomInt(-32, 32), particleY - 500);
      this.lighting.addShortTermLight(particleX / 16, particleY / 16, (byte)5, (byte)12, 50);

      for (int x = 0; x < 10; x++) {
         this.projectile.newEnergyRing(particleX, particleY, 12, ParticleModule.ParticleSet.FIRE_BLUE);
         this.projectile.newEnergyRing(particleX, particleY, 12, ParticleModule.ParticleSet.FIRE_GREEN);
      }

      this.projectile.newExpandingEnergyRing(particleX, particleY, 64, 4.0F, ParticleModule.ParticleSet.FIRE_BLUE);

      for (int x = 0; x < 50; x++) {
         this.particle
            .newParticleGroup(
               particleX + Utilities.randomInt(-6, 6),
               particleY + Utilities.randomInt(-6, 6),
               Utilities.randomInt(360),
               ParticleModule.ParticleGroup.DUST,
               ParticleModule.ParticleSet.MAGIC_BLUE
            );
      }

      for (int x = 0; x < 20; x++) {
         this.particle
            .newParticleGroup(
               particleX + Utilities.randomInt(-6, 6),
               particleY + Utilities.randomInt(-6, 6),
               ParticleModule.ParticleGroup.SMOKE_BELLOW,
               ParticleModule.ParticleSet.FIRE_BLUE
            );
      }
   }
}
