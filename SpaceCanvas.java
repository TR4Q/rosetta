import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.Toolkit;
import java.awt.Image;
import javax.swing.event.MouseInputAdapter;

/* 
	########################
	###     PROBLEMS     ###
	########################
	NO HAY PROBLEMS
*/

public class SpaceCanvas extends JFrame{
	private BufferedImage buffered;
	public static Image bobRossImage, nasaImage, bobRossHead, cometImage, annchiImage;
	
	private int widthOfPlanetScreen = 801;
 	private int heightOfPlanetScreen = 801;
 	private int widthOfScreen = 1003;
 	private int heightOfScreen = 801;
 	public static int xCenter = 501;
 	public static int yCenter = 501;
 	public static double zoomMultiplier = 1;
 	private int time = 0;
 	private boolean runCalcs;

 	private int bobRossOn = 0;
 //SUN
 	private Color sunColor = new Color(239,225,32);
 	private double sunRadiusDouble = planetRadiusScaler(800);
 	private int sunRadius = 20;

 	private Approximation data;
 	private ArrayList<BigMass> planets;
 	private MyListener listener = new MyListener();

	public SpaceCanvas(boolean _runCalcs) throws IOException{

		super("Rosetta");

		bobRossImage =Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/BobRoss.png"));
		nasaImage =Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/NasaImage.png"));
		bobRossHead =Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/BobRossHead.png"));
		cometImage =Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/CometImage.png"));
		annchiImage =Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/AnnchiImage.png"));

		data = new Approximation(_runCalcs);

		planets = data.getPlanets();

		setSize(widthOfScreen,heightOfScreen);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//addMouseListener(this);
		addMouseListener(listener);
		addMouseMotionListener(listener);
		addMouseWheelListener(listener);
     	 
	}

	double scalingConstant = zoomMultiplier*4.2*Math.pow(10,-7);
	public double kmScaler(double km){
		return km*scalingConstant;
	}

	public double planetRadiusScaler(double km){
		return km/800;
	}



 	/*
	############################################
	####   DRAWING THE PLANETS AND ORBITS   ####
	############################################
	*/

	//establishing gui and color constants
	private int rosettaSize = 10;
	private int widthButton = 150;
	private int heightButton = 50;
	private int xPButton = widthOfPlanetScreen+1+(widthOfScreen-widthOfPlanetScreen-widthButton-6)/2;
 	private Color blackBackground = new Color(56,66,78);
 	private Color guiBackground = new Color(123,148,179,50);
 	private Color guiOutline = new Color(37,44,54,200);
 	private Color buttonBackground = new Color(56,66,78,100);
 	private String[] planetNames = {"Sun","Mercury","Venus","Earth","Mars",
 	"P67 Comet","Lutetia Comet", "NASA Rosetta", "My Rosetta"};

