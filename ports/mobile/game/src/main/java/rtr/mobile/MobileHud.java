package rtr.mobile;

import com.badlogic.gdx.graphics.Color;
import rtr.mobile.ui.InspectCard;
import rtr.mobile.ui.SettingsSheet;
import rtr.mobile.ui.Ui;
import rtr.states.PlayState;

/**
 * The phone overlay drawn over the game at full panel resolution:
 * <ul>
 *   <li>a large handle that slides the game's own build panel in and out on the right, with Settings below it</li>
 *   <li>the inspect card for the selection, and the settings sheet</li>
 *   <li>while placing a building: a highlight on the ghost (green when it can be built, tap it to build) and an X
 *   button to stop; Done for painting tools (walls, roads, work orders...)</li>
 * </ul>
 * The build panel tucks itself away while a tool is in use and comes back afterwards.
 * Coordinates are libGDX window units with a top-left origin, the same as touch events.
 */
public class MobileHud {
   /** What the touch layer does for the tool buttons. */
   public interface ToolListener {
      /** Build the building at the ghost. */
      void confirmPlacement();

      /** X / Done pressed. */
      void cancelTool();
   }

   private static final Color BUILD_TINT = new Color(0.12F, 0.30F, 0.12F, 0.94F);
   private static final Color HINT = new Color(1.0F, 1.0F, 1.0F, 0.9F);
   private static final Color GHOST_OK = new Color(0.45F, 1.0F, 0.40F, 1.0F);
   private static final Color GHOST_BAD = new Color(1.0F, 0.35F, 0.28F, 1.0F);
   private final Ui ui = new Ui();
   private final InspectCard card = new InspectCard();
   private final SettingsSheet settings = new SettingsSheet();
   private final Color pulse = new Color();
   private ToolListener listener;
   private long warnUntil;
   private boolean wasUsingTool;
   /** The build panel was open when a tool started, so it reopens when the tool ends. */
   private boolean reopenSidebar;
   /** Ghost footprint (window units) from the last frame, for {@link #isOnGhost}. */
   private float[] ghost;

   public void setListener(ToolListener listener) {
      this.listener = listener;
   }

   /** Briefly shows "Can't build here" (after tapping a ghost that cannot be built). */
   public void flashBlocked() {
      this.warnUntil = System.currentTimeMillis() + 1500L;
   }

   /** True if a touch is on (or within a finger's width of) the building ghost. */
   public boolean isOnGhost(int x, int y) {
      float[] g = this.ghost;
      if (g == null) {
         return false;
      }

      float m = this.ui.unit() * 0.3F;
      return x >= g[0] - m && x <= g[0] + g[2] + m && y >= g[1] - m && y <= g[1] + g[3] + m;
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
         this.settings.open = false;
         this.ghost = null;
         this.wasUsingTool = false;
         this.reopenSidebar = false;
         if (!this.ui.isEmpty()) {
            this.ui.begin();
            this.ui.end();
         }

         return;
      }

      this.ui.begin();

