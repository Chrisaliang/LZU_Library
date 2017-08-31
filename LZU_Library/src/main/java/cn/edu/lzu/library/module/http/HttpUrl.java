package cn.edu.lzu.library.module.http;

/**
 * Created by chris on 2017/4/10.
 * 网址管理
 */

public interface HttpUrl {
    String BASEURL = "http://219.246.191.24:8080";
    String HOUUSEMASTERALBUM = "http://lib.lzu.edu.cn/bgfm/info-6610.shtml";
    String CHARACTERISTICRESOURCE = BASEURL
            + "/sms/opac/news/showNewsList.action?xc=6&type=syzy&pageSize=20";
    String HALLNOTIFY = BASEURL +
            "/sms/opac/news/showNewsList.action?xc=6&type=tzgg&pageSize=20";
    String NEWBOOKRECOMMEND = "http://lib.lzu.edu.cn/xstg/list.shtml";
    String LZULIBRARYHOME = "http://lib.lzu.edu.cn/";
    String CLASSIFICATION = "http://www.ztflh.com/";
    String SERVICE = "http://lib.lzu.edu.cn/jyfw/info-6632.shtml";
    String ZXDT = "http://lib.lzu.edu.cn/gwdt/list.shtml";
    //http://219.246.191.24:8080/sms/opac/news/showNewsList.action?xc=6&type=syzy&pageSize=20
    //http://219.246.191.24:8080/sms/opac/news/showNewsList.action?xc=6&type=tzgg&pageSize=20
    // http://lib.lzu.edu.cn/xstg/list.shtml 新书推荐
    // http://lib.lzu.edu.cn/

}
