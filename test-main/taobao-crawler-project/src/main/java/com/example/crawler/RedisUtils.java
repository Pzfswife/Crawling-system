package com.example.crawler;

import redis.clients.jedis.Jedis;
public class RedisUtils {
    private static Jedis jedis = new Jedis("192.168.10.138", 6379);

    // 添加论文数据
    public static void addPaper(String paper) {
        jedis.rpush("zhiwang_papers", paper);
    }

    // 关闭Jedis连接
    public static void close() {
        if (jedis != null) {
            jedis.close();
        }
    }

    public static void addToQueue(String url) {
        jedis.rpush("url_queue", url);
    }

    public static String getFromQueue() {
        return jedis.lpop("url_queue");
    }

    public static boolean isCrawled(String url) {
        return jedis.sismember("crawled_urls", url);
    }

    public static void markAsCrawled(String url) {
        jedis.sadd("crawled_urls", url);
    }
}
