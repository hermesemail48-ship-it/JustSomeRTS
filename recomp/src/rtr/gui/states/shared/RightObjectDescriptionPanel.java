package rtr.gui.states.shared;

import java.util.ArrayList;
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
import rtr.gui.states.MapEditorGUIController;
import rtr.gui.states.MapEditorGUIData;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.SharedGUIPanelBase;
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
import rtr.system.ScaleControl;

public class RightObjectDescriptionPanel extends SharedGUIPanelBase {
   private Image panel;
   private GUIIcon iconEnergy;
   private EnumMap<ResourceModule.ResourceType, GUIIcon> icons = new EnumMap<>(ResourceModule.ResourceType.class);
   private GUIIcon iconHomeOccupants;
   private GUIIcon iconRangerLodgeOccupants;
   private GUIIcon iconWorkers;
   private GUIIcon iconBuildingSlots;
   private GUIIcon iconRange;
   private GUIIcon iconDesirability;
   private GUIIcon iconProvidedMigrationsPerDay;
   private GUIIcon iconAmmo;
   private GUIIcon objectDescriptionStorage;
   private ObjectFlags highlightedObjectFlags;

   public RightObjectDescriptionPanel(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.init();
   }

   public RightObjectDescriptionPanel(MapEditorGUIController controller, MapEditorGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.init();
   }

   private void init() throws SlickException {
      this.panel = ImageLoader.getImage("res/GUI/gameState/objectDescriptionPanel.png");
      this.iconEnergy = new GUIIcon("iconEnergy");
      this.guiButtons.add(this.iconEnergy);

      ResourceModule.ResourceType[] var4;
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
      this.iconProvidedMigrationsPerDay = new GUIIcon("iconMigration");
      this.guiButtons.add(this.iconProvidedMigrationsPerDay);
      this.objectDescriptionStorage = new GUIIcon("iconChest");
      this.guiButtons.add(this.objectDescriptionStorage);
      this.iconAmmo = new GUIIcon("iconAmmo");
      this.guiButtons.add(this.iconAmmo);
   }

