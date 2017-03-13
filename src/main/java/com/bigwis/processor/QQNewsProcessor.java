package com.bigwis.processor;

import com.bigwis.constant.WebConstant;
import com.bigwis.model.KeyWord;
import com.bigwis.service.KeyWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 腾讯新闻数据采集   只为测试 没有意义
 * Created by Administrator on 2017/3/8.
 */
//@Service
public class QQNewsProcessor implements PageProcessor {

    @Autowired
    private KeyWordService keyWordService;

    /**
     * 浏览器伪装
     */
    public static Site site = Site.me().setCycleRetryTimes(5).setRetryTimes(5).setSleepTime(500).setTimeOut(3 * 60 * 1000)
            .setUserAgent(WebConstant.USER_AGENT)
            .addHeader("Accept", WebConstant.HEADER_ACCEPT)
            .addHeader("Accept-Language", WebConstant.ACCEPT_LANGUAGE)
            .setCharset("GBK");

    private static final int voteNum = 1000;


    /**
     * 页面数据分析抽取
     *
     * @param page
     */
    @Override
    public void process(Page page) {
        List<String> newsTitle = page.getHtml().xpath("//ol[@class='current']/li/a/text()").all();
        int flag = 0;
        for (String keyword : newsTitle) {
            KeyWord keyWord = new KeyWord(keyword);
            keyWord.setSource("腾讯热搜");
            try {
                if (keyWordService.insertKeyWord(keyWord) > 0) {
                    flag++;
                }
            } catch (DuplicateKeyException e) {
                System.out.println("DuplicateKeyException");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
        System.out.println("=========腾讯采集中=========");
        System.out.println("共采集" + flag + "条");
        System.out.println("进行下一项采集");

    }

    @Override
    public Site getSite() {
        return site;
    }


    @PostConstruct
    public void exexute() {
        Page page = new HttpClientDownloader().download(new Request("http://news.qq.com/world_index.shtml"), site.toTask());
        process(page);
    }
}
