package rtr.objects.objectflags;

import java.util.HashMap;
import org.newdawn.slick.SlickException;
import rtr.map.MapTilesLoader;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.corruption.CorruptedFireBowTowerFlags;
import rtr.objects.objectflags.corruption.CorruptedFirePhantomDartTowerFlags;
import rtr.objects.objectflags.corruption.CorruptedFirePitFlags;
import rtr.objects.objectflags.corruption.CorruptedGraveyardLargeFlags;
import rtr.objects.objectflags.corruption.CorruptedGraveyardMediumFlags;
import rtr.objects.objectflags.corruption.CorruptedGraveyardSmallFlags;
import rtr.objects.objectflags.corruption.CorruptedIceBowTowerFlags;
import rtr.objects.objectflags.corruption.CorruptedIcePhantomDartTowerFlags;
import rtr.objects.objectflags.corruption.CorruptedLargeFirePitFlags;
import rtr.objects.objectflags.corruption.CorruptedLightningBowTowerFlags;
import rtr.objects.objectflags.corruption.CorruptedLightningPhantomDartTowerFlags;
import rtr.objects.objectflags.corruption.CorruptedPoisonBowTowerFlags;
import rtr.objects.objectflags.corruption.CorruptedPoisonPhantomDartTowerFlags;
import rtr.objects.objectflags.ruins.AncientCullisGateFlags;
import rtr.objects.objectflags.ruins.AncientRadiancePoolFlags;
import rtr.objects.objectflags.towers.AttractTowerFlags;
import rtr.objects.objectflags.towers.BallistaTowerFlags;
import rtr.objects.objectflags.towers.BanishTowerFlags;
import rtr.objects.objectflags.towers.BowTowerFlags;
import rtr.objects.objectflags.towers.BulletTowerFlags;
import rtr.objects.objectflags.towers.ElementalBoltTowerFlags;
import rtr.objects.objectflags.towers.GodTowerFlags;
import rtr.objects.objectflags.towers.PhantomDartTowerFlags;
import rtr.objects.objectflags.towers.RecombobulatorTowerFlags;
import rtr.objects.objectflags.towers.SlingTowerFlags;
import rtr.objects.objectflags.towers.SprayTowerFlags;
import rtr.objects.objectflags.towers.StaticTowerFlags;
import rtr.objects.objectflags.village.AmmoStorageFlags;
import rtr.objects.objectflags.village.AncillaryFlags;
import rtr.objects.objectflags.village.AnimalPenFlags;
import rtr.objects.objectflags.village.ArmorsmithyFlags;
import rtr.objects.objectflags.village.BottlerFlags;
import rtr.objects.objectflags.village.BowyerFlags;
import rtr.objects.objectflags.village.BurnerFlags;
import rtr.objects.objectflags.village.CastleFlags;
import rtr.objects.objectflags.village.ClinicFlags;
import rtr.objects.objectflags.village.CluckerCoopFlags;
import rtr.objects.objectflags.village.CourierStationFlags;
import rtr.objects.objectflags.village.CrylithiumFirePitFlags;
import rtr.objects.objectflags.village.CrystalGolemCombobulatorFlags;
import rtr.objects.objectflags.village.CrystalHarvestryFlags;
import rtr.objects.objectflags.village.CrystalMotivatorFlags;
import rtr.objects.objectflags.village.CrystalStorageFlags;
import rtr.objects.objectflags.village.CrystilleryFlags;
import rtr.objects.objectflags.village.CubeEGolemCombobulatorFlags;
import rtr.objects.objectflags.village.CullisGateFlags;
import rtr.objects.objectflags.village.DoggoHouseFlags;
import rtr.objects.objectflags.village.EquipmentStorageFlags;
import rtr.objects.objectflags.village.EssenceAltarFlags;
import rtr.objects.objectflags.village.EssenceCollectorFlags;
import rtr.objects.objectflags.village.FarmFlags;
import rtr.objects.objectflags.village.FirePitFlags;
import rtr.objects.objectflags.village.FoodStorageFlags;
import rtr.objects.objectflags.village.ForgeFlags;
import rtr.objects.objectflags.village.GoldStorageFlags;
import rtr.objects.objectflags.village.HousingFlags;
import rtr.objects.objectflags.village.KeyShackFlags;
import rtr.objects.objectflags.village.KitchenFlags;
import rtr.objects.objectflags.village.LandfillFlags;
import rtr.objects.objectflags.village.LargeCrylithiumFirePitFlags;
import rtr.objects.objectflags.village.LargeFirePitFlags;
import rtr.objects.objectflags.village.LargeFountainFlags;
import rtr.objects.objectflags.village.LightningRodFlags;
import rtr.objects.objectflags.village.LootBoxFlags;
import rtr.objects.objectflags.village.LumberMillFlags;
import rtr.objects.objectflags.village.LumberShackFlags;
import rtr.objects.objectflags.village.MaintenanceBuildingFlags;
import rtr.objects.objectflags.village.MarketplaceFlags;
import rtr.objects.objectflags.village.MigrationWayStationFlags;
import rtr.objects.objectflags.village.MineralStorageFlags;
import rtr.objects.objectflags.village.MiningFacilityFlags;
import rtr.objects.objectflags.village.MiscellaneousStorageFlags;
import rtr.objects.objectflags.village.OutpostFlags;
import rtr.objects.objectflags.village.ProcessorFlags;
import rtr.objects.objectflags.village.RainCatcherFlags;
import rtr.objects.objectflags.village.RangerLodgeFlags;
import rtr.objects.objectflags.village.ReliquaryFlags;
import rtr.objects.objectflags.village.RockStorageFlags;
import rtr.objects.objectflags.village.RockTumblerFlags;
import rtr.objects.objectflags.village.SmallFountainFlags;
import rtr.objects.objectflags.village.StoneCutteryFlags;
import rtr.objects.objectflags.village.StoneGolemCombobulatorFlags;
import rtr.objects.objectflags.village.ToolsmithyFlags;
import rtr.objects.objectflags.village.TrashCanFlags;
import rtr.objects.objectflags.village.TrashyCubePileFlags;
import rtr.objects.objectflags.village.WaterPurifierFlags;
import rtr.objects.objectflags.village.WayMakerShackFlags;
import rtr.objects.objectflags.village.WellFlags;
import rtr.objects.objectflags.village.WoodGolemCombobulatorFlags;
import rtr.objects.objectflags.village.WoodStorageFlags;
import rtr.objects.objectflags.walls.CrylithiumCurtainWallFlags;
import rtr.objects.objectflags.walls.CrylithiumWallFlags;
import rtr.objects.objectflags.walls.CrylithiumWallGateNSFlags;
import rtr.objects.objectflags.walls.CrylithiumWallGateWEFlags;
import rtr.objects.objectflags.walls.CurtainWallFlags;
import rtr.objects.objectflags.walls.GodWallFlags;
import rtr.objects.objectflags.walls.StoneWallFlags;
import rtr.objects.objectflags.walls.StoneWallGateNSFlags;
import rtr.objects.objectflags.walls.StoneWallGateWEFlags;
import rtr.objects.objectflags.walls.TrashyCubeWallFlags;
import rtr.objects.objectflags.walls.WoodFenceFlags;
import rtr.objects.objectflags.walls.WoodFenceGateNSFlags;
import rtr.objects.objectflags.walls.WoodFenceGateWEFlags;

public class ObjectFlagFactory {
   private HashMap<MapTilesLoader.TileSet, HashMap<ObjectBase.ObjectSubType, ObjectFlags>> objectFlags = new HashMap<>();

