# LuxeVista Resort - Android Hotel Management System

A complete Android mobile application built in Java with SQLite database for hotel management system.

## 🏨 Project Overview

**LuxeVista Resort** is a comprehensive hotel management system that allows guests to book rooms, reserve services, and view offers, while providing administrators with tools to manage all aspects of the hotel operations.

## 🔹 Tech Stack

- **Language**: Java
- **IDE**: Android Studio
- **Database**: SQLite (using SQLiteOpenHelper)
- **Min SDK**: 21 (Android 5.0)
- **Target SDK**: 34 (Android 14)

## 🔹 Features

### Guest Features
- ✅ User Registration & Login
- ✅ Room Booking with date validation
- ✅ Service Reservations (Spa, Dining, Tours, etc.)
- ✅ View Special Offers
- ✅ Profile Management
- ✅ Booking History

### Admin Features
- ✅ Admin Login
- ✅ Manage Rooms (Add, Update, Delete)
- ✅ Manage Services (Add, Update, Delete)
- ✅ Manage Offers (Add, Update, Delete)
- ✅ View All Bookings
- ✅ User Management

## 🔹 Database Schema

The application uses SQLite with the following tables:

- **users** (id, name, email, password, role)
- **rooms** (id, room_type, price, availability)
- **services** (id, service_name, description, price, availability)
- **bookings** (id, user_id, room_id, checkin_date, checkout_date, status)
- **reservations** (id, user_id, service_id, date, status)
- **offers** (id, title, description, valid_until)

## 🔹 Project Structure

```
app/src/main/java/com/luxevista/resort/
├── activities/
│   ├── LoginActivity.java
│   ├── RegisterActivity.java
│   ├── GuestDashboardActivity.java
│   ├── AdminDashboardActivity.java
│   ├── RoomBookingActivity.java
│   ├── ServiceReservationActivity.java
│   ├── OffersActivity.java
│   ├── ProfileActivity.java
│   ├── ManageRoomsActivity.java
│   ├── ManageServicesActivity.java
│   ├── ManageOffersActivity.java
│   └── ViewBookingsActivity.java
├── adapters/
│   ├── RoomAdapter.java
│   ├── ServiceAdapter.java
│   ├── OfferAdapter.java
│   └── BookingAdapter.java
├── database/
│   └── DatabaseHelper.java
└── models/
    ├── User.java
    ├── Room.java
    ├── Service.java
    ├── Booking.java
    ├── Reservation.java
    └── Offer.java
```

## 🔹 Activities & Layouts

### Authentication
- `LoginActivity` - User login with email/password validation
- `RegisterActivity` - New user registration with validation

### Guest Dashboard
- `GuestDashboardActivity` - Main menu for guests
- `RoomBookingActivity` - Room selection and booking
- `ServiceReservationActivity` - Service selection and reservation
- `OffersActivity` - View special offers
- `ProfileActivity` - User profile and booking history

### Admin Dashboard
- `AdminDashboardActivity` - Main menu for administrators
- `ManageRoomsActivity` - CRUD operations for rooms
- `ManageServicesActivity` - CRUD operations for services
- `ManageOffersActivity` - CRUD operations for offers
- `ViewBookingsActivity` - View all bookings

## 🔹 Key Features Implementation

### Authentication & Session Management
- Email format validation
- Password minimum length validation (6 characters)
- Duplicate email prevention
- SharedPreferences for session management
- Role-based navigation (Guest/Admin)

### Room Booking System
- Availability checking
- Date validation (checkout > checkin)
- Room selection with RecyclerView
- Booking confirmation

### Service Reservation
- Service availability checking
- Date-based reservations
- No duplicate reservations for same service/date

### Admin Management
- CRUD operations for all entities
- Input validation (non-empty fields, price > 0)
- Real-time data updates

## 🔹 Default Credentials

**Admin Account:**
- Email: `admin@luxevista.com`
- Password: `admin123`

**Guest Registration:**
- Guests can register with any valid email
- Default role is set to "guest"

## 🔹 Sample Data

The application comes with pre-loaded sample data:

### Rooms
- Deluxe Suite ($299.99)
- Ocean View ($199.99)
- Presidential Suite ($499.99)
- Standard Room ($149.99)

### Services
- Spa Treatment ($89.99)
- Fine Dining ($79.99)
- Pool Cabana ($59.99)
- City Tour ($49.99)

### Offers
- Summer Special (20% off all rooms)
- Weekend Getaway (Free breakfast included)
- Honeymoon Package (Romantic dinner for two)

## 🔹 Installation & Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd luxevista-resort
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Open the project folder
   - Wait for Gradle sync to complete

3. **Build and Run**
   - Connect an Android device or start an emulator
   - Click "Run" or use `Ctrl+R` (Windows/Linux) or `Cmd+R` (Mac)

## 🔹 Usage Instructions

### For Guests:
1. Register a new account or login
2. Browse and book available rooms
3. Reserve hotel services
4. View special offers
5. Check booking history in profile

### For Administrators:
1. Login with admin credentials
2. Manage rooms, services, and offers
3. View all bookings made by guests
4. Add new rooms and services as needed

## 🔹 Validation Rules

- **Email**: Must be valid email format
- **Password**: Minimum 6 characters
- **Room Booking**: Checkout date must be after checkin date
- **Service Reservation**: Date must be provided
- **Admin CRUD**: All fields must be non-empty, prices must be > 0

## 🔹 Dependencies

```gradle
implementation 'androidx.appcompat:appcompat:1.6.1'
implementation 'com.google.android.material:material:1.10.0'
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
implementation 'androidx.recyclerview:recyclerview:1.3.2'
implementation 'androidx.cardview:cardview:1.0.0'
```

## 🔹 Future Enhancements

- Payment integration
- Push notifications
- Image uploads for rooms/services
- Advanced search and filtering
- Multi-language support
- Offline mode support
- Real-time booking updates

## 🔹 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 🔹 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🔹 Support

For support and questions, please contact the development team or create an issue in the repository.

---

**LuxeVista Resort** - Your Gateway to Luxury Hospitality Management 🏨✨
