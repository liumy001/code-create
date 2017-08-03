1.生成代码前在app.properties里配置好
  mysql数据库连接，soa项目的根路径，web项目和entity项目的src路径，数据库中表的前缀，要生成的模块，以及每个模块包含的表。
2.生成代码前要存有放代码的空项目，包含一些基本的spring配置。
3.运行Main.class开始生成代码。
4.zrpd-base,和sample-web里有一些复杂的controller,service，proxy，process，dto，可以参照。这些代码是被注释掉的，
  只是作为参照，编译不通过的。