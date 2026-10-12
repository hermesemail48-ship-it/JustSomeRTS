package rtr.save;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.newdawn.slick.Color;
import rtr.SettingsParser;
import rtr.console.Console;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.resources.FarmlandBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.OrderedPair;

public class YMLDataMap extends HashMap<String, Object> {
   private static final long serialVersionUID = 1L;
   private String dataMapName;

   public YMLDataMap getSubMap(Object key) {
      HashMap<String, Object> rawDataMap = (HashMap<String, Object>)this.get(key);
      if (rawDataMap == null) {
         if (SettingsParser.getVerboseConsole()) {
            System.out.println((this.dataMapName == null ? "" : this.dataMapName + ": ") + key + " map was not found in save data, returning null!");
         }

         return null;
      } else {
         YMLDataMap dataOut = new YMLDataMap();

         for (Object k : rawDataMap.keySet()) {
            dataOut.put((String)k, rawDataMap.get(k));
         }

         dataOut.setName(this.dataMapName + " " + key);
         return dataOut;
      }
   }

   public String getOrDefault(Object key, String defaultValue) {
      if (this.get(key) == null) {
         if (SettingsParser.getVerboseConsole()) {
            System.out
               .println(
                  (this.dataMapName == null ? "" : this.dataMapName + ": ") + key + " was not found in save data, returning default value of " + defaultValue
               );
         }

         return defaultValue;
      } else {
         return this.get(key).toString();
      }
   }

   public String getOrDefault(Object key, int defaultValue) {
      if (this.get(key) == null) {
         if (SettingsParser.getVerboseConsole()) {
            System.out
               .println(
                  (this.dataMapName == null ? "" : this.dataMapName + ": ") + key + " was not found in save data, returning default value of " + defaultValue
               );
         }

         return Integer.toString(defaultValue);
      } else {
         return this.get(key).toString();
      }
   }

   public String getOrDefault(Object key, float defaultValue) {
      if (this.get(key) == null) {
         if (SettingsParser.getVerboseConsole()) {
            System.out
               .println(
                  (this.dataMapName == null ? "" : this.dataMapName + ": ") + key + " was not found in save data, returning default value of " + defaultValue
               );
         }

         return Float.toString(defaultValue);
      } else {
         return this.get(key).toString();
      }
   }

   public String getOrDefault(Object key, double defaultValue) {
      if (this.get(key) == null) {
         if (SettingsParser.getVerboseConsole()) {
            System.out
               .println(
                  (this.dataMapName == null ? "" : this.dataMapName + ": ") + key + " was not found in save data, returning default value of " + defaultValue
               );
         }

         return Double.toString(defaultValue);
      } else {
         return this.get(key).toString();
      }
   }

   public String getOrDefault(Object key, boolean defaultValue) {
      if (this.get(key) == null) {
         if (SettingsParser.getVerboseConsole()) {
            System.out
               .println(
                  (this.dataMapName == null ? "" : this.dataMapName + ": ") + key + " was not found in save data, returning default value of " + defaultValue
               );
         }

         return Boolean.toString(defaultValue);
      } else {
         return this.get(key).toString();
      }
   }

   public void setName(String s) {
      this.dataMapName = s;
   }

   public String packCoordinatesListWithValue(ArrayList<OrderedPair> coordinatesList, ArrayList<Integer> valueList) {
      if (coordinatesList == null) {
         return null;
      } else {
         StringBuilder stringOut = new StringBuilder("");

         for (int e = 0; e < coordinatesList.size(); e++) {
            stringOut.append("[" + coordinatesList.get(e).getX() + "," + coordinatesList.get(e).getY() + "," + valueList.get(e) + "] ");
         }

         stringOut.deleteCharAt(stringOut.length() - 1);
         return stringOut.toString();
      }
   }

   public int[][] unpackCoordinatesListWithValueToArray(String coordinatesListString, int[][] array) {
      return this.unpackCoordinatesListWithValueToArray(coordinatesListString, array, false);
   }

