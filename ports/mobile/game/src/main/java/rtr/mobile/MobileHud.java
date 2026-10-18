package rtr.mobile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.io.File;
import rtr.compat.RtrPaths;

/**
 * Touch-size buttons drawn over the game while a tool is active: Cancel/Build when placing a building (the ghost is
 * aimed by dragging and only placed on Build), Done for painting tools (walls, roads, work orders...).
 * Coordinates are libGDX window units with a top-left origin, the same as touch events.
 */
public class MobileHud {
   public enum Button {
      CANCEL,
      BUILD,
      DONE
   }

   private static final Color PANEL = new Color(0.16F, 0.09F, 0.06F, 0.92F);
   private static final Color PANEL_PRESSED = new Color(0.32F, 0.20F, 0.12F, 0.95F);
   private static final Color BORDER = new Color(0.78F, 0.55F, 0.25F, 1.0F);
   private static final Color BUILD_TINT = new Color(0.12F, 0.30F, 0.12F, 0.94F);
   private static final Color TEXT = new Color(1.0F, 0.93F, 0.55F, 1.0F);
   private static final Color HINT = new Color(1.0F, 1.0F, 1.0F, 0.85F);
   private static final Color DISABLED = new Color(0.22F, 0.20F, 0.19F, 0.88F);
   private static final Color TEXT_DISABLED = new Color(0.62F, 0.60F, 0.56F, 1.0F);
   private static final Color WARN = new Color(1.0F, 0.45F, 0.35F, 1.0F);
   private long warnUntil;

   /** Briefly shows "Can't build here" (after tapping a greyed-out Build). */
   public void flashBlocked() {
      this.warnUntil = System.currentTimeMillis() + 1500L;
   }

   private SpriteBatch batch;
   private ShapeRenderer shapes;
   private BitmapFont font;
   private BitmapFont hintFont;
   private final GlyphLayout layout = new GlyphLayout();
   private Button pressed;
   private Button[] visible = new Button[0];

   private float buttonH() {
      return Math.max(44.0F, Gdx.graphics.getHeight() * 0.11F);
   }

   private float buttonW() {
      return this.buttonH() * 2.6F;
   }

   private float gap() {
      return this.buttonH() * 0.35F;
   }

   /** Top of the button row: above the selection panel, clear of the game's own placement hints near the top. */
   private float rowTop() {
      return Gdx.graphics.getHeight() * 0.62F;
   }

   private void refreshVisible() {
      if (GameBridge.isPlacingBuilding()) {
         this.visible = new Button[]{MobileHud.Button.CANCEL, MobileHud.Button.BUILD};
      } else if (GameBridge.isUsingTool()) {
         this.visible = new Button[]{MobileHud.Button.DONE};
      } else {
         this.visible = new Button[0];
      }
   }

   /** Left edge of button i in the current row. */
   private float buttonX(int i) {
      float total = this.visible.length * this.buttonW() + (this.visible.length - 1) * this.gap();
      return (Gdx.graphics.getWidth() - total) / 2.0F + i * (this.buttonW() + this.gap());
   }

   /** The button under a touch point (window units, top-left origin), or null. */
   public Button hit(int x, int y) {
      this.refreshVisible();
      float top = this.rowTop();
      if (y < top || y > top + this.buttonH()) {
         return null;
      }

      for (int i = 0; i < this.visible.length; i++) {
         float bx = this.buttonX(i);
         if (x >= bx && x <= bx + this.buttonW()) {
            return this.visible[i];
         }
      }

      return null;
   }

   public void setPressed(Button b) {
      this.pressed = b;
   }

   private void ensureResources() {
      if (this.batch != null) {
         return;
      }

      this.batch = new SpriteBatch(64);
      this.shapes = new ShapeRenderer(64);
      File ttf = new File(RtrPaths.ASSETS + "res/font/PatuaOne-Regular.ttf");
      if (ttf.isFile()) {
         FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.absolute(ttf.getAbsolutePath()));
         FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
         p.size = Math.round(this.buttonH() * 0.42F);
         p.borderWidth = Math.max(1.0F, p.size / 14.0F);
         p.borderColor = new Color(0.1F, 0.05F, 0.02F, 1.0F);
         this.font = gen.generateFont(p);
         p.size = Math.round(this.buttonH() * 0.26F);
         p.borderWidth = Math.max(1.0F, p.size / 10.0F);
         this.hintFont = gen.generateFont(p);
         gen.dispose();
      } else {
         this.font = new BitmapFont();
         this.hintFont = new BitmapFont();
      }
   }

   public void render() {
      this.refreshVisible();
      if (this.visible.length == 0) {
         this.pressed = null;
         return;
      }

      this.ensureResources();
      int w = Gdx.graphics.getWidth();
      int h = Gdx.graphics.getHeight();
      Gdx.gl.glViewport(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
      Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
      Gdx.gl.glColorMask(true, true, true, true);
      Gdx.gl.glEnable(GL20.GL_BLEND);
      Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
      float bh = this.buttonH();
      float bw = this.buttonW();
      float yTop = this.rowTop();
      float y = h - yTop - bh;
      float border = Math.max(2.0F, bh * 0.06F);
      boolean canPlace = GameBridge.canPlaceHere();
      this.shapes.getProjectionMatrix().setToOrtho2D(0.0F, 0.0F, w, h);
      this.shapes.begin(ShapeRenderer.ShapeType.Filled);

      for (int i = 0; i < this.visible.length; i++) {
         float x = this.buttonX(i);
         this.shapes.setColor(BORDER);
         this.shapes.rect(x - border, y - border, bw + border * 2.0F, bh + border * 2.0F);
         Button b = this.visible[i];
         boolean disabled = b == MobileHud.Button.BUILD && !canPlace;
         this.shapes.setColor(disabled ? DISABLED : (b == this.pressed ? PANEL_PRESSED : (b == MobileHud.Button.BUILD ? BUILD_TINT : PANEL)));
         this.shapes.rect(x, y, bw, bh);
      }

      this.shapes.end();
      this.batch.getProjectionMatrix().setToOrtho2D(0.0F, 0.0F, w, h);
      this.batch.begin();

      for (int i = 0; i < this.visible.length; i++) {
         String label = this.label(this.visible[i]);
         this.font.setColor(this.visible[i] == MobileHud.Button.BUILD && !canPlace ? TEXT_DISABLED : TEXT);
         this.layout.setText(this.font, label);
         this.font.draw(this.batch, label, this.buttonX(i) + (bw - this.layout.width) / 2.0F, y + (bh + this.layout.height) / 2.0F);
      }

      String hint;
      if (System.currentTimeMillis() < this.warnUntil) {
         hint = "Can't build here - move it to clear ground inside your territory";
         this.hintFont.setColor(WARN);
      } else {
         hint = GameBridge.isPlacingBuilding()
            ? "Tap to move  •  drag anywhere to nudge  •  two fingers to pan"
            : "Drag to paint  •  two fingers to pan";
         this.hintFont.setColor(HINT);
      }

      this.layout.setText(this.hintFont, hint);
      this.hintFont.draw(this.batch, hint, (w - this.layout.width) / 2.0F, y + bh + border + this.layout.height + bh * 0.2F);
      this.batch.end();
   }

   private String label(Button b) {
      switch (b) {
         case CANCEL:
            return "Cancel";
         case BUILD:
            return "Build";
         default:
            return "Done";
      }
   }
}
