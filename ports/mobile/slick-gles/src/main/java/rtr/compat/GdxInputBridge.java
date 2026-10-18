package rtr.compat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import rtr.compat.lwjgl.input.Keyboard;
import rtr.compat.lwjgl.input.Mouse;
import rtr.compat.lwjgl.opengl.Display;

/**
 * Feeds libGDX input into the emulated LWJGL Keyboard/Mouse queues that Slick's Input polls.
 * Coordinates are converted to the game's logical screen with LWJGL's bottom-left origin.
 * Touch input is translated by a {@link TouchTranslator} when one is installed (see the game module).
 */
public class GdxInputBridge extends InputAdapter {
   private TouchTranslator touch;
   private PerfOverlay perfOverlay;
   private int fingersDown;

   public void setPerfOverlay(PerfOverlay overlay) {
      this.perfOverlay = overlay;
   }

   public interface TouchTranslator {
      boolean touchDown(int x, int y, int pointer, GdxInputBridge bridge);

      boolean touchDragged(int x, int y, int pointer, GdxInputBridge bridge);

      boolean touchUp(int x, int y, int pointer, GdxInputBridge bridge);

      /** Called every frame so the translator can fire long-press timers. */
      void update(GdxInputBridge bridge);
   }

   public void setTouchTranslator(TouchTranslator translator) {
      this.touch = translator;
   }

   public TouchTranslator getTouchTranslator() {
      return this.touch;
   }

   /** Window pixels (top-left origin) to the game's logical x. */
   public int toGameX(int screenX) {
      return (int)((float)screenX * Display.getWidth() / Math.max(1, Gdx.graphics.getWidth()));
   }

   /** Window pixels (top-left origin) to the game's logical y, in LWJGL's bottom-up convention. */
   public int toLwjglY(int screenY) {
      int y = (int)((float)screenY * Display.getHeight() / Math.max(1, Gdx.graphics.getHeight())
         + org.newdawn.slick.opengl.renderer.Renderer.gdx().getViewOffsetY());
      return Display.getHeight() - 1 - y;
   }

   public void mouseMove(int gameX, int lwjglY) {
      Mouse.push(-1, false, gameX, lwjglY, 0);
   }

   public void mouseButton(int button, boolean down, int gameX, int lwjglY) {
      Mouse.push(button, down, gameX, lwjglY, 0);
   }

   public void mouseWheel(int amount) {
      Mouse.push(-1, false, Mouse.getX(), Mouse.getY(), amount);
   }

   public void key(int lwjglKey, char c, boolean down) {
      Keyboard.push(lwjglKey, c, down, false);
   }

   private boolean isTouchEvent() {
      return this.touch != null;
   }

   @Override
   public boolean keyDown(int keycode) {
      if (keycode == com.badlogic.gdx.Input.Keys.F3 && this.perfOverlay != null) {
         this.perfOverlay.toggle();
         return true;
      }

      int k = GdxKeyMap.toLwjgl(keycode);
      Keyboard.push(k, (char)Keyboard.CHAR_NONE, true, false);
      return true;
   }

   @Override
   public boolean keyUp(int keycode) {
      int k = GdxKeyMap.toLwjgl(keycode);
      Keyboard.push(k, (char)Keyboard.CHAR_NONE, false, false);
      return true;
   }

   @Override
   public boolean keyTyped(char character) {
      if (!Keyboard.attachCharacter(character)) {
         Keyboard.push(Keyboard.KEY_NONE, character, true, false);
         Keyboard.push(Keyboard.KEY_NONE, character, false, false);
      }

      return true;
   }

   @Override
   public boolean touchDown(int screenX, int screenY, int pointer, int button) {
      this.fingersDown++;
      if (this.fingersDown == 3 && this.perfOverlay != null) {
         this.perfOverlay.toggle();
      }

      if (this.isTouchEvent()) {
         return this.touch.touchDown(screenX, screenY, pointer, this);
      }

      int x = this.toGameX(screenX);
      int y = this.toLwjglY(screenY);
      this.mouseMove(x, y);
      this.mouseButton(button, true, x, y);
      return true;
   }

   @Override
   public boolean touchUp(int screenX, int screenY, int pointer, int button) {
      this.fingersDown = Math.max(0, this.fingersDown - 1);
      if (this.isTouchEvent()) {
         return this.touch.touchUp(screenX, screenY, pointer, this);
      }

      this.mouseButton(button, false, this.toGameX(screenX), this.toLwjglY(screenY));
      return true;
   }

   @Override
   public boolean touchDragged(int screenX, int screenY, int pointer) {
      if (this.isTouchEvent()) {
         return this.touch.touchDragged(screenX, screenY, pointer, this);
      }

      this.mouseMove(this.toGameX(screenX), this.toLwjglY(screenY));
      return true;
   }

   @Override
   public boolean mouseMoved(int screenX, int screenY) {
      this.mouseMove(this.toGameX(screenX), this.toLwjglY(screenY));
      return true;
   }

   @Override
   public boolean scrolled(float amountX, float amountY) {
      this.mouseWheel(Math.round(-amountY * 120.0F));
      return true;
   }

   public void update() {
      if (this.touch != null) {
         this.touch.update(this);
      }
   }
}
