package rtr.gui.states.playstate;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.TimeModule;
import rtr.font.Text;
import rtr.gui.GUIEnums;
import rtr.gui.buttons.GUIButtonHide;
import rtr.gui.buttons.GUIButtonPage;
import rtr.gui.buttons.GUIClickableArea;
import rtr.gui.buttons.GUIIcon;
import rtr.gui.buttons.GUIOvalButton;
import rtr.gui.buttons.GUIPanelButton;
import rtr.gui.buttons.GUIRoundButton;
import rtr.gui.buttons.GUIRoundMobJobColorButton;
import rtr.gui.buttons.GUISquarePanelButton;
import rtr.gui.states.GUIControllerBase;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.PlayStateGUIPanelBase;
import rtr.map.MapTilesLoader;
import rtr.mobs.MigrationModule;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;
import rtr.states.StateBase;
import rtr.system.ScaleControl;
import rtr.utilities.Assignment;
import rtr.utilities.Utilities;

public class LeftPanel extends PlayStateGUIPanelBase {
   private Rectangle mask;
   private Image blankLargePanel1;
   private Image blankLargePanel2;
   private Image blankLargePanel3;
   private Image blankLargePanel4;
   private Image blankLargePanel5;
   private Image blankLargePanel6;
   private GUIEnums.GUIPanelPage panelPage;
   private GUIButtonHide hidePanel;
   private GUISquarePanelButton workforceTab;
   private GUISquarePanelButton resourceManagementTab;
   private GUISquarePanelButton tradeTab;
   private GUISquarePanelButton tradeTabMock;
   private GUISquarePanelButton migrationTab;
   private GUISquarePanelButton migrationTabMock;
   private GUISquarePanelButton courierTab;
   private GUISquarePanelButton courierTabMock;
   private boolean hidden;
   private Image objectManagementPanel;
   private Image tradePanel;
   private Image tradePanelBar;
   private EnumMap<ResourceModule.ResourceType, GUIOvalButton> tradeBuy = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIOvalButton> tradeSell = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIIcon> tradeGoldVillageStockIcons = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIIcon> tradeGoldCatjeetStockIcons = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIIcon> tradeVillageStockIcons = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIIcon> tradeCatjeetStockIcons = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIIcon> tradeResourceIcons = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIRoundButton> tradeResourceBuyDown = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIRoundButton> tradeResourceBuyUp = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIRoundButton> tradeResourceSellDown = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIRoundButton> tradeResourceSellUp = new EnumMap<>(ResourceModule.ResourceType.class);
   private GUIOvalButton tradeLaborHire;
   private GUIIcon tradeGoldCatjeetLaborerStockIcons;
   private GUIIcon tradeCatjeetLaborerStockIcons;
   private GUIRoundButton tradeLaborDown;
   private GUIRoundButton tradeLaborUp;
   private GUIButtonPage leftTradePageLeftTop;
   private GUIButtonPage leftTradePageRightTop;
   private GUIButtonPage leftTradePageLeftBottom;
   private GUIButtonPage leftTradePageRightBottom;
   private ResourceModule.ResourceCategory tradeCategory = ResourceModule.ResourceCategory.values()[0];
   private int workforcePage = 0;
   private int workforcePageLength = 6;
   private Image availableWorkersPanel;
   private GUIIcon iconUnemployedVillagers;
   private GUIPanelButton[] workforceWorkerPanel = new GUIPanelButton[MobJobBase.MobJobType.values().length];
   private GUIRoundButton[] workforceDesiredIncrease = new GUIRoundButton[MobJobBase.MobJobType.values().length];
   private GUIRoundButton[] workforceDesiredDecrease = new GUIRoundButton[MobJobBase.MobJobType.values().length];
   private GUIRoundMobJobColorButton[] workforceJobColorBase = new GUIRoundMobJobColorButton[MobJobBase.MobJobType.values().length];
   private GUIRoundMobJobColorButton[] workforceJobColorAccent = new GUIRoundMobJobColorButton[MobJobBase.MobJobType.values().length];
   private GUIIcon[] iconDesiredWorkers = new GUIIcon[MobJobBase.MobJobType.values().length];
   private GUIIcon[] iconCurrentWorkers = new GUIIcon[MobJobBase.MobJobType.values().length];
   private GUIButtonPage leftWorkforcePageUpTop;
   private GUIButtonPage leftWorkforcePageDownTop;
   private GUIButtonPage leftWorkforcePageUpBottom;
   private GUIButtonPage leftWorkforcePageDownBottom;
   private Image workforceColorSelectPanel;
   private MobJobBase.MobJobType workforceColorSelectType = MobJobBase.MobJobType.BUILDER;
   private boolean workforceColorSelectAccentMode;
   private GUIRoundMobJobColorButton workforceColorSelectDefaultBase;
   private GUIRoundMobJobColorButton workforceColorSelectDefaultAccent;
   private GUIRoundMobJobColorButton workforceColorSelectCurrentBase;
   private GUIRoundMobJobColorButton workforceColorSelectCurrentAccent;
   private GUIRoundMobJobColorButton[] workforceColorSelectPresets;
   private GUIRoundButton workforceColorSelectDecreaseRed;
   private GUIRoundButton workforceColorSelectIncreaseRed;
   private GUIRoundButton workforceColorSelectDecreaseGreen;
   private GUIRoundButton workforceColorSelectIncreaseGreen;
   private GUIRoundButton workforceColorSelectDecreaseBlue;
   private GUIRoundButton workforceColorSelectIncreaseBlue;
   private GUIRoundButton workforceColorSelectDecreaseAlpha;
   private GUIRoundButton workforceColorSelectIncreaseAlpha;
   private Image resourceManagementPanel;
   private Image resourceManagementPanelBar;
   private GUIRoundButton resourceManagementFilterToggle;
   private GUIControllerBase.ResourceFilter resourceManagementFilter = GUIControllerBase.ResourceFilter.ALL;
   private EnumMap<ResourceModule.ResourceType, GUIIcon> resourceManagementMakeInfinityIcons = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIIcon> resourceManagementMakeStorageIcons = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIIcon> resourceManagementMaintainInfinityIcons = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIIcon> resourceManagementMaintainStorageIcons = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIIcon> resourceManagementResourceIcons = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIRoundButton> resourceManagementResourceMakeDown = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIRoundButton> resourceManagementResourceMakeUp = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIRoundButton> resourceManagementResourceMaintainDown = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIRoundButton> resourceManagementResourceMaintainUp = new EnumMap<>(ResourceModule.ResourceType.class);
   private GUIButtonPage leftResourceManagementPageLeftTop;
   private GUIButtonPage leftResourceManagementPageRightTop;
   private GUIButtonPage leftResourceManagementPageLeftBottom;
   private GUIButtonPage leftResourceManagementPageRightBottom;
   private ResourceModule.ResourceCategory leftResourceManagementCategory = ResourceModule.ResourceCategory.values()[0];
   private Image migrationPanel;
   private Image migrationPanelBar;
   private GUIIcon iconMigratableVillagers;
   private GUIIcon iconMaximumMigrationsPerDay;
   private GUIIcon iconReadyToMigrate;
   private GUIIcon iconTotalMigratedToday;
   private GUIClickableArea[] migrationArea = new GUIClickableArea[8];
   private GUIRoundButton[] migrationIncrease = new GUIRoundButton[8];
   private GUIRoundButton[] migrationDecrease = new GUIRoundButton[8];
   private GUIIcon[] iconRegionsTotalPopulation = new GUIIcon[8];
   private GUIIcon[] iconMigrationEnRoute = new GUIIcon[8];
   private Image courierPanel;
   private Image courierPanelBar;
   private GUIIcon iconCourierGolem;
   private GUIClickableArea[] courierArea = new GUIClickableArea[8];
   private GUIRoundButton courierLastRegion;
   private GUIRoundButton courierNextRegion;
   private GUIRoundButton courierFilterToggle;
   private GUIControllerBase.ResourceFilter courierFilter = GUIControllerBase.ResourceFilter.ALL;
   private EnumMap<ResourceModule.ResourceType, GUIClickableArea> courierResourceArea = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIIcon> courierMakeInfinityIcons = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIIcon> courierMaintainInfinityIcons = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIIcon> courierResourceIcons = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIRoundButton> courierResourceSendNowDown = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIRoundButton> courierResourceSendNowUp = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIRoundButton> courierResourceDailyDown = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIRoundButton> courierResourceDailyUp = new EnumMap<>(ResourceModule.ResourceType.class);
   private GUIButtonPage leftCourierPageLeftTop;
   private GUIButtonPage leftCourierPageRightTop;
   private GUIButtonPage leftCourierPageLeftBottom;
   private GUIButtonPage leftCourierPageRightBottom;
   private String selectedCourierMap;
   private int selectedCourierMapSlot;
   private ResourceModule.ResourceCategory leftCourierCategory = ResourceModule.ResourceCategory.values()[0];

   public LeftPanel(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.mask = new Rectangle(0.0F, 0.0F, 190.0F, 530.0F);
      this.masks.add(this.mask);
      this.blankLargePanel1 = ImageLoader.getImage("res/GUI/gameState/panelLarge1.png");
      this.blankLargePanel2 = ImageLoader.getImage("res/GUI/gameState/panelLarge2.png");
      this.blankLargePanel3 = ImageLoader.getImage("res/GUI/gameState/panelLarge3.png");
      this.blankLargePanel4 = ImageLoader.getImage("res/GUI/gameState/panelLarge4.png");
      this.blankLargePanel5 = ImageLoader.getImage("res/GUI/gameState/panelLarge5.png");
      this.blankLargePanel6 = ImageLoader.getImage("res/GUI/gameState/panelLarge6.png");
      this.panelPage = GUIEnums.GUIPanelPage.LEFT_WORKFORCE;
      this.workforceTab = new GUISquarePanelButton("workforceTab", 0, "workforce");
      this.workforceTab.setToolTip(Text.getText("playLeftPanel.button.tooltip.workforceHeader"));
      this.guiButtons.add(this.workforceTab);
      this.resourceManagementTab = new GUISquarePanelButton("resourceManagementTab", 0, "resourceManagement");
      this.resourceManagementTab.setToolTip(Text.getText("playLeftPanel.button.tooltip.resourceManagementHeader"));
      this.guiButtons.add(this.resourceManagementTab);
      this.tradeTab = new GUISquarePanelButton("tradeTab", 0, "trade");
      this.tradeTab.setToolTip(Text.getText("playLeftPanel.button.tooltip.tradeHeader"));
      this.guiButtons.add(this.tradeTab);
      this.tradeTabMock = new GUISquarePanelButton("tradeTabMock", 0, "trade");
      this.tradeTabMock.setToolTip(Text.getText("playLeftPanel.button.tooltip.tradeHeader"), Text.getText("playLeftPanel.button.tooltip.trade"));
      this.guiButtons.add(this.tradeTabMock);
      this.migrationTab = new GUISquarePanelButton("migrationTab", 0, "migration");
      this.migrationTab.setToolTip(Text.getText("playLeftPanel.button.tooltip.migrationHeader"));
      this.guiButtons.add(this.migrationTab);
      this.migrationTabMock = new GUISquarePanelButton("migrationTabMock", 0, "migration");
      this.migrationTabMock.setToolTip(Text.getText("playLeftPanel.button.tooltip.migrationHeader"), Text.getText("playLeftPanel.button.tooltip.migration"));
      this.guiButtons.add(this.migrationTabMock);
      this.courierTab = new GUISquarePanelButton("courierTab", 0, "courier");
      this.courierTab.setToolTip(Text.getText("playLeftPanel.button.tooltip.courierHeader"));
      this.guiButtons.add(this.courierTab);
      this.courierTabMock = new GUISquarePanelButton("courierTabMock", 0, "courier");
      this.courierTabMock.setToolTip(Text.getText("playLeftPanel.button.tooltip.courierHeader"), Text.getText("playLeftPanel.button.tooltip.courier"));
      this.guiButtons.add(this.courierTabMock);
      this.hidePanel = new GUIButtonHide("hidePanel", false);
      this.hidePanel.setToolTip(Text.getText("playLeftPanel.button.tooltip.hidePanelHeader"));
      this.guiButtons.add(this.hidePanel);
      this.objectManagementPanel = ImageLoader.getImage("res/GUI/gameState/objectManagementPanel.png");
      this.tradePanel = ImageLoader.getImage("res/GUI/gameState/tradePanel.png");
      this.tradePanelBar = ImageLoader.getImage("res/GUI/gameState/tradePanelBar.png");

      ResourceModule.ResourceType[] var9;
      for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
         GUIOvalButton newOvalButton = new GUIOvalButton("tradeBuy" + type.getTemplate().getName(), Text.getText("playLeftPanel.trade.button.buy"), 0);
         Text.setVariableText("resourceName", type.getTemplate().getName());
         newOvalButton.setToolTip(Text.getText("playLeftPanel.trade.button.tooltip.buyHeader"));
         this.tradeBuy.put(type, newOvalButton);
         this.guiButtons.add(newOvalButton);
         newOvalButton = new GUIOvalButton("tradeSell" + type.getTemplate().getName(), Text.getText("playLeftPanel.trade.button.sell"), 0);
         Text.setVariableText("resourceName", type.getTemplate().getName());
         newOvalButton.setToolTip(Text.getText("playLeftPanel.trade.button.tooltip.sellHeader"));
         this.tradeSell.put(type, newOvalButton);
         this.guiButtons.add(newOvalButton);
         GUIIcon newIcon = new GUIIcon("iconGoldCoinsSmall");
         newIcon.setToolTip(Text.getText("playLeftPanel.trade.tooltip.villageStockPriceHeader"));
         this.tradeGoldVillageStockIcons.put(type, newIcon);
         this.guiButtons.add(newIcon);
         newIcon = new GUIIcon("iconGoldCoinsSmall");
         newIcon.setToolTip(Text.getText("playLeftPanel.trade.tooltip.catjeetStockPriceHeader"));
         this.tradeGoldCatjeetStockIcons.put(type, newIcon);
         this.guiButtons.add(newIcon);
         newIcon = new GUIIcon("iconHome");
         newIcon.setToolTip(Text.getText("playLeftPanel.trade.tooltip.villageStockHeader"));
         this.tradeVillageStockIcons.put(type, newIcon);
         this.guiButtons.add(newIcon);
         newIcon = new GUIIcon("iconChest");
         newIcon.setToolTip(Text.getText("playLeftPanel.trade.tooltip.catjeetStockHeader"));
         this.tradeCatjeetStockIcons.put(type, newIcon);
         this.guiButtons.add(newIcon);
         newIcon = new GUIIcon(type);
         newIcon.setToolTip(type.getTemplate().getName());
         this.tradeResourceIcons.put(type, newIcon);
         this.guiButtons.add(newIcon);
         GUIRoundButton newRoundButton = new GUIRoundButton("tradeResourceBuyDown" + type.getTemplate().getName(), "buttonMinus", 0);
         Text.setVariableText("resourceName", type.getTemplate().getNamePlural());
         newRoundButton.setToolTip(
            Text.getText("playLeftPanel.trade.button.tooltip.decreaseBuyOrderHeader"), Text.getText("playLeftPanel.trade.button.tooltip.decreaseBuyOrder")
         );
         this.tradeResourceBuyDown.put(type, newRoundButton);
         this.guiButtons.add(newRoundButton);
         newRoundButton = new GUIRoundButton("tradeResourceBuyUp" + type.getTemplate().getName(), "buttonPlus", 0);
         Text.setVariableText("resourceName", type.getTemplate().getNamePlural());
         newRoundButton.setToolTip(
            Text.getText("playLeftPanel.trade.button.tooltip.increaseBuyOrderHeader"), Text.getText("playLeftPanel.trade.button.tooltip.increaseBuyOrder")
         );
         this.tradeResourceBuyUp.put(type, newRoundButton);
         this.guiButtons.add(newRoundButton);
         newRoundButton = new GUIRoundButton("tradeResourceSellDown" + type.getTemplate().getName(), "buttonMinus", 0);
         Text.setVariableText("resourceName", type.getTemplate().getNamePlural());
         newRoundButton.setToolTip(
            Text.getText("playLeftPanel.trade.button.tooltip.decreaseSellOrderHeader"), Text.getText("playLeftPanel.trade.button.tooltip.decreaseSellOrder")
         );
         this.tradeResourceSellDown.put(type, newRoundButton);
         this.guiButtons.add(newRoundButton);
         newRoundButton = new GUIRoundButton("tradeResourceSellUp" + type.getTemplate().getName(), "buttonPlus", 0);
         Text.setVariableText("resourceName", type.getTemplate().getNamePlural());
         newRoundButton.setToolTip(
            Text.getText("playLeftPanel.trade.button.tooltip.increaseSellOrderHeader"), Text.getText("playLeftPanel.trade.button.tooltip.increaseSellOrder")
         );
         this.tradeResourceSellUp.put(type, newRoundButton);
         this.guiButtons.add(newRoundButton);
      }

