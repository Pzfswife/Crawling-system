package com.example;

import com.example.crawler.ZhiwangCrawler;
import com.example.flink.FlinkProcessor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
    public static void main(String[] args) {
        // 创建一个固定大小为 2 的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        // 提交知网爬虫任务到线程池
        executorService.submit(() -> {
            try {
                // 调用 ZhiwangCrawler 的 main 方法启动爬虫
                ZhiwangCrawler.main(args);
            } catch (Exception e) {
                // 捕获并打印异常信息
                e.printStackTrace();
            }
        });

        // 提交 Flink 处理任务到线程池
        executorService.submit(() -> {
            try {
                // 调用 FlinkProcessor 的 main 方法启动 Flink 处理
                FlinkProcessor.main(args);
            } catch (Exception e) {
                // 捕获并打印异常信息
                e.printStackTrace();
            }
        });

        // 关闭线程池
        executorService.shutdown();
    }
}