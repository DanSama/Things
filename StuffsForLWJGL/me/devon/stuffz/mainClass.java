
// Main Package
import org.lwjgl.*;

// Input Manager
import org.lwjgl.glfw.*;

// Video Render
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class mainClass {
	
	// Window Handler
	private long window;

	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");
		
		init();
		loop();
		
		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		
		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
		
	}
	
	public void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();
		
		// Initialize GLFW. Most GLFW functions will not work before doing this
		if(!glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");
			
		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
	
		// Create the window
		window = glfwCreateWindow(300, 300, "Hello World!", NULL, NULL);
		if( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");
		
		// Setup a key callback. It will be called every time a key is pressed,
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE);
				glfwSetWindowShouldClose(window, true);
		});
		
		// Get the thread stack and push a new frame
		try(MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);
			
			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);
			
			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			
			// Center the window
			
			glfwSetWindowPos(
					window,
					(vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2
					); // The stack frame is popped automatically
			
			// Make the OpenGL context current
			glfwMakeContextCurrent(window);
			// Enable v-sync
			glfwSwapInterval(1);
			
			// Make the window visible
			glfwShowWindow(window);
		}
	}
	private void loop() {
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();
		
		// Set the clear color
		glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
		
		// Run the rendering loop until the user has attempted to close
		// The window or has pressed the ESCAPE key.
		while( !glfwWindowShouldClose(window)) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			glfwSwapBuffers(window); // swap the color buffers
			
			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
		}
	}
	public static void main(String[] args) {
		new mainClass().run();
	}
}
