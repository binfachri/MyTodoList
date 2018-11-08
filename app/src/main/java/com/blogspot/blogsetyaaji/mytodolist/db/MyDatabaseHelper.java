package com.blogspot.blogsetyaaji.mytodolist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.blogspot.blogsetyaaji.mytodolist.model.Todo;

import java.util.ArrayList;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    // nama database
    private static final String NAMA_DATABASE = "db_todo";
    // versi database
    private static final int VERSI_DATABASE = 1;

    // construcotor
    public MyDatabaseHelper(Context context) {
        super(context, NAMA_DATABASE, null, VERSI_DATABASE);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // buat tabel pertama kaliketika aplikasi diinstal
        sqLiteDatabase.execSQL(Todo.BUAT_TABEL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // jika table ada sebelumnya, maka hapus table yang telah ada
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Todo.NAMA_TABEL);

        // buat tabel lagi
        onCreate(sqLiteDatabase);
    }

    // fungsi untuk menyimpan data
    public long simpanData(String nama, String deskripsi, String kategori) {
        // akses database untuk menambah data
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        // komponen untuk menyimpan value data
        ContentValues contentValues = new ContentValues();
        // masukkan data ke dalam contentValue
        contentValues.put(Todo.COLUMN_NAMA, nama);
        contentValues.put(Todo.COLUMN_DESKRIPSI, deskripsi);
        contentValues.put(Todo.COLUMN_KATEGORI, kategori);

        // masukkan data row
        long id = sqLiteDatabase.insert(Todo.NAMA_TABEL, null,
                contentValues);

        // tutup database
        sqLiteDatabase.close();

        // keluarkan hasil id dari proses menyimpan data
        return id;
    }

    // mengambil 1 row data berdasarkan id
    public Todo getTodo(long id) {
        // minta akses mengambil data
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        // posisikaan cursor tabel ke data yang dituju
        Cursor cursor = sqLiteDatabase.query(Todo.NAMA_TABEL,
                new String[]{Todo.COLUMN_ID, Todo.COLUMN_NAMA, Todo.COLUMN_DESKRIPSI,
                        Todo.COLUMN_WAKTU, Todo.COLUMN_KATEGORI},
                Todo.COLUMN_ID + " ? =",
                new String[]{String.valueOf(id)}, null, null, null,
                null);

        // posisikan data yang dipilih cursor ke paling atas/
        // posisikan cursor ke data
        if (cursor != null)
            cursor.moveToFirst();

        // data yang ditemukan cursor dimasukkan ke dalam model
        Todo todo = new Todo(
                cursor.getInt(cursor.getColumnIndex(Todo.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Todo.COLUMN_NAMA)),
                cursor.getString(cursor.getColumnIndex(Todo.COLUMN_DESKRIPSI)),
                cursor.getString(cursor.getColumnIndex(Todo.COLUMN_WAKTU)),
                cursor.getString(cursor.getColumnIndex(Todo.COLUMN_KATEGORI))
        );

        // hilngkan cursor beserta koneksi database
        cursor.close();
        // kembalikan data
        return todo;
    }

    // mengambil seluruh data
    public List<Todo> ambilSemuaData() {
        List<Todo> listTodo = new ArrayList<>();

        String query = "SELECT * FROM " + Todo.NAMA_TABEL + " WHERE " +
                Todo.COLUMN_KATEGORI + " = ?";

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{"Todo"});

        // masukkan data ke list
        if (cursor.moveToFirst()) {
            do {
                Todo todo = new Todo();
                todo.setId(cursor.getInt(cursor.getColumnIndex(Todo.COLUMN_ID)));
                todo.setNama(cursor.getString(cursor.getColumnIndex(Todo.COLUMN_NAMA)));
                todo.setDeskripsi(cursor.getString(cursor.getColumnIndex(Todo.COLUMN_DESKRIPSI)));
                todo.setWaktu(cursor.getString(cursor.getColumnIndex(Todo.COLUMN_WAKTU)));
                todo.setKategori(cursor.getString(cursor.getColumnIndex(Todo.COLUMN_KATEGORI)));

                listTodo.add(todo);
            } while (cursor.moveToNext());
        }

        // tutup koneksi, hilangkan cursor
        sqLiteDatabase.close();

        return listTodo;
    }

    public int ambilJumlahData() {
        String query = "SELECT * FROM " + Todo.NAMA_TABEL + " WHERE " +
                Todo.COLUMN_KATEGORI + " = ?";

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{"Todo"});
        // tampung jumlah seluruh data di variable
        int jumlah = cursor.getCount();
        // tutup database
        cursor.close();
        // hasilkan jumlah data
        return jumlah;
    }

    public int updateTodo(Todo todo) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Todo.COLUMN_NAMA, todo.getNama());
        values.put(Todo.COLUMN_DESKRIPSI, todo.getDeskripsi());
        values.put(Todo.COLUMN_KATEGORI, todo.getKategori());

        return sqLiteDatabase.update(Todo.NAMA_TABEL, values, Todo.COLUMN_ID + " = ? ",
                new String[]{String.valueOf(todo.getId())});
    }

    public void hapusData(Todo todo) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(Todo.NAMA_TABEL, Todo.COLUMN_ID + " = ? ",
                new String[]{String.valueOf(todo.getId())});
        sqLiteDatabase.close();
    }

    public List<Todo> ambilSemuaDataDone() {
        List<Todo> listTodo = new ArrayList<>();

        // Query mengambil semua data
        String query = "SELECT * FROM " + Todo.NAMA_TABEL + " WHERE " +
                Todo.COLUMN_KATEGORI + " = ?";

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{"Done"});
        // masukkan data ke list
        if (cursor.moveToFirst()) {
            do {
                Todo todo = new Todo();
                todo.setId(cursor.getInt(cursor.getColumnIndex(Todo.COLUMN_ID)));
                todo.setNama(cursor.getString(cursor.getColumnIndex(Todo.COLUMN_NAMA)));
                todo.setDeskripsi(cursor.getString(cursor.getColumnIndex(Todo.COLUMN_DESKRIPSI)));
                todo.setWaktu(cursor.getString(cursor.getColumnIndex(Todo.COLUMN_WAKTU)));
                todo.setKategori(cursor.getString(cursor.getColumnIndex(Todo.COLUMN_KATEGORI)));

                listTodo.add(todo);
            } while (cursor.moveToNext());
        }

        // tutup koneksi, hilangkan cursor
        sqLiteDatabase.close();

        return listTodo;
    }

    public int ambilJumlahDataDone() {
        // query mengambil seluruh data
        // Query mengambil semua data
        String query = "SELECT * FROM " + Todo.NAMA_TABEL + " WHERE " +
                Todo.COLUMN_KATEGORI + " = ?";

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{"Done"});
        // tampung jumlah seluruh data di variable
        int jumlah = cursor.getCount();
        // tutup database
        cursor.close();
        // hasilkan jumlah data
        return jumlah;
    }
}
