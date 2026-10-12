package rtr.chest;

import java.util.Map;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.GodModule;
import rtr.ImageLoader;
import rtr.ModuleBase;
import rtr.SoundModule;
import rtr.font.FontModule;
import rtr.font.Text;
import rtr.particles.ParticleModule;
import rtr.particles.projectiles.ProjectileModule;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.system.ScaleControl;
import rtr.utilities.Utilities;

public class Chest {
   private FontModule font = (FontModule)StateBase.getModule(ModuleBase.ModuleType.FONT);
   private ParticleModule particle = (ParticleModule)StateBase.getModule(ModuleBase.ModuleType.PARTICLE);
   private ProjectileModule projectile = (ProjectileModule)StateBase.getModule(ModuleBase.ModuleType.PROJECTILE);
   private SoundModule sound = (SoundModule)StateBase.getModule(ModuleBase.ModuleType.SOUND);
   private GodModule god = (GodModule)StateBase.getModule(ModuleBase.ModuleType.GOD);
   private static final int CHEST_BASE_COST = 1100;
   private Image chestClosed;
   private Image chestOpened;
   private Image iconGodXP;
   private int chestLevel;
   private int openCost;
   private int perksAmount;
   private int godItemsAmount;
   private Chest.RewardChestOpenState currentState = Chest.RewardChestOpenState.LOCKED;
   private boolean alphaFlip;
   private int alphaLevel;
   private Rectangle intersectBox;
   private float rotation = 0.0F;
   private int openingStep;
   private int animationTick;
   private float beamLengthMod;
   private int backgroundAlphaBlack = 0;
   private int backgroundAlphaWhite = 0;
   private boolean whiteFlip;

   public Chest(int baseLevel) throws SlickException {
      this.chestLevel = baseLevel;

      for (int x = baseLevel; x < 4 && Utilities.randomInt(3) == 0; x++) {
         this.chestLevel++;
      }

      this.openCost = 1100 * this.chestLevel + (Utilities.randomInt(250) + 1) * this.chestLevel;

      for (int x = 0; x < this.chestLevel; x++) {
         this.openCost = (int)(this.openCost * 0.9F);
      }

      this.openCost = (int)(100 - this.openCost % 100 + this.openCost);
      this.perksAmount = this.chestLevel;
      this.perksAmount = this.perksAmount + Utilities.randomInt(this.chestLevel + 1);
      this.godItemsAmount = this.chestLevel;
      this.godItemsAmount = this.godItemsAmount + Utilities.randomInt(this.chestLevel + 1);
      this.finalizeChestConstructor();
   }

   public Chest(YMLDataMap dataMap) throws SlickException {
      this.chestLevel = Integer.parseInt(dataMap.getOrDefault("chestLevel", "1"));
      this.openCost = Integer.parseInt(dataMap.getOrDefault("openCost", "1"));
      this.perksAmount = Integer.parseInt(dataMap.getOrDefault("perksAmount", "1"));
      this.godItemsAmount = Integer.parseInt(dataMap.getOrDefault("godItemsAmount", "0"));
      this.finalizeChestConstructor();
   }

   private void finalizeChestConstructor() throws SlickException {
      this.chestClosed = ImageLoader.getImage("res/GUI/gameState/rewardLevel" + this.chestLevel + "Chest1.png");
      this.chestOpened = ImageLoader.getImage("res/GUI/gameState/rewardLevel" + this.chestLevel + "ChestOpen1.png");
      this.iconGodXP = ImageLoader.getImage("res/GUI/icon/iconGodXP.png");
      this.alphaLevel = Utilities.randomInt(250) + 1;
      if (this.god.getGodXP() >= this.openCost) {
         this.currentState = Chest.RewardChestOpenState.CAN_OPEN;
      } else {
         this.currentState = Chest.RewardChestOpenState.LOCKED;
      }

      this.intersectBox = new Rectangle(0.0F, 0.0F, this.chestClosed.getWidth() - 6, this.chestClosed.getHeight() - 6);
   }

