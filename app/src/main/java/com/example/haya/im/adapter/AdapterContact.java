package com.example.haya.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.haya.im.R;

import java.util.HashMap;
import java.util.LinkedList;

//implements Filterable
public class AdapterContact extends BaseExpandableListAdapter {
    public static Boolean visiblityCheckBox = false;
    private LayoutInflater layoutInflater;
    private Context context;
    private HashMap<String, LinkedList<String>> infos;
    private String[] group;

    public AdapterContact(Context context, HashMap<String, LinkedList<String>> infos) {
        this.context = context;
        this.infos = infos;
        this.group = new String[infos.keySet().size()];
        infos.keySet().toArray(group);
        layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return infos.get(group[groupPosition]).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.contact_list_item, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.avatar = convertView.findViewById(R.id.id_avatar);
            childViewHolder.name = convertView.findViewById(R.id.id_name);
            childViewHolder.checkBox = convertView.findViewById(R.id.id_checkbox);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        String child = (String) getChild(groupPosition, childPosition);
        childViewHolder.name.setText(child);
//        if (cur.getAvatar()!=null)
//            childViewHolder.avatar.setImageBitmap(cur.getAvatar());
        if (visiblityCheckBox) childViewHolder.checkBox.setVisibility(View.VISIBLE);
        else childViewHolder.checkBox.setVisibility(View.INVISIBLE);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return infos.get(group[groupPosition]).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group[groupPosition];
    }

    @Override
    public int getGroupCount() {
        return infos.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.contact_list_group_item, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.groupName = convertView.findViewById(R.id.label_expand_group);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.groupName.setText(group[groupPosition]);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }

    static class GroupViewHolder {
        TextView groupName;
    }

    static class ChildViewHolder {
        ImageView avatar;
        TextView name;
        CheckBox checkBox;
    }

}
