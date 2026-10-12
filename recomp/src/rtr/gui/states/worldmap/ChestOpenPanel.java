package rtr.gui.states.worldmap;

import java.util.ArrayList;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.PerkModule;
import rtr.chest.Chest;
import rtr.font.Text;
import rtr.gui.buttons.GUIButtonGenericWood;
import rtr.gui.buttons.GUIButtonPerk;
import rtr.gui.states.WorldMapGUIController;
import rtr.gui.states.WorldMapGUIData;
import rtr.gui.states.WorldMapGUIPanelBase;
import rtr.particles.ParticleModule;
import rtr.system.ScaleControl;
import rtr.utilities.Utilities;

public class ChestOpenPanel extends WorldMapGUIPanelBase {
   private Image iconGodXP;
   private Chest selectedChest;
   private ArrayList<PerkModule.Perk> selectedChestPerks;
   private ArrayList<Image> perkListTopBar = new ArrayList<>();
   private ArrayList<GUIButtonPerk> perkList = new ArrayList<>();
   private int selectedChestOpenCost;
   private boolean revealed;
   private GUIButtonGenericWood takeIt;

   public ChestOpenPanel(WorldMapGUIController controller, WorldMapGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.iconGodXP = ImageLoader.getImage("res/GUI/icon/iconGodXP.png");
      this.takeIt = new GUIButtonGenericWood("takeIt", Text.getText("worldMapChestOpenPanel.button.takeIt"));
      this.guiButtons.add(this.takeIt);
   }

   public void render(boolean debug) throws SlickException {
      this.x = ScaleControl.getInterfaceWidth() / 2 - 275;
      this.y = 130;
      this.selectedChest.renderEffects(this.g, this.mouse, this.x + 250, this.y + 41);
      this.selectedChest.render(this.g, this.mouse, this.x + 250, this.y + 41, debug);
      int fontWidth = Text.getWidth("$GRE0" + this.selectedChestOpenCost, Text.FontType.BODY, 1) / 2;
      this.iconGodXP.draw(this.x + 273 - fontWidth - 14, this.y + 79);
      this.font.drawString(this.x + 273, this.y + 79, "$GRE0" + this.selectedChestOpenCost, Text.FontType.BODY, 1, true);
      if (this.selectedChestOpenCost > 0) {
         if (this.selectedChestOpenCost > 0) {
            this.selectedChestOpenCost -= 5;
         }

         if (this.selectedChestOpenCost > 500) {
            this.selectedChestOpenCost -= 10;
         }

         if (this.selectedChestOpenCost > 1000) {
            this.selectedChestOpenCost -= 10;
         }
      }

      if (this.selectedChestOpenCost < 0) {
         this.selectedChestOpenCost = 0;
      }

      if (this.selectedChest.isOpen()) {
         int alignX = 0;
         int alignY = 0;

         for (int i = 0; i < this.perkList.size(); i++) {
            int xMod = this.x + alignX;
            if (i == this.perkList.size() - 1 && this.perkList.size() % 2 != 0) {
               xMod += 125;
            }

            int yMod = this.y + alignY;
            this.perkList.get(i).render(this.g, this.mouse, xMod + 11, yMod + 140, debug);
            this.perkListTopBar.get(i).draw(xMod + 41, yMod + 115);
            this.font.drawString(xMod + 150, yMod + 123, "$YEL0" + this.selectedChestPerks.get(i).getPerkName(), Text.FontType.BODY, 2, true);
            if (!this.revealed) {
               for (int z = 0; z < 500; z++) {
                  this.particle
                     .newParticleGroup(
                        xMod + 11 + Utilities.randomInt(255),
                        yMod + 115 + Utilities.randomInt(55),
                        ParticleModule.ParticleGroup.SMOKE_BELLOW,
                        ParticleModule.ParticleSet.FIRE_BLUE,
                        ParticleModule.ParticleSet.FIRE_BLUE,
                        true
                     );
               }
            }

            if (alignX == 0) {
               alignX += 270;
            } else if (i != this.perkList.size() - 1) {
               alignX = 0;
               alignY += 60;
            }
         }

         this.revealed = true;
         this.takeIt.render(this.g, this.mouse, this.x + 275, this.y + 175 + alignY, false, true, debug);
      }
   }

   @Override
   public void update() {
   }

   public void startOpenSelectedChest(Chest c, ArrayList<PerkModule.Perk> pList) throws SlickException {
      this.selectedChest = c;
      this.selectedChestOpenCost = c.getOpenCost();
      c.openChest();
      this.selectedChestPerks = pList;

      for (PerkModule.Perk p : this.selectedChestPerks) {
         GUIButtonPerk newPerk = new GUIButtonPerk("perk" + p, p);
         this.perkList.add(newPerk);
         this.perkListTopBar.add(ImageLoader.getImage("res/GUI/gameState/perkBarTop" + (Utilities.randomInt(1) + 1) + ".png"));
      }
   }

   public void reset() throws SlickException {
      this.selectedChest = null;
      this.selectedChestOpenCost = 0;
      this.perkList.clear();
      this.perkListTopBar.clear();
      this.revealed = false;
   }

   public Chest getSelectedChest() {
      return this.selectedChest;
   }

   @Override
   public int getCenterX() {
      return this.x + 275;
   }

   @Override
   public int getCenterY() {
      return this.y + 275;
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
      return this.x + 275;
   }

   @Override
   public int getBottomY() {
      return this.y + 275;
   }
}
