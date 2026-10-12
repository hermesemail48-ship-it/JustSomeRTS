package rtr.influence;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.map.MapData;
import rtr.particles.ParticleModule;
import rtr.resources.ResourceBase;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class SpellHarvest extends SpellBase {
   ArrayList<OrderedPair> coordinatesNearby;

   @Override
   public void config(int x, int y, boolean freeCast) {
      this.type = SpellBase.SpellType.HARVEST;
      this.name = Text.getText("spell.harvest.name");
      this.description = Text.getText("spell.harvest.description");
      this.cooldown = 2100;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.HARVEST_SPELL_COOLDOWN) > 0.0) {
         this.cooldown = (int)(this.cooldown - 60.0 * this.perk.getPerkTypeTotal(PerkModule.PerkType.HARVEST_SPELL_COOLDOWN));
      }

      this.castCost = freeCast ? 0 : 1000;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.HARVEST_SPELL_COST) > 0.0) {
         this.castCost = (int)(this.castCost - this.castCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.HARVEST_SPELL_COST));
      }

      this.targetX = x;
      this.targetY = y;
      this.tileX = x / 16;
      this.tileY = y / 16;
      this.duration = 1020;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.HARVEST_SPELL_DURATION) > 0.0) {
         this.duration = (int)(this.duration + 60.0 * this.perk.getPerkTypeTotal(PerkModule.PerkType.HARVEST_SPELL_DURATION));
      }

      this.radius = 8;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.HARVEST_SPELL_RADIUS) > 0.0) {
         this.radius = (int)(this.radius + this.perk.getPerkTypeTotal(PerkModule.PerkType.HARVEST_SPELL_RADIUS));
      }
   }

   @Override
   public void update() throws SlickException {
      this.durationTick++;
      switch (this.spellStep) {
         case 0:
            this.lighting.addShortTermLight(this.tileX, this.tileY, (byte)this.radius, (byte)16, this.duration);
            this.projectile.newExpandingEnergyRing(this.targetX, this.targetY, this.radius * 16, this.radius / 2, ParticleModule.ParticleSet.MAGIC_YELLOW);
            this.sound.playSound(SoundModule.SoundType.MAGIC_HEAL_SPELL, this.targetX, this.targetY);
            this.sound.playSound(SoundModule.SoundType.MAGIC_ZAP, this.targetX, this.targetY);
            this.mob.tryConfuseInArea(this.tileX, this.tileY, this.radius, true, 2);
            this.coordinatesNearby = Utilities.getAllCoordinatesWithin(this.tileX, this.tileY, this.radius);
            this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.CASTING_SPELLS);
            this.influence.decreaseInfluenceAmount(this.castCost);
            this.spellStep++;
            break;
         case 1:
            if (this.durationTick > 80) {
               this.projectile.newExpandingEnergyRing(this.targetX, this.targetY, (this.radius - 1) * 16, 0.5F, ParticleModule.ParticleSet.FIRE_BLUE);
               this.projectile.newConstrictingEnergyRing(this.targetX, this.targetY, (this.radius - 1) * 16, 0.25F, ParticleModule.ParticleSet.FIRE_BLUE);
               this.spellStep++;
            }
            break;
         case 2:
            this.projectile.newEnergyRing(this.targetX, this.targetY, (this.radius - 1) * 16, ParticleModule.ParticleSet.FIRE_GREEN);
            if (this.durationTick % 300 == 0) {
               this.projectile.newConstrictingEnergyRing(this.targetX, this.targetY, (this.radius - 1) * 16, 0.5F, ParticleModule.ParticleSet.FIRE_GREEN);
            }

            if (this.durationTick % 5 == 0) {
               this.projectile.newEnergyRing(this.targetX, this.targetY, 16, ParticleModule.ParticleSet.MAGIC_GREEN);
            }

            if (this.durationTick % 5 == 0) {
               OrderedPair o = this.coordinatesNearby.get(Utilities.randomInt(this.coordinatesNearby.size()));
               if (this.resource.getResourceValues()[o.getX()][o.getY()] > 0 && !this.map.isTileFrozen(o.getX(), o.getY())) {
                  ResourceBase resourceSelection = this.resource.harvestResource(o.getX(), o.getY(), 10);
                  ArrayList<OrderedPair> coordinatesInRange = new ArrayList<>();

                  for (OrderedPair o2 : this.coordinatesNearby) {
                     if (this.object.isInRange(o2.getX(), o2.getY())) {
                        coordinatesInRange.add(o2);
                     }
                  }

                  ArrayList<OrderedPair> usedCoordinates = this.coordinatesNearby;
                  if (coordinatesInRange.size() > 0) {
                     usedCoordinates = coordinatesInRange;
                  }

                  OrderedPair destination = usedCoordinates.get(Utilities.randomInt(usedCoordinates.size()));
                  OrderedPair drop = this.resource.findOpenSpace(destination.getX(), destination.getY(), MapData.BlockMapGroup.STANDARD);
                  this.resource.setResourcesOnGround(drop.getX(), drop.getY(), resourceSelection);
                  this.projectile.newEnergyBeam(o.getX() * 16 + 8, o.getY() * 16 + 8, drop.getX() * 16 + 8, drop.getY() * 16 + 8);
                  this.mob.tryConfuseInArea(drop.getX(), drop.getY(), 4, true, 2);
                  this.mob.tryPanicInArea(drop.getX(), drop.getY(), 2, true, 4);
                  this.mob.influenceFaithInArea(drop.getX(), drop.getY(), 4, 1, true);
                  this.sound.playSound(SoundModule.SoundType.GRAB, this.targetX, this.targetY);
               }
            }

            if (this.durationTick > this.duration) {
               this.influence.removeSpell(this);
            }
      }
   }
}