   public int[][] unpackCoordinatesListWithValueToArray(String coordinatesListString, int[][] array, boolean create) {
      String key = this.getOrDefault(coordinatesListString, "null");
      if (key.equals("null")) {
         return array;
      } else {
         String[] coordinatesStrings = key.split(" ");

         for (String s : coordinatesStrings) {
            s = s.replace("[", "");
            s = s.replace("]", "");
            String[] coordinates = s.split(",");
            array[Integer.parseInt(coordinates[0])][Integer.parseInt(coordinates[1])] = Integer.parseInt(coordinates[2]);
         }

         return array;
      }
   }

   public byte[][] unpackCoordinatesListWithValueToArray(String coordinatesListString, byte[][] array) {
      return this.unpackCoordinatesListWithValueToArray(coordinatesListString, array, false);
   }

   public byte[][] unpackCoordinatesListWithValueToArray(String coordinatesListString, byte[][] array, boolean create) {
      String key = this.getOrDefault(coordinatesListString, "null");
      if (key.equals("null")) {
         return array;
      } else {
         String[] coordinatesStrings = key.split(" ");

         for (String s : coordinatesStrings) {
            s = s.replace("[", "");
            s = s.replace("]", "");
            String[] coordinates = s.split(",");
            array[Integer.parseInt(coordinates[0])][Integer.parseInt(coordinates[1])] = Byte.parseByte(coordinates[2]);
         }

         return array;
      }
   }

   public String packCoordinatesList(ArrayList<OrderedPair> coordinatesList) {
      if (coordinatesList == null) {
         return null;
      } else {
         StringBuilder stringOut = new StringBuilder("");

         for (int e = 0; e < coordinatesList.size(); e++) {
            stringOut.append("[" + coordinatesList.get(e).getX() + "," + coordinatesList.get(e).getY() + "] ");
         }

         stringOut.deleteCharAt(stringOut.length() - 1);
         return stringOut.toString();
      }
   }

   public ArrayList<OrderedPair> unpackCoordinatesList(String coordinatesListString) {
      return this.unpackCoordinatesList(coordinatesListString, false);
   }

