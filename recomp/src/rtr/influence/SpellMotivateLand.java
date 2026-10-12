package rtr.influence;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.map.RegrowTile;
import rtr.map.SpreadTile;
import rtr.particles.ParticleModule;
import rtr.resources.FarmlandBase;
import rtr.utilities.Utilities;

public class SpellMotivateLand extends SpellBase {
   protected ArrayList<RegrowTile> regrowTiles = this.mapAI.getRegrowArray();
   protected ArrayList<SpreadTile> spreadTiles = this.mapAI.getSpreadArray();
   protected ArrayList<FarmlandBase> farmland = this.resource.getFarmlandArray();
   protected ArrayList<RegrowTile> regrowTilesInRange = new ArrayList<>();
   protected ArrayList<SpreadTile> spreadTilesInRange = new ArrayList<>();
   protected ArrayList<FarmlandBase> farmlandInRange = new ArrayList<>();

   @Override
   public void config(int x, int y, boolean freeCast) {
      this.type = SpellBase.SpellType.MOTIVATE_LAND;
      this.name = Text.getText("spell.motivateLand.name");
      this.description = Text.getText("spell.motivateLand.description");
      this.cooldown = 300;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.MOTIVATE_LAND_SPELL_COOLDOWN) > 0.0) {
         this.cooldown = (int)(this.cooldown - 60.0 * this.perk.getPerkTypeTotal(PerkModule.PerkType.MOTIVATE_LAND_SPELL_COOLDOWN));
      }

      this.castCost = freeCast ? 0 : 700;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.MOTIVATE_LAND_SPELL_COST) > 0.0) {
         this.castCost = (int)(this.castCost - this.castCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.MOTIVATE_LAND_SPELL_COST));
      }

      this.targetX = x;
      this.targetY = y;
      this.tileX = x / 16;
      this.tileY = y / 16;
      this.duration = 750;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.MOTIVATE_LAND_SPELL_DURATION) > 0.0) {
         this.duration = (int)(this.duration + 60.0 * this.perk.getPerkTypeTotal(PerkModule.PerkType.MOTIVATE_LAND_SPELL_DURATION));
      }

      this.radius = 10;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.MOTIVATE_LAND_SPELL_RADIUS) > 0.0) {
         this.radius = (int)(this.radius + this.perk.getPerkTypeTotal(PerkModule.PerkType.MOTIVATE_LAND_SPELL_RADIUS));
      }
   }

   @Override
   public void update() throws SlickException {
      this.particle
         .newParticleGroup(
            this.targetX + Utilities.randomInt(-2, 2),
            this.targetY + Utilities.randomInt(-2, 2),
            ParticleModule.ParticleGroup.FIRE,
            ParticleModule.ParticleSet.FIRE_BLUE
         );
      this.particle.newParticleGroup(this.targetX, this.targetY, ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS, ParticleModule.ParticleSet.MAGIC_GREEN);
      switch (this.spellStep) {
         case 0:
            this.lighting.addShortTermLight(this.tileX, this.tileY, (byte)this.radius, (byte)3, this.duration);
            this.sound.playSound(SoundModule.SoundType.MOTIVATE_LAND_SPELL, this.targetX, this.targetY);
            this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.CASTING_SPELLS);
            this.influence.decreaseInfluenceAmount(this.castCost);
            this.spellStep++;
            break;
         case 1:
            for (RegrowTile tile : this.regrowTiles) {
               int dx = this.tileX - tile.getRegrowX();
               int dy = this.tileY - tile.getRegrowY();
               int hValue = (int)Math.sqrt(dx * dx + dy * dy);
               if (hValue < this.radius) {
                  this.regrowTilesInRange.add(tile);
               }
            }

            for (SpreadTile tilex : this.spreadTiles) {
               int dx = this.tileX - tilex.getSpreadX();
               int dy = this.tileY - tilex.getSpreadY();
               int hValue = (int)Math.sqrt(dx * dx + dy * dy);
               if (hValue < this.radius) {
                  this.spreadTilesInRange.add(tilex);
               }
            }

            for (FarmlandBase tilexx : this.farmland) {
               int dx = this.tileX - tilexx.getTileX();
               int dy = this.tileY - tilexx.getTileY();
               int hValue = (int)Math.sqrt(dx * dx + dy * dy);
               if (hValue < this.radius) {
                  this.farmlandInRange.add(tilexx);
               }
            }

            this.spellStep++;
            break;
         case 2:
            this.durationTick++;
            if (this.durationTick > this.duration) {
               this.spellStep++;
            }

            if (this.durationTick == 100) {
               this.sound.playSound(SoundModule.SoundType.MAGIC_HEAL_SPELL, this.targetX, this.targetY);
            }

            if (this.durationTick == 250) {
               for (int x = 0; x < 20; x++) {
                  this.projectile.newEnergyNova(this.targetX, this.targetY, ParticleModule.ParticleSet.FIRE_GREEN);
               }

               this.projectile.newExpandingEnergyRing(this.targetX, this.targetY, this.radius * 16, 1.0F, ParticleModule.ParticleSet.FIRE_GREEN);
               this.projectile.newExpandingFireRing(this.targetX, this.targetY, this.radius * 16, 0.5F, ParticleModule.ParticleSet.FIRE_GREEN);
            }

            if (this.durationTick > 250 && this.durationTick % 5 == 0) {
               if (Utilities.randomInt(2) == 0) {
                  if (this.regrowTilesInRange.size() > 0) {
                     RegrowTile motivatedTile = this.regrowTilesInRange.get(Utilities.randomInt(this.regrowTilesInRange.size()));

                     for (int x = 0; x < 2; x++) {
                        this.particle
                           .newParticleGroup(
                              motivatedTile.getRegrowX() * 16 + 8,
                              motivatedTile.getRegrowY() * 16 + 8,
                              ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS,
                              ParticleModule.ParticleSet.MAGIC_GREEN
                           );
                     }

                     motivatedTile.motivate(250);
                     if (motivatedTile.tryRegrow()) {
                        if (Utilities.randomInt(2) == 0) {
                           this.sound.playSound(SoundModule.SoundType.STORE_ITEM, this.targetX, this.targetY);
                        } else {
                           this.sound.playSound(SoundModule.SoundType.EARTHQUAKE_ROCKS, this.targetX, this.targetY);
                        }
                     }

                     if (this.durationTick % 50 == 0) {
                        this.mob.tryConfuseInArea(motivatedTile.getRegrowX(), motivatedTile.getRegrowY(), this.radius, true, 1);
                        this.mob.influenceFaithInArea(motivatedTile.getRegrowX(), motivatedTile.getRegrowY(), this.radius, 1, true);
                     }

                     this.lighting.addShortTermLight(motivatedTile.getRegrowX(), motivatedTile.getRegrowY(), (byte)5, (byte)4, this.duration);
                  }
               } else if (this.spreadTilesInRange.size() > 0) {
                  SpreadTile motivatedTile = this.spreadTilesInRange.get(Utilities.randomInt(this.spreadTilesInRange.size()));

                  for (int x = 0; x < 2; x++) {
                     this.particle
                        .newParticleGroup(
                           motivatedTile.getSpreadX() * 16 + 8,
                           motivatedTile.getSpreadY() * 16 + 8,
                           ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS,
                           ParticleModule.ParticleSet.MAGIC_GREEN
                        );
                  }

                  motivatedTile.motivate();
                  if (motivatedTile.trySpread()) {
                     if (Utilities.randomInt(2) == 0) {
                        this.sound.playSound(SoundModule.SoundType.STORE_ITEM, this.targetX, this.targetY);
                     } else {
                        this.sound.playSound(SoundModule.SoundType.EARTHQUAKE_ROCKS, this.targetX, this.targetY);
                     }
                  }

                  if (this.durationTick % 50 == 0) {
                     this.mob.tryConfuseInArea(motivatedTile.getSpreadX(), motivatedTile.getSpreadY(), this.radius, true, 2);
                     this.mob.influenceFaithInArea(motivatedTile.getSpreadX(), motivatedTile.getSpreadY(), this.radius, 1, true);
                  }

                  this.lighting.addShortTermLight(motivatedTile.getSpreadX(), motivatedTile.getSpreadY(), (byte)5, (byte)4, this.duration);
               }

               if (this.farmlandInRange.size() > 0) {
                  FarmlandBase motivatedTile = this.farmlandInRange.get(Utilities.randomInt(this.farmlandInRange.size()));

                  for (int x = 0; x < 2; x++) {
                     this.particle
                        .newParticleGroup(
                           motivatedTile.getTileX() * 16 + 8,
                           motivatedTile.getTileY() * 16 + 8,
                           ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS,
                           ParticleModule.ParticleSet.MAGIC_GREEN
                        );
                  }

                  motivatedTile.motivate();
                  if (Utilities.randomInt(100) == 0) {
                     if (Utilities.randomInt(2) == 0) {
                        this.sound.playSound(SoundModule.SoundType.STORE_ITEM, this.targetX, this.targetY);
                     } else {
                        this.sound.playSound(SoundModule.SoundType.EARTHQUAKE_ROCKS, this.targetX, this.targetY);
                     }
                  }

                  if (this.durationTick % 50 == 0) {
                     this.mob.tryConfuseInArea(motivatedTile.getTileX(), motivatedTile.getTileY(), this.radius, true, 1);
                     this.mob.influenceFaithInArea(motivatedTile.getTileX(), motivatedTile.getTileY(), this.radius, 1, true);
                  }

                  this.lighting.addShortTermLight(motivatedTile.getTileX(), motivatedTile.getTileY(), (byte)5, (byte)4, this.duration);
               }
            }
            break;
         case 3:
            if (this.spreadTilesInRange.size() > 0) {
               for (SpreadTile s : this.spreadTilesInRange) {
                  s.motivate();
               }
            }

            if (this.regrowTilesInRange.size() > 0) {
               for (RegrowTile r : this.regrowTilesInRange) {
                  r.motivate(250);
               }
            }

            this.influence.removeSpell(this);
      }
   }
}
