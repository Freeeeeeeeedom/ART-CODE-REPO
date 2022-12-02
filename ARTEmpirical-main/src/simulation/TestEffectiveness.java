package simulation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import hybrid.Divide_Conquer_art;
import hybrid.EAR_art;
import mart.Mirror_art;
import rrt.ORRT_art;
import faultZone.*;
import model.*;
import sbs.SBS_art;


public class TestEffectiveness {
    //测试有效性

    final static double lp = Parameters.lp;
    final static int times = 100; // The times to run experiments
    final static int thread_pool_num = Parameters.thread_pool_num;

    final static double R = Parameters.R;


    static Class<? extends AbstractART> algorithm = Mirror_art.class;

    public static void main(String args[]) throws IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {


        String basePath = "..\\effectiveness\\";
        File filedir = new File(basePath+algorithm.getName());
        if(!filedir.exists()){
            filedir.mkdirs();
        }
        double[] areas = new double[]{0.01, 0.005, 0.002, 0.001}; // failure rate

        int[] dimensions = new int[]{2};  // dimension of inputdomain

        for (int dim : dimensions) {
            for (double area : areas) {
                DomainBoundary bd = new DomainBoundary(dim, -5000, 5000);

                String s1 = basePath + algorithm.getName() + "\\" + dim + "d-Block-" + area + ".txt";
                String s2 = basePath + algorithm.getName() + "\\" + dim + "d-Strip-" + area + ".txt";
                String s3 = basePath + algorithm.getName() + "\\" + dim + "d-Point-" + area + ".txt";

                test(bd, area, 1, s1, algorithm);
                test(bd, area, 2, s2, algorithm);
                test(bd, area, 3, s3, algorithm);
            }
        }
        System.exit(0);
    }


    /**
     * @param inputBoundary The inputdomain in Simulations.
     * @param failrate      The failrate used to generate the failure pattern.
     * @param faultZoneFlag 1: Block failure region; 2: Strip failure region; 3: Point failure region;
     * @param filePath      The results will be stored in this filepath
     * @param method    The method to generate test cases
     */

    public static void test(DomainBoundary inputBoundary, double failrate, int faultZoneFlag, String filePath, Class method) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, IOException {
        int dimension = inputBoundary.dimensionOfInputDomain();

        long sumsRRT = 0;

        ArrayList<Integer> FmeasureResults = new ArrayList<>();
        //FMeasure:检测第一次失败所需的平均测试次数
        int temp = 0;
        FaultZone fz = null;

        switch (faultZoneFlag) {
            case 1 -> fz = new FaultZone_Block(inputBoundary, failrate);
            case 2 -> fz = new FaultZone_Strip(inputBoundary, failrate, 0.9);
            case 3 -> fz = new FaultZone_Point_Square(inputBoundary, failrate);
            default -> {
            }
        }

        Constructor constructor = method.getConstructor(DomainBoundary.class, Double.class);

        for (int i = 1; i <= times; i++) {
            System.out.print(i);
            AbstractART art_block = (AbstractART) constructor.newInstance(inputBoundary, Parameters.lp);
            assert fz != null;
            temp = art_block.run(fz);
            //times for find fault
            FmeasureResults.add(temp);

            System.out.println("_" + temp + "\t");
            sumsRRT += temp;
        }

        double Fm = sumsRRT / (double) times;
        double Fr = sumsRRT / (double) times * failrate;

        System.out.println("\r\nDimension = " + dimension + "   failure-rate = " + failrate + "   faultFlag = " + faultZoneFlag);
        System.out.println("\tThe average of Fm=: " + Fm + "  Fart/Frt= " + Fr);


        try {

            java.io.File file2 = new java.io.File(filePath);
            FileWriter writeStream = new FileWriter(file2);
            writeStream.write(failrate + "\r\n" + Fm + "\r\n" + Fr + "\r\n" + "\r\n" + "\r\n");

            for (int j = 0; j < FmeasureResults.size(); j++) {
                writeStream.write(FmeasureResults.get(j) + "\r\n");
            }

            writeStream.flush();
            writeStream.close();

        } catch (IOException e) {

            System.out.println(e);
            try {
                System.out.println(String.format(filePath, method.getClass().getName()));
                Thread.sleep(30000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }

    }

}
