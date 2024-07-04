// src/pages/UserProfile.tsx

import React, { useEffect, useState } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import { Card, Descriptions, Spin } from 'antd';
import {currentUser} from "@/services/ant-design-pro/api";

const UserProfile: React.FC = () => {
  const [loading, setLoading] = useState<boolean>(true);
  const [user, setUser] = useState<API.CurrentUser | undefined>(undefined);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        const response = await currentUser();
        if (response?.data) {
          //@ts-ignore
          setUser(response.data);
        }
      } catch (error) {
        console.error('Failed to fetch current user:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  return (
    <PageContainer>
      <Card>
        {loading ? (
          <Spin size="large" />
        ) : (
          <Descriptions title="用户信息" bordered>
            <Descriptions.Item label="id">{user?.id}</Descriptions.Item>
            <Descriptions.Item label="用户名">{user?.username}</Descriptions.Item>
            <Descriptions.Item label="账号">{user?.userAccount}</Descriptions.Item>
            <Descriptions.Item label="头像">
              {user?.avatarUrl ? <img src={user.avatarUrl} alt="avatar" style={{ width: 50, height: 50 }} /> : 'N/A'}
            </Descriptions.Item>
            <Descriptions.Item label="性别">{user?.gender === 1 ? '女':'男'}</Descriptions.Item>
            <Descriptions.Item label="电话">{user?.phone}</Descriptions.Item>
            <Descriptions.Item label="邮箱">{user?.email}</Descriptions.Item>
            <Descriptions.Item label="状态">{user?.userStatus===0?'正常':'异常'}</Descriptions.Item>
            <Descriptions.Item label="创建时间">{user?.createTime?.toLocaleString()}</Descriptions.Item>
            <Descriptions.Item label="更新时间">{user?.updateTime?.toLocaleString()}</Descriptions.Item>
            <Descriptions.Item label="身份">{user?.role===0?'普通用户':'管理员'}</Descriptions.Item>
            <Descriptions.Item label="邀请码">{user?.invitationCode}</Descriptions.Item>
            <Descriptions.Item label="是否删除">{user?.isDelete===1 ? '是' : '否'}</Descriptions.Item>
          </Descriptions>
        )}
      </Card>
    </PageContainer>
  );
};

export default UserProfile;
