package org.newdawn.slick.openal;

/** Global audio switches and volumes, as in Slick's SoundStore (the OpenAL parts are gone; libGDX plays audio). */
public class SoundStore {
   private static final SoundStore store = new SoundStore();
   /** The game loads audio from worker threads; libGDX's decoders are not thread-safe, so loads are serialised. */
   public static final Object LOAD_LOCK = new Object();
   private boolean sounds = true;
   private boolean music = true;
   private float soundVolume = 1.0F;
   private float musicVolume = 1.0F;

   public static SoundStore get() {
      return store;
   }

   public void init() {
   }

   public boolean soundsOn() {
      return this.sounds;
   }

   public boolean musicOn() {
      return this.music;
   }

   public void setSoundsOn(boolean on) {
      this.sounds = on;
   }

   public void setMusicOn(boolean on) {
      this.music = on;
   }

   public float getSoundVolume() {
      return this.soundVolume;
   }

   public float getMusicVolume() {
      return this.musicVolume;
   }

   public void setSoundVolume(float volume) {
      this.soundVolume = Math.max(0.0F, volume);
   }

   public void setMusicVolume(float volume) {
      this.musicVolume = Math.max(0.0F, volume);
      org.newdawn.slick.Music.refreshVolume();
   }
}
