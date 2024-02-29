package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<String> dataList;
    private ArrayList<String> datalist2;

    public MyAdapter(ArrayList<String> dataList, ArrayList<String> dataList2) {
        this.dataList = dataList;
        this.datalist2 = dataList2;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String data = dataList.get(position);
        holder.textView1.setText(data);
        if(datalist2 != null){
            String data2 = datalist2.get(position);
            holder.textView2.setText(data2);
        }else{
            holder.textView2.setText("");
        }

        // Aquí configuramos el clic en el elemento
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtén la posición del elemento clickeado
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    // Acciones al hacer clic en el elemento
                    String valor = dataList.get(adapterPosition);
                    // Puedes abrir una nueva actividad aquí si es necesario
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView1;
        public TextView textView2;

        public ViewHolder(View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
        }
    }
}
