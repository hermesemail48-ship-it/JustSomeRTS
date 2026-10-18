package rtr.mobile;

import com.badlogic.gdx.Gdx;
import java.io.File;
import org.newdawn.slick.SlickException;
import rtr.compat.GdxSlickApp;
import rtr.compat.RtrPaths;
import rtr.compat.lwjgl.opengl.Display;
import rtr.platform.Platform;
import rtr.platform.gdx.GdxPlatform;
import rtr.system.Game;

/**
 * The libGDX application for every libGDX target.
 * {@code assetsRoot} holds res/ and localization/ (may be read-only); {@code dataRoot} holds maps/, profiles/, logs/.
 */
public class RtrGdxGame extends GdxSlickApp {
   /** The game's interface needs at least 1280x720 logical pixels (see rtr.system.ScaleControl). */
   public static final int MIN_LOGICAL_HEIGHT = 720;
   private final boolean fitToMinimumHeight;
   /** Frame rate while touching (and shortly after), when idle in a running game, and in menus or while paused. */
   private int activeFps;
   private int idleFps;
   private int restFps;
   private int currentFps;
   private long lastInteractionMs;
   private static final long ACTIVE_HOLD_MS = 2500L;

   /**
    * @param touch enable touch gesture translation (phones/tablets)
    * @param fitToMinimumHeight render at a logical height of 720 and let the GPU scale up, so the UI fills the screen
    */
   public RtrGdxGame(final File dataRoot, final File assetsRoot, final boolean touch, final boolean fitToMinimumHeight) {
      super(new Runnable() {
         @Override
         public void run() {
            RtrPaths.setAssetsRoot(assetsRoot);
            RtrPaths.setRoot(dataRoot);
            new File(dataRoot, "profiles").mkdirs();
            new File(dataRoot, "maps").mkdirs();
            new File(dataRoot, "logs").mkdirs();
            Platform.set(new GdxPlatform());
            // Set here, not in the constructor: touching PlayState loads game classes that need the Platform.
            rtr.states.PlayState.touchMode = touch;
            if (fitToMinimumHeight) {
               // Lighter first-run effects on phones: clouds were ~30% of the main thread, particles fill a lot of
               // pixels. Players can turn them back on in Settings.
               java.util.Properties d = new java.util.Properties();
               d.setProperty("clouds", "false");
               d.setProperty("particleAmount", "1");
               d.setProperty("mainMenuDrift", "false");
               rtr.SettingsParser.platformDefaults = d;
            }

            try {
               Game.launchGame();
            } catch (SlickException | rtr.compat.lwjgl.LWJGLException var2) {
               throw new RuntimeException(var2);
            }
         }
      });
      this.fitToMinimumHeight = fitToMinimumHeight;
      if (touch) {
         final MobileHud hud = new MobileHud();
         this.getInput().setTouchTranslator(new TouchControls(hud));
         this.setScreenOverlay(new Runnable() {
            @Override
            public void run() {
               hud.render();
            }
         });
      }
   }

   public RtrGdxGame(File dataRoot, boolean touch, boolean fitToMinimumHeight) {
      this(dataRoot, dataRoot, touch, fitToMinimumHeight);
   }

   public RtrGdxGame(File dataRoot) {
      this(dataRoot, false, false);
   }

   @Override
   public void create() {
      // Simulation at its designed 60 ticks/s regardless of frame rate.
      org.newdawn.slick.GameContainer.fixedTickRate = 60.0F;

      if (this.fitToMinimumHeight) {
         int w = Gdx.graphics.getBackBufferWidth();
         int h = Gdx.graphics.getBackBufferHeight();
         float scale = Math.min((float)Math.min(w, h) / MIN_LOGICAL_HEIGHT, (float)Math.max(w, h) / 1280.0F);
         Display.setRenderScale(Math.max(1.0F, scale));
         // Draw at the logical ~720p size and scale up once, instead of shading every layer at full panel resolution.
         org.newdawn.slick.opengl.renderer.Renderer.gdx().setSceneBufferEnabled(!Boolean.getBoolean("rtr.fullres"));
         // Phones: 30 fps rendering (the simulation still ticks 60x/s) and no back-to-back light-map rebuilds.
         this.activeFps = Integer.getInteger("rtr.fps", 30);
         this.idleFps = Math.min(this.activeFps, Integer.getInteger("rtr.idleFps", 20));
         this.restFps = Math.min(this.idleFps, Integer.getInteger("rtr.restFps", 15));
         this.setFps(this.activeFps);
         // 15 fps needs 4 ticks per frame for 60 ticks/s; leave headroom for frame jitter.
         org.newdawn.slick.GameContainer.maxTicksPerFrame = 6;
         rtr.lighting.LightingModule.minFullRebuildIntervalMs = 1000L;
         rtr.weather.CloudModule.densityScale = 0.5F;
      }

      super.create();
   }

   private void setFps(int fps) {
      if (fps != this.currentFps) {
         this.currentFps = fps;
         Gdx.graphics.setForegroundFPS(fps);
      }
   }

   @Override
   public void render() {
      if (this.activeFps > 0) {
         // Drop the frame rate when nobody is touching the screen: the simulation keeps its 60 ticks/s, only drawing
         // slows down, which is most of the GPU and battery cost.
         long now = System.currentTimeMillis();
         if (Gdx.input.isTouched() || Gdx.input.justTouched()) {
            this.lastInteractionMs = now;
         }

         int target;
         if (now - this.lastInteractionMs < ACTIVE_HOLD_MS) {
            target = this.activeFps;
         } else {
            rtr.states.PlayState play = GameBridge.play();
            target = play != null && !play.isGamePaused() ? this.idleFps : this.restFps;
         }

         this.setFps(target);
      }

      super.render();
   }
}
