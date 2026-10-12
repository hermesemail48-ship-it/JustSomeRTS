package rtr.help.tips;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import r2d.animation.Animation;
import r2d.image.SpriteSheet;
import rtr.ModuleBase;
import rtr.SoundModule;
import rtr.TimeModule;
import rtr.gui.buttons.GUIButtonTip;
import rtr.help.HelpModule;
import rtr.map.MapModule;
import rtr.mobs.MigrationModule;
import rtr.mobs.MobBase;
import rtr.mobs.MobModule;
import rtr.mobs.essence.EssenceModule;
import rtr.objects.ObjectBase;
import rtr.objects.ObjectModule;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.save.SaveModule;
import rtr.spawning.CorruptionModule;
import rtr.states.StateBase;
import rtr.system.ScaleControl;
import rtr.weather.WeatherModule;

public class TipBase {
   protected ResourceModule resource = (ResourceModule)StateBase.getModule(ModuleBase.ModuleType.RESOURCE);
   protected MobModule mob = (MobModule)StateBase.getModule(ModuleBase.ModuleType.MOB);
   protected MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   protected ObjectModule object = (ObjectModule)StateBase.getModule(ModuleBase.ModuleType.OBJECT);
   protected TimeModule time = (TimeModule)StateBase.getModule(ModuleBase.ModuleType.TIME);
   protected SaveModule save = (SaveModule)StateBase.getModule(ModuleBase.ModuleType.SAVE);
   protected MigrationModule migration = (MigrationModule)StateBase.getModule(ModuleBase.ModuleType.MIGRATION);
   protected WeatherModule weather = (WeatherModule)StateBase.getModule(ModuleBase.ModuleType.WEATHER);
   protected HelpModule help = (HelpModule)StateBase.getModule(ModuleBase.ModuleType.HELP);
   protected EssenceModule essence = (EssenceModule)StateBase.getModule(ModuleBase.ModuleType.ESSENCE);
   protected CorruptionModule corruption = (CorruptionModule)StateBase.getModule(ModuleBase.ModuleType.CORRUPTION);
   protected SoundModule sound = (SoundModule)StateBase.getModule(ModuleBase.ModuleType.SOUND);
   protected GUIButtonTip tipButton;
   protected float tipX;
   protected float tipY;
   protected float destX;
   protected float destY;
   private float velX;
   private float velY;
   protected boolean onGUI;
   protected boolean foundTipCondition;
   protected String tipName;
   protected String tipHeader;
   protected String tipBody;
   protected boolean forcePopup;
   protected boolean doNotSave;
   protected boolean worldMapTip;
   protected MobBase attachedMob;
   protected ObjectBase attachedObject;
   protected ResourceBase attachedResource;
   private Image newImage;
   private SpriteSheet videoSheet;
   private boolean hasVideo;
   private boolean videoLoaded;
   private boolean stopMoving;
   private int loadVideoStep = -1;
   private int loadBuffer = 0;
   private int maxCount;
   private int currentCount;
   private Image[] videoFrames;
   private Animation video;

