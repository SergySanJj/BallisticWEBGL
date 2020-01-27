export class SceneObject {
    private id: number;
    private x: number;
    private y: number;
    private gl: WebGLRenderingContext;

    constructor(x: number, y: number, id: number) {
        this.id = id;

        this.setCoords(x, y);
    }

    public setCoords(x: number, y: number) {
        this.x = x;
        this.y = y;
    }

    public drawSelf(gl: WebGLRenderingContext) {
        this.gl = gl;
        this.drawPoint();
    }

    public getId() {
        return this.id;
    }

    public drawPoint() {
        /*==========Defining and storing the geometry=======*/

        const vertices = [
            this.x, this.y, 0.0
        ];

        // Create an empty buffer object to store the vertex buffer
        const vertex_buffer = this.gl.createBuffer();

        //Bind appropriate array buffer to it
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, vertex_buffer);

        // Pass the vertex data to the buffer
        this.gl.bufferData(this.gl.ARRAY_BUFFER, new Float32Array(vertices), this.gl.STATIC_DRAW);

        // Unbind the buffer
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, null);

        /*=========================Shaders========================*/

        // vertex shader source code
        const vertCode =
            'attribute vec3 coordinates;' +

            'void main(void) {' +
            ' gl_Position = vec4(coordinates, 1.0);' +
            'gl_PointSize = 20.0;' +
            '}';

        // Create a vertex shader object
        const vertShader = this.gl.createShader(this.gl.VERTEX_SHADER);

        // Attach vertex shader source code
        this.gl.shaderSource(vertShader, vertCode);

        // Compile the vertex shader
        this.gl.compileShader(vertShader);

        // fragment shader source code
        const fragCode =
            'void main(void) {' +
            ' gl_FragColor = vec4(0.0, 0.0, 0.0, 0.1);' +
            '}';

        // Create fragment shader object
        const fragShader = this.gl.createShader(this.gl.FRAGMENT_SHADER);

        // Attach fragment shader source code
        this.gl.shaderSource(fragShader, fragCode);

        // Compile the fragmentt shader
        this.gl.compileShader(fragShader);

        // Create a shader program object to store
        // the combined shader program
        const shaderProgram = this.gl.createProgram();

        // Attach a vertex shader
        this.gl.attachShader(shaderProgram, vertShader);

        // Attach a fragment shader
        this.gl.attachShader(shaderProgram, fragShader);

        // Link both programs
        this.gl.linkProgram(shaderProgram);

        // Use the combined shader program object
        this.gl.useProgram(shaderProgram);

        /*======== Associating shaders to buffer objects ========*/

        // Bind vertex buffer object
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, vertex_buffer);

        // Get the attribute location
        const coord = this.gl.getAttribLocation(shaderProgram, "coordinates");

        // Point an attribute to the currently bound VBO
        this.gl.vertexAttribPointer(coord, 1, this.gl.FLOAT, false, 0, 0);

        // Enable the attribute
        this.gl.enableVertexAttribArray(coord);

        /*============= Drawing the primitive ===============*/

        // Clear the color buffer bit
        this.gl.clear(this.gl.COLOR_BUFFER_BIT);

        // Set the view port
        this.gl.viewport(0, 0, 1000, 800);

        // Draw the triangle
        this.gl.drawArrays(this.gl.POINTS, 0, 1);
    }
}
