package com.imooc.ssm.controller;

import com.imooc.ssm.bean.Seckill;
import com.imooc.ssm.dto.Exposer;
import com.imooc.ssm.dto.SeckillExcution;
import com.imooc.ssm.dto.SeckillResult;
import com.imooc.ssm.enums.SeckillStateEnum;
import com.imooc.ssm.exception.RepeatKillException;
import com.imooc.ssm.exception.SeckillCloseException;
import com.imooc.ssm.exception.SeckillException;
import com.imooc.ssm.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 秒杀控制器类
 */
@Controller
@RequestMapping(value = "/seckill")
public class SeckillController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @RequestMapping(value = "/list",produces = {"application/json;charset=utf8"})
    @ResponseBody
    public List<Seckill> list(Model model) {
        List<Seckill> seckillList = seckillService.getSeckillList();
        return seckillList;
    }

    @RequestMapping(value = "/{seckillId}/detail",method = RequestMethod.GET,produces = {"application/json;charset=utf8"})
    @ResponseBody
    public Map<Object, Object> detail(@PathVariable("seckillId") Long seckillId, Model model) {
        Map<Object, Object> resultMap = new HashMap<Object, Object>();
        if (seckillId == null) {
            String errorInfo = "no select condithonal find";
            resultMap.put("error", errorInfo);
            return resultMap;
        }
        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null) {
            logger.error("该ID没有对应的秒杀产品!");
            resultMap.put("error", "该ID没有对应的秒杀产品!");
            return resultMap;
        }
        resultMap.put("seckill", seckill);
        return resultMap;
    }

    @RequestMapping(value ="/{seckillId}/exposer",method = RequestMethod.GET,produces = {"application/json;charset=utf8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {

        SeckillResult<Exposer> result;
        Exposer exposer=null;
        try {

            exposer= seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new SeckillResult<Exposer>(false, e.getMessage());
        }

        return result;
    }


    @RequestMapping(value = "/{seckillId}/{md5}/execution",method = RequestMethod.POST)
    @ResponseBody
    public SeckillResult<SeckillExcution> excution(@PathVariable("seckillId") Long seckillId, @PathVariable("md5") String md5, @CookieValue(value = "killPhone", required = false) Long iphone, Model model) {

        SeckillExcution seckillExcution=null;
        try {
            if (iphone == null) {
                iphone = 13664925208L;
            }
            seckillExcution= seckillService.executeSeckill(seckillId, iphone, md5);
            return new SeckillResult<SeckillExcution>(true, seckillExcution);
        } catch (RepeatKillException e) {
            seckillExcution = new SeckillExcution(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExcution>(true, seckillExcution);

        } catch (SeckillCloseException e) {
            seckillExcution = new SeckillExcution(seckillId, SeckillStateEnum.END);
            return new SeckillResult<SeckillExcution>(true, seckillExcution);
        } catch (SeckillException e) {
            logger.error(e.getMessage(), e);
            seckillExcution = new SeckillExcution(seckillId, SeckillStateEnum.INNER_ERROR);
            return new SeckillResult<SeckillExcution>(true, seckillExcution);
        }

    }

    @RequestMapping(value = "/now")
    public SeckillResult<Long> time() {
        Date date = new Date();
        return new SeckillResult<Long>(date.getTime());

    }
}
