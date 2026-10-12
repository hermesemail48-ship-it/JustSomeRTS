package rtr.map;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;
import r2d.image.SpriteSheet;
import rtr.ImageLoader;
import rtr.ModuleBase;
import rtr.gui.GUIEnums;
import rtr.gui.states.BrushLoader;
import rtr.mobs.MobBase;
import rtr.resources.ResourceBase;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.utilities.ArrayFillUtil;
import rtr.utilities.Utilities;

public class MiniMapModule extends ModuleBase {
   private byte[][] refreshMap;
   private ImageBuffer miniMapBufferBottom;
   private ImageBuffer miniMapBufferTop;
   private Image miniMapBottom;
   private Image miniMapTop;
   private Image miniMapFull;
   private int mapHeight;
   private int mapWidth;
   private int mapLayers;
   private int mapRefresh = 11;
   private SpriteSheet selectionEdges;
   private Image selectionTopLeft;
   private Image selectionTopRight;
   private Image selectionBottomLeft;
   private Image selectionBottomRight;
   private Color color;

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      this.buildMap();
      if (this.selectionEdges == null) {
         this.selectionEdges = new SpriteSheet(ImageLoader.getImage("res/GUI/miniMapScreenEdges.png"), 8, 8);
         this.selectionTopRight = this.selectionEdges.getSprite(1, 0);
         this.selectionTopLeft = this.selectionEdges.getSprite(0, 0);
         this.selectionBottomRight = this.selectionEdges.getSprite(1, 1);
         this.selectionBottomLeft = this.selectionEdges.getSprite(0, 1);
      }
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
      ArrayFillUtil.fill(this.refreshMap, (byte)0);
      this.miniMapBufferBottom = null;
      this.miniMapBufferTop = null;
      this.miniMapBottom = null;
      this.miniMapTop = null;
      this.miniMapFull = null;
      this.mapHeight = 0;
      this.mapWidth = 0;
      this.mapLayers = 0;
      this.mapRefresh = 11;
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

   public void update() throws SlickException {
      this.mapRefresh++;
      if (this.mapRefresh == 10) {
         this.updateMap();
      }
   }

   public void render(float x, float y, int scale, Graphics g, GUIEnums.InterfaceMode interfaceMode, boolean hideTopography) {
      g.setWorldClip(x, y, 256 * scale, 256 * scale);
      int mouseX = Game.getCS().getMouseTileX();
      int mouseY = Game.getCS().getMouseTileY();
      int brushSize = Game.getCS().getBrushLoader().getBrushSize();
      BrushLoader.BrushType brushType = Game.getCS().getBrushLoader().getBrushType();
      byte[][] currentBrush = Game.getCS().getBrushLoader().getBrush(brushSize, brushType);
      g.setColor(Color.black);
      g.fillRect(x, y, 256 * scale, 256 * scale);
      this.miniMapBottom.draw(x, y, scale);
      if (!hideTopography) {
         this.miniMapTop.draw(x, y, scale);
      }

      this.renderEntities(x, y, scale, g);
      this.selectionTopLeft.draw(x + this.map.getMapRenderTileTopLeftX() * scale, y + this.map.getMapRenderTileTopLeftY() * scale, scale);
      this.selectionTopRight
         .draw(x + this.map.getMapRenderTileTopRightX() * scale - this.selectionTopRight.getWidth(), y + this.map.getMapRenderTileTopRightY() * scale, scale);
      this.selectionBottomLeft
         .draw(
            x + this.map.getMapRenderTileBottomLeftX() * scale,
            y + this.map.getMapRenderTileBottomLeftY() * scale - this.selectionBottomLeft.getHeight(),
            scale
         );
      this.selectionBottomRight
         .draw(
            x + this.map.getMapRenderTileBottomRightX() * scale - this.selectionBottomRight.getWidth(),
            y + this.map.getMapRenderTileBottomRightY() * scale - this.selectionBottomRight.getHeight(),
            scale
         );
      g.setColor(new Color(0, 0, 0, 150));
      g.fillRect(x + mouseX * scale, y + (1 + mouseY * scale), scale, scale);
      g.setColor(Color.orange);
      g.fillRect(x + mouseX * scale, y + mouseY * scale, scale, scale);
      if (interfaceMode == GUIEnums.InterfaceMode.BRUSH
         || interfaceMode == GUIEnums.InterfaceMode.ERASE
         || interfaceMode == GUIEnums.InterfaceMode.PATCH
         || interfaceMode == GUIEnums.InterfaceMode.HOLE) {
         g.setColor(new Color(0, 0, 0, 150));

         for (int x2 = 0; x2 < currentBrush.length; x2++) {
            for (int y2 = 0; y2 < currentBrush[x2].length; y2++) {
               if (currentBrush[x2][y2] >= 2) {
                  g.fillRect(x + (x2 + mouseX - currentBrush.length / 2) * scale, y + (y2 + 1 + mouseY - currentBrush[x2].length / 2) * scale, scale, scale);
               }
            }
         }

         g.setColor(Color.orange);

         for (int x2 = 0; x2 < currentBrush.length; x2++) {
            for (int y2x = 0; y2x < currentBrush[x2].length; y2x++) {
               if (currentBrush[x2][y2x] >= 2) {
                  g.fillRect(x + (x2 + mouseX - currentBrush.length / 2) * scale, y + (y2x + mouseY - currentBrush[x2].length / 2) * scale, scale, scale);
               }
            }
         }
      }

      g.clearWorldClip();
   }

