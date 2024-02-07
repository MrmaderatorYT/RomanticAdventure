let textArray = [
    "[Я прокидаюсь від звука противного будильника]",
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
    "Вань, купи мені цукерки Корівка", //TODO  блок імені Катя і також додати лапки для цукерок (взяти з вьорду) і тепер замість "???" писати в репліку Івана його ім'я
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
    "Через 15 хвилин я захожу в невеличкий магазин Універсал",//TODO в блок імені лапки додати з Вьорду

    //TODO підгілка "Поганий братик"
    "[Катя на мене подивилася собачими очима]",
    "[Вона опустила голову, встала і побігла у свою кімнату не доївши]",
    "[Я тільки фиркнув у її сторону і почав збиратися до магазину]",
    "[Я вийшов з дому і пішов через ліс, бо так швидше]",
    "[Через 13 хвилин я заходжу в магазин Уніка 12/7]",//TODO лапки....


    //TODO гілка Магазін Уківерсал (не глава)
    //TODO підгілка гарний братик в магазині (тут взагалі буде й поганий братик в магазині) Треба через переевірку sharedPreferences перевірити які бали щоб вивести текст
    "[Я зайшов у магазин та взяв візок]",
    "[Я купив усі потрібні речі та підійшов до стійки з цукерками]",
    "[Я підійшов до коробки з цукерками Корівка]", //TODO Лапки з вьорду
    "[Взяв 100 - 120 грам]"
];

let textIndex = 0;
let textElement = document.getElementById("text");
let buttonElement = document.getElementById("buttonFirst");
let buttonSecondElement = document.getElementById("buttonSecond");
let nameElement = document.getElementById("name"); // Отримуємо елемент прямокутника

let delayBetweenCharacters = 40; //затримка між спавном символів
let delayBetweenTexts = 2000; // затримка між спавнінгом іншого тексту з масиву



function animateText() {
    textElement.innerHTML = "";
    let newText = textArray[textIndex];
     if (newText === "росії немає" || newText === "абра") {
            nameElement.innerHTML = "Степан";
        } else {
            nameElement.innerHTML = "???"; // Очищаємо текст, якщо умова не виконується
        }

    function animateFrame(i) {
        setTimeout(() => {
            textElement.innerHTML += newText[i];

            if (i < newText.length - 1) {
                animateFrame(i + 1);
            } else {
                setTimeout(() => {
                    textIndex += 1

                    if (textIndex === 4 || textIndex===17 || textIndex===29) {
                        buttonElement.style.display = "block";
                        buttonSecondElement.style.display = "block";
                        animationInProgress = false;
                        return;
                    } else {
                        buttonElement.style.display = "none";
                        buttonSecondElement.style.display = "none";
                    }

                    buttonElement.addEventListener("click", firstBtn);
                    buttonSecondElement.addEventListener("click", secondBtn);
                    animationInProgress = true;
                    animateText();
                }, delayBetweenTexts);
            }
        }, delayBetweenCharacters);
    }

    animateFrame(0);
}

function firstBtn() {
    if(textIndex===3){
        return;
}
    else if(textIndex===17){
        textElement.innerHTML = "А що ж мені купити? Список дасиш, як минулого разу";
        nameElement.innerHTML = "???"
        textIndex = 18;
    }
    else if (textIndex===29){
        return;
    }

    buttonElement.style.display = "none";
    buttonSecondElement.style.display = "none";
     setTimeout(() => {
            animateText();
        }, delayBetweenTexts);
}

function secondBtn() {
    if(textIndex===4){
        textElement.innerHTML = "Ні, так не піде";
        nameElement.innerHTML = "Протагоніст"
        textIndex = 3;
        }
        else if(textIndex===17){
        textElement.innerHTML = "Ні, так не піде";
        nameElement.innerHTML = "Протагоніст"
        textIndex = 18;
        }
        else if(textIndex==29){
            textIndex=40;
        }

        buttonElement.style.display = "none";
        buttonSecondElement.style.display = "none";
        // Викликаємо animateText() після завершення попередньої анімації
            setTimeout(() => {
                animateText();
            }, delayBetweenTexts);

}

document.addEventListener("DOMContentLoaded", function () {
    animateText();
//FIXME что бы не накладывалось мисив на масив
    // Додаємо обробник події для контейнера textContainer
    document.getElementById("textContainer").addEventListener("click", function () {
        textElement.innerHTML = "";
        // Перевіряємо, чи не досягнуто кінця масиву textArray

        if (textIndex < textArray.length - 1) {
            textIndex++; // Збільшуємо індекс
            textElement.innerHTML = textArray[textIndex]; // Відображаємо новий текст
        }
    });
});
