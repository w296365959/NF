(function(){
    function feedback(){
        this.serverUrl = "http://feedback.dengtacj.cn/";
    }

    var fn = feedback.prototype;
    fn.init = function(){
        $.addTitleBar("意见反馈");
        this.addEvent();
    }

    fn.showConfirmBox = function(){
        $('#popConfirm').show();
        $('#feedbackMsg').val("");
        $('#feedbackContact').val("");
    }

    fn.setFeedbackInfo = function(msg,contact,accountInfo){
        var that = this;
        var options = {
            url:this.serverUrl+'feedback',
            type:"post",
            data:{
                msg:msg,
                contact:contact,
                dtid:accountInfo.dtid,
                dtticket:accountInfo.dtticket,
                "DT-UA":accountInfo['DT-UA'],
                "DT-GUID":accountInfo['DT-GUID']
            },
            success:function(data){
                if(data.ret == 0){
                    that.showConfirmBox();
                }else{
                    $.alert('数据异常，请稍后重试！');
                }
            },
            error:function(data){
                $.alert('网络异常，请稍后重试！');
            }
        }
        $.getData(options);
    };

    fn.addEvent = function(){
        var that=this;
        //提交事件
        $('#btnSubmit').tap(function(){
            if($(this).hasClass("disabled")){
                return;
            }
            var msg=$.trim($('#feedbackMsg').val());
            if(msg == ""){
                $.alert("反馈内容不能为空！");
                return;
            }

            if(msg.length>400){
                $.alert("反馈内容过长！");
            }
            var contact=$('#feedbackContact').val();
            try {
                if(typeof NativeProxy != "undefined"){
                    var accountInfo = NativeProxy.getAccountInfo();
                    accountInfo  =  JSON.parse(accountInfo);
                    that.setFeedbackInfo(msg,contact,accountInfo);
                }else{
                    beaconApi.exec("getAccountInfo",{},function(data){
                        that.setFeedbackInfo(msg,contact,data);
                    });
                }
            } catch (e) {
                var accountInfo = {
                    dtid:"",
                    dtticket:"",
                    "DT-UA":"",
                    "DT-GUID":""
                }
                that.setFeedbackInfo(msg,contact,accountInfo);
            }
        });
        //输入事件
        $('#feedbackMsg').on('propertychange', function(event) {
            var currentLength=$(this).val().length;
            $('#charLeft').html(400-currentLength);
            if($('#feedbackMsg').val() == ""){
                $('#btnSubmit').addClass("disabled");
            }else{
                $('#btnSubmit').removeClass("disabled");
            }
        });
        $('#feedbackMsg').on('input', function(event) {
            var currentLength=$(this).val().length;
            $('#charLeft').html(400-currentLength);
            if($('#feedbackMsg').val() == ""){
                $('#btnSubmit').addClass("disabled");
            }else{
                $('#btnSubmit').removeClass("disabled");
            }
        });

        $('#confirmBtn').on("tap",function(){
            try {
                if(typeof NativeProxy != "undefined"){
                    NativeProxy.finish();
                }else{
                    beaconApi.exec("closeWindow",{},function(data){
                    });
                }
            } catch (e) {
            }
            $('#popConfirm').hide();
        });
    }
    var feedbackAction = new feedback();
    feedbackAction.init();
}());