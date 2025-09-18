package com.luxevista.resort.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.luxevista.resort.models.Booking;
import com.luxevista.resort.models.Offer;
import com.luxevista.resort.models.Reservation;
import com.luxevista.resort.models.Room;
import com.luxevista.resort.models.Service;
import com.luxevista.resort.models.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    
    private static final String DATABASE_NAME = "luxevista_resort.db";
    private static final int DATABASE_VERSION = 5; // Increment version to force database recreation
    private Context context;
    
    // Table names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_ROOMS = "rooms";
    private static final String TABLE_SERVICES = "services";
    private static final String TABLE_BOOKINGS = "bookings";
    private static final String TABLE_RESERVATIONS = "reservations";
    private static final String TABLE_OFFERS = "offers";
    
    // User table columns
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_NAME = "name";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_PASSWORD = "password";
    private static final String COLUMN_USER_ROLE = "role";
    
    // Room table columns
    private static final String COLUMN_ROOM_ID = "id";
    private static final String COLUMN_ROOM_TYPE = "room_type";
    private static final String COLUMN_ROOM_DESCRIPTION = "description";
    private static final String COLUMN_ROOM_IMAGE_PATH = "image_path";
    private static final String COLUMN_ROOM_PRICE = "price";
    private static final String COLUMN_ROOM_AVAILABILITY = "availability";
    
    // Service table columns
    private static final String COLUMN_SERVICE_ID = "id";
    private static final String COLUMN_SERVICE_NAME = "service_name";
    private static final String COLUMN_SERVICE_DESCRIPTION = "description";
    private static final String COLUMN_SERVICE_IMAGE_PATH = "image_path";
    private static final String COLUMN_SERVICE_PRICE = "price";
    private static final String COLUMN_SERVICE_AVAILABILITY = "availability";
    
    // Booking table columns
    private static final String COLUMN_BOOKING_ID = "id";
    private static final String COLUMN_BOOKING_USER_ID = "user_id";
    private static final String COLUMN_BOOKING_ROOM_ID = "room_id";
    private static final String COLUMN_BOOKING_CHECKIN_DATE = "checkin_date";
    private static final String COLUMN_BOOKING_CHECKOUT_DATE = "checkout_date";
    private static final String COLUMN_BOOKING_STATUS = "status";
    private static final String COLUMN_BOOKING_CONFIRMED = "confirmed";
    
    // Reservation table columns
    private static final String COLUMN_RESERVATION_ID = "id";
    private static final String COLUMN_RESERVATION_USER_ID = "user_id";
    private static final String COLUMN_RESERVATION_SERVICE_ID = "service_id";
    private static final String COLUMN_RESERVATION_DATE = "date";
    private static final String COLUMN_RESERVATION_STATUS = "status";
    private static final String COLUMN_RESERVATION_CONFIRMED = "confirmed";
    
    // Offer table columns
    private static final String COLUMN_OFFER_ID = "id";
    private static final String COLUMN_OFFER_TITLE = "title";
    private static final String COLUMN_OFFER_DESCRIPTION = "description";
    private static final String COLUMN_OFFER_VALID_UNTIL = "valid_until";
    
    // Create table statements
    private static final String CREATE_TABLE_USERS = 
        "CREATE TABLE " + TABLE_USERS + "(" +
        COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
        COLUMN_USER_NAME + " TEXT NOT NULL," +
        COLUMN_USER_EMAIL + " TEXT UNIQUE NOT NULL," +
        COLUMN_USER_PASSWORD + " TEXT NOT NULL," +
        COLUMN_USER_ROLE + " TEXT NOT NULL" +
        ")";
    
    private static final String CREATE_TABLE_ROOMS = 
        "CREATE TABLE " + TABLE_ROOMS + "(" +
        COLUMN_ROOM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
        COLUMN_ROOM_TYPE + " TEXT NOT NULL," +
        COLUMN_ROOM_DESCRIPTION + " TEXT," +
        COLUMN_ROOM_IMAGE_PATH + " TEXT," +
        COLUMN_ROOM_PRICE + " REAL NOT NULL," +
        COLUMN_ROOM_AVAILABILITY + " INTEGER NOT NULL" +
        ")";
    
    private static final String CREATE_TABLE_SERVICES = 
        "CREATE TABLE " + TABLE_SERVICES + "(" +
        COLUMN_SERVICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
        COLUMN_SERVICE_NAME + " TEXT NOT NULL," +
        COLUMN_SERVICE_DESCRIPTION + " TEXT," +
        COLUMN_SERVICE_IMAGE_PATH + " TEXT," +
        COLUMN_SERVICE_PRICE + " REAL NOT NULL," +
        COLUMN_SERVICE_AVAILABILITY + " INTEGER NOT NULL" +
        ")";
    
    private static final String CREATE_TABLE_BOOKINGS = 
        "CREATE TABLE " + TABLE_BOOKINGS + "(" +
        COLUMN_BOOKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
        COLUMN_BOOKING_USER_ID + " INTEGER NOT NULL," +
        COLUMN_BOOKING_ROOM_ID + " INTEGER NOT NULL," +
        COLUMN_BOOKING_CHECKIN_DATE + " TEXT NOT NULL," +
        COLUMN_BOOKING_CHECKOUT_DATE + " TEXT NOT NULL," +
        COLUMN_BOOKING_STATUS + " TEXT NOT NULL," +
        COLUMN_BOOKING_CONFIRMED + " INTEGER NOT NULL DEFAULT 0," +
        "FOREIGN KEY(" + COLUMN_BOOKING_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")," +
        "FOREIGN KEY(" + COLUMN_BOOKING_ROOM_ID + ") REFERENCES " + TABLE_ROOMS + "(" + COLUMN_ROOM_ID + ")" +
        ")";
    
    private static final String CREATE_TABLE_RESERVATIONS = 
        "CREATE TABLE " + TABLE_RESERVATIONS + "(" +
        COLUMN_RESERVATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
        COLUMN_RESERVATION_USER_ID + " INTEGER NOT NULL," +
        COLUMN_RESERVATION_SERVICE_ID + " INTEGER NOT NULL," +
        COLUMN_RESERVATION_DATE + " TEXT NOT NULL," +
        COLUMN_RESERVATION_STATUS + " TEXT NOT NULL," +
        COLUMN_RESERVATION_CONFIRMED + " INTEGER NOT NULL DEFAULT 0," +
        "FOREIGN KEY(" + COLUMN_RESERVATION_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")," +
        "FOREIGN KEY(" + COLUMN_RESERVATION_SERVICE_ID + ") REFERENCES " + TABLE_SERVICES + "(" + COLUMN_SERVICE_ID + ")" +
        ")";
    
    private static final String CREATE_TABLE_OFFERS = 
        "CREATE TABLE " + TABLE_OFFERS + "(" +
        COLUMN_OFFER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
        COLUMN_OFFER_TITLE + " TEXT NOT NULL," +
        COLUMN_OFFER_DESCRIPTION + " TEXT," +
        COLUMN_OFFER_VALID_UNTIL + " TEXT NOT NULL" +
        ")";
    
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        // Force database recreation to fix the issue
        android.util.Log.d("DatabaseHelper", "DatabaseHelper created with version " + DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        android.util.Log.d("DatabaseHelper", "Creating new database with version " + DATABASE_VERSION);
        
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_ROOMS);
        db.execSQL(CREATE_TABLE_SERVICES);
        db.execSQL(CREATE_TABLE_BOOKINGS);
        db.execSQL(CREATE_TABLE_RESERVATIONS);
        db.execSQL(CREATE_TABLE_OFFERS);
        
        android.util.Log.d("DatabaseHelper", "All tables created successfully");
        
        // Insert default admin user
        insertDefaultAdmin(db);
        // Insert sample data
        insertSampleData(db);
        
        android.util.Log.d("DatabaseHelper", "Database creation completed");
    }
    
    // Method to check and fix database schema
    public void ensureDatabaseSchema() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // Check if confirmed column exists in bookings table
            Cursor cursor = db.rawQuery("PRAGMA table_info(" + TABLE_BOOKINGS + ")", null);
            boolean hasConfirmedColumn = false;
            android.util.Log.d("DatabaseHelper", "Checking bookings table schema:");
            while (cursor.moveToNext()) {
                String columnName = cursor.getString(1);
                android.util.Log.d("DatabaseHelper", "Column: " + columnName);
                if (columnName.equals(COLUMN_BOOKING_CONFIRMED)) {
                    hasConfirmedColumn = true;
                }
            }
            cursor.close();
            
            if (!hasConfirmedColumn) {
                android.util.Log.d("DatabaseHelper", "Adding confirmed column to bookings table");
                db.execSQL("ALTER TABLE " + TABLE_BOOKINGS + " ADD COLUMN " + COLUMN_BOOKING_CONFIRMED + " INTEGER NOT NULL DEFAULT 0");
            } else {
                android.util.Log.d("DatabaseHelper", "Confirmed column already exists in bookings table");
                // Fix any existing bookings that might have incorrect confirmed status
                fixExistingBookingsConfirmationStatus(db);
            }
            
            // Check if confirmed column exists in reservations table
            cursor = db.rawQuery("PRAGMA table_info(" + TABLE_RESERVATIONS + ")", null);
            hasConfirmedColumn = false;
            android.util.Log.d("DatabaseHelper", "Checking reservations table schema:");
            while (cursor.moveToNext()) {
                String columnName = cursor.getString(1);
                android.util.Log.d("DatabaseHelper", "Column: " + columnName);
                if (columnName.equals(COLUMN_RESERVATION_CONFIRMED)) {
                    hasConfirmedColumn = true;
                }
            }
            cursor.close();
            
            if (!hasConfirmedColumn) {
                android.util.Log.d("DatabaseHelper", "Adding confirmed column to reservations table");
                db.execSQL("ALTER TABLE " + TABLE_RESERVATIONS + " ADD COLUMN " + COLUMN_RESERVATION_CONFIRMED + " INTEGER NOT NULL DEFAULT 0");
            } else {
                android.util.Log.d("DatabaseHelper", "Confirmed column already exists in reservations table");
                // Fix any existing reservations that might have incorrect confirmed status
                fixExistingReservationsConfirmationStatus(db);
            }
        } catch (Exception e) {
            android.util.Log.e("DatabaseHelper", "Error ensuring database schema: " + e.getMessage(), e);
        } finally {
            db.close();
        }
    }
    
    // Fix existing bookings to ensure they are unconfirmed by default
    private void fixExistingBookingsConfirmationStatus(SQLiteDatabase db) {
        try {
            // Update all bookings to be unconfirmed unless they have status "confirmed"
            ContentValues values = new ContentValues();
            values.put(COLUMN_BOOKING_CONFIRMED, 0);
            
            int updated = db.update(TABLE_BOOKINGS, values, 
                COLUMN_BOOKING_STATUS + " != ?", 
                new String[]{"confirmed"});
            
            android.util.Log.d("DatabaseHelper", "Fixed " + updated + " bookings to be unconfirmed");
            
            // Also update bookings with "confirmed" status to be confirmed
            values = new ContentValues();
            values.put(COLUMN_BOOKING_CONFIRMED, 1);
            
            int confirmed = db.update(TABLE_BOOKINGS, values, 
                COLUMN_BOOKING_STATUS + " = ?", 
                new String[]{"confirmed"});
            
            android.util.Log.d("DatabaseHelper", "Set " + confirmed + " bookings to be confirmed");
            
        } catch (Exception e) {
            android.util.Log.e("DatabaseHelper", "Error fixing booking confirmation status: " + e.getMessage(), e);
        }
    }
    
    // Fix existing reservations to ensure they are unconfirmed by default
    private void fixExistingReservationsConfirmationStatus(SQLiteDatabase db) {
        try {
            // Update all reservations to be unconfirmed unless they have status "confirmed"
            ContentValues values = new ContentValues();
            values.put(COLUMN_RESERVATION_CONFIRMED, 0);
            
            int updated = db.update(TABLE_RESERVATIONS, values, 
                COLUMN_RESERVATION_STATUS + " != ?", 
                new String[]{"confirmed"});
            
            android.util.Log.d("DatabaseHelper", "Fixed " + updated + " reservations to be unconfirmed");
            
            // Also update reservations with "confirmed" status to be confirmed
            values = new ContentValues();
            values.put(COLUMN_RESERVATION_CONFIRMED, 1);
            
            int confirmed = db.update(TABLE_RESERVATIONS, values, 
                COLUMN_RESERVATION_STATUS + " = ?", 
                new String[]{"confirmed"});
            
            android.util.Log.d("DatabaseHelper", "Set " + confirmed + " reservations to be confirmed");
            
        } catch (Exception e) {
            android.util.Log.e("DatabaseHelper", "Error fixing reservation confirmation status: " + e.getMessage(), e);
        }
    }
    
    // Method to check actual data in the database
    public void checkDatabaseData() {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            // Check bookings data
            Cursor cursor = db.rawQuery("SELECT id, user_id, status, confirmed FROM " + TABLE_BOOKINGS, null);
            android.util.Log.d("DatabaseHelper", "Bookings data:");
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                int userId = cursor.getInt(1);
                String status = cursor.getString(2);
                int confirmed = cursor.getInt(3);
                android.util.Log.d("DatabaseHelper", "Booking ID: " + id + ", User ID: " + userId + 
                    ", Status: " + status + ", Confirmed: " + confirmed);
            }
            cursor.close();
            
            // Check reservations data
            cursor = db.rawQuery("SELECT id, user_id, status, confirmed FROM " + TABLE_RESERVATIONS, null);
            android.util.Log.d("DatabaseHelper", "Reservations data:");
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                int userId = cursor.getInt(1);
                String status = cursor.getString(2);
                int confirmed = cursor.getInt(3);
                android.util.Log.d("DatabaseHelper", "Reservation ID: " + id + ", User ID: " + userId + 
                    ", Status: " + status + ", Confirmed: " + confirmed);
            }
            cursor.close();
        } catch (Exception e) {
            android.util.Log.e("DatabaseHelper", "Error checking database data: " + e.getMessage(), e);
        } finally {
            db.close();
        }
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        android.util.Log.d("DatabaseHelper", "Upgrading database from version " + oldVersion + " to " + newVersion);
        
        if (oldVersion < 2) {
            // Add new columns to existing tables
            db.execSQL("ALTER TABLE " + TABLE_ROOMS + " ADD COLUMN " + COLUMN_ROOM_DESCRIPTION + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_ROOMS + " ADD COLUMN " + COLUMN_ROOM_IMAGE_PATH + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_SERVICES + " ADD COLUMN " + COLUMN_SERVICE_IMAGE_PATH + " TEXT");
        }
        if (oldVersion < 3) {
            // Add confirmed column to bookings table
            db.execSQL("ALTER TABLE " + TABLE_BOOKINGS + " ADD COLUMN " + COLUMN_BOOKING_CONFIRMED + " INTEGER NOT NULL DEFAULT 0");
        }
        if (oldVersion < 4) {
            // Add confirmed column to reservations table
            db.execSQL("ALTER TABLE " + TABLE_RESERVATIONS + " ADD COLUMN " + COLUMN_RESERVATION_CONFIRMED + " INTEGER NOT NULL DEFAULT 0");
        }
        if (oldVersion < 5) {
            // Force complete database recreation to fix confirmation status issues
            android.util.Log.d("DatabaseHelper", "Forcing database recreation due to confirmation status issues");
            recreateDatabase(db);
        }
    }
    
    // Method to recreate the database completely
    private void recreateDatabase(SQLiteDatabase db) {
        try {
            // Drop all tables
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATIONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROOMS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            
            android.util.Log.d("DatabaseHelper", "All tables dropped");
            
            // Recreate all tables
            db.execSQL(CREATE_TABLE_USERS);
            db.execSQL(CREATE_TABLE_ROOMS);
            db.execSQL(CREATE_TABLE_SERVICES);
            db.execSQL(CREATE_TABLE_BOOKINGS);
            db.execSQL(CREATE_TABLE_RESERVATIONS);
            db.execSQL(CREATE_TABLE_OFFERS);
            
            android.util.Log.d("DatabaseHelper", "All tables recreated");
            
            // Insert default data
            insertDefaultAdmin(db);
            insertSampleData(db);
            
            android.util.Log.d("DatabaseHelper", "Database recreation completed");
            
        } catch (Exception e) {
            android.util.Log.e("DatabaseHelper", "Error recreating database: " + e.getMessage(), e);
        }
    }
    
    private void insertDefaultAdmin(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, "Admin");
        values.put(COLUMN_USER_EMAIL, "admin@luxevista.com");
        values.put(COLUMN_USER_PASSWORD, "admin123");
        values.put(COLUMN_USER_ROLE, "admin");
        db.insert(TABLE_USERS, null, values);
    }
    
    private void insertSampleData(SQLiteDatabase db) {
        // Insert sample rooms
        String[] roomTypes = {"Deluxe Suite", "Ocean View", "Presidential Suite", "Standard Room"};
        double[] prices = {299.99, 199.99, 499.99, 149.99};
        
        for (int i = 0; i < roomTypes.length; i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ROOM_TYPE, roomTypes[i]);
            values.put(COLUMN_ROOM_PRICE, prices[i]);
            values.put(COLUMN_ROOM_AVAILABILITY, 1);
            db.insert(TABLE_ROOMS, null, values);
        }
        
        // Insert sample services
        String[] serviceNames = {"Spa Treatment", "Fine Dining", "Pool Cabana", "City Tour"};
        String[] descriptions = {"Relaxing spa treatment", "Gourmet dining experience", "Private pool cabana", "Guided city tour"};
        double[] servicePrices = {89.99, 79.99, 59.99, 49.99};
        
        for (int i = 0; i < serviceNames.length; i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_SERVICE_NAME, serviceNames[i]);
            values.put(COLUMN_SERVICE_DESCRIPTION, descriptions[i]);
            values.put(COLUMN_SERVICE_PRICE, servicePrices[i]);
            values.put(COLUMN_SERVICE_AVAILABILITY, 1);
            db.insert(TABLE_SERVICES, null, values);
        }
        
        // Insert sample offers
        String[] offerTitles = {"Summer Special", "Weekend Getaway", "Honeymoon Package"};
        String[] offerDescriptions = {"20% off all rooms", "Free breakfast included", "Romantic dinner for two"};
        String[] validUntil = {"2024-12-31", "2024-12-31", "2024-12-31"};
        
        for (int i = 0; i < offerTitles.length; i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_OFFER_TITLE, offerTitles[i]);
            values.put(COLUMN_OFFER_DESCRIPTION, offerDescriptions[i]);
            values.put(COLUMN_OFFER_VALID_UNTIL, validUntil[i]);
            db.insert(TABLE_OFFERS, null, values);
        }
    }
    
    // User operations
    public boolean addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_ROLE, user.getRole());
        
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }
    
    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USER_EMAIL + "=?", 
                new String[]{email}, null, null, null);
        
        User user = null;
        if (cursor.moveToFirst()) {
            user = new User(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ROLE))
            );
        }
        cursor.close();
        db.close();
        return user;
    }
    
    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_ID}, 
                COLUMN_USER_EMAIL + "=?", new String[]{email}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }
    
    // Method to check if specific email exists and return user details
    public void checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USER_EMAIL + "=?", 
                    new String[]{email}, null, null, null);
            
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME));
                String userEmail = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL));
                String role = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ROLE));
                
                android.util.Log.d("DatabaseHelper", "Email '" + email + "' EXISTS:");
                android.util.Log.d("DatabaseHelper", "ID: " + id);
                android.util.Log.d("DatabaseHelper", "Name: " + name);
                android.util.Log.d("DatabaseHelper", "Email: " + userEmail);
                android.util.Log.d("DatabaseHelper", "Role: " + role);
            } else {
                android.util.Log.d("DatabaseHelper", "Email '" + email + "' does NOT exist in database");
            }
            cursor.close();
        } catch (Exception e) {
            android.util.Log.e("DatabaseHelper", "Error checking email: " + e.getMessage(), e);
        } finally {
            db.close();
        }
    }
    
    // Method to show all users in database
    public void showAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.query(TABLE_USERS, null, null, null, null, null, null);
            
            android.util.Log.d("DatabaseHelper", "=== ALL USERS IN DATABASE ===");
            android.util.Log.d("DatabaseHelper", "Total users: " + cursor.getCount());
            
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL));
                String role = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ROLE));
                
                android.util.Log.d("DatabaseHelper", "User " + id + ": " + name + " (" + email + ") - Role: " + role);
            }
            android.util.Log.d("DatabaseHelper", "=== END USER LIST ===");
            cursor.close();
        } catch (Exception e) {
            android.util.Log.e("DatabaseHelper", "Error showing users: " + e.getMessage(), e);
        } finally {
            db.close();
        }
    }
    
    // Room operations
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ROOMS, null, null, null, null, null, null);
        
        while (cursor.moveToNext()) {
            Room room = new Room(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ROOM_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROOM_TYPE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROOM_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROOM_IMAGE_PATH)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ROOM_PRICE)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ROOM_AVAILABILITY))
            );
            rooms.add(room);
        }
        cursor.close();
        db.close();
        return rooms;
    }
    
    public boolean addRoom(Room room) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ROOM_TYPE, room.getRoomType());
        values.put(COLUMN_ROOM_DESCRIPTION, room.getDescription());
        values.put(COLUMN_ROOM_IMAGE_PATH, room.getImagePath());
        values.put(COLUMN_ROOM_PRICE, room.getPrice());
        values.put(COLUMN_ROOM_AVAILABILITY, room.getAvailability());
        
        long result = db.insert(TABLE_ROOMS, null, values);
        db.close();
        return result != -1;
    }
    
    public boolean updateRoom(Room room) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ROOM_TYPE, room.getRoomType());
        values.put(COLUMN_ROOM_DESCRIPTION, room.getDescription());
        values.put(COLUMN_ROOM_IMAGE_PATH, room.getImagePath());
        values.put(COLUMN_ROOM_PRICE, room.getPrice());
        values.put(COLUMN_ROOM_AVAILABILITY, room.getAvailability());
        
        int result = db.update(TABLE_ROOMS, values, COLUMN_ROOM_ID + "=?", 
                new String[]{String.valueOf(room.getId())});
        db.close();
        return result > 0;
    }
    
    public boolean deleteRoom(int roomId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_ROOMS, COLUMN_ROOM_ID + "=?", 
                new String[]{String.valueOf(roomId)});
        db.close();
        return result > 0;
    }
    
    public Room getRoomById(int roomId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ROOMS, null, COLUMN_ROOM_ID + "=?", 
                new String[]{String.valueOf(roomId)}, null, null, null);
        
        Room room = null;
        if (cursor.moveToFirst()) {
            room = new Room(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ROOM_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROOM_TYPE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROOM_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROOM_IMAGE_PATH)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ROOM_PRICE)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ROOM_AVAILABILITY))
            );
        }
        cursor.close();
        db.close();
        return room;
    }
    
    // Service operations
    public List<Service> getAllServices() {
        List<Service> services = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SERVICES, null, null, null, null, null, null);
        
        while (cursor.moveToNext()) {
            Service service = new Service(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_IMAGE_PATH)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_PRICE)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_AVAILABILITY))
            );
            services.add(service);
        }
        cursor.close();
        db.close();
        return services;
    }
    
    public boolean addService(Service service) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SERVICE_NAME, service.getServiceName());
        values.put(COLUMN_SERVICE_DESCRIPTION, service.getDescription());
        values.put(COLUMN_SERVICE_IMAGE_PATH, service.getImagePath());
        values.put(COLUMN_SERVICE_PRICE, service.getPrice());
        values.put(COLUMN_SERVICE_AVAILABILITY, service.getAvailability());
        
        long result = db.insert(TABLE_SERVICES, null, values);
        db.close();
        return result != -1;
    }
    
    public boolean updateService(Service service) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SERVICE_NAME, service.getServiceName());
        values.put(COLUMN_SERVICE_DESCRIPTION, service.getDescription());
        values.put(COLUMN_SERVICE_IMAGE_PATH, service.getImagePath());
        values.put(COLUMN_SERVICE_PRICE, service.getPrice());
        values.put(COLUMN_SERVICE_AVAILABILITY, service.getAvailability());
        
        int result = db.update(TABLE_SERVICES, values, COLUMN_SERVICE_ID + "=?", 
                new String[]{String.valueOf(service.getId())});
        db.close();
        return result > 0;
    }
    
    public boolean deleteService(int serviceId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_SERVICES, COLUMN_SERVICE_ID + "=?", 
                new String[]{String.valueOf(serviceId)});
        db.close();
        return result > 0;
    }
    
    public Service getServiceById(int serviceId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SERVICES, null, COLUMN_SERVICE_ID + "=?", 
                new String[]{String.valueOf(serviceId)}, null, null, null);
        
        Service service = null;
        if (cursor.moveToFirst()) {
            service = new Service(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_IMAGE_PATH)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_PRICE)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_AVAILABILITY))
            );
        }
        cursor.close();
        db.close();
        return service;
    }
    
    // Booking operations
    public boolean addBooking(Booking booking) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOKING_USER_ID, booking.getUserId());
        values.put(COLUMN_BOOKING_ROOM_ID, booking.getRoomId());
        values.put(COLUMN_BOOKING_CHECKIN_DATE, booking.getCheckinDate());
        values.put(COLUMN_BOOKING_CHECKOUT_DATE, booking.getCheckoutDate());
        values.put(COLUMN_BOOKING_STATUS, booking.getStatus());
        values.put(COLUMN_BOOKING_CONFIRMED, booking.isConfirmed() ? 1 : 0);
        
        // Debug logging
        android.util.Log.d("DatabaseHelper", "Adding booking - Status: " + booking.getStatus() + 
            ", Confirmed: " + booking.isConfirmed() + 
            ", Confirmed value in DB: " + (booking.isConfirmed() ? 1 : 0));
        
        long result = db.insert(TABLE_BOOKINGS, null, values);
        
        if (result != -1) {
            android.util.Log.d("DatabaseHelper", "Booking added successfully with ID: " + result);
        } else {
            android.util.Log.e("DatabaseHelper", "Failed to add booking");
        }
        
        db.close();
        return result != -1;
    }
    
    public List<Booking> getBookingsByUserId(int userId) {
        List<Booking> bookings = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKINGS, null, COLUMN_BOOKING_USER_ID + "=?", 
                new String[]{String.valueOf(userId)}, null, null, null);
        
        while (cursor.moveToNext()) {
            int confirmedValue = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_CONFIRMED));
            boolean isConfirmed = confirmedValue == 1;
            
            // Debug logging
            android.util.Log.d("DatabaseHelper", "Reading booking - ID: " + cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_ID)) + 
                ", Confirmed value: " + confirmedValue + ", Is confirmed: " + isConfirmed);
            
            Booking booking = new Booking(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_ID)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_USER_ID)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_ROOM_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_CHECKIN_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_CHECKOUT_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_STATUS)),
                isConfirmed
            );
            bookings.add(booking);
        }
        cursor.close();
        db.close();
        return bookings;
    }
    
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKINGS, null, null, null, null, null, null);
        
        while (cursor.moveToNext()) {
            Booking booking = new Booking(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_ID)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_USER_ID)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_ROOM_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_CHECKIN_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_CHECKOUT_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_STATUS)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_CONFIRMED)) == 1
            );
            bookings.add(booking);
        }
        cursor.close();
        db.close();
        return bookings;
    }
    
    public boolean updateBooking(Booking booking) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOKING_USER_ID, booking.getUserId());
        values.put(COLUMN_BOOKING_ROOM_ID, booking.getRoomId());
        values.put(COLUMN_BOOKING_CHECKIN_DATE, booking.getCheckinDate());
        values.put(COLUMN_BOOKING_CHECKOUT_DATE, booking.getCheckoutDate());
        values.put(COLUMN_BOOKING_STATUS, booking.getStatus());
        values.put(COLUMN_BOOKING_CONFIRMED, booking.isConfirmed() ? 1 : 0);
        
        int result = db.update(TABLE_BOOKINGS, values, COLUMN_BOOKING_ID + "=?", 
                new String[]{String.valueOf(booking.getId())});
        db.close();
        return result > 0;
    }
    
    public boolean confirmBooking(int bookingId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOKING_CONFIRMED, 1);
        values.put(COLUMN_BOOKING_STATUS, "confirmed"); // Update status to confirmed as well
        
        int result = db.update(TABLE_BOOKINGS, values, COLUMN_BOOKING_ID + "=?", 
                new String[]{String.valueOf(bookingId)});
        
        android.util.Log.d("DatabaseHelper", "Confirming booking ID: " + bookingId + ", Result: " + result);
        
        db.close();
        return result > 0;
    }
    
    public boolean deleteBooking(int bookingId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_BOOKINGS, COLUMN_BOOKING_ID + "=?", 
                new String[]{String.valueOf(bookingId)});
        db.close();
        return result > 0;
    }
    
    // Reservation operations
    public boolean addReservation(Reservation reservation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RESERVATION_USER_ID, reservation.getUserId());
        values.put(COLUMN_RESERVATION_SERVICE_ID, reservation.getServiceId());
        values.put(COLUMN_RESERVATION_DATE, reservation.getDate());
        values.put(COLUMN_RESERVATION_STATUS, reservation.getStatus());
        values.put(COLUMN_RESERVATION_CONFIRMED, reservation.isConfirmed() ? 1 : 0);
        
        // Debug logging
        android.util.Log.d("DatabaseHelper", "Adding reservation - Status: " + reservation.getStatus() + 
            ", Confirmed: " + reservation.isConfirmed() + 
            ", Confirmed value in DB: " + (reservation.isConfirmed() ? 1 : 0));
        
        long result = db.insert(TABLE_RESERVATIONS, null, values);
        
        if (result != -1) {
            android.util.Log.d("DatabaseHelper", "Reservation added successfully with ID: " + result);
        } else {
            android.util.Log.e("DatabaseHelper", "Failed to add reservation");
        }
        
        db.close();
        return result != -1;
    }
    
    public List<Reservation> getReservationsByUserId(int userId) {
        List<Reservation> reservations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RESERVATIONS, null, COLUMN_RESERVATION_USER_ID + "=?", 
                new String[]{String.valueOf(userId)}, null, null, null);
        
        while (cursor.moveToNext()) {
            int confirmedValue = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RESERVATION_CONFIRMED));
            boolean isConfirmed = confirmedValue == 1;
            
            // Debug logging
            android.util.Log.d("DatabaseHelper", "Reading reservation - ID: " + cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RESERVATION_ID)) + 
                ", Confirmed value: " + confirmedValue + ", Is confirmed: " + isConfirmed);
            
            Reservation reservation = new Reservation(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RESERVATION_ID)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RESERVATION_USER_ID)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RESERVATION_SERVICE_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESERVATION_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESERVATION_STATUS)),
                isConfirmed
            );
            reservations.add(reservation);
        }
        cursor.close();
        db.close();
        return reservations;
    }
    
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RESERVATIONS, null, null, null, null, null, null);
        
        while (cursor.moveToNext()) {
            Reservation reservation = new Reservation(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RESERVATION_ID)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RESERVATION_USER_ID)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RESERVATION_SERVICE_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESERVATION_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESERVATION_STATUS)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RESERVATION_CONFIRMED)) == 1
            );
            reservations.add(reservation);
        }
        cursor.close();
        db.close();
        return reservations;
    }
    
    public boolean updateReservation(Reservation reservation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RESERVATION_USER_ID, reservation.getUserId());
        values.put(COLUMN_RESERVATION_SERVICE_ID, reservation.getServiceId());
        values.put(COLUMN_RESERVATION_DATE, reservation.getDate());
        values.put(COLUMN_RESERVATION_STATUS, reservation.getStatus());
        values.put(COLUMN_RESERVATION_CONFIRMED, reservation.isConfirmed() ? 1 : 0);
        
        int result = db.update(TABLE_RESERVATIONS, values, COLUMN_RESERVATION_ID + "=?", 
                new String[]{String.valueOf(reservation.getId())});
        db.close();
        return result > 0;
    }
    
    public boolean confirmReservation(int reservationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RESERVATION_CONFIRMED, 1);
        
        int result = db.update(TABLE_RESERVATIONS, values, COLUMN_RESERVATION_ID + "=?", 
                new String[]{String.valueOf(reservationId)});
        db.close();
        return result > 0;
    }
    
    public boolean deleteReservation(int reservationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_RESERVATIONS, COLUMN_RESERVATION_ID + "=?", 
                new String[]{String.valueOf(reservationId)});
        db.close();
        return result > 0;
    }
    
    // Offer operations
    public List<Offer> getAllOffers() {
        List<Offer> offers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_OFFERS, null, null, null, null, null, null);
        
        while (cursor.moveToNext()) {
            Offer offer = new Offer(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_OFFER_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OFFER_TITLE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OFFER_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OFFER_VALID_UNTIL))
            );
            offers.add(offer);
        }
        cursor.close();
        db.close();
        return offers;
    }
    
    public boolean addOffer(Offer offer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_OFFER_TITLE, offer.getTitle());
        values.put(COLUMN_OFFER_DESCRIPTION, offer.getDescription());
        values.put(COLUMN_OFFER_VALID_UNTIL, offer.getValidUntil());
        
        long result = db.insert(TABLE_OFFERS, null, values);
        db.close();
        return result != -1;
    }
    
    public boolean updateOffer(Offer offer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_OFFER_TITLE, offer.getTitle());
        values.put(COLUMN_OFFER_DESCRIPTION, offer.getDescription());
        values.put(COLUMN_OFFER_VALID_UNTIL, offer.getValidUntil());
        
        int result = db.update(TABLE_OFFERS, values, COLUMN_OFFER_ID + "=?", 
                new String[]{String.valueOf(offer.getId())});
        db.close();
        return result > 0;
    }
    
    public boolean deleteOffer(int offerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_OFFERS, COLUMN_OFFER_ID + "=?", 
                new String[]{String.valueOf(offerId)});
        db.close();
        return result > 0;
    }
    
    // Method to fix pending bookings that are incorrectly marked as confirmed
    public void fixPendingBookingsConfirmationStatus() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_BOOKING_CONFIRMED, 0);
            
            // Fix bookings with "pending" status that are marked as confirmed
            int updated = db.update(TABLE_BOOKINGS, values, 
                COLUMN_BOOKING_STATUS + " = ? AND " + COLUMN_BOOKING_CONFIRMED + " = ?", 
                new String[]{"pending", "1"});
            
            android.util.Log.d("DatabaseHelper", "Fixed " + updated + " pending bookings that were incorrectly marked as confirmed");
            
        } catch (Exception e) {
            android.util.Log.e("DatabaseHelper", "Error fixing pending bookings: " + e.getMessage(), e);
        } finally {
            db.close();
        }
    }
    
    // Method to fix pending reservations that are incorrectly marked as confirmed
    public void fixPendingReservationsConfirmationStatus() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_RESERVATION_CONFIRMED, 0);
            
            // Fix reservations with "pending" status that are marked as confirmed
            int updated = db.update(TABLE_RESERVATIONS, values, 
                COLUMN_RESERVATION_STATUS + " = ? AND " + COLUMN_RESERVATION_CONFIRMED + " = ?", 
                new String[]{"pending", "1"});
            
            android.util.Log.d("DatabaseHelper", "Fixed " + updated + " pending reservations that were incorrectly marked as confirmed");
            
        } catch (Exception e) {
            android.util.Log.e("DatabaseHelper", "Error fixing pending reservations: " + e.getMessage(), e);
        } finally {
            db.close();
        }
    }
    
    // Method to reset all bookings to unconfirmed status (for debugging/fixing)
    public void resetAllBookingsToUnconfirmed() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_BOOKING_CONFIRMED, 0);
            
            int updated = db.update(TABLE_BOOKINGS, values, null, null);
            android.util.Log.d("DatabaseHelper", "Reset " + updated + " bookings to unconfirmed status");
            
        } catch (Exception e) {
            android.util.Log.e("DatabaseHelper", "Error resetting bookings: " + e.getMessage(), e);
        } finally {
            db.close();
        }
    }
    
    // Method to reset all reservations to unconfirmed status (for debugging/fixing)
    public void resetAllReservationsToUnconfirmed() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_RESERVATION_CONFIRMED, 0);
            
            int updated = db.update(TABLE_RESERVATIONS, values, null, null);
            android.util.Log.d("DatabaseHelper", "Reset " + updated + " reservations to unconfirmed status");
            
        } catch (Exception e) {
            android.util.Log.e("DatabaseHelper", "Error resetting reservations: " + e.getMessage(), e);
        } finally {
            db.close();
        }
    }
    
    // Method to completely reset the database (nuclear option)
    public void completelyResetDatabase() {
        try {
            android.util.Log.d("DatabaseHelper", "Completely resetting database");
            
            // Close any open connections
            SQLiteDatabase db = this.getWritableDatabase();
            db.close();
            
            // Delete the database file
            boolean deleted = context.deleteDatabase(DATABASE_NAME);
            android.util.Log.d("DatabaseHelper", "Database file deleted: " + deleted);
            
            // Force recreation by getting a new instance
            db = this.getWritableDatabase();
            db.close();
            
            android.util.Log.d("DatabaseHelper", "Database completely reset and recreated");
            
        } catch (Exception e) {
            android.util.Log.e("DatabaseHelper", "Error completely resetting database: " + e.getMessage(), e);
        }
    }
    
    // Method to clean up test bookings (remove bookings with test dates)
    public void cleanupTestBookings() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // Remove bookings with test dates (2024-12-25, 2024-12-27, etc.)
            String[] testDates = {"2024-12-25", "2024-12-27", "2024-01-01", "2024-01-03"};
            
            for (String testDate : testDates) {
                int deleted = db.delete(TABLE_BOOKINGS, 
                    COLUMN_BOOKING_CHECKIN_DATE + " = ? OR " + COLUMN_BOOKING_CHECKOUT_DATE + " = ?", 
                    new String[]{testDate, testDate});
                
                if (deleted > 0) {
                    android.util.Log.d("DatabaseHelper", "Removed " + deleted + " test bookings with date: " + testDate);
                }
            }
            
        } catch (Exception e) {
            android.util.Log.e("DatabaseHelper", "Error cleaning up test bookings: " + e.getMessage(), e);
        } finally {
            db.close();
        }
    }
    
    // Method to fix inconsistent booking statuses
    public void fixInconsistentBookingStatuses() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // Fix bookings that are confirmed but have wrong status
            ContentValues values = new ContentValues();
            values.put(COLUMN_BOOKING_STATUS, "confirmed");
            
            int updated = db.update(TABLE_BOOKINGS, values, 
                COLUMN_BOOKING_CONFIRMED + " = ? AND " + COLUMN_BOOKING_STATUS + " != ?", 
                new String[]{"1", "confirmed"});
            
            if (updated > 0) {
                android.util.Log.d("DatabaseHelper", "Fixed " + updated + " bookings with inconsistent status");
            }
            
        } catch (Exception e) {
            android.util.Log.e("DatabaseHelper", "Error fixing booking statuses: " + e.getMessage(), e);
        } finally {
            db.close();
        }
    }
}
