package gowucheerji.bwie.com.gowucheerji.model;

import java.util.HashMap;

import gowucheerji.bwie.com.gowucheerji.okhttp.OkHttp3Utils;
import okhttp3.Callback;

/**
 * Created by CZ on 2017/12/15.
 */
public class MyGoWuCheModel implements GoWuCheModel {
    @Override
    public void get(String uid, Callback callback) {
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", uid);
        OkHttp3Utils.doPost("http://120.27.23.105/product/getCarts?source=android", map, callback);
    }
}
