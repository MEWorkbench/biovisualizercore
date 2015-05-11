package pt.uminho.ceb.biosystems.mew.biovisualizercore.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.apache.jcs.engine.ElementAttributes;
import org.apache.jcs.engine.control.CompositeCacheManager;

/**
 * Provides a wrapper around Apache JCS (Java Caching System) to facilitate a "common case" 
 * for caching. This wrapper provides the following:
 * <ul>
 * <li> Typed interface, such as SimpleJcs&lt;String, String&gt;. 
 * <li> Automatic configuration of an indexed disk cache backing store.
 * <li> Write-through caching: All cached instances are written out to disk. 
 * <li> Provides a default maximum life for entries of 365 days.
 * <li> Provides a default maximum cache size of 100,000 instances.
 * <li> Provides a default directory location (~/.simplejcs) for cache files. 
 * <li> Ensures that all SimpleJcs instances have a unique name.
 * <li> All caches use the JCS "group" facility to allow access to the set of keys. 
 * <li> Constructor uses "days" rather than seconds as time unit for maxLife.
 * <li> put() uses "hours" rather than seconds as time unit for maxLife.
 * <li> A more convenient API for setting/getting items from the cache and controlling logging.
 * <li> Logging of exceptions raised by JCS (if a logger is supplied).
 * <li> Disables JCS logging messages unless the System property SimpleJcs.enableJCSLogging is set.
 * <li> Shutdown hook ensures that backing index file is closed correctly on JVM exit. 
 * <li> Convenient packaging mechanism for required jar files to simplify library use.
 * </ul>
 * 
 * @param <K> The type of the cache key.
 * @param <V> The type of the cache value. 
 * 
 * @author Philip Johnson
 */
public class JCSWrapper<K extends Serializable, V extends Serializable> {
  
  /** The number of seconds in a day. * */
  private static final long secondsInADay = 60L * 60L * 24L;
  /** Default number of days for max life. */
  private static final Long defaultMaxLifeDays = 365L; 
  /** Maximum number of in-memory instances before sending items to disk. Default is 50,000. */
  private static final Long defaultCapacity = 100000L;
  /** The name of this cache, which defines a "region" in JCS terms. */
  private String cacheName = null;
  /** The logger used for cache exception logging. */
  private Logger logger = null;
  /** Holds a list of already defined caches to help ensure uniqueness. */
  private static List<String> cacheNames = new ArrayList<String>();
  /** Default group name. No client should ever using the following string for a group. */
  private static final String DEFAULT_GROUP = "__Default_SimpleJcs_Group__";
  
  private static final String failureMsg = "Failure to clear cache ";
  
  /** Default directory where cache files go: ~/.simplejcs. **/
  private static final String defaultDir = System.getProperty("user.home") + "/.simplejcs";
  
  /** A thread that will ensure that all of these caches will be disposed of during shutdown. */ 
  private static Thread shutdownThread = new Thread() {
    /** Run the shutdown hook for disposing of all caches. */
    @Override 
    public void run() {
      for (String cacheName : cacheNames) {
        try {
          System.out.println("Shutting down " + cacheName + " cache.");
          JCS.getInstance(cacheName).dispose();
        }
        catch (Exception e) {
          String msg = failureMsg + cacheName + ":" + e.getMessage();
          System.out.println(msg);
        }
      }
    }
  };
  
  /** A boolean that enables us to figure out whether to install the shutdown thread.  */
  private static boolean hasShutdownHook = false;  //NOPMD
  
  /**
   * Creates a new SimpleJcs instance with the specified name.  
   * Defaults used for backing store directory, max life, and capacity.
   *  
   * @param cacheName The name of this cache.
   */
  public JCSWrapper(String cacheName) {
    this(cacheName, defaultDir, defaultMaxLifeDays * secondsInADay, defaultCapacity, null);
  }
  
  /**
   * Creates a new SimpleJcs instance with the specified name in the specified directory.
   * Defaults used for max life, and capacity.
   * 
   * @param cacheName The name of this cache.
   * @param dir The directory where the cache files are located. 
   */
  public JCSWrapper(String cacheName, String dir) {
    this(cacheName, dir, defaultMaxLifeDays * secondsInADay, defaultCapacity, null);
  }
  
