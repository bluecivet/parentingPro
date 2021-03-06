package com.cmpt276.teal.parentingpro.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmpt276.teal.parentingpro.ChildTab;
import com.cmpt276.teal.parentingpro.R;


/**
 * the class a adapter for the list view in config activity
 */
public class ChildrenAdapter extends BaseAdapter {

    private Context mContext;
    private Activity activity ;
    private ChildManagerUI childManager;


    public ChildrenAdapter(Context context, ChildManagerUI childManager, Activity activity) {
        this.mContext = context;
        this.childManager = childManager;
        this.activity = activity;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        childManager.saveToLocal(mContext);
    }


    public void notifyDataSetChanged(boolean isSaving){
        super.notifyDataSetChanged();
        if(isSaving){
            childManager.saveToLocal(mContext);
        }
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
        final ChildUI childUI = childManager.getChild(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.child_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.profile = convertView.findViewById(R.id.imageView);
            viewHolder.itemTv = convertView.findViewById(R.id.tv_name);
            viewHolder.editBtn = convertView.findViewById(R.id.btn_edit);
            viewHolder.delBtn = convertView.findViewById(R.id.btn_del);

            viewHolder.itemTv.setText(childManager.getChild(position).getName());
            viewHolder.profile.setImageBitmap(childUI.getProfile());
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.itemTv.setText(childManager.getChild(position).getName());
            viewHolder.profile.setImageBitmap(childUI.getProfile());
        }

        final ViewHolder finalViewHolder = viewHolder;

        viewHolder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChildTab.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("name", childManager.getChild(position).getName());
                intent.putExtra("pos",position);
                activity.startActivityForResult(intent, 1);
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
        ImageView profile;
        EditText itemEv;
        Button editBtn;
        Button delBtn;
    }
}
