package rtr.influence;

import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.mobs.MobBase;
import rtr.particles.ParticleModule;

public class SpellSummonLaborGolem extends SpellBase {
   @Override
   public void config(int x, int y, boolean freeCast) {
      this.type = SpellBase.SpellType.SUMMON_LABOR_GOLEM;
      this.name = Text.getText("spell.summonLaborGolem.name");
      this.description = Text.getText("spell.summonLaborGolem.description");
      this.cooldown = 300;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.LABOR_GOLEM_SPELL_COOLDOWN) > 0.0) {
         this.cooldown = (int)(this.cooldown - 60.0 * this.perk.getPerkTypeTotal(PerkModule.PerkType.LABOR_GOLEM_SPELL_COOLDOWN));
      }

      this.castCost = freeCast ? 0 : 300;
      this.maintainCost = 300;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.LABOR_GOLEM_SPELL_COST) > 0.0) {
         this.castCost = (int)(this.castCost - this.castCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.LABOR_GOLEM_SPELL_COST));
         this.maintainCost = (int)(this.maintainCost - this.maintainCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.LABOR_GOLEM_SPELL_COST));
      }

      this.targetX = x;
      this.targetY = y;
      this.targetX = x / 16 * 16 + 8;
      this.targetY = y / 16 * 16 + 8;
      this.tileX = x / 16;
      this.tileY = y / 16;
      this.duration = 200;
   }

   @Override
   public void update() throws SlickException {
      if (this.durationTick % 10 == 0 && this.spellStep > 0) {
         this.mob.tryConfuseInArea(this.tileX, this.tileY, 6, true, 2);
      }

      switch (this.spellStep) {
         case 0:
            this.lighting.addShortTermLight(this.tileX, this.tileY, (byte)8, (byte)6, this.duration);
            this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.CASTING_SPELLS);
            this.influence.decreaseInfluenceAmount(this.castCost);
            this.spellStep++;
            break;
         case 1:
            this.durationTick++;
            this.projectile.newEnergyRing(this.targetX, this.targetY, 55 - this.durationTick / 4, ParticleModule.ParticleSet.MAGIC_PURPLE);
            if (this.durationTick > 200) {
               this.projectile.newEnergyBeam(this.targetX, this.targetY, this.targetX, this.targetY - 500);
               this.sound.playSound(SoundModule.SoundType.MAGIC_ZAP, this.targetX, this.targetY);
               this.durationTick = 0;
               this.spellStep++;
            }
            break;
         case 2:
            this.durationTick++;
            if (this.durationTick % 70 == 0) {
               this.projectile.newEnergyBeam(this.targetX, this.targetY, this.targetX, this.targetY - 500);
               this.sound.playSound(SoundModule.SoundType.MAGIC_ZAP, this.targetX, this.targetY);
            }

            if (this.durationTick % 6 == 0) {
               this.projectile.newEnergyRing(this.targetX, this.targetY, 4, ParticleModule.ParticleSet.MAGIC_PURPLE);
            }

            if (this.durationTick > 200) {
               this.projectile.newEnergyBeam(this.targetX, this.targetY, this.targetX, this.targetY - 500);
               this.sound.playSound(SoundModule.SoundType.MAGIC_ZAP, this.targetX, this.targetY);
               this.durationTick = 0;
               MobBase golem = this.mob.newMob(this.tileX, this.tileY, MobBase.MobType.LABOR_GOLEM, true);
               int golemLevel = 0;
               if (this.perk.getPerkTypeTotal(PerkModule.PerkType.LABOR_GOLEM_LEVEL) > 0.0) {
                  golemLevel = (int)(golemLevel + this.perk.getPerkTypeTotal(PerkModule.PerkType.LABOR_GOLEM_LEVEL));
               }

               golem.setLevel(golem.getLevel() + golemLevel);
               this.goal.incrementGoal(GoalModule.GoalType.GOLEMS_WILL_TAKE_OUR_JOBS);
               this.sound.playSound(SoundModule.SoundType.MAGIC_RECALL, this.targetX, this.targetY);
               this.projectile.newExpandingFireRing(this.targetX, this.targetY, 64, 4.0F, ParticleModule.ParticleSet.FIRE_PURPLE);
               this.mob.influenceFaithInArea(this.tileX, this.tileY, 6, 1, 3, true);
               this.influence.removeSpell(this);
            }
      }
   }
}
