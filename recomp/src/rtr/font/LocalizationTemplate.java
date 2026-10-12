package rtr.font;

import rtr.utilities.JavaCompat;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Properties;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.yaml.snakeyaml.Yaml;
import rtr.ImageLoader;
import rtr.utilities.YAMLFlattener;

public class LocalizationTemplate {
   private File languageFolder;
   private String languageFolderName;
   private String languageName;
   private String languageAuthor;
   private String fontNameBody;
   private String fontNameHeader;
   private String languageLink;
   private int[] glyphRangeStart = new int[10];
   private int[] glyphRangeStop = new int[10];
   private int fontHeaderSizeOffset;
   private int fontBodySizeOffset;
   private int fontHeaderXOffset;
   private int fontHeaderYOffset;
   private int fontBodyXOffset;
   private int fontBodyYOffset;
   private boolean wordWrapUseCharacterPositions;
   private double percentCompleted;
   private String languageIconName;
   private Image icon;
   private boolean workshopMod;

   public LocalizationTemplate(File absoluteFolder, boolean workshopMod) {
      this.languageFolder = absoluteFolder;
      this.languageFolderName = this.languageFolder.getName();
      this.workshopMod = workshopMod;
      this.configureProperties();
      this.calculatePercentage();
   }

   private void configureProperties() {
      Properties languageConfig = new Properties();

      try {
         FileInputStream in = new FileInputStream(new File(this.languageFolder, "configuration.properties"));
         languageConfig.load(in);
         in.close();
      } catch (IOException var4) {
         var4.printStackTrace();
      }

      this.languageName = languageConfig.getProperty("languageName");
      this.languageAuthor = languageConfig.getProperty("languageAuthor");
      this.fontNameHeader = languageConfig.getProperty("fontNameHeader");
      this.fontNameBody = languageConfig.getProperty("fontNameBody");

      for (int x = 0; x < this.glyphRangeStart.length; x++) {
         if (!languageConfig.getProperty("glyphRangeStart" + x, "0").equals("0")) {
            this.glyphRangeStart[x] = Integer.parseInt(languageConfig.getProperty("glyphRangeStart" + x), 16);
            this.glyphRangeStop[x] = Integer.parseInt(languageConfig.getProperty("glyphRangeStop" + x), 16);
         }
      }

      this.fontBodySizeOffset = Integer.parseInt(languageConfig.getProperty("fontBodySizeOffset", "0"));
      this.fontHeaderSizeOffset = Integer.parseInt(languageConfig.getProperty("fontHeaderSizeOffset", "0"));
      this.fontBodyXOffset = Integer.parseInt(languageConfig.getProperty("fontBodyXOffset", "0"));
      this.fontBodyYOffset = Integer.parseInt(languageConfig.getProperty("fontBodyYOffset", "0"));
      this.fontHeaderXOffset = Integer.parseInt(languageConfig.getProperty("fontHeaderXOffset", "0"));
      this.fontHeaderYOffset = Integer.parseInt(languageConfig.getProperty("fontHeaderYOffset", "0"));
      this.languageIconName = languageConfig.getProperty("languageIcon", "noFlag");
      if (this.workshopMod) {
         this.languageLink = "https://steamcommunity.com/sharedfiles/filedetails/?id=" + this.languageFolderName;
      } else {
         this.languageLink = languageConfig.getProperty("languageLink");
      }

      this.wordWrapUseCharacterPositions = Boolean.parseBoolean(languageConfig.getProperty("wordWrapUseCharacterPositions", "false"));
   }

   private void calculatePercentage() {
      if (this.languageFolder.toString().contains("defaultEnglish")) {
         this.percentCompleted = 100.0;
      } else {
         Charset UTF_8 = Charset.forName("UTF-8");
         Map<String, Object> defaultEnglishMap = null;

         try {
            File fileDir = new File("localization/defaultEnglish/text.yml");
            InputStreamReader in = new InputStreamReader(new FileInputStream(fileDir), UTF_8);
            defaultEnglishMap = new Yaml().loadAs(in, Map.class);
            defaultEnglishMap = YAMLFlattener.flattenYamlTree(defaultEnglishMap);
            in.close();
         } catch (IOException var13) {
            var13.printStackTrace();
         }

         Map<String, Object> thisMap = null;

         try {
            File fileDir = new File(this.languageFolder, "text.yml");
            InputStreamReader in = new InputStreamReader(new FileInputStream(fileDir), UTF_8);
            thisMap = new Yaml().loadAs(in, Map.class);
            thisMap = YAMLFlattener.flattenYamlTree(thisMap);
            in.close();
         } catch (IOException var12) {
            var12.printStackTrace();
         }

         int totalDefaultEnglishKeys = 0;
         int totalNewKeys = 0;

         for (Object rawKey : defaultEnglishMap.keySet()) {
            if (rawKey.toString().contains(".text")) {
               totalDefaultEnglishKeys++;
            }
         }

         for (String defaultKey : defaultEnglishMap.keySet()) {
            if (defaultKey.contains(".text")) {
               String optionalCheck = defaultKey.replace(".text", ".optional");
               if (defaultEnglishMap.containsKey(optionalCheck)) {
                  totalNewKeys++;
               } else {
                  for (String thisKey : thisMap.keySet()) {
                     if (thisKey.equals(defaultKey)) {
                        String forceCheck = thisKey.replace(".text", ".forceCount");
                        if (thisMap.containsKey(forceCheck)) {
                           totalNewKeys++;
                           break;
                        }

                        if (!thisMap.get(thisKey).equals("")
                           && !thisMap.get(thisKey).equals(" ")
                           && !thisMap.get(thisKey).equals(defaultEnglishMap.get(defaultKey))) {
                           totalNewKeys++;
                           break;
                        }
                     }
                  }
               }
            }
         }

         if (defaultEnglishMap != thisMap) {
            this.generateMissingKeyList(defaultEnglishMap, thisMap);
         }

         if (totalNewKeys == totalDefaultEnglishKeys) {
            this.percentCompleted = 100.0;
         } else {
            this.percentCompleted = totalNewKeys * 1.0 / (totalDefaultEnglishKeys * 1.0) * 100.0;
            this.percentCompleted = Double.parseDouble(Double.toString(this.percentCompleted).substring(0, 4));
         }
      }
   }

