package rtr.utilities;

import com.codedisaster.steamworks.SteamAPICall;
import com.codedisaster.steamworks.SteamApps;
import com.codedisaster.steamworks.SteamException;
import com.codedisaster.steamworks.SteamFriends;
import com.codedisaster.steamworks.SteamFriendsCallback;
import com.codedisaster.steamworks.SteamID;
import com.codedisaster.steamworks.SteamLeaderboardEntriesHandle;
import com.codedisaster.steamworks.SteamLeaderboardEntry;
import com.codedisaster.steamworks.SteamLeaderboardHandle;
import com.codedisaster.steamworks.SteamPublishedFileID;
import com.codedisaster.steamworks.SteamPublishedFileUpdateHandle;
import com.codedisaster.steamworks.SteamRemoteStorage;
import com.codedisaster.steamworks.SteamRemoteStorageCallback;
import com.codedisaster.steamworks.SteamResult;
import com.codedisaster.steamworks.SteamUGC;
import com.codedisaster.steamworks.SteamUGCCallback;
import com.codedisaster.steamworks.SteamUGCDetails;
import com.codedisaster.steamworks.SteamUGCFileWriteStreamHandle;
import com.codedisaster.steamworks.SteamUGCHandle;
import com.codedisaster.steamworks.SteamUGCQuery;
import com.codedisaster.steamworks.SteamUser;
import com.codedisaster.steamworks.SteamUserCallback;
import com.codedisaster.steamworks.SteamUserStats;
import com.codedisaster.steamworks.SteamUserStatsCallback;
import com.codedisaster.steamworks.SteamUtils;
import com.codedisaster.steamworks.SteamUtilsCallback;
import com.codedisaster.steamworks.SteamAuth.AuthSessionResponse;
import com.codedisaster.steamworks.SteamFriends.PersonaChange;
import com.codedisaster.steamworks.SteamRemoteStorage.PublishedFileVisibility;
import com.codedisaster.steamworks.SteamRemoteStorage.UGCReadAction;
import com.codedisaster.steamworks.SteamRemoteStorage.WorkshopFileType;
import com.codedisaster.steamworks.SteamUGC.ItemDownloadInfo;
import com.codedisaster.steamworks.SteamUGC.ItemInstallInfo;
import com.codedisaster.steamworks.SteamUGC.ItemState;
import com.codedisaster.steamworks.SteamUGC.MatchingUGCType;
import com.codedisaster.steamworks.SteamUGC.UserUGCList;
import com.codedisaster.steamworks.SteamUGC.UserUGCListSortOrder;
import com.codedisaster.steamworks.SteamUserStats.LeaderboardDataRequest;
import com.codedisaster.steamworks.SteamUserStats.LeaderboardUploadScoreMethod;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Collection;

