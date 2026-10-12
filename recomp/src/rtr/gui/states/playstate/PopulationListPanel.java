package rtr.gui.states.playstate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.gui.buttons.GUIClickableText;
import rtr.gui.buttons.GUIIcon;
import rtr.gui.buttons.GUIRoundButton;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.PlayStateGUIPanelBase;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;

public class PopulationListPanel extends PlayStateGUIPanelBase {
   private static final int PAGE_LENGTH = 10;
   private Image panel;
   private Rectangle mask;
   private GUIRoundButton filterLeft;
   private GUIRoundButton filterRight;
   private GUIRoundButton listTop;
   private GUIRoundButton listBottom;
   private GUIRoundButton listDown;
   private GUIRoundButton listUp;
   private GUIIcon iconAdults;
   private GUIIcon iconElder;
   private GUIIcon iconChildren;
   private GUIIcon iconGolems;
   private GUIIcon iconAverageAge;
   private GUIIcon iconAverageLifeExpectancy;
   private GUIIcon iconHunger;
   private GUIIcon iconThirst;
   private GUIIcon iconEnergy;
   private GUIIcon iconTotalHomeless;
   private GUIIcon iconTotalPregnant;
   private GUIIcon iconTotalBlessed;
   private GUIIcon iconTotalMate;
   private GUIIcon iconTotalOwner;
   private GUIIcon iconTotalUnemployedVillagers;
   private GUIClickableText[] listActiveMobNames = new GUIClickableText[10];
   private GUIIcon[] iconHomeless = new GUIIcon[10];
   private GUIIcon[] iconPregnant = new GUIIcon[10];
   private GUIIcon[] iconBlessed = new GUIIcon[10];
   private GUIIcon[] iconBlessedChild = new GUIIcon[10];
   private GUIIcon[] iconMate = new GUIIcon[10];
   private GUIIcon[] iconOwner = new GUIIcon[10];
   private GUIIcon[] iconUnemployedVillagers = new GUIIcon[10];
   private GUIIcon[] iconLevel = new GUIIcon[10];
   private GUIIcon[] iconAge = new GUIIcon[10];
   private PopulationListPanel.PopulationFilter populationFilter = PopulationListPanel.PopulationFilter.ALL;
   private ArrayList<MobBase> activeMobList = new ArrayList<>();
   private int activeMobListStart = 0;
   private int page = 1;