 	Font ComicSans = new Font("Comic Sans MS", Font.PLAIN, 25);
 	Font ComicSansKey = new Font("Comic Sans MS", Font.PLAIN, 20);
	Color textcolor = new Color(179,69,45, 200);
	public void paint(Graphics window){
		if(buffered==null)
		 buffered = (BufferedImage)(createImage(getWidth(),getHeight()));

		Graphics windowTemp = buffered.createGraphics();

	//draws background
		windowTemp.setColor(blackBackground);
		windowTemp.fillRect(0,0,widthOfScreen,heightOfScreen);

	//bobRoss background
		if(bobRossOn==1){
			for(int i = 0; i<10 ; i++){
				for(int j = 0; j<10; j++){
				windowTemp.drawImage(bobRossImage,i*198-198+10,j*198-198+10, null);
				}
			}
		}
	//draws sun
		windowTemp.setColor(sunColor);
		sunRadius = 30;
		windowTemp.fillOval(xCenter-sunRadius/2,yCenter-sunRadius/2,sunRadius,sunRadius);

	//draws rosettas tails
		int timeSpeed = time*8;
		windowTemp.setColor(Color.BLUE);
		drawTail(windowTemp, Approximation.pRosetta, timeSpeed);
		windowTemp.setColor(Color.RED);
		drawTail(windowTemp, Approximation.jplRosetta, timeSpeed);

		windowTemp.setColor(Color.BLACK);
		if(yesAnnchi==1)
			drawTail(windowTemp, Approximation.annchi, timeSpeed/4);

		windowTemp.setColor(Color.MAGENTA);
		drawTail(windowTemp, Approximation.pComet, timeSpeed);
		
	//for every planet I want, I draw them and their orbit
		for(int i = 1; i<7; i++){
			planets.get(i).drawOrbit(windowTemp);
			planets.get(i).drawPlanet(windowTemp, timeSpeed);
		}

	//drawing both rosettas
		windowTemp.drawImage(bobRossHead,
			findTopLeftCorner(15,Functions.AUScaler(Approximation.pRosetta.get(timeSpeed)[0])+xCenter),
			findTopLeftCorner(15,Functions.AUScaler(Approximation.pRosetta.get(timeSpeed)[1])+yCenter), null);

		windowTemp.drawImage(nasaImage,
			findTopLeftCorner(15,Functions.AUScaler(Approximation.jplRosetta.get(timeSpeed)[0])+xCenter),
			findTopLeftCorner(15,Functions.AUScaler(Approximation.jplRosetta.get(timeSpeed)[1])+yCenter), null);
	
	windowTemp.setColor(Color.BLACK);
	if(yesAnnchi==1){
		windowTemp.drawImage(annchiImage, findTopLeftCorner(10,Functions.AUScaler(Approximation.annchi.get(timeSpeed/4)[0]))+xCenter,
			findTopLeftCorner(10,Functions.AUScaler(Approximation.annchi.get(timeSpeed/4)[1]))+yCenter, null);
	}

//GUI
	//transparent panel
		windowTemp.setColor(guiBackground);
		windowTemp.fillRect(widthOfPlanetScreen+1,0,
			widthOfScreen-widthOfPlanetScreen, heightOfPlanetScreen);
		windowTemp.setColor(guiOutline);
		windowTemp.drawRect(widthOfPlanetScreen+1,0,
			widthOfScreen-widthOfPlanetScreen, heightOfPlanetScreen);

	//buttons
	windowTemp.setColor(buttonBackground);
		windowTemp.fillRect(xPButton,50,
			widthButton,heightButton);
		windowTemp.fillRect(xPButton,130,
			widthButton,heightButton);

	windowTemp.setColor(guiOutline);
		windowTemp.drawRect(xPButton,50,
			widthButton,heightButton);
		windowTemp.drawRect(xPButton,130,
			widthButton,heightButton);

	//button text
		windowTemp.setFont(ComicSans);
		windowTemp.setColor(textcolor);
		windowTemp.drawString("Restart",xPButton+30 , 85);
		if(stopAnimation == 1){
			windowTemp.drawString("Resume",xPButton+33 , 165);
		}
		else{
			windowTemp.drawString("Stop",xPButton+45 , 165);
		}

	//planet key
		windowTemp.setColor(sunColor);
		sunRadius = 30;
		windowTemp.fillOval(findTopLeftCorner(sunRadius,xPButton),
			findTopLeftCorner(sunRadius,220),sunRadius,sunRadius);

		for(int i = 1; i<7; i++){
			planets.get(i).drawPlanetHere(windowTemp,xPButton,i*40+220);
			planets.get(i).drawName(windowTemp,xPButton+30,i*40+220+10);
		}
		windowTemp.setFont(ComicSansKey);
		windowTemp.drawString("Sun",xPButton+30,220+10);

		windowTemp.setColor(Color.RED);
		//windowTemp.fillRect(findTopLeftCorner(10,xPButton),
		//	findTopLeftCorner(10,7*40+220), 10,10);
		windowTemp.drawImage(nasaImage, findTopLeftCorner(10,xPButton),
			findTopLeftCorner(10,7*40+220), null);
		
		windowTemp.setColor(Color.BLUE);
		//windowTemp.fillRect(findTopLeftCorner(10,xPButton),
		//	findTopLeftCorner(10,8*40+220), 10,10);
		windowTemp.drawImage(bobRossHead, findTopLeftCorner(10,xPButton),
			findTopLeftCorner(10,8*40+220), null);
		windowTemp.drawImage(annchiImage, findTopLeftCorner(10,xPButton),
			findTopLeftCorner(10,9*40+220), null);
		windowTemp.setColor(textcolor);
		windowTemp.drawString("NASA Rosetta", xPButton+30,7*40+220+10);
		windowTemp.drawString("Jason Rosetta", xPButton+30,8*40+220+10);
		windowTemp.drawString("Annchi Rosetta", xPButton+30,9*40+220+10);

	//the most beautiful bob ross
		windowTemp.setColor(guiOutline);
		windowTemp.drawImage(bobRossImage, widthOfPlanetScreen+2, heightOfScreen-200, null);
		windowTemp.drawRect(widthOfPlanetScreen+2, heightOfScreen-200-1,
		 widthOfScreen-widthOfPlanetScreen+3, 1);

	//time and start/end date information
		windowTemp.setColor(textcolor);
		windowTemp.drawString("Hour: " + timeSpeed/4, 20,760);
		windowTemp.drawString("Start Date: 11-9-09 -> End Date: 10-20-14", 400,760);
		window.drawImage(buffered, 0, 0, null);
	}

