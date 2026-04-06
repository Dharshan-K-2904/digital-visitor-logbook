# Testing Guide for Member 3 Implementation

## Prerequisites
1. Ensure MySQL is running
2. Database `visitor_logbook` is created
3. Application is running on http://localhost:8080

## Step-by-Step Testing

### Step 1: Create Test Accounts

1. **Open browser and go to**: http://localhost:8080/register

2. **Create a VISITOR account:**
   - Name: Test Visitor
   - Email: visitor@test.com
   - Password: test123
   - Role: Visitor
   - Click Register

3. **Create a HOST account:**
   - Name: Test Host
   - Email: host@test.com
   - Password: test123
   - Role: Host
   - Click Register

4. **Create a SECURITY account:**
   - Name: Security Guard
   - Email: security@test.com
   - Password: test123
   - Role: Security Personnel
   - Click Register

### Step 2: Test Complete Workflow

#### 2.1 Create Visit Request (VISITOR Role)
1. Login as: visitor@test.com / test123
2. You'll be redirected to `/visitor` dashboard
3. Fill out visit request form:
   - Host Email: host@test.com
   - Purpose: Business Meeting
   - Click Submit

#### 2.2 Approve Visit Request (HOST Role)
1. Logout and login as: host@test.com / test123
2. You'll be redirected to `/host` dashboard
3. You should see the pending visit request
4. Click "Approve" button

#### 2.3 Check-In Visitor (SECURITY Role) - UC-04 ✅
1. Logout and login as: security@test.com / test123
2. You'll be redirected to `/security` dashboard
3. **Verify Dashboard Statistics:**
   - Pending Check-Ins: Should show 1
   - Currently Checked-In: Should show 0
   
4. **Locate the Approved Visitor:**
   - Look for "Test Visitor" in the table
   - Status should be "APPROVED" (green badge)
   - Check-In Time should show "-"
   
5. **Click "✅ Check In" button**
   
6. **Verify Check-In Success:**
   - Status changes to "CHECKED_IN" (blue badge)
   - Check-In Time now shows current date/time
   - Button changes to "🚪 Check Out"
   - Statistics update: Currently Checked-In = 1

#### 2.4 Check-Out Visitor (SECURITY Role) - UC-05 ✅
1. **Stay on security dashboard**
2. **Locate the Checked-In Visitor:**
   - Same visitor should now show "CHECKED_IN" status
   
3. **Click "🚪 Check Out" button**
   
4. **Verify Check-Out Success:**
   - Status changes to "CHECKED_OUT" (purple badge)
   - Check-Out Time now shows current date/time
   - Duration column shows visit duration in minutes
   - Alert popup shows "Visitor checked out successfully! Visit duration: X minutes"

### Step 3: Test Search and Filter Functionality

#### 3.1 Test Search by Visitor Name
1. On security dashboard, type in search box: "Test"
2. Click "Search" button
3. Verify: Only visitors with "Test" in their name appear

#### 3.2 Test Search by Host Email
1. Clear search, type: "host@test.com"
2. Click "Search" button
3. Verify: Only visitors with that host appear

#### 3.3 Test Status Filter
1. Select "Status" dropdown: "CHECKED_OUT"
2. Click "Search" button
3. Verify: Only CHECKED_OUT visitors appear

#### 3.4 Test Combined Search + Filter
1. Search box: "Test"
2. Status dropdown: "CHECKED_IN"
3. Click "Search" button
4. Verify: Only visitors matching BOTH criteria appear

#### 3.5 Test Clear Filters
1. Click "Clear" button
2. Verify: All visitors appear again

### Step 4: Test Edge Cases

#### 4.1 Prevent Duplicate Check-In
1. Create and approve another visit request
2. Check-in the visitor
3. Refresh the page
4. Try to check-in again (button should not appear)

#### 4.2 Check-Out Without Check-In
1. Create and approve a new visit request
2. Try to check-out without checking in first
3. Verify: Check-out button doesn't appear

#### 4.3 Empty Search Results
1. Search for: "NonExistentVisitor"
2. Verify: "No visitors found" message appears

## Expected Results Checklist

### UC-04: Visitor Check-In ✅
- [ ] Security personnel can view approved visitors
- [ ] Check-in button only appears for APPROVED status
- [ ] Check-in time is recorded automatically
- [ ] Status updates from APPROVED to CHECKED_IN
- [ ] Cannot check-in same visitor twice
- [ ] Statistics update in real-time

### UC-05: Visitor Check-Out ✅
- [ ] Check-out button only appears for CHECKED_IN status
- [ ] Check-out time is recorded automatically
- [ ] Visit duration is calculated correctly
- [ ] Duration is displayed in minutes
- [ ] Status updates from CHECKED_IN to CHECKED_OUT
- [ ] Cannot check-out visitor twice

### Search and Filter ✅
- [ ] Search by visitor name works (case-insensitive)
- [ ] Search by host email works (case-insensitive)
- [ ] Filter by status works (all status types)
- [ ] Combined search + filter works
- [ ] Clear button resets all filters
- [ ] "No visitors found" message for empty results

### UI/UX ✅
- [ ] Dashboard shows correct statistics
- [ ] Status badges have correct colors
- [ ] Check-in/check-out buttons appear conditionally
- [ ] Success messages appear after actions
- [ ] Table is responsive and readable
- [ ] Duration is displayed correctly

## Database Verification

Run these SQL queries in MySQL to verify data:

```sql
USE visitor_logbook;

-- View all visit requests
SELECT id, visitor_name, host_email, status, check_in_time, check_out_time, visit_duration 
FROM visit_requests;

-- View checked-in visitors
SELECT * FROM visit_requests WHERE status = 'CHECKED_IN';

-- View checked-out visitors with duration
SELECT visitor_name, check_in_time, check_out_time, visit_duration 
FROM visit_requests 
WHERE status = 'CHECKED_OUT';
```

## Troubleshooting

### Issue: Check-in button doesn't appear
- **Solution**: Ensure visit request is APPROVED by host first

### Issue: Duration shows NULL
- **Solution**: Ensure visitor was checked-in before checking-out

### Issue: Search returns no results
- **Solution**: Ensure visitor_name field is populated when creating requests

### Issue: 404 error on /security
- **Solution**: Ensure you registered as SECURITY role, not Security Personnel

## Success Criteria

✅ All UC-04 requirements implemented
✅ All UC-05 requirements implemented
✅ Search functionality working
✅ Filter functionality working
✅ Duration calculation accurate
✅ No duplicate check-ins/check-outs
✅ Responsive UI
✅ Proper validation

## Screenshots to Take for Documentation

1. Security dashboard with statistics
2. Check-in action in progress
3. Check-out action with duration
4. Search results
5. Filter results
6. Database records showing timestamps and duration

---

**Testing completed by**: Member 3
**Date**: 
**Status**: All tests passed ✅
