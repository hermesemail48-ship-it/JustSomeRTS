package rtr.influence;

import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.system.Game;

public class SpellDispelGodStructure extends SpellBase {
   @Override
   public void config(int x, int y, boolean freeCast) {
      this.type = SpellBase.SpellType.DISPEL_GOD_STRUCTURE;
      this.name = Text.getText("spell.dispelGodStructure.name");
      this.description = Text.getText("spell.dispelGodStructure.description");
      this.castCost = 0;
      this.allowContinuousCast = true;
      this.targetX = x;
      this.targetY = y;
      this.tileX = x / 16;
      this.tileY = y / 16;
   }

   @Override
   public void update() throws SlickException {
      ObjectBase o = this.object.getBuildingAtLocation(this.tileX, this.tileY);
      if (o != null) {
         this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.CASTING_SPELLS);
         this.sound.playSound(SoundModule.SoundType.MAGIC_RECALL, this.targetX, this.targetY);
         this.object.flagObjectForDestructionAndRemoval(o);
         this.projectile.newExpandingFireRing(this.targetX, this.targetY, 16, 2.0F, ParticleModule.ParticleSet.FIRE_GREEN);
         this.goal.incrementGoal(GoalModule.GoalType.IM_DONE_PLAYING_WITH_YOU);
         this.mob.tryConfuseInArea(this.tileX, this.tileY, this.radius, true, 2);
         this.mob.influenceFaithInArea(this.tileX, this.tileY, this.radius, 1, 3, false);
      }

      this.influence.removeSpell(this);
   }

   @Override
   public boolean canCast() throws SlickException {
      if (!super.canCast()) {
         return false;
      } else {
         ObjectBase o = this.object.getBuildingAtLocation(Game.getCS().getMouseTileX(), Game.getCS().getMouseTileY());
         return o != null && o.getObjectFlags().getInfluenceMaintainCost() != 0;
      }
   }
}
