package com.blogspot.blogsetyaaji.mytodolist.Fragment;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blogspot.blogsetyaaji.mytodolist.R;

class MyMenuAdapter extends RecyclerView.Adapter<MyMenuViewHolder> {
    String[] menu;
    int[] gambar;
    Activity activity;

    public MyMenuAdapter(FragmentActivity activity, String[] menuApp, int[] menuGambar) {
        // memasukkan data dari parameter ke variable di dalam class
        menu = menuApp;
        gambar = menuGambar;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // memasang layout dan menghubungkan ke viewholder
        View view = LayoutInflater.from(activity).inflate(R.layout.menu_row, parent, false);
        return new MyMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyMenuViewHolder holder, int position) {
        // memasang data ke layout view holder
        holder.txtmenuitem.setText(menu[position]);
        holder.imgmenuitem.setImageResource(gambar[position]);
    }

    @Override
    public int getItemCount() {
        // menentukan total data yang tampil
        return menu.length;
    }
}
