package com.example.weather;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private EditText userField;
    private Button mainButton;
    private TextView result;
    private TextView resultFeeling;
    private TextView conditionSky;
    private ImageView imageView;
    final Context context = this;
    private TextView final_text;

    @SuppressLint({"ResourceAsColor", "MissingInflatedId"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userField = findViewById(R.id.userField);
        mainButton = findViewById(R.id.mainButton);
        result = findViewById(R.id.result);
        resultFeeling = findViewById(R.id.resultFeeling);
        conditionSky = findViewById(R.id.conditionSky);
        imageView = findViewById(R.id.imageView);

        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userField.getText().toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this, R.string.no_user_input, Toast.LENGTH_LONG).show();//
                    // всплывающее окно должно быть показано на странице MainActivity.Показывает сообщение, 3-1 параметр -длительность
                }//trim -обрезаем пробелы в строке и проверяем на пустую строку
                else {
                    String city = userField.getText().toString();
                    String key = "5e1913d6b50fb4cbbcb9ae046481e0bf";
                    String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + key + "&units=metric&lang=ru";

                    try {
                        Thread thread = new Thread(new GetUrlData(city, key, url), "Connect");
                        thread.start();
                    } catch (RuntimeException e) {
                        Toast.makeText(MainActivity.this, R.string.no_city, Toast.LENGTH_LONG).show();//

                    }
                }
            }
        });//делаем обработчик события на кнопку;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_city, menu);

        return true;
    }


    public void onClickClientCity(View v) {
        TextView textView = findViewById(R.id.final_text);
        int id = textView.getId();

        String city = "";
        String key = "5e1913d6b50fb4cbbcb9ae046481e0bf";
        String url = "";

        if (id == R.id.final_text) {
            city = textView.getText().toString();

            url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + key + "&units=metric&lang=ru";
            Thread thread = new Thread(new GetUrlData(city, key, url), "Connect");
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        String city = "";
        String key = "5e1913d6b50fb4cbbcb9ae046481e0bf";
        String url = "";

        if (id == R.id.Moscow) {
            city = getString(R.string.Moscow);
            url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + key + "&units=metric&lang=ru";
            Thread thread = new Thread(new GetUrlData(city, key, url), "Connect");
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (id == R.id.SaintPetersburg) {
            city = getString(R.string.SaintPetersburg);

            url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + key + "&units=metric&lang=ru";
            Thread thread = new Thread(new GetUrlData(city, key, url), "Connect");
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


        if (id == R.id.newCity) {
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
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //Вводим текст и отображаем в строке ввода на основном экране:
                                    final_text.setText(userInput.getText());
                                    int col = Color.parseColor("#51abf0");
                                    final_text.setBackgroundColor(col);
                                }
                            })
                    .setNegativeButton("Отмена",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

            //Создаем AlertDialog:
            AlertDialog alertDialog = mDialogBuilder.create();

            //и отображаем его:
            alertDialog.show();

        }

        return super.onOptionsItemSelected(item);
    }


    private class GetUrlData implements Runnable {

        private String city = "";
        private String key = "";
        private String urls = "";

        public GetUrlData(String city, String key, String urls) {
            this.city = city;
            this.key = key;
            this.urls = urls;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void run() throws RuntimeException {

            HttpsURLConnection connection = null;
            BufferedReader bufferedReader = null;
            StringBuffer stringBuffer = new StringBuffer();
            /*try {*/
            URL url = null;//передали в конструктор параметр
            try {
                url = new URL(urls);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            try {
                connection = (HttpsURLConnection) url.openConnection();//открыли соединение
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                connection.connect();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            InputStream stream = null;//получаем всю информацию с определенного URL адреса -потоком
            try {
                stream = connection.getInputStream();
            } catch (IOException e) {
                result.setText(R.string.no_city);
            }

            if (stream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(stream));//считываем весь поток в формате строки

                String line = "";

                while (true) {
                    try {
                        if (!((line = bufferedReader.readLine()) != null)) break;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    stringBuffer.append(line).append("\n");
                }
            }

            if (stream != null) {
                try {
                    if (city.equals("") && urls.equals("") && key.equals("")) {
                        Toast.makeText(MainActivity.this, "Введите город ", Toast.LENGTH_LONG).show();
                    } else {
                        JSONObject jsonObject = new JSONObject(stringBuffer.toString());
                        int temp = (int) jsonObject.getJSONObject("main").getDouble("temp");
                        result.setText("Текущая температура: " + temp);
                        int approximately = (int) jsonObject.getJSONObject("main").getDouble("feels_like");
                        resultFeeling.setText("Ощущается как: " + approximately);

                        String value1 = "";
                        JSONArray jsonArray = jsonObject.getJSONArray("weather");
                        for (int i = 0; i < (jsonArray.length()); i++) {
                            JSONObject json_obj = jsonArray.getJSONObject(i);
                            value1 = json_obj.getString("description");
                            conditionSky.setText(value1);

                        }
                        if (value1.equals("переменная облачность")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    imageView.setImageResource(R.drawable.oblako);
                                }
                            });
                        } else if (value1.equals("ясно")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    imageView.setImageResource(R.drawable.sun);
                                }
                            });
                        } else if (value1.equals("облачно с прояснениями")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    imageView.setImageResource(R.drawable.aun_and_cloud);
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    imageView.refreshDrawableState();
                                }
                            });
                        }

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }


        }

    }
}

