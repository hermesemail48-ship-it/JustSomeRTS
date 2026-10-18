package rtr.lighting;

import java.util.ArrayList;
import java.util.Arrays;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import rtr.ImageLoader;
import rtr.ModuleBase;
import rtr.TimeModule;
import rtr.font.Text;
import rtr.missiles.MissileBase;
import rtr.mobs.MobBase;
import rtr.states.StateBase;
import rtr.system.ScaleControl;
import rtr.utilities.ArrayFillUtil;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;
import rtr.weather.disasters.DisasterBase;

public class LightingModule extends ModuleBase {
   private static final int colorRangeAmount = 75;
   private Color lightLevel;
   private Color lightColorCurrent;
   private Color lightColorSet;
   private Image light;
   private Image coloredLight;
   private float lightAlpha;
   private float lightAdjust;
   private Light mouseLight;
   private ArrayList<Light> mapLightsAll = new ArrayList<>();
   private ArrayList<Light> shortTermLights = new ArrayList<>();
   private int[][] lightValueBase;
   private int[][] lightValueExtrasNew;
   private int[][] lightValueMapNewCurrent;
   private int[][] lightValueMapNew;
   private Color[] colorRange;
   private int mapWidth;
   private int mapHeight;
   private int mapLayers;
   private LightNode[][] lightNodes;
   private ArrayList<LightNode> resettableLightNodes = new ArrayList<>();
   private LightNode[][] lightNodesThreaded;
   private ArrayList<LightNode> resettableLightNodesThreaded = new ArrayList<>();
   private LightingModule.LightUpdateStep lightUpdateStep = LightingModule.LightUpdateStep.FINISH;

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      if (this.light == null) {
         this.light = ImageLoader.getImage("res/lightSources/light.png");
      }

      if (this.coloredLight == null) {
         this.coloredLight = ImageLoader.getImage("res/lightSources/coloredLight.png");
      }

      this.mapWidth = 256;
      this.mapHeight = 256;
      this.mapLayers = 12;
      if (this.lightValueBase == null) {
         this.lightValueBase = new int[this.mapWidth][this.mapHeight];
      }

      if (this.lightValueExtrasNew == null) {
         this.lightValueExtrasNew = new int[this.mapWidth][this.mapHeight];
      }

      if (this.lightValueMapNew == null) {
         this.lightValueMapNew = new int[this.mapWidth][this.mapHeight];
      }

      if (this.lightValueMapNewCurrent == null) {
         this.lightValueMapNewCurrent = new int[this.mapWidth][this.mapHeight];
      }

      this.lightNodes = new LightNode[this.mapWidth][this.mapHeight];
      this.lightNodesThreaded = new LightNode[this.mapWidth][this.mapHeight];

      for (int x = 0; x < this.lightNodes.length; x++) {
         for (int y = 0; y < this.lightNodes[x].length; y++) {
            this.lightNodes[x][y] = new LightNode(x, y);
            this.lightNodesThreaded[x][y] = new LightNode(x, y);
         }
      }

      this.buildInitialMap();
      if (this.colorRange == null) {
         this.colorRange = new Color[75];

         for (int x = 0; x < this.colorRange.length; x++) {
            this.colorRange[x] = new Color(1.0F, 1.0F, 1.0F, x * 0.005F);
            if (x <= 50) {
               this.colorRange[x] = new Color(1.0F, 1.0F, 1.0F, x * 0.02F);
            } else if (x <= 100) {
               this.colorRange[x] = new Color(1.0F, 1.0F, 1.0F, x * 0.03F);
            } else if (x <= 200) {
               this.colorRange[x] = new Color(1.0F, 1.0F, 1.0F, x * 0.04F);
            }
         }
      }

