package rtr.gui.states.worldmap;

import java.util.ArrayList;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.goal.Goal;
import rtr.gui.buttons.GUIButtonGenericWood;
import rtr.gui.buttons.GUIButtonGoalStats;
import rtr.gui.buttons.GUIButtonPerk;
import rtr.gui.states.WorldMapGUIController;
import rtr.gui.states.WorldMapGUIData;
import rtr.gui.states.WorldMapGUIPanelBase;
import rtr.particles.ParticleModule;
import rtr.system.ScaleControl;
import rtr.utilities.Utilities;

public class GoalClaimPanel extends WorldMapGUIPanelBase {
   private Image iconGodXP;
   private Image godXPPanel;
   private GUIButtonGoalStats selectedGoalButton;
   private Goal selectedGoal;
   private ArrayList<PerkModule.Perk> selectedGoalPerks;
   private ArrayList<Image> perkListTopBar = new ArrayList<>();
   private ArrayList<GUIButtonPerk> perkList = new ArrayList<>();
   private boolean revealed;
   private GUIButtonGenericWood takeIt;

   public GoalClaimPanel(WorldMapGUIController controller, WorldMapGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.iconGodXP = ImageLoader.getImage("res/GUI/icon/iconGodXP.png");
      this.godXPPanel = ImageLoader.getImage("res/GUI/gameState/perkBarDefense" + (Utilities.randomInt(10) + 1) + ".png");
      this.takeIt = new GUIButtonGenericWood("takeIt", Text.getText("worldMapGoalClaimPanel.button.takeIt"));
      this.guiButtons.add(this.takeIt);
   }

   public void render(boolean debug) throws SlickException {
      this.x = ScaleControl.getInterfaceWidth() / 2;
      this.y = 130;
      this.selectedGoalButton.renderEffects(this.g, this.x - 136, this.y + 41);
      this.selectedGoalButton.render(this.g, this.mouse, this.x - 136, this.y + 41, debug);
      if (!this.revealed) {
         this.projectile.newExpandingEnergyRing(this.x, this.y + 80, 768, 2.0F, ParticleModule.ParticleSet.FIRE_BLUE, true);
         this.projectile.newExpandingEnergyRing(this.x, this.y + 80, 768, 3.0F, ParticleModule.ParticleSet.MAGIC_GREEN, true);
         this.projectile.newExpandingEnergyRing(this.x, this.y + 80, 512, 3.5F, ParticleModule.ParticleSet.FIRE_BLUE, true);
         this.projectile.newExpandingEnergyRing(this.x, this.y + 80, 256, 4.5F, ParticleModule.ParticleSet.MAGIC_GREEN, true);
         this.projectile.newExpandingEnergyRing(this.x, this.y + 80, 256, 10.0F, ParticleModule.ParticleSet.FIRE_BLUE, true);
         this.sound.playSound(SoundModule.SoundType.HEAVY_WIND);
         this.sound.playSound(SoundModule.SoundType.HOLY_SOFT);
      }

      int alignX = -7;
      int alignY = 0;
      int var9 = 118;
      this.godXPPanel.draw(this.x - this.godXPPanel.getWidth() + var9, this.y + 127 + alignY);
      this.iconGodXP.draw(this.x - this.godXPPanel.getWidth() + 7 + var9, this.y + 133 + alignY);
      Text.setVariableText("amount", this.selectedGoal.getGodXPAmount());
      this.font
         .drawString(
            this.x - this.godXPPanel.getWidth() + 33 + var9, this.y + 133 + alignY, Text.getText("worldMapGoalClaimPanel.godExperience"), Text.FontType.BODY, 1
         );
      if (!this.revealed) {
         for (int z = 0; z < 500; z++) {
            this.particle
               .newParticleGroup(
                  this.x - this.godXPPanel.getWidth() + var9 + Utilities.randomInt(255),
                  this.y + 127 + Utilities.randomInt(55),
                  ParticleModule.ParticleGroup.SMOKE_BELLOW,
                  ParticleModule.ParticleSet.FIRE_BLUE,
                  ParticleModule.ParticleSet.FIRE_BLUE,
                  true
               );
         }
      }

      var9 = 0;
      alignY += 50;
      int count = 0;

      for (int i = 0; i < this.perkList.size(); i++) {
         int xMod = this.x + var9;
         if (i == this.perkList.size() - 1 && this.perkList.size() % 2 != 0) {
            xMod += 125;
         }

         int yMod = this.y + alignY;
         this.perkList.get(i).render(this.g, this.mouse, xMod - 262, yMod + 140, debug);
         this.perkListTopBar.get(i).draw(xMod - 234, yMod + 115);
         this.font.drawString(xMod - 123, yMod + 123, "$YEL0" + this.selectedGoalPerks.get(i).getPerkName(), Text.FontType.BODY, 2, true);
         if (!this.revealed) {
            for (int z = 0; z < 500; z++) {
               this.particle
                  .newParticleGroup(
                     xMod - 262 + Utilities.randomInt(255),
                     yMod + 115 + Utilities.randomInt(55),
                     ParticleModule.ParticleGroup.SMOKE_BELLOW,
                     ParticleModule.ParticleSet.FIRE_BLUE,
                     ParticleModule.ParticleSet.FIRE_BLUE,
                     true
                  );
            }
         }

         if (count > 8) {
            alignY += 20;
            Text.setVariableText("amount", this.perkList.size() - count);
            this.font.drawString(xMod - 270, yMod + 173, Text.getText("worldMapGoalClaimPanel.morePerks"), Text.FontType.BODY, 2, true);
            break;
         }

         if (var9 == 0) {
            var9 += 270;
         } else if (i != this.perkList.size() - 1) {
            var9 = 0;
            alignY += 60;
         }

         count++;
      }

      this.revealed = true;
      this.takeIt.render(this.g, this.mouse, this.x, this.y + 175 + alignY, false, true, debug);
   }

   @Override
   public void update() {
   }

   public void setSelectedGoal(Goal g, ArrayList<PerkModule.Perk> pList) throws SlickException {
      this.selectedGoalButton = new GUIButtonGoalStats("selectedGoal", g);
      this.selectedGoal = g;
      this.selectedGoalPerks = pList;

      for (PerkModule.Perk p : this.selectedGoalPerks) {
         GUIButtonPerk newPerk = new GUIButtonPerk("perk" + p, p);
         this.perkList.add(newPerk);
         this.perkListTopBar.add(ImageLoader.getImage("res/GUI/gameState/perkBarTop" + (Utilities.randomInt(1) + 1) + ".png"));
      }

      this.revealed = false;
   }

   public void reset() throws SlickException {
      this.selectedGoalButton = null;
      this.perkList.clear();
      this.perkListTopBar.clear();
      this.revealed = true;
   }

   public GUIButtonGoalStats getSelectedGoalButton() {
      return this.selectedGoalButton;
   }

   @Override
   public int getCenterX() {
      return this.x + 275;
   }

   @Override
   public int getCenterY() {
      return this.y + 275;
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
      return this.x + 275;
   }

   @Override
   public int getBottomY() {
      return this.y + 275;
   }
}
