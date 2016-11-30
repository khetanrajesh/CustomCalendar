package com.rajesh.customcalendar;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.LocalDate;
import org.joda.time.chrono.ISOChronology;

import java.util.ArrayList;


/**
 * Created by rajesh on 28/03/16.
 */
public class CalendarFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener {


    int year;
    int month;
    String identifier;
    ArrayList<String> array;
    GridView gridview;
    OnItemClickedListener mListener = sDummyCallbacks;
    String role;
    Calendar calendar;
    int calendar_id;

    public CalendarFragment() {

    }

    public GridView getGridView() {
        return gridview;
    }

    public interface OnItemClickedListener {
        public void OnItemClicked(int position);
    }

    public static CalendarFragment newInstance(int year, int month, String role, String identifier) {
        CalendarFragment myFragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putInt("year", year);
        args.putInt("month", month);
        args.putString("role", role);
        args.putString("identifier", identifier);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources res = getResources();
        identifier =  getArguments().getString("identifier");
        if (identifier == null) {
            throw new ClassCastException("Calendar in " + getActivity().getLocalClassName() + " must have a identifier attribute");
        }
        calendar_id = res.getIdentifier(identifier, "id", getContext().getPackageName());
        calendar = (Calendar)getActivity().findViewById(calendar_id);
        year = getArguments().getInt("year", -1);
        month = getArguments().getInt("month", -1);
        role = getArguments().getString("role");
        mListener = calendar;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_month, container, false);

        array = new ArrayList<>();

        gridview = (GridView) v.findViewById(R.id.gridview);

        gridview.setBackgroundColor(calendar.getExpandedCalendarBackgroundColor());

        DateTime dt = new DateTime().withYear(year).withMonthOfYear(month);
        DateTime start = dt.withDayOfMonth(1).withTimeAtStartOfDay();
        DateTime end = start.plusMonths(1).minusMillis(1);

        LocalDate startDay = new LocalDate(start);
        LocalDate endDay = new LocalDate(end);


        for (int i = 0; i < 49; i++) {

            array.add(" ");
        }

        array.set(0, "S");
        array.set(1, "M");
        array.set(2, "T");
        array.set(3, "W");
        array.set(4, "T");
        array.set(5, "F");
        array.set(6, "S");

        int i = 0;

        switch (startDay.getDayOfWeek()) {

            case 7:
                i = 7;
                break;
            case 1:
                i = 8;
                break;
            case 2:
                i = 9;
                break;
            case 3:
                i = 10;
                break;
            case 4:
                i = 11;
                break;
            case 5:
                i = 12;
                break;
            case 6:
                i = 13;
                break;
        }

        int value = 1;
        for (int j = i; j < getDaysInMonth(year, month) + i; j++) {
            array.set(j, value++ + "");
        }
        CalendarMonthGridViewAdapter aa = new CalendarMonthGridViewAdapter(v.getContext(), R.layout.calendar_list_item, array, year, month, calendar);
        gridview.setAdapter(aa);
        gridview.setOnItemClickListener(this);
        return v;
    }


    public static int getDaysInMonth(int year, int month) {

        Chronology chrono = ISOChronology.getInstance();
        DateTimeField dayField = chrono.dayOfMonth();
        LocalDate monthDate = new LocalDate(year, month, 1);
        return dayField.getMaximumValue(monthDate);
    }


    private static OnItemClickedListener sDummyCallbacks = new OnItemClickedListener() {
        @Override
        public void OnItemClicked(int position) {
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        for (int i = 0; i < parent.getChildCount(); i++) {
            parent.getChildAt(i).setBackground(null);
        }
        try {
            int a = Integer.parseInt(array.get(position));
            Drawable mDrawable = ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.background_green_ring);
            mDrawable.setColorFilter(new PorterDuffColorFilter(calendar.getExpandedCalendarSelectedColor(), PorterDuff.Mode.MULTIPLY));

            int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackgroundDrawable(mDrawable);
            } else {
                view.setBackground(mDrawable);
            }
            mListener.OnItemClicked(a);

        } catch (Exception e) {

        }
    }


}


