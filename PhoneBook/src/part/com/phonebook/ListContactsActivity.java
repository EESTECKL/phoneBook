package part.com.phonebook;

import java.util.ArrayList;
import java.util.List;

import android.R.string;
import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListContactsActivity extends Activity {
	
	DBAdapter dba;
	ListView lv_names;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_contacts);
		
		dba = new DBAdapter(getApplicationContext());
		List<Contact> contacts = new ArrayList<Contact>();
		lv_names = (ListView)findViewById(R.id.lv_names);
		dba.open();
		
		contacts = dba.getAllContacts();
		
		dba.close();
		
		String[] names = getNamesArray(contacts);
		
		ArrayAdapter<String> name_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_cell, names);
		
		lv_names.setAdapter(name_adapter);
	}

	private String[] getNamesArray(List<Contact> contacts) {
		// TODO Auto-generated method stub
		
		String[] result = new String[contacts.size()];
		
		for(int i = 0; i < contacts.size();i++)
			result[i] = contacts.get(i).first_name + " " +contacts.get(i).last_name;
		
		return result;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_contacts, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
