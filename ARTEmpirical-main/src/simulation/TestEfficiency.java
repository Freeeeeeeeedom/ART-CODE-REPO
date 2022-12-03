package simulation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import algorithm.fscs.FSCS_art;
import model.AbstractART;
import model.DomainBoundary;
import model.Parameters;
import algorithm.pbs.PBS_art;
import algorithm.sbs.SBS_art;

public class TestEfficiency {

	final static int times = 1003;

	public static void main(String[] args) throws IOException {
		int[] dimensionList = {3};

		for (int dimension : dimensionList) {
			efficiency(dimension, SBS_art.class);
		}
	}

	public static void efficiency(int dim, Class<? extends AbstractART> method) {
		ArrayList<Integer> nums = new ArrayList<>();
		nums.add(200);
		nums.add(400);
		nums.add(600);
		nums.add(800);
		nums.add(1000);
		nums.add(2000);
		DomainBoundary inputBoundary=new DomainBoundary(dim,-5000,5000);

		for (int num : nums) {
			String base = "..\\efficiency\\" + method.getName() +"\\SR";
			String s1 = base + "\\pn_" + num + "d_" + dim + ".txt";
			File filedir = new File(base);
			if(!filedir.exists()){
				filedir.mkdirs();
			}
			try {
				testART(dim, s1, inputBoundary, num,Parameters.lp, method);

			} catch (IOException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
				e.printStackTrace();
			}
		}
	}

	public static void testART(int dim, String file, DomainBoundary inputBoundary, int pointNum, double lp, Class<? extends AbstractART> method) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		SBS_art art;
		double sum = 0;

		ArrayList<Double> result = new ArrayList<Double>();
        Constructor constructor = method.getConstructor(DomainBoundary.class, Double.class);

		for (int i = 0; i < times; i++) {
			long n1 = System.currentTimeMillis();

            AbstractART art_block = (AbstractART) constructor.newInstance(inputBoundary, Parameters.lp);

			art = new SBS_art(inputBoundary,lp);
			art.testEfficiency(pointNum);

			long n2 = System.currentTimeMillis();

			if (i != 0 && i != 1 && i != 2) {
				double temp = n2 - n1;
				sum = sum + temp;
				result.add(temp);
				System.out.print(i+"_"+temp+"\t");
			}

		}
		double num = times - 3;

		System.out.println("\r\n ART d = " + dim + "\tpointNum = " + pointNum + "\t" + sum / num  + "\t");

		java.io.File F = new java.io.File(file);
		FileWriter writeStream = new FileWriter(F);

		writeStream.write("point num: " + pointNum + "\r\n" +"sum/num: "+ sum/num + "\r\n" );

		writeStream.flush();
		writeStream.close();
	}

}