      this.tradeLaborHire = new GUIOvalButton("tradeLaborHire", Text.getText("playLeftPanel.trade.button.hire"), 0);
      this.tradeLaborHire
         .setToolTip(Text.getText("playLeftPanel.trade.button.tooltip.hireLaborHeader"), Text.getText("playLeftPanel.trade.button.tooltip.hireLabor"));
      this.guiButtons.add(this.tradeLaborHire);
      this.tradeGoldCatjeetLaborerStockIcons = new GUIIcon("iconGoldCoinsSmall");
      this.tradeGoldCatjeetLaborerStockIcons.setToolTip(Text.getText("playLeftPanel.trade.tooltip.priceHeader"));
      this.guiButtons.add(this.tradeGoldCatjeetLaborerStockIcons);
      this.tradeCatjeetLaborerStockIcons = new GUIIcon("iconChest");
      this.tradeCatjeetLaborerStockIcons
         .setToolTip(Text.getText("playLeftPanel.trade.tooltip.laborerStockHeader"), Text.getText("playLeftPanel.trade.tooltip.laborerStock"));
      this.guiButtons.add(this.tradeCatjeetLaborerStockIcons);
      this.tradeLaborDown = new GUIRoundButton("tradeLaborDown", "buttonMinus", 0);
      this.tradeLaborDown
         .setToolTip(
            Text.getText("playLeftPanel.trade.button.tooltip.decraseCatjeetOrderHeader"),
            Text.getText("playLeftPanel.trade.button.tooltip.decraseCatjeetOrder")
         );
      this.guiButtons.add(this.tradeLaborDown);
      this.tradeLaborUp = new GUIRoundButton("tradeLaborUp", "buttonPlus", 0);
      this.tradeLaborUp
         .setToolTip(
            Text.getText("playLeftPanel.trade.button.tooltip.incraseCatjeetOrderHeader"),
            Text.getText("playLeftPanel.trade.button.tooltip.incraseCatjeetOrder")
         );
      this.guiButtons.add(this.tradeLaborUp);
      this.leftTradePageLeftTop = new GUIButtonPage("leftTradePageLeftTop", "left");
      this.leftTradePageLeftTop.setToolTip(Text.getText("playLeftPanel.trade.button.tooltip.previousCategoryHeader"));
      this.guiButtons.add(this.leftTradePageLeftTop);
      this.leftTradePageRightTop = new GUIButtonPage("leftTradePageRightTop", "right");
      this.leftTradePageRightTop.setToolTip(Text.getText("playLeftPanel.trade.button.tooltip.nextCategoryHeader"));
      this.guiButtons.add(this.leftTradePageRightTop);
      this.leftTradePageLeftBottom = new GUIButtonPage("leftTradePageLeftBottom", "left");
      this.leftTradePageLeftBottom.setToolTip(Text.getText("playLeftPanel.trade.button.tooltip.previousCategoryHeader"));
      this.guiButtons.add(this.leftTradePageLeftBottom);
      this.leftTradePageRightBottom = new GUIButtonPage("leftTradePageRightBottom", "right");
      this.leftTradePageRightBottom.setToolTip(Text.getText("playLeftPanel.trade.button.tooltip.nextCategoryHeader"));
      this.guiButtons.add(this.leftTradePageRightBottom);
      this.availableWorkersPanel = ImageLoader.getImage("res/GUI/gameState/buttonGenericWood50Px" + (Utilities.randomInt(3) + 1) + ".png");
      this.iconUnemployedVillagers = new GUIIcon("iconVillager");
      this.iconUnemployedVillagers
         .setToolTip(
            Text.getText("playLeftPanel.workforce.tooltip.unemployedVillagersHeader"), Text.getText("playLeftPanel.workforce.tooltip.unemployedVillagers")
         );
      this.guiButtons.add(this.iconUnemployedVillagers);

      for (MobJobBase.MobJobType t : MobJobBase.MobJobType.values()) {
         this.workforceWorkerPanel[t.ordinal()] = new GUIPanelButton("workforceWorkerPanel" + t, 1, t.getJob().getMobJobNamePlural());
         this.guiButtons.add(this.workforceWorkerPanel[t.ordinal()]);
         this.workforceDesiredIncrease[t.ordinal()] = new GUIRoundButton("workforceDesiredIncrease" + t, "buttonPlus", 0);
         this.workforceDesiredIncrease[t.ordinal()]
            .setToolTip(
               Text.getText("playLeftPanel.workforce.button.tooltip.requestWorkersHeader"),
               Text.getText("playLeftPanel.workforce.button.tooltip.requestWorkers")
            );
         this.guiButtons.add(this.workforceDesiredIncrease[t.ordinal()]);
         this.workforceDesiredDecrease[t.ordinal()] = new GUIRoundButton("workforceDesiredDecrease" + t, "buttonMinus", 0);
         this.workforceDesiredDecrease[t.ordinal()]
            .setToolTip(
               Text.getText("playLeftPanel.workforce.button.tooltip.reduceWorkersHeader"), Text.getText("playLeftPanel.workforce.button.tooltip.reduceWorkers")
            );
         this.guiButtons.add(this.workforceDesiredDecrease[t.ordinal()]);
         this.workforceJobColorBase[t.ordinal()] = new GUIRoundMobJobColorButton("workforceJobColorBase" + t, t, false, 2);
         this.workforceJobColorBase[t.ordinal()]
            .setToolTip(
               Text.getText("playLeftPanel.workforce.button.tooltip.jobColorBaseHeader"), Text.getText("playLeftPanel.workforce.button.tooltip.jobColorBase")
            );
         this.guiButtons.add(this.workforceJobColorBase[t.ordinal()]);
         this.workforceJobColorAccent[t.ordinal()] = new GUIRoundMobJobColorButton("workforceJobColorAccent" + t, t, true, 4);
         this.workforceJobColorAccent[t.ordinal()]
            .setToolTip(
               Text.getText("playLeftPanel.workforce.button.tooltip.jobColorAccentHeader"),
               Text.getText("playLeftPanel.workforce.button.tooltip.jobColorAccent")
            );
         this.guiButtons.add(this.workforceJobColorAccent[t.ordinal()]);
         this.iconDesiredWorkers[t.ordinal()] = new GUIIcon("iconWorkers");
         this.iconDesiredWorkers[t.ordinal()]
            .setToolTip(
               Text.getText("playLeftPanel.workforce.tooltip.desiredAndMaxWorkersHeader"), Text.getText("playLeftPanel.workforce.tooltip.desiredAndMaxWorkers")
            );
         this.guiButtons.add(this.iconDesiredWorkers[t.ordinal()]);
         this.iconCurrentWorkers[t.ordinal()] = new GUIIcon("iconWorkers");
         this.iconCurrentWorkers[t.ordinal()]
            .setToolTip(
               Text.getText("playLeftPanel.workforce.tooltip.totalAndEnRouteVillagersHeader"),
               Text.getText("playLeftPanel.workforce.tooltip.totalAndEnRouteVillagers")
            );
         this.guiButtons.add(this.iconCurrentWorkers[t.ordinal()]);
      }

      this.leftWorkforcePageUpTop = new GUIButtonPage("leftWorkforcePageUpTop", "up");
      this.leftWorkforcePageUpTop.setToolTip(Text.getText("playLeftPanel.workforce.button.tooltip.pageUpHeader"));
      this.guiButtons.add(this.leftWorkforcePageUpTop);
      this.leftWorkforcePageDownTop = new GUIButtonPage("leftWorkforcePageDownTop", "down");
      this.leftWorkforcePageDownTop.setToolTip(Text.getText("playLeftPanel.workforce.button.tooltip.pageDownHeader"));
      this.guiButtons.add(this.leftWorkforcePageDownTop);
      this.leftWorkforcePageUpBottom = new GUIButtonPage("leftWorkforcePageUpBottom", "up");
      this.leftWorkforcePageUpBottom.setToolTip(Text.getText("playLeftPanel.workforce.button.tooltip.pageUpHeader"));
      this.guiButtons.add(this.leftWorkforcePageUpBottom);
      this.leftWorkforcePageDownBottom = new GUIButtonPage("leftWorkforcePageDownBottom", "down");
      this.leftWorkforcePageDownBottom.setToolTip(Text.getText("playLeftPanel.workforce.button.tooltip.pageDownHeader"));
      this.guiButtons.add(this.leftWorkforcePageDownBottom);
      this.workforceColorSelectPanel = ImageLoader.getImage("res/GUI/gameState/workforceColorSelectPanel.png");
      this.workforceColorSelectType = MobJobBase.MobJobType.NONE;
      this.workforceColorSelectDefaultBase = new GUIRoundMobJobColorButton("workforceColorSelectDefaultBase", this.workforceColorSelectType, false, 2);
      this.workforceColorSelectDefaultBase
         .setToolTip(
            Text.getText("playLeftPanel.workforceColorSelect.button.tooltip.defaultBaseHeader"),
            Text.getText("playLeftPanel.workforceColorSelect.button.tooltip.defaultBase")
         );
      this.guiButtons.add(this.workforceColorSelectDefaultBase);
      this.workforceColorSelectDefaultAccent = new GUIRoundMobJobColorButton("workforceColorSelectDefaultAccent", this.workforceColorSelectType, true, 4);
      this.workforceColorSelectDefaultAccent
         .setToolTip(
            Text.getText("playLeftPanel.workforceColorSelect.button.tooltip.defaultAccentHeader"),
            Text.getText("playLeftPanel.workforceColorSelect.button.tooltip.defaultAccent")
         );
      this.guiButtons.add(this.workforceColorSelectDefaultAccent);
      this.workforceColorSelectCurrentBase = new GUIRoundMobJobColorButton("workforceColorSelectCurrentBase", this.workforceColorSelectType, false, 2);
      this.workforceColorSelectCurrentBase
         .setToolTip(
            Text.getText("playLeftPanel.workforceColorSelect.button.tooltip.currentBaseHeader"),
            Text.getText("playLeftPanel.workforceColorSelect.button.tooltip.currentBase")
         );
      this.guiButtons.add(this.workforceColorSelectCurrentBase);
      this.workforceColorSelectCurrentAccent = new GUIRoundMobJobColorButton("workforceColorSelectCurrentAccent", this.workforceColorSelectType, true, 4);
      this.workforceColorSelectCurrentAccent
         .setToolTip(
            Text.getText("playLeftPanel.workforceColorSelect.button.tooltip.currentAccentHeader"),
            Text.getText("playLeftPanel.workforceColorSelect.button.tooltip.currentAccent")
         );
      this.guiButtons.add(this.workforceColorSelectCurrentAccent);
      ArrayList<GUIRoundMobJobColorButton> workforceColorSelectPresetsList = new ArrayList<>();

      MobJobBase.MobJobType[] var33;
      for (MobJobBase.MobJobType t : MobJobBase.MobJobType.values()) {
         if (t != MobJobBase.MobJobType.NONE) {
            MobJobBase job = t.getJob();
            if (job.getBaseColor() != null) {
               GUIRoundMobJobColorButton newButton = new GUIRoundMobJobColorButton("workforceColorSelectPresets" + t, job.getBaseColor(), 2);
               Text.setVariableText("mobJobName", job.getMobJobName());
               newButton.setToolTip(
                  Text.getText("playLeftPanel.workforceColorSelect.button.tooltip.presetBaseColorHeader"),
                  Text.getText("playLeftPanel.workforceColorSelect.button.tooltip.presetBaseColor")
               );
               workforceColorSelectPresetsList.add(newButton);
               this.guiButtons.add(newButton);
            }

            if (job.getAccentColor() != null) {
               GUIRoundMobJobColorButton newButton = new GUIRoundMobJobColorButton("workforceColorSelectPresetsAccent" + t, job.getAccentColor(), 4);
               Text.setVariableText("mobJobName", job.getMobJobName());
               newButton.setToolTip(
                  Text.getText("playLeftPanel.workforceColorSelect.button.tooltip.presetAccentColorHeader"),
                  Text.getText("playLeftPanel.workforceColorSelect.button.tooltip.presetAccentColor")
               );
               workforceColorSelectPresetsList.add(newButton);
               this.guiButtons.add(newButton);
            }
         }
      }

      this.workforceColorSelectPresets = new GUIRoundMobJobColorButton[workforceColorSelectPresetsList.size()];

      for (int x = 0; x < workforceColorSelectPresetsList.size(); x++) {
         this.workforceColorSelectPresets[x] = workforceColorSelectPresetsList.get(x);
      }

      this.workforceColorSelectDecreaseRed = new GUIRoundButton("workforceColorSelectDecreaseRed", "buttonMinus", 0);
      this.workforceColorSelectDecreaseRed.setToolTip(Text.getText("playLeftPanel.workforceColorSelect.button.tooltip.decreaseRed"));
      this.guiButtons.add(this.workforceColorSelectDecreaseRed);
      this.workforceColorSelectIncreaseRed = new GUIRoundButton("workforceColorSelectIncreaseRed", "buttonPlus", 0);
      this.workforceColorSelectIncreaseRed.setToolTip(Text.getText("playLeftPanel.workforceColorSelect.button.tooltip.increaseRed"));
      this.guiButtons.add(this.workforceColorSelectIncreaseRed);
      this.workforceColorSelectDecreaseGreen = new GUIRoundButton("workforceColorSelectDecreaseGreen", "buttonMinus", 0);
      this.workforceColorSelectDecreaseGreen.setToolTip(Text.getText("playLeftPanel.workforceColorSelect.button.tooltip.decreaseGreen"));
      this.guiButtons.add(this.workforceColorSelectDecreaseGreen);
      this.workforceColorSelectIncreaseGreen = new GUIRoundButton("workforceColorSelectIncreaseGreen", "buttonPlus", 0);
      this.workforceColorSelectIncreaseGreen.setToolTip(Text.getText("playLeftPanel.workforceColorSelect.button.tooltip.increaseGreen"));
      this.guiButtons.add(this.workforceColorSelectIncreaseGreen);
      this.workforceColorSelectDecreaseBlue = new GUIRoundButton("workforceColorSelectDecreaseBlue", "buttonMinus", 0);
      this.workforceColorSelectDecreaseBlue.setToolTip(Text.getText("playLeftPanel.workforceColorSelect.button.tooltip.decreaseBlue"));
      this.guiButtons.add(this.workforceColorSelectDecreaseBlue);
      this.workforceColorSelectIncreaseBlue = new GUIRoundButton("workforceColorSelectIncreaseBlue", "buttonPlus", 0);
      this.workforceColorSelectIncreaseBlue.setToolTip(Text.getText("playLeftPanel.workforceColorSelect.button.tooltip.increaseBlue"));
      this.guiButtons.add(this.workforceColorSelectIncreaseBlue);
      this.workforceColorSelectDecreaseAlpha = new GUIRoundButton("workforceColorSelectDecreaseAlpha", "buttonMinus", 0);
      this.workforceColorSelectDecreaseAlpha.setToolTip(Text.getText("playLeftPanel.workforceColorSelect.button.tooltip.decreaseAlpha"));
      this.guiButtons.add(this.workforceColorSelectDecreaseAlpha);
      this.workforceColorSelectIncreaseAlpha = new GUIRoundButton("workforceColorSelectIncreaseAlpha", "buttonPlus", 0);
      this.workforceColorSelectIncreaseAlpha.setToolTip(Text.getText("playLeftPanel.workforceColorSelect.button.tooltip.increaseAlpha"));
      this.guiButtons.add(this.workforceColorSelectIncreaseAlpha);
      this.resourceManagementPanel = ImageLoader.getImage("res/GUI/gameState/resourceManagementPanel.png");
      this.resourceManagementPanelBar = ImageLoader.getImage("res/GUI/gameState/resourceManagementPanelBar.png");
      this.resourceManagementFilterToggle = new GUIRoundButton("resourceManagementFilterToggle", "buttonCircle", 0);
      this.resourceManagementFilterToggle
         .setToolTip(
            Text.getText("playLeftPanel.resourceManagement.button.tooltip.sortingHeader"),
            Text.getText("playLeftPanel.resourceManagement.button.tooltip.sorting")
         );
      this.guiButtons.add(this.resourceManagementFilterToggle);

