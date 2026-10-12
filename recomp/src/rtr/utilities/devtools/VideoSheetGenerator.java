package rtr.utilities.devtools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.imageout.ImageOut;

public class VideoSheetGenerator {
   private static final int MAXIMUM_TEXTURE_SIZE = 1024;

   public static void buildSpriteSheet(String fileName) throws SlickException {
      ArrayList<Image> frames = new ArrayList<>();
      String path = "res/video/newSequence/" + fileName + "0000.jpg";
      File image = new File(path);

      for (int count = 0; image.exists(); image = new File(path)) {
         frames.add(new Image(path));
         path = "res/video/newSequence/" + fileName + String.format("%04d", ++count) + ".jpg";
      }

      System.out.println("Loaded Sequence with " + frames.size() + " frames.");
      Image output = null;
      if ((frames.size() / 4 + 1) * frames.get(0).getHeight() <= 1024) {
         output = new Image(1024, (frames.size() / 4 + 1) * frames.get(0).getHeight());
      } else {
         output = new Image(1024, 1024);
      }

      Graphics g = output.getGraphics();
      int positionX = 0;
      int positionY = 0;
      int frameWidth = frames.get(0).getWidth();
      int frameHeight = frames.get(0).getHeight();
      int outputPage = 0;

      for (int x = 0; x < frames.size(); x++) {
         Image i = frames.get(x);
         g.drawImage(i, positionX * frameWidth, positionY * frameHeight);
         if (++positionX * frameWidth >= 1024) {
            positionX = 0;
            positionY++;
         }

         if (positionY * frameWidth >= 1024 || x == frames.size() - 1) {
            File folder = new File("res/video/newSequence/" + fileName + "/");
            folder.mkdirs();
            ImageOut.write(output, "res/video/newSequence/" + fileName + "/" + fileName + outputPage + ".png");

            try {
               Thread.sleep(10L);
            } catch (InterruptedException var18) {
               var18.printStackTrace();
            }

            System.gc();

            try {
               Thread.sleep(10L);
            } catch (InterruptedException var17) {
               var17.printStackTrace();
            }

            g.flush();
            if (((frames.size() - x) / 4 + 1) * frames.get(0).getHeight() <= 1024) {
               output = new Image(1024, ((frames.size() - x) / 4 + 1) * frames.get(0).getHeight());
            } else {
               output = new Image(1024, 1024);
            }

            g = output.getGraphics();
            positionX = 0;
            positionY = 0;
            outputPage++;
         }
      }

      Properties properties = new Properties();
      properties.setProperty("frameCount", String.valueOf(frames.size()));

      try {
         FileOutputStream propertiesOut = new FileOutputStream("res/video/newSequence/" + fileName + "/" + fileName + ".properties");
         properties.store(propertiesOut, "Video Frame Data");
         propertiesOut.flush();
         propertiesOut.close();
      } catch (IOException var16) {
         var16.printStackTrace();
      }

      output.destroy();
      output = null;
      g.flush();
      System.out.println("Video Sheet regenerated.");
   }
}
