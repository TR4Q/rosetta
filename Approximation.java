import java.util.ArrayList;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.awt.Color;

public class Approximation{

	/* 
	########################
	###     PROBLEMS     ###
	########################
	*/
	//PROBLEMAS NO SON PROBLEMAS NADA MAS

	/*
	#####################
	##  Data/Constants ##
	#####################
	*/

	//arraylists will be starting from day 1 to whereever
	//index 0 is day 1
	private ArrayList<BigMass> sigForces = new ArrayList<BigMass>();

//I break encapsulation just like I break rules
	public static ArrayList<double[]> pRosetta = new ArrayList<double[]>();
	public static ArrayList<double[]> vRosetta = new ArrayList<double[]>();
	private ArrayList<double[]> pEarth = new ArrayList<double[]>();
	private ArrayList<double[]> pMars = new ArrayList<double[]>();
	private ArrayList<double[]> pJupiter = new ArrayList<double[]>();
	private ArrayList<double[]> pSun = new ArrayList<double[]>();
	private ArrayList<double[]> pVenus = new ArrayList<double[]>();
	private ArrayList<double[]> pMoon = new ArrayList<double[]>();
	private ArrayList<double[]> pSaturn = new ArrayList<double[]>();
	public static ArrayList<double[]> pComet = new ArrayList<double[]>();
	private ArrayList<double[]> pMercury = new ArrayList<double[]>();
	private ArrayList<double[]> pUranus = new ArrayList<double[]>();
	private ArrayList<double[]> pNeptune = new ArrayList<double[]>();
	private ArrayList<double[]> pLutetia = new ArrayList<double[]>();
	public static ArrayList<double[]> jplRosetta = new ArrayList<double[]>();

//creating velocity correction double[]
	private ArrayList<double[]> vCorrections = new ArrayList<double[]>();
	private double[] v1Rosetta = {-2.449132341473928E-03, -8.081287809231398E-03, -2.448904327561051E-04};
	private double[] v0_5Rosetta = {-8.965755926923492E-03, -9.327192021914297E-03,  8.174140777247279E-05};
	private double[] v2Rosetta = {2.301562199407483E-03, -4.761230484636576E-03, -5.793947502135982E-04};
	private double[] v1_5Rosetta = {5.700021008759269E-04, -6.563324466002967E-03, -5.884699014225143E-04};
	private double[] v3Rosetta = {4.254002866111273E-03, -1.245281877123562E-03, -4.463479956798931E-04};
	private double[] v2_5Rosetta = {3.454009111595029E-03, -3.017181805491180E-03, -5.282658344758123E-04};
	private double[] v3_5Rosetta = {4.776930389490283E-03,  6.506068886836056E-04, -3.317526549637048E-04};
	private double[] v4Rosetta = {5.004070637922045E-03,  2.846896989903530E-03, -1.684588553418569E-04};
	private double[] v4_5Rosetta = {4.745493715691966E-03,  5.607697924886107E-03,  7.798452944970831E-05};

//Period in days for orbital graphics, 0 because too lazy
	private int mercPeriod = 88;
	private int venusPeriod = 225;
	private int earthPeriod = 365;
	private int marsPeriod = 687;
	private int jupiPeriod = 0;
	private int satuPeriod = 0;
	private int uranPeriod = 0;
	private int neptPeriod = 0;
	private int moonPeriod = 0;
	private int lutetPeriod = 1500;
	private int cometPeriod = 0;


