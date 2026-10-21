package rtr.system.threadpool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.lighting.LightingModule;
import rtr.map.MapData;
import rtr.map.MapModule;
import rtr.mobs.MobBase;
import rtr.mobs.MobModule;
import rtr.objects.ObjectBase;
import rtr.objects.ObjectModule;
import rtr.particles.ParticleModule;
import rtr.states.StateBase;
import rtr.utilities.OrderedPair;

public class RTRThreadPool implements Runnable {
   private static ForkJoinPool[] threadPools;
   private static int processors = Runtime.getRuntime().availableProcessors();
   private static Thread dedicatedPathThread;
   private static Thread dedicatedDataThread;
   private static Thread dedicatedParticleThread;
   private static List<PathRequest> pathRequests = Collections.synchronizedList(new ArrayList<>());
   private static List<ObjectBase> rangeRequests = Collections.synchronizedList(new ArrayList<>());
   private static List<MobBase> pathMapperRequests = Collections.synchronizedList(new ArrayList<>());
   private static boolean rangeUpdateComplete = true;
   private static boolean pathMapperRequestsComplete = true;
   /**
    * Browser build: threads are TeaVM green threads that only switch when one sleeps or yields, so the dedicated
    * loops must never spin without giving the others (and the render loop) a turn.
    */
   public static boolean cooperative = false;

   private static long lastPause;

   /** Cooperative mode: sleeps when idle; when busy (ms == 0) gives up the CPU about every 8 ms of work. */
   private static void pause(long ms) {
      long now = System.currentTimeMillis();
      if (ms == 0L && now - lastPause < 8L) {
         return;
      }

      lastPause = now;
      try {
         Thread.sleep(ms);
      } catch (InterruptedException var3) {
         var3.printStackTrace();
      }
   }

   public static boolean allThreadsIdle() {
      LightingModule lighting = (LightingModule)StateBase.getModule(ModuleBase.ModuleType.LIGHTING);
      return pathRequests.size() == 0
         && rangeUpdateComplete
         && pathMapperRequestsComplete
         && (lighting == null || lighting.getLightUpdateStep() == LightingModule.LightUpdateStep.FINISH);
   }

   public static void startThreadPool() {
      System.out.println("Starting Dedicated Threads");
      if (processors <= 0) {
         processors = 1;
      }

      if (cooperative) {
         processors = 1;
      }

      threadPools = new ForkJoinPool[processors];
      System.out.println("Thread Pool Size: " + threadPools.length);

      for (int x = 0; x < threadPools.length; x++) {
         threadPools[x] = new ForkJoinPool(x + 1);
      }

      System.out.println();
      if (dedicatedPathThread == null) {
         dedicatedPathThread = new Thread(new Runnable() {
            @Override
            public void run() {
               while (true) {
                  RTRThreadPool.processPathRequests();
                  if (RTRThreadPool.cooperative) {
                     RTRThreadPool.pause(RTRThreadPool.pathRequests.size() == 0 ? 5L : 0L);
                  } else if (RTRThreadPool.pathRequests.size() == 0) {
                     try {
                        Thread.sleep(5L);
                     } catch (InterruptedException var2) {
                        var2.printStackTrace();
                     }
                  }
               }
            }
         }, "Pathfinding Thread");
         dedicatedPathThread.start();
      }

      if (dedicatedDataThread == null) {
         dedicatedDataThread = new Thread(new Runnable() {
            @Override
            public void run() {
               while (true) {
                  RTRThreadPool.processPathMapperMap();
                  RTRThreadPool.processRangeUpdates();
                  RTRThreadPool.processLightingUpdates();
                  LightingModule lighting = (LightingModule)StateBase.getModule(ModuleBase.ModuleType.LIGHTING);
                  if (RTRThreadPool.cooperative) {
                     boolean idle = RTRThreadPool.rangeRequests.size() == 0
                        && RTRThreadPool.pathMapperRequests.size() == 0
                        && (lighting == null || lighting.getLightUpdateStep() == LightingModule.LightUpdateStep.FINISH);
                     RTRThreadPool.pause(idle ? 5L : 0L);
                  } else if (lighting != null && RTRThreadPool.rangeRequests.size() == 0 && lighting.getLightUpdateStep() == LightingModule.LightUpdateStep.FINISH) {
                     try {
                        Thread.sleep(5L);
                     } catch (InterruptedException var3) {
                        var3.printStackTrace();
                     }
                  }
               }
            }
         }, "Data Thread");
         dedicatedDataThread.start();
      }

      if (dedicatedParticleThread == null) {
         dedicatedParticleThread = new Thread(new Runnable() {
            @Override
            public void run() {
               while (true) {
                  ParticleModule particle = (ParticleModule)StateBase.getModule(ModuleBase.ModuleType.PARTICLE);
                  if (particle != null) {
                     try {
                        particle.updateDedicatedThread();
                     } catch (SlickException var3) {
                        var3.printStackTrace();
                     }
                  }

                  if (RTRThreadPool.cooperative) {
                     RTRThreadPool.pause(particle == null ? 5L : 0L);
                  }
               }
            }
         }, "Particle Thread");
         dedicatedParticleThread.start();
      }
   }

