//package edu.dartmouth.cs.finalproject.activities;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.GestureDetector;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.HorizontalScrollView;
//import android.widget.LinearLayout;
//
//import java.util.ArrayList;
//
//import edu.dartmouth.cs.finalproject.R;
//
//public class SnapScrollView extends HorizontalScrollView {
//
//    private static final int SWIPE_MIN_DISTANCE = 5;
//    private static final int SWIPE_THRESHOLD_VELOCITY = 300;
//    private static final String TAG = "SnapScrollView";
//
//    private ArrayList<Button> mItems = null;
//    private GestureDetector mGestureDetector;
//    private int mActiveFeature = 0;
//    private LinearLayout linearLayout;
//
//
//    public SnapScrollView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//    }
//
//    public SnapScrollView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//    public SnapScrollView(Context context) {
//        super(context);
//    }
//
//    public void setFeatureItems(ArrayList<Button> items) {
//        //Create a linear layout to hold each screen in the scroll view
//        LinearLayout internalWrapper = new LinearLayout(getContext());
//
//
//        linearLayout = findViewById(R.id.linear_layout);
//
//        // get device screen width
//        int width = getScreenWidth(SnapScrollView.this);
//        Log.d(TAG, "screenwidth: " + width);
//
//        // size each button to the width of the screen
//        int child_count = linearLayout.getChildCount();
//        Log.d(TAG, "Child count: " + child_count);
//        for (int i=0; i<child_count; i++) {
//            Button button = (Button) linearLayout.getChildAt(i);
//            button.setWidth(width);
//        }
//
//        for(int i = 0; i < items.size(); i++){
//            //...
//            //Create the view for each screen in the scroll view
//            //...
//            internalWrapper.addView();
//        }
//    }
//
//    /*
//     * Helper function that gets the screen with in order to properly size each button
//     */
//    public static int getScreenWidth(Context context) {
//        WindowManager windowManager = (WindowManager) context
//                .getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics dm = new DisplayMetrics();
//        windowManager.getDefaultDisplay().getMetrics(dm);
//        return dm.widthPixels;
//    }
//}
