package raffian.cache;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

/**
 * LRU cache implementation that's thread safe, uses 
 * ReentrantReadWriteLock instead of Collections.synchronizedMap()
 *
 * @author  Raffi Basmajian
 * @date    2/10/2014
 */
@ThreadSafe
public class LRUConcurrentCache<K,V> {   
   private final Map<K,V> cache;   
   private final int maxEntries;
   private final ReadWriteLock readWriteLock;
   private final Lock readLock;
   private final Lock writeLock;
   
   public LRUConcurrentCache(
         int maxEntries
   ){
      this.maxEntries = maxEntries;            
      this.cache = new LinkedHashMap<K,V>(this.maxEntries, 0.75F, true){                       
               private static final long serialVersionUID = -2926481390177591390L;
               @Override
               protected boolean removeEldestEntry(Map.Entry<K,V> eldest){            
                  return size() > maxEntries;
               }
            };
      this.readWriteLock = new ReentrantReadWriteLock(true);
      this.readLock = readWriteLock.readLock();
      this.writeLock = readWriteLock.writeLock();
   }
   
   public void put(K key, V value){
      writeLock.lock();
      try{
         cache.put(key, value);
      }finally{
         writeLock.unlock();
      }
   }
  
   public V get(@Nonnull K key){
      readLock.lock();
      try{
         return cache.get(key);
      }finally{
         readLock.unlock();
      }
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
