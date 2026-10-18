package rtr.map;

import rtr.utilities.IntKeyMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import r2d.image.SpriteSheet;
import rtr.ImageLoader;
import rtr.map.tileSets.TileSetBase;
import rtr.map.tileSets.objects.AmmoStorage;
import rtr.map.tileSets.objects.AncientCullisGate;
import rtr.map.tileSets.objects.AncientRadiancePool;
import rtr.map.tileSets.objects.Ancillary;
import rtr.map.tileSets.objects.AnimalPen;
import rtr.map.tileSets.objects.Armorsmithy;
import rtr.map.tileSets.objects.AttractTower;
import rtr.map.tileSets.objects.BallistaTower;
import rtr.map.tileSets.objects.BanishTower;
import rtr.map.tileSets.objects.Bottler;
import rtr.map.tileSets.objects.BowTower;
import rtr.map.tileSets.objects.Bowyer;
import rtr.map.tileSets.objects.BulletTower;
import rtr.map.tileSets.objects.Burner;
import rtr.map.tileSets.objects.Castle;
import rtr.map.tileSets.objects.Clinic;
import rtr.map.tileSets.objects.CluckerCoop;
import rtr.map.tileSets.objects.CourierStation;
import rtr.map.tileSets.objects.CrylithiumCurtainWall;
import rtr.map.tileSets.objects.CrylithiumFirePit;
import rtr.map.tileSets.objects.CrylithiumWall;
import rtr.map.tileSets.objects.CrylithiumWallGateNS;
import rtr.map.tileSets.objects.CrylithiumWallGateWE;
import rtr.map.tileSets.objects.CrystalGolemCombobulator;
import rtr.map.tileSets.objects.CrystalHarvestry;
import rtr.map.tileSets.objects.CrystalMotivator;
import rtr.map.tileSets.objects.CrystalStorage;
import rtr.map.tileSets.objects.Crystillery;
import rtr.map.tileSets.objects.CubeEGolemCombobulator;
import rtr.map.tileSets.objects.CullisGate;
import rtr.map.tileSets.objects.CurtainWall;
import rtr.map.tileSets.objects.DoggoHouse;
import rtr.map.tileSets.objects.ElementalBoltTower;
import rtr.map.tileSets.objects.EquipmentStorage;
import rtr.map.tileSets.objects.EssenceAltar;
import rtr.map.tileSets.objects.EssenceCollector;
import rtr.map.tileSets.objects.Farm;
import rtr.map.tileSets.objects.FirePit;
import rtr.map.tileSets.objects.FoodStorage;
import rtr.map.tileSets.objects.Forge;
import rtr.map.tileSets.objects.GodTower;
import rtr.map.tileSets.objects.GodWall;
import rtr.map.tileSets.objects.GoldStorage;
import rtr.map.tileSets.objects.Housing;
import rtr.map.tileSets.objects.KeyShack;
import rtr.map.tileSets.objects.Kitchen;
import rtr.map.tileSets.objects.Landfill;
import rtr.map.tileSets.objects.LargeCrylithiumFirePit;
import rtr.map.tileSets.objects.LargeFirePit;
import rtr.map.tileSets.objects.LargeFountain;
import rtr.map.tileSets.objects.LightningRod;
import rtr.map.tileSets.objects.LootBox;
import rtr.map.tileSets.objects.LumberMill;
import rtr.map.tileSets.objects.LumberShack;
import rtr.map.tileSets.objects.MaintenanceBuilding;
import rtr.map.tileSets.objects.Marketplace;
import rtr.map.tileSets.objects.MigrationWayStation;
import rtr.map.tileSets.objects.MineralStorage;
import rtr.map.tileSets.objects.MiningFacility;
import rtr.map.tileSets.objects.MiscellaneousStorage;
import rtr.map.tileSets.objects.Outpost;
import rtr.map.tileSets.objects.PhantomDartTower;
import rtr.map.tileSets.objects.Processor;
import rtr.map.tileSets.objects.RainCatcher;
import rtr.map.tileSets.objects.RangerLodge;
import rtr.map.tileSets.objects.RecombobulatorTower;
import rtr.map.tileSets.objects.Reliquary;
import rtr.map.tileSets.objects.RockStorage;
import rtr.map.tileSets.objects.RockTumbler;
import rtr.map.tileSets.objects.SlingTower;
import rtr.map.tileSets.objects.SmallFountain;
import rtr.map.tileSets.objects.SprayTower;
import rtr.map.tileSets.objects.StaticTower;
import rtr.map.tileSets.objects.StoneCuttery;
import rtr.map.tileSets.objects.StoneGolemCombobulator;
import rtr.map.tileSets.objects.StoneWall;
import rtr.map.tileSets.objects.StoneWallGateNS;
import rtr.map.tileSets.objects.StoneWallGateWE;
import rtr.map.tileSets.objects.Toolsmithy;
import rtr.map.tileSets.objects.TrashCan;
import rtr.map.tileSets.objects.TrashyCubePile;
import rtr.map.tileSets.objects.TrashyCubeWall;
import rtr.map.tileSets.objects.WaterPurifier;
import rtr.map.tileSets.objects.WayMakerShack;
import rtr.map.tileSets.objects.Well;
import rtr.map.tileSets.objects.WoodFence;
import rtr.map.tileSets.objects.WoodFenceGateNS;
import rtr.map.tileSets.objects.WoodFenceGateWE;
import rtr.map.tileSets.objects.WoodGolemCombobulator;
import rtr.map.tileSets.objects.WoodStorage;
import rtr.map.tileSets.objects.corrupted.CorruptedFireBowTower;
import rtr.map.tileSets.objects.corrupted.CorruptedFirePhantomDartTower;
import rtr.map.tileSets.objects.corrupted.CorruptedFirePit;
import rtr.map.tileSets.objects.corrupted.CorruptedGraveyardLarge;
import rtr.map.tileSets.objects.corrupted.CorruptedGraveyardMedium;
import rtr.map.tileSets.objects.corrupted.CorruptedGraveyardSmall;
import rtr.map.tileSets.objects.corrupted.CorruptedIceBowTower;
import rtr.map.tileSets.objects.corrupted.CorruptedIcePhantomDartTower;
import rtr.map.tileSets.objects.corrupted.CorruptedLargeFirePit;
import rtr.map.tileSets.objects.corrupted.CorruptedLightningBowTower;
import rtr.map.tileSets.objects.corrupted.CorruptedLightningPhantomDartTower;
import rtr.map.tileSets.objects.corrupted.CorruptedPoisonBowTower;
import rtr.map.tileSets.objects.corrupted.CorruptedPoisonPhantomDartTower;
import rtr.map.tileSets.roads.CobbleAndBoardRoad;
import rtr.map.tileSets.roads.CobbleAndBoardRoadDebris;
import rtr.map.tileSets.roads.CobbleAndLogPath;
import rtr.map.tileSets.roads.CobbleAndLogPathDebris;
import rtr.map.tileSets.roads.CutStoneAndBoardRoad;
import rtr.map.tileSets.roads.CutStoneAndBoardRoadDebris;
import rtr.map.tileSets.roads.LogPath;
import rtr.map.tileSets.roads.LogPathDebris;
import rtr.map.tileSets.roads.Path;
import rtr.map.tileSets.terrain.Bricks;
import rtr.map.tileSets.terrain.BricksAccent;
import rtr.map.tileSets.terrain.CactusGreen;
import rtr.map.tileSets.terrain.CactusGreenAccent;
import rtr.map.tileSets.terrain.CactusGreenCollected;
import rtr.map.tileSets.terrain.Carrots;
import rtr.map.tileSets.terrain.CarrotsAccent;
import rtr.map.tileSets.terrain.CarrotsCollected;
import rtr.map.tileSets.terrain.Corruption;
import rtr.map.tileSets.terrain.CorruptionAccent;
import rtr.map.tileSets.terrain.CrystalsBlue;
import rtr.map.tileSets.terrain.CrystalsBlueAccent;
import rtr.map.tileSets.terrain.CrystalsBlueCollected;
import rtr.map.tileSets.terrain.CrystalsGreen;
import rtr.map.tileSets.terrain.CrystalsGreenAccent;
import rtr.map.tileSets.terrain.CrystalsGreenCollected;
import rtr.map.tileSets.terrain.CrystalsPurple;
import rtr.map.tileSets.terrain.CrystalsPurpleAccent;
import rtr.map.tileSets.terrain.CrystalsPurpleCollected;
import rtr.map.tileSets.terrain.CrystalsRed;
import rtr.map.tileSets.terrain.CrystalsRedAccent;
import rtr.map.tileSets.terrain.CrystalsRedCollected;
import rtr.map.tileSets.terrain.DirtBrown;
import rtr.map.tileSets.terrain.DirtBrownAccent;
import rtr.map.tileSets.terrain.DirtDarkBrown;
import rtr.map.tileSets.terrain.DirtDarkBrownAccent;
import rtr.map.tileSets.terrain.DirtLightBrown;
import rtr.map.tileSets.terrain.DirtLightBrownAccent;
import rtr.map.tileSets.terrain.FlowersBlack;
import rtr.map.tileSets.terrain.FlowersBlackAccent;
import rtr.map.tileSets.terrain.FlowersBlue;
import rtr.map.tileSets.terrain.FlowersBlueAccent;
import rtr.map.tileSets.terrain.FlowersPurple;
import rtr.map.tileSets.terrain.FlowersPurpleAccent;
import rtr.map.tileSets.terrain.FlowersRed;
import rtr.map.tileSets.terrain.FlowersRedAccent;
import rtr.map.tileSets.terrain.FlowersWhite;
import rtr.map.tileSets.terrain.FlowersWhiteAccent;
import rtr.map.tileSets.terrain.FlowersYellow;
import rtr.map.tileSets.terrain.FlowersYellowAccent;
import rtr.map.tileSets.terrain.GrassEmeraldGreen;
import rtr.map.tileSets.terrain.GrassEmeraldGreenAccent;
import rtr.map.tileSets.terrain.GrassGreen;
import rtr.map.tileSets.terrain.GrassGreenAccent;
import rtr.map.tileSets.terrain.GrassTeal;
import rtr.map.tileSets.terrain.GrassTealAccent;
import rtr.map.tileSets.terrain.GrassYellowBrown;
import rtr.map.tileSets.terrain.GrassYellowBrownAccent;
import rtr.map.tileSets.terrain.GravelBlue;
import rtr.map.tileSets.terrain.GravelBlueAccent;
import rtr.map.tileSets.terrain.GravelGray;
import rtr.map.tileSets.terrain.GravelGrayAccent;
import rtr.map.tileSets.terrain.GravelRed;
import rtr.map.tileSets.terrain.GravelRedAccent;
import rtr.map.tileSets.terrain.Lava;
import rtr.map.tileSets.terrain.LavaAccent;
import rtr.map.tileSets.terrain.Melons;
import rtr.map.tileSets.terrain.MelonsAccent;
import rtr.map.tileSets.terrain.MelonsCollected;
import rtr.map.tileSets.terrain.Mushrooms;
import rtr.map.tileSets.terrain.MushroomsAccent;
import rtr.map.tileSets.terrain.MushroomsCollected;
import rtr.map.tileSets.terrain.Potatoes;
import rtr.map.tileSets.terrain.PotatoesAccent;
import rtr.map.tileSets.terrain.PotatoesCollected;
import rtr.map.tileSets.terrain.PotatoesHoly;
import rtr.map.tileSets.terrain.PotatoesHolyAccent;
import rtr.map.tileSets.terrain.PotatoesHolyCollected;
import rtr.map.tileSets.terrain.RockBlack;
import rtr.map.tileSets.terrain.RockBlackAccent;
import rtr.map.tileSets.terrain.RockBlackCollected;
import rtr.map.tileSets.terrain.RockBrown;
import rtr.map.tileSets.terrain.RockBrownAccent;
import rtr.map.tileSets.terrain.RockBrownCollected;
import rtr.map.tileSets.terrain.RockGray;
import rtr.map.tileSets.terrain.RockGrayAccent;
import rtr.map.tileSets.terrain.RockGrayCollected;
import rtr.map.tileSets.terrain.RockRed;
import rtr.map.tileSets.terrain.RockRedAccent;
import rtr.map.tileSets.terrain.RockRedCollected;
import rtr.map.tileSets.terrain.RockWhite;
import rtr.map.tileSets.terrain.RockWhiteAccent;
import rtr.map.tileSets.terrain.RockWhiteCollected;
import rtr.map.tileSets.terrain.SandBlack;
import rtr.map.tileSets.terrain.SandBlackAccent;
import rtr.map.tileSets.terrain.SandRed;
import rtr.map.tileSets.terrain.SandRedAccent;
import rtr.map.tileSets.terrain.SandTan;
import rtr.map.tileSets.terrain.SandTanAccent;
import rtr.map.tileSets.terrain.SandstoneBlack;
import rtr.map.tileSets.terrain.SandstoneBlackAccent;
import rtr.map.tileSets.terrain.SandstoneBlackCollected;
import rtr.map.tileSets.terrain.SandstoneRed;
import rtr.map.tileSets.terrain.SandstoneRedAccent;
import rtr.map.tileSets.terrain.SandstoneRedCollected;
import rtr.map.tileSets.terrain.SandstoneTan;
import rtr.map.tileSets.terrain.SandstoneTanAccent;
import rtr.map.tileSets.terrain.SandstoneTanCollected;
import rtr.map.tileSets.terrain.SnowWhite;
import rtr.map.tileSets.terrain.SnowWhiteAccent;
import rtr.map.tileSets.terrain.Tar;
import rtr.map.tileSets.terrain.TarAccent;
import rtr.map.tileSets.terrain.Tiles;
import rtr.map.tileSets.terrain.TilesAccent;
import rtr.map.tileSets.terrain.TreesBlue;
import rtr.map.tileSets.terrain.TreesBlueAccent;
import rtr.map.tileSets.terrain.TreesBlueCollected;
import rtr.map.tileSets.terrain.TreesBlueDead;
import rtr.map.tileSets.terrain.TreesBrown;
import rtr.map.tileSets.terrain.TreesBrownAccent;
import rtr.map.tileSets.terrain.TreesBrownCollected;
import rtr.map.tileSets.terrain.TreesBrownDead;
import rtr.map.tileSets.terrain.TreesDarkGreen;
import rtr.map.tileSets.terrain.TreesDarkGreenAccent;
import rtr.map.tileSets.terrain.TreesDarkGreenCollected;
import rtr.map.tileSets.terrain.TreesDarkGreenDead;
import rtr.map.tileSets.terrain.TreesGreen;
import rtr.map.tileSets.terrain.TreesGreenAccent;
import rtr.map.tileSets.terrain.TreesGreenCollected;
import rtr.map.tileSets.terrain.TreesGreenDead;
import rtr.map.tileSets.terrain.TreesLavender;
import rtr.map.tileSets.terrain.TreesLavenderAccent;
import rtr.map.tileSets.terrain.TreesLavenderCollected;
import rtr.map.tileSets.terrain.TreesLavenderDead;
import rtr.map.tileSets.terrain.TreesPaleBlue;
import rtr.map.tileSets.terrain.TreesPaleBlueAccent;
import rtr.map.tileSets.terrain.TreesPaleBlueCollected;
import rtr.map.tileSets.terrain.TreesPaleBlueDead;
import rtr.map.tileSets.terrain.TreesRed;
import rtr.map.tileSets.terrain.TreesRedAccent;
import rtr.map.tileSets.terrain.TreesRedCollected;
import rtr.map.tileSets.terrain.TreesRedDead;
import rtr.map.tileSets.terrain.Turnips;
import rtr.map.tileSets.terrain.TurnipsAccent;
import rtr.map.tileSets.terrain.TurnipsCollected;
import rtr.map.tileSets.terrain.Water;
import rtr.map.tileSets.terrain.WaterAccent;
import rtr.map.tileSets.terrain.WaterDry;
import rtr.map.tileSets.terrain.WaterDryAccent;
import rtr.map.tileSets.terrain.WaterFrozen;
import rtr.map.tileSets.terrain.WaterFrozenAccent;
import rtr.map.tileSets.terrain.WaterPartiallyDry;
import rtr.map.tileSets.terrain.WaterPartiallyDryAccent;
import rtr.map.tileSets.terrain.WaterPartiallyDryFrozen;
import rtr.map.tileSets.terrain.WaterPartiallyDryFrozenAccent;
import rtr.map.tileSets.terrain.WaterPartiallyFrozen;
import rtr.map.tileSets.terrain.WaterPartiallyFrozenAccent;
import rtr.resources.ResourceModule;
import rtr.utilities.Utilities;

