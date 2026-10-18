package rtr.utilities;

import java.util.Arrays;

/**
 * Minimal int-keyed hash map (open addressing, linear probing). Replaces HashMap&lt;Integer, V&gt; on hot paths: the
 * game's tile IDs (e.g. 206205500) are outside Integer's small-value cache, so every HashMap lookup boxed a new
 * Integer — gigabytes of garbage per minute in the lighting/shadow code.
 *
 * Same visible behaviour as the HashMap uses it replaces: get returns null when absent, put returns the old value.
 * Like HashMap, concurrent reads are safe once writes have finished.
 */
public final class IntKeyMap<V> {
   private static final int EMPTY = Integer.MIN_VALUE;
   private int[] keys;
   private Object[] values;
   private int size;
   private int mask;

   public IntKeyMap() {
      this(64);
   }

   public IntKeyMap(int capacity) {
      int cap = Integer.highestOneBit(Math.max(4, capacity - 1)) << 1;
      this.keys = new int[cap];
      Arrays.fill(this.keys, EMPTY);
      this.values = new Object[cap];
      this.mask = cap - 1;
   }

   private static int mix(int k) {
      int h = k * -1640531527;
      return h ^ h >>> 16;
   }

   @SuppressWarnings("unchecked")
   public V get(int key) {
      if (key == EMPTY) {
         throw new IllegalArgumentException("Unsupported key " + key);
      }

      int[] k = this.keys;
      int m = k.length - 1;
      int i = mix(key) & m;

      while (true) {
         int found = k[i];
         if (found == key) {
            return (V)this.values[i];
         }

         if (found == EMPTY) {
            return null;
         }

         i = i + 1 & m;
      }
   }

   public boolean containsKey(int key) {
      return this.get(key) != null;
   }

   @SuppressWarnings("unchecked")
   public V put(int key, V value) {
      if (key == EMPTY) {
         throw new IllegalArgumentException("Unsupported key " + key);
      }

      if ((this.size + 1) * 4 > this.keys.length * 3) {
         this.grow();
      }

      int i = mix(key) & this.mask;

      while (true) {
         int found = this.keys[i];
         if (found == key) {
            V old = (V)this.values[i];
            this.values[i] = value;
            return old;
         }

         if (found == EMPTY) {
            this.keys[i] = key;
            this.values[i] = value;
            this.size++;
            return null;
         }

         i = i + 1 & this.mask;
      }
   }

   public int size() {
      return this.size;
   }

   private void grow() {
      int[] oldKeys = this.keys;
      Object[] oldValues = this.values;
      int cap = oldKeys.length << 1;
      int[] newKeys = new int[cap];
      Arrays.fill(newKeys, EMPTY);
      Object[] newValues = new Object[cap];
      int m = cap - 1;

      for (int j = 0; j < oldKeys.length; j++) {
         if (oldKeys[j] != EMPTY) {
            int i = mix(oldKeys[j]) & m;

            while (newKeys[i] != EMPTY) {
               i = i + 1 & m;
            }

            newKeys[i] = oldKeys[j];
            newValues[i] = oldValues[j];
         }
      }

      // Publish values before keys so a concurrent reader never sees a key without its value.
      this.values = newValues;
      this.keys = newKeys;
      this.mask = m;
   }
}
