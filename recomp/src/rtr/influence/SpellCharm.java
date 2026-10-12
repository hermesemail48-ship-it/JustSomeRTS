package rtr.influence;

import java.util.ArrayList;
import java.util.Collections;
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

public class SpellCharm extends SpellBase {
   @Override
   public void config(int x, int y, boolean freeCast) {
      this.type = SpellBase.SpellType.CHARM;
      this.name = Text.getText("spell.charm.name");
      this.description = Text.getText("spell.charm.description");
      this.cooldown = 1020;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.CHARM_SPELL_COOLDOWN) > 0.0) {
         this.cooldown = (int)(this.cooldown - 60.0 * this.perk.getPerkTypeTotal(PerkModule.PerkType.CHARM_SPELL_COOLDOWN));
      }

      this.castCost = freeCast ? 0 : 750;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.CHARM_SPELL_COST) > 0.0) {
         this.castCost = (int)(this.castCost - this.castCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.CHARM_SPELL_COST));
      }

      this.targetX = x;
      this.targetY = y;
      this.tileX = x / 16;
      this.tileY = y / 16;
   }

   @Override
   public void update() throws SlickException {
      ArrayList<MobBase> mobList = this.mob.getMobsNearby(this.tileX, this.tileY, 2);
      Collections.shuffle(mobList);

      for (MobBase m : mobList) {
         if (m.getMobGroup() == MobBase.MobGroup.MONSTER && m.isAvailable()) {
            if (!m.isBoosted()) {
               m.setMobGroup(MobBase.MobGroup.CHARMED_MONSTER);
               m.getHostileMobGroups().clear();
               m.getHostileMobGroups().add(MobBase.MobGroup.MONSTER);
               m.getFriendlyMobGroups().clear();
               m.unassignFromAll(true);
               m.interruptNode(m.getAIStart());
               this.goal.incrementGoal(GoalModule.GoalType.CHARMING_DEITY);

               for (int i = 0; i < 2; i++) {
                  this.font
                     .newFloater("iconHitPoints", m.getCenterPixelX(true) + Utilities.randomInt(-6, 6), m.getCenterPixelY(true) + Utilities.randomInt(-6, 6));
                  this.font
                     .newFloater("iconChatFlirting", m.getCenterPixelX(true) + Utilities.randomInt(-6, 6), m.getCenterPixelY(true) + Utilities.randomInt(-6, 6));
               }

               for (int i = 0; i < 150; i++) {
                  this.particle
                     .newParticle(
                        m.getCenterPixelX(true) + Utilities.randomInt(-24, 24),
                        m.getCenterPixelY(true) + Utilities.randomInt(-24, 24),
                        ParticleModule.ParticleType.MAGIC_SMOKE_SPECK,
                        ParticleModule.ParticleSet.MAGIC_GREEN
                     );
               }

               this.lighting.addShortTermLight(this.tileX, this.tileY, (byte)4, (byte)12, this.duration + 50);
               this.projectile.newExpandingEnergyRing(this.targetX, this.targetY, 32, 1.0F, ParticleModule.ParticleSet.FIRE_BLUE);
               this.projectile.newConstrictingFireRing(this.targetX, this.targetY, 32, 1.0F, ParticleModule.ParticleSet.FIRE_BLUE);
               this.sound.playSound(SoundModule.SoundType.HOLY_SOFT, this.targetX, this.targetY);
            } else {
               this.font.newFloater("iconConfused", m.getCenterPixelX(true) + Utilities.randomInt(-6, 6), m.getCenterPixelY(true) + Utilities.randomInt(-6, 6));
               this.lighting.addShortTermLight(this.tileX, this.tileY, (byte)4, (byte)12, this.duration + 50);
               this.projectile.newExpandingEnergyRing(this.targetX, this.targetY, 32, 1.0F, ParticleModule.ParticleSet.FIRE_RED);
               this.projectile.newConstrictingFireRing(this.targetX, this.targetY, 32, 1.0F, ParticleModule.ParticleSet.FIRE_RED);
               this.sound.playSound(SoundModule.SoundType.MAGIC_ZAP, this.targetX, this.targetY);
            }

            this.mob.tryConfuseInArea(this.tileX, this.tileY, 8, true, 1);
            this.mob.influenceFaithInArea(this.tileX, this.tileY, 4, 1, 5, true);
            this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.CASTING_SPELLS);
            this.influence.decreaseInfluenceAmount(this.castCost);
            this.influence.removeSpell(this);
            return;
         }
      }

      this.influence.removeSpell(this);
   }

   @Override
   public boolean canCast() throws SlickException {
      if (!super.canCast()) {
         return false;
      } else {
         for (MobBase m : this.mob.getMobsNearby(Game.getCS().getMouseTileX(), Game.getCS().getMouseTileY(), 2)) {
            if (m.getMobGroup() == MobBase.MobGroup.MONSTER && m.isAvailable() && !m.isBoosted()) {
               return true;
            }
         }

         return false;
      }
   }
}
