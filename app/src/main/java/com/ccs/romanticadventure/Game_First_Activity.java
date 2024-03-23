package com.ccs.romanticadventure;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ccs.romanticadventure.data.PreferenceConfig;
import com.ccs.romanticadventure.system.ExitConfirmationDialog;
//супер клас головного вікна, бо тільки так буде працювати код підтвердження виходу з програми

public class Game_First_Activity extends MainActivity {

    private WebView webView;
    private int katya, choose, textIndex = 0,value, indexArray, delayBetweenCharacters = 40, //затримка між спавном символів
            delayBetweenTexts = 2000; // затримка між спавнінгом іншого тексту з масиву;

    private static final int dialogContainerId = View.generateViewId(); // Генерируем уникальный идентификатор для контейнера

    float volumeLvl;
    MediaPlayer mediaPlayer;
    boolean type, historyBlockIsVisible = false, animationInProgress;
    private Button history, save, load,buttonElement, buttonSecondElement;
    private RelativeLayout bg;
    private TextView textElement, nameElement;
    private String[] textArray = {"[Я прокидаюсь від звука противного будильника]",
            "[Я намагаюсь нащупати окуляри...",
            "[Знайшов окуляри...]",
            "Одягнути свої окуляри?",
            "[Я почув дивні звуки. Один з них шиплячий, другий - чавкання, а третій - голосний вигук]",
            "[Через хвилину я зрозумів, що це була кішка]",
            "[Ще через декілька секунд чавкала моя сестра Катя]",
            "Катя, не чавкай!",//TODO в блок імені Мама
            "[Я взувся у свої капці і відчинив двері]",
            "[Я пройшов через кімнату сестри і відразу попав до кухні]",
            "[Там стояла мама й там же сиділа за столом моя сестра, наминаючи канапки]",
            "Доброго ранку, нарешті ти прокинувся", //TODO в блок імені Мама
            "Мене терміново викликали на роботу, тож зайди в магазин, будь ласка", //TODO в блок імені Мама
            "[Мама просунула пару купюр, котрими я повинен розрахуватися за покупки]",
            "...",
            "[Дивно, але списку не було...]",
            "[Чи потрібно запитати про список?]",
            "Точно! Ледт не забула, Катю, дай список брату.",//TODO в блок імені Мама
            "[Я дивлюся, як мама збирається та виходить на вулицю]",
            "Вань, купи мені цукерки «Корівка»", //TODO  блок імені Катя і також додати лапки для цукерок (взяти з вьорду) і тепер замість "???" писати в репліку Івана його ім'я
            "...",
            "[Вибору нема...]",
            "Добре, я куплю тобі цукерки, але тільки обіцяй, що не будеш докучати сьогодні ввечері",//TODO в блоці імені - Іван
            "І приставка сьогодні моя!", //TODO в блоці імені Іван
            "Але я повинна пройти боса в грі...",//TODO додати в блоці імені - Катя
            //25 індекс значення на 28 рядку
            "...",
            "Тоді не куплю",//TODO в блок імені Іван
            "[Я забираю список і йду до вхіднох дверей.]",
            "Ну хоть часик, дасиш пограти?...",//TODO в блоці імені Катя
            //29 index on 33 line
            "Дати Каті годинку пограти ввечері?",

            //TODO подветвь "Хорошый братик"
            "Ну...",//TODO в блок імені Іван
            "Годинку дам",//TODO в блок імені Іван + додати +1 до катіної лояльності
            "Добре, домовились",//TODO в блок імені Катя
            "[Я почав збиратися в магазин.]",
            "[Поглянувши на тумбу, я згадав, що цю шапку подарував мені тато.]",
            "[Він привіз мені з Польщі.]",
            "[Я вийшов з дому, сестра закрила за мною двері.]",
            "Хочеться прогулятись. Піду-но я через ліс...",//TODO в блок імені Іван
            "...",
            //40 index in 46 line
            "Через 15 хвилин я захожу в невеличкий магазин «Універсал»",//TODO в блок імені лапки додати з Вьорду

            //TODO підгілка "Поганий братик"
            "[Катя на мене подивилася собачими очима]",
            "[Вона опустила голову, встала і побігла у свою кімнату не доївши]",
            "[Я тільки фиркнув у її сторону і почав збиратися до магазину]",
            "[Я вийшов з дому і пішов через ліс, бо так швидше]",
            "[Через 13 хвилин я заходжу в магазин «Уніка 12/7»]",//TODO лапки....


            //TODO гілка Магазін Уківерсал (не глава)
            //TODO підгілка гарний братик в магазині (тут взагалі буде й поганий братик в магазині) Треба через переевірку sharedPreferences перевірити які бали щоб вивести текст
            "[Я зайшов у магазин та взяв візок]",
            "[Я купив усі потрібні речі та підійшов до стійки з цукерками]",
            "[Я підійшов до коробки з цукерками «Корівка»]", //TODO Лапки з вьорду
            "[Взяв 100 - 120 грам]"};

