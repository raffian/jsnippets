package raffian.cache;

import java.util.HashMap;
import java.util.LinkedList;
import javax.annotation.Nonnull;

/**
 * LRU cache implementation showing nuts and bolts of LRU. 
 * Uses LinkedList to track recently used keys, and HashMap 
 * as backing cache. See LRULeanCache for simpler implementation.
 * 
 * This class is not synchronized; for thread-safe version, 
 * see LRUConcurrentCache.
 * 
 * @author  Raffi Basmajian
 * @date    2/8/2014
 */
public class LRUCache<K,V> {
   private final LinkedList<K> recentKeys;
   private final HashMap<K,V> cache;
   private final int maxEntries;

   public LRUCache(
         int maxEntries
   ){
      this.maxEntries = maxEntries;
      this.recentKeys = new LinkedList<>();
      this.cache = new HashMap<>();
   }
   
   public void put(@Nonnull K key, V value){
      V previous = cache.put(key, value); 
      //remove existing key
      if(previous != null)               
         recentKeys.remove(key);      
      //discard last if at max capacity
      if(recentKeys.size() >= maxEntries)
         recentKeys.removeLast();      
      //set most recent key to front of list
      setMostRecentKey(key);
   }
  
   public V get(@Nonnull K key){
      V value = cache.get(key);
      if(value != null){
         recentKeys.remove(key);
         setMostRecentKey(key);         
      }      
      return value;               
   }
   
   protected void setMostRecentKey(@Nonnull K key){
      recentKeys.add(0, key);
   }
   
   public String recentKeysToString(){
      return recentKeys.toString();
   }
   
   public static void main(String[] args) {
      LRUCache<String,String > cache = new LRUCache<>(3);
      cache.put("k1", "1");
      cache.put("k2", "2");
      cache.put("k1", "1");
      cache.put("k3", "3");
      cache.put("k4", "4");
          
      assert cache.recentKeysToString().equals("[k4, k3, k1]");      
      System.out.println(cache.recentKeysToString());
      
      cache.get("k3");
      assert cache.recentKeysToString().equals("[k3, k4, k1]");  
      System.out.println(cache.recentKeysToString());
      
      cache.put("k1", "1");      
      assert cache.recentKeysToString().equals("[k1, k3, k4]");  
      System.out.println(cache.recentKeysToString());      
   }
}
