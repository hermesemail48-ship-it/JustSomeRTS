package rtr.influence;

import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.system.Game;

public class SpellGodWall extends SpellBase {
   @Override
   public void config(int x, int y, boolean freeCast) {
      this.type = SpellBase.SpellType.GOD_WALL;
      this.name = Text.getText("spell.godWall.name");
      this.description = Text.getText("spell.godWall.description");
      this.castCost = freeCast ? 0 : 150;
      this.maintainCost = 150;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.GOD_WALL_SPELL_COST) > 0.0) {
         this.castCost = (int)(this.castCost - this.castCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.GOD_WALL_SPELL_COST));
         this.maintainCost = (int)(this.maintainCost - this.maintainCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.GOD_WALL_SPELL_COST));
      }

      this.placementObject = MapTilesLoader.TileSet.GOD_WALL;
      this.allowContinuousCast = true;
      this.targetX = x;
      this.targetY = y;
      this.tileX = x / 16;
      this.tileY = y / 16;
   }

   @Override
   public void update() throws SlickException {
      if (this.map.canPlaceObject(this.tileX, this.tileY, this.placementObject, true)) {
         this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.CASTING_SPELLS);
         this.projectile.newExpandingFireRing(this.targetX, this.targetY, 16, 2.0F, ParticleModule.ParticleSet.FIRE_GREEN);
         this.map.placeObject(this.tileX, this.tileY, this.placementObject, ObjectBase.ObjectSubType.BUILT, true);
         this.influence.decreaseInfluenceAmount(this.castCost);
         this.mob.tryPanicInArea(this.tileX, this.tileY, 2, true, 1);
         this.mob.tryConfuseInArea(this.tileX, this.tileY, 4, true, 1);
         this.mob.influenceFaithInArea(this.tileX, this.tileY, 4, 1, true);
      }

      this.influence.removeSpell(this);
   }

   @Override
   public boolean canCast() throws SlickException {
      return !super.canCast() ? false : this.map.canPlaceObject(Game.getCS().getMouseTileX(), Game.getCS().getMouseTileY(), this.placementObject, true);
   }
}
