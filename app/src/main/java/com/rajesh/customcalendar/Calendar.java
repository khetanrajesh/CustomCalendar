package com.rajesh.customcalendar;

/**
 * Created by rajeshkhetan on 11/09/16.
 */


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.Days;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.Months;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Calendar extends LinearLayout implements CalendarFragment.OnItemClickedListener {

    private List<CalendarFragment> fragments;
    private List<LocalDate> dates;
    private int startYear;
    private int endYear;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ViewPager pager;
    private TextView tv_month_year, tv_today;
    private CalendarPagerAdapter pageAdapter;
    public LocalDate startDate, endDate, todayDate;
    boolean changeDetected = false;
    boolean scrollPager = true;
    boolean clicked = false;
    private int monthYearTextColor, monthYearBackgroundColor, todayTextColor, todayBackgroundColor, expandedCalendarBackgroundColor,
            expandedCalendarTextColor, expandedCalendarSelectedColor, calendarBackground, calendarTextColor, calendarSelectedColor, eventDotColor;
    private LocalDate selected;
    public CalendarListener calendarListener = null;
    private ArrayList<LocalDate> eventDates;
    String identifier;


    public interface CalendarListener {
        public void onExpand();

        public void onCollapse();

        public void onDateChange(LocalDate date);

        public void onTodayClick();
    }


    public CalendarListener getCalendarListener() {
        return calendarListener;
    }

    public void setCalendarListener(CalendarListener calendarListener) {
        this.calendarListener = calendarListener;
    }

    public void addEvent(LocalDate date) {
        if (!eventDates.contains(date)) {
            eventDates.add(date);
            pageAdapter.notifyDataSetChanged();
        }
    }

    public void deleteEvent(LocalDate date) {
        if (eventDates.contains(date)) {
            eventDates.remove(date);
            pageAdapter.notifyDataSetChanged();
        }
    }

    public int getEventDotColor() {
        return eventDotColor;
    }

    public void setEventDotColor(int eventDotColor) {
        this.eventDotColor = eventDotColor;
        pageAdapter.notifyDataSetChanged();
    }

    public int getMonthYearTextColor() {
        return monthYearTextColor;
    }

    public void setMonthYearTextColor(int monthYearTextColor) {
        this.monthYearTextColor = monthYearTextColor;
        tv_month_year.setTextColor(monthYearTextColor);
    }

    public int getMonthYearBackgroundColor() {
        return monthYearBackgroundColor;
    }

    public void setMonthYearBackgroundColor(int monthYearBackgroundColor) {
        this.monthYearBackgroundColor = monthYearBackgroundColor;
        tv_month_year.setBackgroundColor(monthYearBackgroundColor);
    }

    public int getExpandedCalendarSelectedColor() {
        return expandedCalendarSelectedColor;
    }

    public void setExpandedCalendarSelectedColor(int expandedCalendarSelectedColor) {
        this.expandedCalendarSelectedColor = expandedCalendarSelectedColor;
        pageAdapter.notifyDataSetChanged();
    }

    public int getExpandedCalendarTextColor() {
        return expandedCalendarTextColor;
    }

    public void setExpandedCalendarTextColor(int expandedCalendarTextColor) {
        this.expandedCalendarTextColor = expandedCalendarTextColor;
        pageAdapter.notifyDataSetChanged();
    }

    public int getExpandedCalendarBackgroundColor() {
        return expandedCalendarBackgroundColor;
    }

    public void setExpandedCalendarBackgroundColor(int expandedCalendarBackgroundColor) {
        this.expandedCalendarBackgroundColor = expandedCalendarBackgroundColor;
        pageAdapter.notifyDataSetChanged();
    }

    public int getTodayBackgroundColor() {
        return todayBackgroundColor;
    }

    public void setTodayBackgroundColor(int todayBackgroundColor) {
        this.todayBackgroundColor = todayBackgroundColor;
        tv_today.setBackgroundColor(todayBackgroundColor);
    }

    public int getTodayTextColor() {
        return todayTextColor;
    }

    public void setTodayTextColor(int todayTextColor) {
        this.todayTextColor = todayTextColor;
        tv_today.setTextColor(todayTextColor);
    }

    public int getCalendarBackground() {
        return calendarBackground;
    }

    public void setCalendarBackground(int calendarBackground) {
        this.calendarBackground = calendarBackground;
        mAdapter.notifyDataSetChanged();
    }

    public int getCalendarTextColor() {
        return calendarTextColor;
    }

    public void setCalendarTextColor(int calendarTextColor) {
        this.calendarTextColor = calendarTextColor;
        mAdapter.notifyDataSetChanged();

    }

    public int getCalendarSelectedColor() {
        return calendarSelectedColor;
    }

    public void setCalendarSelectedColor(int calendarSelectedColor) {
        this.calendarSelectedColor = calendarSelectedColor;
        mAdapter.notifyDataSetChanged();
    }

    public LocalDate getTodayDate() {
        return todayDate;
    }

    public LocalDate getSelected() {
        return selected;
    }

    public ArrayList<LocalDate> getEventDates() {
        return eventDates;
    }

    public void setEventDates(ArrayList<LocalDate> eventDates) {
        if (eventDates != null) {
            this.eventDates = eventDates;
        } else {
            this.eventDates.clear();
        }
    }

    private static CalendarFragment.OnItemClickedListener callback = new CalendarFragment.OnItemClickedListener() {
        @Override
        public void OnItemClicked(int date) {
            OnItemClicked(date);
        }
    };

    public Calendar(Context context) {
        super(context);
        initControl(context);
    }

    public Calendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CalendarDateElement);
        identifier = a.getString(R.styleable.CalendarDateElement_identifier);
        startYear = a.getInteger(R.styleable.CalendarDateElement_startYear, 1900);
        endYear = a.getInteger(R.styleable.CalendarDateElement_endYear, 2101);
        startDate = new LocalDate(startYear, 1, 1);
        endDate = new LocalDate(endYear, 12, 31);
        todayDate = new LocalDate();
        monthYearTextColor = a.getColor(R.styleable.CalendarDateElement_monthYearTextColor, ContextCompat.getColor(context, android.R.color.white));
        monthYearBackgroundColor = a.getColor(R.styleable.CalendarDateElement_monthYearBackgroundColor, ContextCompat.getColor(context, R.color.fuchsia_pink));
        todayTextColor = a.getColor(R.styleable.CalendarDateElement_todayTextColor, ContextCompat.getColor(context, android.R.color.white));
        todayBackgroundColor = a.getColor(R.styleable.CalendarDateElement_todayBackgroundColor, ContextCompat.getColor(context, R.color.de_york));
        expandedCalendarBackgroundColor = a.getColor(R.styleable.CalendarDateElement_expandedCalendarBackgroundColor, ContextCompat.getColor(context, android.R.color.holo_blue_light));
        expandedCalendarTextColor = a.getColor(R.styleable.CalendarDateElement_expandedCalendarTextColor, ContextCompat.getColor(context, android.R.color.white));
        expandedCalendarSelectedColor = a.getColor(R.styleable.CalendarDateElement_expandedCalendarSelectedColor, ContextCompat.getColor(context, R.color.flat_sunflower));
        calendarBackground = a.getColor(R.styleable.CalendarDateElement_calendarBackground, ContextCompat.getColor(context, android.R.color.holo_green_dark));
        calendarTextColor = a.getColor(R.styleable.CalendarDateElement_calendarTextColor, ContextCompat.getColor(context, android.R.color.white));
        calendarSelectedColor = a.getColor(R.styleable.CalendarDateElement_calendarSelectedColor, ContextCompat.getColor(context, R.color.flat_sunflower));
        eventDotColor = a.getColor(R.styleable.CalendarDateElement_eventDotColor, ContextCompat.getColor(context, R.color.cornflower_lilac));
        initControl(context);
    }

    public Calendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context);
    }

    private void initControl(Context context) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.custom_calendar, this);

        eventDates = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        pager = (ViewPager) findViewById(R.id.viewpager);
        tv_month_year = (TextView) findViewById(R.id.monthYear);
        tv_today = (TextView) findViewById(R.id.date);
        tv_month_year.setTextColor(monthYearTextColor);
        tv_month_year.setBackgroundColor(monthYearBackgroundColor);
        mRecyclerView.setBackgroundColor(expandedCalendarBackgroundColor);
        tv_today.setTextColor(todayTextColor);
        tv_today.setBackgroundColor(todayBackgroundColor);
        if ((todayDate.isBefore(endDate) && todayDate.isAfter(startDate) || todayDate.isEqual(startDate) || todayDate.isEqual(endDate))) {
            tv_today.setVisibility(VISIBLE);
        }else{
            tv_today.setVisibility(INVISIBLE);
        }
        setUpPager(context);
        setUpRecyclerView(context);
        setUpListeners(context);
    }

    private void setUpListeners(final Context context) {
        tv_month_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pager.getVisibility() == View.VISIBLE) {
                    pager.setVisibility(View.GONE);
                    if (calendarListener != null) {
                        calendarListener.onCollapse();
                    }
                    tv_month_year.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down_black_24dp, 0);
                } else {
                    String[] date = tv_month_year.getText().toString().split(",");
                    LocalDate currentSelectedDate = new LocalDate(date[1].trim() + "-" + getMonthValue(date[0].trim()) + "-" + "01");
                    int months = Months.monthsBetween(startDate, currentSelectedDate).getMonths();
                    changeDetected = true;
                    pager.setCurrentItem(months);
                    pager.setVisibility(View.VISIBLE);
                    tv_month_year.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_up_black_24dp, 0);
                    changeDetected = false;
                    if (calendarListener != null) {
                        calendarListener.onExpand();
                    }
                }
            }
        });
        tv_today.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                clicked = false;
                setMonthYearText(getMonthName(todayDate.getMonthOfYear()), "" + todayDate.getYear());
                if (pager.getVisibility() == View.VISIBLE) {
                    pager.setCurrentItem(Months.monthsBetween(startDate, todayDate).getMonths());
                }
                LocalDate t = new LocalDate();
                if (selected != null && !selected.isEqual(t)) {
                    selected = t;
                    if (calendarListener != null) {
                        calendarListener.onDateChange(selected);
                    }
                } else if (selected == null) {
                    selected = t;
                }
                if (calendarListener != null) {
                    calendarListener.onTodayClick();
                }
                mAdapter.notifyDataSetChanged();
                mLayoutManager.scrollToPositionWithOffset(Days.daysBetween(startDate, todayDate).getDays() - 1, 0);
                String s = todayDate.getDayOfMonth() + "";
                highlightDate(fragments.get(pager.getCurrentItem()), todayDate.getDayOfMonth() + "", context);
                return true;
            }
        });
    }

    private void setUpPager(Context context) {

        FragmentActivity activity = (FragmentActivity) context;
        fragments = new ArrayList<>();
        for (int i = startYear; i <= endYear; i++) {
            for (int j = 1; j <= 12; j++) {
                fragments.add(CalendarFragment.newInstance(i, j, "client", identifier));
            }
        }
        pageAdapter = new CalendarPagerAdapter(activity.getSupportFragmentManager(), fragments);
        pager.setAdapter(pageAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                          @Override
                                          public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                          }

                                          @Override
                                          public void onPageSelected(int position) {
                                              if (!changeDetected) {
                                                  position = position + 1;
                                                  int year = position / 12;
                                                  int month = position % 12;
                                                  if (month == 0) {
                                                      year = year - 1;
                                                      month = 12;
                                                  }
                                                  year = startYear + year;
                                                  setMonthYearText(getMonthName(month), "" + year);
                                                  LocalDate s = new LocalDate(year + "-" + month + "-" + "01");
                                                  int numberOfdays = Days.daysBetween(startDate, s).getDays();
                                                  mLayoutManager.scrollToPositionWithOffset(numberOfdays - 1, 0);
                                              }
                                          }

                                          @Override
                                          public void onPageScrollStateChanged(int state) {
                                          }
                                      }
        );
        pager.setVisibility(View.GONE);
        tv_month_year.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down_black_24dp, 0);
    }


    private void setUpRecyclerView(final Context context) {
        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        dates = new ArrayList<>();
        int days = Days.daysBetween(startDate, endDate).getDays();
        for (int i = 0; i < days; i++) {
            LocalDate d = startDate.withFieldAdded(DurationFieldType.days(), i);
            dates.add(d);
        }
        mAdapter = new CalendarRecyclerViewAdapter(dates, this);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager.scrollToPositionWithOffset(Days.daysBetween(startDate, todayDate).getDays() - 1, 0);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        selected = new LocalDate(dates.get(position).getYear() + "-" + dates.get(position).getMonthOfYear() + "-" + dates.get(position).getDayOfMonth());
                        mAdapter.notifyDataSetChanged();
                        mLayoutManager.scrollToPositionWithOffset(position - 1, 0);
                        pageAdapter.notifyDataSetChanged();
                        if (calendarListener != null) {
                            calendarListener.onDateChange(selected);
                        }
                        clicked = true;
                    }
                })
        );
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (scrollPager) {
                    int position = mLayoutManager.findFirstVisibleItemPosition() + 1;
                    LocalDate d = dates.get(position);
                    int year = d.getYear();
                    int month = d.getMonthOfYear();
                    setMonthYearText(getMonthName(month), "" + year);
                    changeDetected = true;
                    pager.setCurrentItem(Months.monthsBetween(startDate, d).getMonths());
                    if (clicked) {
                        highlightDate(fragments.get(Months.monthsBetween(startDate, d).getMonths()), "" + d.getDayOfMonth(), context);
                        clicked = false;
                    }
                    changeDetected = false;
                } else {
                    scrollPager = true;
                }
            }
        });
    }


    private String getMonthName(int month) {

        switch (month) {

            case 1:
                return "Jan";

            case 2:
                return "Feb";

            case 3:
                return "Mar";

            case 4:
                return "Apr";

            case 5:
                return "May";

            case 6:
                return "Jun";

            case 7:
                return "Jul";

            case 8:
                return "Aug";

            case 9:
                return "Sep";

            case 10:
                return "Oct";

            case 11:
                return "Nov";

            case 12:
                return "Dec";
        }

        return null;
    }

    private void highlightDate(CalendarFragment f, String date, Context context) {
        Drawable mDrawable = ContextCompat.getDrawable(context, R.drawable.background_green_ring);
        mDrawable.setColorFilter(new PorterDuffColorFilter(getExpandedCalendarSelectedColor(), PorterDuff.Mode.MULTIPLY));
        GridView v = f.getGridView();
        if (v != null) {
            for (int i = 0; i < v.getChildCount(); i++) {
                LinearLayout l = (LinearLayout) v.getChildAt(i);
                TextView v2 = (TextView) l.getChildAt(0);
                if (v2.getText().toString().equalsIgnoreCase(date)) {
                    l.setBackground(mDrawable);
                } else {
                    v.getChildAt(i).setBackground(null);
                }
            }
        }
    }

    @Override
    public void OnItemClicked(int date) {
        scrollPager = false;
        String[] cdate = tv_month_year.getText().toString().split(",");
        LocalDate currentSelectedDate = new LocalDate(cdate[1].trim() + "-" + getMonthValue(cdate[0].trim()) + "-" + date);
        selected = currentSelectedDate;
        mAdapter.notifyDataSetChanged();
        mLayoutManager.scrollToPositionWithOffset(Days.daysBetween(startDate, currentSelectedDate).getDays() - 1, 0);
        pager.setVisibility(View.GONE);
        tv_month_year.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down_black_24dp, 0);
//        pageAdapter.notifyDataSetChanged();
        if (calendarListener != null) {
            calendarListener.onDateChange(currentSelectedDate);
        }
    }

    private void setMonthYearText(String month, String year) {
        tv_month_year.setText(month + ", " + year);
    }

    private static int getMonthValue(String month) {
        try {
            Date date = new SimpleDateFormat("MMMM").parse(month);
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(date);
            return (cal.get(java.util.Calendar.MONTH) + 1);
        } catch (ParseException p) {
            return 1;
        }
    }

}
