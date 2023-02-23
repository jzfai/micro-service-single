package top.hugo.demo.factory;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeviceType {

    /**
     * pc端
     */
    PC("pc", "1"),

    /**
     * app端
     */
    APP("app", "2"),

    /**
     * 小程序端
     */
    XCX("xcx", "3");

    private final String device;
    private final String index;
}
