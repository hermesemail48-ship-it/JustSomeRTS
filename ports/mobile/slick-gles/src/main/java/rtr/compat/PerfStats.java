package rtr.compat;

/**
 * Per-frame performance counters for the overlay (three-finger tap / F3) and the periodic log line.
 * All counters are touched only on the render thread.
 */
public final class PerfStats {
   public static int drawCalls;
   public static int vertices;
   public static int textureSwitches;
   public static int stateFlushes;
   public static long updateNanos;
   public static long renderNanos;

   // Smoothed values shown on screen (updated twice a second).
   public static float fps;
   public static float avgDrawCalls;
   public static float avgVertices;
   public static float avgTextureSwitches;
   public static float avgUpdateMs;
   public static float avgRenderMs;
   public static float avgFrameMs;
   public static float worstFrameMs;
   public static int updatesPerFrame;

   private static long windowStart = System.nanoTime();
   private static long lastFrame = System.nanoTime();
   private static int frames;
   private static long sumDraw;
   private static long sumVerts;
   private static long sumTex;
   private static long sumUpdate;
   private static long sumRender;
   private static long sumFrame;
   private static long worstFrame;
   private static long lastLog;
   public static boolean logToConsole = Boolean.getBoolean("rtr.perf");

   private PerfStats() {
   }

   /** Called at the very start of each frame. */
   public static void beginFrame() {
      drawCalls = 0;
      vertices = 0;
      textureSwitches = 0;
      stateFlushes = 0;
      updateNanos = 0L;
      renderNanos = 0L;
      updatesPerFrame = 0;
   }

   /** Called at the end of each frame. */
   public static void endFrame() {
      long now = System.nanoTime();
      long frame = now - lastFrame;
      lastFrame = now;
      frames++;
      sumDraw += drawCalls;
      sumVerts += vertices;
      sumTex += textureSwitches;
      sumUpdate += updateNanos;
      sumRender += renderNanos;
      sumFrame += frame;
      worstFrame = Math.max(worstFrame, frame);
      long window = now - windowStart;
      if (window >= 500000000L) {
         fps = frames * 1.0E9F / window;
         avgDrawCalls = (float)sumDraw / frames;
         avgVertices = (float)sumVerts / frames;
         avgTextureSwitches = (float)sumTex / frames;
         avgUpdateMs = sumUpdate / 1.0E6F / frames;
         avgRenderMs = sumRender / 1.0E6F / frames;
         avgFrameMs = sumFrame / 1.0E6F / frames;
         worstFrameMs = worstFrame / 1.0E6F;
         frames = 0;
         sumDraw = sumVerts = sumTex = sumUpdate = sumRender = sumFrame = worstFrame = 0L;
         windowStart = now;
         if (logToConsole && now - lastLog > 5000000000L) {
            lastLog = now;
            System.out.println(summary());
         }
      }
   }

   public static String summary() {
      Runtime rt = Runtime.getRuntime();
      long usedMb = (rt.totalMemory() - rt.freeMemory()) >> 20;
      return String.format(
         "[perf] %.0f fps | frame %.1f ms (worst %.0f) | update %.1f ms | render %.1f ms | draws %.0f | tex %.0f | verts %.0f | heap %d MB",
         fps, avgFrameMs, worstFrameMs, avgUpdateMs, avgRenderMs, avgDrawCalls, avgTextureSwitches, avgVertices, usedMb
      );
   }
}
