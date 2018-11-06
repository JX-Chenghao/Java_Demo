package com.ncu.springboot.mvc.security.redis;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class RedisCache<K, V> implements Cache<K, V> {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
		

	private RedisManager cache;
	
	/**
	 * The Redis key prefix for the sessions 
	 */
	private String keyPrefix = "shiro_redis_session:";
	

	public String getKeyPrefix() {
		return keyPrefix;
	}

	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}
	
	/**
	 * 通过一个JedisManager实例构造RedisCache
	 */
	public RedisCache(RedisManager cache){
		 if (cache == null) {
	         throw new IllegalArgumentException("Cache argument cannot be null.");
	     }
	     this.cache = cache;
	}
	
	/**
	 * Constructs a cache instance with the specified
	 * Redis manager and using a custom key prefix.
	 * @param cache The cache manager instance
	 * @param prefix The Redis key prefix
	 */
	public RedisCache(RedisManager cache, 
				String prefix){
		 
		this( cache );
		
		// set the prefix
		this.keyPrefix = prefix;
	}
	
	/**
	 * 获得byte[]型的key
	 * @param key
	 * @return
	 */
	private byte[] getByteKey(K key){
		if(key instanceof String){
			String preKey = this.keyPrefix + key;
    		return preKey.getBytes();
    	}else{
    		return SerializeUtils.serialize(key);
    	}
	}
 	
	@Override
	public V get(K key) {
		logger.info("ShiroCache--------------------Redis查询对象 {} ",key);
		try {
			if (key == null) {
	            return null;
	        }else{
	        	byte[] rawValue = cache.get(getByteKey(key));
	        	@SuppressWarnings("unchecked")
				V value = (V)SerializeUtils.deserialize(rawValue);
	        	return value;
	        }
		} catch (Exception t) {
			throw new CacheException(t);
		}

	}

	@Override
	public V put(K key, V value) {
		logger.info("ShiroCache--------------------redis保存 key {} ",key);
		 try {
			 	cache.set(getByteKey(key), SerializeUtils.serialize(value));
	            return value;
	        } catch (Exception t) {
	            throw new CacheException(t);
	        }
	}

	@Override
	public V remove(K key)  {
		logger.info("ShiroCache--------------------redis删除 key {}",key);
		try {
            V previous = get(key);
            cache.del(getByteKey(key));
            return previous;
        } catch (Exception t) {
            throw new CacheException(t);
        }
	}

	@Override
	public void clear()  {
		logger.info("ShiroCache--------------------redis删除所有元素");
		try {
            cache.flushDB();
        } catch (Exception t) {
            throw new CacheException(t);
        }
	}

	@Override
	public int size() {
		try {
			Long longSize = cache.dbSize();
            return longSize.intValue();
        } catch (Exception t) {
            throw new CacheException(t);
        }
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<K> keys() {
		try {
            Set<byte[]> keys = cache.keys(this.keyPrefix + "*");
            if (CollectionUtils.isEmpty(keys)) {
            	return Collections.emptySet();
            }else{
            	Set<K> newKeys = new HashSet<>();
            	for(byte[] key:keys){
            		newKeys.add((K)key);
            	}
            	return newKeys;
            }
        } catch (Exception t) {
            throw new CacheException(t);
        }
	}

	@Override
	public Collection<V> values() {
		try {
            Set<byte[]> keys = cache.keys(this.keyPrefix + "*");
            if (!CollectionUtils.isEmpty(keys)) {
                List<V> values = new ArrayList<>(keys.size());
                for (byte[] key : keys) {
                    @SuppressWarnings("unchecked")
					V value = get((K)key);
                    if (value != null) {
                        values.add(value);
                    }
                }
                return Collections.unmodifiableList(values);
            } else {
                return Collections.emptyList();
            }
        } catch (Exception t) {
            throw new CacheException(t);
        }
	}

}
