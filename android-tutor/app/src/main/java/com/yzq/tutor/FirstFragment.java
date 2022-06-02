package com.yzq.tutor;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.yzq.tutor.databinding.FragmentFirstBinding;

import java.io.IOException;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        binding.actBtn.setOnClickListener(v -> {
            binding.actBtn.setText("跳转activity");
            //跳转activity 在fragment里面
            startActivity(new Intent(getActivity(), EmptyActivity.class));

        });
        binding.button4.setOnClickListener(v -> binding.button4.setTextColor(getResources().getColor(R.color.black)));
        binding.button2.setOnClickListener(v -> {
            String url = "https://jsonplaceholder.typicode.com/todos";
            OkHttpClient client = new OkHttpClient();


            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String data = Objects.requireNonNull(response.body()).string();
                Log.d("info", data);
                binding.textviewFirst.setText(data);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        binding.buttonFirst.setOnClickListener(view1 -> NavHostFragment.findNavController(FirstFragment.this)
                .navigate(R.id.action_FirstFragment_to_SecondFragment));
        binding.editText.setOnKeyListener((v, keyCode, event) -> {
            Log.d("tags", String.valueOf(binding.editText.getText()));
            binding.actBtn.setText("已经出发了额");
            return false;
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}