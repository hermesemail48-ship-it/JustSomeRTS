package rtr.influence;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.map.MapData;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class SpellConstruct extends SpellBase {
   @Override
   public void config(int x, int y, boolean freeCast) {
      this.type = SpellBase.SpellType.CONSTRUCT;
      this.name = Text.getText("spell.construct.name");
      this.description = Text.getText("spell.construct.description");
      this.cooldown = 3600;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.CONSTRUCT_SPELL_COOLDOWN) > 0.0) {
         this.cooldown = (int)(this.cooldown - 60.0 * this.perk.getPerkTypeTotal(PerkModule.PerkType.CONSTRUCT_SPELL_COOLDOWN));
      }

      this.castCost = freeCast ? 0 : 1000;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.CONSTRUCT_SPELL_COST) > 0.0) {
         this.castCost = (int)(this.castCost - this.castCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.CONSTRUCT_SPELL_COST));
      }

      this.targetX = x;
      this.targetY = y;
      this.tileX = x / 16;
      this.tileY = y / 16;
      this.duration = 1200;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.CONSTRUCT_SPELL_DURATION) > 0.0) {
         this.duration = (int)(this.duration + 60.0 * this.perk.getPerkTypeTotal(PerkModule.PerkType.CONSTRUCT_SPELL_DURATION));
      }

      this.radius = 6;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.CONSTRUCT_SPELL_RADIUS) > 0.0) {
         this.radius = (int)(this.radius + this.perk.getPerkTypeTotal(PerkModule.PerkType.CONSTRUCT_SPELL_RADIUS));
      }
   }

   @Override
   public void update() throws SlickException {
      this.durationTick++;
      switch (this.spellStep) {
         case 0:
            this.lighting.addShortTermLight(this.tileX, this.tileY, (byte)this.radius, (byte)16, this.duration);

            for (int x = 0; x < 50; x++) {
               this.projectile.newGroundStatic(this.targetX, this.targetY, ParticleModule.ParticleSet.FIRE_GREEN);
            }

            this.projectile.newExpandingFireRing(this.targetX, this.targetY, (this.radius - 2) * 16, 4.0F, ParticleModule.ParticleSet.MAGIC_YELLOW);
            this.sound.playSound(SoundModule.SoundType.MAGIC_HEAL_SPELL, this.targetX, this.targetY);
            this.sound.playSound(SoundModule.SoundType.MAGIC_ZAP, this.targetX, this.targetY);
            this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.CASTING_SPELLS);
            this.influence.decreaseInfluenceAmount(this.castCost);
            this.spellStep++;
            break;
         case 1:
            if (this.durationTick > 80) {
               this.spellStep++;
            }
            break;
         case 2:
            for (int x = 0; x < 10; x++) {
               int randomX = Utilities.randomInt(this.radius * 16 * -1, this.radius * 16);
               int randomY = Utilities.randomInt(this.radius * 16 * -1, this.radius * 16);
               this.particle
                  .newParticleGroup(
                     this.targetX + randomX, this.targetY + randomY, ParticleModule.ParticleGroup.SPARKLES, ParticleModule.ParticleSet.FIRE_GREEN
                  );
            }

            if (this.durationTick % 10 == 0) {
               int randomX = Utilities.randomInt(this.radius * 16 * -1, this.radius * 16);
               int randomY = Utilities.randomInt(this.radius * 16 * -1, this.radius * 16);
               this.particle
                  .newParticleGroup(
                     this.targetX + randomX, this.targetY + randomY, ParticleModule.ParticleGroup.SMOKE_BELLOW, ParticleModule.ParticleSet.FIRE_GREEN
                  );
            }

            if (this.durationTick % 60 == 0) {
               this.projectile.newExpandingFireRing(this.targetX, this.targetY, (this.radius - 1) * 16, 0.5F, ParticleModule.ParticleSet.FIRE_YELLOW);
            }

            if (this.durationTick % 5 == 0) {
               this.projectile.newEnergyRing(this.targetX, this.targetY, 8, ParticleModule.ParticleSet.MAGIC_GREEN);
            }

            ArrayList<ObjectBase> objectsInRange = this.object.getObjectsNearby(this.tileX, this.tileY, this.radius);
            ArrayList<ObjectBase> validObjects = new ArrayList<>();

            for (ObjectBase o : objectsInRange) {
               ResourceModule.ResourceType[] randomY;
               for (ResourceModule.ResourceType type : randomY = ResourceModule.ResourceType.values()) {
                  if (o.getObjectFlags().canBuildingBeBuilt(type)
                     && o.getResourceValueCount(type) < o.getObjectFlags().getResourceValueBaseCount(type)
                     && o.isResourceAvailable(type)) {
                     validObjects.add(o);
                     break;
                  }
               }
            }

            if (validObjects.size() > 0) {
               ObjectBase building = validObjects.get(Utilities.randomInt(validObjects.size()));
               if (this.durationTick % 60 == 0) {
                  this.sound.playSound(SoundModule.SoundType.EARTHQUAKE_ROCKS, this.targetX, this.targetY);
               }

               if (this.durationTick % 20 == 0 && Utilities.randomInt(5) == 0) {
                  this.sound.playSound(SoundModule.SoundType.STORE_ITEM, this.targetX, this.targetY);
               }

               if (this.durationTick % 20 == 0) {
                  this.mob.tryConfuseInArea(this.tileX, this.tileY, this.radius, true, 3);
                  building.increaseHitPoints(1 + (int)(building.getHitPointsMax() * 0.01F));
               }

               if (this.durationTick % 50 == 0) {
                  ResourceBase buildWith = null;

                  ResourceModule.ResourceType[] var28;
                  for (ResourceModule.ResourceType typex : ResourceModule.ResourceType.values()) {
                     if (building.getObjectFlags().canBuildingBeBuilt(typex)
                        && building.getResourceValueCount(typex) < building.getObjectFlags().getResourceValueBaseCount(typex)
                        && building.isResourceAvailable(typex)) {
                        buildWith = building.getAvailableResource(typex);
                        if (this.durationTick % 200 == 0) {
                           this.mob.influenceFaithInArea(this.tileX, this.tileY, this.radius, 1, 2, true);
                        }
                        break;
                     }
                  }

                  ResourceBase possibleTrash = null;
                  if (buildWith != null) {
                     possibleTrash = building.build(buildWith, true, 20);
                  }

                  if (possibleTrash != null) {
                     OrderedPair ox = this.resource.findOpenSpace(this.tileX, this.tileY, MapData.BlockMapGroup.STANDARD);
                     this.resource.setResourcesOnGround(ox.getX(), ox.getY(), possibleTrash);
                  }
               }

               int effects = building.getObjectFlags().getWidth() * building.getObjectFlags().getHeight() / 2;
               if (effects <= 0) {
                  effects = 1;
               }

               for (int s = 0; s < effects; s++) {
                  OrderedPair randomCoords = building.getRandomCoordinates(true);
                  int randomX = randomCoords.getX() * 16 + 8 + Utilities.randomInt(-8, 8);
                  int randomY = randomCoords.getY() * 16 + 8 + Utilities.randomInt(-8, 8);
                  int amount = this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[randomCoords.getX()][randomCoords.getY()] == 1 ? 5 : 1;

                  for (int x = 0; x < amount; x++) {
                     if (this.durationTick % 10 == 0) {
                        this.particle
                           .newParticleGroup(
                              randomX + Utilities.randomInt(-1, 1),
                              randomY + Utilities.randomInt(-1, 1),
                              ParticleModule.ParticleGroup.MAGIC_FIRE,
                              ParticleModule.ParticleSet.FIRE_YELLOW
                           );
                     }

                     if (this.durationTick % 100 == 0) {
                        this.particle
                           .newParticleGroup(
                              randomX + Utilities.randomInt(-1, 1),
                              randomY + Utilities.randomInt(-1, 1),
                              ParticleModule.ParticleGroup.SMOKE_BELLOW,
                              ParticleModule.ParticleSet.MAGIC_YELLOW
                           );
                     }
                  }
               }
            }

            if (this.durationTick > this.duration) {
               this.influence.removeSpell(this);
            }
      }
   }
}
