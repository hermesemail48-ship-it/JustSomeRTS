package rtr.gui.states.playstate;

import java.text.DecimalFormat;
import java.util.EnumMap;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.gui.buttons.GUIClickableArea;
import rtr.gui.buttons.GUIIcon;
import rtr.gui.buttons.GUIRoundButton;
import rtr.gui.states.GUIControllerBase;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.PlayStateGUIPanelBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Utilities;

public class ResourceListPanel extends PlayStateGUIPanelBase {
   private Image panel;
   private Rectangle mask;
   private GUIIcon iconGoldCoins;
   private GUIIcon iconTotalWater;
   private GUIIcon iconTotalDirtyWater;
   private GUIIcon iconTotalFood;
   private GUIIcon iconTotalEnergy;
   private GUIIcon iconTotalFaith;
   private GUIIcon iconTotalResources;
   private GUIRoundButton totalGoldShowButtons;
   private GUIRoundButton totalWaterShowButtons;
   private GUIRoundButton totalDirtyWaterShowButtons;
   private GUIRoundButton totalFoodShowButtons;
   private GUIRoundButton totalEnergyShowButtons;
   private GUIRoundButton totalFaithShowButtons;
   private GUIRoundButton totalGoldFullButtons;
   private GUIRoundButton totalWaterFullButtons;
   private GUIRoundButton totalDirtyWaterFullButtons;
   private GUIRoundButton totalFoodFullButtons;
   private GUIRoundButton totalEnergyFullButtons;
   private GUIRoundButton totalFaithFullButtons;
   private GUIRoundButton totalGoldHideButtons;
   private GUIRoundButton totalWaterHideButtons;
   private GUIRoundButton totalDirtyWaterHideButtons;
   private GUIRoundButton totalFoodHideButtons;
   private GUIRoundButton totalEnergyHideButtons;
   private GUIRoundButton totalFaithHideButtons;
   private GUIClickableArea statRectangle;
   private GUIRoundButton resourceFilterToggle;
   private GUIControllerBase.ResourceFilter resourceFilter = GUIControllerBase.ResourceFilter.ALL;
   private EnumMap<ResourceModule.ResourceType, GUIIcon> resourceIcons = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIRoundButton> resourceShowButtons = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIRoundButton> resourceFullButtons = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIRoundButton> resourceHideButtons = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIClickableArea> resourceIconsArea = new EnumMap<>(ResourceModule.ResourceType.class);

