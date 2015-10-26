package com.bandonleon.musetta.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bandonleon.musetta.R;

/**
 * Created by dombhuphaibool on 10/25/15.
 */
public class PlaceholderFragment extends NavigationPageFragment {
    private static final String TAG = "PlaceholderFragment";
    private static final String ARGS_PLACEHOLDER_TEXT = "args_placeholder_text";

    private TextView mText;

    public static PlaceholderFragment newInstance(String text) {
        Bundle args = new Bundle();
        args.putString(ARGS_PLACEHOLDER_TEXT, text);
        PlaceholderFragment fragment = new PlaceholderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_placeholder, container, false);
        mText = (TextView) rootView.findViewById(R.id.placeholder_txt);

        Bundle args = getArguments();
        if (mText != null && args != null) {
            String text = args.getString(ARGS_PLACEHOLDER_TEXT);
            if (!TextUtils.isEmpty(text)) {
                mText.setText(text);
            }
        }
        return rootView;
    }
}
