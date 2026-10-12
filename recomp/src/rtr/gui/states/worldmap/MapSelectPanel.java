package rtr.gui.states.worldmap;

import rtr.utilities.JavaCompat;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.ModuleBase;
import rtr.SettingsParser;
import rtr.font.Text;
import rtr.gui.buttons.GUIButtonGenericWood;
import rtr.gui.buttons.GUIClickableArea;
import rtr.gui.buttons.GUIClickableText;
import rtr.gui.states.WorldMapGUIController;
import rtr.gui.states.WorldMapGUIData;
import rtr.gui.states.WorldMapGUIPanelBase;
import rtr.mobs.MobBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.save.RegionalSavedGame;
import rtr.save.SavedGamesHandler;
import rtr.system.ScaleControl;

public class MapSelectPanel extends WorldMapGUIPanelBase {
   private Image panel;
   private Image panelNew;
   private Image panelSaveRollback;
   private Rectangle mask;
   private Rectangle maskSmall;
   private Rectangle maskNew;
   private GUIButtonGenericWood visitRegion;
   private GUIButtonGenericWood establishVillage;
   private GUIButtonGenericWood visitRegionResetAI;
   private GUIButtonGenericWood close;
   private Image mapPreview;
   private RegionalSavedGame mapSave;
   private GUIClickableText[] saveSlot = new GUIClickableText[7];
   private float fadeAmount;
   private GUIClickableArea corruptionThreatBarArea;

   public MapSelectPanel(WorldMapGUIController controller, WorldMapGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.panel = ImageLoader.getImage("res/GUI/gameState/worldMapMapSelectPanel.png");
      this.panelNew = ImageLoader.getImage("res/GUI/gameState/worldMapNewMapSelectPanel.png");
      this.panelSaveRollback = ImageLoader.getImage("res/GUI/gameState/worldMapMapSelectSaveRollbackPanel.png");
      this.mask = new Rectangle(0.0F, 0.0F, this.panel.getWidth(), this.panel.getHeight() - 141);
      this.masks.add(this.mask);
      this.maskSmall = new Rectangle(0.0F, 0.0F, 174.0F, 140.0F);
      this.masks.add(this.maskSmall);
      this.maskNew = new Rectangle(0.0F, 0.0F, this.panelNew.getWidth(), this.panelNew.getHeight() - 141);
      this.masks.add(this.maskNew);
      this.mapPreview = ImageLoader.getImage("res/ungeneratedMap.bmp");
      this.visitRegion = new GUIButtonGenericWood("visitRegion", Text.getText("worldMapMapSelectPanel.button.visitRegion"));
      this.guiButtons.add(this.visitRegion);
      this.establishVillage = new GUIButtonGenericWood("establishVillage", Text.getText("worldMapMapSelectPanel.button.establishVillage"));
      this.guiButtons.add(this.establishVillage);
      this.visitRegionResetAI = new GUIButtonGenericWood("visitRegionResetAI", Text.getText("worldMapMapSelectPanel.button.visitRegionResetAI"));
      this.visitRegionResetAI
         .setToolTip(
            Text.getText("worldMapMapSelectPanel.button.tooltip.visitRegionResetAIHeader"),
            Text.getText("worldMapMapSelectPanel.button.tooltip.visitRegionResetAI")
         );
      this.guiButtons.add(this.visitRegionResetAI);
      this.close = new GUIButtonGenericWood("close", Text.getText("worldMapMapSelectPanel.button.close"));
      this.guiButtons.add(this.close);
      this.corruptionThreatBarArea = new GUIClickableArea("corruptionThreatBarArea", 508, 11);
      this.corruptionThreatBarArea
         .setToolTip(Text.getText("worldMapMapSelectPanel.tooltip.corruptionThreatHeader"), Text.getText("worldMapMapSelectPanel.tooltip.corruptionThreat"));
      this.guiButtons.add(this.corruptionThreatBarArea);

      for (int i = 0; i < this.saveSlot.length; i++) {
         this.saveSlot[i] = new GUIClickableText("saveSlot" + i, 1);
         this.saveSlot[i]
            .setToolTip(
               Text.getText("worldMapMapSelectPanel.button.tooltip.rollbackSaveHeader"), Text.getText("worldMapMapSelectPanel.button.tooltip.rollbackSave")
            );
         this.guiButtons.add(this.saveSlot[i]);
      }
   }

