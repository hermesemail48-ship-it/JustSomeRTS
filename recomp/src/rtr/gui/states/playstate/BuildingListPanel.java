package rtr.gui.states.playstate;

import rtr.platform.Platform;
import java.util.ArrayList;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.gui.buttons.GUIClickableText;
import rtr.gui.buttons.GUIIcon;
import rtr.gui.buttons.GUIRoundButton;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.PlayStateGUIPanelBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.system.ScaleControl;

public class BuildingListPanel extends PlayStateGUIPanelBase {
   private Image panel;
   private Rectangle mask;
   private GUIRoundButton listTop;
   private GUIRoundButton listBottom;
   private GUIRoundButton listDown;
   private GUIRoundButton listUp;
   private GUIIcon iconTotalBuildings;
   private GUIIcon iconTotalAncillaries;
   private GUIIcon iconTotalHomeOccupancy;
   private GUIIcon iconTotalRangerLodgeOccupancy;
   private GUIIcon iconTotalWorkers;
   private GUIClickableText[] nameList = new GUIClickableText[12];
   private GUIIcon[] iconWorkers = new GUIIcon[12];
   private GUIIcon[] iconOccupancy = new GUIIcon[12];
   private GUIIcon[] iconEnergy = new GUIIcon[12];
   private GUIIcon[] iconDirtyWater = new GUIIcon[12];
   private GUIIcon[] iconWater = new GUIIcon[12];
   private GUIIcon[] iconGoldCoins = new GUIIcon[12];
   private GUIIcon[] iconAmmo = new GUIIcon[12];
   private GUIRoundButton[] priorityUp = new GUIRoundButton[12];
   private GUIRoundButton[] priorityDown = new GUIRoundButton[12];
   private GUIRoundButton[] priorityTop = new GUIRoundButton[12];
   private GUIRoundButton[] priorityBottom = new GUIRoundButton[12];
   private GUIRoundButton[] prioritySortOrder = new GUIRoundButton[12];
   private ArrayList<ArrayList<ObjectBase>> buildingListGrouped;
   private int buildingsListStart = 0;
   private int buildingsPage = 1;

