# CustomCalendar

A customizable calendar android.

![](http://gph.is/2gVLwSF)

## Installation

Add the dependency to your build.gradle 
  ```
  compile 'com.rajesh.customcalendar:app:1.0.1'
  ```
## Usage
The simplest way to add calendar to your app:

```
  <com.rajesh.customcalendar.Calendar
        android:id="@+id/customCalendar"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        />
 ```
 ### Using Attributes 
 ```
  <?xml version="1.0" encoding="utf-8"?>
  <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">
     <com.rajesh.customcalendar.Calendar
        app:startYear="2006"
        app:endYear="2017"
        app:monthYearTextColor="@android:color/black"
        app:monthYearBackgroundColor="@android:color/holo_green_dark"
        app:todayTextColor="@android:color/holo_green_dark"
        app:todayBackgroundColor="@android:color/black"
        app:expandedCalendarBackgroundColor="@android:color/holo_green_dark"
        app:expandedCalendarTextColor="@android:color/holo_blue_dark"
        app:expandedCalendarSelectedColor="@android:color/holo_red_dark"
        app:calendarBackground="@android:color/holo_green_dark"
        app:calendarTextColor="@android:color/holo_purple"
        app:calendarSelectedColor="@android:color/white"
        app:eventDotColor="@android:color/white"
        android:id="@+id/customCalendar"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        />
 </LinearLayout>
 ```
 ### Methods 
 
 - `getTodayDate()` - Returns today's date
 - `getSelected()` - Returns current selected date
 - `setEventDates(ArrayList<LocalDate>)` - Add events to a list of dates
 - `addEvent(LocalDate)` - Add  event to a date
 - `deleteEvent(LocalDate)` - DeleteEvent from a date
 - `getEventDates` - Returns array of dates which has events.
 - `deleteEvent(LocalDate)` - DeleteEvent from a date
 - `deleteEvent(LocalDate)` - DeleteEvent from a date
 
  #### Listen to Calendar Events  
  ```
   calendarInstance.setCalendarListener(new Calendar.CalendarListener() {
            @Override
            public void onExpand() {
             //called on calendar expand
            }
            @Override
            public void onCollapse() {
            //called on calendar collapse
            }
            @Override
            public void onDateChange(LocalDate date) {
            //called when calendar selected date changes
            }
            @Override
            public void onTodayClick() {
            //when today button clicked
            }
        });
   ```
 
 ####Settings calendar colors 
  
 - setEventDotColor(int color)
 - setMonthYearTextColor(int color)
 - setMonthYearBackgroundColor(int color)
 - setExpandedCalendarSelectedColor(int color)
 - setExpandedCalendarTextColor(int color)
 - setExpandedCalendarBackgroundColor(int color)
 - setTodayBackgroundColor(int color)
 - setTodayTextColor(int color)
 - setCalendarBackground(int color)
 - setCalendarTextColor(int color)
 - setCalendarSelectedColor(int color)
  
  
### Questions?
Feel free to contact me in [twitter](https://twitter.com/khetanrajesh) or [create an issue](https://github.com/khetanrajesh/CustomCalendar/issues/new)  
  
 
