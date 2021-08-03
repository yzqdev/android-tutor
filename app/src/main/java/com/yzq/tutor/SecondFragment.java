package com.yzq.tutor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.yzq.tutor.databinding.FragmentSecondBinding;

import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
private  SecondAdapter secondAdapter;
    class TestData {
        String text = "aaa";
        String btn = "bbb";

        public TestData(String text, String btn) {
            this.text = text;
            this.btn = btn;
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    private ArrayList<TestData> initData() {
        ArrayList<TestData> listData = new ArrayList<>();
        listData.add(new TestData("test1", "测试1"));
        listData.add(new TestData("test2", "测试2"));
        listData.add(new TestData("test3", "测试3"));
        listData.add(new TestData("test4", "测试4"));
        return listData;
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


       secondAdapter=new SecondAdapter(getActivity(), initData());
        binding.recycleView.setAdapter(secondAdapter);
        binding.recycleView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}