    @SuppressLint({"SetJavaScriptEnabled", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_first);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        choose = PreferenceConfig.getChoose(this);
        history = findViewById(R.id.buttonHistory);
        save = findViewById(R.id.fastSave_btn);
        load = findViewById(R.id.fastLoad_btn);
        bg = findViewById(R.id.bg);
        textElement = findViewById(R.id.dialog);
        nameElement = findViewById(R.id.name);
        type = PreferenceConfig.getAnimSwitchValue(this);
        buttonElement = findViewById(R.id.first_btn);
        buttonSecondElement = findViewById(R.id.second_btn);
        value = PreferenceConfig.getValue(this);


        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        volumeLvl = PreferenceConfig.getVolumeLevel(this);
        mediaPlayer = MediaPlayer.create(this, R.raw.school);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(volumeLvl, volumeLvl);
        buttonElement.setVisibility(View.INVISIBLE);
        buttonSecondElement.setVisibility(View.INVISIBLE);

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(!historyBlockIsVisible) {
                showHistoryDialog();
            }else{
                hideHistoryDialog();
            }
            }
        });

        animateText();

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quickLoad();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quickSave();
            }
        });
        buttonElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstBtn();
            }
        });
        buttonSecondElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondBtn();
            }
        });





    }
    @Override
    public void onBackPressed() {
        ExitConfirmationDialog.showExitConfirmationDialog(this);
    }

    private void quickLoad() {
        textElement.setText("");
        textIndex = value;
    }

    private void quickSave() {
        // Реализация быстрого сохранения
        PreferenceConfig.setValue(getApplicationContext(), textIndex);
    }

    private void showHistoryDialog() {
        historyBlockIsVisible = true;
        // Создание диалогового контейнера
        LinearLayout dialogContainer = new LinearLayout(this);
        dialogContainer.setId(dialogContainerId);
        dialogContainer.setLayoutParams(new ViewGroup.LayoutParams(
                convertDpToPx(300), // Ширина контейнера
                convertDpToPx(200) // Высота контейнера
        ));
        dialogContainer.setOrientation(LinearLayout.VERTICAL);
        dialogContainer.setBackgroundColor(Color.WHITE); // Белый цвет фона
        dialogContainer.setPadding(convertDpToPx(10), convertDpToPx(10), convertDpToPx(10), convertDpToPx(10)); // Отступы внутри контейнера
        dialogContainer.setBackgroundResource(R.drawable.pink_bg); // Граница контейнера
        dialogContainer.setX(getScreenWidth() / 2f - convertDpToPx(150)); // Положение по горизонтали
        dialogContainer.setY(getScreenHeight() / 2f - convertDpToPx(100)); // Положение по вертикали

        // Создание элемента для отображения текста
        LinearLayout textContainer = new LinearLayout(this);
        textContainer.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        textContainer.setOrientation(LinearLayout.VERTICAL);
        textContainer.setScrollbarFadingEnabled(false);
        textContainer.setVerticalScrollBarEnabled(true);
        textContainer.setHorizontalScrollBarEnabled(false);

        // Добавление каждой строки из массива в элемент текста
        for (int i = 0; i < textIndex; i++) {
            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            textView.setText(textArray[i]);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            textView.setLineSpacing(0, 1.5f);
            textContainer.addView(textView);
            Log.d("TextIndex", String.valueOf(textIndex));
        }

        // Добавление элемента текста в диалоговый контейнер
        dialogContainer.addView(textContainer);

        // Добавление диалогового контейнера в корневую разметку активности
        ((ViewGroup) getWindow().getDecorView().getRootView()).addView(dialogContainer);
    }


    // Метод для преобразования dp в px
    private int convertDpToPx(int dp) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    // Метод для получения ширины экрана
    private int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    // Метод для получения высоты экрана
    private int getScreenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private void hideHistoryDialog() {
        historyBlockIsVisible = false;
        ViewGroup rootView = (ViewGroup) getWindow().getDecorView().getRootView();
        View dialogContainer = rootView.findViewById(dialogContainerId);
        if (dialogContainer != null) {
            rootView.removeView(dialogContainer);
        }
    }

    private void animateText() {
        if (!type) {
            return;
        }
        textElement.setText(""); // Очищаем текстовый элемент

        String newText = textArray[textIndex];
        if (newText.equals("росії немає") || newText.equals("абра")) {
            nameElement.setText("Степан");
        } else {
            nameElement.setText("???"); // Очищаем текст, если условие не выполняется
        }

        animateFrame(0);
    }

    private void animateFrame(final int i) {
        String newText = textArray[textIndex];
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                textElement.append(Character.toString(newText.charAt(i))); // Добавляем символ в текстовый элемент

                if (i < newText.length() - 1) {
                    animateFrame(i + 1);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!type) {
                                return;
                            }
                            textIndex++;

                            if (textIndex == 4 || textIndex == 17 || textIndex == 29) {
                                buttonElement.setVisibility(View.VISIBLE);
                                buttonSecondElement.setVisibility(View.VISIBLE);
                                animationInProgress = false;
                                return;
                            } else {
                                buttonElement.setVisibility(View.GONE);
                                buttonSecondElement.setVisibility(View.GONE);
                            }

                            buttonElement.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    firstBtn();
                                }
                            });
                            buttonSecondElement.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    secondBtn();
                                }
                            });
                            animationInProgress = true;
                            animateText();
                            indexArray = i;
                        }
                    }, delayBetweenTexts);
                }
            }
        }, delayBetweenCharacters);
    }
    private void firstBtn(){

        switch (textIndex){
            case 3:
                textIndex = 4;
                break;
            case 17:
                nameElement.setText("???");
                textElement.setText("А що ж мені купити? Список дасиш, як минулого разу?");
                textIndex = 18;
                break;
        }

    }
    private void secondBtn(){
        switch (textIndex){
            case 3:
                nameElement.setText("???");
                textElement.setText("Ні, так не піде");
                textIndex = 3;
                break;
            case 17:
                nameElement.setText("???");
                textElement.setText("А що ж мені купити? Список дасиш, як минулого разу?");
                textIndex = 18;
                break;
        }


    }



}