      for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
         GUIIcon newIcon = new GUIIcon("iconInfinity");
         Text.setVariableText("resourceName", type.getTemplate().getNamePlural());
         newIcon.setToolTip(
            Text.getText("playLeftPanel.resourceManagement.tooltip.makeInfinityHeader"), Text.getText("playLeftPanel.resourceManagement.tooltip.makeInfinity")
         );
         this.resourceManagementMakeInfinityIcons.put(type, newIcon);
         this.guiButtons.add(newIcon);
         newIcon = new GUIIcon("iconChest");
         Text.setVariableText("resourceName", type.getTemplate().getNamePlural());
         newIcon.setToolTip(
            Text.getText("playLeftPanel.resourceManagement.tooltip.makeStorageHeader"), Text.getText("playLeftPanel.resourceManagement.tooltip.makeStorage")
         );
         this.resourceManagementMakeStorageIcons.put(type, newIcon);
         this.guiButtons.add(newIcon);
         newIcon = new GUIIcon("iconInfinity");
         Text.setVariableText("resourceName", type.getTemplate().getNamePlural());
         newIcon.setToolTip(
            Text.getText("playLeftPanel.resourceManagement.tooltip.maintainInfinityHeader"),
            Text.getText("playLeftPanel.resourceManagement.tooltip.maintainInfinity")
         );
         this.resourceManagementMaintainInfinityIcons.put(type, newIcon);
         this.guiButtons.add(newIcon);
         newIcon = new GUIIcon("iconChest");
         Text.setVariableText("resourceName", type.getTemplate().getNamePlural());
         newIcon.setToolTip(
            Text.getText("playLeftPanel.resourceManagement.tooltip.maintainStorageHeader"),
            Text.getText("playLeftPanel.resourceManagement.tooltip.maintainStorage")
         );
         this.resourceManagementMaintainStorageIcons.put(type, newIcon);
         this.guiButtons.add(newIcon);
         if (type.getTemplate().getBaseResources().size() <= 0) {
            newIcon = new GUIIcon(type);
            newIcon.setToolTip(type.getTemplate().getName(), type.getTemplate().getDescription());
         } else {
            newIcon = new GUIIcon(type);
            String resourcesOut = "";

            for (int i = 0; i < type.getTemplate().getBaseResources().size(); i++) {
               ResourceModule.ResourceType tx = type.getTemplate().getBaseResources().get(i);
               if (i == type.getTemplate().getBaseResources().size() - 1) {
                  resourcesOut = resourcesOut + tx.getTemplate().getName();
               } else {
                  resourcesOut = resourcesOut + tx.getTemplate().getName() + ", ";
               }
            }

            Text.setVariableText("resourceName", resourcesOut);
            newIcon.setToolTip(
               type.getTemplate().getName(),
               type.getTemplate().getDescription() + "~$GRE1" + Text.getText("playLeftPanel.resourceManagement.tooltip.requiresResource")
            );
         }