   public BuildingListPanel(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.panel = ImageLoader.getImage("res/GUI/gameState/buildingListPanel.png");
      this.mask = new Rectangle(0.0F, 0.0F, this.panel.getWidth(), this.panel.getHeight());
      this.masks.add(this.mask);
      this.listTop = new GUIRoundButton("listTop", "buttonTop", 0);
      this.listTop.setToolTip(Text.getText("playBuildingListPanel.button.tooltip.firstPageHeader"));
      this.guiButtons.add(this.listTop);
      this.listBottom = new GUIRoundButton("listBottom", "buttonBottom", 0);
      this.listBottom.setToolTip(Text.getText("playBuildingListPanel.button.tooltip.lastPageHeader"));
      this.guiButtons.add(this.listBottom);
      this.listDown = new GUIRoundButton("listDown", "buttonDown", 0);
      this.listDown.setToolTip(Text.getText("playBuildingListPanel.button.tooltip.pageDownHeader"));
      this.guiButtons.add(this.listDown);
      this.listUp = new GUIRoundButton("listUp", "buttonUp", 0);
      this.listUp.setToolTip(Text.getText("playBuildingListPanel.button.tooltip.pageUpHeader"));
      this.guiButtons.add(this.listUp);
      this.iconTotalBuildings = new GUIIcon("iconCivic");
      this.iconTotalBuildings
         .setToolTip(Text.getText("playBuildingListPanel.tooltip.totalBuildingsHeader"), Text.getText("playBuildingListPanel.tooltip.totalBuildings"));
      this.guiButtons.add(this.iconTotalBuildings);
      this.iconTotalAncillaries = new GUIIcon("iconCivic");
      this.iconTotalAncillaries
         .setToolTip(Text.getText("playBuildingListPanel.tooltip.totalAncillariesHeader"), Text.getText("playBuildingListPanel.tooltip.totalAncillaries"));
      this.guiButtons.add(this.iconTotalAncillaries);
      this.iconTotalHomeOccupancy = new GUIIcon("iconHome");
      this.iconTotalHomeOccupancy
         .setToolTip(Text.getText("playBuildingListPanel.tooltip.totalHomeOccupancyHeader"), Text.getText("playBuildingListPanel.tooltip.totalHomeOccupancy"));
      this.guiButtons.add(this.iconTotalHomeOccupancy);
      this.iconTotalRangerLodgeOccupancy = new GUIIcon("iconRangerLodge");
      this.iconTotalRangerLodgeOccupancy
         .setToolTip(
            Text.getText("playBuildingListPanel.tooltip.totalRangerLodgeOccupancyHeader"),
            Text.getText("playBuildingListPanel.tooltip.totalRangerLodgeOccupancy")
         );
      this.guiButtons.add(this.iconTotalRangerLodgeOccupancy);
      this.iconTotalWorkers = new GUIIcon("iconWorkers");
      this.iconTotalWorkers
         .setToolTip(Text.getText("playBuildingListPanel.tooltip.totalWorkersHeader"), Text.getText("playBuildingListPanel.tooltip.totalWorkers"));
      this.guiButtons.add(this.iconTotalWorkers);

      for (int i = 0; i < this.nameList.length; i++) {
         this.nameList[i] = new GUIClickableText("nameList" + i, 1);
         this.guiButtons.add(this.nameList[i]);
         this.iconWorkers[i] = new GUIIcon("iconWorkers");
         this.iconWorkers[i].setToolTip(Text.getText("playBuildingListPanel.tooltip.workersHeader"), Text.getText("playBuildingListPanel.tooltip.workers"));
         this.guiButtons.add(this.iconWorkers[i]);
         this.iconOccupancy[i] = new GUIIcon("iconHome");
         this.iconOccupancy[i]
            .setToolTip(Text.getText("playBuildingListPanel.tooltip.occupancyHeader"), Text.getText("playBuildingListPanel.tooltip.occupancy"));
         this.guiButtons.add(this.iconOccupancy[i]);
         this.iconEnergy[i] = new GUIIcon("iconEnergy");
         this.iconEnergy[i].setToolTip(Text.getText("playBuildingListPanel.tooltip.energyHeader"), Text.getText("playBuildingListPanel.tooltip.energy"));
         this.guiButtons.add(this.iconEnergy[i]);
         this.iconDirtyWater[i] = new GUIIcon("iconDirtyWater");
         this.iconDirtyWater[i]
            .setToolTip(Text.getText("playBuildingListPanel.tooltip.dirtyWaterHeader"), Text.getText("playBuildingListPanel.tooltip.dirtyWater"));
         this.guiButtons.add(this.iconDirtyWater[i]);
         this.iconWater[i] = new GUIIcon("iconWater");
         this.iconWater[i].setToolTip(Text.getText("playBuildingListPanel.tooltip.waterHeader"), Text.getText("playBuildingListPanel.tooltip.water"));
         this.guiButtons.add(this.iconWater[i]);
         this.iconGoldCoins[i] = new GUIIcon("iconGoldCoins");
         this.iconGoldCoins[i]
            .setToolTip(Text.getText("playBuildingListPanel.tooltip.goldCoinsHeader"), Text.getText("playBuildingListPanel.tooltip.goldCoins"));
         this.guiButtons.add(this.iconGoldCoins[i]);
         this.iconAmmo[i] = new GUIIcon("iconAmmo");
         this.iconAmmo[i].setToolTip(Text.getText("playBuildingListPanel.tooltip.ammoHeader"), Text.getText("playBuildingListPanel.tooltip.ammo"));
         this.guiButtons.add(this.iconAmmo[i]);
         this.priorityUp[i] = new GUIRoundButton("priorityUp" + i, "buttonUp", 0);
         this.priorityUp[i]
            .setToolTip(
               Text.getText("playBuildingListPanel.button.tooltip.increasePriorityHeader"),
               Text.getText("playBuildingListPanel.button.tooltip.increasePriority")
            );
         this.guiButtons.add(this.priorityUp[i]);
         this.priorityDown[i] = new GUIRoundButton("priorityDown" + i, "buttonDown", 0);
         this.priorityDown[i]
            .setToolTip(
               Text.getText("playBuildingListPanel.button.tooltip.decreasePriorityHeader"),
               Text.getText("playBuildingListPanel.button.tooltip.decreasePriority")
            );
         this.guiButtons.add(this.priorityDown[i]);
         this.priorityTop[i] = new GUIRoundButton("priorityTop" + i, "buttonTop", 0);
         this.priorityTop[i]
            .setToolTip(
               Text.getText("playBuildingListPanel.button.tooltip.topPriorityHeader"), Text.getText("playBuildingListPanel.button.tooltip.topPriority")
            );
         this.guiButtons.add(this.priorityTop[i]);
         this.priorityBottom[i] = new GUIRoundButton("priorityBottom" + i, "buttonBottom", 0);
         this.priorityBottom[i]
            .setToolTip(
               Text.getText("playBuildingListPanel.button.tooltip.lastPriorityHeader"), Text.getText("playBuildingListPanel.button.tooltip.lastPriority")
            );
         this.guiButtons.add(this.priorityBottom[i]);
         this.prioritySortOrder[i] = new GUIRoundButton("prioritySortOrder" + i, "buttonCircle", 0);
         this.prioritySortOrder[i]
            .setToolTip(
               Text.getText("playBuildingListPanel.button.tooltip.prioritySortOrderHeader"),
               Text.getText("playBuildingListPanel.button.tooltip.prioritySortOrder")
            );
         this.guiButtons.add(this.prioritySortOrder[i]);
      }

      this.buildingListGrouped = this.object.getObjectGroupedPriorityList();
   }

