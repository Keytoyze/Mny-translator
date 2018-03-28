package com.mnysqtp.com.mnyproject.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mnysqtp.com.mnyproject.Activity.WebViewActivity;
import com.mnysqtp.com.mnyproject.Info.DiscoveryInfo;
import com.mnysqtp.com.mnyproject.R;

import java.util.ArrayList;

public class DiscoveryAdapter extends RecyclerView.Adapter<DiscoveryAdapter.ViewHolder> {
    private ArrayList<DiscoveryInfo> mInfoList;
    private Context mContext;

    public DiscoveryAdapter(Context context, ArrayList<DiscoveryInfo> infoList) {
        mContext = context;
        mInfoList = infoList;
    }

    @Override
    public DiscoveryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DiscoveryAdapter.ViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.adapter_discovery, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String title = mInfoList.get(position).getTitle();
        String url = mInfoList.get(position).getUrl();
        String resource = mInfoList.get(position).getResource();
        holder.title.setText(title);
        holder.resource.setText(getResourceName(resource, url));
    }

    @Override
    public int getItemCount() {
        return mInfoList.size();
    }

    public static String getResourceName(String rawResource, String url) {
        String[] array = url.split("/");
        for (String s: array) {
            if (s.length() >= 7) {
                return rawResource + " " + s;
            }
        }
        return rawResource;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView resource;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.discovery_title);
            resource = itemView.findViewById(R.id.discovery_resource);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebViewActivity.sendIntent(mInfoList.get(getAdapterPosition()).getUrl(), mContext);
                }
            });
        }

    }
}
