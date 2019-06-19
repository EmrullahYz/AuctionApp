package com.examples.firebaseauth;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.examples.firebaseauth.model.Antika;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapterEkspertiz extends RecyclerView.Adapter<MyAdapterEkspertiz.MyViewHolder> {
    private Context mContext;
    private List<Antika> antikaList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView baslik,kalanSure,teklif,id;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);

            image = view.findViewById(R.id.antiqueListItemPic);
            baslik = view.findViewById(R.id.antiqueListBaslik);
            kalanSure = view.findViewById(R.id.antiqueListSure);
            teklif = view.findViewById(R.id.antiqueListTeklif);

        }
    }


    public MyAdapterEkspertiz(Context context, List<Antika> antikaListem) {
        antikaList = antikaListem;
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.antika_list_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Antika antika = antikaList.get(position);
        Picasso.get().load(antika.getUrl1())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.image);
        holder.baslik.setText(""+ antika.getBaslik());
        holder.teklif.setText("Son Teklif :"+ antika.getMinFiyat().toString());
        if (antika.isExpertiz()){
            holder.kalanSure.setText(" ONAYLANDI." );
            holder.kalanSure.setTextColor(ContextCompat.getColor(mContext,R.color.colorGreen));
        }else {
            holder.kalanSure.setText(" ONAYLANMADI.");
            holder.kalanSure.setTextColor(ContextCompat.getColor(mContext,R.color.colorRed));
        }

    }

    @Override
    public int getItemCount() {
        return antikaList.size();
    }


}