package rtr.map;

import rtr.utilities.IntKeyMap;
import java.util.HashMap;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.objects.ObjectBase;
import rtr.states.StateBase;
import rtr.utilities.Utilities;

public class CollisionModule extends ModuleBase {
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
   private int tileGlobalX;
   private int tileGlobalY;
   private byte[][] collisionMap;
   private IntKeyMap<byte[][]> collisionMasks;
   private Color[] colorNorth;
   private Color[] colorEast;
   private Color[] colorSouth;
   private Color[] colorWest;
   private Color[] colorAll;

   public byte getCollisionNone() {
      return 0;
   }

   public byte getCollisionNorthLow() {
      return 1;
   }

   public byte getCollisionNorthMedium() {
      return 2;
   }

   public byte getCollisionNorthHigh() {
      return 3;
   }

   public byte getCollisionEastLow() {
      return 4;
   }

   public byte getCollisionEastMedium() {
      return 5;
   }

   public byte getCollisionEastHigh() {
      return 6;
   }

   public byte getCollisionSouthLow() {
      return 7;
   }

   public byte getCollisionSouthMedium() {
      return 8;
   }

   public byte getCollisionSouthHigh() {
      return 9;
   }

   public byte getCollisionWestLow() {
      return 10;
   }

   public byte getCollisionWestMedium() {
      return 11;
   }

   public byte getCollisionWestHigh() {
      return 12;
   }

   public byte getCollisionAllLow() {
      return 13;
   }

   public byte getCollisionAllMedium() {
      return 14;
   }

   public byte getCollisionAllHigh() {
      return 15;
   }

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
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

      if (this.collisionMap == null) {
         this.collisionMap = new byte[4096][4096];
      }

      if (this.collisionMasks == null) {
         this.collisionMasks = this.map.getMapTileLoader().getCollisionTiles();
      }

      this.buildMap();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
      this.tileGlobalX = 0;
      this.tileGlobalY = 0;

      for (int x = 0; x < this.collisionMap.length; x++) {
         for (int y = 0; y < this.collisionMap[x].length; y++) {
            this.collisionMap[x][y] = 0;
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
      if (this.map.getMapTileLoader().isTileCollision(tileID)) {
         this.tileGlobalX = tX * 16;
         this.tileGlobalY = tY * 16;
         byte[][] collisionArrayBuilder = this.collisionMasks.get(tileID);

         for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
               int pixelX = this.tileGlobalX + x;
               int pixelY = this.tileGlobalY + y;
               if (collisionArrayBuilder[x][y] != 0) {
                  this.collisionMap[pixelX][pixelY] = collisionArrayBuilder[x][y];
               }
            }
         }

         collisionArrayBuilder = null;
      }
   }

   public void updateCollisionMap(int x, int y) throws SlickException {
      int[] tileID = new int[12];
      this.resetCollision(x, y);

      for (int l = 0; l < tileID.length; l++) {
         tileID[l] = this.map.getTileId(x, y, l);
         if (tileID[l] != 0) {
            this.builder(x, y, l, tileID[l]);
         }
      }
   }

   private void resetCollision(int tX, int tY) throws SlickException {
      this.tileGlobalX = tX * 16;
      this.tileGlobalY = tY * 16;

      for (int x = 0; x < 16; x++) {
         for (int y = 0; y < 16; y++) {
            int pixelX = this.tileGlobalX + x;
            int pixelY = this.tileGlobalY + y;
            this.collisionMap[pixelX][pixelY] = 0;
         }
      }
   }

   public byte checkParticleCollision(int x, int y, CollisionModule.CollisionHeight h) {
      if (Utilities.isOutOfBounds(4095, 4095, x, y)) {
         return 0;
      } else {
         byte collisionType = this.collisionMap[x][y];
         if (h == CollisionModule.CollisionHeight.ANY) {
            return collisionType;
         } else if (h != CollisionModule.CollisionHeight.HIGH
            || collisionType != 3 && collisionType != 6 && collisionType != 9 && collisionType != 12 && collisionType != 15) {
            if (h != CollisionModule.CollisionHeight.MEDIUM
               || collisionType != 3
                  && collisionType != 6
                  && collisionType != 9
                  && collisionType != 12
                  && collisionType != 15
                  && collisionType != 2
                  && collisionType != 5
                  && collisionType != 8
                  && collisionType != 11
                  && collisionType != 14) {
               return h != CollisionModule.CollisionHeight.LOW
                     || collisionType != 3
                        && collisionType != 6
                        && collisionType != 9
                        && collisionType != 12
                        && collisionType != 15
                        && collisionType != 2
                        && collisionType != 5
                        && collisionType != 8
                        && collisionType != 11
                        && collisionType != 14
                        && collisionType != 1
                        && collisionType != 4
                        && collisionType != 7
                        && collisionType != 10
                        && collisionType != 13
                  ? 0
                  : collisionType;
            } else {
               return collisionType;
            }
         } else {
            return collisionType;
         }
      }
   }

   public boolean checkParticleCollisionBetween(float startX, float startY, float endX, float endY, CollisionModule.CollisionHeight h) {
      float angle = Utilities.calculateAngle(startX, startY, endX, endY);
      double cos = Math.cos(angle);
      double sin = Math.sin(angle);

      while (Math.abs(startX - endX) >= 2.0F || Math.abs(startY - endY) >= 2.0F) {
         startX = (float)(startX + cos);
         startY = (float)(startY + sin);
         if (this.checkParticleCollision((int)startX, (int)startY, h) != 0) {
            return true;
         }
      }

      return false;
   }

   public boolean checkParticleCollisionBetween(float startX, float startY, float endX, float endY, ObjectBase target, CollisionModule.CollisionHeight h) {
      float angle = Utilities.calculateAngle(startX, startY, endX, endY);
      double cos = Math.cos(angle);
      double sin = Math.sin(angle);

      while (Math.abs(startX - endX) >= 2.0F || Math.abs(startY - endY) >= 2.0F) {
         int tileX = (int)(startX / 16.0F);
         int tileY = (int)(startY / 16.0F);
         ObjectBase objeckCheck = this.object.getBuildingAtLocation(tileX, tileY);
         if (objeckCheck != null && objeckCheck == target) {
            return false;
         }

         startX = (float)(startX + cos);
         startY = (float)(startY + sin);
         if (this.checkParticleCollision((int)startX, (int)startY, h) != 0) {
            return true;
         }
      }

      return false;
   }

   public byte[][] getCollisionMap() {
      return this.collisionMap;
   }

   public static enum CollisionHeight {
      ANY,
      LOW,
      MEDIUM,
      HIGH,
      NONE;
   }
}
