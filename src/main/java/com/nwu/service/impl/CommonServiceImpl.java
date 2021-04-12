package com.nwu.service.impl;

/**
 * @author Rex Joush
 * @time 2021.04.07
 */

import com.nwu.service.CommonService;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.HasMetadata;
import org.springframework.stereotype.Service;

import java.io.*;
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
            inputStream.close();
            yaml.delete();
            if (orReplace != null) {
                return 1200;
            } else {
                return 1201;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 1202;
        }
    }

    @Override
    public int changeServicesByYaml(File yaml) {

        try {
            InputStream inputStream = new FileInputStream(yaml);

            io.fabric8.kubernetes.api.model.Service service = KubernetesUtils.client.services().load(inputStream).get();

            Boolean delete = KubernetesUtils.client.services().inNamespace(service.getMetadata().getNamespace()).withName(service.getMetadata().getName()).delete();

            InputStream inputStream2 = new FileInputStream(yaml);

            List<HasMetadata> orReplace = KubernetesUtils.client.load(inputStream2).createOrReplace();

            inputStream.close();

            yaml.delete();

            if (delete) {
                return 1200;
            }else {
                return 1201;
            }


        } catch (Exception e) {
            e.printStackTrace();
            return 1020;
        }
    }

}
