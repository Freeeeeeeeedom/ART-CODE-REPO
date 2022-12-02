package realZone;

import dt.original.Triangle;
import dt.original.Triangle2;
import faultZone.FaultZone;
import model.Testcase;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class realZone_Triangle2 extends FaultZone {
    Triangle2 correct = new Triangle2();
    Triangle2 mutation;

    Method method;
    Class<?>[] paramters;
    int pcount = 0;
    public realZone_Triangle2(Constructor constructor, Method m) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        this.mutation = (Triangle2) constructor.newInstance();
        pcount = m.getParameterCount();
        paramters = m.getParameterTypes();
        method = m;
    }
    @Override
    public Boolean isCorrect(Testcase testcase) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        int x = (int) testcase.getValue(0);
        int y = (int) testcase.getValue(1);
        int z = new Random().nextInt(1000);
//        System.out.println();
//        System.out.println("x = " + x + " y = " + y);
//        return false;

        try {
            double a = correct.exe(x,y,z);
            double b = mutation.exe(x,y,z);
            if(a==b) return true;
        }
        catch (Exception e){
            return false;
        }
        return false;
    }
}
