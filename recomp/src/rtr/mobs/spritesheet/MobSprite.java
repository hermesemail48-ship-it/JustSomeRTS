package rtr.mobs.spritesheet;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import r2d.animation.Animation;
import rtr.SettingsParser;
import rtr.mobs.spritesheet.templates.MobSpriteTemplateBase;
import rtr.utilities.Utilities;

public class MobSprite {
   private Animation[] baseArtAnimation;
   private Animation shadowAnimation;
   private MobSprite.MobAnimationFrames currentMobAnimationFrame;
   private int sheetHeight = MobSprite.MobAnimationFrames.values().length;
   private Animation[][] baseArtAnimationMap = new Animation[this.sheetHeight][];
   private Animation[] shadowAnimationMap = new Animation[this.sheetHeight];
   private HashMap<MobSprite.MobAnimationFrames, Integer> carriedResourceOffsetX = new HashMap<>();
   private HashMap<MobSprite.MobAnimationFrames, Integer> carriedResourceOffsetY = new HashMap<>();
   private int layers;
   private boolean randomFrames;
   private boolean playingAnimation;
   private int randomFramesChance;
   private Color[] colorOut;
   private Color shadowColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
   private Color waterHighlight = new Color(255, 255, 255, 150);

   public MobSprite(
      EnumMap<MobSprite.MobAnimationFrames, Image[]> shadowSheet,
      EnumMap<MobSprite.MobAnimationFrames, ArrayList<Image[]>> baseSheet,
      MobSpriteTemplateBase template
   ) throws SlickException {
      this.layers = template.getLayers();
      this.colorOut = new Color[this.layers];

      for (int l = 0; l < this.layers; l++) {
         this.colorOut[l] = new Color(0, 0, 0);
      }

      for (int x = 0; x < this.sheetHeight; x++) {
         this.baseArtAnimationMap[x] = new Animation[this.layers];
      }

      MobSprite.MobAnimationFrames[] var7;
      for (MobSprite.MobAnimationFrames mA : MobSprite.MobAnimationFrames.values()) {
         int row = mA.getID();
         MobSpriteTemplateBase.MobAnimationConfig animConfig = mA.getConfig();
         this.carriedResourceOffsetX.put(mA, template.getCarriedResourceXOffset(animConfig));
         this.carriedResourceOffsetY.put(mA, template.getCarriedResourceYOffset(animConfig));
         this.shadowAnimationMap[row] = new Animation(shadowSheet.get(mA));
         this.shadowAnimationMap[row].setSpeed(template.getAnimationDurations().get(animConfig));
         this.shadowAnimationMap[row].setMode(template.getAnimationFlags().get(animConfig));

         for (int l = 0; l < this.layers; l++) {
            this.baseArtAnimationMap[row][l] = new Animation(baseSheet.get(mA).get(l));
            this.baseArtAnimationMap[row][l].setSpeed(template.getAnimationDurations().get(animConfig));
            this.baseArtAnimationMap[row][l].setMode(template.getAnimationFlags().get(animConfig));
         }
      }

      this.setPosition(MobSprite.MobAnimationFrames.FACE_NORTH);
   }

