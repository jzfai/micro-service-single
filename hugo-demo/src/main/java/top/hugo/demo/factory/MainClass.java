package top.hugo.demo.factory;

public class MainClass {
    public static void main(String[] args) {
        DeviceType pc = DeviceType.PC;
        System.out.println(CodeStatus.ERROR);
        System.out.println(pc.getDevice());
        System.out.println(pc.getIndex());
    }
}
