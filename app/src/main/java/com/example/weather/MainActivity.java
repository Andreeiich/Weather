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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.weather.AppModule.AppModule;
import com.example.weather.AppModule.DaggerAppModule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;


public class MainActivity extends AppCompatActivity implements MainPresenter.View {

    private EditText userField;
    private Button mainButton;
    private TextView result;
    private TextView resultFeeling;
    private TextView conditionSky;
    private TextView editTextTime1;
    private TextView editTextTime2;
    private TextView editTextTime3;
    private TextView editTextTime4;
    private TextView editTextTime5;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private TextView logo;
    private ImageView imageView;
    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
    private ImageView image4;
    private ImageView image5;
    final Context context = this;
    private TextView final_text;
    private Resources resources;
    @Inject
    MainPresenter mainPresenter;
    private String key;
    private List<Integer> listId;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    private LinearLayout cont1;
    private LinearLayout cont2;
    private LinearLayout cont3;
    private LinearLayout cont4;
    private LinearLayout cont5;
    ArrayList<LinearLayout> listCont;
    ArrayList<TextView> editTextArr;
    ArrayList<TextView> textViewArr;
    ArrayList<ImageView> imageArr;
    private Calendar time;

    @SuppressLint({"ResourceAsColor","MissingInflatedId"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_app_layout);
        setContentView(R.layout.activity_main);
        AppModule appModule = DaggerAppModule.create();
        mainPresenter = appModule.mainPresenter();
        mainPresenter.attachView(this);
        init();
    }

