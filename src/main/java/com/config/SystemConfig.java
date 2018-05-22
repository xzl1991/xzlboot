package com.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xlizy on 2017/7/26.
 *
 * 系统配置
 */
public final class SystemConfig {

    /**
     * 获取静态资源方式
     * (local:本地;cdn:内容分发网络;默认:local)
     * zk节点:ZKNode.CONFIG_NODE_GETRESOURCESWAY
     * */
    @Getter @Setter(AccessLevel.PROTECTED)
    private static String getResourcesWay = "local";

    /**
     * 代扣限制
     * (true:允许代扣;false:禁止代扣)
     * zk节点:ZKNode.CONFIG_NODE_PATH_SUBSTITUTECHARGE
     * */
    @Getter @Setter(AccessLevel.PROTECTED)
    private static boolean substituteCharge = true;

    /**
     * 禁止代扣提示语
     * zk节点:ZKNode.CONFIG_NODE_PATH_FORBIDDENSUBSTITUTECHARGEMSG
     * */
    @Getter @Setter(AccessLevel.PROTECTED)
    private static String forbiddenSubstituteChargeMsg = "系统正在同步数据";

    /**
     * 是否使用redis缓存
     * (true:可用;false:禁用)
     * zk节点:ZKNode.CONFIG_NODE_PATH_USEREDIS
     * */
    @Getter @Setter(AccessLevel.PROTECTED)
    private static boolean useRedis = true;

    /**
     * 二级产品缓存版本
     * uuid
     * zk节点:ZKNode.LOCAL_CACHE_VERSION_FOR_Dictionary
     * */
    @Getter @Setter(AccessLevel.PUBLIC)
    private static String localCacheVersionFor_Dictionary;

    /**
     * 全部催收组缓存版本
     * uuid
     * zk节点:ZKNode.localCacheVersionFor_CollectOrgAll
     * */
    @Getter @Setter(AccessLevel.PUBLIC)
    private static String localCacheVersionFor_CollectOrgAll;

    /**
     * 全部催收员缓存版本
     * uuid
     * zk节点:ZKNode.localCacheVersionFor_CollertorAll
     * */
    @Getter @Setter(AccessLevel.PUBLIC)
    private static String localCacheVersionFor_CollertorAll;

}
