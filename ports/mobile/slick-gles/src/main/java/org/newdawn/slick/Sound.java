package org.newdawn.slick;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.io.File;
import org.newdawn.slick.openal.AudioLength;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.Log;
import rtr.compat.RtrPaths;

/** Slick Sound on libGDX. libGDX cannot query a sound instance, so playing() is tracked from the clip length. */
public class Sound {
   private com.badlogic.gdx.audio.Sound sound;
   private long lastId = -1L;
   private float seconds;
   private long endTimeMs;
   private boolean looping;

   public Sound(String ref) throws SlickException {
      String path = RtrPaths.resolveAudio(ref, ".wav");
      try {
         FileHandle handle = new File(path).isAbsolute() ? rtr.compat.GdxFiles.absolute(path) : Gdx.files.internal(path);
         synchronized (SoundStore.LOAD_LOCK) {
            this.sound = Gdx.audio.newSound(handle);
         }
         this.seconds = AudioLength.seconds(handle.file().getPath());
      } catch (Exception var4) {
         Log.error(var4);
         throw new SlickException("Failed to load sound: " + ref);
      }
   }

   public void play() {
      this.play(1.0F, 1.0F);
   }

   public void play(float pitch, float volume) {
      if (SoundStore.get().soundsOn()) {
         this.lastId = this.sound.play(volume * SoundStore.get().getSoundVolume(), pitch, 0.0F);
         this.looping = false;
         this.endTimeMs = System.currentTimeMillis() + (this.seconds > 0.0F ? (long)(this.seconds * 1000.0F / Math.max(pitch, 0.01F)) : 0L);
      }
   }

   public void playAt(float x, float y, float z) {
      this.play();
   }

   public void playAt(float pitch, float volume, float x, float y, float z) {
      this.play(pitch, volume);
   }

   public void loop() {
      this.loop(1.0F, 1.0F);
   }

   public void loop(float pitch, float volume) {
      if (SoundStore.get().soundsOn()) {
         this.lastId = this.sound.loop(volume * SoundStore.get().getSoundVolume(), pitch, 0.0F);
         this.looping = true;
      }
   }

   public boolean playing() {
      return this.lastId != -1L && (this.looping || System.currentTimeMillis() < this.endTimeMs);
   }

   public void stop() {
      this.sound.stop();
      this.lastId = -1L;
      this.looping = false;
   }

   /** Sets the gain of the most recently started instance (Slick set the OpenAL source gain directly). */
   public void setGain(float gain) {
      if (this.lastId != -1L) {
         this.sound.setVolume(this.lastId, gain);
      }
   }

   public void setPitch(float pitch) {
      if (this.lastId != -1L) {
         this.sound.setPitch(this.lastId, pitch);
      }
   }
}
