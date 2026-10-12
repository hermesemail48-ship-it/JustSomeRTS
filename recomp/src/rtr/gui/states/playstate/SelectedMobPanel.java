package rtr.gui.states.playstate;

import java.util.EnumMap;
import java.util.HashMap;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.gui.buttons.GUIClickableArea;
import rtr.gui.buttons.GUIIcon;
import rtr.gui.buttons.GUIToolTipOverlay;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.PlayStateGUIPanelBase;
import rtr.mobs.MobBase;
import rtr.mobs.MobFlagsBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.system.ScaleControl;

public class SelectedMobPanel extends PlayStateGUIPanelBase {
   private Image panel = ImageLoader.getImage("res/GUI/gameState/mobPanel.png");
   private Rectangle mask = new Rectangle(0.0F, 0.0F, this.panel.getWidth(), this.panel.getHeight());
   private Image inventoryIconHighlight1;
   private Image inventoryIconHighlight2;
   private Image inventoryIconHighlight3;
   private Image inventoryIconHighlight4;
   private Image inventoryIconHighlight5;
   private Image inventoryIconHighlight6;
   private Image inventoryIconHighlight7;
   private Image inventoryIconHighlight8;
   private Image inventoryIconHighlight9;
   private Image inventoryIconHighlight10;
   private Image inventoryIcon1;
   private Image inventoryIcon2;
   private Image inventoryIcon3;
   private Image inventoryIcon4;
   private Image inventoryIcon5;
   private Image inventoryIcon6;
   private Image inventoryIcon7;
   private Image inventoryIcon8;
   private Image inventoryIcon9;
   private Image inventoryIcon10;
   private GUIIcon iconHitPoints;
   private GUIIcon iconTrash;
   private GUIIcon iconEgg;
   private GUIIcon iconTemp;
   private GUIIcon iconMate;
   private GUIIcon iconOwner;
   private GUIIcon iconPregnant;
   private GUIIcon iconBlessed;
   private GUIIcon iconBlessedChild;
   private GUIIcon iconKills;
   private GUIIcon iconLevel;
   private GUIIcon iconAge;
   private GUIIcon iconLifeExpectancy;
   private GUIIcon iconLifeExpectancyInfinity;
   private GUIIcon iconHunger;
   private GUIIcon iconThirst;
   private GUIIcon iconFaith;
   private GUIIcon iconEnergy;
   private GUIIcon iconConstitution;
   private GUIIcon iconSocial;
   private GUIIcon iconStrength;
   private GUIIcon iconDexterity;
   private GUIClickableArea experienceBar;
   private GUIClickableArea statRectangle;
   private GUIToolTipOverlay inventoryToolTip;
   private GUIIcon iconHeadSlot;
   private GUIIcon iconChestSlot;
   private GUIIcon iconToolSlot;
   private GUIIcon iconMeleeWeaponSlot;
   private GUIIcon iconShieldSlot;
   private GUIIcon iconRecoverySlot;
   private GUIIcon iconWaterSlot;
   private GUIIcon iconFoodSlot;
   private GUIIcon iconRangedWeaponSlot;
   private GUIIcon iconRangedWeaponAmmoSlot;

