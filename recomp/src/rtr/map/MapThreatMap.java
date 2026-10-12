package rtr.map;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.console.Console;
import rtr.mobs.MobBase;
import rtr.mobs.MobModule;
import rtr.objects.ObjectBase;
import rtr.objects.ObjectModule;
import rtr.objects.towerhead.TowerBase;
import rtr.resources.ResourceBase;
import rtr.save.YMLDataMap;
import rtr.spawning.CorruptionModule;
import rtr.states.StateBase;
import rtr.utilities.ArrayFillUtil;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class MapThreatMap {
   private MobModule mob = (MobModule)StateBase.getModule(ModuleBase.ModuleType.MOB);
   private ObjectModule object = (ObjectModule)StateBase.getModule(ModuleBase.ModuleType.OBJECT);
   private CorruptionModule corruption = (CorruptionModule)StateBase.getModule(ModuleBase.ModuleType.CORRUPTION);
   protected CollisionModule collision = (CollisionModule)StateBase.getModule(ModuleBase.ModuleType.COLLISION);
   protected MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   public static final byte THREAT_MAXIMUM = 127;
   private byte[][] threatMap = new byte[256][256];
   private int threatCooldownTick;

   public MapThreatMap() throws SlickException {
   }

   protected void loadRegionalSaveData(YMLDataMap dataMap) {
      if (dataMap.containsKey(Integer.toString(0))) {
         Console.saveConverterOut(this, "InDev 33f", "InDev 34 Unstable 1", "Converting threatAmountCount");

         for (int threatAmountCount = 0; dataMap.containsKey(Integer.toString(threatAmountCount)); threatAmountCount++) {
            YMLDataMap thisMap = dataMap.getSubMap(Integer.toString(threatAmountCount));
            OrderedPair o = dataMap.unpackCoordinates(Integer.toString(threatAmountCount));
            int x = o.getX();
            int y = o.getY();
            byte value = Byte.parseByte(thisMap.getOrDefault("value", 0));
            this.threatMap[x][y] = value;
         }
      } else if (dataMap.containsKey("threatMap")) {
         dataMap.unpackCoordinatesListWithValueToArray("threatMap", this.threatMap);
      }
   }

   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      ArrayList<OrderedPair> coordinatesList = new ArrayList<>();
      ArrayList<Integer> valuesList = new ArrayList<>();

      for (int x = 0; x < this.threatMap.length; x++) {
         for (int y = 0; y < this.threatMap[x].length; y++) {
            if (this.threatMap[x][y] != 0 && !this.corruption.isTileCorrupted(x, y)) {
               coordinatesList.add(OrderedPair.getOrderedPair(x, y));
               valuesList.add(Integer.valueOf(this.threatMap[x][y]));
            }
         }
      }

      if (coordinatesList.size() > 0) {
         String threatMapString = dataMap.packCoordinatesListWithValue(coordinatesList, valuesList);
         dataMap.put("threatMap", threatMapString);
      }

      return dataMap;
   }

   public void regenerateCorruptionTileThreat() {
      for (OrderedPair c : this.corruption.getCorruptionTilesArray()) {
         this.setThreat(c.getX(), c.getY(), (byte)127);
      }
   }

   public void update() throws SlickException {
      this.threatCooldownTick++;
      if (this.threatCooldownTick % 40 == 0) {
         for (int x = 0; x < 256; x++) {
            for (int y = 0; y < 256; y++) {
               if (!this.corruption.isTileCorrupted(x, y) && this.threatMap[x][y] > 0) {
                  this.threatMap[x][y]--;
               }
            }
         }
      }

      if (this.threatCooldownTick % 100 == 0) {
         for (MobBase m : this.mob.getMobArray()) {
            if (!m.isDead() && m.getMobGroup() != MobBase.MobGroup.CHARMED_MONSTER && m.getMobFlags().canThreatenTiles()) {
               for (OrderedPair c : this.mob.getPathfinder().getCoordinatesInRange(m.getTileX(), m.getTileY(), 10, false, m.getMobFlags().getBlockMapGroup())) {
                  if (this.threatMap[c.getX()][c.getY()] < 127) {
                     this.increaseThreat(c.getX(), c.getY(), (byte)10);
                  }
               }
            }
         }

         for (ObjectBase o : this.object.getObjectsByGroup(MobBase.MobGroup.MONSTER)) {
            if (o.getTower() != null) {
               TowerBase t = o.getTower();

               for (OrderedPair cx : Utilities.getAllCoordinatesWithin(t.getX(), t.getY(), t.getTowerFlags().getDistance() + 4)) {
                  if (!this.corruption.isTileCorrupted(cx.getX(), cx.getY())
                     && !this.collision
                        .checkParticleCollisionBetween(
                           t.getCenterPixelX(),
                           t.getCenterPixelY(),
                           MapModule.getCenterPixelOfCoordinateX(cx.getX()),
                           MapModule.getCenterPixelOfCoordinateY(cx.getY()),
                           t.getTowerFlags().getCollisionHeight()
                        )
                     && this.threatMap[cx.getX()][cx.getY()] < 127) {
                     this.increaseThreat(cx.getX(), cx.getY(), (byte)10);
                  }
               }
            }
         }

         this.regenerateCorruptionTileThreat();
      }
   }

   public void increaseThreat(int tileX, int tileY, byte i) {
      if (this.threatMap[tileX][tileY] < 127 - i) {
         this.threatMap[tileX][tileY] = (byte)(this.threatMap[tileX][tileY] + i);
      } else {
         this.threatMap[tileX][tileY] = 127;
      }

      for (int x = -1; x < 2; x++) {
         for (int y = -1; y < 2; y++) {
            if ((x != 0 || y != 0)
               && (x != -1 || y != -1)
               && (x != 1 || y != 1)
               && (x != -1 || y != 1)
               && (x != 1 || y != -1)
               && !Utilities.isOutOfBoundsOfUsableMap(tileX + x, tileY + y)
               && this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[tileX + x][tileY + y] > 0) {
               if (this.threatMap[tileX + x][tileY + y] < 127 - i) {
                  this.threatMap[tileX + x][tileY + y] = (byte)(this.threatMap[tileX + x][tileY + y] + i);
               } else {
                  this.threatMap[tileX + x][tileY + y] = 127;
               }
            }
         }
      }
   }

   public void setThreat(int tileX, int tileY, byte i) {
      this.threatMap[tileX][tileY] = i;

      for (int x = -1; x < 2; x++) {
         for (int y = -1; y < 2; y++) {
            if ((x != 0 || y != 0)
               && (x != -1 || y != -1)
               && (x != 1 || y != 1)
               && (x != -1 || y != 1)
               && (x != 1 || y != -1)
               && !Utilities.isOutOfBoundsOfUsableMap(tileX + x, tileY + y)
               && this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[tileX + x][tileY + y] > 0) {
               this.threatMap[tileX + x][tileY + y] = i;
            }
         }
      }
   }

   public byte[][] getThreatMap() {
      return this.threatMap;
   }

   public boolean isTileThreatened(OrderedPair o) {
      return this.threatMap[o.getX()][o.getY()] > 0;
   }

   public boolean isTileThreatened(int x, int y) {
      return this.threatMap[x][y] > 0;
   }

   public boolean isResourceThreatened(ResourceBase r) {
      return this.isTileThreatened(r.getTileX(), r.getTileY());
   }

   public boolean isMobThreatened(MobBase m) {
      return this.isTileThreatened(m.getTileX(), m.getTileY());
   }

   public boolean isObjectThreatened(ObjectBase o) {
      int objectWidth = o.getObjectFlags().getWidth();
      int objectHeight = o.getObjectFlags().getHeight();
      int tileX = o.getObjectX();
      int tileY = o.getObjectY();

      for (int w = 0; w < objectWidth; w++) {
         for (int h = 0; h < objectHeight; h++) {
            if (this.isTileThreatened(w + tileX, h + tileY)) {
               return true;
            }
         }
      }

      return false;
   }

   public void reset() {
      ArrayFillUtil.fill(this.threatMap, (byte)0);
   }
}
