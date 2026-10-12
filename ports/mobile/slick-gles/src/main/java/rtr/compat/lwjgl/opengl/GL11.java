package rtr.compat.lwjgl.opengl;

import com.badlogic.gdx.Gdx;
import org.newdawn.slick.opengl.renderer.Renderer;

/** The two raw GL calls the game makes outside Slick, routed through the emulated renderer. */
public final class GL11 {
   public static final int GL_VENDOR = 7936;
   public static final int GL_RENDERER = 7937;
   public static final int GL_VERSION = 7938;

   public static void glBlendFunc(int sfactor, int dfactor) {
      Renderer.get().glBlendFunc(sfactor, dfactor);
   }

   public static String glGetString(int name) {
      return Gdx.gl.glGetString(name);
   }
}
