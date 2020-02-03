package com.example.quiz.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.example.quiz.R;
import com.example.quiz.network.QuestionDataRepository;
import com.example.quiz.viewModel.MainViewModel;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button startQuizButton;
    private TextView refreshTextView;
    private TextInputLayout qNumberInputLayout;
    private int qNumber;
    private MainViewModel mainViewModel;

    private static final String internetNeeded = "Internet permission needed to be able to bring questions from our website";
    private static final String[] permissions = {Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.INTERNET};
    private static final int ACCESS_WIFI_STATE_REQ_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inflateItems();

        if (!mainViewModel.dataDownloaded()) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                showExplanationDialog(internetNeeded);
                requestAccessWifiStatePermission();
            } else {
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (wifiManager != null && wifiManager.isWifiEnabled()) {
                    downloadData();
                } else {
                    refreshTextView.setVisibility(View.VISIBLE);
                    showDialog("Please turn on your wifi to download contest questions :) ");
                }
            }
        } else {
            startQuizButton.setEnabled(true);
        }
    }

    private void inflateItems() {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        qNumberInputLayout = findViewById(R.id.text_input_q_num);
        startQuizButton = findViewById(R.id.main_btn_start);
        refreshTextView = findViewById(R.id.main_refresh_text_view);
        findViewById(R.id.activity).setOnClickListener(this);
    }

    private void downloadData() {
        boolean downloadedPreviously = mainViewModel.dataDownloaded();
        if (!downloadedPreviously) {
            mainViewModel.updateQuestions(new QuestionDataRepository.RetrofitResponseListener() {
                @Override
                public void onSuccess() {
                    startQuizButton.setEnabled(true);
                }

                @Override
                public void onFailure() {
                    refreshTextView.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public void startQuiz(View view) {
        String qNum = qNumberInputLayout.getEditText().getText().toString().trim();
        // Edge case: Empty fields
        if (qNum.isEmpty()) {
            Toast.makeText(this, R.string.fill_fields, Toast.LENGTH_LONG).show();
            return;
        }
        qNumber = Integer.parseInt(qNum);
        if (!validQNum())
            return;
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra("q_number", qNumber);
        startActivity(intent);
    }

    private boolean validQNum() {
        if (qNumber < 5 || qNumber > 45) {
            qNumberInputLayout.setError(getResources().getText(R.string.invalid_q_num));
            return false;
        }
        qNumberInputLayout.setError(null);
        return true;
    }

    // Hides keyboard when touch the screen
    @Override
    public void onClick(View v) {
        if (v.getId() != R.id.text_input_q_num)
            hideKeyboard(this);
    }

    /* This method hides the keyboard of the activity send to it as parameter */
    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private void requestAccessWifiStatePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CHANGE_WIFI_STATE)) {
            showExplanationDialog(internetNeeded);
        } else {
            ActivityCompat.requestPermissions(this, permissions, ACCESS_WIFI_STATE_REQ_CODE);
        }
    }

    private void showDialog(String explanation) {
        new AlertDialog.Builder(this).setMessage(explanation).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    private void showExplanationDialog(String explanation) {
        new AlertDialog.Builder(this).setMessage(explanation).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(MainActivity.this, permissions, ACCESS_WIFI_STATE_REQ_CODE);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_WIFI_STATE_REQ_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadData();
                } else {
                    showExplanationDialog(internetNeeded);
                }
                break;
        }
    }

    public void refresh(View view) {
        refreshTextView.setVisibility(View.INVISIBLE);
        downloadData();
    }

}