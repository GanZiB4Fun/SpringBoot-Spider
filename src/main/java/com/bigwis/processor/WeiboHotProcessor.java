package com.bigwis.processor;

import com.bigwis.util.WebkitToolsService;
import com.bigwis.constant.WebConstant;
import com.bigwis.service.KeyWordService;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by Administrator on 2017/3/9.
 */
//@Service
public class WeiboHotProcessor implements PageProcessor {
    @Autowired
    private KeyWordService keyWordService;

    private Site site = Site.me().
            setUserAgent(WebConstant.USER_AGENT).
            addHeader("accept", WebConstant.HEADER_ACCEPT).
            addHeader("Accept-Language", WebConstant.ACCEPT_LANGUAGE);

    /**
     * 抽取关键词信息 并保存
     *
     * @param page
     */
    @Override
    public void process(Page page) {
        //获取微博榜单链接
        WebkitToolsService webkitToolsService = new WebkitToolsService();
        Html html =webkitToolsService.getDetailUrlAndType("http://s.weibo.com/top/summary?Refer=top_hot&topnav=1&wvr=6");
        List<String> urls = html.xpath("//ul[@class='nav']/li/a/@href").all();
        List<String> category = html.xpath("//ul[@class='nav']/li/a/span/text()").all();
        //将榜单链接加入链接池
        page.addTargetRequests(urls);

        List<String> keyWords = page.getHtml().xpath("//table[@class='list-table']/tbody//td[@class='keyword']/a[@class='list-title']/text()").all();
//        int flag = 0;
//        for (String keyword : keyWords) {
//            KeyWord keyWord = new KeyWord(keyword);
//            keyWord.setSource("百度热搜关键词");
//
//            try {
//                if (keyWordService.insertKeyWord(keyWord) != 0) {
//                    flag++;
//                }
//            } catch (DuplicateKeyException e) {
//                System.out.println("DuplicateKeyException");
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//
//        }

//        System.out.println("=========百度采集完成=========");
//        System.out.println("共采集" + flag + "条");
//        System.out.println("进行下一项采集");
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
                .run();
    }

}
