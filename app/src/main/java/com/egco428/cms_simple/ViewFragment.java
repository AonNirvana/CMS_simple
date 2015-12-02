package com.egco428.cms_simple;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewFragment extends Fragment {
    public static final String ARG_KEY = "key";
    public static final String ARG_COL = "col";

    private String mKey;
    private String mCol;

    private View view;
    private EditText editText;
    private Button viewConfirm;
    private Button viewReset;
    private Button viewDelete;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param key The article's name.
     * @param col The article's contents.
     * @return A new instance of fragment ViewFragment.
     */
    public static ViewFragment newInstance(String key, String col) {
        ViewFragment fragment = new ViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        args.putString(ARG_COL, col);
        fragment.setArguments(args);
        return fragment;
    }

    public ViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mKey = getArguments().getString(ARG_KEY);
            mCol = getArguments().getString(ARG_COL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view, container, false);
        editText = (EditText)view.findViewById(R.id.editText);
        //Set the mCol to the present EditText.
        editText.setText(mCol);
        viewConfirm = (Button)view.findViewById(R.id.viewConfirm);
        viewConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Re-release mCol
                mCol = editText.getText().toString();
                //Updating database.
                if (mListener != null) {
                    mListener.onFragmentInteraction(mKey,mCol,true);
                }
            }
        });
        viewReset = (Button)view.findViewById(R.id.viewReset);
        viewReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(mCol);
            }
        });
        viewDelete = (Button)view.findViewById(R.id.viewDelete);
        viewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Updating database.
                if (mListener != null) {
                    mListener.onFragmentInteraction(mKey,mCol,false);
                }
            }
        });
        return view;
    }

    public void onListViewPressed(String key, String col) {
        mKey = key;
        mCol = col;
        editText.setText(col);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String key, String col, boolean update);
    }

}
