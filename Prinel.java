// Profeanu Ioana, 323CA
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


/**
 * Class for solving the Prinel problem
 */
public class Prinel {
	static class Task {
		public static final String INPUT_FILE = "prinel.in";
		public static final String OUTPUT_FILE = "prinel.out";
		int n, k;
		// the maximum number of values we can have within the target array
		int maxNumbers = 100000;

		// the number of operations for all the numbers that we could receive
		// similar to a frequency array, at allNumbersOperationList.get(i) we will have
		// the number of operations until reaching the value i
		// it initially has all its elements initialised with inf
		ArrayList<Long> allNumbersOperationList =
			new ArrayList<>(Collections.nCopies(maxNumbers + 1, Long.MAX_VALUE));
		// arraylist of the operations made for each target element
		ArrayList<Long> targetNoOperations = new ArrayList<>();
		// the points arraylist
		ArrayList<Integer> p = new ArrayList<>();

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
				getAllOperations();
				// read n and k
				n = sc.nextInt();
				k = sc.nextInt();
				// get all n elements of the target vector
				for (int i = 0; i < n; i++) {
					int target = sc.nextInt();
					// we won't keep the actual value of the number given,
					// we keep the number of operations until reaching it
					targetNoOperations.add(allNumbersOperationList.get(target));
				}
				// get the points vector
				for (int i = 0; i < n; i++) {
					int x = sc.nextInt();
					p.add(x);
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
				// print the result
				pw.println(getResult());
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * Method to get the list of divisors for a given number
		 * @param number the input number
		 * @return the list of divisors
		 */
		private ArrayList<Integer> getDivisors(int number) {
			ArrayList<Integer> listOfDivisors = new ArrayList<>();
			// iterate through the possible divisors values
			for (int i = 1; i <= Math.sqrt(number); i++) {
				// if the sum between the divisor and the number is
				// greater than the maximum number we can have within
				// target, return the list
				if (number + i > maxNumbers) {
					return listOfDivisors;
				}
				// if the current number is divisor
				if (number % i == 0) {
					if (i * i != number) {
						listOfDivisors.add(number / i);
					}
					listOfDivisors.add(i);
				}
			}
			// add the number itself
			listOfDivisors.add(number);
			// return the list
			return listOfDivisors;
		}

		/**
		 * Method which gets the total number of operations until
		 * reaching each number, using dynamic programming
		 */
		private void getAllOperations() {
			// initial case is for 1, to which the total number
			// of operations to be made is 0
			allNumbersOperationList.set(1, 0L);
			// general case for the rest of the numbers
			for (int i = 1; i <= maxNumbers; i++) {
				// get list of divisors
				ArrayList<Integer> listOfDivisors = getDivisors(i);
				// for each divisor
				for (int divisor : listOfDivisors) {
					// if the sum between the divisor and the number is
					// greater than the maximum number we can have within target
					if (divisor + i < maxNumbers) {
						// the value of the (i + divisor) th element in the array is
						// the minimum value between the current value (which initially is
						// inf, and the operations until reaching the number i + 1
						Long currentValue = allNumbersOperationList.get(i + divisor);
						allNumbersOperationList.set(i + divisor, Math.min(currentValue,
							allNumbersOperationList.get(i) + 1));
					}
				}
			}
		}

		/**
		 * Method which calculates the total number of points that could
		 * be accumulated by doing k or fewer operations
		 */
		private long getResult() {
			// if the total number of operations for each number within target is
			// lower than the total number of operations available (k), then
			// return the sum of all points (the sum of all elements in p)
			if (targetNoOperations.stream().mapToInt(Long::intValue).sum() <= k) {
				return p.stream().mapToInt(Integer::intValue).sum();
			}

			// create the dp array
			long []dp = new long[k + 1];

			// use dynamic programming in the same manner we would in
			// the case of the knapsack problem
			// here, k will be the weight, targetNoOperations will be the
			// array of objects and p the array of prices
			// the purpose is to maximise the cost
			for (int i = 1; i < n + 1; i++) {
				for (int j = k; j >= 0; j--) {
					if (targetNoOperations.get(i - 1) <= j) {
						// find the maximum value
						dp[j] = Math.max(dp[j],
							dp[(int) (j - targetNoOperations.get(i - 1))] + p.get(i - 1));
					}
				}
			}
			// the last element of the array is the result
			return dp[k];
		}
	}

	public static void main(String[] args) {
		new Task().solve();
	}
}