   public void render(
      float spriteX,
      float spriteY,
      boolean isGrabbed,
      int depth,
      Color[] colorLayers,
      float alpha,
      float scale,
      boolean isCold,
      boolean isHot,
      boolean isPoisoned,
      boolean isDysentery,
      boolean isBlighted,
      boolean isCaptured
   ) throws SlickException {
      if (!isCaptured && SettingsParser.getShadows()) {
         if (isGrabbed) {
            if (this.shadowColor.a > 0.0F) {
               this.shadowColor.a -= 0.025F;
            }
         } else if (this.shadowColor.a < alpha / 2.5F) {
            this.shadowColor.a += 0.025F;
         }

         if (this.shadowColor.a > 0.0F) {
            this.shadowAnimation
               .render(
                  spriteX - depth,
                  spriteY + 12.0F * scale + depth - (int)(depth * 0.4),
                  16.0F * scale * 2.0F,
                  16.0F * scale - depth,
                  0.0F,
                  depth,
                  32.0F,
                  16.0F,
                  this.shadowColor
               );
         }
      }

      this.baseArtAnimation[0].getCurrentFrame().startUse();

      for (int l = 0; l < this.layers; l++) {
         this.colorOut[l].r = colorLayers[l].r;
         this.colorOut[l].g = colorLayers[l].g;
         this.colorOut[l].b = colorLayers[l].b;
         if (alpha == 1.0F) {
            this.colorOut[l].a = colorLayers[l].a;
         } else {
            this.colorOut[l].a = alpha;
         }

         if (this.colorOut[l].a > 1.0F) {
            this.colorOut[l].a = 1.0F;
         }

         if (this.colorOut[l].a < 0.0F) {
            this.colorOut[l].a = 0.0F;
         }

         if (isCold) {
            this.colorOut[l].b = (float)(this.colorOut[l].b + 0.5);
            this.colorOut[l].g = (float)(this.colorOut[l].g + 0.5);
         }

         if (isHot) {
            this.colorOut[l].r = (float)(this.colorOut[l].r + 0.5);
         }

         if (isPoisoned) {
            this.colorOut[l].g = (float)(this.colorOut[l].g + 0.5);
         }

         if (isDysentery) {
            this.colorOut[l] = this.colorOut[l].darker(0.3F);
         }

         if (isBlighted) {
            this.colorOut[l] = this.colorOut[l].darker(0.7F);
         }

         if (depth > 0) {
            this.baseArtAnimation[l]
               .renderEmbedded(spriteX, spriteY + depth, 16.0F * scale, 16.0F * scale - depth + 1.0F, 0.0F, 0.0F, 16.0F, 16 - depth + 1, this.waterHighlight);
         }

         if (isCaptured) {
            this.baseArtAnimation[l]
               .renderEmbedded(spriteX, spriteY + depth, 16.0F * scale, 16.0F * scale - depth, 0.0F, 0.0F, 16.0F, 16 - depth, this.colorOut[l], false, true);
         } else {
            this.baseArtAnimation[l]
               .renderEmbedded(spriteX, spriteY + depth, 16.0F * scale, 16.0F * scale - depth, 0.0F, 0.0F, 16.0F, 16 - depth, this.colorOut[l]);
         }
      }

      this.baseArtAnimation[0].getCurrentFrame().endUse();
   }

   public void renderMapWriter(
      float spriteX,
      float spriteY,
      boolean isGrabbed,
      int depth,
      Color[] colorLayers,
      float alpha,
      boolean isCold,
      boolean isHot,
      boolean isPoisoned,
      boolean isDysentery,
      boolean isBlighted,
      boolean isCaptured
   ) {
      if (isGrabbed) {
         if (this.shadowColor.a > 0.0F) {
            this.shadowColor.a -= 0.025F;
         }
      } else if (this.shadowColor.a < alpha / 2.5F) {
         this.shadowColor.a += 0.025F;
      }

      if (this.shadowColor.a > 0.0F) {
         this.shadowAnimation
            .render(spriteX - depth, spriteY + 12.0F + depth - (int)(depth * 0.4), 32.0F, 16 - depth, 0.0F, depth, 32.0F, 16.0F, this.shadowColor);
      }

      this.baseArtAnimation[0].getCurrentFrame().startUse();

      for (int l = 0; l < this.layers; l++) {
         this.colorOut[l].r = colorLayers[l].r;
         this.colorOut[l].g = colorLayers[l].g;
         this.colorOut[l].b = colorLayers[l].b;
         if (alpha == 1.0F) {
            this.colorOut[l].a = colorLayers[l].a;
         } else {
            this.colorOut[l].a = alpha;
         }

         if (this.colorOut[l].a > 1.0F) {
            this.colorOut[l].a = 1.0F;
         }

         if (this.colorOut[l].a < 0.0F) {
            this.colorOut[l].a = 0.0F;
         }

         if (isCold) {
            this.colorOut[l].b = (float)(this.colorOut[l].b + 0.5);
            this.colorOut[l].g = (float)(this.colorOut[l].g + 0.5);
         }

         if (isHot) {
            this.colorOut[l].r = (float)(this.colorOut[l].r + 0.5);
         }

         if (isPoisoned) {
            this.colorOut[l].g = (float)(this.colorOut[l].g + 0.5);
         }

         if (isDysentery) {
            this.colorOut[l] = this.colorOut[l].darker(0.3F);
         }

         if (isBlighted) {
            this.colorOut[l] = this.colorOut[l].darker(0.7F);
         }

         if (depth > 0) {
            this.baseArtAnimation[l].renderEmbedded(spriteX, spriteY + depth, 16.0F, 16 - depth + 1, 0.0F, 0.0F, 16.0F, 16 - depth + 1, this.waterHighlight);
         }

         this.baseArtAnimation[l].renderEmbedded(spriteX, spriteY + depth, 16.0F, 16 - depth, 0.0F, 0.0F, 16.0F, 16 - depth, this.colorOut[l]);
      }

      this.baseArtAnimation[0].getCurrentFrame().endUse();
   }