   public PopulationListPanel(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.panel = ImageLoader.getImage("res/GUI/gameState/populationListPanel.png");
      this.mask = new Rectangle(0.0F, 0.0F, this.panel.getWidth(), this.panel.getHeight());
      this.masks.add(this.mask);
      this.filterLeft = new GUIRoundButton("filterLeft", "buttonLeft", 0);
      this.filterLeft.setToolTip(Text.getText("playPopulationListPanel.button.tooltip.previousSortFilterHeader"));
      this.guiButtons.add(this.filterLeft);
      this.filterRight = new GUIRoundButton("filterRight", "buttonRight", 0);
      this.filterRight.setToolTip(Text.getText("playPopulationListPanel.button.tooltip.nextSortFilterHeader"));
      this.guiButtons.add(this.filterRight);
      this.listTop = new GUIRoundButton("listTop", "buttonTop", 0);
      this.listTop.setToolTip(Text.getText("playPopulationListPanel.button.tooltip.firstPageHeader"));
      this.guiButtons.add(this.listTop);
      this.listBottom = new GUIRoundButton("listBottom", "buttonBottom", 0);
      this.listBottom.setToolTip(Text.getText("playPopulationListPanel.button.tooltip.lastPageHeader"));
      this.guiButtons.add(this.listBottom);
      this.listDown = new GUIRoundButton("listDown", "buttonDown", 0);
      this.listDown.setToolTip(Text.getText("playPopulationListPanel.button.tooltip.pageDownHeader"));
      this.guiButtons.add(this.listDown);
      this.listUp = new GUIRoundButton("listUp", "buttonUp", 0);
      this.listUp.setToolTip(Text.getText("playPopulationListPanel.button.tooltip.pageUpHeader"));
      this.guiButtons.add(this.listUp);
      this.iconAdults = new GUIIcon("iconVillager");
      this.iconAdults
         .setToolTip(Text.getText("playPopulationListPanel.tooltip.totalAdultsHeader"), Text.getText("playPopulationListPanel.tooltip.totalAdults"));
      this.guiButtons.add(this.iconAdults);
      this.iconElder = new GUIIcon("iconElder");
      this.iconElder.setToolTip(Text.getText("playPopulationListPanel.tooltip.totalEldersHeader"), Text.getText("playPopulationListPanel.tooltip.totalElders"));
      this.guiButtons.add(this.iconElder);
      this.iconChildren = new GUIIcon("iconChild");
      this.iconChildren
         .setToolTip(Text.getText("playPopulationListPanel.tooltip.totalChildrenHeader"), Text.getText("playPopulationListPanel.tooltip.totalChildren"));
      this.guiButtons.add(this.iconChildren);
      this.iconGolems = new GUIIcon("iconGolem");
      this.iconGolems
         .setToolTip(Text.getText("playPopulationListPanel.tooltip.totalGolemsHeader"), Text.getText("playPopulationListPanel.tooltip.totalGolems"));
      this.guiButtons.add(this.iconGolems);
      this.iconAverageAge = new GUIIcon("iconAge");
      this.iconAverageAge
         .setToolTip(Text.getText("playPopulationListPanel.tooltip.averageAgeHeader"), Text.getText("playPopulationListPanel.tooltip.averageAge"));
      this.guiButtons.add(this.iconAverageAge);
      this.iconHunger = new GUIIcon("iconHunger");
      this.iconHunger
         .setToolTip(Text.getText("playPopulationListPanel.tooltip.averageHungerHeader"), Text.getText("playPopulationListPanel.tooltip.averageHunger"));
      this.guiButtons.add(this.iconHunger);
      this.iconThirst = new GUIIcon("iconWater");
      this.iconThirst
         .setToolTip(Text.getText("playPopulationListPanel.tooltip.averageThirstHeader"), Text.getText("playPopulationListPanel.tooltip.averageThirst"));
      this.guiButtons.add(this.iconThirst);
      this.iconEnergy = new GUIIcon("iconEnergy");
      this.iconEnergy
         .setToolTip(Text.getText("playPopulationListPanel.tooltip.averageEnergyHeader"), Text.getText("playPopulationListPanel.tooltip.averageEnergy"));
      this.guiButtons.add(this.iconEnergy);
      this.iconTotalHomeless = new GUIIcon("iconHomeless");
      this.iconTotalHomeless
         .setToolTip(Text.getText("playPopulationListPanel.tooltip.totalHomlessHeader"), Text.getText("playPopulationListPanel.tooltip.totalHomless"));
      this.guiButtons.add(this.iconTotalHomeless);
      this.iconTotalPregnant = new GUIIcon("iconPregnant");
      this.iconTotalPregnant
         .setToolTip(Text.getText("playPopulationListPanel.tooltip.totalPregnantHeader"), Text.getText("playPopulationListPanel.tooltip.totalPregnant"));
      this.guiButtons.add(this.iconTotalPregnant);
      this.iconTotalBlessed = new GUIIcon("iconFaith");
      this.iconTotalBlessed
         .setToolTip(Text.getText("playPopulationListPanel.tooltip.totalBlessedHeader"), Text.getText("playPopulationListPanel.tooltip.totalBlessed"));
      this.guiButtons.add(this.iconTotalBlessed);
      this.iconTotalMate = new GUIIcon("iconMate");
      this.iconTotalMate
         .setToolTip(Text.getText("playPopulationListPanel.tooltip.totalMatesHeader"), Text.getText("playPopulationListPanel.tooltip.totalMates"));
      this.guiButtons.add(this.iconTotalMate);
      this.iconTotalOwner = new GUIIcon("iconOwner");
      this.iconTotalOwner
         .setToolTip(Text.getText("playPopulationListPanel.tooltip.totalAnimalsHeader"), Text.getText("playPopulationListPanel.tooltip.totalAnimals"));
      this.guiButtons.add(this.iconTotalOwner);
      this.iconTotalUnemployedVillagers = new GUIIcon("iconWorkers");
      this.iconTotalUnemployedVillagers
         .setToolTip(
            Text.getText("playPopulationListPanel.tooltip.unemployedVillagersHeader"), Text.getText("playPopulationListPanel.tooltip.unemployedVillagers")
         );
      this.guiButtons.add(this.iconTotalUnemployedVillagers);
      this.iconAverageLifeExpectancy = new GUIIcon("iconLifeExpectancy");
      this.iconAverageLifeExpectancy
         .setToolTip(
            Text.getText("playPopulationListPanel.tooltip.averageLifeExpectancyHeader"), Text.getText("playPopulationListPanel.tooltip.averageLifeExpectancy")
         );
      this.guiButtons.add(this.iconAverageLifeExpectancy);

      for (int i = 0; i < 10; i++) {
         this.listActiveMobNames[i] = new GUIClickableText("villagerNameList" + i, 1);
         this.iconHomeless[i] = new GUIIcon("iconHomeless");
         this.iconHomeless[i]
            .setToolTip(Text.getText("playPopulationListPanel.tooltip.homelessHeader"), Text.getText("playPopulationListPanel.tooltip.homeless"));
         this.guiButtons.add(this.iconHomeless[i]);
         this.iconPregnant[i] = new GUIIcon("iconPregnant");
         this.iconPregnant[i]
            .setToolTip(Text.getText("playPopulationListPanel.tooltip.pregnantHeader"), Text.getText("playPopulationListPanel.tooltip.pregnant"));
         this.guiButtons.add(this.iconPregnant[i]);
         this.iconBlessed[i] = new GUIIcon("iconFaith");
         this.iconBlessed[i].setToolTip(Text.getText("playPopulationListPanel.tooltip.blessedHeader"), Text.getText("playPopulationListPanel.tooltip.blessed"));
         this.guiButtons.add(this.iconBlessed[i]);
         this.iconBlessedChild[i] = new GUIIcon("iconFaithUp");
         this.iconBlessedChild[i]
            .setToolTip(Text.getText("playPopulationListPanel.tooltip.blessedChildHeader"), Text.getText("playPopulationListPanel.tooltip.blessedChild"));
         this.guiButtons.add(this.iconBlessedChild[i]);
         this.iconMate[i] = new GUIIcon("iconMate");
         this.iconMate[i].setToolTip(Text.getText("playPopulationListPanel.tooltip.hasMateHeader"), Text.getText("playPopulationListPanel.tooltip.hasMate"));
         this.guiButtons.add(this.iconMate[i]);
         this.iconOwner[i] = new GUIIcon("iconOwner");
         this.iconOwner[i].setToolTip(Text.getText("playPopulationListPanel.tooltip.ownerHeader"), Text.getText("playPopulationListPanel.tooltip.owner"));
         this.guiButtons.add(this.iconOwner[i]);
         this.iconUnemployedVillagers[i] = new GUIIcon("iconWorkers");
         this.iconUnemployedVillagers[i]
            .setToolTip(Text.getText("playPopulationListPanel.tooltip.unemployedHeader"), Text.getText("playPopulationListPanel.tooltip.unemployed"));
         this.guiButtons.add(this.iconUnemployedVillagers[i]);
         this.iconLevel[i] = new GUIIcon("iconLevel");
         this.iconLevel[i].setToolTip(Text.getText("playPopulationListPanel.tooltip.levelHeader"), Text.getText("playPopulationListPanel.tooltip.level"));
         this.guiButtons.add(this.iconLevel[i]);
         this.iconAge[i] = new GUIIcon("iconAge");
         this.iconAge[i].setToolTip(Text.getText("playPopulationListPanel.tooltip.ageHeader"), Text.getText("playPopulationListPanel.tooltip.age"));
         this.guiButtons.add(this.iconAge[i]);
      }
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
      this.font.drawString(this.x + 135, this.y + 3, Text.getText("playPopulationListPanel.header"), Text.FontType.HEADER, 2, true);
      ArrayList<MobBase> villagersList = this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false);
      ArrayList<MobBase> adultsList = this.mob.getMobsByType(MobBase.MobType.HUMAN_VILLAGER, false);
      adultsList.addAll(this.mob.getMobsByType(MobBase.MobType.CATJEET_VILLAGER, false));
      adultsList.addAll(this.mob.getMobsByType(MobBase.MobType.HUMAN_VILLAGER_NEPHILIM, false));
      adultsList.addAll(this.mob.getMobsByType(MobBase.MobType.CATJEET_VILLAGER_NEPHILIM, false));
      ArrayList<MobBase> eldersList = this.mob.getMobsByType(MobBase.MobType.HUMAN_VILLAGER_ELDER, false);
      eldersList.addAll(this.mob.getMobsByType(MobBase.MobType.CATJEET_VILLAGER_ELDER, false));
      eldersList.addAll(this.mob.getMobsByType(MobBase.MobType.HUMAN_VILLAGER_ELDER_NEPHILIM, false));
      eldersList.addAll(this.mob.getMobsByType(MobBase.MobType.CATJEET_VILLAGER_ELDER_NEPHILIM, false));
      ArrayList<MobBase> childrenList = this.mob.getMobsByType(MobBase.MobType.HUMAN_VILLAGER_CHILD, false);
      childrenList.addAll(this.mob.getMobsByType(MobBase.MobType.CATJEET_VILLAGER_CHILD, false));
      childrenList.addAll(this.mob.getMobsByType(MobBase.MobType.HUMAN_VILLAGER_CHILD_NEPHILIM, false));
      childrenList.addAll(this.mob.getMobsByType(MobBase.MobType.CATJEET_VILLAGER_CHILD_NEPHILIM, false));
      ArrayList<MobBase> golemsList = this.mob.getMobsByGroup(MobBase.MobGroup.GOLEM, false);
      ArrayList<MobBase> animalList = this.mob.getMobsByGroup(MobBase.MobGroup.DOMESTICATED_ANIMAL, false);
      ArrayList<MobBase> fullMobList = new ArrayList<>();
      fullMobList.addAll(villagersList);
      fullMobList.addAll(golemsList);
      fullMobList.addAll(animalList);
      this.activeMobList.clear();
      if (this.populationFilter == PopulationListPanel.PopulationFilter.ALL) {
         this.activeMobList.addAll(fullMobList);
      } else if (this.populationFilter == PopulationListPanel.PopulationFilter.VILLAGERS) {
         this.activeMobList.addAll(villagersList);
      } else if (this.populationFilter == PopulationListPanel.PopulationFilter.ADULTS) {
         this.activeMobList.addAll(adultsList);
      } else if (this.populationFilter == PopulationListPanel.PopulationFilter.ELDERS) {
         this.activeMobList.addAll(eldersList);
      } else if (this.populationFilter == PopulationListPanel.PopulationFilter.CHILDREN) {
         this.activeMobList.addAll(childrenList);
      } else if (this.populationFilter == PopulationListPanel.PopulationFilter.GOLEMS) {
         this.activeMobList.addAll(golemsList);
      } else if (this.populationFilter == PopulationListPanel.PopulationFilter.DOMESTICATED_ANIMALS) {
         this.activeMobList.addAll(animalList);
      }

