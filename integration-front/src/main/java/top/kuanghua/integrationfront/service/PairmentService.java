package top.kuanghua.integrationfront.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.kuanghua.commonpom.entity.SelfCommonParams;
import top.kuanghua.integrationfront.entity.Pairment;
import top.kuanghua.integrationfront.entity.Repair;
import top.kuanghua.integrationfront.entity.Replacement;
import top.kuanghua.integrationfront.mapper.PairmentMapper;
import top.kuanghua.integrationfront.mapper.RepairMapper;
import top.kuanghua.integrationfront.mapper.ReplacementMapper;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 多表中实体类的注释Service
 *
 * @author 熊猫哥
 * @since 2022-06-25 10:32:12
 */
@Service
public class PairmentService {

    @Resource
    private PairmentMapper pairmentMapper;

    @Resource
    private RepairMapper repairMapper;
    @Resource
    private ReplacementMapper replacementMapper;


    public Page<Map> selectPairment(SelfCommonParams commonParams, Map repairVo) {
        return pairmentMapper.selectPairment(new Page<Map>(commonParams.getPageNum(), commonParams.getPageSize()), repairVo);
    }

    public Map selectByKey(String pairment) {
        return pairmentMapper.selectByKey(pairment);
    }

    @Transactional
    public void insert(Pairment pairment) {
        this.repairMapper.insert(JSON.parseObject(JSON.toJSONString(pairment), Repair.class));
        this.replacementMapper.insert(JSON.parseObject(JSON.toJSONString(pairment), Replacement.class));
    }

    @Transactional
    public void updateByKey(Pairment pairment) {
        //售后维修表
        Repair repair = JSON.parseObject(JSON.toJSONString(pairment), Repair.class);
        QueryWrapper<Repair> repairWrapper = new QueryWrapper<>();
        repairWrapper.eq("sn", pairment.getSn());
        this.repairMapper.update(repair, repairWrapper);
        //换件表
        Replacement replacement = JSON.parseObject(JSON.toJSONString(pairment), Replacement.class);
        QueryWrapper<Replacement> replacementWrapper = new QueryWrapper<>();
        replacementWrapper.eq("sn", pairment.getSn());
        this.replacementMapper.update(replacement, replacementWrapper);
    }

    @Transactional
    public void deleteByKey(String sn) {
        //repair
        QueryWrapper<Repair> repairWrapper = new QueryWrapper<>();
        repairWrapper.eq("sn", sn);
        this.repairMapper.delete(repairWrapper);
        //replacement
        QueryWrapper<Replacement> replacementWrapper = new QueryWrapper<>();
        replacementWrapper.eq("sn", sn);
        this.replacementMapper.delete(replacementWrapper);
    }
}
