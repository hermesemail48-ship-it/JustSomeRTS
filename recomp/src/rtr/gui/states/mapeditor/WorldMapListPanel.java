package rtr.gui.states.mapeditor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.gui.buttons.GUIClickableText;
import rtr.gui.states.MapEditorGUIController;
import rtr.gui.states.MapEditorGUIData;
import rtr.gui.states.MapEditorGUIPanelBase;

public class WorldMapListPanel extends MapEditorGUIPanelBase {
   private Image panel = ImageLoader.getImage("res/GUI/gameState/dataViewsDebugPanel.png");
   private Image panelMiniMap = ImageLoader.getImage("res/GUI/gameState/miniMapPanel.png");
   private Rectangle mask = new Rectangle(0.0F, 0.0F, this.panel.getWidth(), this.panel.getHeight());
   private Rectangle maskMiniMap;
   private ArrayList<GUIClickableText> worldMapButtonList;
   private ArrayList<String> worldMapNameList;
   private ArrayList<Image> worldMapMiniMapsList;

   public WorldMapListPanel(MapEditorGUIController controller, MapEditorGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.masks.add(this.mask);
      this.maskMiniMap = new Rectangle(0.0F, 0.0F, this.panelMiniMap.getWidth(), this.panelMiniMap.getHeight());
      this.masks.add(this.maskMiniMap);
      this.worldMapButtonList = new ArrayList<>();
      this.worldMapNameList = new ArrayList<>();
      this.worldMapMiniMapsList = new ArrayList<>();
      String mapPackFolderName = "WorldMaps";
      File[] listOfMaps = new File("maps/" + mapPackFolderName).listFiles();

      for (int i = 0; i < listOfMaps.length; i++) {
         if (listOfMaps[i].isFile() && listOfMaps[i].toString().contains(".zip")) {
            File zipFile = listOfMaps[i];

            try {
               ZipFile zip = new ZipFile(zipFile);
               ZipEntry zipEntry = zip.getEntry("minimap.bmp");
               InputStream minimapInputStream = zip.getInputStream(zipEntry);
               this.worldMapMiniMapsList.add(new Image(minimapInputStream, minimapInputStream.toString(), false));
               this.worldMapNameList.add(listOfMaps[i].getName().replaceAll(".zip", ""));
               zip.close();
               minimapInputStream.close();
               GUIClickableText newButton = new GUIClickableText("worldMapList" + i, 1);
               this.worldMapButtonList.add(newButton);
               this.guiButtons.add(newButton);
            } catch (IOException var14) {
               System.out.println("Corrupt or empty map folder found: maps/" + mapPackFolderName + "/" + listOfMaps[i].getName() + "/");
               var14.printStackTrace();
            }
         }
      }
   }

   public void render(boolean debug) throws SlickException {
      this.x = this.controller.getSystemAndDataBar().getX() + this.panel.getWidth() - 228;
      if (this.x + this.panel.getWidth() > this.controller.getRightPanel().getLeftX()) {
         this.x = this.x - Math.abs(this.x + this.panel.getWidth() - this.controller.getRightPanel().getLeftX());
      }

      this.y = 49;
      this.mask.setX(this.x);
      this.mask.setY(this.y);
      this.maskMiniMap.setX(this.x - 300);
      this.maskMiniMap.setY(this.y + 29);
      this.panel.draw(this.x, this.y);
      this.font.drawString(this.x + 93, this.y + 3, Text.getText("mapEditorWorldMapListPanel.header.regionList"), Text.FontType.HEADER, 2, true);
      int alignY = 0;
      this.g.setColor(Color.black);
      this.g.fillRect(this.x - 283, this.y + 57, 256.0F, 256.0F);

      for (int i = 0; i < this.worldMapButtonList.size(); i++) {
         this.worldMapButtonList
            .get(i)
            .render(this.g, this.mouse, this.worldMapNameList.get(i), this.x + 24, this.y + 29 + alignY, "$GRE1", "$WHI0", false, debug);
         if (this.worldMapButtonList.get(i).intersects(this.mouse)) {
            this.worldMapMiniMapsList.get(i).draw(this.x - 283, this.y + 57);
         }

         alignY += 11;
      }

      this.panelMiniMap.draw(this.x - 300, this.y + 29);
      this.font.drawString(this.x - 210, this.y + 32, Text.getText("mapEditorWorldMapListPanel.header.miniMap"), Text.FontType.HEADER, 2, true);
   }

   public String getIntersectWorldMapName() {
      for (int i = 0; i < this.worldMapButtonList.size(); i++) {
         if (this.worldMapButtonList.get(i).intersects(this.mouse)) {
            return this.worldMapNameList.get(i);
         }
      }

      return null;
   }

   @Override
   public int getCenterX() {
      return this.x + this.panel.getWidth() / 2;
   }

   @Override
   public int getCenterY() {
      return this.y + this.panel.getHeight() / 2;
   }

   @Override
   public int getLeftX() {
      return this.x;
   }

   @Override
   public int getTopY() {
      return this.y;
   }

   @Override
   public int getRightX() {
      return this.x + this.panel.getWidth();
   }

   @Override
   public int getBottomY() {
      return this.y + this.panel.getHeight();
   }
}
