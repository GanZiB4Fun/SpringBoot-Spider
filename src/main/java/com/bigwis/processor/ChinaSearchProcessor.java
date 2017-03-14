package com.bigwis.processor;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigwis.constant.WebConstant;
import com.bigwis.model.KeyWord;
import com.bigwis.service.KeyWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 中国搜索实时热搜关键词 采集器管理类
 * processor()作为页面分析方法
 * Created by Garen on 2017/3/8.
 */
@Service
public class ChinaSearchProcessor implements PageProcessor {

    @Autowired
    private KeyWordService keyWordService;

    private Site site = Site.me().
            setUserAgent(WebConstant.USER_AGENT).
            addHeader("accept", WebConstant.HEADER_ACCEPT).
            addHeader("Accept-Language", WebConstant.ACCEPT_LANGUAGE).setCharset("UTF-8");

    /**
     * 抽取关键词信息 并保存
     *
     * @param page
     */
    @Override
    public void process(Page page) {

        //将连接加入连接池
        List<String> urls = new ArrayList<>();
        for (int i = 99; i > 1; i--) {
            String url = "http://news.chinaso.com/feapi/proxy/news-hotwords?&callback=jsonpCallback&type=jsonp&cv=201607v&query=%26subChannel%3D%26tableName%3Dnews%26tableId%3Dtable_2%26date%3D%26day%3D30%26type%3Dabstract%26sourceType%3D1%2C2%2C3%2C4%2C5%26IsRoll%3D0%2C1%26pageSize%3D20%26pageNo%3D" + i + "%26queryId%3D3618483034";
            urls.add(url);
        }
        page.addTargetRequests(urls);

        int flag = 0;
        //获取数据
        String text = page.getRawText();

        //处理返回数据
        if (text.indexOf("jsonpCallback(") != -1) {
            text = text.replace("jsonpCallback(", "").replace(")", "");
        }
        //抽取json数据
        JSONArray jsonArray = JSONObject.parseObject(text).getJSONArray("data");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            KeyWord keyWord = new KeyWord(jsonObject.get("title").toString());
            try {
                keyWord.setSource("www.chinaso.com");
                if (keyWordService.insertKeyWord(keyWord) != 0)
                    flag++;
            } catch (DuplicateKeyException e) {
                System.out.println("DuplicateKeyException");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

        System.out.println("=========中国搜索采集中=========");
        System.out.println("共采集" + flag + "条");
        System.out.println("进行下一项采集");
    }

    @Override
    public Site getSite() {
        return site;
    }

    /**
     * 爬虫入口
     */
    @PostConstruct
    public void exexute() {
        Spider.create(this)
                .addUrl("http://www.chinaso.com/search/api/hotsug.json")
                .thread(10)
                .run();
    }
}
