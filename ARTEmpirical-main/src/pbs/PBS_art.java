package pbs;

import faultZone.FaultZone;
import faultZone.FaultZone_Point_Square;
import model.AbstractART;
import model.DomainBoundary;
import model.Parameters;
import model.Testcase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PBS_art extends AbstractART {
    public DomainBoundary inputBoundary = new DomainBoundary();
    int count = 1;

    public List<Testcase> left = new ArrayList<>();
    public List<Testcase> right = new ArrayList<>();

    Double p;

    public PBS_art(DomainBoundary inputBoundary, Double p) {
        this.inputBoundary = inputBoundary;
        this.p=p;
    }

    public PBS_art(){

    }

    public void Partitioning(){
        //这里使用二分的分区方法
        left.clear();
        right.clear();

        int flag = new Random().nextInt(Candidate.size());

        for(int i=0;i<flag;i++) left.add(Candidate.get(i));
        for(int i=flag;i<Candidate.size();i++) right.add(Candidate.get(i));
    }
    @Override
    public Testcase Best_candidate() {
        this.Candidate.clear();
        this.Candidate=Testcase.generateCandates(10,inputBoundary.getList());

        Partitioning();

        Testcase p = null;
        double mindist, maxmin = 0;
        int cixu = -1;
        if(total.size()==0){
            return Candidate.get(new Random().nextInt(Candidate.size()));
        }
        if(left.size()>right.size()){
            int index = new Random().nextInt(left.size());
            cixu = index;
        }
        else{
            int index = new Random().nextInt(right.size()) + left.size();
            cixu = index;
        }
//        for (int i = 0; i < this.Candidate.size(); i++) {
//            mindist = Double.MAX_VALUE;
//            for (int j = 0; j < this.total.size(); j++) {
//                double dist = Testcase.Distance(this.Candidate.get(i), this.total.get(j), this.p);
//                if (dist < mindist) {
//                    mindist = dist;
//                }
//            }
//            if (maxmin < mindist) {
//                maxmin = mindist;
//                cixu = i;
//            }
//        }

        return this.Candidate.get(cixu);
    }

    @Override
    public void testEfficiency(int pointNum) {
        Testcase testcase = new Testcase(inputBoundary);
        while(total.size()<pointNum){ // 随机生成n个候选的测试用例
            total.add(testcase);
            Candidate = new ArrayList<Testcase>();
            for (int i = 0; i < 10; i++) {
                Candidate.add(new Testcase(inputBoundary));
            }
            testcase = Best_candidate();
        }
    }

    public static void main(String[] args) {
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
            PBS_art pbs_block = new PBS_art(bd, p);

            temp = pbs_block.run(fz);
            result.add(temp);
            System.out.println("第" + i + "次试验F_Measure：" + temp);
            sums += temp;
        }

        System.out.println("PBS_block当前参数：dimension = " + dimension +"   lp = " + p +"   failure-rate = " + failrate); //输出当前参数信息
        System.out.println("Fm: " + sums / (double) times + "  且最后的Fart/Frt: " + sums / (double) times * failrate);// 平均每次使用的测试用例数

    }
}
