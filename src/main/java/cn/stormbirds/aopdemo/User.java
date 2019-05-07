package cn.stormbirds.aopdemo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "User",description = "用户Model")
public class User {
    @ApiModelProperty(value = "id",example = "1",required = true)
    private Long id;
    @ApiModelProperty(value = "名字",example = "张三",required = true)
    private String name;
    @ApiModelProperty(value = "年龄",example = "10",required = true)
    private int age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
