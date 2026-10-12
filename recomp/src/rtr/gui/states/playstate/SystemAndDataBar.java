package rtr.gui.states.playstate;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.SettingsParser;
import rtr.font.Text;
import rtr.gui.buttons.GUISquarePanelButton;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.PlayStateGUIPanelBase;
import rtr.states.StateBase;

public class SystemAndDataBar extends PlayStateGUIPanelBase {
   private GUISquarePanelButton mainMenu = new GUISquarePanelButton("mainMenu", SettingsParser.getKeyMainMenuName(), 0, "mainMenu");
   private GUISquarePanelButton minimap;
   private GUISquarePanelButton statistics;
   private GUISquarePanelButton dataViews;
   private GUISquarePanelButton problemPanel;
   private GUISquarePanelButton buildingList;
   private GUISquarePanelButton resourceList;
   private GUISquarePanelButton populationList;
   private GUISquarePanelButton villageEfficiency;
   private GUISquarePanelButton music;
   private GUISquarePanelButton goals;

   public SystemAndDataBar(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      Text.setVariableText("shortcutKey", SettingsParser.getKeyMainMenuName());
      this.mainMenu
         .setToolTip(Text.getText("playSystemAndDataBar.button.tooltip.mainMenuHeader"), Text.getText("playSystemAndDataBar.button.tooltip.mainMenu"));
      this.guiButtons.add(this.mainMenu);
      this.minimap = new GUISquarePanelButton("minimap", SettingsParser.getKeyMinimapName(), 0, "minimap");
      Text.setVariableText("shortcutKey", SettingsParser.getKeyMinimapName());
      this.minimap.setToolTip(Text.getText("playSystemAndDataBar.button.tooltip.minimapHeader"), Text.getText("playSystemAndDataBar.button.tooltip.minimap"));
      this.guiButtons.add(this.minimap);
      this.statistics = new GUISquarePanelButton("statistics", 0, "statistics");
      this.statistics
         .setToolTip(Text.getText("playSystemAndDataBar.button.tooltip.statisticsHeader"), Text.getText("playSystemAndDataBar.button.tooltip.statistics"));
      this.guiButtons.add(this.statistics);
      this.dataViews = new GUISquarePanelButton("dataViews", SettingsParser.getKeyDataViewsName(), 0, "dataViews");
      Text.setVariableText("shortcutKey", SettingsParser.getKeyDataViewsName());
      this.dataViews
         .setToolTip(Text.getText("playSystemAndDataBar.button.tooltip.dataViewsHeader"), Text.getText("playSystemAndDataBar.button.tooltip.dataViews"));
      this.guiButtons.add(this.dataViews);
      this.problemPanel = new GUISquarePanelButton("problemPanel", SettingsParser.getKeyProblemPanelName(), 0, "problemPanel");
      Text.setVariableText("shortcutKey", SettingsParser.getKeyProblemPanelName());
      this.problemPanel
         .setToolTip(Text.getText("playSystemAndDataBar.button.tooltip.problemPanelHeader"), Text.getText("playSystemAndDataBar.button.tooltip.problemPanel"));
      this.guiButtons.add(this.problemPanel);
      this.buildingList = new GUISquarePanelButton("buildingList", SettingsParser.getKeyBuildingListName(), 0, "buildingList");
      Text.setVariableText("shortcutKey", SettingsParser.getKeyBuildingListName());
      this.buildingList
         .setToolTip(Text.getText("playSystemAndDataBar.button.tooltip.buildingListHeader"), Text.getText("playSystemAndDataBar.button.tooltip.buildingList"));
      this.guiButtons.add(this.buildingList);
      this.resourceList = new GUISquarePanelButton("resourceList", 0, "resourceList");
      this.resourceList
         .setToolTip(Text.getText("playSystemAndDataBar.button.tooltip.resourceListHeader"), Text.getText("playSystemAndDataBar.button.tooltip.resourceList"));
      this.guiButtons.add(this.resourceList);
      this.populationList = new GUISquarePanelButton("populationList", SettingsParser.getKeyPopulationName(), 0, "populationList");
      Text.setVariableText("shortcutKey", SettingsParser.getKeyPopulationName());
      this.populationList
         .setToolTip(
            Text.getText("playSystemAndDataBar.button.tooltip.populationListHeader"), Text.getText("playSystemAndDataBar.button.tooltip.populationList")
         );
      this.guiButtons.add(this.populationList);
      this.villageEfficiency = new GUISquarePanelButton("villageEfficiency", 0, "villagerEfficiency");
      this.villageEfficiency
         .setToolTip(
            Text.getText("playSystemAndDataBar.button.tooltip.villagerEfficiencyHeader"),
            Text.getText("playSystemAndDataBar.button.tooltip.villagerEfficiency")
         );
      this.guiButtons.add(this.villageEfficiency);
      this.music = new GUISquarePanelButton("music", 0, "music");
      this.music.setToolTip(Text.getText("playSystemAndDataBar.button.tooltip.musicHeader"), Text.getText("playSystemAndDataBar.button.tooltip.music"));
      this.guiButtons.add(this.music);
      this.goals = new GUISquarePanelButton("goals", 0, "goals");
      this.goals.setToolTip(Text.getText("playSystemAndDataBar.button.tooltip.goalsHeader"), Text.getText("playSystemAndDataBar.button.tooltip.goals"));
      this.guiButtons.add(this.goals);
   }

