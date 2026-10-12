package rtr.system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.lwjgl.LWJGLException;
import org.newdawn.slick.SlickException;

public class Launcher {
   private static String logPath;

   public static void main(String[] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, FileNotFoundException, LWJGLException, SlickException {
      String inEclipseStr = System.getProperty("runInEclipse");
      boolean inEclipse = Boolean.parseBoolean(inEclipseStr);
      if (!inEclipse) {
         File logsFolder = new File("logs/");
         if (!logsFolder.exists()) {
            logsFolder.mkdirs();
         }

         DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
         Date date = new Date();
         File[] listOfFiles = logsFolder.listFiles();

         for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() && date.getTime() - listOfFiles[i].lastModified() > 1209600000L) {
               listOfFiles[i].delete();
            }
         }

         Runtime.getRuntime().addShutdownHook(new ShutdownHook());
         logPath = "logs/" + dateFormat.format(date) + ".txt";
         PrintStream out = new PrintStream(new FileOutputStream(logPath));
         System.setOut(out);
         System.setErr(out);
      }

      String nativesFolder = new File("lib/natives").getAbsolutePath();
      String jarFolder = new File("lib/jar").getAbsolutePath();
      System.setProperty("java.library.path", nativesFolder + ";" + jarFolder + ";" + System.getProperty("java.library.path"));
      System.setProperty("org.lwjgl.librarypath", nativesFolder);
      System.setProperty("Dcom.codedisaster.steamworks.SharedLibraryExtractPath", nativesFolder);
      System.setProperty("dcom.codedisaster.steamworks.SharedLibraryExtractPath", nativesFolder);
      Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
      sysPathsField.setAccessible(true);
      sysPathsField.set(null, null);
      Game.launchGame();
   }

   public static String getLogPath() {
      return logPath;
   }
}
