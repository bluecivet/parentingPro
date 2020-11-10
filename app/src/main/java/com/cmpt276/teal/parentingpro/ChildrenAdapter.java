package com.cmpt276.teal.parentingpro;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cmpt276.teal.parentingpro.model.ChildManager;

public class ChildrenAdapter extends BaseAdapter {

    private Context mContext;

    private ChildManager childManager;

    public ChildrenAdapter(Context context, ChildManager childManager) {
        this.mContext = context;
        this.childManager = childManager;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        childManager.saveToLocal(mContext);
    }

    @Override
    public int getCount() {
        // return names.size();
        return childManager.length();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.child_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.itemEv = convertView.findViewById(R.id.edit_name);
            viewHolder.itemTv = convertView.findViewById(R.id.tv_name);
            viewHolder.editBtn = convertView.findViewById(R.id.btn_edit);
            viewHolder.delBtn = convertView.findViewById(R.id.btn_del);

            viewHolder.itemEv.setText(childManager.getChild(position).getName());
            viewHolder.itemTv.setText(childManager.getChild(position).getName());

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.itemEv.setText(childManager.getChild(position).getName());
            viewHolder.itemTv.setText(childManager.getChild(position).getName());
        }

        final ViewHolder finalViewHolder = viewHolder;

        viewHolder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalViewHolder.itemTv.getVisibility() == View.VISIBLE){
                    finalViewHolder.itemTv.setVisibility(View.INVISIBLE);
                    finalViewHolder.itemEv.setVisibility(View.VISIBLE);
                    finalViewHolder.editBtn.setText(R.string.save_button_text);
                }else{
                    if(finalViewHolder.itemEv.getText().toString().length() == 0){
                        return;
                    }
                    finalViewHolder.itemTv.setVisibility(View.VISIBLE);
                    finalViewHolder.itemEv.setVisibility(View.INVISIBLE);
                    Log.e("TAG", finalViewHolder.itemEv.getText().toString());
                    finalViewHolder.editBtn.setText(R.string.edit_button_text);
                    childManager.getChild(position).setName(finalViewHolder.itemEv.getText().toString());
                    ChildrenAdapter.this.notifyDataSetChanged();
                }
            }
        });

        viewHolder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childManager.remove(position);
                ChildrenAdapter.this.notifyDataSetChanged();
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView itemTv;
        EditText itemEv;
        Button editBtn;
        Button delBtn;
    }
}
