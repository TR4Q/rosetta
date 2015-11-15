import java.io.IOException;
public class Runner{
	public static void main(String[] args) throws IOException{
		//Approximation go = new Approximation();
		//go.justDoIt();

		//if using this, then the rosetta data in the file can only be positions
		SpaceCanvas canvas;
		if(args.length==0)
			canvas = new SpaceCanvas(true);
		else
			canvas = new SpaceCanvas(false);
		canvas.animate();
	}
}
