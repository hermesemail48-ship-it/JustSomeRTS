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

   /**
    * @param touch enable touch gesture translation (phones/tablets)
    * @param fitToMinimumHeight render at a logical height of 720 and let the GPU scale up, so the UI fills the screen
    */
   public RtrGdxGame(final File dataRoot, final File assetsRoot, boolean touch, boolean fitToMinimumHeight) {
      super(new Runnable() {
         @Override
         public void run() {
            RtrPaths.setAssetsRoot(assetsRoot);
            RtrPaths.setRoot(dataRoot);
            new File(dataRoot, "profiles").mkdirs();
            new File(dataRoot, "maps").mkdirs();
            new File(dataRoot, "logs").mkdirs();
            Platform.set(new GdxPlatform());

            try {
               Game.launchGame();
            } catch (SlickException | rtr.compat.lwjgl.LWJGLException var2) {
               throw new RuntimeException(var2);
            }
         }
      });
      this.fitToMinimumHeight = fitToMinimumHeight;
      if (touch) {
         this.getInput().setTouchTranslator(new TouchControls());
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
      if (this.fitToMinimumHeight) {
         int w = Gdx.graphics.getBackBufferWidth();
         int h = Gdx.graphics.getBackBufferHeight();
         float scale = Math.min((float)Math.min(w, h) / MIN_LOGICAL_HEIGHT, (float)Math.max(w, h) / 1280.0F);
         Display.setRenderScale(Math.max(1.0F, scale));
      }

      super.create();
   }
}
