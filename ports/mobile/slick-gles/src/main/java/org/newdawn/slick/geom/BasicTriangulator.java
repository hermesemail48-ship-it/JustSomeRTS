package org.newdawn.slick.geom;

import java.util.ArrayList;

public class BasicTriangulator implements Triangulator {
   private static final float EPSILON = 1.0E-10F;
   private BasicTriangulator.PointList poly = new BasicTriangulator.PointList();
   private BasicTriangulator.PointList tris = new BasicTriangulator.PointList();
   private boolean tried;

   @Override
   public void addPolyPoint(float x, float y) {
      BasicTriangulator.Point p = new BasicTriangulator.Point(x, y);
      if (!this.poly.contains(p)) {
         this.poly.add(p);
      }
   }

   public int getPolyPointCount() {
      return this.poly.size();
   }

   public float[] getPolyPoint(int index) {
      return new float[]{this.poly.get(index).x, this.poly.get(index).y};
   }

   @Override
   public boolean triangulate() {
      this.tried = true;
      return this.process(this.poly, this.tris);
   }

   @Override
   public int getTriangleCount() {
      if (!this.tried) {
         throw new RuntimeException("Call triangulate() before accessing triangles");
      } else {
         return this.tris.size() / 3;
      }
   }

   @Override
   public float[] getTrianglePoint(int tri, int i) {
      if (!this.tried) {
         throw new RuntimeException("Call triangulate() before accessing triangles");
      } else {
         return this.tris.get(tri * 3 + i).toArray();
      }
   }

   private float area(BasicTriangulator.PointList contour) {
      int n = contour.size();
      float A = 0.0F;
      int p = n - 1;

      for (int q = 0; q < n; p = q++) {
         BasicTriangulator.Point contourP = contour.get(p);
         BasicTriangulator.Point contourQ = contour.get(q);
         A += contourP.getX() * contourQ.getY() - contourQ.getX() * contourP.getY();
      }

      return A * 0.5F;
   }

   private boolean insideTriangle(float Ax, float Ay, float Bx, float By, float Cx, float Cy, float Px, float Py) {
      float ax = Cx - Bx;
      float ay = Cy - By;
      float bx = Ax - Cx;
      float by = Ay - Cy;
      float cx = Bx - Ax;
      float cy = By - Ay;
      float apx = Px - Ax;
      float apy = Py - Ay;
      float bpx = Px - Bx;
      float bpy = Py - By;
      float cpx = Px - Cx;
      float cpy = Py - Cy;
      float aCROSSbp = ax * bpy - ay * bpx;
      float cCROSSap = cx * apy - cy * apx;
      float bCROSScp = bx * cpy - by * cpx;
      return aCROSSbp >= 0.0F && bCROSScp >= 0.0F && cCROSSap >= 0.0F;
   }

   private boolean snip(BasicTriangulator.PointList contour, int u, int v, int w, int n, int[] V) {
      float Ax = contour.get(V[u]).getX();
      float Ay = contour.get(V[u]).getY();
      float Bx = contour.get(V[v]).getX();
      float By = contour.get(V[v]).getY();
      float Cx = contour.get(V[w]).getX();
      float Cy = contour.get(V[w]).getY();
      if (1.0E-10F > (Bx - Ax) * (Cy - Ay) - (By - Ay) * (Cx - Ax)) {
         return false;
      } else {
         for (int p = 0; p < n; p++) {
            if (p != u && p != v && p != w) {
               float Px = contour.get(V[p]).getX();
               float Py = contour.get(V[p]).getY();
               if (this.insideTriangle(Ax, Ay, Bx, By, Cx, Cy, Px, Py)) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   private boolean process(BasicTriangulator.PointList contour, BasicTriangulator.PointList result) {
      result.clear();
      int n = contour.size();
      if (n < 3) {
         return false;
      } else {
         int[] V = new int[n];
         if (0.0F < this.area(contour)) {
            int v = 0;

            while (v < n) {
               V[v] = v++;
            }
         } else {
            for (int v = 0; v < n; v++) {
               V[v] = n - 1 - v;
            }
         }

         int nv = n;
         int count = 2 * n;
         int m = 0;
         int v = n - 1;

         while (nv > 2) {
            if (0 >= count--) {
               return false;
            }

            int u = v;
            if (nv <= v) {
               u = 0;
            }

            v = u + 1;
            if (nv <= v) {
               v = 0;
            }

            int w = v + 1;
            if (nv <= w) {
               w = 0;
            }

            if (this.snip(contour, u, v, w, nv, V)) {
               int a = V[u];
               int b = V[v];
               int c = V[w];
               result.add(contour.get(a));
               result.add(contour.get(b));
               result.add(contour.get(c));
               m++;
               int s = v;

               for (int t = v + 1; t < nv; t++) {
                  V[s] = V[t];
                  s++;
               }

               count = 2 * --nv;
            }
         }

         return true;
      }
   }

   @Override
   public void startHole() {
   }

   private class Point {
      private float x;
      private float y;
      private float[] array;

      public Point(float x, float y) {
         this.x = x;
         this.y = y;
         this.array = new float[]{x, y};
      }

      public float getX() {
         return this.x;
      }

      public float getY() {
         return this.y;
      }

      public float[] toArray() {
         return this.array;
      }

      @Override
      public int hashCode() {
         return (int)(this.x * this.y * 31.0F);
      }

      @Override
      public boolean equals(Object other) {
         if (!(other instanceof BasicTriangulator.Point)) {
            return false;
         } else {
            BasicTriangulator.Point p = (BasicTriangulator.Point)other;
            return p.x == this.x && p.y == this.y;
         }
      }
   }

   private class PointList {
      private ArrayList points = new ArrayList();

      public PointList() {
      }

      public boolean contains(BasicTriangulator.Point p) {
         return this.points.contains(p);
      }

      public void add(BasicTriangulator.Point point) {
         this.points.add(point);
      }

      public void remove(BasicTriangulator.Point point) {
         this.points.remove(point);
      }

      public int size() {
         return this.points.size();
      }

      public BasicTriangulator.Point get(int i) {
         return (BasicTriangulator.Point)this.points.get(i);
      }

      public void clear() {
         this.points.clear();
      }
   }
}
