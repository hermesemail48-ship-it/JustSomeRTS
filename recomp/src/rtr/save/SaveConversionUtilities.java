package rtr.save;

import rtr.console.Console;

public class SaveConversionUtilities {
   public static String mobTypeConversion(Object originClass, String mobTypeString) {
      String mobTypeOut = mobTypeString;
      String newString = null;
      if (mobTypeString.equals("VILLAGER")) {
         newString = "HUMAN_VILLAGER";
      }

      if (mobTypeString.equals("CHILD")) {
         newString = "HUMAN_VILLAGER_CHILD";
      }

      if (mobTypeString.equals("VILLAGER_ELDER")) {
         newString = "HUMAN_VILLAGER_ELDER";
      }

      if (mobTypeString.equals("NOMAD")) {
         newString = "HUMAN_NOMAD";
      }

      if (mobTypeString.equals("NOMAD_CHILD")) {
         newString = "HUMAN_NOMAD_CHILD";
      }

      if (mobTypeString.equals("NOMAD_ELDER")) {
         newString = "HUMAN_NOMAD_ELDER";
      }

      if (mobTypeString.equals("MIGRANT_DEPARTING")) {
         newString = "HUMAN_MIGRANT_DEPARTING";
      }

      if (mobTypeString.equals("MIGRANT_ARRIVING")) {
         newString = "HUMAN_MIGRANT_ARRIVING";
      }

      if (mobTypeString.equals("GHOST")) {
         newString = "HUMAN_VILLAGER_GHOST";
      }

      if (mobTypeString.equals("GHOST_CHILD")) {
         newString = "HUMAN_VILLAGER_GHOST_CHILD";
      }

      if (mobTypeString.equals("CATJEET_CHILD")) {
         newString = "CATJEET_VILLAGER_CHILD";
      }

      if (mobTypeString.equals("GHOST_CATJEET")) {
         newString = "CATJEET_GHOST";
      }

      if (mobTypeString.equals("GHOST_CATJEET_CHILD")) {
         newString = "CATJEET_GHOST_CHILD";
      }

      if (newString != null) {
         Console.saveConverterOut(originClass, "Release 1", "Update 1 Unstable 1", "Converting " + mobTypeString + " to " + newString + ".");
         mobTypeOut = newString;
      }

      return mobTypeOut;
   }
}
