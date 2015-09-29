package heartbeatmonitor.project.edu.heartbeatmonitor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;


public class AlarmList extends Activity {

    ArrayList<HashMap<String, String>> alarmArrList ;
    String deleteAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_newalarm) {

            Intent intent = new Intent(this,AlarmConfiguration.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public StringBuilder getSQLMessage(){
        try
        {
            StringBuilder sql = new StringBuilder();
            SQLiteDatabase db = openOrCreateDatabase("AlarmList.db", Context.MODE_PRIVATE,null);
            Cursor cursor = db.rawQuery("SELECT * FROM ReminderMessage", null);
            if(cursor!= null){
                if (cursor.moveToFirst()) {
                    do {
                        String alarmlist = cursor.getString(cursor.getColumnIndex("Category"));
                    }while(cursor.moveToNext());
                }
            }
            return sql;
        }
        catch(Exception e)
        {
            return new StringBuilder("No SQL");
        }
    }



    View temp;

    @Override
    protected void onResume(){

        super.onResume();

        ListView list = (ListView) findViewById(R.id.reminder);
        alarmArrList = new ArrayList<HashMap<String, String>>();
        try
        {
            HashMap<String, String> map= new HashMap<String, String>();
            //   map.put("category", "Reminders");
            alarmArrList.add(map);
            SQLiteDatabase db = openOrCreateDatabase("AlarmList.db", Context.MODE_PRIVATE,null);
            Cursor cursor = db.rawQuery("SELECT * FROM ReminderMessage", null);
            if(cursor!= null){
                if (cursor.moveToFirst()) {
                    do {

                        String alarmlist = cursor.getString(cursor.getColumnIndex("Category"));
                        String id = cursor.getString(cursor.getColumnIndex("ID"));
                        map = new HashMap<String, String>();
                        map.put("id",id);
                        map.put("category", alarmlist);

                        alarmArrList.add(map);
                    }while(cursor.moveToNext());
                }
            }
        }
        catch(Exception e)
        {

        }

        final SimpleAdapter mReminder = new SimpleAdapter(this, alarmArrList, R.layout.activity_alarm_list_entry,
                new String[] {"category"}, new int[] {R.id.category});
        list.setAdapter(mReminder);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                temp= view;
                deleteAlarm = alarmArrList.get(position).get("id");

                Button b = (Button)view.findViewById(R.id.deleteButton);
                b.setVisibility(View.VISIBLE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        SQLiteDatabase db = openOrCreateDatabase("AlarmList.db", Context.MODE_PRIVATE,null);
                        int i = db.delete("ReminderMessage", "ID" + "=" + deleteAlarm, null);
                        mReminder.notifyDataSetChanged();
                        if(i >0){
                            temp.setVisibility(View.INVISIBLE);
                        }
                    }
                });

            }
        });
    }

}
