package gowucheerji.bwie.com.gowucheerji;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import gowucheerji.bwie.com.gowucheerji.adapter.ElvAdapter;
import gowucheerji.bwie.com.gowucheerji.bean.PriceAndCount;
import gowucheerji.bwie.com.gowucheerji.bean.ShopBean;
import gowucheerji.bwie.com.gowucheerji.persenter.GoWuChePersenter;
import gowucheerji.bwie.com.gowucheerji.view.GoWuCheView;

public class MainActivity extends AppCompatActivity implements GoWuCheView {
    private GoWuChePersenter persenter;
    private ExpandableListView mElv;
    /**
     * 全选
     */
    private CheckBox mCb;
    /**
     * 合计:
     */
    private TextView mTvTotal;
    /**
     * 去结算(0)
     */
    private TextView mTvCount;
    private ElvAdapter elvAdapter;

    List<ShopBean.Data> group = new ArrayList<>();
    List<List<ShopBean.Data.List>> child = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        persenter = new GoWuChePersenter(this);
        persenter.getData("99");
        mCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elvAdapter.AllOrNone(mCb.isChecked());
            }
        });
    }

    private void initView() {
        mElv = (ExpandableListView) findViewById(R.id.elv);
        mCb = (CheckBox) findViewById(R.id.cb);
        mTvTotal = (TextView) findViewById(R.id.tvTotal);
        mTvCount = (TextView) findViewById(R.id.tvCount);
    }


    public void setPriceAndCount(PriceAndCount priceAndCount) {
        mTvTotal.setText("合计：" + priceAndCount.getPrice());
        mTvCount.setText("去结算（" + priceAndCount.getCount() + ")");
    }

    public void setAllChecked(boolean bool) {
        mCb.setChecked(bool);
    }

    @Override
    public void success(ShopBean bean) {
        for (int i = 0; i < bean.getData().size(); i++) {
            group.add(bean.getData().get(i));
        }
        for (int i = 0; i < group.size(); i++) {
            child.add(bean.getData().get(i).getList());
        }
        elvAdapter = new ElvAdapter(this, group, child);
        mElv.setGroupIndicator(null);
        mElv.setAdapter(elvAdapter);
        for (int i = 0; i < group.size(); i++) {
            mElv.expandGroup(i);
        }
    }

    @Override
    public void failuer(Exception e) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        persenter.saaa();
    }
}