public class MapTilesLoader {
   private IntKeyMap<Image> tiles = new IntKeyMap<>();
   private IntKeyMap<byte[][]> collisionTiles = new IntKeyMap<>();
   private IntKeyMap<byte[][]> depthTiles = new IntKeyMap<>();
   private IntKeyMap<byte[][]> particleTiles = new IntKeyMap<>();
   private IntKeyMap<byte[][][]> shadowTiles = new IntKeyMap<>();
   private IntKeyMap<Image> objects = new IntKeyMap<>();
   private IntKeyMap<MapTilesLoader.TileSet> GIDToTileSet = new IntKeyMap<>();
   private IntKeyMap<Boolean> isLoaded = new IntKeyMap<>();
   private Properties properties = new Properties();
   private IntKeyMap<String> tileName = new IntKeyMap<>();
   private IntKeyMap<String> tileDescription = new IntKeyMap<>();
   private IntKeyMap<Byte> tileBlockLarge = new IntKeyMap<>();
   private IntKeyMap<Byte> tileBlockSmall = new IntKeyMap<>();
   private IntKeyMap<Byte> tileBlockRoad = new IntKeyMap<>();
   private IntKeyMap<Byte> tileBlockGate = new IntKeyMap<>();
   private IntKeyMap<Byte> tileBlockLight = new IntKeyMap<>();
   private IntKeyMap<Byte> tileBlockWater = new IntKeyMap<>();
   private IntKeyMap<Byte> tileBlockCorruption = new IntKeyMap<>();
   private IntKeyMap<Byte> tileBlockGhost = new IntKeyMap<>();
   private IntKeyMap<Integer> tileMovementCost = new IntKeyMap<>();
   private IntKeyMap<Byte> tileDepth = new IntKeyMap<>();
   private IntKeyMap<Byte> tileParticles = new IntKeyMap<>();
   private IntKeyMap<Byte> tileShadow = new IntKeyMap<>();
   private IntKeyMap<Byte> tileLight = new IntKeyMap<>();
   private IntKeyMap<Byte> tileLayer = new IntKeyMap<>();
   private IntKeyMap<MapTilesLoader.TileSetType> tileType = new IntKeyMap<>();
   private IntKeyMap<Byte> tileCollision = new IntKeyMap<>();
   private IntKeyMap<ResourceModule.ResourceType> tileResourceType = new IntKeyMap<>();
   private IntKeyMap<Byte> tileResourceAmount = new IntKeyMap<>();
   private IntKeyMap<Byte> tileResourceAmountMax = new IntKeyMap<>();
   private IntKeyMap<ResourceModule.ResourceColorSet> tileResourceColorSet = new IntKeyMap<>();
   private IntKeyMap<Byte> tileCanClear = new IntKeyMap<>();
   private IntKeyMap<Byte> tileInvulnerable = new IntKeyMap<>();
   private IntKeyMap<Byte> tileFrozen = new IntKeyMap<>();
   private IntKeyMap<Byte> tileWater = new IntKeyMap<>();
   private IntKeyMap<Byte> tileCourierGolemParking = new IntKeyMap<>();
   private IntKeyMap<Byte> tileInteractPoint = new IntKeyMap<>();
   private IntKeyMap<Byte> tileFunctionalPoint = new IntKeyMap<>();
   private IntKeyMap<Byte> tilePrayPoint = new IntKeyMap<>();
   private IntKeyMap<Byte> tileVesselPoint = new IntKeyMap<>();
   private IntKeyMap<Byte> tileCrops = new IntKeyMap<>();
   private IntKeyMap<Color> tileMiniMapColor = new IntKeyMap<>();
   private IntKeyMap<MapTilesLoader.TileSet> tileCanRegrow = new IntKeyMap<>();
   private IntKeyMap<Integer> tileRegrowRate = new IntKeyMap<>();
   private IntKeyMap<Byte> tileRegrowOnlyMotivated = new IntKeyMap<>();
   private IntKeyMap<Byte> tileRegrowIgnoreWeather = new IntKeyMap<>();
   private IntKeyMap<MapTilesLoader.TileSet> tileCanSpread = new IntKeyMap<>();
   private IntKeyMap<Integer> tileSpreadRate = new IntKeyMap<>();
   private IntKeyMap<Byte> tileSpreadOnlyMotivated = new IntKeyMap<>();
   private IntKeyMap<Byte> tileSpreadIgnoreWeather = new IntKeyMap<>();
   private IntKeyMap<Integer> tileEssenceAmount = new IntKeyMap<>();
   private IntKeyMap<Byte> tilePropHeight = new IntKeyMap<>();
   private IntKeyMap<Byte> tilePropWidth = new IntKeyMap<>();
   private Color[] colorNorth;
   private Color[] colorEast;
   private Color[] colorSouth;
   private Color[] colorWest;
   private Color[] colorAll;
   private static final byte COLLISION_NONE = 0;
   private static final byte COLLISION_NORTH_LOW = 1;
   private static final byte COLLISION_NORTH_MEDIUM = 2;
   private static final byte COLLISION_NORTH_HIGH = 3;
   private static final byte COLLISION_EAST_LOW = 4;
   private static final byte COLLISION_EAST_MEDIUM = 5;
   private static final byte COLLISION_EAST_HIGH = 6;
   private static final byte COLLISION_SOUTH_LOW = 7;
   private static final byte COLLISION_SOUTH_MEDIUM = 8;
   private static final byte COLLISION_SOUTH_HIGH = 9;
   private static final byte COLLISION_WEST_LOW = 10;
   private static final byte COLLISION_WEST_MEDIUM = 11;
   private static final byte COLLISION_WEST_HIGH = 12;
   private static final byte COLLISION_ALL_LOW = 13;
   private static final byte COLLISION_ALL_MEDIUM = 14;
   private static final byte COLLISION_ALL_HIGH = 15;
   private static final Color fireRedVeryLow = new Color(100, 50, 50);
   private static final Color fireRedLow = new Color(150, 50, 50);
   private static final Color fireRedMedium = new Color(200, 50, 50);
   private static final Color fireRedHigh = new Color(250, 50, 50);
   private static final Color fireYellowVeryLow = new Color(100, 100, 50);
   private static final Color fireYellowLow = new Color(150, 150, 50);
   private static final Color fireYellowMedium = new Color(200, 200, 50);
   private static final Color fireYellowHigh = new Color(250, 250, 50);
   private static final Color fireGreenVeryLow = new Color(50, 100, 50);
   private static final Color fireGreenLow = new Color(50, 150, 50);
   private static final Color fireGreenMedium = new Color(50, 200, 50);
   private static final Color fireGreenHigh = new Color(50, 250, 50);
   private static final Color fireBlueVeryLow = new Color(50, 50, 100);
   private static final Color fireBlueLow = new Color(50, 50, 150);
   private static final Color fireBlueMedium = new Color(50, 50, 200);
   private static final Color fireBlueHigh = new Color(50, 50, 250);
   private static final Color firePurpleVeryLow = new Color(100, 50, 100);
   private static final Color firePurpleLow = new Color(150, 50, 150);
   private static final Color firePurpleMedium = new Color(200, 50, 200);
   private static final Color firePurpleHigh = new Color(250, 50, 250);
   private static final Color magicRedLow = new Color(250, 100, 100);
   private static final Color magicRedMedium = new Color(250, 150, 150);
   private static final Color magicRedHigh = new Color(250, 200, 200);
   private static final Color magicYellowLow = new Color(250, 250, 100);
   private static final Color magicYellowMedium = new Color(250, 250, 150);
   private static final Color magicYellowHigh = new Color(250, 250, 200);
   private static final Color magicGreenLow = new Color(100, 250, 100);
   private static final Color magicGreenMedium = new Color(150, 250, 150);
   private static final Color magicGreenHigh = new Color(200, 250, 200);
   private static final Color magicBlueLow = new Color(100, 100, 250);
   private static final Color magicBlueMedium = new Color(150, 150, 250);
   private static final Color magicBlueHigh = new Color(200, 200, 250);
   private static final Color magicPurpleLow = new Color(250, 100, 250);
   private static final Color magicPurpleMedium = new Color(250, 150, 250);
   private static final Color magicPurpleHigh = new Color(250, 200, 250);
   private static final Color smokeLow = new Color(150, 150, 150);
   private static final Color smokeMedium = new Color(200, 200, 200);
   private static final Color smokeHigh = new Color(250, 250, 250);
   private static final Color redSparklesVeryLow = new Color(100, 0, 0);
   private static final Color redSparklesLow = new Color(150, 0, 0);
   private static final Color redSparklesMedium = new Color(200, 0, 0);
   private static final Color redSparklesHigh = new Color(250, 0, 0);
   private static final Color greenSparklesVeryLow = new Color(0, 100, 0);
   private static final Color greenSparklesLow = new Color(0, 150, 0);
   private static final Color greenSparklesMedium = new Color(0, 200, 0);
   private static final Color greenSparklesHigh = new Color(0, 250, 0);
   private static final Color blueSparklesVeryLow = new Color(0, 0, 100);
   private static final Color blueSparklesLow = new Color(0, 0, 150);
   private static final Color blueSparklesMedium = new Color(0, 0, 200);
   private static final Color blueSparklesHigh = new Color(0, 0, 250);
   private static final Color purpleSparklesVeryLow = new Color(100, 0, 100);
   private static final Color purpleSparklesLow = new Color(150, 0, 150);
   private static final Color purpleSparklesMedium = new Color(200, 0, 200);
   private static final Color purpleSparklesHigh = new Color(250, 0, 250);
   private static final Color yellowSparklesVeryLow = new Color(100, 100, 0);
   private static final Color yellowSparklesLow = new Color(150, 150, 0);
   private static final Color yellowSparklesMedium = new Color(200, 200, 0);
   private static final Color yellowSparklesHigh = new Color(250, 250, 0);
   private static final Color magicFireRedLow = new Color(240, 100, 100);
   private static final Color magicFireRedMedium = new Color(240, 150, 150);
   private static final Color magicFireRedHigh = new Color(240, 200, 200);
   private static final Color magicFireYellowLow = new Color(240, 240, 100);
   private static final Color magicFireYellowMedium = new Color(240, 240, 150);
   private static final Color magicFireYellowHigh = new Color(240, 240, 200);
   private static final Color magicFireGreenLow = new Color(100, 240, 100);
   private static final Color magicFireGreenMedium = new Color(150, 240, 150);
   private static final Color magicFireGreenHigh = new Color(200, 240, 200);
   private static final Color magicFireBlueLow = new Color(100, 100, 240);
   private static final Color magicFireBlueMedium = new Color(150, 150, 240);
   private static final Color magicFireBlueHigh = new Color(200, 200, 240);
   private static final Color magicFirePurpleLow = new Color(240, 100, 240);
   private static final Color magicFirePurpleMedium = new Color(240, 150, 240);
   private static final Color magicFirePurpleHigh = new Color(240, 200, 240);
   private static int TERRAIN_TILE_RANGE = 10000;
   private static int TERRAIN_TILE_SPACING = 200000;
   public static int TERRAIN_TILE_SUB = 500;
   private static int OBJECT_TILE_RANGE = 200000000;
   private static int OBJECT_TILE_SPACING = 200000;
   private static int OBJECT_TILE_SUB = 10000;

   private static int calcTerrainTileID(int rangePosition, int subPosition) {
      int tileOut = TERRAIN_TILE_RANGE;
      tileOut += TERRAIN_TILE_SPACING * rangePosition;
      return tileOut + TERRAIN_TILE_SUB * subPosition;
   }

   private static int calcObjectTileID(int rangePosition, int subPosition) {
      int tileOut = OBJECT_TILE_RANGE;
      tileOut += OBJECT_TILE_SPACING * rangePosition;
      return tileOut + OBJECT_TILE_SUB * subPosition;
   }

   MapTilesLoader() throws SlickException {
      if (this.colorNorth == null) {
         this.colorNorth = new Color[3];
         this.colorNorth[0] = new Color(50, 0, 0, 255);
         this.colorNorth[1] = new Color(150, 0, 0, 255);
         this.colorNorth[2] = new Color(250, 0, 0, 255);
      }

      if (this.colorEast == null) {
         this.colorEast = new Color[3];
         this.colorEast[0] = new Color(50, 50, 0, 255);
         this.colorEast[1] = new Color(150, 150, 0, 255);
         this.colorEast[2] = new Color(250, 250, 0, 255);
      }

      if (this.colorSouth == null) {
         this.colorSouth = new Color[3];
         this.colorSouth[0] = new Color(0, 50, 0, 255);
         this.colorSouth[1] = new Color(0, 150, 0, 255);
         this.colorSouth[2] = new Color(0, 250, 0, 255);
      }

      if (this.colorWest == null) {
         this.colorWest = new Color[3];
         this.colorWest[0] = new Color(0, 0, 50, 255);
         this.colorWest[1] = new Color(0, 0, 150, 255);
         this.colorWest[2] = new Color(0, 0, 250, 255);
      }

      if (this.colorAll == null) {
         this.colorAll = new Color[3];
         this.colorAll[0] = new Color(50, 50, 50, 255);
         this.colorAll[1] = new Color(150, 150, 150, 255);
         this.colorAll[2] = new Color(250, 250, 250, 255);
      }

      MapTilesLoader.TileSet[] var4;
      for (MapTilesLoader.TileSet t : MapTilesLoader.TileSet.values()) {
         if (t.getTileSetTemplate().getType() != MapTilesLoader.TileSetType.OBJECT && t.getTileSetTemplate().getType() != MapTilesLoader.TileSetType.WALL) {
            this.GIDToTileSet.put(t.getTileSetTemplate().getTileGID(), t);
         } else {
            MapTilesLoader.TileSetObjectPhase[] var8;
            for (MapTilesLoader.TileSetObjectPhase p : MapTilesLoader.TileSetObjectPhase.values()) {
               this.GIDToTileSet.put(t.getTileSetTemplate().getTileGID() + p.getTileOffset(), t);
            }
         }
      }
   }

   public void checkAndloadTileset(int tileID) throws SlickException {
      int globalID = this.getTileSetGID(tileID);
      if (this.isLoaded.get(globalID) == null || !this.isLoaded.get(globalID)) {
         Utilities.startTimer();
         MapTilesLoader.TileSet t = this.getTileSetByTileID(globalID);
         if (t == null) {
            System.out.println("Failed to Load Tileset: (" + globalID + ") ");
            return;
         }

         TileSetBase template = t.getTileSetTemplate();
         template.initialize();
         if (template.getType() != MapTilesLoader.TileSetType.OBJECT) {
            try {
               this.loadTerrain(template);
            } catch (FileNotFoundException var6) {
               var6.printStackTrace();
            }
         } else {
            this.loadObject(template);
         }

         System.out.print("Loaded Tileset: " + t + " (" + globalID + ") ");
         Utilities.endTimer();
      }
   }

   private void loadTerrain(TileSetBase tileSetBase) throws SlickException, FileNotFoundException {
      this.properties.clear();
      Image baseImage = ImageLoader.getImage("res/maps/base/" + tileSetBase.getFileName() + ".png");
      SpriteSheet baseSheet = new SpriteSheet(baseImage, 16, 16);
      int startGID = tileSetBase.getTileGID();
      int tileSheetH = baseSheet.getHorizontalCount();
      int tileSheetV = baseSheet.getVerticalCount();
      this.isLoaded.put(startGID, true);

      for (int y = 0; y < tileSheetV; y++) {
         for (int x = 0; x < tileSheetH; x++) {
            int tileID = y * tileSheetH + x;
            this.tiles.put(startGID + tileID, baseSheet.getSprite(x, y));
            this.tileName.put(startGID + tileID, tileSetBase.getName());
            this.tileDescription.put(startGID + tileID, tileSetBase.getDescription());
            this.tileLayer.put(startGID + tileID, tileSetBase.getLayer());
            this.tileType.put(startGID + tileID, tileSetBase.getType());
            if (tileSetBase.getResourceType() != null) {
               this.tileResourceType.put(startGID + tileID, tileSetBase.getResourceType());
               this.tileResourceAmount.put(startGID + tileID, tileSetBase.getResourceAmount());
               this.tileResourceAmountMax.put(startGID + tileID, tileSetBase.getResourceAmountMax());
               this.tileResourceColorSet.put(startGID + tileID, tileSetBase.getResourceColorSet());
            }

            this.tileMiniMapColor.put(startGID + tileID, tileSetBase.getMiniMapColor());
            if (tileSetBase.canRegrow() != null) {
               this.tileCanRegrow.put(startGID + tileID, tileSetBase.canRegrow());
               this.tileRegrowRate.put(startGID + tileID, tileSetBase.getRegrowRate());
               if (tileSetBase.getRegrowOnlyMotivated()) {
                  this.tileRegrowOnlyMotivated.put(startGID + tileID, (byte)1);
               }

               if (tileSetBase.getRegrowIgnoreWeather()) {
                  this.tileRegrowIgnoreWeather.put(startGID + tileID, (byte)1);
               }
            }

            if (tileSetBase.canSpread() != null) {
               this.tileCanSpread.put(startGID + tileID, tileSetBase.canSpread());
               this.tileSpreadRate.put(startGID + tileID, tileSetBase.getSpreadRate());
               if (tileSetBase.getSpreadOnlyMotivated()) {
                  this.tileSpreadOnlyMotivated.put(startGID + tileID, (byte)1);
               }

               if (tileSetBase.getSpreadIgnoreWeather()) {
                  this.tileSpreadIgnoreWeather.put(startGID + tileID, (byte)1);
               }
            }

            if (tileSetBase.getEssenceAmount() > 0) {
               this.tileEssenceAmount.put(startGID + tileID, tileSetBase.getEssenceAmount());
            }

            this.tilePropHeight.put(startGID + tileID, (byte)tileSetBase.getObjectHeight());
            this.tilePropWidth.put(startGID + tileID, (byte)tileSetBase.getObjectWidth());
         }
      }

      File file = new File("res/maps/data/" + tileSetBase.getFileName() + ".png");
      if (file.exists()) {
         FileInputStream fileStream = new FileInputStream(file);
         if (fileStream != null) {
            Image dataImage = ImageLoader.getImage("res/maps/data/" + tileSetBase.getFileName() + ".png");
            SpriteSheet dataSheet = new SpriteSheet(dataImage, 16, 16);

            for (int y = 0; y < tileSheetV; y++) {
               for (int x = 0; x < tileSheetH; x++) {
                  int tileIDx = y * tileSheetH + x;
                  this.generateBasicTileData(tileSetBase, x, y, startGID, tileIDx, dataSheet);
               }
            }

            dataSheet.flushPixelData();
            ImageLoader.unloadImage("res/maps/data/" + tileSetBase.getFileName() + ".png");
         }

         try {
            if (fileStream != null) {
               fileStream.close();
            }
         } catch (IOException var18) {
            var18.printStackTrace();
         }
      }

      file = new File("res/maps/collision/" + tileSetBase.getFileName() + ".png");
      if (file.exists()) {
         FileInputStream fileStream = new FileInputStream(file);
         if (fileStream != null) {
            Image collisionImage = ImageLoader.getImage("res/maps/collision/" + tileSetBase.getFileName() + ".png");
            SpriteSheet collisionSheet = new SpriteSheet(collisionImage, 16, 16);

            for (int y = 0; y < tileSheetV; y++) {
               for (int x = 0; x < tileSheetH; x++) {
                  int tileIDx = y * tileSheetH + x;
                  this.generateCollisionData(tileSetBase, x, y, startGID, tileIDx, collisionSheet);
               }
            }

            collisionSheet.flushPixelData();
            ImageLoader.unloadImage("res/maps/collision/" + tileSetBase.getFileName() + ".png");
         }

         try {
            if (fileStream != null) {
               fileStream.close();
            }
         } catch (IOException var17) {
            var17.printStackTrace();
         }
      }

      file = new File("res/maps/depth/" + tileSetBase.getFileName() + ".png");
      if (file.exists()) {
         FileInputStream fileStream = new FileInputStream(file);
         if (fileStream != null) {
            Image depthImage = ImageLoader.getImage("res/maps/depth/" + tileSetBase.getFileName() + ".png");
            SpriteSheet depthSheet = new SpriteSheet(depthImage, 16, 16);

            for (int y = 0; y < tileSheetV; y++) {
               for (int x = 0; x < tileSheetH; x++) {
                  int tileIDx = y * tileSheetH + x;
                  this.generateDepthData(tileSetBase, x, y, startGID, tileIDx, depthSheet);
               }
            }

            depthSheet.flushPixelData();
            ImageLoader.unloadImage("res/maps/depth/" + tileSetBase.getFileName() + ".png");
         }

         try {
            if (fileStream != null) {
               fileStream.close();
            }
         } catch (IOException var16) {
            var16.printStackTrace();
         }
      }

      file = new File("res/maps/particle/" + tileSetBase.getFileName() + ".png");
      if (file.exists()) {
         FileInputStream fileStream = new FileInputStream(file);
         if (fileStream != null) {
            Image particleImage = ImageLoader.getImage("res/maps/particle/" + tileSetBase.getFileName() + ".png");
            SpriteSheet particleSheet = new SpriteSheet(particleImage, 16, 16);

            for (int y = 0; y < tileSheetV; y++) {
               for (int x = 0; x < tileSheetH; x++) {
                  int tileIDx = y * tileSheetH + x;
                  this.generateParticleData(tileSetBase, x, y, startGID, tileIDx, particleSheet);
               }
            }

            particleSheet.flushPixelData();
            ImageLoader.unloadImage("res/maps/particle/" + tileSetBase.getFileName() + ".png");
         }

         try {
            if (fileStream != null) {
               fileStream.close();
            }
         } catch (IOException var15) {
            var15.printStackTrace();
         }
      }

      file = new File("res/maps/shadow/" + tileSetBase.getFileName() + ".png");
      if (file.exists()) {
         FileInputStream fileStream = new FileInputStream(file);
         if (fileStream != null) {
            Image shadowImage = ImageLoader.getImage("res/maps/shadow/" + tileSetBase.getFileName() + ".png");
            SpriteSheet shadowSheet = new SpriteSheet(shadowImage, 16, 16);

            for (int y = 0; y < tileSheetV; y++) {
               for (int x = 0; x < tileSheetH; x++) {
                  int tileIDx = y * tileSheetH + x;
                  this.generateShadowData(tileSetBase, x, y, startGID, tileIDx, shadowSheet);
               }
            }

            shadowSheet.flushPixelData();
            ImageLoader.unloadImage("res/maps/shadow/" + tileSetBase.getFileName() + ".png");
         }

         try {
            if (fileStream != null) {
               fileStream.close();
            }
         } catch (IOException var14) {
            var14.printStackTrace();
         }
      }
   }

