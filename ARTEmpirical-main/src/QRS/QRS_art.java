package QRS;

import faultZone.FaultZone;
import faultZone.FaultZone_Point_Square;
import fscs.FSCS_art;
import model.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * QRS代码实现
 * 论文：2005-Quasi-Random Testing
 *
 * Quasi-Random-Sequence-Selection component: Halton or Sobol or Niederreiter
 * Halton Refered : 2006-COMPUTATIONAL INVESTIGATIONS OF QUASIRANDOM SEQUENCES IN GENERATING TEST CASES FOR SPECIFICATION-BASED TESTS
 *
 * Randomization component : Cranley-Patterson Rotation or Owen Scrambling or  Random Shaking and Rotation
 */
public class QRS_art extends AbstractART {
    public DomainBoundary inputBoundary;

    public double p;

    public QRS_art(DomainBoundary inputBoundary, double p){
        this.inputBoundary = inputBoundary;
        this.p = p;
    }

    @Override
    public Testcase Best_candidate() {
        this.Candidate.clear();
        generateHalton(10);

        if(total.size()==0){
            return Candidate.get(new Random().nextInt(Candidate.size()));
        }
        double mindist, maxmin = 0;
        int cixu = -1;
        //Generate new testcase using Halton


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

        return this.Candidate.get(cixu);
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

    private void generateHalton(int num){
        ArrayList<Testcase> Candidate = new ArrayList<>();
        ArrayList<Integer> randomNum = new ArrayList<>();
        for (int j = 0; j < num; j++) {
            Testcase dimlist = new Testcase();
            int base[] = {2, 3};
            int haltonNum = -1;
            while(haltonNum <= 0 || randomNum.contains(haltonNum)){
                haltonNum = new Random().nextInt(100000);
            }

            double halton = Halton(2, haltonNum);
            dimlist.addValue(inputBoundary.getDimension(0).getMin() + inputBoundary.getDimension(0).getRange() * halton);

            halton = Halton(3, haltonNum);
            dimlist.addValue(inputBoundary.getDimension(1).getMin() + inputBoundary.getDimension(1).getRange() * halton);
            Candidate.add(dimlist);
        }
        this.Candidate = Candidate;
    }

    double RadicalInverse(int Base, int i)
    {
        double Digit, Radical, Inverse;
        Digit = Radical = 1.0 / (double) Base;
        Inverse = 0.0;
        while(i != 0)
        {
            // i余Base求出i在"Base"进制下的最低位的数
            // 乘以Digit将这个数镜像到小数点右边
            Inverse += Digit * (double) (i % Base);
            Digit *= Radical;

            // i除以Base即可求右一位的数
            i /= Base;
        }
        return Inverse;
    }

    double Halton(int Dimension, int Index)
    {
        // 直接用第Dimension个质数作为底数调用RadicalInverse即可
        return RadicalInverse(Dimension, Index);
    }

    public static void main(String args[]) {
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
            QRS_art qrs_block= new QRS_art(bd, p);

            temp=qrs_block.run(fz);
            result.add(temp);
            System.out.println("第" + i + "次试验F_Measure：" + temp);
            sums += temp;
        }

        System.out.println("FSCS_block当前参数：dimension = " + dimension +"   lp = " + p +"   failure-rate = " + failrate); //输出当前参数信息
        System.out.println("Fm: " + sums / (double) times + "  且最后的Fart/Frt: "
                + sums / (double) times * failrate);// 平均每次使用的测试用例数


    }
}
