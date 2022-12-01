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
 * SBS ¥˙¬Î µœ÷
 * ¬€Œƒ£∫2004-ASIAN-Adaptive random testing
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
        while(total.size()<pointNum){ // ÀÊª˙…˙≥…n∏ˆ∫Ú—°µƒ≤‚ ‘”√¿˝
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
        long sums = 0;// ≥ı ºªØ π”√µƒ≤‚ ‘”√¿˝ ˝
        int temp = 0;// ≥ı ºªØ≤‚ ‘”√¿˝¬‰‘⁄ ß–ß”Úµƒ π”√µƒ≤‚ ‘”√¿˝µƒ∏ˆ ˝

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
            System.out.println("µ⁄" + i + "¥Œ ‘—ÈF_Measure£∫" + temp);
            sums += temp;
        }

        System.out.println("PBS_blockµ±«∞≤Œ ˝£∫dimension = " + dimension +"   lp = " + p +"   failure-rate = " + failrate); // ‰≥ˆµ±«∞≤Œ ˝–≈œ¢
        System.out.println("Fm: " + sums / (double) times + "  «“◊Ó∫ÛµƒFart/Frt: " + sums / (double) times * failrate);// ∆Ωæ˘√ø¥Œ π”√µƒ≤‚ ‘”√¿˝ ˝

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
 * SBS ‰ª£Á†ÅÂÆûÁé∞
 * ËÆ∫ÊñáÔºö2004-ASIAN-Adaptive random testing
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
        while(total.size()<pointNum){ // ÈöèÊú∫ÁîüÊàên‰∏™ÂÄôÈÄâÁöÑÊµãËØïÁî®‰æã
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
        long sums = 0;// ÂàùÂßãÂåñ‰ΩøÁî®ÁöÑÊµãËØïÁî®‰æãÊï∞
        int temp = 0;// ÂàùÂßãÂåñÊµãËØïÁî®‰æãËêΩÂú®Â§±ÊïàÂüüÁöÑ‰ΩøÁî®ÁöÑÊµãËØïÁî®‰æãÁöÑ‰∏™Êï∞

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
            System.out.println("Á¨¨" + i + "Ê¨°ËØïÈ™åF_MeasureÔºö" + temp);
            sums += temp;
        }

        System.out.println("PBS_blockÂΩìÂâçÂèÇÊï∞Ôºödimension = " + dimension +"   lp = " + p +"   failure-rate = " + failrate); //ËæìÂá∫ÂΩìÂâçÂèÇÊï∞‰ø°ÊÅØ
        System.out.println("Fm: " + sums / (double) times + "  ‰∏îÊúÄÂêéÁöÑFart/Frt: " + sums / (double) times * failrate);// Âπ≥ÂùáÊØèÊ¨°‰ΩøÁî®ÁöÑÊµãËØïÁî®‰æãÊï∞

    }
}
>>>>>>> 83f0b02ae98b424e1a960656e012bca6d8d42b5f
