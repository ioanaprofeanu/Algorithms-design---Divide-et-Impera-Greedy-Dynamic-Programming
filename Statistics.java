// Profeanu Ioana, 323CA
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import static java.util.Collections.nCopies;
import static java.util.Collections.reverseOrder;
import java.util.Scanner;

/**
 * Class for solving the Statistics problem
 */
public class Statistics {
	static class Task {
		public static final String INPUT_FILE = "statistics.in";
		public static final String OUTPUT_FILE = "statistics.out";
		int n;
		int alphabetLength = 26;
		// the list of strings
		ArrayList<String> listOfStrings = new ArrayList<>();
		// arraylist of arraylist, where listOfLetterOccurrence.get(i) represents the arraylist
		// of letters occurrence within the i-th word in listOfStrings;
		// within that arraylist, the j-th element represents the occurrence of the letter j in
		// that word; the letters are coded like so: a = 0, b = 1 ... z = 25
		ArrayList<ArrayList<Integer>> listOfLetterOccurrence = new ArrayList<>();

		public void solve() throws FileNotFoundException {
			readInput();
			writeOutput();
		}

		/**
		 * Read the input from the file
		 */
		private void readInput() throws FileNotFoundException {
			Scanner sc = new Scanner(new File(INPUT_FILE));
			// read n and k
			n = Integer.parseInt(sc.nextLine());
			// read the n strings
			for (int i = 0; i < n; i++) {
				String string = sc.nextLine();
				// add the string to the strings list
				listOfStrings.add(string);
				// initialise the frequency list of letters for the current string
				// initially, each element is 0
				ArrayList<Integer> wordLettersOccurrenceList =
					new ArrayList<>(nCopies(26, 0));
				// add the list of letters occurrence for the current string
				// to the general list
				listOfLetterOccurrence.add(wordLettersOccurrenceList);
				// iterate through the letters of the string
				for (int j = 0; j < string.length(); j++) {
					// get the index of the character with % 97
					// 97 in ascii is the code for the first letter, "a"
					int index = string.charAt(j) % 97;
					Integer oldValue = listOfLetterOccurrence.get(i).get(index);
					// increase the value in the frequency arraylist
					oldValue++;
					wordLettersOccurrenceList.set(index, oldValue);
				}
			}
			System.out.println(listOfLetterOccurrence);
			sc.close();
		}

		/**
		 * Write the output in the file
		 */
		private void writeOutput() {
			try {
				PrintWriter pw = new PrintWriter(OUTPUT_FILE);
				// print the result
				pw.printf("%d", getResult());
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		// to check if a letter is majority in a word, we have the formula:
		// noOccurrence > totalLength / 2; from this formula, we calculate the
		// "majority index" with the derived formula: 2 * noOccurrence - totalLength
		// if this difference is greater than 0, the letter is majority; otherwise,
		// it is not
		private int calculateMajorityIndexString(int wordIndex, int letterIndex, int wordLength) {
			return 2 * listOfLetterOccurrence.get(wordIndex).get(letterIndex) - wordLength;
		}

		private int getResult() {
			// we will keep a list which contains the majority index of a specific
			// current letter within all the words
			ArrayList<Integer> wordsLetterMajorityIndex =
				new ArrayList<>(nCopies(n, 0));
			// keep a variable for the result number of concatenated words
			int resultConcatenatedWords = -1;
			for (int i = 0; i < alphabetLength; i++) {
				for (int j = 0; j < listOfStrings.size(); j++) {
					wordsLetterMajorityIndex.set(j, calculateMajorityIndexString(j, i,
						listOfStrings.get(j).length()));
				}
				// sort the list of majority indexes in descending order
				wordsLetterMajorityIndex.sort(reverseOrder());
				// keep a variable for the number of current "concatenated" words
				int noConcatenatedWOrds = 0;
				// keep a variable for the current "concatenated" words majority index
				int totalMajorityIndex = 0;
				for (Integer letterMajorityIndex : wordsLetterMajorityIndex) {
					totalMajorityIndex += letterMajorityIndex;
					// if the majority index is 0 or lower, end iteration because it
					// means from further on, the letter will no longer be majority
					// within the concatenated string
					if (totalMajorityIndex <= 0) {
						// if the number of concatenated words is greater than the current
						// result, change the result
						if (resultConcatenatedWords < noConcatenatedWOrds) {
							resultConcatenatedWords = noConcatenatedWOrds;
						}
						break;
					}
					noConcatenatedWOrds++;
				}
				// check the result and the concatenated words again
				if (resultConcatenatedWords < noConcatenatedWOrds) {
					resultConcatenatedWords = noConcatenatedWOrds;
				}
			}
			// return the result
			return resultConcatenatedWords;
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		new Task().solve();
	}
}
