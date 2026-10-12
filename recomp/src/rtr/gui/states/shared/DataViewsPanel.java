package rtr.gui.states.shared;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.DataModule;
import rtr.ImageLoader;
import rtr.SettingsParser;
import rtr.font.Text;
import rtr.gui.buttons.GUIButtonGenericWood;
import rtr.gui.states.MapEditorGUIController;
import rtr.gui.states.MapEditorGUIData;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.SharedGUIPanelBase;
import rtr.system.Game;
import rtr.system.gamemodetemplates.GameModeTemplateBase;

public class DataViewsPanel extends SharedGUIPanelBase {
   private Image panel;
   private Rectangle mask;
   private GUIButtonGenericWood resourceValue;
   private GUIButtonGenericWood movementCost;
   private GUIButtonGenericWood blockMap;
   private GUIButtonGenericWood range;
   private GUIButtonGenericWood essenceBuildupMap;
   private GUIButtonGenericWood corruptionResistance;
   private GUIButtonGenericWood desirability;
   private GUIButtonGenericWood towerRange;
   private GUIButtonGenericWood lightningRodRange;
   private GUIButtonGenericWood corruptedTiles;
   private GUIButtonGenericWood hideTopography;
   private GUIButtonGenericWood collisionMap;
   private GUIButtonGenericWood shadowMap;
   private GUIButtonGenericWood depthMap;
   private GUIButtonGenericWood lightValue;
   private GUIButtonGenericWood paths;
   private GUIButtonGenericWood traffic;
   private GUIButtonGenericWood roadDurability;
   private GUIButtonGenericWood villageThreatMap;
   private GUIButtonGenericWood threatenedTileMap;

   public DataViewsPanel(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.init();
   }

   public DataViewsPanel(MapEditorGUIController controller, MapEditorGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.init();
   }

   public void init() throws SlickException {
      if (SettingsParser.getDebug() && Game.getCS().getGameMode() != GameModeTemplateBase.GameMode.MAP_EDITOR) {
         this.panel = ImageLoader.getImage("res/GUI/gameState/dataViewsDebugPanel.png");
      } else {
         this.panel = ImageLoader.getImage("res/GUI/gameState/dataViewsPanel.png");
      }

      this.mask = new Rectangle(0.0F, 0.0F, this.panel.getWidth(), this.panel.getHeight());
      this.masks.add(this.mask);
      this.resourceValue = new GUIButtonGenericWood("resourceValue", Text.getText("sharedDataViewsPanel.button.resourceValues"));
      this.guiButtons.add(this.resourceValue);
      this.movementCost = new GUIButtonGenericWood("movementCost", Text.getText("sharedDataViewsPanel.button.movementCosts"));
      this.guiButtons.add(this.movementCost);
      this.blockMap = new GUIButtonGenericWood("blockMap", Text.getText("sharedDataViewsPanel.button.blockMap"));
      this.guiButtons.add(this.blockMap);
      this.range = new GUIButtonGenericWood("range", Text.getText("sharedDataViewsPanel.button.range"));
      this.guiButtons.add(this.range);
      this.essenceBuildupMap = new GUIButtonGenericWood("essenceBuildupMap", Text.getText("sharedDataViewsPanel.button.essenceBuildupMap"));
      this.guiButtons.add(this.essenceBuildupMap);
      this.corruptionResistance = new GUIButtonGenericWood("corruptionResistance", Text.getText("sharedDataViewsPanel.button.corruptionResistance"));
      this.guiButtons.add(this.corruptionResistance);
      this.desirability = new GUIButtonGenericWood("desirability", Text.getText("sharedDataViewsPanel.button.desirability"));
      this.guiButtons.add(this.desirability);
      this.towerRange = new GUIButtonGenericWood("towerRange", Text.getText("sharedDataViewsPanel.button.towerRange"));
      this.guiButtons.add(this.towerRange);
      this.lightningRodRange = new GUIButtonGenericWood("lightningRodRange", Text.getText("sharedDataViewsPanel.button.lightningRodRange"));
      this.guiButtons.add(this.lightningRodRange);
      this.corruptedTiles = new GUIButtonGenericWood("corruptedTiles", Text.getText("sharedDataViewsPanel.button.corruptedTiles"));
      this.guiButtons.add(this.corruptedTiles);
      this.hideTopography = new GUIButtonGenericWood("hideTopography", Text.getText("sharedDataViewsPanel.button.hideTopography"));
      this.guiButtons.add(this.hideTopography);
      this.collisionMap = new GUIButtonGenericWood("collisionMap", Text.getText("sharedDataViewsPanel.button.collisionMap"));
      this.guiButtons.add(this.collisionMap);
      this.shadowMap = new GUIButtonGenericWood("shadowMap", Text.getText("sharedDataViewsPanel.button.shadowMap"));
      this.guiButtons.add(this.shadowMap);
      this.depthMap = new GUIButtonGenericWood("depthMap", Text.getText("sharedDataViewsPanel.button.depthMap"));
      this.guiButtons.add(this.depthMap);
      this.lightValue = new GUIButtonGenericWood("lightValue", Text.getText("sharedDataViewsPanel.button.lightValues"));
      this.guiButtons.add(this.lightValue);
      this.paths = new GUIButtonGenericWood("paths", Text.getText("sharedDataViewsPanel.button.paths"));
      this.guiButtons.add(this.paths);
      this.traffic = new GUIButtonGenericWood("traffic", Text.getText("sharedDataViewsPanel.button.traffic"));
      this.guiButtons.add(this.traffic);
      this.roadDurability = new GUIButtonGenericWood("roadDurability", Text.getText("sharedDataViewsPanel.button.roadDurability"));
      this.guiButtons.add(this.roadDurability);
      this.villageThreatMap = new GUIButtonGenericWood("villageThreatMap", Text.getText("sharedDataViewsPanel.button.villageThreatMap"));
      this.guiButtons.add(this.villageThreatMap);
      this.threatenedTileMap = new GUIButtonGenericWood("threatenedTileMap", Text.getText("sharedDataViewsPanel.button.threatenedTileMap"));
      this.guiButtons.add(this.threatenedTileMap);
   }