   public void render(Graphics g, Rectangle mouse, int x, int y, boolean debug) throws SlickException {
      this.intersectBox.setX(x + 3);
      this.intersectBox.setY(y + 3);
      if (this.god.getGodXP() >= this.openCost && this.currentState == Chest.RewardChestOpenState.LOCKED) {
         this.currentState = Chest.RewardChestOpenState.CAN_OPEN;
      } else if (this.god.getGodXP() < this.openCost && this.currentState == Chest.RewardChestOpenState.CAN_OPEN) {
         this.currentState = Chest.RewardChestOpenState.LOCKED;
      }

      switch (this.currentState) {
         case APPEAR:
            this.renderAppear(mouse, x, y);
            break;
         case LOCKED:
            this.renderLocked(mouse, x, y);
            break;
         case CAN_OPEN:
            this.renderCanOpen(g, mouse, x, y);
            break;
         case OPEN:
            this.renderOpen(g, mouse, x, y);
            break;
         case OPENING:
            this.renderOpening(g, mouse, x, y);
      }

      if (debug) {
         g.setColor(Color.cyan);
         g.draw(this.intersectBox);
      }
   }

   private void renderAppear(Rectangle mouse, int x, int y) throws SlickException {
      this.animationTick++;
      if (this.beamLengthMod < 32.0F) {
         this.beamLengthMod += 0.15F;
      }

      switch (this.openingStep) {
         case 0:
            this.sound.playSound(SoundModule.SoundType.HOLY_SOFT, x, y);
            this.projectile
               .newConstrictingFireRing(
                  x + this.chestClosed.getWidth() / 2, y + this.chestClosed.getHeight() / 2, 128, 3.0F, ParticleModule.ParticleSet.MAGIC_GREEN, true
               );
            this.animationTick = 0;
            this.openingStep++;
            break;
         case 1:
            if (this.animationTick > 40) {
               this.openingStep++;
            }
            break;
         case 2:
            for (int z = 0; z < 500; z++) {
               for (int i = 0; i < 43; i++) {
                  if (Utilities.randomInt(20) == 0) {
                     this.particle
                        .newParticle(
                           x + 4 + i - Utilities.randomInt(-4, 4),
                           y + 23 - Utilities.randomInt(-16, 16),
                           180 + Utilities.randomInt(24),
                           ParticleModule.ParticleType.SMOKE_SPECK,
                           ParticleModule.ParticleSet.MAGIC_BLUE,
                           true
                        );
                     this.particle
                        .newParticle(
                           x + 4 + i - Utilities.randomInt(-4, 4),
                           y + 23 - Utilities.randomInt(-16, 16),
                           360 - Utilities.randomInt(24),
                           ParticleModule.ParticleType.SMOKE_SPECK,
                           ParticleModule.ParticleSet.MAGIC_BLUE,
                           true
                        );
                  }

                  if (Utilities.randomInt(6) == 0) {
                     this.particle
                        .newParticle(
                           x + 4 + i - Utilities.randomInt(-4, 4),
                           y + 23 - Utilities.randomInt(-16, 16),
                           180 + Utilities.randomInt(24),
                           ParticleModule.ParticleType.MAGIC_SMOKE_SPECK,
                           ParticleModule.ParticleSet.FIRE_BLUE,
                           true
                        );
                     this.particle
                        .newParticle(
                           x + 4 + i - Utilities.randomInt(-4, 4),
                           y + 23 - Utilities.randomInt(-16, 16),
                           360 - Utilities.randomInt(24),
                           ParticleModule.ParticleType.MAGIC_SMOKE_SPECK,
                           ParticleModule.ParticleSet.FIRE_BLUE,
                           true
                        );
                  }
               }
            }

            this.openingStep++;
            break;
         case 3:
            if (this.animationTick > 50) {
               if (this.god.getGodXP() >= this.openCost) {
                  this.currentState = Chest.RewardChestOpenState.CAN_OPEN;
               } else {
                  this.currentState = Chest.RewardChestOpenState.LOCKED;
               }

               this.openingStep = 0;
               this.animationTick = 0;
            }
      }
   }