   @Override
   public void finalize() {
      try {
         this.tipButton = new GUIButtonTip(this);
         this.tipButton.setToolTip(this.tipHeader);
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

      File imageCheck = new File("res/video/" + this.tipName + "/" + this.tipName + "0.png");
      if (imageCheck.exists()) {
         this.hasVideo = true;
      }
   }

   public void render(Graphics g, Rectangle mouse, boolean debug) {
      int drawX = (int)ScaleControl.getTransformedPositionX(this.tipX);
      int drawY = (int)ScaleControl.getTransformedPositionY(this.tipY);
      if (this.foundTipCondition) {
         this.tipButton.render(g, mouse, drawX, drawY, false, debug);
         if (this.tipButton.intersects(mouse)) {
            this.stopMoving = true;
         } else {
            this.stopMoving = false;
         }
      }
   }

   public void renderToolTip(Graphics g, Rectangle mouse) throws SlickException {
      if (this.foundTipCondition && this.tipButton.intersects(mouse)) {
         this.tipButton.renderDescriptionToolTip(mouse, g);
      }
   }

   public void updateTipConditions() throws SlickException {
      if (!this.foundTipCondition) {
         this.findTipConditions();
      } else if (!this.forcePopup) {
         this.verifyTipConditions();
      }
   }

   public void update() throws SlickException {
      this.updateAttachments();
   }

   private void updateAttachments() {
      if (!this.foundTipCondition) {
         this.tipX = (int)Math.floor(this.map.getMapX() * -1.0F + ScaleControl.getInterfaceWidth() / 2 / ScaleControl.getScalingDifference());
         this.tipY = (int)Math.floor(this.map.getMapY() * -1.0F + ScaleControl.getInterfaceHeight() / 2 / ScaleControl.getScalingDifference());
         this.velX = 0.0F;
         this.velY = 0.0F;
      }

      if (!this.stopMoving) {
         if (this.attachedMob != null) {
            this.destX = this.attachedMob.getMobX() + 16.0F;
            this.destY = this.attachedMob.getMobY() - 16.0F;
         }

         if (this.attachedObject != null) {
            this.destX = (this.attachedObject.getObjectX() + this.attachedObject.getObjectFlags().getWidth() - 3) * 16;
            this.destY = this.attachedObject.getObjectY() * 16;
         }

         if (this.attachedResource != null) {
            this.destX = this.attachedResource.getResourceX() + 16.0F;
            this.destY = this.attachedResource.getResourceY() - 16.0F;
         }

         this.updatePosition();
      }
   }

   private void updatePosition() {
      float velMod = 0.03F;
      if (Math.abs(this.tipX - this.destX) > 10.0F) {
         if (this.tipX > this.destX) {
            this.velX -= velMod;
         }

         if (this.tipX < this.destX) {
            this.velX += velMod;
         }
      }

      if (Math.abs(this.tipY - this.destY) > 10.0F) {
         if (this.tipY > this.destY) {
            this.velY -= velMod;
         }

         if (this.tipY < this.destY) {
            this.velY += velMod;
         }
      }

      if (this.velX > 5.0F) {
         this.velX = 5.0F;
      }

      if (this.velX < -5.0F) {
         this.velX = -5.0F;
      }

      if (this.velY > 5.0F) {
         this.velY = 5.0F;
      }

      if (this.velY < -5.0F) {
         this.velY = -5.0F;
      }

      boolean checkCollision = Math.abs(this.tipX - this.destX) < 32.0F * ScaleControl.getWorldScale()
         && Math.abs(this.tipY - this.destY) < 32.0F * ScaleControl.getWorldScale();
      if (checkCollision) {
         for (TipBase t : this.help.getActiveTips()) {
            if (t != this && t.hasFoundTipCondition() && this.tipButton.intersects(t.getTipButton().getIntersectBox())) {
               this.tipX = (float)(this.tipX - (this.tipX < t.getTipX() ? 0.1 : -0.1));
               this.tipY = (float)(this.tipY - (this.tipY < t.getTipY() ? 0.1 : -0.1));
               this.velX = 0.0F;
               this.velY = 0.0F;
            }
         }
      }

      if (Math.abs(this.tipX - this.destX) > 3.0F) {
         if (checkCollision) {
            for (TipBase tx : this.help.getActiveTips()) {
               if (tx != this && tx.hasFoundTipCondition() && Math.abs(this.tipX - tx.getTipX()) < 8.0F && Math.abs(this.tipY - tx.getTipY()) < 8.0F) {
                  this.velX = 0.0F;
                  break;
               }
            }
         }

         this.tipX = this.tipX + this.velX;
      } else {
         this.velX = 0.0F;
      }

      if (Math.abs(this.tipY - this.destY) > 3.0F) {
         if (checkCollision) {
            for (TipBase txx : this.help.getActiveTips()) {
               if (txx != this && txx.hasFoundTipCondition() && Math.abs(this.tipX - txx.getTipX()) < 8.0F && Math.abs(this.tipY - txx.getTipY()) < 8.0F) {
                  this.velY = 0.0F;
                  break;
               }
            }
         }

         this.tipY = this.tipY + this.velY;
      } else {
         this.velY = 0.0F;
      }

      if (this.velX == 0.0F && this.velY == 0.0F) {
         this.tipButton.update();
      }
   }

   private boolean hasFoundTipCondition() {
      return this.foundTipCondition;
   }

   public float getTipX() {
      return this.tipX;
   }

   public float getTipY() {
      return this.tipY;
   }

   protected void findTipConditions() throws SlickException {
   }

   protected void verifyTipConditions() throws SlickException {
   }

   protected void clearAttachmentData() {
      this.attachedMob = null;
      this.attachedObject = null;
      this.attachedResource = null;
   }

   public void loadVideo() throws SlickException {
      if (this.foundTipCondition && this.hasVideo && !this.videoLoaded) {
         this.loadBuffer++;
         if (this.loadBuffer % 6 != 0) {
            return;
         }

         if (this.loadVideoStep == -1) {
            Properties videoProperties = new Properties();

            try {
               FileInputStream file = new FileInputStream("res/video/" + this.tipName + "/" + this.tipName + ".properties");
               videoProperties.load(file);
            } catch (IOException var3) {
               var3.printStackTrace();
            }

            this.maxCount = Integer.parseInt(videoProperties.getProperty("frameCount"));
            this.videoFrames = new Image[this.maxCount];
            this.loadVideoStep++;
         } else {
            this.newImage = new Image("res/video/" + this.tipName + "/" + this.tipName + this.loadVideoStep + ".png");
            this.videoSheet = new SpriteSheet(this.newImage, 256, 256);

            for (int y = 0; y < this.videoSheet.getVerticalCount(); y++) {
               for (int x = 0; x < this.videoSheet.getHorizontalCount(); x++) {
                  this.videoFrames[this.currentCount] = this.videoSheet.getSprite(x, y);
                  this.currentCount++;
                  if (this.currentCount > this.maxCount - 1) {
                     this.video = new Animation(this.videoFrames);
                     this.video.setSpeed(2);
                     this.video.setMode(Animation.AnimationMode.LOOPING);
                     this.videoLoaded = true;
                     return;
                  }
               }
            }

            this.loadVideoStep++;
         }
      }
   }

   public boolean hasVideo() {
      return this.hasVideo;
   }

   public boolean isVideoLoaded() {
      return this.videoLoaded;
   }

   public String getTipHeader() {
      return this.tipHeader;
   }

   public String getTipBody() {
      return this.tipBody;
   }

   public Animation getTipVideo() {
      return this.video;
   }

   public GUIButtonTip getTipButton() {
      return this.tipButton;
   }

   public boolean isForcePopup() {
      return this.forcePopup;
   }

   public boolean isWorldMapTip() {
      return this.worldMapTip;
   }

   public String getTipName() {
      return this.tipName;
   }

   public boolean doNotSave() {
      return this.doNotSave;
   }
}
