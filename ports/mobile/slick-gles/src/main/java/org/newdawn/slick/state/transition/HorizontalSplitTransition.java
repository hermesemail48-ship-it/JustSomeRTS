package org.newdawn.slick.state.transition;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public class HorizontalSplitTransition implements Transition {
   protected static SGL GL = Renderer.get();
   private GameState prev;
   private float offset;
   private boolean finish;
   private Color background;

   public HorizontalSplitTransition() {
   }

   public HorizontalSplitTransition(Color background) {
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
      g.translate(-this.offset, 0.0F);
      g.setClip((int)(-this.offset), 0, container.getWidth() / 2, container.getHeight());
      if (this.background != null) {
         Color c = g.getColor();
         g.setColor(this.background);
         g.fillRect(0.0F, 0.0F, container.getWidth(), container.getHeight());
         g.setColor(c);
      }

      GL.glPushMatrix();
      this.prev.render(container, game, g);
      GL.glPopMatrix();
      g.clearClip();
      g.translate(this.offset * 2.0F, 0.0F);
      g.setClip((int)(container.getWidth() / 2 + this.offset), 0, container.getWidth() / 2, container.getHeight());
      if (this.background != null) {
         Color c = g.getColor();
         g.setColor(this.background);
         g.fillRect(0.0F, 0.0F, container.getWidth(), container.getHeight());
         g.setColor(c);
      }

      GL.glPushMatrix();
      this.prev.render(container, game, g);
      GL.glPopMatrix();
      g.clearClip();
      g.translate(-this.offset, 0.0F);
   }

   @Override
   public void preRender(StateBasedGame game, GameContainer container, Graphics g) throws SlickException {
   }

   @Override
   public void update(StateBasedGame game, GameContainer container, int delta) throws SlickException {
      this.offset += delta * 1.0F;
      if (this.offset > container.getWidth() / 2) {
         this.finish = true;
      }
   }
}