      try {
         boolean tool = GameBridge.isUsingTool();
         if (tool && !this.wasUsingTool && p.isBuildSidebarOpen()) {
            p.setBuildSidebarOpen(false);
            this.reopenSidebar = true;
         } else if (!tool && this.wasUsingTool && this.reopenSidebar) {
            p.setBuildSidebarOpen(true);
            this.reopenSidebar = false;
         }

         this.wasUsingTool = tool;
         this.ghost = GameBridge.isPlacingBuilding() ? GameBridge.ghostBounds() : null;
         if (this.settings.open) {
            this.settings.render(this.ui);
         } else if (tool) {
            this.renderToolButtons(p);
         } else {
            this.card.render(this.ui, p);
            this.renderSidebarHandle(p);
         }
      } finally {
         this.ui.end();
      }
   }

   /** A tab on the build panel's left edge (or the screen edge when it is tucked away), with Settings below it. */
   private void renderSidebarHandle(final PlayState p) {
      float u = this.ui.unit();
      final boolean open = p.isBuildSidebarOpen();
      float[] sb = GameBridge.displayToWindow(p.getBuildSidebarBounds());
      float hw = u * 0.78F;
      float hh = u * 1.25F;
      // Open: tucked slightly under the panel's edge. Closed: just inside the screen edge.
      float hx = open ? sb[0] - hw + u * 0.12F : this.ui.width() - hw - u * 0.12F;
      float hy = Math.max(u * 0.2F, sb[1] + u * 0.15F);
      boolean down = this.ui.isPressed(hx, hy, hw, hh);
      this.ui.frame(hx, hy, hw, hh, down ? Ui.BUTTON_PRESSED : BUILD_TINT);
      this.ui.chevron(hx + hw * 0.45F, hy + hh * 0.36F, u * 0.34F, Math.max(2.0F, u * 0.07F), open ? 1 : -1, Ui.TEXT);
      this.ui.textCentered(this.ui.small, "Build", hx + hw * 0.45F, hy + hh * 0.74F, Ui.TEXT);
      this.ui.area(hx, hy, hw, hh, new Ui.Action() {
         @Override
         public void run() {
            p.setBuildSidebarOpen(!open);
            MobileHud.this.reopenSidebar = false;
         }
      }, null);
      float gy = hy + hh + u * 0.18F;
      float gh = u * 0.78F;
      boolean gDown = this.ui.isPressed(hx, gy, hw, gh);
      this.ui.frame(hx, gy, hw, gh, gDown ? Ui.BUTTON_PRESSED : Ui.BUTTON);
      this.ui.gear(hx + hw * 0.45F, gy + gh / 2.0F, u * 0.2F, Ui.TEXT, gDown ? Ui.BUTTON_PRESSED : Ui.BUTTON);
      this.ui.area(hx, gy, hw, gh, new Ui.Action() {
         @Override
         public void run() {
            MobileHud.this.settings.open = true;
         }
      }, null);
   }

   private void renderToolButtons(PlayState p) {
      float u = this.ui.unit();
      boolean placing = GameBridge.isPlacingBuilding();
      Ui.Action cancel = new Ui.Action() {
         @Override
         public void run() {
            if (MobileHud.this.listener != null) {
               MobileHud.this.listener.cancelTool();
            }
         }
      };
      float hintY;
      if (placing) {
         boolean canPlace = GameBridge.canPlaceHere();
         this.renderGhost(canPlace);
         float r = u * 0.48F;
         float cx = this.ui.width() / 2.0F;
         float cy = this.ui.height() - r - u * 0.3F;
         boolean down = this.ui.circleButton(cx, cy, r, Ui.DANGER, cancel);
         this.ui.cross(cx, cy, r * 0.8F, Math.max(3.0F, u * 0.1F), down ? Ui.TEXT : Ui.TEXT_LIGHT);
         hintY = cy - r - u * 0.38F;
      } else {
         float bh = u * 0.9F;
         float bw = bh * 2.6F;
         float y = this.ui.height() - bh - u * 0.25F;
         this.ui.button((this.ui.width() - bw) / 2.0F, y, bw, bh, "Done", true, null, cancel, null, this.ui.title);
         hintY = y - u * 0.38F;
      }

      if (System.currentTimeMillis() < this.warnUntil) {
         this.ui.pill(this.ui.small, "Can't build here - move it to clear ground inside your territory", this.ui.width() / 2.0F, hintY, Ui.WARN);
      } else {
         String hint = placing ? "Drag the building to move it  •  tap it to build  •  drag the map to pan" : "Drag to paint  •  two fingers to pan";
         this.ui.pill(this.ui.small, hint, this.ui.width() / 2.0F, hintY, HINT);
      }
   }

   /** A pulsing outline around the ghost's footprint and a label above it. */
   private void renderGhost(boolean canPlace) {
      float[] g = this.ghost;
      if (g == null || g[2] <= 0.0F) {
         return;
      }

      float u = this.ui.unit();
      float t = Math.max(2.0F, u * 0.05F);
      float m = t * 2.0F;
      Color base = canPlace ? GHOST_OK : GHOST_BAD;
      float wave = (float)(0.5 + 0.5 * Math.sin(System.currentTimeMillis() / 220.0));
      this.pulse.set(base.r, base.g, base.b, 0.55F + 0.4F * wave);
      this.ui.outline(g[0] - m, g[1] - m, g[2] + m * 2.0F, g[3] + m * 2.0F, t, this.pulse);
      float ly = Math.max(u * 0.3F, g[1] - m - u * 0.32F);
      this.ui.pill(this.ui.small, canPlace ? "Tap to build" : "Can't build here", g[0] + g[2] / 2.0F, ly, base);
   }
}
