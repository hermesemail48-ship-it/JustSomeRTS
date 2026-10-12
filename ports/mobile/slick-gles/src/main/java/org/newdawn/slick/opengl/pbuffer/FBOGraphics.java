package org.newdawn.slick.opengl.pbuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.InternalTextureLoader;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.renderer.GdxGL;
import org.newdawn.slick.opengl.renderer.Renderer;

/** Slick's render-to-texture Graphics on a GLES framebuffer object. */
public class FBOGraphics extends Graphics {
   private final Image image;
   private int fbo;
   private boolean valid = true;
   private GdxGL.State saved;

   public FBOGraphics(Image image) throws SlickException {
      super(image.getTexture().getTextureWidth(), image.getTexture().getTextureHeight());
      this.image = image;
      this.init();
   }

   private void init() throws SlickException {
      this.fbo = Gdx.gl.glGenFramebuffer();

      try {
         Texture tex = InternalTextureLoader.get().createTexture(this.image.getWidth(), this.image.getHeight(), this.image.getFilter());
         GdxGL gl = Renderer.gdx();
         GdxGL.State outer = gl.saveState();
         Gdx.gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, this.fbo);
         Gdx.gl.glFramebufferTexture2D(GL20.GL_FRAMEBUFFER, GL20.GL_COLOR_ATTACHMENT0, GL20.GL_TEXTURE_2D, tex.getTextureID(), 0);
         int status = Gdx.gl.glCheckFramebufferStatus(GL20.GL_FRAMEBUFFER);
         gl.restoreState(outer);
         if (status != GL20.GL_FRAMEBUFFER_COMPLETE) {
            throw new SlickException("Framebuffer incomplete: 0x" + Integer.toHexString(status));
         }

         this.clear();
         this.flush();
         this.drawImage(this.image, 0.0F, 0.0F);
         this.flush();
         this.image.setTexture(tex);
      } catch (SlickException var4) {
         throw var4;
      } catch (Exception var5) {
         throw new SlickException("Failed to create new texture for FBO", var5);
      }
   }

   @Override
   protected void enable() {
      if (!this.valid) {
         throw new RuntimeException("Attempt to use a destroy()ed offscreen graphics context.");
      }

      GdxGL gl = Renderer.gdx();
      this.saved = gl.saveState();
      gl.beginOffscreen(this.fbo, this.screenWidth, this.screenHeight);
   }

   @Override
   protected void disable() {
      GdxGL gl = Renderer.gdx();
      gl.flush();
      if (this.saved != null) {
         gl.restoreState(this.saved);
         this.saved = null;
      }
   }

   @Override
   public void destroy() {
      super.destroy();
      Gdx.gl.glDeleteFramebuffer(this.fbo);
      this.valid = false;
   }

   @Override
   public void flush() {
      super.flush();
      this.image.flushPixelData();
   }
}
