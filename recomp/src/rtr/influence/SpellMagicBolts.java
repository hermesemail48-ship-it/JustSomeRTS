package rtr.influence;

import java.util.ArrayList;
import java.util.Collections;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;

public class SpellMagicBolts extends SpellBase {
   @Override
   public void config(int x, int y, boolean freeCast) {
      this.type = SpellBase.SpellType.MAGIC_BOLTS;
      this.name = Text.getText("spell.magicBolts.name");
      this.description = Text.getText("spell.magicBolts.description");
      this.cooldown = 20;
      this.castCost = freeCast ? 0 : 100;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.MAGIC_BOLTS_SPELL_COST) > 0.0) {
         this.castCost = (int)(this.castCost - this.castCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.MAGIC_BOLTS_SPELL_COST));
      }

      this.targetX = x;
      this.targetY = y;
      this.tileX = x / 16;
      this.tileY = y / 16;
   }

   @Override
   public void update() throws SlickException {
      int count = 0;
      ArrayList<MobBase> monsters = this.mob.getMobsNearby(this.tileX, this.tileY, 8, MobBase.MobGroup.MONSTER);
      Collections.shuffle(monsters);
      int countMax = 3;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.MAGIC_BOLTS_SPELL_COUNT) > 0.0) {
         countMax = (int)(countMax + this.perk.getPerkTypeTotal(PerkModule.PerkType.MAGIC_BOLTS_SPELL_COUNT));
      }

      for (MobBase m : monsters) {
         if (!m.isDead() && Math.abs(m.getTileX() - this.tileX) < 8 && Math.abs(m.getTileY() - this.tileY) < 8) {
            this.missile.newMissile(this.targetX, this.targetY, m, MissileModule.MissileType.SMALL_ELEMENTAL_BOLT);
            count++;
         }

         if (count >= countMax) {
            break;
         }
      }

      if (count > 0) {
         this.sound.playSound(SoundModule.SoundType.FLAME_FIRE, this.targetX, this.targetY);
         this.mob.tryConfuseInArea(this.tileX, this.tileY, 4, true, 1);
         this.mob.tryPanicInArea(this.tileX, this.tileY, 4, true, 2);
         this.mob.influenceFaithInArea(this.tileX, this.tileY, 4, count, true);
         this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.CASTING_SPELLS);
         this.influence.decreaseInfluenceAmount(this.castCost);
      }

      this.influence.removeSpell(this);
   }
}
