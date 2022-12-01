package SBS;

import model.Testcase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm implements Evolution{

    int size = 4;
    double standard = 2000.00;
    public List<Testcase> cross(List<Testcase> T1, List<Testcase> L2){
        L2.forEach(T1::remove);
        return T1;
    }

    public List<Testcase> mutation(List<Testcase> T){
        T.forEach(t->{
            if(new Random().nextDouble()<0.1){
                t.setValue(0,-t.getValue(1));
                t.setValue(1,t.getValue(0));
            }
        });
        return T;
    }

    public List<List<Testcase>> PT_generate(int PT_size,List<Testcase> candidates){
        List<List<Testcase>> PT = new ArrayList<>();
        for(int i=0;i<PT_size;i++) PT.add(new ArrayList<>());
        for(int i=0;i<PT_size;i++) PT.get(i).add(candidates.get(i));
        for(int i = PT_size;i<candidates.size();i++){
            PT.get(i % PT_size).add(candidates.get(i));
        }
        return PT;
    }
    @Override
    public List<Testcase> evolution(List<List<Testcase>> PT, SBS_art sbs) {
        List<List<Testcase>> list = new ArrayList<>();
        PT.forEach(p->{
            if(sbs.Fitness(p) > standard) list.add(p);
        });
        if(list.size()==0){
            standard /= 2;
            return evolution(PT,sbs);
        }
        List<Testcase> selectedDomain = list.get(0);
        for(int i=1;i<list.size();i++){
            selectedDomain = cross(selectedDomain,list.get(i));
            selectedDomain = mutation(selectedDomain);
        }
        return selectedDomain;
    }

}
