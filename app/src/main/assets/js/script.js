let textArray = [
    "Реклама на русском радіо",
    "Хіт ФМ",
    "Абра радіо"
    // Текст
];

let textIndex = 0;
let textElement = document.getElementById("text");

function animateText() {
    // Очищуємо текст, щоб не було з'єднання з іншим масивом
    textElement.innerHTML = "";

    // отримуємо новий індекс
    let newText = textArray[textIndex];

    let i = 0;
    // встановлюємо інтервал
    let intervalId = setInterval(function () {
        // додаємо новий індекс до масиву
        textElement.innerHTML += newText[i];
        i++;

        // Якщо досягли кінця - очищуємо інтервал
        if (i === newText.length) {
            clearInterval(intervalId);

            // Після затримки в 1000 мілісекондс - викликаємо новий тєкст
            setTimeout(function () {
                // оновлюємо індекс масиву з текстом
                textIndex = (textIndex + 1) % textArray.length;
                // викликаємо фунцію для анімації тексту
                animateText();
            }, 1000);
        }
    }, 50); // Інтервал між появою тексту (символами)
}

document.addEventListener("DOMContentLoaded", function () {
//стартуємо фунцію
    animateText();
});
