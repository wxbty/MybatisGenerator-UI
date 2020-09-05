package ink.zfei.gen.controller;

import com.alibaba.fastjson.JSONObject;
import ink.zfei.gen.MetaData;
import ink.zfei.gen.service.GenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by lis on 17/5/11.
 */
@Controller
public class IndexController {

    @Autowired
    public GenService genService;


    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/gen")
    @ResponseBody
    public String generator(MetaData metaData) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", genService.genCode(metaData));
        return jsonObject.toJSONString();
    }


}