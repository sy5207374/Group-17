xample.lab3databases;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView productId;
    EditText productName, productPrice;
    Button addBtn, findBtn, deleteBtn;
    ListView productListView;

    ArrayList<String> productList;
    ArrayAdapter<String> adapter;
    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productList = new ArrayList<>();

        // info layout
        productId = findViewById(R.id.productId);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);

        //buttons
        addBtn = findViewById(R.id.addBtn);
        findBtn = findViewById(R.id.findBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        // listview
        productListView = findViewById(R.id.productListView);

        // db handler
        dbHandler = new MyDBHandler(this);

        // 首次启动如为空，插入两条种子数据，满足录屏要求
        dbHandler.seedIfEmpty();

        // 适配器
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);
        productListView.setAdapter(adapter);

        // 按钮监听
        addBtn.setOnClickListener(v -> {
            String name = productName.getText().toString().trim();
            Double price = parsePrice(productPrice);

            if (name.isEmpty() || price == null) {
                Toast.makeText(MainActivity.this, "Please fill in the valid name or pirze", Toast.LENGTH_SHORT).show();
                return;
            }

            Product product = new Product(name, price);
            dbHandler.addProduct(product);

            productName.setText("");
            productPrice.setText("");

            viewProductsAll(); // 添加后显示全部
        });

        findBtn.setOnClickListener(v -> {
            String namePrefix = productName.getText().toString().trim();
            Double price = parsePrice(productPrice);

            Cursor cursor = dbHandler.queryProducts(
                    namePrefix.isEmpty() ? null : namePrefix,
                    price
            );
            viewProductsFromCursor(cursor);
        });

        deleteBtn.setOnClickListener(v -> {
            String name = productName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(MainActivity.this, "please type in the item name to delete", Toast.LENGTH_SHORT).show();
                return;
            }
            int rows = dbHandler.deleteByName(name, /*caseSensitive=*/false);
            Toast.makeText(MainActivity.this, "delete counts: " + rows, Toast.LENGTH_SHORT).show();

            viewProductsAll();
        });

        // 启动时显示全部
        viewProductsAll();
    }

    private Double parsePrice(EditText et) {
        String t = et.getText().toString().trim();
        if (t.isEmpty()) return null;
        try {
            return Double.parseDouble(t);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void viewProductsAll() {
        Cursor cursor = dbHandler.getData();
        viewProductsFromCursor(cursor);
    }

    private void viewProductsFromCursor(Cursor cursor) {
        productList.clear();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String row = cursor.getString(1) + " (" + cursor.getString(2) + ")";
                productList.add(row);
            }
            cursor.close();
        }
        adapter.notifyDataSetChanged();
        if (productList.isEmpty()) {
            Toast.makeText(MainActivity.this, "no results found", Toast.LENGTH_SHORT).show();
