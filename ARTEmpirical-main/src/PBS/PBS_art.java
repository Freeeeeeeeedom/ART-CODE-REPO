package PBS;

import faultZone.FaultZone;
import faultZone.FaultZone_Point_Square;
import model.AbstractART;
import model.DomainBoundary;
import model.Parameters;
import model.Testcase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class PBS_art extends AbstractART {
    public DomainBoundary inputBoundary = new DomainBoundary();
    int count = 1;

    SubDomainSelection selectionStrategy = new MaximumSize();
    public List<List<Testcase>> SubDomains = new ArrayList<>();
    int partitions = 10;
    Double p;

    public PBS_art(DomainBoundary inputBoundary, Double p) {
        this.inputBoundary = inputBoundary;
        this.p=p;
    }

    public PBS_art(){

    }

    public void Partitioning(){
        //这里使用随机分区的分区方法
        //将candidates分成随机块
        SubDomains.clear();
        for(int i=0;i<partitions;i++) SubDomains.add(new ArrayList<Testcase>());
        for (Testcase testcase : Candidate) {
            int check = new Random().nextInt(4);
            SubDomains.get(check).add(testcase);
        }
    }


    @Override
    public Testcase Best_candidate() {
        this.Candidate.clear();
        this.Candidate=Testcase.generateCandates(100,inputBoundary.getList());

        //分区
        Partitioning();

        int cixu = -1;
        int cur_size = -1;
        List<Testcase> selectedDomain = SubDomains.get(0);
        if(total.size()==0){
            return Candidate.get(new Random().nextInt(Candidate.size()));
        }

        //采用Maximum size的选取策略
        selectedDomain = selectionStrategy.select(SubDomains);
//        for (List<Testcase> subDomain : SubDomains) {
//            List<Testcase> temp = new ArrayList<>(subDomain);
//            temp.retainAll(total);
//            if (temp.size() == 0 && subDomain.size() > cur_size) {
//                cur_size = subDomain.size();
//                selectedDomain = subDomain;
//            }
//        }

        return selectedDomain.get(new Random().nextInt(cur_size));

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

//        return this.Candidate.get(cixu);
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
