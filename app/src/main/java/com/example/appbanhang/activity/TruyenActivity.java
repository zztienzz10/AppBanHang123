package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.TruyenAdapter;
import com.example.appbanhang.model.SanPhamMoi;
import com.example.appbanhang.retrofit.ApiBanHang;
import com.example.appbanhang.retrofit.RetrofitClient;
import com.example.appbanhang.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TruyenActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page= 1;
    int loai;
    TruyenAdapter adapterDt;
    List<SanPhamMoi> sanPhamMoiList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truyen);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        loai = getIntent().getIntExtra("loai", 1);
        AnhXa();
        ActionToolBar();
        getData();
    }

    private void getData() {
        compositeDisposable.add(apiBanHang.getSanPham(page,loai)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                sanPhamMoiModel -> {
                    if (sanPhamMoiModel.isSuccess()){
                        sanPhamMoiList = sanPhamMoiModel.getResult();
                        adapterDt = new TruyenAdapter(getApplicationContext(), sanPhamMoiList);
                        recyclerView.setAdapter(adapterDt);
                    }

                },
                throwable -> {
                    Toast.makeText(getApplicationContext(),"không kết nối sever", Toast.LENGTH_LONG).show();
                }
        ));

    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void AnhXa() {
        toolbar = findViewById(R.id.toobar);
        recyclerView = findViewById(R.id.recycleview_dt);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        sanPhamMoiList = new ArrayList<>();

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();

        super.onDestroy();
    }
}