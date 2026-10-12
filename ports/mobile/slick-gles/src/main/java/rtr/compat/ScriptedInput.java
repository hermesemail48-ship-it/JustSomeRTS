package rtr.compat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import rtr.compat.lwjgl.input.Keyboard;
import rtr.compat.lwjgl.input.Mouse;
import rtr.compat.lwjgl.opengl.Display;

/**
 * Debug aid for automated play-testing (-Drtr.script=file). One command per line, game-pixel coordinates
 * with a top-left origin:
 * <pre>
 * wait 2.5              pause before the next command (seconds)
 * move X Y
 * click X Y             left click (press and release on separate frames)
 * rclick X Y            right click
 * drag B X1 Y1 X2 Y2    hold button B while moving
 * wheel N               mouse-wheel notches (+ zooms in)
 * key NAME              press and release an LWJGL key, e.g. key ESCAPE
 * type TEXT             type characters like a phone keyboard (character events without key codes)
 * shot PATH             save a screenshot
 * exit
 * </pre>
 */
public class ScriptedInput {
   private static final long STEP_MS = 60L;
   private final List<String[]> commands = new ArrayList<String[]>();
   private final List<Runnable> pending = new ArrayList<Runnable>();
   private final List<Long> pendingAt = new ArrayList<Long>();
   private int next;
   private long readyAt;
   private final GdxSlickApp app;

   public ScriptedInput(String path, GdxSlickApp app) throws IOException {
      this.app = app;
      BufferedReader r = new BufferedReader(new FileReader(path));

      try {
         String line;
         while ((line = r.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty() && !line.startsWith("#")) {
               this.commands.add(line.split("\\s+"));
            }
         }
      } finally {
         r.close();
      }
   }

   private static int y(String v) {
      return Display.getHeight() - 1 - Integer.parseInt(v);
   }

   private void at(long t, Runnable r) {
      this.pendingAt.add(t);
      this.pending.add(r);
   }

   public void update() {
      long now = System.currentTimeMillis();

      for (int i = 0; i < this.pending.size(); i++) {
         if (this.pendingAt.get(i) <= now) {
            this.pendingAt.remove(i);
            this.pending.remove(i--).run();
         }
      }

      while (this.pending.isEmpty() && this.next < this.commands.size() && now >= this.readyAt) {
         this.run(this.commands.get(this.next++), now);
      }
   }

   private void run(final String[] c, long now) {
      String cmd = c[0];
      System.out.println("[script] " + java.util.Arrays.toString(c));
      if (cmd.equals("wait")) {
         this.readyAt = now + (long)(Float.parseFloat(c[1]) * 1000.0F);
      } else if (cmd.equals("move")) {
         Mouse.push(-1, false, Integer.parseInt(c[1]), y(c[2]), 0);
      } else if (cmd.equals("click") || cmd.equals("rclick")) {
         final int b = cmd.equals("click") ? 0 : 1;
         final int x = Integer.parseInt(c[1]);
         final int yy = y(c[2]);
         Mouse.push(-1, false, x, yy, 0);
         this.at(now + STEP_MS, () -> Mouse.push(b, true, x, yy, 0));
         this.at(now + 3L * STEP_MS, () -> Mouse.push(b, false, x, yy, 0));
      } else if (cmd.equals("drag")) {
         final int b = Integer.parseInt(c[1]);
         final int x1 = Integer.parseInt(c[2]);
         final int y1 = y(c[3]);
         final int x2 = Integer.parseInt(c[4]);
         final int y2 = y(c[5]);
         Mouse.push(-1, false, x1, y1, 0);
         this.at(now + STEP_MS, () -> Mouse.push(b, true, x1, y1, 0));

         for (int i = 1; i <= 10; i++) {
            final int xi = x1 + (x2 - x1) * i / 10;
            final int yi = y1 + (y2 - y1) * i / 10;
            this.at(now + (1 + i) * STEP_MS, () -> Mouse.push(-1, false, xi, yi, 0));
         }

         this.at(now + 13L * STEP_MS, () -> Mouse.push(b, false, x2, y2, 0));
      } else if (cmd.equals("wheel")) {
         Mouse.push(-1, false, Mouse.getX(), Mouse.getY(), Integer.parseInt(c[1]) * 120);
      } else if (cmd.equals("key")) {
         final int k = Keyboard.getKeyIndex(c[1]);
         Keyboard.push(k, (char)0, true, false);
         this.at(now + 2L * STEP_MS, () -> Keyboard.push(k, (char)0, false, false));
      } else if (cmd.equals("type")) {
         for (int i = 1; i < c.length; i++) {
            String word = (i > 1 ? " " : "") + c[i];

            for (int j = 0; j < word.length(); j++) {
               Keyboard.push(Keyboard.KEY_NONE, word.charAt(j), true, false);
               Keyboard.push(Keyboard.KEY_NONE, word.charAt(j), false, false);
            }
         }
      } else if (cmd.equals("shot")) {
         this.app.saveScreenshot(c[1]);
      } else if (cmd.equals("exit")) {
         System.exit(0);
      } else {
         System.out.println("[script] unknown command " + cmd);
      }
   }
}
