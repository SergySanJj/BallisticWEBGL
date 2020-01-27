import {Scene} from "./scene";
import {BallisticParams} from "./ballisticParams";
import {Message} from "./message";

export class BallisticGUI {
    private gravitySlider: HTMLInputElement;
    private gravityDisplay: HTMLElement;

    private angleSlider: HTMLInputElement;
    private angleDisplay: HTMLElement;

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
        const angle = parseInt(this.angleDisplay.innerHTML);
        const gravity = parseInt(this.gravityDisplay.innerHTML);
        const speedX = 1;
        const speedY = 1;
        const bp = new BallisticParams(angle, speedX, speedY, gravity);

        return bp;
    }

    public getObjectCount(): number {
        return this.scene.getObjectCount();
    }
}
