// Profeanu Ioana, 323CA
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class for solving the Walsh problem
 */
public class Walsh {
	static class Task {
		/**
		 * Class which contains indices elements, with the line and column indices
		 */
		static class Indices {
			int x, y;
			public Indices(int x, int y) {
				this.x = x;
				this.y = y;
			}
		}
		public static final String INPUT_FILE = "walsh.in";
		public static final String OUTPUT_FILE = "walsh.out";
		int n, k;
		// list of Indices objects, where we store the given indices pairs
		ArrayList<Indices> listOfIndices = new ArrayList<>();

		public void solve() {
			readInput();
			writeOutput();
		}

		/**
		 * Read the input from the file
		 */
		private void readInput() {
			try {
				Scanner sc = new Scanner(new File(INPUT_FILE));
				// read n and k
				n = sc.nextInt();
				k = sc.nextInt();
				// get all the k pairs of indices
				for (int i = 0; i < k; i++) {
					int x = sc.nextInt();
					int y = sc.nextInt();
					// create a new indices object and add it to the arraylist
					Indices newIndices = new Indices(x, y);
					listOfIndices.add(newIndices);
				}
				sc.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * Write the output in the file
		 */
		private void writeOutput() {
			try {
				PrintWriter pw = new PrintWriter(OUTPUT_FILE);
				// for each pair of indices, call the getResult method; its result is a boolean,
				// so write in the file as 0 for false and 1 for true
				for (int i = 0; i < k; i++) {
					if (getResult(n, listOfIndices.get(i).x, listOfIndices.get(i).y)) {
						pw.printf("%d", 1);
					} else {
						pw.printf("%d", 0);
					}
					if (i != k - 1) {
						pw.printf("\n");
					}
				}
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * Recursive function which computes the result
		 * @param n the size of the matrix
		 * @param x the x index
		 * @param y the y index
		 * @return the value located at a certain position in the matrix
		 */
		private boolean getResult(int n, int x, int y) {
			// the base case is when the wanted cell in the matrix is the first one;
			// here, the result is 0 (false)
			if (x == 1 && y == 1) {
				return false;
			}

			// get the middle of the matrix
			int middle = n / 2;

			// recursively call the function depending on which part of the matrix
			// we are on (considering that it is divided in 4 equal pieces, in the
			// middle of the lines and columns

			// if the indices are lower than the middle, we are in the top left
			// part of the matrix
			if (x <= middle && y <= middle) {
				return getResult(n / 2, x ,y);
			}

			// if the indices are lower than the middle, we are in the top right
			// part of the matrix
			if (x <= middle && y > middle) {
				return getResult(n / 2, x, y - middle);
			}
			// if the indices are lower than the middle, we are in the bottom left
			// part of the matrix
			if (x > middle && y <= middle) {
				return getResult(n / 2,x - middle, y);
			}
			// if the indices are lower than the middle, we are in the bottom right
			// part of the matrix
			if (x > middle && y > middle) {
				return !getResult(n / 2,x - middle, y - middle);
			}

			return false;
		}
	}

	public static void main(String[] args) {
		new Task().solve();
	}
}
