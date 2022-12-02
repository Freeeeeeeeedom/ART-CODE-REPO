package pbs;

import faultZone.FaultZone;
import faultZone.FaultZone_Point_Square;
import model.AbstractART;
import model.DomainBoundary;
import model.Parameters;
import model.Testcase;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * PBS 代码实现
 * 论文：T. Y. Chen, R. G. Merkel, P. K. Wong, and G. Eddy, “Adaptive
 * random testing through dynamic partitioning,” in Proceedings of
 * the 4th International Conference on Quality Software (QSIC’04), 2004,
 * pp. 79–86.
 *
 */

public class PBS_art extends AbstractART {
    public DomainBoundary inputBoundary = new DomainBoundary();
    int count = 1;

        SubDomainSelection selectionStrategy = new FewestPreviouslyGenerated();

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
        //当strategy为Maximumsize时采用随机分区
        //其余采用均匀分区
        //将candidates分成随机块
        if(selectionStrategy instanceof MaximumSize) {
            SubDomains.clear();
            for (int i = 0; i < partitions; i++) SubDomains.add(new ArrayList<Testcase>());
            for (int i = 0; i < partitions; i++) {
                SubDomains.get(i).add(Candidate.get(i));
            }
            for (int i = partitions; i < Candidate.size(); i++) {
                SubDomains.get(new Random().nextInt(partitions)).add(Candidate.get(i));
            }
        }
        else{
            SubDomains.clear();
            for(int i=0;i<partitions;i++) SubDomains.add(new ArrayList<>());
            for(int i=0;i<Candidate.size();i++){
                SubDomains.get(i % partitions).add(Candidate.get(i));
            }
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

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
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

            pbs_block.SetStrategy(new NoTestCaseInTarget());

            temp = pbs_block.run(fz);
            result.add(temp);
            System.out.println("第" + i + "次试验F_Measure：" + temp);
            sums += temp;
        }

        System.out.println("PBS_block当前参数：dimension = " + dimension +"   lp = " + p +"   failure-rate = " + failrate); //输出当前参数信息
        System.out.println("Fm: " + sums / (double) times + "  且最后的Fart/Frt: " + sums / (double) times * failrate);// 平均每次使用的测试用例数

    }
}