  /**
   * Creates a new SimpleJcs with the specified parameters. 
   * If a cache with this name already exists, then this instance will be an alias to that cache
   * and its original configuration will remain unchanged. 
   * 
   * @param cacheName The name of this SimpleJcs, which will be used as the JCS "region".
   * @param dir the directory in which the backing store will be created.
   * @param maxLifeSeconds The maximum number of SECONDS after which items expire from the cache.
   * @param capacity The maximum number of instances to hold in the cache.
   * @param logger The user-supplied logger, if logging of errors is desired. Null if no logging.  
   */
  public JCSWrapper(String cacheName, String dir, Long maxLifeSeconds, Long capacity, 
      Logger logger) {
    // Only one SimpleJcs can be created at a time so that we can enforce unique cache names. 
    synchronized (JCSWrapper.class) {
      if (!JCSWrapper.hasShutdownHook) {
        Runtime.getRuntime().addShutdownHook(JCSWrapper.shutdownThread);
        JCSWrapper.hasShutdownHook = true;
      }
      this.cacheName = cacheName;
      this.logger = logger;
      mkdirs(dir);

      // Finish configuration if this is a new instance of the cache.
      if (!JCSWrapper.cacheNames.contains(cacheName)) {
        JCSWrapper.cacheNames.add(cacheName);
        if (!System.getProperties().containsKey("SimpleJcs.enableJCSLogging")) {
          Logger.getLogger("org.apache.jcs").setLevel(Level.OFF);
        }
        CompositeCacheManager ccm = CompositeCacheManager.getUnconfiguredInstance();
        ccm.configure(initJcsProps(cacheName, dir, maxLifeSeconds, capacity));
      }
    }
  }
  
  /**
   * Adds the key-value pair to this cache. Entry will expire from cache after the default maxLife.
   * Logs a message if the cache throws an exception.
   * 
   * @param key The key.
   * @param value The value.
   */
  public void put(K key, V value) {
    try {
      JCS.getInstance(this.cacheName).putInGroup(key, DEFAULT_GROUP, value);
    }
    catch (CacheException e) {
      String msg = "Failure to add " + key + " to cache " + this.cacheName + ":" + e.getMessage();
      log(msg);
    }
  }
  
  /**
   * Adds the key-value pair to this cache with an explicit expiration time.  
   * 
   * @param key The key.
   * @param value The value.
   * @param maxLifeHours The number of hours before this item will expire from cache.
   */
  public void put(K key, V value, double maxLifeHours) {
    try {
      ElementAttributes attributes = new ElementAttributes();
      long maxLifeSeconds = (long)(maxLifeHours * 3600D);
      attributes.setMaxLifeSeconds(maxLifeSeconds);
      attributes.setIsEternal(false);
      JCS.getInstance(this.cacheName).putInGroup(key, DEFAULT_GROUP, value, attributes);
    }
    catch (CacheException e) {
      String msg = "Failure to add " + key + " to cache " + this.cacheName + ":" + e.getMessage();
      log(msg);
    }
  }
  
  /**
   * Returns the object associated with key from the cache, or null if not found. 
   * 
   * @param key The key whose associated value is to be retrieved.
   * @return The value, or null if not found.
   */
  @SuppressWarnings("unchecked")
  public V get(K key) {
    try {
      return (V)JCS.getInstance(this.cacheName).getFromGroup(key, DEFAULT_GROUP);
    }
    catch (CacheException e) {
      String msg = "Failure of get: " + key + " in cache " + this.cacheName + ":" + e.getMessage();
      log(msg);
      return null;
    }
  }

  /**
   * Ensures that the key-value pair associated with key is no longer in this cache. 
   * Logs a message if the cache throws an exception.
   * 
   * @param key The key to be removed.
   */
  public void remove(K key) {
    try {
      JCS.getInstance(this.cacheName).remove(key, DEFAULT_GROUP);
    }
    catch (CacheException e) {
      String msg = "Failure to remove: " + key + " cache " + this.cacheName + ":" + e.getMessage();
      log(msg);
    }
  }
  
  /**
   * Removes everything in the default cache, but not any of the group caches. 
   */
  public void clear() {
    clearGroup(DEFAULT_GROUP);
  }

  /**
   * Clears the default as well as all group caches. 
   */
  public void clearAll() {
    try {
      JCS.getInstance(this.cacheName).clear();
    }
    catch (CacheException e) {
      String msg = failureMsg + this.cacheName + ":" + e.getMessage();
      log(msg);
    }
  }

  /**
   * Returns the set of keys associated with this cache. 
   * @return The set containing the keys for this cache. 
   */
  public Set<K> getKeys() {
    return getGroupKeys(DEFAULT_GROUP);
  }
  
