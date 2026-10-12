package rtr.mobs;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class MobTwitchData implements Runnable {
   private long lastCheckTick = 10000L;
   private List<String> nameList = Collections.synchronizedList(new ArrayList<>());
   private List<String> fullNameList = Collections.synchronizedList(new ArrayList<>());
   private List<String> useList = Collections.synchronizedList(new ArrayList<>());
   private String channelName = "";
   private boolean isRunning;

   public MobTwitchData(String c) {
      this.channelName = c.toLowerCase();
   }

   public void update() {
   }

   public List<String> getNamesList() {
      return this.nameList;
   }

   public List<String> getFullNamesList() {
      return this.fullNameList;
   }

   public void addUseList(String s) {
      this.nameList.remove(s);
      this.fullNameList.remove(s);
      this.useList.add(s);
   }

   @Override
   public void run() {
      this.nameList.clear();
      this.fullNameList.clear();

      try {
         URL url = new URL("http://tmi.twitch.tv/group/user/" + this.channelName + "/chatters");
         Scanner s = new Scanner(url.openStream());

         label70:
         while (s.hasNext()) {
            String out = s.nextLine();
            if (out.contains("      ")) {
               out = out.replaceAll("      ", "");
               int spacePosition = -1;
               out = out.replaceAll("[^a-zA-Z0-9\\_]", "");
               if (out.length() > 6) {
                  for (int x = 2; x < out.length() - 4; x++) {
                     if (out.charAt(x) == "_".charAt(0)) {
                        spacePosition = x;
                        break;
                     }
                  }
               }

               String first = null;
               String last = null;
               if (spacePosition != -1) {
                  first = out.substring(0, spacePosition);
                  last = out.substring(spacePosition);
                  first = first.replaceAll("[^a-zA-Z0-9]", "");
                  first = first.substring(0, 1).toUpperCase() + first.substring(1);
                  last = last.replaceAll("[^a-zA-Z0-9]", "");
                  last = last.substring(0, 1).toUpperCase() + last.substring(1);
                  out = first + " " + last;
                  int size = this.useList.size();

                  for (int i = 0; i < size; i++) {
                     if (this.useList.get(i).equals(out)) {
                        continue label70;
                     }
                  }

                  this.fullNameList.add(out);
               } else {
                  out = out.replaceAll("[^a-zA-Z0-9]", "");
                  out = out.substring(0, 1).toUpperCase() + out.substring(1);
                  int size = this.useList.size();

                  for (int ix = 0; ix < size; ix++) {
                     if (this.useList.get(ix).equals(out)) {
                        continue label70;
                     }
                  }

                  this.nameList.add(out);
               }
            }
         }

         s.close();
         this.isRunning = false;
      } catch (IOException var9) {
         this.isRunning = false;
         var9.printStackTrace();
      }
   }
}
