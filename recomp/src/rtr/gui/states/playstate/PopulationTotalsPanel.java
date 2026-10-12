package rtr.gui.states.playstate;

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
import rtr.mobs.MobBase;

public class PopulationTotalsPanel extends PlayStateGUIPanelBase {
   private Image panel = ImageLoader.getImage("res/GUI/gameState/populationTotalsPanel.png");
   private Rectangle mask = new Rectangle(0.0F, 0.0F, this.panel.getWidth(), this.panel.getHeight());
   private GUIIcon iconVillagers;
   private GUIIcon iconHomeOccupancy;
   private GUIIcon iconRangerLodgeOccupancy;
   private GUIIcon iconAncillaries;
   private GUIIcon iconBuildings;
   private GUIIcon iconChildren;

   public PopulationTotalsPanel(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.masks.add(this.mask);
      this.iconVillagers = new GUIIcon("iconVillager");
      this.iconVillagers.setToolTip(Text.getText("playPopulationTotalsPanel.tooltip.totalPopulationHeader"));
      this.guiButtons.add(this.iconVillagers);
      this.iconHomeOccupancy = new GUIIcon("iconHome");
      this.iconHomeOccupancy
         .setToolTip(
            Text.getText("playPopulationTotalsPanel.tooltip.totalHomeOccupancyHeader"), Text.getText("playPopulationTotalsPanel.tooltip.totalHomeOccupancy")
         );
      this.guiButtons.add(this.iconHomeOccupancy);
      this.iconRangerLodgeOccupancy = new GUIIcon("iconRangerLodge");
      this.iconRangerLodgeOccupancy
         .setToolTip(
            Text.getText("playPopulationTotalsPanel.tooltip.totalRangerLodgeOccupancyHeader"),
            Text.getText("playPopulationTotalsPanel.tooltip.totalRangerLodgeOccupancy")
         );
      this.guiButtons.add(this.iconRangerLodgeOccupancy);
      this.iconAncillaries = new GUIIcon("iconCivic");
      this.iconAncillaries
         .setToolTip(
            Text.getText("playPopulationTotalsPanel.tooltip.totalAncillariesHeader"), Text.getText("playPopulationTotalsPanel.tooltip.totalAncillaries")
         );
      this.guiButtons.add(this.iconAncillaries);
      this.iconBuildings = new GUIIcon("iconCivic");
      this.iconBuildings
         .setToolTip(Text.getText("playPopulationTotalsPanel.tooltip.totalBuildingsHeader"), Text.getText("playPopulationTotalsPanel.tooltip.totalBuildings"));
      this.guiButtons.add(this.iconBuildings);
      this.iconChildren = new GUIIcon("iconChild");
      this.iconChildren
         .setToolTip(Text.getText("playPopulationTotalsPanel.tooltip.totalChildrenHeader"), Text.getText("playPopulationTotalsPanel.tooltip.totalChildren"));
      this.guiButtons.add(this.iconChildren);
   }

   public void render(boolean debug) throws SlickException {
      this.x = 6;
      this.y = 6;
      this.mask.setX(this.x);
      this.mask.setY(this.y);
      this.panel.draw(this.x, this.y);
      this.font.drawString(this.x + this.panel.getWidth() / 2, 10.0F, Text.getText("playPopulationTotalsPanel.header"), Text.FontType.HEADER, 1, true);
      int alignY = 0;
      this.iconVillagers.render(this.g, this.x + 17, this.y + 25 + alignY, debug);
      this.font.drawString(this.x + 38, this.y + 23 + alignY, "" + this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false).size(), Text.FontType.BODY, 2);
      alignY += 15;
      this.iconHomeOccupancy.render(this.g, this.x + 17, this.y + 25 + alignY, debug);
      this.font.drawString(this.x + 38, this.y + 23 + alignY, this.guiData.getOccupants() + "/" + this.guiData.getOccupantsMax(), Text.FontType.BODY, 2);
      alignY += 15;
      this.iconRangerLodgeOccupancy.render(this.g, this.x + 17, this.y + 25 + alignY, debug);
      this.font
         .drawString(
            this.x + 38, this.y + 23 + alignY, this.guiData.getRangerLodgeOccupants() + "/" + this.guiData.getRangerLodgeOccupantsMax(), Text.FontType.BODY, 2
         );
      alignY += 15;
      this.iconAncillaries.render(this.g, this.x + 17, this.y + 25 + alignY, debug);
      this.font
         .drawString(this.x + 38, this.y + 23 + alignY, this.object.getActiveAncillaries() + "/" + this.object.getMaximumAncillaries(), Text.FontType.BODY, 2);
      alignY += 15;
      this.iconBuildings.render(this.g, this.x + 17, this.y + 25 + alignY, debug);
      this.font
         .drawString(this.x + 38, this.y + 23 + alignY, this.object.getActiveBuildings() + "/" + this.object.getMaximumBuildings(), Text.FontType.BODY, 2);
      alignY += 15;
      this.iconChildren.render(this.g, this.x + 17, this.y + 25 + alignY, debug);
      int childrenAmount = this.mob.getMobsByType(MobBase.MobType.HUMAN_VILLAGER_CHILD, false).size();
      childrenAmount += this.mob.getMobsByType(MobBase.MobType.HUMAN_VILLAGER_CHILD_NEPHILIM, false).size();
      childrenAmount += this.mob.getMobsByType(MobBase.MobType.CATJEET_VILLAGER_CHILD, false).size();
      childrenAmount += this.mob.getMobsByType(MobBase.MobType.CATJEET_VILLAGER_CHILD_NEPHILIM, false).size();
      this.font.drawString(this.x + 38, this.y + 23 + alignY, "" + childrenAmount, Text.FontType.BODY, 2);
      alignY += 15;
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
