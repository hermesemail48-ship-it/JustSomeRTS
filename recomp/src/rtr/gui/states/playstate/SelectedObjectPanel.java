package rtr.gui.states.playstate;

import java.util.ArrayList;
import java.util.EnumMap;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.gui.buttons.GUIButtonGenericWood;
import rtr.gui.buttons.GUIButtonResource;
import rtr.gui.buttons.GUIClickableArea;
import rtr.gui.buttons.GUIClickableText;
import rtr.gui.buttons.GUIIcon;
import rtr.gui.buttons.GUIRoundButton;
import rtr.gui.buttons.GUIToolTipOverlay;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.PlayStateGUIPanelBase;
import rtr.map.MapTilesLoader;
import rtr.missiles.MissileBase;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.objects.towerhead.TowerBase;
import rtr.objects.towerhead.towerheadflags.TowerHeadFlags;
import rtr.resources.ResourceModule;
import rtr.system.ScaleControl;

public class SelectedObjectPanel extends PlayStateGUIPanelBase {
   private Image panel;
   private Image workerPanel;
   private Image occupantPanel;
   private Image workerOccupantPanel;
   private Rectangle mask;
   private GUIIcon iconRange;
   private GUIIcon iconDesirability;
   private GUIIcon iconProvidedMigrationsPerDay;
   private GUIIcon iconProvidedWorkers;
   private GUIIcon iconDesiredWorkers;
   private GUIIcon iconCurrentWorkers;
   private GUIIcon iconUnemployedVillagers;
   private GUIIcon iconOccupancy;
   private GUIIcon iconRangerLodgeOccupancy;
   private GUIIcon iconStorage;
   private GUIIcon iconValue;
   private GUIIcon iconHitPoints;
   private GUIIcon iconEnergy;
   private GUIIcon iconDirtyWater;
   private GUIIcon iconWater;
   private GUIIcon iconGoldCoins;
   private GUIIcon iconKills;
   private GUIIcon iconCharge;
   private GUIIcon iconAmmo;
   private GUIClickableArea statRectangle;
   private EnumMap<ResourceModule.ResourceType, GUIButtonResource> resourceIcons = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIIcon> resourceValueIcons = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIIcon> harvestingIcons = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIIcon> refiningIcons = new EnumMap<>(ResourceModule.ResourceType.class);
   private GUIRoundButton desiredWorkerDecrease;
   private GUIRoundButton desiredWorkerIncrease;
   private GUIRoundButton priorityTop;
   private GUIRoundButton priorityUp;
   private GUIRoundButton priorityDown;
   private GUIRoundButton priorityBottom;
   private GUIRoundButton pause;
   private GUIRoundButton resume;
   private GUIRoundButton dismantle;
   private GUIClickableText buildingPriority;
   private GUIButtonGenericWood reclaim;
   private GUIButtonGenericWood upgrade;
   private GUIButtonGenericWood startUpgrade1;
   private GUIButtonGenericWood startUpgrade2;
   private GUIButtonGenericWood startUpgrade3;
   private GUIButtonGenericWood upgradeBack;
   private GUIButtonGenericWood dismantleYes;
   private GUIButtonGenericWood dismantleNo;
   private boolean objectPanelAttemptDismantle;
   private boolean objectPanelAttemptUpgrade;

   public SelectedObjectPanel(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.panel = ImageLoader.getImage("res/GUI/gameState/objectPanel.png");
      this.workerPanel = ImageLoader.getImage("res/GUI/gameState/objectPanelWorkerPanel.png");
      this.occupantPanel = ImageLoader.getImage("res/GUI/gameState/objectPanelOccupantPanel.png");
      this.workerOccupantPanel = ImageLoader.getImage("res/GUI/gameState/objectPanelWorkerOccupantPanel.png");
      this.mask = new Rectangle(0.0F, 0.0F, this.panel.getWidth(), this.panel.getHeight());
      this.masks.add(this.mask);
      this.iconRange = new GUIIcon("iconRange");
      this.iconRange.setToolTip(Text.getText("playSelectedObjectPanel.tooltip.buildRangeHeader"), Text.getText("playSelectedObjectPanel.tooltip.buildRange"));
      this.guiButtons.add(this.iconRange);
      this.iconDesirability = new GUIIcon("iconDesirability");
      this.iconDesirability
         .setToolTip(Text.getText("playSelectedObjectPanel.tooltip.landDesirabilityHeader"), Text.getText("playSelectedObjectPanel.tooltip.landDesirability"));
      this.guiButtons.add(this.iconDesirability);
      this.iconProvidedMigrationsPerDay = new GUIIcon("iconMigration");
      this.iconProvidedMigrationsPerDay
         .setToolTip(
            Text.getText("playSelectedObjectPanel.tooltip.providedDailyMigrationsHeader"),
            Text.getText("playSelectedObjectPanel.tooltip.providedDailyMigrations")
         );
      this.guiButtons.add(this.iconProvidedMigrationsPerDay);
      this.iconProvidedWorkers = new GUIIcon("iconWorkers");
      this.iconProvidedWorkers
         .setToolTip(
            Text.getText("playSelectedObjectPanel.tooltip.providedWorkersHeader"),
            Text.getText("playSelectedObjectPanel.tooltip.providedWorkers"),
            GUIToolTipOverlay.ToolTipPosition.LEFT
         );
      this.guiButtons.add(this.iconProvidedWorkers);
      this.iconDesiredWorkers = new GUIIcon("iconWorkers");
      this.iconDesiredWorkers
         .setToolTip(
            Text.getText("playSelectedObjectPanel.tooltip.desiredAndMaxWorkersHeader"),
            Text.getText("playSelectedObjectPanel.tooltip.desiredAndMaxWorkers"),
            GUIToolTipOverlay.ToolTipPosition.LEFT
         );
      this.guiButtons.add(this.iconDesiredWorkers);
      this.iconCurrentWorkers = new GUIIcon("iconWorkers");
      this.iconCurrentWorkers
         .setToolTip(
            Text.getText("playSelectedObjectPanel.tooltip.totalPlusEnrouteWorkersHeader"),
            Text.getText("playSelectedObjectPanel.tooltip.totalPlusEnrouteWorkers"),
            GUIToolTipOverlay.ToolTipPosition.LEFT
         );
      this.guiButtons.add(this.iconCurrentWorkers);
      this.iconUnemployedVillagers = new GUIIcon("iconVillager");
      this.iconUnemployedVillagers
         .setToolTip(
            Text.getText("playSelectedObjectPanel.tooltip.unemployedVillagersHeader"),
            Text.getText("playSelectedObjectPanel.tooltip.unemployedVillagers"),
            GUIToolTipOverlay.ToolTipPosition.LEFT
         );
      this.guiButtons.add(this.iconUnemployedVillagers);
      this.iconOccupancy = new GUIIcon("iconHome");
      this.iconOccupancy.setToolTip(Text.getText("playSelectedObjectPanel.tooltip.occupancyHeader"), Text.getText("playSelectedObjectPanel.tooltip.occupancy"));
      this.guiButtons.add(this.iconOccupancy);
      this.iconRangerLodgeOccupancy = new GUIIcon("iconRangerLodge");
      this.iconRangerLodgeOccupancy
         .setToolTip(
            Text.getText("playSelectedObjectPanel.tooltip.rangerLodgeOccupancyHeader"), Text.getText("playSelectedObjectPanel.tooltip.rangerLodgeOccupancy")
         );
      this.guiButtons.add(this.iconRangerLodgeOccupancy);
      this.iconStorage = new GUIIcon("iconChest");
      this.iconStorage
         .setToolTip(
            Text.getText("playSelectedObjectPanel.tooltip.currentAndMaxStorageHeader"), Text.getText("playSelectedObjectPanel.tooltip.currentAndMaxStorage")
         );
      this.guiButtons.add(this.iconStorage);
      this.iconValue = new GUIIcon("iconCivic");
      this.iconValue
         .setToolTip(
            Text.getText("playSelectedObjectPanel.tooltip.buildingResourceValueHeader"), Text.getText("playSelectedObjectPanel.tooltip.buildingResourceValue")
         );
      this.guiButtons.add(this.iconValue);
      this.iconHitPoints = new GUIIcon("iconHitPoints");
      this.iconHitPoints.setToolTip(Text.getText("playSelectedObjectPanel.tooltip.hitPointsHeader"), Text.getText("playSelectedObjectPanel.tooltip.hitPoints"));
      this.guiButtons.add(this.iconHitPoints);
      this.iconEnergy = new GUIIcon("iconEnergy");
      this.iconEnergy.setToolTip(Text.getText("playSelectedObjectPanel.tooltip.energyHeader"), Text.getText("playSelectedObjectPanel.tooltip.energy"));
      this.guiButtons.add(this.iconEnergy);
      this.iconWater = new GUIIcon("iconWater");
      this.iconWater.setToolTip(Text.getText("playSelectedObjectPanel.tooltip.waterHeader"), Text.getText("playSelectedObjectPanel.tooltip.water"));
      this.guiButtons.add(this.iconWater);
      this.iconGoldCoins = new GUIIcon("iconGoldCoins");
      this.iconGoldCoins.setToolTip(Text.getText("playSelectedObjectPanel.tooltip.goldCoinsHeader"), Text.getText("playSelectedObjectPanel.tooltip.goldCoins"));
      this.guiButtons.add(this.iconGoldCoins);
      this.iconKills = new GUIIcon(ResourceModule.ResourceType.IRON_SWORD);
      this.iconKills.setToolTip(Text.getText("playSelectedObjectPanel.tooltip.killsHeader"), Text.getText("playSelectedObjectPanel.tooltip.kills"));
      this.guiButtons.add(this.iconKills);
      this.iconDirtyWater = new GUIIcon("iconDirtyWater");
      this.iconDirtyWater
         .setToolTip(Text.getText("playSelectedObjectPanel.tooltip.dirtyWaterHeader"), Text.getText("playSelectedObjectPanel.tooltip.dirtyWater"));
      this.guiButtons.add(this.iconDirtyWater);
      this.iconCharge = new GUIIcon("iconEnergy");
      this.iconCharge.setToolTip(Text.getText("playSelectedObjectPanel.tooltip.chargeHeader"), Text.getText("playSelectedObjectPanel.tooltip.charge"));
      this.guiButtons.add(this.iconCharge);
      this.iconAmmo = new GUIIcon("iconAmmo");
      this.iconAmmo.setToolTip(Text.getText("playSelectedObjectPanel.tooltip.ammoHeader"), Text.getText("playSelectedObjectPanel.tooltip.ammo"));
      this.guiButtons.add(this.iconAmmo);
      this.statRectangle = new GUIClickableArea("statRectangle", 52, 12);
      this.guiButtons.add(this.statRectangle);

      ResourceModule.ResourceType[] var9;
      for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
         GUIButtonResource newResourceButton = new GUIButtonResource(type, "resourceIcons" + type.getTemplate().getName());
         Text.setVariableText("resourceName", type.getTemplate().getName());
         newResourceButton.setToolTip(Text.getText("playSelectedObjectPanel.tooltip.resourceStores"), type.getTemplate().getDescription());
         this.resourceIcons.put(type, newResourceButton);
         this.guiButtons.add(this.resourceIcons.get(type));
         GUIIcon newIcon = new GUIIcon(type);
         Text.setVariableText("resourceName", type.getTemplate().getName());
         newIcon.setToolTip(Text.getText("playSelectedObjectPanel.tooltip.resourceValues"), type.getTemplate().getDescription());
         this.resourceValueIcons.put(type, newIcon);
         this.guiButtons.add(this.resourceValueIcons.get(type));
         newIcon = new GUIIcon(type);
         newIcon.setToolTip(type.getTemplate().getNamePlural(), type.getTemplate().getDescription());
         this.harvestingIcons.put(type, newIcon);
         this.guiButtons.add(this.harvestingIcons.get(type));
         newIcon = new GUIIcon(type);
         newIcon.setToolTip(type.getTemplate().getNamePlural(), type.getTemplate().getDescription());
         this.refiningIcons.put(type, newIcon);
         this.guiButtons.add(this.refiningIcons.get(type));
      }

