package org.newdawn.slick;

import org.newdawn.slick.opengl.ImageData;
import org.newdawn.slick.util.Log;
import rtr.compat.lwjgl.input.Cursor;
import rtr.compat.lwjgl.input.Mouse;
import rtr.compat.lwjgl.opengl.Display;

/**
 * Slick's AppGameContainer driven by libGDX instead of owning the LWJGL loop: {@link #start()} performs Slick's
 * setup on the GL thread and returns; {@link rtr.compat.GdxSlickApp} then calls {@link #frame()} once per render.
 */
public class AppGameContainer extends GameContainer {
   private static AppGameContainer current;
   private boolean started;
   private boolean mouseGrabbed;

   public static AppGameContainer current() {
      return current;
   }

   public AppGameContainer(Game game) throws SlickException {
      super(game);
      this.width = Display.getWidth();
      this.height = Display.getHeight();
   }

   public AppGameContainer(Game game, int width, int height, boolean fullscreen) throws SlickException {
      this(game);
   }

   public void setDisplayMode(int width, int height, boolean fullscreen) throws SlickException {
      this.setDisplayMode(width, height, fullscreen, true);
   }

   /** The device decides the size; this re-syncs the container with it, like Slick did after a mode change. */
   public void setDisplayMode(int width, int height, boolean fullscreen, boolean reinit) throws SlickException {
      this.width = Display.getWidth();
      this.height = Display.getHeight();
      if (this.started) {
         this.initGL();
         this.enterOrtho();
      }
   }

   public boolean isFullscreen() {
      return true;
   }

   public void setFullscreen(boolean fullscreen) throws SlickException {
   }

   public void setTitle(String title) {
      Display.setTitle(title);
   }

   @Override
   public void setMouseCursor(String ref, int hotSpotX, int hotSpotY) throws SlickException {
   }

   @Override
   public void setMouseCursor(ImageData data, int hotSpotX, int hotSpotY) throws SlickException {
   }

   @Override
   public void setMouseCursor(Cursor cursor, int hotSpotX, int hotSpotY) throws SlickException {
      try {
         Mouse.setNativeCursor(cursor);
      } catch (rtr.compat.lwjgl.LWJGLException var5) {
         throw new SlickException("Failed to set mouse cursor", var5);
      }
   }

   @Override
   public void setMouseCursor(Image image, int hotSpotX, int hotSpotY) throws SlickException {
   }

   @Override
   public void setDefaultMouseCursor() {
      try {
         Mouse.setNativeCursor(null);
      } catch (rtr.compat.lwjgl.LWJGLException var2) {
      }
   }

   public void reinit() throws SlickException {
      this.initGL();
      this.enterOrtho();
   }

   public void start() throws SlickException {
      current = this;
      this.setup();
      this.started = true;
   }

   protected void setup() throws SlickException {
      Display.update();
      this.width = Display.getWidth();
      this.height = Display.getHeight();
      Log.info("Slick/libGDX display " + this.width + "x" + this.height);
      this.initSystem();
      this.enterOrtho();

      try {
         this.getInput().initControllers();
      } catch (SlickException var2) {
         Log.info("Controllers not available");
      }

      this.game.init(this);
      this.getDelta();
   }

   /** One iteration of Slick's game loop. */
   public void frame() throws SlickException {
      int delta = this.getDelta();
      this.updateAndRender(delta);
      this.updateFPS();
   }

   @Override
   public void setIcon(String ref) throws SlickException {
   }

   @Override
   public void setIcons(String[] refs) throws SlickException {
   }

   @Override
   public void setMouseGrabbed(boolean grabbed) {
      this.mouseGrabbed = grabbed;
      Mouse.setGrabbed(grabbed);
   }

   @Override
   public boolean isMouseGrabbed() {
      return this.mouseGrabbed;
   }

   @Override
   public boolean hasFocus() {
      return Display.isActive();
   }

   @Override
   public int getScreenWidth() {
      return Display.getWidth();
   }

   @Override
   public int getScreenHeight() {
      return Display.getHeight();
   }

   public void setUpdateOnlyWhenVisible(boolean updateOnlyWhenVisible) {
   }

   public boolean isUpdatingOnlyWhenVisible() {
      return false;
   }

   @Override
   public void exit() {
      this.running = false;
      com.badlogic.gdx.Gdx.app.exit();
   }

   public void destroy() {
   }
}
