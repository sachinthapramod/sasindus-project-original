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
    private static final int DATABASE_VERSION = 1;
    
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
    private static final String COLUMN_ROOM_PRICE = "price";
    private static final String COLUMN_ROOM_AVAILABILITY = "availability";
    
    // Service table columns
    private static final String COLUMN_SERVICE_ID = "id";
    private static final String COLUMN_SERVICE_NAME = "service_name";
    private static final String COLUMN_SERVICE_DESCRIPTION = "description";
    private static final String COLUMN_SERVICE_PRICE = "price";
    private static final String COLUMN_SERVICE_AVAILABILITY = "availability";
    
    // Booking table columns
    private static final String COLUMN_BOOKING_ID = "id";
    private static final String COLUMN_BOOKING_USER_ID = "user_id";
    private static final String COLUMN_BOOKING_ROOM_ID = "room_id";
    private static final String COLUMN_BOOKING_CHECKIN_DATE = "checkin_date";
    private static final String COLUMN_BOOKING_CHECKOUT_DATE = "checkout_date";
    private static final String COLUMN_BOOKING_STATUS = "status";
    
    // Reservation table columns
    private static final String COLUMN_RESERVATION_ID = "id";
    private static final String COLUMN_RESERVATION_USER_ID = "user_id";
    private static final String COLUMN_RESERVATION_SERVICE_ID = "service_id";
    private static final String COLUMN_RESERVATION_DATE = "date";
    private static final String COLUMN_RESERVATION_STATUS = "status";
    
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
        COLUMN_ROOM_PRICE + " REAL NOT NULL," +
        COLUMN_ROOM_AVAILABILITY + " INTEGER NOT NULL" +
        ")";
    
    private static final String CREATE_TABLE_SERVICES = 
        "CREATE TABLE " + TABLE_SERVICES + "(" +
        COLUMN_SERVICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
        COLUMN_SERVICE_NAME + " TEXT NOT NULL," +
        COLUMN_SERVICE_DESCRIPTION + " TEXT," +
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
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_ROOMS);
        db.execSQL(CREATE_TABLE_SERVICES);
        db.execSQL(CREATE_TABLE_BOOKINGS);
        db.execSQL(CREATE_TABLE_RESERVATIONS);
        db.execSQL(CREATE_TABLE_OFFERS);
        
        // Insert default admin user
        insertDefaultAdmin(db);
        // Insert sample data
        insertSampleData(db);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROOMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
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
    
    // Room operations
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ROOMS, null, null, null, null, null, null);
        
        while (cursor.moveToNext()) {
            Room room = new Room(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ROOM_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROOM_TYPE)),
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
    
    // Booking operations
    public boolean addBooking(Booking booking) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOKING_USER_ID, booking.getUserId());
        values.put(COLUMN_BOOKING_ROOM_ID, booking.getRoomId());
        values.put(COLUMN_BOOKING_CHECKIN_DATE, booking.getCheckinDate());
        values.put(COLUMN_BOOKING_CHECKOUT_DATE, booking.getCheckoutDate());
        values.put(COLUMN_BOOKING_STATUS, booking.getStatus());
        
        long result = db.insert(TABLE_BOOKINGS, null, values);
        db.close();
        return result != -1;
    }
    
    public List<Booking> getBookingsByUserId(int userId) {
        List<Booking> bookings = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKINGS, null, COLUMN_BOOKING_USER_ID + "=?", 
                new String[]{String.valueOf(userId)}, null, null, null);
        
        while (cursor.moveToNext()) {
            Booking booking = new Booking(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_ID)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_USER_ID)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_ROOM_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_CHECKIN_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_CHECKOUT_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_STATUS))
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
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_STATUS))
            );
            bookings.add(booking);
        }
        cursor.close();
        db.close();
        return bookings;
    }
    
    // Reservation operations
    public boolean addReservation(Reservation reservation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RESERVATION_USER_ID, reservation.getUserId());
        values.put(COLUMN_RESERVATION_SERVICE_ID, reservation.getServiceId());
        values.put(COLUMN_RESERVATION_DATE, reservation.getDate());
        values.put(COLUMN_RESERVATION_STATUS, reservation.getStatus());
        
        long result = db.insert(TABLE_RESERVATIONS, null, values);
        db.close();
        return result != -1;
    }
    
    public List<Reservation> getReservationsByUserId(int userId) {
        List<Reservation> reservations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RESERVATIONS, null, COLUMN_RESERVATION_USER_ID + "=?", 
                new String[]{String.valueOf(userId)}, null, null, null);
        
        while (cursor.moveToNext()) {
            Reservation reservation = new Reservation(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RESERVATION_ID)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RESERVATION_USER_ID)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RESERVATION_SERVICE_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESERVATION_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESERVATION_STATUS))
            );
            reservations.add(reservation);
        }
        cursor.close();
        db.close();
        return reservations;
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
}
