package rtr;

import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.map.MapData;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.ObjectModule;
import rtr.objects.objectflags.ObjectFlags;
import rtr.objects.rangebuilder.RangeBuilder;
import rtr.objects.towerhead.TowerBase;
import rtr.objects.towerhead.towerheadflags.TowerHeadFlags;
import rtr.resources.ResourceModule;
import rtr.states.StateBase;
import rtr.system.ScaleControl;
import rtr.system.gamemodetemplates.GameModeTemplateBase;
import rtr.utilities.Utilities;

public class DataModule extends ModuleBase {
   private DataModule.DataDisplayMode displayMode = DataModule.DataDisplayMode.NONE;
   private DataModule.DataDisplayMode previousDisplayMode = DataModule.DataDisplayMode.NONE;
   private DataModule.DataDisplayMode lockedDisplayMode = DataModule.DataDisplayMode.NONE;
   private DataModule.DataDisplayMode previousLockedDisplayMode = DataModule.DataDisplayMode.NONE;
   private Image gridBox;

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      if (this.gridBox == null) {
         this.gridBox = ImageLoader.getImage("res/GUI/gridBox.png");
      }

      this.initLoad();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
      this.displayMode = DataModule.DataDisplayMode.NONE;
      this.lockedDisplayMode = DataModule.DataDisplayMode.NONE;
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
      if (this.displayMode != this.previousDisplayMode || this.lockedDisplayMode != this.previousLockedDisplayMode) {
         if (this.displayMode.requiresRefresh
            || this.previousDisplayMode.requiresRefresh
            || this.lockedDisplayMode.requiresRefresh
            || this.previousLockedDisplayMode.requiresRefresh) {
            this.map.refreshFullMap();
         }

         this.previousDisplayMode = this.displayMode;
         this.previousLockedDisplayMode = this.lockedDisplayMode;
      }
   }

   public void render(Graphics g, boolean debug) throws SlickException {
      float mapX = this.map.getMapX();
      float mapY = this.map.getMapY();
      int startX = this.map.getMapRenderTileTopLeftX() - 4;
      int startY = this.map.getMapRenderTileTopLeftY() - 4;
      int endX = this.map.getMapRenderTileBottomRightX() + 4;
      int endY = this.map.getMapRenderTileBottomRightY() + 4;
      if (startX < 0) {
         startX = 0;
      }

      if (startY < 0) {
         startY = 0;
      }

      if (endX > 255) {
         endX = 255;
      }

      if (endY > 255) {
         endY = 255;
      }

      int startPixelX = this.map.getMapRenderTopLeftX() - 4;
      int startPixelY = this.map.getMapRenderTopLeftY() - 4;
      int endPixelX = this.map.getMapRenderBottomRightX() + 4;
      int endPixelY = this.map.getMapRenderBottomRightY() + 4;
      if (startPixelX < 0) {
         startPixelX = 0;
      }

      if (startPixelY < 0) {
         startPixelY = 0;
      }

      if (endPixelX > 4095) {
         endPixelX = 4095;
      }

      if (endPixelY > 4095) {
         endPixelY = 4095;
      }

      if (this.displayMode == DataModule.DataDisplayMode.RESOURCE_VALUE
         || this.displayMode == DataModule.DataDisplayMode.RESOURCE_HIGHLIGHT_WOOD
         || this.displayMode == DataModule.DataDisplayMode.RESOURCE_HIGHLIGHT_ROCK
         || this.displayMode == DataModule.DataDisplayMode.RESOURCE_HIGHLIGHT_FOOD_AND_WATER
         || this.displayMode == DataModule.DataDisplayMode.RESOURCE_HIGHLIGHT_CRYSTAL
         || this.lockedDisplayMode == DataModule.DataDisplayMode.RESOURCE_VALUE
         || this.lockedDisplayMode == DataModule.DataDisplayMode.RESOURCE_HIGHLIGHT_WOOD
         || this.lockedDisplayMode == DataModule.DataDisplayMode.RESOURCE_HIGHLIGHT_ROCK
         || this.lockedDisplayMode == DataModule.DataDisplayMode.RESOURCE_HIGHLIGHT_FOOD_AND_WATER
         || this.lockedDisplayMode == DataModule.DataDisplayMode.RESOURCE_HIGHLIGHT_CRYSTAL) {
         byte[][] resourceValue = this.resource.getResourceValues();
         ArrayList<ResourceModule.ResourceType> filter = new ArrayList<>();
         if (this.displayMode == DataModule.DataDisplayMode.RESOURCE_HIGHLIGHT_WOOD
            || this.lockedDisplayMode == DataModule.DataDisplayMode.RESOURCE_HIGHLIGHT_WOOD) {
            filter.add(ResourceModule.ResourceType.WOOD);
         } else if (this.displayMode == DataModule.DataDisplayMode.RESOURCE_HIGHLIGHT_ROCK
            || this.lockedDisplayMode == DataModule.DataDisplayMode.RESOURCE_HIGHLIGHT_ROCK) {
            filter.add(ResourceModule.ResourceType.ROCK);
         } else if (this.displayMode == DataModule.DataDisplayMode.RESOURCE_HIGHLIGHT_FOOD_AND_WATER
            || this.lockedDisplayMode == DataModule.DataDisplayMode.RESOURCE_HIGHLIGHT_FOOD_AND_WATER) {
            filter.add(ResourceModule.ResourceType.RAW_VEGETABLE);
            filter.add(ResourceModule.ResourceType.DIRTY_WATER_BUCKET);
         } else if (this.displayMode == DataModule.DataDisplayMode.RESOURCE_HIGHLIGHT_CRYSTAL
            || this.lockedDisplayMode == DataModule.DataDisplayMode.RESOURCE_HIGHLIGHT_CRYSTAL) {
            filter.add(ResourceModule.ResourceType.CRYSTAL);
         }

         GL11.glBlendFunc(770, 772);
         g.glBeginQuads();

         for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
               if (resourceValue[x][y] > 0 && (filter.size() <= 0 || filter.contains(this.resource.getResourceType()[x][y]))) {
                  int drawX = x * 16;
                  int drawY = y * 16;
                  int value = resourceValue[x][y];
                  Color color;
                  if (value >= 6 || filter.size() > 0) {
                     color = new Color(0, 255, 0, 25);
                  } else if (value >= 5) {
                     color = new Color(100, 255, 0, 25);
                  } else if (value >= 4) {
                     color = new Color(150, 255, 0, 25);
                  } else if (value >= 3) {
                     color = new Color(255, 255, 0, 25);
                  } else if (value >= 2) {
                     color = new Color(255, 150, 0, 25);
                  } else if (value >= 1) {
                     color = new Color(255, 100, 0, 25);
                  } else {
                     color = new Color(255, 0, 0, 25);
                  }

                  g.setColor(color);
                  g.fillRectEmbedded(drawX + mapX, drawY + mapY, 16.0F, 16.0F);
               }
            }
         }

         g.glEnd();
         GL11.glBlendFunc(770, 771);
         this.gridBox.startUse();

         for (int x = startX; x < endX; x++) {
            for (int yx = startY; yx < endY; yx++) {
               if (resourceValue[x][yx] > 0 && (filter.size() <= 0 || filter.contains(this.resource.getResourceType()[x][yx]))) {
                  int drawX = x * 16;
                  int drawY = yx * 16;
                  int value = resourceValue[x][yx];
                  Color color;
                  if (value >= 6 || filter.size() > 0) {
                     color = new Color(0, 255, 0, 100);
                  } else if (value >= 5) {
                     color = new Color(100, 255, 0, 100);
                  } else if (value >= 4) {
                     color = new Color(150, 255, 0, 100);
                  } else if (value >= 3) {
                     color = new Color(255, 255, 0, 100);
                  } else if (value >= 2) {
                     color = new Color(255, 150, 0, 100);
                  } else if (value >= 1) {
                     color = new Color(255, 100, 0, 100);
                  } else {
                     color = new Color(255, 0, 0, 100);
                  }

                  this.gridBox.drawEmbedded(drawX + mapX, drawY + mapY, drawX + mapX + 16.0F, drawY + mapY + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, color);
               }
            }
         }

         this.gridBox.endUse();
         if (debug) {
            g.resetTransform();
            g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());

            for (int x = startX; x < endX; x++) {
               for (int yxx = startY; yxx < endY; yxx++) {
                  int drawX = (int)ScaleControl.getTransformedPositionX(x * 16 + 8);
                  int drawY = (int)ScaleControl.getTransformedPositionY(yxx * 16 + 8);
                  if (resourceValue[x][yxx] > 6) {
                     this.font.drawString(drawX, drawY, "$GRE1" + resourceValue[x][yxx], Text.FontType.BODY, 0, true);
                  } else if (resourceValue[x][yxx] > 4) {
                     this.font.drawString(drawX, drawY, "$YEL1" + resourceValue[x][yxx], Text.FontType.BODY, 0, true);
                  } else if (resourceValue[x][yxx] > 2) {
                     this.font.drawString(drawX, drawY, "$ORA1" + resourceValue[x][yxx], Text.FontType.BODY, 0, true);
                  } else if (resourceValue[x][yxx] > 0) {
                     this.font.drawString(drawX, drawY, "$RED1" + resourceValue[x][yxx], Text.FontType.BODY, 0, true);
                  }
               }
            }

            g.resetTransform();
            g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
         }
      }

      if (this.displayMode == DataModule.DataDisplayMode.MOVEMENT_COST || this.lockedDisplayMode == DataModule.DataDisplayMode.MOVEMENT_COST) {
         GL11.glBlendFunc(770, 772);
         g.glBeginQuads();

         for (int x = startX; x < endX; x++) {
            for (int yxxx = startY; yxxx < endY; yxxx++) {
               if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[x][yxxx] == 0) {
                  int drawX = x * 16;
                  int drawY = yxxx * 16;
                  int movementCost = this.map.getMapData().getMovementCost(MapData.BlockMapGroup.STANDARD, x, yxxx);
                  Color color;
                  if (movementCost >= 1250) {
                     color = new Color(255, 0, 0, 75);
                  } else if (movementCost >= 1000) {
                     color = new Color(255, 100, 0, 75);
                  } else if (movementCost >= 800) {
                     color = new Color(255, 255, 0, 75);
                  } else {
                     color = new Color(0, 255, 0, 75);
                  }

                  g.setColor(color);
                  g.fillRectEmbedded(drawX + mapX, drawY + mapY, 16.0F, 16.0F);
               }
            }
         }

         g.glEnd();
         GL11.glBlendFunc(770, 771);
         this.gridBox.startUse();

         for (int x = startX; x < endX; x++) {
            for (int yxxxx = startY; yxxxx < endY; yxxxx++) {
               if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[x][yxxxx] == 0) {
                  int drawX = x * 16;
                  int drawY = yxxxx * 16;
                  int movementCost = this.map.getMapData().getMovementCost(MapData.BlockMapGroup.STANDARD, x, yxxxx);
                  Color color;
                  if (movementCost >= 1250) {
                     color = new Color(255, 0, 0, 75);
                  } else if (movementCost >= 1000) {
                     color = new Color(255, 100, 0, 75);
                  } else if (movementCost >= 800) {
                     color = new Color(255, 255, 0, 75);
                  } else {
                     color = new Color(0, 255, 0, 75);
                  }

                  this.gridBox.drawEmbedded(drawX + mapX, drawY + mapY, drawX + mapX + 16.0F, drawY + mapY + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, color);
               }
            }
         }

         this.gridBox.endUse();
      }

      if (this.displayMode == DataModule.DataDisplayMode.BLOCK_MAP || this.lockedDisplayMode == DataModule.DataDisplayMode.BLOCK_MAP) {
         GL11.glBlendFunc(770, 772);
         g.glBeginQuads();

         for (int x = startX; x < endX; x++) {
            for (int yxxxxx = startY; yxxxxx < endY; yxxxxx++) {
               if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[x][yxxxxx] > 0) {
                  int drawX = x * 16;
                  int drawY = yxxxxx * 16;
                  g.setColor(new Color(255, 0, 0, 75));
                  g.fillRectEmbedded(drawX + mapX, drawY + mapY, 16.0F, 16.0F);
               }
            }
         }

         g.glEnd();
         GL11.glBlendFunc(770, 771);
         this.gridBox.startUse();

         for (int x = startX; x < endX; x++) {
            for (int yxxxxxx = startY; yxxxxxx < endY; yxxxxxx++) {
               if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[x][yxxxxxx] > 0) {
                  int drawX = x * 16;
                  int drawY = yxxxxxx * 16;
                  this.gridBox
                     .drawEmbedded(drawX + mapX, drawY + mapY, drawX + mapX + 16.0F, drawY + mapY + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, new Color(255, 0, 0, 100));
               }
            }
         }

         this.gridBox.endUse();
      }

      if (this.displayMode == DataModule.DataDisplayMode.HIDE_TOPOGRAPHY || this.lockedDisplayMode == DataModule.DataDisplayMode.HIDE_TOPOGRAPHY) {
         GL11.glBlendFunc(770, 772);
         g.glBeginQuads();

         for (int x = startX; x < endX; x++) {
            for (int yxxxxxxx = startY; yxxxxxxx < endY; yxxxxxxx++) {
               if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[x][yxxxxxxx] > 0) {
                  int drawX = x * 16;
                  int drawY = yxxxxxxx * 16;
                  g.setColor(new Color(255, 0, 0, 25));
                  g.fillRectEmbedded(drawX + mapX, drawY + mapY, 16.0F, 16.0F);
               }
            }
         }

         g.glEnd();
         GL11.glBlendFunc(770, 771);
         this.gridBox.startUse();

         for (int x = startX; x < endX; x++) {
            for (int yxxxxxxxx = startY; yxxxxxxxx < endY; yxxxxxxxx++) {
               if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[x][yxxxxxxxx] > 0) {
                  int drawX = x * 16;
                  int drawY = yxxxxxxxx * 16;
                  this.gridBox
                     .drawEmbedded(drawX + mapX, drawY + mapY, drawX + mapX + 16.0F, drawY + mapY + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, new Color(255, 0, 0, 80));
               } else {
                  for (int l = 6; l < 12; l++) {
                     if (this.map.getMap()[x][yxxxxxxxx][l] != 0) {
                        int drawX = x * 16;
                        int drawY = yxxxxxxxx * 16;
                        this.gridBox
                           .drawEmbedded(
                              drawX + mapX, drawY + mapY, drawX + mapX + 16.0F, drawY + mapY + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, new Color(255, 255, 0, 80)
                           );
                        break;
                     }
                  }
               }
            }
         }

         this.gridBox.endUse();
      }

      if (this.displayMode == DataModule.DataDisplayMode.GRID || this.lockedDisplayMode == DataModule.DataDisplayMode.GRID) {
         GL11.glBlendFunc(770, 772);
         this.gridBox.startUse();

         for (int x = startX; x < endX; x++) {
            for (int yxxxxxxxxx = startY; yxxxxxxxxx < endY; yxxxxxxxxx++) {
               int drawX = x * 16;
               int drawY = yxxxxxxxxx * 16;
               this.gridBox
                  .drawEmbedded(drawX + mapX, drawY + mapY, drawX + mapX + 16.0F, drawY + mapY + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, new Color(255, 255, 255, 35));
            }
         }

         this.gridBox.endUse();
         GL11.glBlendFunc(770, 771);
      }

      if (this.displayMode == DataModule.DataDisplayMode.RANGE_MAP || this.lockedDisplayMode == DataModule.DataDisplayMode.RANGE_MAP) {
         int[][] rangeMap = this.object.getRangeMap(RangeBuilder.RangeMapLayer.RANGE_AND_DESIRABILITY);
         this.gridBox.startUse();

         for (int x = startX; x < endX; x++) {
            for (int yxxxxxxxxx = startY; yxxxxxxxxx < endY; yxxxxxxxxx++) {
               int drawX = x * 16;
               int drawY = yxxxxxxxxx * 16;
               if (rangeMap[x][yxxxxxxxxx] > 0) {
                  if (this.map.getMap()[x][yxxxxxxxxx][4] <= 0 && !this.object.isObject(x, yxxxxxxxxx)) {
                     this.gridBox
                        .drawEmbedded(
                           drawX + mapX, drawY + mapY, drawX + mapX + 16.0F, drawY + mapY + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, new Color(0, 255, 0, 50)
                        );
                  } else {
                     this.gridBox
                        .drawEmbedded(
                           drawX + mapX, drawY + mapY, drawX + mapX + 16.0F, drawY + mapY + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, new Color(255, 255, 0, 50)
                        );
                  }
               }

               if (rangeMap[x][yxxxxxxxxx] == -1) {
                  if (this.map.getMap()[x][yxxxxxxxxx][4] <= 0 && !this.object.isObject(x, yxxxxxxxxx)) {
                     this.gridBox
                        .drawEmbedded(
                           drawX + mapX, drawY + mapY, drawX + mapX + 16.0F, drawY + mapY + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, new Color(255, 0, 0, 75)
                        );
                  } else {
                     this.gridBox
                        .drawEmbedded(
                           drawX + mapX, drawY + mapY, drawX + mapX + 16.0F, drawY + mapY + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, new Color(255, 0, 0, 75)
                        );
                  }
               }
            }
         }

         this.gridBox.endUse();
         GL11.glBlendFunc(770, 772);
         g.glBeginQuads();

         for (int x = startX; x < endX; x++) {
            for (int yxxxxxxxxx = startY; yxxxxxxxxx < endY; yxxxxxxxxx++) {
               if (rangeMap[x][yxxxxxxxxx] > 0) {
                  int drawXx = x * 16;
                  int drawYx = yxxxxxxxxx * 16;
                  if (this.map.getMap()[x][yxxxxxxxxx][4] <= 0 && !this.object.isObject(x, yxxxxxxxxx)) {
                     g.setColor(new Color(0, 255, 0, 25));
                  } else {
                     g.setColor(new Color(255, 255, 0, 25));
                  }

                  g.fillRectEmbedded(drawXx + mapX, drawYx + mapY, 16.0F, 16.0F);
               }

               if (rangeMap[x][yxxxxxxxxx] == -1) {
                  int drawXx = x * 16;
                  int drawYx = yxxxxxxxxx * 16;
                  if (this.map.getMap()[x][yxxxxxxxxx][4] <= 0 && !this.object.isObject(x, yxxxxxxxxx)) {
                     g.setColor(new Color(255, 0, 0, 20));
                  } else {
                     g.setColor(new Color(255, 0, 0, 20));
                  }

                  g.fillRectEmbedded(drawXx + mapX, drawYx + mapY, 16.0F, 16.0F);
               }
            }
         }

         g.glEnd();
         GL11.glBlendFunc(770, 771);
      }

      if (this.displayMode == DataModule.DataDisplayMode.ESSENCE_BUILDUP_MAP || this.lockedDisplayMode == DataModule.DataDisplayMode.ESSENCE_BUILDUP_MAP) {
         int[][] values = this.mapAI.getEssenceBuildupMap();
         g.resetTransform();
         g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());

         for (int x = startX; x < endX; x++) {
            for (int yxxxxxxxxx = startY; yxxxxxxxxx < endY; yxxxxxxxxx++) {
               int drawXx = (int)ScaleControl.getTransformedPositionX(x * 16 + 8);
               int drawYx = (int)ScaleControl.getTransformedPositionY(yxxxxxxxxx * 16 + 8);
               if (values[x][yxxxxxxxxx] >= 100) {
                  this.font.drawString(drawXx, drawYx, "$GRE1" + values[x][yxxxxxxxxx], Text.FontType.BODY, 0, true);
               } else if (values[x][yxxxxxxxxx] >= 75) {
                  this.font.drawString(drawXx, drawYx, "$YEL1" + values[x][yxxxxxxxxx], Text.FontType.BODY, 0, true);
               } else if (values[x][yxxxxxxxxx] >= 25) {
                  this.font.drawString(drawXx, drawYx, "$ORA1" + values[x][yxxxxxxxxx], Text.FontType.BODY, 0, true);
               } else if (values[x][yxxxxxxxxx] > 0) {
                  this.font.drawString(drawXx, drawYx, "$RED1" + values[x][yxxxxxxxxx], Text.FontType.BODY, 0, true);
               }
            }
         }

         g.resetTransform();
         g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
      }

      if (this.displayMode == DataModule.DataDisplayMode.DESIRABILITY_MAP || this.lockedDisplayMode == DataModule.DataDisplayMode.DESIRABILITY_MAP) {
         int[][] rangeMap = this.object.getRangeMap(RangeBuilder.RangeMapLayer.RANGE_AND_DESIRABILITY);
         GL11.glBlendFunc(770, 772);
         g.glBeginQuads();

         for (int x = startX; x < endX; x++) {
            for (int yxxxxxxxxxx = startY; yxxxxxxxxxx < endY; yxxxxxxxxxx++) {
               if (rangeMap[x][yxxxxxxxxxx] > 0) {
                  int drawXx = x * 16;
                  int drawYx = yxxxxxxxxxx * 16;
                  int desirability = rangeMap[x][yxxxxxxxxxx];
                  Color color;
                  if (desirability >= 30) {
                     color = new Color(0, 255, 0, 100);
                  } else if (desirability >= 25) {
                     color = new Color(100, 255, 0, 100);
                  } else if (desirability >= 20) {
                     color = new Color(150, 255, 0, 100);
                  } else if (desirability >= 15) {
                     color = new Color(255, 255, 0, 100);
                  } else if (desirability >= 10) {
                     color = new Color(255, 150, 0, 100);
                  } else if (desirability >= 5) {
                     color = new Color(255, 100, 0, 100);
                  } else {
                     color = new Color(255, 0, 0, 100);
                  }

                  g.setColor(color);
                  g.fillRectEmbedded(drawXx + mapX, drawYx + mapY, 16.0F, 16.0F);
               }
            }
         }

         g.glEnd();
         GL11.glBlendFunc(770, 771);
         this.gridBox.startUse();

         for (int x = startX; x < endX; x++) {
            for (int yxxxxxxxxxxx = startY; yxxxxxxxxxxx < endY; yxxxxxxxxxxx++) {
               if (rangeMap[x][yxxxxxxxxxxx] > 0) {
                  int drawXx = x * 16;
                  int drawYx = yxxxxxxxxxxx * 16;
                  int desirability = rangeMap[x][yxxxxxxxxxxx];
                  Color color;
                  if (desirability >= 30) {
                     color = new Color(0, 255, 0, 100);
                  } else if (desirability >= 25) {
                     color = new Color(100, 255, 0, 100);
                  } else if (desirability >= 20) {
                     color = new Color(150, 255, 0, 100);
                  } else if (desirability >= 15) {
                     color = new Color(255, 255, 0, 100);
                  } else if (desirability >= 10) {
                     color = new Color(255, 150, 0, 100);
                  } else if (desirability >= 5) {
                     color = new Color(255, 100, 0, 100);
                  } else {
                     color = new Color(255, 0, 0, 100);
                  }

                  this.gridBox.drawEmbedded(drawXx + mapX, drawYx + mapY, drawXx + mapX + 16.0F, drawYx + mapY + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, color);
               }
            }
         }

         this.gridBox.endUse();
      }

      if (this.displayMode == DataModule.DataDisplayMode.CORRUPTION_RESISTANCE_MAP
         || this.lockedDisplayMode == DataModule.DataDisplayMode.CORRUPTION_RESISTANCE_MAP) {
         int[][] rangeMap = this.object.getRangeMap(RangeBuilder.RangeMapLayer.CORRUPTION_RESISTANCE);
         GL11.glBlendFunc(770, 772);
         g.glBeginQuads();

         for (int x = startX; x < endX; x++) {
            for (int yxxxxxxxxxxxx = startY; yxxxxxxxxxxxx < endY; yxxxxxxxxxxxx++) {
               if (rangeMap[x][yxxxxxxxxxxxx] > 0) {
                  int drawXx = x * 16;
                  int drawYx = yxxxxxxxxxxxx * 16;
                  Color color = new Color(0, 255, 0, 50);
                  g.setColor(color);
                  g.fillRectEmbedded(drawXx + mapX, drawYx + mapY, 16.0F, 16.0F);
               }
            }
         }

         g.glEnd();
         GL11.glBlendFunc(770, 771);
         this.gridBox.startUse();

         for (int x = startX; x < endX; x++) {
            for (int yxxxxxxxxxxxxx = startY; yxxxxxxxxxxxxx < endY; yxxxxxxxxxxxxx++) {
               if (rangeMap[x][yxxxxxxxxxxxxx] > 0) {
                  int drawXx = x * 16;
                  int drawYx = yxxxxxxxxxxxxx * 16;
                  Color color = new Color(255, 255, 255, 75);
                  this.gridBox.drawEmbedded(drawXx + mapX, drawYx + mapY, drawXx + mapX + 16.0F, drawYx + mapY + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, color);
               }
            }
         }

         this.gridBox.endUse();
         if (debug) {
            g.resetTransform();
            g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());

            for (int x = startX; x < endX; x++) {
               for (int yxxxxxxxxxxxxxx = startY; yxxxxxxxxxxxxxx < endY; yxxxxxxxxxxxxxx++) {
                  int drawXx = (int)ScaleControl.getTransformedPositionX(x * 16 + 8);
                  int drawYx = (int)ScaleControl.getTransformedPositionY(yxxxxxxxxxxxxxx * 16 + 8);
                  if (rangeMap[x][yxxxxxxxxxxxxxx] > 6) {
                     this.font.drawString(drawXx, drawYx, "$GRE1" + rangeMap[x][yxxxxxxxxxxxxxx], Text.FontType.BODY, 0, true);
                  } else if (rangeMap[x][yxxxxxxxxxxxxxx] > 4) {
                     this.font.drawString(drawXx, drawYx, "$YEL1" + rangeMap[x][yxxxxxxxxxxxxxx], Text.FontType.BODY, 0, true);
                  } else if (rangeMap[x][yxxxxxxxxxxxxxx] > 2) {
                     this.font.drawString(drawXx, drawYx, "$ORA1" + rangeMap[x][yxxxxxxxxxxxxxx], Text.FontType.BODY, 0, true);
                  } else if (rangeMap[x][yxxxxxxxxxxxxxx] > 0) {
                     this.font.drawString(drawXx, drawYx, "$RED1" + rangeMap[x][yxxxxxxxxxxxxxx], Text.FontType.BODY, 0, true);
                  }
               }
            }

            g.resetTransform();
            g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
         }
      }

      if (this.displayMode == DataModule.DataDisplayMode.TOWER_RANGE_MAP || this.lockedDisplayMode == DataModule.DataDisplayMode.TOWER_RANGE_MAP) {
         byte[][] towerRangeMap = new byte[256][256];

         for (ObjectBase o : this.object.getObjectsBySortGroup(ObjectModule.ObjectSortGroup.TOWERS)) {
            TowerBase t = o.getTower();
            byte[][] towerRange = Utilities.createRangeMap(t.getTowerFlags().getDistance());

            for (int x = 0; x < towerRange.length; x++) {
               for (int yxxxxxxxxxxxxxxx = 0; yxxxxxxxxxxxxxxx < towerRange[x].length; yxxxxxxxxxxxxxxx++) {
                  int tX = t.getX() - t.getTowerFlags().getDistance() + x;
                  int tY = t.getY() - t.getTowerFlags().getDistance() + yxxxxxxxxxxxxxxx;
                  if (!Utilities.isOutOfBoundsOfUsableMap(tX, tY)) {
                     towerRangeMap[tX][tY] = (byte)(towerRangeMap[tX][tY] + towerRange[x][yxxxxxxxxxxxxxxx]);
                  }
               }
            }
         }

         GL11.glBlendFunc(770, 772);
         g.glBeginQuads();

         for (int x = startX; x < endX; x++) {
            for (int yxxxxxxxxxxxxxxxx = startY; yxxxxxxxxxxxxxxxx < endY; yxxxxxxxxxxxxxxxx++) {
               if (towerRangeMap[x][yxxxxxxxxxxxxxxxx] > 0) {
                  int drawXx = x * 16;
                  int drawYx = yxxxxxxxxxxxxxxxx * 16;
                  int value = towerRangeMap[x][yxxxxxxxxxxxxxxxx];
                  Color color;
                  if (value >= 6) {
                     color = new Color(0, 255, 0, 25);
                  } else if (value >= 5) {
                     color = new Color(100, 255, 0, 25);
                  } else if (value >= 4) {
                     color = new Color(150, 255, 0, 25);
                  } else if (value >= 3) {
                     color = new Color(255, 255, 0, 25);
                  } else if (value >= 2) {
                     color = new Color(255, 150, 0, 25);
                  } else if (value >= 1) {
                     color = new Color(255, 100, 0, 25);
                  } else {
                     color = new Color(255, 0, 0, 25);
                  }

                  g.setColor(color);
                  g.fillRectEmbedded(drawXx + mapX, drawYx + mapY, 16.0F, 16.0F);
               }
            }
         }

         g.glEnd();
         GL11.glBlendFunc(770, 771);
         this.gridBox.startUse();

         for (int x = startX; x < endX; x++) {
            for (int yxxxxxxxxxxxxxxxxx = startY; yxxxxxxxxxxxxxxxxx < endY; yxxxxxxxxxxxxxxxxx++) {
               if (towerRangeMap[x][yxxxxxxxxxxxxxxxxx] > 0) {
                  int drawXx = x * 16;
                  int drawYx = yxxxxxxxxxxxxxxxxx * 16;
                  int desirability = towerRangeMap[x][yxxxxxxxxxxxxxxxxx];
                  Color color;
                  if (desirability >= 6) {
                     color = new Color(0, 255, 0, 150);
                  } else if (desirability >= 5) {
                     color = new Color(100, 255, 0, 150);
                  } else if (desirability >= 4) {
                     color = new Color(150, 255, 0, 150);
                  } else if (desirability >= 3) {
                     color = new Color(255, 255, 0, 150);
                  } else if (desirability >= 2) {
                     color = new Color(255, 150, 0, 150);
                  } else if (desirability >= 1) {
                     color = new Color(255, 100, 0, 150);
                  } else {
                     color = new Color(255, 0, 0, 150);
                  }

                  this.gridBox.drawEmbedded(drawXx + mapX, drawYx + mapY, drawXx + mapX + 16.0F, drawYx + mapY + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, color);
               }
            }
         }

         this.gridBox.endUse();
      }

      if (this.displayMode == DataModule.DataDisplayMode.LIGHTNING_ROD_RANGE_MAP
         || this.lockedDisplayMode == DataModule.DataDisplayMode.LIGHTNING_ROD_RANGE_MAP) {
         byte[][] lightningRodRangeMap = new byte[256][256];

         for (ObjectBase o : this.object.getObjectsBySortGroup(ObjectModule.ObjectSortGroup.LIGHTNING_RODS)) {
            byte[][] lightningRodRange = Utilities.createRangeMap(o.getObjectFlags().getLightningRodRange());

            for (int x = 0; x < lightningRodRange.length; x++) {
               for (int yxxxxxxxxxxxxxxxxxx = 0; yxxxxxxxxxxxxxxxxxx < lightningRodRange[x].length; yxxxxxxxxxxxxxxxxxx++) {
                  int tX = o.getLightningRodXOnMap() - o.getObjectFlags().getLightningRodRange() + x;
                  int tY = o.getLightningRodYOnMap() - o.getObjectFlags().getLightningRodRange() + yxxxxxxxxxxxxxxxxxx;
                  if (!Utilities.isOutOfBoundsOfUsableMap(tX, tY)) {
                     lightningRodRangeMap[tX][tY] = (byte)(lightningRodRangeMap[tX][tY] + lightningRodRange[x][yxxxxxxxxxxxxxxxxxx]);
                  }
               }
            }
         }

         GL11.glBlendFunc(770, 772);
         g.glBeginQuads();

         for (int x = startX; x < endX; x++) {
            for (int yxxxxxxxxxxxxxxxxxxx = startY; yxxxxxxxxxxxxxxxxxxx < endY; yxxxxxxxxxxxxxxxxxxx++) {
               if (lightningRodRangeMap[x][yxxxxxxxxxxxxxxxxxxx] > 0) {
                  int drawXx = x * 16;
                  int drawYx = yxxxxxxxxxxxxxxxxxxx * 16;
                  int value = lightningRodRangeMap[x][yxxxxxxxxxxxxxxxxxxx];
                  Color color;
                  if (value >= 6) {
                     color = new Color(0, 255, 0, 25);
                  } else if (value >= 5) {
                     color = new Color(100, 255, 0, 25);
                  } else if (value >= 4) {
                     color = new Color(150, 255, 0, 25);
                  } else if (value >= 3) {
                     color = new Color(255, 255, 0, 25);
                  } else if (value >= 2) {
                     color = new Color(255, 150, 0, 25);
                  } else if (value >= 1) {
                     color = new Color(255, 100, 0, 25);
                  } else {
                     color = new Color(255, 0, 0, 25);
                  }

                  g.setColor(color);
                  g.fillRectEmbedded(drawXx + mapX, drawYx + mapY, 16.0F, 16.0F);
               }
            }
         }

         g.glEnd();
         GL11.glBlendFunc(770, 771);
         this.gridBox.startUse();

         for (int x = startX; x < endX; x++) {
            for (int yxxxxxxxxxxxxxxxxxxxx = startY; yxxxxxxxxxxxxxxxxxxxx < endY; yxxxxxxxxxxxxxxxxxxxx++) {
               if (lightningRodRangeMap[x][yxxxxxxxxxxxxxxxxxxxx] > 0) {
                  int drawXx = x * 16;
                  int drawYx = yxxxxxxxxxxxxxxxxxxxx * 16;
                  int desirability = lightningRodRangeMap[x][yxxxxxxxxxxxxxxxxxxxx];
                  Color color;
                  if (desirability >= 6) {
                     color = new Color(0, 255, 0, 150);
                  } else if (desirability >= 5) {
                     color = new Color(100, 255, 0, 150);
                  } else if (desirability >= 4) {
                     color = new Color(150, 255, 0, 150);
                  } else if (desirability >= 3) {
                     color = new Color(255, 255, 0, 150);
                  } else if (desirability >= 2) {
                     color = new Color(255, 150, 0, 150);
                  } else if (desirability >= 1) {
                     color = new Color(255, 100, 0, 150);
                  } else {
                     color = new Color(255, 0, 0, 150);
                  }

                  this.gridBox.drawEmbedded(drawXx + mapX, drawYx + mapY, drawXx + mapX + 16.0F, drawYx + mapY + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, color);
               }
            }
         }

         this.gridBox.endUse();
      }

      if (this.displayMode == DataModule.DataDisplayMode.COLLISION_MAP || this.lockedDisplayMode == DataModule.DataDisplayMode.COLLISION_MAP) {
         g.glBeginQuads();
         byte[][] collisionMap = this.collision.getCollisionMap();

         for (int x = startPixelX; x < endPixelX; x++) {
            for (int yxxxxxxxxxxxxxxxxxxxxx = startPixelY; yxxxxxxxxxxxxxxxxxxxxx < endPixelY; yxxxxxxxxxxxxxxxxxxxxx++) {
               if (collisionMap[x][yxxxxxxxxxxxxxxxxxxxxx] != this.collision.getCollisionNone()) {
                  Color color;
                  if (collisionMap[x][yxxxxxxxxxxxxxxxxxxxxx] == this.collision.getCollisionNorthHigh()) {
                     color = new Color(255, 0, 0, 255);
                  } else if (collisionMap[x][yxxxxxxxxxxxxxxxxxxxxx] == this.collision.getCollisionNorthMedium()) {
                     color = new Color(150, 0, 0, 255);
                  } else if (collisionMap[x][yxxxxxxxxxxxxxxxxxxxxx] == this.collision.getCollisionNorthLow()) {
                     color = new Color(100, 0, 0, 255);
                  } else if (collisionMap[x][yxxxxxxxxxxxxxxxxxxxxx] == this.collision.getCollisionEastHigh()) {
                     color = new Color(255, 255, 0, 255);
                  } else if (collisionMap[x][yxxxxxxxxxxxxxxxxxxxxx] == this.collision.getCollisionEastMedium()) {
                     color = new Color(150, 150, 0, 255);
                  } else if (collisionMap[x][yxxxxxxxxxxxxxxxxxxxxx] == this.collision.getCollisionEastLow()) {
                     color = new Color(100, 100, 0, 255);
                  } else if (collisionMap[x][yxxxxxxxxxxxxxxxxxxxxx] == this.collision.getCollisionSouthHigh()) {
                     color = new Color(0, 255, 0, 255);
                  } else if (collisionMap[x][yxxxxxxxxxxxxxxxxxxxxx] == this.collision.getCollisionSouthMedium()) {
                     color = new Color(0, 150, 0, 255);
                  } else if (collisionMap[x][yxxxxxxxxxxxxxxxxxxxxx] == this.collision.getCollisionSouthLow()) {
                     color = new Color(0, 100, 0, 255);
                  } else if (collisionMap[x][yxxxxxxxxxxxxxxxxxxxxx] == this.collision.getCollisionWestHigh()) {
                     color = new Color(0, 0, 255, 255);
                  } else if (collisionMap[x][yxxxxxxxxxxxxxxxxxxxxx] == this.collision.getCollisionWestMedium()) {
                     color = new Color(0, 0, 150, 255);
                  } else if (collisionMap[x][yxxxxxxxxxxxxxxxxxxxxx] == this.collision.getCollisionWestLow()) {
                     color = new Color(0, 0, 100, 255);
                  } else {
                     color = new Color(255, 255, 255, 255);
                  }

                  g.setColor(color);
                  g.fillRectEmbedded(x + mapX, yxxxxxxxxxxxxxxxxxxxxx + mapY, 1.0F, 1.0F);
               }
            }
         }

         g.glEnd();
      }

      if (this.displayMode == DataModule.DataDisplayMode.SHADOW_MAP || this.lockedDisplayMode == DataModule.DataDisplayMode.SHADOW_MAP) {
         g.glBeginQuads();
         Color[][] shadowMapBottom = this.shadow.getShadowMap();

         for (int x = startPixelX; x < endPixelX; x++) {
            for (int yxxxxxxxxxxxxxxxxxxxxxx = startPixelY; yxxxxxxxxxxxxxxxxxxxxxx < endPixelY; yxxxxxxxxxxxxxxxxxxxxxx++) {
               if (shadowMapBottom[x][yxxxxxxxxxxxxxxxxxxxxxx] != null
                  && (
                     shadowMapBottom[x][yxxxxxxxxxxxxxxxxxxxxxx].getRed() > 0
                        || shadowMapBottom[x][yxxxxxxxxxxxxxxxxxxxxxx].getGreen() > 0
                        || shadowMapBottom[x][yxxxxxxxxxxxxxxxxxxxxxx].getBlue() > 0
                  )) {
                  g.setColor(shadowMapBottom[x][yxxxxxxxxxxxxxxxxxxxxxx]);
                  g.fillRectEmbedded(x + mapX, yxxxxxxxxxxxxxxxxxxxxxx + mapY, 1.0F, 1.0F);
               }
            }
         }

         g.glEnd();
      }

      if (this.displayMode == DataModule.DataDisplayMode.DEPTH_MAP || this.lockedDisplayMode == DataModule.DataDisplayMode.DEPTH_MAP) {
         g.glBeginQuads();
         byte[][] depthMap = this.depth.getDepthMap();

         for (int x = startPixelX; x < endPixelX; x++) {
            for (int yxxxxxxxxxxxxxxxxxxxxxxx = startPixelY; yxxxxxxxxxxxxxxxxxxxxxxx < endPixelY; yxxxxxxxxxxxxxxxxxxxxxxx++) {
               if (depthMap[x][yxxxxxxxxxxxxxxxxxxxxxxx] > 0) {
                  g.setColor(
                     new Color(
                        depthMap[x][yxxxxxxxxxxxxxxxxxxxxxxx] * 10, depthMap[x][yxxxxxxxxxxxxxxxxxxxxxxx] * 10, depthMap[x][yxxxxxxxxxxxxxxxxxxxxxxx] * 10, 255
                     )
                  );
                  g.fillRectEmbedded(x + mapX, yxxxxxxxxxxxxxxxxxxxxxxx + mapY, 1.0F, 1.0F);
               }
            }
         }

         g.glEnd();
      }

      if (this.displayMode == DataModule.DataDisplayMode.LIGHT_VALUE || this.lockedDisplayMode == DataModule.DataDisplayMode.LIGHT_VALUE) {
         g.resetTransform();
         g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
         int[][] values = this.lighting.getLightValueMap();

         for (int x = startX; x < endX; x++) {
            for (int yxxxxxxxxxxxxxxxxxxxxxxxx = startY; yxxxxxxxxxxxxxxxxxxxxxxxx < endY; yxxxxxxxxxxxxxxxxxxxxxxxx++) {
               int drawXx = (int)ScaleControl.getTransformedPositionX(x * 16 + 8);
               int drawYx = (int)ScaleControl.getTransformedPositionY(yxxxxxxxxxxxxxxxxxxxxxxxx * 16 + 8);
               if (values[x][yxxxxxxxxxxxxxxxxxxxxxxxx] > 30) {
                  this.font.drawString(drawXx, drawYx, "$GRE1" + values[x][yxxxxxxxxxxxxxxxxxxxxxxxx], Text.FontType.BODY, 0, true);
               } else if (values[x][yxxxxxxxxxxxxxxxxxxxxxxxx] > 20) {
                  this.font.drawString(drawXx, drawYx, "$YEL1" + values[x][yxxxxxxxxxxxxxxxxxxxxxxxx], Text.FontType.BODY, 0, true);
               } else if (values[x][yxxxxxxxxxxxxxxxxxxxxxxxx] > 10) {
                  this.font.drawString(drawXx, drawYx, "$ORA1" + values[x][yxxxxxxxxxxxxxxxxxxxxxxxx], Text.FontType.BODY, 0, true);
               } else if (values[x][yxxxxxxxxxxxxxxxxxxxxxxxx] > 0) {
                  this.font.drawString(drawXx, drawYx, "$RED1" + values[x][yxxxxxxxxxxxxxxxxxxxxxxxx], Text.FontType.BODY, 0, true);
               }
            }
         }

         g.resetTransform();
         g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
      }

      if (this.displayMode == DataModule.DataDisplayMode.PATHS || this.lockedDisplayMode == DataModule.DataDisplayMode.PATHS) {
         for (MobBase m : this.mob.getMobArray()) {
            this.mob.renderMobPath(m);
         }

         this.waypoint.render(g);
         if (debug) {
            byte[][] values = this.mob.getPathMapper().getPathMap();
            g.resetTransform();
            g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());

            for (int x = startX; x < endX; x++) {
               for (int yxxxxxxxxxxxxxxxxxxxxxxxxx = startY; yxxxxxxxxxxxxxxxxxxxxxxxxx < endY; yxxxxxxxxxxxxxxxxxxxxxxxxx++) {
                  if (values[x][yxxxxxxxxxxxxxxxxxxxxxxxxx] > 0) {
                     int drawXx = (int)ScaleControl.getTransformedPositionX(x * 16 + 8);
                     int drawYx = (int)ScaleControl.getTransformedPositionY(yxxxxxxxxxxxxxxxxxxxxxxxxx * 16 + 8);
                     if (values[x][yxxxxxxxxxxxxxxxxxxxxxxxxx] > 30) {
                        this.font.drawString(drawXx, drawYx, "$RED1" + values[x][yxxxxxxxxxxxxxxxxxxxxxxxxx], Text.FontType.BODY, 0, true);
                     } else if (values[x][yxxxxxxxxxxxxxxxxxxxxxxxxx] > 20) {
                        this.font.drawString(drawXx, drawYx, "$ORA1" + values[x][yxxxxxxxxxxxxxxxxxxxxxxxxx], Text.FontType.BODY, 0, true);
                     } else if (values[x][yxxxxxxxxxxxxxxxxxxxxxxxxx] > 10) {
                        this.font.drawString(drawXx, drawYx, "$YEL1" + values[x][yxxxxxxxxxxxxxxxxxxxxxxxxx], Text.FontType.BODY, 0, true);
                     } else if (values[x][yxxxxxxxxxxxxxxxxxxxxxxxxx] > 0) {
                        this.font.drawString(drawXx, drawYx, "$GRE1" + values[x][yxxxxxxxxxxxxxxxxxxxxxxxxx], Text.FontType.BODY, 0, true);
                     }
                  }
               }
            }

            g.resetTransform();
            g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
         }
      }

      if (this.displayMode == DataModule.DataDisplayMode.TRAFFIC || this.lockedDisplayMode == DataModule.DataDisplayMode.TRAFFIC) {
         int[][] values = this.road.getTrafficAmount();
         g.resetTransform();
         g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());

         for (int x = startX; x < endX; x++) {
            for (int yxxxxxxxxxxxxxxxxxxxxxxxxxx = startY; yxxxxxxxxxxxxxxxxxxxxxxxxxx < endY; yxxxxxxxxxxxxxxxxxxxxxxxxxx++) {
               int drawXx = (int)ScaleControl.getTransformedPositionX(x * 16 + 8);
               int drawYx = (int)ScaleControl.getTransformedPositionY(yxxxxxxxxxxxxxxxxxxxxxxxxxx * 16 + 8);
               if (values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxx] > 9000) {
                  this.font.drawString(drawXx, drawYx, "$GRE1" + values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxx] / 1000 + "k", Text.FontType.BODY, 0, true);
               } else if (values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxx] > 6000) {
                  this.font.drawString(drawXx, drawYx, "$YEL1" + values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxx] / 1000 + "k", Text.FontType.BODY, 0, true);
               } else if (values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxx] > 3000) {
                  this.font.drawString(drawXx, drawYx, "$ORA1" + values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxx] / 1000 + "k", Text.FontType.BODY, 0, true);
               } else if (values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxx] > 0) {
                  this.font.drawString(drawXx, drawYx, "$RED1" + values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxx] / 1000 + "k", Text.FontType.BODY, 0, true);
               }
            }
         }

         g.resetTransform();
         g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
      }

      if (this.displayMode == DataModule.DataDisplayMode.ROAD_DURABILITY || this.lockedDisplayMode == DataModule.DataDisplayMode.ROAD_DURABILITY) {
         g.resetTransform();
         g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
         int[][] values = this.road.getRoadDurability();

         for (int x = startX; x < endX; x++) {
            for (int yxxxxxxxxxxxxxxxxxxxxxxxxxxx = startY; yxxxxxxxxxxxxxxxxxxxxxxxxxxx < endY; yxxxxxxxxxxxxxxxxxxxxxxxxxxx++) {
               int drawXx = (int)ScaleControl.getTransformedPositionX(x * 16 + 8);
               int drawYx = (int)ScaleControl.getTransformedPositionY(yxxxxxxxxxxxxxxxxxxxxxxxxxxx * 16 + 8);
               if (values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxxx] > 900) {
                  this.font.drawString(drawXx, drawYx, "$GRE1" + values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxxx], Text.FontType.BODY, 0, true);
               } else if (values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxxx] > 500) {
                  this.font.drawString(drawXx, drawYx, "$YEL1" + values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxxx], Text.FontType.BODY, 0, true);
               } else if (values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxxx] > 300) {
                  this.font.drawString(drawXx, drawYx, "$ORA1" + values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxxx], Text.FontType.BODY, 0, true);
               } else if (values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxxx] > 0) {
                  this.font.drawString(drawXx, drawYx, "$RED1" + values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxxx], Text.FontType.BODY, 0, true);
               }
            }
         }

         g.resetTransform();
         g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
      }

      if (this.displayMode == DataModule.DataDisplayMode.VILLAGE_THREAT_MAP || this.lockedDisplayMode == DataModule.DataDisplayMode.VILLAGE_THREAT_MAP) {
         int[][] values = this.object.getRangeMap(RangeBuilder.RangeMapLayer.VILLAGE_THREAT_MAP);
         g.resetTransform();
         g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());

         for (int x = startX; x < endX; x++) {
            for (int yxxxxxxxxxxxxxxxxxxxxxxxxxxxx = startY; yxxxxxxxxxxxxxxxxxxxxxxxxxxxx < endY; yxxxxxxxxxxxxxxxxxxxxxxxxxxxx++) {
               int drawXx = (int)ScaleControl.getTransformedPositionX(x * 16 + 8);
               int drawYx = (int)ScaleControl.getTransformedPositionY(yxxxxxxxxxxxxxxxxxxxxxxxxxxxx * 16 + 8);
               if (values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxxxx] > 200) {
                  this.font.drawString(drawXx, drawYx, "$RED1" + values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxxxx], Text.FontType.BODY, 0, true);
               } else if (values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxxxx] > 100) {
                  this.font.drawString(drawXx, drawYx, "$ORA1" + values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxxxx], Text.FontType.BODY, 0, true);
               } else if (values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxxxx] > 50) {
                  this.font.drawString(drawXx, drawYx, "$YEL1" + values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxxxx], Text.FontType.BODY, 0, true);
               } else if (values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxxxx] > 0) {
                  this.font.drawString(drawXx, drawYx, "$GRE1" + values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxxxx], Text.FontType.BODY, 0, true);
               }
            }
         }

         g.resetTransform();
         g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
      }

      if (this.displayMode == DataModule.DataDisplayMode.THREATENED_TILE_MAP || this.lockedDisplayMode == DataModule.DataDisplayMode.THREATENED_TILE_MAP) {
         byte[][] values = this.map.getMapThreatMap().getThreatMap();
         g.resetTransform();
         g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());

         for (int x = startX; x < endX; x++) {
            for (int yxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = startY; yxxxxxxxxxxxxxxxxxxxxxxxxxxxxx < endY; yxxxxxxxxxxxxxxxxxxxxxxxxxxxxx++) {
               int drawXx = (int)ScaleControl.getTransformedPositionX(x * 16 + 8);
               int drawYx = (int)ScaleControl.getTransformedPositionY(yxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * 16 + 8);
               if (values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxxxxx] > 200) {
                  this.font.drawString(drawXx, drawYx, "$RED1" + values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxxxxx], Text.FontType.BODY, 0, true);
               } else if (values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxxxxx] > 100) {
                  this.font.drawString(drawXx, drawYx, "$ORA1" + values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxxxxx], Text.FontType.BODY, 0, true);
               } else if (values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxxxxx] > 50) {
                  this.font.drawString(drawXx, drawYx, "$YEL1" + values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxxxxx], Text.FontType.BODY, 0, true);
               } else if (values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxxxxx] > 0) {
                  this.font.drawString(drawXx, drawYx, "$GRE1" + values[x][yxxxxxxxxxxxxxxxxxxxxxxxxxxxxx], Text.FontType.BODY, 0, true);
               }
            }
         }

         g.resetTransform();
         g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
      }

      if (this.displayMode == DataModule.DataDisplayMode.CORRUPTED_TILES || this.lockedDisplayMode == DataModule.DataDisplayMode.CORRUPTED_TILES) {
         GL11.glBlendFunc(770, 772);
         g.glBeginQuads();

         for (int x = startX; x < endX; x++) {
            for (int yxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = startY; yxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx < endY; yxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx++) {
               if (this.corruption.isTileCorrupted(x, yxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)) {
                  int drawXx = x * 16;
                  int drawYx = yxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * 16;
                  g.setColor(new Color(255, 0, 0, 15));
                  g.fillRectEmbedded(drawXx + mapX, drawYx + mapY, 16.0F, 16.0F);
               }
            }
         }

         g.glEnd();
         GL11.glBlendFunc(770, 771);
         this.gridBox.startUse();

         for (int x = startX; x < endX; x++) {
            for (int yxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = startY; yxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx < endY; yxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx++) {
               if (this.corruption.isTileCorrupted(x, yxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)) {
                  int drawXx = x * 16;
                  int drawYx = yxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * 16;
                  this.gridBox
                     .drawEmbedded(
                        drawXx + mapX, drawYx + mapY, drawXx + mapX + 16.0F, drawYx + mapY + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, new Color(255, 0, 0, 75)
                     );
               }
            }
         }

         this.gridBox.endUse();
      }
   }

   public void renderObjectOutline(int tileX, int tileY, Graphics g, MapTilesLoader.TileSet objectType, ObjectBase.ObjectSubType subType, boolean ignoreRange) throws SlickException {
      int mapWidth = 256;
      int mapHeight = 256;
      float mapX = this.map.getMapX();
      float mapY = this.map.getMapY();
      ObjectFlags objectFlags = this.object.getObjectFlagFactory().getObjectFlags(objectType, ObjectBase.ObjectSubType.BUILT);
      int tileGID = this.map.getMapTileLoader().getTileSetGID(objectType);
      if (subType == ObjectBase.ObjectSubType.CONSTRUCTION) {
         tileGID = this.map.getMapTileLoader().getTileSetGID(objectType, MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_OUTLINE);
      } else if (subType == ObjectBase.ObjectSubType.ABANDONED) {
         tileGID = this.map.getMapTileLoader().getTileSetGID(objectType, MapTilesLoader.TileSetObjectPhase.ABANDONED);
      }

      int objectWidth = objectFlags.getWidth();
      int objectHeight = objectFlags.getHeight();
      int offsetX = objectWidth / 2;
      int offsetY = objectHeight / 2;
      float drawX = mapX + tileX * 16 - offsetX * 16;
      float drawY = mapY + tileY * 16 - offsetY * 16;
      this.map.getMapTileLoader().getObjectImage(tileGID).draw(drawX, drawY);
      if (objectFlags.getTowerType() != null) {
         TowerHeadFlags towerFlags = this.object.getTowerHeadFlagFactory().getTowerFlags(objectFlags.getTowerType(), objectFlags.getTowerUpgradeStage());
         this.renderGenericMap(
            tileX + objectFlags.getTowerXOnBuilding() - offsetX,
            tileY + objectFlags.getTowerYOnBuilding() - offsetY,
            Utilities.createRangeMap(towerFlags.getDistance()),
            new Color(200, 0, 0, 25),
            g
         );
      }

      if (objectFlags.getLightningRodRange() > 0) {
         this.renderGenericMap(
            tileX + objectFlags.getLightningRodXOnBuilding(),
            tileY + objectFlags.getLightningRodYOnBuilding(),
            Utilities.createRangeMap(objectFlags.getLightningRodRange()),
            new Color(200, 200, 0, 50),
            g
         );
      }

      GL11.glBlendFunc(770, 772);

      for (int w = 0; w < objectWidth; w++) {
         for (int h = 0; h < objectHeight; h++) {
            if (tileX - offsetX + w >= 1 && tileX - offsetX + w <= mapWidth - 1 && tileY - offsetY + h >= 1 && tileY - offsetY + h <= mapHeight - 1) {
               if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[tileX - offsetX + w][tileY - offsetY + h] != 1
                  && (ignoreRange || this.object.isInRange(tileX - offsetX + w, tileY - offsetY + h) || objectType == MapTilesLoader.TileSet.CASTLE_1)) {
                  if (this.object.isObject(tileX - offsetX + w, tileY - offsetY + h)
                     || this.map.getMap()[tileX - offsetX + w][tileY - offsetY + h][6] > 0
                     || this.map.getMap()[tileX - offsetX + w][tileY - offsetY + h][7] > 0) {
                     g.setColor(new Color(255, 255, 0, 100));
                     g.fillRect(drawX + w * 16, drawY + h * 16, 16.0F, 16.0F);
                  } else if (this.map.getMap()[tileX - offsetX + w][tileY - offsetY + h][5] > 0) {
                     g.setColor(new Color(100, 255, 100, 70));
                     g.fillRect(drawX + w * 16, drawY + h * 16, 16.0F, 16.0F);
                  } else {
                     g.setColor(new Color(0, 255, 0, 30));
                     g.fillRect(drawX + w * 16, drawY + h * 16, 16.0F, 16.0F);
                  }
               } else {
                  g.setColor(new Color(255, 0, 0, 100));
                  g.fillRect(drawX + w * 16, drawY + h * 16, 16.0F, 16.0F);
               }
            }
         }
      }

      GL11.glBlendFunc(770, 771);
      if (this.gameMode != GameModeTemplateBase.GameMode.MAP_EDITOR) {
         float transformX = ScaleControl.getTransformedPosition(drawX + offsetX * 16);
         float transformY = ScaleControl.getTransformedPosition(drawY - 16.0F);
         g.resetTransform();
         g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
         if (this.gameMode == GameModeTemplateBase.GameMode.SANDBOX) {
            Text.setVariableText("shortcutKey", SettingsParser.getKeyAccentModeToggleName());
            this.font.drawString(transformX, transformY, Text.getText("playOverlay.general.autobuild"), Text.FontType.BODY, 2, true);
         }

         g.resetTransform();
         g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
      }
   }

   public void renderWallOutline(int tileX, int tileY, Graphics g, MapTilesLoader.TileSet objectType, boolean ignoreRange) throws SlickException {
      int mapWidth = 256;
      int mapHeight = 256;
      float mapX = this.map.getMapX();
      float mapY = this.map.getMapY();
      int tileGID = this.map.getMapTileLoader().getTileSetGID(objectType);
      this.map.getMapTileLoader().getWallImage(tileGID).draw(mapX + tileX * 16, mapY + tileY * 16);
      GL11.glBlendFunc(770, 772);
      if (tileX >= 1 && tileX <= mapWidth - 1 && tileY >= 1 && tileY <= mapHeight - 1) {
         if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[tileX][tileY] != 1
            && (ignoreRange || this.object.isInRange(tileX, tileY) || objectType == MapTilesLoader.TileSet.CASTLE_1)) {
            if (this.object.isObject(tileX, tileY) || this.map.getMap()[tileX][tileY][6] > 0 || this.map.getMap()[tileX][tileY][7] > 0) {
               g.setColor(new Color(255, 255, 0, 100));
               g.fillRect(mapX + tileX * 16, mapY + tileY * 16, 16.0F, 16.0F);
            } else if (this.map.getMap()[tileX][tileY][5] > 0) {
               g.setColor(new Color(100, 255, 100, 70));
               g.fillRect(mapX + tileX * 16, mapY + tileY * 16, 16.0F, 16.0F);
            } else {
               g.setColor(new Color(0, 255, 0, 30));
               g.fillRect(mapX + tileX * 16, mapY + tileY * 16, 16.0F, 16.0F);
            }
         } else {
            g.setColor(new Color(255, 0, 0, 100));
            g.fillRect(mapX + tileX * 16, mapY + tileY * 16, 16.0F, 16.0F);
         }
      }

      GL11.glBlendFunc(770, 771);
      float drawX = mapX + tileX * 16;
      float drawY = mapY + tileY * 16;
      if (this.gameMode != GameModeTemplateBase.GameMode.MAP_EDITOR) {
         float transformX = ScaleControl.getTransformedPosition(drawX);
         float transformY = ScaleControl.getTransformedPosition(drawY);
         g.resetTransform();
         g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
         if (this.currentState.getGameMode() == GameModeTemplateBase.GameMode.SANDBOX) {
            Text.setVariableText("shortcutKey", SettingsParser.getKeyAccentModeToggleName());
            this.font.drawString(transformX, transformY - 48.0F, Text.getText("playOverlay.general.autobuild"), Text.FontType.BODY, 2, true);
         }

         g.resetTransform();
         g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
      }
   }

   public void renderGenericMap(int tileX, int tileY, byte[][] array, Color c, Graphics g) {
      float mapX = this.map.getMapX();
      float mapY = this.map.getMapY();
      int towerDistance = array.length / 2;
      GL11.glBlendFunc(770, 772);
      g.glBeginQuads();

      for (int x = 0; x < array.length; x++) {
         for (int y = 0; y < array[x].length; y++) {
            if (array[x][y] > 0) {
               int drawX = (int)((tileX - towerDistance + x) * 16 + mapX);
               int drawY = (int)((tileY - towerDistance + y) * 16 + mapY);
               g.setColor(c);
               g.fillRectEmbedded(drawX, drawY, 16.0F, 16.0F);
            }
         }
      }

      g.glEnd();
      GL11.glBlendFunc(770, 771);
      this.gridBox.startUse();

      for (int x = 0; x < array.length; x++) {
         for (int yx = 0; yx < array[x].length; yx++) {
            if (array[x][yx] > 0) {
               int drawX = (int)((tileX - towerDistance + x) * 16 + mapX);
               int drawY = (int)((tileY - towerDistance + yx) * 16 + mapY);
               this.gridBox
                  .drawEmbedded(
                     drawX,
                     drawY,
                     drawX + this.gridBox.getWidth(),
                     drawY + this.gridBox.getHeight(),
                     0.0F,
                     0.0F,
                     this.gridBox.getWidth(),
                     this.gridBox.getHeight(),
                     c
                  );
            }
         }
      }

      this.gridBox.endUse();
   }

   public DataModule.DataDisplayMode getDisplayMode() {
      return this.displayMode;
   }

   public DataModule.DataDisplayMode getLockedDisplayMode() {
      return this.lockedDisplayMode;
   }

   public void setDisplayMode(DataModule.DataDisplayMode m) {
      this.previousDisplayMode = this.displayMode;
      this.displayMode = m;
   }

   public void lockDisplayMode(DataModule.DataDisplayMode m) {
      this.previousLockedDisplayMode = this.lockedDisplayMode;
      this.lockedDisplayMode = m;
   }

   public void unlockDisplayMode() {
      this.lockedDisplayMode = DataModule.DataDisplayMode.NONE;
   }

   public static enum DataDisplayMode {
      NONE(false),
      RESOURCE_VALUE(false),
      MOVEMENT_COST(false),
      BLOCK_MAP(false),
      GRID(false),
      HIDE_TOPOGRAPHY(true),
      RANGE_MAP(false),
      ESSENCE_BUILDUP_MAP(false),
      DESIRABILITY_MAP(false),
      CORRUPTION_RESISTANCE_MAP(false),
      TOWER_RANGE_MAP(false),
      LIGHTNING_ROD_RANGE_MAP(false),
      COLLISION_MAP(false),
      SHADOW_MAP(false),
      DEPTH_MAP(false),
      LIGHT_VALUE(false),
      PATHS(false),
      TRAFFIC(false),
      ROAD_DURABILITY(false),
      VILLAGE_THREAT_MAP(false),
      RESOURCE_HIGHLIGHT_WOOD(false),
      RESOURCE_HIGHLIGHT_ROCK(false),
      RESOURCE_HIGHLIGHT_FOOD_AND_WATER(false),
      RESOURCE_HIGHLIGHT_CRYSTAL(false),
      THREATENED_TILE_MAP(false),
      CORRUPTED_TILES(false);

      private final boolean requiresRefresh;

      private DataDisplayMode(boolean requiresRefresh) {
         this.requiresRefresh = requiresRefresh;
      }

      public boolean requiresRefresh() {
         return this.requiresRefresh;
      }
   }
}
