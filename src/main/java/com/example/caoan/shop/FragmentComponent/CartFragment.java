package com.example.caoan.shop.FragmentComponent;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.caoan.shop.Adapter.CartRecyclerViewAdapter;
import com.example.caoan.shop.BottomNavigationBarActivity;
import com.example.caoan.shop.Database.DataCart;
import com.example.caoan.shop.Interface.ItemTouchListener;
import com.example.caoan.shop.Model.Cart;
import com.example.caoan.shop.PayActivity;
import com.example.caoan.shop.R;
import com.example.caoan.shop.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static TextView textView;
    private BottomNavigationBarActivity bottomNavigationBarActivity;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private Button btthanhtoan;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private List<Cart> cartList;
    private DataCart dataCart;
    private String key_store;
    private CartRecyclerViewAdapter cartRecyclerViewAdapter;


    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static void setTextview(String sum) {
        textView.setText(sum);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cart, container, false);

        btthanhtoan = view.findViewById(R.id.btthanhtoan);
        recyclerView = view.findViewById(R.id.rcvcart);
        textView = view.findViewById(R.id.tvsum);
        progressBar = view.findViewById(R.id.progress);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("key_store", Context.MODE_PRIVATE);
        key_store = sharedPreferences.getString("key", "");
        dataCart = new DataCart(getContext());

        String userID = getActivity().getSharedPreferences("Account", Context.MODE_PRIVATE).getString("userID", "");
        if (userID == null || userID.equals("")) {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            btthanhtoan.setEnabled(false);
        } else {
            new ProgressBarProcess().execute();
        }
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(new ItemTouchListener() {
            @Override
            public void onSwipe(final int vitri, final int huong) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Warning").setMessage("Are you sure delete this product?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                cartRecyclerViewAdapter.swipe(vitri, huong);
                                new ProgressBarProcess().execute();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                new ProgressBarProcess().execute();
                            }
                        }).create().show();
            }

            @Override
            public void onMove(int oldPostion, int newPosition) {
                cartRecyclerViewAdapter.move(oldPostion, newPosition);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        btthanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = getActivity().getSharedPreferences("Account", Context.MODE_PRIVATE).getString("userID", "");
                if (userID.equals("")) {
                    Intent intent = new Intent(getActivity(), BottomNavigationBarActivity.class);
                    intent.putExtra("login", true);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), PayActivity.class);
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_cart, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                cartRecyclerViewAdapter.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            case R.id.refresh:
                Intent intent = getActivity().getIntent();
                getActivity().finish();
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BottomNavigationBarActivity) {
            this.bottomNavigationBarActivity = (BottomNavigationBarActivity) context;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event

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

    class ProgressBarProcess extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cartList = new ArrayList<Cart>();
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("key_store", Context.MODE_PRIVATE);
            String key_store = sharedPreferences.getString("key", "");
            cartList = dataCart.getCartList(key_store);

        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 1; i <= 100; i++) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);

            cartRecyclerViewAdapter = new CartRecyclerViewAdapter(getContext(), cartList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(cartRecyclerViewAdapter);
            String str = (String) textView.getText();
            if (dataCart.Total(key_store).equals("0") || str.equals("0đ")) {
                btthanhtoan.setEnabled(false);
            } else {
                btthanhtoan.setEnabled(true);
            }
        }
    }
}