   private void renderLocked(Rectangle mouse, int x, int y) {
      this.chestClosed.draw(x, y);
      int fontWidth = Text.getWidth(String.valueOf(this.openCost), Text.FontType.BODY, 1) / 2;
      this.iconGodXP.draw(x + this.chestClosed.getWidth() / 2 - fontWidth - 14, y + 38);
      if (this.intersectBox.intersects(mouse)) {
         this.font.drawString(x + this.chestClosed.getWidth() / 2, y + 38, "$GRE0" + this.openCost, Text.FontType.BODY, 1, true);
      } else {
         this.font.drawString(x + this.chestClosed.getWidth() / 2, y + 38, "$YEL0" + this.openCost, Text.FontType.BODY, 1, true);
      }

      for (int i = 0; i < 43; i++) {
         if (i == 18) {
            i += 9;
         } else if (Utilities.randomInt(150) == 0) {
            this.particle.newParticleGroup(x + 4 + i, y + 22, ParticleModule.ParticleGroup.FALL, ParticleModule.ParticleSet.FIRE_YELLOW, true);
         }
      }
   }

   private void renderCanOpen(Graphics g, Rectangle mouse, int x, int y) {
      this.chestClosed.draw(x, y);
      GL11.glBlendFunc(774, 770);
      if (this.intersectBox.intersects(mouse)) {
         this.chestClosed.drawFlash(x, y, this.chestClosed.getWidth(), this.chestClosed.getHeight(), Color.white);
      } else {
         this.chestClosed.drawFlash(x, y, this.chestClosed.getWidth(), this.chestClosed.getHeight(), new Color(255, 255, 255, this.alphaLevel));
      }

      g.setDrawMode(Graphics.MODE_NORMAL);
      this.alphaLevel = this.alphaLevel + (this.alphaFlip ? -5 : 5);
      if (this.alphaLevel > 150) {
         this.alphaFlip = true;
      }

      if (this.alphaLevel < 0) {
         this.alphaFlip = false;
      }

      for (int i = 0; i < 43; i++) {
         if (i == 18) {
            i += 9;
         } else if (Utilities.randomInt(40) == 0) {
            this.particle.newParticleGroup(x + 4 + i, y + 22, ParticleModule.ParticleGroup.FALL, ParticleModule.ParticleSet.FIRE_YELLOW, true);
         }
      }
   }

   private void renderOpening(Graphics g, Rectangle mouse, int x, int y) throws SlickException {
      this.animationTick++;
      this.chestClosed.draw(x, y);
      GL11.glBlendFunc(774, 770);
      if (this.intersectBox.intersects(mouse)) {
         this.chestClosed.drawFlash(x, y, this.chestClosed.getWidth(), this.chestClosed.getHeight(), Color.white);
      } else {
         this.chestClosed.drawFlash(x, y, this.chestClosed.getWidth(), this.chestClosed.getHeight(), new Color(255, 255, 255, this.alphaLevel));
      }

      g.setDrawMode(Graphics.MODE_NORMAL);
      if (this.beamLengthMod < 32.0F) {
         this.beamLengthMod += 0.15F;
      }

      switch (this.openingStep) {
         case 0:
            this.sound.playSound(SoundModule.SoundType.LOOT_BOX_OPEN, x, y);
            this.projectile
               .newConstrictingFireRing(
                  x + this.chestClosed.getWidth() / 2, y + this.chestClosed.getHeight() / 2, 128, 3.0F, ParticleModule.ParticleSet.MAGIC_GREEN, true
               );
            this.animationTick = 0;
            this.openingStep++;
            break;
         case 1:
            if (this.alphaLevel < 250) {
               this.alphaLevel += 5;
            }

            if (this.animationTick > 100) {
               this.sound.playSound(SoundModule.SoundType.HOLY_LOUD);
               this.projectile
                  .newExpandingEnergyRing(
                     x + this.chestClosed.getWidth() / 2, y + this.chestClosed.getHeight() / 2, 128, 0.25F, ParticleModule.ParticleSet.WHITE, true
                  );
               this.animationTick = 0;
               this.openingStep++;
            }
            break;
         case 2:
            if (this.animationTick == 70) {
               this.sound.playSound(SoundModule.SoundType.HEAVY_WIND);
            }

            if (this.animationTick > 100) {
               this.animationTick = 0;
               this.openingStep++;
               this.projectile
                  .newExpandingEnergyRing(
                     x + this.chestClosed.getWidth() / 2, y + this.chestClosed.getHeight() / 2, 768, 2.0F, ParticleModule.ParticleSet.FIRE_BLUE, true
                  );
               this.projectile
                  .newExpandingEnergyRing(
                     x + this.chestClosed.getWidth() / 2, y + this.chestClosed.getHeight() / 2, 768, 3.0F, ParticleModule.ParticleSet.MAGIC_GREEN, true
                  );
               this.projectile
                  .newExpandingEnergyRing(
                     x + this.chestClosed.getWidth() / 2, y + this.chestClosed.getHeight() / 2, 512, 3.5F, ParticleModule.ParticleSet.FIRE_BLUE, true
                  );
               this.projectile
                  .newExpandingEnergyRing(
                     x + this.chestClosed.getWidth() / 2, y + this.chestClosed.getHeight() / 2, 256, 4.5F, ParticleModule.ParticleSet.MAGIC_GREEN, true
                  );
               this.projectile
                  .newExpandingEnergyRing(
                     x + this.chestClosed.getWidth() / 2, y + this.chestClosed.getHeight() / 2, 256, 10.0F, ParticleModule.ParticleSet.FIRE_BLUE, true
                  );
            }
            break;
         case 3:
            this.openingStep = 0;
            this.animationTick = 0;
            this.currentState = Chest.RewardChestOpenState.OPEN;
      }
   }

