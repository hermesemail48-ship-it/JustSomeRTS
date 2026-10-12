package org.newdawn.slick;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.io.File;
import java.util.ArrayList;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.Log;
import rtr.compat.RtrPaths;

/** Slick Music on libGDX (streamed). As in Slick, only one Music is "current" at a time. */
public class Music {
   private static Music currentMusic;
   private final com.badlogic.gdx.audio.Music music;
   private final ArrayList<MusicListener> listeners = new ArrayList<MusicListener>();
   private boolean playing;
   private float volume = 1.0F;
   private float fadeStartGain;
   private float fadeEndGain;
   private int fadeTime;
   private int fadeDuration;
   private boolean stopAfterFade;
   private boolean endedPending;

   public static void poll(int delta) {
      Music m = currentMusic;
      if (m != null) {
         if (m.endedPending) {
            m.endedPending = false;
            m.playing = false;
            currentMusic = null;
            m.fireMusicEnded();
         } else {
            m.update(delta);
         }
      }
   }

   public static void refreshVolume() {
      if (currentMusic != null) {
         currentMusic.applyVolume();
      }
   }

   public Music(String ref) throws SlickException {
      this(ref, false);
   }

   public Music(String ref, boolean streamingHint) throws SlickException {
      String path = RtrPaths.resolveAudio(ref, ".mp3");
      try {
         FileHandle handle = new File(path).isAbsolute() ? Gdx.files.absolute(path) : Gdx.files.internal(path);
         synchronized (SoundStore.LOAD_LOCK) {
            this.music = Gdx.audio.newMusic(handle);
         }
      } catch (Exception var5) {
         Log.error(var5);
         throw new SlickException("Failed to load music: " + ref);
      }

      this.music.setOnCompletionListener(new com.badlogic.gdx.audio.Music.OnCompletionListener() {
         @Override
         public void onCompletion(com.badlogic.gdx.audio.Music m) {
            if (!Music.this.music.isLooping()) {
               Music.this.endedPending = true;
            }
         }
      });
   }

   public void addListener(MusicListener listener) {
      this.listeners.add(listener);
   }

   public void removeListener(MusicListener listener) {
      this.listeners.remove(listener);
   }

   private void fireMusicEnded() {
      for (int i = 0; i < this.listeners.size(); i++) {
         this.listeners.get(i).musicEnded(this);
      }
   }

   private void fireMusicSwapped(Music newMusic) {
      for (int i = 0; i < this.listeners.size(); i++) {
         this.listeners.get(i).musicSwapped(this, newMusic);
      }
   }

   public void loop() {
      this.loop(1.0F, 1.0F);
   }

   public void play() {
      this.play(1.0F, 1.0F);
   }

   public void play(float pitch, float volume) {
      this.startMusic(volume, false);
   }

   public void loop(float pitch, float volume) {
      this.startMusic(volume, true);
   }

   private void startMusic(float vol, boolean loop) {
      if (currentMusic != null && currentMusic != this) {
         currentMusic.music.stop();
         currentMusic.playing = false;
         currentMusic.fireMusicSwapped(this);
      }

      currentMusic = this;
      this.endedPending = false;
      this.volume = clamp(vol);
      this.fadeDuration = 0;
      this.music.setLooping(loop);
      this.applyVolume();
      this.music.stop();
      if (SoundStore.get().musicOn()) {
         this.music.play();
      }

      this.playing = true;
   }

   private static float clamp(float v) {
      return v < 0.0F ? 0.0F : (v > 1.0F ? 1.0F : v);
   }

   private void applyVolume() {
      this.music.setVolume(this.volume * SoundStore.get().getMusicVolume());
   }

   public void pause() {
      this.playing = false;
      this.music.pause();
   }

   public void stop() {
      this.playing = false;
      this.music.stop();
      if (currentMusic == this) {
         currentMusic = null;
      }
   }

   public void resume() {
      this.playing = true;
      this.music.play();
   }

   public boolean playing() {
      return currentMusic == this && this.playing;
   }

   public void setVolume(float volume) {
      this.volume = clamp(volume);
      if (currentMusic == this) {
         this.applyVolume();
      }
   }

   public float getVolume() {
      return this.volume;
   }

   public void fade(int duration, float endVolume, boolean stopAfterFade) {
      this.stopAfterFade = stopAfterFade;
      this.fadeStartGain = this.volume;
      this.fadeEndGain = endVolume;
      this.fadeDuration = duration;
      this.fadeTime = duration;
   }

   void update(int delta) {
      if (this.playing && this.fadeTime > 0) {
         this.fadeTime -= delta;
         if (this.fadeTime < 0) {
            this.fadeTime = 0;
            if (this.stopAfterFade) {
               this.stop();
               return;
            }
         }

         float offset = (this.fadeEndGain - this.fadeStartGain) * (1.0F - (float)this.fadeTime / this.fadeDuration);
         this.setVolume(this.fadeStartGain + offset);
      }
   }

   public boolean setPosition(float position) {
      this.music.setPosition(position);
      return true;
   }

   public float getPosition() {
      return this.music.getPosition();
   }

   public void setGain(float gain) {
      this.music.setVolume(gain);
   }

   public void setPitch(float pitch) {
   }
}
