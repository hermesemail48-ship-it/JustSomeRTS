package rtr.influence;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.mobs.MobBase;
import rtr.mobs.MobFactory;
import rtr.particles.ParticleModule;
import rtr.system.Game;
import rtr.utilities.Utilities;

public class SpellResurrect extends SpellBase {
   @Override
   public void config(int x, int y, boolean freeCast) {
      this.type = SpellBase.SpellType.RESURRECT;
      this.name = Text.getText("spell.resurrect.name");
      this.description = Text.getText("spell.resurrect.description");
      this.cooldown = 300;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.RESSURECT_SPELL_COOLDOWN) > 0.0) {
         this.cooldown = (int)(this.cooldown - 60.0 * this.perk.getPerkTypeTotal(PerkModule.PerkType.RESSURECT_SPELL_COOLDOWN));
      }

      this.castCost = freeCast ? 0 : 500;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.RESSURECT_SPELL_COST) > 0.0) {
         this.castCost = (int)(this.castCost - this.castCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.RESSURECT_SPELL_COST));
      }

      this.targetX = x;
      this.targetY = y;
      this.tileX = x / 16;
      this.tileY = y / 16;
   }

   @Override
   public void update() throws SlickException {
      switch (this.spellStep) {
         case 0:
            ArrayList<MobBase> nearbyMobsInRange = this.mob.getMobsNearby(this.tileX, this.tileY, 2);
            ArrayList<MobBase> ghosts = new ArrayList<>();

            for (MobBase m : nearbyMobsInRange) {
               if (m.getMobFlags().getGhostBaseType() != null && m.getAlpha() > 0.0F) {
                  ghosts.add(m);
               }
            }

            if (ghosts.size() > 0) {
               MobBase ghost = ghosts.get(Utilities.randomInt(ghosts.size()));
               this.sound.playSound(SoundModule.SoundType.RESURRECT);
               this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.CASTING_SPELLS);
               this.influence.decreaseInfluenceAmount(this.castCost);
               int resurrectChance = 10;
               if (this.perk.getPerkTypeTotal(PerkModule.PerkType.RESURRECT_SPELL_SUCCESS_CHANCE) > 0.0) {
                  resurrectChance = (int)(resurrectChance + resurrectChance * this.perk.getPerkTypeTotal(PerkModule.PerkType.RESURRECT_SPELL_SUCCESS_CHANCE));
               }

               MobBase.MobGroup ghostResurrectType = MobFactory.getMobInstance(ghost.getMobFlags().getGhostBaseType()).getMobGroup();
               if (ghostResurrectType == MobBase.MobGroup.VILLAGER) {
                  this.goal.incrementGoal(GoalModule.GoalType.RISE_FROM_THE_GRAVE);
               }

               if (ghostResurrectType == MobBase.MobGroup.DOMESTICATED_ANIMAL) {
                  this.goal.incrementGoal(GoalModule.GoalType.WAS_I_A_GOOD_BOY);
               }

               if (Utilities.randomInt(resurrectChance) == 0 && ghost.getMobFlags().getGhostNegativeBaseType() != null) {
                  this.sound.playSound(SoundModule.SoundType.RESURRECT_NEGATIVE);
                  this.mob.tryPanicInArea(this.tileX, this.tileY, 6, true, 1);
                  this.mob.influenceFaithInArea(this.tileX, this.tileY, 6, 10, 15, true);
                  if (ghost.getBoundGhostResource() != null) {
                     for (int x = 0; x < 5; x++) {
                        this.projectile
                           .newEnergyBeam(
                              ghost.getCenterPixelX(true),
                              ghost.getCenterPixelY(true),
                              ghost.getBoundGhostResource().getCenterPixelX(true),
                              ghost.getBoundGhostResource().getCenterPixelY(true)
                           );
                     }

                     this.resource.deleteResource(ghost.getBoundGhostResource());
                  }

                  if (ghost.getBoundGhostBody() != null) {
                     MobBase ghostBody = ghost.getBoundGhostBody();

                     for (int x = 0; x < 5; x++) {
                        this.projectile
                           .newEnergyBeam(
                              ghost.getCenterPixelX(true), ghost.getCenterPixelY(true), ghostBody.getCenterPixelX(true), ghostBody.getCenterPixelY(true)
                           );
                     }

                     while (ghostBody != null && ghostBody.getDecayTick() < 200000) {
                        ghostBody.poke();
                     }
                  }

                  this.mob.spawnMob(ghost, ghost.getMobFlags().getGhostNegativeBaseType(), true);

                  for (int x = 0; x < 5; x++) {
                     this.projectile
                        .newEnergyBeam(
                           ghost.getCenterPixelX(true),
                           ghost.getCenterPixelY(true),
                           ghost.getCenterPixelX(true) + Utilities.randomInt(-8, 8),
                           ghost.getCenterPixelY(true) - 500
                        );
                  }

                  for (int x = 0; x < 20; x++) {
                     this.projectile.newEnergyNova(ghost.getCenterPixelX(true), ghost.getCenterPixelY(true), ParticleModule.ParticleSet.FIRE_RED);
                  }

                  for (int x = 0; x < 10; x++) {
                     this.projectile.newEnergyRing(ghost.getCenterPixelX(true), ghost.getCenterPixelY(true), 16, ParticleModule.ParticleSet.FIRE_RED);
                  }

                  for (int x = 0; x < 40; x++) {
                     this.particle
                        .newParticleGroup(
                           ghost.getCenterPixelX(true) + Utilities.randomInt(-8, 8),
                           ghost.getCenterPixelY(true) + Utilities.randomInt(-8, 8),
                           ParticleModule.ParticleGroup.SMOKE_BELLOW,
                           ParticleModule.ParticleSet.FIRE_RED
                        );
                  }
               } else {
                  this.sound.playSound(SoundModule.SoundType.FULL_MOON);
                  this.mob.tryConfuseInArea(this.tileX, this.tileY, 6, true, 1);
                  this.mob.influenceFaithInArea(this.tileX, this.tileY, 6, 5, 10, true);
                  if (ghost.getBoundGhostResource() != null) {
                     for (int x = 0; x < 5; x++) {
                        this.projectile
                           .newEnergyBeam(
                              ghost.getCenterPixelX(true),
                              ghost.getCenterPixelY(true),
                              ghost.getBoundGhostResource().getCenterPixelX(true),
                              ghost.getBoundGhostResource().getCenterPixelY(true)
                           );
                     }

                     this.resource.deleteResource(ghost.getBoundGhostResource());
                  }

                  if (ghost.getBoundGhostBody() != null) {
                     MobBase ghostBody = ghost.getBoundGhostBody();

                     for (int x = 0; x < 5; x++) {
                        this.projectile
                           .newEnergyBeam(
                              ghost.getCenterPixelX(true), ghost.getCenterPixelY(true), ghostBody.getCenterPixelX(true), ghostBody.getCenterPixelY(true)
                           );
                     }

                     while (ghostBody != null && ghostBody.getDecayTick() < 200000) {
                        ghostBody.poke();
                     }
                  }

                  MobBase newMob = this.mob.spawnMob(ghost, ghost.getMobFlags().getGhostBaseType(), true);
                  if (newMob.getMobFlags().getAllowFaith()) {
                     newMob.setFaith(newMob.getFaithMax());
                  }

                  for (int x = 0; x < 5; x++) {
                     this.projectile
                        .newEnergyBeam(
                           ghost.getCenterPixelX(true),
                           ghost.getCenterPixelY(true),
                           ghost.getCenterPixelX(true) + Utilities.randomInt(-8, 8),
                           ghost.getCenterPixelY(true) - 500
                        );
                  }

                  for (int x = 0; x < 20; x++) {
                     this.projectile.newEnergyNova(ghost.getCenterPixelX(true), ghost.getCenterPixelY(true), ParticleModule.ParticleSet.FIRE_PURPLE);
                  }

                  for (int x = 0; x < 10; x++) {
                     this.projectile.newEnergyRing(ghost.getCenterPixelX(true), ghost.getCenterPixelY(true), 16, ParticleModule.ParticleSet.MAGIC_PURPLE);
                  }

                  for (int x = 0; x < 40; x++) {
                     this.particle
                        .newParticleGroup(
                           ghost.getCenterPixelX(true) + Utilities.randomInt(-8, 8),
                           ghost.getCenterPixelY(true) + Utilities.randomInt(-8, 8),
                           ParticleModule.ParticleGroup.SMOKE_BELLOW,
                           ParticleModule.ParticleSet.MAGIC_BLUE
                        );
                  }
               }
            }

            this.influence.removeSpell(this);
      }
   }

   @Override
   public boolean canCast() throws SlickException {
      if (!super.canCast()) {
         return false;
      } else {
         ArrayList<MobBase> nearbyMobsInRange = this.mob.getMobsNearby(Game.getCS().getMouseTileX(), Game.getCS().getMouseTileY(), 2);
         ArrayList<MobBase> ghosts = new ArrayList<>();

         for (MobBase m : nearbyMobsInRange) {
            if (m.getMobFlags().getGhostBaseType() != null && m.getAlpha() > 0.0F) {
               ghosts.add(m);
            }
         }

         return ghosts.size() > 0;
      }
   }
}
