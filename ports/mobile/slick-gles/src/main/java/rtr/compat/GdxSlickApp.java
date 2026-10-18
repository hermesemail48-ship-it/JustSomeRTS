package rtr.compat;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.renderer.Renderer;
import rtr.compat.lwjgl.input.Keyboard;
import rtr.compat.lwjgl.opengl.Display;

/** libGDX entry point that hosts a Slick game: {@code launcher} must create and start() an AppGameContainer. */
public class GdxSlickApp extends ApplicationAdapter {
   private final Runnable launcher;
   private final GdxInputBridge input = new GdxInputBridge();
   private final String shotPath = System.getProperty("rtr.shot");
   private final long shotAtMs = (long)(Float.parseFloat(System.getProperty("rtr.shotAt", "20")) * 1000.0F);
   private long startMs;
   private ScriptedInput script;
   private String pendingShot;
   private boolean pendingExit;
   private OnscreenKeyboard keyboard;
   private final PerfOverlay perf = new PerfOverlay();
   private Runnable screenOverlay;

   public GdxSlickApp(Runnable launcher) {
      this.launcher = launcher;
   }

   /** Drawn every frame on top of the game, in window units (used for the touch HUD). */
   public void setScreenOverlay(Runnable overlay) {
      this.screenOverlay = overlay;
   }

   public PerfOverlay getPerfOverlay() {
      return this.perf;
   }

   public GdxInputBridge getInput() {
      return this.input;
   }

   @Override
   public void create() {
      this.input.setPerfOverlay(this.perf);
      Gdx.input.setInputProcessor(this.input);
      Gdx.input.setCatchKey(com.badlogic.gdx.Input.Keys.BACK, true);
      Display.update();
      Renderer.gdx().initDisplay(Display.getWidth(), Display.getHeight());
      this.startMs = System.currentTimeMillis();
      if (this.input.getTouchTranslator() != null) {
         this.keyboard = new OnscreenKeyboard();
      }

      this.launcher.run();
      if (System.getProperty("rtr.script") != null) {
         try {
            this.script = new ScriptedInput(System.getProperty("rtr.script"), this);
         } catch (java.io.IOException var2) {
            throw new RuntimeException(var2);
         }
      }

      if (AppGameContainer.current() == null) {
         throw new IllegalStateException("The launcher did not start an AppGameContainer");
      }
   }

   @Override
   public void render() {
      PerfStats.beginFrame();
      Display.update();
      Renderer.gdx().beginScene();
      this.input.update();
      if (this.script != null) {
         this.script.update();
      }

      if (this.keyboard != null) {
         this.keyboard.update();
      }

      AppGameContainer container = AppGameContainer.current();
      if (container != null) {
         try {
            container.frame();
         } catch (SlickException var3) {
            throw new RuntimeException(var3);
         }
      }

      Renderer.gdx().endScene();
      if (this.screenOverlay != null) {
         this.screenOverlay.run();
      }

      this.perf.render();
      PerfStats.endFrame();
      if (this.pendingShot != null) {
         this.saveScreenshot(this.pendingShot);
         this.pendingShot = null;
      }

      if (this.pendingExit) {
         System.exit(0);
      }

      if (this.shotPath != null && System.currentTimeMillis() - this.startMs > this.shotAtMs) {
         this.saveScreenshot(this.shotPath);
         System.exit(0);
         Gdx.app.exit();
      }
   }

   /** Saves a screenshot at the end of the current frame (after the scene is on screen). */
   void requestExit() {
      this.pendingExit = true;
   }

   void requestScreenshot(String path) {
      this.pendingShot = path;
   }

   /** Debug aid: -Drtr.shot=file.png [-Drtr.shotAt=seconds] saves the back buffer and exits. */
   void saveScreenshot(String path) {
      Renderer.gdx().flush();
      int w = Gdx.graphics.getBackBufferWidth();
      int h = Gdx.graphics.getBackBufferHeight();
      Pixmap p = Pixmap.createFromFrameBuffer(0, 0, w, h);
      PixmapIO.writePNG(Gdx.files.absolute(path), p, -1, true);
      p.dispose();
      System.out.println("Saved screenshot " + path);
   }

   @Override
   public void resize(int width, int height) {
      Renderer.gdx().initDisplay(Display.getWidth(), Display.getHeight());
   }

   @Override
   public void pause() {
      Display.setActive(false);
      Keyboard.releaseAll();
   }

   @Override
   public void resume() {
      Display.setActive(true);
   }
}
