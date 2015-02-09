package raffian.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nonnull;

/**
 * LRU cache implementation using a single LinkedHashMap 
 * for managing cache entries based on use.
 * 
 * This class is not synchronized; for thread-safe version, 
 * see LRUConcurrentCache.
 * 
 * @author  Raffi Basmajian
 * @date    2/8/2014
 * 
 */
public class LRULeanCache<K,V> {
   private final Map<K,V> cache;   
   private final int maxEntries;

   public LRULeanCache(
         int maxEntries
   ){
      this.maxEntries = maxEntries;      
      this.cache = new LinkedHashMap<K,V>(maxEntries, 0.75F, true){         
         private static final long serialVersionUID = -1218438805001761239L;
         @Override
         protected boolean removeEldestEntry(Map.Entry<K,V> eldest){            
            return size() > maxEntries;
         }
      };
   }
   
   public void put(K key, V value){
      cache.put(key, value);
   }
  
   public V get(@Nonnull K key){
      return cache.get(key);               
   }
   
   public String toString(){
      return cache.toString();
   }
   
   public static void main(String[] args) {
      LRULeanCache<String,String > cache = new LRULeanCache<>(3);
      cache.put("k1", "1");
      cache.put("k2", "2");
      cache.put("k3", "3");
      
      cache.get("k1");
      cache.put("k4", "4");
      cache.put("k5", "5");
      System.out.println(cache);
      assert cache.toString().equals("{k1=1, k4=4, k5=5}");
      
      cache.get("k3");
      cache.put("k2", "2");
      System.out.println(cache);
      assert cache.toString().equals("{k4=4, k5=5, k2=2}");
            
      cache.put("k5", "5");
      System.out.println(cache);
      assert cache.toString().equals("{k4=4, k2=2, k5=5}");
   }
}
