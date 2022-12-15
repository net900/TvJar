package com.github.catvod.spider;

import android.content.Context;
import android.text.TextUtils;
import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.spider.merge.RSA;
import com.github.catvod.utils.okhttp.OkHttpUtil;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class SP293 extends Spider {
    public static String l = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAL7WXD09MzbK8i3I\n0Rsg76zuRFEItrQ908umVh5hRbbiNVLVc53e9Ex0jfAlstQXxkV3rR2bT7ThRz6Z\nnpLuYpbMsAmbUXuhaJW9eHVoM7KlpJhlUCOdK1RoqHeFJUfj0qKaL1/O18Sztd4l\nFHumW5Hz0mWjtbUgimEelJvpfjs1AgMBAAECgYBwFEFUDg94DLUzQo/c2PkqnT0u\nQmnbbbxoDKbjbMme4TfkEEy42WqtRH7gHaPrgJMH1YOssbycRPqsv1gsfykS1luO\nTAO0EIZKRx1caZsdJfc17rRrCitP9jcBmXm7lX16OH5A1ffbOICy/MKFVs7ON836\nqN3v24j1t0aActKYQQJBAPlet22mfeFe7GpDM2I2hdgDvochbYZSuL1Gt3wHJzk5\nNql7eW+VDIh0JTtSem02KdKmF6urm4PBvkrNuKeM+1kCQQDD6UD+r0mLWHVDrGv4\n0q7TGz6vfVQnpPferoty8NcLTIYXrMY+9VieAGzof1VuKuTLF++VoEo1bxB2SbP1\nqS89AkAz54QUfagL5d8dixlB4wle2gCpTcrnP4aWVwbP+Qkv/vmis0GmeQafzFUH\nkPZMjw6LeujIYbK/7O630TQTI+QZAkEAoXom6kWMtuBId2ks7cCp/LMeLgN9U9fz\nvoXbxvegKF4AwI7WMJqFWmY7Xj9mKRIN1yB5h7jAsd5DkVwaisfSeQJBALfrWoWS\nh44ASfjZgm7mL0qGVkYzKCbbKsIDe2mHWGgFhN0QdLz59i5icN9CvnAdu1Bwm8zB\nmhR7kKqxhxX0g+Y=";

    private String n8 = "";
    private Map<String, Set<String>> papi = new HashMap();
    private String token = "";
    private String csrf = "j%2FQUwbt62ulEg01bYSw7Cd0w8AYQLJhG5qaSBsVgVI1PSHSOClETCI31BVIVTbuas6Jlq55xldhAfPczk6yVB%2FqKciRnTt6ylTF6SNHtZnu6jhiBEGu1uWRlIaUlPiOD%2Bi9YUPxih%2FmY8OKmSpKQSSKwU9NXZGGLZImfuRG117o%3D";
    private String I4 = "";


    private HashMap<String, String> n8() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 10; PBEM00 Build/QKQ1.190918.001)");
        return hashMap;
    }

    private String qb(String str, String str2) {
        return RSA.qb(str, str2);
    }

    private String zC() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
        JSONObject result = new JSONObject();
        try {
            String url = this.n8 + "/";
            Map<String, String> params = new HashMap<>();
            if (extend != null) {
                params.putAll(extend);
            }
            params.put("class", tid);
            params.put("page", pg);
            params.put("ac", "list");

            List<String> ps = new LinkedList<>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                ps.add(entry.getKey() + "=" + entry.getValue());
            }
            ps.add("csrf=" + this.csrf);
            url = url + "?" + TextUtils.join("&", ps);
            String encodeData = OkHttpUtil.string(url, n8());
            JSONObject json = new JSONObject(qb(encodeData,l));
            JSONArray list = json.optJSONArray("data");
            JSONArray vods = new JSONArray();
            for (int i = 0; i < list.length(); i++) {
                JSONObject item = list.getJSONObject(i);
                JSONObject vod = new JSONObject();
                String nextlink = item.getString("nextlink");
                String vod_id = nextlink.substring(nextlink.indexOf("ids=") + 4);
                vod.put("vod_id", vod_id);
                vod.put("vod_name", item.getString("title"));
                vod.put("vod_pic", item.getString("pic"));
                vod.put("vod_remarks", item.getString("state"));
                vods.put(vod);
            }
            int parseInt = Integer.parseInt(pg);
            result.put("page", parseInt);
            if (vods.length() == 20) {
                parseInt++;
            }
            result.put("pagecount", parseInt);
            result.put("limit", 20);
            result.put("total", Integer.MAX_VALUE);
            result.put("list", vods);
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return result.toString();
    }


    public String detailContent(List<String> ids) {
        JSONObject result = new JSONObject();
        try {
            String url = this.n8 +  "/?ac=detail&ids=" + ids.get(0) + "&csrf=" + this.csrf + "&token=" + this.token;
            String encodeData = OkHttpUtil.string(url, n8());
            JSONObject vodInfos = new JSONObject(encodeData);
            JSONObject vodInfo = new JSONObject(qb(vodInfos.getString("data"),l));
            JSONObject vod = new JSONObject();
            vod.put("vod_id", vodInfo.getString("id"));
            vod.put("vod_name", vodInfo.getString("title"));
            vod.put("vod_pic", vodInfo.getString("img_url"));
            vod.put("vod_year", vodInfo.optString("pubtime"));
            vod.put("vod_remarks", vodInfo.optString("season_num"));
            vod.put("vod_content", vodInfo.optString("intro").trim());
            JSONArray type = vodInfo.optJSONArray("type");
            if (type != null && type.length() > 0) {
                vod.put("type_name", type.toString().replace("[\"", "").replace("\"]", ""));
            }
            JSONArray area = vodInfo.optJSONArray("area");
            if (area != null && area.length() > 0) {
                vod.put("vod_area", area.toString().replace("[\"", "").replace("\"]", ""));
            }
            JSONArray actor = vodInfo.optJSONArray("actor");
            if (actor != null && actor.length() > 0) {
                vod.put("vod_actor", actor.toString().replace("[\"", "").replace("\"]", ""));
            }
            JSONArray director = vodInfo.optJSONArray("director");
            if (director != null && director.length() > 0) {
                vod.put("vod_director", director.toString().replace("[\"", "").replace("\"]", ""));
            }
            JSONArray videolist = vodInfos.optJSONArray("video_list");
            Map < String, String > map = new HashMap < String, String > ();
            for (int j = 0; j < videolist.length(); j++) {
                JSONObject item = videolist.getJSONObject(j);
                String source = item.getString("name");
                String typess = item.getString("type");
                JSONArray list = item.optJSONArray("list");
                JSONArray apiurls = item.optJSONArray("parse");
                HashSet hashSet = new HashSet();
                for (int i2 = 0; i2 < apiurls.length(); i2++) {
                    hashSet.add(apiurls.optString(i2));
                }
                this.papi.put(source, hashSet);

                List < String > sourceValues = new ArrayList < > ();
                for (int jj = 0; jj < list.length(); jj++) {
                    JSONObject items = list.getJSONObject(jj);
                    String title = items.optString("title");
                    String urlv = items.optString("url");
                    sourceValues.add(title + "$" + urlv);
                }
                map.put(source, TextUtils.join("#", sourceValues));
            }
            String join = TextUtils.join("$$$", map.keySet());
            String join2 = TextUtils.join("$$$", map.values());
            vod.put("vod_play_from", join);
            vod.put("vod_play_url", join2);
            JSONArray vods = new JSONArray();
            vods.put(vod);
            result.put("list", vods);
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return result.toString();
    }

    public String homeContent(boolean filter) {
        try {
            JSONObject result = new JSONObject();
            JSONArray classes = new JSONArray();
            LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("movie", "电影");
            linkedHashMap.put("tvplay", "剧集");
            linkedHashMap.put("comic", "动漫");
            linkedHashMap.put("tvshow", "综艺");
            for (String str : linkedHashMap.keySet()) {
                JSONObject item = new JSONObject();
                item.put("type_id", str);
                item.put("type_name", linkedHashMap.get(str));
                classes.put(item);
            }
            result.put("class", classes);

            if (filter) {
                JSONObject filterBox = new JSONObject();
                result.put("filters", filterBox);
                String json = OkHttpUtil.string(this.n8 + "?ac=flitter&csrf=" + this.csrf, n8());

                JSONObject sourceData = new JSONObject(qb(json, l));
                for (String tid : linkedHashMap.keySet()) {
                    JSONArray filterArr = new JSONArray();
                    filterBox.put(tid, filterArr);
                    JSONArray typeJSON = sourceData.optJSONArray(tid);
                    for (int j = 0; j < typeJSON.length(); j++) {
                        JSONObject item = typeJSON.optJSONObject(j);
                        String itemName = item.optString("name");
                        String itemField = item.optString("field");
                        JSONArray itemValues = item.optJSONArray("values");
                        JSONObject jOne = new JSONObject();
                        jOne.put("key", itemField);
                        jOne.put("name", itemName);
                        filterArr.put(jOne);
                        JSONArray values = new JSONArray();
                        jOne.put("value", values);

                        JSONObject all = new JSONObject();
                        all.put("n", "全部");
                        all.put("v", "");
                        values.put(all);
                        for (int k = 0; k < itemValues.length(); k++) {
                            String k1 = itemValues.optString(k);
                            JSONObject kvo = new JSONObject();
                            kvo.put("n", k1);
                            kvo.put("v", k1);
                            values.put(kvo);
                        }
                    }
                }
            }

            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
            return "";
        }
    }

    public String homeVideoContent() {
        JSONObject result = new JSONObject();
        try {
            String url = this.n8 + "/?ac=list&class=tvplay&page=1&csrf=" + this.csrf;
            String encodeData = OkHttpUtil.string(url, n8());
            JSONObject json = new JSONObject(qb(encodeData,l));
            JSONArray data = json.optJSONArray("data");
            JSONArray list = new JSONArray();
            for (int i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                JSONObject vod = new JSONObject();
                String nextlink = item.getString("nextlink");
                String vod_id = nextlink.substring(nextlink.indexOf("ids=") + 4);
                vod.put("vod_id", vod_id);
                vod.put("vod_name", item.getString("title"));
                vod.put("vod_pic", item.getString("pic"));
                vod.put("vod_remarks", item.getString("state"));
                list.put(vod);
            }
            result.put("list", list);
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return result.toString();
    }

    public void init(Context context) {
        SP293.super.init(context);
    }

    public boolean isVideoFormat(String str) {
        return SP293.super.isVideoFormat(str);
    }

    public boolean manualVideoCheck() {
        return SP293.super.manualVideoCheck();
    }

    public String playerContent(String str, String str2, List<String> list) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("parse", "0");
            jSONObject.put("playUrl", "");
            if (this.papi.containsKey(str)) {
                Iterator<String> it = this.papi.get(str).iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    JSONObject jSONObject2 = new JSONObject(OkHttpUtil.string(it.next() + str2 + "&app=10004&app=10004&account=&password=", n8()));
                    String optString = jSONObject2.optString("data");

                    if (Integer.valueOf(jSONObject2.optInt("code")).intValue() == 1) {
                        String optString4 = new JSONObject(optString).optString("url");
                        if (!TextUtils.isEmpty(optString4)) {
                            jSONObject.put("url", optString4);
                            break;
                        }

                    }
                }
            }
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return jSONObject.toString();
    }


    public String searchContent(String key, boolean quick) {
        JSONObject result = new JSONObject();
        try {
            String url = this.n8 + "/?ac=list&zm=" + key + "&page=1&csrf=" + this.csrf;
            String encodeData = OkHttpUtil.string(url, n8());
            JSONObject json = new JSONObject(qb(encodeData,l));
            JSONArray list = json.optJSONArray("data");
            JSONArray vods = new JSONArray();
            for (int i = 0; i < list.length(); i++) {
                JSONObject item = list.getJSONObject(i);
                JSONObject vod = new JSONObject();
                String nextlink = item.getString("nextlink");
                String vod_id = nextlink.substring(nextlink.indexOf("ids=") + 4);
                vod.put("vod_id", vod_id);
                vod.put("vod_name", item.getString("title"));
                vod.put("vod_pic", item.getString("pic"));
                vod.put("vod_remarks", item.getString("state"));
                vods.put(vod);
            }
            result.put("list", vods);
        } catch (Exception e) {

        }
        return result.toString();
    }

    public void init(Context context, String str) {
        try {
            this.n8 = "/api.php/Chengcheng1/vod";
            this.token = OkHttpUtil.string("293token.txt", (Map) null);
            SpiderDebug.log("By:Qiuyi");
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
    }
}
