package rtr.gui.states.playstate;

import java.util.EnumMap;
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

public class VillageEfficiencyPanel extends PlayStateGUIPanelBase {
   private Image panel;
   private Rectangle mask;
   private EnumMap<ResourceModule.ResourceType, GUIIcon> harvestingIcons = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIIcon> refiningIcons = new EnumMap<>(ResourceModule.ResourceType.class);

   public VillageEfficiencyPanel(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.panel = ImageLoader.getImage("res/GUI/gameState/villageEfficiencyPanel.png");
      this.mask = new Rectangle(0.0F, 0.0F, this.panel.getWidth(), this.panel.getHeight());
      this.masks.add(this.mask);

      ResourceModule.ResourceType[] var9;
      for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
         GUIIcon newIcon = new GUIIcon(type);
         newIcon.setToolTip(type.getTemplate().getName());
         this.harvestingIcons.put(type, newIcon);
         this.guiButtons.add(newIcon);
      }

      for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
         GUIIcon newIcon = new GUIIcon(type);
         newIcon.setToolTip(type.getTemplate().getName());
         this.refiningIcons.put(type, newIcon);
         this.guiButtons.add(newIcon);
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
      this.font.drawString(this.x + 135, this.y + 3, Text.getText("playVillageEfficiencyPanel.header.villageEfficiency"), Text.FontType.HEADER, 2, true);
      int alignX = 0;
      this.font.drawString(this.x + 78, this.y + 28, Text.getText("playVillageEfficiencyPanel.header.general"), Text.FontType.BODY, 2, true);
      int alignY = 0;
      if (this.object.getGlobalWorkSpeedReduction() > 0.0) {
         this.font.drawString(this.x + 25, this.y + 57 + alignY, Text.getText("playVillageEfficiencyPanel.globalSpeed"), Text.FontType.BODY, 1, false);
         this.font
            .drawString(this.x + 103, this.y + 57 + alignY, "$GRE0+" + (int)this.object.getGlobalWorkSpeedReduction() + "%", Text.FontType.BODY, 1, false);
         alignY += 13;
      }

      if (this.weather.getDisasters().size() > 0) {
         this.font.drawString(this.x + 25, this.y + 57 + alignY, Text.getText("playVillageEfficiencyPanel.weather"), Text.FontType.BODY, 1, false);
         double weatherReduction = (int)(100.0 - this.weather.getMovementSpeedReductionTotal() * 100.0F);
         this.font.drawString(this.x + 103, this.y + 57 + alignY, "$RED0-" + weatherReduction + "%", Text.FontType.BODY, 1, false);
         alignY += 13;
      }