   private void generateMissingKeyList(Map<String, Object> defaultEnglishMap, Map<String, Object> thisMap) {
      int count = 1;
      BufferedWriter writer = null;

      try {
         String newLine = System.getProperty("line.separator");
         writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.languageFolder + "/missingKeysReport.txt"), "utf-8"));
         writer.write("------------------------------------------" + newLine);
         writer.write(this.languageName + " by " + this.languageAuthor + " Missing Keys Report" + newLine);
         writer.write("------------------------------------------" + newLine);
         writer.write(
            "This report is automatically generated upon launch of the game, if you'd like to generate a new list simply relaunch the game." + newLine
         );
         writer.write("------------------------------------------" + newLine);
         writer.write("Missing Keys: \t\t\tKeys that are completely missing from the translation file." + newLine);
         writer.write(
            "Old Revision Keys: \t\tKeys that are included in the translation file, but are from an old revision number and need updated. (Note: When updating, be sure to add the new revision number)"
               + newLine
         );
         writer.write(
            "Force Counted Keys: \tKeys that have been forced to be counted as complete by the system, even though the translation matches the original text and normally would not be counted."
               + newLine
         );
         writer.write("------------------------------------------" + newLine);

         label110:
         for (String defaultKey : defaultEnglishMap.keySet()) {
            if (defaultKey.endsWith(".text")) {
               String stringCheck = defaultKey.replace(".text", ".optional");
               if (!defaultEnglishMap.containsKey(stringCheck)) {
                  stringCheck = defaultKey.replace(".text", ".revision");
                  if (defaultEnglishMap.containsKey(stringCheck)) {
                     int baseRevisionNumber = Integer.parseInt(JavaCompat.getOrDefault(defaultEnglishMap, stringCheck, "-1").toString());
                     int thisRevisionNumber = Integer.parseInt(JavaCompat.getOrDefault(thisMap, stringCheck, "-1").toString());
                     if (baseRevisionNumber > thisRevisionNumber) {
                        writer.write("Old Revision Key\t\t" + count++ + ": " + defaultKey + newLine);
                        continue;
                     }
                  }

                  for (String thisKey : thisMap.keySet()) {
                     if (thisKey.equals(defaultKey)) {
                        stringCheck = thisKey.replace(".text", ".forceCount");
                        if (thisMap.containsKey(stringCheck)) {
                           writer.write("Force Counted Key\t\t" + count++ + ": " + defaultKey + newLine);
                           continue label110;
                        }

                        if (!thisMap.get(thisKey).equals(defaultEnglishMap.get(defaultKey))) {
                           continue label110;
                        }
                     }
                  }

                  writer.write("Missing Key\t\t\t\t" + count++ + ": " + defaultKey + newLine);
               }
            }
         }

         writer.close();
      } catch (IOException var19) {
         var19.printStackTrace();
      } finally {
         try {
            writer.close();
         } catch (Exception var18) {
         }
      }
   }

   public File getLanguageFolder() {
      return this.languageFolder;
   }

   public String getLanguageName() {
      return this.languageName;
   }

   public String getLanguageAuthor() {
      return this.languageAuthor;
   }

   public String getFontName(Text.FontType type) {
      return type == Text.FontType.BODY ? this.fontNameBody : this.fontNameHeader;
   }

   public int[] getGlyphRangeStart() {
      return this.glyphRangeStart;
   }

   public int[] getGlyphRangeStop() {
      return this.glyphRangeStop;
   }

   public int getFontSizeOffset(Text.FontType type) {
      return type == Text.FontType.BODY ? this.fontBodySizeOffset : this.fontHeaderSizeOffset;
   }

   public int getFontXOffset(Text.FontType type) {
      return type == Text.FontType.BODY ? this.fontBodyXOffset : this.fontHeaderXOffset;
   }

   public int getFontYOffset(Text.FontType type) {
      return type == Text.FontType.BODY ? this.fontBodyYOffset : this.fontHeaderYOffset;
   }

   public double getPercentCompleted() {
      return this.percentCompleted;
   }

   public Image getIcon() {
      if (this.icon == null) {
         try {
            File localFolderIcon = new File(this.languageFolder, this.languageIconName + ".png");
            File flagFolderIcon = new File("localization/flags/" + this.languageIconName + ".png");
            if (localFolderIcon.exists()) {
               this.icon = ImageLoader.getImage(localFolderIcon.toString());
            } else if (flagFolderIcon.exists()) {
               this.icon = ImageLoader.getImage(flagFolderIcon.toString());
            } else {
               this.icon = ImageLoader.getImage("localization/flags/noFlag.png");
            }
         } catch (SlickException var3) {
            var3.printStackTrace();
         }
      }

      return this.icon;
   }

   public File getTextFilePath() {
      return new File(this.languageFolder, "text.yml");
   }

   public String getLanguageFolderName() {
      return this.languageFolderName;
   }

   public String getLanguageLink() {
      return this.languageLink;
   }

   public boolean getWordWrapUseCharacterPositions() {
      return this.wordWrapUseCharacterPositions;
   }
}
