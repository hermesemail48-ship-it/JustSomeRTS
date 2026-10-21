package rtr.compat.font;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import java.util.HashMap;
import java.util.Map;

/**
 * A TrueType font at one pixel size, rasterised with FreeType. Stands in for java.awt.Font: metrics follow AWT's
 * conventions (cell height = ascent + descent + leading, integer advances for drawing, fractional for measuring).
 */
public class GdxFont {
   private static final Color GLYPH_WHITE = new Color(Color.WHITE);
   private static FreeType.Library library;
   private static final Map<String, FreeType.Face> faces = new HashMap<String, FreeType.Face>();
   private final FreeType.Face face;
   private final float size;
   private final int ascent;
   private final int lineHeight;

   private static synchronized FreeType.Face face(FileHandle file) {
      if (library == null) {
         library = FreeType.initFreeType();
      }

      String key = file.path();
      FreeType.Face f = faces.get(key);
      if (f == null) {
         f = library.newFace(file, 0);
         faces.put(key, f);
      }

      return f;
   }

   public GdxFont(FileHandle file, float size) {
      this.face = face(file);
      this.size = size;
      synchronized (this.face) {
         this.select();
         FreeType.SizeMetrics m = this.face.getSize().getMetrics();
         this.ascent = FreeType.toInt(m.getAscender());
         int descent = -FreeType.toInt(m.getDescender());
         int height = FreeType.toInt(m.getHeight());
         this.lineHeight = Math.max(height, this.ascent + descent);
      }
   }

   private void select() {
      this.face.setPixelSizes(0, Math.max(1, Math.round(this.size)));
   }

   public float getSize() {
      return this.size;
   }

   public int getAscent() {
      return this.ascent;
   }

   public int getLineHeight() {
      return this.lineHeight;
   }

   /** Integer advance like AWT FontMetrics.charWidth. */
   public int charWidth(char c) {
      synchronized (this.face) {
         this.select();
         if (!this.face.loadChar(c, FreeType.FT_LOAD_DEFAULT)) {
            return 0;
         }

         return FreeType.toInt(this.face.getGlyph().getAdvanceX());
      }
   }

   /** Fractional string width like AWT Font.getStringBounds(..., fractional metrics).getWidth(). */
   public double stringWidth(String text) {
      synchronized (this.face) {
         this.select();
         double w = 0.0;

         for (int i = 0; i < text.length(); i++) {
            if (this.face.loadChar(text.charAt(i), FreeType.FT_LOAD_NO_HINTING)) {
               w += this.face.getGlyph().getLinearHoriAdvance() / 65536.0;
            }
         }

         return w;
      }
   }

   /**
    * Renders one glyph into a white-on-transparent cell of {@code width x getLineHeight()} pixels with the baseline
    * at {@link #getAscent()}, matching how the AWT version drew each character. Caller disposes the Pixmap.
    */
   public Pixmap renderCell(char c, int width, boolean antiAlias) {
      Pixmap cell = new Pixmap(Math.max(1, width), Math.max(1, this.lineHeight), Pixmap.Format.RGBA8888);
      cell.setBlending(Pixmap.Blending.None);
      cell.setColor(0x00000000);
      cell.fill();
      synchronized (this.face) {
         this.select();
         if (!this.face.loadChar(c, FreeType.FT_LOAD_DEFAULT)) {
            return cell;
         }

         FreeType.GlyphSlot slot = this.face.getGlyph();
         if (!slot.renderGlyph(antiAlias ? FreeType.FT_RENDER_MODE_NORMAL : FreeType.FT_RENDER_MODE_MONO)) {
            return cell;
         }

         FreeType.Bitmap bitmap = slot.getBitmap();
         if (bitmap.getWidth() > 0 && bitmap.getRows() > 0) {
            // A copy of WHITE, not the constant: getPixmap's fast path for Color.WHITE leaves the glyph empty on the
            // browser backend (gdx-teavm never copies that Alpha pixmap to the native heap). Same pixels elsewhere.
            Pixmap glyph = bitmap.getPixmap(Pixmap.Format.RGBA8888, GLYPH_WHITE, 1.0F);
            cell.setBlending(Pixmap.Blending.SourceOver);
            cell.drawPixmap(glyph, slot.getBitmapLeft(), this.ascent - slot.getBitmapTop());
            glyph.dispose();
         }
      }

      return cell;
   }
}
