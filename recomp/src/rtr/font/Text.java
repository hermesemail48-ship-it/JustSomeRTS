package rtr.font;

import rtr.utilities.JavaCompat;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import rtr.platform.Platform;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;
import rtr.SettingsParser;
import rtr.utilities.Steamworks;
import rtr.utilities.YAMLFlattener;

public class Text {
   public static final Charset UTF_8 = Charset.forName("UTF-8");
   private static int[] FONT_REAL_SIZES = new int[]{10, 12, 16, 20, 24, 28, 32, 20, 24, 32, 40, 48, 56, 64};
   private static EnumMap<Text.FontType, ArrayList<Platform.PlatformFont>> loadedFonts = new EnumMap<>(Text.FontType.class);
   private static HashMap<String, String> variableText = new HashMap<>();
   private static HashMap<String, String> text;
   private static ArrayList<LocalizationTemplate> availableLocalizations = new ArrayList<>();
   private static LocalizationTemplate defaultEnglish;
   private static LocalizationTemplate loadedLanguage;
   public static boolean languageInitialized = false;

   public static void initializeLanguage() {
      availableLocalizations.clear();
      text = new HashMap<>();
      textCache.clear();
      System.out.println("Fetching local language files.");
      File folderList = new File("localization/");

      File[] keyCount;
      for (File f : keyCount = folderList.listFiles()) {
         if (f.isDirectory()) {
            File checkText = new File(f.getAbsoluteFile(), "text.yml");
            File checkConfig = new File(f.getAbsoluteFile(), "configuration.properties");
            if (checkText.exists() && checkConfig.exists()) {
               System.out.println("Attempting to load: " + f.getAbsoluteFile());
               LocalizationTemplate template = new LocalizationTemplate(f, false);
               if (f.getName().contains("defaultEnglish")) {
                  defaultEnglish = template;
               }

               availableLocalizations.add(template);
            }
         }
      }

      for (LocalizationTemplate a : availableLocalizations) {
         if (a.getLanguageFolderName().equals("defaultEnglish")) {
            availableLocalizations.remove(a);
            availableLocalizations.add(0, a);
            break;
         }
      }

      System.out.println();
      System.out.println("Fetching Steam Workshop language files, if logged in to Steam.");
      ArrayList<String> workshopLanguages = Steamworks.getAndRefreshLoadedLanguages();
      if (workshopLanguages.size() == 0) {
         System.out.println("No Steam Workshop language files found.");
      }

      for (String modFolderRef : workshopLanguages) {
         if (modFolderRef != null) {
            File checkText = new File(modFolderRef, "text.yml");
            File checkConfig = new File(modFolderRef, "configuration.properties");
            if (checkText.exists() && checkConfig.exists()) {
               System.out.println("Attempting to load: " + modFolderRef);
               File fx = new File(modFolderRef);
               LocalizationTemplate template = new LocalizationTemplate(fx, true);
               availableLocalizations.add(template);
            }
         }
      }

      System.out.println();
      String setLanguage = SettingsParser.getLanguage();

      for (LocalizationTemplate ax : availableLocalizations) {
         if (ax.getLanguageFolderName().equals(setLanguage)) {
            loadedLanguage = ax;
            break;
         }
      }

      if (loadedLanguage == null) {
         System.out.println("Failed to find set loaded language, falling back to default.");
         loadedLanguage = defaultEnglish;
      }

      initialize(defaultEnglish, true);
      if (loadedLanguage != defaultEnglish) {
         initialize(loadedLanguage, false);
      }

      int wordCount = 0;
      int keyCountx = 0;

      for (String k : text.keySet()) {
         String word = text.get(k);
         if (k.contains(".text")) {
            keyCountx++;
         }

         wordCount += word.split(" ").length;
      }

      System.out.println("Total word count: " + wordCount);
      System.out.println("Total key count: " + text.keySet().size());
      System.out.println("Text key count: " + keyCountx);
      System.out.println();
      System.out.println("Loading Font Data");

      Text.FontType[] var8;
      for (Text.FontType type : Text.FontType.values()) {
         loadedFonts.put(type, new ArrayList<>());
      }

      int[] var41 = FONT_REAL_SIZES;
      int var40 = FONT_REAL_SIZES.length;

      for (int var36 = 0; var36 < var40; var36++) {
         Integer size = var41[var36];

         Text.FontType[] var12;
         for (Text.FontType type : Text.FontType.values()) {
            int sizeOffset = loadedLanguage.getFontSizeOffset(type);
            File fontFile;
            try {
               if (type == Text.FontType.BODY && loadedLanguage.getFontName(type).equals("")) {
                  fontFile = new File("res/font/PatuaOne-Regular.ttf");
               } else if (type == Text.FontType.HEADER && loadedLanguage.getFontName(type).equals("")) {
                  fontFile = new File("res/font/YatraOne-Regular.ttf");
               } else {
                  String fontName = loadedLanguage.getFontName(type);
                  fontName = fontName.replaceAll(".ttf", "");
                  fontName = fontName.replaceAll(".TTF", "");
                  fontFile = new File(loadedLanguage.getLanguageFolder() + "/" + fontName + ".ttf");
                  if (!fontFile.exists()) {
                     fontFile = new File(loadedLanguage.getLanguageFolder() + "/" + fontName + ".TTF");
                  }
               }

               loadedFonts.get(type).add(Platform.get().loadFont(fontFile, size.intValue() + sizeOffset * 1.0F));
            } catch (IOException var17) {
               var17.printStackTrace();
            }
         }
      }

      languageInitialized = true;
   }

