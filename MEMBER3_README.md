# Member 3 Implementation - Digital Visitor Logbook

## Responsibilities Completed

### Major Responsibility: Visitor Check-In and Check-Out Logging ✅

#### UC-04: Visitor Check-In and Entry Verification
- **Implementation**: `SecurityController.checkIn()` method
- **Features**:
  - Security personnel can verify visitor approval status
  - Records check-in time automatically using `LocalDateTime.now()`
  - Updates visitor status from "APPROVED" to "CHECKED_IN"
  - Validates that visitor is approved before allowing check-in
  - Prevents duplicate check-ins

#### UC-05: Visitor Check-Out and Duration Logging
- **Implementation**: `SecurityController.checkOut()` method
- **Features**:
  - Security personnel can log visitor exit
  - Records check-out time automatically
  - Calculates visit duration in minutes using `Duration.between()`
  - Updates visitor status to "CHECKED_OUT"
  - Validates that visitor is checked-in before allowing check-out

### Minor Responsibility: Visitor Search and Filtering ✅

#### Search Functionality
- **Implementation**: `VisitorRepository.searchAndFilter()` method
- **Features**:
  - Search by visitor name (case-insensitive, partial match)
  - Search by host email (case-insensitive, partial match)
  - Filter by status (PENDING, APPROVED, REJECTED, CHECKED_IN, CHECKED_OUT)
  - Combined search and filter capability
  - Real-time search through UI

## Files Created/Modified

### Backend Files
1. **SecurityController.java** (NEW)
   - `/security` - Security dashboard
   - `/security/check-in/{id}` - Check-in endpoint
   - `/security/check-out/{id}` - Check-out endpoint
   - `/security/visitor/{id}` - View visitor details

2. **VisitorRequest.java** (MODIFIED)
   - Added `visitorName` field for searching
   - Added `checkInTime` field for entry tracking
   - Added `checkOutTime` field for exit tracking
   - Added `visitDuration` field for duration calculation

3. **VisitorRepository.java** (MODIFIED)
   - Added `findByStatus()` for status filtering
   - Added `findByVisitorNameContainingIgnoreCase()` for name search
   - Added `findApprovedAndNotCheckedIn()` for pending check-ins
   - Added `findCheckedInVisitors()` for currently checked-in
   - Added `searchAndFilter()` for combined search/filter

4. **VisitorService.java** (MODIFIED)
   - Added `checkIn()` method implementing UC-04
   - Added `checkOut()` method implementing UC-05
   - Added `getPendingCheckIns()` for dashboard stats
   - Added `getCurrentlyCheckedIn()` for dashboard stats
   - Added `searchVisitors()` for search functionality

5. **AuthController.java** (MODIFIED)
   - Added SECURITY role routing
   - Added ADMIN role routing

### Frontend Files
1. **security-dashboard.html** (NEW)
   - Statistics cards showing pending and checked-in counts
   - Search bar for visitor name/host email
   - Status filter dropdown
   - Visitor table with all information
   - Check-in button (only for APPROVED visitors)
   - Check-out button (only for CHECKED_IN visitors)
   - Visit duration display

2. **register.html** (MODIFIED)
   - Added SECURITY role option
   - Added ADMIN role option

### Database Files
1. **schema.sql** (MODIFIED)
   - Added `visitor_name` column
   - Added `check_in_time` column (TIMESTAMP NULL)
   - Added `check_out_time` column (TIMESTAMP NULL)
   - Added `visit_duration` column (INT NULL)

## Design Patterns Implemented

### 1. DAO Pattern (Data Access Object)
- **Where**: `VisitorRepository` interface
- **Purpose**: Abstracts database operations
- **Benefit**: Separates business logic from data access

### 2. MVC Pattern (Model-View-Controller)
- **Model**: `VisitorRequest.java` - Data structure
- **View**: `security-dashboard.html` - User interface
- **Controller**: `SecurityController.java` - Business logic
- **Benefit**: Clear separation of concerns

### 3. Single Responsibility Principle
- **SecurityController**: Only handles security-related operations
- **VisitorService**: Only handles visitor business logic
- **VisitorRepository**: Only handles data access
- **Benefit**: Each class has one reason to change

### 4. Dependency Inversion Principle
- **Implementation**: Using `@Autowired` for dependency injection
- **SecurityController** depends on `VisitorService` interface, not implementation
- **Benefit**: Loose coupling, easier testing

## Testing Checklist

### Manual Testing Steps

1. **Create Test Users**
   ```
   - Register as SECURITY role
   - Register as VISITOR role
   - Register as HOST role
   ```

2. **Test Check-In Flow (UC-04)**
   - Login as VISITOR → Create visit request
   - Login as HOST → Approve the request
   - Login as SECURITY → Navigate to /security
   - Find the approved visitor
   - Click "Check In" button
   - Verify status changes to "CHECKED_IN"
   - Verify check-in time is recorded

3. **Test Check-Out Flow (UC-05)**
   - Find a checked-in visitor
   - Click "Check Out" button
   - Verify status changes to "CHECKED_OUT"
   - Verify check-out time is recorded
   - Verify visit duration is calculated

4. **Test Search Functionality**
   - Search by visitor name
   - Search by host email
   - Filter by status
   - Combine search and filter

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/security` | Security dashboard with search/filter |
| POST | `/security/check-in/{id}` | Check-in a visitor (UC-04) |
| POST | `/security/check-out/{id}` | Check-out a visitor (UC-05) |
| GET | `/security/visitor/{id}` | View visitor details |

## Database Schema Changes

```sql
ALTER TABLE visit_requests 
ADD COLUMN visitor_name VARCHAR(100),
ADD COLUMN check_in_time TIMESTAMP NULL,
ADD COLUMN check_out_time TIMESTAMP NULL,
ADD COLUMN visit_duration INT NULL;
```

## Key Features Implemented

✅ Visitor check-in with approval verification
✅ Visitor check-out with duration calculation
✅ Real-time duration calculation in minutes
✅ Search by visitor name (case-insensitive)
✅ Search by host email (case-insensitive)
✅ Filter by status (all status types)
✅ Combined search and filter
✅ Statistics dashboard (pending check-ins, currently checked-in)
✅ Responsive UI with color-coded status badges
✅ Validation to prevent duplicate check-ins/check-outs
✅ Role-based access control for security personnel

## Technologies Used

- **Backend**: Java, Spring Boot, Spring MVC, Spring Data JPA
- **Frontend**: Thymeleaf, HTML5, CSS3
- **Database**: MySQL
- **Build Tool**: Maven

## Future Enhancements (Optional)

1. **Observer Pattern for Notifications**
   - Notify hosts when visitor checks in
   - Notify admins of security events

2. **QR Code Generation**
   - Generate QR codes for approved visitors
   - Scan QR code for quick check-in

3. **Photo Capture**
   - Capture visitor photo during check-in
   - Store for security records

4. **Export Reports**
   - Export visitor logs to CSV/PDF
   - Generate daily/weekly reports

5. **Real-time Dashboard**
   - WebSocket integration
   - Live updates without refresh

## Contact

**Member 3** - Visitor Check-In/Check-Out Logging & Search/Filtering
