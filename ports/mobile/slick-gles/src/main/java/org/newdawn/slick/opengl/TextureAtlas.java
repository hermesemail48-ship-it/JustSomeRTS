package org.newdawn.slick.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import rtr.compat.lwjgl.BufferUtils;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;

/**
 * Packs the game's many small sprite files into a few shared GL textures, so consecutive draws of different sprites
 * (a mob's shadow then its body, GUI pieces, icons...) no longer end the batch on every texture switch.
 * Only RGBA images from an allow-list of folders are packed; they are all drawn with nearest filtering.
 * Each packed file becomes an {@link AtlasTexture} region; Image treats it like an ordinary texture.
 */
public final class TextureAtlas {
   /** Set by the port to enable packing. */
   public static boolean enabled = false;
   /** Page size, chosen when the first page is made (rtr.atlasPage can raise it to 4096 for testing). */
   static int PAGE_SIZE = 0;
   private static final int PAD = 2;
   private static final int MAX_W = 1024;
   private static final int MAX_H = 1024;
   private static final String[] INCLUDE = new String[]{
      "res/mobs/", "res/GUI/", "res/maps/base/", "res/towers/", "res/gates/", "res/lightSources/", "res/particleSheet.png", "res/particles.png", "res/resourceSheet.png",
      "res/resourceArrow.png"
   };
   private static final String[] EXCLUDE = new String[]{"res/GUI/worldMap", "res/GUI/cursors/"};
   private static final SGL GL = Renderer.get();
   private static final ArrayList<Page> pages = new ArrayList<Page>();
   private static final HashMap<String, AtlasTexture> regions = new HashMap<String, AtlasTexture>();
   private static Page lastPage;
   private static long packedPixels;
   private static long livePixels;

   static final class Page {
      final int id;
      /** Open shelves: {y, height, nextX}. Items go on the shelf closest to their height to limit waste. */
      final ArrayList<int[]> shelves = new ArrayList<int[]>();
      /** Space of released images {x, y, w, h} (padded), reused first: menus and maps reload the same files. */
      final ArrayList<int[]> free = new ArrayList<int[]>();
      int nextY;

      Page(int id) {
         this.id = id;
      }

      /** Returns {x, y} or null when the image does not fit. */
      int[] alloc(int w, int h) {
         // Full-width strips (font pages) go edge to edge.
         int pw = Math.min(w + PAD, PAGE_SIZE);
         int ph = h + PAD;
         int[] hole = null;

         for (int[] f : this.free) {
            if (f[2] >= pw && f[3] >= ph && (hole == null || f[2] * f[3] < hole[2] * hole[3])) {
               hole = f;
            }
         }

         if (hole != null) {
            this.free.remove(hole);
            return new int[]{hole[0], hole[1]};
         }

         int[] best = null;

         for (int[] shelf : this.shelves) {
            if (shelf[1] >= ph && shelf[2] + pw <= PAGE_SIZE && (best == null || shelf[1] < best[1])) {
               best = shelf;
            }
         }

         // A shelf much taller than the image wastes the space above it; start a fitting shelf while there is room.
         if ((best == null || best[1] > ph + ph / 2 + 8) && this.nextY + ph <= PAGE_SIZE) {
            best = new int[]{this.nextY, ph, 0};
            this.shelves.add(best);
            this.nextY += ph;
         }

         if (best == null || pw > PAGE_SIZE) {
            return null;
         }

         int[] at = new int[]{best[2], best[0]};
         best[2] += pw;
         return at;
      }
   }

   private TextureAtlas() {
   }

   static boolean eligible(String resourceName, int width, int height, boolean alpha, int[] transparent) {
      if (!enabled || !alpha || transparent != null || width > MAX_W || height > MAX_H || resourceName == null) {
         return false;
      }

      String n = resourceName.replace('\\', '/');

      for (String e : EXCLUDE) {
         if (n.contains(e)) {
            return false;
         }
      }

      for (String i : INCLUDE) {
         if (n.contains(i)) {
            return true;
         }
      }

      return false;
   }

   /** Packs generated RGBA pixels (e.g. font glyph pages); null when disabled or full. */
   public static AtlasTexture packPixels(String name, int width, int height, ByteBuffer rgba) {
      if (!enabled || width > Math.max(PAGE_SIZE, 2048) || height > MAX_H) {
         return null;
      }

      return pack(name, name, width, height, rgba);
   }

   static AtlasTexture get(String resName) {
      return regions.get(resName);
   }