   public ResourceListPanel(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.panel = ImageLoader.getImage("res/GUI/gameState/resourceListPanel.png");
      this.mask = new Rectangle(0.0F, 0.0F, this.panel.getWidth(), this.panel.getHeight());
      this.masks.add(this.mask);
      this.iconGoldCoins = new GUIIcon("iconGoldCoins");
      this.iconGoldCoins
         .setToolTip(Text.getText("playResourceListPanel.tooltip.totalGoldCoinsHeader"), Text.getText("playResourceListPanel.tooltip.totalGoldCoins"));
      this.guiButtons.add(this.iconGoldCoins);
      this.iconTotalWater = new GUIIcon("iconWater");
      this.iconTotalWater.setToolTip(Text.getText("playResourceListPanel.tooltip.totalWaterHeader"), Text.getText("playResourceListPanel.tooltip.totalWater"));
      this.guiButtons.add(this.iconTotalWater);
      this.iconTotalDirtyWater = new GUIIcon("iconDirtyWater");
      this.iconTotalDirtyWater
         .setToolTip(Text.getText("playResourceListPanel.tooltip.totalDirtyWaterHeader"), Text.getText("playResourceListPanel.tooltip.totalDirtyWater"));
      this.guiButtons.add(this.iconTotalDirtyWater);
      this.iconTotalFood = new GUIIcon("iconHunger");
      this.iconTotalFood.setToolTip(Text.getText("playResourceListPanel.tooltip.totalFoodHeader"), Text.getText("playResourceListPanel.tooltip.totalFood"));
      this.guiButtons.add(this.iconTotalFood);
      this.iconTotalEnergy = new GUIIcon("iconEnergy");
      this.iconTotalEnergy
         .setToolTip(Text.getText("playResourceListPanel.tooltip.totalEnergyHeader"), Text.getText("playResourceListPanel.tooltip.totalEnergy"));
      this.guiButtons.add(this.iconTotalEnergy);
      this.iconTotalFaith = new GUIIcon("iconFaith");
      this.iconTotalFaith.setToolTip(Text.getText("playResourceListPanel.tooltip.totalFaithHeader"), Text.getText("playResourceListPanel.tooltip.totalFaith"));
      this.guiButtons.add(this.iconTotalFaith);
      this.iconTotalResources = new GUIIcon("iconCivic");
      this.iconTotalResources
         .setToolTip(Text.getText("playResourceListPanel.tooltip.totalResourcesHeader"), Text.getText("playResourceListPanel.tooltip.totalResources"));
      this.guiButtons.add(this.iconTotalResources);
      this.totalGoldShowButtons = new GUIRoundButton("totalGoldShowButtons", "buttonMinus", 0);
      this.totalGoldShowButtons
         .setToolTip(
            Text.getText("playResourceListPanel.tooltip.showResourceOnBarGoldHeader"), Text.getText("playResourceListPanel.tooltip.showResourceOnBarGold")
         );
      this.guiButtons.add(this.totalGoldShowButtons);
      this.totalGoldFullButtons = new GUIRoundButton("totalGoldFullButtons", "buttonDismantle", 0);
      this.totalGoldFullButtons.setToolTip(Text.getText("playResourceListPanel.tooltip.fullResourceOnBarHeader"));
      this.guiButtons.add(this.totalGoldFullButtons);
      this.totalGoldHideButtons = new GUIRoundButton("totalGoldHideButtons", "buttonCircle", 0);
      this.totalGoldHideButtons
         .setToolTip(
            Text.getText("playResourceListPanel.tooltip.hideResourceOnBarGoldHeader"), Text.getText("playResourceListPanel.tooltip.hideResourceOnBarGold")
         );
      this.guiButtons.add(this.totalGoldHideButtons);
      this.totalWaterShowButtons = new GUIRoundButton("totalWaterShowButtons", "buttonMinus", 0);
      this.totalWaterShowButtons
         .setToolTip(
            Text.getText("playResourceListPanel.tooltip.showResourceOnBarWaterHeader"), Text.getText("playResourceListPanel.tooltip.showResourceOnBarWater")
         );
      this.guiButtons.add(this.totalWaterShowButtons);
      this.totalWaterFullButtons = new GUIRoundButton("totalWaterFullButtons", "buttonDismantle", 0);
      this.totalWaterFullButtons.setToolTip(Text.getText("playResourceListPanel.tooltip.fullResourceOnBarHeader"));
      this.guiButtons.add(this.totalWaterFullButtons);
      this.totalWaterHideButtons = new GUIRoundButton("totalWaterHideButtons", "buttonCircle", 0);
      this.totalWaterHideButtons
         .setToolTip(
            Text.getText("playResourceListPanel.tooltip.hideResourceOnBarWaterHeader"), Text.getText("playResourceListPanel.tooltip.hideResourceOnBarWater")
         );
      this.guiButtons.add(this.totalWaterHideButtons);
      this.totalDirtyWaterShowButtons = new GUIRoundButton("totalDirtyWaterShowButtons", "buttonMinus", 0);
      this.totalDirtyWaterShowButtons
         .setToolTip(
            Text.getText("playResourceListPanel.tooltip.showResourceOnBarDirtyWaterHeader"),
            Text.getText("playResourceListPanel.tooltip.showResourceOnBarDirtyWater")
         );
      this.guiButtons.add(this.totalDirtyWaterShowButtons);
      this.totalDirtyWaterFullButtons = new GUIRoundButton("totalDirtyWaterFullButtons", "buttonDismantle", 0);
      this.totalDirtyWaterFullButtons.setToolTip(Text.getText("playResourceListPanel.tooltip.fullResourceOnBarHeader"));
      this.guiButtons.add(this.totalDirtyWaterFullButtons);
      this.totalDirtyWaterHideButtons = new GUIRoundButton("totalDirtyWaterHideButtons", "buttonCircle", 0);
      this.totalDirtyWaterHideButtons
         .setToolTip(
            Text.getText("playResourceListPanel.tooltip.hideResourceOnBarDirtyWaterHeader"),
            Text.getText("playResourceListPanel.tooltip.hideResourceOnBarDirtyWater")
         );
      this.guiButtons.add(this.totalDirtyWaterHideButtons);
      this.totalFoodShowButtons = new GUIRoundButton("totalFoodShowButtons", "buttonMinus", 0);
      this.totalFoodShowButtons
         .setToolTip(
            Text.getText("playResourceListPanel.tooltip.showResourceOnBarFoodHeader"), Text.getText("playResourceListPanel.tooltip.showResourceOnBarFood")
         );
      this.guiButtons.add(this.totalFoodShowButtons);
      this.totalFoodFullButtons = new GUIRoundButton("totalFoodFullButtons", "buttonDismantle", 0);
      this.totalFoodFullButtons.setToolTip(Text.getText("playResourceListPanel.tooltip.fullResourceOnBarHeader"));
      this.guiButtons.add(this.totalFoodFullButtons);
      this.totalFoodHideButtons = new GUIRoundButton("totalFoodHideButtons", "buttonCircle", 0);
      this.totalFoodHideButtons
         .setToolTip(
            Text.getText("playResourceListPanel.tooltip.hideResourceOnBarFoodHeader"), Text.getText("playResourceListPanel.tooltip.hideResourceOnBarFood")
         );
      this.guiButtons.add(this.totalFoodHideButtons);
      this.totalEnergyShowButtons = new GUIRoundButton("totalEnergyShowButtons", "buttonMinus", 0);
      this.totalEnergyShowButtons
         .setToolTip(
            Text.getText("playResourceListPanel.tooltip.showResourceOnBarEnergyHeader"), Text.getText("playResourceListPanel.tooltip.showResourceOnBarEnergy")
         );
      this.guiButtons.add(this.totalEnergyShowButtons);
      this.totalEnergyFullButtons = new GUIRoundButton("totalEnergyFullButtons", "buttonDismantle", 0);
      this.totalEnergyFullButtons.setToolTip(Text.getText("playResourceListPanel.tooltip.fullResourceOnBarHeader"));
      this.guiButtons.add(this.totalEnergyFullButtons);
      this.totalEnergyHideButtons = new GUIRoundButton("totalEnergyHideButtons", "buttonCircle", 0);
      this.totalEnergyHideButtons
         .setToolTip(
            Text.getText("playResourceListPanel.tooltip.hideResourceOnBarEnergyHeader"), Text.getText("playResourceListPanel.tooltip.hideResourceOnBarEnergy")
         );
      this.guiButtons.add(this.totalEnergyHideButtons);
      this.totalFaithShowButtons = new GUIRoundButton("totalFaithShowButtons", "buttonMinus", 0);
      this.totalFaithShowButtons
         .setToolTip(
            Text.getText("playResourceListPanel.tooltip.showResourceOnBarFaithHeader"), Text.getText("playResourceListPanel.tooltip.showResourceOnBarFaith")
         );
      this.guiButtons.add(this.totalFaithShowButtons);
      this.totalFaithFullButtons = new GUIRoundButton("totalFaithFullButtons", "buttonDismantle", 0);
      this.totalFaithFullButtons.setToolTip(Text.getText("playResourceListPanel.tooltip.fullResourceOnBarHeader"));
      this.guiButtons.add(this.totalFaithFullButtons);
      this.totalFaithHideButtons = new GUIRoundButton("totalFaithHideButtons", "buttonCircle", 0);
      this.totalFaithHideButtons
         .setToolTip(
            Text.getText("playResourceListPanel.tooltip.hideResourceOnBarFaithHeader"), Text.getText("playResourceListPanel.tooltip.hideResourceOnBarFaith")
         );
      this.guiButtons.add(this.totalFaithHideButtons);
      this.statRectangle = new GUIClickableArea("statRectangle", 52, 12);
      this.guiButtons.add(this.statRectangle);
      this.resourceFilterToggle = new GUIRoundButton("resourceFilterToggle", "buttonCircle", 0);
      this.resourceFilterToggle
         .setToolTip(Text.getText("playResourceListPanel.button.tooltip.sortingHeader"), Text.getText("playResourceListPanel.button.tooltip.sorting"));
      this.guiButtons.add(this.resourceFilterToggle);

      ResourceModule.ResourceType[] var9;
      for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
         if (!type.getTemplate().canHideFromResourceBar()) {
            GUIIcon newIcon = new GUIIcon(type);
            newIcon.setToolTip(type.getTemplate().getNamePlural(), type.getTemplate().getDescription());
            this.resourceIcons.put(type, newIcon);
            this.guiButtons.add(newIcon);
            GUIRoundButton toggleButton = new GUIRoundButton(type.toString() + "Show", "buttonMinus", 0);
            Text.setVariableText("resourceName", type.getTemplate().getNamePlural());
            toggleButton.setToolTip(
               Text.getText("playResourceListPanel.tooltip.showResourceOnBarHeader"), Text.getText("playResourceListPanel.tooltip.showResourceOnBar")
            );
            this.resourceShowButtons.put(type, toggleButton);
            this.guiButtons.add(toggleButton);
            toggleButton = new GUIRoundButton(type.toString() + "Full", "buttonDismantle", 0);
            toggleButton.setToolTip(Text.getText("playResourceListPanel.tooltip.fullResourceOnBarHeader"));
            this.resourceFullButtons.put(type, toggleButton);
            this.guiButtons.add(toggleButton);
            toggleButton = new GUIRoundButton(type.toString() + "Hide", "buttonCircle", 0);
            Text.setVariableText("resourceName", type.getTemplate().getNamePlural());
            toggleButton.setToolTip(
               Text.getText("playResourceListPanel.tooltip.hideResourceOnBarHeader"), Text.getText("playResourceListPanel.tooltip.hideResourceOnBar")
            );
            this.resourceHideButtons.put(type, toggleButton);
            this.guiButtons.add(toggleButton);
            GUIClickableArea newIconArea = new GUIClickableArea(this.resourceIconsArea + type.toString(), 32, 16);
            Text.setVariableText("resourceNamePlural", type.getTemplate().getNamePlural());
            Text.setVariableText("resourceName", type.getTemplate().getName());
            newIconArea.setToolTip(
               Text.getText("playResourceListPanel.tooltip.resourceCountAndStorageHeader"),
               Text.getText("playResourceListPanel.tooltip.resourceCountAndStorage")
            );
            this.resourceIconsArea.put(type, newIconArea);
            this.guiButtons.add(newIconArea);
         }
      }
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
      this.font.drawString(this.x + 135, this.y + 3, Text.getText("playResourceListPanel.header"), Text.FontType.HEADER, 2, true);
      this.sidebarTotals(
         this.iconGoldCoins,
         ResourceModule.ResourceBarType.GOLD,
         this.totalGoldShowButtons,
         this.totalGoldFullButtons,
         this.totalGoldHideButtons,
         this.resource.getTotalAvailableGoldCoins(),
         this.resource.getMaximumAvailableGoldCoins(),
         145,
         33,
         debug
      );
      this.sidebarTotals(
         this.iconTotalWater,
         ResourceModule.ResourceBarType.WATER,
         this.totalWaterShowButtons,
         this.totalWaterFullButtons,
         this.totalWaterHideButtons,
         this.resource.getTotalAvailableWater(),
         this.resource.getMaximumAvailableWater(),
         145,
         50,
         debug
      );
      this.sidebarTotals(
         this.iconTotalDirtyWater,
         ResourceModule.ResourceBarType.DIRTY_WATER,
         this.totalDirtyWaterShowButtons,
         this.totalDirtyWaterFullButtons,
         this.totalDirtyWaterHideButtons,
         this.resource.getTotalAvailableDirtyWater(),
         this.resource.getMaximumAvailableDirtyWater(),
         275,
         33,
         debug
      );
      this.sidebarTotals(
         this.iconTotalFood,
         ResourceModule.ResourceBarType.FOOD,
         this.totalFoodShowButtons,
         this.totalFoodFullButtons,
         this.totalFoodHideButtons,
         this.resource.getTotalAvailableFood(),
         this.resource.getMaximumAvailableFood(),
         275,
         50,
         debug
      );
      this.sidebarTotals(
         this.iconTotalEnergy,
         ResourceModule.ResourceBarType.ENERGY,
         this.totalEnergyShowButtons,
         this.totalEnergyFullButtons,
         this.totalEnergyHideButtons,
         this.resource.getTotalAvailableEnergy(),
         this.resource.getMaximumAvailableEnergy(),
         405,
         33,
         debug
      );
      this.sidebarTotals(
         this.iconTotalFaith,
         ResourceModule.ResourceBarType.FAITH,
         this.totalFaithShowButtons,
         this.totalFaithFullButtons,
         this.totalFaithHideButtons,
         this.mob.getTotalAvailableFaith(),
         this.mob.getTotalMaximumFaith(),
         405,
         50,
         debug
      );
      this.iconTotalResources.render(this.g, this.x + 25, this.y + 50, debug);
      int total = 0;

