package rtr.mobile;

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

   public static void cancelTool() {
      PlayState p = play();
      if (p != null) {
         p.cancelInterfaceMode();
      }
   }
}
