package top.hugo.demo.factory;

public class WorkComputer implements IComputer {
    @Override
    public void work() {
        System.out.println("这是一体机正在办公");
    }
}
