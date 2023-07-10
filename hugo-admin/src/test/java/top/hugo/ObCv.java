package top.hugo;


import lombok.Data;

@Data
public class ObCv {
    //订单来源：1快照订单库 2车电分离订单库
    private Integer orderSourceType;
    //业务条线 1-换电订单 2-车电分离
    private Integer bizLine;
}
