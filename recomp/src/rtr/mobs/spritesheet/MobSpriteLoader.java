package rtr.mobs.spritesheet;

import java.util.ArrayList;
import java.util.EnumMap;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import r2d.image.SpriteSheet;
import rtr.ImageLoader;
import rtr.mobs.MobBase;
import rtr.mobs.spritesheet.templates.MobSpriteTemplateBase;
import rtr.mobs.spritesheet.templates.MobSpriteTemplateBeefalo;
import rtr.mobs.spritesheet.templates.MobSpriteTemplateCatjeet;
import rtr.mobs.spritesheet.templates.MobSpriteTemplateCatjeetGhost;
import rtr.mobs.spritesheet.templates.MobSpriteTemplateCatjeetZombie;
import rtr.mobs.spritesheet.templates.MobSpriteTemplateClucker;
import rtr.mobs.spritesheet.templates.MobSpriteTemplateCourierGolem;
import rtr.mobs.spritesheet.templates.MobSpriteTemplateCubeEGolem;
import rtr.mobs.spritesheet.templates.MobSpriteTemplateDoggo;
import rtr.mobs.spritesheet.templates.MobSpriteTemplateEntler;
import rtr.mobs.spritesheet.templates.MobSpriteTemplateFireElemental;
import rtr.mobs.spritesheet.templates.MobSpriteTemplateGhost;
import rtr.mobs.spritesheet.templates.MobSpriteTemplateGolem;
import rtr.mobs.spritesheet.templates.MobSpriteTemplateHeadless;
import rtr.mobs.spritesheet.templates.MobSpriteTemplateRous;
import rtr.mobs.spritesheet.templates.MobSpriteTemplateSkeleton;
import rtr.mobs.spritesheet.templates.MobSpriteTemplateSlime;
import rtr.mobs.spritesheet.templates.MobSpriteTemplateSmallSlime;
import rtr.mobs.spritesheet.templates.MobSpriteTemplateVillager;
import rtr.mobs.spritesheet.templates.MobSpriteTemplateZombie;

public class MobSpriteLoader {
   private static final int ANIMATION_COUNT = 101;
   private static final int ANIMATION_FRAMES = 8;
   private EnumMap<MobBase.MobType, EnumMap<MobBase.MobGender, EnumMap<MobSprite.MobAnimationFrames, ArrayList<Image[]>>>> baseArtSpriteSheets = new EnumMap<>(
      MobBase.MobType.class
   );
   private EnumMap<MobBase.MobType, EnumMap<MobBase.MobGender, EnumMap<MobSprite.MobAnimationFrames, Image[]>>> shadowSpriteSheets = new EnumMap<>(
      MobBase.MobType.class
   );
   private EnumMap<MobBase.MobType, EnumMap<MobBase.MobGender, MobSpriteTemplateBase>> spriteTemplates = new EnumMap<>(MobBase.MobType.class);

