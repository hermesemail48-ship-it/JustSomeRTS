package rtr.influence;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.map.RegrowTile;
import rtr.map.SpreadTile;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class SpellFlame extends SpellBase {
   protected ArrayList<RegrowTile> regrowTilesInRange = new ArrayList<>();
   protected ArrayList<SpreadTile> spreadTilesInRange = new ArrayList<>();

   @Override
   public void config(int x, int y, boolean freeCast) {
      this.type = SpellBase.SpellType.FLAME;
      this.name = Text.getText("spell.flame.name");
      this.description = Text.getText("spell.flame.description");
      this.castCost = freeCast ? 0 : 5;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.FLAME_SPELL_COST) > 0.0) {
         this.castCost = (int)(this.castCost - this.castCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.FLAME_SPELL_COST));
      }

      this.allowContinuousCast = true;
      this.targetX = x;
      this.targetY = y;
      this.tileX = x / 16;
      this.tileY = y / 16;
      this.duration = 30;
      this.radius = 3;
   }

   @Override
   public void update() throws SlickException {
      this.particle
         .newParticle(
            this.targetX + Utilities.randomInt(-2, 2),
            this.targetY + Utilities.randomInt(-2, 2),
            ParticleModule.ParticleType.FIRE_SPECK,
            ParticleModule.ParticleSet.FIRE_RED
         );
      switch (this.spellStep) {
         case 0:
            for (int x = 0; x < 10; x++) {
               this.projectile.newSmallBlast(this.targetX, this.targetY, ParticleModule.ParticleSet.FIRE_RED);
            }

            this.lighting.addShortTermLight(this.tileX, this.tileY, (byte)15, (byte)5, 10);
            this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.CASTING_SPELLS);
            this.influence.decreaseInfluenceAmount(this.castCost);
            this.weather.addDisasterTemp((Utilities.randomInt(5) + 10.0) / 1600.0);
            this.spellStep++;
            break;
         case 1:
            if (this.durationTick % 10 == 0) {
               this.particle
                  .newParticle(
                     this.targetX + Utilities.randomInt(-16, 16),
                     this.targetY + Utilities.randomInt(-16, 16),
                     ParticleModule.ParticleType.FIRE_SPECK,
                     ParticleModule.ParticleSet.FIRE_RED
                  );
            }

            for (MobBase m : this.mob.getMobsNearby(this.tileX, this.tileY, this.radius)) {
               if (Utilities.randomInt(10) == 1) {
                  m.takeDamage(1, this.targetX, this.targetY, MobBase.DamageType.MAGIC_FIRE, MobBase.DamageCause.GOD);
               }
            }

            for (ObjectBase o : this.object.getObjectsNearby(this.tileX, this.tileY, this.radius)) {
               if (Utilities.randomInt(10) == 1) {
                  o.takeDamage(1, MobBase.DamageType.MAGIC_FIRE, MobBase.DamageCause.GOD);
               }
            }

            this.sound.playSound(SoundModule.SoundType.FLAME_FIRE, this.targetX, this.targetY);
            this.durationTick++;
            if (this.durationTick > this.duration) {
               if (Utilities.randomInt(10) == 0) {
                  this.mob.influenceFaithInArea(this.tileX, this.tileY, 5, 1, 2, false);
               }

               this.mob.tryPanicInArea(this.tileX, this.tileY, 5, true, 1);
               this.mob.tryConfuseInArea(this.tileX, this.tileY, 8, true, 1);
               this.influence.removeSpell(this);
            }
      }
   }
}