   public void render(boolean debug) throws SlickException {
      this.x = this.controller.getResourceAndInfluenceBar().getRightX() - this.panel.getWidth() + 170;
      if (this.x + this.panel.getWidth() > this.controller.getRightPanel().getLeftX() - 46) {
         this.x = this.x - Math.abs(this.x + this.panel.getWidth() - this.controller.getRightPanel().getLeftX() + 46);
      }

      this.y = 133;
      this.mask.setX(this.x);
      this.mask.setY(this.y);
      this.panel.draw(this.x, this.y);
      this.font.drawString(this.x + 135, this.y + 3, Text.getText("playBuildingListPanel.header"), Text.FontType.HEADER, 2, true);
      int totalPages = this.buildingListGrouped.size() / 12;
      if (this.buildingListGrouped.size() % 12 > 0) {
         totalPages++;
      }

      Text.setVariableText("currentPage", this.buildingsPage);
      Text.setVariableText("totalPages", totalPages);
      this.font.drawString(this.x + 36, this.y + 62, Text.getText("playBuildingListPanel.buildingListPage"), Text.FontType.BODY, 1);
      this.iconTotalBuildings.render(this.g, this.x + 23, this.y + 32, debug);
      this.font.drawString(this.x + 43, this.y + 32, this.object.getActiveBuildings() + "/" + this.object.getMaximumBuildings(), Text.FontType.BODY, 1);
      this.iconTotalAncillaries.render(this.g, this.x + 103, this.y + 32, debug);
      this.font.drawString(this.x + 123, this.y + 32, this.object.getActiveAncillaries() + "/" + this.object.getMaximumAncillaries(), Text.FontType.BODY, 1);
      this.iconTotalHomeOccupancy.render(this.g, this.x + 183, this.y + 32, debug);
      this.font.drawString(this.x + 203, this.y + 32, this.guiData.getOccupants() + "/" + this.guiData.getOccupantsMax(), Text.FontType.BODY, 1);
      this.iconTotalRangerLodgeOccupancy.render(this.g, this.x + 263, this.y + 32, debug);
      this.font
         .drawString(this.x + 283, this.y + 32, this.guiData.getRangerLodgeOccupants() + "/" + this.guiData.getRangerLodgeOccupantsMax(), Text.FontType.BODY, 1);
      this.iconTotalWorkers.render(this.g, this.x + 343, this.y + 32, debug);
      this.font.drawString(this.x + 363, this.y + 32, this.mobJob.getTotalCurrentWorkers() + "/" + this.mobJob.getTotalMaximumWorkers(), Text.FontType.BODY, 1);
      String[] buildingColor = new String[]{"$WHI0", "$WHI0"};
      if (this.buildingListGrouped.size() > 0) {
         for (int i = 0; i < 12; i++) {
            if (this.buildingsListStart + i >= this.buildingListGrouped.size()) {
               this.nameList[i].hideButton();
               this.priorityUp[i].hideButton();
               this.priorityDown[i].hideButton();
               this.priorityTop[i].hideButton();
               this.priorityBottom[i].hideButton();
               this.prioritySortOrder[i].hideButton();
            } else {
               ObjectBase building = this.buildingListGrouped.get(this.buildingsListStart + i).get(0);
               ObjectFlags objectFlags = building.getObjectFlags();
               if (objectFlags.getSubType() == ObjectBase.ObjectSubType.CONSTRUCTION || objectFlags.getSubType() == ObjectBase.ObjectSubType.DISMANTLE) {
                  buildingColor[0] = "$RED0";
                  buildingColor[1] = "$RED1";
               } else if (objectFlags.getSubType() == ObjectBase.ObjectSubType.ABANDONED) {
                  buildingColor[0] = "$YEL0";
                  buildingColor[1] = "$YEL1";
               } else if (objectFlags.getSubType() == ObjectBase.ObjectSubType.BUILT) {
                  buildingColor[0] = "$GRE0";
                  buildingColor[1] = "$GRE1";
               }

               int alignX = 0;
               int alignY = i * 18;
               this.priorityBottom[i].render(this.g, this.mouse, this.x + 20 + alignX, this.y + alignY + 80, debug);
               this.priorityDown[i].render(this.g, this.mouse, this.x + 38 + alignX, this.y + alignY + 80, debug);
               this.priorityUp[i].render(this.g, this.mouse, this.x + 56 + alignX, this.y + alignY + 80, debug);
               this.priorityTop[i].render(this.g, this.mouse, this.x + 74 + alignX, this.y + alignY + 80, debug);
               if (this.buildingListGrouped.get(this.buildingsListStart + i).size() > 1) {
                  this.prioritySortOrder[i].render(this.g, this.mouse, this.x + 92 + alignX, this.y + alignY + 80, debug);
               }

               if (this.buildingListGrouped.get(this.buildingsListStart + i).size() > 1) {
                  this.font
                     .drawString(
                        this.x + 115 + alignX,
                        this.y + alignY + 82,
                        objectFlags.getName()
                           + " ("
                           + this.buildingListGrouped.get(this.buildingsListStart + i).size()
                           + ", "
                           + this.object.getGroupSortOrder(building).getText()
                           + ")",
                        Text.FontType.BODY,
                        1
                     );
               } else if (building.isPaused()) {
                  this.nameList[i]
                     .render(
                        this.g,
                        this.mouse,
                        objectFlags.getName() + Text.getText("playBuildingListPanel.buildingPaused"),
                        this.x + 97 + alignX,
                        this.y + alignY + 82,
                        buildingColor[0],
                        buildingColor[1],
                        false,
                        false,
                        debug
                     );
               } else {
                  this.nameList[i]
                     .render(
                        this.g,
                        this.mouse,
                        objectFlags.getName(),
                        this.x + 97 + alignX,
                        this.y + alignY + 82,
                        buildingColor[0],
                        buildingColor[1],
                        false,
                        false,
                        debug
                     );
               }

               if (objectFlags.getWorkersProvided() > 0) {
                  this.iconWorkers[i].render(this.g, this.x + 250 + alignX, this.y + alignY + 82, debug);
                  this.font.drawString(this.x + 270 + alignX, this.y + alignY + 82, "" + objectFlags.getWorkersProvided(), Text.FontType.BODY, 1);
               }

               if (objectFlags.getOccupantsMax() > 0) {
                  if (objectFlags.getWorkersProvided() > 0) {
                     this.iconOccupancy[i].render(this.g, this.x + 300 + alignX, this.y + alignY + 82, debug);
                     this.font
                        .drawString(
                           this.x + 320 + alignX,
                           this.y + alignY + 82,
                           building.getOccupants().size() + "/" + objectFlags.getOccupantsMax(),
                           Text.FontType.BODY,
                           1
                        );
                  } else {
                     this.iconOccupancy[i].render(this.g, this.x + 250 + alignX, this.y + alignY + 82, debug);
                     this.font
                        .drawString(
                           this.x + 270 + alignX,
                           this.y + alignY + 82,
                           building.getOccupants().size() + "/" + objectFlags.getOccupantsMax(),
                           Text.FontType.BODY,
                           1
                        );
                  }
               }

               if (objectFlags.getEnergyMax() > 0) {
                  this.iconEnergy[i].render(this.g, this.x + 300 + alignX, this.y + alignY + 82, debug);
                  this.font
                     .drawString(this.x + 320 + alignX, this.y + alignY + 82, building.getEnergy() + "/" + objectFlags.getEnergyMax(), Text.FontType.BODY, 1);
               }

               int totalDirtyWater = building.getTotalDirtyWater();
               if (totalDirtyWater > 0) {
                  this.iconDirtyWater[i].render(this.g, this.x + 300 + alignX, this.y + alignY + 82, debug);
                  this.font.drawString(this.x + 320 + alignX, this.y + alignY + 82, String.valueOf(totalDirtyWater), Text.FontType.BODY, 1);
               }

               int totalWater = building.getTotalWater();
               if (totalWater > 0) {
                  this.iconWater[i].render(this.g, this.x + 300 + alignX, this.y + alignY + 82, debug);
                  this.font.drawString(this.x + 320 + alignX, this.y + alignY + 82, String.valueOf(totalWater), Text.FontType.BODY, 1);
               }

               int totalGoldCoins = building.getTotalGoldCoins();
               if (totalGoldCoins > 0) {
                  this.iconGoldCoins[i].render(this.g, this.x + 300 + alignX, this.y + alignY + 82, debug);
                  this.font.drawString(this.x + 320 + alignX, this.y + alignY + 82, String.valueOf(totalGoldCoins), Text.FontType.BODY, 1);
               }

               if (objectFlags.getAmmoMax() > 0) {
                  this.iconAmmo[i].render(this.g, this.x + 300 + alignX, this.y + alignY + 82, debug);
                  this.font.drawString(this.x + 320 + alignX, this.y + alignY + 82, building.getAmmo() + "/" + objectFlags.getAmmoMax(), Text.FontType.BODY, 1);
               }
            }
         }
      }

      this.listTop.render(this.g, this.mouse, this.x + 380, this.y + 63, debug);
      this.listUp.render(this.g, this.mouse, this.x + 380, this.y + 88, debug);
      this.listDown.render(this.g, this.mouse, this.x + 380, this.y + 246, debug);
      this.listBottom.render(this.g, this.mouse, this.x + 380, this.y + 271, debug);
   }

