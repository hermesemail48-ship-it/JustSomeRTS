package rtr.gui.states.playstate;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.TimeModule;
import rtr.font.Text;
import rtr.gui.buttons.GUIClickableArea;
import rtr.gui.buttons.GUIRoundButton;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.PlayStateGUIPanelBase;
import rtr.system.Game;
import rtr.system.ScaleControl;

public class WeatherPanel extends PlayStateGUIPanelBase {
   private Image panel = ImageLoader.getImage("res/GUI/gameState/weatherPanel.png");
   private Rectangle mask = new Rectangle(0.0F, 0.0F, this.panel.getWidth(), this.panel.getHeight());
   private GUIClickableArea weatherArea;
   private GUIClickableArea timeArea;
   private GUIRoundButton speedDecrease;
   private GUIRoundButton speedIncrease;

   public WeatherPanel(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.masks.add(this.mask);
      this.weatherArea = new GUIClickableArea("weatherArea", this.panel.getWidth(), this.panel.getHeight() / 2 - 12);
      this.weatherArea.setToolTip(Text.getText("playWeatherPanel.tooltip.weatherHeader"), Text.getText("playWeatherPanel.tooltip.weather"));
      this.guiButtons.add(this.weatherArea);
      this.timeArea = new GUIClickableArea("timeArea", this.panel.getWidth(), this.panel.getHeight() / 2 - 5);
      this.timeArea.setToolTip(Text.getText("playWeatherPanel.tooltip.timeHeader"), Text.getText("playWeatherPanel.tooltip.time"));
      this.guiButtons.add(this.timeArea);
      this.speedDecrease = new GUIRoundButton("speedDecrease", "buttonLast", 0);
      this.speedDecrease.setToolTip(Text.getText("playWeatherPanel.button.tooltip.decreaseGameSpeed"));
      this.guiButtons.add(this.speedDecrease);
      this.speedIncrease = new GUIRoundButton("speedIncrease", "buttonFirst", 0);
      this.speedIncrease.setToolTip(Text.getText("playWeatherPanel.button.tooltip.increaseGameSpeed"));
      this.guiButtons.add(this.speedIncrease);
   }