      if (this.time.getTimeOfDay() == TimeModule.TimeOfDay.NIGHT) {
         this.lightAlpha = 90.0F;
      } else if (this.time.getTimeOfDay() == TimeModule.TimeOfDay.DAWN) {
         this.lightAlpha = 160.0F;
      } else if (this.time.getTimeOfDay() == TimeModule.TimeOfDay.MORNING) {
         this.lightAlpha = 220.0F;
      } else if (this.time.getTimeOfDay() == TimeModule.TimeOfDay.MIDDAY) {
         this.lightAlpha = 255.0F;
      } else if (this.time.getTimeOfDay() == TimeModule.TimeOfDay.EVENING) {
         this.lightAlpha = 200.0F;
      } else if (this.time.getTimeOfDay() == TimeModule.TimeOfDay.DUSK) {
         this.lightAlpha = 160.0F;
      }

      this.lightLevel = new Color(0.0F, 0.0F, 0.0F, this.lightAlpha);
      this.lightColorCurrent = new Color(0, 0, 0, 255);
      this.lightColorSet = new Color(0, 0, 0, 255);
      this.setLightLevel();
      this.forceLightAndColorLevels();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
      this.lightUpdateStep = LightingModule.LightUpdateStep.FINISH;
      this.lightAlpha = 0.0F;
      this.lightAdjust = 0.0F;
      this.mapLightsAll.clear();
      this.lightColorCurrent = new Color(0, 0, 0, 255);
      this.lightColorSet = new Color(0, 0, 0, 255);
      this.mouseLight = null;
      this.shortTermLights.clear();
      ArrayFillUtil.fill(this.lightValueBase, 0);
      ArrayFillUtil.fill(this.lightValueExtrasNew, 0);
      ArrayFillUtil.fill(this.lightValueMapNewCurrent, 0);
      ArrayFillUtil.fill(this.lightValueMapNew, 0);
      this.mapWidth = 0;
      this.mapHeight = 0;
      this.mapLayers = 0;

