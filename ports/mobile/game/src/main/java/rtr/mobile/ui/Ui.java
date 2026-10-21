package rtr.mobile.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import rtr.compat.RtrPaths;

/**
 * A tiny immediate-mode toolkit for the phone overlay, drawn at full panel resolution over the game.
 * Coordinates are libGDX window units with a top-left origin (the same as touch events). Each frame the components
 * draw themselves and register hit areas; touches are matched against the areas of the last drawn frame.
 */
public final class Ui {
   public interface Action {
      void run() throws Exception;
   }

   /** Scroll state of a scrolling row (owned by the component, kept across frames). */
   public static final class Scroll {
      public float offset;
      public float max;
      final boolean horizontal;

      public Scroll(boolean horizontal) {
         this.horizontal = horizontal;
      }

      void clamp() {
         this.offset = Math.max(0.0F, Math.min(this.max, this.offset));
      }
   }

   private static final class Hit {
      float x;
      float y;
      float w;
      float h;
      Action tap;
      Scroll scroll;

      boolean contains(float px, float py) {
         return px >= this.x && px <= this.x + this.w && py >= this.y && py <= this.y + this.h;
      }
   }

   public static final Color PANEL = new Color(0.13F, 0.08F, 0.05F, 0.94F);
   public static final Color PANEL_DARK = new Color(0.08F, 0.05F, 0.03F, 0.96F);
   public static final Color BORDER = new Color(0.78F, 0.55F, 0.25F, 1.0F);
   public static final Color BUTTON = new Color(0.30F, 0.18F, 0.10F, 1.0F);
   public static final Color BUTTON_PRESSED = new Color(0.48F, 0.30F, 0.16F, 1.0F);
   public static final Color BUTTON_ON = new Color(0.16F, 0.36F, 0.16F, 1.0F);
   public static final Color DISABLED = new Color(0.22F, 0.20F, 0.19F, 0.92F);
   public static final Color TEXT = new Color(1.0F, 0.93F, 0.55F, 1.0F);
   public static final Color TEXT_LIGHT = new Color(0.96F, 0.94F, 0.90F, 1.0F);
   public static final Color TEXT_DIM = new Color(0.66F, 0.62F, 0.56F, 1.0F);
   public static final Color WARN = new Color(1.0F, 0.45F, 0.35F, 1.0F);
   public static final Color GOOD = new Color(0.55F, 0.95F, 0.45F, 1.0F);
   public static final Color BAR_BACK = new Color(0.05F, 0.03F, 0.02F, 1.0F);
   public static final Color DIM_SCREEN = new Color(0.0F, 0.0F, 0.0F, 0.45F);

   private SpriteBatch batch;
   private ShapeRenderer shapes;
   public BitmapFont title;
   public BitmapFont body;
   public BitmapFont small;
   private final GlyphLayout layout = new GlyphLayout();
   private final Matrix4 projection = new Matrix4();
   private final HashMap<String, Texture> textures = new HashMap<String, Texture>();
   private ArrayList<Hit> hits = new ArrayList<Hit>();
   private ArrayList<Hit> lastHits = new ArrayList<Hit>();
   private int mode;
   private float width;
   private float height;
   private Hit pressed;
   private float downX;
   private float downY;
   private float downOffset;
   private boolean scrolling;
   private boolean tapCancelled;

   /** Standard touch target height in window units (about 11% of the screen height, at least 44). */
   public float unit() {
      return Math.max(44.0F, Gdx.graphics.getHeight() * 0.11F);
   }

   public float width() {
      return this.width;
   }

   public float height() {
      return this.height;
   }

   private float slop() {
      return Math.max(8.0F, Gdx.graphics.getHeight() / 60.0F);
   }