   public static ForkJoinPool getThreadPool(int size) {
      return size > processors ? threadPools[threadPools.length - 1] : threadPools[size - 1];
   }

   private static void processPathMapperMap() {
      MobModule mob = (MobModule)StateBase.getModule(ModuleBase.ModuleType.MOB);

      for (int count = 0; count < 20; count++) {
         if (!pathMapperRequestsComplete && pathMapperRequests.size() > 0) {
            MobBase m = pathMapperRequests.get(0);
            mob.getPathMapper().createSinglePath(m);
            pathMapperRequests.remove(0);
         }
      }

      if (!pathMapperRequestsComplete && pathMapperRequests.size() == 0) {
         pathMapperRequestsComplete = true;
      }
   }

   private static void processPathRequests() {
      MobModule mob = (MobModule)StateBase.getModule(ModuleBase.ModuleType.MOB);
      if (pathRequests.size() > 0) {
         PathRequest r = pathRequests.get(0);
         ArrayList<OrderedPair> blockList = null;
         ArrayList<OrderedPair> list;
         if (r.isUnstick()) {
            list = mob.getPathfinder().findUnstickPath(r.getStartX(), r.getStartY(), r.getG());
         } else {
            list = mob.getPathfinder().findPath(r.getStartX(), r.getStartY(), r.getGoalX(), r.getGoalY(), r.isUseForce(), r.isPreferVillage(), r.getG());
            if (list != null) {
               blockList = processBlockList(list, r.getG());
            }
         }

         if (r.getRequestingMob().getActiveNode().isWaitingForPath()) {
            r.getRequestingMob().getActiveNode().loadPathCoordinatesList(list, blockList);
         }

         pathRequests.remove(0);
      }
   }

   private static void processRangeUpdates() {
      try {
         ObjectModule object = (ObjectModule)StateBase.getModule(ModuleBase.ModuleType.OBJECT);
         if (!rangeUpdateComplete && rangeRequests.size() > 0) {
            ObjectBase o = rangeRequests.get(0);
            if (o.getObjectFlags().getBaseRange() > 0) {
               object.getObjectRangeBuilder().createSingleRange(o);
               object.getObjectRangeBuilder().createSingleCorruptionResistance(o);
            }

            rangeRequests.remove(0);
         }

         if (!rangeUpdateComplete && rangeRequests.size() == 0) {
            object.getObjectRangeBuilder().trimRangeMap();
            object.getObjectRangeBuilder().createThreatMap();
            rangeUpdateComplete = true;
         }
      } catch (SlickException var2) {
         var2.printStackTrace();
      }
   }

   private static void processLightingUpdates() {
      try {
         LightingModule lighting = (LightingModule)StateBase.getModule(ModuleBase.ModuleType.LIGHTING);
         if (lighting != null) {
            lighting.updateEntireLightMap();
         }
      } catch (SlickException var1) {
         var1.printStackTrace();
      }
   }

   private static ArrayList<OrderedPair> processBlockList(ArrayList<OrderedPair> list, MapData.BlockMapGroup g) {
      ArrayList<OrderedPair> blockList = new ArrayList<>();
      MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
      byte[][] blockMap = map.getMapData().getBlockMap(g);

      for (OrderedPair o : list) {
         if (blockMap[o.getX()][o.getY()] > 0) {
            blockList.add(o);
         }
      }

      return blockList;
   }

   public static void addNewPathRequest(
      MobBase requestingMob, int startX, int startY, int goalX, int goalY, boolean useForce, boolean preferVillage, MapData.BlockMapGroup g
   ) {
      pathRequests.add(new PathRequest(requestingMob, startX, startY, goalX, goalY, useForce, preferVillage, g));
   }

   public static void addNewUnstickPathRequest(MobBase requestingMob, int startX, int startY, MapData.BlockMapGroup g) {
      pathRequests.add(new PathRequest(requestingMob, startX, startY, g, true));
   }

   public static List<PathRequest> getPathRequests() {
      return pathRequests;
   }

   public static void generateNewRangeMap(ArrayList<ObjectBase> list) {
      rangeRequests.clear();
      rangeRequests.addAll(list);
      rangeUpdateComplete = false;
   }

   public static boolean isRangeComplete() {
      return rangeUpdateComplete;
   }

   public static void generatePathMapperMap(ArrayList<MobBase> list) {
      pathMapperRequests.clear();
      pathMapperRequests.addAll(list);
      pathMapperRequestsComplete = false;
   }

   public static boolean isPathMapperRequestsComplete() {
      return pathMapperRequestsComplete;
   }

   @Override
   public void run() {
   }
}
