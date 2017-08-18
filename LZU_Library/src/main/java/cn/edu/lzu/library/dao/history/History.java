package cn.edu.lzu.library.dao.history;


/**
 * 历史记录bean
 * Created by chris on 2017/3/27.
 */

public class History {
    public History() {
    }

    public History(int id, String url, long time, int type) {

        this.id = id;
        this.url = url;
        this.time = time;
        this.type = type;
    }

    public int id;     // 对应数据库_id列
    public String url; // 对应数据库url列
    public long time;  // 对应数据库time列
    public int type;   // 对应数据库type列
}
