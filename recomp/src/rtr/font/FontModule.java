package rtr.font;

import rtr.platform.Platform;
import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import rtr.ImageLoader;
import rtr.ModuleBase;
import rtr.states.StateBase;
import rtr.system.ScaleControl;

public class FontModule extends ModuleBase {
   private ArrayList<FloaterObject> floaters = new ArrayList<>();
   private HashMap<String, Image> loadedFloaters = new HashMap<>();
   private HashMap<String, Color> fontColors = new HashMap<>();
   private boolean fontsLoaded;
   private EnumMap<Text.FontType, ArrayList<TrueTypeFont>> unicodeFonts = new EnumMap<>(Text.FontType.class);
   private ArrayList<ArrayList<String>> outputTexts = new ArrayList<>();
   private ArrayList<ArrayList<Color>> outputColors = new ArrayList<>();
   private int headerXOffset;
   private int headerYOffset;
   private int bodyXOffset;
   private int bodyYOffset;
   private Graphics g;

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      this.g = new Graphics();
      if (!this.fontsLoaded) {
         this.fontColors.put("WHI0", new Color(250, 250, 250));
         this.fontColors.put("YEL0", new Color(250, 250, 200));
         this.fontColors.put("YEL1", new Color(250, 255, 100));
         this.fontColors.put("YEL2", new Color(150, 150, 0));
         this.fontColors.put("RED0", new Color(250, 200, 200));
         this.fontColors.put("RED1", new Color(250, 100, 100));
         this.fontColors.put("RED2", new Color(150, 0, 0));
         this.fontColors.put("GRE0", new Color(100, 250, 200));
         this.fontColors.put("GRE1", new Color(100, 250, 100));
         this.fontColors.put("GRE2", new Color(0, 150, 0));
         this.fontColors.put("BLU0", new Color(200, 200, 250));
         this.fontColors.put("BLU1", new Color(100, 100, 250));
         this.fontColors.put("BLU2", new Color(0, 0, 150));
         this.fontColors.put("BLA0", new Color(0, 0, 0));
         this.fontColors.put("BLA1", new Color(50, 50, 50));
         this.fontColors.put("BLA2", new Color(100, 100, 100));
         this.fontColors.put("CYA0", new Color(200, 250, 250));
         this.fontColors.put("CYA1", new Color(100, 250, 250));
         this.fontColors.put("CYA2", new Color(0, 150, 150));
         this.fontColors.put("PIN0", new Color(255, 170, 215));
         this.fontColors.put("PIN1", new Color(255, 115, 190));
         this.fontColors.put("PIN2", new Color(190, 70, 130));
         this.fontColors.put("ORA0", new Color(235, 170, 100));
         this.fontColors.put("ORA1", new Color(235, 135, 40));
         this.fontColors.put("ORA2", new Color(180, 105, 30));
         this.fontColors.put("MAG0", new Color(214, 141, 210));
         this.fontColors.put("MAG1", new Color(215, 80, 205));
         this.fontColors.put("MAG2", new Color(150, 55, 150));
         this.unicodeFonts.put(Text.FontType.BODY, new ArrayList<>());
         this.unicodeFonts.put(Text.FontType.HEADER, new ArrayList<>());
         LocalizationTemplate template = Text.getLoadedTemplate();
         this.headerXOffset = template.getFontXOffset(Text.FontType.HEADER);
         this.headerYOffset = template.getFontYOffset(Text.FontType.HEADER);
         this.bodyXOffset = template.getFontXOffset(Text.FontType.BODY);
         this.bodyYOffset = template.getFontYOffset(Text.FontType.BODY);
         char[] finalCharsOut = Text.getAllStringCharacters();
         int[] fontSizes = Text.getFontRealSizes();
         EnumMap<Text.FontType, ArrayList<Platform.PlatformFont>> fonts = Text.getFonts();
         HashMap<Integer, HashMap<Text.FontType, TrueTypeFont>> loadedFonts = new HashMap<>();

         for (int i = 0; i < fontSizes.length; i++) {
            if (loadedFonts.containsKey(fontSizes[i])) {
               Text.FontType[] var17;
               for (Text.FontType type : Text.FontType.values()) {
                  this.unicodeFonts.get(type).add(loadedFonts.get(fontSizes[i]).get(type));
               }
            } else {
               loadedFonts.put(fontSizes[i], new HashMap<>());

               Text.FontType[] var12;
               for (Text.FontType type : Text.FontType.values()) {
                  TrueTypeFont fontOutNew = Platform.get().createTrueTypeFont(fonts.get(type).get(i), true, finalCharsOut);
                  this.unicodeFonts.get(type).add(fontOutNew);
                  loadedFonts.get(fontSizes[i]).put(type, fontOutNew);
               }
            }
         }

         this.fontsLoaded = true;
      }
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
      this.floaters.clear();
   }

   @Override
   protected void newMainMenu() throws SlickException {
   }

   @Override
   protected void newPlay() throws SlickException {
   }

   @Override
   protected void loadPlay() throws SlickException {
   }

   @Override
   protected void newMapEditor() throws SlickException {
   }

   @Override
   protected void loadMapEditor() throws SlickException {
   }

   @Override
   protected void newWorldMap() throws SlickException {
   }

   @Override
   protected void loadWorldMap() throws SlickException {
   }

   @Override
   protected void loadRegionalSaveData() throws SlickException {
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
   }

   public void update() {
      for (int i = 0; i < this.floaters.size(); i++) {
         this.floaters.get(i).update();
      }
   }

   public void render(Graphics g) {
      for (int i = 0; i < this.floaters.size(); i++) {
         this.floaters.get(i).render(g);
      }
   }

   public void newFloater(String text, float x, float y) throws SlickException {
      this.newFloater(text, x, y, false);
   }

   public void newFloater(String icon, String text, float x, float y) throws SlickException {
      this.newFloater(icon, text, x, y, false);
   }

   public void newFloater(String text, float x, float y, boolean f) throws SlickException {
      if (!this.loadedFloaters.containsKey(text)) {
         File imageCheck = new File("res/GUI/icon/" + text + ".png");
         if (imageCheck.exists()) {
            Image imageLoad = ImageLoader.getImage("res/GUI/icon/" + text + ".png");
            this.loadedFloaters.put(text, imageLoad);
            this.floaters.add(new FloaterObject(imageLoad, x, y, f));
         } else {
            this.loadedFloaters.put(text, null);
            this.floaters.add(new FloaterObject(text, x, y, f));
         }
      } else {
         Image image = this.loadedFloaters.get(text);
         if (image != null) {
            this.floaters.add(new FloaterObject(image, x, y, f));
         } else {
            this.floaters.add(new FloaterObject(text, x, y, f));
         }
      }
   }

   public void newFloater(String icon, String text, float x, float y, boolean f) throws SlickException {
      if (!this.loadedFloaters.containsKey(icon)) {
         File imageCheck = new File("res/GUI/icon/" + icon + ".png");
         if (imageCheck.exists()) {
            Image imageLoad = ImageLoader.getImage("res/GUI/icon/" + icon + ".png");
            this.loadedFloaters.put(text, imageLoad);
            this.floaters.add(new FloaterObject(imageLoad, text, x, y, f));
         } else {
            this.loadedFloaters.put(icon, null);
            this.floaters.add(new FloaterObject(text, x, y, f));
         }
      } else {
         Image image = this.loadedFloaters.get(icon);
         if (image != null) {
            this.floaters.add(new FloaterObject(image, text, x, y, f));
         } else {
            this.floaters.add(new FloaterObject(text, x, y, f));
         }
      }
   }

   public void removeFloater(FloaterObject floater) {
      this.floaters.remove(floater);
   }

   public void drawString(float x, float y, String text, Text.FontType type, int size) {
      this.drawString(x, y, text, 255, type, size, false);
   }

   public void drawString(float x, float y, String text, Text.FontType type, int size, boolean centered) {
      this.drawString(x, y, text, 255, type, size, centered);
   }

   public void drawString(float x, float y, String text, int alpha, Text.FontType type, int size, boolean centered) {
      if (type == Text.FontType.HEADER) {
         x += this.headerXOffset;
         y += this.headerYOffset;
      } else {
         x += this.bodyXOffset;
         y += this.bodyYOffset;
      }

      if (ScaleControl.getInterfaceScale() >= 2.0F) {
         size += 7;
         x *= 2.0F;
         y *= 2.0F;
         this.g.resetTransform();
         this.g.scale(ScaleControl.getInterfaceScale() / 2.0F, ScaleControl.getInterfaceScale() / 2.0F);
      }

      TrueTypeFont fontOut = this.unicodeFonts.get(type).get(size);
      this.outputTexts.clear();
      this.outputColors.clear();
      Color outputColor = this.fontColors.get("WHI0");
      int start = 0;
      int stop = 0;
      int outputLines = text.split("~").length;

      for (int l = 0; l < outputLines; l++) {
         this.outputTexts.add(new ArrayList<>());
         this.outputColors.add(new ArrayList<>());
      }

      for (int l = 0; l < outputLines; l++) {
         for (int t = start; t < text.length(); t++) {
            if (text.charAt(t) == '$' && text.length() >= t + 5) {
               if (start != t) {
                  this.outputTexts.get(l).add(text.substring(start, t));
                  this.outputColors.get(l).add(outputColor);
               }

               start = t + 5;
               String subStringCheck = text.substring(t + 1, t + 5);

               for (String fC : this.fontColors.keySet()) {
                  if (fC.equals(subStringCheck)) {
                     outputColor = this.fontColors.get(fC);
                     break;
                  }
               }
            } else if (t == text.length() - 1) {
               this.outputTexts.get(l).add(text.substring(start, text.length()));
               this.outputColors.get(l).add(outputColor);
            } else if (text.charAt(t) == '~') {
               this.outputTexts.get(l).add(text.substring(start, t));
               this.outputColors.get(l).add(outputColor);
               start = t + 1;
               break;
            }
         }
      }

      int stringHeight = 0;

      for (int l = 0; l < outputLines; l++) {
         int stringLength = 0;
         ArrayList<String> outputTextsArray = this.outputTexts.get(l);
         int outputTextsSize = this.outputTexts.get(l).size();

         for (int tx = 0; tx < outputTextsSize; tx++) {
            String finalString = outputTextsArray.get(tx);
            Color outputColorThis = this.outputColors.get(l).get(tx);
            Color finalColor;
            Color shadowColor;
            if (alpha < 255) {
               finalColor = new Color(outputColorThis.getRed(), outputColorThis.getGreen(), outputColorThis.getBlue(), alpha);
               shadowColor = new Color(0, 0, 0, alpha);
            } else {
               finalColor = outputColorThis;
               shadowColor = Color.black;
            }

            if (!centered) {
               if (size <= 1) {
                  fontOut.drawString(x + 1.0F + stringLength, y + 1.0F + stringHeight, finalString, shadowColor);
               } else {
                  fontOut.drawString(x + size / 2 + stringLength, y + size / 2 + stringHeight, finalString, shadowColor);
               }

               fontOut.drawString(x + stringLength, y + stringHeight, finalString, finalColor);
               stringLength += fontOut.getWidth(this.outputTexts.get(l).get(tx));
            } else {
               int stringLengthLeft = 0;
               int stringLengthRight = 0;

               for (int c = 0; c < outputTextsSize; c++) {
                  if (c < tx) {
                     stringLengthLeft += fontOut.getWidth(outputTextsArray.get(c));
                  } else if (c > tx) {
                     stringLengthRight += fontOut.getWidth(outputTextsArray.get(c));
                  }
               }

               int stringAdjustmentFinal = (stringLengthLeft - stringLengthRight) / 2;
               int fontWidth = fontOut.getWidth(finalString);
               if (size <= 1) {
                  fontOut.drawString(x + stringAdjustmentFinal - fontWidth / 2 + 1.0F, y + stringHeight + 1.0F, finalString, shadowColor);
               } else {
                  fontOut.drawString(x + stringAdjustmentFinal - fontWidth / 2 + size / 2, y + stringHeight + size / 2, finalString, shadowColor);
               }

               fontOut.drawString(x + stringAdjustmentFinal - fontWidth / 2, y + stringHeight, finalString, finalColor);
            }
         }

         stringHeight += fontOut.getLineHeight();
      }

      if (ScaleControl.getInterfaceScale() >= 2.0F) {
         this.g.resetTransform();
         this.g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
      }
   }

   public TrueTypeFont getFont(Text.FontType type, int size) {
      return this.unicodeFonts.get(type).get(size);
   }
}
