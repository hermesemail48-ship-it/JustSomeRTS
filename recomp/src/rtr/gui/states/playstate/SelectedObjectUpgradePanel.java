package rtr.gui.states.playstate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.gui.buttons.GUIIcon;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.PlayStateGUIPanelBase;
import rtr.map.MapTilesLoader;
import rtr.missiles.MissileBase;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.objects.towerhead.TowerBase;
import rtr.objects.towerhead.towerheadflags.TowerHeadFlags;
import rtr.resources.ResourceModule;

public class SelectedObjectUpgradePanel extends PlayStateGUIPanelBase {
   private Image panel;
   private Rectangle mask;
   private EnumMap<ResourceModule.ResourceType, GUIIcon> harvestingIcons = new EnumMap<>(ResourceModule.ResourceType.class);
   private EnumMap<ResourceModule.ResourceType, GUIIcon> refiningIcons = new EnumMap<>(ResourceModule.ResourceType.class);
   private GUIIcon iconEnergy;
   private EnumMap<ResourceModule.ResourceType, GUIIcon> icons = new EnumMap<>(ResourceModule.ResourceType.class);
   private GUIIcon iconHomeOccupants;
   private GUIIcon iconRangerLodgeOccupants;
   private GUIIcon iconWorkers;
   private GUIIcon iconBuildingSlots;
   private GUIIcon iconRange;
   private GUIIcon iconDesirability;
   private GUIIcon iconAmmo;
   private GUIIcon objectDescriptionStorage;

   public SelectedObjectUpgradePanel(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.panel = ImageLoader.getImage("res/GUI/gameState/objectUpgradePanel.png");
      this.mask = new Rectangle(0.0F, 0.0F, this.panel.getWidth(), this.panel.getHeight());
      this.masks.add(this.mask);

      ResourceModule.ResourceType[] var9;
      for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
         this.harvestingIcons.put(type, new GUIIcon(type));
         this.guiButtons.add(this.harvestingIcons.get(type));
         this.refiningIcons.put(type, new GUIIcon(type));
         this.guiButtons.add(this.refiningIcons.get(type));
      }

      this.iconEnergy = new GUIIcon("iconEnergy");
      this.guiButtons.add(this.iconEnergy);

