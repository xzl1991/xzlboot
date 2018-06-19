package com.controller;

import com.dangdang.ddframe.job.lite.lifecycle.internal.operate.JobOperateAPIImpl;
import com.dangdang.ddframe.job.lite.lifecycle.internal.reg.RegistryCenterFactory;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @auther xzl on 18:16 2018/5/25
 */
@Controller
@RequestMapping(value = "/xzl/job")
public class JobController {

    @Resource
    private ZookeeperRegistryCenter regCenter;
    @Value("${elastic-job.regCenter.serverList}")
    private String connectString;
    @Value("${elastic-job.regCenter.namespace}")
    private String namespace;
    @Value("${elastic-job.serviceIp}")
    private String serviceIp;

    @RequestMapping("/status/{name}")
    @ResponseBody
    public String manageJob(@PathVariable String name){
//        @Bean(initMethod = "init")
//        public ZookeeperRegistryCenter regCenter(@Value("${elastic-job.regCenter.serverList}") final String serverList, @Value("${elastic-job.regCenter.namespace}") final String namespace) {
//            return new ZookeeperRegistryCenter(new ZookeeperConfiguration(serverList, namespace));
//        }
        Optional<String> optional = Optional.of("smsTaskJob-dynamic-107535");
//        Optional<String> optional = Optional.of(name);
//        Optional<String> optional1 = Optional.of("10.152.17.85");
        Optional<String> ipOptional ;
        // RegistryCenterFactory.createCoordinatorRegistryCenter(connectString, namespace, optional
        JobOperateAPIImpl jobOperateAPI =  new JobOperateAPIImpl(regCenter);
        String[] ips = serviceIp.split(",");
        for (String ip : ips){
            ipOptional = Optional.of(ip);
            jobOperateAPI.disable(optional,ipOptional);
        }
        return "";
    }
    @RequestMapping("/start/{name}")
    @ResponseBody
    public String startJob(@PathVariable String name){
        //com.gomefinance.ermas.jobs.mobJob.MobJobConfig
//        @Bean(initMethod = "init")
//        public ZookeeperRegistryCenter regCenter(@Value("${elastic-job.regCenter.serverList}") final String serverList, @Value("${elastic-job.regCenter.namespace}") final String namespace) {
//            return new ZookeeperRegistryCenter(new ZookeeperConfiguration(serverList, namespace));
//        }
        Optional<String> optional = Optional.of("smsTaskJob-dynamic-107535");
//        Optional<String> optional1 = Optional.of("10.152.17.85");
        Optional<String> ipOptional ;
        // RegistryCenterFactory.createCoordinatorRegistryCenter(connectString, namespace, optional
        JobOperateAPIImpl jobOperateAPI =  new JobOperateAPIImpl(regCenter);
        String[] ips = serviceIp.split(",");
        for (String ip : ips){
            ipOptional = Optional.of(ip);
            jobOperateAPI.enable(optional,ipOptional);
        }
        return "";
    }
}
