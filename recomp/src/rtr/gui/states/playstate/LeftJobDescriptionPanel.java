package rtr.gui.states.playstate;

import java.util.EnumMap;
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
import rtr.mobs.jobs.MobJobBase;
import rtr.resources.ResourceModule;
import rtr.system.ScaleControl;

public class LeftJobDescriptionPanel extends PlayStateGUIPanelBase {
   private Image panel;
   private EnumMap<ResourceModule.ResourceType, GUIIcon> icons = new EnumMap<>(ResourceModule.ResourceType.class);
   private MobJobBase.MobJobType highlightedJob;

   public LeftJobDescriptionPanel(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.panel = ImageLoader.getImage("res/GUI/gameState/objectDescriptionPanel.png");

      ResourceModule.ResourceType[] var9;
      for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
         GUIIcon newIcon = new GUIIcon(type);
         this.icons.put(type, newIcon);
         this.guiButtons.add(newIcon);
      }
   }

   public void render(boolean debug) throws SlickException {
      this.x = this.controller.getLeftPanel().getRightX();
      this.y = ScaleControl.getInterfaceCenterY() - 265;
      if (this.y < 134) {
         this.y = 134;
      }

      this.panel.draw(this.x, this.y);
      MobJobBase job = this.highlightedJob.getJob();
      if (job.getMobJobName().length() > 22) {
         this.font.drawString(this.x + 135, this.y + 6, "$CYA1" + job.getMobJobName(), Text.FontType.HEADER, 1, true);
      } else {
         this.font.drawString(this.x + 135, this.y + 4, "$CYA1" + job.getMobJobName(), Text.FontType.HEADER, 2, true);
      }

      int alignX = 0;
      int alignY = 27;
      int count = 0;
      int maximumCount = 12;
      String jobDescription = Text.wrapString(job.getDescription(), Text.FontType.BODY, 1, 230);
      this.font.drawString(this.x + alignX + 20, this.y + alignY, jobDescription, Text.FontType.BODY, 1);
      alignY += 81;
      if (job.canWorkerBuild()) {
         this.font.drawString(this.x + alignX + 24, this.y + alignY, Text.getText("playLeftJobDescriptionPanel.header.canBuildUsing"), Text.FontType.BODY, 2);
         alignY += 16;

         for (ResourceModule.ResourceType type : job.getCanWorkerBuild()) {
            this.icons.get(type).render(this.g, this.x + alignX + 20, this.y + alignY, debug);
            this.font.drawString(this.x + alignX + 39, this.y + 4 + alignY, "$YEL1" + type.getTemplate().getNamePlural(), Text.FontType.BODY, 0);
            if (count % 2 == 0) {
               alignX += 110;
            } else {
               alignY += 12;
               alignX = 0;
            }

            if (++count > maximumCount) {
               Text.setVariableText("amount", job.getCanWorkerBuild().size() - count);
               this.font
                  .drawString(
                     this.x + alignX + 39,
                     this.y + alignY,
                     Text.getText("playLeftJobDescriptionPanel.playLeftJobDescriptionPanel.moreResourcesThanListedCount"),
                     Text.FontType.BODY,
                     1
                  );
               break;
            }
         }

         alignX = 0;
         if (count % 2 == 0) {
            alignY += 12;
         } else {
            alignY += 24;
         }

         count = 0;
      }

      if (job.canWorkerBuildRoad()) {
         this.font.drawString(this.x + alignX + 24, this.y + alignY, Text.getText("playLeftJobDescriptionPanel.header.buildRoadsUsing"), Text.FontType.BODY, 2);
         alignY += 16;

         for (ResourceModule.ResourceType type : job.getCanWorkerBuildRoad()) {
            this.icons.get(type).render(this.g, this.x + alignX + 20, this.y + alignY, debug);
            this.font.drawString(this.x + alignX + 39, this.y + 4 + alignY, "$YEL1" + type.getTemplate().getNamePlural(), Text.FontType.BODY, 0);
            if (count % 2 == 0) {
               alignX += 110;
            } else {
               alignY += 12;
               alignX = 0;
            }

            if (++count > maximumCount) {
               Text.setVariableText("amount", job.getCanWorkerBuildRoad().size() - count);
               this.font
                  .drawString(
                     this.x + alignX + 39, this.y + alignY, Text.getText("playLeftJobDescriptionPanel.moreResourcesThanListedCount"), Text.FontType.BODY, 1
                  );
               break;
            }
         }

         alignX = 0;
         if (count % 2 == 0) {
            alignY += 12;
         } else {
            alignY += 24;
         }

         count = 0;
      }

      if (job.canWorkerDeliverRoadConstruction()) {
         this.font
            .drawString(this.x + alignX + 24, this.y + alignY, Text.getText("playLeftJobDescriptionPanel.header.canDeliverToBuildRoads"), Text.FontType.BODY, 2);
         alignY += 16;

         for (ResourceModule.ResourceType type : job.getCanWorkerDeliverRoadConstruction()) {
            this.icons.get(type).render(this.g, this.x + alignX + 20, this.y + alignY, debug);
            this.font.drawString(this.x + alignX + 39, this.y + 4 + alignY, "$YEL1" + type.getTemplate().getNamePlural(), Text.FontType.BODY, 0);
            if (count % 2 == 0) {
               alignX += 110;
            } else {
               alignY += 12;
               alignX = 0;
            }

            if (++count > maximumCount) {
               Text.setVariableText("amount", job.getCanWorkerDeliverRoadConstruction().size() - count);
               this.font
                  .drawString(
                     this.x + alignX + 39, this.y + alignY, Text.getText("playLeftJobDescriptionPanel.moreResourcesThanListedCount"), Text.FontType.BODY, 1
                  );
               break;
            }
         }

         alignX = 0;
         if (count % 2 == 0) {
            alignY += 12;
         } else {
            alignY += 24;
         }

         count = 0;
      }

      if (job.canWorkerConstructionHarvest()) {
         this.font
            .drawString(this.x + alignX + 24, this.y + alignY, Text.getText("playLeftJobDescriptionPanel.header.canHarvestToBuild"), Text.FontType.BODY, 2);
         alignY += 16;

         for (ResourceModule.ResourceType type : job.getCanWorkerConstructionHarvest()) {
            this.icons.get(type).render(this.g, this.x + alignX + 20, this.y + alignY, debug);
            this.font.drawString(this.x + alignX + 39, this.y + 4 + alignY, "$YEL1" + type.getTemplate().getNamePlural(), Text.FontType.BODY, 0);
            if (count % 2 == 0) {
               alignX += 110;
            } else {
               alignY += 12;
               alignX = 0;
            }

            if (++count > maximumCount) {
               Text.setVariableText("amount", job.getCanWorkerConstructionHarvest().size() - count);
               this.font
                  .drawString(
                     this.x + alignX + 39, this.y + alignY, Text.getText("playLeftJobDescriptionPanel.moreResourcesThanListedCount"), Text.FontType.BODY, 1
                  );
               break;
            }
         }

         alignX = 0;
         if (count % 2 == 0) {
            alignY += 12;
         } else {
            alignY += 24;
         }

         count = 0;
      }

      if (job.canWorkerDeliverConstruction()) {
         this.font
            .drawString(this.x + alignX + 24, this.y + alignY, Text.getText("playLeftJobDescriptionPanel.header.canDeliverToBuild"), Text.FontType.BODY, 2);
         alignY += 16;

         for (ResourceModule.ResourceType type : job.getCanWorkerDeliverConstruction()) {
            this.icons.get(type).render(this.g, this.x + alignX + 20, this.y + alignY, debug);
            this.font.drawString(this.x + alignX + 39, this.y + 4 + alignY, "$YEL1" + type.getTemplate().getNamePlural(), Text.FontType.BODY, 0);
            if (count % 2 == 0) {
               alignX += 110;
            } else {
               alignY += 12;
               alignX = 0;
            }

            if (++count > maximumCount) {
               Text.setVariableText("amount", job.getCanWorkerDeliverConstruction().size() - count);
               this.font
                  .drawString(
                     this.x + alignX + 39, this.y + alignY, Text.getText("playLeftJobDescriptionPanel.moreResourcesThanListedCount"), Text.FontType.BODY, 1
                  );
               break;
            }
         }

         alignX = 0;
         if (count % 2 == 0) {
            alignY += 12;
         } else {
            alignY += 24;
         }

         count = 0;
      }

      if (job.canWorkerHarvest()) {
         this.font
            .drawString(this.x + alignX + 24, this.y + alignY, Text.getText("playLeftJobDescriptionPanel.header.canHarvestToStore"), Text.FontType.BODY, 2);
         alignY += 16;

         for (ResourceModule.ResourceType type : job.getCanWorkerHarvest()) {
            this.icons.get(type).render(this.g, this.x + alignX + 20, this.y + alignY, debug);
            this.font.drawString(this.x + alignX + 39, this.y + 4 + alignY, "$YEL1" + type.getTemplate().getNamePlural(), Text.FontType.BODY, 0);
            if (count % 2 == 0) {
               alignX += 110;
            } else {
               alignY += 12;
               alignX = 0;
            }

            if (++count > maximumCount) {
               Text.setVariableText("amount", job.getCanWorkerHarvest().size() - count);
               this.font
                  .drawString(
                     this.x + alignX + 39, this.y + alignY, Text.getText("playLeftJobDescriptionPanel.moreResourcesThanListedCount"), Text.FontType.BODY, 1
                  );
               break;
            }
         }

         alignX = 0;
         if (count % 2 == 0) {
            alignY += 12;
         } else {
            alignY += 24;
         }

         count = 0;
      }

      if (job.canWorkerRefine()) {
         this.font
            .drawString(this.x + alignX + 24, this.y + alignY, Text.getText("playLeftJobDescriptionPanel.header.canRefineOrManufacture"), Text.FontType.BODY, 2);
         alignY += 16;

         for (ResourceModule.ResourceType type : job.getCanWorkerRefine()) {
            this.icons.get(type).render(this.g, this.x + alignX + 20, this.y + alignY, debug);
            this.font.drawString(this.x + alignX + 39, this.y + 4 + alignY, "$YEL1" + type.getTemplate().getNamePlural(), Text.FontType.BODY, 0);
            if (count % 2 == 0) {
               alignX += 110;
            } else {
               alignY += 12;
               alignX = 0;
            }

            if (++count > maximumCount) {
               Text.setVariableText("amount", job.getCanWorkerRefine().size() - count);
               this.font
                  .drawString(
                     this.x + alignX + 39, this.y + alignY, Text.getText("playLeftJobDescriptionPanel.moreResourcesThanListedCount"), Text.FontType.BODY, 1
                  );
               break;
            }
         }

         alignX = 0;
         if (count % 2 == 0) {
            alignY += 12;
         } else {
            alignY += 24;
         }

         count = 0;
      }

      if (job.canWorkerSlaughter()) {
         this.font.drawString(this.x + alignX + 24, this.y + alignY, Text.getText("playLeftJobDescriptionPanel.header.canSlaughter"), Text.FontType.BODY, 2);
         alignY += 16;

         for (ResourceModule.ResourceType type : job.getCanWorkerSlaughter()) {
            this.icons.get(type).render(this.g, this.x + alignX + 20, this.y + alignY, debug);
            this.font.drawString(this.x + alignX + 39, this.y + 4 + alignY, "$YEL1" + type.getTemplate().getNamePlural(), Text.FontType.BODY, 0);
            if (count % 2 == 0) {
               alignX += 110;
            } else {
               alignY += 12;
               alignX = 0;
            }

            if (++count > maximumCount) {
               Text.setVariableText("amount", job.getCanWorkerRefine().size() - count);
               this.font
                  .drawString(
                     this.x + alignX + 39, this.y + alignY, Text.getText("playLeftJobDescriptionPanel.moreResourcesThanListedCount"), Text.FontType.BODY, 1
                  );
               break;
            }
         }

         alignX = 0;
         if (count % 2 == 0) {
            alignY += 12;
         } else {
            alignY += 24;
         }

         count = 0;
      }

      if (job.canWorkerPlant()) {
         this.font.drawString(this.x + alignX + 24, this.y + alignY, Text.getText("playLeftJobDescriptionPanel.header.canPlant"), Text.FontType.BODY, 2);
         alignY += 16;

         for (ResourceModule.ResourceType type : job.getCanWorkerPlant()) {
            this.icons.get(type).render(this.g, this.x + alignX + 20, this.y + alignY, debug);
            this.font.drawString(this.x + alignX + 39, this.y + 4 + alignY, "$YEL1" + type.getTemplate().getNamePlural(), Text.FontType.BODY, 0);
            if (count % 2 == 0) {
               alignX += 110;
            } else {
               alignY += 12;
               alignX = 0;
            }

            if (++count > maximumCount) {
               Text.setVariableText("amount", job.getCanWorkerPlant().size() - count);
               this.font
                  .drawString(
                     this.x + alignX + 39, this.y + alignY, Text.getText("playLeftJobDescriptionPanel.moreResourcesThanListedCount"), Text.FontType.BODY, 1
                  );
               break;
            }
         }

         alignX = 0;
         if (count % 2 == 0) {
            alignY += 12;
         } else {
            alignY += 24;
         }

         count = 0;
      }

      if (job.canWorkerDeliverCourier()) {
         this.font
            .drawString(
               this.x + alignX + 24, this.y + alignY, Text.getText("playLeftJobDescriptionPanel.header.canDeliverToCourierGolems"), Text.FontType.BODY, 2
            );
         alignY += 16;

         for (ResourceModule.ResourceType type : job.getCanWorkerDeliverCourier()) {
            this.icons.get(type).render(this.g, this.x + alignX + 20, this.y + alignY, debug);
            this.font.drawString(this.x + alignX + 39, this.y + 4 + alignY, "$YEL1" + type.getTemplate().getNamePlural(), Text.FontType.BODY, 0);
            if (count % 2 == 0) {
               alignX += 110;
            } else {
               alignY += 12;
               alignX = 0;
            }

            if (++count > maximumCount) {
               Text.setVariableText("amount", job.getCanWorkerDeliverCourier().size() - count);
               this.font
                  .drawString(
                     this.x + alignX + 39, this.y + alignY, Text.getText("playLeftJobDescriptionPanel.moreResourcesThanListedCount"), Text.FontType.BODY, 1
                  );
               break;
            }
         }

         alignX = 0;
         if (count % 2 == 0) {
            alignY += 12;
         } else {
            alignY += 24;
         }

         count = 0;
      }

      if (job.canWorkerStore()) {
         this.font.drawString(this.x + alignX + 24, this.y + alignY, Text.getText("playLeftJobDescriptionPanel.header.canStore"), Text.FontType.BODY, 2);
         alignY += 16;

         for (ResourceModule.ResourceType type : job.getCanWorkerStore()) {
            this.icons.get(type).render(this.g, this.x + alignX + 20, this.y + alignY, debug);
            this.font.drawString(this.x + alignX + 39, this.y + 4 + alignY, "$YEL1" + type.getTemplate().getNamePlural(), Text.FontType.BODY, 0);
            if (count % 2 == 0) {
               alignX += 110;
            } else {
               alignY += 12;
               alignX = 0;
            }

            if (++count > maximumCount) {
               Text.setVariableText("amount", job.getCanWorkerStore().size() - count);
               this.font
                  .drawString(
                     this.x + alignX + 39, this.y + alignY, Text.getText("playLeftJobDescriptionPanel.moreResourcesThanListedCount"), Text.FontType.BODY, 1
                  );
               break;
            }
         }

         alignX = 0;
         if (count % 2 == 0) {
            alignY += 12;
         } else {
            alignY += 24;
         }

         count = 0;
      }

      if (job.canWorkerUseTools()) {
         this.font.drawString(this.x + alignX + 24, this.y + alignY, Text.getText("playLeftJobDescriptionPanel.header.preferredTools"), Text.FontType.BODY, 2);
         alignY += 16;

         for (ResourceModule.ResourceType type : job.getPreferredTools()) {
            this.icons.get(type).render(this.g, this.x + alignX + 20, this.y + alignY, debug);
            this.font.drawString(this.x + alignX + 39, this.y + 4 + alignY, "$YEL1" + type.getTemplate().getNamePlural(), Text.FontType.BODY, 0);
            if (count % 2 == 0) {
               alignX += 110;
            } else {
               alignY += 12;
               alignX = 0;
            }

            if (++count > maximumCount) {
               Text.setVariableText("amount", job.getPreferredTools().size() - count);
               this.font
                  .drawString(
                     this.x + alignX + 39, this.y + alignY, Text.getText("playLeftJobDescriptionPanel.moreResourcesThanListedCount"), Text.FontType.BODY, 1
                  );
               break;
            }
         }

         alignX = 0;
         if (count % 2 == 0) {
            alignY += 12;
         } else {
            alignY += 24;
         }

      }

      if (job.getBonusDamagePercent() > 0 || job.getBonusDamagePercent() > 0) {
         this.font.drawString(this.x + alignX + 24, this.y + alignY, Text.getText("playLeftJobDescriptionPanel.header.combatBonuses"), Text.FontType.BODY, 2);
         alignY += 16;
         if (job.getBonusDamagePercent() > 0) {
            this.icons.get(ResourceModule.ResourceType.IRON_SWORD).render(this.g, this.x + alignX + 20, this.y + alignY, debug);
            Text.setVariableText("amount", job.getBonusDamagePercent());
            this.font
               .drawString(this.x + alignX + 40, this.y + alignY - 1, Text.getText("playLeftJobDescriptionPanel.combatDamageBonus"), Text.FontType.BODY, 1);
            alignY += 12;
         }

         if (job.getBonusDefensePercent() > 0) {
            this.icons.get(ResourceModule.ResourceType.IRON_SHIELD).render(this.g, this.x + alignX + 20, this.y + alignY, debug);
            Text.setVariableText("amount", job.getBonusDefensePercent());
            this.font
               .drawString(this.x + alignX + 40, this.y + alignY - 1, Text.getText("playLeftJobDescriptionPanel.combatDefenseBonus"), Text.FontType.BODY, 1);
            alignY += 12;
         }
      }

      if (job.canRepair()) {
         this.icons.get(ResourceModule.ResourceType.HAMMER).render(this.g, this.x + alignX + 20, this.y + alignY, debug);
         this.font
            .drawString(this.x + alignX + 40, this.y + alignY - 1, Text.getText("playLeftJobDescriptionPanel.header.canRepairBuildings"), Text.FontType.BODY, 1);
         alignY += 12;
      }

      if (job.canRepairRoad()) {
         this.icons.get(ResourceModule.ResourceType.HAMMER).render(this.g, this.x + alignX + 20, this.y + alignY, debug);
         this.font
            .drawString(this.x + alignX + 40, this.y + alignY - 1, Text.getText("playLeftJobDescriptionPanel.header.canRepairRoads"), Text.FontType.BODY, 1);
         alignY += 12;
      }

      if (job.canClearTopography()) {
         this.icons.get(ResourceModule.ResourceType.HAMMER).render(this.g, this.x + alignX + 20, this.y + alignY, debug);
         this.font
            .drawString(
               this.x + alignX + 40, this.y + alignY - 1, Text.getText("playLeftJobDescriptionPanel.header.canDestroyClearTerrain"), Text.FontType.BODY, 1
            );
         alignY += 12;
      }

      if (job.canClearRoads()) {
         this.icons.get(ResourceModule.ResourceType.HAMMER).render(this.g, this.x + alignX + 20, this.y + alignY, debug);
         this.font
            .drawString(
               this.x + alignX + 40, this.y + alignY - 1, Text.getText("playLeftJobDescriptionPanel.header.canDestroyClearRoads"), Text.FontType.BODY, 1
            );
         alignY += 12;
      }

      if (job.canDigHoles()) {
         this.icons.get(ResourceModule.ResourceType.HAMMER).render(this.g, this.x + alignX + 20, this.y + alignY, debug);
         this.font
            .drawString(this.x + alignX + 40, this.y + alignY - 1, Text.getText("playLeftJobDescriptionPanel.header.canCreateLakes"), Text.FontType.BODY, 1);
         alignY += 12;
      }

      if (job.canWorkerPlant()) {
         this.icons.get(ResourceModule.ResourceType.HAMMER).render(this.g, this.x + alignX + 20, this.y + alignY, debug);
         this.font
            .drawString(
               this.x + alignX + 40, this.y + alignY - 1, Text.getText("playLeftJobDescriptionPanel.header.canTendToAndWaterFarms"), Text.FontType.BODY, 1
            );
         alignY += 12;
      }
   }

   @Override
   public void update() {
      String intersectingButton = this.controller.getLeftPanel().getIntersects();
      boolean leftPanelHighlight = false;
      if (intersectingButton != null && this.controller.getLeftPanel().intersectsGUIMask()) {
         MobJobBase.MobJobType[] var6;
         for (MobJobBase.MobJobType t : MobJobBase.MobJobType.values()) {
            if (intersectingButton.equals("workforceWorkerPanel" + t)) {
               this.highlightedJob = t;
               leftPanelHighlight = true;
            }
         }
      }

      if (!leftPanelHighlight) {
         this.highlightedJob = null;
      }
   }

   public MobJobBase.MobJobType getHighlightedJob() {
      return this.highlightedJob;
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
