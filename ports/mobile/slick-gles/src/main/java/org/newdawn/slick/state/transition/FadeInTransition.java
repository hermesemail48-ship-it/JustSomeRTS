package org.newdawn.slick.state.transition;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public class FadeInTransition implements Transition {
   private Color color;
   private int fadeTime = 500;

   public FadeInTransition() {
      this(Color.black, 500);
   }

   public FadeInTransition(Color color) {
      this(color, 500);
   }

   public FadeInTransition(Color color, int fadeTime) {
      this.color = new Color(color);
      this.color.a = 1.0F;
      this.fadeTime = fadeTime;
   }

   @Override
   public boolean isComplete() {
      return this.color.a <= 0.0F;
   }

   @Override
   public void postRender(StateBasedGame game, GameContainer container, Graphics g) {
      Color old = g.getColor();
      g.setColor(this.color);
      g.fillRect(0.0F, 0.0F, container.getWidth() * 2, container.getHeight() * 2);
      g.setColor(old);
   }

   @Override
   public void update(StateBasedGame game, GameContainer container, int delta) {
      this.color.a = this.color.a - delta * (1.0F / this.fadeTime);
      if (this.color.a < 0.0F) {
         this.color.a = 0.0F;
      }
   }

   @Override
   public void preRender(StateBasedGame game, GameContainer container, Graphics g) {
   }

   @Override
   public void init(GameState firstState, GameState secondState) {
   }
}
