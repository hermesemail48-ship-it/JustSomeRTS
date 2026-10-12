package rtr.compat.lwjgl.opengl;

import com.badlogic.gdx.Gdx;
import rtr.compat.lwjgl.LWJGLException;

/**
 * LWJGL 2 Display emulation over libGDX. The game's "screen" is the libGDX back buffer divided by
 * {@link #getRenderScale()}, so high-DPI phones can render at a lower logical resolution.
 */
public final class Display {
   private static float renderScale = 1.0F;
   private static int lastWidth = -1;
   private static int lastHeight = -1;
   private static boolean resizedFlag;
   private static boolean active = true;
   private static String title = "";

   public static void setRenderScale(float scale) {
      renderScale = scale;
   }

   public static float getRenderScale() {
      return renderScale;
   }

   public static int getWidth() {
      return Math.max(1, Math.round(Gdx.graphics.getBackBufferWidth() / renderScale));
   }

   public static int getHeight() {
      return Math.max(1, Math.round(Gdx.graphics.getBackBufferHeight() / renderScale));
   }

   /** Called once per frame by the container, before the game updates (like LWJGL's Display.update()). */
   public static void update() {
      int w = getWidth();
      int h = getHeight();
      resizedFlag = lastWidth != -1 && (w != lastWidth || h != lastHeight);
      lastWidth = w;
      lastHeight = h;
   }

   /** True for the whole frame after a size change, as in LWJGL. */
   public static boolean wasResized() {
      return resizedFlag;
   }

   public static void setActive(boolean a) {
      active = a;
   }

   public static boolean isActive() {
      return active;
   }

   public static boolean isVisible() {
      return active;
   }

   public static boolean isCreated() {
      return Gdx.graphics != null;
   }

   public static boolean isCloseRequested() {
      return false;
   }

   public static boolean isFullscreen() {
      return true;
   }

   public static void setFullscreen(boolean fullscreen) throws LWJGLException {
   }

   public static void setResizable(boolean resizable) {
   }

   public static boolean isResizable() {
      return true;
   }

   public static void setLocation(int x, int y) {
   }

   public static void setTitle(String newTitle) {
      title = newTitle;
   }

   public static String getTitle() {
      return title;
   }

   public static void setVSyncEnabled(boolean sync) {
      if (Gdx.graphics != null) {
         Gdx.graphics.setVSync(sync);
      }
   }

   /** Frame pacing is handled by libGDX. */
   public static void sync(int fps) {
   }

   public static DisplayMode getDisplayMode() {
      return getDesktopDisplayMode();
   }

   public static DisplayMode getDesktopDisplayMode() {
      return new DisplayMode(getWidth(), getHeight());
   }

   public static DisplayMode[] getAvailableDisplayModes() throws LWJGLException {
      return new DisplayMode[]{getDesktopDisplayMode()};
   }

   public static void setDisplayMode(DisplayMode mode) throws LWJGLException {
   }

   public static String getAdapter() {
      return Gdx.gl == null ? "unknown" : Gdx.gl.glGetString(0x1F01);
   }

   public static String getVersion() {
      return Gdx.gl == null ? "unknown" : Gdx.gl.glGetString(0x1F02);
   }
}
