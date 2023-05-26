package com.example.weather;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

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
    private List<Integer> listId;
    private   ProgressBar progressBar;
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
        progressBar=findViewById(R.id.progressBar);
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                if (userField.getText().toString().trim().equals(getResources().getString(R.string.space))) {
                    progressBar.setVisibility(View.INVISIBLE);
                    userField.setText("");
                    result.setText(R.string.space);
                    resultFeeling.setText(R.string.space);
                    conditionSky.setText("");
                    Toast.makeText(MainActivity.this, R.string.no_user_input, Toast.LENGTH_LONG).show();//
                    // всплывающее окно должно быть показано на странице MainActivity.Показывает сообщение, 3-1 параметр -длительность
                }//trim -обрезаем пробелы в строке и проверяем на пустую строку
                else {

                    userField.refreshDrawableState();
                    result.refreshDrawableState();
                    resultFeeling.refreshDrawableState();
                    imageView.setImageDrawable(null);

                    String city = userField.getText().toString();
                    key = BuildConfig.key;
                    mainPresenter.handleSendRequest(city, key,context);

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
                    mainPresenter.handleSendRequest(city, key,context);
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
            mainPresenter.handleSendRequest(city, key,context);
        }
    }

    @SuppressLint({"SetTextI18n", "ResourceType"})
    @Override
    public void updateWeatherInfo(int temperature, int approximatelyTemperature, String conditionSkym, String image) {

        progressBar.setVisibility(View.INVISIBLE);
        result.setText(getResources().getString(R.string.cur_temp) + temperature);
        resultFeeling.setText(getResources().getString(R.string.fel_temp) + approximatelyTemperature);
        conditionSky.setText(conditionSkym);

       if (image.equals((String) getResources().getString(R.string.partly_cloudy)) || image.equals((String)getResources().getString(R.string.some_cloudy))) {
            imageView.setImageResource(R.drawable.oblako);
        } else if (image.equals((String) getResources().getString(R.string.clean))) {
            imageView.setImageResource(R.drawable.sun);
        } else if (image.equals((String) getResources().getString(R.string.cloudy_with_space))) {
            imageView.setImageResource(R.drawable.aun_and_cloud);
        } else if (image.equals((String)getResources().getString(R.string.rain))) {
           imageView.setImageResource(R.drawable.some_rain);
       } else {
            imageView.setImageDrawable(null);
        }

    }

    @Override
    public void wrongData() {
        progressBar.setVisibility(View.INVISIBLE);
        userField.setText("");
        result.setText(R.string.space);
        resultFeeling.setText(R.string.space);
        conditionSky.setText("");
        Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError(String error) {
        progressBar.setVisibility(View.INVISIBLE);
        conditionSky.setText(error);
        Toast.makeText(MainActivity.this, R.string.error_request, Toast.LENGTH_LONG).show();
    }

    // TODO Dependency Injection



}

