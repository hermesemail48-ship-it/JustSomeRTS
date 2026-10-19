package rtr;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.states.StateBase;
import rtr.system.ScaleControl;
import rtr.utilities.Utilities;

public class SoundModule extends ModuleBase {
   public static final int SOUND_LEVELS = 25;
   private static ArrayList<Music> musicPlaylist = new ArrayList<>();
   private static ArrayList<String> musicTitles = new ArrayList<>();
   private static ArrayList<Integer> musicDurations = new ArrayList<>();
   private float windVolume = 0.0F;
   private static EnumMap<SoundModule.SoundType, ArrayList<Sound>> soundEffects = new EnumMap<>(SoundModule.SoundType.class);
   private static EnumMap<SoundModule.SoundType, Integer> soundEffectsBuffer = new EnumMap<>(SoundModule.SoundType.class);
   private Music riseToRuins;
   private Music riseToRuinsSpooky;
   private Music musicGoodbyeRetroSky;
   private Music musicCurrent;
   private Music musicLonelySunrise;
   private Music musicSettleAtDawn;
   private Music musicSpecial;
   private String musicCurrentTitle;
   private int musicCurrentDuration;
   private int soundBufferTick;
   private int rainTick;
   private float musicVolume;
   private float soundVolume;
   private boolean musicFade;
   private int musicDelay = 100;
   private int musicDelayTick;
   private static final int SOUND_LOAD_BANKS = 2;
   private static boolean musicLoaded;
   private static boolean[] banksLoaded = new boolean[2];
   private static boolean soundLoaded;
   /** Set by the phone port: load sound effects on first use. */
   public static boolean lazySounds = false;

   /** The loaded sounds of a type, loading them now if needed. */
   private static ArrayList<Sound> sounds(SoundModule.SoundType type) {
      ArrayList<Sound> s = soundEffects.get(type);
      if (s == null) {
         try {
            loadSound(type);
         } catch (SlickException e) {
            throw new RuntimeException("Cannot load sound " + type, e);
         }

         s = soundEffects.get(type);
      }

      return s;
   }

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      if (!musicLoaded) {
         this.riseToRuins = new Music("res/music/riseToRuins.ogg", true);
         this.riseToRuinsSpooky = new Music("res/music/riseToRuinsSpooky.ogg", true);
         this.musicGoodbyeRetroSky = new Music("res/music/goodbyeRetroSky.ogg", true);
         this.musicLonelySunrise = new Music("res/music/lonelySunrise.ogg", true);
         this.musicSettleAtDawn = new Music("res/music/settleAtDawn.ogg", true);
         this.musicSpecial = new Music("res/music/special.ogg", true);
         loadMusic("A Way to Freedom", "aWayToFreedom", this.getSeconds(2, 37));
         loadMusic("Chant of Light", "chantOfLight", this.getSeconds(2, 49));
         loadMusic("Crops of Nowhere", "cropsOfNowhere", this.getSeconds(2, 32));
         loadMusic("Endless Drought", "endlessDrought", this.getSeconds(3, 44));
         loadMusic("Farewell", "farewell", this.getSeconds(2, 57));
         loadMusic("Gloom", "gloom", this.getSeconds(2, 58));
         loadMusic("Hollow Land", "hollowLand", this.getSeconds(2, 14));
         loadMusic("Last Words", "lastWords", this.getSeconds(3, 4));
         loadMusic("Lonely Sunrise", "lonelySunrise", this.getSeconds(2, 58));
         loadMusic("Maxine", "maxine", this.getSeconds(3, 24));
         loadMusic("Old Swamp", "oldSwamp", this.getSeconds(2, 47));
         loadMusic("Ritual of Nature", "ritualOfNature", this.getSeconds(3, 53));
         loadMusic("Sorrow", "sorrow", this.getSeconds(3, 24));
         loadMusic("Spring Village", "springVillage", this.getSeconds(3, 36));
         loadMusic("Sunken Hope", "sunkenHope", this.getSeconds(2, 50));
         loadMusic("Sunset", "sunset", this.getSeconds(2, 36));
         loadMusic("Sun's Spirit", "sunsSpirit", this.getSeconds(4, 4));
         loadMusic("Underworld", "underworld", this.getSeconds(3, 57));
         loadMusic("Wild Desert", "wildDesert", this.getSeconds(2, 26));
         loadMusic("Winter is Nigh", "winterIsNigh", this.getSeconds(3, 12));
         if (lazySounds) {
            // Phones: each sound type is decoded the first time it plays (see sounds()), instead of all ~60 MB of
            // effects being decoded by a background thread at start-up.
            soundLoaded = true;
         } else {
            this.startThreadedSoundLoad();
         }

         musicLoaded = true;
      }

