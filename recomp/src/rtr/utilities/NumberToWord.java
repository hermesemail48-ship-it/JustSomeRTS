package rtr.utilities;

public class NumberToWord {
   private static final String[] specialNames = new String[]{"", " thousand", " million", " billion", " trillion", " quadrillion", " quintillion"};
   private static final String[] tensNames = new String[]{"", " ten", " twenty", " thirty", " forty", " fifty", " sixty", " seventy", " eighty", " ninety"};
   private static final String[] numNames = new String[]{
      "",
      " one",
      " two",
      " three",
      " four",
      " five",
      " six",
      " seven",
      " eight",
      " nine",
      " ten",
      " eleven",
      " twelve",
      " thirteen",
      " fourteen",
      " fifteen",
      " sixteen",
      " seventeen",
      " eighteen",
      " nineteen"
   };

   private static String convertLessThanOneThousand(int number) {
      String current;
      if (number % 100 < 20) {
         current = numNames[number % 100];
         number /= 100;
      } else {
         current = numNames[number % 10];
         number /= 10;
         current = tensNames[number % 10] + current;
         number /= 10;
      }

      return number == 0 ? current : numNames[number] + " hundred" + current;
   }

   public static String convert(int number) {
      if (number == 0) {
         return "zero";
      } else {
         String prefix = "";
         if (number < 0) {
            number = -number;
            prefix = "negative";
         }

         String current = "";
         int place = 0;

         do {
            int n = number % 1000;
            if (n != 0) {
               String s = convertLessThanOneThousand(n);
               current = s + specialNames[place] + current;
            }

            place++;
            number /= 1000;
         } while (number > 0);

         String finalOut = (prefix + current).trim();
         String s1 = finalOut.substring(0, 1).toUpperCase();
         return s1 + finalOut.substring(1);
      }
   }
}
