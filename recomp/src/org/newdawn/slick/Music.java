package org.newdawn.slick;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import org.lwjgl.openal.AL10;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioImpl;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.Log;

public class Music {
   private static Music currentMusic;
   private Audio sound;
   private boolean playing;
   private ArrayList listeners = new ArrayList();
   private float volume = 1.0F;
   private float fadeStartGain;
   private float fadeEndGain;
   private int fadeTime;
   private int fadeDuration;
   private boolean stopAfterFade;
   private boolean positioning;
   private float requiredPosition = -1.0F;
   private int soundIndex;

   public static void poll(int delta) {
      if (currentMusic != null) {
         SoundStore.get().poll(delta);
         if (!SoundStore.get().isMusicPlaying()) {
            if (!currentMusic.positioning) {
               Music oldMusic = currentMusic;
               currentMusic = null;
               oldMusic.fireMusicEnded();
            }
         } else {
            currentMusic.update(delta);
         }
      }
   }

   public Music(String ref) throws SlickException {
      this(ref, false);
   }

   public Music(URL ref) throws SlickException {
      this(ref, false);
   }

   public Music(InputStream in, String ref) throws SlickException {
      SoundStore.get().init();

      try {
         if (ref.toLowerCase().endsWith(".ogg")) {
            this.sound = SoundStore.get().getOgg(in);
         } else if (ref.toLowerCase().endsWith(".wav")) {
            this.sound = SoundStore.get().getWAV(in);
         } else if (!ref.toLowerCase().endsWith(".xm") && !ref.toLowerCase().endsWith(".mod")) {
            if (!ref.toLowerCase().endsWith(".aif") && !ref.toLowerCase().endsWith(".aiff")) {
               throw new SlickException("Only .xm, .mod, .ogg, and .aif/f are currently supported.");
            }

            this.sound = SoundStore.get().getAIF(in);
         } else {
            this.sound = SoundStore.get().getMOD(in);
         }
      } catch (Exception var4) {
         Log.error(var4);
         throw new SlickException("Failed to load music: " + ref);
      }
   }

   public Music(URL url, boolean streamingHint) throws SlickException {
      SoundStore.get().init();
      String ref = url.getFile();

      try {
         if (ref.toLowerCase().endsWith(".ogg")) {
            if (streamingHint) {
               this.sound = SoundStore.get().getOggStream(url);
            } else {
               this.sound = SoundStore.get().getOgg(url.openStream());
            }
         } else if (ref.toLowerCase().endsWith(".wav")) {
            this.sound = SoundStore.get().getWAV(url.openStream());
         } else if (!ref.toLowerCase().endsWith(".xm") && !ref.toLowerCase().endsWith(".mod")) {
            if (!ref.toLowerCase().endsWith(".aif") && !ref.toLowerCase().endsWith(".aiff")) {
               throw new SlickException("Only .xm, .mod, .ogg, and .aif/f are currently supported.");
            }

            this.sound = SoundStore.get().getAIF(url.openStream());
         } else {
            this.sound = SoundStore.get().getMOD(url.openStream());
         }
      } catch (Exception var5) {
         Log.error(var5);
         throw new SlickException("Failed to load sound: " + url);
      }
   }

   public Music(String ref, boolean streamingHint) throws SlickException {
      SoundStore.get().init();

      try {
         if (ref.toLowerCase().endsWith(".ogg")) {
            if (streamingHint) {
               this.sound = SoundStore.get().getOggStream(ref);
            } else {
               this.sound = SoundStore.get().getOgg(ref);
            }
         } else if (ref.toLowerCase().endsWith(".wav")) {
            this.sound = SoundStore.get().getWAV(ref);
         } else if (!ref.toLowerCase().endsWith(".xm") && !ref.toLowerCase().endsWith(".mod")) {
            if (!ref.toLowerCase().endsWith(".aif") && !ref.toLowerCase().endsWith(".aiff")) {
               throw new SlickException("Only .xm, .mod, .ogg, and .aif/f are currently supported.");
            }

            this.sound = SoundStore.get().getAIF(ref);
         } else {
            this.sound = SoundStore.get().getMOD(ref);
         }
      } catch (Exception var4) {
         Log.error(var4);
         throw new SlickException("Failed to load sound: " + ref);
      }
   }

   public void addListener(MusicListener listener) {
      this.listeners.add(listener);
   }

   public void removeListener(MusicListener listener) {
      this.listeners.remove(listener);
   }

   private void fireMusicEnded() {
      this.playing = false;

      for (int i = 0; i < this.listeners.size(); i++) {
         ((MusicListener)this.listeners.get(i)).musicEnded(this);
      }
   }

   private void fireMusicSwapped(Music newMusic) {
      this.playing = false;

      for (int i = 0; i < this.listeners.size(); i++) {
         ((MusicListener)this.listeners.get(i)).musicSwapped(this, newMusic);
      }
   }

   public void loop() {
      this.loop(1.0F, 1.0F);
   }

   public void play() {
      this.play(1.0F, 1.0F);
   }

   public void play(float pitch, float volume) {
      this.startMusic(pitch, volume, false);
   }

   public void loop(float pitch, float volume) {
      this.startMusic(pitch, volume, true);
   }

   private void startMusic(float pitch, float volume, boolean loop) {
      if (currentMusic != null) {
         currentMusic.stop();
         currentMusic.fireMusicSwapped(this);
      }

      currentMusic = this;
      if (volume < 0.0F) {
         volume = 0.0F;
      }

      if (volume > 1.0F) {
         volume = 1.0F;
      }

      this.soundIndex = this.sound.playAsMusic(pitch, volume, loop);
      this.playing = true;
      this.setVolume(volume);
      if (this.requiredPosition != -1.0F) {
         this.setPosition(this.requiredPosition);
      }
   }

   public void pause() {
      this.playing = false;
      AudioImpl.pauseMusic();
   }

   public void stop() {
      this.sound.stop();
   }

   public void resume() {
      this.playing = true;
      AudioImpl.restartMusic();
   }

   public boolean playing() {
      return currentMusic == this && this.playing;
   }

   public void setVolume(float volume) {
      if (volume > 1.0F) {
         volume = 1.0F;
      } else if (volume < 0.0F) {
         volume = 0.0F;
      }

      this.volume = volume;
      if (currentMusic == this) {
         SoundStore.get().setCurrentMusicVolume(volume);
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
      if (this.playing) {
         if (this.fadeTime > 0) {
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
   }

   public boolean setPosition(float position) {
      if (this.playing) {
         this.requiredPosition = -1.0F;
         this.positioning = true;
         this.playing = false;
         boolean result = this.sound.setPosition(position);
         this.playing = true;
         this.positioning = false;
         return result;
      } else {
         this.requiredPosition = position;
         return false;
      }
   }

   public float getPosition() {
      return this.sound.getPosition();
   }

   public void setGain(float gain) {
      AL10.alSourcef(this.soundIndex, 4106, gain);
   }

   public void setPitch(float pitch) {
      AL10.alSourcef(this.soundIndex, 4099, pitch);
   }
}