   private void renderOpen(Graphics g, Rectangle mouse, int x, int y) {
      this.chestOpened.draw(x, y);
      GL11.glBlendFunc(774, 770);
      if (this.intersectBox.intersects(mouse)) {
         this.chestOpened.drawFlash(x, y, this.chestOpened.getWidth(), this.chestOpened.getHeight(), Color.white);
      } else {
         this.chestOpened.drawFlash(x, y, this.chestOpened.getWidth(), this.chestOpened.getHeight(), new Color(255, 255, 255, this.alphaLevel));
      }

      g.setDrawMode(Graphics.MODE_NORMAL);
      this.alphaLevel = this.alphaLevel + (this.alphaFlip ? -5 : 5);
      if (this.alphaLevel > 150) {
         this.alphaFlip = true;
      }

      if (this.alphaLevel < 0) {
         this.alphaFlip = false;
      }

      if (this.beamLengthMod > 0.0F) {
         this.beamLengthMod -= 0.05F;
      }

      if (this.beamLengthMod < 0.0F) {
         this.beamLengthMod = 0.0F;
      }

      for (int i = 0; i < 43; i++) {
         if (i == 18) {
            i += 9;
         } else if (Utilities.randomInt(40) == 0) {
            this.particle.newParticleGroup(x + 4 + i, y + 22, ParticleModule.ParticleGroup.FALL, ParticleModule.ParticleSet.FIRE_YELLOW, true);
         }
      }

      for (int ix = 0; ix < 43; ix++) {
         if (Utilities.randomInt(30) == 0) {
            this.particle
               .newParticleGroup(
                  x + 4 + ix - Utilities.randomInt(-4, 4),
                  y + 22,
                  270 + Utilities.randomInt(-24, 24),
                  ParticleModule.ParticleGroup.MAGIC_SMOKE,
                  ParticleModule.ParticleSet.FIRE_BLUE,
                  ParticleModule.ParticleSet.FIRE_BLUE,
                  true
               );
         }

         if (Utilities.randomInt(60) == 0) {
            this.particle
               .newParticleGroup(
                  x + 4 + ix - Utilities.randomInt(-4, 4),
                  y + 23 - Utilities.randomInt(8),
                  180 + Utilities.randomInt(24),
                  ParticleModule.ParticleGroup.MAGIC_SMOKE,
                  ParticleModule.ParticleSet.FIRE_BLUE,
                  ParticleModule.ParticleSet.FIRE_BLUE,
                  true
               );
            this.particle
               .newParticleGroup(
                  x + 4 + ix - Utilities.randomInt(-4, 4),
                  y + 23 - Utilities.randomInt(8),
                  360 - Utilities.randomInt(24),
                  ParticleModule.ParticleGroup.MAGIC_SMOKE,
                  ParticleModule.ParticleSet.FIRE_BLUE,
                  ParticleModule.ParticleSet.FIRE_BLUE,
                  true
               );
            this.particle
               .newParticleGroup(
                  x + 4 + ix - Utilities.randomInt(-4, 4),
                  y + 23 - Utilities.randomInt(4),
                  180 + Utilities.randomInt(24),
                  ParticleModule.ParticleGroup.MAGIC_SMOKE,
                  ParticleModule.ParticleSet.FIRE_BLUE,
                  ParticleModule.ParticleSet.FIRE_BLUE,
                  true
               );
            this.particle
               .newParticleGroup(
                  x + 4 + ix - Utilities.randomInt(-4, 4),
                  y + 23 - Utilities.randomInt(4),
                  360 - Utilities.randomInt(24),
                  ParticleModule.ParticleGroup.MAGIC_SMOKE,
                  ParticleModule.ParticleSet.FIRE_BLUE,
                  ParticleModule.ParticleSet.FIRE_BLUE,
                  true
               );
         }
      }
   }

