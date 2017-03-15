package com.bigwis.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by Administrator on 2017/3/15.
 */
@Service
public class MainProcessor {

    @Autowired
    BaiduTopProcessor baidu;
    @Autowired
    ChinaSearchProcessor chinaSo;
    @Autowired
    TianGouYunProcessor tianGou;
    @Autowired
    TouTiaoProcessor touTiao;


    /**
     * 统一调度
     */
    @PostConstruct
    public void execute() {
        baidu.init();
        chinaSo.init();
        tianGou.init();
        touTiao.init();
    }

}
