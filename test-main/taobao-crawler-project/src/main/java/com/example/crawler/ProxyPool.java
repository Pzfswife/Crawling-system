package com.example.crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProxyPool {
    private static List<String> proxies = new ArrayList<>();

    static {
        // 添加代理 IP 和端口
        proxies.add("127.0.0.1:8080");
        proxies.add("127.0.0.2:8081");
    }

    public static String getProxy() {
        Random random = new Random();
        int index = random.nextInt(proxies.size());
        return proxies.get(index);
    }
}
