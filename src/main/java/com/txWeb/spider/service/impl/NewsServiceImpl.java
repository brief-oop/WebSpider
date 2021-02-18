package com.txWeb.spider.service.impl;

import com.txWeb.spider.bean.News;
import com.txWeb.spider.mapper.NewsMapper;
import com.txWeb.spider.service.NewsService;
import com.txWeb.spider.util.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

@Service
public class NewsServiceImpl implements NewsService {

    public int save(News news) {
        SqlSession session = MybatisUtil.getSession();
        NewsMapper mapper = session.getMapper(NewsMapper.class);
        int num = mapper.save(news);
        session.commit();
        session.close();
        return num;
    }
}
