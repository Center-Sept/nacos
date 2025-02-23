/*
 * Copyright 1999-2023 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.nacos.naming.paramcheck;

import com.alibaba.nacos.api.common.Constants;
import com.alibaba.nacos.common.paramcheck.ParamCheckUtils;
import com.alibaba.nacos.common.paramcheck.ParamInfo;
import com.alibaba.nacos.common.utils.HttpMethod;
import com.alibaba.nacos.common.utils.StringUtils;
import com.alibaba.nacos.core.paramcheck.AbstractHttpParamExtractor;
import com.alibaba.nacos.naming.misc.UtilsAndCommons;

import javax.servlet.http.HttpServletRequest;

/**
 * Naming instance list http param extractor.
 *
 * @author zhuoguang
 */
public class NamingInstanceListHttpParamExtractor extends AbstractHttpParamExtractor {
    
    @Override
    public void init() {
        addTargetRequest(UtilsAndCommons.NACOS_NAMING_CONTEXT + UtilsAndCommons.NACOS_NAMING_INSTANCE_CONTEXT + "/list",
                HttpMethod.GET);
    }
    
    @Override
    public void extractParamAndCheck(HttpServletRequest request) throws Exception {
        ParamInfo paramInfo = new ParamInfo();
        String serviceName = request.getParameter("serviceName");
        String groupName = request.getParameter("groupName");
        String groupServiceName = serviceName;
        if (StringUtils.isNotBlank(groupServiceName) && groupServiceName.contains(Constants.SERVICE_INFO_SPLITER)) {
            String[] splits = groupServiceName.split(Constants.SERVICE_INFO_SPLITER, 2);
            groupName = splits[0];
            serviceName = splits[1];
        }
        paramInfo.setServiceName(serviceName);
        paramInfo.setGroup(groupName);
        paramInfo.setNamespaceId(request.getParameter("namespaceId"));
        String clusters = request.getParameter(request.getParameter("clusters"));
        if (StringUtils.isNotBlank(clusters)) {
            String[] cluster = clusters.split(",");
            for (String clusterName : cluster) {
                ParamCheckUtils.checkClusterFormat(clusterName);
            }
        }
        ParamCheckUtils.checkParamInfoFormat(paramInfo);
    }
}
