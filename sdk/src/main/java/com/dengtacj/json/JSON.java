package com.dengtacj.json;

/**
 * Created by Westonli on 2016/11/9.
 */
public class JSON {
    static public String toJSONString(Object o) throws JSONException{
        return  com.alibaba.fastjson.JSON.toJSONString(o);
    }

     public static <T> T parseObject(String s, Class c) throws JSONException{
            return  (T)com.alibaba.fastjson.JSON.parseObject(s, c);
    }
}
