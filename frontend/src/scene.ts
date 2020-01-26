import {SceneObject} from './sceneObject'

export class Scene {
    private objects: Array<SceneObject>;
    private gl: WebGLRenderingContext;

    constructor(gl: WebGLRenderingContext) {
        this.gl = gl;
    }

    public update() {
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
}
