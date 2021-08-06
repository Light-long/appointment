package com.terminus.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.terminus.model.model.cmn.Dict;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DictService extends IService<Dict> {
    List<Dict> findChildData(Long id);

    void exportDict(HttpServletResponse response);

    void importDict(MultipartFile file);
}
