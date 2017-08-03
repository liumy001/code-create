package org.unique.generator;

import org.unique.generator.model.Database;
import org.unique.generator.model.Table;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
* Created by renrf on 2016/9/12.
*/
public class Main {

    public static void main(String[] args){
        String classDriver= ConfigUtil.getValue("mysql.driverClassName");
        String url=ConfigUtil.getValue("mysql.url");
        String username=ConfigUtil.getValue("mysql.username");
        String password=ConfigUtil.getValue("mysql.password");
        String soas = ConfigUtil.getValue("modules");
        String soaPath = ConfigUtil.getValue("soa.project.path");

        try {
            String schema = url.substring(url.lastIndexOf("/") + 1);
            CodeGenerator codeGenerator = new CodeGenerator();
            Database databaseBean = null;
            databaseBean = new DatabaseInfoOp(classDriver, url, username, password, schema).getDbInfo();
            // 获取该库所有表
            List<Table> tableList = databaseBean.getTableList();

            for(String soa:soas.split(",")){
                String[] modules = soa.split("-");
                String module = modules[modules.length - 1];
                String outPath = soaPath + soa + "/src/main/java/";
                String[] tableNames = ConfigUtil.getValue("module."+soa).split(",");

                codeGenerator.generator(processTables(tableList, tableNames) ,module,outPath);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Table> processTables(List<Table> tableList, String[] tableNames){
        List<Table> tables = new ArrayList<>();
        List<String> tableNameList = Arrays.asList(tableNames);
        for(Table table:tableList){
            if(tableNameList.contains(table.getTableName())){
                tables.add(table);
            }
        }
        return tables;
    }
}
