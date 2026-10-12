package rtr.road;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.map.MapModule;
import rtr.map.MapTilesLoader;
import rtr.states.StateBase;
import rtr.utilities.OrderedPair;

public class RoadFinder {
   private MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   private byte[][] checkMap;
   private ArrayList<OrderedPair> sweepList = new ArrayList<>();
   private ArrayList<OrderedPair> setList = new ArrayList<>();

   public RoadFinder() {
      int mapWidth = 256;
      int mapHeight = 256;
      this.checkMap = new byte[mapWidth][mapHeight];
   }

   public ArrayList<OrderedPair> collectRoadCoordinates(int x, int y) throws SlickException {
      int roadTileID = this.map.getTileId(x, y, 5);
      if (roadTileID == 0) {
         return new ArrayList<>();
      } else {
         int roadTileGID = this.map.getMapTileLoader().getTileSetGID(roadTileID);
         int altRoadTileGID = -1;
         MapTilesLoader.TileSet roadType = this.map.getMapTileLoader().getTileSetByTileID(roadTileID);
         if (roadType == MapTilesLoader.TileSet.LOG_PATH) {
            altRoadTileGID = MapTilesLoader.TileSet.LOG_PATH_DEBRIS.getTileSetTemplate().getTileGID();
         } else if (roadType == MapTilesLoader.TileSet.COBBLE_AND_LOG_PATH) {
            altRoadTileGID = MapTilesLoader.TileSet.COBBLE_AND_LOG_PATH_DEBRIS.getTileSetTemplate().getTileGID();
         } else if (roadType == MapTilesLoader.TileSet.COBBLE_AND_BOARD_ROAD) {
            altRoadTileGID = MapTilesLoader.TileSet.COBBLE_AND_BOARD_ROAD_DEBRIS.getTileSetTemplate().getTileGID();
         } else if (roadType == MapTilesLoader.TileSet.CUT_STONE_AND_BOARD_ROAD) {
            altRoadTileGID = MapTilesLoader.TileSet.CUT_STONE_AND_BOARD_ROAD_DEBRIS.getTileSetTemplate().getTileGID();
         } else if (roadType == MapTilesLoader.TileSet.LOG_PATH_DEBRIS) {
            altRoadTileGID = MapTilesLoader.TileSet.LOG_PATH.getTileSetTemplate().getTileGID();
         } else if (roadType == MapTilesLoader.TileSet.COBBLE_AND_LOG_PATH_DEBRIS) {
            altRoadTileGID = MapTilesLoader.TileSet.COBBLE_AND_LOG_PATH.getTileSetTemplate().getTileGID();
         } else if (roadType == MapTilesLoader.TileSet.COBBLE_AND_BOARD_ROAD_DEBRIS) {
            altRoadTileGID = MapTilesLoader.TileSet.COBBLE_AND_BOARD_ROAD.getTileSetTemplate().getTileGID();
         } else if (roadType == MapTilesLoader.TileSet.CUT_STONE_AND_BOARD_ROAD_DEBRIS) {
            altRoadTileGID = MapTilesLoader.TileSet.CUT_STONE_AND_BOARD_ROAD.getTileSetTemplate().getTileGID();
         }

         this.sweepList.add(OrderedPair.getOrderedPair(x, y));
         int mapWidth = 256;
         int mapHeight = 256;
         this.checkMap = new byte[mapWidth][mapHeight];
         this.checkMap[x][y] = 1;
         ArrayList<OrderedPair> out = new ArrayList<>();
         out.add(OrderedPair.getOrderedPair(x, y));
         int thisX = 0;
         int thisY = 0;
         int thisRoadTileGID = 0;
         boolean completedSearch = false;

         while (!completedSearch) {
            for (OrderedPair o : this.sweepList) {
               thisX = o.getX();
               thisY = o.getY();
               if (thisX < 255 && thisX > 0 && thisY < 255 && thisY > 0) {
                  thisRoadTileGID = this.map.getMapTileLoader().getTileSetGID(this.map.getTileId(thisX, thisY + 1, 5));
                  if ((thisRoadTileGID == roadTileGID || thisRoadTileGID == altRoadTileGID) && this.checkMap[thisX][thisY + 1] == 0) {
                     this.setList.add(OrderedPair.getOrderedPair(thisX, thisY + 1));
                     out.add(OrderedPair.getOrderedPair(thisX, thisY + 1));
                     this.checkMap[thisX][thisY + 1] = 1;
                  }

                  thisRoadTileGID = this.map.getMapTileLoader().getTileSetGID(this.map.getTileId(thisX, thisY - 1, 5));
                  if ((thisRoadTileGID == roadTileGID || thisRoadTileGID == altRoadTileGID) && this.checkMap[thisX][thisY - 1] == 0) {
                     this.setList.add(OrderedPair.getOrderedPair(thisX, thisY - 1));
                     out.add(OrderedPair.getOrderedPair(thisX, thisY - 1));
                     this.checkMap[thisX][thisY - 1] = 1;
                  }

                  thisRoadTileGID = this.map.getMapTileLoader().getTileSetGID(this.map.getTileId(thisX - 1, thisY, 5));
                  if ((thisRoadTileGID == roadTileGID || thisRoadTileGID == altRoadTileGID) && this.checkMap[thisX - 1][thisY] == 0) {
                     this.setList.add(OrderedPair.getOrderedPair(thisX - 1, thisY));
                     out.add(OrderedPair.getOrderedPair(thisX - 1, thisY));
                     this.checkMap[thisX - 1][thisY] = 1;
                  }

                  thisRoadTileGID = this.map.getMapTileLoader().getTileSetGID(this.map.getTileId(thisX + 1, thisY, 5));
                  if ((thisRoadTileGID == roadTileGID || thisRoadTileGID == altRoadTileGID) && this.checkMap[thisX + 1][thisY] == 0) {
                     this.setList.add(OrderedPair.getOrderedPair(thisX + 1, thisY));
                     out.add(OrderedPair.getOrderedPair(thisX + 1, thisY));
                     this.checkMap[thisX + 1][thisY] = 1;
                  }
               }
            }

            this.sweepList.clear();
            if (this.setList.size() == 0) {
               completedSearch = true;
            }

            this.sweepList.addAll(this.setList);
            this.setList.clear();
         }

         this.sweepList.clear();
         this.setList.clear();
         return out;
      }
   }
}
