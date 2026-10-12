package org.newdawn.slick;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.CursorLoader;
import org.newdawn.slick.opengl.ImageData;
import org.newdawn.slick.opengl.ImageIOImageData;
import org.newdawn.slick.opengl.InternalTextureLoader;
import org.newdawn.slick.opengl.LoadableImageData;
import org.newdawn.slick.opengl.TGAImageData;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;

public class AppGameContainer extends GameContainer {
   protected DisplayMode originalDisplayMode;
   protected DisplayMode targetDisplayMode;
   protected boolean updateOnlyOnVisible = true;
   protected boolean alphaSupport = false;

   static {
      AccessController.doPrivileged(new PrivilegedAction() {
         @Override
         public Object run() {
            try {
               Display.getDisplayMode();
            } catch (Exception var2) {
               Log.error(var2);
            }

            return null;
         }
      });
   }

   public AppGameContainer(Game game) throws SlickException {
      this(game, 640, 480);
   }

   public AppGameContainer(Game game, boolean fullscreen) throws SlickException {
      super(game);
      this.originalDisplayMode = Display.getDesktopDisplayMode();
      this.setDisplayMode(this.originalDisplayMode.getWidth(), this.originalDisplayMode.getHeight(), fullscreen, true);
   }

   public AppGameContainer(Game game, int width, int height) throws SlickException {
      super(game);
      this.originalDisplayMode = Display.getDesktopDisplayMode();
      this.setDisplayMode(width, height, false, true);
   }

   public boolean supportsAlphaInBackBuffer() {
      return this.alphaSupport;
   }

   public void setTitle(String title) {
      Display.setTitle(title);
   }

   public void setDisplayMode(int width, int height, boolean fullscreen) throws SlickException {
      this.setDisplayMode(width, height, fullscreen, false);
   }

