package com.llf.common.tools;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.llf.basemodel.utils.JsonUtils;
import com.llf.common.entity.NewsDetialEntity;
import com.llf.common.entity.NewsEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by llf on 2017/4/13.
 */

public class NewsJsonUtils {
    private final static String TAG = "NewsJsonUtils";

    /**
     * 将获取到的json转换为新闻列表对象
     *
     * @param res
     * @param value
     * @return
     */
    public static List<NewsEntity> readJsonDataBeans(String res, String value) {
        List<NewsEntity> beans = new ArrayList<>();
        try {
            JsonParser parser = new JsonParser();
            JsonObject jsonObj = parser.parse(res).getAsJsonObject();
            JsonElement jsonElement = jsonObj.get(value);
            if (jsonElement == null) {
                return null;
            }
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (int i = 1; i < jsonArray.size(); i++) {
                JsonObject jo = jsonArray.get(i).getAsJsonObject();
                if (jo.has("skipType") && "special".equals(jo.get("skipType").getAsString())) {
                    continue;
                }
                if (jo.has("TAGS") && !jo.has("TAG")) {
                    continue;
                }

                NewsEntity news = JsonUtils.deserialize(jo, NewsEntity.class);
                beans.add(news);
            }
        } catch (Exception e) {
        }
        return beans;
    }

    public static NewsDetialEntity readJsonNewsDetailBeans(String res, String docId) {
        NewsDetialEntity newsDetailBean = null;
        try {
            JsonParser parser = new JsonParser();
            JsonObject jsonObj = parser.parse(res).getAsJsonObject();
            JsonElement jsonElement = jsonObj.get(docId);
            if (jsonElement == null) {
                return null;
            }
            newsDetailBean = JsonUtils.deserialize(jsonElement.getAsJsonObject(), NewsDetialEntity.class);
        } catch (Exception e) {
        }
        return newsDetailBean;
    }
}
