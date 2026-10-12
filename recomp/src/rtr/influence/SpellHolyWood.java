package rtr.influence;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.gui.states.BrushLoader;
import rtr.map.MapData;
import rtr.map.MapTilesLoader;
import rtr.particles.ParticleModule;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class SpellHolyWood extends SpellBase {
   protected ArrayList<OrderedPair> tilesInRangeBase = new ArrayList<>();
   protected ArrayList<OrderedPair> tilesInRangeDead = new ArrayList<>();

   @Override
   public void config(int x, int y, boolean freeCast) {
      this.type = SpellBase.SpellType.HOLY_WOOD;
      this.name = Text.getText("spell.holyWood.name");
      this.description = Text.getText("spell.holyWood.description");
      this.cooldown = 3600;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.HOLY_WOOD_SPELL_COOLDOWN) > 0.0) {
         this.cooldown = (int)(this.cooldown - 60.0 * this.perk.getPerkTypeTotal(PerkModule.PerkType.HOLY_WOOD_SPELL_COOLDOWN));
      }

      this.castCost = freeCast ? 0 : 1500;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.HOLY_WOOD_SPELL_COST) > 0.0) {
         this.castCost = (int)(this.castCost - this.castCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.HOLY_WOOD_SPELL_COST));
      }

      this.targetX = x;
      this.targetY = y;
      this.tileX = x / 16;
      this.tileY = y / 16;
      this.duration = 1500;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.HOLY_WOOD_SPELL_DURATION) > 0.0) {
         this.duration = (int)(this.duration + 60.0 * this.perk.getPerkTypeTotal(PerkModule.PerkType.HOLY_WOOD_SPELL_DURATION));
      }

      this.radius = 6;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.HOLY_WOOD_SPELL_RADIUS) > 0.0) {
         this.radius = (int)(this.radius + this.perk.getPerkTypeTotal(PerkModule.PerkType.HOLY_WOOD_SPELL_RADIUS));
      }
   }

   @Override
   public void update() throws SlickException {
      this.particle
         .newParticleGroup(
            this.targetX + Utilities.randomInt(-2, 2),
            this.targetY + Utilities.randomInt(-2, 2),
            ParticleModule.ParticleGroup.FIRE,
            ParticleModule.ParticleSet.FIRE_BLUE
         );
      this.particle.newParticleGroup(this.targetX, this.targetY, ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS, ParticleModule.ParticleSet.MAGIC_GREEN);
      switch (this.spellStep) {
         case 0:
            this.lighting.addShortTermLight(this.tileX, this.tileY, (byte)this.radius, (byte)3, this.duration);
            this.sound.playSound(SoundModule.SoundType.MOTIVATE_LAND_SPELL, this.targetX, this.targetY);
            this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.CASTING_SPELLS);
            this.influence.decreaseInfluenceAmount(this.castCost);
            this.spellStep++;
            break;
         case 1:
            this.tilesInRangeBase = Utilities.getAllCoordinatesWithin(this.tileX, this.tileY, this.radius - 2);
            this.tilesInRangeDead = Utilities.getAllCoordinatesWithin(this.tileX, this.tileY, this.radius);

            for (OrderedPair o : Utilities.getAllCoordinatesWithin(this.tileX, this.tileY, this.radius)) {
               if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[o.getX()][o.getY()] > 0) {
                  this.tilesInRangeBase.remove(o);
                  this.tilesInRangeDead.remove(o);
               }
            }

            this.spellStep++;
            break;
         case 2:
            this.durationTick++;
            if (this.durationTick > this.duration) {
               this.spellStep++;
            }

            if (this.durationTick == 100) {
               this.sound.playSound(SoundModule.SoundType.MAGIC_HEAL_SPELL, this.targetX, this.targetY);
            }

            if (this.durationTick == 250) {
               for (int x = 0; x < 20; x++) {
                  this.projectile.newEnergyNova(this.targetX, this.targetY, ParticleModule.ParticleSet.FIRE_GREEN);
               }

               this.projectile.newExpandingEnergyRing(this.targetX, this.targetY, this.radius * 16, 1.0F, ParticleModule.ParticleSet.FIRE_GREEN);
               this.projectile.newExpandingFireRing(this.targetX, this.targetY, this.radius * 16, 0.5F, ParticleModule.ParticleSet.FIRE_GREEN);
            }

            if (this.durationTick > 250) {
               if (this.durationTick % 10 == 0) {
                  this.mob.tryConfuseInArea(this.tileX, this.tileY, this.radius, true, 2);
               }

               if (this.durationTick % 10 == 0) {
                  if (Utilities.randomInt(2) == 0) {
                     this.sound.playSound(SoundModule.SoundType.STORE_ITEM, this.targetX, this.targetY);
                  } else {
                     this.sound.playSound(SoundModule.SoundType.EARTHQUAKE_ROCKS, this.targetX, this.targetY);
                  }

                  if (this.durationTick < this.duration / 2 && this.tilesInRangeDead.size() > 0) {
                     OrderedPair o = this.tilesInRangeDead.get(Utilities.randomInt(this.tilesInRangeDead.size()));
                     this.lighting.addShortTermLight(o.getX(), o.getY(), (byte)5, (byte)4, this.duration);
                     this.map.drawToMap(o.getX(), o.getY(), MapTilesLoader.TileSet.TREES_LAVENDER_DEAD, 1, BrushLoader.BrushType.ROUND);
                     this.mob.tryConfuseInArea(o.getX(), o.getY(), 4, true, 2);
                     this.mob.tryPanicInArea(o.getX(), o.getY(), 2, true, 2);
                     this.mob.influenceFaithInArea(o.getX(), o.getY(), 4, 1, true);
                     this.tilesInRangeDead.remove(o);
                  } else if (this.tilesInRangeBase.size() > 0) {
                     OrderedPair o = this.tilesInRangeBase.get(Utilities.randomInt(this.tilesInRangeBase.size()));
                     this.lighting.addShortTermLight(o.getX(), o.getY(), (byte)5, (byte)4, this.duration);
                     this.map.drawToMap(o.getX(), o.getY(), MapTilesLoader.TileSet.TREES_LAVENDER, 1, BrushLoader.BrushType.ROUND);
                     this.mob.tryConfuseInArea(o.getX(), o.getY(), 4, true, 2);
                     this.mob.tryPanicInArea(o.getX(), o.getY(), 2, true, 2);
                     this.mob.influenceFaithInArea(o.getX(), o.getY(), 4, 1, true);
                     this.tilesInRangeDead.remove(o);
                  }
               }
            }
            break;
         case 3:
            this.influence.removeSpell(this);
      }
   }
}