   public void setDisplayMode(int width, int height, boolean fullscreen, boolean reinit) throws SlickException {
      if (this.width != width || this.height != height || this.isFullscreen() != fullscreen) {
         try {
            this.targetDisplayMode = null;
            if (!fullscreen) {
               this.targetDisplayMode = new DisplayMode(width, height);
            } else {
               DisplayMode[] modes = Display.getAvailableDisplayModes();
               int freq = 0;

               for (int i = 0; i < modes.length; i++) {
                  DisplayMode current = modes[i];
                  if (current.getWidth() == width && current.getHeight() == height) {
                     if ((this.targetDisplayMode == null || current.getFrequency() >= freq)
                        && (this.targetDisplayMode == null || current.getBitsPerPixel() > this.targetDisplayMode.getBitsPerPixel())) {
                        this.targetDisplayMode = current;
                        freq = this.targetDisplayMode.getFrequency();
                     }

                     if (current.getBitsPerPixel() == this.originalDisplayMode.getBitsPerPixel()
                        && current.getFrequency() == this.originalDisplayMode.getFrequency()) {
                        this.targetDisplayMode = current;
                        break;
                     }
                  }
               }
            }

            if (this.targetDisplayMode == null) {
               throw new SlickException("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
            }

            this.width = width;
            this.height = height;
            if (reinit) {
               Display.setDisplayMode(this.targetDisplayMode);
               Display.setFullscreen(fullscreen);
            }

            if (Display.isCreated()) {
               this.initGL();
               this.enterOrtho();
            }

            Display.setResizable(false);
            Display.setResizable(true);
            if (this.targetDisplayMode.getBitsPerPixel() == 16) {
               InternalTextureLoader.get().set16BitMode();
            }
         } catch (LWJGLException var9) {
            throw new SlickException("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen, var9);
         }

         this.getDelta();
      }
   }

   @Override
   public boolean isFullscreen() {
      return Display.isFullscreen();
   }

   @Override
   public void setFullscreen(boolean fullscreen) throws SlickException {
      if (this.isFullscreen() != fullscreen) {
         if (!fullscreen) {
            try {
               Display.setFullscreen(fullscreen);
            } catch (LWJGLException var3) {
               throw new SlickException("Unable to set fullscreen=" + fullscreen, var3);
            }
         } else {
            this.setDisplayMode(this.width, this.height, fullscreen);
         }

         this.getDelta();
      }
   }

   @Override
   public void setMouseCursor(String ref, int hotSpotX, int hotSpotY) throws SlickException {
      try {
         Cursor cursor = CursorLoader.get().getCursor(ref, hotSpotX, hotSpotY);
         Mouse.setNativeCursor(cursor);
      } catch (Throwable var5) {
         Log.error("Failed to load and apply cursor.", var5);
         throw new SlickException("Failed to set mouse cursor", var5);
      }
   }

   @Override
   public void setMouseCursor(ImageData data, int hotSpotX, int hotSpotY) throws SlickException {
      try {
         Cursor cursor = CursorLoader.get().getCursor(data, hotSpotX, hotSpotY);
         Mouse.setNativeCursor(cursor);
      } catch (Throwable var5) {
         Log.error("Failed to load and apply cursor.", var5);
         throw new SlickException("Failed to set mouse cursor", var5);
      }
   }

   @Override
   public void setMouseCursor(Cursor cursor, int hotSpotX, int hotSpotY) throws SlickException {
      try {
         Mouse.setNativeCursor(cursor);
      } catch (Throwable var5) {
         Log.error("Failed to load and apply cursor.", var5);
         throw new SlickException("Failed to set mouse cursor", var5);
      }
   }

   private int get2Fold(int fold) {
      int ret = 2;

      while (ret < fold) {
         ret *= 2;
      }

      return ret;
   }

   @Override
   public void setMouseCursor(Image image, int hotSpotX, int hotSpotY) throws SlickException {
      try {
         Image temp = new Image(this.get2Fold(image.getWidth()), this.get2Fold(image.getHeight()));
         Graphics g = temp.getGraphics();
         ByteBuffer buffer = BufferUtils.createByteBuffer(temp.getWidth() * temp.getHeight() * 4);
         g.drawImage(image.getFlippedCopy(false, true), 0.0F, 0.0F);
         g.flush();
         g.getArea(0, 0, temp.getWidth(), temp.getHeight(), buffer);
         Cursor cursor = CursorLoader.get().getCursor(buffer, hotSpotX, hotSpotY, temp.getWidth(), image.getHeight());
         Mouse.setNativeCursor(cursor);
      } catch (Throwable var8) {
         Log.error("Failed to load and apply cursor.", var8);
         throw new SlickException("Failed to set mouse cursor", var8);
      }
   }

   @Override
   public void reinit() throws SlickException {
      InternalTextureLoader.get().clear();
      SoundStore.get().clear();
      this.initSystem();
      this.enterOrtho();

      try {
         this.game.init(this);
      } catch (SlickException var2) {
         Log.error(var2);
         this.running = false;
      }
   }

   private void tryCreateDisplay(PixelFormat format) throws LWJGLException {
      if (SHARED_DRAWABLE == null) {
         Display.create(format);
      } else {
         Display.create(format, SHARED_DRAWABLE);
      }
   }

   public void start() throws SlickException {
      try {
         this.setup();
         this.getDelta();

         while (this.running()) {
            this.gameLoop();
         }
      } finally {
         this.destroy();
      }

      if (this.forceExit) {
         System.exit(0);
      }
   }

   protected void setup() throws SlickException {
      if (this.targetDisplayMode == null) {
         this.setDisplayMode(640, 480, false);
      }

      Display.setTitle(this.game.getTitle());
      Log.info("LWJGL Version: " + Sys.getVersion());
      Log.info("OriginalDisplayMode: " + this.originalDisplayMode);
      Log.info("TargetDisplayMode: " + this.targetDisplayMode);
      AccessController.doPrivileged(new PrivilegedAction() {
         @Override
         public Object run() {
            try {
               PixelFormat format = new PixelFormat(8, 8, AppGameContainer.stencil ? 8 : 0, AppGameContainer.this.samples);
               AppGameContainer.this.tryCreateDisplay(format);
               AppGameContainer.this.supportsMultiSample = true;
            } catch (Exception var6) {
               Display.destroy();

               try {
                  PixelFormat formatx = new PixelFormat(8, 8, AppGameContainer.stencil ? 8 : 0);
                  AppGameContainer.this.tryCreateDisplay(formatx);
                  AppGameContainer.this.alphaSupport = false;
               } catch (Exception var5) {
                  Display.destroy();

                  try {
                     AppGameContainer.this.tryCreateDisplay(new PixelFormat());
                  } catch (Exception var4) {
                     Log.error(var4);
                  }
               }
            }

            return null;
         }
      });
      if (!Display.isCreated()) {
         throw new SlickException("Failed to initialise the LWJGL display");
      } else {
         this.initSystem();
         this.enterOrtho();

         try {
            this.getInput().initControllers();
         } catch (SlickException var3) {
            Log.info("Controllers not available");
         } catch (Throwable var4) {
            Log.info("Controllers not available");
         }

         try {
            this.game.init(this);
         } catch (SlickException var2) {
            Log.error(var2);
            this.running = false;
         }
      }
   }

   protected void gameLoop() throws SlickException {
      int delta = this.getDelta();
      if (!Display.isVisible() && this.updateOnlyOnVisible) {
         try {
            Thread.sleep(100L);
         } catch (Exception var4) {
         }
      } else {
         try {
            this.updateAndRender(delta);
         } catch (SlickException var3) {
            Log.error(var3);
            this.running = false;
            return;
         }
      }

      this.updateFPS();
      Display.update();
      if (Display.isCloseRequested() && this.game.closeRequested()) {
         this.running = false;
      }
   }

   @Override
   public void setUpdateOnlyWhenVisible(boolean updateOnlyWhenVisible) {
      this.updateOnlyOnVisible = updateOnlyWhenVisible;
   }

   @Override
   public boolean isUpdatingOnlyWhenVisible() {
      return this.updateOnlyOnVisible;
   }

   @Override
   public void setIcon(String ref) throws SlickException {
      this.setIcons(new String[]{ref});
   }

   @Override
   public void setMouseGrabbed(boolean grabbed) {
      Mouse.setGrabbed(grabbed);
   }

   @Override
   public boolean isMouseGrabbed() {
      return Mouse.isGrabbed();
   }

   @Override
   public boolean hasFocus() {
      return Display.isActive();
   }

   @Override
   public int getScreenHeight() {
      return this.originalDisplayMode.getHeight();
   }

   @Override
   public int getScreenWidth() {
      return this.originalDisplayMode.getWidth();
   }

   public void destroy() {
      Display.destroy();
      AL.destroy();
   }

   @Override
   public void setIcons(String[] refs) throws SlickException {
      ByteBuffer[] bufs = new ByteBuffer[refs.length];

      for (int i = 0; i < refs.length; i++) {
         boolean flip = true;
         LoadableImageData data;
         if (refs[i].endsWith(".tga")) {
            data = new TGAImageData();
         } else {
            flip = false;
            data = new ImageIOImageData();
         }

         try {
            bufs[i] = data.loadImage(ResourceLoader.getResourceAsStream(refs[i]), flip, false, null);
         } catch (Exception var7) {
            Log.error(var7);
            throw new SlickException("Failed to set the icon");
         }
      }

      Display.setIcon(bufs);
   }

   @Override
   public void setDefaultMouseCursor() {
      try {
         Mouse.setNativeCursor(null);
      } catch (LWJGLException var2) {
         Log.error("Failed to reset mouse cursor", var2);
      }
   }

   private class NullOutputStream extends OutputStream {
      @Override
      public void write(int b) throws IOException {
      }
   }
}
