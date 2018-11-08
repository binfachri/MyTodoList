package com.blogspot.blogsetyaaji.mytodolist.Fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.blogsetyaaji.mytodolist.R;

// memasukkan komponen layout ke adapter
class MyMenuViewHolder extends RecyclerView.ViewHolder {
    TextView txtmenuitem;
    ImageView imgmenuitem;

    public MyMenuViewHolder(View itemView) {
        super(itemView);
        txtmenuitem = (TextView) itemView.findViewById(R.id.txtmenuitem);
        imgmenuitem = (ImageView) itemView.findViewById(R.id.imgmenuitem);
    }
}