      ResourceModule.ResourceType[] alignX;
      for (ResourceModule.ResourceType t : alignX = ResourceModule.ResourceType.values()) {
         if (!t.getTemplate().canHideFromResourceBar()) {
            switch (this.resourceFilter) {
               case ALL:
                  total += this.resource.getResourceTotal(t, true);
                  break;
               case GROUND:
                  total += this.resource.getResourceGround(t, true);
                  break;
               case STORAGE:
                  total += this.resource.getResourceStores(t);
                  break;
               case EQUIPPED:
                  total += this.resource.getResourceEquipped(t);
                  break;
               case CARRIED:
                  total += this.resource.getResourceCarried(t);
                  break;
               case RATE:
                  total += this.resource.getResourceRate(t);
            }
         }
      }

      switch (this.resourceFilter) {
         case ALL:
            this.font.drawString(this.x + 45, this.y + 50, "$GRE0" + total, Text.FontType.BODY, 1);
            break;
         case GROUND:
            this.font.drawString(this.x + 45, this.y + 50, "$ORA0" + total, Text.FontType.BODY, 1);
            break;
         case STORAGE:
            this.font.drawString(this.x + 45, this.y + 50, "$MAG0" + total, Text.FontType.BODY, 1);
            break;
         case EQUIPPED:
            this.font.drawString(this.x + 45, this.y + 50, "$YEL0" + total, Text.FontType.BODY, 1);
            break;
         case CARRIED:
            this.font.drawString(this.x + 45, this.y + 50, "$PUR0" + total, Text.FontType.BODY, 1);
            break;
         case RATE:
            if (total > 0) {
               this.font.drawString(this.x + 45, this.y + 50, "$GRE0+" + total, Text.FontType.BODY, 1);
            } else if (total == 0) {
               this.font.drawString(this.x + 45, this.y + 50, "$WHI0" + total, Text.FontType.BODY, 1);
            } else {
               this.font.drawString(this.x + 45, this.y + 50, "$RED0" + total, Text.FontType.BODY, 1);
            }
      }

