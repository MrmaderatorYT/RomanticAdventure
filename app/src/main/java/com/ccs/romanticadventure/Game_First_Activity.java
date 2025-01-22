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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Game_First_Activity extends MainActivity {

    private WebView webView;
    private int katya, choose, textIndex = 0, value, indexArray, delayBetweenCharacters = 40, //затримка між спавном символів
            delayBetweenTexts = 2000; // затримка між спавнінгом іншого тексту з масиву;

    private static final int dialogContainerId = View.generateViewId(); // Генерируем уникальный идентификатор для контейнера
    private List<Integer> specialIndexes = Arrays.asList(0, 1, 3); // Індекси, які потрібно перевіряти

    float volumeLvl;
    MediaPlayer mediaPlayer;
    boolean type, historyBlockIsVisible = false, animationInProgress;
    private Button history, save, load;
    private RelativeLayout bg;
    private TextView textElement, nameElement;
    private ArrayList<Pair> textArray = new ArrayList<>();
    private LinearLayout buttonContainer;


    private static class Pair {
        String name;
        String text;
        int value;
        List <Choice> choices;

        Pair(int value, String name, String text, List<Choice> choices) {
            this.name = name;
            this.text = text;
            this.value = value;
            this.choices = choices;

        }
    }
    private static class Choice {
        String text;
        int nextIndex;

        Choice(String text, int nextIndex) {
            this.text = text;
            this.nextIndex = nextIndex;
        }
    }

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
        buttonContainer = findViewById(R.id.button_container);
        value = PreferenceConfig.getValue(this);

        initializeTextArray();

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        volumeLvl = PreferenceConfig.getVolumeLevel(this);
        mediaPlayer = MediaPlayer.create(this, R.raw.school);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(volumeLvl, volumeLvl);

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!historyBlockIsVisible) {
                    showHistoryDialog();
                } else {
                    hideHistoryDialog();
                }
            }
        });

        // Start text animation
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
        // Створення діалогового контейнера
        LinearLayout dialogContainer = new LinearLayout(this);
        dialogContainer.setId(dialogContainerId);
        dialogContainer.setLayoutParams(new ViewGroup.LayoutParams(
                convertDpToPx(300), // Ширина контейнера
                convertDpToPx(200) // Висота контейнера
        ));
        dialogContainer.setOrientation(LinearLayout.VERTICAL);
        dialogContainer.setBackgroundColor(Color.WHITE); // Білий колір фону
        dialogContainer.setPadding(convertDpToPx(10), convertDpToPx(10), convertDpToPx(10), convertDpToPx(10)); // Відступи всередині контейнера
        dialogContainer.setBackgroundResource(R.drawable.pink_bg); // Границя контейнера
        dialogContainer.setX(getScreenWidth() / 2f - convertDpToPx(150)); // Положення по горизонталі
        dialogContainer.setY(getScreenHeight() / 2f - convertDpToPx(100)); // Положення по вертикалі

        // Створення елемента для відображення тексту
        LinearLayout textContainer = new LinearLayout(this);
        textContainer.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        textContainer.setOrientation(LinearLayout.VERTICAL);
        textContainer.setScrollbarFadingEnabled(false);
        textContainer.setVerticalScrollBarEnabled(true);
        textContainer.setHorizontalScrollBarEnabled(false);

        // Додавання кожного ключа та значення з HashMap до текстового елемента
        for (int i = 0; i < textIndex; i++) {
            Pair pair = textArray.get(i);

            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            textView.setText(pair.name + ": " + pair.text);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            textView.setLineSpacing(0, 1.5f);
            textContainer.addView(textView);
            Log.d("TextIndex", String.valueOf(textIndex));
        }

        // Додавання текстового елемента до діалогового контейнера
        dialogContainer.addView(textContainer);

        // Додавання діалогового контейнера до кореневої розмітки активності
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
        if (textIndex < textArray.size()) {
            Pair pair = textArray.get(textIndex);

            // Якщо ключ не містить спешл значення, показати ім'я
            if (specialIndexes.contains(textIndex)) {
                nameElement.setText(pair.name);
            } else {
                nameElement.setText("Кейт");
            }

            textElement.setText("");
            String textToAnimate = pair.text;
            animationInProgress = true;
            new Handler().postDelayed(new Runnable() {
                int i = 0;

                @Override
                public void run() {
                    if (i < textToAnimate.length()) {
                        textElement.append(String.valueOf(textToAnimate.charAt(i)));
                        i++;
                        new Handler().postDelayed(this, delayBetweenCharacters);
                    } else {
                        textIndex++;
                        animationInProgress = false;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                animateText(); // Call the method recursively to show the next text
                            }
                        }, delayBetweenTexts);
                    }
                }
            }, delayBetweenCharacters);
        } else {
            textElement.setText("");
        }
    }

    private void initializeTextArray() {
//        List<Choice> choices1 = Arrays.asList(
//                new Choice("Так", 11),
//                new Choice("Ні", 12)
//        );
//
//        List<Choice> choices2 = Arrays.asList(
//                new Choice("Так", 21),
//                new Choice("Ні", 22)
//        );
//        textArray.add(new Pair(0, "", "", choices1));
//        textArray.add(new Pair(1, "", "", choices2));


        textArray.add(new Pair(0, "", "*Я просыпaюсь от звукa противного будильникa...*", null));
        textArray.add(new Pair(1, "", "*Я пытaюсь нaщупaть очки...*", null));
        textArray.add(new Pair(2, "", "*Я нaшел свои очки*", null));
        textArray.add(new Pair(3, "", "[Выбор одеть очки или нет]", null));
        textArray.add(new Pair(4, "", "#скрежет#", null));
        textArray.add(new Pair(5, "Мысли", "/Что это было.. Наверное показалось/", null));
        textArray.add(new Pair(6, "", "#Стук в окно#", null));
        textArray.add(new Pair(7, "Мысли", "/Наверное ветер/", null));
        textArray.add(new Pair(8, ".", "#Стук в окно#", null));
        textArray.add(new Pair(9, ".", "/Что же это ? Надо открыть шторы, скоро всё равно должно взойти солнце/", null));
        textArray.add(new Pair(10, ".", "*Иван открыл шторы*", null));
        textArray.add(new Pair(11, ".", "Иван: Не видно ни черта! Темно как в жопе. Да ещё и подозрительно тихо.", null));
        textArray.add(new Pair(12, ".", "*В окно прилетает камень*", null));
        textArray.add(new Pair(13, ".", "/ЧТО ? ОТКУДА?/", null));
        textArray.add(new Pair(14, ".", "*Иван прищюрился*", null));
        textArray.add(new Pair(15, ".", "/Что же это могло быть/", null));
        textArray.add(new Pair(16, ".", "*Иван заметил силуэт*", null));
        textArray.add(new Pair(17, ".", "/Что же это ? Похоже на медведя.. или это олень. И это он бросал камни ? Может это всё сон/", null));
        textArray.add(new Pair(18, ".", "*По спине Ивана пробежал холодок и Иван чихнул*", null));
        textArray.add(new Pair(19, ".", "Иван: А.. А Где Зверь ?..", null));
        textArray.add(new Pair(20, ".", "*Иван услышал быстрые шаги приближающихся к двери в свою комнату*", null));
        textArray.add(new Pair(21, ".", "*Иван насторожился*", null));
        textArray.add(new Pair(22, ".", "*Дверь приоткрылась*", null));
        textArray.add(new Pair(23, ".", "[Выбор: Посмотреть что скрывается за дверью ?]", null));
        textArray.add(new Pair(24, ".", "[Нет]", null));
        textArray.add(new Pair(25, ".", "[Да]", null));
        textArray.add(new Pair(26, ".", "Глaвa 1: Овертюрa", null));
        textArray.add(new Pair(27, ".", "*Я просыпaюсь от звукa противного будильникa...*", null));
        textArray.add(new Pair(28, ".", "Ветвь: Дом, милый дом...", null));
        textArray.add(new Pair(29, ".", "*Нет! Тaк не пойдет.*", null));
        textArray.add(new Pair(30, ".", "*Я пытaюсь нaщупaть очки...*", null));
        textArray.add(new Pair(31, ".", "*Я нaшел свои очки*", null));
        textArray.add(new Pair(32, ".", "[Выбор одеть очки или нет]", null));
        textArray.add(new Pair(33, ".", "[Нет]", null));
        textArray.add(new Pair(34, ".", "[Дa]", null));
        textArray.add(new Pair(35, ".", "#Несколько звуков доснелось с кухни. Один был шипением, второй чaвкaнием a третий громким предложением...#", null));
        textArray.add(new Pair(36, ".", "*Через минуту я понял что шипелa кошкa*", null));
        textArray.add(new Pair(37, ".", "*Еще через несколько секунд чaвкaлa моя сестрa Кaтя*", null));
        textArray.add(new Pair(38, ".", "*Еще через несколько секунд я понял что предложение скaзaлa моя мaмa. Онa скaзaлa Кaтя, не чaвкaй*", null));
        textArray.add(new Pair(39, ".", "*Я одел свои тaпки и открыл дверь.*", null));
        textArray.add(new Pair(40, ".", "*Я прошел через комнaту сестры и срaзу попaл нa кухню где стоялa мaмa и сиделa елa Кaтя*", null));
        textArray.add(new Pair(41, ".", "Мaмa: О! Проснулся нaконец! Я побежaлa нa рaботу a ты пойдешь в мaгaзин.", null));
        textArray.add(new Pair(42, ".", "*Мaмa сунулa мне в руку несколько купюр нa которые я должен был купить продукты. Но стрaнно, спискa нету.*", null));
        textArray.add(new Pair(43, ".", "[Спросить про список]", null));
        textArray.add(new Pair(44, ".", "[Дa]", null));
        textArray.add(new Pair(45, ".", "Протaгонист: A что мне купить? Может список есть кaк в прошлый рaз?", null));
        textArray.add(new Pair(46, ".", "[Нет]", null));
        textArray.add(new Pair(47, ".", "Мaмa: Точно! Чуть не зaбылa. Кaтя, отдaй список брaту.", null));
        textArray.add(new Pair(48, ".", "*Я смотрю кaк мaмa собирaется и уходит.*", null));
        textArray.add(new Pair(49, ".", "Имя рaзблокировaно! Протaгонист -> Ивaн", null));
        textArray.add(new Pair(50, ".", "Кaтя: Ивaн, купи мне пожaлуйстa конфет коровкa.", null));
        textArray.add(new Pair(51, ".", "*Выходa нет...*", null));
        textArray.add(new Pair(52, ".", "Ивaн: Хорошо. Я куплю тебе конфет, только обещaй что сегодня вечером ты не будешь меня достaвaть. И пристaвкa вечером моя.", null));
        textArray.add(new Pair(53, ".", "Кaтя: Ну... нет. Я сегодня должнa пройти яйцо-боссa!", null));
        textArray.add(new Pair(54, ".", "Ивaн: Тогдa не куплю.", null));
        textArray.add(new Pair(55, ".", "*Я беру и нaчинaю отходить от кухонного столa и двигaюсь в нaпрaвлении входной двери*", null));
        textArray.add(new Pair(56, ".", "Кaтя: Ну хоть чaсик дaшь поигрaть?..", null));
        textArray.add(new Pair(57, ".", "[Дaть Кaте поигрaть чaсик вечером]", null));
        textArray.add(new Pair(58, ".", "[Не дaть Кaте игрaть]", null));
        textArray.add(new Pair(59, ".", "ПодВетвь: Злой брaтик", null));
        textArray.add(new Pair(60, ".", "[Дaть 1 чaс]", null));
        textArray.add(new Pair(61, ".", "*Она ударила меня и я умер*", null));
        textArray.add(new Pair(62, ".", "ПодВетвь: Добрый брaтик", null));
        textArray.add(new Pair(63, ".", "Ивaн: Нуу.. Чaсик дaм.", null));
        textArray.add(new Pair(64, ".", "Кaтя: Хорошо. Договорились!", null));
        textArray.add(new Pair(65, ".", "*Я нaчaл собирaться в мaгaзин. И нaшел свою шaпку которую привез мой пaпa из Польши*", null));
        textArray.add(new Pair(66, ".", "*Я вышел из домa и зaкрыл дверь нa зaмок кaк мaмa просилa двa дня нaзaд. Я пошел в обход лесa*", null));
        textArray.add(new Pair(67, ".", "*Через 15 минут я зaхожу в мaленький мaгaзин Мaгaзин 'УНИВЕРСAЛ' 24/7", null));
        textArray.add(new Pair(68, ".", "*Я зашёл в ларёк. Он был небольшим, но в нём много чего продавалось*", null));
        textArray.add(new Pair(69, ".", "*Я решил посмотреть ассортимент магазина. Может что-то нового добавили*", null));
        textArray.add(new Pair(70, ".", "*Эх... Жаль, ничено нового. Что ж, возьму что в списке прописала мама*", null));
        textArray.add(new Pair(71, ".", "*Я взял 3 огурцa именовaнных кaк 'Усские' и пошел к стенду с помидорaми*", null));
        textArray.add(new Pair(72, ".", "*Подходя к стенду с помидорaми я нaступил нa лук и чуть ли не упaв в кого то влетел*", null));
        textArray.add(new Pair(73, ".", "*Я влетел в Тётю Петровну, онa рaботaет тут кaк менеджер*", null));
        textArray.add(new Pair(74, ".", "Петровнa: Ивaн!? Тебя опять мaмa зaстaвилa пойти зa покупкaми?", null));
        textArray.add(new Pair(75, ".", "Иван: Угу", null));
        textArray.add(new Pair(76, ".", "*И дал я свой список*", null));
        textArray.add(new Pair(77, ".", "Петрована пошла брать продукты из списка", null));
        textArray.add(new Pair(78, ".", "*Я решил прогуляться по магазинчику и увидел стенд с конфетами, которые хотела моя сестра*", null));
        textArray.add(new Pair(79, ".", "[Купить конфеты сестре?]", null));
        textArray.add(new Pair(80, ".", "Нет", null));
        textArray.add(new Pair(81, ".", "Под ветвь [Смертный приговор]", null));
        textArray.add(new Pair(82, ".", "*Я же сказал, что не куплю конфеты, что же сейчас должно измениться?*", null));
        textArray.add(new Pair(83, ".", "Да", null));
        textArray.add(new Pair(84, ".", "*Я иду к Петровне что бы забрать пакет с продуктами*", null));
        textArray.add(new Pair(85, ".", "Петровна: С тебя 119 грн", null));
        textArray.add(new Pair(86, ".", "Ваня: Можно ещё конфеты Коровка?", null));
        textArray.add(new Pair(87, ".", "*Я даю ровно 119 грн и ухожу*", null));
        textArray.add(new Pair(88, ".", "Петровна: Хорошо", null));
        textArray.add(new Pair(89, ".", "[Куда идти?]", null));
        textArray.add(new Pair(90, ".", "Прогуляться", null));
        textArray.add(new Pair(91, ".", "*Я решил прогуляться*", null));
        textArray.add(new Pair(92, ".", "Зайду-ка я в лес*", null));
        textArray.add(new Pair(93, ".", "Ваня: Эх... какая-же природа Украины прекрастна. Что не глянь в любую сторону, то сразу идеальные пейзажи!", null));
        textArray.add(new Pair(94, ".", "*Я подождал пару минут и мне дала Павловна пакет.*", null));
        textArray.add(new Pair(95, ".", "Домой", null));
        textArray.add(new Pair(96, ".", "*Любуюсь видами леса*", null));
        textArray.add(new Pair(97, ".", "*Я сразу пошёл домой*", null));
        textArray.add(new Pair(98, ".", "Петровна: С тебя 140 грн", null));
        textArray.add(new Pair(99, ".", "Ваня: Что ж, пора возвращаться", null));
        textArray.add(new Pair(100, ".", "Задержка текста на 5 сек", null));
        textArray.add(new Pair(101, ".", "*Идя домой, увидел озеро, возле которого я люблю проводить время на едине, но сейчас уже не время*", null));
        textArray.add(new Pair(102, ".", "Ваня: Вот", null));
        textArray.add(new Pair(103, ".", "*БАЦ*", null));
        textArray.add(new Pair(104, ".", "Звук спотыкания", null));
        textArray.add(new Pair(105, ".", "Петровна: Спасибо", null));
        textArray.add(new Pair(106, ".", "Ваня: ай-ай-ай. Надо же было так споткуться... Блин, теперь неудобно идти, а если опоздаю, то мама будет рагать...", null));
        textArray.add(new Pair(107, ".", "*Думаю нужно вернуться домой, что бы дать конфеты сестре*", null));
        textArray.add(new Pair(108, ".", "*Вижу подорожник*", null));
        textArray.add(new Pair(109, ".", "***", null));
        textArray.add(new Pair(110, ".", "*Я пошёл быстро домой*", null));
        textArray.add(new Pair(111, ".", "*Завязав колено подорожником я начал ускоряться, но так и до конца шкандыбал*", null));
        textArray.add(new Pair(112, ".", "*Спустя пару минут я пришёл домой*", null));
        textArray.add(new Pair(113, ".", "*Прийдя домой, мама уже была дома, она посмотрела на тебя*", null));
        textArray.add(new Pair(114, ".", "*Открыв двери, Катя стояла возле двери*", null));
        textArray.add(new Pair(115, ".", "Мама: О Боже, где тебя носило? Быстро залетай домой, сейчас лечить тебя будем, неуклюжего", null));
        textArray.add(new Pair(116, ".", "*Я послушно зашёл, разулся и пошёл в свою комнату*", null));
        textArray.add(new Pair(117, ".", "Катя: Ну что, купил?", null));
        textArray.add(new Pair(118, ".", "Мама крича: Ваня, ты где?", null));
        textArray.add(new Pair(119, ".", "Ваня: Конечно, держи", null));
        textArray.add(new Pair(120, ".", "Ваня: идууууууу.....", null));
        textArray.add(new Pair(121, ".", "*Сестра улыбнулась*", null));
        textArray.add(new Pair(122, ".", "*Возвращяясь назад, я взял пакет возле входа и пошёл в кухню*", null));
        textArray.add(new Pair(123, ".", "Мама: О, молодец,денег хватило?", null));
        textArray.add(new Pair(124, ".", "Ваня: ещё 20 гривен осталось", null));
        textArray.add(new Pair(125, ".", "Мама: Можешь себе оставить", null));
        textArray.add(new Pair(126, ".", "Ваня: Спасибо", null));
        textArray.add(new Pair(127, ".", "*И стало слишком тихо*", null));
        textArray.add(new Pair(128, ".", "Мама: Ой, совсем забыла, сейчас чинить будем тебя", null));
        textArray.add(new Pair(129, ".", "*Мама взяла бинт и перекись и начала обматывать рану на колене*", null));
        textArray.add(new Pair(130, ".", "Черный экран, типо закрыл глаза", null));
        textArray.add(new Pair(131, ".", "Мама: Вот и готово", null));
        textArray.add(new Pair(132, ".", "Мама: Давно ты не издавал никаких звуков когда тебя мы лечили", null));
        textArray.add(new Pair(133, ".", "Ваня: Ага.... Спасибо большое за помощь", null));
        textArray.add(new Pair(134, ".", "Мама: Прийму как благодарность в том случае, если будешь внимательнее", null));
        textArray.add(new Pair(135, ".", "Ваня: Хорошо...", null));
        textArray.add(new Pair(136, ".", "*Ты слышишь шаги*", null));
        textArray.add(new Pair(137, ".", "*Оказиваеться это сестра*", null));
        textArray.add(new Pair(138, ".", "Мама: Раз все в сборе, будете есть?", null));
        textArray.add(new Pair(139, ".", "Сестра: Без него", null));
        textArray.add(new Pair(140, ".", "*Катя разворачиваеться и уходит назад к себе в комнату*", null));
        textArray.add(new Pair(141, ".", "Мама: что случилось уже с вами?", null));
        textArray.add(new Pair(142, ".", "Ваня: Ну...Она просила купить её конфеты, а я отказался. У нас фильм в это время разные идут и она сказала что даст мне половину своего фильма посмотреть, а потом она посмотрит вторую половину своего", null));
        textArray.add(new Pair(143, ".", "[Сказать правду?]", null));
        textArray.add(new Pair(144, ".", "Да", null));
        textArray.add(new Pair(145, ".", "Мама: Понятно, но это же не повод ссориться за то что конфет не купил. Я Поговрю с ней", null));
        textArray.add(new Pair(146, ".", "Ваня: Спасибо", null));
        textArray.add(new Pair(147, ".", "Нет", null));
        textArray.add(new Pair(148, ".", "*Мама пошла к Кати и они что-то там неразборчиво говрили в течении 20 минут, пока ты в это время ужинал сам*", null));
        textArray.add(new Pair(149, ".", "*Почти закончивши ужин, мама и Катя заходят в кухню*", null));
        textArray.add(new Pair(150, ".", "Сестра: ...", null));
        textArray.add(new Pair(151, ".", "Сестра: Я обиделась на тебя, по тому что просто хочу конфеты, я их очень давно не ела. Они мои любимые. Но... Но.... Также сегодня последняя серия мультика, по этому я не могу дать тебе посмотреть твой фильм", null));
        textArray.add(new Pair(152, ".", "*Жаль, я сегодня планировал посмотреть повтор фильма, который я пропустил. Я бы уговаривал её, на счёт того что бы она дала мне фильм посмотреть, но зади мама", null));
        textArray.add(new Pair(153, ".", "Ваня: Ну... Хорошо... В следуйщий раз, я буду смотреть", null));
        textArray.add(new Pair(154, ".", "Сеста: Да, конечно", null));
        textArray.add(new Pair(155, ".", "* И тут я увидел небольшую улыбку своей сестры*", null));
        textArray.add(new Pair(156, ".", "*Милота....*", null));
        textArray.add(new Pair(157, ".", "Мама: Вот и закончим. Надеюсь таких ситуаций будет редко", null));
        textArray.add(new Pair(158, ".", "Ваня и Катя: Дааааа....", null));
        textArray.add(new Pair(159, ".", "Мама: Катя, иди кушать. Сынок, тебе есть чем заняться?", null));
        textArray.add(new Pair(160, ".", "Ваня:Да конечно", null));
        textArray.add(new Pair(161, ".", "*На самом делея обманул так как ничего интересного в моей жизни да и комнате нет", null));
        textArray.add(new Pair(162, ".", "*Я иду в свою комнату, раздеваюсь, ложусь на кровать и засынаю*", null));
        textArray.add(new Pair(163, ".", "*Я оказался в лесу... В очень тёном лесу...*", null));
        textArray.add(new Pair(164, ".", "Ваня: интересно...", null));
        textArray.add(new Pair(165, ".", "*звуки крика медведя*", null));
        textArray.add(new Pair(166, ".", "Ваня: Бежать отсюда к чёрту... Какое счастье что это сон", null));
        textArray.add(new Pair(167, ".", "???: Я так не думаю, хе-хе", null));
        textArray.add(new Pair(168, ".", "Тут чёрный экран", null));
        textArray.add(new Pair(169, ".", "Ваня: А где свет?", null));
        textArray.add(new Pair(170, ".", "???: Бу", null));
        textArray.add(new Pair(171, ".", "Спавним фотку мишки", null));
        textArray.add(new Pair(172, ".", "*Я рефлекторно ударил её по лицу*", null));
        textArray.add(new Pair(173, ".", "???: Ай-ай-ай... Больно", null));
        textArray.add(new Pair(174, ".", "Ваня: Ты кто?", null));
        textArray.add(new Pair(175, ".", "???: Можешь называть меня Мишка, я твой страх", null));
        textArray.add(new Pair(176, ".", "*Мой страх? Та чёрт с ним, это же девчёнка говорит что звать её надо в мужском роде*", null));
        textArray.add(new Pair(177, ".", "Ваня: Л-ладно...", null));
        textArray.add(new Pair(178, ".", "Мишка: Умничка что подхватываешь всё на ходу", null));
        textArray.add(new Pair(179, ".", "Ваня: ...", null));
        textArray.add(new Pair(180, ".", "Мишка: Люблю таких, хе-хе", null));
        textArray.add(new Pair(181, ".", "Ваня: Славв Богу, что это лишь сон", null));
        textArray.add(new Pair(182, ".", "Мишка: Это да, но ты в своём сне, где управляю именно я и по этому только я могу тебе разрешить проснуться, хе-хе", null));
        textArray.add(new Pair(183, ".", "Ваня: ...", null));
        textArray.add(new Pair(184, ".", "Мишка: Вижу что ты в ужасе", null));
        textArray.add(new Pair(185, ".", "Ваня: Есть такое", null));
        textArray.add(new Pair(186, ".", "*Надо подавать вид, что у меня железные яйца, но на самом деле я щас закричу*", null));
        textArray.add(new Pair(187, ".", "Мишка: Хм, я думаю пока что для нашей первой встречи. Так что на этой ноте попрощаемся. Увидимся ночью, хе-хе", null));
        textArray.add(new Pair(188, ".", "*Я резко встаю с кровати весь в поту*", null));
        textArray.add(new Pair(189, ".", "Ваня: Часы показывают 6 часов утра... Серйозно?", null));
        textArray.add(new Pair(190, ".", "*Ну, мой портфель всегда собран так что может пойти в школу", null));
        textArray.add(new Pair(191, ".", "*Так как я вышел рано, то можно и по лесу прогуляться*", null));
        textArray.add(new Pair(192, ".", "Ваня: Надеюсь еда в столовке будет нормальная", null));
        textArray.add(new Pair(193, ".", "[Пойти прямо сейчас в школу?]", null));
        textArray.add(new Pair(194, ".", "Да", null));
        textArray.add(new Pair(195, ".", "*Что ж делать нечего, пойду в школу*", null));
        textArray.add(new Pair(196, ".", "Нет", null));
        textArray.add(new Pair(197, ".", "*Идя по лесу, я внимательно следил за всем происходящим вокруг меня, что бы не упасть как вчера*", null));
        textArray.add(new Pair(198, ".", "*Спустя час, я наконец-то в школе*", null));
        textArray.add(new Pair(199, ".", "*Уроки идут медленно, а я как всегда не слушаю, а просто пялусь в окно*", null));
        textArray.add(new Pair(200, ".", "*...*", null));
        textArray.add(new Pair(201, ".", "Задержка на 5 сек", null));
        textArray.add(new Pair(202, ".", "*Так у меня проходит каждый день*", null));
        textArray.add(new Pair(203, ".", "*Друзей у меня нет*", null));
        textArray.add(new Pair(204, ".", "*Мне они не нужны*", null));
        textArray.add(new Pair(205, ".", "*...*", null));
        textArray.add(new Pair(206, ".", "*Единственное что я хочу, то это девушку*", null));
        textArray.add(new Pair(207, ".", "*Конечно, желательно что бы были одногодки, но на год старше, тоже допустимо*", null));
        textArray.add(new Pair(208, ".", "Звук звонка", null));
        textArray.add(new Pair(209, ".", "Ваня: фух... Наконец-то домой.", null));
        textArray.add(new Pair(210, ".", "*На этот раз я пойду домой быстрой дорогой, не через лес, а то отдохнуть охота", null));
        textArray.add(new Pair(211, ".", "Тут задержка на пару секунд с фоном леса", null));
        textArray.add(new Pair(212, ".", "...", null));
        textArray.add(new Pair(213, ".", "Ваня: Наконец-то дома", null));
        textArray.add(new Pair(214, ".", "*Войдя домой, я вижу как Катя кушает хлопья*", null));
        textArray.add(new Pair(215, ".", "Ваня: Привет, как дела?", null));
        textArray.add(new Pair(216, ".", "Катя: нормально, мама если что, пошла за покупками", null));
        textArray.add(new Pair(217, ".", "Ваня: Понятно, ну если что, я посплю тогда", null));
        textArray.add(new Pair(218, ".", "Катя: Та делай что хочешь", null));
        textArray.add(new Pair(219, ".", "* Я залетаю в свою комнату, раздеваюсь и прыгаю на кровать*", null));
        textArray.add(new Pair(220, ".", "...", null));
        textArray.add(new Pair(221, ".", "*Я резко засыпаю*", null));
        textArray.add(new Pair(222, ".", "???: Привееет!", null));
        textArray.add(new Pair(223, ".", "Ваня: ...", null));
        textArray.add(new Pair(224, ".", "Ваня: Привет", null));
        textArray.add(new Pair(225, ".", "Мишкa И так... Ми не закончили в прошлую встречу", null));
        textArray.add(new Pair(226, ".", "Ваня: та мне и прошлого раза хватило, если честно", null));
        textArray.add(new Pair(227, ".", "Мишка: Ха-ха, ты забавный)", null));
        textArray.add(new Pair(228, ".", "Ваня: ...", null));
        textArray.add(new Pair(229, ".", "мишка: И так... Сегодня поговорим о тебе. Ты странный человек, често говоря", null));
        textArray.add(new Pair(230, ".", "Ваня: ...", null));
        textArray.add(new Pair(231, ".", "Мишка:Таких как ты - мало и они в целом не всегда понимают где...", null));
        textArray.add(new Pair(232, ".", "Мишка: Хм...", null));
        textArray.add(new Pair(233, ".", "Мишка: Лучше не буду говорить", null));
        textArray.add(new Pair(234, ".", "Ваня: то есть ты говоришь мне про проблемы. а саму проблему не хочешь говорить?", null));
        textArray.add(new Pair(235, ".", "Мишка: Именно", null));
        textArray.add(new Pair(236, ".", "Ваня: ну это же мой сон, я хочу что бы ты сказала мне", null));
        textArray.add(new Pair(237, ".", "Мишка: Напомню, это твой сон, но контролирую здесь я", null));
        textArray.add(new Pair(238, ".", "*Черт я совсем забыл*", null));
        textArray.add(new Pair(239, ".", "Мишка: Ну, единственное что я тебе скажу - будь акуратнее", null));
        textArray.add(new Pair(240, ".", "Ваня: Где именно? В ногах, в людях? Перестань головоломки ставить. Парни не понимают намёков, скажи прямо", null));
        textArray.add(new Pair(241, ".", "Мишка: Ты забавный, так бы съела, ну ладно... Просто если малейший вопрос будет  тебя в голове, то просто детально проанализируй", null));
        textArray.add(new Pair(242, ".", "Ваня: ...", null));
        textArray.add(new Pair(243, ".", "Ваня: Ладно?", null));
        textArray.add(new Pair(244, ".", "Мишка: Та ты сам поймёшь)", null));
        textArray.add(new Pair(245, ".", "Ваня: Ясно...", null));
        textArray.add(new Pair(246, ".", "Мишка: Пасмурно", null));
        textArray.add(new Pair(247, ".", "Мишка: ладно, на этом пока всё", null));
        textArray.add(new Pair(248, ".", "Ваня: Да-да", null));
        textArray.add(new Pair(249, ".", "*Я вннезапно проснулся*", null));
        textArray.add(new Pair(250, ".", "Ваня: теперь опять в школу", null));
        textArray.add(new Pair(251, ".", "*Я оделся, взял еду, которая мама со вчерашнего дня оставила*", null));
        textArray.add(new Pair(252, ".", "*Опять пройдусь длинным путём*", null));
        textArray.add(new Pair(253, ".", "...", null));
        textArray.add(new Pair(254, ".", "*Какая красота в лесу*", null));
        textArray.add(new Pair(255, ".", "*Прийдя в школу, я сел за парту и опять провалился в облака*", null));
        textArray.add(new Pair(256, ".", "звук звонока", null));
        textArray.add(new Pair(257, ".", "*звук на переменку, пойду-ка я прогуляюсь*", null));
        textArray.add(new Pair(258, ".", "*Я вышел со школы, что бы намотать пару кругов во круг поля*", null));
        textArray.add(new Pair(259, ".", "*Я увидел ученицу, которая сидит на лавочке и следит за мной*", null));
        textArray.add(new Pair(260, ".", "*Я не буду подходить к ней так как мне нет дела к ней, лучше пойду в клас*", null));
        textArray.add(new Pair(261, ".", "*Зайдя в класс, я опять провалился в мечты*", null));
        textArray.add(new Pair(262, ".", "[Подойти к ней?]", null));
        textArray.add(new Pair(263, ".", "Нет", null));
        textArray.add(new Pair(264, ".", "Звук звонка", null));
        textArray.add(new Pair(265, ".", "Да", null));
        textArray.add(new Pair(266, ".", "Ваня: Ну слава Богу, это конец", null));
        textArray.add(new Pair(267, ".", "*Я встал и пошёл дальше гулять по територии школы*", null));
        textArray.add(new Pair(268, ".", "*На том же месте сидит та девочка которая была и в обед*", null));
        textArray.add(new Pair(269, ".", "*Я её никогда не видел, наверное потому что не обращаю внимание на окружающих*", null));
        textArray.add(new Pair(270, ".", "*Я сел с другой стороны поля*", null));
        textArray.add(new Pair(271, ".", "*Посидел пару минут и лёг. Я смотрел в небо до того момента, как не уснул*", null));
        textArray.add(new Pair(272, ".", "Мишка: Бу", null));
        textArray.add(new Pair(273, ".", "Ваня:..", null));
        textArray.add(new Pair(274, ".", "Мишка: Что, уже не пугает?", null));
        textArray.add(new Pair(275, ".", "Ваня: не совсем уже", null));
        textArray.add(new Pair(276, ".", "*Кому я это говорю?*", null));
        textArray.add(new Pair(277, ".", "Мишка: Ну ладно, как знаешь, я буду страшнее тогда", null));
        textArray.add(new Pair(278, ".", "Ваня:...", null));
        textArray.add(new Pair(279, ".", "Ваня: Так что, зачем я опять здесь?", null));
        textArray.add(new Pair(280, ".", "Мишка: А, точно. Ты же видел ту девочку?", null));
        textArray.add(new Pair(281, ".", "Ваня: Мою маму?", null));
        textArray.add(new Pair(282, ".", "Мишка: Шутник)", null));
        textArray.add(new Pair(283, ".", "Ваня: Весь в тебя", null));
        textArray.add(new Pair(284, ".", "Мишка: Ну ладно, вот та девчонка которая навпротив тебя сидит - её нельзя допустить к твоему сердцу", null));
        textArray.add(new Pair(285, ".", "Ваня: Что?", null));
        textArray.add(new Pair(286, ".", "Мишка: Что бы было тебе легче, скажу тебе сразу - она не настоящая", null));
        textArray.add(new Pair(287, ".", "Ваня: То есть? Не искренная с эмоциями?", null));
        textArray.add(new Pair(288, ".", "Мишка: ...", null));
        textArray.add(new Pair(289, ".", "Ваня: И это тоже", null));
        textArray.add(new Pair(290, ".", "Ваня: И что ты хочешь что бы я сделал?", null));
        textArray.add(new Pair(291, ".", "Ваня: Избегай её", null));
        textArray.add(new Pair(292, ".", "Ваня: Зачем?", null));
        textArray.add(new Pair(293, ".", "Мишка: Просто сделай", null));
        textArray.add(new Pair(294, ".", "Мишка: Зачем?", null));
        textArray.add(new Pair(295, ".", "Ваня: Девушку хочу я!", null));
        textArray.add(new Pair(296, ".", "Мишка: ха", null));
        textArray.add(new Pair(297, ".", "Мишка: ха-ха", null));
        textArray.add(new Pair(298, ".", "Мишка: ха-ха-ха-ха-ха-ха-ха-ха", null));
        textArray.add(new Pair(299, ".", "Мишка: Ну да, ты же парень...", null));
        textArray.add(new Pair(300, ".", "Ваня: В смысле? что то не так?", null));
        textArray.add(new Pair(301, ".", "Мишка: Нет, просто пожалуйста, избегай её и ударь её когда будешь один, если получиться - окей, встречайся и делай что хочешь", null));
        textArray.add(new Pair(302, ".", "Ваня: Окей", null));
        textArray.add(new Pair(303, ".", "Мишка: Я много букв сказала... На этом всё", null));
        textArray.add(new Pair(304, ".", "*Я проснулся и открыл глаза*", null));
        textArray.add(new Pair(305, ".", "*Та девочка смотрит на меня и стоит возле меня*", null));
        textArray.add(new Pair(306, ".", "???: П-Привет", null));
        textArray.add(new Pair(307, ".", "Ваня: Эм... Привет?", null));
        textArray.add(new Pair(308, ".", "*Та девочка улыбнулась*", null));
        textArray.add(new Pair(309, ".", "...", null));
        textArray.add(new Pair(310, ".", "*Милота....*", null));
        textArray.add(new Pair(311, ".", "???: Я... Я Соня", null));
        textArray.add(new Pair(312, ".", "Ваня: Я Ваня", null));
        textArray.add(new Pair(313, ".", "Єтот текст нужно сделать микропиздрическим", null));
        textArray.add(new Pair(314, ".", "Соня: Я знаю", null));
        textArray.add(new Pair(315, ".", "*Мне не послышалось?*", null));
        textArray.add(new Pair(316, ".", "Соня: Ты мне нравишься....", null));
        textArray.add(new Pair(317, ".", "Ваня: Понятно....", null));
        textArray.add(new Pair(318, ".", "Соня: ...", null));
        textArray.add(new Pair(319, ".", "Соня: Давай будем гулять с тобой часто?", null));
        textArray.add(new Pair(320, ".", "*Хм... А где подвох? Там же Мишка ещё говорила что нужно быть осторожным*", null));
        textArray.add(new Pair(321, ".", "[Встречаться?]", null));
        textArray.add(new Pair(322, ".", "Нет", null));
        textArray.add(new Pair(323, ".", "Ваня: Нет, спасибо", null));
        textArray.add(new Pair(324, ".", "Соня: П-почему?", null));
        textArray.add(new Pair(325, ".", "Да", null));
        textArray.add(new Pair(326, ".", "Ваня: Не нуждаюсь", null));
        textArray.add(new Pair(327, ".", "*Та всё равно, это моя жизнь, а я ещё буду каким-то снам доверять свою жизнь*", null));
        textArray.add(new Pair(328, ".", "*Мишка, я просрал первый и последний шанс!!!*", null));
        textArray.add(new Pair(329, ".", "Соня: Л-ладно", null));
        textArray.add(new Pair(330, ".", "Ваня: Давай встрчаться?", null));
        textArray.add(new Pair(331, ".", "*Соня ехидно улыбнулась*", null));
        textArray.add(new Pair(332, ".", "Соня: Д-да, давай...", null));
        textArray.add(new Pair(333, ".", "*Мда...*", null));
        textArray.add(new Pair(334, ".", "*А что, так ожно было!?*", null));
        textArray.add(new Pair(335, ".", "Соня: НУ что же... Ты точно не хочешь?", null));
        textArray.add(new Pair(336, ".ʼВаня", "Ваня: Давай погуляем?", null));
        textArray.add(new Pair(337, ".ʼВаня", "Ваня: Точно не хочу", null));
        textArray.add(new Pair(338, ".", "*Соня покраснела*", null));
        textArray.add(new Pair(339, "Соня", "Соня: Точно?", null));
        textArray.add(new Pair(340, ".", "*Я взёл её за руку*", null));
        textArray.add(new Pair(341, "Ваня", "Ваня: Точно", null));
        textArray.add(new Pair(342, ".", "*Мы гуляли по лесу*", null));
        textArray.add(new Pair(343, ".", "Соня: Точно точно?", null));
        textArray.add(new Pair(344, ".", "Соня: Давай пойдём к озеру там где ты сидишь часто?", null));
        textArray.add(new Pair(345, ".", "Ваня: ДА!", null));
        textArray.add(new Pair(346, ".", "Ваня: Откуда ты знаешь?", null));
        textArray.add(new Pair(347, ".", "Соня: То...", null));
        textArray.add(new Pair(348, ".", "Соня: Я же твоя девушка, мне же нужно знать всё про тебя, хе-хе", null));
        textArray.add(new Pair(349, ".", "Ваня: ДА!!!!!", null));
        textArray.add(new Pair(350, ".", "*Звучит разумно, но странно*", null));
        textArray.add(new Pair(351, ".", "*Соня ещё больше улыбнулась*", null));
        textArray.add(new Pair(352, ".", "*Мы пришли к пляжу*", null));
        textArray.add(new Pair(353, ".", "Соня: Ну что же...", null));
        textArray.add(new Pair(354, ".", "Соня: Ладно... Скоро увидися", null));
        textArray.add(new Pair(355, ".", "*Соня ушла*", null));
        textArray.add(new Pair(356, ".", "Ваня: Что это за нахрен было?", null));
        textArray.add(new Pair(357, ".", "Ваня...", null));
        textArray.add(new Pair(358, ".", "Ваня: ЧТо ж, пойду домой через лес. Пятница же, так что спешить некуда", null));
        textArray.add(new Pair(359, ".", "*Я встал и пошёл*", null));
        textArray.add(new Pair(360, ".", "...", null));
        textArray.add(new Pair(361, ".", "*Я пошёл через лес*", null));
        textArray.add(new Pair(362, ".", "Ваня: Зайду-ка я на пляж возле озера", null));
        textArray.add(new Pair(363, ".", "Задержка 5 секунд + картинка пляжа", null));
        textArray.add(new Pair(364, ".", "Ваня: Как здесь красиво", null));
        textArray.add(new Pair(365, ".", "Звуки шуршания", null));
        textArray.add(new Pair(366, ".", "Ваня: Кто здесь?", null));
        textArray.add(new Pair(367, ".", "???:", null));
        textArray.add(new Pair(368, ".", "Ваня: выходи, я не буду бить", null));
        textArray.add(new Pair(369, ".", "*Из кустов вышла Соня*", null));
        textArray.add(new Pair(370, ".", "Ваня: ...", null));
        textArray.add(new Pair(371, ".", "Соня: ...", null));
        textArray.add(new Pair(372, ".", "Ваня: Ну, что ты делаешь тут", null));
        textArray.add(new Pair(373, ".", "Соня: я тут часто гуляю", null));
        textArray.add(new Pair(374, ".", "Ваня: Я думал тут никто не может быть так как это место очень далеко от месности", null));
        textArray.add(new Pair(375, ".", "Соня: Это правда", null));
        textArray.add(new Pair(376, ".", "*В руке Сони выглядывает мокрая тряпка и нож в другой руке*", null));
        textArray.add(new Pair(377, ".", "Ваня: Зачем тебе нож и тряпка, ещё и мокрая", null));
        textArray.add(new Pair(378, ".", "*Соня начала приближаться*", null));
        textArray.add(new Pair(379, ".", "Соня: Сейчас покажу", null));
        textArray.add(new Pair(380, ".", "[Бежать?]", null));
        textArray.add(new Pair(381, ".", "Да", null));
        textArray.add(new Pair(382, ".", "*Я резко бобежал домой*", null));
        textArray.add(new Pair(383, ".", "Нет", null));
        textArray.add(new Pair(384, ".", "*Надо сваливать от сюда*", null));
        textArray.add(new Pair(385, ".", "Ваня: Х-хорошо)", null));
        textArray.add(new Pair(386, ".", "*Эта странная девочка погналась за мной и странно смеясь, как будто для неё это игра, где приз это моя жизнь.*", null));
        textArray.add(new Pair(387, ".", "Соня: Правильно, хе-хе", null));
        textArray.add(new Pair(388, ".", "*я бежу по лесной тропе*", null));
        textArray.add(new Pair(389, ".", "*Что-то мне не посебе...*", null));
        textArray.add(new Pair(390, ".", "/ЧТО ЖЕ ЭТО ВСЁ ЗА ЧЕРТОВШИНА/", null));
        textArray.add(new Pair(391, ".", "/КАК БУД-ТО БЕГУ УЖЕ ВЕЧНОСТЬ/", null));
        textArray.add(new Pair(392, ".", "*Наверное она меня догонит, так что не стану испытывать судьбу", null));
        textArray.add(new Pair(393, ".", "Нет, не убегать", null));
        textArray.add(new Pair(394, ".", "[Точно не следует убегать?]", null));
        textArray.add(new Pair(395, ".", "*Перепрыгиваю ров*", null));
        textArray.add(new Pair(396, ".", "Убежать", null));
        textArray.add(new Pair(397, ".", "*Соня набросилась на меня с улыбкой, закрыв мне глаза и нос*", null));
        textArray.add(new Pair(398, ".", "*я спотыкаюсь о камень*", null));
        textArray.add(new Pair(399, ".", "#Мерзские звуки похожие на смех#", null));
        textArray.add(new Pair(400, ".", "*Я рвонул от неё, а она почти набросилась на меня*", null));
        textArray.add(new Pair(401, ".", "Соня: Тихо, тихо, не бойся, всё будет хорошо, хе-хе", null));
        textArray.add(new Pair(402, ".", "[Выбор: подобрать камень и кинуть в неё]", null));
        textArray.add(new Pair(403, ".", "[Да]", null));
        textArray.add(new Pair(404, ".", "Соня: Эй.... Ты куда???", null));
        textArray.add(new Pair(405, ".", "[Нет]", null));
        textArray.add(new Pair(406, ".", "Иван: ИДИ К ЧЁРТУ", null));
        textArray.add(new Pair(407, ".", "*Я отключился*", null));
        textArray.add(new Pair(408, ".", "*Я вскакиваю и начинаю бежать дальше*", null));
        textArray.add(new Pair(409, ".", "*Я замахиваюсь и спеша кидю камень в тёмный силуэт*", null));
        textArray.add(new Pair(410, ".", "*Я ничего не отвечая бежал*", null));
        textArray.add(new Pair(411, ".", "???: Бу...", null));
        textArray.add(new Pair(412, ".", "*Пробегаю каменный склон*", null));
        textArray.add(new Pair(413, ".", "#КРИК#", null));
        textArray.add(new Pair(414, ".", "Ваня: Мишка, не страшно", null));
        textArray.add(new Pair(415, ".", "/ещё чуть чуть и я выйду на свет/", null));
        textArray.add(new Pair(416, ".", "*Адреналин в крови Ивана поднялся настолько, что он не понял как оказался у двери дома*", null));
        textArray.add(new Pair(417, ".", "*Иван выбегает на дорогу*", null));
        textArray.add(new Pair(418, ".", "Мишка: Ладно, ладно", null));
        textArray.add(new Pair(419, ".", "*Распахивает дверь дома*", null));
        textArray.add(new Pair(420, ".", "*Я Мчитсь к дому не замечая прохожих*", null));
        textArray.add(new Pair(421, ".", "Мишка: Я вижу ты в полном дерьме...", null));
        textArray.add(new Pair(422, ".", "Безумные глаза, полные страха смотрят на семью которая накрывают на стол. Семья готовится к празднику.", null));
        textArray.add(new Pair(423, ".", "*Открываю дверь*", null));
        textArray.add(new Pair(424, ".", "Ваня: Ага..,", null));
        textArray.add(new Pair(425, ".", "*Мои глаза полные страха смотрят на семью которая накрывают на стол. Семья готовится к празднику.*", null));
        textArray.add(new Pair(426, ".", "Катя: ЧТО ТО СЛУЧИЛОСЬ БРАТИК ?", null));
        textArray.add(new Pair(427, ".", "Мишка: Ну прости, я не знала что так будет", null));
        textArray.add(new Pair(428, ".", "Катя: ЧТО ТО СЛУЧИЛОСЬ БРАТИК ?", null));
        textArray.add(new Pair(429, ".", "*Нежилая испугать родных, ябыстро попытался собраться и ответил.*", null));
        textArray.add(new Pair(430, ".", "*Нежилая испугать родных, я быстро попытался собраться и ответил.*", null));
        textArray.add(new Pair(431, ".", "Иван: Да так.. ничего.. просто не хотел опоздать к столу", null));
        textArray.add(new Pair(432, ".", "Ваня: Хорошо, но если ответишь на вопросы", null));
        textArray.add(new Pair(433, ".", "Иван: Да так.. ничего.. просто собаку испугался", null));
        textArray.add(new Pair(434, ".", "#СТУК В ДВЕРЬ#", null));
        textArray.add(new Pair(435, ".", "Мишка: А что если не хочу?", null));
        textArray.add(new Pair(436, ".", "#СТУК В ДВЕРЬ#", null));
        textArray.add(new Pair(437, ".", "*меня покрыл пот, ноги заледенели и затряслись, глаз задёргался*", null));
        textArray.add(new Pair(438, ".", "*Меня покрыл пот, ноги заледенели и затряслись, в глазах пролетел ужас.*", null));
        textArray.add(new Pair(439, ".", "Ваня: Буду бить тебя", null));
        textArray.add(new Pair(440, ".", "*Сестра пошла открывать дверь, я попытался что то возразить, но не нашёл нужных слов.*", null));
        textArray.add(new Pair(441, ".", "*Сестра пошла открывать дверь, я попытался что то возразить, но потом страх меня сковал.*", null));
        textArray.add(new Pair(442, ".", "Мишка: Бидося, страшно, ха-ха. Я твоим сном управляю, ты можешь не проснуться!", null));
        textArray.add(new Pair(443, ".", "*Дом сразу наполнился шумом. Это оказались гости пришившие на праздник.*", null));
        textArray.add(new Pair(444, ".", "*Дом сразу наполнился шумом. Это оказались гости пришившие на праздник.*", null));
        textArray.add(new Pair(445, ".", "Ваня: Мне нужны ответы", null));
        textArray.add(new Pair(446, ".", "*Увидев что это были всего лишь гости, я успокоился и пошёл тоже их встречать.*", null));
        textArray.add(new Pair(447, ".", "*Я увидел что это были всего лишь гости, Я успокоился и пошёл тоже их встречать.*", null));
        textArray.add(new Pair(448, ".", "Мишка: Ну, ладно, давай послушаем, что тебя интересует", null));
        textArray.add(new Pair(449, ".", "Гость: ИВАН! ТЫ НАС РАЗВЕ НЕ ЗАМЕТИЛ? ТЫ ПРОБИГАЛ ВОЗЛЕ НАС.. МИНУТЫ 3 НАЗАД.", null));
        textArray.add(new Pair(450, ".", "Гость: ИВАН! ТЫ НАС РАЗВЕ НЕ ЗАМЕТИЛ? ТЫ ПРОБИГАЛ ВОЗЛЕ НАС.. МИНУТ 5 НАЗАД.", null));
        textArray.add(new Pair(451, ".", "Иван: ДА НЕ ПРИЗНАЛ.. СПЕШИЛ ДОМОЙ Ответил Иван опустив взгляд на пыльный порог.", null));
        textArray.add(new Pair(452, ".", "Ваня: В чём смысл жизни?", null));
        textArray.add(new Pair(453, ".", "Иван: ДА НЕ ПРИЗНАЛ.. СПЕШИЛ ДОМОЙ Ответил Иван опустив взгляд на порог и увидел пару капель крови.", null));
        textArray.add(new Pair(454, ".", "Мишка: У тебя серйозно вопросы есть...", null));
        textArray.add(new Pair(455, ".", "*Я решил просто полежать*", null));
        textArray.add(new Pair(456, ".", "*Я вернулся в свою комнату и разделся*", null));
        textArray.add(new Pair(457, ".", "Ваня: Почему бублики готовят с дырками?", null));
        textArray.add(new Pair(458, ".", "*Из не одкуда появилась Соня*", null));
        textArray.add(new Pair(459, ".", "Ваня: Нужно теперь помыться", null));
        textArray.add(new Pair(460, ".", "Ваня: ....", null));
        textArray.add(new Pair(461, ".", "Ваня: Что за нафиг???", null));
        textArray.add(new Pair(462, ".", "*Я захожу в душевую и тщательно отмываюсь от грязи и крови*", null));
        textArray.add(new Pair(463, ".", "Ваня: Чем обычный человек отличается от нормального?", null));
        textArray.add(new Pair(464, ".", "*Вибрация* *её ебальник на весь экран*", null));
        textArray.add(new Pair(465, ".", "???: привет", null));
        textArray.add(new Pair(466, ".", "Мишка: РОТ ЗАКРОЙ", null));
        textArray.add(new Pair(467, ".", "*Я подскользнулся и упал*", null));
        textArray.add(new Pair(468, ".", "Ваня: ...", null));
        textArray.add(new Pair(469, ".", "Ваня: К-как?", null));
        textArray.add(new Pair(470, ".", "Мишка: Давай нормальные вопросы", null));
        textArray.add(new Pair(471, ".", "Соня: Хе-хе", null));
        textArray.add(new Pair(472, ".", "Ваня: 2+2=5?", null));
        textArray.add(new Pair(473, ".", "Ваня: ...", null));
        textArray.add(new Pair(474, ".", "Мишка: Ты не нормальный... Пока", null));
        textArray.add(new Pair(475, ".", "Соня: Ты только мой!", null));
        textArray.add(new Pair(476, ".", "Ваня: Ладно, ладно... У мамы спрошу, а если на счёт таких вопросов, то кто ты?", null));
        textArray.add(new Pair(477, ".", "Ваня: нет, я говорил что не буду, вот и всё", null));
        textArray.add(new Pair(478, ".", "Соня: Сейчас я у тебя не спрашиваю", null));
        textArray.add(new Pair(479, ".", "Мишка: Я твоё воображение и всё", null));
        textArray.add(new Pair(480, ".", "*БАЦ*", null));
        textArray.add(new Pair(481, ".", "Ваня: Ага, а я мать разраба", null));
        textArray.add(new Pair(482, ".", "*Соня меня стукнула по голове*", null));
        textArray.add(new Pair(483, ".", "Мишка: Я серйозно", null));
        textArray.add(new Pair(484, ".", "*Я очнулся в лесу*", null));
        textArray.add(new Pair(485, ".", "Ваня: Ну ладно, а кто такая Соня", null));
        textArray.add(new Pair(486, ".", "*Внезапно появилась Мишка*", null));
        textArray.add(new Pair(487, ".", "Мишка: тоже как и я - воображение твоё.", null));
        textArray.add(new Pair(488, ".", "Мишка: О, а ты молодец. Долго пробыл", null));
        textArray.add(new Pair(489, ".", "Ваня: Как так?", null));
        textArray.add(new Pair(490, ".", "Ваня: Ч-что?", null));
        textArray.add(new Pair(491, ".", "Мишка: Откуда мне знать, я знаю всё что ты знаешь и думаешь!", null));
        textArray.add(new Pair(492, ".", "Мишка: Ты что, даже сейчас не понял?", null));
        textArray.add(new Pair(493, ".", "Ваня: Логично....", null));
        textArray.add(new Pair(494, ".", "Ваня: нет, а что?", null));
        textArray.add(new Pair(495, ".", "Мишка: Ну, тебе к доктору в лучшем случае нужно пойти!", null));
        textArray.add(new Pair(496, ".", "Мишка: А как ты думаешь, как девочка может попасть в дом, где все окна закрыты и гости в доме?", null));
        textArray.add(new Pair(497, ".", "Ваня: Да, надо...", null));
        textArray.add(new Pair(498, ".", "Ваня: ...", null));
        textArray.add(new Pair(499, ".", "Мишка: Так а что теперь будешь делать?", null));
        textArray.add(new Pair(500, ".", "Ваня: Может быть всё что угодно, если что.", null));
        textArray.add(new Pair(501, ".", "Ваня: Та нужно сейчас разобраться, как такое возможно что я сам себя вырубаю...", null));
        textArray.add(new Pair(502, ".", "Мишка: ...", null));
        textArray.add(new Pair(503, ".", "Мишка: ха-ха. Ладно, иди просыпайся", null));
        textArray.add(new Pair(504, ".", "Мишка: Ладно, ты так и не понял, ты же ещё и подросток... Надеюсь ты скоро всё поймёшь", null));
        textArray.add(new Pair(505, ".", "*Я резко проснулся*", null));
        textArray.add(new Pair(506, ".", "Ваня: ...", null));
        textArray.add(new Pair(507, ".", "*я лежал на земле*", null));
        textArray.add(new Pair(508, ".", "Мишка: Увидимся, я надеюсь...", null));
        textArray.add(new Pair(509, ".", "*Я встал, обтрусился и пошёл домой*", null));
        textArray.add(new Pair(510, ".", "*Я резко проснулся*", null));
        textArray.add(new Pair(511, ".", "*По дороге домой, я думал как мне сказать правильно маме*", null));
        textArray.add(new Pair(512, ".", "Ваня: Стоп, а где я?", null));
        textArray.add(new Pair(513, ".", "*Я зашёл домой и пошёл в комнату полежать...*", null));
        textArray.add(new Pair(514, ".", "*Я привязан к стульчику*", null));
        textArray.add(new Pair(515, ".", "*Поспать так и не получилось*", null));
        textArray.add(new Pair(516, ".", "Ваня: Ч-что?", null));
        textArray.add(new Pair(517, ".", "Ваня: Где я?", null));
        textArray.add(new Pair(518, ".", "Ваня: П-почему я привязан?", null));
        textArray.add(new Pair(519, ".", "Ваня: К-как?", null));
        textArray.add(new Pair(520, ".", "*Я огляделся*", null));
        textArray.add(new Pair(521, ".", "*...*", null));
        textArray.add(new Pair(522, ".", "*Никого нет*", null));
        textArray.add(new Pair(523, ".", "Ваня: Что ж... Вибора нет...", null));
        textArray.add(new Pair(524, ".", "[Закричать?]", null));
        textArray.add(new Pair(525, ".", "Да", null));
        textArray.add(new Pair(526, ".", "Ваня: СПАСИТЕЕЕЕЕЕЕ", null));
        textArray.add(new Pair(527, ".", "Нет", null));
        textArray.add(new Pair(528, ".", "*Я кричал секунд 20 и внезапно забежали гости и моя семья и я оказался в своей комнате*", null));
        textArray.add(new Pair(529, ".", "???: Приветик! Ты уже очнулся уже? Как это здорово!", null));
        textArray.add(new Pair(530, ".", "Все в один голос: что случилось?", null));
        textArray.add(new Pair(531, ".", "*Это Соня...*", null));
        textArray.add(new Pair(532, ".", "*Сзади выглядывает Соня*", null));
        textArray.add(new Pair(533, ".", "*У неё в руке был нож... Видимо это конец...*", null));
        textArray.add(new Pair(534, ".", "Ваня: СЗАДИ!!!!", null));
        textArray.add(new Pair(535, ".", "Соня: Давай прогуляемся?", null));
        textArray.add(new Pair(536, ".", "*ВСЕ ПОСМОТРЕЛИ НАЗАД*", null));
        textArray.add(new Pair(537, ".", "*Будет ли это моим спасением?*", null));
        textArray.add(new Pair(538, ".", "ВСЕ: Что? Тут никого нет...", null));
        textArray.add(new Pair(539, ".", "Ваня: Давай?", null));
        textArray.add(new Pair(540, ".", "*Что же за чёртовщина тут твориться?*", null));
        textArray.add(new Pair(541, ".", "Соня: Ура, ура, ура", null));
        textArray.add(new Pair(542, ".", "Ваня: Наверное показалось....", null));
        textArray.add(new Pair(543, ".", "*Соня начала развязывать Ваню, но она до сих пор держит нож*", null));
        textArray.add(new Pair(544, ".", "Гости: ты так не шути", null));
        textArray.add(new Pair(545, ".", "Ваня: Спасибо...", null));
        textArray.add(new Pair(546, ".", "Ваня: я наверное посплю", null));
        textArray.add(new Pair(547, ".", "*Соня взяла меня за руки и сильно зжала*", null));
        textArray.add(new Pair(548, ".", "*Все ушли, а я иду спать*", null));
        textArray.add(new Pair(549, ".", "*Думаю лучше ничего не говорить*", null));
        textArray.add(new Pair(550, ".", "*Я уснул...*", null));
        textArray.add(new Pair(551, ".", "*Мы вышли со здания заброшеного и пошли в лес*", null));
        textArray.add(new Pair(552, ".", "Мишка: как дела?", null));
        textArray.add(new Pair(553, ".", "*Мы шли долго*", null));
        textArray.add(new Pair(554, ".", "Задержка на 5 секунд", null));
        textArray.add(new Pair(555, ".", "Ваня: ...", null));
        textArray.add(new Pair(556, ".", "Соня: Постой смирно и закрой глаза", null));
        textArray.add(new Pair(557, ".", "Мишка: как я говорила.... Думай...", null));
        textArray.add(new Pair(558, ".", "*Лучше не спорить*", null));
        textArray.add(new Pair(559, ".", "Ваня:...", null));
        textArray.add(new Pair(560, ".", "Ваня: Х-хорошо", null));
        textArray.add(new Pair(561, ".", "Мишка: Что?", null));
        textArray.add(new Pair(562, ".", "*Соня мило улібнулась и обошла меня*", null));
        textArray.add(new Pair(563, ".", "Ваня: То есть всё что ты говорила на счёт вымешленых - это правда?", null));
        textArray.add(new Pair(564, ".", "*Она завязала мне глаза*", null));
        textArray.add(new Pair(565, ".", "Мишка: Наконец-то допёрло.", null));
        textArray.add(new Pair(566, ".", "*...*", null));
        textArray.add(new Pair(567, ".", "Ваня: Выходит что у меня проблемы с головой?", null));
        textArray.add(new Pair(568, ".", "*И очень туго!*", null));
        textArray.add(new Pair(569, ".", "Мишка: Та не совсем. Я с тобой вот общаюсь и нормально. Ну ты понял", null));
        textArray.add(new Pair(570, ".", "*Соня молча взяла меня за руку и повела*", null));
        textArray.add(new Pair(571, ".", "Ваня: То есть мне нужно сходить к доктору?", null));
        textArray.add(new Pair(572, ".", "*Так мы шли 10 минут. Я каким-то чудом не споткнулся. Ещё в лесу!*", null));
        textArray.add(new Pair(573, ".", "Мишка: Как знаешь....", null));
        textArray.add(new Pair(574, ".", "Соня: Пришли...", null));
        textArray.add(new Pair(575, ".", "[Сходить в больницу?]", null));
        textArray.add(new Pair(576, ".", "Да", null));
        textArray.add(new Pair(577, ".", "Ваня: Думаю, что да", null));
        textArray.add(new Pair(578, ".", "Мишка: Не могу не возразить и не поддержать. Мне всё равно", null));
        textArray.add(new Pair(579, ".", "*Соня развязала мне глаза*", null));
        textArray.add(new Pair(580, ".", "нет", null));
        textArray.add(new Pair(581, ".", "Ваня: Ну и отлично", null));
        textArray.add(new Pair(582, ".", "*Мы оказались на том пляже*", null));
        textArray.add(new Pair(583, ".", "Ваня: Ну, я не думаю, что это нужно", null));
        textArray.add(new Pair(584, ".", "*Я резко проснулся*", null));
        textArray.add(new Pair(585, ".", "Соня: Покупаемся?", null));
        textArray.add(new Pair(586, ".", "*Мишка немного улыбнулась*", null));
        textArray.add(new Pair(587, ".", "Ваня: Так-с, теперь нужно собираться", null));
        textArray.add(new Pair(588, ".", "[Покупаться?]", null));
        textArray.add(new Pair(589, ".", "нет", null));
        textArray.add(new Pair(590, ".", "Мишка: Ну, мне приятно будет", null));
        textArray.add(new Pair(591, ".", "*Нужно теперь с этим разобраться и поговорить с мамой*", null));
        textArray.add(new Pair(592, ".", "Да", null));
        textArray.add(new Pair(593, ".", "Ваня: Давай я просто посижу тут, а ты покупаешься?", null));
        textArray.add(new Pair(594, ".", "Ваня: Ты всё равно моя фантазия", null));
        textArray.add(new Pair(595, ".", "*Я встал и пошёл в кухню, что бы рассказать это маме*", null));
        textArray.add(new Pair(596, ".", "Ваня: давай", null));
        textArray.add(new Pair(597, ".", "Соня: Ну хорошо, только ты никуда не иди", null));
        textArray.add(new Pair(598, ".", "Мишка: Ну и?", null));
        textArray.add(new Pair(599, ".", "*Мама стоит в кухне*", null));
        textArray.add(new Pair(600, ".", "Соня: Ура, ура!", null));
        textArray.add(new Pair(601, ".", "Ваня Хорошо", null));
        textArray.add(new Pair(602, ".", "Ваня: Ладно, тогда будем видеться каждую ночь?", null));
        textArray.add(new Pair(603, ".", "Ваня: Мам, можно мы сходим к доктору?", null));
        textArray.add(new Pair(604, ".", "*Я залез в воду не раздеваясь*", null));
        textArray.add(new Pair(605, ".", "*Соня пошла купаться, а я  рвонул и закричал*", null));
        textArray.add(new Pair(606, ".", "Мишка: Конечно", null));
        textArray.add(new Pair(607, ".", "Мама: Это из-за вчерашней ситуации?", null));
        textArray.add(new Pair(608, ".", "*Я и Соня поплывли до средины озера*", null));
        textArray.add(new Pair(609, ".", "Ваня: Тогда, до встречи", null));
        textArray.add(new Pair(610, ".", "*Не думаю что ей нужно это говорить, а лучше сразу к доктору сказать это всё*", null));
        textArray.add(new Pair(611, ".", "*Тут очень глубоко*", null));
        textArray.add(new Pair(612, ".", "Мишка: Агась", null));
        textArray.add(new Pair(613, ".", "Мама: Ладно, одевайся", null));
        textArray.add(new Pair(614, ".", "Соня: правда здесь прекрасно?", null));
        textArray.add(new Pair(615, ".", "*Я проснулся и начал заниматься привычными делами.*", null));
        textArray.add(new Pair(616, ".", "Ваня: Большое спасибо", null));
        textArray.add(new Pair(617, ".", "Ваня: Очень", null));
        textArray.add(new Pair(618, ".", "*Это происхоило днем за днём*", null));
        textArray.add(new Pair(619, ".", "*Мы с мамой гуляли по лесу*", null));
        textArray.add(new Pair(620, ".", "*Соня замолчала*", null));
        textArray.add(new Pair(621, ".", "*Я всё большое проводил время во сне лиш бы не видеть Соню, так как Мишка меня личила*", null));
        textArray.add(new Pair(622, ".", "*Зайдя в больницу, я взял талончик*", null));
        textArray.add(new Pair(623, ".", "*Я развернулся и Соня напрыгнула на меня*", null));
        textArray.add(new Pair(624, ".", "*Меня вызвали к доктору-психиатру*", null));
        textArray.add(new Pair(625, ".", "*И одного дня я увидел, как я смтрю на самого себя, но моё тело лежит на кровати, а я стою перед ним*", null));
        textArray.add(new Pair(626, ".", "*Она начала меня топить...*", null));
        textArray.add(new Pair(627, ".", "*Я рассказал ему все свои проблемы*", null));
        textArray.add(new Pair(628, ".", "*Я в пал кому...*", null));
        textArray.add(new Pair(629, ".", "*Я начал кричать*", null));
        textArray.add(new Pair(630, ".", "*Он внимательно выслушал меня*", null));
        textArray.add(new Pair(631, ".", "*Хватка Сони была очень сильна и я не смог даже вынырнуть*", null));
        textArray.add(new Pair(632, ".", "Сделать чёрный экран", null));
        textArray.add(new Pair(633, ".", "*После этого, он выпроводил меня и пригласил мою маму*", null));
        textArray.add(new Pair(634, ".", "*Я задыхался и ничего не мог сделать*", null));
        textArray.add(new Pair(635, ".", "*Мама была с доктором дольше за меня*", null));
        textArray.add(new Pair(636, ".", "*Она смеялась в это время...*", null));
        textArray.add(new Pair(637, ".", "*Выйдя с кабинета, мама рыдала*", null));
        textArray.add(new Pair(638, ".", "*Я брысгался, но это не помогло...*", null));
        textArray.add(new Pair(639, ".", "*Меня ударили и я отключился*", null));
        textArray.add(new Pair(640, ".", "*Меня поместили в камеру и я лечился более 10 лет*", null));
        textArray.add(new Pair(641, ".", "*В этот период меня пичкали таблетками*", null));
        textArray.add(new Pair(642, ".", "*Из-за этого я не видел ни Мишку, ни Соню*", null));
        textArray.add(new Pair(643, ".", "*Я выпустился из больницы*", null));
        textArray.add(new Pair(644, ".", "*Вернулся домой и начал работать*", null));
        textArray.add(new Pair(645, ".", "*Мы были вновь вместе втроём как ни в чём не бывало*", null));
        textArray.add(new Pair(646, ".", "*И от того Вани, который был раньше - уже ничего не осталось*", null));

    }
    protected void finalize(){
        //
    }


        // Додати інші пари за необхідності


}
