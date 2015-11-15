import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Image;

public class BigMass{
	double mass;
	int planetRadius;
	Color planetColor, orbitColor;
	ArrayList<double[]> position;
	int period = 1;
	String planetName = "getREKT";
	Image picture;
	boolean drawPic;
	
	public BigMass(double _mass, ArrayList<double[]> _position,
	double _planetRadius, Color _planetColor, Color _orbitColor, int _period, 
	String _planetName, Image _picture, boolean _drawPic){
		mass = _mass;
		position = _position;
		planetRadius = planetRadiusScaler(_planetRadius);
		planetColor = _planetColor;
		orbitColor = _orbitColor;
		period = _period;
		planetName = _planetName;
		picture = _picture;
		drawPic = _drawPic;
	}
	

	/*
	##########################
	##  Formulas/Functions  ##
	##########################
	*/

	//show me the most beautiful bob ross
	public void changeRossBoolean(boolean state){
		drawPic = state;
	}

	public String getName(){
		return planetName;
	}

	//finds the force acting on a planet at a given time/index
	public double[] getForcePlanet(int index){
		double[] vector = Functions.findDistanceBetween(position.get(index),
			Approximation.pRosetta.get(index));
		
		return Functions.scalarTimesVector(Functions.findGMRCubed(mass,vector),vector);
	}
	
	//draws the planet at a given time
	public void drawPlanet(Graphics windowTemp, int index){
		if(drawPic){
			windowTemp.drawImage(picture,
				findTopLeftCorner(15,Functions.AUScaler(position.get(index)[0]))+SpaceCanvas.xCenter,
				findTopLeftCorner(15,Functions.AUScaler(position.get(index)[1]))+SpaceCanvas.yCenter,
				null);
		}
		else{
			int xPlanet = findTopLeftCorner(planetRadius*2,Functions.AUScaler(position.get(index)[0]));
			int yPlanet = findTopLeftCorner(planetRadius*2,Functions.AUScaler(position.get(index)[1]));

			windowTemp.setColor(planetColor);
			windowTemp.fillOval(xPlanet+SpaceCanvas.xCenter,yPlanet+SpaceCanvas.yCenter,
				planetRadius*2,planetRadius*2);
		}
	}

	//draws planet at a given location, meant for the key
	public void drawPlanetHere(Graphics windowTemp, int xHere, int yHere){
		if(drawPic){
			windowTemp.drawImage(picture,findTopLeftCorner(15,xHere),
				findTopLeftCorner(15,yHere),null);
		}
		else{
			windowTemp.setColor(planetColor);
			windowTemp.fillOval(findTopLeftCorner(planetRadius*2,xHere),
				findTopLeftCorner(planetRadius*2,yHere),
				planetRadius*2,planetRadius*2);
		}
	}

	//draws the name of the planet for the key
	Font ComicSans = new Font("Comic Sans MS", Font.PLAIN, 20);
	Color textcolor = new Color(179,69,45, 200);
	public void drawName(Graphics windowTemp, int xHere, int yHere){
		windowTemp.setFont(ComicSans);
		windowTemp.setColor(textcolor);
		windowTemp.drawString(planetName, xHere, yHere);
	}

	//draws the orbit of the planet
	public static int SIZE_ORBIT = 2;
	public void drawOrbit(Graphics windowTemp){
		for(int i = 0 ; i<period*2 ; i++){
			int xPlanet = Functions.AUScaler(position.get(i*48)[0]);
			int yPlanet = Functions.AUScaler(position.get(i*48)[1]);
			windowTemp.setColor(orbitColor);
			windowTemp.fillOval(findTopLeftCorner(SIZE_ORBIT,xPlanet+SpaceCanvas.xCenter),
				findTopLeftCorner(SIZE_ORBIT,yPlanet+SpaceCanvas.yCenter),
				SIZE_ORBIT,SIZE_ORBIT);
		}
	}

	//scales AU to java graphical units
	

	//rofl idk, probably not even used
	public int planetRadiusScaler(double km){
		return (int) km/800;
	}

	//xLocation is the position that you want it to be at
    //This method returns the top left corner to draw it
    public int findTopLeftCorner(int xLength, int xCenterPlanet){
        return xCenterPlanet-xLength/2; 
    }
}