   public void renderOpenCost(Rectangle mouse, int x, int y) throws SlickException {
      int fontWidth = Text.getWidth(String.valueOf(this.openCost), Text.FontType.BODY, 1) / 2;
      this.iconGodXP.draw(x + this.chestOpened.getWidth() / 2 - fontWidth - 14, y + 38);
      if (this.intersectBox.intersects(mouse)) {
         this.font.drawString(x + this.chestClosed.getWidth() / 2, y + 38, "$GRE0" + this.openCost, Text.FontType.BODY, 1, true);
      } else {
         this.font.drawString(x + this.chestClosed.getWidth() / 2, y + 38, "$YEL0" + this.openCost, Text.FontType.BODY, 1, true);
      }
   }

   public void renderEffects(Graphics g, Rectangle mouse, int x, int y) {
      switch (this.currentState) {
         case LOCKED:
            this.renderEffectsLocked(g, mouse, x, y);
            break;
         case CAN_OPEN:
            this.renderEffectsCanOpen(g, mouse, x, y);
            break;
         case OPEN:
            this.renderEffectsOpen(g, x, y);
            break;
         case OPENING:
            this.renderEffectsOpening(g, x, y);
      }
   }

   private void renderEffectsLocked(Graphics g, Rectangle mouse, int x, int y) {
      float finalRotation = this.rotation;

      for (int d = 0; d < 2; d++) {
         if (d == 1) {
            finalRotation = -finalRotation * 1.5F;
         }

         for (int c = 0; c < 10; c++) {
            float length = 8 * c;
            if (d == 1) {
               length *= 1.25F;
            }

            if (this.intersectBox.intersects(mouse)) {
               g.setColor(new Color(155, 255, 255, 3));
            } else {
               g.setColor(new Color(155, 255, 255, 3));
            }

            for (int i = 0; i < 9; i++) {
               g.fillArc(
                  x - length / 2.0F + this.chestClosed.getWidth() / 2,
                  y - length / 2.0F + this.chestClosed.getHeight() / 2,
                  length,
                  length,
                  0.0F + finalRotation + i * 40,
                  20.0F + finalRotation + i * 40
               );
            }
         }
      }
   }

   private void renderEffectsCanOpen(Graphics g, Rectangle mouse, int x, int y) {
      float finalRotation = this.rotation;

      for (int d = 0; d < 2; d++) {
         if (d == 1) {
            finalRotation = -finalRotation * 1.5F;
         }

         for (int c = 0; c < 15; c++) {
            float length = 8 * c;
            if (d == 1) {
               length *= 1.25F;
            }

            if (this.intersectBox.intersects(mouse)) {
               if (d == 1) {
                  g.setColor(new Color(155, 255, 255, 5));
               } else {
                  g.setColor(new Color(55, 55, 255, 8));
               }
            } else if (d == 1) {
               g.setColor(new Color(155, 255, 255, 3));
            } else {
               g.setColor(new Color(55, 55, 255, 6));
            }

            for (int i = 0; i < 9; i++) {
               g.fillArc(
                  x - length / 2.0F + this.chestClosed.getWidth() / 2,
                  y - length / 2.0F + this.chestClosed.getHeight() / 2,
                  length,
                  length,
                  0.0F + finalRotation + i * 40,
                  20.0F + finalRotation + i * 40
               );
            }
         }
      }

      this.rotation += 0.25F;
      if (this.intersectBox.intersects(mouse)) {
         this.rotation += 0.15F;
      }
   }