   public ArrayList<OrderedPair> unpackCoordinatesList(String coordinatesListString, boolean create) {
      if (!this.containsMap(coordinatesListString)) {
         String key = this.getOrDefault(coordinatesListString, "null");
         if (key.equals("null")) {
            return new ArrayList<>();
         } else {
            ArrayList<OrderedPair> coordinatesList = new ArrayList<>();
            String[] coordinatesStrings = key.split(" ");

            for (String s : coordinatesStrings) {
               s = s.replace("[", "");
               s = s.replace("]", "");
               String[] coordinates = s.split(",");
               OrderedPair o = null;
               if (create) {
                  o = new OrderedPair(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
               } else {
                  o = OrderedPair.getOrderedPair(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
               }

               coordinatesList.add(o);
            }

            return coordinatesList;
         }
      } else {
         YMLDataMap map = this.getSubMap(coordinatesListString);
         Console.saveConverterOut(this, "InDev 33f", "InDev 34 Unstable 1", "Converting coordinates data: " + coordinatesListString);
         ArrayList<OrderedPair> coordinatesList = new ArrayList<>();

         for (int count = 0; map.containsKey("coordinate" + count); count++) {
            OrderedPair o = map.unpackCoordinates("coordinate" + count, create);
            coordinatesList.add(o);
         }

         return coordinatesList;
      }
   }

   public String packCoordinates(OrderedPair coordinates) {
      if (coordinates == null) {
         return null;
      } else {
         StringBuilder stringOut = new StringBuilder("");
         stringOut.append(coordinates.getX() + "," + coordinates.getY());
         return stringOut.toString();
      }
   }

   public OrderedPair unpackCoordinates(String coordinatesString) {
      return this.unpackCoordinates(coordinatesString, false);
   }

   public OrderedPair unpackCoordinates(String coordinatesString, boolean create) {
      if (this.containsMap(coordinatesString)) {
         YMLDataMap map = this.getSubMap(coordinatesString);
         int x = Integer.parseInt(map.getOrDefault("x", -1));
         int y = Integer.parseInt(map.getOrDefault("y", -1));
         Console.saveConverterOut(this, "InDev 33f", "InDev 34 Unstable 1", "Converting InDev 33f coordinates data: " + x + "," + y);
         if (x != -1 && y != -1) {
            if (create) {
               return new OrderedPair(x, y);
            }

            return OrderedPair.getOrderedPair(x, y);
         }
      } else {
         String key = this.getOrDefault(coordinatesString, "null");
         if (key.equals("null")) {
            return null;
         }

         String[] coordinates = key.split(",");
         int x = Integer.parseInt(coordinates[0]);
         int y = Integer.parseInt(coordinates[1]);
         if (x != -1 && y != -1) {
            if (create) {
               return new OrderedPair(x, y);
            }

            return OrderedPair.getOrderedPair(x, y);
         }
      }

      return null;
   }

   public String packColor(Color color) {
      return color == null ? null : color.getRedByte() + "," + color.getGreenByte() + "," + color.getBlueByte() + "," + color.getAlphaByte();
   }

   public Color unpackColor(String color) {
      if (this.containsMap(color)) {
         YMLDataMap map = this.getSubMap(color);
         int r = Integer.parseInt(map.getOrDefault("red", -1));
         int g = Integer.parseInt(map.getOrDefault("green", -1));
         int b = Integer.parseInt(map.getOrDefault("blue", -1));
         int a = Integer.parseInt(map.getOrDefault("alpha", -1));
         if (r != -1 && g != -1 && b != -1 && a != -1) {
            return new Color(r, g, b, a);
         }
      } else if (this.containsKey(color)) {
         String key = this.getOrDefault(color, "null");
         if (key.equals("null")) {
            return null;
         }

         String[] colorSplit = key.split(",");
         int r = Integer.parseInt(colorSplit[0]);
         int g = Integer.parseInt(colorSplit[1]);
         int b = Integer.parseInt(colorSplit[2]);
         int a = Integer.parseInt(colorSplit[3]);
         return new Color(r, g, b, a);
      }

      return null;
   }

   public Map<String, Object> packDamageTypeMap(EnumMap<MobBase.DamageType, Integer> damageMap) {
      Map<String, Object> mapOut = new HashMap<>();
      if (damageMap == null) {
         return mapOut;
      } else {
         int count = 0;

         for (MobBase.DamageType t : damageMap.keySet()) {
            Map<String, Object> thisTypeOut = new HashMap<>();
            thisTypeOut.put("damageType", t.toString());
            thisTypeOut.put("damageAmount", damageMap.get(t));
            mapOut.put(Integer.toString(count), thisTypeOut);
            count++;
         }

         return mapOut;
      }
   }

   public EnumMap<MobBase.DamageType, Integer> unpackDamageTypeMap(String damageMapString) {
      YMLDataMap map = this.getSubMap(damageMapString);
      EnumMap<MobBase.DamageType, Integer> hashMapOut = new EnumMap<>(MobBase.DamageType.class);

      for (int count = 0; map.containsKey(Integer.toString(count)); count++) {
         YMLDataMap thisMap = map.getSubMap(Integer.toString(count));
         String damageTypeString = thisMap.getOrDefault("damageType", "null");

         MobBase.DamageType[] var10;
         for (MobBase.DamageType type : MobBase.DamageType.values()) {
            if (damageTypeString.equals(type.toString())) {
               hashMapOut.put(type, Integer.parseInt(thisMap.getOrDefault("damageAmount", 0)));
               break;
            }
         }
      }

      return hashMapOut;
   }

   public YMLDataMap packResourceColorSetArray(ArrayList<ResourceModule.ResourceColorSet> array) {
      YMLDataMap mapOut = new YMLDataMap();
      if (array == null) {
         return mapOut;
      } else {
         HashMap<ResourceModule.ResourceColorSet, Integer> hashMap = new HashMap<>();

         for (ResourceModule.ResourceColorSet c : array) {
            if (!hashMap.containsKey(c)) {
               hashMap.put(c, 0);
            }

            hashMap.put(c, hashMap.get(c) + 1);
         }

         for (ResourceModule.ResourceColorSet c : hashMap.keySet()) {
            mapOut.put(c.toString(), hashMap.get(c));
         }

         return mapOut;
      }
   }

   public ArrayList<ResourceModule.ResourceColorSet> unpackResourceColorSetArray(String resourceColorSetArrayString) {
      YMLDataMap map = this.getSubMap(resourceColorSetArrayString);
      ArrayList<ResourceModule.ResourceColorSet> arrayOut = new ArrayList<>();

      ResourceModule.ResourceColorSet[] var7;
      for (ResourceModule.ResourceColorSet c : ResourceModule.ResourceColorSet.values()) {
         if (map.containsKey(c.toString())) {
            int count = Integer.parseInt(map.getOrDefault(c.toString(), 0));

            for (int i = 0; i < count; i++) {
               arrayOut.add(c);
            }
         }
      }

      return arrayOut;
   }

   public String packEnumArray(List<?> array) {
      if (array == null) {
         return null;
      } else {
         StringBuilder stringOut = new StringBuilder("");

         for (int i = 0; i < array.size(); i++) {
            stringOut.append((array.get(i) != null ? array.get(i).toString() : "null") + ",");
         }

         stringOut.deleteCharAt(stringOut.length() - 1);
         return stringOut.toString();
      }
   }

   public ArrayList<MobBase.MobType> unpackMobTypeArray(String mobTypeArrayString) {
      ArrayList<MobBase.MobType> arrayOut = new ArrayList<>();
      if (this.containsMap(mobTypeArrayString)) {
         YMLDataMap map = this.getSubMap(mobTypeArrayString);

         for (int count = 0; map.containsKey(Integer.toString(count)); count++) {
            String mobTypeTypeString = map.getOrDefault(Integer.toString(count), "null");
            mobTypeTypeString = SaveConversionUtilities.mobTypeConversion(this, mobTypeTypeString);

            MobBase.MobType[] type;
            for (MobBase.MobType typex : type = MobBase.MobType.values()) {
               if (mobTypeTypeString.equals(typex.toString())) {
                  arrayOut.add(typex);
                  break;
               }
            }
         }
      } else if (this.containsKey(mobTypeArrayString)) {
         String key = this.getOrDefault(mobTypeArrayString, "null");
         String[] arraySplit = key.split(",");

         for (String s : arraySplit) {
            s = SaveConversionUtilities.mobTypeConversion(this, s);

            MobBase.MobType[] var12;
            for (MobBase.MobType typex : MobBase.MobType.values()) {
               if (s.equals(typex.toString())) {
                  arrayOut.add(typex);
                  break;
               }
            }
         }
      }

      return arrayOut;
   }

   public ArrayList<ResourceModule.ResourceType> unpackResourceTypeArray(String resourceTypeArrayString) {
      ArrayList<ResourceModule.ResourceType> arrayOut = new ArrayList<>();
      if (this.containsMap(resourceTypeArrayString)) {
         YMLDataMap map = this.getSubMap(resourceTypeArrayString);

         for (int count = 0; map.containsKey(Integer.toString(count)); count++) {
            String resourceTypeTypeString = map.getOrDefault(Integer.toString(count), "null");

            ResourceModule.ResourceType[] type;
            for (ResourceModule.ResourceType typex : type = ResourceModule.ResourceType.values()) {
               if (resourceTypeTypeString.equals(typex.toString())) {
                  arrayOut.add(typex);
                  break;
               }
            }
         }
      } else if (this.containsKey(resourceTypeArrayString)) {
         String key = this.getOrDefault(resourceTypeArrayString, "null");
         String[] arraySplit = key.split(",");

         for (String s : arraySplit) {
            ResourceModule.ResourceType[] var12;
            for (ResourceModule.ResourceType typex : ResourceModule.ResourceType.values()) {
               if (s.equals(typex.toString())) {
                  arrayOut.add(typex);
                  break;
               }
            }
         }
      }

      return arrayOut;
   }

   public ArrayList<MobBase.MobGroup> unpackMobGroupArray(String mobGroupArrayString) {
      ArrayList<MobBase.MobGroup> arrayOut = new ArrayList<>();
      if (this.containsMap(mobGroupArrayString)) {
         YMLDataMap map = this.getSubMap(mobGroupArrayString);

         for (int count = 0; map.containsKey(Integer.toString(count)); count++) {
            String mobGroupTypeString = map.getOrDefault(Integer.toString(count), "null");

            MobBase.MobGroup[] type;
            for (MobBase.MobGroup typex : type = MobBase.MobGroup.values()) {
               if (mobGroupTypeString.equals(typex.toString())) {
                  arrayOut.add(typex);
                  break;
               }
            }
         }
      } else if (this.containsKey(mobGroupArrayString)) {
         String key = this.getOrDefault(mobGroupArrayString, "null");
         String[] arraySplit = key.split(",");

         for (String s : arraySplit) {
            MobBase.MobGroup[] var12;
            for (MobBase.MobGroup typex : MobBase.MobGroup.values()) {
               if (s.equals(typex.toString())) {
                  arrayOut.add(typex);
                  break;
               }
            }
         }
      }

      return arrayOut;
   }

   public ArrayList<String> unpackStringArray(String stringArrayString) {
      ArrayList<String> arrayOut = new ArrayList<>();
      if (this.containsKey(stringArrayString)) {
         String key = this.getOrDefault(stringArrayString, "null");
         String[] arraySplit = key.split(",");

         for (String s : arraySplit) {
            arrayOut.add(s);
         }
      }

      return arrayOut;
   }

   public String packObjectArrayIDs(ArrayList<?> arrayIn) {
      int[] arrayOut = new int[arrayIn.size()];

      for (int i = 0; i < arrayIn.size(); i++) {
         Object thisObject = arrayIn.get(i);
         if (thisObject instanceof MobBase) {
            arrayOut[i] = ((MobBase)thisObject).getID();
         } else if (thisObject instanceof ResourceBase) {
            arrayOut[i] = ((ResourceBase)thisObject).getID();
         } else if (thisObject instanceof ObjectBase) {
            arrayOut[i] = ((ObjectBase)thisObject).getID();
         } else if (thisObject instanceof FarmlandBase) {
            arrayOut[i] = ((FarmlandBase)thisObject).getID();
         } else {
            System.out.println("WARNING: Tried to pack " + thisObject.toString() + " object IDs but it is not supported in method packEnumArrayIDs()!");
         }
      }

      if (arrayOut.length == 0) {
         return null;
      } else {
         StringBuilder stringOut = new StringBuilder("");

         for (int e = 0; e < arrayOut.length; e++) {
            stringOut.append(arrayOut[e] + ",");
         }

         stringOut.deleteCharAt(stringOut.length() - 1);
         return stringOut.toString();
      }
   }

   public int[] unpackObjectArrayIDs(String arrayStringIn) {
      if (this.containsKey(arrayStringIn) && this.getOrDefault(arrayStringIn, "null").startsWith("[")) {
         String beforeSplit = this.getOrDefault(arrayStringIn, "null").replaceAll("\\[|\\]|\\s", "");
         if (!beforeSplit.equals("null") && !beforeSplit.equals("")) {
            String[] split = beforeSplit.split("\\,");
            int[] result = new int[split.length];

            for (int i = 0; i < split.length; i++) {
               result[i] = Integer.parseInt(split[i]);
            }

            return result;
         } else {
            return null;
         }
      } else if (!this.containsKey(arrayStringIn)) {
         return null;
      } else {
         String key = this.getOrDefault(arrayStringIn, "null");
         String[] arraySplit = key.split(",");
         if (arraySplit.length == 0) {
            return null;
         } else {
            int[] result = new int[arraySplit.length];

            for (int i = 0; i < arraySplit.length; i++) {
               result[i] = Integer.parseInt(arraySplit[i]);
            }

            return result;
         }
      }
   }

   public boolean containsMap(String key) {
      if (this.containsKey(key) && this.get(key) != null && !this.get(key).equals("null")) {
         try {
            HashMap<String, Object> rawDataMap = (HashMap<String, Object>)this.get(key);
            return true;
         } catch (Exception var3) {
            return false;
         }
      } else {
         return false;
      }
   }
}
