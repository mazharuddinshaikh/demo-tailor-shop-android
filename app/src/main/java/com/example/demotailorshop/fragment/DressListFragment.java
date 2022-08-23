package com.example.demotailorshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demotailorshop.MainActivity;
import com.example.demotailorshop.R;
import com.example.demotailorshop.adapter.DressListAdapter;
import com.example.demotailorshop.databinding.FragmentDressListBinding;
import com.example.demotailorshop.entity.ApiError;
import com.example.demotailorshop.entity.Dress;
import com.example.demotailorshop.entity.User;
import com.example.demotailorshop.listener.DressListClickListener;
import com.example.demotailorshop.listener.RetryListener;
import com.example.demotailorshop.utils.DtsSharedPreferenceUtil;
import com.example.demotailorshop.utils.DtsUtils;
import com.example.demotailorshop.viewmodel.DressListViewModel;

import java.util.List;

//import kotlin.jvm.JvmDefaultWithoutCompatibility;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DressListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DressListFragment extends Fragment {
    private static final String TAG = "DressListFragment";
    private DressListViewModel dressListViewModel;
//    private FragmentDressListBinding binding;

    public DressListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DressListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DressListFragment newInstance() {
        DressListFragment fragment = new DressListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "Dress list fragment");
        // Inflate the layout for this fragment
        final int displayWidth = DtsUtils.getScreenWidth(requireActivity());
        List<String> dressTypeList = null;
        FragmentDressListBinding binding = FragmentDressListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        User user = DtsSharedPreferenceUtil.getInstance().getUserFromPref(requireActivity(), DtsSharedPreferenceUtil.KEY_USER);
        if (user == null) {
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            intent.putExtra(DtsSharedPreferenceUtil.KEY_AUTHENTICATION_MESSAGE, DtsSharedPreferenceUtil.VALUE_AUTHENTICATION_MESSAGE);
            startActivity(intent);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DressListAdapter dressListAdapter = new DressListAdapter();
        dressListAdapter.setContext(getActivity());
        dressListAdapter.setDisplayWidth(displayWidth);
        dressListAdapter.setListener(new DressListClickListener() {
            @Override
            public void onDressClicked(Dress dress) {
//                Open Dress details fragment and dress object to it
                Log.v(TAG, "Open dress detail");
                DressDetailFragment dressDetailFragment = new DressDetailFragment();
                dressDetailFragment.setDress(dress);
                dressDetailFragment.setNewCustomer(false);
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fcvContainer, dressDetailFragment, "DressDetailFragment")
                        .addToBackStack("DressDetailFragment")
                        .commit();
            }
        });
        dressListAdapter.setRetryListener(new RetryListener() {
            @Override
            public void retry() {
                Log.v(TAG, "Retry load dress list ");
                loadDress(user, dressTypeList, dressListAdapter);

            }
        });

        binding.rvDressList.setLayoutManager(layoutManager);
        binding.rvDressList.setAdapter(dressListAdapter);
        dressListViewModel = new ViewModelProvider(requireActivity())
                .get(DressListViewModel.class);
        dressListViewModel.setLimit(3);
        dressListViewModel.setOffset(0);

        loadDress(user, dressTypeList, dressListAdapter);
        dressListViewModel.getApiErrorMutableLiveData().observe(requireActivity(), new Observer<ApiError>() {
            @Override
            public void onChanged(ApiError apiError) {
                dressListAdapter.setApiError(apiError);
            }
        });
        binding.rvDressList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                int itemCount = recyclerView.getAdapter().getItemCount();
                if (dy < 0) {
                    dressListViewModel.removeDress(null);
                }
                if (dy > 0 && lastVisibleItemPosition == (itemCount - 1)) {
                    int offset = dressListViewModel.getOffset() + 1;
                    dressListViewModel.setOffset(offset);
                    Log.v(TAG, "Scroll reach at last");
                    dressListViewModel.addDress(null);
                    dressListViewModel.addDressList(user, dressTypeList);

                }
            }
        });
        return view;
    }

    private void loadDress(User user, List<String> dressTypeList, DressListAdapter dressListAdapter) {
        dressListViewModel.getDressListLiveData(user, dressTypeList).observe(requireActivity(),
                new Observer<List<Dress>>() {
                    @Override
                    public void onChanged(List<Dress> dresses) {
                        dressListAdapter.setDressList(dresses);
                        dressListAdapter.notifyDataSetChanged();

                    }
                });
    }

}