package rtr.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.ObjectModule;
import rtr.objects.objectflags.ObjectFlags;
import rtr.objects.towerhead.TowerBase;
import rtr.resources.ResourceModule;
import rtr.states.StateBase;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class WaypointModule extends ModuleBase {
   private ArrayList<OrderedPair> rangerPatrolWaypoints;
   private ArrayList<OrderedPair> golemWaypoints;
   private HashMap<ResourceModule.ResourceType, ArrayList<OrderedPair>> animalWaypoints;
   private ArrayList<OrderedPair> rangerPatrolWaypointsRaw;
   private ArrayList<OrderedPair> golemWaypointsRaw;
   private ArrayList<OrderedPair> rangerPatrolWaypointsNew;
   private ArrayList<OrderedPair> golemWaypointsNew;
   private HashMap<ResourceModule.ResourceType, ArrayList<OrderedPair>> animalWaypointsNew;
   private int updateDelayTick;
   private int updateDelay = 200;
   private boolean done;
   private boolean sortingDone;
   private int step;

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      if (this.rangerPatrolWaypoints == null) {
         this.rangerPatrolWaypoints = new ArrayList<>();
      }

      if (this.golemWaypoints == null) {
         this.golemWaypoints = new ArrayList<>();
      }

      if (this.animalWaypoints == null) {
         this.animalWaypoints = new HashMap<>();
      }

      if (this.rangerPatrolWaypointsRaw == null) {
         this.rangerPatrolWaypointsRaw = new ArrayList<>();
      }

      if (this.golemWaypointsRaw == null) {
         this.golemWaypointsRaw = new ArrayList<>();
      }

      if (this.rangerPatrolWaypointsNew == null) {
         this.rangerPatrolWaypointsNew = new ArrayList<>();
      }

      if (this.golemWaypointsNew == null) {
         this.golemWaypointsNew = new ArrayList<>();
      }

      if (this.animalWaypointsNew == null) {
         this.animalWaypointsNew = new HashMap<>();
      }

      this.requiresSync = true;
      this.initLoad();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
      this.rangerPatrolWaypoints.clear();
      this.golemWaypoints.clear();
      this.animalWaypoints.clear();
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

   @Override
   public void initLoadSync() throws SlickException {
      this.generateWaypointsNow();
      this.synced = true;
   }

   public void update() throws SlickException {
      this.updateDelayTick++;
      if (this.updateDelayTick > this.updateDelay) {
         this.generateWaypoints();
         if (this.done) {
            this.done = false;
            this.updateDelayTick = 0;
         }
      }
   }

   public void render(Graphics g) {
      this.renderPath(g, this.rangerPatrolWaypoints, Color.green);
      this.renderPath(g, this.golemWaypoints, Color.yellow);
      g.setColor(Color.white);

      for (ArrayList<OrderedPair> list : this.animalWaypoints.values()) {
         for (OrderedPair s : list) {
            float x1 = s.getX() * 16 + this.map.getMapX();
            float y1 = s.getY() * 16 + this.map.getMapY();
            g.setColor(Color.black);
            g.fillOval(x1 + 2.0F, y1 + 2.0F, 12.0F, 12.0F);
            g.setColor(Color.green);
            g.fillOval(x1 + 3.0F, y1 + 3.0F, 10.0F, 10.0F);
         }
      }
   }

   private void renderPath(Graphics g, ArrayList<OrderedPair> waypoints, Color color) {
      g.setColor(color);

      for (int o = 0; o < waypoints.size(); o++) {
         OrderedPair s = waypoints.get(o);
         OrderedPair e = null;
         if (o < waypoints.size() - 1) {
            e = waypoints.get(o + 1);
         } else {
            e = waypoints.get(0);
         }

         float x1 = s.getX() * 16 + this.map.getMapX();
         float y1 = s.getY() * 16 + this.map.getMapY();
         float x2 = e.getX() * 16 + this.map.getMapX();
         float y2 = e.getY() * 16 + this.map.getMapY();
         g.drawLine(x1, y1, x2, y2);
         this.font.drawString(x1, y1, String.valueOf(o), Text.FontType.BODY, 2);
      }
   }

   private void generateWaypointsNow() throws SlickException {
      this.rangerPatrolWaypointsRaw.clear();
      this.golemWaypointsRaw.clear();

      for (ObjectBase o : this.object.getObjectMasterBuildingPriorityList()) {
         ObjectFlags objectFlags = o.getObjectFlags();
         if (objectFlags.getSubType() == ObjectBase.ObjectSubType.BUILT) {
            if (objectFlags.getBaseType() == MapTilesLoader.TileSet.OUTPOST || objectFlags.getBaseType() == MapTilesLoader.TileSet.RANGER_LODGE) {
               this.rangerPatrolWaypointsRaw.add(o.getBestRandomInteractCoordinates(true));
            } else if (objectFlags.getBaseType() == MapTilesLoader.TileSet.WOOD_GOLEM_COMBOBULATOR
               || objectFlags.getBaseType() == MapTilesLoader.TileSet.STONE_GOLEM_COMBOBULATOR
               || objectFlags.getBaseType() == MapTilesLoader.TileSet.CRYSTAL_GOLEM_COMBOBULATOR
               || o.getTower() != null) {
               this.golemWaypointsRaw.add(o.getBestRandomInteractCoordinates(true));
            }
         }
      }

      this.sortWaypointsNow(this.rangerPatrolWaypointsRaw, this.rangerPatrolWaypoints);
      this.sortWaypointsNow(this.golemWaypointsRaw, this.golemWaypoints);
      this.animalWaypoints.clear();
      int nearbySearchDistance = 32;

      for (int x = 0; x < 256; x++) {
         label211:
         for (int y = 0; y < 256; y++) {
            ResourceModule.ResourceType type = this.resource.getResourceType()[x][y];
            if (type != null) {
               for (int x2 = -1; x2 < 2; x2++) {
                  for (int y2 = -1; y2 < 2; y2++) {
                     if ((x2 != 0 || y2 != 0) && (x2 != -1 || y2 != -1) && (x2 != 1 || y2 != 1) && (x2 != -1 || y2 != 1) && (x2 != 1 || y2 != -1)) {
                        int thisX = x + x2;
                        int thisY = y + y2;
                        if (!Utilities.isOutOfBoundsOfUsableMap(thisX, thisY)
                           && this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD_NO_CORRUPTION)[thisX][thisY] == 0
                           && this.map.getMap()[thisX][thisY][6] <= 0) {
                           if (this.animalWaypoints.get(type) == null) {
                              this.animalWaypoints.put(type, new ArrayList<>());
                           }

                           ArrayList<OrderedPair> thisTypeList = this.animalWaypoints.get(type);
                           if (!thisTypeList.contains(OrderedPair.getOrderedPair(thisX, thisY))) {
                              for (OrderedPair ox : thisTypeList) {
                                 if (Utilities.coordinatesWithin(ox.getX(), ox.getY(), thisX, thisY, nearbySearchDistance)) {
                                    continue label211;
                                 }
                              }

                              thisTypeList.add(OrderedPair.getOrderedPair(thisX, thisY));
                              continue label211;
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      ArrayList<OrderedPair> foodList = this.animalWaypoints.get(ResourceModule.ResourceType.RAW_VEGETABLE);
      ArrayList<OrderedPair> waterList = this.animalWaypoints.get(ResourceModule.ResourceType.DIRTY_WATER_BUCKET);
      if (foodList == null || foodList.size() == 0 || waterList == null || waterList.size() == 0) {
         this.animalWaypoints.clear();
      }

      ResourceModule.ResourceType[] var27;
      for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
         if (this.animalWaypoints.get(type) != null) {
            if (this.animalWaypoints.get(type).size() == 0) {
               this.animalWaypoints.remove(type);
            } else if (type != ResourceModule.ResourceType.DIRTY_WATER_BUCKET && type != ResourceModule.ResourceType.RAW_VEGETABLE) {
               ArrayList<OrderedPair> thisTypeList = this.animalWaypoints.get(type);
               boolean foundWater = false;
               boolean foundFood = false;
               ArrayList<OrderedPair> toRemoveFromList = new ArrayList<>();

               for (OrderedPair oxx : thisTypeList) {
                  for (OrderedPair oF : foodList) {
                     if (Utilities.coordinatesWithin(oxx.getX(), oxx.getY(), oF.getX(), oF.getY(), nearbySearchDistance)) {
                        foundFood = true;
                     }
                  }

                  for (OrderedPair oW : waterList) {
                     if (Utilities.coordinatesWithin(oxx.getX(), oxx.getY(), oW.getX(), oW.getY(), nearbySearchDistance)) {
                        foundWater = true;
                     }
                  }

                  if (foundFood && foundWater) {
                     ArrayList<OrderedPair> nearbyCoordinates = this.mob
                        .getPathfinder()
                        .getCoordinatesInRange(oxx.getX(), oxx.getY(), nearbySearchDistance, false, MapData.BlockMapGroup.STANDARD_NO_CORRUPTION);
                     foundWater = false;
                     foundFood = false;

                     for (OrderedPair oN : nearbyCoordinates) {
                        ResourceModule.ResourceType thisType = this.resource.getResourceType()[oN.getX()][oN.getY()];
                        if (thisType != null) {
                           if (thisType == ResourceModule.ResourceType.RAW_VEGETABLE) {
                              foundFood = true;
                           }

                           if (thisType == ResourceModule.ResourceType.DIRTY_WATER_BUCKET) {
                              foundWater = true;
                           }

                           if (foundFood && foundWater) {
                              break;
                           }
                        }
                     }
                  }

                  if (!foundFood || !foundWater) {
                     toRemoveFromList.add(oxx);
                  }
               }

               thisTypeList.removeAll(toRemoveFromList);
               if (this.animalWaypoints.get(type).size() == 0) {
                  this.animalWaypoints.remove(type);
               }
            }
         }
      }

      this.animalWaypoints.remove(ResourceModule.ResourceType.RAW_VEGETABLE);
      this.animalWaypoints.remove(ResourceModule.ResourceType.DIRTY_WATER_BUCKET);
   }

   private void sortWaypointsNow(ArrayList<OrderedPair> raw, ArrayList<OrderedPair> sorted) throws SlickException {
      sorted.clear();
      Collections.sort(raw, new Comparator<OrderedPair>() {
         public int compare(OrderedPair o1, OrderedPair o2) {
            return o1 == o2 ? 0 : o1.getY() - o2.getY();
         }
      });
      if (raw.size() > 0) {
         sorted.add(raw.get(0));

         while (raw.size() > 0) {
            OrderedPair source = raw.get(0);
            raw.remove(0);
            OrderedPair o = this.mob.getPathfinder().searchFor(source.getX(), source.getY(), raw, false, true, MapData.BlockMapGroup.STANDARD);
            if (o == null) {
               break;
            }

            raw.remove(o);
            raw.add(0, o);
            sorted.add(o);
         }
      }
   }

   private void generateWaypoints() throws SlickException {
      switch (this.step) {
         case 0:
            this.rangerPatrolWaypointsRaw.clear();
            this.golemWaypointsRaw.clear();
            this.rangerPatrolWaypointsNew.clear();
            this.golemWaypointsNew.clear();

            for (ObjectBase ox : this.object.getObjectMasterBuildingPriorityList()) {
               ObjectFlags objectFlags = ox.getObjectFlags();
               if (objectFlags.getSubType() == ObjectBase.ObjectSubType.BUILT) {
                  if (objectFlags.getBaseType() != MapTilesLoader.TileSet.OUTPOST && objectFlags.getBaseType() != MapTilesLoader.TileSet.RANGER_LODGE) {
                     if (objectFlags.getBaseType() == MapTilesLoader.TileSet.WOOD_GOLEM_COMBOBULATOR
                        || objectFlags.getBaseType() == MapTilesLoader.TileSet.STONE_GOLEM_COMBOBULATOR
                        || objectFlags.getBaseType() == MapTilesLoader.TileSet.CRYSTAL_GOLEM_COMBOBULATOR
                        || ox.getTower() != null
                           && ox.getTower().getTowerFlags().getTowerType() != TowerBase.TowerType.MOTIVATOR
                           && objectFlags.getMobGroup() == MobBase.MobGroup.VILLAGER) {
                        this.golemWaypointsRaw.add(ox.getBestRandomInteractCoordinates(false));
                     }
                  } else {
                     this.rangerPatrolWaypointsRaw.add(ox.getBestRandomInteractCoordinates(false));
                  }
               }
            }

            this.step++;
            break;
         case 1:
            this.animalWaypointsNew.clear();

            for (int x = 0; x < 256; x++) {
               label237:
               for (int y = 0; y < 256; y++) {
                  ResourceModule.ResourceType type = this.resource.getResourceType()[x][y];
                  if (type != null) {
                     for (int x2 = -1; x2 < 2; x2++) {
                        for (int y2 = -1; y2 < 2; y2++) {
                           if ((x2 != 0 || y2 != 0) && (x2 != -1 || y2 != -1) && (x2 != 1 || y2 != 1) && (x2 != -1 || y2 != 1) && (x2 != 1 || y2 != -1)) {
                              int thisX = x + x2;
                              int thisY = y + y2;
                              if (!Utilities.isOutOfBoundsOfUsableMap(thisX, thisY)
                                 && this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD_NO_CORRUPTION)[thisX][thisY] == 0
                                 && this.map.getMap()[thisX][thisY][6] <= 0) {
                                 if (this.animalWaypointsNew.get(type) == null) {
                                    this.animalWaypointsNew.put(type, new ArrayList<>());
                                 }

                                 ArrayList<OrderedPair> thisTypeList = this.animalWaypointsNew.get(type);
                                 if (!thisTypeList.contains(OrderedPair.getOrderedPair(thisX, thisY))) {
                                    for (OrderedPair o : thisTypeList) {
                                       if (Utilities.coordinatesWithin(o.getX(), o.getY(), thisX, thisY, 32)) {
                                          continue label237;
                                       }
                                    }

                                    thisTypeList.add(OrderedPair.getOrderedPair(thisX, thisY));
                                    continue label237;
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }

            ArrayList<OrderedPair> foodList = this.animalWaypointsNew.get(ResourceModule.ResourceType.RAW_VEGETABLE);
            ArrayList<OrderedPair> waterList = this.animalWaypointsNew.get(ResourceModule.ResourceType.DIRTY_WATER_BUCKET);
            if (foodList == null || foodList.size() == 0 || waterList == null || waterList.size() == 0) {
               this.animalWaypointsNew.clear();
            }

            ResourceModule.ResourceType[] var28;
            for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
               if (this.animalWaypointsNew.get(type) != null) {
                  if (this.animalWaypointsNew.get(type).size() == 0) {
                     this.animalWaypointsNew.remove(type);
                  } else if (type != ResourceModule.ResourceType.DIRTY_WATER_BUCKET && type != ResourceModule.ResourceType.RAW_VEGETABLE) {
                     ArrayList<OrderedPair> thisTypeList = this.animalWaypointsNew.get(type);
                     boolean foundWater = false;
                     boolean foundFood = false;
                     ArrayList<OrderedPair> toRemoveFromList = new ArrayList<>();

                     for (OrderedPair ox : thisTypeList) {
                        for (OrderedPair oF : foodList) {
                           if (Utilities.coordinatesWithin(ox.getX(), ox.getY(), oF.getX(), oF.getY(), 32)) {
                              foundFood = true;
                           }
                        }

                        for (OrderedPair oW : waterList) {
                           if (Utilities.coordinatesWithin(ox.getX(), ox.getY(), oW.getX(), oW.getY(), 32)) {
                              foundWater = true;
                           }
                        }

                        if (foundFood && foundWater) {
                           ArrayList<OrderedPair> nearbyCoordinates = this.mob
                              .getPathfinder()
                              .getCoordinatesInRange(ox.getX(), ox.getY(), 32, false, MapData.BlockMapGroup.STANDARD_NO_CORRUPTION);
                           foundWater = false;
                           foundFood = false;

                           for (OrderedPair oN : nearbyCoordinates) {
                              ResourceModule.ResourceType thisType = this.resource.getResourceType()[oN.getX()][oN.getY()];
                              if (thisType != null) {
                                 if (thisType == ResourceModule.ResourceType.RAW_VEGETABLE) {
                                    foundFood = true;
                                 }

                                 if (thisType == ResourceModule.ResourceType.DIRTY_WATER_BUCKET) {
                                    foundWater = true;
                                 }

                                 if (foundFood && foundWater) {
                                    break;
                                 }
                              }
                           }
                        }

                        if (!foundFood || !foundWater) {
                           toRemoveFromList.add(ox);
                        }
                     }

                     thisTypeList.removeAll(toRemoveFromList);
                     if (this.animalWaypointsNew.get(type).size() == 0) {
                        this.animalWaypointsNew.remove(type);
                     }
                  }
               }
            }

            this.animalWaypointsNew.remove(ResourceModule.ResourceType.RAW_VEGETABLE);
            this.animalWaypointsNew.remove(ResourceModule.ResourceType.DIRTY_WATER_BUCKET);
            this.step++;
            break;
         case 2:
            this.sortWaypointsStepOne(this.rangerPatrolWaypointsRaw);
            this.step++;
            break;
         case 3:
            this.sortWaypointsStepTwo(this.rangerPatrolWaypointsRaw, this.rangerPatrolWaypointsNew);
            if (this.sortingDone) {
               this.step++;
               this.sortingDone = false;
            }
            break;
         case 4:
            this.sortWaypointsStepOne(this.golemWaypointsRaw);
            this.step++;
            break;
         case 5:
            this.sortWaypointsStepTwo(this.golemWaypointsRaw, this.golemWaypointsNew);
            if (this.sortingDone) {
               this.step++;
               this.sortingDone = false;
            }
            break;
         case 6:
            this.rangerPatrolWaypoints.clear();
            this.rangerPatrolWaypoints.addAll(this.rangerPatrolWaypointsNew);
            this.golemWaypoints.clear();
            this.golemWaypoints.addAll(this.golemWaypointsNew);
            this.animalWaypoints.clear();

            for (ResourceModule.ResourceType t : this.animalWaypointsNew.keySet()) {
               this.animalWaypoints.put(t, new ArrayList<>());
               this.animalWaypoints.get(t).addAll(this.animalWaypointsNew.get(t));
            }

            this.step = 0;
            this.done = true;
      }
   }

   private void sortWaypointsStepOne(ArrayList<OrderedPair> raw) throws SlickException {
      Collections.sort(raw, new Comparator<OrderedPair>() {
         public int compare(OrderedPair o1, OrderedPair o2) {
            return o1 == o2 ? 0 : o1.getY() - o2.getY();
         }
      });
   }

   private void sortWaypointsStepTwo(ArrayList<OrderedPair> raw, ArrayList<OrderedPair> sorted) throws SlickException {
      if (raw.size() > 0) {
         sorted.add(raw.get(0));

         for (int x = 0; x < 5; x++) {
            if (raw.size() > 0) {
               OrderedPair source = raw.get(0);
               raw.remove(0);
               OrderedPair o = this.mob.getPathfinder().searchFor(source.getX(), source.getY(), raw, false, true, MapData.BlockMapGroup.STANDARD);
               if (o == null) {
                  return;
               }

               raw.remove(o);
               raw.add(0, o);
               sorted.add(o);
            }
         }
      } else {
         this.sortingDone = true;
      }
   }

   public ArrayList<OrderedPair> getRangerWaypoints() {
      return this.rangerPatrolWaypoints;
   }

   public ArrayList<OrderedPair> getGolemWaypoints() {
      return this.golemWaypoints;
   }

   public OrderedPair getRandomFrontLinesWaypoint(MobBase m) {
      ArrayList<ObjectBase> nearbyObjects = this.object.getObjectsNearby(m, 8);
      ArrayList<ObjectBase> validObjects = new ArrayList<>();

      for (ObjectBase o : nearbyObjects) {
         if (o.getTower() != null && o.getObjectFlags().getMobGroup() == m.getMobGroup()) {
            validObjects.add(o);
         }
      }

      if (validObjects.size() <= 0) {
         for (ObjectBase ox : this.object.getObjectsBySortGroup(ObjectModule.ObjectSortGroup.TOWERS)) {
            if (m.canReach(ox) && ox.getObjectFlags().getMobGroup() == m.getMobGroup()) {
               validObjects.add(ox);
            }
         }
      }

      return validObjects.size() <= 0 ? null : validObjects.get(Utilities.randomInt(validObjects.size())).getRandomCoordinates();
   }

   public HashMap<ResourceModule.ResourceType, ArrayList<OrderedPair>> getAnimalResourceWaypointsMap() {
      return this.animalWaypoints;
   }

   public ArrayList<OrderedPair> getAnimalResourceWaypoints(ResourceModule.ResourceType t) {
      return this.animalWaypoints.get(t);
   }
}