      for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
         GUIIcon newIcon = new GUIIcon(type);
         this.icons.put(type, newIcon);
         this.guiButtons.add(newIcon);
      }

      this.iconHomeOccupants = new GUIIcon("iconHome");
      this.guiButtons.add(this.iconHomeOccupants);
      this.iconRangerLodgeOccupants = new GUIIcon("iconRangerLodge");
      this.guiButtons.add(this.iconRangerLodgeOccupants);
      this.iconWorkers = new GUIIcon("iconWorkers");
      this.guiButtons.add(this.iconWorkers);
      this.iconBuildingSlots = new GUIIcon("iconCivic");
      this.guiButtons.add(this.iconBuildingSlots);
      this.iconRange = new GUIIcon("iconRange");
      this.guiButtons.add(this.iconRange);
      this.iconDesirability = new GUIIcon("iconDesirability");
      this.guiButtons.add(this.iconDesirability);
      this.objectDescriptionStorage = new GUIIcon("iconChest");
      this.guiButtons.add(this.objectDescriptionStorage);
      this.iconAmmo = new GUIIcon("iconAmmo");
      this.guiButtons.add(this.iconAmmo);
   }

   public void render(int x, int y, ObjectFlags nextUpgradeFlags, boolean debug) throws SlickException {
      this.x = x;
      this.y = y;
      this.mask.setX(x);
      this.mask.setY(y);
      this.panel.draw(x, y);
      ObjectFlags selectedObjectFlags = this.object
         .getObjectFlagFactory()
         .getObjectFlags(this.guiData.getSelectedObject().getObjectFlags().getCurrentType(), this.guiData.getSelectedObject().getObjectFlags().getSubType());
      if (nextUpgradeFlags.getName().length() > 32) {
         this.font.drawString(x + 135, y + 8, "$CYA1" + nextUpgradeFlags.getName(), Text.FontType.HEADER, 0, true);
      } else if (nextUpgradeFlags.getName().length() > 24) {
         this.font.drawString(x + 135, y + 6, "$CYA1" + nextUpgradeFlags.getName(), Text.FontType.HEADER, 1, true);
      } else {
         this.font.drawString(x + 135, y + 4, "$CYA1" + nextUpgradeFlags.getName(), Text.FontType.HEADER, 2, true);
      }

      EnumMap<ResourceModule.ResourceType, String> manufacuringBuildingNames = new EnumMap<>(ResourceModule.ResourceType.class);
      HashMap<MapTilesLoader.TileSet, HashMap<ObjectBase.ObjectSubType, ObjectFlags>> objectFlagsList = this.object.getObjectFlagFactory().getObjectFlagsList();

      ResourceModule.ResourceType[] buildingDescription;
      for (ResourceModule.ResourceType type : buildingDescription = ResourceModule.ResourceType.values()) {
         for (HashMap<ObjectBase.ObjectSubType, ObjectFlags> o : objectFlagsList.values()) {
            if (o.get(ObjectBase.ObjectSubType.BUILT).getWorkerJobType().getJob().canWorkerRefine(type)
               && o.get(ObjectBase.ObjectSubType.BUILT).getCanBuildingStore().contains(type)) {
               manufacuringBuildingNames.put(type, o.get(ObjectBase.ObjectSubType.BUILT).getBaseName());
            }
         }
      }

      int alignX = 0;
      int alignY = 27;
      int count = 0;
      String buildingDescriptionx = Text.wrapString(nextUpgradeFlags.getDescription(), Text.FontType.BODY, 1, 230);
      this.font.drawString(x + alignX + 20, y + alignY, buildingDescriptionx, Text.FontType.BODY, 1);
      alignY += 81;
      this.font.drawString(x + alignX + 24, y + alignY, Text.getText("playSelectedObjectUpgradePanel.header.constructionRequirements"), Text.FontType.BODY, 2);
      alignY += 18;
      boolean foundRefined = false;

      ResourceModule.ResourceType[] selectedMaximumStorage;
      for (ResourceModule.ResourceType type : selectedMaximumStorage = ResourceModule.ResourceType.values()) {
         int valueDifference = nextUpgradeFlags.getResourceValueBaseCount(type) - selectedObjectFlags.getResourceValueBaseCount(type);
         if (valueDifference > 0 && type.getTemplate().getBaseResources().size() > 0) {
            Text.setVariableText("buildingName", manufacuringBuildingNames.get(type));
            Text.setVariableText("resourceName", type.getTemplate().getNamePlural());
            this.font.drawString(x + alignX + 24, y + alignY, Text.getText("playSelectedObjectUpgradePanel.constructionRequirements"), Text.FontType.BODY, 1);
            foundRefined = true;
            alignY += 12;
         }
      }

      if (foundRefined) {
         alignY += 4;
      }

      for (ResourceModule.ResourceType typex : selectedMaximumStorage = ResourceModule.ResourceType.values()) {
         int valueDifference = nextUpgradeFlags.getResourceValueBaseCount(typex) - selectedObjectFlags.getResourceValueBaseCount(typex);
         if (valueDifference > 0) {
            this.icons.get(typex).render(this.g, x + alignX + 20, y + alignY, debug);
            this.font.drawString(x + alignX + 39, y + alignY, "$RED1" + valueDifference + " " + typex.getTemplate().getNamePlural(), Text.FontType.BODY, 1);
            if (count % 2 == 0) {
               alignX += 110;
            } else {
               alignY += 12;
               alignX = 0;
            }

            count++;
         }
      }

      if (alignX == 110) {
         alignY += 12;
      }

      int var50 = 0;
      alignY += 8;
      ArrayList<MapTilesLoader.TileSet[]> requiredOut = new ArrayList<>();

      label629:
      for (MapTilesLoader.TileSet[] a : nextUpgradeFlags.getRequiredSupportBuildings()) {
         for (MapTilesLoader.TileSet[] a2 : selectedObjectFlags.getRequiredSupportBuildings()) {
            if (Arrays.equals((Object[])a, (Object[])a2)) {
               continue label629;
            }
         }

         requiredOut.add(a);
      }

      if (requiredOut.size() > 0) {
         this.font.drawString(x + var50 + 24, y + alignY, Text.getText("playSelectedObjectUpgradePanel.header.requiredSupport"), Text.FontType.BODY, 2);
         alignY += 16;

         for (MapTilesLoader.TileSet[] a : requiredOut) {
            String out = "";

            for (MapTilesLoader.TileSet t : a) {
               if (out.equals("")) {
                  out = "$RED0" + this.object.getObjectFlagFactory().getObjectFlags(t, ObjectBase.ObjectSubType.BUILT).getBaseName();
               } else {
                  out = out
                     + Text.getText("playSelectedObjectUpgradePanel.supportConjuntion")
                     + this.object.getObjectFlagFactory().getObjectFlags(t, ObjectBase.ObjectSubType.BUILT).getBaseName();
               }
            }

            this.font.drawString(x + var50 + 40, y + alignY, out, Text.FontType.BODY, 1);
            alignY += 12;
         }

         alignY += 12;
      }

      ArrayList<MapTilesLoader.TileSet[]> recommendedOut = new ArrayList<>();

      label603:
      for (MapTilesLoader.TileSet[] a : nextUpgradeFlags.getRecommendedSupportBuildings()) {
         for (MapTilesLoader.TileSet[] a2x : selectedObjectFlags.getRecommendedSupportBuildings()) {
            if (Arrays.equals((Object[])a, (Object[])a2x)) {
               continue label603;
            }
         }

         recommendedOut.add(a);
      }

      if (recommendedOut.size() > 0) {
         this.font.drawString(x + var50 + 24, y + alignY, Text.getText("playSelectedObjectUpgradePanel.header.recommendedSupport"), Text.FontType.BODY, 2);
         alignY += 16;

         for (MapTilesLoader.TileSet[] a : recommendedOut) {
            String out = "";

            for (MapTilesLoader.TileSet tx : a) {
               if (out.equals("")) {
                  out = "$GRE0" + this.object.getObjectFlagFactory().getObjectFlags(tx, ObjectBase.ObjectSubType.BUILT).getBaseName();
               } else {
                  out = out
                     + Text.getText("playSelectedObjectUpgradePanel.playSelectedObjectUpgradePanel.supportConjuntion")
                     + this.object.getObjectFlagFactory().getObjectFlags(tx, ObjectBase.ObjectSubType.BUILT).getBaseName();
               }
            }

            this.font.drawString(x + var50 + 40, y + alignY, out, Text.FontType.BODY, 1);
            alignY += 12;
         }

         alignY += 12;
      }

      int nextMaximumStorage = 0;

      for (ResourceModule.ResourceType txx : nextUpgradeFlags.getResourceStorageMax().keySet()) {
         nextMaximumStorage += nextUpgradeFlags.getResourceStorageMax().get(txx);
      }

      int selectedMaximumStoragex = 0;

      for (ResourceModule.ResourceType txx : selectedObjectFlags.getResourceStorageMax().keySet()) {
         selectedMaximumStoragex += selectedObjectFlags.getResourceStorageMax().get(txx);
      }

      int storageMaxDifference = nextMaximumStorage - selectedMaximumStoragex;
      int energyMaxDifference = nextUpgradeFlags.getEnergyMax() - selectedObjectFlags.getEnergyMax();
      if (storageMaxDifference > 0 || energyMaxDifference > 0) {
         count = 0;
         this.font.drawString(x + var50 + 24, y + alignY, Text.getText("playSelectedObjectUpgradePanel.header.canStore"), Text.FontType.BODY, 2);
         alignY += 20;
         if (storageMaxDifference > 0) {
            this.objectDescriptionStorage.render(this.g, x + var50 + 20, y + alignY, debug);
            Text.setVariableText("amount", storageMaxDifference);
            this.font.drawString(x + var50 + 40, y + alignY - 1, Text.getText("playSelectedObjectUpgradePanel.maximumStorage"), Text.FontType.BODY, 1);
            alignY += 10;
         }

         if (energyMaxDifference > 0) {
            this.iconEnergy.render(this.g, x + var50 + 20, y + alignY, debug);
            Text.setVariableText("amount", energyMaxDifference);
            this.font.drawString(x + var50 + 39, y + alignY, Text.getText("playSelectedObjectUpgradePanel.energy"), Text.FontType.BODY, 1);
            if (count % 2 == 0) {
               var50 += 110;
            } else {
               alignY += 12;
               var50 = 0;
            }

            count++;
         }

         ResourceModule.ResourceType[] hasHousing;
         for (ResourceModule.ResourceType typexx : hasHousing = ResourceModule.ResourceType.values()) {
            int next = nextUpgradeFlags.getResourceStorageMax().get(typexx);
            int current = selectedObjectFlags.getResourceStorageMax().get(typexx);
            int difference = next - current;
            if (difference > 0) {
               this.icons.get(typexx).render(this.g, x + var50 + 20, y + alignY, debug);
               String stringOut = "+" + difference + " $YEL1" + typexx.getTemplate().getNamePlural();
               this.font.drawString(x + var50 + 39, y + 4 + alignY, stringOut, Text.FontType.BODY, 0);
               if (count % 2 == 0 && stringOut.length() < 25) {
                  var50 += 110;
               } else {
                  alignY += 10;
                  var50 = 0;
               }

               if (count > 16) {
                  this.font
                     .drawString(x + var50 + 39, y + 4 + alignY, Text.getText("playSelectedObjectUpgradePanel.canStoreMoreOverflow"), Text.FontType.BODY, 0);
                  break;
               }

               count++;
            }
         }

         var50 = 0;
         if (count != 0) {
            if (count % 2 == 0) {
               alignY += 12;
            } else {
               alignY += 24;
            }
         }
      }

      boolean hasGeneral = false;
      boolean hasHarvest = false;
      boolean hasRefine = false;
      boolean hasHousingx = false;

      ResourceModule.ResourceType[] var133;
      for (ResourceModule.ResourceType txx : ResourceModule.ResourceType.values()) {
         if (!hasGeneral
            && (
               nextUpgradeFlags.getGlobalWorkSpeedReduction() - selectedObjectFlags.getGlobalWorkSpeedReduction() > 0.0
                  || nextUpgradeFlags.getProcessingTrashSpeedReduction() - selectedObjectFlags.getProcessingTrashSpeedReduction() > 0.0
                  || nextUpgradeFlags.getBuildingSpeedReduction() - selectedObjectFlags.getBuildingSpeedReduction() > 0.0
                  || nextUpgradeFlags.getBuildingRoadSpeedReduction() - selectedObjectFlags.getBuildingRoadSpeedReduction() > 0.0
                  || nextUpgradeFlags.getDismantlingSpeedReduction() - selectedObjectFlags.getDismantlingSpeedReduction() > 0.0
                  || nextUpgradeFlags.getClearingSpeedReduction() - selectedObjectFlags.getClearingSpeedReduction() > 0.0
                  || nextUpgradeFlags.getRepairingSpeedReduction() - selectedObjectFlags.getRepairingSpeedReduction() > 0.0
                  || nextUpgradeFlags.getRepairingRoadSpeedReduction() - selectedObjectFlags.getRepairingRoadSpeedReduction() > 0.0
                  || nextUpgradeFlags.getFarmingSpeedReduction() - selectedObjectFlags.getFarmingSpeedReduction() > 0.0
                  || nextUpgradeFlags.getMedicSpeedReduction() - selectedObjectFlags.getMedicSpeedReduction() > 0.0
                  || nextUpgradeFlags.getPrayingSpeedReduction() - selectedObjectFlags.getPrayingSpeedReduction() > 0.0
                  || nextUpgradeFlags.getCapturingSoulsSpeedReduction() - selectedObjectFlags.getCapturingSoulsSpeedReduction() > 0.0
                  || nextUpgradeFlags.getTendAnimalsSpeedReduction() - selectedObjectFlags.getTendAnimalsSpeedReduction() > 0.0
            )) {
            hasGeneral = true;
         }

         if (!hasHarvest && nextUpgradeFlags.getHarvestingSpeedReduction(txx) - selectedObjectFlags.getHarvestingSpeedReduction(txx) > 0.0) {
            hasHarvest = true;
         }

         if (!hasRefine && nextUpgradeFlags.getRefiningSpeedReduction(txx) - selectedObjectFlags.getRefiningSpeedReduction(txx) > 0.0) {
            hasRefine = true;
         }

         if (!hasHousingx
            && (
               nextUpgradeFlags.getOccupantsSleepBonusPercentOff() - selectedObjectFlags.getOccupantsSleepBonusPercentOff() != 0
                  || nextUpgradeFlags.getOccupantsHealthRegenBonusTicks() - selectedObjectFlags.getOccupantsHealthRegenBonusTicks() != 0
                  || nextUpgradeFlags.getOccupantsFaithBonus() - selectedObjectFlags.getOccupantsFaithBonus() != 0
            )) {
            hasHousingx = true;
         }

         if (hasGeneral && hasHarvest && hasRefine && hasHousingx) {
            break;
         }
      }

      if (hasGeneral || hasHarvest || hasRefine) {
         this.font.drawString(x + var50 + 24, y + alignY, Text.getText("playSelectedObjectUpgradePanel.header.globalBonuses"), Text.FontType.BODY, 2, false);
         alignY += 16;
      }

      if (hasGeneral) {
         double globalWorkSpeedReductionDifference = nextUpgradeFlags.getGlobalWorkSpeedReduction() - selectedObjectFlags.getGlobalWorkSpeedReduction();
         if (globalWorkSpeedReductionDifference > 0.0) {
            Text.setVariableText("amount", globalWorkSpeedReductionDifference);
            this.font.drawString(x + var50 + 20, y + alignY, Text.getText("playSelectedObjectUpgradePanel.globalSpeed"), Text.FontType.BODY, 1, false);
            alignY += 12;
         }

         double processingTrashSpeedReduction = nextUpgradeFlags.getProcessingTrashSpeedReduction() - selectedObjectFlags.getProcessingTrashSpeedReduction();
         if (processingTrashSpeedReduction > 0.0) {
            Text.setVariableText("amount", processingTrashSpeedReduction);
            this.font.drawString(x + var50 + 20, y + alignY, Text.getText("playSelectedObjectUpgradePanel.processingTrashSpeed"), Text.FontType.BODY, 1, false);
            alignY += 12;
         }

         double buildingSpeedReduction = nextUpgradeFlags.getBuildingSpeedReduction() - selectedObjectFlags.getBuildingSpeedReduction();
         if (buildingSpeedReduction > 0.0) {
            Text.setVariableText("amount", buildingSpeedReduction);
            this.font.drawString(x + var50 + 20, y + alignY, Text.getText("playSelectedObjectUpgradePanel.buildingSpeed"), Text.FontType.BODY, 1, false);
            alignY += 12;
         }

         double buildingRoadSpeedReduction = nextUpgradeFlags.getBuildingRoadSpeedReduction() - selectedObjectFlags.getBuildingRoadSpeedReduction();
         if (buildingRoadSpeedReduction > 0.0) {
            Text.setVariableText("amount", buildingRoadSpeedReduction);
            this.font.drawString(x + var50 + 20, y + alignY, Text.getText("playSelectedObjectUpgradePanel.buildRoadSpeed"), Text.FontType.BODY, 1, false);
            alignY += 12;
         }

         double dismantlingSpeedReduction = nextUpgradeFlags.getDismantlingSpeedReduction() - selectedObjectFlags.getDismantlingSpeedReduction();
         if (dismantlingSpeedReduction > 0.0) {
            Text.setVariableText("amount", dismantlingSpeedReduction);
            this.font.drawString(x + var50 + 20, y + alignY, Text.getText("playSelectedObjectUpgradePanel.dismantlingSpeed"), Text.FontType.BODY, 1, false);
            alignY += 12;
         }

         double clearingSpeedReduction = nextUpgradeFlags.getClearingSpeedReduction() - selectedObjectFlags.getClearingSpeedReduction();
         if (clearingSpeedReduction > 0.0) {
            Text.setVariableText("amount", clearingSpeedReduction);
            this.font.drawString(x + var50 + 20, y + alignY, Text.getText("playSelectedObjectUpgradePanel.clearingSpeed"), Text.FontType.BODY, 1, false);
            alignY += 12;
         }

         double repairingSpeedReduction = nextUpgradeFlags.getRepairingSpeedReduction() - selectedObjectFlags.getRepairingSpeedReduction();
         if (repairingSpeedReduction > 0.0) {
            Text.setVariableText("amount", repairingSpeedReduction);
            this.font.drawString(x + var50 + 20, y + alignY, Text.getText("playSelectedObjectUpgradePanel.repairingSpeed"), Text.FontType.BODY, 1, false);
            alignY += 12;
         }

         double repairingRoadSpeedReduction = nextUpgradeFlags.getRepairingRoadSpeedReduction() - selectedObjectFlags.getRepairingRoadSpeedReduction();
         if (repairingRoadSpeedReduction > 0.0) {
            Text.setVariableText("amount", repairingRoadSpeedReduction);
            this.font.drawString(x + var50 + 20, y + alignY, Text.getText("playSelectedObjectUpgradePanel.repairRoadSpeed"), Text.FontType.BODY, 1, false);
            alignY += 12;
         }

         double farmingSpeedReduction = nextUpgradeFlags.getFarmingSpeedReduction() - selectedObjectFlags.getFarmingSpeedReduction();
         if (farmingSpeedReduction > 0.0) {
            Text.setVariableText("amount", farmingSpeedReduction);
            this.font.drawString(x + var50 + 20, y + alignY, Text.getText("playSelectedObjectUpgradePanel.farmingSpeed"), Text.FontType.BODY, 1, false);
            alignY += 12;
         }

         double medicSpeedReduction = nextUpgradeFlags.getMedicSpeedReduction() - selectedObjectFlags.getMedicSpeedReduction();
         if (medicSpeedReduction > 0.0) {
            Text.setVariableText("amount", medicSpeedReduction);
            this.font.drawString(x + var50 + 20, y + alignY, Text.getText("playSelectedObjectUpgradePanel.medicWorkSpeed"), Text.FontType.BODY, 1, false);
            alignY += 12;
         }

         double prayingSpeedReduction = nextUpgradeFlags.getPrayingSpeedReduction() - selectedObjectFlags.getPrayingSpeedReduction();
         if (prayingSpeedReduction > 0.0) {
            Text.setVariableText("amount", prayingSpeedReduction);
            this.font.drawString(x + var50 + 20, y + alignY, Text.getText("playSelectedObjectUpgradePanel.prayingSpeed"), Text.FontType.BODY, 1, false);
            alignY += 12;
         }

         double capturingSoulsSpeedReduction = nextUpgradeFlags.getCapturingSoulsSpeedReduction() - selectedObjectFlags.getCapturingSoulsSpeedReduction();
         if (capturingSoulsSpeedReduction > 0.0) {
            Text.setVariableText("amount", capturingSoulsSpeedReduction);
            this.font.drawString(x + var50 + 20, y + alignY, Text.getText("playSelectedObjectUpgradePanel.capturingSoulsSpeed"), Text.FontType.BODY, 1, false);
            alignY += 12;
         }

         double tendAnimalsSpeedReduction = nextUpgradeFlags.getTendAnimalsSpeedReduction() - selectedObjectFlags.getTendAnimalsSpeedReduction();
         if (tendAnimalsSpeedReduction > 0.0) {
            Text.setVariableText("amount", tendAnimalsSpeedReduction);
            this.font.drawString(x + var50 + 20, y + alignY, Text.getText("playSelectedObjectUpgradePanel.tendingAnimalsSpeed"), Text.FontType.BODY, 1, false);
            alignY += 12;
         }
      }

      if (hasHarvest) {
         this.font.drawString(x + var50 + 24, y + alignY, Text.getText("playSelectedObjectUpgradePanel.harvestSpeed"), Text.FontType.BODY, 1, false);
         alignY += 14;

         for (ResourceModule.ResourceType txx : ResourceModule.ResourceType.values()) {
            double harvestingSpeedReduction = nextUpgradeFlags.getHarvestingSpeedReduction(txx);
            harvestingSpeedReduction -= selectedObjectFlags.getHarvestingSpeedReduction(txx);
            if (harvestingSpeedReduction > 0.0) {
               this.harvestingIcons.get(txx).render(this.g, x + var50 + 20, y + alignY, debug);
               Text.setVariableText("amount", harvestingSpeedReduction);
               this.font.drawString(x + var50 + 35, y + alignY, "$GRE0+" + (int)harvestingSpeedReduction + "%", Text.FontType.BODY, 1, false);
               var50 += 45;
               if (var50 / 45 >= 5) {
                  var50 = 0;
                  alignY += 12;
               }
            }
         }

         alignY += 12;
      }

      var50 = 0;
      if (hasRefine) {
         this.font.drawString(x + var50 + 24, y + alignY, Text.getText("playSelectedObjectUpgradePanel.refineSpeed"), Text.FontType.BODY, 1, false);
         alignY += 14;

         for (ResourceModule.ResourceType txxx : ResourceModule.ResourceType.values()) {
            double refiningSpeedReduction = nextUpgradeFlags.getRefiningSpeedReduction(txxx);
            refiningSpeedReduction -= selectedObjectFlags.getRefiningSpeedReduction(txxx);
            if (refiningSpeedReduction > 0.0) {
               this.refiningIcons.get(txxx).render(this.g, x + var50 + 20, y + alignY, debug);
               this.font.drawString(x + var50 + 35, y + alignY, "$GRE0+" + (int)refiningSpeedReduction + "%", Text.FontType.BODY, 1, false);
               var50 += 45;
               if (var50 / 45 >= 5) {
                  var50 = 0;
                  alignY += 12;
               }
            }
         }

         alignY += 12;
      }

      alignY += 12;
      var50 = 0;
      if (hasHousingx) {
         this.font.drawString(x + var50 + 24, y + alignY, Text.getText("playSelectedObjectUpgradePanel.header.homeBonuses"), Text.FontType.BODY, 2, false);
         alignY += 16;
      }

      if (hasHousingx) {
         double occupantsSleepBonusPercentOff = nextUpgradeFlags.getOccupantsSleepBonusPercentOff() - selectedObjectFlags.getOccupantsSleepBonusPercentOff();
         if (occupantsSleepBonusPercentOff > 0.0) {
            Text.setVariableText("amount", occupantsSleepBonusPercentOff);
            this.font.drawString(x + var50 + 20, y + alignY, Text.getText("playSelectedObjectUpgradePanel.sleepSpeedPlus"), Text.FontType.BODY, 1, false);
            alignY += 12;
         } else if (occupantsSleepBonusPercentOff < 0.0) {
            Text.setVariableText("amount", occupantsSleepBonusPercentOff);
            this.font.drawString(x + var50 + 20, y + alignY, Text.getText("playSelectedObjectUpgradePanel.sleepSpeedMinus"), Text.FontType.BODY, 1, false);
            alignY += 12;
         }

         double occupantsHealthRegenBonusTicks = nextUpgradeFlags.getOccupantsHealthRegenBonusTicks() - selectedObjectFlags.getOccupantsHealthRegenBonusTicks();
         if (occupantsHealthRegenBonusTicks > 0.0) {
            Text.setVariableText("amount", occupantsHealthRegenBonusTicks);
            this.font
               .drawString(x + var50 + 20, y + alignY, Text.getText("playSelectedObjectUpgradePanel.healthRegenerationPlus"), Text.FontType.BODY, 1, false);
            alignY += 12;
         } else if (occupantsHealthRegenBonusTicks < 0.0) {
            Text.setVariableText("amount", occupantsHealthRegenBonusTicks);
            this.font
               .drawString(x + var50 + 20, y + alignY, Text.getText("playSelectedObjectUpgradePanel.healthRegenerationMinus"), Text.FontType.BODY, 1, false);
            alignY += 12;
         }

         double occupantsFaithBonus = nextUpgradeFlags.getOccupantsFaithBonus() - selectedObjectFlags.getOccupantsFaithBonus();
         if (occupantsFaithBonus > 0.0) {
            Text.setVariableText("amount", occupantsFaithBonus);
            this.font.drawString(x + var50 + 20, y + alignY, Text.getText("playSelectedObjectUpgradePanel.faithBonusPlus"), Text.FontType.BODY, 1, false);
            alignY += 12;
         } else if (occupantsFaithBonus < 0.0) {
            Text.setVariableText("amount", occupantsFaithBonus);
            this.font.drawString(x + var50 + 20, y + alignY, Text.getText("playSelectedObjectUpgradePanel.faithBonusMinus"), Text.FontType.BODY, 1, false);
            alignY += 12;
         }
      }

      if (nextUpgradeFlags.getTowerType() != null) {
         TowerHeadFlags nextUpgradeTowerFlags = this.object
            .getTowerHeadFlagFactory()
            .getTowerFlags(nextUpgradeFlags.getTowerType(), nextUpgradeFlags.getTowerUpgradeStage());
         TowerHeadFlags selectedUpgradeTowerFlags = null;
         if (selectedObjectFlags.getTowerType() != null) {
            selectedUpgradeTowerFlags = this.object
               .getTowerHeadFlagFactory()
               .getTowerFlags(selectedObjectFlags.getTowerType(), selectedObjectFlags.getTowerUpgradeStage());
         }

         this.font.drawString(x + var50 + 24, y + alignY, Text.getText("playSelectedObjectUpgradePanel.header.defensiveTower"), Text.FontType.BODY, 2);
         alignY += 16;
         MissileModule.MissileType oldTowerMissileType = null;
         MissileModule.MissileType oldTowerSubMissileType = null;
         if (selectedObjectFlags.getTowerType() != null) {
            oldTowerMissileType = selectedUpgradeTowerFlags.getMissileType();
            oldTowerSubMissileType = selectedUpgradeTowerFlags.getAltMissileType();
         }

         MissileModule.MissileType newTowerMissileType = nextUpgradeTowerFlags.getMissileType();
         MissileModule.MissileType newTowerSubMissileType = nextUpgradeTowerFlags.getAltMissileType();
         MissileBase oldTowerMissile = null;
         if (oldTowerMissileType != null) {
            oldTowerMissile = this.missile.getMissileFactory().getMissileInstance(oldTowerMissileType);
         }

         MissileBase oldTowerSubMissile = null;
         if (oldTowerSubMissileType != null) {
            oldTowerSubMissile = this.missile.getMissileFactory().getMissileInstance(oldTowerSubMissileType);
         }

         MissileBase newTowerMissile = null;
         if (newTowerMissileType != null) {
            newTowerMissile = this.missile.getMissileFactory().getMissileInstance(newTowerMissileType);
         }

         MissileBase newTowerSubMissile = null;
         if (newTowerSubMissileType != null) {
            newTowerSubMissile = this.missile.getMissileFactory().getMissileInstance(newTowerSubMissileType);
         }

         if (nextUpgradeTowerFlags.getTowerType() == TowerBase.TowerType.STATIC) {
            this.iconAmmo.render(this.g, x + var50 + 20, y + alignY, debug);
            this.font.drawString(x + var50 + 40, y + alignY, Text.getText("playSelectedObjectUpgradePanel.staticTowerDamageOverride"), Text.FontType.BODY, 1);
            alignY += 16;
         }

         if (newTowerMissile != null && newTowerMissile.getDamageTypesMax().size() > 0) {
            for (MobBase.DamageType d : newTowerMissile.getDamageTypesMax().keySet()) {
               int damageTypeMinDifference = newTowerMissile.getDamageTypesMin().get(d);
               if (oldTowerMissile != null && oldTowerMissile.getDamageTypesMin().containsKey(d)) {
                  damageTypeMinDifference -= oldTowerMissile.getDamageTypesMin().get(d);
               }

               int damageTypeMaxDifference = newTowerMissile.getDamageTypesMax().get(d);
               if (oldTowerMissile != null && oldTowerMissile.getDamageTypesMax().containsKey(d)) {
                  damageTypeMaxDifference -= oldTowerMissile.getDamageTypesMax().get(d);
               }

               if (damageTypeMinDifference > 0 || damageTypeMaxDifference > 0) {
                  this.iconAmmo.render(this.g, x + var50 + 20, y + alignY, debug);
                  Text.setVariableText("damageType", d.getText());
                  Text.setVariableText("damageMinimum", damageTypeMinDifference);
                  Text.setVariableText("damageMaximum", damageTypeMaxDifference);
                  if (newTowerSubMissile != null) {
                     this.font.drawString(x + var50 + 40, y + alignY, Text.getText("playSelectedObjectUpgradePanel.chargedDamage"), Text.FontType.BODY, 1);
                  } else {
                     this.font.drawString(x + var50 + 40, y + alignY, Text.getText("playSelectedObjectUpgradePanel.damage"), Text.FontType.BODY, 1);
                  }

                  alignY += 16;
               }
            }

            if (newTowerSubMissile != null) {
               for (MobBase.DamageType d : newTowerSubMissile.getDamageTypesMax().keySet()) {
                  int damageTypeMinDifferencex = newTowerSubMissile.getDamageTypesMin().get(d);
                  if (oldTowerSubMissile != null && oldTowerSubMissile.getDamageTypesMin().containsKey(d)) {
                     damageTypeMinDifferencex -= oldTowerSubMissile.getDamageTypesMin().get(d);
                  }

                  if (oldTowerSubMissile == null && oldTowerMissile.getDamageTypesMin().containsKey(d)) {
                     damageTypeMinDifferencex -= oldTowerMissile.getDamageTypesMin().get(d);
                  }

                  int damageTypeMaxDifferencex = newTowerSubMissile.getDamageTypesMax().get(d);
                  if (oldTowerSubMissile != null && oldTowerSubMissile.getDamageTypesMax().containsKey(d)) {
                     damageTypeMaxDifferencex -= oldTowerSubMissile.getDamageTypesMax().get(d);
                  }

                  if (oldTowerSubMissile == null && oldTowerMissile.getDamageTypesMax().containsKey(d)) {
                     damageTypeMaxDifferencex -= oldTowerMissile.getDamageTypesMax().get(d);
                  }

                  if (damageTypeMinDifferencex > 0 || damageTypeMaxDifferencex > 0) {
                     this.iconAmmo.render(this.g, x + var50 + 20, y + alignY, debug);
                     Text.setVariableText("damageType", d.getText());
                     Text.setVariableText("damageMinimum", damageTypeMinDifferencex);
                     Text.setVariableText("damageMaximum", damageTypeMaxDifferencex);
                     this.font.drawString(x + var50 + 40, y + alignY, Text.getText("playSelectedObjectUpgradePanel.unchargedDamage"), Text.FontType.BODY, 1);
                     alignY += 16;
                  }
               }
            }
         }

         if (nextUpgradeFlags.getAmmoMax() > 0) {
            int ammoMaxDifference = nextUpgradeFlags.getAmmoMax() - selectedObjectFlags.getAmmoMax();
            if (ammoMaxDifference > 0) {
               this.iconAmmo.render(this.g, x + var50 + 20, y + alignY, debug);
               Text.setVariableText("amount", ammoMaxDifference);
               this.font.drawString(x + var50 + 40, y + alignY - 1, Text.getText("playSelectedObjectUpgradePanel.ammo"), Text.FontType.BODY, 1);
               alignY += 16;
            }
         }

         if (nextUpgradeTowerFlags.getFireEnergyCost() > 0) {
            int ammoMaxDifference = nextUpgradeTowerFlags.getFireEnergyCost();
            if (selectedObjectFlags.getTowerType() != null) {
               ammoMaxDifference -= selectedUpgradeTowerFlags.getFireEnergyCost();
            }

            if (ammoMaxDifference > 0) {
               this.iconEnergy.render(this.g, x + var50 + 20, y + alignY, debug);
               Text.setVariableText("amount", ammoMaxDifference);
               this.font.drawString(x + var50 + 40, y + alignY - 1, Text.getText("playSelectedObjectUpgradePanel.energyPerShot"), Text.FontType.BODY, 1);
               alignY += 16;
            }
         }

         int reloadRateDifference = nextUpgradeTowerFlags.getFullReloadRate();
         if (selectedObjectFlags.getTowerType() != null) {
            reloadRateDifference -= selectedUpgradeTowerFlags.getFullReloadRate();
         }

         if (reloadRateDifference > 0) {
            this.iconAmmo.render(this.g, x + var50 + 20, y + alignY, debug);
            Text.setVariableText("amount", reloadRateDifference);
            this.font.drawString(x + var50 + 40, y + alignY - 1, Text.getText("playSelectedObjectUpgradePanel.reloadTime"), Text.FontType.BODY, 1);
            alignY += 16;
         }

         int distanceDifference = nextUpgradeTowerFlags.getDistance();
         if (selectedObjectFlags.getTowerType() != null) {
            distanceDifference -= selectedUpgradeTowerFlags.getDistance();
         }

         if (distanceDifference > 0) {
            this.iconAmmo.render(this.g, x + var50 + 20, y + alignY, debug);
            Text.setVariableText("amount", distanceDifference);
            this.font.drawString(x + var50 + 40, y + alignY - 1, Text.getText("playSelectedObjectUpgradePanel.firingRange"), Text.FontType.BODY, 1);
            alignY += 24;
         }
      }

      if (nextUpgradeFlags.isCombobulator()) {
         this.font.drawString(x + var50 + 24, y + alignY, Text.getText("playSelectedObjectUpgradePanel.canSpawnGolems"), Text.FontType.BODY, 2);
         alignY += 16;

         for (MobBase.MobType typexxx : nextUpgradeFlags.getCombobulatorSpawnTypes()) {
            if (!selectedObjectFlags.getCombobulatorSpawnTypes().contains(typexxx)) {
               Text.setVariableText("mobName", typexxx.getNamePlural());
               this.font.drawString(x + var50 + 20, y + alignY, Text.getText("playSelectedObjectUpgradePanel.spawnsGolems"), Text.FontType.BODY, 1);
               alignY += 12;
            }
         }

         int spawnsMaxDifference = nextUpgradeFlags.getCombobulatorMax() - selectedObjectFlags.getCombobulatorMax();
         if (spawnsMaxDifference > 0) {
            Text.setVariableText("amount", spawnsMaxDifference);
            this.font.drawString(x + var50 + 20, y + alignY, Text.getText("playSelectedObjectUpgradePanel.maximumGolems"), Text.FontType.BODY, 1);
            alignY += 12;
         }

         if (nextUpgradeFlags.isCombobulator()) {
            int spawnsEnergyRequiredDifference = nextUpgradeFlags.getCombobulatorEnergyRequired() - selectedObjectFlags.getCombobulatorEnergyRequired();
            if (spawnsEnergyRequiredDifference > 0) {
               Text.setVariableText("resourceName", nextUpgradeFlags.getCombobulatorResourceType().getTemplate().getNamePlural());
               Text.setVariableText("amount", spawnsEnergyRequiredDifference);
               this.font
                  .drawString(
                     x + var50 + 20, y + alignY, Text.getText("playSelectedObjectUpgradePanel.combobulatorNeedsResourcesAndEnergy"), Text.FontType.BODY, 1
                  );
            }
         }

         alignY += 24;
      }

      alignY += 12;
      int rangeDifference = nextUpgradeFlags.getBaseRange() - selectedObjectFlags.getBaseRange();
      if (rangeDifference > 0) {
         this.iconRange.render(this.g, x + var50 + 20, y + alignY, debug);
         Text.setVariableText("amount", rangeDifference);
         this.font.drawString(x + var50 + 40, y + alignY - 1, Text.getText("playSelectedObjectUpgradePanel.rangeGenerated"), Text.FontType.BODY, 1);
         alignY += 16;
      }

      int desirabilityDifference = nextUpgradeFlags.getBaseDesirability() - selectedObjectFlags.getBaseDesirability();
      if (desirabilityDifference > 0) {
         this.iconDesirability.render(this.g, x + var50 + 20, y + alignY, debug);
         Text.setVariableText("amount", desirabilityDifference);
         this.font.drawString(x + var50 + 40, y + alignY - 1, Text.getText("playSelectedObjectUpgradePanel.desirability"), Text.FontType.BODY, 1);
         alignY += 16;
      }

      int occupantsDifference = nextUpgradeFlags.getOccupantsMax() - selectedObjectFlags.getOccupantsMax();
      if (occupantsDifference > 0) {
         if (selectedObjectFlags.getOccupantsJob() == MobJobBase.MobJobType.RANGER) {
            this.iconRangerLodgeOccupants.render(this.g, x + var50 + 20, y + alignY, debug);
            Text.setVariableText("amount", occupantsDifference);
            this.font.drawString(x + var50 + 40, y + alignY - 1, Text.getText("playSelectedObjectUpgradePanel.rangerHousingCapacity"), Text.FontType.BODY, 1);
         } else {
            this.iconHomeOccupants.render(this.g, x + var50 + 20, y + alignY, debug);
            Text.setVariableText("amount", occupantsDifference);
            this.font.drawString(x + var50 + 40, y + alignY - 1, Text.getText("playSelectedObjectUpgradePanel.housingCapacity"), Text.FontType.BODY, 1);
         }

         alignY += 16;
      }

      int workersDifference = nextUpgradeFlags.getWorkersProvided() - selectedObjectFlags.getWorkersProvided();
      if (workersDifference > 0) {
         this.iconWorkers.render(this.g, x + var50 + 20, y + alignY, debug);
         Text.setVariableText("amount", workersDifference);
         Text.setVariableText("mobJobName", selectedObjectFlags.getWorkerJobType().getJob().getMobJobNamePlural());
         this.font.drawString(x + var50 + 40, y + alignY - 1, Text.getText("playSelectedObjectUpgradePanel.workersSlots"), Text.FontType.BODY, 1);
         alignY += 16;
      }

      int buildingsProvidedDifference = nextUpgradeFlags.getBuildingsProvided() - selectedObjectFlags.getBuildingsProvided();
      if (buildingsProvidedDifference > 0) {
         this.iconBuildingSlots.render(this.g, x + var50 + 20, y + alignY, debug);
         Text.setVariableText("amount", buildingsProvidedDifference);
         this.font.drawString(x + var50 + 40, y + alignY - 1, Text.getText("playSelectedObjectUpgradePanel.buildingSlotsProvided"), Text.FontType.BODY, 1);
         alignY += 16;
      }

      if (nextUpgradeFlags.canAcceptEssence() && !selectedObjectFlags.canAcceptEssence()) {
         this.iconEnergy.render(this.g, x + var50 + 20, y + alignY, debug);
         this.font.drawString(x + var50 + 40, y + alignY - 1, Text.getText("playSelectedObjectUpgradePanel.acceptsEssence"), Text.FontType.BODY, 1);
         alignY += 16;
      }

      if (nextUpgradeFlags.canCollectEssence() && !selectedObjectFlags.canCollectEssence()) {
         this.font.drawString(x + var50 + 40, y + alignY - 1, Text.getText("playSelectedObjectUpgradePanel.canCollectRawEssence"), Text.FontType.BODY, 1);
         alignY += 16;
         if (nextUpgradeFlags.canBuildingStore(ResourceModule.ResourceType.CRYLITHIUM)) {
            this.font.drawString(x + var50 + 40, y + alignY - 1, Text.getText("playSelectedObjectUpgradePanel.consumesCrylithium"), Text.FontType.BODY, 1);
            alignY += 16;
         }
      }

      if (nextUpgradeFlags.getWorkerJobType().getJob().canWorkerBuildRoad() && !selectedObjectFlags.getWorkerJobType().getJob().canWorkerBuildRoad()) {
         this.font.drawString(x + var50 + 40, y + alignY - 1, Text.getText("playSelectedObjectUpgradePanel.canBuildRoads"), Text.FontType.BODY, 1);
         alignY += 16;
      }
   }

   public int getPanelWidth() {
      return this.panel.getWidth();
   }

   public int getPanelHeight() {
      return this.panel.getHeight();
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
