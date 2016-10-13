package com.kafka.demo04.newapi;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.util.*;

/**
 * Created by lushuai on 16/10/13.
 */
public class  JosnHelper {
    /**
     * 解析Json数据
     * @param josnString Json数据字符串
     */
    public static Map<String, Object> jsonToMap(String josnString) {
        JSONObject json = JSONObject.fromObject(josnString);
        return toMap(json);
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<>();
        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }
            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }
    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<>();
        for(int i = 0; i < array.size(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }
            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    public static void main(String[] args) {
        String jsonStr="{\n" +
                "    \"app_no\": \"123456\",\n" +
                "    \"channel\":\"meiyifen\",\n" +
                "    \"renfa\": {\n" +
                "        \"code\": 0,\n" +
                "        \"message\": \"³É¹¦\",\n" +
                "        \"indexs\": {\n" +
                "            \"RFCN010001\": 1,\n" +
                "            \"RFCN010002\": 1,\n" +
                "            \"RFCN010003\": \"2016-09-30\",\n" +
                "            \"RFCN010004\": \"2016-09-30\",\n" +
                "            \"RFCN010005\": 1,\n" +
                "            \"RFCN010006\": 1\n" +
                "        }\n" +
                "    },\n" +
                "    \"shixin\": {\n" +
                "        \"code\": 0,\n" +
                "        \"message\": \"³É¹¦\",\n" +
                "        \"indexs\": {\n" +
                "            \"SXCN010001\": 1,\n" +
                "            \"SXCN010002\": 1,\n" +
                "            \"SXCN010003\": \"2016-09-30\",\n" +
                "            \"SXCN010004\": \"2016-09-30\",\n" +
                "            \"SXCN010005\": 1,\n" +
                "            \"SXCN010006\": 1\n" +
                "        }\n" +
                "    },\n" +
                "    \"bop\": {\n" +
                "        \"code\": 0,\n" +
                "        \"message\": \"³É¹¦\",\n" +
                "        \"indexs\": {\n" +
                "            \"PBCR0D0041\": 1,\n" +
                "            \"PBCR0D0004\": 1,\n" +
                "            \"PBCR0Z0001\": 1,\n" +
                "            \"PBCR0D0005\": 1,\n" +
                "            \"PBCR0C0001\": 1,\n" +
                "            \"PBCR0D0052\": 1,\n" +
                "            \"PBCR0D0019\": 1,\n" +
                "            \"PBCR0Z0002\": 1,\n" +
                "            \"PBCR0D0053\": 1,\n" +
                "            \"PBCR0D0054\": 1,\n" +
                "            \"PBCR0D0055\": 1,\n" +
                "            \"PBCR0D0056\": 1,\n" +
                "            \"PBCR0D0020\": 1,\n" +
                "            \"PBCR0D0021\": 1,\n" +
                "            \"PBCR0D0022\": 1,\n" +
                "            \"PBCR0D0023\": 1,\n" +
                "            \"PBCR0D0057\": 1,\n" +
                "            \"PBCR0D0058\": 1,\n" +
                "            \"PBCR0D0059\": 1,\n" +
                "            \"PBCR0D0060\": 1,\n" +
                "            \"PBCR0D0024\": 1,\n" +
                "            \"PBCR0D0025\": 1,\n" +
                "            \"PBCR0D0026\": 1,\n" +
                "            \"PBCR0D0027\": 1,\n" +
                "            \"PBCR0D0061\": 1,\n" +
                "            \"PBCR0D0062\": 1,\n" +
                "            \"PBCR0D0063\": 1,\n" +
                "            \"PBCR0D0064\": 1,\n" +
                "            \"PBCR0D0028\": 1,\n" +
                "            \"PBCR0D0029\": 1,\n" +
                "            \"PBCR0D0030\": 1,\n" +
                "            \"PBCR0D0031\": 1,\n" +
                "            \"PBCR0D0065\": 1,\n" +
                "            \"PBCR0D0032\": 1,\n" +
                "            \"PBCR0Z0003\": 1,\n" +
                "            \"PBCR0D0042\": 1,\n" +
                "            \"PBCR0D0006\": 1,\n" +
                "            \"PBCR0D0007\": 1,\n" +
                "            \"PBCR0D0049\": 1,\n" +
                "            \"PBCR0D0016\": 1,\n" +
                "            \"PBCR0D0017\": 1,\n" +
                "            \"PBCR0D0043\": 2.21,\n" +
                "            \"PBCR0D0008\": 2.21,\n" +
                "            \"PBCR0D0009\": 2.21,\n" +
                "            \"PBCR0D0044\": 2.21,\n" +
                "            \"PBCR0D0010\": 2.21,\n" +
                "            \"PBCR0D0011\": 2.21,\n" +
                "            \"PBCR0D0051\": 2.21,\n" +
                "            \"PBCR0D0033\": 2.21,\n" +
                "            \"PBCR0D0034\": 2.21,\n" +
                "            \"PBCR0Z0011\": 2.21,\n" +
                "            \"PBCR0Z0004\": 2.21,\n" +
                "            \"PBCR0Z0005\": 2.21,\n" +
                "            \"PBCR0Z0006\": 2.21,\n" +
                "            \"PBCR0D0066\": 1,\n" +
                "            \"PBCR0D0045\": 1,\n" +
                "            \"PBCR0D0012\": 1,\n" +
                "            \"PBCR0Z0008\": 1,\n" +
                "            \"PBCR0D0050\": 1,\n" +
                "            \"PBCR0D0018\": 1,\n" +
                "            \"PBCR0Z0009\": 1,\n" +
                "            \"PBCR0D0067\": 2.21,\n" +
                "            \"PBCR0D0068\": 2.21,\n" +
                "            \"PBCR0E0007\": 1,\n" +
                "            \"PBCR0E0008\": 1,\n" +
                "            \"PBCR0E0009\": 1,\n" +
                "            \"PBCR0E0010\": 1,\n" +
                "            \"PBCR0E0011\": 1,\n" +
                "            \"PBCR0E0012\": 1,\n" +
                "            \"PBCR0E0013\": 1,\n" +
                "            \"PBCR0E0014\": 1,\n" +
                "            \"PBCR0E0015\": 1,\n" +
                "            \"PBCR0D0046\": 1,\n" +
                "            \"PBCR0D0047\": 1,\n" +
                "            \"PBCR0D0048\": 1,\n" +
                "            \"PBCR0D0013\": 1,\n" +
                "            \"PBCR0D0014\": 1,\n" +
                "            \"PBCR0D0015\": 1,\n" +
                "            \"PBCR0D0040\": 1,\n" +
                "            \"PBCR0D0069\": 1,\n" +
                "            \"PBCR0D0071\": 1,\n" +
                "            \"PBCR0D0003\": 1,\n" +
                "            \"PBCR0D0035\": 2.21,\n" +
                "            \"PBCR0D0039\": 1,\n" +
                "            \"PBCR0D0036\": 1,\n" +
                "            \"PBCR0D0037\": 1,\n" +
                "            \"PBCR0D0072\": 1,\n" +
                "            \"PBCR0D0001\": 1,\n" +
                "            \"PBCR0B0006\": 1,\n" +
                "            \"PBCR0B0007\": 1,\n" +
                "            \"PBCR0B0005\": 1,\n" +
                "            \"PBCR0E0001\": 1,\n" +
                "            \"PBCR0B0003\": 1,\n" +
                "            \"PBCR0B0001\": 1,\n" +
                "            \"PBCR0B0002\": 1,\n" +
                "            \"PBCR0B0004\": 1,\n" +
                "            \"PBCR0E0002\": 1,\n" +
                "            \"PBCR0E0003\": 2.21,\n" +
                "            \"PBCR0E0004\": \"abcd\",\n" +
                "            \"PBCR0C0003\": 1,\n" +
                "            \"PBCR0D0070\": 2.21,\n" +
                "            \"PBCR0D0038\": 2.21,\n" +
                "            \"PBCR0D0002\": 1,\n" +
                "            \"PBCR0C0002\": 2.21,\n" +
                "            \"PBCR0E0005\": 1,\n" +
                "            \"PBCR0E0006\": 1,\n" +
                "            \"PBCR0Z0010\": 1,\n" +
                "            \"PBCR0E0016\": \"abcd\",\n" +
                "            \"PBCR0D0073\": 1,\n" +
                "            \"PBCR0D0074\": 1,\n" +
                "            \"PBCR0D0075\": 1.23,\n" +
                "            \"PBCR0D0076\": 1.23,\n" +
                "            \"PBCR0D0077\": 1,\n" +
                "            \"PBCR0D0078\": 1.23,\n" +
                "            \"PBCR0D0079\": 1.23,\n" +
                "            \"PBCR0D0080\": 1.23,\n" +
                "            \"PBCR0D0081\": 1.23,\n" +
                "            \"PBCR0D0082\": 1.23,\n" +
                "            \"PBCR0D0083\": 1.23,\n" +
                "            \"PBCR0D0084\": 1,\n" +
                "            \"PBCR0D0085\": 1,\n" +
                "            \"PBCR0D0086\": 1,\n" +
                "            \"PBCR0D0087\": 1,\n" +
                "            \"PBCR0D0088\": 1,\n" +
                "            \"PBCR0D0089\": 1,\n" +
                "            \"PBCR0D0090\": 1,\n" +
                "            \"PBCR0D0091\": 1\n" +
                "        }\n" +
                "    }\n" +
                "}";
        Map<String, Object> map = jsonToMap(jsonStr);

        System.out.println(String.format("key:app_no,value:%s",map.get("app_no")));
        System.out.println(String.format("key:channel,value:%s",map.get("channel")));
        //renfa
        Map<String, Object> mapRenfa = (Map<String, Object>)map.get("renfa");
        System.out.println(String.format("key:code,value:%s",mapRenfa.get("code")));
        System.out.println(String.format("key:message,value:%s",mapRenfa.get("message")));
        Map<String, Object> indexsRenfa = (Map<String, Object>)mapRenfa.get("indexs");
        System.out.println(String.format("key:RFCN010001,value:%s",indexsRenfa.get("RFCN010001")));
        System.out.println(String.format("key:RFCN010002,value:%s",indexsRenfa.get("RFCN010002")));
        System.out.println(String.format("key:RFCN010003,value:%s",indexsRenfa.get("RFCN010003")));
        System.out.println(String.format("key:RFCN010004,value:%s",indexsRenfa.get("RFCN010004")));
        System.out.println(String.format("key:RFCN010005,value:%s",indexsRenfa.get("RFCN010005")));
        //shixin
        Map<String, Object> mapShixin = (Map<String, Object>)map.get("shixin");
        System.out.println(String.format("key:code,value:%s",mapShixin.get("code")));
        System.out.println(String.format("key:message,value:%s",mapShixin.get("message")));
        Map<String, Object> indexsShixin = (Map<String, Object>)mapShixin.get("indexs");
        System.out.println(String.format("key:SXCN010001,value:%s",indexsShixin.get("SXCN010001")));
        System.out.println(String.format("key:SXCN010002,value:%s",indexsShixin.get("SXCN010002")));

        //bop
        Map<String, Object> mapBop = (Map<String, Object>)map.get("bop");
        System.out.println(String.format("key:code,value:%s",mapBop.get("code")));
        System.out.println(String.format("key:message,value:%s",mapBop.get("message")));
        Map<String, Object> indexsBop = (Map<String, Object>)mapBop.get("indexs");
        System.out.println(String.format("key:PBCR0D0041,value:%s",indexsBop.get("PBCR0D0041")));
        System.out.println(String.format("key:PBCR0D0004,value:%s",indexsBop.get("PBCR0D0004")));

    }
}