   public void render(boolean debug) throws SlickException {
      if (this.mapSave != null
         && WorldMapGUIData.getCurrentDay(this.mapSave) != -1
         && (
            WorldMapGUIData.getVillagerList(this.mapSave).size() + WorldMapGUIData.getMigrantList(this.mapSave).size() != 0
               || WorldMapGUIData.getMonsterList(this.mapSave).size() <= 0
         )) {
         this.renderExisting(debug);
      } else {
         this.renderNew(debug);
      }
   }

   public void renderNew(boolean debug) throws SlickException {
      this.x = ScaleControl.getInterfaceWidth() / 2 - this.panelNew.getWidth() / 2;
      this.y = 150;
      this.maskNew.setX(this.x);
      this.maskNew.setY(this.y);
      this.maskSmall.setX(this.x + 59);
      this.maskSmall.setY(this.y + 300);
      if (this.mapSave != null) {
         this.mapPreview.draw(this.x + 17, this.y + 28);
      }

      this.mapPreview.setAlpha(this.fadeAmount);
      this.panelNew.draw(this.x, this.y);
      if (this.mapSave != null) {
         this.font
            .drawString(
               this.x + 144,
               this.y + 3,
               "$ORA1" + this.mapSave.getModuleSaveData(ModuleBase.ModuleType.MAP).getOrDefault("mapName", "null"),
               Text.FontType.HEADER,
               2,
               true
            );
         int migrantsEnRoute = this.migration
            .getEnroutePopulation(this.mapSave.getModuleSaveData(ModuleBase.ModuleType.MAP).getOrDefault("mapFileName", "null"));
         if (WorldMapGUIData.isNewGame()) {
            this.font.drawString(this.x + 144, this.y + 314, Text.getText("worldMapMapSelectPanel.newRegion.header"), Text.FontType.HEADER, 2, true);
            this.establishVillage.render(this.g, this.mouse, this.x + 145, this.y + 354, false, true, debug);
         } else if (WorldMapGUIData.isMapLost(this.mapSave)) {
            this.font.drawString(this.x + 144, this.y + 314, Text.getText("worldMapMapSelectPanel.lostRegion.header"), Text.FontType.HEADER, 2, true);
            this.font.drawString(this.x + 145, this.y + 342, Text.getText("worldMapMapSelectPanel.lostRegion.lostRegion"), Text.FontType.BODY, 1, true);
         } else if (migrantsEnRoute < 10) {
            this.font.drawString(this.x + 144, this.y + 314, Text.getText("worldMapMapSelectPanel.newRegion.header"), Text.FontType.HEADER, 2, true);
            this.font.drawString(this.x + 144, this.y + 336, Text.getText("worldMapMapSelectPanel.newRegion.noMigrants"), Text.FontType.BODY, 1, true);
            Text.setVariableText("amount", migrantsEnRoute);
            this.font.drawString(this.x + 144, this.y + 366, Text.getText("worldMapMapSelectPanel.newRegion.migrantsEnRoute"), Text.FontType.BODY, 1, true);
            Text.setVariableText("amount", 10);
            this.font.drawString(this.x + 144, this.y + 380, Text.getText("worldMapMapSelectPanel.newRegion.migrantsRequired"), Text.FontType.BODY, 1, true);
         } else {
            this.font.drawString(this.x + 144, this.y + 314, Text.getText("worldMapMapSelectPanel.newRegion.header"), Text.FontType.HEADER, 2, true);
            Text.setVariableText("amount", migrantsEnRoute);
            this.font.drawString(this.x + 144, this.y + 342, Text.getText("worldMapMapSelectPanel.newRegion.migrantsEnRoute"), Text.FontType.BODY, 1, true);
            this.establishVillage.render(this.g, this.mouse, this.x + 145, this.y + 364, false, true, debug);
         }

         this.close.render(this.g, this.mouse, this.x + 145, this.y + 395, false, true, debug);
      }
   }