      for (int x = 0; x < this.lightNodes.length; x++) {
         for (int y = 0; y < this.lightNodes[x].length; y++) {
            this.lightNodes[x][y].flagResettable();
            this.lightNodesThreaded[x][y].flagResettable();
         }
      }
   }

   @Override
   protected void newMainMenu() throws SlickException {
   }

   @Override
   protected void newPlay() throws SlickException {
   }

   @Override
   protected void loadPlay() throws SlickException {
   }

   @Override
   protected void newMapEditor() throws SlickException {
   }

   @Override
   protected void loadMapEditor() throws SlickException {
   }

   @Override
   protected void newWorldMap() throws SlickException {
   }

   @Override
   protected void loadWorldMap() throws SlickException {
   }

   @Override
   protected void loadRegionalSaveData() throws SlickException {
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
   }

   public void update() throws SlickException {
      this.setMouseLight();
      this.setLightLevel();
      this.adjustLightLevel();
      this.adjustLightColor();
      this.fillLightValueExtrasNew();
      ArrayList<Light> shortTermLightsRemove = new ArrayList<>();

      for (Light l : this.shortTermLights) {
         l.update();
         if (l.getLightDuration() <= 0) {
            shortTermLightsRemove.add(l);
         }
      }

      this.shortTermLights.removeAll(shortTermLightsRemove);
      if (this.lightUpdateStep == LightingModule.LightUpdateStep.FINISH) {
         this.updateLightMapFinalize();
      }

      for (int x = 0; x < this.mapWidth; x++) {
         for (int y = 0; y < this.mapHeight; y++) {
            if (this.lightValueBase[x][y] < this.lightValueExtrasNew[x][y] + this.lightValueMapNewCurrent[x][y]) {
               this.lightValueBase[x][y]++;
            } else if (this.lightValueBase[x][y] > this.lightValueExtrasNew[x][y] + this.lightValueMapNewCurrent[x][y]) {
               this.lightValueBase[x][y]--;
            }
         }
      }
   }

   public void render(Graphics g) throws SlickException {
      int displayWidth = ScaleControl.getWorldWidth();
      int displayHeight = ScaleControl.getWorldHeight();
      int startX = this.map.getMapRenderTileTopLeftX() - 2;
      if (startX < 0) {
         startX = 0;
      }

      int startY = this.map.getMapRenderTileTopLeftY() - 2;
      if (startY < 0) {
         startY = 0;
      }

      int endX = this.map.getMapRenderTileBottomRightX() + 2;
      if (endX > 255) {
         endX = 255;
      }

      int endY = this.map.getMapRenderTileBottomRightY() + 2;
      if (endY > 255) {
         endY = 255;
      }

      float mapX = this.map.getMapX();
      float mapY = this.map.getMapY();
      GL11.glBlendFunc(774, 770);
      g.setColor(this.lightColorCurrent);
      g.fillRect(0.0F, 0.0F, displayWidth + 16, displayHeight + 16);
      GL11.glBlendFunc(1, 769);
      g.setColor(this.lightColorCurrent.darker(0.7F));
      g.fillRect(0.0F, 0.0F, displayWidth + 16, displayHeight + 16);
      g.clearAlphaMapFast();
      GL11.glBlendFunc(770, 1);
      g.setColor(this.lightLevel);
      g.fillRect(0.0F, 0.0F, displayWidth + 16, displayHeight + 16);
      if (this.lightAlpha < 254.0F) {
         this.light.startUse();

         for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
               if (this.lightValueBase[x][y] > 0) {
                  float x2 = x * 16 + mapX;
                  float y2 = y * 16 + mapY;
                  int finalColor = this.lightValueBase[x][y];
                  if (this.lightValueBase[x][y] >= 75) {
                     finalColor = 74;
                  }

                  this.light
                     .drawEmbedded(
                        x2 - 16.0F, y2 - 16.0F, x2 + 32.0F, y2 + 32.0F, 0.0F, 0.0F, this.light.getWidth(), this.light.getHeight(), this.colorRange[finalColor]
                     );
               }
            }
         }

         this.light.endUse();
      }

      GL11.glBlendFunc(1, 772);
      g.setColor(Color.black);
      g.fillRect(0.0F, 0.0F, displayWidth + 16, displayHeight + 16);
      g.setDrawMode(Graphics.MODE_NORMAL);
   }

   public void renderMapWriter(Graphics g) throws SlickException {
      int displayWidth = 4096;
      int displayHeight = 4096;
      GL11.glBlendFunc(774, 770);
      g.setColor(this.lightColorCurrent);
      g.fillRect(0.0F, 0.0F, displayWidth + 16, displayHeight + 16);
      GL11.glBlendFunc(1, 769);
      g.setColor(this.lightColorCurrent.darker(0.7F));
      g.fillRect(0.0F, 0.0F, displayWidth + 16, displayHeight + 16);
      g.clearAlphaMap();
      GL11.glBlendFunc(770, 1);
      g.setColor(this.lightLevel);
      g.fillRect(0.0F, 0.0F, displayWidth + 16, displayHeight + 16);
      this.light.startUse();

      for (int x = 0; x < this.mapWidth; x++) {
         for (int y = 0; y < this.mapHeight; y++) {
            if (this.lightValueBase[x][y] > 0) {
               if (this.lightValueBase[x][y] >= 75) {
                  this.light
                     .drawEmbedded(
                        x * 16 - 16, y * 16 - 16, x * 16 + 32, y * 16 + 32, 0.0F, 0.0F, this.light.getWidth(), this.light.getHeight(), this.colorRange[74]
                     );
               } else {
                  this.light
                     .drawEmbedded(
                        x * 16 - 16,
                        y * 16 - 16,
                        x * 16 + 32,
                        y * 16 + 32,
                        0.0F,
                        0.0F,
                        this.light.getWidth(),
                        this.light.getHeight(),
                        this.colorRange[this.lightValueBase[x][y]]
                     );
               }
            }
         }
      }

      this.light.endUse();
      GL11.glBlendFunc(1, 772);
      g.setColor(Color.black);
      g.fillRect(0.0F, 0.0F, displayWidth + 16, displayHeight + 16);
      g.setDrawMode(Graphics.MODE_NORMAL);
      if (this.debug) {
         for (int x = 0; x < this.mapWidth; x++) {
            for (int yx = 0; yx < this.mapHeight; yx++) {
               if (x * 8 > -10 && x * 8 < displayWidth + 10 && yx * 8 > -10 && yx * 8 < displayHeight + 10 && this.lightValueBase[x][yx] != 0) {
                  this.font.drawString(x * 8 + 1, yx * 8 + 1, "" + this.lightValueBase[x][yx], Text.FontType.BODY, 1);
               }
            }
         }
      }
   }

   private void fillLightValueExtrasNew() {
      for (int i = 0; i < this.lightValueExtrasNew.length; i++) {
         Arrays.fill(this.lightValueExtrasNew[i], 0);
      }

      byte[][] mouseLightMap = this.mouseLight.getLightMap();

      for (int x1 = 0; x1 < mouseLightMap.length; x1++) {
         for (int y1 = 0; y1 < mouseLightMap[x1].length; y1++) {
            int lightAddX = x1 + (this.mouseLight.getLightX() - mouseLightMap.length / 2);
            int lightAddY = y1 + (this.mouseLight.getLightY() - mouseLightMap[x1].length / 2);
            if (!Utilities.isOutOfBoundsOfUsableMap(lightAddX, lightAddY)) {
               this.lightValueExtrasNew[lightAddX][lightAddY] = this.lightValueExtrasNew[lightAddX][lightAddY] + mouseLightMap[x1][y1];
            }
         }
      }

      for (MobBase m : this.mob.getMobArray()) {
         Light l = m.getLight();
         if (l != null) {
            byte[][] lightMap = l.getLightMap();

            for (int x1 = 0; x1 < lightMap.length; x1++) {
               for (int y1x = 0; y1x < lightMap[x1].length; y1x++) {
                  int lightAddX = x1 + (l.getLightX() - lightMap.length / 2);
                  int lightAddY = y1x + (l.getLightY() - lightMap[x1].length / 2);
                  if (!Utilities.isOutOfBoundsOfUsableMap(lightAddX, lightAddY)) {
                     this.lightValueExtrasNew[lightAddX][lightAddY] = this.lightValueExtrasNew[lightAddX][lightAddY] + lightMap[x1][y1x];
                  }
               }
            }
         }
      }

      for (MissileBase mx : this.missile.getMissileArray()) {
         Light l = mx.getLight();
         if (l != null) {
            byte[][] lightMap = l.getLightMap();

            for (int x1 = 0; x1 < lightMap.length; x1++) {
               for (int y1xx = 0; y1xx < lightMap[x1].length; y1xx++) {
                  int lightAddX = x1 + (l.getLightX() - lightMap.length / 2);
                  int lightAddY = y1xx + (l.getLightY() - lightMap[x1].length / 2);
                  if (!Utilities.isOutOfBoundsOfUsableMap(lightAddX, lightAddY)) {
                     this.lightValueExtrasNew[lightAddX][lightAddY] = this.lightValueExtrasNew[lightAddX][lightAddY] + lightMap[x1][y1xx];
                  }
               }
            }
         }
      }

      if (this.lootBox.isInitialized()) {
         Light[][] lootBoxLights = this.lootBox.getLootBoxLights();

         for (OrderedPair o : this.lootBox.getLootBoxLocationsArray()) {
            Light l = lootBoxLights[o.getX()][o.getY()];
            byte[][] lightMap = l.getLightMap();

            for (int x1 = 0; x1 < lightMap.length; x1++) {
               for (int y1xxx = 0; y1xxx < lightMap[x1].length; y1xxx++) {
                  int lightAddX = x1 + (l.getLightX() - lightMap.length / 2);
                  int lightAddY = y1xxx + (l.getLightY() - lightMap[x1].length / 2);
                  if (!Utilities.isOutOfBoundsOfUsableMap(lightAddX, lightAddY)) {
                     this.lightValueExtrasNew[lightAddX][lightAddY] = this.lightValueExtrasNew[lightAddX][lightAddY] + lightMap[x1][y1xxx];
                  }
               }
            }
         }
      }

      for (Light l : this.shortTermLights) {
         byte[][] lightMap = l.getLightMap();

         for (int x1 = 0; x1 < lightMap.length; x1++) {
            for (int y1xxxx = 0; y1xxxx < lightMap[x1].length; y1xxxx++) {
               int lightAddX = x1 + (l.getLightX() - lightMap.length / 2);
               int lightAddY = y1xxxx + (l.getLightY() - lightMap[x1].length / 2);
               if (!Utilities.isOutOfBoundsOfUsableMap(lightAddX, lightAddY)) {
                  this.lightValueExtrasNew[lightAddX][lightAddY] = this.lightValueExtrasNew[lightAddX][lightAddY] + lightMap[x1][y1xxxx];
               }
            }
         }
      }
   }

   private void setLightLevel() {
      if (this.time.getTimeOfDay() == TimeModule.TimeOfDay.NIGHT) {
         if (this.time.isFullMoon()) {
            this.lightColorSet = new Color(0.0F, 0.25F, 0.75F, 1.0F);
            this.lightAdjust = 180.0F;
         } else {
            this.lightColorSet = new Color(0.0F, 0.0F, 0.5F, 1.0F);
            this.lightAdjust = 80.0F;
         }
      } else if (this.time.getTimeOfDay() == TimeModule.TimeOfDay.DAWN) {
         this.lightColorSet = new Color(0.5F, 0.25F, 0.0F, 1.0F);
         this.lightAdjust = 160.0F;
      } else if (this.time.getTimeOfDay() == TimeModule.TimeOfDay.MORNING) {
         this.lightColorSet = new Color(0.15F, 0.05F, 0.0F, 1.0F);
         this.lightAdjust = 220.0F;
      } else if (this.time.getTimeOfDay() == TimeModule.TimeOfDay.MIDDAY) {
         this.lightColorSet = new Color(0.0F, 0.0F, 0.0F, 1.0F);
         this.lightAdjust = 255.0F;
      } else if (this.time.getTimeOfDay() == TimeModule.TimeOfDay.EVENING) {
         this.lightColorSet = new Color(0.25F, 0.15F, 0.0F, 1.0F);
         this.lightAdjust = 200.0F;
      } else if (this.time.getTimeOfDay() == TimeModule.TimeOfDay.DUSK) {
         this.lightColorSet = new Color(0.5F, 0.2F, 0.0F, 1.0F);
         this.lightAdjust = 160.0F;
      }

      if (this.weather.getCurrentTemp() <= this.weather.getColdPoint()) {
         this.lightColorSet.b += 0.2F;
      }

      if (this.weather.getCurrentTemp() <= this.weather.getFreezePoint()) {
         this.lightColorSet.b += 0.2F;
      }

      if (this.weather.getCurrentTemp() >= this.weather.getHotPoint()) {
         this.lightColorSet.r += 0.1F;
      }

      if (this.weather.getCurrentTemp() >= this.weather.getFryPoint()) {
         this.lightColorSet.r += 0.1F;
      }

      if (this.weather.getDisasters().size() > 0) {
         for (DisasterBase d : this.weather.getDisasters()) {
            Color c = d.getEnviromentColor();
            if (c != null) {
               this.lightColorSet.r = this.lightColorSet.r + c.r;
               this.lightColorSet.g = this.lightColorSet.g + c.g;
               this.lightColorSet.b = this.lightColorSet.b + c.b;
            }

            this.lightAdjust = this.lightAdjust + d.getEnviromentLightAdjust();
         }
      }

      if (this.corruption.isInitialized()) {
         int corruptionAmountOnScreen = this.corruption.calculateCorruptionPercentOnScreen();
         this.lightAdjust = (float)(this.lightAdjust - corruptionAmountOnScreen * 1.5);
         this.lightColorSet.r = (float)(this.lightColorSet.r + corruptionAmountOnScreen / 150.0);
      }
   }

   private void forceLightAndColorLevels() {
      this.lightLevel = new Color(0, 0, 0, (int)this.lightAdjust);
      this.lightColorCurrent.r = this.lightColorSet.r;
      this.lightColorCurrent.g = this.lightColorSet.g;
      this.lightColorCurrent.b = this.lightColorSet.b;
      this.lightColorCurrent.a = this.lightColorSet.a;
   }

   private void adjustLightLevel() {
      if ((int)this.lightAlpha != (int)this.lightAdjust) {
         if (this.lightAlpha > this.lightAdjust) {
            this.lightAlpha -= 0.2F;
         } else {
            this.lightAlpha += 0.2F;
         }
      }

      this.lightLevel = new Color(0, 0, 0, (int)this.lightAlpha);
   }

   private void adjustLightColor() {
      if ((int)(this.lightColorCurrent.r * 1000.0F) < (int)(this.lightColorSet.r * 1000.0F)) {
         this.lightColorCurrent.r += 0.002F;
      } else if ((int)(this.lightColorCurrent.r * 1000.0F) > (int)(this.lightColorSet.r * 1000.0F)) {
         this.lightColorCurrent.r -= 0.002F;
      }

      if ((int)(this.lightColorCurrent.g * 1000.0F) < (int)(this.lightColorSet.g * 1000.0F)) {
         this.lightColorCurrent.g += 0.002F;
      } else if ((int)(this.lightColorCurrent.g * 1000.0F) > (int)(this.lightColorSet.g * 1000.0F)) {
         this.lightColorCurrent.g -= 0.002F;
      }

      if ((int)(this.lightColorCurrent.b * 1000.0F) < (int)(this.lightColorSet.b * 1000.0F)) {
         this.lightColorCurrent.b += 0.002F;
      } else if ((int)(this.lightColorCurrent.b * 1000.0F) > (int)(this.lightColorSet.b * 1000.0F)) {
         this.lightColorCurrent.b -= 0.002F;
      }

      if ((int)(this.lightColorCurrent.a * 1000.0F) < (int)(this.lightColorSet.a * 1000.0F)) {
         this.lightColorCurrent.a += 0.002F;
      } else if ((int)(this.lightColorCurrent.a * 1000.0F) > (int)(this.lightColorSet.a * 1000.0F)) {
         this.lightColorCurrent.a -= 0.002F;
      }
   }

   private void buildInitialMap() throws SlickException {
      this.mapLightsAll.clear();

      for (int x = 0; x < this.mapWidth; x++) {
         for (int y = 0; y < this.mapHeight; y++) {
            for (int l = 0; l < this.mapLayers; l++) {
               int tileID = this.map.getTileId(x, y, l);
               if (tileID != 0 && this.map.getMapTileLoader().getTileLight(tileID) != 0) {
                  this.mapLightsAll.add(new Light(x, y, this.map.getMapTileLoader().getTileLight(tileID), true));
                  break;
               }
            }
         }
      }

      for (Light light : this.mapLightsAll) {
         byte[][] lightMap = light.getLightMap();

         for (int x1 = 0; x1 < lightMap.length; x1++) {
            for (int y1 = 0; y1 < lightMap[x1].length; y1++) {
               int lightAddX = x1 + (light.getLightX() - lightMap.length / 2);
               int lightAddY = y1 + (light.getLightY() - lightMap[x1].length / 2);
               if (!Utilities.isOutOfBoundsOfUsableMap(lightAddX, lightAddY)) {
                  this.lightValueMapNewCurrent[lightAddX][lightAddY] = this.lightValueMapNewCurrent[lightAddX][lightAddY] + lightMap[x1][y1];
               }
            }
         }
      }
   }

   public LightingModule.LightUpdateStep getLightUpdateStep() {
      return this.lightUpdateStep;
   }

   public void updateEntireLightMap() throws SlickException {
      switch (this.lightUpdateStep) {
         case INIT:
            this.mapLightsAll.clear();
            int[][][] mapData = this.map.getMap();
            if (mapData != null) {
               for (int x = 0; x < this.mapWidth; x++) {
                  for (int y = 0; y < this.mapHeight; y++) {
                     for (int l = 0; l < this.mapLayers; l++) {
                        int tileID = mapData[x][y][l];
                        if (tileID != 0) {
                           int lightLevel = this.map.getMapTileLoader().getTileLight(tileID);
                           if (lightLevel != 0) {
                              this.mapLightsAll.add(new Light(x, y, this.map.getMapTileLoader().getTileLight(tileID), true));
                              break;
                           }
                        }
                     }
                  }
               }

               ArrayFillUtil.fill(this.lightValueMapNew, 0);
               this.lightUpdateStep = LightingModule.LightUpdateStep.RUN;
            }
            break;
         case RUN:
            int count = 0;

            while (this.mapLightsAll.size() != 0 && count < 2048) {
               Light light = this.mapLightsAll.remove(0);
               byte[][] lightMap = light.getLightMap();

               for (int x1 = 0; x1 < lightMap.length; x1++) {
                  for (int y1 = 0; y1 < lightMap[x1].length; y1++) {
                     int lightAddX = x1 + (light.getLightX() - lightMap.length / 2);
                     int lightAddY = y1 + (light.getLightY() - lightMap[x1].length / 2);
                     if (!Utilities.isOutOfBoundsOfUsableMap(lightAddX, lightAddY)) {
                        this.lightValueMapNew[lightAddX][lightAddY] = this.lightValueMapNew[lightAddX][lightAddY] + lightMap[x1][y1];
                        count++;
                     }
                  }
               }
            }

            if (this.mapLightsAll.size() == 0) {
               this.lightUpdateStep = LightingModule.LightUpdateStep.FINISH;
            }
         case FINISH:
      }
   }

   /**
    * Minimum time between full light-map rebuilds. 0 (desktop default) restarts a rebuild as soon as the previous one
    * finishes, which keeps one CPU core busy forever; mobile builds raise it (the result fades in gradually anyway).
    */
   public static long minFullRebuildIntervalMs = 0L;
   private long lastFullRebuildMs;

   private void updateLightMapFinalize() {
      long now = System.currentTimeMillis();
      if (now - this.lastFullRebuildMs < minFullRebuildIntervalMs) {
         return;
      }

      this.lastFullRebuildMs = now;
      this.lightUpdateStep = LightingModule.LightUpdateStep.INIT;

      for (int x = 0; x < this.lightValueMapNew.length; x++) {
         for (int y = 0; y < this.lightValueMapNew[x].length; y++) {
            this.lightValueMapNewCurrent[x][y] = this.lightValueMapNew[x][y];
         }
      }
   }

   public void addShortTermLight(int x, int y, byte r, byte i, int d) {
      this.shortTermLights.add(new Light(x, y, r, i, d, false));
   }

   public float getLightLevel() {
      return this.lightAlpha;
   }

   public LightNode[][] getLightNodes() {
      return this.lightNodes;
   }

   public LightNode[][] getLightNodesThreaded() {
      return this.lightNodesThreaded;
   }

   public ArrayList<LightNode> getResettableLightNodes() {
      return this.resettableLightNodes;
   }

   public ArrayList<LightNode> getResettableLightNodesThreaded() {
      return this.resettableLightNodesThreaded;
   }

   public void resetOldLightNodes() {
      for (LightNode l : this.resettableLightNodes) {
         l.flagResettable();
      }

      this.resettableLightNodes.clear();
   }

   public void resetOldLightNodesThreaded() {
      for (LightNode l : this.resettableLightNodesThreaded) {
         l.flagResettable();
      }

      this.resettableLightNodesThreaded.clear();
   }

   public void setMouseLight() {
      if (this.mouseLight == null) {
         this.mouseLight = new Light(this.currentState.getMouseTileX(), this.currentState.getMouseTileY(), (byte)14, (byte)10, false);
      }

      if (this.currentState.getGUI().isHideGUI()) {
         this.mouseLight.setLightX(0);
         this.mouseLight.setLightY(0);
      } else {
         this.mouseLight.setLightX(this.currentState.getMouseTileX());
         this.mouseLight.setLightY(this.currentState.getMouseTileY());
      }
   }

   public int[][] getLightValueMap() {
      return this.lightValueBase;
   }

   public static enum LightUpdateStep {
      INIT,
      RUN,
      FINISH;
   }
}
