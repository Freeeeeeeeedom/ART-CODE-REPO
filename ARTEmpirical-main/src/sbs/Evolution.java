package sbs;

import model.Testcase;

import java.util.List;

public interface Evolution {
    //演化函数
    public List<Testcase> evolution(List<List<Testcase>> PT,SBS_art sbs);
}