   public void renderExisting(boolean debug) throws SlickException {
      this.x = ScaleControl.getInterfaceWidth() / 2 - this.panel.getWidth() / 2;
      this.y = 150;
      this.mask.setX(this.x);
      this.mask.setY(this.y);
      this.maskSmall.setX(this.x);
      this.maskSmall.setY(this.y + 300);
      if (this.mapSave != null) {
         this.mapPreview.draw(this.x + 289, this.y + 28);
      }

      this.mapPreview.setAlpha(this.fadeAmount);
      this.panel.draw(this.x, this.y);
      this.corruptionThreatBarArea.render(this.g, this.mouse, this.x + 24, this.y + 295, false, debug);
      if (this.mapSave != null) {
         this.font
            .drawString(
               this.x + 413,
               this.y + 3,
               "$ORA1" + this.mapSave.getModuleSaveData(ModuleBase.ModuleType.MAP).getOrDefault("mapName", "null"),
               Text.FontType.HEADER,
               2,
               true
            );
         this.font.drawString(this.x + 82, this.y + 20, Text.getText("worldMapMapSelectPanel.existingRegion.header.resources"), Text.FontType.HEADER, 2, true);
         ArrayList<ResourceBase> resourcesList = WorldMapGUIData.getResourcesList(this.mapSave);
         EnumMap<ResourceModule.ResourceType, ArrayList<ResourceBase>> resourceSort = new EnumMap<>(ResourceModule.ResourceType.class);

         ResourceModule.ResourceType[] resourceCountOver;
         for (ResourceModule.ResourceType type : resourceCountOver = ResourceModule.ResourceType.values()) {
            resourceSort.put(type, new ArrayList<>());
         }

         for (int i = 0; i < resourcesList.size(); i++) {
            ResourceBase r = resourcesList.get(i);
            resourceSort.get(r.getType()).add(r);
         }

         int resourceAlignY = 0;
         int resourceShiftX = 0;
         int resourceCount = 0;
         int resourceCountOverx = 0;

         ResourceModule.ResourceType[] monsterList;
         for (ResourceModule.ResourceType type : monsterList = ResourceModule.ResourceType.values()) {
            if (resourceSort.get(type).size() > 0) {
               if (resourceCount > 16) {
                  resourceCountOverx++;
                  continue;
               }

               for (int i = 0; i < resourceSort.get(type).size(); i++) {
                  ResourceBase r = resourceSort.get(type).get(i);
                  if (i >= 15) {
                     break;
                  }

                  r.renderSprite(this.x + resourceShiftX + 20, this.y + resourceAlignY + 39, false);
                  resourceShiftX += 6;
               }

               this.font.drawString(this.x + 120, this.y + resourceAlignY + 42, "" + resourceSort.get(type).size(), Text.FontType.BODY, 1, false);
               resourceAlignY += 12;
               resourceCount++;
            }

            resourceShiftX = 0;
         }

         if (resourceCountOverx > 0) {
            Text.setVariableText("amount", resourceCountOverx);
            this.font
               .drawString(this.x + 30, this.y + resourceAlignY + 42, Text.getText("worldMapMapSelectPanel.moreResourceTypes"), Text.FontType.BODY, 1, false);
            resourceAlignY += 12;
         }

         if (resourcesList.size() > 0) {
            Text.setVariableText("amount", resourcesList.size());
            this.font.drawString(this.x + 75, this.y + resourceAlignY + 42, Text.getText("worldMapMapSelectPanel.total"), Text.FontType.BODY, 1);
         } else {
            this.font.drawString(this.x + 66, this.y + resourceAlignY + 44, Text.getText("worldMapMapSelectPanel.none"), Text.FontType.BODY, 1);
         }

         this.font.drawString(this.x + 218, this.y + 20, Text.getText("worldMapMapSelectPanel.existingRegion.header.villagers"), Text.FontType.HEADER, 2, true);
         ArrayList<MobBase> villagerList = WorldMapGUIData.getVillagerList(this.mapSave);
         int villagerShiftX = 0;
         int villagerShiftY = 0;

         for (int i = 0; i < villagerList.size(); i++) {
            MobBase m = villagerList.get(i);
            if (i % 14 == 0 && i != 0) {
               villagerShiftY += 18;
               villagerShiftX = 0;
            }

            if (i >= 80) {
               this.font.drawString(this.x + 250, this.y + 137, "$GRE1+" + (villagerList.size() - 80), Text.FontType.BODY, 1);
               break;
            }

            m.renderSprite(this.g, this.x + villagerShiftX + 153, this.y + villagerShiftY + 47, debug);
            m.getSprite().update();
            m.setPositionFace();
            villagerShiftX += 9;
         }

         if (villagerList.size() > 0) {
            Text.setVariableText("amount", villagerList.size());
            this.font.drawString(this.x + 210, this.y + villagerShiftY + 62, Text.getText("worldMapMapSelectPanel.total"), Text.FontType.BODY, 1);
         } else {
            this.font.drawString(this.x + 203, this.y + 44, Text.getText("worldMapMapSelectPanel.none"), Text.FontType.BODY, 1);
         }

         for (int i = 0; i < villagerList.size(); i++) {
            MobBase mx = villagerList.get(i);
            if (mx.intersectsGUI(this.mouse)) {
               this.controller.getMobOverlay().render(mx, this.mouse.getX(), this.mouse.getY() - 60.0F, this.g, debug);
            }
         }

         this.font.drawString(this.x + 218, this.y + 171, Text.getText("worldMapMapSelectPanel.existingRegion.header.monsters"), Text.FontType.HEADER, 2, true);
         ArrayList<MobBase> monsterListx = WorldMapGUIData.getMonsterList(this.mapSave);
         int monsterShiftX = 0;
         int monsterShiftY = 0;

         for (int ix = 0; ix < monsterListx.size(); ix++) {
            MobBase mx = monsterListx.get(ix);
            if (ix % 14 == 0 && ix != 0) {
               monsterShiftY += 18;
               monsterShiftX = 0;
            }

            if (ix >= 52) {
               this.font.drawString(this.x + 250, this.y + 252, "$GRE1+" + (monsterListx.size() - 52), Text.FontType.BODY, 1);
               break;
            }

            mx.renderSprite(this.g, this.x + monsterShiftX + 153, this.y + monsterShiftY + 198, debug);
            mx.getSprite().update();
            mx.setPositionFace();
            monsterShiftX += 9;
         }

         if (monsterListx.size() > 0) {
            Text.setVariableText("amount", monsterListx.size());
            this.font.drawString(this.x + 210, this.y + monsterShiftY + 213, Text.getText("worldMapMapSelectPanel.total"), Text.FontType.BODY, 1);
         } else {
            this.font.drawString(this.x + 203, this.y + 195, Text.getText("worldMapMapSelectPanel.none"), Text.FontType.BODY, 1);
         }

         for (int ix = 0; ix < monsterListx.size(); ix++) {
            MobBase mxx = monsterListx.get(ix);
            if (mxx.intersectsGUI(this.mouse)) {
               this.controller.getMobOverlay().render(mxx, this.mouse.getX(), this.mouse.getY() - 60.0F, this.g, debug);
            }
         }

         if (!SettingsParser.getDebug() && !this.gc.getInput().isKeyDown(42)) {
            this.visitRegion.render(this.g, this.mouse, this.x + 87, this.y + 340, false, true, debug);
            this.close.render(this.g, this.mouse, this.x + 87, this.y + 380, false, true, debug);
         } else {
            this.visitRegion.render(this.g, this.mouse, this.x + 87, this.y + 330, false, true, debug);
            this.visitRegionResetAI.render(this.g, this.mouse, this.x + 87, this.y + 360, false, true, debug);
            this.close.render(this.g, this.mouse, this.x + 87, this.y + 390, false, true, debug);
            this.renderSaveRollback(this.x + 174, this.y + 305, debug);
         }
      }
   }

