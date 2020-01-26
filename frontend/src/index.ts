import {BallisticGUI} from './gui';

const gui = new BallisticGUI();

const loc = location.hostname;
const port = location.port;
const socketPort = 8081;
const socket = new WebSocket(`ws://${loc}:${socketPort}`);


socket.onopen = function () {
    console.log("Connected to " + socketPort);
    gui.assignSocket(socket);
};

socket.onclose = function (event) {
    if (event.wasClean) {
        console.log('Connection closed');
    } else {
        console.log('Connection broken');
    }
    console.log('Code: ' + event.code + ' Caused by: ' + event.reason);
};

socket.onmessage = function (event) {
    console.log("Server: " + event.data);
};

socket.onerror = function (error: ErrorEvent) {
    console.log("Error: " + error.message);
};


