package com.Azhara.tiketsaya;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TiketAdapter extends RecyclerView.Adapter<TiketAdapter.TiketViewHolder> {

    Context context;
    ArrayList<MyTicket> list;

    public TiketAdapter(Context context, ArrayList<MyTicket> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TiketAdapter.TiketViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TiketViewHolder(LayoutInflater.from(context).inflate(R.layout.item_tiket, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TiketAdapter.TiketViewHolder tiketViewHolder, int i) {
        MyTicket tiket = list.get(i);
        tiketViewHolder.namaWisata.setText(tiket.getNama_wisata());
        tiketViewHolder.tempatWisata.setText(tiket.getLokasi());
        tiketViewHolder.jmlTiket.setText(tiket.getJumlah_tiket());

        final String getNoWisata = tiket.getNoWisata();

        tiketViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyTicketDetail_Activity.class);
                intent.putExtra("nama_tiket", getNoWisata);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TiketViewHolder extends RecyclerView.ViewHolder {

        private TextView namaWisata, tempatWisata, jmlTiket;

        TiketViewHolder(@NonNull View itemView) {
            super(itemView);
            namaWisata = itemView.findViewById(R.id.tv_item_namawisata);
            tempatWisata = itemView.findViewById(R.id.tv_item_lokasiwisata);
            jmlTiket = itemView.findViewById(R.id.tv_item_jumlahtiket);
        }
    }
}
