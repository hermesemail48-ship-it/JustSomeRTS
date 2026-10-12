package rtr.utilities;

import com.codedisaster.steamworks.SteamAPI;
import com.codedisaster.steamworks.SteamException;
import com.codedisaster.steamworks.SteamID;
import com.codedisaster.steamworks.SteamLeaderboardEntriesHandle;
import com.codedisaster.steamworks.SteamLeaderboardHandle;
import com.codedisaster.steamworks.SteamPublishedFileID;
import com.codedisaster.steamworks.SteamResult;
import com.codedisaster.steamworks.SteamUGC;
import com.codedisaster.steamworks.SteamUGCCallback;
import com.codedisaster.steamworks.SteamUGCDetails;
import com.codedisaster.steamworks.SteamUGCQuery;
import com.codedisaster.steamworks.SteamUser;
import com.codedisaster.steamworks.SteamUserCallback;
import com.codedisaster.steamworks.SteamUserStats;
import com.codedisaster.steamworks.SteamUserStatsCallback;
import com.codedisaster.steamworks.SteamUtils;
import com.codedisaster.steamworks.SteamUtilsCallback;
import com.codedisaster.steamworks.SteamAuth.AuthSessionResponse;
import com.codedisaster.steamworks.SteamUGC.ItemInstallInfo;
import com.codedisaster.steamworks.SteamUGC.MatchingUGCType;
import com.codedisaster.steamworks.SteamUGC.UserUGCList;
import com.codedisaster.steamworks.SteamUGC.UserUGCListSortOrder;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class Steamworks {
   private static AtomicReference<ArrayList<String>> filePathsRef = new AtomicReference<>();
   private static CountDownLatch startSignal = new CountDownLatch(1);
   private static SteamUser user;
   private static SteamUserStats userStats;
   private static SteamUGC ugc;
   private static SteamUtils utils;
   private static volatile boolean steamLoop;
   private static volatile boolean isRunning;
   private static SteamUGCCallback ugcCallback = new SteamUGCCallback() {
      @Override
      public void onUGCQueryCompleted(SteamUGCQuery query, int numResultsReturned, int totalMatchingResults, boolean isCachedData, SteamResult result) {
         ArrayList<SteamPublishedFileID> returnedUGCQuery = new ArrayList<>();

         for (int i = 0; i < numResultsReturned; i++) {
            SteamUGCDetails details = new SteamUGCDetails();
            Steamworks.ugc.getQueryUGCResult(query, i, details);
            returnedUGCQuery.add(details.getPublishedFileID());
         }

         ArrayList<String> filePaths = new ArrayList<>();

         for (SteamPublishedFileID f : returnedUGCQuery) {
            Long id = Long.parseLong(f.toString(), 16);
            ItemInstallInfo installInfo = new ItemInstallInfo();
            Steamworks.ugc.getItemInstallInfo(new SteamPublishedFileID(id), installInfo);
            filePaths.add(installInfo.getFolder());
         }

         Steamworks.filePathsRef.set(filePaths);
         Steamworks.startSignal.countDown();
         Steamworks.ugc.releaseQueryUserUGCRequest(query);
      }

      @Override
      public void onSubscribeItem(SteamPublishedFileID publishedFileID, SteamResult result) {
         System.out.println("SteamAPI: Subscribe item result: publishedFileID=" + publishedFileID + ", result=" + result);
      }

      @Override
      public void onUnsubscribeItem(SteamPublishedFileID publishedFileID, SteamResult result) {
         System.out.println("SteamAPI: Unsubscribe item result: publishedFileID=" + publishedFileID + ", result=" + result);
      }

      @Override
      public void onRequestUGCDetails(SteamUGCDetails details, SteamResult result) {
         System.out.println("SteamAPI: Request details result: result=" + result);
         this.printUGCDetails("UGC details ", details);
      }

      @Override
      public void onCreateItem(SteamPublishedFileID publishedFileID, boolean needsToAcceptWLA, SteamResult result) {
      }

      @Override
      public void onSubmitItemUpdate(SteamPublishedFileID publishedFileID, boolean needsToAcceptWLA, SteamResult result) {
      }

      @Override
      public void onDownloadItemResult(int appID, SteamPublishedFileID publishedFileID, SteamResult result) {
      }

      @Override
      public void onUserFavoriteItemsListChanged(SteamPublishedFileID publishedFileID, boolean wasAddRequest, SteamResult result) {
      }

      @Override
      public void onSetUserItemVote(SteamPublishedFileID publishedFileID, boolean voteUp, SteamResult result) {
      }

      @Override
      public void onGetUserItemVote(SteamPublishedFileID publishedFileID, boolean votedUp, boolean votedDown, boolean voteSkipped, SteamResult result) {
      }

      private void printUGCDetails(String prefix, SteamUGCDetails details) {
         System.out
            .println(
               prefix
                  + ": publishedFileID="
                  + details.getPublishedFileID().toString()
                  + ", result="
                  + details.getResult().name()
                  + ", type="
                  + details.getFileType().name()
                  + ", title='"
                  + details.getTitle()
                  + "'"
                  + ", description='"
                  + details.getDescription()
                  + "'"
                  + ", tags='"
                  + details.getTags()
                  + "'"
                  + ", fileName="
                  + details.getFileName()
                  + ", fileHandle="
                  + details.getFileHandle().toString()
                  + ", previewFileHandle="
                  + details.getPreviewFileHandle().toString()
                  + ", url="
                  + details.getURL()
            );
      }

      @Override
      public void onStartPlaytimeTracking(SteamResult result) {
      }

      @Override
      public void onStopPlaytimeTracking(SteamResult result) {
      }

      @Override
      public void onStopPlaytimeTrackingForAllItems(SteamResult result) {
      }

      @Override
      public void onDeleteItem(SteamPublishedFileID publishedFileID, SteamResult result) {
      }
   };
   private static SteamUtilsCallback utilsCallback = new SteamUtilsCallback() {
      @Override
      public void onSteamShutdown() {
         System.out.println("SteamAPI: Steam client wants to shut down!");
      }
   };
   private static SteamUserCallback userCallback = new SteamUserCallback() {
      @Override
      public void onValidateAuthTicket(SteamID steamID, AuthSessionResponse authSessionResponse, SteamID ownerSteamID) {
      }

      @Override
      public void onMicroTxnAuthorization(int appID, long orderID, boolean authorized) {
      }

      @Override
      public void onEncryptedAppTicket(SteamResult result) {
      }
   };
   private static SteamUserStatsCallback userStatsCallback = new SteamUserStatsCallback() {
      @Override
      public void onGlobalStatsReceived(long arg0, SteamResult arg1) {
      }

      @Override
      public void onLeaderboardFindResult(SteamLeaderboardHandle arg0, boolean arg1) {
      }

      @Override
      public void onLeaderboardScoreUploaded(boolean arg0, SteamLeaderboardHandle arg1, int arg2, boolean arg3, int arg4, int arg5) {
      }

      @Override
      public void onLeaderboardScoresDownloaded(SteamLeaderboardHandle arg0, SteamLeaderboardEntriesHandle arg1, int arg2) {
      }

      @Override
      public void onUserAchievementStored(long arg0, boolean arg1, String arg2, int arg3, int arg4) {
      }

      @Override
      public void onUserStatsReceived(long arg0, SteamID arg1, SteamResult arg2) {
      }

      @Override
      public void onUserStatsStored(long arg0, SteamResult arg1) {
      }

      @Override
      public void onUserStatsUnloaded(SteamID arg0) {
      }
   };

   public static void runCallbacks() {
      SteamAPI.runCallbacks();
   }

   public static void shutdownApi() {
      SteamAPI.shutdown();
   }

   public static boolean isRunning() {
      return isRunning;
   }

   public static void initializeSteam() {
      try {
         SteamAPI.loadLibraries("lib/natives");
         isRunning = SteamAPI.init();
         if (!isRunning) {
            return;
         }

         user = new SteamUser(userCallback);
         userStats = new SteamUserStats(userStatsCallback);
         ugc = new SteamUGC(ugcCallback);
         utils = new SteamUtils(utilsCallback);
         steamLoop = true;
         Thread callbackThread = new Thread() {
            @Override
            public void run() {
               while (Steamworks.steamLoop) {
                  SteamAPI.runCallbacks();

                  try {
                     Thread.sleep(10L);
                  } catch (InterruptedException var2) {
                     var2.printStackTrace();
                  }
               }
            }
         };
         callbackThread.start();
      } catch (SteamException var1) {
         var1.printStackTrace();
      }
   }

   public static void shutdownSteam() {
      if (isRunning) {
         steamLoop = false;
         System.out.println("SteamAPI: Shutting down Steam API.");
         SteamAPI.shutdown();
      } else {
         System.out.println("SteamAPI: Attempted to shutdown Steam API, but it was not on.");
      }
   }

   public static void grantAchievement(String achievementName, boolean silent) {
      if (isRunning) {
         userStats.requestCurrentStats();

         try {
            boolean isAchieved = userStats.isAchieved(achievementName, false);
            if (isAchieved) {
               if (!silent) {
                  System.out.println("SteamAPI: Achievement Already Unlocked: " + achievementName);
               }

               return;
            }

            userStats.setAchievement(achievementName);
            boolean isStored = userStats.storeStats();
            if (!silent) {
               if (isStored) {
                  System.out.println("SteamAPI: Achievement Unlocked: " + achievementName);
               } else {
                  System.out.println("SteamAPI: Achievement Fail To Unlocked: " + achievementName);
               }
            }
         } catch (Exception var4) {
            System.out.println("SteamAPI: Achievement Threw Exception Trying To Unlocked: " + achievementName);
         }
      }
   }

   public static void clearAchievement(String achievementName, boolean silent) {
      if (isRunning) {
         userStats.requestCurrentStats();

         try {
            boolean isAchieved = userStats.isAchieved(achievementName, false);
            if (isAchieved) {
               boolean isCleared = userStats.clearAchievement(achievementName);
               if (!silent) {
                  if (isCleared) {
                     System.out.println("SteamAPI: Achievement Cleared: " + achievementName);
                  } else {
                     System.out.println("SteamAPI: Achievement Not Cleared: " + achievementName);
                  }
               }
            } else if (!silent) {
               System.out.println("SteamAPI: Achievement Not Unlocked: " + achievementName);
            }
         } catch (Exception var4) {
            System.out.println("SteamAPI: Achievement Threw Exception Trying To Clear: " + achievementName);
         }
      }
   }

   public static ArrayList<String> getAndRefreshLoadedLanguages() {
      if (!isRunning) {
         return new ArrayList<>();
      } else {
         SteamUGCQuery query = ugc.createQueryUserUGCRequest(
            user.getSteamID().getAccountID(),
            UserUGCList.Subscribed,
            MatchingUGCType.UsableInGame,
            UserUGCListSortOrder.TitleAsc,
            utils.getAppID(),
            utils.getAppID(),
            1
         );
         if (query.isValid()) {
            ugc.sendQueryUGCRequest(query);
         }

         try {
            startSignal.await();
         } catch (InterruptedException var2) {
            var2.printStackTrace();
         }

         return filePathsRef.get();
      }
   }
}
