package model;

import faultZone.FaultZone;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class AbstractART {
    public ArrayList<Testcase> total = new ArrayList<>();
    public ArrayList<Testcase> Candidate = new ArrayList<>();
    int count=0;


    public int run(FaultZone fz) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Testcase testcase = null;
        do{
            testcase = Best_candidate();
            count++;
            total.add(testcase);
        } while (fz.isCorrect(testcase));
        return count;
    }

    public abstract Testcase Best_candidate();

    public abstract void testEfficiency(int pointNum);


}



