package com.bigwis.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigwis.constant.WebConstant;
import com.bigwis.model.KeyWord;
import com.bigwis.model.Tiangou;
import com.bigwis.service.KeyWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.annotation.PostConstruct;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/13.
 */
@Service
public class TianGouYunProcessor implements PageProcessor {
    @Autowired
    private KeyWordService keyWordService;

    private Site site = Site.me().
            setUserAgent(WebConstant.USER_AGENT).
            addHeader("accept", WebConstant.HEADER_ACCEPT).
            addHeader("Accept-Language", WebConstant.ACCEPT_LANGUAGE).setTimeOut(10000);

    /**
     * 抽取关键词信息 并保存
     *
     * @param page
     */
    @Override
    public void process(Page page) {


        //将连接加入连接池
        List<String> urls = new ArrayList<>();
        for (int i = 1; i <=1000 ; i++) {
            String url = "http://www.tngou.net/api/top/list?page="+i+"&rows=20";
            urls.add(url);
        }
        page.addTargetRequests(urls);

        int flag = 0;
        //获取数据
        String text = page.getRawText();
        JSONArray jsonArray = JSONObject.parseObject(text).getJSONArray("tngou");
        Tiangou tiangou = JSON.parseObject(text, Tiangou.class);

        for (int i = 0; i < tiangou.getTngou().size(); i++) {
            KeyWord keyWord = new KeyWord(tiangou.getTngou().get(i).getTitle());
            keyWord.setSource("www.tngou.net");
            try {
                if (keyWordService.insertKeyWord(keyWord)>0){
                    flag++;
                }
            }catch (DuplicateKeyException e){
                System.out.println("DuplicateKeyException");
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        System.out.println(tiangou.toString());
        System.out.println("=========天狗云数据采集中=========");
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
    public void exexute(){
        Spider.create(this)
                .addUrl("http://www.tngou.net/api/top/list?page=1&rows=20")
                .thread(10)
                .run();
    }
}
