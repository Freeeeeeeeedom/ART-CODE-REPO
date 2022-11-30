package sbs;

import faultZone.FaultZone;
import faultZone.FaultZone_Point_Square;
import model.AbstractART;
import model.DomainBoundary;
import model.Parameters;
import model.Testcase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SBS_art extends AbstractART {
    public DomainBoundary inputBoundary = new DomainBoundary();
    int count = 1;

    public List<List<Testcase>>  PT = new ArrayList<>();
    int PT_size = 5;

    int partitions = 10;
    Double p;

    public SBS_art(DomainBoundary inputBoundary, Double p) {
        this.inputBoundary = inputBoundary;
        this.p=p;
    }

    public SBS_art(){

    }

    public void PT_generate(){
        for(int i=0;i<PT_size;i++)PT.add(new ArrayList<Testcase>());
        for(int i=0;i<PT_size;i++) PT.get(i).add(Candidate.get(i));
        for(int i=PT_size;i<Candidate.size();i++){
            PT.get(new Random().nextInt(PT_size)).add(Candidate.get(i));
        }
    }

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
    public Testcase Best_candidate() {
        this.Candidate.clear();
        this.Candidate=Testcase.generateCandates(20,inputBoundary.getList());

        PT_generate();

        int cixu = new Random().nextInt(PT_size);

        //right search
        while(cixu < PT_size - 1){
            if(Fitness(PT.get(cixu)) < Fitness(PT.get(cixu + 1))){
                   cixu++;
            }
            else{
                break;
            }
        }
        return PT.get(cixu).get(new Random().nextInt(PT.get(cixu).size()));
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
            SBS_art pbs_block = new SBS_art(bd, p);

            temp = pbs_block.run(fz);
            result.add(temp);
            System.out.println("第" + i + "次试验F_Measure：" + temp);
            sums += temp;
        }

        System.out.println("PBS_block当前参数：dimension = " + dimension +"   lp = " + p +"   failure-rate = " + failrate); //输出当前参数信息
        System.out.println("Fm: " + sums / (double) times + "  且最后的Fart/Frt: " + sums / (double) times * failrate);// 平均每次使用的测试用例数

    }
}
