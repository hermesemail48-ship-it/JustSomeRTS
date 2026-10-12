package rtr.gui.states;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.DataModule;
import rtr.ModuleBase;
import rtr.gui.GUIEnums;
import rtr.map.MapModule;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.mobs.jobs.MobJobModule;
import rtr.objects.ObjectBase;
import rtr.objects.ObjectModule;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;
import rtr.road.RoadModule;
import rtr.states.StateBase;

public class PlayStateGUIData {
   protected ObjectModule object = (ObjectModule)StateBase.getModule(ModuleBase.ModuleType.OBJECT);
   protected MobJobModule mobJob = (MobJobModule)StateBase.getModule(ModuleBase.ModuleType.MOB_JOB);
   protected MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   protected DataModule data = (DataModule)StateBase.getModule(ModuleBase.ModuleType.DATA);
   private GUIEnums.InterfaceMode interfaceMode = GUIEnums.InterfaceMode.SELECT;
   private ResourceModule.ResourceType interfaceModeResourceType;
   private RoadModule.RoadType interfaceModeRoadType;
   private MapTilesLoader.TileSet placementTerrainType;
   private RoadModule.RoadType placementRoadType;
   private MapTilesLoader.TileSet placementObjectType;
   private ObjectBase.ObjectSubType placementObjectSubType;
   private ObjectBase.ObjectSubType placementObjectSubTypeDisplayed;
   private MapModule.EraseMode selectedEraseMode;
   private int occupants;
   private int occupantsMax;
   private int rangerLodgeOccupants;
   private int rangerLodgeOccupantsMax;
   private MobBase selectedMob;
   private ObjectBase selectedObject;

   public GUIEnums.InterfaceMode getInterfaceMode() {
      return this.interfaceMode;
   }

   public void setInterfaceMode(GUIEnums.InterfaceMode iM) {
      this.interfaceMode = iM;
      this.interfaceModeResourceType = null;
      this.interfaceModeRoadType = null;
   }

   public void setInterfaceModeResource(GUIEnums.InterfaceMode iM, ResourceModule.ResourceType t) {
      this.interfaceMode = iM;
      this.interfaceModeResourceType = t;
      this.interfaceModeRoadType = null;
   }

   public void setInterfaceModeRoad(GUIEnums.InterfaceMode iM, RoadModule.RoadType t) {
      this.interfaceMode = iM;
      this.interfaceModeResourceType = null;
      this.interfaceModeRoadType = t;
   }

   public ResourceModule.ResourceType getInterfaceModeResourceType() {
      return this.interfaceModeResourceType;
   }

   public RoadModule.RoadType getInterfaceModeRoadType() {
      return this.interfaceModeRoadType;
   }

   public MapTilesLoader.TileSet getPlacementTerrainType() {
      return this.placementTerrainType;
   }

   public void setPlacementTerrainType(MapTilesLoader.TileSet t) throws SlickException {
      this.placementTerrainType = t;
      this.setInterfaceMode(GUIEnums.InterfaceMode.BRUSH);
   }

   public RoadModule.RoadType getPlacementRoadType() {
      return this.placementRoadType;
   }

   public void setPlacementRoadType(RoadModule.RoadType t) throws SlickException {
      this.placementRoadType = t;
      this.setInterfaceMode(GUIEnums.InterfaceMode.PLACE_ROAD);
   }

   public MapTilesLoader.TileSet getPlacementObjectType() {
      return this.placementObjectType;
   }

   public ObjectBase.ObjectSubType getPlacementObjectSubType() {
      return this.placementObjectSubType;
   }

   public ObjectBase.ObjectSubType getPlacementObjectSubTypeDisplayed() {
      return this.placementObjectSubTypeDisplayed;
   }

   public void setPlacementObject(MapTilesLoader.TileSet oT, ObjectBase.ObjectSubType oST, ObjectBase.ObjectSubType oSTD) throws SlickException {
      this.map.getMapTileLoader().checkAndloadTileset(this.map.getMapTileLoader().getTileSetGID(oT));
      this.placementObjectType = oT;
      this.placementObjectSubType = oST;
      this.placementObjectSubTypeDisplayed = oSTD;
      if (this.placementObjectType.getTileSetTemplate().getType() == MapTilesLoader.TileSetType.WALL) {
         this.setInterfaceMode(GUIEnums.InterfaceMode.WALL);
      } else {
         this.setInterfaceMode(GUIEnums.InterfaceMode.OBJECT);
      }
   }

   public MapModule.EraseMode getSelectedEraseMode() {
      return this.selectedEraseMode;
   }