      EnumMap<ResourceModule.ResourceCategory, Integer> categoryLengths = new EnumMap<>(ResourceModule.ResourceCategory.class);

      ResourceModule.ResourceCategory[] alignY;
      for (ResourceModule.ResourceCategory c : alignY = ResourceModule.ResourceCategory.values()) {
         int thisCatLength = 20;

         ResourceModule.ResourceType[] tx;
         for (ResourceModule.ResourceType txx : tx = ResourceModule.ResourceType.values()) {
            if (txx.getTemplate().getResourceCategory() == c) {
               thisCatLength += 20;
            }
         }

         categoryLengths.put(c, thisCatLength);
      }

      this.resourceFilterToggle.render(this.g, this.mouse, this.x + 23, this.y + 31, debug);
      switch (this.resourceFilter) {
         case ALL:
            this.font.drawString(this.x + 43, this.y + 34, Text.getText("playResourceListPanel.sort.total"), Text.FontType.BODY, 1);
            break;
         case GROUND:
            this.font.drawString(this.x + 43, this.y + 34, Text.getText("playResourceListPanel.sort.ground"), Text.FontType.BODY, 1);
            break;
         case STORAGE:
            this.font.drawString(this.x + 43, this.y + 34, Text.getText("playResourceListPanel.sort.stored"), Text.FontType.BODY, 1);
            break;
         case EQUIPPED:
            this.font.drawString(this.x + 43, this.y + 34, Text.getText("playResourceListPanel.sort.equip"), Text.FontType.BODY, 1);
            break;
         case CARRIED:
            this.font.drawString(this.x + 43, this.y + 34, Text.getText("playResourceListPanel.sort.carry"), Text.FontType.BODY, 1);
            break;
         case RATE:
            this.font.drawString(this.x + 43, this.y + 34, Text.getText("playResourceListPanel.sort.rate"), Text.FontType.BODY, 1);
      }

