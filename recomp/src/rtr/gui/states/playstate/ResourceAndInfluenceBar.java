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
import rtr.gui.GUIEnums;
import rtr.gui.buttons.GUIClickableArea;
import rtr.gui.buttons.GUIIcon;
import rtr.gui.buttons.GUIRoundButton;
import rtr.gui.states.GUIControllerBase;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.PlayStateGUIPanelBase;
import rtr.influence.SpellBase;
import rtr.particles.ParticleModule;
import rtr.resources.ResourceModule;
import rtr.system.ScaleControl;
import rtr.utilities.Utilities;

public class ResourceAndInfluenceBar extends PlayStateGUIPanelBase {
   private Image panel;
   private Rectangle mask;
   private int influnceBarOld;
   private int influnceBarLeft;
   private GUIRoundButton resourceFilterToggle;
   private GUIControllerBase.ResourceFilter resourceFilter = GUIControllerBase.ResourceFilter.ALL;
   private EnumMap<ResourceModule.ResourceType, GUIIcon> resourceIcons = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIClickableArea> resourceIconsArea = new EnumMap<>(ResourceModule.ResourceType.class);
   private GUIIcon iconGoldCoins;
   private GUIIcon iconTotalWater;
   private GUIIcon iconTotalDirtyWater;
   private GUIIcon iconTotalFood;
   private GUIIcon iconTotalEnergy;
   private GUIIcon iconTotalFaith;
   private GUIClickableArea statRectangle;
   private GUIClickableArea corruptionThreatBarArea;
   private GUIClickableArea influenceBarArea;
   protected boolean flashing = false;
   protected int flashAlpha;
   protected boolean flashUp;

   public ResourceAndInfluenceBar(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.panel = ImageLoader.getImage("res/GUI/gameState/resourceBar.png");
      this.mask = new Rectangle(0.0F, 0.0F, this.panel.getWidth(), this.panel.getHeight());
      this.masks.add(this.mask);
      this.resourceFilterToggle = new GUIRoundButton("resourceFilterToggle", "buttonCircle", 0);
      this.resourceFilterToggle
         .setToolTip(
            Text.getText("playResourceAndInfluenceBar.button.tooltip.sortingHeader"), Text.getText("playResourceAndInfluenceBar.button.tooltip.sorting")
         );
      this.guiButtons.add(this.resourceFilterToggle);

      ResourceModule.ResourceType[] var9;
      for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
         GUIIcon newIcon = new GUIIcon(type);
         newIcon.setToolTip(type.getTemplate().getNamePlural(), type.getTemplate().getDescription());
         this.resourceIcons.put(type, newIcon);
         this.guiButtons.add(newIcon);
         GUIClickableArea newIconArea = new GUIClickableArea(this.resourceIconsArea + type.toString(), 32, 16);
         Text.setVariableText("resourceNamePlural", type.getTemplate().getNamePlural());
         Text.setVariableText("resourceName", type.getTemplate().getName());
         newIconArea.setToolTip(
            Text.getText("playResourceAndInfluenceBar.tooltip.resourceCountAndStorageHeader"),
            Text.getText("playResourceAndInfluenceBar.tooltip.resourceCountAndStorage")
         );
         this.resourceIconsArea.put(type, newIconArea);
         this.guiButtons.add(newIconArea);
      }

