package com.bigwis.processor;

import com.bigwis.constant.WebConstant;
import com.bigwis.model.KeyWord;
import com.bigwis.service.KeyWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 百度实时热搜关键词 采集器管理类
 * processor()作为页面分析方法
 * Created by Garen on 2017/3/8.
 */
//@Service
public class BaiduTopProcessor implements PageProcessor {

    @Autowired
    private KeyWordService keyWordService;

    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private Site site = Site.me().
            setUserAgent(WebConstant.USER_AGENT).
            addHeader("accept", WebConstant.HEADER_ACCEPT).
            addHeader("Accept-Language", WebConstant.ACCEPT_LANGUAGE).
            setCharset("GBK");

    /**
     * 初始化服務調度
     */
    public void init() {
        long delayTime = 5 * 60; // 每5分钟调度一次
        // 以相对固定时间执行调度（等上一个任务执行完成后，再延迟指定的时间）
        this.executor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                System.out.println("百度搜索热搜关键字 - 开始");
                execute();
                System.out.println("百度搜索热搜关键字 - 结束");
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
        //获取百度榜单链接
        List<String> urls = page.getHtml().xpath("//div[@class='hblock']/ul/li/a/@href").regex("([\\s\\S]+/buzz\\?b=[0-9]+)").all();
        //将榜单链接加入链接池
        page.addTargetRequests(urls);

        List<String> keyWords = page.getHtml().xpath("//table[@class='list-table']/tbody//td[@class='keyword']/a[@class='list-title']/text()").all();
        int flag = 0;
        for (String keyword : keyWords) {
            KeyWord keyWord = new KeyWord(keyword);
            keyWord.setSource("top.baidu.com");

            try {
                if (keyWordService.insertKeyWord(keyWord) != 0) {
                    flag++;
                }
            } catch (DuplicateKeyException e) {
                System.out.println("DuplicateKeyException");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

        System.out.println("=========百度采集中=========");
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
                .addUrl("http://top.baidu.com/buzz?b=1&c=513&fr=topbuzz_b341_c513")
                .thread(5)
                .run();
    }
}
