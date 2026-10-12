package rtr.utilities;

import java.util.HashMap;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.yaml.snakeyaml.Yaml;

public class YAMLFlattener {
   public Map<String, Object> YAMLFlattener(String string) {
      return flattenYamlTree(new Yaml().loadAs(string, Map.class));
   }

   public Map<String, Object> YAMLFlattener(InputStream stream) {
      return flattenYamlTree(new Yaml().loadAs(stream, Map.class));
   }

   public Map<String, Object> YAMLFlattener(Reader reader) {
      return flattenYamlTree(new Yaml().loadAs(reader, Map.class));
   }

   public static Map<String, Object> flattenYamlTree(Map<?, ?> map) {
      Map<String, Object> out = new HashMap<>();

      for (Entry<?, ?> entry : map.entrySet()) {
         flattenYamlTree(entry.getKey().toString(), entry.getValue(), out);
      }

      return out;
   }

   private static void flattenYamlTree(String key, Object value, Map<String, Object> out) {
      if (value instanceof Map) {
         for (Entry<?, ?> e : ((Map<?, ?>)value).entrySet()) {
            flattenYamlTree(key + "." + e.getKey(), e.getValue(), out);
         }
      } else if (value instanceof List) {
         List<?> valueAsList = (List<?>)value;
         out.put(key, valueAsList.toArray(new String[0]));

         for (int i = 0; i < valueAsList.size(); i++) {
            out.put(key + "[" + i + "]", valueAsList.get(i));
         }
      } else {
         out.put(key, value);
      }
   }
}
