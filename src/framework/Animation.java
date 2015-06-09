package framework;
import java.awt.Image;
import java.util.ArrayList;

/**Manages sprite frames for characters  
 * 
 * @author Liam
 *
 */
public class Animation {

	
	private ArrayList<AnimFrame> frames;//Array of frames with image and time to display
	private int currentFrame;//which frame to currently display
	private long animTime;//Current duration of frame loop
	private long totalDuration; //Total amount of duration in the frame loop
	
	
	public Animation(){
		//Create animation frame array with default values
		frames = new ArrayList<AnimFrame>();
		totalDuration = 0;
		
		//Concurrently change values
		synchronized(this) {
			animTime =0;
			currentFrame = 0;
		}
	}
	
	/**Add a image/frame to array along with the duration of that frame.
	 * 
	 * @param image frame of the animation
	 * @param duration length of time the frame is shown on screen
	 */
	public synchronized void addFrame(Image image, long duration){
		totalDuration += duration; 
		frames.add(new AnimFrame(image,totalDuration));
	}
	
	/**update to next frame in the array to display on screen. 
	 * 
	 * @param elapsedTime how much time has passed between the last frame and this frame. 
	 */
	public synchronized void update(long elapsedTime){
		//If there are more than one frame in the array
		if(frames.size() > 1){
			animTime += elapsedTime; 
			//If the current animation time has surpassed total animation time, reset.
			if(animTime >= totalDuration){
				//mod animation time and total time and save the remainder
				animTime = animTime % totalDuration;
				//go back to the first frame
				currentFrame = 0; 
			}
			
			//get frame number to display
			while(animTime > getFrame(currentFrame).endTime){
				currentFrame++;
			}
			}
	}
	
	/**Get current image within the frame to be displayed 
	 * 
	 * @return current frame image 
	 */
	public synchronized Image getImage(){
		if(frames.size() == 0){
			return null;
		}else {
			return getFrame(currentFrame).image;
		}
	}
	
	/**Get fame in array with endTime and image.
	 * 
	 * @param i which Animation frame to return
	 * @return Animation frame that has the endTime and image.
	 */
	private AnimFrame getFrame(int i){
		return (AnimFrame)frames.get(i);
	}
	
	//The frame date with the image to show and the time to display it
	private class AnimFrame {
		Image image;
		long endTime; 
		
		public AnimFrame(Image image, long endTime){
			this.image = image;
			this.endTime = endTime;
		}
	}
	
}
