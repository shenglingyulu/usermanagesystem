import { PlusOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { ProTable,  } from '@ant-design/pro-components';
import {Button, Image, message} from 'antd';
import { useRef } from 'react';
import {removeUser, searchUsers, updateUser} from "@/services/ant-design-pro/api";
import {data} from "@umijs/utils/compiled/cheerio/lib/api/attributes";
export const waitTimePromise = async (time: number = 100) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve(true);
    }, time);
  });
};

export const waitTime = async (time: number = 100) => {
  await waitTimePromise(time);
};


const columns: ProColumns<API.CurrentUser>[] = [
  {
    title: 'id',
    dataIndex: 'id',
    editable:false,
  },
  {
    title: '用户名',
    dataIndex: 'username',
    ellipsis: true,
  },
  {
    title: '账号',
    dataIndex: 'userAccount',
    editable:false,
    ellipsis: true,
  },
  {
    title: '头像',
    dataIndex: 'avatarUrl',
    search:false,
    render: (_, record, ) => [
    <div>
     <Image src={record.avatarUrl} width={50}/>
   </div>
  ]
  },
  {
    title: '性别',
    dataIndex: 'gender',
    search:false,
    ellipsis: true,
    valueEnum:{
      0: {text:'男'},
      1: {text:'女'}
    }
  },
  {
    title: '电话',
    dataIndex: 'phone',
    search:false,
    ellipsis: true,
  },
  {
    title: '邮箱',
    search:false,
    dataIndex: 'email',
    ellipsis: true,
  },

  {
    title: '创建时间',
    search:false,
    dataIndex: 'createTime',
    editable:false,
    valueType: 'date',
    ellipsis: true,
  },
  {
    title: '用户状态',
    search:false,
    dataIndex: 'userStatus',
    ellipsis: true,
    valueEnum:{
      0: {text:'正常'},
      1: {text:'异常'}
    }
  },
  {
    title: '用户权限',
    search:false,
    dataIndex: 'role',
    ellipsis: true,
    valueEnum:{
      0: {text:'普道用户',status:'Default'},
      1: {text:'管理员',status:'Error'}
}
  },
  {
    title: '邀请码',
    search:false,
    dataIndex: 'invitationCode',
    editable:false,
    ellipsis: true,
  },
  {
    title: '操作',
    valueType: 'option',
    key: 'option',
    render: (text, record, _, action) => [
      <a
        key="editable"
        onClick={async () => {
          // @ts-ignore
          action?.startEditable?.(record.id);
        }}
      >
        编辑
      </a>,

    ],
  },
];

export default () => {
  const actionRef = useRef<ActionType>();
  // @ts-ignore
  return (
    <ProTable<API.CurrentUser>
      columns={columns}
      actionRef={actionRef}
      cardBordered
      request={async (params) => {
        console.log(params);
        const userList = await searchUsers({params});
        return {
          // @ts-ignore
          data: userList.data,
        }
      }}
      editable={{
        type: 'multiple',
        onSave: async (rowKey, data) => {
          const update =await updateUser(data);
          if (update.code===0) {
            message.success("保存成功")
            return {
              data: update.data
            }
          }
          else{
            message.error(update.description)
          }
          await waitTime(2000);

        },
        onDelete:async (rowKey, data) => {
          console.log(data.id)
          //@ts-ignore
          const remove =await removeUser(data.id);
          if(remove.code===0)
          {
            message.success("删除成功")
            return {
              data: remove.data
            }
          }else{
            message.error(remove.description)
          }

          await waitTime(2000);

        },
      }}
      columnsState={{
        persistenceKey: 'pro-table-singe-demos',
        persistenceType: 'localStorage',
        defaultValue: {
          option: {fixed: 'right', disable: true},
        },
        onChange(value) {
          console.log('value: ', value);
        },
      }}
      rowKey="id"
      search={{
        labelWidth: 'auto',

      }}
      options={{
        setting: {
          listsHeight: 400,
        },
      }}

      pagination={{
        pageSize: 2,
        total: 20,
        onChange: (page) => console.log(page),
      }}

      dateFormatter="string"
      headerTitle="用户信息"

    />
  );
};
