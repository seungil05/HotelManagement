var countGuest = 0;
var guestsSorted = [];
/* fetches all guests from the database and displays them in a table*/
fetch("http://localhost:8080/guests", {
    method: "GET",
    headers: {
        Authorization: "Basic " + btoa("admin:adminpassword"),

        "Content-Type": "application/json",
    },
})
    .then((response) => response.json())

    .then((data) => {
        if (data.length > 0) {
            countGuest = data.length;
            const guests = data;
            guestsSorted = guests;
            const table = document.getElementById("table-body");
            guests.forEach((guest) => {
                const row = table.insertRow(-1);
                const guestId = row.insertCell(0);
                const firstname = row.insertCell(1);
                const lastname = row.insertCell(2);
                const email = row.insertCell(3);
                const visit = row.insertCell(4);
                const room = row.insertCell(5);
                const deleteButton = row.insertCell(6);

                guestId.textContent = guest.guestId;
                firstname.textContent = guest.firstName;
                lastname.textContent = guest.lastName;
                email.textContent = guest.emailAddress;
                visit.textContent = guest.visitTime;
                room.textContent = guest.roomId;

                const button = document.createElement("button");
                button.type = "button";
                button.className = "btn btn-danger";
                button.innerHTML = '<i class="bi bi-trash"></i>';
                button.addEventListener("click", () => {
                    deleteById(guest.guestId);
                });

                deleteButton.appendChild(button);

                row.appendChild(guestId);
                row.appendChild(firstname);
                row.appendChild(lastname);
                row.appendChild(email);
                row.appendChild(visit);
                row.appendChild(room);
                row.appendChild(deleteButton);

                row.addEventListener("click", markRow);
                table.appendChild(row);
            });
            const insertedTable = document.getElementById("table-body");
            guestsSorted = Array.from(insertedTable.getElementsByTagName("tr"));
        } else {
            document.getElementById("no-guest-found").innerHTML =
                "No Guests Returned!";
            document.getElementById("no-guest-found").classList.add("text-danger");
        }
    })
    .catch((error) => {
        console.error("Error:", error);
        document.getElementById("no-guest-found").innerHTML =
            "No Guests Returned! Please check if backend is running.";
        document.getElementById("no-guest-found").classList.add("text-danger");
    });

/* fetches all rooms from the database and displays them in a table*/
var countRooms = 0;
fetch("http://localhost:8080/rooms", {
    method: "GET",
})
    .then((response) => response.json())

    .then((data) => {
        if (data.length > 0) {
            const rooms = data;
            const table = document.getElementById("table-body-room");
            countRooms = rooms.length;
            rooms.forEach((room) => {
                const row = table.insertRow(-1);
                const roomId = row.insertCell(0);
                const floor = row.insertCell(1);
                const roomNumber = row.insertCell(2);
                const capacity = row.insertCell(3);
                const price = row.insertCell(4);

                roomId.textContent = room.roomId;
                floor.textContent = room.floor;
                roomNumber.textContent = room.roomNumber;
                capacity.textContent = room.capacity;
                price.textContent = room.price;

                row.appendChild(roomId);
                row.appendChild(floor);
                row.appendChild(roomNumber);
                row.appendChild(capacity);
                row.appendChild(price);

                table.appendChild(row);
            });
        } else {
            document.getElementById("no-rooms-found").innerHTML =
                "No Rooms Returned!";
            document.getElementById("no-rooms-found").classList.add("text-danger");
        }
    })
    .catch((error) => {
        console.error("Error:", error);
        document.getElementById("no-rooms-found").innerHTML =
            "No Rooms Returned! Please check if backend is running.";
        document.getElementById("no-rooms-found").classList.add("text-danger");
    });

/* when a user clicks on a guest in the table, the guest's information is displayed in the update form
and the user will be marked in the table*/
let selectedRow = null;
function markRow() {
    if (selectedRow !== null) {
        document.getElementById("guest-id").value = "";
        selectedRow.classList.remove("table-primary");
        clearForm();
    }

    if (selectedRow === this) {
        selectedRow = null;
    } else {
        this.classList.add("table-primary");
        selectedRow = this;
        setFormValues(this);
    }
}

