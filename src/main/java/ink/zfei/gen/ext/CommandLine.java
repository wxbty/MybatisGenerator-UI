package ink.zfei.gen.ext;

import ink.zfei.gen.GeneratorFacade;
import ink.zfei.gen.GeneratorProperties;
import ink.zfei.gen.util.ArrayHelper;
import ink.zfei.gen.util.StringHelper;
import ink.zfei.gen.util.SystemHelper;

import java.io.File;

/**
 * 命令行工具类,可以直接运行
 *
 * @author badqiu
 */
public class CommandLine {

    private static final String javaPath = "src" + File.separator + "main" + File.separator + "java";
    private final String resourcePath = "src" + File.separator + "main" + File.separator + "resources";

    public static void startProcess(String cmd, String tableName) {

        System.out.println("templateRootDir:" + new File(getTemplateRootDir()).getAbsolutePath());
        printUsages();
        try {
            processLine(cmd, tableName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            printUsages();
        }
    }


    private static void processLine(String cmd, String tableName) throws Exception {
        
        String project = "adudu";

        File file = new File(System.getProperty("user.dir"));
        String path = String.join(File.separator, file.getParent(), project, javaPath, "com", "news");

        GeneratorFacade facade = new GeneratorFacade(path);
        if ("gen".equals(cmd)) {
            String[] args = nextArguments(tableName);
            if (args.length == 0) return;
            facade.g.setIncludes(getIncludes(args, 1));
            facade.generateByTable(args[0], getTemplateRootDir());
            if (SystemHelper.isWindowsOS) {
                // renfufei 处理目录不存在的问题
                String outRoot = GeneratorProperties.getRequiredProperty("outRoot").replace('/', '\\');
                //
                File outRootDir = new File(outRoot);
                if (!outRootDir.exists()) {
                    outRootDir.mkdirs();
                }
                // 打开目录
                Runtime.getRuntime().exec("cmd.exe /c start " + outRoot);
            }
        } else if ("del".equals(cmd)) {
            String[] args = nextArguments(tableName);
            if (args.length == 0) return;
            facade.g.setIncludes(getIncludes(args, 1));
            facade.deleteByTable(args[0], getTemplateRootDir());
        } else if ("quit".equals(cmd) || "exit".equals(cmd)) {
            System.exit(0);
        } else {
            System.err.println(" [ERROR] unknow command:" + cmd);
        }
    }

    private static String getIncludes(String[] args, int i) {
        String includes = ArrayHelper.getValue(args, i);
        if (includes == null) return null;
        return includes.indexOf("*") >= 0 || includes.indexOf(",") >= 0 ? includes : includes + "/**";
    }

    private static String getTemplateRootDir() {
        return System.getProperty("templateRootDir", "template");
    }

    private static void printUsages() {
        System.out.println("Usage:");
        System.out.println("\tgen table_name [include_path]: generate files by table_name");
        System.out.println("\tdel table_name [include_path]: delete files by table_name");
        System.out.println("\tgen * [include_path]: search database all tables and generate files");
        System.out.println("\tdel * [include_path]: search database all tables and delete files");
        System.out.println("\tquit : quit");
        System.out.println("\t[include_path] subdir of templateRootDir,example: 1. dao  2. dao/**,service/**");
        System.out.print("please input command:");
    }

    private static String[] nextArguments(String tableName) {
        return StringHelper.tokenizeToStringArray(tableName, " ");
    }
}
