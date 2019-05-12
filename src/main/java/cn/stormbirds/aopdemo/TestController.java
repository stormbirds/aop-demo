package cn.stormbirds.aopdemo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "测试控制器")
@RequestMapping(value = "")
public class TestController {

    @ApiOperation(value = "测试api", notes="测试切面取值")
    @RequestMapping(value = "/getid",method = RequestMethod.POST)
    @IsUser(check = true,userId = "#user.id")
    @Cacheable(value = "test",key = "#user")
    public String testAop(@RequestBody User user,@RequestParam Long cId){
        System.out.println("运算过程");
        return "未注解结果";
    }
}
