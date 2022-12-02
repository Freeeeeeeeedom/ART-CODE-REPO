package realZone;

import dt.original.Triangle2;
import dt.original.Variance;
import faultZone.FaultZone;
import model.Testcase;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

public class realZone_Variance extends FaultZone {
    Variance correct = new Variance();
    Variance mutation;

    Method method;
    Class<?>[] paramters;
    int pcount = 0;
    public realZone_Variance(Constructor constructor, Method m) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        this.mutation = (Variance) constructor.newInstance();
        pcount = m.getParameterCount();
        paramters = m.getParameterTypes();
        method = m;
    }
    @Override
    public Boolean isCorrect(Testcase testcase) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        int x = (int) testcase.getValue(0);
        int y = (int) testcase.getValue(1);
        int[] arr = new int[]{x,y};
//        System.out.println();
//        System.out.println("x = " + x + " y = " + y);
//        return false;
        try {
            double a = correct.exe(arr.clone());
            double b = mutation.exe(arr.clone());
            if(a==b) return true;
        }
        catch (Exception e){
            return false;
        }
        return false;
    }
}
