package rtr.gui.states;

import org.newdawn.slick.SlickException;
import rtr.DataModule;
import rtr.ModuleBase;
import rtr.gui.GUIEnums;
import rtr.map.MapModule;
import rtr.map.MapTilesLoader;
import rtr.mobs.jobs.MobJobModule;
import rtr.objects.ObjectBase;
import rtr.objects.ObjectModule;
import rtr.resources.ResourceModule;
import rtr.road.RoadModule;
import rtr.states.StateBase;

public class MapEditorGUIData {
   protected ObjectModule object = (ObjectModule)StateBase.getModule(ModuleBase.ModuleType.OBJECT);
   protected MobJobModule mobJob = (MobJobModule)StateBase.getModule(ModuleBase.ModuleType.MOB_JOB);
   protected MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   protected DataModule data = (DataModule)StateBase.getModule(ModuleBase.ModuleType.DATA);
   private GUIEnums.InterfaceMode interfaceMode = GUIEnums.InterfaceMode.SELECT;
   private ResourceModule.ResourceType interfaceModeResourceType;
   private MapTilesLoader.TileSet placementTerrainType;
   private RoadModule.RoadType placementRoadType;
   private MapTilesLoader.TileSet placementObjectType;
   private ObjectBase.ObjectSubType placementObjectSubType;
   private ObjectBase.ObjectSubType placementObjectSubTypeDisplayed;
   private MapModule.EraseMode selectedEraseMode;

   public GUIEnums.InterfaceMode getInterfaceMode() {
      return this.interfaceMode;
   }

   public void setInterfaceMode(GUIEnums.InterfaceMode iM) {
      this.setInterfaceMode(iM, null);
   }

   public void setInterfaceMode(GUIEnums.InterfaceMode iM, ResourceModule.ResourceType t) {
      this.interfaceMode = iM;
      this.interfaceModeResourceType = t;
   }

   public ResourceModule.ResourceType getInterfaceModeResourceType() {
      return this.interfaceModeResourceType;
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
}
