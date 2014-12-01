package part.com.phonebook;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_FNAME = "first_name";
	public static final String KEY_LNAME = "last_name";
	public static final String KEY_PHONE = "phone_number";
	public static final String KEY_PHOTO = "photo_path";

	private static final String TAG = "DBAdapter";
	private static final String DATABASE_NAME = "MyDB";
	private static final String DATABASE_TABLE = "contacts";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE = "create table contacts (_id integer primary key autoincrement, "
			+ "first_name text not null,last_name text not null, phone_number text not null, photo_path text);";

	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(DATABASE_CREATE);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");

			db.execSQL("DROP TABLE IF EXISTS contacts");
			onCreate(db);
		}

	}

	// ---opens the database---
	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		DBHelper.close();
	}

	// ---insert a contact into the database---
	public long insertContact(Contact contact) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_FNAME, contact.first_name);
		initialValues.put(KEY_LNAME, contact.last_name);
		initialValues.put(KEY_PHONE, contact.phone_number);
		initialValues.put(KEY_PHOTO, contact.photo_path);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}

	// ---deletes a particular contact---
	public boolean deleteContact(long rowId) {
		return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	// ---retrieves all the contacts---
	public List<Contact> getAllContacts() {

		List<Contact> contacts = new ArrayList<Contact>();

		Cursor c = db.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_FNAME,
				KEY_LNAME,KEY_PHONE, KEY_PHOTO }, null, null, null, null, null);

		if (c != null) {
			c.moveToFirst();
			do {
				Contact contact = new Contact();
				contact.id = c.getLong(0);
				contact.first_name = c.getString(1);
				contact.last_name = c.getString(2);
				contact.phone_number = c.getString(3);
				contact.photo_path = c.getString(4);
				contacts.add(contact);
			} while (c.moveToNext());
		}

		return contacts;
	}

	// ---updates a contact---
	public boolean updateContact(Contact contact) {
		ContentValues args = new ContentValues();
		args.put(KEY_FNAME, contact.first_name);
		args.put(KEY_LNAME, contact.last_name);
		args.put(KEY_PHONE, contact.phone_number);
		args.put(KEY_PHOTO, contact.photo_path);

		return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + contact.id,
				null) > 0;
	}
}