   public SelectedMobPanel(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.masks.add(this.mask);
      this.inventoryIconHighlight1 = ImageLoader.getImage("res/GUI/gameState/mobPanelInventoryIconHighlight1.png");
      this.inventoryIconHighlight2 = ImageLoader.getImage("res/GUI/gameState/mobPanelInventoryIconHighlight2.png");
      this.inventoryIconHighlight3 = ImageLoader.getImage("res/GUI/gameState/mobPanelInventoryIconHighlight3.png");
      this.inventoryIconHighlight4 = ImageLoader.getImage("res/GUI/gameState/mobPanelInventoryIconHighlight4.png");
      this.inventoryIconHighlight5 = ImageLoader.getImage("res/GUI/gameState/mobPanelInventoryIconHighlight5.png");
      this.inventoryIconHighlight6 = ImageLoader.getImage("res/GUI/gameState/mobPanelInventoryIconHighlight6.png");
      this.inventoryIconHighlight7 = ImageLoader.getImage("res/GUI/gameState/mobPanelInventoryIconHighlight7.png");
      this.inventoryIconHighlight8 = ImageLoader.getImage("res/GUI/gameState/mobPanelInventoryIconHighlight8.png");
      this.inventoryIconHighlight9 = ImageLoader.getImage("res/GUI/gameState/mobPanelInventoryIconHighlight9.png");
      this.inventoryIconHighlight10 = ImageLoader.getImage("res/GUI/gameState/mobPanelInventoryIconHighlight10.png");
      this.inventoryIcon1 = ImageLoader.getImage("res/GUI/gameState/mobPanelInventoryIcon1.png");
      this.inventoryIcon2 = ImageLoader.getImage("res/GUI/gameState/mobPanelInventoryIcon2.png");
      this.inventoryIcon3 = ImageLoader.getImage("res/GUI/gameState/mobPanelInventoryIcon3.png");
      this.inventoryIcon4 = ImageLoader.getImage("res/GUI/gameState/mobPanelInventoryIcon4.png");
      this.inventoryIcon5 = ImageLoader.getImage("res/GUI/gameState/mobPanelInventoryIcon5.png");
      this.inventoryIcon6 = ImageLoader.getImage("res/GUI/gameState/mobPanelInventoryIcon6.png");
      this.inventoryIcon7 = ImageLoader.getImage("res/GUI/gameState/mobPanelInventoryIcon7.png");
      this.inventoryIcon8 = ImageLoader.getImage("res/GUI/gameState/mobPanelInventoryIcon8.png");
      this.inventoryIcon9 = ImageLoader.getImage("res/GUI/gameState/mobPanelInventoryIcon9.png");
      this.inventoryIcon10 = ImageLoader.getImage("res/GUI/gameState/mobPanelInventoryIcon10.png");
      this.iconHitPoints = new GUIIcon("iconHitPoints");
      this.iconHitPoints.setToolTip(Text.getText("playSelectedMobPanel.tooltip.hitPointsHeader"), Text.getText("playSelectedMobPanel.tooltip.hitPoints"));
      this.guiButtons.add(this.iconHitPoints);
      this.iconHunger = new GUIIcon("iconHunger");
      this.iconHunger.setToolTip(Text.getText("playSelectedMobPanel.tooltip.hungerHeader"), Text.getText("playSelectedMobPanel.tooltip.hunger"));
      this.guiButtons.add(this.iconHunger);
      this.iconThirst = new GUIIcon("iconWater");
      this.iconThirst.setToolTip(Text.getText("playSelectedMobPanel.tooltip.thirstHeader"), Text.getText("playSelectedMobPanel.tooltip.thirst"));
      this.guiButtons.add(this.iconThirst);
      this.iconFaith = new GUIIcon("iconFaith");
      this.iconFaith.setToolTip(Text.getText("playSelectedMobPanel.tooltip.faithHeader"), Text.getText("playSelectedMobPanel.tooltip.faith"));
      this.guiButtons.add(this.iconFaith);
      this.iconTrash = new GUIIcon(ResourceModule.ResourceType.TRASHY_TRASH);
      this.iconTrash.setToolTip(Text.getText("playSelectedMobPanel.tooltip.trashHeader"), Text.getText("playSelectedMobPanel.tooltip.trash"));
      this.guiButtons.add(this.iconTrash);
      this.iconEgg = new GUIIcon(ResourceModule.ResourceType.EGG);
      this.iconEgg.setToolTip(Text.getText("playSelectedMobPanel.tooltip.eggHeader"), Text.getText("playSelectedMobPanel.tooltip.egg"));
      this.guiButtons.add(this.iconEgg);
      this.iconTemp = new GUIIcon("iconTemp");
      this.iconTemp
         .setToolTip(Text.getText("playSelectedMobPanel.tooltip.bodyTemperatureHeader"), Text.getText("playSelectedMobPanel.tooltip.bodyTemperature"));
      this.guiButtons.add(this.iconTemp);
      this.iconMate = new GUIIcon("iconMate");
      this.iconMate.setToolTip(Text.getText("playSelectedMobPanel.tooltip.hasMateHeader"), Text.getText("playSelectedMobPanel.tooltip.hasMate"));
      this.guiButtons.add(this.iconMate);
      this.iconOwner = new GUIIcon("iconOwner");
      this.iconOwner.setToolTip(Text.getText("playSelectedMobPanel.tooltip.ownerHeader"), Text.getText("playSelectedMobPanel.tooltip.owner"));
      this.guiButtons.add(this.iconOwner);
      this.iconPregnant = new GUIIcon("iconPregnant");
      this.iconPregnant.setToolTip(Text.getText("playSelectedMobPanel.tooltip.pregnantHeader"), Text.getText("playSelectedMobPanel.tooltip.pregnant"));
      this.guiButtons.add(this.iconPregnant);
      this.iconBlessed = new GUIIcon("iconFaith");
      this.iconBlessed.setToolTip(Text.getText("playSelectedMobPanel.tooltip.blessedHeader"), Text.getText("playSelectedMobPanel.tooltip.blessed"));
      this.guiButtons.add(this.iconBlessed);
      this.iconBlessedChild = new GUIIcon("iconFaithUp");
      this.iconBlessedChild
         .setToolTip(Text.getText("playSelectedMobPanel.tooltip.blessedChildHeader"), Text.getText("playSelectedMobPanel.tooltip.blessedChild"));
      this.guiButtons.add(this.iconBlessedChild);
      this.iconKills = new GUIIcon(ResourceModule.ResourceType.IRON_SWORD);
      this.iconKills.setToolTip(Text.getText("playSelectedMobPanel.tooltip.killsHeader"), Text.getText("playSelectedMobPanel.tooltip.kills"));
      this.guiButtons.add(this.iconKills);
      this.iconLevel = new GUIIcon("iconLevel");
      this.iconLevel.setToolTip(Text.getText("playSelectedMobPanel.tooltip.levelHeader"), Text.getText("playSelectedMobPanel.tooltip.level"));
      this.guiButtons.add(this.iconLevel);
      this.iconAge = new GUIIcon("iconAge");
      this.iconAge.setToolTip(Text.getText("playSelectedMobPanel.tooltip.ageHeader"), Text.getText("playSelectedMobPanel.tooltip.age"));
      this.guiButtons.add(this.iconAge);
      this.iconLifeExpectancy = new GUIIcon("iconLifeExpectancy");
      this.iconLifeExpectancy
         .setToolTip(Text.getText("playSelectedMobPanel.tooltip.lifeExpectancyHeader"), Text.getText("playSelectedMobPanel.tooltip.lifeExpectancy"));
      this.guiButtons.add(this.iconLifeExpectancy);
      this.iconLifeExpectancyInfinity = new GUIIcon("iconInfinity");
      this.iconLifeExpectancyInfinity
         .setToolTip(Text.getText("playSelectedMobPanel.tooltip.immortalHeader"), Text.getText("playSelectedMobPanel.tooltip.immortal"));
      this.guiButtons.add(this.iconLifeExpectancyInfinity);
      this.iconEnergy = new GUIIcon("iconEnergy");
      this.iconEnergy.setToolTip(Text.getText("playSelectedMobPanel.tooltip.energyHeader"), Text.getText("playSelectedMobPanel.tooltip.energy"));
      this.guiButtons.add(this.iconEnergy);
      this.iconConstitution = new GUIIcon("iconConstitution");
      this.iconConstitution
         .setToolTip(Text.getText("playSelectedMobPanel.tooltip.lifelongConstitutionHeader"), Text.getText("playSelectedMobPanel.tooltip.lifelongConstitution"));
      this.guiButtons.add(this.iconConstitution);
      this.iconSocial = new GUIIcon("iconChatBubble1");
      this.iconSocial.setToolTip(Text.getText("playSelectedMobPanel.tooltip.socialHeader"), Text.getText("playSelectedMobPanel.tooltip.social"));
      this.guiButtons.add(this.iconSocial);
      this.iconStrength = new GUIIcon("iconStrength");
      this.iconStrength.setToolTip(Text.getText("playSelectedMobPanel.tooltip.strengthHeader"), Text.getText("playSelectedMobPanel.tooltip.strength"));
      this.guiButtons.add(this.iconStrength);
      this.iconDexterity = new GUIIcon("iconDexterity");
      this.iconDexterity.setToolTip(Text.getText("playSelectedMobPanel.tooltip.dexterityHeader"), Text.getText("playSelectedMobPanel.tooltip.dexterity"));
      this.guiButtons.add(this.iconDexterity);
      this.experienceBar = new GUIClickableArea("experienceBar", 100, 8);
      this.experienceBar.setToolTip(Text.getText("playSelectedMobPanel.tooltip.experienceBarHeader"));
      this.guiButtons.add(this.experienceBar);
      this.statRectangle = new GUIClickableArea("statRectangle", 52, 12);
      this.guiButtons.add(this.statRectangle);
      this.inventoryToolTip = new GUIToolTipOverlay();
      this.iconHeadSlot = new GUIIcon("iconHeadSlot");
      this.iconChestSlot = new GUIIcon("iconChestSlot");
      this.iconToolSlot = new GUIIcon("iconToolSlot");
      this.iconMeleeWeaponSlot = new GUIIcon("iconMeleeWeaponSlot");
      this.iconShieldSlot = new GUIIcon("iconShieldSlot");
      this.iconRecoverySlot = new GUIIcon("iconRecoverySlot");
      this.iconWaterSlot = new GUIIcon("iconWaterSlot");
      this.iconFoodSlot = new GUIIcon("iconFoodSlot");
      this.iconRangedWeaponSlot = new GUIIcon("iconRangedWeaponSlot");
      this.iconRangedWeaponAmmoSlot = new GUIIcon("iconRangedWeaponAmmoSlot");
   }