   private static void initialize(LocalizationTemplate loadedLanguage, boolean defaultEnglish) {
      try {
         File fileDir = loadedLanguage.getTextFilePath();
         InputStreamReader in = new InputStreamReader(new FileInputStream(fileDir), UTF_8);
         Map<String, Object> yaml = new Yaml().loadAs(in, Map.class);
         yaml = YAMLFlattener.flattenYamlTree(yaml);
         in.close();

         for (String rawKey : yaml.keySet()) {
            String rawText = yaml.get(rawKey).toString();
            if (!defaultEnglish && rawKey.contains(".text")) {
               String revisionTag = rawKey.replace(".text", ".revision");
               int baseRevisionNumber = Integer.parseInt(JavaCompat.getOrDefault(text, revisionTag, "-1"));
               int thisRevisionNumber = -1;
               if (yaml.containsKey(revisionTag)) {
                  thisRevisionNumber = Integer.parseInt(JavaCompat.getOrDefault(yaml, revisionTag, "-1").toString());
               }

               if (baseRevisionNumber > thisRevisionNumber) {
                  continue;
               }
            }

            text.put(rawKey, rawText);
         }
      } catch (IOException var12) {
         System.out.println(loadedLanguage + " failed to load!");
         var12.printStackTrace();
      }
   }

   public static String getVariableText(String k) {
      return variableText.get(k);
   }

   public static void setVariableText(String k, String v) {
      variableText.put(k, v);
   }

   public static void setVariableText(String k, int v) {
      variableText.put(k, Integer.toString(v));
   }

   public static void setVariableText(String k, double v) {
      variableText.put(k, Double.toString(v));
   }

   /** key -> its template (or MISSING), so lookups don't build key + ".text" each time. */
   private static final java.util.concurrent.ConcurrentHashMap<String, String> textCache = new java.util.concurrent.ConcurrentHashMap<>();
   private static final String MISSING = new String(" missing");

   public static String getText(String key) {
      if (!languageInitialized) {
         initializeLanguage();
      }

      String out = textCache.get(key);
      if (out == null) {
         out = text.get(key + ".text");
         if (out == null) {
            out = MISSING;
         }

         textCache.put(key, out);
      }

      if (out == MISSING) {
         return key;
      } else if (out.indexOf('{') < 0) {
         // Most texts have no placeholders: AI tasks look these up every tick for every mob.
         return out;
      } else {
         for (String k : variableText.keySet()) {
            if (out.contains("{" + k + "}")) {
               out = out.replace("{" + k + "}", variableText.get(k));
            }
         }

         return out;
      }
   }

   public static char[] getAllStringCharacters() {
      ArrayList<Character> characterArrayList = new ArrayList<>();

      for (String key : text.keySet()) {
         String value = text.get(key);
         char[] thisStringChars = value.toCharArray();

         for (char c : thisStringChars) {
            if (!characterArrayList.contains(c)) {
               characterArrayList.add(c);
            }
         }
      }

      for (int g = 0; g < loadedLanguage.getGlyphRangeStart().length; g++) {
         int start = loadedLanguage.getGlyphRangeStart()[g];
         int stop = loadedLanguage.getGlyphRangeStop()[g];
         if (start != 0 && stop != 0) {
            for (int x = start; x < stop; x++) {
               char[] charPair = Character.toChars(x);

               for (int y = 0; y < charPair.length; y++) {
                  if (!characterArrayList.contains(charPair[y])) {
                     characterArrayList.add(charPair[y]);
                  }
               }
            }
         }
      }

      char[] charsOut = new char[characterArrayList.size()];

      for (int cx = 0; cx < charsOut.length; cx++) {
         charsOut[cx] = characterArrayList.get(cx);
      }

      return charsOut;
   }

   public static String wrapString(String input, Text.FontType type, int size, int wrapAt) {
      return wrapString(input, type, size, wrapAt, false);
   }

