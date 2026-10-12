package r2d.animation;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import rtr.utilities.Utilities;

public class Animation {
   private Image[] frames;
   private Image currentFrame;
   private int currentFrameNumber;
   private int speed;
   private int speedTick;
   private boolean stopped;
   int width;
   int height;
   private Animation.AnimationMode mode = Animation.AnimationMode.NO_LOOPING;

   public Animation(Image[] i) {
      this.frames = i;
      this.currentFrame = this.frames[0];
      this.width = this.currentFrame.getWidth();
      this.height = this.currentFrame.getHeight();
   }

   public void render(float x, float y) {
      this.currentFrame.draw(x, y);
   }

   public void renderEmbedded(float x, float y) {
      this.currentFrame.drawEmbedded(x, y);
   }

   public void render(float x, float y, Color c) {
      this.currentFrame.draw(x, y, x + this.width, y + this.height, 0.0F, 0.0F, this.width, this.height, c);
   }

   public void renderEmbedded(float x, float y, Color c) {
      this.currentFrame.drawEmbedded(x, y, x + this.width, y + this.height, 0.0F, 0.0F, this.width, this.height, c);
   }

   public void render(float x, float y, float angle) {
      this.currentFrame.setRotation(angle);
      this.currentFrame.draw(x, y, x + this.width, y + this.height, 0.0F, 0.0F, this.width, this.height);
   }

   public void render(float x, float y, float angle, Color c) {
      this.currentFrame.setRotation(angle);
      this.currentFrame.draw(x, y, x + this.width, y + this.height, 0.0F, 0.0F, this.width, this.height, c);
   }

   public void renderEmbedded(float x, float y, float angle) {
      this.currentFrame.drawEmbedded(x, y, this.width, this.height, angle, null);
   }

   public void renderEmbedded(float x, float y, float angle, Color c) {
      this.currentFrame.drawEmbedded(x, y, this.width, this.height, angle, c);
   }

   public void render(float x, float y, float x2, float y2, float srcx, float srcy, float srcx2, float srcy2, Color filter) {
      this.render(x, y, x2, y2, srcx, srcy, srcx2, srcy2, filter, false, false);
   }

   public void render(float x, float y, float x2, float y2, float srcx, float srcy, float srcx2, float srcy2, Color filter, boolean flipH, boolean flipV) {
      if (!flipH && !flipV) {
         this.currentFrame.draw(x, y, x + x2, y + y2, srcx, srcy, srcx2, srcy2, filter);
      } else {
         Image flipOut = this.currentFrame.getFlippedCopy(flipH, flipV);
         flipOut.draw(x, y, x + x2, y + y2, srcx, srcy, srcx2, srcy2, filter);
      }
   }

   public void renderEmbedded(float x, float y, float x2, float y2, float srcx, float srcy, float srcx2, float srcy2, Color filter) {
      this.renderEmbedded(x, y, x2, y2, srcx, srcy, srcx2, srcy2, filter, false, false);
   }

   public void renderEmbedded(
      float x, float y, float x2, float y2, float srcx, float srcy, float srcx2, float srcy2, Color filter, boolean flipH, boolean flipV
   ) {
      if (!flipH && !flipV) {
         this.currentFrame.drawEmbedded(x, y, x + x2, y + y2, srcx, srcy, srcx2, srcy2, filter);
      } else {
         Image flipOut = this.currentFrame.getFlippedCopy(flipH, flipV);
         flipOut.drawEmbedded(x, y, x + x2, y + y2, srcx, srcy, srcx2, srcy2, filter);
      }
   }

   public void update() {
      this.update(0);
   }

   public void update(int speedAdjust) {
      int finalSpeedOut = this.speed + speedAdjust;
      if (finalSpeedOut < 0) {
         finalSpeedOut = 0;
      }

      if (!this.stopped
         && this.mode != Animation.AnimationMode.MANUAL
         && this.mode != Animation.AnimationMode.MANUAL_RANDOM
         && this.mode != Animation.AnimationMode.MANUAL_NO_LOOPING) {
         this.speedTick++;
         if (this.speedTick > this.speed + speedAdjust) {
            this.nextFrame();
            this.speedTick = 0;
         }
      }
   }

   public void nextFrame() {
      if (!this.stopped) {
         switch (this.mode) {
            case MANUAL_NO_LOOPING:
            case NO_LOOPING:
               if (this.currentFrameNumber == this.frames.length - 1) {
                  this.stopped = true;
               } else {
                  this.currentFrame = this.frames[++this.currentFrameNumber];
               }
               break;
            case LOOPING:
            case PING_PONG:
            case MANUAL:
               if (this.currentFrameNumber == this.frames.length - 1) {
                  this.currentFrame = this.frames[0];
                  this.currentFrameNumber = 0;
               } else {
                  this.currentFrame = this.frames[++this.currentFrameNumber];
               }
               break;
            case MANUAL_RANDOM:
            case RANDOM:
               this.currentFrame = this.frames[Utilities.randomInt(this.frames.length)];
            case START_RANDOM_FRAME:
         }
      }
   }

   public void setSpeed(int i) {
      this.speed = i;
   }

   public void setMode(Animation.AnimationMode a) {
      this.mode = a;
      if (this.mode == Animation.AnimationMode.PING_PONG) {
         ArrayList<Image> reversedFramesArray = new ArrayList<>();

         for (int i = 0; i < this.frames.length; i++) {
            reversedFramesArray.add(this.frames[i]);
         }

         reversedFramesArray.add(this.frames[0]);

         for (int i = this.frames.length - 1; i > 0; i--) {
            reversedFramesArray.add(this.frames[i]);
         }

         this.frames = new Image[reversedFramesArray.size()];

         for (int i = 0; i < reversedFramesArray.size(); i++) {
            this.frames[i] = reversedFramesArray.get(i);
         }
      }

      if (this.mode == Animation.AnimationMode.START_RANDOM_FRAME) {
         this.currentFrame = this.frames[Utilities.randomInt(this.frames.length)];
      }
   }

   public Image getCurrentFrame() {
      return this.currentFrame;
   }

   public void setFrame(int i) {
      this.currentFrame = this.frames[i];
   }

   public void restart() {
      this.currentFrameNumber = 0;
      this.currentFrame = this.frames[++this.currentFrameNumber];
      this.stopped = false;
   }

   public int getFrameCount() {
      return this.frames.length;
   }

   public int getSpeed() {
      return this.speed;
   }

   public boolean isStopped() {
      return this.stopped;
   }

   public Animation.AnimationMode getMode() {
      return this.mode;
   }

   public static enum AnimationMode {
      MANUAL_NO_LOOPING,
      NO_LOOPING,
      LOOPING,
      MANUAL_RANDOM,
      RANDOM,
      START_RANDOM_FRAME,
      PING_PONG,
      MANUAL;
   }
}
