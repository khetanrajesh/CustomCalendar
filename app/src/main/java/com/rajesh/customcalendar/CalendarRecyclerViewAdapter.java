package com.rajesh.customcalendar;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.util.List;


public class CalendarRecyclerViewAdapter extends RecyclerView.Adapter<CalendarRecyclerViewAdapter.ViewHolder> {
    private List<LocalDate> mDataset;
    Calendar calendar;

    static Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView date, day;
        View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;

            this.date = (TextView) view
                    .findViewById(R.id.date);
            this.day = (TextView) view
                    .findViewById(R.id.day);

        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CalendarRecyclerViewAdapter(List<LocalDate> myDataset, Calendar calendar) {

        mDataset = myDataset;
        this.calendar = calendar;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_recycler_view_list_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        context = parent.getContext();
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        GradientDrawable bgShape = (GradientDrawable) holder.view.getBackground();
        bgShape.setColor(calendar.getCalendarBackground());

        holder.date.setText("" + mDataset.get(position).getDayOfMonth());
        holder.date.setTextColor(calendar.getCalendarTextColor());
        holder.day.setTextColor(calendar.getCalendarTextColor());
        int day = mDataset.get(position).getDayOfWeek();

        if (calendar.getSelected() != null) {

            if ((calendar.getSelected().getDayOfYear() == mDataset.get(position).getDayOfYear() && calendar.getSelected().getYear() == mDataset.get(position).getYear())) {

                holder.date.setTextColor(calendar.getCalendarSelectedColor());
                holder.day.setTextColor(calendar.getCalendarSelectedColor());
            }

        }


        if (day == 1) {

            holder.day.setText("Mon");

        } else if (day == 2) {

            holder.day.setText("Tue");

        } else if (day == 3) {

            holder.day.setText("Wed");

        } else if (day == 4) {

            holder.day.setText("Thu");

        } else if (day == 5) {

            holder.day.setText("Fri");

        } else if (day == 6) {

            holder.day.setText("Sat");

        } else if (day == 7) {

            holder.day.setText("Sun");

        }
    }

//    public LocalDate getSeleteced() {
//
//        return selected;
//    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}