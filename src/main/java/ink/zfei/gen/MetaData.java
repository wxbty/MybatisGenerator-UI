package ink.zfei.gen;

public class MetaData {

    private String ip;
    private String db;
    private String port;
    private String username;
    private String password;
    private String modelpackagename;
    private String daopackagename;
    private String mapperpath;
    private String project;
    private String module;
    private String tablenames[];
    private String tablemodels[];

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getModelpackagename() {
        return modelpackagename;
    }

    public void setModelpackagename(String modelpackagename) {
        this.modelpackagename = modelpackagename;
    }

    public String getDaopackagename() {
        return daopackagename;
    }

    public void setDaopackagename(String daopackagename) {
        this.daopackagename = daopackagename;
    }

    public String getMapperpath() {
        return mapperpath;
    }

    public void setMapperpath(String mapperpath) {
        this.mapperpath = mapperpath;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String[] getTablenames() {
        return tablenames;
    }

    public void setTablenames(String[] tablenames) {
        this.tablenames = tablenames;
    }

    public String[] getTablemodels() {
        return tablemodels;
    }

    public void setTablemodels(String[] tablemodels) {
        this.tablemodels = tablemodels;
    }
}