   public static String wrapString(String input, Text.FontType type, int size, int wrapAt, boolean forceWrapBySpace) {
      StringBuilder sbInput = new StringBuilder(input);
      StringBuilder sbOutput = new StringBuilder();
      Platform.PlatformFont font = loadedFonts.get(type).get(size);
      int baseOffset = 0;
      HashMap<Integer, String> colorCodes = new HashMap<>();
      HashMap<Integer, Integer> colorCodeBaseOffset = new HashMap<>();

      for (int i = 0; i < sbInput.length(); i++) {
         if (sbInput.charAt(i) == '~') {
            baseOffset--;
         }

         if (sbInput.charAt(i) == '$') {
            String colorCode = sbInput.substring(i, i + 5);
            colorCodes.put(i, colorCode);
            colorCodeBaseOffset.put(i, baseOffset);
         }
      }

      for (int i = 0; i < sbInput.length(); i++) {
         if (sbInput.charAt(i) == '$') {
            sbInput.replace(i, i + 5, "");
         }
      }

      double textWidth = 0.0;
      int lastWrap = 0;
      int lastSpace = 0;
      if (loadedLanguage.getWordWrapUseCharacterPositions() && !forceWrapBySpace) {
         for (int ix = 0; ix < sbInput.length(); ix++) {
            String subString = sbInput.substring(lastWrap, ix);
            if (sbInput.charAt(ix) == '~') {
               sbOutput.append(subString);
               lastWrap = ix;
            } else {
               String subStringNext = sbInput.substring(lastWrap, ix + 1);
               textWidth = Math.ceil(font.getStringWidth(subStringNext));
               if (textWidth >= wrapAt) {
                  sbOutput.append(subString + "~");
                  lastWrap = ix--;
               } else if (ix == sbInput.length() - 1) {
                  sbOutput.append(subStringNext);
               }
            }
         }
      } else {
         for (int ixx = 0; ixx < sbInput.length(); ixx++) {
            String subString = sbInput.substring(lastWrap, ixx);
            if (sbInput.charAt(ixx) == '~') {
               sbOutput.append(subString);
               lastSpace = ixx;
               lastWrap = ixx;
            } else {
               if (sbInput.charAt(ixx) == ' ') {
                  lastSpace = ixx;
               }

               String subStringNext = sbInput.substring(lastWrap, ixx + 1);
               textWidth = Math.ceil(font.getStringWidth(subStringNext));
               if (textWidth >= wrapAt) {
                  String subStringIn = sbInput.substring(lastWrap, lastSpace);
                  sbOutput.append(subStringIn + "~");
                  lastWrap = lastSpace;
                  lastSpace = ixx--;
               } else if (ixx == sbInput.length() - 1) {
                  sbOutput.append(subStringNext);
               }
            }
         }
      }

      int offset = 0;

      for (int ixxx = 0; ixxx < sbOutput.length(); ixxx++) {
         if (sbOutput.charAt(ixxx) == '~') {
            offset++;
         }

         if (colorCodes.get(ixxx) != null) {
            int additionalOffset = colorCodeBaseOffset.get(ixxx);
            sbOutput.replace(ixxx + offset + additionalOffset, ixxx + offset + additionalOffset, colorCodes.get(ixxx));
         }
      }

      sbOutput = new StringBuilder(sbOutput.toString().replaceAll("~ ", "~"));
      return sbOutput.toString();
   }

   public static int getHeight(String text, Text.FontType type, int size) {
      Platform.PlatformFont font = loadedFonts.get(type).get(size);
      double textHeight = Math.ceil(font.getStringHeight(text));
      return (int)textHeight;
   }

   public static int getWidth(String text, Text.FontType type, int size) {
      StringBuilder sbInput = new StringBuilder(text);

      for (int t = 0; t < sbInput.length(); t++) {
         if (sbInput.charAt(t) == '$') {
            sbInput.replace(t, t + 5, "");
         }
      }

      text = sbInput.toString();
      Platform.PlatformFont font = loadedFonts.get(type).get(size);
      double textWidth = Math.ceil(font.getStringWidth(text));
      return (int)textWidth;
   }

   public static int[] getFontRealSizes() {
      return FONT_REAL_SIZES;
   }

   public static ArrayList<LocalizationTemplate> getAvailableLocalizations() {
      return availableLocalizations;
   }

   public static LocalizationTemplate getDefaultTemplate() {
      return defaultEnglish;
   }

   public static LocalizationTemplate getLoadedTemplate() {
      return loadedLanguage;
   }

   public static EnumMap<Text.FontType, ArrayList<Platform.PlatformFont>> getFonts() {
      return loadedFonts;
   }

   public static enum FontType {
      BODY,
      HEADER;
   }
}
