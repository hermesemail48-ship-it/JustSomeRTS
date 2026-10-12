package rtr.gui.states.shared;

import java.util.concurrent.TimeUnit;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.font.Text;
import rtr.gui.states.MainMenuGUIController;
import rtr.gui.states.MainMenuGUIData;
import rtr.gui.states.MapEditorGUIController;
import rtr.gui.states.MapEditorGUIData;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.SharedGUIPanelBase;
import rtr.gui.states.WorldMapGUIController;
import rtr.gui.states.WorldMapGUIData;
import rtr.system.Game;
import rtr.system.ScaleControl;

public class DebugData extends SharedGUIPanelBase {
   protected Runtime runtime = Runtime.getRuntime();

   public DebugData(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
   }

   public DebugData(MapEditorGUIController controller, MapEditorGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
   }

   public DebugData(MainMenuGUIController controller, MainMenuGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
   }

   public DebugData(WorldMapGUIController controller, WorldMapGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
   }

   public void render(float x, float y) {
      int mouseTileX = Game.getCS().getMouseTileX();
      int mouseTileY = Game.getCS().getMouseTileY();
      this.g.setColor(new Color(0, 0, 0, 150));
      this.g.fillRect(x - 5.0F, y + 5.0F, 320.0F, 370.0F);
      int alignY = 0;
      Text.setVariableText("fps", this.gc.getFPS());
      this.font.drawString(x, y + 15.0F + alignY, Text.getText("sharedDebugData.fps"), Text.FontType.BODY, 4);
      alignY += 25;
      long runtimeInSeconds = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime()) - Game.getStartTimeInSeconds();
      Text.setVariableText("hours", (double)(runtimeInSeconds / 60L / 60L));
      Text.setVariableText("minutes", (double)(runtimeInSeconds / 60L % 60L));
      Text.setVariableText("seconds", (double)(runtimeInSeconds % 60L));
      this.font.drawString(x, y + 15.0F + alignY, Text.getText("sharedDebugData.totalRuntime"), Text.FontType.BODY, 1);
      alignY += 20;
      Text.setVariableText("xPosition", (double)this.map.getMapX());
      Text.setVariableText("yPosition", (double)this.map.getMapY());
      this.font.drawString(x, y + 15.0F + alignY, Text.getText("sharedDebugData.mapPosition"), Text.FontType.BODY, 1);
      alignY += 20;
      Text.setVariableText("amount", this.particle.getParticleCount());
      this.font.drawString(x, y + 15.0F + alignY, Text.getText("sharedDebugData.particleCount"), Text.FontType.BODY, 1);
      alignY += 20;
      this.font.drawString(x, y + 15.0F + alignY, Text.getText("sharedDebugData.mousePosition"), Text.FontType.BODY, 1);
      this.font.drawString(x + 90.0F, y + 15.0F + alignY, "X$YEL1" + Game.getCS().getMouseX(), Text.FontType.BODY, 1);
      this.font.drawString(x + 135.0F, y + 15.0F + alignY, "Y$YEL1" + Game.getCS().getMouseY(), Text.FontType.BODY, 1);
      alignY += 10;
      this.font.drawString(x, y + 15.0F + alignY, Text.getText("sharedDebugData.mouseTile"), Text.FontType.BODY, 1);
      this.font.drawString(x + 90.0F, y + 15.0F + alignY, "X$YEL1" + mouseTileX, Text.FontType.BODY, 1);
      this.font.drawString(x + 135.0F, y + 15.0F + alignY, "Y$YEL1" + mouseTileY, Text.FontType.BODY, 1);
      alignY += 10;
      this.font.drawString(x, y + 15.0F + alignY, Text.getText("sharedDebugData.mouseMap"), Text.FontType.BODY, 1);
      this.font.drawString(x + 90.0F, y + 15.0F + alignY, "X$YEL1" + Game.getCS().getMouseOnMapX(), Text.FontType.BODY, 1);
      this.font.drawString(x + 135.0F, y + 15.0F + alignY, "Y$YEL1" + Game.getCS().getMouseOnMapY(), Text.FontType.BODY, 1);
      alignY += 20;
      Text.setVariableText("gpuModelName", GL11.glGetString(7937));
      this.font.drawString(x, y + 15.0F + alignY, Text.getText("sharedDebugData.gpuModel"), Text.FontType.BODY, 1);
      alignY += 20;
      this.font.drawString(x, y + 15.0F + alignY, Text.getText("sharedDebugData.usedMemory"), Text.FontType.BODY, 1);
      this.font.drawString(x + 85.0F, y + 15.0F + alignY, "$YEL1" + (this.runtime.totalMemory() - this.runtime.freeMemory()) / 1048576L, Text.FontType.BODY, 1);
      alignY += 10;
      this.font.drawString(x, y + 15.0F + alignY, Text.getText("sharedDebugData.freeMemory"), Text.FontType.BODY, 1);
      this.font.drawString(x + 85.0F, y + 15.0F + alignY, "$YEL1" + this.runtime.freeMemory() / 1048576L, Text.FontType.BODY, 1);
      alignY += 10;
      this.font.drawString(x, y + 15.0F + alignY, Text.getText("sharedDebugData.totalMemory"), Text.FontType.BODY, 1);
      this.font.drawString(x + 85.0F, y + 15.0F + alignY, "$YEL1" + this.runtime.totalMemory() / 1048576L, Text.FontType.BODY, 1);
      alignY += 10;
      this.font.drawString(x, y + 15.0F + alignY, Text.getText("sharedDebugData.maxMemory"), Text.FontType.BODY, 1);
      this.font.drawString(x + 85.0F, y + 15.0F + alignY, "$YEL1" + this.runtime.maxMemory() / 1048576L, Text.FontType.BODY, 1);
      alignY += 20;
      Text.setVariableText("width", Display.getWidth());
      Text.setVariableText("height", Display.getHeight());
      this.font.drawString(x, y + 15.0F + alignY, Text.getText("sharedDebugData.resolution"), Text.FontType.BODY, 1);
      alignY += 10;
      this.font.drawString(x, y + 15.0F + alignY, Text.getText("sharedDebugData.uiScale"), Text.FontType.BODY, 1);
      this.font.drawString(x + 70.0F, y + 15.0F + alignY, "$YEL1" + ScaleControl.getInterfaceScale(), Text.FontType.BODY, 1);
      alignY += 10;
      this.font.drawString(x, y + 15.0F + alignY, Text.getText("sharedDebugData.worldScale"), Text.FontType.BODY, 1);
      this.font.drawString(x + 70.0F, y + 15.0F + alignY, "$YEL1" + ScaleControl.getWorldScale(), Text.FontType.BODY, 1);
      alignY += 20;

