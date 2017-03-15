package com.bigwis.processor;

import com.alibaba.fastjson.JSON;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    /**
     * 初始化服務調度
     */
    public void init() {
        long delayTime = 5 * 60; // 每5分钟调度一次
        // 以相对固定时间执行调度（等上一个任务执行完成后，再延迟指定的时间）
        this.executor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                System.out.println("天狗热搜关键字采集 - 开始");
                execute();
                System.out.println("天狗热搜关键字采集 - 结束");
            }
        }, 0, delayTime, TimeUnit.SECONDS);
    }


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
        Tiangou tiangou = JSON.parseObject(text, Tiangou.class);

        for (int i = 0; i < tiangou.getTngou().size(); i++) {
            KeyWord keyWord = new KeyWord(tiangou.getTngou().get(i).getKeywords());
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
    public void execute() {
        Spider.create(this)
                .addUrl("http://www.tngou.net/api/top/list?page=1&rows=20")
                .thread(10)
                .run();
    }
}
