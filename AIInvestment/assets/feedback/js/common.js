$.extend($, {
    //jq模板
    compiler: function (str, data){
        var compiler = arguments.callee;
        //如果参数str全部为组词字符
        //console.log /^\w*$/.test(str), "xxx"
        var fn;
        if(/^\w*$/.test(str)){
            fn = !compiler[str] && (compiler[str] = compiler(document.getElementById(str).innerHTML));
        }else{
            fn = new Function("obj", "var p=[];with(obj){p.push('" +
                str.replace(/[\r\t\n]/g, " ")
                    .split("<@").join("\t")
                    .replace(/((^|@>)[^\t]*)'/g, "$1\r")
                    .replace(/\t=(.*?)@>/g, "',$1,'")
                    .split("\t").join("');")
                    .split("@>").join("p.push('")
                    .split("\r").join("\\'")+ "');}return p.join('');");
        }
        return data ? fn(data) : fn;
    },

    //touch事件 模拟tap事件  并且添加active
    touchEvent: function () {
        var isMob 	= ("ontouchstart" in document),
            START_EV = isMob ? 'touchstart' : 'mousedown',
            MOVE_EV = isMob ? 'touchmove' : 'mousemove',
            END_EV = isMob ? 'touchend' : 'mouseup',
            CANCEL_EV = isMob ? 'touchcancel' : 'mouseup';
        if(arguments.length == 2){
            var elem = arguments[0];
            var callback = arguments[1];
            addActiveEvent(elem,"");
        }else if(arguments.length == 3){
            var elem = arguments[0];
            var selector = arguments[1];
            var callback = arguments[2];
            addActiveEvent(elem,selector);
        }
        //给元素添加事件
        function addActiveEvent(elem,selector){
            if(selector == ""){
                elem.on(START_EV,startEvent)
            }else{
                elem.on(START_EV,selector,startEvent)
            }
        }
        function startEvent(event){
            event.stopPropagation();
            var ifMove = false;
            var that = this;
            var timer = setTimeout(function(){
                if(!ifMove){
                    $(that).addClass('active');
                }
            },80);
            $(this).on(MOVE_EV,function () {
                clearTimeout(timer);
                ifMove = true;
                $(that).removeClass('active');
                eventOff(that);
            })
            $(this).on(END_EV,function () {
                clearTimeout(timer);
                if(!ifMove){
                    $(that).removeClass('active');
                    callback.call(that,event);
                }
                eventOff(that);
            })
            $(this).on(CANCEL_EV,function () {
                clearTimeout(timer);
                $(that).removeClass('active');
                eventOff(that);
            })
        }
        function eventOff(element){
            // $(element).off(START_EV);
            $(element).off(MOVE_EV);
            $(element).off(END_EV);
            $(element).off(CANCEL_EV);
        }
    },
    getQueryString:function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return decodeURIComponent(r[2]); return null;
    },

    /** 
     *var testDate = new Date( 1320336000000 );//这里必须是整数，毫秒
     *var testStr = testDate.format("yyyy年MM月dd日hh小时mm分ss秒");
     *var testStr2 = testDate.format("yyyyMMdd hh:mm:ss");
     */
    dateConverter:function(formatData, timestamp){
        Date.prototype.format = function(format){
            var o =
            {
                "M+" : this.getMonth()+1, //month
                "d+" : this.getDate(), //day
                "h+" : this.getHours(), //hour
                "m+" : this.getMinutes(), //minute
                "s+" : this.getSeconds(), //second
                "q+" : Math.floor((this.getMonth()+3)/3), //quarter
                "S" : this.getMilliseconds() //millisecond
            }

            if(/(y+)/.test(format))
            {
                format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
            }

            for(var k in o)
            {
                if(new RegExp("("+ k +")").test(format))
                {
                    format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
                }
            }
            return format;
        }
        var timeFormat = new Date(timestamp);
        return timeFormat.format(formatData);
    },
    isIOS:function(){
        var u = navigator.userAgent;
        return !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);
    },
    alert:function( tips,times){
        if(times==undefined){
            var optTimes=2000;
        }else{
            var optTimes=times;
        }

        if(!$("#util-pop-alert").length){

            $("body").append('<div id="util-pop-alert" class="pop_alert"></div>')

            $("#util-pop-alert").css({
                position: 'fixed',
                left: '50%',
                bottom: '40px',
                background: 'rgba(89,89,89,.9)',
                color: '#fff',
                padding: '10px',
                opacity: '0',
                'text-align':'center',
                '-webkit-transform':'translate(-50%,0)',
                'transform':'translate(-50%,0)',
                'z-index': '2000',
                'width': '78%',
                'border-radius': '6px',
                'line-height': '1.5',
                'word-break':'break-all',
                'font-size':'16px',
                '-webkit-transition':'all .3s ease-in'
            }).on('tap',function(){
                $("#util-pop-alert").css({opacity:'0'});
            });
        }

        setTimeout(function(){
            $("#util-pop-alert").html(tips).css({opacity:'1'});
        },0);
        
        setTimeout(function(){
            $("#util-pop-alert").css({opacity:'0'});
        },optTimes);
    },
    mobileConsole:function(content){
        var consoleContent='',
            mobileConsoleStyle='<style>	#mobileConsole{position: fixed; z-index: 1000; right: 0; top: 0; width: 60%; height: 50%; background-color: rgba(0,0,0,0.6); overflow: auto; color: #fff; font-size: 12px; word-break:break-all; padding: 5px 0; border-radius: 0 0 0 10px; font-family: "Microsoft Yahei",SimHei,"Helvetica Neue","DejaVu Sans",Tahoma; pointer-events:none; } #mobileConsole p{ padding: 4px 10px; margin: 0; border-bottom: 1px rgba(255,255,255,.1) solid;} </style>';

        function isJson(obj){
            var isjson = typeof(obj) == "object" && Object.prototype.toString.call(obj).toLowerCase() == "[object object]" && !obj.length;
            return isjson;
        }
        //创建dom结构和样式
        if( $('#mobileConsole').length==0 ){
            $('body').append('<div id="mobileConsole"></div>');
            $('body').append(mobileConsoleStyle);
        }
        //如果为json对象，则字符串化
        consoleContent=content;
        if(isJson(content)){
            consoleContent=JSON.stringify(content);
        }
        //填入数据
        $('#mobileConsole').prepend('<p>'+consoleContent+'</p>');
    },
    getData:function(options){
        var settings={
            type: 'get',
            timeout:10000,
            dataType:"json",
            error:function(){
                $.alert('网速不给力啊，请稍后重试！',6000);
            }
        };
        $.extend(settings,options);
        $.ajax({
            type: settings.type,
            url: settings.url,
            data : settings.data,
            dataType:settings.dataType,
            timeout:settings.timeout,
            success: function(data){
                settings.success(data);
            },
            error:function(){
                settings.error();
            }
        })
    },

    addTitleBar:function(title){
        var from = $.getQueryString("dt_from");
        title = title.replace(/\+/g,' ');
        if(from == "web"){
            if($(".title").length>0){
                return;
            }
            var titleDOm = document.createElement("div");
            titleDOm.className = "title";
            titleDOm.style.cssText = "text-align: center; font-size: 16px;color: #595959;background: #f6f6f6;border-bottom: 1px solid #ececec; height: 40px;display: block; line-height: 40px;";
            titleDOm.innerHTML = title;
            $("body").prepend(titleDOm);
        }else{
            try {
                if(typeof NativeProxy != "undefined"){
                    NativeProxy.setTitle(title);
                }else{
                    beaconApi.exec("setTitle",{"title":title},function(data){
                    });
                }
            } catch (e) {
                console.log('NativeProxy must be executed in app');
            }
        }
    },
    addInstallBar:function(){
        var installHtml='<div id="installBar" class="install_bar installed"><div class="install_close" id="installBarClose"><span></span></div><div class="install_logo"></div><div class="install_txt"><div class="install_title">灯塔</div><p class="install_desc install_desc1">已安装</p><p class="install_desc install_desc2">你的股票投资明灯</p></div><div class="install_link"><a href="#" class="install_link1">打开</a><a href="#" class="install_link2">下载app</a></div></div>';
        $("body").append(installHtml);
        $("body").addClass('from_web');
        $('#installBarClose').on('tap',function(){
            $('#installBar').addClass('none');
        })
    },
    showLoading:function(options){
        var settings={
            obj:null,
            height:'auto',
            isBig:true,
            center:false
        };
        var height;
        $.extend(settings,options);
        if(settings.height=='auto'){
            height="auto;";
        }else if(settings.height!='auto' && settings.isBig){
            height=settings.height-40+'px;';
        }else if(settings.height!='auto' && !settings.isBig){
            height=settings.height+'px;';
        }
        if(settings.center){
            height+=' position:absolute; left:0; top:0; width:100%; height:100%; padding: 0; display:-webkit-box; -webkit-box-pack:center; -webkit-box-align:center; -webkit-box-orient:vertical';
        }
        settings.obj.html('').prepend( "<div class=\"loading loading_big rotating\" style=\"height:"+height+"\"><i></i><span>加载中...</span></div>");
    },
    hideLoading:function(obj){
        obj.find('.loading').remove();
    },
    stockDetailSkip:function(secCode,secName){
        if(secCode&&secName){
            if($.getQueryString("dt_from") == "web"){
                location.href = $.getAjaxHost("commonHref")+"stockDetail.html?secCode="+secCode+"&secName="+secName+"&dt_from=web";
            }else if($.getQueryString("dt_from") == "app"){
                location.href = "beacon://stock?id="+secCode+"&name="+secName;
            }else{
                location.href = "beacon://stock?id="+secCode+"&name="+secName;
            }
        }
    },
    getAjaxHost:function(type){
        var nowUrl = location.href;
        if(nowUrl.indexOf("192.168")!=-1||nowUrl.indexOf("node.dengtacj")!=-1||nowUrl.indexOf("59.172.4.154")!=-1){
            if(type == "news"){
                return "http://59.172.4.154:55551/";
            }else if(type == "common"){
                return "http://59.172.4.154:55553/";
            }else if(type == "newsHref"){
                return "http://node.dengtacj.cn/";
            }else if(type == "commonHref"){
                return "http://node.dengtacj.cn/";
            }else{
                return "http://node.dengtacj.cn/";
            }
        }else if(nowUrl.indexOf("wedengta")!=-1){
            if(type == "news"){
                return "http://news.wedengta.com/";
            }else if(type == "common"){
                return "http://sec.wedengta.com/";
            }else if(type == "newsHref"){
                return "http://news.wedengta.com/";
            }else if(type == "commonHref"){
                return "http://sec.wedengta.com/";
            }else{
                return "http://sec.wedengta.com/";
            }
        }else if(nowUrl.indexOf("dengtacjw")!=-1){
            if(type == "news"){
                return "http://news.dengtacjw.cn/";
            }else if(type == "common"){
                return "http://sec.dengtacjw.cn/";
            }else if(type == "newsHref"){
                return "http://news.dengtacjw.cn/";
            }else if(type == "commonHref"){
                return "http://sec.dengtacjw.cn/";
            }else{
                return "http://sec.dengtacjw.cn/";
            }
        }
    },
    weixinSDKRegister:function(weixinOptions){
        if(typeof wx != "undefined"&&$.getQueryString("dt_from") == "web"){
            var locationUrl = location.href;
            var options = {
                url:$.getAjaxHost("common")+'getWxConfig',
                data:{
                    action:"JsConfig",
                    shareUrl:encodeURIComponent(locationUrl)
                },
                success:function(data){
                        var dataContent = JSON.parse(data.content);
                        wx.config({
                            debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                            appId: dataContent.sAppID, // 必填，公众号的唯一标识
                            timestamp: dataContent.lTimeStamp, // 必填，生成签名的时间戳
                            nonceStr: dataContent.sNonceStr, // 必填，生成签名的随机串
                            signature: dataContent.sSignature,// 必填，签名，见附录1
                            jsApiList: ['onMenuShareTimeline','onMenuShareAppMessage','onMenuShareQQ'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
                        });
                        var defaultOptions = {
                            title:"灯塔",
                            desc:"指引钱途",
                            imgUrl:"http://res.idtcdn.com/icon.jpg",
                            link:location.href,
                            type:"link",
                            dataUrl:""
                        };
                        $.extend(defaultOptions,weixinOptions);
                        wx.ready(function(){
                            wx.onMenuShareTimeline({
                                title: defaultOptions.title, // 分享标题
                                link: defaultOptions.link, // 分享链接
                                imgUrl: defaultOptions.imgUrl, // 分享图标
                                success: function () {
                                    // 用户确认分享后执行的回调函数
                                },
                                cancel: function () {
                                    // 用户取消分享后执行的回调函数
                                }
                            });

                            wx.onMenuShareAppMessage({
                                title: defaultOptions.title, // 分享标题
                                desc: defaultOptions.desc, // 分享描述
                                link: defaultOptions.link, // 分享链接
                                imgUrl: defaultOptions.imgUrl, // 分享图标
                                type: defaultOptions.type, // 分享类型,music、video或link，不填默认为link
                                dataUrl: defaultOptions.dataUrl, // 如果type是music或video，则要提供数据链接，默认为空
                                success: function () {
                                    // 用户确认分享后执行的回调函数
                                },
                                cancel: function () {
                                    // 用户取消分享后执行的回调函数
                                }
                            });

                            wx.onMenuShareQQ({
                                title: defaultOptions.title, // 分享标题
                                desc: defaultOptions.desc, // 分享描述
                                link: defaultOptions.link, // 分享链接
                                imgUrl: defaultOptions.imgUrl, // 分享图标
                                success: function () {
                                    // 用户确认分享后执行的回调函数
                                },
                                cancel: function () {
                                    // 用户取消分享后执行的回调函数
                                }
                            });
                        });
                        wx.error(function(res){

                        });
                },
                error:function(data){
                }
            };
            $.getData(options);
        }
    }
});
(function(){
    Array.prototype.max = function(){
        return Math.max.apply({},this)
    }
    Array.prototype.min = function(){
        return Math.min.apply({},this)
    }

    //ios设备做0.5像素线兼容
    var isIOS=function(){
        var u = navigator.userAgent;
        if( !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/) ){
            $('html').addClass('ios');
        }
    }
    isIOS();
    //触摸设备和非触摸设备的样式区别处理
    var touchDevice=function(){
        if( ('ontouchstart' in window)==false ){
            $('html').addClass('no_touch_device');
        }
    }
    touchDevice();
})();
//接口协议代码
;(function () {
    var api = {};
    var cmdQueue = {};
    var protocol = "beacon";
    var index = 0;
    api.exec = function(cmd,option,callback){
        ++index;
        var callbackId = new Date().getTime()+"|"+index;
        option = option||{};
        cmdQueue[callbackId] = callback;
        var nativeOption = {
            callbackId:callbackId,
            option:option
        }
        var promptContent =  protocol+"://["+cmd+"-"+JSON.stringify(nativeOption)+"]";
        prompt(protocol,promptContent);
    }

    window.AppCallback = function(callbackId,option){
        if(cmdQueue[callbackId])
        {
            cmdQueue[callbackId](option);
        }
    };

    window[protocol+"Api"] = api;

    //客户端预埋接口
    window.ReqWeb = function (key, value) {
        if(mNativeFunction[key] != undefined ){
            mNativeFunction[key](value);
        }
    }

    window.mNativeFunction = {};

    //监听native
    window.lisenNative = function (key, func) {
        mNativeFunction[key] = func;
    }
})();


