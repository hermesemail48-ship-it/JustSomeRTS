package rtr.gui.states.shared;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import r2d.animation.Animation;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.gui.buttons.GUIButtonGenericWood;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.SharedGUIPanelBase;
import rtr.gui.states.WorldMapGUIController;
import rtr.gui.states.WorldMapGUIData;
import rtr.help.tips.TipBase;
import rtr.system.ScaleControl;

public class TipsPanel extends SharedGUIPanelBase {
   private Image panelSmall;
   private Image panelMedium;
   private Image panelLarge;
   private Image panelVideo;
   private Rectangle mask;
   private Rectangle tipPanelVideoMask;
   private GUIButtonGenericWood ok;
   private TipBase activeTip;

   public TipsPanel(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.init();
   }

   public TipsPanel(WorldMapGUIController controller, WorldMapGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.init();
   }

   private void init() throws SlickException {
      this.panelSmall = ImageLoader.getImage("res/GUI/gameState/tipPanelSmall.png");
      this.panelMedium = ImageLoader.getImage("res/GUI/gameState/tipPanelMedium.png");
      this.panelLarge = ImageLoader.getImage("res/GUI/gameState/tipPanelLarge.png");
      this.panelVideo = ImageLoader.getImage("res/GUI/gameState/tipPanelVideo.png");
      this.mask = new Rectangle(0.0F, 0.0F, this.panelSmall.getWidth(), this.panelSmall.getHeight());
      this.masks.add(this.mask);
      this.tipPanelVideoMask = new Rectangle(0.0F, 0.0F, this.panelVideo.getWidth(), this.panelVideo.getHeight());
      this.masks.add(this.tipPanelVideoMask);
      this.ok = new GUIButtonGenericWood("ok", Text.getText("sharedTipsPanel.button.ok"));
      this.guiButtons.add(this.ok);
   }

   public void render(boolean debug) throws SlickException {
      this.x = ScaleControl.getInterfaceWidth() / 2 - this.panelLarge.getWidth() / 2;
      this.y = ScaleControl.getInterfaceHeight() / 2 - this.panelLarge.getHeight() / 2 - 50;
      if (this.activeTip != null) {
         String tipContent = Text.wrapString(this.activeTip.getTipBody(), Text.FontType.BODY, 2, 500);
         String tipTitle = this.activeTip.getTipHeader();
         int x = 0;
         int y = 0;
         x = ScaleControl.getInterfaceWidth() / 2 - this.panelLarge.getWidth() / 2 - this.panelVideo.getWidth() / 2;
         y = ScaleControl.getInterfaceHeight() / 2 - this.panelLarge.getHeight() / 2;
         if (tipContent.length() < 200) {
            this.mask.setBounds(x + 5, y + 5, this.panelSmall.getWidth() - 10, this.panelSmall.getHeight() - 10);
            this.panelSmall.draw(x, y);
            this.ok.render(this.g, this.mouse, x + this.panelSmall.getWidth() / 2 + 94, y + 127, false, debug);
         } else if (tipContent.length() < 360) {
            this.mask.setBounds(x + 5, y + 5, this.panelMedium.getWidth() - 10, this.panelMedium.getHeight() - 10);
            this.panelMedium.draw(x, y);
            this.ok.render(this.g, this.mouse, x + this.panelSmall.getWidth() / 2 + 94, y + 187, false, debug);
         } else {
            this.mask.setBounds(x + 5, y + 5, this.panelLarge.getWidth() - 10, this.panelLarge.getHeight() - 10);
            this.panelLarge.draw(x, y);
            this.ok.render(this.g, this.mouse, x + this.panelSmall.getWidth() / 2 + 94, y + 247, false, debug);
         }

         this.font.drawString(x + this.panelSmall.getWidth() / 2, y + 16, "$GRE1" + tipTitle, Text.FontType.HEADER, 3, true);
         this.font.drawString(x + this.panelSmall.getWidth() / 2, y + 50, tipContent, Text.FontType.BODY, 2, true);
         Animation video = this.activeTip.getTipVideo();
         if (video != null) {
            this.tipPanelVideoMask.setX(x + this.panelSmall.getWidth());
            this.tipPanelVideoMask.setY(y);
            video.render(x + this.panelSmall.getWidth() + 17, y + 17);
            this.panelVideo.draw(x + this.panelSmall.getWidth(), y);
         }
      }
   }

   @Override
   public void update() throws SlickException {
      if (this.activeTip != null) {
         Animation video = this.activeTip.getTipVideo();
         if (video != null) {
            video.update();
         }
      }
   }

   public void setTip(TipBase t) {
      this.activeTip = t;
   }

   public TipBase getActiveTip() {
      return this.activeTip;
   }

   public void clearActiveTip() {
      this.activeTip = null;
   }
}