         this.resourceManagementResourceIcons.put(type, newIcon);
         this.guiButtons.add(newIcon);
         GUIRoundButton newRoundButton = new GUIRoundButton("resourceManagementResourceMake" + type.getTemplate().getName() + "Down", "buttonMinus", 0);
         Text.setVariableText("resourceName", type.getTemplate().getNamePlural());
         newRoundButton.setToolTip(
            Text.getText("playLeftPanel.resourceManagement.button.tooltip.makeHeader"), Text.getText("playLeftPanel.resourceManagement.button.tooltip.make")
         );
         this.resourceManagementResourceMakeDown.put(type, newRoundButton);
         this.guiButtons.add(newRoundButton);
         newRoundButton = new GUIRoundButton("resourceManagementResourceMake" + type.getTemplate().getName() + "Up", "buttonPlus", 0);
         Text.setVariableText("resourceName", type.getTemplate().getNamePlural());
         newRoundButton.setToolTip(
            Text.getText("playLeftPanel.resourceManagement.button.tooltip.makeHeader"), Text.getText("playLeftPanel.resourceManagement.button.tooltip.make")
         );
         this.resourceManagementResourceMakeUp.put(type, newRoundButton);
         this.guiButtons.add(newRoundButton);
         newRoundButton = new GUIRoundButton("resourceManagementResourceMaintain" + type.getTemplate().getName() + "Down", "buttonMinus", 0);
         Text.setVariableText("resourceName", type.getTemplate().getNamePlural());
         newRoundButton.setToolTip(
            Text.getText("playLeftPanel.resourceManagement.button.tooltip.maintainHeader"),
            Text.getText("playLeftPanel.resourceManagement.button.tooltip.maintain")
         );
         this.resourceManagementResourceMaintainDown.put(type, newRoundButton);
         this.guiButtons.add(newRoundButton);
         newRoundButton = new GUIRoundButton("resourceManagementResourceMaintain" + type.getTemplate().getName() + "Up", "buttonPlus", 0);
         Text.setVariableText("resourceName", type.getTemplate().getNamePlural());
         newRoundButton.setToolTip(
            Text.getText("playLeftPanel.resourceManagement.button.tooltip.maintainHeader"),
            Text.getText("playLeftPanel.resourceManagement.button.tooltip.maintain")
         );
         this.resourceManagementResourceMaintainUp.put(type, newRoundButton);
         this.guiButtons.add(newRoundButton);
      }

      this.leftResourceManagementPageLeftTop = new GUIButtonPage("leftResourceManagementPageLeftTop", "left");
      this.leftResourceManagementPageLeftTop.setToolTip(Text.getText("playLeftPanel.resourceManagement.button.tooltip.previousCategoryHeader"));
      this.guiButtons.add(this.leftResourceManagementPageLeftTop);
      this.leftResourceManagementPageRightTop = new GUIButtonPage("leftResourceManagementPageRightTop", "right");
      this.leftResourceManagementPageRightTop.setToolTip(Text.getText("playLeftPanel.resourceManagement.button.tooltip.nextCategoryHeader"));
      this.guiButtons.add(this.leftResourceManagementPageRightTop);
      this.leftResourceManagementPageLeftBottom = new GUIButtonPage("leftResourceManagementPageLeftBottom", "left");
      this.leftResourceManagementPageLeftBottom.setToolTip(Text.getText("playLeftPanel.resourceManagement.button.tooltip.previousCategoryHeader"));
      this.guiButtons.add(this.leftResourceManagementPageLeftBottom);
      this.leftResourceManagementPageRightBottom = new GUIButtonPage("leftResourceManagementPageRightBottom", "right");
      this.leftResourceManagementPageRightBottom.setToolTip(Text.getText("playLeftPanel.resourceManagement.button.tooltip.nextCategoryHeader"));
      this.guiButtons.add(this.leftResourceManagementPageRightBottom);
      this.migrationPanel = ImageLoader.getImage("res/GUI/gameState/migrationPanel.png");
      this.migrationPanelBar = ImageLoader.getImage("res/GUI/gameState/tradePanelBar.png");
      this.iconMigratableVillagers = new GUIIcon("iconVillager");
      this.iconMigratableVillagers
         .setToolTip(
            Text.getText("playLeftPanel.migration.tooltip.migratableVillagersHeader"), Text.getText("playLeftPanel.migration.tooltip.migratableVillagers")
         );
      this.guiButtons.add(this.iconMigratableVillagers);
      this.iconMaximumMigrationsPerDay = new GUIIcon("iconMigration");
      this.iconMaximumMigrationsPerDay
         .setToolTip(
            Text.getText("playLeftPanel.migration.tooltip.maximumDailyMigrationHeader"), Text.getText("playLeftPanel.migration.tooltip.maximumDailyMigration")
         );
      this.guiButtons.add(this.iconMaximumMigrationsPerDay);
      this.iconReadyToMigrate = new GUIIcon("iconMigrationReady");
      this.iconReadyToMigrate
         .setToolTip(Text.getText("playLeftPanel.migration.tooltip.readyToMigrateHeader"), Text.getText("playLeftPanel.migration.tooltip.readyToMigrate"));
      this.guiButtons.add(this.iconReadyToMigrate);
      this.iconTotalMigratedToday = new GUIIcon("iconMigrationEnRoute");
      this.iconTotalMigratedToday
         .setToolTip(
            Text.getText("playLeftPanel.migration.tooltip.totalMigratedTodayHeader"), Text.getText("playLeftPanel.migration.tooltip.totalMigratedToday")
         );
      this.guiButtons.add(this.iconTotalMigratedToday);

      for (int x = 0; x < 8; x++) {
         this.migrationArea[x] = new GUIClickableArea("migrationArea" + x, this.migrationPanelBar.getWidth(), this.migrationPanelBar.getHeight());
         this.guiButtons.add(this.migrationArea[x]);
         this.migrationIncrease[x] = new GUIRoundButton("migrationIncrease" + x, "buttonPlus", 0);
         this.migrationIncrease[x]
            .setToolTip(
               Text.getText("playLeftPanel.migration.button.tooltip.increaseMigrantsToSendHeader"),
               Text.getText("playLeftPanel.migration.button.tooltip.increaseMigrantsToSend")
            );
         this.guiButtons.add(this.migrationIncrease[x]);
         this.migrationDecrease[x] = new GUIRoundButton("migrationDecrease" + x, "buttonMinus", 0);
         this.migrationDecrease[x]
            .setToolTip(
               Text.getText("playLeftPanel.migration.button.tooltip.decreaseMigrantsToSendHeader"),
               Text.getText("playLeftPanel.migration.button.tooltip.decreaseMigrantsToSend")
            );
         this.guiButtons.add(this.migrationDecrease[x]);
         this.iconRegionsTotalPopulation[x] = new GUIIcon("iconVillager");
         this.iconRegionsTotalPopulation[x]
            .setToolTip(
               Text.getText("playLeftPanel.migration.tooltip.regionsTotalPopulationHeader"),
               Text.getText("playLeftPanel.migration.tooltip.regionsTotalPopulation")
            );
         this.guiButtons.add(this.iconRegionsTotalPopulation[x]);
         this.iconMigrationEnRoute[x] = new GUIIcon("iconMigrationEnRoute");
         this.iconMigrationEnRoute[x]
            .setToolTip(Text.getText("playLeftPanel.migration.tooltip.enRouteMigrantsHeader"), Text.getText("playLeftPanel.migration.tooltip.enRouteMigrants"));
         this.guiButtons.add(this.iconMigrationEnRoute[x]);
      }

      this.courierPanel = ImageLoader.getImage("res/GUI/gameState/courierPanel.png");
      this.courierPanelBar = ImageLoader.getImage("res/GUI/gameState/resourceManagementPanelBar.png");
      this.iconCourierGolem = new GUIIcon("iconCourierGolem");
      this.iconCourierGolem
         .setToolTip(Text.getText("playLeftPanel.courier.tooltip.parkedCourierGolemsHeader"), Text.getText("playLeftPanel.courier.tooltip.parkedCourierGolems"));
      this.guiButtons.add(this.iconCourierGolem);

      for (int x = 0; x < 8; x++) {
         this.courierArea[x] = new GUIClickableArea("courierArea" + x, this.courierPanelBar.getWidth(), this.courierPanelBar.getHeight());
         this.guiButtons.add(this.courierArea[x]);
      }

      this.courierLastRegion = new GUIRoundButton("courierLastRegion", "buttonLeft", 0);
      this.courierLastRegion
         .setToolTip(
            Text.getText("playLeftPanel.courier.button.tooltip.previousRegionHeader"), Text.getText("playLeftPanel.courier.button.tooltip.previousRegion")
         );
      this.guiButtons.add(this.courierLastRegion);
      this.courierNextRegion = new GUIRoundButton("courierNextRegion", "buttonRight", 0);
      this.courierNextRegion
         .setToolTip(Text.getText("playLeftPanel.courier.button.tooltip.nextRegionHeader"), Text.getText("playLeftPanel.courier.button.tooltip.nextRegion"));
      this.guiButtons.add(this.courierNextRegion);
      this.courierFilterToggle = new GUIRoundButton("courierFilterToggle", "buttonCircle", 0);
      this.courierFilterToggle
         .setToolTip(Text.getText("playLeftPanel.courier.button.tooltip.sortingHeader"), Text.getText("playLeftPanel.courier.button.tooltip.sorting"));
      this.guiButtons.add(this.courierFilterToggle);

      for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
         GUIClickableArea courierResourceAreaButton = new GUIClickableArea(
            "courierResourceArea" + type.getTemplate().getName(), this.courierPanelBar.getWidth(), this.courierPanelBar.getHeight()
         );
         this.courierResourceArea.put(type, courierResourceAreaButton);
         this.guiButtons.add(courierResourceAreaButton);
         GUIIcon newIcon = new GUIIcon("iconInfinity");
         newIcon.setToolTip(Text.getText("playLeftPanel.courier.tooltip.sendInfinityHeader"));
         this.courierMakeInfinityIcons.put(type, newIcon);
         this.guiButtons.add(newIcon);
         newIcon = new GUIIcon("iconInfinity");
         newIcon.setToolTip(Text.getText("playLeftPanel.courier.tooltip.sendInfinityHeader"));
         this.courierMaintainInfinityIcons.put(type, newIcon);
         this.guiButtons.add(newIcon);
         newIcon = new GUIIcon(type);
         newIcon.setToolTip(type.getTemplate().getName(), type.getTemplate().getDescription());
         this.courierResourceIcons.put(type, newIcon);
         this.guiButtons.add(newIcon);
         GUIRoundButton newRoundButton = new GUIRoundButton("courierResourceSendNow" + type.getTemplate().getName() + "Down", "buttonMinus", 0);
         Text.setVariableText("resourceName", type.getTemplate().getNamePlural());
         newRoundButton.setToolTip(
            Text.getText("playLeftPanel.courier.button.tooltip.sendNowHeader"), Text.getText("playLeftPanel.courier.button.tooltip.sendNow")
         );
         this.courierResourceSendNowDown.put(type, newRoundButton);
         this.guiButtons.add(newRoundButton);
         newRoundButton = new GUIRoundButton("courierResourceSendNow" + type.getTemplate().getName() + "Up", "buttonPlus", 0);
         Text.setVariableText("resourceName", type.getTemplate().getNamePlural());
         newRoundButton.setToolTip(
            Text.getText("playLeftPanel.courier.button.tooltip.sendNowHeader"), Text.getText("playLeftPanel.courier.button.tooltip.sendNow")
         );
         this.courierResourceSendNowUp.put(type, newRoundButton);
         this.guiButtons.add(newRoundButton);
         newRoundButton = new GUIRoundButton("courierResourceDaily" + type.getTemplate().getName() + "Down", "buttonMinus", 0);
         Text.setVariableText("resourceName", type.getTemplate().getNamePlural());
         newRoundButton.setToolTip(
            Text.getText("playLeftPanel.courier.button.tooltip.sendDailyHeader"), Text.getText("playLeftPanel.courier.button.tooltip.sendDaily")
         );
         this.courierResourceDailyDown.put(type, newRoundButton);
         this.guiButtons.add(newRoundButton);
         newRoundButton = new GUIRoundButton("courierResourceDaily" + type.getTemplate().getName() + "Up", "buttonPlus", 0);
         Text.setVariableText("resourceName", type.getTemplate().getNamePlural());
         newRoundButton.setToolTip(
            Text.getText("playLeftPanel.courier.button.tooltip.sendDailyHeader"), Text.getText("playLeftPanel.courier.button.tooltip.sendDaily")
         );
         this.courierResourceDailyUp.put(type, newRoundButton);
         this.guiButtons.add(newRoundButton);
      }

      this.leftCourierPageLeftTop = new GUIButtonPage("leftCourierPageLeftTop", "left");
      this.leftCourierPageLeftTop.setToolTip(Text.getText("playLeftPanel.courier.button.tooltip.previousCategoryHeader"));
      this.guiButtons.add(this.leftCourierPageLeftTop);
      this.leftCourierPageRightTop = new GUIButtonPage("leftCourierPageRightTop", "right");
      this.leftCourierPageRightTop.setToolTip(Text.getText("playLeftPanel.courier.button.tooltip.nextCategoryHeader"));
      this.guiButtons.add(this.leftCourierPageRightTop);
      this.leftCourierPageLeftBottom = new GUIButtonPage("leftCourierPageLeftBottom", "left");
      this.leftCourierPageLeftBottom.setToolTip(Text.getText("playLeftPanel.courier.button.tooltip.previousCategoryHeader"));
      this.guiButtons.add(this.leftCourierPageLeftBottom);
      this.leftCourierPageRightBottom = new GUIButtonPage("leftCourierPageRightBottom", "right");
      this.leftCourierPageRightBottom.setToolTip(Text.getText("playLeftPanel.courier.button.tooltip.nextCategoryHeader"));
      this.guiButtons.add(this.leftCourierPageRightBottom);
      this.selectedCourierMapSlot = 0;
      this.selectedCourierMap = this.map.getConnectedRegionList()[this.selectedCourierMapSlot];
   }

   public void render(boolean debug) throws SlickException {
      this.x = 6;
      this.y = ScaleControl.getInterfaceCenterY() - 265;
      if (this.y < 134) {
         this.y = 134;
      }

      if (this.hidden) {
         this.x = -180;
      }

      this.mask.setX(this.x);
      this.mask.setY(this.y);
      if (this.hidden) {
         this.hidePanel.render(this.g, this.mouse, this.x + 191, this.y, false, debug);
      } else {
         this.hidePanel.render(this.g, this.mouse, this.x, this.y, false, debug);
      }

      int yShift = 0;
      this.workforceTab.render(this.g, this.mouse, this.x + 190, this.y + 28 + yShift, false, debug);
      yShift += this.workforceTab.getHeight();
      this.resourceManagementTab.render(this.g, this.mouse, this.x + 190, this.y + 28 + yShift, false, debug);
      yShift += this.resourceManagementTab.getHeight();
      if (this.object.getMaximumCatjeetProvisioners() == 0) {
         this.tradeTabMock.render(this.g, this.mouse, this.x + 190, this.y + 28 + yShift, true, debug);
      } else {
         this.tradeTab.render(this.g, this.mouse, this.x + 190, this.y + 28 + yShift, false, debug);
      }

      yShift += this.tradeTab.getHeight();
      if (this.save.getActiveRegionalSave().getGameType() != StateBase.GameType.SKIRMISH) {
         if (this.object.getMaximumMigrationsPerDay() == 0) {
            this.migrationTabMock.render(this.g, this.mouse, this.x + 190, this.y + 28 + yShift, true, debug);
         } else {
            this.migrationTab.render(this.g, this.mouse, this.x + 190, this.y + 28 + yShift, false, debug);
         }

         yShift += this.migrationTab.getHeight();
         if (!this.object.villageHasGolemParkingSpots()) {
            this.courierTabMock.render(this.g, this.mouse, this.x + 190, this.y + 28 + yShift, true, debug);
         } else {
            this.courierTab.render(this.g, this.mouse, this.x + 190, this.y + 28 + yShift, false, debug);
         }

         yShift += this.courierTab.getHeight();
      }

      ObjectBase selectedObject = this.guiData.getSelectedObject();
      ObjectFlags objectFlags = null;
      if (selectedObject != null) {
         objectFlags = selectedObject.getObjectFlags();
      }

      MobBase selectedMob = this.guiData.getSelectedMob();
      boolean foundSelection = false;
      if ((
            selectedObject == null
               || objectFlags.getWorkerJobType() == MobJobBase.MobJobType.NONE
               || objectFlags.getWorkerJobType() == MobJobBase.MobJobType.COURIER_SUPPLIER
               || objectFlags.getBaseType() == MapTilesLoader.TileSet.MARKETPLACE
         )
         && (
            selectedMob == null
               || selectedMob.getMobJobType() == MobJobBase.MobJobType.NONE
               || selectedMob.getMobJobType() == MobJobBase.MobJobType.COURIER_SUPPLIER
               || selectedMob.getMobJobType().getJob().isNotCounted()
         )) {
         if (selectedObject != null && objectFlags.getBaseType() == MapTilesLoader.TileSet.MARKETPLACE && this.object.getMaximumCatjeetProvisioners() > 0) {
            this.renderTrade(this.x, this.y, debug);
            foundSelection = true;
         } else if (selectedObject != null && objectFlags.getMigrationsPerDayProvided() > 0) {
            this.renderMigration(this.x, this.y, debug);
            foundSelection = true;
         } else if (selectedObject != null && objectFlags.getWorkerJobType() == MobJobBase.MobJobType.COURIER_SUPPLIER) {
            this.renderCourier(this.x, this.y, debug);
            foundSelection = true;
         }
      } else {
         this.renderObjectManagement(this.x, this.y, debug);
         foundSelection = true;
      }

      if (!foundSelection) {
         if (this.panelPage == GUIEnums.GUIPanelPage.LEFT_TRADE) {
            this.renderTrade(this.x, this.y, debug);
         } else if (this.panelPage == GUIEnums.GUIPanelPage.LEFT_MIGRATION) {
            this.renderMigration(this.x, this.y, debug);
         } else if (this.panelPage == GUIEnums.GUIPanelPage.LEFT_COURIER) {
            this.renderCourier(this.x, this.y, debug);
         } else if (this.panelPage == GUIEnums.GUIPanelPage.LEFT_WORKFORCE) {
            this.renderWorkforce(this.x, this.y, debug);
         } else if (this.panelPage == GUIEnums.GUIPanelPage.LEFT_WORKFORCE_COLOR_SELECT) {
            this.renderWorkforceColorSelect(this.x, this.y, debug);
         } else if (this.panelPage == GUIEnums.GUIPanelPage.LEFT_RESOURCE_MANAGEMENT) {
            this.renderResourceManagement(this.x, this.y, debug);
         }
      }
   }

   private void renderObjectManagement(int x, int y, boolean debug) throws SlickException {
      MobJobBase.MobJobType jt = null;
      ObjectBase selectedObject = this.guiData.getSelectedObject();
      MobBase selectedMob = this.guiData.getSelectedMob();
      if (selectedObject != null) {
         jt = selectedObject.getObjectFlags().getWorkerJobType();
      } else {
         jt = selectedMob.getMobJobType();
      }

      MobJobBase j = jt.getJob();
      this.objectManagementPanel.draw(x, y + 107);
      this.availableWorkersPanel.draw(x + 135, y);
      this.iconUnemployedVillagers.render(this.g, x + 139, y + 6, debug);
      this.font.drawString(x + 152, y + 3, "$YEL1" + this.mobJob.getUnassignedVillagers(), Text.FontType.HEADER, 2);
      ArrayList<MobBase> villagers = this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false);
      EnumMap<MobJobBase.MobJobType, ArrayList<MobBase>> villagerJobs = new EnumMap<>(MobJobBase.MobJobType.class);

      MobJobBase.MobJobType[] mobAlignY;
      for (MobJobBase.MobJobType t : mobAlignY = MobJobBase.MobJobType.values()) {
         villagerJobs.put(t, new ArrayList<>());
      }

      int totalWorkers = 0;

      for (MobBase m : villagers) {
         if (m.getMobFlags().getAllowWork()) {
            villagerJobs.get(m.getMobJobType()).add(m);
            totalWorkers++;
         }
      }

      int alignY = 0;
      int mobAlignX = 0;
      int mobAlignYx = 0;
      int count = 0;
      String currentDesiredColor = "$RED1";
      if (this.mobJob.getTotalDesiredWorkers() <= totalWorkers) {
         currentDesiredColor = "$GRE1";
      }

      if (this.mobJob.getMaximumWorkers(jt) != 0) {
         this.workforceWorkerPanel[jt.ordinal()].render(this.g, this.mouse, x, y + 28 + alignY, false, debug);
         int desiredWorkers = this.mobJob.getDesiredWorkers(jt);
         int maximumWorkers = this.mobJob.getMaximumWorkers(jt);
         int currentWorkers = this.mobJob.getCurrentWorkers(jt);
         int currentWorkersEnroute = this.mobJob.getCurrentWorkers(jt, true) - currentWorkers;
         String currentWorkersColor = "$GRE1";
         if (currentWorkers < desiredWorkers * 0.4F) {
            currentWorkersColor = "$RED1";
         } else if (currentWorkers < desiredWorkers * 0.6F) {
            currentWorkersColor = "$ORA1";
         } else if (currentWorkers < desiredWorkers * 0.8F) {
            currentWorkersColor = "$YEL1";
         }

         this.workforceDesiredDecrease[jt.ordinal()].render(this.g, this.mouse, x + 12, y + 48 + alignY, debug);
         this.workforceDesiredIncrease[jt.ordinal()].render(this.g, this.mouse, x + 32, y + 48 + alignY, debug);
         this.iconDesiredWorkers[jt.ordinal()].render(this.g, x + 55, y + 51 + alignY, debug);
         this.font.drawString(x + 75, y + 48 + alignY, currentDesiredColor + desiredWorkers + "$WHI0/" + maximumWorkers, Text.FontType.HEADER, 2);
         this.workforceJobColorBase[jt.ordinal()].render(this.g, this.mouse, x + 138, y + 48 + alignY, debug);
         this.workforceJobColorAccent[jt.ordinal()].render(this.g, this.mouse, x + 158, y + 48 + alignY, debug);
         String currentWorkersEnrouteFormatted = "";
         if (currentWorkersEnroute > 0) {
            currentWorkersEnrouteFormatted = "$GRE1+" + currentWorkersEnroute;
         }

         if (currentWorkersEnroute < 0) {
            currentWorkersEnrouteFormatted = "$RED1" + currentWorkersEnroute;
         }

         this.iconCurrentWorkers[jt.ordinal()].render(this.g, x + 12, y + 74 + alignY, debug);
         this.font.drawString(x + 30, y + 71 + alignY, currentWorkersColor + currentWorkers + currentWorkersEnrouteFormatted, Text.FontType.HEADER, 2);
         count = 0;

         for (MobBase mx : villagerJobs.get(jt)) {
            if (count >= 12) {
               this.font.drawString(x + 154, y + 84 + alignY, "+" + (villagerJobs.get(jt).size() - 12), Text.FontType.BODY, 1);
               break;
            }

            count++;
            mx.renderSprite(this.g, x + 60 + mobAlignX, y + 75 + mobAlignYx + alignY, debug);
            mobAlignX += 8;
         }
      }

      this.font
         .drawString(
            x + this.resourceManagementPanel.getWidth() / 2,
            y + 110,
            Text.getText("playLeftPanel.resourceManagement.header.resourceManagement"),
            Text.FontType.BODY,
            1,
            true
         );
      this.resourceManagementFilterToggle.render(this.g, this.mouse, x + 10, y + 128, debug);
      switch (this.resourceManagementFilter) {
         case ALL:
            this.font.drawString(x + 30, y + 132, Text.getText("playLeftPanel.resourceManagement.sort.total"), Text.FontType.BODY, 1);
            break;
         case GROUND:
            this.font.drawString(x + 30, y + 132, Text.getText("playLeftPanel.resourceManagement.sort.ground"), Text.FontType.BODY, 1);
            break;
         case STORAGE:
            this.font.drawString(x + 30, y + 132, Text.getText("playLeftPanel.resourceManagement.sort.stored"), Text.FontType.BODY, 1);
            break;
         case EQUIPPED:
            this.font.drawString(x + 30, y + 132, Text.getText("playLeftPanel.resourceManagement.sort.equip"), Text.FontType.BODY, 1);
            break;
         case CARRIED:
            this.font.drawString(x + 30, y + 132, Text.getText("playLeftPanel.resourceManagement.sort.carry"), Text.FontType.BODY, 1);
            break;
         case RATE:
            this.font.drawString(x + 30, y + 132, Text.getText("playLeftPanel.resourceManagement.sort.rate"), Text.FontType.BODY, 1);
      }

      this.font.drawString(x + 73, y + 132, Text.getText("playLeftPanel.resourceManagement.header.make"), Text.FontType.BODY, 1);
      this.font.drawString(x + 124, y + 132, Text.getText("playLeftPanel.resourceManagement.header.maintain"), Text.FontType.BODY, 1);
      int var28 = 80;

      for (ResourceModule.ResourceType t : this.resourceManagementResourceIcons.keySet()) {
         if (j.canWorkerHarvest(t) || j.canWorkerRefine(t) || j.canWorkerSlaughter(t)) {
            this.resourceManagementPanelBar.draw(x + 11, y + var28 + 65);
            this.resourceManagementResourceIcons.get(t).render(this.g, x + 10, y + var28 + 70, debug);
            switch (this.resourceManagementFilter) {
               case ALL:
                  this.font.drawString(x + 30, y + var28 + 72, "$GRE0" + this.resource.getResourceTotal(t, true), Text.FontType.BODY, 1);
                  break;
               case GROUND:
                  this.font.drawString(x + 30, y + var28 + 72, "$ORA0" + this.resource.getResourceGround(t, true), Text.FontType.BODY, 1);
                  break;
               case STORAGE:
                  this.font.drawString(x + 30, y + var28 + 72, "$MAG0" + this.resource.getResourceStores(t), Text.FontType.BODY, 1);
                  break;
               case EQUIPPED:
                  this.font.drawString(x + 30, y + var28 + 72, "$YEL0" + this.resource.getResourceEquipped(t), Text.FontType.BODY, 1);
                  break;
               case CARRIED:
                  this.font.drawString(x + 30, y + var28 + 72, "$PUR0" + this.resource.getResourceCarried(t), Text.FontType.BODY, 1);
                  break;
               case RATE:
                  if (this.resource.getResourceRate(t) > 0) {
                     this.font.drawString(x + 30, y + var28 + 72, "$GRE0+" + this.resource.getResourceRate(t), Text.FontType.BODY, 1);
                  } else if (this.resource.getResourceRate(t) < 0) {
                     this.font.drawString(x + 30, y + var28 + 72, "$RED0" + this.resource.getResourceRate(t), Text.FontType.BODY, 1);
                  } else {
                     this.font.drawString(x + 30, y + var28 + 72, "0", Text.FontType.BODY, 1);
                  }
            }

            this.resourceManagementResourceMakeDown.get(t).render(this.g, this.mouse, x + 58, y + var28 + 70, debug);
            if (this.resource.getResourceMakeAmount(t) == ResourceModule.RESOURCE_INFINITE_AMOUNT) {
               this.resourceManagementMakeInfinityIcons.get(t).render(this.g, x + 80, y + var28 + 72, debug);
            } else if (this.resource.getResourceMakeAmount(t) == ResourceModule.RESOURCE_STORAGE_AMOUNT) {
               this.resourceManagementMakeStorageIcons.get(t).render(this.g, x + 80, y + var28 + 72, debug);
            } else {
               this.font.drawString(x + 88, y + var28 + 72, String.valueOf(this.resource.getResourceMakeAmount(t)), Text.FontType.BODY, 1, true);
            }

            this.resourceManagementResourceMakeUp.get(t).render(this.g, this.mouse, x + 99, y + var28 + 70, debug);
            this.resourceManagementResourceMaintainDown.get(t).render(this.g, this.mouse, x + 119, y + var28 + 70, debug);
            if (this.resource.getResourceMaintainAmount(t) == ResourceModule.RESOURCE_INFINITE_AMOUNT) {
               this.resourceManagementMaintainInfinityIcons.get(t).render(this.g, x + 141, y + var28 + 72, debug);
            } else if (this.resource.getResourceMaintainAmount(t) == ResourceModule.RESOURCE_STORAGE_AMOUNT) {
               this.resourceManagementMaintainStorageIcons.get(t).render(this.g, x + 141, y + var28 + 72, debug);
            } else {
               String colorOut = "$YEL1";
               if (this.resource.getResourceMaintainAmount(t) <= this.resource.getResourceTotal(t, true)) {
                  colorOut = "$GRE1";
               }

               this.font.drawString(x + 149, y + var28 + 72, colorOut + this.resource.getResourceMaintainAmount(t), Text.FontType.BODY, 1, true);
            }

            this.resourceManagementResourceMaintainUp.get(t).render(this.g, this.mouse, x + 160, y + var28 + 70, debug);
            var28 += 25;
            if (++count > 16) {
               break;
            }
         }
      }
   }

   private void renderWorkforce(int x, int y, boolean debug) throws SlickException {
      this.availableWorkersPanel.draw(x + 135, y);
      this.iconUnemployedVillagers.render(this.g, x + 139, y + 6, debug);
      this.font.drawString(x + 152, y + 3, "$YEL1" + this.mobJob.getUnassignedVillagers(), Text.FontType.HEADER, 2);
      ArrayList<MobBase> villagers = this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false);
      EnumMap<MobJobBase.MobJobType, ArrayList<MobBase>> villagerJobs = new EnumMap<>(MobJobBase.MobJobType.class);

      MobJobBase.MobJobType[] jobCount;
      for (MobJobBase.MobJobType t : jobCount = MobJobBase.MobJobType.values()) {
         villagerJobs.put(t, new ArrayList<>());
      }

      int totalWorkers = 0;

      for (MobBase m : villagers) {
         if (m.getMobFlags().getAllowWork()) {
            villagerJobs.get(m.getMobJobType()).add(m);
            totalWorkers++;
         }
      }

      int workforcePageStart = this.workforcePage * this.workforcePageLength;
      int workforcePageEnd = this.workforcePage * this.workforcePageLength + this.workforcePageLength;
      int jobCountx = 0;
      String currentDesiredColor = "$RED1";
      if (this.mobJob.getTotalDesiredWorkers() <= totalWorkers) {
         currentDesiredColor = "$GRE1";
      }

      int alignY = 0;
      int mobAlignX = 0;
      int mobAlignY = 0;
      int count = 0;
      int panelsLeft = 6;

      MobJobBase.MobJobType[] var19;
      for (MobJobBase.MobJobType t : MobJobBase.MobJobType.values()) {
         if (this.mobJob.getMaximumWorkers(t) != 0) {
            jobCountx++;
            if (jobCountx > workforcePageStart && jobCountx <= workforcePageEnd) {
               panelsLeft--;
               this.workforceWorkerPanel[t.ordinal()].render(this.g, this.mouse, x, y + 28 + alignY, false, debug);
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

               this.workforceDesiredDecrease[t.ordinal()].render(this.g, this.mouse, x + 12, y + 48 + alignY, debug);
               this.workforceDesiredIncrease[t.ordinal()].render(this.g, this.mouse, x + 32, y + 48 + alignY, debug);
               this.iconDesiredWorkers[t.ordinal()].render(this.g, x + 55, y + 51 + alignY, debug);
               this.font.drawString(x + 75, y + 48 + alignY, currentDesiredColor + desiredWorkers + "$WHI0/" + maximumWorkers, Text.FontType.HEADER, 2);
               this.workforceJobColorBase[t.ordinal()].render(this.g, this.mouse, x + 138, y + 48 + alignY, debug);
               this.workforceJobColorAccent[t.ordinal()].render(this.g, this.mouse, x + 158, y + 48 + alignY, debug);
               String currentWorkersEnrouteFormatted = "";
               if (currentWorkersEnroute > 0) {
                  currentWorkersEnrouteFormatted = "$GRE1+" + currentWorkersEnroute;
               }

               if (currentWorkersEnroute < 0) {
                  currentWorkersEnrouteFormatted = "$RED1" + currentWorkersEnroute;
               }

               this.iconCurrentWorkers[t.ordinal()].render(this.g, x + 12, y + 74 + alignY, debug);
               this.font.drawString(x + 30, y + 71 + alignY, currentWorkersColor + currentWorkers + currentWorkersEnrouteFormatted, Text.FontType.HEADER, 2);
               count = 0;

               for (MobBase mx : villagerJobs.get(t)) {
                  if (count >= 12) {
                     this.font.drawString(x + 154, y + 84 + alignY, "+" + (villagerJobs.get(t).size() - 12), Text.FontType.BODY, 1);
                     break;
                  }

                  count++;
                  mx.renderSprite(this.g, x + 60 + mobAlignX, y + 75 + mobAlignY + alignY, debug);
                  mobAlignX += 8;
               }

               mobAlignX = 0;
               mobAlignY = 0;
               alignY += this.blankLargePanel1.getHeight();
            }
         }
      }

      alignY = this.renderBlankPanels(panelsLeft, x, y, alignY, this.g);
      if (this.workforcePage <= 0) {
         this.leftWorkforcePageUpTop.render(this.g, this.mouse, x + 67, y, true, debug);
         this.leftWorkforcePageUpBottom.render(this.g, this.mouse, x + 67, y + alignY + 28, true, debug);
      } else {
         this.leftWorkforcePageUpTop.render(this.g, this.mouse, x + 67, y, false, debug);
         this.leftWorkforcePageUpBottom.render(this.g, this.mouse, x + 67, y + alignY + 28, false, debug);
      }

      if ((this.workforcePage + 1) * this.workforcePageLength >= jobCountx) {
         this.leftWorkforcePageDownTop.render(this.g, this.mouse, x + 96, y, true, debug);
         this.leftWorkforcePageDownBottom.render(this.g, this.mouse, x + 96, y + alignY + 28, true, debug);
      } else {
         this.leftWorkforcePageDownTop.render(this.g, this.mouse, x + 96, y, false, debug);
         this.leftWorkforcePageDownBottom.render(this.g, this.mouse, x + 96, y + alignY + 28, false, debug);
      }
   }

   private void renderWorkforceColorSelect(int x, int y, boolean debug) throws SlickException {
      this.availableWorkersPanel.draw(x + 135, y);
      this.iconUnemployedVillagers.render(this.g, x + 139, y + 6, debug);
      this.font.drawString(x + 152, y + 3, "$YEL1" + this.mobJob.getUnassignedVillagers(), Text.FontType.HEADER, 2);
      ArrayList<MobBase> villagers = this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false);
      EnumMap<MobJobBase.MobJobType, ArrayList<MobBase>> villagerJobs = new EnumMap<>(MobJobBase.MobJobType.class);

      MobJobBase.MobJobType[] mobAlignY;
      for (MobJobBase.MobJobType t : mobAlignY = MobJobBase.MobJobType.values()) {
         villagerJobs.put(t, new ArrayList<>());
      }

      int totalWorkers = 0;

      for (MobBase m : villagers) {
         if (m.getMobFlags().getAllowWork()) {
            villagerJobs.get(m.getMobJobType()).add(m);
            totalWorkers++;
         }
      }

      int alignY = 0;
      int mobAlignX = 0;
      int mobAlignYx = 0;
      int count = 0;
      String currentDesiredColor = "$RED1";
      if (this.mobJob.getTotalDesiredWorkers() <= totalWorkers) {
         currentDesiredColor = "$GRE1";
      }

      if (this.mobJob.getMaximumWorkers(this.workforceColorSelectType) != 0) {
         this.workforceWorkerPanel[this.workforceColorSelectType.ordinal()].render(this.g, this.mouse, x, y + 28 + alignY, false, debug);
         int desiredWorkers = this.mobJob.getDesiredWorkers(this.workforceColorSelectType);
         int maximumWorkers = this.mobJob.getMaximumWorkers(this.workforceColorSelectType);
         int currentWorkers = this.mobJob.getCurrentWorkers(this.workforceColorSelectType);
         int currentWorkersEnroute = this.mobJob.getCurrentWorkers(this.workforceColorSelectType, true) - currentWorkers;
         String currentWorkersColor = "$GRE1";
         if (currentWorkers < desiredWorkers * 0.4F) {
            currentWorkersColor = "$RED1";
         } else if (currentWorkers < desiredWorkers * 0.6F) {
            currentWorkersColor = "$ORA1";
         } else if (currentWorkers < desiredWorkers * 0.8F) {
            currentWorkersColor = "$YEL1";
         }

         this.workforceDesiredDecrease[this.workforceColorSelectType.ordinal()].render(this.g, this.mouse, x + 12, y + 48 + alignY, debug);
         this.workforceDesiredIncrease[this.workforceColorSelectType.ordinal()].render(this.g, this.mouse, x + 32, y + 48 + alignY, debug);
         this.iconDesiredWorkers[this.workforceColorSelectType.ordinal()].render(this.g, x + 55, y + 51 + alignY, debug);
         this.font.drawString(x + 75, y + 48 + alignY, currentDesiredColor + desiredWorkers + "$WHI0/" + maximumWorkers, Text.FontType.HEADER, 2);
         if (!this.workforceColorSelectAccentMode) {
            this.workforceJobColorBase[this.workforceColorSelectType.ordinal()].select();
            this.workforceJobColorAccent[this.workforceColorSelectType.ordinal()].deselect();
         } else {
            this.workforceJobColorBase[this.workforceColorSelectType.ordinal()].deselect();
            this.workforceJobColorAccent[this.workforceColorSelectType.ordinal()].select();
         }

         this.workforceJobColorBase[this.workforceColorSelectType.ordinal()].render(this.g, this.mouse, x + 138, y + 48 + alignY, debug);
         this.workforceJobColorAccent[this.workforceColorSelectType.ordinal()].render(this.g, this.mouse, x + 158, y + 48 + alignY, debug);
         String currentWorkersEnrouteFormatted = "";
         if (currentWorkersEnroute > 0) {
            currentWorkersEnrouteFormatted = "$GRE1+" + currentWorkersEnroute;
         }

         if (currentWorkersEnroute < 0) {
            currentWorkersEnrouteFormatted = "$RED1" + currentWorkersEnroute;
         }

         this.iconCurrentWorkers[this.workforceColorSelectType.ordinal()].render(this.g, x + 12, y + 74 + alignY, debug);
         this.font.drawString(x + 30, y + 71 + alignY, currentWorkersColor + currentWorkers + currentWorkersEnrouteFormatted, Text.FontType.HEADER, 2);
         count = 0;

         for (MobBase mx : villagerJobs.get(this.workforceColorSelectType)) {
            if (count >= 12) {
               this.font.drawString(x + 154, y + 84 + alignY, "+" + (villagerJobs.get(this.workforceColorSelectType).size() - 12), Text.FontType.BODY, 1);
               break;
            }

            count++;
            mx.renderSprite(this.g, x + 60 + mobAlignX, y + 75 + mobAlignYx + alignY, debug);
            mobAlignX += 8;
         }
      }

      this.workforceColorSelectPanel.draw(x, y + 107);
      if (this.workforceColorSelectAccentMode) {
         this.font
            .drawString(
               x + this.workforceColorSelectPanel.getWidth() / 2,
               y + 110,
               Text.getText("playLeftPanel.workforceColorSelect.selectAccentColor"),
               Text.FontType.BODY,
               1,
               true
            );
      } else {
         this.font
            .drawString(
               x + this.workforceColorSelectPanel.getWidth() / 2,
               y + 110,
               Text.getText("playLeftPanel.workforceColorSelect.selectBaseColor"),
               Text.FontType.BODY,
               1,
               true
            );
      }

      int colorButtonAlignX = 0;
      int colorButtonAlignY = 0;

      for (int i = 0; i < this.workforceColorSelectPresets.length; i++) {
         this.workforceColorSelectPresets[i].render(this.g, this.mouse, x + 15 + colorButtonAlignX, y + 130 + colorButtonAlignY, debug);
         colorButtonAlignX += 20;
         if ((i + 1) % 8 == 0) {
            colorButtonAlignX = 0;
            colorButtonAlignY += 20;
         }
      }

      int centerX = this.workforceColorSelectPanel.getWidth() / 2;
      if (!this.workforceColorSelectAccentMode) {
         this.workforceColorSelectCurrentBase.select();
         this.workforceColorSelectCurrentAccent.deselect();
      } else {
         this.workforceColorSelectCurrentBase.deselect();
         this.workforceColorSelectCurrentAccent.select();
      }

      this.font
         .drawString(x + centerX - 57, y + 295 + alignY, "$GRE1" + Text.getText("playLeftPanel.workforceColorSelect.current"), Text.FontType.BODY, 1, true);
      this.workforceColorSelectCurrentBase.setColor(this.mobJob.getCustomBaseColor(this.workforceColorSelectType));
      this.workforceColorSelectCurrentBase.render(this.g, this.mouse, x + centerX - 77, y + 310 + alignY, debug);
      this.workforceColorSelectCurrentAccent.setColor(this.mobJob.getCustomAccentColor(this.workforceColorSelectType));
      this.workforceColorSelectCurrentAccent.render(this.g, this.mouse, x + centerX - 57, y + 310 + alignY, debug);
      this.font
         .drawString(x + centerX + 57, y + 295 + alignY, "$GRE1" + Text.getText("playLeftPanel.workforceColorSelect.default"), Text.FontType.BODY, 1, true);
      this.workforceColorSelectDefaultBase.render(this.g, this.mouse, x + centerX + 37, y + 310 + alignY, debug);
      this.workforceColorSelectDefaultAccent.render(this.g, this.mouse, x + centerX + 57, y + 310 + alignY, debug);
      if (villagerJobs.get(this.workforceColorSelectType).size() > 0) {
         MobBase mx = villagerJobs.get(this.workforceColorSelectType).get(0);
         mx.renderSprite(this.g, x + centerX - 32, y + 300 + alignY, 4.0F, debug);
      }

      Color workforceColorSelected = this.mobJob.getCustomBaseColor(this.workforceColorSelectType);
      if (this.workforceColorSelectAccentMode) {
         workforceColorSelected = this.mobJob.getCustomAccentColor(this.workforceColorSelectType);
      }

      this.font.drawString(x + centerX - 40, y + 390 + alignY, "$RED1" + Text.getText("playLeftPanel.workforceColorSelect.red"), Text.FontType.BODY, 2, true);
      this.font.drawString(x + centerX - 40, y + 390 + alignY + 19, "$RED0" + workforceColorSelected.getRed(), Text.FontType.BODY, 2, true);
      this.workforceColorSelectDecreaseRed.render(this.g, this.mouse, x + centerX - 10 - 65, y + 390 + alignY + 20, debug);
      this.workforceColorSelectIncreaseRed.render(this.g, this.mouse, x + centerX - 10 - 15, y + 390 + alignY + 20, debug);
      this.font.drawString(x + centerX + 40, y + 390 + alignY, "$GRE1" + Text.getText("playLeftPanel.workforceColorSelect.green"), Text.FontType.BODY, 2, true);
      this.font.drawString(x + centerX + 40, y + 390 + alignY + 19, "$GRE0" + workforceColorSelected.getGreen(), Text.FontType.BODY, 2, true);
      this.workforceColorSelectDecreaseGreen.render(this.g, this.mouse, x + centerX - 10 + 15, y + 390 + alignY + 20, debug);
      this.workforceColorSelectIncreaseGreen.render(this.g, this.mouse, x + centerX - 10 + 65, y + 390 + alignY + 20, debug);
      this.font
         .drawString(x + centerX - 40, y + 390 + alignY + 50, "$BLU1" + Text.getText("playLeftPanel.workforceColorSelect.blue"), Text.FontType.BODY, 2, true);
      this.font.drawString(x + centerX - 40, y + 390 + alignY + 69, "$BLU0" + workforceColorSelected.getBlue(), Text.FontType.BODY, 2, true);
      this.workforceColorSelectDecreaseBlue.render(this.g, this.mouse, x + centerX - 10 - 65, y + 390 + alignY + 70, debug);
      this.workforceColorSelectIncreaseBlue.render(this.g, this.mouse, x + centerX - 10 - 15, y + 390 + alignY + 70, debug);
      this.font.drawString(x + centerX + 40, y + 390 + alignY + 50, Text.getText("playLeftPanel.workforceColorSelect.alpha"), Text.FontType.BODY, 2, true);
      this.font.drawString(x + centerX + 40, y + 390 + alignY + 69, "" + workforceColorSelected.getAlpha(), Text.FontType.BODY, 2, true);
      this.workforceColorSelectDecreaseAlpha.render(this.g, this.mouse, x + centerX - 10 + 15, y + 390 + alignY + 70, debug);
      this.workforceColorSelectIncreaseAlpha.render(this.g, this.mouse, x + centerX - 10 + 65, y + 390 + alignY + 70, debug);
   }

   private void renderResourceManagement(int x, int y, boolean debug) {
      this.resourceManagementPanel.draw(x, y + 28);
      this.font.drawString(x + this.resourceManagementPanel.getWidth() / 2, y + 30, this.leftResourceManagementCategory.getText(), Text.FontType.BODY, 1, true);
      this.resourceManagementFilterToggle.render(this.g, this.mouse, x + 10, y + 48, debug);
      switch (this.resourceManagementFilter) {
         case ALL:
            this.font.drawString(x + 30, y + 52, Text.getText("playLeftPanel.resourceManagement.sort.total"), Text.FontType.BODY, 1);
            break;
         case GROUND:
            this.font.drawString(x + 30, y + 52, Text.getText("playLeftPanel.resourceManagement.sort.ground"), Text.FontType.BODY, 1);
            break;
         case STORAGE:
            this.font.drawString(x + 30, y + 52, Text.getText("playLeftPanel.resourceManagement.sort.stored"), Text.FontType.BODY, 1);
            break;
         case EQUIPPED:
            this.font.drawString(x + 30, y + 52, Text.getText("playLeftPanel.resourceManagement.sort.equip"), Text.FontType.BODY, 1);
            break;
         case CARRIED:
            this.font.drawString(x + 30, y + 52, Text.getText("playLeftPanel.resourceManagement.sort.carry"), Text.FontType.BODY, 1);
            break;
         case RATE:
            this.font.drawString(x + 30, y + 52, Text.getText("playLeftPanel.resourceManagement.sort.rate"), Text.FontType.BODY, 1);
      }

      this.font.drawString(x + 73, y + 52, Text.getText("playLeftPanel.resourceManagement.header.make"), Text.FontType.BODY, 1);
      this.font.drawString(x + 124, y + 52, Text.getText("playLeftPanel.resourceManagement.header.maintain"), Text.FontType.BODY, 1);
      HashMap<MapTilesLoader.TileSet, HashMap<ObjectBase.ObjectSubType, ObjectFlags>> objectFlagsList = this.object.getObjectFlagFactory().getObjectFlagsList();
      int alignY = 0;
      int count = 0;

      for (ResourceModule.ResourceType t : this.resourceManagementResourceIcons.keySet()) {
         if (t.getTemplate().getResourceCategory() == this.leftResourceManagementCategory && !t.getTemplate().canHideFromResourceBar()) {
            boolean foundBuilding = false;
            ResourceModule.ResourceType foundBaseResource = null;
            ObjectFlags buildingFlags = null;

            for (HashMap<ObjectBase.ObjectSubType, ObjectFlags> o : objectFlagsList.values()) {
               MobJobBase.MobJobType j = o.get(ObjectBase.ObjectSubType.BUILT).getWorkerJobType();
               if (j != null) {
                  if (j.getJob().canWorkerHarvest(t) || j.getJob().canWorkerRefine(t) || j.getJob().canWorkerSlaughter(t)) {
                     buildingFlags = o.get(ObjectBase.ObjectSubType.BUILT);
                     if (this.object.objectCount(o.get(ObjectBase.ObjectSubType.BUILT).getBaseType(), ObjectBase.ObjectSubType.BUILT) > 0
                        || this.object.objectCount(o.get(ObjectBase.ObjectSubType.BUILT).getBaseType(), ObjectBase.ObjectSubType.CONSTRUCTION) > 0) {
                        foundBuilding = true;
                        break;
                     }
                  }

                  ResourceModule.ResourceType[] var18;
                  for (ResourceModule.ResourceType t2 : ResourceModule.ResourceType.values()) {
                     if (this.resource.getResourceRares(t2).contains(t) && (j.getJob().canWorkerHarvest(t2) || j.getJob().canWorkerRefine(t2))) {
                        foundBaseResource = t2;
                     }
                  }
               }
            }

            this.resourceManagementPanelBar.draw(x + 11, y + alignY + 65);
            this.resourceManagementResourceIcons.get(t).render(this.g, x + 10, y + alignY + 70, debug);
            switch (this.resourceManagementFilter) {
               case ALL:
                  this.font.drawString(x + 30, y + alignY + 72, "$GRE0" + this.resource.getResourceTotal(t, true), Text.FontType.BODY, 1);
                  break;
               case GROUND:
                  this.font.drawString(x + 30, y + alignY + 72, "$ORA0" + this.resource.getResourceGround(t, true), Text.FontType.BODY, 1);
                  break;
               case STORAGE:
                  this.font.drawString(x + 30, y + alignY + 72, "$MAG0" + this.resource.getResourceStores(t), Text.FontType.BODY, 1);
                  break;
               case EQUIPPED:
                  this.font.drawString(x + 30, y + alignY + 72, "$YEL0" + this.resource.getResourceEquipped(t), Text.FontType.BODY, 1);
                  break;
               case CARRIED:
                  this.font.drawString(x + 30, y + alignY + 72, "$PUR0" + this.resource.getResourceCarried(t), Text.FontType.BODY, 1);
                  break;
               case RATE:
                  if (this.resource.getResourceRate(t) > 0) {
                     this.font.drawString(x + 30, y + alignY + 72, "$GRE0+" + this.resource.getResourceRate(t), Text.FontType.BODY, 1);
                  } else if (this.resource.getResourceRate(t) < 0) {
                     this.font.drawString(x + 30, y + alignY + 72, "$RED0" + this.resource.getResourceRate(t), Text.FontType.BODY, 1);
                  } else {
                     this.font.drawString(x + 30, y + alignY + 72, "0", Text.FontType.BODY, 1);
                  }
            }

            if (foundBuilding && foundBaseResource == null) {
               this.resourceManagementResourceMakeDown.get(t).render(this.g, this.mouse, x + 58, y + alignY + 70, debug);
               if (this.resource.getResourceMakeAmount(t) == ResourceModule.RESOURCE_INFINITE_AMOUNT) {
                  this.resourceManagementMakeInfinityIcons.get(t).render(this.g, x + 80, y + alignY + 72, debug);
               } else if (this.resource.getResourceMakeAmount(t) == ResourceModule.RESOURCE_STORAGE_AMOUNT) {
                  this.resourceManagementMakeStorageIcons.get(t).render(this.g, x + 141, y + alignY + 72, debug);
               } else {
                  this.font.drawString(x + 88, y + alignY + 72, String.valueOf(this.resource.getResourceMakeAmount(t)), Text.FontType.BODY, 1, true);
               }

               this.resourceManagementResourceMakeUp.get(t).render(this.g, this.mouse, x + 99, y + alignY + 70, debug);
               this.resourceManagementResourceMaintainDown.get(t).render(this.g, this.mouse, x + 119, y + alignY + 70, debug);
               if (this.resource.getResourceMaintainAmount(t) == ResourceModule.RESOURCE_INFINITE_AMOUNT) {
                  this.resourceManagementMaintainInfinityIcons.get(t).render(this.g, x + 141, y + alignY + 72, debug);
               } else if (this.resource.getResourceMaintainAmount(t) == ResourceModule.RESOURCE_STORAGE_AMOUNT) {
                  this.resourceManagementMaintainStorageIcons.get(t).render(this.g, x + 141, y + alignY + 72, debug);
               } else {
                  String colorOut = "$YEL1";
                  if (this.resource.getResourceMaintainAmount(t) <= this.resource.getResourceTotal(t, true)) {
                     colorOut = "$GRE1";
                  }

                  this.font.drawString(x + 149, y + alignY + 72, colorOut + this.resource.getResourceMaintainAmount(t), Text.FontType.BODY, 1, true);
               }

               this.resourceManagementResourceMaintainUp.get(t).render(this.g, this.mouse, x + 160, y + alignY + 70, debug);
            } else if (t == ResourceModule.ResourceType.EERIE_VESSEL) {
               this.font
                  .drawString(x + 119, y + alignY + 73, Text.getText("playLeftPanel.resourceManagement.createdByOccultists"), Text.FontType.BODY, 0, true);
            } else if (t == ResourceModule.ResourceType.WATER_BUCKET) {
               this.font
                  .drawString(x + 119, y + alignY + 73, Text.getText("playLeftPanel.resourceManagement.producedByWaterSource"), Text.FontType.BODY, 0, true);
            } else if (foundBaseResource != null) {
               Text.setVariableText("resourceOrMonsterType", foundBaseResource.getTemplate().getName());
               this.font.drawString(x + 119, y + alignY + 73, Text.getText("playLeftPanel.resourceManagement.rareDrop"), Text.FontType.BODY, 0, true);
            } else if (buildingFlags == null) {
               this.font.drawString(x + 119, y + alignY + 73, Text.getText("playLeftPanel.resourceManagement.monsterDrop"), Text.FontType.BODY, 0, true);
            } else if (buildingFlags.getBaseName()
               .equals(this.object.getObjectFlagFactory().getObjectFlags(MapTilesLoader.TileSet.WELL, ObjectBase.ObjectSubType.BUILT).getBaseName())) {
               this.font
                  .drawString(x + 119, y + alignY + 73, Text.getText("playLeftPanel.resourceManagement.requiresWaterSource"), Text.FontType.BODY, 0, true);
            } else {
               Text.setVariableText("buildingName", buildingFlags.getBaseName());
               this.font.drawString(x + 119, y + alignY + 73, Text.getText("playLeftPanel.resourceManagement.requiresBuilding"), Text.FontType.BODY, 0, true);
            }

            alignY += 25;
            if (++count > 16) {
               break;
            }
         }
      }

      this.leftResourceManagementPageLeftTop.render(this.g, this.mouse, x + 67, y, false, debug);
      this.leftResourceManagementPageLeftBottom.render(this.g, this.mouse, x + 67, y + 502, false, debug);
      this.leftResourceManagementPageRightTop.render(this.g, this.mouse, x + 96, y, false, debug);
      this.leftResourceManagementPageRightBottom.render(this.g, this.mouse, x + 96, y + 502, false, debug);
   }

   private void renderTrade(int x, int y, boolean debug) throws SlickException {
      MobJobBase.MobJobType jt = MobJobBase.MobJobType.PROVISIONER;
      this.tradePanel.draw(x, y + 107);
      this.availableWorkersPanel.draw(x + 135, y);
      this.iconUnemployedVillagers.render(this.g, x + 139, y + 6, debug);
      this.font.drawString(x + 152, y + 3, "$YEL1" + this.mobJob.getUnassignedVillagers(), Text.FontType.HEADER, 2);
      ArrayList<MobBase> villagers = this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false);
      EnumMap<MobJobBase.MobJobType, ArrayList<MobBase>> villagerJobs = new EnumMap<>(MobJobBase.MobJobType.class);

      MobJobBase.MobJobType[] mobAlignX;
      for (MobJobBase.MobJobType t : mobAlignX = MobJobBase.MobJobType.values()) {
         villagerJobs.put(t, new ArrayList<>());
      }

      int totalWorkers = 0;

      for (MobBase m : villagers) {
         if (m.getMobFlags().getAllowWork()) {
            villagerJobs.get(m.getMobJobType()).add(m);
            totalWorkers++;
         }
      }

      String currentDesiredColor = "$RED1";
      if (this.mobJob.getTotalDesiredWorkers() <= totalWorkers) {
         currentDesiredColor = "$GRE1";
      }

      int alignY = 0;
      int mobAlignXx = 0;
      int mobAlignY = 0;
      int count = 0;
      if (this.mobJob.getMaximumWorkers(jt) != 0) {
         this.workforceWorkerPanel[jt.ordinal()].render(this.g, this.mouse, x, y + 28 + alignY, false, debug);
         int desiredWorkers = this.mobJob.getDesiredWorkers(jt);
         int maximumWorkers = this.mobJob.getMaximumWorkers(jt);
         int currentWorkers = this.mobJob.getCurrentWorkers(jt);
         int currentWorkersEnroute = this.mobJob.getCurrentWorkers(jt, true) - currentWorkers;
         String currentWorkersColor = "$GRE1";
         if (currentWorkers < desiredWorkers * 0.4F) {
            currentWorkersColor = "$RED1";
         } else if (currentWorkers < desiredWorkers * 0.6F) {
            currentWorkersColor = "$ORA1";
         } else if (currentWorkers < desiredWorkers * 0.8F) {
            currentWorkersColor = "$YEL1";
         }

         this.workforceDesiredDecrease[jt.ordinal()].render(this.g, this.mouse, x + 12, y + 48 + alignY, debug);
         this.workforceDesiredIncrease[jt.ordinal()].render(this.g, this.mouse, x + 32, y + 48 + alignY, debug);
         this.iconDesiredWorkers[jt.ordinal()].render(this.g, x + 55, y + 51 + alignY, debug);
         this.font.drawString(x + 75, y + 48 + alignY, currentDesiredColor + desiredWorkers + "$WHI0/" + maximumWorkers, Text.FontType.HEADER, 2);
         this.workforceJobColorBase[jt.ordinal()].render(this.g, this.mouse, x + 138, y + 48 + alignY, debug);
         this.workforceJobColorAccent[jt.ordinal()].render(this.g, this.mouse, x + 158, y + 48 + alignY, debug);
         String currentWorkersEnrouteFormatted = "";
         if (currentWorkersEnroute > 0) {
            currentWorkersEnrouteFormatted = "$GRE1+" + currentWorkersEnroute;
         }

         if (currentWorkersEnroute < 0) {
            currentWorkersEnrouteFormatted = "$RED1" + currentWorkersEnroute;
         }

         this.iconCurrentWorkers[jt.ordinal()].render(this.g, x + 12, y + 74 + alignY, debug);
         this.font.drawString(x + 30, y + 71 + alignY, currentWorkersColor + currentWorkers + currentWorkersEnrouteFormatted, Text.FontType.HEADER, 2);
         count = 0;

         for (MobBase mx : villagerJobs.get(jt)) {
            if (count >= 12) {
               this.font.drawString(x + 154, y + 84 + alignY, "+" + (villagerJobs.get(jt).size() - 12), Text.FontType.BODY, 1);
               break;
            }

            count++;
            mx.renderSprite(this.g, x + 60 + mobAlignXx, y + 75 + mobAlignY + alignY, debug);
            mobAlignXx += 8;
         }
      }

      this.font.drawString(x + this.tradePanel.getWidth() / 2, y + 110, this.tradeCategory.getText(), Text.FontType.BODY, 1, true);
      count = 0;

      ResourceModule.ResourceType[] var44;
      for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
         if (t.getTemplate().getResourceCategory() == this.tradeCategory && t.getTemplate().canTrade() && !t.getTemplate().canHideFromResourceBar()) {
            this.tradePanelBar.draw(x + 11, y + alignY + 128);
            this.tradeResourceIcons.get(t).render(this.g, x + 10, y + alignY + 132, debug);
            if (this.object.objectCount(MapTilesLoader.TileSet.MARKETPLACE) > 0) {
               this.tradeBuy.get(t).render(this.g, this.mouse, x + 27, y + alignY + 134, debug);
               this.tradeSell.get(t).render(this.g, this.mouse, x + 68, y + alignY + 134, debug);
               if (this.trade.isModeBuy(t)) {
                  this.tradeBuy.get(t).select();
                  this.tradeSell.get(t).deselect();
               } else if (this.trade.isModeSell(t)) {
                  this.tradeBuy.get(t).deselect();
                  this.tradeSell.get(t).select();
               } else {
                  this.tradeBuy.get(t).deselect();
                  this.tradeSell.get(t).deselect();
               }

               if (this.trade.isModeBuy(t)) {
                  this.tradeResourceBuyDown.get(t).render(this.g, this.mouse, x + 109, y + alignY + 134, debug);
                  this.font.drawString(x + 143, y + alignY + 136, String.valueOf(this.trade.getTradeAmount(t)), Text.FontType.BODY, 1, true);
                  this.tradeResourceBuyUp.get(t).render(this.g, this.mouse, x + 158, y + alignY + 134, debug);
               }

               if (this.trade.isModeSell(t)) {
                  this.tradeResourceSellDown.get(t).render(this.g, this.mouse, x + 109, y + alignY + 134, debug);
                  this.font.drawString(x + 143, y + alignY + 136, String.valueOf(this.trade.getTradeAmount(t)), Text.FontType.BODY, 1, true);
                  this.tradeResourceSellUp.get(t).render(this.g, this.mouse, x + 158, y + alignY + 134, debug);
               }
            } else {
               this.font.drawString(x + 100, y + alignY + 136, Text.getText("playLeftPanel.trade.requiresMarketplace"), Text.FontType.BODY, 1, true);
            }

            String totalOut = "";
            int width = 0;
            this.tradeVillageStockIcons.get(t).render(this.g, x + 15, y + alignY + 155, debug);
            if (this.resource.getResourceTotal(t, true) - this.resource.getResourceEquipped(t) <= 0) {
               this.font.drawString(x + 33, y + alignY + 155, Text.getText("playLeftPanel.trade.empty"), Text.FontType.BODY, 1);
            } else {
               Text.setVariableText("amount", this.resource.getResourceTotal(t, true) - this.resource.getResourceEquipped(t));
               Text.setVariableText("price", this.trade.getSellPrice(t));
               totalOut = Text.getText("playLeftPanel.trade.sellAmountAndPrice");
               width = Text.getWidth(totalOut, Text.FontType.BODY, 1);
               this.font.drawString(x + 33, y + alignY + 155, totalOut, Text.FontType.BODY, 1);
               this.tradeGoldVillageStockIcons.get(t).render(this.g, x + 33 + width - 3, y + alignY + 155, debug);
            }

            this.tradeCatjeetStockIcons.get(t).render(this.g, x + 95, y + alignY + 155, debug);
            if (this.resource.getProvisionerCount(t) <= 0) {
               Text.setVariableText("amount", this.resource.getProvisionerCount(t));
               Text.setVariableText("price", this.trade.getBuyPrice(t));
               totalOut = Text.getText("playLeftPanel.trade.buyAmountAndPriceTooHigh");
               width = Text.getWidth(totalOut, Text.FontType.BODY, 1);
               this.font.drawString(x + 110, y + alignY + 155, totalOut, Text.FontType.BODY, 1);
            } else {
               Text.setVariableText("amount", this.resource.getProvisionerCount(t));
               Text.setVariableText("price", this.trade.getBuyPrice(t));
               totalOut = Text.getText("playLeftPanel.trade.buyAmountAndPrice");
               width = Text.getWidth(totalOut, Text.FontType.BODY, 1);
               this.font.drawString(x + 110, y + alignY + 155, totalOut, Text.FontType.BODY, 1);
            }

            this.tradeGoldCatjeetStockIcons.get(t).render(this.g, x + 110 + width - 3, y + alignY + 155, debug);
            alignY += 47;
            if (++count > 8) {
               break;
            }
         }
      }

      int var26 = 314;
      this.tradePanelBar.draw(x + 11, y + var26 + 128);
      this.font.drawString(x + this.tradePanel.getWidth() / 2, y + 425, Text.getText("playLeftPanel.trade.catjeetLaborersHeader"), Text.FontType.BODY, 1, true);
      count = 0;
      int var28 = 0;
      int var29 = 0;
      ArrayList<MobBase> laborers = this.mob.getMobsByAssignment(Assignment.AssignmentType.CATJEET_SELLING_LABOR, false);

      for (MobBase mx : laborers) {
         if (count % 6 == 0 && count != 0) {
            var28 = 0;
            var29 += 10;
         }

         if (count > 17) {
            break;
         }

         count++;
         mx.renderSprite(this.g, x + 10 + var28, y + 135 + var29 + var26, debug);
         var28 += 8;
      }

      if (this.object.objectCount(MapTilesLoader.TileSet.MARKETPLACE) > 0) {
         this.tradeLaborHire.render(this.g, this.mouse, x + 68, y + var26 + 134, debug);
         if (this.trade.isLaborModeBuy()) {
            this.tradeLaborHire.select();
         } else {
            this.tradeLaborHire.deselect();
         }
      } else {
         this.font.drawString(x + 100, y + var26 + 136, Text.getText("playLeftPanel.trade.requiresMarketplace"), Text.FontType.BODY, 1, true);
      }

      if (this.trade.isLaborModeBuy()) {
         this.tradeLaborDown.render(this.g, this.mouse, x + 109, y + var26 + 134, debug);
         this.font.drawString(x + 143, y + var26 + 136, String.valueOf(this.trade.getLaborTradeAmount()), Text.FontType.BODY, 1, true);
         this.tradeLaborUp.render(this.g, this.mouse, x + 158, y + var26 + 134, debug);
      }

      String totalOutx = "";
      int widthx = 0;
      this.tradeCatjeetLaborerStockIcons.render(this.g, x + 95, y + var26 + 155, debug);
      if (laborers.size() <= 0) {
         this.font.drawString(x + 50, y + var26 + 155, Text.getText("playLeftPanel.trade.noLaborers"), Text.FontType.BODY, 1, true);
         Text.setVariableText("amount", laborers.size());
         Text.setVariableText("price", this.trade.getLaborBuyPrice());
         totalOutx = Text.getText("playLeftPanel.trade.buyAmountAndPriceTooHigh");
         widthx = Text.getWidth(totalOutx, Text.FontType.BODY, 1);
         this.font.drawString(x + 110, y + var26 + 155, totalOutx, Text.FontType.BODY, 1);
      } else {
         Text.setVariableText("amount", laborers.size());
         Text.setVariableText("price", this.trade.getLaborBuyPrice());
         totalOutx = Text.getText("playLeftPanel.trade.buyAmountAndPrice");
         widthx = Text.getWidth(totalOutx, Text.FontType.BODY, 1);
         this.font.drawString(x + 110, y + var26 + 155, totalOutx, Text.FontType.BODY, 1);
      }

      this.tradeGoldCatjeetLaborerStockIcons.render(this.g, x + 110 + widthx - 3, y + var26 + 155, debug);
      this.leftTradePageLeftTop.render(this.g, this.mouse, x + 67, y, false, debug);
      this.leftTradePageLeftBottom.render(this.g, this.mouse, x + 67, y + 502, false, debug);
      this.leftTradePageRightTop.render(this.g, this.mouse, x + 96, y, false, debug);
      this.leftTradePageRightBottom.render(this.g, this.mouse, x + 96, y + 502, false, debug);
   }

   private void renderMigration(int x, int y, boolean debug) {
      this.migrationPanel.draw(x, y + 28);
      this.availableWorkersPanel.draw(x + 135, y);
      this.iconUnemployedVillagers.render(this.g, x + 139, y + 6, debug);
      this.font.drawString(x + 152, y + 3, "$YEL1" + this.mobJob.getUnassignedVillagers(), Text.FontType.HEADER, 2);
      this.font
         .drawString(x + this.migrationPanel.getWidth() / 2, y + 30, Text.getText("playLeftPanel.migration.availableVillagers"), Text.FontType.BODY, 1, true);
      this.iconMigratableVillagers.render(this.g, x + 8, y + 50, debug);
      this.font.drawString(x + 25, y + 50, "$GRE1" + this.migration.getAvailableVillagers(), Text.FontType.BODY, 1);
      this.iconMaximumMigrationsPerDay.render(this.g, x + 53, y + 50, debug);
      this.font.drawString(x + 70, y + 50, "$GRE1" + this.object.getMaximumMigrationsPerDay(), Text.FontType.BODY, 1);
      this.iconReadyToMigrate.render(this.g, x + 98, y + 50, debug);
      this.font
         .drawString(
            x + 115,
            y + 50,
            "$YEL1"
               + (
                  this.mob.getMobsByType(MobBase.MobType.HUMAN_MIGRANT_DEPARTING, false).size()
                     + this.mob.getMobsByType(MobBase.MobType.CATJEET_MIGRANT_DEPARTING, false).size()
               ),
            Text.FontType.BODY,
            1
         );
      this.iconTotalMigratedToday.render(this.g, x + 141, y + 50, debug);
      this.font.drawString(x + 158, y + 50, "$YEL1" + this.migration.getTotalMigratedToday(), Text.FontType.BODY, 1);
      this.font.drawString(x + this.migrationPanel.getWidth() / 2, y + 80, Text.getText("playLeftPanel.migration.regionList"), Text.FontType.BODY, 1, true);
      String[] mapList = this.map.getConnectedRegionList();
      int alignY = 0;

      for (int i = 0; i < 8; i++) {
         if (!mapList[i].equals("null")) {
            this.migrationPanelBar.draw(x + 11, y + alignY + 102);
            this.migrationArea[i].render(this.g, this.mouse, x + 11, y + alignY + 99, false, debug);
            if (this.map.getConnectedRegionListWorldLost(mapList[i])) {
               this.font.drawString(x + 17, y + alignY + 108, "$RED1" + this.map.getConnectedRegionListWorldName(i), Text.FontType.BODY, 1);
            } else if (this.migration.getCurrentPopulation().get(mapList[i]) > 0) {
               this.font.drawString(x + 17, y + alignY + 108, "$GRE1" + this.map.getConnectedRegionListWorldName(i), Text.FontType.BODY, 1);
            } else {
               this.font.drawString(x + 17, y + alignY + 108, "$YEL0" + this.map.getConnectedRegionListWorldName(i), Text.FontType.BODY, 1);
            }

            if (this.map.getConnectedRegionListWorldLost(mapList[i])) {
               this.font.drawString(x + 28, y + alignY + 127, Text.getText("playLeftPanel.migration.regionLost"), Text.FontType.BODY, 1);
            } else {
               this.iconRegionsTotalPopulation[i].render(this.g, x + 13, y + alignY + 127, debug);
               this.font.drawString(x + 28, y + alignY + 127, "$YEL1" + this.migration.getCurrentPopulation().get(mapList[i]), Text.FontType.BODY, 1);
               this.iconMigrationEnRoute[i].render(this.g, x + 63, y + alignY + 127, debug);
               this.font.drawString(x + 80, y + alignY + 127, "$YEL1" + this.migration.getEnroutePopulation(mapList[i]), Text.FontType.BODY, 1);
               this.font.drawString(x + 145, y + alignY + 108, Text.getText("playLeftPanel.migration.send"), Text.FontType.BODY, 1, true);
               this.migrationDecrease[i].render(this.g, this.mouse, x + 115, y + alignY + 125, debug);
               this.font.drawString(x + 145, y + alignY + 127, "$YEL1" + this.migration.getRequestedPopulation().get(mapList[i]), Text.FontType.BODY, 1, true);
               this.migrationIncrease[i].render(this.g, this.mouse, x + 155, y + alignY + 125, debug);
            }

            alignY += 48;
         }
      }
   }

   private void renderCourier(int x, int y, boolean debug) throws SlickException {
      MobJobBase.MobJobType jt = MobJobBase.MobJobType.COURIER_SUPPLIER;
      this.courierPanel.draw(x, y + 107);
      this.availableWorkersPanel.draw(x + 135, y);
      this.iconUnemployedVillagers.render(this.g, x + 139, y + 6, debug);
      this.font.drawString(x + 152, y + 3, "$YEL1" + this.mobJob.getUnassignedVillagers(), Text.FontType.HEADER, 2);
      ArrayList<MobBase> villagers = this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false);
      EnumMap<MobJobBase.MobJobType, ArrayList<MobBase>> villagerJobs = new EnumMap<>(MobJobBase.MobJobType.class);

      MobJobBase.MobJobType[] mobAlignY;
      for (MobJobBase.MobJobType t : mobAlignY = MobJobBase.MobJobType.values()) {
         villagerJobs.put(t, new ArrayList<>());
      }

      int totalWorkers = 0;

      for (MobBase m : villagers) {
         if (m.getMobFlags().getAllowWork()) {
            villagerJobs.get(m.getMobJobType()).add(m);
            totalWorkers++;
         }
      }

      int alignY = 0;
      int mobAlignX = 0;
      int mobAlignYx = 0;
      int count = 0;
      String currentDesiredColor = "$RED1";
      if (this.mobJob.getTotalDesiredWorkers() <= totalWorkers) {
         currentDesiredColor = "$GRE1";
      }

      if (this.mobJob.getMaximumWorkers(jt) != 0) {
         this.workforceWorkerPanel[jt.ordinal()].render(this.g, this.mouse, x, y + 28 + alignY, false, debug);
         int desiredWorkers = this.mobJob.getDesiredWorkers(jt);
         int maximumWorkers = this.mobJob.getMaximumWorkers(jt);
         int currentWorkers = this.mobJob.getCurrentWorkers(jt);
         int currentWorkersEnroute = this.mobJob.getCurrentWorkers(jt, true) - currentWorkers;
         String currentWorkersColor = "$GRE1";
         if (currentWorkers < desiredWorkers * 0.4F) {
            currentWorkersColor = "$RED1";
         } else if (currentWorkers < desiredWorkers * 0.6F) {
            currentWorkersColor = "$ORA1";
         } else if (currentWorkers < desiredWorkers * 0.8F) {
            currentWorkersColor = "$YEL1";
         }

         this.workforceDesiredDecrease[jt.ordinal()].render(this.g, this.mouse, x + 12, y + 48 + alignY, debug);
         this.workforceDesiredIncrease[jt.ordinal()].render(this.g, this.mouse, x + 32, y + 48 + alignY, debug);
         this.iconDesiredWorkers[jt.ordinal()].render(this.g, x + 55, y + 51 + alignY, debug);
         this.font.drawString(x + 75, y + 48 + alignY, currentDesiredColor + desiredWorkers + "$WHI0/" + maximumWorkers, Text.FontType.HEADER, 2);
         this.workforceJobColorBase[jt.ordinal()].render(this.g, this.mouse, x + 138, y + 48 + alignY, debug);
         this.workforceJobColorAccent[jt.ordinal()].render(this.g, this.mouse, x + 158, y + 48 + alignY, debug);
         String currentWorkersEnrouteFormatted = "";
         if (currentWorkersEnroute > 0) {
            currentWorkersEnrouteFormatted = "$GRE1+" + currentWorkersEnroute;
         }

         if (currentWorkersEnroute < 0) {
            currentWorkersEnrouteFormatted = "$RED1" + currentWorkersEnroute;
         }

         this.iconCurrentWorkers[jt.ordinal()].render(this.g, x + 12, y + 74 + alignY, debug);
         this.font.drawString(x + 30, y + 71 + alignY, currentWorkersColor + currentWorkers + currentWorkersEnrouteFormatted, Text.FontType.HEADER, 2);
         count = 0;

         for (MobBase mx : villagerJobs.get(jt)) {
            if (count >= 12) {
               this.font.drawString(x + 154, y + 84 + alignY, "+" + (villagerJobs.get(jt).size() - 12), Text.FontType.BODY, 1);
               break;
            }

            count++;
            mx.renderSprite(this.g, x + 60 + mobAlignX, y + 75 + mobAlignYx + alignY, debug);
            mobAlignX += 8;
         }
      }

      this.font
         .drawString(x + this.courierPanel.getWidth() / 2, y + 110, Text.getText("playLeftPanel.courier.header.selectedRegion"), Text.FontType.BODY, 1, true);
      String[] mapList = this.map.getConnectedRegionList();
      this.courierPanelBar.draw(x + 11, y + 126);
      this.courierArea[this.selectedCourierMapSlot].render(this.g, this.mouse, x + 8, y + 123, false, debug);
      this.courierLastRegion.render(this.g, this.mouse, x + 13, y + 131, debug);
      if (this.map.getConnectedRegionListWorldLost(mapList[this.selectedCourierMapSlot])) {
         this.font
            .drawString(
               x + this.courierPanel.getWidth() / 2,
               y + 134,
               "$RED1" + this.map.getConnectedRegionListWorldName(this.selectedCourierMapSlot),
               Text.FontType.BODY,
               1,
               true
            );
      } else if (this.migration.getCurrentPopulation().get(mapList[this.selectedCourierMapSlot]) > 0) {
         this.font
            .drawString(
               x + this.courierPanel.getWidth() / 2,
               y + 134,
               "$GRE1" + this.map.getConnectedRegionListWorldName(this.selectedCourierMapSlot),
               Text.FontType.BODY,
               1,
               true
            );
      } else {
         this.font
            .drawString(
               x + this.courierPanel.getWidth() / 2,
               y + 134,
               "$YEL0" + this.map.getConnectedRegionListWorldName(this.selectedCourierMapSlot),
               Text.FontType.BODY,
               1,
               true
            );
      }

      this.courierNextRegion.render(this.g, this.mouse, x + 158, y + 131, debug);
      if (this.map.getConnectedRegionListWorldLost(mapList[this.selectedCourierMapSlot])) {
         this.font.drawString(x + this.courierPanel.getWidth() / 2, y + 234, Text.getText("playLeftPanel.courier.regionLost"), Text.FontType.BODY, 1, true);
      } else {
         int var24 = 166;
         count = 0;
         this.font.drawString(x + this.courierPanel.getWidth() / 2, y + 196, this.leftCourierCategory.getText(), Text.FontType.BODY, 1, true);
         this.courierFilterToggle.render(this.g, this.mouse, x + 10, y + 214, debug);
         switch (this.courierFilter) {
            case ALL:
               this.font.drawString(x + 30, y + 218, Text.getText("playLeftPanel.courier.sort.total"), Text.FontType.BODY, 1);
               break;
            case GROUND:
               this.font.drawString(x + 30, y + 218, Text.getText("playLeftPanel.courier.sort.ground"), Text.FontType.BODY, 1);
               break;
            case STORAGE:
               this.font.drawString(x + 30, y + 218, Text.getText("playLeftPanel.courier.sort.stored"), Text.FontType.BODY, 1);
               break;
            case EQUIPPED:
               this.font.drawString(x + 30, y + 218, Text.getText("playLeftPanel.courier.sort.equip"), Text.FontType.BODY, 1);
               break;
            case CARRIED:
               this.font.drawString(x + 30, y + 218, Text.getText("playLeftPanel.courier.sort.carry"), Text.FontType.BODY, 1);
               break;
            case RATE:
               this.font.drawString(x + 30, y + 218, Text.getText("playLeftPanel.courier.sort.rate"), Text.FontType.BODY, 1);
         }

         this.font.drawString(x + 76, y + 218, Text.getText("playLeftPanel.courier.header.send"), Text.FontType.BODY, 1);
         this.font.drawString(x + 136, y + 218, Text.getText("playLeftPanel.courier.header.daily"), Text.FontType.BODY, 1);

         for (ResourceModule.ResourceType t : this.courierResourceIcons.keySet()) {
            if (t.getTemplate().getResourceCategory() == this.leftCourierCategory && !t.getTemplate().canHideFromResourceBar()) {
               this.courierPanelBar.draw(x + 11, y + var24 + 65);
               this.courierResourceIcons.get(t).render(this.g, x + 10, y + var24 + 70, debug);
               switch (this.courierFilter) {
                  case ALL:
                     this.font.drawString(x + 30, y + var24 + 72, "$GRE0" + this.resource.getResourceTotal(t, true), Text.FontType.BODY, 1);
                     break;
                  case GROUND:
                     this.font.drawString(x + 30, y + var24 + 72, "$ORA0" + this.resource.getResourceGround(t, true), Text.FontType.BODY, 1);
                     break;
                  case STORAGE:
                     this.font.drawString(x + 30, y + var24 + 72, "$MAG0" + this.resource.getResourceStores(t), Text.FontType.BODY, 1);
                     break;
                  case EQUIPPED:
                     this.font.drawString(x + 30, y + var24 + 72, "$YEL0" + this.resource.getResourceEquipped(t), Text.FontType.BODY, 1);
                     break;
                  case CARRIED:
                     this.font.drawString(x + 30, y + var24 + 72, "$PUR0" + this.resource.getResourceCarried(t), Text.FontType.BODY, 1);
                     break;
                  case RATE:
                     if (this.resource.getResourceRate(t) > 0) {
                        this.font.drawString(x + 30, y + var24 + 72, "$GRE0+" + this.resource.getResourceRate(t), Text.FontType.BODY, 1);
                     } else if (this.resource.getResourceRate(t) < 0) {
                        this.font.drawString(x + 30, y + var24 + 72, "$RED0" + this.resource.getResourceRate(t), Text.FontType.BODY, 1);
                     } else {
                        this.font.drawString(x + 30, y + var24 + 72, "0", Text.FontType.BODY, 1);
                     }
               }

               this.courierResourceSendNowDown.get(t).render(this.g, this.mouse, x + 58, y + var24 + 70, debug);
               if (this.migration.getCourierResourceSendAmount(this.selectedCourierMap, t) == MigrationModule.RESOURCE_INFINITE_AMOUNT) {
                  this.courierMakeInfinityIcons.get(t).render(this.g, x + 80, y + var24 + 72, debug);
               } else {
                  this.font
                     .drawString(
                        x + 88,
                        y + var24 + 72,
                        String.valueOf(this.migration.getCourierResourceSendAmount(this.selectedCourierMap, t)),
                        Text.FontType.BODY,
                        1,
                        true
                     );
               }

               this.courierResourceSendNowUp.get(t).render(this.g, this.mouse, x + 99, y + var24 + 70, debug);
               this.courierResourceDailyDown.get(t).render(this.g, this.mouse, x + 119, y + var24 + 70, debug);
               if (this.migration.getCourierResourceDailyAmount(this.selectedCourierMap, t) == MigrationModule.RESOURCE_INFINITE_AMOUNT) {
                  this.courierMaintainInfinityIcons.get(t).render(this.g, x + 141, y + var24 + 72, debug);
               } else {
                  int amountLeft = this.migration.getCourierResourceDailyAmount(this.selectedCourierMap, t)
                     - this.migration.getCourierResourceDailySentTodayAmounts(this.selectedCourierMap, t);
                  String colorOut = "$RED1";
                  if (amountLeft > 0
                     && this.time.getTimeOfDay().ordinal() > TimeModule.TimeOfDay.MIDDAY.ordinal()
                     && this.mob.getMobsByAssignment(Assignment.AssignmentType.COURIER_DEPARTING, false).size() <= 0) {
                     colorOut = "$RED1";
                  } else if (amountLeft > 0) {
                     colorOut = "$YEL1";
                  } else if (amountLeft <= 0) {
                     colorOut = "$GRE1";
                  }

                  this.font
                     .drawString(
                        x + 149,
                        y + var24 + 72,
                        colorOut + this.migration.getCourierResourceDailyAmount(this.selectedCourierMap, t),
                        Text.FontType.BODY,
                        1,
                        true
                     );
               }

               this.courierResourceDailyUp.get(t).render(this.g, this.mouse, x + 160, y + var24 + 70, debug);
               this.courierResourceArea.get(t).render(this.g, this.mouse, x + 8, y + var24 + 62, false, debug);
               var24 += 25;
               if (++count > 9) {
                  break;
               }
            }
         }

         this.availableWorkersPanel.draw(x + 135, y + 166);
         this.iconCourierGolem.render(this.g, x + 139, y + 172, debug);
         ArrayList<MobBase> courierGolems = this.mob.getMobsByAssignment(Assignment.AssignmentType.COURIER_PARKED, false);
         this.font.drawString(x + 152, y + 169, "$YEL1" + courierGolems.size(), Text.FontType.HEADER, 2);
         this.leftCourierPageLeftTop.render(this.g, this.mouse, x + 67, y + 166, false, debug);
         this.leftCourierPageLeftBottom.render(this.g, this.mouse, x + 67, y + 502, false, debug);
         this.leftCourierPageRightTop.render(this.g, this.mouse, x + 96, y + 166, false, debug);
         this.leftCourierPageRightBottom.render(this.g, this.mouse, x + 96, y + 502, false, debug);
      }
   }

   private int renderBlankPanels(int i, int x, int y, int yShift, Graphics g) {
      if (i >= 6) {
         g.setColor(new Color(26, 26, 26, 255));
         g.fillRect(x + 10, y + 28 + yShift + 19, this.blankLargePanel1.getWidth() - 20, this.blankLargePanel1.getHeight() - 29);
         this.blankLargePanel1.draw(x, y + 28 + yShift);
         this.blankLargePanel1.drawFlash(x, y + 28 + yShift, this.blankLargePanel1.getWidth(), this.blankLargePanel1.getHeight(), new Color(0, 0, 0, 200));
         yShift += this.blankLargePanel1.getHeight();
      }

      if (i >= 5) {
         g.setColor(new Color(26, 26, 26, 255));
         g.fillRect(x + 10, y + 28 + yShift + 19, this.blankLargePanel2.getWidth() - 20, this.blankLargePanel2.getHeight() - 29);
         this.blankLargePanel2.draw(x, y + 28 + yShift);
         this.blankLargePanel2.drawFlash(x, y + 28 + yShift, this.blankLargePanel2.getWidth(), this.blankLargePanel2.getHeight(), new Color(0, 0, 0, 200));
         yShift += this.blankLargePanel2.getHeight();
      }

      if (i >= 4) {
         g.setColor(new Color(26, 26, 26, 255));
         g.fillRect(x + 10, y + 28 + yShift + 19, this.blankLargePanel3.getWidth() - 20, this.blankLargePanel3.getHeight() - 29);
         this.blankLargePanel3.draw(x, y + 28 + yShift);
         this.blankLargePanel3.drawFlash(x, y + 28 + yShift, this.blankLargePanel3.getWidth(), this.blankLargePanel3.getHeight(), new Color(0, 0, 0, 200));
         yShift += this.blankLargePanel3.getHeight();
      }

      if (i >= 3) {
         g.setColor(new Color(26, 26, 26, 255));
         g.fillRect(x + 10, y + 28 + yShift + 19, this.blankLargePanel4.getWidth() - 20, this.blankLargePanel4.getHeight() - 29);
         this.blankLargePanel4.draw(x, y + 28 + yShift);
         this.blankLargePanel4.drawFlash(x, y + 28 + yShift, this.blankLargePanel4.getWidth(), this.blankLargePanel4.getHeight(), new Color(0, 0, 0, 200));
         yShift += this.blankLargePanel4.getHeight();
      }

      if (i >= 2) {
         g.setColor(new Color(26, 26, 26, 255));
         g.fillRect(x + 10, y + 28 + yShift + 19, this.blankLargePanel5.getWidth() - 20, this.blankLargePanel5.getHeight() - 29);
         this.blankLargePanel5.draw(x, y + 28 + yShift);
         this.blankLargePanel5.drawFlash(x, y + 28 + yShift, this.blankLargePanel5.getWidth(), this.blankLargePanel5.getHeight(), new Color(0, 0, 0, 200));
         yShift += this.blankLargePanel5.getHeight();
      }

      if (i >= 1) {
         g.setColor(new Color(26, 26, 26, 255));
         g.fillRect(x + 10, y + 28 + yShift + 19, this.blankLargePanel6.getWidth() - 20, this.blankLargePanel6.getHeight() - 29);
         this.blankLargePanel6.draw(x, y + 28 + yShift);
         this.blankLargePanel6.drawFlash(x, y + 28 + yShift, this.blankLargePanel6.getWidth(), this.blankLargePanel6.getHeight(), new Color(0, 0, 0, 200));
         yShift += this.blankLargePanel6.getHeight();
      }

      return yShift;
   }

   @Override
   public void update() {
      if (this.panelPage == GUIEnums.GUIPanelPage.LEFT_WORKFORCE) {
         this.workforceTab.select();
      } else {
         this.workforceTab.deselect();
      }

      if (this.panelPage == GUIEnums.GUIPanelPage.LEFT_RESOURCE_MANAGEMENT) {
         this.resourceManagementTab.select();
      } else {
         this.resourceManagementTab.deselect();
      }

      if ((
            this.guiData.getSelectedObject() == null
               || this.guiData.getSelectedObject().getObjectFlags().getBaseType() != MapTilesLoader.TileSet.MARKETPLACE
               || this.object.getMaximumCatjeetProvisioners() <= 0
         )
         && this.panelPage != GUIEnums.GUIPanelPage.LEFT_TRADE) {
         this.tradeTab.deselect();
      } else {
         this.tradeTab.select();
         this.resourceManagementTab.deselect();
         this.workforceTab.deselect();
      }
   }

   public void setPanelPage(GUIEnums.GUIPanelPage p) {
      this.panelPage = p;
   }

   public void togglePanelHidden() {
      this.hidden = !this.hidden;
      this.hidePanel.cycleButton();
   }

   public boolean isPanelHidden() {
      return this.hidden;
   }

   public GUIEnums.GUIPanelPage getPanelPage() {
      return this.panelPage;
   }

   public void setTradeCategoryLeft() {
      if (this.tradeCategory.ordinal() != ResourceModule.ResourceCategory.values().length - 1) {
         this.tradeCategory = ResourceModule.ResourceCategory.values()[this.tradeCategory.ordinal() + 1];
      } else {
         this.tradeCategory = ResourceModule.ResourceCategory.values()[0];
      }
   }

   public void setTradeCategoryRight() {
      if (this.tradeCategory.ordinal() != 0) {
         this.tradeCategory = ResourceModule.ResourceCategory.values()[this.tradeCategory.ordinal() - 1];
      } else {
         this.tradeCategory = ResourceModule.ResourceCategory.values()[ResourceModule.ResourceCategory.values().length - 1];
      }
   }

   public void setWorkforcePageUp() {
      int jobCount = 0;

      MobJobBase.MobJobType[] var5;
      for (MobJobBase.MobJobType t : MobJobBase.MobJobType.values()) {
         if (this.mobJob.getMaximumWorkers(t) != 0) {
            jobCount++;
         }
      }

      if ((this.workforcePage + 1) * this.workforcePageLength < jobCount) {
         this.workforcePage++;
      }
   }

   public void setWorkforcePageDown() {
      if (this.workforcePage > 0) {
         this.workforcePage--;
      }
   }

   public void setWorkforceColorSelectPanel(MobJobBase.MobJobType t, GUIEnums.GUIPanelPage p, boolean isAccent) {
      this.workforceColorSelectCurrentBase.setColor(this.mobJob.getCustomBaseColor(t));
      this.workforceColorSelectCurrentAccent.setColor(this.mobJob.getCustomAccentColor(t));
      this.workforceColorSelectDefaultBase.setColor(t.getJob().getBaseColor());
      this.workforceColorSelectDefaultAccent.setColor(t.getJob().getAccentColor());
      this.workforceColorSelectType = t;
      this.workforceColorSelectAccentMode = isAccent;
   }

   public MobJobBase.MobJobType getWorkforceColorSelectType() {
      return this.workforceColorSelectType;
   }

   public boolean isWorkforceColorSelectAccentMode() {
      return this.workforceColorSelectAccentMode;
   }

   public void toggleResourceManagementFilter() {
      if (this.resourceManagementFilter.ordinal() != GUIControllerBase.ResourceFilter.values().length - 1) {
         this.resourceManagementFilter = GUIControllerBase.ResourceFilter.values()[this.resourceManagementFilter.ordinal() + 1];
      } else {
         this.resourceManagementFilter = GUIControllerBase.ResourceFilter.values()[0];
      }
   }

   public void setResourceManagementCategoryLeft() {
      if (this.leftResourceManagementCategory.ordinal() != ResourceModule.ResourceCategory.values().length - 1) {
         this.leftResourceManagementCategory = ResourceModule.ResourceCategory.values()[this.leftResourceManagementCategory.ordinal() + 1];
      } else {
         this.leftResourceManagementCategory = ResourceModule.ResourceCategory.values()[0];
      }
   }

   public void setResourceManagementCategoryRight() {
      if (this.leftResourceManagementCategory.ordinal() != 0) {
         this.leftResourceManagementCategory = ResourceModule.ResourceCategory.values()[this.leftResourceManagementCategory.ordinal() - 1];
      } else {
         this.leftResourceManagementCategory = ResourceModule.ResourceCategory.values()[ResourceModule.ResourceCategory.values().length - 1];
      }
   }

   public void toggleCourierFilter() {
      if (this.courierFilter.ordinal() != GUIControllerBase.ResourceFilter.values().length - 1) {
         this.courierFilter = GUIControllerBase.ResourceFilter.values()[this.courierFilter.ordinal() + 1];
      } else {
         this.courierFilter = GUIControllerBase.ResourceFilter.values()[0];
      }
   }

   public void setCourierCategoryLeft() {
      if (this.leftCourierCategory.ordinal() != ResourceModule.ResourceCategory.values().length - 1) {
         this.leftCourierCategory = ResourceModule.ResourceCategory.values()[this.leftCourierCategory.ordinal() + 1];
      } else {
         this.leftCourierCategory = ResourceModule.ResourceCategory.values()[0];
      }
   }

   public void setCourierCategoryRight() {
      if (this.leftCourierCategory.ordinal() != 0) {
         this.leftCourierCategory = ResourceModule.ResourceCategory.values()[this.leftCourierCategory.ordinal() - 1];
      } else {
         this.leftCourierCategory = ResourceModule.ResourceCategory.values()[ResourceModule.ResourceCategory.values().length - 1];
      }
   }

   public void nextCourierMap() {
      this.selectedCourierMapSlot++;
      String[] mapList = this.map.getConnectedRegionList();

      while (mapList.length - 1 < this.selectedCourierMapSlot || mapList[this.selectedCourierMapSlot].equals("null")) {
         this.selectedCourierMapSlot++;
         if (mapList.length - 1 < this.selectedCourierMapSlot) {
            this.selectedCourierMapSlot = 0;
         }
      }

      this.selectedCourierMap = mapList[this.selectedCourierMapSlot];
   }

   public void lastCourierMap() {
      this.selectedCourierMapSlot--;
      String[] mapList = this.map.getConnectedRegionList();

      while (this.selectedCourierMapSlot < 0 || mapList[this.selectedCourierMapSlot].equals("null")) {
         this.selectedCourierMapSlot--;
         if (this.selectedCourierMapSlot < 0) {
            this.selectedCourierMapSlot = mapList.length - 1;
         }
      }

      this.selectedCourierMap = mapList[this.selectedCourierMapSlot];
   }

   public String getSelectedCourierMap() {
      return this.selectedCourierMap;
   }

   public int getSelectedCourierMapSlot() {
      return this.selectedCourierMapSlot;
   }

   @Override
   public int getCenterX() {
      return (int)(this.x + this.mask.getWidth() / 2.0F);
   }

   @Override
   public int getCenterY() {
      return (int)(this.y + this.mask.getHeight() / 2.0F);
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
      return (int)(this.x + this.mask.getWidth());
   }

   @Override
   public int getBottomY() {
      return (int)(this.y + this.mask.getHeight());
   }
}