      alignY += 13;
      if (this.object.getBuildingSpeedReduction() > 0.0
         || this.object.getRepairingSpeedReduction() > 0.0
         || this.object.getBuildingRoadSpeedReduction() > 0.0
         || this.object.getProcessingTrashSpeedReduction() > 0.0
         || this.object.getRepairingRoadSpeedReduction() > 0.0
         || this.object.getFarmingSpeedReduction() > 0.0
         || this.object.getMedicSpeedReduction() > 0.0
         || this.object.getPrayingSpeedReduction() > 0.0
         || this.object.getCapturingSoulsSpeedReduction() > 0.0
         || this.object.getTendAnimalsSpeedReduction() > 0.0
         || this.object.getClearingSpeedReduction() > 0.0
         || this.object.getDismantlingSpeedReduction() > 0.0) {
         if (this.object.getProcessingTrashSpeedReduction() > 0.0) {
            this.font.drawString(this.x + 25, this.y + 57 + alignY, Text.getText("playVillageEfficiencyPanel.processTrash"), Text.FontType.BODY, 0, false);
            this.font
               .drawString(
                  this.x + 115, this.y + 57 + alignY, "$GRE0+" + (int)this.object.getProcessingTrashSpeedReduction() + "%", Text.FontType.BODY, 0, false
               );
            alignY += 13;
         }

         if (this.object.getBuildingSpeedReduction() > 0.0) {
            this.font.drawString(this.x + 25, this.y + 57 + alignY, Text.getText("playVillageEfficiencyPanel.building"), Text.FontType.BODY, 0, false);
            this.font
               .drawString(this.x + 115, this.y + 57 + alignY, "$GRE0+" + (int)this.object.getBuildingSpeedReduction() + "%", Text.FontType.BODY, 0, false);
            alignY += 13;
         }

         if (this.object.getRepairingSpeedReduction() > 0.0) {
            this.font.drawString(this.x + 25, this.y + 57 + alignY, Text.getText("playVillageEfficiencyPanel.repairing"), Text.FontType.BODY, 0, false);
            this.font
               .drawString(this.x + 115, this.y + 57 + alignY, "$GRE0+" + (int)this.object.getRepairingSpeedReduction() + "%", Text.FontType.BODY, 0, false);
            alignY += 13;
         }

         if (this.object.getBuildingRoadSpeedReduction() > 0.0) {
            this.font.drawString(this.x + 25, this.y + 57 + alignY, Text.getText("playVillageEfficiencyPanel.buildRoad"), Text.FontType.BODY, 0, false);
            this.font
               .drawString(this.x + 115, this.y + 57 + alignY, "$GRE0+" + (int)this.object.getBuildingRoadSpeedReduction() + "%", Text.FontType.BODY, 0, false);
            alignY += 13;
         }

         if (this.object.getRepairingRoadSpeedReduction() > 0.0) {
            this.font.drawString(this.x + 25, this.y + 57 + alignY, Text.getText("playVillageEfficiencyPanel.repairRoad"), Text.FontType.BODY, 0, false);
            this.font
               .drawString(this.x + 115, this.y + 57 + alignY, "$GRE0+" + (int)this.object.getRepairingRoadSpeedReduction() + "%", Text.FontType.BODY, 0, false);
            alignY += 13;
         }

         if (this.object.getFarmingSpeedReduction() > 0.0) {
            this.font.drawString(this.x + 25, this.y + 57 + alignY, Text.getText("playVillageEfficiencyPanel.farming"), Text.FontType.BODY, 0, false);
            this.font
               .drawString(this.x + 115, this.y + 57 + alignY, "$GRE0+" + (int)this.object.getFarmingSpeedReduction() + "%", Text.FontType.BODY, 0, false);
            alignY += 13;
         }

         if (this.object.getMedicSpeedReduction() > 0.0) {
            this.font.drawString(this.x + 25, this.y + 57 + alignY, Text.getText("playVillageEfficiencyPanel.medicWork"), Text.FontType.BODY, 0, false);
            this.font.drawString(this.x + 115, this.y + 57 + alignY, "$GRE0+" + (int)this.object.getMedicSpeedReduction() + "%", Text.FontType.BODY, 0, false);
            alignY += 13;
         }

         if (this.object.getPrayingSpeedReduction() > 0.0) {
            this.font.drawString(this.x + 25, this.y + 57 + alignY, Text.getText("playVillageEfficiencyPanel.praying"), Text.FontType.BODY, 0, false);
            this.font
               .drawString(this.x + 115, this.y + 57 + alignY, "$GRE0+" + (int)this.object.getPrayingSpeedReduction() + "%", Text.FontType.BODY, 0, false);
            alignY += 13;
         }

         if (this.object.getCapturingSoulsSpeedReduction() > 0.0) {
            this.font.drawString(this.x + 25, this.y + 57 + alignY, Text.getText("playVillageEfficiencyPanel.capturingSouls"), Text.FontType.BODY, 0, false);
            this.font
               .drawString(
                  this.x + 115, this.y + 57 + alignY, "$GRE0+" + (int)this.object.getCapturingSoulsSpeedReduction() + "%", Text.FontType.BODY, 0, false
               );
            alignY += 13;
         }

         if (this.object.getTendAnimalsSpeedReduction() > 0.0) {
            this.font.drawString(this.x + 25, this.y + 57 + alignY, Text.getText("playVillageEfficiencyPanel.tendingAnimals"), Text.FontType.BODY, 0, false);
            this.font
               .drawString(this.x + 115, this.y + 57 + alignY, "$GRE0+" + (int)this.object.getTendAnimalsSpeedReduction() + "%", Text.FontType.BODY, 0, false);
            alignY += 13;
         }

         if (this.object.getClearingSpeedReduction() > 0.0) {
            this.font.drawString(this.x + 25, this.y + 57 + alignY, Text.getText("playVillageEfficiencyPanel.clearing"), Text.FontType.BODY, 0, false);
            this.font
               .drawString(this.x + 115, this.y + 57 + alignY, "$GRE0+" + (int)this.object.getClearingSpeedReduction() + "%", Text.FontType.BODY, 0, false);
            alignY += 13;
         }

         if (this.object.getDismantlingSpeedReduction() > 0.0) {
            this.font.drawString(this.x + 25, this.y + 57 + alignY, Text.getText("playVillageEfficiencyPanel.dismantling"), Text.FontType.BODY, 0, false);
            this.font
               .drawString(this.x + 115, this.y + 57 + alignY, "$GRE0+" + (int)this.object.getDismantlingSpeedReduction() + "%", Text.FontType.BODY, 0, false);
            alignY += 13;
         }

         alignY += 26;
      }

      int var8 = 0;
      int var12 = 0;
      this.font.drawString(this.x + 210, this.y + 28 + var12, Text.getText("playVillageEfficiencyPanel.header.harvestSpeed"), Text.FontType.BODY, 2, true);

      ResourceModule.ResourceType[] var7;
      for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
         if (this.object.getHarvestingSpeedReduction(t) > 0.0) {
            this.harvestingIcons.get(t).render(this.g, this.x + var8 + 155, this.y + 57 + var12, debug);
            this.font
               .drawString(
                  this.x + var8 + 170, this.y + 57 + var12, "$GRE0+" + (int)this.object.getHarvestingSpeedReduction(t) + "%", Text.FontType.BODY, 1, false
               );
            var8 += 57;
         }

         if (var8 > 57) {
            var8 = 0;
            var12 += 13;
         }
      }

      var12 += 39;
      var8 = 0;
      int var14 = 0;
      this.font.drawString(this.x + 345, this.y + 28 + var14, Text.getText("playVillageEfficiencyPanel.header.refiningSpeed"), Text.FontType.BODY, 2, true);

      for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
         if (this.object.getRefiningSpeedReduction(t) > 0.0) {
            this.refiningIcons.get(t).render(this.g, this.x + var8 + 290, this.y + 57 + var14, debug);
            this.font
               .drawString(
                  this.x + var8 + 305, this.y + 57 + var14, "$GRE0+" + (int)this.object.getRefiningSpeedReduction(t) + "%", Text.FontType.BODY, 1, false
               );
            var8 += 57;
         }

         if (var8 > 57) {
            var8 = 0;
            var14 += 13;
         }
      }

      var14 += 26;
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
