package com.example.schedule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class ScheduleFragment extends Fragment {
    RecyclerView recyclerView;
    HomeActivity home;
    TextView selectedYearTxt;
    ImageView arrow;
    CompactCalendarView compactCalendarView;
    LinearLayout calenderHeader;
    TextView labelSelectedDay;

    SimpleDateFormat shortDayFormat;
    SimpleDateFormat longDayFormat;
    SimpleDateFormat monthYearFormat;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        home = (HomeActivity) getActivity();

        shortDayFormat = new SimpleDateFormat("d MMM");
        longDayFormat = new SimpleDateFormat("d MMMM");
        monthYearFormat = new SimpleDateFormat("MMMM yyyy");

        Calendar cal = Calendar.getInstance();

        arrow = view.findViewById(R.id.arrowCalenderView);
        selectedYearTxt = view.findViewById(R.id.labelCalenderView);

        selectedYearTxt.setText(monthYearFormat.format(cal.getTime()));

        calenderHeader = view.findViewById(R.id.layoutCalenderTxt);
        calenderHeader.setOnClickListener(new calenderHeaderListener());

        labelSelectedDay = view.findViewById(R.id.txtCalenderView);
        labelSelectedDay.setText(longDayFormat.format(cal.getTime()));

        recyclerView = view.findViewById(R.id.shiftRecyclerView);
        recyclerView.setAdapter(getAdapter(shortDayFormat.format(cal.getTime())));
        recyclerView.setLayoutManager(new LinearLayoutManager(home));

        compactCalendarView = view.findViewById(R.id.compactcalendar_view);
        compactCalendarView.setListener(new dateChangeListener());

        setEvents(home.getComingShifts());

        return view;
    }

    /**
     * En klass som implementerar interfacet View.OnClickListener
     */
    public class calenderHeaderListener implements View.OnClickListener{
        /** Överskriver interfacets onClick metod som innefattar toggling av kalenderns synlighet
         * @param view vyn som var tryckt.
         */
        @Override
        public void onClick(View view) {
            if (compactCalendarView.getVisibility() != View.VISIBLE) {
                compactCalendarView.setVisibility(View.VISIBLE);
                arrow.animate().rotation(180).start();
            } else {
                compactCalendarView.setVisibility(View.GONE);
                arrow.animate().rotation(0).start();
            }
        }
    }

    /**
     * En funktion som skapar en adapter beroende på utvalt datum
     * @param formattedDate det utvalda datumet
     */
    private ShiftRecyclerViewAdapter getAdapter(String formattedDate){
        return new ShiftRecyclerViewAdapter(home, home.getShiftsAtDate(formattedDate));
    }

    /**
     * En klass som implementerar interfacet CompactCalendarView.CompactCalendarViewListener
     */
    public class dateChangeListener implements CompactCalendarView.CompactCalendarViewListener{
        /** Överskriver interfacets onDayClick metod som innefattar uppdatering av datumets skift och rubrik
         * @param dateClicked datum som var tryckt.
         */
        @Override
        public void onDayClick(Date dateClicked) {
            labelSelectedDay.setText(longDayFormat.format(dateClicked));

            String date = shortDayFormat.format(dateClicked);
            recyclerView.setAdapter(getAdapter(date));
            recyclerView.setLayoutManager(new LinearLayoutManager(home));
        }

        /** Överskriver interfacets onMonthScroll metod som innefattar uppdatering av rubriken ovanför kalendern
         * @param firstDayOfNewMonth datum som var tryckt.
         */
        @Override
        public void onMonthScroll(Date firstDayOfNewMonth) {
            selectedYearTxt.setText(monthYearFormat.format(firstDayOfNewMonth));
        }
    }

    /**
     * En funktion som sätter Event på kalendern
     * @param shifts skift som servitören har i framtiden
     */
    private void setEvents(ArrayList<Shift> shifts){
        Calendar c;
        int color;
        for(Shift s : shifts){
            c = Calendar.getInstance();
            c.set(s.getDate(Calendar.YEAR), s.getDate(Calendar.MONTH), s.getDate(Calendar.DAY_OF_MONTH));
            if(s.isLate()){
                color = getResources().getColor(R.color.lateShift);
            }else{
                color = getResources().getColor(R.color.earlyShift);
            }
            Event e = new Event(color, c.getTimeInMillis());
            compactCalendarView.addEvent(e);
        }
    }
}