   public void render(boolean debug) throws SlickException {
      this.x = this.controller.getResourceAndInfluenceBar().getLeftX() + 556;
      this.y = 87;
      int alignX = 0;
      this.mainMenu.render(this.g, this.mouse, this.x + alignX, this.y, false, debug);
      alignX += this.mainMenu.getWidth();
      this.minimap.render(this.g, this.mouse, this.x + alignX, this.y, false, debug);
      alignX += this.minimap.getWidth();
      this.statistics.render(this.g, this.mouse, this.x + alignX, this.y, false, debug);
      alignX += this.statistics.getWidth();
      this.dataViews.render(this.g, this.mouse, this.x + alignX, this.y, false, debug);
      alignX += this.dataViews.getWidth();
      this.problemPanel.render(this.g, this.mouse, this.x + alignX, this.y, false, debug);
      alignX += this.problemPanel.getWidth();
      this.buildingList.render(this.g, this.mouse, this.x + alignX, this.y, false, debug);
      alignX += this.buildingList.getWidth();
      this.resourceList.render(this.g, this.mouse, this.x + alignX, this.y, false, debug);
      alignX += this.resourceList.getWidth();
      this.populationList.render(this.g, this.mouse, this.x + alignX, this.y, false, debug);
      alignX += this.populationList.getWidth();
      this.villageEfficiency.render(this.g, this.mouse, this.x + alignX, this.y, false, debug);
      alignX += this.villageEfficiency.getWidth();
      this.music.render(this.g, this.mouse, this.x + alignX, this.y, false, debug);
      alignX += this.music.getWidth();
      if (this.save.getActiveRegionalSave().getGameType() != StateBase.GameType.SKIRMISH) {
         this.goals.render(this.g, this.mouse, this.x + alignX, this.y, false, debug);
         alignX += this.goals.getWidth();
      }
   }

   @Override
   public void update() {
      if (this.controller.isMainMenuPanelEnabled()) {
         this.mainMenu.select();
      } else {
         this.mainMenu.deselect();
      }

      if (this.controller.isMinimapPanelEnabled()) {
         this.minimap.select();
      } else {
         this.minimap.deselect();
      }

      if (this.controller.isStatisticsPanelEnabled()) {
         this.statistics.select();
      } else {
         this.statistics.deselect();
      }

      if (this.controller.isDataViewsPanelEnabled()) {
         this.dataViews.select();
      } else {
         this.dataViews.deselect();
      }

      if (this.controller.isProblemPanelPanelEnabled()) {
         this.problemPanel.select();
      } else {
         this.problemPanel.deselect();
      }

      if (this.controller.isBuildingListPanelEnabled()) {
         this.buildingList.select();
      } else {
         this.buildingList.deselect();
      }

      if (this.controller.isResourceListPanelEnabled()) {
         this.resourceList.select();
      } else {
         this.resourceList.deselect();
      }

      if (this.controller.isPopulationListPanelEnabled()) {
         this.populationList.select();
      } else {
         this.populationList.deselect();
      }

      if (this.controller.isMusicPanelEnabled()) {
         this.music.select();
      } else {
         this.music.deselect();
      }

      if (this.controller.isVillageEfficiencyPanelEnabled()) {
         this.villageEfficiency.select();
      } else {
         this.villageEfficiency.deselect();
      }
   }
}
