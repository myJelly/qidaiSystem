$.ajaxSetup({ xhrFields: { withCredentials: true }, crossDomain: true });//添加跨域头
$(document).ready(function(){
	  var  baseurl = 'http://cheewp.duapp.com/WeChat/';
	  var id;//定义一个全局变量来跨函数运用userid
	  var friend_id;//定义一个全局变量获取好友id
	  var frieng_name;//定义一个全局变量获取好友昵称
	  var MenID;//获取点击当前好友的id
	  var Title_Name;//目的来修改聊天内容昵称
	  var _interval;         //停止计时器
	  var myname;
/*********************聊天界面**************************/
/***********聊天初始界面***********/
$(".chat-view").click(function(){
	$("#view-photo").attr("src","../static/image/greenwe_07.png");
	$("#contact-photo").attr("src","../static/image/liaxni_04.png");
	$(".menber-orign").hide();
	$(".box1").hide();
	$(".correct").hide();
	$(".dropdown-menu").hide();
	$(".box2").hide();
	$(".slide-online").hide();
	$(".chating").show();
	$(".mmmpop-profile").hide();
	if ($(".box_mainwrap").length>0) {
		$("#chatArea").show();
		$(".chat-orign").hide();
	}
	else{
		$("#chatArea").hide();
		$(".chat-orign").show();
	}
});
/******************聊天窗口******************************/
$("#act_sent").click(function(){
    $("#view-photo").attr("src","../static/image/greenwe_07.png");
	$("#contact-photo").attr("src","../static/image/liaxni_04.png");
	$("#chatArea").show();
    $(".menber-orign").hide();
	$(".chat-orign").hide();
	$(".box1").hide();
	$(".correct").hide();
	$(".dropdown-menu").hide();
	$(".box2").hide();
	$(".slide-online").hide();
	$(".chating").show();
	$(".mmmpop-profile").hide();
	//聊天窗口顶部显示好友昵称
	$("#CA-nickname").text(frieng_name);
	//发送信息
	Add2(friend_id);
	//把这模块添加到正在聊天
	Add(friend_id);
});
/**************聊天内容发送************************/
//发送按钮按enter键发送
$(".box_ft").keydown(function(e){
    if (e.ctrlKey && e.which == 13 || e.which == 10) {
    	$(".btn_send").trigger("click");
    }
});
$(".btn_send").click(function(){
var $content = $("#send-frame"); //发送内容  
if ($content.val() != "") {
  	send($content.val());
  	$(".box_main").scrollTop($(".box_main").height());
  }
  else{
  	// alert("发送内容不能为空!");
  	$content.focus();
  	return false;
  }
});


/****************聊天记录显示*********************/
$(".checkchat").click(function(){
	$(".history").show();
	$("#chatArea").hide();
	if ($("#"+MenID+'123').length>0) {
       Recentnews(MenID);
       // $(".his_mainbody").hide();
       // $("#"+MenID+'123').show();
       $("#"+MenID+'123').css("display","block").siblings("div").css("display","none");
	}
	else{
		var body = '<div class="his_mainbody" id="'+MenID+'123">'+'</div>';
		$(".history_main").append(body);
		 Recentnews(MenID);
		// $(".his_mainbody").hide();
  //      $("#"+MenID+'123').show();
       $("#"+MenID+'123').css("display","block").siblings("div").css("display","none");
	}
})
$(".close").click(function(){
	$(".history").hide();
	$("#chatArea").show();
})
/*********************以下的全为函数部分********************/
//获取聊天记录
function Recentnews(ID){
	$.ajax({
		type:"POST",
		url:baseurl+'?action=Message&method=getRecentByUserId',
		data:{
			userid:ID,
			number:20,
		},
		success:function(data){
             var obj = eval("("+data+")");
             // console.log(data);
              for (var i = obj.length-1; i >0; i--) {
              	var toUserId = obj[i].toUserId;
              	var fromUserId = obj[i].fromUserId;
              	var messigeId = obj[i].messigeId;
              	var content = obj[i].content;
              	var time = obj[i].time;
              	if (ID==fromUserId) {
              		var meg = '<div class="you" id='+messigeId+'>'+
                                '<div class="clearfix">'+
                                  '<div class="you-hd">'+
                                    '<span class="you-hd-name">'+fname+'</span>'+
                                     '<span class="you-hd-time">'+time+'</span>'+
                                   '</div>'+
                              '<div class="you-content">'+
                             '<pre>'+content+'</pre>'+
                              '</div>'+
                            '</div>'+
                          '</div>';
                      $("#"+fromUserId+'123').append(meg); 
                      // console.log("B="+B);
                      $("#"+fromUserId+'123').scrollTop( $("#"+fromUserId+'123')[0].scrollHeight );
              	}
              	else{
              	var meg = '<div class="me" id='+messigeId+'>'+
                             '<div class="clearfix">'+
                                '<div class="me-hd">'+
                                  '<span class="me-hd-name">'+myname+'</span>'+
                                  '<span class="me-hd-time">'+time+'</span>'+
                                '</div>'+
                             '<div class="me-content">'+
                                 '<pre>'+content+'</pre>'+
                             '</div>'+
                           '</div>'+
                        '</div>';
                     $("#"+toUserId+'123').append(meg);
                      $("#"+toUserId+'123').scrollTop( $("#"+toUserId+'123')[0].scrollHeight );
              	}
              }
          }
		
	})
} 

/***************************表情模块************************/
//自定义设置表情图标函数
function InitFace() {
   var strHTML = "";
   for (var i = 1; i <=60; i++) {
   	strHTML ="<li>"+"<a>"+"<img src='../static/image/emo_" + i + ".gif' id='"+i+"'/>"+"</a>"+"</li>";
   $(".faces_main ul").append(strHTML);
   }
}
//绑定单击表情事件
$(document).on("click",".faces_main ul li a img",function(){
	var strContent = $("#send-frame").val() +  "<img src='" + this.src + "'>";
	$("#send-frame").val(strContent);
})
$(".face").click(function(){
	if ($(".faces_main ul li a img").length>0) {$(".faces_box").toggle();}
	else{
	InitFace();//获取表情
	$(".faces_box").toggle();}
})
/************************过滤消息******************/
function strFilter(str){
  // str = str.replace(/<\/?(?!img\b)[a-z]+[^>]*>/ig,'');
  str = str.replace(/<\/?(?!img\b)[a-z\d]+[^>]*>/ig, function ($0) { return $0.replace(/</g, '&lt;').replace(/>/g, '&gt;') });
  return str;
}
/*********************添加红点*****************************/
function judgereddot(ID) {
					    if ($("#"+ID+'222').is(":hidden")) {			    
					      $(".reddot").show();
					      $("#"+ID+'777').show();					      
					      getMeg(ID);
				         }
				        else{				     	
				     	 getMeg(ID);
				     	 $("#CA-nickname").text(Title_Name);
				     	 $("."+ID).css("background","#3A3F45").siblings("div").css("background","#2e3238");
				        }
				      }
/************************************************************/
});


