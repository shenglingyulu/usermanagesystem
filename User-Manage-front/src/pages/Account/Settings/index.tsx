// src/pages/UserSettings.tsx

import React, { useEffect, useState } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import {Card, Form, Input, Button, message, Spin, Upload,Radio} from 'antd';
import {currentUser, updateUser, uploadAvatar} from "@/services/ant-design-pro/api";
import { UploadOutlined } from '@ant-design/icons';
import {sleep} from "@antfu/utils";

const UserSettings: React.FC = () => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState<boolean>(true);
  const [, setUser] = useState<API.CurrentUser | undefined>(undefined);
  const [avatarLoading, setAvatarLoading] = useState<boolean>(false);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        const response = await currentUser();
        if (response?.data) {
          //@ts-ignore
          setUser(response.data);
          form.setFieldsValue(response.data);
        }
      } catch (error) {
        console.error('Failed to fetch current user:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [form]);
  const handleAvatarUpload = async (file: File) => {
    setAvatarLoading(true);
    try {
      const response = await uploadAvatar(file);
      if (response.code===0) {
        form.setFieldsValue({ avatarUrl: response.data.url });
        message.success('头像上传成功');
        await sleep(3000)
        location.reload()
      } else {
        message.error(response.description);
      }
    } catch (error) {
      console.error('上传图像失败:', error);
      message.error('上传图像失败');
    } finally {
      setAvatarLoading(false);
    }
  };
  const onFinish = async (values: API.CurrentUser) => {
    try {
      const response = await updateUser(values);
      if (response.code === 0) {
        message.success('用户更新成功');
        await sleep(3000)
        location.reload()
      } else {
        message.error(response.description);
      }
    } catch (error) {
      console.error('Failed to update user info:', error);
      message.error('Failed to update user info');
    }
  };

  return (
    <PageContainer>
      <Card>
        {loading ? (
          <Spin size="large" />
        ) : (
          <Form form={form} onFinish={onFinish} layout="vertical">
            <Form.Item name="id" label="id">
            </Form.Item>
            <Form.Item name="username" label="用户名">
              <Input />
            </Form.Item>
            <Form.Item name="avatarUrl" label="头像">
              <Input hidden />
              <Upload
                showUploadList={false}
                customRequest={({ file }) => handleAvatarUpload(file as File)}
              >
                <Button icon={<UploadOutlined />} loading={avatarLoading}>上传图片</Button>
              </Upload>
              {form.getFieldValue('avatarUrl') && (
                <img src={form.getFieldValue('avatarUrl')} alt="avatar" style={{ width: 50, height: 50, marginTop: 10 }} />
              )}
            </Form.Item>

            <Form.Item name="gender" label="性别">
              <Radio.Group>
                <Radio value="0">男</Radio>
                <Radio value="1">女</Radio>
              </Radio.Group>
            </Form.Item>
            <Form.Item name="phone" label="手机号">
              <Input />
            </Form.Item>
            <Form.Item name="email" label="邮箱">
              <Input />
            </Form.Item>
            <Form.Item>
              <Button type="primary" htmlType="submit">更新</Button>
            </Form.Item>
          </Form>
        )}
      </Card>
    </PageContainer>
  );
};

export default UserSettings;
