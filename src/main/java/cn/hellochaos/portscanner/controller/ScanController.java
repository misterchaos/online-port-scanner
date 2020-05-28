package cn.hellochaos.portscanner.controller;

import cn.hellochaos.portscanner.entity.ScanTask;
import cn.hellochaos.portscanner.entity.dto.ResultBean;
import cn.hellochaos.portscanner.service.PortScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description
 * @date 2020-05-28 22:05
 */
@RestController
@RequestMapping("/port-scanner/api/v1/scan")
public class ScanController {

    @Autowired
    private PortScanService portScanService;


    /**
     * 新增
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResultBean<?> insert(@RequestBody ScanTask scanTask) {
        portScanService.submitScanTask(scanTask);
        return new ResultBean<>();
    }


    /**
     * 根据id查询
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResultBean<?> getById(@PathVariable("id") String id) {
        return new ResultBean<>(portScanService.getScanTask(id));
    }

    /**
     * 查询全部数据
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResultBean<?> list() {
        return new ResultBean<>();
    }


}