   private void renderSaveRollback(float x, float y, boolean debug) {
      this.panelSaveRollback.draw(x, y);
      this.font.drawString(x + 87.0F, y + 9.0F, Text.getText("worldMapMapSelectPanel.existingRegion.header.saveRollback"), Text.FontType.HEADER, 2, true);
      float alignY = 0.0F;
      ArrayList<File> savesList = SavedGamesHandler.getSortedListOfSaves(this.mapSave.getBaseSaveFolder(), this.mapSave.getGameMode());
      if (savesList.size() > 0) {
         savesList.remove(0);

         for (int i = 0; i < savesList.size() && i < this.saveSlot.length; i++) {
            File thisSave = savesList.get(i);
            String parsedDate = JavaCompat.fileTimeIso(thisSave);
            parsedDate = parsedDate.replaceAll("T", " at ");
            parsedDate = parsedDate.substring(0, 19);
            this.saveSlot[i].render(this.g, this.mouse, parsedDate, (int)(x + 34.0F), (int)(y + 32.0F + alignY), "$YEL0", "$GRE0", false, false, debug);
            alignY += 12.0F;
         }
      }
   }

   public RegionalSavedGame getMapSave() {
      return this.mapSave;
   }

   public void setMapSave(RegionalSavedGame s) throws SlickException {
      this.mapSave = s;
      this.mapPreview = s.getMiniMapImagePreview();
      this.fadeAmount = 0.0F;
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

   public void fadeIn() {
      if (this.fadeAmount < 1.0F) {
         this.fadeAmount += 0.02F;
         if (this.fadeAmount > 1.0F) {
            this.fadeAmount = 1.0F;
         }
      }
   }
}
