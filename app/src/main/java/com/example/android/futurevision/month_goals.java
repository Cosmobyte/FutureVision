package com.example.android.futurevision;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by Cosmin on 2/21/2016.
 */
public class month_goals extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private SQLiteDatabase db;
    private Cursor cursor;
    GoalListAdapter adapter;
    //interface used for passing the recyclerView to the activity
    public interface HideBottomBarMonth {
        void hideViewsMonth(RecyclerView recyclerView);
    }
    HideBottomBarMonth hideViewsImp;

    public static month_goals newInstance() {
        month_goals fragmentFirst = new month_goals();
        Bundle args = new Bundle();
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }
    public month_goals()
    {

    }
    public void onAttach(Activity activity){
        super.onAttach(activity);
        Context context = getActivity();
        hideViewsImp =(HideBottomBarMonth)context;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.month_goals, container, false);
            final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.month_goals_recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            hideViewsImp.hideViewsMonth(recyclerView);
            ItemDivider itemDivider = new ItemDivider(getContext(),R.dimen.item_offset);
            recyclerView.addItemDecoration(itemDivider);
            SQLiteOpenHelper futureVisionDatabaseHelper = new FutureVisionDatabaseHelper(getActivity());
            db = futureVisionDatabaseHelper.getReadableDatabase();
            cursor = db.query("MONTH_GOALS", new String[]{"_id", "GOAL"}, null, null, null, null, null);
            adapter = new GoalListAdapter(getContext(),cursor);
            recyclerView.setAdapter(adapter);

            return view;

        }
    public void refreshFragment() {
        month_goals Month_goals = new month_goals();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.month_goals_layout, Month_goals);
        ft.commit();

    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = FutureVisionDatabaseHelper.URI_TABLE_MONTH;
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


    public class GoalListAdapter extends CursorRecyclerViewAdapter<GoalListViewHolder> implements View.OnLongClickListener {
        public String index;//get the current row index
        public String currentText;


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
        public void onBindViewHolder(GoalListViewHolder holder, Cursor cursor) {
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
            holder.itemView.setOnLongClickListener(this);
            cursor.moveToNext();

        }

        @Override
        public boolean onLongClick(View v) {
            //getting the index and the text of the current goal
            GoalListViewHolder vh = new GoalListViewHolder(v);
            currentText = (String) vh.TextViewName.getText();
            index = (String) vh.TextCircle.getText();
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("EDIT OR DELETE A GOAL");
            builder.setItems(R.array.dialog_items,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    SQLiteOpenHelper futureVisionDatabaseHelper = new FutureVisionDatabaseHelper(getActivity());
                                    db = futureVisionDatabaseHelper.getReadableDatabase();
                                    db.delete("MONTH_GOALS", "_id=?", new String[]{index});
                                    adapter.notifyDataSetChanged();
                                    //refresh the fragment
                                    refreshFragment();
//                                    db.close();
                                    break;
                                case 1:
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                                    alertDialog.setTitle("EDIT");
                                    final EditText input = new EditText(getContext());
                                    input.append(currentText);
                                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT);
                                    input.setLayoutParams(lp);
                                    alertDialog.setView(input);
                                    // Setting Positive "YES" Button
                                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            SQLiteOpenHelper futureVisionDatabaseHelper = new FutureVisionDatabaseHelper(getActivity());
                                            db = futureVisionDatabaseHelper.getWritableDatabase();
                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put("GOAL", input.getText().toString());
                                            db.update("MONTH_GOALS", contentValues, "_id=?", new String[]{index});
                                            //refresh the fragment
                                            refreshFragment();
                                        }
                                    });
                                    // Setting Negative "NO" Button
                                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int which){
                                            dialog.cancel();
                                        }

                                    });
                                    alertDialog.create().show(); // display the AlertDialog
                                    break;
                            }
                        }
                    }
            );
            // set the AlertDialog's negative Button
            builder.setNegativeButton(getString(R.string.cancel), null);

            builder.create().show(); // display the AlertDialog
            return true;
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
