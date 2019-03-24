package com.example.caoan.shop.FragmentComponent;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.caoan.shop.Adapter.FragmentAdapter;
import com.example.caoan.shop.BottomNavigationBarActivity;
import com.example.caoan.shop.R;
import com.example.caoan.shop.ViewPageTransformer.CubeInScalingTransformation;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private BottomNavigationBarActivity bottomNavigationBarActivity;
    private View view;
    private FragmentAdapter fragmentAdapter;
    private List<Fragment> fragmentList;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    private void fillFragment(String key, String userkey) {
        fragmentList = new ArrayList<Fragment>();
        FoodFragment foodFragment = new FoodFragment().newInstance(key, userkey);
        DrinkFragment drinkFragment = new DrinkFragment().newInstance(key, userkey);

        fragmentList.add(foodFragment);
        fragmentList.add(drinkFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //System.out.println("Create View Fragment");
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_home, container, false);

        tabLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.viewpager);
        Bundle bundle = getArguments();
        String key = bundle.getString(ARG_PARAM1);
        String userkey = bundle.getString(ARG_PARAM2);
//        System.out.println("key: "+key+"\nuserkey: "+userkey);
        fillFragment(key, userkey);
        fragmentAdapter = new FragmentAdapter(getChildFragmentManager(), fragmentList);
        viewPager.setPageTransformer(true, new CubeInScalingTransformation());
        viewPager.setAdapter(fragmentAdapter);

        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BottomNavigationBarActivity) {
            this.bottomNavigationBarActivity = (BottomNavigationBarActivity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
