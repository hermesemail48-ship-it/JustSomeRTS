package org.newdawn.slick;

import java.io.IOException;
import java.util.Properties;
import rtr.compat.lwjgl.LWJGLException;
import rtr.compat.lwjgl.Sys;
import rtr.compat.lwjgl.input.Cursor;
import rtr.compat.lwjgl.opengl.Display;
import rtr.compat.lwjgl.opengl.Drawable;
import rtr.compat.lwjgl.opengl.Pbuffer;
import rtr.compat.lwjgl.opengl.PixelFormat;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.ImageData;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;

public abstract class GameContainer implements GUIContext {
   protected static SGL GL = Renderer.get();
   protected static Drawable SHARED_DRAWABLE;
   protected long lastFrame;
   protected long lastFPS;
   protected int recordedFPS;
   protected int fps;
   protected boolean running = true;
   protected int width;
   protected int height;
   protected Game game;
   private Font defaultFont;
   private Graphics graphics;
   protected Input input;
   protected int targetFPS = -1;
   private boolean showFPS = true;
   protected long minimumLogicInterval = 1L;
   protected long storedDelta;
   protected long maximumLogicInterval = 0L;
   protected Game lastGame;
   protected boolean clearEachFrame = true;
   protected boolean paused;
   protected boolean forceExit = true;
   protected boolean vsync;
   protected boolean smoothDeltas;
   protected int samples;
   protected boolean supportsMultiSample;
   protected boolean alwaysRender;
   protected static boolean stencil;

   protected GameContainer(Game game) {
      this.game = game;
      this.lastFrame = this.getTime();
      getBuildVersion();
      Log.checkVerboseLogSetting();
   }

   public static void enableStencil() {
      stencil = true;
   }

   public void setDefaultFont(Font font) {
      if (font != null) {
         this.defaultFont = font;
      } else {
         Log.warn("Please provide a non null font");
      }
   }

   public void setMultiSample(int samples) {
      this.samples = samples;
   }

   public boolean supportsMultiSample() {
      return this.supportsMultiSample;
   }

   public int getSamples() {
      return this.samples;
   }

   public void setForceExit(boolean forceExit) {
      this.forceExit = forceExit;
   }

   public void setSmoothDeltas(boolean smoothDeltas) {
      this.smoothDeltas = smoothDeltas;
   }

   public boolean isFullscreen() {
      return false;
   }

   public float getAspectRatio() {
      return this.getWidth() / this.getHeight();
   }

   public void setFullscreen(boolean fullscreen) throws SlickException {
   }

   public static void enableSharedContext() throws SlickException {
      try {
         SHARED_DRAWABLE = new Pbuffer(64, 64, new PixelFormat(8, 0, 0), null);
      } catch (LWJGLException var1) {
         throw new SlickException("Unable to create the pbuffer used for shard context, buffers not supported", var1);
      }
   }

   public static Drawable getSharedContext() {
      return SHARED_DRAWABLE;
   }

   public void setClearEachFrame(boolean clear) {
      this.clearEachFrame = clear;
   }

   public void reinit() throws SlickException {
   }

   public void pause() {
      this.setPaused(true);
   }

   public void resume() {
      this.setPaused(false);
   }

   public boolean isPaused() {
      return this.paused;
   }

   public void setPaused(boolean paused) {
      this.paused = paused;
   }

   public boolean getAlwaysRender() {
      return this.alwaysRender;
   }

   public void setAlwaysRender(boolean alwaysRender) {
      this.alwaysRender = alwaysRender;
   }

   public static int getBuildVersion() {
      try {
         Properties props = new Properties();
         props.load(ResourceLoader.getResourceAsStream("version"));
         int build = Integer.parseInt(props.getProperty("build"));
         Log.info("Slick Build #" + build);
         return build;
      } catch (Exception var2) {
         Log.error("Unable to determine Slick build number");
         return -1;
      }
   }

   @Override
   public Font getDefaultFont() {
      return this.defaultFont;
   }

   public boolean isSoundOn() {
      return SoundStore.get().soundsOn();
   }

   public boolean isMusicOn() {
      return SoundStore.get().musicOn();
   }

   public void setMusicOn(boolean on) {
      SoundStore.get().setMusicOn(on);
   }

