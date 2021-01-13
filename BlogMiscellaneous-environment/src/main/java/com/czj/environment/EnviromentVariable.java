package com.czj.environment;

public class EnviromentVariable {
    public static void main(String[] args) {
        //Java 运行时环境版本
        System.out.println(System.getProperty("java.version"));
        //操作系统的名称
        System.out.println(System.getProperty("os.name"));
        //操作系统的架构
        System.out.println(System.getProperty("os.arch"));
        //操作系统的版本
        System.out.println(System.getProperty("os.version"));
        System.out.println("----------------------------------------");
        //分隔符  linux中是  /   windows中是   \
        System.out.println(System.getProperty("file.separator"));
        //分隔符  linux中是 :    windows中是   ;
        System.out.println(System.getProperty("path.separator"));
        //分隔符  linux中是 /n   windows中是  \n
        System.out.print(System.getProperty("line.separator"));
        System.out.println("----------------------------------------");
        //Java 安装目录
        System.out.println(System.getProperty("java.home"));
        //默认的临时文件路径
        System.out.println(System.getProperty("java.io.tmpdir"));
        System.out.println("----------------------------------------");
        //用户名称
        System.out.println(System.getProperty("user.name"));
        //用户主目录
        System.out.println(System.getProperty("user.home"));
        //项目路径
        System.out.println(System.getProperty("user.dir"));
        System.out.println("----------------------------------------");
        //设置自定义系统属性
        System.setProperty("my_property", "hello world");
        //获取自定义系统属性，无值返回null
        System.out.println(System.getProperty("my_property"));
    }
}
/*java.version          Java 运行时环境版本
	java.vendor         Java 运行时环境供应商
	java.vendor.url         Java 供应商的 URL
	java.vm.specification.version         Java 虚拟机规范版本
	java.vm.specification.vendor         Java 虚拟机规范供应商
	java.vm.specification.name         Java 虚拟机规范名称
	java.vm.version         Java 虚拟机实现版本
	java.vm.vendor         Java 虚拟机实现供应商
	java.vm.name         Java 虚拟机实现名称
	java.specification.version         Java 运行时环境规范版本
	java.specification.vendor         Java 运行时环境规范供应商
	java.specification.name         Java 运行时环境规范名称
	os.name         操作系统的名称
	os.arch         操作系统的架构
	os.version         操作系统的版本
	file.separator         文件分隔符（在 UNIX 系统中是“ / ”）
	path.separator         路径分隔符（在 UNIX 系统中是“ : ”）
	line.separator         行分隔符（在 UNIX 系统中是“ /n ”）

	java.home         Java 安装目录
	java.class.version         Java 类格式版本号
	java.class.path         Java 类路径
	java.library.path          加载库时搜索的路径列表
	java.io.tmpdir         默认的临时文件路径
	java.compiler         要使用的 JIT 编译器的名称
	java.ext.dirs         一个或多个扩展目录的路径
	user.name         用户的账户名称
	user.home         用户的主目录
	user.dir
*/