package rtr.compat.lwjgl.opengl;

import rtr.compat.lwjgl.LWJGLException;

/** Shared-context pbuffers do not exist on GLES; constructing one reports failure so Slick skips it. */
public final class Pbuffer implements Drawable {
   public Pbuffer(int width, int height, PixelFormat format, Drawable shared) throws LWJGLException {
      throw new LWJGLException("Pbuffers are not supported");
   }
}
