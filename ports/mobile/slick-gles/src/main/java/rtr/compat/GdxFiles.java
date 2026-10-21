package rtr.compat;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.io.File;

/** File handles for absolute game paths on every backend. */
public final class GdxFiles {
   private GdxFiles() {
   }

   /**
    * Like {@code Gdx.files.absolute}. The browser backend has no absolute handles, so there the handle reads through
    * java.io, which TeaVM routes to the web build's virtual file system.
    */
   public static FileHandle absolute(String path) {
      return Gdx.app.getType() == Application.ApplicationType.WebGL ? new FileHandle(new File(path)) : Gdx.files.absolute(path);
   }
}
