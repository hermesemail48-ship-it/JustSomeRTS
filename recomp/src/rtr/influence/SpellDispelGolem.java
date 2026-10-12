package rtr.influence;

import java.util.ArrayList;
import java.util.Collections;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.mobs.MobBase;
import rtr.particles.ParticleModule;
import rtr.system.Game;

public class SpellDispelGolem extends SpellBase {
   @Override
   public void config(int x, int y, boolean freeCast) {
      this.type = SpellBase.SpellType.DISPEL_GOLEM;
      this.name = Text.getText("spell.dispelGolem.name");
      this.description = Text.getText("spell.dispelGolem.description");
      this.castCost = 0;
      this.mobTypesList = new ArrayList<>();
      this.mobTypesList.add(MobBase.MobType.HOLY_GOLEM);
      this.mobTypesList.add(MobBase.MobType.LABOR_GOLEM);
      this.targetX = x;
      this.targetY = y;
      this.tileX = x / 16;
      this.tileY = y / 16;
   }

   @Override
   public void update() throws SlickException {
      MobBase mobSelection = this.mob.getMobAt(Game.getCS().getMouseRectangle(), false);
      if (mobSelection != null && mobSelection.getMobGroup() == MobBase.MobGroup.GOLEM) {
         mobSelection.setHitPoints(0);
         this.goal.incrementGoal(GoalModule.GoalType.IM_DONE_PLAYING_WITH_YOU);
         this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.CASTING_SPELLS);
         this.projectile.newExpandingFireRing(this.targetX, this.targetY, 16, 2.0F, ParticleModule.ParticleSet.FIRE_GREEN);
         this.sound.playSound(SoundModule.SoundType.MAGIC_RECALL, mobSelection.getMobX(), mobSelection.getMobY());
         this.mob.tryPanicInArea(this.tileX, this.tileY, 2, true, 1);
         this.influence.removeSpell(this);
      } else {
         ArrayList<MobBase> mobsNearby = this.mob.getMobsAt(this.tileX, this.tileY);
         if (mobsNearby.size() > 0) {
            Collections.shuffle(mobsNearby);

            for (MobBase m : mobsNearby) {
               if (m.getMobGroup() == MobBase.MobGroup.GOLEM) {
                  this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.CASTING_SPELLS);
                  m.setHitPoints(0);
                  this.goal.incrementGoal(GoalModule.GoalType.IM_DONE_PLAYING_WITH_YOU);
                  this.projectile.newExpandingFireRing(this.targetX, this.targetY, 16, 2.0F, ParticleModule.ParticleSet.FIRE_GREEN);
                  this.sound.playSound(SoundModule.SoundType.MAGIC_RECALL, m.getMobX(), m.getMobY());
                  this.mob.tryPanicInArea(this.tileX, this.tileY, 2, true, 1);
                  this.influence.removeSpell(this);
                  return;
               }
            }
         }

         this.influence.removeSpell(this);
      }
   }
}
