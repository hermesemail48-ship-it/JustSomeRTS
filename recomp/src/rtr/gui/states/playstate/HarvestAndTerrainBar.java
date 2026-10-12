package rtr.gui.states.playstate;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.SettingsParser;
import rtr.font.Text;
import rtr.gui.buttons.GUIRoundButton;
import rtr.gui.buttons.GUISquarePanelButton;
import rtr.gui.states.BrushLoader;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.PlayStateGUIPanelBase;
import rtr.map.MapModule;
import rtr.map.MapTilesLoader;
import rtr.objects.ObjectBase;
import rtr.system.ScaleControl;
import rtr.system.gamemodetemplates.GameModeTemplateBase;

public class HarvestAndTerrainBar extends PlayStateGUIPanelBase {
   private GUISquarePanelButton brushCircle = new GUISquarePanelButton("brushCircle", SettingsParser.getKeyCycleBrushTypeName(), 0, "brushCircle");
   private GUISquarePanelButton brushFilletedSquare;
   private GUISquarePanelButton brushChamferedSquare;
   private GUISquarePanelButton brushSquare;
   private BrushLoader.BrushType selectedBrushMode;
   private GUIRoundButton brushSizeMinus;
   private GUIRoundButton brushSizePlus;
   private GUISquarePanelButton harvestWood;
   private GUISquarePanelButton harvestRock;
   private GUISquarePanelButton harvestFoodAndWater;
   private GUISquarePanelButton harvestCrystals;
   private GUISquarePanelButton accentMode;
   private GUISquarePanelButton eraseAll;
   private GUISquarePanelButton eraseTerrain;
   private GUISquarePanelButton eraseTopography;
   private GUISquarePanelButton eraseObjects;
   private GUISquarePanelButton eraseRoads;
   private MapModule.EraseMode selectedEraseMode;
   private GUISquarePanelButton hole;
   private GUISquarePanelButton patch;
   private int selectedHolePatchMode;