      this.desiredWorkerDecrease = new GUIRoundButton("desiredWorkerDecrease", "buttonMinus", 0);
      this.desiredWorkerDecrease
         .setToolTip(
            Text.getText("playSelectedObjectPanel.button.tooltip.reduceWorkersHeader"),
            Text.getText("playSelectedObjectPanel.button.tooltip.reduceWorkers"),
            GUIToolTipOverlay.ToolTipPosition.LEFT
         );
      this.guiButtons.add(this.desiredWorkerDecrease);
      this.desiredWorkerIncrease = new GUIRoundButton("desiredWorkerIncrease", "buttonPlus", 0);
      this.desiredWorkerIncrease
         .setToolTip(
            Text.getText("playSelectedObjectPanel.button.tooltip.requestWorkersHeader"),
            Text.getText("playSelectedObjectPanel.button.tooltip.requestWorkers"),
            GUIToolTipOverlay.ToolTipPosition.LEFT
         );
      this.guiButtons.add(this.desiredWorkerIncrease);
      this.priorityTop = new GUIRoundButton("priorityTop", "buttonTop", 1);
      this.priorityTop
         .setToolTip(
            Text.getText("playSelectedObjectPanel.button.tooltip.topPriorityHeader"),
            Text.getText("playSelectedObjectPanel.button.tooltip.topPriority"),
            GUIToolTipOverlay.ToolTipPosition.LEFT
         );
      this.guiButtons.add(this.priorityTop);
      this.priorityUp = new GUIRoundButton("priorityUp", "buttonUp", 1);
      this.priorityUp
         .setToolTip(
            Text.getText("playSelectedObjectPanel.button.tooltip.increasePriorityHeader"),
            Text.getText("playSelectedObjectPanel.button.tooltip.increasePriority"),
            GUIToolTipOverlay.ToolTipPosition.LEFT
         );
      this.guiButtons.add(this.priorityUp);
      this.priorityDown = new GUIRoundButton("priorityDown", "buttonDown", 1);
      this.priorityDown
         .setToolTip(
            Text.getText("playSelectedObjectPanel.button.tooltip.decreasePriorityHeader"),
            Text.getText("playSelectedObjectPanel.button.tooltip.decreasePriority"),
            GUIToolTipOverlay.ToolTipPosition.LEFT
         );
      this.guiButtons.add(this.priorityDown);
      this.priorityBottom = new GUIRoundButton("priorityBottom", "buttonBottom", 1);
      this.priorityBottom
         .setToolTip(
            Text.getText("playSelectedObjectPanel.button.tooltip.lastPriorityHeader"),
            Text.getText("playSelectedObjectPanel.button.tooltip.lastPriority"),
            GUIToolTipOverlay.ToolTipPosition.LEFT
         );
      this.guiButtons.add(this.priorityBottom);
      this.pause = new GUIRoundButton("pause", "buttonPause", 1);
      this.pause
         .setToolTip(
            Text.getText("playSelectedObjectPanel.button.tooltip.pauseBuildingHeader"),
            Text.getText("playSelectedObjectPanel.button.tooltip.pauseBuilding"),
            GUIToolTipOverlay.ToolTipPosition.LEFT
         );
      this.guiButtons.add(this.pause);
      this.resume = new GUIRoundButton("resume", "buttonResume", 1);
      this.resume.setToolTip(Text.getText("playSelectedObjectPanel.button.tooltip.resumeBuildingHeader"), GUIToolTipOverlay.ToolTipPosition.LEFT);
      this.guiButtons.add(this.resume);
      this.dismantle = new GUIRoundButton("dismantle", "buttonDismantle", 1);
      this.dismantle
         .setToolTip(
            Text.getText("playSelectedObjectPanel.button.tooltip.dismantleBuildingHeader"),
            Text.getText("playSelectedObjectPanel.button.tooltip.dismantleBuilding"),
            GUIToolTipOverlay.ToolTipPosition.LEFT
         );
      this.guiButtons.add(this.dismantle);
      this.buildingPriority = new GUIClickableText("buildingPriority", 1);
      this.buildingPriority
         .setToolTip(
            Text.getText("playSelectedObjectPanel.button.tooltip.buildingPriorityHeader"),
            Text.getText("playSelectedObjectPanel.button.tooltip.buildingPriority"),
            GUIToolTipOverlay.ToolTipPosition.LEFT
         );
      this.guiButtons.add(this.buildingPriority);
      this.reclaim = new GUIButtonGenericWood("reclaim", Text.getText("playSelectedObjectPanel.button.reclaim"));
      this.reclaim
         .setToolTip(Text.getText("playSelectedObjectPanel.button.tooltip.reclaimHeader"), Text.getText("playSelectedObjectPanel.button.tooltip.reclaim"));
      this.guiButtons.add(this.reclaim);
      this.upgrade = new GUIButtonGenericWood("upgrade", Text.getText("playSelectedObjectPanel.button.upgrade"));
      this.upgrade
         .setToolTip(Text.getText("playSelectedObjectPanel.button.tooltip.upgradeHeader"), Text.getText("playSelectedObjectPanel.button.tooltip.upgrade"));
      this.guiButtons.add(this.upgrade);
      this.startUpgrade1 = new GUIButtonGenericWood("startUpgrade1", Text.getText("playSelectedObjectPanel.button.upgrade"));
      this.guiButtons.add(this.startUpgrade1);
      this.startUpgrade2 = new GUIButtonGenericWood("startUpgrade2", Text.getText("playSelectedObjectPanel.button.upgrade"));
      this.guiButtons.add(this.startUpgrade2);
      this.startUpgrade3 = new GUIButtonGenericWood("startUpgrade3", Text.getText("playSelectedObjectPanel.button.upgrade"));
      this.guiButtons.add(this.startUpgrade3);
      this.upgradeBack = new GUIButtonGenericWood("upgradeBack", Text.getText("playSelectedObjectPanel.button.back"));
      this.guiButtons.add(this.upgradeBack);
      this.dismantleYes = new GUIButtonGenericWood("dismantleYes", Text.getText("playSelectedObjectPanel.button.yes"));
      this.guiButtons.add(this.dismantleYes);
      this.dismantleNo = new GUIButtonGenericWood("dismantleNo", Text.getText("playSelectedObjectPanel.button.no"));
      this.guiButtons.add(this.dismantleNo);
   }

   public void render(boolean debug) throws SlickException {
      this.x = ScaleControl.getInterfaceCenterX() - this.panel.getWidth() / 2;
      this.y = ScaleControl.getInterfaceHeight() - this.panel.getHeight() - 3;
      if (this.objectPanelAttemptUpgrade) {
         this.y += 150;
      }

      this.mask.setX(this.x);
      this.mask.setY(this.y);
      ObjectBase selectedObject = this.guiData.getSelectedObject();
      ObjectFlags selectedObjectFlags = this.guiData.getSelectedObject().getObjectFlags();
      if (!this.object.objectExists(selectedObject)) {
         this.controller.deselectAllSelected();
      } else {
         this.panel.draw(this.x, this.y);
         if (selectedObjectFlags.getSubType() == ObjectBase.ObjectSubType.CONSTRUCTION
            || selectedObjectFlags.getSubType() == ObjectBase.ObjectSubType.DISMANTLE) {
            this.font.drawString(this.x + 15, this.y + 9, "$RED1" + selectedObjectFlags.getName(), Text.FontType.HEADER, 2);
         } else if (selectedObjectFlags.getSubType() == ObjectBase.ObjectSubType.ABANDONED) {
            this.font.drawString(this.x + 15, this.y + 9, "$YEL1" + selectedObjectFlags.getName(), Text.FontType.HEADER, 2);
         } else if (selectedObjectFlags.getSubType() == ObjectBase.ObjectSubType.BUILT) {
            this.font.drawString(this.x + 15, this.y + 9, "$GRE1" + selectedObjectFlags.getName(), Text.FontType.HEADER, 2);
         }

         int row1ListY = 0;
         int row2ListY = 0;
         int row2ListX = 0;
         int row3ListX = 0;
         int row3ListY = 0;
         if (selectedObject.isInRange()) {
            int priority = -1;
            ArrayList<ArrayList<ObjectBase>> buildingListGrouped = this.controller.getBuildingListPanel().getBuildingListGrouped();

            for (ArrayList<ObjectBase> a : buildingListGrouped) {
               if (a.contains(selectedObject)) {
                  priority = buildingListGrouped.indexOf(a);
               }
            }

            if (selectedObjectFlags.getMobGroup() != MobBase.MobGroup.MONSTER) {
               this.priorityTop.render(this.g, this.mouse, this.x + 375, this.y + 10, debug);
               this.priorityUp.render(this.g, this.mouse, this.x + 395, this.y + 10, debug);
               if (priority >= buildingListGrouped.size() - 1) {
                  this.buildingPriority
                     .render(
                        this.g,
                        this.mouse,
                        Text.getText("playSelectedObjectPanel.bottomPriority"),
                        this.x + 430,
                        this.y + 12,
                        "$RED0",
                        "$RED1",
                        false,
                        true,
                        debug
                     );
               } else if (priority == 0) {
                  this.buildingPriority
                     .render(
                        this.g,
                        this.mouse,
                        Text.getText("playSelectedObjectPanel.topPriority"),
                        this.x + 430,
                        this.y + 12,
                        "$GRE0",
                        "$GRE1",
                        false,
                        true,
                        debug
                     );
               } else {
                  this.buildingPriority
                     .render(this.g, this.mouse, String.valueOf(priority + 1), this.x + 430, this.y + 12, "$YEL0", "$YEL1", false, true, debug);
               }

               this.priorityDown.render(this.g, this.mouse, this.x + 446, this.y + 10, debug);
               this.priorityBottom.render(this.g, this.mouse, this.x + 466, this.y + 10, debug);
            }
         }

         if (selectedObjectFlags.allowPause()) {
            if (selectedObject.isPaused()) {
               this.resume.render(this.g, this.mouse, this.x + 506, this.y + 10, debug);
            } else {
               this.pause.render(this.g, this.mouse, this.x + 506, this.y + 10, debug);
            }
         }

         if (selectedObject.isInRange() && selectedObjectFlags.allowDismantle() && selectedObjectFlags.getSubType() != ObjectBase.ObjectSubType.DISMANTLE) {
            this.dismantle.render(this.g, this.mouse, this.x + 526, this.y + 10, debug);
         }

         if (this.objectPanelAttemptUpgrade) {
            ArrayList<MapTilesLoader.TileSet> upgradeTypes = new ArrayList<>();
            if (selectedObjectFlags.getNextUpgradeType() != null) {
               upgradeTypes.add(selectedObjectFlags.getNextUpgradeType());
            }

            if (selectedObjectFlags.getNextUpgradeType2() != null) {
               upgradeTypes.add(selectedObjectFlags.getNextUpgradeType2());
            }

            if (selectedObjectFlags.getNextUpgradeType3() != null) {
               upgradeTypes.add(selectedObjectFlags.getNextUpgradeType3());
            }

            int upgradePanelWidth = this.controller.getSelectedObjectUpgradePanel1().getPanelWidth();
            int upgradePanelHeight = this.controller.getSelectedObjectUpgradePanel1().getPanelHeight();

            for (MapTilesLoader.TileSet t : upgradeTypes) {
               ObjectFlags nextUpgrade = this.object.getObjectFlagFactory().getObjectFlags(t, ObjectBase.ObjectSubType.BUILT);
               if (selectedObjectFlags.getNextUpgradeType() == t) {
                  int x2 = this.x - upgradePanelWidth / 2;
                  int y2 = this.y - upgradePanelHeight;
                  if (selectedObjectFlags.getNextUpgradeType2() != null && selectedObjectFlags.getNextUpgradeType3() == null) {
                     x2 = this.x - upgradePanelWidth / 2 + upgradePanelWidth / 2 + 14;
                     y2 = this.y - upgradePanelHeight;
                  }

                  if (selectedObjectFlags.getNextUpgradeType2() == null && selectedObjectFlags.getNextUpgradeType3() == null) {
                     x2 = this.x - upgradePanelWidth / 2 + upgradePanelWidth;
                     y2 = this.y - upgradePanelHeight;
                  }

                  this.controller.getSelectedObjectUpgradePanel1().render(x2, y2, nextUpgrade, debug);
                  if (selectedObject.isInRange()) {
                     this.startUpgrade1.render(this.g, this.mouse, x2 + 88, y2 + 500, false, debug);
                  }

                  this.upgradeBack.render(this.g, this.mouse, x2 + 22, y2 + 500, false, debug);
               }

               if (selectedObjectFlags.getNextUpgradeType2() == t) {
                  int x2x = this.x - upgradePanelWidth / 2 + (int)(upgradePanelWidth * 1.5F) + 14;
                  int y2x = this.y - upgradePanelHeight;
                  if (selectedObjectFlags.getNextUpgradeType3() != null) {
                     x2x = this.x - upgradePanelWidth / 2 + upgradePanelWidth;
                     y2x = this.y - upgradePanelHeight;
                  }

                  this.controller.getSelectedObjectUpgradePanel2().render(x2x, y2x, nextUpgrade, debug);
                  if (selectedObject.isInRange()) {
                     this.startUpgrade2.render(this.g, this.mouse, x2x + 88, y2x + 500, false, debug);
                  }
               }

               if (selectedObjectFlags.getNextUpgradeType3() == t) {
                  int x2xx = this.x - upgradePanelWidth / 2 + upgradePanelWidth * 2;
                  int y2xx = this.y - upgradePanelHeight;
                  this.controller.getSelectedObjectUpgradePanel3().render(x2xx, y2xx, nextUpgrade, debug);
                  if (selectedObject.isInRange()) {
                     this.startUpgrade3.render(this.g, this.mouse, x2xx + 88, y2xx + 500, false, debug);
                  }
               }
            }
         } else {
            if (selectedObject.isInRange() && selectedObjectFlags.canUpgrade()) {
               this.upgrade.render(this.g, this.mouse, this.x + 20, this.y + row1ListY + 36, false, debug);
               row1ListY += 32;
            }

            if (!selectedObject.isInRange() && selectedObjectFlags.getSubType() != ObjectBase.ObjectSubType.DISMANTLE) {
               this.font.drawString(this.x + 20, this.y + row1ListY + 34, Text.getText("playSelectedObjectPanel.notInRange"), Text.FontType.BODY, 1);
               row1ListY += 16;
            }

            if (selectedObjectFlags.getSubType() == ObjectBase.ObjectSubType.DISMANTLE) {
               this.font.drawString(this.x + 20, this.y + row1ListY + 34, Text.getText("playSelectedObjectPanel.dismantling"), Text.FontType.BODY, 1);
               row1ListY += 16;
            }

            if (selectedObject.getHitPointsMax() > 0) {
               this.iconHitPoints.render(this.g, this.x + 20, this.y + row1ListY + 33, debug);
               int barWidth = 50;
               if (selectedObject.getHitPoints() < selectedObject.getHitPointsMax()) {
                  barWidth = (int)Math.ceil((float)selectedObject.getHitPoints() / selectedObject.getHitPointsMax() * 50.0F);
               }

               this.g.setColor(new Color(0, 0, 0, 200));
               this.g.fillRect(this.x + 38, this.y + row1ListY + 35, 52.0F, 12.0F);
               if (barWidth > 37.5) {
                  this.g.setColor(new Color(0, 100, 0));
               } else if (barWidth > 25.0) {
                  this.g.setColor(new Color(100, 100, 0));
               } else if (barWidth > 12.5) {
                  this.g.setColor(new Color(100, 50, 0));
               } else {
                  this.g.setColor(new Color(100, 0, 0));
               }

               this.g.fillRect(this.x + 39, this.y + row1ListY + 36, barWidth, 10.0F);
               this.statRectangle.render(this.g, this.x + 38, this.y + row1ListY + 35, debug);
               if (debug || this.statRectangle.intersects(this.mouse)) {
                  this.font
                     .drawString(
                        this.x + 40, this.y + row1ListY + 34, selectedObject.getHitPoints() + "/" + selectedObject.getHitPointsMax(), Text.FontType.BODY, 0
                     );
               }

               row1ListY += 16;
            }

            if (selectedObjectFlags.getEnergyMax() > 0 || selectedObject.getEnergy() > 0) {
               this.iconEnergy.render(this.g, this.x + 20, this.y + row1ListY + 33, debug);
               int barWidthx = 50;
               if (selectedObject.getEnergy() < selectedObjectFlags.getEnergyMax()) {
                  barWidthx = (int)Math.ceil((float)selectedObject.getEnergy() / selectedObjectFlags.getEnergyMax() * 50.0F);
               }

               this.g.setColor(new Color(0, 0, 0, 200));
               this.g.fillRect(this.x + 38, this.y + row1ListY + 35, 52.0F, 12.0F);
               if (barWidthx > 37.5) {
                  this.g.setColor(new Color(0, 100, 0));
               } else if (barWidthx > 25.0) {
                  this.g.setColor(new Color(100, 100, 0));
               } else if (barWidthx > 12.5) {
                  this.g.setColor(new Color(100, 50, 0));
               } else {
                  this.g.setColor(new Color(100, 0, 0));
               }

               this.g.fillRect(this.x + 39, this.y + row1ListY + 36, barWidthx, 10.0F);
               this.statRectangle.render(this.g, this.x + 38, this.y + row1ListY + 35, debug);
               if (debug || this.statRectangle.intersects(this.mouse)) {
                  this.font
                     .drawString(
                        this.x + 40, this.y + row1ListY + 34, selectedObject.getEnergy() + "/" + selectedObjectFlags.getEnergyMax(), Text.FontType.BODY, 0
                     );
               }

               row1ListY += 16;
            }

            if (selectedObjectFlags.isCombobulator()) {
               this.iconCharge.render(this.g, this.x + 20, this.y + row1ListY + 33, debug);
               int barWidthxx = 50;
               if (selectedObject.getCombobulatorEnergyRequiredTick() < selectedObjectFlags.getCombobulatorEnergyRequired()) {
                  barWidthxx = (int)Math.ceil(
                     (float)selectedObject.getCombobulatorEnergyRequiredTick() / selectedObjectFlags.getCombobulatorEnergyRequired() * 50.0F
                  );
               }

               this.g.setColor(new Color(0, 0, 0, 200));
               this.g.fillRect(this.x + 38, this.y + row1ListY + 35, 52.0F, 12.0F);
               if (barWidthxx > 37.5) {
                  this.g.setColor(new Color(0, 100, 0));
               } else if (barWidthxx > 25.0) {
                  this.g.setColor(new Color(100, 100, 0));
               } else if (barWidthxx > 12.5) {
                  this.g.setColor(new Color(100, 50, 0));
               } else {
                  this.g.setColor(new Color(100, 0, 0));
               }

               this.g.fillRect(this.x + 39, this.y + row1ListY + 36, barWidthxx, 10.0F);
               this.statRectangle.render(this.g, this.x + 38, this.y + row1ListY + 35, debug);
               if (debug || this.statRectangle.intersects(this.mouse)) {
                  this.font
                     .drawString(
                        this.x + 40,
                        this.y + row1ListY + 34,
                        selectedObject.getCombobulatorEnergyRequiredTick() + "/" + selectedObjectFlags.getCombobulatorEnergyRequired(),
                        Text.FontType.BODY,
                        0
                     );
               }

               row1ListY += 16;
            }

            if (selectedObjectFlags.getWorkersProvided() > 0 && selectedObjectFlags.getBaseRange() > 0) {
               this.iconProvidedWorkers.render(this.g, this.x + 20, this.y + row1ListY + 33, debug);
               this.font.drawString(this.x + 40, this.y + row1ListY + 31, "" + selectedObjectFlags.getWorkersProvided(), Text.FontType.BODY, 2);
               this.iconRange.render(this.g, this.x + 60, this.y + row1ListY + 33, debug);
               this.font.drawString(this.x + 80, this.y + row1ListY + 31, "" + selectedObjectFlags.getBaseRange(), Text.FontType.BODY, 2);
               row1ListY += 16;
            } else {
               if (selectedObjectFlags.getWorkersProvided() > 0) {
                  this.iconProvidedWorkers.render(this.g, this.x + 20, this.y + row1ListY + 33, debug);
                  this.font.drawString(this.x + 40, this.y + row1ListY + 31, "" + selectedObjectFlags.getWorkersProvided(), Text.FontType.BODY, 2);
                  row1ListY += 16;
               }

               if (selectedObjectFlags.getBaseRange() > 0) {
                  this.iconRange.render(this.g, this.x + 20, this.y + row1ListY + 33, debug);
                  this.font.drawString(this.x + 40, this.y + row1ListY + 31, "" + selectedObjectFlags.getBaseRange(), Text.FontType.BODY, 2);
                  row1ListY += 16;
               }
            }

            this.iconDesirability.render(this.g, this.x + 20, this.y + row1ListY + 33, debug);
            this.font.drawString(this.x + 40, this.y + row1ListY + 31, "" + selectedObjectFlags.getBaseDesirability(), Text.FontType.BODY, 2);
            row1ListY += 16;
            if (selectedObjectFlags.getMigrationsPerDayProvided() > 0) {
               this.iconProvidedMigrationsPerDay.render(this.g, this.x + 20, this.y + row1ListY + 33, debug);
               this.font.drawString(this.x + 40, this.y + row1ListY + 31, "" + selectedObjectFlags.getMigrationsPerDayProvided(), Text.FontType.BODY, 2);
               row1ListY += 16;
            }

            int totalDirtyWater = selectedObject.getTotalDirtyWater();
            if (totalDirtyWater > 0) {
               this.iconDirtyWater.render(this.g, this.x + 100, this.y + row2ListY + 33, debug);
               this.font.drawString(this.x + 120, this.y + row2ListY + 31, String.valueOf(totalDirtyWater), Text.FontType.BODY, 2);
               row2ListY += 16;
            }

            int totalWater = selectedObject.getTotalWater();
            if (totalWater > 0) {
               this.iconWater.render(this.g, this.x + 100, this.y + row2ListY + 33, debug);
               this.font.drawString(this.x + 120, this.y + row2ListY + 31, String.valueOf(totalWater), Text.FontType.BODY, 2);
               row2ListY += 16;
            }

            int totalGoldCoins = selectedObject.getTotalGoldCoins();
            if (totalGoldCoins > 0) {
               this.iconGoldCoins.render(this.g, this.x + 100, this.y + row2ListY + 33, debug);
               this.font.drawString(this.x + 120, this.y + row2ListY + 31, String.valueOf(totalGoldCoins), Text.FontType.BODY, 2);
               row2ListY += 16;
            }

            if (selectedObject.getKills() > 0) {
               this.iconKills.render(this.g, this.x + 100, this.y + row2ListY + 33, debug);
               String killList = "";

               for (String s : selectedObject.getKilledNameList()) {
                  killList = killList + s + "~";
               }

               this.iconKills
                  .setToolTip(Text.getText("playSelectedMobPanel.tooltip.killsHeader"), Text.getText("playSelectedMobPanel.tooltip.kills") + "$RED1" + killList);
               this.font.drawString(this.x + 120, this.y + row2ListY + 31, String.valueOf(selectedObject.getKills()), Text.FontType.BODY, 2);
               row2ListY += 15;
            }

            if (selectedObjectFlags.getAmmoMax() > 0 || selectedObject.getAmmo() > 0) {
               this.iconAmmo.render(this.g, this.x + 100, this.y + row2ListY + 33, debug);
               int bundledAmmo = 0;

               for (ResourceModule.ResourceType t : selectedObjectFlags.getCanBuildingFire()) {
                  bundledAmmo += selectedObject.getResourceCount(t) * selectedObjectFlags.getAmmoMax();
               }

               this.font
                  .drawString(
                     this.x + 120,
                     this.y + row2ListY + 31,
                     selectedObject.getAmmo() + "/" + selectedObjectFlags.getAmmoMax() + " (+" + bundledAmmo + ")",
                     Text.FontType.BODY,
                     2
                  );
               row2ListY += 16;
            }

            if (selectedObjectFlags.getSubType() != ObjectBase.ObjectSubType.BUILT) {
               this.iconStorage.render(this.g, this.x + 100, this.y + row2ListY + 33, debug);
               this.font
                  .drawString(
                     this.x + 120,
                     this.y + row2ListY + 31,
                     "$GRE1" + selectedObject.getTotalResourcesCount() + "/" + (selectedObjectFlags.getTotalValueBase() - selectedObject.getTotalValue()),
                     Text.FontType.BODY,
                     2
                  );
               row2ListY += 16;

               ResourceModule.ResourceType[] var72;
               for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
                  if (selectedObjectFlags.getResourceValueBaseCount(type) - selectedObject.getResourceValueCount(type) > 0
                     || selectedObject.getResourceCount(type) > 0) {
                     this.resourceIcons.get(type).render(this.g, this.mouse, this.x + row2ListX + 100, this.y + row2ListY + 31, selectedObject, debug);
                     this.font
                        .drawString(
                           this.x + row2ListX + 118,
                           this.y + row2ListY + 35,
                           "$YEL1"
                              + selectedObject.getResourceCount(type)
                              + "/"
                              + (selectedObjectFlags.getResourceValueBaseCount(type) - selectedObject.getResourceValueCount(type)),
                           Text.FontType.BODY,
                           0
                        );
                     row2ListY += 12;
                     if (row2ListY >= 96) {
                        row2ListY = 14;
                        row2ListX += 64;
                     }
                  }
               }
            } else if (selectedObjectFlags.hasAnyStorage()) {
               this.iconStorage.render(this.g, this.x + 100, this.y + row2ListY + 33, debug);
               this.font.drawString(this.x + 120, this.y + row2ListY + 31, "$GRE1" + selectedObject.getTotalResourcesCount(), Text.FontType.BODY, 2);
               int row2ListYResourceStart = row2ListY;
               row2ListY += 16;

               ResourceModule.ResourceType[] var78;
               for (ResourceModule.ResourceType typex : ResourceModule.ResourceType.values()) {
                  if (selectedObjectFlags.canBuildingStore(typex) || selectedObject.getResourceCount(typex) > 0) {
                     this.resourceIcons.get(typex).render(this.g, this.mouse, this.x + row2ListX + 100, this.y + row2ListY + 31, selectedObject, debug);
                     int count = selectedObject.getResourceCount(typex);
                     int maximum = selectedObjectFlags.getResourceStorageMax().get(typex);
                     this.font.drawString(this.x + row2ListX + 115, this.y + row2ListY + 35, "$YEL1" + count + "/" + maximum, Text.FontType.BODY, 0);
                     row2ListY += 12;
                     if (row2ListY >= 96) {
                        row2ListY = 14 + row2ListYResourceStart;
                        row2ListX += 50;
                     }
                  }
               }
            }

            if (this.objectPanelAttemptDismantle) {
               int valueY = 0;
               this.iconValue.render(this.g, this.x + 320, this.y + valueY + 33, debug);
               this.font.drawString(this.x + 340, this.y + valueY + 31, "$YEL1" + selectedObject.getTotalValue(), Text.FontType.BODY, 2);
               valueY += 16;

               ResourceModule.ResourceType[] var79;
               for (ResourceModule.ResourceType typexx : ResourceModule.ResourceType.values()) {
                  if (selectedObject.getResourceValueCount(typexx) > 0) {
                     this.resourceValueIcons.get(typexx).render(this.g, this.x + 320, this.y + valueY + 31, debug);
                     int count = selectedObject.getResourceValueCount(typexx);
                     this.font.drawString(this.x + 337, this.y + valueY + 33, "$RED1" + count, Text.FontType.BODY, 1);
                     valueY += 12;
                  }
               }

               this.font
                  .drawString(
                     this.x + 467,
                     this.y + 40,
                     Text.wrapString(Text.getText("playSelectedObjectPanel.dismantlingWarning"), Text.FontType.BODY, 1, 175),
                     Text.FontType.BODY,
                     1,
                     true
                  );
               this.dismantleNo.render(this.g, this.mouse, this.x + 420, this.y + 115, debug, false);
               this.dismantleYes.render(this.g, this.mouse, this.x + 470, this.y + 115, debug, false);
            } else {
               if (selectedObject.getTower() != null) {
                  TowerHeadFlags selectedObjectTowerFlags = selectedObject.getTower().getTowerFlags();
                  MissileModule.MissileType towerMissileType = selectedObjectTowerFlags.getMissileType();
                  MissileModule.MissileType towerAltMissileType = selectedObjectTowerFlags.getAltMissileType();
                  MissileBase towerMissle = null;
                  if (towerMissileType != null) {
                     towerMissle = this.missile.getMissileFactory().getMissileInstance(towerMissileType);
                  }

                  MissileBase towerSubMissile = null;
                  if (towerAltMissileType != null) {
                     towerSubMissile = this.missile.getMissileFactory().getMissileInstance(towerAltMissileType);
                  }

                  if (selectedObjectTowerFlags.getTowerType() == TowerBase.TowerType.STATIC) {
                     this.font
                        .drawString(
                           this.x + 315, this.y + row3ListY + 31, Text.getText("playSelectedObjectPanel.staticTowerDamageOverride"), Text.FontType.BODY, 2
                        );
                     row3ListY += 16;
                     this.font
                        .drawString(
                           this.x + 315,
                           this.y + row3ListY + 31,
                           Text.getText("playSelectedObjectPanel.staticTowerDamageOverrideAmount"),
                           Text.FontType.BODY,
                           1
                        );
                     row3ListY += 12;
                  }

                  if (towerMissle != null && towerMissle.getDamageTypesMax().size() > 0) {
                     if (towerSubMissile != null) {
                        this.font
                           .drawString(this.x + 315, this.y + row3ListY + 31, Text.getText("playSelectedObjectPanel.chargedDamage"), Text.FontType.BODY, 2);
                     } else {
                        this.font.drawString(this.x + 315, this.y + row3ListY + 31, Text.getText("playSelectedObjectPanel.damage"), Text.FontType.BODY, 2);
                     }

                     row3ListY += 16;

                     for (MobBase.DamageType d : towerMissle.getDamageTypesMax().keySet()) {
                        this.font
                           .drawString(
                              this.x + 315,
                              this.y + row3ListY + 31,
                              "$GRE1" + d.getText() + " $RED0(" + towerMissle.getDamageTypesMin().get(d) + "-" + towerMissle.getDamageTypesMax().get(d) + ")",
                              Text.FontType.BODY,
                              1
                           );
                        row3ListY += 12;
                     }

                     row3ListY += 12;
                     if (towerSubMissile != null) {
                        this.font
                           .drawString(this.x + 315, this.y + row3ListY + 31, Text.getText("playSelectedObjectPanel.unchargedDamage"), Text.FontType.BODY, 2);
                        row3ListY += 16;

                        for (MobBase.DamageType d : towerSubMissile.getDamageTypesMax().keySet()) {
                           this.font
                              .drawString(
                                 this.x + 315,
                                 this.y + row3ListY + 31,
                                 "$GRE1"
                                    + d.getText()
                                    + " $RED0("
                                    + towerSubMissile.getDamageTypesMin().get(d)
                                    + "-"
                                    + towerSubMissile.getDamageTypesMax().get(d)
                                    + ")",
                                 Text.FontType.BODY,
                                 1
                              );
                           row3ListY += 12;
                        }

                        row3ListY += 12;
                     }
                  }
               }

               boolean hasGeneral = false;
               boolean hasHarvest = false;
               boolean hasRefine = false;
               boolean hasHousing = false;

               ResourceModule.ResourceType[] currentWorkers;
               for (ResourceModule.ResourceType t : currentWorkers = ResourceModule.ResourceType.values()) {
                  if (!hasGeneral
                     && (
                        selectedObjectFlags.getGlobalWorkSpeedReduction() > 0.0
                           || selectedObjectFlags.getProcessingTrashSpeedReduction() > 0.0
                           || selectedObjectFlags.getBuildingSpeedReduction() > 0.0
                           || selectedObjectFlags.getBuildingRoadSpeedReduction() > 0.0
                           || selectedObjectFlags.getDismantlingSpeedReduction() > 0.0
                           || selectedObjectFlags.getClearingSpeedReduction() > 0.0
                           || selectedObjectFlags.getRepairingSpeedReduction() > 0.0
                           || selectedObjectFlags.getRepairingRoadSpeedReduction() > 0.0
                           || selectedObjectFlags.getFarmingSpeedReduction() > 0.0
                           || selectedObjectFlags.getMedicSpeedReduction() > 0.0
                           || selectedObjectFlags.getPrayingSpeedReduction() > 0.0
                           || selectedObjectFlags.getCapturingSoulsSpeedReduction() > 0.0
                           || selectedObjectFlags.getTendAnimalsSpeedReduction() > 0.0
                     )) {
                     hasGeneral = true;
                  }

                  if (!hasHarvest && selectedObjectFlags.getHarvestingSpeedReduction(t) > 0.0) {
                     hasHarvest = true;
                  }

                  if (!hasRefine && selectedObjectFlags.getRefiningSpeedReduction(t) > 0.0) {
                     hasRefine = true;
                  }

                  if (!hasHousing
                     && (
                        selectedObjectFlags.getOccupantsSleepBonusPercentOff() > 0
                           || selectedObjectFlags.getOccupantsHealthRegenBonusTicks() > 0
                           || selectedObjectFlags.getOccupantsFaithBonus() > 0
                     )) {
                     hasHousing = true;
                  }

                  if (hasGeneral && hasHarvest && hasRefine && hasHousing) {
                     break;
                  }
               }

               if (hasGeneral || hasHarvest || hasRefine) {
                  this.font
                     .drawString(
                        this.x + 295, this.y + 35 + row3ListY, Text.getText("playSelectedObjectPanel.globalBonusesHeader"), Text.FontType.BODY, 1, false
                     );
               }

               if (hasGeneral) {
                  if (selectedObjectFlags.getGlobalWorkSpeedReduction() > 0.0) {
                     Text.setVariableText("amount", selectedObjectFlags.getGlobalWorkSpeedReduction());
                     this.font
                        .drawString(this.x + 295, this.y + 47 + row3ListY, Text.getText("playSelectedObjectPanel.globalSpeed"), Text.FontType.BODY, 1, false);
                     row3ListY += 12;
                  }

                  if (selectedObjectFlags.getProcessingTrashSpeedReduction() > 0.0) {
                     Text.setVariableText("amount", selectedObjectFlags.getProcessingTrashSpeedReduction());
                     this.font
                        .drawString(
                           this.x + 295, this.y + 47 + row3ListY, Text.getText("playSelectedObjectPanel.processingTrashSpeed"), Text.FontType.BODY, 1, false
                        );
                     row3ListY += 12;
                  }

                  if (selectedObjectFlags.getBuildingSpeedReduction() > 0.0) {
                     Text.setVariableText("amount", selectedObjectFlags.getBuildingSpeedReduction());
                     this.font
                        .drawString(this.x + 295, this.y + 47 + row3ListY, Text.getText("playSelectedObjectPanel.buildingSpeed"), Text.FontType.BODY, 1, false);
                     row3ListY += 12;
                  }

                  if (selectedObjectFlags.getBuildingRoadSpeedReduction() > 0.0) {
                     Text.setVariableText("amount", selectedObjectFlags.getBuildingRoadSpeedReduction());
                     this.font
                        .drawString(this.x + 295, this.y + 47 + row3ListY, Text.getText("playSelectedObjectPanel.buildRoadSpeed"), Text.FontType.BODY, 1, false);
                     row3ListY += 12;
                  }

                  if (selectedObjectFlags.getDismantlingSpeedReduction() > 0.0) {
                     Text.setVariableText("amount", selectedObjectFlags.getDismantlingSpeedReduction());
                     this.font
                        .drawString(
                           this.x + 295, this.y + 47 + row3ListY, Text.getText("playSelectedObjectPanel.dismantlingSpeed"), Text.FontType.BODY, 1, false
                        );
                     row3ListY += 12;
                  }

                  if (selectedObjectFlags.getClearingSpeedReduction() > 0.0) {
                     Text.setVariableText("amount", selectedObjectFlags.getClearingSpeedReduction());
                     this.font
                        .drawString(this.x + 295, this.y + 47 + row3ListY, Text.getText("playSelectedObjectPanel.clearingSpeed"), Text.FontType.BODY, 1, false);
                     row3ListY += 12;
                  }

                  if (selectedObjectFlags.getRepairingSpeedReduction() > 0.0) {
                     Text.setVariableText("amount", selectedObjectFlags.getRepairingSpeedReduction());
                     this.font
                        .drawString(this.x + 295, this.y + 47 + row3ListY, Text.getText("playSelectedObjectPanel.repairingSpeed"), Text.FontType.BODY, 1, false);
                     row3ListY += 12;
                  }

                  if (selectedObjectFlags.getRepairingRoadSpeedReduction() > 0.0) {
                     Text.setVariableText("amount", selectedObjectFlags.getRepairingRoadSpeedReduction());
                     this.font
                        .drawString(
                           this.x + 295, this.y + 47 + row3ListY, Text.getText("playSelectedObjectPanel.repairRoadSpeed"), Text.FontType.BODY, 1, false
                        );
                     row3ListY += 12;
                  }

                  if (selectedObjectFlags.getFarmingSpeedReduction() > 0.0) {
                     Text.setVariableText("amount", selectedObjectFlags.getFarmingSpeedReduction());
                     this.font
                        .drawString(this.x + 295, this.y + 47 + row3ListY, Text.getText("playSelectedObjectPanel.farmingSpeed"), Text.FontType.BODY, 1, false);
                     row3ListY += 12;
                  }

                  if (selectedObjectFlags.getMedicSpeedReduction() > 0.0) {
                     Text.setVariableText("amount", selectedObjectFlags.getMedicSpeedReduction());
                     this.font
                        .drawString(this.x + 295, this.y + 47 + row3ListY, Text.getText("playSelectedObjectPanel.medicWorkSpeed"), Text.FontType.BODY, 1, false);
                     row3ListY += 12;
                  }

                  if (selectedObjectFlags.getPrayingSpeedReduction() > 0.0) {
                     Text.setVariableText("amount", selectedObjectFlags.getPrayingSpeedReduction());
                     this.font
                        .drawString(this.x + 295, this.y + 47 + row3ListY, Text.getText("playSelectedObjectPanel.prayingSpeed"), Text.FontType.BODY, 1, false);
                     row3ListY += 12;
                  }

                  if (selectedObjectFlags.getCapturingSoulsSpeedReduction() > 0.0) {
                     Text.setVariableText("amount", selectedObjectFlags.getCapturingSoulsSpeedReduction());
                     this.font
                        .drawString(
                           this.x + 295, this.y + 47 + row3ListY, Text.getText("playSelectedObjectPanel.capturingSoulsSpeed"), Text.FontType.BODY, 1, false
                        );
                     row3ListY += 12;
                  }

                  if (selectedObjectFlags.getTendAnimalsSpeedReduction() > 0.0) {
                     Text.setVariableText("amount", selectedObjectFlags.getTendAnimalsSpeedReduction());
                     this.font
                        .drawString(
                           this.x + 295, this.y + 47 + row3ListY, Text.getText("playSelectedObjectPanel.tendingAnimalsSpeed"), Text.FontType.BODY, 1, false
                        );
                     row3ListY += 12;
                  }

                  row3ListY += 11;
               }

               if (hasHarvest) {
                  this.font
                     .drawString(this.x + 295, this.y + 47 + row3ListY, Text.getText("playSelectedObjectPanel.harvestingSpeed"), Text.FontType.BODY, 1, false);
                  row3ListY += 11;

                  for (ResourceModule.ResourceType t : currentWorkers = ResourceModule.ResourceType.values()) {
                     if (selectedObjectFlags.getHarvestingSpeedReduction(t) > 0.0) {
                        this.harvestingIcons.get(t).render(this.g, this.x + row3ListX + 295, this.y + 47 + row3ListY, debug);
                        this.font
                           .drawString(
                              this.x + row3ListX + 312,
                              this.y + 47 + row3ListY,
                              "$GRE0+" + (int)selectedObjectFlags.getHarvestingSpeedReduction(t) + "%",
                              Text.FontType.BODY,
                              1,
                              false
                           );
                        row3ListX += 52;
                        if (row3ListX > 52) {
                           row3ListX = 0;
                           row3ListY += 13;
                        }
                     }
                  }

                  row3ListY += 39;
               }

               int var30 = 0;
               if (hasRefine) {
                  this.font
                     .drawString(this.x + 295, this.y + 47 + row3ListY, Text.getText("playSelectedObjectPanel.refiningSpeed"), Text.FontType.BODY, 1, false);
                  row3ListY += 11;

                  for (ResourceModule.ResourceType tx : currentWorkers = ResourceModule.ResourceType.values()) {
                     if (selectedObjectFlags.getRefiningSpeedReduction(tx) > 0.0) {
                        this.refiningIcons.get(tx).render(this.g, this.x + var30 + 295, this.y + 47 + row3ListY, debug);
                        this.font
                           .drawString(
                              this.x + var30 + 312,
                              this.y + 47 + row3ListY,
                              "$GRE0+" + (int)selectedObjectFlags.getRefiningSpeedReduction(tx) + "%",
                              Text.FontType.BODY,
                              1,
                              false
                           );
                        var30 += 52;
                        if (var30 > 52) {
                           var30 = 0;
                           row3ListY += 13;
                        }
                     }
                  }

                  row3ListY += 39;
               }

               if (hasHousing) {
                  this.font
                     .drawString(this.x + 295, this.y + 35 + row3ListY, Text.getText("playSelectedObjectPanel.homeBonusesHeader"), Text.FontType.BODY, 1, false);
               }

               if (hasHousing) {
                  if (selectedObjectFlags.getOccupantsSleepBonusPercentOff() > 0) {
                     Text.setVariableText("amount", selectedObjectFlags.getOccupantsSleepBonusPercentOff());
                     this.font
                        .drawString(
                           this.x + 295, this.y + 47 + row3ListY, Text.getText("playSelectedObjectPanel.sleepSpeedBoost"), Text.FontType.BODY, 1, false
                        );
                     row3ListY += 12;
                  }

                  if (selectedObjectFlags.getOccupantsHealthRegenBonusTicks() > 0) {
                     Text.setVariableText("amount", selectedObjectFlags.getOccupantsHealthRegenBonusTicks());
                     this.font
                        .drawString(
                           this.x + 295, this.y + 47 + row3ListY, Text.getText("playSelectedObjectPanel.healthRegenerationBoost"), Text.FontType.BODY, 1, false
                        );
                     row3ListY += 12;
                  }

                  if (selectedObjectFlags.getOccupantsFaithBonus() > 0) {
                     Text.setVariableText("amount", selectedObjectFlags.getOccupantsFaithBonus());
                     this.font
                        .drawString(this.x + 295, this.y + 47 + row3ListY, Text.getText("playSelectedObjectPanel.faithBonus"), Text.FontType.BODY, 1, false);
                     row3ListY += 12;
                  }
               }

               MobJobBase.MobJobType objectJobType = selectedObjectFlags.getWorkerJobType();
               int desiredWorkers = this.mobJob.getDesiredWorkers(objectJobType);
               int maximumWorkers = this.mobJob.getMaximumWorkers(objectJobType);
               int currentWorkersx = this.mobJob.getCurrentWorkers(objectJobType);
               int currentWorkersEnroute = this.mobJob.getCurrentWorkers(objectJobType, true) - currentWorkersx;
               String currentWorkersColor = "$GRE1";
               if (currentWorkersx < desiredWorkers * 0.4F) {
                  currentWorkersColor = "$RED1";
               } else if (currentWorkersx < desiredWorkers * 0.6F) {
                  currentWorkersColor = "$ORA1";
               } else if (currentWorkersx < desiredWorkers * 0.8F) {
                  currentWorkersColor = "$YEL1";
               }

               if (objectJobType != MobJobBase.MobJobType.NONE && selectedObjectFlags.getOccupantsMax() > 0) {
                  this.desiredWorkerDecrease.render(this.g, this.mouse, this.x + 420, this.y + 33, debug);
                  this.desiredWorkerIncrease.render(this.g, this.mouse, this.x + 440, this.y + 33, debug);
                  ArrayList<MobBase> villagers = this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false);
                  int totalWorkers = 0;

                  for (MobBase m : villagers) {
                     if (m.getMobFlags().getAllowWork()) {
                        totalWorkers++;
                     }
                  }

                  String currentDesiredColor = "$RED1";
                  if (this.mobJob.getTotalDesiredWorkers() <= totalWorkers) {
                     currentDesiredColor = "$GRE1";
                  }

                  this.iconDesiredWorkers.render(this.g, this.x + 460, this.y + 35, debug);
                  this.font.drawString(this.x + 480, this.y + 32, currentDesiredColor + desiredWorkers + "$WHI0/" + maximumWorkers, Text.FontType.BODY, 2);
                  this.iconUnemployedVillagers.render(this.g, this.x + 420, this.y + 55, debug);
                  this.font.drawString(this.x + 440, this.y + 52, "$YEL1" + this.mobJob.getUnassignedVillagers(), Text.FontType.BODY, 2);
                  String currentWorkersEnrouteFormatted = "";
                  if (currentWorkersEnroute > 0) {
                     currentWorkersEnrouteFormatted = "$GRE1+" + currentWorkersEnroute;
                  }

                  if (currentWorkersEnroute < 0) {
                     currentWorkersEnrouteFormatted = "$RED1" + currentWorkersEnroute;
                  }

                  this.iconCurrentWorkers.render(this.g, this.x + 480, this.y + 55, debug);
                  this.font
                     .drawString(this.x + 500, this.y + 52, currentWorkersColor + currentWorkersx + currentWorkersEnrouteFormatted, Text.FontType.BODY, 2);
                  if (selectedObjectFlags.getOccupantsJob() == MobJobBase.MobJobType.RANGER) {
                     this.iconRangerLodgeOccupancy.render(this.g, this.x + 420, this.y + 102, debug);
                  } else {
                     this.iconOccupancy.render(this.g, this.x + 420, this.y + 102, debug);
                  }

                  this.font
                     .drawString(
                        this.x + 440, this.y + 99, selectedObject.getOccupants().size() + "/" + selectedObjectFlags.getOccupantsMax(), Text.FontType.BODY, 2
                     );
                  this.renderWorkersAndOccupants(
                     this.g, this.x + 420, this.y + 75, this.mouse, selectedObjectFlags.getWorkerJobType(), selectedObject.getOccupants(), debug
                  );
               } else if (objectJobType != MobJobBase.MobJobType.NONE && selectedObjectFlags.getCombobulatorMax() > 0) {
                  this.desiredWorkerDecrease.render(this.g, this.mouse, this.x + 420, this.y + 33, debug);
                  this.desiredWorkerIncrease.render(this.g, this.mouse, this.x + 440, this.y + 33, debug);
                  ArrayList<MobBase> villagers = this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false);
                  int totalWorkers = 0;

                  for (MobBase mx : villagers) {
                     if (mx.getMobFlags().getAllowWork()) {
                        totalWorkers++;
                     }
                  }

                  String currentDesiredColorx = "$RED1";
                  if (this.mobJob.getTotalDesiredWorkers() <= totalWorkers) {
                     currentDesiredColorx = "$GRE1";
                  }

                  this.iconDesiredWorkers.render(this.g, this.x + 460, this.y + 35, debug);
                  this.font.drawString(this.x + 480, this.y + 32, currentDesiredColorx + desiredWorkers + "$WHI0/" + maximumWorkers, Text.FontType.BODY, 2);
                  this.iconUnemployedVillagers.render(this.g, this.x + 420, this.y + 55, debug);
                  this.font.drawString(this.x + 440, this.y + 52, "$YEL1" + this.mobJob.getUnassignedVillagers(), Text.FontType.BODY, 2);
                  String currentWorkersEnrouteFormattedx = "";
                  if (currentWorkersEnroute > 0) {
                     currentWorkersEnrouteFormattedx = "$GRE1+" + currentWorkersEnroute;
                  }

                  if (currentWorkersEnroute < 0) {
                     currentWorkersEnrouteFormattedx = "$RED1" + currentWorkersEnroute;
                  }

                  this.iconCurrentWorkers.render(this.g, this.x + 480, this.y + 55, debug);
                  this.font
                     .drawString(this.x + 500, this.y + 52, currentWorkersColor + currentWorkersx + currentWorkersEnrouteFormattedx, Text.FontType.BODY, 2);
                  if (selectedObjectFlags.getOccupantsJob() == MobJobBase.MobJobType.RANGER) {
                     this.iconRangerLodgeOccupancy.render(this.g, this.x + 420, this.y + 102, debug);
                  } else {
                     this.iconOccupancy.render(this.g, this.x + 420, this.y + 102, debug);
                  }

                  this.font
                     .drawString(
                        this.x + 440,
                        this.y + 99,
                        selectedObject.getCombobulatorSpawns().size() + "/" + selectedObjectFlags.getCombobulatorMax(),
                        Text.FontType.BODY,
                        2
                     );
                  this.renderWorkersAndOccupants(
                     this.g, this.x + 420, this.y + 75, this.mouse, selectedObjectFlags.getWorkerJobType(), selectedObject.getCombobulatorSpawns(), debug
                  );
               } else if (objectJobType == MobJobBase.MobJobType.NONE) {
                  if (selectedObjectFlags.getOccupantsMax() > 0) {
                     if (selectedObjectFlags.getOccupantsJob() == MobJobBase.MobJobType.RANGER) {
                        this.iconRangerLodgeOccupancy.render(this.g, this.x + 420, this.y + 35, debug);
                     } else {
                        this.iconOccupancy.render(this.g, this.x + 420, this.y + 35, debug);
                     }

                     this.font
                        .drawString(
                           this.x + 440, this.y + 32, selectedObject.getOccupants().size() + "/" + selectedObjectFlags.getOccupantsMax(), Text.FontType.BODY, 2
                        );
                     this.renderOccupants(this.g, this.x + 420, this.y + 55, this.mouse, selectedObject.getOccupants(), debug);
                  } else if (selectedObjectFlags.getCombobulatorMax() > 0) {
                     if (selectedObjectFlags.getOccupantsJob() == MobJobBase.MobJobType.RANGER) {
                        this.iconRangerLodgeOccupancy.render(this.g, this.x + 420, this.y + 35, debug);
                     } else {
                        this.iconOccupancy.render(this.g, this.x + 420, this.y + 35, debug);
                     }

                     this.font
                        .drawString(
                           this.x + 440,
                           this.y + 32,
                           selectedObject.getCombobulatorSpawns().size() + "/" + selectedObjectFlags.getCombobulatorMax(),
                           Text.FontType.BODY,
                           2
                        );
                     this.renderOccupants(this.g, this.x + 420, this.y + 55, this.mouse, selectedObject.getCombobulatorSpawns(), debug);
                  }
               } else {
                  this.desiredWorkerDecrease.render(this.g, this.mouse, this.x + 420, this.y + 33, debug);
                  this.desiredWorkerIncrease.render(this.g, this.mouse, this.x + 440, this.y + 33, debug);
                  ArrayList<MobBase> villagers = this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false);
                  int totalWorkers = 0;

                  for (MobBase mxx : villagers) {
                     if (mxx.getMobFlags().getAllowWork()) {
                        totalWorkers++;
                     }
                  }

                  String currentDesiredColorxx = "$RED1";
                  if (this.mobJob.getTotalDesiredWorkers() <= totalWorkers) {
                     currentDesiredColorxx = "$GRE1";
                  }

                  this.iconDesiredWorkers.render(this.g, this.x + 460, this.y + 35, debug);
                  this.font.drawString(this.x + 480, this.y + 32, currentDesiredColorxx + desiredWorkers + "$WHI0/" + maximumWorkers, Text.FontType.BODY, 2);
                  this.iconUnemployedVillagers.render(this.g, this.x + 420, this.y + 55, debug);
                  this.font.drawString(this.x + 440, this.y + 52, "$YEL1" + this.mobJob.getUnassignedVillagers(), Text.FontType.BODY, 2);
                  String currentWorkersEnrouteFormattedxx = "";
                  if (currentWorkersEnroute > 0) {
                     currentWorkersEnrouteFormattedxx = "$GRE1+" + currentWorkersEnroute;
                  }

                  if (currentWorkersEnroute < 0) {
                     currentWorkersEnrouteFormattedxx = "$RED1" + currentWorkersEnroute;
                  }

                  this.iconCurrentWorkers.render(this.g, this.x + 480, this.y + 55, debug);
                  this.font
                     .drawString(this.x + 500, this.y + 52, currentWorkersColor + currentWorkersx + currentWorkersEnrouteFormattedxx, Text.FontType.BODY, 2);
                  this.renderWorkers(this.g, this.x + 420, this.y + 75, this.mouse, selectedObjectFlags.getWorkerJobType(), debug);
               }

               if (selectedObjectFlags.getSubType() == ObjectBase.ObjectSubType.ABANDONED && selectedObject.isPaused() && selectedObject.isInRange()) {
                  if (this.object.canBuildNewBuilding()) {
                     this.font.drawString(this.x + 467, this.y + 50, Text.getText("playSelectedObjectPanel.reclaimMessage"), Text.FontType.BODY, 2, true);
                     this.reclaim.render(this.g, this.mouse, this.x + 430, this.y + 100, false, debug);
                  } else {
                     ObjectBase castle = this.object.getFirstBaseTypeFound(MapTilesLoader.TileSet.CASTLE_1);
                     if (castle == null) {
                        this.font
                           .drawString(
                              this.x + 467, this.y + 40, Text.getText("playSelectedObjectPanel.reclaimMessageNoSlotsNoCamp"), Text.FontType.BODY, 1, true
                           );
                     } else {
                        Text.setVariableText("castleName", castle.getFormattedName());
                        this.font
                           .drawString(this.x + 467, this.y + 40, Text.getText("playSelectedObjectPanel.reclaimMessageNoSlots"), Text.FontType.BODY, 1, true);
                     }

                     this.reclaim.render(this.g, this.mouse, this.x + 430, this.y + 100, true, debug);
                  }
               }
            }
         }
      }
   }

   private void renderWorkers(Graphics g, int x, int y, Rectangle mouse, MobJobBase.MobJobType jobType, boolean debug) throws SlickException {
      ArrayList<MobBase> workerList = this.mob.getMobsByJob(jobType, false);
      this.workerPanel.draw(x, y);
      int mobAlignX = 0;
      int mobAlignY = 0;
      int count = 0;

      for (MobBase m : workerList) {
         if (count >= 47) {
            this.font.drawString(x + 80, y + 46, "+" + (workerList.size() - 47), Text.FontType.BODY, 1);
            break;
         }

         count++;
         m.renderSprite(g, x + 5 + mobAlignX, y + 5 + mobAlignY, debug);
         mobAlignX += 10;
         if (count % 10 == 0) {
            mobAlignX = 0;
            mobAlignY += 10;
         }
      }
   }

   private void renderOccupants(Graphics g, int x, int y, Rectangle mouse, ArrayList<MobBase> occupantList, boolean debug) throws SlickException {
      this.occupantPanel.draw(x, y);
      int mobAlignX = 0;
      int mobAlignY = 0;
      int count = 0;

      for (MobBase m : occupantList) {
         if (count >= 67) {
            this.font.drawString(x + 80, y + 66, "+" + (occupantList.size() - 67), Text.FontType.BODY, 1);
            break;
         }

         count++;
         m.renderSprite(g, x + 5 + mobAlignX, y + 5 + mobAlignY, debug);
         mobAlignX += 10;
         if (count % 10 == 0) {
            mobAlignX = 0;
            mobAlignY += 10;
         }
      }
   }

   private void renderWorkersAndOccupants(
      Graphics g, int x, int y, Rectangle mouse, MobJobBase.MobJobType jobType, ArrayList<MobBase> occupantList, boolean debug
   ) throws SlickException {
      this.workerOccupantPanel.draw(x, y);
      ArrayList<MobBase> workerList = this.mob.getMobsByJob(jobType, false);
      int mobAlignX = 0;
      int mobAlignY = 0;
      int count = 0;

      for (MobBase m : workerList) {
         if (count >= 11) {
            this.font.drawString(x + 100, y + 6, "+" + (workerList.size() - 11), Text.FontType.BODY, 1);
            break;
         }

         count++;
         m.renderSprite(g, x + 5 + mobAlignX, y + 5 + mobAlignY, debug);
         mobAlignX += 8;
      }

      int alignY = 44;
      this.workerOccupantPanel.draw(x, y + alignY);
      int var15 = 0;
      int var16 = 0;
      count = 0;

      for (MobBase m : occupantList) {
         if (count >= 11) {
            this.font.drawString(x + 100, y + alignY + 6, "+" + (occupantList.size() - 11), Text.FontType.BODY, 1);
            break;
         }

         count++;
         m.renderSprite(g, x + 5 + var15, y + 5 + alignY + var16, debug);
         var15 += 8;
      }
   }

   public void setObjectPanelAttemptDismantle(boolean b) {
      this.objectPanelAttemptDismantle = b;
   }

   public void setObjectPanelAttemptUpgrade(boolean b) {
      this.objectPanelAttemptUpgrade = b;
   }

   public void resetAttemptDismantle() {
      this.objectPanelAttemptDismantle = false;
   }

   public void resetAttemptUpgrade() {
      this.objectPanelAttemptUpgrade = false;
   }

   public boolean isAttemptDismantle() {
      return this.objectPanelAttemptDismantle;
   }

   public boolean isAttemptUpgrade() {
      return this.objectPanelAttemptUpgrade;
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