      try {
         this.font.drawString(x, y + 15.0F + alignY, Text.getText("sharedDebugData.terrain.baseTerrain"), Text.FontType.BODY, 1);
         this.font.drawString(x + 125.0F, y + 15.0F + alignY, "$BLU0" + this.map.getTileId(mouseTileX, mouseTileY, 0), Text.FontType.BODY, 1);
         alignY += 10;
         this.font.drawString(x, y + 15.0F + alignY, Text.getText("sharedDebugData.terrain.baseTerrainAccent"), Text.FontType.BODY, 1);
         this.font.drawString(x + 125.0F, y + 15.0F + alignY, "$BLU0" + this.map.getTileId(mouseTileX, mouseTileY, 1), Text.FontType.BODY, 1);
         alignY += 10;
         this.font.drawString(x, y + 15.0F + alignY, Text.getText("sharedDebugData.terrain.terrain"), Text.FontType.BODY, 1);
         this.font.drawString(x + 125.0F, y + 15.0F + alignY, "$BLU0" + this.map.getTileId(mouseTileX, mouseTileY, 2), Text.FontType.BODY, 1);
         alignY += 10;
         this.font.drawString(x, y + 15.0F + alignY, Text.getText("sharedDebugData.terrain.terrainAccent"), Text.FontType.BODY, 1);
         this.font.drawString(x + 125.0F, y + 15.0F + alignY, "$BLU0" + this.map.getTileId(mouseTileX, mouseTileY, 3), Text.FontType.BODY, 1);
         alignY += 10;
         this.font.drawString(x, y + 15.0F + alignY, Text.getText("sharedDebugData.terrain.corruption"), Text.FontType.BODY, 1);
         this.font.drawString(x + 125.0F, y + 15.0F + alignY, "$BLU0" + this.map.getTileId(mouseTileX, mouseTileY, 4), Text.FontType.BODY, 1);
         alignY += 10;
         this.font.drawString(x, y + 15.0F + alignY, Text.getText("sharedDebugData.terrain.roads"), Text.FontType.BODY, 1);
         this.font.drawString(x + 125.0F, y + 15.0F + alignY, "$BLU0" + this.map.getTileId(mouseTileX, mouseTileY, 5), Text.FontType.BODY, 1);
         alignY += 10;
         this.font.drawString(x, y + 15.0F + alignY, Text.getText("sharedDebugData.terrain.lowerTopography"), Text.FontType.BODY, 1);
         this.font.drawString(x + 125.0F, y + 15.0F + alignY, "$BLU0" + this.map.getTileId(mouseTileX, mouseTileY, 6), Text.FontType.BODY, 1);
         alignY += 10;
         this.font.drawString(x, y + 15.0F + alignY, Text.getText("sharedDebugData.terrain.lowerTopographyAccent"), Text.FontType.BODY, 1);
         this.font.drawString(x + 125.0F, y + 15.0F + alignY, "$BLU0" + this.map.getTileId(mouseTileX, mouseTileY, 7), Text.FontType.BODY, 1);
         alignY += 10;
         this.font.drawString(x, y + 15.0F + alignY, Text.getText("sharedDebugData.terrain.bridges"), Text.FontType.BODY, 1);
         this.font.drawString(x + 125.0F, y + 15.0F + alignY, "$BLU0" + this.map.getTileId(mouseTileX, mouseTileY, 8), Text.FontType.BODY, 1);
         alignY += 10;
         this.font.drawString(x, y + 15.0F + alignY, Text.getText("sharedDebugData.terrain.upperTopography"), Text.FontType.BODY, 1);
         this.font.drawString(x + 125.0F, y + 15.0F + alignY, "$BLU0" + this.map.getTileId(mouseTileX, mouseTileY, 9), Text.FontType.BODY, 1);
         alignY += 10;
         this.font.drawString(x, y + 15.0F + alignY, Text.getText("sharedDebugData.terrain.upperTopographyAccent"), Text.FontType.BODY, 1);
         this.font.drawString(x + 125.0F, y + 15.0F + alignY, "$BLU0" + this.map.getTileId(mouseTileX, mouseTileY, 10), Text.FontType.BODY, 1);
         alignY += 10;
         this.font.drawString(x, y + 15.0F + alignY, Text.getText("sharedDebugData.terrain.objects"), Text.FontType.BODY, 1);
         this.font.drawString(x + 125.0F, y + 15.0F + alignY, "$BLU0" + this.map.getTileId(mouseTileX, mouseTileY, 11), Text.FontType.BODY, 1);
         alignY += 10;
      } catch (Exception var9) {
         this.font.drawString(x, y + 15.0F + alignY, Text.getText("sharedDebugData.terrain.outOfBounds"), Text.FontType.BODY, 1);
         alignY += 10;
      }
   }
}