/* sets the values of the update form to the values of the selected guest*/
function setFormValues(row) {
    document.getElementById("guestId").value = row.cells[0].textContent;
    document.getElementById("lastname").value = row.cells[2].textContent;
    document.getElementById("firstname").value = row.cells[1].textContent;
    document.getElementById("emailaddress").value = row.cells[3].textContent;
    document.getElementById("visittime").value = row.cells[4].textContent;
    document.getElementById("roomId").value = row.cells[5].textContent;
}

/* clears the update form*/
function clearForm() {
    document.getElementById("guest-id").classList.remove("is-valid");
    document.getElementById("feedbackGuestId").classList.remove("valid-feedback");
    document.getElementById("feedbackGuestId").innerHTML = "";
    document.getElementById("guestId").value = "";
    document.getElementById("lastname").value = "";
    document.getElementById("firstname").value = "";
    document.getElementById("emailaddress").value = "";
    document.getElementById("visittime").value = "";
    document.getElementById("roomId").value = "";
}

/* fetches a guest by id and marks the guest in the table and also adds the guest to the update form*/
function getById() {
    const id = document.getElementById("guest-id").value;
    fetch("http://localhost:8080/guests/" + id, {
        method: "GET",
        headers: {
            Authorization: "Basic " + btoa("admin:adminpassword"),

            "Content-Type": "application/json",
        },
    })
        .then((response) => response.json())

        .then((data) => {
            const table = document.getElementById("table-body");
            var rows = table.getElementsByTagName("tr");
            for (var i = 0; i < rows.length; i++) {
                var row = rows[i];
                var cells = row.getElementsByTagName("td");
                if (cells[0].innerHTML === data[0].guestId) {
                    markRow.call(row);
                    break;
                }
            }
        })
        .catch((error) => {
            console.error("Error:", error);
            document.getElementById("feedbackGuestId").innerHTML =
                "No Guest With ID " + id + " Found!";
            document.getElementById("feedbackGuestId").classList.add("text-danger");
            setTimeout(function () {
                document.getElementById("feedbackGuestId").innerHTML = "";
                document
                    .getElementById("feedbackGuestId")
                    .classList.remove("text-danger");
            }, 3000);
        });
}

/* deletes a guest by id
this function gets called in the guests table trough a click on a button*/
function deleteById(id) {
    fetch("http://localhost:8080/guests/" + id, {
        method: "DELETE",
        headers: {
            Authorization: "Basic " + btoa("admin:adminpassword"),

            "Content-Type": "application/json",
        },
    })
        .then((result) => {
            console.log(result);
            window.location.reload();
        })
        .catch((error) => {
            console.error("Error:", error);
        });
}

/* updates a guest by id*/
function updateGuest() {
    const firstName = document.getElementById("firstname").value;
    const lastName = document.getElementById("lastname").value;
    const email = document.getElementById("emailaddress").value;
    const visitTime = document.getElementById("visittime").value;
    const roomId = document.getElementById("roomId").value;
    const id = document.getElementById("guestId").value;

    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");
    myHeaders.append("Authorization", "Basic " + btoa("admin:adminpassword"));

    var raw = JSON.stringify({
        guestId: id,
        firstName: firstName,
        lastName: lastName,
        emailAddress: email,
        roomId: roomId,
        visitTime: visitTime,
    });

    var requestOptions = {
        method: "PUT",
        headers: myHeaders,
        body: raw,
        redirect: "follow",
    };

    fetch("http://localhost:8080/guests", requestOptions)
        .then((response) => response.text())
        .then((result) => {
            console.log(result);
        })
        .catch((error) => console.log("error", error));
}