      Collections.sort(this.activeMobList, new Comparator<MobBase>() {
         public int compare(MobBase m1, MobBase m2) {
            return m1 == m2 ? 0 : m1.getID() - m2.getID();
         }
      });
      Collections.sort(this.activeMobList, new Comparator<MobBase>() {
         public int compare(MobBase m1, MobBase m2) {
            return m1 == m2 ? 0 : m2.getLevel() - m1.getLevel();
         }
      });
      int unassignedVillagers = 0;
      int adults = 0;
      int elders = 0;
      int children = 0;
      int golems = 0;
      int homeless = 0;
      int pregnant = 0;
      int blessed = 0;
      int mate = 0;
      int animalsOwned = 0;

      for (int i = 0; i < fullMobList.size(); i++) {
         MobBase m = fullMobList.get(i);
         if (m.getMobType() == MobBase.MobType.HUMAN_VILLAGER
            || m.getMobType() == MobBase.MobType.CATJEET_VILLAGER
            || m.getMobType() == MobBase.MobType.HUMAN_VILLAGER_NEPHILIM
            || m.getMobType() == MobBase.MobType.CATJEET_VILLAGER_NEPHILIM) {
            adults++;
         }

         if (m.getMobType() == MobBase.MobType.HUMAN_VILLAGER_ELDER
            || m.getMobType() == MobBase.MobType.CATJEET_VILLAGER_ELDER
            || m.getMobType() == MobBase.MobType.HUMAN_VILLAGER_ELDER_NEPHILIM
            || m.getMobType() == MobBase.MobType.CATJEET_VILLAGER_ELDER_NEPHILIM) {
            elders++;
         }

         if (m.getMobType() == MobBase.MobType.HUMAN_VILLAGER_CHILD
            || m.getMobType() == MobBase.MobType.CATJEET_VILLAGER_CHILD
            || m.getMobType() == MobBase.MobType.HUMAN_VILLAGER_CHILD_NEPHILIM
            || m.getMobType() == MobBase.MobType.CATJEET_VILLAGER_CHILD_NEPHILIM) {
            children++;
         }

         if (m.getMobGroup() == MobBase.MobGroup.GOLEM) {
            golems++;
         }

         if (m.getMobJobType() == MobJobBase.MobJobType.NONE
            && m.getMobType() != MobBase.MobType.HUMAN_VILLAGER_CHILD
            && m.getMobType() != MobBase.MobType.HUMAN_VILLAGER_ELDER
            && m.getMobType() != MobBase.MobType.CATJEET_VILLAGER_ELDER
            && m.getMobType() != MobBase.MobType.CATJEET_VILLAGER_CHILD
            && m.getMobType() != MobBase.MobType.HUMAN_VILLAGER_CHILD_NEPHILIM
            && m.getMobType() != MobBase.MobType.HUMAN_VILLAGER_ELDER_NEPHILIM
            && m.getMobType() != MobBase.MobType.CATJEET_VILLAGER_ELDER_NEPHILIM
            && m.getMobType() != MobBase.MobType.CATJEET_VILLAGER_CHILD_NEPHILIM
            && m.getMobGroup() != MobBase.MobGroup.DOMESTICATED_ANIMAL
            && m.getMobGroup() != MobBase.MobGroup.GOLEM) {
            unassignedVillagers++;
         }

         if (m.getMobFlags().getAllowHome() && m.getAssignedHome() == -1 || m.getMobFlags().getAllowSpawnSearch() && m.getAssignedSpawn() == -1) {
            homeless++;
         }

         if (m.isPregnant()
            && (
               m.getMobFlags().getSocialMateChildType() == MobBase.MobType.HUMAN_VILLAGER_CHILD
                  || m.getMobFlags().getSocialMateChildType() == MobBase.MobType.CATJEET_VILLAGER_CHILD
            )) {
            pregnant++;
         }

         if (m.isSocialBlessed()) {
            blessed++;
         }

         if (m.getMate() != null) {
            mate++;
         }

         if (m.getOwner() != null && m.getMobFlags().canSocialBeOwner()) {
            animalsOwned++;
         }
      }

