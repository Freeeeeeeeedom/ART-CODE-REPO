package mart;

import com.sun.source.doctree.TextTree;
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
 * Mirror partitioning scheme: 2 * 2
 *
 * Result doesn't match my expectation.
 *
 * author: yhj
 */
public class RBMT_art extends AbstractART {
    public DomainBoundary inputBoundary;
    public double p;

    public ArrayList<Testcase> mps = new ArrayList<>(); //Source Domains
    public ArrayList<Testcase> testBuffer = new ArrayList<>(); //Virtual Domains
    private int testBufferPointer;

    public ArrayList<ArrayList<Double>> boarder_vector = new ArrayList<>();

    public RBMT_art(DomainBoundary inputBoundary, Double p){
        this.inputBoundary = inputBoundary;
        this.p = p;
        this.testBufferPointer = 0;
        for(int i = 0; i < 4; i++){
            ArrayList<Double> tmp_boarder_vector = new ArrayList<>();
            tmp_boarder_vector.add(new Random().nextDouble() * inputBoundary.getDimension(i / 2).getRange() / 2);
            tmp_boarder_vector.add(new Random().nextDouble() * inputBoundary.getDimension(i % 2).getRange() / 2);
            boarder_vector.add(tmp_boarder_vector);
        }

    }

    @Override
    public Testcase Best_candidate() {
        this.Candidate.clear();
        this.Candidate=Testcase.generateCandates(10,inputBoundary.getList());

        if(mps.size() == 0){
            Testcase testcase = this.Candidate.get(new Random().nextInt(10));
            mps.add(testcase);
            return testcase;
        }

        Testcase testcase;
        if(testBuffer.size() == 0){
            testcase = this.mps.get(0);
            testBuffer.add(testcase);
            testcase = new Testcase();
            testcase.addValue(this.mps.get(0).getValue(0));
            testcase.addValue(this.inputBoundary.getDimension(1).getMax() - this.mps.get(0).getValue(1));
            testBuffer.add(testcase);
            testcase = new Testcase();
            testcase.addValue(this.inputBoundary.getDimension(0).getMax() - this.mps.get(0).getValue(0));
            testcase.addValue(this.mps.get(0).getValue(1));
            testBuffer.add(testcase);
            testcase = new Testcase();
            testcase.addValue(this.inputBoundary.getDimension(0).getMax() - this.mps.get(0).getValue(0));
            testcase.addValue(this.inputBoundary.getDimension(1).getMax() - this.mps.get(0).getValue(1));
            testBuffer.add(testcase);
        }

        testcase = new Testcase();
        testcase.addValue(this.testBuffer.get(testBufferPointer).getValue(0) + this.boarder_vector.get(testBufferPointer).get(0));
        testcase.addValue(this.testBuffer.get(testBufferPointer).getValue(1) + this.boarder_vector.get(testBufferPointer).get(1));
        this.testBufferPointer++;

        if(testBufferPointer == 4){
            this.mps.clear();
            this.testBufferPointer = 0;
            this.testBuffer.clear();
        }
        return testcase;
    }


    public static void main(String args[]) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        int times = 3000;
        long sums = 0;// 初始化使用的测试用例数
        int temp = 0;// 初始化测试用例落在失效域的使用的测试用例的个数

        ArrayList<Integer> result = new ArrayList<>();

        double p = Parameters.lp;
        double failrate = 0.005;
        int dimension = 2;
        DomainBoundary bd=new DomainBoundary(dimension,-5000,5000);

        for (int i = 1; i <= times; i++) {
            FaultZone fz = new FaultZone_Point_Square(bd, failrate);


            RBMT_art fscs_block = new RBMT_art(bd, p);

            temp = fscs_block.run(fz);
            result.add(temp);
            System.out.println("第" + i + "次试验F_Measure：" + temp);
            sums += temp;
        }

        System.out.println("FSCS_block当前参数：dimension = " + dimension +"   lp = " + p +"   failure-rate = " + failrate); //输出当前参数信息
        System.out.println("Fm: " + sums / (double) times + "  且最后的Fart/Frt: "
                + sums / (double) times * failrate);// 平均每次使用的测试用例数

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
}
