package com.example.android.futurevision;


import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by Cosmin on 2/23/2016.
 */
public class year_goals extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private SQLiteDatabase db;
    private Cursor cursor;
    GoalListAdapter adapter;

    // newInstance constructor for creating fragment
    public static year_goals newInstance() {
        year_goals fragmentFirst = new year_goals();
        Bundle args = new Bundle();
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }
    public year_goals()
    {

    }
    @Override
    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.year_goals, container, false);
        // Inflate the layout for this fragment
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.year_goals_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SQLiteOpenHelper futureVisionDatabaseHelper = new FutureVisionDatabaseHelper(getActivity());
        ItemDivider itemDivider = new ItemDivider(getContext(),R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDivider);
        db = futureVisionDatabaseHelper.getReadableDatabase();
        cursor = db.query("YEAR_GOALS", new String[]{"_id", "GOAL"}, null, null, null, null, null);
        adapter = new GoalListAdapter(getContext(),cursor);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = FutureVisionDatabaseHelper.URI_TABLE_YEAR;
        return new CursorLoader(getContext(),uri,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }


    public class GoalListAdapter extends CursorRecyclerViewAdapter<GoalListViewHolder>  {


        public GoalListAdapter(Context context,Cursor cursor) {
            super(context,cursor);
        }


        @Override
        public GoalListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.row_goals, parent, false);
            return new GoalListViewHolder(view);
        }


        @Override
        public int getItemCount() {
            return (cursor!=null) ? cursor.getCount(): 0;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(GoalListViewHolder holder,Cursor cursor) {
            holder.TextCircle.setText(cursor.getString(0));
            holder.TextViewName.setText(cursor.getString(1));
            // taking a random color from the array of colors
            int[] androidColors = getResources().getIntArray(R.array.androidcolors);
            int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
            //creating a circle shape for the TextView
            ShapeDrawable background = new ShapeDrawable();
            background.setShape(new OvalShape());
            background.getPaint().setColor(randomAndroidColor);
            holder.TextCircle.setBackground(background);
            cursor.moveToNext();

        }

    }


    public class GoalListViewHolder extends RecyclerView.ViewHolder
    {   public TextView TextViewName;
        public TextView TextCircle;

        public GoalListViewHolder(View itemView) {
            super(itemView);
            TextViewName = (TextView) itemView.findViewById(R.id.text_viewid);
            TextCircle = (TextView) itemView.findViewById(R.id.circle_text);
            itemView.setClickable(true);
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        cursor.close();
        db.close();
    }

}