   public void setSoundOn(boolean on) {
      SoundStore.get().setSoundsOn(on);
   }

   public float getMusicVolume() {
      return SoundStore.get().getMusicVolume();
   }

   public float getSoundVolume() {
      return SoundStore.get().getSoundVolume();
   }

   public void setSoundVolume(float volume) {
      SoundStore.get().setSoundVolume(volume);
   }

   public void setMusicVolume(float volume) {
      SoundStore.get().setMusicVolume(volume);
   }

   @Override
   public abstract int getScreenWidth();

   @Override
   public abstract int getScreenHeight();

   @Override
   public int getWidth() {
      return this.width;
   }

   @Override
   public int getHeight() {
      return this.height;
   }

   public abstract void setIcon(String var1) throws SlickException;

   public abstract void setIcons(String[] var1) throws SlickException;

   @Override
   public long getTime() {
      return Sys.getTime() * 1000L / Sys.getTimerResolution();
   }

   public void sleep(int milliseconds) {
      long target = this.getTime() + milliseconds;

      while (this.getTime() < target) {
         try {
            Thread.sleep(1L);
         } catch (Exception var5) {
         }
      }
   }

   @Override
   public abstract void setMouseCursor(String var1, int var2, int var3) throws SlickException;

   @Override
   public abstract void setMouseCursor(ImageData var1, int var2, int var3) throws SlickException;

   public abstract void setMouseCursor(Image var1, int var2, int var3) throws SlickException;

   @Override
   public abstract void setMouseCursor(Cursor var1, int var2, int var3) throws SlickException;

   public void setAnimatedMouseCursor(String ref, int x, int y, int width, int height, int[] cursorDelays) throws SlickException {
      throw new SlickException("Animated cursors are not supported");
   }

   @Override
   public abstract void setDefaultMouseCursor();

   @Override
   public Input getInput() {
      return this.input;
   }

   public int getFPS() {
      return this.recordedFPS;
   }

   public abstract void setMouseGrabbed(boolean var1);

   public abstract boolean isMouseGrabbed();

   protected int getDelta() {
      long time = this.getTime();
      int delta = (int)(time - this.lastFrame);
      this.lastFrame = time;
      return delta;
   }

   protected void updateFPS() {
      if (this.getTime() - this.lastFPS > 1000L) {
         this.lastFPS = this.getTime();
         this.recordedFPS = this.fps;
         this.fps = 0;
      }

      this.fps++;
   }

   public void setMinimumLogicUpdateInterval(int interval) {
      this.minimumLogicInterval = interval;
   }

   public void setMaximumLogicUpdateInterval(int interval) {
      this.maximumLogicInterval = interval;
   }

   /**
    * Simulation rate for the libGDX build. Rise to Ruins advances one fixed step per update call (it ignores delta),
    * so it is designed around 60 updates/s; tying updates to frames made the whole game run in slow motion when the
    * frame rate dropped. With a fixed rate, rendering can run at 30 fps while the simulation keeps its speed.
    * 0 keeps Slick's original one-update-per-frame behaviour.
    */
   public static float fixedTickRate = 0.0F;
   /** Upper bound on catch-up ticks per frame, so a slow device degrades to slow motion instead of freezing. */
   public static int maxTicksPerFrame = 4;
   private double tickAccumulatorMs;

   private void timedUpdate(int delta) throws SlickException {
      long t0 = System.nanoTime();
      this.game.update(this, delta);
      rtr.compat.PerfStats.updateNanos += System.nanoTime() - t0;
      rtr.compat.PerfStats.updatesPerFrame++;
   }