   public void render(boolean debug) throws SlickException {
      if (this.playStateController != null) {
         this.x = this.playStateController.getResourceAndInfluenceBar().getRightX() - this.panel.getWidth() - 251;
         if (this.x + this.panel.getWidth() > this.playStateController.getRightPanel().getLeftX() - 46) {
            this.x = this.x - Math.abs(this.x + this.panel.getWidth() - this.playStateController.getRightPanel().getLeftX() + 46);
         }

         this.y = 133;
      }

      if (this.mapEditorController != null) {
         this.x = this.panel.getWidth() + this.mapEditorController.getSystemAndDataBar().getLeftX() - 112;
         if (this.x + this.panel.getWidth() > this.mapEditorController.getRightPanel().getLeftX()) {
            this.x = this.x - Math.abs(this.x + this.panel.getWidth() - this.mapEditorController.getRightPanel().getLeftX());
         }

         this.y = 49;
      }

      this.mask.setX(this.x);
      this.mask.setY(this.y);
      this.panel.draw(this.x, this.y);
      this.font.drawString(this.x + 93, this.y + 3, Text.getText("sharedDataViewsPanel.header"), Text.FontType.HEADER, 2, true);
      int alignY = 0;
      if (Game.getCS().getGameMode() != GameModeTemplateBase.GameMode.MAP_EDITOR) {
         this.resourceValue.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2, this.y + alignY + 30, false, true, debug);
         alignY += 24;
      }