   public ObjectFlagFactory() throws SlickException {
      MapTilesLoader.TileSet[] var4;
      for (MapTilesLoader.TileSet type : MapTilesLoader.TileSet.values()) {
         this.objectFlags.put(type, new HashMap<>());
      }

      for (ObjectBase.ObjectSubType subType : ObjectBase.ObjectSubType.values()) {
         this.objectFlags
            .get(MapTilesLoader.TileSet.CORRUPTED_FIRE_BOW_TOWER)
            .put(subType, new CorruptedFireBowTowerFlags(MapTilesLoader.TileSet.CORRUPTED_FIRE_BOW_TOWER, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CORRUPTED_FIRE_PHANTOM_DART_TOWER)
            .put(subType, new CorruptedFirePhantomDartTowerFlags(MapTilesLoader.TileSet.CORRUPTED_FIRE_PHANTOM_DART_TOWER, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CORRUPTED_FIRE_PIT)
            .put(subType, new CorruptedFirePitFlags(MapTilesLoader.TileSet.CORRUPTED_FIRE_PIT, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CORRUPTED_GRAVEYARD_LARGE)
            .put(subType, new CorruptedGraveyardLargeFlags(MapTilesLoader.TileSet.CORRUPTED_GRAVEYARD_LARGE, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CORRUPTED_GRAVEYARD_MEDIUM)
            .put(subType, new CorruptedGraveyardMediumFlags(MapTilesLoader.TileSet.CORRUPTED_GRAVEYARD_MEDIUM, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CORRUPTED_GRAVEYARD_SMALL)
            .put(subType, new CorruptedGraveyardSmallFlags(MapTilesLoader.TileSet.CORRUPTED_GRAVEYARD_SMALL, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CORRUPTED_ICE_BOW_TOWER)
            .put(subType, new CorruptedIceBowTowerFlags(MapTilesLoader.TileSet.CORRUPTED_ICE_BOW_TOWER, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CORRUPTED_ICE_PHANTOM_DART_TOWER)
            .put(subType, new CorruptedIcePhantomDartTowerFlags(MapTilesLoader.TileSet.CORRUPTED_ICE_PHANTOM_DART_TOWER, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CORRUPTED_LARGE_FIRE_PIT)
            .put(subType, new CorruptedLargeFirePitFlags(MapTilesLoader.TileSet.CORRUPTED_LARGE_FIRE_PIT, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CORRUPTED_LIGHTNING_BOW_TOWER)
            .put(subType, new CorruptedLightningBowTowerFlags(MapTilesLoader.TileSet.CORRUPTED_LIGHTNING_BOW_TOWER, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CORRUPTED_LIGHTNING_PHANTOM_DART_TOWER)
            .put(subType, new CorruptedLightningPhantomDartTowerFlags(MapTilesLoader.TileSet.CORRUPTED_LIGHTNING_PHANTOM_DART_TOWER, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CORRUPTED_POISON_BOW_TOWER)
            .put(subType, new CorruptedPoisonBowTowerFlags(MapTilesLoader.TileSet.CORRUPTED_POISON_BOW_TOWER, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CORRUPTED_POISON_PHANTOM_DART_TOWER)
            .put(subType, new CorruptedPoisonPhantomDartTowerFlags(MapTilesLoader.TileSet.CORRUPTED_POISON_PHANTOM_DART_TOWER, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.ANCIENT_CULLIS_GATE)
            .put(subType, new AncientCullisGateFlags(MapTilesLoader.TileSet.ANCIENT_CULLIS_GATE, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.ANCIENT_RADIANCE_POOL)
            .put(subType, new AncientRadiancePoolFlags(MapTilesLoader.TileSet.ANCIENT_RADIANCE_POOL, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.ATTRACT_TOWER).put(subType, new AttractTowerFlags(MapTilesLoader.TileSet.ATTRACT_TOWER, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.ATTRACT_TOWER_UPGRADE_1)
            .put(subType, new AttractTowerFlags(MapTilesLoader.TileSet.ATTRACT_TOWER_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.ATTRACT_TOWER_UPGRADE_2)
            .put(subType, new AttractTowerFlags(MapTilesLoader.TileSet.ATTRACT_TOWER_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.ATTRACT_TOWER_UPGRADE_3)
            .put(subType, new AttractTowerFlags(MapTilesLoader.TileSet.ATTRACT_TOWER_UPGRADE_3, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.BALLISTA_TOWER).put(subType, new BallistaTowerFlags(MapTilesLoader.TileSet.BALLISTA_TOWER, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.BALLISTA_TOWER_UPGRADE_1)
            .put(subType, new BallistaTowerFlags(MapTilesLoader.TileSet.BALLISTA_TOWER_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.BALLISTA_TOWER_UPGRADE_2)
            .put(subType, new BallistaTowerFlags(MapTilesLoader.TileSet.BALLISTA_TOWER_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.BALLISTA_TOWER_UPGRADE_3_FIRE)
            .put(subType, new BallistaTowerFlags(MapTilesLoader.TileSet.BALLISTA_TOWER_UPGRADE_3_FIRE, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.BALLISTA_TOWER_UPGRADE_3_POISON)
            .put(subType, new BallistaTowerFlags(MapTilesLoader.TileSet.BALLISTA_TOWER_UPGRADE_3_POISON, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.BALLISTA_TOWER_UPGRADE_3_ICE)
            .put(subType, new BallistaTowerFlags(MapTilesLoader.TileSet.BALLISTA_TOWER_UPGRADE_3_ICE, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.BANISH_TOWER).put(subType, new BanishTowerFlags(MapTilesLoader.TileSet.BANISH_TOWER, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.BANISH_TOWER_UPGRADE_1)
            .put(subType, new BanishTowerFlags(MapTilesLoader.TileSet.BANISH_TOWER_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.BANISH_TOWER_UPGRADE_2)
            .put(subType, new BanishTowerFlags(MapTilesLoader.TileSet.BANISH_TOWER_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.BANISH_TOWER_UPGRADE_3)
            .put(subType, new BanishTowerFlags(MapTilesLoader.TileSet.BANISH_TOWER_UPGRADE_3, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.BOW_TOWER).put(subType, new BowTowerFlags(MapTilesLoader.TileSet.BOW_TOWER, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.BOW_TOWER_UPGRADE_1).put(subType, new BowTowerFlags(MapTilesLoader.TileSet.BOW_TOWER_UPGRADE_1, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.BOW_TOWER_UPGRADE_2).put(subType, new BowTowerFlags(MapTilesLoader.TileSet.BOW_TOWER_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.BOW_TOWER_UPGRADE_3_FIRE)
            .put(subType, new BowTowerFlags(MapTilesLoader.TileSet.BOW_TOWER_UPGRADE_3_FIRE, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.BOW_TOWER_UPGRADE_3_POISON)
            .put(subType, new BowTowerFlags(MapTilesLoader.TileSet.BOW_TOWER_UPGRADE_3_POISON, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.BOW_TOWER_UPGRADE_3_ICE)
            .put(subType, new BowTowerFlags(MapTilesLoader.TileSet.BOW_TOWER_UPGRADE_3_ICE, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.BULLET_TOWER).put(subType, new BulletTowerFlags(MapTilesLoader.TileSet.BULLET_TOWER, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.BULLET_TOWER_UPGRADE_1)
            .put(subType, new BulletTowerFlags(MapTilesLoader.TileSet.BULLET_TOWER_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.BULLET_TOWER_UPGRADE_2)
            .put(subType, new BulletTowerFlags(MapTilesLoader.TileSet.BULLET_TOWER_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.BULLET_TOWER_UPGRADE_3_FIRE)
            .put(subType, new BulletTowerFlags(MapTilesLoader.TileSet.BULLET_TOWER_UPGRADE_3_FIRE, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.BULLET_TOWER_UPGRADE_3_LIGHTNING)
            .put(subType, new BulletTowerFlags(MapTilesLoader.TileSet.BULLET_TOWER_UPGRADE_3_LIGHTNING, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.BULLET_TOWER_UPGRADE_3_ICE)
            .put(subType, new BulletTowerFlags(MapTilesLoader.TileSet.BULLET_TOWER_UPGRADE_3_ICE, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.ELEMENTAL_BOLT_TOWER)
            .put(subType, new ElementalBoltTowerFlags(MapTilesLoader.TileSet.ELEMENTAL_BOLT_TOWER, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.ELEMENTAL_BOLT_TOWER_UPGRADE_1)
            .put(subType, new ElementalBoltTowerFlags(MapTilesLoader.TileSet.ELEMENTAL_BOLT_TOWER_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.ELEMENTAL_BOLT_TOWER_UPGRADE_2)
            .put(subType, new ElementalBoltTowerFlags(MapTilesLoader.TileSet.ELEMENTAL_BOLT_TOWER_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.ELEMENTAL_BOLT_TOWER_UPGRADE_3_FIRE)
            .put(subType, new ElementalBoltTowerFlags(MapTilesLoader.TileSet.ELEMENTAL_BOLT_TOWER_UPGRADE_3_FIRE, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.ELEMENTAL_BOLT_TOWER_UPGRADE_3_LIGHTNING)
            .put(subType, new ElementalBoltTowerFlags(MapTilesLoader.TileSet.ELEMENTAL_BOLT_TOWER_UPGRADE_3_LIGHTNING, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.ELEMENTAL_BOLT_TOWER_UPGRADE_3_ICE)
            .put(subType, new ElementalBoltTowerFlags(MapTilesLoader.TileSet.ELEMENTAL_BOLT_TOWER_UPGRADE_3_ICE, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.PHANTOM_DART_TOWER)
            .put(subType, new PhantomDartTowerFlags(MapTilesLoader.TileSet.PHANTOM_DART_TOWER, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.PHANTOM_DART_TOWER_UPGRADE_1)
            .put(subType, new PhantomDartTowerFlags(MapTilesLoader.TileSet.PHANTOM_DART_TOWER_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.PHANTOM_DART_TOWER_UPGRADE_2)
            .put(subType, new PhantomDartTowerFlags(MapTilesLoader.TileSet.PHANTOM_DART_TOWER_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.PHANTOM_DART_TOWER_UPGRADE_3_FIRE)
            .put(subType, new PhantomDartTowerFlags(MapTilesLoader.TileSet.PHANTOM_DART_TOWER_UPGRADE_3_FIRE, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.PHANTOM_DART_TOWER_UPGRADE_3_LIGHTNING)
            .put(subType, new PhantomDartTowerFlags(MapTilesLoader.TileSet.PHANTOM_DART_TOWER_UPGRADE_3_LIGHTNING, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.PHANTOM_DART_TOWER_UPGRADE_3_ICE)
            .put(subType, new PhantomDartTowerFlags(MapTilesLoader.TileSet.PHANTOM_DART_TOWER_UPGRADE_3_ICE, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.RECOMBOBULATOR_TOWER)
            .put(subType, new RecombobulatorTowerFlags(MapTilesLoader.TileSet.RECOMBOBULATOR_TOWER, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.RECOMBOBULATOR_TOWER_UPGRADE_1)
            .put(subType, new RecombobulatorTowerFlags(MapTilesLoader.TileSet.RECOMBOBULATOR_TOWER_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.RECOMBOBULATOR_TOWER_UPGRADE_2)
            .put(subType, new RecombobulatorTowerFlags(MapTilesLoader.TileSet.RECOMBOBULATOR_TOWER_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.RECOMBOBULATOR_TOWER_UPGRADE_3)
            .put(subType, new RecombobulatorTowerFlags(MapTilesLoader.TileSet.RECOMBOBULATOR_TOWER_UPGRADE_3, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.SLING_TOWER).put(subType, new SlingTowerFlags(MapTilesLoader.TileSet.SLING_TOWER, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.SLING_TOWER_UPGRADE_1)
            .put(subType, new SlingTowerFlags(MapTilesLoader.TileSet.SLING_TOWER_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.SLING_TOWER_UPGRADE_2)
            .put(subType, new SlingTowerFlags(MapTilesLoader.TileSet.SLING_TOWER_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.SLING_TOWER_UPGRADE_3_EXPLODING)
            .put(subType, new SlingTowerFlags(MapTilesLoader.TileSet.SLING_TOWER_UPGRADE_3_EXPLODING, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.SLING_TOWER_UPGRADE_3_LIGHTNING)
            .put(subType, new SlingTowerFlags(MapTilesLoader.TileSet.SLING_TOWER_UPGRADE_3_LIGHTNING, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.SLING_TOWER_UPGRADE_3_ICE)
            .put(subType, new SlingTowerFlags(MapTilesLoader.TileSet.SLING_TOWER_UPGRADE_3_ICE, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.SPRAY_TOWER).put(subType, new SprayTowerFlags(MapTilesLoader.TileSet.SPRAY_TOWER, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.SPRAY_TOWER_UPGRADE_1)
            .put(subType, new SprayTowerFlags(MapTilesLoader.TileSet.SPRAY_TOWER_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.SPRAY_TOWER_UPGRADE_2)
            .put(subType, new SprayTowerFlags(MapTilesLoader.TileSet.SPRAY_TOWER_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.SPRAY_TOWER_UPGRADE_3_FIRE)
            .put(subType, new SprayTowerFlags(MapTilesLoader.TileSet.SPRAY_TOWER_UPGRADE_3_FIRE, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.SPRAY_TOWER_UPGRADE_3_LIGHTNING)
            .put(subType, new SprayTowerFlags(MapTilesLoader.TileSet.SPRAY_TOWER_UPGRADE_3_LIGHTNING, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.SPRAY_TOWER_UPGRADE_3_ICE)
            .put(subType, new SprayTowerFlags(MapTilesLoader.TileSet.SPRAY_TOWER_UPGRADE_3_ICE, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.STATIC_TOWER).put(subType, new StaticTowerFlags(MapTilesLoader.TileSet.STATIC_TOWER, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.STATIC_TOWER_UPGRADE_1)
            .put(subType, new StaticTowerFlags(MapTilesLoader.TileSet.STATIC_TOWER_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.STATIC_TOWER_UPGRADE_2)
            .put(subType, new StaticTowerFlags(MapTilesLoader.TileSet.STATIC_TOWER_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.STATIC_TOWER_UPGRADE_3)
            .put(subType, new StaticTowerFlags(MapTilesLoader.TileSet.STATIC_TOWER_UPGRADE_3, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.GOD_TOWER).put(subType, new GodTowerFlags(MapTilesLoader.TileSet.GOD_TOWER, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.AMMO_STORAGE).put(subType, new AmmoStorageFlags(MapTilesLoader.TileSet.AMMO_STORAGE, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.AMMO_STORAGE_UPGRADE_1)
            .put(subType, new AmmoStorageFlags(MapTilesLoader.TileSet.AMMO_STORAGE_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.AMMO_STORAGE_UPGRADE_2)
            .put(subType, new AmmoStorageFlags(MapTilesLoader.TileSet.AMMO_STORAGE_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.AMMO_STORAGE_UPGRADE_3)
            .put(subType, new AmmoStorageFlags(MapTilesLoader.TileSet.AMMO_STORAGE_UPGRADE_3, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.AMMO_STORAGE_UPGRADE_4)
            .put(subType, new AmmoStorageFlags(MapTilesLoader.TileSet.AMMO_STORAGE_UPGRADE_4, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.ANCILLARY).put(subType, new AncillaryFlags(MapTilesLoader.TileSet.ANCILLARY, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.ANCILLARY_UPGRADE_1).put(subType, new AncillaryFlags(MapTilesLoader.TileSet.ANCILLARY_UPGRADE_1, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.ANCILLARY_UPGRADE_2).put(subType, new AncillaryFlags(MapTilesLoader.TileSet.ANCILLARY_UPGRADE_2, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.ANCILLARY_UPGRADE_3).put(subType, new AncillaryFlags(MapTilesLoader.TileSet.ANCILLARY_UPGRADE_3, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.ANCILLARY_UPGRADE_4).put(subType, new AncillaryFlags(MapTilesLoader.TileSet.ANCILLARY_UPGRADE_4, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.ANIMAL_PEN).put(subType, new AnimalPenFlags(MapTilesLoader.TileSet.ANIMAL_PEN, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.ANIMAL_PEN_UPGRADE_1)
            .put(subType, new AnimalPenFlags(MapTilesLoader.TileSet.ANIMAL_PEN_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.ANIMAL_PEN_UPGRADE_2)
            .put(subType, new AnimalPenFlags(MapTilesLoader.TileSet.ANIMAL_PEN_UPGRADE_2, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.ARMORSMITHY).put(subType, new ArmorsmithyFlags(MapTilesLoader.TileSet.ARMORSMITHY, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.ARMORSMITHY_UPGRADE_1)
            .put(subType, new ArmorsmithyFlags(MapTilesLoader.TileSet.ARMORSMITHY_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.ARMORSMITHY_UPGRADE_2)
            .put(subType, new ArmorsmithyFlags(MapTilesLoader.TileSet.ARMORSMITHY_UPGRADE_2, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.BOTTLER).put(subType, new BottlerFlags(MapTilesLoader.TileSet.BOTTLER, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.BOTTLER_UPGRADE_1).put(subType, new BottlerFlags(MapTilesLoader.TileSet.BOTTLER_UPGRADE_1, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.BOTTLER_UPGRADE_2).put(subType, new BottlerFlags(MapTilesLoader.TileSet.BOTTLER_UPGRADE_2, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.RANGER_LODGE).put(subType, new RangerLodgeFlags(MapTilesLoader.TileSet.RANGER_LODGE, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.RANGER_LODGE_UPGRADE_1)
            .put(subType, new RangerLodgeFlags(MapTilesLoader.TileSet.RANGER_LODGE_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.RANGER_LODGE_UPGRADE_2)
            .put(subType, new RangerLodgeFlags(MapTilesLoader.TileSet.RANGER_LODGE_UPGRADE_2, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.BURNER).put(subType, new BurnerFlags(MapTilesLoader.TileSet.BURNER, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.BURNER_UPGRADE_1).put(subType, new BurnerFlags(MapTilesLoader.TileSet.BURNER_UPGRADE_1, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.BURNER_UPGRADE_2).put(subType, new BurnerFlags(MapTilesLoader.TileSet.BURNER_UPGRADE_2, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.BOWYER).put(subType, new BowyerFlags(MapTilesLoader.TileSet.BOWYER, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.BOWYER_UPGRADE_1).put(subType, new BowyerFlags(MapTilesLoader.TileSet.BOWYER_UPGRADE_1, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.BOWYER_UPGRADE_2).put(subType, new BowyerFlags(MapTilesLoader.TileSet.BOWYER_UPGRADE_2, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.CASTLE_1).put(subType, new CastleFlags(MapTilesLoader.TileSet.CASTLE_1, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.CASTLE_2).put(subType, new CastleFlags(MapTilesLoader.TileSet.CASTLE_2, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.CASTLE_3).put(subType, new CastleFlags(MapTilesLoader.TileSet.CASTLE_3, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.CASTLE_4).put(subType, new CastleFlags(MapTilesLoader.TileSet.CASTLE_4, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.CASTLE_5).put(subType, new CastleFlags(MapTilesLoader.TileSet.CASTLE_5, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.CASTLE_6).put(subType, new CastleFlags(MapTilesLoader.TileSet.CASTLE_6, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.CASTLE_7).put(subType, new CastleFlags(MapTilesLoader.TileSet.CASTLE_7, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.CASTLE_8).put(subType, new CastleFlags(MapTilesLoader.TileSet.CASTLE_8, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.CASTLE_9).put(subType, new CastleFlags(MapTilesLoader.TileSet.CASTLE_9, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.CASTLE_10).put(subType, new CastleFlags(MapTilesLoader.TileSet.CASTLE_10, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.CASTLE_11).put(subType, new CastleFlags(MapTilesLoader.TileSet.CASTLE_11, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.CASTLE_12).put(subType, new CastleFlags(MapTilesLoader.TileSet.CASTLE_12, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.CASTLE_13).put(subType, new CastleFlags(MapTilesLoader.TileSet.CASTLE_13, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.CASTLE_14).put(subType, new CastleFlags(MapTilesLoader.TileSet.CASTLE_14, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.CASTLE_15).put(subType, new CastleFlags(MapTilesLoader.TileSet.CASTLE_15, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.CLINIC).put(subType, new ClinicFlags(MapTilesLoader.TileSet.CLINIC, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.CLINIC_UPGRADE_1).put(subType, new ClinicFlags(MapTilesLoader.TileSet.CLINIC_UPGRADE_1, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.CLINIC_UPGRADE_2).put(subType, new ClinicFlags(MapTilesLoader.TileSet.CLINIC_UPGRADE_2, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.CLUCKER_COOP).put(subType, new CluckerCoopFlags(MapTilesLoader.TileSet.CLUCKER_COOP, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CLUCKER_COOP_UPGRADE_1)
            .put(subType, new CluckerCoopFlags(MapTilesLoader.TileSet.CLUCKER_COOP_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CLUCKER_COOP_UPGRADE_2)
            .put(subType, new CluckerCoopFlags(MapTilesLoader.TileSet.CLUCKER_COOP_UPGRADE_2, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.COURIER_STATION).put(subType, new CourierStationFlags(MapTilesLoader.TileSet.COURIER_STATION, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.COURIER_STATION_UPGRADE_1)
            .put(subType, new CourierStationFlags(MapTilesLoader.TileSet.COURIER_STATION_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.COURIER_STATION_UPGRADE_2)
            .put(subType, new CourierStationFlags(MapTilesLoader.TileSet.COURIER_STATION_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CRYLITHIUM_FIRE_PIT)
            .put(subType, new CrylithiumFirePitFlags(MapTilesLoader.TileSet.CRYLITHIUM_FIRE_PIT, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CRYSTAL_GOLEM_COMBOBULATOR)
            .put(subType, new CrystalGolemCombobulatorFlags(MapTilesLoader.TileSet.CRYSTAL_GOLEM_COMBOBULATOR, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CRYSTAL_GOLEM_COMBOBULATOR_UPGRADE_1)
            .put(subType, new CrystalGolemCombobulatorFlags(MapTilesLoader.TileSet.CRYSTAL_GOLEM_COMBOBULATOR_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CRYSTAL_GOLEM_COMBOBULATOR_UPGRADE_2)
            .put(subType, new CrystalGolemCombobulatorFlags(MapTilesLoader.TileSet.CRYSTAL_GOLEM_COMBOBULATOR_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CRYSTAL_GOLEM_COMBOBULATOR_UPGRADE_3_LIGHTNING)
            .put(subType, new CrystalGolemCombobulatorFlags(MapTilesLoader.TileSet.CRYSTAL_GOLEM_COMBOBULATOR_UPGRADE_3_LIGHTNING, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CRYSTAL_GOLEM_COMBOBULATOR_UPGRADE_3_ICE)
            .put(subType, new CrystalGolemCombobulatorFlags(MapTilesLoader.TileSet.CRYSTAL_GOLEM_COMBOBULATOR_UPGRADE_3_ICE, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CRYSTAL_GOLEM_COMBOBULATOR_UPGRADE_3_FIRE)
            .put(subType, new CrystalGolemCombobulatorFlags(MapTilesLoader.TileSet.CRYSTAL_GOLEM_COMBOBULATOR_UPGRADE_3_FIRE, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CRYSTAL_HARVESTRY)
            .put(subType, new CrystalHarvestryFlags(MapTilesLoader.TileSet.CRYSTAL_HARVESTRY, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CRYSTAL_HARVESTRY_UPGRADE_1)
            .put(subType, new CrystalHarvestryFlags(MapTilesLoader.TileSet.CRYSTAL_HARVESTRY_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CRYSTAL_HARVESTRY_UPGRADE_2)
            .put(subType, new CrystalHarvestryFlags(MapTilesLoader.TileSet.CRYSTAL_HARVESTRY_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CRYSTAL_MOTIVATOR)
            .put(subType, new CrystalMotivatorFlags(MapTilesLoader.TileSet.CRYSTAL_MOTIVATOR, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.CRYSTAL_STORAGE).put(subType, new CrystalStorageFlags(MapTilesLoader.TileSet.CRYSTAL_STORAGE, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CRYSTAL_STORAGE_UPGRADE_1)
            .put(subType, new CrystalStorageFlags(MapTilesLoader.TileSet.CRYSTAL_STORAGE_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CRYSTAL_STORAGE_UPGRADE_2)
            .put(subType, new CrystalStorageFlags(MapTilesLoader.TileSet.CRYSTAL_STORAGE_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CRYSTAL_STORAGE_UPGRADE_3)
            .put(subType, new CrystalStorageFlags(MapTilesLoader.TileSet.CRYSTAL_STORAGE_UPGRADE_3, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CRYSTAL_STORAGE_UPGRADE_4)
            .put(subType, new CrystalStorageFlags(MapTilesLoader.TileSet.CRYSTAL_STORAGE_UPGRADE_4, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.CRYSTILLERY).put(subType, new CrystilleryFlags(MapTilesLoader.TileSet.CRYSTILLERY, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CRYSTILLERY_UPGRADE_1)
            .put(subType, new CrystilleryFlags(MapTilesLoader.TileSet.CRYSTILLERY_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CRYSTILLERY_UPGRADE_2)
            .put(subType, new CrystilleryFlags(MapTilesLoader.TileSet.CRYSTILLERY_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CUBE_E_GOLEM_COMBOBULATOR)
            .put(subType, new CubeEGolemCombobulatorFlags(MapTilesLoader.TileSet.CUBE_E_GOLEM_COMBOBULATOR, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CUBE_E_GOLEM_COMBOBULATOR_UPGRADE_1)
            .put(subType, new CubeEGolemCombobulatorFlags(MapTilesLoader.TileSet.CUBE_E_GOLEM_COMBOBULATOR_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CUBE_E_GOLEM_COMBOBULATOR_UPGRADE_2)
            .put(subType, new CubeEGolemCombobulatorFlags(MapTilesLoader.TileSet.CUBE_E_GOLEM_COMBOBULATOR_UPGRADE_2, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.CULLIS_GATE).put(subType, new CullisGateFlags(MapTilesLoader.TileSet.CULLIS_GATE, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.DOGGO_HOUSE).put(subType, new DoggoHouseFlags(MapTilesLoader.TileSet.DOGGO_HOUSE, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.DOGGO_HOUSE_UPGRADE_1)
            .put(subType, new DoggoHouseFlags(MapTilesLoader.TileSet.DOGGO_HOUSE_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.DOGGO_HOUSE_UPGRADE_2)
            .put(subType, new DoggoHouseFlags(MapTilesLoader.TileSet.DOGGO_HOUSE_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.EQUIPMENT_STORAGE)
            .put(subType, new EquipmentStorageFlags(MapTilesLoader.TileSet.EQUIPMENT_STORAGE, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.EQUIPMENT_STORAGE_UPGRADE_1)
            .put(subType, new EquipmentStorageFlags(MapTilesLoader.TileSet.EQUIPMENT_STORAGE_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.EQUIPMENT_STORAGE_UPGRADE_2)
            .put(subType, new EquipmentStorageFlags(MapTilesLoader.TileSet.EQUIPMENT_STORAGE_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.EQUIPMENT_STORAGE_UPGRADE_3)
            .put(subType, new EquipmentStorageFlags(MapTilesLoader.TileSet.EQUIPMENT_STORAGE_UPGRADE_3, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.EQUIPMENT_STORAGE_UPGRADE_4)
            .put(subType, new EquipmentStorageFlags(MapTilesLoader.TileSet.EQUIPMENT_STORAGE_UPGRADE_4, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.ESSENCE_ALTAR).put(subType, new EssenceAltarFlags(MapTilesLoader.TileSet.ESSENCE_ALTAR, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.ESSENCE_ALTAR_UPGRADE_1)
            .put(subType, new EssenceAltarFlags(MapTilesLoader.TileSet.ESSENCE_ALTAR_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.ESSENCE_ALTAR_UPGRADE_2)
            .put(subType, new EssenceAltarFlags(MapTilesLoader.TileSet.ESSENCE_ALTAR_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.ESSENCE_COLLECTOR)
            .put(subType, new EssenceCollectorFlags(MapTilesLoader.TileSet.ESSENCE_COLLECTOR, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.ESSENCE_COLLECTOR_UPGRADE_1)
            .put(subType, new EssenceCollectorFlags(MapTilesLoader.TileSet.ESSENCE_COLLECTOR_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.ESSENCE_COLLECTOR_UPGRADE_2)
            .put(subType, new EssenceCollectorFlags(MapTilesLoader.TileSet.ESSENCE_COLLECTOR_UPGRADE_2, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.FARM).put(subType, new FarmFlags(MapTilesLoader.TileSet.FARM, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.FARM_UPGRADE_1).put(subType, new FarmFlags(MapTilesLoader.TileSet.FARM_UPGRADE_1, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.FARM_UPGRADE_2).put(subType, new FarmFlags(MapTilesLoader.TileSet.FARM_UPGRADE_2, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.FIRE_PIT).put(subType, new FirePitFlags(MapTilesLoader.TileSet.FIRE_PIT, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.FOOD_STORAGE).put(subType, new FoodStorageFlags(MapTilesLoader.TileSet.FOOD_STORAGE, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.FOOD_STORAGE_UPGRADE_1)
            .put(subType, new FoodStorageFlags(MapTilesLoader.TileSet.FOOD_STORAGE_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.FOOD_STORAGE_UPGRADE_2)
            .put(subType, new FoodStorageFlags(MapTilesLoader.TileSet.FOOD_STORAGE_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.FOOD_STORAGE_UPGRADE_3)
            .put(subType, new FoodStorageFlags(MapTilesLoader.TileSet.FOOD_STORAGE_UPGRADE_3, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.FOOD_STORAGE_UPGRADE_4)
            .put(subType, new FoodStorageFlags(MapTilesLoader.TileSet.FOOD_STORAGE_UPGRADE_4, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.FORGE).put(subType, new ForgeFlags(MapTilesLoader.TileSet.FORGE, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.FORGE_UPGRADE_1).put(subType, new ForgeFlags(MapTilesLoader.TileSet.FORGE_UPGRADE_1, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.FORGE_UPGRADE_2).put(subType, new ForgeFlags(MapTilesLoader.TileSet.FORGE_UPGRADE_2, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.GOLD_STORAGE).put(subType, new GoldStorageFlags(MapTilesLoader.TileSet.GOLD_STORAGE, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.GOLD_STORAGE_UPGRADE_1)
            .put(subType, new GoldStorageFlags(MapTilesLoader.TileSet.GOLD_STORAGE_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.GOLD_STORAGE_UPGRADE_2)
            .put(subType, new GoldStorageFlags(MapTilesLoader.TileSet.GOLD_STORAGE_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.GOLD_STORAGE_UPGRADE_3)
            .put(subType, new GoldStorageFlags(MapTilesLoader.TileSet.GOLD_STORAGE_UPGRADE_3, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.GOLD_STORAGE_UPGRADE_4)
            .put(subType, new GoldStorageFlags(MapTilesLoader.TileSet.GOLD_STORAGE_UPGRADE_4, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.HOUSING).put(subType, new HousingFlags(MapTilesLoader.TileSet.HOUSING, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.HOUSING_UPGRADE_1_OCCUPANCY)
            .put(subType, new HousingFlags(MapTilesLoader.TileSet.HOUSING_UPGRADE_1_OCCUPANCY, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.HOUSING_UPGRADE_2_OCCUPANCY)
            .put(subType, new HousingFlags(MapTilesLoader.TileSet.HOUSING_UPGRADE_2_OCCUPANCY, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.HOUSING_UPGRADE_3_OCCUPANCY)
            .put(subType, new HousingFlags(MapTilesLoader.TileSet.HOUSING_UPGRADE_3_OCCUPANCY, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.HOUSING_UPGRADE_4_OCCUPANCY)
            .put(subType, new HousingFlags(MapTilesLoader.TileSet.HOUSING_UPGRADE_4_OCCUPANCY, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.HOUSING_UPGRADE_1_STANDARD)
            .put(subType, new HousingFlags(MapTilesLoader.TileSet.HOUSING_UPGRADE_1_STANDARD, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.HOUSING_UPGRADE_2_STANDARD)
            .put(subType, new HousingFlags(MapTilesLoader.TileSet.HOUSING_UPGRADE_2_STANDARD, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.HOUSING_UPGRADE_3_STANDARD)
            .put(subType, new HousingFlags(MapTilesLoader.TileSet.HOUSING_UPGRADE_3_STANDARD, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.HOUSING_UPGRADE_4_STANDARD)
            .put(subType, new HousingFlags(MapTilesLoader.TileSet.HOUSING_UPGRADE_4_STANDARD, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.HOUSING_UPGRADE_1_QUALITY)
            .put(subType, new HousingFlags(MapTilesLoader.TileSet.HOUSING_UPGRADE_1_QUALITY, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.HOUSING_UPGRADE_2_QUALITY)
            .put(subType, new HousingFlags(MapTilesLoader.TileSet.HOUSING_UPGRADE_2_QUALITY, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.HOUSING_UPGRADE_3_QUALITY)
            .put(subType, new HousingFlags(MapTilesLoader.TileSet.HOUSING_UPGRADE_3_QUALITY, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.HOUSING_UPGRADE_4_QUALITY)
            .put(subType, new HousingFlags(MapTilesLoader.TileSet.HOUSING_UPGRADE_4_QUALITY, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.KEY_SHACK).put(subType, new KeyShackFlags(MapTilesLoader.TileSet.KEY_SHACK, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.KEY_SHACK_UPGRADE_1).put(subType, new KeyShackFlags(MapTilesLoader.TileSet.KEY_SHACK_UPGRADE_1, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.KEY_SHACK_UPGRADE_2).put(subType, new KeyShackFlags(MapTilesLoader.TileSet.KEY_SHACK_UPGRADE_2, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.KEY_SHACK_UPGRADE_3).put(subType, new KeyShackFlags(MapTilesLoader.TileSet.KEY_SHACK_UPGRADE_3, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.KEY_SHACK_UPGRADE_4).put(subType, new KeyShackFlags(MapTilesLoader.TileSet.KEY_SHACK_UPGRADE_4, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.KITCHEN).put(subType, new KitchenFlags(MapTilesLoader.TileSet.KITCHEN, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.KITCHEN_UPGRADE_1).put(subType, new KitchenFlags(MapTilesLoader.TileSet.KITCHEN_UPGRADE_1, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.KITCHEN_UPGRADE_2).put(subType, new KitchenFlags(MapTilesLoader.TileSet.KITCHEN_UPGRADE_2, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.LANDFILL).put(subType, new LandfillFlags(MapTilesLoader.TileSet.LANDFILL, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.LANDFILL_UPGRADE_1).put(subType, new LandfillFlags(MapTilesLoader.TileSet.LANDFILL_UPGRADE_1, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.LANDFILL_UPGRADE_2).put(subType, new LandfillFlags(MapTilesLoader.TileSet.LANDFILL_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.LARGE_CRYLITHIUM_FIRE_PIT)
            .put(subType, new LargeCrylithiumFirePitFlags(MapTilesLoader.TileSet.LARGE_CRYLITHIUM_FIRE_PIT, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.LARGE_FIRE_PIT).put(subType, new LargeFirePitFlags(MapTilesLoader.TileSet.LARGE_FIRE_PIT, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.LARGE_FOUNTAIN).put(subType, new LargeFountainFlags(MapTilesLoader.TileSet.LARGE_FOUNTAIN, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.LIGHTNING_ROD).put(subType, new LightningRodFlags(MapTilesLoader.TileSet.LIGHTNING_ROD, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.LIGHTNING_ROD_UPGRADE_1)
            .put(subType, new LightningRodFlags(MapTilesLoader.TileSet.LIGHTNING_ROD_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.LIGHTNING_ROD_UPGRADE_2)
            .put(subType, new LightningRodFlags(MapTilesLoader.TileSet.LIGHTNING_ROD_UPGRADE_2, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.LOOT_BOX).put(subType, new LootBoxFlags(MapTilesLoader.TileSet.LOOT_BOX, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.LUMBER_MILL).put(subType, new LumberMillFlags(MapTilesLoader.TileSet.LUMBER_MILL, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.LUMBER_MILL_UPGRADE_1)
            .put(subType, new LumberMillFlags(MapTilesLoader.TileSet.LUMBER_MILL_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.LUMBER_MILL_UPGRADE_2)
            .put(subType, new LumberMillFlags(MapTilesLoader.TileSet.LUMBER_MILL_UPGRADE_2, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.LUMBER_SHACK).put(subType, new LumberShackFlags(MapTilesLoader.TileSet.LUMBER_SHACK, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.LUMBER_SHACK_UPGRADE_1)
            .put(subType, new LumberShackFlags(MapTilesLoader.TileSet.LUMBER_SHACK_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.LUMBER_SHACK_UPGRADE_2)
            .put(subType, new LumberShackFlags(MapTilesLoader.TileSet.LUMBER_SHACK_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.MAINTENANCE_BUILDING)
            .put(subType, new MaintenanceBuildingFlags(MapTilesLoader.TileSet.MAINTENANCE_BUILDING, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.MAINTENANCE_BUILDING_UPGRADE_1)
            .put(subType, new MaintenanceBuildingFlags(MapTilesLoader.TileSet.MAINTENANCE_BUILDING_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.MAINTENANCE_BUILDING_UPGRADE_2)
            .put(subType, new MaintenanceBuildingFlags(MapTilesLoader.TileSet.MAINTENANCE_BUILDING_UPGRADE_2, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.MARKETPLACE).put(subType, new MarketplaceFlags(MapTilesLoader.TileSet.MARKETPLACE, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.MARKETPLACE_UPGRADE_1)
            .put(subType, new MarketplaceFlags(MapTilesLoader.TileSet.MARKETPLACE_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.MARKETPLACE_UPGRADE_2)
            .put(subType, new MarketplaceFlags(MapTilesLoader.TileSet.MARKETPLACE_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.MIGRATION_WAY_STATION)
            .put(subType, new MigrationWayStationFlags(MapTilesLoader.TileSet.MIGRATION_WAY_STATION, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.MIGRATION_WAY_STATION_UPGRADE_1)
            .put(subType, new MigrationWayStationFlags(MapTilesLoader.TileSet.MIGRATION_WAY_STATION_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.MIGRATION_WAY_STATION_UPGRADE_2)
            .put(subType, new MigrationWayStationFlags(MapTilesLoader.TileSet.MIGRATION_WAY_STATION_UPGRADE_2, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.MINERAL_STORAGE).put(subType, new MineralStorageFlags(MapTilesLoader.TileSet.MINERAL_STORAGE, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.MINERAL_STORAGE_UPGRADE_1)
            .put(subType, new MineralStorageFlags(MapTilesLoader.TileSet.MINERAL_STORAGE_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.MINERAL_STORAGE_UPGRADE_2)
            .put(subType, new MineralStorageFlags(MapTilesLoader.TileSet.MINERAL_STORAGE_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.MINERAL_STORAGE_UPGRADE_3)
            .put(subType, new MineralStorageFlags(MapTilesLoader.TileSet.MINERAL_STORAGE_UPGRADE_3, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.MINERAL_STORAGE_UPGRADE_4)
            .put(subType, new MineralStorageFlags(MapTilesLoader.TileSet.MINERAL_STORAGE_UPGRADE_4, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.MINING_FACILITY).put(subType, new MiningFacilityFlags(MapTilesLoader.TileSet.MINING_FACILITY, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.MINING_FACILITY_UPGRADE_1)
            .put(subType, new MiningFacilityFlags(MapTilesLoader.TileSet.MINING_FACILITY_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.MINING_FACILITY_UPGRADE_2)
            .put(subType, new MiningFacilityFlags(MapTilesLoader.TileSet.MINING_FACILITY_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.MISCELLANEOUS_STORAGE)
            .put(subType, new MiscellaneousStorageFlags(MapTilesLoader.TileSet.MISCELLANEOUS_STORAGE, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.MISCELLANEOUS_STORAGE_UPGRADE_1)
            .put(subType, new MiscellaneousStorageFlags(MapTilesLoader.TileSet.MISCELLANEOUS_STORAGE_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.MISCELLANEOUS_STORAGE_UPGRADE_2)
            .put(subType, new MiscellaneousStorageFlags(MapTilesLoader.TileSet.MISCELLANEOUS_STORAGE_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.MISCELLANEOUS_STORAGE_UPGRADE_3)
            .put(subType, new MiscellaneousStorageFlags(MapTilesLoader.TileSet.MISCELLANEOUS_STORAGE_UPGRADE_3, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.MISCELLANEOUS_STORAGE_UPGRADE_4)
            .put(subType, new MiscellaneousStorageFlags(MapTilesLoader.TileSet.MISCELLANEOUS_STORAGE_UPGRADE_4, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.OUTPOST).put(subType, new OutpostFlags(MapTilesLoader.TileSet.OUTPOST, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.OUTPOST_UPGRADE_1).put(subType, new OutpostFlags(MapTilesLoader.TileSet.OUTPOST_UPGRADE_1, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.OUTPOST_UPGRADE_2).put(subType, new OutpostFlags(MapTilesLoader.TileSet.OUTPOST_UPGRADE_2, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.PROCESSOR).put(subType, new ProcessorFlags(MapTilesLoader.TileSet.PROCESSOR, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.PROCESSOR_UPGRADE_1).put(subType, new ProcessorFlags(MapTilesLoader.TileSet.PROCESSOR_UPGRADE_1, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.PROCESSOR_UPGRADE_2).put(subType, new ProcessorFlags(MapTilesLoader.TileSet.PROCESSOR_UPGRADE_2, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.RAIN_CATCHER).put(subType, new RainCatcherFlags(MapTilesLoader.TileSet.RAIN_CATCHER, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.RAIN_CATCHER_UPGRADE_1)
            .put(subType, new RainCatcherFlags(MapTilesLoader.TileSet.RAIN_CATCHER_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.RAIN_CATCHER_UPGRADE_2)
            .put(subType, new RainCatcherFlags(MapTilesLoader.TileSet.RAIN_CATCHER_UPGRADE_2, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.RELIQUARY).put(subType, new ReliquaryFlags(MapTilesLoader.TileSet.RELIQUARY, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.RELIQUARY_UPGRADE_1).put(subType, new ReliquaryFlags(MapTilesLoader.TileSet.RELIQUARY_UPGRADE_1, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.RELIQUARY_UPGRADE_2).put(subType, new ReliquaryFlags(MapTilesLoader.TileSet.RELIQUARY_UPGRADE_2, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.ROCK_STORAGE).put(subType, new RockStorageFlags(MapTilesLoader.TileSet.ROCK_STORAGE, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.ROCK_STORAGE_UPGRADE_1)
            .put(subType, new RockStorageFlags(MapTilesLoader.TileSet.ROCK_STORAGE_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.ROCK_STORAGE_UPGRADE_2)
            .put(subType, new RockStorageFlags(MapTilesLoader.TileSet.ROCK_STORAGE_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.ROCK_STORAGE_UPGRADE_3)
            .put(subType, new RockStorageFlags(MapTilesLoader.TileSet.ROCK_STORAGE_UPGRADE_3, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.ROCK_STORAGE_UPGRADE_4)
            .put(subType, new RockStorageFlags(MapTilesLoader.TileSet.ROCK_STORAGE_UPGRADE_4, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.ROCK_TUMBLER).put(subType, new RockTumblerFlags(MapTilesLoader.TileSet.ROCK_TUMBLER, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.ROCK_TUMBLER_UPGRADE_1)
            .put(subType, new RockTumblerFlags(MapTilesLoader.TileSet.ROCK_TUMBLER_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.ROCK_TUMBLER_UPGRADE_2)
            .put(subType, new RockTumblerFlags(MapTilesLoader.TileSet.ROCK_TUMBLER_UPGRADE_2, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.SMALL_FOUNTAIN).put(subType, new SmallFountainFlags(MapTilesLoader.TileSet.SMALL_FOUNTAIN, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.STONE_CUTTERY).put(subType, new StoneCutteryFlags(MapTilesLoader.TileSet.STONE_CUTTERY, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.STONE_CUTTERY_UPGRADE_1)
            .put(subType, new StoneCutteryFlags(MapTilesLoader.TileSet.STONE_CUTTERY_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.STONE_CUTTERY_UPGRADE_2)
            .put(subType, new StoneCutteryFlags(MapTilesLoader.TileSet.STONE_CUTTERY_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.STONE_GOLEM_COMBOBULATOR)
            .put(subType, new StoneGolemCombobulatorFlags(MapTilesLoader.TileSet.STONE_GOLEM_COMBOBULATOR, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.STONE_GOLEM_COMBOBULATOR_UPGRADE_1)
            .put(subType, new StoneGolemCombobulatorFlags(MapTilesLoader.TileSet.STONE_GOLEM_COMBOBULATOR_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.STONE_GOLEM_COMBOBULATOR_UPGRADE_2)
            .put(subType, new StoneGolemCombobulatorFlags(MapTilesLoader.TileSet.STONE_GOLEM_COMBOBULATOR_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.STONE_GOLEM_COMBOBULATOR_UPGRADE_3_LIGHTNING)
            .put(subType, new StoneGolemCombobulatorFlags(MapTilesLoader.TileSet.STONE_GOLEM_COMBOBULATOR_UPGRADE_3_LIGHTNING, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.STONE_GOLEM_COMBOBULATOR_UPGRADE_3_ICE)
            .put(subType, new StoneGolemCombobulatorFlags(MapTilesLoader.TileSet.STONE_GOLEM_COMBOBULATOR_UPGRADE_3_ICE, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.STONE_GOLEM_COMBOBULATOR_UPGRADE_3_FIRE)
            .put(subType, new StoneGolemCombobulatorFlags(MapTilesLoader.TileSet.STONE_GOLEM_COMBOBULATOR_UPGRADE_3_FIRE, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.TOOLSMITHY).put(subType, new ToolsmithyFlags(MapTilesLoader.TileSet.TOOLSMITHY, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.TOOLSMITHY_UPGRADE_1)
            .put(subType, new ToolsmithyFlags(MapTilesLoader.TileSet.TOOLSMITHY_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.TOOLSMITHY_UPGRADE_2)
            .put(subType, new ToolsmithyFlags(MapTilesLoader.TileSet.TOOLSMITHY_UPGRADE_2, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.TRASH_CAN).put(subType, new TrashCanFlags(MapTilesLoader.TileSet.TRASH_CAN, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.TRASH_CAN_UPGRADE_1).put(subType, new TrashCanFlags(MapTilesLoader.TileSet.TRASH_CAN_UPGRADE_1, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.TRASH_CAN_UPGRADE_2).put(subType, new TrashCanFlags(MapTilesLoader.TileSet.TRASH_CAN_UPGRADE_2, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.TRASHY_CUBE_PILE).put(subType, new TrashyCubePileFlags(MapTilesLoader.TileSet.TRASHY_CUBE_PILE, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.WATER_PURIFIER).put(subType, new WaterPurifierFlags(MapTilesLoader.TileSet.WATER_PURIFIER, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.WATER_PURIFIER_UPGRADE_1)
            .put(subType, new WaterPurifierFlags(MapTilesLoader.TileSet.WATER_PURIFIER_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.WATER_PURIFIER_UPGRADE_2)
            .put(subType, new WaterPurifierFlags(MapTilesLoader.TileSet.WATER_PURIFIER_UPGRADE_2, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.WAY_MAKER_SHACK).put(subType, new WayMakerShackFlags(MapTilesLoader.TileSet.WAY_MAKER_SHACK, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.WAY_MAKER_SHACK_UPGRADE_1)
            .put(subType, new WayMakerShackFlags(MapTilesLoader.TileSet.WAY_MAKER_SHACK_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.WAY_MAKER_SHACK_UPGRADE_2)
            .put(subType, new WayMakerShackFlags(MapTilesLoader.TileSet.WAY_MAKER_SHACK_UPGRADE_2, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.WELL).put(subType, new WellFlags(MapTilesLoader.TileSet.WELL, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.WELL_UPGRADE_1).put(subType, new WellFlags(MapTilesLoader.TileSet.WELL_UPGRADE_1, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.WELL_UPGRADE_2).put(subType, new WellFlags(MapTilesLoader.TileSet.WELL_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.WOOD_GOLEM_COMBOBULATOR)
            .put(subType, new WoodGolemCombobulatorFlags(MapTilesLoader.TileSet.WOOD_GOLEM_COMBOBULATOR, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.WOOD_GOLEM_COMBOBULATOR_UPGRADE_1)
            .put(subType, new WoodGolemCombobulatorFlags(MapTilesLoader.TileSet.WOOD_GOLEM_COMBOBULATOR_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.WOOD_GOLEM_COMBOBULATOR_UPGRADE_2)
            .put(subType, new WoodGolemCombobulatorFlags(MapTilesLoader.TileSet.WOOD_GOLEM_COMBOBULATOR_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.WOOD_GOLEM_COMBOBULATOR_UPGRADE_3_LIGHTNING)
            .put(subType, new WoodGolemCombobulatorFlags(MapTilesLoader.TileSet.WOOD_GOLEM_COMBOBULATOR_UPGRADE_3_LIGHTNING, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.WOOD_GOLEM_COMBOBULATOR_UPGRADE_3_ICE)
            .put(subType, new WoodGolemCombobulatorFlags(MapTilesLoader.TileSet.WOOD_GOLEM_COMBOBULATOR_UPGRADE_3_ICE, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.WOOD_GOLEM_COMBOBULATOR_UPGRADE_3_POISON)
            .put(subType, new WoodGolemCombobulatorFlags(MapTilesLoader.TileSet.WOOD_GOLEM_COMBOBULATOR_UPGRADE_3_POISON, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.WOOD_STORAGE).put(subType, new WoodStorageFlags(MapTilesLoader.TileSet.WOOD_STORAGE, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.WOOD_STORAGE_UPGRADE_1)
            .put(subType, new WoodStorageFlags(MapTilesLoader.TileSet.WOOD_STORAGE_UPGRADE_1, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.WOOD_STORAGE_UPGRADE_2)
            .put(subType, new WoodStorageFlags(MapTilesLoader.TileSet.WOOD_STORAGE_UPGRADE_2, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.WOOD_STORAGE_UPGRADE_3)
            .put(subType, new WoodStorageFlags(MapTilesLoader.TileSet.WOOD_STORAGE_UPGRADE_3, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.WOOD_STORAGE_UPGRADE_4)
            .put(subType, new WoodStorageFlags(MapTilesLoader.TileSet.WOOD_STORAGE_UPGRADE_4, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.CRYLITHIUM_WALL).put(subType, new CrylithiumWallFlags(MapTilesLoader.TileSet.CRYLITHIUM_WALL, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CRYLITHIUM_WALL_GATE_NS)
            .put(subType, new CrylithiumWallGateNSFlags(MapTilesLoader.TileSet.CRYLITHIUM_WALL_GATE_NS, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CRYLITHIUM_WALL_GATE_WE)
            .put(subType, new CrylithiumWallGateWEFlags(MapTilesLoader.TileSet.CRYLITHIUM_WALL_GATE_WE, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.CRYLITHIUM_CURTAIN_WALL)
            .put(subType, new CrylithiumCurtainWallFlags(MapTilesLoader.TileSet.CRYLITHIUM_CURTAIN_WALL, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.CURTAIN_WALL).put(subType, new CurtainWallFlags(MapTilesLoader.TileSet.CURTAIN_WALL, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.GOD_WALL).put(subType, new GodWallFlags(MapTilesLoader.TileSet.GOD_WALL, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.STONE_WALL).put(subType, new StoneWallFlags(MapTilesLoader.TileSet.STONE_WALL, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.STONE_WALL_GATE_NS)
            .put(subType, new StoneWallGateNSFlags(MapTilesLoader.TileSet.STONE_WALL_GATE_NS, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.STONE_WALL_GATE_WE)
            .put(subType, new StoneWallGateWEFlags(MapTilesLoader.TileSet.STONE_WALL_GATE_WE, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.TRASHY_CUBE_WALL).put(subType, new TrashyCubeWallFlags(MapTilesLoader.TileSet.TRASHY_CUBE_WALL, subType));
         this.objectFlags.get(MapTilesLoader.TileSet.WOOD_FENCE).put(subType, new WoodFenceFlags(MapTilesLoader.TileSet.WOOD_FENCE, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.WOOD_FENCE_GATE_NS)
            .put(subType, new WoodFenceGateNSFlags(MapTilesLoader.TileSet.WOOD_FENCE_GATE_NS, subType));
         this.objectFlags
            .get(MapTilesLoader.TileSet.WOOD_FENCE_GATE_WE)
            .put(subType, new WoodFenceGateWEFlags(MapTilesLoader.TileSet.WOOD_FENCE_GATE_WE, subType));
      }

      for (MapTilesLoader.TileSet type : MapTilesLoader.TileSet.values()) {
         if (this.objectFlags.get(type).size() == 0) {
            this.objectFlags.remove(type);
         }
      }
   }

   public ObjectFlags getObjectFlags(MapTilesLoader.TileSet baseType, ObjectBase.ObjectSubType subType) {
      return this.objectFlags.get(baseType).get(subType);
   }

   public HashMap<MapTilesLoader.TileSet, HashMap<ObjectBase.ObjectSubType, ObjectFlags>> getObjectFlagsList() {
      return this.objectFlags;
   }
}
