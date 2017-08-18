package cn.edu.lzu.library.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.lzu.library.R;

/**
 * Created by chris on 2017/1/19.
 */
public class MyRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> list;

    public MyRvAdapter() {
        super();
        list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("我是堂前第" + (i+1)+"只小猫咪                      ");
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutItem = R.layout.item;
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutItem, null);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String info = list.get(position);
        MyViewHolder hd = (MyViewHolder) holder;
        hd.infos.setText("1.2.6 \n "+(position+1)+"分"+" \n"+info);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.infos)
        TextView infos;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}