   public MobSpriteLoader() throws SlickException {
      MobSpriteTemplateBase template = new MobSpriteTemplateVillager();
      int spritePositionMale = 0;
      int spritePositionFemale = 1;
      this.loadMobAllGenders(MobBase.MobType.HUMAN_VILLAGER, template, spritePositionMale, spritePositionFemale);
      this.loadMobAllGenders(MobBase.MobType.HUMAN_MIGRANT_ARRIVING, template, spritePositionMale, spritePositionFemale);
      this.loadMobAllGenders(MobBase.MobType.HUMAN_MIGRANT_DEPARTING, template, spritePositionMale, spritePositionFemale);
      this.loadMobAllGenders(MobBase.MobType.HUMAN_NOMAD, template, spritePositionMale, spritePositionFemale);
      this.loadMobAllGenders(MobBase.MobType.HUMAN_NOMAD_ELDER, template, spritePositionMale, spritePositionFemale);
      this.loadMobAllGenders(MobBase.MobType.HUMAN_VILLAGER_ELDER, template, spritePositionMale, spritePositionFemale);
      this.loadMobAllGenders(MobBase.MobType.HUMAN_MIGRANT_NEPHILIM_ARRIVING, template, spritePositionMale, spritePositionFemale);
      this.loadMobAllGenders(MobBase.MobType.HUMAN_MIGRANT_NEPHILIM_DEPARTING, template, spritePositionMale, spritePositionFemale);
      this.loadMobAllGenders(MobBase.MobType.HUMAN_NOMAD_NEPHILIM, template, spritePositionMale, spritePositionFemale);
      this.loadMobAllGenders(MobBase.MobType.DRONE, template, spritePositionMale, spritePositionFemale);
      this.loadMobAllGenders(MobBase.MobType.HUMAN_NOMAD_ELDER_NEPHILIM, template, spritePositionMale, spritePositionFemale);
      this.loadMobAllGenders(MobBase.MobType.HUMAN_VILLAGER_NEPHILIM, template, spritePositionMale, spritePositionFemale);
      this.loadMobAllGenders(MobBase.MobType.HUMAN_VILLAGER_ELDER_NEPHILIM, template, spritePositionMale, spritePositionFemale);
      template = new MobSpriteTemplateCatjeet();
      int var28 = 2;
      int var52 = 3;
      this.loadMobAllGenders(MobBase.MobType.CATJEET_VILLAGER, template, var28, var52);
      this.loadMobAllGenders(MobBase.MobType.CATJEET_MIGRANT_ARRIVING, template, var28, var52);
      this.loadMobAllGenders(MobBase.MobType.CATJEET_MIGRANT_DEPARTING, template, var28, var52);
      this.loadMobAllGenders(MobBase.MobType.CATJEET_NOMAD, template, var28, var52);
      this.loadMobAllGenders(MobBase.MobType.CATJEET_NOMAD_ELDER, template, var28, var52);
      this.loadMobAllGenders(MobBase.MobType.CATJEET_PROVISIONER, template, var28, var52);
      this.loadMobAllGenders(MobBase.MobType.CATJEET_LABORER, template, var28, var52);
      this.loadMobAllGenders(MobBase.MobType.CATJEET_VILLAGER_ELDER, template, var28, var52);
      this.loadMobAllGenders(MobBase.MobType.CATJEET_MIGRANT_NEPHILIM_ARRIVING, template, var28, var52);
      this.loadMobAllGenders(MobBase.MobType.CATJEET_MIGRANT_NEPHILIM_DEPARTING, template, var28, var52);
      this.loadMobAllGenders(MobBase.MobType.CATJEET_NOMAD_NEPHILIM, template, var28, var52);
      this.loadMobAllGenders(MobBase.MobType.CATJEET_NOMAD_ELDER_NEPHILIM, template, var28, var52);
      this.loadMobAllGenders(MobBase.MobType.CATJEET_VILLAGER_NEPHILIM, template, var28, var52);
      this.loadMobAllGenders(MobBase.MobType.CATJEET_VILLAGER_ELDER_NEPHILIM, template, var28, var52);
      template = new MobSpriteTemplateVillager();
      var28 = 4;
      this.loadMobOneGender(MobBase.MobType.HUMAN_VILLAGER_CHILD, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.HUMAN_NOMAD_CHILD, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.HUMAN_NOMAD_CHILD_NEPHILIM, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.HUMAN_VILLAGER_CHILD_NEPHILIM, MobBase.MobGender.NONE, template, var28);
      template = new MobSpriteTemplateCatjeet();
      var28 = 5;
      this.loadMobOneGender(MobBase.MobType.CATJEET_VILLAGER_CHILD, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.CATJEET_NOMAD_CHILD, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.CATJEET_NOMAD_CHILD_NEPHILIM, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.CATJEET_VILLAGER_CHILD_NEPHILIM, MobBase.MobGender.NONE, template, var28);
      template = new MobSpriteTemplateZombie();
      var28 = 6;
      var52 = 7;
      this.loadMobAllGenders(MobBase.MobType.ZOMBIE, template, var28, var52);
      template = new MobSpriteTemplateCatjeetZombie();
      var28 = 8;
      var52 = 9;
      this.loadMobAllGenders(MobBase.MobType.ZOMBIE_CATJEET, template, var28, var52);
      template = new MobSpriteTemplateZombie();
      var28 = 10;
      this.loadMobOneGender(MobBase.MobType.ZOMBIE_CHILD, MobBase.MobGender.NONE, template, var28);
      template = new MobSpriteTemplateCatjeetZombie();
      var28 = 11;
      this.loadMobOneGender(MobBase.MobType.ZOMBIE_CATJEET_CHILD, MobBase.MobGender.NONE, template, var28);
      template = new MobSpriteTemplateGhost();
      var28 = 12;
      var52 = 13;
      this.loadMobAllGenders(MobBase.MobType.HUMAN_VILLAGER_GHOST, template, var28, var52);
      this.loadMobAllGenders(MobBase.MobType.HUMAN_VILLAGER_GHOST_NEPHILIM, template, var28, var52);
      this.loadMobAllGenders(MobBase.MobType.SPECTER, template, var28, var52);
      template = new MobSpriteTemplateCatjeetGhost();
      var28 = 14;
      var52 = 15;
      this.loadMobAllGenders(MobBase.MobType.CATJEET_GHOST, template, var28, var52);
      this.loadMobAllGenders(MobBase.MobType.CATJEET_GHOST_NEPHILIM, template, var28, var52);
      template = new MobSpriteTemplateGhost();
      var28 = 16;
      this.loadMobOneGender(MobBase.MobType.HUMAN_VILLAGER_GHOST_CHILD, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.HUMAN_VILLAGER_GHOST_CHILD_NEPHILIM, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.SPECTER_CHILD, MobBase.MobGender.NONE, template, var28);
      template = new MobSpriteTemplateCatjeetGhost();
      var28 = 17;
      this.loadMobOneGender(MobBase.MobType.CATJEET_GHOST_CHILD, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.CATJEET_GHOST_CHILD_NEPHILIM, MobBase.MobGender.NONE, template, var28);
      template = new MobSpriteTemplateFireElemental();
      var28 = 18;
      this.loadMobOneGender(MobBase.MobType.FIRE_ELEMENTAL, MobBase.MobGender.NONE, template, var28);
      template = new MobSpriteTemplateGolem();
      var28 = 18;
      this.loadMobOneGender(MobBase.MobType.STONE_GOLEM, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.WOOD_GOLEM, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.CRYSTAL_GOLEM, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.HOLY_GOLEM, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.LABOR_GOLEM, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.POISON_WOOD_GOLEM, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.FIRE_STONE_GOLEM, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.FIRE_CRYSTAL_GOLEM, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.ICE_STONE_GOLEM, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.ICE_WOOD_GOLEM, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.ICE_CRYSTAL_GOLEM, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.LIGHTNING_STONE_GOLEM, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.LIGHTNING_WOOD_GOLEM, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.LIGHTNING_CRYSTAL_GOLEM, MobBase.MobGender.NONE, template, var28);
      template = new MobSpriteTemplateCubeEGolem();
      var28 = 19;
      this.loadMobOneGender(MobBase.MobType.CUBE_E_GOLEM, MobBase.MobGender.NONE, template, var28);
      template = new MobSpriteTemplateCourierGolem();
      var28 = 20;
      this.loadMobOneGender(MobBase.MobType.COURIER_GOLEM, MobBase.MobGender.NONE, template, var28);
      template = new MobSpriteTemplateHeadless();
      var28 = 21;
      this.loadMobOneGender(MobBase.MobType.HEADLESS, MobBase.MobGender.NONE, template, var28);
      template = new MobSpriteTemplateSkeleton();
      var28 = 22;
      this.loadMobOneGender(MobBase.MobType.SKELETON, MobBase.MobGender.NONE, template, var28);
      template = new MobSpriteTemplateSmallSlime();
      var28 = 23;
      this.loadMobOneGender(MobBase.MobType.SMALL_SLIME, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.SMALL_TRASHY_SLIME, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.SMALL_BLOOD_SLIME, MobBase.MobGender.NONE, template, var28);
      template = new MobSpriteTemplateSlime();
      var28 = 24;
      this.loadMobOneGender(MobBase.MobType.SLIME, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.TRASHY_SLIME, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.BLOOD_SLIME, MobBase.MobGender.NONE, template, var28);
      template = new MobSpriteTemplateDoggo();
      var28 = 25;
      this.loadMobOneGender(MobBase.MobType.DOGGO, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.WILD_DOGGO, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.ZOMBIE_DOGGO, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.GHOST_DOGGO, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.DOOFY_DOGGO, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.WILD_DOOFY_DOGGO, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.ZOMBIE_DOOFY_DOGGO, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.GHOST_DOOFY_DOGGO, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.ROGGO_GOLEM, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.LOGGO_GOLEM, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.CRYGGO_GOLEM, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.GODDO_GOLEM, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.POISON_LOGGO_GOLEM, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.FIRE_ROGGO_GOLEM, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.FIRE_CRYGGO_GOLEM, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.ICE_ROGGO_GOLEM, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.ICE_LOGGO_GOLEM, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.ICE_CRYGGO_GOLEM, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.LIGHTNING_ROGGO_GOLEM, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.LIGHTNING_LOGGO_GOLEM, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.LIGHTNING_CRYGGO_GOLEM, MobBase.MobGender.NONE, template, var28);
      template = new MobSpriteTemplateEntler();
      var28 = 26;
      this.loadMobOneGender(MobBase.MobType.WILD_ENTLER, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.ZOMBIE_ENTLER, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.ENTLER, MobBase.MobGender.NONE, template, var28);
      template = new MobSpriteTemplateBeefalo();
      var28 = 27;
      this.loadMobOneGender(MobBase.MobType.WILD_BEEFALO, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.ZOMBIE_BEEFALO, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.BEEFALO, MobBase.MobGender.NONE, template, var28);
      template = new MobSpriteTemplateRous();
      var28 = 28;
      this.loadMobOneGender(MobBase.MobType.WILD_ROUS, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.ZOMBIE_ROUS, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.ROUS, MobBase.MobGender.NONE, template, var28);
      template = new MobSpriteTemplateClucker();
      var28 = 29;
      this.loadMobOneGender(MobBase.MobType.WILD_CLUCKER, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.ZOMBIE_CLUCKER, MobBase.MobGender.NONE, template, var28);
      this.loadMobOneGender(MobBase.MobType.CLUCKER, MobBase.MobGender.NONE, template, var28);
   }

