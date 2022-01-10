package top.kuanghua.tyexample.listener;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.xpand.starter.canal.annotation.*;

/**
 * @Title: CanalDataEventListener
 * @Description:
 * @Auther: kuanghua
 * @create 2020/9/1 22:43
 */
@CanalEventListener
public class CanalDataEventListener {
    @InsertListenPoint
    public void onEventInsert(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
            System.out.println("新增" + column.getName() + ":获取的数据" + column.getValue());
        }
    }

    @UpdateListenPoint
    public void onEventUpdate(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
            System.out.println("修改前" + column.getName() + ":获取的数据" + column.getValue());
        }
        for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
            System.out.println("修改后" + column.getName() + ":获取的数据" + column.getValue());
        }
    }

    @DeleteListenPoint
    public void onEventDelete(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
            System.out.println("删除前" + column.getName() + ":获取的数据" + column.getValue());
        }
    }

    //自定义监听
    @ListenPoint(
            eventType = {CanalEntry.EventType.DELETE, CanalEntry.EventType.UPDATE}, //监听类型
            schema = {"changgou_content"},
            table = {"tb_content"}, //指定监控的表
            destination = "example" //指定实例的地址
    )
    public void onEventCustomUpdate(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
            System.out.println("自定义前" + column.getName() + ":获取的数据" + column.getValue());
        }
        for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
            System.out.println("自定义后" + column.getName() + ":获取的数据" + column.getValue());
        }
    }
}
