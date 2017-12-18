package gowucheerji.bwie.com.gowucheerji.persenter;

import com.google.gson.Gson;

import java.io.IOException;

import gowucheerji.bwie.com.gowucheerji.bean.ShopBean;
import gowucheerji.bwie.com.gowucheerji.model.MyGoWuCheModel;
import gowucheerji.bwie.com.gowucheerji.okhttp.OnUiCallback;
import gowucheerji.bwie.com.gowucheerji.view.GoWuCheView;
import okhttp3.Call;

/**
 * Created by CZ on 2017/12/15.
 */
public class GoWuChePersenter {
    GoWuCheView view;
    private final MyGoWuCheModel model;

    public GoWuChePersenter(GoWuCheView view) {
        this.view = view;
        model = new MyGoWuCheModel();
    }

    public void getData(String uid) {
        model.get(uid, new OnUiCallback() {
            @Override
            public void onFailed(Call call, IOException e) {
                if (view != null) {
                    view.failuer(e);
                }
            }

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                ShopBean bean = gson.fromJson(result, ShopBean.class);
                view.success(bean);
            }
        });
    }

    public void saaa() {
        this.view = null;
    }
}
