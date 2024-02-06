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
    "...",
    "Тоді не куплю",//TODO в блок імені Іван
    "[Я забираю список і йду до вхіднох дверей.]",
    "Ну хоть часик, дасиш пограти?..."//TODO в блоці імені Катя

];

let textIndex = 0;
let textElement = document.getElementById("text");
let buttonElement = document.getElementById("buttonFirst");
let buttonSecondElement = document.getElementById("buttonSecond");
let nameElement = document.getElementById("name"); // Отримуємо елемент прямокутника

let delayBetweenCharacters = 100; //затримка між спавном символів
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

                    if (textIndex === 4 || textIndex===17) {
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
        // Викликаємо animateText() після завершення попередньої анімації
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
        buttonElement.style.display = "none";
        buttonSecondElement.style.display = "none";
        // Викликаємо animateText() після завершення попередньої анімації
            setTimeout(() => {
                animateText();
            }, delayBetweenTexts);

}

document.addEventListener("DOMContentLoaded", function () {
    animateText();

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
