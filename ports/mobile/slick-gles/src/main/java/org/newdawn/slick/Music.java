package org.newdawn.slick;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.io.File;
import java.util.ArrayList;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.Log;
import rtr.compat.RtrPaths;

/**
 * Slick Music on libGDX (streamed). As in Slick, only one Music is "current" at a time. The libGDX music is opened
 * on first use: the game creates every track at start-up, and in the browser each one would be downloaded and decoded.
 */
public class Music {
   private static Music currentMusic;
   private final String ref;
   private final FileHandle handle;
   private com.badlogic.gdx.audio.Music music;
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
      this.ref = ref;
      String path = RtrPaths.resolveAudio(ref, ".mp3");
      this.handle = new File(path).isAbsolute() ? rtr.compat.GdxFiles.absolute(path) : Gdx.files.internal(path);
      if (!this.handle.exists()) {
         throw new SlickException("Failed to load music: " + ref);
      }
   }

   /** The libGDX music, opened on first use. */
   private com.badlogic.gdx.audio.Music gdx() {
      if (this.music == null) {
         try {
            synchronized (SoundStore.LOAD_LOCK) {
               this.music = Gdx.audio.newMusic(this.handle);
            }
         } catch (RuntimeException var3) {
            Log.error("Failed to load music: " + this.ref, var3);
            throw var3;
         }

         final com.badlogic.gdx.audio.Music opened = this.music;
         this.music.setOnCompletionListener(new com.badlogic.gdx.audio.Music.OnCompletionListener() {
            @Override
            public void onCompletion(com.badlogic.gdx.audio.Music m) {
               if (!opened.isLooping()) {
                  Music.this.endedPending = true;
               }
            }
         });
      }

      return this.music;
   }

   /** Browser only: frees a track that is no longer current (it is held fully decoded in memory there). */
   private void releaseIfWeb() {
      if (this.music != null && Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.WebGL) {
         this.music.dispose();
         this.music = null;
      }
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
         Music old = currentMusic;
         if (old.music != null) {
            old.music.stop();
         }

         old.playing = false;
         old.releaseIfWeb();
         old.fireMusicSwapped(this);
      }

      currentMusic = this;
      this.endedPending = false;
      this.volume = clamp(vol);
      this.fadeDuration = 0;
      this.gdx().setLooping(loop);
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
      this.gdx().setVolume(this.volume * SoundStore.get().getMusicVolume());
   }

   public void pause() {
      this.playing = false;
      if (this.music != null) {
         this.music.pause();
      }
   }

   public void stop() {
      this.playing = false;
      if (this.music != null) {
         this.music.stop();
      }

      if (currentMusic == this) {
         currentMusic = null;
      }
   }

   public void resume() {
      this.playing = true;
      this.gdx().play();
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
      this.gdx().setPosition(position);
      return true;
   }

   public float getPosition() {
      return this.music == null ? 0.0F : this.music.getPosition();
   }

   public void setGain(float gain) {
      this.gdx().setVolume(gain);
   }

   public void setPitch(float pitch) {
   }
}
