package rtr.mobile;

import com.badlogic.gdx.Gdx;
import rtr.compat.lwjgl.opengl.Display;
import rtr.gui.GUIEnums;
import rtr.states.PlayState;
import rtr.states.StateBase;
import rtr.system.Game;

/** Read-only view of the running game for the touch layer (all calls are on the render thread). */
public final class GameBridge {
   private GameBridge() {
   }

   /** The play state, once its modules are loaded; null in menus, on the world map and while loading. */
   public static PlayState play() {
      StateBase cs = Game.getCS();
      return cs instanceof PlayState && cs.isModulesLoaded() ? (PlayState)cs : null;
   }

   public static GUIEnums.InterfaceMode mode() {
      PlayState p = play();
      return p == null ? null : p.getInterfaceMode();
   }

   /** Placing a building: taps should aim the ghost, and placement needs an explicit confirm. */
   public static boolean isPlacingBuilding() {
      return mode() == GUIEnums.InterfaceMode.OBJECT;
   }

   /** Any tool other than plain selection (building, walls, roads, harvest/clear/dig orders, dismantle, spells...). */
   public static boolean isUsingTool() {
      GUIEnums.InterfaceMode m = mode();
      if (m == null) {
         return false;
      }

      switch (m) {
         case SELECT:
         case PAUSE:
            return false;
         default:
            return true;
      }
   }

   /** Whether Build would succeed with the ghost where it is now. */
   public static boolean canPlaceHere() {
      PlayState p = play();
      return p != null && p.canPlaceAtPointer();
   }

   public static boolean isGuiAt(int displayX, int displayY) {
      PlayState p = play();
      return p != null && p.isGuiAt(displayX, displayY);
   }

   /** Moves the map by a finger movement in display pixels. */
   public static void pan(float dx, float dy) {
      PlayState p = play();
      if (p != null) {
         p.panByDisplay(dx, dy);
      }
   }

   /** Display pixels (the game's logical screen, top-left origin) to window units, as {x, y, w, h}. */
   public static float[] displayToWindow(float[] r) {
      if (r == null) {
         return null;
      }

      float kx = (float)Gdx.graphics.getWidth() / Math.max(1, Display.getWidth());
      float ky = (float)Gdx.graphics.getHeight() / Math.max(1, Display.getHeight());
      float offY = org.newdawn.slick.opengl.renderer.Renderer.gdx().getViewOffsetY();
      return new float[]{r[0] * kx, (r[1] - offY) * ky, r[2] * kx, r[3] * ky};
   }

   /** The building ghost's footprint in window units, or null when not placing. */
   public static float[] ghostBounds() {
      PlayState p = play();
      return p == null ? null : displayToWindow(p.getPlacementGhostBounds());
   }

   public static void cancelTool() {
      PlayState p = play();
      if (p != null) {
         p.cancelInterfaceMode();
      }
   }
}
