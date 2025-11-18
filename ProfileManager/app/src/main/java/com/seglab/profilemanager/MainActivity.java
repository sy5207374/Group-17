package com.seglab.profilemanager;   // ← 用你自己的包名，例如 com.example.profilemanager

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_AVATAR = 1001;
    public static final String EXTRA_AVATAR_ID = "EXTRA_AVATAR_ID";

    private ImageView imageAvatar;
    private EditText editAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageAvatar = findViewById(R.id.imageAvatar);
        editAddress = findViewById(R.id.editAddress);
        Button buttonChangeAvatar = findViewById(R.id.buttonChangeAvatar);
        Button buttonOpenMaps = findViewById(R.id.buttonOpenMaps);

        // 跳转到选择头像 Activity
        buttonChangeAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AvatarActivity.class);
            startActivityForResult(intent, REQUEST_AVATAR);
        });

        // 打开 Google Maps
        buttonOpenMaps.setOnClickListener(v -> openMaps());
    }

    private void openMaps() {
        String address = editAddress.getText().toString().trim();
        if (address.isEmpty()) {
            Toast.makeText(this, "Enter your post code", Toast.LENGTH_SHORT).show();
            return;
        }

        // 使用 geo URI 打开 Google Maps，并搜索地址
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "This device doesn't install Google Maps", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 接收头像选择结果
        if (requestCode == REQUEST_AVATAR && resultCode == RESULT_OK && data != null) {
            int avatarResId = data.getIntExtra(EXTRA_AVATAR_ID, R.drawable.ic_launcher_foreground);
            imageAvatar.setImageResource(avatarResId);
        }
    }
}