async function fetchUserInfo() {
  try {
    // Show loading message or spinner
    document.getElementById('loadingMessage').innerText = 'Loading...';

    const response = await fetch('/api/v1/users/username');
    const data = await response.json();
    const username = data.username;
    const role = data.role;
    localStorage.setItem('connectedUser', username);
    localStorage.setItem('roleUser', role);

    // Hide loading message or spinner
    document.getElementById('loadingMessage').innerText = '';
  } catch (error) {
    console.error('Error fetching user info:', error);
    // Handle error, e.g., show an error message
    document.getElementById('loadingMessage').innerText = 'Error fetching user info';
  }


function displayHomePage(){
    loadAndDisplayUsers();
}

function loadAndDisplayUsers(){
    const userListElement = document.getElementById("userList");
    userListElement.innerHTML = "Loading...";

}

window.addEventListener("load", displayHomePage);

function handleNewMeeting(){
    const connectedUser = localStorage.getItem("connectedUser");
    window.open(`videocall.html?username=${connectedUser}`,"_blank");
}

const newMeetingBtn = document.getElementById("newMeetingBtn");

if (localStorage.getItem('roleUser') == "ROLE_STUDENT"){
    newMeetingBtn.style.display = "none";
}
if (localStorage.getItem('roleUser') == "ROLE_TEACHER"){
    document.getElementById("joinMeetingBtn").style.display = "none";
    document.getElementById("meetingName").style.display = "none";
}


document.getElementById("username-text").innerHTML = `Welcome, ${localStorage.getItem("connectedUser")}`;

newMeetingBtn.addEventListener("click", handleNewMeeting);

function handleJoinMeeting(){
    const roomId = document.getElementById("meetingName").value;
    const connectedUser = localStorage.getItem("connectedUser");

    const url = `videocall.html?roomID=${roomId}&username=${connectedUser}`;
    if (roomId === "") {
        alert("Please enter a room ID");
        return;
    }
    window.open(url,"_blank");

}

const joinMeetingBtn = document.getElementById("joinMeetingBtn");
joinMeetingBtn.addEventListener("click", handleJoinMeeting);



}

fetchUserInfo();