   public void render(boolean debug) throws SlickException {
      this.x = ScaleControl.getInterfaceCenterX() - this.panel.getWidth() / 2;
      this.y = ScaleControl.getInterfaceHeight() - this.panel.getHeight() - 3;
      this.mask.setX(this.x);
      this.mask.setY(this.y);
      MobBase selectedMob = this.guiData.getSelectedMob();
      MobFlagsBase selectedMobFlags = this.guiData.getSelectedMob().getMobFlags();
      if (this.mob.getMobArray().contains(selectedMob) && !selectedMob.getNoSelect()) {
         this.panel.draw(this.x, this.y);
         selectedMob.renderSprite(this.g, this.x + 15, this.y + 12, debug);
         this.font.drawString(this.x + 35, this.y + 9, selectedMob.getFormattedName(false), Text.FontType.HEADER, 2);
         if (selectedMob.getMobJobType() != MobJobBase.MobJobType.NONE && selectedMob.getMobJobType().getJob().getMobJobName() != null) {
            Text.setVariableText("mobJobName", selectedMob.getMobJobType().getJob().getMobJobName());
            this.font.drawString(this.x + 34, this.y + 30, Text.getText("playSelectedMobPanel.mobJobNamePrefix"), Text.FontType.BODY, 0);
         } else {
            Text.setVariableText("mobName", selectedMob.getMobType().getName());
            if (selectedMob.isBoosted()) {
               this.font.drawString(this.x + 34, this.y + 30, Text.getText("playSelectedMobPanel.boostedMobPrefix"), Text.FontType.BODY, 0);
            } else {
               this.font.drawString(this.x + 34, this.y + 30, Text.getText("playSelectedMobPanel.mobNamePrefix"), Text.FontType.BODY, 0);
            }
         }

         int alignY = 0;
         int alignX = 0;
         this.iconHitPoints.render(this.g, this.x + alignX + 20, this.y + alignY + 45, debug);
         int barWidth = 50;
         if (selectedMob.getHitPoints() < selectedMob.getHitPointsMax()) {
            barWidth = (int)Math.ceil((float)selectedMob.getHitPoints() / selectedMob.getHitPointsMax() * 50.0F);
         }

         this.g.setColor(new Color(0, 0, 0, 200));
         this.g.fillRect(this.x + alignX + 38, this.y + alignY + 47, 52.0F, 12.0F);
         if (barWidth > 37.5) {
            this.g.setColor(new Color(0, 100, 0));
         } else if (barWidth > 25.0) {
            this.g.setColor(new Color(100, 100, 0));
         } else if (barWidth > 12.5) {
            this.g.setColor(new Color(100, 50, 0));
         } else {
            this.g.setColor(new Color(100, 0, 0));
         }

         this.g.fillRect(this.x + alignX + 39, this.y + alignY + 48, barWidth, 10.0F);
         this.statRectangle.render(this.g, this.x + alignX + 38, this.y + alignY + 47, debug);
         if (debug || this.statRectangle.intersects(this.mouse)) {
            this.font
               .drawString(this.x + alignX + 40, this.y + alignY + 46, selectedMob.getHitPoints() + "/" + selectedMob.getHitPointsMax(), Text.FontType.BODY, 0);
         }

         alignY += 15;
         if (selectedMob.getMobJobType() != null && selectedMob.getMobJobType().getJob().canMakeTrashyCubes()) {
            this.iconTrash.render(this.g, this.x + alignX + 20, this.y + alignY + 45, debug);
            barWidth = 50;
            if (selectedMob.getTrash() < selectedMob.getTrashMax()) {
               barWidth = (int)Math.ceil((float)selectedMob.getTrash() / selectedMob.getTrashMax() * 50.0F);
            }

            this.g.setColor(new Color(0, 0, 0, 200));
            this.g.fillRect(this.x + alignX + 38, this.y + alignY + 47, 52.0F, 12.0F);
            if (barWidth > 37.5) {
               this.g.setColor(new Color(0, 100, 0));
            } else if (barWidth > 25.0) {
               this.g.setColor(new Color(100, 100, 0));
            } else if (barWidth > 12.5) {
               this.g.setColor(new Color(100, 50, 0));
            } else {
               this.g.setColor(new Color(100, 0, 0));
            }

            this.g.fillRect(this.x + alignX + 39, this.y + alignY + 48, barWidth, 10.0F);
            this.statRectangle.render(this.g, this.x + alignX + 38, this.y + alignY + 47, debug);
            if (debug || this.statRectangle.intersects(this.mouse)) {
               this.font
                  .drawString(this.x + alignX + 40, this.y + alignY + 46, selectedMob.getTrash() + "/" + selectedMob.getTrashMax(), Text.FontType.BODY, 0);
            }

            alignY += 15;
         }

         if (selectedMob.getMobFlags().allowProduceEggs()) {
            this.iconEgg.render(this.g, this.x + alignX + 20, this.y + alignY + 45, debug);
            barWidth = 50;
            if (selectedMob.getEgg() < selectedMob.getEggMax()) {
               barWidth = (int)Math.ceil((float)selectedMob.getEgg() / selectedMob.getEggMax() * 50.0F);
            }

            this.g.setColor(new Color(0, 0, 0, 200));
            this.g.fillRect(this.x + alignX + 38, this.y + alignY + 47, 52.0F, 12.0F);
            if (barWidth > 37.5) {
               this.g.setColor(new Color(0, 100, 0));
            } else if (barWidth > 25.0) {
               this.g.setColor(new Color(100, 100, 0));
            } else if (barWidth > 12.5) {
               this.g.setColor(new Color(100, 50, 0));
            } else {
               this.g.setColor(new Color(100, 0, 0));
            }

            this.g.fillRect(this.x + alignX + 39, this.y + alignY + 48, barWidth, 10.0F);
            this.statRectangle.render(this.g, this.x + alignX + 38, this.y + alignY + 47, debug);
            if (debug || this.statRectangle.intersects(this.mouse)) {
               this.font.drawString(this.x + alignX + 40, this.y + alignY + 46, selectedMob.getEgg() + "/" + selectedMob.getEggMax(), Text.FontType.BODY, 0);
            }

            alignY += 15;
         }

         if (selectedMobFlags.getAllowHunger()) {
            this.iconHunger.render(this.g, this.x + alignX + 20, this.y + alignY + 45, debug);
            barWidth = 50;
            if (selectedMob.getHunger() < selectedMob.getHungerMax()) {
               barWidth = (int)Math.ceil((float)selectedMob.getHunger() / selectedMob.getHungerMax() * 50.0F);
            }

            this.g.setColor(new Color(0, 0, 0, 200));
            this.g.fillRect(this.x + alignX + 38, this.y + alignY + 47, 52.0F, 12.0F);
            if (barWidth > 37.5) {
               this.g.setColor(new Color(0, 100, 0));
            } else if (barWidth > 25.0) {
               this.g.setColor(new Color(100, 100, 0));
            } else if (barWidth > 12.5) {
               this.g.setColor(new Color(100, 50, 0));
            } else {
               this.g.setColor(new Color(100, 0, 0));
            }

            this.g.fillRect(this.x + alignX + 39, this.y + alignY + 48, barWidth, 10.0F);
            this.statRectangle.render(this.g, this.x + alignX + 38, this.y + alignY + 47, debug);
            if (debug || this.statRectangle.intersects(this.mouse)) {
               this.font
                  .drawString(this.x + alignX + 40, this.y + alignY + 46, selectedMob.getHunger() + "/" + selectedMob.getHungerMax(), Text.FontType.BODY, 0);
            }

            alignY += 15;
         }

         if (selectedMobFlags.getAllowThirst()) {
            this.iconThirst.render(this.g, this.x + alignX + 20, this.y + alignY + 45, debug);
            barWidth = 50;
            if (selectedMob.getThirst() < selectedMob.getThirstMax()) {
               barWidth = (int)Math.ceil((float)selectedMob.getThirst() / selectedMob.getThirstMax() * 50.0F);
            }

            this.g.setColor(new Color(0, 0, 0, 200));
            this.g.fillRect(this.x + alignX + 38, this.y + alignY + 47, 52.0F, 12.0F);
            if (barWidth > 37.5) {
               this.g.setColor(new Color(0, 100, 0));
            } else if (barWidth > 25.0) {
               this.g.setColor(new Color(100, 100, 0));
            } else if (barWidth > 12.5) {
               this.g.setColor(new Color(100, 50, 0));
            } else {
               this.g.setColor(new Color(100, 0, 0));
            }

            this.g.fillRect(this.x + alignX + 39, this.y + alignY + 48, barWidth, 10.0F);
            this.statRectangle.render(this.g, this.x + alignX + 38, this.y + alignY + 47, debug);
            if (debug || this.statRectangle.intersects(this.mouse)) {
               this.font
                  .drawString(this.x + alignX + 40, this.y + alignY + 46, selectedMob.getThirst() + "/" + selectedMob.getThirstMax(), Text.FontType.BODY, 0);
            }

            alignY += 15;
         }

         if (selectedMobFlags.getAllowFaith()) {
            this.iconFaith.render(this.g, this.x + alignX + 20, this.y + alignY + 45, debug);
            barWidth = 50;
            if (selectedMob.getFaith() < selectedMob.getFaithMax()) {
               barWidth = (int)Math.ceil((float)selectedMob.getFaith() / selectedMob.getFaithMax() * 50.0F);
            }

            this.g.setColor(new Color(0, 0, 0, 200));
            this.g.fillRect(this.x + alignX + 38, this.y + alignY + 47, 52.0F, 12.0F);
            if (barWidth > 37.5) {
               this.g.setColor(new Color(0, 100, 0));
            } else if (barWidth > 25.0) {
               this.g.setColor(new Color(100, 100, 0));
            } else if (barWidth > 12.5) {
               this.g.setColor(new Color(100, 50, 0));
            } else {
               this.g.setColor(new Color(100, 0, 0));
            }

            this.g.fillRect(this.x + alignX + 39, this.y + alignY + 48, barWidth, 10.0F);
            this.statRectangle.render(this.g, this.x + alignX + 38, this.y + alignY + 47, debug);
            if (debug || this.statRectangle.intersects(this.mouse)) {
               this.font
                  .drawString(this.x + alignX + 40, this.y + alignY + 46, selectedMob.getFaith() + "/" + selectedMob.getFaithMax(), Text.FontType.BODY, 0);
            }

            alignY += 15;
         }

         if (selectedMobFlags.getAllowSleep()) {
            this.iconEnergy.render(this.g, this.x + alignX + 20, this.y + alignY + 45, debug);
            barWidth = 50;
            if (selectedMob.getEnergy() < selectedMob.getEnergyMax()) {
               barWidth = (int)Math.ceil((float)selectedMob.getEnergy() / selectedMob.getEnergyMax() * 50.0F);
            }

            this.g.setColor(new Color(0, 0, 0, 200));
            this.g.fillRect(this.x + alignX + 38, this.y + alignY + 47, 52.0F, 12.0F);
            if (barWidth > 37.5) {
               this.g.setColor(new Color(0, 100, 0));
            } else if (barWidth > 25.0) {
               this.g.setColor(new Color(100, 100, 0));
            } else if (barWidth > 12.5) {
               this.g.setColor(new Color(100, 50, 0));
            } else {
               this.g.setColor(new Color(100, 0, 0));
            }

            this.g.fillRect(this.x + alignX + 39, this.y + alignY + 48, barWidth, 10.0F);
            this.statRectangle.render(this.g, this.x + alignX + 38, this.y + alignY + 47, debug);
            if (debug || this.statRectangle.intersects(this.mouse)) {
               this.font
                  .drawString(this.x + alignX + 40, this.y + alignY + 46, selectedMob.getEnergy() + "/" + selectedMob.getEnergyMax(), Text.FontType.BODY, 0);
            }

            alignY += 15;
         }

         if (selectedMobFlags.getAllowThermal()) {
            this.iconTemp.render(this.g, this.x + alignX + 20, this.y + alignY + 45, debug);
            barWidth = 50;
            if (selectedMob.getBodyTemp() < selectedMob.getBodyTempMax()) {
               barWidth = (int)Math.ceil((float)selectedMob.getBodyTemp() / selectedMob.getBodyTempMax() * 50.0F);
            }

            this.g.setColor(new Color(0, 0, 0, 200));
            this.g.fillRect(this.x + alignX + 38, this.y + alignY + 47, 52.0F, 12.0F);
            if (selectedMob.getBodyTemp() <= selectedMobFlags.getThermalFreezeCritical()) {
               this.g.setColor(new Color(0, 0, 100));
            } else if (selectedMob.getBodyTemp() <= selectedMobFlags.getThermalFreeze()) {
               this.g.setColor(new Color(0, 100, 100));
            } else if (selectedMob.getBodyTemp() >= selectedMobFlags.getThermalOverheatCritical()) {
               this.g.setColor(new Color(100, 0, 0));
            } else if (selectedMob.getBodyTemp() >= selectedMobFlags.getThermalOverheat()) {
               this.g.setColor(new Color(100, 100, 0));
            } else {
               this.g.setColor(new Color(0, 100, 0));
            }

            this.g.fillRect(this.x + alignX + 39, this.y + alignY + 48, barWidth, 10.0F);
            this.statRectangle.render(this.g, this.x + alignX + 38, this.y + alignY + 47, debug);
            if (debug || this.statRectangle.intersects(this.mouse)) {
               this.font
                  .drawString(this.x + alignX + 40, this.y + alignY + 46, selectedMob.getBodyTemp() + "/" + selectedMob.getBodyTempMax(), Text.FontType.BODY, 0);
            }

            alignY += 15;
         }

         alignX += 80;
         int var16 = 0;
         if (selectedMobFlags.getAllowAging() && selectedMobFlags.getAgingMaximumAge() > 0) {
            this.iconConstitution.render(this.g, this.x + alignX + 20, this.y + var16 + 45, debug);
            barWidth = 50;
            if (selectedMob.getLifeExpectancy() < selectedMobFlags.getAgingMaximumAge()) {
               barWidth = (int)Math.ceil((float)selectedMob.getLifeExpectancy() / selectedMobFlags.getAgingMaximumAge() * 50.0F);
            }

            Color colorOut = new Color(0, 0, 0, 200);
            this.g.setColor(colorOut);
            this.g.fillRect(this.x + alignX + 38, this.y + var16 + 47, 52.0F, 12.0F);
            if (barWidth > 40.0) {
               colorOut = new Color(0, 100, 0);
            } else if (barWidth > 25.0) {
               colorOut = new Color(100, 100, 0);
            } else if (barWidth > 15.0) {
               colorOut = new Color(100, 50, 0);
            } else {
               colorOut = new Color(100, 0, 0);
            }

            this.g.setColor(colorOut);
            this.g.fillRect(this.x + alignX + 39, this.y + var16 + 48, barWidth, 10.0F);
            barWidth = 50;
            if (selectedMob.getAgingConstitutionDecayRateTick() < selectedMobFlags.getAgingConstitutionDecayRate()) {
               barWidth = (int)Math.ceil(
                  (float)(selectedMobFlags.getAgingConstitutionDecayRate() - selectedMob.getAgingConstitutionDecayRateTick())
                     / selectedMobFlags.getAgingConstitutionDecayRate()
                     * 50.0F
               );
            }

            this.g.setColor(colorOut.brighter(1.0F));
            this.g.fillRect(this.x + alignX + 39, this.y + var16 + 55, barWidth, 3.0F);
            this.statRectangle.render(this.g, this.x + alignX + 38, this.y + var16 + 47, debug);
            if (debug || this.statRectangle.intersects(this.mouse)) {
               this.font
                  .drawString(
                     this.x + alignX + 40,
                     this.y + var16 + 46,
                     selectedMob.getLifeExpectancy() + "/" + selectedMobFlags.getAgingMaximumAge(),
                     Text.FontType.BODY,
                     0
                  );
            }

            var16 += 15;
         }

         if (selectedMobFlags.getAllowSocial()) {
            this.iconSocial.render(this.g, this.x + alignX + 20, this.y + var16 + 45, debug);
            barWidth = 50;
            if (selectedMob.getSocial() < selectedMob.getSocialMax()) {
               barWidth = (int)Math.ceil((float)selectedMob.getSocial() / selectedMob.getSocialMax() * 50.0F);
            }

            this.g.setColor(new Color(0, 0, 0, 200));
            this.g.fillRect(this.x + alignX + 38, this.y + var16 + 47, 52.0F, 12.0F);
            if (barWidth > 37.5) {
               this.g.setColor(new Color(0, 100, 0));
            } else if (barWidth > 25.0) {
               this.g.setColor(new Color(100, 100, 0));
            } else if (barWidth > 12.5) {
               this.g.setColor(new Color(100, 50, 0));
            } else {
               this.g.setColor(new Color(100, 0, 0));
            }

            this.g.fillRect(this.x + alignX + 39, this.y + var16 + 48, barWidth, 10.0F);
            this.statRectangle.render(this.g, this.x + alignX + 38, this.y + var16 + 47, debug);
            if (debug || this.statRectangle.intersects(this.mouse)) {
               this.font
                  .drawString(this.x + alignX + 40, this.y + var16 + 46, selectedMob.getSocial() + "/" + selectedMob.getSocialMax(), Text.FontType.BODY, 0);
            }

            var16 += 15;
         }

         if (selectedMob.getMate() != null) {
            this.iconMate.render(this.g, this.x + alignX + 20, this.y + var16 + 45, debug);
            this.font.drawString(this.x + alignX + 40, this.y + var16 + 42, selectedMob.getMate().getFirstName(), Text.FontType.BODY, 2);
            var16 += 15;
         }

         if (selectedMob.getOwner() != null) {
            this.iconOwner.render(this.g, this.x + alignX + 20, this.y + var16 + 45, debug);
            this.font.drawString(this.x + alignX + 40, this.y + var16 + 42, selectedMob.getOwner().getFullName(), Text.FontType.BODY, 2);
            var16 += 15;
         }

         if (selectedMob.isPregnant()) {
            this.iconPregnant.render(this.g, this.x + alignX + 20, this.y + var16 + 45, debug);
            this.font.drawString(this.x + alignX + 40, this.y + var16 + 42, Text.getText("playSelectedMobPanel.pregnant"), Text.FontType.BODY, 2);
            var16 += 15;
         }

         if (selectedMob.isSocialBlessed()) {
            this.iconBlessed.render(this.g, this.x + alignX + 20, this.y + var16 + 45, debug);
            this.font.drawString(this.x + alignX + 40, this.y + var16 + 42, Text.getText("playSelectedMobPanel.blessed"), Text.FontType.BODY, 2);
            var16 += 15;
         }

         if (selectedMob.isSocialChildBlessed()) {
            this.iconBlessedChild.render(this.g, this.x + alignX + 20, this.y + var16 + 45, debug);
            this.font.drawString(this.x + alignX + 40, this.y + var16 + 42, Text.getText("playSelectedMobPanel.blessedChild"), Text.FontType.BODY, 2);
            var16 += 15;
         }

         if (selectedMob.getKills() > 0) {
            this.iconKills.render(this.g, this.x + alignX + 20, this.y + var16 + 45, debug);
            String killList = "";

            for (String s : selectedMob.getKilledNameList()) {
               killList = killList + s + "~";
            }

            this.iconKills
               .setToolTip(Text.getText("playSelectedMobPanel.tooltip.killsHeader"), Text.getText("playSelectedMobPanel.tooltip.kills") + "$RED1" + killList);
            this.font.drawString(this.x + alignX + 40, this.y + var16 + 42, String.valueOf(selectedMob.getKills()), Text.FontType.BODY, 2);
            var16 += 15;
         }

         int var18 = 0;
         if (selectedMobFlags.getDamageResistances().size() > 0) {
            this.font.drawString(this.x + 215, this.y + var18 + 31, Text.getText("playSelectedMobPanel.damageResistance"), Text.FontType.BODY, 1);
            var18 += 14;
            HashMap<Integer, String> output = new HashMap<>();
            EnumMap<MobBase.DamageType, Integer> types = selectedMobFlags.getDamageResistances();

            for (MobBase.DamageType d : selectedMobFlags.getDamageResistances().keySet()) {
               if (output.containsKey(types.get(d))) {
                  output.put(types.get(d), output.get(types.get(d)) + ", " + d.getText());
               } else {
                  output.put(types.get(d), d.getText());
               }
            }

            for (Integer i : output.keySet()) {
               this.font.drawString(this.x + 215, this.y + var18 + 31, "$BLU0" + i + "% $GRE1" + output.get(i), Text.FontType.BODY, 0);
               var18 += 11;
            }
         }

         var18 += 4;
         if (selectedMobFlags.getDamageVulnerabilities().size() > 0) {
            this.font.drawString(this.x + 215, this.y + var18 + 31, Text.getText("playSelectedMobPanel.damageVulnerabilities"), Text.FontType.BODY, 1);
            var18 += 14;
            HashMap<Integer, String> output = new HashMap<>();
            EnumMap<MobBase.DamageType, Integer> types = selectedMobFlags.getDamageVulnerabilities();

            for (MobBase.DamageType dx : selectedMobFlags.getDamageVulnerabilities().keySet()) {
               if (output.containsKey(types.get(dx))) {
                  output.put(types.get(dx), output.get(types.get(dx)) + ", " + dx.getText());
               } else {
                  output.put(types.get(dx), dx.getText());
               }
            }

            for (Integer i : output.keySet()) {
               this.font.drawString(this.x + 215, this.y + var18 + 31, "$RED0" + i + "% $GRE1" + output.get(i), Text.FontType.BODY, 0);
               var18 += 11;
            }
         }

         int xpLeft = (int)Math.ceil((float)selectedMob.getExperience() / (selectedMobFlags.getExperienceMultiplier() * selectedMob.getLevel()) * 100.0F);
         this.g.setColor(new Color(0, 255, 0, 100));
         this.g.fillRect(this.x + 340, this.y + 16, xpLeft, 8.0F);
         this.experienceBar.render(this.g, this.mouse, this.x + 337, this.y + 13, false, debug);
         int var24 = 0;
         this.iconLevel.render(this.g, this.x + var24 + 452, this.y + 12, debug);
         this.font.drawString(this.x + var24 + 470, this.y + 9, "" + selectedMob.getLevel(), Text.FontType.BODY, 2);
         var24 += 50;
         this.iconAge.render(this.g, this.x + var24 + 452, this.y + 12, debug);
         this.font.drawString(this.x + var24 + 470, this.y + 9, "" + selectedMob.getAge(), Text.FontType.BODY, 2);
         int var26 = 20;
         this.iconStrength.render(this.g, this.x + var26 + 352, this.y + 32, debug);
         this.font.drawString(this.x + var26 + 370, this.y + 30, "$RED1" + selectedMob.getStrength(), Text.FontType.BODY, 2);
         var26 += 60;
         this.iconDexterity.render(this.g, this.x + var26 + 352, this.y + 32, debug);
         this.font.drawString(this.x + var26 + 370, this.y + 30, "$GRE1" + selectedMob.getDexterity(), Text.FontType.BODY, 2);
         var26 += 70;
         this.iconLifeExpectancy.render(this.g, this.x + var26 + 352, this.y + 32, debug);
         if (selectedMobFlags.getAgingMaximumAge() > 0) {
            String colorOutx = "$GRE1";
            if (selectedMob.getLifeExpectancy() <= selectedMobFlags.getAgingMaximumAge() * 0.3F) {
               colorOutx = "$RED1";
            } else if (selectedMob.getLifeExpectancy() <= selectedMobFlags.getAgingMaximumAge() * 0.5F) {
               colorOutx = "$ORA1";
            } else if (selectedMob.getLifeExpectancy() <= selectedMobFlags.getAgingMaximumAge() * 0.8F) {
               colorOutx = "$YEL1";
            }

            this.font.drawString(this.x + var26 + 370, this.y + 30, colorOutx + selectedMob.getLifeExpectancy(), Text.FontType.BODY, 2);
         } else {
            this.iconLifeExpectancyInfinity.render(this.g, this.x + var26 + 372, this.y + 32, debug);
         }

         String inventoryMouseOver = null;
         int var29 = 0;
         int var21 = 0;
         if (selectedMobFlags.getCanInventoryEquipHead()) {
            this.inventoryIcon1.draw(this.x + var29 + 398, this.y + var21 + 59);
            this.iconHeadSlot.render(this.g, this.x + var29 + 402, this.y + var21 + 62, debug);
            ResourceBase highlightSlot = selectedMob.getInventoryHeadSlot();
            if (highlightSlot != null) {
               this.inventoryIconHighlight1.draw(this.x + var29 + 398, this.y + var21 + 59);
               highlightSlot.renderSprite(this.x + var29 + 402, this.y + var21 + 62, true);
            }

            if (this.iconHeadSlot.intersects(this.mouse)) {
               if (highlightSlot != null) {
                  inventoryMouseOver = "$CYA1" + highlightSlot.getType().getTemplate().getName();
               } else {
                  inventoryMouseOver = Text.getText("playSelectedMobPanel.inventorySlot.head");
               }
            }
         }

         var29 += 27;
         if (selectedMobFlags.getCanInventoryEquipChest()) {
            this.inventoryIcon2.draw(this.x + var29 + 398, this.y + var21 + 59);
            this.iconChestSlot.render(this.g, this.x + var29 + 402, this.y + var21 + 62, debug);
            ResourceBase highlightSlotx = selectedMob.getInventoryChestSlot();
            if (highlightSlotx != null) {
               this.inventoryIconHighlight2.draw(this.x + var29 + 398, this.y + var21 + 59);
               highlightSlotx.renderSprite(this.x + var29 + 402, this.y + var21 + 62, true);
            }

            if (this.iconChestSlot.intersects(this.mouse)) {
               if (highlightSlotx != null) {
                  inventoryMouseOver = "$CYA1" + highlightSlotx.getType().getTemplate().getName();
               } else {
                  inventoryMouseOver = Text.getText("playSelectedMobPanel.inventorySlot.chest");
               }
            }
         }

         var29 += 27;
         if (selectedMobFlags.getCanInventoryEquipTool()) {
            this.inventoryIcon3.draw(this.x + var29 + 398, this.y + var21 + 59);
            this.iconToolSlot.render(this.g, this.x + var29 + 402, this.y + var21 + 62, debug);
            ResourceBase highlightSlotxx = selectedMob.getInventoryToolSlot();
            if (highlightSlotxx != null) {
               this.inventoryIconHighlight3.draw(this.x + var29 + 398, this.y + var21 + 59);
               highlightSlotxx.renderSprite(this.x + var29 + 402, this.y + var21 + 62, true);
            }

            if (this.iconToolSlot.intersects(this.mouse)) {
               if (highlightSlotxx != null) {
                  inventoryMouseOver = "$CYA1" + highlightSlotxx.getType().getTemplate().getName();
               } else {
                  inventoryMouseOver = Text.getText("playSelectedMobPanel.inventorySlot.tool");
               }
            }
         }

         var29 += 27;
         if (selectedMobFlags.getCanInventoryEquipMeleeWeapon()) {
            this.inventoryIcon4.draw(this.x + var29 + 398, this.y + var21 + 59);
            this.iconMeleeWeaponSlot.render(this.g, this.x + var29 + 402, this.y + var21 + 62, debug);
            ResourceBase highlightSlotxxx = selectedMob.getInventoryMeleeWeaponSlot();
            if (highlightSlotxxx != null) {
               this.inventoryIconHighlight4.draw(this.x + var29 + 398, this.y + var21 + 59);
               highlightSlotxxx.renderSprite(this.x + var29 + 402, this.y + var21 + 62, true);
            }

            if (this.iconMeleeWeaponSlot.intersects(this.mouse)) {
               if (highlightSlotxxx != null) {
                  inventoryMouseOver = "$CYA1" + highlightSlotxxx.getType().getTemplate().getName();
               } else {
                  inventoryMouseOver = Text.getText("playSelectedMobPanel.inventorySlot.weapon");
               }
            }
         }

         var29 += 27;
         if (selectedMobFlags.getCanInventoryEquipShield()) {
            this.inventoryIcon5.draw(this.x + var29 + 398, this.y + var21 + 59);
            this.iconShieldSlot.render(this.g, this.x + var29 + 402, this.y + var21 + 62, debug);
            ResourceBase highlightSlotxxxx = selectedMob.getInventoryShieldSlot();
            if (highlightSlotxxxx != null) {
               this.inventoryIconHighlight5.draw(this.x + var29 + 398, this.y + var21 + 59);
               highlightSlotxxxx.renderSprite(this.x + var29 + 402, this.y + var21 + 62, true);
            }

            if (this.iconShieldSlot.intersects(this.mouse)) {
               if (highlightSlotxxxx != null) {
                  inventoryMouseOver = "$CYA1" + highlightSlotxxxx.getType().getTemplate().getName();
               } else {
                  inventoryMouseOver = Text.getText("playSelectedMobPanel.inventorySlot.shield");
               }
            }
         }

         int var34 = 0;
         var21 += 27;
         if (selectedMobFlags.getCanInventoryEquipRecovery()) {
            this.inventoryIcon6.draw(this.x + var34 + 398, this.y + var21 + 59);
            this.iconRecoverySlot.render(this.g, this.x + var34 + 402, this.y + var21 + 62, debug);
            ResourceBase highlightSlotxxxxx = selectedMob.getInventoryRecoverySlot();
            if (highlightSlotxxxxx != null) {
               this.inventoryIconHighlight6.draw(this.x + var34 + 398, this.y + var21 + 59);
               highlightSlotxxxxx.renderSprite(this.x + var34 + 402, this.y + var21 + 62, true);
            }

            if (this.iconRecoverySlot.intersects(this.mouse)) {
               if (highlightSlotxxxxx != null) {
                  inventoryMouseOver = "$CYA1" + highlightSlotxxxxx.getType().getTemplate().getName();
               } else {
                  inventoryMouseOver = Text.getText("playSelectedMobPanel.inventorySlot.recovery");
               }
            }
         }

         var34 += 27;
         if (selectedMobFlags.getCanInventoryEquipWater()) {
            this.inventoryIcon7.draw(this.x + var34 + 398, this.y + var21 + 59);
            this.iconWaterSlot.render(this.g, this.x + var34 + 402, this.y + var21 + 62, debug);
            ResourceBase highlightSlotxxxxxx = selectedMob.getInventoryWaterSlot();
            if (highlightSlotxxxxxx != null) {
               this.inventoryIconHighlight7.draw(this.x + var34 + 398, this.y + var21 + 59);
               highlightSlotxxxxxx.renderSprite(this.x + var34 + 402, this.y + var21 + 62, true);
            }

            if (this.iconWaterSlot.intersects(this.mouse)) {
               if (highlightSlotxxxxxx != null) {
                  inventoryMouseOver = "$CYA1" + highlightSlotxxxxxx.getType().getTemplate().getName();
               } else {
                  inventoryMouseOver = Text.getText("playSelectedMobPanel.inventorySlot.water");
               }
            }
         }

         var34 += 27;
         if (selectedMobFlags.getCanInventoryEquipFood()) {
            this.inventoryIcon8.draw(this.x + var34 + 398, this.y + var21 + 59);
            this.iconFoodSlot.render(this.g, this.x + var34 + 402, this.y + var21 + 62, debug);
            ResourceBase highlightSlotxxxxxxx = selectedMob.getInventoryFoodSlot();
            if (highlightSlotxxxxxxx != null) {
               this.inventoryIconHighlight8.draw(this.x + var34 + 398, this.y + var21 + 59);
               highlightSlotxxxxxxx.renderSprite(this.x + var34 + 402, this.y + var21 + 62, true);
            }

            if (this.iconFoodSlot.intersects(this.mouse)) {
               if (highlightSlotxxxxxxx != null) {
                  inventoryMouseOver = "$CYA1" + highlightSlotxxxxxxx.getType().getTemplate().getName();
               } else {
                  inventoryMouseOver = Text.getText("playSelectedMobPanel.inventorySlot.food");
               }
            }
         }

         var34 += 27;
         if (selectedMobFlags.getCanInventoryEquipRangedWeapon()) {
            this.inventoryIcon9.draw(this.x + var34 + 398, this.y + var21 + 59);
            this.iconRangedWeaponSlot.render(this.g, this.x + var34 + 402, this.y + var21 + 62, debug);
            ResourceBase highlightSlotxxxxxxxx = selectedMob.getInventoryRangedWeaponSlot();
            if (highlightSlotxxxxxxxx != null) {
               this.inventoryIconHighlight9.draw(this.x + var34 + 398, this.y + var21 + 59);
               highlightSlotxxxxxxxx.renderSprite(this.x + var34 + 402, this.y + var21 + 62, true);
            }

            if (this.iconRangedWeaponSlot.intersects(this.mouse)) {
               if (highlightSlotxxxxxxxx != null) {
                  inventoryMouseOver = "$CYA1" + highlightSlotxxxxxxxx.getType().getTemplate().getName();
               } else {
                  inventoryMouseOver = Text.getText("playSelectedMobPanel.inventorySlot.rangedWeapon");
               }
            }
         }

         var34 += 27;
         if (selectedMobFlags.getCanInventoryEquipRangedWeaponAmmo()) {
            this.inventoryIcon10.draw(this.x + var34 + 398, this.y + var21 + 59);
            this.iconRangedWeaponAmmoSlot.render(this.g, this.x + var34 + 402, this.y + var21 + 62, debug);
            ResourceBase highlightSlotxxxxxxxxx = selectedMob.getInventoryRangedWeaponAmmoSlot();
            if (highlightSlotxxxxxxxxx != null) {
               this.inventoryIconHighlight10.draw(this.x + var34 + 398, this.y + var21 + 59);
               highlightSlotxxxxxxxxx.renderSprite(this.x + var34 + 402, this.y + var21 + 62, true);
            }

            if (this.iconRangedWeaponAmmoSlot.intersects(this.mouse)) {
               if (highlightSlotxxxxxxxxx != null) {
                  inventoryMouseOver = "$CYA1" + highlightSlotxxxxxxxxx.getType().getTemplate().getName();
               } else {
                  inventoryMouseOver = Text.getText("playSelectedMobPanel.inventorySlot.rangedWeaponAmmo");
               }
            }
         }

         if (selectedMobFlags.getAllowCombat()) {
            ResourceBase rangedWeapon = selectedMob.getInventoryRangedWeaponSlot();
            ResourceBase ammo = selectedMob.getInventoryRangedWeaponAmmoSlot();
            ResourceBase weapon = selectedMob.getInventoryMeleeWeaponSlot();
            ResourceBase tool = selectedMob.getInventoryToolSlot();
            if (selectedMob.canRangedAttack()) {
               if (weapon != null) {
                  Text.setVariableText("damageType", weapon.getType().getTemplate().getDamageType().getText());
                  this.font.drawString(this.x + 462, this.y + 110, Text.getText("playSelectedMobPanel.meleeDamage"), Text.FontType.BODY, 0, true);
               } else if (tool != null) {
                  Text.setVariableText("damageType", tool.getType().getTemplate().getDamageType().getText());
                  this.font.drawString(this.x + 462, this.y + 110, Text.getText("playSelectedMobPanel.meleeDamage"), Text.FontType.BODY, 0, true);
               } else {
                  Text.setVariableText("damageType", selectedMobFlags.getCombatDamageType().getText());
                  this.font.drawString(this.x + 462, this.y + 110, Text.getText("playSelectedMobPanel.meleeDamage"), Text.FontType.BODY, 0, true);
               }

               if (rangedWeapon != null && rangedWeapon.getType().getTemplate().getRangedWeaponRequiredAmmo() == null) {
                  Text.setVariableText("damageType", rangedWeapon.getType().getTemplate().getDamageType().getText());
                  this.font.drawString(this.x + 462, this.y + 120, Text.getText("playSelectedMobPanel.rangedDamage"), Text.FontType.BODY, 0, true);
               } else if (rangedWeapon != null && ammo != null && rangedWeapon.getType().getTemplate().getRangedWeaponRequiredAmmo() == ammo.getType()) {
                  MobBase.DamageType type = (MobBase.DamageType)this.missile
                     .getMissileFactory()
                     .getMissileInstance(rangedWeapon.getType().getTemplate().getRangedWeaponMissileType())
                     .getDamageTypesMax()
                     .keySet()
                     .toArray()[0];
                  Text.setVariableText("damageType", type.getText());
                  this.font.drawString(this.x + 462, this.y + 120, Text.getText("playSelectedMobPanel.rangedDamage"), Text.FontType.BODY, 0, true);
               } else {
                  Text.setVariableText("damageType", selectedMobFlags.getCombatDamageType().getText());
                  this.font.drawString(this.x + 462, this.y + 120, Text.getText("playSelectedMobPanel.rangedDamage"), Text.FontType.BODY, 0, true);
               }
            } else if (weapon != null) {
               Text.setVariableText("damageType", weapon.getType().getTemplate().getDamageType().getText());
               this.font.drawString(this.x + 462, this.y + 114, Text.getText("playSelectedMobPanel.meleeDamage"), Text.FontType.BODY, 1, true);
            } else if (tool != null) {
               Text.setVariableText("damageType", tool.getType().getTemplate().getDamageType().getText());
               this.font.drawString(this.x + 462, this.y + 114, Text.getText("playSelectedMobPanel.meleeDamage"), Text.FontType.BODY, 1, true);
            } else {
               Text.setVariableText("damageType", selectedMobFlags.getCombatDamageType().getText());
               this.font.drawString(this.x + 462, this.y + 114, Text.getText("playSelectedMobPanel.meleeDamage"), Text.FontType.BODY, 1, true);
            }
         }

         if (inventoryMouseOver != null) {
            this.inventoryToolTip.renderToolTip(this.g, this.mouse, GUIToolTipOverlay.ToolTipPosition.RIGHT, inventoryMouseOver, debug);
         }

         String statusAndTask = "";
         if (selectedMob.isBlighted()) {
            statusAndTask = statusAndTask + Text.getText("playSelectedMobPanel.statusEffect.blighted");
         }

         if (selectedMob.isPoisoned()) {
            statusAndTask = statusAndTask + Text.getText("playSelectedMobPanel.statusEffect.poison");
         }

         if (selectedMob.isDysentery()) {
            statusAndTask = statusAndTask + Text.getText("playSelectedMobPanel.statusEffect.dysentery");
         }

         if (selectedMob.isOnFire()) {
            statusAndTask = statusAndTask + Text.getText("playSelectedMobPanel.statusEffect.onFire");
         }

         if (selectedMob.isFrozen()) {
            statusAndTask = statusAndTask + Text.getText("playSelectedMobPanel.statusEffect.frozen");
         }

         if (!statusAndTask.equals("")) {
            statusAndTask = statusAndTask.substring(0, statusAndTask.length() - 2);
            statusAndTask = statusAndTask + Text.getText("playSelectedMobPanel.statusEffect.conjuntion");
         }

         this.font.drawString(this.x + 38, this.y + 134, statusAndTask + "$BLU0" + selectedMob.getCurrentTask(), Text.FontType.BODY, 0);
      } else {
         this.controller.deselectAllSelected();
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