   private void ensureResources() {
      if (this.batch != null) {
         return;
      }

      this.batch = new SpriteBatch(256);
      this.shapes = new ShapeRenderer(512);
      float u = this.unit();
      File ttf = new File(RtrPaths.ASSETS + "res/font/PatuaOne-Regular.ttf");
      if (ttf.isFile()) {
         FreeTypeFontGenerator gen = new FreeTypeFontGenerator(rtr.compat.GdxFiles.absolute(ttf.getAbsolutePath()));
         this.title = this.font(gen, Math.round(u * 0.40F));
         this.body = this.font(gen, Math.round(u * 0.30F));
         this.small = this.font(gen, Math.round(u * 0.24F));
         gen.dispose();
      } else {
         this.title = new BitmapFont(true);
         this.body = new BitmapFont(true);
         this.small = new BitmapFont(true);
      }
   }

   private BitmapFont font(FreeTypeFontGenerator gen, int size) {
      FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
      p.size = Math.max(8, size);
      p.flip = true;
      p.borderWidth = Math.max(1.0F, p.size / 14.0F);
      p.borderColor = new Color(0.1F, 0.05F, 0.02F, 1.0F);
      p.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "•×−—–’“”…⚙";
      BitmapFont f = gen.generateFont(p);
      f.setUseIntegerPositions(true);
      return f;
   }

   // ---------------------------------------------------------------- frame

