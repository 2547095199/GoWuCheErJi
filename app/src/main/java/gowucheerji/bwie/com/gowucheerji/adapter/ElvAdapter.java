package gowucheerji.bwie.com.gowucheerji.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import gowucheerji.bwie.com.gowucheerji.MainActivity;
import gowucheerji.bwie.com.gowucheerji.R;
import gowucheerji.bwie.com.gowucheerji.bean.PriceAndCount;
import gowucheerji.bwie.com.gowucheerji.bean.ShopBean;

/**
 * Date:2017/12/16
 * time:19:22
 * author:HuangZhangpeng
 */
public class ElvAdapter extends BaseExpandableListAdapter {
    private final LayoutInflater inflater;
    private Context context;
    private List<ShopBean.Data> group;
    private List<List<ShopBean.Data.List>> child;


    public ElvAdapter(Context context, List<ShopBean.Data> group, List<List<ShopBean.Data.List>> child) {
        this.context = context;
        this.group = group;
        this.child = child;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return group.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return child.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return child.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view;
        Log.i("============", "getGroupView: ");
        final GroupViewHolder holder;
        if (convertView == null) {
            view = inflater.inflate(R.layout.elv_group, null);
            holder = new GroupViewHolder();
            holder.tv = view.findViewById(R.id.tv_group);
            holder.cbGroup = view.findViewById(R.id.cb_group);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (GroupViewHolder) view.getTag();
        }
        final ShopBean.Data data = group.get(groupPosition);
        holder.tv.setText(data.getSellerName());
        holder.cbGroup.setChecked(data.isCheck());
        holder.cbGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //需要改变三个CheckBox的状态值
                //1.一级列表的CheckBox的状态值
                data.setCheck(holder.cbGroup.isChecked());
                //二级列表的CheckBox的状态值
                setChildrenCb(groupPosition, holder.cbGroup.isChecked());
                //全选的CheckBox状态值
                ((MainActivity) context).setAllChecked(isAllGroupCbChecked());
                //计算钱和数量并显示
                setPriceAndCount();
                //刷新界面
                notifyDataSetChanged();
            }
        });
        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        View view1;
        final ChildViewHolder holder1;
        if (convertView == null) {
            view1 = inflater.inflate(R.layout.elv_child, null);
            holder1 = new ChildViewHolder();
            holder1.iv = view1.findViewById(R.id.iv);
            holder1.tvTitle = view1.findViewById(R.id.tv_title);
            holder1.tvSubhead = view1.findViewById(R.id.tvSubhead);
            holder1.tvPrice = view1.findViewById(R.id.tvPrice);
            holder1.cbChild = view1.findViewById(R.id.cb_child);
            holder1.btDel = view1.findViewById(R.id.btDel);
            holder1.tvNum = view1.findViewById(R.id.tvNum);
            holder1.ivDel = view1.findViewById(R.id.ivDel);
            holder1.ivAdd = view1.findViewById(R.id.ivAdd);
            view1.setTag(holder1);

        } else {
            view1 = convertView;
            holder1 = (ChildViewHolder) view1.getTag();
        }
        final ShopBean.Data.List listBean = child.get(groupPosition).get(childPosition);
        String images = listBean.getImages();
        Glide.with(context).load(images.split("\\|")[0]).into(holder1.iv);
        holder1.tvTitle.setText(listBean.getTitle());
        holder1.cbChild.setChecked(child.get(groupPosition).get(childPosition).isChecked());
        holder1.tvSubhead.setText(listBean.getSubhead());
        holder1.tvPrice.setText(listBean.getPrice() + "元");
        holder1.tvNum.setText(listBean.getCount() + "");
        holder1.cbChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //需要改变三个CheckBox的状态值


                //2.二级列表的checkbox状态值
                listBean.setChecked(holder1.cbChild.isChecked());
                //1.一级列表的CheckBox的状态值
                group.get(groupPosition).setCheck(isAllChildCbChecked(groupPosition));
                //3.全选的CheckBox状态值
                ((MainActivity) context).setAllChecked(isAllGroupCbChecked());
                //计算钱和数量并显示
                setPriceAndCount();
                //刷新界面
                notifyDataSetChanged();
            }
        });
        //加一件商品
        holder1.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = listBean.getCount();
                count++;
                //改变JavaBean里的状态值
                listBean.setCount(count);
                //计算钱和数量并显示
                setPriceAndCount();
                //刷新列表
                notifyDataSetChanged();
            }
        });

        //减少一件商品
        holder1.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = listBean.getCount();
                if (count <= 1) {
                    count = 1;
                } else {
                    count--;
                }
                //改变JavaBean里的状态值
                listBean.setCount(count);
                //计算钱和数量并显示
                setPriceAndCount();
                //刷新列表
                notifyDataSetChanged();
            }
        });
        //删除
        holder1.btDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //其时就是删除集合
                List<ShopBean.Data.List> listBeans = child.get(groupPosition);
                if (listBeans.size() > 0) {
                    listBeans.remove(childPosition);
                }
                if (listBeans.size() == 0) {
                    child.remove(groupPosition);
                    group.remove(groupPosition);
                }
                //计算钱和数量并显示
                setPriceAndCount();
                //改变全选状态
                ((MainActivity) context).setAllChecked(isAllGroupCbChecked());
                //刷新列表
                notifyDataSetChanged();
            }
        });
        return view1;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    /**
     * 设置一级列表对应的二级列表的CheckBox状态
     */
    private void setChildrenCb(int groupPosition, boolean bool) {
        List<ShopBean.Data.List> listBeans = child.get(groupPosition);
        for (int i = 0; i < listBeans.size(); i++) {
            listBeans.get(i).setChecked(bool);
        }

    }

    /**
     * 判断一级列表CheckBox状态
     */
    private boolean isAllGroupCbChecked() {
        if (group.size() == 0) {
            return false;
        }
        for (int i = 0; i < group.size(); i++) {
            if (!group.get(i).isCheck()) {
                return false;
            }
        }
        return true;
    }


    /**
     * 判断二级列表CheckBox的状态
     */
    private boolean isAllChildCbChecked(int groupPosition) {
        List<ShopBean.Data.List> listBeans = child.get(groupPosition);
        for (int i = 0; i < listBeans.size(); i++) {
            if (!listBeans.get(i).isChecked()) {
                return false;
            }
        }
        return true;

    }

    /**
     * 设置钱和数量
     */
    private void setPriceAndCount(){
        ((MainActivity)context).setPriceAndCount(compute());
    }

    /**
     * 计算钱和数量
     */
    private PriceAndCount compute(){
        double price = 0;
        int count = 0;
        for (int i = 0; i < group.size(); i++) {
            List<ShopBean.Data.List> listBeans = child.get(i);
            for (int j = 0; j <listBeans.size() ; j++) {
                if(listBeans.get(j).isChecked()){
                    price+=listBeans.get(j).getPrice()*listBeans.get(j).getCount();
                    count+=listBeans.get(j).getCount();
                }
            }
        }
        return new PriceAndCount(price,count);
    }

    /**
     * 全选或者全不选
     */
    public void AllOrNone(boolean bool){
        for (int i = 0; i < group.size(); i++) {
            group.get(i).setCheck(bool);
            setChildrenCb(i,bool);
        }
        setPriceAndCount();
        notifyDataSetChanged();
    }

    class GroupViewHolder {
        TextView tv;
        CheckBox cbGroup;
    }

    class ChildViewHolder {
        ImageView iv;
        TextView tvTitle;
        TextView tvSubhead;
        TextView tvPrice;
        CheckBox cbChild;
        TextView btDel;
        TextView tvNum;
        ImageView ivDel;
        ImageView ivAdd;
    }

}
