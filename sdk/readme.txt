


SDKManager使用流程

1. 建议在Application的onCreate中初始化sdk，调用SDKManager.getInstance().init()，传入context appId appKey

2. 调用SDKManager提供的功能，如筹码分布、相似K线等


注意项：
1）GUID处理策略：如果是灯塔APP，SDK自动取消GUID获取及上报流程，交由APP处理；如果是第三方APP，则走GUID获取及上报
2）关于DUA中渠道号：如果是灯塔APP，渠道号自身来处理，如果是第三方APP，则SDK会自动生成渠道号为"SDK"


对第三方APP的影响：


创建文件：
data/file/目录下面创建一个服务器ip存储文件
data/file/目录下面创建一个pref配置文件




// TODO:
ajax请求模块
BEC协议转JSON
