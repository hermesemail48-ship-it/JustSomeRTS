package org.newdawn.slick.state.transition;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public class RotateTransition implements Transition {
   private GameState prev;
   private float ang;
   private boolean finish;
   private float scale = 1.0F;
   private Color background;

   public RotateTransition() {
   }

   public RotateTransition(Color background) {
      this.background = background;
   }

   @Override
   public void init(GameState firstState, GameState secondState) {
      this.prev = secondState;
   }

   @Override
   public boolean isComplete() {
      return this.finish;
   }

   @Override
   public void postRender(StateBasedGame game, GameContainer container, Graphics g) throws SlickException {
      g.translate(container.getWidth() / 2, container.getHeight() / 2);
      g.scale(this.scale, this.scale);
      g.rotate(0.0F, 0.0F, this.ang);
      g.translate(-container.getWidth() / 2, -container.getHeight() / 2);
      if (this.background != null) {
         Color c = g.getColor();
         g.setColor(this.background);
         g.fillRect(0.0F, 0.0F, container.getWidth(), container.getHeight());
         g.setColor(c);
      }

      this.prev.render(container, game, g);
      g.translate(container.getWidth() / 2, container.getHeight() / 2);
      g.rotate(0.0F, 0.0F, -this.ang);
      g.scale(1.0F / this.scale, 1.0F / this.scale);
      g.translate(-container.getWidth() / 2, -container.getHeight() / 2);
   }

   @Override
   public void preRender(StateBasedGame game, GameContainer container, Graphics g) throws SlickException {
   }

   @Override
   public void update(StateBasedGame game, GameContainer container, int delta) throws SlickException {
      this.ang += delta * 0.5F;
      if (this.ang > 500.0F) {
         this.finish = true;
      }

      this.scale -= delta * 0.001F;
      if (this.scale < 0.0F) {
         this.scale = 0.0F;
      }
   }
}
