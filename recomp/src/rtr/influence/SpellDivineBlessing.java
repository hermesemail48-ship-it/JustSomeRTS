package rtr.influence;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.mobs.MobBase;
import rtr.particles.ParticleModule;
import rtr.system.Game;
import rtr.utilities.Utilities;

public class SpellDivineBlessing extends SpellBase {
   @Override
   public void config(int x, int y, boolean freeCast) {
      this.type = SpellBase.SpellType.DIVINE_BLESSING;
      this.name = Text.getText("spell.divineBlessing.name");
      this.description = Text.getText("spell.divineBlessing.description");
      this.cooldown = 600;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.DIVINE_BLESSING_SPELL_COOLDOWN) > 0.0) {
         this.cooldown = (int)(this.cooldown - 60.0 * this.perk.getPerkTypeTotal(PerkModule.PerkType.DIVINE_BLESSING_SPELL_COOLDOWN));
      }

      this.castCost = freeCast ? 0 : 1500;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.DIVINE_BLESSING_SPELL_COST) > 0.0) {
         this.castCost = (int)(this.castCost - this.castCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.DIVINE_BLESSING_SPELL_COST));
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
            ArrayList<MobBase> unblessedVillagers = new ArrayList<>();

            for (MobBase m : nearbyMobsInRange) {
               if (m.getMobFlags().canBeBless() && !m.isSocialBlessed() && m.isAvailable()) {
                  unblessedVillagers.add(m);
               }
            }

            if (unblessedVillagers.size() > 0) {
               MobBase unblessedVillager = unblessedVillagers.get(Utilities.randomInt(unblessedVillagers.size()));
               this.sound.playSound(SoundModule.SoundType.RESURRECT);
               this.sound.playSound(SoundModule.SoundType.FULL_MOON);
               this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.CASTING_SPELLS);
               this.influence.decreaseInfluenceAmount(this.castCost);
               unblessedVillager.setSocialBlessed(true);
               if (unblessedVillager.getGender() == MobBase.MobGender.FEMALE) {
                  this.goal.incrementGoal(GoalModule.GoalType.GOD_OF_LOVE_AND_THUNDER);
               }

               for (int x = 0; x < 5; x++) {
                  this.projectile
                     .newEnergyBeam(
                        unblessedVillager.getCenterPixelX(true),
                        unblessedVillager.getCenterPixelY(true),
                        unblessedVillager.getCenterPixelX(true) + Utilities.randomInt(-8, 8),
                        unblessedVillager.getCenterPixelY(true) - 500
                     );
               }

               for (int x = 0; x < 20; x++) {
                  this.projectile
                     .newEnergyNova(unblessedVillager.getCenterPixelX(true), unblessedVillager.getCenterPixelY(true), ParticleModule.ParticleSet.FIRE_PURPLE);
               }

               for (int x = 0; x < 10; x++) {
                  this.projectile
                     .newEnergyRing(
                        unblessedVillager.getCenterPixelX(true), unblessedVillager.getCenterPixelY(true), 16, ParticleModule.ParticleSet.MAGIC_PURPLE
                     );
               }

               for (int x = 0; x < 40; x++) {
                  this.particle
                     .newParticleGroup(
                        unblessedVillager.getCenterPixelX(true) + Utilities.randomInt(-8, 8),
                        unblessedVillager.getCenterPixelY(true) + Utilities.randomInt(-8, 8),
                        ParticleModule.ParticleGroup.SMOKE_BELLOW,
                        ParticleModule.ParticleSet.MAGIC_BLUE
                     );
               }

               this.mob.tryPanicInArea(this.tileX, this.tileY, 8, true, 1);
               this.mob.tryConfuseInArea(this.tileX, this.tileY, 8, true, 1);
               this.mob.influenceFaithInArea(this.tileX, this.tileY, 8, 3, 5, true);
               if (unblessedVillager.getMobFlags().getAllowFaith()) {
                  unblessedVillager.increaseFaith(Utilities.randomInt(6) + 1, true);
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
         ArrayList<MobBase> unblessedVillagers = new ArrayList<>();

         for (MobBase m : nearbyMobsInRange) {
            if (m.getMobFlags().canBeBless() && !m.isSocialBlessed() && m.isAvailable()) {
               unblessedVillagers.add(m);
            }
         }

         return unblessedVillagers.size() > 0;
      }
   }
}
