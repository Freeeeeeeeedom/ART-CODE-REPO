package hybrid;

import faultZone.FaultZone;
import faultZone.FaultZone_Point_Square;
import model.AbstractART;
import model.DomainBoundary;
import model.Parameters;
import model.Testcase;
import sbs.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**

 2009-A Novel Evolutionary Approach for Adaptive Random Testing

 SBS & QRS结合

 SBS : SA

 QRS : Sobol generate random testcases

 author: yhj
 */
public class EAR_art extends AbstractART {
    public DomainBoundary inputBoundary;

    public double p;

    List<List<Testcase>> PT = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> C = new ArrayList<>(); //Generator Matrix
    public ArrayList<Integer> C_Size = new ArrayList<>();

    static sbs.Evolution evolution;

    int PT_size = 5;

    public EAR_art(DomainBoundary inputBoundary, Double p) throws IOException {
        setC();
        this.inputBoundary = inputBoundary;
        this.p = p;
    }

    //read file to init C
    public void setC() throws IOException {
        String path = "ARTEmpirical-main/new-joe-kuo-7.21201";
        File file = new File(path);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        line = br.readLine();
        while((line = br.readLine()) != null){
            String[] temp_nums = line.split(" ");
            ArrayList<Integer> nums = new ArrayList<>();

            for(String num : temp_nums)
                if(num.length() > 0) nums.add(Integer.parseInt(num));

            ArrayList<Integer> temp = new ArrayList<>();
            for(int i = 3; i < nums.size(); i++){
                temp.add(nums.get(i));
            }
            C.add(temp);
            C_Size.add(nums.get(1));
        }
    }

    public EAR_art(){}

    @Override
    public Testcase Best_candidate() {
        this.Candidate.clear();

        //Generate Testcases from Sobol Sequence
        generateSobol(10);

        //SBS.GA
        List<Testcase> SelectedT;

        GeneticAlgorithm ga = new GeneticAlgorithm();
        PT = ga.PT_generate(PT_size,Candidate);
        SelectedT = new ArrayList<>(ga.evolution_ear(PT,this));


        //right searc
        return SelectedT.get(0);
    }

    //from QRS_Sobol_art
    public void generateSobol(int num){
        ArrayList Candidate = new ArrayList<>();
        for (int j = 0; j < num; j++) {
            Testcase dimlist = new Testcase();

            int dimension =  new Random().nextInt(21200);
            int sobolNum = new Random().nextInt((int)Math.pow(2, C_Size.get(dimension)));

            double halton = Sobol(sobolNum, dimension);
            dimlist.addValue(inputBoundary.getDimension(0).getMin() + inputBoundary.getDimension(0).getRange() * halton);

            sobolNum = new Random().nextInt((int)Math.pow(2, C_Size.get(dimension)));
            halton = Sobol(sobolNum, dimension);
            dimlist.addValue(inputBoundary.getDimension(1).getMin() + inputBoundary.getDimension(1).getRange() * halton);
            Candidate.add(dimlist);
        }
        this.Candidate = Candidate;
    }

    private double Sobol(int i, int Dimension)
    {
        int r = i;
        int k;
        // 将i依次右移，提取2进制里的每一位
        for (k = 0; i > 0; i >>= 1, k++)
            if ((i & 1) == 1) // 若当前位为1，则用异或和矩阵相乘
                r ^= C.get(Dimension).get(k);
        return r / (double) (1 << k); // 除以2^M,移到小数点右边
    }

    //from SBS_art.Fitness
    public double Fitness(List<Testcase> T){
        double Mindist = Double.MAX_VALUE;
        if(T.size()==1) return Double.MAX_VALUE;
        for(int i=0;i<T.size();i++){
            for(int j=i+1;j<T.size();j++){
                double distance = Testcase.Distance(T.get(i),T.get(j),p);
                if(distance < Mindist) Mindist = distance;
            }
        }
        return Mindist;
    }

    @Override
    public void testEfficiency(int pointNum) {
        Testcase testcase = new Testcase(inputBoundary);
        while(total.size() < pointNum){ // 随机生成n个候选的测试用例
            total.add(testcase);
            Candidate = new ArrayList<Testcase>();
            for (int i = 0; i < 10; i++) {
                Candidate.add(new Testcase(inputBoundary));
            }
            testcase = Best_candidate();
        }
    }

    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        int times = 3000;
        long sums = 0;
        int temp = 0;

        ArrayList<Integer> result = new ArrayList<>();

        double p = Parameters.lp;
        double failrate = 0.005;
        int dimension = 2;
        DomainBoundary bd = new DomainBoundary(dimension,-5000,5000);

        evolution = new GeneticAlgorithm();

        for (int i = 1; i <= times; i++) {
            FaultZone fz = new FaultZone_Point_Square(bd, failrate);
            EAR_art ear_block = new EAR_art(bd, p);

            temp = ear_block.run(fz);
            result.add(temp);
            System.out.println("第" + i + "次实验F_Measure：" + temp);
            sums += temp;
        }

        System.out.println("PBS_block各项参数dimension = " + dimension +"   lp = " + p +"   failure-rate = " + failrate);
        System.out.println("Fm: " + sums / (double) times + "  Fart/Frt: " + sums / (double) times * failrate);

    }
}