package top.hugo.db.handler;


import lombok.Data;

import java.util.Date;

@Data
public class FillEntity {
    private Date createTime;
    private Date updateTime;
    private Date createBy;
    private Date updateBy;
}
