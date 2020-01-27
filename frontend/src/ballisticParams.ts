export class BallisticParams {
    private angle: number;
    private speed: number;
    private gravity: number;

    constructor(angle: number, speed: number,  gravity: number) {
        this.angle = angle;
        this.speed = speed;
        this.gravity = gravity;
    }
}
