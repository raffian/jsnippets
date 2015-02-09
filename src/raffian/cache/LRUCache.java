package raffian.cache;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Simple LRU cache implementation; uses LinkedList
 * to track recently used keys, HashMap as backing cache.
 * 
 * This class is not synchronized; for thread-safe version, 
 * see ConcurrentLRUCache.
 * 
 * @author Raffi Basmajian
 * @date   2/8/2014
 * 
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
   
   public void set(K key, V value){
      V previous = cache.put(key, value); 
      //remove existing key
      if(previous != null)               
         recentKeys.remove(key);      
      //discard last if at max capacity
      if(recentKeys.size() >= maxEntries)
         recentKeys.removeLast();      
      //update with most recent used key
      recentKeys.add(0, key);
   }
  
   public V get(K key){
      V value = cache.get(key);
      if(value != null){
         recentKeys.remove(key);
         recentKeys.add(0, key);         
      }      
      return value;               
   }
   
   public String toString(){
      return String
               .format("recentKeys-> %s, cache-> %s",
                  recentKeys.toString(),
                  cache.toString());
   }
   
   public String recentKeysToString(){
      return recentKeys.toString();
   }
   
   public String cacheToString(){
      return cache.toString();
   }
   
   public static void main(String[] args) {
      LRUCache<String,String > cache = new LRUCache<>(3);
      cache.set("k1", "1");
      cache.set("k2", "2");
      cache.set("k1", "1");
      cache.set("k3", "3");
      cache.set("k4", "4");
      
      System.out.println(cache);      
      assert cache.recentKeysToString().equals("[k4, k3, k1]");
      assert cache.cacheToString().equals("{k1=1, k2=2, k3=3, k4=4}");
   }
}
