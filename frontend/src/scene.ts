import {SceneObject} from './sceneObject'
import {FlightInfo} from "./flightInfo";
import {Observable} from "rxjs";

export class Scene {
    private objects: Array<SceneObject> = new Array<SceneObject>();
    private gl: WebGLRenderingContext;

    constructor(gl: WebGLRenderingContext) {
        this.gl = gl;
    }

    public update() {
        this.clearScreen();
        for (const obj of this.objects) {
            obj.drawSelf(this.gl);
        }
    }

    public addObject(object: SceneObject) {
        this.objects.push(object);
    }

    public getObject(id: number) {
        for (const obj of this.objects) {
            if (obj.getId() === id) {
                return obj;
            }
        }
    }

    public clearScreen() {
        this.gl.clear(this.gl.COLOR_BUFFER_BIT);
    }

    public clearAll() {
        this.clearScreen();
        this.objects = new Array<SceneObject>();
    }


    public updateFromFlightInfo(info: FlightInfo) {
        const obj = this.getObject(info.id);
        if (obj)
            obj.setCoords(info.x, info.y);
    }

    public async run() {
        console.log("Scene rendering cycle started");

        const scene = this;
        await setInterval(() => {
            scene.update();
        }, 40);

    }

    public getObjectCount(): number {
        return this.objects.length;
    }
}