      this.iconGoldCoins = new GUIIcon("iconGoldCoins");
      this.iconGoldCoins
         .setToolTip(
            Text.getText("playResourceAndInfluenceBar.tooltip.totalGoldCoinsHeader"), Text.getText("playResourceAndInfluenceBar.tooltip.totalGoldCoins")
         );
      this.guiButtons.add(this.iconGoldCoins);
      this.iconTotalWater = new GUIIcon("iconWater");
      this.iconTotalWater
         .setToolTip(Text.getText("playResourceAndInfluenceBar.tooltip.totalWaterHeader"), Text.getText("playResourceAndInfluenceBar.tooltip.totalWater"));
      this.guiButtons.add(this.iconTotalWater);
      this.iconTotalDirtyWater = new GUIIcon("iconDirtyWater");
      this.iconTotalDirtyWater
         .setToolTip(
            Text.getText("playResourceAndInfluenceBar.tooltip.totalDirtyWaterHeader"), Text.getText("playResourceAndInfluenceBar.tooltip.totalDirtyWater")
         );
      this.guiButtons.add(this.iconTotalDirtyWater);
      this.iconTotalFood = new GUIIcon("iconHunger");
      this.iconTotalFood
         .setToolTip(Text.getText("playResourceAndInfluenceBar.tooltip.totalFoodHeader"), Text.getText("playResourceAndInfluenceBar.tooltip.totalFood"));
      this.guiButtons.add(this.iconTotalFood);
      this.iconTotalEnergy = new GUIIcon("iconEnergy");
      this.iconTotalEnergy
         .setToolTip(Text.getText("playResourceAndInfluenceBar.tooltip.totalEnergyHeader"), Text.getText("playResourceAndInfluenceBar.tooltip.totalEnergy"));
      this.guiButtons.add(this.iconTotalEnergy);
      this.iconTotalFaith = new GUIIcon("iconFaith");
      this.iconTotalFaith
         .setToolTip(Text.getText("playResourceAndInfluenceBar.tooltip.totalFaithHeader"), Text.getText("playResourceAndInfluenceBar.tooltip.totalFaith"));
      this.guiButtons.add(this.iconTotalFaith);
      this.statRectangle = new GUIClickableArea("statRectangle", 52, 12);
      this.guiButtons.add(this.statRectangle);
      this.corruptionThreatBarArea = new GUIClickableArea("corruptionThreatBarArea", 930, 12);
      this.corruptionThreatBarArea
         .setToolTip(
            Text.getText("playResourceAndInfluenceBar.tooltip.corruptionThreatHeader"), Text.getText("playResourceAndInfluenceBar.tooltip.corruptionThreat")
         );
      this.guiButtons.add(this.corruptionThreatBarArea);
      this.influenceBarArea = new GUIClickableArea("influenceBarArea", 930, 12);
      this.influenceBarArea
         .setToolTip(
            Text.getText("playResourceAndInfluenceBar.tooltip.totalInfluenceHeader"), Text.getText("playResourceAndInfluenceBar.tooltip.totalInfluence")
         );
      this.guiButtons.add(this.influenceBarArea);
   }

   public void render(boolean debug) throws SlickException {
      this.x = ScaleControl.getInterfaceCenterX() - this.panel.getWidth() / 2;
      this.y = 6;
      this.mask.setX(this.x);
      this.mask.setY(this.y);
      this.renderCorruptionThreatBar(debug);
      this.renderInflunceBar(debug);
      this.panel.draw(this.x, this.y);
      this.corruptionThreatBarArea.render(this.g, this.mouse, this.x + 13, this.y + 47, false, debug);
      this.influenceBarArea.render(this.g, this.mouse, this.x + 13, this.y + 61, false, debug);
      String influenceAmountString = null;
      if (this.influence.getInfluenceMaintainingAmount() > 0) {
         influenceAmountString = this.influence.getInfluenceAmount()
            + "/"
            + (this.influence.getInfluenceAmountMax() - this.influence.getInfluenceMaintainingAmount())
            + "(-"
            + this.influence.getInfluenceMaintainingAmount()
            + ")";
      } else {
         influenceAmountString = this.influence.getInfluenceAmount() + "/" + this.influence.getInfluenceAmountMax();
      }

      this.font.drawString(this.x + this.panel.getWidth() / 2 - 1, this.y + 59, "$BLA0" + influenceAmountString, Text.FontType.BODY, 2, true);
      this.font.drawString(this.x + this.panel.getWidth() / 2 + 1, this.y + 59, "$BLA0" + influenceAmountString, Text.FontType.BODY, 2, true);
      this.font.drawString(this.x + this.panel.getWidth() / 2, this.y + 59, influenceAmountString, Text.FontType.BODY, 2, true);
      int corruptionBarLength = (int)Math.ceil(this.corruption.getCorruptionThreatenedPercentBoost() / 500.0 * 930.0);
      if (corruptionBarLength > 930) {
         int var5 = 930;
      }

      this.renderResources(debug);
   }

   private void renderCorruptionThreatBar(boolean debug) {
      this.g.setColor(new Color(0, 0, 0, 150));
      this.g.fillRect(this.x + 15, this.y + 50, 930.0F, 12.0F);
      int corruptionBarLength = (int)Math.ceil(this.corruption.getCorruptionThreatenedPercentBoost() / 500.0 * 930.0);
      int blazingAmount = 0;
      if (corruptionBarLength > 930) {
         blazingAmount = (corruptionBarLength - 930) / 8;
         int flashSpeed = (corruptionBarLength - 930) / 20;
         corruptionBarLength = 930;
         if (flashSpeed > 10) {
            if (this.flashUp) {
               if (this.flashAlpha < 250) {
                  this.flashAlpha += flashSpeed;
               } else {
                  this.flashUp = false;
               }
            } else if (this.flashAlpha > 0) {
               this.flashAlpha -= flashSpeed;
            } else {
               this.flashUp = true;
            }

            this.g.setColor(new Color(255, this.flashAlpha, this.flashAlpha, 255));
         } else {
            this.g.setColor(Color.red);
         }
      } else {
         this.g.setColor(Color.red);
      }

      this.g.fillRect(this.x + 15, this.y + 50, corruptionBarLength, 12.0F);
      int particleX = Utilities.randomInt(930);
      int particleY = this.y + Utilities.randomInt(10);
      if (particleX <= corruptionBarLength + 5) {
         this.particle
            .newParticleGroup(this.x + 15 + particleX, this.y + 45 + particleY, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_RED, true);
      }

      for (int i = 0; i < 100 + blazingAmount; i++) {
         particleX = Utilities.randomInt(930);
         particleY = this.y + Utilities.randomInt(10);
         if (particleX <= corruptionBarLength + 5) {
            this.particle
               .newParticleGroup(this.x + 15 + particleX, this.y + 45 + particleY, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_RED, true);
         }
      }

      for (int ix = 0; ix < 10; ix++) {
         particleX = Utilities.randomInt(930);
         particleY = this.y + Utilities.randomInt(10);
         if (particleX <= corruptionBarLength + 5) {
            this.particle
               .newParticle(
                  this.x + 15 + particleX, this.y + 45 + particleY, ParticleModule.ParticleType.SPARKLE_SPECK, ParticleModule.ParticleSet.MAGIC_RED, true
               );
         }
      }

      for (int ixx = 0; ixx < 10; ixx++) {
         particleX = Utilities.randomInt(930);
         particleY = this.y + Utilities.randomInt(5);
         if (particleX <= corruptionBarLength + 5) {
            this.particle
               .newParticleGroup(
                  this.x + 15 + particleX, this.y + 48 + particleY, ParticleModule.ParticleGroup.SPARKLES, ParticleModule.ParticleSet.MAGIC_RED, true
               );
         }
      }
   }

   private void renderInflunceBar(boolean debug) {
      this.g.setColor(new Color(0, 0, 0, 150));
      this.g.fillRect(this.x + 15, this.y + 64, 930.0F, 12.0F);
      SpellBase.SpellType highlightedSpell = this.controller.getSpellBar().getHighlightedSpell();
      GUIEnums.InterfaceMode interfaceMode = this.controller.getGUIData().getInterfaceMode();
      if (interfaceMode == GUIEnums.InterfaceMode.INFLUENCE && highlightedSpell == null) {
         highlightedSpell = this.influence.getSelectedSpell();
      }

      int influenceCost = 0;
      int maintainCost = 0;
      if (highlightedSpell != null) {
         influenceCost = (int)Math.ceil((float)this.influence.getCastCost(highlightedSpell) / this.influence.getInfluenceAmountMax() * 930.0F);
         if (influenceCost > 931) {
            influenceCost = 931;
         }

         maintainCost = (int)Math.ceil((float)this.influence.getMaintainCost(highlightedSpell) / this.influence.getInfluenceAmountMax() * 930.0F);
      }

      this.influnceBarLeft = (int)Math.ceil((float)this.influence.getInfluenceAmount() / this.influence.getInfluenceAmountMax() * 930.0F);
      int influnceBarMaintain = (int)Math.ceil((float)this.influence.getInfluenceMaintainingAmount() / this.influence.getInfluenceAmountMax() * 930.0F);
      if (this.influnceBarOld > this.influnceBarLeft) {
         int influenceDifference = this.influnceBarOld - this.influnceBarLeft;
         int barPosition = this.influnceBarLeft;

         for (int i = 0; i < influenceDifference * 3; i++) {
            this.particle
               .newParticleGroup(
                  this.x + 15 + barPosition + Utilities.randomInt(influenceDifference),
                  this.y + 64 + Utilities.randomInt(10),
                  ParticleModule.ParticleGroup.FLOATING_MAGIC_SQUARES,
                  ParticleModule.ParticleSet.FIRE_RED,
                  true
               );
         }
      }

      this.influnceBarOld = this.influnceBarLeft;
      if ((interfaceMode == GUIEnums.InterfaceMode.INFLUENCE || highlightedSpell != null) && influenceCost > this.influnceBarLeft) {
         this.g.setColor(new Color(60, 0, 0));
         this.g.fillRect(this.x + 15, this.y + 64, influenceCost, 12.0F);
         this.g.setColor(new Color(150, 0, 0));
         this.g.fillRect(this.x + 15, this.y + 64, this.influnceBarLeft, 12.0F);
      } else {
         this.g.setColor(Color.blue);
         this.g.fillRect(this.x + 15, this.y + 64, this.influnceBarLeft, 12.0F);
      }

      this.g.setColor(Color.white);
      this.g.fillRect(this.x + 15 + 930 - influnceBarMaintain, this.y + 64, influnceBarMaintain, 12.0F);
      if ((interfaceMode == GUIEnums.InterfaceMode.INFLUENCE || highlightedSpell != null) && influenceCost > this.influnceBarLeft) {
         this.g.setColor(new Color(200, 0, 0, 150));
         this.g.fillRect(this.x + 15, this.y + 68, influenceCost, 4.0F);
      }

      if ((interfaceMode == GUIEnums.InterfaceMode.INFLUENCE || highlightedSpell != null) && influenceCost <= this.influnceBarLeft) {
         this.g.setColor(new Color(150, 80, 0));
         this.g.fillRect(this.x + 15 + this.influnceBarLeft - influenceCost, this.y + 64, influenceCost, 12.0F);
         if (maintainCost > 0) {
            this.g.setColor(new Color(255, 255, 255, 100));
            this.g.fillRect(this.x + 15 + 930 - maintainCost - influnceBarMaintain, this.y + 64, maintainCost, 12.0F);
         }
      }

      int particleX = Utilities.randomInt(930);
      int particleY = this.y + Utilities.randomInt(10);
      if (particleX <= this.influnceBarLeft + 5) {
         this.particle
            .newParticleGroup(
               this.x + 15 + particleX,
               this.y + 59 + particleY,
               ParticleModule.ParticleGroup.FLOATING_MAGIC_SQUARES,
               ParticleModule.ParticleSet.MAGIC_BLUE,
               true
            );
      }

      for (int i = 0; i < 3; i++) {
         particleX = Utilities.randomInt(930);
         particleY = this.y + Utilities.randomInt(10);
         if (particleX <= this.influnceBarLeft + 5) {
            this.particle
               .newParticleGroup(
                  this.x + 15 + particleX, this.y + 59 + particleY, ParticleModule.ParticleGroup.MAGIC_FALL, ParticleModule.ParticleSet.MAGIC_BLUE, true
               );
         }
      }

      for (int ix = 0; ix < 5; ix++) {
         particleX = Utilities.randomInt(930);
         particleY = this.y + Utilities.randomInt(10);
         if (particleX <= this.influnceBarLeft + 5) {
            this.particle
               .newParticle(
                  this.x + 15 + particleX, this.y + 59 + particleY, ParticleModule.ParticleType.SPARKLE_SPECK, ParticleModule.ParticleSet.MAGIC_BLUE, true
               );
         }
      }

      for (int ixx = 0; ixx < 5; ixx++) {
         particleX = Utilities.randomInt(930);
         particleY = this.y + Utilities.randomInt(5);
         if (particleX <= this.influnceBarLeft + 5) {
            this.particle
               .newParticleGroup(
                  this.x + 15 + particleX, this.y + 62 + particleY, ParticleModule.ParticleGroup.SPARKLES, ParticleModule.ParticleSet.MAGIC_BLUE, true
               );
         }
      }
   }

   private void renderResources(boolean debug) {
      this.resourceFilterToggle.render(this.g, this.mouse, this.x + 18, this.y + 16, debug);
      switch (this.resourceFilter) {
         case ALL:
            this.font.drawString(this.x + 38, this.y + 18, Text.getText("playResourceAndInfluenceBar.sort.total"), Text.FontType.BODY, 1);
            break;
         case GROUND:
            this.font.drawString(this.x + 38, this.y + 18, Text.getText("playResourceAndInfluenceBar.sort.ground"), Text.FontType.BODY, 1);
            break;
         case STORAGE:
            this.font.drawString(this.x + 38, this.y + 18, Text.getText("playResourceAndInfluenceBar.sort.stored"), Text.FontType.BODY, 1);
            break;
         case EQUIPPED:
            this.font.drawString(this.x + 38, this.y + 18, Text.getText("playResourceAndInfluenceBar.sort.equip"), Text.FontType.BODY, 1);
            break;
         case CARRIED:
            this.font.drawString(this.x + 38, this.y + 18, Text.getText("playResourceAndInfluenceBar.sort.carry"), Text.FontType.BODY, 1);
            break;
         case RATE:
            this.font.drawString(this.x + 38, this.y + 18, Text.getText("playResourceAndInfluenceBar.sort.rate"), Text.FontType.BODY, 1);
      }

      int resourceAlignX = 0;
      int resourceAlignY = 0;
      int shiftY = 0;
      int length = 0;

      ResourceModule.ResourceType[] var9;
      for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
         if (this.resource.isOnResourceBarMain(type)) {
            if (++length > 16) {
               shiftY -= 8;
               break;
            }
         }
      }

      for (ResourceModule.ResourceType typex : ResourceModule.ResourceType.values()) {
         if (this.resource.isOnResourceBarMain(typex)) {
            this.resourceIcons.get(typex).render(this.g, this.x + 82 + resourceAlignX, this.y + 17 + resourceAlignY + shiftY, debug);
            this.resourceIconsArea.get(typex).render(this.g, this.mouse, this.x + 100 + resourceAlignX, this.y + 17 + resourceAlignY + shiftY, false, debug);
            int getLength = 0;
            String colorCode = "$RED1";
            switch (this.resourceFilter) {
               case ALL:
                  getLength = Text.getWidth(this.resource.getResourceTotal(typex, true) + "/", Text.FontType.BODY, 1);
                  if (this.resource.getResourceTotal(typex, true) > 0) {
                     colorCode = "$GRE0";
                  }

                  this.font
                     .drawString(
                        this.x + 99 + resourceAlignX,
                        this.y + 19 + resourceAlignY + shiftY,
                        colorCode + this.resource.getResourceTotal(typex, true) + "$GRE0/",
                        Text.FontType.BODY,
                        1
                     );
                  this.font
                     .drawString(
                        this.x + 98 + resourceAlignX + getLength,
                        this.y + 23 + resourceAlignY + shiftY,
                        "$GRE2" + this.resource.getResourceStoresSpace(typex),
                        Text.FontType.BODY,
                        0
                     );
                  break;
               case GROUND:
                  getLength = Text.getWidth(this.resource.getResourceGround(typex, true) + "/", Text.FontType.BODY, 1);
                  if (this.resource.getResourceGround(typex, true) > 0) {
                     colorCode = "$ORA0";
                  }

                  this.font
                     .drawString(
                        this.x + 99 + resourceAlignX,
                        this.y + 19 + resourceAlignY + shiftY,
                        colorCode + this.resource.getResourceGround(typex, true) + "$ORA0/",
                        Text.FontType.BODY,
                        1
                     );
                  this.font
                     .drawString(
                        this.x + 98 + resourceAlignX + getLength,
                        this.y + 23 + resourceAlignY + shiftY,
                        "$ORA2" + this.resource.getResourceStoresSpace(typex),
                        Text.FontType.BODY,
                        0
                     );
                  break;
               case STORAGE:
                  getLength = Text.getWidth(this.resource.getResourceStores(typex) + "/", Text.FontType.BODY, 1);
                  if (this.resource.getResourceStores(typex) > 0) {
                     colorCode = "$MAG0";
                  }

                  this.font
                     .drawString(
                        this.x + 99 + resourceAlignX,
                        this.y + 19 + resourceAlignY + shiftY,
                        colorCode + this.resource.getResourceStores(typex) + "$MAG0/",
                        Text.FontType.BODY,
                        1
                     );
                  this.font
                     .drawString(
                        this.x + 98 + resourceAlignX + getLength,
                        this.y + 23 + resourceAlignY + shiftY,
                        "$MAG2" + this.resource.getResourceStoresSpace(typex),
                        Text.FontType.BODY,
                        0
                     );
                  break;
               case EQUIPPED:
                  getLength = Text.getWidth(this.resource.getResourceEquipped(typex) + "/", Text.FontType.BODY, 1);
                  if (this.resource.getResourceEquipped(typex) > 0) {
                     colorCode = "$YEL0";
                  }

                  this.font
                     .drawString(
                        this.x + 99 + resourceAlignX,
                        this.y + 19 + resourceAlignY + shiftY,
                        colorCode + this.resource.getResourceEquipped(typex) + "$YEL0/",
                        Text.FontType.BODY,
                        1
                     );
                  this.font
                     .drawString(
                        this.x + 98 + resourceAlignX + getLength,
                        this.y + 23 + resourceAlignY + shiftY,
                        "$YEL2" + this.resource.getResourceStoresSpace(typex),
                        Text.FontType.BODY,
                        0
                     );
                  break;
               case CARRIED:
                  getLength = Text.getWidth(this.resource.getResourceCarried(typex) + "/", Text.FontType.BODY, 1);
                  if (this.resource.getResourceCarried(typex) > 0) {
                     colorCode = "$CYA0";
                  }

                  this.font
                     .drawString(
                        this.x + 99 + resourceAlignX,
                        this.y + 19 + resourceAlignY + shiftY,
                        colorCode + this.resource.getResourceCarried(typex) + "$CYA0/",
                        Text.FontType.BODY,
                        1
                     );
                  this.font
                     .drawString(
                        this.x + 98 + resourceAlignX + getLength,
                        this.y + 23 + resourceAlignY + shiftY,
                        "$CYA2" + this.resource.getResourceStoresSpace(typex),
                        Text.FontType.BODY,
                        0
                     );
                  break;
               case RATE:
                  getLength = Text.getWidth(String.valueOf(this.resource.getResourceTotal(typex, true)), Text.FontType.BODY, 0);
                  if (this.resource.getResourceRate(typex) > 0) {
                     colorCode = "$BLA2\\$GRE0+";
                  } else if (this.resource.getResourceRate(typex) < 0) {
                     colorCode = "$BLA2\\$RED0";
                  }

                  if (this.resource.getResourceRate(typex) == 0) {
                     this.font
                        .drawString(
                           this.x + 98 + resourceAlignX,
                           this.y + 23 + resourceAlignY + shiftY,
                           "" + this.resource.getResourceTotal(typex, true),
                           Text.FontType.BODY,
                           0
                        );
                     this.font
                        .drawString(this.x + 98 + resourceAlignX + getLength - 1, this.y + 19 + resourceAlignY + shiftY, "$BLA2\\~", Text.FontType.BODY, 0);
                  } else {
                     this.font
                        .drawString(
                           this.x + 98 + resourceAlignX,
                           this.y + 23 + resourceAlignY + shiftY,
                           "" + this.resource.getResourceTotal(typex, true),
                           Text.FontType.BODY,
                           0
                        );
                     this.font
                        .drawString(
                           this.x + 98 + resourceAlignX + getLength - 1,
                           this.y + 19 + resourceAlignY + shiftY,
                           colorCode + this.resource.getResourceRate(typex),
                           Text.FontType.BODY,
                           1
                        );
                  }
            }

            resourceAlignX += 53;
            if (resourceAlignX > 795) {
               resourceAlignX = 0;
               resourceAlignY += 13;
            }
         }
      }

      int alignY = 12;

      ResourceModule.ResourceBarType[] var26;
      for (ResourceModule.ResourceBarType typexx : ResourceModule.ResourceBarType.values()) {
         if (this.resource.isOnResourceBarRight(typexx)) {
            switch (typexx) {
               case GOLD:
                  this.sidebarTotals(
                     this.iconGoldCoins, this.resource.getTotalAvailableGoldCoins(), this.resource.getMaximumAvailableGoldCoins(), alignY, debug
                  );
                  alignY += 14;
                  break;
               case FOOD:
                  this.sidebarTotals(this.iconTotalFood, this.resource.getTotalAvailableFood(), this.resource.getMaximumAvailableFood(), alignY, debug);
                  alignY += 14;
                  break;
               case WATER:
                  this.sidebarTotals(this.iconTotalWater, this.resource.getTotalAvailableWater(), this.resource.getMaximumAvailableWater(), alignY, debug);
                  alignY += 14;
                  break;
               case DIRTY_WATER:
                  this.sidebarTotals(
                     this.iconTotalDirtyWater, this.resource.getTotalAvailableDirtyWater(), this.resource.getMaximumAvailableDirtyWater(), alignY, debug
                  );
                  alignY += 14;
                  break;
               case ENERGY:
                  this.sidebarTotals(this.iconTotalEnergy, this.resource.getTotalAvailableEnergy(), this.resource.getMaximumAvailableEnergy(), alignY, debug);
                  alignY += 14;
                  break;
               case FAITH:
                  this.sidebarTotals(this.iconTotalFaith, this.mob.getTotalAvailableFaith(), this.mob.getTotalMaximumFaith(), alignY, debug);
                  alignY += 14;
            }
         }
      }
   }

   private void sidebarTotals(GUIIcon icon, int lowNum, int highNum, int sidebarAlignY, boolean debug) {
      icon.render(this.g, this.x + 964, this.y + sidebarAlignY, debug);
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
      this.g.fillRect(this.x + 983, this.y + sidebarAlignY + 1, barMaxWidth + 2, 12.0F);
      if (barWidth > barMaxWidth * 0.75) {
         this.g.setColor(new Color(0, 100, 0));
      } else if (barWidth > barMaxWidth * 0.5) {
         this.g.setColor(new Color(100, 100, 0));
      } else if (barWidth > barMaxWidth * 0.25) {
         this.g.setColor(new Color(100, 50, 0));
      } else {
         this.g.setColor(new Color(100, 0, 0));
      }

      this.g.fillRect(this.x + 984, this.y + sidebarAlignY + 2, barWidth, 10.0F);
      if (barWidthOverLimit > 0) {
         this.g.setColor(new Color(0, 200, 0));
         this.g.fillRect(this.x + 984, this.y + sidebarAlignY + 2, barWidthOverLimit, 10.0F);
      }

      this.statRectangle.render(this.g, this.x + 986, this.y + sidebarAlignY - 2, debug);
      if (!debug && !this.statRectangle.intersects(this.mouse)) {
         this.font.drawString(this.x + 987, this.y + sidebarAlignY, "$YEL1" + Integer.toString(lowNum), Text.FontType.BODY, 0);
      } else {
         String low = Integer.toString(lowNum);
         if (lowNum >= 1000) {
            DecimalFormat lowD = new DecimalFormat("#.#");
            low = lowD.format(lowNum / 1000.0) + "k";
         }

         String high = Integer.toString(highNum);
         if (highNum >= 1000) {
            DecimalFormat highD = new DecimalFormat("#.#");
            high = highD.format(highNum / 1000.0) + "k";
         }

         this.font.drawString(this.x + 987, this.y + sidebarAlignY, "$YEL1" + low + "/" + high, Text.FontType.BODY, 0);
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
