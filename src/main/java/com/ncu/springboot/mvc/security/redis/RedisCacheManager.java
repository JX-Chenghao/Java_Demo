package com.ncu.springboot.mvc.security.redis;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 不再使用此类,只做学习Shiro-redis源码用途
 *
 */
public class RedisCacheManager implements CacheManager {

	private static final Logger logger = LoggerFactory
			.getLogger(RedisCacheManager.class);

	// fast lookup by name map
	private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();

	private RedisManager redisManager;

	private String keyPrefix = "shiro_redis_cache:";


	public String getKeyPrefix() {
		return keyPrefix;
	}

	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}
	
	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		logger.info("获取名称为 {} 的RedisCache实例",name);
		
		Cache cache = caches.get(name);
		
		if (cache == null) {

			// initialize the Redis manager instance
			redisManager.init();
			
			// create a new cache instance
			cache = new RedisCache<K, V>(redisManager, keyPrefix);
			
			// add it to the cache collection
			caches.put(name, cache);
		}
		return cache;
	}

	public RedisManager getRedisManager() {
		return redisManager;
	}

	public void setRedisManager(RedisManager redisManager) {
		this.redisManager = redisManager;
	}

}
