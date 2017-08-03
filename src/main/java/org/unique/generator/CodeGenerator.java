package org.unique.generator;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.unique.generator.model.Database;
import org.unique.generator.model.Table;

/**
 * 代码生成类
 * 
 * @ClassName: DomainGenerator
 * @author AndrewWen
 * @date 2013-1-14 下午8:45:30
 */
public class CodeGenerator {

	private Logger logger = Logger.getLogger(CodeGenerator.class);
	
	private List<String> listTbaleName = new ArrayList<String>();

    private static String webPath = ConfigUtil.getValue("web.project.path");
    private static String entityPath = ConfigUtil.getValue("entity.project.path");
    private static String classPackage = ConfigUtil.getValue("parent.package");
    private static String author=ConfigUtil.getValue("code.author");
    private static String contact=ConfigUtil.getValue("author.contact");

	//是否去掉模块前面的编号
	public static String prefix = "";
	
	public boolean generator(List<Table> tableList, String module, String soaPath) {

		Long start = System.currentTimeMillis();
		
		//initList();

		try {

            List<Table> soaList = new ArrayList<>();

			logger.info("---------------start---------------");
			
			/**
			 * 遍历生成代码
			 */
			for (int i=0,j=tableList.size();i<j;i++) {
				Table table = tableList.get(i);
				table.setPackageName(classPackage);
				
				Map<String, Object> map = new HashMap<String, Object>();

				SimpleDateFormat formatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String sdate = formatTime.format( new Date());

				map.put("table", table);
				map.put("contact", contact);
				map.put("author", author);
				map.put("mail", contact);
				map.put("nowDate",sdate);
				map.put("shuoming", table.getTableComment());
				map.put("firstPath", "accountCenter");
                map.put("module", module);
				//过滤掉已经生成过code的表
				//if(this.listTbaleName.contains(table.getTableName())){
					// 生成对应的代码模板导入
                    VelocityInfoOp.generatorCode("entity.vm", map, entityPath + table.getPackagePath() + "/entity",
                            table.getClassName() + "Entity.java");

					VelocityInfoOp.generatorCode("dao.vm", map, soaPath + table.getPackagePath() + "/" + module + "/dao",
                            table.getClassName() + "Dao.java");

					VelocityInfoOp.generatorCode("mapper.vm", map, soaPath + table.getPackagePath() + "/" + module + "/dao/map",
                            table.getClassNameFirstLower() + "EntityMysql.xml");

                    VelocityInfoOp.generatorCode("innerService.vm", map, soaPath + table.getPackagePath() + "/" + module + "/api/inner",
                            table.getClassName() + "InnerService.java");

                    VelocityInfoOp.generatorCode("outerService.vm", map, soaPath + table.getPackagePath() + "/" + module + "/api/outer",
                            table.getClassName() + "OuterService.java");

                    VelocityInfoOp.generatorCode("soaService.vm", map, soaPath + table.getPackagePath() + "/" + module + "/service",
                            table.getClassName() + "Service.java");

                    VelocityInfoOp.generatorCode("proxy.vm", map, soaPath + table.getPackagePath() + "/" + module + "/proxy",
                            table.getClassName() + "ServiceProxy.java");

                    VelocityInfoOp.generatorCode("request.vm", map, soaPath + table.getPackagePath() + "/" + module + "/dto/request",
                            table.getClassName() + "Request.java");

                    VelocityInfoOp.generatorCode("response.vm", map, soaPath + table.getPackagePath() + "/" + module + "/dto/response",
                            table.getClassName() + "Response.java");

                    // web端service生成
                    VelocityInfoOp.generatorCode("busService.vm", map, webPath + table.getPackagePath() + "/"
                                    + table.getClassName() + "/service/impl", table.getClassName() + "ServiceImpl.java");

                    // web端service接口生成
                    VelocityInfoOp.generatorCode("IService.vm", map, webPath + table.getPackagePath() + "/"
                            + table.getClassNameFirstLower() + "/service", "I" + table.getClassName() + "Service.java");

                    // web端service生成
                    VelocityInfoOp.generatorCode("controller.vm", map, webPath + table.getPackagePath() + "/"
                            + table.getClassNameFirstLower() + "/controller", table.getClassName() + "Controller.java");

                    soaList.add(table);
                    logger.info("表：" + table.getTableName() + "成功");
				//}
			}

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("tableList", soaList);
            map.put("module", module);

            VelocityInfoOp.generatorCode("innerConsumer.vm", map, soaPath.replace("java", "resources") + "/META-INF/spring",
                    "zrpd-" + module + "-inner-consumer.xml");

            VelocityInfoOp.generatorCode("outerConsumer.vm", map, soaPath.replace("java", "resources") + "/META-INF/spring",
                    "zrpd-" + module + "-outer-consumer.xml");

            VelocityInfoOp.generatorCode("provider.vm", map, soaPath.replace("java", "resources") + "/META-INF/spring",
                    "zrpd-" + module + "-provider.xml");

			logger.info("--------------- end time：" + (System.currentTimeMillis() - start) + "ms-----");
			logger.info("包：" + classPackage);
			logger.info("作者：" + author);
			logger.info("联系：" + contact);
			logger.info("------------------------------------");
			return true;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return false;
	}

	//初始化要过滤的表
	public void initList(){
		
		this.listTbaleName.add("fw_product");
	}

}
