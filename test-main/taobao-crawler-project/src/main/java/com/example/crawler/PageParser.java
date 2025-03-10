package com.example.crawler;

import com.example.crawler.RedisUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PageParser {
    public static void parsePage(String html) {
        Document soup = Jsoup.parse(html);
        Elements items = soup.select(".result-table-list tbody tr");
        for (Element item : items) {
            Elements detail = item.select("td");
            if (detail.size() >= 6) {
                String id = detail.get(0).text().trim();
                String name = detail.get(1).text().trim();
                String author = detail.get(2).text().trim();
                String resource = detail.get(3).text().trim();
                String time = detail.get(4).text().trim();
                String data = detail.get(5).text().trim();
                String paper = "{\"id\":\"" + id + "\",\"name\":\"" + name + "\",\"author\":\"" + author + "\",\"resource\":\"" + resource + "\",\"time\":\"" + time + "\",\"data\":\"" + data + "\"}";
                // 将数据保存到Redis
                RedisUtils.addPaper(paper);
                System.out.println(paper);
            }
        }
    }
}
