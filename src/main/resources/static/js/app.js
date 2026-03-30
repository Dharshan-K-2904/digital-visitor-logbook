// DARK MODE TOGGLE
function toggleDarkMode() {
    document.body.classList.toggle("dark");
}

// TOAST MESSAGE
function showToast(message) {
    const toast = document.getElementById("toast");
    toast.innerText = message;
    toast.style.display = "block";

    setTimeout(() => {
        toast.style.display = "none";
    }, 3000);
}