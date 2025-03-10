package com.example.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {
    // 定义默认的请求间隔时间，单位为毫秒
    private static final long DEFAULT_REQUEST_INTERVAL = 2000; 
    // 记录上次请求的时间
    private static long lastRequestTime = 0; 

    public static String sendRequest(String urlStr, Map<String, String> headers) throws IOException {
        // 控制请求频率
        controlRequestFrequency();

        URL url = new URL(urlStr);
        // 创建代理对象，从代理池中获取代理
        Proxy proxy = new Proxy(Proxy.Type.HTTP, java.net.InetSocketAddress.createUnresolved(ProxyPool.getProxy().split(":")[0], Integer.parseInt(ProxyPool.getProxy().split(":")[1])));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
        connection.setRequestMethod("GET");

        // 设置默认请求头
        Map<String, String> defaultHeaders = getDefaultHeaders();
        for (Map.Entry<String, String> entry : defaultHeaders.entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }

        // 设置用户自定义请求头
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } catch (IOException e) {
            // 可以在这里添加更详细的异常处理逻辑，例如记录日志等
            System.err.println("Error sending request to " + urlStr + ": " + e.getMessage());
            throw e;
        }
    }

    private static Map<String, String> getDefaultHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("User - Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q = 0.9,image/avif,image/webp,image/apng,*/*;q = 0.8,application/signed - exchange;v = b3;q = 0.9");
        headers.put("Accept - Language", "zh - CN,zh;q = 0.9");
        headers.put("Referer", "https://www.cnki.net/");
        return headers;
    }

    private static void controlRequestFrequency() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastRequestTime < DEFAULT_REQUEST_INTERVAL) {
            try {
                // 线程休眠，保证请求间隔不小于 DEFAULT_REQUEST_INTERVAL
                Thread.sleep(DEFAULT_REQUEST_INTERVAL - (currentTime - lastRequestTime));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        lastRequestTime = System.currentTimeMillis();
    }
}
