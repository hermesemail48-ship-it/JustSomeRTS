package rtr.particles.groups;

import java.util.ArrayList;
import java.util.Arrays;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class ParticleGroupBase {
   protected ParticleModule.ParticleType[] particleTypes;
   protected ParticleModule.ParticleSet[] particleStartColor;
   protected int[] particleStartCustomColor;
   protected ParticleModule.ParticleSet[] particleEndColor;
   protected ParticleModule.ParticleSet[] customColors = new ParticleModule.ParticleSet[3];
   protected int[] particleEndCustomColor;
   protected int[] particleWeight;
   protected ArrayList<Integer> particleWeighter = new ArrayList<>();

   public int getRandomParticleNumber() {
      return this.particleWeighter.get(Utilities.randomInt(this.particleWeighter.size()));
   }

   public ParticleModule.ParticleType getParticleType(int i) {
      return this.particleTypes[i];
   }

   public ParticleModule.ParticleSet getParticleStartColor(int i) {
      return this.particleStartCustomColor[i] != -1 ? this.customColors[this.particleStartCustomColor[i]] : this.particleStartColor[i];
   }

   public ParticleModule.ParticleSet getParticleEndColor(int i) {
      return this.particleEndCustomColor[i] != -1 ? this.customColors[this.particleEndCustomColor[i]] : this.particleEndColor[i];
   }

   public Integer getParticleWeight(int i) {
      return this.particleWeight[i];
   }

   public void setColors(ParticleModule.ParticleSet c1, ParticleModule.ParticleSet c2, ParticleModule.ParticleSet c3) {
      this.customColors[0] = c1;
      this.customColors[1] = c2;
      this.customColors[2] = c3;
   }

   public void load() {
   }

   protected void createArrays(int i) {
      this.particleTypes = new ParticleModule.ParticleType[i];
      this.particleStartColor = new ParticleModule.ParticleSet[i];
      this.particleStartCustomColor = new int[i];
      Arrays.fill(this.particleStartCustomColor, -1);
      this.particleEndColor = new ParticleModule.ParticleSet[i];
      this.particleEndCustomColor = new int[i];
      Arrays.fill(this.particleEndCustomColor, -1);
      this.particleWeight = new int[i];
   }

   protected void addParticle(int slot, ParticleModule.ParticleType type, ParticleModule.ParticleSet startColor, int weight) {
      this.addParticle(slot, type, startColor, startColor, weight);
   }

   protected void addParticle(
      int slot, ParticleModule.ParticleType type, ParticleModule.ParticleSet startColor, ParticleModule.ParticleSet endColor, int weight
   ) {
      this.particleTypes[slot] = type;
      this.particleStartColor[slot] = startColor;
      this.particleEndColor[slot] = endColor;
      this.particleWeight[slot] = weight;
   }

   protected void addParticle(int slot, ParticleModule.ParticleType type, int startColor, int weight) {
      this.addParticle(slot, type, startColor, startColor, weight);
   }

   protected void addParticle(int slot, ParticleModule.ParticleType type, int startColor, ParticleModule.ParticleSet endColor, int weight) {
      this.particleTypes[slot] = type;
      this.particleStartCustomColor[slot] = startColor;
      this.particleEndColor[slot] = endColor;
      this.particleWeight[slot] = weight;
   }

   protected void addParticle(int slot, ParticleModule.ParticleType type, ParticleModule.ParticleSet startColor, int endColor, int weight) {
      this.particleTypes[slot] = type;
      this.particleStartColor[slot] = startColor;
      this.particleEndCustomColor[slot] = endColor;
      this.particleWeight[slot] = weight;
   }

   protected void addParticle(int slot, ParticleModule.ParticleType type, int startColor, int endColor, int weight) {
      this.particleTypes[slot] = type;
      this.particleStartCustomColor[slot] = startColor;
      this.particleEndCustomColor[slot] = endColor;
      this.particleWeight[slot] = weight;
   }

   protected void loadWeighter() {
      this.particleWeighter.clear();

      for (int i = 0; i < this.particleTypes.length; i++) {
         int amount = this.particleWeight[i];

         for (int a = 0; a < amount; a++) {
            this.particleWeighter.add(i);
         }
      }
   }
}
