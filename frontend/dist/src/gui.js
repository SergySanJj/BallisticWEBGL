var BallisticGUI = /** @class */ (function () {
    function BallisticGUI() {
        // console.log(document.getElementById("gravity-slider"));
        this.gravitySlider = document.getElementById("gravity-slider");
        this.gravityDisplay = document.getElementById("gravity-display");
        this.fireButton = document.getElementById('fire');
        this.clearButton = document.getElementById('fire');
        this.gravitySlider.addEventListener('input', this.updateGui);
        this.fireButton.addEventListener('click', this.fireProjectile);
        this.clearButton.addEventListener('click', this.clearDisplay);
        this.canvas = document.querySelector('canvas');
        this.gl = this.canvas.getContext("webgl");
        this.gl.clearColor(1.0, 0.0, 0.0, 1.0);
        this.gl.clear(this.gl.COLOR_BUFFER_BIT);
        this.updateGui();
    }
    BallisticGUI.prototype.updateGui = function () {
        var newGravity = this.gravitySlider.value;
        this.gravityDisplay.innerHTML = newGravity;
    };
    BallisticGUI.prototype.clearDisplay = function () {
        this.gl.clear(this.gl.COLOR_BUFFER_BIT);
    };
    BallisticGUI.prototype.fireProjectile = function () {
    };
    return BallisticGUI;
}());
export { BallisticGUI };
//# sourceMappingURL=gui.js.map