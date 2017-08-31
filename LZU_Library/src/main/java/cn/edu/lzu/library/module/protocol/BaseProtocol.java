package cn.edu.lzu.library.module.protocol;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import cn.edu.lzu.library.utils.UIUtils;

/**
 * Created by chris on 2017/4/10.
 * 定义规则和协议
 * 1. 统一维护网络请求
 * 2. 统一维护缓存机制
 */

public abstract class BaseProtocol<T> {
    public T getData(String url, int index, String params) {
        // 1. 从本地获取缓存数据
        String result = getDataFromLocal(url, index, params);
        String json;
        if (!TextUtils.isEmpty(result)) {
            //获取本地结果不为空，说明数据是本地的，赋值给json
            json = result;
        } else {
            //2,说明本地结果为空，从网络获取数据
            json = getDataFromNet(url, index, params);
        }
        //3,解析json
        return parseJson(json);
    }

    /**
     * 从网络获取json
     *
     * @param url   请求地址
     * @param index 请求页面的数据索引
     * @return 返回请求结果
     */
    private String getDataFromNet(String url, int index, String params) {
        String json = null;
       /* HttpUtils httpUtils = new HttpUtils();
        httpUtils.configHttpCacheSize(0);
        try {
            //同步请求
            ResponseStream responseStream = httpUtils.sendSync(HttpRequest.HttpMethod.GET,
                    url + "?index=" + index+params, null);
            //返回结果中的json
            json = responseStream.readString();
            //本地缓存
            if (!TextUtils.isEmpty(json)) {
                writeToLocal(json, url, index,params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return json;
    }


    /**
     * 从本地读取缓存
     *
     * @param url   缓存的url
     * @param index 页面索引
     * @return 返回本地缓存结果
     */
    private String getDataFromLocal(String url, int index, String params) {
        File cacheDir = UIUtils.getContext().getCacheDir();
        String substring = url.substring(url.lastIndexOf("/") + 1);
        File file = new File(cacheDir, substring + index + params);
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(
                    new FileReader(file.getAbsoluteFile()));
            //获取改文件的有效时间戳
            String invlidate = bufferedReader.readLine();
            long invlidTime = Long.parseLong(invlidate);
            //文件读取时间
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis < invlidTime) {
                //文件有效
                String data;
                StringBuilder stringBuilder = new StringBuilder();
                while ((data = bufferedReader.readLine()) != null) {
                    stringBuilder.append(data);
                }
                return stringBuilder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 写入本地缓存
     *
     * @param json  缓存内容
     * @param url   缓存内容是从哪个url获取的
     * @param index 页面的索引
     */
    private void writeToLocal(String json, String url, int index, String params) {
        File cacheDir = UIUtils.getContext().getCacheDir();
        String substring = url.substring(url.lastIndexOf("/") + 1);
        File file = new File(cacheDir, substring + index + params);
        FileWriter fileWriter;
        BufferedWriter bufferedWriter = null;
        try {
            fileWriter = new FileWriter(file.getAbsoluteFile());
            bufferedWriter = new BufferedWriter(fileWriter);
            //缓存第一行写入文件有效时间戳
            long invlidTime = System.currentTimeMillis() + 5 * 60 * 1000;
            bufferedWriter.write(invlidTime + "\r\n");
            //写入json
            bufferedWriter.write(json.toCharArray());
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    /**
     * 子类实现json解析
     *
     * @param json 网络获取的json数据
     * @return 返回处理结果
     */
    protected abstract T parseJson(String json);
}
