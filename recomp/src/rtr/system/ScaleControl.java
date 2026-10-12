package rtr.system;

import org.lwjgl.opengl.Display;
import rtr.ModuleBase;
import rtr.SettingsParser;
import rtr.gui.states.WorldMapGUIController;
import rtr.map.MapModule;
import rtr.states.StateBase;
import rtr.states.WorldMapState;
import rtr.system.gamemodetemplates.GameModeTemplateBase;

public class ScaleControl {
   public static int MIN_WIDTH = 1280;
   public static int MIN_HEIGHT = 720;
   private static int interfaceWidth;
   private static int interfaceHeight;
   private static float interfaceScale;
   private static int worldWidth;
   private static int worldHeight;
   private static float worldScale;
   private static float setWorldScale;

   public static void update() {
      if (Math.abs(setWorldScale - worldScale) != 0.0F) {
         float originalWidth = getWorldWidth();
         float originalHeight = getWorldHeight();
         float speed = 0.2F;
         if (Math.abs(setWorldScale - worldScale) > 2.0F) {
            speed = 0.6F;
         } else if (Math.abs(setWorldScale - worldScale) > 1.0F) {
            speed = 0.4F;
         }

         if (setWorldScale > worldScale) {
            worldScale += speed;
         } else if (setWorldScale < worldScale) {
            worldScale -= speed;
         }

         if (Math.abs(setWorldScale - worldScale) < speed) {
            worldScale = setWorldScale;
         }

         worldWidth = (int)(Display.getWidth() / worldScale);
         worldHeight = (int)(Display.getHeight() / worldScale);
         zoomUpdate(originalWidth, originalHeight, speed);
      }
   }

   private static void zoomUpdate(float originalWidth, float originalHeight, float speed) {
      float adjustmentWidth = (worldWidth - originalWidth) / 2.0F;
      float adjustmentHeight = (worldHeight - originalHeight) / 2.0F;
      if (Game.getCS().getGameMode() != GameModeTemplateBase.GameMode.WORLD_MAP) {
         Game.getCS();
         MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
         if (map.isInitialized()) {
            float mapX = map.getMapX();
            float mapY = map.getMapY();
            map.setMapPosition(mapX + adjustmentWidth, mapY + adjustmentHeight);
            map.realignMapPosition();
         }
      } else {
         WorldMapState state = (WorldMapState)Game.getCS();
         WorldMapGUIController gui = (WorldMapGUIController)state.getGUI();
         gui.getWorldMap().setMapPosition(gui.getWorldMap().getMapX() + adjustmentWidth, gui.getWorldMap().getMapY() + adjustmentHeight);
         gui.getWorldMap().realignMapPosition();
      }
   }

   public static void initDisplay() {
      setWorldScale(2, true);
      setDisplay();
      if (interfaceScale > 1.0F && SettingsParser.getForceInterfaceScale() == -1.0F) {
         while (Display.getWidth() > 1280.0F * interfaceScale || Display.getHeight() > 720.0F * interfaceScale) {
            interfaceScale++;
         }

         while (Display.getWidth() < 1280.0F * interfaceScale || Display.getHeight() < 720.0F * interfaceScale) {
            interfaceScale--;
         }

         if (interfaceScale < 1.0F) {
            interfaceScale = 1.0F;
         }
      }
   }

   public static void setDisplay() {
      if (SettingsParser.getForceInterfaceScale() != -1.0F) {
         interfaceScale = SettingsParser.getForceInterfaceScale();

         while (interfaceScale > 1.0 && (MIN_HEIGHT * interfaceScale >= Display.getHeight() || MIN_WIDTH * interfaceScale >= Display.getWidth())) {
            interfaceScale = (float)(interfaceScale - 0.1);
         }
      } else {
         while (Display.getWidth() > 1280.0F * interfaceScale || Display.getHeight() > 720.0F * interfaceScale) {
            interfaceScale++;
         }

         while (Display.getWidth() < 1280.0F * interfaceScale || Display.getHeight() < 720.0F * interfaceScale) {
            interfaceScale--;
         }

         if (interfaceScale < 1.0F) {
            interfaceScale = 1.0F;
         }

         interfaceScale = (int)interfaceScale;
         System.out.println("Interface scale auto adjusted to: " + interfaceScale);
      }

      if (interfaceScale < 1.0F) {
         interfaceScale = 1.0F;
      }

      interfaceWidth = (int)(Display.getWidth() / interfaceScale);
      interfaceHeight = (int)(Display.getHeight() / interfaceScale);
      worldWidth = (int)(Display.getWidth() / worldScale);
      worldHeight = (int)(Display.getHeight() / worldScale);
   }

   public static void setWorldScale(int i) {
      setWorldScale(i, false);
   }

   public static void setWorldScale(int i, boolean forced) {
      if (forced) {
         worldScale = i;
      }

      setWorldScale = i;
   }

   public static void forceSetInterfaceScale(int i) {
      interfaceScale = i;
   }

   public static void increaseWorldScale() {
      if (setWorldScale < 8.0F) {
         setWorldScale++;
      }
   }

   public static void decreaseWorldScale() {
      if (setWorldScale > interfaceScale) {
         setWorldScale--;
      }
   }

   public static int getInterfaceWidth() {
      return interfaceWidth;
   }

   public static int getInterfaceHeight() {
      return interfaceHeight;
   }

   public static int getInterfaceCenterX() {
      return interfaceWidth / 2;
   }

   public static int getInterfaceCenterY() {
      return interfaceHeight / 2;
   }

   public static float getInterfaceScale() {
      return interfaceScale;
   }

   public static int getWorldWidth() {
      return worldWidth;
   }

   public static int getWorldHeight() {
      return worldHeight;
   }

   public static int getWorldCenterX() {
      return worldWidth / 2;
   }

   public static int getWorldCenterY() {
      return worldHeight / 2;
   }

   public static float getWorldScale() {
      return worldScale;
   }

   public static float getScalingDifference() {
      return worldScale / interfaceScale;
   }

   public static float getTransformedPositionX(float x) {
      MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
      return (map.getMapX() + x) * getScalingDifference();
   }

   public static float getTransformedPositionY(float y) {
      MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
      return (map.getMapY() + y) * getScalingDifference();
   }

   public static float getTransformedPosition(float p) {
      return p * getScalingDifference();
   }
}
