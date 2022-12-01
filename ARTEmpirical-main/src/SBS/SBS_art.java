<<<<<<< HEAD
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
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * SBS 代码实现
 * 论文：2004-ASIAN-Adaptive random testing
 */

public class SBS_art extends AbstractART {
    public DomainBoundary inputBoundary = new DomainBoundary();
    int count = 1;

    List<List<Testcase>> PT = new ArrayList<>();
    static Evolution evolution;

    int PT_size = 5;

    int partitions = 10;
    Double p;

    public SBS_art(DomainBoundary inputBoundary, Double p) {
        this.inputBoundary = inputBoundary;
        this.p=p;
    }

    public SBS_art(){

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

        List<Testcase> SelectedT = new ArrayList<>();

        if(evolution instanceof HillClimbing hc){
            PT =  hc.PT_generate(PT_size,Candidate);
            SelectedT = new ArrayList<>(hc.evolution(PT,this));
        }
        else if(evolution instanceof  SimulatedAnnealing sa){
            PT = sa.PT_generate(PT_size,Candidate);
            SelectedT = new ArrayList<>(sa.evolution(PT,this));
        }
        else if(evolution instanceof GeneticAlgorithm ga){
            PT = ga.PT_generate(PT_size,Candidate);
            SelectedT = new ArrayList<>(ga.evolution(PT,this));
        }
        else if(evolution instanceof SimulatedRepulsion sr){
            PT = sr.PT_generate(PT_size,Candidate);
            SelectedT = new ArrayList<>(sr.evolution(PT,this));
        }
        //right searc
        return SelectedT.get(0);
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

        evolution = new GeneticAlgorithm();

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
=======
package sbs;

import faultZone.FaultZone;
import faultZone.FaultZone_Point_Square;
import model.AbstractART;
import model.DomainBoundary;
import model.Parameters;
import model.Testcase;

import java.util.ArrayList;
import java.util.List;
<<<<<<< HEAD
import java.util.Set;
=======
import java.util.Random;
<<<<<<< HEAD
=======
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
>>>>>>> 560116392d8292a437d173d0af7ce9750de515a6
>>>>>>> 75d1503c45c799dc1f4ed0ab994d0d96c28e3f56

/**
 * SBS 浠ｇ爜瀹炵幇
 * 璁烘枃锛�2004-ASIAN-Adaptive random testing
 */

public class SBS_art extends AbstractART {
    public DomainBoundary inputBoundary = new DomainBoundary();
    int count = 1;

    List<List<Testcase>> PT = new ArrayList<>();
    static Evolution evolution;

    int PT_size = 5;

    int partitions = 10;
    Double p;

    public SBS_art(DomainBoundary inputBoundary, Double p) {
        this.inputBoundary = inputBoundary;
        this.p=p;
    }

    public SBS_art(){

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

        List<Testcase> SelectedT = new ArrayList<>();

        if(evolution instanceof HillClimbing hc){
            PT =  hc.PT_generate(PT_size,Candidate);
            SelectedT = new ArrayList<>(hc.evolution(PT,this));
        }
        else if(evolution instanceof  SimulatedAnnealing sa){
            PT = sa.PT_generate(PT_size,Candidate);
            SelectedT = new ArrayList<>(sa.evolution(PT,this));
        }
        else if(evolution instanceof GeneticAlgorithm ga){
            PT = ga.PT_generate(PT_size,Candidate);
            SelectedT = new ArrayList<>(ga.evolution(PT,this));
        }
        else if(evolution instanceof SimulatedRepulsion sr){
            PT = sr.PT_generate(PT_size,Candidate);
            SelectedT = new ArrayList<>(sr.evolution(PT,this));
        }
        else if(evolution instanceof LocalSpreding ls){
            SelectedT = new ArrayList<>(ls.init(Candidate,this));
        }
        //right searc
        return SelectedT.get(0);
    }

    @Override
    public void testEfficiency(int pointNum) {
        Testcase testcase = new Testcase(inputBoundary);
        while(total.size()<pointNum){ // 闅忔満鐢熸垚n涓�欓�夌殑娴嬭瘯鐢ㄤ緥
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
        long sums = 0;// 鍒濆鍖栦娇鐢ㄧ殑娴嬭瘯鐢ㄤ緥鏁�
        int temp = 0;// 鍒濆鍖栨祴璇曠敤渚嬭惤鍦ㄥけ鏁堝煙鐨勪娇鐢ㄧ殑娴嬭瘯鐢ㄤ緥鐨勪釜鏁�

        ArrayList<Integer> result = new ArrayList<>();

        double p = Parameters.lp;
        double failrate = 0.005;
        int dimension = 2;
        DomainBoundary bd = new DomainBoundary(dimension,-5000,5000);

        evolution = new LocalSpreding();

        for (int i = 1; i <= times; i++) {
            FaultZone fz = new FaultZone_Point_Square(bd, failrate);
            SBS_art pbs_block = new SBS_art(bd, p);

            temp = pbs_block.run(fz);
            result.add(temp);
            System.out.println("绗�" + i + "娆¤瘯楠孎_Measure锛�" + temp);
            sums += temp;
        }

        System.out.println("PBS_block褰撳墠鍙傛暟锛歞imension = " + dimension +"   lp = " + p +"   failure-rate = " + failrate); //杈撳嚭褰撳墠鍙傛暟淇℃伅
        System.out.println("Fm: " + sums / (double) times + "  涓旀渶鍚庣殑Fart/Frt: " + sums / (double) times * failrate);// 骞冲潎姣忔浣跨敤鐨勬祴璇曠敤渚嬫暟

    }
}
>>>>>>> 83f0b02ae98b424e1a960656e012bca6d8d42b5f
