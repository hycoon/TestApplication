package com.hycoon.testLirbary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootView = (ConstraintLayout)findViewById(R.id.root_view);
        SVipBgView sVipBgView = new SVipBgView(MainActivity.this);
        sVipBgView.resetLinearGradient(this.getResources().getColor(R.color.vip_gloden_begin),
                this.getResources().getColor(R.color.vip_gloden_end));
        sVipBgView.setHeight(225);
        sVipBgView .setTitleText("vip会员");
        rootView.addView(sVipBgView);
        String data1 = "白夜行";
        String data2 = "全世界从你身边路过";
        String data3 = "民日报新媒体记者奔赴广州，专访中国工程院院士、？";
        String data4 = "假如没有明天";

        List<String> dataList = new ArrayList<>();
        dataList.add(data1);
        dataList.add(data2);
        dataList.add(data3);
        dataList.add(data4);
        sVipBgView.setStrList(dataList);
        sVipBgView.setOnScrollClickLisenter(new SVipBgView.OnScrollClickLisenter() {
            @Override
            public void onClick() {
                Toast.makeText(MainActivity.this,"点击滚动位置",Toast.LENGTH_SHORT).show();
            }
        });

        sVipBgView.setOnSubmitClickLisenter(new SVipBgView.OnSubmitClickLisenter() {
            @Override
            public void onClick() {
                Toast.makeText(MainActivity.this,"点击提交位置",Toast.LENGTH_SHORT).show();

            }
        });
    }

}