      this.movementCost.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2, this.y + alignY + 30, false, true, debug);
      alignY += 24;
      this.blockMap.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2, this.y + alignY + 30, false, true, debug);
      alignY += 24;
      if (Game.getCS().getGameMode() != GameModeTemplateBase.GameMode.MAP_EDITOR) {
         this.corruptionResistance.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2, this.y + alignY + 30, false, true, debug);
         alignY += 24;
         if (SettingsParser.getDebug()) {
            this.range.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2 - 48, this.y + alignY + 30, false, true, debug);
            this.essenceBuildupMap.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2 + 28, this.y + alignY + 30, false, true, debug);
            alignY += 24;
         } else {
            this.range.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2, this.y + alignY + 30, false, true, debug);
            alignY += 24;
         }

         this.desirability.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2, this.y + alignY + 30, false, true, debug);
         alignY += 24;
         this.towerRange.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2, this.y + alignY + 30, false, true, debug);
         alignY += 24;
         this.lightningRodRange.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2, this.y + alignY + 30, false, true, debug);
         alignY += 24;
         this.corruptedTiles.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2, this.y + alignY + 30, false, true, debug);
         alignY += 24;
      }

      this.hideTopography.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2, this.y + alignY + 30, false, true, debug);
      alignY += 24;
      if (SettingsParser.getDebug()) {
         this.collisionMap.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2, this.y + alignY + 30, false, true, debug);
         alignY += 24;
         this.shadowMap.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2, this.y + alignY + 30, false, true, debug);
         alignY += 24;
         this.depthMap.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2 - 32, this.y + alignY + 30, false, true, debug);
         this.traffic.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2 + 43, this.y + alignY + 30, false, true, debug);
         alignY += 24;
         if (Game.getCS().getGameMode() != GameModeTemplateBase.GameMode.MAP_EDITOR) {
            this.paths.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2 - 47, this.y + alignY + 30, false, true, debug);
            this.lightValue.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2 + 28, this.y + alignY + 30, false, true, debug);
            alignY += 24;
            this.roadDurability.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2, this.y + alignY + 30, false, true, debug);
            alignY += 24;
            this.villageThreatMap.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2, this.y + alignY + 30, false, true, debug);
            alignY += 24;
            this.threatenedTileMap.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2, this.y + alignY + 30, false, true, debug);
            alignY += 24;
         }
      }
   }

   @Override
   public void update() {
      if (this.data.getDisplayMode() == DataModule.DataDisplayMode.RESOURCE_VALUE) {
         this.resourceValue.select();
      } else {
         this.resourceValue.deselect();
      }

      if (this.data.getDisplayMode() == DataModule.DataDisplayMode.MOVEMENT_COST) {
         this.movementCost.select();
      } else {
         this.movementCost.deselect();
      }

      if (this.data.getDisplayMode() == DataModule.DataDisplayMode.BLOCK_MAP) {
         this.blockMap.select();
      } else {
         this.blockMap.deselect();
      }

      if (this.data.getDisplayMode() == DataModule.DataDisplayMode.RANGE_MAP) {
         this.range.select();
      } else {
         this.range.deselect();
      }

      if (this.data.getDisplayMode() == DataModule.DataDisplayMode.ESSENCE_BUILDUP_MAP) {
         this.essenceBuildupMap.select();
      } else {
         this.essenceBuildupMap.deselect();
      }

      if (this.data.getDisplayMode() == DataModule.DataDisplayMode.CORRUPTION_RESISTANCE_MAP) {
         this.corruptionResistance.select();
      } else {
         this.corruptionResistance.deselect();
      }

      if (this.data.getDisplayMode() == DataModule.DataDisplayMode.DESIRABILITY_MAP) {
         this.desirability.select();
      } else {
         this.desirability.deselect();
      }

      if (this.data.getDisplayMode() == DataModule.DataDisplayMode.TOWER_RANGE_MAP) {
         this.towerRange.select();
      } else {
         this.towerRange.deselect();
      }

      if (this.data.getDisplayMode() == DataModule.DataDisplayMode.LIGHTNING_ROD_RANGE_MAP) {
         this.lightningRodRange.select();
      } else {
         this.lightningRodRange.deselect();
      }

      if (this.data.getDisplayMode() == DataModule.DataDisplayMode.CORRUPTED_TILES) {
         this.corruptedTiles.select();
      } else {
         this.corruptedTiles.deselect();
      }

      if (this.data.getDisplayMode() == DataModule.DataDisplayMode.HIDE_TOPOGRAPHY) {
         this.hideTopography.select();
      } else {
         this.hideTopography.deselect();
      }

      if (this.data.getDisplayMode() == DataModule.DataDisplayMode.COLLISION_MAP) {
         this.collisionMap.select();
      } else {
         this.collisionMap.deselect();
      }

      if (this.data.getDisplayMode() == DataModule.DataDisplayMode.SHADOW_MAP) {
         this.shadowMap.select();
      } else {
         this.shadowMap.deselect();
      }

      if (this.data.getDisplayMode() == DataModule.DataDisplayMode.DEPTH_MAP) {
         this.depthMap.select();
      } else {
         this.depthMap.deselect();
      }

      if (this.data.getDisplayMode() == DataModule.DataDisplayMode.LIGHT_VALUE) {
         this.lightValue.select();
      } else {
         this.lightValue.deselect();
      }

      if (this.data.getDisplayMode() == DataModule.DataDisplayMode.PATHS) {
         this.paths.select();
      } else {
         this.paths.deselect();
      }

      if (this.data.getDisplayMode() == DataModule.DataDisplayMode.TRAFFIC) {
         this.traffic.select();
      } else {
         this.traffic.deselect();
      }

      if (this.data.getDisplayMode() == DataModule.DataDisplayMode.ROAD_DURABILITY) {
         this.roadDurability.select();
      } else {
         this.roadDurability.deselect();
      }

      if (this.data.getDisplayMode() == DataModule.DataDisplayMode.VILLAGE_THREAT_MAP) {
         this.villageThreatMap.select();
      } else {
         this.villageThreatMap.deselect();
      }

      if (this.data.getDisplayMode() == DataModule.DataDisplayMode.THREATENED_TILE_MAP) {
         this.threatenedTileMap.select();
      } else {
         this.threatenedTileMap.deselect();
      }
   }

   @Override
   public int getCenterX() {
      return this.x + this.panel.getWidth() / 2;
   }

   @Override
   public int getCenterY() {
      return this.y + this.panel.getHeight() / 2;
   }

   @Override
   public int getLeftX() {
      return this.x;
   }

   @Override
   public int getTopY() {
      return this.y;
   }

   @Override
   public int getRightX() {
      return this.x + this.panel.getWidth();
   }

   @Override
   public int getBottomY() {
      return this.y + this.panel.getHeight();
   }
}
