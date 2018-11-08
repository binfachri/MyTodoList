package com.blogspot.blogsetyaaji.mytodolist.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.blogspot.blogsetyaaji.mytodolist.R;
import com.blogspot.blogsetyaaji.mytodolist.db.MyDatabaseHelper;
import com.blogspot.blogsetyaaji.mytodolist.model.Todo;
import com.blogspot.blogsetyaaji.mytodolist.utils.ClickListener;
import com.blogspot.blogsetyaaji.mytodolist.utils.RecyclerClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodoFragment extends Fragment {

    private RecyclerView lvTodo;
    private SwipeRefreshLayout todoSwipe;
    private List<Todo> todoList = new ArrayList<>();
    private LinearLayout todoKosong;
    private TodoAdapter todoAdapter;

    private MyDatabaseHelper myDatabaseHelper;

    public TodoFragment() {
        // Required empty public constructor
    }

    String nama;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //toodo toodo = new Toodo(1, "makan malam", "makan malam bersama teman", "besok", "todo");
        // tampikan nama
        //Log.d("NAMA", todoo.getNama());
        // ubah nama
        //toodo.setNama("bermain bola");
        // tampilkan nama
        //Log.d("NAMA", todoo.getNama());
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        lvTodo = view.findViewById(R.id.lvTodo);
        todoKosong = view.findViewById(R.id.todoKosong);
        todoSwipe = view.findViewById(R.id.todoSwipe);

        todoSwipe.setColorSchemeResources(R.color.pink, R.color.indigo, R.color.lime);

        myDatabaseHelper = new MyDatabaseHelper(getActivity());

        todoAdapter = new TodoAdapter(getActivity(), todoList);

        lvTodo.setLayoutManager(new LinearLayoutManager(getActivity()));
        lvTodo.setAdapter(todoAdapter);

        tampilData();

        todoSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tampilData();
            }
        });

        lvTodo.addOnItemTouchListener(new RecyclerClickListener(getActivity(),
                lvTodo, new ClickListener() {
            @Override
            public void OnClick(View view, int posisi) {

            }

            @Override
            public void onLongClick(View view, int posisi) {
                tampilDialogAksi(posisi);
            }
        }));

        aturTodoKosong();

        return view;
    }

    private void tampilData() {
        todoSwipe.setRefreshing(false);
        todoList.clear();
        // meletakkan semua data ke dalam list
        todoList.addAll(myDatabaseHelper.ambilSemuaData());
        todoAdapter.notifyDataSetChanged();
        aturTodoKosong();
    }

    private void tampilDialogAksi(final int posisi) {
        CharSequence teksTombol[] = new CharSequence[]{getString(R.string.edit), getString(R.string.delete)};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.option);
        builder.setItems(teksTombol, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    tampilEditDialog(todoList.get(posisi), posisi);
                } else {
                    hapusTodo(posisi);
                }
            }
        });
        builder.show();
    }

    private void hapusTodo(int posisi) {
        // hapus data
        myDatabaseHelper.hapusData(todoList.get(posisi));
        // hapus data dari model
        todoList.remove(posisi);
        // hapus data dari adapter
        todoAdapter.notifyItemRemoved(posisi);
        // tampilkna pesan data kosong ketika menghapus data terakhir
        aturTodoKosong();
    }

    private void tampilEditDialog(final Todo todo, final int posisi) {
        // tempelkan layout ke dalam view
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.dialog_edit, null);
        // pasang view ke dalam alertdialog
        android.support.v7.app.AlertDialog.Builder alertDialogInput = new android.support.v7.app.AlertDialog.Builder(getActivity());
        alertDialogInput.setView(view);
        // inisialisasi komponen dalam dialog
        final EditText edENama = view.findViewById(R.id.edENama);
        final EditText edEDesk = view.findViewById(R.id.edEDesk);
        TextView txtETitle = view.findViewById(R.id.txtETitle);
        Spinner spkategori = view.findViewById(R.id.spedit);
        // tampilkan data sekarang ke dalam komponen dialog
        edENama.setText(todo.getNama());
        edEDesk.setText(todo.getDeskripsi());
        // menampilkan kategori data di spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.itemkategori,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spkategori.setAdapter(adapter);
        if (todo.getKategori() != null) {
            int spinnerPosition = adapter.getPosition(todo.getKategori());
            spkategori.setSelection(spinnerPosition);
        }
        // mengambil item dari spinner untuk disimpan
        final String[] kategori = {null};
        spkategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                kategori[0] = getResources().getStringArray(R.array.itemkategori)[i];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // mengatur judul dialog
        txtETitle.setText(R.string.edittodo);
        // membuat tombol dialoog
        alertDialogInput
                .setCancelable(false)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        // memaasang tombol ke alert dialog
        final android.support.v7.app.AlertDialog alertDialog = alertDialogInput.create();
        alertDialog.show();

        alertDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // cek apakah kode kosong
                if (TextUtils.isEmpty(edENama.getText().toString())) {
                    edENama.setError(getString(R.string.nama_kosong));
                    edENama.requestFocus();
                } else if (TextUtils.isEmpty(edEDesk.getText().toString())) {
                    edEDesk.setError(getString(R.string.desc_kosong));
                    edEDesk.requestFocus();
                } else {
                    // simpan data ke database
                    // simpna ke database dan dapatkan id data yang baru saja disimpan
                    todo.setNama(edENama.getText().toString());
                    todo.setDeskripsi(edEDesk.getText().toString());
                    todo.setKategori(kategori[0]);
                    // proses update database
                    myDatabaseHelper.updateTodo(todo);
                    // mengatur posisi yang diubah
                    todoList.set(posisi, todo);

                    aturTodoKosong();
                    alertDialog.dismiss();
                }
            }
        });
    }

    private void aturTodoKosong() {
        // cek apakah data kosong
        // jika kosong maka tampilkan pesan kosong

        if (myDatabaseHelper.ambilJumlahData() > 0) {
            todoKosong.setVisibility(View.GONE);
        } else {
            todoKosong.setVisibility(View.VISIBLE);
        }
    }

}
