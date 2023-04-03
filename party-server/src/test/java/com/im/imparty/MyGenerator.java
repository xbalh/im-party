package com.im.imparty;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * mybatis-plus-generator代码生成器
 * 修改配置后直接运行main方法即可
 */
public class MyGenerator {

    /** 数据源配置*/
    private static final String jdbc = "jdbc:mysql://117.50.183.42:3306/imparty?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=true";
    private static final String driverName = "com.mysql.cj.jdbc.Driver";
    private static String username = "";
    private static String password = "";

    static {
        // -DdatabaseUser=xxx  -DdatabasePassword=xxx
        username = System.getProperty("DdatabaseUser");
        password = System.getProperty("DdatabasePassword");
    }

    /** 包名*/
    private static final String moduleName = "user";
    /** 表名前缀*/
    private static final String beginName = "";
    /** 需要生成代码的表*/
    private static final String [] tables = new String[]{"user"};




    public static void main(String[] args) {

        // 官方网站：https://baomidou.com/pages/d357af/#%E8%87%AA%E5%AE%9A%E4%B9%89%E5%B1%9E%E6%80%A7%E6%B3%A8%E5%85%A5

        // 参考网站：https://blog.csdn.net/kinghmj01/article/details/97748509

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        // 当前项目路径
        // String projectPath = System.getProperty("user.dir");
        String projectPath = "E:/code/java/im-party/user-manager";
        // 当前项目的下的路径
        gc.setOutputDir(projectPath + "/src/main/java");
        // 作者
        gc.setAuthor("2");
        // 是否打开输出目录 默认为true
        gc.setOpen(false);
        // 实体属性 Swagger2 注解
        gc.setSwagger2(true);

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setControllerName("%sController");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setMapperName("%sMapper");
        gc.setEntityName("%sDomain");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        // dsc.setSchemaName("public");
        dsc.setUrl(jdbc);
        dsc.setDriverName(driverName);
        dsc.setUsername(username);
        dsc.setPassword(password);
        mpg.setDataSource(dsc);

        // 设置包名
        PackageConfig pc = new PackageConfig();
        // 用于包名、表名前缀
        //pc.setModuleName(moduleName);
        // 生成到那些包下 如 com.modules主包下的 controller.sys.TestController
        pc.setParent("com.im.imparty.test");
        pc.setController(moduleName + ".controller");
        pc.setService(moduleName + ".service");
        pc.setServiceImpl(moduleName + ".service"+".impl");
        pc.setMapper(moduleName + ".mapper");
        pc.setEntity(moduleName + ".entity");
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/java/" + StringUtils.join(pc.getParent().split("\\."), "/") + "/" + moduleName + "/mapper/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        /*
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建
                checkDir("调用默认方法创建的目录，自定义目录用");
                if (fileType == FileType.MAPPER) {
                    // 已经生成 mapper 文件判断存在，不想重新生成返回 false
                    return !new File(filePath).exists();
                }
                // 允许生成模板文件
                return true;
            }
        });
        */
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        // templateConfig.setService();
        // templateConfig.setController();

        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();

        //需要生成的表
        strategy.setInclude(tables);
        strategy.setControllerMappingHyphenStyle(true);
        //类名生成策略：驼峰命名
        strategy.setNaming(NamingStrategy.underline_to_camel);
        //字段名生成方式：驼峰命名
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // 表前缀
        strategy.setTablePrefix(beginName+"_");

        // 写于父类中的公共字段（在父类中已经有的不需要生成的字段）
        strategy.setSuperEntityColumns("id");
        // 每层的继承（不需要可不设置）
        strategy.setSuperControllerClass("com.im.imparty.common.BaseController");
        strategy.setSuperServiceClass("com.baomidou.mybatisplus.extension.service.IService");
        strategy.setSuperServiceImplClass("com.baomidou.mybatisplus.extension.service.impl.ServiceImpl");
        strategy.setSuperMapperClass("com.baomidou.mybatisplus.core.mapper.BaseMapper");
        //继承的属性父类
        strategy.setSuperEntityClass("com.im.imparty.common.BaseDomain");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);

        mpg.setStrategy(strategy);

        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        mpg.execute();
    }



}