   private void loadObject(TileSetBase tileSetBase) throws SlickException {
      this.properties.clear();
      int startGID = tileSetBase.getTileGID();
      int currentGID = 0;
      int objW = tileSetBase.getObjectWidth();
      int objH = tileSetBase.getObjectHeight();
      boolean hasFill = false;
      Image baseImage = ImageLoader.getImage("res/maps/base/" + tileSetBase.getFileName() + ".png");
      if (baseImage.getWidth() > objW * 16) {
         hasFill = true;
      }

      int hMax = 1;
      int vMax = 8;
      if (hasFill) {
         hMax = 2;
         vMax = 6;
      }

      for (int spriteY = 0; spriteY < vMax; spriteY++) {
         for (int spriteX = 0; spriteX < hMax; spriteX++) {
            currentGID = this.tempGetCurrentGID(spriteX, spriteY, startGID, hMax == 2, false);
            Image subBaseImage = baseImage.getSubImage(spriteX * objW * 16, spriteY * objH * 16, objW * 16, objH * 16);
            SpriteSheet baseSheet = new SpriteSheet(subBaseImage, 16, 16);
            int tileSheetH = baseSheet.getHorizontalCount();
            int tileSheetV = baseSheet.getVerticalCount();
            this.objects.put(currentGID, subBaseImage);
            this.isLoaded.put(currentGID, true);

            for (int y = 0; y < tileSheetV; y++) {
               for (int x = 0; x < tileSheetH; x++) {
                  int tileID = y * tileSheetH + x;
                  this.tiles.put(currentGID + tileID, baseSheet.getSprite(x, y));
                  this.tileName.put(currentGID + tileID, tileSetBase.getName());
                  this.tileDescription.put(currentGID + tileID, tileSetBase.getDescription());
                  this.tileLayer.put(currentGID + tileID, tileSetBase.getLayer());
                  this.tileType.put(currentGID + tileID, tileSetBase.getType());
                  if (tileSetBase.getResourceType() != null) {
                     this.tileResourceType.put(currentGID + tileID, tileSetBase.getResourceType());
                     this.tileResourceAmount.put(currentGID + tileID, tileSetBase.getResourceAmount());
                     this.tileResourceAmountMax.put(currentGID + tileID, tileSetBase.getResourceAmountMax());
                     this.tileResourceColorSet.put(currentGID + tileID, tileSetBase.getResourceColorSet());
                  }

                  this.tileMiniMapColor.put(currentGID + tileID, tileSetBase.getMiniMapColor());
                  if (tileSetBase.canRegrow() != null) {
                     this.tileCanRegrow.put(currentGID + tileID, tileSetBase.canRegrow());
                     this.tileRegrowRate.put(currentGID + tileID, tileSetBase.getRegrowRate());
                  }

                  if (tileSetBase.canSpread() != null) {
                     this.tileCanSpread.put(currentGID + tileID, tileSetBase.canSpread());
                     this.tileSpreadRate.put(currentGID + tileID, tileSetBase.getSpreadRate());
                  }

                  if (tileSetBase.getEssenceAmount() > 0) {
                     this.tileEssenceAmount.put(currentGID + tileID, tileSetBase.getEssenceAmount());
                  }

                  this.tilePropHeight.put(currentGID + tileID, (byte)tileSetBase.getObjectHeight());
                  this.tilePropWidth.put(currentGID + tileID, (byte)tileSetBase.getObjectWidth());
               }
            }
         }
      }

      File dataCheck = new File("res/maps/data/" + tileSetBase.getFileName() + ".png");
      Image dataImage = null;
      if (dataCheck.exists()) {
         dataImage = ImageLoader.getImage("res/maps/data/" + tileSetBase.getFileName() + ".png");
      }

      File collisionCheck = new File("res/maps/collision/" + tileSetBase.getFileName() + ".png");
      Image collisionImage = null;
      if (collisionCheck.exists()) {
         collisionImage = ImageLoader.getImage("res/maps/collision/" + tileSetBase.getFileName() + ".png");
      }

      File depthCheck = new File("res/maps/depth/" + tileSetBase.getFileName() + ".png");
      Image depthImage = null;
      if (depthCheck.exists()) {
         depthImage = ImageLoader.getImage("res/maps/depth/" + tileSetBase.getFileName() + ".png");
      }

      File particleCheck = new File("res/maps/particle/" + tileSetBase.getFileName() + ".png");
      Image particleImage = null;
      if (particleCheck.exists()) {
         particleImage = ImageLoader.getImage("res/maps/particle/" + tileSetBase.getFileName() + ".png");
      }

      File shadowCheck = new File("res/maps/shadow/" + tileSetBase.getFileName() + ".png");
      Image shadowImage = null;
      if (shadowCheck.exists()) {
         shadowImage = ImageLoader.getImage("res/maps/shadow/" + tileSetBase.getFileName() + ".png");
      }

      if (dataCheck.exists()) {
         for (int spriteY = 0; spriteY < vMax; spriteY++) {
            for (int spriteX = 0; spriteX < hMax; spriteX++) {
               currentGID = this.tempGetCurrentGID(spriteX, spriteY, startGID, hMax == 2, false);
               Image subDataImage = dataImage.getSubImage(spriteX * objW * 16, spriteY * objH * 16, objW * 16, objH * 16);
               SpriteSheet dataSheet = new SpriteSheet(subDataImage, 16, 16);
               int tileSheetH = dataSheet.getHorizontalCount();
               int tileSheetV = dataSheet.getVerticalCount();

               for (int y = 0; y < tileSheetV; y++) {
                  for (int x = 0; x < tileSheetH; x++) {
                     int tileIDx = y * tileSheetH + x;
                     this.generateBasicTileData(tileSetBase, x, y, currentGID, tileIDx, dataSheet);
                  }
               }

               dataSheet.flushPixelData();
            }
         }
      }

      if (collisionCheck.exists()) {
         for (int spriteY = 0; spriteY < vMax; spriteY++) {
            for (int spriteX = 0; spriteX < hMax; spriteX++) {
               currentGID = this.tempGetCurrentGID(spriteX, spriteY, startGID, hMax == 2, false);
               Image subCollisionImage = collisionImage.getSubImage(spriteX * objW * 16, spriteY * objH * 16, objW * 16, objH * 16);
               SpriteSheet collisionSheet = new SpriteSheet(subCollisionImage, 16, 16);
               int tileSheetH = collisionSheet.getHorizontalCount();
               int tileSheetV = collisionSheet.getVerticalCount();

               for (int y = 0; y < tileSheetV; y++) {
                  for (int x = 0; x < tileSheetH; x++) {
                     int tileIDx = y * tileSheetH + x;
                     this.generateCollisionData(tileSetBase, x, y, currentGID, tileIDx, collisionSheet);
                  }
               }

               collisionSheet.flushPixelData();
            }
         }
      }

      if (depthCheck.exists()) {
         for (int spriteY = 0; spriteY < vMax; spriteY++) {
            for (int spriteX = 0; spriteX < hMax; spriteX++) {
               currentGID = this.tempGetCurrentGID(spriteX, spriteY, startGID, hMax == 2, false);
               Image subDepthImage = depthImage.getSubImage(spriteX * objW * 16, spriteY * objH * 16, objW * 16, objH * 16);
               SpriteSheet depthSheet = new SpriteSheet(subDepthImage, 16, 16);
               int tileSheetH = depthSheet.getHorizontalCount();
               int tileSheetV = depthSheet.getVerticalCount();

               for (int y = 0; y < tileSheetV; y++) {
                  for (int x = 0; x < tileSheetH; x++) {
                     int tileIDx = y * tileSheetH + x;
                     this.generateDepthData(tileSetBase, x, y, currentGID, tileIDx, depthSheet);
                  }
               }

               depthSheet.flushPixelData();
            }
         }
      }

      if (particleCheck.exists()) {
         for (int spriteY = 0; spriteY < vMax; spriteY++) {
            for (int spriteX = 0; spriteX < hMax; spriteX++) {
               currentGID = this.tempGetCurrentGID(spriteX, spriteY, startGID, hMax == 2, false);
               Image subParticleImage = particleImage.getSubImage(spriteX * objW * 16, spriteY * objH * 16, objW * 16, objH * 16);
               SpriteSheet particleSheet = new SpriteSheet(subParticleImage, 16, 16);
               int tileSheetH = particleSheet.getHorizontalCount();
               int tileSheetV = particleSheet.getVerticalCount();

               for (int y = 0; y < tileSheetV; y++) {
                  for (int x = 0; x < tileSheetH; x++) {
                     int tileIDx = y * tileSheetH + x;
                     this.generateParticleData(tileSetBase, x, y, currentGID, tileIDx, particleSheet);
                  }
               }

               particleSheet.flushPixelData();
            }
         }
      }

      if (shadowCheck.exists()) {
         for (int spriteY = 0; spriteY < vMax; spriteY++) {
            for (int spriteX = 0; spriteX < hMax; spriteX++) {
               currentGID = this.tempGetCurrentGID(spriteX, spriteY, startGID, hMax == 2, false);
               Image subShadowImage = shadowImage.getSubImage(spriteX * objW * 16, spriteY * objH * 16, objW * 16, objH * 16);
               SpriteSheet shadowSheet = new SpriteSheet(subShadowImage, 16, 16);
               int tileSheetH = shadowSheet.getHorizontalCount();
               int tileSheetV = shadowSheet.getVerticalCount();

               for (int y = 0; y < tileSheetV; y++) {
                  for (int x = 0; x < tileSheetH; x++) {
                     int tileIDx = y * tileSheetH + x;
                     this.generateShadowData(tileSetBase, x, y, currentGID, tileIDx, shadowSheet);
                  }
               }

               shadowSheet.flushPixelData();
            }
         }
      }

      if (dataCheck.exists()) {
         ImageLoader.unloadImage("res/maps/data/" + tileSetBase.getFileName() + ".png");
      }

      if (collisionCheck.exists()) {
         ImageLoader.unloadImage("res/maps/collision/" + tileSetBase.getFileName() + ".png");
      }

      if (depthCheck.exists()) {
         ImageLoader.unloadImage("res/maps/depth/" + tileSetBase.getFileName() + ".png");
      }

      if (particleCheck.exists()) {
         ImageLoader.unloadImage("res/maps/particle/" + tileSetBase.getFileName() + ".png");
      }

      if (shadowCheck.exists()) {
         ImageLoader.unloadImage("res/maps/shadow/" + tileSetBase.getFileName() + ".png");
      }
   }

   private int tempGetCurrentGID(int spriteX, int spriteY, int startGID, boolean buildingHasFill, boolean isWall) {
      if (buildingHasFill) {
         if (spriteX == 0 && spriteY == 0) {
            return startGID + MapTilesLoader.TileSetObjectPhase.FILL_PHASE_4.getTileOffset();
         }

         if (spriteX == 0 && spriteY == 1) {
            return startGID + MapTilesLoader.TileSetObjectPhase.FILL_PHASE_3.getTileOffset();
         }

         if (spriteX == 0 && spriteY == 2) {
            return startGID + MapTilesLoader.TileSetObjectPhase.FILL_PHASE_2.getTileOffset();
         }

         if (spriteX == 0 && spriteY == 3) {
            return startGID + MapTilesLoader.TileSetObjectPhase.FILL_PHASE_1.getTileOffset();
         }

         if (spriteX == 0 && spriteY == 4) {
            return startGID;
         }

         if (spriteX == 0 && spriteY == 5) {
            return startGID + MapTilesLoader.TileSetObjectPhase.ABANDONED.getTileOffset();
         }

         if (spriteX == 1 && spriteY == 0) {
            return startGID + MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_5.getTileOffset();
         }

         if (spriteX == 1 && spriteY == 1) {
            return startGID + MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_4.getTileOffset();
         }

         if (spriteX == 1 && spriteY == 2) {
            return startGID + MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_3.getTileOffset();
         }

         if (spriteX == 1 && spriteY == 3) {
            return startGID + MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_2.getTileOffset();
         }

         if (spriteX == 1 && spriteY == 4) {
            return startGID + MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_1.getTileOffset();
         }

         if (spriteX == 1 && spriteY == 5) {
            return startGID + MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_OUTLINE.getTileOffset();
         }
      } else if (isWall) {
         if (spriteY == 0) {
            return startGID;
         }

         if (spriteY == 1) {
            return startGID + MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_1.getTileOffset();
         }

         if (spriteY == 2) {
            return startGID + MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_OUTLINE.getTileOffset();
         }

         if (spriteY == 3) {
            return startGID + MapTilesLoader.TileSetObjectPhase.ABANDONED.getTileOffset();
         }
      } else {
         if (spriteY == 0) {
            return startGID;
         }

         if (spriteY == 1) {
            return startGID + MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_5.getTileOffset();
         }

         if (spriteY == 2) {
            return startGID + MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_4.getTileOffset();
         }

         if (spriteY == 3) {
            return startGID + MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_3.getTileOffset();
         }

         if (spriteY == 4) {
            return startGID + MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_2.getTileOffset();
         }

         if (spriteY == 5) {
            return startGID + MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_1.getTileOffset();
         }

         if (spriteY == 6) {
            return startGID + MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_OUTLINE.getTileOffset();
         }

         if (spriteY == 7) {
            return startGID + MapTilesLoader.TileSetObjectPhase.ABANDONED.getTileOffset();
         }
      }

      return startGID;
   }

   private void generateBasicTileData(TileSetBase tileSetBase, int x, int y, int startGID, int tileID, SpriteSheet dataSheet) {
      Color colorCheck = null;
      colorCheck = dataSheet.getColor(x * 16, y * 16);
      if (colorCheck.getAlphaByte() > 0) {
         this.tileBlockLarge.put(startGID + tileID, (byte)1);
      }

      colorCheck = dataSheet.getColor(x * 16, y * 16 + 1);
      if (colorCheck.getAlphaByte() > 0) {
         this.tileBlockSmall.put(startGID + tileID, (byte)1);
      }

      colorCheck = dataSheet.getColor(x * 16, y * 16 + 2);
      if (colorCheck.getAlphaByte() > 0) {
         this.tileBlockRoad.put(startGID + tileID, (byte)1);
      }

      colorCheck = dataSheet.getColor(x * 16, y * 16 + 3);
      if (colorCheck.getAlphaByte() > 0) {
         this.tileBlockGate.put(startGID + tileID, (byte)1);
      }

      colorCheck = dataSheet.getColor(x * 16, y * 16 + 4);
      if (colorCheck.getAlphaByte() > 0) {
         this.tileBlockLight.put(startGID + tileID, (byte)1);
      }

      colorCheck = dataSheet.getColor(x * 16, y * 16 + 5);
      if (colorCheck.getAlphaByte() > 0) {
         this.tileBlockWater.put(startGID + tileID, (byte)1);
      }

      colorCheck = dataSheet.getColor(x * 16, y * 16 + 6);
      if (colorCheck.getAlphaByte() > 0) {
         this.tileBlockCorruption.put(startGID + tileID, (byte)1);
      }

      colorCheck = dataSheet.getColor(x * 16, y * 16 + 7);
      if (colorCheck.getAlphaByte() > 0) {
         this.tileBlockGhost.put(startGID + tileID, (byte)1);
      }

      colorCheck = dataSheet.getColor(x * 16 + 1, y * 16);
      if (colorCheck.getAlphaByte() > 0) {
         this.tileLight.put(startGID + tileID, (byte)(colorCheck.getRedByte() / 10));
      }

      colorCheck = dataSheet.getColor(x * 16 + 2, y * 16);
      if (colorCheck.getAlphaByte() > 0) {
         this.tileInvulnerable.put(startGID + tileID, (byte)1);
      }

      colorCheck = dataSheet.getColor(x * 16 + 3, y * 16);
      if (colorCheck.getAlphaByte() > 0) {
         this.tileCanClear.put(startGID + tileID, (byte)1);
      }

      colorCheck = dataSheet.getColor(x * 16 + 4, y * 16);
      if (colorCheck.getAlphaByte() > 0) {
         this.tileCrops.put(startGID + tileID, (byte)1);
      }

      colorCheck = dataSheet.getColor(x * 16 + 5, y * 16);
      if (colorCheck.getAlphaByte() > 0) {
         if (colorCheck.getRedByte() > 0) {
            this.tileMovementCost.put(startGID + tileID, colorCheck.getRedByte() * 10);
         } else {
            this.tileMovementCost.put(startGID + tileID, colorCheck.getGreenByte() * 10 * -1);
         }
      }

      colorCheck = dataSheet.getColor(x * 16 + 6, y * 16);
      if (colorCheck.getAlphaByte() > 0) {
         this.tileFrozen.put(startGID + tileID, (byte)1);
      }

      colorCheck = dataSheet.getColor(x * 16 + 7, y * 16);
      if (colorCheck.getAlphaByte() > 0) {
         this.tileWater.put(startGID + tileID, (byte)1);
      }

      colorCheck = dataSheet.getColor(x * 16 + 8, y * 16);
      if (colorCheck.getAlphaByte() > 0) {
         this.tileCourierGolemParking.put(startGID + tileID, (byte)1);
      }

      colorCheck = dataSheet.getColor(x * 16 + 9, y * 16);
      if (colorCheck.getAlphaByte() > 0) {
         this.tileInteractPoint.put(startGID + tileID, (byte)1);
      }

      colorCheck = dataSheet.getColor(x * 16 + 10, y * 16);
      if (colorCheck.getAlphaByte() > 0) {
         this.tileFunctionalPoint.put(startGID + tileID, (byte)1);
      }

      colorCheck = dataSheet.getColor(x * 16 + 11, y * 16);
      if (colorCheck.getAlphaByte() > 0) {
         this.tilePrayPoint.put(startGID + tileID, (byte)1);
      }

      colorCheck = dataSheet.getColor(x * 16 + 12, y * 16);
      if (colorCheck.getAlphaByte() > 0) {
         this.tileVesselPoint.put(startGID + tileID, (byte)1);
      }
   }