    @SuppressLint("ResourceAsColor")
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
        progressBar = findViewById(R.id.progressBar);
        linearLayout = findViewById(R.id.fieldFelling);
        cont1 = findViewById(R.id.cont1);
        cont2 = findViewById(R.id.cont2);
        cont3 = findViewById(R.id.cont3);
        cont4 = findViewById(R.id.cont4);
        cont5 = findViewById(R.id.cont5);
        listCont = new ArrayList<>();
        listCont.add(cont1);
        listCont.add(cont2);
        listCont.add(cont3);
        listCont.add(cont4);
        listCont.add(cont5);
        editTextTime1 = findViewById(R.id.editTextTime1);
        editTextTime2 = findViewById(R.id.editTextTime2);
        editTextTime3 = findViewById(R.id.editTextTime3);
        editTextTime4 = findViewById(R.id.editTextTime4);
        editTextTime5 = findViewById(R.id.editTextTime5);
        editTextArr = new ArrayList<>();
        editTextArr.add(editTextTime1);
        editTextArr.add(editTextTime2);
        editTextArr.add(editTextTime3);
        editTextArr.add(editTextTime4);
        editTextArr.add(editTextTime5);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        textViewArr = new ArrayList<>();
        textViewArr.add(textView1);
        textViewArr.add(textView2);
        textViewArr.add(textView3);
        textViewArr.add(textView4);
        textViewArr.add(textView5);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        image4 = findViewById(R.id.image4);
        image5 = findViewById(R.id.image5);
        imageArr = new ArrayList<>();
        imageArr.add(image1);
        imageArr.add(image2);
        imageArr.add(image3);
        imageArr.add(image4);
        imageArr.add(image5);
        logo = findViewById(R.id.logo);

        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                if (isTextEqualToResouce(userField.getText().toString(),getResources().getString(R.string.space))) {
                    setupView();
                    Toast.makeText(MainActivity.this,R.string.no_user_input,Toast.LENGTH_LONG).show();//
                    // всплывающее окно должно быть показано на странице MainActivity.Показывает сообщение, 3-1 параметр -длительность
                }//trim -обрезаем пробелы в строке и проверяем на пустую строку
                else {
                    userField.refreshDrawableState();
                    result.refreshDrawableState();
                    resultFeeling.refreshDrawableState();
                    imageView.setImageDrawable(null);
                    String city = userField.getText().toString();
                    key = BuildConfig.key;
                    mainPresenter.handleSendRequest(city,key,getResources().getString(R.string.units).toString(),getResources().getString(R.string.lang));
                }
            }
        });//делаем обработчик события на кнопку;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_city,menu);
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
                    View promptsView = li.inflate(R.layout.city_layout,null);
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
                                        public void onClick(DialogInterface dialog,int id) {
                                            //Вводим текст и отображаем в строке ввода на основном экране:
                                            final_text.setText(userInput.getText());
                                            resources = getResources();
                                            final_text.setBackgroundResource(R.drawable.radius_angle);
                                        }
                                    })
                            .setNegativeButton(R.string.Cancel,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
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
                    mainPresenter.handleSendRequest(city,key,getResources().getString(R.string.units).toString(),getResources().getString(R.string.lang));

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
            mainPresenter.handleSendRequest(city,key,getResources().getString(R.string.units).toString(),getResources().getString(R.string.lang));

        }
    }


    @Override
    public void setDataTime(int index,int temp,String time,String image) {

        listCont.get(index).setBackgroundResource(R.drawable.radius_angle);
        // listCont.get(index).setBackground(getDrawable(R.drawable.background));
        if (time.equals(getResources().getString(R.string.t21)) || time.equals(getResources().getString(R.string.t00)) || time.equals(getResources().getString(R.string.t03))) {
            imageArr.get(index).setImageResource(getImageNight(image));
        } else {
            imageArr.get(index).setImageResource(getImage(image));
        }
        editTextArr.get(index).setText(time);
        textViewArr.get(index).setText(temp + getResources().getString(R.string.cur_temp));

    }

    @SuppressLint({"SetTextI18n","ResourceType"})
    @Override
    public void updateWeatherInfo(int temperature,int approximatelyTemperature,String conditionSkym,String image,String city) {

        logo.setText(getResources().getString(R.string.logo) + " " + city);
        progressBar.setVisibility(View.INVISIBLE);
        result.setText(temperature + getResources().getString(R.string.cur_temp));
        resultFeeling.setText(getResources().getString(R.string.fel_temp) + " " + approximatelyTemperature + getResources().getString(R.string.cur_temp));
        conditionSky.setText(conditionSkym);
        linearLayout.setBackgroundResource(R.drawable.radius_angle);
        time = Calendar.getInstance();
        String str = time.getTime().toString().substring(11,13);
        int hour = Integer.parseInt(str);
        boolean an = hour <= Integer.parseInt(getResources().getString(R.string.t24).substring(0,2)) || hour >= Integer.parseInt(getResources().getString(R.string.t21).substring(0,2));
        if (hour >= Integer.parseInt(getResources().getString(R.string.t21).substring(0,2)) || hour >= Integer.parseInt(getResources().getString(R.string.t24).substring(0,2)) ||
                hour <= Integer.parseInt(getResources().getString(R.string.t03).substring(0,2))) {
            imageView.setImageResource(getImageNight(image));
        } else {
            imageView.setImageResource(getImage(image));
        }

    }


    @Override
    public void wrongData() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
                userField.setText("");
                result.setText(R.string.space);
                resultFeeling.setText(R.string.space);
                conditionSky.setText("");
                Toast.makeText(MainActivity.this,R.string.error,Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void showError(String error) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
                conditionSky.setText(error);
                Toast.makeText(MainActivity.this,R.string.error_request,Toast.LENGTH_LONG).show();
            }
        });

    }

    public boolean isTextEqualToResouce(String text,String resource) {

        if (text.equalsIgnoreCase(resource)) {
            return true;
        }
        return false;
    }


    public void setupView() {
        progressBar.setVisibility(View.INVISIBLE);
        userField.setText("");
        result.setText(R.string.space);
        resultFeeling.setText(R.string.space);
        conditionSky.setText("");
    }

    public int getImage(String image) {

        if (image.equalsIgnoreCase((String) getResources().getString(R.string.partly_cloudy)) || image.equalsIgnoreCase((String) getResources().getString(R.string.some_cloudy))) {
            return R.drawable.sun_figma;
        } else if (image.equalsIgnoreCase((String) getResources().getString(R.string.clean))) {
            return R.drawable.sun;
        } else if (image.equalsIgnoreCase((String) getResources().getString(R.string.cloudy_with_space))) {
            return R.drawable.aun_and_cloud;
        } else if (image.equalsIgnoreCase((String) getResources().getString(R.string.rain)) || image.equalsIgnoreCase(getResources().getString(R.string.mainly_cloudy)) || image.equalsIgnoreCase(getResources().getString(R.string.some_rain))) {
            return R.drawable.rain;
        } else {
            return 0;
        }

    }

    public int getImageNight(String image) {

        if (image.equalsIgnoreCase((String) getResources().getString(R.string.partly_cloudy)) || image.equalsIgnoreCase((String) getResources().getString(R.string.some_cloudy)) || image.equalsIgnoreCase((String) getResources().getString(R.string.cloudy_with_space))) {
            return R.drawable.moon_cloud;
        } else if (image.equalsIgnoreCase((String) getResources().getString(R.string.clean))) {
            return R.drawable.moon;
        } else if (image.equalsIgnoreCase((String) getResources().getString(R.string.rain)) || image.equalsIgnoreCase(getResources().getString(R.string.mainly_cloudy)) || image.equalsIgnoreCase(getResources().getString(R.string.some_rain))) {
            return R.drawable.night_rain;
        } else {
            return 0;
        }

    }


}

