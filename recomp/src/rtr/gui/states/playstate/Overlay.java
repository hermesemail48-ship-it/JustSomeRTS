package rtr.gui.states.playstate;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import r2d.image.SpriteSheet;
import rtr.DataModule;
import rtr.ImageLoader;
import rtr.ModuleBase;
import rtr.SettingsParser;
import rtr.font.FontModule;
import rtr.font.Text;
import rtr.gui.GUIEnums;
import rtr.gui.buttons.GUIToolTipOverlay;
import rtr.gui.states.BrushLoader;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.PlayStateGUIPanelBase;
import rtr.influence.InfluenceModule;
import rtr.influence.SpellBase;
import rtr.map.MapModule;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.MobModule;
import rtr.mobs.jobs.MobJobBase;
import rtr.mobs.jobs.MobJobModule;
import rtr.objects.ObjectBase;
import rtr.objects.ObjectModule;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.road.RoadModule;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.system.ScaleControl;
import rtr.system.gamemodetemplates.GameModeTemplateBase;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class Overlay extends PlayStateGUIPanelBase {
   private MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   private ResourceModule resource = (ResourceModule)StateBase.getModule(ModuleBase.ModuleType.RESOURCE);
   private ObjectModule object = (ObjectModule)StateBase.getModule(ModuleBase.ModuleType.OBJECT);
   private FontModule font = (FontModule)StateBase.getModule(ModuleBase.ModuleType.FONT);
   private MobModule mob = (MobModule)StateBase.getModule(ModuleBase.ModuleType.MOB);
   private MobJobModule mobJob = (MobJobModule)StateBase.getModule(ModuleBase.ModuleType.MOB_JOB);
   private DataModule data = (DataModule)StateBase.getModule(ModuleBase.ModuleType.DATA);
   private RoadModule road = (RoadModule)StateBase.getModule(ModuleBase.ModuleType.ROAD);
   private InfluenceModule influence = (InfluenceModule)StateBase.getModule(ModuleBase.ModuleType.INFLUENCE);
   private Image mouseLock;
   private SpriteSheet selectionEdges;
   private SpriteSheet brushDots;
   private Image brushDot;
   private Image brushDotAccent;
   private Image selectionTopLeft;
   private Image selectionTopRight;
   private Image selectionBottomLeft;
   private Image selectionBottomRight;
   private Image roadPlacementSquare;
   private GUIToolTipOverlay toolTip;
   private int toolTipTicker;
   private float mouseX;
   private float mouseY;
   private float mouseNewX;
   private float mouseNewY;
   private Color selectionColor = Color.white;
   private Image mobSelect = ImageLoader.getImage("res/GUI/mobSelect.png");
   private Image iconPregnant;
   private Image iconMate;
   private Image iconHome;
   private Image iconWorkers;
   private Image iconLevel;
   private Image iconAge;
   private Image iconHealth;

   public Overlay(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.roadPlacementSquare = ImageLoader.getImage("res/GUI/mobSelect.png");
      this.iconPregnant = ImageLoader.getImage("res/GUI/icon/iconPregnant.png");
      this.iconMate = ImageLoader.getImage("res/GUI/icon/iconMate.png");
      this.iconHome = ImageLoader.getImage("res/GUI/icon/iconHome.png");
      this.iconWorkers = ImageLoader.getImage("res/GUI/icon/iconWorkers.png");
      this.iconLevel = ImageLoader.getImage("res/GUI/icon/iconLevel.png");
      this.iconAge = ImageLoader.getImage("res/GUI/icon/iconAge.png");
      this.iconHealth = ImageLoader.getImage("res/GUI/icon/iconHitPoints.png");
      this.mouseLock = ImageLoader.getImage("res/GUI/mouseLock.png");
      this.brushDots = new SpriteSheet(ImageLoader.getImage("res/GUI/brushDots.png"), 16, 16);
      this.brushDot = this.brushDots.getSprite(0, 0);
      this.brushDotAccent = this.brushDots.getSprite(1, 0);
      this.selectionEdges = new SpriteSheet(ImageLoader.getImage("res/GUI/selectionEdges.png"), 16, 16);
      this.selectionTopRight = this.selectionEdges.getSprite(1, 0);
      this.selectionTopLeft = this.selectionEdges.getSprite(0, 0);
      this.selectionBottomRight = this.selectionEdges.getSprite(1, 1);
      this.selectionBottomLeft = this.selectionEdges.getSprite(0, 1);
      this.toolTip = new GUIToolTipOverlay();
   }

   public void render(boolean debug) throws SlickException {
      int tileX = Game.getCS().getMouseTileX();
      int tileY = Game.getCS().getMouseTileY();
      float mouseXPressed = Game.getCS().getMouseXPressed();
      float mouseYPressed = Game.getCS().getMouseYPressed();
      GUIEnums.InterfaceMode interfaceMode = this.controller.getGUIData().getInterfaceMode();
      MobBase mouseOverMob = this.mob.getMobAt(this.mouse, false);
      ObjectBase mouseOverObject = this.object.getObjectAt(this.mouse);
      MobBase selectedMob = this.controller.getGUIData().getSelectedMob();
      ObjectBase selectedObject = this.controller.getGUIData().getSelectedObject();
      if (!this.object.objectExists(selectedObject)) {
         selectedObject = null;
      }

      if (selectedObject != null && !selectedObject.getObjectFlags().canNotSelect()) {
         this.displayObjectOverlay(selectedObject, this.g, true, debug);
         if (mouseOverObject != null) {
            this.displayResourceGrabBar(mouseOverObject, this.g, debug);
         }
      }

      if (!this.mob.getMobArray().contains(selectedMob)) {
         selectedMob = null;
      }

      if (selectedMob != null && !selectedMob.getNoSelect() && selectedMob.isAvailable()) {
         this.displayMobOverlay(selectedMob, this.g, debug);
      }

      this.g.resetTransform();
      this.g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
      if (mouseOverMob != null && !mouseOverMob.getNoSelect() && mouseOverMob != selectedMob) {
         this.displayMobOverlay(mouseOverMob, this.g, debug);
         this.g.resetTransform();
         this.g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
      }

      if (mouseOverObject != null
         && mouseOverObject.getObjectFlags().getBaseType().getTileSetTemplate().getType() != MapTilesLoader.TileSetType.WALL
         && !mouseOverObject.getObjectFlags().canNotSelect()
         && mouseOverObject != selectedObject) {
         this.displayObjectOverlay(mouseOverObject, this.g, false, debug);
         this.displayResourceGrabBar(mouseOverObject, this.g, debug);
         this.g.resetTransform();
         this.g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
      }

      BrushLoader brushLoader = this.controller.getBrushLoader();
      if (interfaceMode == GUIEnums.InterfaceMode.BRUSH
         || interfaceMode == GUIEnums.InterfaceMode.ERASE
         || interfaceMode == GUIEnums.InterfaceMode.PATCH
         || interfaceMode == GUIEnums.InterfaceMode.HOLE) {
         int brushSize = brushLoader.getBrushSize();
         BrushLoader.BrushType brushType = brushLoader.getBrushType();
         this.brushDot.startUse();
         if (this.map.getAccentMode() && interfaceMode == GUIEnums.InterfaceMode.BRUSH) {
            byte[][] currentAccentBrush = brushLoader.getAccentBrush(brushSize, brushType);

            for (int x = 0; x < currentAccentBrush.length; x++) {
               for (int y = 0; y < currentAccentBrush[x].length; y++) {
                  if (currentAccentBrush[x][y] == 2) {
                     int brushX = (int)(x * 16 + tileX * 16 - currentAccentBrush.length / 2 * 16 + this.map.getMapX());
                     int brushY = (int)(y * 16 + tileY * 16 - currentAccentBrush[x].length / 2 * 16 + this.map.getMapY());
                     this.brushDotAccent.drawEmbedded(brushX, brushY);
                  }
               }
            }
         }

         byte[][] currentBrush = brushLoader.getBrush(brushSize, brushType);

         for (int x = 0; x < currentBrush.length; x++) {
            for (int yx = 0; yx < currentBrush[x].length; yx++) {
               if (currentBrush[x][yx] == 2) {
                  int brushX = (int)(x * 16 + tileX * 16 - currentBrush.length / 2 * 16 + this.map.getMapX());
                  int brushY = (int)(yx * 16 + tileY * 16 - currentBrush[x].length / 2 * 16 + this.map.getMapY());
                  this.brushDot.drawEmbedded(brushX, brushY);
               }
            }
         }

         this.brushDot.endUse();
      }

      if (interfaceMode == GUIEnums.InterfaceMode.ASSIGN_HARVEST_WORK
         || interfaceMode == GUIEnums.InterfaceMode.ASSIGN_CLEAR_WORK
         || interfaceMode == GUIEnums.InterfaceMode.ASSIGN_DIG_WORK) {
         int brushSize = brushLoader.getBrushSize();
         BrushLoader.BrushType brushType = brushLoader.getBrushType();
         this.brushDot.startUse();
         byte[][] currentBrush = brushLoader.getBrush(brushSize, brushType);

         for (int x = 0; x < currentBrush.length; x++) {
            for (int yxx = 0; yxx < currentBrush[x].length; yxx++) {
               if (currentBrush[x][yxx] == 2) {
                  int brushX = (int)(x * 16 + tileX * 16 - currentBrush.length / 2 * 16 + this.map.getMapX());
                  int brushY = (int)(yxx * 16 + tileY * 16 - currentBrush[x].length / 2 * 16 + this.map.getMapY());
                  this.brushDot.drawEmbedded(brushX, brushY);
               }
            }
         }

         this.brushDot.endUse();
      }

      MapTilesLoader.TileSet selectedObjectType = this.controller.getGUIData().getPlacementObjectType();
      ObjectBase.ObjectSubType objectSubTypeDisplayed = this.controller.getGUIData().getPlacementObjectSubTypeDisplayed();
      if (interfaceMode == GUIEnums.InterfaceMode.OBJECT) {
         if ((selectedObjectType != MapTilesLoader.TileSet.ANCILLARY || !this.object.canBuildNewAncillary())
            && (selectedObjectType == MapTilesLoader.TileSet.ANCILLARY || !this.object.canBuildNewBuilding())
            && selectedObjectType != MapTilesLoader.TileSet.CASTLE_1) {
            this.g.resetTransform();
            this.g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
            float x = Game.getCS().getMouseX();
            float yxxx = Game.getCS().getMouseY();
            ObjectBase castle = this.object.getFirstBaseTypeFound(MapTilesLoader.TileSet.CASTLE_1);
            if (selectedObjectType == MapTilesLoader.TileSet.ANCILLARY) {
               if (castle == null) {
                  this.font.drawString(x, yxxx - 60.0F, Text.getText("playOverlay.slots.outOfAncillarySlotsNoCamp"), Text.FontType.BODY, 2, true);
               } else if (castle.getObjectFlags().canUpgrade()) {
                  Text.setVariableText("castleName", castle.getObjectFlags().getName());
                  this.font.drawString(x, yxxx - 60.0F, Text.getText("playOverlay.slots.outOfAncillarySlotsUpgrade"), Text.FontType.BODY, 2, true);
               } else {
                  this.font.drawString(x, yxxx - 60.0F, Text.getText("playOverlay.slots.outOfAncillarySlots"), Text.FontType.BODY, 2, true);
               }
            } else if (castle == null) {
               this.font.drawString(x, yxxx - 60.0F, Text.getText("playOverlay.slots.outOfBuildingSlotsNoCamp"), Text.FontType.BODY, 2, true);
            } else if (castle.getObjectFlags().canUpgrade()) {
               Text.setVariableText("castleName", castle.getObjectFlags().getName());
               this.font.drawString(x, yxxx - 60.0F, Text.getText("playOverlay.slots.outOfBuildingSlotsUpgrade"), Text.FontType.BODY, 2, true);
            } else {
               this.font.drawString(x, yxxx - 60.0F, Text.getText("playOverlay.slots.outOfBuildingSlots"), Text.FontType.BODY, 2, true);
            }

            this.g.resetTransform();
            this.g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
         } else {
            this.data.renderObjectOutline(tileX, tileY, this.g, selectedObjectType, objectSubTypeDisplayed, false);
         }
      }

      if (interfaceMode == GUIEnums.InterfaceMode.WALL) {
         this.data.renderWallOutline(tileX, tileY, this.g, selectedObjectType, true);
      }

      if (interfaceMode == GUIEnums.InterfaceMode.INFLUENCE) {
         SpellBase spell = this.influence.getSpellFactory().getSpellInstance(this.influence.getSelectedSpell());
         ArrayList<MobBase.MobType> spellMobTypesList = spell.getMobTypesList();
         if (spellMobTypesList != null && spellMobTypesList.size() > 0) {
            ArrayList<MobBase> spellMobsList = new ArrayList<>();

            for (MobBase.MobType t : spellMobTypesList) {
               spellMobsList.addAll(this.mob.getMobsByType(t, false));
            }

            if (spellMobsList.size() > 0) {
               this.renderMobLinesSet(spellMobsList, this.map.getMapX() + Game.getCS().getMouseOnMapX(), this.map.getMapY() + Game.getCS().getMouseOnMapY());
            }
         }

         if (this.influence.getSelectedSpell() == SpellBase.SpellType.DISPEL_GOD_STRUCTURE) {
            ArrayList<ObjectBase> buildings = this.object.getObjectsArrayList();
            ArrayList<ObjectBase> godBuildings = new ArrayList<>();

            for (ObjectBase o : buildings) {
               if (o.getObjectFlags().getInfluenceMaintainCost() > 0) {
                  godBuildings.add(o);
               }
            }

            this.renderObjectLinesSet(godBuildings, this.map.getMapX() + Game.getCS().getMouseOnMapX(), this.map.getMapY() + Game.getCS().getMouseOnMapY());
         }

         MapTilesLoader.TileSet spellObject = spell.getPlacementObject();
         if (spellObject != null) {
            if (spellObject.getTileSetTemplate().getType() == MapTilesLoader.TileSetType.WALL) {
               this.data.renderWallOutline(tileX, tileY, this.g, spellObject, true);
            } else {
               this.data.renderObjectOutline(tileX, tileY, this.g, spellObject, ObjectBase.ObjectSubType.BUILT, false);
            }
         }

         if (spell.getRadius() > 0) {
            this.data.renderGenericMap(tileX, tileY, Utilities.createRangeMap(spell.getRadius()), new Color(0, 200, 200, 50), this.g);
         }

         if (!spell.hideCooldownWheel() && this.influence.getSpellCooldown(this.influence.getSelectedSpell()) > 0) {
            this.g.resetTransform();
            this.g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
            int cooldownLeft = (int)Math.ceil((float)this.influence.getSpellCooldown(this.influence.getSelectedSpell()) / spell.getCooldown() * 360.0F);
            this.g.setColor(Color.black);
            this.g.fillOval(this.mouse.getX() + 22.0F, this.mouse.getY() + 8.0F, 20.0F, 20.0F);
            if (cooldownLeft < 90) {
               this.g.setColor(Color.green);
            } else if (cooldownLeft < 180) {
               this.g.setColor(Color.yellow);
            } else if (cooldownLeft < 240) {
               this.g.setColor(Color.orange);
            } else {
               this.g.setColor(Color.red);
            }

            this.g.fillArc(this.mouse.getX() + 23.0F, this.mouse.getY() + 9.0F, 18.0F, 18.0F, -90.0F, -cooldownLeft - 90);
            this.g.resetTransform();
            this.g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
         }
      }

      if (interfaceMode == GUIEnums.InterfaceMode.PLACE_ROAD) {
         this.roadPlacementSquare.draw(this.map.getMapX() + tileX * 16, this.map.getMapY() + tileY * 16);
      }

      if (interfaceMode == GUIEnums.InterfaceMode.SPAWN_MOB || interfaceMode == GUIEnums.InterfaceMode.SPAWN_RESOURCE) {
         this.mobSelect.draw(this.map.getMapX() + tileX * 16, this.map.getMapY() + tileY * 16, this.selectionColor);
      }

      if (interfaceMode == GUIEnums.InterfaceMode.ASSIGN_ROAD_WORK || interfaceMode == GUIEnums.InterfaceMode.ASSIGN_DISMANTLE_ROAD_WORK) {
         float x = this.map.getMapX() + tileX * 16;
         float yxxx = this.map.getMapY() + tileY * 16;
         if (!Utilities.isOutOfBoundsOfUsableMap(tileX, tileY)) {
            if (StateBase.getInput().isKeyDown(SettingsParser.getKeyAccentModeToggle())) {
               for (OrderedPair ox : this.road.getConnectedRoadCoordinates(tileX, tileY)) {
                  if (this.object.isInRange(ox.getX(), ox.getY())) {
                     float thisX = this.map.getMapX() + ox.getX() * 16;
                     float thisY = this.map.getMapY() + ox.getY() * 16;
                     this.roadPlacementSquare.draw(thisX, thisY);
                  }
               }
            } else {
               this.roadPlacementSquare.draw(x, yxxx);
            }
         }
      }

      this.g.resetTransform();
      this.g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
      if (Game.getCS().isMapMoving()) {
         this.mouseLock.drawCentered(mouseXPressed, mouseYPressed);
      }
   }

   public void renderToolTip(boolean debug) throws SlickException {
      if (!this.controller.intersectsAnyGUIMask() && this.help.getIntersectingTip(Game.getCS().getMouseRectangle()) == null) {
         this.g.resetTransform();
         this.g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
         int tileX = Game.getCS().getMouseTileX();
         int tileY = Game.getCS().getMouseTileY();
         this.mouseNewX = this.mouse.getX();
         this.mouseNewY = this.mouse.getY();
         if (this.mouseNewX == this.mouseX && this.mouseNewY == this.mouseY && this.toolTipTicker < 30) {
            this.toolTipTicker++;
         } else if (this.toolTipTicker > 0) {
            this.toolTipTicker--;
         }

         GUIEnums.InterfaceMode interfaceMode = this.controller.getGUIData().getInterfaceMode();
         GameModeTemplateBase.GameMode gameMode = Game.getCS().getGameMode();
         if (!this.controller.isHideGUI()) {
            boolean otherTooltipsShown = false;
            float scale = ScaleControl.getInterfaceScale();
            String accentKey = SettingsParser.getKeyAccentModeToggleName().toUpperCase();
            String unassignKey = SettingsParser.getKeyUnassignName().toUpperCase();
            String cancelKey = SettingsParser.getKeyCancelName().toUpperCase();
            if (interfaceMode != GUIEnums.InterfaceMode.SELECT) {
               Text.setVariableText("shortcutKey", cancelKey);
               this.font
                  .drawString(ScaleControl.getInterfaceWidth() / 2, 180.0F, Text.getText("playOverlay.general.cancelAction"), Text.FontType.BODY, 2, true);
            }

            if (!Utilities.isOutOfBoundsOfUsableMap(tileX, tileY)
               && (
                  interfaceMode == GUIEnums.InterfaceMode.ASSIGN_HARVEST_WORK
                     || interfaceMode == GUIEnums.InterfaceMode.ASSIGN_CLEAR_WORK
                     || interfaceMode == GUIEnums.InterfaceMode.ASSIGN_ROAD_WORK
                     || interfaceMode == GUIEnums.InterfaceMode.ASSIGN_DISMANTLE_ROAD_WORK
               )) {
               Text.setVariableText("shortcutKey", unassignKey);
               this.toolTip
                  .renderToolTip(
                     this.g,
                     this.mouse,
                     GUIToolTipOverlay.ToolTipPosition.RIGHT,
                     0,
                     -13 * (int)scale,
                     Text.getText("playOverlay.general.unassignWork"),
                     null,
                     debug
                  );
               if (interfaceMode == GUIEnums.InterfaceMode.ASSIGN_ROAD_WORK || interfaceMode == GUIEnums.InterfaceMode.ASSIGN_DISMANTLE_ROAD_WORK) {
                  Text.setVariableText("shortcutKey", accentKey);
                  this.toolTip
                     .renderToolTip(
                        this.g,
                        this.mouse,
                        GUIToolTipOverlay.ToolTipPosition.RIGHT,
                        0,
                        0 * (int)scale,
                        Text.getText("playOverlay.general.selectAllConnected"),
                        null,
                        debug
                     );
                  if (StateBase.getInput().isKeyDown(SettingsParser.getKeyAccentModeToggle())) {
                     ArrayList<OrderedPair> allConnectedRoadsTest = this.road.getConnectedRoadCoordinates(tileX, tileY);
                     if (interfaceMode == GUIEnums.InterfaceMode.ASSIGN_ROAD_WORK) {
                        RoadModule.RoadType roadType = this.controller.getGUIData().getInterfaceModeRoadType();
                        this.map.getMapTileLoader().checkAndloadTileset(roadType.getTemplate().getRoadTileSet().getTileSetTemplate().getTileGID());
                        String nextRoadName = roadType.getTemplate().getRoadTileSet().getTileSetTemplate().getName();
                        ArrayList<ResourceModule.ResourceType> allRequiredResources = this.road.getTotalRequiredResource(tileX, tileY, roadType);
                        if (allConnectedRoadsTest.size() == 0) {
                           this.toolTip
                              .renderToolTip(
                                 this.g,
                                 this.mouse,
                                 GUIToolTipOverlay.ToolTipPosition.RIGHT,
                                 0,
                                 12 * (int)scale,
                                 Text.getText("playOverlay.roads.noRoadSelected"),
                                 null,
                                 debug
                              );
                           otherTooltipsShown = true;
                        } else if (allRequiredResources.size() == 0) {
                           if (roadType.getTemplate().getNextRoadType() == null) {
                              this.toolTip
                                 .renderToolTip(
                                    this.g,
                                    this.mouse,
                                    GUIToolTipOverlay.ToolTipPosition.RIGHT,
                                    0,
                                    12 * (int)scale,
                                    Text.getText("playOverlay.roads.canNotImprove"),
                                    null,
                                    debug
                                 );
                           } else if (roadType.getTemplate().getRoadLevel() <= this.road.getRoadType(tileX, tileY).getTemplate().getRoadLevel()) {
                              this.toolTip
                                 .renderToolTip(
                                    this.g,
                                    this.mouse,
                                    GUIToolTipOverlay.ToolTipPosition.RIGHT,
                                    0,
                                    12 * (int)scale,
                                    Text.getText("playOverlay.roads.sameOrBetter"),
                                    null,
                                    debug
                                 );
                           } else {
                              this.toolTip
                                 .renderToolTip(
                                    this.g,
                                    this.mouse,
                                    GUIToolTipOverlay.ToolTipPosition.RIGHT,
                                    0,
                                    12 * (int)scale,
                                    nextRoadName,
                                    Text.getText("playOverlay.roads.roadCostFree"),
                                    debug
                                 );
                           }

                           otherTooltipsShown = true;
                        } else {
                           String resourcesList = "";

                           for (int i = 0; i < allRequiredResources.size(); i++) {
                              resourcesList = resourcesList + allRequiredResources.get(i).getTemplate().getName();
                              if (i != allRequiredResources.size() - 1) {
                                 resourcesList = resourcesList + ", ";
                              }
                           }

                           int amount = 0;

                           for (OrderedPair o : allConnectedRoadsTest) {
                              if (this.object.isInRange(o.getX(), o.getY())) {
                                 amount++;
                              }
                           }

                           Text.setVariableText("amount", amount);
                           Text.setVariableText("resourceName", resourcesList);
                           this.toolTip
                              .renderToolTip(
                                 this.g,
                                 this.mouse,
                                 GUIToolTipOverlay.ToolTipPosition.RIGHT,
                                 0,
                                 12 * (int)scale,
                                 nextRoadName,
                                 Text.getText("playOverlay.roads.roadCost"),
                                 debug
                              );
                           otherTooltipsShown = true;
                        }
                     }
                  } else if (interfaceMode == GUIEnums.InterfaceMode.ASSIGN_ROAD_WORK) {
                     RoadModule.RoadType roadType = this.controller.getGUIData().getInterfaceModeRoadType();
                     this.map.getMapTileLoader().checkAndloadTileset(roadType.getTemplate().getRoadTileSet().getTileSetTemplate().getTileGID());
                     ArrayList<ResourceModule.ResourceType> allRequiredResources = this.road.getTotalRequiredResource(tileX, tileY, roadType);
                     String nextRoadName = roadType.getTemplate().getRoadTileSet().getTileSetTemplate().getName();
                     if (allRequiredResources.size() == 0) {
                        if (nextRoadName == null) {
                           this.toolTip
                              .renderToolTip(
                                 this.g,
                                 this.mouse,
                                 GUIToolTipOverlay.ToolTipPosition.RIGHT,
                                 0,
                                 12 * (int)scale,
                                 Text.getText("playOverlay.roads.canNotImprove"),
                                 null,
                                 debug
                              );
                        } else if (roadType.getTemplate().getRoadLevel() <= this.road.getRoadType(tileX, tileY).getTemplate().getRoadLevel()) {
                           this.toolTip
                              .renderToolTip(
                                 this.g,
                                 this.mouse,
                                 GUIToolTipOverlay.ToolTipPosition.RIGHT,
                                 0,
                                 12 * (int)scale,
                                 Text.getText("playOverlay.roads.sameOrBetter"),
                                 null,
                                 debug
                              );
                        } else {
                           this.toolTip
                              .renderToolTip(
                                 this.g,
                                 this.mouse,
                                 GUIToolTipOverlay.ToolTipPosition.RIGHT,
                                 0,
                                 12 * (int)scale,
                                 nextRoadName,
                                 Text.getText("playOverlay.roads.roadCostFree"),
                                 debug
                              );
                        }

                        otherTooltipsShown = true;
                     } else {
                        String resourcesList = "";

                        for (int ix = 0; ix < allRequiredResources.size(); ix++) {
                           resourcesList = resourcesList + allRequiredResources.get(ix).getTemplate().getName();
                           if (ix != allRequiredResources.size() - 1) {
                              resourcesList = resourcesList + ", ";
                           }
                        }

                        Text.setVariableText("amount", "1");
                        Text.setVariableText("resourceName", resourcesList);
                        this.toolTip
                           .renderToolTip(
                              this.g,
                              this.mouse,
                              GUIToolTipOverlay.ToolTipPosition.RIGHT,
                              0,
                              12 * (int)scale,
                              nextRoadName,
                              Text.getText("playOverlay.roads.roadCost"),
                              debug
                           );
                        otherTooltipsShown = true;
                     }
                  }
               }
            }

            if (!Utilities.isOutOfBoundsOfUsableMap(tileX, tileY) && this.toolTipTicker > 25 && !otherTooltipsShown) {
               ResourceModule.ResourceType r = this.resource.getResourceTypeOnGround(tileX, tileY);
               if (r != null) {
                  this.toolTip
                     .renderToolTip(
                        this.g, this.mouse, GUIToolTipOverlay.ToolTipPosition.RIGHT, r.getTemplate().getName(), r.getTemplate().getDescription(), debug
                     );
               } else {
                  int tileID = 0;

                  for (int l = 11; l >= 0; l--) {
                     tileID = this.map.getTileId(tileX, tileY, l);
                     if (tileID != 0) {
                        break;
                     }
                  }

                  if (tileID != 0) {
                     this.toolTip
                        .renderToolTip(
                           this.g,
                           this.mouse,
                           GUIToolTipOverlay.ToolTipPosition.RIGHT,
                           this.map.getMapTileLoader().getTileName(tileID),
                           this.map.getMapTileLoader().getTileDescription(tileID),
                           debug
                        );
                  }
               }
            }

            if (gameMode == GameModeTemplateBase.GameMode.SANDBOX) {
               if (interfaceMode == GUIEnums.InterfaceMode.DISMANTLE) {
                  Text.setVariableText("shortcutKey", accentKey);
                  this.toolTip
                     .renderToolTip(
                        this.g,
                        this.mouse,
                        GUIToolTipOverlay.ToolTipPosition.RIGHT,
                        0,
                        -13 * (int)scale,
                        Text.getText("playOverlay.general.deleteObjects"),
                        null,
                        debug
                     );
               }

               if (interfaceMode == GUIEnums.InterfaceMode.INFLUENCE) {
                  Text.setVariableText("shortcutKey", accentKey);
                  this.toolTip
                     .renderToolTip(
                        this.g,
                        this.mouse,
                        GUIToolTipOverlay.ToolTipPosition.RIGHT,
                        0,
                        -13 * (int)scale,
                        Text.getText("playOverlay.general.castFree"),
                        null,
                        debug
                     );
               }
            }
         }

         this.mouseX = this.mouse.getX();
         this.mouseY = this.mouse.getY();
         this.g.resetTransform();
         this.g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
      }
   }

   private void displayMobOverlay(MobBase m, Graphics g, boolean debug) throws SlickException {
      g.resetTransform();
      g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
      this.mob.renderMobPath(m);
      if (m.getMobGroup() == MobBase.MobGroup.VILLAGER) {
         this.selectionColor = Color.green;
      } else if (m.getMobGroup() == MobBase.MobGroup.MONSTER) {
         this.selectionColor = Color.red;
      } else if (m.getMobGroup() == MobBase.MobGroup.GOLEM
         || m.getMobGroup() == MobBase.MobGroup.ANIMAL
         || m.getMobGroup() == MobBase.MobGroup.DOMESTICATED_ANIMAL) {
         this.selectionColor = Color.yellow;
      } else if (m.getMobGroup() == MobBase.MobGroup.CHARMED_MONSTER) {
         this.selectionColor = Color.orange;
      } else {
         this.selectionColor = Color.white;
      }

      this.mobSelect.draw(m.getMobX() + this.map.getMapX(), m.getMobY() + this.map.getMapY(), this.selectionColor);
      m.render(g, true);
      g.resetTransform();
      g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
      int nameLength = Text.getWidth(m.getFormattedName(false), Text.FontType.BODY, 1);
      int lengthMinimum = 70;
      if (m.getAssignedHome() != -1) {
         lengthMinimum += 20;
      }

      if (m.getMate() != null) {
         lengthMinimum += 20;
      }

      if (m.isPregnant()) {
         lengthMinimum += 20;
      }

      while (nameLength < lengthMinimum) {
         nameLength += 10;
      }

      int HPLength = Text.getWidth(m.getHitPoints() + "/" + m.getHitPointsMax(), Text.FontType.BODY, 1);
      float positionX = (m.getMobX() + this.map.getMapX()) * ScaleControl.getScalingDifference() + 2.0F;
      float positionY = (m.getMobY() + this.map.getMapY()) * ScaleControl.getScalingDifference() - 60.0F;
      g.setColor(new Color(0, 0, 0, 200));
      if (nameLength > HPLength + 20) {
         g.fillRect(positionX - 4.0F, positionY - 1.0F, nameLength + 8, 51.0F);
      } else {
         g.fillRect(positionX + nameLength - HPLength - 22.0F, positionY, HPLength + 28, 51.0F);
      }

      this.iconLevel.draw(positionX - 4.0F, positionY);
      this.font.drawString(positionX + 14.0F, positionY, String.valueOf(m.getLevel()), Text.FontType.BODY, 1);
      this.iconAge.draw(positionX + 28.0F, positionY);
      this.font.drawString(positionX + 46.0F, positionY, String.valueOf(m.getAge()), Text.FontType.BODY, 1);
      int iconPositionX = -16;
      if (m.getAssignedHome() != -1) {
         this.iconHome.draw(positionX + nameLength + iconPositionX, positionY);
         iconPositionX -= 16;
      }

      if (m.getMate() != null) {
         this.iconMate.draw(positionX + nameLength + iconPositionX, positionY);
         iconPositionX -= 16;
      }

      if (m.isPregnant()) {
         this.iconPregnant.draw(positionX + nameLength + iconPositionX, positionY);
         iconPositionX -= 16;
      }

      int xpLeft = (int)Math.ceil((float)m.getExperience() / (m.getMobFlags().getExperienceMultiplier() * m.getLevel()) * nameLength);
      g.setColor(new Color(50, 50, 50, 255));
      g.fillRect(positionX - 1.0F, positionY + 16.0F, nameLength + 2, 5.0F);
      g.setColor(new Color(0, 200, 0));
      g.fillRect(positionX, positionY + 17.0F, xpLeft, 2.0F);
      this.font.drawString(positionX, positionY + 20.0F, m.getFormattedName(false), Text.FontType.BODY, 1);
      this.iconHealth.draw(positionX + nameLength - HPLength - 20.0F, positionY + 33.0F);
      this.font.drawString(positionX + nameLength - HPLength, positionY + 33.0F, m.getHitPoints() + "/" + m.getHitPointsMax(), Text.FontType.BODY, 1);
   }

   private void displayObjectOverlay(ObjectBase o, Graphics g, boolean mobPositionData, boolean debug) throws SlickException {
      g.resetTransform();
      g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
      ObjectFlags objectFlags = o.getObjectFlags();
      float topRightX = (o.getObjectX() + objectFlags.getWidth()) * 16 + this.map.getMapX() - 16.0F;
      float topRightY = o.getObjectY() * 16 + this.map.getMapY();
      float topLeftX = o.getObjectX() * 16 + this.map.getMapX();
      float topLeftY = o.getObjectY() * 16 + this.map.getMapY();
      float bottomRightX = (o.getObjectX() + objectFlags.getWidth()) * 16 + this.map.getMapX() - 16.0F;
      float bottomRightY = (o.getObjectY() + objectFlags.getHeight()) * 16 + this.map.getMapY() - 16.0F;
      float bottomLeftX = o.getObjectX() * 16 + this.map.getMapX();
      float bottomLeftY = (o.getObjectY() + objectFlags.getHeight()) * 16 + this.map.getMapY() - 16.0F;
      this.selectionTopRight.draw(topRightX, topRightY);
      this.selectionTopLeft.draw(topLeftX, topLeftY);
      this.selectionBottomRight.draw(bottomRightX, bottomRightY);
      this.selectionBottomLeft.draw(bottomLeftX, bottomLeftY);
      if (o.getTower() != null) {
         this.data
            .renderGenericMap(
               o.getTower().getX(), o.getTower().getY(), Utilities.createRangeMap(o.getTower().getTowerFlags().getDistance()), new Color(200, 0, 0, 50), g
            );
      }

      if (objectFlags.getLightningRodRange() > 0) {
         this.data
            .renderGenericMap(
               o.getLightningRodXOnMap(),
               o.getLightningRodYOnMap(),
               Utilities.createRangeMap(objectFlags.getLightningRodRange()),
               new Color(200, 200, 0, 50),
               g
            );
      }

      g.resetTransform();
      g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
      int nameLength = Text.getWidth(objectFlags.getName(), Text.FontType.BODY, 1);
      int HPLength = Text.getWidth(o.getHitPoints() + "/" + o.getHitPointsMax(), Text.FontType.BODY, 1) + this.iconHealth.getWidth();
      float positionX = (o.getObjectX() * 16 + this.map.getMapX()) * ScaleControl.getScalingDifference();
      float positionY = (o.getObjectY() * 16 + this.map.getMapY()) * ScaleControl.getScalingDifference() - 42.0F;
      g.setColor(new Color(0, 0, 0, 200));
      if (objectFlags.getOccupantsMax() > 0) {
         positionY -= 24.0F;
         if (nameLength > HPLength) {
            g.fillRect(positionX - 2.0F, positionY, nameLength + 5, 50.0F);
         } else {
            g.fillRect(positionX - 2.0F, positionY, HPLength + 8, 50.0F);
         }
      } else if (objectFlags.getWorkersProvided() > 0) {
         positionY -= 24.0F;
         if (nameLength > HPLength) {
            g.fillRect(positionX - 2.0F, positionY, nameLength + 5, 66.0F);
         } else {
            g.fillRect(positionX - 2.0F, positionY, HPLength + 8, 66.0F);
         }
      } else if (nameLength > HPLength) {
         g.fillRect(positionX - 2.0F, positionY, nameLength + 5, 34.0F);
      } else {
         g.fillRect(positionX - 2.0F, positionY, HPLength + 8, 34.0F);
      }

      this.font.drawString(positionX, positionY, objectFlags.getName(), Text.FontType.BODY, 1);
      this.iconHealth.draw(positionX, positionY + 16.0F);
      this.font.drawString(positionX + 20.0F, positionY + 16.0F, o.getHitPoints() + "/" + o.getHitPointsMax(), Text.FontType.BODY, 1);
      if (mobPositionData) {
         ArrayList<MobBase> occupants = new ArrayList<>();
         occupants.addAll(o.getOccupants());
         if (objectFlags.getWorkerJobType() != MobJobBase.MobJobType.NONE) {
            occupants.addAll(this.mob.getMobsByJob(objectFlags.getWorkerJobType(), false));
         }

         if (o.getCombobulatorSpawns().size() > 0) {
            occupants.addAll(o.getCombobulatorSpawns());
         }

         if (objectFlags.getSubType() == ObjectBase.ObjectSubType.CONSTRUCTION) {
            ArrayList<MobBase> workers = this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false);
            workers.addAll(this.mob.getMobsByGroup(MobBase.MobGroup.DOMESTICATED_ANIMAL, false));

            for (MobBase m : workers) {
               if (m.getAssignment() != null && (m.getAssignment().getPrimaryObject() == o || m.getAssignment().getSecondaryObject() == o)) {
                  occupants.add(m);
               }
            }
         }

         g.resetTransform();
         g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());

         for (MobBase mx : occupants) {
            this.mob.renderMobPath(mx);
         }

         for (MobBase mx : occupants) {
            if (mx.isAvailable()) {
               if (mx.getMobGroup() == MobBase.MobGroup.VILLAGER) {
                  this.selectionColor = Color.green;
               } else if (mx.getMobGroup() == MobBase.MobGroup.MONSTER) {
                  this.selectionColor = Color.red;
               } else if (mx.getMobGroup() == MobBase.MobGroup.GOLEM
                  || mx.getMobGroup() == MobBase.MobGroup.ANIMAL
                  || mx.getMobGroup() == MobBase.MobGroup.DOMESTICATED_ANIMAL) {
                  this.selectionColor = Color.yellow;
               } else if (mx.getMobGroup() == MobBase.MobGroup.CHARMED_MONSTER) {
                  this.selectionColor = Color.orange;
               } else {
                  this.selectionColor = Color.white;
               }

               this.mobSelect.draw(mx.getMobX() + this.map.getMapX(), mx.getMobY() + this.map.getMapY(), this.selectionColor);
               mx.render(g, true);
            }
         }

         this.renderMobLinesSet(occupants, this.map.getMapX() + o.getCenterPixelX(), this.map.getMapY() + o.getCenterPixelY());
      }

      if (objectFlags.getOccupantsMax() > 0) {
         this.iconHome.draw(positionX, positionY + 32.0F);
         this.font.drawString(positionX + 20.0F, positionY + 32.0F, o.getOccupants().size() + "/" + objectFlags.getOccupantsMax(), Text.FontType.BODY, 1);
      } else if (objectFlags.getWorkersProvided() > 0) {
         MobJobBase.MobJobType t = objectFlags.getWorkerJobType();
         int desiredWorkers = this.mobJob.getDesiredWorkers(t);
         int maximumWorkers = this.mobJob.getMaximumWorkers(t);
         int currentWorkers = this.mobJob.getCurrentWorkers(t);
         int currentWorkersEnroute = this.mobJob.getCurrentWorkers(t, true) - currentWorkers;
         String currentWorkersColor = "$GRE1";
         if (currentWorkers < desiredWorkers * 0.4F) {
            currentWorkersColor = "$RED1";
         } else if (currentWorkers < desiredWorkers * 0.6F) {
            currentWorkersColor = "$ORA1";
         } else if (currentWorkers < desiredWorkers * 0.8F) {
            currentWorkersColor = "$YEL1";
         }

         this.iconWorkers.draw(positionX, positionY + 32.0F);
         this.font.drawString(positionX + 20.0F, positionY + 32.0F, "$CYA1" + desiredWorkers + "$WHI0/" + maximumWorkers, Text.FontType.BODY, 1);
         String currentWorkersEnrouteFormatted = "";
         if (currentWorkersEnroute > 0) {
            currentWorkersEnrouteFormatted = "$GRE1+" + currentWorkersEnroute;
         }

         if (currentWorkersEnroute < 0) {
            currentWorkersEnrouteFormatted = "$RED1" + currentWorkersEnroute;
         }

         this.iconWorkers.draw(positionX, positionY + 48.0F);
         this.font
            .drawString(positionX + 20.0F, positionY + 48.0F, currentWorkersColor + currentWorkers + currentWorkersEnrouteFormatted, Text.FontType.BODY, 1);
      }
   }

   private void renderMobLinesSet(ArrayList<MobBase> occupants, float originX, float originY) {
      if (occupants.size() == 0) {
         this.g.resetTransform();
         this.g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
      } else {
         this.g.setLineWidth(ScaleControl.getWorldScale());

         for (MobBase m : occupants) {
            this.g.setColor(new Color(0, 0, 0, 100));
            this.g.drawLine(originX, originY + 1.0F, this.map.getMapX() + m.getCenterPixelX(true), this.map.getMapY() + m.getCenterPixelY(true) + 1.0F);
         }

         for (MobBase m : occupants) {
            this.g.setColor(m.getColorLayers()[2]);
            this.g.drawLine(originX, originY, this.map.getMapX() + m.getCenterPixelX(true), this.map.getMapY() + m.getCenterPixelY(true));
         }

         this.g.fillRoundRect(originX - 2.0F, originY - 2.0F, 4.0F, 4.0F, 1);
         this.g.setLineWidth(1.0F);
         this.g.resetTransform();
         this.g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
      }
   }

   private void renderObjectLinesSet(ArrayList<ObjectBase> buildings, float originX, float originY) {
      if (buildings.size() == 0) {
         this.g.resetTransform();
         this.g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
      } else {
         this.g.setLineWidth(ScaleControl.getWorldScale());

         for (ObjectBase o : buildings) {
            this.g.setColor(new Color(0, 0, 0, 100));
            this.g.drawLine(originX, originY + 1.0F, this.map.getMapX() + o.getCenterPixelX(), this.map.getMapY() + o.getCenterPixelY() + 1.0F);
         }

         for (ObjectBase o : buildings) {
            this.g.setColor(new Color(255, 255, 255, 100));
            this.g.drawLine(originX, originY, this.map.getMapX() + o.getCenterPixelX(), this.map.getMapY() + o.getCenterPixelY());
         }

         this.g.setColor(Color.white);
         this.g.fillRoundRect(originX - 2.0F, originY - 2.0F, 4.0F, 4.0F, 1);
         this.g.setLineWidth(1.0F);
         this.g.resetTransform();
         this.g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
      }
   }

   private void displayResourceGrabBar(ObjectBase o, Graphics g, boolean debug) {
      if (this.controller.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.INFLUENCE && this.influence.getSelectedSpell() == SpellBase.SpellType.GRAB) {
         g.resetTransform();
         g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
         float x = Game.getCS().getMouseX();
         float y = Game.getCS().getMouseY();
         if (o.getResourceCount(this.influence.getGrabRotationResourceType()) != 0) {
            ResourceBase r = o.getStorageResources(this.influence.getGrabRotationResourceType()).get(0);
            r.renderSprite((int)x + 30, (int)y - 10, debug);
            int mobCount = o.getStorageResources(this.influence.getGrabRotationResourceType()).size();
            if (mobCount <= 1) {
               this.font.drawString(x + 50.0F, y - 10.0F, mobCount + " " + r.getType().getTemplate().getName(), Text.FontType.BODY, 1);
            } else {
               this.font.drawString(x + 50.0F, y - 10.0F, mobCount + " " + r.getType().getTemplate().getNamePlural(), Text.FontType.BODY, 1);
            }
         }

         if (o.getTotalResourcesCount() <= 0) {
            this.font.drawString(x + 50.0F, y - 10.0F, Text.getText("playOverlay.general.nothingToGrab"), Text.FontType.BODY, 1);
         } else {
            g.setColor(new Color(0, 0, 0, 200));
            g.fillRect(x + 49.0F, y + 8.0F, this.influence.getGrabRotationPeriod() / 4 + 2, 5.0F);
            g.setColor(new Color(0, 255, 0, 200));
            int tickOut = this.influence.getGrabRotationPeriodTick();
            if (tickOut < 0) {
               tickOut = 0;
            }

            g.fillRect(x + 50.0F, y + 9.0F, (this.influence.getGrabRotationPeriod() - tickOut) / 4, 3.0F);
         }
      }
   }
}
