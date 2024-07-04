本项目是前后端分离项目
打开项目后前端前端部分在终端输入 npm install 加载所需要的内容
后端在application.yml中
file:
  uploadFolder: D:/project/user-manage-system/User-Manage-back/src/main/resources/static/images/ #修改为自己的路径
  staticAccessPath: /file/**
  request: http://localhost:8000/api/file/  
