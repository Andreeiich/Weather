package com.example.weather;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.JsonReader;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.internal.StaticLayoutBuilderConfigurer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements MainPresenter.View {

    private EditText userField;
    private Button mainButton;
    private TextView result;
    private TextView resultFeeling;
    private TextView conditionSky;
    private ImageView imageView;
    final Context context = this;
    private TextView final_text;
    private Resources resources;
    private MainPresenter mainPresenter;

    private String key;
    private String url;

    private List<Integer> listId;

    @SuppressLint({"ResourceAsColor", "MissingInflatedId"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainPresenter = new MainPresenter(this);
        init();
    }

    public void init() {
        listId = new ArrayList<>();
        listId.add(R.id.Moscow);
        listId.add(R.id.SaintPetersburg);
        listId.add(R.id.newCity);

        userField = findViewById(R.id.userField);
        mainButton = findViewById(R.id.mainButton);
        result = findViewById(R.id.result);
        resultFeeling = findViewById(R.id.resultFeeling);
        conditionSky = findViewById(R.id.conditionSky);
        imageView = findViewById(R.id.imageView);
        MainModel model = new MainModel();
        mainPresenter = new MainPresenter(model);
        mainPresenter.attachView(this);

        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userField.getText().toString().trim().equals(getResources().getString(R.string.space))) {
                    Toast.makeText(MainActivity.this, R.string.no_user_input, Toast.LENGTH_LONG).show();//
                    // всплывающее окно должно быть показано на странице MainActivity.Показывает сообщение, 3-1 параметр -длительность
                }//trim -обрезаем пробелы в строке и проверяем на пустую строку
                else {

                    // result.setText("");
                    //resultFeeling.setText("");
                    //conditionSky.setText("");
                    userField.refreshDrawableState();
                    result.refreshDrawableState();
                    resultFeeling.refreshDrawableState();
                    imageView.setImageDrawable(null);

                    String city = userField.getText().toString();
                    // sendRequest(city);
                    key = BuildConfig.key;
                    url = getResources().getString(R.string.url) + city + getResources().getString(R.string.appid) + key + getResources().getString(R.string.units_and_lang);
                    mainPresenter.handleSendRequest(city, key, url);
                }
            }
        });//делаем обработчик события на кнопку;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_city, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String city = "";
        for (Integer cur : listId) {
            if (cur == item.getItemId()) {
                if (cur == R.id.newCity) {
                    final_text = (TextView) findViewById(R.id.final_text);
                    //Получаем вид с файла city_layout.xml, который применим для диалогового окна:
                    LayoutInflater li = LayoutInflater.from(context);
                    View promptsView = li.inflate(R.layout.city_layout, null);

                    //Создаем AlertDialog
                    AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);

                    //Настраиваем city_layout.xml для нашего AlertDialog:
                    mDialogBuilder.setView(promptsView);

                    //Настраиваем отображение поля для ввода текста в открытом диалоге:
                    final EditText userInput = (EditText) promptsView.findViewById(R.id.EditTextIdNewCity);

                    //Настраиваем сообщение в диалоговом окне:
                    mDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton(R.string.OK,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //Вводим текст и отображаем в строке ввода на основном экране:
                                            final_text.setText(userInput.getText());
                                            resources = getResources();
                                            int col = resources.getColor(R.color.backgroundUserCity);
                                            final_text.setBackgroundColor(col);
                                        }
                                    })
                            .setNegativeButton(R.string.Cancel,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                    //Создаем AlertDialog:
                    AlertDialog alertDialog = mDialogBuilder.create();

                    //и отображаем его:
                    alertDialog.show();

                } else {
                    city = (String) item.getTitle();
                    key = BuildConfig.key;
                    url = getResources().getString(R.string.url) + city + getResources().getString(R.string.appid) + key + getResources().getString(R.string.units_and_lang);
                    mainPresenter.handleSendRequest(city, key, url);
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickClientCity(View v) {
        TextView textView = findViewById(R.id.final_text);
        int id = textView.getId();

        String city = "";

        if (id == R.id.final_text) {
            city = textView.getText().toString();
            key = BuildConfig.key;
            url = getResources().getString(R.string.url) + city + getResources().getString(R.string.appid) + key + getResources().getString(R.string.units_and_lang);
            mainPresenter.handleSendRequest(city, key, url);
        }
    }

    @SuppressLint({"SetTextI18n", "ResourceType"})
    @Override
    public void updateWeatherInfo(int temperature, int approximatelyTemperature, String conditionSkym, String image) {


        result.setText(getResources().getString(R.string.cur_temp) + temperature);
        resultFeeling.setText(getResources().getString(R.string.fel_temp) + approximatelyTemperature);
        conditionSky.setText(conditionSkym);

        if (image.equals((String) getResources().getString(R.string.partly_cloudy))) {
            imageView.setImageResource(R.drawable.oblako);
        } else if (image.equals((String) getResources().getString(R.string.clean))) {
            imageView.setImageResource(R.drawable.sun);
        } else if (image.equals((String) getResources().getString(R.string.cloudy_with_space))) {
            imageView.setImageResource(R.drawable.aun_and_cloud);
        } else {
            imageView.setImageDrawable(null);
        }

    }

    @Override
    public void wrongData() {
        userField.setText("");
        result.setText(R.string.space);
        resultFeeling.setText(R.string.space);
        conditionSky.setText("");
        Toast.makeText(MainActivity.this, R.string.no_city, Toast.LENGTH_LONG).show();
    }


    // TODO Dependency Injection
    // TODO Retrofit


}

