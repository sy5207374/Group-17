package com.seglab.profilemanager;   // ← 和 MainActivity 的包名完全一致

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;

public class AvatarActivity extends AppCompatActivity {

    private RadioGroup radioGroupAvatars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);

        radioGroupAvatars = findViewById(R.id.radioGroupAvatars);
        Button buttonConfirmAvatar = findViewById(R.id.buttonConfirmAvatar);

        buttonConfirmAvatar.setOnClickListener(v -> returnSelectedAvatar());
    }

    private void returnSelectedAvatar() {
        int checkedId = radioGroupAvatars.getCheckedRadioButtonId();
        int avatarResId = R.drawable.ic_launcher_foreground; // 默认头像

        if (checkedId == R.id.radioAvatar1) {
            avatarResId = R.drawable.ic_launcher_foreground;
        } else if (checkedId == R.id.radioAvatar2) {
            avatarResId = R.drawable.ic_launcher_foreground;
        } else if (checkedId == R.id.radioAvatar3) {
            avatarResId = R.drawable.ic_launcher_foreground;
        }

        Intent result = new Intent();
        result.putExtra(MainActivity.EXTRA_AVATAR_ID, avatarResId);
        setResult(RESULT_OK, result);
        finish();
    }
}