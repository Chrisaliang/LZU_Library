package cn.edu.lzu.library.dao.user;

/**
 * Created by chris on 2017/4/11.
 * 用户中心的 bean 类
 */

public class UserCenter {

    /**
     * msg : {"department":"","displayname":"","email":"","headpic":"http://p.ananas.chaoxing.com/star3/origin/5585c0d87df0dd92fe628828df933852.jpg?rw=481&rh=481","minheadpic":"http://p.ananas.chaoxing.com/star3/180_180/5585c0d87df0dd92fe628828df933852.jpg?rw=481&rh=481","phone":"","result":1,"schoolId":421,"schoolName":"兰州大学","showName":"兰州大学","uid":"175c7544a17943ef9d19b8146782849c","unableEditInfo":{"department":0,"displayname":0,"email":0,"phone":0},"uname":"3201309201811","userid":11738802}
     * result : 1
     */

    public MsgBean msg;
    /**
     * errorMsg : 用户名或密码错误！
     */
    public String errorMsg;
    public int result;


    public static class MsgBean {
        /**
         * department :
         * displayname :
         * email :
         * headpic : http://p.ananas.chaoxing.com/star3/origin/5585c0d87df0dd92fe628828df933852.jpg?rw=481&rh=481
         * minheadpic : http://p.ananas.chaoxing.com/star3/180_180/5585c0d87df0dd92fe628828df933852.jpg?rw=481&rh=481
         * phone :
         * result : 1
         * schoolId : 421
         * schoolName : 兰州大学
         * showName : 兰州大学
         * uid : 175c7544a17943ef9d19b8146782849c
         * unableEditInfo : {"department":0,"displayname":0,"email":0,"phone":0}
         * uname : 3201309201811
         * userid : 11738802
         */

        public String department;
        public String displayname;
        public String email;
        public String headpic;
        public String minheadpic;
        public String phone;
        public int result;
        public int schoolId;
        public String schoolName;
        public String showName;
        public String uid;
        public UnableEditInfoBean unableEditInfo;
        public String uname;
        public int userid;

        public static class UnableEditInfoBean {
            /**
             * department : 0
             * displayname : 0
             * email : 0
             * phone : 0
             */

            public int department;
            public int displayname;
            public int email;
            public int phone;

        }
    }
}
