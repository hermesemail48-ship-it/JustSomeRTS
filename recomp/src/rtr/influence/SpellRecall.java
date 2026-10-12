package rtr.influence;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.map.MapData;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.resources.ResourceBase;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class SpellRecall extends SpellBase {
   protected int blastX;
   protected int blastY;

   @Override
   public void config(int x, int y, boolean freeCast) {
      this.type = SpellBase.SpellType.RECALL;
      this.name = Text.getText("spell.recall.name");
      this.description = Text.getText("spell.recall.description");
      this.cooldown = 300;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.RECALL_SPELL_COOLDOWN) > 0.0) {
         this.cooldown = (int)(this.cooldown - 60.0 * this.perk.getPerkTypeTotal(PerkModule.PerkType.RECALL_SPELL_COOLDOWN));
      }

      this.castCost = freeCast ? 0 : 200;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.RECALL_SPELL_COST) > 0.0) {
         this.castCost = (int)(this.castCost - this.castCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.RECALL_SPELL_COST));
      }

      this.blastX = x;
      this.blastY = y - 500 + Utilities.randomInt(50);
      this.targetX = x;
      this.targetY = y;
      this.tileX = x / 16;
      this.tileY = y / 16;
      this.duration = 1020;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.RECALL_SPELL_DURATION) > 0.0) {
         this.duration = (int)(this.duration + 60.0 * this.perk.getPerkTypeTotal(PerkModule.PerkType.RECALL_SPELL_DURATION));
      }

      this.radius = 8;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.RECALL_SPELL_RADIUS) > 0.0) {
         this.radius = (int)(this.radius + this.perk.getPerkTypeTotal(PerkModule.PerkType.RECALL_SPELL_RADIUS));
      }
   }

   @Override
   public void update() throws SlickException {
      this.durationTick++;
      if (this.durationTick > this.duration) {
         this.influence.removeSpell(this);
      }

      int randomX = Utilities.randomInt(this.radius * 16 * -1, this.radius * 16);
      int randomY = Utilities.randomInt(this.radius * 16 * -1, this.radius * 16);
      this.particle
         .newParticleGroup(
            this.targetX + randomX, this.targetY + randomY, ParticleModule.ParticleGroup.FLOATING_MAGIC_SQUARES, ParticleModule.ParticleSet.MAGIC_GREEN
         );
      randomX = Utilities.randomInt(this.radius * 16 * -1, this.radius * 16);
      randomY = Utilities.randomInt(this.radius * 16 * -1, this.radius * 16);
      this.particle
         .newParticleGroup(
            this.targetX + randomX, this.targetY + randomY, ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS, ParticleModule.ParticleSet.MAGIC_GREEN
         );

      for (int x = 0; x < 10; x++) {
         randomX = Utilities.randomInt(this.radius * 16 * -1, this.radius * 16);
         randomY = Utilities.randomInt(this.radius * 16 * -1, this.radius * 16);
         this.particle
            .newParticleGroup(this.targetX + randomX, this.targetY + randomY, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_GREEN);
      }

      switch (this.spellStep) {
         case 0:
            this.lighting.addShortTermLight(this.tileX, this.tileY, (byte)(this.radius + 4), (byte)10, this.duration);
            this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.CASTING_SPELLS);
            this.influence.decreaseInfluenceAmount(this.castCost);
            this.spellStep++;
            break;
         case 1:
            if (this.durationTick > 150 && this.durationTick % 10 == 0) {
               ArrayList<MobBase> mobsInRange = this.mob.getMobsNearby(this.tileX, this.tileY, this.radius, MobBase.MobGroup.VILLAGER);
               mobsInRange.addAll(this.mob.getMobsNearby(this.tileX, this.tileY, this.radius, MobBase.MobGroup.NOMAD));
               mobsInRange.addAll(this.mob.getMobsNearby(this.tileX, this.tileY, this.radius, MobBase.MobGroup.MIGRANT));
               mobsInRange.addAll(this.mob.getMobsNearby(this.tileX, this.tileY, this.radius, MobBase.MobGroup.PROVISIONER));
               mobsInRange.addAll(this.mob.getMobsNearby(this.tileX, this.tileY, this.radius, MobBase.MobGroup.DOMESTICATED_ANIMAL));
               if (mobsInRange.size() > 0) {
                  MobBase selectedTarget = mobsInRange.get(Utilities.randomInt(mobsInRange.size()));
                  OrderedPair randomCoordinates = null;
                  ObjectBase home = this.object.getObjects().get(selectedTarget.getAssignedHome());
                  ObjectBase castle = this.object.objectFindFirst(MapTilesLoader.TileSet.CASTLE_1);
                  if (home != null && !home.isDestroyed() && !this.tooClose(home)) {
                     randomCoordinates = home.getRandomCoordinates(selectedTarget, false, false);
                  } else if (castle != null && !castle.isDestroyed() && !this.tooClose(castle)) {
                     randomCoordinates = castle.getRandomCoordinates(selectedTarget, false, false);
                  }

                  if (randomCoordinates != null) {
                     if (selectedTarget.getMobGroup() == MobBase.MobGroup.NOMAD) {
                        this.goal.incrementGoal(GoalModule.GoalType.IMPATIENT);
                     }

                     if ((selectedTarget.getHitPoints() < selectedTarget.getHitPointsMax() || selectedTarget.isCompromised())
                        && (
                           selectedTarget.getActiveNode() == selectedTarget.getAIPanic()
                              || selectedTarget.getActiveNode() == selectedTarget.getAIOnFirePanic()
                              || selectedTarget.getActiveNode() == selectedTarget.getAICombatMobs()
                              || selectedTarget.getActiveNode() == selectedTarget.getAICombatBuildings()
                        )) {
                        this.goal.incrementGoal(GoalModule.GoalType.NEVER_GONNA_GIVE_YOU_UP);
                     }

                     this.moveMob(selectedTarget, randomCoordinates);
                  }

                  for (MobBase m : this.mob.getMobArray()) {
                     if (m.getAssignment().getPrimaryMob() == selectedTarget) {
                        m.getActiveNode().interruptNode(m.getAIConfused());
                     }
                  }
               }

               ArrayList<ResourceBase> resourcesInRange = this.resource.getResourcesNearby(this.tileX, this.tileY, this.radius);
               if (resourcesInRange.size() > 0) {
                  ResourceBase selectedTargetx = resourcesInRange.get(Utilities.randomInt(resourcesInRange.size()));
                  ObjectBase castlex = this.object.objectFindFirst(MapTilesLoader.TileSet.CASTLE_1);
                  if (castlex != null && !castlex.isDestroyed() && !this.tooClose(castlex)) {
                     OrderedPair randomCoordinatesx = castlex.getRandomCoordinates();
                     if (randomCoordinatesx != null) {
                        this.moveResource(selectedTargetx, randomCoordinatesx);
                     }
                  }
               }
            }
      }
   }

   private boolean tooClose(ObjectBase o) {
      int objectWidth = o.getObjectFlags().getWidth();
      int objectHeight = o.getObjectFlags().getHeight();
      int objectX = o.getObjectX();
      int objectY = o.getObjectY();

      for (int w = objectX; w < objectX + objectWidth; w++) {
         for (int h = objectY; h < objectY + objectHeight; h++) {
            int dx = this.tileX - w;
            int dy = this.tileY - h;
            int hValue = (int)Math.sqrt(dx * dx + dy * dy);
            if (hValue < this.radius) {
               return true;
            }
         }
      }

      return false;
   }

   private void moveMob(MobBase selectedTarget, OrderedPair randomCoordinates) throws SlickException {
      this.projectile
         .newEnergyBeam(
            selectedTarget.getCenterPixelX(true), selectedTarget.getCenterPixelY(true), randomCoordinates.getX() * 16 + 8, randomCoordinates.getY() * 16 + 8
         );
      this.lighting.addShortTermLight(randomCoordinates.getX(), randomCoordinates.getY(), (byte)5, (byte)5, 50);

      for (int x = 0; x < 5; x++) {
         this.projectile.newEnergyRing(selectedTarget.getCenterPixelX(true), selectedTarget.getCenterPixelY(true), 16, ParticleModule.ParticleSet.FIRE_BLUE);
      }

      for (int x = 0; x < 20; x++) {
         this.particle
            .newParticleGroup(
               selectedTarget.getCenterPixelX(true) + Utilities.randomInt(-1, 1),
               selectedTarget.getTopPixelY(true) + Utilities.randomInt(selectedTarget.getSprite().getSpriteWidth()),
               ParticleModule.ParticleGroup.SMOKE_BELLOW,
               ParticleModule.ParticleSet.FIRE_BLUE
            );
      }

      this.mob.tryConfuseInArea(selectedTarget.getTileX(), selectedTarget.getTileY(), 4, true, 3);
      if (Utilities.randomInt(4) == 0) {
         this.mob.influenceFaithInArea(selectedTarget.getTileX(), selectedTarget.getTileY(), 4, 1, 2, false);
      }

      this.sound.playSound(SoundModule.SoundType.MAGIC_RECALL, selectedTarget.getMobX(), selectedTarget.getMobY());
      selectedTarget.move(randomCoordinates);
      this.sound.playSound(SoundModule.SoundType.MAGIC_RECALL, selectedTarget.getMobX(), selectedTarget.getMobY());
      this.mob.tryConfuseInArea(selectedTarget.getTileX(), selectedTarget.getTileY(), 4, true, 3);
      if (Utilities.randomInt(4) == 0) {
         this.mob.influenceFaithInArea(selectedTarget.getTileX(), selectedTarget.getTileY(), 4, 1, 2, true);
      }

      for (int x = 0; x < 5; x++) {
         this.projectile.newEnergyRing(selectedTarget.getCenterPixelX(true), selectedTarget.getCenterPixelY(true), 16, ParticleModule.ParticleSet.FIRE_GREEN);
      }

      for (int x = 0; x < 20; x++) {
         this.particle
            .newParticleGroup(
               selectedTarget.getCenterPixelX(true) + Utilities.randomInt(-1, 1),
               selectedTarget.getTopPixelY(true) + Utilities.randomInt(selectedTarget.getSprite().getSpriteWidth()),
               ParticleModule.ParticleGroup.SMOKE_BELLOW,
               ParticleModule.ParticleSet.FIRE_BLUE
            );
      }

      selectedTarget.interruptNode(selectedTarget.getAIConfused());
      if (selectedTarget.getMobFlags().getAllowFaith()) {
         selectedTarget.increaseFaith(Utilities.randomInt(1, 5), true);
      }
   }

   private void moveResource(ResourceBase selectedTarget, OrderedPair randomCoordinates) throws SlickException {
      if (this.resource.isResourcesOnGround(randomCoordinates.getX(), randomCoordinates.getY())) {
         randomCoordinates = this.resource.findOpenSpace(randomCoordinates.getX(), randomCoordinates.getY(), MapData.BlockMapGroup.STANDARD);
      }

      this.projectile
         .newEnergyBeam(
            selectedTarget.getCenterPixelX(true), selectedTarget.getCenterPixelY(true), randomCoordinates.getX() * 16 + 8, randomCoordinates.getY() * 16 + 8
         );
      this.lighting.addShortTermLight(randomCoordinates.getX(), randomCoordinates.getY(), (byte)5, (byte)5, 50);

      for (int x = 0; x < 5; x++) {
         this.projectile.newEnergyRing(selectedTarget.getCenterPixelX(true), selectedTarget.getCenterPixelY(true), 16, ParticleModule.ParticleSet.FIRE_BLUE);
      }

      for (int x = 0; x < 20; x++) {
         this.particle
            .newParticleGroup(
               selectedTarget.getCenterPixelX(true) + Utilities.randomInt(-1, 1),
               selectedTarget.getTopPixelY(true) + Utilities.randomInt(selectedTarget.getSprite().getWidth()),
               ParticleModule.ParticleGroup.SMOKE_BELLOW,
               ParticleModule.ParticleSet.FIRE_BLUE
            );
      }

      this.mob.tryConfuseInArea(selectedTarget.getTileX(), selectedTarget.getTileY(), 4, true, 3);
      if (Utilities.randomInt(4) == 0) {
         this.mob.influenceFaithInArea(selectedTarget.getTileX(), selectedTarget.getTileY(), 4, 1, 2, false);
      }

      this.sound.playSound(SoundModule.SoundType.MAGIC_ATTRACT_FIRE, selectedTarget.getResourceX(), selectedTarget.getResourceY());
      selectedTarget.moveResource(randomCoordinates);
      this.sound.playSound(SoundModule.SoundType.MAGIC_ATTRACT_FIRE, selectedTarget.getResourceX(), selectedTarget.getResourceY());
      this.mob.tryConfuseInArea(selectedTarget.getTileX(), selectedTarget.getTileY(), 4, true, 3);
      if (Utilities.randomInt(4) == 0) {
         this.mob.influenceFaithInArea(selectedTarget.getTileX(), selectedTarget.getTileY(), 4, 1, 2, true);
      }

      for (int x = 0; x < 5; x++) {
         this.projectile.newEnergyRing(selectedTarget.getCenterPixelX(true), selectedTarget.getCenterPixelY(true), 16, ParticleModule.ParticleSet.FIRE_GREEN);
      }

      for (int x = 0; x < 20; x++) {
         this.particle
            .newParticleGroup(
               selectedTarget.getCenterPixelX(true) + Utilities.randomInt(-1, 1),
               selectedTarget.getTopPixelY(true) + Utilities.randomInt(selectedTarget.getSprite().getWidth()),
               ParticleModule.ParticleGroup.SMOKE_BELLOW,
               ParticleModule.ParticleSet.FIRE_BLUE
            );
      }
   }
}
