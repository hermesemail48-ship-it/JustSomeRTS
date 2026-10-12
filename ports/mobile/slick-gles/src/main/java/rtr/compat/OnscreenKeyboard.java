package rtr.compat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.opengl.renderer.GdxGL;
import org.newdawn.slick.opengl.renderer.Renderer;
import rtr.compat.lwjgl.opengl.Display;

/**
 * Shows the phone's keyboard while a Slick TextField has focus, and scrolls the view so that field stays above it.
 * Typed text reaches the field through {@link GdxInputBridge} like a hardware keyboard.
 */
public class OnscreenKeyboard {
   /** Used until the platform reports the keyboard height (landscape phone keyboards cover about this much). */
   private static final float DEFAULT_HEIGHT_FRACTION = 0.55F;
   private static final float MARGIN = 12.0F;
   private static float fieldBottom = -1.0F;
   private static long fieldSeenFrame;
   private static long frame;
   private boolean shown;
   private int reportedHeight = -1;

   public OnscreenKeyboard() {
      Gdx.input.setKeyboardHeightObserver(new Input.KeyboardHeightObserver() {
         @Override
         public void onKeyboardHeightChanged(int height) {
            OnscreenKeyboard.this.reportedHeight = height;
         }
      });
   }

   /** Called by TextField.render for the focused field, with its coordinates in the current transform. */
   public static void focusedFieldDrawn(float x, float y, float height) {
      fieldBottom = Renderer.gdx().screenY(x, y + height);
      fieldSeenFrame = frame;
   }

   /** Call once per frame before the game renders (the main screen must be the render target). */
   public void update() {
      frame++;
      AbstractComponent focus = AbstractComponent.getCurrentFocus();
      boolean want = focus instanceof TextField && focus.hasFocus();
      if (want != this.shown) {
         this.shown = want;
         Gdx.input.setOnscreenKeyboardVisible(want, Input.OnscreenKeyboardType.Default);
      }

      GdxGL gl = Renderer.gdx();
      float target = 0.0F;
      if (this.shown && fieldBottom >= 0.0F && frame - fieldSeenFrame < 3L) {
         float visibleBottom = Display.getHeight() - this.keyboardHeight();
         target = Math.max(0.0F, fieldBottom + MARGIN - visibleBottom);
      }

      float current = gl.getViewOffsetY();
      float next = Math.abs(target - current) < 1.0F ? target : current + (target - current) * 0.35F;
      gl.setViewOffsetY(next);
   }

   /** Keyboard height in logical game pixels. */
   private float keyboardHeight() {
      if (this.reportedHeight <= 0) {
         return Display.getHeight() * DEFAULT_HEIGHT_FRACTION;
      }

      // Backends report either window points or back-buffer pixels; convert whichever fits.
      float units = this.reportedHeight > Gdx.graphics.getHeight() ? Gdx.graphics.getBackBufferHeight() : Gdx.graphics.getHeight();
      return Math.min(Display.getHeight() * 0.8F, this.reportedHeight * Display.getHeight() / units);
   }
}
