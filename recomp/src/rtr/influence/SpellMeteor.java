package rtr.influence;

import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.PerkModule;
import rtr.font.Text;
import rtr.particles.ParticleModule;

public class SpellMeteor extends SpellBase {
   @Override
   public void config(int x, int y, boolean freeCast) {
      this.type = SpellBase.SpellType.METEOR;
      this.name = Text.getText("spell.meteor.name");
      this.description = Text.getText("spell.meteor.description");
      this.cooldown = 30;
      this.castCost = freeCast ? 0 : 200;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.METEOR_SPELL_COST) > 0.0) {
         this.castCost = (int)(this.castCost - this.castCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.METEOR_SPELL_COST));
      }

      this.radius = 4;
      this.targetX = x;
      this.targetY = y;
      this.tileX = x / 16;
      this.tileY = y / 16;
   }

   @Override
   public void update() throws SlickException {
      switch (this.spellStep) {
         case 0:
            this.lighting.addShortTermLight(this.tileX, this.tileY, (byte)5, (byte)5, 100);
            this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.CASTING_SPELLS);
            this.influence.decreaseInfluenceAmount(this.castCost);
            this.spellStep++;
            break;
         case 1:
            int amount = 1;
            if (this.perk.getPerkTypeTotal(PerkModule.PerkType.METEOR_SPELL_AMOUNT) > 0.0) {
               amount = (int)(amount + this.perk.getPerkTypeTotal(PerkModule.PerkType.METEOR_SPELL_AMOUNT));
            }

            for (int i = 0; i < amount; i++) {
               this.weather.newMeteor(this.targetX, this.targetY, true);
            }

            this.projectile.newExpandingFireRing(this.targetX, this.targetY, 32, 2.0F, ParticleModule.ParticleSet.MAGIC_BLUE);
            this.influence.removeSpell(this);
      }
   }
}