   public void setSelectedEraseMode(MapModule.EraseMode e) {
      this.selectedEraseMode = e;
   }

   public int getOccupants() {
      return this.occupants;
   }

   public void setOccupants(int occupants) {
      this.occupants = occupants;
   }

   public int getOccupantsMax() {
      return this.occupantsMax;
   }

   public void setOccupantsMax(int occupantsMax) {
      this.occupantsMax = occupantsMax;
   }

   public int getRangerLodgeOccupants() {
      return this.rangerLodgeOccupants;
   }

   public void setRangerLodgeOccupants(int rangerLodgeOccupants) {
      this.rangerLodgeOccupants = rangerLodgeOccupants;
   }

   public int getRangerLodgeOccupantsMax() {
      return this.rangerLodgeOccupantsMax;
   }

   public void setRangerLodgeOccupantsMax(int rangerLodgeOccupantsMax) {
      this.rangerLodgeOccupantsMax = rangerLodgeOccupantsMax;
   }

   public MobBase getSelectedMob() {
      return this.selectedMob;
   }

   public void setSelectedMob(MobBase m) {
      this.selectedMob = m;
   }

   public ObjectBase getSelectedObject() {
      return this.selectedObject;
   }

   public void setSelectedObject(ObjectBase o) {
      this.selectedObject = o;
   }

   public void update() {
      ArrayList<ObjectBase> buildingListMaster = this.object.getObjectMasterBuildingPriorityList();
      this.rangerLodgeOccupants = 0;
      this.rangerLodgeOccupantsMax = 0;
      this.occupants = 0;
      this.occupantsMax = 0;

      for (ObjectBase o : buildingListMaster) {
         ObjectFlags objectFlags = o.getObjectFlags();
         if (objectFlags.getOccupantsJob() == MobJobBase.MobJobType.RANGER) {
            this.rangerLodgeOccupants = this.rangerLodgeOccupants + o.getOccupants().size();
            this.rangerLodgeOccupantsMax = this.rangerLodgeOccupantsMax + objectFlags.getOccupantsMax();
         } else if (objectFlags.getOccupantsJob() == null && objectFlags.getOccupantsTypes().size() == 0) {
            this.occupants = this.occupants + o.getOccupants().size();
            this.occupantsMax = this.occupantsMax + objectFlags.getOccupantsMax();
         }
      }

      if (this.interfaceMode == GUIEnums.InterfaceMode.OBJECT
         || this.interfaceMode == GUIEnums.InterfaceMode.ASSIGN_ROAD_WORK
         || this.interfaceMode == GUIEnums.InterfaceMode.ASSIGN_DISMANTLE_ROAD_WORK
         || this.interfaceMode == GUIEnums.InterfaceMode.WALL) {
         this.data.lockDisplayMode(DataModule.DataDisplayMode.RANGE_MAP);
      } else if (this.interfaceMode != GUIEnums.InterfaceMode.ASSIGN_HARVEST_WORK || this.interfaceModeResourceType == null) {
         if (this.data.getLockedDisplayMode() == DataModule.DataDisplayMode.RANGE_MAP
            || this.data.getLockedDisplayMode() == DataModule.DataDisplayMode.RESOURCE_HIGHLIGHT_WOOD
            || this.data.getLockedDisplayMode() == DataModule.DataDisplayMode.RESOURCE_HIGHLIGHT_ROCK
            || this.data.getLockedDisplayMode() == DataModule.DataDisplayMode.RESOURCE_HIGHLIGHT_FOOD_AND_WATER
            || this.data.getLockedDisplayMode() == DataModule.DataDisplayMode.RESOURCE_HIGHLIGHT_CRYSTAL) {
            this.data.unlockDisplayMode();
         }
      } else if (this.interfaceModeResourceType == ResourceModule.ResourceType.WOOD) {
         this.data.lockDisplayMode(DataModule.DataDisplayMode.RESOURCE_HIGHLIGHT_WOOD);
      } else if (this.interfaceModeResourceType == ResourceModule.ResourceType.ROCK) {
         this.data.lockDisplayMode(DataModule.DataDisplayMode.RESOURCE_HIGHLIGHT_ROCK);
      } else if (this.interfaceModeResourceType == ResourceModule.ResourceType.RAW_VEGETABLE) {
         this.data.lockDisplayMode(DataModule.DataDisplayMode.RESOURCE_HIGHLIGHT_FOOD_AND_WATER);
      } else if (this.interfaceModeResourceType == ResourceModule.ResourceType.CRYSTAL) {
         this.data.lockDisplayMode(DataModule.DataDisplayMode.RESOURCE_HIGHLIGHT_CRYSTAL);
      }
   }
}