   public void update() throws SlickException {
      this.update(0);
   }

   public void update(int speedAdjust) throws SlickException {
      if (this.randomFrames && Utilities.randomInt(this.randomFramesChance) == 0) {
         this.randomFrame();
      }

      this.shadowAnimation.update(speedAdjust);

      for (int l = 0; l < this.layers; l++) {
         this.baseArtAnimation[l].update(speedAdjust);
      }

      if (this.playingAnimation && this.baseArtAnimation[0].isStopped()) {
         this.playingAnimation = false;
      }
   }

   public void forcePlayAnimation(MobSprite.MobAnimationFrames a) {
      this.currentMobAnimationFrame = a;
      this.shadowAnimation = this.shadowAnimationMap[a.getID()];
      this.baseArtAnimation = this.baseArtAnimationMap[a.getID()];

      for (int l = 0; l < this.layers; l++) {
         this.baseArtAnimationMap[a.getID()][l].restart();
      }

      this.shadowAnimationMap[a.getID()].restart();
      this.playingAnimation = true;
   }

   public void setPosition(MobSprite.MobAnimationFrames position) {
      this.currentMobAnimationFrame = position;
      if (!this.playingAnimation) {
         this.shadowAnimation = this.shadowAnimationMap[position.getID()];
         this.baseArtAnimation = this.baseArtAnimationMap[position.getID()];
         if (this.baseArtAnimation[0].getMode() == Animation.AnimationMode.MANUAL_RANDOM) {
            this.randomFrames = true;
            this.randomFramesChance = this.baseArtAnimationMap[position.getID()][0].getSpeed();
         } else {
            this.randomFrames = false;
         }
      }
   }

   public void setFrame(int i) {
      this.shadowAnimation.setFrame(i);

      for (int l = 0; l < this.layers; l++) {
         this.baseArtAnimation[l].setFrame(i);
      }
   }

   public void nextFrame() {
      this.shadowAnimation.nextFrame();

      for (int l = 0; l < this.layers; l++) {
         this.baseArtAnimation[l].nextFrame();
      }
   }

   public void randomFrame() {
      int randomFrame = Utilities.randomInt(this.baseArtAnimation[0].getFrameCount());
      this.shadowAnimation.setFrame(randomFrame);

      for (int l = 0; l < this.layers; l++) {
         this.baseArtAnimation[l].setFrame(randomFrame);
      }
   }

   public void restart() {
      this.shadowAnimation.restart();

      for (int l = 0; l < this.layers; l++) {
         this.baseArtAnimation[l].restart();
      }
   }

   public int getSpriteWidth() {
      return this.baseArtAnimation[0].getCurrentFrame().getWidth();
   }

   public int getSpriteHeight() {
      return this.baseArtAnimation[0].getCurrentFrame().getHeight();
   }

   public boolean isStopped() {
      return this.baseArtAnimation[0].isStopped();
   }

   public Integer getCarriedResourceOffsetX() {
      return this.carriedResourceOffsetX.get(this.currentMobAnimationFrame);
   }