   public void render(boolean debug) throws SlickException {
      this.x = ScaleControl.getInterfaceWidth() - 464;
      this.y = ScaleControl.getInterfaceCenterY() - 265;
      if (this.y < 134) {
         this.y = 134;
      }

      this.panel.draw(this.x, this.y);
      if (this.highlightedObjectFlags.getName().length() > 32) {
         this.font.drawString(this.x + 135, this.y + 8, "$CYA1" + this.highlightedObjectFlags.getName(), Text.FontType.HEADER, 0, true);
      } else if (this.highlightedObjectFlags.getName().length() > 24) {
         this.font.drawString(this.x + 135, this.y + 6, "$CYA1" + this.highlightedObjectFlags.getName(), Text.FontType.HEADER, 1, true);
      } else {
         this.font.drawString(this.x + 135, this.y + 4, "$CYA1" + this.highlightedObjectFlags.getName(), Text.FontType.HEADER, 2, true);
      }

      EnumMap<ResourceModule.ResourceType, String> manufacuringBuildingNames = new EnumMap<>(ResourceModule.ResourceType.class);
      HashMap<MapTilesLoader.TileSet, HashMap<ObjectBase.ObjectSubType, ObjectFlags>> objectFlagList = this.object.getObjectFlagFactory().getObjectFlagsList();

      ResourceModule.ResourceType[] buildingDescription;
      for (ResourceModule.ResourceType type : buildingDescription = ResourceModule.ResourceType.values()) {
         for (HashMap<ObjectBase.ObjectSubType, ObjectFlags> o : objectFlagList.values()) {
            if (o.get(ObjectBase.ObjectSubType.BUILT).getWorkerJobType().getJob().canWorkerRefine(type)
               && o.get(ObjectBase.ObjectSubType.BUILT).getCanBuildingStore().contains(type)) {
               manufacuringBuildingNames.put(type, o.get(ObjectBase.ObjectSubType.BUILT).getBaseName());
            }
         }
      }

      int alignX = 0;
      int alignY = 27;
      int count = 0;
      String buildingDescriptionx = Text.wrapString(this.highlightedObjectFlags.getDescription(), Text.FontType.BODY, 1, 230);
      this.font.drawString(this.x + alignX + 20, this.y + alignY, buildingDescriptionx, Text.FontType.BODY, 1);
      alignY += 81;
      this.font
         .drawString(
            this.x + alignX + 24, this.y + alignY, Text.getText("playRightObjectDescriptionPanel.header.constructionRequirements"), Text.FontType.BODY, 2
         );
      alignY += 18;
      boolean foundRefined = false;

      ResourceModule.ResourceType[] towerAltMissileType;
      for (ResourceModule.ResourceType type : towerAltMissileType = ResourceModule.ResourceType.values()) {
         if (this.highlightedObjectFlags.getResourceValueBaseCount(type) > 0
            && type.getTemplate().getBaseResources().size() > 0
            && manufacuringBuildingNames.containsKey(type)) {
            Text.setVariableText("buildingName", manufacuringBuildingNames.get(type));
            Text.setVariableText("resourceName", type.getTemplate().getNamePlural());
            this.font
               .drawString(
                  this.x + alignX + 24, this.y + alignY, Text.getText("playRightObjectDescriptionPanel.constructionRequirements"), Text.FontType.BODY, 1
               );
            foundRefined = true;
            alignY += 12;
         }
      }

      if (foundRefined) {
         alignY += 4;
      }

      for (ResourceModule.ResourceType typex : towerAltMissileType = ResourceModule.ResourceType.values()) {
         if (this.highlightedObjectFlags.getResourceValueBaseCount(typex) > 0) {
            this.icons.get(typex).render(this.g, this.x + alignX + 20, this.y + alignY, debug);
            this.font
               .drawString(
                  this.x + alignX + 39,
                  this.y + alignY,
                  "$RED1" + this.highlightedObjectFlags.getResourceValueBaseCount(typex) + " " + typex.getTemplate().getNamePlural(),
                  Text.FontType.BODY,
                  1
               );
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

      int var18 = 0;
      alignY += 8;
      if (this.highlightedObjectFlags.getRequiredSupportBuildings().size() > 0) {
         this.font
            .drawString(this.x + var18 + 24, this.y + alignY, Text.getText("playRightObjectDescriptionPanel.header.requiredSupport"), Text.FontType.BODY, 2);
         alignY += 16;

         for (MapTilesLoader.TileSet[] a : this.highlightedObjectFlags.getRequiredSupportBuildings()) {
            String out = "";

            for (MapTilesLoader.TileSet t : a) {
               if (out.equals("")) {
                  out = "$RED0" + this.object.getObjectFlagFactory().getObjectFlags(t, ObjectBase.ObjectSubType.BUILT).getBaseName();
               } else {
                  out = out
                     + Text.getText("playRightObjectDescriptionPanel.supportConjuntion")
                     + this.object.getObjectFlagFactory().getObjectFlags(t, ObjectBase.ObjectSubType.BUILT).getBaseName();
               }
            }

            this.font.drawString(this.x + var18 + 40, this.y + alignY, out, Text.FontType.BODY, 1);
            alignY += 12;
         }

         alignY += 12;
      }

      if (this.highlightedObjectFlags.getRecommendedSupportBuildings().size() > 0) {
         this.font
            .drawString(this.x + var18 + 24, this.y + alignY, Text.getText("playRightObjectDescriptionPanel.header.recommendedSupport"), Text.FontType.BODY, 2);
         alignY += 16;

         for (MapTilesLoader.TileSet[] a : this.highlightedObjectFlags.getRecommendedSupportBuildings()) {
            String out = "";

            for (MapTilesLoader.TileSet tx : a) {
               if (out.equals("")) {
                  out = "$GRE0" + this.object.getObjectFlagFactory().getObjectFlags(tx, ObjectBase.ObjectSubType.BUILT).getBaseName();
               } else {
                  out = out
                     + Text.getText("playRightObjectDescriptionPanel.supportConjuntion")
                     + this.object.getObjectFlagFactory().getObjectFlags(tx, ObjectBase.ObjectSubType.BUILT).getBaseName();
               }
            }

            this.font.drawString(this.x + var18 + 40, this.y + alignY, out, Text.FontType.BODY, 1);
            alignY += 12;
         }

         alignY += 12;
      }

      int maximumStorage = 0;

      for (ResourceModule.ResourceType txx : this.highlightedObjectFlags.getResourceStorageMax().keySet()) {
         maximumStorage += this.highlightedObjectFlags.getResourceStorageMax().get(txx);
      }

      if (maximumStorage > 0 || this.highlightedObjectFlags.getEnergyMax() > 0) {
         count = 0;
         this.font.drawString(this.x + var18 + 24, this.y + alignY, Text.getText("playRightObjectDescriptionPanel.header.canStore"), Text.FontType.BODY, 2);
         alignY += 20;
         if (maximumStorage > 0) {
            this.objectDescriptionStorage.render(this.g, this.x + var18 + 20, this.y + alignY, debug);
            Text.setVariableText("amount", maximumStorage);
            this.font
               .drawString(this.x + var18 + 40, this.y + alignY - 1, Text.getText("playRightObjectDescriptionPanel.maximumStorage"), Text.FontType.BODY, 1);
            alignY += 10;
         }

         if (this.highlightedObjectFlags.getEnergyMax() > 0) {
            this.iconEnergy.render(this.g, this.x + var18 + 20, this.y + alignY, debug);
            Text.setVariableText("amount", this.highlightedObjectFlags.getEnergyMax());
            this.font.drawString(this.x + var18 + 39, this.y + alignY, Text.getText("playRightObjectDescriptionPanel.energy"), Text.FontType.BODY, 1);
            if (count % 2 == 0) {
               var18 += 110;
            } else {
               alignY += 12;
               var18 = 0;
            }

            count++;
         }

         ResourceModule.ResourceType[] var75;
         for (ResourceModule.ResourceType typexx : ResourceModule.ResourceType.values()) {
            if (this.highlightedObjectFlags.canBuildingStore(typexx)) {
               this.icons.get(typexx).render(this.g, this.x + var18 + 20, this.y + alignY, debug);
               String stringOut = this.highlightedObjectFlags.getResourceStorageMax().get(typexx) + " $YEL1" + typexx.getTemplate().getNamePlural();
               this.font.drawString(this.x + var18 + 39, this.y + 4 + alignY, stringOut, Text.FontType.BODY, 0);
               if (count % 2 == 0 && stringOut.length() < 25) {
                  var18 += 110;
               } else {
                  alignY += 10;
                  var18 = 0;
               }

               if (count > 16) {
                  this.font
                     .drawString(
                        this.x + var18 + 39, this.y + 4 + alignY, Text.getText("playRightObjectDescriptionPanel.canStoreMoreOverflow"), Text.FontType.BODY, 0
                     );
                  break;
               }

               count++;
            }
         }

         var18 = 0;
         if (count % 2 == 0) {
            alignY += 12;
         } else {
            alignY += 24;
         }
      }

      if (this.highlightedObjectFlags.getWorkerJobType().getJob().canWorkerRefine()) {
         ArrayList<ResourceModule.ResourceType> validTypes = new ArrayList<>();

         for (ResourceModule.ResourceType typexxx : this.highlightedObjectFlags.getWorkerJobType().getJob().getCanWorkerRefine()) {
            if (this.highlightedObjectFlags.getCanBuildingStore().contains(typexxx)) {
               validTypes.add(typexxx);
            }
         }

         if (validTypes.size() > 0) {
            count = 0;
            this.font
               .drawString(
                  this.x + var18 + 24, this.y + alignY, Text.getText("playRightObjectDescriptionPanel.header.canRefineOrManufacture"), Text.FontType.BODY, 2
               );
            alignY += 16;

            for (ResourceModule.ResourceType typexxxx : validTypes) {
               this.icons.get(typexxxx).render(this.g, this.x + var18 + 20, this.y + alignY, debug);
               this.font.drawString(this.x + var18 + 39, this.y + 4 + alignY, "$YEL1" + typexxxx.getTemplate().getNamePlural(), Text.FontType.BODY, 0);
               if (count % 2 == 0) {
                  var18 += 110;
               } else {
                  alignY += 12;
                  var18 = 0;
               }

               count++;
            }

            var18 = 0;
            if (count % 2 == 0) {
               alignY += 12;
            } else {
               alignY += 24;
            }
         }
      }

      if (this.highlightedObjectFlags.getWorkerJobType().getJob().canWorkerSlaughter()) {
         count = 0;
         this.font.drawString(this.x + var18 + 24, this.y + alignY, Text.getText("playRightObjectDescriptionPanel.header.canSlaughter"), Text.FontType.BODY, 2);
         alignY += 16;

         for (ResourceModule.ResourceType typexxxx : this.highlightedObjectFlags.getWorkerJobType().getJob().getCanWorkerSlaughter()) {
            this.icons.get(typexxxx).render(this.g, this.x + var18 + 20, this.y + alignY, debug);
            this.font.drawString(this.x + var18 + 39, this.y + 4 + alignY, "$YEL1" + typexxxx.getTemplate().getNamePlural(), Text.FontType.BODY, 0);
            if (count % 2 == 0) {
               var18 += 110;
            } else {
               alignY += 12;
               var18 = 0;
            }

            count++;
         }

         var18 = 0;
         if (count % 2 == 0) {
            alignY += 12;
         } else {
            alignY += 24;
         }
      }

      if (this.highlightedObjectFlags.getTowerType() != null) {
         TowerHeadFlags highlightedTowerFlags = this.object
            .getTowerHeadFlagFactory()
            .getTowerFlags(this.highlightedObjectFlags.getTowerType(), this.highlightedObjectFlags.getTowerUpgradeStage());
         this.font
            .drawString(this.x + var18 + 24, this.y + alignY, Text.getText("playRightObjectDescriptionPanel.header.defensiveTower"), Text.FontType.BODY, 2);
         alignY += 16;
         MissileModule.MissileType towerMissileType = highlightedTowerFlags.getMissileType();
         MissileModule.MissileType towerAltMissileTypex = highlightedTowerFlags.getAltMissileType();
         MissileBase towerMissle = null;
         if (towerMissileType != null) {
            towerMissle = this.missile.getMissileFactory().getMissileInstance(towerMissileType);
         }

         MissileBase towerSubMissile = null;
         if (towerAltMissileTypex != null) {
            towerSubMissile = this.missile.getMissileFactory().getMissileInstance(towerAltMissileTypex);
         }

         if (highlightedTowerFlags.getTowerType() == TowerBase.TowerType.STATIC) {
            this.iconAmmo.render(this.g, this.x + var18 + 20, this.y + alignY, debug);
            this.font
               .drawString(
                  this.x + var18 + 40, this.y + alignY, Text.getText("playRightObjectDescriptionPanel.staticTowerDamageOverride"), Text.FontType.BODY, 1
               );
            alignY += 16;
         }

         if (towerMissle != null && towerMissle.getDamageTypesMax().size() > 0) {
            for (MobBase.DamageType d : towerMissle.getDamageTypesMax().keySet()) {
               this.iconAmmo.render(this.g, this.x + var18 + 20, this.y + alignY, debug);
               Text.setVariableText("damageType", d.getText());
               Text.setVariableText("damageMinimum", towerMissle.getDamageTypesMin().get(d));
               Text.setVariableText("damageMaximum", towerMissle.getDamageTypesMax().get(d));
               if (towerSubMissile != null) {
                  this.font
                     .drawString(this.x + var18 + 40, this.y + alignY, Text.getText("playRightObjectDescriptionPanel.chargedDamage"), Text.FontType.BODY, 1);
               } else {
                  this.font.drawString(this.x + var18 + 40, this.y + alignY, Text.getText("playRightObjectDescriptionPanel.damage"), Text.FontType.BODY, 1);
               }

               alignY += 16;
            }

            if (towerSubMissile != null) {
               for (MobBase.DamageType d : towerSubMissile.getDamageTypesMax().keySet()) {
                  this.iconAmmo.render(this.g, this.x + var18 + 20, this.y + alignY, debug);
                  Text.setVariableText("damageType", d.getText());
                  Text.setVariableText("damageMinimum", towerSubMissile.getDamageTypesMin().get(d));
                  Text.setVariableText("damageMaximum", towerSubMissile.getDamageTypesMax().get(d));
                  this.font
                     .drawString(this.x + var18 + 40, this.y + alignY, Text.getText("playRightObjectDescriptionPanel.unchargedDamage"), Text.FontType.BODY, 1);
                  alignY += 16;
               }
            }
         }

         this.iconAmmo.render(this.g, this.x + var18 + 20, this.y + alignY, debug);
         Text.setVariableText("ammoName", highlightedTowerFlags.getAmmoTypeName());
         this.font.drawString(this.x + var18 + 40, this.y + alignY - 1, Text.getText("playRightObjectDescriptionPanel.fires"), Text.FontType.BODY, 1);
         alignY += 16;
         if (this.highlightedObjectFlags.getAmmoMax() > 0) {
            this.iconAmmo.render(this.g, this.x + var18 + 20, this.y + alignY, debug);
            Text.setVariableText("amount", this.highlightedObjectFlags.getAmmoMax());
            this.font.drawString(this.x + var18 + 40, this.y + alignY - 1, Text.getText("playRightObjectDescriptionPanel.ammo"), Text.FontType.BODY, 1);
            alignY += 16;
         }

         if (highlightedTowerFlags.getFireEnergyCost() > 0) {
            this.iconEnergy.render(this.g, this.x + var18 + 20, this.y + alignY, debug);
            Text.setVariableText("amount", highlightedTowerFlags.getFireEnergyCost());
            this.font
               .drawString(this.x + var18 + 40, this.y + alignY - 1, Text.getText("playRightObjectDescriptionPanel.energyPerShot"), Text.FontType.BODY, 1);
            alignY += 16;
         }

         this.iconAmmo.render(this.g, this.x + var18 + 20, this.y + alignY, debug);
         Text.setVariableText("amount", highlightedTowerFlags.getFullReloadRate());
         this.font.drawString(this.x + var18 + 40, this.y + alignY - 1, Text.getText("playRightObjectDescriptionPanel.reloadTime"), Text.FontType.BODY, 1);
         alignY += 16;
         this.iconAmmo.render(this.g, this.x + var18 + 20, this.y + alignY, debug);
         Text.setVariableText("amount", highlightedTowerFlags.getDistance());
         this.font.drawString(this.x + var18 + 40, this.y + alignY - 1, Text.getText("playRightObjectDescriptionPanel.firingRange"), Text.FontType.BODY, 1);
         alignY += 24;
      }

      if (this.highlightedObjectFlags.isCombobulator()) {
         this.font.drawString(this.x + var18 + 24, this.y + alignY, Text.getText("playRightObjectDescriptionPanel.canSpawnGolems"), Text.FontType.BODY, 2);
         alignY += 16;

         for (MobBase.MobType typexxxx : this.highlightedObjectFlags.getCombobulatorSpawnTypes()) {
            Text.setVariableText("mobName", typexxxx.getNamePlural());
            this.font.drawString(this.x + var18 + 20, this.y + alignY, Text.getText("playRightObjectDescriptionPanel.spawnsGolems"), Text.FontType.BODY, 1);
            alignY += 12;
         }

         Text.setVariableText("amount", this.highlightedObjectFlags.getCombobulatorMax());
         this.font.drawString(this.x + var18 + 20, this.y + alignY, Text.getText("playRightObjectDescriptionPanel.maximumGolems"), Text.FontType.BODY, 1);
         alignY += 12;
         if (this.highlightedObjectFlags.isCombobulator()) {
            Text.setVariableText("resourceName", this.highlightedObjectFlags.getCombobulatorResourceType().getTemplate().getNamePlural());
            Text.setVariableText("amount", this.highlightedObjectFlags.getCombobulatorEnergyRequired());
            this.font
               .drawString(
                  this.x + var18 + 20,
                  this.y + alignY,
                  Text.getText("playRightObjectDescriptionPanel.combobulatorNeedsResourcesAndEnergy"),
                  Text.FontType.BODY,
                  1
               );
         }

         alignY += 24;
      }

      alignY += 12;
      if (this.highlightedObjectFlags.getBaseRange() > 0) {
         this.iconRange.render(this.g, this.x + var18 + 20, this.y + alignY, debug);
         Text.setVariableText("amount", this.highlightedObjectFlags.getBaseRange());
         this.font.drawString(this.x + var18 + 40, this.y + alignY - 1, Text.getText("playRightObjectDescriptionPanel.rangeGenerated"), Text.FontType.BODY, 1);
         alignY += 16;
      }

      this.iconDesirability.render(this.g, this.x + var18 + 20, this.y + alignY, debug);
      Text.setVariableText("amount", this.highlightedObjectFlags.getBaseDesirability());
      this.font.drawString(this.x + var18 + 40, this.y + alignY - 1, Text.getText("playRightObjectDescriptionPanel.desirability"), Text.FontType.BODY, 1);
      alignY += 16;
      if (this.highlightedObjectFlags.getMigrationsPerDayProvided() > 0) {
         this.iconProvidedMigrationsPerDay.render(this.g, this.x + var18 + 20, this.y + alignY, debug);
         Text.setVariableText("amount", this.highlightedObjectFlags.getMigrationsPerDayProvided());
         this.font
            .drawString(
               this.x + var18 + 40, this.y + alignY - 1, Text.getText("playRightObjectDescriptionPanel.providedMigrationsPerDay"), Text.FontType.BODY, 1
            );
         alignY += 16;
      }

      if (this.highlightedObjectFlags.getOccupantsMax() > 0) {
         if (this.highlightedObjectFlags.getOccupantsJob() == MobJobBase.MobJobType.RANGER) {
            this.iconRangerLodgeOccupants.render(this.g, this.x + var18 + 20, this.y + alignY, debug);
            Text.setVariableText("amount", this.highlightedObjectFlags.getOccupantsMax());
            this.font
               .drawString(
                  this.x + var18 + 40, this.y + alignY - 1, Text.getText("playRightObjectDescriptionPanel.rangerHousingCapacity"), Text.FontType.BODY, 1
               );
         } else {
            this.iconHomeOccupants.render(this.g, this.x + var18 + 20, this.y + alignY, debug);
            Text.setVariableText("amount", this.highlightedObjectFlags.getOccupantsMax());
            this.font
               .drawString(this.x + var18 + 40, this.y + alignY - 1, Text.getText("playRightObjectDescriptionPanel.housingCapacity"), Text.FontType.BODY, 1);
         }

         alignY += 16;
      }

      if (this.highlightedObjectFlags.getWorkersProvided() > 0) {
         this.iconWorkers.render(this.g, this.x + var18 + 20, this.y + alignY, debug);
         Text.setVariableText("amount", this.highlightedObjectFlags.getWorkersProvided());
         Text.setVariableText("mobJobName", this.highlightedObjectFlags.getWorkerJobType().getJob().getMobJobNamePlural());
         this.font.drawString(this.x + var18 + 40, this.y + alignY - 1, Text.getText("playRightObjectDescriptionPanel.workersSlots"), Text.FontType.BODY, 1);
         alignY += 16;
      }

      if (this.highlightedObjectFlags.getBuildingsProvided() > 0) {
         this.iconBuildingSlots.render(this.g, this.x + var18 + 20, this.y + alignY, debug);
         Text.setVariableText("amount", this.highlightedObjectFlags.getBuildingsProvided());
         this.font
            .drawString(this.x + var18 + 40, this.y + alignY - 1, Text.getText("playRightObjectDescriptionPanel.buildingSlotsProvided"), Text.FontType.BODY, 1);
         alignY += 16;
      }

      if (this.highlightedObjectFlags.canAcceptEssence()) {
         this.iconEnergy.render(this.g, this.x + var18 + 20, this.y + alignY, debug);
         this.font.drawString(this.x + var18 + 40, this.y + alignY - 1, Text.getText("playRightObjectDescriptionPanel.acceptsEssence"), Text.FontType.BODY, 1);
         alignY += 16;
      }

      if (this.highlightedObjectFlags.canCollectEssence()) {
         this.font
            .drawString(this.x + var18 + 40, this.y + alignY - 1, Text.getText("playRightObjectDescriptionPanel.canCollectRawEssence"), Text.FontType.BODY, 1);
         alignY += 16;
      }

      if (this.highlightedObjectFlags.canDeliverEssence()) {
         this.font
            .drawString(this.x + var18 + 40, this.y + alignY - 1, Text.getText("playRightObjectDescriptionPanel.canPowerStructures"), Text.FontType.BODY, 1);
         alignY += 16;
      }

      if (this.highlightedObjectFlags.canDeliverEssenceToCollectors()) {
         ObjectFlags collector = this.object.getObjectFlagFactory().getObjectFlags(MapTilesLoader.TileSet.ESSENCE_COLLECTOR, ObjectBase.ObjectSubType.BUILT);
         Text.setVariableText("buildingName", collector.getBaseName());
         this.font
            .drawString(
               this.x + var18 + 40, this.y + alignY - 1, Text.getText("playRightObjectDescriptionPanel.canDeliverEssenceToCollectors"), Text.FontType.BODY, 1
            );
         alignY += 16;
      }

      if (this.highlightedObjectFlags.getWorkerJobType().getJob().canWorkerBuildRoad()) {
         this.font.drawString(this.x + var18 + 40, this.y + alignY - 1, Text.getText("playRightObjectDescriptionPanel.canBuildRoads"), Text.FontType.BODY, 1);
         alignY += 16;
      }
   }

   @Override
   public void update() {
      String intersectingButton = null;
      boolean intersectsMask = false;
      if (this.playStateController != null) {
         intersectingButton = this.playStateController.getRightPanel().getIntersects();
         intersectsMask = this.playStateController.getRightPanel().intersectsGUIMask();
      }

      if (this.mapEditorController != null) {
         intersectingButton = this.mapEditorController.getRightPanel().getIntersects();
         intersectsMask = this.mapEditorController.getRightPanel().intersectsGUIMask();
      }

      if (intersectingButton == null || !intersectsMask) {
         this.resetHighlightedObject();
      } else if (intersectingButton.equals("panelAncillary")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.ANCILLARY);
      } else if (intersectingButton.equals("panelClinic")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.CLINIC);
      } else if (intersectingButton.equals("panelCourierStation")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.COURIER_STATION);
      } else if (intersectingButton.equals("panelMaintenanceBuilding")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.MAINTENANCE_BUILDING);
      } else if (intersectingButton.equals("panelMarketplace")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.MARKETPLACE);
      } else if (intersectingButton.equals("panelMigrationWayStation")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.MIGRATION_WAY_STATION);
      } else if (intersectingButton.equals("panelRangerLodge")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.RANGER_LODGE);
      } else if (intersectingButton.equals("panelOutpost")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.OUTPOST);
      } else if (intersectingButton.equals("panelCrystalGolemCombobulator")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.CRYSTAL_GOLEM_COMBOBULATOR);
      } else if (intersectingButton.equals("panelStoneGolemCombobulator")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.STONE_GOLEM_COMBOBULATOR);
      } else if (intersectingButton.equals("panelWoodGolemCombobulator")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.WOOD_GOLEM_COMBOBULATOR);
      } else if (intersectingButton.equals("panelAttractTower")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.ATTRACT_TOWER);
      } else if (intersectingButton.equals("panelBanishTower")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.BANISH_TOWER);
      } else if (intersectingButton.equals("panelBowTower")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.BOW_TOWER);
      } else if (intersectingButton.equals("panelBallistaTower")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.BALLISTA_TOWER);
      } else if (intersectingButton.equals("panelBulletTower")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.BULLET_TOWER);
      } else if (intersectingButton.equals("panelElementalBoltTower")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.ELEMENTAL_BOLT_TOWER);
      } else if (intersectingButton.equals("panelPhantomDartTower")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.PHANTOM_DART_TOWER);
      } else if (intersectingButton.equals("panelSlingTower")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.SLING_TOWER);
      } else if (intersectingButton.equals("panelSprayTower")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.SPRAY_TOWER);
      } else if (intersectingButton.equals("panelStaticTower")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.STATIC_TOWER);
      } else if (intersectingButton.equals("panelLightningRod")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.LIGHTNING_ROD);
      } else if (intersectingButton.equals("panelRecombobulatorTower")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.RECOMBOBULATOR_TOWER);
      } else if (intersectingButton.equals("panelAnimalPen")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.ANIMAL_PEN);
      } else if (intersectingButton.equals("panelBottler")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.BOTTLER);
      } else if (intersectingButton.equals("panelCluckerCoop")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.CLUCKER_COOP);
      } else if (intersectingButton.equals("panelFarm")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.FARM);
      } else if (intersectingButton.equals("panelSmallFountain")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.SMALL_FOUNTAIN);
      } else if (intersectingButton.equals("panelLargeFountain")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.LARGE_FOUNTAIN);
      } else if (intersectingButton.equals("panelKitchen")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.KITCHEN);
      } else if (intersectingButton.equals("panelRainCatcher")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.RAIN_CATCHER);
      } else if (intersectingButton.equals("panelWaterPurifier")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.WATER_PURIFIER);
      } else if (intersectingButton.equals("panelWell")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.WELL);
      } else if (intersectingButton.equals("panelCrystalHarvestry")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.CRYSTAL_HARVESTRY);
      } else if (intersectingButton.equals("panelLumberShack")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.LUMBER_SHACK);
      } else if (intersectingButton.equals("panelMiningFacility")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.MINING_FACILITY);
      } else if (intersectingButton.equals("panelDoggoHouse")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.DOGGO_HOUSE);
      } else if (intersectingButton.equals("panelHousing")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.HOUSING);
      } else if (intersectingButton.equals("panelCrylithiumFirePit")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.CRYLITHIUM_FIRE_PIT);
      } else if (intersectingButton.equals("panelLargeCrylithiumFirePit")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.LARGE_CRYLITHIUM_FIRE_PIT);
      } else if (intersectingButton.equals("panelFirePit")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.FIRE_PIT);
      } else if (intersectingButton.equals("panelLargeFirePit")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.LARGE_FIRE_PIT);
      } else if (intersectingButton.equals("panelCrystalMotivator")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.CRYSTAL_MOTIVATOR);
      } else if (intersectingButton.equals("panelCullisGate")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.CULLIS_GATE);
      } else if (intersectingButton.equals("panelEssenceAltar")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.ESSENCE_ALTAR);
      } else if (intersectingButton.equals("panelEssenceCollector")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.ESSENCE_COLLECTOR);
      } else if (intersectingButton.equals("panelReliquary")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.RELIQUARY);
      } else if (intersectingButton.equals("panelArmorsmithy")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.ARMORSMITHY);
      } else if (intersectingButton.equals("panelBowyer")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.BOWYER);
      } else if (intersectingButton.equals("panelForge")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.FORGE);
      } else if (intersectingButton.equals("panelRockTumbler")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.ROCK_TUMBLER);
      } else if (intersectingButton.equals("panelToolsmithy")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.TOOLSMITHY);
      } else if (intersectingButton.equals("panelWayMakerShack")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.WAY_MAKER_SHACK);
      } else if (intersectingButton.equals("panelCrystillery")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.CRYSTILLERY);
      } else if (intersectingButton.equals("panelStoneCuttery")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.STONE_CUTTERY);
      } else if (intersectingButton.equals("panelLumberMill")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.LUMBER_MILL);
      } else if (intersectingButton.equals("panelAmmoStorage")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.AMMO_STORAGE);
      } else if (intersectingButton.equals("panelCrystalStorage")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.CRYSTAL_STORAGE);
      } else if (intersectingButton.equals("panelEquipmentStorage")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.EQUIPMENT_STORAGE);
      } else if (intersectingButton.equals("panelFoodStorage")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.FOOD_STORAGE);
      } else if (intersectingButton.equals("panelGoldStorage")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.GOLD_STORAGE);
      } else if (intersectingButton.equals("panelKeyShack")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.KEY_SHACK);
      } else if (intersectingButton.equals("panelMineralStorage")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.MINERAL_STORAGE);
      } else if (intersectingButton.equals("panelMiscellaneousStorage")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.MISCELLANEOUS_STORAGE);
      } else if (intersectingButton.equals("panelRockStorage")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.ROCK_STORAGE);
      } else if (intersectingButton.equals("panelWoodStorage")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.WOOD_STORAGE);
      } else if (intersectingButton.equals("panelBurner")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.BURNER);
      } else if (intersectingButton.equals("panelCubeEGolemCombobulator")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.CUBE_E_GOLEM_COMBOBULATOR);
      } else if (intersectingButton.equals("panelLandfill")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.LANDFILL);
      } else if (intersectingButton.equals("panelProcessor")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.PROCESSOR);
      } else if (intersectingButton.equals("panelTrashCan")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.TRASH_CAN);
      } else if (intersectingButton.equals("panelTrashyCubePile")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.TRASHY_CUBE_PILE);
      } else if (intersectingButton.equals("panelWoodFenceGateNS")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.WOOD_FENCE_GATE_NS);
      } else if (intersectingButton.equals("panelWoodFenceGateWE")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.WOOD_FENCE_GATE_WE);
      } else if (intersectingButton.equals("panelCrylithiumWallGateNS")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.CRYLITHIUM_WALL_GATE_NS);
      } else if (intersectingButton.equals("panelCrylithiumWallGateWE")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.CRYLITHIUM_WALL_GATE_WE);
      } else if (intersectingButton.equals("panelStoneWallGateNS")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.STONE_WALL_GATE_NS);
      } else if (intersectingButton.equals("panelStoneWallGateWE")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.STONE_WALL_GATE_WE);
      } else if (intersectingButton.equals("panelCrylithiumWall")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.CRYLITHIUM_WALL);
      } else if (intersectingButton.equals("panelCrylithiumCurtainWall")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.CRYLITHIUM_CURTAIN_WALL);
      } else if (intersectingButton.equals("panelTrashyCubeWall")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.TRASHY_CUBE_WALL);
      } else if (intersectingButton.equals("panelWoodFence")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.WOOD_FENCE);
      } else if (intersectingButton.equals("panelStoneWall")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.STONE_WALL);
      } else if (intersectingButton.equals("panelCurtainWall")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.CURTAIN_WALL);
      } else if (intersectingButton.equals("panelCamp")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.CASTLE_1);
      } else if (intersectingButton.equals("panelLootBox")) {
         this.setHighlightedObject(MapTilesLoader.TileSet.LOOT_BOX);
      } else {
         this.resetHighlightedObject();
      }
   }

   public ObjectFlags getHighlightedObject() {
      return this.highlightedObjectFlags;
   }

   public void setHighlightedObject(MapTilesLoader.TileSet objectType) {
      this.highlightedObjectFlags = this.object.getObjectFlagFactory().getObjectFlags(objectType, ObjectBase.ObjectSubType.BUILT);
   }

   public void resetHighlightedObject() {
      this.highlightedObjectFlags = null;
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
