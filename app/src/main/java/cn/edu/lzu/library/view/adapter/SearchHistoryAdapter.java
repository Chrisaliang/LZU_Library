package cn.edu.lzu.library.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.edu.lzu.library.R;
import cn.edu.lzu.library.module.dao.history.History;
import cn.edu.lzu.library.module.dao.history.HistoryDAO;
import cn.edu.lzu.library.module.dao.history.HistorySQLiteOpenHelper;
import cn.edu.lzu.library.utils.UIUtils;

/**
 * 查询历史适配器
 * Created by chris on 2017/3/21.
 */

public class SearchHistoryAdapter extends BaseAdapter {

    private List<History> mList;
    private HistorySQLiteOpenHelper mHelper;

    public SearchHistoryAdapter(HistorySQLiteOpenHelper helper) {
        // 查询所有数据
        mHelper = helper;
        mList = HistoryDAO.queryAll(helper);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public History getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = UIUtils.inflate(R.layout.item_search_history);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        History item = getItem(position);
        holder.ivType.setImageResource(item.type == 1 ? R.mipmap.search_history_icon :
                R.mipmap.search_history_icon);
        holder.tvHistory.setText(item.url);
        holder.btnClearItem.setImageResource(R.mipmap.search_history_item_delete);
        holder.btnClearItem.setOnClickListener(v -> {
            HistoryDAO.delete(mHelper, getItem(position).id);
            // 刷新lv
            mList.remove(position);
            notifyDataSetChanged();
        });
        return convertView;
    }

    static class ViewHolder {
        ImageView ivType;
        TextView tvHistory;
        ImageButton btnClearItem;

        ViewHolder(View view) {
            ivType = view.findViewById(R.id.iv_type);
            tvHistory = view.findViewById(R.id.tv_history);
            btnClearItem = view.findViewById(R.id.btn_clear_item);
        }
    }
}
