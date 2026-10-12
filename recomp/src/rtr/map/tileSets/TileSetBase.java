package rtr.map.tileSets;

import org.newdawn.slick.Color;
import rtr.map.MapTilesLoader;
import rtr.resources.ResourceModule;

public class TileSetBase {
   protected String name;
   protected String fileName;
   protected String description;
   protected int tileGID;
   protected byte layer;
   protected MapTilesLoader.TileSetType type;
   protected int upgradePhase = -1;
   protected Color miniMapColor;
   protected ResourceModule.ResourceType resourceType;
   protected byte resourceAmount;
   protected byte resourceAmountMax;
   protected ResourceModule.ResourceColorSet resourceColorSet;
   protected int objectWidth;
   protected int objectHeight;
   protected MapTilesLoader.TileSet canRegrow;
   protected int regrowRate;
   protected boolean regrowOnlyMotivated;
   protected boolean regrowIgnoreWeather;
   protected MapTilesLoader.TileSet canSpread;
   protected int spreadRate;
   protected boolean spreadOnlyMotivated;
   protected boolean spreadIgnoreWeather;
   protected int essenceAmount;

   public TileSetBase() {
   }

   public TileSetBase(int tGID, MapTilesLoader.TileSetType t) {
      this.tileGID = tGID;
      this.type = t;
   }

   public TileSetBase(int tGID, int uP, MapTilesLoader.TileSetType t) {
      this.tileGID = tGID;
      this.type = t;
      this.upgradePhase = uP;
   }

   public void initialize() {
   }

   public String getName() {
      return this.name;
   }

   public String getFileName() {
      return this.fileName;
   }

   public String getDescription() {
      return this.description;
   }

   public int getTileGID() {
      return this.tileGID;
   }

   public byte getLayer() {
      return this.layer;
   }

   public MapTilesLoader.TileSetType getType() {
      return this.type;
   }

   public Color getMiniMapColor() {
      return this.miniMapColor == null ? new Color(255, 0, 255, 255) : this.miniMapColor;
   }

   public ResourceModule.ResourceType getResourceType() {
      return this.resourceType;
   }

   public byte getResourceAmount() {
      return this.resourceAmount;
   }

   public byte getResourceAmountMax() {
      return this.resourceAmountMax;
   }

   public ResourceModule.ResourceColorSet getResourceColorSet() {
      return this.resourceColorSet;
   }

   public int getObjectWidth() {
      return this.objectWidth;
   }

   public int getObjectHeight() {
      return this.objectHeight;
   }

   public MapTilesLoader.TileSet canRegrow() {
      return this.canRegrow == null ? null : this.canRegrow;
   }

   public int getRegrowRate() {
      return this.canRegrow == null ? 0 : this.regrowRate;
   }

   public boolean getRegrowOnlyMotivated() {
      return this.regrowOnlyMotivated;
   }

   public boolean getRegrowIgnoreWeather() {
      return this.regrowIgnoreWeather;
   }

   public MapTilesLoader.TileSet canSpread() {
      return this.canSpread == null ? null : this.canSpread;
   }

   public boolean getSpreadOnlyMotivated() {
      return this.spreadOnlyMotivated;
   }

   public boolean getSpreadIgnoreWeather() {
      return this.spreadIgnoreWeather;
   }

   public int getSpreadRate() {
      return this.canSpread == null ? 0 : this.spreadRate;
   }

   public int getEssenceAmount() {
      return this.essenceAmount;
   }
}
