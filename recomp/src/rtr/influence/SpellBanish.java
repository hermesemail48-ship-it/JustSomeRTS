package rtr.influence;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.map.MapData;
import rtr.mobs.MobBase;
import rtr.particles.ParticleModule;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class SpellBanish extends SpellBase {
   protected int blastX;
   protected int blastY;
   protected OrderedPair randomCoordinates;

   @Override
   public void config(int x, int y, boolean freeCast) {
      this.type = SpellBase.SpellType.BANISH;
      this.name = Text.getText("spell.banish.name");
      this.description = Text.getText("spell.banish.description");
      this.cooldown = 300;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.BANISH_SPELL_COOLDOWN) > 0.0) {
         this.cooldown = (int)(this.cooldown - 60.0 * this.perk.getPerkTypeTotal(PerkModule.PerkType.BANISH_SPELL_COOLDOWN));
      }

      this.castCost = freeCast ? 0 : 500;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.BANISH_SPELL_COST) > 0.0) {
         this.castCost = (int)(this.castCost - this.castCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.BANISH_SPELL_COST));
      }

      this.blastX = x;
      this.blastY = y - 500 + Utilities.randomInt(50);
      this.targetX = x;
      this.targetY = y;
      this.tileX = x / 16;
      this.tileY = y / 16;
      this.duration = 1020;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.BANISH_SPELL_DURATION) > 0.0) {
         this.duration = (int)(this.duration + 60.0 * this.perk.getPerkTypeTotal(PerkModule.PerkType.BANISH_SPELL_DURATION));
      }

      this.radius = 8;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.BANISH_SPELL_RADIUS) > 0.0) {
         this.radius = (int)(this.radius + this.perk.getPerkTypeTotal(PerkModule.PerkType.BANISH_SPELL_RADIUS));
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
            this.targetX + randomX, this.targetY + randomY, ParticleModule.ParticleGroup.FLOATING_MAGIC_SQUARES, ParticleModule.ParticleSet.MAGIC_RED
         );
      randomX = Utilities.randomInt(this.radius * 16 * -1, this.radius * 16);
      randomY = Utilities.randomInt(this.radius * 16 * -1, this.radius * 16);
      this.particle
         .newParticleGroup(
            this.targetX + randomX, this.targetY + randomY, ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS, ParticleModule.ParticleSet.MAGIC_RED
         );

      for (int x = 0; x < 10; x++) {
         randomX = Utilities.randomInt(this.radius * 16 * -1, this.radius * 16);
         randomY = Utilities.randomInt(this.radius * 16 * -1, this.radius * 16);
         this.particle.newParticleGroup(this.targetX + randomX, this.targetY + randomY, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_RED);
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
               ArrayList<MobBase> mobsInRange = this.mob.getMobsNearby(this.tileX, this.tileY, this.radius, MobBase.MobGroup.MONSTER);
               if (mobsInRange.size() > 0) {
                  MobBase selectedTarget = mobsInRange.get(Utilities.randomInt(mobsInRange.size()));
                  this.mob.tryConfuseInArea(selectedTarget.getTileX(), selectedTarget.getTileY(), this.radius / 2, true, 2);
                  if (Utilities.randomInt(4) == 0) {
                     this.mob.influenceFaithInArea(selectedTarget.getTileX(), selectedTarget.getTileY(), this.radius / 2, 1, 2, true);
                  }

                  this.randomCoordinates = this.randomCoordinates();
                  this.moveMob(selectedTarget, this.randomCoordinates);

                  for (MobBase m : this.mob.getMobArray()) {
                     if (m.getAssignment().getPrimaryMob() == selectedTarget) {
                        m.getActiveNode().interruptNode(m.getAIConfused());
                     }
                  }
               }
            }
      }
   }

   protected OrderedPair randomCoordinates() {
      int randomX = Utilities.randomInt(255);

      int randomY;
      for (randomY = Utilities.randomInt(255);
         this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[randomX][randomY] > 0
            || this.map.getMap()[randomX][randomY][6] > 0
            || this.map.getMap()[randomX][randomY][9] > 0
            || this.object.isInRange(randomX, randomY);
         randomY = Utilities.randomInt(255)
      ) {
         randomX = Utilities.randomInt(255);
      }

      return OrderedPair.getOrderedPair(randomX, randomY);
   }

   private void moveMob(MobBase selectedTarget, OrderedPair randomCoordinates) throws SlickException {
      this.projectile
         .newEnergyBeam(
            selectedTarget.getCenterPixelX(true), selectedTarget.getCenterPixelY(true), randomCoordinates.getX() * 16 + 8, randomCoordinates.getY() * 16 + 8
         );
      this.lighting.addShortTermLight(randomCoordinates.getX(), randomCoordinates.getY(), (byte)5, (byte)5, 50);

      for (int x = 0; x < 5; x++) {
         this.projectile
            .newEnergyRing(selectedTarget.getCenterPixelX(true), selectedTarget.getCenterPixelY(true), this.radius * 2, ParticleModule.ParticleSet.FIRE_RED);
      }

      for (int x = 0; x < 20; x++) {
         this.particle
            .newParticleGroup(
               selectedTarget.getCenterPixelX(true) + Utilities.randomInt(-1, 1),
               selectedTarget.getTopPixelY(true) + Utilities.randomInt(selectedTarget.getSprite().getSpriteWidth()),
               ParticleModule.ParticleGroup.SMOKE_BELLOW,
               ParticleModule.ParticleSet.FIRE_PURPLE
            );
      }

      this.sound.playSound(SoundModule.SoundType.MAGIC_BANISH, selectedTarget.getMobX(), selectedTarget.getMobY());
      selectedTarget.move(randomCoordinates);
      this.sound.playSound(SoundModule.SoundType.MAGIC_BANISH, selectedTarget.getMobX(), selectedTarget.getMobY());

      for (int x = 0; x < 5; x++) {
         this.projectile
            .newEnergyRing(selectedTarget.getCenterPixelX(true), selectedTarget.getCenterPixelY(true), this.radius * 2, ParticleModule.ParticleSet.FIRE_RED);
      }

      for (int x = 0; x < 20; x++) {
         this.particle
            .newParticleGroup(
               selectedTarget.getCenterPixelX(true) + Utilities.randomInt(-1, 1),
               selectedTarget.getTopPixelY(true) + Utilities.randomInt(selectedTarget.getSprite().getSpriteWidth()),
               ParticleModule.ParticleGroup.SMOKE_BELLOW,
               ParticleModule.ParticleSet.FIRE_PURPLE
            );
      }

      selectedTarget.interruptNode(selectedTarget.getAIConfused());
   }
}
