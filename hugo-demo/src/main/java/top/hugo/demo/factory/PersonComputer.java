package top.hugo.demo.factory;

public class PersonComputer implements IComputer {
    @Override
    public void work() {
        System.out.println("这是笔记本电脑，正在办公");
    }
}