	private BigMass earth, mars, jupiter, sun, moon, saturn, 
	venus, comet, mercury, uranus, neptune, lutetia;
	public Approximation(boolean _runCalcs) throws IOException{
	//sets up the sun (color and location)
		for(int i = 0; i<500000 ; i++){
			pSun.add(new double[]{0,0,0});
		}
		Color sunColor = new Color(239,225,32);
		sun = new BigMass(1.988544E+30, pSun, 49580, sunColor, sunColor, 0, "Sun"
			,SpaceCanvas.bobRossHead, false);

	//sets inital position and velocity of rosetta
		double dayToHour = 24.0;
		pRosetta.add(new double[]{6.999013793210094E-01,  7.273402208287623E-01, -1.156085542813600E-02});
		vRosetta.add(new double[]{(-1.715271288817883E-02) /dayToHour,  
			(9.932375860308275E-03) /dayToHour,  
			(2.652040639367198E-03) /dayToHour});

	//adds velocity to the corrections array that happens every 6 months
		vCorrections.add(Functions.scalarTimesVector(1.0/24, v0_5Rosetta));
		vCorrections.add(Functions.scalarTimesVector(1.0/24, v1Rosetta));
		vCorrections.add(Functions.scalarTimesVector(1.0/24, v1_5Rosetta));
		vCorrections.add(Functions.scalarTimesVector(1.0/24, v2Rosetta));
		vCorrections.add(Functions.scalarTimesVector(1.0/24, v2_5Rosetta));
		vCorrections.add(Functions.scalarTimesVector(1.0/24, v3Rosetta));
		vCorrections.add(Functions.scalarTimesVector(1.0/24, v3_5Rosetta));
		vCorrections.add(Functions.scalarTimesVector(1.0/24, v4Rosetta));
		vCorrections.add(Functions.scalarTimesVector(1.0/24, v4_5Rosetta));

	//gets data from the txt files which contain jpl data
		getDataFromFiles("MercuryData.txt", pMercury, "pMercury");
		getDataFromFiles("VenusData.txt", pVenus, "pVenus");
		getDataFromFiles("EarthData.txt", pEarth, "pEarth");
		getDataFromFiles("MoonData.txt", pMoon, "pMoon");
		getDataFromFiles("MarsData.txt", pMars, "pMars");
		getDataFromFiles("JupiterData.txt", pJupiter, "pJupiter");
		getDataFromFiles("SaturnData.txt", pSaturn, "pSaturn");
		getDataFromFiles("UranusData.txt", pUranus, "pUranus");
		getDataFromFiles("NeptuneData.txt", pNeptune, "pNeptune");
		getDataFromFiles("CometData.txt", pComet, "pComet");
		getDataFromFiles("LutetiaData.txt", pLutetia, "pLutetia");
	
		getDataFromFiles("RosettaData.txt", jplRosetta, "jplRosetta");
	//sun data is the dumbest thing you can do, wtf are you doing
		//getDataFromFiles("SunData.txt", pSun, "pSun");

	//Creating color variables and inputting in information for each planet/bigmass
		Color mercColorPlanet = new Color(191,152,60);
		Color mercColorOrbit = new Color(230,173,134);
		mercury = new BigMass(3.302E+23, pMercury, 2440, mercColorPlanet, mercColorOrbit, mercPeriod,"Mercury"
			,SpaceCanvas.bobRossHead, false);

		Color venusColorPlanet = new Color(181,183,30);
		Color venusColorOrbit = new Color(212,212,75);
		venus = new BigMass(4.8685E+24, pVenus,6052, venusColorPlanet, venusColorOrbit, venusPeriod,"Venus"
			,SpaceCanvas.bobRossHead, false);

		Color earthColorPlanet = new Color(23,135,68);
		Color earthColorOrbit = new Color(125,228,166);
		earth = new BigMass(5.97219E+24, pEarth, 6371, earthColorPlanet, earthColorOrbit, earthPeriod,"Earth"
			,SpaceCanvas.bobRossHead, false);

		Color moonColorPlanet = new Color(152,141,116);
		Color moonColorOrbit = new Color(230,173,134);
		moon = new BigMass(7.349E+22, pMoon,1737, moonColorPlanet, moonColorOrbit, moonPeriod,"Moon"
			,SpaceCanvas.bobRossHead, false);

		Color marsColorPlanet = new Color(209,112,48);
		Color marsColorOrbit = new Color(247,158,98);
		mars = new BigMass(6.4185E+23, pMars,3390, marsColorPlanet, marsColorOrbit, marsPeriod,"Mars"
			,SpaceCanvas.bobRossHead, false);

		Color jupiColorPlanet = new Color(121,36,11);
		Color jupiColorOrbit = new Color(230,173,134);
		jupiter = new BigMass(1.899E+27, pJupiter,8991, jupiColorPlanet, jupiColorOrbit, jupiPeriod,"Jupiter"
			,SpaceCanvas.bobRossHead, false);

		Color saturnColorPlanet = new Color(191,131,91);
		Color saturnColorOrbit = new Color(230,173,134);

		saturn = new BigMass(5.68319E+26, pSaturn,7823, saturnColorPlanet, saturnColorOrbit, satuPeriod,"Saturn"
			,SpaceCanvas.bobRossHead, false);

		Color cometColorPlanet = new Color(152,141,116);
		Color cometColorOrbit = new Color(230,173,134);
		uranus = new BigMass(86.8103E+24, pUranus, 2536, cometColorPlanet, cometColorOrbit,uranPeriod,"Uranus"
			,SpaceCanvas.bobRossHead, false);

		neptune = new BigMass(102.41E+24, pNeptune, 2462, cometColorPlanet, cometColorOrbit, neptPeriod,"Neptune"
			,SpaceCanvas.bobRossHead, false);

		comet = new BigMass(1E+13, pComet,4000, Color.MAGENTA, cometColorOrbit, cometPeriod,"P67 Comet"
			,SpaceCanvas.cometImage, true);
//MASS ISN'T CORRECT?
		Color lutetiaColorOrbit = new Color(125,160,200);
		lutetia = new BigMass(1.7E+18, pLutetia,4000, Color.CYAN, lutetiaColorOrbit, lutetPeriod,"Lutetia Comet"
			,SpaceCanvas.bobRossHead, false);

		
	//adds all the planets to an arraylist on which I do operations
		sigForces.add(sun);
		
		sigForces.add(mercury);
		sigForces.add(venus);
		sigForces.add(earth);
		sigForces.add(mars);
		sigForces.add(lutetia);
		sigForces.add(comet);

	//these planets are not graphically displayed because they are too far out
		sigForces.add(jupiter);
		sigForces.add(moon);
		sigForces.add(saturn);
		sigForces.add(uranus);
		sigForces.add(neptune);

		writeAnnchi();

		if(_runCalcs){
			justDoIt();
			writeDataToFile(pRosetta,vRosetta);
		}
		else{
			noCalcs();
		}
	}

