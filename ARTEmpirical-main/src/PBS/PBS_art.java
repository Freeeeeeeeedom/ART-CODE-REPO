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

    SubDomainSelection selectionStrategy = new MaximumSize();

    public List<List<Testcase>> SubDomains = new ArrayList<>();
    int partitions = 10;
    Double p;

    public PBS_art(DomainBoundary inputBoundary, Double p) {
        this.inputBoundary = inputBoundary;
        this.p=p;
        selectionStrategy.SetTotal(total);
    }

    public PBS_art(){

    }
    public void SetStrategy(SubDomainSelection s){
        this.selectionStrategy = s;
        selectionStrategy.SetTotal(total);
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
        List<Testcase> selectedDomain  = selectionStrategy.select(SubDomains);

        return selectedDomain.get(new Random().nextInt(selectedDomain.size()));

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
            pbs_block.SetStrategy(new FewestPreviouslyGenerated());
            temp = pbs_block.run(fz);
            result.add(temp);
            System.out.println("第" + i + "次试验F_Measure：" + temp);
            sums += temp;
        }

        System.out.println("PBS_block当前参数：dimension = " + dimension +"   lp = " + p +"   failure-rate = " + failrate); //输出当前参数信息
        System.out.println("Fm: " + sums / (double) times + "  且最后的Fart/Frt: " + sums / (double) times * failrate);// 平均每次使用的测试用例数

    }
}
