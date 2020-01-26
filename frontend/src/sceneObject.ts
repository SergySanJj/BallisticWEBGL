export class SceneObject {
    private static maxID = 0;

    private id: number;
    private x: number;
    private y: number;

    constructor(x: number, y: number) {
        this.id = SceneObject.maxID;
        SceneObject.maxID++;

        this.setCoords(x, y);
    }

    public setCoords(x: number, y: number) {
        this.x = x;
        this.y = y;
    }

    public drawSelf(gl: WebGLRenderingContext) {
        console.log(`Object ${this.id} drawing on {${this.x} ${this.y}}`);
    }

    public getId() {
        return this.id;
    }
}