   private void loadMobAllGenders(MobBase.MobType type, MobSpriteTemplateBase template, int spritePositionMale, int spritePositionFemale) throws SlickException {
      this.loadMobOneGender(type, MobBase.MobGender.NONE, template, spritePositionMale);
      this.loadMobOneGender(type, MobBase.MobGender.MALE, template, spritePositionMale);
      this.loadMobOneGender(type, MobBase.MobGender.FEMALE, template, spritePositionFemale);
   }

   /**
    * Sprite sheets of every mob type used to be cut up at start-up (dozens of type/gender combinations, thousands of
    * Image objects each), though a game only meets a few of them. They are now registered here and cut up the first
    * time a mob of that type is created.
    */
   private final EnumMap<MobBase.MobType, EnumMap<MobBase.MobGender, Integer>> pendingSheets = new EnumMap<>(MobBase.MobType.class);

   private void loadMobOneGender(MobBase.MobType type, MobBase.MobGender gender, MobSpriteTemplateBase template, int spritePosition) throws SlickException {
      if (!this.spriteTemplates.containsKey(type)) {
         this.spriteTemplates.put(type, new EnumMap<>(MobBase.MobGender.class));
      }

      this.spriteTemplates.get(type).put(gender, template);
      if (this.baseArtSpriteSheets.get(type) == null) {
         this.baseArtSpriteSheets.put(type, new EnumMap<>(MobBase.MobGender.class));
      }

      if (this.shadowSpriteSheets.get(type) == null) {
         this.shadowSpriteSheets.put(type, new EnumMap<>(MobBase.MobGender.class));
      }

      if (!this.pendingSheets.containsKey(type)) {
         this.pendingSheets.put(type, new EnumMap<>(MobBase.MobGender.class));
      }

      this.pendingSheets.get(type).put(gender, spritePosition);
   }

