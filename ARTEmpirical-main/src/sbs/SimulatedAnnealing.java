package sbs;

import model.Testcase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulatedAnnealing implements Evolution{
    //随机概率函数
    public double controlledprobility(){
        return new Random().nextDouble();
    }
    //测试用例集合生成
    public List<List<Testcase>> PT_generate(int PT_size,List<Testcase> candidates){
        List<List<Testcase>> PT = new ArrayList<>();
        for(int i=0;i<PT_size;i++) PT.add(new ArrayList<>());
        for(int i=0;i<PT_size;i++) PT.get(i).add(candidates.get(i));
        for(int i = PT_size;i<candidates.size();i++){
            PT.get(i % PT_size).add(candidates.get(i));
        }
        return PT;
    }
    //演化函数
    @Override
    public List<Testcase> evolution(List<List<Testcase>> PT, SBS_art sbs) {
        List<Testcase> selectedDomain;

        int index = new Random().nextInt(PT.size());
        selectedDomain = PT.get(index);
        while(index < PT.size() -1){
            selectedDomain = PT.get(index);
            if(sbs.Fitness(PT.get(index)) < sbs.Fitness(PT.get(index+1))){
                index++;
            }
            else{
                if(controlledprobility() < 0.5)
                {
                    selectedDomain = PT.get(index);
                    break;
                }
                else{
                    index++;
                }
            }
        }
        return selectedDomain;
    }
}
