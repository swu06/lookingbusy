package com.lookingdynamic.lookingbusy;

import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

/**
 * This class handles the menu labels and icons to create the expected view for the game.
 * Created by swu on 9/10/2015.
 */
public class ArrayAdapterWithIcons extends ArrayAdapter<String> {

    private List<Integer> images;

    public ArrayAdapterWithIcons(Context context, int layout, String[] items, Integer[] images) {
        super(context, layout, items);
        this.images = Arrays.asList(images);
    }

    /*
     * This method handles adding an icon to each of the textView objects that make up a menu.
     * There are two different methods used for this depending on the SDK version for backwards
     * compatibility
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        if (Build.VERSION.SDK_INT < 17) {
            textView.setCompoundDrawablesWithIntrinsicBounds(images.get(position), 0, 0, 0);
        } else {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(images.get(position), 0, 0, 0);
        }
        textView.setCompoundDrawablePadding(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        12,
                        getContext().getResources().getDisplayMetrics()));

        return view;
    }

}
