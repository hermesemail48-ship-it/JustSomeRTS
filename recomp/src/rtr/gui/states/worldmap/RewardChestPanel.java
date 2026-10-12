package rtr.gui.states.worldmap;

import java.text.DecimalFormat;
import java.util.EnumMap;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.GodModule;
import rtr.ImageLoader;
import rtr.ModuleBase;
import rtr.chest.Chest;
import rtr.chest.ChestModule;
import rtr.font.Text;
import rtr.gui.buttons.GUIClickableArea;
import rtr.gui.buttons.GUIIcon;
import rtr.gui.states.WorldMapGUIController;
import rtr.gui.states.WorldMapGUIData;
import rtr.gui.states.WorldMapGUIPanelBase;
import rtr.particles.ParticleModule;
import rtr.states.StateBase;
import rtr.system.ScaleControl;
import rtr.utilities.Utilities;

public class RewardChestPanel extends WorldMapGUIPanelBase {
   private ChestModule chest = (ChestModule)StateBase.getModule(ModuleBase.ModuleType.CHEST);
   private GodModule god = (GodModule)StateBase.getModule(ModuleBase.ModuleType.GOD);
   private Image panel = ImageLoader.getImage("res/GUI/gameState/rewardChestPanel.png");
   private Image chestSlot = ImageLoader.getImage("res/GUI/gameState/rewardChestBlank.png");
   private Rectangle mask = new Rectangle(0.0F, 0.0F, this.panel.getWidth(), this.panel.getHeight());
   private GUIIcon iconGodXP;
   private GUIClickableArea chestBarArea;
   private GUIClickableArea chestBarFullArea;

   public RewardChestPanel(WorldMapGUIController controller, WorldMapGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.masks.add(this.mask);
      this.iconGodXP = new GUIIcon("iconGodXP");
      this.iconGodXP.setToolTip(Text.getText("worldMapRewardChestPanel.godExperienceHeader"), Text.getText("worldMapRewardChestPanel.godExperience"));
      this.guiButtons.add(this.iconGodXP);
      this.chestBarArea = new GUIClickableArea("chestBarArea", 316, 11);
      this.chestBarArea
         .setToolTip(Text.getText("worldMapRewardChestPanel.chestGrantProgressHeader"), Text.getText("worldMapRewardChestPanel.chestGrantProgress"));
      this.guiButtons.add(this.chestBarArea);
      this.chestBarFullArea = new GUIClickableArea("chestBarFullArea", 316, 11);
      this.chestBarFullArea
         .setToolTip(Text.getText("worldMapRewardChestPanel.chestGrantProgressHeader"), Text.getText("worldMapRewardChestPanel.chestGrantProgressFull"));
      this.guiButtons.add(this.chestBarFullArea);
   }

