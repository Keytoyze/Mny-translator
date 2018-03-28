package com.mnysqtp.com.mnyproject.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mnysqtp.com.mnyproject.Info.DiscoveryInfo;
import com.mnysqtp.com.mnyproject.R;
import com.mnysqtp.com.mnyproject.Utils.DiscoveryAdapter;
import com.mnysqtp.com.mnyproject.Utils.DividerItemDecoration;

public class DiscoveryFragment extends android.app.Fragment{

    RecyclerView recyclerView;
    DiscoveryAdapter adapter;

    public DiscoveryFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discovery, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.discovery_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        adapter = new DiscoveryAdapter(getActivity(), DiscoveryInfo.fromXML(R.xml.discovery, getActivity()));
        recyclerView.setAdapter(adapter);
    }
}
