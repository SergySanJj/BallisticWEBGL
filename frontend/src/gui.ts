import {Scene} from "./scene";
import {BallisticParams} from "./ballisticParams";
import {Message} from "./message";

export class BallisticGUI {
    private gravitySlider: HTMLInputElement;
    private gravityDisplay: HTMLElement;

    private angleSlider: HTMLInputElement;
    private angleDisplay: HTMLElement;

    private speedSlider: HTMLInputElement;
    private speedDisplay: HTMLElement;

    private fireButton: HTMLInputElement;
    private clearButton: HTMLInputElement;

    private objectCount: HTMLElement;

    private scene: Scene;
    private socket: WebSocket;

    constructor() {
        this.initGUI();
        this.initGUIevents();

        this.updateGui();
    }

    public setScene(scene: Scene) {
        this.scene = scene;
    }

    private initGUI() {
        this.gravitySlider = window.document.getElementById("gravity-slider") as HTMLInputElement;
        this.gravityDisplay = window.document.getElementById("gravity-display");

        this.angleSlider = window.document.getElementById("angle-slider") as HTMLInputElement;
        this.angleDisplay = window.document.getElementById("angle-display");

        this.speedSlider = window.document.getElementById("speed-slider") as HTMLInputElement;
        this.speedDisplay = window.document.getElementById("speed-display");

        this.fireButton = window.document.getElementById('fire') as HTMLInputElement;
        this.clearButton = window.document.getElementById('clear') as HTMLInputElement;

        this.objectCount = window.document.getElementById("object-count");
    }

    private initGUIevents() {
        const gui = this;
        this.gravitySlider.addEventListener('input', () => {
            gui.updateGui()
        });
        this.angleSlider.addEventListener('input', () => {
            gui.updateGui()
        });
        this.speedSlider.addEventListener('input', () => {
            gui.updateGui()
        });

        this.fireButton.addEventListener('click', () => {
            gui.fireProjectile()
        });
        this.clearButton.addEventListener('click', () => {
            gui.clearAll();
        });
    }

    public updateGui() {
        this.gravityDisplay.innerHTML = this.gravitySlider.value;
        this.angleDisplay.innerHTML = this.angleSlider.value;
        this.speedDisplay.innerHTML = this.speedSlider.value;
        if (this.scene) {
            this.objectCount.innerHTML = this.getObjectCount().toString();
        }
    }

    public clearScreen() {
        this.scene.clearScreen();
    }

    public clearAll() {
        this.scene.clearAll();
        this.updateGui();
    }

    public fireProjectile() {
        const msg = new Message('fire', JSON.stringify(this.getBallisticParams()));
        this.sendMessage(JSON.stringify(msg));
    }

    public assignSocket(socket: WebSocket) {
        this.socket = socket;
    }

    private sendMessage(message: string) {
        this.socket.send(message);
    }

    public getBallisticParams(): BallisticParams {
        const angle = parseFloat(this.angleDisplay.innerHTML);
        const gravity = parseFloat(this.gravityDisplay.innerHTML);
        const speed = parseFloat(this.speedDisplay.innerHTML);
        const bp = new BallisticParams(angle, speed, gravity);

        return bp;
    }

    public getObjectCount(): number {
        return this.scene.getObjectCount();
    }
}