   public HarvestAndTerrainBar(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      Text.setVariableText("shortcutKey", SettingsParser.getKeyCycleBrushTypeName());
      this.brushCircle.setToolTip(Text.getText("mapEditorPlayHarvestAndTerrainBar.button.tooltip.circleBrushHeader"));
      this.guiButtons.add(this.brushCircle);
      this.brushFilletedSquare = new GUISquarePanelButton("brushFilletedSquare", SettingsParser.getKeyCycleBrushTypeName(), 0, "brushFilletedSquare");
      Text.setVariableText("shortcutKey", SettingsParser.getKeyCycleBrushTypeName());
      this.brushFilletedSquare.setToolTip(Text.getText("mapEditorPlayHarvestAndTerrainBar.button.tooltip.filletedSquareBrushHeader"));
      this.guiButtons.add(this.brushFilletedSquare);
      this.brushChamferedSquare = new GUISquarePanelButton("brushChamferedSquare", SettingsParser.getKeyCycleBrushTypeName(), 0, "brushChamferedSquare");
      Text.setVariableText("shortcutKey", SettingsParser.getKeyCycleBrushTypeName());
      this.brushChamferedSquare.setToolTip(Text.getText("mapEditorPlayHarvestAndTerrainBar.button.tooltip.chamferedSquareBrushHeader"));
      this.guiButtons.add(this.brushChamferedSquare);
      this.brushSquare = new GUISquarePanelButton("brushSquare", SettingsParser.getKeyCycleBrushTypeName(), 0, "brushSquare");
      Text.setVariableText("shortcutKey", SettingsParser.getKeyCycleBrushTypeName());
      this.brushSquare.setToolTip(Text.getText("mapEditorPlayHarvestAndTerrainBar.button.tooltip.squareBrushHeader"));
      this.guiButtons.add(this.brushSquare);
      this.brushSizeMinus = new GUIRoundButton("brushSizeMinus", "buttonMinus", 0);
      Text.setVariableText("shortcutKey", SettingsParser.getBrushSizeDownName());
      this.brushSizeMinus.setToolTip(Text.getText("mapEditorPlayHarvestAndTerrainBar.button.tooltip.decreaseBrushSizeHeader"));
      this.guiButtons.add(this.brushSizeMinus);
      this.brushSizePlus = new GUIRoundButton("brushSizePlus", "buttonPlus", 0);
      Text.setVariableText("shortcutKey", SettingsParser.getBrushSizeUpName());
      this.brushSizePlus.setToolTip(Text.getText("mapEditorPlayHarvestAndTerrainBar.button.tooltip.increaseBrushSizeHeader"));
      this.guiButtons.add(this.brushSizePlus);
      this.harvestWood = new GUISquarePanelButton("harvestWood", SettingsParser.getKeyHarvestWoodName(), 1, "harvestWood");
      Text.setVariableText("shortcutKey", SettingsParser.getKeyHarvestWoodName());
      this.harvestWood
         .setToolTip(
            Text.getText("mapEditorPlayHarvestAndTerrainBar.button.tooltip.harvestWoodHeader"),
            Text.getText("mapEditorPlayHarvestAndTerrainBar.button.tooltip.harvestWood")
         );
      this.guiButtons.add(this.harvestWood);
      this.harvestRock = new GUISquarePanelButton("harvestRock", SettingsParser.getKeyHarvestRockName(), 1, "harvestRock");
      Text.setVariableText("shortcutKey", SettingsParser.getKeyHarvestRockName());
      this.harvestRock
         .setToolTip(
            Text.getText("mapEditorPlayHarvestAndTerrainBar.button.tooltip.harvestRockHeader"),
            Text.getText("mapEditorPlayHarvestAndTerrainBar.button.tooltip.harvestRock")
         );
      this.guiButtons.add(this.harvestRock);
      this.harvestFoodAndWater = new GUISquarePanelButton("harvestFoodAndWater", SettingsParser.getKeyHarvestFoodAndWaterName(), 1, "harvestFoodAndWater");
      Text.setVariableText("shortcutKey", SettingsParser.getKeyHarvestFoodAndWaterName());
      this.harvestFoodAndWater
         .setToolTip(
            Text.getText("mapEditorPlayHarvestAndTerrainBar.button.tooltip.harvestFoodAndWaterHeader"),
            Text.getText("mapEditorPlayHarvestAndTerrainBar.button.tooltip.harvestFoodAndWater")
         );
      this.guiButtons.add(this.harvestFoodAndWater);
      this.harvestCrystals = new GUISquarePanelButton("harvestCrystals", SettingsParser.getKeyHarvestCrystalsName(), 1, "harvestCrystals");
      Text.setVariableText("shortcutKey", SettingsParser.getKeyHarvestCrystalsName());
      this.harvestCrystals
         .setToolTip(
            Text.getText("mapEditorPlayHarvestAndTerrainBar.button.tooltip.harvestCrystalHeader"),
            Text.getText("mapEditorPlayHarvestAndTerrainBar.button.tooltip.harvestCrystal")
         );
      this.guiButtons.add(this.harvestCrystals);
      this.accentMode = new GUISquarePanelButton("accentMode", SettingsParser.getKeyAccentModeToggleName(), 0, "accentMode");
      Text.setVariableText("shortcutKey", SettingsParser.getKeyAccentModeToggleName());
      this.accentMode
         .setToolTip(
            Text.getText("mapEditorPlayHarvestAndTerrainBar.button.tooltip.accentModeHeader"),
            Text.getText("mapEditorPlayHarvestAndTerrainBar.button.tooltip.accentMode")
         );
      this.guiButtons.add(this.accentMode);
      this.eraseAll = new GUISquarePanelButton("eraseAll", SettingsParser.getKeyEraseToolName(), 0, "eraseAll");
      Text.setVariableText("shortcutKey", SettingsParser.getKeyEraseToolName());
      this.eraseAll
         .setToolTip(
            Text.getText("mapEditorPlayHarvestAndTerrainBar.button.tooltip.eraseAllHeader"),
            Text.getText("mapEditorPlayHarvestAndTerrainBar.button.tooltip.eraseAll")
         );
      this.guiButtons.add(this.eraseAll);
      this.eraseTerrain = new GUISquarePanelButton("eraseTerrain", SettingsParser.getKeyEraseToolName(), 0, "eraseTerrain");
      Text.setVariableText("shortcutKey", SettingsParser.getKeyEraseToolName());
      this.eraseTerrain
         .setToolTip(
            Text.getText("mapEditorPlayHarvestAndTerrainBar.button.tooltip.eraseTerrainHeader"),
            Text.getText("mapEditorPlayHarvestAndTerrainBar.button.tooltip.eraseTerrain")
         );
      this.guiButtons.add(this.eraseTerrain);
      this.eraseTopography = new GUISquarePanelButton("eraseTopography", SettingsParser.getKeyEraseToolName(), 0, "eraseTopography");
      Text.setVariableText("shortcutKey", SettingsParser.getKeyEraseToolName());
      this.eraseTopography
         .setToolTip(
            Text.getText("mapEditorPlayHarvestAndTerrainBar.button.tooltip.eraseTopographyHeader"),
            Text.getText("mapEditorPlayHarvestAndTerrainBar.button.tooltip.eraseTopography")
         );
      this.guiButtons.add(this.eraseTopography);
      this.eraseObjects = new GUISquarePanelButton("eraseObjects", SettingsParser.getKeyEraseToolName(), 0, "eraseObjects");
      Text.setVariableText("shortcutKey", SettingsParser.getKeyEraseToolName());
      this.eraseObjects
         .setToolTip(
            Text.getText("mapEditorPlayHarvestAndTerrainBar.button.tooltip.eraseObjectsHeader"),
            Text.getText("mapEditorPlayHarvestAndTerrainBar.button.tooltip.eraseObjects")
         );
      this.guiButtons.add(this.eraseObjects);
      this.eraseRoads = new GUISquarePanelButton("eraseRoads", SettingsParser.getKeyEraseToolName(), 0, "eraseRoads");
      Text.setVariableText("shortcutKey", SettingsParser.getKeyEraseToolName());
      this.eraseRoads
         .setToolTip(
            Text.getText("mapEditorPlayHarvestAndTerrainBar.button.tooltip.eraseRoadsHeader"),
            Text.getText("mapEditorPlayHarvestAndTerrainBar.button.tooltip.eraseRoads")
         );
      this.guiButtons.add(this.eraseRoads);
      this.hole = new GUISquarePanelButton("hole", SettingsParser.getKeyHoleToolName(), 0, "hole");
      Text.setVariableText("shortcutKey", SettingsParser.getKeyHoleToolName());
      this.hole
         .setToolTip(
            Text.getText("mapEditorPlayHarvestAndTerrainBar.button.tooltip.holeHeader"), Text.getText("mapEditorPlayHarvestAndTerrainBar.button.tooltip.hole")
         );
      this.guiButtons.add(this.hole);
      this.patch = new GUISquarePanelButton("patch", SettingsParser.getKeyPatchToolName(), 0, "patch");
      Text.setVariableText("shortcutKey", SettingsParser.getKeyPatchToolName());
      this.patch
         .setToolTip(
            Text.getText("mapEditorPlayHarvestAndTerrainBar.button.tooltip.patchHeader"),
            Text.getText("mapEditorPlayHarvestAndTerrainBar.button.tooltip.patch")
         );
      this.guiButtons.add(this.patch);
      this.selectedBrushMode = BrushLoader.BrushType.ROUND;
      this.selectedEraseMode = MapModule.EraseMode.ALL;
   }

