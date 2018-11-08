package com.blogspot.blogsetyaaji.mytodolist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.blogsetyaaji.mytodolist.Fragment.DoneFragment;
import com.blogspot.blogsetyaaji.mytodolist.Fragment.MenuFragment;
import com.blogspot.blogsetyaaji.mytodolist.Fragment.ProgressFragment;
import com.blogspot.blogsetyaaji.mytodolist.Fragment.TodoFragment;
import com.blogspot.blogsetyaaji.mytodolist.db.MyDatabaseHelper;
import com.blogspot.blogsetyaaji.mytodolist.model.Todo;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MyDatabaseHelper myDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tampilDialogTodo();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        myDatabaseHelper = new MyDatabaseHelper(this);

        MenuFragment menuFragment = new MenuFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, menuFragment).commit();

    }

    private void tampilDialogTodo() {
        // tempelkan layout ke dalam view
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.dialog_todo, null);
        // pasang view ke dalam alertdialog
        AlertDialog.Builder alertDialogInput = new AlertDialog.Builder(this);
        alertDialogInput.setView(view);
        // inisialisasi komponen dalam dialog
        final EditText edInNama = view.findViewById(R.id.edInNama);
        final EditText edInDesk = view.findViewById(R.id.edInDesk);
        TextView txtInTitle = view.findViewById(R.id.txtInTitle);
        // mengatur judul dialog
        txtInTitle.setText(R.string.newtodo);
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
        final AlertDialog alertDialog = alertDialogInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // cek apakah kode kosong
                if (TextUtils.isEmpty(edInNama.getText().toString())) {
                    edInNama.setError(getString(R.string.nama_kosong));
                    edInNama.requestFocus();
                } else if (TextUtils.isEmpty(edInDesk.getText().toString())) {
                    edInDesk.setError(getString(R.string.desc_kosong));
                    edInDesk.requestFocus();
                } else {
                    // simpan data ke database
                    // simpna ke database dan dapatkan id data yang baru saja disimpan
                    myDatabaseHelper.simpanData(edInNama.getText().toString(),
                            edInDesk.getText().toString(), "Todo");
                    alertDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            MenuFragment menuFragment = new MenuFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, menuFragment).commit();
        } else if (id == R.id.nav_todo) {
            TodoFragment todoFragment = new TodoFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, todoFragment).commit();
        } else if (id == R.id.nav_progress) {
            ProgressFragment progressFragment = new ProgressFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, progressFragment).commit();
        } else if (id == R.id.nav_done) {
            DoneFragment doneFragment = new DoneFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, doneFragment).commit();
        } else if (id == R.id.nav_feedback) {
            showFeedbackDialog();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFeedbackDialog() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.feedback_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputFeedback = view.findViewById(R.id.edFeedback);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(getString(R.string.lbl_title));

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputFeedback.getText().toString())) {
                    Toast.makeText(MainActivity.this, R.string.hint_enter_feedback, Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // kirim email menggunakan intent untuk membuka aplikasi email
                Intent intent = new Intent(Intent.ACTION_SEND);
                // buat intent meembawa data pada aplikasi
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"setyaaji07@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback My TodoList App");
                intent.putExtra(Intent.EXTRA_TEXT, inputFeedback.getText().toString());
                // mengubah tipe dari email
                intent.setType("message/rfc822");
                // mulai mengirim email ke aplikasi gmail
                startActivity(Intent.createChooser(intent, "Pilih Aplikasi"));
            }
        });
    }
}
