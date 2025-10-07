package com.example.calculator; 

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tv;
    private StringBuilder input = new StringBuilder(); // 当前正在输入的数字（字符串）
    private Double accumulator = null;                 // 累加器：已计算/待计算的左操作数
    private Character pendingOp = null;                // 待执行的运算符
    private boolean justEvaluated = false;             // 刚按过 "="

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tvDisplay);

        // 数字键
        int[] digitIds = { R.id.btn0,R.id.btn1,R.id.btn2,R.id.btn3,R.id.btn4,
                R.id.btn5,R.id.btn6,R.id.btn7,R.id.btn8,R.id.btn9 };
        for (int id : digitIds) {
            Button b = findViewById(id);
            b.setOnClickListener(v -> onDigit(((Button) v).getText().toString()));
        }

        // 小数点
        findViewById(R.id.btnDot).setOnClickListener(v -> onDot());

        // 运算符
        findViewById(R.id.btnAdd).setOnClickListener(v -> onOperator('+'));
        findViewById(R.id.btnSub).setOnClickListener(v -> onOperator('-'));
        findViewById(R.id.btnMul).setOnClickListener(v -> onOperator('*'));
        findViewById(R.id.btnDiv).setOnClickListener(v -> onOperator('/'));

        // 等号
        findViewById(R.id.btnEq).setOnClickListener(v -> onEquals());

        // 清零 & 退格
        findViewById(R.id.btnClear).setOnClickListener(v -> clearAll());
        findViewById(R.id.btnBack).setOnClickListener(v -> backspace());

        updateDisplay("0");
    }



}

