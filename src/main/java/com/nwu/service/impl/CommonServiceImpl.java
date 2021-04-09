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

import java.io.InputStream;
import java.util.List;

/**
 * 公共方法 service 接口实现类
 */
@Service
public class CommonServiceImpl implements CommonService {

    public static void main(String[] args) {
        new CommonServiceImpl().changeResourceByYaml("");
    }

    @Override
    public String changeResourceByYaml(String yaml) {
        InputStream inputStream = Yaml.loadAs(yaml, InputStream.class);
        List<HasMetadata> orReplace = KubernetesUtils.client.load(inputStream).createOrReplace();

        System.out.println(orReplace);
        return "";

    }
}
