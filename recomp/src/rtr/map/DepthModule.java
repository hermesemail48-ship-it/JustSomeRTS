package rtr.map;

import rtr.utilities.IntKeyMap;
import java.util.HashMap;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.states.StateBase;
import rtr.utilities.ArrayFillUtil;

public class DepthModule extends ModuleBase {
   private int tileGlobalX;
   private int tileGlobalY;
   private byte[][] depthMap;
   private ImageBuffer[] renderBuffer = new ImageBuffer[17];
   private Image[] render = new Image[17];
   private IntKeyMap<byte[][]> depthMasks;
   private boolean debug = false;

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      if (this.debug) {
         for (int x = 0; x < this.renderBuffer.length; x++) {
            this.renderBuffer[x] = new ImageBuffer(1, 1);
         }

         for (int x = 0; x < this.renderBuffer.length; x++) {
            this.renderBuffer[x].setRGBA(0, 0, x * 10, x * 10, x * 10, 255);
         }

         for (int x = 0; x < this.render.length; x++) {
            this.render[x] = this.renderBuffer[x].getImage();
            this.render[x].setFilter(9728);
         }
      }

      if (this.depthMap == null) {
         this.depthMap = new byte[4096][4096];
      }

      if (this.depthMasks == null) {
         this.depthMasks = this.map.getMapTileLoader().getDepthTiles();
      }

      this.buildMap();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
      this.tileGlobalX = 0;
      this.tileGlobalY = 0;
      ArrayFillUtil.fill(this.depthMap, (byte)0);
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

   private void buildMap() throws SlickException {
      int[] tileID = new int[12];
      int mapWidth = 256;
      int mapHeight = 256;

      for (int x = 0; x < mapWidth; x++) {
         for (int y = 0; y < mapHeight; y++) {
            for (int l = 0; l < tileID.length; l++) {
               tileID[l] = this.map.getTileId(x, y, l);
               if (tileID[l] != 0) {
                  this.builder(x, y, l, tileID[l]);
               }
            }
         }
      }
   }

   private void builder(int tX, int tY, int tL, int tileID) throws SlickException {
      if (this.map.getMapTileLoader().isTileDepth(tileID)) {
         this.tileGlobalX = tX * 16;
         this.tileGlobalY = tY * 16;
         byte[][] depthArrayBuilder = this.depthMasks.get(tileID);

         for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
               int pixelX = this.tileGlobalX + x;
               int pixelY = this.tileGlobalY + y;
               if (depthArrayBuilder[x][y] != 0) {
                  this.depthMap[pixelX][pixelY] = depthArrayBuilder[x][y];
               }
            }
         }
      }
   }

   public void updateDepthMap(int x, int y) throws SlickException {
      int[] tileID = new int[12];
      this.resetDepth(x, y);

      for (int l = 0; l < tileID.length; l++) {
         tileID[l] = this.map.getTileId(x, y, l);
         if (tileID[l] != 0) {
            this.builder(x, y, l, tileID[l]);
         }
      }
   }

   private void resetDepth(int tX, int tY) throws SlickException {
      this.tileGlobalX = tX * 16;
      this.tileGlobalY = tY * 16;

      for (int x = 0; x < 16; x++) {
         for (int y = 0; y < 16; y++) {
            int pixelX = this.tileGlobalX + x;
            int pixelY = this.tileGlobalY + y;
            this.depthMap[pixelX][pixelY] = 0;
         }
      }
   }

   public byte checkParticleDepth(int objectX, int objectY) {
      return this.depthMap[objectX][objectY];
   }

   public byte[][] getDepthMap() {
      return this.depthMap;
   }
}
