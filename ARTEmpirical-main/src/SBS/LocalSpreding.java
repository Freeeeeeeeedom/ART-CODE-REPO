package SBS;

import model.Testcase;

import java.util.List;

public class LocalSpreding implements Evolution{
    SBS_art art;
    public boolean move(List<Testcase> T, Testcase t){
        Testcase first = null;
        double df = 0.0;
        double ds = 0.0;
        double MinDist = Double.MAX_VALUE;
        Testcase second = null;
        for (Testcase value : T) {
            if (value != t) {
                double dis = Testcase.Distance(value, t);
                if (dis < MinDist) {
                    first = value;
                    MinDist = dis;
                }
            }
        }
        df = MinDist;
        MinDist = Double.MAX_VALUE;
        for (Testcase testcase : T) {
            if (testcase != t && testcase != first) {
                double dis = Testcase.Distance(testcase, t);
                if (dis < MinDist) {
                    second = testcase;
                    MinDist = dis;
                }
            }
        }
        ds = MinDist;
        if(first==null || second==null) return false;
        if(Math.abs(ds - df) < 30) return false;//由于实数域的稠密性，我们难以真正模拟，所以添加了界定条件来帮助终止程序
        for(int i=0;i < t.size();i++){
            t.setValue(i,t.getValue(i)+ first.getValue(i) - second.getValue(i));
        }
        return true;
    }
    public List<Testcase> init(List<Testcase> PT,SBS_art sbs){
        this.art = sbs;
        for(int i=0;;i++){
            int idx = i%PT.size();
            idx = (idx+PT.size())%PT.size();
            boolean flag = move(PT,PT.get(idx));
            if(!flag) break;
        }
        return PT;
    }
    @Override
    public List<Testcase> evolution(List<List<Testcase>> PT, SBS_art sbs) {
        return null;
    }
}
