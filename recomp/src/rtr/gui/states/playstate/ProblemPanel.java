package rtr.gui.states.playstate;

import java.util.ArrayList;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.PlayStateGUIPanelBase;
import rtr.mobs.MobBase;
import rtr.mobs.MobFlagsBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class ProblemPanel extends PlayStateGUIPanelBase {
   private Image panel = ImageLoader.getImage("res/GUI/gameState/buildingListPanel.png");
   private Rectangle mask = new Rectangle(0.0F, 0.0F, this.panel.getWidth(), this.panel.getHeight());
   private int problemPanelUpdateTick;
   private int minorIssues;
   private int moderateIssues;
   private int majorIssues;
   private boolean lowVillagerCount;
   private int deadVillagers;
   private int starvingVillagers;
   private int hungryVillagers;
   private int dehydratedVillagers;
   private int thirstyVillagers;
   private int homelessVillagers;
   private int confusedVillagers;
   private int combatVillagers;
   private int outsideVillagers;
   private int freezeVillagers;
   private int freezeCriticalVillagers;
   private int overheatVillagers;
   private int overheatCriticalVillagers;
   private int onFireVillagers;
   private boolean hasHarvestedFood;
   private boolean hasWater;
   private boolean freezing;
   private boolean freezingCritical;
   private boolean frying;
   private boolean fryingCritical;
   private int inVillageMonsters;
   private int attackingBuildingMonsters;
   private int emptyWorkBuildings;
   private int noAmmoTowers;
   private int underConstructionBuildings;
   private int noFoodBuildings;
   private int damagedBuildings;
   private boolean hasHomes;
   private boolean tooMuchConstruction;

   public ProblemPanel(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.masks.add(this.mask);
   }

   public void render(boolean debug) throws SlickException {
      this.x = this.controller.getResourceAndInfluenceBar().getRightX() - this.panel.getWidth() + 170;
      if (this.x + this.panel.getWidth() > this.controller.getRightPanel().getLeftX() - 46) {
         this.x = this.x - Math.abs(this.x + this.panel.getWidth() - this.controller.getRightPanel().getLeftX() + 46);
      }

      this.y = 133;
      this.mask.setX(this.x);
      this.mask.setY(this.y);
      this.panel.draw(this.x, this.y);
      this.font.drawString(this.x + 135, this.y + 3, Text.getText("playProblemPanel.header"), Text.FontType.HEADER, 2, true);
      Text.setVariableText("amount", this.minorIssues);
      this.font.drawString(this.x + 76, this.y + 32, Text.getText("playProblemPanel.minorIssues"), Text.FontType.BODY, 1, true);
      Text.setVariableText("amount", this.moderateIssues);
      this.font.drawString(this.x + 210, this.y + 32, Text.getText("playProblemPanel.moderateIssues"), Text.FontType.BODY, 1, true);
      Text.setVariableText("amount", this.majorIssues);
      this.font.drawString(this.x + 348, this.y + 32, Text.getText("playProblemPanel.majorIssues"), Text.FontType.BODY, 1, true);
      int alignY = 0;
      if (!this.hasHarvestedFood) {
         this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.noFoodAnywhere"), Text.FontType.BODY, 1);
         alignY += 14;
      }

      if (!this.hasWater) {
         this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.noWaterAnywhere"), Text.FontType.BODY, 1);
         alignY += 14;
      }

      if (!this.hasHomes) {
         this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.noHomesAnywhere"), Text.FontType.BODY, 1);
         alignY += 14;
      }

      if (this.lowVillagerCount) {
         this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.lowVillagers"), Text.FontType.BODY, 1);
         alignY += 14;
      }

      if (this.tooMuchConstruction) {
         this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.tooMuchConstruction"), Text.FontType.BODY, 1);
         alignY += 14;
      }

      if (this.freezingCritical) {
         this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.dangerouslyCold"), Text.FontType.BODY, 1);
         alignY += 14;
      } else if (this.freezing) {
         this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.veryCold"), Text.FontType.BODY, 1);
         alignY += 14;
      }

      if (this.fryingCritical) {
         this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.dangerouslyHot"), Text.FontType.BODY, 1);
         alignY += 14;
      } else if (this.frying) {
         this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.veryHot"), Text.FontType.BODY, 1);
         alignY += 14;
      }

      if (this.deadVillagers > 0) {
         Text.setVariableText("amount", this.deadVillagers);
         if (this.deadVillagers == 1) {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.recentlyDied"), Text.FontType.BODY, 1);
         } else {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.recentlyDiedPlural"), Text.FontType.BODY, 1);
         }

         alignY += 14;
      }

      if (this.hungryVillagers > 0) {
         Text.setVariableText("amount", this.hungryVillagers);
         if (this.hungryVillagers == 1) {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.villagersHungry"), Text.FontType.BODY, 1);
         } else {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.villagersHungryPlural"), Text.FontType.BODY, 1);
         }

         alignY += 14;
      }

      if (this.starvingVillagers > 0) {
         Text.setVariableText("amount", this.starvingVillagers);
         if (this.starvingVillagers == 1) {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.villagersStarving"), Text.FontType.BODY, 1);
         } else {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.villagersStarvingPlural"), Text.FontType.BODY, 1);
         }

         alignY += 14;
      }

      if (this.thirstyVillagers > 0) {
         Text.setVariableText("amount", this.thirstyVillagers);
         if (this.thirstyVillagers == 1) {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.villagersThirsty"), Text.FontType.BODY, 1);
         } else {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.villagersThirstyPlural"), Text.FontType.BODY, 1);
         }

         alignY += 14;
      }

      if (this.dehydratedVillagers > 0) {
         Text.setVariableText("amount", this.dehydratedVillagers);
         if (this.dehydratedVillagers == 1) {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.villagersDehydrated"), Text.FontType.BODY, 1);
         } else {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.villagersDehydratedPlural"), Text.FontType.BODY, 1);
         }

         alignY += 14;
      }

      if (this.homelessVillagers > 0) {
         Text.setVariableText("amount", this.homelessVillagers);
         if (this.homelessVillagers == 1) {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.villagersHomeless"), Text.FontType.BODY, 1);
         } else {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.villagersHomelessPlural"), Text.FontType.BODY, 1);
         }

         alignY += 14;
      }

      if (this.confusedVillagers > 0) {
         Text.setVariableText("amount", this.confusedVillagers);
         if (this.confusedVillagers == 1) {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.villagersConfused"), Text.FontType.BODY, 1);
         } else {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.villagersConfusedPlural"), Text.FontType.BODY, 1);
         }

         alignY += 14;
      }

      if (this.combatVillagers > 0) {
         Text.setVariableText("amount", this.combatVillagers);
         if (this.combatVillagers == 1) {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.villagersInCombat"), Text.FontType.BODY, 1);
         } else {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.villagersInCombatPlural"), Text.FontType.BODY, 1);
         }

         alignY += 14;
      }

      if (this.outsideVillagers > 0) {
         Text.setVariableText("amount", this.outsideVillagers);
         if (this.outsideVillagers == 1) {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.villagersOutside"), Text.FontType.BODY, 1);
         } else {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.villagersOutsidePlural"), Text.FontType.BODY, 1);
         }

         alignY += 14;
      }

      if (this.freezeVillagers > 0) {
         Text.setVariableText("amount", this.freezeVillagers);
         if (this.freezeVillagers == 1) {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.villagersCold"), Text.FontType.BODY, 1);
         } else {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.villagersColdPlural"), Text.FontType.BODY, 1);
         }

         alignY += 14;
      }

      if (this.freezeCriticalVillagers > 0) {
         Text.setVariableText("amount", this.freezeCriticalVillagers);
         if (this.freezeCriticalVillagers == 1) {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.villagersFreezing"), Text.FontType.BODY, 1);
         } else {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.villagersFreezingPlural"), Text.FontType.BODY, 1);
         }

         alignY += 14;
      }

      if (this.overheatVillagers > 0) {
         Text.setVariableText("amount", this.overheatVillagers);
         if (this.overheatVillagers == 1) {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.villagersHot"), Text.FontType.BODY, 1);
         } else {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.villagersHotPlural"), Text.FontType.BODY, 1);
         }

         alignY += 14;
      }

      if (this.overheatCriticalVillagers > 0) {
         Text.setVariableText("amount", this.overheatCriticalVillagers);
         if (this.overheatCriticalVillagers == 1) {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.villagersOverHeating"), Text.FontType.BODY, 1);
         } else {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.villagersOverHeatingPlural"), Text.FontType.BODY, 1);
         }

         alignY += 14;
      }

      if (this.onFireVillagers > 0) {
         Text.setVariableText("amount", this.onFireVillagers);
         if (this.onFireVillagers == 1) {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.villagersOnFire"), Text.FontType.BODY, 1);
         } else {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.villagersOnFirePlural"), Text.FontType.BODY, 1);
         }

         alignY += 14;
      }

      if (this.emptyWorkBuildings > 0) {
         Text.setVariableText("amount", this.emptyWorkBuildings);
         if (this.emptyWorkBuildings == 1) {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.noWorkers"), Text.FontType.BODY, 1);
         } else {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.noWorkersPlural"), Text.FontType.BODY, 1);
         }

         alignY += 14;
      }

      if (this.noAmmoTowers > 0) {
         Text.setVariableText("amount", this.noAmmoTowers);
         if (this.noAmmoTowers == 1) {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.noAmmo"), Text.FontType.BODY, 1);
         } else {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.noAmmoPlural"), Text.FontType.BODY, 1);
         }

         alignY += 14;
      }

      if (this.noFoodBuildings > 0) {
         Text.setVariableText("amount", this.noFoodBuildings);
         if (this.noFoodBuildings == 1) {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.noFood"), Text.FontType.BODY, 1);
         } else {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.noFoodPlural"), Text.FontType.BODY, 1);
         }

         alignY += 14;
      }

      if (this.damagedBuildings > 0) {
         Text.setVariableText("amount", this.damagedBuildings);
         if (this.damagedBuildings == 1) {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.buildingsDamaged"), Text.FontType.BODY, 1);
         } else {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.buildingsDamagedPlural"), Text.FontType.BODY, 1);
         }

         alignY += 14;
      }

      if (this.inVillageMonsters > 0) {
         Text.setVariableText("amount", this.inVillageMonsters);
         if (this.inVillageMonsters == 1) {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.monstersInVillage"), Text.FontType.BODY, 1);
         } else {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.monstersInVillagePlural"), Text.FontType.BODY, 1);
         }

         alignY += 14;
      }

      if (this.attackingBuildingMonsters > 0) {
         Text.setVariableText("amount", this.attackingBuildingMonsters);
         if (this.attackingBuildingMonsters == 1) {
            this.font.drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.monstersAttackingBuildings"), Text.FontType.BODY, 1);
         } else {
            this.font
               .drawString(this.x + 26, this.y + 59 + alignY, Text.getText("playProblemPanel.problem.monstersAttackingBuildingsPlural"), Text.FontType.BODY, 1);
         }

         alignY += 14;
      }
   }

   @Override
   public void update() {
      this.problemPanelUpdateTick++;
      if (this.problemPanelUpdateTick > 100) {
         ArrayList<MobBase> villagers = this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, true);
         ArrayList<MobBase> monsters = this.mob.getMobsByGroup(MobBase.MobGroup.MONSTER, false);
         ArrayList<ObjectBase> objects = this.object.getObjectsArrayList();
         this.minorIssues = 0;
         this.moderateIssues = 0;
         this.majorIssues = 0;
         this.lowVillagerCount = false;
         this.deadVillagers = 0;
         this.starvingVillagers = 0;
         this.hungryVillagers = 0;
         this.dehydratedVillagers = 0;
         this.thirstyVillagers = 0;
         this.homelessVillagers = 0;
         this.confusedVillagers = 0;
         this.combatVillagers = 0;
         this.outsideVillagers = 0;
         this.freezeVillagers = 0;
         this.freezeCriticalVillagers = 0;
         this.overheatVillagers = 0;
         this.overheatCriticalVillagers = 0;
         this.onFireVillagers = 0;
         this.hasHarvestedFood = false;
         this.hasWater = false;
         this.freezing = false;
         this.freezingCritical = false;
         this.frying = false;
         this.fryingCritical = false;
         this.inVillageMonsters = 0;
         this.attackingBuildingMonsters = 0;
         this.emptyWorkBuildings = 0;
         this.noAmmoTowers = 0;
         this.underConstructionBuildings = 0;
         this.noFoodBuildings = 0;
         this.damagedBuildings = 0;
         this.hasHomes = false;
         this.tooMuchConstruction = false;

         ResourceModule.ResourceType[] buildingStores;
         for (ResourceModule.ResourceType t : buildingStores = ResourceModule.ResourceType.values()) {
            if (t.getTemplate().getFoodPerUse() > 0 && this.resource.getResourceTotal(t, true) > 0) {
               this.hasHarvestedFood = true;
            }
         }

         if (!this.hasHarvestedFood) {
            this.majorIssues++;
         }

         if (this.resource.getTotalAvailableWater() > 0) {
            this.hasWater = true;
         } else {
            this.majorIssues++;
         }

         if (this.weather.getCurrentTemp() <= this.weather.getFreezePoint()) {
            this.freezingCritical = true;
            this.majorIssues++;
         } else if (this.weather.getCurrentTemp() <= this.weather.getColdPoint()) {
            this.freezing = true;
            this.minorIssues++;
         }

         if (this.weather.getCurrentTemp() >= this.weather.getFryPoint()) {
            this.fryingCritical = true;
            this.majorIssues++;
         } else if (this.weather.getCurrentTemp() >= this.weather.getHotPoint()) {
            this.frying = true;
            this.minorIssues++;
         }

         if (villagers.size() <= 6) {
            this.lowVillagerCount = true;
            this.moderateIssues++;
         }

         for (MobBase m : villagers) {
            MobFlagsBase mf = m.getMobFlags();
            if (m.isDead()) {
               this.deadVillagers++;
               this.minorIssues++;
            }

            if (!m.isDead()) {
               if (m.getHunger() <= mf.getHungerStarvationFoodSearchLevel()) {
                  this.starvingVillagers++;
                  this.majorIssues++;
               } else if (m.getHunger() <= mf.getHungerHungryFoodSearchLevel()) {
                  this.hungryVillagers++;
                  this.moderateIssues++;
               }

               if (m.getThirst() <= mf.getThirstDehydrationWaterSearchLevel()) {
                  this.dehydratedVillagers++;
                  this.majorIssues++;
               } else if (m.getThirst() <= mf.getThirstThirstyWaterSearchLevel()) {
                  this.thirstyVillagers++;
                  this.moderateIssues++;
               }

               if (m.getAssignedHome() == -1) {
                  this.homelessVillagers++;
                  this.minorIssues++;
               }

               if (m.getActiveNode() == m.getAIConfused()) {
                  this.confusedVillagers++;
                  this.minorIssues++;
               }

               if (m.getActiveNode() == m.getAICombatMobs() || m.getActiveNode() == m.getAICombatBuildings()) {
                  this.combatVillagers++;
                  this.moderateIssues++;
               }

               if (!this.object.isInRange(m.getTileX(), m.getTileY())) {
                  this.outsideVillagers++;
                  this.minorIssues++;
               }

               if (m.getBodyTemp() <= mf.getThermalFreezeCritical()) {
                  this.freezeCriticalVillagers++;
                  this.majorIssues++;
               } else if (m.getBodyTemp() <= mf.getThermalFreeze()) {
                  this.freezeVillagers++;
                  this.minorIssues++;
               }

               if (m.getBodyTemp() >= mf.getThermalOverheatCritical()) {
                  this.overheatCriticalVillagers++;
                  this.majorIssues++;
               } else if (m.getBodyTemp() >= mf.getThermalOverheat()) {
                  this.overheatVillagers++;
                  this.minorIssues++;
               }

               if (m.isOnFire()) {
                  this.onFireVillagers++;
                  this.majorIssues++;
               }
            }
         }

         for (MobBase m : monsters) {
            if (this.object.isInRange(m.getTileX(), m.getTileY())) {
               this.inVillageMonsters++;
               this.moderateIssues++;
               if (m.getActiveNode() == m.getAICombatBuildings()) {
                  this.attackingBuildingMonsters++;
                  this.moderateIssues++;
               }
            }
         }

         for (ObjectBase o : objects) {
            if (o.isInRange()) {
               ObjectFlags objectFlags = o.getObjectFlags();
               if (o.getTower() != null) {
                  if (objectFlags.getAmmoMax() > 0 && o.getAmmo() == 0) {
                     this.noAmmoTowers++;
                     this.moderateIssues++;
                  }

                  if (objectFlags.getEnergyMax() > 0 && o.getEnergy() == 0) {
                     this.noAmmoTowers++;
                     this.moderateIssues++;
                  }
               }

               if (objectFlags.getSubType() == ObjectBase.ObjectSubType.CONSTRUCTION
                  || objectFlags.getSubType() == ObjectBase.ObjectSubType.ABANDONED && !o.isPaused()) {
                  this.underConstructionBuildings++;
               }

               if (objectFlags.getOccupantsMax() > 0) {
                  for (ResourceModule.ResourceType tx : objectFlags.getCanBuildingStore()) {
                     if (tx.getTemplate().getFoodPerUse() > 0 && o.getResourceCount(tx) == 0) {
                        this.noFoodBuildings++;
                        this.moderateIssues++;
                        break;
                     }
                  }
               }

               if (objectFlags.getOccupantsMax() > 0) {
                  this.hasHomes = true;
               }

               if (o.getHitPoints() < o.getHitPointsMax()) {
                  this.damagedBuildings++;
                  this.moderateIssues++;
               }
            }
         }

         if (!this.hasHomes) {
            this.majorIssues++;
         }

         if (this.underConstructionBuildings >= 8) {
            this.tooMuchConstruction = true;
            this.minorIssues++;
         }

         this.problemPanelUpdateTick = 0;
      }
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
