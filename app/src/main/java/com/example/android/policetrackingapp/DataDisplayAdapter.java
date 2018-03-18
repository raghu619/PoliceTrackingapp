package com.example.android.policetrackingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by raghvendra on 9/3/18.
 */

public class DataDisplayAdapter  extends RecyclerView.Adapter<DataDisplayAdapter.DataDisplayAdapterViewHolder> {

    private final Context mContext;
    private ArrayList<Current_Location> arrayList;
    Current_Location mCurrentdata;
    private final DataDisplayAdapterOnClickHandler mClickHandler;


    public interface DataDisplayAdapterOnClickHandler {
        void onClick(Current_Location location);
    }

    @Override
    public DataDisplayAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.list_items,parent,false);
        view.setFocusable(true);

        return new DataDisplayAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataDisplayAdapterViewHolder holder, int position) {
         mCurrentdata=arrayList.get(position);
        holder.Emailview.setText(mCurrentdata.getMail_id());
        holder.Addressview.setText(mCurrentdata.getMaddress());
        holder.Timeview.setText(mCurrentdata.getMtime());
        holder.StatusView.setText(mCurrentdata.getMstatus());

    }

    @Override
    public int getItemCount() {
        if(arrayList==null)
        return 0;
        else{

            return arrayList.size();
        }
    }
    public DataDisplayAdapter(Context context,DataDisplayAdapterOnClickHandler mClickHandler){
        this.mClickHandler=mClickHandler;
        mContext=context;

    }

    class DataDisplayAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

      final  TextView Emailview;
      final  TextView Addressview;
      final  TextView Timeview;
      final  TextView StatusView;


        public DataDisplayAdapterViewHolder(View itemView) {
            super(itemView);
            Emailview=itemView.findViewById(R.id.emailtext);
            Addressview=itemView.findViewById(R.id.addresstext);
            Timeview=itemView.findViewById(R.id.timetext);
            StatusView=itemView.findViewById(R.id.statusview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
              int adapterPosition = getAdapterPosition();
            mCurrentdata=arrayList.get(adapterPosition);


            mClickHandler.onClick(mCurrentdata);


        }
    }

    public  void setUserdata(ArrayList<Current_Location> list){
        arrayList=list;
        notifyDataSetChanged();



    }

}
