import {BallisticGUI} from './gui';
import {Message} from "./message";
import {Scene} from "./scene";
import {SceneObject} from "./sceneObject";
import {FlightInfo} from "./flightInfo";

const canvas = document.querySelector('canvas');
const gl = canvas.getContext("webgl");

gl.clearColor(0.0, 0.0, 0.0, 1.0);
gl.clear(gl.COLOR_BUFFER_BIT);


const scene = new Scene(gl);
const gui = new BallisticGUI();
gui.setScene(scene);
scene.run();

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
    console.log("Trying to reconnect");
};

socket.onmessage = function (event) {
    const msg = JSON.parse(event.data) as Message;
    console.log("Server message: " + msg.message);
    console.log("Server data: " + msg.data);
    if (msg.message === "createBall") {
        scene.addObject(new SceneObject(0, 0, parseInt(msg.data)));
    } else if (msg.message === "flight") {
        const info = JSON.parse(msg.data) as FlightInfo;
        scene.updateFromFlightInfo(info);
    }
    gui.updateGui();
};

socket.onerror = function (error: ErrorEvent) {
    console.log("Error: " + error.message);
};




