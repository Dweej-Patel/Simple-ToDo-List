package com.example.dweejpatel.todo;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readFileItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView = findViewById(R.id.listView);
        listView.setAdapter(itemsAdapter);

        setupListViewListener();

    }

    public void addItems(View v) {
        EditText textEditor = findViewById(R.id.lvEditText);
        String text = textEditor.getText().toString();
        items.add(text);
        itemsAdapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(), "Item Added", Toast.LENGTH_SHORT).show();

        writeItems();

        textEditor.setText("");
    }

    private void setupListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Do to want to remove \"" + items.get(position) + "\"?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                items.remove(position);
                                itemsAdapter.notifyDataSetChanged();
                                writeItems();
                                Toast.makeText(getApplicationContext(), "Item Removed", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });
    }

    private File getFile() {
        return new File(getFilesDir(), "simpleToDo.txt");
    }

    private void readFileItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            items = new ArrayList<>();
        }
    }

    private void writeItems() {
        try {
            FileUtils.writeLines(getFile(), items);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Data could not be written!", Toast.LENGTH_SHORT).show();
        }
    }
}