   private synchronized void ensureLoaded(MobBase.MobType type, MobBase.MobGender gender) throws SlickException {
      EnumMap<MobBase.MobGender, Integer> pending = this.pendingSheets.get(type);
      Integer spritePosition = pending == null ? null : pending.remove(gender);
      if (spritePosition != null) {
         this.cutSheets(type, gender, this.spriteTemplates.get(type).get(gender), spritePosition);
      }
   }

   private void cutSheets(MobBase.MobType type, MobBase.MobGender gender, MobSpriteTemplateBase template, int spritePosition) throws SlickException {
      int layers = this.spriteTemplates.get(type).get(gender).getLayers();
      SpriteSheet baseArtSheet = new SpriteSheet(ImageLoader.getImage("res/mobs/" + spritePosition + ".png"), 128, 1616);
      SpriteSheet shadowSheet = new SpriteSheet(ImageLoader.getImage("res/mobs/" + spritePosition + "shadow.png"), 32, 16);
      int posX = 0;
      int posY = 0;
      SpriteSheet[] baseArtSheets = new SpriteSheet[layers];

      for (int i = 0; i < baseArtSheets.length; i++) {
         baseArtSheets[i] = new SpriteSheet(baseArtSheet.getSprite(posX, posY), 16, 16);
         if (++posX >= baseArtSheet.getHorizontalCount()) {
            posX = 0;
            posY++;
         }
      }

      EnumMap<MobSprite.MobAnimationFrames, ArrayList<Image[]>> processedBaseArtOut = new EnumMap<>(MobSprite.MobAnimationFrames.class);
      EnumMap<MobSprite.MobAnimationFrames, Image[]> processedShadowOut = new EnumMap<>(MobSprite.MobAnimationFrames.class);

      MobSprite.MobAnimationFrames[] var16;
      for (MobSprite.MobAnimationFrames mA : MobSprite.MobAnimationFrames.values()) {
         int row = mA.getID();
         processedBaseArtOut.put(mA, new ArrayList<>());

         for (int l = 0; l < layers; l++) {
            Image[] animationImages = new Image[]{
               baseArtSheets[l].getSprite(0, row),
               baseArtSheets[l].getSprite(1, row),
               baseArtSheets[l].getSprite(2, row),
               baseArtSheets[l].getSprite(3, row),
               baseArtSheets[l].getSprite(4, row),
               baseArtSheets[l].getSprite(5, row),
               baseArtSheets[l].getSprite(6, row),
               baseArtSheets[l].getSprite(7, row)
            };
            processedBaseArtOut.get(mA).add(animationImages);
         }

         Image[] animationImages = new Image[]{
            shadowSheet.getSprite(0, row),
            shadowSheet.getSprite(1, row),
            shadowSheet.getSprite(2, row),
            shadowSheet.getSprite(3, row),
            shadowSheet.getSprite(4, row),
            shadowSheet.getSprite(5, row),
            shadowSheet.getSprite(6, row),
            shadowSheet.getSprite(7, row)
         };
         processedShadowOut.put(mA, animationImages);
      }

      if (this.baseArtSpriteSheets.get(type) == null) {
         this.baseArtSpriteSheets.put(type, new EnumMap<>(MobBase.MobGender.class));
      }

      if (this.shadowSpriteSheets.get(type) == null) {
         this.shadowSpriteSheets.put(type, new EnumMap<>(MobBase.MobGender.class));
      }

      this.baseArtSpriteSheets.get(type).put(gender, processedBaseArtOut);
      this.shadowSpriteSheets.get(type).put(gender, processedShadowOut);
   }

   public MobSprite createNewMobSprite(MobBase.MobType type, MobBase.MobGender gender) throws SlickException {
      this.ensureLoaded(type, gender);
      if (this.baseArtSpriteSheets.get(type).get(gender) == null) {
         this.ensureLoaded(type, MobBase.MobGender.NONE);
      }

      return this.baseArtSpriteSheets.get(type).get(gender) != null
         ? new MobSprite(
            this.shadowSpriteSheets.get(type).get(gender), this.baseArtSpriteSheets.get(type).get(gender), this.spriteTemplates.get(type).get(gender)
         )
         : new MobSprite(
            this.shadowSpriteSheets.get(type).get(MobBase.MobGender.NONE),
            this.baseArtSpriteSheets.get(type).get(MobBase.MobGender.NONE),
            this.spriteTemplates.get(type).get(MobBase.MobGender.NONE)
         );
   }
}
