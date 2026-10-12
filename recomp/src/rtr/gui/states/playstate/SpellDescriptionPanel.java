package rtr.gui.states.playstate;

import java.text.DecimalFormat;
import java.util.EnumMap;
import java.util.HashMap;
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
import rtr.influence.SpellBase;
import rtr.map.MapTilesLoader;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class SpellDescriptionPanel extends PlayStateGUIPanelBase {
   private Image panel;
   private GUIIcon iconEnergy;
   private EnumMap<ResourceModule.ResourceType, GUIIcon> icons = new EnumMap<>(ResourceModule.ResourceType.class);
   private GUIIcon iconHomeOccupants;
   private GUIIcon iconRangerLodgeOccupants;
   private GUIIcon iconWorkers;
   private GUIIcon iconBuildingSlots;
   private GUIIcon iconRange;
   private GUIIcon iconDesirability;
   private GUIIcon iconProvidedMigrationsPerDay;
   private GUIIcon iconAmmo;
   private GUIIcon objectDescriptionStorage;
   private SpellBase highlightedSpell;

   public SpellDescriptionPanel(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.init();
   }

   private void init() throws SlickException {
      this.panel = ImageLoader.getImage("res/GUI/gameState/spellDescriptionPanel.png");
      this.iconEnergy = new GUIIcon("iconEnergy");
      this.guiButtons.add(this.iconEnergy);

      ResourceModule.ResourceType[] var4;
      for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
         GUIIcon newIcon = new GUIIcon(type);
         this.icons.put(type, newIcon);
         this.guiButtons.add(newIcon);
      }

      this.iconHomeOccupants = new GUIIcon("iconHome");
      this.guiButtons.add(this.iconHomeOccupants);
      this.iconRangerLodgeOccupants = new GUIIcon("iconRangerLodge");
      this.guiButtons.add(this.iconRangerLodgeOccupants);
      this.iconWorkers = new GUIIcon("iconWorkers");
      this.guiButtons.add(this.iconWorkers);
      this.iconBuildingSlots = new GUIIcon("iconCivic");
      this.guiButtons.add(this.iconBuildingSlots);
      this.iconRange = new GUIIcon("iconRange");
      this.guiButtons.add(this.iconRange);
      this.iconDesirability = new GUIIcon("iconDesirability");
      this.guiButtons.add(this.iconDesirability);
      this.iconProvidedMigrationsPerDay = new GUIIcon("iconMigration");
      this.guiButtons.add(this.iconProvidedMigrationsPerDay);
      this.objectDescriptionStorage = new GUIIcon("iconChest");
      this.guiButtons.add(this.objectDescriptionStorage);
      this.iconAmmo = new GUIIcon("iconAmmo");
      this.guiButtons.add(this.iconAmmo);
   }

   public void render(boolean debug) throws SlickException {
      if (this.controller.isSpellSelectPanelEnabled()) {
         this.x = this.controller.getResourceAndInfluenceBar().getLeftX() + 408;
      } else {
         this.x = this.controller.getResourceAndInfluenceBar().getLeftX() + 73;
      }

      this.y = 151;
      if (this.y < 134) {
         this.y = 134;
      }

      this.panel.draw(this.x, this.y);
      if (this.highlightedSpell.getName().length() > 32) {
         this.font.drawString(this.x + 135, this.y + 8, "$CYA1" + this.highlightedSpell.getName(), Text.FontType.HEADER, 0, true);
      } else if (this.highlightedSpell.getName().length() > 24) {
         this.font.drawString(this.x + 135, this.y + 6, "$CYA1" + this.highlightedSpell.getName(), Text.FontType.HEADER, 1, true);
      } else {
         this.font.drawString(this.x + 135, this.y + 4, "$CYA1" + this.highlightedSpell.getName(), Text.FontType.HEADER, 2, true);
      }

      EnumMap<ResourceModule.ResourceType, String> manufacuringBuildingNames = new EnumMap<>(ResourceModule.ResourceType.class);
      HashMap<MapTilesLoader.TileSet, HashMap<ObjectBase.ObjectSubType, ObjectFlags>> objectFlagList = this.object.getObjectFlagFactory().getObjectFlagsList();

      ResourceModule.ResourceType[] df;
      for (ResourceModule.ResourceType type : df = ResourceModule.ResourceType.values()) {
         for (HashMap<ObjectBase.ObjectSubType, ObjectFlags> o : objectFlagList.values()) {
            if (o.get(ObjectBase.ObjectSubType.BUILT).getWorkerJobType().getJob().canWorkerRefine(type)
               && o.get(ObjectBase.ObjectSubType.BUILT).getCanBuildingStore().contains(type)) {
               manufacuringBuildingNames.put(type, o.get(ObjectBase.ObjectSubType.BUILT).getBaseName());
            }
         }
      }

      int alignX = 0;
      int alignY = 27;
      String buildingDescription = Text.wrapString(this.highlightedSpell.getDescription(), Text.FontType.BODY, 1, 230);
      this.font.drawString(this.x + alignX + 20, this.y + alignY, buildingDescription, Text.FontType.BODY, 1);
      alignY += 111;
      if (this.highlightedSpell.getCastCost() > 0 || this.highlightedSpell.getMaintainCost() > 0) {
         int percentCast = (int)Math.ceil((float)this.highlightedSpell.getCastCost() / this.influence.getInfluenceAmountMax() * 100.0);
         int percentMaintain = (int)Math.ceil((float)this.highlightedSpell.getMaintainCost() / this.influence.getInfluenceAmountMax() * 100.0);
         if (percentCast > 100 || percentMaintain > 100) {
            this.font
               .drawString(this.x + alignX + 24, this.y + alignY, Text.getText("playSpellDescriptionPanel.notEnoughMaximumInfluence"), Text.FontType.BODY, 1);
            alignY += 16;
         }
      }

      if (this.highlightedSpell.getDuration() > 0) {
         DecimalFormat dfx = new DecimalFormat("####0.0");
         Text.setVariableText("amount", dfx.format(1.0 * this.highlightedSpell.getDuration() / 60.0));
         this.font.drawString(this.x + alignX + 24, this.y + alignY, Text.getText("playSpellDescriptionPanel.duration"), Text.FontType.BODY, 1);
         alignY += 12;
      }

      if (this.highlightedSpell.getRadius() > 0) {
         Text.setVariableText("amount", this.highlightedSpell.getRadius());
         this.font.drawString(this.x + alignX + 24, this.y + alignY, Text.getText("playSpellDescriptionPanel.radius"), Text.FontType.BODY, 1);
         alignY += 12;
      }

      if (this.highlightedSpell.getCastCost() > 0) {
         Text.setVariableText("amount", this.highlightedSpell.getCastCost());
         int percent = (int)Math.ceil((float)this.highlightedSpell.getCastCost() / this.influence.getInfluenceAmountMax() * 100.0);
         Text.setVariableText("percentage", percent);
         if (percent <= 100) {
            this.font.drawString(this.x + alignX + 24, this.y + alignY, Text.getText("playSpellDescriptionPanel.castCost"), Text.FontType.BODY, 1);
         } else {
            this.font.drawString(this.x + alignX + 24, this.y + alignY, Text.getText("playSpellDescriptionPanel.castCostCantCast"), Text.FontType.BODY, 1);
         }

         alignY += 12;
      }

      if (this.highlightedSpell.getMaintainCost() > 0) {
         Text.setVariableText("amount", this.highlightedSpell.getMaintainCost());
         int percent = (int)Math.ceil((float)this.highlightedSpell.getMaintainCost() / this.influence.getInfluenceAmountMax() * 100.0);
         Text.setVariableText("percentage", percent);
         if (percent <= 100) {
            this.font.drawString(this.x + alignX + 24, this.y + alignY, Text.getText("playSpellDescriptionPanel.maintainCost"), Text.FontType.BODY, 1);
         } else {
            this.font
               .drawString(this.x + alignX + 24, this.y + alignY, Text.getText("playSpellDescriptionPanel.maintainCostCantMaintain"), Text.FontType.BODY, 1);
         }

         alignY += 12;
      }

      if (this.highlightedSpell.getCooldown() > 0) {
         DecimalFormat dfx = new DecimalFormat("####0.0");
         Text.setVariableText("amount", dfx.format(1.0 * this.highlightedSpell.getCooldown() / 60.0));
         this.font.drawString(this.x + alignX + 24, this.y + alignY, Text.getText("playSpellDescriptionPanel.cooldown"), Text.FontType.BODY, 1);
         alignY += 12;
      }

      if (this.highlightedSpell.allowContinuousCast()) {
         this.font.drawString(this.x + alignX + 24, this.y + alignY, Text.getText("playSpellDescriptionPanel.continuousCast"), Text.FontType.BODY, 1);
         alignY += 12;
      }
   }

   @Override
   public void update() {
      SpellBase.SpellType t = this.controller.getSpellSelectPanel().getIntersectingSpellType();
      if (t == null) {
         t = this.controller.getSpellBar().getIntersectingSpellType();
      }

      if (t != null) {
         this.setHighlightedSpell(t);
      } else {
         this.resetHighlightedSpell();
      }
   }

   public SpellBase getHighlightedSpell() {
      return this.highlightedSpell;
   }

   public void setHighlightedSpell(SpellBase.SpellType t) {
      this.highlightedSpell = this.influence.getSpellFactory().getSpellInstance(t);
   }

   public void resetHighlightedSpell() {
      this.highlightedSpell = null;
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
