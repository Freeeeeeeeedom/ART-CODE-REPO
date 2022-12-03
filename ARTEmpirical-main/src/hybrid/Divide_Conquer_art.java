package hybrid;

import faultZone.FaultZone;
import faultZone.FaultZone_Block;
import faultZone.FaultZone_Point_Square;
import fscs.FSCS_art;
import model.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

/**
 * STFCS + PBS
 * 2013-The ART of Divide and Conquer
 *
 * author : yhj
 */
public class Divide_Conquer_art extends AbstractART {
    public DomainBoundary inputBoundary;

    public double p;
    public int lambda; //threshold

    public int dn = 2; //dimension

    //From left to right , up to down
    private ArrayList<ArrayList<Testcase>> SubDomain = new ArrayList<>();

    public Divide_Conquer_art(DomainBoundary inputBoundary, Double p){
        this.inputBoundary = inputBoundary;
        this.p = p;
        this.lambda = 10; //set the threshold,choose FCSC or PBS
        //init the SubDomain
        for(int i = 0; i < dn * dn; i++){
            ArrayList<Testcase> tmp = new ArrayList<>();
            SubDomain.add(tmp);
        }
    }

    @Override
    public Testcase Best_candidate() {
        this.Candidate.clear();

        //STFCS, From FSCS_art
        if(this.total.size() < lambda){
            this.Candidate = Testcase.generateCandates(10, inputBoundary.getList());
            Testcase p = null;
            double mindist, maxmin = 0;
            int cixu = -1;
            if(total.size()==0){
                return Candidate.get(new Random().nextInt(Candidate.size()));
            }
            for (int i = 0; i < this.Candidate.size(); i++) {
                mindist = Double.MAX_VALUE;
                for (int j = 0; j < this.total.size(); j++) {
                    double dist = Testcase.Distance(this.Candidate.get(i), this.total.get(j), this.p);
                    if (dist < mindist) {
                        mindist = dist;
                    }
                }
                if (maxmin < mindist) {
                    maxmin = mindist;
                    cixu = i;
                }
            }

            Testcase test = this.Candidate.get(cixu);

            int row , column;
            if(test.getValue(0) < (inputBoundary.getDimension(0).getMin() + inputBoundary.getDimension(0).getMax()) / 2)  row = 0;
            else row = 1;
            if(test.getValue(1) < (inputBoundary.getDimension(1).getMin() + inputBoundary.getDimension(1).getMax()) / 2) column = 0;
            else column = 1;

            SubDomain.get(row * dn + column).add(test);


            return test;
        }

        //PBS & TSFCS
        int min = SubDomain.get(0).size();
        int subDomain = 0;
        for(int i = 1; i < SubDomain.size(); i++){
            if(SubDomain.get(i).size() < min){
                min = SubDomain.get(i).size();
                subDomain = i;
            }
        }
        int row = subDomain / dn;
        int column = subDomain % dn;

        Testcase testcase = new Testcase();
        int range = (int)(inputBoundary.getDimension(0).getRange() / dn);
        testcase.addValue(row * range + new Random().nextInt(range) + inputBoundary.getDimension(0).getMin());
        testcase.addValue(column * range + new Random().nextInt(range) + inputBoundary.getDimension(1).getMin());

        SubDomain.get(subDomain).add(testcase);
        return testcase;
    }


    public static void main(String args[]) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        int times = 3000;
        long sums = 0;// 初始化使用的测试用例数
        int temp = 0;// 初始化测试用例落在失效域的使用的测试用例的个数

        ArrayList<Integer> result = new ArrayList<>();

        double p = Parameters.lp;
        double failrate = 0.005;
        int dimension = 2;
        DomainBoundary bd=new DomainBoundary(dimension,-5000,5000);

        for (int i = 1; i <= times; i++) {
            FaultZone fz=new FaultZone_Point_Square(bd, failrate);
            Divide_Conquer_art divide_conquer_art = new Divide_Conquer_art(bd, p);

            temp=divide_conquer_art.run(fz);
            result.add(temp);
            System.out.println("第" + i + "次试验F_Measure：" + temp);
            sums += temp;
        }

        System.out.println("FSCS_block当前参数：dimension = " + dimension +"   lp = " + p +"   failure-rate = " + failrate); //输出当前参数信息
        System.out.println("Fm: " + sums / (double) times + "  且最后的Fart/Frt: "
                + sums / (double) times * failrate);// 平均每次使用的测试用例数

    }

    //from FSCS_art.testEfficiency
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
}
