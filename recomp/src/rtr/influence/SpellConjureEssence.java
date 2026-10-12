package rtr.influence;

import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class SpellConjureEssence extends SpellBase {
   @Override
   public void config(int x, int y, boolean freeCast) {
      this.type = SpellBase.SpellType.CONJURE_ESSENCE;
      this.name = Text.getText("spell.conjureEssence.name");
      this.description = Text.getText("spell.conjureEssence.description");
      this.castCost = freeCast ? 0 : 20;
      this.allowContinuousCast = true;
      this.radius = 4;
      this.targetX = x;
      this.targetY = y;
      this.tileX = x / 16;
      this.tileY = y / 16;
   }

   @Override
   public void update() throws SlickException {
      this.lighting.addShortTermLight(this.tileX, this.tileY, (byte)4, (byte)12, 50);
      this.projectile.newExpandingFireRing(this.targetX, this.targetY, 16, 2.0F, ParticleModule.ParticleSet.FIRE_GREEN);
      this.sound.playSound(SoundModule.SoundType.MAGIC_ZAP, this.targetX, this.targetY);
      this.essence.newEssence(this.targetX, this.targetY, 4 + Utilities.randomInt(2), 2);
      this.mob.tryPanicInArea(this.tileX, this.tileY, this.radius, true, 6);
      this.mob.tryConfuseInArea(this.tileX, this.tileY, this.radius, true, 4);
      if (Utilities.randomInt(100) == 0) {
         this.mob.influenceFaithInArea(this.tileX, this.tileY, this.radius, 1, true);
      }

      this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.CASTING_SPELLS);
      this.influence.decreaseInfluenceAmount(this.castCost);
      this.influence.removeSpell(this);
   }
}
