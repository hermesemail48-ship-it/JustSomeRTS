package rtr.particles;

import rtr.utilities.IntKeyMap;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.ForkJoinPool;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import r2d.image.SpriteSheet;
import rtr.ImageLoader;
import rtr.ModuleBase;
import rtr.SettingsParser;
import rtr.gui.states.WorldMapGUIController;
import rtr.particles.groups.ParticleGroupBase;
import rtr.particles.groups.ParticleGroupBloodPool;
import rtr.particles.groups.ParticleGroupBloodSplatters;
import rtr.particles.groups.ParticleGroupDust;
import rtr.particles.groups.ParticleGroupElectricLines;
import rtr.particles.groups.ParticleGroupFall;
import rtr.particles.groups.ParticleGroupFire;
import rtr.particles.groups.ParticleGroupFloatingDust;
import rtr.particles.groups.ParticleGroupFloatingMagicRings;
import rtr.particles.groups.ParticleGroupFloatingMagicSquares;
import rtr.particles.groups.ParticleGroupMagicFall;
import rtr.particles.groups.ParticleGroupMagicFire;
import rtr.particles.groups.ParticleGroupMagicSmoke;
import rtr.particles.groups.ParticleGroupSmokeBellow;
import rtr.particles.groups.ParticleGroupSparkles;
import rtr.particles.templates.ParticleBase;
import rtr.particles.templates.ParticleBlood;
import rtr.particles.templates.ParticleBloodPool;
import rtr.particles.templates.ParticleCrumb;
import rtr.particles.templates.ParticleDust;
import rtr.particles.templates.ParticleFall;
import rtr.particles.templates.ParticleFire;
import rtr.particles.templates.ParticleFlash;
import rtr.particles.templates.ParticleFlashFast;
import rtr.particles.templates.ParticleFloatingDust;
import rtr.particles.templates.ParticleHail;
import rtr.particles.templates.ParticleHailLanded;
import rtr.particles.templates.ParticleMagicFloat;
import rtr.particles.templates.ParticleMagicSmoke;
import rtr.particles.templates.ParticleRain;
import rtr.particles.templates.ParticleSmoke;
import rtr.particles.templates.ParticleSnow;
import rtr.particles.templates.ParticleSparkle;
import rtr.particles.templates.ParticleSpeck;
import rtr.particles.templates.ParticleWater;
import rtr.particles.templates.ParticleWaterPool;
import rtr.particles.templates.ParticleWaterPoolFast;
import rtr.particles.templates.ParticleWindDust;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.system.ScaleControl;
import rtr.system.gamemodetemplates.GameModeTemplateBase;
import rtr.utilities.Utilities;

public class ParticleModule extends ModuleBase {
   private ArrayList<ParticleEmitter>[][] particleEmittersMap;
   private byte[][] flaggedEmitterUpdateMap;
   private ArrayList<ParticleEmitter> particleEmittersWorldMap = new ArrayList<>();
   private IntKeyMap<byte[][]> emitterMasks;
   private int tileGlobalX;
   private int tileGlobalY;
   private ParticleAnimationLoader particleAnimationLoader;
   private ArrayList<Particle> particleArrayTop = new ArrayList<>();
   private ArrayList<Particle> particleArrayBottom = new ArrayList<>();
   private ArrayList<Particle> particleArrayGUI = new ArrayList<>();
   private ArrayList<Particle> particleArrayRenderTop;
   private ArrayList<Particle> particleArrayRenderBottom;
   private ArrayList<Particle> particleArrayRenderGUI;
   private ArrayList<Particle> particleArrayTopAdd = new ArrayList<>();
   private ArrayList<Particle> particleArrayBottomAdd = new ArrayList<>();
   private ArrayList<Particle> particleArrayGUIAdd = new ArrayList<>();
   private ArrayList<Particle> particleArrayTopInThreadAdd = new ArrayList<>();
   private ArrayList<Particle> particleArrayBottomInThreadAdd = new ArrayList<>();
   private BitSet particleLiquidMap;
   private Color[][] particleColors;
   private boolean particleThreadRunning = false;
   private boolean particlesTopRendering = false;
   private boolean particlesBottomRendering = false;
   private boolean particlesGUIRendering = false;
   ForkJoinPool activePoolTop = null;
   ForkJoinPool activePoolBot = null;
   ForkJoinPool activePoolGUI = null;

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      this.loadParticleData();
      if (this.particleColors == null) {
         SpriteSheet particleColorSheet = new SpriteSheet(ImageLoader.getImage("res/particles.png"), 1, 1);
         this.particleColors = new Color[particleColorSheet.getHorizontalCount()][particleColorSheet.getVerticalCount()];

         for (int x = 0; x < particleColorSheet.getHorizontalCount(); x++) {
            for (int y = 0; y < particleColorSheet.getVerticalCount(); y++) {
               this.particleColors[x][y] = particleColorSheet.getSprite(x, y).getColor(0, 0);
            }
         }

         particleColorSheet.flushPixelData();
      }

      this.particleAnimationLoader = new ParticleAnimationLoader();
      this.particleEmittersMap = new ArrayList[256][256];
      this.flaggedEmitterUpdateMap = new byte[256][256];
      this.particleLiquidMap = new BitSet(16777216);
      if (this.emitterMasks == null) {
         this.emitterMasks = this.map.getMapTileLoader().getParticleTiles();
      }

      this.initLoad();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();

      while (this.particleThreadRunning) {
         try {
            Thread.sleep(2L);
         } catch (InterruptedException var2) {
            var2.printStackTrace();
         }
      }

