package com.examples.firebaseauth;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.examples.firebaseauth.model.Antika;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context mContext;
    private List<Antika> antikaList;
    private String kalan;
    public static final long HOUR = 3600 * 1000;

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

    public MyAdapter(Context context, List<Antika> antikaListem) {
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
        Antika antika = antikaList.get(position);
        Picasso.get().load(antika.getUrl1())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.image);
        holder.baslik.setText(antika.getBaslik());
        holder.teklif.setText("Son Teklif :"+ antika.getMinFiyat().toString()+" TL");

        if (antika.isAktif()){
            kalan = kalanZaman(Long.valueOf(antika.getSaat()));
            holder.kalanSure.setText(kalan);
            holder.kalanSure.setTextColor(ContextCompat.getColor(mContext,R.color.colorGreen));
        }else {
            holder.kalanSure.setText("ilan süresi bitmiştir..");
            holder.kalanSure.setTextColor(ContextCompat.getColor(mContext,R.color.colorRed));

        }

    }

    @Override
    public int getItemCount() {
        return antikaList.size();
    }

    public String kalanZaman(long timeNext){
        Date date =   new java.util.Date();
        Timestamp time1 = new Timestamp(date.getTime());
        long timeMilis = time1.getTime();

        Log.d(TAG, "kalanZaman: milis"+ timeMilis);
        long sonuc = timeNext - timeMilis;
        int seconds = (int) sonuc / 1000;
        int hours = seconds / 3600;
        Log.d(TAG, "kalanZaman: saat"+ hours);
        int minutes = (seconds % 3600 ) / 60;

        Log.d(TAG, "kalanZaman: minutes"+ minutes);
        seconds = (seconds % 3600) % 60 ;

        Log.d(TAG, "kalanZaman: seconds"+ seconds);
        if (hours<0 && minutes <0){
            return null;
        }
        return "İlanin bitmesine "+hours+" saat "+ minutes+" dakika kaldi.";
    }


}