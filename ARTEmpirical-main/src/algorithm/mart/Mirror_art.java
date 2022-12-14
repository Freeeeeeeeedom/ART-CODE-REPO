package algorithm.mart;

import faultZone.FaultZone;
import faultZone.FaultZone_Point_Square;
import model.AbstractART;
import model.DomainBoundary;
import model.Parameters;
import model.Testcase;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

/**
 * 2019-Random Border Mirror Transform: A diversity based approach to an effective and efficient mirror adaptive random testing
 *
 * basic MART
 *
 * Mirror partitioning scheme: 2 * 1
 * MART mapping functions : reflect
 * Mirror selection order: Halton sequential order
 */
public class Mirror_art extends AbstractART {
    public DomainBoundary inputBoundary;
    public double p;

    public Mirror_art(DomainBoundary inputBoundary, Double p){
        this.inputBoundary = inputBoundary;
        this.p = p;
    }

    @Override
    public Testcase Best_candidate() {
        this.Candidate.clear();
        generateHalton(10); //generate Halton Sequence

        if(total.size()==0){
            return Candidate.get(new Random().nextInt(Candidate.size()));
        }

        //set mirror partition
        for(int i = 0; i < 10; i++){
            Testcase testcase = this.Candidate.get(i);
            Testcase reflectTestcase = new Testcase();
            double testcaseLocation = testcase.getValue(0);
            reflectTestcase.addValue(inputBoundary.getDimension(0).getMax() - testcaseLocation);
            testcaseLocation = testcase.getValue(1);
            reflectTestcase.addValue(testcaseLocation);
            this.Candidate.add(reflectTestcase);
        }

        //select by FSCS
        int i = 0, cixu = -1;
        double mindist, maxmin = 0;

        if(new Random().nextInt() > 0.5) i = 1;

        for (int j = i * Candidate.size() / 2; j < this.Candidate.size(); j++) {
            mindist = Double.MAX_VALUE;
            for (int k = 0; k < this.total.size(); k++) {
                double dist = Testcase.Distance(this.Candidate.get(j), this.total.get(k), this.p);
                if (dist < mindist) {
                    mindist = dist;
                }
            }
            if (maxmin < mindist) {
                maxmin = mindist;
                cixu = j;
            }
        }

        return this.Candidate.get(cixu);
    }


    public static void main(String args[]) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        int times = 3000;
        long sums = 0;// ?????????????????????????????????
        int temp = 0;// ?????????????????????????????????????????????????????????????????????

        ArrayList<Integer> result = new ArrayList<>();

        double p = Parameters.lp;
        double failrate = 0.005;
        int dimension = 2;
        DomainBoundary bd=new DomainBoundary(dimension,-5000,5000);

        for (int i = 1; i <= times; i++) {
            FaultZone fz = new FaultZone_Point_Square(bd, failrate);


            Mirror_art fscs_block = new Mirror_art(bd, p);

            temp = fscs_block.run(fz);
            result.add(temp);
            System.out.println("???" + i + "?????????F_Measure???" + temp);
            sums += temp;
        }

        System.out.println("FSCS_block???????????????dimension = " + dimension +"   lp = " + p +"   failure-rate = " + failrate); //????????????????????????
        System.out.println("Fm: " + sums / (double) times + "  ????????????Fart/Frt: "
                + sums / (double) times * failrate);// ????????????????????????????????????

    }

    @Override
    public void testEfficiency(int pointNum) {
        Testcase testcase = new Testcase(inputBoundary);
        while(total.size() < pointNum){ // ????????????n????????????????????????

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

    /**
     * Generate Halton Num by Reversing Inverse
     * ??????bits???????????????????????????????????????
     * @param Base ?????????demension in Halton()
     * @param i the ith digit in the sequence
     * @return
     */
    double RadicalInverse(int Base, int i)
    {
        double Digit, Radical, Inverse;
        Digit = Radical = 1.0 / (double) Base;
        Inverse = 0.0;
        while(i != 0)
        {
            // i???Base??????i???"Base"???????????????????????????
            // ??????Digit????????????????????????????????????
            Inverse += Digit * (double) (i % Base);
            Digit *= Radical;

            // i??????Base????????????????????????
            i /= Base;
        }
        return Inverse;
    }

    /**
     *
     * @param Dimension the Dimension th prime num
     * @param Index
     * @return a double generated by Halton Sequence
     */
    double Halton(int Dimension, int Index)
    {
        //the Dimension th prime num
        return RadicalInverse(Dimension, Index);
    }
}
