
function displayHomePage(){
    // check if user is connected
    const connectedUser =  localStorage.getItem("connectedUser");
     if(!connectedUser){
         window.location.href = "login.html";  // <=> window.location = "login.html";
         return;
    }
    loadAndDisplayUsers();
}

function loadAndDisplayUsers(){
    const userListElement = document.getElementById("userList");
    userListElement.innerHTML = "Loading...";
    fetch('http://localhost:8080/api/v1/users')
        .then(response => {
            return response.json();
        })
        .then(response => {
            console.log(response);
            displayUsers(response, userListElement);
        });
}

function displayUsers(userList, userListElement){
    userListElement.innerHTML = "";
    userList.forEach(user => {
        if(user.status==="online"){
            const listItem = document.createElement("li");
            listItem.innerHTML = `
                <div>
                    <i class="fa fa-user-circle"></i>
                    ${user.username} <i class="user-email">(${user.email})</i>
                </div>
                <i class="fa fa-lightbulb-o online"></i>
            `;
            userListElement.appendChild(listItem);
        }
    });

}

window.addEventListener("load", displayHomePage);



function handleLogout(){
    fetch('http://localhost:8080/api/v1/users/logout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: localStorage.getItem("connectedUser")
    })
        .then(response => {
            return response;
        })
        .then(response => {
            localStorage.removeItem("connectedUser");
            window.location.href = "login.html"; // <=> window.location = "login.html";
        })
}

const logoutBtn = document.getElementById("logoutBtn");
logoutBtn.addEventListener("click", handleLogout);



function handleNewMeeting(){
    const connectedUser = JSON.parse(localStorage.getItem("connectedUser"));
    window.open(`videocall.html?username=${connectedUser.username}`,"_blank");
}

const newMeetingBtn = document.getElementById("newMeetingBtn");
newMeetingBtn.addEventListener("click", handleNewMeeting);

function handleJoinMeeting(){
    const roomId = document.getElementById("meetingName").value;
    const connectedUser = JSON.parse(localStorage.getItem("connectedUser"));

    const url = `videocall.html?roomID=${roomId}&username=${connectedUser.username}`;
    if (roomId === "") {
        alert("Please enter a room ID");
        return;
    }
    window.open(url,"_blank");

}

const joinMeetingBtn = document.getElementById("joinMeetingBtn");
joinMeetingBtn.addEventListener("click", handleJoinMeeting);



