package com.bigwis.processor;

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

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 今日头条 热门关键词采集
 * Created by Administrator on 2017/3/9.
 */
@Service
public class TouTiaoProcessor implements PageProcessor{

    @Autowired
    private KeyWordService keyWordService;

    /**
     * 伪装
     */
    private Site site = Site.me().
            setUserAgent(WebConstant.USER_AGENT).
            addHeader("accept",WebConstant.HEADER_ACCEPT).
            addHeader("Accept-Language",WebConstant.ACCEPT_LANGUAGE)
            ;

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
                System.out.println("头条热搜关键字 - 开始");
                execute();
                System.out.println("头条热搜关键字 - 结束");
            }
        }, 0, delayTime, TimeUnit.SECONDS);
    }


    @Override
    public void process(Page page) {

        //数据抽取
        List<String> keyWords = page.getHtml().xpath("//ul[@class='shenma']/li/a/text()").all();
        int flag = 0;
        //插入数据
        for (String keyword : keyWords) {
            KeyWord keyWord = new KeyWord(keyword);
            keyWord.setSource("www.toutiao.com");

            try {
                int tamp = keyWordService.insertKeyWord(keyWord);
                if ( tamp!= 0) {
                    flag++;
                }
            } catch (DuplicateKeyException e) {
                System.out.println("DuplicateKeyException");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

        System.out.println("=========今日头条采集中=========");
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
                .addUrl("http://m.toutiao.com/search/")
                .run();
    }
}
