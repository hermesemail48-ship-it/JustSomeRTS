package rtr.mobs.ai;

import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.SoundModule;
import rtr.console.Console;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.system.Game;
import rtr.utilities.Utilities;
import rtr.weather.disasters.DisasterBase;

public class AIDeath extends AIBase {
   private static final int DEATH = 0;
   private static final int DECAY = 1;

   public AIDeath(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_DEATH;
   }

   @Override
   public boolean allowRun() {
      return true;
   }

   @Override
   public boolean tryRun() {
      return this.allowRun();
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         switch (this.aiStep) {
            case 0:
               this.death();
               break;
            case 1:
               this.decay();
         }
      }
   }

   private void death() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.death.death"));
      if (this.thisMob.isDead()) {
         this.aiStep = 1;
      } else {
         if (this.thisMob.getMobGroup() == MobBase.MobGroup.VILLAGER || this.thisMob.getMobGroup() == MobBase.MobGroup.DOMESTICATED_ANIMAL) {
            this.randomDeathMessage();
         }

         if (this.thisMob.getLastDamageCause() != MobBase.DamageCause.NATURAL_CAUSES) {
            if (this.thisMobFlags.getMobSoundSet(this.thisMob.getGender()) == SoundModule.SoundSet.VILLAGER_MALE && Utilities.randomInt(200) == 1) {
               this.sound.playSound(SoundModule.SoundType.WILHELM, this.thisMob.getMobX(), this.thisMob.getMobY());
            } else {
               SoundModule.SoundType deathSound = this.thisMobFlags.getMobSoundSet(this.thisMob.getGender()).getSoundDeath();
               if (deathSound != null) {
                  this.sound.playSound(deathSound, this.thisMob.getMobX(), this.thisMob.getMobY());
               }
            }
         }

         if (this.thisMobFlags.getAllowDeathSplit()
            && Utilities.randomInt(this.thisMobFlags.getDeathSplitChance()) == 0
            && this.thisMob.getLastDamageCause() != MobBase.DamageCause.NATURAL_CAUSES) {
            int splitAmount = Utilities.randomInt(this.thisMobFlags.getDeathSplitAmount());
            if (splitAmount <= 1) {
               splitAmount = 2;
            }

            int tileX = this.thisMob.getTileX();
            int tileY = this.thisMob.getTileY();
            byte[][] blockMap = this.map.getMapData().getBlockMap(this.thisMobFlags.getBlockMapGroup());

            for (int x = 0; x < splitAmount; x++) {
               int randomX = tileX + Utilities.randomInt(-2, 2);

               int randomY;
               for (randomY = tileY + Utilities.randomInt(-2, 2); blockMap[randomX][randomY] > 0; randomY = tileY + Utilities.randomInt(-2, 2)) {
                  randomX = tileX + Utilities.randomInt(-2, 2);
               }

               this.mob.newMob(randomX, randomY, this.thisMobFlags.getDeathSplitType(), true);
            }
         }

         int essenceAmount = (this.thisMob.getLevel() * this.thisMobFlags.getExperienceMultiplier() + this.thisMob.getExperience()) / 25;

         while (essenceAmount > 0) {
            int randomEnergy = Utilities.randomInt(16) + 1;

            while (randomEnergy > essenceAmount) {
               randomEnergy = Utilities.randomInt(16) + 1;
            }

            this.essence.newEssence(this.thisMob.getCenterPixelX(true), this.thisMob.getCenterPixelY(true), randomEnergy, 1);
            essenceAmount -= randomEnergy;
         }

         this.thisMob.setHitPoints(0);
         this.thisMob.setDead(true);
         int tileX = this.thisMob.getTileX();
         int tileY = this.thisMob.getTileY();
         if (this.thisMob.getMobGroup() == MobBase.MobGroup.VILLAGER) {
            this.stats.increaseVillagerDeathsThisPeriod(this.thisMob.getMobType());
            this.goal.incrementGoal(GoalModule.GoalType.WEAKEST_LINKS);
            if (this.thisMob.getLastDamageCause() == MobBase.DamageCause.FIRE || this.thisMob.getLastDamageCause() == MobBase.DamageCause.ON_FIRE) {
               this.goal.incrementGoal(GoalModule.GoalType.BURNED_ALIVE);
            }

            if (this.thisMob.getLastDamageCause() == MobBase.DamageCause.NATURAL_CAUSES) {
               this.goal.incrementGoal(GoalModule.GoalType.HOSPICE_CARE);
               this.god.increaseGodXP(5.0, GodModule.GodXPType.LIVED_FULL_LIFE);
            } else {
               for (MobBase m : this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false)) {
                  if (m.getMobFlags().getAllowFaith()) {
                     m.decreaseFaith(Utilities.randomInt(1, 3), true);
                  }
               }

               this.mob.influenceFaithInArea(tileX, tileY, 8, 10, 15, false);
               if (this.thisMob.getMobType() == MobBase.MobType.CATJEET_VILLAGER_CHILD
                  || this.thisMob.getMobType() == MobBase.MobType.CATJEET_VILLAGER
                  || this.thisMob.getMobType() == MobBase.MobType.CATJEET_VILLAGER_ELDER
                  || this.thisMob.getMobType() == MobBase.MobType.CATJEET_VILLAGER_CHILD_NEPHILIM
                  || this.thisMob.getMobType() == MobBase.MobType.CATJEET_VILLAGER_NEPHILIM
                  || this.thisMob.getMobType() == MobBase.MobType.CATJEET_VILLAGER_ELDER_NEPHILIM) {
                  this.goal.incrementGoal(GoalModule.GoalType.CATJEETASTROPHIC);
               }
            }

            MobBase mate = this.thisMob.getMate();
            if (mate != null && mate.getMobFlags().getAllowFaith()) {
               mate.decreaseFaith(mate.getFaithMax() / 4, true);
            }
         } else if (this.thisMob.getMobGroup() == MobBase.MobGroup.DOMESTICATED_ANIMAL) {
            this.stats.increaseAnimalDeathsThisPeriod(this.thisMob.getMobType());
            if (this.thisMob.getMobType() == MobBase.MobType.DOGGO || this.thisMob.getMobType() == MobBase.MobType.DOOFY_DOGGO) {
               this.goal.incrementGoal(GoalModule.GoalType.ANIMAL_RIGHTS_VIOLATIONS);
            }
         } else if (this.thisMob.getMobGroup() == MobBase.MobGroup.MONSTER) {
            this.god.increaseGodXP(0.05, GodModule.GodXPType.MONSTERS_KILLED);
            this.stats.increaseMonstersKilledThisPeriod(this.thisMob.getMobType());
            this.mob.influenceFaithInArea(tileX, tileY, 4, 1, true);
            if (this.thisMob.getMobType() == MobBase.MobType.SKELETON && this.thisMob.getLastDamageType() == MobBase.DamageType.PIERCING) {
               this.goal.incrementGoal(GoalModule.GoalType.POOR_WEAPONRY);
            }

            if (this.thisMob.getMobType() == MobBase.MobType.DRONE) {
               this.goal.incrementGoal(GoalModule.GoalType.IT_NEVER_STOPS);
            }

            int tileID = this.map.getTileId(tileX, tileY, 6);
            if (this.map.getMapTileLoader().isTileWater(tileID) && !this.map.isTileFrozen(tileX, tileY) && this.resource.getResourceValues()[tileX][tileY] > 0) {
               this.goal.incrementGoal(GoalModule.GoalType.WATER_JOUSTING);
            }

            if (this.time.isFullMoon()) {
               this.goal.incrementGoal(GoalModule.GoalType.THE_MOON);
            }
         }

         this.thisMob.unassignFromAll(false);
         if (!this.thisMob.isDropsBlocked()) {
            this.thisMob.generateDrops();
         }

         if (this.thisMobFlags.getAllowGhost() && this.thisMob.getLastDamageCause() != MobBase.DamageCause.NATURAL_CAUSES) {
            MobBase mx = this.mob.spawnMob(this.thisMob, this.thisMobFlags.getGhostSpawnType(), false);
            mx.setBoundGhost(this.thisMob);
            this.thisMob.setBoundGhost(mx);
         }

         if (Game.getCS() == Game.getMainMenuState()) {
            this.mob.increaseMainMenuKillCount();
            this.font.newFloater("$GRE1" + this.mob.getMainMenuKillCount(), this.thisMob.getMobX(), this.thisMob.getMobY());
            if (this.mob.getMainMenuKillCount() % 50 == 0) {
               this.mob.resetMainMenuKillCount();
               this.weather.forceNewDisaster(DisasterBase.DisasterType.MUSIC, false);
            }

            if (this.mob.getMainMenuKillCount() == 41) {
               this.sound.playSound(SoundModule.SoundType.RETRO_NOTE_1);
            }

            if (this.mob.getMainMenuKillCount() == 42) {
               this.sound.playSound(SoundModule.SoundType.RETRO_NOTE_2);
            }

            if (this.mob.getMainMenuKillCount() == 43) {
               this.sound.playSound(SoundModule.SoundType.RETRO_NOTE_3);
            }

            if (this.mob.getMainMenuKillCount() == 44) {
               this.sound.playSound(SoundModule.SoundType.RETRO_NOTE_4);
            }

            if (this.mob.getMainMenuKillCount() == 45) {
               this.sound.playSound(SoundModule.SoundType.RETRO_NOTE_5);
            }

            if (this.mob.getMainMenuKillCount() == 46) {
               this.sound.playSound(SoundModule.SoundType.RETRO_NOTE_6);
            }

            if (this.mob.getMainMenuKillCount() == 47) {
               this.sound.playSound(SoundModule.SoundType.RETRO_NOTE_7);
            }

            if (this.mob.getMainMenuKillCount() == 48) {
               this.sound.playSound(SoundModule.SoundType.RETRO_NOTE_8);
            }

            if (this.mob.getMainMenuKillCount() == 49) {
               this.sound.playSound(SoundModule.SoundType.RETRO_NOTE_9);
            }

            if (this.mob.getMainMenuKillCount() % 10 == 0 && this.mob.getMainMenuKillCount() <= 40) {
               this.sound.playSound(SoundModule.SoundType.RETRO_CLICK);

               for (int x = 0; x < 10; x++) {
                  this.particle
                     .newParticle(
                        this.thisMob.getCenterPixelX(true) + Utilities.randomInt(-32, 32),
                        this.thisMob.getCenterPixelY(true) + Utilities.randomInt(-32, 32),
                        ParticleModule.ParticleType.MAGIC_FLOAT_SPECK,
                        ParticleModule.ParticleSet.MAGIC_BLUE
                     );
                  this.particle
                     .newParticle(
                        this.thisMob.getCenterPixelX(true) + Utilities.randomInt(-32, 32),
                        this.thisMob.getCenterPixelY(true) + Utilities.randomInt(-32, 32),
                        ParticleModule.ParticleType.MAGIC_FLOAT_SPECK,
                        ParticleModule.ParticleSet.MAGIC_GREEN
                     );
                  this.particle
                     .newParticle(
                        this.thisMob.getCenterPixelX(true) + Utilities.randomInt(-32, 32),
                        this.thisMob.getCenterPixelY(true) + Utilities.randomInt(-32, 32),
                        ParticleModule.ParticleType.MAGIC_FLOAT_SPECK,
                        ParticleModule.ParticleSet.MAGIC_PURPLE
                     );
                  this.particle
                     .newParticle(
                        this.thisMob.getCenterPixelX(true) + Utilities.randomInt(-32, 32),
                        this.thisMob.getCenterPixelY(true) + Utilities.randomInt(-32, 32),
                        ParticleModule.ParticleType.MAGIC_FLOAT_SPECK,
                        ParticleModule.ParticleSet.MAGIC_YELLOW
                     );
                  this.particle
                     .newParticle(
                        this.thisMob.getCenterPixelX(true) + Utilities.randomInt(-32, 32),
                        this.thisMob.getCenterPixelY(true) + Utilities.randomInt(-32, 32),
                        ParticleModule.ParticleType.MAGIC_FLOAT_SPECK,
                        ParticleModule.ParticleSet.MAGIC_RED
                     );
               }

               for (int x = 0; x < 100 * this.mob.getMainMenuKillCount(); x++) {
                  int randomX = Utilities.randomInt(4096);

                  int randomY;
                  for (randomY = Utilities.randomInt(4096); this.map.isInView(randomX, randomY, 512); randomY = Utilities.randomInt(4096)) {
                     randomX = Utilities.randomInt(4096);
                  }

                  this.particle.newParticleGroup(randomX, randomY, ParticleModule.ParticleGroup.MAGIC_FALL, ParticleModule.ParticleSet.FIRE_YELLOW);
               }
            }
         }

         this.aiStep = 1;
         String mobName = this.thisMob.getFullName();
         if (mobName.length() > 24) {
            mobName = mobName.substring(0, 23);
         }

         MobBase mx = this.thisMob.getLastDamageMob();
         if (mx != null) {
            mx.increaseKills(1, mobName);
         }

         ObjectBase o = this.thisMob.getLastDamageObject();
         if (o != null) {
            o.increaseKills(1, mobName);
         }
      }
   }

   private void decay() throws SlickException {
      if (this.thisMobFlags.getAllowBlighted()
         && this.thisMob.isBlighted()
         && this.thisMobFlags.getBlightSpawnType() != null
         && Utilities.randomInt(10000) == 0) {
         this.sound.playSound(SoundModule.SoundType.RESURRECT_NEGATIVE);
         this.mob.spawnMob(this.thisMob, this.thisMobFlags.getBlightSpawnType(), true);
         this.goal.incrementGoal(GoalModule.GoalType.TWENTY_EIGHT_SECONDS_LATER);
         Text.setVariableText("mobName", this.thisMob.getFormattedName(false));
         Text.setVariableText("mobType", this.thisMobFlags.getBlightSpawnType().getName());
         Console.newBanner(Text.getText("console.banner.mobAI.turnedIntoZombie.header"), Text.getText("console.banner.mobAI.turnedIntoZombie.subtitle"));
      } else {
         if (this.thisMob.getDecayTick() < 1000) {
            this.thisMob.setPositionDeath();
         } else if (this.thisMob.getDecayTick() >= 1000) {
            this.thisMob.setDecaying(true);
            this.thisMob.setPositionDecay();
         }

         if (this.thisMob.getDecayTick() >= 1900 && this.thisMob.getDecayTick() % 20000 == 0) {
            this.thisMob.decay();
         }

         if (this.thisMob.getDecayTick() >= 200000) {
            this.thisMob.unassignFromAll(false);
            this.mob.deleteMob(this.thisMob);
         }
      }
   }

   private void randomDeathMessage() throws SlickException {
      String randomSubtitle = this.mob.getRandomDeathMessage(this.thisMob.getGender());
      String deathReason = "";
      String title = "";
      if (this.thisMob.getMobGroup() != MobBase.MobGroup.VILLAGER) {
         Text.setVariableText("mobType", this.thisMob.getMobType().getName());
         title = Text.getText("console.banner.mobAI.death.title");
      }

      Text.setVariableText("mobName", this.thisMob.getFormattedName(true));
      Text.setVariableText("mobTitle", title);
      if (this.thisMob.getLastDamageCause() == MobBase.DamageCause.COLD
         || this.thisMob.getLastDamageCause() == MobBase.DamageCause.ICE
         || this.thisMob.getLastDamageCause() == MobBase.DamageCause.FROZEN) {
         deathReason = Text.getText("console.banner.mobAI.death.header.frozen");
      } else if (this.thisMob.getLastDamageCause() == MobBase.DamageCause.COMBAT) {
         MobBase m = this.thisMob.getLastDamageMob();
         ObjectBase o = this.thisMob.getLastDamageObject();
         if (m != null) {
            Text.setVariableText("killerMob", m.getFormattedName(true));
            deathReason = Text.getText("console.banner.mobAI.death.header.byMob");
         } else if (o != null) {
            Text.setVariableText("buildingName", o.getFormattedName());
            deathReason = Text.getText("console.banner.mobAI.death.header.byBuilding");
         } else {
            deathReason = Text.getText("console.banner.mobAI.death.header.byUnknown");
         }
      } else if (this.thisMob.getLastDamageCause() == MobBase.DamageCause.CRUSHING) {
         deathReason = Text.getText("console.banner.mobAI.death.header.crushing");
      } else if (this.thisMob.getLastDamageCause() == MobBase.DamageCause.BEING_SLAUGHTERED) {
         deathReason = Text.getText("console.banner.mobAI.death.header.beingSlaughtered");
      } else if (this.thisMob.getLastDamageCause() == MobBase.DamageCause.DEHYDRATION) {
         deathReason = Text.getText("console.banner.mobAI.death.header.dehydration");
      } else if (this.thisMob.getLastDamageCause() == MobBase.DamageCause.BLIGHT || this.thisMob.getLastDamageCause() == MobBase.DamageCause.FROM_BLIGHT) {
         deathReason = Text.getText("console.banner.mobAI.death.header.blight");
      } else if (this.thisMob.getLastDamageCause() == MobBase.DamageCause.DROPPED) {
         deathReason = Text.getText("console.banner.mobAI.death.header.dropped");
      } else if (this.thisMob.getLastDamageCause() == MobBase.DamageCause.FIRE || this.thisMob.getLastDamageCause() == MobBase.DamageCause.ON_FIRE) {
         deathReason = Text.getText("console.banner.mobAI.death.header.fire");
      } else if (this.thisMob.getLastDamageCause() == MobBase.DamageCause.GOD) {
         deathReason = Text.getText("console.banner.mobAI.death.header.god");
      } else if (this.thisMob.getLastDamageCause() == MobBase.DamageCause.HEAT) {
         deathReason = Text.getText("console.banner.mobAI.death.header.heat");
      } else if (this.thisMob.getLastDamageCause() == MobBase.DamageCause.LIGHTNING) {
         deathReason = Text.getText("console.banner.mobAI.death.header.lightning");
      } else if (this.thisMob.getLastDamageCause() == MobBase.DamageCause.MAGIC) {
         deathReason = Text.getText("console.banner.mobAI.death.header.magic");
      } else if (this.thisMob.getLastDamageCause() == MobBase.DamageCause.NONE) {
         deathReason = Text.getText("console.banner.mobAI.death.header.unknown");
      } else if (this.thisMob.getLastDamageCause() == MobBase.DamageCause.PIERCING) {
         deathReason = Text.getText("console.banner.mobAI.death.header.piercing");
      } else if (this.thisMob.getLastDamageCause() == MobBase.DamageCause.POISON || this.thisMob.getLastDamageCause() == MobBase.DamageCause.FROM_POISON) {
         deathReason = Text.getText("console.banner.mobAI.death.header.poisoned");
      } else if (this.thisMob.getLastDamageCause() == MobBase.DamageCause.DYSENTERY || this.thisMob.getLastDamageCause() == MobBase.DamageCause.FROM_DYSENTERY) {
         deathReason = Text.getText("console.banner.mobAI.death.header.dysentery");
      } else if (this.thisMob.getLastDamageCause() == MobBase.DamageCause.STARVATION) {
         deathReason = Text.getText("console.banner.mobAI.death.header.starved");
      } else if (this.thisMob.getLastDamageCause() == MobBase.DamageCause.TOWER) {
         deathReason = Text.getText("console.banner.mobAI.death.header.tower");
      } else if (this.thisMob.getLastDamageCause() == MobBase.DamageCause.EARTHQUAKE) {
         deathReason = Text.getText("console.banner.mobAI.death.header.earthquake");
      } else if (this.thisMob.getLastDamageCause() == MobBase.DamageCause.HAIL) {
         deathReason = Text.getText("console.banner.mobAI.death.header.hail");
      } else if (this.thisMob.getLastDamageCause() == MobBase.DamageCause.METEOR) {
         deathReason = Text.getText("console.banner.mobAI.death.header.meteor");
      } else if (this.thisMob.getLastDamageCause() == MobBase.DamageCause.NATURAL_CAUSES) {
         deathReason = Text.getText("console.banner.mobAI.death.header.age");
         if (this.thisMob.getGender() == MobBase.MobGender.MALE) {
            randomSubtitle = Text.getText("console.banner.mobAI.death.ageSubtitle.male");
         } else if (this.thisMob.getGender() == MobBase.MobGender.FEMALE) {
            randomSubtitle = Text.getText("console.banner.mobAI.death.ageSubtitle.female");
         } else {
            randomSubtitle = Text.getText("console.banner.mobAI.death.ageSubtitle.none");
         }
      } else if (this.thisMob.getLastDamageCause() == MobBase.DamageCause.CHILD_BIRTH) {
         deathReason = Text.getText("console.banner.mobAI.death.header.birth");
      } else if (this.thisMob.getLastDamageCause() == MobBase.DamageCause.COMET) {
         deathReason = Text.getText("console.banner.mobAI.death.header.comet");
      } else if (this.thisMob.getLastDamageCause() == MobBase.DamageCause.TERMINATING_PREGNANCY) {
         deathReason = Text.getText("console.banner.mobAI.death.header.terminatingPregnancy");
      }

      if (this.thisMob.getLastDamageCause() != MobBase.DamageCause.BEING_SLAUGHTERED) {
         Console.newBanner(deathReason, randomSubtitle);
         this.sound.playSound(SoundModule.SoundType.VILLAGER_DEATH);
      }
   }
}
