package rtr.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class OrderedPair {
   private static OrderedPair[][] orderedPairMap;
   private int x;
   private int y;

   static {
      initOrderedPairs();
   }

   public OrderedPair(int x, int y) {
      this.x = x;
      this.y = y;
   }

   private static void initOrderedPairs() {
      orderedPairMap = new OrderedPair[256][256];

      for (int x = 0; x < 256; x++) {
         for (int y = 0; y < 256; y++) {
            orderedPairMap[x][y] = new OrderedPair(x, y);
         }
      }
   }

   public static OrderedPair getOrderedPair(int x, int y) {
      return !Utilities.isOutOfBounds(256, 256, x, y) ? orderedPairMap[x][y] : null;
   }

   public static ArrayList<OrderedPair> sortPairsDistance(final int tileX, final int tileY, ArrayList<OrderedPair> list) {
      Collections.sort(list, new Comparator<OrderedPair>() {
         public int compare(OrderedPair m1, OrderedPair m2) {
            int dx1 = m1.getX() - tileX;
            int dy1 = m1.getY() - tileY;
            int hValue1 = dx1 * dx1 + dy1 * dy1;
            int dx2 = m2.getX() - tileX;
            int dy2 = m2.getY() - tileY;
            int hValue2 = dx2 * dx2 + dy2 * dy2;
            return m1 == m2 ? 0 : hValue1 - hValue2;
         }
      });
      return list;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public void setX(int x) {
      this.x = x;
   }

   public void setY(int y) {
      this.y = y;
   }
}
