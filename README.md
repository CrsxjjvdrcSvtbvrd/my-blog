> Kotlin Multiplatform 网站生成器


一个用于生成网站的 Kotlin Multiplatform 工具。它的主要功能是通过模块的组合生成 HTML 文件。目前支持以下函数：

- `import(文件, 参数)`: 该函数用于读取另一个模板文件，并将其替换到当前位置。
- `build(目标文件, 目标模板, 介绍模板, 数据文件(html))`: 通过目标模板创建 HTML 文件，并将介绍模板生成的板块插入到当前位置。

将编译后的二进制文件放到example文件夹中，运行，会读取data/index.html并开始生成，theme中可以作为模板文件，data作为数据文件

上班族精力有限，后面有时间慢慢完善，目前做一个小网站还是可以的