      int thisColumnLength = categoryLengths.get(ResourceModule.ResourceCategory.values()[0]);
      int barLength = 0;

      ResourceModule.ResourceType[] var31;
      for (ResourceModule.ResourceType tx : ResourceModule.ResourceType.values()) {
         if (!tx.getTemplate().canHideFromResourceBar() && this.resource.isOnResourceBarMain(tx)) {
            barLength++;
         }
      }

      int alignXx = 0;
      int alignYx = 0;

      ResourceModule.ResourceCategory[] var34;
      for (ResourceModule.ResourceCategory c : ResourceModule.ResourceCategory.values()) {
         this.font.drawString(this.x + 28 + alignXx, this.y + 81 + alignYx, "$BLU0" + c.getText(), Text.FontType.BODY, 1);
         int var28 = alignYx + 10;

         ResourceModule.ResourceType[] var15;
         for (ResourceModule.ResourceType txxx : ResourceModule.ResourceType.values()) {
            if (!txxx.getTemplate().canHideFromResourceBar() && txxx.getTemplate().getResourceCategory() == c) {
               if (this.resource.isOnResourceBarMain(txxx)) {
                  this.resourceHideButtons.get(txxx).render(this.g, this.mouse, this.x + 24 + alignXx, this.y + 88 + var28, debug);
               } else if (barLength >= 32) {
                  this.resourceFullButtons.get(txxx).render(this.g, this.mouse, this.x + 24 + alignXx, this.y + 88 + var28, debug);
               } else {
                  this.resourceShowButtons.get(txxx).render(this.g, this.mouse, this.x + 24 + alignXx, this.y + 88 + var28, debug);
               }

               this.resourceIcons.get(txxx).render(this.g, this.x + 42 + alignXx, this.y + 88 + var28, debug);
               this.resourceIconsArea.get(txxx).render(this.g, this.mouse, this.x + 60 + alignXx, this.y + 88 + var28, false, debug);
               int getLength = 0;
               String colorCode = "$RED1";
               switch (this.resourceFilter) {
                  case ALL:
                     getLength = Text.getWidth(this.resource.getResourceTotal(txxx, true) + "/", Text.FontType.BODY, 1);
                     if (this.resource.getResourceTotal(txxx, true) > 0) {
                        colorCode = "$GRE0";
                     }

                     this.font
                        .drawString(
                           this.x + 61 + alignXx, this.y + 90 + var28, colorCode + this.resource.getResourceTotal(txxx, true) + "$GRE0/", Text.FontType.BODY, 1
                        );
                     this.font
                        .drawString(
                           this.x + 60 + alignXx + getLength, this.y + 94 + var28, "$GRE2" + this.resource.getResourceStoresSpace(txxx), Text.FontType.BODY, 0
                        );
                     break;
                  case GROUND:
                     getLength = Text.getWidth(this.resource.getResourceGround(txxx, true) + "/", Text.FontType.BODY, 1);
                     if (this.resource.getResourceGround(txxx, true) > 0) {
                        colorCode = "$ORA0";
                     }

                     this.font
                        .drawString(
                           this.x + 61 + alignXx,
                           this.y + 90 + var28,
                           colorCode + this.resource.getResourceGround(txxx, true) + "$ORA0/",
                           Text.FontType.BODY,
                           1
                        );
                     this.font
                        .drawString(
                           this.x + 60 + alignXx + getLength, this.y + 94 + var28, "$ORA2" + this.resource.getResourceStoresSpace(txxx), Text.FontType.BODY, 0
                        );
                     break;
                  case STORAGE:
                     getLength = Text.getWidth(this.resource.getResourceStores(txxx) + "/", Text.FontType.BODY, 1);
                     if (this.resource.getResourceStores(txxx) > 0) {
                        colorCode = "$MAG0";
                     }

                     this.font
                        .drawString(
                           this.x + 61 + alignXx, this.y + 90 + var28, colorCode + this.resource.getResourceStores(txxx) + "$MAG0/", Text.FontType.BODY, 1
                        );
                     this.font
                        .drawString(
                           this.x + 60 + alignXx + getLength, this.y + 94 + var28, "$MAG2" + this.resource.getResourceStoresSpace(txxx), Text.FontType.BODY, 0
                        );
                     break;
                  case EQUIPPED:
                     getLength = Text.getWidth(this.resource.getResourceEquipped(txxx) + "/", Text.FontType.BODY, 1);
                     if (this.resource.getResourceEquipped(txxx) > 0) {
                        colorCode = "$YEL0";
                     }

                     this.font
                        .drawString(
                           this.x + 61 + alignXx, this.y + 90 + var28, colorCode + this.resource.getResourceEquipped(txxx) + "$YEL0/", Text.FontType.BODY, 1
                        );
                     this.font
                        .drawString(
                           this.x + 60 + alignXx + getLength, this.y + 94 + var28, "$YEL2" + this.resource.getResourceStoresSpace(txxx), Text.FontType.BODY, 0
                        );
                     break;
                  case CARRIED:
                     getLength = Text.getWidth(this.resource.getResourceCarried(txxx) + "/", Text.FontType.BODY, 1);
                     if (this.resource.getResourceCarried(txxx) > 0) {
                        colorCode = "$CYA0";
                     }

                     this.font
                        .drawString(
                           this.x + 61 + alignXx, this.y + 90 + var28, colorCode + this.resource.getResourceCarried(txxx) + "$CYA0/", Text.FontType.BODY, 1
                        );
                     this.font
                        .drawString(
                           this.x + 60 + alignXx + getLength, this.y + 94 + var28, "$CYA2" + this.resource.getResourceStoresSpace(txxx), Text.FontType.BODY, 0
                        );
                     break;
                  case RATE:
                     getLength = Text.getWidth(String.valueOf(this.resource.getResourceTotal(txxx, true)), Text.FontType.BODY, 0);
                     if (this.resource.getResourceRate(txxx) > 0) {
                        colorCode = "$BLA2\\$GRE0+";
                     } else if (this.resource.getResourceRate(txxx) < 0) {
                        colorCode = "$BLA2\\$RED0";
                     }

                     if (this.resource.getResourceRate(txxx) == 0) {
                        this.font
                           .drawString(this.x + 61 + alignXx, this.y + 90 + var28, "" + this.resource.getResourceTotal(txxx, true), Text.FontType.BODY, 0);
                        this.font.drawString(this.x + 61 + alignXx + getLength - 1, this.y + 86 + var28, "$BLA2\\~", Text.FontType.BODY, 0);
                     } else {
                        this.font
                           .drawString(this.x + 61 + alignXx, this.y + 90 + var28, "" + this.resource.getResourceTotal(txxx, true), Text.FontType.BODY, 0);
                        this.font
                           .drawString(
                              this.x + 61 + alignXx + getLength - 1,
                              this.y + 86 + var28,
                              colorCode + this.resource.getResourceRate(txxx),
                              Text.FontType.BODY,
                              1
                           );
                     }
               }

               var28 += 18;
            }
         }

         if (c.ordinal() < ResourceModule.ResourceCategory.values().length - 1
            && thisColumnLength + categoryLengths.get(ResourceModule.ResourceCategory.values()[c.ordinal() + 1]) >= 330) {
            alignXx += 104;
            alignYx = 0;
            thisColumnLength = categoryLengths.get(ResourceModule.ResourceCategory.values()[c.ordinal() + 1]);
         } else {
            alignYx = var28 + 10;
            thisColumnLength += categoryLengths.get(c);
         }
      }
   }

   private void sidebarTotals(
      GUIIcon icon,
      ResourceModule.ResourceBarType type,
      GUIRoundButton buttonShow,
      GUIRoundButton buttonFull,
      GUIRoundButton buttonHide,
      int lowNum,
      int highNum,
      int sidebarAlignX,
      int sidebarAlignY,
      boolean debug
   ) {
      int barLength = 0;

      ResourceModule.ResourceBarType[] low;
      for (ResourceModule.ResourceBarType t : low = ResourceModule.ResourceBarType.values()) {
         if (this.resource.isOnResourceBarRight(t)) {
            barLength++;
         }
      }

      if (this.resource.isOnResourceBarRight(type)) {
         buttonHide.render(this.g, this.mouse, this.x + sidebarAlignX, this.y + sidebarAlignY - 2, debug);
      } else if (barLength >= 4) {
         buttonFull.render(this.g, this.mouse, this.x + sidebarAlignX, this.y + sidebarAlignY - 2, debug);
      } else {
         buttonShow.render(this.g, this.mouse, this.x + sidebarAlignX, this.y + sidebarAlignY - 2, debug);
      }

      icon.render(this.g, this.x + sidebarAlignX + 20, this.y + sidebarAlignY, debug);
      int barMaxWidth = 60;
      int barWidth = (int)Utilities.getPercentage(lowNum, highNum, barMaxWidth);
      int barWidthOverLimit = 0;
      if (barWidth > barMaxWidth) {
         barWidthOverLimit = barWidth - barMaxWidth;
         if (barWidthOverLimit > barMaxWidth) {
            barWidthOverLimit = barMaxWidth;
         }

         barWidth = barMaxWidth;
      }

      this.g.setColor(new Color(26, 26, 26));
      this.g.fillRect(this.x + sidebarAlignX + 39, this.y + sidebarAlignY + 2, barMaxWidth + 2, 12.0F);
      if (barWidth > barMaxWidth * 0.75) {
         this.g.setColor(new Color(0, 100, 0));
      } else if (barWidth > barMaxWidth * 0.5) {
         this.g.setColor(new Color(100, 100, 0));
      } else if (barWidth > barMaxWidth * 0.25) {
         this.g.setColor(new Color(100, 50, 0));
      } else {
         this.g.setColor(new Color(100, 0, 0));
      }

      this.g.fillRect(this.x + sidebarAlignX + 40, this.y + sidebarAlignY + 3, barWidth, 10.0F);
      if (barWidthOverLimit > 0) {
         this.g.setColor(new Color(0, 200, 0));
         this.g.fillRect(this.x + sidebarAlignX + 40, this.y + sidebarAlignY + 3, barWidthOverLimit, 10.0F);
      }

      this.statRectangle.render(this.g, this.x + sidebarAlignX + 42, this.y + sidebarAlignY - 2, debug);
      if (!debug && !this.statRectangle.intersects(this.mouse)) {
         this.font.drawString(this.x + sidebarAlignX + 43, this.y + sidebarAlignY + 1, "$YEL1" + Integer.toString(lowNum), Text.FontType.BODY, 0);
      } else {
         String lowx = Integer.toString(lowNum);
         if (lowNum >= 1000) {
            DecimalFormat lowD = new DecimalFormat("#.#");
            lowx = lowD.format(lowNum / 1000.0) + "k";
         }

         String high = Integer.toString(highNum);
         if (highNum >= 1000) {
            DecimalFormat highD = new DecimalFormat("#.#");
            high = highD.format(highNum / 1000.0) + "k";
         }

         this.font.drawString(this.x + sidebarAlignX + 43, this.y + sidebarAlignY + 1, "$YEL1" + lowx + "/" + high, Text.FontType.BODY, 0);
      }

      sidebarAlignY += 13;
   }

   public void toggleResourceFilter() {
      if (this.resourceFilter.ordinal() != GUIControllerBase.ResourceFilter.values().length - 1) {
         this.resourceFilter = GUIControllerBase.ResourceFilter.values()[this.resourceFilter.ordinal() + 1];
      } else {
         this.resourceFilter = GUIControllerBase.ResourceFilter.values()[0];
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