/* creates a guest*/
function createGuest() {
    const firstName = document.getElementById("first-name").value;
    const lastName = document.getElementById("last-name").value;
    const email = document.getElementById("email-address").value;
    const visitTime = document.getElementById("visit-time").value;
    const roomId = document.getElementById("room-Id").value;

    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");
    myHeaders.append("Authorization", "Basic " + btoa("admin:adminpassword"));

    var raw = JSON.stringify({
        firstName: firstName,
        lastName: lastName,
        emailAddress: email,
        roomId: roomId,
        visitTime: visitTime,
    });
    var requestOptions = {
        method: "POST",
        headers: myHeaders,
        body: raw,
        redirect: "follow",
    };

    fetch("http://localhost:8080/guests", requestOptions)
        .then((response) => response.text())
        .then((result) => {
            console.log(result);
        })
        .catch((error) => {
            console.log("error", error);
        });
}

/* checks if the email is valid*/
function checkEmail(emailName, emailTextName) {
    var emailInput = document.getElementById(emailName);
    if (emailInput.value == "") {
        emailInput.classList.remove("is-valid");
        emailInput.classList.remove("is-invalid");
        return;
    }
    var emailText = document.getElementById(emailTextName);
    var email = emailInput.value.trim();

    var emailRegex = /^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]{2,7}$/;

    if (emailRegex.test(email)) {
        emailInput.classList.remove("is-invalid");
        emailInput.classList.add("is-valid");
        emailText.innerHTML = "Email is valid";
        emailText.classList.remove("invalid-feedback");
        emailText.classList.add("valid-feedback");
    } else {
        emailInput.classList.remove("is-valid");
        emailInput.classList.add("is-invalid");
        emailText.innerHTML = "Email is invalid! Must include @ and .";
        emailText.classList.remove("valid-feedback");
        emailText.classList.add("invalid-feedback");
    }
}

/* checks if the room is valid*/
function checkForValidRoomId(roomName, roomText) {
    const roomInput = document.getElementById(roomName);
    if (roomInput.value == "") {
        roomInput.classList.remove("is-valid");
        roomInput.classList.remove("is-invalid");
        return;
    }
    const roomTextElement = document.getElementById(roomText);
    if (roomInput.value > countRooms || roomInput.value < 1) {
        roomInput.classList.remove("is-valid");
        roomInput.classList.add("is-invalid");
        roomTextElement.innerHTML = "Room is invalid!";
        roomTextElement.classList.remove("valid-feedback");
        roomTextElement.classList.add("invalid-feedback");
    } else {
        roomInput.classList.remove("is-invalid");
        roomInput.classList.add("is-valid");
        roomTextElement.innerHTML = "Room is valid";
        roomTextElement.classList.remove("invalid-feedback");
        roomTextElement.classList.add("valid-feedback");
    }
}

/* checks if the number is valid*/
function validNumber(inputName, errorText) {
    const input = document.getElementById(inputName);
    const text = document.getElementById(errorText);
    if (input.value == "") {
        input.classList.remove("is-valid");
        input.classList.remove("is-invalid");
        return;
    }
    if (inputName == "guest-id") {
        if (input.value < 1) {
            input.classList.remove("is-valid");
            input.classList.add("is-invalid");
            text.innerHTML = "ID is invalid!";
            text.classList.remove("valid-feedback");
            text.classList.add("invalid-feedback");
            return;
        } else {
            input.classList.remove("is-invalid");
            input.classList.add("is-valid");
            text.innerHTML = "ID is valid";
            text.classList.remove("invalid-feedback");
            text.classList.add("valid-feedback");
            return;
        }
    }

    if (input.value < 1 || input.value > 20) {
        input.classList.remove("is-valid");
        input.classList.add("is-invalid");
        text.innerHTML = "Number is invalid!";
        text.classList.remove("valid-feedback");
        text.classList.add("invalid-feedback");
    } else {
        input.classList.remove("is-invalid");
        input.classList.add("is-valid");
        text.innerHTML = "Number is valid";
        text.classList.remove("invalid-feedback");
        text.classList.add("valid-feedback");
    }
}