  /**
   * Returns the current number of elements in this cache. 
   * @return The current size of this cache. 
   */
  public int size() {
    return getGroupSize(DEFAULT_GROUP);
  }
  
  
  /**
   * Shuts down the specified cache, and removes it from the list of active caches so it can be
   * created again.
   * 
   * @param cacheName The name of the cache to dispose of.
   */
  public static void dispose(String cacheName) {
    try {
      cacheNames.remove(cacheName);
      JCS.getInstance(cacheName).dispose();
    }
    catch (CacheException e) {
      String msg = failureMsg + cacheName + ":" + e.getMessage();
      System.out.println(msg);
    }
  }
  
  
  /**
   * Implements group-based addition of cache elements.
   * @param key The key.
   * @param group The group.
   * @param value The value.
   */
  public void putInGroup(K key, String group, V value) {
    try {
      JCS.getInstance(this.cacheName).putInGroup(key, group, value);
    }
    catch (CacheException e) {
      String msg = "Failure to add " + key + " to cache " + this.cacheName + ":" + e.getMessage();
      log(msg);
    }
  }

  /**
   * Implements group-based retrieval of cache elements. 
   * @param key The key.
   * @param group The group.
   * @return The element associated with key in the group, or null.
   */
  @SuppressWarnings("unchecked")
  public V getFromGroup(K key, String group) {
    try {
      return (V)JCS.getInstance(this.cacheName).getFromGroup(key, group);
    }
    catch (CacheException e) {
      String msg = "Failure of get: " + key + " in cache " + this.cacheName + ":" + e.getMessage();
      log(msg);
      return null;
    }
  }
  
  /**
   * Implements group-based removal of cache elements. 
   * @param key The key whose value is to be removed. 
   * @param group The group.
   */
  public void removeFromGroup(K key, String group) {
    try {
      JCS.getInstance(this.cacheName).remove(key, group);
    }
    catch (CacheException e) {
      String msg = "Failure to remove: " + key + " cache " + this.cacheName + ":" + e.getMessage();
      log(msg);
    }
  }
  
  /**
   * Returns the set of cache keys associated with this group.
   * @param group The group.
   * @return The set of cache keys for this group.
   */
  @SuppressWarnings("unchecked")
  public Set<K> getGroupKeys(String group) {
    Set<K> keySet;
    try {
      keySet = JCS.getInstance(this.cacheName).getGroupKeys(group);
    }
    catch (CacheException e) {
      String msg = "Failure to obtain keyset for cache: " + this.cacheName;
      log(msg);
      keySet = new HashSet<K>();
    }
    return keySet;
  }
  
  /**
   * Returns the current number of elements in this cache group.
   * @param group The name of the group.  
   * @return The current size of this cache. 
   */
  public int getGroupSize(String group) {
    return getGroupKeys(group).size();
  }
 
  /**
   * Removes everything in the specified group.
   * @param group The group name.  
   */
  public void clearGroup(String group) {
    try {
      JCS cache = JCS.getInstance(this.cacheName);
      for (Object key : cache.getGroupKeys(group)) {
        cache.remove(key, group);
      }
    }
    catch (CacheException e) {
      String msg = failureMsg + this.cacheName + ":" + e.getMessage();
      log(msg);
    }
  }