      int totalPages = this.activeMobList.size() / 10;
      if (this.activeMobList.size() % 10 > 0) {
         totalPages++;
      }

      Text.setVariableText("currentPage", this.page);
      Text.setVariableText("totalPages", totalPages);
      this.font.drawString(this.x + 36, this.y + 90, Text.getText("playPopulationListPanel.pageList"), Text.FontType.BODY, 1);
      this.filterLeft.render(this.g, this.mouse, this.x + 255, this.y + 88, debug);
      if (this.populationFilter == PopulationListPanel.PopulationFilter.ALL) {
         this.font.drawString(this.x + 309, this.y + 90, Text.getText("playPopulationListPanel.sort.all"), Text.FontType.BODY, 1, true);
      } else if (this.populationFilter == PopulationListPanel.PopulationFilter.VILLAGERS) {
         this.font.drawString(this.x + 309, this.y + 90, Text.getText("playPopulationListPanel.sort.villagers"), Text.FontType.BODY, 1, true);
      } else if (this.populationFilter == PopulationListPanel.PopulationFilter.ADULTS) {
         this.font.drawString(this.x + 309, this.y + 90, Text.getText("playPopulationListPanel.sort.adults"), Text.FontType.BODY, 1, true);
      } else if (this.populationFilter == PopulationListPanel.PopulationFilter.ELDERS) {
         this.font.drawString(this.x + 309, this.y + 90, Text.getText("playPopulationListPanel.sort.elders"), Text.FontType.BODY, 1, true);
      } else if (this.populationFilter == PopulationListPanel.PopulationFilter.CHILDREN) {
         this.font.drawString(this.x + 309, this.y + 90, Text.getText("playPopulationListPanel.sort.children"), Text.FontType.BODY, 1, true);
      } else if (this.populationFilter == PopulationListPanel.PopulationFilter.GOLEMS) {
         this.font.drawString(this.x + 309, this.y + 90, Text.getText("playPopulationListPanel.sort.golems"), Text.FontType.BODY, 1, true);
      } else if (this.populationFilter == PopulationListPanel.PopulationFilter.DOMESTICATED_ANIMALS) {
         this.font.drawString(this.x + 309, this.y + 90, Text.getText("playPopulationListPanel.sort.animals"), Text.FontType.BODY, 1, true);
      }

