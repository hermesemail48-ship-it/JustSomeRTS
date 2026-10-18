package rtr.compat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/** Performance readout drawn over the game. Toggle: three-finger tap, or F3 on a keyboard. */
public class PerfOverlay {
   private SpriteBatch batch;
   private BitmapFont font;
   private ShapeRenderer shapes;
   public boolean visible = Boolean.getBoolean("rtr.perfOverlay");

   public void toggle() {
      this.visible = !this.visible;
   }

   public void render() {
      if (!this.visible) {
         return;
      }

      if (this.batch == null) {
         this.batch = new SpriteBatch(200);
         this.font = new BitmapFont();
         this.shapes = new ShapeRenderer(32);
      }

      float scale = Math.max(1.0F, Gdx.graphics.getBackBufferHeight() / 540.0F);
      this.font.getData().setScale(scale);
      int w = Gdx.graphics.getBackBufferWidth();
      int h = Gdx.graphics.getBackBufferHeight();
      Gdx.gl.glViewport(0, 0, w, h);
      Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
      Gdx.gl.glColorMask(true, true, true, true);
      Gdx.gl.glEnable(GL20.GL_BLEND);
      Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
      String[] lines = new String[]{
         String.format("%.0f fps   frame %.1f ms   worst %.0f ms", PerfStats.fps, PerfStats.avgFrameMs, PerfStats.worstFrameMs),
         String.format("update %.1f ms (%d/frame)   render %.1f ms", PerfStats.avgUpdateMs, PerfStats.updatesPerFrame, PerfStats.avgRenderMs),
         String.format("draws %.0f   tex %.0f   verts %.0f", PerfStats.avgDrawCalls, PerfStats.avgTextureSwitches, PerfStats.avgVertices),
         String.format("heap %d MB   %dx%d", (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) >> 20, w, h)
      };
      float lineH = this.font.getLineHeight();
      float boxW = 330.0F * scale;
      float boxH = lineH * lines.length + 8.0F * scale;
      this.shapes.getProjectionMatrix().setToOrtho2D(0.0F, 0.0F, w, h);
      this.shapes.begin(ShapeRenderer.ShapeType.Filled);
      this.shapes.setColor(0.0F, 0.0F, 0.0F, 0.6F);
      this.shapes.rect(4.0F * scale, h - boxH - 4.0F * scale, boxW, boxH);
      this.shapes.end();
      this.batch.getProjectionMatrix().setToOrtho2D(0.0F, 0.0F, w, h);
      this.batch.begin();
      this.font.setColor(Color.WHITE);

      for (int i = 0; i < lines.length; i++) {
         this.font.draw(this.batch, lines[i], 8.0F * scale, h - 8.0F * scale - i * lineH);
      }

      this.batch.end();
   }
}
