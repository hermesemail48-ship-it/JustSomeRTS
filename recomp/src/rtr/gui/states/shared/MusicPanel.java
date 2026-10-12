package rtr.gui.states.shared;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.gui.buttons.GUIClickableText;
import rtr.gui.states.MapEditorGUIController;
import rtr.gui.states.MapEditorGUIData;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.SharedGUIPanelBase;

public class MusicPanel extends SharedGUIPanelBase {
   private Image panel;
   private Rectangle mask;
   private GUIClickableText[] musicListButtons;

   public MusicPanel(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.init();
   }

   public MusicPanel(MapEditorGUIController controller, MapEditorGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.init();
   }

   private void init() throws SlickException {
      this.panel = ImageLoader.getImage("res/GUI/gameState/dataViewsDebugPanel.png");
      this.mask = new Rectangle(0.0F, 0.0F, this.panel.getWidth(), this.panel.getHeight());
      this.masks.add(this.mask);
      this.musicListButtons = new GUIClickableText[this.sound.getMusicTitles().size()];

      for (int x = 0; x < this.sound.getMusicTitles().size(); x++) {
         this.musicListButtons[x] = new GUIClickableText(this.sound.getMusicTitles().get(x), 2);
         this.guiButtons.add(this.musicListButtons[x]);
      }
   }

   public void render(boolean debug) throws SlickException {
      if (this.playStateController != null) {
         this.x = this.playStateController.getResourceAndInfluenceBar().getRightX() - this.panel.getWidth() + 72;
         if (this.x + this.panel.getWidth() > this.playStateController.getRightPanel().getLeftX() - 46) {
            this.x = this.x - Math.abs(this.x + this.panel.getWidth() - this.playStateController.getRightPanel().getLeftX() + 46);
         }

         this.y = 133;
      }

      if (this.mapEditorController != null) {
         this.x = this.mapEditorController.getSystemAndDataBar().getX() + 24;
         if (this.x + this.panel.getWidth() > this.mapEditorController.getRightPanel().getLeftX()) {
            this.x = this.x - Math.abs(this.x + this.panel.getWidth() - this.mapEditorController.getRightPanel().getLeftX());
         }

         this.y = 49;
      }

      this.mask.setX(this.x);
      this.mask.setY(this.y);
      this.panel.draw(this.x, this.y);
      this.font.drawString(this.x + 93, this.y + 3, Text.getText("sharedMusicPanel.header"), Text.FontType.HEADER, 2, true);
      if (this.sound.getCurrentMusicTitle() != null) {
         this.font.drawString(this.x + 95, this.y + 30, "$CYA0" + this.sound.getCurrentMusicTitle(), Text.FontType.HEADER, 2, true);
         this.g.setColor(new Color(0, 0, 0, 150));
         this.g.fillRect(this.x + 20, this.y + 53, 148.0F, 6.0F);
         int influenceLeft = (int)Math.ceil(this.sound.getCurrentMusic().getPosition() / this.sound.getCurrentMusicDuration() * 148.0F);
         this.g.setColor(Color.green);
         this.g.fillRect(this.x + 20, this.y + 53, influenceLeft, 6.0F);
      }

      int alignY = 0;

      for (int b = 0; b < this.sound.getMusicTitles().size(); b++) {
         if (this.sound.getCurrentMusicTitle() == this.sound.getMusicTitles().get(b)) {
            this.musicListButtons[b]
               .render(
                  this.g,
                  this.mouse,
                  "-> " + this.sound.getMusicTitles().get(b) + " <-",
                  this.x + this.panel.getWidth() / 2,
                  this.y + 64 + alignY,
                  "$WHI0",
                  "$GRE1",
                  false,
                  true,
                  debug
               );
         } else {
            this.musicListButtons[b]
               .render(
                  this.g,
                  this.mouse,
                  this.sound.getMusicTitles().get(b),
                  this.x + this.panel.getWidth() / 2,
                  this.y + 64 + alignY,
                  "$WHI0",
                  "$YEL1",
                  false,
                  true,
                  debug
               );
         }

         alignY += 18;
      }
   }

   public boolean intersectsMusic() {
      for (int i = 0; i < this.musicListButtons.length; i++) {
         if (this.musicListButtons[i].intersects(this.mouse)) {
            return true;
         }
      }

      return false;
   }

   public String getMusicTitle() {
      for (int i = 0; i < this.musicListButtons.length; i++) {
         if (this.musicListButtons[i].intersects(this.mouse)) {
            return this.sound.getMusicTitles().get(i);
         }
      }

      return null;
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