   private void renderEffectsOpening(Graphics g, int x, int y) {
      if (this.backgroundAlphaBlack < 200) {
         this.backgroundAlphaBlack += 2;
      }

      g.setColor(new Color(0, 0, 0, this.backgroundAlphaBlack));
      g.fillRect(0.0F, 0.0F, ScaleControl.getInterfaceWidth(), ScaleControl.getInterfaceHeight());
      float finalRotation = this.rotation;

      for (int d = 0; d < 2; d++) {
         if (d == 1) {
            finalRotation = -finalRotation * 1.5F;
         }

         for (int c = 0; c < 15.0F + this.beamLengthMod / 8.0F; c++) {
            float length = (this.beamLengthMod + 8.0F) * c;
            if (d == 1) {
               length *= 1.25F;
            }

            if (d == 1) {
               g.setColor(new Color(155, 255, 255, 3));
            } else {
               g.setColor(new Color(55, 55, 255, 6));
            }

            for (int i = 0; i < 9; i++) {
               g.fillArc(
                  x - length / 2.0F + this.chestClosed.getWidth() / 2,
                  y - length / 2.0F + this.chestClosed.getHeight() / 2,
                  length,
                  length,
                  0.0F + finalRotation + i * 40,
                  20.0F + finalRotation + i * 40
               );
            }
         }
      }

      this.rotation += 0.3F;
   }

   private void renderEffectsOpen(Graphics g, int x, int y) {
      if (this.backgroundAlphaBlack > 0) {
         this.backgroundAlphaBlack -= 10;
      }

      if (this.whiteFlip) {
         if (this.backgroundAlphaWhite > 0) {
            this.backgroundAlphaWhite -= 2;
         }
      } else if (this.backgroundAlphaWhite < 200) {
         this.backgroundAlphaWhite += 5;
      } else {
         this.whiteFlip = true;
      }

      g.setColor(new Color(255, 255, 255, this.backgroundAlphaWhite));
      GL11.glBlendFunc(774, 770);
      g.fillRect(0.0F, 0.0F, ScaleControl.getInterfaceWidth(), ScaleControl.getInterfaceHeight());
      g.setDrawMode(Graphics.MODE_NORMAL);
      g.setColor(new Color(0, 0, 0, this.backgroundAlphaBlack));
      g.fillRect(0.0F, 0.0F, ScaleControl.getInterfaceWidth(), ScaleControl.getInterfaceHeight());
      float finalRotation = this.rotation;

      for (int d = 0; d < 2; d++) {
         if (d == 1) {
            finalRotation = -finalRotation * 1.5F;
         }

         for (int c = 0; c < 15.0F + this.beamLengthMod / 8.0F; c++) {
            float length = (this.beamLengthMod + 8.0F) * c;
            if (d == 1) {
               length *= 1.25F;
            }

            if (d == 1) {
               g.setColor(new Color(155, 255, 255, 3));
            } else {
               g.setColor(new Color(55, 55, 255, 6));
            }

            for (int i = 0; i < 9; i++) {
               g.fillArc(
                  x - length / 2.0F + this.chestClosed.getWidth() / 2,
                  y - length / 2.0F + this.chestClosed.getHeight() / 2,
                  length,
                  length,
                  0.0F + finalRotation + i * 40,
                  20.0F + finalRotation + i * 40
               );
            }
         }
      }

      this.rotation += 0.3F;
   }

   public int getOpenCost() {
      return this.openCost;
   }

   public void openChest() {
      this.currentState = Chest.RewardChestOpenState.OPENING;
   }

   public boolean isOpening() {
      return this.currentState == Chest.RewardChestOpenState.OPENING;
   }

   public boolean isOpen() {
      return this.currentState == Chest.RewardChestOpenState.OPEN;
   }

   public boolean intersects(Rectangle mouse) {
      return this.intersectBox.intersects(mouse);
   }

   public int getChestLevel() {
      return this.chestLevel;
   }

   public int getPerksAmount() {
      return this.perksAmount;
   }

   public int getGodItemsAmount() {
      return this.godItemsAmount;
   }

   public void makeAppear() {
      this.currentState = Chest.RewardChestOpenState.APPEAR;
   }

   public Map<String, Object> getWorldMapProperties() {
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("chestLevel", this.chestLevel);
      dataMap.put("openCost", this.openCost);
      dataMap.put("perksAmount", this.perksAmount);
      dataMap.put("godItemsAmount", this.godItemsAmount);
      return dataMap;
   }

   private static enum RewardChestOpenState {
      APPEAR,
      LOCKED,
      CAN_OPEN,
      OPEN,
      OPENING;
   }
}