   protected void updateAndRender(int delta) throws SlickException {
      if (this.smoothDeltas && this.getFPS() != 0) {
         delta = 1000 / this.getFPS();
      }

      this.input.poll(this.width, this.height);
      Music.poll(delta);
      if (!this.paused && fixedTickRate > 0.0F) {
         double tickMs = 1000.0 / fixedTickRate;
         this.tickAccumulatorMs += delta;
         int ticks = (int)(this.tickAccumulatorMs / tickMs);
         if (ticks > maxTicksPerFrame) {
            ticks = maxTicksPerFrame;
            this.tickAccumulatorMs = 0.0;
         } else {
            this.tickAccumulatorMs -= ticks * tickMs;
         }

         try {
            for (int i = 0; i < ticks; i++) {
               this.timedUpdate((int)Math.round(tickMs));
            }
         } catch (Throwable var7) {
            Log.error(var7);
            throw new SlickException("Game.update() failure - check the game code.");
         }
      } else if (!this.paused) {
         this.storedDelta += delta;
         if (this.storedDelta >= this.minimumLogicInterval) {
            try {
               if (this.maximumLogicInterval != 0L) {
                  long cycles = this.storedDelta / this.maximumLogicInterval;

                  for (int i = 0; i < cycles; i++) {
                     this.timedUpdate((int)this.maximumLogicInterval);
                  }

                  int remainder = (int)(this.storedDelta % this.maximumLogicInterval);
                  if (remainder > this.minimumLogicInterval) {
                     this.timedUpdate((int)(remainder % this.maximumLogicInterval));
                     this.storedDelta = 0L;
                  } else {
                     this.storedDelta = remainder;
                  }
               } else {
                  this.timedUpdate((int)this.storedDelta);
                  this.storedDelta = 0L;
               }
            } catch (Throwable var6) {
               Log.error(var6);
               throw new SlickException("Game.update() failure - check the game code.");
            }
         }
      } else {
         this.timedUpdate(0);
      }

      if (this.hasFocus() || this.getAlwaysRender()) {
         if (this.clearEachFrame) {
            GL.glClear(16640);
         }

         GL.glLoadIdentity();
         this.graphics.resetTransform();
         this.graphics.resetFont();
         this.graphics.resetLineWidth();
         this.graphics.setAntiAlias(false);

         try {
            long t0 = System.nanoTime();
            this.game.render(this, this.graphics);
            rtr.compat.PerfStats.renderNanos += System.nanoTime() - t0;
         } catch (Throwable var5) {
            Log.error(var5);
            throw new SlickException("Game.render() failure - check the game code.");
         }

         this.graphics.resetTransform();
         if (this.showFPS && this.defaultFont != null) {
            this.defaultFont.drawString(10.0F, 10.0F, "FPS: " + this.recordedFPS);
         }

         GL.flush();
      }

      if (this.targetFPS != -1) {
         Display.sync(this.targetFPS);
      }
   }

   public void setUpdateOnlyWhenVisible(boolean updateOnlyWhenVisible) {
   }

   public boolean isUpdatingOnlyWhenVisible() {
      return true;
   }

   protected void initGL() {
      Log.info("Starting display " + this.width + "x" + this.height);
      GL.initDisplay(this.width, this.height);
      if (this.input == null) {
         this.input = new Input(this.height);
      }

      this.input.init(this.height);
      if (this.game instanceof InputListener) {
         this.input.removeListener((InputListener)this.game);
         this.input.addListener((InputListener)this.game);
      }

      if (this.graphics != null) {
         this.graphics.setDimensions(this.getWidth(), this.getHeight());
      }

      this.lastGame = this.game;
   }

   protected void initSystem() throws SlickException {
      this.initGL();
      this.setMusicVolume(1.0F);
      this.setSoundVolume(1.0F);
      this.graphics = new Graphics(this.width, this.height);
      this.defaultFont = this.graphics.getFont();
   }

   protected void enterOrtho() {
      this.enterOrtho(this.width, this.height);
   }

   public void setShowFPS(boolean show) {
      this.showFPS = show;
   }

   public boolean isShowingFPS() {
      return this.showFPS;
   }

   public void setTargetFrameRate(int fps) {
      this.targetFPS = fps;
   }

   public void setVSync(boolean vsync) {
      this.vsync = vsync;
      Display.setVSyncEnabled(vsync);
   }

   public boolean isVSyncRequested() {
      return this.vsync;
   }

   protected boolean running() {
      return this.running;
   }

   public void setVerbose(boolean verbose) {
      Log.setVerbose(verbose);
   }

   public void exit() {
      this.running = false;
   }

   public abstract boolean hasFocus();

   public Graphics getGraphics() {
      return this.graphics;
   }

   protected void enterOrtho(int xsize, int ysize) {
      GL.enterOrtho(xsize, ysize);
   }
}
