package part.com.phonebook;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class AddContactActivity extends Activity {
	private static final int SELECT_PHOTO = 100;
	DBAdapter dba;

	EditText et_first_name;
	EditText et_last_name;
	EditText et_phone_number;
	TextView tv_message;
	String photo_path = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		dba = new DBAdapter(getApplicationContext());
		et_first_name = (EditText) findViewById(R.id.EditTextFirstName);
		et_last_name = (EditText) findViewById(R.id.editTextLastName);
		et_phone_number = (EditText) findViewById(R.id.editTextTelNumber);
		tv_message = (TextView) findViewById(R.id.tv_message);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_contact, menu);
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

	public void addPhoto(View view) {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, SELECT_PHOTO);
	}

	public void saveContact(View view) {
		Contact contact = new Contact();
		contact.first_name = et_first_name.getText().toString();
		contact.last_name = et_last_name.getText().toString();
		contact.phone_number = et_phone_number.getText().toString();
		contact.photo_path = photo_path;

		if (contact.first_name.length()>0 && contact.last_name.length()>0
				&& contact.phone_number.length()>0)
		{
			dba.open();
			contact.id = dba.insertContact(contact);
			dba.close();
			this.finish();
		}
		else
		{
			tv_message.setText("Please fill all fields");
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

		switch (requestCode) {
		case SELECT_PHOTO:
			if (resultCode == RESULT_OK) {
				Uri selectedImage = imageReturnedIntent.getData();
				InputStream imageStream = null;
				photo_path = selectedImage.getPath();
				try {
					imageStream = getContentResolver().openInputStream(
							selectedImage);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Bitmap yourSelectedImage = BitmapFactory
						.decodeStream(imageStream);
				ImageView image = (ImageView) findViewById(R.id.imageView1);
				image.setImageBitmap(yourSelectedImage);
			}
		}
	}
}
