var sample_bn_speed = 0; //움직임 속도
var sample_bn_time; 
var sample_bn_timeSpeed = 3000; //setInterval 함수 반복실행 시간(해당시간마다 sample_bn_after펑션을 호출한다.)
var sample_bn_auto = "N";//자동재생을 설정한다.(Y = 자동재생, N = 효과없음)
function sample_bn_AC(sel){
	var obj = jQuery("."+sel); 
	var after_bt = obj.find(">p, >span, >a").eq(0); 
	var before_bt = obj.find(">p, >span, >a").eq(1); 
	var bn_play = obj.find(" .sample_bn_play"); 
	var bn_stop = obj.find(" .sample_bn_stop"); 
	

	obj.attr("clickCheck","on");
	
	after_bt.click(function(){sample_bn_after(obj);return false;});
	before_bt.click(function(){sample_bn_before(obj);return false;});
	

	bn_play.click(function(){sample_bn_play(obj);return false;});
	bn_stop.click(function(){sample_bn_stop(obj);return false;});
	
	if(sample_bn_auto == "Y"){
		sample_bn_play(obj);
	}
}

function sample_bn_after(obj){
	if(obj.attr("clickCheck") == "on"){
		sample_bn_stop(obj);
		obj.attr("clickCheck","off");
		var move_obj = jQuery(obj).find(" > div > ul"); 
		var move_obj_width = (move_obj.find(">li").width() * -1); 
		
		move_obj.find(">li").eq(0).clone().appendTo(move_obj);
		move_obj.animate(
			{left:move_obj_width}
			,sample_bn_speed
			,function(){


				move_obj.find(">li").eq(0).remove();
				move_obj.css("left","0");


				obj.attr("clickCheck","on");
				
				if(sample_bn_auto == "Y"){
					sample_bn_play(obj);
				}
			}
		);
	}
}

function sample_bn_before(obj){
	if(obj.attr("clickCheck") == "on"){
		sample_bn_stop(obj);
		obj.attr("clickCheck","off");
		var move_obj = jQuery(obj).find(" > div > ul");
		var move_obj_width = (move_obj.find(">li").width() * -1);
		
		move_obj.find(">li:last").clone().prependTo(move_obj);
		move_obj.css("left",move_obj_width+"px");
		
		move_obj.animate(
			{left:0}
			,sample_bn_speed
			,function(){
				move_obj.find(">li:last").remove();
				obj.attr("clickCheck","on");
				if(sample_bn_auto == "Y"){
					sample_bn_play(obj);
				}
			}
		);
	}
}

function sample_bn_play(obj){
	sample_bn_stop(obj);
	
	jQuery(obj).attr("timer",setInterval(function(){sample_bn_after(obj)},sample_bn_timeSpeed));
	return false;
}

function sample_bn_stop(obj){

	clearInterval(jQuery(obj).attr("timer"));
	return false;
}
jQuery(document).ready(function() {
sample_bn_AC("sample_bn");//sample_bn 호출!
sample_bn_AC("sample_bn2");
sample_bn_AC("sample_bn3");
sample_bn_AC("sample_bn4");
sample_bn_AC("sample_bn5");
sample_bn_AC("sample_bn6");
sample_bn_AC("sample_bn7");

sample_bn_AC("sample_gnb_s1");
sample_bn_AC("sample_gnb_s2");
sample_bn_AC("sample_gnb_s3");
sample_bn_AC("sample_gnb_s4");
sample_bn_AC("sample_gnb_s5");
sample_bn_AC("sample_gnb_s6");
sample_bn_AC("sample_gnb_s7");

sample_bn_AC("sample_bn_lately");

sample_bn_AC("sample_bn_d_tab");

});