   public Integer getCarriedResourceOffsetY() {
      return this.carriedResourceOffsetY.get(this.currentMobAnimationFrame);
   }

   public static enum MobAnimationFrames {
      WALK_NORTH(0, MobSpriteTemplateBase.MobAnimationConfig.WALK),
      WALK_EAST(1, MobSpriteTemplateBase.MobAnimationConfig.WALK),
      WALK_SOUTH(2, MobSpriteTemplateBase.MobAnimationConfig.WALK),
      WALK_WEST(3, MobSpriteTemplateBase.MobAnimationConfig.WALK),
      FACE_NORTH(4, MobSpriteTemplateBase.MobAnimationConfig.FACE),
      FACE_EAST(5, MobSpriteTemplateBase.MobAnimationConfig.FACE),
      FACE_SOUTH(6, MobSpriteTemplateBase.MobAnimationConfig.FACE),
      FACE_WEST(7, MobSpriteTemplateBase.MobAnimationConfig.FACE),
      DEATH_NORTH(8, MobSpriteTemplateBase.MobAnimationConfig.DEATH),
      DEATH_EAST(9, MobSpriteTemplateBase.MobAnimationConfig.DEATH),
      DEATH_SOUTH(10, MobSpriteTemplateBase.MobAnimationConfig.DEATH),
      DEATH_WEST(11, MobSpriteTemplateBase.MobAnimationConfig.DEATH),
      DECAY_NORTH(12, MobSpriteTemplateBase.MobAnimationConfig.DECAY),
      DECAY_EAST(13, MobSpriteTemplateBase.MobAnimationConfig.DECAY),
      DECAY_SOUTH(14, MobSpriteTemplateBase.MobAnimationConfig.DECAY),
      DECAY_WEST(15, MobSpriteTemplateBase.MobAnimationConfig.DECAY),
      ATTACK_NORTH(16, MobSpriteTemplateBase.MobAnimationConfig.ATTACK),
      ATTACK_EAST(17, MobSpriteTemplateBase.MobAnimationConfig.ATTACK),
      ATTACK_SOUTH(18, MobSpriteTemplateBase.MobAnimationConfig.ATTACK),
      ATTACK_WEST(19, MobSpriteTemplateBase.MobAnimationConfig.ATTACK),
      CARRY_NORTH(20, MobSpriteTemplateBase.MobAnimationConfig.CARRY),
      CARRY_EAST(21, MobSpriteTemplateBase.MobAnimationConfig.CARRY),
      CARRY_SOUTH(22, MobSpriteTemplateBase.MobAnimationConfig.CARRY),
      CARRY_WEST(23, MobSpriteTemplateBase.MobAnimationConfig.CARRY),
      HOLD_NORTH(24, MobSpriteTemplateBase.MobAnimationConfig.HOLD),
      HOLD_EAST(25, MobSpriteTemplateBase.MobAnimationConfig.HOLD),
      HOLD_SOUTH(26, MobSpriteTemplateBase.MobAnimationConfig.HOLD),
      HOLD_WEST(27, MobSpriteTemplateBase.MobAnimationConfig.HOLD),
      INTERACT_TOOL_NORTH(28, MobSpriteTemplateBase.MobAnimationConfig.INTERACT_TOOL),
      INTERACT_TOOL_EAST(29, MobSpriteTemplateBase.MobAnimationConfig.INTERACT_TOOL),
      INTERACT_TOOL_SOUTH(30, MobSpriteTemplateBase.MobAnimationConfig.INTERACT_TOOL),
      INTERACT_TOOL_WEST(31, MobSpriteTemplateBase.MobAnimationConfig.INTERACT_TOOL),
      INTERACT_NORTH(32, MobSpriteTemplateBase.MobAnimationConfig.INTERACT),
      INTERACT_EAST(33, MobSpriteTemplateBase.MobAnimationConfig.INTERACT),
      INTERACT_SOUTH(34, MobSpriteTemplateBase.MobAnimationConfig.INTERACT),
      INTERACT_WEST(35, MobSpriteTemplateBase.MobAnimationConfig.INTERACT),
      HIT_NORTH(36, MobSpriteTemplateBase.MobAnimationConfig.HIT),
      HIT_EAST(37, MobSpriteTemplateBase.MobAnimationConfig.HIT),
      HIT_SOUTH(38, MobSpriteTemplateBase.MobAnimationConfig.HIT),
      HIT_WEST(39, MobSpriteTemplateBase.MobAnimationConfig.HIT),
      SLEEP_NORTH(40, MobSpriteTemplateBase.MobAnimationConfig.SLEEP),
      SLEEP_EAST(41, MobSpriteTemplateBase.MobAnimationConfig.SLEEP),
      SLEEP_SOUTH(42, MobSpriteTemplateBase.MobAnimationConfig.SLEEP),
      SLEEP_WEST(43, MobSpriteTemplateBase.MobAnimationConfig.SLEEP),
      FLEE_NORTH(44, MobSpriteTemplateBase.MobAnimationConfig.FLEE),
      FLEE_EAST(45, MobSpriteTemplateBase.MobAnimationConfig.FLEE),
      FLEE_SOUTH(46, MobSpriteTemplateBase.MobAnimationConfig.FLEE),
      FLEE_WEST(47, MobSpriteTemplateBase.MobAnimationConfig.FLEE),
      EAT_NORTH(48, MobSpriteTemplateBase.MobAnimationConfig.EAT),
      EAT_EAST(49, MobSpriteTemplateBase.MobAnimationConfig.EAT),
      EAT_SOUTH(50, MobSpriteTemplateBase.MobAnimationConfig.EAT),
      EAT_WEST(51, MobSpriteTemplateBase.MobAnimationConfig.EAT),
      DRINK_NORTH(52, MobSpriteTemplateBase.MobAnimationConfig.DRINK),
      DRINK_EAST(53, MobSpriteTemplateBase.MobAnimationConfig.DRINK),
      DRINK_SOUTH(54, MobSpriteTemplateBase.MobAnimationConfig.DRINK),
      DRINK_WEST(55, MobSpriteTemplateBase.MobAnimationConfig.DRINK),
      DRINK_BOTTLE_NORTH(56, MobSpriteTemplateBase.MobAnimationConfig.DRINK_BOTTLE),
      DRINK_BOTTLE_EAST(57, MobSpriteTemplateBase.MobAnimationConfig.DRINK_BOTTLE),
      DRINK_BOTTLE_SOUTH(58, MobSpriteTemplateBase.MobAnimationConfig.DRINK_BOTTLE),
      DRINK_BOTTLE_WEST(59, MobSpriteTemplateBase.MobAnimationConfig.DRINK_BOTTLE),
      BOW_IDLE_NORTH(60, MobSpriteTemplateBase.MobAnimationConfig.BOW_IDLE),
      BOW_IDLE_EAST(61, MobSpriteTemplateBase.MobAnimationConfig.BOW_IDLE),
      BOW_IDLE_SOUTH(62, MobSpriteTemplateBase.MobAnimationConfig.BOW_IDLE),
      BOW_IDLE_WEST(63, MobSpriteTemplateBase.MobAnimationConfig.BOW_IDLE),
      BOW_FIRE_NORTH(64, MobSpriteTemplateBase.MobAnimationConfig.BOW_FIRE),
      BOW_FIRE_EAST(65, MobSpriteTemplateBase.MobAnimationConfig.BOW_FIRE),
      BOW_FIRE_SOUTH(66, MobSpriteTemplateBase.MobAnimationConfig.BOW_FIRE),
      BOW_FIRE_WEST(67, MobSpriteTemplateBase.MobAnimationConfig.BOW_FIRE),
      PRAY_NORTH(68, MobSpriteTemplateBase.MobAnimationConfig.PRAY),
      PRAY_EAST(69, MobSpriteTemplateBase.MobAnimationConfig.PRAY),
      PRAY_SOUTH(70, MobSpriteTemplateBase.MobAnimationConfig.PRAY),
      PRAY_WEST(71, MobSpriteTemplateBase.MobAnimationConfig.PRAY),
      PRAY_FINISHING_NORTH(72, MobSpriteTemplateBase.MobAnimationConfig.PRAY_FINISHING),
      PRAY_FINISHING_EAST(73, MobSpriteTemplateBase.MobAnimationConfig.PRAY_FINISHING),
      PRAY_FINISHING_SOUTH(74, MobSpriteTemplateBase.MobAnimationConfig.PRAY_FINISHING),
      PRAY_FINISHING_WEST(75, MobSpriteTemplateBase.MobAnimationConfig.PRAY_FINISHING),
      PICKUP_NORTH(76, MobSpriteTemplateBase.MobAnimationConfig.PICKUP),
      PICKUP_EAST(77, MobSpriteTemplateBase.MobAnimationConfig.PICKUP),
      PICKUP_SOUTH(78, MobSpriteTemplateBase.MobAnimationConfig.PICKUP),
      PICKUP_WEST(79, MobSpriteTemplateBase.MobAnimationConfig.PICKUP),
      LIFT_EQUIP_NORTH(80, MobSpriteTemplateBase.MobAnimationConfig.LIFT_EQUIP),
      LIFT_EQUIP_EAST(81, MobSpriteTemplateBase.MobAnimationConfig.LIFT_EQUIP),
      LIFT_EQUIP_SOUTH(82, MobSpriteTemplateBase.MobAnimationConfig.LIFT_EQUIP),
      LIFT_EQUIP_WEST(83, MobSpriteTemplateBase.MobAnimationConfig.LIFT_EQUIP),
      LIFT_HAUL_NORTH(84, MobSpriteTemplateBase.MobAnimationConfig.LIFT_HAUL),
      LIFT_HAUL_EAST(85, MobSpriteTemplateBase.MobAnimationConfig.LIFT_HAUL),
      LIFT_HAUL_SOUTH(86, MobSpriteTemplateBase.MobAnimationConfig.LIFT_HAUL),
      LIFT_HAUL_WEST(87, MobSpriteTemplateBase.MobAnimationConfig.LIFT_HAUL),
      DROP_NORTH(88, MobSpriteTemplateBase.MobAnimationConfig.DROP),
      DROP_EAST(89, MobSpriteTemplateBase.MobAnimationConfig.DROP),
      DROP_SOUTH(90, MobSpriteTemplateBase.MobAnimationConfig.DROP),
      DROP_WEST(91, MobSpriteTemplateBase.MobAnimationConfig.DROP),
      INTERACT_GROUND_TOOL_NORTH(92, MobSpriteTemplateBase.MobAnimationConfig.INTERACT_GROUND_TOOL),
      INTERACT_GROUND_TOOL_EAST(93, MobSpriteTemplateBase.MobAnimationConfig.INTERACT_GROUND_TOOL),
      INTERACT_GROUND_TOOL_SOUTH(94, MobSpriteTemplateBase.MobAnimationConfig.INTERACT_GROUND_TOOL),
      INTERACT_GROUND_TOOL_WEST(95, MobSpriteTemplateBase.MobAnimationConfig.INTERACT_GROUND_TOOL),
      INTERACT_GROUND_NORTH(96, MobSpriteTemplateBase.MobAnimationConfig.INTERACT_GROUND),
      INTERACT_GROUND_EAST(97, MobSpriteTemplateBase.MobAnimationConfig.INTERACT_GROUND),
      INTERACT_GROUND_SOUTH(98, MobSpriteTemplateBase.MobAnimationConfig.INTERACT_GROUND),
      INTERACT_GROUND_WEST(99, MobSpriteTemplateBase.MobAnimationConfig.INTERACT_GROUND);

      private final int mobAnimID;
      private final MobSpriteTemplateBase.MobAnimationConfig mobAnimConfig;

      private MobAnimationFrames(int mobAnimID, MobSpriteTemplateBase.MobAnimationConfig mobAnimConfig) {
         this.mobAnimID = mobAnimID;
         this.mobAnimConfig = mobAnimConfig;
      }

      public int getID() {
         return this.mobAnimID;
      }

      public MobSpriteTemplateBase.MobAnimationConfig getConfig() {
         return this.mobAnimConfig;
      }
   }
}