   public void render(boolean debug) throws SlickException {
      this.x = ScaleControl.getInterfaceWidth() / 2 - 367;
      this.y = 18;
      this.mask.setX(this.x);
      this.mask.setY(this.y);
      if (this.chest.isChestsFull()) {
         this.g.setColor(new Color(255, 255, 255));
         this.g.fillRect(this.x + 27, this.y + this.panel.getHeight() - 17, 316.0F, 11.0F);
      } else {
         int chestBarLength = (int)Math.ceil((float)this.chest.getNextChestCounter() / (this.time.getFullDayLength() * 2) * 316.0F);
         this.g.setColor(new Color(5, 5, 5));
         this.g.fillRect(this.x + 27, this.y + this.panel.getHeight() - 17, 316.0F, 11.0F);
         this.g.setColor(new Color(0, 255, 200));
         this.g.fillRect(this.x + 27, this.y + this.panel.getHeight() - 17, chestBarLength, 11.0F);
         if (this.chest.getChestXP() > 0) {
            for (int i = 0; i < 10; i++) {
               this.particle
                  .newParticle(
                     this.x + 27 + chestBarLength + Utilities.randomInt(2),
                     this.y + this.panel.getHeight() - 17 + Utilities.randomInt(11),
                     ParticleModule.ParticleType.FIRE_SPECK,
                     ParticleModule.ParticleSet.FIRE_BLUE,
                     true
                  );
            }

            for (int i = 0; i < 5; i++) {
               this.particle
                  .newParticle(
                     this.x + 27 + chestBarLength + Utilities.randomInt(2),
                     this.y + this.panel.getHeight() - 17 + Utilities.randomInt(11),
                     ParticleModule.ParticleType.MAGIC_SMOKE_SPECK,
                     ParticleModule.ParticleSet.FIRE_BLUE,
                     true
                  );
            }
         }
      }

      this.panel.draw(this.x, this.y);
      if (this.chest.isChestsFull()) {
         this.chestBarFullArea.render(this.g, this.mouse, this.x + 24, this.y + this.panel.getHeight() - 20, false, debug);
      } else {
         this.chestBarArea.render(this.g, this.mouse, this.x + 24, this.y + this.panel.getHeight() - 20, false, debug);
      }

      int alignX = 0;
      Chest[] chestList = this.chest.getChestArray();
      int var13 = 0;

      for (int i = 0; i < chestList.length; i++) {
         if (chestList[i] == null || chestList[i].isOpening() || chestList[i].isOpen()) {
            this.chestSlot.draw(this.x + 16 + var13, this.y + 11);
         }

         var13 += 52;
      }

      var13 = 0;

      for (int i = 0; i < chestList.length; i++) {
         if (chestList[i] != null && !chestList[i].isOpening() && !chestList[i].isOpen()) {
            chestList[i].renderEffects(this.g, this.mouse, this.x + 16 + var13, this.y + 11);
         }

         var13 += 52;
      }

      var13 = 0;

      for (int i = 0; i < chestList.length; i++) {
         if (chestList[i] != null && !chestList[i].isOpening() && !chestList[i].isOpen()) {
            chestList[i].render(this.g, this.mouse, this.x + 16 + var13, this.y + 11, debug);
         }

         var13 += 52;
      }

      var13 = 0;

      for (int i = 0; i < chestList.length; i++) {
         if (chestList[i] != null && !chestList[i].isOpening() && !chestList[i].isOpen()) {
            chestList[i].renderOpenCost(this.mouse, this.x + 16 + var13, this.y + 11);
         }

         var13 += 52;
      }

      this.iconGodXP.render(this.g, this.x + this.panel.getWidth() - 93, this.y + this.panel.getHeight() - 19, debug);
      this.font
         .drawString(
            this.x + this.panel.getWidth() - 78, this.y + this.panel.getHeight() - 19, "$BLU0" + (int)this.god.getGodXPWorldMapCount(), Text.FontType.BODY, 1
         );
      if (this.chest.getChestXP() < 10) {
         int alignY = 0;
         int count = 0;
         EnumMap<GodModule.GodXPType, Double> godXPByType = this.god.getGodXPByType();

         for (GodModule.GodXPType g : godXPByType.keySet()) {
            if (godXPByType.get(g) > 0.0) {
               double amount = godXPByType.get(g);
               if (Utilities.randomInt(4) == 0) {
                  this.particle
                     .newParticle(
                        this.x + 359 + Utilities.randomInt(-4, 4),
                        this.y + this.panel.getHeight() - 9 + Utilities.randomInt(-4, 4),
                        ParticleModule.ParticleType.MAGIC_SMOKE_SPECK,
                        ParticleModule.ParticleSet.FIRE_GREEN,
                        true
                     );
               }

               if (Utilities.randomInt(2) == 0) {
                  this.particle
                     .newParticle(
                        this.x + 359 + Utilities.randomInt(-1, 1),
                        this.y + this.panel.getHeight() - 9 + Utilities.randomInt(-1, 1),
                        ParticleModule.ParticleType.MAGIC_SMOKE_SPECK,
                        ParticleModule.ParticleSet.FIRE_BLUE,
                        true
                     );
               }

               String amountOut = new DecimalFormat("#.##").format(amount);
               this.font.drawString(this.x + this.panel.getWidth() - 90, this.y + this.panel.getHeight() + alignY, "+" + amountOut, Text.FontType.BODY, 1);
               this.font.drawString(this.x + this.panel.getWidth() - 40, this.y + this.panel.getHeight() + alignY, g.getName(), Text.FontType.BODY, 1);
               alignY += 16;
               if (++count > 8) {
                  break;
               }
            }
         }
      }
   }

   @Override
   public void update() {
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
