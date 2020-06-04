package cn.edu.lzu.library.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.lzu.library.R;
import cn.edu.lzu.library.utils.UIUtils;

/**
 * 堂主模块的数据适配器
 * Created by chris on 2017/1/19.
 */
public class HallFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> titleList = new ArrayList<>();
    private List<Integer> picList = new ArrayList<>();
    private OnRecyclerViewItemClickListener mListener;

    public HallFragmentAdapter() {
        String[] hallTitle = UIUtils.getStringArray(R.array.hall_title);
        String[] hallPicName = UIUtils.getStringArray(R.array.hall_pic);
        titleList = Arrays.asList(hallTitle);
        for (String str : hallPicName) {
            int resourceId = UIUtils.getResourceId(str);
            picList.add(resourceId);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int itemId = R.layout.item_hall;
        View itemView = UIUtils.inflate(itemId);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder hd = (ViewHolder) holder;
        if (getItemCount() >= position) {
            String title = titleList.get(position);
            hd.tvTitle.setText(title);
            //根据位置设置图标
            int ivId = picList.get(position);
            hd.ivPic.setImageResource(ivId);
            if (mListener != null) {
                hd.llViewHolder.setOnClickListener(v -> mListener.onClick(position));
            }
        }
    }

    public void setItemClickListener(OnRecyclerViewItemClickListener listener) {
        mListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onClick(int position);
    }

    @Override
    public int getItemCount() {
        return titleList.size() == picList.size() ? titleList.size() : 0;
    }

    public void clear() {
        titleList.clear();
    }

    public void refresh(List<String> list) {
        this.titleList = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_pic)
        ImageView ivPic;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.ll_viewHolder)
        LinearLayout llViewHolder;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