  /**
   * Sets up the Properties instance for configuring this JCS cache instance. Each SimpleJcs is
   * defined as a JCS "region". Given a SimpleJcs named "PJ", we create a properties instance whose
   * contents are similar to the following:
   * 
   * <pre>
   * jcs.region.PJ=DC-PJ
   * jcs.region.PJ.cacheattributes=org.apache.jcs.engine.CompositeCacheAttributes
   * jcs.region.PJ.cacheattributes.MaxObjects=[maxCacheCapacity]
   * jcs.region.PJ.cacheattributes.MemoryCacheName=org.apache.jcs.engine.memory.lru.LRUMemoryCache
   * jcs.region.PJ.cacheattributes.UseMemoryShrinker=true
   * jcs.region.PJ.cacheattributes.MaxMemoryIdleTimeSeconds=3600
   * jcs.region.PJ.cacheattributes.ShrinkerIntervalSeconds=3600
   * jcs.region.PJ.cacheattributes.MaxSpoolPerRun=500
   * jcs.region.PJ.elementattributes=org.apache.jcs.engine.ElementAttributes
   * jcs.region.PJ.elementattributes.IsEternal=false
   * jcs.region.PJ.elementattributes.MaxLifeSeconds=[maxIdleTime]
   * jcs.auxiliary.DC-PJ=org.apache.jcs.auxiliary.disk.indexed.IndexedDiskCacheFactory
   * jcs.auxiliary.DC-PJ.attributes=org.apache.jcs.auxiliary.disk.indexed.IndexedDiskCacheAttributes
   * jcs.auxiliary.DC-PJ.attributes.DiskPath=[cachePath]
   * jcs.auxiliary.DC-PJ.attributes.maxKeySize=10000000
   * </pre>
   * 
   * See bottom of: http://jakarta.apache.org/jcs/BasicJCSConfiguration.html for more details.
   * 
   * @param cacheName The name of this cache, used to define the region properties.
   * @param dir The directory name, used to generate the disk storage directory.
   * @param maxLifeSeconds The maximum life of instances in the cache in seconds before they expire.
   * @param maxCapacity The maximum size of this cache.
   * @return The properties file.
   */
  private Properties initJcsProps(String cacheName, String dir, Long maxLifeSeconds, 
      Long maxCapacity) {
    String reg = "jcs.region." + cacheName;
    String regCacheAtt = reg + ".cacheattributes";
    String regEleAtt = reg + ".elementattributes";
    String aux = "jcs.auxiliary.DC-" + cacheName;
    String auxAtt = aux + ".attributes";
    String memName = "org.apache.jcs.engine.memory.lru.LRUMemoryCache";
    String diskAttName = "org.apache.jcs.auxiliary.disk.indexed.IndexedDiskCacheAttributes";
    Properties props = new Properties();
    props.setProperty("jcs.default", "DC");
    //props.setProperty("jcs.default.cacheattributes.MaxObjects", maxCapacity.toString());
    //props.setProperty("jcs.default.elementattributes.MaxLifeSeconds", maxLifeSeconds.toString());
    
    props.setProperty(reg, "DC-" + cacheName);
    props.setProperty(regCacheAtt, "org.apache.jcs.engine.CompositeCacheAttributes");
    props.setProperty(regCacheAtt + ".MaxObjects", maxCapacity.toString());
    props.setProperty(regCacheAtt + ".MemoryCacheName", memName);
    props.setProperty(regCacheAtt + ".UseMemoryShrinker", "true");
    props.setProperty(regCacheAtt + ".MaxMemoryIdleTimeSeconds", "3600");
    props.setProperty(regCacheAtt + ".ShrinkerIntervalSeconds", "3600");
    props.setProperty(regCacheAtt + ".DiskUsagePatternName", "UPDATE");
    props.setProperty(regCacheAtt + ".MaxSpoolPerRun", "500");
    props.setProperty(regEleAtt, "org.apache.jcs.engine.ElementAttributes");
    props.setProperty(regEleAtt + ".IsEternal", "false");
    props.setProperty(regEleAtt + ".MaxLifeSeconds", maxLifeSeconds.toString());
    props.setProperty(aux, "org.apache.jcs.auxiliary.disk.indexed.IndexedDiskCacheFactory");
    props.setProperty(auxAtt, diskAttName);
    props.setProperty(auxAtt + ".DiskPath", dir);
    props.setProperty(auxAtt + ".maxKeySize", "1000000");
    
//    echoProperties(props);
    return props;
  }
  
  /**
   * Instantiates the directory indicated by dir, and throws an error if this is unsuccessful.
   * 
   * @param dir The directory where the cache files will go.
   */
  private void mkdirs(String dir) {
    File path = new File(dir);
    boolean dirsOk = path.mkdirs();
    if (!dirsOk && !path.exists()) {
      throw new RuntimeException("mkdirs() failed");
    }
  }
  
  /**
   * Returns a string containing all current properties in alphabetical order.
   * For debugging purposes. 
   * @param properties The properties of interest.
   */
  private void echoProperties(Properties properties) {
    String cr = System.getProperty("line.separator"); 
    String eq = "=";
    // Adding them to a treemap has the effect of alphabetizing them. 
    TreeMap<String, String> alphaProps = new TreeMap<String, String>();
    for (Map.Entry<Object, Object> entry : properties.entrySet()) {
      String propName = (String)entry.getKey();
      String propValue = (String)entry.getValue();
      alphaProps.put(propName, propValue);
    }
    StringBuffer buff = new StringBuffer(25);
    for (String key : alphaProps.keySet()) {
      buff.append(key).append(eq).append(properties.get(key)).append(cr);
    }
    // Now print the properties to a file. 
//    if(!propertiesFileAlreadyExists(".")){
	    BufferedWriter f;
	    try {
	      long timestamp = new Date().getTime();
	      f = new BufferedWriter(new FileWriter("jcs." + timestamp + ".properties"));
	      f.write(buff.toString());
	      f.close();
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
//    }
  }

  /**
   * Logs the message if a logger has been supplied.
   * @param msg The msg to be logged, if there is a logger. 
   */
  private void log(String msg) {
    if (this.logger != null) {
      this.logger.warning(msg);
    }
  }
  
  private boolean propertiesFileAlreadyExists(String folder){
	  //File dir = new File(".");
	  File dir = new File(folder);

	  File[] files = dir.listFiles(new FileFilter() {
		  @Override
		  public boolean accept(File file) {
			  return file.getName().matches("jcs.\\d+.properties");
		  }
	  });
	  
	  if(files.length>1)
		  return true;
	  
	  return false;
  }
}
