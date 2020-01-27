export class BallisticParams {
    private angle: number;
    private speedX: number;
    private speedY: number;
    private gravity: number;

    constructor(angle: number, speedX: number, speedY: number, gravity: number) {
        this.angle = angle;
        this.speedX = speedX;
        this.speedY = speedY;
        this.gravity = gravity;
    }
}
