package rtr.influence;

import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class SpellLightningBolt extends SpellBase {
   @Override
   public void config(int x, int y, boolean freeCast) {
      this.type = SpellBase.SpellType.LIGHTNING_BOLT;
      this.name = Text.getText("spell.lightningBolt.name");
      this.description = Text.getText("spell.lightningBolt.description");
      this.cooldown = 20;
      this.castCost = freeCast ? 0 : 100;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.LIGHTNING_BOLT_SPELL_COST) > 0.0) {
         this.castCost = (int)(this.castCost - this.castCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.LIGHTNING_BOLT_SPELL_COST));
      }

      this.allowContinuousCast = true;
      this.hideCooldownWheel = true;
      this.targetX = x;
      this.targetY = y;
      this.tileX = x / 16;
      this.tileY = y / 16;
   }

   @Override
   public void update() throws SlickException {
      this.durationTick++;
      switch (this.spellStep) {
         case 0:
            this.projectile.newConstrictingFireRing(this.targetX, this.targetY, 32, 1.0F, ParticleModule.ParticleSet.MAGIC_BLUE);
            this.sound.playSound(SoundModule.SoundType.MAGIC_ZAP, this.targetX, this.targetY);
            this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.CASTING_SPELLS);
            this.influence.decreaseInfluenceAmount(this.castCost);
            this.spellStep++;
            break;
         case 1:
            if (this.durationTick > 40) {
               this.spellStep++;
            }
            break;
         case 2:
            int length = Utilities.randomInt(250) + 200;
            this.weather.newLightning(this.tileX, this.tileY, length, true, ParticleModule.ParticleSet.MAGIC_BLUE);
            this.influence.removeSpell(this);
            this.spellStep++;
      }
   }
}
