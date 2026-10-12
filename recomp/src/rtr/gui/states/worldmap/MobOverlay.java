package rtr.gui.states.worldmap;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.gui.states.WorldMapGUIController;
import rtr.gui.states.WorldMapGUIData;
import rtr.gui.states.WorldMapGUIPanelBase;
import rtr.mobs.MobBase;

public class MobOverlay extends WorldMapGUIPanelBase {
   private Image mapSelectIconPregnant = ImageLoader.getImage("res/GUI/icon/iconPregnant.png");
   private Image mapSelectIconMate = ImageLoader.getImage("res/GUI/icon/iconMate.png");
   private Image mapSelectIconHome = ImageLoader.getImage("res/GUI/icon/iconHome.png");
   private Image mapSelectIconLevel = ImageLoader.getImage("res/GUI/icon/iconLevel.png");
   private Image mapSelectIconHealth = ImageLoader.getImage("res/GUI/icon/iconHitPoints.png");

   public MobOverlay(WorldMapGUIController controller, WorldMapGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
   }

   public void render(MobBase m, float mouseX, float mouseY, Graphics g, boolean debug) {
      String genderColor = "$WHI0";
      if (m.getGender() == MobBase.MobGender.MALE) {
         genderColor = "$CYA1";
      }

      if (m.getGender() == MobBase.MobGender.FEMALE) {
         genderColor = "$PIN1";
      }

      int nameLength = Text.getWidth(m.getFullName(), Text.FontType.BODY, 1);

      while (nameLength < 70) {
         nameLength += 10;
      }

      int HPLength = Text.getWidth(m.getHitPoints() + "/" + m.getHitPointsMax(), Text.FontType.BODY, 1);
      g.setColor(new Color(0, 0, 0, 100));
      if (nameLength > HPLength + 20) {
         g.fillRect(mouseX - 4.0F, mouseY - 2.0F, nameLength + 8, 58.0F);
      } else {
         g.fillRect(mouseX + nameLength - HPLength - 22.0F, mouseY - 1.0F, HPLength + 28, 58.0F);
      }

      this.mapSelectIconLevel.draw(mouseX - 4.0F, mouseY);
      this.font.drawString(mouseX + 12.0F, mouseY, String.valueOf(m.getLevel()), Text.FontType.BODY, 1);
      int iconPositionX = -16;
      if (m.getAssignedHome() != -1) {
         this.mapSelectIconHome.draw(mouseX + nameLength + iconPositionX, mouseY);
         iconPositionX -= 16;
      }

      if (m.getMateID() != -1) {
         this.mapSelectIconMate.draw(mouseX + nameLength + iconPositionX, mouseY);
         iconPositionX -= 16;
      }

      if (m.isPregnant()) {
         this.mapSelectIconPregnant.draw(mouseX + nameLength + iconPositionX, mouseY);
         iconPositionX -= 16;
      }

      int xpLeft = (int)Math.ceil((float)m.getExperience() / (m.getMobFlags().getExperienceMultiplier() * m.getLevel()) * nameLength);
      g.setColor(new Color(0, 0, 0, 200));
      g.fillRect(mouseX - 1.0F, mouseY + 17.0F, nameLength + 2, 5.0F);
      g.setColor(new Color(0, 200, 0));
      g.fillRect(mouseX, mouseY + 18.0F, xpLeft, 2.0F);
      this.font.drawString(mouseX, mouseY + 22.0F, genderColor + m.getFullName(), Text.FontType.BODY, 1);
      this.mapSelectIconHealth.draw(mouseX + nameLength - HPLength - 20.0F, mouseY + 38.0F);
      this.font.drawString(mouseX + nameLength - HPLength, mouseY + 38.0F, m.getHitPoints() + "/" + m.getHitPointsMax(), Text.FontType.BODY, 1);
   }
}
