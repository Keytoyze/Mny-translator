package com.mnysqtp.com.mnyproject.Fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mnysqtp.com.mnyproject.Utils.InputManager;
import com.mnysqtp.com.mnyproject.Utils.Mediaclass;
import com.mnysqtp.com.mnyproject.R;
import com.mnysqtp.com.mnyproject.Utils.SQLiteclass;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TranslationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TranslationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TranslationFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String[][] re;
    private OnFragmentInteractionListener mListener;

    public TranslationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TranslationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TranslationFragment newInstance(String param1, String param2) {
        TranslationFragment fragment = new TranslationFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_translation, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onStart(){
        super.onStart();
        final Activity ac = getActivity();
        final ImageButton IB = ac.findViewById(R.id.id_search_imageButton);
        final TextView RE = ac.findViewById(R.id.id_translationresult);
        final TextView RE2 = ac.findViewById(R.id.id_RomanResult);
        final ScrollView Sv = ac.findViewById(R.id.Scroll_tran);
        final EditText et = ac.findViewById(R.id.id_translation_edit);
        final ImageButton male = ac.findViewById(R.id.id_male_tran);
        final ImageButton female = ac.findViewById(R.id.id_female_tran);
        final TextView male_ = ac.findViewById(R.id.id_maletext);
        final TextView female_ = ac.findViewById(R.id.id_femaletext);
        IB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer result = new StringBuffer();
                StringBuffer result_roman = new StringBuffer();
                String[] Minnan_ = null;
                String[] Luoma_ = null;
                String Path;
                InputManager.HideInput(ac, et);
                re = SQLiteclass.Translate(et.getText().toString(), result, result_roman);
                if (result != null && result.length() != 0 && result_roman != null && result_roman.length() != 0) {
                    Sv.setVisibility(View.VISIBLE);
                    RE.setText(result);
                    RE2.setText(result_roman);
                } else {
                    Sv.setVisibility(View.INVISIBLE);
                }
            }
        });
        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String path = SQLiteclass.readSound(re[1],0);
                    Mediaclass.Play(path, male);
                }catch (Exception e){
                    Toast.makeText(ac, "播放失败！", Toast.LENGTH_LONG).show();
                }
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String path = SQLiteclass.readSound(re[1],1);
                    Mediaclass.Play(path, female);
                }catch (Exception e){
                    Toast.makeText(ac, "播放失败！", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