   public boolean intersectsBuildingsList() {
      if (!this.controller.isBuildingListPanelEnabled()) {
         return false;
      } else {
         for (int i = 0; i < this.nameList.length; i++) {
            if (this.nameList[i].intersects(this.mouse)) {
               return true;
            }
         }

         return false;
      }
   }

   public ObjectBase getSelectedBuildingsList() {
      if (!this.controller.isBuildingListPanelEnabled()) {
         return null;
      } else {
         for (int i = 0; i < this.nameList.length; i++) {
            if (this.nameList[i].intersects(this.mouse)) {
               return this.buildingListGrouped.get(this.buildingsListStart + i).get(0);
            }
         }

         return null;
      }
   }

   public boolean intersectsBuildingsPriorityUp() {
      if (!this.controller.isBuildingListPanelEnabled()) {
         return false;
      } else {
         for (int i = 0; i < this.priorityUp.length; i++) {
            if (this.priorityUp[i].intersects(this.mouse)) {
               return true;
            }
         }

         return false;
      }
   }

   public ObjectBase getSelectedBuildingsPriorityUp() {
      if (!this.controller.isBuildingListPanelEnabled()) {
         return null;
      } else {
         for (int i = 0; i < this.priorityUp.length; i++) {
            if (this.priorityUp[i].intersects(this.mouse)) {
               ObjectBase selectedObject = this.buildingListGrouped.get(this.buildingsListStart + i).get(0);
               if (i != 0) {
                  Platform.get().moveMouseBy(0, (int)(-ScaleControl.getInterfaceScale() * 9.0F));
               } else if (this.buildingsListStart != 0) {
                  Platform.get().moveMouseBy(0, (int)(ScaleControl.getInterfaceScale() * 11.0F * 9.0F));

                  this.setPageUp();
               }

               return selectedObject;
            }
         }

         return null;
      }
   }

