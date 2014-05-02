package morseCodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: hofmannt
 * Date: 4/25/14
 * Time: 9:13 AM
 * This class loads a file and, using a LookupTable for character mapping,
 * converts its contents into Morse Code.
 */

public class MorseEncoder {

	/**
	 * This is the main method. It loads the morse table, gets the input and output file names from
	 * the user, and encodes the file.
	 * @param args ignored
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException{
		LookupTable<String, String> morseTable = loadMorseTable("morsecode.txt");
		File inputFile = new File(promptFilename("Enter an input filename:", true));
		File outputFile = new File(promptFilename("Enter an output filename:", false));
		encode(morseTable, inputFile, outputFile);
	}

	/**
	 * Returns a LookupTable containing the morse code keys
	 * @param filename location of the morse file
	 * @return morse table
	 * @throws FileNotFoundException
	 */
	private static LookupTable loadMorseTable(String filename) throws FileNotFoundException{
		LookupTable<String, String> morseTable = new LookupTable<String, String>();
		Scanner scanner = null;
		try{
			scanner = new Scanner(new File(filename));
			String line;
			while (scanner.hasNextLine()){
				line = scanner.nextLine();
				String character = line.split("\t| ")[0];
				String morse = line.split("\t| ")[1];
				morseTable.put(character, morse);
			}
		}
		finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		return morseTable;
	}

	/**
	 * Returns a String for a file name, collected through System.in
	 * @param prompt String prompting the input
	 * @param mustExist whether or not the file must exist
	 * @return String file name
	 */
	private static String promptFilename(String prompt, boolean mustExist){
		String filename = null;
		boolean fileExists;
		Scanner in =  new Scanner(System.in);
		do{
			System.out.println(prompt);
			filename = in.next();
			fileExists = (new File(filename)).exists();
			if (!fileExists && mustExist){
				System.out.println("File does not exist. Please try again.");
			}
		} while(!fileExists && mustExist);
		return filename;
	}

	/**
	 * Encodes the plaintext into morse code
	 * @param morseTable table containing morse keys
	 * @param inputFile location of plaintext file
	 * @param outputFile location to save morse output
	 * @throws FileNotFoundException
	 */
	private static void encode(LookupTable<String, String> morseTable, File inputFile, File outputFile) throws FileNotFoundException{
		Scanner scanner = null;
		try{
			scanner = new Scanner(inputFile);
			String line;
			ArrayList<String> encodedLines = new ArrayList<String>();
			while (scanner.hasNextLine()){
				line = scanner.nextLine().toUpperCase();
				String encodedLine = "";
				for (String word : line.split(" ")){
					for (int i=0; i<word.length(); i++){
						String character = word.substring(i, i+1);
						if (morseTable.containsKey(character)){
							encodedLine += morseTable.get(character)+" ";
						}
						else{
							System.out.println("Warning: skipping: "+character);
						}
					}
					if (encodedLine.length() > 0){
						encodedLine += "| ";
					}
				}
				encodedLines.add(encodedLine);
			}
			output(encodedLines, outputFile);
		}
		finally {
			if (scanner != null) {
				scanner.close();
			}
		}
	}

	/**
	 * Writes morse code to the output file
	 * @param encodedLines ArrayList of lines to be written
	 * @param outputFile location of file to be written to
	 * @throws FileNotFoundException
	 */
	private static void output(ArrayList<String> encodedLines, File outputFile) throws FileNotFoundException {
		PrintWriter pw = null;
		try{
			pw = new PrintWriter(outputFile);
			for (String encodedLine : encodedLines){
				pw.println(encodedLine);
			}
		}
		finally{
			pw.close();
		}
	}

}
