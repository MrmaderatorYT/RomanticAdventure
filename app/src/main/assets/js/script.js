let textArray = [
    "Реклама на русском радіо",
    "Україна понад усе",
    "росії немає",
];

let textIndex = 0;
let textElement = document.getElementById("text");
let buttonElement = document.getElementById("buttonFirst");
let buttonSecondElement = document.getElementById("buttonSecond");

let delayBetweenCharacters = 100; //затримка між спавном символів
let delayBetweenTexts = 2000; // затримка між спавнінгом іншого тексту з масиву

function animateText() {
    textElement.innerHTML = "";
    let newText = textArray[textIndex];

    function animateFrame(i) {
        setTimeout(() => {
            textElement.innerHTML += newText[i];

            if (i < newText.length - 1) {
                animateFrame(i + 1);
            } else {
                setTimeout(() => {
                    textIndex = (textIndex + 1) % textArray.length;

                    if (textIndex === 2) {
                        buttonElement.style.display = "block";
                        buttonSecondElement.style.display = "block";
                        animationInProgress = false;
                        return;
                    } else {
                        buttonElement.style.display = "none";
                        buttonSecondElement.style.display = "none";
                    }

                    buttonElement.addEventListener("click", redirectToFirstHTML);
                    buttonSecondElement.addEventListener("click", redirectToSecondHTML);
                    animationInProgress = true;
                    animateText();
                }, delayBetweenTexts);
            }
        }, delayBetweenCharacters);
    }

    animateFrame(0);
}

function redirectToFirstHTML() {
    window.location.href = "FFF";
}

function redirectToSecondHTML() {
    window.location.href = "FF";
}

document.addEventListener("DOMContentLoaded", function () {
    animateText();
});