      this.filterRight.render(this.g, this.mouse, this.x + 345, this.y + 88, debug);
      String[] genderColor = new String[2];
      if (this.activeMobList.size() > 0) {
         for (int i = 0; i < 10; i++) {
            if (this.activeMobListStart + i >= this.activeMobList.size()) {
               this.listActiveMobNames[i].hideButton();
            } else {
               MobBase mx = this.activeMobList.get(this.activeMobListStart + i);
               int alignX = 0;
               int alignY = i * 18;
               mx.renderSprite(this.g, this.x + 20 + alignX, this.y + alignY + 116, debug);
               genderColor[0] = "$WHI0";
               genderColor[1] = "$WHI0";
               if (mx.getGender() == MobBase.MobGender.MALE) {
                  genderColor[0] = "$CYA0";
                  genderColor[1] = "$CYA1";
               }

               if (mx.getGender() == MobBase.MobGender.FEMALE) {
                  genderColor[0] = "$PIN0";
                  genderColor[1] = "$PIN1";
               }

               this.listActiveMobNames[i]
                  .render(this.g, this.mouse, mx.getFullName(), this.x + 40 + alignX, this.y + alignY + 119, genderColor[0], genderColor[1], false, debug);
               if (mx.getMobJobType() == MobJobBase.MobJobType.NONE
                  && mx.getMobType() != MobBase.MobType.HUMAN_VILLAGER_CHILD
                  && mx.getMobType() != MobBase.MobType.CATJEET_VILLAGER_CHILD
                  && mx.getMobType() != MobBase.MobType.HUMAN_VILLAGER_CHILD_NEPHILIM
                  && mx.getMobType() != MobBase.MobType.CATJEET_VILLAGER_CHILD_NEPHILIM
                  && mx.getMobType() != MobBase.MobType.HUMAN_VILLAGER_ELDER
                  && mx.getMobType() != MobBase.MobType.CATJEET_VILLAGER_ELDER
                  && mx.getMobType() != MobBase.MobType.HUMAN_VILLAGER_ELDER_NEPHILIM
                  && mx.getMobType() != MobBase.MobType.CATJEET_VILLAGER_ELDER_NEPHILIM
                  && mx.getMobGroup() != MobBase.MobGroup.DOMESTICATED_ANIMAL
                  && mx.getMobGroup() != MobBase.MobGroup.GOLEM) {
                  this.iconUnemployedVillagers[i].render(this.g, this.x + 180 + alignX, this.y + alignY + 116, debug);
               }

               if (mx.getMobFlags().getAllowHome() && mx.getAssignedHome() == -1 || mx.getMobFlags().getAllowSpawnSearch() && mx.getAssignedSpawn() == -1) {
                  this.iconHomeless[i].render(this.g, this.x + 200 + alignX, this.y + alignY + 116, debug);
               }

               if (mx.getMate() != null) {
                  this.iconMate[i].render(this.g, this.x + 220 + alignX, this.y + alignY + 116, debug);
               }

               if (mx.getOwner() != null) {
                  this.iconOwner[i].render(this.g, this.x + 240 + alignX, this.y + alignY + 116, debug);
               }

               if (mx.isPregnant()) {
                  this.iconPregnant[i].render(this.g, this.x + 260 + alignX, this.y + alignY + 116, debug);
               }

               if (mx.isSocialBlessed()) {
                  this.iconBlessed[i].render(this.g, this.x + 280 + alignX, this.y + alignY + 116, debug);
               } else if (mx.isSocialChildBlessed()) {
                  this.iconBlessedChild[i].render(this.g, this.x + 280 + alignX, this.y + alignY + 116, debug);
               }

               this.iconLevel[i].render(this.g, this.x + 300 + alignX, this.y + alignY + 116, debug);
               this.font.drawString(this.x + 318 + alignX, this.y + alignY + 116, "$GRE1" + mx.getLevel(), Text.FontType.BODY, 1);
               alignX += 46;
               this.iconAge[i].render(this.g, this.x + 300 + alignX, this.y + alignY + 116, debug);
               this.font.drawString(this.x + 318 + alignX, this.y + alignY + 116, "$GRE1" + mx.getAge(), Text.FontType.BODY, 1);
            }
         }
      }

