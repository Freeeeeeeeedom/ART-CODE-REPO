package RRT;

import faultZone.FaultZone;
import faultZone.FaultZone_Point_Square;
import fscs.FSCS_art;
import model.AbstractART;
import model.DomainBoundary;
import model.Parameters;
import model.Testcase;

import java.util.ArrayList;
import java.util.Random;


/**
 * RRT代码实现
 * 论文：2006-DBLP-Restricted Random Testing: Adaptive Random Testing by Exclusion.
 * ORRT通过在检测到的每一个检测到的test case附近创造一个exclusion zone
 */
public class ORRT_art extends AbstractART {
    public DomainBoundary inputBoundary = new DomainBoundary();
    public double targetExclusion = 0;

    /**
     * ideal zone size = (A / (n * Pi)) ^ (1 / 2)
     * A is total_size * targetExclusion
      */
    double exclusionZone = 0;

    double p;

    public ORRT_art(DomainBoundary inputBoundary, double targetExclusion, double p){
        this.inputBoundary = inputBoundary;
        this.targetExclusion = targetExclusion;
        this.p = p;
    }

    /**
     * ORRT selects testcase from testcases out of exclusion zone
     * @return selected testcase
     */
    @Override
    public Testcase Best_candidate() {
        this.Candidate.clear();
        this.Candidate=Testcase.generateCandates(1000,inputBoundary.getList());

        Testcase p = null;

        if(total.size() == 0)
            return Candidate.get(new Random().nextInt(Candidate.size()));

        exclusionZone = Math.pow(inputBoundary.sizeOfInputDomain() * targetExclusion / (total.size() * Math.PI), 0.5);
        for(int i = 0; i < Candidate.size(); i++){
            boolean outExclusion = true;
            p = Candidate.get(i);
            for(int j = 0; j < total.size(); j++){
                double distance = Testcase.Distance(p, total.get(j), this.p);
                if(distance < exclusionZone) {
                    outExclusion = false;
                    break;
                }
            }
            if(outExclusion) break;
        }
        return p;
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
            ORRT_art orrt_block = new ORRT_art(bd, 1.0, p);
            temp=orrt_block.run(fz);
            result.add(temp);
            System.out.println("第" + i + "次试验F_Measure：" + temp);
            sums += temp;
        }

        System.out.println("ORRT_block当前参数：dimension = " + dimension +"   lp = " + p +"   failure-rate = " + failrate); //输出当前参数信息
        System.out.println("Fm: " + sums / (double) times + "  且最后的Fart/Frt: "
                + sums / (double) times * failrate + " Exlusion = ");// 平均每次使用的测试用例数

    }

    @Override
    public void testEfficiency(int pointNum) {

    }
}
