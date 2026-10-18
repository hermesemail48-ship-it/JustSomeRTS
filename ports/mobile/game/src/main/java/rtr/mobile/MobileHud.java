package rtr.mobile;

import com.badlogic.gdx.graphics.Color;
import rtr.mobile.ui.BuildDrawer;
import rtr.mobile.ui.InspectCard;
import rtr.mobile.ui.SettingsSheet;
import rtr.mobile.ui.Ui;
import rtr.states.PlayState;

/**
 * The phone overlay drawn over the game at full panel resolution:
 * <ul>
 *   <li>a Build / Settings rail on the right edge (where the desktop build panel was)</li>
 *   <li>the build drawer, inspect card for the selection, and the settings sheet</li>
 *   <li>Cancel / Build while placing a building (the ghost is aimed by dragging and only placed on Build), Done for
 *   painting tools (walls, roads, work orders...)</li>
 * </ul>
 * Coordinates are libGDX window units with a top-left origin, the same as touch events.
 */
public class MobileHud {
   /** What the touch layer does for the tool buttons. */
   public interface ToolListener {
      /** Build pressed: place the building at the ghost. */
      void confirmPlacement();

      /** Cancel / Done pressed. */
      void cancelTool();
   }

   private static final Color BUILD_TINT = new Color(0.12F, 0.30F, 0.12F, 0.94F);
   private static final Color HINT = new Color(1.0F, 1.0F, 1.0F, 0.85F);
   private final Ui ui = new Ui();
   private final BuildDrawer drawer = new BuildDrawer();
   private final InspectCard card = new InspectCard();
   private final SettingsSheet settings = new SettingsSheet();
   private ToolListener listener;
   private long warnUntil;
   private boolean wasUsingTool;

   public void setListener(ToolListener listener) {
      this.listener = listener;
   }

   /** Briefly shows "Can't build here" (after tapping a greyed-out Build). */
   public void flashBlocked() {
      this.warnUntil = System.currentTimeMillis() + 1500L;
   }

   public boolean touchDown(int x, int y) {
      return this.ui.touchDown(x, y);
   }

   public void touchDragged(int x, int y) {
      this.ui.touchDragged(x, y);
   }

   public void touchUp(int x, int y) {
      this.ui.touchUp(x, y);
   }

   public void render() {
      PlayState p = GameBridge.play();
      if (p == null) {
         this.drawer.open = false;
         this.settings.open = false;
         if (!this.ui.isEmpty()) {
            this.ui.begin();
            this.ui.end();
         }

         return;
      }

      this.ui.begin();

      try {
         boolean tool = GameBridge.isUsingTool();
         if (tool && !this.wasUsingTool) {
            this.drawer.open = false;
         }

         this.wasUsingTool = tool;
         if (this.settings.open) {
            this.settings.render(this.ui);
         } else if (tool) {
            this.renderToolButtons(p);
         } else if (this.drawer.open) {
            this.drawer.render(this.ui, p);
         } else {
            this.card.render(this.ui, p);
            this.renderRail(p);
         }
      } finally {
         this.ui.end();
      }
   }

   /** Build and Settings on the right edge, in the space the desktop build panel used. */
   private void renderRail(PlayState p) {
      float u = this.ui.unit();
      float bw = u * 1.9F;
      float bh = u * 0.95F;
      float x = this.ui.width() - bw - u * 0.25F;
      float y = this.ui.height() * 0.42F;
      final PlayState play = p;
      if (!p.hasCamp()) {
         this.ui.button(x - bw * 0.3F, y, bw * 1.3F, bh, "Place Camp", true, BUILD_TINT, new Ui.Action() {
            @Override
            public void run() throws Exception {
               play.startPlacing(rtr.map.MapTilesLoader.TileSet.CASTLE_1);
            }
         }, null, this.ui.body);
         y += bh + u * 0.25F;
      } else if (!p.isSandbox()) {
         this.ui.button(x, y, bw, bh, "Build", true, BUILD_TINT, new Ui.Action() {
            @Override
            public void run() {
               MobileHud.this.drawer.open = true;
            }
         }, null, this.ui.title);
         y += bh + u * 0.25F;
      }

      this.ui.button(x, y, bw, bh * 0.8F, "Settings", true, null, new Ui.Action() {
         @Override
         public void run() {
            MobileHud.this.settings.open = true;
         }
      }, null, this.ui.body);
   }

   private void renderToolButtons(PlayState p) {
      float u = this.ui.unit();
      float bh = u;
      float bw = bh * 2.6F;
      float gap = bh * 0.35F;
      boolean placing = GameBridge.isPlacingBuilding();
      int n = placing ? 2 : 1;
      float total = n * bw + (n - 1) * gap;
      float x0 = (this.ui.width() - total) / 2.0F;
      // Along the bottom edge (the desktop console's spot), clear of the ghost aimed mid-screen.
      float y = this.ui.height() - bh - u * 0.25F;
      Ui.Action cancel = new Ui.Action() {
         @Override
         public void run() {
            if (MobileHud.this.listener != null) {
               MobileHud.this.listener.cancelTool();
            }
         }
      };
      if (placing) {
         boolean canPlace = GameBridge.canPlaceHere();
         this.ui.button(x0, y, bw, bh, "Cancel", true, null, cancel, null, this.ui.title);
         // A greyed Build still takes the tap so it can explain why nothing happened.
         this.ui.button(x0 + bw + gap, y, bw, bh, "Build", true, canPlace ? BUILD_TINT : Ui.DISABLED, new Ui.Action() {
            @Override
            public void run() {
               if (MobileHud.this.listener != null) {
                  MobileHud.this.listener.confirmPlacement();
               }
            }
         }, null, this.ui.title);
      } else {
         this.ui.button(x0, y, bw, bh, "Done", true, null, cancel, null, this.ui.title);
      }

      String hint;
      Color c;
      if (System.currentTimeMillis() < this.warnUntil) {
         hint = "Can't build here - move it to clear ground inside your territory";
         c = Ui.WARN;
      } else {
         hint = placing ? "Tap to move  •  drag anywhere to nudge  •  two fingers to pan" : "Drag to paint  •  two fingers to pan";
         c = HINT;
      }

      this.ui.textCentered(this.ui.small, hint, this.ui.width() / 2.0F, y - u * 0.35F, c);
   }
}
