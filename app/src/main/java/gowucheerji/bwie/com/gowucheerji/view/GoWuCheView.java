package gowucheerji.bwie.com.gowucheerji.view;


import gowucheerji.bwie.com.gowucheerji.bean.ShopBean;

/**
 * Created by CZ on 2017/12/15.
 */
public interface GoWuCheView {
    public void success(ShopBean bean);

    public void failuer(Exception e);
}