   private void generateCollisionData(TileSetBase tileSetBase, int x, int y, int startGID, int tileID, SpriteSheet collisionSheet) {
      byte[][] tileOut = new byte[16][16];
      int startX = x * 16;
      int startY = y * 16;
      boolean foundCollision = false;

      for (int w = 0; w < 16; w++) {
         for (int h = 0; h < 16; h++) {
            Color colorCheck = collisionSheet.getColor(startX + w, startY + h);
            if (colorCheck.getAlpha() != 0) {
               foundCollision = true;
               if (colorCheck.equals(this.colorNorth[0])) {
                  tileOut[w][h] = 1;
               } else if (colorCheck.equals(this.colorNorth[1])) {
                  tileOut[w][h] = 2;
               } else if (colorCheck.equals(this.colorNorth[2])) {
                  tileOut[w][h] = 3;
               } else if (colorCheck.equals(this.colorEast[0])) {
                  tileOut[w][h] = 4;
               } else if (colorCheck.equals(this.colorEast[1])) {
                  tileOut[w][h] = 5;
               } else if (colorCheck.equals(this.colorEast[2])) {
                  tileOut[w][h] = 6;
               } else if (colorCheck.equals(this.colorSouth[0])) {
                  tileOut[w][h] = 7;
               } else if (colorCheck.equals(this.colorSouth[1])) {
                  tileOut[w][h] = 8;
               } else if (colorCheck.equals(this.colorSouth[2])) {
                  tileOut[w][h] = 9;
               } else if (colorCheck.equals(this.colorWest[0])) {
                  tileOut[w][h] = 10;
               } else if (colorCheck.equals(this.colorWest[1])) {
                  tileOut[w][h] = 11;
               } else if (colorCheck.equals(this.colorWest[2])) {
                  tileOut[w][h] = 12;
               } else if (colorCheck.equals(this.colorAll[0])) {
                  tileOut[w][h] = 13;
               } else if (colorCheck.equals(this.colorAll[1])) {
                  tileOut[w][h] = 14;
               } else if (colorCheck.equals(this.colorAll[2])) {
                  tileOut[w][h] = 15;
               } else {
                  tileOut[w][h] = 0;
               }
            }
         }
      }

      if (foundCollision) {
         this.tileCollision.put(startGID + tileID, (byte)1);
         this.collisionTiles.put(startGID + tileID, tileOut);
      }
   }

   private void generateDepthData(TileSetBase tileSetBase, int x, int y, int startGID, int tileID, SpriteSheet depthSheet) {
      byte[][] tileOut = new byte[16][16];
      int startX = x * 16;
      int startY = y * 16;
      boolean foundDepth = false;

      for (int w = 0; w < 16; w++) {
         for (int h = 0; h < 16; h++) {
            if (depthSheet.getColor(startX + w, startY + h).getAlpha() != 0) {
               foundDepth = true;
               int colorCheck = depthSheet.getColor(startX + w, startY + h).getBlueByte();
               tileOut[w][h] = (byte)(colorCheck / 10);
            }
         }
      }

      if (foundDepth) {
         this.tileDepth.put(startGID + tileID, (byte)1);
         this.depthTiles.put(startGID + tileID, tileOut);
      }
   }

   public byte[][] generateWorldMapParticleData() throws SlickException {
      Image worldMapParticleMap = ImageLoader.getImage("res/GUI/worldMap/worldMapParticles.png");
      byte[][] worldMapByteArray = new byte[worldMapParticleMap.getWidth()][worldMapParticleMap.getHeight()];

      for (int w = 0; w < worldMapParticleMap.getWidth(); w++) {
         for (int h = 0; h < worldMapParticleMap.getHeight(); h++) {
            Color colorCheck = worldMapParticleMap.getColor(w, h);
            if (colorCheck.getAlpha() != 0) {
               worldMapByteArray[w][h] = this.getParticleInt(colorCheck);
            }
         }
      }

      return worldMapByteArray;
   }

   private void generateParticleData(TileSetBase tileSetBase, int x, int y, int startGID, int tileID, SpriteSheet particleSheet) {
      byte[][] tileOut = new byte[16][16];
      int startX = x * 16;
      int startY = y * 16;
      boolean foundParticle = false;

      for (int w = 0; w < 16; w++) {
         for (int h = 0; h < 16; h++) {
            Color colorCheck = particleSheet.getColor(startX + w, startY + h);
            if (colorCheck.getAlpha() != 0) {
               foundParticle = true;
               tileOut[w][h] = this.getParticleInt(colorCheck);
            }
         }

         if (foundParticle) {
            this.tileParticles.put(startGID + tileID, (byte)1);
            this.particleTiles.put(startGID + tileID, tileOut);
         }
      }
   }

   private byte getParticleInt(Color colorCheck) {
      if (colorCheck.equals(fireRedVeryLow)) {
         return 1;
      } else if (colorCheck.equals(fireRedLow)) {
         return 2;
      } else if (colorCheck.equals(fireRedMedium)) {
         return 3;
      } else if (colorCheck.equals(fireRedHigh)) {
         return 4;
      } else if (colorCheck.equals(fireYellowVeryLow)) {
         return 5;
      } else if (colorCheck.equals(fireYellowLow)) {
         return 6;
      } else if (colorCheck.equals(fireYellowMedium)) {
         return 7;
      } else if (colorCheck.equals(fireYellowHigh)) {
         return 8;
      } else if (colorCheck.equals(fireGreenVeryLow)) {
         return 9;
      } else if (colorCheck.equals(fireGreenLow)) {
         return 10;
      } else if (colorCheck.equals(fireGreenMedium)) {
         return 11;
      } else if (colorCheck.equals(fireGreenHigh)) {
         return 12;
      } else if (colorCheck.equals(fireBlueVeryLow)) {
         return 13;
      } else if (colorCheck.equals(fireBlueLow)) {
         return 14;
      } else if (colorCheck.equals(fireBlueMedium)) {
         return 15;
      } else if (colorCheck.equals(fireBlueHigh)) {
         return 16;
      } else if (colorCheck.equals(smokeLow)) {
         return 17;
      } else if (colorCheck.equals(smokeMedium)) {
         return 18;
      } else if (colorCheck.equals(smokeHigh)) {
         return 19;
      } else if (colorCheck.equals(redSparklesVeryLow)) {
         return 20;
      } else if (colorCheck.equals(redSparklesLow)) {
         return 21;
      } else if (colorCheck.equals(redSparklesMedium)) {
         return 22;
      } else if (colorCheck.equals(redSparklesHigh)) {
         return 23;
      } else if (colorCheck.equals(greenSparklesVeryLow)) {
         return 24;
      } else if (colorCheck.equals(greenSparklesLow)) {
         return 25;
      } else if (colorCheck.equals(greenSparklesMedium)) {
         return 26;
      } else if (colorCheck.equals(greenSparklesHigh)) {
         return 27;
      } else if (colorCheck.equals(blueSparklesVeryLow)) {
         return 28;
      } else if (colorCheck.equals(blueSparklesLow)) {
         return 29;
      } else if (colorCheck.equals(blueSparklesMedium)) {
         return 30;
      } else if (colorCheck.equals(blueSparklesHigh)) {
         return 31;
      } else if (colorCheck.equals(magicRedLow)) {
         return 32;
      } else if (colorCheck.equals(magicRedMedium)) {
         return 33;
      } else if (colorCheck.equals(magicRedHigh)) {
         return 34;
      } else if (colorCheck.equals(magicYellowLow)) {
         return 35;
      } else if (colorCheck.equals(magicYellowMedium)) {
         return 36;
      } else if (colorCheck.equals(magicYellowHigh)) {
         return 37;
      } else if (colorCheck.equals(magicGreenLow)) {
         return 38;
      } else if (colorCheck.equals(magicGreenMedium)) {
         return 39;
      } else if (colorCheck.equals(magicGreenHigh)) {
         return 40;
      } else if (colorCheck.equals(magicBlueLow)) {
         return 41;
      } else if (colorCheck.equals(magicBlueMedium)) {
         return 42;
      } else if (colorCheck.equals(magicBlueHigh)) {
         return 43;
      } else if (colorCheck.equals(purpleSparklesVeryLow)) {
         return 44;
      } else if (colorCheck.equals(purpleSparklesLow)) {
         return 45;
      } else if (colorCheck.equals(purpleSparklesMedium)) {
         return 46;
      } else if (colorCheck.equals(purpleSparklesHigh)) {
         return 47;
      } else if (colorCheck.equals(firePurpleVeryLow)) {
         return 48;
      } else if (colorCheck.equals(firePurpleLow)) {
         return 49;
      } else if (colorCheck.equals(firePurpleMedium)) {
         return 50;
      } else if (colorCheck.equals(firePurpleHigh)) {
         return 51;
      } else if (colorCheck.equals(magicPurpleLow)) {
         return 52;
      } else if (colorCheck.equals(magicPurpleMedium)) {
         return 53;
      } else if (colorCheck.equals(magicPurpleHigh)) {
         return 54;
      } else if (colorCheck.equals(yellowSparklesVeryLow)) {
         return 55;
      } else if (colorCheck.equals(yellowSparklesLow)) {
         return 56;
      } else if (colorCheck.equals(yellowSparklesMedium)) {
         return 57;
      } else if (colorCheck.equals(yellowSparklesHigh)) {
         return 58;
      } else if (colorCheck.equals(magicFireRedLow)) {
         return 59;
      } else if (colorCheck.equals(magicFireRedMedium)) {
         return 60;
      } else if (colorCheck.equals(magicFireRedHigh)) {
         return 61;
      } else if (colorCheck.equals(magicFireYellowLow)) {
         return 62;
      } else if (colorCheck.equals(magicFireYellowMedium)) {
         return 63;
      } else if (colorCheck.equals(magicFireYellowHigh)) {
         return 64;
      } else if (colorCheck.equals(magicFireGreenLow)) {
         return 65;
      } else if (colorCheck.equals(magicFireGreenMedium)) {
         return 66;
      } else if (colorCheck.equals(magicFireGreenHigh)) {
         return 67;
      } else if (colorCheck.equals(magicFireBlueLow)) {
         return 68;
      } else if (colorCheck.equals(magicFireBlueMedium)) {
         return 69;
      } else if (colorCheck.equals(magicFireBlueHigh)) {
         return 70;
      } else if (colorCheck.equals(magicFirePurpleLow)) {
         return 71;
      } else if (colorCheck.equals(magicFirePurpleMedium)) {
         return 72;
      } else {
         return (byte)(colorCheck.equals(magicFirePurpleHigh) ? 73 : 0);
      }
   }

   private void generateShadowData(TileSetBase tileSetBase, int x, int y, int startGID, int tileID, SpriteSheet shadowSheet) {
      byte[][][] tileOut = new byte[16][16][3];
      int startX = x * 16;
      int startY = y * 16;
      boolean foundShadow = false;

      for (int w = 0; w < 16; w++) {
         for (int h = 0; h < 16; h++) {
            if (shadowSheet.getColor(startX + w, startY + h).getAlpha() != 0) {
               foundShadow = true;
               int colorCheckBase = shadowSheet.getColor(startX + w, startY + h).getRedByte();
               int colorCheckStart = shadowSheet.getColor(startX + w, startY + h).getGreenByte();
               int colorCheckStop = shadowSheet.getColor(startX + w, startY + h).getBlueByte();
               tileOut[w][h][0] = (byte)(colorCheckBase / 5);
               tileOut[w][h][1] = (byte)(colorCheckStart / 5);
               tileOut[w][h][2] = (byte)(colorCheckStop / 5);
            }
         }
      }

      if (foundShadow) {
         this.tileShadow.put(startGID + tileID, (byte)1);
         this.shadowTiles.put(startGID + tileID, tileOut);
      }
   }

   public String getTileName(int tileID) {
      return this.tileName.get(tileID);
   }

   public String getTileDescription(int tileID) {
      return this.tileDescription.get(tileID);
   }

   public boolean isTileBlockLarge(int tileID) {
      return this.tileBlockLarge.get(tileID) != null;
   }

   public boolean isTileBlockSmall(int tileID) {
      return this.tileBlockSmall.get(tileID) != null;
   }

   public boolean isTileBlockRoad(int tileID) {
      return this.tileBlockRoad.get(tileID) != null;
   }

   public boolean isTileBlockGate(int tileID) {
      return this.tileBlockGate.get(tileID) != null;
   }

   public boolean isTileBlockLight(int tileID) {
      return this.tileBlockLight.get(tileID) != null;
   }

   public boolean isTileBlockWater(int tileID) {
      return this.tileBlockWater.get(tileID) != null;
   }

   public boolean isTileBlockCorruption(int tileID) {
      return this.tileBlockCorruption.get(tileID) != null;
   }

   public boolean isTileBlockGhost(int tileID) {
      return this.tileBlockGhost.get(tileID) != null;
   }

   public int getMovementCost(int tileID) {
      return this.tileMovementCost.get(tileID) != null ? this.tileMovementCost.get(tileID) : 0;
   }

   public boolean isTileDepth(int tileID) {
      return this.tileDepth.get(tileID) != null;
   }

   public boolean isTileParticles(int tileID) {
      return this.tileParticles.get(tileID) != null;
   }

   public boolean isTileShadow(int tileID) {
      return this.tileShadow.get(tileID) != null;
   }

   public byte getTileLight(int tileID) {
      return this.tileLight.get(tileID) != null ? this.tileLight.get(tileID) : 0;
   }

   public byte getTileLayer(int tileID) {
      return this.tileLayer.get(tileID);
   }

   public MapTilesLoader.TileSetType getTileType(int tileID) {
      return this.tileType.get(tileID);
   }

   public boolean isTileCollision(int tileID) {
      return this.tileCollision.get(tileID) != null;
   }

   public ResourceModule.ResourceType getTileResourceType(int tileID) {
      return this.tileResourceType.get(tileID);
   }

   public byte getTileResourceAmount(int tileID) {
      return this.tileResourceAmount.get(tileID) != null ? this.tileResourceAmount.get(tileID) : 0;
   }

   public byte getTileResourceAmountMax(int tileID) {
      return this.tileResourceAmountMax.get(tileID) != null ? this.tileResourceAmountMax.get(tileID) : 0;
   }

   public ResourceModule.ResourceColorSet getTileResourceColorSet(int tileID) {
      return this.tileResourceColorSet.get(tileID);
   }

   public boolean isTileCrop(int tileID) {
      return this.tileCrops.get(tileID) != null;
   }

   public boolean canClear(int tileID) {
      return this.tileCanClear.get(tileID) != null;
   }

   public boolean isTileInvulnerable(int tileID) {
      return this.tileInvulnerable.get(tileID) != null;
   }

   public boolean isTileFrozen(int tileID) {
      return this.tileFrozen.get(tileID) != null;
   }

   public boolean isTileWater(int tileID) {
      return this.tileWater.get(tileID) != null;
   }

   public boolean isTileCourierGolemParking(int tileID) {
      return this.tileCourierGolemParking.get(tileID) != null;
   }

   public boolean isTileInteractPoint(int tileID) {
      return this.tileInteractPoint.get(tileID) != null;
   }

   public boolean isTileFunctionalPoint(int tileID) {
      return this.tileFunctionalPoint.get(tileID) != null;
   }

   public boolean isTilePrayPoint(int tileID) {
      return this.tilePrayPoint.get(tileID) != null;
   }

   public boolean isTileVesselPoint(int tileID) {
      return this.tileVesselPoint.get(tileID) != null;
   }

   public Color getTileMiniMapColor(int tileID) {
      return this.tileMiniMapColor.get(tileID);
   }

   public MapTilesLoader.TileSet canTileRegrow(int tileID) {
      return this.tileCanRegrow.get(tileID);
   }

   public int getRegrowRate(int tileID) {
      return this.tileRegrowRate.get(tileID);
   }

   public boolean getRegrowOnlyMotivated(int tileID) {
      return this.tileRegrowOnlyMotivated.get(tileID) != null;
   }

   public boolean getRegrowIgnoreWeather(int tileID) {
      return this.tileRegrowIgnoreWeather.get(tileID) != null;
   }

   public MapTilesLoader.TileSet canTileSpread(int tileID) {
      return this.tileCanSpread.get(tileID);
   }

   public int getSpreadRate(int tileID) {
      return this.tileSpreadRate.get(tileID);
   }

   public boolean getSpreadOnlyMotivated(int tileID) {
      return this.tileSpreadOnlyMotivated.get(tileID) != null;
   }

   public boolean getSpreadIgnoreWeather(int tileID) {
      return this.tileSpreadIgnoreWeather.get(tileID) != null;
   }

