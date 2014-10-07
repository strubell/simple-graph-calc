package hw02;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *  GraphCalc.java
 *  
 *  The executable Graph Calculator class. 
 *  
 *  To generate a file that may be used as input to the graph calculator to test all products on
 *  all combinations of named graphs of order 5, use the --genTests argument followed by the
 *  path to the folder to which you would like these files to be saved. This will also create a
 *  LaTeX .tex file that will list figures of all the graph products, assuming that these figures
 *  are being output by the Graph class (not on by default, TODO: make it easier to turn graph
 *  depiction output on/off; currently hard-coded.)
 * 
 *  @author Emma Strubell
 *  COS397 - Chawathe
 *  February 17, 2011
 */

public class GraphCalc{
	public static void main(String[] args){

		GraphCup parser;
		if(args.length == 2){
			if(args[0].equals("--genTests"))
				genTests(args[1]);
		}
		else{
			parser = new GraphCup(new GraphLex(System.in));
			try {
				parser.parse();
			} catch (Exception e) {
				System.err.println("Unable to parse input...");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Automates creation of massive test file and LaTeX document to display the results of
	 * such a test. See comments at top of class for more information.
	 * 
	 * @param path path to file output folder
	 */
	private static void genTests(String path){
		try {
			BufferedWriter inputfile = new BufferedWriter(new FileWriter(path + "/p1tests"));
			BufferedWriter latexfile = new BufferedWriter(new FileWriter(path + "/p1.tex"));
			Character[] gnames = new Character[]{'k', 'b', 'p', 'c', 'w', 's'};
			String[] opnames = new String[]{"dir", "car", "str", "odd", "lex", "sum", "join"};
			String[] texOps = new String[]{"\\otimes", "\\square", "\\boxtimes", "\\bigtriangleup", "\\circ", "\\oplus", "$---$"};
			String[] texNames = new String[]{"K_5", "K_{4,6}", "P_5", "C_5", "W_5", "S_5"};
			String[] nameds = new String[]{".K5", ".K4,6", ".P5", ".C5", ".W5", ".S5"};
			String[] binOps = new String[]{"(X)", "[ ]", "[X]", "(^)", "(o)", "(+)", "---"};
			
			// complements
			for(int i = 0; i < nameds.length; i++){
				inputfile.write(gnames[i] + "c = -" + nameds[i] + "; ");
				inputfile.write("print " + gnames[i] + "c;\n");
				latexfile.write("$\\overline{" + texNames[i] + "}$:");
				latexfile.write("\\input{p1figures/" + gnames[i] + "c.tex}\n");
			}
			
			// all binary operators
			for(int i = 0; i < nameds.length; i++){
				for(int j = i+1; j < nameds.length; j++){
					for(int k = 0; k < binOps.length; k++){
						inputfile.write(gnames[i] + opnames[k] + gnames[j] + " = ");
						inputfile.write(nameds[i] + binOps[k] + nameds[j] + "; ");
						inputfile.write("print " + gnames[i] + opnames[k] + gnames[j] + ";\n");
						latexfile.write("$" + texNames[i] + texOps[k] + " " + texNames[j] + "$:");
						latexfile.write("\\input{p1figures/" + gnames[i] + opnames[k] + gnames[j] + ".tex}\n");
					}
				}
			}
			inputfile.close();
			latexfile.close();
		} catch (IOException e) {
			System.err.println("Unable to output tests file!");
			e.printStackTrace();
		}
	}
}
