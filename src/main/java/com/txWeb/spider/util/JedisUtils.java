package com.txWeb.spider.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtils {

    public static JedisPool pool=null;
    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMinIdle(1);
        pool = new JedisPool(config,"localhost",6379);
    }

    public static Jedis getRedis(){
        return pool.getResource();
    }
}
