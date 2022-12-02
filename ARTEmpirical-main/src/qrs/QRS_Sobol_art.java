package qrs;

import faultZone.FaultZone;
import faultZone.FaultZone_Point_Square;
import model.AbstractART;
import model.DomainBoundary;
import model.Parameters;
import model.Testcase;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

/**
 * QRS 代码 Sobol序列实现
 * 论文：2005-Quasi-Random Testing
 *
 * Quasi-Random-Sequence-Selection component: Sobol
 * Sobol Refered : 2006-COMPUTATIONAL INVESTIGATIONS OF QUASIRANDOM SEQUENCES IN GENERATING TEST CASES FOR SPECIFICATION-BASED TESTS
 *
 * Randomization component : Cranley-Patterson Rotation
 *
 * author: yhj
 */
public class QRS_Sobol_art extends AbstractART {
    public DomainBoundary inputBoundary;

    public double p;

    public ArrayList<ArrayList<Integer>> C = new ArrayList<>(); //Generator Matrix
    public ArrayList<Integer> C_Size = new ArrayList<>();

    public QRS_Sobol_art(DomainBoundary inputBoundary, Double p) throws IOException {
        setC();
        this.inputBoundary = inputBoundary;
        this.p = p;
    }

    public QRS_Sobol_art(){}

    /**
     * C from Sobol Sequence Generator
     * @throws IOException
     */
    public void setC() throws IOException {
        String path = "ARTEmpirical-main/new-joe-kuo-7.21201";
        File file = new File(path);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        line = br.readLine();
        while((line = br.readLine()) != null){
            String[]  temp_nums = line.split(" ");
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

    @Override
    public Testcase Best_candidate() {
        this.Candidate.clear();
        generateSobol(1);

        return this.Candidate.get(0);
    }

    public void generateSobol(int num){
        ArrayList<Testcase> Candidate = new ArrayList<>();
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
        int  k;
        // 将i依次右移，提取2进制里的每一位
        for (k = 0; i > 0; i >>= 1, k++)
            if ((i & 1) == 1) // 若当前位为1，则用异或和矩阵相乘
                r ^= C.get(Dimension).get(k);
        return r / (double) (1 << k); // 除以2^M,移到小数点右边
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
        long sums = 0;// 初始化使用的测试用例数
        int temp = 0;// 初始化测试用例落在失效域的使用的测试用例的个数

        ArrayList<Integer> result = new ArrayList<>();

        double p = Parameters.lp;
        double failrate = 0.005;
        int dimension = 2;
        DomainBoundary bd = new DomainBoundary(dimension,-5000,5000);

        for (int i = 1; i <= times; i++) {
            FaultZone fz = new FaultZone_Point_Square(bd, failrate);
            QRS_Sobol_art pbs_block = new QRS_Sobol_art(bd, p);

            temp = pbs_block.run(fz);
            result.add(temp);
            System.out.println("第" + i + "次试验F_Measure：" + temp);
            sums += temp;
        }

        System.out.println("PBS_block当前参数：dimension = " + dimension +"   lp = " + p +"   failure-rate = " + failrate); //输出当前参数信息
        System.out.println("Fm: " + sums / (double) times + "  且最后的Fart/Frt: " + sums / (double) times * failrate);// 平均每次使用的测试用例数
    }
}
