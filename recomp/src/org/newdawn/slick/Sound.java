package org.newdawn.slick;

import java.io.InputStream;
import java.net.URL;
import org.lwjgl.openal.AL10;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.NullAudio;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.Log;

public class Sound {
   private Audio sound;
   private int soundIndex;

   public Sound(InputStream in, String ref) throws SlickException {
      SoundStore.get().init();

      try {
         if (ref.toLowerCase().endsWith(".ogg")) {
            this.sound = SoundStore.get().getOgg(in);
         } else if (ref.toLowerCase().endsWith(".wav")) {
            this.sound = SoundStore.get().getWAV(in);
         } else if (ref.toLowerCase().endsWith(".aif")) {
            this.sound = SoundStore.get().getAIF(in);
         } else {
            if (!ref.toLowerCase().endsWith(".xm") && !ref.toLowerCase().endsWith(".mod")) {
               throw new SlickException("Only .xm, .mod, .aif, .wav and .ogg are currently supported.");
            }

            this.sound = SoundStore.get().getMOD(in);
         }
      } catch (Exception var4) {
         Log.error(var4);
         throw new SlickException("Failed to load sound: " + ref);
      }
   }

   public Sound(URL url) throws SlickException {
      SoundStore.get().init();
      String ref = url.getFile();

      try {
         if (ref.toLowerCase().endsWith(".ogg")) {
            this.sound = SoundStore.get().getOgg(url.openStream());
         } else if (ref.toLowerCase().endsWith(".wav")) {
            this.sound = SoundStore.get().getWAV(url.openStream());
         } else if (ref.toLowerCase().endsWith(".aif")) {
            this.sound = SoundStore.get().getAIF(url.openStream());
         } else {
            if (!ref.toLowerCase().endsWith(".xm") && !ref.toLowerCase().endsWith(".mod")) {
               throw new SlickException("Only .xm, .mod, .aif, .wav and .ogg are currently supported.");
            }

            this.sound = SoundStore.get().getMOD(url.openStream());
         }
      } catch (Exception var4) {
         Log.error(var4);
         throw new SlickException("Failed to load sound: " + ref);
      }
   }

   public Sound(String ref) throws SlickException {
      SoundStore.get().init();

      try {
         if (ref.toLowerCase().endsWith(".ogg")) {
            this.sound = SoundStore.get().getOgg(ref);
         } else if (ref.toLowerCase().endsWith(".wav")) {
            this.sound = SoundStore.get().getWAV(ref);
         } else if (ref.toLowerCase().endsWith(".aif")) {
            this.sound = SoundStore.get().getAIF(ref);
         } else {
            if (!ref.toLowerCase().endsWith(".xm") && !ref.toLowerCase().endsWith(".mod")) {
               throw new SlickException("Only .xm, .mod, .aif, .wav and .ogg are currently supported.");
            }

            this.sound = SoundStore.get().getMOD(ref);
         }
      } catch (Exception var3) {
         Log.error(var3);
         throw new SlickException("Failed to load sound: " + ref);
      }
   }

   public void play() {
      this.play(1.0F, 1.0F);
   }

   public void play(float pitch, float volume) {
      this.soundIndex = this.sound.playAsSoundEffect(pitch, volume * SoundStore.get().getSoundVolume(), false);
   }

   public void playAt(float x, float y, float z) {
      this.playAt(1.0F, 1.0F, x, y, z);
   }

   public void playAt(float pitch, float volume, float x, float y, float z) {
      this.soundIndex = this.sound.playAsSoundEffect(pitch, volume * SoundStore.get().getSoundVolume(), false, x, y, z);
   }

   public void loop() {
      this.loop(1.0F, 1.0F);
   }

   public void loop(float pitch, float volume) {
      this.soundIndex = this.sound.playAsSoundEffect(pitch, volume * SoundStore.get().getSoundVolume(), true);
   }

   public boolean playing() {
      return this.sound.isPlaying();
   }

   public void stop() {
      this.sound.stop();
   }

   public void setGain(float gain) {
      if (!(this.sound instanceof NullAudio)) {
         AL10.alSourcef(this.soundIndex, 4106, gain);
      }
   }

   public void setPitch(float pitch) {
      if (!(this.sound instanceof NullAudio)) {
         AL10.alSourcef(this.soundIndex, 4099, pitch);
      }
   }
}