   /** Uploads RGBA pixels into a page; returns null if the image cannot be packed. */
   static AtlasTexture pack(String ref, String resName, int width, int height, ByteBuffer rgba) {
      int[] at = null;
      Page page = lastPage;
      // Prefer the page used last, so files loaded together (a mob sheet and its shadow) share a page.
      if (page != null) {
         at = page.alloc(width, height);
      }

      if (at == null) {
         for (Page p : pages) {
            if (p != lastPage) {
               at = p.alloc(width, height);
               if (at != null) {
                  page = p;
                  break;
               }
            }
         }
      }

      if (at == null) {
         page = newPage();
         if (page == null) {
            return null;
         }

         at = page.alloc(width, height);
         if (at == null) {
            return null;
         }
      }

      lastPage = page;
      packedPixels += (long)width * height;
      livePixels += (long)width * height;
      GL.glBindTexture(3553, page.id);
      GL.glTexSubImage2D(3553, 0, at[0], at[1], width, height, 6408, 5121, rgba);
      TextureImpl.unbind();
      AtlasTexture region = new AtlasTexture(ref, page.id, at[0], at[1], width, height, PAGE_SIZE);
      region.setCacheName(resName);
      regions.put(resName, region);
      return region;
   }

   private static Page newPage() {
      if (PAGE_SIZE == 0) {
         IntBuffer max = BufferUtils.createIntBuffer(16);
         GL.glGetInteger(3379, max);
         // 4096 pages batch a little better but waste ~100 MB more GPU memory on a phone; 2048 is the better trade.
         PAGE_SIZE = max.get(0) >= 2048 ? Integer.getInteger("rtr.atlasPage", 2048) : -1;
      }

      if (PAGE_SIZE <= 0) {
         return null;
      }

      IntBuffer ids = BufferUtils.createIntBuffer(1);
      GL.glGenTextures(ids);
      int id = ids.get(0);
      GL.glBindTexture(3553, id);
      GL.glTexParameteri(3553, 10241, 9728);
      GL.glTexParameteri(3553, 10240, 9728);
      GL.glTexParameteri(3553, 10242, 33071);
      GL.glTexParameteri(3553, 10243, 33071);
      GL.glTexImage2D(3553, 0, 6408, PAGE_SIZE, PAGE_SIZE, 0, 6408, 5121, null);
      TextureImpl.unbind();
      clearToTransparent(id);
      Page p = new Page(id);
      pages.add(p);
      System.out.println("Texture atlas: page " + pages.size() + " (" + regions.size() + " images live, " + livePixels / 1000 + "k px live, " + packedPixels / 1000 + "k px packed in total)");
      return p;
   }

   /** Transparent so the padding between files never bleeds colour (cleared on the GPU, no 64 MB buffer). */
   private static void clearToTransparent(int textureId) {
      com.badlogic.gdx.graphics.GL20 gl = com.badlogic.gdx.Gdx.gl;
      IntBuffer prev = BufferUtils.createIntBuffer(16);
      gl.glGetIntegerv(com.badlogic.gdx.graphics.GL20.GL_FRAMEBUFFER_BINDING, prev);
      IntBuffer vp = BufferUtils.createIntBuffer(16);
      gl.glGetIntegerv(com.badlogic.gdx.graphics.GL20.GL_VIEWPORT, vp);
      int fbo = gl.glGenFramebuffer();
      gl.glBindFramebuffer(com.badlogic.gdx.graphics.GL20.GL_FRAMEBUFFER, fbo);
      gl.glFramebufferTexture2D(
         com.badlogic.gdx.graphics.GL20.GL_FRAMEBUFFER, com.badlogic.gdx.graphics.GL20.GL_COLOR_ATTACHMENT0, com.badlogic.gdx.graphics.GL20.GL_TEXTURE_2D, textureId, 0
      );
      gl.glDisable(com.badlogic.gdx.graphics.GL20.GL_SCISSOR_TEST);
      gl.glColorMask(true, true, true, true);
      gl.glViewport(0, 0, PAGE_SIZE, PAGE_SIZE);
      gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
      gl.glClear(com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT);
      gl.glBindFramebuffer(com.badlogic.gdx.graphics.GL20.GL_FRAMEBUFFER, prev.get(0));
      gl.glDeleteFramebuffer(fbo);
      gl.glViewport(vp.get(0), vp.get(1), vp.get(2), vp.get(3));
      org.newdawn.slick.opengl.renderer.Renderer.gdx().invalidateGlState();
   }

   static void release(AtlasTexture region) {
      if (region.getCacheName() != null && regions.get(region.getCacheName()) == region) {
         regions.remove(region.getCacheName());
      }

      livePixels -= (long)region.getImageWidth() * region.getImageHeight();

      for (Page p : pages) {
         if (p.id == region.getTextureID()) {
            p.free.add(new int[]{region.getRegionX(), region.getRegionY(),
               Math.min(region.getImageWidth() + PAD, PAGE_SIZE), region.getImageHeight() + PAD});
         }
      }
   }

   public static int pageCount() {
      return pages.size();
   }

   public static int imageCount() {
      return regions.size();
   }
}
