package qrs;

import faultZone.FaultZone;
import faultZone.FaultZone_Point_Square;
import model.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

/**
 * QRS����ʵ��
 * ���ģ�2005-Quasi-Random Testing
 *
 * Quasi-Random-Sequence-Selection component: Halton
 * Halton Refered : 2006-COMPUTATIONAL INVESTIGATIONS OF QUASIRANDOM SEQUENCES IN GENERATING TEST CASES FOR SPECIFICATION-BASED TESTS
 *
 * Randomization component : Cranley-Patterson Rotation
 *
 * author: yhj
 */
public class QRS_Halton_art extends AbstractART {

    public DomainBoundary inputBoundary;

    public double p;

    /**
     * Initialize QRS_Halton_art
     * @param inputBoundary
     * @param p
     */
    public QRS_Halton_art(DomainBoundary inputBoundary, double p){
        this.inputBoundary = inputBoundary;
        this.p = p;
    }

    @Override
    public Testcase Best_candidate() {
        this.Candidate.clear();
        generateHalton(1);

        if(total.size()==0){
            return Candidate.get(new Random().nextInt(Candidate.size()));
        }

        return this.Candidate.get(0);
    }

    @Override
    public void testEfficiency(int pointNum) {
        Testcase testcase = new Testcase(inputBoundary);
        while(total.size() < pointNum){ // �������n����ѡ�Ĳ�������

            total.add(testcase);
            Candidate = new ArrayList<Testcase>();
            for (int i = 0; i < 10; i++) {
                Candidate.add(new Testcase(inputBoundary));
            }

            testcase = Best_candidate();
        }
    }

    /**
     * Generate Test Num using Halton Sequence
     * @param num the num of test cases needed to generate
     */
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
     * ���ڸ�����ʵ�֣�ȷ��bits��Ŀ�㹻
     * @param Base ������demension in Halton()
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
            // i��Base���i��"Base"�����µ����λ����
            // ����Digit�����������С�����ұ�
            Inverse += Digit * (double) (i % Base);
            Digit *= Radical;

            // i����Base��������һλ����
            i /= Base;
        }
        return Inverse;
    }

    /**
     *
     * @param Dimension
     * @param Index
     * @return a double generated by Halton Sequence
     */
    double Halton(int Dimension, int Index)
    {
        // ֱ���õ�Dimension��������Ϊ��������RadicalInverse����
        return RadicalInverse(Dimension, Index);
    }

    public static void main(String args[]) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        int times = 3000;
        long sums = 0;// ��ʼ��ʹ�õĲ���������
        int temp = 0;// ��ʼ��������������ʧЧ���ʹ�õĲ��������ĸ���

        ArrayList<Integer> result = new ArrayList<>();

        double p = Parameters.lp;
        double failrate = 0.005;
        int dimension = 2;
        DomainBoundary bd=new DomainBoundary(dimension,-5000,5000);

        for (int i = 1; i <= times; i++) {
            FaultZone fz=new FaultZone_Point_Square(bd, failrate);
            QRS_Halton_art qrs_block= new QRS_Halton_art(bd, p);

            temp=qrs_block.run(fz);
            result.add(temp);
            System.out.println("第" + i + "次试验F_Measure：" + temp);
            sums += temp;
        }

        System.out.println("ORRT_block当前参数：dimension = " + dimension +"   lp = " + p +"   failure-rate = " + failrate); //输出当前参数信息
        System.out.println("Fm: " + sums / (double) times + "  且最后的Fart/Frt: "
                + sums / (double) times * failrate + " Exlusion = ");// 平均每次使用的测试用例数


    }
}