   public int getTileEssenceAmount(int tileID) {
      return this.tileEssenceAmount.get(tileID) != null ? this.tileEssenceAmount.get(tileID) : 0;
   }

   public byte getTileHeight(int tileID) {
      return this.tilePropHeight.get(tileID);
   }

   public byte getTileWidth(int tileID) {
      return this.tilePropWidth.get(tileID);
   }

   public int getTileSetGID(MapTilesLoader.TileSet tileSet) {
      return tileSet.getTileSetTemplate().getTileGID();
   }

   public int getTileSetGID(int tileID) {
      if (tileID == 0) {
         return 0;
      } else {
         return tileID >= OBJECT_TILE_RANGE && tileID <= OBJECT_TILE_RANGE
            ? tileID / OBJECT_TILE_SPACING * OBJECT_TILE_SPACING
            : tileID / TERRAIN_TILE_SUB * TERRAIN_TILE_SUB;
      }
   }

   public int getTileSetGID(MapTilesLoader.TileSet tileSet, MapTilesLoader.TileSetObjectPhase tileSetObjectPhase) {
      return tileSet.getTileSetTemplate().getTileGID() + tileSetObjectPhase.getTileOffset();
   }

   public int getTileSetGID(int tileID, MapTilesLoader.TileSetObjectPhase tileSetObjectPhase) {
      if (tileID == 0) {
         return 0;
      } else {
         return tileID >= OBJECT_TILE_RANGE && tileID <= OBJECT_TILE_RANGE
            ? tileID / OBJECT_TILE_SPACING * OBJECT_TILE_SPACING + tileSetObjectPhase.getTileOffset()
            : tileID / TERRAIN_TILE_SUB * TERRAIN_TILE_SUB + tileSetObjectPhase.getTileOffset();
      }
   }

   public MapTilesLoader.TileSet getTileSetByTileID(int tileId) {
      return this.GIDToTileSet.get(this.getTileSetGID(tileId));
   }

   public IntKeyMap<Image> getTiles() {
      return this.tiles;
   }

   public IntKeyMap<byte[][]> getCollisionTiles() {
      return this.collisionTiles;
   }

   public IntKeyMap<byte[][]> getDepthTiles() {
      return this.depthTiles;
   }

   public IntKeyMap<byte[][]> getParticleTiles() {
      return this.particleTiles;
   }

   public IntKeyMap<byte[][][]> getShadowTiles() {
      return this.shadowTiles;
   }

   public Image getObjectImage(int tileGID) throws SlickException {
      this.checkAndloadTileset(tileGID);
      return this.objects.get(tileGID);
   }

   public Image getWallImage(int tileGID) throws SlickException {
      this.checkAndloadTileset(tileGID);
      return this.tiles.get(tileGID + 250);
   }