      this.tileGlobalX = 0;
      this.tileGlobalY = 0;
      this.particleArrayTop.clear();
      this.particleArrayBottom.clear();
      this.particleArrayTopAdd.clear();
      this.particleArrayBottomAdd.clear();
      this.particleArrayGUI.clear();
      this.particleArrayGUIAdd.clear();
      this.particleEmittersWorldMap.clear();
   }

   @Override
   protected void newMainMenu() throws SlickException {
      this.buildFullEmitterMap();
   }

   @Override
   protected void newPlay() throws SlickException {
      this.buildFullEmitterMap();
   }

   @Override
   protected void loadPlay() throws SlickException {
      this.buildFullEmitterMap();
   }

   @Override
   protected void newMapEditor() throws SlickException {
      this.buildFullEmitterMap();
   }

   @Override
   protected void loadMapEditor() throws SlickException {
      this.buildFullEmitterMap();
   }

   @Override
   protected void newWorldMap() throws SlickException {
      this.buildEmitterWorldMap();
   }

   @Override
   protected void loadWorldMap() throws SlickException {
      this.buildEmitterWorldMap();
   }

   @Override
   protected void loadRegionalSaveData() throws SlickException {
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
   }

   private void loadParticleData() {
      ParticleModule.ParticleType[] var4;
      for (ParticleModule.ParticleType r : ParticleModule.ParticleType.values()) {
         r.getTemplate().load();
      }

      for (ParticleModule.ParticleGroup g : ParticleModule.ParticleGroup.values()) {
         g.getTemplate().load();
      }
   }

   public void updateDedicatedThread() throws SlickException {
      while (!this.particleThreadRunning) {
         try {
            Thread.sleep(2L);
         } catch (InterruptedException var9) {
            var9.printStackTrace();
         }
      }

      if (!this.initialized) {
         this.particleThreadRunning = false;
      } else {
         ArrayList<Particle> particleArrayRenderTopNew = new ArrayList<>();
         ArrayList<Particle> particleArrayRenderBottomNew = new ArrayList<>();
         ArrayList<Particle> particleArrayRenderGUINew = new ArrayList<>();
         if (this.gameMode == GameModeTemplateBase.GameMode.WORLD_MAP) {
            WorldMapGUIController gui = (WorldMapGUIController)Game.getWorldMapState().getGUI();
            if (gui != null) {
               this.updateWorldMapEmitters((int)gui.getWorldMap().getMapX(), (int)gui.getWorldMap().getMapY());
            }
         } else {
            this.updateEmitters();
         }

         for (int x = this.particleArrayTop.size() - 1; x >= 0; x--) {
            Particle p = this.particleArrayTop.get(x);
            p.update();
            if (p.getDuration() <= 0) {
               this.particleArrayTop.remove(x);
            } else if (!p.isTop()) {
               this.particleArrayTop.remove(x);
               this.particleArrayBottomInThreadAdd.add(p);
            }

            if (p.getDuration() > 0 && (p.canRender() || this.gameMode == GameModeTemplateBase.GameMode.WORLD_MAP)) {
               particleArrayRenderTopNew.add(p);
            }
         }

         Collections.sort(particleArrayRenderTopNew, new Comparator<Particle>() {
            @Override
            public int compare(Particle a, Particle b) {
               return Integer.compare((int)a.getParticleY(), (int)b.getParticleY());
            }
         });

         for (int x = this.particleArrayBottom.size() - 1; x >= 0; x--) {
            Particle px = this.particleArrayBottom.get(x);
            px.update();
            if (px.getDuration() <= 0) {
               this.particleArrayBottom.remove(x);
            } else if (px.isTop()) {
               this.particleArrayBottom.remove(x);
               this.particleArrayTopInThreadAdd.add(px);
            }

            if (px.getDuration() > 0 && (px.canRender() || this.gameMode == GameModeTemplateBase.GameMode.WORLD_MAP)) {
               particleArrayRenderBottomNew.add(px);
            }
         }

         Collections.sort(particleArrayRenderBottomNew, new Comparator<Particle>() {
            @Override
            public int compare(Particle a, Particle b) {
               return Integer.compare((int)a.getParticleY(), (int)b.getParticleY());
            }
         });

         for (int x = this.particleArrayGUI.size() - 1; x >= 0; x--) {
            Particle pxx = this.particleArrayGUI.get(x);
            pxx.update();
            if (pxx.getDuration() <= 0) {
               this.particleArrayGUI.remove(x);
            } else {
               particleArrayRenderGUINew.add(pxx);
            }
         }

         Collections.sort(particleArrayRenderGUINew, new Comparator<Particle>() {
            @Override
            public int compare(Particle a, Particle b) {
               return Integer.compare((int)a.getParticleY(), (int)b.getParticleY());
            }
         });

         while (this.particlesTopRendering) {
            try {
               Thread.sleep(2L);
            } catch (InterruptedException var8) {
               var8.printStackTrace();
            }
         }

         this.particleArrayRenderTop = particleArrayRenderTopNew;

         while (this.particlesBottomRendering) {
            try {
               Thread.sleep(2L);
            } catch (InterruptedException var7) {
               var7.printStackTrace();
            }
         }

         this.particleArrayRenderBottom = particleArrayRenderBottomNew;

         while (this.particlesGUIRendering) {
            try {
               Thread.sleep(2L);
            } catch (InterruptedException var6) {
               var6.printStackTrace();
            }
         }

         this.particleArrayRenderGUI = particleArrayRenderGUINew;
         this.particleThreadRunning = false;
      }
   }

   private void updateEmitters() throws SlickException {
      int startX = this.map.getMapRenderTileTopLeftX() - 2;
      int startY = this.map.getMapRenderTileTopLeftY() - 2;
      int endX = this.map.getMapRenderTileBottomRightX() + 2;
      int endY = this.map.getMapRenderTileBottomRightY() + 2;

      for (int x = startX; x < endX; x++) {
         for (int y = startY; y < endY; y++) {
            if (x >= 0 && x <= 255 && y >= 0 && y <= 255) {
               if (this.flaggedEmitterUpdateMap[x][y] == 1) {
                  this.updateEmitterMap(x, y);
                  this.flaggedEmitterUpdateMap[x][y] = 0;
               }

               ArrayList<ParticleEmitter> emitOut = this.particleEmittersMap[x][y];
               if (emitOut != null) {
                  for (ParticleEmitter e : emitOut) {
                     e.emit();
                  }
               }
            }
         }
      }
   }

   public void updateEmitterMap(int x, int y) throws SlickException {
      if (this.particleEmittersMap[x][y] != null) {
         this.particleEmittersMap[x][y].clear();
      }

      int[] tileID = new int[12];

      for (int l = 0; l < tileID.length; l++) {
         tileID[l] = this.map.getTileId(x, y, l);
         if (tileID[l] != 0) {
            this.updateEmitterTile(x, y, l, tileID[l]);
         }
      }
   }

   public void flagEmitterMapForUpdate(int x, int y) throws SlickException {
      this.flaggedEmitterUpdateMap[x][y] = 1;
   }

   public void update() throws SlickException {
      while (this.particleThreadRunning) {
         try {
            Thread.sleep(2L);
         } catch (InterruptedException var3) {
            var3.printStackTrace();
         }
      }

      if (SettingsParser.getDebug()) {
         this.loadParticleData();
      }

      for (Particle p : this.particleArrayTopInThreadAdd) {
         this.particleArrayTop.add(p);
      }

      for (Particle p : this.particleArrayTopAdd) {
         this.particleArrayTop.add(p);
      }

      for (Particle p : this.particleArrayBottomInThreadAdd) {
         this.particleArrayBottom.add(p);
      }

      for (Particle p : this.particleArrayBottomAdd) {
         this.particleArrayBottom.add(p);
      }

      for (Particle p : this.particleArrayGUIAdd) {
         this.particleArrayGUI.add(p);
      }

      this.particleArrayTopAdd.clear();
      this.particleArrayTopInThreadAdd.clear();
      this.particleArrayBottomAdd.clear();
      this.particleArrayBottomInThreadAdd.clear();
      this.particleArrayGUIAdd.clear();
      this.particleThreadRunning = true;
   }

   private void updateWorldMapEmitters(int mapX, int mapY) {
      if (SettingsParser.getParticleAmount() != SettingsParser.ParticleAmount.REDUCED || Utilities.randomInt(2) != 0) {
         if (SettingsParser.getParticleAmount() != SettingsParser.ParticleAmount.MINIMAL) {
            for (ParticleEmitter e : this.particleEmittersWorldMap) {
               int buffer = 10;
               if (e.getX() + mapX > -buffer
                  && e.getX() + mapX < ScaleControl.getWorldWidth() + buffer
                  && e.getY() + mapY > -buffer
                  && e.getY() + mapY < ScaleControl.getWorldHeight() + buffer) {
                  e.emit();
               }
            }
         }
      }
   }

   public void renderBottom() {
      if (this.particleArrayRenderBottom != null) {
         this.particlesBottomRendering = true;
         this.particleAnimationLoader.startUse();
         synchronized (this.particleArrayRenderBottom) {
            for (Particle p : this.particleArrayRenderBottom) {
               p.render();
            }
         }

         this.particleAnimationLoader.endUse();
         this.particlesBottomRendering = false;
      }
   }

   public void renderTop() {
      if (this.particleArrayRenderTop != null) {
         this.particlesTopRendering = true;
         this.particleAnimationLoader.startUse();
         synchronized (this.particleArrayRenderTop) {
            for (Particle p : this.particleArrayRenderTop) {
               p.render();
            }
         }

         this.particleAnimationLoader.endUse();
         this.particlesTopRendering = false;
      }
   }

   public void renderGUI() throws SlickException {
      if (this.particleArrayRenderGUI != null) {
         this.particlesGUIRendering = true;
         this.particleAnimationLoader.startUse();
         synchronized (this.particleArrayRenderGUI) {
            for (Particle p : this.particleArrayRenderGUI) {
               p.renderGUI();
            }
         }

         this.particleAnimationLoader.endUse();
         this.particlesGUIRendering = false;
      }
   }

   public void renderWorldMap(float mapX, float mapY) throws SlickException {
      this.particlesTopRendering = true;
      this.particlesBottomRendering = true;
      this.particleAnimationLoader.startUse();
      synchronized (this.particleArrayRenderBottom) {
         for (Particle p : this.particleArrayRenderBottom) {
            p.renderWorldMap(mapX, mapY);
         }
      }

      synchronized (this.particleArrayRenderTop) {
         for (Particle p : this.particleArrayRenderTop) {
            p.renderWorldMap(mapX, mapY);
         }
      }

      this.particleAnimationLoader.endUse();
      this.particlesTopRendering = false;
      this.particlesBottomRendering = false;
   }

   private void buildFullEmitterMap() throws SlickException {
      int[] tileID = new int[12];

      for (int x = 0; x < 256; x++) {
         for (int y = 0; y < 256; y++) {
            for (int l = 0; l < tileID.length; l++) {
               tileID[l] = this.map.getTileId(x, y, l);
               if (tileID[l] != 0) {
                  this.updateEmitterTile(x, y, l, tileID[l]);
               }
            }
         }
      }
   }

   private void buildEmitterWorldMap() throws SlickException {
      Image worldMap = ImageLoader.getImage("res/GUI/worldMap/worldMapParticles.png");
      byte[][] worldMapByteArray = this.map.getMapTileLoader().generateWorldMapParticleData();
      ImageLoader.unloadImage("res/GUI/worldMap/worldMapParticles.png");
      byte[][] checkedByte = new byte[worldMap.getWidth()][worldMap.getHeight()];

      for (int x = 0; x < worldMapByteArray.length; x++) {
         for (int y = 0; y < worldMapByteArray[x].length; y++) {
            if (x % 16 == 0 && y % 16 == 0) {
               boolean wholeTile = true;
               byte firstEmitter = worldMapByteArray[x][y];
               int xStop = x + 16;
               int yStop = y + 16;

               label78:
               for (int xStart = x; xStart < xStop; xStart++) {
                  for (int yStart = y; yStart < yStop; yStart++) {
                     if (worldMap.getWidth() - 1 < xStart || worldMap.getHeight() - 1 < yStart || worldMapByteArray[xStart][yStart] != firstEmitter) {
                        wholeTile = false;
                        break label78;
                     }
                  }
               }

               if (wholeTile) {
                  for (int xStart = x; xStart < xStop; xStart++) {
                     for (int yStartx = y; yStartx < yStop; yStartx++) {
                        checkedByte[xStart][yStartx] = 1;
                     }
                  }

                  ParticleEmitter emitter = this.createNewEmitter(worldMapByteArray[x][y], x, y, true);
                  if (emitter != null) {
                     this.particleEmittersWorldMap.add(emitter);
                  }
               }
            }

            if (checkedByte[x][y] == 0) {
               ParticleEmitter emitter = this.createNewEmitter(worldMapByteArray[x][y], x, y, false);
               if (emitter != null) {
                  this.particleEmittersWorldMap.add(emitter);
               }

               checkedByte[x][y] = 1;
            }
         }
      }
   }

   private void updateEmitterTile(int tX, int tY, int tL, int tileID) throws SlickException {
      if (this.map.getMapTileLoader().isTileParticles(tileID)) {
         this.tileGlobalX = tX * 16;
         this.tileGlobalY = tY * 16;
         byte[][] emitterTile = this.emitterMasks.get(tileID);
         boolean isFull = true;
         int start = emitterTile[0][0];

         for (int x = 0; x < emitterTile.length; x++) {
            for (int y = 0; y < emitterTile[x].length; y++) {
               if (emitterTile[x][y] != start) {
                  isFull = false;
               }
            }
         }

         for (int x = 0; x < 16; x++) {
            for (int yx = 0; yx < 16; yx++) {
               int pixelX = this.tileGlobalX + x;
               int pixelY = this.tileGlobalY + yx;
               if (emitterTile[x][yx] != 0) {
                  if (this.particleEmittersMap[tX][tY] == null) {
                     this.particleEmittersMap[tX][tY] = new ArrayList<>();
                  }

                  ParticleEmitter emitter = this.createNewEmitter(emitterTile[x][yx], pixelX, pixelY, isFull);
                  this.particleEmittersMap[tX][tY].add(emitter);
                  if (isFull) {
                     return;
                  }
               }
            }
         }
      }
   }

   private ParticleEmitter createNewEmitter(byte emitterNumber, int pixelX, int pixelY, boolean isFull) {
      switch (emitterNumber) {
         case 1:
            return new ParticleEmitter(pixelX, pixelY, 200, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_RED, isFull);
         case 2:
            return new ParticleEmitter(pixelX, pixelY, 100, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_RED, isFull);
         case 3:
            return new ParticleEmitter(pixelX, pixelY, 30, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_RED, isFull);
         case 4:
            return new ParticleEmitter(pixelX, pixelY, 10, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_RED, isFull);
         case 5:
            return new ParticleEmitter(pixelX, pixelY, 200, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_YELLOW, isFull);
         case 6:
            return new ParticleEmitter(pixelX, pixelY, 100, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_YELLOW, isFull);
         case 7:
            return new ParticleEmitter(pixelX, pixelY, 40, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_YELLOW, isFull);
         case 8:
            return new ParticleEmitter(pixelX, pixelY, 10, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_YELLOW, isFull);
         case 9:
            return new ParticleEmitter(pixelX, pixelY, 200, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_GREEN, isFull);
         case 10:
            return new ParticleEmitter(pixelX, pixelY, 100, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_GREEN, isFull);
         case 11:
            return new ParticleEmitter(pixelX, pixelY, 30, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_GREEN, isFull);
         case 12:
            return new ParticleEmitter(pixelX, pixelY, 10, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_GREEN, isFull);
         case 13:
            return new ParticleEmitter(pixelX, pixelY, 200, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_BLUE, isFull);
         case 14:
            return new ParticleEmitter(pixelX, pixelY, 100, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_BLUE, isFull);
         case 15:
            return new ParticleEmitter(pixelX, pixelY, 30, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_BLUE, isFull);
         case 16:
            return new ParticleEmitter(pixelX, pixelY, 10, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_BLUE, isFull);
         case 17:
            return new ParticleEmitter(
               pixelX, pixelY, 350, ParticleModule.ParticleGroup.SMOKE_BELLOW, ParticleModule.ParticleSet.DARK_GRAY, ParticleModule.ParticleSet.WHITE, isFull
            );
         case 18:
            return new ParticleEmitter(
               pixelX, pixelY, 110, ParticleModule.ParticleGroup.SMOKE_BELLOW, ParticleModule.ParticleSet.DARK_GRAY, ParticleModule.ParticleSet.WHITE, isFull
            );
         case 19:
            return new ParticleEmitter(
               pixelX, pixelY, 20, ParticleModule.ParticleGroup.SMOKE_BELLOW, ParticleModule.ParticleSet.DARK_GRAY, ParticleModule.ParticleSet.WHITE, isFull
            );
         case 20:
            return new ParticleEmitter(
               pixelX, pixelY, 12000, ParticleModule.ParticleGroup.SPARKLES, ParticleModule.ParticleSet.WHITE, ParticleModule.ParticleSet.MAGIC_RED, isFull
            );
         case 21:
            return new ParticleEmitter(
               pixelX, pixelY, 6000, ParticleModule.ParticleGroup.SPARKLES, ParticleModule.ParticleSet.WHITE, ParticleModule.ParticleSet.MAGIC_RED, isFull
            );
         case 22:
            return new ParticleEmitter(
               pixelX, pixelY, 3000, ParticleModule.ParticleGroup.SPARKLES, ParticleModule.ParticleSet.WHITE, ParticleModule.ParticleSet.MAGIC_RED, isFull
            );
         case 23:
            return new ParticleEmitter(
               pixelX, pixelY, 1500, ParticleModule.ParticleGroup.SPARKLES, ParticleModule.ParticleSet.WHITE, ParticleModule.ParticleSet.MAGIC_RED, isFull
            );
         case 24:
            return new ParticleEmitter(
               pixelX, pixelY, 12000, ParticleModule.ParticleGroup.SPARKLES, ParticleModule.ParticleSet.WHITE, ParticleModule.ParticleSet.MAGIC_GREEN, isFull
            );
         case 25:
            return new ParticleEmitter(
               pixelX, pixelY, 6000, ParticleModule.ParticleGroup.SPARKLES, ParticleModule.ParticleSet.WHITE, ParticleModule.ParticleSet.MAGIC_GREEN, isFull
            );
         case 26:
            return new ParticleEmitter(
               pixelX, pixelY, 3000, ParticleModule.ParticleGroup.SPARKLES, ParticleModule.ParticleSet.WHITE, ParticleModule.ParticleSet.MAGIC_GREEN, isFull
            );
         case 27:
            return new ParticleEmitter(
               pixelX, pixelY, 1500, ParticleModule.ParticleGroup.SPARKLES, ParticleModule.ParticleSet.WHITE, ParticleModule.ParticleSet.MAGIC_GREEN, isFull
            );
         case 28:
            return new ParticleEmitter(
               pixelX, pixelY, 12000, ParticleModule.ParticleGroup.SPARKLES, ParticleModule.ParticleSet.WHITE, ParticleModule.ParticleSet.MAGIC_BLUE, isFull
            );
         case 29:
            return new ParticleEmitter(
               pixelX, pixelY, 6000, ParticleModule.ParticleGroup.SPARKLES, ParticleModule.ParticleSet.WHITE, ParticleModule.ParticleSet.MAGIC_BLUE, isFull
            );
         case 30:
            return new ParticleEmitter(
               pixelX, pixelY, 3000, ParticleModule.ParticleGroup.SPARKLES, ParticleModule.ParticleSet.WHITE, ParticleModule.ParticleSet.MAGIC_BLUE, isFull
            );
         case 31:
            return new ParticleEmitter(
               pixelX, pixelY, 1500, ParticleModule.ParticleGroup.SPARKLES, ParticleModule.ParticleSet.WHITE, ParticleModule.ParticleSet.MAGIC_BLUE, isFull
            );
         case 32:
            return new ParticleEmitter(
               pixelX,
               pixelY,
               1000,
               ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS,
               ParticleModule.ParticleSet.MAGIC_RED,
               ParticleModule.ParticleSet.MAGIC_RED,
               isFull
            );
         case 33:
            return new ParticleEmitter(
               pixelX,
               pixelY,
               500,
               ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS,
               ParticleModule.ParticleSet.MAGIC_RED,
               ParticleModule.ParticleSet.MAGIC_RED,
               isFull
            );
         case 34:
            return new ParticleEmitter(
               pixelX,
               pixelY,
               100,
               ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS,
               ParticleModule.ParticleSet.MAGIC_RED,
               ParticleModule.ParticleSet.MAGIC_RED,
               isFull
            );
         case 35:
            return new ParticleEmitter(
               pixelX,
               pixelY,
               1000,
               ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS,
               ParticleModule.ParticleSet.MAGIC_YELLOW,
               ParticleModule.ParticleSet.MAGIC_YELLOW,
               isFull
            );
         case 36:
            return new ParticleEmitter(
               pixelX,
               pixelY,
               500,
               ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS,
               ParticleModule.ParticleSet.MAGIC_YELLOW,
               ParticleModule.ParticleSet.MAGIC_YELLOW,
               isFull
            );
         case 37:
            return new ParticleEmitter(
               pixelX,
               pixelY,
               100,
               ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS,
               ParticleModule.ParticleSet.MAGIC_YELLOW,
               ParticleModule.ParticleSet.MAGIC_YELLOW,
               isFull
            );
         case 38:
            return new ParticleEmitter(
               pixelX,
               pixelY,
               1000,
               ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS,
               ParticleModule.ParticleSet.MAGIC_GREEN,
               ParticleModule.ParticleSet.MAGIC_GREEN,
               isFull
            );
         case 39:
            return new ParticleEmitter(
               pixelX,
               pixelY,
               500,
               ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS,
               ParticleModule.ParticleSet.MAGIC_GREEN,
               ParticleModule.ParticleSet.MAGIC_GREEN,
               isFull
            );
         case 40:
            return new ParticleEmitter(
               pixelX,
               pixelY,
               100,
               ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS,
               ParticleModule.ParticleSet.MAGIC_GREEN,
               ParticleModule.ParticleSet.MAGIC_GREEN,
               isFull
            );
         case 41:
            return new ParticleEmitter(
               pixelX,
               pixelY,
               1000,
               ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS,
               ParticleModule.ParticleSet.MAGIC_BLUE,
               ParticleModule.ParticleSet.MAGIC_BLUE,
               isFull
            );
         case 42:
            return new ParticleEmitter(
               pixelX,
               pixelY,
               500,
               ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS,
               ParticleModule.ParticleSet.MAGIC_BLUE,
               ParticleModule.ParticleSet.MAGIC_BLUE,
               isFull
            );
         case 43:
            return new ParticleEmitter(
               pixelX,
               pixelY,
               100,
               ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS,
               ParticleModule.ParticleSet.MAGIC_BLUE,
               ParticleModule.ParticleSet.MAGIC_BLUE,
               isFull
            );
         case 44:
            return new ParticleEmitter(
               pixelX, pixelY, 12000, ParticleModule.ParticleGroup.SPARKLES, ParticleModule.ParticleSet.WHITE, ParticleModule.ParticleSet.MAGIC_PURPLE, isFull
            );
         case 45:
            return new ParticleEmitter(
               pixelX, pixelY, 6000, ParticleModule.ParticleGroup.SPARKLES, ParticleModule.ParticleSet.WHITE, ParticleModule.ParticleSet.MAGIC_PURPLE, isFull
            );
         case 46:
            return new ParticleEmitter(
               pixelX, pixelY, 3000, ParticleModule.ParticleGroup.SPARKLES, ParticleModule.ParticleSet.WHITE, ParticleModule.ParticleSet.MAGIC_PURPLE, isFull
            );
         case 47:
            return new ParticleEmitter(
               pixelX, pixelY, 1500, ParticleModule.ParticleGroup.SPARKLES, ParticleModule.ParticleSet.WHITE, ParticleModule.ParticleSet.MAGIC_PURPLE, isFull
            );
         case 48:
            return new ParticleEmitter(pixelX, pixelY, 200, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_PURPLE, isFull);
         case 49:
            return new ParticleEmitter(pixelX, pixelY, 100, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_PURPLE, isFull);
         case 50:
            return new ParticleEmitter(pixelX, pixelY, 30, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_PURPLE, isFull);
         case 51:
            return new ParticleEmitter(pixelX, pixelY, 10, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_PURPLE, isFull);
         case 52:
            return new ParticleEmitter(
               pixelX,
               pixelY,
               1000,
               ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS,
               ParticleModule.ParticleSet.MAGIC_PURPLE,
               ParticleModule.ParticleSet.MAGIC_PURPLE,
               isFull
            );
         case 53:
            return new ParticleEmitter(
               pixelX,
               pixelY,
               500,
               ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS,
               ParticleModule.ParticleSet.MAGIC_PURPLE,
               ParticleModule.ParticleSet.MAGIC_PURPLE,
               isFull
            );
         case 54:
            return new ParticleEmitter(
               pixelX,
               pixelY,
               100,
               ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS,
               ParticleModule.ParticleSet.MAGIC_PURPLE,
               ParticleModule.ParticleSet.MAGIC_PURPLE,
               isFull
            );
         case 55:
            return new ParticleEmitter(
               pixelX, pixelY, 12000, ParticleModule.ParticleGroup.SPARKLES, ParticleModule.ParticleSet.WHITE, ParticleModule.ParticleSet.MAGIC_YELLOW, isFull
            );
         case 56:
            return new ParticleEmitter(
               pixelX, pixelY, 6000, ParticleModule.ParticleGroup.SPARKLES, ParticleModule.ParticleSet.WHITE, ParticleModule.ParticleSet.MAGIC_YELLOW, isFull
            );
         case 57:
            return new ParticleEmitter(
               pixelX, pixelY, 3000, ParticleModule.ParticleGroup.SPARKLES, ParticleModule.ParticleSet.WHITE, ParticleModule.ParticleSet.MAGIC_YELLOW, isFull
            );
         case 58:
            return new ParticleEmitter(
               pixelX, pixelY, 1500, ParticleModule.ParticleGroup.SPARKLES, ParticleModule.ParticleSet.WHITE, ParticleModule.ParticleSet.MAGIC_YELLOW, isFull
            );
         case 59:
            return new ParticleEmitter(
               pixelX, pixelY, 100, ParticleModule.ParticleGroup.MAGIC_FIRE, ParticleModule.ParticleSet.MAGIC_RED, ParticleModule.ParticleSet.MAGIC_RED, isFull
            );
         case 60:
            return new ParticleEmitter(
               pixelX, pixelY, 30, ParticleModule.ParticleGroup.MAGIC_FIRE, ParticleModule.ParticleSet.MAGIC_RED, ParticleModule.ParticleSet.MAGIC_RED, isFull
            );
         case 61:
            return new ParticleEmitter(
               pixelX, pixelY, 10, ParticleModule.ParticleGroup.MAGIC_FIRE, ParticleModule.ParticleSet.MAGIC_RED, ParticleModule.ParticleSet.MAGIC_RED, isFull
            );
         case 62:
            return new ParticleEmitter(
               pixelX,
               pixelY,
               100,
               ParticleModule.ParticleGroup.MAGIC_FIRE,
               ParticleModule.ParticleSet.MAGIC_YELLOW,
               ParticleModule.ParticleSet.MAGIC_YELLOW,
               isFull
            );
         case 63:
            return new ParticleEmitter(
               pixelX,
               pixelY,
               30,
               ParticleModule.ParticleGroup.MAGIC_FIRE,
               ParticleModule.ParticleSet.MAGIC_YELLOW,
               ParticleModule.ParticleSet.MAGIC_YELLOW,
               isFull
            );
         case 64:
            return new ParticleEmitter(
               pixelX,
               pixelY,
               10,
               ParticleModule.ParticleGroup.MAGIC_FIRE,
               ParticleModule.ParticleSet.MAGIC_YELLOW,
               ParticleModule.ParticleSet.MAGIC_YELLOW,
               isFull
            );
         case 65:
            return new ParticleEmitter(
               pixelX,
               pixelY,
               100,
               ParticleModule.ParticleGroup.MAGIC_FIRE,
               ParticleModule.ParticleSet.MAGIC_GREEN,
               ParticleModule.ParticleSet.MAGIC_GREEN,
               isFull
            );
         case 66:
            return new ParticleEmitter(
               pixelX,
               pixelY,
               30,
               ParticleModule.ParticleGroup.MAGIC_FIRE,
               ParticleModule.ParticleSet.MAGIC_GREEN,
               ParticleModule.ParticleSet.MAGIC_GREEN,
               isFull
            );
         case 67:
            return new ParticleEmitter(
               pixelX,
               pixelY,
               10,
               ParticleModule.ParticleGroup.MAGIC_FIRE,
               ParticleModule.ParticleSet.MAGIC_GREEN,
               ParticleModule.ParticleSet.MAGIC_GREEN,
               isFull
            );
         case 68:
            return new ParticleEmitter(
               pixelX,
               pixelY,
               100,
               ParticleModule.ParticleGroup.MAGIC_FIRE,
               ParticleModule.ParticleSet.MAGIC_BLUE,
               ParticleModule.ParticleSet.MAGIC_BLUE,
               isFull
            );
         case 69:
            return new ParticleEmitter(
               pixelX,
               pixelY,
               30,
               ParticleModule.ParticleGroup.MAGIC_FIRE,
               ParticleModule.ParticleSet.MAGIC_BLUE,
               ParticleModule.ParticleSet.MAGIC_BLUE,
               isFull
            );
         case 70:
            return new ParticleEmitter(
               pixelX,
               pixelY,
               10,
               ParticleModule.ParticleGroup.MAGIC_FIRE,
               ParticleModule.ParticleSet.MAGIC_BLUE,
               ParticleModule.ParticleSet.MAGIC_BLUE,
               isFull
            );
         case 71:
            return new ParticleEmitter(
               pixelX,
               pixelY,
               100,
               ParticleModule.ParticleGroup.MAGIC_FIRE,
               ParticleModule.ParticleSet.MAGIC_PURPLE,
               ParticleModule.ParticleSet.MAGIC_PURPLE,
               isFull
            );
         case 72:
            return new ParticleEmitter(
               pixelX,
               pixelY,
               30,
               ParticleModule.ParticleGroup.MAGIC_FIRE,
               ParticleModule.ParticleSet.MAGIC_PURPLE,
               ParticleModule.ParticleSet.MAGIC_PURPLE,
               isFull
            );
         case 73:
            return new ParticleEmitter(
               pixelX,
               pixelY,
               10,
               ParticleModule.ParticleGroup.MAGIC_FIRE,
               ParticleModule.ParticleSet.MAGIC_PURPLE,
               ParticleModule.ParticleSet.MAGIC_PURPLE,
               isFull
            );
         default:
            return null;
      }
   }

   public void newParticleGroup(float x, float y, ParticleModule.ParticleGroup g, ParticleModule.ParticleSet c1, boolean guiParticle) {
      this.newParticleGroup(x, y, -1.0F, g, c1, c1, c1, guiParticle);
   }

   public void newParticleGroup(
      float x, float y, ParticleModule.ParticleGroup g, ParticleModule.ParticleSet c1, ParticleModule.ParticleSet c2, boolean guiParticle
   ) {
      this.newParticleGroup(x, y, -1.0F, g, c1, c2, c2, guiParticle);
   }

   public void newParticleGroup(float x, float y, float a, ParticleModule.ParticleGroup g, ParticleModule.ParticleSet c1, boolean guiParticle) {
      this.newParticleGroup(x, y, a, g, c1, c1, c1, guiParticle);
   }

   public void newParticleGroup(
      float x, float y, float a, ParticleModule.ParticleGroup g, ParticleModule.ParticleSet c1, ParticleModule.ParticleSet c2, boolean guiParticle
   ) {
      this.newParticleGroup(x, y, a, g, c1, c2, c2, guiParticle);
   }

   public void newParticleGroup(
      float x,
      float y,
      ParticleModule.ParticleGroup g,
      ParticleModule.ParticleSet c1,
      ParticleModule.ParticleSet c2,
      ParticleModule.ParticleSet c3,
      boolean guiParticle
   ) {
      this.newParticleGroup(x, y, -1.0F, g, c1, c2, c3, guiParticle);
   }

   public void newParticleGroup(float x, float y, ParticleModule.ParticleGroup g, ParticleModule.ParticleSet c1) {
      this.newParticleGroup(x, y, -1.0F, g, c1, c1, c1, false);
   }

   public void newParticleGroup(float x, float y, ParticleModule.ParticleGroup g, ParticleModule.ParticleSet c1, ParticleModule.ParticleSet c2) {
      this.newParticleGroup(x, y, -1.0F, g, c1, c2, c2, false);
   }

   public void newParticleGroup(float x, float y, float a, ParticleModule.ParticleGroup g, ParticleModule.ParticleSet c1) {
      this.newParticleGroup(x, y, a, g, c1, c1, c1, false);
   }

   public void newParticleGroup(float x, float y, float a, ParticleModule.ParticleGroup g, ParticleModule.ParticleSet c1, ParticleModule.ParticleSet c2) {
      this.newParticleGroup(x, y, a, g, c1, c2, c2, false);
   }

   public void newParticleGroup(
      float x, float y, ParticleModule.ParticleGroup g, ParticleModule.ParticleSet c1, ParticleModule.ParticleSet c2, ParticleModule.ParticleSet c3
   ) {
      this.newParticleGroup(x, y, -1.0F, g, c1, c2, c3, false);
   }

   public void newParticleGroup(
      float x,
      float y,
      float a,
      ParticleModule.ParticleGroup g,
      ParticleModule.ParticleSet c1,
      ParticleModule.ParticleSet c2,
      ParticleModule.ParticleSet c3,
      boolean guiParticle
   ) {
      ParticleGroupBase group = g.getTemplate();
      group.setColors(c1, c2, c3);
      int randomOut = group.getRandomParticleNumber();
      this.newParticle(x, y, a, group.getParticleType(randomOut), group.getParticleStartColor(randomOut), group.getParticleEndColor(randomOut), guiParticle);
   }

   public void newParticle(float x, float y, ParticleModule.ParticleType type, ParticleModule.ParticleSet color) {
      this.newParticle(x, y, -1.0F, type, color, false);
   }

   public void newParticle(float x, float y, float a, ParticleModule.ParticleType type, ParticleModule.ParticleSet color) {
      this.newParticle(x, y, a, type, color, false);
   }

   public void newParticle(float x, float y, ParticleModule.ParticleType type, ParticleModule.ParticleSet startColor, ParticleModule.ParticleSet endColor) {
      this.newParticle(x, y, -1.0F, type, startColor, endColor, false);
   }

   public void newParticle(float x, float y, ParticleModule.ParticleType type, ParticleModule.ParticleSet color, boolean guiParticle) {
      this.newParticle(x, y, -1.0F, type, color, guiParticle);
   }

   public void newParticle(float x, float y, float a, ParticleModule.ParticleType type, ParticleModule.ParticleSet color, boolean guiParticle) {
      this.newParticle(x, y, a, type, color, null, guiParticle);
   }

   public void newParticle(
      float x,
      float y,
      float a,
      ParticleModule.ParticleType type,
      ParticleModule.ParticleSet startColor,
      ParticleModule.ParticleSet endColor,
      boolean guiParticle
   ) {
      int particleCount = this.particleArrayTop.size() + this.particleArrayBottom.size();
      if (SettingsParser.getParticleAmount() == SettingsParser.ParticleAmount.FULL && particleCount <= 50000 || Utilities.randomInt(2) != 0) {
         if (SettingsParser.getParticleAmount() != SettingsParser.ParticleAmount.MINIMAL && particleCount <= 75000 || type.getTemplate().isEssential()) {
            if (guiParticle || Utilities.randomInt(Game.getCS().getGameSpeed()) == 0) {
               Particle p = new Particle();
               p.configure(x, y, type.getTemplate(), startColor, endColor, a);
               if (guiParticle) {
                  this.particleArrayGUIAdd.add(p);
               } else if (p.isTop()) {
                  this.particleArrayTopAdd.add(p);
               } else {
                  this.particleArrayBottomAdd.add(p);
               }
            }
         }
      }
   }

   public boolean checkLiquidMap(int x, int y) {
      return this.particleLiquidMap.get(x + y * 4096);
   }

   public void addLiquidMap(int x, int y) {
      this.particleLiquidMap.set(x + y * 4096, true);
   }

   public void removeLiquidMap(int x, int y) {
      this.particleLiquidMap.set(x + y * 4096, false);
   }

   public int getParticleCount() {
      return this.particleArrayTop.size() + this.particleArrayBottom.size() + this.particleArrayGUI.size();
   }

   public Color getParticleColor(ParticleModule.ParticleSet set) {
      Color randomColor = this.particleColors[Utilities.randomInt(this.particleColors.length)][set.getID()];
      return new Color(randomColor.r, randomColor.g, randomColor.b);
   }

   public ParticleAnimationLoader getParticleAnimationLoader() {
      return this.particleAnimationLoader;
   }

   public static enum ParticleGroup {
      FIRE(new ParticleGroupFire()),
      FLOATING_DUST(new ParticleGroupFloatingDust()),
      DUST(new ParticleGroupDust()),
      MAGIC_FIRE(new ParticleGroupMagicFire()),
      MAGIC_SMOKE(new ParticleGroupMagicSmoke()),
      SMOKE_BELLOW(new ParticleGroupSmokeBellow()),
      SPARKLES(new ParticleGroupSparkles()),
      MAGIC_FALL(new ParticleGroupMagicFall()),
      FALL(new ParticleGroupFall()),
      FLOATING_MAGIC_RINGS(new ParticleGroupFloatingMagicRings()),
      FLOATING_MAGIC_SQUARES(new ParticleGroupFloatingMagicSquares()),
      BLOOD_SPLATTERS(new ParticleGroupBloodSplatters()),
      ELECTRIC_LINES(new ParticleGroupElectricLines()),
      BLOOD_POOL(new ParticleGroupBloodPool());

      private ParticleGroupBase template;

      private ParticleGroup(ParticleGroupBase template) {
         this.template = template;
      }

      public ParticleGroupBase getTemplate() {
         return this.template;
      }
   }

   public static enum ParticleSet {
      BLOOD_RED(0),
      BLOOD_GREEN(1),
      WATER(2),
      FIRE_RED(8),
      FIRE_GREEN(9),
      FIRE_BLUE(10),
      FIRE_PURPLE(11),
      FIRE_YELLOW(12),
      MAGIC_RED(3),
      MAGIC_GREEN(4),
      MAGIC_BLUE(5),
      MAGIC_PURPLE(6),
      MAGIC_YELLOW(7),
      LIGHT_GRAY(13),
      GRAY(14),
      DARK_GRAY(15),
      BLACK(16),
      BROWN(17),
      WHITE(18);

      private final int particleID;

      private ParticleSet(int particleID) {
         this.particleID = particleID;
      }

      public int getID() {
         return this.particleID;
      }
   }

   public static enum ParticleType {
      BLOOD_SPECK(new ParticleBlood(0)),
      BLOOD_SMALL(new ParticleBlood(1)),
      BLOOD_LARGE(new ParticleBlood(2)),
      BLOOD_POOL_SPECK(new ParticleBloodPool(0)),
      BLOOD_POOL_SMALL(new ParticleBloodPool(1)),
      BLOOD_POOL_LARGE(new ParticleBloodPool(2)),
      WATER(new ParticleWater()),
      WATER_POOL(new ParticleWaterPool()),
      WATER_POOL_FAST(new ParticleWaterPoolFast()),
      RAIN(new ParticleRain()),
      HAIL(new ParticleHail()),
      HAIL_LANDED(new ParticleHailLanded()),
      SPARKLE_SPECK(new ParticleSparkle(0)),
      SPARKLE_SMALL(new ParticleSparkle(1)),
      SPARKLE_MEDIUM(new ParticleSparkle(2)),
      SPARKLE_LARGE(new ParticleSparkle(3)),
      FIRE_SPECK(new ParticleFire(0)),
      FIRE_SMALL(new ParticleFire(1)),
      FIRE_LARGE(new ParticleFire(2)),
      FLASH_SPECK(new ParticleFlash(0)),
      FLASH_SPECKS_2PX(new ParticleFlash(1)),
      FLASH_SPECKS_3PX(new ParticleFlash(2)),
      FLASH_SPECKS_4PX(new ParticleFlash(3)),
      FLASH_LINE_2PX(new ParticleFlash(4)),
      FLASH_LINE_3PX(new ParticleFlash(5)),
      FLASH_LINE_4PX(new ParticleFlash(6)),
      FLASH_LINE_5PX(new ParticleFlash(7)),
      FLASH_LINE_6PX(new ParticleFlash(8)),
      FLASH_FAST(new ParticleFlashFast()),
      FLOATING_DUST_SPECK(new ParticleFloatingDust(0)),
      FLOATING_DUST_CIRCLE_2PX(new ParticleFloatingDust(1)),
      FLOATING_DUST_CIRCLE_4PX(new ParticleFloatingDust(2)),
      MAGIC_FLOAT_SPECK(new ParticleMagicFloat(0)),
      MAGIC_FLOAT_SPECKS_2PX(new ParticleMagicFloat(1)),
      MAGIC_FLOAT_EXPANDING_RING_4PX_DISSIPATING(new ParticleMagicFloat(2)),
      MAGIC_FLOAT_EXPANDING_RING_8PX_DISSIPATING(new ParticleMagicFloat(3)),
      MAGIC_FLOAT_EXPANDING_RING_12PX_DISSIPATING(new ParticleMagicFloat(4)),
      MAGIC_FLOAT_EXPANDING_RING_16PX_DISSIPATING(new ParticleMagicFloat(5)),
      MAGIC_FLOAT_EXPANDING_SQUARE_RING_4PX_DISSIPATING(new ParticleMagicFloat(6)),
      MAGIC_FLOAT_EXPANDING_SQUARE_RING_8PX_DISSIPATING(new ParticleMagicFloat(7)),
      MAGIC_FLOAT_EXPANDING_SQUARE_RING_12PX_DISSIPATING(new ParticleMagicFloat(8)),
      MAGIC_FLOAT_EXPANDING_SQUARE_RING_16PX_DISSIPATING(new ParticleMagicFloat(9)),
      FALL_SPECK(new ParticleFall(0, false)),
      FALL_SPECKS_2PX(new ParticleFall(1, false)),
      FALL_SPECKS_3PX(new ParticleFall(2, false)),
      FALL_SPECKS_4PX(new ParticleFall(3, false)),
      FALL_SPLATTER_3PX(new ParticleFall(4, false)),
      FALL_SPLATTER_5PX(new ParticleFall(5, false)),
      FALL_EXPANDING_RING_4PX_DISSIPATING(new ParticleFall(6, false)),
      FALL_EXPANDING_SQUARE_RING_4PX_DISSIPATING(new ParticleFall(7, false)),
      FALL_FAST_SPECK(new ParticleFall(0, true)),
      FALL_FAST_SPECKS_2PX(new ParticleFall(1, true)),
      FALL_FAST_SPECKS_3PX(new ParticleFall(2, true)),
      FALL_FAST_SPECKS_4PX(new ParticleFall(3, true)),
      FALL_FAST_SPLATTER_3PX(new ParticleFall(4, true)),
      FALL_FAST_SPLATTER_5PX(new ParticleFall(5, true)),
      FALL_FAST_EXPANDING_RING_4PX_DISSIPATING(new ParticleFall(6, true)),
      FALL_FAST_EXPANDING_SQUARE_RING_4PX_DISSIPATING(new ParticleFall(7, true)),
      SMOKE_SPECK(new ParticleSmoke(0)),
      SMOKE_LARGE_SPECK(new ParticleSmoke(1)),
      SMOKE_SMALL_PUFF(new ParticleSmoke(2)),
      SMOKE_MEDIUM_PUFF(new ParticleSmoke(3)),
      SMOKE_LARGE_PUFF(new ParticleSmoke(4)),
      MAGIC_SMOKE_SPECK(new ParticleMagicSmoke(0)),
      MAGIC_SMOKE_SMALL(new ParticleMagicSmoke(1)),
      MAGIC_SMOKE_PUFF(new ParticleMagicSmoke(2)),
      SPECK(new ParticleSpeck()),
      DUST_SPECK(new ParticleDust(0)),
      DUST_SPECKS_2PX(new ParticleDust(1)),
      DUST_SPECKS_3PX(new ParticleDust(2)),
      DUST_SPECKS_4PX(new ParticleDust(3)),
      DUST_SPLATTER_3PX(new ParticleDust(4)),
      DUST_SPLATTER_5PX(new ParticleDust(5)),
      CRUMB(new ParticleCrumb()),
      SNOW(new ParticleSnow()),
      WIND_DUST(new ParticleWindDust());

      private final ParticleBase template;

      private ParticleType(ParticleBase template) {
         this.template = template;
      }

      public ParticleBase getTemplate() {
         return this.template;
      }
   }
}
