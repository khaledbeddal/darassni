
function handleRegistration(event){
    event.preventDefault();

    const username = document.getElementById("username").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const status = "online";

    const user = {
        username: username,
        email: email,
        password: password,
        status: status,
    };

    fetch('http://localhost:8080/api/v1/users', {
        method:'POST',
        headers:{
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(user)
    }).then(response => {
        if(!response.ok){
            throw new Error("Registration failed");
        }
        return response;
    }).then(() => {
        localStorage.setItem('connectedUser', JSON.stringify(user));
        window.location.href = "login.html";
    }).catch(error => {
        console.error("POST request error: ", error);
    });

}












const registrationForm = document.getElementById("registrationForm");
registrationForm.addEventListener("submit", handleRegistration);
