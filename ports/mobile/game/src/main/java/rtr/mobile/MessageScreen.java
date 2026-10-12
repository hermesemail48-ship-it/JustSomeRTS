package rtr.mobile;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

/** Plain text screen (libGDX's built-in font) for setup problems, e.g. missing game data. */
public class MessageScreen extends ApplicationAdapter {
   private final String message;
   private SpriteBatch batch;
   private BitmapFont font;

   public MessageScreen(String message) {
      this.message = message;
   }

   @Override
   public void create() {
      this.batch = new SpriteBatch();
      this.font = new BitmapFont();
      this.font.getData().setScale(Math.max(1.0F, Gdx.graphics.getHeight() / 360.0F));
   }

   @Override
   public void render() {
      Gdx.gl.glClearColor(0.08F, 0.06F, 0.05F, 1.0F);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
      this.batch.getProjectionMatrix().setToOrtho2D(0.0F, 0.0F, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
      this.batch.begin();
      float margin = Gdx.graphics.getWidth() * 0.06F;
      this.font.draw(this.batch, this.message, margin, Gdx.graphics.getHeight() - margin, Gdx.graphics.getWidth() - 2.0F * margin, Align.left, true);
      this.batch.end();
   }

   @Override
   public void dispose() {
      this.batch.dispose();
      this.font.dispose();
   }
}