   private void renderEntities(float x, float y, int scale, Graphics g) {
      Color shadow = new Color(0, 0, 0, 130);
      g.glBeginQuads();
      if (this.resource != null) {
         ArrayList<ResourceBase> resourcesOnGround = this.resource.getResourcesOnGround();

         for (int i = 0; i < resourcesOnGround.size(); i++) {
            float tileX = x + resourcesOnGround.get(i).getTileX() * scale;
            float tileY = y + resourcesOnGround.get(i).getTileY() * scale;
            g.setColor(shadow);
            g.fillRectEmbedded(tileX - scale, tileY - scale, scale * 3, scale * 3);
         }
      }

      if (this.mob != null) {
         for (MobBase m : this.mob.getMobArray()) {
            float mobX = x + m.getTileX() * scale;
            float mobY = y + m.getTileY() * scale;
            if (!m.isDead() && m.getAlpha() > 0.0F) {
               g.setColor(shadow);
               g.fillRectEmbedded(mobX - scale, mobY - scale, scale * 3, scale * 3);
            }
         }
      }

      if (this.resource != null) {
         ArrayList<ResourceBase> resourcesOnGround = this.resource.getResourcesOnGround();

         for (int i = 0; i < resourcesOnGround.size(); i++) {
            float tileX = x + resourcesOnGround.get(i).getTileX() * scale;
            float tileY = y + resourcesOnGround.get(i).getTileY() * scale;
            g.setColor(Color.white);
            g.fillRectEmbedded(tileX, tileY, scale, scale);
         }
      }

      if (this.mob != null) {
         for (MobBase mx : this.mob.getMobArray()) {
            float mobX = x + mx.getTileX() * scale;
            float mobY = y + mx.getTileY() * scale;
            if (!mx.isDead() && mx.getAlpha() > 0.0F) {
               if (mx.getMobGroup() == MobBase.MobGroup.MONSTER) {
                  g.setColor(Color.red);
               } else if (mx.getMobGroup() == MobBase.MobGroup.NOMAD
                  || mx.getMobGroup() == MobBase.MobGroup.PROVISIONER
                  || mx.getMobGroup() == MobBase.MobGroup.MIGRANT) {
                  g.setColor(Color.cyan);
               } else if (mx.getMobGroup() == MobBase.MobGroup.VILLAGER) {
                  g.setColor(Color.green);
               } else if (mx.getMobGroup() == MobBase.MobGroup.GOLEM || mx.getMobGroup() == MobBase.MobGroup.CHARMED_MONSTER) {
                  g.setColor(Color.orange);
               } else if (mx.getMobGroup() != MobBase.MobGroup.ANIMAL && mx.getMobGroup() != MobBase.MobGroup.DOMESTICATED_ANIMAL) {
                  g.setColor(Color.white);
               } else {
                  g.setColor(Color.yellow);
               }

               g.fillRectEmbedded(mobX, mobY, scale, scale);
            }
         }
      }

      g.glEnd();
   }