	/*
	###########################
	###  FILE READER STUFF  ###
	###########################
	*/
//don't calculate p of rosetta, just use the txt file
	public void noCalcs() throws IOException{
		BufferedReader inputStream = null;

        try {
            inputStream = new BufferedReader(new FileReader("rosetta.txt"));
            String line;
            while ((line = inputStream.readLine()) != null) {
            	String[] allParts = line.split(",");
            	double[] sigParts = new double[3];
            	for(int i = 1; i < 4; i++){
            		sigParts[i-1] = Double.parseDouble(allParts[i]);
            	}
            	pRosetta.add(sigParts);
                
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
	}
//gets data from files for whatever planet/file you want
	public void getDataFromFiles(String file, ArrayList<double[]> planet,
	 String arrayName) throws IOException{
		BufferedReader inputStream = null;

        try {
            inputStream = new BufferedReader(new FileReader(file));
            String line;
            while ((line = inputStream.readLine()) != null) {
            	String[] allParts = line.split(",");
            	double[] sigParts = new double[3];
            	for(int i = 2; i < 5; i++){
            		sigParts[i-2] = Double.parseDouble(allParts[i]);
            	}
            	planet.add(sigParts);
                
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
	}
//if you want syntax in the form of new double[]
	public void getSyntaxFromFiles(String file, ArrayList<double[]> planet,
	 String arrayName) throws IOException{
		BufferedReader inputStream = null;
        PrintWriter outputStream = null;

        try {
            inputStream = new BufferedReader(new FileReader(file));
            outputStream = new PrintWriter(new FileWriter("EarthSyntax.txt"));
            String line;
            while ((line = inputStream.readLine()) != null) {
            	String[] allParts = line.split(",");
            	double[] sigParts = new double[3];
            	for(int i = 2; i < 5; i++){
            		sigParts[i-2] = Double.parseDouble(allParts[i]);
            	}
            	outputStream.println(arrayName+".add(new double[]{" +sigParts[0] + ","+   
            		sigParts[1]+"," + sigParts[2]+  "});");
            	planet.add(sigParts);
                
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
	}

//writes to a file, given the p and v arrays
	public void writeDataToFile(ArrayList<double[]> listP, ArrayList<double[]> listV) throws IOException{
        PrintWriter outputStream = null;

        try {
            outputStream = new PrintWriter(new FileWriter("rosetta.txt"));
           	for(int i = 0; i<listP.size(); i++) {
           		String lineP = "Hour " + i + " P, ";
           		String lineV = "Hour " + i + " V, ";
           		for(int j = 0; j<3; j++){
           			if(j == 0){
	            		lineP = lineP+listP.get(i)[j];
	            		lineV = lineV+listV.get(i)[j];
	            	}
	            	else{
	            		lineP = lineP+", " +listP.get(i)[j];
	            		lineV = lineV+", " +listV.get(i)[j];
	            	}
                }
                outputStream.println(lineP);
                //outputStream.println(lineV);
            }
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
	}

//annchi's data
	public static ArrayList<double[]> annchi = new ArrayList<double[]>();
	public void writeAnnchi() throws IOException{
		BufferedReader inputStream = null;

        try {
            inputStream = new BufferedReader(new FileReader("AnnchiData.txt"));
            String line;
            while ((line = inputStream.readLine()) != null) {
            	String[] allParts = line.split(",");
            	double[] sigParts = new double[3];
            	for(int i = 0; i < 3; i++){
            		sigParts[i] = Double.parseDouble(allParts[i]);
            	}
            	annchi.add(sigParts);
                
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
	}

//writes the acceleration to a file given the acceleration array
	public void writeAccelToFile(ArrayList<double[]> listP) throws IOException{
        PrintWriter outputStream = null;

        try {
            outputStream = new PrintWriter(new FileWriter("rosetta.txt"));
           	for(int i = 0; i<listP.size(); i++) {
           		String lineP = "Planet " + i + " P, ";
           		for(int j = 0; j<3; j++){
           			if(j == 0){
	            		lineP = lineP+listP.get(i)[j];
	            	}
	            	else{
	            		lineP = lineP+", " +listP.get(i)[j];
	            	}
                }
                outputStream.println(lineP);
                //outputStream.println(lineV);
            }
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
	}

	/*
	#################
	## CALL TO RUN ##
	#################
	*/

	//called to run, no need to call anything else
	public void justDoIt(){
		for(int i = 2; i<pEarth.size()-7; i++){
			pRosetta.add(findPosition(i));
			if((i+1)%17520!=0)
				vRosetta.add(findVelocity(i));
			else{
				System.out.println("GO");
				//vRosetta.add(findVelocity(i));
				vRosetta.add(vCorrections.get(i/17520));
			}
		}
	}

	/*
	##########################
	##  Formulas/Functions  ##
	##########################
	*/

	public BigMass getAPlanet(int index){
		return sigForces.get(index);
	}
	//returns the list of all the planets that are used to calculate force
	public ArrayList<BigMass> getPlanets(){
		return sigForces;
	}

	//finds a position at a given time
	ArrayList<double[]> positionSum = new ArrayList<double[]>();
	public double[] findPosition(int hour){
//deltaT used to be day-1
		double deltaT = 1.0/4;
		positionSum.clear();
		positionSum.add(Functions.scalarTimesVector(deltaT*deltaT/2,findAcceleration(hour-1)));
	//day-2 because you want the previous days velocity/position
		//and also because indexes start at 0
		positionSum.add(Functions.scalarTimesVector(deltaT,vRosetta.get(hour-2)));
		positionSum.add(pRosetta.get(hour-2));
		return Functions.sumVectors(positionSum);
	}

	//finds the velocity at a given time
	ArrayList<double[]> velocitySum = new ArrayList<double[]>();
	public double[] findVelocity(int hour){
//deltaT used to be day-1
		double deltaT = 1.0/4;
		velocitySum.clear();
		velocitySum.add(Functions.scalarTimesVector(deltaT,findAcceleration(hour-1)));
//velocitySum.add(scalarTimesVector(deltaT,findAcceleration(day)));
		//should be day-1 because we're finding the acceleration of the previous day.
		velocitySum.add(vRosetta.get(hour-2));
		return Functions.sumVectors(velocitySum);
	}

	//finds the acceleration at a given time
	ArrayList<double[]> vectorsToSum = new ArrayList<double[]>();
	public double[] findAcceleration(int hour){
		vectorsToSum.clear();
		int index = hour-1;
		for(int i = 0; i<sigForces.size(); i++){
			vectorsToSum.add(sigForces.get(i).getForcePlanet(index));
		}
		double[] accelVector = Functions.sumVectors(vectorsToSum);
		return accelVector;
	}
}