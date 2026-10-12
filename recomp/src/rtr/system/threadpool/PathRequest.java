package rtr.system.threadpool;

import rtr.map.MapData;
import rtr.mobs.MobBase;

public class PathRequest {
   private MobBase requestingMob;
   private int startX;
   private int startY;
   private int goalX;
   private int goalY;
   private boolean useForce;
   private boolean preferVillage;
   private MapData.BlockMapGroup g;
   private boolean unstick;

   public PathRequest(MobBase requestingMob, int startX, int startY, MapData.BlockMapGroup g, boolean unstick) {
      this.requestingMob = requestingMob;
      this.startX = startX;
      this.startY = startY;
      this.g = g;
      this.unstick = unstick;
   }

   public PathRequest(MobBase requestingMob, int startX, int startY, int goalX, int goalY, boolean useForce, boolean preferVillage, MapData.BlockMapGroup g) {
      this.requestingMob = requestingMob;
      this.startX = startX;
      this.startY = startY;
      this.goalX = goalX;
      this.goalY = goalY;
      this.useForce = useForce;
      this.preferVillage = preferVillage;
      this.g = g;
   }

   public MobBase getRequestingMob() {
      return this.requestingMob;
   }

   public int getStartX() {
      return this.startX;
   }

   public int getStartY() {
      return this.startY;
   }

   public int getGoalX() {
      return this.goalX;
   }

   public int getGoalY() {
      return this.goalY;
   }

   public boolean isUseForce() {
      return this.useForce;
   }

   public boolean isPreferVillage() {
      return this.preferVillage;
   }

   public MapData.BlockMapGroup getG() {
      return this.g;
   }

   public boolean isUnstick() {
      return this.unstick;
   }
}
