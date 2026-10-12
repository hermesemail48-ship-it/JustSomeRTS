package rtr.gui.states.worldmap;

import java.util.ArrayList;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ModuleBase;
import rtr.PerkModule;
import rtr.font.Text;
import rtr.gui.buttons.GUIButtonPage;
import rtr.gui.buttons.GUIButtonPerk;
import rtr.gui.states.WorldMapGUIController;
import rtr.gui.states.WorldMapGUIData;
import rtr.gui.states.WorldMapGUIPanelBase;
import rtr.states.StateBase;
import rtr.system.ScaleControl;

public class PerksList extends WorldMapGUIPanelBase {
   private PerkModule perk = (PerkModule)StateBase.getModule(ModuleBase.ModuleType.PERK);
   private Rectangle mask;
   private ArrayList<GUIButtonPerk> perkList = new ArrayList<>();
   private GUIButtonPage pageUp;
   private GUIButtonPage pageDown;
   private int perkPosition;
   private int maximumPerksPerPage;

   public PerksList(WorldMapGUIController controller, WorldMapGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.refreshPerksList();
   }

   public void render(boolean debug) throws SlickException {
      this.x = ScaleControl.getInterfaceWidth() - 255;
      this.y = ScaleControl.getInterfaceHeight() / 2 + 60;
      this.mask.setBounds(ScaleControl.getInterfaceWidth() - 255, 0.0F, 255.0F, ScaleControl.getInterfaceHeight());
      this.maximumPerksPerPage = ScaleControl.getInterfaceHeight() / 28 - 3;
      this.y = 3;
      int alignY = 0;
      int count = 0;
      int startPosition = this.perkPosition;
      int endPosition = startPosition + this.maximumPerksPerPage;

      for (int i = startPosition; i < endPosition && i < this.perkList.size(); i++) {
         GUIButtonPerk b = this.perkList.get(i);
         b.render(this.g, this.mouse, this.x, this.y + alignY, debug);
         if (b.isSelected()) {
            b.renderPerkList(this.g, this.x + 30, this.y + 25 + alignY);
            alignY += b.getPerkOffset();
         }

         alignY += 28;
         if (++count >= this.maximumPerksPerPage) {
            break;
         }
      }

      if (this.perkList.size() > this.maximumPerksPerPage) {
         this.pageUp.render(this.g, this.mouse, this.x - 28, this.y, this.perkPosition == 0, debug);
         this.pageDown.render(this.g, this.mouse, this.x - 28, this.y + 28, this.perkList.size() <= endPosition, debug);
      }
   }

   public void refreshPerksList() throws SlickException {
      this.perkList.clear();
      this.guiButtons.clear();
      this.pageUp = new GUIButtonPage("pageUp", "up");
      this.pageUp.setToolTip(Text.getText("worldMapPerksList.button.tooltip.pageUp"));
      this.guiButtons.add(this.pageUp);
      this.pageDown = new GUIButtonPage("pageDown", "down");
      this.pageDown.setToolTip(Text.getText("worldMapPerksList.button.tooltip.pageDown"));
      this.guiButtons.add(this.pageDown);
      this.mask = new Rectangle(0.0F, 0.0F, 300.0F, ScaleControl.getInterfaceHeight());
      this.masks.add(this.mask);

      PerkModule.PerkType[] var4;
      for (PerkModule.PerkType t : PerkModule.PerkType.values()) {
         if (this.perk.getPerkTypeTotal(t) > 0.0) {
            GUIButtonPerk newPerk = new GUIButtonPerk("perkType" + t, t);
            this.perkList.add(newPerk);
            this.guiButtons.add(newPerk);
         }
      }
   }

   public void moveUp() {
      this.perkPosition--;
      if (this.perkPosition < 0) {
         this.perkPosition = 0;
      }
   }

   public void moveDown() {
      this.perkPosition++;
      if (this.perkPosition > this.perkList.size() - this.maximumPerksPerPage) {
         this.perkPosition = this.perkList.size() - this.maximumPerksPerPage;
      }

      if (this.perkPosition < 0) {
         this.perkPosition = 0;
      }
   }

   public void pageUp() {
      this.perkPosition = this.perkPosition - this.maximumPerksPerPage;
      if (this.perkPosition < 0) {
         this.perkPosition = 0;
      }
   }

   public void pageDown() {
      this.perkPosition = this.perkPosition + this.maximumPerksPerPage;
      if (this.perkPosition > this.perkList.size() - this.maximumPerksPerPage) {
         this.perkPosition = this.perkList.size() - this.maximumPerksPerPage;
      }

      if (this.perkPosition < 0) {
         this.perkPosition = 0;
      }
   }

   @Override
   public int getLeftX() {
      return this.x;
   }

   @Override
   public int getTopY() {
      return this.y;
   }
}
