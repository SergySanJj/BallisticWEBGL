import {Scene} from "./scene";

export class BallisticGUI {
    private gravitySlider: HTMLInputElement;
    private gravityDisplay: HTMLElement;
    private fireButton: HTMLInputElement;
    private clearButton: HTMLInputElement;

    private canvas: HTMLCanvasElement;
    private gl: WebGLRenderingContext;
    private scene: Scene;

    private socket: WebSocket;

    constructor() {
        this.initGUI();
        this.initGUIevents();

        this.canvas = document.querySelector('canvas');
        this.gl = this.canvas.getContext("webgl");

        this.gl.clearColor(1.0, 0.0, 0.0, 1.0);
        this.gl.clear(this.gl.COLOR_BUFFER_BIT);

        this.updateGui();

        this.scene = new Scene(this.gl);
    }

    private initGUI() {
        this.gravitySlider = window.document.getElementById("gravity-slider") as HTMLInputElement;
        this.gravityDisplay = window.document.getElementById("gravity-display");
        this.fireButton = window.document.getElementById('fire') as HTMLInputElement;
        this.clearButton = window.document.getElementById('fire') as HTMLInputElement;
    }

    private initGUIevents() {
        const gui = this;
        this.gravitySlider.addEventListener('input', () => {
            gui.updateGui()
        });
        this.fireButton.addEventListener('click', () => {
            gui.fireProjectile()
        });
        this.clearButton.addEventListener('click', () => {
            gui.clearDisplay()
        });
    }

    public updateGui() {
        this.gravityDisplay.innerHTML = this.gravitySlider.value;
    }

    public clearDisplay() {
        this.gl.clear(this.gl.COLOR_BUFFER_BIT);
    }

    public fireProjectile() {
        this.sendMessage("fire");
    }

    public assignSocket(socket: WebSocket) {
        this.socket = socket;
    }

    private sendMessage(message:string){
        this.socket.send(message);
    }
}