   public boolean intersectsBuildingsPriorityDown() {
      if (!this.controller.isBuildingListPanelEnabled()) {
         return false;
      } else {
         for (int i = 0; i < this.priorityDown.length; i++) {
            if (this.priorityDown[i].intersects(this.mouse)) {
               return true;
            }
         }

         return false;
      }
   }

   public ObjectBase getSelectedBuildingsPriorityDown() {
      if (!this.controller.isBuildingListPanelEnabled()) {
         return null;
      } else {
         for (int i = 0; i < this.priorityDown.length; i++) {
            if (this.priorityDown[i].intersects(this.mouse)) {
               ObjectBase selectedObject = this.buildingListGrouped.get(this.buildingsListStart + i).get(0);
               if (i != 11 && this.buildingsListStart + i != this.buildingListGrouped.size() - 1) {
                  Platform.get().moveMouseBy(0, (int)(ScaleControl.getInterfaceScale() * 9.0F));
               } else if (this.buildingsListStart + 12 < this.buildingListGrouped.size()) {
                  Platform.get().moveMouseBy(0, (int)(-ScaleControl.getInterfaceScale() * 11.0F * 9.0F));

                  this.setPageDown();
               }

               return selectedObject;
            }
         }

         return null;
      }
   }

   public boolean intersectsBuildingsPriorityTop() {
      if (!this.controller.isBuildingListPanelEnabled()) {
         return false;
      } else {
         for (int i = 0; i < this.priorityTop.length; i++) {
            if (this.priorityTop[i].intersects(this.mouse)) {
               return true;
            }
         }

         return false;
      }
   }

