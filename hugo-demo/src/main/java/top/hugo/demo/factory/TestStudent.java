package top.hugo.demo.factory;

public class TestStudent extends AbstractPerson {
    @Override
    public void getUp() {
        System.out.println(111);
    }

    @Override
    public void eat() {
        System.out.println(111);
    }

    @Override
    public void dressing() {
        System.out.println(111);
    }
}
