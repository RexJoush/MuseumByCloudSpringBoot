package com.nwu.service.impl;

/**
 * @author Rex Joush
 * @time 2021.04.07
 */

import com.nwu.service.CommonService;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.kubernetes.client.util.Yaml;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 公共方法 service 接口实现类
 */
@Service
public class CommonServiceImpl implements CommonService {

    @Override
    public int changeResourceByYaml(File yaml) {
        try {
            InputStream inputStream = new FileInputStream(yaml);
            List<HasMetadata> orReplace = KubernetesUtils.client.load(inputStream).createOrReplace();
            yaml.delete();
            if (orReplace != null) {
                return 1200;
            } else {
                return 1201;
            }
        } catch (Exception e) {
            return 1202;
        }
    }
}
