package com.example.firoz.phonebookdatabase;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText e1,e2;
    Button save,clear,show, search;
    TextView tv;
    private static final String DATABASE_NAME="Phonebook";
    private static final String TABLE_NAME="contacts";
    SQLiteDatabase sqldb;
    String sqlquery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e1=(EditText)findViewById(R.id.editText);
        e2=(EditText)findViewById(R.id.editText2);
        tv=(TextView)findViewById(R.id.textView);
        save=(Button)findViewById(R.id.button);
        clear=(Button)findViewById(R.id.button2);
        show=(Button)findViewById(R.id.button3);
        search=(Button)findViewById(R.id.button4);




        sqldb=openOrCreateDatabase(DATABASE_NAME,SQLiteDatabase.CREATE_IF_NECESSARY,null);
        sqldb.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"(NAME TEXT,PHONE LONG)");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=e1.getText().toString();
                String phone=e2.getText().toString();
                sqlquery="insert into "+TABLE_NAME+" values('"+name+"','"+phone+"');";
                sqldb.execSQL(sqlquery);
                Toast.makeText(getApplicationContext(),"contact saved",Toast.LENGTH_LONG).show();

            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setText("");
                e2.setText("");
                e1.requestFocus();
                tv.setText("");

            }
        });
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //retreiving data using cursor
                Cursor c=sqldb.query(TABLE_NAME,null,null,null,null,null,null);
                if(c.moveToFirst())     //to set the cursor on the first row.
                {
                    while(c.isAfterLast()==false)
                    {
                        /*this method checks whether the cursor is pointing to the position after last row.
                        if the cursor is not pointing after the last row , keep on iterating the cursor
                        using moveToNext method.*/
                        tv.append("\n"+c.getString(0)+"\n"+c.getString(1));   //column index to retrieve specified column
                        c.moveToNext();    //it is used to set the cursor to the next row
                    }
                }
                //if cursor is not empty and not closed, close the cursor
                if(c!=null && !c.isClosed())
                {
                    c.close();
                }
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Search Contact");
                builder.setMessage("Type Name to search ");

                final EditText input = new EditText(MainActivity.this);
                builder.setView(input);


                //set Negative Button
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv.setText("You Clicked Cancel Button");


                    }
                });

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String data=input.getText().toString();
                        Cursor c=sqldb.query(TABLE_NAME,null,null,null,null,null,null);
                        if(c.moveToFirst())
                        {
                            while(c.isAfterLast()==false)
                            {
                                String contactName=c.getString(0);
                                String phoneNumber=c.getString(1);

                                if(data.equals(contactName))
                                {
                                    tv.setText("Name: "+contactName+"\n"+"Phone: "+phoneNumber);
                                    break;
                                }
                                c.moveToNext();    //it is used to set the cursor to the next row
                            }
                        }

                    }
                });
                //display the dialog
                builder.show();
            }
        });


    }
}
