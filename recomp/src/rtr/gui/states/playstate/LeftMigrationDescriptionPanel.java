package rtr.gui.states.playstate;

import java.util.EnumMap;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.gui.buttons.GUIIcon;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.PlayStateGUIPanelBase;
import rtr.resources.ResourceModule;
import rtr.system.ScaleControl;

public class LeftMigrationDescriptionPanel extends PlayStateGUIPanelBase {
   private Image panel;
   private Image migrationPanelWorldMap;
   private Image worldMapHighlightBoxSmall;
   private GUIIcon home;
   private GUIIcon enRoute;
   private GUIIcon courierGolem;
   private GUIIcon population;
   private EnumMap<ResourceModule.ResourceType, GUIIcon> resourceIcons = new EnumMap<>(ResourceModule.ResourceType.class);
   private String highlightedRegion;
   private ResourceModule.ResourceType highlightedRegionResource;
   private boolean useResourceTotals;

   public LeftMigrationDescriptionPanel(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.panel = ImageLoader.getImage("res/GUI/gameState/migrationDescriptionPanel.png");
      this.migrationPanelWorldMap = ImageLoader.getImage("res/GUI/worldMap/worldMapSmall.png");
      this.worldMapHighlightBoxSmall = ImageLoader.getImage("res/GUI/gameState/worldMapHighlightBoxSmall.png");
      this.home = new GUIIcon("iconHome");
      this.enRoute = new GUIIcon("iconMigrationEnRoute");
      this.courierGolem = new GUIIcon("iconCourierGolem");
      this.population = new GUIIcon("iconVillager");

      ResourceModule.ResourceType[] var9;
      for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
         GUIIcon newIcon = new GUIIcon(type);
         this.resourceIcons.put(type, newIcon);
         this.guiButtons.add(newIcon);
      }
   }

   public void render(boolean debug) throws SlickException {
      this.x = this.controller.getLeftPanel().getRightX();
      this.y = ScaleControl.getInterfaceCenterY() - 265;
      if (this.y < 134) {
         this.y = 134;
      }

      if (!this.highlightedRegion.equals("null")) {
         String[] mapList = this.map.getConnectedRegionList();

         for (int i = 0; i < 8; i++) {
            if (mapList[i].equals(this.highlightedRegion)) {
               this.map.getConnectedRegionListWorldMiniMaps(i).draw(this.x + 10, this.y + 10);
               this.g.setWorldClip(this.x + 10, this.y + 270, 256.0F, 250.0F);
               int worldMapOffsetX = this.x + 138 - this.map.getWorldMapX() / 2;
               int worldMapOffsetY = this.y + 395 - this.map.getWorldMapY() / 2;
               this.migrationPanelWorldMap.draw(worldMapOffsetX, worldMapOffsetY);
               this.g.clearWorldClip();
               this.panel.draw(this.x, this.y);
               this.worldMapHighlightBoxSmall
                  .drawCentered(worldMapOffsetX + this.map.getWorldMapX() / 2, worldMapOffsetY + this.map.getWorldMapY() / 2, new Color(255, 255, 255, 150));
               this.g.setColor(new Color(0, 0, 0, 200));
               this.g.fillOval(worldMapOffsetX + this.map.getWorldMapX() / 2 - 22, worldMapOffsetY + this.map.getWorldMapY() / 2 - 16, 32.0F, 32.0F, 6);
               this.g.fillOval(worldMapOffsetX + this.map.getWorldMapX() / 2 - 10, worldMapOffsetY + this.map.getWorldMapY() / 2 - 16, 32.0F, 32.0F, 6);
               this.home.render(this.g, worldMapOffsetX + this.map.getWorldMapX() / 2 - 8, worldMapOffsetY + this.map.getWorldMapY() / 2 - 8, debug);

               for (int i2 = 0; i2 < 8; i2++) {
                  if (!mapList[i2].equals("null")) {
                     int connectedRegionX = this.map.getConnectedRegionListWorldMapX(i2);
                     int connectedRegionY = this.map.getConnectedRegionListWorldMapY(i2);
                     if (i == i2) {
                        this.worldMapHighlightBoxSmall.drawCentered(worldMapOffsetX + connectedRegionX / 2, worldMapOffsetY + connectedRegionY / 2);
                     } else {
                        this.worldMapHighlightBoxSmall
                           .drawCentered(worldMapOffsetX + connectedRegionX / 2, worldMapOffsetY + connectedRegionY / 2, new Color(255, 255, 255, 100));
                     }

                     this.g.fillOval(worldMapOffsetX + connectedRegionX / 2 - 32, worldMapOffsetY + connectedRegionY / 2 - 16, 32.0F, 32.0F, 6);
                     this.g.fillOval(worldMapOffsetX + connectedRegionX / 2 - 16, worldMapOffsetY + connectedRegionY / 2 - 16, 32.0F, 32.0F, 6);
                     this.g.fillOval(worldMapOffsetX + connectedRegionX / 2, worldMapOffsetY + connectedRegionY / 2 - 16, 32.0F, 32.0F, 6);
                     if (this.map.getConnectedRegionListWorldLost(mapList[i2])) {
                        this.font
                           .drawString(
                              worldMapOffsetX + connectedRegionX / 2,
                              worldMapOffsetY + connectedRegionY / 2 - 11,
                              Text.getText("playLeftMigrationDescriptionPanel.lost"),
                              Text.FontType.BODY,
                              2,
                              true
                           );
                     } else if (this.highlightedRegionResource != null) {
                        this.resourceIcons
                           .get(this.highlightedRegionResource)
                           .render(this.g, worldMapOffsetX + connectedRegionX / 2 - 26, worldMapOffsetY + connectedRegionY / 2 - 18, debug);
                        this.courierGolem.render(this.g, worldMapOffsetX + connectedRegionX / 2 - 26, worldMapOffsetY + connectedRegionY / 2 - 2, debug);
                        this.font
                           .drawString(
                              worldMapOffsetX + connectedRegionX / 2 - 10,
                              worldMapOffsetY + connectedRegionY / 2 - 14,
                              "$GRE1" + this.migration.getCurrentResourcesCount(mapList[i2], this.highlightedRegionResource),
                              Text.FontType.BODY,
                              1
                           );
                        this.font
                           .drawString(
                              worldMapOffsetX + connectedRegionX / 2 - 10,
                              worldMapOffsetY + connectedRegionY / 2 - 2,
                              "$GRE1" + this.migration.getEnrouteResourcesCount(mapList[i2], this.highlightedRegionResource),
                              Text.FontType.BODY,
                              1
                           );
                     } else if (this.useResourceTotals) {
                        this.resourceIcons
                           .get(ResourceModule.ResourceType.GOLD_COIN_SACK)
                           .render(this.g, worldMapOffsetX + connectedRegionX / 2 - 26, worldMapOffsetY + connectedRegionY / 2 - 18, debug);
                        this.courierGolem.render(this.g, worldMapOffsetX + connectedRegionX / 2 - 26, worldMapOffsetY + connectedRegionY / 2 - 2, debug);
                        this.font
                           .drawString(
                              worldMapOffsetX + connectedRegionX / 2 - 10,
                              worldMapOffsetY + connectedRegionY / 2 - 14,
                              "$GRE1" + this.migration.getTotalCurrentResourcesCount(mapList[i2]),
                              Text.FontType.BODY,
                              1
                           );
                        this.font
                           .drawString(
                              worldMapOffsetX + connectedRegionX / 2 - 10,
                              worldMapOffsetY + connectedRegionY / 2 - 2,
                              "$GRE1" + this.migration.getTotalEnrouteResourcesCount(mapList[i2]),
                              Text.FontType.BODY,
                              1
                           );
                     } else {
                        this.population.render(this.g, worldMapOffsetX + connectedRegionX / 2 - 26, worldMapOffsetY + connectedRegionY / 2 - 14, debug);
                        this.enRoute.render(this.g, worldMapOffsetX + connectedRegionX / 2 - 26, worldMapOffsetY + connectedRegionY / 2 - 2, debug);
                        this.font
                           .drawString(
                              worldMapOffsetX + connectedRegionX / 2 - 10,
                              worldMapOffsetY + connectedRegionY / 2 - 14,
                              "$GRE1" + this.migration.getCurrentPopulation().get(mapList[i2]),
                              Text.FontType.BODY,
                              1
                           );
                        this.font
                           .drawString(
                              worldMapOffsetX + connectedRegionX / 2 - 10,
                              worldMapOffsetY + connectedRegionY / 2 - 2,
                              "$GRE1" + this.migration.getEnroutePopulation(mapList[i2]),
                              Text.FontType.BODY,
                              1
                           );
                     }
                  }
               }
            }
         }
      }
   }

   @Override
   public void update() {
      String intersectingButton = this.controller.getLeftPanel().getIntersects();
      boolean leftPanelHighlight = false;
      this.highlightedRegion = "null";
      this.highlightedRegionResource = null;
      this.useResourceTotals = false;
      if (intersectingButton != null && this.controller.getLeftPanel().intersectsGUIMask()) {
         String[] mapList = this.map.getConnectedRegionList();

         for (int i = 0; i < 8; i++) {
            if (intersectingButton.equals("migrationArea" + i) || intersectingButton.equals("courierArea" + i)) {
               this.highlightedRegion = mapList[i];
               leftPanelHighlight = true;
               if (intersectingButton.equals("courierArea" + i)) {
                  this.useResourceTotals = true;
               }
               break;
            }
         }

         if (!leftPanelHighlight) {
            ResourceModule.ResourceType[] var7;
            for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
               if (intersectingButton.equals("courierResourceArea" + t.getTemplate().getName())) {
                  this.highlightedRegion = this.controller.getLeftPanel().getSelectedCourierMap();
                  this.highlightedRegionResource = t;
                  leftPanelHighlight = true;
                  break;
               }
            }
         }
      }

      if (!leftPanelHighlight) {
         this.highlightedRegion = "null";
      }
   }

   public String getHighlightedRegion() {
      return this.highlightedRegion;
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