      this.listTop.render(this.g, this.mouse, this.x + 380, this.y + 90, debug);
      this.listUp.render(this.g, this.mouse, this.x + 380, this.y + 115, debug);
      this.listDown.render(this.g, this.mouse, this.x + 380, this.y + 243, debug);
      this.listBottom.render(this.g, this.mouse, this.x + 380, this.y + 268, debug);
      this.iconAdults.render(this.g, this.x + 30, this.y + 37, debug);
      this.font.drawString(this.x + 50, this.y + 37, "" + adults, Text.FontType.BODY, 1);
      this.iconElder.render(this.g, this.x + 80, this.y + 37, debug);
      this.font.drawString(this.x + 100, this.y + 37, "" + elders, Text.FontType.BODY, 1);
      this.iconChildren.render(this.g, this.x + 130, this.y + 37, debug);
      this.font.drawString(this.x + 150, this.y + 37, "" + children, Text.FontType.BODY, 1);
      this.iconGolems.render(this.g, this.x + 180, this.y + 37, debug);
      this.font.drawString(this.x + 200, this.y + 37, "" + golems, Text.FontType.BODY, 1);
      this.iconTotalPregnant.render(this.g, this.x + 230, this.y + 37, debug);
      this.font.drawString(this.x + 250, this.y + 37, "" + pregnant, Text.FontType.BODY, 1);
      this.iconTotalBlessed.render(this.g, this.x + 230, this.y + 57, debug);
      this.font.drawString(this.x + 250, this.y + 57, "" + blessed, Text.FontType.BODY, 1);
      this.iconTotalHomeless.render(this.g, this.x + 30, this.y + 57, debug);
      this.font.drawString(this.x + 50, this.y + 57, "" + homeless, Text.FontType.BODY, 1);
      this.iconTotalMate.render(this.g, this.x + 80, this.y + 57, debug);
      this.font.drawString(this.x + 100, this.y + 57, "" + mate, Text.FontType.BODY, 1);
      this.iconTotalOwner.render(this.g, this.x + 130, this.y + 57, debug);
      this.font.drawString(this.x + 150, this.y + 57, "" + animalsOwned, Text.FontType.BODY, 1);
      this.iconTotalUnemployedVillagers.render(this.g, this.x + 180, this.y + 57, debug);
      this.font.drawString(this.x + 200, this.y + 57, "" + unassignedVillagers, Text.FontType.BODY, 1);
      this.iconHunger.render(this.g, this.x + 325, this.y + 32, debug);
      this.font.drawString(this.x + 345, this.y + 32, "" + this.mob.getAverageHunger(MobBase.MobGroup.VILLAGER), Text.FontType.BODY, 1);
      this.iconAverageAge.render(this.g, this.x + 325, this.y + 49, debug);
      this.font.drawString(this.x + 345, this.y + 49, "" + this.mob.getAverageAge(MobBase.MobGroup.VILLAGER), Text.FontType.BODY, 1);
      this.iconAverageLifeExpectancy.render(this.g, this.x + 325, this.y + 66, debug);
      this.font.drawString(this.x + 345, this.y + 66, "" + this.mob.getAverageLifeExpectancy(MobBase.MobGroup.VILLAGER), Text.FontType.BODY, 1);
      this.iconThirst.render(this.g, this.x + 365, this.y + 32, debug);
      this.font.drawString(this.x + 385, this.y + 32, "" + this.mob.getAverageThirst(MobBase.MobGroup.VILLAGER), Text.FontType.BODY, 1);
      this.iconEnergy.render(this.g, this.x + 365, this.y + 49, debug);
      this.font.drawString(this.x + 385, this.y + 49, "" + this.mob.getAverageEnergy(MobBase.MobGroup.VILLAGER), Text.FontType.BODY, 1);
   }

   public boolean intersectsPopulationList() {
      if (!this.controller.isPopulationListPanelEnabled()) {
         return false;
      } else {
         for (int i = 0; i < this.listActiveMobNames.length; i++) {
            if (this.listActiveMobNames[i].intersects(this.mouse)) {
               return true;
            }
         }

         return false;
      }
   }

   public MobBase getSelectedPopulationList() {
      if (!this.controller.isPopulationListPanelEnabled()) {
         return null;
      } else {
         for (int i = 0; i < this.listActiveMobNames.length; i++) {
            if (this.listActiveMobNames[i].intersects(this.mouse)) {
               return this.activeMobList.get(this.activeMobListStart + i);
            }
         }

         return null;
      }
   }

   public void listPageTop() {
      this.activeMobListStart = 0;
      this.page = 1;
   }

   public void listPageBottom() {
      if (this.activeMobList.size() > 10) {
         this.page = (this.activeMobList.size() + 9) / 10;
         this.activeMobListStart = this.page * 10 - 10;
      }
   }

   public void listPageUp() {
      if (this.activeMobListStart != 0) {
         this.activeMobListStart -= 10;
         this.page--;
      }
   }

   public void listPageDown() {
      if (this.activeMobListStart + 10 < this.activeMobList.size()) {
         this.activeMobListStart += 10;
         this.page++;
      }
   }

   public void setFilterUp() {
      if (this.populationFilter.ordinal() != PopulationListPanel.PopulationFilter.values().length - 1) {
         this.populationFilter = PopulationListPanel.PopulationFilter.values()[this.populationFilter.ordinal() + 1];
      } else {
         this.populationFilter = PopulationListPanel.PopulationFilter.values()[0];
      }

      this.listPageTop();
   }

   public void setFilterDown() {
      if (this.populationFilter.ordinal() != 0) {
         this.populationFilter = PopulationListPanel.PopulationFilter.values()[this.populationFilter.ordinal() - 1];
      } else {
         this.populationFilter = PopulationListPanel.PopulationFilter.values()[PopulationListPanel.PopulationFilter.values().length - 1];
      }

      this.listPageTop();
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

   private static enum PopulationFilter {
      ALL,
      VILLAGERS,
      ADULTS,
      ELDERS,
      CHILDREN,
      GOLEMS,
      DOMESTICATED_ANIMALS;
   }
}