   public void begin() {
      this.ensureResources();
      this.width = Gdx.graphics.getWidth();
      this.height = Gdx.graphics.getHeight();
      this.hits = new ArrayList<Hit>();
      Gdx.gl.glViewport(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
      Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
      Gdx.gl.glColorMask(true, true, true, true);
      Gdx.gl.glEnable(GL20.GL_BLEND);
      Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
      this.projection.setToOrtho(0.0F, this.width, this.height, 0.0F, -1.0F, 1.0F);
      this.shapes.setProjectionMatrix(this.projection);
      this.batch.setProjectionMatrix(this.projection);
      this.mode = 0;
   }

   public void end() {
      this.flush();
      Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
      this.lastHits = this.hits;
   }

   /** True when nothing was drawn last frame that could take a touch. */
   public boolean isEmpty() {
      return this.lastHits.isEmpty();
   }

   private void flush() {
      if (this.mode == 1) {
         this.shapes.end();
      } else if (this.mode == 2) {
         this.batch.end();
      }

      this.mode = 0;
   }

   private void useShapes() {
      if (this.mode != 1) {
         this.flush();
         this.shapes.begin(ShapeRenderer.ShapeType.Filled);
         this.mode = 1;
      }
   }

   private void useBatch() {
      if (this.mode != 2) {
         this.flush();
         this.batch.begin();
         this.mode = 2;
      }
   }

   public void clip(float x, float y, float w, float h) {
      this.flush();
      float sx = Gdx.graphics.getBackBufferWidth() / this.width;
      float sy = Gdx.graphics.getBackBufferHeight() / this.height;
      Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
      Gdx.gl.glScissor(Math.round(x * sx), Math.round((this.height - y - h) * sy), Math.round(w * sx), Math.round(h * sy));
   }

   public void unclip() {
      this.flush();
      Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
   }

   // ---------------------------------------------------------------- drawing

   public void rect(float x, float y, float w, float h, Color c) {
      this.useShapes();
      this.shapes.setColor(c);
      this.shapes.rect(x, y, w, h);
   }

   /** A bordered panel that also swallows touches (so taps on it never reach the map). */
   public void panel(float x, float y, float w, float h) {
      this.frame(x, y, w, h, PANEL);
      this.block(x, y, w, h);
   }

   public void frame(float x, float y, float w, float h, Color fill) {
      float b = Math.max(2.0F, this.unit() * 0.05F);
      this.rect(x - b, y - b, w + b * 2.0F, h + b * 2.0F, BORDER);
      this.rect(x, y, w, h, fill);
   }

   /** Swallows touches in an area without an action. */
   public void block(float x, float y, float w, float h) {
      this.addHit(x, y, w, h, null, null);
   }

   public float textWidth(BitmapFont f, String s) {
      this.layout.setText(f, s);
      return this.layout.width;
   }

   public float lineHeight(BitmapFont f) {
      return f.getCapHeight() - f.getDescent();
   }

   /** Draws text with its top-left corner at (x, y). */
   public void text(BitmapFont f, String s, float x, float y, Color c) {
      this.useBatch();
      f.setColor(c);
      f.draw(this.batch, s, x, y);
   }

   /** Draws text cut to a width, adding an ellipsis. */
   public void textFit(BitmapFont f, String s, float x, float y, float maxW, Color c) {
      if (this.textWidth(f, s) > maxW) {
         while (s.length() > 1 && this.textWidth(f, s + "…") > maxW) {
            s = s.substring(0, s.length() - 1);
         }

         s = s + "…";
      }

      this.text(f, s, x, y, c);
   }

   public void textCentered(BitmapFont f, String s, float cx, float cy, Color c) {
      this.layout.setText(f, s);
      this.text(f, s, Math.round(cx - this.layout.width / 2.0F), Math.round(cy - f.getCapHeight() / 2.0F), c);
   }

   /** Word-wraps text into a box; returns the height used. */
   public float textWrapped(BitmapFont f, String s, float x, float y, float w, int maxLines, Color c) {
      String[] words = s.split(" ");
      StringBuilder line = new StringBuilder();
      float lh = this.lineHeight(f) * 1.25F;
      int lines = 0;

      for (int i = 0; i < words.length; i++) {
         String trial = line.length() == 0 ? words[i] : line + " " + words[i];
         if (line.length() > 0 && this.textWidth(f, trial) > w) {
            if (lines == maxLines - 1) {
               this.textFit(f, line + " " + words[i], x, y + lines * lh, w, c);
               return (lines + 1) * lh;
            }

            this.text(f, line.toString(), x, y + lines * lh, c);
            lines++;
            line = new StringBuilder(words[i]);
         } else {
            line = new StringBuilder(trial);
         }
      }

      if (line.length() > 0) {
         this.textFit(f, line.toString(), x, y + lines * lh, w, c);
         lines++;
      }

      return lines * lh;
   }

   /** Draws a game image file (path under the assets root), keeping its aspect ratio inside the box. */
   public void image(String assetPath, float x, float y, float w, float h, Color tint) {
      Texture t = this.texture(assetPath);
      if (t == null) {
         return;
      }

      float s = Math.min(w / t.getWidth(), h / t.getHeight());
      float dw = t.getWidth() * s;
      float dh = t.getHeight() * s;
      this.useBatch();
      this.batch.setColor(tint);
      this.batch.draw(t, x + (w - dw) / 2.0F, y + (h - dh) / 2.0F, dw, dh, 0, 0, t.getWidth(), t.getHeight(), false, true);
      this.batch.setColor(Color.WHITE);
   }

   private Texture texture(String assetPath) {
      if (this.textures.containsKey(assetPath)) {
         return this.textures.get(assetPath);
      }

      Texture t = null;
      File f = new File(RtrPaths.ASSETS + assetPath);
      if (f.isFile()) {
         try {
            t = new Texture(rtr.compat.GdxFiles.absolute(f.getAbsolutePath()));
            t.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
         } catch (RuntimeException e) {
            System.err.println("Phone UI: cannot load " + assetPath + ": " + e);
         }
      }

      this.textures.put(assetPath, t);
      return t;
   }

   /** A progress bar with a label drawn inside it. */
   public void bar(float x, float y, float w, float h, float frac, Color fill, String label) {
      frac = Math.max(0.0F, Math.min(1.0F, Float.isNaN(frac) ? 0.0F : frac));
      this.rect(x, y, w, h, BORDER);
      float b = Math.max(1.0F, h * 0.08F);
      this.rect(x + b, y + b, w - b * 2.0F, h - b * 2.0F, BAR_BACK);
      this.rect(x + b, y + b, (w - b * 2.0F) * frac, h - b * 2.0F, fill);
      if (label != null) {
         this.textFit(this.small, label, x + b * 4.0F, y + (h - this.small.getCapHeight()) / 2.0F, w - b * 8.0F, TEXT_LIGHT);
      }
   }

   /** A button; disabled buttons swallow touches but do nothing. */
   public boolean button(float x, float y, float w, float h, String label, boolean enabled, Color base, Action tap) {
      return this.button(x, y, w, h, label, enabled, base, tap, null, this.body);
   }

   public boolean button(float x, float y, float w, float h, String label, boolean enabled, Color base, Action tap, Scroll scroll, BitmapFont f) {
      boolean down = enabled && this.isPressed(x, y, w, h);
      this.frame(x, y, w, h, !enabled ? DISABLED : (down ? BUTTON_PRESSED : (base == null ? BUTTON : base)));
      if (label != null) {
         float maxW = w * 0.92F;
         String s = label;
         if (this.textWidth(f, s) > maxW) {
            this.textFit(f, s, x + w * 0.04F, y + (h - f.getCapHeight()) / 2.0F, maxW, enabled ? TEXT : TEXT_DIM);
         } else {
            this.textCentered(f, s, x + w / 2.0F, y + h / 2.0F, enabled ? TEXT : TEXT_DIM);
         }
      }

      this.addHit(x, y, w, h, enabled ? tap : null, scroll);
      return down;
   }

   /** Registers a tap area without drawing (for custom-drawn cards). */
   public void area(float x, float y, float w, float h, Action tap, Scroll scroll) {
      this.addHit(x, y, w, h, tap, scroll);
   }

   public boolean isPressed(float x, float y, float w, float h) {
      Hit p = this.pressed;
      return p != null && !this.scrolling && !this.tapCancelled && p.tap != null && p.x == x && p.y == y && p.w == w && p.h == h;
   }

   private void addHit(float x, float y, float w, float h, Action tap, Scroll scroll) {
      Hit hit = new Hit();
      hit.x = x;
      hit.y = y;
      hit.w = w;
      hit.h = h;
      hit.tap = tap;
      hit.scroll = scroll;
      this.hits.add(hit);
   }

   // ---------------------------------------------------------------- touch

   /** True if the touch landed on something this overlay drew (it then owns the gesture until release). */
   public boolean touchDown(float x, float y) {
      for (int i = this.lastHits.size() - 1; i >= 0; i--) {
         Hit h = this.lastHits.get(i);
         if (h.contains(x, y)) {
            this.pressed = h;
            this.downX = x;
            this.downY = y;
            this.downOffset = h.scroll == null ? 0.0F : h.scroll.offset;
            this.scrolling = false;
            this.tapCancelled = false;
            return true;
         }
      }

      return false;
   }

   public void touchDragged(float x, float y) {
      Hit p = this.pressed;
      if (p == null) {
         return;
      }

      float dx = x - this.downX;
      float dy = y - this.downY;
      if (!this.scrolling && !this.tapCancelled && Math.abs(dx) <= this.slop() && Math.abs(dy) <= this.slop()) {
         return;
      }

      if (p.scroll != null) {
         this.scrolling = true;
         p.scroll.offset = this.downOffset - (p.scroll.horizontal ? dx : dy);
         p.scroll.clamp();
      } else {
         this.tapCancelled = !p.contains(x, y);
      }
   }

   public void touchUp(float x, float y) {
      Hit p = this.pressed;
      this.pressed = null;
      if (p != null && p.tap != null && !this.scrolling && p.contains(x, y)) {
         try {
            p.tap.run();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }

   /** Strips the game's inline colour codes ("$GRE1", "$YEL0"...). */
   public static String plain(String s) {
      return s == null ? "" : s.replaceAll("\\$[A-Z]{3}\\d", "").trim();
   }
}
