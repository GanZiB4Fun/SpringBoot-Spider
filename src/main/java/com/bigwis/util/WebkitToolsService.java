package com.bigwis.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.stereotype.Service;

import us.codecraft.webmagic.selector.Html;

/**
 * 工具服务服务类
 * 
 * @author frankx
 *
 * @date 2016年8月12日
 */
@Service
public class WebkitToolsService {

	/**
	 * 利用无界面浏览器，返回异步加载完成后的html页面
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String getPageText(String url) throws Exception {
		String is = PhantomTools.getPageText(url);
		return is;
	}

	/**
	 * return after reload page
	 * @param url
	 * @return
	 */
	public Html getDetailUrlAndType(String url){
		WebkitToolsService s = new WebkitToolsService();
		
		Html html = null;
		try {
			html = new Html(s.getPageText(url));
		} catch (Exception e) {
			e.printStackTrace();
		}


		return html;
	}
	
}
/**
 * 网页转图片处理类，使用外部CMD 需安装 phantomjs-2.1.1-windows
 */
class PhantomTools {

	// windows下phantomjs位置
	private static final String path = "C:/phantomjs-2.1.1-windows/";
	// 要执行的js，要使用决定路径
	private static final String jsPath = path + "bin/getPage.js ";

	/**
	 * 
	 * 根据传入的url，调用phantomjs进行下载，并返回base64流信息
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String getPageText(String url) throws IOException {
		Runtime rt = Runtime.getRuntime();

		final String cmd = path + "bin/phantomjs " + jsPath + url.trim();
		// 执行CMD命令
		Process process = rt.exec(cmd);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
		StringBuffer sbf = new StringBuffer();
		String tmp = "";
		while ((tmp = br.readLine()) != null) {
			sbf.append(tmp);
		}
		return sbf.toString();
	}
}