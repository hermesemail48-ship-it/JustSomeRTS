package rtr.utilities;

import com.codedisaster.steamworks.SteamAPI;
import com.codedisaster.steamworks.SteamAPIWarningMessageHook;
import com.codedisaster.steamworks.SteamException;
import com.codedisaster.steamworks.SteamUtils;
import com.codedisaster.steamworks.SteamUtilsCallback;
import java.util.Scanner;

public abstract class SteamTestApp {
   protected SteamUtils clientUtils;
   protected static final int MS_PER_TICK = 66;
   private SteamAPIWarningMessageHook clMessageHook = new SteamAPIWarningMessageHook() {
      @Override
      public void onWarningMessage(int severity, String message) {
         System.err.println("[client debug message] (" + severity + ") " + message);
      }
   };
   private SteamUtilsCallback clUtilsCallback = new SteamUtilsCallback() {
      @Override
      public void onSteamShutdown() {
         System.err.println("Steam client requested to shut down!");
      }
   };

   protected abstract void registerInterfaces() throws SteamException;

   protected abstract void unregisterInterfaces() throws SteamException;

   protected abstract void processUpdate() throws SteamException;

   protected abstract void processInput(String var1) throws SteamException;

   private boolean runAsClient(String[] arguments) throws SteamException {
      System.out.println("Load native libraries ...");
      SteamAPI.loadLibraries();
      System.out.println("Initialise Steam client API ...");
      if (!SteamAPI.init()) {
         SteamAPI.printDebugInfo(System.err);
         return false;
      } else {
         SteamAPI.printDebugInfo(System.out);
         this.registerInterfaces();
         this.clientUtils = new SteamUtils(this.clUtilsCallback);
         this.clientUtils.setWarningMessageHook(this.clMessageHook);
         if (SteamAPI.restartAppIfNecessary(this.clientUtils.getAppID())) {
            System.out.println("SteamAPI_RestartAppIfNecessary returned 'false'");
         }

         SteamTestApp.InputHandler inputHandler = new SteamTestApp.InputHandler(Thread.currentThread());
         Thread inputThread = new Thread(inputHandler);
         inputThread.start();

         while (inputHandler.alive() && Steamworks.isRunning()) {
            SteamAPI.runCallbacks();
            this.processUpdate();

            try {
               Thread.sleep(66L);
            } catch (InterruptedException var6) {
            }
         }

         System.out.println("Shutting down Steam client API ...");

         try {
            inputThread.join();
         } catch (InterruptedException var5) {
            throw new SteamException(var5);
         }

         this.clientUtils.dispose();
         this.unregisterInterfaces();
         SteamAPI.shutdown();
         return true;
      }
   }

   private class InputHandler implements Runnable {
      private volatile boolean alive = true;
      private Thread mainThread;
      private Scanner scanner;

      public InputHandler(Thread mainThread) {
         this.mainThread = mainThread;
         this.scanner = new Scanner(System.in, "UTF-8");
         this.scanner.useDelimiter("[\r\n\t]");
      }

      @Override
      public void run() {
         try {
            while (this.alive && this.mainThread.isAlive()) {
               if (this.scanner.hasNext()) {
                  String input = this.scanner.next();
                  if (!input.equals("quit") && !input.equals("exit")) {
                     try {
                        SteamTestApp.this.processInput(input);
                     } catch (NumberFormatException var3) {
                        var3.printStackTrace();
                     }
                  } else {
                     this.alive = false;
                  }
               }
            }
         } catch (SteamException var4) {
            var4.printStackTrace();
         }
      }

      public boolean alive() {
         return this.alive;
      }
   }
}
