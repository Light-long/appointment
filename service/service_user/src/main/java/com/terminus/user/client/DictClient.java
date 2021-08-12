package com.terminus.user.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-cmn")
@Component
public interface DictClient {

    @GetMapping("/admin/cmn/dict/getName/{value}")
    String getNameByValue(@PathVariable(value = "value") String value);

    @GetMapping("/admin/cmn/dict/getName/{dictCode}/{value}")
    String getNameByDictCodeAndValue(@PathVariable(value = "dictCode") String dictCode,
                                     @PathVariable(value = "value") String value);
}