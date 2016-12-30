//mdm

package org.ima.whatworks.utils.wordalign.wordalign;

import java.io.*;

import java.text.NumberFormat;
/**
 * 
 * @author v131
 *
 */
public class Utils {
	private static final String LF = "\n";

	public static String getFileText(String strFileName, boolean isUTF8) {
		String fileName = strFileName;
		StringBuilder sb = new StringBuilder();
		BufferedReader in = null;
		try {
			if (isUTF8) {
				in = new BufferedReader(new InputStreamReader(new FileInputStream(strFileName), "UTF-8"));
			}
			else {
				in = new BufferedReader(new FileReader(strFileName));
			}

			String str;
			while ((str = in.readLine()) != null) {
				sb.append(str);
				sb.append(LF);
			}
		}
		catch (IOException e) {
			System.out.println("There is Exception: (" + e + ") in reading " + fileName);
		}
		finally {
			if (in != null) {
				try {
					in.close();
				}
				catch (Exception ignored) {
				}
			}
		}
		return sb.toString();
	}


	public static boolean checkForExist(int[] array, int value) {
		for (int tmp : array) {
			if (tmp == value) {
				return true;
			}
		}
		return false;
	}

	public static void print(String[] array) {
		System.out.println("");
		for (String str : array) {
			System.out.print(str + " ");
		}
		System.out.println("");
	}


	public static void printMemory() {
		// Runtime runtime = Runtime.getRuntime();

		NumberFormat format = NumberFormat.getInstance();

		long total = Runtime.getRuntime().totalMemory();
		long used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

		StringBuilder sb = new StringBuilder();
		sb.append("Total memory: " + format.format(total / 1024) + "\n");
		sb.append("Allocated memory: " + format.format(used / 1024) + "\n");

		System.out.println(sb.toString());
	}
}