public class SteamClientAPITest extends SteamTestApp {
   private SteamUser user;
   private SteamUserStats userStats;
   private SteamRemoteStorage remoteStorage;
   private SteamUGC ugc;
   private SteamUtils utils;
   private SteamApps apps;
   private SteamFriends friends;
   private SteamLeaderboardHandle currentLeaderboard = null;
   private SteamUserCallback userCallback = new SteamUserCallback() {
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
   private SteamUserStatsCallback userStatsCallback = new SteamUserStatsCallback() {
      @Override
      public void onUserStatsReceived(long gameId, SteamID steamIDUser, SteamResult result) {
         System.out.println("User stats received: gameId=" + gameId + ", userId=" + steamIDUser.getAccountID() + ", result=" + result.toString());
         int numAchievements = SteamClientAPITest.this.userStats.getNumAchievements();
         System.out.println("Num of achievements: " + numAchievements);

         for (int i = 0; i < numAchievements; i++) {
            String name = SteamClientAPITest.this.userStats.getAchievementName(i);
            boolean achieved = SteamClientAPITest.this.userStats.isAchieved(name, false);
            System.out.println("# " + i + " : name=" + name + ", achieved=" + (achieved ? "yes" : "no"));
         }
      }

      @Override
      public void onUserStatsStored(long gameId, SteamResult result) {
         System.out.println("User stats stored: gameId=" + gameId + ", result=" + result.toString());
      }

      @Override
      public void onUserStatsUnloaded(SteamID steamIDUser) {
         System.out.println("User stats unloaded: userId=" + steamIDUser.getAccountID());
      }

      @Override
      public void onUserAchievementStored(long gameId, boolean isGroupAchievement, String achievementName, int curProgress, int maxProgress) {
         System.out.println("User achievement stored: gameId=" + gameId + ", name=" + achievementName + ", progress=" + curProgress + "/" + maxProgress);
      }

      @Override
      public void onLeaderboardFindResult(SteamLeaderboardHandle leaderboard, boolean found) {
         System.out.println("Leaderboard find result: handle=" + leaderboard.toString() + ", found=" + (found ? "yes" : "no"));
         if (found) {
            System.out
               .println(
                  "Leaderboard: name="
                     + SteamClientAPITest.this.userStats.getLeaderboardName(leaderboard)
                     + ", entries="
                     + SteamClientAPITest.this.userStats.getLeaderboardEntryCount(leaderboard)
               );
            SteamClientAPITest.this.currentLeaderboard = leaderboard;
         }
      }

      @Override
      public void onLeaderboardScoresDownloaded(SteamLeaderboardHandle leaderboard, SteamLeaderboardEntriesHandle entries, int numEntries) {
         System.out.println("Leaderboard scores downloaded: handle=" + leaderboard.toString() + ", entries=" + entries.toString() + ", count=" + numEntries);
         int[] details = new int[16];

         for (int i = 0; i < numEntries; i++) {
            SteamLeaderboardEntry entry = new SteamLeaderboardEntry();
            if (SteamClientAPITest.this.userStats.getDownloadedLeaderboardEntry(entries, i, entry, details)) {
               int numDetails = entry.getNumDetails();
               System.out
                  .println(
                     "Leaderboard entry #"
                        + i
                        + ": accountID="
                        + entry.getSteamIDUser().getAccountID()
                        + ", globalRank="
                        + entry.getGlobalRank()
                        + ", score="
                        + entry.getScore()
                        + ", numDetails="
                        + numDetails
                  );

               for (int detail = 0; detail < numDetails; detail++) {
                  System.out.println("  ... detail #" + detail + "=" + details[detail]);
               }

               if (SteamClientAPITest.this.friends.requestUserInformation(entry.getSteamIDUser(), false)) {
                  System.out.println("  ... requested user information for entry");
               } else {
                  System.out.println("  ... user name is '" + SteamClientAPITest.this.friends.getFriendPersonaName(entry.getSteamIDUser()) + "'");
                  int smallAvatar = SteamClientAPITest.this.friends.getSmallFriendAvatar(entry.getSteamIDUser());
                  if (smallAvatar != 0) {
                     int w = SteamClientAPITest.this.utils.getImageWidth(smallAvatar);
                     int h = SteamClientAPITest.this.utils.getImageHeight(smallAvatar);
                     System.out.println("  ... small avatar size: " + w + "x" + h + " pixels");
                     ByteBuffer image = ByteBuffer.allocateDirect(w * h * 4);

                     try {
                        if (!SteamClientAPITest.this.utils.getImageRGBA(smallAvatar, image)) {
                           System.out.println("  ... small avatar retrieve avatar image failed!");
                        } else {
                           System.out.println("  ... small avatar retrieve avatar image successful");
                           int nonZeroes = w * h;

                           for (int y = 0; y < h; y++) {
                              for (int x = 0; x < w; x++) {
                                 if (image.getInt(y * w + x) == 0) {
                                    nonZeroes--;
                                 }
                              }
                           }

                           if (nonZeroes == 0) {
                              System.err.println("Something's wrong! Avatar image is empty!");
                           }
                        }
                     } catch (SteamException var15) {
                        var15.printStackTrace();
                     }
                  } else {
                     System.out.println("  ... small avatar image not available!");
                  }
               }
            }
         }
      }

      @Override
      public void onLeaderboardScoreUploaded(
         boolean success, SteamLeaderboardHandle leaderboard, int score, boolean scoreChanged, int globalRankNew, int globalRankPrevious
      ) {
         System.out
            .println(
               "Leaderboard score uploaded: "
                  + (success ? "yes" : "no")
                  + ", handle="
                  + leaderboard.toString()
                  + ", score="
                  + score
                  + ", changed="
                  + (scoreChanged ? "yes" : "no")
                  + ", globalRankNew="
                  + globalRankNew
                  + ", globalRankPrevious="
                  + globalRankPrevious
            );
      }

      @Override
      public void onGlobalStatsReceived(long gameId, SteamResult result) {
         System.out.println("Global stats received: gameId=" + gameId + ", result=" + result.toString());
      }
   };
   private SteamRemoteStorageCallback remoteStorageCallback = new SteamRemoteStorageCallback() {
      @Override
      public void onFileWriteAsyncComplete(SteamResult result) {
      }

      @Override
      public void onFileReadAsyncComplete(SteamAPICall fileReadAsync, SteamResult result, int offset, int read) {
      }

      @Override
      public void onFileShareResult(SteamUGCHandle fileHandle, String fileName, SteamResult result) {
         System.out.println("Remote storage file share result: handle='" + fileHandle.toString() + ", name=" + fileName + "', result=" + result.toString());
      }

      @Override
      public void onDownloadUGCResult(SteamUGCHandle fileHandle, SteamResult result) {
         System.out.println("Remote storage download UGC result: handle='" + fileHandle.toString() + "', result=" + result.toString());
         ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
         int offset = 0;

         int bytesRead;
         do {
            bytesRead = SteamClientAPITest.this.remoteStorage.ugcRead(fileHandle, buffer, buffer.limit(), offset, UGCReadAction.ContinueReadingUntilFinished);
            offset += bytesRead;
         } while (bytesRead > 0);

         System.out.println("Read " + offset + " bytes from handle=" + fileHandle.toString());
      }

      @Override
      public void onPublishFileResult(SteamPublishedFileID publishedFileID, boolean needsToAcceptWLA, SteamResult result) {
         System.out
            .println(
               "Remote storage publish file result: publishedFileID="
                  + publishedFileID.toString()
                  + ", needsToAcceptWLA="
                  + needsToAcceptWLA
                  + ", result="
                  + result.toString()
            );
      }

      @Override
      public void onUpdatePublishedFileResult(SteamPublishedFileID publishedFileID, boolean needsToAcceptWLA, SteamResult result) {
         System.out
            .println(
               "Remote storage update published file result: publishedFileID="
                  + publishedFileID.toString()
                  + ", needsToAcceptWLA="
                  + needsToAcceptWLA
                  + ", result="
                  + result.toString()
            );
      }

      @Override
      public void onPublishedFileSubscribed(SteamPublishedFileID publishedFileID, int appID) {
      }

      @Override
      public void onPublishedFileUnsubscribed(SteamPublishedFileID publishedFileID, int appID) {
      }

      @Override
      public void onPublishedFileDeleted(SteamPublishedFileID publishedFileID, int appID) {
      }
   };
   private SteamUGCCallback ugcCallback = new SteamUGCCallback() {
      @Override
      public void onUGCQueryCompleted(SteamUGCQuery query, int numResultsReturned, int totalMatchingResults, boolean isCachedData, SteamResult result) {
         System.out
            .println(
               "UGC query completed: handle="
                  + query.toString()
                  + ", "
                  + numResultsReturned
                  + " of "
                  + totalMatchingResults
                  + " results returned, result="
                  + result.toString()
            );

         for (int i = 0; i < numResultsReturned; i++) {
            SteamUGCDetails details = new SteamUGCDetails();
            SteamClientAPITest.this.ugc.getQueryUGCResult(query, i, details);
            this.printUGCDetails("UGC details #" + i, details);
         }

         SteamClientAPITest.this.ugc.releaseQueryUserUGCRequest(query);
      }

      @Override
      public void onSubscribeItem(SteamPublishedFileID publishedFileID, SteamResult result) {
         System.out.println("Subscribe item result: publishedFileID=" + publishedFileID + ", result=" + result);
      }

      @Override
      public void onUnsubscribeItem(SteamPublishedFileID publishedFileID, SteamResult result) {
         System.out.println("Unsubscribe item result: publishedFileID=" + publishedFileID + ", result=" + result);
      }

      @Override
      public void onRequestUGCDetails(SteamUGCDetails details, SteamResult result) {
         System.out.println("Request details result: result=" + result);
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
   private SteamFriendsCallback friendsCallback = new SteamFriendsCallback() {
      @Override
      public void onSetPersonaNameResponse(boolean success, boolean localSuccess, SteamResult result) {
      }

      @Override
      public void onPersonaStateChange(SteamID steamID, PersonaChange change) {
         switch (change) {
            case Name:
               System.out
                  .println(
                     "Persona name received: accountID="
                        + steamID.getAccountID()
                        + ", name='"
                        + SteamClientAPITest.this.friends.getFriendPersonaName(steamID)
                        + "'"
                  );
               break;
            default:
               System.out.println("Persona state changed (unhandled): accountID=" + steamID.getAccountID() + ", change=" + change.name());
         }
      }

      @Override
      public void onGameOverlayActivated(boolean active) {
      }

      @Override
      public void onGameLobbyJoinRequested(SteamID steamIDLobby, SteamID steamIDFriend) {
      }

      @Override
      public void onAvatarImageLoaded(SteamID steamID, int image, int width, int height) {
      }

      @Override
      public void onFriendRichPresenceUpdate(SteamID steamIDFriend, int appID) {
      }

      @Override
      public void onGameRichPresenceJoinRequested(SteamID steamIDFriend, String connect) {
      }

      @Override
      public void onGameServerChangeRequested(String server, String password) {
      }
   };
   private SteamUtilsCallback utilsCallback = new SteamUtilsCallback() {
      @Override
      public void onSteamShutdown() {
         System.out.println("Steam client wants to shut down!");
      }
   };

   @Override
   protected void registerInterfaces() {
      System.out.println("Register user ...");
      this.user = new SteamUser(this.userCallback);
      System.out.println("Register user stats callback ...");
      this.userStats = new SteamUserStats(this.userStatsCallback);
      System.out.println("Register remote storage ...");
      this.remoteStorage = new SteamRemoteStorage(this.remoteStorageCallback);
      System.out.println("Register UGC ...");
      this.ugc = new SteamUGC(this.ugcCallback);
      System.out.println("Register Utils ...");
      this.utils = new SteamUtils(this.utilsCallback);
      System.out.println("Register Apps ...");
      this.apps = new SteamApps();
      System.out.println("Register Friends ...");
      this.friends = new SteamFriends(this.friendsCallback);
      System.out.println("Local user account ID: " + this.user.getSteamID().getAccountID());
      System.out.println("Local user steam ID: " + SteamID.getNativeHandle(this.user.getSteamID()));
      System.out.println("Local user friends name: " + this.friends.getPersonaName());
      System.out.println("App ID: " + this.utils.getAppID());
      System.out.println("App build ID: " + this.apps.getAppBuildId());
      System.out.println("App owner: " + this.apps.getAppOwner().getAccountID());
      System.out.println("Current game language: " + this.apps.getCurrentGameLanguage());
      System.out.println("Available game languages: " + this.apps.getAvailableGameLanguages());
   }

   @Override
   protected void unregisterInterfaces() {
      this.user.dispose();
      this.userStats.dispose();
      this.remoteStorage.dispose();
      this.ugc.dispose();
      this.utils.dispose();
      this.apps.dispose();
      this.friends.dispose();
   }

   @Override
   protected void processUpdate() throws SteamException {
   }

   // $VF: Could not inline inconsistent finally blocks
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   protected void processInput(String input) throws SteamException {
      if (input.startsWith("stats global ")) {
         String[] cmd = input.substring("stats global ".length()).split(" ");
         if (cmd.length > 0) {
            if (cmd[0].equals("request")) {
               int days = 0;
               if (cmd.length > 1) {
                  days = Integer.parseInt(cmd[1]);
               }

               this.userStats.requestGlobalStats(days);
            } else if (cmd[0].equals("lget") && cmd.length > 1) {
               int days = 0;
               if (cmd.length > 2) {
                  days = Integer.parseInt(cmd[2]);
               }

               if (days == 0) {
                  long value = this.userStats.getGlobalStat(cmd[1], -1L);
                  System.out.println("global stat (L) '" + cmd[1] + "' = " + value);
               } else {
                  long[] data = new long[days];
                  int count = this.userStats.getGlobalStatHistory(cmd[1], data);
                  System.out.print("global stat history (L) for " + count + " of " + days + " days:");

                  for (int i = 0; i < count; i++) {
                     System.out.print(" " + Long.toString(data[i]));
                  }

                  System.out.println();
               }
            } else if (cmd[0].equals("dget") && cmd.length > 1) {
               int daysx = 0;
               if (cmd.length > 2) {
                  daysx = Integer.parseInt(cmd[2]);
               }

               if (daysx == 0) {
                  double value = this.userStats.getGlobalStat(cmd[1], -1.0);
                  System.out.println("global stat (D) '" + cmd[1] + "' = " + value);
               } else {
                  double[] data = new double[daysx];
                  int count = this.userStats.getGlobalStatHistory(cmd[1], data);
                  System.out.print("global stat history (D) for " + count + " of " + daysx + " days:");

                  for (int i = 0; i < count; i++) {
                     System.out.print(" " + Double.toString(data[i]));
                  }

                  System.out.println();
               }
            }
         }
      } else if (input.equals("stats request")) {
         this.userStats.requestCurrentStats();
      } else if (input.equals("stats store")) {
         this.userStats.storeStats();
      } else if (input.startsWith("achievement set ")) {
         String achievementName = input.substring("achievement set ".length());
         System.out.println("- setting " + achievementName + " to 'achieved'");
         this.userStats.setAchievement(achievementName);
      } else if (input.startsWith("achievement clear ")) {
         String achievementName = input.substring("achievement clear ".length());
         System.out.println("- clearing " + achievementName);
         this.userStats.clearAchievement(achievementName);
      } else if (input.equals("file list")) {
         int numFiles = this.remoteStorage.getFileCount();
         System.out.println("Num of files: " + numFiles);

         for (int i = 0; i < numFiles; i++) {
            int[] sizes = new int[1];
            String file = this.remoteStorage.getFileNameAndSize(i, sizes);
            boolean exists = this.remoteStorage.fileExists(file);
            System.out.println("# " + i + " : name=" + file + ", size=" + sizes[0] + ", exists=" + (exists ? "yes" : "no"));
         }
      } else if (input.startsWith("file write ")) {
         String path = input.substring("file write ".length());
         File file = new File(path);

         try (FileInputStream in = new FileInputStream(file)) {
            SteamUGCFileWriteStreamHandle remoteFile = this.remoteStorage.fileWriteStreamOpen(path);
            if (remoteFile != null) {
               byte[] bytes = new byte[1024];

               int bytesRead;
               while ((bytesRead = in.read(bytes, 0, bytes.length)) > 0) {
                  ByteBuffer buffer = ByteBuffer.allocateDirect(bytesRead);
                  buffer.put(bytes, 0, bytesRead);
                  ((Buffer)buffer).flip();
                  this.remoteStorage.fileWriteStreamWriteChunk(remoteFile, buffer);
               }

               this.remoteStorage.fileWriteStreamClose(remoteFile);
            }
         } catch (IOException var19) {
            var19.printStackTrace();
         }
      } else if (input.startsWith("file delete ")) {
         String path = input.substring("file delete ".length());
         if (this.remoteStorage.fileDelete(path)) {
            System.out.println("deleted file '" + path + "'");
         }
      } else if (input.startsWith("file share ")) {
         this.remoteStorage.fileShare(input.substring("file share ".length()));
      } else if (input.startsWith("file publish ")) {
         String[] paths = input.substring("file publish ".length()).split(" ");
         if (paths.length >= 2) {
            System.out.println("publishing file: " + paths[0] + ", preview file: " + paths[1]);
            this.remoteStorage
               .publishWorkshopFile(
                  paths[0],
                  paths[1],
                  this.utils.getAppID(),
                  "Test UGC!",
                  "Dummy UGC file published by test application.",
                  PublishedFileVisibility.Private,
                  null,
                  WorkshopFileType.Community
               );
         }
      } else if (input.startsWith("file republish ")) {
         String[] paths = input.substring("file republish ".length()).split(" ");
         if (paths.length >= 3) {
            System.out.println("republishing id: " + paths[0] + ", file: " + paths[1] + ", preview file: " + paths[2]);
            SteamPublishedFileID fileID = new SteamPublishedFileID(Long.parseLong(paths[0]));
            SteamPublishedFileUpdateHandle updateHandle = this.remoteStorage.createPublishedFileUpdateRequest(fileID);
            if (updateHandle != null) {
               this.remoteStorage.updatePublishedFileFile(updateHandle, paths[1]);
               this.remoteStorage.updatePublishedFilePreviewFile(updateHandle, paths[2]);
               this.remoteStorage.updatePublishedFileTitle(updateHandle, "Updated Test UGC!");
               this.remoteStorage.updatePublishedFileDescription(updateHandle, "Dummy UGC file *updated* by test application.");
               this.remoteStorage.commitPublishedFileUpdate(updateHandle);
            }
         }
      } else if (input.equals("ugc query")) {
         SteamUGCQuery query = this.ugc
            .createQueryUserUGCRequest(
               this.user.getSteamID().getAccountID(),
               UserUGCList.Subscribed,
               MatchingUGCType.UsableInGame,
               UserUGCListSortOrder.TitleAsc,
               this.utils.getAppID(),
               this.utils.getAppID(),
               1
            );
         if (query.isValid()) {
            System.out.println("sending UGC query: " + query.toString());
            this.ugc.sendQueryUGCRequest(query);
         }
      } else if (input.startsWith("ugc download ")) {
         String name = input.substring("ugc download ".length());
         SteamUGCHandle handle = new SteamUGCHandle(Long.parseLong(name, 16));
         this.remoteStorage.ugcDownload(handle, 0);
      } else if (input.startsWith("ugc subscribe ")) {
         Long id = Long.parseLong(input.substring("ugc subscribe ".length()), 16);
         this.ugc.subscribeItem(new SteamPublishedFileID(id));
      } else if (input.startsWith("ugc unsubscribe ")) {
         Long id = Long.parseLong(input.substring("ugc unsubscribe ".length()), 16);
         this.ugc.unsubscribeItem(new SteamPublishedFileID(id));
      } else if (input.startsWith("ugc state ")) {
         Long id = Long.parseLong(input.substring("ugc state ".length()), 16);
         Collection<ItemState> itemStates = this.ugc.getItemState(new SteamPublishedFileID(id));
         System.out.println("UGC item states: " + itemStates.size());

         for (ItemState itemState : itemStates) {
            System.out.println("  " + itemState.name());
         }
      } else if (input.startsWith("ugc details ")) {
         System.out.println("requesting UGC details (deprecated API call)");
         Long id = Long.parseLong(input.substring("ugc details ".length()), 16);
         this.ugc.requestUGCDetails(new SteamPublishedFileID(id), 0);
         SteamUGCQuery query = this.ugc.createQueryUGCDetailsRequest(new SteamPublishedFileID(id));
         if (query.isValid()) {
            System.out.println("sending UGC details query: " + query.toString());
            this.ugc.sendQueryUGCRequest(query);
         }
      } else if (input.startsWith("ugc info ")) {
         Long id = Long.parseLong(input.substring("ugc info ".length()), 16);
         ItemInstallInfo installInfo = new ItemInstallInfo();
         if (this.ugc.getItemInstallInfo(new SteamPublishedFileID(id), installInfo)) {
            System.out.println("  folder: " + installInfo.getFolder());
            System.out.println("  size on disk: " + installInfo.getSizeOnDisk());
         }

         ItemDownloadInfo downloadInfo = new ItemDownloadInfo();
         if (this.ugc.getItemDownloadInfo(new SteamPublishedFileID(id), downloadInfo)) {
            System.out.println("  bytes downloaded: " + downloadInfo.getBytesDownloaded());
            System.out.println("  bytes total: " + downloadInfo.getBytesTotal());
         }
      } else if (input.startsWith("leaderboard find ")) {
         String name = input.substring("leaderboard find ".length());
         this.userStats.findLeaderboard(name);
      } else if (input.startsWith("leaderboard list ")) {
         String[] params = input.substring("leaderboard list ".length()).split(" ");
         if (this.currentLeaderboard != null && params.length >= 2) {
            this.userStats
               .downloadLeaderboardEntries(this.currentLeaderboard, LeaderboardDataRequest.Global, Integer.parseInt(params[0]), Integer.parseInt(params[1]));
         }
      } else if (input.startsWith("leaderboard users ")) {
         String[] params = input.substring("leaderboard users ".length()).split(" ");
         if (this.currentLeaderboard != null && params.length > 0) {
            SteamID[] users = new SteamID[params.length];

            for (int i = 0; i < params.length; i++) {
               users[i] = SteamID.createFromNativeHandle(Long.parseLong(params[i]));
            }

            this.userStats.downloadLeaderboardEntriesForUsers(this.currentLeaderboard, users);
         }
      } else if (input.startsWith("leaderboard score ")) {
         String score = input.substring("leaderboard score ".length());
         if (this.currentLeaderboard != null) {
            System.out.println("uploading score " + score + " to leaderboard " + this.currentLeaderboard.toString());
            this.userStats.uploadLeaderboardScore(this.currentLeaderboard, LeaderboardUploadScoreMethod.KeepBest, Integer.parseInt(score), new int[0]);
         }
      } else if (input.startsWith("apps subscribed ")) {
         String appId = input.substring("apps subscribed ".length());
         boolean subscribed = this.apps.isSubscribedApp(Integer.parseInt(appId));
         System.out.println("user described to app #" + appId + ": " + (subscribed ? "yes" : "no"));
      }
   }
}
