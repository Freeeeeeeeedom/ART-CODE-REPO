package realZone;

import dt.original.Fisher;
import faultZone.FaultZone;
import model.Testcase;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class realZone_Fisher extends FaultZone {
    Fisher correct = new Fisher();
    Fisher mutation;

    Method method;
    Class<?>[] paramters;
    int pcount = 0;

    public realZone_Fisher(Constructor constructor, Method m) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        this.mutation = (Fisher) constructor.newInstance();
        pcount = m.getParameterCount();
        paramters = m.getParameterTypes();
        method = m;
    }

    @Override
    public Boolean isCorrect(Testcase testcase) throws IllegalArgumentException {
        int x = (int) testcase.getValue(0);
        int y = (int)testcase.getValue(1);
        double z = x * y / 100.0;
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
