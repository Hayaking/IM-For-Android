package com.example.haya.im.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.haya.im.IMService;
import com.example.haya.im.R;
import com.example.haya.im.activity.InfoActivity;
import com.example.haya.im.adapter.AdapterContact;
import com.example.haya.im.bean.Historical;
import com.example.haya.im.client.Client;
import com.example.haya.im.client.Message;
import com.example.haya.im.utils.DBUtils;

import java.util.HashMap;
import java.util.LinkedList;


public class Contacts extends Fragment {
    public static Contacts newInstance(IMService.EchoServiceBinder args) {

        Bundle bundle = new Bundle();
        bundle.putBinder("binder", args);
        Contacts fragment = new Contacts();
        fragment.setArguments(bundle);
        return fragment;
    }
    private ExpandableListView exList;

    public Contacts() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Client client = Client.getInstance();
        //向服务器请求好友信息
        client.send(Message.TYPE.GET_CONTACT, null);
        IMService.EchoServiceBinder binder = (IMService.EchoServiceBinder) getArguments().getBinder("binder");
        HashMap<String, LinkedList<String>> contacts = binder.getContacts();
        AdapterContact adapter = new AdapterContact(getActivity(), contacts);
        exList = getActivity().findViewById(R.id.list);
        exList.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            String name = (String) adapter.getChild(groupPosition, childPosition);
            Intent intent = new Intent(getActivity(), InfoActivity.class);
            intent.putExtra("name", name);
            Historical item = new Historical(name, name, Historical.TYPE.NORMAL);
            item.setFlag(false);
            DBUtils.insertHistory(item);
            startActivity(intent);
            return false;
        });
        exList.setAdapter(adapter);
    }


}
