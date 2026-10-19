package rtr.mobs;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.font.FontModule;
import rtr.font.Text;
import rtr.map.MapModule;
import rtr.objects.ObjectBase;
import rtr.objects.ObjectModule;
import rtr.resources.ResourceBase;
import rtr.states.StateBase;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class MobReachMap {
   protected MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   protected FontModule font = (FontModule)StateBase.getModule(ModuleBase.ModuleType.FONT);
   protected MobModule mob = (MobModule)StateBase.getModule(ModuleBase.ModuleType.MOB);
   protected ObjectModule object = (ObjectModule)StateBase.getModule(ModuleBase.ModuleType.OBJECT);
   protected MobBase thisMob;
   protected byte[][] reachMap;
   protected ArrayList<OrderedPair> sweepList = new ArrayList<>();
   protected ArrayList<OrderedPair> setList = new ArrayList<>();

   public MobReachMap(MobBase m) throws SlickException {
      this.thisMob = m;
   }

   public void render() throws SlickException {
      int mapWidth = 256;
      int mapHeight = 256;
      float mapX = this.map.getMapX();
      float mapY = this.map.getMapY();

      for (int x = 0; x < mapWidth; x++) {
         for (int y = 0; y < mapHeight; y++) {
            if (this.map.isInView(x * 16, y * 16, 10)) {
               if (this.reachMap[x][y] == 1) {
                  this.font.drawString(x * 16 + mapX, y * 16 + mapY, "$RED1X", Text.FontType.BODY, 0);
               } else if (this.reachMap[x][y] == 2) {
                  this.font.drawString(x * 16 + mapX, y * 16 + mapY, "$GRE1X", Text.FontType.BODY, 0);
               }
            }
         }
      }
   }

   public byte[][] getReachMap() {
      return this.reachMap;
   }

   public void removeReachMap() {
      this.reachMap = null;
   }

   public void buildNewReachMap() {
      if (this.reachMap == null) {
         this.reachMap = new byte[256][256];
         byte[][] blockMap = this.map.getMapData().getBlockMap(this.thisMob.getMobFlags().getBlockMapGroup());
         if (blockMap[this.thisMob.getTileX()][this.thisMob.getTileY()] <= 0) {
            this.sweepList.add(OrderedPair.getOrderedPair(this.thisMob.getTileX(), this.thisMob.getTileY()));

            for (int i = 0; i < blockMap.length; i++) {
               System.arraycopy(blockMap[i], 0, this.reachMap[i], 0, blockMap[i].length);
            }

            this.reachMap[this.thisMob.getTileX()][this.thisMob.getTileY()] = 2;
            int thisX = 0;
            int thisY = 0;
            boolean completedSearch = false;

            while (!completedSearch) {
               for (OrderedPair o : this.sweepList) {
                  thisX = o.getX();
                  thisY = o.getY();
                  if (thisX < 255 && thisX > 0 && thisY < 255 && thisY > 0) {
                     if (this.reachMap[thisX][thisY + 1] == 0) {
                        this.setList.add(OrderedPair.getOrderedPair(thisX, thisY + 1));
                        this.reachMap[thisX][thisY + 1] = 2;
                     }

                     if (this.reachMap[thisX][thisY - 1] == 0) {
                        this.setList.add(OrderedPair.getOrderedPair(thisX, thisY - 1));
                        this.reachMap[thisX][thisY - 1] = 2;
                     }

                     if (this.reachMap[thisX - 1][thisY] == 0) {
                        this.setList.add(OrderedPair.getOrderedPair(thisX - 1, thisY));
                        this.reachMap[thisX - 1][thisY] = 2;
                     }

                     if (this.reachMap[thisX + 1][thisY] == 0) {
                        this.setList.add(OrderedPair.getOrderedPair(thisX + 1, thisY));
                        this.reachMap[thisX + 1][thisY] = 2;
                     }
                  }
               }

               this.sweepList.clear();
               if (this.setList.size() == 0) {
                  completedSearch = true;
               }

               // Swap instead of copying the next frontier (addAll copied it into a fresh array every step).
               ArrayList<OrderedPair> next = this.setList;
               this.setList = this.sweepList;
               this.sweepList = next;
            }

            for (MobBase m : this.mob.getMobArray()) {
               if (m != this.thisMob
                  && this.reachMap[m.getTileX()][m.getTileY()] == 2
                  && this.thisMob.getMobFlags().getBlockMapGroup() == m.getMobFlags().getBlockMapGroup()) {
                  m.shareReachMap(this.reachMap);
               }
            }

            this.sweepList.clear();
            this.setList.clear();
         }
      }
   }

   public void findReachMap() {
      this.reachMap = new byte[256][256];
      byte[][] blockMap = this.map.getMapData().getBlockMap(this.thisMob.getMobFlags().getBlockMapGroup());
      if (blockMap[this.thisMob.getTileX()][this.thisMob.getTileY()] <= 0) {
         for (int i = 0; i < blockMap.length; i++) {
            System.arraycopy(blockMap[i], 0, this.reachMap[i], 0, blockMap[i].length);
         }

         this.sweepList.add(OrderedPair.getOrderedPair(this.thisMob.getTileX(), this.thisMob.getTileY()));
         boolean completedSearch = false;

         while (!completedSearch) {
            for (OrderedPair o : this.sweepList) {
               int thisX = o.getX();
               int thisY = o.getY();
               if (thisX < 255 && thisX > 0 && thisY < 255 && thisY > 0) {
                  if (this.reachMap[thisX][thisY + 1] == 0) {
                     this.setList.add(OrderedPair.getOrderedPair(thisX, thisY + 1));
                     this.reachMap[thisX][thisY + 1] = 2;
                     MobBase mobAt = this.mob.getMobAt(thisX, thisY + 1, this.thisMob);
                     if (mobAt != null && this.thisMob.getMobFlags().getBlockMapGroup() == mobAt.getMobFlags().getBlockMapGroup()) {
                        this.shareReachMap(mobAt.getMobReachMap());
                        break;
                     }
                  }

                  if (this.reachMap[thisX][thisY - 1] == 0) {
                     this.setList.add(OrderedPair.getOrderedPair(thisX, thisY - 1));
                     this.reachMap[thisX][thisY - 1] = 2;
                     MobBase mobAt = this.mob.getMobAt(thisX, thisY - 1, this.thisMob);
                     if (mobAt != null && this.thisMob.getMobFlags().getBlockMapGroup() == mobAt.getMobFlags().getBlockMapGroup()) {
                        this.shareReachMap(mobAt.getMobReachMap());
                        break;
                     }
                  }

                  if (this.reachMap[thisX - 1][thisY] == 0) {
                     this.setList.add(OrderedPair.getOrderedPair(thisX - 1, thisY));
                     this.reachMap[thisX - 1][thisY] = 2;
                     MobBase mobAt = this.mob.getMobAt(thisX - 1, thisY, this.thisMob);
                     if (mobAt != null && this.thisMob.getMobFlags().getBlockMapGroup() == mobAt.getMobFlags().getBlockMapGroup()) {
                        this.shareReachMap(mobAt.getMobReachMap());
                        break;
                     }
                  }

                  if (this.reachMap[thisX + 1][thisY] == 0) {
                     this.setList.add(OrderedPair.getOrderedPair(thisX + 1, thisY));
                     this.reachMap[thisX + 1][thisY] = 2;
                     MobBase mobAt = this.mob.getMobAt(thisX + 1, thisY, this.thisMob);
                     if (mobAt != null && this.thisMob.getMobFlags().getBlockMapGroup() == mobAt.getMobFlags().getBlockMapGroup()) {
                        this.shareReachMap(mobAt.getMobReachMap());
                        break;
                     }
                  }
               }
            }

            this.sweepList.clear();
            if (this.setList.size() == 0) {
               completedSearch = true;
            }

            // Swap instead of copying the next frontier (addAll copied it into a fresh array every step).
            ArrayList<OrderedPair> next = this.setList;
            this.setList = this.sweepList;
            this.sweepList = next;
         }

         this.sweepList.clear();
         this.setList.clear();
      }
   }

   public void shareReachMap(byte[][] reachMapCopy) {
      this.reachMap = reachMapCopy;
   }

   public boolean canReach(MobBase m, boolean checkRangeMap) {
      return this.canReach(m.getTileX(), m.getTileY(), checkRangeMap);
   }

   public boolean canReach(ResourceBase r, boolean checkRangeMap) {
      return this.canReach(r.getTileX(), r.getTileY(), checkRangeMap);
   }

   public boolean canReach(ObjectBase targetObject, boolean checkInRange) {
      int width = targetObject.getObjectFlags().getWidth();
      int height = targetObject.getObjectFlags().getHeight();
      int objectX = targetObject.getObjectX();
      int objectY = targetObject.getObjectY();
      byte[][] blockMap = this.map.getMapData().getBlockMap(this.thisMob.getMobFlags().getBlockMapGroup());

      for (int w = -1; w < width + 1; w++) {
         for (int h = -1; h < height + 1; h++) {
            if (!Utilities.isOutOfBoundsOfUsableMap(objectX + w, objectY + h)
               && this.reachMap[objectX + w][objectY + h] == 2
               && (
                  w == -1 && h == -1
                     ? blockMap[objectX + w + 1][objectY + h] <= 0 || blockMap[objectX + w][objectY + h + 1] <= 0
                     : (
                        w == width && h == height
                           ? blockMap[objectX + w - 1][objectY + h] <= 0 || blockMap[objectX + w][objectY + h - 1] <= 0
                           : (
                              w == -1 && h == height
                                 ? blockMap[objectX + w + 1][objectY + h] <= 0 || blockMap[objectX + w][objectY + h - 1] <= 0
                                 : w != width || h != -1 || blockMap[objectX + w - 1][objectY + h] <= 0 || blockMap[objectX + w][objectY + h + 1] <= 0
                           )
                     )
               )) {
               if (checkInRange && this.object.isInRange(objectX + w, objectY + h)) {
                  return true;
               }

               if (!checkInRange) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public boolean canReach(int tileX, int tileY, boolean checkInRange) {
      if (this.reachMap[tileX][tileY] == 2) {
         if (checkInRange && this.object.isInRange(tileX, tileY)) {
            return true;
         }

         if (!checkInRange) {
            return true;
         }
      }

      return false;
   }
}
