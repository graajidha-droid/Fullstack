let studentName = "";
let score = 0;
let timeLeft = 60;
let timerInterval;

// Question Bank
const questions = [
    { q: "Java is a?", options: ["Programming Language", "Database", "Browser"], answer: 0 },
    { q: "Which is a Database?", options: ["HTML", "MySQL", "CSS"], answer: 1 },
    { q: "Spring Boot is used for?", options: ["Web Development", "Gaming", "Animation"], answer: 0 },
    { q: "HTML stands for?", options: ["Hyper Text Markup Language", "High Tool ML", "Home Tool ML"], answer: 0 },
    { q: "CSS is used for?", options: ["Styling", "Database", "Backend"], answer: 0 },
    { q: "Which is Backend language?", options: ["Java", "HTML", "CSS"], answer: 0 },
    { q: "MySQL is?", options: ["Language", "Database", "Browser"], answer: 1 },
    { q: "JavaScript runs in?", options: ["Browser", "Database", "Printer"], answer: 0 },
    { q: "Which is framework?", options: ["Spring Boot", "Word", "Excel"], answer: 0 },
    { q: "LocalStorage is used to?", options: ["Store Data", "Delete OS", "Compile"], answer: 0 }
];

// START QUIZ
function startQuiz() {

    studentName = document.getElementById("username").value.trim();

    if (studentName === "") {
        alert("Please enter your name");
        return;
    }

    document.getElementById("page1").classList.add("hidden");
    document.getElementById("page2").classList.remove("hidden");

    loadQuestions();
    startTimer();
}

// LOAD QUESTIONS
function loadQuestions() {

    const form = document.getElementById("quizForm");
    form.innerHTML = "";

    questions.forEach((item, index) => {

        let div = document.createElement("div");
        div.className = "question";

        div.innerHTML = `<p>${index + 1}. ${item.q}</p>`;

        item.options.forEach((opt, i) => {
            div.innerHTML += `
                <label>
                    <input type="radio" name="q${index}" value="${i}">
                    ${opt}
                </label>
            `;
        });

        form.appendChild(div);
    });
}

// TIMER
function startTimer() {

    timeLeft = 60;
    document.getElementById("timer").innerText = "Time: " + timeLeft;

    timerInterval = setInterval(() => {

        timeLeft--;
        document.getElementById("timer").innerText = "Time: " + timeLeft;

        if (timeLeft <= 0) {
            clearInterval(timerInterval);
            alert("Time's up! Auto submitting...");
            submitQuiz();
        }

    }, 1000);
}

// SUBMIT QUIZ
function submitQuiz() {

    clearInterval(timerInterval);

    score = 0;

    for (let i = 0; i < questions.length; i++) {
        let selected = document.querySelector(`input[name="q${i}"]:checked`);
        if (selected && parseInt(selected.value) === questions[i].answer) {
            score++;
        }
    }

    let percentage = ((score / questions.length) * 100).toFixed(2);
    let resultStatus = percentage >= 50 ? "PASS ✅" : "FAIL ❌";

    saveToLeaderboard(studentName, score, percentage);

    document.getElementById("page2").classList.add("hidden");
    document.getElementById("page3").classList.remove("hidden");

    document.getElementById("resultText").innerText =
        `${studentName}, Score: ${score}/10 (${percentage}%) - ${resultStatus}`;

    showLeaderboard();
}

// SAVE LEADERBOARD
function saveToLeaderboard(name, score, percentage) {

    let leaderboard = JSON.parse(localStorage.getItem("leaderboard")) || [];

    leaderboard.push({ name, score, percentage });

    // Sort Highest Score First
    leaderboard.sort((a, b) => b.score - a.score);

    localStorage.setItem("leaderboard", JSON.stringify(leaderboard));
}

// SHOW LEADERBOARD
function showLeaderboard() {

    let leaderboard = JSON.parse(localStorage.getItem("leaderboard")) || [];
    let boardDiv = document.getElementById("leaderboard");

    boardDiv.innerHTML = "";

    leaderboard.forEach((item, index) => {

        boardDiv.innerHTML +=
            `<p>${index + 1}. ${item.name} - ${item.score}/10 (${item.percentage}%)</p>`;
    });
}

// DOWNLOAD PROFESSIONAL PDF CERTIFICATE
function downloadCertificate() {

    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();

    let pageWidth = doc.internal.pageSize.getWidth();
    let today = new Date().toLocaleDateString();

    doc.setFont("helvetica", "bold");
    doc.setFontSize(24);
    doc.text("QUIZ CERTIFICATE", pageWidth / 2, 30, { align: "center" });

    doc.setFontSize(16);
    doc.setFont("helvetica", "normal");
    doc.text("This is to certify that", pageWidth / 2, 50, { align: "center" });

    doc.setFontSize(20);
    doc.setFont("helvetica", "bold");
    doc.text(studentName, pageWidth / 2, 65, { align: "center" });

    doc.setFontSize(16);
    doc.setFont("helvetica", "normal");
    doc.text("has successfully completed the Quiz.", pageWidth / 2, 80, { align: "center" });

    doc.text(`Score: ${score}/10`, pageWidth / 2, 95, { align: "center" });

    doc.text(`Date: ${today}`, pageWidth / 2, 110, { align: "center" });

    doc.setFontSize(14);
    doc.text("Congratulations!", pageWidth / 2, 125, { align: "center" });

    doc.save(`Certificate_${studentName}.pdf`);
}