   public static enum TileSet {
      CASTLE_1(new Castle(MapTilesLoader.calcObjectTileID(0, 0), 0)),
      CASTLE_2(new Castle(MapTilesLoader.calcObjectTileID(0, 1), 1)),
      CASTLE_3(new Castle(MapTilesLoader.calcObjectTileID(0, 2), 2)),
      CASTLE_4(new Castle(MapTilesLoader.calcObjectTileID(0, 3), 3)),
      CASTLE_5(new Castle(MapTilesLoader.calcObjectTileID(0, 4), 4)),
      CASTLE_6(new Castle(MapTilesLoader.calcObjectTileID(0, 5), 5)),
      CASTLE_7(new Castle(MapTilesLoader.calcObjectTileID(0, 6), 6)),
      CASTLE_8(new Castle(MapTilesLoader.calcObjectTileID(0, 7), 7)),
      CASTLE_9(new Castle(MapTilesLoader.calcObjectTileID(0, 8), 8)),
      CASTLE_10(new Castle(MapTilesLoader.calcObjectTileID(0, 9), 9)),
      CASTLE_11(new Castle(MapTilesLoader.calcObjectTileID(0, 10), 10)),
      CASTLE_12(new Castle(MapTilesLoader.calcObjectTileID(0, 11), 11)),
      CASTLE_13(new Castle(MapTilesLoader.calcObjectTileID(0, 12), 12)),
      CASTLE_14(new Castle(MapTilesLoader.calcObjectTileID(0, 13), 13)),
      CASTLE_15(new Castle(MapTilesLoader.calcObjectTileID(0, 14), 14)),
      ANCILLARY(new Ancillary(MapTilesLoader.calcObjectTileID(1, 0), 0)),
      ANCILLARY_UPGRADE_1(new Ancillary(MapTilesLoader.calcObjectTileID(1, 1), 1)),
      ANCILLARY_UPGRADE_2(new Ancillary(MapTilesLoader.calcObjectTileID(1, 2), 2)),
      ANCILLARY_UPGRADE_3(new Ancillary(MapTilesLoader.calcObjectTileID(1, 3), 3)),
      ANCILLARY_UPGRADE_4(new Ancillary(MapTilesLoader.calcObjectTileID(1, 4), 4)),
      ANCIENT_CULLIS_GATE(new AncientCullisGate(MapTilesLoader.calcObjectTileID(2, 0))),
      ANCIENT_RADIANCE_POOL(new AncientRadiancePool(MapTilesLoader.calcObjectTileID(3, 0))),
      ARMORSMITHY(new Armorsmithy(MapTilesLoader.calcObjectTileID(4, 0), 0)),
      ARMORSMITHY_UPGRADE_1(new Armorsmithy(MapTilesLoader.calcObjectTileID(4, 1), 1)),
      ARMORSMITHY_UPGRADE_2(new Armorsmithy(MapTilesLoader.calcObjectTileID(4, 2), 2)),
      AMMO_STORAGE(new AmmoStorage(MapTilesLoader.calcObjectTileID(5, 0), 0)),
      AMMO_STORAGE_UPGRADE_1(new AmmoStorage(MapTilesLoader.calcObjectTileID(5, 1), 1)),
      AMMO_STORAGE_UPGRADE_2(new AmmoStorage(MapTilesLoader.calcObjectTileID(5, 2), 2)),
      AMMO_STORAGE_UPGRADE_3(new AmmoStorage(MapTilesLoader.calcObjectTileID(5, 3), 3)),
      AMMO_STORAGE_UPGRADE_4(new AmmoStorage(MapTilesLoader.calcObjectTileID(5, 4), 4)),
      ATTRACT_TOWER(new AttractTower(MapTilesLoader.calcObjectTileID(6, 0), 0)),
      ATTRACT_TOWER_UPGRADE_1(new AttractTower(MapTilesLoader.calcObjectTileID(6, 1), 1)),
      ATTRACT_TOWER_UPGRADE_2(new AttractTower(MapTilesLoader.calcObjectTileID(6, 2), 2)),
      ATTRACT_TOWER_UPGRADE_3(new AttractTower(MapTilesLoader.calcObjectTileID(6, 3), 3)),
      BALLISTA_TOWER(new BallistaTower(MapTilesLoader.calcObjectTileID(7, 0), 0)),
      BALLISTA_TOWER_UPGRADE_1(new BallistaTower(MapTilesLoader.calcObjectTileID(7, 1), 1)),
      BALLISTA_TOWER_UPGRADE_2(new BallistaTower(MapTilesLoader.calcObjectTileID(7, 2), 2)),
      BALLISTA_TOWER_UPGRADE_3_FIRE(new BallistaTower(MapTilesLoader.calcObjectTileID(7, 3), 3)),
      BALLISTA_TOWER_UPGRADE_3_POISON(new BallistaTower(MapTilesLoader.calcObjectTileID(7, 4), 4)),
      BALLISTA_TOWER_UPGRADE_3_ICE(new BallistaTower(MapTilesLoader.calcObjectTileID(7, 5), 5)),
      BANISH_TOWER(new BanishTower(MapTilesLoader.calcObjectTileID(8, 0), 0)),
      BANISH_TOWER_UPGRADE_1(new BanishTower(MapTilesLoader.calcObjectTileID(8, 1), 1)),
      BANISH_TOWER_UPGRADE_2(new BanishTower(MapTilesLoader.calcObjectTileID(8, 2), 2)),
      BANISH_TOWER_UPGRADE_3(new BanishTower(MapTilesLoader.calcObjectTileID(8, 3), 3)),
      RANGER_LODGE(new RangerLodge(MapTilesLoader.calcObjectTileID(9, 0), 0)),
      RANGER_LODGE_UPGRADE_1(new RangerLodge(MapTilesLoader.calcObjectTileID(9, 1), 1)),
      RANGER_LODGE_UPGRADE_2(new RangerLodge(MapTilesLoader.calcObjectTileID(9, 2), 2)),
      BOTTLER(new Bottler(MapTilesLoader.calcObjectTileID(10, 0), 0)),
      BOTTLER_UPGRADE_1(new Bottler(MapTilesLoader.calcObjectTileID(10, 1), 1)),
      BOTTLER_UPGRADE_2(new Bottler(MapTilesLoader.calcObjectTileID(10, 2), 2)),
      BOW_TOWER(new BowTower(MapTilesLoader.calcObjectTileID(11, 0), 0)),
      BOW_TOWER_UPGRADE_1(new BowTower(MapTilesLoader.calcObjectTileID(11, 1), 1)),
      BOW_TOWER_UPGRADE_2(new BowTower(MapTilesLoader.calcObjectTileID(11, 2), 2)),
      BOW_TOWER_UPGRADE_3_FIRE(new BowTower(MapTilesLoader.calcObjectTileID(11, 3), 3)),
      BOW_TOWER_UPGRADE_3_POISON(new BowTower(MapTilesLoader.calcObjectTileID(11, 4), 4)),
      BOW_TOWER_UPGRADE_3_ICE(new BowTower(MapTilesLoader.calcObjectTileID(11, 5), 5)),
      BOWYER(new Bowyer(MapTilesLoader.calcObjectTileID(12, 0), 0)),
      BOWYER_UPGRADE_1(new Bowyer(MapTilesLoader.calcObjectTileID(12, 1), 1)),
      BOWYER_UPGRADE_2(new Bowyer(MapTilesLoader.calcObjectTileID(12, 2), 2)),
      BULLET_TOWER(new BulletTower(MapTilesLoader.calcObjectTileID(13, 0), 0)),
      BULLET_TOWER_UPGRADE_1(new BulletTower(MapTilesLoader.calcObjectTileID(13, 1), 1)),
      BULLET_TOWER_UPGRADE_2(new BulletTower(MapTilesLoader.calcObjectTileID(13, 2), 2)),
      BULLET_TOWER_UPGRADE_3_FIRE(new BulletTower(MapTilesLoader.calcObjectTileID(13, 3), 3)),
      BULLET_TOWER_UPGRADE_3_LIGHTNING(new BulletTower(MapTilesLoader.calcObjectTileID(13, 4), 4)),
      BULLET_TOWER_UPGRADE_3_ICE(new BulletTower(MapTilesLoader.calcObjectTileID(13, 5), 5)),
      CLINIC(new Clinic(MapTilesLoader.calcObjectTileID(14, 0), 0)),
      CLINIC_UPGRADE_1(new Clinic(MapTilesLoader.calcObjectTileID(14, 1), 1)),
      CLINIC_UPGRADE_2(new Clinic(MapTilesLoader.calcObjectTileID(14, 2), 2)),
      CRYSTAL_GOLEM_COMBOBULATOR(new CrystalGolemCombobulator(MapTilesLoader.calcObjectTileID(15, 0), 0)),
      CRYSTAL_GOLEM_COMBOBULATOR_UPGRADE_1(new CrystalGolemCombobulator(MapTilesLoader.calcObjectTileID(15, 1), 1)),
      CRYSTAL_GOLEM_COMBOBULATOR_UPGRADE_2(new CrystalGolemCombobulator(MapTilesLoader.calcObjectTileID(15, 2), 2)),
      CRYSTAL_GOLEM_COMBOBULATOR_UPGRADE_3_LIGHTNING(new CrystalGolemCombobulator(MapTilesLoader.calcObjectTileID(15, 3), 3)),
      CRYSTAL_GOLEM_COMBOBULATOR_UPGRADE_3_ICE(new CrystalGolemCombobulator(MapTilesLoader.calcObjectTileID(15, 4), 4)),
      CRYSTAL_GOLEM_COMBOBULATOR_UPGRADE_3_FIRE(new CrystalGolemCombobulator(MapTilesLoader.calcObjectTileID(15, 5), 5)),
      CRYSTAL_HARVESTRY(new CrystalHarvestry(MapTilesLoader.calcObjectTileID(16, 0), 0)),
      CRYSTAL_HARVESTRY_UPGRADE_1(new CrystalHarvestry(MapTilesLoader.calcObjectTileID(16, 1), 1)),
      CRYSTAL_HARVESTRY_UPGRADE_2(new CrystalHarvestry(MapTilesLoader.calcObjectTileID(16, 2), 2)),
      CRYSTAL_MOTIVATOR(new CrystalMotivator(MapTilesLoader.calcObjectTileID(17, 0))),
      CRYSTAL_STORAGE(new CrystalStorage(MapTilesLoader.calcObjectTileID(18, 0), 0)),
      CRYSTAL_STORAGE_UPGRADE_1(new CrystalStorage(MapTilesLoader.calcObjectTileID(18, 1), 1)),
      CRYSTAL_STORAGE_UPGRADE_2(new CrystalStorage(MapTilesLoader.calcObjectTileID(18, 2), 2)),
      CRYSTAL_STORAGE_UPGRADE_3(new CrystalStorage(MapTilesLoader.calcObjectTileID(18, 3), 3)),
      CRYSTAL_STORAGE_UPGRADE_4(new CrystalStorage(MapTilesLoader.calcObjectTileID(18, 4), 4)),
      CRYSTILLERY(new Crystillery(MapTilesLoader.calcObjectTileID(19, 0), 0)),
      CRYSTILLERY_UPGRADE_1(new Crystillery(MapTilesLoader.calcObjectTileID(19, 1), 1)),
      CRYSTILLERY_UPGRADE_2(new Crystillery(MapTilesLoader.calcObjectTileID(19, 2), 2)),
      CULLIS_GATE(new CullisGate(MapTilesLoader.calcObjectTileID(20, 0))),
      DOGGO_HOUSE(new DoggoHouse(MapTilesLoader.calcObjectTileID(21, 0), 0)),
      DOGGO_HOUSE_UPGRADE_1(new DoggoHouse(MapTilesLoader.calcObjectTileID(21, 1), 1)),
      DOGGO_HOUSE_UPGRADE_2(new DoggoHouse(MapTilesLoader.calcObjectTileID(21, 2), 2)),
      EQUIPMENT_STORAGE(new EquipmentStorage(MapTilesLoader.calcObjectTileID(22, 0), 0)),
      EQUIPMENT_STORAGE_UPGRADE_1(new EquipmentStorage(MapTilesLoader.calcObjectTileID(22, 1), 1)),
      EQUIPMENT_STORAGE_UPGRADE_2(new EquipmentStorage(MapTilesLoader.calcObjectTileID(22, 2), 2)),
      EQUIPMENT_STORAGE_UPGRADE_3(new EquipmentStorage(MapTilesLoader.calcObjectTileID(22, 3), 3)),
      EQUIPMENT_STORAGE_UPGRADE_4(new EquipmentStorage(MapTilesLoader.calcObjectTileID(22, 4), 4)),
      ESSENCE_COLLECTOR(new EssenceCollector(MapTilesLoader.calcObjectTileID(23, 0), 0)),
      ESSENCE_COLLECTOR_UPGRADE_1(new EssenceCollector(MapTilesLoader.calcObjectTileID(23, 1), 1)),
      ESSENCE_COLLECTOR_UPGRADE_2(new EssenceCollector(MapTilesLoader.calcObjectTileID(23, 2), 2)),
      ELEMENTAL_BOLT_TOWER(new ElementalBoltTower(MapTilesLoader.calcObjectTileID(24, 0), 0)),
      ELEMENTAL_BOLT_TOWER_UPGRADE_1(new ElementalBoltTower(MapTilesLoader.calcObjectTileID(24, 1), 1)),
      ELEMENTAL_BOLT_TOWER_UPGRADE_2(new ElementalBoltTower(MapTilesLoader.calcObjectTileID(24, 2), 2)),
      ELEMENTAL_BOLT_TOWER_UPGRADE_3_FIRE(new ElementalBoltTower(MapTilesLoader.calcObjectTileID(24, 3), 3)),
      ELEMENTAL_BOLT_TOWER_UPGRADE_3_LIGHTNING(new ElementalBoltTower(MapTilesLoader.calcObjectTileID(24, 4), 4)),
      ELEMENTAL_BOLT_TOWER_UPGRADE_3_ICE(new ElementalBoltTower(MapTilesLoader.calcObjectTileID(24, 5), 5)),
      FARM(new Farm(MapTilesLoader.calcObjectTileID(25, 0), 0)),
      FARM_UPGRADE_1(new Farm(MapTilesLoader.calcObjectTileID(25, 1), 1)),
      FARM_UPGRADE_2(new Farm(MapTilesLoader.calcObjectTileID(25, 2), 2)),
      FIRE_PIT(new FirePit(MapTilesLoader.calcObjectTileID(26, 0))),
      LARGE_FIRE_PIT(new LargeFirePit(MapTilesLoader.calcObjectTileID(27, 0))),
      FOOD_STORAGE(new FoodStorage(MapTilesLoader.calcObjectTileID(28, 0), 0)),
      FOOD_STORAGE_UPGRADE_1(new FoodStorage(MapTilesLoader.calcObjectTileID(28, 1), 1)),
      FOOD_STORAGE_UPGRADE_2(new FoodStorage(MapTilesLoader.calcObjectTileID(28, 2), 2)),
      FOOD_STORAGE_UPGRADE_3(new FoodStorage(MapTilesLoader.calcObjectTileID(28, 3), 3)),
      FOOD_STORAGE_UPGRADE_4(new FoodStorage(MapTilesLoader.calcObjectTileID(28, 4), 4)),
      FORGE(new Forge(MapTilesLoader.calcObjectTileID(29, 0), 0)),
      FORGE_UPGRADE_1(new Forge(MapTilesLoader.calcObjectTileID(29, 1), 1)),
      FORGE_UPGRADE_2(new Forge(MapTilesLoader.calcObjectTileID(29, 2), 2)),
      GOLD_STORAGE(new GoldStorage(MapTilesLoader.calcObjectTileID(30, 0), 0)),
      GOLD_STORAGE_UPGRADE_1(new GoldStorage(MapTilesLoader.calcObjectTileID(30, 1), 1)),
      GOLD_STORAGE_UPGRADE_2(new GoldStorage(MapTilesLoader.calcObjectTileID(30, 2), 2)),
      GOLD_STORAGE_UPGRADE_3(new GoldStorage(MapTilesLoader.calcObjectTileID(30, 3), 3)),
      GOLD_STORAGE_UPGRADE_4(new GoldStorage(MapTilesLoader.calcObjectTileID(30, 4), 4)),
      HOUSING(new Housing(MapTilesLoader.calcObjectTileID(31, 0), 0)),
      HOUSING_UPGRADE_1_OCCUPANCY(new Housing(MapTilesLoader.calcObjectTileID(31, 1), 1)),
      HOUSING_UPGRADE_2_OCCUPANCY(new Housing(MapTilesLoader.calcObjectTileID(31, 2), 2)),
      HOUSING_UPGRADE_3_OCCUPANCY(new Housing(MapTilesLoader.calcObjectTileID(31, 3), 3)),
      HOUSING_UPGRADE_4_OCCUPANCY(new Housing(MapTilesLoader.calcObjectTileID(31, 4), 4)),
      HOUSING_UPGRADE_1_STANDARD(new Housing(MapTilesLoader.calcObjectTileID(31, 5), 5)),
      HOUSING_UPGRADE_2_STANDARD(new Housing(MapTilesLoader.calcObjectTileID(31, 6), 6)),
      HOUSING_UPGRADE_3_STANDARD(new Housing(MapTilesLoader.calcObjectTileID(31, 7), 7)),
      HOUSING_UPGRADE_4_STANDARD(new Housing(MapTilesLoader.calcObjectTileID(31, 8), 8)),
      HOUSING_UPGRADE_1_QUALITY(new Housing(MapTilesLoader.calcObjectTileID(31, 9), 9)),
      HOUSING_UPGRADE_2_QUALITY(new Housing(MapTilesLoader.calcObjectTileID(31, 10), 10)),
      HOUSING_UPGRADE_3_QUALITY(new Housing(MapTilesLoader.calcObjectTileID(31, 11), 11)),
      HOUSING_UPGRADE_4_QUALITY(new Housing(MapTilesLoader.calcObjectTileID(31, 12), 12)),
      KEY_SHACK(new KeyShack(MapTilesLoader.calcObjectTileID(32, 0), 0)),
      KEY_SHACK_UPGRADE_1(new KeyShack(MapTilesLoader.calcObjectTileID(32, 1), 1)),
      KEY_SHACK_UPGRADE_2(new KeyShack(MapTilesLoader.calcObjectTileID(32, 2), 2)),
      KEY_SHACK_UPGRADE_3(new KeyShack(MapTilesLoader.calcObjectTileID(32, 3), 3)),
      KEY_SHACK_UPGRADE_4(new KeyShack(MapTilesLoader.calcObjectTileID(32, 4), 4)),
      LARGE_FOUNTAIN(new LargeFountain(MapTilesLoader.calcObjectTileID(33, 0))),
      LOOT_BOX(new LootBox(MapTilesLoader.calcObjectTileID(34, 0))),
      LIGHTNING_ROD(new LightningRod(MapTilesLoader.calcObjectTileID(35, 0), 0)),
      LIGHTNING_ROD_UPGRADE_1(new LightningRod(MapTilesLoader.calcObjectTileID(35, 1), 1)),
      LIGHTNING_ROD_UPGRADE_2(new LightningRod(MapTilesLoader.calcObjectTileID(35, 2), 2)),
      CORRUPTED_GRAVEYARD_SMALL(new CorruptedGraveyardSmall(MapTilesLoader.calcObjectTileID(36, 0))),
      CORRUPTED_GRAVEYARD_MEDIUM(new CorruptedGraveyardMedium(MapTilesLoader.calcObjectTileID(37, 0))),
      CORRUPTED_GRAVEYARD_LARGE(new CorruptedGraveyardLarge(MapTilesLoader.calcObjectTileID(38, 0))),
      CORRUPTED_FIRE_BOW_TOWER(new CorruptedFireBowTower(MapTilesLoader.calcObjectTileID(39, 0))),
      CORRUPTED_ICE_BOW_TOWER(new CorruptedIceBowTower(MapTilesLoader.calcObjectTileID(40, 0))),
      CORRUPTED_POISON_BOW_TOWER(new CorruptedPoisonBowTower(MapTilesLoader.calcObjectTileID(41, 0))),
      CORRUPTED_LIGHTNING_BOW_TOWER(new CorruptedLightningBowTower(MapTilesLoader.calcObjectTileID(42, 0))),
      CORRUPTED_FIRE_PHANTOM_DART_TOWER(new CorruptedFirePhantomDartTower(MapTilesLoader.calcObjectTileID(43, 0))),
      CORRUPTED_ICE_PHANTOM_DART_TOWER(new CorruptedIcePhantomDartTower(MapTilesLoader.calcObjectTileID(44, 0))),
      CORRUPTED_POISON_PHANTOM_DART_TOWER(new CorruptedPoisonPhantomDartTower(MapTilesLoader.calcObjectTileID(45, 0))),
      CORRUPTED_LIGHTNING_PHANTOM_DART_TOWER(new CorruptedLightningPhantomDartTower(MapTilesLoader.calcObjectTileID(46, 0))),
      CORRUPTED_LARGE_FIRE_PIT(new CorruptedLargeFirePit(MapTilesLoader.calcObjectTileID(47, 0))),
      CORRUPTED_FIRE_PIT(new CorruptedFirePit(MapTilesLoader.calcObjectTileID(48, 0))),
      COURIER_STATION(new CourierStation(MapTilesLoader.calcObjectTileID(49, 0), 0)),
      COURIER_STATION_UPGRADE_1(new CourierStation(MapTilesLoader.calcObjectTileID(49, 1), 1)),
      COURIER_STATION_UPGRADE_2(new CourierStation(MapTilesLoader.calcObjectTileID(49, 2), 2)),
      KITCHEN(new Kitchen(MapTilesLoader.calcObjectTileID(50, 0), 0)),
      KITCHEN_UPGRADE_1(new Kitchen(MapTilesLoader.calcObjectTileID(50, 1), 1)),
      KITCHEN_UPGRADE_2(new Kitchen(MapTilesLoader.calcObjectTileID(50, 2), 2)),
      LUMBER_MILL(new LumberMill(MapTilesLoader.calcObjectTileID(51, 0), 0)),
      LUMBER_MILL_UPGRADE_1(new LumberMill(MapTilesLoader.calcObjectTileID(51, 1), 1)),
      LUMBER_MILL_UPGRADE_2(new LumberMill(MapTilesLoader.calcObjectTileID(51, 2), 2)),
      LUMBER_SHACK(new LumberShack(MapTilesLoader.calcObjectTileID(52, 0), 0)),
      LUMBER_SHACK_UPGRADE_1(new LumberShack(MapTilesLoader.calcObjectTileID(52, 1), 1)),
      LUMBER_SHACK_UPGRADE_2(new LumberShack(MapTilesLoader.calcObjectTileID(52, 2), 2)),
      OUTPOST(new Outpost(MapTilesLoader.calcObjectTileID(53, 0), 0)),
      OUTPOST_UPGRADE_1(new Outpost(MapTilesLoader.calcObjectTileID(53, 1), 1)),
      OUTPOST_UPGRADE_2(new Outpost(MapTilesLoader.calcObjectTileID(53, 2), 2)),
      PHANTOM_DART_TOWER(new PhantomDartTower(MapTilesLoader.calcObjectTileID(54, 0), 0)),
      PHANTOM_DART_TOWER_UPGRADE_1(new PhantomDartTower(MapTilesLoader.calcObjectTileID(54, 1), 1)),
      PHANTOM_DART_TOWER_UPGRADE_2(new PhantomDartTower(MapTilesLoader.calcObjectTileID(54, 2), 2)),
      PHANTOM_DART_TOWER_UPGRADE_3_FIRE(new PhantomDartTower(MapTilesLoader.calcObjectTileID(54, 3), 3)),
      PHANTOM_DART_TOWER_UPGRADE_3_LIGHTNING(new PhantomDartTower(MapTilesLoader.calcObjectTileID(54, 4), 4)),
      PHANTOM_DART_TOWER_UPGRADE_3_ICE(new PhantomDartTower(MapTilesLoader.calcObjectTileID(54, 5), 5)),
      MAINTENANCE_BUILDING(new MaintenanceBuilding(MapTilesLoader.calcObjectTileID(55, 0), 0)),
      MAINTENANCE_BUILDING_UPGRADE_1(new MaintenanceBuilding(MapTilesLoader.calcObjectTileID(55, 1), 1)),
      MAINTENANCE_BUILDING_UPGRADE_2(new MaintenanceBuilding(MapTilesLoader.calcObjectTileID(55, 2), 2)),
      MARKETPLACE(new Marketplace(MapTilesLoader.calcObjectTileID(56, 0), 0)),
      MARKETPLACE_UPGRADE_1(new Marketplace(MapTilesLoader.calcObjectTileID(56, 1), 1)),
      MARKETPLACE_UPGRADE_2(new Marketplace(MapTilesLoader.calcObjectTileID(56, 2), 2)),
      MIGRATION_WAY_STATION(new MigrationWayStation(MapTilesLoader.calcObjectTileID(57, 0), 0)),
      MIGRATION_WAY_STATION_UPGRADE_1(new MigrationWayStation(MapTilesLoader.calcObjectTileID(57, 1), 1)),
      MIGRATION_WAY_STATION_UPGRADE_2(new MigrationWayStation(MapTilesLoader.calcObjectTileID(57, 2), 2)),
      MINERAL_STORAGE(new MineralStorage(MapTilesLoader.calcObjectTileID(58, 0), 0)),
      MINERAL_STORAGE_UPGRADE_1(new MineralStorage(MapTilesLoader.calcObjectTileID(58, 1), 1)),
      MINERAL_STORAGE_UPGRADE_2(new MineralStorage(MapTilesLoader.calcObjectTileID(58, 2), 2)),
      MINERAL_STORAGE_UPGRADE_3(new MineralStorage(MapTilesLoader.calcObjectTileID(58, 3), 3)),
      MINERAL_STORAGE_UPGRADE_4(new MineralStorage(MapTilesLoader.calcObjectTileID(58, 4), 4)),
      MINING_FACILITY(new MiningFacility(MapTilesLoader.calcObjectTileID(59, 0), 0)),
      MINING_FACILITY_UPGRADE_1(new MiningFacility(MapTilesLoader.calcObjectTileID(59, 1), 1)),
      MINING_FACILITY_UPGRADE_2(new MiningFacility(MapTilesLoader.calcObjectTileID(59, 2), 2)),
      MISCELLANEOUS_STORAGE(new MiscellaneousStorage(MapTilesLoader.calcObjectTileID(60, 0), 0)),
      MISCELLANEOUS_STORAGE_UPGRADE_1(new MiscellaneousStorage(MapTilesLoader.calcObjectTileID(60, 1), 1)),
      MISCELLANEOUS_STORAGE_UPGRADE_2(new MiscellaneousStorage(MapTilesLoader.calcObjectTileID(60, 2), 2)),
      MISCELLANEOUS_STORAGE_UPGRADE_3(new MiscellaneousStorage(MapTilesLoader.calcObjectTileID(60, 3), 3)),
      MISCELLANEOUS_STORAGE_UPGRADE_4(new MiscellaneousStorage(MapTilesLoader.calcObjectTileID(60, 4), 4)),
      SLING_TOWER(new SlingTower(MapTilesLoader.calcObjectTileID(61, 0), 0)),
      SLING_TOWER_UPGRADE_1(new SlingTower(MapTilesLoader.calcObjectTileID(61, 1), 1)),
      SLING_TOWER_UPGRADE_2(new SlingTower(MapTilesLoader.calcObjectTileID(61, 2), 2)),
      SLING_TOWER_UPGRADE_3_EXPLODING(new SlingTower(MapTilesLoader.calcObjectTileID(61, 3), 3)),
      SLING_TOWER_UPGRADE_3_LIGHTNING(new SlingTower(MapTilesLoader.calcObjectTileID(61, 4), 4)),
      SLING_TOWER_UPGRADE_3_ICE(new SlingTower(MapTilesLoader.calcObjectTileID(61, 5), 5)),
      SMALL_FOUNTAIN(new SmallFountain(MapTilesLoader.calcObjectTileID(62, 0))),
      SPRAY_TOWER(new SprayTower(MapTilesLoader.calcObjectTileID(63, 0), 0)),
      SPRAY_TOWER_UPGRADE_1(new SprayTower(MapTilesLoader.calcObjectTileID(63, 1), 1)),
      SPRAY_TOWER_UPGRADE_2(new SprayTower(MapTilesLoader.calcObjectTileID(63, 2), 2)),
      SPRAY_TOWER_UPGRADE_3_FIRE(new SprayTower(MapTilesLoader.calcObjectTileID(63, 3), 3)),
      SPRAY_TOWER_UPGRADE_3_LIGHTNING(new SprayTower(MapTilesLoader.calcObjectTileID(63, 4), 4)),
      SPRAY_TOWER_UPGRADE_3_ICE(new SprayTower(MapTilesLoader.calcObjectTileID(63, 5), 5)),
      STATIC_TOWER(new StaticTower(MapTilesLoader.calcObjectTileID(64, 0), 0)),
      STATIC_TOWER_UPGRADE_1(new StaticTower(MapTilesLoader.calcObjectTileID(64, 1), 1)),
      STATIC_TOWER_UPGRADE_2(new StaticTower(MapTilesLoader.calcObjectTileID(64, 2), 2)),
      STATIC_TOWER_UPGRADE_3(new StaticTower(MapTilesLoader.calcObjectTileID(64, 3), 3)),
      RAIN_CATCHER(new RainCatcher(MapTilesLoader.calcObjectTileID(65, 0), 0)),
      RAIN_CATCHER_UPGRADE_1(new RainCatcher(MapTilesLoader.calcObjectTileID(65, 1), 1)),
      RAIN_CATCHER_UPGRADE_2(new RainCatcher(MapTilesLoader.calcObjectTileID(65, 2), 2)),
      RECOMBOBULATOR_TOWER(new RecombobulatorTower(MapTilesLoader.calcObjectTileID(66, 0), 0)),
      RECOMBOBULATOR_TOWER_UPGRADE_1(new RecombobulatorTower(MapTilesLoader.calcObjectTileID(66, 1), 1)),
      RECOMBOBULATOR_TOWER_UPGRADE_2(new RecombobulatorTower(MapTilesLoader.calcObjectTileID(66, 2), 2)),
      RECOMBOBULATOR_TOWER_UPGRADE_3(new RecombobulatorTower(MapTilesLoader.calcObjectTileID(66, 3), 3)),
      ROCK_STORAGE(new RockStorage(MapTilesLoader.calcObjectTileID(67, 0), 0)),
      ROCK_STORAGE_UPGRADE_1(new RockStorage(MapTilesLoader.calcObjectTileID(67, 1), 1)),
      ROCK_STORAGE_UPGRADE_2(new RockStorage(MapTilesLoader.calcObjectTileID(67, 2), 2)),
      ROCK_STORAGE_UPGRADE_3(new RockStorage(MapTilesLoader.calcObjectTileID(67, 3), 3)),
      ROCK_STORAGE_UPGRADE_4(new RockStorage(MapTilesLoader.calcObjectTileID(67, 4), 4)),
      ROCK_TUMBLER(new RockTumbler(MapTilesLoader.calcObjectTileID(68, 0), 0)),
      ROCK_TUMBLER_UPGRADE_1(new RockTumbler(MapTilesLoader.calcObjectTileID(68, 1), 1)),
      ROCK_TUMBLER_UPGRADE_2(new RockTumbler(MapTilesLoader.calcObjectTileID(68, 2), 2)),
      STONE_CUTTERY(new StoneCuttery(MapTilesLoader.calcObjectTileID(69, 0), 0)),
      STONE_CUTTERY_UPGRADE_1(new StoneCuttery(MapTilesLoader.calcObjectTileID(69, 1), 1)),
      STONE_CUTTERY_UPGRADE_2(new StoneCuttery(MapTilesLoader.calcObjectTileID(69, 2), 2)),
      STONE_GOLEM_COMBOBULATOR(new StoneGolemCombobulator(MapTilesLoader.calcObjectTileID(70, 0), 0)),
      STONE_GOLEM_COMBOBULATOR_UPGRADE_1(new StoneGolemCombobulator(MapTilesLoader.calcObjectTileID(70, 1), 1)),
      STONE_GOLEM_COMBOBULATOR_UPGRADE_2(new StoneGolemCombobulator(MapTilesLoader.calcObjectTileID(70, 2), 2)),
      STONE_GOLEM_COMBOBULATOR_UPGRADE_3_LIGHTNING(new StoneGolemCombobulator(MapTilesLoader.calcObjectTileID(70, 3), 3)),
      STONE_GOLEM_COMBOBULATOR_UPGRADE_3_ICE(new StoneGolemCombobulator(MapTilesLoader.calcObjectTileID(70, 4), 4)),
      STONE_GOLEM_COMBOBULATOR_UPGRADE_3_FIRE(new StoneGolemCombobulator(MapTilesLoader.calcObjectTileID(70, 5), 5)),
      TOOLSMITHY(new Toolsmithy(MapTilesLoader.calcObjectTileID(71, 0), 0)),
      TOOLSMITHY_UPGRADE_1(new Toolsmithy(MapTilesLoader.calcObjectTileID(71, 1), 1)),
      TOOLSMITHY_UPGRADE_2(new Toolsmithy(MapTilesLoader.calcObjectTileID(71, 2), 2)),
      WATER_PURIFIER(new WaterPurifier(MapTilesLoader.calcObjectTileID(72, 0), 0)),
      WATER_PURIFIER_UPGRADE_1(new WaterPurifier(MapTilesLoader.calcObjectTileID(72, 1), 1)),
      WATER_PURIFIER_UPGRADE_2(new WaterPurifier(MapTilesLoader.calcObjectTileID(72, 2), 2)),
      WAY_MAKER_SHACK(new WayMakerShack(MapTilesLoader.calcObjectTileID(73, 0), 0)),
      WAY_MAKER_SHACK_UPGRADE_1(new WayMakerShack(MapTilesLoader.calcObjectTileID(73, 1), 1)),
      WAY_MAKER_SHACK_UPGRADE_2(new WayMakerShack(MapTilesLoader.calcObjectTileID(73, 2), 2)),
      WELL(new Well(MapTilesLoader.calcObjectTileID(74, 0), 0)),
      WELL_UPGRADE_1(new Well(MapTilesLoader.calcObjectTileID(74, 1), 1)),
      WELL_UPGRADE_2(new Well(MapTilesLoader.calcObjectTileID(74, 2), 2)),
      WOOD_GOLEM_COMBOBULATOR(new WoodGolemCombobulator(MapTilesLoader.calcObjectTileID(75, 0), 0)),
      WOOD_GOLEM_COMBOBULATOR_UPGRADE_1(new WoodGolemCombobulator(MapTilesLoader.calcObjectTileID(75, 1), 1)),
      WOOD_GOLEM_COMBOBULATOR_UPGRADE_2(new WoodGolemCombobulator(MapTilesLoader.calcObjectTileID(75, 2), 2)),
      WOOD_GOLEM_COMBOBULATOR_UPGRADE_3_LIGHTNING(new WoodGolemCombobulator(MapTilesLoader.calcObjectTileID(75, 3), 3)),
      WOOD_GOLEM_COMBOBULATOR_UPGRADE_3_ICE(new WoodGolemCombobulator(MapTilesLoader.calcObjectTileID(75, 4), 4)),
      WOOD_GOLEM_COMBOBULATOR_UPGRADE_3_POISON(new WoodGolemCombobulator(MapTilesLoader.calcObjectTileID(75, 5), 5)),
      WOOD_STORAGE(new WoodStorage(MapTilesLoader.calcObjectTileID(76, 0), 0)),
      WOOD_STORAGE_UPGRADE_1(new WoodStorage(MapTilesLoader.calcObjectTileID(76, 1), 1)),
      WOOD_STORAGE_UPGRADE_2(new WoodStorage(MapTilesLoader.calcObjectTileID(76, 2), 2)),
      WOOD_STORAGE_UPGRADE_3(new WoodStorage(MapTilesLoader.calcObjectTileID(76, 3), 3)),
      WOOD_STORAGE_UPGRADE_4(new WoodStorage(MapTilesLoader.calcObjectTileID(76, 4), 4)),
      WOOD_FENCE_GATE_NS(new WoodFenceGateNS(MapTilesLoader.calcObjectTileID(77, 0))),
      WOOD_FENCE_GATE_WE(new WoodFenceGateWE(MapTilesLoader.calcObjectTileID(77, 1))),
      STONE_WALL_GATE_NS(new StoneWallGateNS(MapTilesLoader.calcObjectTileID(78, 0))),
      STONE_WALL_GATE_WE(new StoneWallGateWE(MapTilesLoader.calcObjectTileID(78, 1))),
      BURNER(new Burner(MapTilesLoader.calcObjectTileID(79, 0), 0)),
      BURNER_UPGRADE_1(new Burner(MapTilesLoader.calcObjectTileID(79, 1), 1)),
      BURNER_UPGRADE_2(new Burner(MapTilesLoader.calcObjectTileID(79, 2), 2)),
      LANDFILL(new Landfill(MapTilesLoader.calcObjectTileID(80, 0), 0)),
      LANDFILL_UPGRADE_1(new Landfill(MapTilesLoader.calcObjectTileID(80, 1), 1)),
      LANDFILL_UPGRADE_2(new Landfill(MapTilesLoader.calcObjectTileID(80, 2), 2)),
      PROCESSOR(new Processor(MapTilesLoader.calcObjectTileID(81, 0), 0)),
      PROCESSOR_UPGRADE_1(new Processor(MapTilesLoader.calcObjectTileID(81, 1), 1)),
      PROCESSOR_UPGRADE_2(new Processor(MapTilesLoader.calcObjectTileID(81, 2), 2)),
      TRASH_CAN(new TrashCan(MapTilesLoader.calcObjectTileID(82, 0), 0)),
      TRASH_CAN_UPGRADE_1(new TrashCan(MapTilesLoader.calcObjectTileID(82, 1), 1)),
      TRASH_CAN_UPGRADE_2(new TrashCan(MapTilesLoader.calcObjectTileID(82, 2), 2)),
      TRASHY_CUBE_PILE(new TrashyCubePile(MapTilesLoader.calcObjectTileID(83, 0))),
      CUBE_E_GOLEM_COMBOBULATOR(new CubeEGolemCombobulator(MapTilesLoader.calcObjectTileID(84, 0), 0)),
      CUBE_E_GOLEM_COMBOBULATOR_UPGRADE_1(new CubeEGolemCombobulator(MapTilesLoader.calcObjectTileID(84, 1), 1)),
      CUBE_E_GOLEM_COMBOBULATOR_UPGRADE_2(new CubeEGolemCombobulator(MapTilesLoader.calcObjectTileID(84, 2), 2)),
      ANIMAL_PEN(new AnimalPen(MapTilesLoader.calcObjectTileID(85, 0), 0)),
      ANIMAL_PEN_UPGRADE_1(new AnimalPen(MapTilesLoader.calcObjectTileID(85, 1), 1)),
      ANIMAL_PEN_UPGRADE_2(new AnimalPen(MapTilesLoader.calcObjectTileID(85, 2), 2)),
      CLUCKER_COOP(new CluckerCoop(MapTilesLoader.calcObjectTileID(86, 0), 0)),
      CLUCKER_COOP_UPGRADE_1(new CluckerCoop(MapTilesLoader.calcObjectTileID(86, 1), 1)),
      CLUCKER_COOP_UPGRADE_2(new CluckerCoop(MapTilesLoader.calcObjectTileID(86, 2), 2)),
      CRYLITHIUM_WALL_GATE_NS(new CrylithiumWallGateNS(MapTilesLoader.calcObjectTileID(87, 0))),
      CRYLITHIUM_WALL_GATE_WE(new CrylithiumWallGateWE(MapTilesLoader.calcObjectTileID(87, 1))),
      GOD_TOWER(new GodTower(MapTilesLoader.calcObjectTileID(88, 0), 0)),
      ESSENCE_ALTAR(new EssenceAltar(MapTilesLoader.calcObjectTileID(89, 0), 0)),
      ESSENCE_ALTAR_UPGRADE_1(new EssenceAltar(MapTilesLoader.calcObjectTileID(89, 1), 1)),
      ESSENCE_ALTAR_UPGRADE_2(new EssenceAltar(MapTilesLoader.calcObjectTileID(89, 2), 2)),
      CRYLITHIUM_FIRE_PIT(new CrylithiumFirePit(MapTilesLoader.calcObjectTileID(90, 0))),
      LARGE_CRYLITHIUM_FIRE_PIT(new LargeCrylithiumFirePit(MapTilesLoader.calcObjectTileID(91, 0))),
      RELIQUARY(new Reliquary(MapTilesLoader.calcObjectTileID(92, 0), 0)),
      RELIQUARY_UPGRADE_1(new Reliquary(MapTilesLoader.calcObjectTileID(92, 1), 1)),
      RELIQUARY_UPGRADE_2(new Reliquary(MapTilesLoader.calcObjectTileID(92, 2), 2)),
      TREES_BLUE_COLLECTED(new TreesBlueCollected(MapTilesLoader.calcTerrainTileID(0, 3))),
      TREES_BLUE_ACCENT(new TreesBlueAccent(MapTilesLoader.calcTerrainTileID(0, 2))),
      TREES_BLUE_DEAD(new TreesBlueDead(MapTilesLoader.calcTerrainTileID(0, 1)), null, TREES_BLUE_COLLECTED),
      TREES_BLUE(new TreesBlue(MapTilesLoader.calcTerrainTileID(0, 0)), TREES_BLUE_ACCENT, TREES_BLUE_COLLECTED, TREES_BLUE_DEAD),
      TREES_BROWN_COLLECTED(new TreesBrownCollected(MapTilesLoader.calcTerrainTileID(0, 13))),
      TREES_BROWN_ACCENT(new TreesBrownAccent(MapTilesLoader.calcTerrainTileID(0, 12))),
      TREES_BROWN_DEAD(new TreesBrownDead(MapTilesLoader.calcTerrainTileID(0, 11)), null, TREES_BROWN_COLLECTED),
      TREES_BROWN(new TreesBrown(MapTilesLoader.calcTerrainTileID(0, 10)), TREES_BROWN_ACCENT, TREES_BROWN_COLLECTED, TREES_BROWN_DEAD),
      TREES_DARK_GREEN_COLLECTED(new TreesDarkGreenCollected(MapTilesLoader.calcTerrainTileID(0, 23))),
      TREES_DARK_GREEN_ACCENT(new TreesDarkGreenAccent(MapTilesLoader.calcTerrainTileID(0, 22))),
      TREES_DARK_GREEN_DEAD(new TreesDarkGreenDead(MapTilesLoader.calcTerrainTileID(0, 21)), null, TREES_DARK_GREEN_COLLECTED),
      TREES_DARK_GREEN(new TreesDarkGreen(MapTilesLoader.calcTerrainTileID(0, 20)), TREES_DARK_GREEN_ACCENT, TREES_DARK_GREEN_COLLECTED, TREES_DARK_GREEN_DEAD),
      TREES_GREEN_COLLECTED(new TreesGreenCollected(MapTilesLoader.calcTerrainTileID(0, 33))),
      TREES_GREEN_ACCENT(new TreesGreenAccent(MapTilesLoader.calcTerrainTileID(0, 32))),
      TREES_GREEN_DEAD(new TreesGreenDead(MapTilesLoader.calcTerrainTileID(0, 31)), null, TREES_GREEN_COLLECTED),
      TREES_GREEN(new TreesGreen(MapTilesLoader.calcTerrainTileID(0, 30)), TREES_GREEN_ACCENT, TREES_GREEN_COLLECTED, TREES_GREEN_DEAD),
      TREES_RED_COLLECTED(new TreesRedCollected(MapTilesLoader.calcTerrainTileID(0, 43))),
      TREES_RED_ACCENT(new TreesRedAccent(MapTilesLoader.calcTerrainTileID(0, 42))),
      TREES_RED_DEAD(new TreesRedDead(MapTilesLoader.calcTerrainTileID(0, 41)), null, TREES_RED_COLLECTED),
      TREES_RED(new TreesRed(MapTilesLoader.calcTerrainTileID(0, 40)), TREES_RED_ACCENT, TREES_RED_COLLECTED, TREES_RED_DEAD),
      TREES_LAVENDER_COLLECTED(new TreesLavenderCollected(MapTilesLoader.calcTerrainTileID(0, 53))),
      TREES_LAVENDER_ACCENT(new TreesLavenderAccent(MapTilesLoader.calcTerrainTileID(0, 52))),
      TREES_LAVENDER_DEAD(new TreesLavenderDead(MapTilesLoader.calcTerrainTileID(0, 51)), null, TREES_LAVENDER_COLLECTED),
      TREES_LAVENDER(new TreesLavender(MapTilesLoader.calcTerrainTileID(0, 50)), TREES_LAVENDER_ACCENT, TREES_LAVENDER_COLLECTED, TREES_LAVENDER_DEAD),
      TREES_PALE_BLUE_COLLECTED(new TreesPaleBlueCollected(MapTilesLoader.calcTerrainTileID(0, 63))),
      TREES_PALE_BLUE_ACCENT(new TreesPaleBlueAccent(MapTilesLoader.calcTerrainTileID(0, 62))),
      TREES_PALE_BLUE_DEAD(new TreesPaleBlueDead(MapTilesLoader.calcTerrainTileID(0, 61)), null, TREES_PALE_BLUE_COLLECTED),
      TREES_PALE_BLUE(new TreesPaleBlue(MapTilesLoader.calcTerrainTileID(0, 60)), TREES_PALE_BLUE_ACCENT, TREES_PALE_BLUE_COLLECTED, TREES_PALE_BLUE_DEAD),
      ROCK_BLACK_COLLECTED(new RockBlackCollected(MapTilesLoader.calcTerrainTileID(1, 2))),
      ROCK_BLACK_ACCENT(new RockBlackAccent(MapTilesLoader.calcTerrainTileID(1, 1))),
      ROCK_BLACK(new RockBlack(MapTilesLoader.calcTerrainTileID(1, 0)), ROCK_BLACK_ACCENT, ROCK_BLACK_COLLECTED),
      ROCK_BROWN_COLLECTED(new RockBrownCollected(MapTilesLoader.calcTerrainTileID(1, 12))),
      ROCK_BROWN_ACCENT(new RockBrownAccent(MapTilesLoader.calcTerrainTileID(1, 11))),
      ROCK_BROWN(new RockBrown(MapTilesLoader.calcTerrainTileID(1, 10)), ROCK_BROWN_ACCENT, ROCK_BROWN_COLLECTED),
      ROCK_GRAY_COLLECTED(new RockGrayCollected(MapTilesLoader.calcTerrainTileID(1, 22))),
      ROCK_GRAY_ACCENT(new RockGrayAccent(MapTilesLoader.calcTerrainTileID(1, 21))),
      ROCK_GRAY(new RockGray(MapTilesLoader.calcTerrainTileID(1, 20)), ROCK_GRAY_ACCENT, ROCK_GRAY_COLLECTED),
      ROCK_RED_COLLECTED(new RockRedCollected(MapTilesLoader.calcTerrainTileID(1, 32))),
      ROCK_RED_ACCENT(new RockRedAccent(MapTilesLoader.calcTerrainTileID(1, 31))),
      ROCK_RED(new RockRed(MapTilesLoader.calcTerrainTileID(1, 30)), ROCK_RED_ACCENT, ROCK_RED_COLLECTED),
      ROCK_WHITE_COLLECTED(new RockWhiteCollected(MapTilesLoader.calcTerrainTileID(1, 42))),
      ROCK_WHITE_ACCENT(new RockWhiteAccent(MapTilesLoader.calcTerrainTileID(1, 41))),
      ROCK_WHITE(new RockWhite(MapTilesLoader.calcTerrainTileID(1, 40)), ROCK_WHITE_ACCENT, ROCK_WHITE_COLLECTED),
      SANDSTONE_BLACK_COLLECTED(new SandstoneBlackCollected(MapTilesLoader.calcTerrainTileID(2, 2))),
      SANDSTONE_BLACK_ACCENT(new SandstoneBlackAccent(MapTilesLoader.calcTerrainTileID(2, 1))),
      SANDSTONE_BLACK(new SandstoneBlack(MapTilesLoader.calcTerrainTileID(2, 0)), SANDSTONE_BLACK_ACCENT, SANDSTONE_BLACK_COLLECTED),
      SANDSTONE_RED_COLLECTED(new SandstoneRedCollected(MapTilesLoader.calcTerrainTileID(2, 12))),
      SANDSTONE_RED_ACCENT(new SandstoneRedAccent(MapTilesLoader.calcTerrainTileID(2, 11))),
      SANDSTONE_RED(new SandstoneRed(MapTilesLoader.calcTerrainTileID(2, 10)), SANDSTONE_RED_ACCENT, SANDSTONE_RED_COLLECTED),
      SANDSTONE_TAN_COLLECTED(new SandstoneTanCollected(MapTilesLoader.calcTerrainTileID(2, 22))),
      SANDSTONE_TAN_ACCENT(new SandstoneTanAccent(MapTilesLoader.calcTerrainTileID(2, 21))),
      SANDSTONE_TAN(new SandstoneTan(MapTilesLoader.calcTerrainTileID(2, 20)), SANDSTONE_TAN_ACCENT, SANDSTONE_TAN_COLLECTED),
      CRYSTAL_RED_COLLECTED(new CrystalsRedCollected(MapTilesLoader.calcTerrainTileID(3, 2))),
      CRYSTAL_RED_ACCENT(new CrystalsRedAccent(MapTilesLoader.calcTerrainTileID(3, 1))),
      CRYSTAL_RED(new CrystalsRed(MapTilesLoader.calcTerrainTileID(3, 0)), CRYSTAL_RED_ACCENT, CRYSTAL_RED_COLLECTED),
      CRYSTAL_GREEN_COLLECTED(new CrystalsGreenCollected(MapTilesLoader.calcTerrainTileID(3, 12))),
      CRYSTAL_GREEN_ACCENT(new CrystalsGreenAccent(MapTilesLoader.calcTerrainTileID(3, 11))),
      CRYSTAL_GREEN(new CrystalsGreen(MapTilesLoader.calcTerrainTileID(3, 10)), CRYSTAL_GREEN_ACCENT, CRYSTAL_GREEN_COLLECTED),
      CRYSTAL_BLUE_COLLECTED(new CrystalsBlueCollected(MapTilesLoader.calcTerrainTileID(3, 22))),
      CRYSTAL_BLUE_ACCENT(new CrystalsBlueAccent(MapTilesLoader.calcTerrainTileID(3, 21))),
      CRYSTAL_BLUE(new CrystalsBlue(MapTilesLoader.calcTerrainTileID(3, 20)), CRYSTAL_BLUE_ACCENT, CRYSTAL_BLUE_COLLECTED),
      CRYSTAL_PURPLE_COLLECTED(new CrystalsPurpleCollected(MapTilesLoader.calcTerrainTileID(3, 32))),
      CRYSTAL_PURPLE_ACCENT(new CrystalsPurpleAccent(MapTilesLoader.calcTerrainTileID(3, 31))),
      CRYSTAL_PURPLE(new CrystalsPurple(MapTilesLoader.calcTerrainTileID(3, 30)), CRYSTAL_PURPLE_ACCENT, CRYSTAL_PURPLE_COLLECTED),
      POTATOES_COLLECTED(new PotatoesCollected(MapTilesLoader.calcTerrainTileID(4, 2))),
      POTATOES_ACCENT(new PotatoesAccent(MapTilesLoader.calcTerrainTileID(4, 1))),
      POTATOES(new Potatoes(MapTilesLoader.calcTerrainTileID(4, 0)), POTATOES_ACCENT, POTATOES_COLLECTED),
      HOLY_POTATOES_COLLECTED(new PotatoesHolyCollected(MapTilesLoader.calcTerrainTileID(4, 5))),
      HOLY_POTATOES_ACCENT(new PotatoesHolyAccent(MapTilesLoader.calcTerrainTileID(4, 4))),
      HOLY_POTATOES(new PotatoesHoly(MapTilesLoader.calcTerrainTileID(4, 3)), HOLY_POTATOES_ACCENT, HOLY_POTATOES_COLLECTED),
      TURNIPS_COLLECTED(new TurnipsCollected(MapTilesLoader.calcTerrainTileID(5, 2))),
      TURNIPS_ACCENT(new TurnipsAccent(MapTilesLoader.calcTerrainTileID(5, 1))),
      TURNIPS(new Turnips(MapTilesLoader.calcTerrainTileID(5, 0)), TURNIPS_ACCENT, TURNIPS_COLLECTED),
      CACTUS_GREEN_COLLECTED(new CactusGreenCollected(MapTilesLoader.calcTerrainTileID(6, 2))),
      CACTUS_GREEN_ACCENT(new CactusGreenAccent(MapTilesLoader.calcTerrainTileID(6, 1))),
      CACTUS_GREEN(new CactusGreen(MapTilesLoader.calcTerrainTileID(6, 0)), CACTUS_GREEN_ACCENT, CACTUS_GREEN_COLLECTED),
      CARROTS_COLLECTED(new CarrotsCollected(MapTilesLoader.calcTerrainTileID(7, 2))),
      CARROTS_ACCENT(new CarrotsAccent(MapTilesLoader.calcTerrainTileID(7, 1))),
      CARROTS(new Carrots(MapTilesLoader.calcTerrainTileID(7, 0)), CARROTS_ACCENT, CARROTS_COLLECTED),
      MELONS_COLLECTED(new MelonsCollected(MapTilesLoader.calcTerrainTileID(8, 2))),
      MELONS_ACCENT(new MelonsAccent(MapTilesLoader.calcTerrainTileID(8, 1))),
      MELONS(new Melons(MapTilesLoader.calcTerrainTileID(8, 0)), MELONS_ACCENT, MELONS_COLLECTED),
      BRICKS_ACCENT(new BricksAccent(MapTilesLoader.calcTerrainTileID(9, 1))),
      BRICKS(new Bricks(MapTilesLoader.calcTerrainTileID(9, 0)), BRICKS_ACCENT),
      DIRT_BROWN_ACCENT(new DirtBrownAccent(MapTilesLoader.calcTerrainTileID(10, 1))),
      DIRT_BROWN(new DirtBrown(MapTilesLoader.calcTerrainTileID(10, 0)), DIRT_BROWN_ACCENT),
      DIRT_DARK_BROWN_ACCENT(new DirtDarkBrownAccent(MapTilesLoader.calcTerrainTileID(10, 11))),
      DIRT_DARK_BROWN(new DirtDarkBrown(MapTilesLoader.calcTerrainTileID(10, 10)), DIRT_DARK_BROWN_ACCENT),
      DIRT_LIGHT_BROWN_ACCENT(new DirtLightBrownAccent(MapTilesLoader.calcTerrainTileID(10, 21))),
      DIRT_LIGHT_BROWN(new DirtLightBrown(MapTilesLoader.calcTerrainTileID(10, 20)), DIRT_LIGHT_BROWN_ACCENT),
      FLOWERS_BLACK_ACCENT(new FlowersBlackAccent(MapTilesLoader.calcTerrainTileID(11, 1))),
      FLOWERS_BLACK(new FlowersBlack(MapTilesLoader.calcTerrainTileID(11, 0)), FLOWERS_BLACK_ACCENT),
      FLOWERS_BLUE_ACCENT(new FlowersBlueAccent(MapTilesLoader.calcTerrainTileID(11, 11))),
      FLOWERS_BLUE(new FlowersBlue(MapTilesLoader.calcTerrainTileID(11, 10)), FLOWERS_BLUE_ACCENT),
      FLOWERS_PURPLE_ACCENT(new FlowersPurpleAccent(MapTilesLoader.calcTerrainTileID(11, 21))),
      FLOWERS_PURPLE(new FlowersPurple(MapTilesLoader.calcTerrainTileID(11, 20)), FLOWERS_PURPLE_ACCENT),
      FLOWERS_RED_ACCENT(new FlowersRedAccent(MapTilesLoader.calcTerrainTileID(11, 31))),
      FLOWERS_RED(new FlowersRed(MapTilesLoader.calcTerrainTileID(11, 30)), FLOWERS_RED_ACCENT),
      FLOWERS_WHITE_ACCENT(new FlowersWhiteAccent(MapTilesLoader.calcTerrainTileID(11, 41))),
      FLOWERS_WHITE(new FlowersWhite(MapTilesLoader.calcTerrainTileID(11, 40)), FLOWERS_WHITE_ACCENT),
      FLOWERS_YELLOW_ACCENT(new FlowersYellowAccent(MapTilesLoader.calcTerrainTileID(11, 51))),
      FLOWERS_YELLOW(new FlowersYellow(MapTilesLoader.calcTerrainTileID(11, 50)), FLOWERS_YELLOW_ACCENT),
      GRASS_EMERALD_GREEN_ACCENT(new GrassEmeraldGreenAccent(MapTilesLoader.calcTerrainTileID(12, 1))),
      GRASS_EMERALD_GREEN(new GrassEmeraldGreen(MapTilesLoader.calcTerrainTileID(12, 0)), GRASS_EMERALD_GREEN_ACCENT),
      GRASS_GREEN_ACCENT(new GrassGreenAccent(MapTilesLoader.calcTerrainTileID(12, 11))),
      GRASS_GREEN(new GrassGreen(MapTilesLoader.calcTerrainTileID(12, 10)), GRASS_GREEN_ACCENT),
      GRASS_YELLOW_BROWN_ACCENT(new GrassYellowBrownAccent(MapTilesLoader.calcTerrainTileID(12, 21))),
      GRASS_YELLOW_BROWN(new GrassYellowBrown(MapTilesLoader.calcTerrainTileID(12, 20)), GRASS_YELLOW_BROWN_ACCENT),
      GRASS_TEAL_ACCENT(new GrassTealAccent(MapTilesLoader.calcTerrainTileID(12, 31))),
      GRASS_TEAL(new GrassTeal(MapTilesLoader.calcTerrainTileID(12, 30)), GRASS_TEAL_ACCENT),
      GRAVEL_BLUE_ACCENT(new GravelBlueAccent(MapTilesLoader.calcTerrainTileID(13, 31))),
      GRAVEL_BLUE(new GravelBlue(MapTilesLoader.calcTerrainTileID(13, 30)), GRAVEL_BLUE_ACCENT),
      GRAVEL_GRAY_ACCENT(new GravelGrayAccent(MapTilesLoader.calcTerrainTileID(13, 41))),
      GRAVEL_GRAY(new GravelGray(MapTilesLoader.calcTerrainTileID(13, 40)), GRAVEL_GRAY_ACCENT),
      GRAVEL_RED_ACCENT(new GravelRedAccent(MapTilesLoader.calcTerrainTileID(13, 51))),
      GRAVEL_RED(new GravelRed(MapTilesLoader.calcTerrainTileID(13, 50)), GRAVEL_RED_ACCENT),
      LAVA_ACCENT(new LavaAccent(MapTilesLoader.calcTerrainTileID(14, 1))),
      LAVA(new Lava(MapTilesLoader.calcTerrainTileID(14, 0)), LAVA_ACCENT),
      SAND_BLACK_ACCENT(new SandBlackAccent(MapTilesLoader.calcTerrainTileID(15, 1))),
      SAND_BLACK(new SandBlack(MapTilesLoader.calcTerrainTileID(15, 0)), SAND_BLACK_ACCENT),
      SAND_RED_ACCENT(new SandRedAccent(MapTilesLoader.calcTerrainTileID(15, 11))),
      SAND_RED(new SandRed(MapTilesLoader.calcTerrainTileID(15, 10)), SAND_RED_ACCENT),
      SAND_TAN_ACCENT(new SandTanAccent(MapTilesLoader.calcTerrainTileID(15, 21))),
      SAND_TAN(new SandTan(MapTilesLoader.calcTerrainTileID(15, 20)), SAND_TAN_ACCENT),
      TAR_ACCENT(new TarAccent(MapTilesLoader.calcTerrainTileID(16, 1))),
      TAR(new Tar(MapTilesLoader.calcTerrainTileID(16, 0)), TAR_ACCENT),
      TILES_ACCENT(new TilesAccent(MapTilesLoader.calcTerrainTileID(17, 1))),
      TILES(new Tiles(MapTilesLoader.calcTerrainTileID(17, 0)), TILES_ACCENT),
      WATER_ACCENT(new WaterAccent(MapTilesLoader.calcTerrainTileID(18, 1))),
      WATER(new Water(MapTilesLoader.calcTerrainTileID(18, 0)), WATER_ACCENT),
      WATER_DRY_ACCENT(new WaterDryAccent(MapTilesLoader.calcTerrainTileID(18, 11))),
      WATER_DRY(new WaterDry(MapTilesLoader.calcTerrainTileID(18, 10)), WATER_DRY_ACCENT),
      WATER_PARTIALLY_DRY_ACCENT(new WaterPartiallyDryAccent(MapTilesLoader.calcTerrainTileID(18, 21))),
      WATER_PARTIALLY_DRY(new WaterPartiallyDry(MapTilesLoader.calcTerrainTileID(18, 20)), WATER_PARTIALLY_DRY_ACCENT),
      WATER_PARTIALLY_DRY_FROZEN_ACCENT(new WaterPartiallyDryFrozenAccent(MapTilesLoader.calcTerrainTileID(18, 31))),
      WATER_PARTIALLY_DRY_FROZEN(new WaterPartiallyDryFrozen(MapTilesLoader.calcTerrainTileID(18, 30)), WATER_PARTIALLY_DRY_FROZEN_ACCENT),
      WATER_PARTIALLY_FROZEN_ACCENT(new WaterPartiallyFrozenAccent(MapTilesLoader.calcTerrainTileID(18, 41))),
      WATER_PARTIALLY_FROZEN(new WaterPartiallyFrozen(MapTilesLoader.calcTerrainTileID(18, 40)), WATER_PARTIALLY_FROZEN_ACCENT),
      WATER_FROZEN_ACCENT(new WaterFrozenAccent(MapTilesLoader.calcTerrainTileID(18, 51))),
      WATER_FROZEN(new WaterFrozen(MapTilesLoader.calcTerrainTileID(18, 50)), WATER_FROZEN_ACCENT),
      CORRUPTION_ACCENT(new CorruptionAccent(MapTilesLoader.calcTerrainTileID(19, 1))),
      CORRUPTION(new Corruption(MapTilesLoader.calcTerrainTileID(19, 0)), CORRUPTION_ACCENT),
      PATH(new Path(MapTilesLoader.calcTerrainTileID(20, 0))),
      LOG_PATH_DEBRIS(new LogPathDebris(MapTilesLoader.calcTerrainTileID(20, 1))),
      LOG_PATH(new LogPath(MapTilesLoader.calcTerrainTileID(20, 2))),
      COBBLE_AND_LOG_PATH_DEBRIS(new CobbleAndLogPathDebris(MapTilesLoader.calcTerrainTileID(20, 3))),
      COBBLE_AND_LOG_PATH(new CobbleAndLogPath(MapTilesLoader.calcTerrainTileID(20, 4))),
      COBBLE_AND_BOARD_ROAD_DEBRIS(new CobbleAndBoardRoadDebris(MapTilesLoader.calcTerrainTileID(20, 5))),
      COBBLE_AND_BOARD_ROAD(new CobbleAndBoardRoad(MapTilesLoader.calcTerrainTileID(20, 6))),
      CUT_STONE_AND_BOARD_ROAD_DEBRIS(new CutStoneAndBoardRoadDebris(MapTilesLoader.calcTerrainTileID(20, 7))),
      CUT_STONE_AND_BOARD_ROAD(new CutStoneAndBoardRoad(MapTilesLoader.calcTerrainTileID(20, 8))),
      CURTAIN_WALL_OUTLINE(new CurtainWall(MapTilesLoader.calcTerrainTileID(21, 0), 0)),
      CURTAIN_WALL_CONSTRUCTION(new CurtainWall(MapTilesLoader.calcTerrainTileID(21, 1), 1)),
      CURTAIN_WALL(new CurtainWall(MapTilesLoader.calcTerrainTileID(21, 2), 2), CURTAIN_WALL_OUTLINE, CURTAIN_WALL_CONSTRUCTION),
      STONE_WALL_OUTLINE(new StoneWall(MapTilesLoader.calcTerrainTileID(21, 3), 0)),
      STONE_WALL_CONSTRUCTION(new StoneWall(MapTilesLoader.calcTerrainTileID(21, 4), 1)),
      STONE_WALL(new StoneWall(MapTilesLoader.calcTerrainTileID(21, 5), 2), STONE_WALL_OUTLINE, STONE_WALL_CONSTRUCTION),
      WOOD_FENCE_OUTLINE(new WoodFence(MapTilesLoader.calcTerrainTileID(21, 6), 0)),
      WOOD_FENCE_CONSTRUCTION(new WoodFence(MapTilesLoader.calcTerrainTileID(21, 7), 1)),
      WOOD_FENCE(new WoodFence(MapTilesLoader.calcTerrainTileID(21, 8), 2), WOOD_FENCE_OUTLINE, WOOD_FENCE_CONSTRUCTION),
      TRASHY_CUBE_WALL_OUTLINE(new TrashyCubeWall(MapTilesLoader.calcTerrainTileID(21, 9), 0)),
      TRASHY_CUBE_WALL_CONSTRUCTION(new TrashyCubeWall(MapTilesLoader.calcTerrainTileID(21, 10), 1)),
      TRASHY_CUBE_WALL(new TrashyCubeWall(MapTilesLoader.calcTerrainTileID(21, 11), 2), TRASHY_CUBE_WALL_OUTLINE, TRASHY_CUBE_WALL_CONSTRUCTION),
      CRYLITHIUM_WALL_OUTLINE(new CrylithiumWall(MapTilesLoader.calcTerrainTileID(21, 12), 0)),
      CRYLITHIUM_WALL_CONSTRUCTION(new CrylithiumWall(MapTilesLoader.calcTerrainTileID(21, 13), 1)),
      CRYLITHIUM_WALL(new CrylithiumWall(MapTilesLoader.calcTerrainTileID(21, 14), 2), CRYLITHIUM_WALL_OUTLINE, CRYLITHIUM_WALL_CONSTRUCTION),
      CRYLITHIUM_CURTAIN_WALL_OUTLINE(new CrylithiumCurtainWall(MapTilesLoader.calcTerrainTileID(21, 15), 0)),
      CRYLITHIUM_CURTAIN_WALL_CONSTRUCTION(new CrylithiumCurtainWall(MapTilesLoader.calcTerrainTileID(21, 16), 1)),
      CRYLITHIUM_CURTAIN_WALL(
         new CrylithiumCurtainWall(MapTilesLoader.calcTerrainTileID(21, 17), 2), CRYLITHIUM_CURTAIN_WALL_OUTLINE, CRYLITHIUM_CURTAIN_WALL_CONSTRUCTION
      ),
      GOD_WALL_OUTLINE(new GodWall(MapTilesLoader.calcTerrainTileID(21, 18), 0)),
      GOD_WALL_CONSTRUCTION(new GodWall(MapTilesLoader.calcTerrainTileID(21, 19), 1)),
      GOD_WALL(new GodWall(MapTilesLoader.calcTerrainTileID(21, 20), 2), CRYLITHIUM_CURTAIN_WALL_OUTLINE, CRYLITHIUM_CURTAIN_WALL_CONSTRUCTION),
      SNOW_WHITE_ACCENT(new SnowWhiteAccent(MapTilesLoader.calcTerrainTileID(22, 1))),
      SNOW_WHITE(new SnowWhite(MapTilesLoader.calcTerrainTileID(22, 0)), SNOW_WHITE_ACCENT),
      MUSHROOMS_COLLECTED(new MushroomsCollected(MapTilesLoader.calcTerrainTileID(23, 2))),
      MUSHROOMS_ACCENT(new MushroomsAccent(MapTilesLoader.calcTerrainTileID(23, 1))),
      MUSHROOMS(new Mushrooms(MapTilesLoader.calcTerrainTileID(23, 0)), MUSHROOMS_ACCENT, MUSHROOMS_COLLECTED);

