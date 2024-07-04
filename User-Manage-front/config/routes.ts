export default [
  {
    path: '/user',
    layout: false,
    routes: [
      { name: '登录', path: '/user/login', component: './User/Login'},
      { name: '注册', path: '/user/register', component: './User/Register'},
    ],
  },
  { path: '/welcome', name: '欢迎', icon: 'smile', component: './Welcome' },
  {
    path: '/admin',
    name: '管理页',
    icon: 'crown',
    access: 'canAdmin',
    routes: [
      { path: '/admin/user-manage', name: '用户管理页', component: './Admin/UserManage' },
    ],
  },
  {
    icon: 'crown',
    name: '个人页',
    path: '/account',
    routes: [
      { name: '个人中心', path: '/account/center', component: './Account/Center'},
      { name: '个人设置', path: '/account/settings', component: './Account/Settings'}
    ],
  },
  {
    path: '/account',
    routes: [
      { name: '个人中心', path: '/account/center', component: './Account/Center'},
      { name: '个人设置', path: '/account/settings', component: './Account/Settings'}
    ],
  },
  { path: '/', redirect: '/welcome' },
  { path: '*', layout: false, component: './404' },
];