   private void buildMap() throws SlickException {
      this.mapHeight = 256;
      this.mapWidth = 256;
      this.mapLayers = 12;
      this.refreshMap = new byte[this.mapWidth][this.mapHeight];
      this.miniMapBufferBottom = new ImageBuffer(this.mapWidth, this.mapHeight);
      this.miniMapBufferTop = new ImageBuffer(this.mapWidth, this.mapHeight);
      this.miniMapFull = new Image(this.mapWidth, this.mapHeight);

      for (int x = 0; x < this.mapWidth; x++) {
         for (int y = 0; y < this.mapHeight; y++) {
            this.setColor(x, y);
         }
      }

      this.miniMapBottom = this.miniMapBufferBottom.getImage(9728);
      this.miniMapTop = this.miniMapBufferTop.getImage(9728);
      Graphics mapMerge = this.miniMapFull.getGraphics();
      mapMerge.drawImage(this.miniMapBottom, 0.0F, 0.0F);
      mapMerge.drawImage(this.miniMapTop, 0.0F, 0.0F);
      this.renderEntities(0.0F, 0.0F, 1, mapMerge);
      mapMerge.flush();
   }

   private void updateMap() throws SlickException {
      this.miniMapBottom.destroy();
      this.miniMapTop.destroy();

      for (int x = 0; x < this.mapWidth; x++) {
         for (int y = 0; y < this.mapHeight; y++) {
            if (this.refreshMap[x][y] != 0) {
               this.setColor(x, y);
            }
         }
      }

      this.miniMapBottom = this.miniMapBufferBottom.getImage(9728);
      this.miniMapTop = this.miniMapBufferTop.getImage(9728);
      Graphics mapMerge = this.miniMapFull.getGraphics();
      mapMerge.clearArea(0, 0, this.miniMapFull.getWidth(), this.miniMapFull.getHeight());
      mapMerge.drawImage(this.miniMapBottom, 0.0F, 0.0F);
      mapMerge.drawImage(this.miniMapTop, 0.0F, 0.0F);
      this.renderEntities(0.0F, 0.0F, 1, mapMerge);
      mapMerge.flush();
      this.refreshMap = new byte[this.mapWidth][this.mapHeight];
   }

   private void setColor(int x, int y) {
      this.miniMapBufferBottom.setRGBA(x, y, 0, 0, 0, 0);
      this.miniMapBufferTop.setRGBA(x, y, 0, 0, 0, 0);

      for (int l = this.mapLayers - 1; l >= 0; l--) {
         int tileID = this.map.getTileId(x, y, l);
         if (tileID != 0) {
            int randomColor = Utilities.randomInt(15);
            this.color = this.map.getMapTileLoader().getTileMiniMapColor(tileID);
            if (this.color == null) {
               System.out.println(x + " " + y + " " + tileID);
            }

            int redCheck = this.color.getRed() + randomColor;
            int red = redCheck > 255 ? 255 : redCheck;
            int greenCheck = this.color.getGreen() + randomColor;
            int green = greenCheck > 255 ? 255 : greenCheck;
            int blueCheck = this.color.getBlue() + randomColor;
            int blue = blueCheck > 255 ? 255 : blueCheck;
            int roadTile = this.map.getTileId(x, y, 5);
            if (l <= 5) {
               this.miniMapBufferBottom.setRGBA(x, y, red, green, blue, 255);
               break;
            }

            if (l >= 5 && roadTile == 0) {
               int tileSetThis = this.map.getMapTileLoader().getTileSetGID(tileID);
               if ((l == 6 || l == 7 || l == 9 || l == 10)
                  && this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[x][y] > 0
                  && (
                     this.map.getMapTileLoader().getTileSetGID(this.map.getTileId(x + 1, y, l)) != tileSetThis
                        || this.map.getMapTileLoader().getTileSetGID(this.map.getTileId(x - 1, y, l)) != tileSetThis
                        || this.map.getMapTileLoader().getTileSetGID(this.map.getTileId(x, y + 1, l)) != tileSetThis
                        || this.map.getMapTileLoader().getTileSetGID(this.map.getTileId(x, y - 1, l)) != tileSetThis
                  )) {
                  if (l != 7 && l != 10) {
                     this.miniMapBufferTop.setRGBA(x, y, red / 2, green / 2, blue / 2, 255);
                  } else {
                     this.miniMapBufferTop.setRGBA(x, y, (int)(red * 0.6), (int)(green * 0.6), (int)(blue * 0.6), 255);
                  }
               } else {
                  this.miniMapBufferTop.setRGBA(x, y, red, green, blue, 255);
               }
               break;
            }
         }
      }
   }

   public Image getMiniMapFull() throws SlickException {
      return this.miniMapFull;
   }

   public void flagForUpdate(int tileX, int tileY) {
      this.refreshMap[tileX][tileY] = 1;
      this.mapRefresh = 0;
   }
}
