package SBS;

import model.Testcase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulatedRepulsion implements Evolution{
    double Q = new Random().nextDouble(100.00);
    int ps = 5;//组数
    public List<List<Testcase>> PT_generate(int PT_size,List<Testcase> candidates){
        List<List<Testcase>> PT = new ArrayList<>();
        for(int i=0;i<PT_size;i++) PT.add(new ArrayList<>());
        for(int i=0;i<PT_size;i++) PT.get(i).add(candidates.get(i));
        for(int i = PT_size;i<candidates.size();i++){
            PT.get(i % PT_size).add(candidates.get(i));
        }
        return PT;
    }

    public List<Testcase> move(List<Testcase> Ti){
        for(int i=0; i < Ti.size(); i++){
            Testcase t = Ti.get(i);
            t.setValue(0,t.getValue(0) + rf(t,Ti)/Ti.size());
            t.setValue(1,t.getValue(1) + rf(t,Ti)/Ti.size());
        }
        return Ti;
    }

    public double rf(Testcase t,List<Testcase> T) {
        double ans = 0.0;
        for (Testcase testcase : T) {
            if (testcase != t) {
                ans += (Q * Q) / (Testcase.Distance(t, testcase) * Testcase.Distance(t, testcase));
            }
        }
        return ans;
    }
    @Override
    public List<Testcase> evolution(List<List<Testcase>> PT, SBS_art sbs) {
        List<Testcase> selectedDomain = new ArrayList<>();

        List<List<Testcase>> list = new ArrayList<>();
        while(list.size() < ps){
            List<Testcase> t = PT.get(new Random().nextInt(PT.size()));
            if(!list.contains(t)){
                list.add(t);
            }
        }
        for(int i=0;i<ps;i++) list.set(i,move(list.get(i)));
        selectedDomain = list.get(new Random().nextInt(list.size()));
        return selectedDomain;
    }
}
