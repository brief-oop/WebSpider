package com.txWeb.spider.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.txWeb.spider.bean.News;
import com.txWeb.spider.service.impl.NewsServiceImpl;
import com.txWeb.spider.util.JedisUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Component
public class GetNews {

    public static final String NEWS="spider";

    public static NewsServiceImpl service=new NewsServiceImpl();

    @Test
    @PostConstruct
    @Scheduled(cron = "0 0 */2 * * ?")
    public void spider() throws IOException {
        int page=0;

        while (true){
//            确认首页url
            String url="https://pacaio.match.qq.com/irs/rcd?cid=146&token=49cbb2154853ef1a74ff4e53723372ce&ext=ent&page="+page+"&callback=__jp13";
//            创建httpclient对象
            CloseableHttpClient httpClient = HttpClients.createDefault();
//            设置请求方式
            HttpGet get = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode()==200){
//                获取响应的数据
                String s = EntityUtils.toString(response.getEntity(), "utf-8");
//                转换为json数据
                s=toJsonString(s);
//                转换为集合
                HashMap map = JSON.parseObject(s, HashMap.class);
                if (Integer.parseInt(map.get("datanum").toString())==0){
                    break;
                }
                List<News> list = getList(map);
                System.out.println(list);
                for (News news : list) {
                    service.save(news);
                }
            }
            System.out.println(page);
            page++;
        }
        Date date = new Date();
        System.out.println("系统时间："+date.toString());
    }

    public static String toJsonString(String s){
        Integer start=s.indexOf("{");
        Integer end=s.lastIndexOf("}")+1;
        return s.substring(start,end);
    }

    public static List<News> getList(HashMap map){
        Jedis jedis = JedisUtils.getRedis();
        ArrayList<News> list = new ArrayList<News>();
        JSONArray data =(JSONArray) map.get("data");
        for (Object o : data) {
            Map newsMap = (Map) o;
            String url = newsMap.get("url").toString();
            Long num = jedis.sadd("NEWS", url);
            if (num==0){
                continue;
            }

            News news = new News();
            news.setTitle(newsMap.get("title").toString());
            news.setIntro(newsMap.get("intro").toString());
            news.setSource(newsMap.get("source").toString());
            news.setUrl(url);
            news.setPublish_time(newsMap.get("publish_time").toString());
            list.add(news);
        }
        jedis.close();
        return list;
    }
}