   public void render(boolean gamePaused, boolean debug) throws SlickException {
      this.x = ScaleControl.getInterfaceWidth() - this.panel.getWidth() - 6;
      this.y = 6;
      this.mask.setX(this.x);
      this.mask.setY(this.y);
      int currentTempLeft = (int)Math.ceil((float)this.weather.getCurrentTemp() / 100.0F * 180.0F);
      if (this.weather.getNewTemp() > this.weather.getCurrentTemp()) {
         int newTempLeft = (int)Math.ceil((float)this.weather.getNewTemp() / 100.0F * 180.0F);
         this.g.setColor(new Color(255, 0, 0, 200));
         this.g.fillArc(this.x + 6, this.y + 16, 94.0F, 94.0F, 180 + currentTempLeft, 180 + newTempLeft);
      }

      if (this.weather.getCurrentTemp() <= this.weather.getFreezePoint()) {
         this.g.setColor(new Color(0, 255, 255, 200));
      } else if (this.weather.getCurrentTemp() <= this.weather.getColdPoint()) {
         this.g.setColor(new Color(0, 255, 200, 200));
      } else if (this.weather.getCurrentTemp() <= this.weather.getColdPoint() + 20) {
         this.g.setColor(new Color(0, 255, 150, 200));
      } else if (this.weather.getCurrentTemp() > this.weather.getColdPoint() + 20 && this.weather.getCurrentTemp() < this.weather.getHotPoint() - 20) {
         this.g.setColor(new Color(0, 255, 0, 200));
      } else if (this.weather.getCurrentTemp() >= this.weather.getFryPoint()) {
         this.g.setColor(new Color(255, 0, 0, 200));
      } else if (this.weather.getCurrentTemp() >= this.weather.getHotPoint()) {
         this.g.setColor(new Color(255, 150, 0, 200));
      } else if (this.weather.getCurrentTemp() >= this.weather.getHotPoint() - 20) {
         this.g.setColor(new Color(255, 255, 0, 200));
      }

      this.g.fillArc(this.x + 6, this.y + 16, 94.0F, 94.0F, 180.0F, 180 + currentTempLeft);
      int tempBar = (int)Math.ceil(this.weather.getFreezePoint() / 100.0F * 180.0F);
      this.g.setColor(new Color(0, 0, 0, 150));
      this.g.fillArc(this.x + 6, this.y + 16, 94.0F, 94.0F, 180 + tempBar - 2, 180 + tempBar + 4);
      this.g.setColor(Color.cyan);
      this.g.fillArc(this.x + 6, this.y + 16, 94.0F, 94.0F, 180 + tempBar, 180 + tempBar + 2);
      tempBar = (int)Math.ceil(this.weather.getColdPoint() / 100.0F * 180.0F);
      this.g.setColor(new Color(0, 0, 0, 150));
      this.g.fillArc(this.x + 6, this.y + 16, 94.0F, 94.0F, 180 + tempBar - 2, 180 + tempBar + 4);
      this.g.setColor(Color.cyan);
      this.g.fillArc(this.x + 6, this.y + 16, 94.0F, 94.0F, 180 + tempBar, 180 + tempBar + 2);
      tempBar = (int)Math.ceil(this.weather.getHotPoint() / 100.0F * 180.0F);
      this.g.setColor(new Color(0, 0, 0, 150));
      this.g.fillArc(this.x + 6, this.y + 16, 94.0F, 94.0F, 180 + tempBar - 2, 180 + tempBar + 4);
      this.g.setColor(Color.red);
      this.g.fillArc(this.x + 6, this.y + 16, 94.0F, 94.0F, 180 + tempBar, 180 + tempBar + 2);
      tempBar = (int)Math.ceil(this.weather.getFryPoint() / 100.0F * 180.0F);
      this.g.setColor(new Color(0, 0, 0, 150));
      this.g.fillArc(this.x + 6, this.y + 16, 94.0F, 94.0F, 180 + tempBar - 2, 180 + tempBar + 4);
      this.g.setColor(Color.red);
      this.g.fillArc(this.x + 6, this.y + 16, 94.0F, 94.0F, 180 + tempBar, 180 + tempBar + 2);
      if (this.weather.getNewTemp() < this.weather.getCurrentTemp()) {
         int newTempLeft = (int)Math.ceil(((float)this.weather.getCurrentTemp() - (float)this.weather.getNewTemp()) / 100.0F * 180.0F);
         this.g.setColor(new Color(255, 255, 255, 200));
         this.g.fillArc(this.x + 6, this.y + 16, 94.0F, 94.0F, 180 + currentTempLeft - newTempLeft, 180 + currentTempLeft);
      }

      int timeLeft = (int)Math.ceil((float)this.time.getTimeOfDayTick() / this.time.getDayDuration() * 180.0F);
      this.g.setColor(new Color(0, 255, 0, 200));
      this.g.fillArc(this.x + 6, this.y + 16, 94.0F, 94.0F, 180 - timeLeft, 180.0F);
      this.panel.draw(this.x, this.y);
      this.weatherArea.render(this.g, this.mouse, this.x - 3, this.y - 3, false, debug);
      this.timeArea.render(this.g, this.mouse, this.x - 3, this.y + 2 + this.panel.getHeight() / 2, false, debug);
      if (!gamePaused) {
         if (debug) {
            this.font
               .drawString(
                  this.x + this.panel.getWidth() / 2,
                  56.0F,
                  this.time.getTimeOfDayTick() + "/" + this.time.getTimeOfDay().getDuration(this.time.getSeason()),
                  Text.FontType.BODY,
                  0,
                  true
               );
            this.font
               .drawString(
                  this.x + this.panel.getWidth() / 2, 64.0F, this.weather.getCurrentWind() + "/" + this.weather.getNewWind(), Text.FontType.BODY, 0, true
               );
         }

         String tempString = "";
         if (this.weather.getCurrentTemp() <= this.weather.getFreezePoint()) {
            if (this.weather.isSnowing()) {
               tempString = Text.getText("playWeatherPanel.weather.snowing");
            } else {
               tempString = Text.getText("playWeatherPanel.weather.veryCold");
            }
         } else if (this.weather.getCurrentTemp() <= this.weather.getColdPoint()) {
            if (this.weather.isRaining()) {
               tempString = Text.getText("playWeatherPanel.weather.coldRain");
            } else {
               tempString = Text.getText("playWeatherPanel.weather.cold");
            }
         } else if (this.weather.getCurrentTemp() <= this.weather.getColdPoint() + 20) {
            if (this.weather.isRaining()) {
               tempString = Text.getText("playWeatherPanel.weather.chillyRain");
            } else {
               tempString = Text.getText("playWeatherPanel.weather.chilly");
            }
         } else if (this.weather.getCurrentTemp() > this.weather.getColdPoint() + 20 && this.weather.getCurrentTemp() < this.weather.getHotPoint() - 20) {
            if (this.weather.isRaining()) {
               tempString = Text.getText("playWeatherPanel.weather.raining");
            } else {
               tempString = Text.getText("playWeatherPanel.weather.temperate");
            }
         } else if (this.weather.getCurrentTemp() >= this.weather.getFryPoint()) {
            if (this.weather.isRaining()) {
               tempString = Text.getText("playWeatherPanel.weather.hotRain");
            } else {
               tempString = Text.getText("playWeatherPanel.weather.veryHot");
            }
         } else if (this.weather.getCurrentTemp() >= this.weather.getHotPoint()) {
            if (this.weather.isRaining()) {
               tempString = Text.getText("playWeatherPanel.weather.warmRain");
            } else {
               tempString = Text.getText("playWeatherPanel.weather.hot");
            }
         } else if (this.weather.getCurrentTemp() >= this.weather.getHotPoint() - 20) {
            if (this.weather.isRaining()) {
               tempString = Text.getText("playWeatherPanel.weather.coolRain");
            } else {
               tempString = Text.getText("playWeatherPanel.weather.warm");
            }
         }

         this.font.drawString(this.x + this.panel.getWidth() / 2, 11.0F, tempString, Text.FontType.HEADER, 1, true);
         TimeModule.Season s = this.time.getSeason();
         String seasonColor = "";
         if (s == TimeModule.Season.WINTER) {
            seasonColor = "$BLU0";
         } else if (s == TimeModule.Season.SPRING) {
            seasonColor = "$GRE0";
         } else if (s == TimeModule.Season.SUMMER) {
            seasonColor = "$YEL0";
         } else if (s == TimeModule.Season.AUTUMN) {
            seasonColor = "$ORA0";
         }

         this.font.drawString(this.x + this.panel.getWidth() / 2, 43.0F, seasonColor + s.getText(), Text.FontType.BODY, 1, true);
         Text.setVariableText("day", this.time.getDay());
         this.font.drawString(this.x + this.panel.getWidth() / 2, 73.0F, Text.getText("playWeatherPanel.day"), Text.FontType.BODY, 1, true);
         Text.setVariableText("year", this.time.getYear());
         this.font.drawString(this.x + this.panel.getWidth() / 2, 85.0F, Text.getText("playWeatherPanel.year"), Text.FontType.BODY, 0, true);
         if (this.time.isFullMoon()) {
            this.font.drawString(this.x + this.panel.getWidth() / 2, 112.0F, Text.getText("playWeatherPanel.moon.full"), Text.FontType.HEADER, 1, true);
         } else if (this.time.isBloodMoon()) {
            this.font.drawString(this.x + this.panel.getWidth() / 2, 112.0F, Text.getText("playWeatherPanel.moon.blood"), Text.FontType.HEADER, 1, true);
         } else if (this.time.isEclipse()) {
            this.font.drawString(this.x + this.panel.getWidth() / 2, 112.0F, Text.getText("playWeatherPanel.moon.eclipse"), Text.FontType.BODY, 1, true);
         } else {
            this.font.drawString(this.x + this.panel.getWidth() / 2, 112.0F, "$GRE1" + this.time.getTimeOfDay().getText(), Text.FontType.HEADER, 1, true);
         }
      }

      this.speedDecrease.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2 - 30, 56, debug);
      if (gamePaused) {
         this.font.drawString(this.x + this.panel.getWidth() / 2, 42.0F, Text.getText("playWeatherPanel.paused"), Text.FontType.BODY, 1, true);
      }

      this.font.drawString(this.x + this.panel.getWidth() / 2, 56.0F, Game.getCS().getGameSpeed() + "x", Text.FontType.BODY, 2, true);
      this.speedIncrease.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2 + 10, 56, debug);
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
