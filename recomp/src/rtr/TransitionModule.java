package rtr;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.system.ScaleControl;

public class TransitionModule extends ModuleBase {
   private static final int FADE_SPEED = 10;
   private GameContainer gc;
   private StateBasedGame sbg;
   private Color fade = new Color(0, 0, 0, 255);
   private boolean fadeIn = false;
   private boolean fadeOut = false;
   private boolean fadeOutComplete = false;
   private int fadeAmount = 0;
   private boolean controlLockout = false;
   private StateBase state;
   protected Image loadingLogo;
   protected Image loadingPillar;
   private Input input;

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      this.loadingLogo = ImageLoader.getImage("res/GUI/mainMenu/logo.png");
      this.loadingPillar = ImageLoader.getImage("res/GUI/mainMenu/pillar.png");
      this.gc = StateBase.getGC();
      this.sbg = StateBase.getSBG();
      this.input = StateBase.getInput();
   }

   @Override
   protected void newMainMenu() throws SlickException {
   }

   @Override
   protected void newPlay() throws SlickException {
   }

   @Override
   protected void loadPlay() throws SlickException {
   }

   @Override
   protected void newMapEditor() throws SlickException {
   }

   @Override
   protected void loadMapEditor() throws SlickException {
   }

   @Override
   protected void newWorldMap() throws SlickException {
   }

   @Override
   protected void loadWorldMap() throws SlickException {
   }

   @Override
   protected void loadRegionalSaveData() throws SlickException {
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
   }

   public void update() {
      if (this.fadeOut) {
         this.controlLockout = true;
         this.fadeAmount += 10;
         this.fade = new Color(0, 0, 0, this.fadeAmount);
      }

      if (this.fadeOut && this.fadeAmount >= 300) {
         this.fadeOut = false;
         this.fadeOutComplete = true;
      }

      if (this.fadeIn && this.fadeAmount <= 0) {
         this.controlLockout = false;
         this.fadeIn = false;
         this.fadeAmount = 0;
      }

      if (this.fadeIn) {
         this.controlLockout = true;
      }

      if (this.fadeOutComplete) {
         this.fadeOutComplete = false;
         this.fadeAmount = 0;
         this.controlLockout = false;
         if (this.state == null) {
            StateBase.getSBG().closeRequested();
         } else {
            this.input.clearControlPressedRecord();
            this.input.clearKeyPressedRecord();
            this.input.clearMousePressedRecord();
            Game.setCS(this.state);
            this.sbg.enterState(this.state.getID());
         }
      }
   }

   public void render(Graphics g) {
      int displayWidth = ScaleControl.getInterfaceWidth();
      int displayHeight = ScaleControl.getInterfaceHeight();
      g.setColor(this.fade);
      g.fillRect(-16.0F, -16.0F, displayWidth + 32, displayHeight + 32);
      if (this.fadeOut) {
         Color imageFade = new Color(255, 255, 255, this.fadeAmount);
         if (this.state != null) {
            this.loadingLogo.drawCentered(ScaleControl.getInterfaceWidth() / 2, ScaleControl.getInterfaceHeight() / 2 - 110, imageFade);
            this.loadingPillar.draw(-96.0F, 0.0F, imageFade);
            this.loadingPillar.draw(ScaleControl.getInterfaceWidth() - this.loadingPillar.getWidth() + 96, 0.0F, imageFade);
         }
      }

      if (this.fadeIn) {
         this.fadeAmount -= 10;
         this.fade = new Color(0, 0, 0, this.fadeAmount);
         Color imageFade = new Color(255, 255, 255, this.fadeAmount);
         this.loadingLogo.drawCentered(ScaleControl.getInterfaceWidth() / 2, ScaleControl.getInterfaceHeight() / 2 - 110, imageFade);
         this.loadingPillar.draw(-96.0F, 0.0F, imageFade);
         this.loadingPillar.draw(ScaleControl.getInterfaceWidth() - this.loadingPillar.getWidth() + 96, 0.0F, imageFade);
      }
   }

   public void startTransition(StateBase s) {
      this.fadeOut = true;
      this.sound.fade();
      this.state = s;
   }

   public boolean getControlLockout() {
      return this.controlLockout;
   }

   public void enter() {
      this.fadeAmount = 300;
      this.fadeIn = true;
   }
}
