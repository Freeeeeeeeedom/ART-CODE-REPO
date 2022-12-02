package realZone;

import dt.original.BubbleSort;
import dt.original.Encoder;
import faultZone.FaultZone;
import model.Testcase;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Random;

public class realZone_Encoder extends FaultZone {
    Encoder correct = new Encoder();
    Encoder mutation;
    Method method;
    Class<?>[] paramters;
    int pcount = 0;


    public realZone_Encoder(Constructor constructor, Method m) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        this.mutation = (Encoder) constructor.newInstance();
        pcount = m.getParameterCount();
        paramters = m.getParameterTypes();
        method = m;
    }

    @Override
    public Boolean isCorrect(Testcase testcase) throws IllegalArgumentException {
        byte x = (byte) testcase.getValue(0);
        byte y = (byte) testcase.getValue(1);
//        System.out.println();
//        System.out.println("x = " + x + " y = " + y);
//        return false;
        int length = new Random().nextInt(100) + 2;
        byte[] arr = new byte[length];
        arr[0] = x;
        arr[1] = y;
        for(int i=0;i<length;i++){
            arr[i] = (byte) (x * y * (1 + new Random().nextInt(10)));
        }
        try {
            byte[] a = arr.clone();
            byte[] b = arr.clone();
            correct.exe(a);
            mutation.exe(b);
            if(Arrays.equals(a, b)) return true;
        }
        catch (Exception e){
            return false;
        }
        return false;
    }
}