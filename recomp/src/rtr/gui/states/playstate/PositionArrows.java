package rtr.gui.states.playstate;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import r2d.image.SpriteSheet;
import rtr.ImageLoader;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.PlayStateGUIPanelBase;
import rtr.mobs.MobBase;
import rtr.system.ScaleControl;

public class PositionArrows extends PlayStateGUIPanelBase {
   private SpriteSheet positionArrowsSheet;
   private Image[][] positionArrows;
   private Color redFlashing = new Color(255, 0, 0);
   private Color redRingFlashing = new Color(255, 0, 0);
   private float flashAmount = 0.0F;

   public PositionArrows(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.positionArrowsSheet = new SpriteSheet(ImageLoader.getImage("res/GUI/positionArrows.png"), 16, 16);
      this.positionArrows = new Image[this.positionArrowsSheet.getHorizontalCount()][this.positionArrowsSheet.getVerticalCount()];

      for (int x = 0; x < this.positionArrows.length; x++) {
         for (int y = 0; y < this.positionArrows[x].length; y++) {
            this.positionArrows[x][y] = this.positionArrowsSheet.getSprite(x, y);
         }
      }
   }

   public void render(boolean debug) throws SlickException {
      ArrayList<MobBase> friendlyMobs = this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false);
      friendlyMobs.addAll(this.mob.getMobsByGroup(MobBase.MobGroup.DOMESTICATED_ANIMAL, false));
      friendlyMobs.addAll(this.mob.getMobsByGroup(MobBase.MobGroup.GOLEM, false));
      this.flashAmount = (float)(this.flashAmount + 0.05);
      if (this.flashAmount > 1.0F) {
         this.flashAmount = 0.0F;
      }

      this.redFlashing.g = this.flashAmount;
      this.redFlashing.b = this.flashAmount;
      this.redRingFlashing.g = this.flashAmount;
      this.redRingFlashing.b = this.flashAmount;
      this.redRingFlashing.a = 1.0F - this.flashAmount;
      this.positionArrowsSheet.startUse();

      for (MobBase m : friendlyMobs) {
         float x = (m.getMobX() + this.map.getMapX()) * ScaleControl.getScalingDifference();
         float y = (m.getMobY() + this.map.getMapY()) * ScaleControl.getScalingDifference();
         Color colorOut = Color.green.brighter(3.0F);
         if (!m.getMobJobType().getJob().isNotCounted()) {
            colorOut = m.getMobJobType().getJob().getBaseColor().brighter(3.0F);
         }

         if (m.getHitPoints() <= m.getHitPointsMax() * 0.75) {
            colorOut = this.redFlashing;
         }

         int left = 0;
         int right = ScaleControl.getInterfaceWidth() - this.positionArrows[0][0].getWidth();
         int top = 0;
         int bottom = ScaleControl.getInterfaceHeight() - this.positionArrows[0][0].getHeight();
         if (!(x > left) || !(x < right) || !(y > top) || !(y < bottom)) {
            if (x < left) {
               x = left;
            }

            if (x > right) {
               x = right;
            }

            if (y < top) {
               y = top;
            }

            if (y > bottom) {
               y = bottom;
            }

            if (x == left && y == top) {
               this.positionArrows[0][0].drawEmbedded(x, y, x + 16.0F, y + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, colorOut);
            } else if (x == left && y == bottom) {
               this.positionArrows[0][2].drawEmbedded(x, y, x + 16.0F, y + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, colorOut);
            } else if (x == right && y == bottom) {
               this.positionArrows[2][2].drawEmbedded(x, y, x + 16.0F, y + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, colorOut);
            } else if (x == right && y == top) {
               this.positionArrows[2][0].drawEmbedded(x, y, x + 16.0F, y + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, colorOut);
            } else if (x == left) {
               this.positionArrows[0][1].drawEmbedded(x, y, x + 16.0F, y + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, colorOut);
            } else if (x == right) {
               this.positionArrows[2][1].drawEmbedded(x, y, x + 16.0F, y + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, colorOut);
            } else if (y == top) {
               this.positionArrows[1][0].drawEmbedded(x, y, x + 16.0F, y + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, colorOut);
            } else if (y == bottom) {
               this.positionArrows[1][2].drawEmbedded(x, y, x + 16.0F, y + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, colorOut);
            }
         }
      }

      this.positionArrowsSheet.endUse();

      for (MobBase m : friendlyMobs) {
         if (!(m.getHitPoints() > m.getHitPointsMax() * 0.75)) {
            float xx = (m.getMobX() + this.map.getMapX()) * ScaleControl.getScalingDifference() + 8.0F;
            float yx = (m.getMobY() + this.map.getMapY()) * ScaleControl.getScalingDifference() + 8.0F;
            int left = 0;
            int right = ScaleControl.getInterfaceWidth();
            int top = 0;
            int bottom = ScaleControl.getInterfaceHeight();
            if (!(xx > left) || !(xx < right) || !(yx > top) || !(yx < bottom)) {
               if (xx < left) {
                  xx = left;
               }

               if (xx > right) {
                  xx = right;
               }

               if (yx < top) {
                  yx = top;
               }

               if (yx > bottom) {
                  yx = bottom;
               }

               this.g.setColor(this.redRingFlashing);
               this.g
                  .fillOval(xx - this.flashAmount * 50.0F / 2.0F, yx - this.flashAmount * 50.0F / 2.0F, this.flashAmount * 50.0F, this.flashAmount * 50.0F, 6);
               this.g.setColor(this.redRingFlashing.brighter(3.0F));
               this.g
                  .fillOval(xx - this.flashAmount * 30.0F / 2.0F, yx - this.flashAmount * 30.0F / 2.0F, this.flashAmount * 30.0F, this.flashAmount * 30.0F, 6);
               this.g.setColor(this.redRingFlashing.brighter(6.0F));
               this.g
                  .fillOval(xx - this.flashAmount * 20.0F / 2.0F, yx - this.flashAmount * 20.0F / 2.0F, this.flashAmount * 20.0F, this.flashAmount * 20.0F, 6);
            }
         }
      }
   }
}