	public void switchBobRoss(boolean state){
		for(BigMass each: planets){
			each.changeRossBoolean(state);
			if(each.getName().equals("P67 Comet")){
				each.changeRossBoolean(true);
			}
		}
	}

	//sizeOrbit is the size of each part of the tail
	private int sizeOrbit = 1;
	public void drawTail(Graphics windowTemp, ArrayList<double[]> positions,int currentIndex){
		for(int i = 0; i<currentIndex/24; i++){
			windowTemp.fillRect(
			findTopLeftCorner(sizeOrbit,Functions.AUScaler(positions.get(i*24)[0])+xCenter),
			findTopLeftCorner(sizeOrbit,Functions.AUScaler(positions.get(i*24)[1])+yCenter),
			sizeOrbit,sizeOrbit);
		}
	}

	//Given the center and the diameter, it finds the top left corner for java graphics
	public int findTopLeftCorner(int diameter, int centerPlanet){
        return centerPlanet-diameter/2; 
    }
 	 

//mouse listener stuff

    //if 0, it means no annchi
	private int yesAnnchi = 1;
	//stops animation if = to 1
	private int stopAnimation = 0;

	//created a new class to include both mouse listener and motion
	private class MyListener extends MouseInputAdapter {
		public void mouseClicked(MouseEvent e) {} 
		int xInitial = 501;
		int yInitial = 501;
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}

		//moves the planets around
        public void mouseDragged(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            int xDiff = x-xInitial;
            int yDiff = y-yInitial;
            xCenter = xCenter+xDiff;
            yCenter = yCenter+yDiff;
            xInitial = x;
            yInitial = y;
        }
        public void mouseReleased(MouseEvent e) {
           
        }

        //detects buttons
        public void mousePressed(MouseEvent e) 
		{
			//gets the x and y location of click
			int x = e.getX();
			int y = e.getY();
			xInitial = e.getX();
			yInitial = e.getY();
			if(x>xPButton && x<xPButton+widthButton){
				//reset buttom
				if(y>50 && y<50+heightButton){
					time = 0;
				}

				//pause and unpause button
				else if(y>130 && y<130+heightButton){
					stopAnimation = 1 - stopAnimation;
				}
			}
			if(x>widthOfPlanetScreen && x<widthOfScreen){
				if(y>600 && y<800){
					bobRossOn = 1- bobRossOn;
					if(bobRossOn==1){
						switchBobRoss(true);
					}
					else{
						switchBobRoss(false);
					}
				}
			}
			if(x>0 && x<200){
				if(y>0 && y<200){
					yesAnnchi = 1-yesAnnchi; 
				}
			}
		}

		//zoom zoom zoom
		public void mouseWheelMoved(MouseWheelEvent e){
			zoomMultiplier = zoomMultiplier - e.getUnitsToScroll()/10.0;
			if(zoomMultiplier<0){
				zoomMultiplier = 0.1;
			}
			Functions.scalingConstant=Functions.scalingConstantMouse*zoomMultiplier;
		}
    }
	
//Animation- Repains every 0.01 seconds?
	public void animate(){
    	//about 43000 total
    	//corrections after may 7
    	while( true)
    	{
        	try {
            	Thread.sleep(1);
        	} catch(InterruptedException ex) {
        		System.out.println("FAILED");
            	Thread.currentThread().interrupt();
        	}

        	time++;
        	//if pause or reach end limit, prevent time from increasing
        	if(stopAnimation == 1){
        		time--;
        	}
        	if(time>39384/2 ){
        		time--;
        	}
            repaint();
        	
    	}
 
	}
 
}