   public ObjectBase getSelectedBuildingsPriorityTop() {
      if (!this.controller.isBuildingListPanelEnabled()) {
         return null;
      } else {
         for (int i = 0; i < this.priorityTop.length; i++) {
            if (this.priorityTop[i].intersects(this.mouse)) {
               return this.buildingListGrouped.get(this.buildingsListStart + i).get(0);
            }
         }

         return null;
      }
   }

   public boolean intersectsBuildingsPriorityBottom() {
      if (!this.controller.isBuildingListPanelEnabled()) {
         return false;
      } else {
         for (int i = 0; i < this.priorityBottom.length; i++) {
            if (this.priorityBottom[i].intersects(this.mouse)) {
               return true;
            }
         }

         return false;
      }
   }

   public ObjectBase getSelectedBuildingsPriorityBottom() {
      if (!this.controller.isBuildingListPanelEnabled()) {
         return null;
      } else {
         for (int i = 0; i < this.priorityBottom.length; i++) {
            if (this.priorityBottom[i].intersects(this.mouse)) {
               return this.buildingListGrouped.get(this.buildingsListStart + i).get(0);
            }
         }

         return null;
      }
   }

   public boolean intersectsPrioritySortOrder() {
      if (!this.controller.isBuildingListPanelEnabled()) {
         return false;
      } else {
         for (int i = 0; i < this.prioritySortOrder.length; i++) {
            if (this.prioritySortOrder[i].intersects(this.mouse)) {
               return true;
            }
         }

         return false;
      }
   }

   public ObjectBase getPrioritySortOrder() {
      if (!this.controller.isBuildingListPanelEnabled()) {
         return null;
      } else {
         for (int i = 0; i < this.prioritySortOrder.length; i++) {
            if (this.prioritySortOrder[i].intersects(this.mouse)) {
               return this.buildingListGrouped.get(this.buildingsListStart + i).get(0);
            }
         }

         return null;
      }
   }

   public void setPageTop() {
      this.buildingsListStart = 0;
      this.buildingsPage = 1;
   }

   public void setPageBottom() {
      if (this.buildingListGrouped.size() > 12) {
         this.buildingsPage = (this.buildingListGrouped.size() + 11) / 12;
         this.buildingsListStart = this.buildingsPage * 12 - 12;
      }
   }

   public void setPageUp() {
      if (this.buildingsListStart != 0) {
         this.buildingsListStart -= 12;
         this.buildingsPage--;
      }
   }

   public void setPageDown() {
      if (this.buildingsListStart + 12 < this.buildingListGrouped.size()) {
         this.buildingsListStart += 12;
         this.buildingsPage++;
      }
   }

   public ArrayList<ArrayList<ObjectBase>> getBuildingListGrouped() {
      return this.buildingListGrouped;
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
