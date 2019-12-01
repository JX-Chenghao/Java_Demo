package com.ncu.springboot.mvc.security.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Set;

/**
 * 不再使用此类,只做学习Shiro-redis源码用途
 *
 */
public class RedisManager {
    private  static final Logger LOG= LoggerFactory.getLogger(RedisManager.class);
	private String host = "127.0.0.1";
	private int port = 6379;
	//键值对 过期
	private int expire = 0;
	//链接conn 超时
	private int timeout = 2000;
	private String password = null;
	
	private static JedisPool jedisPool = null;
	
	public RedisManager(){
		
	}

	public void init(){
		if(jedisPool == null){
			if(StringUtils.isEmpty(password)){
                jedisPool = new JedisPool(new JedisPoolConfig(), host, port, timeout);
			}else{
                jedisPool = new JedisPool(new JedisPoolConfig(), host, port, timeout, password);
			}
            LOG.info("RedisManager 初始化成功");
		}
	}
	
	/**
	 * get value from redis
	 * @param key
	 * @return
	 */
	public byte[] get(byte[] key){
		byte[] value = null;
		Jedis jedis = jedisPool.getResource();
		try{
			value = jedis.get(key);
		}finally{
			jedis.close();
		}
		return value;
	}

	public byte[] set(byte[] key,byte[] value){
		Jedis jedis = jedisPool.getResource();
		try{
			jedis.set(key,value);
			if(this.expire != 0){
				jedis.expire(key, this.expire);
		 	}
		}finally{
			jedis.close();
		}
		return value;
	}
	

	public byte[] set(byte[] key,byte[] value,int expire){
		Jedis jedis = jedisPool.getResource();
		try{
			jedis.set(key,value);
			if(expire != 0){
				jedis.expire(key, expire);
		 	}
		}finally{
			jedis.close();
		}
		return value;
	}
	

	public void del(byte[] key){
		Jedis jedis = jedisPool.getResource();
		try{
			jedis.del(key);
		}finally{
			jedis.close();
		}
	}

	public void flushDB(){
		Jedis jedis = jedisPool.getResource();
		try{
			jedis.flushDB();
		}finally{
			jedis.close();
		}
	}
	

	public Long dbSize(){
		Long dbSize = 0L;
		Jedis jedis = jedisPool.getResource();
		try{
			dbSize = jedis.dbSize();
		}finally{
			jedis.close();
		}
		return dbSize;
	}


	public Set<byte[]> keys(String pattern){
		Set<byte[]> keys = null;
		Jedis jedis = jedisPool.getResource();
		try{
			keys = jedis.keys(pattern.getBytes());
		}finally{
			jedis.close();
		}
		return keys;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getExpire() {
		return expire;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static JedisPool getJedisPool() {
		return jedisPool;
	}

	public static void setJedisPool(JedisPool jedisPool) {
		RedisManager.jedisPool = jedisPool;
	}
}