   public void render(boolean debug) throws SlickException {
      this.x = 6;
      this.y = ScaleControl.getInterfaceHeight() - 49;
      switch (this.selectedBrushMode) {
         case ROUND:
            this.brushCircle.render(this.g, this.mouse, this.x + 6, this.y, false, debug);
            break;
         case FILLETED_SQUARE:
            this.brushFilletedSquare.render(this.g, this.mouse, this.x + 6, this.y, false, debug);
            break;
         case CHAMFERED_SQUARE:
            this.brushChamferedSquare.render(this.g, this.mouse, this.x + 6, this.y, false, debug);
            break;
         case SQUARE:
            this.brushSquare.render(this.g, this.mouse, this.x + 6, this.y, false, debug);
      }

      this.brushSizeMinus.render(this.g, this.mouse, this.x, this.y + 13, debug);
      this.brushSizePlus.render(this.g, this.mouse, this.x + 38, this.y + 13, debug);
      this.font.drawString(this.x + 28, this.y + 16, "$CYA1" + this.controller.getBrushLoader().getBrushSize(), Text.FontType.BODY, 1, true);
      int alignX = 55;
      this.harvestWood.render(this.g, this.mouse, this.x + alignX + 6, this.y, this.object.objectCount(MapTilesLoader.TileSet.CASTLE_1) == 0, debug);
      alignX += this.harvestWood.getWidth();
      this.harvestRock.render(this.g, this.mouse, this.x + alignX + 6, this.y, this.object.objectCount(MapTilesLoader.TileSet.CASTLE_1) == 0, debug);
      alignX += this.harvestRock.getWidth();
      boolean canHarvestFoodAndWater = this.object.objectCount(MapTilesLoader.TileSet.FARM, ObjectBase.ObjectSubType.BUILT) > 0
         || this.object.objectCount(MapTilesLoader.TileSet.WATER_PURIFIER, ObjectBase.ObjectSubType.BUILT) > 0
         || this.object.objectCount(MapTilesLoader.TileSet.FARM, ObjectBase.ObjectSubType.CONSTRUCTION) > 0
         || this.object.objectCount(MapTilesLoader.TileSet.WATER_PURIFIER, ObjectBase.ObjectSubType.CONSTRUCTION) > 0;
      this.harvestFoodAndWater.render(this.g, this.mouse, this.x + alignX + 6, this.y, !canHarvestFoodAndWater, debug);
      alignX += this.harvestFoodAndWater.getWidth();
      this.harvestCrystals.render(this.g, this.mouse, this.x + alignX + 6, this.y, this.object.objectCount(MapTilesLoader.TileSet.CASTLE_1) == 0, debug);
      alignX += this.harvestCrystals.getWidth() * 2;
      if (this.save.getActiveRegionalSave().getGameMode() == GameModeTemplateBase.GameMode.SANDBOX) {
         this.accentMode.render(this.g, this.mouse, this.x + alignX + 6, this.y, false, debug);
         alignX += this.accentMode.getWidth();
         switch (this.selectedEraseMode) {
            case ALL:
               this.eraseAll.render(this.g, this.mouse, this.x + alignX + 6, this.y, false, debug);
               break;
            case TERRAIN:
               this.eraseTerrain.render(this.g, this.mouse, this.x + alignX + 6, this.y, false, debug);
            case TERRAIN_BOTTOM_ONLY:
            case TERRAIN_TOP_ONLY:
            default:
               break;
            case TOPOGRAPHY:
               this.eraseTopography.render(this.g, this.mouse, this.x + alignX + 6, this.y, false, debug);
               break;
            case OBJECTS:
               this.eraseObjects.render(this.g, this.mouse, this.x + alignX + 6, this.y, false, debug);
               break;
            case ROADS:
               this.eraseRoads.render(this.g, this.mouse, this.x + alignX + 6, this.y, false, debug);
         }

         alignX += this.eraseAll.getWidth();
         switch (this.selectedHolePatchMode) {
            case 0:
               this.hole.render(this.g, this.mouse, this.x + alignX + 6, this.y, false, debug);
               break;
            case 1:
               this.patch.render(this.g, this.mouse, this.x + alignX + 6, this.y, false, debug);
         }

         alignX += this.hole.getWidth();
      }
   }

   @Override
   public void update() {
      if (this.map.getAccentMode()) {
         this.accentMode.select();
      } else {
         this.accentMode.deselect();
      }
   }

   public BrushLoader.BrushType getBrush() {
      return this.selectedBrushMode;
   }

   public void setBrush(BrushLoader.BrushType t) {
      this.selectedBrushMode = t;
   }

   public void setEraseMode(MapModule.EraseMode e) {
      this.selectedEraseMode = e;
   }

   public void setHolePatchHole() {
      this.selectedHolePatchMode = 0;
   }

   public void setHolePatchPatch() {
      this.selectedHolePatchMode = 1;
   }
}
