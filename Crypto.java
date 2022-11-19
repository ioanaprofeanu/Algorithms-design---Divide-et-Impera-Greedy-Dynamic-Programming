// Profeanu Ioana, 323CA
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Class for solving the Crypto problem
 */
public class Crypto {
	static class Task {
		public static final String INPUT_FILE = "crypto.in";
		public static final String OUTPUT_FILE = "crypto.out";
		int stringSize, substringSize;
		long mod = (long) (Math.pow(10, 9) + 7);
		String string = "";
		String substring = "";

		public void solve() throws FileNotFoundException {
			readInput();
			writeOutput();
		}

		/**
		 * Read the input from the file
		 */
		private void readInput() throws FileNotFoundException {
			Scanner sc = new Scanner(new File(INPUT_FILE));
			// read the sizes of the string and the substring
			stringSize = sc.nextInt();
			substringSize = sc.nextInt();
			sc.nextLine();
			// read the string and substring
			string = sc.nextLine();
			substring = sc.nextLine();
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

		/**
		 * Method which returns the power oof a given base and exponent
		 * @param base the base of the power
		 * @param exponent the exponent of the power
		 * @return the power
		 */
		long power(long base, long exponent) {
			long totalPower = 1;
			// while the exponent is not 0
			while (exponent > 0) {
				// if the exponent is even
				if (exponent % 2 == 0) {
					base = (((base % mod) * (base % mod)) % mod);
					exponent = exponent / 2;
				} else {
					// when the exponent is odd
					totalPower = (((totalPower % mod) * (base % mod)) % mod);
					exponent = exponent - 1;
				}
			}
			return totalPower;
		}

		/**
		 * Method which calculates the total number substring occurrences
		 * @return the total number of occurrences
		 */
		private long getResult() {
			// get the number of distinct characters within the string;
			// the number will be the total distinct number of letters in the
			// substring
			long distinctStringChars = substring.chars().distinct().count();
			// the dp array
			long[][] dp = new long[stringSize + 1][substringSize + 1];
			// initialise the first line of the dp array with zeros
			for (int i = 0; i <= substringSize; i++) {
				dp[0][i] = 0;
			}
			// initialise the columns
			dp[0][0] = 1;
			// count the number of "?" in the string
			int countQuestionMarks = 0;
			// iterate through the characters of the string
			for (int i = 1; i <= stringSize; i++) {
				// if the character is "?", increase their number
				if (string.charAt(i - 1) == '?') {
					countQuestionMarks++;
				}
				// the current element in the first column, linked to the current
				// word, is the distinct number of characters in the string at the
				// power of the number of question marks (unknown characters)
				dp[i][0] = power(distinctStringChars, countQuestionMarks) % mod;
			}

			// for each character in the string
			for (int i = 1; i <= stringSize; i++) {
				// for each character in the substring
				for (int j = 1; j <= substringSize; j++) {
					// if the string character is known
					if (string.charAt(i - 1) != '?') {
						if (string.charAt(i - 1) == substring.charAt(j - 1)) {
							// if the current characters in both string match,
							// the current value in the dp table is the sum of the one above
							// and the one on the left of the one above
							dp[i][j] = ((dp[i - 1][j] % mod) + (dp[i - 1][j - 1] % mod)) % mod;
						} else {
							// otherwise, the current value in the dp table is the same as the
							// one above
							dp[i][j] = dp[i - 1][j] % mod;
						}
					}
					if (string.charAt(i - 1) == '?') {
						// if the letter is unknown, its value is the one above multiplied
						// by the number of distinct characters in the string, plus the
						// one on the left of the one above
						dp[i][j] = (((dp[i - 1][j] % mod) * (distinctStringChars % mod))
							+ ((dp[i - 1][j - 1] % mod) % mod)) % mod;
					}
				}
			}
			// the result will be the last element in the matrix
			return dp[stringSize][substringSize];
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		new Task().solve();
	}
}
