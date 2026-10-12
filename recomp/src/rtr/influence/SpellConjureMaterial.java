package rtr.influence;

import java.util.ArrayList;
import java.util.Collections;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.map.MapData;
import rtr.map.MapTilesLoader;
import rtr.particles.ParticleModule;
import rtr.resources.ResourceModule;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class SpellConjureMaterial extends SpellBase {
   protected ResourceModule.ResourceType resourceType;
   protected ResourceModule.ResourceColorSet resourceColor;

   @Override
   public void config(int x, int y, boolean freeCast) {
      this.type = SpellBase.SpellType.CONJURE_MATERIAL;
      this.name = Text.getText("spell.conjureMaterial.name");
      this.description = Text.getText("spell.conjureMaterial.description");
      this.cooldown = 30;
      this.castCost = freeCast ? 0 : 75;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.CONJURE_MATERIAL_SPELL_COST) > 0.0) {
         this.castCost = (int)(this.castCost - this.castCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.CONJURE_MATERIAL_SPELL_COST));
      }

      this.radius = 2;
      this.targetX = x;
      this.targetY = y;
      this.tileX = x / 16;
      this.tileY = y / 16;
   }

   @Override
   public void update() throws SlickException {
      this.durationTick++;
      switch (this.spellStep) {
         case 0:
            this.lighting.addShortTermLight(this.tileX, this.tileY, (byte)2, (byte)12, 400);
            this.projectile.newConstrictingFireRing(this.targetX, this.targetY, 32, 0.2F, ParticleModule.ParticleSet.FIRE_PURPLE);
            this.sound.playSound(SoundModule.SoundType.MAGIC_ZAP, this.targetX, this.targetY);
            this.pickResourceType();
            this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.CASTING_SPELLS);
            this.influence.decreaseInfluenceAmount(this.castCost);
            this.mob.tryConfuseInArea(this.tileX, this.tileY, this.radius, true, 3);
            this.spellStep++;
            break;
         case 1:
            if (this.durationTick > 120) {
               this.sound.playSound(SoundModule.SoundType.EARTHQUAKE_ROCKS, this.targetX, this.targetY);
               this.spellStep++;
            }
            break;
         case 2:
            this.projectile.newExpandingFireRing(this.targetX, this.targetY, 8, 2.0F, ParticleModule.ParticleSet.FIRE_GREEN);
            if (this.durationTick % 30 == 0) {
               this.sound.playSound(SoundModule.SoundType.EARTHQUAKE_ROCKS, this.targetX, this.targetY);
            }

            if (this.durationTick == 300) {
               this.sound.playSound(SoundModule.SoundType.MAGIC_HEAL, this.targetX, this.targetY);
            }

            if (this.durationTick > 340) {
               this.spellStep++;
            }
            break;
         case 3:
            int amount = 1 + Utilities.randomInt(3);
            if (this.perk.getPerkTypeTotal(PerkModule.PerkType.CONJURE_MATERIAL_SPELL_AMOUNT) > 0.0) {
               amount = (int)(amount + this.perk.getPerkTypeTotal(PerkModule.PerkType.CONJURE_MATERIAL_SPELL_AMOUNT));
            }

            for (int i = 0; i < amount; i++) {
               if (!this.resource.isResourcesOnGround(this.tileX, this.tileY)
                  && this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[this.tileX][this.tileY] == 0) {
                  this.resource.createResourceOnGround(this.resourceType, this.resourceColor, this.tileX, this.tileY);
               } else {
                  OrderedPair o = this.resource.findOpenSpace(this.tileX, this.tileY, MapData.BlockMapGroup.STANDARD);
                  this.resource.createResourceOnGround(this.resourceType, this.resourceColor, o.getX(), o.getY());
               }
            }

            this.projectile.newExpandingEnergyRing(this.targetX, this.targetY, 42, 1.0F, ParticleModule.ParticleSet.FIRE_GREEN);
            this.mob.tryPanicInArea(this.tileX, this.tileY, this.radius, true, 1);
            this.mob.influenceFaithInArea(this.tileX, this.tileY, this.radius, 1, 3, true);
            this.influence.removeSpell(this);
            this.spellStep++;
      }
   }

   private void pickResourceType() {
      int distance = 32;
      ArrayList<OrderedPair> nearbyCoordinates = Utilities.getAllCoordinatesWithin(this.tileX, this.tileY, distance * 2);
      ArrayList<OrderedPair> culledCoordinates = new ArrayList<>();
      ArrayList<OrderedPair> resourceCoordinates = new ArrayList<>();
      MapTilesLoader.TileSet resourceTileSet = null;

      for (OrderedPair tile : nearbyCoordinates) {
         int dx = this.tileX - tile.getX();
         int dy = this.tileY - tile.getY();
         int hValue = (int)Math.sqrt(dx * dx + dy * dy);
         if (hValue < distance) {
            culledCoordinates.add(tile);
         }
      }

      Collections.shuffle(culledCoordinates);

      for (OrderedPair tilex : culledCoordinates) {
         int tileID = this.map.getTileId(tilex.getX(), tilex.getY(), 6);
         if (tileID == 0) {
            tileID = this.map.getTileId(tilex.getX(), tilex.getY(), 9);
         }

         if (tileID != 0) {
            int tileGID = this.map.getMapTileLoader().getTileSetGID(tileID);
            if (resourceTileSet == null) {
               MapTilesLoader.TileSet rockNearby = this.map.getMapTileLoader().getTileSetByTileID(tileGID);
               ResourceModule.ResourceType resourceType = this.map.getMapTileLoader().getTileResourceType(tileGID);
               if (resourceType == ResourceModule.ResourceType.ROCK
                  || resourceType == ResourceModule.ResourceType.WOOD
                  || resourceType == ResourceModule.ResourceType.CRYSTAL
                  || resourceType == ResourceModule.ResourceType.RAW_VEGETABLE) {
                  resourceTileSet = rockNearby;
               }
            }

            MapTilesLoader.TileSet regrowTile = this.map.getMapTileLoader().canTileRegrow(tileID);
            if (regrowTile != null) {
               ResourceModule.ResourceType resourceType = this.map
                  .getMapTileLoader()
                  .getTileResourceType(this.map.getMapTileLoader().getTileSetGID(regrowTile));
               if (resourceType == ResourceModule.ResourceType.ROCK
                  && resourceType == ResourceModule.ResourceType.WOOD
                  && resourceType == ResourceModule.ResourceType.CRYSTAL
                  && resourceType == ResourceModule.ResourceType.RAW_VEGETABLE) {
                  if (resourceTileSet == null) {
                     resourceTileSet = regrowTile;
                  }

                  resourceCoordinates.add(tilex);
               }
            }
         }
      }

      if (resourceTileSet == null) {
         label86:
         for (int x = 0; x < 256; x++) {
            int y = 0;

            while (y < 256) {
               int tileIDx = this.map.getTileId(x, y, 6);
               int tileGIDx = this.map.getMapTileLoader().getTileSetGID(tileIDx);
               MapTilesLoader.TileSet resourceNearby = this.map.getMapTileLoader().getTileSetByTileID(tileGIDx);
               ResourceModule.ResourceType resourceType = this.map.getMapTileLoader().getTileResourceType(tileGIDx);
               if (resourceType != ResourceModule.ResourceType.ROCK
                  && resourceType != ResourceModule.ResourceType.WOOD
                  && resourceType != ResourceModule.ResourceType.CRYSTAL
                  && resourceType != ResourceModule.ResourceType.RAW_VEGETABLE) {
                  tileIDx = this.map.getTileId(x, y, 9);
                  tileGIDx = this.map.getMapTileLoader().getTileSetGID(tileIDx);
                  resourceNearby = this.map.getMapTileLoader().getTileSetByTileID(tileGIDx);
                  resourceType = this.map.getMapTileLoader().getTileResourceType(tileGIDx);
                  if (resourceType != ResourceModule.ResourceType.ROCK
                     && resourceType != ResourceModule.ResourceType.WOOD
                     && resourceType != ResourceModule.ResourceType.CRYSTAL
                     && resourceType != ResourceModule.ResourceType.RAW_VEGETABLE) {
                     y++;
                     continue;
                  }

                  resourceTileSet = resourceNearby;
                  break label86;
               }

               resourceTileSet = resourceNearby;
               break label86;
            }
         }
      }

      if (resourceTileSet == null) {
         switch (Utilities.randomInt(4)) {
            case 0:
               resourceTileSet = MapTilesLoader.TileSet.ROCK_GRAY;
            case 1:
               resourceTileSet = MapTilesLoader.TileSet.TREES_GREEN;
            case 2:
               resourceTileSet = MapTilesLoader.TileSet.CARROTS;
            case 3:
               resourceTileSet = MapTilesLoader.TileSet.CRYSTAL_BLUE;
         }
      }

      this.resourceType = this.map.getMapTileLoader().getTileResourceType(this.map.getMapTileLoader().getTileSetGID(resourceTileSet));
      this.resourceColor = this.map.getMapTileLoader().getTileResourceColorSet(this.map.getMapTileLoader().getTileSetGID(resourceTileSet));
   }
}