      private TileSetBase tileSetTemplate;
      private MapTilesLoader.TileSet accentTileSet;
      private MapTilesLoader.TileSet collectedTileSet;
      private MapTilesLoader.TileSet deadTileSet;

      private TileSet(TileSetBase template) {
         this.tileSetTemplate = template;
      }

      private TileSet(TileSetBase template, MapTilesLoader.TileSet accent, MapTilesLoader.TileSet collected, MapTilesLoader.TileSet dead) {
         this.tileSetTemplate = template;
         this.accentTileSet = accent;
         this.collectedTileSet = collected;
         this.deadTileSet = dead;
      }

      private TileSet(TileSetBase template, MapTilesLoader.TileSet accent, MapTilesLoader.TileSet collected) {
         this.tileSetTemplate = template;
         this.accentTileSet = accent;
         this.collectedTileSet = collected;
      }

      private TileSet(TileSetBase template, MapTilesLoader.TileSet accent) {
         this.tileSetTemplate = template;
         this.accentTileSet = accent;
      }

      public TileSetBase getTileSetTemplate() {
         return this.tileSetTemplate;
      }

      public MapTilesLoader.TileSet getAccent() {
         return this.accentTileSet;
      }

      public MapTilesLoader.TileSet getCollected() {
         return this.collectedTileSet;
      }

      public MapTilesLoader.TileSet getWallOutline() {
         return this.accentTileSet;
      }

      public MapTilesLoader.TileSet getWallConstruction() {
         return this.collectedTileSet;
      }

      public MapTilesLoader.TileSet getDead() {
         return this.deadTileSet;
      }
   }

   public static enum TileSetObjectPhase {
      ABANDONED(5500),
      BASE(0),
      CONSTRUCTION_OUTLINE(5000),
      CONSTRUCTION_PHASE_5(4500),
      CONSTRUCTION_PHASE_4(4000),
      CONSTRUCTION_PHASE_3(3500),
      CONSTRUCTION_PHASE_2(3000),
      CONSTRUCTION_PHASE_1(2500),
      FILL_PHASE_4(2000),
      FILL_PHASE_3(1500),
      FILL_PHASE_2(1000),
      FILL_PHASE_1(500);

      private int tileOffset;

      private TileSetObjectPhase(int tileOffset) {
         this.tileOffset = tileOffset;
      }

      public int getTileOffset() {
         return this.tileOffset;
      }
   }

   public static enum TileSetType {
      TERRAIN,
      OBJECT,
      WALL,
      ROAD;
   }
}
