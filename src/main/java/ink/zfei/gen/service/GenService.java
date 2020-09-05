package ink.zfei.gen.service;

import ink.zfei.gen.MetaData;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lis on 17/5/15.
 */
@Service
public class GenService {

    private final String javaPath = "src" + File.separator + "main" + File.separator + "java";
    private final String resourcePath = "src" + File.separator + "main" + File.separator + "resources";


    public String genCode(MetaData metaData) {

        File file = new File(System.getProperty("user.dir"));

        String path = String.join(File.separator, file.getParent(), metaData.getProject(), metaData.getModule(), javaPath);
        String resource = String.join(File.separator, file.getParent(), metaData.getProject(), metaData.getModule(), resourcePath);

        try {
            List<String> warnings = new ArrayList<>();

            File conf = new File(System.getProperty("user.dir") + "/conf/Configuration.xml");
            ConfigurationParser parser = new ConfigurationParser(warnings);
            Configuration config = parser.parseConfiguration(conf);
            Context context = config.getContexts().get(0);

            // db
            JDBCConnectionConfiguration jdbcConnectionConfiguration = context.getJdbcConnectionConfiguration();
            String connection = "jdbc:mysql://" + metaData.getIp() + ":" + metaData.getPort() + "/" + metaData.getDb();
            jdbcConnectionConfiguration.setConnectionURL(connection);
            jdbcConnectionConfiguration.setUserId(metaData.getUsername());
            jdbcConnectionConfiguration.setPassword(metaData.getPassword());
            // model 配置
            JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = context.getJavaModelGeneratorConfiguration();
            javaModelGeneratorConfiguration.setTargetPackage(metaData.getModelpackagename());
            javaModelGeneratorConfiguration.setTargetProject(path);
            // DAO 配置
            JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = context.getJavaClientGeneratorConfiguration();
            javaClientGeneratorConfiguration.setTargetPackage(metaData.getDaopackagename());
            javaClientGeneratorConfiguration.setTargetProject(path);
            // Mapper
            SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = context.getSqlMapGeneratorConfiguration();
            sqlMapGeneratorConfiguration.setTargetPackage(metaData.getMapperpath());
            sqlMapGeneratorConfiguration.setTargetProject(resource);
            // 表
            List<TableConfiguration> tableConfigurations = context.getTableConfigurations();
            tableConfigurations.clear();
            String tableNames[] = metaData.getTablenames();
            String tableModels[] = metaData.getTablemodels();
//        String tableModels[] = request.getParameterValues("tablemodels");
            for (int i = 0; i < tableNames.length; i++) {
                if (!tableNames[i].isEmpty() && !tableModels[i].isEmpty()) {
                    TableConfiguration tableConfiguration = new TableConfiguration(context);
                    tableConfiguration.setTableName(tableNames[i]);
                    tableConfiguration.setDomainObjectName(tableModels[i]);
                    tableConfiguration.setCountByExampleStatementEnabled(false);
                    tableConfiguration.setDeleteByExampleStatementEnabled(false);
                    tableConfiguration.setSelectByExampleStatementEnabled(false);
                    tableConfiguration.setUpdateByExampleStatementEnabled(false);
                    tableConfiguration.getProperties().setProperty("useActualColumnNames", "false");
                    tableConfigurations.add(tableConfiguration);
                }
            }

            DefaultShellCallback callback = new DefaultShellCallback(true);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(null);

        } catch (SQLException e) {
            e.printStackTrace();
            return "01";
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return "02";
        } catch (InterruptedException ite) {
            ite.printStackTrace();
            return "03";
        } catch (InvalidConfigurationException ice) {
            ice.printStackTrace();
            return "04";
        } catch (XMLParserException xmle) {
            xmle.printStackTrace();
            return "05";
        }
        return "00";
    }

}

