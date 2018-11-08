package com.blogspot.blogsetyaaji.mytodolist.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blogspot.blogsetyaaji.mytodolist.R;
import com.blogspot.blogsetyaaji.mytodolist.model.Todo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.MyTodoViewHolder> {

    private Context context;
    private List<Todo> todos;

    public TodoAdapter(FragmentActivity activity, List<Todo> todoList) {
        context = activity;
        todos = todoList;
    }

    @NonNull
    @Override
    public MyTodoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.todo_row, viewGroup,
                false);
        return new MyTodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTodoViewHolder myTodoViewHolder, int i) {
        Todo todo = todos.get(i);

        myTodoViewHolder.txtNamaTodo.setText(todo.getNama());
        myTodoViewHolder.waktuTodo.setText(formatTanggal(todo.getWaktu()));

        if (todo.getKategori().equals("Done")) {
            myTodoViewHolder.dot.setTextColor(Color.BLUE);
        } else if (todo.getKategori().equals("Progress")) {
            myTodoViewHolder.dot.setTextColor(Color.GREEN);
        } else {
            myTodoViewHolder.dot.setTextColor(Color.YELLOW);
        }

        myTodoViewHolder.dot.setText(Html.fromHtml("&#8226;"));
    }

    private String formatTanggal(String waktu) {
        try {
            // membuat format default tanggal
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // mengubah waktu ke tipe date dengan format yang disiapkan tadi
            Date date = simpleDateFormat.parse(waktu);
            // buat format baru
            SimpleDateFormat formatAkhir = new SimpleDateFormat("MMM d");
            // terapkan format baru ke tanggal yang sudah dibuat
            return formatAkhir.format(date);
        } catch (ParseException e) {

        }

        return "";
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    public class MyTodoViewHolder extends RecyclerView.ViewHolder {

        TextView txtNamaTodo, waktuTodo, dot;

        public MyTodoViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNamaTodo = itemView.findViewById(R.id.txtNamaTodo);
            dot = itemView.findViewById(R.id.dot);
            waktuTodo = itemView.findViewById(R.id.waktuTodo);
        }
    }
}
