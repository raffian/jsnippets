package raffian.cache;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

/**
 * LRU cache implementation that's thread safe, uses 
 * ReentrantReadWriteLock instead of Collections.synchronizedMap()
 * for improved read throughput, assuming cache is read-mostly.
 *
 * @author  Raffi Basmajian
 * @date    2/8/2014
 */
@ThreadSafe
public class LRUConcurrentCache<K,V> {   
   private final Map<K,V> cache;   
   private final int maxEntries;

   public LRUConcurrentCache(
         int maxEntries
   ){
      this.maxEntries = maxEntries;      
      //TODO change this to ReentrantReadWriteLock
      this.cache = Collections.synchronizedMap(
            new LinkedHashMap<K,V>(this.maxEntries, 0.75F, true){                       
               private static final long serialVersionUID = -2926481390177591390L;
               @Override
               protected boolean removeEldestEntry(Map.Entry<K,V> eldest){            
                  return size() > maxEntries;
               }
            });
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
      LRUConcurrentCache<String,String > cache = new LRUConcurrentCache<>(3);
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
