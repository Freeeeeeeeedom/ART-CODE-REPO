package realZone;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import faultZone.*;
import algorithm.fscs.FSCS_art;
import algorithm.mart.RBMT_art;
import model.*;
import algorithm.pbs.PBS_art;
import algorithm.sbs.SBS_art;


public class RealTestEffectiveness {
    //测试有效性

    final static double lp = Parameters.lp;
    final static int times = 100; // The times to run experiments
    final static int thread_pool_num = Parameters.thread_pool_num;

    final static double R = Parameters.R;


    static Class<? extends AbstractART> algorithm = RBMT_art.class;
    static String originalName = "";
    public static void main(String args[]) throws Exception {

            int id = 11;
                switch (id) {
                    case 1 -> originalName = "Bessj";
                    case 2 -> originalName = "BubbleSort";
                    case 3 -> originalName = "Encoder";
                    case 4 -> originalName = "Expint";
                    case 5 -> originalName = "Fisher";
                    case 6 -> originalName = "Gammq";
                    case 7 -> originalName = "Median";
                    case 8 -> originalName = "Remainder";
                    case 9 -> originalName = "Triangle";
                    case 10 -> originalName = "Triangle2";
                    case 11 -> originalName = "Variance";
                }

                String basePath = "..\\effectiveness\\";
                String path = basePath + "RealZone-" + algorithm.getName()  + "\\" + originalName;
                File filedir = new File(path);

                if (!filedir.exists()) {
                    filedir.mkdirs();
                }
                double[] areas = new double[]{0.01}; // failure rate

                int[] dimensions = new int[]{2};  // dimension of inputdomain

                for (int dim : dimensions) {
                    for (double area : areas) {
                        DomainBoundary bd = new DomainBoundary(dim, -5000, 5000);

                        String s1 = path + "\\" + dim + "d-Block-" + area + ".txt";
                        test(bd, area, s1, (Class<AbstractART>) algorithm, id);
                    }
                }

        System.exit(0);
    }


    /**
     * @param inputBoundary The inputdomain in Simulations.
     * @param failrate      The failrate used to generate the failure pattern.
     * @param filePath      The results will be stored in this filepath
     * @param method    The method to generate test cases
     */

    public static void test(DomainBoundary inputBoundary, double failrate, String filePath, Class<AbstractART> method,int id) throws Exception {
        int dimension = inputBoundary.dimensionOfInputDomain();

        long sumsRRT = 0;
        ArrayList<Integer> FmeasureResults = new ArrayList<>();
        //FMeasure:检测第一次失败所需的平均测试次数
        int temp = 0;

        String packName = "dt.mutant." + originalName;
        Constructor<AbstractART> constructor = method.getConstructor(DomainBoundary.class, Double.class);

        Set<Class<?>> mutations = ClassUtil.getClasses(packName);
        //获取该包名下的所有类
        int index = 0;
        for(Class<?> mutation : mutations){
            System.out.print(index);
            index++;

            FaultZone fr = switch (id) {
                case 1 -> new realZone_Bessj(mutation.getConstructor(), mutation.getMethods()[0]);
                case 2 -> new realZone_BubbleSort(mutation.getConstructor(), mutation.getMethods()[0]);
                case 3 -> new realZone_Encoder(mutation.getConstructor(), mutation.getMethods()[0]);
                case 4 -> new realZone_Expint(mutation.getConstructor(), mutation.getMethods()[0]);
                case 5 -> new realZone_Fisher(mutation.getConstructor(), mutation.getMethods()[0]);
                case 6 -> new realZone_Gammq(mutation.getConstructor(), mutation.getMethods()[0]);
                case 7 -> new realZone_Median(mutation.getConstructor(), mutation.getMethods()[0]);
                case 8 -> new realZone_Remainder(mutation.getConstructor(), mutation.getMethods()[0]);
                case 9 -> new realZone_Triangle(mutation.getConstructor(), mutation.getMethods()[0]);
                case 10 -> new realZone_Triangle2(mutation.getConstructor(), mutation.getMethods()[0]);
                case 11 -> new realZone_Variance(mutation.getConstructor(), mutation.getMethods()[0]);
                default -> null;
            };
            AbstractART art_block = constructor.newInstance(inputBoundary, Parameters.lp);

            ThreadWithCallback callback = new ThreadWithCallback(inputBoundary,art_block,fr);
            ExecutorService executor = Executors.newFixedThreadPool(2);

            Future future = executor.submit(callback);
            try{
                Object result = future.get(1,TimeUnit.SECONDS);
                temp = (int) result;
            }
            catch (Exception ignored){

            }
            finally {
                FmeasureResults.add(temp);
                System.out.println("_" + temp + "\t");
                sumsRRT += temp;
                executor.shutdown();
            }
        }

        double Fm = sumsRRT / (double) mutations.size();
        double Fr = sumsRRT / (double)  mutations.size() * failrate;

        System.out.println("\r\nDimension = " + dimension + "   failure-rate = " + failrate);
        System.out.println("\tThe average of Fm =: " + Fm + "  Fart/Frt= " + Fr);


        try {
//            System.out.println(filePath);
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