      this.musicCurrent = this.riseToRuins;
      this.resetVolume();
      this.initLoad();
   }

   private void startThreadedSoundLoad() {
      Runnable r = new Runnable() {
         @Override
         public void run() {
            try {
               SoundModule.SoundType[] var4;
               for (SoundModule.SoundType t : SoundModule.SoundType.values()) {
                  SoundModule.loadSound(t);
               }

               SoundModule.soundLoaded = true;
               System.out.println("Sound Finished Loading.");
            } catch (SlickException var5) {
               var5.printStackTrace();
            }
         }
      };
      new Thread(r).start();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
      this.musicFade = false;
      this.musicDelayTick = 0;
      this.musicCurrent = this.riseToRuins;
      if (soundLoaded && soundEffects.size() > 0) {
         for (ArrayList<Sound> loaded : soundEffects.values()) {
            for (Sound sound : loaded) {
               sound.stop();
            }
         }
      }
   }

   @Override
   protected void newMainMenu() throws SlickException {
      this.playMainMenuTheme();
   }

   @Override
   protected void newPlay() throws SlickException {
      this.playSettleAtDawn();
   }

   @Override
   protected void loadPlay() throws SlickException {
   }

   @Override
   protected void newMapEditor() throws SlickException {
   }

   @Override
   protected void loadMapEditor() throws SlickException {
   }

   @Override
   protected void newWorldMap() throws SlickException {
   }

   @Override
   protected void loadWorldMap() throws SlickException {
      this.playLonelySunrise();
   }

   @Override
   protected void loadRegionalSaveData() throws SlickException {
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
   }

   public void playSound(SoundModule.SoundType type) throws SlickException {
      ArrayList<Sound> currentSounds = sounds(type);
      if (currentSounds.size() == 0) {
         return;
      }

      if (soundEffectsBuffer.get(type) < type.getMaxSimultaneous()) {
         if (type.isVariablePitch()) {
            currentSounds.get(Utilities.randomInt(currentSounds.size())).play(this.getPitchVariance(), this.soundVolume);
         } else {
            currentSounds.get(Utilities.randomInt(currentSounds.size())).play(1.0F, this.soundVolume);
         }

         soundEffectsBuffer.put(type, soundEffectsBuffer.get(type) + 1);
      }
   }

   public void playSound(SoundModule.SoundType type, float x, float y) throws SlickException {
      ArrayList<Sound> currentSounds = sounds(type);
      if (currentSounds.size() == 0) {
         return;
      }

      if (soundEffectsBuffer.get(type) < type.getMaxSimultaneous()) {
         float varianceVolume = this.getGameWorldVolumeVariance(x, y);
         if (type.isVariablePitch()) {
            currentSounds.get(Utilities.randomInt(currentSounds.size())).play(this.getPitchVariance(), varianceVolume);
         } else {
            currentSounds.get(Utilities.randomInt(currentSounds.size())).play(1.0F, varianceVolume);
         }

         soundEffectsBuffer.put(type, soundEffectsBuffer.get(type) + 1);
      }
   }

   private static void loadSound(SoundModule.SoundType type) throws SlickException {
      ArrayList<Sound> currentSounds = new ArrayList<>();
      if (type.isSingle()) {
         String path = "res/sounds/" + type.getFileName() + ".ogg";
         File soundCheck = new File(path);
         if (!soundCheck.exists()) {
            path = "res/sounds/" + type.getFileName() + ".wav";
         }

         currentSounds.add(new Sound(path));
      } else {
         String extension = "res/sounds/" + type.getFileName() + "1.ogg";
         File file = new File(extension);
         if (!file.exists()) {
            extension = ".wav";
         } else {
            extension = ".ogg";
         }

         File soundCheck = new File("res/sounds/" + type.getFileName() + "1" + extension);

         for (int count = 1; soundCheck.exists(); soundCheck = new File("res/sounds/" + type.getFileName() + ++count + extension)) {
            currentSounds.add(new Sound(soundCheck.toString()));
         }
      }

      soundEffects.put(type, currentSounds);
      soundEffectsBuffer.put(type, 0);
   }

   public static void loadMusic(String songName, String songFileName, int duration) throws SlickException {
      if (!musicTitles.contains(songName)) {
         musicPlaylist.add(new Music("res/music/" + songFileName + ".ogg", true));
         musicTitles.add(songName);
         musicDurations.add(duration);
      }
   }

   public void playRain() {
      if (soundLoaded) {
         this.rainTick++;
         if (this.rainTick > 400 || this.rainTick > 200 && Utilities.randomInt(200) == 0 || !sounds(SoundModule.SoundType.RAIN).get(0).playing()) {
            sounds(SoundModule.SoundType.RAIN).get(0).play(1.0F, this.soundVolume / 20.0F);
            this.rainTick = 0;
         }
      }
   }

   public void stopRain() {
      if (soundLoaded) {
         sounds(SoundModule.SoundType.RAIN).get(0).stop();
      }
   }

   public void playChestRoll() {
      if (soundLoaded) {
         if (!sounds(SoundModule.SoundType.CHEST_REWARD_BAR_ROLL).get(0).playing()) {
            sounds(SoundModule.SoundType.CHEST_REWARD_BAR_ROLL).get(0).play(1.0F, this.soundVolume);
         }
      }
   }

   public void stopChestRoll() {
      if (soundLoaded) {
         sounds(SoundModule.SoundType.CHEST_REWARD_BAR_ROLL).get(0).stop();
      }
   }

   public void playMainMenuTheme() {
      if (TimeModule.isHalloween()) {
         this.musicCurrent = this.riseToRuinsSpooky;
      } else {
         this.musicCurrent = this.riseToRuins;
      }

      this.musicCurrent.play();
      this.musicCurrent.setVolume(this.musicVolume);
   }

   public void playGoodbyeRetroSky() {
      this.musicCurrent = this.musicGoodbyeRetroSky;
      this.musicCurrent.play();
      this.musicCurrent.setVolume(this.musicVolume);
   }

   public void playLonelySunrise() {
      this.musicCurrent = this.musicLonelySunrise;
      this.musicCurrent.play();
      this.musicCurrent.setVolume(this.musicVolume);
   }

   public void playSettleAtDawn() {
      this.musicCurrent = this.musicSettleAtDawn;
      this.musicCurrent.play();
      this.musicCurrent.setVolume(this.musicVolume);
   }

   public void playLostSoul() {
      this.musicCurrent = this.musicSpecial;
      this.musicCurrent.play();
      this.musicCurrent.setVolume(this.musicVolume);
   }

   public void update() throws SlickException {
      if (!this.musicFade) {
         if (!this.musicCurrent.playing()) {
            this.musicDelayTick++;
         }

         if (this.musicDelayTick > this.musicDelay) {
            this.playRandomMusic();
            this.musicDelayTick = 0;
         }

         this.musicVolume = SettingsParser.getMusicVolume() / 25.0F;
         this.soundVolume = SettingsParser.getSoundVolume() / 25.0F;
         int corruptionPercent = 0;
         if (this.corruption.isInitialized()) {
            corruptionPercent = this.corruption.calculateCorruptionPercentOnScreen();
         }

         if (this.musicVolume > 0.0F && this.corruption.isInitialized()) {
            this.musicVolume = (float)(this.musicVolume - this.musicVolume * (corruptionPercent / 100.0));
            if (this.musicVolume <= 0.05F) {
               this.musicVolume = 0.05F;
            }
         }

         if (soundLoaded && corruptionPercent > 1) {
            Sound ambientCorruption = sounds(SoundModule.SoundType.CORRUPTION_AMBIENT).get(0);
            float volume = SettingsParser.getSoundVolume() / 25.0F;
            volume = (float)(corruptionPercent / 100.0) * volume;
            if (!ambientCorruption.playing()) {
               ambientCorruption.play(1.0F, volume);
            }

            ambientCorruption.setGain(volume);
         } else if (soundLoaded) {
            Sound ambientCorruption = sounds(SoundModule.SoundType.CORRUPTION_AMBIENT).get(0);
            ambientCorruption.stop();
         }

         this.musicCurrent.setVolume(this.musicVolume);
      }

      this.soundBufferTick++;
      if (this.soundBufferTick > 10) {
         for (SoundModule.SoundType t : soundEffectsBuffer.keySet()) {
            if (soundEffectsBuffer.get(t) > 0) {
               soundEffectsBuffer.put(t, soundEffectsBuffer.get(t) - 1);
            }
         }

         this.soundBufferTick = 0;
      }

      this.runEnviromentalAudio();
   }

   private void runEnviromentalAudio() {
      if (soundLoaded) {
         if (ScaleControl.getWorldScale() % 1.0F != 0.0F) {
            if (this.windVolume < 0.1F) {
               this.windVolume += 0.05F;
            } else if (this.windVolume > 0.5F) {
               this.windVolume += 0.01F;
            } else {
               this.windVolume += 0.03F;
            }

            if (this.windVolume > 1.0F) {
               this.windVolume = 1.0F;
            }
         } else if (this.windVolume > 0.0F) {
            if (this.windVolume > 0.3) {
               this.windVolume -= 0.02F;
            } else {
               this.windVolume -= 0.01F;
            }
         } else if (this.windVolume * this.soundVolume <= 0.0F && sounds(SoundModule.SoundType.ZOOM_WIND).get(0).playing()) {
            sounds(SoundModule.SoundType.ZOOM_WIND).get(0).stop();
         }

         sounds(SoundModule.SoundType.ZOOM_WIND).get(0).setGain(this.windVolume * this.soundVolume);
         if (!sounds(SoundModule.SoundType.ZOOM_WIND).get(0).playing()) {
            sounds(SoundModule.SoundType.ZOOM_WIND).get(0).play(1.0F, this.windVolume * this.soundVolume);
         }

         int tileCenterX = (int)((ScaleControl.getWorldWidth() / 2 - this.map.getMapX()) / 16.0F);
         int tileCenterY = (int)((ScaleControl.getWorldHeight() / 2 - this.map.getMapY()) / 16.0F);
         ArrayList<MobBase> mobsAtCenter = this.mob.getMobsNearby(tileCenterX, tileCenterY, 24, MobBase.MobGroup.VILLAGER);
         ArrayList<Sound> villagers = sounds(SoundModule.SoundType.ENVIROMENTAL_VILLAGERS);
         float volume = mobsAtCenter.size() / 40.0F * this.getGameWorldVolumeVariance();
         if (volume > this.getGameWorldVolumeVariance()) {
            volume = this.getGameWorldVolumeVariance();
         }

         if (!villagers.get(0).playing()) {
            villagers.get(0).play(1.0F, volume);
         }

         villagers.get(0).setGain(volume);
         ArrayList<ObjectBase> objectsAtCenter = this.object.getObjectsNearby(tileCenterX, tileCenterY, 24, MobBase.MobGroup.VILLAGER);
         ArrayList<ObjectBase> completedBuildings = new ArrayList<>();
         ArrayList<ObjectBase> constructionBuildings = new ArrayList<>();

         for (ObjectBase o : objectsAtCenter) {
            if (o.getObjectFlags().getBaseType().getTileSetTemplate().getType() != MapTilesLoader.TileSetType.WALL) {
               if (o.getObjectFlags().getSubType() != ObjectBase.ObjectSubType.CONSTRUCTION) {
                  completedBuildings.add(o);
               } else {
                  constructionBuildings.add(o);
               }
            }
         }

         ArrayList<Sound> buildings = sounds(SoundModule.SoundType.ENVIROMENTAL_BUILDINGS);
         volume = completedBuildings.size() / 25.0F * this.getGameWorldVolumeVariance();
         if (volume > this.getGameWorldVolumeVariance()) {
            volume = this.getGameWorldVolumeVariance();
         }

         if (!buildings.get(0).playing()) {
            buildings.get(0).play(1.0F, volume);
         }

         buildings.get(0).setGain(volume);
         ArrayList<Sound> construction = sounds(SoundModule.SoundType.ENVIROMENTAL_CONSTRUCTION);
         volume = constructionBuildings.size() / 5.0F * this.getGameWorldVolumeVariance();
         if (volume > this.getGameWorldVolumeVariance()) {
            volume = this.getGameWorldVolumeVariance();
         }

         if (!construction.get(0).playing()) {
            construction.get(0).play(1.0F, volume);
         }

         construction.get(0).setGain(volume);
      }
   }

   public void playRandomMusic() throws SlickException {
      int trackNumber = Utilities.randomInt(musicPlaylist.size());

      while (musicPlaylist.get(trackNumber) == this.musicCurrent) {
         trackNumber = Utilities.randomInt(musicPlaylist.size());
      }

      if (!this.musicCurrent.playing()) {
         this.musicCurrent = musicPlaylist.get(trackNumber);
         this.musicCurrentTitle = musicTitles.get(trackNumber);
         this.musicCurrentDuration = musicDurations.get(trackNumber);
         this.musicCurrent.play();
         this.musicCurrent.setVolume(this.musicVolume);
      }
   }

   public void playMusic(String musicTitle) {
      for (int x = 0; x < musicTitles.size(); x++) {
         if (musicTitles.get(x).equals(musicTitle)) {
            this.musicCurrent = musicPlaylist.get(x);
            this.musicCurrentTitle = musicTitles.get(x);
            this.musicCurrentDuration = musicDurations.get(x);
            this.musicCurrent.play();
            this.musicCurrent.setVolume(this.musicVolume);
         }
      }
   }

   public void playCredits() {
      this.musicCurrent = musicPlaylist.get(12);
      this.musicCurrentTitle = musicTitles.get(12);
      this.musicCurrentDuration = musicDurations.get(12);
      this.musicCurrent.loop();
      this.musicCurrent.setVolume(this.musicVolume);
   }

   public void fade() {
      this.musicFade = true;
      this.musicCurrent.fade(1000, 0.0F, true);
   }

   public ArrayList<String> getMusicTitles() {
      return musicTitles;
   }

   public Music getCurrentMusic() {
      return this.musicCurrent;
   }

   public String getCurrentMusicTitle() {
      return this.musicCurrentTitle;
   }

   public int getCurrentMusicDuration() {
      return this.musicCurrentDuration;
   }

   private float getPitchVariance() {
      return Utilities.randomFloat() / 4.0F + 0.9F;
   }

   private float getGameWorldVolumeVariance() {
      int worldScale = (int)ScaleControl.getWorldScale();
      return this.soundVolume * (worldScale * 2.0F / 25.0F);
   }

   private float getGameWorldVolumeVariance(float x, float y) {
      int worldScale = (int)ScaleControl.getWorldScale();
      float widthCenter = ScaleControl.getWorldWidth() / 2;
      float heightCenter = ScaleControl.getWorldHeight() / 2;
      float volumeVariance = this.soundVolume
         * (float)(0.5 - Math.abs(widthCenter - (this.map.getMapX() + x)) / 1500.0F + (0.5 - Math.abs(heightCenter - (this.map.getMapY() + y)) / 1500.0F));
      if (volumeVariance < 0.0F) {
         volumeVariance = 0.0F;
      }

      return volumeVariance * (worldScale * 2.0F / 25.0F);
   }

   public void resetVolume() {
      this.musicVolume = SettingsParser.getMusicVolume() / 25.0F;
      this.soundVolume = SettingsParser.getSoundVolume() / 25.0F;
      this.setCurrentMusicVolume(this.musicVolume);
   }

   public void setCurrentMusicVolume(float i) {
      if (this.musicCurrent != null) {
         this.musicCurrent.setVolume(i);
      }
   }

   public void setCurrentMusicVolume(int i) {
      if (this.musicCurrent != null) {
         this.musicCurrent.setVolume(i / 25.0F);
      }
   }

   public void setSoundVolume(float i) {
      this.soundVolume = i;
   }

   public void setSoundVolume(int i) {
      this.soundVolume = i / 25.0F;
   }

   private int getSeconds(int min, int sec) {
      return min * 60 + sec;
   }

   public static enum SoundSet {
      VILLAGER_MALE(
         null,
         null,
         SoundModule.SoundType.HIT_GENERIC,
         SoundModule.SoundType.HIT_SWEETENER_GENERIC,
         SoundModule.SoundType.HIT_SWEETENER_VILLAGER_MALE,
         SoundModule.SoundType.DEATH_VILLAGER_MALE
      ),
      VILLAGER_FEMALE(
         null,
         null,
         SoundModule.SoundType.HIT_GENERIC,
         SoundModule.SoundType.HIT_SWEETENER_GENERIC,
         SoundModule.SoundType.HIT_SWEETENER_VILLAGER_FEMALE,
         SoundModule.SoundType.DEATH_VILLAGER_FEMALE
      ),
      DOGGO(
         SoundModule.SoundType.IDLE_DOGGO,
         SoundModule.SoundType.GET_HIT_DOGGO,
         SoundModule.SoundType.HIT_GENERIC,
         SoundModule.SoundType.HIT_SWEETENER_GENERIC,
         SoundModule.SoundType.HIT_SWEETENER_DOGGO,
         SoundModule.SoundType.DEATH_DOGGO
      ),
      GENERIC(null, null, SoundModule.SoundType.HIT_GENERIC, SoundModule.SoundType.HIT_SWEETENER_GENERIC, null, null),
      SLIME(null, null, SoundModule.SoundType.HIT_GENERIC, SoundModule.SoundType.HIT_SWEETENER_GENERIC, null, null),
      GHOST_DOGGO(null, null, SoundModule.SoundType.HIT_GENERIC, SoundModule.SoundType.HIT_SWEETENER_GENERIC, null, null),
      ZOMBIE_DOGGO(null, null, SoundModule.SoundType.HIT_GENERIC, SoundModule.SoundType.HIT_SWEETENER_GENERIC, null, null),
      SKELETON(null, null, SoundModule.SoundType.HIT_GENERIC, SoundModule.SoundType.HIT_SWEETENER_GENERIC, null, null),
      GHOST(null, null, SoundModule.SoundType.HIT_GENERIC, SoundModule.SoundType.HIT_SWEETENER_GENERIC, null, null),
      ZOMBIE(null, null, SoundModule.SoundType.HIT_GENERIC, SoundModule.SoundType.HIT_SWEETENER_GENERIC, null, null),
      FIRE_ELEMENTAL(null, null, SoundModule.SoundType.HIT_GENERIC, SoundModule.SoundType.HIT_SWEETENER_GENERIC, null, null),
      GOLEM(null, null, SoundModule.SoundType.HIT_GENERIC, SoundModule.SoundType.HIT_SWEETENER_GENERIC, null, null),
      BEEFALO(null, null, SoundModule.SoundType.HIT_GENERIC, SoundModule.SoundType.HIT_SWEETENER_GENERIC, null, null),
      ENTLER(null, null, SoundModule.SoundType.HIT_GENERIC, SoundModule.SoundType.HIT_SWEETENER_GENERIC, null, null),
      ROUS(null, null, SoundModule.SoundType.HIT_GENERIC, SoundModule.SoundType.HIT_SWEETENER_GENERIC, null, null),
      CLUCKER(null, null, SoundModule.SoundType.HIT_GENERIC, SoundModule.SoundType.HIT_SWEETENER_GENERIC, null, null),
      ZOMBIE_BEEFALO(null, null, SoundModule.SoundType.HIT_GENERIC, SoundModule.SoundType.HIT_SWEETENER_GENERIC, null, null),
      ZOMBIE_ENTLER(null, null, SoundModule.SoundType.HIT_GENERIC, SoundModule.SoundType.HIT_SWEETENER_GENERIC, null, null),
      ZOMBIE_ROUS(null, null, SoundModule.SoundType.HIT_GENERIC, SoundModule.SoundType.HIT_SWEETENER_GENERIC, null, null),
      ZOMBIE_CLUCKER(null, null, SoundModule.SoundType.HIT_GENERIC, SoundModule.SoundType.HIT_SWEETENER_GENERIC, null, null);

      private SoundModule.SoundType idle;
      private SoundModule.SoundType getHit;
      private SoundModule.SoundType hit;
      private SoundModule.SoundType hitSweetener;
      private SoundModule.SoundType hitSweetener2;
      private SoundModule.SoundType death;

      private SoundSet(
         SoundModule.SoundType idle,
         SoundModule.SoundType getHit,
         SoundModule.SoundType hit,
         SoundModule.SoundType hitSweetener,
         SoundModule.SoundType hitSweetener2,
         SoundModule.SoundType death
      ) {
         this.idle = idle;
         this.getHit = getHit;
         this.hit = hit;
         this.hitSweetener = hitSweetener;
         this.hitSweetener2 = hitSweetener2;
         this.death = death;
      }

      public SoundModule.SoundType getSoundIdle() {
         return this.idle;
      }

      public SoundModule.SoundType getSoundGetHit() {
         return this.getHit;
      }

      public SoundModule.SoundType getSoundHit() {
         return this.hit;
      }

      public SoundModule.SoundType getSoundHitSweetener() {
         return this.hitSweetener;
      }

      public SoundModule.SoundType getSoundHitSweetener2() {
         return this.hitSweetener2;
      }

      public SoundModule.SoundType getSoundDeath() {
         return this.death;
      }
   }

   public static enum SoundType {
      CHEST_REWARD_BAR_ROLL("chestRewardBarRoll", 1, true, false),
      BELL("bell", 5, true),
      ARROW_HIT("arrowHit", 5, true),
      BOW_FIRE("bowFire", 5, true),
      SLING_FIRE("slingFire", 5, true),
      BALLISTA_FIRE("ballistaFire", 5, true),
      MAGIC_FIRE("magicFire", 5, true),
      MAGIC_HIT("magicHit", 5, true),
      MAGIC_BANISH("magicBanish", 30, true),
      MAGIC_RECALL("magicRecall", 5, true),
      MAGIC_ATTRACT_FIRE("magicAttractFire", 30, true),
      MAGIC_HEAL("magicHeal", 15, true),
      MAGIC_ZAP("magicZap", 15, true),
      MAGIC_HEAL_SPELL("magicHealSpell", 15, true),
      DISSOLVE("dissolve", 15, true),
      FLAME_FIRE("flameFire", 15, true),
      METEOR_FIRE("meteorFire", 15, true),
      METEOR_HIT("meteorHit", 15, true),
      PHANTNOM_DART_FIRE("phantomDartFire", 15, true),
      PHANTNOM_DART_HIT("phantomDartHit", 15, true),
      SPRAY_BULLET_FIRE("sprayBulletFire", 100, true),
      SPRAY_BULLET_HIT("sprayBulletHit", 100, true, true),
      MINING("mining", 5, true),
      STORE_ITEM("storeItem", 5, true),
      WOOD_CHOP("woodChop", 5, true),
      BUILDING("building", 5, true),
      OPEN_GATE("openGate", 5, true),
      CLOSE_GATE("closeGate", 5, true),
      THUNDER("thunder", 15, false),
      PLACE_OBJECT("placeObject", 5, false),
      EARTHQUAKE_ROCKS("earthquakeRocks", 5, true),
      EARTHQUAKE_BOOM("earthquakeBoom", 5, true),
      HEAVY_WIND("heavyWind", 5, true),
      GRAB("grab", 5, true),
      DROP_RESOURCE("dropResource", 5, true),
      FULL_MOON_MAGIC("fullMoonMagic", 5, true),
      MAGIC_CLICK("magicClick", 5, true),
      LOOT_BOX_OPEN("lootBoxOpen", 5, true),
      LOOT_BOX_LOCKED("lootBoxLocked", 5, true),
      HOLY_SOFT("holySoft", 5, true),
      HOLY_LOUD("holyLoud", 5, true),
      CORRUPTION("corruption", 5, true),
      CORRUPTION_AMBIENT("corruptionAmbient", 1, true, false),
      ZOOM_WIND("zoomWind", 1, true, false),
      REGISTER("register", 2, true),
      HIT_WEAPON_GENERIC("hitWeaponGeneric", 5, true),
      HIT_TOOL_GENERIC("hitToolGeneric", 5, true),
      HIT_GENERIC("hitGeneric", 5, true),
      HIT_SWEETENER_GENERIC("hitSweetenerGeneric", 5, true),
      PRAY_CHANTING("prayChanting", 2, true),
      IDLE_DOGGO("idleDoggo", 5, true),
      GET_HIT_DOGGO("getHitDoggo", 5, true),
      HIT_SWEETENER_DOGGO("hitSweetenerDoggo", 5, true),
      DEATH_DOGGO("deathDoggo", 5, false),
      HIT_SWEETENER_VILLAGER_MALE("hitSweetenerVillagerMale", 5, false),
      HIT_SWEETENER_VILLAGER_FEMALE("hitSweetenerVillagerFemale", 5, false),
      DEATH_VILLAGER_MALE("deathVillagerMale", 5, false),
      DEATH_VILLAGER_FEMALE("deathVillagerFemale", 5, false),
      DECAY("decay", 2, true),
      POP("pop", 5, true),
      MEDIUM_WOOSH("mediumWoosh", 5, true),
      LARGE_WOOSH("largeWoosh", 5, true),
      MAGIC_SPARKLES("magicSparkles", 5, true),
      COMPLETE_GOAL("completeGoal", 5, true),
      COMPLETE_GOAL_NEGATIVE("completeGoalNegative", 5, true),
      DISSOLVE_SPELL("dissolveSpell", 5, true, true),
      MOTIVATE_LAND_SPELL("motivateLandSpell", 5, true, true),
      ESSENCE_SUCK("essenceSuck", 5, true, true),
      BUTTON_CLICK("buttonClick", 5, true, true),
      VILLAGER_DEATH("villagerDeath", 5, true, false),
      VILLAGER_DEATH_OLD_AGE("villagerDeathOldAge", 5, true, false),
      VILLAGER_BIRTH("villagerBirth", 5, true, false),
      VILLAGER_LEVEL_UP("villagerLevelUp", 5, true, false),
      RESURRECT("resurrect", 5, true, false),
      RESURRECT_NEGATIVE("resurrectNegative", 5, true, false),
      FULL_MOON("fullMoon", 5, true, false),
      BLOOD_MOON("bloodMoon", 5, true, false),
      DISASTER_START("disasterStart", 5, true, false),
      NOMADS("nomads", 5, true, false),
      TIP("tip", 2, false),
      DAWN_ARRIVED("dawnArrived", 5, true, false),
      NIGHT_ARRIVED("nightArrived", 5, true, false),
      RAIN("rain", 5, true, false),
      EARTHQUAKE("earthquake", 5, true, false),
      RETRO_CLICK("retroClick", 5, true, false),
      RETRO_NOTE_1("retroNote1", 5, true, false),
      RETRO_NOTE_2("retroNote2", 5, true, false),
      RETRO_NOTE_3("retroNote3", 5, true, false),
      RETRO_NOTE_4("retroNote4", 5, true, false),
      RETRO_NOTE_5("retroNote5", 5, true, false),
      RETRO_NOTE_6("retroNote6", 5, true, false),
      RETRO_NOTE_7("retroNote7", 5, true, false),
      RETRO_NOTE_8("retroNote8", 5, true, false),
      RETRO_NOTE_9("retroNote9", 5, true, false),
      MORNING_ROOSTER("morningRooster", 2, true, false),
      ENVIROMENTAL_VILLAGERS("enviromentalVillagers", 5, true, false),
      ENVIROMENTAL_BUILDINGS("enviromentalBuildings", 5, true, false),
      ENVIROMENTAL_CONSTRUCTION("enviromentalConstruction", 5, true, false),
      WILHELM("wilhelm", 5, true, false);

      private final String fileName;
      private final int maxSimultaneous;
      private final boolean single;
      private final boolean variablePitch;

      private SoundType(String fN, int mS, boolean s, boolean vP) {
         this.fileName = fN;
         this.maxSimultaneous = mS;
         this.single = s;
         this.variablePitch = vP;
      }

      private SoundType(String fN, int mS, boolean vP) {
         this.fileName = fN;
         this.maxSimultaneous = mS;
         this.single = false;
         this.variablePitch = vP;
      }

      public String getFileName() {
         return this.fileName;
      }

      public int getMaxSimultaneous() {
         return this.maxSimultaneous;
      }

      public boolean isSingle() {
         return this.single;
      }

      public boolean isVariablePitch() {
         return this.variablePitch;
      }
   }
}
