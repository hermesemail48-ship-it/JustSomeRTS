package rtr.influence;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.gui.states.BrushLoader;
import rtr.map.MapModule;
import rtr.mobs.MobBase;
import rtr.particles.ParticleModule;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class SpellDissolve extends SpellBase {
   protected ArrayList<OrderedPair> regrowTilesInRange = new ArrayList<>();

   @Override
   public void config(int x, int y, boolean freeCast) {
      this.type = SpellBase.SpellType.DISSOLVE;
      this.name = Text.getText("spell.dissolve.name");
      this.description = Text.getText("spell.dissolve.description");
      this.cooldown = 240;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.DISSOLVE_SPELL_COOLDOWN) > 0.0) {
         this.cooldown = (int)(this.cooldown - 60.0 * this.perk.getPerkTypeTotal(PerkModule.PerkType.DISSOLVE_SPELL_COOLDOWN));
      }

      this.castCost = freeCast ? 0 : 200;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.DISSOLVE_SPELL_COST) > 0.0) {
         this.castCost = (int)(this.castCost - this.castCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.DISSOLVE_SPELL_COST));
      }

      this.targetX = x;
      this.targetY = y;
      this.tileX = x / 16;
      this.tileY = y / 16;
      this.duration = 660;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.DISSOLVE_SPELL_DURATION) > 0.0) {
         this.duration = (int)(this.duration + 60.0 * this.perk.getPerkTypeTotal(PerkModule.PerkType.DISSOLVE_SPELL_DURATION));
      }

      this.radius = 8;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.DISSOLVE_SPELL_RADIUS) > 0.0) {
         this.radius = (int)(this.radius + this.perk.getPerkTypeTotal(PerkModule.PerkType.DISSOLVE_SPELL_RADIUS));
      }
   }

   @Override
   public void update() throws SlickException {
      this.particle
         .newParticle(
            this.targetX + Utilities.randomInt(-2, 2),
            this.targetY + Utilities.randomInt(-2, 2),
            ParticleModule.ParticleType.FIRE_SPECK,
            ParticleModule.ParticleSet.FIRE_BLUE
         );
      this.particle.newParticle(this.targetX, this.targetY, ParticleModule.ParticleType.MAGIC_FLOAT_SPECK, ParticleModule.ParticleSet.MAGIC_GREEN);
      switch (this.spellStep) {
         case 0:
            this.lighting.addShortTermLight(this.tileX, this.tileY, (byte)this.radius, (byte)3, this.duration);
            this.sound.playSound(SoundModule.SoundType.DISSOLVE_SPELL, this.targetX, this.targetY);
            this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.CASTING_SPELLS);
            this.influence.decreaseInfluenceAmount(this.castCost);
            this.spellStep++;
            break;
         case 1:
            for (int x = this.tileX - this.radius; x < this.tileX + this.radius; x++) {
               for (int y = this.tileY - this.radius; y < this.tileY + this.radius; y++) {
                  if (!Utilities.isOutOfBoundsOfUsableMap(x, y)) {
                     int dx = this.tileX - x;
                     int dy = this.tileY - y;
                     int hValue = (int)Math.sqrt(dx * dx + dy * dy);
                     if (hValue < this.radius) {
                        this.regrowTilesInRange.add(OrderedPair.getOrderedPair(x, y));
                     }
                  }
               }
            }

            if (this.regrowTilesInRange.size() <= 0) {
               this.influence.removeSpell(this);
               return;
            }

            this.spellStep++;
            break;
         case 2:
            this.durationTick++;
            int randomX = 0;
            int randomY = 0;
            this.projectile.newEnergyRing(this.targetX, this.targetY, 16, ParticleModule.ParticleSet.FIRE_GREEN);
            OrderedPair random = this.regrowTilesInRange.get(Utilities.randomInt(this.regrowTilesInRange.size()));

            for (int x = 0; x < 5; x++) {
               randomX = random.getX() * 16 + Utilities.randomInt(16);
               randomY = random.getY() * 16 + Utilities.randomInt(16);
               this.particle.newParticle(randomX, randomY, ParticleModule.ParticleType.FIRE_SPECK, ParticleModule.ParticleSet.FIRE_GREEN);
            }

            for (int x = 0; x < 5; x++) {
               randomX = random.getX() * 16 + Utilities.randomInt(16);
               randomY = random.getY() * 16 + Utilities.randomInt(16);
               this.particle.newParticle(randomX, randomY, ParticleModule.ParticleType.MAGIC_FLOAT_SPECK, ParticleModule.ParticleSet.MAGIC_GREEN);
            }

            if (this.durationTick > 100) {
               if (this.durationTick % 5 == 0) {
                  int tileID = this.map.getTileId(random.getX(), random.getY(), 6);
                  int essenceAmount = this.map.getMapTileLoader().getTileEssenceAmount(tileID);
                  if (essenceAmount <= 0) {
                     tileID = this.map.getTileId(random.getX(), random.getY(), 9);
                     essenceAmount = this.map.getMapTileLoader().getTileEssenceAmount(tileID);
                  }

                  if (essenceAmount > 0) {
                     this.resource.damageResource(random.getX(), random.getY());
                     if (this.resource.getResourceValues()[random.getX()][random.getY()] == 0) {
                        this.essence.newEssence(random.getX() * 16 + 8, random.getY() * 16 + 8, Utilities.randomInt(essenceAmount) + 1, 1);
                        this.map.erase(random.getX(), random.getY(), MapModule.EraseMode.TOPOGRAPHY, 1, BrushLoader.BrushType.ROUND);
                        this.goal.incrementGoal(GoalModule.GoalType.ECO_TERRORIST);
                     }

                     if (Utilities.randomInt(2) == 0) {
                        this.sound.playSound(SoundModule.SoundType.DISSOLVE, random.getX() * 16, random.getY() * 16);
                     }

                     this.lighting.addShortTermLight(random.getX(), random.getY(), (byte)(this.radius / 2), (byte)4, 20);
                     randomX = random.getX() * 16 + Utilities.randomInt(16);
                     randomY = random.getY() * 16 + Utilities.randomInt(16);

                     for (int x = 0; x < 50; x++) {
                        randomX = random.getX() * 16 + Utilities.randomInt(-16, 16);
                        randomY = random.getY() * 16 + Utilities.randomInt(-16, 16);
                        this.particle.newParticle(randomX, randomY, ParticleModule.ParticleType.SMOKE_SPECK, ParticleModule.ParticleSet.GRAY);
                     }
                  }
               }

               if (this.durationTick % 100 == 0) {
                  this.mob.tryConfuseInArea(this.tileX, this.tileY, this.radius, true, 1);
                  this.mob.tryPanicInArea(this.tileX, this.tileY, this.radius, true, 3);
                  this.mob.influenceFaithInArea(this.tileX, this.tileY, this.radius, 1, 3, false);
               }

               if (this.durationTick % 10 == 0) {
                  for (MobBase m : this.mob.getDeadMobsNearby(this.tileX, this.tileY, this.radius)) {
                     if (m.isDead() && Utilities.randomInt(10) == 0) {
                        m.poke();
                        if (m.getDecayTick() >= 200000) {
                           this.goal.incrementGoal(GoalModule.GoalType.POPCORN);
                        }

                        if (Utilities.randomInt(4) == 0) {
                           this.sound.playSound(SoundModule.SoundType.DISSOLVE, m.getMobX(), m.getMobY());
                        }
                     }
                  }
               }
            }

            if (this.durationTick > this.duration) {
               this.influence.removeSpell(this);
            }
      }
   }
}
